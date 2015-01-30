package com.tongyan.fragment;

import com.tongyan.activity.R;
import com.tongyan.activity.gps.GpsSearchSettingAct;
import com.tongyan.activity.gps.GpsProjectLocAct;
import com.tongyan.activity.oa.OAContactsAct;

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
 * @Desc 主页面-GPS定位
 */
public class MainGpsLocFragment extends Fragment implements OnClickListener{
	
	private Context mMContext;
	private Button mMainGpsWorkingSearchBtn,mMainGpsProjectReadBtn,mMainGpsRouteSearchBtn;
	private SharedPreferences mPreferences = null;
	
	
	public static MainGpsLocFragment newInstance(Context mContext) {
		MainGpsLocFragment mFragment = new MainGpsLocFragment();
		mFragment.mMContext = mContext;
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMContext=getActivity();
		View view = inflater.inflate(R.layout.main_mobile_gps_fragment, container, false);
		mMainGpsWorkingSearchBtn = (Button) view.findViewById(R.id.main_gps_working_search_btn);
		mMainGpsProjectReadBtn = (Button) view.findViewById(R.id.main_gps_project_info_reading_btn);
		mMainGpsRouteSearchBtn = (Button) view.findViewById(R.id.main_gps_project_user_route_btn);
		mMainGpsWorkingSearchBtn.setOnClickListener(this);
		mMainGpsProjectReadBtn.setOnClickListener(this);
		mMainGpsRouteSearchBtn.setOnClickListener(this);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mMContext);
		return view;
	}

	@Override
	public void onClick(View v) {
		/**
		 * 在岗查询
		 */
		if(v.equals(mMainGpsWorkingSearchBtn)) {
			String mLinkRoute = mPreferences.getString("v_Attendance", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, GpsSearchSettingAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		/**
		 * 工程订阅
		 */
		if (v.equals(mMainGpsProjectReadBtn)) {
			String mLinkRoute = mPreferences.getString("v_Project", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, GpsProjectLocAct.class);
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		/**
		 * 轨迹查询
		 */
		if (v.equals(mMainGpsRouteSearchBtn)) {
			String mLinkRoute = mPreferences.getString("v_Locus", "");
			if (mLinkRoute != null && "1".equals(mLinkRoute)) {
				Intent intent = new Intent(mMContext, OAContactsAct.class);
				intent.putExtra("type", "route");
				startActivity(intent);
			} else {
				Toast.makeText(mMContext, "您暂无该权限", Toast.LENGTH_SHORT).show();
			}
			return;
		}
	}
}
