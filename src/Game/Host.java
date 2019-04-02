package Game;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Stack;

import controller.ConnectionEndpoint;
import controller.GameState;
import controller.Message;
public class Host{
	//Custom thread class
	 public class CustomThread extends Thread implements Runnable{
		 private int connectionSize;
		 private int numPlayers;
		 private ServerSocket socket;
		 private ArrayList<ConnectionEndpoint> connectionList;
		 private ArrayList<Player> playerList;
		 
//		public CustomThread(int connectionsSize, int numPlayers,ServerSocket socket, ArrayList<ConnectionEndpoint> connectionList, ArrayList<Player> playerList) {
//			this.connectionSize = connectionsSize;
//			this.numPlayers =numPlayers;
//			this.socket = socket;
//			this.connectionList = connectionList;
//			this.playerList = playerList;
//		}

		@Override
		public void run() {
			try {
			//wait until all clients connect
				while(!abort && GameState.connections.size() < GameState.numberOfPlayers) {
					System.out.println("Waiting For player to connect");
					//create a new socket
					socket = new ServerSocket(GameState.port[GameState.players.size()]);
					//reusable port
					socket.setReuseAddress(true);
					//wait until someone connects
					Socket client = socket.accept();
					//set reusable port
					client.setReuseAddress(true);
					//add their connection to connection list + add them to playerList
					GameState.connections.add(new ConnectionEndpoint(GameState, gameBoard,client));
					//parse their ip
					String[] temp = client.getInetAddress().toString().split("/");
					if(temp.length>1) {
						//check if its the same address
						if(temp[1].equals(GameState.hostIP)) {
							//If it is then we add Local tag + count
							temp[1] = temp[1].concat("_Local" + GameState.localPlayerCount);
							GameState.localPlayerCount++;
						}
						//add player to list
						GameState.players.add(new Player(temp[1], availableColors.pop()));

						}else {
							if(temp[0].equals(GameState.hostIP)) {
								//If it is then we add Local tag + count
								temp[0] = temp[0].concat("_Local" + GameState.localPlayerCount);
								GameState.localPlayerCount++;
							}
							//add player to list
							GameState.players.add(new Player(temp[0], availableColors.pop()));
							}
					
					System.out.println("Player " + GameState.connections.size() + " has connected");
				}
			} catch (IOException e) {
				//error message
				System.out.println("Failed to accept a client");
				e.printStackTrace();
			}
			//check if we aborted
			if(!abort) {
				//all players connected add self to player list
				GameState.players.add(new Player(GameState.hostIP.toString(), availableColors.pop()));
				//position of client in playerList array
				int playerID = 0;
				//For every client
				for(ConnectionEndpoint client : GameState.connections) {
					//send a sync message
					client.sendMessage(new Message(serverTime.toString(), GameState.players, GameState.hostIP, playerID));
					System.out.println("Host: Sent Message to player " + (playerID+1));
					//increment id by 1
					playerID++;
				}
				//Start the game
				GameState.setCurrentState("GameBoard");
			}
		}
	}
	//max 10 players
	private Stack<Color> availableColors = new Stack<Color>();

	//flag for abort
	public static boolean abort = false;
	//Server Time
	private Instant serverTime;
	//Host Thread
	private CustomThread hostThread;
	//server socket
	private ServerSocket socket;
	//Gamestate
	private GameState GameState;
	private GameBoard gameBoard;
	public Host(GameState GameState, GameBoard gameBoard) {
		this.GameState = GameState;
		this.gameBoard = gameBoard;
	}
	public ServerSocket getServerSocket() {
		return this.socket;
	}
	public void init() {

		//set available colors
		availableColors.add(Color.lightGray);
		availableColors.add(Color.gray);
		availableColors.add(Color.yellow);
		availableColors.add(Color.red);
		availableColors.add(Color.pink);
		availableColors.add(Color.magenta);
		availableColors.add(Color.green);
		availableColors.add(Color.cyan);
		availableColors.add(Color.orange);
		availableColors.add(Color.blue);
		//reset if abort flag if abort was called
		if(abort) {abort = false;}
		
		//set self to host
		GameState.Host = true;
		//set the server time
		serverTime = Instant.now();
		try{
			//set self ip to host
			//take only ip portion
			String[] temp = InetAddress.getLocalHost().toString().split("/");
			if(temp.length>1) {GameState.hostIP=temp[1];}else {GameState.hostIP = temp[0];}
			//set self player Id
			GameState.myPlayerID = GameState.hostIP;
			//set self time
			GameState.time = serverTime;
			//create thread
			if(hostThread == null) {
				hostThread = new CustomThread();
				hostThread.start();
			}else{
				//hostThread was started before, clean up
				try {
					hostThread.join();
					//create new thread and start
					hostThread = new CustomThread();
					hostThread.start();
				} catch (InterruptedException e) {
					//Error message for joining
					System.out.println("Host: Failed to Join host thread");
					e.printStackTrace();
				}
				}
		}catch(IOException e) {
			System.out.println("Host: Failed to get local address");
		}

		

	}
}
