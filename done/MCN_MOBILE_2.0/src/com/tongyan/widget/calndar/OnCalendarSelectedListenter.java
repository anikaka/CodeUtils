package com.tongyan.widget.calndar;

import java.util.List;

import com.tongyan.common.entities._Agendas;

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
	public void onSelected(int year,int month,int day,View v,List<_Agendas> agendaList);
}
