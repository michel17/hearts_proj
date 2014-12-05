package edu.up.cs301.hearts;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class HeartsPassAction extends GameAction {
	//Card Array to hold the passedCards
	Card[] passedCards;
	
	private static final long serialVersionUID = -3348122760020242638L;
	
	/**
	 * HeartsPassAction:
	 * 
	 * Constructor for the HeartsPassAction object
	 * @param player The player doing the passing
	 * @param cards The cards being passed
	 */
	public HeartsPassAction(GamePlayer player, Card[] cards) {
		super(player);
		passedCards = cards;
	}
	
	/**
	 * getPassedCards:
	 * 
	 * Gets the array of passed cards
	 * @return the passed cards
	 */
	public Card[] getPassedCards() {
		return passedCards;
	}
	
	/**
	 * getCard1:
	 * 
	 * Gets the first card passed
	 * @return The first card in the array
	 */
	public Card getCard1(){
		return passedCards[0];
	}
	/**
	 * getCard2:
	 * 
	 * Gets the second card passed
	 * @return The second card in the array
	 */
	public Card getCard2(){
		return passedCards[1];
	}
	
	/**
	 * getCard3:
	 * 
	 * Gets the third card passed
	 * @return The third card in the array
	 */
	public Card getCard3(){
		return passedCards[2];
	}
}
