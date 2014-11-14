package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsLocalGame extends LocalGame implements Game {

	HeartsState state;
	Card[] deck;
	
	public HeartsLocalGame() {
		int count = 0;
		deck =  new Card[52];
		String suit = null;
		String rank;
		String newCard;
		for(int i = 0;i<4;i++){
			switch(i){
			case 1: i = 0;
				suit = "H";
				break;
			
			case 2: i = 1;
				suit = "S";
				break;
			
			case 3: i = 2;
				suit = "D";
				break;
			
			case 4: i = 3;
				suit = "C";
				break;
			}
			for(int j = 2;j<15; j++){
				switch(j){
				case 1: j = 10;
					rank = "T";
					break;
				case 2: j = 11;
					rank = "J";
					break;
				case 3: j = 12;
					rank = "Q";
					break;
				case 4: j = 13;
					rank = "K";
					break;
				case 5: j = 14;
					rank = "A";
					break;
				default:
					rank = "" + j;
					break;
				}
				newCard = rank + suit;
				deck[count] = Card.fromString(newCard);
				count++;
				
				
			}
		}
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
		GamePlayer p;
		if(action instanceof HeartsPlayAction){
			p = action.getPlayer();
			if(p instanceof HeartsHumanPlayer){
				if(canMove((((HeartsHumanPlayer)p).getPlayerNumber())));
			}
		}
		return false;
	}

}
