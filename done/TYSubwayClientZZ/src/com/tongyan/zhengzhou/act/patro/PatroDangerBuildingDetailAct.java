package com.tongyan.zhengzhou.act.patro;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalBaseInfoFragment;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalNoticeFragment;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalPhotoFragment;
import com.tongyan.zhengzhou.act.fragment.proillegalinfo.IllegalVedioFragment;
import com.tongyan.zhengzhou.act.fragment.proprotectinfo.PhotoFileFragment;
/**
 * 
 * @Title: PatroInfoDetailAct.java 
 * @author Rubert
 * @date 2015-3-24 PM 04:13:47 
 * @version V1.0 
 * @Description: 巡查详细 act
 */
public class PatroDangerBuildingDetailAct extends FragmentActivity implements OnClickListener{
	private Button  mBtnBaseInfo, 			//基础信息
	                mBtnDesignFile,   		//告知单
	                mBtnReconnanceFile,	//知单存根
	                mBtnImplementFile,		//图片
	                mBtnPhotoFile
	                ;			//视频
	private LinearLayout mBackBtn;
	private HashMap<String, Object> mMap;
	private TextView mTitle;
	private Context mContext = this;
	private ArrayList<String> picList = new ArrayList<String>();
	private ArrayList<String> vedioList = new ArrayList<String>();
	private String illegalID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_danger_building_detail_info_act);
		MApplication.getInstance().addActivity(this); 
		
		if(getIntent().getExtras()!=null){
			mMap=(HashMap<String, Object>)getIntent().getExtras().get("illegalProInfo");
		}
		mBtnBaseInfo=(Button)findViewById(R.id.btnBaseInfo);
		mBtnDesignFile=(Button)findViewById(R.id.btnDesginFile);
		mBtnReconnanceFile=(Button)findViewById(R.id.btnReconnanceFile);
		mBtnImplementFile=(Button)findViewById(R.id.btnImplementFile);
		mBtnPhotoFile=(Button)findViewById(R.id.btnPhotoFile);
		mTitle = (TextView) findViewById(R.id.title_content);
		mBackBtn = (LinearLayout)findViewById(R.id.title_back_btn);
		
		
		mTitle.setText("违规项目");
		mBtnDesignFile.setText("告知单");
		mBtnReconnanceFile.setText("知单存根");
		mBtnImplementFile.setText("图片");
		mBtnPhotoFile.setText("视频");
		
		mBtnBaseInfo.setOnClickListener(this);
		mBtnDesignFile.setOnClickListener(this);
		mBtnReconnanceFile.setOnClickListener(this);
		mBtnImplementFile.setOnClickListener(this);
		mBtnPhotoFile.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		
		picList.add("/UploadFile/TempImg/20150324094439remit.PNG");
		picList.add("/UploadFile/TempImg/20150324094448111.png");
		picList.add("/UploadFile/TempImg/20150324094439remit.PNG");
		picList.add("/UploadFile/TempImg/20150324094448111.png");
		picList.add("/UploadFile/TempImg/20150324094439remit.PNG");
		picList.add("/UploadFile/TempImg/20150324094439remit.PNG");
		picList.add("/UploadFile/TempImg/20150324094448111.png");
		
		vedioList.add("/UploadFile/TempImg/0001.mp4");
		vedioList.add("/UploadFile/TempImg/0002.mp4");
		vedioList.add("/UploadFile/TempImg/0003.mp4");
		
		if(mMap != null && mMap.size() > 0){
			HashMap<String, String> baseInfo = (HashMap<String, String>) mMap.get("BaseInfo");
			if(baseInfo != null){
				illegalID = baseInfo.get("IllegalID");
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.flLine,IllegalBaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
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
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine,IllegalBaseInfoFragment.getInstance((HashMap<String, String>)mMap.get("BaseInfo"))).commit();
				break;

		case R.id.btnDesginFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnDesignFile.setTextColor(getResources().getColor(R.color.white));
				mBtnDesignFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, IllegalNoticeFragment.getInstance((HashMap<String, String>)mMap.get("IllegalNotice"))).commit();
				break;
				
		case R.id.btnReconnanceFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnReconnanceFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.white));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, IllegalNoticeFragment.getInstance((HashMap<String, String>)mMap.get("IllegalNoticeStub"))).commit();
				break;
				
		case R.id.btnImplementFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnImplementFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnImplementFile.setTextColor((getResources().getColor(R.color.white)));
//				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, IllegalPhotoFragment.getInstance((ArrayList<String>)mMap.get("PhotoFiles"),mContext)).commit();
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, IllegalPhotoFragment.getInstance(picList,mContext)).commit();
				break;
				
		case R.id.btnPhotoFile:
				setBtnDefaultColor();
				setBtnDefaultTextColor();
				mBtnPhotoFile.setBackgroundResource(getResources().getColor(R.color.transparent));
				mBtnPhotoFile.setTextColor(getResources().getColor(R.color.white));
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, IllegalVedioFragment.getInstance(vedioList,illegalID,mContext)).commit();
				break;
		case R.id.title_back_btn:
			finish();
			break;
		}
	}
	
}
