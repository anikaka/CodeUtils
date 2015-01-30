package com.tygeo.highwaytunnel.activity.hidecheck;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;


/**
 * 隐患排查Tabhost主界面
 * @author chenLang
 */

public class HideCheckTabHost  extends TabActivity implements OnTabChangeListener{


	private Context mContext=this;
	private TabHost mTabHost;
	private ArrayList<HashMap<String, String>> mCheckPorjectItem;
	private String mPorjectCode; //项目Id
	private String mStartMileage; //开始里程
	private String mEndMileage; //结束里程
	private String mCheckFormId;
	
	/** 初始化组件*/
	private void initWidget(){
		mTabHost=getTabHost();
		mTabHost.setOnTabChangedListener(this);
		mCheckPorjectItem=DB_Provider.getCheckPorjectItem(mPorjectCode);
		if(mCheckPorjectItem!=null && mCheckPorjectItem.size()>0){
			for(HashMap<String, String> map: mCheckPorjectItem){
				Intent  intent=new Intent(mContext, HideCheckTabItemActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("checkFormId", mCheckFormId);
				bundle.putString("startMileage", mStartMileage);
				bundle.putString("endMileage", mEndMileage);
				bundle.putString("projectCode", mPorjectCode);
				bundle.putString("itemCode", map.get("code"));
				intent.putExtra("checkBundle", bundle);
				mTabHost.addTab(mTabHost.newTabSpec(map.get("code")).setIndicator(map.get("name")).setContent(intent));
			}
		}
		mTabHost.setCurrentTab(0);
		initTabView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hidecheck_tabhost);
		if(getIntent().getExtras()!=null){
			mCheckFormId=getIntent().getExtras().getString("checkFormId");
			mPorjectCode=getIntent().getExtras().getString("projectCode");
			mStartMileage=getIntent().getExtras().getString("startMileage");
			mEndMileage=getIntent().getExtras().getString("endMileage");
		}
		initWidget();
	}

	@Override
	public void onTabChanged(String tabId) {
		mTabHost.setCurrentTabByTag(tabId);
		initTabView();
	}
	
	/** 
	 * 初始化标签*/
 private  void initTabView(){
	 for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++){
		 View view = mTabHost.getTabWidget().getChildAt(i);
		TextView  txtTitle=(TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
		txtTitle.setWidth(160);
		txtTitle.setHeight(40);
		txtTitle.setTextSize(18);
		txtTitle.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
		txtTitle.setGravity(Gravity.CENTER);
		if(mTabHost.getCurrentTab()==i){
//			mTabHost.setCurrentTab(i);
			view.setBackgroundColor(getResources().getColor(R.color.common_title_or_background_color));
			txtTitle.setTextColor(getResources().getColor(R.color.white_color)); 
		}else{
			view.setBackgroundColor(getResources().getColor(R.color.common_sub_title_background_color));
			txtTitle.setTextColor(getResources().getColor(R.color.black_color));
		}
	 }
}
	
 
}
