package edu.up.cs301.hearts;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.infoMsg.GameInfo;

public class HeartsHumanPlayer extends GameHumanPlayer implements Animator {

	public HeartsHumanPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAsGui(GameMainActivity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getTopView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveInfo(GameInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int interval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int backgroundColor() {
		// TODO Auto-generated method stub
		return 0;
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
