package edu.up.cs301.hearts;

import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsLocalGame extends LocalGame implements Game {

	HeartsState state;
	
	public HeartsLocalGame() {
		state = new HeartsState();
	}
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		if(p instanceof HeartsHumanPlayer){
		((HeartsHumanPlayer)p).receiveInfo(new HeartsState(state));
		}
		else if(p instanceof HeartsComputerPlayer){
			((HeartsComputerPlayer)p).receiveInfo(new HeartsState(state));
		}
	}

	@Override
	protected boolean canMove(int playerIdx) {
		if(playerIdx == state.getTurnIdx()){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	protected String checkIfGameOver() {
		for(int i = 0; i < state.getNumPlayers(); i++){
			if(state.getOverallScore(i) >= 100){
				return this.playerNames[i] + " is teh winrar";
			}
		}
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		// TODO Auto-generated method stub
		return false;
	}

}
