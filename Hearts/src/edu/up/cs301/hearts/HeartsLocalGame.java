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
	private int passIndex;
	private static final int INCREMENT_TURN = -1;
	private static final int ACE_VALUE = 14;
	private int passDirection = 1;

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
				false, new Card[4][3]);
		boolean flag = false;
		// set starting player
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 13; k++) {
				if (deal[j][k].equals(new Card(Rank.TWO, Suit.Club))) {
					setTurnIdx(j);
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
		}
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
				for (int j = 0; j < players.length; j++) {
					if ((state.getOverallScores())[j] <= (state
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
					Log.i("MOVE", "MOVE REQUESTED BY: " + p.toString());
					if (canMove(i) == true) {

						Card[] trick = state.getCurrentTrick();
						Suit ledSuit = trick[0] == null ? null : trick[0]
								.getSuit();

						for (int j = 0; j < trick.length; j++) {

							// if the spot is open, check if valid and add card
							if (trick[j] == null) {

								if (isValidPlay(act.getPlayedCard(), i, ledSuit)) {
									state.setFirstTurn(false);
									tf = state.addCardToTrick(act.PlayedCard);

									if (tf == true) {
										setTurnIdx(INCREMENT_TURN);
										if (checkTrick()) {
											// manually push new states so the
											// player can see the fourth card
											for (GamePlayer play : players) {
												play.sendInfo(state);
												Log.i("Thread Name", Thread.currentThread().getName());
												if (Thread.currentThread().getName().equals("main") && play instanceof HeartsHumanPlayer) {
													((HeartsHumanPlayer) play).forceRedraw(state);
												}
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
		//TODO LOOK AT THIS AND TELL ME IF IT SUCKS
		//STEVEN ADDED THIS
		//if we get a pass action, come through here
		if(action instanceof HeartsPassAction){
			//if we're in the passing state, come through here
			if(state.getSubState() == HeartsState.PASSING){

				//Set the action and the player with the action as variables
				HeartsPassAction act = (HeartsPassAction) action;
				p = act.getPlayer();

				//Find which player by number sent the action
				for(int i = 0; i < players.length; i++){

					//if we find the player, we know his number
					if(players[i].equals(p)){
						//This person is the passer, he gets an index
						int passerIdx = i;
						//get the array of "passCards" similar to how we store a trick
						Card[][] passCards = state.getPassCards();

						//If we don't have a null object and we find an open slot in the array
						//we try to add the card to the passCards variable in state
						if(passCards != null && passCards[((i + passDirection) % 4)][0] == null){

							//The addPassCards method takes the three cards from the action along with the passer's index
							//And the pass direction and saves the cards to a slot in the array corresponding to the player
							//That will receive the cards
							state.addPassCards(act.getCard1(),act.getCard2(),act.getCard3(), passerIdx, passDirection);

							//The checkPass method checks the passCards Array in state for any openings
							//If it finds any null spots in the array we know that we still have people
							//Who haven't added their cards to the passCards array
							//If there are no open spots it returns true
							setTurnIdx(INCREMENT_TURN);
							if(checkPass()){
								//Calls the passCards method to add the cards from the currentPassCards array in state to
								//Each player's hand by checking for openings and filling them in
								//This will probably result in out of order cards :(
								state.passCards();
								
								////////////////////////////////////////////////
								// ADDED THIS
								////////////////////////////////////////////////
								Card[][] deal = state.getCurrentDeal();
								boolean flag = false;
								// set starting player
								for (int j = 0; j < 4; j++) {
									for (int k = 0; k < 13; k++) {
										if (deal[j][k].equals(new Card(Rank.TWO, Suit.Club))) {
											setTurnIdx(j);
											flag = true;
											break;
										}
									}
									if (flag) {
										break;
									}
								}
								
								//Then since we know we just passed the cards, we set the substate to PLAYING
								state.setSubstate(HeartsState.PLAYING);

								//Then before leaving we change the direction of passing so that next hand we go in a different direction
								if(passDirection == 1){
									passDirection = 3;
								}
								else if(passDirection == -1){
									passDirection = 2;
								}
								else if (passDirection == 2){
									passDirection = 0;
								}
								else if(passDirection == 0){
									passDirection = 1;
								}
								state.clearPassCards();
								return true;
							}
							//If checkPass returns false it means we found an opening, but the person's cards were added so we return true here
							else{
								return true;
							}

						}
						//If we get here it meant the first card in the passCards array for the action's player was not null
						//We then know that the player has already added cards to the passCards array previously so we return false
						else{
							
							return false;
							
						}
					}
				}
			}
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
	public boolean isValidPlay(Card c, int idx, Suit ledSuit) {
		ArrayList<Card> playersHand = state.getPlayerHand(idx);
		if (state.getFirstTurn() && !c.equals(new Card(Rank.TWO,Suit.Club))) {//first trick of hand	
			return false;
		}
		if (playersHand.contains(c)
				&& (c.getSuit().equals(ledSuit) || ledSuit == null)) {// Playing in suit/leading
			if (ledSuit == null && !state.isHeartsBroken() && c.getSuit().equals(Suit.Heart)) {
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
	 * checkTrick
	 * 
	 * checks to see if the trick is over and awards points if it is
	 * 
	 * @return true if the trick is over, false if it isn't
	 */
	private boolean checkTrick() {
		Card[] trickCards;
		trickCards = state.getCurrentTrick();
		// if the trick is full, find the winner
		if (trickCards[3] != null) {
			Card highCard = null;
			int winnerIndex = turnIdx;
			int realWinner = -1;
			int points = 0;
			Suit ledSuit = trickCards[0].getSuit();
			for (int i = 0; i < trickCards.length; i++) {
				// This may or may not work, I don't know how comparing suits
				// works with '=='
				if (trickCards[i].getSuit().equals(ledSuit)) {
					if (highCard == null
							|| (trickCards[i].getRank().value(ACE_VALUE) > highCard
									.getRank().value(ACE_VALUE))) {
						realWinner = winnerIndex;
						highCard = trickCards[i];
					}

				}
				if (trickCards[i].getSuit().equals(Suit.Heart)) {
					state.setHeartsBroken(true);
					points++;
				}
				else if (trickCards[i].getRank().equals(Rank.QUEEN)
						&& trickCards[i].getSuit().equals(Suit.Spade)) {
					points = points + 13;
				}
				winnerIndex = ((winnerIndex + 1) % 4);
			}
			// WE HAVE WINNER
			// TODO ADD GIVING POINTS TO THE WINNER HERE
			state.setHandScore(realWinner, state.getHandScore(realWinner) + points);
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
			state = new HeartsState(createNewDeal(), state.getOverallScores(),
					new int[players.length], new Card[players.length], false, state.getPassCards());
			boolean flag = false;
			Card[][] deal = state.getCurrentDeal();
			// set starting player
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					if (deal[j][k].equals(new Card(Rank.TWO, Suit.Club))) {
						setTurnIdx(j);
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * checkPass
	 * 
	 * Cycles through the cards currently in the passCards array
	 * and if any are null returns false.
	 * Effectively checks if everyone has passed their cards before they can all receive their cards
	 * 
	 * @return true if there are no open spaces, false if there is at least one open space
	 */
	public boolean checkPass(){

		Card[][] passCards = state.getPassCards();
		for(int i = 0; i < passCards.length; i++){
			for(int k = 0; k < passCards[i].length; k++){
				if(passCards[i][k] == null){
					return false;
				}
			}
		}
		return true;
	}
	
	
	

}
