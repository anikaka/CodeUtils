package com.tongyan.zhengzhou.act.fragment.patro;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/**
 * 
 * @Title: PatroMapLineSelectFragment.java 
 * @author Rubert
 * @date 2015-3-25 AM 11:07:54 
 * @version V1.0 
 * @Description: 巡查管理-线路选择
 */
public class PatroMapLineSelectFragment extends Fragment {
	
	private SharedPreferences mPreferences;
	
	
	public static PatroMapLineSelectFragment newinstance() {
		return new PatroMapLineSelectFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.patro_map_line_select_fragment, null, false);
		RadioGroup mRadioGroup = (RadioGroup)view.findViewById(R.id.line_list);
		ArrayList<HashMap<String, String>> list = new DBService(getActivity()).getLinesMainList();
		if(list != null) {
			
			mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			
			for(int i = 0,len = list.size(); i < len; i ++) {
				final HashMap<String, String> map = list.get(i);
				if(map != null) {
					RadioButton btn = new RadioButton(getActivity());
					btn.setText(map.get("MetroLineName"));
					btn.setTextColor(Color.parseColor("#000000"));
					btn.setButtonDrawable(R.drawable.map_checkbox_selector);
					mRadioGroup.addView(btn);
					if(mPreferences.getString(Constants.PREFERENCES_LINE_NUMBER, "").equals(map.get("MetroLineId"))) {
						btn.setChecked(true);
					}
					btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Editor mEditor = mPreferences.edit();
							mEditor.putString(Constants.PREFERENCES_LINE_NUMBER, map.get("MetroLineId"));
							mEditor.putString(Constants.PREFERENCES_LINE_NAME, map.get("MetroLineName"));
							mEditor.commit();
						}
					});
				}
			}
		}
		return view;
	}
	
	
	
}
