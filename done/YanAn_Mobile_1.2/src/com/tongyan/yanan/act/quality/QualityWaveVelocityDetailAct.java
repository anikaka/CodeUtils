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
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: QualityWaveVelocityDetailAct.java 
 * @author Rubert
 * @date 2014-8-22 PM 02:15:56 
 * @version V1.0 
 * @Description: TODO
 */
public class QualityWaveVelocityDetailAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	@ViewInject(id=R.id.quality_project) TextView mQualityProject;//工程
	@ViewInject(id=R.id.quality_term_pact) TextView mQualityTermPact;//合同段
	@ViewInject(id=R.id.quality_tester) TextView mQualityTester;//检测人
	
	@ViewInject(id=R.id.quality_company) TextView mQualityTestCompany; //检测单位：
	@ViewInject(id=R.id.quality_time) TextView mQualityTestTime;//检测时间：
	@ViewInject(id=R.id.quality_detectioner_number) TextView mQualityDetectionerNo;//检测编号
	
	
	@ViewInject(id=R.id.testing_layer_numbers_container, click="onAddLayerNumsListener") RelativeLayout mQualityNumberLayout;//本次检测层数量
	@ViewInject(id=R.id.quality_detection_layer_numbers) TextView mQualityDeteLayerNum;//本次检测层数量
	
	@ViewInject(id=R.id.quality_experiment_content) TextView mQualityExpContent;//实验内容
	@ViewInject(id=R.id.quality_experiment_result) TextView mQualityExpResult;//实验结论
	
	private Context mContext = this;
	private SharedPreferences mPreferences;
	private HashMap<String, String> mPactMap = null;
	private HashMap<String, String> mItemMap = null;
	
	private String mQualityId = null;
	private String mIntentType = null;//波速测试、贯入测试
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_wavevelocity_details);
		mTitleContent.setText("添增压实度");
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("PactMap");
			mItemMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			mIntentType = getIntent().getExtras().getString("IntentType");
			if(mPactMap != null) {
				mQualityProject.setText("工程：" + mPactMap.get("periodName"));
				mQualityTermPact.setText("合同段：" + mPactMap.get("LotName"));
			}
			mQualityTester.setText("检测人：" + mPreferences.getString(Constants.PREFERENCES_INFO_USERNAME, ""));
			if("1".equals(mIntentType)) {
				mTitleContent.setText("波速测试详细");
			} else {
				mTitleContent.setText("标准贯入测试详细");
			}
				 if(mItemMap != null) {
					 	mQualityId = mItemMap.get("ID");
					    mQualityTestCompany.setText(mItemMap.get("DetectUnitName"));
			    		mQualityDetectionerNo.setText(mItemMap.get("DetectNumber"));//
			    		mQualityTestTime.setText(mItemMap.get("DetectDate"));//
			    		mQualityExpContent.setText(mItemMap.get("TestContent"));
			    		mQualityExpResult.setText(mItemMap.get("TestConClusion"));
			    		mQualityDeteLayerNum.setText("本次检测点数量：" + new DBService(mContext).getWaveInjectPointsNum(mQualityId));
			    	}
		}
	}
	
	
	/**
	 * 
	 * @param v
	 */
	public void onAddLayerNumsListener(View v) {
		if(mQualityId != null && !"-1".equals(mQualityId)) {
			Intent intent = new Intent(mContext, QualityWaveVelocityLayerDetailsAct.class);
			intent.putExtra("QualityWaveVelocityId", mQualityId);
			startActivityForResult(intent, 1234);
		} else {
			Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(1234 == requestCode) {
			mQualityDeteLayerNum.setText("本次检测点数量：" + new DBService(mContext).getWaveInjectPointsNum(mQualityId));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
