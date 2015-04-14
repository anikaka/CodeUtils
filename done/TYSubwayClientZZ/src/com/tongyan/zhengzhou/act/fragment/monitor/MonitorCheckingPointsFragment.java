package com.tongyan.zhengzhou.act.fragment.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;


public class MonitorCheckingPointsFragment extends MFinalFragment {
	
	private Context mContext;
	private HashMap<String, Object> mBaseMap = new HashMap<String, Object>();
	public static final String LEFT_DIR_X = "LeftDirX";
	public static final String RIGHT_DIR_X = "RightDirX";
	public static final String LEFT_DIR_NAME ="LeftDirName";
	public static final String RIGHT_DIR_NAME ="RightDirName";
	public static final String LEFT_DIRECTION = "LeftDirection";
	public static final String RIGHT_DIRECTION = "RightDirection";
	public static final String TIME = "Time";
//	ArrayList<String> leftTimeList = new ArrayList<String>();
	
	public static MonitorCheckingPointsFragment getInstance(Context context,HashMap<String, Object> map){
		MonitorCheckingPointsFragment fragment = new MonitorCheckingPointsFragment();
		fragment.mContext = context;
		fragment.mBaseMap.putAll(map);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.monitor_checking_points_fragment, container, false);
		LinearLayout timeLayout = (LinearLayout) view.findViewById(R.id.ll_time);
		if(mBaseMap != null){
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			View layoutView = layoutInflater.inflate(R.layout.each_linear_layout,null, false);
			LinearLayout layout = (LinearLayout) layoutView.findViewById(R.id.linearLayout);
//			layout.setBackgroundResource(getResources().getColor(R.color.blue));
			
			LayoutInflater inflater1 = LayoutInflater.from(getActivity());
			View mView = inflater1.inflate(R.layout.each_horizontal,null, false);
			View topView = mView.findViewById(R.id.topView);
			View downView = mView.findViewById(R.id.downView);
			View leftView = mView.findViewById(R.id.leftView);
			View rightView = mView.findViewById(R.id.rightView);
			TextView mTextView = (TextView) mView.findViewById(R.id.each);
			mTextView.setText("区段名称");
			layout.addView(mView);
			
			LayoutInflater inf = LayoutInflater.from(getActivity());
			View msView = inf.inflate(R.layout.each_horizontal,null, false);
			View topsView = msView.findViewById(R.id.topView);
			View downsView = msView.findViewById(R.id.downView);
			View leftsView = msView.findViewById(R.id.leftView);
			View rightsView = msView.findViewById(R.id.rightView);
			TextView msTextView = (TextView) msView.findViewById(R.id.each);
			msTextView.setText("测点名称");
			layout.addView(msView);
			//时间段
			ArrayList<String> leftTimeList = (ArrayList<String>) getYList(LEFT_DIRECTION,TIME).get("time");
			if(leftTimeList != null && leftTimeList.size() > 0){
				for(int i = 0; i < leftTimeList.size() ; i++){
					LayoutInflater inflater2 = LayoutInflater.from(getActivity());
					View convertView = inflater2.inflate(R.layout.each_horizontal,null, false);
					TextView textView = (TextView) convertView.findViewById(R.id.each);
					textView.setText(leftTimeList.get(i));
					layout.addView(convertView);
				}
			}else{
				topView.setVisibility(View.GONE);
				downView.setVisibility(View.GONE);
				leftView.setVisibility(View.GONE);
				rightView.setVisibility(View.GONE);
				topsView.setVisibility(View.GONE);
				downsView.setVisibility(View.GONE);
				leftsView.setVisibility(View.GONE);
				rightsView.setVisibility(View.GONE);
				msTextView.setVisibility(View.GONE);
				mTextView.setText("暂无数据");
				timeLayout.addView(layout);
				return view;
			}
			timeLayout.addView(layout);
			//上行
			ArrayList<ArrayList<String>> leftYList = (ArrayList<ArrayList<String>>) getYList(LEFT_DIRECTION,TIME).get("y");
			String leftName = (String) mBaseMap.get(LEFT_DIR_NAME);
			if(leftName != null && leftName.length() > 0){
				leftName = leftName.substring(leftName.indexOf("(")+1,leftName.lastIndexOf(")"));
			}
			ArrayList<String> leftXList = getXList(LEFT_DIR_X);
			writeData(leftXList,leftYList,leftName,timeLayout);
			
			//下行
			ArrayList<String> rightXList = getXList(RIGHT_DIR_X);
			ArrayList<ArrayList<String>> rightYList = (ArrayList<ArrayList<String>>) getYList(RIGHT_DIRECTION,TIME).get("y");
			String rightName = (String) mBaseMap.get(RIGHT_DIR_NAME);
			if(rightName != null && rightName.length() > 0){
				rightName = rightName.substring(rightName.indexOf("(")+1,rightName.lastIndexOf(")"));
			}
			writeData(rightXList,rightYList,rightName,timeLayout);
		}
		return view;
	}
	/**
	 * 填充数据
	 * @param xList
	 * @param yList
	 * @param name
	 * @param timeLayout
	 */
	public void writeData(ArrayList<String> xList,ArrayList<ArrayList<String>> yList,String name,LinearLayout timeLayout){
		if(xList != null && xList.size() > 0){
			for(int j = 0; j < xList.size(); j++){
				LayoutInflater layoutInflaterUp = LayoutInflater.from(getActivity());
				View layoutUpView = layoutInflaterUp.inflate(R.layout.each_linear_layout,null, false);
				LinearLayout layout = (LinearLayout) layoutUpView.findViewById(R.id.linearLayout);
				//区段名称
				LayoutInflater inflaterName = LayoutInflater.from(getActivity());
				View mNameView = inflaterName.inflate(R.layout.each_horizontal,null, false);
				TextView mNameTextView = (TextView) mNameView.findViewById(R.id.each);
				View topView = mNameView.findViewById(R.id.topView);
				View downView = mNameView.findViewById(R.id.downView);
				topView.setVisibility(View.INVISIBLE);
				downView.setVisibility(View.INVISIBLE);
				mNameTextView.setVisibility(View.INVISIBLE);
				if(j == 0){
					topView.setVisibility(View.VISIBLE);
				}
				if(j == xList.size() - 1){
					downView.setVisibility(View.VISIBLE);
				}
				if(2*j == xList.size() - 1 || 2*j == xList.size()){
					mNameTextView.setVisibility(View.VISIBLE);
					mNameTextView.setText(name);
				}
				layout.addView(mNameView);
				//测点名称
				LayoutInflater infName = LayoutInflater.from(getActivity());
				View mInfView = infName.inflate(R.layout.each_horizontal,null, false);
				TextView mInfTextView = (TextView) mInfView.findViewById(R.id.each);
				mInfTextView.setText(xList.get(j));
				layout.addView(mInfView);
				//落差量
				if(yList != null && yList.size() > 0){
					for(int k = 0; k < yList.size() ;k++){
						LayoutInflater sumLayout = LayoutInflater.from(getActivity());
						View mView = sumLayout.inflate(R.layout.each_horizontal,null, false);
						TextView mTextView = (TextView) mView.findViewById(R.id.each);
						mTextView.setText(yList.get(k).get(j));
						layout.addView(mView);
					}
				}
				timeLayout.addView(layout);
			}
		}
	}
	/**
	 * 获得控制点
	 * @param dirX
	 * @return
	 */
	public ArrayList<String> getXList(String dirX){
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) mBaseMap.get(dirX);
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				HashMap<String, String> map = list.get(i);
				if(map != null){
					xList.add(map.get("x"));
				}
			}
		}
		return xList;
	}
	/**
	 * 获取落差量
	 * @param direcion
	 * @return
	 */
	public HashMap<String, Object> getYList(String direcion,String time){
		HashMap<String, Object> objectMap = new HashMap<String, Object>();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<ArrayList<String>> yList = new ArrayList<ArrayList<String>>();
		ArrayList<HashMap<String, Object>> allList = (ArrayList<HashMap<String, Object>>) mBaseMap.get(direcion);
		if(allList != null){
			for(int i = 0; i < allList.size(); i++){
				HashMap<String, Object> map = allList.get(i);
				if(map != null){
					yList.add( (ArrayList<String>) map.get(direcion));
					timeList.add((String) map.get(time));
				}
			}
		}
		objectMap.put("time", timeList);
		objectMap.put("y", yList);
		return objectMap;
	}
	
}
