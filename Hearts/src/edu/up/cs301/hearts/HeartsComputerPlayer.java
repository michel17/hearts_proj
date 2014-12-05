package edu.up.cs301.hearts;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * HeartsComputerPlayer -
 * 
 * A computerized game player in the game of hearts that operates entirely
 * without any gui, choosing its moves algorithmically. The algorithm used
 * is based on the aitype variable, but otherwise the computer player should
 * behave the same regardless of difficulty setting.
 * 
 * @author Steven Lind, Kyle Michel, David Rodden
 * @version 12/5/2014
 */
public class HeartsComputerPlayer extends GameComputerPlayer {
	
	//A copy of the current game state
	HeartsState state;
	
	//The player's current hand
	ArrayList<Card> hand = new ArrayList<Card>();
	//The amount of time the ai will wait before playing a card
	int sleepsecs = 750;
	boolean hasattemptedmove = false;
	//The algorithm used in determining moves, based on the "difficulty" of the AI
	private int aitype;
	
	//constants
	public static final int DUMB_AI = 0;
	public static final int SMART_AI = 1;
	
	/**
	 * HeartsComputerPlayer
	 * 
	 * Initializes the HeartsComputerPlayer object, sets the player type/difficulty
	 * 
	 * @param name
	 * 		The player's name
	 * @param type
	 * 		The difficulty
	 */
	public HeartsComputerPlayer(String name, int type) {
		super(name);
		aitype = type;
		getTimer().setInterval(50);
		getTimer().start();
	}

	@Override
	protected void receiveInfo(GameInfo info) {
		Log.i("HeartsComputerPlayer",
				"receiving updated state (" + info.getClass() + ")");
		if (!(info instanceof HeartsState)) {
			// otherwise, if it's not a game-state message, ignore
		} else {
			this.state = (HeartsState) info;
			hand = state.getPlayerHand(playerNum);
			hasattemptedmove = false;
			Log.i("Computer player", "receiving");
		}
		
	}

	@Override
	protected void timerTicked() {
		if (game != null && game instanceof HeartsLocalGame && state != null) {
			// attempt move
			if (state.getTurnIdx() == playerNum) {//is it our turn?
				if (hasattemptedmove == false) {
					sleep(750);
				}
				if (state.getSubState() == HeartsState.PLAYING) {
					if (aitype == DUMB_AI) {
						game.sendAction(new HeartsPlayAction(this, dumbAI()));
					}
					else {
						game.sendAction(new HeartsPlayAction(this, smartAI()));
					}
				}
				else if (state.getSubState() == HeartsState.PASSING) {
					game.sendAction(new HeartsPassAction(this, dumbPass()));
				}
				hasattemptedmove = true;
			}
		}
	}

	/**
	 * getPlayerNumber
	 * 
	 * Getter method for playerNum
	 * 
	 * @return
	 * 		The player's player index
	 */
	public int getPlayerNumber() {
		return playerNum;
	}

	/**
	 * smartAI
	 * 
	 * The logic for a player choosing the lowest card possible in the 
	 * current situation in order to avoid taking tricks and therefore 
	 * points at all.
	 * 
	 * @return
	 * 		The card the algorithm has determined best to play
	 */
	private Card smartAI() {

		Card[] trick = state.getCurrentTrick();
		Suit leadSuit = null;

		// If the first card of the trick isn't null, set the suit to that
		// card's suit
		if (trick[0] != null) {
			leadSuit = trick[0].getSuit();
		}
		// All code in this else statement would get skipped whenever the
		// computer isn't first
		// Meaning it would set ledsuit and then just end without sending
		// any cards
		// If the computer were first it would just choose a random card,
		// But on any other turn it won't even get the chance to play

		// randomly picks a card

		// if it got here with ledsuit null, it skips it and just plays a
		// random card
		// But it can't get here when ledsuit isn't null to play on any
		// other turn

		ArrayList<Card> suitHand = new ArrayList<Card>();
		if (hand.isEmpty()) {
			return null;
		}
		if (leadSuit != null) {
			for (int i = 0; i < hand.size(); i++) {
				if (hand.get(i).getSuit() == leadSuit) suitHand.add(hand.get(i));
			}
			if (suitHand.size() != 0) {
				Card leastElement = null;
				for (int i = 0; i < suitHand.size(); i++) {
					if (leastElement == null) leastElement = suitHand.get(i);
					if (suitHand.get(i).getRank().value(14) < leastElement.getRank().value(14))
						leastElement = hand.get(i);
				}
				return leastElement;
			}
		} 
		int heartCount = 0, spadeCount = 0, diamondCount = 0, clubCount = 0;
		ArrayList<Suit> newSuit = new ArrayList<Suit>();
		for(int i = 0; i < hand.size(); i++){
			switch(hand.get(i).getSuit()){
			case Heart:
				heartCount++;
				if(!newSuit.contains(Suit.Heart))	newSuit.add(Suit.Heart);
				break;
			case Spade:
				spadeCount++;
				if(!newSuit.contains(Suit.Spade))	newSuit.add(Suit.Spade);
				break;
			case Diamond:
				diamondCount++;
				if(!newSuit.contains(Suit.Diamond))	newSuit.add(Suit.Diamond);
					break;
			case Club:
				clubCount++;
				if(!newSuit.contains(Suit.Club))	newSuit.add(Suit.Club);
				break;
			}
		}
		Suit chosenSuit = newSuit.get((int)(Math.random() * newSuit.size()));
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getSuit() == chosenSuit) suitHand.add(hand.get(i));
		}
		Card leastElement = null;
		for (int i = 0; i < suitHand.size(); i++) {
			if (leastElement == null) leastElement = suitHand.get(i);
			if (suitHand.get(i).getRank().value(14) < leastElement.getRank().value(14))
				leastElement = hand.get(i);
		}
		return leastElement;
	}

	/**
	 * dumbAI
	 * 
	 * Chooses a random card to play from the player's hand, within the 
	 * confines of what the game rules allow.
	 * 
	 * @return
	 * 		A random card
	 */
	private Card dumbAI() {
		if (hand.isEmpty()) {
			return null;
		}
		Card c;
		ArrayList<Card> whitelist = (ArrayList<Card>) hand.clone();
		boolean validcard = false;
		do {
			c = whitelist.get((int) (Math.random() * (whitelist.size())));
			validcard = isValidPlay(c, playerNum, state.getCurrentTrick()[0] == null ? null : state.getCurrentTrick()[0].getSuit());
			whitelist.remove(c);
		}while (!validcard);
		return c;
	}
	
	/**
	 * dumbPass
	 * 
	 * Randomly chooses three cards from the player's hand to pass to another player.
	 * 
	 * @return
	 * 		Three random cards
	 */
	private Card[] dumbPass() {
		Card[] retArry = new Card[3];
		ArrayList<Card> whitelist = (ArrayList<Card>) hand.clone();
		for (int i = 0; i < retArry.length; i++) {
			retArry[i] = whitelist.get((int) (Math.random() * (whitelist.size())));
			whitelist.remove(retArry[i]);
		}
		return retArry;
	}
	
	/**
	 * isValidPlay
	 * 
	 * Locally checks to see if the given card is allowed to be played within the
	 * constraints of the game rules, before sending the move to the game.
	 * 
	 * @param c
	 * 		A card that may be played
	 * @param idx
	 * 		The player's index in the order of rotation
	 * @param ledSuit
	 * 		The lead suit of the current trick
	 * @return
	 * 		Whether or not this card is allowed to be played
	 */
	public boolean isValidPlay(Card c, int idx, Suit ledSuit) {
		ArrayList<Card> playersHand = state.getPlayerHand(idx);
		if (state.getFirstTurn() && !c.equals(new Card(Rank.TWO,Suit.Club))) {//first trick of hand	
			return false;
		}
		if (playersHand.contains(c)
				&& (c.getSuit().equals(ledSuit) || ledSuit == null)) {// Playing
																		// in
																		// suit/leading
			if (ledSuit == null && !state.isHeartsBroken() && !playerHasOnlyHearts(idx) && c.getSuit().equals(Suit.Heart)) {
				return false;
			}
			
			return true;
		} else if (playersHand.contains(c)) {
			boolean suitinhand = false;
			for (Card test : playersHand) {
				if (test.getSuit().equals(ledSuit)) {
					suitinhand = true;
				}
			}
			if (!suitinhand) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * playerHasOnlyHearts
	 * 
	 * Helper for isValidPlay, handles special case for breaking hearts
	 * 
	 * @param idx
	 * 		The player's index
	 * @return
	 * 		Whether or not the player's hand contains only hearts.
	 */	
	private boolean playerHasOnlyHearts(int idx) {
		for (Card c: state.getPlayerHand(idx)) {
			if (c.getSuit() != Suit.Heart) {
				return false;
			}
		}
		state.setHeartsBroken(true);
		return true;
	}

}