package com.tongyan.zhengzhou.act.patro;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.BaseInfoFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.DesignFileFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.ImplementFileFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.ReconnanceFileFragment;
/**
 * 
 * @Title: PatroInfoDetailAct.java 
 * @author Rubert
 * @date 2015-3-24 PM 04:13:47 
 * @version V1.0 
 * @Description: 巡查详细 act
 */
public class PatroInfoDetailAct extends FragmentActivity implements OnClickListener{
	
	private TextView mTitle;
	private Button  mBtnBaseInfo;			//基础信息
	private Button mBtnDesignFile;   		//设计文件
	private Button mBtnReconnanceFile;	//勘察文件
	private Button mBtnImplementFile;		//实施文件
	
	private HashMap<String, Object> mMap;
	private LinearLayout mBackBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_detail_info_act);
		MApplication.getInstance().addActivity(this); 
		
		if(getIntent().getExtras()!=null){
			mMap=(HashMap<String, Object>)getIntent().getExtras().get("protectProInfo");
		}
		
		mTitle = (TextView) findViewById(R.id.title_content);
		mBtnBaseInfo=(Button)findViewById(R.id.menu_base_info_btn);
		mBtnDesignFile=(Button)findViewById(R.id.menu_design_files_btn);
		mBtnReconnanceFile=(Button)findViewById(R.id.menu_reconnance_files_btn);
		mBtnImplementFile=(Button)findViewById(R.id.menu_implement_files_btn);
		mBackBtn = (LinearLayout)findViewById(R.id.title_back_btn);
		
		mTitle.setText("监护项目");
		
		mBtnBaseInfo.setOnClickListener(this);
		mBtnDesignFile.setOnClickListener(this);
		mBtnReconnanceFile.setOnClickListener(this);
		mBtnImplementFile.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.flLine,BaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
		
	}
	/**
	 * 设置按钮的默认颜色*/
	private void setBtnDefaultColor(){
		mBtnBaseInfo.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnDesignFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnReconnanceFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnImplementFile.setBackgroundResource(R.color.detail_menu_normal_bg);
	}
	
	/**
	 * 设置按钮的字体颜色*/
	private void setBtnDefaultTextColor(){
		mBtnBaseInfo.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnDesignFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnImplementFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.menu_base_info_btn: //基础信息
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnBaseInfo.setTextColor(getResources().getColor(R.color.white));
				mBtnBaseInfo.setBackgroundResource(getResources().getColor(R.color.transparent));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine,BaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
				break;

		case R.id.menu_design_files_btn:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnDesignFile.setTextColor(getResources().getColor(R.color.white));
				mBtnDesignFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, DesignFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("DesignFiles"))).commit();
				break;
				
		case R.id.menu_reconnance_files_btn:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnReconnanceFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.white));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, ReconnanceFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("ReconnanceFiles"))).commit();
				break;
				
		case R.id.menu_implement_files_btn:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnImplementFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnImplementFile.setTextColor((getResources().getColor(R.color.white)));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, ImplementFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("ImplementFiles"))).commit();
				break;
		case R.id.title_back_btn:
			finish();
			break;		
		}
	}
	
}
