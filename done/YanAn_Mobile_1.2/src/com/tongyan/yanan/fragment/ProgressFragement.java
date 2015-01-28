package com.tongyan.yanan.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.progress.plan.ProgressProjectAct;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.https.HttpUtils;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @category进度界面
 * @author Administrator
 * @date 2014/06/19
 * @version YanAn 1.0
 */

public class ProgressFragement extends  BaseFragement implements View.OnClickListener{
	
	private View mView;
	RelativeLayout  mRlContent_reported_project_week, //周计划
							mRlContent_reported_project_month, //月计划
							mRlContent_reported_project_year,  	 //年计划
							mRlContent_reported_accomplish_day,  //日完成量
							mRlContent_reported_accomplish_week, //周完成量
							mRlContent_reported_accomplish_month,//月完成量
							mRlContent_reported_accomplish_year;		 //年完成量
	private Dialog mDialog;
	
	private SharedPreferences mPreferences;
	
	public  void init(){
		mRlContent_reported_project_week=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_project_week);
		mRlContent_reported_project_month=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_project_month);
		mRlContent_reported_project_year=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_project_year);
		
		mRlContent_reported_accomplish_day=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_accomplish_day);
		mRlContent_reported_accomplish_week=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_accomplish_week);
		mRlContent_reported_accomplish_month=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_accomplish_month);
		mRlContent_reported_accomplish_year=(RelativeLayout)mView.findViewById(R.id.rlContent_reported_accomplish_year);
		
		mRlContent_reported_project_week.setOnClickListener(this);
		mRlContent_reported_project_month.setOnClickListener(this);
		mRlContent_reported_project_year.setOnClickListener(this);

		mRlContent_reported_accomplish_day.setOnClickListener(this);
		mRlContent_reported_accomplish_week.setOnClickListener(this);
		mRlContent_reported_accomplish_month.setOnClickListener(this);
		mRlContent_reported_accomplish_year.setOnClickListener(this);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 mView=inflater.inflate(R.layout.layout_progress, null, false);
		 init(); //初始化
		 mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 String s = mPreferences.getString(Constants.PREFERENCES_PROGRESS_SETTING, "");
		 String mNowTarget = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		 if ((s != null && !s.equals(mNowTarget)) || new DBService(getActivity()).isEmpty("ProgressInfo")) {
			getTypeInfo(mNowTarget);
		 }
		return mView;
	}
	
	public void savePreference(String nowTarget) {
		Editor mEditor = mPreferences.edit();
		mEditor.putString(Constants.PREFERENCES_PROGRESS_SETTING, nowTarget);
		mEditor.commit();
	}
	
	public void getTypeInfo(final String nowTarget){
		//加载进度条
		mDialog=new Dialog(getActivity(), R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		getActivity().setProgressBarIndeterminateVisibility(true);
		mDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				HashMap<String, String> paramProgress = new HashMap<String, String>();
				paramProgress.put("method", Constants.METHOD_OF_GET_CONSTURCTION);
				paramProgress.put("key", Constants.PUBLIC_KEY);
				paramProgress.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));	
				paramProgress.put("fieldList", "");
				try {
					String mProgressResponseBody = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, paramProgress, getActivity()));
					HashMap<String, Object> mProgressBaseData = JsonTools.getProgerssInfo(mProgressResponseBody);
					if(mProgressBaseData != null && "ok".equalsIgnoreCase((String)mProgressBaseData.get("s"))) {
						ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)mProgressBaseData.get("v");
						if(list != null && list.size() > 0) {
							if(new DBService(getActivity()).delAll(Constants.TABLE_PROGRESS_INFO)) {
								if(new DBService(getActivity()).insertProgressInfo(list)) {
									//loginSend(mLoginMap, mUMap);
									sendFragmentMessage(Constants.COMMON_MESSAGE_1);
									savePreference(nowTarget);
								} else {
									sendFragmentMessage(Constants.COMMON_MESSAGE_2);
								}
							} else {
								sendFragmentMessage(Constants.COMMON_MESSAGE_2);
							}
						} else {
							sendFragmentMessage(Constants.COMMON_MESSAGE_2);
						}
					} else {
						sendFragmentMessage(Constants.COMMON_MESSAGE_2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendFragmentMessage(Constants.COMMON_MESSAGE_2);
				}
			}
		}).start();
	}
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.COMMON_MESSAGE_1:
			Toast.makeText(getActivity(), "基础数据加载成功", Toast.LENGTH_SHORT).show();
			closeDialog();
			break;
		case Constants.COMMON_MESSAGE_2:
			Toast.makeText(getActivity(), "基础数据加载失败", Toast.LENGTH_SHORT).show();
			closeDialog();
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlContent_reported_project_week: // 周计划
				Intent intentWeek=new Intent(getActivity(),ProgressProjectAct.class);			  		  
				intentWeek.putExtra("projectInfo", "周计划");
				getActivity().startActivity(intentWeek);
			break;
		case R.id.rlContent_reported_project_month: // 月计划
			    Intent  intentM=new Intent(getActivity(),ProgressProjectAct.class);			
			    intentM.putExtra("projectInfo", "月计划");
				getActivity().startActivity(intentM);
			break;
		case R.id.rlContent_reported_project_year: // 年计划
//			    Intent  intentY=new Intent(getActivity(),ProgressProjectAct.class);			
//			    intentY.putEsxtra("projectInfo", "年计划");
//			    getActivity().startActivity(intentY);
			    Toast.makeText(getActivity(), "正在开发中...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rlContent_reported_accomplish_day: // 日完成量
			    Intent  intent=new Intent(getActivity(),ProgressProjectAct.class);			
				intent.putExtra("projectInfo", "日完成量");
				getActivity().startActivity(intent);
			break;
		case R.id.rlContent_reported_accomplish_week: // 周完成量
				Intent intentAccomplishWeek=new Intent(getActivity(),ProgressProjectAct.class);			
				intentAccomplishWeek.putExtra("projectInfo", "周完成量");
				getActivity().startActivity(intentAccomplishWeek);
			break;
		case R.id.rlContent_reported_accomplish_month: // 月完成量
			   Intent  intentAccomplishMonth=new Intent(getActivity(),ProgressProjectAct.class);			    
			   intentAccomplishMonth.putExtra("projectInfo", "月完成量");
			   getActivity().startActivity(intentAccomplishMonth);
			break;
		case R.id.rlContent_reported_accomplish_year: // 年完成量
				/*Intent  intentAccomplishYear=new Intent(getActivity(),ProgressProjectAct.class);			  
				intentAccomplishYear.putExtra("projectInfo", "年完成量");
				getActivity().startActivity(intentAccomplishYear);*/
			Toast.makeText(getActivity(), "正在开发中...", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}

