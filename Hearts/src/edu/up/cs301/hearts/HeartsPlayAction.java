package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsPlayAction extends GameAction {
	
	Card PlayedCard;

	private static final long serialVersionUID = 8272259018444961179L;
	
	public HeartsPlayAction(GamePlayer player, Card c) {
		super(player);
		PlayedCard = c;
	}
	
	public Card getPlayedCard() {
		return PlayedCard;
	}
}
