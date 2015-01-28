package com.tongyan.yanan.act.subside;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.SubSidePointAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category地表沉降监测点界面
 * @author Administrator
 * @date 2014/06/20
 * @version YanAn 1.0
 */

public class SubsidePointAct extends FinalActivity{

	@ViewInject(id = R.id.listView_layout_subside_point)
	ListView mListViewSubsideMonitorPoint;
	// 标题名称
	@ViewInject(id = R.id.txtTitle_Layout_subside_point)
	TextView mTxtMonitorTypeName; 
	// 合同段名称
	@ViewInject(id = R.id.txtPactName)
	TextView mTxtPactName; 
	// 搜索编辑框
	@ViewInject(id = R.id.editSearchContentS)
	EditText mEditSearch;
	//搜索按钮
	@ViewInject(id = R.id.butSearchS,click="butSearch")
	Button mButSearch;
	private SharedPreferences mPreferences;
	Context mContext=this;
	ArrayList<HashMap<String, String>> mListMonitorPoint=new ArrayList<HashMap<String,String>>();//检测点集合
	ArrayList<HashMap<String, String>> mListMonitortUpload=null;//检测点下载集合
	SubSidePointAdapter  mSubsidePonintAdapter;
	
	private String mTypeId;
	private String mPactId;
	private String  mPactName; //合同段名称
	private String mMonitorTypeName;
	private String mMonitorProjectTypeName;
	private Dialog mDialog;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_subside_point);
		mDialog=new  Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		mDialog.show();
		setProgressBarIndeterminateVisibility(true);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		//设置适配器
		if(getIntent() != null && getIntent().getExtras() != null) {
			HashMap<String, String> mPactMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			if(mPactMap != null) {
				mPactId = mPactMap.get("NewId"); 
				mPactName=mPactMap.get("LotName");
				mTxtPactName.setText(mPactName);
			}
			mMonitorProjectTypeName=getIntent().getExtras().getString("monitorProjectTypeName");
			HashMap<String, String> mTypetMap = (HashMap<String, String>)getIntent().getExtras().get("TypeMap");
			if(mTypetMap != null) {
				mTypeId = mTypetMap.get("NewId"); // 类型Id
			}
			//获取监测类型名字
			mMonitorTypeName =getIntent().getExtras().getString("monitorTypeName"); 
			if(mMonitorTypeName != null) {
				if(!"".equals(mMonitorTypeName)){
					mTxtMonitorTypeName.setText(mMonitorTypeName);
				}
			}
		}
		mSubsidePonintAdapter = new SubSidePointAdapter(mContext, mListMonitorPoint, R.layout.main_subside_point_listview_item);
		mListViewSubsideMonitorPoint.setAdapter(mSubsidePonintAdapter);
		//设置ListView监听事件
		mListViewSubsideMonitorPoint.setOnItemClickListener(listener);
		//第一次请求搜索内容为空
		getPointinfo("");
 	
 	}

 	public void butSearch(View v){
 		if("".equals(mEditSearch.getText().toString())){
 			mDialog.show();
 			getPointinfo("");
 		}else{
 			mDialog.show();
 			getPointinfo(mEditSearch.getText().toString());
 		}
 	}
 	
 	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			SubSidePointAdapter.HolderViewSubSidePoint mholderViewSubSidePoint=(SubSidePointAdapter.HolderViewSubSidePoint)	view.getTag();
			Intent intent=new Intent(SubsidePointAct.this,SubsidePactSelecPointtDataList.class);
			Bundle  data=new Bundle();
			data.putString("monitorPointId",mholderViewSubSidePoint.mapHolderViewSubSidePoint.get("newId"));
			data.putString("monitorProjectTypeId", mTypeId); //检测类型Id就等于父类检测类型Id
			data.putString("monitorProjectTypeName", mMonitorProjectTypeName);
			data.putString("monitorTypeId", mTypeId); //监测类型ID
			data.putString("monitorTypeName", mMonitorTypeName); //监测类型名称
			data.putString("pactId", mPactId); //合同段ID
			data.putString("pactName", mPactName);//合同段名称
			data.putString("monitorName", mholderViewSubSidePoint.mapHolderViewSubSidePoint.get("monitorName"));
			 intent.putExtras(data);
			startActivity(intent);
		}
	};
	
	public void  getPointinfo(final String searchContent){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String userId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
				HashMap<String, String>  params=new HashMap<String, String>(); 
				params.put("method", Constants.METHOD_OF_SUBSIDE_POINT);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", userId);
				params.put("monitorTypeId", mTypeId);
				params.put("lotId", mPactId); //合同段Id
				params.put("search", searchContent);
				params.put("fieldList", "");
				String mResponseBody;
				try {
					mResponseBody = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_SUBSIDE_POINT, params, mContext));
					mListMonitortUpload=JsonTools.getMonitorPointList(mResponseBody);
					if(mListMonitortUpload != null && mListMonitortUpload.size()>0){						
						sendFMessage(Constants.SUCCESS);
					} else {
						sendFMessage(Constants.ERROR);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendFMessage(Constants.CONNECTION_TIMEOUT);
				}
			}
			
		}).start();
	}
	
	
	public void  refreshListView(){
		if(mListMonitortUpload!=null){
			if(mListMonitorPoint!=null ){
				mListMonitorPoint.clear();
				mListMonitorPoint.addAll(mListMonitortUpload);
			}
			mSubsidePonintAdapter.notifyDataSetChanged();
			mListMonitortUpload.clear();
		}
	
	}
	
	
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constants.SUCCESS:
			refreshListView();
			mDialog.cancel();
			break;
		case Constants.ERROR:
	 		Toast.makeText(mContext, "无测点数据", Toast.LENGTH_SHORT).show();
	 		mDialog.cancel();
	 		break;
		case Constants.CONNECTION_TIMEOUT:
	 		Toast.makeText(mContext, "访问超时,请检查网络...", Toast.LENGTH_SHORT).show();
	 		break;
		default:
			break;
		}
		
	};
	
}
