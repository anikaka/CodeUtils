package com.tongyan.yanan.act.progress.plan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.progress.completion.DayCompletionListAct;
import com.tongyan.yanan.act.progress.completion.MonthCompletionListAct;
import com.tongyan.yanan.act.progress.completion.WeekCompletionListAct;
import com.tongyan.yanan.common.adapter.ProgressProjectListVeiwAdpater;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

public class ProgressProjectAct  extends FinalActivity{
	/**
	 * @category计划界面
	 * @category ChenLang
	 * @date 2014/07/04
	 * @version YanAn 1.0
	 */
	@ViewInject (id=R.id.txtTitle_progress_project)  TextView mTxtTitle_progress_project;
	@ViewInject(id=R.id.listView_progress_project)  ListView  mListView_progress_project;
	
	private ProgressProjectListVeiwAdpater  mProgressProjectListViewAdapter; //适配器
	private Context mContext=this;
	private SharedPreferences mSP;
	private ArrayList<HashMap<String, String>> mArrayList;
	private String mProjectInfo;
    private ArrayList<HashMap<String, String>> mArrayListLotNumber=new ArrayList<HashMap<String,String>>();
    private ArrayList<HashMap<String, String>> mArrayListlotNumberJson=new ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_progress_project);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
	   //得到选择计划的类型
		if(getIntent().getExtras()!=null){			
			mProjectInfo=getIntent().getExtras().getString("projectInfo"); //projectInfo

  		if(!"".equals(mProjectInfo)){
			mTxtTitle_progress_project.setText(mProjectInfo);
		}
  		 if("周计划".equals(mProjectInfo)){					 
  			getLotNumber("zjh");
		 }else if("月计划".equals(mProjectInfo)){
			 mArrayListLotNumber.clear();
			 mArrayListLotNumber=new DBService(mContext).getTableMonthPlanNumber(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
		 } else if("日完成量".equals(mProjectInfo)) {
			 mArrayListLotNumber.clear();
			 mArrayListLotNumber=new DBService(mContext).getTableCompletion(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), "1");
		 }  else if("周完成量".equals(mProjectInfo)) {
			 getLotNumber("zwcl");
		 }  else if("月完成量".equals(mProjectInfo)) {
			 mArrayListLotNumber.clear();
			 mArrayListLotNumber=new DBService(mContext).getTableCompletion(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), "3");
		 }
		}	
		if(mArrayList==null ){			
			mArrayList=new DBService(mContext).getTermPartPact();
		}else{
			mArrayList.clear();
			mArrayList=new DBService(mContext).getTermPartPact();
		}

		//设置适配器
		mProgressProjectListViewAdapter=new ProgressProjectListVeiwAdpater(mContext, mArrayList, R.layout.mian_subside_progress_project_listview_item,mArrayListLotNumber);
		mListView_progress_project.setAdapter(mProgressProjectListViewAdapter);
		//设置ListView监听
		mListView_progress_project.setOnItemClickListener(listener);
		
	}
	/** 获取每个合同段信息的数目*/
	public void getLotNumber(final String typeName){

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_LOTNUMBER);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("typeName", typeName);
				params.put("typeId", "");
				String mStream="";
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PACTSELECT, params, mContext));
					if(!"".equals(mStream)){
						mArrayListlotNumberJson=JsonTools.getLotNumber(mStream);
						sendFMessage(Constants.SUCCESS);
					}else{

					}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void refresh(){
		if(mArrayListlotNumberJson.size()>0){
			mArrayListLotNumber.clear();
			mArrayListLotNumber.addAll(mArrayListlotNumberJson);
			mArrayListlotNumberJson.clear();
		}
		mProgressProjectListViewAdapter.notifyDataSetChanged();
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.ERROR:
			Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.SUCCESS:
				refresh();
			break;
		default:
			break;
		}
	};
	//  
	OnItemClickListener listener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//获取Item的值
			ProgressProjectListVeiwAdpater.ViewHolderProgressProject viewHolderProgressProject = 
					(ProgressProjectListVeiwAdpater.ViewHolderProgressProject) view
					.getTag();
			HashMap<String, String> map= viewHolderProgressProject.mMapProgressProject;
			if(!"".equals(mProjectInfo)){
				 if("周计划".equals(mProjectInfo)){					 
					 goIntent(map, ProgressProjectUploadAct.class,"0");
				 }else if("月计划".equals(mProjectInfo)){
					 goIntent(map, ProgressMonthProjectAct.class,"0");
				 } else if("日完成量".equals(mProjectInfo)) {
					 goIntent(map, DayCompletionListAct.class,"1");
				 }  else if("周完成量".equals(mProjectInfo)) {
					 goIntent(map, WeekCompletionListAct.class,"2");
				 }  else if("月完成量".equals(mProjectInfo)) {
					 goIntent(map, MonthCompletionListAct.class,"3");
				 }
			}else{
				Toast.makeText(mContext, "获取信息失败,请重新登录",Toast.LENGTH_SHORT).show();
			}
		}		
	};
	
	/**
	 * 
	 * @param map
	 * @param clazz
	 */
	public void goIntent(HashMap<String, String> map, Class<?> clazz, String mDataType) {
		 Intent  intent = new Intent(mContext, clazz);
		 Bundle  bundle = new Bundle();
		 bundle.putString("periodId", map.get("periodId"));
		 bundle.putString("periodName", map.get("periodName"));
		 bundle.putString("projectInfo", mProjectInfo);
		 bundle.putString("lotId", map.get("NewId"));
		 bundle.putString("lotName", map.get("LotName"));
		 bundle.putString("DataType", mDataType);
		 intent.putExtras(bundle);
		 startActivity(intent);
	}
}
