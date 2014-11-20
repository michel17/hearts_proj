package edu.up.cs301.hearts;

import java.util.ArrayList;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

public class HeartsState extends GameState {

	///Constants
	private static final long serialVersionUID = 2368846731817984773L;
	public final int WAIT_WAIT = 0;
	public final int WAIT_PASS = 1;
	public final int WAIT_RECEIVE = 2;
	public final int WAIT_PLAY = 3;
	public final int WAIT_OVER = 4;
	public static final int NUM_PLAYERS = 4;
	public static final int MAX_CARDS = 13;
	
	//instance variables
	private int subState;
	private int[] overallScores;
	private int[] handScores;
	private Card[] currentTrick;
	private Card[][] currentHands;
	private boolean heartsBroken;
	private int turnIdx;
	private boolean firstTurn;
	
	public HeartsState(Card[][] ncurrentHands, int[] noverallScores, int[] nhandScores, Card[] ntrick, boolean nbroken) {
		overallScores = noverallScores;
		handScores = nhandScores;
		currentTrick = ntrick;
		currentHands = ncurrentHands;
		heartsBroken = nbroken;
		firstTurn = true;
	}
	
	public HeartsState(HeartsState orig, int forPlayer) {
		overallScores = new int[NUM_PLAYERS];
		handScores = new int[NUM_PLAYERS];
		currentTrick = new Card[NUM_PLAYERS];
		currentHands = new Card[4][13];
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
				}
				else {
					currentHands[i][j] = null;
				}
			}
		}
		currentTrick = orig.getCurrentTrick();
		heartsBroken = orig.isHeartsBroken();
	}
	
	public int getSubState() {
		return subState;
	}
	
	
	public int getOverallScore(int idx) {
		if(idx < NUM_PLAYERS){
			return overallScores[idx];
		}
		else{
			return 0;
		}
	}
	
	public int getHandScore(int idx) {
		return handScores[idx];
	}
	
	public Card[] getCurrentTrick() {
		Card[] copy = new Card[NUM_PLAYERS];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = currentTrick[i];
		}
		return copy;
	}
	public boolean isHeartsBroken() {
		return heartsBroken;
	}
	public int getNumPlayers() {
		return NUM_PLAYERS;
	}
	public void setSubstate(int i){
		// TODO FIX THIS ISH
		subState = i;
		return;
	}
	
	
	
	public Card[][] getCurrentDeal() {
		return currentHands;
	}
	
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
	 * adds a card to the next open slot in the currentTrick array.
	 * Returns true if we were able to find an open slot, false if we could not
	 * @param c
	 * @return
	 */
	public boolean addCardToTrick(Card c){
		for(int i = 0; i < currentTrick.length; i++){
			if(currentTrick[i] == null){
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
	 * @return the overallScoresArray
	 */
	public int[] getOverallScores() {
		return overallScores;
	}
	
	/**
	 * getHandScores
	 * 
	 * returns the handScores array to the player calling it
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
	 * @param del The card that needs to be removed
	 * @return the deck without the removed card in it
	 */
	private Card[][] removeCard(Card del) {
		Card[][] newDeal = new Card[4][13];
		for (int i = 0; i < newDeal.length; i++) {
			for (int j = 0; j < newDeal[i].length; j++) {
				if (del == currentHands[i][j]) {
					newDeal[i][j] = null;
				}
				else {
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
	 * the id number of the player whose score is being changed
	 * @param score 
	 * the number the player's score will be set to 
	 */
	public void setHandScore(int player, int score){
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
	 * the id of the player whose score will be changed
	 * @param score
	 * the number
	 */
	public void setOverallScore(int player, int score) {
		overallScores[player] = score;
	}
	
	/**
	 * getTurnIdx
	 * 
	 * @return
	 * 		the current turn index
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
	 * 		The turn to set turn index to
	 */
	public void setTurnIdx(int i) {
		if(i == -1){
			turnIdx = ((turnIdx + 1) % 4);
		}
		else if (i < 4){
			turnIdx = i;
		}
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
}
