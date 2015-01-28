package com.tongyan.yanan.act.subside;


import java.util.ArrayList;
import java.util.HashMap;


import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.SubsidePactSelectPointListDataListView;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @category监测点的数据列表
 * @author ChenLang
 * @version YanAn 1.0
 *layout_subside_pactselect_point_dataList.xml
 */
public class SubsidePactSelecPointtDataList  extends FinalActivity  {
 

	@ViewInject(id=R.id.common_button_container, click="addsubisidePactSelectPoint")  RelativeLayout mRlimgAdd_subsideselect_point; //图片添加点
	@ViewInject(id=R.id.title_common_content) TextView  mTxtTitle_subsideselect_point_dataList;
	@ViewInject(id=R.id.title_common_add_view) ImageView  mCommonAddView;
	@ViewInject (id=R.id.txtSupervise_layout_subside_pactselect_data_modifys_type_title_content) TextView  
	mtxtSupervise_layout_subside_pactselect_data_modifys_type_title_content; //检测类型
	@ViewInject (id=R.id.txtSupervise_content)  TextView  mTxtSuperviseContent; //检测内容
	@ViewInject (id=R.id.listView_data)		ListView mListViewDataList;
	
    private String  mMonitorPointId ;//监测点Id
	private String  mMonitorProjectTypeId;	
	private String  mMonitorProjectTypeName;
	private String  mMonitorTypeId;
	private String  mMonitorTypeName;
	private String  mPactId;
	private String mPactName;
	private String mMonitorName;
	private String mMonitorValue;
	private String mMonitorDate;
	private String mMonitorDeep;//埋深
	private SharedPreferences mPreferences;
	private String mUploadMark;
	private Context mContext=this;
	public Dialog mCrcleDialog;
	ArrayList<HashMap<String, String>>  mListSubsidePactSelecPointtDataList=new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>>  mListSubsidePactSelecPointtDataListSQL=new ArrayList<HashMap<String, String>>();
	SubsidePactSelectPointListDataListView   mSubsidePactSelectPointListDataListViewAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mRlimgAdd_subsideselect_point.setVisibility(View.VISIBLE);
		mCommonAddView.setVisibility(View.VISIBLE);
		
		/*
		 * data.putString("monitorProjectTypeId", mTypeId); //检测类型Id就等于父类检测类型Id
		 * data.putString("monitorProjectTypeName", mMonitorProjectTypeName);
		 * data.putString("monitorTypeId", mTypeId); //监测类型ID
		 * data.putString("monitorTypeName", mMonitorTypeName); //监测类型名称
		 * data.putString("pactId", "mPactId"); //合同段ID
		 * data.putString("pactName", mPactName);//合同段名称
		 */
		mPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
		Bundle bundle = getIntent().getExtras();
		mMonitorProjectTypeId = bundle.getString("monitorProjectTypeId");
		mMonitorProjectTypeName = bundle.getString("monitorProjectTypeName");
		mMonitorTypeId = bundle.getString("monitorTypeId");
		mMonitorTypeName = bundle.getString("monitorTypeName");
		mPactId = bundle.getString("pactId");
		mPactName = bundle.getString("pactName");
		mMonitorName = bundle.getString("monitorName");
		mMonitorPointId=bundle.getString("monitorPointId");
		mListSubsidePactSelecPointtDataList = new DBService(mContext).queryTableMonitorPointUpload(mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""),mMonitorPointId);
		if(!"".equals(mMonitorName)){
			mTxtTitle_subsideselect_point_dataList.setText(mMonitorName+"数据列表");
		}
		mSubsidePactSelectPointListDataListViewAdapter = new SubsidePactSelectPointListDataListView(
				this, mListSubsidePactSelecPointtDataList,R.layout.main_subside_pactselect_point_datalist_listview_item);
		mListViewDataList.setAdapter(mSubsidePactSelectPointListDataListViewAdapter);
		mListViewDataList.setOnItemClickListener(listener);
	}

	OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//得到每次点击Item的map
			SubsidePactSelectPointListDataListView.HolderViewSubsidePactSelectPointListDataListView	  mHolderView=(SubsidePactSelectPointListDataListView.HolderViewSubsidePactSelectPointListDataListView)view.getTag();
			 HashMap<String, String> map=mHolderView.mapHolderViewSubsidePactSelectPointListDataListView;
			  mUploadMark=map.get("uploadMark");
			  mMonitorDeep=map.get("monitorDeep");
			 //新建对话框
			final Dialog mDialog=new Dialog(mContext);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.layout_subside_pactselect_datalist_dialog);
			mDialog.show();
			//查找对话框组件;
			Button  mButLook=(Button)mDialog.findViewById(R.id.butLook_layout_subside_pactselect_point_datalist);
			Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_layout_subside_pactselect_point_datalist);
			Button mButDelete=(Button)mDialog.findViewById(R.id.butDelete_layout_subside_pactselect_point_datalist);
			String  mUploadState=new DBService(mContext).queryTableMonitorPointUploadState(mMonitorTypeId, mUploadMark);
			if("1".equals(mUploadState)){
				mButLook.setText("查看");
			}else{
				mButLook.setText("修改");
			}
			//查看/修改
			mButLook.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					String  mUploadState=new DBService(mContext).queryTableMonitorPointUploadState(mMonitorTypeId, mUploadMark);
					String mMonitorValue=	new DBService(mContext).queryTableMonitorPointUploadMonitorValue(mMonitorTypeId, mUploadMark);
					if("1".equals(mUploadState)){
						//跳转到查看界面
						Intent intent =new Intent(SubsidePactSelecPointtDataList.this,SubsidepactselectDataLook.class);
						intent.putExtra("monitorValue", mMonitorValue);
						intent.putExtra("monitorPointId", mMonitorPointId);//监测点Id
						intent.putExtra("monitorTypeId", mMonitorTypeId);
						intent.putExtra("uploadMark", mUploadMark);
						intent.putExtra("monitorDeep", mMonitorDeep);
					    startActivityForResult(intent, Constants.PAGE_BACK_SUBSIDEPACTSELECTPOINTDATALIST_LOOK);
						mDialog.dismiss();
					}else{
						
						Intent intent =new Intent(SubsidePactSelecPointtDataList.this,SubsidePactSelectDataAddOrModify.class);
						intent.putExtra("modify", "1");
						intent.putExtra("monitorValue", mMonitorValue);
						intent.putExtra("monitorPointId", mMonitorPointId);//监测点Id
						intent.putExtra("monitorTypeId", mMonitorTypeId);
						intent.putExtra("uploadMark", mUploadMark);
						startActivityForResult(intent, Constants.PAGE_BACK_SUBSIDEPACTSELECTPOINTDATALIST_UPLOAD);
						mDialog.dismiss();
					}
				
				}
			});
			
			//数据上传
			mButUpload.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					mCrcleDialog=new Dialog(mContext, R.style.dialog);
					mCrcleDialog.setContentView(R.layout.common_normal_progressbar);
					setProgressBarIndeterminateVisibility(true);
					mCrcleDialog.show();
					uploadData();
					mDialog.dismiss();
				}
			});
			
			//删除
			mButDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				
					AlertDialog.Builder  mDia=new AlertDialog.Builder(mContext);
					mDia.setMessage("确定要删除吗？");
					mDia.setPositiveButton("是",new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 boolean deleteResult	=new DBService(mContext).delTableMonitorPointUploadData(mMonitorTypeId, mUploadMark);
							 if(deleteResult){
								 updateListData(); //更新数据
								 Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
								 mDialog.dismiss();
							 }
						}
					});
					mDia.setNegativeButton("否", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						
							mDialog.dismiss();
						}
					});

					mDia.show();

				}
			});
		}
};

	public void  addsubisidePactSelectPoint(View view){
		sendFMessage(Constants.PAGE_GO);

	}
	
	/** 数据上传 */
	public void uploadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String userId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
				ArrayList<HashMap<String, String>> mArrayList = new DBService(
						mContext).queryTableMonitorPointUpload(mMonitorPointId,mMonitorTypeId,
						mUploadMark);
				/*
				 * MonitorPointId=? ThisVariation=？ 测值 & MonitorDate 监测时间
				 * {"MonitorPointId":"", "ThisVariation":"","MonitorDate":"'}
				 */
				if (mArrayList.size() > 0) {
//					mMonitorValue =null;
//					mMonitorDate = null;
//					mMonitorDeep=null;
					for (HashMap<String, String> map : mArrayList) {
						mMonitorValue = map.get("monitorValue");
						mMonitorDate = map.get("superviseDate");
						mMonitorDeep=map.get("monitorDeep");
					}
				}
				String data = "{" + "\"" + "MonitorPointId" + "\"" + ":" + "\""
						+ mMonitorPointId + "\"" + "," + "\"" + "ThisVariation"
						+ "\"" + ":" + "\"" + mMonitorValue + "\"" + "," + "\""
						+ "MonitorDate" + "\"" + ":" + "\"" + mMonitorDate
						+ "\"" +","
						+"\"ThisMonitorDeep\""+":"+"\""+mMonitorDeep+"\""
						+ "}";
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_MONITOR_VALUES_UPLOAD);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", userId);
				data = data.replaceAll(" ", "%20");
				params.put("data", data);
				String mResponseBody="";
				try {
					mResponseBody = HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_SUBSIDE_POINT,params, mContext));
				 boolean  mResult=	JsonTools.getCommonResult(mResponseBody);
				 if(mResult){
					 sendFMessage(Constants.SUCCESS);
				 }else{
					 sendFMessage(Constants.ERROR);
				 }
					//Log.i("test", mResponseBody);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		 switch (flag) {
		case Constants.PAGE_GO:
			Intent intent=new Intent(SubsidePactSelecPointtDataList.this,SubsidePactSelectDataAddOrModify.class);
			Bundle data = new Bundle();
			data.putString("monitorPointId", mMonitorPointId);
			data.putString("monitorProjectTypeId", mMonitorProjectTypeId); // 检测类型Id就等于父类检测类型Id
			data.putString("monitorProjectTypeName", mMonitorProjectTypeName);
			data.putString("monitorTypeId", mMonitorTypeId); // 监测类型ID
			data.putString("monitorTypeName", mMonitorTypeName); // 监测类型名称
			data.putString("pactId", mPactId); // 合同段ID
			data.putString("pactName", mPactName);// 合同段名称
			data.putString("monitorName", mMonitorName);
			intent.putExtras(data);
//			startActivity(intent);
			startActivityForResult(intent, Constants.PAGE_BACK_SUbSIDEPACTSELECPOINTDATALIST);
			break;
		case Constants.SUCCESS:
			updateUploadState();
			mCrcleDialog.cancel();
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			break;
		case Constants.ERROR:
			mCrcleDialog.cancel();
			Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		super.handleOtherMessage(flag);
	}
	
	public void updateUploadState(){
		new DBService(mContext).updateUploadState(mMonitorTypeId, mUploadMark,DateTools.getDate());
		updateListData();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		switch(resultCode){
				case Constants.PAGE_BACK_SUbSIDEPACTSELECPOINTDATALIST:
					updateListData();
					break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	 public void updateListData(){
		if(mListSubsidePactSelecPointtDataListSQL!=null){
			mListSubsidePactSelecPointtDataListSQL=new DBService(mContext).queryTableMonitorPointUpload(mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""),mMonitorPointId);
			mListSubsidePactSelecPointtDataList.clear();
			mListSubsidePactSelecPointtDataList.addAll(mListSubsidePactSelecPointtDataListSQL);
		}
		mSubsidePactSelectPointListDataListViewAdapter.notifyDataSetChanged();
	}
}
