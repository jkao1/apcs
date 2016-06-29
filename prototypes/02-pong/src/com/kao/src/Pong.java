package com.kao.src;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener {
	
	public static Pong pong = new Pong();
	
	public int width = 700, height = 700;
	
	public static int windowCorrectionBottom = 22;
		
	public Renderer renderer; 
	
	public Paddle p1, p2;
	
	public Ball ball;
	
	public boolean bot = false;
	
	public boolean w, s, up, down;
	
	public int gameStatus = 0; // 0 = stopped/menu, 1 = paused, 2 = playing 
	
	/* Constructor provides initial values for class fields when you create the object */
	public Pong() {
		/* The timer object tells ActionListener to fire an ActionEvent at the specified interval of 20 milliseconds */
		Timer timer = new Timer(20, this);
		JFrame jframe = new JFrame("Pong");
		
		renderer = new Renderer();
		
		jframe.pack(); // used to reposition window
		jframe.setVisible(true);
		jframe.setSize(width, height);
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null); // window opens in the middle
		
		jframe.add(renderer);
		jframe.addKeyListener(this);
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // window opens in the middle
				
		timer.start();
	}
	
	public void start() {
		gameStatus = 2;
		p1 = new Paddle(this, 1);
		p2 = new Paddle(this, 2);
		ball = new Ball(this);
	}
	
	public void update() {
		if (w) {
			p1.move(true);
		}
		if (s) {
			p1.move(false);
		}
		if (!bot) {
			if (up) {
				p2.move(true);
			}
			if (down) {
				p2.move(false);
			}
		} else {
			if (p2.y + p2.height / 2 < ball.y) {
				p2.move(false);
			} else  if (p2.y + p2.height / 2 > ball.y) {
				p2.move(true);
			}
		}
		
		ball.update(p1, p2);
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (gameStatus == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 28));
			g.drawString("PONG", width / 2 - 22,height / 2 - 18);
			
			g.setFont(new Font(null, 1, 12));
			g.drawString("Press Space for singleplayer.", width / 2 - 70,height / 2 + 20);
			
			g.setFont(new Font(null, 1, 12));
			g.drawString("Press Shift for multiplayer.", width / 2 - 65,height / 2 + 45);
		}
		if (gameStatus == 1 || gameStatus == 2) {
			/* White line down the middle: *
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(5f));
			g.drawLine(width / 2, 20, width / 2, 80);
			*/
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 28));
			g.drawString(String.valueOf(p1.score), width / 2 - 40, 60);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 28));
			g.drawString(String.valueOf(p2.score), width / 2 + 24, 60);
			
			p1.render(g);
			p2.render(g);
			ball.render(g);
		}
		if (gameStatus == 1) {
			g.setColor(Color.WHITE);
			g.setFont(new Font(null, 1, 24));
			g.drawString("PAUSED", width / 2 - 48,height / 2 - 30);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameStatus == 2) {
			update();			
		}
		
		renderer.repaint(); // repaint: in a frame, all components must be repainted
	}
	
	public static void main(String[] args) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		
		if (id == KeyEvent.VK_W) {
			w = true;
		} else if (id == KeyEvent.VK_S) {
			s = true;
		} else if (id == KeyEvent.VK_UP) {
			up = true;
		} else if (id == KeyEvent.VK_DOWN) {
			down = true;
		}
		
		else if (id == KeyEvent.VK_ESCAPE && gameStatus == 2) {
			gameStatus = 0;
		}
		
		else if (id == KeyEvent.VK_SHIFT && gameStatus == 0) {
			bot = true;
			gameStatus = 2;
			start();
		}
		
		else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus  == 0) {
				gameStatus = 2;
				start();
			} else if (gameStatus == 1) {
				gameStatus = 2;
			} else if (gameStatus == 2) {
				gameStatus = 1;
			} 
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();
		
		if (id == KeyEvent.VK_W) {
			w = false;
		} else if (id == KeyEvent.VK_S) {
			s = false;
		} else if (id == KeyEvent.VK_UP) {
			up = false;
		} else if (id == KeyEvent.VK_DOWN) {
			down = false;
		}
		
		
	}
	
}