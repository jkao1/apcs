package com.kao.src;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent; // needed for ActionListener
import java.awt.event.ActionListener; // needed for Timer()
import javax.swing.JPanel;
import javax.swing.Timer;
import com.kao.src.objects.Player; // needed for Timer()

public class Game extends JPanel implements ActionListener {

	/** Added default serial version ID.
	 * This id's our class.
	 */
	private static final long serialVersionUID = 1L;
	
	Timer loopTimer; // made a timer variable
	Player p; // initiates Player object named p

	public Game(){
		setFocusable(true); // focuses window
		
		loopTimer = new Timer(10, this); // every 10 milliseconds, "this" [class] will run
		loopTimer.start(); // starts timer
		
		p = new Player(100,100); // defines object p
		
	}

	public void paint(Graphics g) {
		super.paint(g); // super means the super class (MainClass)
		
		Graphics2D g2d = (Graphics2D) g; // g2d now has methods that can draw onto the board
		p.draw(g2d); // p can access Player class functions
	}

	@Override
	public void actionPerformed(ActionEvent e) { // contains anything you want to happen 
		repaint(); // built-in function that calls public void paint class 
	} 
}
