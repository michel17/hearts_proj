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
		
	}
	
	public int getSubState() {
		return subState;
	}
	
	public int getTurnIdx() {
		return turnIdx;
	}
	
	public int getOverallScore(int idx) {
		return overallScores[idx];
	}
	
	public int getHandScore(int idx) {
		return handScores[idx];
	}
	public boolean isHeartsBroken() {
		return heartsBroken;
	}

}
