package edu.up.cs301.hearts;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
	// sizes and locations of card decks and cards, expressed as percentages
		// of the screen height and width
		private final static float CARD_HEIGHT_PERCENT = 50; // height of a card
		private final static float CARD_WIDTH_PERCENT = 17; // width of a card
		private final static float LEFT_BORDER_PERCENT = 4; // width of left border
		private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
		private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders
		
		// our game state
		protected HeartsState state;

		// our activity
		private Activity myActivity;

		// the animation surface
		private AnimationSurface surface;
		
		// the background color
		private int backgroundColor;
		
		private Paint paint;
		private boolean hasChecked;
		private char[] alphabet = { 'A', 'B', 'C', 'D' };
		private int design = 0;
		private Path wallPath;
		private ArrayList<PointF> scorePoint = new ArrayList<>(3);
		private static float width, height;
		private ArrayList<Card> dummyCards;

	public HeartsHumanPlayer(String name) {
		super(name);
		backgroundColor = 0xff006400;
		paint = new Paint();
		
		//Set up dummy card arraylist
		dummyCards = new ArrayList<Card>();
		dummyCards.add(new Card(Rank.ACE, Suit.Club));
		dummyCards.add(new Card(Rank.EIGHT, Suit.Heart));
		dummyCards.add(new Card(Rank.FOUR, Suit.Heart));
		dummyCards.add(new Card(Rank.EIGHT, Suit.Diamond));
		dummyCards.add(new Card(Rank.EIGHT, Suit.Spade));
		dummyCards.add(new Card(Rank.THREE, Suit.Heart));
	}

	@Override
	public void setAsGui(GameMainActivity activity) {
		myActivity = activity;
		// Load the layout resource for the new configuration
				activity.setContentView(R.layout.hearts_human_player);

				// link the animator (this object) to the animation surface
				surface = (AnimationSurface) myActivity
						.findViewById(R.id.animation_surface);
				surface.setAnimator(this);
				
				// read in the card images
				Card.initImages(activity);

				// if the state is not null, simulate having just received the state so that
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
		Log.i("HeartsHumanPlayer", "receiving updated state ("+info.getClass()+")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof HeartsState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
		else {
			// it's a game-state object: update the state. Since we have an animation
			// going, there is no need to explicitly display anything. That will happen
			// at the next animation-tick, which should occur within 1/20 of a second
			this.state = (HeartsState)info;
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
		if (!hasChecked)
			pointUpdate();
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
		paint.setTextSize(20);
		for (int i = 0; i < scorePoint.size(); i++) {
			g.drawText("Score: 0", scorePoint.get(i).x, scorePoint.get(i).y,
					paint);
			if (i == 3) {
				paint.setColor(Color.YELLOW);
				paint.setTextSize(30);
				g.drawText("YOU", scorePoint.get(i).x,
						scorePoint.get(i).y - 30, paint);
			} else {
				g.drawText("Player " + String.valueOf(alphabet[i]),
						scorePoint.get(i).x, scorePoint.get(i).y - 30, paint);
			}
		}

		Rect r = new Rect((int) width / 5, (int) ((height / 4) - (height /8)),
				(int) (width - width / 5), (int) ((height - height / 4) - (height /8)));
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xff640000);
		g.drawRect(r, paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		g.drawText("Current Trick", (float) (width / 4.6),
				(float) (height / 5.3), paint);
		paint.setColor(Color.YELLOW);
		paint.setTextSize(15);
		g.drawText("01", (float) (width/3.2), (float) (height/5.9), paint);
		
		paint.setStyle(Paint.Style.STROKE);
		g.drawRect(r, paint);
		r = new Rect((int) width / 5, (int) ((height / 4) - (height /8)), (int) (width / 3),
				(int) ((height / 3) - (height /8)));

		g.drawRect(r, paint);
		for (design = 0; design < 3; design++) {
			pathHelper();
			g.drawPath(wallPath, paint);

		}
		
		drawCards(g);

		
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		
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
			wallPath.moveTo(width / 5, height - height / 4 - (height /8));
			wallPath.lineTo(width / 5, height - height / 4 - triangleDepth - (height /8));
			wallPath.lineTo(width / 5 + triangleDepth, height - height / 4 - (height /8));
			wallPath.lineTo(width / 5, height - height / 4 - (height /8));
			break;
		case 1:
			wallPath.moveTo(width - width / 5, height - height / 4 - (height /8));
			wallPath.lineTo(width - width / 5, height - height / 4 - triangleDepth - (height /8));
			wallPath.lineTo(width - width / 5 - triangleDepth, height - height / 4 - (height /8));
			wallPath.lineTo(width - width / 5, height - height / 4 - (height /8));
			break;
		case 2:
			wallPath.moveTo(width - width / 5, height / 4 - (height /8));
			wallPath.lineTo(width - width / 5, height / 4 + triangleDepth - (height /8));
			wallPath.lineTo(width - width / 5 - triangleDepth, height / 4 - (height /8));
			wallPath.lineTo(width - width / 5, height / 4 - (height /8));
			break;
		}
	}
	
	private void drawCards(Canvas g) {
		for (int i = 0; i < dummyCards.size(); i++) {
			RectF r = new RectF( (width/13)*i,(height - (height/4)),(width/13)*(i+1),height);
			dummyCards.get(i).drawOn(g,r);
		}
	}
}
