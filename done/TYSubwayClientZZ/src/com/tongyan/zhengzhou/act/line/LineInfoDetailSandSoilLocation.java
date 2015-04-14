package com.tongyan.zhengzhou.act.line;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoLevel6SandSoilAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

/**
 * 砂性土壤区时间
 * @author Administrator
 *
 */

public class LineInfoDetailSandSoilLocation extends Activity {

	private Context mContext=this;
	private ListView mListView;
	private LineBaseInfoLevel6SandSoilAdapter mAdapter;
	private ArrayList<HashMap<String, String>>  mArrayList=new ArrayList<HashMap<String,String>>();
	
	
	private void initView(){
		mListView=(ListView)findViewById(R.id.listViewBaseInfoSandSoilLocation);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_baseinfo_sandsoillocation_item);
		MApplication.getInstance().addActivity(this); 
		initView();
		if(getIntent().getExtras()!=null){
			mArrayList.addAll((ArrayList<HashMap<String, String>>)getIntent().getExtras().get("tunnleInfo"));
		}
		mAdapter=new LineBaseInfoLevel6SandSoilAdapter(mContext, mArrayList, R.layout.line_baseinfo_listview_level6_sandsoil_item);
		mListView.setAdapter(mAdapter);
	}
	
	
}
