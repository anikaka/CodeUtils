package com.tongyan.activity.measure.measure;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.MidMeasureListAdapter;
import com.tongyan.activity.adapter.MidMeasureRecordOptionListViewApdater;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

/**
 *合同中间计量单选项
 *@author ChenLang
 *@date 2014/11/03
 */
public class MidMeasureRecordOptionAct  extends AbstructCommonActivity implements OnClickListener{

	private ListView mMiddleRecordFormListView;
	private Button  btnAllCheck,btnCancleCheck,btnApprove,btnBack;
	private Context mContext=this;
	private  MidMeasureRecordOptionListViewApdater mAdater;
	private String  mRowId;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListData;
	private _User mUser;
	private MyApplication mMyApplication;
	private Dialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_mid_record_option);
		init();
		mMyApplication=(MyApplication)getApplication();
		mMyApplication.addActivity(this);
		mUser=mMyApplication.localUser;
		if(getIntent().getExtras()!=null){
			mRowId=getIntent().getExtras().getString("rowId");
		}
		mAdater=new MidMeasureRecordOptionListViewApdater(mContext, mArrayList,R.layout.measure_mid_record_option_item);
		mMiddleRecordFormListView.setAdapter(mAdater);
		requestData();
		baseShowDialog();
	}
	
	/**设置复选框状态*/
	public void  setCheckBoxState(int flag){
		if(mArrayList!=null){
			if(flag==1 || flag==0){
				for(HashMap<String, String> map :mArrayList){
					map.put("checkBoxState", String.valueOf(flag));
				}
				mAdater.notifyDataSetChanged();
			}
		}
	}
	
	/** 组件初始化*/
	private void init(){
		mMiddleRecordFormListView=(ListView)findViewById(R.id.middleRecordFormListView);
	}
	
	/** 后台数据获取*/
	private  void  requestData(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());
				parameters.put("type", "MeasurementListDetail");
				parameters.put("parms","{rowId:\'"+ mRowId+"\'}");
				String stream=null;
				try{
					stream=WebServiceUtils.requestM(parameters,Constansts.METHOD_OF_RECORDFORM_OPTION, mContext);
					mArrayListData=new Str2Json().getBillData(stream);
					if(mArrayListData!=null){
						sendMessage(Constansts.SUCCESS);
					}else{
						sendMessage(Constansts.ERRER);
					}
				}catch(Exception e){
					e.printStackTrace();
					sendMessage(Constansts.ERRER);
				}
			}
		}).start();
	}
	

 /**  数据更新*/
	private void update(){
		if(mArrayList!=null){
			mArrayList.clear();
		}
		if(mArrayListData!=null){
			mArrayList.addAll(mArrayListData);
		}
		mAdater.notifyDataSetChanged();
		mArrayListData.clear();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
				update();
				baseCloseDialog();
			break;
		case Constansts.ERRER:
				Toast.makeText(mContext,"网络连接失败", Toast.LENGTH_SHORT);
				baseCloseDialog();
			break;
		default:
			break;
		}
		super.handleOtherMessage(flag);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
