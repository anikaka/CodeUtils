package com.tongyan.yanan.act.progress.plan;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.CommonListViewAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDateNoDayPickerDialog;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category月计划界面
 * @author     ChenLang
 *	@date 	   2014/07/10
 *	@version		YanAn 1.0
 */

public class ProgressMonthProjectAct   extends FinalActivity implements android.view.View.OnClickListener{

	@ViewInject(id=R.id.txtTitle_progress_monthplan) 
	TextView mTxtTitle_progress_monthplan;
	
	@ViewInject(id = R.id.rlAddMonth)
	RelativeLayout mRlAddMonth; // 添加月计划

	@ViewInject(id = R.id.listViewProgressMonthPlan)
	ListView mListViewProgressMonthPlan;
	
	
	 private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	 private ArrayList<HashMap<String, String>> mArrayListSQL=new ArrayList<HashMap<String,String>>();
	 
	 private Context mContext=this;
	 private String     mPeriodId; //期段ID
	 private String     mLotId;		//合同段Id
	 private String  	mLotName;
	 private String 		mPeriodName;//
	 private String  	mMonthPlanId;//月计划Id
	 private String 		mMonthDate;
	 private Bundle    mBundle;
	 private Dialog  mDialogPregressBar;
	 private CommonListViewAdapter mAdapter;

	 private SharedPreferences mSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_monthplan);
		mSp=PreferenceManager.getDefaultSharedPreferences(mContext);
		mDialogPregressBar=new Dialog(mContext, R.style.dialog);
		mDialogPregressBar.setContentView(R.layout.common_normal_progressbar);
		initListener();
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mLotId=mBundle.getString("lotId");
			mLotName=mBundle.getString("lotName");
			mPeriodId=mBundle.getString("periodId");
			mPeriodName=mBundle.getString("periodName");
			mTxtTitle_progress_monthplan.setText(mPeriodName+" "+mLotName);
		}
		//构造适配器
		mAdapter=new CommonListViewAdapter(mContext, mArrayList, R.layout.common_state_listview_item, ProgressMonthProjectAct.class);
		mListViewProgressMonthPlan.setAdapter(mAdapter);
		refreshData();
	}

	/** 初始化监听 */
	public void initListener(){
		mRlAddMonth.setOnClickListener(this);
		mListViewProgressMonthPlan.setOnItemClickListener(listener);
	}
	
	/** ListView 监听*/
  OnItemClickListener listener=new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	CommonListViewAdapter.HolderViewProgressMonthProject  mHolderView=  (CommonListViewAdapter.HolderViewProgressMonthProject)view.getTag();
	HashMap<String, String> mMapHolderView=mHolderView.mMapProgressMonth;
		if(mMapHolderView!=null && mMapHolderView.size()>0){
			mMonthPlanId=mMapHolderView.get("id");
		    mMonthDate=mMapHolderView.get("CommonInfo");
		}
		final Dialog  mDialog=new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_progressproject_upload);
		mDialog.show();
		//填写按钮
		Button mButModify=(Button)mDialog.findViewById(R.id.butModify_dialog_progssproject_upload);
		//上传
		Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_dialog_progssproject_upload);
		//删除
		Button mButDel=(Button)mDialog.findViewById(R.id.butDelete_butModify_dialog_progssproject_upload);
		
		//填写点击事件
		mButModify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if(new DBService(mContext).queryTableMonthPlanWeekDateDataEmpty(mMonthPlanId)==false){
						new DBService(mContext).insertTableMonthPlanWeekDate(mMonthPlanId, "机械", "2");
						new DBService(mContext).insertTableMonthPlanWeekDate(mMonthPlanId, "人员", "3");
						/*for(int i = 1; i <= 4; i++){//
							new DBService(mContext).insertTableMonthPlanWeekDate(mMonthPlanId, String.valueOf(i), "1");
						}*/
						//2014-09-02 modify by Rubert, 将每月分4周改成1个汇总
						new DBService(mContext).insertTableMonthPlanWeekDate(mMonthPlanId, "工程项目", "1");
					}
				Intent intent= new   Intent(mContext,ProgressMonthProjectWeeksAct.class);
				if(mBundle!=null){
					mBundle.putString("monthPlanId",mMonthPlanId);
					mBundle.putString("monthDate",mMonthDate);
					intent.putExtras(mBundle);
				}
				startActivityForResult(intent,Constants.PAGE_BACK_PROGRESS_MONTHPROJECT);
				mDialog.cancel();
			}
		});
		
		//上传或者送审点击事件
		mButUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
					if ("2".equals(new DBService(mContext)
							.queryTableMonthPlanDate(mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),mMonthPlanId))) {
						Toast.makeText(mContext, "已上传", Toast.LENGTH_SHORT).show();
						return;
					} else {
						ArrayList<HashMap<String, String>> mArrayList = new DBService(mContext).queryTableMonthPlanWeekDateAllData(mMonthPlanId);
						for (int i = 0; i < mArrayList.size(); i++) {
							HashMap<String, String> mMap = mArrayList.get(i);
							if ("0".equals(mMap.get("state"))) {
								Toast.makeText(mContext, "有信息未填写完整",Toast.LENGTH_SHORT).show();
								break;
							}
							if (i + 1 == mArrayList.size()&& "1".equals(mMap.get("state"))) {
								mDialogPregressBar.show();
								uploadMonthPlan();
							}
						}
					}
					mDialog.cancel();
			}
		});
		
		//删除点击事件
		mButDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder mDia=new AlertDialog.Builder(mContext);
				mDia.setMessage("确定要删除吗?");
				mDia.setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(!"".equals(mMonthPlanId)){
							boolean   mDelResult=new DBService(mContext).delTableMonthPlanRow(mMonthPlanId);
							if(mDelResult){
								refreshData();
								new DBService(mContext).deleteTableMonthPlanWeekDateInfo(mMonthPlanId);
								Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(mContext, "操作有误,请重新操作", Toast.LENGTH_SHORT).show();
						}
						
						mDialog.cancel();
					}
				});
				mDia.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mDialog.cancel();
					}
				});
				mDia.show();
			}
		});
	}
};
	/** 数据刷新 */
	public  void refreshData(){
	if(mArrayList!=null){
		mArrayList.clear();
		mArrayListSQL=new DBService(mContext).queryMonthPlan(mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),mPeriodId, mLotId);
		if(mArrayListSQL.size()>0){
			mArrayList.addAll(mArrayListSQL);
		}
		mAdapter.notifyDataSetChanged();
		mArrayListSQL.clear();
	}
}
	
@Override
	public void onClick(View v) {
		switch(v.getId()){
		  case  R.id.rlAddMonth:
			  new MDateNoDayPickerDialog(mContext, new MDateNoDayPickerDialog.OnDateTimeSetListener() {
				@Override
				public boolean onDateTimeSet(int year, int monthOfYear) {
					String myDate=String.valueOf(year+"年"+monthOfYear+"月");
					//每当添加计划
					 if(new DBService(mContext).queryTableMonthPlan(mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),mLotId,myDate)){
						 Toast.makeText(mContext, "月计划已经存在", Toast.LENGTH_SHORT).show();
						 return false;
					 }else{
						boolean mInsertDataResult =new DBService(mContext).insertTableMonthPlan(mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),mPeriodId,mLotId,myDate);
						myDate=null;
						if(mInsertDataResult){
							refreshData();
							Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
							return true;
						}else{
							Toast.makeText(mContext, "添加失败,请重试", Toast.LENGTH_SHORT).show();
							return false;
						}
					 }
				}
			}).show();
			  break;
		}
	}
	
	/** 上传月计划*/
	public  void  uploadMonthPlan(){
		new Thread(){
			public void run(){
				HashMap<String, String> mDateMap=new DBService(mContext).queryTableMonthPlanDate(mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),mLotId, mMonthPlanId);
				//查询备注信息
				//ArrayList<HashMap<String, String>> mRemarkList=new DBService(mContext).queryTableWeekMark(mLotId, mMonthPlanId) ;
				String mDate=DateTools.getDate();
				//HashMap<String, String> mRemarkMap=mRemarkList.get(0);
				String mUserId=mSp.getString(Constants.PREFERENCES_INFO_USERID, "");//用户Id
				 StringBuffer mData=new StringBuffer();
				 mData.append("{");//开始
				 //合同段
				 mData.append("\"LotId\"");
				 mData.append(":");
				 mData.append("\""+mLotId+"\"");
				 mData.append(",");
				//创建时间
				 mData.append("\"CreateTime\""); 
				 mData.append(":");
				 mData.append("\""+mDate.replaceAll("\b", " ")+"\"");
				 mData.append(",");
				 //年
				 mData.append("\"Years\"");
				 mData.append(":");
				 mData.append("\""+mDateMap.get("year")+"\"");
				 mData.append(",");
				 //月
				 mData.append("\"Months\"");
				 mData.append(":");
				 mData.append("\""+mDateMap.get("month")+"\"");
				 mData.append(",");
				 //创建人
				 mData.append("\"CreateId\"");
				 mData.append(":");
				 mData.append("\""+mUserId+"\"");
				 mData.append(",");
				 //备注
				 mData.append("\"Remark\"");
				 mData.append(":\""+new DBService(mContext).queryTableMontPlanRemark(mMonthPlanId)+"\"");
				 mData.append(",");
				 //周期值
				 mData.append("\"Items\"");
				 mData.append(":");
				 mData.append("[");
				//=============================================Deprecated======
				/* ArrayList<HashMap<String, String>> mListItemsId=new DBService(mContext).queryTableMonthPlanUploadItemsId(mLotId, mMonthPlanId, "1");
				 if(mListItemsId.size()>0){
					  for(int i=0;i<mListItemsId.size();i++){
						  HashMap<String, String> mMap=mListItemsId.get(i);
							 mData.append("{");
							 mData.append("\"ItemsId\"");
							 mData.append(":");
							 mData.append("\""+mMap.get("itemsId")+"\"");
							 mData.append(",");
							 
							 mData.append("\"SchedulePlanMonth\"");
							 mData.append(":");
							 mData.append("[");
							 ArrayList<HashMap<String, String>> mListUploadInfo=new DBService(mContext).queryTableMonthPlanUploadInfo(mLotId, mMonthPlanId, mMap.get("itemsId"));
							 if(mListUploadInfo.size()>0 && mListUploadInfo!=null){ 								 
								 for(int j=0;j<mListUploadInfo.size();j++){
									 HashMap<String, String>  mMapWeekNum=mListUploadInfo.get(j);
									 mData.append("{");
									 mData.append("\"WeekNum\"");
									 mData.append(":");
									 mData.append("\""+mMapWeekNum.get("weekNum")+"\"");
									 mData.append(",");
									 
									 mData.append("\"WeekDone\"");
									 mData.append(":");
									 mData.append("\""+mMapWeekNum.get("value")+"\"");
									 
									 mData.append("}");
									 if(j+1!=mListUploadInfo.size()){
										 mData.append(",");
									 }
								 }
								 mData.append("]");
								 mData.append("}");
								 if(i+1!=mListItemsId.size()){								 
									 mData.append(",");
								 }
							 }
					  }
					 
				 }else{
					 mData.append("{");
					 mData.append("\"ItemsId\"");
					 mData.append(":");
					 mData.append("\"\"");
					 mData.append(",");
					 
					 mData.append("\"SchedulePlanMonth\"");
					 mData.append(":");
					 mData.append("\"\"");
					 mData.append("}");
				 }*/
				 //=============================================Deprecated======
				 ArrayList<HashMap<String, String>> mListItemsIdList = new DBService(mContext).queryMonthPlanUploadItemsId(mLotId, mMonthPlanId, "1");
				 if(mListItemsIdList != null && mListItemsIdList.size() > 0) {
					 for(int i = 0,len = mListItemsIdList.size(); i < len; i ++) {
						 HashMap<String, String> map = mListItemsIdList.get(i);
						 if(map != null) {
							 mData.append("{");
							 mData.append("\"ItemsId\"");
							 mData.append(":");
							 mData.append("\""+map.get("itemsId")+"\"");
							 mData.append(",");
							 mData.append("\"PlanDone\"");
							 mData.append(":");
							 mData.append("\""+map.get("value")+"\"");
							 mData.append("}");
							 if(i+1 != len ){			 
								 mData.append(",");
							 }
						 }
					 }
				 }
				 mData.append("]");
				 mData.append(",");
				 //mData.append("{");
				  //机械
				  mData.append("\"Machinery\"");
				  mData.append(":");
				  mData.append("[");
				  ArrayList<HashMap<String, String>> mListMachineryItems=new DBService(mContext).queryTableMonthPlanUploadItemsId(mLotId, mMonthPlanId, "2");
				  if(mListMachineryItems.size()>0 &&  mListMachineryItems!=null){
					  for(int k=0; k<mListMachineryItems.size();k++){
						  HashMap<String, String> mMapMachineryItemsId=mListMachineryItems.get(k);
						  String  mItemsId=mMapMachineryItemsId.get("itemsId");
						  ArrayList<HashMap<String, String>> mListMachinery=new DBService(mContext).queryTableMonthPlanUploadInfo(mLotId, mMonthPlanId, mItemsId);
						  for(int l=0;l<mListMachinery.size();l++){
							 HashMap<String, String>  mMapMachinery=mListMachinery.get(l);
							  mData.append("{");
							  
							  mData.append("\"MachineryId\"");
							  mData.append(":");
							  mData.append("\""+mItemsId+"\"");
							  mData.append(",");
							  mData.append("\"MachineryDone\"");
							  mData.append(":");
							  mData.append("\""+mMapMachinery.get("value")+"\"");
							  mData.append("}");
							  
							  if(k+1!=mListMachineryItems.size()){
								  mData.append(",");
							  }
						  }
					  }
				  }
				  mData.append("]");
				  //mData.append("}");
				  mData.append(",");
				  
				  //人员
				  //mData.append("{");
				  ArrayList<HashMap<String, String>> mListPersonnelItems=new DBService(mContext).queryTableMonthPlanUploadItemsId(mLotId, mMonthPlanId, "3");
				  mData.append("\"Personnel\"");
				  mData.append(":");
				  mData.append("[");
				  if(mListPersonnelItems.size()>0){
					  for(int i=0;i<mListPersonnelItems.size();i++){						  
						  HashMap<String, String> mMapItemsId=mListPersonnelItems.get(i);
						  String mItemsId=mMapItemsId.get("itemsId");
						  ArrayList<HashMap<String, String>> mListPersonnel=new DBService(mContext).queryTableMonthPlanUploadInfo(mLotId, mMonthPlanId, mItemsId);
						  for(int j=0;j<mListPersonnel.size();j++){
							  HashMap<String, String> mMap=mListPersonnel.get(j);
							  mData.append("{");
							  
							  mData.append("\"PersonnelId\"");
							  mData.append(":");
							  mData.append("\""+mItemsId+"\"");
							  mData.append(",");
							  mData.append("\"PersonnelDone\"");
							  mData.append(":");
							  mData.append("\""+mMap.get("value")+"\"");
							  mData.append("}");
							  if(i+1!=mListPersonnelItems.size()){
								  mData.append(",");
							  }
						  }
					  }
				  }
				 // mData.append("]");
				  //mData.append("}");
				  mData.append("]");
				 mData.append("}");//结束
				 HashMap<String, String>  mParams=new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_ADDMONTHPLAN);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mUserId);
				String mDataReslut =mData.toString();
			  	mDataReslut=mDataReslut.replaceAll(" ", "20%");
				 mParams.put("data",mDataReslut);
				 String   mTream=null;
				 try{
					 mTream=HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, mParams, mContext));
					if(!"".equals(mTream) && mTream!=null){						
						if(JsonTools.getCommonResult(mTream)){
							sendFMessage(Constants.SUCCESS);
						}else{
							sendFMessage(Constants.COMMON_MESSAGE_1);
						}
					}else{
						 sendFMessage(Constants.ERROR);
					}
				 }catch(Exception e){
					 e.printStackTrace();
					 sendFMessage(Constants.ERROR);
				 }
			}
		}.start();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		// TODO Auto-generated method stub
		super.handleOtherMessage(flag);
		 switch(flag){
				 case Constants.SUCCESS:
					 //修改状态值
					 new DBService(mContext).updateTableMonthPlanState(mMonthPlanId, "2");
					 refreshData();
					 Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
					 mDialogPregressBar.cancel();
					 break;
				 case Constants.ERROR:
					 Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
					 mDialogPregressBar.cancel();
					 break;
				 case Constants.COMMON_MESSAGE_1:
					 Toast.makeText(mContext, "该月已上传,不能上传重复的月计划", Toast.LENGTH_LONG).show();
					 mDialogPregressBar.cancel();
					 break;
		 }
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 switch(requestCode){
		 case Constants.PAGE_BACK_PROGRESS_MONTHPROJECT:
			 refreshData();
			 break;
		 }
	}
}
