package edu.up.cs301.hearts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

public class HeartsHumanPlayer extends GameHumanPlayer implements Animator {

	// our game state
	protected HeartsState state;

	// our activity
	private Activity myActivity;

	// the animation surface
	private AnimationSurface surface;

	// the background color
	private int backgroundColor;

	private Paint paint;
	private boolean hasChecked, displayStart;
	private char[] alphabet = { 'A', 'B', 'C', 'D' };
	private int design = 0, alpha = 200;
	private Path wallPath;
	private ArrayList<PointF> scorePoint = new ArrayList<>(3);
	private static float width, height;
	private ArrayList<Card> hand;
	private float currentspacing;
	private Card selectedCard;

	public HeartsHumanPlayer(String name) {
		super(name);
		backgroundColor = 0xff006400;
		paint = new Paint();
		selectedCard = null;

		// Set up dummy card arraylist
		hand = new ArrayList<Card>();
	}

	@Override
	public void setAsGui(GameMainActivity activity) {
		myActivity = activity;
		// Load the layout resource for the new configuration
		activity.setContentView(R.layout.hearts_human_player);

		// link the animator (this object) to the animation surface
		surface = (AnimationSurface) myActivity.findViewById(R.id.animation_surface);
		surface.setAnimator(this);

		// read in the card images
		Card.initImages(activity);

		// if the state is not null, simulate having just received the state so
		// that
		// any state-related processing is done
		if (state != null) {
			receiveInfo(state);
		}
	}

	@Override
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	@Override
	public void receiveInfo(GameInfo info) {
		Log.i("HeartsHumanPlayer", "receiving updated state (" + info.getClass() + ")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			surface.flash(Color.RED, 50);
		} else if (!(info instanceof HeartsState)) {
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
			Log.i("human player", "receiving");
		}
	}

	@Override
	public int interval() {
		return 20;
	}

	@Override
	public int backgroundColor() {
		return backgroundColor;
	}

	@Override
	public boolean doPause() {
		return false;
	}

	@Override
	public boolean doQuit() {
		return false;
	}

	@Override
	public void tick(Canvas g) {
		width = g.getWidth();
		height = g.getHeight();

		if (!hasChecked) pointUpdate();
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
		paint.setTextSize(20);
		for (int i = 0; i < scorePoint.size(); i++) {
			g.drawText("Score: " + state.getOverallScore(i), scorePoint.get(i).x, scorePoint.get(i).y, paint);
			if (i == 3) {
				paint.setColor(Color.YELLOW);
				paint.setTextSize(30);
				g.drawText("Filthy Casual", scorePoint.get(i).x - 70, scorePoint.get(i).y - 30, paint);
			} else {
				g.drawText("Player " + String.valueOf(alphabet[i]), scorePoint.get(i).x,
						scorePoint.get(i).y - 30, paint);
			}
		}

		Rect r = new Rect((int) width / 5, (int) ((height / 4) - (height / 8)),
				(int) (width - width / 5), (int) ((height - height / 4) - (height / 8)));
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xff640000);
		g.drawRect(r, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		g.drawText("Current Trick", (float) (width / 4.6), (float) (height / 5.3), paint);
		paint.setColor(Color.YELLOW);
		paint.setTextSize(15);
		g.drawText("01", (float) (width / 3.2), (float) (height / 5.9), paint);

		paint.setStyle(Paint.Style.STROKE);
		g.drawRect(r, paint);
		r = new Rect((int) width / 5, (int) ((height / 4) - (height / 8)), (int) (width / 3),
				(int) ((height / 3) - (height / 8)));

		g.drawRect(r, paint);
		for (design = 0; design < 3; design++) {
			pathHelper();
			g.drawPath(wallPath, paint);

		}
		drawTrick(g);
		drawCards(g);
		
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float tx = event.getX();
			float ty = event.getY();
			Rect trickBoundingBox = new Rect((int) width / 5, (int) ((height / 4) - (height / 8)),
					(int) (width - width / 5), (int) ((height - height / 4) - (height / 8)));
			if (selectedCard != null && trickBoundingBox.contains((int) tx, (int) ty)) {
				// playing card
				game.sendAction(new HeartsPlayAction(this, selectedCard));

			} else {
				// Selection / deselection of cards
				int handIdx = (int) ((hand.size()) * tx / width);
				if (ty > height - (height / 3) && !(hand.get(handIdx) == selectedCard)) {// in
																							// bounds
																							// vertically
					selectedCard = hand.get(handIdx);
				} else {
					selectedCard = null;
				}
			}
		}
	}

	public void pointUpdate() {
		PointF p;
		p = new PointF(width / 10, height / 10);
		scorePoint.add(p);
		p = new PointF(width / 2, height / 10);
		scorePoint.add(p);
		p = new PointF(width - width / 10, height / 10);
		scorePoint.add(p);
		p = new PointF(width / 10, height - (float) (height / 2.5));
		scorePoint.add(p);
		hasChecked = true;
	}

	public void pathHelper() {
		int triangleDepth = 20;
		wallPath = new Path();
		switch (design) {
		case 0:
			wallPath.moveTo(width / 5, height - height / 4 - (height / 8));
			wallPath.lineTo(width / 5, height - height / 4 - triangleDepth - (height / 8));
			wallPath.lineTo(width / 5 + triangleDepth, height - height / 4 - (height / 8));
			wallPath.lineTo(width / 5, height - height / 4 - (height / 8));
			break;
		case 1:
			wallPath.moveTo(width - width / 5, height - height / 4 - (height / 8));
			wallPath.lineTo(width - width / 5, height - height / 4 - triangleDepth - (height / 8));
			wallPath.lineTo(width - width / 5 - triangleDepth, height - height / 4 - (height / 8));
			wallPath.lineTo(width - width / 5, height - height / 4 - (height / 8));
			break;
		case 2:
			wallPath.moveTo(width - width / 5, height / 4 - (height / 8));
			wallPath.lineTo(width - width / 5, height / 4 + triangleDepth - (height / 8));
			wallPath.lineTo(width - width / 5 - triangleDepth, height / 4 - (height / 8));
			wallPath.lineTo(width - width / 5, height / 4 - (height / 8));
			break;
		}
	}

	private void drawCards(Canvas g) {
		currentspacing = hand.size();
		for (int i = 0; i < hand.size(); i++) {
			if (selectedCard != null && hand.get(i).equals(selectedCard)) {
				drawSelectedCard(g, hand.get(i), i);
			} else {
				RectF r = new RectF((width / currentspacing) * i, (height - (height / 3)),
						(width / currentspacing) * i + 150, height);
				hand.get(i).drawOn(g, r);
			}
		}
	}

	private void drawSelectedCard(Canvas g, Card c, int loc) {
		if (c == null) {
			return;
		}
		paint.setColor(Color.YELLOW);
		RectF highlight = new RectF((width / currentspacing) * loc - 1,
				(height - (height / 3)) - 21, (width / currentspacing) * loc + 151, height - 19);
		g.drawRect(highlight, paint);
		RectF r = new RectF((width / currentspacing) * loc, (height - (height / 3)) - 20,
				(width / currentspacing) * loc + 150, height - 20);
		c.drawOn(g, r);
	}

	private void drawTrick(Canvas g) {
		if (state == null) {
			return;
		}
		Card[] trick = state.getCurrentTrick();
		for (int i = 0; i < trick.length; i++) {
			if (trick[i] != null) {
				RectF r = new RectF((width / 5) + i * (width - (2 * width / 5)) / 4 + 20,
						height / 4, (width / 5) + i * (width - (2 * width / 5)) / 4 + 170, height
								- (3 * height / 8) - 20);
				trick[i].drawOn(g, r);
			}
		}
	}
}
