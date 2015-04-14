package com.tongyan.zhengzhou.act.fragment.proillegalinfo;

import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IllegalNoticeFragment extends Fragment{
	
	private HashMap<String, String> illegalMap = new HashMap<String, String>();
	private TextView noticeCompany;
	private TextView signCompany;
	private TextView noticePerson;
	private TextView signDate;
	
	public static IllegalNoticeFragment getInstance(HashMap<String, String> map){
		IllegalNoticeFragment fragment = new IllegalNoticeFragment();
		if(map != null){
			fragment.illegalMap.putAll(map);
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pro_illegal_notice_info, null , false);
		
		noticeCompany =(TextView) view.findViewById(R.id.pro_illegal_noice_company);
		signCompany = (TextView) view.findViewById(R.id.pro_illegal_sign_company);
		noticePerson = (TextView) view.findViewById(R.id.pro_illegal_noice_person);
		signDate = (TextView) view.findViewById(R.id.pro_illegal_sign_date);
		
		if(illegalMap != null && illegalMap.size() > 0){
			noticeCompany.setText("违规单位:"+illegalMap.get("NoticeCompany"));
			signCompany.setText("签收单位:"+illegalMap.get("SignCompany"));
			noticePerson.setText("告知书送达人:"+illegalMap.get("NoticeoPerson"));
			signDate.setText("签字时间:"+illegalMap.get("SignDate"));
		}
		
		return view;
	}
}
