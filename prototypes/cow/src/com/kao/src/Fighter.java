package com.kao.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class Fighter {
	
	public int fNum, x, y, width = 20, height = 20, livingStatus = 2, score;
	public int globuleSize = 12, ammoIndicatorSize = 6, ammoIndicatorDistance = 20, maxAmmo = 3, ammoLeft;
	
	public Color color;
	
	public double angle, speedForward = .35, speedMilk = .8, speedRotate = 1;
	public boolean turning = false;
	
	public int motionX, motionY;
	public Double velX, velY;
			
	public static int distanceFromBorder = 20, windowCorrectionBottom = 22;
	
	public ArrayList<Milk> blobs = new ArrayList<Milk>();
	
	/* globuleStatus: {tick start, globuleActive?, globuleMoving?},
	 * keyEvents: {turn, shoot},
	 * dashStatus: {previous end, next start, dashTimer}
	 */
	public int[] globuleStatus = new int[3], keyEvents = new int[2], dashStatus = new int[3];
				
	public Fighter(Cow cow, int fNum) {
		
		this.fNum = fNum - 1;
		
		ammoLeft = maxAmmo;
		
		switch (fNum) {
		case 1: // lower left corner
			x = 0 + distanceFromBorder;
			y = cow.height - height - distanceFromBorder - windowCorrectionBottom;
			angle = 7 * Math.PI / 4;
			color = Color.CYAN;
			keyEvents[0] = KeyEvent.VK_A;
			keyEvents[1] = KeyEvent.VK_S;
			break;
		case 2: // upper right corner
			x = cow.width - width - distanceFromBorder;
			y = 0 + distanceFromBorder;
			angle = 3 * Math.PI / 4; 
			color = Color.YELLOW;
			keyEvents[0] = KeyEvent.VK_N;
			keyEvents[1] = KeyEvent.VK_M;
			break;
		case 3: // upper left corner
			x = 0 + distanceFromBorder;
			y = 0 + distanceFromBorder;
			angle = 1 * Math.PI / 4; 
			color = Color.GREEN;
			keyEvents[0] = KeyEvent.VK_CLOSE_BRACKET;
			keyEvents[1] = KeyEvent.VK_OPEN_BRACKET;
			break;
		case 4: // lower right corner
			x = cow.width - width - distanceFromBorder;
			y = cow.height - height - distanceFromBorder - windowCorrectionBottom;
			angle = 4 * Math.PI / 4; 
			color = Color.MAGENTA;
			keyEvents[0] = KeyEvent.VK_V;
			keyEvents[1] = KeyEvent.VK_C;
			break;
		}

	}
	
	public void move() {		
		if (turning) {
			angle += Math.PI * speedRotate / 40;
		}
		
		if (globuleStatus[1] == 1) 
		{
			if (Cow.ticks - globuleStatus[0] == 300) {
				livingStatus += 1;
				globuleStatus[1] = 0;
				speedForward = 0.35;
				width = 20;
				height = 20;
			}
			
			if (globuleStatus[2] == 0) {
				if (speedForward >= 0.01) {speedForward -= 0.01;}
			} else if (globuleStatus[2] == 1) {
				speedForward = 0.35;
			}
		} 
		else
		{ 
			if (dashStatus[1] - dashStatus[0] > 0 && dashStatus[1] - dashStatus[0] <= 5) 
			{
				dashStatus[0] = 0;
				dashStatus[2] = Cow.ticks;
				angle += Math.PI/2;	
				speedForward = 1;
			}
			if (Cow.ticks - dashStatus[2] > 5) {
				speedForward = .35;
			}
		}
		
		velX = new Double(Math.cos(angle) * 10);
		motionX = (int) (velX.intValue() * speedForward);
		velY = new Double(Math.sin(angle) * 10); 
		motionY = (int) (velY.intValue() * speedForward);
		
		boolean outX = x + motionX < 0 || x + width + motionX > Cow.cow.width || x + width > Cow.cow.width;
		boolean outY = y + motionY < 0 || y + height + motionY > Cow.cow.height - windowCorrectionBottom || y + height > Cow.cow.height;
		
		if (outX && outY) {}
		else if (outX) {y += motionY;}
		else if (outY) {x += motionX;}
		else {x += motionX; y += motionY;}
		
	}
	
	public void dash() {
		angle += Math.PI/10;
		velX = new Double(Math.cos(angle) * 10);
		motionX = (int) (velX.intValue() * speedForward);
		velY = new Double(Math.sin(angle) * 10); 
		motionY = (int) (velY.intValue() * speedForward);
	}
	
	public void shoot() {
		if (ammoLeft > 0) {
			blobs.add(new Milk(this));
			ammoLeft -= 1;
		}
	}
	
	public int[] collisionStatus(Fighter fighter) {
		
		int[] collisionStatus = {0, 0}; // {collision, blob#}
		for (int activeBlob = 0; activeBlob < blobs.size(); activeBlob++) 
		{
			Milk m = blobs.get(activeBlob);
			int[] Fsides = {fighter.x, fighter.x+fighter.width, fighter.y, fighter.y+fighter.height};
			int[] Msides = {m.x, m.x+m.width, m.y, m.y+m.height};
			
			
			if (!(Fsides[3] < Msides[2] || Fsides[2] > Msides[3] || Fsides[1] < Msides[0] || Fsides[0] > Msides[1]))
			{
				collisionStatus[0] = 1;
				collisionStatus[1] = activeBlob;
			}
		}
		return collisionStatus;
	}
		
	public void getHit() {
		
		for (int currentEnemy = 0; currentEnemy < Cow.FighterList.size(); currentEnemy++) 
		{
			if (currentEnemy != fNum) 
			{
				if (Cow.FighterList.get(fNum).globuleStatus[1] == 1) 
				{
					Fighter rnFighter = Cow.FighterList.get(fNum);
					Fighter rnEnemy = Cow.FighterList.get(currentEnemy);
					// left, right, top, bottom
					int[] Fsides = {rnFighter.x, rnFighter.x+rnFighter.width, rnFighter.y, rnFighter.y+rnFighter.height};
					int[] Esides = {rnEnemy.x, rnEnemy.x+rnEnemy.width, rnEnemy.y, rnEnemy.y+rnEnemy.height};
					
					if (!(Fsides[3] < Esides[2] || Fsides[2] > Esides[3] || Fsides[1] < Esides[0] || Fsides[0] > Esides[1]) && rnFighter.livingStatus == 1)
					{
						Cow.FighterList.get(fNum).livingStatus -= 1;
						Cow.scores[currentEnemy] += 1;
					} 	
				}
				
				int[] collisionStatus = Cow.FighterList.get(currentEnemy).collisionStatus(Cow.FighterList.get(fNum));
				if (collisionStatus[0] == 1 && Cow.FighterList.get(fNum).livingStatus > 0) 
				{
					Cow.FighterList.get(fNum).livingStatus -= 1;
					Cow.FighterList.get(currentEnemy).blobs.remove(collisionStatus[1]);
					Cow.FighterList.get(currentEnemy).ammoLeft += 1;
					
					if (livingStatus == 1) {
						x += width / 4;
						y += height / 4;
						width = globuleSize;
						height = globuleSize;
						globuleStatus[0] = Cow.ticks; // ticks when globule became active
						globuleStatus[1] = 1; // globuleActive? 0=no, 1=yes
						globuleStatus[2] = 0; // globuleMoving? 0=no, 1=yes
					}
					if (livingStatus == 0) {
						Cow.scores[currentEnemy] += 1;
					}
				} 
				
			}
		}
	}
	
	public void reload() {
		
		for (int i = 0; i < blobs.size(); i++)
		{
			Milk currentBlob = blobs.get(i);
			boolean outX = currentBlob.x + currentBlob.motionX < 0 || currentBlob.x + currentBlob.width + currentBlob.motionX > Cow.cow.width;
			boolean outY = currentBlob.y + currentBlob.motionY < 0 || currentBlob.y + currentBlob.height + currentBlob.motionY > Cow.cow.height;
			
			if (outX || outY) {
				blobs.remove(i);
				ammoLeft++;
			}
		}
	}
	
	public int[] rotate() {
		int x1 = 10, y1 = 10, width1 = 20, height1 = 10;
		int centerX = x1 + width1/2;
		int centerY = y1 + height1/2;
		
		int[] Points = new int[8]; // from top left clockwise
		
		int distanceToCenter = (int) Math.pow(Math.pow(centerX - x1, 2) + Math.pow(centerY - y1, 2), 1/2);
		
		for (int i = 0; i < Points.length/2; i++) {
			Points[i] = (int) (distanceToCenter * Math.cos(angle));
			Points[i+4] = (int) (distanceToCenter * Math.sin(angle));
		}
		
		int[] hi = {0,100,0,100,
				    0,50,0,50};
		return hi;
	}
	
	public void render(Graphics g) {
		if (livingStatus > 0) 
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.WHITE);
			int[] Points = rotate();
			
			GeneralPath polygon = 
			        new GeneralPath(GeneralPath.WIND_EVEN_ODD,
			                        Points.length/2);
			polygon.moveTo(Points[0], Points[4]);

			for (int index = 1; index < Points.length/2; index++) {
			        polygon.lineTo(Points[index], Points[index+4]);
			};

			polygon.closePath();
			g2.draw(polygon);
			
			g.setColor(color);
			g.drawRect(x, y, width, height);
			
			if (globuleStatus[1] == 0) 
			{
				for (int blobNumber = 0; blobNumber < ammoLeft; blobNumber++) {
					switch (blobNumber) {
					case 0:
						g.setColor(Color.WHITE);
						g.fillRect(x + width/2 - ammoIndicatorSize/4, y - ammoIndicatorDistance, ammoIndicatorSize, ammoIndicatorSize);
						break;
					case 1:
						g.setColor(Color.WHITE);
						g.fillRect(x - ammoIndicatorDistance + ammoIndicatorSize/2, y + height/2 + ammoIndicatorDistance/2, ammoIndicatorSize, ammoIndicatorSize);
						break;
					case 2:
						g.setColor(Color.WHITE);
						g.fillRect(x + width + ammoIndicatorDistance - ammoIndicatorSize/2, y + height/2 + ammoIndicatorDistance/2, ammoIndicatorSize, ammoIndicatorSize);
						break;
					}
				}
			}
			
			for (int blobNumber = 0; blobNumber < blobs.size(); blobNumber++) {
				blobs.get(blobNumber).render(g);
			}
		}

	}

}