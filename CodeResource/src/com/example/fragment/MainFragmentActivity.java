package com.example.fragment;

import view.sliding.SlidingMenu;

import com.example.coderesource.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainFragmentActivity  extends FragmentActivity{
	
	private SlidingMenu mSlidingMenu;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_fragment);
		initSlidingMenu();
	}

	private void initSlidingMenu(){
		// 设置滑动菜单的属性值
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置目录右侧的阴影区间宽度
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置内容占屏幕宽度的多少dp
		mSlidingMenu.setFadeDegree(1f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		getSupportFragmentManager().beginTransaction().replace(R.id.flMenu,  new Fragment1()).commit();
	}
	
	
	
}
