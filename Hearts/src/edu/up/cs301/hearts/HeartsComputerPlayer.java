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

	public HeartsComputerPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void receiveInfo(GameInfo info) {
		System.out.println(info.toString());
		Log.i("HeartsComputerPlayer", "receiving updated state (" + info.getClass() + ")");
		if (!(info instanceof HeartsState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		} else {
			// it's a game-state object: update the state. Since we have an
			// animation
			// going, there is no need to explicitly display anything. That will
			// happen
			// at the next animation-tick, which should occur within 1/20 of a
			// second
			this.state = (HeartsState) info;
			hand = state.getPlayerHand(playerNum);
			Log.i("Computer player", "receiving");
		}

	}

	@Override
	protected void timerTicked() {
		if (game != null && game instanceof HeartsLocalGame) {

		}
		Card[] trick = state.getCurrentTrick();
		Suit leadSuit = null;
		if (trick[0] != null) {
			leadSuit = trick[0].getSuit();
		} else {
			ArrayList<Card> whiteList = new ArrayList<Card>();
			Collections.copy(whiteList, hand);
			Card currentCard = whiteList.get((int) (Math.random() * hand.size()));
			while (currentCard.getSuit() == Suit.Heart
					|| (currentCard.getSuit() != leadSuit && leadSuit != null)) {
				if (state.isHeartsBroken()) {
					break;
				} else {
					whiteList.remove(currentCard);
					if (leadSuit == null) {
						currentCard = hand.get((int) (Math.random() * whiteList.size()));
					} else {
						Rank rank = Rank.ACE;
						for (int i = 0; i < trick.length; i++) {
							Rank tempRank = trick[i].getRank();
							if (tempRank.value(14) < rank.value(14)) {
								rank = tempRank;
							}
						}
						Rank lowestRank = Rank.ACE;
						Card lowestRankCard = null;
						for (int i = 0; i < whiteList.size(); i++) {
							Rank tempRank = whiteList.get(i).getRank();
							if (tempRank.value(14) < lowestRank.value(14)) {
								lowestRank = tempRank;
								lowestRankCard = whiteList.get(i);
							}
						}
						if (lowestRank.value(14) < rank.value(14)) {
							ArrayList<Card> tempCards = new ArrayList<Card>();
							for (int i = 0; i < whiteList.size(); i++) {
								if (whiteList.get(i).getRank().value(14) < rank.value(14)) {
									tempCards.add(whiteList.get(i));
								}
							}
							Rank highestRank = Rank.TWO;
							Card highestRankCard = null;
							for (int i = 0; i < tempCards.size(); i++) {
								if (tempCards.get(i).getRank().value(14) > highestRank.value(14)) {
									highestRank = tempCards.get(i).getRank();
									highestRankCard = tempCards.get(i);
								}
							}
							currentCard = highestRankCard;
						} else {
							currentCard = lowestRankCard;
						}

					}
				}
			}
			game.sendAction(new HeartsPlayAction(this, currentCard));
			hand.remove(currentCard);
		}
	}

	public int getPlayerNumber() {
		return playerNum;
	}

}
