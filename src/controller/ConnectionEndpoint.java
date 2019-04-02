package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import Game.GameBoard;
import Game.Player;

public class ConnectionEndpoint{
	//Thread for placing messagges in to receive buffer
	public class ReceiveThread extends Thread implements Runnable{
		private ArrayList<Message> receiveBuffer;
		private boolean stopReceiveThread = false;
		public ReceiveThread(ObjectInputStream inputStream, ArrayList<Message> receiveBuffer) {
			//receive buffer of client / host passed by reference
			this.receiveBuffer = receiveBuffer;
		}
		public void stopReceiveThread() {
			stopReceiveThread = true;
		}
		public void run() {
			while(!stopReceiveThread) {
				//listen for messages and place into buffer
				try {
					this.receiveBuffer.add((Message)inputStream.readObject());
				} catch (ClassNotFoundException e) {
					//class wasn't found error
					System.out.println("ConnectionEndpoint: Class not found");
					e.printStackTrace();
				} catch (IOException e) {
					//Problem with stream
					System.out.println("ConnectionEndpoint: Inputstream failed");
					//clear streams and sockets
					inputStream = null;
					outputStream = null;
					if(socket!=null) {
						try {
							socket.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					//Check if this player is the host
					if(GameState.myPlayerID.equals(GameState.hostIP)) {
						//A player has disconnected
						System.out.println("ConnectionEndpoint: I am the host, a player has disconnected");
						//set 2nd crash
						if(ConnectionEndpoint.playerCrash) {
							ConnectionEndpoint.playerCrash2 = true;
							//debug
							System.out.println("ConnectionEndpoint: 2nd player crashed, playerCrash2 = true");
						}
						//set crash flag
						ConnectionEndpoint.playerCrash = true;
						//finding offending player by pinging everyone
						int indexOfNull = 0;
						//bugg fix
						if(playerCrash && GameState.connections.size()==2) {indexOfNull++; System.out.println("ConnectionEndpoint: Added to nullIndex");}
						try {
							for(ConnectionEndpoint connection : GameState.connections) {
								connection.sendMessage(new Message("Ping"));
								indexOfNull++;
							}
						}catch(NullPointerException nullPtr) {
							//debug
							System.out.println("ConnectionEndpoint: Host removed " + GameState.getPlayers().get(indexOfNull).getPlayerID()+" from player list");
							
							//if null triggered, offending player found at indexOfNull
							int origNullIndex = indexOfNull;
							//remove from Host's connections in gamestate
							if(indexOfNull == GameState.connections.size()) {indexOfNull=GameState.connections.size()-1; System.out.println("fixed indexOfNull1");}
							GameState.connections.remove(indexOfNull);
							System.out.println("connection size " +GameState.connections.size() + " indexNull " + indexOfNull);
							if((indexOfNull) == GameState.connections.size()) {indexOfNull=origNullIndex;System.out.println("fixed indexOfNull2");}
							//remove from player's list
							GameState.getPlayers().remove(indexOfNull);
							for(int i = 0; i < GameState.getPlayers().size(); i++) {
								//debug
								System.out.println("ConnectionEndpoint: Host's player list after removing disconnected player: " + GameState.getPlayers().get(i).getPlayerID());
							}
							//bug fix
							ArrayList<Player> playerListToSend = (ArrayList) GameState.getPlayers().clone();
							//letting clients know their knew position in playerlist
							int clientPosition = 0;
							if(playerListToSend.get(0).getPlayerID().equals(GameState.myPlayerID)) {clientPosition=1;}
							//send a resync to everybody
							for(ConnectionEndpoint connection : GameState.connections) {
								connection.sendMessage(new Message(GameState.time.now().toString(), playerListToSend, GameState.hostIP,clientPosition));
								clientPosition++;
							}
						}


					}else {
						//this player is not a host, but a player
						//remove host from connections
						GameState.connections.clear();
						//remove host from playerlist
						System.out.println("ConnectionEndpoint: I am not the host, the host disconnected");
						for(int i = 0; i < GameState.getPlayers().size(); i++) {
							if(GameState.hostIP.equals(GameState.getPlayers().get(i).getPlayerID())) {
								//remove host
								GameState.getPlayers().remove(i);
								break;
							}
						}
						//set new host
						GameState.hostIP = GameState.getPlayers().get(0).getPlayerID();

						//extract IP portion only
						String[] temp = GameState.hostIP.split("_");
						String[] temp2 = GameState.myPlayerID.split("_");
						String ip;
						String myId;
						if(temp.length > 1) {
							ip = temp[0].trim();
							myId = temp2[0].trim();
						}else{
							ip = GameState.hostIP.trim();
							myId = GameState.myPlayerID.trim();
						}
						//check my id
						if(GameState.myPlayerID.equals(GameState.hostIP)) {
							//debug
							System.out.println("ConnectionEndpoint: " + GameState.myPlayerID + " I am the host now");
							//this player is the new host
							//wait for everyone to connect
							while(GameState.connections.size() != GameState.getPlayers().size()-1) {
								try {
									//create server socket
									serverSocket = new ServerSocket(GameState.port[GameState.connections.size()]);
									serverSocket.setReuseAddress(true);
									//wait until someone connects
									Socket client = serverSocket.accept();
									//debug
									System.out.println("ConnectionEndpoint: client connected on port " + GameState.port[GameState.connections.size()]);
									System.out.println("ConnectionEndpoint: size of GameState.connections " + GameState.connections.size());

									//add their connection to connection list
									GameState.connections.add(new ConnectionEndpoint(GameState, gameBoard,client));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
				
						}else {
							//this player is not the new host
							//my position in player List
							int myPosition = -1;
							for(int i = 0; i < GameState.getPlayers().size(); i++) {
								if(GameState.myPlayerID.equals(GameState.getPlayers().get(i).getPlayerID())) {
									myPosition = i;
									break;
								}
							}
							//add host to connection
							//failure flag
							boolean pass = false;
							while(!pass) {
								
							
							try {
								//debug
								System.out.println("ConnectionEndpoint: " + GameState.myPlayerID + " Connecting to host on port" + GameState.port[myPosition-1]);
								//add host to connection
								GameState.connections.add(new ConnectionEndpoint(GameState, gameBoard, new Socket(ip, GameState.port[myPosition-1])));
								pass = true;
								
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								pass=false;
								e1.printStackTrace();
							}
							}
						}

					}
					e.printStackTrace();
				}
			}
		}
	}
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ReceiveThread receiveThread;
	private GameState GameState;
	private boolean hostCrash = false;
	private static boolean playerCrash = false;
	private static boolean playerCrash2 = false;
	//for updating
	private GameBoard gameBoard;
	//Buffer of messages
	private ArrayList<Message> receiveBuffer = new ArrayList<Message>();
	
	public ConnectionEndpoint(GameState GameState, GameBoard gameBoard,Socket s) {
		try {
			//set gameState
			this.GameState = GameState;
			//set gameboard
			this.gameBoard = gameBoard;
			//set socket
			this.socket = s;
			outputStream = new ObjectOutputStream(s.getOutputStream());
			inputStream = new ObjectInputStream(s.getInputStream());
			//initialize receiving thread
			receiveThread = new ReceiveThread(inputStream, receiveBuffer);
			//start receive thread
			receiveThread.start();
		}catch(IOException e) {
			System.out.println("Failed to make connection Endpoint");
		}

	}

	

	
	public void sendMessage(Message message) {
		//push the message into the socket
		try {
			outputStream.writeObject(message);
		} catch (IOException e) {
			System.out.println("Connection Endpoint: Failed to send message");
			e.printStackTrace();
		}
	}
	
	
	public Message readMessage() {
		//ensure there is a message to read
		if(receiveBuffer.size() != 0) {
			//Check if its the initial sync message
			if(receiveBuffer.get(0).isForSync()) {
				//perform sync operations
				performSync(receiveBuffer.get(0));
				//remove from queue
				receiveBuffer.remove(0);
				return null;
			}
			
			//Game related messages
			
			//sort buffer by timestamp
			sortBuffer();
			//return first message in receive buffer
			Message message = receiveBuffer.get(0);
			receiveBuffer.remove(0);
			//A lock message received from host
			if(message.getAction().equals("lock")) {
				//debug message
				System.out.println("Client: received lock message from host");
				//lock the square in question
				gameBoard.getSquareList().get(message.getSquareID()).setLock(true);
			}else if(message.getAction().equals("unlock")) {
				//debug message
				System.out.println("Client: received unlock message from host");
				//unlock received by host, unlock the square in question
				gameBoard.getSquareList().get(message.getSquareID()).setLock(false);
			}else if(message.getAction().equals("update")) {
				//debug message
				System.out.println("Client: received update message from host");
				//client received update from host, occupy the square
				gameBoard.getSquareList().get(message.getSquareID()).setOccupiedPlayerID(message.getPlayerID());
				gameBoard.getSquareList().get(message.getSquareID()).setOccupied(true);
			}else if(message.getAction().equals("check")) {
				//debug message
				System.out.println("Host: received check message from client");
				//host received client check message
				//reply with square status
				if(!gameBoard.getSquareList().get(message.getSquareID()).isLocked()) {
					//Square is unlocked, send approval to only requestor, and send a lock to everyone else
					int indexOfPlayer = 0;
					//corrections of index
					if(GameState.connections.size()==2 && !playerCrash) {indexOfPlayer--; System.out.println("here1");}
					if(GameState.connections.size()==1 && !playerCrash) {indexOfPlayer--;System.out.println("here2");}


					for(Player player : GameState.getPlayers()) {
						//debug
						System.out.println("ConnectionEndPoint: Player" + indexOfPlayer + "= " + player.getPlayerID());
						if(player.getPlayerID().equals(message.getSourceIpAddress())) {
							break;
						}else {
							indexOfPlayer++;
						}
					}
					//debug
					System.out.println("ConnectionEndPoint: Host sending approve to " + GameState.players.get(indexOfPlayer).getPlayerID());
					//bug fix
					if(GameState.connections.size()==2 && ConnectionEndpoint.playerCrash && ConnectionEndpoint.playerCrash2) {indexOfPlayer = 0;}
					if(GameState.connections.size()==1 && ConnectionEndpoint.playerCrash) {indexOfPlayer = 0;}

					//debug
					System.out.println("ConnectionEndpoint: ConnectionSize "  + GameState.connections.size() + " indexOfPlayer " + indexOfPlayer);
					//send approval to requestor
					GameState.connections.get(indexOfPlayer).sendMessage(new Message(GameState.time.now().toString(), message.getSourceIpAddress(), message.getSquareID(), "approve", GameState.myPlayerID));
					//send a lock to everyone else
					for(int i = 0; i < GameState.connections.size(); i++) {
						//ignore this iteration because only sending lock to others
						if(i == indexOfPlayer) {
							continue;
						}else{
							//send a lock message to this player
							GameState.connections.get(i).sendMessage(new Message(GameState.time.now().toString(), message.getSourceIpAddress(), message.getSquareID(), "lock", GameState.myPlayerID));
						}
					}
					//Lock square on own board
					gameBoard.getSquareList().get(message.getSquareID()).setLock(true);
				}else{
					//Square is taken by someone else
					//Find the original requestors connection
					int indexOfPlayer = 0;
					for(Player player : GameState.getPlayers()) {
						if(player.getPlayerID().equals(message.getSourceIpAddress())) {
							break;
						}else {
							indexOfPlayer++;
						}
					}
					//debug
					System.out.println("ConnectionEndPoint: Host sending deny to " + GameState.players.get(indexOfPlayer).getPlayerID());
					//square is locked, send a deny message
					GameState.connections.get(indexOfPlayer).sendMessage(new Message(GameState.time.now().toString(), message.getSourceIpAddress(), message.getSquareID(), "deny", GameState.myPlayerID));
				}
				
			}else if(message.getAction().equals("approve")) {
				//debug message
				System.out.println("Client: received approve message from host");
				//client received approve from host for check
				//client can now draw
				gameBoard.getSquareList().get(message.getSquareID()).setApproved(true);
			}else if(message.getAction().equals("deny")) {
				//debug message
				System.out.println("Client: received deny message from host");
				//client received deny from host as response to check
				//client cannot draw
				gameBoard.getSquareList().get(message.getSquareID()).setApproved(false);
			}else if(message.getAction().equals("release")) {
				//debug message
				System.out.println("Host: received release message from client");
				//host received release from client, unlock own square and let everyone know
				gameBoard.getSquareList().get(message.getSquareID()).setLock(false);
				//send unlock to everyone
				for(int i = 0; i < GameState.connections.size(); i++) {
					GameState.connections.get(i).sendMessage(new Message(GameState.time.now().toString(), message.getSourceIpAddress(), message.getSquareID(), "unlock", GameState.myPlayerID));
				}
			}else if(message.getAction().equals("declare")) {
					//debug message
					System.out.println("Host: received declare message from client");
					//Host received a declare from a client, update self and let everyone know
					//set own square to occupied
					gameBoard.getSquareList().get(message.getSquareID()).setOccupied(true);
					//set own square occupied player id
					gameBoard.getSquareList().get(message.getSquareID()).setOccupiedPlayerID(message.getPlayerID());
					//let everyone else know
					for(int i = 0; i < GameState.connections.size(); i++) {
						GameState.connections.get(i).sendMessage(new Message(GameState.time.now().toString(), message.getPlayerID(), message.getSquareID(), "update", GameState.myPlayerID));
					}			
				}
			return null;
		}
			return null;
		
	}
	//for client sync to host
	public void performSync(Message syncMessage) {
		//extract server time
		String serverTimeString = syncMessage.getServerTime();
		//extract player List
		ArrayList<Player> playerList = syncMessage.getPlayerList();
		//debug
		for(int i = 0; i < syncMessage.getPlayerList().size(); i++) {
			System.out.println("ConnectionEndpoint: Client sync received playerList: " + syncMessage.getPlayerList().get(i).getPlayerID());
		}
		//extract host address
		String hostAddress = syncMessage.getHostAddress();
		//update GameState with new info
		//update host ip
		GameState.hostIP = hostAddress;
		//update gamestate's player list
		GameState.players = playerList;
		//update gameState time with sync
		Instant serverTime = Instant.parse(serverTimeString);
		 serverTime = serverTime.plusNanos(GameState.delay);
		 GameState.time = serverTime;
		 //update own player id
		GameState.myPlayerID = syncMessage.getPlayerList().get(syncMessage.getClientsPlayerIDIndex()).getPlayerID();
		//Print Id
		System.out.println("ConnectionEndpoint: " + GameState.myPlayerID + " Sync complete");
		//print player list
		for(Player player : GameState.players) {
			System.out.println("ConnectionEndpoint: PlayerList: " + player.getPlayerID());
		}
		//print player's own id
		System.out.println("ConnectionEndpoint: My ID: " + GameState.myPlayerID);
		//Start the game
		System.out.println("ConnectionEndpoint: " + GameState.myPlayerID + " Starting Game");
		GameState.setCurrentState("GameBoard");
	}
	
	//Possibly add this to another thread
	public void sortBuffer(){
		try {

			//selection sort
			int indexOfCurrent = 0;
			int indexOfMin = -1;
			for(Message message2 : receiveBuffer) {
				Instant minTimeStamp = Instant.parse(receiveBuffer.get(indexOfCurrent).getTimeStamp());
				for(int indexOfOther = indexOfCurrent; indexOfOther < receiveBuffer.size(); indexOfOther++) {
					Message message = receiveBuffer.get(indexOfOther);
					Instant otherTimeStamp = Instant.parse(message.getTimeStamp());
					if(otherTimeStamp.compareTo(minTimeStamp) < 0) {
						indexOfMin = indexOfOther;
						minTimeStamp = Instant.parse(message.getTimeStamp());
					}
					indexOfOther++;
				}
				//swap min with index of current
				if(indexOfMin != -1) {swap(indexOfCurrent, indexOfMin);}
				//reset index of min
				indexOfMin = -1;
				//increment current index
				indexOfCurrent++;
			}
		}catch(Exception e) {
			System.out.println("Time parse failed");
		}
	}
	
	public void swap(int indexOfCurrent, int indexOfMin) {
		//temp
		Message temp = receiveBuffer.get(indexOfCurrent);
		//swap
		receiveBuffer.set(indexOfCurrent, receiveBuffer.get(indexOfMin));
		receiveBuffer.set(indexOfMin, temp);
	}
	

}
