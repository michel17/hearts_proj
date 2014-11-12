package edu.up.cs301.hearts;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

public class HeartsComputerPlayer extends GameComputerPlayer {

	public HeartsComputerPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void receiveInfo(GameInfo info) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void timerTicked() {
		if(game != null && game instanceof HeartsLocalGame){
			
			if(((HeartsLocalGame)game).canMove(playerNum)){
				//Make a move
			}
		}
	}
	
	public int getPlayerNumber(){
		return playerNum;
	}

}
