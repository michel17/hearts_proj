package edu.up.cs301.hearts;

import java.util.ArrayList;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.infoMsg.GameState;

public class HeartsState extends GameState {

	// /Constants
	private static final long serialVersionUID = 2368846731817984773L;

	//Constants to deal with passing cards
	public final int PASS_LEFT = -1;
	public final int PASS_RIGHT = 1;
	public final int PASS_ACROSS = 2;

	//Phase constants
	public static final int PASSING = 0;
	public static final int RECEIVING = 1;
	public static final int PLAYING = 2;
	public static final int NUM_PLAYERS = 4;
	public static final int MAX_CARDS = 13;

	// instance variables
	private int subState;
	private int[] overallScores;
	private int[] handScores;
	private Card[] currentTrick;
	private Card[][] currentPassCards;
	private Card[][] currentHands;
	private boolean heartsBroken;
	private int turnIdx;
	private boolean firstTurn;

	/**
	 * HeartsState:
	 * 
	 * Constructor for the HeartsState object
	 * 
	 * @param ncurrentHands Card Array to represent the current hands of all the players
	 * @param noverallScores Array of integers to represent the overall scores of each player
	 * @param nhandScores Array of integers to represent the hand scores of each player
	 * @param ntrick Array of cards to represent the current trick in play
	 * @param nbroken Boolean to tell if hearts have been broken yet
	 * @param ncurrentPassCards Array of cards that are being passed in the passing phase
	 */
	public HeartsState(Card[][] ncurrentHands, int[] noverallScores,
			int[] nhandScores, Card[] ntrick, boolean nbroken,
			Card[][] ncurrentPassCards) {
		overallScores = noverallScores;
		handScores = nhandScores;
		currentTrick = ntrick;
		currentHands = ncurrentHands;
		currentPassCards = ncurrentPassCards;
		heartsBroken = nbroken;
		firstTurn = true;
		subState = PASSING;
	}

	/**
	 * HeartsState:
	 * 
	 * Copy constructor for the HeartsState object
	 * 
	 * @param orig HeartsState object to be copied
	 * @param forPlayer integer identifier of the player we're sending the copy to
	 */
	public HeartsState(HeartsState orig, int forPlayer) {
		overallScores = new int[NUM_PLAYERS];
		handScores = new int[NUM_PLAYERS];
		currentTrick = new Card[NUM_PLAYERS];
		currentHands = new Card[4][13];
		currentPassCards = new Card[4][3];
		heartsBroken = false;
		for (int i = 0; i < overallScores.length; i++) {
			overallScores[i] = orig.getOverallScore(i);
			handScores[i] = orig.getHandScore(i);
		}
		Card[][] cDeal = orig.getCurrentDeal();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				if (i == forPlayer) {
					currentHands[i][j] = cDeal[i][j];
				} else {
					currentHands[i][j] = null;
				}
			}
		}
		turnIdx = orig.getTurnIdx();
		firstTurn = orig.getFirstTurn();
		currentTrick = orig.getCurrentTrick();
		heartsBroken = orig.isHeartsBroken();
		subState = orig.getSubState();
	}

	/**
	 * getSubstate:
	 * 
	 * Getter method for the substate integer
	 * 
	 * @return the current substate
	 */
	public int getSubState() {
		return subState;
	}

	/**
	 * getOverallScore:
	 * 
	 * Gets the overall score for a given player
	 * 
	 * @param idx The index of the player whose score we're finding
	 * @return The overall score for that player
	 */
	public int getOverallScore(int idx) {
		if (idx < NUM_PLAYERS) {
			return overallScores[idx];
		} else {
			return 0;
		}
	}

	/**
	 * getHandScore:
	 * 
	 * Gets the hand score for a given player
	 * @param idx the index of the player whose hand score we're finding
	 * @return The hand score for the given player
	 */
	public int getHandScore(int idx) {
		return handScores[idx];
	}

	/**
	 * getCurrentTrick:
	 * 
	 * Gets the current trick
	 * 
	 * @return A card array containing the cards in the current trick in order
	 */
	public Card[] getCurrentTrick() {
		Card[] copy = new Card[NUM_PLAYERS];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = currentTrick[i];
		}
		return copy;
	}

	/**
	 * setHeartsBroken:
	 * 
	 * Sets hearts to be broken or not
	 * 
	 * @param b Boolean of what heartsBroken will be set to
	 */
	public void setHeartsBroken(boolean b) {
		heartsBroken = b;
	}

	/**
	 * isHeartsBroken:
	 * 
	 * Tells whether or not hearts have been broken yet
	 * 
	 * @return True if hearts have been broken, false if they have not
	 */
	public boolean isHeartsBroken() {
		return heartsBroken;
	}

	/**
	 * getNumPlayers:
	 * 
	 * Gets the number of players in the game
	 * @return The number of players in the game
	 */
	public int getNumPlayers() {
		return NUM_PLAYERS;
	}

	/**
	 * setSubstate:
	 * 
	 * Sets the substate to the given integer
	 * @param i The number we're setting the substate to
	 */
	public void setSubstate(int i) {
		subState = i;
		return;
	}

	/**
	 * getCurrentDeal:
	 * 
	 * Gets the current deal of cards in the game
	 * 
	 * @return The currentHands instance variable
	 */
	public Card[][] getCurrentDeal() {
		return currentHands;
	}

	/**
	 * getPlayerHand
	 * 
	 * Gets a card array of a given player
	 * 
	 * @param idx the index for the player whose hand we're trying to find
	 * @return An arrayList of cards in the player's hand
	 */
	public ArrayList<Card> getPlayerHand(int idx) {
		ArrayList<Card> hand = new ArrayList<Card>();
		for (int i = 0; i < currentHands[idx].length; i++) {
			if (currentHands[idx][i] != null) {
				hand.add(currentHands[idx][i]);
			}
		}
		return hand;
	}

	/**
	 * addCardToTrick
	 * 
	 * adds a card to the next open slot in the currentTrick array. Returns true
	 * if we were able to find an open slot, false if we could not
	 * 
	 * @param c
	 * @return
	 */
	public boolean addCardToTrick(Card c) {
		for (int i = 0; i < currentTrick.length; i++) {
			if (currentTrick[i] == null) {
				currentTrick[i] = c;
				currentHands = removeCard(c);
				return true;
			}
		}
		return false;
	}

	/**
	 * getOverallScores
	 * 
	 * gets the overallScores array for the player that calls it
	 * 
	 * @return the overallScoresArray
	 */
	public int[] getOverallScores() {
		return overallScores;
	}

	/**
	 * getHandScores
	 * 
	 * returns the handScores array to the player calling it
	 * 
	 * @return a copy of the handScores array
	 */
	public int[] getHandScores() {
		return handScores;
	}

	/**
	 * removeCard
	 * 
	 * Removes the given card from our full deck for dealing
	 * 
	 * @param del
	 *            The card that needs to be removed
	 * @return the deck without the removed card in it
	 */
	private Card[][] removeCard(Card del) {
		Card[][] newDeal = new Card[4][13];
		for (int i = 0; i < newDeal.length; i++) {
			for (int j = 0; j < newDeal[i].length; j++) {
				if (del == currentHands[i][j]) {
					newDeal[i][j] = null;
				} else {
					newDeal[i][j] = currentHands[i][j];
				}
			}
		}
		return newDeal;
	}

	/**
	 * setHandScore
	 * 
	 * Sets the "hand score" for the given player to the given value
	 * 
	 * @param player
	 *            the id number of the player whose score is being changed
	 * @param score
	 *            the number the player's score will be set to
	 */
	public void setHandScore(int player, int score) {
		if (player >= 0 && player <= 3) {
			handScores[player] = score;
		}
	}

	/**
	 * setOverallScore
	 * 
	 * Sets the overall game score of the given player to the given score
	 * 
	 * @param player
	 *            the id of the player whose score will be changed
	 * @param score
	 *            the number
	 */
	public void setOverallScore(int player, int score) {
		overallScores[player] = score;
	}

	/**
	 * getTurnIdx
	 * 
	 * @return the current turn index
	 */
	public int getTurnIdx() {
		return turnIdx;
	}

	/**
	 * setTurnIdx
	 * 
	 * sets the turn index to whatever value is passed by i
	 * 
	 * @param i
	 *            The turn to set turn index to
	 */
	public void setTurnIdx(int i) {
		turnIdx = i;
	}

	public void clearTrick() {
		for (int i = 0; i < currentTrick.length; i++) {
			currentTrick[i] = null;
		}
	}

	public void setFirstTurn(boolean set) {
		firstTurn = set;
	}

	public boolean getFirstTurn() {
		return firstTurn;
	}

	/**
	 * addPassCards
	 * 
	 * Assigns three cards to slots in the currentPassCards array based on who
	 * passed them and what direction we're passing them
	 * 
	 * @param a
	 *            first card
	 * @param b
	 *            second card
	 * @param c
	 *            third card
	 * @param passer
	 *            the person doing the passing
	 * @param passDirection
	 *            indicator whether we're passing to the right, left, across, or
	 *            not at all
	 */
	public void addPassCards(Card a, Card b, Card c, int passer,
			int passDirection) {
		passer = ((passer + passDirection) % 4);
		currentPassCards[passer][0] = a;
		currentPassCards[passer][1] = b;
		currentPassCards[passer][2] = c;
		currentHands = removeCard(a);
		currentHands = removeCard(b);
		currentHands = removeCard(c);
	}

	/**
	 * getPassCards
	 * 
	 * Gets the currentPassCards array
	 * 
	 * @return the currentPassCards array
	 */
	public Card[][] getPassCards() {
		return currentPassCards;
	}

	/**
	 * passCards
	 * 
	 * Sets the cards currentPassCards array to the open slots in peoples' hands
	 * caused by them passing cards the currentPassCards array already takes
	 * passing into account, so this method simply finds open slots for the
	 * person and adds the cards to their hand
	 */
	public void passCards() {
		int q = 0;
		for (int i = 0; i < currentHands.length; i++) {
			for (int k = 0; k < currentHands[i].length; k++) {
				if (currentHands[i][k] == null && q < 3) {
					currentHands[i][k] = currentPassCards[i][q];
					q++;
				}
			}
			q = 0;
		}
		sortHands();
	}
	/**
	 * clearPassCards
	 * 
	 * Clears the passCards array for reuse
	 */
	public void clearPassCards() {
		for (int i = 0; i < currentPassCards.length; i++) {
			for (int k = 0; k < currentPassCards[i].length; k++) {
				currentPassCards[i][k] = null;
			}
		}
	}

	/**
	 * sortHands
	 * 
	 * Sorts the hands of a player, used to make sure hands are organized after passing occurs
	 */
	private void sortHands() {
		// sort hands using selection sort
		for (int j = 0; j < 4; j++) {
			int first = 0;
			Card c;
			for (int k = currentHands[j].length - 1; k > 0; k--) {
				first = 0;
				for (int l = 1; l <= k; l++) {
					if (suitCompare(currentHands[j][l].getSuit(),
							currentHands[j][first].getSuit()) < 0
							|| (suitCompare(currentHands[j][l].getSuit(),
									currentHands[j][first].getSuit()) == 0 && currentHands[j][l]
									.getRank().value(14) > currentHands[j][first]
									.getRank().value(14))) {
						first = l;
					}
				}
				c = currentHands[j][first];
				currentHands[j][first] = currentHands[j][k];
				currentHands[j][k] = c;
			}
		}
	}
	
	/**
	 * suitCompare
	 * 
	 * Compares two suits for the purpose for sorting a player's hand
	 * @param first the first suit we're looking at
	 * @param second the second suit we're looking at
	 * @return 1 if the first goes further left in the hand than the second, -1 if the second goes further left in the hand
	 */
	private int suitCompare(Suit first, Suit second) {
		if (first == second) {
			return 0;
		}
		switch (first) {
		case Heart:
			return 1;
		case Spade:
			if (second == Suit.Heart) {
				return -1;
			}
			return 1;
		case Diamond:
			if (second == Suit.Club) {
				return 1;
			}
			return -1;
		case Club:
			return -1;
		}
		return 0;
	}
}
