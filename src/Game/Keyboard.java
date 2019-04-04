package Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.GameState;
import menus.ClientMenu;

public class Keyboard implements KeyListener{
	//enter flag
private boolean finishedFlag= false;
//passed in reference
private GameState GameState;
public Keyboard(GameState GameState) {
	this.GameState = GameState;
}
//triggers when a key was pressed
	@Override
	public void keyPressed(KeyEvent e) {
		//check if client menu
		if(GameState.currentState.equals("ClientMenu")) {
			//Check if enter was pressed
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				//Make sure ip field is not empty
				if(!ClientMenu.ipInput.equals("")) {
					//set enter flag to true
					ClientMenu.enter = true;
				}
			}
			//check if delete
			if(!ClientMenu.ipInput.equals("")) {
				//8 is code for backspace
				if(e.getKeyCode() == 8) {
						ClientMenu.ipInput = ClientMenu.ipInput.substring(0, ClientMenu.ipInput.length()-1);
					}else {
						//backspace was not pressed
						//make sure not shift
						if(e.getKeyCode() != KeyEvent.VK_SHIFT) {
							ClientMenu.ipInput = ClientMenu.ipInput.concat(String.valueOf(e.getKeyChar()));
						}
					}
			}else{
				//make sure its not backspace or enter or shift
				if(e.getKeyCode() != 8 && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_SHIFT) {
					ClientMenu.ipInput = ClientMenu.ipInput.concat(String.valueOf(e.getKeyChar()));
				}
			}
			
		}
		//listen only when the gameState is at hostmenu
		if(GameState.currentState.equals("HostMenu")) {
			//Check if enter was pressed
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				//Make sure pensize field is not empty
				if(!GameState.territoryLimit.equals("")) {
					//set enter flag to true
					finishedFlag=true;
				}
			}
			
			if(!finishedFlag) {
				//Modifying territory
				//check if delete
				if(!GameState.territoryLimit.equals("")) {
					//8 is code for backspace
					if(e.getKeyCode() == 8) {
						GameState.territoryLimit = GameState.territoryLimit.substring(0, GameState.territoryLimit.length()-1);
						}else {
							//backspace was not pressed
							//make sure not shift
							if(e.getKeyCode() != KeyEvent.VK_SHIFT) {
								GameState.territoryLimit = GameState.territoryLimit.concat(String.valueOf(e.getKeyChar()));
							}
						}
				}else{
					//make sure its not backspace or enter or shift
					if(e.getKeyCode() != 8 && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_SHIFT) {
						GameState.territoryLimit = GameState.territoryLimit.concat(String.valueOf(e.getKeyChar()));
					}
				}

			}else {
				//Modifying pen
				if(!GameState.penThickness.equals("")) {
					//8 is code for backspace
					if(e.getKeyCode() == 8) {
						GameState.penThickness = GameState.penThickness.substring(0, GameState.penThickness.length()-1);
						}else {
							//backspace was not pressed
							//make sure not shift
							if(e.getKeyCode() != KeyEvent.VK_SHIFT) {
								GameState.penThickness = GameState.penThickness.concat(String.valueOf(e.getKeyChar()));
							}
						}
				}else{
					//make sure its not backspace or enter or shift
					if(e.getKeyCode() != 8 && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_SHIFT) {
						GameState.penThickness = GameState.penThickness.concat(String.valueOf(e.getKeyChar()));
					}
				}
			}

		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
