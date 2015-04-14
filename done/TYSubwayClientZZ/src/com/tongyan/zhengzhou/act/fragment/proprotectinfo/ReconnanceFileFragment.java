package com.tongyan.zhengzhou.act.fragment.proprotectinfo;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.ProtectProDesignFlieAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 勘察文件
 * @author ChenLang
 */

public class ReconnanceFileFragment  extends Fragment{

	private static	ReconnanceFileFragment  instance=new ReconnanceFileFragment();
	private ArrayList<HashMap<String, String>> mArrayListFile=new ArrayList<HashMap<String,String>>();
	private ListView mListView;
	private ProtectProDesignFlieAdapter mAdapter;
	
	public static ReconnanceFileFragment getInstance(ArrayList<HashMap<String, String>> arrayList){
		if(arrayList!=null){
			if(instance.mArrayListFile.size()>0){
				instance.mArrayListFile.clear();
			}
			instance.mArrayListFile.addAll(arrayList);
		}
		return instance;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.protectpro_reconnancefile, null);
		mListView=(ListView)view.findViewById(R.id.listViewReconnaceFile);
		mAdapter=new ProtectProDesignFlieAdapter(getActivity(), mArrayListFile, R.layout.protectpro_designfile_item);
		mListView.setAdapter(mAdapter);
		return view;
	}
}
