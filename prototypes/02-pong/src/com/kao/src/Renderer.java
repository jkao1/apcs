package com.kao.src;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Renderer extends JPanel {

	/* serialVersionUID used to ID your class. If none, if class changes then UID will change and Java won't let you reload old data 
	 * --> http://c2.com/ppr/wiki/JavaIdioms/AlwaysDeclareSerialVersionUid.html
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * paintComponent method is where all custom painting takes place.
	 * It's only parameter, a Graphics object, exposes a number of methods for drawing 2D shapes
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g); // super refers to the parent class (the one the current class extends (JPanel))
		
		Pong.pong.render((Graphics2D) g);
	}

}