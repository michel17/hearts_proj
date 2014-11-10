package edu.up.cs301.hearts;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

public class HeartsState extends GameState {

	///Constants
	private static final long serialVersionUID = 2368846731817984773L;
	private final int WAIT_WAIT = 0;
	private final int WAIT_PASS = 1;
	private final int WAIT_RECEIVE = 2;
	private final int WAIT_PLAY = 3;
	private final int WAIT_OVER = 4;
	private final int NUM_PLAYERS = 4;
	
	//instance variables
	private int subState;
	private int turnIdx;
	private int[] overallScores;
	private int[] handScores;
	private Card[] currentTrick;
	private ArrayList<Card[]> currentHands;
	private boolean heartsBroken;
	
	public HeartsState() {
		overallScores = new int[NUM_PLAYERS];
		handScores = new int[NUM_PLAYERS];
		currentTrick = new Card[NUM_PLAYERS];
		currentHands = new ArrayList<Card[]>();
		heartsBroken = false;
	}
	
	public HeartsState(HeartsState orig) {
		overallScores = new int[NUM_PLAYERS];
		handScores = new int[NUM_PLAYERS];
		currentTrick = new Card[NUM_PLAYERS];
		currentHands = new ArrayList<Card[]>();
		heartsBroken = false;
		for (int i = 0; i < overallScores.length; i++) {
			overallScores[i] = orig.getOverallScore(i);
			handScores[i] = orig.getHandScore(i);
		}
		currentTrick = orig.getCurrentTrick();
		currentHands = new ArrayList<Card[]>();
		heartsBroken = orig.isHeartsBroken();
	}
	
	public int getSubState() {
		return subState;
	}
	
	public int getTurnIdx() {
		return turnIdx;
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
		return;
	}
	
	public void setTurnIdx(int i){
		if(i == -1){
			turnIdx++;
			turnIdx = turnIdx % 4;
		}
		else if(i < 4){
			turnIdx = i;
		}
	}

}
