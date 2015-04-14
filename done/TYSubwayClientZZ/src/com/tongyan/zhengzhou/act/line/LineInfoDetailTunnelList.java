package com.tongyan.zhengzhou.act.line;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoTunnelAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class LineInfoDetailTunnelList extends Activity {

	
	private Context mContext=this;
	private ListView mListView;
	private LineBaseInfoTunnelAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private void initView(){
		mListView=(ListView)findViewById(R.id.listViewLineLevel5Tunnel);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_baseinfo_tunnel_list);
		if(getIntent().getExtras()!=null){
			if(mArrayList.size()>0){
				mArrayList.clear();
			}
			mArrayList.addAll((ArrayList<HashMap<String, String>>)getIntent().getExtras().get("baseInfo"));
		}
		initView();
		mAdapter=new LineBaseInfoTunnelAdapter(mContext, mArrayList, R.layout.line_baseinfo_listview_level5_tunnel);
		mListView.setAdapter(mAdapter);
	}
	
}
