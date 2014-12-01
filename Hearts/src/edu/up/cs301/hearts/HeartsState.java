package edu.up.cs301.hearts;

import java.util.ArrayList;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

public class HeartsState extends GameState {

	///Constants
	private static final long serialVersionUID = 2368846731817984773L;
<<<<<<< HEAD
	public final int WAIT_WAIT = 0;
	public final int PASSING = 1;
	public final int WAIT_RECEIVE = 2;
	public final int PLAYING = 3;
	public final int WAIT_OVER = 4;
	
	public final int PASS_LEFT = -1;
	public final int PASS_RIGHT = 1;
	public final int PASS_ACROSS = 2;
	
=======
	public static final int PASSING = 0;
	public static final int RECEIVING = 1;
	public static final int PLAYING = 2;
>>>>>>> origin/master
	public static final int NUM_PLAYERS = 4;
	public static final int MAX_CARDS = 13;
	
	//instance variables
	private int subState;
	private int[] overallScores;
	private int[] handScores;
	private Card[] currentTrick;
	private Card[][] currentPassCards;
	private Card[][] currentHands;
	private boolean heartsBroken;
	private int turnIdx;
	private boolean firstTurn;
	
	public HeartsState(Card[][] ncurrentHands, int[] noverallScores, int[] nhandScores, Card[] ntrick, boolean nbroken, Card[][] ncurrentPassCards) {
		overallScores = noverallScores;
		handScores = nhandScores;
		currentTrick = ntrick;
		currentHands = ncurrentHands;
		currentPassCards = ncurrentPassCards;
		heartsBroken = nbroken;
		firstTurn = true;
		subState = PASSING;
	}
	
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
				}
				else {
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
	public void setHeartsBroken(boolean b) {
		heartsBroken = b;
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
	 * Assigns three cards to slots in the currentPassCards array based on who passed them and what direction we're passing them
	 * 
	 * @param a first card
	 * @param b second card
	 * @param c third card
	 * @param passer the person doing the passing
	 * @param passDirection indicator whether we're passing to the right, left, across, or not at all
	 */
	public void addPassCards(Card a, Card b, Card c, int passer, int passDirection){
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
	public Card[][] getPassCards(){
		return currentPassCards;
	}
	
	/**
	 * passCards
	 * 
	 * Sets the cards currentPassCards array to the open slots in peoples' hands caused by them passing cards
	 * the currentPassCards array already takes passing into account, so this method
	 * simply finds open slots for the person and adds the cards to their hand
	 */
	public void passCards(){
		int q = 0;
		for(int i = 0; i < currentHands.length; i++){
			for(int k = 0;k < currentHands[i].length; k++){
				if(currentHands[i][k] == null && q < 3){
					currentHands[i][k] = currentPassCards[i][q];
					q++;
				}
			}
			q = 0;
		}
	}
	public void getPassDirec(){
		
	}
}
