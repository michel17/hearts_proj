package edu.up.cs301.hearts;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.Game;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

public class HeartsLocalGame extends LocalGame implements Game {

	HeartsState state;
	Card[] deck;
	int turnIdx;
	ArrayList<GamePlayer> TrickOrder = new ArrayList<GamePlayer>();
	private static final int INCREMENT_TURN = -1;
	private static final int ACE_VALUE = 14;

	public HeartsLocalGame() {
		super();
		int count = 0;
		deck = new Card[52];
		String suit = null;
		String rank;
		String newCard;
		// Creates a sorted deck
		// Goes from 0 - 3 for suit then 2 - 14 for rank
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				suit = "H";
				break;

			case 1:
				suit = "S";
				break;

			case 2:
				suit = "D";
				break;

			case 3:
				suit = "C";
				break;
			}

			for (int j = 2; j < 15; j++) {
				switch (j) {
				case 10:
					rank = "T";
					break;
				case 11:
					rank = "J";
					break;
				case 12:
					rank = "Q";
					break;
				case 13:
					rank = "K";
					break;
				case 14:
					rank = "A";
					break;
				default:
					rank = "" + j;
					break;
				}
				newCard = rank + suit;
				deck[count] = Card.fromString(newCard);
				count++;
			}
		}
		Card[][] deal = createNewDeal();
		state = new HeartsState(deal, new int[4], new int[4], new Card[4],
				false);
		state.clearTrick();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		if (p == null) {
			return;
		}
		int playerIdx = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				playerIdx = i;
			}
		}
		p.sendInfo(new HeartsState(state, playerIdx));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		if (playerIdx == turnIdx) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String checkIfGameOver() {
		int winnerNum = 0;
		for (int i = 0; i < state.getNumPlayers(); i++) {
			if (state.getOverallScore(i) >= 100) {
				// THIS IS NOT WRONG ANYMORE
				for (int j = 0; i < players.length; j++) {
					if ((state.getOverallScores())[j] >= (state
							.getOverallScores())[winnerNum]) {
						winnerNum = j;
					}
				}
				return this.playerNames[winnerNum] + " is teh winrar";
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		Log.i("MOVE", "MOVE REQUESTED");
		GamePlayer p;
		boolean tf;
		if (action instanceof HeartsPlayAction) {
			HeartsPlayAction act = (HeartsPlayAction) action;
			p = action.getPlayer();
			if (act.getPlayedCard() == null) {
				return false;
			}

			for (int i = 0; i < players.length; i++) {

				if (players[i].equals(p)) {

					if (canMove(i) == true) {

						Card[] trick = state.getCurrentTrick();
						Suit ledSuit = trick[0] == null ? null : trick[0].getSuit();

						for (int j = 0; j < trick.length; j++) {

							// if the spot is open, check if valid and add card
							if (trick[j] == null) {

								if (isValidPlay(act.getPlayedCard(), i, ledSuit)) {
									tf = state.addCardToTrick(act.PlayedCard);

									if (tf == true) {
										setTurnIdx(INCREMENT_TURN);
										if (checkTrick()) {
											// manually push new states so the
											// player can see the fourth card
											for (GamePlayer play : players) {
												play.sendInfo(state);
											}
											try {
												Thread.sleep(1000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											isHandOver();
											state.clearTrick();
										}
										return true;
									} else {
										return false;
									}
								}
								break;
							}
						}

					}
					break;
				}
			}
			p.sendInfo(new NotYourTurnInfo());
		}

		return false;
	}

	/**
	 * createNewDeal
	 * 
	 * creates a new deal of cards Requires import statements for great
	 * depression and FDR to run
	 * 
	 * @return the new deal of cards for the next hand
	 */
	private Card[][] createNewDeal() {
		Card[][] deal = new Card[4][13];
		// essentially, pick a random order to attempt dealing cards in
		ArrayList<Integer> order = new ArrayList<Integer>();
		order.add(0);
		order.add(1);
		order.add(2);
		order.add(3);
		for (int card = 0; card < deck.length; card++) {
			Collections.shuffle(order);
			for (int player : order) {
				int i = 0;
				while (i < 13 && deal[player][i] != null) {
					i++;
				}
				if (i < 13) {
					deal[player][i] = deck[card];
					break;
				}
				// otherwise, this player's hand is full, try the next one
			}
		}
		return deal;
	}

	public void setTurnIdx(int i) {
		if (i == -1) {
			turnIdx++;
			turnIdx = turnIdx % 4;
		} else if (i < 4) {
			turnIdx = i;
		}
		state.setTurnIdx(turnIdx);
	}

	/**
	 * isValidPlay
	 * 
	 * checks if the chosen card is a valid play
	 * 
	 * @param c
	 *            the card chosen by the player to be played
	 * @param idx
	 *            the index of the player asking to play the card
	 * @param ledSuit
	 *            the leading suit of the trick
	 * @return true if the play is valid and false is not
	 */
	private boolean isValidPlay(Card c, int idx, Suit ledSuit) {
		ArrayList<Card> playersHand = state.getPlayerHand(idx);
		if (playersHand.contains(c)
				&& (c.getSuit().equals(ledSuit) || ledSuit == null)) {// Playing
																		// in
																		// suit/leading
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
	 * checkTrick
	 * 
	 * checks to see if the trick is over and awards points if it is
	 * 
	 * @return true if the trick is over, false if it isn't
	 */
	private boolean checkTrick() {
		Card[] trickCards;
		trickCards = state.getCurrentTrick();
		//if the trick is full, find the winner
		if(trickCards[3] != null){
			Card highCard = null;
			int winnerIndex = (turnIdx+1)%4;
			int realWinner = -1;
			int points = 0;
			Suit ledSuit = trickCards[0].getSuit();
			for(int i = 0; i < trickCards.length; i++){
				//This may or may not work, I don't know how comparing suits works with '=='
				//Adding shortname should fix that
				if(trickCards[i].getSuit().equals(ledSuit));{
					
					if(highCard == null || (trickCards[i].getRank().value(ACE_VALUE) > highCard.getRank().value(ACE_VALUE))){
						realWinner = winnerIndex;
						highCard = trickCards[i];
					}
					if (trickCards[i].getSuit().equals(Suit.Heart)) {
						points = points
								+ trickCards[i].getRank().value(ACE_VALUE);
					}
					if (trickCards[i].getRank().equals(Rank.QUEEN) && trickCards[i].getSuit().equals(Suit.Spade)) {
						points = points + 13;
					}
				}

				winnerIndex = ((winnerIndex + 1) % 4);
			}
			// WE HAVE WINNER
			// TODO ADD GIVING POINTS TO THE WINNER HERE
			state.setHandScore(realWinner, points);
			setTurnIdx(realWinner);
			return true;
		}
		return false;
	}

	// THINK ON YOUR SINS
	// private int reverseTurnIndex(){
	// turnIdx = ((turnIdx + 3) % 4);
	// return turnIdx;
	// }

	/**
	 * isHandOver
	 * 
	 * checks if the hand is over
	 * 
	 * @return true if the hand is over, false if it isn't
	 */
	private boolean isHandOver() {
		// This method will only be called at the end of a hand, so let's assume
		// all players have the same number of cards
		if (state.getPlayerHand(0).isEmpty()) {
			// Hand is over, compile scores together
			boolean moonshot = false;
			int i;
			for (i = 0; i < players.length; i++) {
				int score = state.getHandScore(i);
				if (score == 26) {
					moonshot = true;
					break;
				}
				state.setOverallScore(i, state.getOverallScore(i) + score);
			}
			// If someone shot the moon, i now holds their index
			if (moonshot == true) {
				for (int j = 0; j < players.length; j++) {
					if (i != j) {
						state.setOverallScore(j, state.getOverallScore(j) + 26);
					}
				}
			}
			state = new HeartsState(createNewDeal(), state.getOverallScores(), state.getHandScores(), new Card[players.length], false);
			return true;
		}
		return false;
	}
}
