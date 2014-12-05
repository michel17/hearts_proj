

package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
/**
 * HeartsPlayAction - 
 * 
 * Action used when playing a card, represents a card being played into a trick
 * 
 * @author Steven Lind, Kyle Michel, David Rodden
 * @version 12/5/2014
 */
public class HeartsPlayAction extends GameAction {
	
	//The card being played
	Card PlayedCard;

	private static final long serialVersionUID = 8272259018444961179L;
	
	/**
	 * HeartsPlayAction:
	 * 
	 * Constructor for the HeartsPlayAction Object
	 * @param player The player making the play
	 * @param c The card being played
	 */
	public HeartsPlayAction(GamePlayer player, Card c) {
		super(player);
		PlayedCard = c;
	}
	
	/**
	 * getPlayedCard:
	 * 
	 * Gets the card being played
	 * @return The card being played
	 */
	public Card getPlayedCard() {
		return PlayedCard;
	}
}
