package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsPassAction extends GameAction {
	
	Card[] passedCards;
	
	private static final long serialVersionUID = -3348122760020242638L;
	
	public HeartsPassAction(GamePlayer player, Card[] cards) {
		super(player);
		passedCards = cards;
	}
	
	public Card[] getPassedCards() {
		return passedCards;
	}
}
