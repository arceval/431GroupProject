package Game;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.ObjectInputStream.GetField;

import controller.GameState;
import menus.MainMenu;

public class Game extends Canvas implements Runnable{
	//flag for stopping game
	public boolean isRunning = true;
	//fps rate
	private final double fps = 1000000000.0/60.0;
	//tick rate
	private final double tick = 1000000000.0/30.0;
	// buffer strategy for rendering
	private BufferStrategy buffer;
	//painter that draws the graphics
	private Graphics2D g;
	//gameState
	private GameState gameState = new GameState();
	//Main thread
	public static void main(String[] args) {
		//create game obj
		Thread game = new Thread(new Game());
		//start the game
		game.start();	
	}
	

	public void render() {
		//get the current strategy
		buffer =  getBufferStrategy();
		//if no strategy
		if(buffer == null) {
			//create a triple buffer
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
		//set the canvas
		gameState.setCanvas(this);
		//init gameState
		gameState.init();
		//variable initials
		double fpsDelta = 0.0;
		double tickDelta = 0.0;
		long fpsCounter = 0;
		long tickCounter = 0;
		long timer = 0;
		//get time before
		long timeBefore = System.nanoTime();
		while(isRunning) {
			//get time after
			long timeAfter = System.nanoTime();
			//for capping fps
			fpsDelta += (timeAfter - timeBefore)/fps;
			//for capaping tick
			tickDelta += (timeAfter - timeBefore)/tick;
			//for timer to measure
			timer += timeAfter - timeBefore;
			//tick rate
			if(tickDelta >= 1.000000) {
				tick();
				tickDelta--;
				tickCounter++;
			}
			//delta rate
			if(fpsDelta >= 1.000000) {
				render();
				fpsDelta--;
				fpsCounter++;
			}
			//timer that triggers after every second, used for showing rate of fps and tick
			if(timer >= 1000000000) {
//				System.out.println("Fps: " + fpsCounter);
//				System.out.println("Ticks: " + tickCounter);
				fpsCounter = 0;
				tickCounter = 0;
				timer = 0;
			}
			timeBefore = System.nanoTime();
		}
	}
}
