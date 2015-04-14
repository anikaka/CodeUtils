package com.tongyan.zhengzhou.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tongyan.zhengzhou.common.afinal.MFinalService;
import com.tongyan.zhengzhou.common.db.DBService;
import com.tongyan.zhengzhou.common.db.WebServiceDBService;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @Title: BaseDataService.java
 * @author Rubert
 * @date 2014-11-27 AM　10:17:40
 * @version V2.0z
 * @Description 用于基础数据的更新
 */
public class BaseDataService extends MFinalService {

	private Context mContext = this;
	private JSONParseUtils mJsonUtils = new JSONParseUtils();
	private String updateTime = "";
	private String damageUpdateTime = "";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			updateTime = intent.getStringExtra("updateTime");
			damageUpdateTime = intent.getStringExtra("damageUpdateTime");
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				updateDamageTypeInfo();
				noticeDownNew();
			}
		}).start();
		return Service.START_REDELIVER_INTENT;
	}

	private String mUpdateLineName = "";

	public synchronized void noticeDownNew() {
		HashMap<String, String>  mMetroLineMap=new HashMap<String, String>();
		mMetroLineMap.put("Type", "MetroLine");
//		mMetroLineMap.put("UpdateTime", "2000-01-01 00:00:00");
		mMetroLineMap.put("UpdateTime", updateTime);
		String jsonStr="";
		try {
			jsonStr = WebServiceUtils.requestM(mContext, mMetroLineMap, Constants.METHOD_OF_CLIENT_LINEINFO);
			HashMap<String, Object> mBaseMap = mJsonUtils.getMetroLineList(jsonStr);
			if(mBaseMap != null){
				new WebServiceDBService(mContext).setUpdateTime("LineInfo", (String) mBaseMap.get("UpdateTime"));
				List<HashMap<String, String>> list = (List<HashMap<String, String>>) mBaseMap.get("MetroLineList");
				if(list != null && list.size() > 0) {
					for(HashMap<String, String> s : list) {
						if(s != null) {
							HashMap<String, String>  map0 = new HashMap<String, String>();
							map0.put("LineIds", s.get("MetroLineId"));
							mUpdateLineName = s.get("MetroLineName");
							sendMessage(Constants.MSG_0x2001);
							String jsonStr0 = WebServiceUtils.requestM(mContext, map0, Constants.METHOD_OF_CLIENT_GETBASEINFOBYLINEIDS);
							HashMap<String, Object> infoMap = mJsonUtils.analysisBaseJson(jsonStr0);
							if(infoMap != null) {
								ArrayList<HashMap<String, String>> linlist = (ArrayList<HashMap<String, String>>)infoMap.get("lineList");
								ArrayList<HashMap<String, String>> objectlist = (ArrayList<HashMap<String, String>>)infoMap.get("checkObjectList");
								ArrayList<HashMap<String, String>> sheildInfolist = (ArrayList<HashMap<String, String>>)infoMap.get("shiledInfoList");
								ArrayList<HashMap<String, String>> checkObjectDetailList = (ArrayList<HashMap<String, String>>)infoMap.get("checkObjectDetailList");
								ArrayList<HashMap<String, String>> FacilityList = (ArrayList<HashMap<String, String>>)infoMap.get("FacilityList");
								//失败需要回滚//TODO
								new DBService(mContext).saveLinInfo(linlist);
								new DBService(mContext).saveCheckObjectInfo(objectlist);
								new DBService(mContext).saveCheckObjectDetailInfo(checkObjectDetailList);
								new DBService(mContext).saveFacilityInfo(FacilityList);
								new DBService(mContext).saveShieldInfo(sheildInfolist);
								//通过循环线路，然后获取线路的编号，把线路编号传入各个接口中(红线、车站、隧道)
								if(linlist != null){
									insertCheckData(linlist);
								}
								linlist.clear();
								linlist = null;
								objectlist.clear();
								objectlist = null;
								checkObjectDetailList.clear();
								checkObjectDetailList = null;
								FacilityList.clear();
								FacilityList = null;
								sheildInfolist.clear();
								sheildInfolist = null;
							}
						}
					}
					sendMessage(Constants.MSG_0x2002);
				}else{
					sendMessage(Constants.MSG_0x2003);
				}
			}else{
				sendMessage(Constants.MSG_0x2003);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 3.下载病害类型的配置信息以及其他辅助信息
	 * 更新表DamageType，DamgeLevel，DamageInfoFieldRel，CheckTypeDamageRel
	 * ，DamageInputOpreation，z_ConstructionCheckDic,FacilityConstraint
	 * (重庆需要对解析方法微调！)
	 */
	public synchronized void updateDamageTypeInfo() {
		HashMap<String, String> mMetroLineMap = new HashMap<String, String>();

		// mMetroLineMap.put("UpdateTime", new
		// WebServiceDBService(mContext).getUpdateTime(Constants.METHOD_OF_DAMAGETYPEINFO));
		mMetroLineMap.put("UpdateTime", damageUpdateTime);
		mMetroLineMap.put("RecordWay", "0");
		String jsonStr = "";
		try {
			jsonStr = WebServiceUtils.requestM(mContext, mMetroLineMap,Constants.METHOD_OF_DAMAGETYPEINFO);
			HashMap<String, Object> infoMap = mJsonUtils.parseDamageTypeInfo(jsonStr);
			if (infoMap == null) {
				sendMessage(Constants.MSG_0x2003);
				return;
			}
			sendMessage(Constants.MSG_0x2008);
			// List<HashMap<String, String>> list = (List<HashMap<String,
			// String>>) metroLineMap.get("MetroLines");
			if (infoMap != null) {
				String upDateTime = (String) infoMap.get("UpdateTime");
				new WebServiceDBService(mContext).setUpdateTime(Constants.METHOD_OF_DAMAGETYPEINFO, upDateTime);
				ArrayList<HashMap<String, String>> z_ConstructionCheckDicList = (ArrayList<HashMap<String, String>>) infoMap.get("z_ConstructionCheckDicList");

				if (z_ConstructionCheckDicList != null) { // 更新表z_ConstructionCheckDic
					new WebServiceDBService(mContext).clearDBTable("z_ConstructionCheckDic");
					new WebServiceDBService(mContext).saveDBInfo("z_ConstructionCheckDic",z_ConstructionCheckDicList);
					z_ConstructionCheckDicList.clear();
					z_ConstructionCheckDicList = null;
				}else{
					sendMessage(Constants.MSG_0x2003);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将红线、车站、隧道的数据插入数据库
	 * 
	 * @param list
	 */
	private void insertCheckData(ArrayList<HashMap<String, String>> list) {
		if (list != null && list.size() > 0) {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					// 车站
					HashMap<String, String> StationBaseInfoMap = new HashMap<String, String>();
					StationBaseInfoMap.put("Type", "Station");
					StationBaseInfoMap.put("MetroLineID",
							map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext,
								StationBaseInfoMap,
								Constants.METHOD_OF_CLIENT_STATIONINFO);
						HashMap<String, Object> mR = new JSONParseUtils()
								.getBaseInfoStation(mBackJson);
						if (mR != null) {
							String mS = (String) mR.get("s");
							if ("ok".equalsIgnoreCase(mS)) {
								ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>) mR
										.get("v");
								if (mBaseInfoList != null
										&& mBaseInfoList.size() > 0) {
									for (HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext)
												.insertStationBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 红线
					HashMap<String, String> RedLineBaseInfoMap = new HashMap<String, String>();
					RedLineBaseInfoMap.put("Type", "RedLine");
					RedLineBaseInfoMap.put("MetroLineID",
							map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext,
								RedLineBaseInfoMap,
								Constants.METHOD_OF_CLIENT_REDLINE);
						HashMap<String, Object> mR = new JSONParseUtils()
								.getBaseInfoRedLine(mBackJson);
						if (mR != null) {
							ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>) mR
									.get("v");
							String mS = (String) mR.get("s");
							if ("ok".equalsIgnoreCase(mS)) {
								if (mBaseInfoList != null
										&& mBaseInfoList.size() > 0) {
									for (HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext)
												.insertRedLineBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 隧道
					HashMap<String, String> TunnelBaseInfoMap = new HashMap<String, String>();
					TunnelBaseInfoMap.put("Type", "Tunnel");
					TunnelBaseInfoMap
							.put("MetroLineID", map.get("MetroLineId"));
					try {
						String mBackJson = WebServiceUtils.requestM(mContext,
								TunnelBaseInfoMap,
								Constants.METHOD_OF_CLIENT_TUNNELINFO);
						HashMap<String, Object> mR = new JSONParseUtils()
								.getBaseInfoTunnel(mBackJson);
						if (mR != null) {
							ArrayList<HashMap<String, String>> mBaseInfoList = (ArrayList<HashMap<String, String>>) mR
									.get("v");
							String mS = (String) mR.get("s");
							if ("ok".equalsIgnoreCase(mS)) {
								if (mBaseInfoList != null
										&& mBaseInfoList.size() > 0) {
									for (HashMap<String, String> mBaseInfoMap : mBaseInfoList) {
										new DBService(mContext)
												.insertTunnelBaseInfo(mBaseInfoMap);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.MSG_0x2001:
			Toast.makeText(mContext,
					String.format("正在同步%s数据", mUpdateLineName),
					Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2002:
			Toast.makeText(mContext, "同步完毕", Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2008:
			Toast.makeText(mContext, "字典表下载", Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2003:
			Toast.makeText(mContext, "暂无更新数据", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
