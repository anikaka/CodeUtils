package com.tongyan.yanan.act.progress.plan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.ProgressProjectUploadDateListViewAdapter;
import com.tongyan.yanan.common.adapter.ProgressProjectUploadListViewAdapter;
import com.tongyan.yanan.common.db.DBHelp;
import com.tongyan.yanan.common.db.DBHelper;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.utils.NumberTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category计划上报界面
 * @author ChenLang
 * @date 2014/07/04
 * @version YanAn 1.0
 */

public class ProgressProjectUploadAct extends FinalActivity {
	@ViewInject (id=R.id.txtTitle_progress_project_upload)  TextView mTxtTitle_progress_project_upload;
	@ViewInject (id=R.id.listView_progress_project_upload) ListView mListViewProgress_project_upload;
	
	private ProgressProjectUploadListViewAdapter mListViewAdapter;
	
	private ArrayList<HashMap<String, String>>  mArrayListProgress=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>>  mArrayListProgressRequest=new ArrayList<HashMap<String,String>>();
 	private Context mContext=this;
 	private String mLotId; //合同段Id
 	private String  mLotName;//合同段名称
 	private String mPeriodId;// 期段Id
    private String mPeriodName;//期段名称
    private Dialog mDialogProgressBar; //进度条
    private Bundle  mBundle;
    private String mWeekPlanId;
    
    private String mProjectInfo; //计划类型;
    private SharedPreferences mSp;
    
    private String mUserId;
    
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_project_upload);
		mSp=PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserId = mSp.getString(Constants.PREFERENCES_INFO_USERID, "");
		//进度条
		mDialogProgressBar=new Dialog(mContext,R.style.dialog);
		mDialogProgressBar.setContentView(R.layout.common_normal_progressbar);
    	mDialogProgressBar.show();
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mPeriodId=mBundle.getString("periodId");
			mPeriodName=mBundle.getString("periodName");
			mProjectInfo=mBundle.getString("projectInfo");
			mLotId=mBundle.getString("lotId");
			mLotName=mBundle.getString("lotName");
			mTxtTitle_progress_project_upload.setText(mPeriodName+" "+mLotName);
		}
		
		requestContent();
		//构造适配器
		mListViewAdapter=new ProgressProjectUploadListViewAdapter(this, mArrayListProgress, R.layout.mian_progress_project_upload_listview_item);
		mListViewProgress_project_upload.setAdapter(mListViewAdapter);
		mListViewProgress_project_upload.setOnItemClickListener(listener);
	}
	

 	/** ListView 监听事件*/
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProgressProjectUploadListViewAdapter.ViewHolderProgressProjectUpload mViewHolder =(ProgressProjectUploadListViewAdapter.ViewHolderProgressProjectUpload)view.getTag();
		  final	HashMap<String, String> mMap = mViewHolder.mMapProgressProjectUpload;
		  mWeekPlanId=mMap.get("weekPlanId");//weekPlanId
		  if(mMap != null) {
			  showDialog(mMap);
		  }
		}
	};
	
	
	public void showDialog(final HashMap<String, String> mMap) {
			final	Dialog  mDialog=new Dialog(mContext);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.dialog_progressproject_upload);
			mDialog.show();
			//填写/查看按钮
			Button mButModify=(Button)mDialog.findViewById(R.id.butModify_dialog_progssproject_upload);
			//上传
			Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_dialog_progssproject_upload);
			//删除
			Button mButDel=(Button)mDialog.findViewById(R.id.butDelete_butModify_dialog_progssproject_upload);
			//查看
			mButModify.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent    intent=new Intent(mContext,ProgressProjectUploadDateAct.class);
					if(mBundle!=null){
						mBundle.putString("weekName", mMap.get("CommonInfo"));  
						mBundle.putString("weekPlanId", mMap.get("weekPlanId"));
						mBundle.putString("remark", mMap.get("remark"));
						mBundle.putString("remainDay", mMap.get("remainDay"));
						intent.putExtras(mBundle);
					}
					startActivityForResult(intent, Constants.PAGE_BACK_PROGRESSPROJECT_UPLOAD_DATE);
					mDialog.cancel();
				}
			});
			
			//上传或者送审
			mButUpload.setOnClickListener(new  View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if("0".equals(mMap.get("State"))) {
						Toast.makeText(mContext, "该记录还未完成", Toast.LENGTH_SHORT).show();
						return;
					}
					if("2".equals(mMap.get("State"))) {
						Toast.makeText(mContext, "该记录已上传", Toast.LENGTH_SHORT).show();
						return;
					}
					// 查询周期计划日期表中所有填写的状态,如果状态值为0说明还有资料没有填写完成
				/*	ArrayList<HashMap<String, String>> mArrayList = new DBService(mContext).queryTableWeekPanDayAllInfo(mLotId,mMap.get("weekPlanId"));
				if (mArrayList != null && mArrayList.size() > 0) {
					for (int i = 0; i < mArrayList.size(); i++) {
						HashMap<String, String> map = mArrayList.get(i);
						String mState = Mmap.get("state");
						if ("0".equals(mState)) {
							sendFMessage(Constants.INFA_EMPTY);
							break;
						}
						// 当容器全部遍历完,状态全部为1的时候就上传数据
						if (i == mArrayList.size() - 1 && "1".equals(mState)) {
							mDialogProgressBar.show();
							uploadWeekPlan(mMap);
						}
						// 当状态全部为2的时候就送审数据
					}
					}*/
					uploadWeekPlan(mMap);
					mDialog.cancel();
				}
			});
			//删除
			mButDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder mDia=new AlertDialog.Builder(mContext);
					mDia.setMessage("确定删除吗?");
					mDia.setPositiveButton("是", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 boolean   mDelDataResult=	new DBService(mContext).delTableWeekPlanData(mWeekPlanId,mUserId);
								if(mDelDataResult){
									new DBService(mContext).delTableWeekPlanDay(mUserId,mWeekPlanId);
									Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
									refreshProgress();
								}else{
									Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
								}
								mDialog.cancel();
						}
					});
					mDia.setNegativeButton("否", new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mDialog.cancel();
						}
						
					});
					mDia.show();
				}
			});
	}
	
	/** 周期计划上传*/
	public void  uploadWeekPlan(final HashMap<String, String> map){
		new Thread(){
			public void run(){ 
				//查询上传的ItemsId
				HashMap<String, ArrayList<HashMap<String,String>>> mArrayListItemsId=new DBService(mContext).selectProgressUpdateInfoItems(mWeekPlanId, mUserId);
				ArrayList<HashMap<String, String>> mMachineryList = new DBService(mContext).selectProgressUpdateInfo(mWeekPlanId, "2", mUserId);
				ArrayList<HashMap<String, String>> mPersonList = new DBService(mContext).selectProgressUpdateInfo(mWeekPlanId, "3", mUserId);
				String mUserId = mSp.getString(Constants.PREFERENCES_INFO_USERID, "");
				String mDate= DateTools.getDateTime();
				StringBuffer mData=new StringBuffer();
				mData.append("{"); //开始标记
				mData.append("\"LotId\"");
				mData.append(":");
				mData.append("\""+mLotId+"\"");
				mData.append(",\"WeekId\":");
				mData.append("\""+mWeekPlanId+"\",");
				mData.append("\"CreateTime\"");
				mData.append(":");
				mData.append("\""+mDate+"\"");
				mData.append(",");
				mData.append("\"CreateId\""); //创建人
				mData.append(":");
				mData.append("\""+ mUserId +"\"");
				mData.append(",");
				//备注
				mData.append("\"Remark\""); 
				mData.append(":\""); 
				mData.append(JsonTools.getURLEncoderString(map.get("remark")));
				mData.append("\","); 
				//剩余工期(天数)
				mData.append("\"ResidueDay\"");
				mData.append(":");
				mData.append("\""+map.get("remainDay")+"\"");
				mData.append(",");
				mData.append("\"Items\"");
				mData.append(":");
				mData.append("["); //开始值
				
				int mMapNum = 0;
				int mMapCount = 0;
				if(mArrayListItemsId != null && mArrayListItemsId.size() > 0) {
					mMapCount = mArrayListItemsId.size();
					for(Map.Entry<String, ArrayList<HashMap<String,String>>> m : mArrayListItemsId.entrySet()) {
						if(m != null) {
							String key = m.getKey();
							ArrayList<HashMap<String,String>> list = m.getValue();
							mData.append("{");
							mData.append("\"ItemsId\"");
							mData.append(":");
							mData.append("\""+key+"\"");
							mData.append(",");
							mData.append("\"SchedulePlanWeekDay\"");
							mData.append(":");
							mData.append("[");
							if(list != null && list.size() > 0) {
								for(int i = 0,len = list.size(); i < len; i ++) {
									HashMap<String, String> map = list.get(i);
									if(map != null) {
										mData.append("{\"");
										mData.append("RealDay\"");
										mData.append(":");
										mData.append("\""+map.get("RealDay")+"\"");
										mData.append(",");
										mData.append("\"Plan\"");
										mData.append(":");
										mData.append("\""+map.get("ValueContent")+"\"");
										mData.append("}");
										if(i != len-1 ){								
											mData.append(",");
										}
									}
								}
							}
							mData.append("]");
							++mMapNum;
							mData.append("}");
							if(mMapNum != mMapCount) {
								mData.append(",");
							}
						}
					}
				}
				
				mData.append("]");
				mData.append(",");
				mData.append("\"Machinery\"");  //添加机械开始标记
				mData.append(":");
				mData.append("[");
				if(mMachineryList != null && mMachineryList.size() > 0) {
					for(int i = 0,len = mMachineryList.size(); i < len; i ++) {
						HashMap<String, String> map = mMachineryList.get(i);
						if(map != null) {
							mData.append("{");
							mData.append("\"MachineryId\"");
							mData.append(":");
							mData.append("\""+map.get("itemsId")+"\"");
							mData.append(",");
							mData.append("\"MachineryDone\"");
							mData.append(":");
							mData.append("\""+map.get("value")+"\"");
							mData.append("}");
							if(i != len - 1){
								mData.append(",");
							}
						}
					}
				}
				mData.append("]");
				mData.append(",");
				mData.append("\"Personnel\"");  //添加人员开始标记
				mData.append(":");
				mData.append("[");
				if(mPersonList != null && mPersonList.size() > 0) {
					for(int i = 0,len = mPersonList.size(); i < len; i ++) {
						HashMap<String, String> map = mPersonList.get(i);
						if(map != null) {
							mData.append("{");
							mData.append("\"PersonnelId\"");
							mData.append(":");
							mData.append("\""+map.get("itemsId")+"\"");
							mData.append(",");
							mData.append("\"PersonnelDone\"");
							mData.append(":");
							mData.append("\""+map.get("value")+"\"");
							mData.append("}");
							if(i != len - 1){
								mData.append(",");
							}
						}
					}
				}
				mData.append("]");
				mData.append("}");//结束标记
				HashMap<String, String>  mParameters=new HashMap<String, String>();
				mParameters.put("method", Constants.METHOD_OF_ADDWEEKPLAN);
				mParameters.put("key",Constants.PUBLIC_KEY);
				mParameters.put("userId",mUserId);
				String data = mData.toString();
				data = data.replaceAll(" ", "%20");
				mParameters.put("data", data);
				String mResponseBody="";
				try{				
					mResponseBody = HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, mParameters, mContext));
					if(JsonTools.getCommonResult(mResponseBody)) {
						sendFMessage(Constants.COMMON_MESSAGE_1);
						new DBService(mContext).updateWeekPlanState(mWeekPlanId, "2", mUserId);
					} else {
						sendFMessage(Constants.ERROR);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				}
		}.start();
	}
	
	/** 获取周期数*/
	public void requestContent(){
		
		new Thread(new Runnable(){
			
			@Override
			public void run() {
				mSp=PreferenceManager.getDefaultSharedPreferences(mContext);
				String  userId=mSp.getString(Constants.PREFERENCES_INFO_USERID, "");
				HashMap<String, String> params=new HashMap<String, String>();
				 params.put("method", Constants.METHOD_OF_PROGRESS_PROJECT_UPLOAD_WEEK);
				 params.put("key", Constants.PUBLIC_KEY);
				 params.put("userId", userId);
				 params.put("LotId", mLotId);
				 params.put("isPlan", "true");
				 params.put("fieldList", "NewId,CycleName,StartDate,EndDate,CycleType");
				 String mResponseBody ;
				 try {
					 mResponseBody=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, params, mContext));
					Log.i("test","url="+ HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, params, mContext));
					 if(!"".endsWith(mResponseBody)){
						 mArrayListProgressRequest  =JsonTools.getProgress(mResponseBody);
						 if(mArrayListProgressRequest.size()>0){
							  if(!"".equals(mLotId)){									  
//								  boolean   mIsData=new DBService(mContext).queryTableWeekPlanEmptyData(mLotId, mUserId);
//								 //如果数据库存在数据时候,就不需要再次插入数据到周期计划表中
//								  if(mIsData){
//								   }else{	
//									   //删除没有完成的周计划
									   new DBService(mContext).delTableWeekPlan(mLotId, mUserId,mWeekPlanId);
									   //如果不存在,就插入数据
									   new DBService(mContext).insertTableWeekPlan(mArrayListProgressRequest, mPeriodId, mLotId, mUserId);
//								   }
									   
							  }else{									  
								  //new DBService(mContext).clearTableWeekPlanData();//在多用户下，不可有此操作
							  }
							 //获取周期数据表中的数据
							 ArrayList<HashMap<String, String>> tempArrayList=new DBService(mContext).queryTableWeekPlan(mLotId, mUserId);
							 if(tempArrayList.size()>0){
								  	 for(HashMap<String, String> map:tempArrayList){
//								  		 String   periodId=map.get("periodId");
								  		 String   weekPlanId=map.get("weekPlanId");
								  		 String   startDate=map.get("startDate");
								  		 String   endDate=map.get("endDate");
								  		 //判断周期计划日期表中是否有存在数据,如果不存在就插入数据到表中
								  			ArrayList<HashMap<String, String>> dateList=DateTools.getDateList(startDate, endDate);
								  			if(dateList.size()>0){
								  			boolean  mResult=	new DBService(mContext).queryTableWeekPanDay(mLotId, weekPlanId,mUserId);
								  			if(mResult==false){				
								  				new DBService(mContext).insertTableWeekPlanDay(mLotId, weekPlanId, "机械", "2", mUserId);
								  				new DBService(mContext).insertTableWeekPlanDay(mLotId, weekPlanId, "人员", "3", mUserId);
								  				for(HashMap<String, String> m : dateList){	
								  					//插入周期计划日期表数据
								  					new DBService(mContext).insertTableWeekPlanDay(mLotId, weekPlanId, m.get("date"), "1", mUserId);
								  				}
								  			}
								  			}
								  		}
								  	 }
							 
							 sendFMessage(Constants.SUCCESS);
						 }else{
							 sendFMessage(Constants.DATA_EMPTY);
						 }
					 }else{
						 sendFMessage(Constants.DATA_EMPTY);
					 }
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
				
			}
			
		}).start();
	}

 	protected void refreshProgress(){
 		if(mArrayListProgress!=null){ 			
 			mArrayListProgress.clear();
 			mArrayListProgressRequest= new DBService(mContext).queryTableWeekPlan(mLotId, mUserId);
 			mArrayListProgress.addAll(mArrayListProgressRequest);
 			mListViewAdapter.notifyDataSetChanged();
 		}
 	}

 	@Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		super.onActivityResult(requestCode, resultCode, data);
 		switch(resultCode){
 		  case  Constants.PAGE_BACK_PROGRESSPROJECT_UPLOAD_DATE:
 			    refreshProgress();
 			break;
 		}
 	}
 	
 	@Override
 	protected void handleOtherMessage(int flag) {
 		switch(flag){
 			case  Constants.ERROR:
 				Toast.makeText(mContext, "上传失败", Toast.LENGTH_LONG).show();
 				mDialogProgressBar.cancel();
 				break;
 			case Constants.SUCCESS:
 				mDialogProgressBar.cancel();
//				Toast.makeText(mContext, "上传成功", Toast.LENGTH_LONG).show();
 				refreshProgress();
 				break;
 			case Constants.DATA_EMPTY:
 				mDialogProgressBar.cancel();
 				Toast.makeText(mContext, "无数据显示", Toast.LENGTH_SHORT).show();
 				break;	
 			case  Constants.INFA_EMPTY:
 				Toast.makeText(mContext, "信息未填写完整",Toast.LENGTH_SHORT).show();
 				break;
 			case Constants.COMMON_MESSAGE_1:
 				Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
 				refreshProgress();
 				mDialogProgressBar.cancel();
 				break;
 			case Constants.REFRESH:
 				mDialogProgressBar.cancel();
 				refreshProgress();
 			break;
 		}
 		super.handleOtherMessage(flag);
 	}
}
