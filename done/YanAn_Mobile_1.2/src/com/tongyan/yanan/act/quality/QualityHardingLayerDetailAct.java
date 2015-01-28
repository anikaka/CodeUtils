package com.tongyan.yanan.act.quality;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.QualityHardingLayerAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: QualityHardingAddLayerAct.java 
 * @author Rubert
 * @date 2014-8-15 PM 04:20:36 
 * @version V1.0 
 * @Description: 压实度检测层点详情
 */
public class QualityHardingLayerDetailAct extends FinalActivity{
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.listView_data)  ListView mListView;
	
	private Context mContext = this;
	private static String mQualityHardingId = null;
	
	private static ArrayList<HashMap<String, String>> mPointList = new ArrayList<HashMap<String, String>>();
	
	private static QualityHardingLayerAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("压实度检测层");
		
		mAdapter = new QualityHardingLayerAdapter(mContext, mPointList, QualityHardingLayerDetailAct.class);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mQualityHardingId = getIntent().getExtras().getString("QualityHardingId");
			refreshListView(mContext);
		}
	}
	
	public static void refreshListView(Context context) {
		ArrayList<HashMap<String, String>>  list = new DBService(context).getHardingPoint(mQualityHardingId);
		if(mPointList != null) {
			mPointList.clear();
			if(list != null) {
				mPointList.addAll(list);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			QualityHardingLayerAdapter.HolderView mHolderView = (QualityHardingLayerAdapter.HolderView)arg1.getTag();
			HashMap<String, String> map = mHolderView.mItemData;
			if(!"title".equals(map.get("attribute"))) {
				Dialog  mDialog = new Dialog(mContext, R.style.dialog);
				mDialog.setContentView(R.layout.quality_harding_layer_detail_dialog);
				mDialog.show();
				
				TextView mLayerNums = (TextView)mDialog.findViewById(R.id.quality_layer_nums);
				TextView mPressThick = (TextView)mDialog.findViewById(R.id.quality_press_thick);
				TextView mPointX = (TextView)mDialog.findViewById(R.id.quality_point_x);
				TextView mPointY = (TextView)mDialog.findViewById(R.id.quality_point_y);
				TextView mPointZ = (TextView)mDialog.findViewById(R.id.quality_point_z);
				TextView mDryDensity = (TextView)mDialog.findViewById(R.id.quality_dry_density);
				TextView mMaxDryDensity = (TextView)mDialog.findViewById(R.id.quality_max_dry_density);
				TextView mNatureSoil = (TextView)mDialog.findViewById(R.id.quality_nature_of_the_soil);

				mLayerNums.setText(map.get("LayerNum"));
				mPressThick.setText(map.get("Compaction"));
				mPointX.setText(map.get("PointX"));
				mPointY.setText(map.get("PointY"));
				mPointZ.setText(map.get("PointZ"));
				mDryDensity.setText(map.get("Density"));
				mMaxDryDensity.setText(map.get("MaxDensity"));
				mNatureSoil.setText(map.get("EarthNature"));
				
			}
		}
	};
}
