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
	public final int NUM_PLAYERS = 4;
	private final int MAX_CARDS = 13;
	
	//instance variables
	private int subState;
	private int[] overallScores;
	private int[] handScores;
	private Card[] currentTrick;
	private Card[][] currentHands;
	private boolean heartsBroken;
	
	public HeartsState(Card[][] ncurrentHands, int[] noverallScores, int[] nhandScores, Card[] ntrick, boolean nbroken) {
		overallScores = noverallScores;
		handScores = nhandScores;
		currentTrick = ntrick;
		currentHands = ncurrentHands;
		heartsBroken = nbroken;
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
	
	public int[] getOverallScores() {
		return overallScores;
	}
	
	public int[] getHandScores() {
		return handScores;
	}
}
