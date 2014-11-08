package edu.up.cs301.hearts;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

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
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int interval() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int backgroundColor() {
		// TODO Auto-generated method stub
		return backgroundColor;
	}

	@Override
	public boolean doPause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doQuit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
