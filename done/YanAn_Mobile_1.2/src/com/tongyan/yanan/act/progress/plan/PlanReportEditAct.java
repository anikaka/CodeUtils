package com.tongyan.yanan.act.progress.plan;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.ProgressPlanEditListAdpater;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;

import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @className PlanReportEditAct
 * @author wanghb
 * @date 2014-7-7 AM 09:37:21
 * @Desc 计划上传编辑项
 */

public class PlanReportEditAct extends FinalActivity {

	
	private Context mContext = this;

	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	@ViewInject(id=R.id.progress_plan_report_save_btn, click="onSaveListener") Button mSaveBtn;
	@ViewInject(id=R.id.progress_plan_report_clear_btn, click="onClearListener") Button mClearBtn;
	@ViewInject(id=R.id.progress_plan_report_fragment_listview) ListView mListView;

	private static ArrayList<HashMap<String, String>> mCommonList = new ArrayList<HashMap<String, String>>();
	//周计划
//	private String mPeriodId;
	private String mWeekPlanId;
	private String mDayDate;
	private String mDayDateId;
	private String mDataType;
	
	//月计划
	private String mLotId;
	private String mWeekId; //周Id
	private String mMonthPlanId; //月计划ID
	private String mProjectInfo;
	private String mWeekDate; // 周期数 可以为机械或人员 
	private static ProgressPlanEditListAdpater mAdapter;
	private Bundle mBundle;
	
	private SharedPreferences mPreferences;
    private String mUserId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_plan_report_edit);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mUserId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		if (getIntent().getExtras() != null) {
			mBundle = getIntent().getExtras();
			mProjectInfo = mBundle.getString("projectInfo");
			
			if ("周计划".equals(mProjectInfo)) {
				// mPeriodId = mBundle.getString("periodId");
				mWeekPlanId = mBundle.getString("weekPlanId");
				mLotId = mBundle.getString("lotId");
				mDayDate = mBundle.getString("dayDate");
				mDataType = mBundle.getString("DataType");
				mDayDateId = mBundle.getString("dayDateId");

				mTitleContent.setText(mDayDate);
				mSaveBtn.setVisibility(View.VISIBLE);
				// 初始化
				clearProgressValues();
				ArrayList<HashMap<String, String>> mlist = new DBService(mContext).getProgressUpdateInfoList(mLotId, mWeekPlanId, mDayDateId, mUserId);
				if (mlist != null && mlist.size() > 0) {
					new DBService(mContext).updateCacheProgress(mlist);
				}
			} else if ("月计划".equals(mProjectInfo)) {
				mLotId = mBundle.getString("lotId");
				mWeekPlanId = mBundle.getString("weekPlanId");
				mMonthPlanId=mBundle.getString("monthPlanId");
				mWeekId = mBundle.getString("weekId");
				mDataType = mBundle.getString("dataType");
				mWeekDate = mBundle.getString("weekDate");
				if("1".equals(mDataType)) {
					mTitleContent.setText(String.format("第%s周", mWeekDate));
				} else {
					mTitleContent.setText(mWeekDate);
				}
				mSaveBtn.setVisibility(View.VISIBLE);
				// 初始化
				clearProgressValues();
				ArrayList<HashMap<String, String>> mlist = new DBService(mContext).queryTableMonthPlanUpload(mLotId, mWeekId);
				if (mlist != null && mlist.size() > 0) {
					new DBService(mContext).updateCacheProgress(mlist);
				}
			}
			mAdapter = new ProgressPlanEditListAdpater(this, mCommonList, R.layout.progress_plan_report_listview_item);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(mOnItemClickListener);
			refreshListView(this);
		}
	}
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			final ProgressPlanEditListAdpater.ViewHolder mViewHolder = (ProgressPlanEditListAdpater.ViewHolder)arg1.getTag();
		
			if(mViewHolder != null && mViewHolder.mItemData != null ){
				if(new DBService(mContext).queryTableProgressInfoCheckBox(mViewHolder.mItemData.get("NewId"))){
					
				final Dialog mDialog = new Dialog(mContext);
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mDialog.show();
				mDialog.setContentView(R.layout.common_input_dialog);
				mDialog.setCanceledOnTouchOutside(false);
				TextView mTextView = (TextView) mDialog.findViewById(R.id.title_common_content);
				mTextView.setText("输入值");
				final EditText mInputView = (EditText) mDialog.findViewById(R.id.common_content_edit);
				Button mSureBtn = (Button) mDialog.findViewById(R.id.common_save_btn);
				Button mCancleBtn = (Button) mDialog.findViewById(R.id.common_clear_btn);
				
				mInputView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//调用数字键盘
						mInputView.setInputType(3);
					}
				});
				mSureBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String mText = mInputView.getText().toString();
						if("".equals(mText)) {
							Toast.makeText(mContext, "请输入值", Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							Float.parseFloat(mText);
						} catch (Exception e) {
							Toast.makeText(mContext, "请输入正确的数字", Toast.LENGTH_SHORT).show();
							return;
						}
						
						if (mDialog != null) {
							mDialog.dismiss();
						}
						new DBService(mContext).updateProgressInputText(mText, mViewHolder.mItemData.get("ID"));
						refreshListView(mContext);
					}
				});
				mCancleBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mDialog != null) {
							mDialog.dismiss();
						}
					}
				});
				
		 }else{
			 Toast.makeText(mContext, "请先勾选该项", Toast.LENGTH_SHORT).show();
		 }
			}
		}
	};
	
	
	public  void refreshListView(Context context) {
		ArrayList<HashMap<String, String>> list = new DBService(context).getProgressInfoList(mDataType);
		if(mCommonList != null) {
			mCommonList.clear();
			mCommonList.addAll(list);
			list = null;
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 保存
	 * @param v
	 */
	 public void onSaveListener(View v) {
		 AlertDialog.Builder builder = new Builder(mContext);
			builder.setMessage("确认保存吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					saveItem();
				}
			});
			builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
	 }
	 
	 
	 
	 public void saveItem() {
		 ArrayList<HashMap<String, String>> list = new DBService(mContext).getProgressInfoValueList();
		 final  ArrayList<HashMap<String, String>> mCacheList = new  ArrayList<HashMap<String, String>>();
		if(!"null".equals(mProjectInfo)){
		  if("周计划".equals(mProjectInfo)){
		      if(null != list && list.size() > 0) {
			        for(HashMap<String, String> map : list) {
				         if(map != null) {
						       String mInputText = map.get("InputText");
						       String mIsEnable = map.get("IsEnable");
						     if((mInputText != null && !"".equals(mInputText)) || ("true".equals(mIsEnable))) {
							 HashMap<String, String> map0 = new HashMap<String, String>();
							 map0.put("ValueContent", mInputText);
							 map0.put("ItemsId", map.get("NewId"));
							 map0.put("lotId", mLotId);
							 map0.put("WeekPlanId", mWeekPlanId);
							 map0.put("DayId", mDayDateId);
							 map0.put("RealDay", mDayDate);
							 map0.put("ItemsType", map.get("ConstructioType"));
							 mCacheList.add(map0);
					 }
				 }
			 }
			 if(null != mCacheList && mCacheList.size() > 0) {
				 new DBService(mContext).insertProgressUpdateInfo(mCacheList, mUserId);
			 }
		 }
		 new DBService(mContext).updateTableWeekPanDayState(mDayDateId, "1");
		 }else if("月计划".equals(mProjectInfo)){
			 	if(null!=list && list.size()>0){
			 		for(HashMap<String, String> map : list){
			 			if(map!=null){
						      String mInputText = map.get("InputText");//InputText
						      String mIsEnable = map.get("IsEnable");
						      if((mInputText != null && !"".equals(mInputText)) || ("true".equals(mIsEnable))) {
						    	  HashMap<String, String> mMap=new HashMap<String, String>();
						    	  mMap.put("lotId", mLotId);	//合同段ID
						    	  mMap.put("monthPlanId", mMonthPlanId); //月计划ID
						    	  mMap.put("weekId", mWeekId);//
						    	  mMap.put("itemsId", map.get("NewId"));
						    	  mMap.put("itemsType", map.get("ConstructioType"));
						    	  mMap.put("weekNum", mWeekDate);
						    	  mMap.put("value", mInputText);
						    	  mCacheList.add(mMap);
						      }
			 			}
			 		}
			 		if(mCacheList!=null && mCacheList.size() > 0 ){
			 			new DBService(mContext).insertTableMonthPlanUpload(mCacheList);
			 		}
			 	}
			 	new DBService(mContext).updateTableMonthPlanWeekDateState(mWeekId, "1");
			 	
		 	}
		 }
		 clearProgressValues();
		 finish();
	 }
	 
	 public void clearProgressValues() {
		 new DBService(mContext).updateClearProgress();
	 }
	 /**
	  * 清除监听器
	  * @param v
	  */
	 public void onClearListener(View v) {
		 clearProgressValues();
		 refreshListView(this);
	 }
	 
	 
}
