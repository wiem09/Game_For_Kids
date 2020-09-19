import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import pack.JSkillMeter;
import demineur.JDemineur;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;



@SuppressWarnings("serial")
public class JDesktopFrame extends JFrame implements ActionListener{

	public static final int NIVEAU_FACILE = 6;
	public static final int NIVEAU_MOYEN = 9;
	public static final int NIVEAU_EXPERT = 12;
	public static final int WIDTH_FRAME_LEVEL1 = 520;
	public static final int HEIGHT_FRAME_LEVEL1 = 580;
	public static final int WIDTH_FRAME_LEVEL2 = 520;
	public static final int HEIGHT_FRAME_LEVEL2 = 580;
	public static final int WIDTH_FRAME_LEVEL3 = 520;
	public static final int HEIGHT_FRAME_LEVEL3 = 580;
	
	private JDesktopPane desktop;
	private JMenu fichier;
	//private JMenu edition;
	private JMenu ihm;
	private JMenu menuDemineur;
	private JMenu menuSkillMeter;
	private JMenu menuLevelDemineur;
	private JMenu segmentation;
	private JMenu aide;
	private JMenuItem itemDemineur;
	private JMenuItem itemSkillMeter;
	private JMenuItem itemOpenFile;
	private JMenuItem itemQuit;
        private JMenuItem itemhome;
	private JMenuItem itemAbout;
	private JCheckBoxMenuItem itemLevelDemineur1, itemLevelDemineur2, itemLevelDemineur3;
	private ButtonGroup groupLevel;
	private JFileChooser chooser;
	public JDesktopFrame(){
		this.setTitle("Games");
		//this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                this.setResizable(false);
                this.setSize(1000,700);
		this.setLocationRelativeTo(null);
		desktop = new JDesktopPane(){
                ImageIcon icon=new ImageIcon(getClass().getResource("/image/pi.jpg"));
                Image image=icon.getImage();
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image,0,0,getWidth(),getHeight(),this);
        //super.paintComponent(g);
        //g.setColor(new java.awt.Color(11, 131, 172));
        //g.fillRect(0, 0, getWidth(), getHeight());
    }
};
                
		this.setContentPane(desktop);
		
        chooser = new JFileChooser();
		
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		fichier = new JMenu("Home");
		fichier.setMnemonic(KeyEvent.VK_F);
		menubar.add(fichier);
//		edition = new JMenu("Edition");
//		edition.setMnemonic(KeyEvent.VK_E);
//		menubar.add(edition);
		ihm = new JMenu("Games/Levels");
		ihm.setMnemonic(KeyEvent.VK_I);
		menubar.add(ihm);

		aide = new JMenu("?");
		menubar.add(aide);

		fichier.add(new JSeparator());
                itemhome = new JMenuItem("Home Page", new ImageIcon("icons//home.png"));
                itemhome.addActionListener(this);
		fichier.add(itemhome);
		itemQuit = new JMenuItem("Close", new ImageIcon("icons//exit.png"));
		itemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		itemQuit.addActionListener(this);
		fichier.add(itemQuit);
		
		menuDemineur = new JMenu("Demineur");
		itemDemineur = new JMenuItem("New Game");
		itemDemineur.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		itemDemineur.addActionListener(this);
		menuLevelDemineur = new JMenu("Level");
		itemLevelDemineur1 = new JCheckBoxMenuItem("Easy", true);
		itemLevelDemineur2 = new JCheckBoxMenuItem("Medium");
		itemLevelDemineur3 = new JCheckBoxMenuItem("Expert");
		groupLevel = new ButtonGroup();
		groupLevel.add(itemLevelDemineur1);
		groupLevel.add(itemLevelDemineur2);
		groupLevel.add(itemLevelDemineur3);
		menuLevelDemineur.add(itemLevelDemineur1);
		menuLevelDemineur.add(itemLevelDemineur2);
		menuLevelDemineur.add(itemLevelDemineur3);
		menuDemineur.add(itemDemineur);	
		menuDemineur.add(menuLevelDemineur);
		
		menuSkillMeter = new JMenu("SkillMeter");
		itemSkillMeter = new JMenuItem("New Game");
		itemSkillMeter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		itemSkillMeter.addActionListener(this);
		menuSkillMeter.add(itemSkillMeter);
		
		
		ihm.add(menuDemineur);
		ihm.add(menuSkillMeter);
		
		
		itemAbout = new JMenuItem("About");
		itemAbout.setMnemonic(KeyEvent.VK_A);
		itemAbout.addActionListener(this);
		aide.add(itemAbout);
		// ajouter un internalFrame
		

//		
//		addInternalFrame(cDemineur, "Demineur");//, true, true, true, true));
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
                this.setLocationRelativeTo(null);
	}
	
	
	
	public void openDemineur(){
		final JInternalFrame iframe = new JInternalFrame("Demineur",false,true,false,true);
		int niveau = NIVEAU_FACILE;
		Enumeration<AbstractButton> levels = groupLevel.getElements();
		while(levels.hasMoreElements()){
			JCheckBoxMenuItem obj = (JCheckBoxMenuItem)levels.nextElement();
			if(obj.isSelected()){
				if(obj.getActionCommand().equals("Easy")){
					niveau = NIVEAU_FACILE;
					iframe.setSize(WIDTH_FRAME_LEVEL1, HEIGHT_FRAME_LEVEL1);
				}else if(obj.getActionCommand().equals("Medium")){
					niveau = NIVEAU_MOYEN;
					iframe.setSize(WIDTH_FRAME_LEVEL2, HEIGHT_FRAME_LEVEL2);
				}else if(obj.getActionCommand().equals("Expert")){
					niveau = NIVEAU_EXPERT;
					iframe.setSize(WIDTH_FRAME_LEVEL3, HEIGHT_FRAME_LEVEL3);
				}
			}
		}
                Dimension desktopSize = desktop.getSize();
                Dimension jInternalFrameSize = iframe.getSize();
                iframe.setLocation((desktopSize.width - jInternalFrameSize.width)/2,
    (desktopSize.height- jInternalFrameSize.height)/2);
		JDemineur cDemineur = new JDemineur(niveau);
		iframe.getContentPane().add(cDemineur);
		try{
			iframe.setSelected(true);
		}catch(PropertyVetoException e){}
		iframe.addVetoableChangeListener(new VetoableChangeListener() {
			@Override
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				String name = evt.getPropertyName();
				Object value = evt.getNewValue();
				if(name.equals("closed") && value.equals(Boolean.TRUE)){
					int choix = JOptionPane.showInternalConfirmDialog(iframe, "Do you want to close this window?");
					if(choix != JOptionPane.YES_OPTION){
						throw new PropertyVetoException("User refused to leave.", evt);
					}
				}
				
			}
		});
		iframe.show();
                
		//iframe.toFront();
		desktop.add(iframe);
		//System.out.println("Demineur");
	}
	
	public void openSkillMeter(){
		final JInternalFrame iframe = new JInternalFrame("SkillMeter",false, true, false, true);
		iframe.setSize(600, 600);
		JSkillMeter skillMeter = new JSkillMeter(iframe);
		//iframe.setTitle("");
		iframe.getContentPane().add(skillMeter);
		iframe.addVetoableChangeListener(new VetoableChangeListener() {
			@Override
			public void vetoableChange(PropertyChangeEvent evt)
					throws PropertyVetoException {
				String name = evt.getPropertyName();
				Object value = evt.getNewValue();
				if(name.equals("closed") && value.equals(Boolean.TRUE)){
					int choix = JOptionPane.showInternalConfirmDialog(iframe, "Do you want to close this window?");
					if(choix != JOptionPane.YES_OPTION){
						throw new PropertyVetoException("User refused to leave.", evt);
					}
				}
				
			}
		});
		try{
			iframe.setSelected(true);
		}catch(PropertyVetoException e){
			e.printStackTrace();
		}
                Dimension desktopSize = desktop.getSize();
                Dimension jInternalFrameSize = iframe.getSize();
                iframe.setLocation((desktopSize.width - jInternalFrameSize.width)/2,
    (desktopSize.height- jInternalFrameSize.height)/2);
		iframe.show();
		//iframe.toFront();
		desktop.add(iframe);
		System.out.println("Skill Meter");
	}
	
	public String openFile(FileFilter filter)
    {
        chooser.setCurrentDirectory(new File("image"));
        chooser.setDialogTitle("Ouvrir image [Basics4Image]");
        chooser.setFileFilter(filter);
        int r = chooser.showOpenDialog(this);
        if(r==JFileChooser.APPROVE_OPTION){
        	this.repaint();
            return chooser.getSelectedFile().getPath();
        }
        return null;
    }
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource().equals(itemQuit)){
			System.exit(0);
		}
		else if(evt.getSource().equals(itemDemineur)){
			openDemineur();
		}
		else if(evt.getSource().equals(itemhome)){
                    GameZone p=new GameZone();
                    this.setVisible(false);
                    p.setVisible(true);
		}
		else if(evt.getSource().equals(itemSkillMeter)){
			openSkillMeter();
		}else if(evt.getSource().equals(itemAbout)){
			JOptionPane.showMessageDialog(desktop, "<html><em>JAVA</em><br/><center>Wiem Dridi<br/>Software Engineer<br/>wiemdridi09@gmail.com</center></html>");
		}
		
	}

	public static void main(String[] args){
		new JDesktopFrame();
	}

}