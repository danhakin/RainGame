package com.devcraft.rain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.devcraft.rain.graphics.Screen;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -8877849365799012104L;

	public static int width = 300;
	public static int height = width / 16 * 9; // 16:9 ratio
	public static int scale = 3;
	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	private Screen screen;
	
	/**
	 * Stores a buffered image the size of our game
	 */
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	/**
	 * Stores the buffered image data into a pixel array
	 */
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		screen = new Screen(width,height);
		
		frame = new JFrame();
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		//System.out.println("Last Time: " + lastTime);
		final double ns = 1000000000.0 / 60.0;
		//System.out.println("NS: " + ns);
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			//System.out.println("now: " + now);
			delta += (now - lastTime) / ns;
			//System.out.println("Delta: " + delta);
			lastTime = now;
			//System.out.println("lastTime: " + lastTime);
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
		}
		stop();
	}
	
	/**
	 * Do games calculations. Sometimes is also called tick()
	 */
	public void update() {
		System.out.println("**** Update function");
	}
	
	/**
	 * Repaint all elements to screen
	 */
	public void render() {
		/* 
		  BufferStartegy allows to define a way (strategy) to calculate / normalize 
		  the image/video we want to show in advance and update the screen 
		  by switching buffers instead of repainting pixels one by one which will lead to
		  graphics display problems due to machine/graphic card performance. 
		*/
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // Use 3 buffers to render
			return;
		}
		
		screen.clear();
		screen.render();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		// All drawing code goes here... 
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		// ... before call dispose()
		
		g.dispose();
		
		// Then we do Blitting = buffer swap.
		bs.show();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Rain");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}

	
}
