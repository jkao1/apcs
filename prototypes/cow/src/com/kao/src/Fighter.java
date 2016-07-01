package com.kao.src;

import java.awt.Color;
import java.awt.Graphics;

public class Fighter {
	
	public int x, y, width = 20, height = 20, livingStatus = 2;

	public static int maxAmmo = 3;
	
	public double angle;
	
	public boolean turning = false;
	
	public Double velX, velY;
		
	public static double speedForward = .35, speedMilk = .8, speedRotate;
	
	public static int distanceFromBorder = 20, windowCorrectionBottom = 22, ammoBulletRadius = 20;
	
	public Milk blobA, blobB, blobC;
	public Milk[] blobs = {blobA, blobB, blobC};
	public int ammo = blobs.length;
	
	public boolean[] shotLeft = {true, true, true};
		
	public Fighter(Cow cow, int fighterNumber) {
		
		switch (fighterNumber) {
		case 1: // lower left corner
			x = 0 + distanceFromBorder;
			y = cow.height - height - distanceFromBorder - windowCorrectionBottom;
			angle = 7 * Math.PI / 4; 
			break;
		case 2: // upper right corner
			x = cow.width - width - distanceFromBorder;
			y = 0 + distanceFromBorder;
			angle = 3 * Math.PI / 4; 
			break;
		case 3: // upper left corner
			x = 0 + distanceFromBorder;
			y = 0 + distanceFromBorder;
			angle = 1 * Math.PI / 4; 
			break;
		case 4: // lower right corner
			x = cow.width - width - distanceFromBorder;
			y = cow.height - height - distanceFromBorder - windowCorrectionBottom;
			angle = 4 * Math.PI / 4; 
			break;
		}
	}
	
	public void move() {
		if (turning) {
			angle += Math.PI / 40;
		}
		
		velX = new Double(Math.cos(angle) * 10);
		int motionX = (int) (velX.intValue() * speedForward);
		velY = new Double(Math.sin(angle) * 10); 
		int motionY = (int) (velY.intValue() * speedForward);
		
		boolean outX = x + motionX < 0 || x + width + motionX > Cow.cow.width;
		boolean outY = y + motionX < 0 || y + height + motionY > Cow.cow.height - windowCorrectionBottom;
		
		if (outX) {
			y += motionY;
		} else if (outY) {
			x += motionX;
		} else {
			x += motionX;
			y += motionY;
		}
	}
	
	public void shoot() {
		if (ammo > 0) {
			blobs[maxAmmo - ammo] = new Milk(this);
			ammo -= 1;
		}
	}
	
	public int[] hit(Fighter fighter) {
		
		int[] collisionStatus = {0, 0}; // {collision, blob#}
		for (int activeBlob = 0; activeBlob < maxAmmo - ammo; activeBlob++) 
		{
			boolean collision = Math.pow(blobs[activeBlob].x - fighter.x,2) + Math.pow(blobs[activeBlob].y - fighter.y,2) < 100;
			if (collision) {
				collisionStatus[0] = 1;
				collisionStatus[1] = activeBlob;
			}
		}
		return collisionStatus;
	}
	
	public void reload(Milk milk) {
		ammo = maxAmmo;
		blobs[0] = blobA;
		blobs[1] = blobB;
		blobs[2] = blobC;
		
		for (int i = 0; i < maxAmmo; i++) {
			shotLeft[i] = true;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		
		for (int ammoBullet = 0; ammoBullet < ammo; ammoBullet++) {
			switch (ammoBullet) {
			case 0:
				g.setColor(Color.WHITE);
				g.fillRect(x, y - ammoBulletRadius, 2, 2);
				break;
			case 1:
				g.setColor(Color.WHITE);
				g.fillRect(x - ammoBulletRadius / 2, y + ammoBulletRadius / 2, 2, 2);
				break;
			case 2:
				g.setColor(Color.WHITE);
				g.fillRect(x + ammoBulletRadius / 2, y - ammoBulletRadius / 2, 2, 2);
				break;
			}
			
		}
	}

}
