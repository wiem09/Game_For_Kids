package demineur;
/**
*
* @author nabil
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class CaseDemineur extends JButton implements ActionListener{

	private final int WIDGH_CASE = 10;
	private final int HEIGHT_CASE = 10;
	
	
	private boolean caseArmed;
	private int posX;
	private int posY;
	
	public CaseDemineur() {
		setMaximumSize(new Dimension(WIDGH_CASE, HEIGHT_CASE));
		setMinimumSize(new Dimension(WIDGH_CASE, HEIGHT_CASE));
		setPreferredSize(new Dimension(WIDGH_CASE, HEIGHT_CASE));
		setSize(new Dimension(WIDGH_CASE, HEIGHT_CASE));
		this.addActionListener(this);
		
		// par defaut la case ne contient pas de bombe
		setBombe(false);
	}

        @Override
	 public void actionPerformed(ActionEvent event){
		 //JOptionPane.showMessageDialog(null, "bouton ");
		 if(caseArmed)
                     setBackground(Color.RED);
		 else
                     setBackground(new java.awt.Color(0, 153, 102));
		 //setEnabled(false);
		 
	 }
	 
	 public void setPosition(int x, int y){
		 posX = x;
		 posY = y;
	 }
	 public Point getPosition(){
		 return new Point(posX, posY);
	 }
	 public void setBombe(boolean b){
		 caseArmed = b;
	 }
	 public boolean isBombe(){ return caseArmed;}
	 
	 
}
