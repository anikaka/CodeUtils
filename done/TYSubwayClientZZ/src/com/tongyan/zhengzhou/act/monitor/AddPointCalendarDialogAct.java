package com.tongyan.zhengzhou.act.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.afinal.MFinalActivity;
import com.tongyan.zhengzhou.common.afinal.annotation.view.ViewInject;
import com.tongyan.zhengzhou.common.utils.CommonUtils;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
import com.tongyan.zhengzhou.common.widgets.calendar.CalendarWidget;
import com.tongyan.zhengzhou.common.widgets.calendar.OnCalendarSelectedListenter;
import com.tongyan.zhengzhou.common.widgets.datetime.wheelview.MDatePickerNoDayDialog;
import com.tongyan.zhengzhou.common.widgets.datetime.wheelview.MDatePickerNoDayDialog.OnDateTimeSetListener;
/**
 * 
 * @Title: AddPointCalendarAct.java 
 * @author Rubert
 * @date 2015-4-8 AM 11:25:15 
 * @version V1.0 
 * @Description: 添加测点日历Dialog
 */
public class AddPointCalendarDialogAct extends MFinalActivity implements OnClickListener{
	
	private final static String FALSE = "false";
	private final static String TRUE = "true";
	
	
	// 当前操作日期
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	public static Calendar calStartDate = Calendar.getInstance();
	private Context mContext = this;
	
	@ViewInject(id= R.id.monitor_checking_points_list) ListView mListView;//存放测点日期
	@ViewInject(id= R.id.calendar_widget) CalendarWidget mCalendarWidget;//日历控件
	@ViewInject(id= R.id.calendar_previous) RelativeLayout mPreviousBtn;//上一月
	@ViewInject(id= R.id.calendar_next) RelativeLayout mNextBtn;//下一月
	@ViewInject(id= R.id.date_show_text) TextView mDateText;//月份日期
	
	private String mMonitorCode;
	private Map<String,Map<String,Boolean>> mList;
	private Dialog mDialog = null;
	
	private ArrayList<String> mCheckingPoints = new ArrayList<String>();
	private MonitorCheckingPointAdapter mAdapter = null;
	
	private int mScreenWidth = 0;
	
	private LayoutInflater mInflater = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initLayout();
		
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*LinearLayout mTimeContainer = (LinearLayout)findViewById(R.id.date_time_container);//存放测点日期
		CalendarWidget mCalendarWidget = (CalendarWidget)findViewById(R.id.calendar_widget);//日历控件
		Button mPreviousBtn = (Button)findViewById(R.id.calendar_previous);//上一月
		Button mNextBtn = (Button)findViewById(R.id.calendar_next);//下一月
		TextView mDateText = (TextView)findViewById(R.id.date_show_text);//月份日期
*/		//init
		ArrayList<String> mCacheTimeList = null;
		if(getIntent().getExtras()!=null){
			mMonitorCode = (String) getIntent().getExtras().get("MonitorCode");
			mCacheTimeList = (ArrayList<String>)getIntent().getExtras().get("CacheTimeList");
		}
		
		//set listener
		mCalendarWidget.setCalendarSelectedListenter(mCalendarSelectedListener);
		mPreviousBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
		mDateText.setOnClickListener(this);
		//
		if(mCacheTimeList != null)
		mCheckingPoints.addAll(mCacheTimeList);
		mAdapter = new MonitorCheckingPointAdapter();
		mListView.setAdapter(mAdapter);
		
		UpdateStartDateForMonth();
	}
	
	@Override
	protected void onPause() {
		if(mCheckingPoints != null && mCheckingPoints.size() > 0) {
			SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			Editor mEditor = mSharedPreferences.edit();
			Set<String> w = new HashSet<String>();
			w.addAll(mCheckingPoints);
			mEditor.putStringSet("CheckingPointsSet", w);
			mEditor.commit();
			setResult(234);
		}
		super.onPause();
	}
	
	
	
	/**
	 * 初始化布局
	 */
	private void initLayout() {
		setContentView(R.layout.monitor_calendar_select);
		MApplication.getInstance().addActivity(this); 
	    Display d = getWindowManager().getDefaultDisplay();  //为获取屏幕宽、高    
	    LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值    
	    mScreenWidth = d.getWidth();   //宽度设置为屏幕的0.8
	    
	    p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的1.0   
	    p.width = (int) (mScreenWidth * 0.8); ;    //宽度设置为屏幕的0.8
	    
	    this.onWindowAttributesChanged(p);
	    
	    mCalendarWidget.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(d.getHeight() * 0.4)));
	    
	}
	
	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		UpdateCurrentMonthDisplay();
	}
	
	// 更新日历标题上显示的年月
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "年" + (calStartDate.get(Calendar.MONTH) + 1) + "月";
		mDateText.setText(date);
	}
	
	
	OnCalendarSelectedListenter mCalendarSelectedListener = new OnCalendarSelectedListenter() {
		@Override
		public void onSelected(int year, int month) {}
		
		@Override
		public void onSelected(final int year, final int month,final int day, View v, Map<String, Boolean> dateMap) {
			if(dateMap != null && dateMap.size() > 0) {
				final Dialog md = CommonUtils.createDialog(mContext, R.layout.monitor_time_point_dialog, 0.46, 0.30, getWindowManager());//TODO
				GridView mGridView = (GridView)md.findViewById(R.id.monitor_points_gridview);
				ArrayList<HashMap<String,String>> mTimeList = new ArrayList<HashMap<String, String>>();
				for(int i = 1; i <= 24; i ++) {
					HashMap<String, String> map = new HashMap<String, String>();
					if(i < 10) {
						String time = String.valueOf(0) + i;
						map.put("Time", time + ":00");
						if(dateMap != null && dateMap.size() > 0) {
							if(dateMap.get(String.valueOf(0) + i) != null && dateMap.get(String.valueOf(0) + i) == true ) {
								map.put("isData", TRUE);
							} else {
								map.put("isData", FALSE);
							}
						}
					} else {
						if(dateMap != null && dateMap.size() > 0) {
							map.put("Time", i + ":00");
							if(dateMap != null && dateMap.size() > 0) {
								if(dateMap.get(String.valueOf(0) + i) != null && dateMap.get(String.valueOf(0) + i) == true ) {
									map.put("isData", TRUE);
								} else {
									map.put("isData", FALSE);
								}
							}
						}
					}
					mTimeList.add(map);
				}
				CheckingPointsTimeAdapter mPointTimeAdapter = new CheckingPointsTimeAdapter(mTimeList);
				mGridView.setAdapter(mPointTimeAdapter);
				mGridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						if(mCheckingPoints.size() <9) {
							CheckingPointsTimeAdapter.ViewHolder mViewHolder = (CheckingPointsTimeAdapter.ViewHolder)arg1.getTag();
							HashMap<String, String> map = mViewHolder.mData;
							if(map != null) {
								String mClickTime = year + "-" + (month < 10 ? String.valueOf("0" + month) : month) + "-" +(day < 10 ? String.valueOf("0" + day) : day) +" " + map.get("Time") + ":00";
								if(TRUE.equals(map.get("isData"))) {
									boolean isExist = false;
									for(String s : mCheckingPoints) {
										if(s.equals(mClickTime)) {
											isExist = true;
										}
									}
									if(isExist) {
										Toast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
									} else {
										mCheckingPoints.add(mClickTime);
										mAdapter.notifyDataSetChanged();
									}
								} else {
									Toast.makeText(mContext, "该项无数据", Toast.LENGTH_SHORT).show();
								}
							}
						} else {
							Toast.makeText(mContext, "不能再添加了", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}
	};
	
	class CheckingPointsTimeAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, String>> mList;
		
		public CheckingPointsTimeAdapter (ArrayList<HashMap<String, String>> list) {
			this.mList = list;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckingPointsTimeAdapter.ViewHolder mViewHolder = null;
			if(convertView == null) {
				mViewHolder = new CheckingPointsTimeAdapter.ViewHolder();
				convertView = mInflater.inflate(R.layout.monitor_gridview_item, null);
				TextView view = (TextView)convertView.findViewById(R.id.monitor_gridview_content);
				mViewHolder.mContent = view;
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder)convertView.getTag();
			}
			HashMap<String, String> map = mList.get(position);
			mViewHolder.mContent.setText(map.get("Time"));
			mViewHolder.mData = map;
			if(TRUE.equals(map.get("isData"))){
				convertView.setBackgroundColor(getResources().getColor(R.color.monitor_calendar_point_color));
			} else {
				convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			return convertView;
		}
		class ViewHolder {
			public TextView mContent;
			public HashMap<String, String> mData;
		}
	}
	
	
	
	
	
	
	/**
	 * 加载日历相关数据
	 */
	private void loadingData() {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String stream=null;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("MonitorCode", mMonitorCode);
				map.put("Month", calStartDate.get(Calendar.YEAR) + "-" +  (calStartDate.get(Calendar.MONTH) + 1));
				try {
					stream = WebServiceUtils.requestM(mContext, map, Constants.METHOD_OF_MONITORCALENDARLIST);
					mList = new JSONParseUtils().getMonitorCalendarList(stream);
					if(mList != null) {
						sendMessage(Constants.SUCCESS);
					} else {
						sendMessage(Constants.GET_DATA_ERROR);
					}
				} catch(Exception e) {
					e.printStackTrace();
					sendMessage(Constants.CONNECTION_TIMEOUT);
				}
			}
		}).start();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calendar_next://下一月
			iMonthViewCurrentMonth++;
			if (iMonthViewCurrentMonth == 12) {
				iMonthViewCurrentMonth = 0;
				iMonthViewCurrentYear++;
			}
			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			UpdateStartDateForMonth();
			mCalendarWidget.setFontSize(15);
			mCalendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
			loadingData();
			break;
		case R.id.calendar_previous://上一月
			iMonthViewCurrentMonth--;
			if (iMonthViewCurrentMonth == -1) {
				iMonthViewCurrentMonth = 11;
				iMonthViewCurrentYear--;
			}
			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			UpdateStartDateForMonth();
			mCalendarWidget.setFontSize(15);
			mCalendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
			loadingData();
			break;
		case R.id.date_show_text://选择
			new MDatePickerNoDayDialog(mContext,CommonUtils.getScreenWidth(getWindowManager()) ,new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear) {
					calStartDate.set(Calendar.MONTH, monthOfYear - 1);
					calStartDate.set(Calendar.YEAR, year);
					mCalendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
					UpdateStartDateForMonth();
					loadingData();
				}
			}).show();
			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @Title: AddPointCalendarDialogAct.java 
	 * @author Rubert
	 * @date 2015-4-10 PM 05:10:34 
	 * @version V1.0 
	 * @Description: 新增时间测点的适配器
	 */
	class MonitorCheckingPointAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mCheckingPoints.size();
		}

		@Override
		public Object getItem(int position) {
			return mCheckingPoints.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder = null;
			if(convertView == null){
				mViewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.monitor_checking_point_item, null);
				mViewHolder.mPointText=(TextView)convertView.findViewById(R.id.monitor_checking_points_content);
				mViewHolder.mDeleteBtn=(RelativeLayout)convertView.findViewById(R.id.monitor_checking_point);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder=(ViewHolder)convertView.getTag();
			}
			mViewHolder.mPointText.setText(mCheckingPoints.get(position));
			mViewHolder.mDeleteBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCheckingPoints.remove(position);
					mAdapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
		class ViewHolder {
			TextView mPointText;
			RelativeLayout mDeleteBtn;
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			mCalendarWidget.setAgendaHashMap(mList);
			break;
		case Constants.GET_DATA_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
			break;
		case Constants.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
}
