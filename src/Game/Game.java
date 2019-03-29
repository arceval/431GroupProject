package Game;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.ObjectInputStream.GetField;

import controller.GameState;
import menus.MainMenu;

public class Game extends Canvas implements Runnable{
	public boolean isRunning = true;
	private double fps = 1000000000.0/60.0;
	private double tick = 1000000000.0/30.0;
	private BufferStrategy buffer;
	private Graphics2D g;
	private GameState gameState = new GameState();
	
	public static void main(String[] args) {
		Thread game = new Thread(new Game());
		game.start();	
	}
	

	public void render() {
		buffer =  getBufferStrategy();
		if(buffer == null) {
			//triple buffer
			createBufferStrategy(3);
			return;
		}
		//set the painter
		g = (Graphics2D) buffer.getDrawGraphics();
		//clear the canvas by erasing previous drawings for next render
		g.clearRect(0, 0, gameState.getCanvas().getWidth(), gameState.getCanvas().getHeight());
		//Pass the painter to gamestate's render
		gameState.render(g);
		//dispose the painter
		g.dispose();
		//Show the buffer
		buffer.show();
	}
	
	public void tick() {
		//update the game logic
		gameState.tick();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		gameState.setCanvas(this);
		gameState.init();
		double fpsDelta = 0.0;
		double tickDelta = 0.0;
		long fpsCounter = 0;
		long tickCounter = 0;
		long timer = 0;
		long timeBefore = System.nanoTime();
		while(isRunning) {
			long timeAfter = System.nanoTime();
			fpsDelta += (timeAfter - timeBefore)/fps;
			tickDelta += (timeAfter - timeBefore)/tick;
			timer += timeAfter - timeBefore;
			if(tickDelta >= 1.000000) {
				tick();
				tickDelta--;
				tickCounter++;
			}
			if(fpsDelta >= 1.000000) {
				render();
				fpsDelta--;
				fpsCounter++;
			}
			if(timer >= 1000000000) {
				System.out.println("Fps: " + fpsCounter);
				System.out.println("Ticks: " + tickCounter);
				fpsCounter = 0;
				tickCounter = 0;
				timer = 0;
			}
			timeBefore = System.nanoTime();
		}
	}
}
