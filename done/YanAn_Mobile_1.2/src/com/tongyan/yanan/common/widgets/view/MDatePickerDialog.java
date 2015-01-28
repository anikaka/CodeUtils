package com.tongyan.yanan.common.widgets.view;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.widgets.datetime.wheelview.NumericWheelAdapter;
import com.tongyan.yanan.common.widgets.datetime.wheelview.OnWheelChangedListener;
import com.tongyan.yanan.common.widgets.datetime.wheelview.WheelView;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * @className MDatePickerDialog2
 * @author wanghb
 * @date 2014-7-17 PM 01:07:03
 * @Desc TODO
 */
public class MDatePickerDialog extends Dialog implements android.view.View.OnClickListener {
	private static int START_YEAR = 2000,END_YEAR=2100;
	private final OnDateTimeSetListener mCallBack;
	private final Calendar mCalendar;
	private int curr_year, curr_month, curr_day;
	// 添加大小月月份并将其转换为list,方便之后的判断
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	final WheelView wv_year , wv_month, wv_day; 
	final List<String> list_big, list_little;
	
	private final Button btn1,btn2;
	
	private Context mContext;
	
	public MDatePickerDialog(Context context, OnDateTimeSetListener callBack) {
		this(context, START_YEAR,END_YEAR, callBack);
		this.mContext = context;
	}
	
	public MDatePickerDialog(Context context, final int START_YEAR,  final int END_YEAR,OnDateTimeSetListener callBack) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.common_date_picker_layout, null);
		setContentView(view);
		this.START_YEAR = START_YEAR;
		mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DATE);
		this.END_YEAR = END_YEAR;
		mCallBack = callBack;
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		
		TextView titltView = (TextView)view.findViewById(R.id.timePickerTitle);
		titltView.setText("日期选择");
		
		int textSize = 0;
		textSize = adjustFontSize(getWindow().getWindowManager()); 
		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);
		
		btn1 = (Button) view.findViewById(R.id.ok);
		btn2 = (Button) view.findViewById(R.id.cancle);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		
	}
	
	 public void show() {
	     super.show();
	 }
	 
	public  interface OnDateTimeSetListener {
			boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth);
	}
	
	public static int adjustFontSize(WindowManager windowmanager) {

		 int screenWidth = windowmanager.getDefaultDisplay().getWidth();
		if (screenWidth <= 240) { // 240X320 屏幕
			return 10;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 14;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 24;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 26;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 30;
		} else { // 大于 800X1280
			return 30;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.equals(btn1)) {
			curr_year = wv_year.getCurrentItem() + START_YEAR;
			curr_month = wv_month.getCurrentItem() + 1;
			curr_day = wv_day.getCurrentItem() + 1;
			if (mCallBack != null) {
				if(mCallBack.onDateTimeSet(curr_year, curr_month, curr_day)) {
					dismiss();
				} 
			}
		} else if(v.equals(btn2)) {
			super.dismiss();
		}
	}
}

