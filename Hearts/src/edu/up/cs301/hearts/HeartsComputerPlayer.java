package edu.up.cs301.hearts;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

public class HeartsComputerPlayer extends GameComputerPlayer {
	HeartsState state;
	ArrayList<Card> hand = new ArrayList<>();
	int sleepsecs = 750;
	boolean hasattemptedmove = false;
	public static final int DUMB_AI = 0;
	public static final int SMART_AI = 1;
	private int aitype = 0;
	
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

	public int getPlayerNumber() {
		return playerNum;
	}

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

	// We're going stone age basic. Try a random card and play it. Ignore
	// everything else.
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
	
	private Card[] dumbPass() {
		Card[] retArry = new Card[3];
		ArrayList<Card> whitelist = (ArrayList<Card>) hand.clone();
		for (int i = 0; i < retArry.length; i++) {
			retArry[i] = whitelist.get((int) (Math.random() * (whitelist.size())));
			whitelist.remove(retArry[i]);
		}
		return retArry;
	}
	
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