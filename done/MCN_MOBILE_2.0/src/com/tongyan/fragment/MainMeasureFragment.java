package com.tongyan.fragment;

import com.tongyan.activity.R;
import com.tongyan.activity.measure.command.CommandMenuAct;
import com.tongyan.activity.measure.measure.MeasureMenuAct;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
 * @className MainRiskCheckFragment
 * @author wanghb
 * @date 2014-6-11 PM 03:18:57
 * @Desc 主页面- 计量管理
 */
public class MainMeasureFragment extends Fragment implements OnClickListener{
	
	private Context mContext;
	private Button mMainMeasuresBtn;
	private Button  mBtnCommand;
	//private SharedPreferences mPreferences = null;
	
	
	public static MainMeasureFragment newInstance(Context mContext) {
		MainMeasureFragment mFragment = new MainMeasureFragment();
		mFragment.mContext = mContext;
		return mFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		View view = inflater.inflate(R.layout.main_mobile_measures_fragment, container, false);
		mContext=getActivity();
		mMainMeasuresBtn = (Button) view.findViewById(R.id.main_measures_btn);
		mBtnCommand=(Button)view.findViewById(R.id.btnMeasureCommand);
		mMainMeasuresBtn.setOnClickListener(this);
		mBtnCommand.setOnClickListener(this);
		//mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		/**
		 * 计量
		 */
		if(v.equals(mMainMeasuresBtn)) {
			Intent intent = new Intent(mContext, MeasureMenuAct.class);
			startActivity(intent);
			return;
		}
		if(v.equals(mBtnCommand)){
			Intent intent=new Intent(mContext, CommandMenuAct.class);
			startActivity(intent);
			return;
		}
		
	}
}
