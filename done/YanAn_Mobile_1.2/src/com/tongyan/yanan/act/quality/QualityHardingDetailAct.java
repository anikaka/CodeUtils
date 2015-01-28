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
 * 
 * @Title: QualityHardingDetailAct.java 
 * @author Rubert
 * @date 2014-8-19 PM 02:41:42 
 * @version V1.0 
 * @Description: TODO
 */
public class QualityHardingDetailAct extends FinalActivity {
@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	@ViewInject(id=R.id.quality_project) TextView mQualityProject;//工程
	@ViewInject(id=R.id.quality_term_pact) TextView mQualityTermPact;//合同段
	@ViewInject(id=R.id.quality_area_time) TextView mQualityAreaTime;//区域时间
	@ViewInject(id=R.id.quality_test_area) TextView mQualityTestArea;//检测区域
	@ViewInject(id=R.id.quality_tester) TextView mQualityTester;//检测人
	
	@ViewInject(id=R.id.quality_company) TextView mQualityTestCompany; //检测单位：
	@ViewInject(id=R.id.quality_time) TextView mQualityTestTime;//检测时间：
	@ViewInject(id=R.id.quality_acreage) TextView mQualityAcreage;//面积
	@ViewInject(id=R.id.quality_machine) TextView mQualityMachine;//辗压机械
	@ViewInject(id=R.id.quality_variate) TextView mQualityVariate;//冲击变数
	@ViewInject(id=R.id.quality_thickness) TextView mQualityThickness;//虚铺厚度
	@ViewInject(id=R.id.quality_press_thickness) TextView mQualityPressThickness;//压实厚度
	
	
	@ViewInject(id=R.id.testing_layer_numbers_container, click="onAddLayerNumsListener") RelativeLayout mQualityNumberLayout;//本次检测层数量
	@ViewInject(id=R.id.quality_detection_layer_numbers) TextView mQualityDeteLayerNum;//本次检测层数量
	
	@ViewInject(id=R.id.quality_experiment_content) TextView mQualityExpContent;//实验内容
	@ViewInject(id=R.id.quality_experiment_result) TextView mQualityExpResult;//实验结论
	
	
	private Context mContext = this;
	
	private SharedPreferences mPreferences;
	
	private HashMap<String, String> mPactMap = null;
	private HashMap<String, String> mQualityMap = null;
	private HashMap<String, String> mItemMap = null;
	
	private String mQualityHardingId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_harding_detail);
		mTitleContent.setText("压实度详情");
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("PactMap");
			mQualityMap = (HashMap<String, String>)getIntent().getExtras().get("QualityMap");
			mItemMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			
			mQualityProject.setText("工程        ：" + mPactMap.get("periodName"));
			mQualityTermPact.setText("合同段    ：" + mPactMap.get("LotName"));
			mQualityAreaTime.setText("区域时间：" + mQualityMap.get("ChangeName"));
			mQualityTestArea.setText("区域区域：" + mQualityMap.get("AreaName"));
		    mQualityTester.setText("检测人    ：" + mPreferences.getString(Constants.PREFERENCES_INFO_USERNAME, ""));
			
		    	if(mItemMap != null) {
		    		mQualityHardingId = mItemMap.get("ID");
		    		mQualityTestCompany.setText(mItemMap.get("DetectionCompany"));
		    		mQualityTestTime.setText(mItemMap.get("DetectionTime"));//
		    		mQualityAcreage.setText(mItemMap.get("Area"));
		    		mQualityMachine.setText(mItemMap.get("Machine"));
		    		mQualityVariate.setText(mItemMap.get("Variate"));
		    		mQualityThickness.setText(mItemMap.get("Thickness"));
		    		mQualityPressThickness.setText(mItemMap.get("PressThickness"));
		    		mQualityExpContent.setText(mItemMap.get("ExpContent"));
		    		mQualityExpResult.setText(mItemMap.get("ExpResult"));
		    		mQualityDeteLayerNum.setText("本次检测层数量：" + new DBService(mContext).getHardingPointsNum(mQualityHardingId.toString()));
		    	}
		
		}	
	}
	
	
	public void onAddLayerNumsListener(View v) {
		Intent intent = new Intent(mContext, QualityHardingLayerDetailAct.class);
		intent.putExtra("QualityHardingId", mQualityHardingId.toString());
		startActivityForResult(intent, 1234);
	}
	
	
	
}
