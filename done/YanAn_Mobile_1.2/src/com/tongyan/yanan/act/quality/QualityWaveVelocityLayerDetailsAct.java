package com.tongyan.yanan.act.quality;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.QualityWaveVelocityLayerAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: QualityWaveVelocityLayerDetailsAct.java 
 * @author Rubert
 * @date 2014-8-22 下午03:31:59 
 * @version V1.0 
 */
public class QualityWaveVelocityLayerDetailsAct extends FinalActivity{
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.listView_data)  ListView mListView;
	
	private Context mContext = this;
	private static String mQualityWaveVelocityId = null;
	
	private static ArrayList<HashMap<String, String>> mPointList = new ArrayList<HashMap<String, String>>();
	
	private static QualityWaveVelocityLayerAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("检测点列表");
		
		mAdapter = new QualityWaveVelocityLayerAdapter(mContext, mPointList, QualityWaveVelocityLayerDetailsAct.class);
		mListView.setAdapter(mAdapter);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mQualityWaveVelocityId = getIntent().getExtras().getString("QualityWaveVelocityId");
			refreshListView(mContext);
		}
	}
	
	public static void refreshListView(Context context) {
		ArrayList<HashMap<String, String>>  list = new DBService(context).getWaveInjectPointsList(mQualityWaveVelocityId);
		if(mPointList != null) {
			mPointList.clear();
			if(list != null) {
				mPointList.addAll(list);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
	
}
