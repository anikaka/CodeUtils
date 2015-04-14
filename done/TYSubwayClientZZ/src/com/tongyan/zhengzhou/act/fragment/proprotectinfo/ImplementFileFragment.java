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
 * 实施文件
 * @author ChenLang
 */

public class ImplementFileFragment  extends Fragment {

	
	
	private static ImplementFileFragment   instance=new ImplementFileFragment();
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ListView mListView;
	private ProtectProDesignFlieAdapter mAdapter;
	

	public static ImplementFileFragment  getInstance(ArrayList<HashMap<String, String>> arrayList){
		if(arrayList!=null ){			
			if(instance.mArrayList.size()>0){
				instance.mArrayList.clear();
			}
			instance.mArrayList.addAll(arrayList);
		}
		return instance;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.protectpro_implementfile,null);
			mListView=(ListView)view.findViewById(R.id.listViewImplementFile);
			mAdapter=new ProtectProDesignFlieAdapter(getActivity(), mArrayList, R.layout.protectpro_designfile_item);
			mListView.setAdapter(mAdapter);
		return view;
	}
	
	
}
