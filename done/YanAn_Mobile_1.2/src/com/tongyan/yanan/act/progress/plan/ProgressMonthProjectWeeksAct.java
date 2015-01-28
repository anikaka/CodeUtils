package com.tongyan.yanan.act.progress.plan;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.ProgressMonthProjectWeeksListViewAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * @category月计划周数显示周期界面
 * @author chenLang
 * @date 2014/07/14
 * @version YanAn 1.0
 */

public class ProgressMonthProjectWeeksAct extends FinalActivity{

	//标题
	@ViewInject(id=R.id.txtTitle_progress_monthplan_week)
	TextView mTxtTitle_progress_monthplan_week;
	//备注
	@ViewInject(id=R.id.editRemark_progres_monthplan_week)
	EditText mEditRemark_progres_monthplan_week;
	//确认按钮
	@ViewInject(id=R.id.butConfirm_progres_monthplan_week,click="butConfirm")
	Button  mButConfirm_progres_monthplan_week;
	//清除按钮
	@ViewInject(id=R.id.butClear_progres_monthplan_week,click="butCancel")
	Button mButClear_progres_monthplan_week;
	//周期列表
	@ViewInject(id=R.id.listView_progres_monthplan_week)
	ListView  mListView_progres_monthplan_week;
	
	ProgressMonthProjectWeeksListViewAdapter mAdapter;
	
	private ArrayList<HashMap<String, String>>   mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>>   mArrayListSQL=new ArrayList<HashMap<String,String>>();
	
	private String mLotId;
	private Context  mContext=this;
	private  String mMonthPlanId; //月计划Id
    private String mMonthDate;// 日期
	private Bundle mBundle;
	//private SharedPreferences mSP;
	//private String mUserId;
//	private String mDataType;//类型 1 日期,2机械,3人员
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_monthproject_week);
		//mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		//mUserId=mSP.getString(Constants.PREFERENCES_INFO_USERID, "");
		 if(getIntent().getExtras()!=null){
			 mBundle=getIntent().getExtras();
			 mMonthPlanId=mBundle.getString("monthPlanId");
			 mMonthDate=mBundle.getString("monthDate");
			 mLotId=mBundle.getString("lotId");
			 //设置标题
			 mTxtTitle_progress_monthplan_week.setText(mMonthDate);
		 }
		 initRemark();//初始化备注
		// 构造适配器
	   mAdapter=new ProgressMonthProjectWeeksListViewAdapter(mContext, mArrayList, R.layout.common_state_listview_item);
	   mListView_progres_monthplan_week.setAdapter(mAdapter);
	   //设置列表监听
	   mListView_progres_monthplan_week.setOnItemClickListener(listener);
	   refreshData();
	   
	}

	OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			 ProgressMonthProjectWeeksListViewAdapter.ViewHolderProgressMonthProjectWeeksListViewAdapter mViewHolder =(ProgressMonthProjectWeeksListViewAdapter.ViewHolderProgressMonthProjectWeeksListViewAdapter)view.getTag();
			 HashMap<String, String> mMap=mViewHolder.mMapWeekOfMonth;
			 if(mMap!=null && mMap.size()>0){
				 if(mBundle!=null){					  
					 mBundle.putString("weekId", mMap.get("weekId"));
					 mBundle.putString("weekDate", mMap.get("weekDate"));
					 mBundle.putString("dataType",mMap.get("dataType"));
				 }
			 }
			 Intent intent = new Intent(mContext, PlanReportEditAct.class);
			 intent.putExtras(mBundle);
			 startActivityForResult(intent, Constants.PAGE_BACK_PROGRESS_MONTHPROJECT_WEEK);
		}
	};
	
	/** 确认按钮*/
	public void butConfirm(View view){
		new DBService(mContext).updateMonthPlanRemark(mMonthPlanId, mEditRemark_progres_monthplan_week.getText().toString());
		//查询所有的周是否都填写完毕,如果没有填写完毕就不能确认
		ArrayList<HashMap<String, String>> mArrayList = new DBService(mContext).queryTableMonthPlanWeekDateAllData(mMonthPlanId);
		for(int i=0;i<mArrayList.size();i++){
			HashMap<String, String> map=mArrayList.get(i);
			if("0".equals(map.get("state"))){
				Toast.makeText(mContext, "信息没有写完整", Toast.LENGTH_SHORT).show();
				break;
			}
			if("1".equals(map.get("state")) && mArrayList.size()==(i+1)){
				new DBService(mContext).updateTableMonthPlanState(mMonthPlanId, "1");
				setResult(Constants.PAGE_BACK_PROGRESS_MONTHPROJECT_WEEK);
				this.finish();
			}
		}
	}

	
	 /** 初始化备注信息*/
	public void initRemark(){
		mEditRemark_progres_monthplan_week.setText(new DBService(mContext).queryTableMontPlanRemark(mMonthPlanId));
	}
	
	/**清空按钮 */
	public void butCancel(View view){
		new DBService(mContext).updateTableMonthPlanWeekDateAllState(mMonthPlanId, "0");
		new DBService(mContext).deleteTableMonthPlanUpload(mMonthPlanId, mLotId);
		mEditRemark_progres_monthplan_week.setText("");
		refreshData();
	}
	
	/** 数据刷新*/
	public void refreshData(){
		if(mArrayList!=null){
			mArrayList.clear();
			mArrayListSQL=new DBService(mContext).queryTableMonthPlanWeekDateAllData(mMonthPlanId);
			mArrayList.addAll(mArrayListSQL);
			mAdapter.notifyDataSetChanged();
		}
		mArrayListSQL.clear();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
				case  Constants.PAGE_BACK_PROGRESS_MONTHPROJECT_WEEK:
					refreshData();
					break;
		}
	}	
}
