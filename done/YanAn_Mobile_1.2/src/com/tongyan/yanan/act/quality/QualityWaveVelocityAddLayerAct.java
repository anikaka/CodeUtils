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
import com.tongyan.yanan.common.adapter.QualityWaveVelocityLayerAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: QualityHardingAddLayerAct.java 
 * @author Rubert
 * @date 2014-8-15 PM 04:20:36 
 * @version V1.0 
 * @Description: 添加波速、贯入检测点
 */
public class QualityWaveVelocityAddLayerAct extends FinalActivity{
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.listView_data)  ListView mListView;
	
	
	@ViewInject(id=R.id.common_button_container, click="addHardingLayerListener") RelativeLayout mRightBtn;
	@ViewInject(id=R.id.title_common_add_view) ImageView mAddView;
	
	private Context mContext = this;
	private static String mQualityWaveVelocityId = null;
	
	private static ArrayList<HashMap<String, String>> mPointList = new ArrayList<HashMap<String, String>>();
	
	private static QualityWaveVelocityLayerAdapter mAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("添加检测点列表");
		
		mRightBtn.setVisibility(View.VISIBLE);
		mAddView.setVisibility(View.VISIBLE);
		
		mAdapter = new QualityWaveVelocityLayerAdapter(mContext, mPointList, QualityWaveVelocityAddLayerAct.class);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(onItemClickListener);
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
	
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
			QualityWaveVelocityLayerAdapter.HolderView mHolderView = (QualityWaveVelocityLayerAdapter.HolderView)arg1.getTag();
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
							showModifyDialog(map);
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
							new DBService(mContext).delWaveInjectPoints(map.get("PointNums"), mQualityWaveVelocityId);
						} else {
							new DBService(mContext).delWaveInjectPointById(map.get("ID"));
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
	 * 修改标题
	 * @param layerNums
	 */
	public void showModifyDialog(final HashMap<String, String> map) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_wave_inject_layer_modify_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		if(map != null) {
			mLayerNums.setText(map.get("PointNums"));
			mPointX.setText(map.get("PointX"));
			mPointY.setText(map.get("PointY"));
			mPointZ.setText(map.get("PointZ"));
		}
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				String mLayerNumsText = mLayerNums.getText().toString();
				String mPointXText = mPointX.getText().toString();
				String mPointYText = mPointY.getText().toString();
				String mPointZText = mPointZ.getText().toString();
				if("".equals(mLayerNumsText.trim())) {
					Toast.makeText(mContext, "请填写检测点号", Toast.LENGTH_SHORT).show();
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
				
				HashMap<String, String> uMap = new HashMap<String, String>();
				uMap.put("PointNums", mLayerNumsText);
				uMap.put("PointX", mPointXText);
				uMap.put("PointY", mPointYText);
				uMap.put("PointZ", mPointZText);
				uMap.put("WaveInjectionId", mQualityWaveVelocityId);
				if(new DBService(mContext).updateWaveInjectPointValues(uMap)) {
					refreshListView(mContext);
				}
			}
		});
		
		
	}
	/**
	 * 修改
	 * @param map
	 */
	public void showModifyPointDialog(final HashMap<String, String> map) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_wave_inject_layer_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		final EditText mDeepValueStart = (EditText)mDialog.findViewById(R.id.quality_deep_value_start);
		final EditText mDeepValueEnd = (EditText)mDialog.findViewById(R.id.quality_deep_value_end);
		final EditText mWaveVelocity = (EditText)mDialog.findViewById(R.id.quality_wave_velocity);
		
		if(map != null) {
			mLayerNums.setText(map.get("PointNums"));
			mLayerNums.setBackgroundColor(Color.GRAY);
			mLayerNums.setEnabled(false);
			
			mPointX.setText(map.get("PointX"));
			mPointX.setBackgroundColor(Color.GRAY);
			mPointX.setEnabled(false);
			
			mPointY.setText(map.get("PointY"));
			mPointY.setBackgroundColor(Color.GRAY);
			mPointY.setEnabled(false);
			
			mPointZ.setText(map.get("PointZ"));
			mPointZ.setBackgroundColor(Color.GRAY);
			mPointZ.setEnabled(false);
			
			mDeepValueStart.setText(map.get("DeepStartValue"));
			mDeepValueEnd.setText(map.get("DeepEndValue"));
			mWaveVelocity.setText(map.get("WaveVelocityValue"));
		}
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mLayerNumsText = mLayerNums.getText().toString();
				String mPointXText = mPointX.getText().toString();
				String mPointYText = mPointY.getText().toString();
				String mPointZText = mPointZ.getText().toString();
				String mDeepValueStartText = mDeepValueStart.getText().toString();
				String mDeepValueEndText = mDeepValueEnd.getText().toString();
				String mWaveVelocityText = mWaveVelocity.getText().toString();
				if("".equals(mLayerNumsText.trim())) {
					Toast.makeText(mContext, "请填写检测点号", Toast.LENGTH_SHORT).show();
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
				if("".equals(mDeepValueStartText.trim())) {
					Toast.makeText(mContext, "请填写深度开始值", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mDeepValueEndText.trim())) {
					Toast.makeText(mContext, "请填写深度结束值", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mWaveVelocityText.trim())) {
					Toast.makeText(mContext, "请填写波速", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String, String> m = new HashMap<String, String>();
				m.put("ID", map.get("ID"));
				m.put("WaveInjectionId", mQualityWaveVelocityId);
				m.put("LayerNum", mLayerNumsText);
				m.put("PointX", mPointXText);
				m.put("PointY", mPointYText);
				m.put("PointZ", mPointZText);
				m.put("DeepStartValue", mDeepValueStartText);
				m.put("DeepEndValue", mDeepValueEndText);
				m.put("WaveVelocityValue", mWaveVelocityText);
				if(new DBService(mContext).updateWaveInjectPoint(m)) {
					refreshListView(mContext);
				}
				mDialog.dismiss();
			}
		});
		
	}
	
	
	public static void showDialog(final Context context, HashMap<String, String> showType) {
		final Dialog  mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(R.layout.quality_wave_inject_layer_dialog);
		mDialog.show();
		
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		final EditText mDeepValueStart = (EditText)mDialog.findViewById(R.id.quality_deep_value_start);
		final EditText mDeepValueEnd = (EditText)mDialog.findViewById(R.id.quality_deep_value_end);
		final EditText mWaveVelocity = (EditText)mDialog.findViewById(R.id.quality_wave_velocity);
		if(showType != null) {
			mLayerNums.setText(showType.get("PointNums"));
			mLayerNums.setBackgroundColor(Color.GRAY);
			mLayerNums.setEnabled(false);
			
			mPointX.setText(showType.get("PointX"));
			mPointX.setBackgroundColor(Color.GRAY);
			mPointX.setEnabled(false);
			
			mPointY.setText(showType.get("PointY"));
			mPointY.setBackgroundColor(Color.GRAY);
			mPointY.setEnabled(false);
			
			mPointZ.setText(showType.get("PointZ"));
			mPointZ.setBackgroundColor(Color.GRAY);
			mPointZ.setEnabled(false);
		}
		
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mLayerNumsText = mLayerNums.getText().toString();
				String mPointXText = mPointX.getText().toString();
				String mPointYText = mPointY.getText().toString();
				String mPointZText = mPointZ.getText().toString();
				String mDeepValueStartText = mDeepValueStart.getText().toString();
				String mDeepValueEndText = mDeepValueEnd.getText().toString();
				String mWaveVelocityText = mWaveVelocity.getText().toString();
				if("".equals(mLayerNumsText.trim())) {
					Toast.makeText(context, "请填写检测点号", Toast.LENGTH_SHORT).show();
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
				if("".equals(mDeepValueStartText.trim())) {
					Toast.makeText(context, "请填写深度开始值", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mDeepValueEndText.trim())) {
					Toast.makeText(context, "请填写深度结束值", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(mWaveVelocityText.trim())) {
					Toast.makeText(context, "请填写波速", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("WaveInjectionId", mQualityWaveVelocityId);
				map.put("PointNums", mLayerNumsText);
				map.put("PointX", mPointXText);
				map.put("PointY", mPointYText);
				map.put("PointZ", mPointZText);
				map.put("DeepStartValue", mDeepValueStartText);
				map.put("DeepEndValue", mDeepValueEndText);
				map.put("WaveVelocityValue", mWaveVelocityText);
				if(new DBService(context).insertWaveInjectPoint(map)) {
					mDialog.dismiss();
					refreshListView(context);
				}
			}
		});
	}
	
	
	
}
