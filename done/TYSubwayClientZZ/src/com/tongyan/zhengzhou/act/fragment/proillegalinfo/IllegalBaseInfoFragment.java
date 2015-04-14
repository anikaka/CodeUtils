package com.tongyan.zhengzhou.act.fragment.proillegalinfo;

import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IllegalBaseInfoFragment extends Fragment{

	private TextView illegalName;
	private TextView sectionName;
	private TextView illegalArea;
	private TextView illegalDistance;
	private TextView illegalContent;
	private TextView illegalCompany;
	private TextView responsibleName;
	private TextView responsibleTel;
	private TextView declarantName;
	private TextView recordingTime;
	private HashMap<String, String> illegalMap = new HashMap<String, String>();
	
	public static IllegalBaseInfoFragment getInstance(HashMap<String, String> map){
		IllegalBaseInfoFragment fragment = new IllegalBaseInfoFragment();
		if(map != null){
			fragment.illegalMap.putAll(map);
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pro_illegal_base_info,null, false);
		
		illegalName =(TextView) view.findViewById(R.id.pro_illegal_base_name);
		sectionName = (TextView) view.findViewById(R.id.pro_section_base_name);
		illegalArea = (TextView) view.findViewById(R.id.pro_illegal_base_area);
		illegalDistance = (TextView) view.findViewById(R.id.pro_illegal_base_distance);
		illegalContent = (TextView) view.findViewById(R.id.pro_illegal_base_content); 
		illegalCompany = (TextView) view.findViewById(R.id.pro_illegal_base_company);
		responsibleName = (TextView) view.findViewById(R.id.pro_illegal_base_responsiblename);
		responsibleTel = (TextView) view.findViewById(R.id.pro_illegal_base_responsibletel);
		declarantName = (TextView) view.findViewById(R.id.pro_illegal_base_declarantname);
		recordingTime = (TextView) view.findViewById(R.id.pro_illegal_base_recordingtime);
		
		if(illegalMap != null && illegalMap.size() > 0){
			illegalName.setText("违规项目名称:"+illegalMap.get("IllegalName"));
			sectionName.setText("区间:"+illegalMap.get("SectionName"));
			illegalArea.setText("面积:"+illegalMap.get("Area"));
			illegalDistance.setText("距离地铁:"+illegalMap.get("Distance"));
			illegalContent.setText("违规内容:"+illegalMap.get("IllegalContent"));
			illegalCompany.setText("责任单位:"+illegalMap.get("IllegalCompany"));
			responsibleName.setText("责任联系人:"+illegalMap.get("ResponsibleName"));
			responsibleTel.setText("责任人联系电话:"+illegalMap.get("ResponsibleTel"));
			declarantName.setText("申请人:"+illegalMap.get("DeclarantName"));
			recordingTime.setText("告知单日期:"+illegalMap.get("RecordingTime"));
		}
		
		return view;
	}
}
