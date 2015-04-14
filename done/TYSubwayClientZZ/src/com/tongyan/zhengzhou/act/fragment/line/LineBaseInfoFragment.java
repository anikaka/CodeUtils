package com.tongyan.zhengzhou.act.fragment.line;

import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;


import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoAdapter;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoLevel2Adapter;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoLevel3Adapter;
import com.tongyan.zhengzhou.act.adapter.LineBaseInfoLevel4Adapter;
import com.tongyan.zhengzhou.act.line.LineInfoDetailSandSoilLocation;
import com.tongyan.zhengzhou.act.line.LineInfoDetailTunnel;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 基础信息
 * @author ChenLang
 *
 */
public class LineBaseInfoFragment  extends MFinalFragment{

	
	private static LineBaseInfoFragment  sInstance=new LineBaseInfoFragment();	
	private String mNodeCode;
	private int mNodeLevel;
	private Context mContext;
	private ListView mListView;
	private LineBaseInfoAdapter mBaseInfoAdapter;
	private LineBaseInfoLevel2Adapter mBaseInfoLeveL2Adapter;
	private LineBaseInfoLevel3Adapter mBaseInfoLevel3Adapter;
	private LineBaseInfoLevel4Adapter mBaseInfoLevel4Adapter;
	private LinearLayout  mLLLineLevel1;
	private LinearLayout   mLLLineLevel4; // 第四级区间信息
	private LinearLayout    mLLLineLevel5;//第五级信息
	private LinearLayout   mLLLineLevel6; //第六级信息
	
	private ListView  mListViewLevel4;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> mArrayListS;
	private HashMap<String, Object> mMapLineLevel4=new HashMap<String, Object>();
	private HashMap<String, Object> mMapLineLevel4S;
	private TextView  mTxtLineLevel1ProjectName, // 项目名称
								mTxtLineLevel1Carrieroperator; //运营商
 	
	private TextView mTxtLineLevel4SectionName, //标段名称
								mTxtLineLevel4LeftStartMile,  //上行开始里程
								mTxtLineLevel4LeftEndMile,   //上行终止里程
								mTxtLineLevel4RightStartMile,//下行开始里程
								mTxtLineLevel4RightEndMile,  //下行终止里程
								mTxtLineLevel4StartStation,    //开始车站
								mTxtLineLevel4EndStation;     // 结束车站
	
	private TextView mTxtLineLevel5IntervalName,  //区间名称
								mTxtLineLevel5IntervalNumber, //区间数量
								mTxtLineLevel5IntervalCode,    //区间编号
								mTxtLinelevel5ShaftNumber, //区间编号
								mTxtLineLevel5IsImportInterva,  
								mTxtLineLevel5Remark,   //备注
								mTxtLineLeftLevel5StartMileage,  
								mTxtLineLeftLevel5EndMileage,
								mTxtLineLeftLevel5CheckFrequency,
								mTxtLineRightLevel5StartMileage,
								mTxtLineRightLevel5EndMileage,
								mTxtLineRightLevel5CheckFrequency;  
	
	private TextView mTxtLineLevel6StationName, //站名,
								mTxtLineLevel6StationType,  //站点类型
								mTxtLineLevel6IsChangeOthers, //是否可以换乘
								mTxtLineLevel6StationNumber,  //站点序号
								mTxtLineLevel6StationCode,      // 站点编号
								mTxtLineLeftStartMileLevel6,
								mTxtLineLeftEndMileLevel6,
								mTxtLineLeftCheckFrequencyLevel6,
								mTxtLineLeftTunnelInfoLevel6,
								mTxtLineRightStartMileLevel6,
								mTxtLineRightEndMileLevel6,
								mTxtLineRightCheckFrequencyLevel6,
								mTxtLineRightTunnelInfoLevel6;
	
	private LinearLayout mLLLineLeftInfo,mLLLineRightInfo;
	public static LineBaseInfoFragment getInstance(String nodeCode,int nodeLevel){
		sInstance.mNodeCode=nodeCode;
		sInstance.mNodeLevel=nodeLevel;
		return sInstance;
	}  
	
	private void initView(View view){
  		mLLLineLevel1=(LinearLayout)view.findViewById(R.id.llLineLevel1);
  		mTxtLineLevel1ProjectName=(TextView)view.findViewById(R.id.txtLineBaseInfoProjectName);
  		mTxtLineLevel1Carrieroperator=(TextView)view.findViewById(R.id.txtLineBaseInfoCarrieroperator);
		mListView=(ListView)view.findViewById(R.id.listViewLineBaseInfo);
		mLLLineLevel4=(LinearLayout)view.findViewById(R.id.llLineLeve4BaseInfo);
		mTxtLineLevel4SectionName=(TextView)view.findViewById(R.id.txtLineLevel4SectionName);
		mTxtLineLevel4LeftStartMile=(TextView)view.findViewById(R.id.txtLinelevel4LeftStartMile);
		mTxtLineLevel4LeftEndMile=(TextView)view.findViewById(R.id.txtLinelevel4LeftEndMile);
		mTxtLineLevel4RightStartMile=(TextView)view.findViewById(R.id.txtLineLevel4RightStartMile);
		mTxtLineLevel4RightEndMile=(TextView)view.findViewById(R.id.txtLineLevel4RightEndMile);
		mTxtLineLevel4StartStation=(TextView)view.findViewById(R.id.txtLine4StartStation);
		mTxtLineLevel4EndStation=(TextView)view.findViewById(R.id.txtLine4RightEndStation);
		mListViewLevel4=(ListView)view.findViewById(R.id.listViewLineLevel4BaseInfo);
		//
		mLLLineLevel5=(LinearLayout)view.findViewById(R.id.lllineLevel5BaseInfo);
		mTxtLineLevel5IntervalName=(TextView)view.findViewById(R.id.txtLineLevel5IntervalName);
		mTxtLineLevel5IntervalNumber=(TextView)view.findViewById(R.id.txtLineLevel5IntervalNumber);
		mTxtLineLevel5IntervalCode=(TextView)view.findViewById(R.id.txtLineLevel5IntervalCode);
		mTxtLinelevel5ShaftNumber=(TextView)view.findViewById(R.id.txtLinelevel5ShaftNumber);
		mTxtLineLevel5IsImportInterva=(TextView)view.findViewById(R.id.txtLineLevel5IsImportInterva);
		mTxtLineLevel5Remark=(TextView)view.findViewById(R.id.txtLineLevel5Remark);
		mTxtLineLeftLevel5StartMileage=(TextView)view.findViewById(R.id.txtLineLeftLevel5StartMileage);
		mTxtLineLeftLevel5EndMileage=(TextView)view.findViewById(R.id.txtLineLeftLevel5EndMileage);
		mTxtLineLeftLevel5CheckFrequency=(TextView)view.findViewById(R.id.txtLineLeftLevel5CheckFrequency);
		mTxtLineRightLevel5StartMileage=(TextView)view.findViewById(R.id.txtLineRightLevel5StartMileage);
		mTxtLineRightLevel5EndMileage=(TextView)view.findViewById(R.id.txtLineRightLevel5EndMileage);
		mTxtLineRightLevel5CheckFrequency=(TextView)view.findViewById(R.id.txtLineRightLevel5CheckFrequency);
		mLLLineLeftInfo=(LinearLayout)view.findViewById(R.id.llLineLeftInfo);
		mLLLineRightInfo=(LinearLayout)view.findViewById(R.id.llLineRightInfo);
		//
		mLLLineLevel6=(LinearLayout)view.findViewById(R.id.lllineLevel6BaseInfo);
		mTxtLineLevel6StationName=(TextView)view.findViewById(R.id.txtLineLevel6StationName);
		mTxtLineLevel6StationType=(TextView)view.findViewById(R.id.txtLineLevel6StationType);
		mTxtLineLevel6IsChangeOthers=(TextView)view.findViewById(R.id.txtLineLevel6IsChangeOthers);
		mTxtLineLevel6StationNumber=(TextView)view.findViewById(R.id.txtLineLevel6StationNumber);
		mTxtLineLevel6StationCode=(TextView)view.findViewById(R.id.txtLineLevel6StationCode);
		mTxtLineLeftStartMileLevel6=(TextView)view.findViewById(R.id.txtLineLeftStartMileLevel6);
		mTxtLineLeftEndMileLevel6=(TextView)view.findViewById(R.id.txtLineLeftEndMileLevel6);
		mTxtLineLeftCheckFrequencyLevel6=(TextView)view.findViewById(R.id.txtLineLeftCheckFrequencyLevel6);
		mTxtLineLeftTunnelInfoLevel6=(TextView)view.findViewById(R.id.txtLineLeftTunnelInfoLevel6);
		mTxtLineRightStartMileLevel6=(TextView)view.findViewById(R.id.txtLineRightStartMileLevel6);
		mTxtLineRightEndMileLevel6=(TextView)view.findViewById(R.id.txtLineRightEndMileLevel6);
		mTxtLineRightCheckFrequencyLevel6=(TextView)view.findViewById(R.id.txtLineRightCheckFrequencyLevel6);
		mTxtLineRightTunnelInfoLevel6=(TextView)view.findViewById(R.id.txtLineRightTunnelInfoLevel6);
		
		}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  	View view=inflater.inflate(R.layout.line_baseinfo, null, false);
		  	initView(view);
		  	mContext=getActivity();
		  	new Thread(new Runnable(){
				@Override
				public void run() { 
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("metroCode", mNodeCode);
					map.put("metroLevel",  String.valueOf(mNodeLevel==6?7:mNodeLevel));
					String stream = null;
					try {
						stream = WebServiceUtils.requestM(mContext, map, Constants.METHOD_OF_CLIENT_GETINFOBYLINE);
						if(stream!=null){
							if(mNodeLevel==1){
								mArrayListS=new JSONParseUtils().getLineBaseInfoLevel1(stream);
								sendMessage(Constants.MSG_0x2001);
							}
							if(mNodeLevel==2){
								mArrayListS=new JSONParseUtils().getLineBaseInfoLevel2(stream);
								sendMessage(Constants.MSG_0x2002);
							}
							if( mNodeLevel==3){
								mArrayListS=new JSONParseUtils().getLineBaseInfoLevel3(stream);
								sendMessage(Constants.MSG_0x2003);
							}
							if(mNodeLevel==4){
								mMapLineLevel4S=new JSONParseUtils().getLineBaseInfoLeve4(stream);
								sendMessage(Constants.MSG_0x2004);
							}
							if(mNodeLevel==5){
								mMapLineLevel4S=new JSONParseUtils().getLineBaseInfoLevel5(stream);
								sendMessage(Constants.MSG_0x2005);
							}
							if(mNodeLevel==6 || mNodeLevel==7){
								mMapLineLevel4S=new JSONParseUtils().getLineBaseInfoLevel6(stream);
								sendMessage(Constants.MSG_0x2006);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
//					System.out.println(stream);
				}
		  	}).start();
		  	
		  	switch (mNodeLevel) {
			case 1:		//6级，车站级别
				mLLLineLevel1.setVisibility(View.VISIBLE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
		  		mListView.setVisibility(View.GONE);
				break;
			case 2:		//6级，车站级别
				mLLLineLevel1.setVisibility(View.GONE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
		  		mListView.setVisibility(View.VISIBLE);
//		  		if(mArrayList.size()>0){
//		  			mArrayList.clear();
//		  		}
//		  		mArrayList=new DBService(mContext).getLineOneInfo(mNodeCode);
//		  		mBaseInfoAdapter=new LineBaseInfoAdapter(mContext, mArrayList, R.layout.line_baseinfo_item);
//		  		mListView.setAdapter(mBaseInfoAdapter);
		  		mBaseInfoLeveL2Adapter=new LineBaseInfoLevel2Adapter(mContext, mArrayList, R.layout.line_baseinfo_listview_leve2_item);
				mListView.setAdapter(mBaseInfoLeveL2Adapter);
				break;
			case 3:		//6级，车站级别
				mLLLineLevel1.setVisibility(View.GONE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mBaseInfoLevel3Adapter=new LineBaseInfoLevel3Adapter(mContext, mArrayList, R.layout.line_baseinfo_listview_leve3_item);
				mListView.setAdapter(mBaseInfoLevel3Adapter);
				break;
			case 4:
				mLLLineLevel1.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
				break;
			case 5:
				mLLLineLevel1.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
				break;
			case 6:		//6级，车站级别
				mLLLineLevel1.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				mLLLineLevel4.setVisibility(View.GONE);
				mLLLineLevel5.setVisibility(View.GONE);
				mLLLineLevel6.setVisibility(View.GONE);
//		  		new Thread(new Runnable() {
//					@Override
//					public void run() {
//						if(mArrayList.size()>0){
//							mArrayList.clear();
//						}
//						mArrayList=new LineInfoDBService(mContext).getStationInfo(mNodeCode);
//						sendMessage(0x0001);
//					}
//				}).start();
				break;
			default:
				break;
			}
		  	return view;
	}
	
	/**
	 * 初始化第一级数据
	 */
	private void initViewLevel1(ArrayList<HashMap<String, String>> arrayList){
		if(arrayList!=null && arrayList.size()>0){
			HashMap<String, String> map=arrayList.get(0);
			mTxtLineLevel1ProjectName.setText(map.get("projectName"));
			mTxtLineLevel1Carrieroperator.setText(map.get("carrieroperator"));
		}
	}
	
	/**
	 *初始化第四级数据*/
	private void initViewLevel4(HashMap<String, Object>map){
		HashMap<String, String> mapBaseInfo=(HashMap<String, String>)map.get("baseInfo");
		if(mapBaseInfo!=null){
			mTxtLineLevel4SectionName.setText("标段名称:"+mapBaseInfo.get("sectionName"));
			mTxtLineLevel4LeftStartMile.setText("上行起始里程:"+mapBaseInfo.get("leftStartMile"));
			mTxtLineLevel4LeftEndMile.setText("上行终止里程:"+mapBaseInfo.get("leftEndMile"));
			mTxtLineLevel4RightStartMile.setText("下行起始里程:"+mapBaseInfo.get("rightStartMile"));
			mTxtLineLevel4RightEndMile.setText("下行终止里程:"+mapBaseInfo.get("rightEndMile"));
			mTxtLineLevel4StartStation.setText("起始站点:"+mapBaseInfo.get("startStationName"));
			mTxtLineLevel4EndStation.setText("终止站点:"+mapBaseInfo.get("endStationName"));
		}
		mBaseInfoLevel4Adapter=new LineBaseInfoLevel4Adapter(mContext,(ArrayList<HashMap<String,String>>)map.get("intervalList"),R.layout.line_baseinfo_listview_level4_item);
		mListViewLevel4.setAdapter(mBaseInfoLevel4Adapter);
	}
	
	
	/**
	 * 初始化第五级数据*/
	private void initViewLevel5(HashMap<String, Object> map){
		if(map!=null){
			HashMap<String, String> mapBaseInfo=(HashMap<String, String>)map.get("baseInfo");
			mTxtLineLevel5IntervalName.setText("区间名称:"+mapBaseInfo.get("intervalName"));
			mTxtLineLevel5IntervalNumber.setText("区间编号:"+mapBaseInfo.get("intervalNumber"));
			mTxtLineLevel5IntervalCode.setText("区间编码:"+mapBaseInfo.get("intervalCode"));
			mTxtLinelevel5ShaftNumber.setText("风井数量:"+mapBaseInfo.get("shaftNumber"));
			mTxtLineLevel5IsImportInterva.setText("是否重点区间:"+mapBaseInfo.get("isImportInterval"));
			mTxtLineLevel5Remark.setText("备注:"+mapBaseInfo.get("remark"));
			HashMap<String, Object> mapLeftDirBaseInfo=(HashMap<String, Object>)map.get("leftDirection");
			if(mapLeftDirBaseInfo!=null && mapLeftDirBaseInfo.size()>0){
				HashMap<String, String> mapLeftBaseInfo=(HashMap<String, String>)mapLeftDirBaseInfo.get("baseInfo");
				mTxtLineLeftLevel5StartMileage.setText("起始里程:"+mapLeftBaseInfo.get("startMile"));
				mTxtLineLeftLevel5EndMileage.setText("终止里程:"+mapLeftBaseInfo.get("endMile"));
				mTxtLineLeftLevel5CheckFrequency.setText("结构检查频率:"+mapLeftBaseInfo.get("checkFrequency"));
				ArrayList<HashMap<String, Object>>arrayListLeftDirection=(ArrayList<HashMap<String, Object>>)mapLeftDirBaseInfo.get("tunnelList");
				for(int i=0;i<arrayListLeftDirection.size();i++){
					HashMap<String, Object> mapTunnel=arrayListLeftDirection.get(i);
					TextView txt=new TextView(mContext);
					ViewGroup.LayoutParams  lp=new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					txt.setLayoutParams(lp);
					txt.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
					txt.setText(Html.fromHtml("<u>"+((HashMap<String, String>)mapTunnel.get("tunnelName")).get("tunnelName")+"</u>"));
					txt.setTag(mapTunnel);
					txt.setOnClickListener(onClickListener);
					mLLLineLeftInfo.addView(txt);
				}
				// 区间线性
				TextView txtLineLeftIntervalmetroGeome=new TextView(mContext);
				ViewGroup.LayoutParams  lp=new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				txtLineLeftIntervalmetroGeome.setLayoutParams(lp);
				txtLineLeftIntervalmetroGeome.setPadding(0, 10, 0, 0);
				txtLineLeftIntervalmetroGeome.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
				txtLineLeftIntervalmetroGeome.setText(Html.fromHtml("<u>"+"区间线型"+"</u>"));
				txtLineLeftIntervalmetroGeome.setTag(mapLeftDirBaseInfo.get("metroGeome"));
				txtLineLeftIntervalmetroGeome.setOnClickListener(onClickLinenerTxtGeometryLine);
				mLLLineLeftInfo.addView(txtLineLeftIntervalmetroGeome);
				//砂性突然区
				TextView txtLineLeftSandSoilLoactio=new TextView(mContext);
				txtLineLeftSandSoilLoactio.setLayoutParams(lp);
				txtLineLeftSandSoilLoactio.setPadding(0, 10, 0, 0);
				txtLineLeftSandSoilLoactio.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
				txtLineLeftSandSoilLoactio.setText(Html.fromHtml("<u>"+"砂性土壤区"+"</u>"));
				txtLineLeftSandSoilLoactio.setTag(mapLeftDirBaseInfo.get("sandSoilLoaction"));
				txtLineLeftIntervalmetroGeome.setOnClickListener(onClickLinenerTxtSandSoilLoaction);
				mLLLineLeftInfo.addView(txtLineLeftSandSoilLoactio);
				/*下行*/
				HashMap<String, Object> mapRightDirBaseInfo=(HashMap<String, Object>)map.get("leftDirection");
				if(mapLeftDirBaseInfo!=null && mapLeftDirBaseInfo.size()>0){
					HashMap<String, String> mapRightBaseInfo=(HashMap<String, String>)mapRightDirBaseInfo.get("baseInfo");
					mTxtLineRightLevel5StartMileage.setText("起始里程:"+mapLeftBaseInfo.get("startMile"));
					mTxtLineRightLevel5EndMileage.setText("终止里程:"+mapLeftBaseInfo.get("endMile"));
					mTxtLineRightLevel5CheckFrequency.setText("结构检查频率:"+mapLeftBaseInfo.get("checkFrequency"));
					ArrayList<HashMap<String, Object>>arrayListRightDirection=(ArrayList<HashMap<String, Object>>)mapLeftDirBaseInfo.get("tunnelList");
					for(int i=0;i<arrayListRightDirection.size();i++){
						HashMap<String, Object> mapTunnel=arrayListRightDirection.get(i);
						TextView txt=new TextView(mContext);
						txt.setLayoutParams(lp);
						txt.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
						txt.setText(Html.fromHtml("<u>"+((HashMap<String, String>)mapTunnel.get("tunnelName")).get("tunnelName")+"</u>"));
						txt.setTag(mapTunnel);
						txt.setOnClickListener(onClickListener);
						mLLLineRightInfo.addView(txt);
					}
					// 区间线性
					TextView txtLineRightIntervalmetroGeome=new TextView(mContext);
					txtLineRightIntervalmetroGeome.setLayoutParams(lp);
					txtLineRightIntervalmetroGeome.setPadding(0, 10, 0, 0);
					txtLineRightIntervalmetroGeome.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
					txtLineRightIntervalmetroGeome.setText(Html.fromHtml("<u>"+"区间线型"+"</u>"));
					txtLineRightIntervalmetroGeome.setTag(mapRightDirBaseInfo.get("metroGeome"));
					txtLineRightIntervalmetroGeome.setOnClickListener(onClickLinenerTxtGeometryLine);
					mLLLineRightInfo.addView(txtLineRightIntervalmetroGeome);
					//砂性突然区
					TextView txtLineRightSandSoilLoactio=new TextView(mContext);
					txtLineRightSandSoilLoactio.setLayoutParams(lp);
					txtLineRightSandSoilLoactio.setPadding(0, 10, 0, 0);
					txtLineRightSandSoilLoactio.setTextAppearance(getActivity(), R.style.protectProBaseInfoContent);
					txtLineRightSandSoilLoactio.setText(Html.fromHtml("<u>"+"砂性土壤区"+"</u>"));
					txtLineRightSandSoilLoactio.setTag(mapRightDirBaseInfo.get("sandSoilLoaction"));
					txtLineLeftIntervalmetroGeome.setOnClickListener(onClickLinenerTxtSandSoilLoaction);
					mLLLineRightInfo.addView(txtLineRightSandSoilLoactio);
				}
			}
		}
	}
	
	/**
	 * 初始化第六级视图
	 * @param map
	 */
	private void    initViewLevel6(HashMap<String, Object> map){
		if(map!=null){
			HashMap<String, String> mapBaseInfo=(HashMap<String, String>)map.get("baseInfo");
			mTxtLineLevel6StationName.setText("站点名称:"+mapBaseInfo.get("stationName"));
			mTxtLineLevel6StationType.setText("站点类型:"+mapBaseInfo.get("stationType"));
			mTxtLineLevel6IsChangeOthers.setText("可否换乘:"+mapBaseInfo.get("isChangeOthers"));
			mTxtLineLevel6StationNumber.setText("站点序号:"+mapBaseInfo.get("stationNumber"));
			mTxtLineLevel6StationCode.setText("站点编号:"+mapBaseInfo.get("stationCode"));
			HashMap<String, Object> mapLeftDirection=(HashMap<String, Object>)map.get("leftDirection");
			if(mapLeftDirection!=null && mapLeftDirection.size()>0){
				HashMap<String, String> mapLeftDirectionBaseInfo=(HashMap<String, String>)mapLeftDirection.get("baseInfo");
				mTxtLineLeftStartMileLevel6.setText("起始里程:"+mapLeftDirectionBaseInfo.get("startMile"));
				mTxtLineLeftEndMileLevel6.setText("终止里程:"+mapLeftDirectionBaseInfo.get("endMile"));
				mTxtLineLeftCheckFrequencyLevel6.setText("检查结构频率:"+mapLeftDirectionBaseInfo.get("checkFrequency"));
				mTxtLineLeftTunnelInfoLevel6.setText(Html.fromHtml("<u>"+"隧道附加信息"+"</u>"));
				mTxtLineLeftTunnelInfoLevel6.setTag(mapLeftDirection.get("tunnelList"));
				mTxtLineLeftTunnelInfoLevel6.setOnClickListener(onClickLinenerTxtSandSoilLoaction);
			}
			
			HashMap<String, Object> mapRightDirection=(HashMap<String, Object>)map.get("rightDirection");
			if(mapLeftDirection!=null && mapRightDirection.size()>0){
				HashMap<String, String> mapRightDirectionBaseInfo=(HashMap<String, String>)mapRightDirection.get("baseInfo");
				mTxtLineRightStartMileLevel6.setText("起始里程:"+mapRightDirectionBaseInfo.get("startMile"));
				mTxtLineRightEndMileLevel6.setText("终止里程:"+mapRightDirectionBaseInfo.get("endMile"));
				mTxtLineRightCheckFrequencyLevel6.setText("检查结构频率:"+mapRightDirectionBaseInfo.get("checkFrequency"));
				mTxtLineRightTunnelInfoLevel6.setText(Html.fromHtml("<u>"+"隧道附加信息"+"</u>"));
				mTxtLineRightTunnelInfoLevel6.setTag(mapLeftDirection.get("tunnelList"));
				mTxtLineRightTunnelInfoLevel6.setOnClickListener(onClickLinenerTxtSandSoilLoaction);
			}
		}
		
	}
	
	private OnClickListener  onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			HashMap<String, Object> map=(HashMap<String, Object>)v.getTag();
			Intent intent=new Intent(mContext, LineInfoDetailTunnel.class);
			intent.putExtra("baseInfo", map);
			startActivity(intent);
		}
	};
	
	
	/**
	 * 区间线性点击事件*/
	private OnClickListener onClickLinenerTxtGeometryLine=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	}; 

	
	/**
	 *砂性土壤区点击事件 */
	private OnClickListener onClickLinenerTxtSandSoilLoaction=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 Intent intent=new Intent(mContext, LineInfoDetailSandSoilLocation.class);
			 intent.putExtra("tunnleInfo",(ArrayList<HashMap<String, String>>)v.getTag());
			 startActivity(intent);
		}
	}; 
	
	
	/**
	 * 数据刷新
	 */
	private void modifyLineInfo(){
		if(mArrayListS!=null){
			if(mArrayList.size()>0){
				mArrayList.clear();
			}
			mArrayList.addAll(mArrayListS);
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch (flag) {
		case 0x0001:
			mBaseInfoAdapter=new LineBaseInfoAdapter(mContext, mArrayList, R.layout.line_baseinfo_item);
	  		mListView.setAdapter(mBaseInfoAdapter);
			break;
		case Constants.MSG_0x2001:
			if(mArrayList.size()>0){	
				modifyLineInfo();
				initViewLevel1(mArrayList);
			}
			break;
		case Constants.MSG_0x2002:
			modifyLineInfo();
			mBaseInfoLeveL2Adapter.notifyDataSetChanged();
			break;
		case Constants.MSG_0x2003: //线路第三级信息
			modifyLineInfo();
			mBaseInfoLevel3Adapter.notifyDataSetChanged();
			break;
		case Constants.MSG_0x2004:
			if(mMapLineLevel4S.size()>0){	
				mLLLineLevel4.setVisibility(View.VISIBLE);
				mMapLineLevel4.putAll(mMapLineLevel4S);
				initViewLevel4(mMapLineLevel4);
			}
			break;
		case Constants.MSG_0x2005:
			if(mMapLineLevel4S.size()>0){				
				mLLLineLevel5.setVisibility(View.VISIBLE);
				mMapLineLevel4.putAll(mMapLineLevel4S);
				initViewLevel5(mMapLineLevel4);
			}
			break;
		case Constants.MSG_0x2006:
			if(mMapLineLevel4S.size()>0){				
				mLLLineLevel6.setVisibility(View.VISIBLE);
				mMapLineLevel4.putAll(mMapLineLevel4S);
				initViewLevel6(mMapLineLevel4);
			}
			break;
		default:
			break;
		}
	}
}
