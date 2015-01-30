package com.tongyan.fragment;

import com.tongyan.activity.R;
import com.tongyan.activity.supervice.SuperviceAct;
import com.tongyan.activity.supervice.SuperviceSettingAct;

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
 * @className MainSupervisionFragment
 * @author wanghb
 * @date 2014-6-11 PM 03:18:57
 * @Desc 主页面-监理监督
 */
public class MainSupervisionFragment extends Fragment implements OnClickListener{
	
	private Context mMContext;
	private Button mMainSupervisionAddBtn, mMainGpsReadResultBtn;
	private SharedPreferences mPreferences = null;
	
	public static MainSupervisionFragment newInstance(Context mContext) {
		MainSupervisionFragment mFragment = new MainSupervisionFragment();
		mFragment.mMContext = mContext;
	
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mMContext==null){
			mMContext=getActivity();
		}
		View view = inflater.inflate(R.layout.main_mobile_supervision_fragment, container, false);
		mMainSupervisionAddBtn = (Button) view.findViewById(R.id.main_supervision_add_btn);
		mMainGpsReadResultBtn = (Button) view.findViewById(R.id.main_supervision_read_result_btn);
		mMainSupervisionAddBtn.setOnClickListener(this);
		mMainGpsReadResultBtn.setOnClickListener(this);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mMContext);
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(mMainSupervisionAddBtn)) {
			String mLinkRoute = mPreferences.getString("v_SupervisionIn", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, SuperviceAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		
		if (v.equals(mMainGpsReadResultBtn)) {
			String mLinkRoute = mPreferences.getString("v_SupervisionView", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, SuperviceSettingAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
	}
	
}
