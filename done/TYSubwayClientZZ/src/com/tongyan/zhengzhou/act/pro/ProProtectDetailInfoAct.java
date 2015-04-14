package com.tongyan.zhengzhou.act.pro;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalPhotoFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.BaseInfoFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.DesignFileFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.ImplementFileFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.PhotoFileFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.ReconnanceFileFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * 安保区详情信息主界面
 * @author ChenLang
 */

public class ProProtectDetailInfoAct  extends FragmentActivity  implements OnClickListener{
	private Button  mBtnBaseInfo, 			//基础信息
							 mBtnDesignFile,   		//设计文件
							 mBtnReconnanceFile,	//勘察文件
							 mBtnImplementFile,		//实施文件
							 mBtnPhotoFile		//照片文件
							 ;
	private HashMap<String, Object> mMap;
	private LinearLayout mBackBtn;
	private ArrayList<String> mArrayListPhoto=new ArrayList<String>();
	
	/**
	 *  视图初始化*/
	private  void initView(){
		mBackBtn=(LinearLayout)findViewById(R.id.title_back_btn);
		mBtnBaseInfo=(Button)findViewById(R.id.btnBaseInfo);
		mBtnDesignFile=(Button)findViewById(R.id.btnDesginFile);
		mBtnReconnanceFile=(Button)findViewById(R.id.btnReconnanceFile);
		mBtnImplementFile=(Button)findViewById(R.id.btnImplementFile);
		mBtnPhotoFile=(Button)findViewById(R.id.btnPhotoFile);
		mBtnBaseInfo.setOnClickListener(this);
		mBtnDesignFile.setOnClickListener(this);
		mBtnReconnanceFile.setOnClickListener(this);
		mBtnImplementFile.setOnClickListener(this);
		mBtnPhotoFile.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
	}
	
	
	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.protectpro_main);
		MApplication.getInstance().addActivity(this); 
		if(getIntent().getExtras()!=null){
			mMap=(HashMap<String, Object>)getIntent().getExtras().get("protectProInfo");
		}
		initView();
		setArrayListPhoto();
		getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, BaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
	}

	private  void  setArrayListPhoto(){
		ArrayList<HashMap<String, String>> arrayList=(ArrayList<HashMap<String,String>>)mMap.get("PhotoFiles");
		for(int i=0;i<arrayList.size();i++){
			HashMap<String, String> map=arrayList.get(i);
			mArrayListPhoto.add(map.get("filePath"));
		}
	}
	
	/**
	 * 设置按钮的默认颜色*/
	private void setBtnDefaultColor(){
		mBtnBaseInfo.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnDesignFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnReconnanceFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnImplementFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnPhotoFile.setBackgroundResource(R.color.detail_menu_normal_bg);
	}
	
	/**
	 * 设置按钮的字体颜色*/
	private void setBtnDefaultTextColor(){
		mBtnBaseInfo.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnDesignFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnImplementFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnPhotoFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnBaseInfo: //基础信息
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnBaseInfo.setTextColor(getResources().getColor(R.color.white));
				mBtnBaseInfo.setBackgroundResource(getResources().getColor(R.color.transparent));
				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro,BaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
				break;

		case R.id.btnDesginFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnDesignFile.setTextColor(getResources().getColor(R.color.white));
				mBtnDesignFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, DesignFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("DesignFiles"))).commit();
				break;
				
		case R.id.btnReconnanceFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnReconnanceFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.white));
				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, ReconnanceFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("ReconnanceFiles"))).commit();
				break;
				
		case R.id.btnImplementFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnImplementFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnImplementFile.setTextColor((getResources().getColor(R.color.white)));
				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, ImplementFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("ImplementFiles"))).commit();
				break;
				
		case R.id.btnPhotoFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnPhotoFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnPhotoFile.setTextColor(getResources().getColor(R.color.white));
//				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, PhotoFileFragment.getInstance((ArrayList<HashMap<String,String>>)mMap.get("PhotoFiles"))).commit();
				getSupportFragmentManager().beginTransaction().replace(R.id.flProtectPro, IllegalPhotoFragment.getInstance(mArrayListPhoto,getApplicationContext())).commit();
				
				break;
				
		case R.id.title_back_btn:
			finish();
			break;
		}
	}
	
}
