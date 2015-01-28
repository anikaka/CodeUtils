package com.tongyan.yanan.fragment;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.quality.QualityTermPartPactAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @category 质量界面
 * @author ChenLang
 * @date 2014/06/18 -
 * @version YanAn 1.0
 * @Modfiy 2014/08/14 by Rubert
 */

public class QualityFragement  extends BaseFragement implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.layout_quality, null, false);
		RelativeLayout mHardGround = (RelativeLayout)mView.findViewById(R.id.quality_hardening_of_ground);
		RelativeLayout mPadLoad = (RelativeLayout)mView.findViewById(R.id.quality_pad_load);
		RelativeLayout mWaveTest = (RelativeLayout)mView.findViewById(R.id.quality_wave_testing);
		RelativeLayout mInjectTest = (RelativeLayout)mView.findViewById(R.id.quality_inject_testing);
		mHardGround.setOnClickListener(this);
		mPadLoad.setOnClickListener(this);
		mWaveTest.setOnClickListener(this);
		mInjectTest.setOnClickListener(this);
		return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quality_hardening_of_ground:
			intentClazz("hardening_of_ground");
			break;
		case R.id.quality_pad_load:
			intentClazz("pad_load");
			break;
		case R.id.quality_wave_testing:
			intentClazz("wave_testing");
			//Toast.makeText(getActivity(), "正在开发中...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.quality_inject_testing:
			intentClazz("inject_testing");
			//Toast.makeText(getActivity(), "正在开发中...", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	public void intentClazz(String type) {
		Intent intent = new Intent(getActivity(), QualityTermPartPactAct.class);
		intent.putExtra("IntentType", type);
		startActivity(intent);
	}
}
