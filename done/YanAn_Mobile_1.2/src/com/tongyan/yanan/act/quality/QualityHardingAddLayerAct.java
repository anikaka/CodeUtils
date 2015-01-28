package com.tongyan.yanan.act.quality;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
 * @Description: 添加压实度检测层
 */
public class QualityHardingAddLayerAct extends FinalActivity{
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.listView_data)  ListView mListView;
	
	
	@ViewInject(id=R.id.common_button_container, click="addHardingLayerListener") RelativeLayout mRightBtn;
	@ViewInject(id=R.id.title_common_add_view) ImageView mAddView;
	
	private Context mContext = this;
	private static String mQualityHardingId = null;
	
	private static ArrayList<HashMap<String, String>> mPointList = new ArrayList<HashMap<String, String>>();
	
	private static QualityHardingLayerAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("添加压实度检测层");
		
		mRightBtn.setVisibility(View.VISIBLE);
		mAddView.setVisibility(View.VISIBLE);
		
		mAdapter = new QualityHardingLayerAdapter(mContext, mPointList, QualityHardingAddLayerAct.class);
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
			final HashMap<String, String> map = mHolderView.mItemData;
			
			final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
			mDialog.setContentView(R.layout.dilog_progress_monthproject);
			mDialog.show();
			
			Button mModifyBtn = (Button)mDialog.findViewById(R.id.butModify_dialog_progssmonthproject);
			Button mDeleteBtn = (Button)mDialog.findViewById(R.id.butUpload_dialog_progssmonthproject);
			
			mModifyBtn.setText("修改");
			mDeleteBtn.setText("删除");
			
			mModifyBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(map != null) {
						String s = map.get("attribute");
						if("title".equals(s)) {
							showModifyDialog(map.get("LayerNum"));
						} else {
							showModifyPointDialog(map);
						}
					}
				}
			});
			
			mDeleteBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					if(map != null) {
						String s = map.get("attribute");
						if("title".equals(s)) {
							new DBService(mContext).delHardingPointsByLayerNums(mQualityHardingId, map.get("LayerNum"));
						} else {
							new DBService(mContext).delHardingPointsById(map.get("ID"));
						}
						refreshListView(mContext);
					}
				}
			});
		}
	};
	
	
	
	
	public void addHardingLayerListener(View v) {
		showDialog(mContext, null);
	}
	
	
	/**
	 * 
	 * @param layerNums
	 */
	public void showModifyDialog(final String layerNums) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_harding_layer_modify_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		
		mLayerNums.setText(layerNums);
		
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				String s = mLayerNums.getText().toString();
				if("".equals(s.trim())) {
					Toast.makeText(mContext, "请填写压实度层数", Toast.LENGTH_SHORT).show();
					return;
				}
				if(new DBService(mContext).updateHardingPointLayer(mQualityHardingId, layerNums, s)) {
					refreshListView(mContext);
				}
			}
		});
		
		
	}
	
	public void showModifyPointDialog(final HashMap<String, String> map) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_harding_layer_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPressThick = (EditText)mDialog.findViewById(R.id.quality_press_thick);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		final EditText mDryDensity = (EditText)mDialog.findViewById(R.id.quality_dry_density);
		final EditText mMaxDryDensity = (EditText)mDialog.findViewById(R.id.quality_max_dry_density);
		final EditText mNatureSoil = (EditText)mDialog.findViewById(R.id.quality_nature_of_the_soil);
		
		if(map != null) {
			mLayerNums.setText(map.get("LayerNum"));
			mLayerNums.setBackgroundColor(Color.GRAY);
			mLayerNums.setEnabled(false);
			
			mPressThick.setText(map.get("Compaction"));
			mPointX.setText(map.get("PointX"));
			mPointY.setText(map.get("PointY"));
			mPointZ.setText(map.get("PointZ"));
			mDryDensity.setText(map.get("Density"));
			mMaxDryDensity.setText(map.get("MaxDensity"));
			mNatureSoil.setText(map.get("EarthNature"));
		}
		
		
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mLayerNumsText = mLayerNums.getText().toString();
				String mPressThickText = mPressThick.getText().toString();
				String mPointXText = mPointX.getText().toString();
				String mPointYText = mPointY.getText().toString();
				String mPointZText = mPointZ.getText().toString();
				String mDryDensityText = mDryDensity.getText().toString();
				String mMaxDryDensityText = mMaxDryDensity.getText().toString();
				String mNatureSoilText = mNatureSoil.getText().toString();
				if("".equals(mLayerNumsText.trim())) {
					Toast.makeText(mContext, "请填写压实度层数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPressThickText.trim())) {
					Toast.makeText(mContext, "请填写压实数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointXText.trim())) {
					Toast.makeText(mContext, "请填写坐标X", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointYText.trim())) {
					Toast.makeText(mContext, "请填写坐标Y", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointZText.trim())) {
					Toast.makeText(mContext, "请填写坐标Z", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mDryDensityText.trim())) {
					Toast.makeText(mContext, "请填写干密度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mMaxDryDensityText.trim())) {
					Toast.makeText(mContext, "请填写最大干密度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mNatureSoilText.trim())) {
					Toast.makeText(mContext, "请填写土的性质", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String, String> m = new HashMap<String, String>();
				m.put("ID", map.get("ID"));
				m.put("HardingId", mQualityHardingId);
				m.put("LayerNum", mLayerNumsText);
				m.put("PointX", mPointXText);
				m.put("PointY", mPointYText);
				m.put("PointZ", mPointZText);
				m.put("Compaction", mPressThickText);
				m.put("Density", mDryDensityText);
				m.put("MaxDensity", mMaxDryDensityText);
				m.put("EarthNature", mNatureSoilText);
				if(new DBService(mContext).updateHardingPoint(m)) {
					refreshListView(mContext);
				}
				mDialog.dismiss();
			}
		});
		
	}
	
	
	public static void showDialog(final Context context, String showType) {
		final Dialog  mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(R.layout.quality_harding_layer_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPressThick = (EditText)mDialog.findViewById(R.id.quality_press_thick);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		final EditText mDryDensity = (EditText)mDialog.findViewById(R.id.quality_dry_density);
		final EditText mMaxDryDensity = (EditText)mDialog.findViewById(R.id.quality_max_dry_density);
		final EditText mNatureSoil = (EditText)mDialog.findViewById(R.id.quality_nature_of_the_soil);
		if(showType != null) {
			mLayerNums.setText(showType);
			mLayerNums.setBackgroundColor(Color.GRAY);
			mLayerNums.setEnabled(false);
		}
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mLayerNumsText = mLayerNums.getText().toString();
				String mPressThickText = mPressThick.getText().toString();
				String mPointXText = mPointX.getText().toString();
				String mPointYText = mPointY.getText().toString();
				String mPointZText = mPointZ.getText().toString();
				String mDryDensityText = mDryDensity.getText().toString();
				String mMaxDryDensityText = mMaxDryDensity.getText().toString();
				String mNatureSoilText = mNatureSoil.getText().toString();
				if("".equals(mLayerNumsText.trim())) {
					Toast.makeText(context, "请填写压实度层数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPressThickText.trim())) {
					Toast.makeText(context, "请填写压实数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointXText.trim())) {
					Toast.makeText(context, "请填写坐标X", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointYText.trim())) {
					Toast.makeText(context, "请填写坐标Y", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mPointZText.trim())) {
					Toast.makeText(context, "请填写坐标Z", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mDryDensityText.trim())) {
					Toast.makeText(context, "请填写干密度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mMaxDryDensityText.trim())) {
					Toast.makeText(context, "请填写最大干密度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mNatureSoilText.trim())) {
					Toast.makeText(context, "请填写土的性质", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("HardingId", mQualityHardingId);
				map.put("LayerNum", mLayerNumsText);
				map.put("PointX", mPointXText);
				map.put("PointY", mPointYText);
				map.put("PointZ", mPointZText);
				map.put("Compaction", mPressThickText);
				map.put("Density", mDryDensityText);
				map.put("MaxDensity", mMaxDryDensityText);
				map.put("EarthNature", mNatureSoilText);
				if(new DBService(context).insertHardingPoint(map)) {
					mDialog.dismiss();
					refreshListView(context);
				}
			}
		});
	}
	
	
	
}
