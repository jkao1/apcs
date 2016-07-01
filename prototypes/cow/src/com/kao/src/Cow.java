package com.kao.src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Cow implements ActionListener, KeyListener {
	
	public static Cow cow = new Cow();
	public Renderer renderer;
	public static Fighter[] players;
	
	public int numberOfPlayers = 2;
	
	public int gameStatus = 0; // 0 = menu, 1 = playing, 2 = paused
	
	public boolean shot = false;
	
	public int width = 700, height = 700;
		
	 // max is 3 bullets per player

	public Cow() {
		
		Timer timer = new Timer(20, this);
		JFrame jframe = new JFrame("Cow");
		renderer = new Renderer();
		
		jframe.pack(); // used to reposition window
		jframe.setVisible(true);
		jframe.setSize(width, height);
		jframe.setResizable(true);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.add(renderer);
		jframe.addKeyListener(this);
		
		timer.start();
		start();
	}
	
	public void start() {
		gameStatus = 1;
		
		players = new Fighter[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			players[i] = new Fighter(this, i + 1);
		}
				
	}
	
	public void getHit() {
		
		for (int currentPlayer = 0; currentPlayer < numberOfPlayers; currentPlayer++) 
		{
			for (int currentEnemy = 0; currentEnemy < numberOfPlayers; currentEnemy++) 
			{
				if (currentEnemy != currentPlayer) 
				{
					int[] collisionStatus = players[currentEnemy].hit(players[currentPlayer]);
					if (collisionStatus[0] == 1) // collided
					{
						players[currentPlayer].livingStatus -= 1;
						players[currentEnemy].shotLeft[collisionStatus[1]] = false;
						System.out.println(players[currentPlayer].livingStatus);
					} 
				}
			}

		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		if (gameStatus == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 28));
			g.drawString("COW", width / 2 - 18,height / 2 - 18);
			
			g.setFont(new Font(null, 1, 12));
			g.drawString("Press Enter for multiplayer.", width / 2 - 70,height / 2 + 20);
			
			g.setFont(new Font(null, 1, 12));
			g.drawString("> "+String.valueOf(numberOfPlayers)+" players", width / 2 - 18,height / 2 + 48);
		} 
		if (gameStatus == 1 || gameStatus == 2) 
		{
			for (int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) 
			{
				players[playerNumber].render(g);
				for (int blobNumber = 0; blobNumber < Fighter.maxAmmo - players[playerNumber].ammo; blobNumber++) 
				{
					if (players[playerNumber].shotLeft[blobNumber]) {
						players[playerNumber].blobs[blobNumber].render(g);
					}
				}
			}
		}
		if (gameStatus == 2) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 28));
			g.drawString("PAUSED", width / 2 - 48,height / 2 - 18);
		}
		
		
	}
	
	public void update() {
		
		// moves every player in players and every blob in player's blobs
		for (int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) 
		{
			players[playerNumber].move();
			for (int blobNumber = 0; blobNumber < 3 - players[playerNumber].ammo; blobNumber++) 
			{
				if (players[playerNumber].shotLeft[blobNumber]) {
					players[playerNumber].blobs[blobNumber].move();
				}
			}
		}
		
		getHit();
	}

	public static void main(String[] args) throws ScriptException {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameStatus == 1) {
			update();
		}
		renderer.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		
		switch (gameStatus) {
		case 0:
			if (id == KeyEvent.VK_ENTER) {
				start();
			} else if (id == KeyEvent.VK_RIGHT) {
				if (numberOfPlayers == 4) {
					numberOfPlayers = 2;
				} else {
					numberOfPlayers++;
				}
			}
			break;
			
		case 1:
			
			if (id == KeyEvent.VK_SPACE) {
				gameStatus = 2;
			}
			
			if (id == KeyEvent.VK_A) {
				players[0].turning = true;
			} else if (id == KeyEvent.VK_S) {
				players[0].shoot();
			}
			if (id == KeyEvent.VK_B) {
				players[1].turning = true;
			} else if (id == KeyEvent.VK_N) {
				players[1].shoot();
			} 
			break;
		
		case 2:
			if (id == KeyEvent.VK_SPACE) {
				gameStatus = 1;
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();
			
		if (id == KeyEvent.VK_A) {
			players[0].turning = false;
		}
		if (id == KeyEvent.VK_B) {
			players[1].turning = false;
		}
	}

}
