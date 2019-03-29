package menus;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu{
	public final int width = 505;
	public final int height = 530;
	private JFrame frame;
	//must be inited before
	private Canvas window;
	private BufferedImage mainMenuImage;
	//To be called when program runs
	public void init() {
		//set the canvas size
		window.setSize(width, height);
		//empty frame for the canvas to be placed in
		frame = new JFrame();
		//close the program when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set the frame size
		frame.setSize(width, height);
		//disable resizing of frame
		frame.setResizable(false);
		//place the panel into the frame
		frame.add(window);
		//make both frame and panel visible
		frame.setVisible(true);
		window.setVisible(true);
		//paint main menu
		paintMainMenu();
	}
	public void setCanvas(Canvas canvas) {
		this.window = canvas;
	}
	private void paintMainMenu() {
		try {
			//set image
			mainMenuImage = ImageIO.read(new File("res/main_menu.jpg"));
			//create graphics for panel
			//paint the menu image onto the panel
			Graphics2D painter = (Graphics2D) window.getGraphics();
			painter.drawImage(mainMenuImage, null, 0, 0);
		}catch(IOException e) {
			System.out.println("Failed to open mainMenuImage located in res folder");
			e.printStackTrace();
		}
	}
	//get panel
	public Canvas getCanvas() {
		return this.window;
	}
	
	public void render(Graphics2D g) {
		g.drawImage(mainMenuImage, null, 0, 0);
	}
}
