package com.tongyan.widget.view;

import com.tongyan.activity.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.Button;
/**
 * 
 * @className AnimationButton
 * @author wanghb
 * @date 2014-7-23 PM 03:15:08
 * @Desc TODO
 */
public class AnimationButton extends Button {
	
	
	private AnimationDrawable mAnimationDrawable;
	private boolean Running;
	
	
	public AnimationButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.common_refresh_blue_animation);
		mAnimationDrawable = (AnimationDrawable)getBackground();
	}
	
	public void startAnimation(){
		if(mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
	}
	
	public void stopAnimation() {
		if(mAnimationDrawable != null) {
			mAnimationDrawable.stop();
		}
	}

	public void setRunning(boolean running) {
		Running = running;
	}

	public boolean getRunning() {
		return Running;
	}


}
