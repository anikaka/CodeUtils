package com.tongyan.activity;

import java.util.ArrayList;

import com.tongyan.fragment.MFragmentPagerAdapter;
import com.tongyan.fragment.MainGpsLocFragment;
import com.tongyan.fragment.MainMeasureFragment;
import com.tongyan.fragment.MainMobileOaFragment;
import com.tongyan.fragment.MainRiskCheckFragment;
import com.tongyan.fragment.MainSupervisionFragment;
import com.tongyan.service.MGPSService;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName P03_MainAct 
 * @author wanghb
 * @date 2013-7-12 pm 05:13:55
 * @desc 主界面在1.9.7版之前，主要有移动OA、GPS定位、监理监督、风险检查
 *       之后新增计量管理模块。采用ViewPage+Button切换的形式
 */
public class MainAct extends AbstructFragmentActivity {
	
	private MyApplication mApplication;
	
	private Button mTitleUserBtn;
	private TextView mTitleContent;
	private ViewPager mPagerView;
    private ArrayList<Fragment> mFragmentsList;
    private View mMobileOaTarget,mMobileMeasureTarget,mGpsLocTarget,mSupervisionTarget,mRiskCheckTarget;
	private TextView mMobileOaText,mMobileMeasureText,mGpsLocText,mSupervisionText,mRiskCheckText;
	
	private static final int MENU_MOBILE_OA_TARGET = 0;
	private static final int MENU_MEASURE_TARGET = 1;
	private static final int MENU_GPS_LOC_TARGET = 2;
	private static final int MENU_SUPERVISION_TARGET = 3;
	private static final int MENU_RISK_CHECK_TARGET = 4;
	
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
	}
	
	private void initPage() {
		setContentView(R.layout.main);
		mTitleUserBtn = (Button)findViewById(R.id.main_title_user_btn);
		mTitleContent = (TextView)findViewById(R.id.main_title_content_text);
		mPagerView = (ViewPager)findViewById(R.id.vPager);
		//移动OA
		mMobileOaTarget = (View)findViewById(R.id.main_mobile_oa_target);
		mMobileOaText = (TextView)findViewById(R.id.main_mobile_oa_text);
		//计量管理
		mMobileMeasureTarget = (View)findViewById(R.id.main_measure_target);
		mMobileMeasureText = (TextView)findViewById(R.id.main_measure_text);
		//GPS定位
		mGpsLocTarget = (View)findViewById(R.id.main_gps_loc_target);
		mGpsLocText = (TextView)findViewById(R.id.main_gps_loc_text);
		//监理监督
		mSupervisionTarget = (View)findViewById(R.id.main_supervision_target);
		mSupervisionText = (TextView)findViewById(R.id.main_supervision_text);
		//风险检查
		mRiskCheckTarget = (View)findViewById(R.id.main_risk_check_target);
		mRiskCheckText = (TextView)findViewById(R.id.main_risk_check_text);
		
		mFragmentsList = new ArrayList<Fragment>();
		MainMobileOaFragment mMainMobileOaFragment = MainMobileOaFragment.newInstance(mContext);
		MainMeasureFragment mMainMobileMeasureFragment = MainMeasureFragment.newInstance(mContext);
		
		MainSupervisionFragment mMainSupervisionFragment = MainSupervisionFragment.newInstance(mContext);
		MainGpsLocFragment mMainGpsLocFragment = MainGpsLocFragment.newInstance(mContext);
		MainRiskCheckFragment mMainRiskCheckFragment = MainRiskCheckFragment.newInstance(mContext);
		//注意添加的顺序
		mFragmentsList.add(mMainMobileOaFragment);
		mFragmentsList.add(mMainMobileMeasureFragment);
		mFragmentsList.add(mMainGpsLocFragment);
		mFragmentsList.add(mMainSupervisionFragment);
		mFragmentsList.add(mMainRiskCheckFragment);
		mPagerView.setAdapter(new MFragmentPagerAdapter(getSupportFragmentManager(), mFragmentsList));
        mPagerView.setCurrentItem(MENU_MOBILE_OA_TARGET);
        mPagerView.setOnPageChangeListener(new MyOnPageChangeListener());
        selectMenu(MENU_MOBILE_OA_TARGET);
	}
	
	
	private void setClickListener() {
		mMobileOaText.setOnClickListener(new MyOnClickListener(0));
		mMobileMeasureText.setOnClickListener(new MyOnClickListener(1));
		mGpsLocText.setOnClickListener(new MyOnClickListener(2));
		mSupervisionText.setOnClickListener(new MyOnClickListener(3));
		mRiskCheckText.setOnClickListener(new MyOnClickListener(4));
		mTitleUserBtn.setOnClickListener(mTitleUserBtnListener);
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		//getMessageCount();
	}
	
	public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
        	mPagerView.setCurrentItem(index);
        	selectMenu(index);
        }
    };
    
    
    OnClickListener mTitleUserBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,SettingAct.class);
			startActivity(intent);
		}
    };
    
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int arg0) {
			selectMenu(arg0);
		}
	 };
	
	
	public void selectMenu(int index) {
		switch (index) {
		case MENU_MOBILE_OA_TARGET:
			mTitleContent.setText(getResources().getString(R.string.title_mobile_oa_name));
			setTargetTextColor();
			mMobileOaTarget.setBackgroundColor(getResources().getColor(R.color.main_bottom_target));
			mMobileOaText.setTextColor(getResources().getColor(R.color.common_black_text));
			break;
		case MENU_MEASURE_TARGET:
			mTitleContent.setText(getResources().getString(R.string.title_measure_name));
			setTargetTextColor();
			mMobileMeasureTarget.setBackgroundColor(getResources().getColor(R.color.main_bottom_target));
			mMobileMeasureText.setTextColor(getResources().getColor(R.color.common_black_text));
			break;
		case MENU_GPS_LOC_TARGET:
			mTitleContent.setText(getResources().getString(R.string.title_gps_loc_name));
			setTargetTextColor();
			mGpsLocTarget.setBackgroundColor(getResources().getColor(R.color.main_bottom_target));
			mGpsLocText.setTextColor(getResources().getColor(R.color.common_black_text));
			break;
		case MENU_SUPERVISION_TARGET:
			mTitleContent.setText(getResources().getString(R.string.title_supervision_name));
			setTargetTextColor();
			mSupervisionTarget.setBackgroundColor(getResources().getColor(R.color.main_bottom_target));
			mSupervisionText.setTextColor(getResources().getColor(R.color.common_black_text));
			break;
		case MENU_RISK_CHECK_TARGET:
			mTitleContent.setText(getResources().getString(R.string.title_risk_check_name));
			setTargetTextColor();
			mRiskCheckTarget.setBackgroundColor(getResources().getColor(R.color.main_bottom_target));
			mRiskCheckText.setTextColor(getResources().getColor(R.color.common_black_text));
			break;
		default:
			break;
		}
	}
	
	
	public void setTargetTextColor() {
		mMobileOaTarget.setBackgroundColor(Color.WHITE);
		mMobileMeasureTarget.setBackgroundColor(Color.WHITE);
		mGpsLocTarget.setBackgroundColor(Color.WHITE);
		mSupervisionTarget.setBackgroundColor(Color.WHITE);
		mRiskCheckTarget.setBackgroundColor(Color.WHITE);
		mMobileOaText.setTextColor(getResources().getColor(R.color.common_gray_text));
		mMobileMeasureText.setTextColor(getResources().getColor(R.color.common_gray_text));
		mGpsLocText.setTextColor(getResources().getColor(R.color.common_gray_text));
		mSupervisionText.setTextColor(getResources().getColor(R.color.common_gray_text));
		mRiskCheckText.setTextColor(getResources().getColor(R.color.common_gray_text));
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
				Intent servicIntentStart = new Intent(mContext, MGPSService.class);
				stopService(servicIntentStart);
				
				ActivityManager activityMgr= (ActivityManager) getApplication().getSystemService(ACTIVITY_SERVICE); 
				activityMgr.killBackgroundProcesses(getPackageName()); 
				mApplication.exit();
			}
		}
		return false;
	}
	
	
	/*@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.ERRER:
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}*/
}
