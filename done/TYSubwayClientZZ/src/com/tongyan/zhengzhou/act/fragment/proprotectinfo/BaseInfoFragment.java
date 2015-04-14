package com.tongyan.zhengzhou.act.fragment.proprotectinfo;

import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 安保区监护工程基本信息
 * @author ChenLang
 */

public class BaseInfoFragment extends Fragment {

	//private static BaseInfoFragment instance=new BaseInfoFragment();
	private HashMap<String, String> mMapBaseInfo=new HashMap<String, String>();
	private LinearLayout mLLBaseInfo;
	private TextView mProjectName;
	private TextView  mProjectClass;
	private TextView  mFoundationPitClass; //基坑等级
	private TextView  mRedLineDistance;
	private TextView  mDigArea;
	private TextView  mLongitude; //经度
	private TextView  mLatitude;//纬度
	private TextView  mConstructionCompany;//建设单位
	private TextView  mConstructionBy; // 施工单位
	private TextView  mDetectionCompany;// 检测单位
	private TextView  mDesignCompany;//设计单位
	private TextView  mSupervisorCompany; // 监理单位
	private TextView  mSurveyCompany;//勘察单位
	private TextView  mStartTime;//开工时间
	private TextView  mEndTime;//竣工时间
	private TextView  mCreateTime; //创建时间
	private TextView  mProjectAddress;//工程地址
//	private TextView  mProject
	
	
	private void initView(View view){
		mLLBaseInfo=(LinearLayout)view.findViewById(R.id.llBaseIno);
		mProjectName=(TextView)view.findViewById(R.id.protectProBaseInfoProjectName);
		mProjectClass=(TextView)view.findViewById(R.id.protectProBaseInfoProjectClass);
		mFoundationPitClass=(TextView)view.findViewById(R.id.protectProBaseInfoFoundationPitClass);
		mRedLineDistance=(TextView)view.findViewById(R.id.protectProBaseInfoRedLineDistance);
		mDigArea=(TextView)view.findViewById(R.id.protectProBaseInfoDigArea);
		mLongitude=(TextView)view.findViewById(R.id.protectProBaseInfoLongitude);
		mLatitude=(TextView)view.findViewById(R.id.protectProBaseInfoLatitude);
		mConstructionCompany=(TextView)view.findViewById(R.id.protectProBaseInfoConstructionCompany);
		mConstructionBy=(TextView)view.findViewById(R.id.protectProBaseInfoConstructionByy);
		mDetectionCompany=(TextView)view.findViewById(R.id.protectProBaseInfoDetectionCompany);
		mDesignCompany=(TextView)view.findViewById(R.id.protectProBaseInfoDesignCompany);
		mSupervisorCompany=(TextView)view.findViewById(R.id.protectProBaseInfoSupervisorCompany);
		mSurveyCompany=(TextView)view.findViewById(R.id.protectProBaseInfoSurveyCompany);
		mStartTime=(TextView)view.findViewById(R.id.protectProBaseInfoStartTime);
		mEndTime=(TextView)view.findViewById(R.id.protectProBaseInfoEndTime);
		mCreateTime=(TextView)view.findViewById(R.id.protectProBaseInfoCreateTime);
		mProjectAddress=(TextView)view.findViewById(R.id.protectProBaseInfoProjectAddress);
	}
	
	
	private void  initData(){
		if(mMapBaseInfo.size()>0){
			mProjectName.setText(mMapBaseInfo.get("projectName"));
			mProjectClass.setText(mMapBaseInfo.get("projectClass"));
			mFoundationPitClass.setText(mMapBaseInfo.get("foundationPitClass"));
			mRedLineDistance.setText(mMapBaseInfo.get("redLineDistance"));
			mDigArea.setText(mMapBaseInfo.get("digArea"));
			mLongitude.setText(mMapBaseInfo.get("x"));
			mLatitude.setText(mMapBaseInfo.get("y"));
			mConstructionCompany.setText(mMapBaseInfo.get("constructionCompany"));
			mConstructionBy.setText(mMapBaseInfo.get("constructionBy"));
			mDetectionCompany.setText(mMapBaseInfo.get("detectionCompany"));
			mDesignCompany.setText(mMapBaseInfo.get("designCompany"));
			mSupervisorCompany.setText(mMapBaseInfo.get("surveyCompany"));
			mSurveyCompany.setText(mMapBaseInfo.get("surveyCompany"));
			mStartTime.setText(mMapBaseInfo.get("startTime"));
			mEndTime.setText(mMapBaseInfo.get("endTime"));
//			mCreateTime.setText(mMapBaseInfo.get(""));
			mProjectAddress.setText(mMapBaseInfo.get("projectAddress"));
		}
	}
	
	
	public static BaseInfoFragment getInstance(HashMap<String, String> map){
		BaseInfoFragment instance=new BaseInfoFragment();
		if(map!=null){
			instance.mMapBaseInfo.putAll(map);
		}
		return instance;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.protectpro_baseinfo, null, false);
			initView(view);
			if(mMapBaseInfo.size()>0){
				mLLBaseInfo.setVisibility(View.VISIBLE);
				initData();
			} else {
				mLLBaseInfo.setVisibility(View.GONE);
			}
		return view;
	}

}
