package com.kao.src;

import java.awt.Color;
import java.awt.Graphics;
import com.kao.src.Fighter;

public class Milk {
	
	public int x, y, width = 10, height = 10;
	
	public Double velX, velY;
	public int motionX, motionY;
	
	private final double milkDirection;
	
	public Milk(Fighter fighter) {
		this.x = fighter.x + fighter.width / 2;
		this.y = fighter.y + fighter.height / 2;
		
		milkDirection = fighter.angle;
		
		velX = new Double(Math.cos(milkDirection) * 10);
		motionX = (int) (velX.intValue() * Fighter.speedMilk);
		velY = new Double(Math.sin(milkDirection) * 10); 
		motionY = (int) (velY.intValue() * Fighter.speedMilk);
		
	}
	
	public void move() {
		x += motionX;
		y += motionY;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

}
