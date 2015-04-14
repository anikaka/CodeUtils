package com.tongyan.zhengzhou.act;

import com.tongyan.zhengzhou.act.fragment.MainFragmentLineInfo;
import com.tongyan.zhengzhou.act.fragment.MainFragmentMap;
import com.tongyan.zhengzhou.act.fragment.MainFragmentMonitor;
import com.tongyan.zhengzhou.act.fragment.MainFragmentProtectProInfo;
import com.tongyan.zhengzhou.act.fragment.MainFragmentSetUp;
import com.tongyan.zhengzhou.act.fragment.MainFragmentStructure;
import com.tongyan.zhengzhou.common.widgets.slidingmenu.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @Title: MainAct.java 
 * @author Rubert
 * @date 2015-3-2 AM 11:05:43 
 * @version V1.0 
 * @Description: 
 */
public class MainAct extends FragmentActivity implements OnClickListener {
	
	
	public static SlidingMenu mSlidingMenu;
    private ImageButton   mImgBtnBackMenu;
	private Context mContext = this;
	
//	@ViewInject(id = R.id.menu_administrator_layout)
	RelativeLayout administratorLayout;
//	@ViewInject(id = R.id.menu_patrol_manage_layout)
	RelativeLayout patrolManageLayout;
//	@ViewInject(id = R.id.menu_check_manage_layout)
	RelativeLayout checkManageLayout;
//	@ViewInject(id = R.id.menu_monitor_manage_layout)
	RelativeLayout monitorManageLayout;
//	@ViewInject(id = R.id.menu_protect_manage_layout)
	RelativeLayout protectManageLayout;
//	@ViewInject(id = R.id.menu_line_manage_layout)
	RelativeLayout lineManageLayout;
//	@ViewInject(id = R.id.menu_protect_info_manage_layout)
	RelativeLayout protectInfoManageLayout;
//	@ViewInject(id = R.id.menu_administrator_text)
	TextView administratorText;
//	@ViewInject(id = R.id.menu_patrol_manage_text)
	TextView patrolManageText;
//	@ViewInject(id = R.id.menu_check_manage_text)
	TextView checkManageText;
//	@ViewInject(id = R.id.menu_monitor_manage_text)
	TextView monitorManageText;
//	@ViewInject(id = R.id.menu_protect_manage_text)
	TextView protectManageText;
//	@ViewInject(id = R.id.menu_line_manage_text)
	TextView lineManageText;
//	@ViewInject(id = R.id.menu_protect_info_manage_text)
	TextView protectInfoManageText;
//	@ViewInject(id = R.id.menu_patrol_manage_view)
	ImageView patrolManageView;
//	@ViewInject(id = R.id.menu_check_manage_view)
	ImageView checkManageView;
	//@ViewInject(id = R.id.menu_monitor_manage_view)
	ImageView monitorManageView;
	//@ViewInject(id = R.id.menu_protect_manage_view)
	ImageView protectManageView;
//	@ViewInject(id = R.id.menu_line_manage_view)
	ImageView lineManageView;
//	@ViewInject(id = R.id.menu_protect_info_manage_view)
	ImageView protectInfoManageView;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		MApplication.getInstance().addActivity(this); 
		initSlidingMenu();
	}

	
	private void initSlidingMenu() {
		// 设置滑动菜单的属性值
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 还不知道
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置目录右侧的阴影区间宽度
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 4);// 设置内容占屏幕宽度的多少dp
		mSlidingMenu.setFadeDegree(1f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 设置滑动菜单的视图界面
		mSlidingMenu.setMenu(R.layout.main_menu_list_layout);
		
		//mImgBtnBackMenu=(ImageButton)findViewById(R.id.imgBtnMainSliding);
    	//mImgBtnBackMenu.setOnClickListener(this);
		/*administratorLayout.setOnClickListener(this);
		patrolManageLayout.setOnClickListener(this);
		checkManageLayout.setOnClickListener(this);
		monitorManageLayout.setOnClickListener(this);
		protectManageLayout.setOnClickListener(this);
		lineManageLayout.setOnClickListener(this);
		protectInfoManageLayout.setOnClickListener(this);*/
		
//		mImgBtnBackMenu=(ImageButton)mSlidingMenu.findViewById(R.id.imgBtnMainSliding);
		administratorLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_administrator_layout);
		patrolManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_patrol_manage_layout);
		checkManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_check_manage_layout);
		monitorManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_monitor_manage_layout);
		protectManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_protect_manage_layout);
		lineManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_line_manage_layout);
		protectInfoManageLayout=(RelativeLayout)mSlidingMenu.findViewById(R.id.menu_protect_info_manage_layout);
		
		administratorText=(TextView)mSlidingMenu.findViewById(R.id.menu_administrator_text);
		patrolManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_patrol_manage_text);  //
		checkManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_check_manage_text);
		monitorManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_monitor_manage_text);
		protectManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_protect_manage_text);
		protectInfoManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_protect_info_manage_text);
		lineManageText=(TextView)mSlidingMenu.findViewById(R.id.menu_line_manage_text);
		
		patrolManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_patrol_manage_view); 
		checkManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_check_manage_view);
		monitorManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_monitor_manage_view);
		protectManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_protect_manage_view);
		lineManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_line_manage_view);
		protectInfoManageView=(ImageView)mSlidingMenu.findViewById(R.id.menu_protect_info_manage_view);
		
		
		administratorLayout.setOnClickListener(this);
		patrolManageLayout.setOnClickListener(this);
		checkManageLayout.setOnClickListener(this);
		monitorManageLayout.setOnClickListener(this);
		protectManageLayout.setOnClickListener(this);
		protectInfoManageLayout.setOnClickListener(this);
		lineManageLayout.setOnClickListener(this);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentMap.newInstance(mContext)).commit();
	}
	
	
	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View v) {
		
		/*if(v.equals(mImgBtnBackMenu)){
			if(mSlidingMenu.isMenuShowing()){
				onBackPressed();
				mImgBtnBackMenu.setBackgroundDrawable(mFrameAnimMenuMenuMenuBack);
				AnimationDrawable earthButtonAnimation = (AnimationDrawable) mImgBtnBackMenu.getBackground();
			     earthButtonAnimation.start();
				mFrameAnimMenuMenuMenu.stop();
				mFrameAnimMenuMenuMenuBack.start();			
			}else{
				mSlidingMenu.showMenu();
				mImgBtnBackMenu.setBackgroundDrawable(mFrameAnimMenuMenuMenu);
				 AnimationDrawable earthButtonAnimation = (AnimationDrawable) mImgBtnBackMenu.getBackground();
			     earthButtonAnimation.start();
				mFrameAnimMenuMenuMenuBack.stop();
				mFrameAnimMenuMenuMenu.start();
			}
			return;
		}*/
		
		if(v.equals(administratorLayout)) {
			setTextColor();
			setImagePic();
			setBackGroundColor();
			administratorLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentSetUp.newInstance(mContext)).commit();
			mSlidingMenu.showContent();
			return;
		}
		
		if (v.equals(patrolManageLayout)) {
			setTextColor();
			patrolManageText.setTextColor(Color.WHITE);
			setImagePic();
			patrolManageView.setImageResource(R.drawable.menu_inspection_manage_h);
			setBackGroundColor();
			patrolManageLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentMap.newInstance(mContext)).commit();
			mSlidingMenu.showContent();
			return;
		}
		
		if (v.equals(checkManageLayout)) {
			setTextColor();
			checkManageText.setTextColor(Color.WHITE);
			setImagePic();
			checkManageView.setImageResource(R.drawable.menu_checking_manage_h);
			setBackGroundColor();
			checkManageLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentStructure.newInstance()).commit();
			mSlidingMenu.showContent();
			return;
		}
		
		if (v.equals(monitorManageLayout)) {
			setTextColor();
			monitorManageText.setTextColor(Color.WHITE); 
			setImagePic();
			monitorManageView.setImageResource(R.drawable.menu_detection_manage_h);
			setBackGroundColor();
			monitorManageLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentMonitor.newInstance(mContext)).commit();
			mSlidingMenu.showContent();
			return;
		}
		
		if (v.equals(protectManageLayout)) {
			setTextColor();
			protectManageText.setTextColor(Color.WHITE);
			setImagePic();
			protectManageView.setImageResource(R.drawable.menu_protect_manage_h);
			setBackGroundColor();
			protectManageLayout.setBackgroundResource(R.color.lightblue);
			return;
		}
		
		if (v.equals(lineManageLayout)) {
			setTextColor();
			lineManageText.setTextColor(Color.WHITE);
			setImagePic();     
			lineManageView.setImageResource(R.drawable.menu_line_manage_h);
			setBackGroundColor();
			lineManageLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentLineInfo.newInstance(mContext)).commit();
			mSlidingMenu.showContent();
			return;
		}
		
		if (v.equals(protectInfoManageLayout)) {
			setTextColor();
			protectInfoManageText.setTextColor(Color.WHITE);
			setImagePic();
			protectInfoManageView.setImageResource(R.drawable.menu_protect_info_mange_h);
			setBackGroundColor();
			protectInfoManageLayout.setBackgroundResource(R.color.lightblue);
			getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, MainFragmentProtectProInfo.newewInstance(mContext)).commit();
			mSlidingMenu.showContent();
			return;
		}
	}
	
	/**
	 * 设置字体颜色
	 */
	public void setTextColor(){
		administratorText.setTextColor(Color.BLACK);
		patrolManageText.setTextColor(Color.BLACK);
		checkManageText.setTextColor(Color.BLACK);
		monitorManageText.setTextColor(Color.BLACK);
		protectManageText.setTextColor(Color.BLACK);
		lineManageText.setTextColor(Color.BLACK);
		protectInfoManageText.setTextColor(Color.BLACK);
	}
	
	
	/**
	 * 设置图片
	 */
	public void setImagePic(){
		patrolManageView.setImageResource(R.drawable.menu_inspection_manage);
		checkManageView.setImageResource(R.drawable.menu_checking_manage);
		monitorManageView.setImageResource(R.drawable.menu_detection_manage);
		protectManageView.setImageResource(R.drawable.menu_protect_manage);
		lineManageView.setImageResource(R.drawable.menu_line_manage);
		protectInfoManageView.setImageResource(R.drawable.menu_protect_info_mange);
	}
	
	
	/**
	 * 设置背景颜色
	 */
	public void setBackGroundColor(){
		administratorLayout.setBackgroundResource(R.color.grey);
		patrolManageLayout.setBackgroundResource(R.color.grey);
		checkManageLayout.setBackgroundResource(R.color.grey);
		monitorManageLayout.setBackgroundResource(R.color.grey);
		protectManageLayout.setBackgroundResource(R.color.grey);
		lineManageLayout.setBackgroundResource(R.color.grey);
		protectInfoManageLayout.setBackgroundResource(R.color.grey);
	}
	private boolean mIsExitOut = false;
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!mIsExitOut) {
				mIsExitOut = true;
				Toast.makeText(mContext, "再点击一次就退出程序", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							Thread.currentThread().sleep(5000);
							mIsExitOut = false;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} else {
//				ActivityManager activityMgr= (ActivityManager) getApplication().getSystemService(ACTIVITY_SERVICE); 
//				activityMgr.killBackgroundProcesses(getPackageName()); 
				MApplication.getInstance().exit();
			}
		}
		return false;
	}
	
	
}
