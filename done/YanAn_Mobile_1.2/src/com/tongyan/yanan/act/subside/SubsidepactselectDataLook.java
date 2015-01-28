package com.tongyan.yanan.act.subside;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.R.layout;
import com.tongyan.yanan.common.db.DBHelper;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * @category 数据查看界面
 * @author Administrator
 * @version YanAn 1.0
 */

public class SubsidepactselectDataLook  extends FinalActivity{
	
	@ViewInject (id=R.id.txtTile_data_look) 
	TextView mTxtTitle_SubsidepactselectDataLook;
	@ViewInject(id = R.id.txtSupervise_type_title_content)
	TextView mTxtSupervise_type_title_content; // 监测类型

	@ViewInject(id = R.id.txtSupervise_content)
	TextView mTxtSuperviseProject_content; // 监测项目

	@ViewInject(id = R.id.txtMonitorValuer_content)
	TextView  mTxtMonitorValue_content; // 本次测值


	@ViewInject(id = R.id.txtSuperviseDate_content)
	TextView mTxtSuperviseDate_content; // 监测时间

	@ViewInject(id = R.id.txtUploadingDate_content)
	TextView mTxtUploadingDate_content; // 上传时间

	@ViewInject(id = R.id.txtUploadingUser_content)
	TextView mTTxtUploadingUser_content; // 上传人
	//埋深
	@ViewInject(id=R.id.txtMonitorDeep_content)
	TextView mTxtMonitorDeep;
	private String mMonitorPointId;//测点Id
	private String mMonitorTypeId;
	private String mUploadMark;
	private String mMonitorValue; //监测值
	private String mMonitorName;
	private String mMonitorDeep;//埋深
	Context mcontext=this;
	ArrayList<HashMap<String, String >> mListSubsidepactselectDataLook=new ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_subside_pactselect_data_look);
	  if(getIntent().getExtras()!=null){
		  mMonitorTypeId= getIntent().getExtras().getString("monitorTypeId");
		  mUploadMark=getIntent().getExtras().getString("uploadMark");
		  mMonitorPointId=getIntent().getExtras().getString("monitorPointId");
		  mMonitorValue=getIntent().getExtras().getString("mMonitorValue");
		  mMonitorName=getIntent().getExtras().getString("monitorName");
		  mMonitorDeep=getIntent().getExtras().getString("monitorDeep");
	  }

	  //设置本次测值的初始值
	  if("".equals(mMonitorValue) || mMonitorValue==null){
		  mTxtMonitorValue_content.setText("");
	  }else{
		  mTxtMonitorValue_content.setText(mMonitorValue);	  
	  }
	  
	  //查询数据表,初始化数据
	  mListSubsidepactselectDataLook=new DBService(mcontext).queryTableMonitorPointUpload(mMonitorPointId,mMonitorTypeId, mUploadMark);
	  if(mListSubsidepactselectDataLook.size()>0){
		   for(HashMap<String, String> map:mListSubsidepactselectDataLook){
			   mTxtSupervise_type_title_content.setText(map.get("monitorProjectTypeName"));
			   mTxtSuperviseProject_content.setText( map.get("monitorTypeName"));
			   mTxtMonitorValue_content.setText(map.get("monitorValue"));
			   mTxtSuperviseDate_content.setText(map.get("superviseDate")); 
			   mTxtUploadingDate_content.setText(map.get("uploadDate")); //上传时间
			   mTTxtUploadingUser_content.setText(map.get("uploadUser"));
			   mTxtSuperviseDate_content.setText(map.get("superviseDate"));
			   mTxtTitle_SubsidepactselectDataLook.setText(map.get("monitorPointName")+"数据查看");
			   mTxtMonitorDeep.setText(mMonitorDeep);
		   }
	  }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}	
}
