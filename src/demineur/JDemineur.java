package demineur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
/**
*
* @author nabil
*/
@SuppressWarnings("serial")
public class JDemineur extends JPanel implements ActionListener{

	
	private int nbrCase;
	private int nbrBombe;
	private long tGame;	//temps de fin partie
	private int cptVisitedCase;
		
	private JMenu menuPartie;
	private JMenu menuHelp;
	private JMenuItem itemNewGame;
	private JMenuItem itemSeek;
	private JMenuItem itemExit;
	private JMenuItem itemAbout;
	private JMenuItem itemHelp;
	private JButton btnSeek;
	private JLabel lblCompteurBombe, lblTempsJeu;
	
	private CaseDemineur[][] grille;
	//private DemineurEngine engine;
	private Properties prop;
	private Container content;
	private JPanel panelGrille, panelHeader;
	private Timer timer;
	private ArrayList<Point> vecBombe;
	
	private Point[] voisins = new Point[]{
			new Point(-1,-1),
			new Point(-1,0),
			new Point(-1,1),
			new Point(0,-1),
			new Point(0,1),
			new Point(1,-1),
			new Point(1,0),
			new Point(1,1)
		};
	
	public JDemineur(int level){
		//setTitle("Mini-Demineur");
		this.setLayout(new BorderLayout());
		
//		this.setMaximumSize(new Dimension(WIDTH_FRAME, HEIGHT_FRAME));
//		this.setMinimumSize(new Dimension(WIDTH_FRAME, HEIGHT_FRAME));
//		this.setPreferredSize(new Dimension(WIDTH_FRAME, HEIGHT_FRAME));
		//this.setLocationRelativeTo(null);
		//this.setResizable(false);
		
		
		// chargement de la configuration du  jeu
		prop = new Properties();
		try{
			FileReader fr = new FileReader("config.properties");
			prop.load(fr);
			fr.close();
			nbrCase  = 6; //Integer.parseInt(prop.getProperty("nbrcase", "6"));
			nbrBombe = level; //Integer.parseInt(prop.getProperty("nbrbombe", "6"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		// creation du menu de la fenetre
		content = this;//.getContentPane();
		vecBombe = new ArrayList<Point>();
		
		panelHeader = new JPanel(new GridLayout(1,3,10,40));
		lblCompteurBombe 	= new JLabel(String.valueOf(nbrBombe));
		lblCompteurBombe.setIcon(new ImageIcon("icons//bombe.png"));
		lblCompteurBombe.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompteurBombe.setFont(new Font("Arial", Font.BOLD, 35));
		//lblCompteurBombe.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		lblTempsJeu 		= new JLabel("000");
		lblTempsJeu.setHorizontalAlignment(SwingConstants.CENTER);
		lblTempsJeu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		lblTempsJeu.setOpaque(true);
		lblTempsJeu.setBackground(Color.BLACK);
		lblTempsJeu.setForeground(Color.red);
		lblTempsJeu.setFont(new Font("Arial", Font.BOLD, 35));
		btnSeek	= new JButton(new ImageIcon("icons//find.png")); //prop.getProperty("btnseek", "DÃ©couvrir"),
		btnSeek.addActionListener(this);
		panelHeader.add(lblCompteurBombe);
		panelHeader.add(btnSeek);
		panelHeader.add(lblTempsJeu);
		panelHeader.setBackground(Color.WHITE);
		
		grille = new CaseDemineur[nbrCase][nbrCase];
		
		panelGrille = new JPanel(new GridLayout(nbrCase,nbrCase));
		panelGrille.setMaximumSize(new Dimension(10*nbrCase,10*nbrCase));
		panelGrille.setMinimumSize(new Dimension(10*nbrCase,10*nbrCase));
		panelGrille.setPreferredSize(new Dimension(10*nbrCase,10*nbrCase));
		//init grille
		for(int i=0;i<nbrCase;i++){
			for(int j=0;j<nbrCase;j++){
				grille[i][j] = new CaseDemineur();
				grille[i][j].setPosition(i, j);
				grille[i][j].addActionListener(this);
				panelGrille.add(grille[i][j]);
			}
		}
		generateBombe();
		
		content.add(panelHeader, BorderLayout.NORTH);
		content.add(panelGrille, BorderLayout.CENTER);
		//timer pour compter le timing du jeu
		timer = new Timer(1000,new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				String temps = (tGame<10)?"00"+tGame:((tGame<100)?"0"+tGame:""+tGame);
				lblTempsJeu.setText(temps);
				tGame++;
			}
		});
		timer.start();
		
//		this.setVisible(true);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.doLayout();
//		this.pack();
	}
	public void generateBombe(){
		Random rand = new Random();
		int ri, rj;
		for(int k=0; k<nbrBombe; k++){
			ri = rand.nextInt(nbrCase);
			rj = rand.nextInt(nbrCase);
			if(!vecBombe.contains(new Point(ri, rj)))
				vecBombe.add(new Point(ri, rj));
			else{
				k--;
				continue;
			}
			grille[ri][rj].setBombe(true);
		}
	}
	
	private void disableGrille(){
		for(Point p: vecBombe){
			grille[p.x][p.y].setEnabled(false);
			grille[p.x][p.y].setIcon(new ImageIcon("icons/bombe.png"));
		}
		for(int i=0; i<grille.length; i++){
			for(int j=0; j<grille[i].length; j++){
				if(grille[i][j].isEnabled())
					grille[i][j].setEnabled(false);
			}
		}
	}
	
	public void setNombreCase(int nbrc){
		nbrCase = nbrc;
	}
	public void actionPerformed(ActionEvent evt){
		
		if( evt.getSource() instanceof CaseDemineur){
			CaseDemineur btn = (CaseDemineur)evt.getSource();
			//System.out.println("je suis le bouton "+btn.getPosition());
			if(btn.isBombe()){
				if(vecBombe.contains(btn.getPosition())){
					vecBombe.remove(btn.getPosition());
					lblCompteurBombe.setText(String.valueOf(vecBombe.size()));
					//System.out.println("je suis une bombe");
				}
				if(vecBombe.size() == 0){
					timer.stop();
					new Thread(){
						public void run(){
							JOptionPane.showMessageDialog(null, "Vous avez perdu. x_x","MiniDeminineur Message", JOptionPane.WARNING_MESSAGE);
						}
					}.start();
					
					disableGrille();
					btnSeek.removeActionListener(this);
					itemSeek.removeActionListener(this);
					return;
				}
			}else{
				cptVisitedCase++;
				int x = btn.getPosition().x;
				int y = btn.getPosition().y;
				int cptBV = 0;
				
				for(int i=0; i<8; i++){
					try{
						if(grille[x+voisins[i].x][y+voisins[i].y].isBombe())
							cptBV++;
					}catch(IndexOutOfBoundsException e){
						continue;
					}
				}
				btn.setText(String.valueOf(cptBV));
				if(cptVisitedCase == (nbrCase*nbrCase-nbrBombe)){
					timer.stop();
					new Thread(){
						public void run(){
							JOptionPane.showMessageDialog(null, "Fin du jeu avec "+(nbrBombe-vecBombe.size())+" erreur(s)", "Mini-Demineur Message", JOptionPane.INFORMATION_MESSAGE);
						}
					}.start();
					
					disableGrille();
					btnSeek.removeActionListener(this);
					itemSeek.removeActionListener(this);
					return;
				}
				//System.out.println(cptVisitedCase+" "+(nbrCase*nbrCase-nbrBombe));
			}
		}
		
		if(evt.getSource().equals(itemSeek) || evt.getSource().equals(btnSeek)){
			//System.out.println("decouvrir");
			
			new Thread(){
				public void run(){
					Color defaultColor = grille[vecBombe.get(0).x][vecBombe.get(0).y].getBackground();
					for(Point p: vecBombe){
						grille[p.x][p.y].setBackground(Color.ORANGE);
						try{
							Thread.sleep(200);
						}catch(InterruptedException e){	}
						grille[p.x][p.y].setBackground(defaultColor);
					}
				}
			}.start();
			
		}else if(evt.getSource().equals(itemNewGame)){
//			this.dispose();
			new JDemineur(6);
		}else if(evt.getSource().equals(itemHelp)){
			
		}else if(evt.getSource().equals(itemAbout)){
			
		}else if(evt.getSource().equals(itemExit)){
			if(JOptionPane.showConfirmDialog(null, "Voulez vous quitter le jeu?", "Quitter Mini-Demineur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
				System.exit(0);
			}
		}
	}
	public static void main(String[] args){
		new JDemineur(6);
		
	}
}
