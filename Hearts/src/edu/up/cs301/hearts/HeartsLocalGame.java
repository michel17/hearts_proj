package edu.up.cs301.hearts;

import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsLocalGame extends LocalGame implements Game {

	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean canMove(int playerIdx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String checkIfGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		// TODO Auto-generated method stub
		return false;
	}

}
