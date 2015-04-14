
 package com.tongyan.zhengzhou.act.fragment.structruecheck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @Title: BaseObjectFragment.java 
 * @author Rubert
 * @date 2015-3-16 AM 11:30:40 
 * @version V1.0 
 * @Description: TODO
 */
public class BaseObjectFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView v = new TextView(getActivity());
		v.setText("Testing");
		return v;
	}
}
