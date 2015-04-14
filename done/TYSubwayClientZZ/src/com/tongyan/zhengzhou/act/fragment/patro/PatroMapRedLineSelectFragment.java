package com.tongyan.zhengzhou.act.fragment.patro;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
/**
 * 
 * @Title: PatroMapLineSelectFragment.java 
 * @author Rubert
 * @date 2015-3-25 AM 11:07:54 
 * @version V1.0 
 * @Description: 巡查管理-红线选择
 * 
 * 采用SharedPreferences保存 key:在 Constants.java中; value为true和flase
 */
public class PatroMapRedLineSelectFragment extends Fragment implements OnClickListener{
	
	private CheckBox mRedLineCheckBox, mStationCheckBox, mintervalCheckBox;
	private SharedPreferences mPreferences;
	
	public static PatroMapRedLineSelectFragment newinstance() {
		return new PatroMapRedLineSelectFragment();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.patro_map_red_line_select_fragment, null, false);
		mintervalCheckBox = (CheckBox)view.findViewById(R.id.interval);
		mStationCheckBox = (CheckBox)view.findViewById(R.id.station);
		mRedLineCheckBox = (CheckBox)view.findViewById(R.id.red_line);
		
		mRedLineCheckBox.setOnClickListener(this);
		mStationCheckBox.setOnClickListener(this);
		mintervalCheckBox.setOnClickListener(this);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		//
		if(mPreferences.getBoolean(Constants.PREFERENCES_INTERVAL, false)) {
			mintervalCheckBox.setChecked(true);
		} else {
			mintervalCheckBox.setChecked(false);
		}
		if(mPreferences.getBoolean(Constants.PREFERENCES_STATION, false)) {
			mStationCheckBox.setChecked(true);
		} else {
			mStationCheckBox.setChecked(false);
		}
		if(mPreferences.getBoolean(Constants.PREFERENCES_RED_LINE, false)) {
			mRedLineCheckBox.setChecked(true);
		} else {
			mRedLineCheckBox.setChecked(false);
		}
		return view;
	}
	
	@Override
	public void onClick(View v) {
		Editor mEditor = mPreferences.edit();
		switch (v.getId()) {
		case R.id.interval:
			if(mPreferences.getBoolean(Constants.PREFERENCES_INTERVAL, true)) {
				mEditor.putBoolean(Constants.PREFERENCES_INTERVAL, false);
			} else {
				mEditor.putBoolean(Constants.PREFERENCES_INTERVAL, true);
			}
			break;
		case R.id.station:
			if(mPreferences.getBoolean(Constants.PREFERENCES_STATION, true)) {
				mEditor.putBoolean(Constants.PREFERENCES_STATION, false);
			} else {
				mEditor.putBoolean(Constants.PREFERENCES_STATION, true);
			}
			break;
        case R.id.red_line:
        	if(mPreferences.getBoolean(Constants.PREFERENCES_RED_LINE, true)) {
        		mEditor.putBoolean(Constants.PREFERENCES_RED_LINE, false);
			} else {
				mEditor.putBoolean(Constants.PREFERENCES_RED_LINE, true);
			}
			break;
		default:
			break;
		}
		mEditor.commit();
	}
	
	
	
	
}
