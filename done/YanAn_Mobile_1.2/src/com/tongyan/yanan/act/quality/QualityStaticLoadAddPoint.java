package com.tongyan.yanan.act.quality;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.QualityStaticLoadAddPointAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * 监测点列表
 * @author ChenLang
 */
public class QualityStaticLoadAddPoint extends FinalActivity implements OnItemClickListener{
	
	@ViewInject(id=R.id.title_content) TextView mTitlContent;
	//添加检测点
	@ViewInject(id=R.id.rlAddStaticLoadPoint,click="addPoint") RelativeLayout  mAddPoint;
	//检测点列表
	@ViewInject(id=R.id.listViewStaticUploadAddPoint) ListView mListViewExaminePoint;
	
	private Context mContext=this;
	private Bundle mBundle;
	private	 String mNoId;
	private QualityStaticLoadAddPointAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListSQL=new ArrayList<HashMap<String,String>>();
	
	
	
	private String mIntentType = null;
	//private HashMap<String, String> mCacheMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_staticload_add_point);
		if(getIntent().getExtras()!=null){
			mBundle = getIntent().getExtras();
			mNoId = mBundle.getString("noId");
			
			mIntentType = mBundle.getString("IntentType");
			if("details".equals(mIntentType)) {
				mAddPoint.setVisibility(View.GONE);
				mTitlContent.setText(getResources().getString(R.string.examine_poind_info));
			}
		}
		mAdapter=new QualityStaticLoadAddPointAdapter(mContext, mArrayList, R.layout.quality_staticload_add_point_listview_item);
		mListViewExaminePoint.setAdapter(mAdapter);
		mListViewExaminePoint.setOnItemClickListener(this);
		refresh();
	}
	
	/** 添加点*/
	public void addPoint(View v){
		showModifyDialog(null);
	}
	
	public void refresh(){
		mArrayList.clear();
		mArrayListSQL=new DBService(mContext).queryTableExaminePointAllData(mNoId);
		mArrayList.addAll(mArrayListSQL);
		mAdapter.notifyDataSetChanged();
		mArrayListSQL.clear();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		QualityStaticLoadAddPointAdapter.ViewHolderQualityStaticLoadAddPoint mViewHolder=(QualityStaticLoadAddPointAdapter.ViewHolderQualityStaticLoadAddPoint)view.getTag();
		final HashMap<String, String> mMap=mViewHolder.mMapExaminePoint;
		if("add".equals(mIntentType)) {//修改
			final Dialog mDialog=new Dialog(mContext);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.quality_common_dialog);
			mDialog.show();
			Button mButLookOrModify=(Button)mDialog.findViewById(R.id.butLookOrModify);
			Button mButDel=(Button)mDialog.findViewById(R.id.butDelete);
			
			mButLookOrModify.setText("修改");
			mButLookOrModify.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showModifyDialog(mMap);
					mDialog.cancel();
				}
			});
			
			//删除指定列
			mButDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new DBService(mContext).deleteTableExaminePointColumn(mMap.get("id"));
					refresh();
					mDialog.cancel();
				}
			});
		} else {
			showDetailDialog(mMap);
		}
	}
	
	/**
	 * 修改标题
	 * @param layerNums
	 */
	public void showModifyDialog(final HashMap<String, String> map) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_wave_inject_layer_modify_dialog);
		mDialog.show();
		TextView mTitleText = (TextView)mDialog.findViewById(R.id.title_common_content);
		mTitleText.setText("添加监测点");
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		mSaveBtn.setText("添加");
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		if(map != null) {
			mTitleText.setText("修改监测点");
			mSaveBtn.setText("修改");
			mLayerNums.setText(map.get("pointName"));
			mPointX.setText(map.get("pointX"));
			mPointY.setText(map.get("pointY"));
			mPointZ.setText(map.get("pointZ"));
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
				uMap.put("PointName", mLayerNumsText);
				uMap.put("PointX", mPointXText);
				uMap.put("PointY", mPointYText);
				uMap.put("PointZ", mPointZText);
				uMap.put("NoId", mNoId);
				
				if(map != null) {
					uMap.put("ID", map.get("id"));
					new DBService(mContext).updateTableExaminePointData(uMap);
				} else {
					new DBService(mContext).insertTableExaminePointData(uMap);
				}
				refresh();
			}
		});
	}
	
	public void showDetailDialog(final HashMap<String, String> map) {
		final Dialog  mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.quality_wave_inject_layer_modify_dialog);
		mDialog.show();
		
		TextView mTitleText = (TextView)mDialog.findViewById(R.id.title_common_content);
		mTitleText.setText("监测点信息");
		Button mSaveBtn = (Button)mDialog.findViewById(R.id.title_right_btn);
		mSaveBtn.setVisibility(View.GONE);
		final EditText mLayerNums = (EditText)mDialog.findViewById(R.id.quality_layer_nums);
		final EditText mPointX = (EditText)mDialog.findViewById(R.id.quality_point_x);
		final EditText mPointY = (EditText)mDialog.findViewById(R.id.quality_point_y);
		final EditText mPointZ = (EditText)mDialog.findViewById(R.id.quality_point_z);
		if(map != null) {
			mLayerNums.setText(map.get("pointName"));
			mPointX.setText(map.get("pointX"));
			mPointY.setText(map.get("pointY"));
			mPointZ.setText(map.get("pointZ"));
			mLayerNums.setEnabled(false);
			mPointX.setEnabled(false);
			mPointY.setEnabled(false);
			mPointZ.setEnabled(false);
		}
	}
	
	
	
}
