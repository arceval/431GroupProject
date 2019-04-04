package Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.ConnectionEndpoint;
import controller.GameState;
import menus.ClientMenu;
import menus.HostMenu;

public class Client {
	//socket
	private Socket socket;
	//references to be passed in
	private GameState GameState;
	private GameBoard gameBoard;
	public Client(GameState GameState, GameBoard gameBoard) {
		this.GameState = GameState;
		this.gameBoard = gameBoard;
	}
//init to be called on first run
	public void init() {
		try {
			System.out.println("Client: IP input: " + ClientMenu.ipInput);
			//split into ip and port
			String[] split = ClientMenu.ipInput.split(":");
			String ip = split[0].trim();
			String port = split[1].trim();
			//time before
			long delayBefore = System.nanoTime();
			//connect to server
			socket = new Socket(ip, Integer.valueOf(port));
			long delayAfter = System.nanoTime();
			//set delay 
			GameState.delay = delayAfter - delayBefore;
			//establish endpoint to host
			GameState.connections.add(new ConnectionEndpoint(GameState, gameBoard,socket));

		} catch (UnknownHostException e) {
			//can't find host
			System.out.println("Client: Failed to connect host: " + ClientMenu.ipInput);
			e.printStackTrace();
		} catch (IOException e) {
			// Trouble creating socket
			System.out.println("Client: Failed to create socket: " + ClientMenu.ipInput + " Port: " + GameState.port);
			e.printStackTrace();
		} catch(ArrayIndexOutOfBoundsException forgotPort) {
			//Error check
			System.out.println("Don't forget to add port to ip address");
		}
	}
}
