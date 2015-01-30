package com.tongyan.activity.oa;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Agendas;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.calndar.CalendarWidget;
import com.tongyan.widget.calndar.OnCalendarSelectedListenter;

/**
 * 
 * @ClassName P07_AgendaScheduleAct 
 * @author wanghb
 * @date 2013-7-16 pm 01:28:38
 * @desc 移动OA-日程安排
 */
public class OaAgendaScheduleAct extends AbstructCommonActivity{//
	
	private Context mContext = this;
	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();
	// 当前操作日期
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	
	
	// 页面控件
	TextView Top_Date = null;
	Button btn_pre_month = null;
	Button btn_next_month = null;
	RelativeLayout btn_all_agenda = null;
	RelativeLayout btn_add_agenda = null;
	
	private _User localUser;
	private String isSucc;
	
	private CalendarWidget calendarWidget;
	TextView textSizeView;
	
	float fontSize;
	
	private Dialog mDialog;
	
	private Button homeBtn;
	
	private Map<String,List<_Agendas>> agendasMap = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setOnclickListener();
		businessM();
	}
	
	
	private void initPage() {
		((MyApplication)getApplication()).addActivity(this);
		// 制定布局文件，并设置属性
		setContentView(R.layout.oa_schedule_main);
		homeBtn = (Button)findViewById(R.id.p08_schedule_home_btn);
		Top_Date = (TextView) findViewById(R.id.p08_schedule_top_date);
		btn_pre_month = (Button) findViewById(R.id.p07_schedule_btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.p07_schedule_btn_next_month);
		btn_all_agenda = (RelativeLayout) findViewById(R.id.p08_schedule_all_btn);
		btn_add_agenda = (RelativeLayout) findViewById(R.id.p08_schedule_add_btn);
		calendarWidget = (CalendarWidget) findViewById(R.id.p08_schedule_calendar);
		textSizeView = (TextView)findViewById(R.id.p08_test_TextSize);
		
		fontSize = textSizeView.getTextSize();
	}
	
	public void setOnclickListener() {
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());
		btn_all_agenda.setOnClickListener(all_Agendas);
		btn_add_agenda.setOnClickListener(add_Agendas);
		homeBtn.setOnClickListener(homeBtnListener);
		calendarWidget.setCalendarSelectedListenter(selectedListener);
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnCalendarSelectedListenter selectedListener = new OnCalendarSelectedListenter() {

		@Override
		public void onSelected(int year, int month) {
			
		}

		@Override
		public void onSelected(int year, int month, int day, View v,
				List<_Agendas> agendaList) {
			//BaseToast.show(this, agendaList == null ? "null" : agendaList.size() + "11");
			if(agendaList != null && agendaList.size() > 0) {
				Intent intent = new Intent(mContext,OaAgendaSomeAct.class);
				intent.putExtra("agendasList", (Serializable)agendaList);
				startActivityForResult(intent, 302);
			} 
		}
		
	};
	
	private void businessM() {
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		calendarWidget.setFontSize(fontSize);
		calStartDate = getCalendarStartDate();
		loadingData();
	}
	
	public void loadingData() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String ym = calStartDate.get(Calendar.YEAR) + "-" +  (calStartDate.get(Calendar.MONTH) + 1) + "-";
					String mStartDate = ym + "1";
					String mEndDate = ym + calStartDate.getActualMaximum(Calendar.DAY_OF_MONTH);
					String params = "{emp_id:'"+localUser.getUserid()+"',stime:'"+ mStartDate +"',etime:'"+mEndDate+"'}";
					String jsonStr = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), String.valueOf(200), "1", "Scheduling", params, Constansts.METHOD_OF_GETLIST,mContext);
					Map<String,Object> mR = new Str2Json().getScheduleList(jsonStr,(calStartDate.get(Calendar.MONTH) + 1),calStartDate.getActualMaximum(Calendar.DAY_OF_MONTH));
					//Map<String,Object> mR = new Str2Json().getScheduleList(jsonStr);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							agendasMap = (Map<String,List<_Agendas>>)mR.get("agendasMap");
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				}  catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	// 更新日历标题上显示的年月
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "年" + (calStartDate.get(Calendar.MONTH) + 1) + "月";
		Top_Date.setText(date);
	}
	
	OnClickListener add_Agendas = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,OaAgendaAddAct.class);
			startActivityForResult(intent, 302);
		}
	};
	
	
	OnClickListener all_Agendas = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,OaAgendaAllAct.class);
			intent.putExtra("yearAndMonth", calStartDate.get(Calendar.YEAR) + "-" + (calStartDate.get(Calendar.MONTH) + 1));
			intent.putExtra("ActualMaximum", String.valueOf(calStartDate.getActualMaximum(Calendar.DAY_OF_MONTH)));
			startActivityForResult(intent, 302);
		}
	};

	// 点击下月按钮，触发事件
	class Next_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
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
			calendarWidget.setFontSize(fontSize);
			calendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
			loadingData();
		}
	}
	
	class Pre_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
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
			calendarWidget.setFontSize(fontSize);
			calendarWidget.onClick(calStartDate.get(Calendar.YEAR), calStartDate.get(Calendar.MONTH));
			//startDate = (Calendar) calStartDate.clone();
			//endDate = GetEndDate(startDate);
			loadingData();
		}
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadingData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(104);
		}
		super.onKeyDown(keyCode, event);
		return true;
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			calendarWidget.setAgendaHashMap(agendasMap);
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			calendarWidget.setAgendaHashMap(null);
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			calendarWidget.setAgendaHashMap(null);
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
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
