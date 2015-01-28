package com.tongyan.yanan.act.progress.plan;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.ProgressProjectUploadDateListViewAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * @category日期选择界面
 * @author ChenLang
 * @date 2014/07/08
 * @version YanAn 1.0
 */
 
public class ProgressProjectUploadDateAct extends FinalActivity {

	@ViewInject(id = R.id.txtTitle_progress_project_upload_date)
	TextView mTxtTitle_progress_project_upload_date; // 标题
	
	@ViewInject(id = R.id.listView_progress_project_upload_date)
	ListView mListView_progress_project_upload_date;
	
	@ViewInject(id = R.id.butConfirm_progress_project_upload_date,click="butConfirm")
	Button mButConfirm_progress_project_upload_date;
	
	@ViewInject(id = R.id.butClear_progress_project_upload_date,click="butClear")
	Button mButCancel_progress_project_upload_date; //取消按钮
	
	@ViewInject(id = R.id.editRemark)
	EditText mEditRemark; // 备注
	//剩余工期天数
	@ViewInject(id=R.id.editRemainDay)
	EditText mEditRemainDay;
	
	private ArrayList<HashMap<String, String>> mWeekPlanDateList = new ArrayList<HashMap<String, String>>();
	
	private ProgressProjectUploadDateListViewAdapter mAdapter;
	private Context mContext = this;
	private String mPeriodId; // 期数Id
	private String mLotId; //合同段Id
	private String mWeekPlanId; // 周计划Id
	private String mWeekName;
	private Bundle mBundle;
	private String mProjectInfo;
	private String mUserId;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_project_upload_date);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mUserId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		
		if (getIntent().getExtras() != null) {
			mBundle = getIntent().getExtras();
			mPeriodId = mBundle.getString("periodId");
			mLotId=mBundle.getString("lotId");
			mWeekPlanId = mBundle.getString("weekPlanId");
			mWeekName = mBundle.getString("weekName");
			mProjectInfo=mBundle.getString("projectInfo");
			mTxtTitle_progress_project_upload_date.setText("第"+mWeekName+"周");
			mEditRemark.setText(mBundle.getString("remark"));
			mEditRemainDay.setText(mBundle.getString("remainDay"));
		}		
		mAdapter = new ProgressProjectUploadDateListViewAdapter(mContext, mWeekPlanDateList, R.layout.main_progress_upload_date_listview_item);
		mListView_progress_project_upload_date.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(mListView_progress_project_upload_date);
		refreshListView();
		mListView_progress_project_upload_date.setOnItemClickListener(listener);
	}
	
	
	public void refreshListView()  {
		ArrayList<HashMap<String, String>> list = new DBService(mContext).queryTableWeekPanDayAllInfo(mLotId, mWeekPlanId,mUserId);
		if(mWeekPlanDateList != null) {
			mWeekPlanDateList.clear();
			mWeekPlanDateList.addAll(list);
		}
		mAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(mListView_progress_project_upload_date);
	}
	
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProgressProjectUploadDateListViewAdapter.HolderViewProgressProjectUploadDateListViewAdapter mHolderView = (ProgressProjectUploadDateListViewAdapter.HolderViewProgressProjectUploadDateListViewAdapter) view
					.getTag();
			HashMap<String, String> map = mHolderView.mMapDate;
			if (map != null) {
				 Intent intent=new Intent(mContext, PlanReportEditAct.class);
				 intent.putExtra("projectInfo", mProjectInfo);
				 intent.putExtra("lotId", mLotId);
				 intent.putExtra("weekPlanId", mWeekPlanId);
				 intent.putExtra("dayDate", map.get("dayDate"));
				 intent.putExtra("DataType", map.get("DataType"));
				 intent.putExtra("dayDateId", map.get("_Id"));
				 startActivityForResult(intent, 2014);
			}
		}
	};
	
	/** 确认按钮*/
	public void butConfirm(View v){
		if(new DBService(mContext).selectWeekPanDayState(mLotId, mWeekPlanId,mUserId)) {
			new DBService(mContext).updateWeekPlanState(mWeekPlanId, "1", mUserId);
			new DBService(mContext).updateTableWeekPlanRemark(mWeekPlanId,mEditRemark.getText().toString(),mEditRemainDay.getText().toString());
			this.setResult(Constants.PAGE_BACK_PROGRESSPROJECT_UPLOAD_DATE);
			finish();
		} else {
			Toast.makeText(mContext, "您还有任务没做检查", Toast.LENGTH_SHORT).show();
		}
	}
	/**  清除备注按钮*/
	public void butClear(View v){
		new DBService(mContext).updateTableWeekPlanDayState(mUserId, mLotId, "0");
		new DBService(mContext).deleteTableProgressUpdateInfo(mUserId, mWeekPlanId);
		refreshListView();
		mEditRemark.setText("");
	
	}
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2014 || resultCode == 0) {
			new DBService(mContext).updateClearProgress();
			refreshListView();//refresh list
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	 /** 动态改变ListView的高度 */
	 public static void setListViewHeightBasedOnChildren(ListView listView) { 
		    if(listView == null) return;

		    ListAdapter listAdapter = listView.getAdapter(); 
		    if (listAdapter == null) { 
		        return; 
		    } 

		    int totalHeight = 0; 
		    for (int i = 0; i < listAdapter.getCount(); i++) { 
		        View listItem = listAdapter.getView(i, null, listView); 
		        listItem.measure(0, 0); 
		        totalHeight += listItem.getMeasuredHeight(); 
		    } 

		    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
		    listView.setLayoutParams(params); 
		}
}
