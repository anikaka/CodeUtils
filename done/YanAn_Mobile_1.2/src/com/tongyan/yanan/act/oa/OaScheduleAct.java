package com.tongyan.yanan.act.oa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.calendar.CalendarWidget;
import com.tongyan.yanan.common.widgets.calendar.OnCalendarSelectedListenter;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @className OaScheduleAct
 * @author Rubert
 * @date 2014-7-14 AM 08:16:24
 * @Desc 日程安排
 */
public class OaScheduleAct extends FinalActivity{
	
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	@ViewInject(id=R.id.schedule_all_btn, click="allScheduleListener") RelativeLayout mAllSchedule;
	@ViewInject(id=R.id.schedule_add_btn, click="addScheduleListener") RelativeLayout mAddSchedule;
	@ViewInject(id=R.id.schedule_top_date) TextView mDateContent;
	
	@ViewInject(id=R.id.schedule_calendar) CalendarWidget mCalendarWidget;
	
	@ViewInject(id=R.id.schedule_btn_pre_month, click="mPreMonthListener") Button mPreMonthBtn;
	@ViewInject(id=R.id.schedule_btn_next_month, click="mNextMonthListener") Button mNextMonthBtn;
	
	private Context mContext = this;
	private Dialog mDialog = null;
	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();
	// 当前操作日期
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	
	private SharedPreferences mPreferences;
	private String mUserid  = null;
	
	private ArrayList<HashMap<String, Object>> mAllList = null;
	private HashMap<String, ArrayList<HashMap<String, Object>>> mAllMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		setContentView(R.layout.oa_calendar_schedule);
		mTitleName.setText(getResources().getString(R.string.calendar_schedule));//标题
		mCalendarWidget.setCalendarSelectedListenter(selectedListener);
		getCalendarStartDate();
		loadingData();
	}
	
	
	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		// update days for week
		UpdateCurrentMonthDisplay();
	}
	// 设置当天日期和被选中日期
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		//calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			//calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			//calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}
		
		UpdateStartDateForMonth();
		return calStartDate;
	}
	// 更新日历标题上显示的年月
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "年" + (calStartDate.get(Calendar.MONTH) + 1) + "月";
		mDateContent.setText(date);
	}
	
	public void loadingData() {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.show();
		mDialog.setContentView(R.layout.common_normal_progressbar);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String ym = calStartDate.get(Calendar.YEAR) + "-" +  (calStartDate.get(Calendar.MONTH) + 1) + "-";
				String mStartDate = ym + "1";
				String mEndDate = ym + calStartDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				HashMap<String, String>  mParams=new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETWORKSCHEDULE);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mUserid);
				 mParams.put("pageNum", String.valueOf(1));
				 mParams.put("pageSize", String.valueOf(100));
				 mParams.put("stime", mStartDate);
				 mParams.put("etime", mEndDate);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					HashMap<String, Object> obj = JsonTools.getScheduleList(mStrJson);
					if(obj != null) {
						mAllList = (ArrayList<HashMap<String, Object>>)obj.get("AllList");
						mAllMap = (HashMap<String, ArrayList<HashMap<String, Object>>>)obj.get("AllMap");
						sendFMessage(Constants.SUCCESS);
					} else {
						sendFMessage(Constants.ERROR);
					}
				 }catch(Exception e){
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
			}
		}).start();
	}
	
	
	/**
	 * 上一个月
	 * @param v
	 */
	public void mPreMonthListener(View v) {
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentMonth--;
		
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		calStartDate.set(Calendar.MILLISECOND, 0);
		UpdateStartDateForMonth();
		//calendarWidget.setFontSize(fontSize);
		mCalendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
		//startDate = (Calendar) calStartDate.clone();
		//endDate = GetEndDate(startDate);
		loadingData();
	}
	
	/**
	 * 下一个月
	 * @param v
	 */
	public void mNextMonthListener(View v) {
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentMonth++;
		
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		UpdateStartDateForMonth();
		//calendarWidget.setFontSize(fontSize);
		mCalendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
		loadingData();
	}
	
	/**
	 * 添加日程
	 * @param v
	 */
	public void addScheduleListener(View v) {
		Intent intent = new Intent(mContext, OaScheduleAddAct.class);
		intent.putExtra("IntentType", "OaScheduleAct");
		startActivityForResult(intent, 9897);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadingData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 全部日程
	 * @param v
	 */
	public void allScheduleListener(View v) {
		Intent intent = new Intent(mContext, OaScheduleListAct.class);
		intent.putExtra("agendasList", mAllList);
		intent.putExtra("title", "全部日程");
		startActivityForResult(intent, 302);
	}
	/**
	 * 日历控件监听器
	 */
	OnCalendarSelectedListenter selectedListener = new OnCalendarSelectedListenter() {

		@Override
		public void onSelected(int year, int month) {
			
		}
		
		@Override
		public void onSelected(int year, int month, int day, View v,ArrayList<HashMap<String, Object>> agendaList) {
			if(agendaList != null && agendaList.size() > 0) {
				Intent intent = new Intent(mContext, OaScheduleListAct.class);
				intent.putExtra("agendasList", agendaList);
				intent.putExtra("title", year + "-" + month + "-" + day);
				startActivityForResult(intent, 302);
			} 
		}
	};
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constants.SUCCESS:
			closeDialog();
			mCalendarWidget.setAgendaHashMap(mAllMap);
			break;
		case Constants.ERROR:
			closeDialog();
			mCalendarWidget.setAgendaHashMap(null);
	 		Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
	 		break;
		case Constants.CONNECTION_TIMEOUT:
			closeDialog();
			mCalendarWidget.setAgendaHashMap(null);
	 		Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
	 		break;
		}
	};
	
}
