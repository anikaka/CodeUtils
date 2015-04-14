package com.tongyan.zhengzhou.act.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.fragment.monitor.MonitorCheckingPointsFragment;
import com.tongyan.zhengzhou.act.fragment.monitor.MonitorDiagramFragment;
import com.tongyan.zhengzhou.common.afinal.MFinalFragmentActivity;
import com.tongyan.zhengzhou.common.afinal.annotation.view.ViewInject;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
/**
 * 
 * @Title: MonitorStructureAct.java 
 * @author Rubert
 * @date 2015-4-14 PM 02:51:02 
 * @version V1.0 
 * @Description: 地铁结构监测成果分析
 */
public class MonitorStructureAct extends MFinalFragmentActivity{

//	@ViewInject(id = R.id.title_content) TextView mTitle;
//	@ViewInject(id = R.id.menu_diagram_btn) Button mDiagramBtn;
//	@ViewInject(id = R.id.menu_checking_points_btn) Button mCheckingPointsBtn;
	
//	private ViewPager mPagerView;
//	private ArrayList<Fragment> fragmentsList;
//	private MFragmentPagerAdapter mPageFragment;
	
	@ViewInject(id=R.id.date_checking_points) RelativeLayout mAddCheckingPoints;
	@ViewInject(id=R.id.back_check_manage) RelativeLayout mCheckingChanging;
	@ViewInject(id=R.id.title_back_btn) LinearLayout mBackBtn;
	
	private TextView mTitle;
	private Button mDiagramBtn;
	private Button mCheckingPointsBtn;
	private Context mContext = this;
	private String mMonitorCode;
	private HashMap<String, Object> mBaseMap = new HashMap<String, Object>();
	
	private final static String LOCAL_CURVE = String.valueOf(2);       //本次曲线：2
	private final static String ACCUMULATE_CURVE = String.valueOf(1);  //累积曲线：1
	private String mCurrentCurve = LOCAL_CURVE;
	private String mLastCurve = mCurrentCurve;
	private ArrayList<String> mCachePointTimeList = new ArrayList<String>();
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_structure_act);
		MApplication.getInstance().addActivity(this); 
		
		if(getIntent().getExtras()!=null){
			mMonitorCode = (String) getIntent().getExtras().get("MonitorCode");
		}
		
		mTitle = (TextView) findViewById(R.id.title_content); 
		mDiagramBtn = (Button) findViewById(R.id.menu_diagram_btn); 
		mCheckingPointsBtn = (Button) findViewById(R.id.menu_checking_points_btn); 
		
		mTitle.setText("地铁结构监测成果分析");
		getGraphByCommonPro();
//		mPagerView = (ViewPager)findViewById(R.id.vViewPager);
//		fragmentsList = new ArrayList<Fragment>();
//		fragmentsList.add(new MonitorDiagramFragment());
//		fragmentsList.add(new MonitorCheckingPointsFragment());
		//http://blog.csdn.net/cshxql/article/details/22788343
//		mPageFragment = new MFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList);
		
//		mPagerView.setAdapter(mPageFragment);
//        mPagerView.setCurrentItem(0);
//        mPagerView.setOnPageChangeListener(new MyOnPageChangeListener());
//        mDiagramBtn.setOnClickListener(new MyOnClickListener(0));
//        mCheckingPointsBtn.setOnClickListener(new MyOnClickListener(1));
        mDiagramBtn.setOnClickListener(new MyOnClickListener());
        mCheckingPointsBtn.setOnClickListener(new MyOnClickListener());
        mAddCheckingPoints.setOnClickListener(new MyOnClickListener());
        mCheckingChanging.setOnClickListener(new MyOnClickListener());
        mBackBtn.setOnClickListener(new MyOnClickListener());
        mAddCheckingPoints.setVisibility(View.VISIBLE);
        mCheckingChanging.setVisibility(View.VISIBLE);
	}
	
	public void getGraphByCommonPro(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("MonitorCode", mMonitorCode);
				int len = mCachePointTimeList.size();
				if(len == 0) {
					map.put("TimeList", "[]");
				} else {
					StringBuffer buffer = new StringBuffer("[");
					for(int i = 0; i < len; i ++) {
						buffer.append("{Time:'").append(mCachePointTimeList.get(i)).append("'}");
						if(i != len - 1) {
							buffer.append(",");
						}
					}
					buffer.append("]");
					map.put("TimeList", buffer.toString());
				}
				map.put("GraphType", mCurrentCurve);
				String stream=null;
				try{
					stream = WebServiceUtils.requestM(mContext, map, Constants.METHOD_OF_CLIENT_GRAPHBYCOMMONPRO);
					mBaseMap = new JSONParseUtils().getGraphByCommonProInfo(stream);
					if(mBaseMap.size() > 0) {
						if(mCachePointTimeList.size() > 0) {
							mCachePointTimeList.clear();
						}
						mCachePointTimeList.addAll((ArrayList<String>)mBaseMap.get("CacheTimeList"));
						sendMessage(Constants.SUCCESS);
					} else {
						sendMessage(Constants.ERROR);
					}
				}catch (Exception e) {
					e.printStackTrace();
					sendMessage(Constants.CONNECTION_TIMEOUT);
				}
			}
		}).start();
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			getSupportFragmentManager().beginTransaction().replace(R.id.flLine, MonitorDiagramFragment.getInstance(mContext,mBaseMap)).commit();
			break;
		case Constants.CONNECTION_TIMEOUT :
			Toast.makeText(mContext, "网络连接超时,请重试", Toast.LENGTH_SHORT).show();
			break;
		case Constants.ERROR :
			Toast.makeText(mContext, "获取数据失败,请重试", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	};
	
	public class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.menu_diagram_btn:
				mCheckingPointsBtn.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
				mDiagramBtn.setTextColor(getResources().getColor(R.color.white));
				mCheckingPointsBtn.setBackgroundResource(R.color.detail_menu_normal_bg);
				mDiagramBtn.setBackgroundResource(R.color.transparent);
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, MonitorDiagramFragment.getInstance(mContext,mBaseMap)).commit();
				break;
			case R.id.menu_checking_points_btn:
				mCheckingPointsBtn.setTextColor(getResources().getColor(R.color.white));
				mDiagramBtn.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
				mCheckingPointsBtn.setBackgroundResource(R.color.transparent);
				mDiagramBtn.setBackgroundResource(R.color.detail_menu_normal_bg);
				getSupportFragmentManager().beginTransaction().replace(R.id.flLine, MonitorCheckingPointsFragment.getInstance(mContext, mBaseMap)).commit();
				break;
			case R.id.date_checking_points://新增测点
				//弹出日历
				if(mBaseMap != null && mBaseMap.size() > 0) {
					Intent intent = new Intent(mContext, AddPointCalendarDialogAct.class);
					intent.putStringArrayListExtra("CacheTimeList", (ArrayList<String>)mBaseMap.get("CacheTimeList"));
					intent.putExtra("MonitorCode", mMonitorCode);
					startActivityForResult(intent, 123);
				} else {
					Toast.makeText(mContext, "暂无测点数据", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.back_check_manage://次变、累变
				Dialog dialog = CommonUtils.createDialog(mContext, R.layout.monitor_change_select_dialog, 0.7, 0.1, getWindowManager());
				RadioGroup mRadioGroup = (RadioGroup)dialog.findViewById(R.id.curve_group);
				if(mCurrentCurve.equals(LOCAL_CURVE)) {
					mRadioGroup.check(R.id.local_curve);
				} else {
					mRadioGroup.check(R.id.accumulate_curve);
				}
				mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.local_curve://本次曲线
							mLastCurve = mCurrentCurve;
							mCurrentCurve = LOCAL_CURVE;
							if(!mLastCurve.equals(mCurrentCurve)) {
								getGraphByCommonPro();
							}
							break;
						case R.id.accumulate_curve://累计曲线
							mLastCurve = mCurrentCurve;
							mCurrentCurve = ACCUMULATE_CURVE;
							if(!mLastCurve.equals(mCurrentCurve)) {
								getGraphByCommonPro();
							}
							break;	
						default:
							break;
						}
					}
				});
				break;
			case R.id.title_back_btn:
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	@SuppressLint("ResourceAsColor")
	public void selectButton(int index) {
		if(1 == index){
			mCheckingPointsBtn.setTextColor(getResources().getColor(R.color.white));
			mDiagramBtn.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
			mCheckingPointsBtn.setBackgroundResource(R.color.transparent);
			mDiagramBtn.setBackgroundResource(R.color.detail_menu_normal_bg);
		}else{
			mCheckingPointsBtn.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
			mDiagramBtn.setTextColor(getResources().getColor(R.color.white));
			mCheckingPointsBtn.setBackgroundResource(R.color.detail_menu_normal_bg);
			mDiagramBtn.setBackgroundResource(R.color.transparent);
		}
	}
	
	    @Override
		protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
			if(requestCode == 123) {//CheckingPointsSet
				SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
				Set<String> mSet = mSharedPreferences.getStringSet("CheckingPointsSet", null);
				if(mSet != null && mSet.size() > 0) {
					Iterator<String> it = mSet.iterator();
					ArrayList<String> list = new ArrayList<String>();
					while (it.hasNext()) {  
					  list.add(it.next());
					}  
					compareWithList(list);
				}
			}
			super.onActivityResult(requestCode, arg1, arg2);
		}
	 /**
	  * 逻辑：比较原始时间列表和新增修改后的时间列表是否有异同，如果有则重新加载数据，否则不加载
	  * @param mList
	  */
	  private void compareWithList(ArrayList<String> mList) {
		  int len = mList.size();
		 if(len == 0) {
			 Toast.makeText(mContext, "请选择测点", Toast.LENGTH_SHORT).show();
		 } else {
			if(mCachePointTimeList.size() != mList.size()) {//肯定改动
				mCachePointTimeList.clear();
				mCachePointTimeList.addAll(mList);
				getGraphByCommonPro();
			} else {//不一定有改动
				for(String s : mList) {
					boolean isCommon = false;
					if(s != null) {
						for(String sc : mCachePointTimeList) {
							if(s.equals(sc)) {
								isCommon = true;
							}
						}
					}
					if(!isCommon) {
						mCachePointTimeList.clear();
						mCachePointTimeList.addAll(mList);
						getGraphByCommonPro();
						break;
					}
				}
			}
		 }
	  }
}
