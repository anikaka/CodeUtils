package com.tongyan.yanan.act.quality;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * 平板静载荷试验详情
 * @author Rubert
 *
 */
public class QualityStaticLoadDetailsAct extends FinalActivity{
	// 工程
	@ViewInject(id = R.id.txtObject_staticload) TextView mTxtobject;
	// 合同段
	@ViewInject(id = R.id.txtPact_staticload) TextView mTxtPact;
	// 检测人
	@ViewInject(id = R.id.txtExaminePerson_staticload) TextView mTxtExaminePerson;
	// 检测编号
	@ViewInject(id = R.id.txtExamineNo_staticload) TextView mEditExamineNo;
	// 检测单位
	@ViewInject(id = R.id.spinnerExamineUnit_staticload) TextView mExamineUnit;
	// 检测时间
	@ViewInject(id = R.id.editExamineDate_staticload) TextView mEditExamineDate;
	// 监测数量
	@ViewInject(id = R.id.testing_layer_numbers_container,click="inputNumber") RelativeLayout mEditPointNumberContainer;
	@ViewInject(id = R.id.experimentPoint) TextView mEditPointNumber;
	// 试验内容
	@ViewInject(id = R.id.editExperimentContent_staticload) TextView mEditContent;
	// 试验结论
	@ViewInject(id = R.id.editExperimentVerdict_staticload) TextView mEditVerdict;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private Bundle mBundle;
	private String mNoId;//平板静电荷试验编号Id
	private String mProjectName;//期段名称
	private String mLotName;//合同段名称  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_staticload_details);
		((TextView)findViewById(R.id.title_common_content)).setText(getResources().getString(R.string.addStaticUploadInfo));
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			if(getIntent().getExtras()!=null){
				mBundle=getIntent().getExtras();
				mNoId = mBundle.getString("noId");
				mProjectName=mBundle.getString("projectName");
				mLotName=mBundle.getString("lotName");
				
				mTxtobject.setText(getResources().getString(R.string.project) +  mProjectName);//项目
				mTxtPact.setText(getResources().getString(R.string.pact) +  mLotName);//合同段
				mTxtExaminePerson.setText(getResources().getString(R.string.examinePerson) +  mSP.getString(Constants.PREFERENCES_INFO_USERNAME, ""));//检测人
				HashMap<String,String> mCacheMap = (HashMap<String,String>)mBundle.get("CacheMap");
				if(mCacheMap != null) {
					mEditExamineNo.setText(mCacheMap.get("no"));
					mExamineUnit.setText(new DBService(mContext).queryContactName(mCacheMap.get("unit")));
					mEditExamineDate.setText(mCacheMap.get("date"));
					mEditContent.setText(mCacheMap.get("content"));//试验内容
					mEditVerdict.setText(mCacheMap.get("conclusion"));//试验结论
					mEditPointNumber.setText(getResources().getString(R.string.examinePoint) + new DBService(mContext).queryTableExaminePointNumber(mNoId));
				}
			}
		}
	}
	
	public void inputNumber(View v) {
		Intent intent = new Intent(mContext, QualityStaticLoadAddPoint.class);
		intent.putExtras(mBundle);
		intent.putExtra("IntentType", "details");
		startActivity(intent);
	}
	

}
