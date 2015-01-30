package com.tongyan.activity.oa;

import java.util.ArrayList;

import com.tongyan.activity.AbstructFragmentActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.fragment.MFragmentPagerAdapter;
import com.tongyan.fragment.OaContactsAllFragment;
import com.tongyan.fragment.OaContactsDepartmentFragment;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @ClassName P14_ContactAct 
 * @author wanghb
 * @date 2013-7-18 am 09:06:00
 * @desc 移动OA-通讯录-主页
 */
public class OAContactsAct extends AbstructFragmentActivity implements OnClickListener{
	
	private Button mHomeBtn,mMenuDepartmentBtn,mMenuAllBtn;
	private String mIntentType;
	private ViewPager mViewPager;
	private Context mContext = this;
	private ArrayList<Fragment> mFragmentsList;
	private Dialog mDialog;
	private _User mUser;
	
	private ArrayList<String> mRouteList = null;
	private boolean mRouteIsVisiable = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_contacts_main);
		mHomeBtn = (Button)findViewById(R.id.oa_contact_title_home_btn);
		mViewPager = (ViewPager)findViewById(R.id.oa_contacts_viewpager);
		mMenuDepartmentBtn = (Button)findViewById(R.id.oa_contacts_menu_department_btn);
		mMenuAllBtn = (Button)findViewById(R.id.oa_contacts_menu_all_btn);
	}
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(this);
		mMenuDepartmentBtn.setOnClickListener(new MyOnClickListener(0));
		mMenuAllBtn.setOnClickListener(new MyOnClickListener(1));
	}
	
	private void businessM(){
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity( this);
		mUser = mApplication.localUser;
		//判断是否从gps跳转
		if(getIntent() != null && getIntent().getExtras() != null) {
			String params = (String) getIntent().getExtras().get("type");
			if("gps".equals(params)) {
				mIntentType = "gps";
			} else if("route".equals(params)) {
				mIntentType = "route";
				getRouteVisiable();
				mRouteIsVisiable = true;
			} else {
				initFragment();
			}
		} else {
			initFragment();
		}
	}
	
	
	public void initFragment() {
		mFragmentsList = new ArrayList<Fragment>();
		OaContactsAllFragment mOaContactsAllFragment = OaContactsAllFragment.newInstance(mContext, mIntentType, mRouteList,mRouteIsVisiable);
		OaContactsDepartmentFragment mOaContactsDepartmentFragment = OaContactsDepartmentFragment.newInstance(mContext, mIntentType, mRouteList,mRouteIsVisiable);
		mFragmentsList.add(mOaContactsDepartmentFragment);
		mFragmentsList.add(mOaContactsAllFragment);
		mViewPager.setAdapter(new MFragmentPagerAdapter(getSupportFragmentManager(), mFragmentsList));
		mViewPager.setCurrentItem(0);
		selectMenu(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	
	public void getRouteVisiable() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					String str = WebServiceUtils.getRequestStr(mUser.getUsername(), mUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETEMPLOYEEVISIBLE,mContext);
					mRouteList = new Str2Json().getGpsRoute(str);
					if(mRouteList == null) {
						sendMessage(Constansts.ERRER);
					} else {
						sendMessage(Constansts.SUCCESS);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				} 
			}
			
		}).start();
	}
	
	
	
	
	public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
        	mViewPager.setCurrentItem(index);
        }
    };
    
    
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			selectMenu(arg0);
		}
	 };
	
	 
	 public void selectMenu(int index) {
		if (0 == index) {
			mMenuDepartmentBtn.setBackgroundColor(getResources().getColor(R.color.common_color));
			mMenuDepartmentBtn.setTextColor(Color.WHITE);
			mMenuAllBtn.setBackgroundColor(Color.WHITE);
			mMenuAllBtn.setTextColor(Color.BLACK);
			return;
		}
		if (1 == index) {
			mMenuDepartmentBtn.setBackgroundColor(Color.WHITE);
			mMenuDepartmentBtn.setTextColor(Color.BLACK);
			mMenuAllBtn.setBackgroundColor(getResources().getColor(R.color.common_color));
			mMenuAllBtn.setTextColor(Color.WHITE);
			return;
		}
	 }
	
	@Override
	public void onClick(View v) {
		if(v.equals(mHomeBtn)) {
			Intent intent = new Intent(OAContactsAct.this, MainAct.class);
			startActivity(intent);
			return;
		}
	}
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS :
			closeDialog();
			initFragment();
			break;
		case Constansts.ERRER :
			closeDialog();
			Toast.makeText(mContext, "没有用户选择，请在网站配置", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			closeDialog();
			Toast.makeText(mContext, "网络请求超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1 :
			
			break;
		default:
			break;
		}
	}
	
}
