package pack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class JSkillMeter extends JPanel implements ActionListener {

	final int FRAME_WIDTH = 600;
	final int FRAME_HEIGHT = 600;
	final int BUTTON_HEIGHT = 40;
	final int BUTTON_WIDTH = 140;
	
	
	private long tempsMoy;
	private long tempsTotal;
	private long tempsStart;
	private long tempsApparition;
	private long tempsClick;
	private JButton mBouton;
	private Random random = new Random();
	private int counterGame, refCounter;
	private String btnStartText, msg1, msg2, msg4;
	private Properties props, rankProps;
	private MessageFormat msgFormat;
	private JInternalFrame parentIFrame;
	
	public JSkillMeter(JInternalFrame iframe){
		parentIFrame = iframe;
		// chargement du fichier config.propoerties
		props = new Properties();
		try{
			this.setLocale(Locale.ENGLISH);
			String fileConf = "config.properties";
			if(this.getLocale().equals(Locale.ENGLISH))
				fileConf = "config_en.properties";
			props.load(new InputStreamReader(new FileInputStream(fileConf),"UTF8"));
			
			counterGame = Integer.parseInt(props.getProperty("skillmeter.countergame", "10"));
			refCounter = counterGame;
			//props.getProperty("skillmeter.gamedesc");
			
			msgFormat = new MessageFormat(props.getProperty("skillmeter.coutsrestant"));
			Object[] msg1Param = {new Integer(counterGame), new Integer(refCounter)};
			msg1 = msgFormat.format(msg1Param);
			msg2 = props.getProperty("skillmeter.msgfinpartie");
			props.getProperty("skillmeter.titredialog");
			msg4 = props.getProperty("skillmeter.inputname");
			btnStartText = props.getProperty("skillmeter.btnstart");
						
		}catch(IOException e){
			e.printStackTrace();
		}
		
		parentIFrame.setTitle("Skill Meter ("+msg1+")");
		this.setLayout(null);
		this.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		this.setMaximumSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		this.setMinimumSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		this.setBackground(Color.WHITE);
		
		
		mBouton = new JButton(btnStartText);
		mBouton.setBounds((FRAME_WIDTH-BUTTON_WIDTH)/2, (FRAME_HEIGHT-BUTTON_HEIGHT)/2, BUTTON_WIDTH, BUTTON_HEIGHT);
		mBouton.setBorderPainted(false);
		mBouton.addActionListener(this);
		
		this.add(mBouton);
		tempsTotal = 0;
		tempsStart = System.currentTimeMillis();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(!e.getActionCommand().equals("")){
			mBouton.setText("");
			mBouton.setSize(150, 150);
			mBouton.setIcon(new ImageIcon("image//icone2.jpg"));
			mBouton.setBackground(Color.WHITE);
			int dx = random.nextInt(JSkillMeter.this.getWidth()-100);
			int dy = random.nextInt(JSkillMeter.this.getHeight()-100);
			mBouton.setLocation(dx, dy);
			return;
		}
			
		counterGame--;
		Object[] msg1Param = {new Integer(counterGame), new Integer(refCounter)};
		msg1 = msgFormat.format(msg1Param);
		parentIFrame.setTitle("Skill Meter ("+msg1+")");
		if(counterGame < 1){
			mBouton.setEnabled(false);
			mBouton.setBackground(Color.GRAY);
			tempsTotal -= tempsStart;
			tempsMoy = tempsTotal/9;

			//msg2 = msg2.replace("##VAR1##", new Date(tempsTotal).toString());
			//msg2 = msg2.replace("##VAR2##", new Date(tempsMoy).toString());
			rankProps = new Properties();
			List<Scoring> scores = new ArrayList<Scoring>(10);
			try{
				FileReader fr = new FileReader("top10win.properties");
				rankProps.load(fr);
				for(int i=1;i<=10;i++){
					StringTokenizer newScore = new StringTokenizer(rankProps.getProperty("rank"+i),"|");
					scores.add(new Scoring(newScore.nextToken(),Long.parseLong(newScore.nextToken())));
				}
				fr.close();
				for(int i=0;i<scores.size();i++)
					System.out.println(scores.get(i).getNom()+"  "+scores.get(i).getTemps());
				
				//if(tempsTotal < scores.get(0).getTemps()){
					String vNom = JOptionPane.showInputDialog(this, msg4);
					if(vNom == null || vNom.equals("")) vNom = "anonyme";
					scores.get(0).setNom(vNom);
					scores.get(0).setTemps(tempsTotal);
					Collections.sort(scores);//, Collections.reverseOrder());
					Object[][] showData = new Object[10][3];
					for(int i=0;i<scores.size();i++){
						rankProps.setProperty("rank"+(i+1), scores.get(i).toString());
						showData[i][0] = i+1;
						showData[i][1] = scores.get(scores.size()-1-i).getNom();
						showData[i][2] = new Date(scores.get(scores.size()-1-i).getTemps());
					}
					String[] tableColumnName = {"#","Nom","Temps"};
					JTable table = new JTable(showData, tableColumnName);
					
					JOptionPane.showMessageDialog(this, new JScrollPane(table));
					FileWriter fw = new FileWriter("top10win.properties");
					rankProps.store(fw, "top 10 SkillMeter");
					fw.close();
				//}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			return;
		}
		

		mBouton.setVisible(false);
		// attendre un laps de temps avant de rendre le bouton visible.
		new Thread(){
			public void run(){
				try{
					Thread.sleep(random.nextInt(3000));
					int dx = random.nextInt(JSkillMeter.this.getWidth()-BUTTON_WIDTH);
					int dy = random.nextInt(JSkillMeter.this.getHeight()-BUTTON_HEIGHT);
					mBouton.setVisible(true);
					tempsApparition = System.currentTimeMillis();
					mBouton.setLocation(dx, dy);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}.start();
		tempsClick = System.currentTimeMillis();
		tempsTotal += tempsClick - tempsApparition;
		
	}
	private class Date{
		private long minute;
		private long second;
		private long milSec;
		
		Date(long time){
			milSec = time % 1000;
			second = (time/1000)%60;
			minute = ((time/1000)/60)%60;
		}
		public String toString(){
			String strMinute = (minute<10)?"0"+minute:""+minute;
			String strSecond = (second<10)?"0"+second:""+second;
			String strMilSec = (milSec<10)?"0"+minute:""+milSec;
			return strMinute+":"+strSecond+"'"+strMilSec;
		}
	}
	private class Scoring implements Comparable<Object>{
		private String nom;
		private long temps;
		
		public Scoring(String n, long t){
			nom = n;
			temps = t;
		}
		public String getNom(){ return nom;}
		public long getTemps(){ return temps;}
		public void setNom(String n){nom = n;}
		public void setTemps(long t){temps = t;}
		@Override
		public int compareTo(Object o) {
//			return String.valueOf(this.getTemps()).compareTo(((Scoring)o).getTemps());
			if(this.getTemps() > ((Scoring)o).getTemps())
				return 1;
			else if(this.getTemps() < ((Scoring)o).getTemps())
				return -1;
			else
				return 0;
		}
		public String toString(){
			return nom+"|"+temps;
		}
	}
	
	
	public static void main(String[] args) {
		//new JSkillMeter();
	}

}
