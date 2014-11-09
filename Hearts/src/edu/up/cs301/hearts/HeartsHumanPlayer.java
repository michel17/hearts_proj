package edu.up.cs301.hearts;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
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

	public HeartsHumanPlayer(String name) {
		super(name);
		backgroundColor = 0xff006400;
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
	public void tick(Canvas canvas) {
		
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
