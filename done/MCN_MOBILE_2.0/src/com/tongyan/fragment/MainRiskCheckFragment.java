package com.tongyan.fragment;

import com.tongyan.activity.R;
import com.tongyan.activity.risk.P35_RiskTaskListAct;
import com.tongyan.activity.risk.P38_RiskShowingAct;
import com.tongyan.activity.risk.P42_RiskShowSetAct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @className MainRiskCheckFragment
 * @author wanghb
 * @date 2014-6-11 PM 03:18:57
 * @Desc 主页面-风险检查
 */
public class MainRiskCheckFragment extends Fragment implements OnClickListener{
	
	
	private Context  mMContext;
	private Button mMainRiskEvaluateAddBtn,mMainRiskNoticeBtn,mMainRiskShowingBtn;
	private SharedPreferences mPreferences = null;
	
	public static MainRiskCheckFragment newInstance(Context mContext) {
		MainRiskCheckFragment mFragment = new MainRiskCheckFragment();
		mFragment.mMContext = mContext;
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_mobile_riskcheck_fragment, container, false);
		mMainRiskEvaluateAddBtn = (Button) view.findViewById(R.id.main_risk_evaluate_add_btn);
		mMainRiskNoticeBtn = (Button) view.findViewById(R.id.main_risk_notice_btn);
		mMainRiskShowingBtn = (Button) view.findViewById(R.id.main_risk_showing_btn);
		mMainRiskEvaluateAddBtn.setOnClickListener(this);
		mMainRiskNoticeBtn.setOnClickListener(this);
		mMainRiskShowingBtn.setOnClickListener(this);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mMContext);
		return view;
	}

	@Override
	public void onClick(View v) {
		/**
		 * 风险检查-风险评估录入
		 */
		if(v.equals(mMainRiskEvaluateAddBtn)) {
			String mLinkRoute = mPreferences.getString("v_RiskAdd", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, P35_RiskTaskListAct.class);
				startActivityForResult(intent, 104);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		/**
		 * 风险检查-风险展示
		 */
		if(v.equals(mMainRiskShowingBtn)) {
			String mLinkRoute = mPreferences.getString("v_RiskShow", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, P38_RiskShowingAct.class);
				startActivityForResult(intent, 104);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
		}
		/**
		 * 风险检查-风险提醒
		 */
		if(v.equals(mMainRiskNoticeBtn)) {
			String mLinkRoute = mPreferences.getString("v_RiskTip", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, P42_RiskShowSetAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		
	}
	
	
	
}
