package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsPassAction extends GameAction {
	
	Card card1;
	Card card2;
	Card card3;
	
	private static final long serialVersionUID = -3348122760020242638L;
	
	public HeartsPassAction(GamePlayer player, Card a, Card b, Card c) {
		super(player);
		card1 = a;
		card2 = b;
		card3 = c;
	}
	public Card getCard1(){
		return card1;
	}
	public Card getCard2(){
		return card2;
	}
	public Card getCard3(){
		return card3;
	}
}
