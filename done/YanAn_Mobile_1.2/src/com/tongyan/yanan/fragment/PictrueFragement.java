package com.tongyan.yanan.fragment;


import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.pic.PicCompactionAct;
import com.tongyan.yanan.act.pic.PicGutterAct;
import com.tongyan.yanan.act.pic.PicOriginalAct;
import com.tongyan.yanan.act.pic.PicPactAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * @category 照片界面
 * @author ChenLang
 * @date 2014/06/18
 * @version YanAn 1.0
 */
public class PictrueFragement  extends BaseFragement implements View.OnClickListener{
	
	private View mView;
	private RelativeLayout  mRlLandform,		//原地貌
										 mRlCompaction,	//强夯处理
										 mRlDrain,				//盲沟修筑
										 mRlPhotos;			//定点拍照
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 mView=inflater.inflate(R.layout.layout_pricture, null,false);
		 initComponent();
		return mView;
	}
	
	/** 初始化组件*/
	private void initComponent(){
		mRlLandform=(RelativeLayout)mView.findViewById(R.id.rlLandform);
		mRlCompaction=(RelativeLayout)mView.findViewById(R.id.rlCompaction);
		mRlDrain=(RelativeLayout)mView.findViewById(R.id.rlDrain);
		mRlPhotos=(RelativeLayout)mView.findViewById(R.id.rlPhotos);
		
		mRlLandform.setOnClickListener(this);
		mRlCompaction.setOnClickListener(this);
		mRlDrain.setOnClickListener(this);
		mRlPhotos.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlLandform:		 //原地貌
			    Intent mIntentLandform=new Intent(getActivity(),PicOriginalAct.class);
			    Bundle mBundleL=new Bundle();
			    mBundleL.putString("type", "0");
			    mIntentLandform.putExtras(mBundleL);
			    getActivity().startActivity(mIntentLandform);
			break;
		case R.id.rlCompaction:	 //强夯处理
				Intent  mIntentC=new Intent(getActivity(),PicCompactionAct.class);
				Bundle  mBundleC=new Bundle();
				mBundleC.putString("type","1");
				mIntentC.putExtras(mBundleC);
				getActivity().startActivity(mIntentC);
			break;
		case R.id.rlDrain:				//盲沟修筑
				Intent  mIntentDrain=new Intent(getActivity(),PicGutterAct.class);
				Bundle  mBundleD=new Bundle();
				mBundleD.putString("type","2");
				mIntentDrain.putExtras(mBundleD);
				getActivity().startActivity(mIntentDrain);
			break;
		case R.id.rlPhotos:		   //定点拍照
				Intent  mIntentP=new Intent(getActivity(),PicPactAct.class);
				Bundle  mBundleP=new Bundle();
				mBundleP.putString("type","3");
				mIntentP.putExtras(mBundleP);
				getActivity().startActivity(mIntentP);
			break;
		default:
			break;
		}
	}
}
