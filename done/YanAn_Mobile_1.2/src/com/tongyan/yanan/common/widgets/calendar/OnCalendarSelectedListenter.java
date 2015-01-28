package com.tongyan.yanan.common.widgets.calendar;

import java.util.ArrayList;
import java.util.HashMap;


import android.view.View;

/**
 * 
 * Created by Eclipse3.6.2
 * @ClassName: OnCalendarSelectedListenter
 * @Author wanghb
 * @Date 2012-10-26 pm 02:40:49 
 * @Desc: TODO
 */
public interface OnCalendarSelectedListenter {
	public void onSelected(int year,int month);
	//public void onSelected(int year,int month,int day);
	public void onSelected(int year,int month,int day,View v,ArrayList<HashMap<String,Object>> agendaList);
}
