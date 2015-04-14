package com.tongyan.zhengzhou.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tongyan.zhengzhou.common.widgets.charting.data.Entry;

/**
 * 
 * @Title: JSONParseUtils.java 
 * @author Rubert
 * @date 2015-3-11 PM 03:39:31 
 * @version V1.0 
 * @Description: TODO
 */
public class JSONParseUtils {
	
	/**
	 * 
	 * @param jsonStr  JSONString
	 * @param mType    xxxxxResult
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getHandlerJSON(String jsonStr,String mType) throws JSONException {
		jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}")+1);
		jsonStr = jsonStr.replaceAll(mType + "=", mType + ":");
		jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}")-2) + "}";
		return new JSONObject(jsonStr).getJSONObject(mType);//进行数据处理
	}
	
	/**
	 * {"MetroLineCode":"11","MetroLineName":"11号线主线","MetroLineId":99,"ParentId":98},
	 * @param jsonStr
	 * @returnLineInfoResponse{"UpdateTime":"2015-03-11 13:31:39.547","MetroLineList":[{"MetroLineCode":"01","ParentId":105,"MetroLineName":"测试线路主线","MetroLineId":106}]}
	 * @throws JSONException
	 */
	public HashMap<String, Object> getMetroLineList(String jsonStr) throws JSONException {
		HashMap<String, Object> mContentMap = new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_LineInfoResult");
		String succStr = jsonObj.getString("s");
		HashMap<String, Object> mR = new HashMap<String, Object>();
		mR.put("s", succStr);
		List<HashMap<String, String>> list = null;
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonV = jsonObj.getJSONObject("v");
			if(jsonV != null) {
				mContentMap.put("UpdateTime", jsonV.getString("UpdateTime"));
				JSONArray array = jsonV.getJSONArray("MetroLineList");
				if(array != null && array.length() > 0) {
					list = new ArrayList<HashMap<String, String>>();
					for(int i = 0,len = array.length(); i < len; i ++) {
						JSONObject mObj = array.getJSONObject(i);
						if(mObj != null) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("MetroLineId", mObj.getString("MetroLineId"));
							map.put("ParentId", mObj.getString("ParentId"));
							map.put("MetroLineCode", mObj.getString("MetroLineCode"));
							map.put("MetroLineName", mObj.getString("MetroLineName"));
							list.add(map);
						}
					}
					mContentMap.put("MetroLineList", list);
				}
			}
		}
		return mContentMap;
	}
	
	
	/**
	 * Client_LoginResponse{Client_LoginResult={"s":"Ok","v":{"empId":1,"empAccount":"root","empName":"root","empPassword":"123"}}; }
	 * @param jsonStr
	 * @return
	 */
	public HashMap<String, String> getLoginInfo(String jsonStr){
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_LoginResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
			JSONObject jsonLogin=new JSONObject(jsonObj.getString("v"));
			map.put("empId", jsonLogin.getString("empId"));
			map.put("loginAccount", jsonLogin.getString("empAccount"));
			map.put("userName", jsonLogin.getString("empName"));
			map.put("password", jsonLogin.getString("empPassword"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}// 进行数据处理
		
		return map;
	}
	
	
	/**
	 *基础信息解析
	 * @throws JSONException 
	 */
	public HashMap<String, Object> analysisBaseJson(String jsonStr ) throws JSONException{
		HashMap<String, Object> baseinfo=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetBaseInfoByLineIdsResult");//BaseInfoResult
		ArrayList<HashMap<String, String>> lineList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> FacilityList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> checkObjectList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> checkObjectDetailList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> shiledInfoList=new ArrayList<HashMap<String,String>>();
		String succStr = jsonObj.getString("s");
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONArray  detaillist=new JSONArray(jsonObj.getString("v"));
			if(detaillist!= null) {
				int len =detaillist.length();
				if(len > 0) {
					//ArrayList<HashMap<String, Object>> mBaseInfoList = new ArrayList<HashMap<String, Object>>();
					for(int i = 0; i < len; i ++) {
						//HashMap<String, Object> mBaseInfo=new HashMap<String, Object>();
						HashMap<String, String> LinMap=new HashMap<String, String>();
						JSONObject jsonBaseInfo= (JSONObject)detaillist.get(i);
						LinMap.put("MetroLineCode", jsonBaseInfo.get("MetroLineCode").toString());
						LinMap.put("MetroLineName", jsonBaseInfo.get("MetroLineName").toString());
						LinMap.put("MetroLineId", jsonBaseInfo.get("MetroLineId").toString());
						LinMap.put("PMetroLineId", jsonBaseInfo.get("ParentId").toString());
						lineList.add(LinMap);
						JSONArray JsCheckObjectList=jsonBaseInfo.getJSONArray("CheckObjectList");
						if (JsCheckObjectList.length()>0) {
						for (int j = 0; j < JsCheckObjectList.length(); j++) {
							JSONObject jsonCheckObjectInfo= (JSONObject)JsCheckObjectList.get(j);
							HashMap<String, String> checkObjectMap=new HashMap<String, String>();
							checkObjectMap.put("CheckObjectCode", jsonCheckObjectInfo.getString("CheckObjectCode"));
							checkObjectMap.put("CheckObjectName", jsonCheckObjectInfo.getString("CheckObjectName"));
							checkObjectMap.put("CheckObjectType", jsonCheckObjectInfo.getString("CheckObjectType"));
							checkObjectMap.put("MetroLineId",String.valueOf(jsonBaseInfo.get("MetroLineId")));			
							checkObjectMap.put("SerialCode", jsonCheckObjectInfo.getString("SerialCode"));
							checkObjectMap.put("TermPartId", jsonCheckObjectInfo.getString("TermPartId"));//期段id add by Rubert 2015-03
							checkObjectMap.put("StartStationCode", jsonCheckObjectInfo.getString("StartStationCode"));//前站编号 add by Rubert 2015-04-07
							checkObjectMap.put("EndStationCode", jsonCheckObjectInfo.getString("EndStationCode"));//后站编号           add by Rubert  2015-04-07 
							checkObjectList.add(checkObjectMap);
							JSONArray JsCheckObjectDetaiList = jsonCheckObjectInfo.getJSONArray("CheckObjectChildDetailList");
							if (JsCheckObjectDetaiList.length()>0) {
							for (int k = 0; k < JsCheckObjectDetaiList.length(); k++) {
								JSONObject jsonCheckObjectDetailInfo= (JSONObject)JsCheckObjectDetaiList.get(k);
								HashMap<String, String> CheckObjectDetailMap=new HashMap<String, String>();
								CheckObjectDetailMap.put("CheckObjectCode", jsonCheckObjectInfo.getString("CheckObjectCode"));
								CheckObjectDetailMap.put("CheckObjectDetailCode", jsonCheckObjectDetailInfo.getString("CheckObjectDetailCode"));
								CheckObjectDetailMap.put("LineDirection", jsonCheckObjectDetailInfo.getString("LineDirection"));
								CheckObjectDetailMap.put("StartMileage", jsonCheckObjectDetailInfo.getString("StartMileage"));
								CheckObjectDetailMap.put("EndMileage", jsonCheckObjectDetailInfo.getString("EndMileage"));
								CheckObjectDetailMap.put("MileageAddDirection", jsonCheckObjectDetailInfo.getString("MileageAddDirection"));
								CheckObjectDetailMap.put("ArenosolArea", jsonCheckObjectDetailInfo.getString("ArenosolArea"));
								CheckObjectDetailMap.put("OverproofAre", jsonCheckObjectDetailInfo.getString("OverproofAre"));
								CheckObjectDetailMap.put("DamagedArea", jsonCheckObjectDetailInfo.getString("DamagedArea"));
								CheckObjectDetailMap.put("CheckObjectDetailType", jsonCheckObjectDetailInfo.getString("CheckObjectDetailType"));
								CheckObjectDetailMap.put("UpdateTime", jsonCheckObjectDetailInfo.getString("UpdateTime"));
								checkObjectDetailList.add(CheckObjectDetailMap);
								
								JSONArray JsFacilityList=jsonCheckObjectDetailInfo.getJSONArray("FacilityList");
								if (JsFacilityList.length()>0) {
								for (int l = 0; l < JsFacilityList.length(); l++) {
									JSONObject jsonFacilityInfo= (JSONObject)JsFacilityList.get(l);
									HashMap<String, String> CheckObjectFacilityMap=new HashMap<String, String>();
									CheckObjectFacilityMap.put("CheckObjectDetailCode", jsonCheckObjectDetailInfo.getString("CheckObjectDetailCode"));
									CheckObjectFacilityMap.put("FacilityCode", jsonFacilityInfo.getString("FacilityCode"));
									CheckObjectFacilityMap.put("FacilityName", jsonFacilityInfo.getString("FacilityName"));
									CheckObjectFacilityMap.put("FacilityTypeCode", jsonFacilityInfo.getString("FacilityTypeCode"));
									CheckObjectFacilityMap.put("PFacilityCode", jsonFacilityInfo.getString("PFacilityCode"));
									CheckObjectFacilityMap.put("StartMileage", jsonFacilityInfo.getString("StartMileage"));
									CheckObjectFacilityMap.put("EndMileage", jsonFacilityInfo.getString("EndMileage"));
									CheckObjectFacilityMap.put("StartCircle", jsonFacilityInfo.getString("StartCircle"));
									CheckObjectFacilityMap.put("EndCircle", jsonFacilityInfo.getString("EndCircle"));
									CheckObjectFacilityMap.put("SerialNumber", jsonFacilityInfo.getString("SerialNumber"));
									FacilityList.add(CheckObjectFacilityMap);
									
									//CircleWidth":0,"HoopBolt":0,"SegmentType":0,"FirstStationCode"
									//:0,"TunnelDiameter":0,"EndwiseBolt":0,"SegmentsDirection":0
									JSONObject jsonShiledInfo=jsonFacilityInfo.getJSONObject("ShieldInfo");
									if (jsonShiledInfo.length()>0) {
//											shiledInfoList
											HashMap<String, String> CheckObjectShiledMap=new HashMap<String, String>();
											if (!jsonShiledInfo.getString("CircleWidth").equals("0")) {//&&!jsonShiledInfo.getString("TunnelDiameter").equals("0")
											CheckObjectShiledMap.put("FacilityCode", jsonFacilityInfo.getString("FacilityCode"));
											CheckObjectShiledMap.put("SegmentType", jsonShiledInfo.getString("SegmentType"));
											CheckObjectShiledMap.put("CircleWidth", jsonShiledInfo.getString("CircleWidth"));
											CheckObjectShiledMap.put("TunnelDiameter", jsonShiledInfo.getString("TunnelDiameter"));
											CheckObjectShiledMap.put("HoopBolt", jsonShiledInfo.getString("HoopBolt"));
											CheckObjectShiledMap.put("EndwiseBolt", jsonShiledInfo.getString("EndwiseBolt"));
											CheckObjectShiledMap.put("FirstStationCode", jsonShiledInfo.getString("FirstStationCode"));
											CheckObjectShiledMap.put("SegmentsDirection", jsonShiledInfo.getString("SegmentsDirection"));
//											CheckObjectShiledMap.put("EndwiseBolt", jsonShiledInfo.getString("EndwiseBolt"));
											shiledInfoList.add(CheckObjectShiledMap);
											}
										}
									}
							}}
						}}
						}
						}
				}
			}
		}
		baseinfo.put("lineList", lineList);
		baseinfo.put("FacilityList", FacilityList);
		baseinfo.put("checkObjectList", checkObjectList);
		baseinfo.put("checkObjectDetailList", checkObjectDetailList);
		baseinfo.put("shiledInfoList", shiledInfoList);
		return baseinfo; 
	}
	
	/**
	 * 安保区监护工程信息获取
	 * return {@link HashMap}
	 */
	public ArrayList<HashMap<String,Object>> getProtectProInfo(String jsonStr){	
		ArrayList<HashMap<String,Object>> mBackMapList = new ArrayList<HashMap<String,Object>>();
		try {
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_ProtectProjectInfoListResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject json=new JSONObject(jsonObj.getString("v"));
				JSONArray jsonArrayBaseInfo=new JSONArray(json.getString("List"));
				 for(int i=0;i<jsonArrayBaseInfo.length();i++){
					 HashMap<String, Object> mContainerMap = new HashMap<String, Object>();
					 JSONObject  jsonBaseInfo=jsonArrayBaseInfo.getJSONObject(i);
					 HashMap<String, String>  map=new HashMap<String, String>();
					 map.put("projectId", jsonBaseInfo.getString("ProjectID")); //项目id
					 map.put("projectName", jsonBaseInfo.getString("ProjectName")); //项目名称
					 map.put("projectClass", jsonBaseInfo.getString("ProjectClass"));  //监护项目等级
					 map.put("foundationPitClass", jsonBaseInfo.getString("FoundationPitClass")); //基坑等级
					 map.put("redLineDistance", jsonBaseInfo.getString("RedLineDistance")); //红线边线距离
					 map.put("shoringType", jsonBaseInfo.getString("ShoringType"));//支护结构
					 map.put("structureWater", jsonBaseInfo.getString("StructureWater"));//结构防水形式
					 map.put("digArea", jsonBaseInfo.getString("DigArea")); //开挖面积
					 map.put("digDepth", jsonBaseInfo.getString("DigDepth"));// 开挖深度
					 map.put("x", jsonBaseInfo.getString("X"));//经度
					 map.put("y", jsonBaseInfo.getString("Y"));//纬度
					 map.put("projectState", jsonBaseInfo.getString("ProjectState")); //工程状态
					 map.put("constructionCompany", jsonBaseInfo.getString("ConstructionCompany")); //建设单位
					 map.put("constructionBy", jsonBaseInfo.getString("ConstructionBy")); //施工单位
					 map.put("detectionCompany", jsonBaseInfo.getString("DetectionCompany")); //检测单位
					 map.put("supervisorCompany", jsonBaseInfo.getString("SupervisorCompany"));//监理单位
					 map.put("designCompany", jsonBaseInfo.getString("DesignCompany"));//设计单位
					 map.put("surveyCompany", jsonBaseInfo.getString("SurveyCompany"));//勘察单位
					 map.put("startTime", jsonBaseInfo.getString("StartTime")); //开工日期
					 map.put("endTime", jsonBaseInfo.getString("EndTime")); // 竣工日期
//				     map.put("createTime", jsonBaseInfo.getString("CreateTime")); //创建时间
				     map.put("UpdateTime", jsonBaseInfo.getString("UpdateTime")); //更新时间 
				     map.put("projectAddress", jsonBaseInfo.getString("ProjectAddress"));//工程地址 
				     map.put("IsWaring", jsonBaseInfo.getString("IsWaring"));
				     mContainerMap.put("BaseInfo", map);
				 //设计文件
				 JSONArray  jsonArrayDesignFiles=new JSONArray(jsonBaseInfo.getString("DesignFiles"));
				 ArrayList<HashMap<String, String>> mDesignFiles = new ArrayList<HashMap<String, String>>();
				 for(int j=0;j<jsonArrayDesignFiles.length();j++){
					 JSONObject  jsonDesignFiles=jsonArrayDesignFiles.getJSONObject(j);
					 HashMap<String, String> mapDesignFiles=new HashMap<String, String>();
					 mapDesignFiles.put("fileName", jsonDesignFiles.getString("FileName"));
					 mapDesignFiles.put("filePath", jsonDesignFiles.getString("FilePath"));
					 mDesignFiles.add(mapDesignFiles);
				 }
				 mContainerMap.put("DesignFiles", mDesignFiles);
				 //勘察文件
				 JSONArray jsonArrReconnanceFiles=new JSONArray(jsonBaseInfo.getString("ReconnanceFiles"));
				 ArrayList<HashMap<String, String>> mReconnanceFiles = new ArrayList<HashMap<String, String>>();
				 for(int k=0;k<jsonArrReconnanceFiles.length();k++){
					 JSONObject jsonReconnaceFiles=jsonArrReconnanceFiles.getJSONObject(k);
					 HashMap<String, String>  mapImplementFiles=new HashMap<String, String>();
					 mapImplementFiles.put("fileName", jsonReconnaceFiles.getString("FileName"));
					 mapImplementFiles.put("filePath", jsonReconnaceFiles.getString("FilePath"));
					 mReconnanceFiles.add(mapImplementFiles);
				 }
				 mContainerMap.put("ReconnanceFiles", mReconnanceFiles);
				 //实施文件
				 JSONArray jsonArrImplementFiles=new JSONArray(jsonBaseInfo.getString("ImplementFiles"));
				 ArrayList<HashMap<String, String>> mImplementFiles = new ArrayList<HashMap<String, String>>();
				 for(int j=0;j<jsonArrImplementFiles.length();j++){
					 JSONObject jsonImplementFile=jsonArrImplementFiles.getJSONObject(j);
					 HashMap<String, String> mapImplementFile=new HashMap<String, String>();
					 mapImplementFile.put("fileName", jsonImplementFile.getString("FileName"));
					 mapImplementFile.put("filePath", jsonImplementFile.getString("FilePath"));
					 mImplementFiles.add(mapImplementFile);
				 	}
				 mContainerMap.put("ImplementFiles", mImplementFiles);
				 //照片文件
				 JSONArray  jsonArrPhotoFiles=new JSONArray(jsonBaseInfo.getString("PhotoFiles"));
				 ArrayList<HashMap<String, String>> mPhotoFiles = new ArrayList<HashMap<String, String>>();
				 for(int z=0;z<jsonArrPhotoFiles.length();z++){
					 JSONObject JsonPhotoFiles=jsonArrPhotoFiles.getJSONObject(z);
						HashMap<String, String> mapPhotoFiles=new HashMap<String, String>();
						mapPhotoFiles.put("fileName", JsonPhotoFiles.getString("FileName"));
						mapPhotoFiles.put("filePath", JsonPhotoFiles.getString("FilePath"));
						mPhotoFiles.add(mapPhotoFiles);
				  }
				 mContainerMap.put("PhotoFiles", mPhotoFiles);
				 mBackMapList.add(mContainerMap);
				 }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mBackMapList;
 	}
	/**
	 * 违规建筑信息获取
	 * @param jsonStr
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> getIllegalProInfo(String jsonStr){
		ArrayList<HashMap<String,Object>> mBackMapList = new ArrayList<HashMap<String,Object>>();
		try{
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetDangerProjectListResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONArray jsonArrayIllegalInfo = jsonObj.getJSONArray("v");
				if(jsonArrayIllegalInfo != null && jsonArrayIllegalInfo.length() > 0){
					for(int i=0;i<jsonArrayIllegalInfo.length();i++){
						HashMap<String, Object> mContainerMap = new HashMap<String, Object>();
						JSONObject  jsonIllegalInfo = jsonArrayIllegalInfo.getJSONObject(i);
						HashMap<String, String>  map=new HashMap<String, String>();
						map.put("IllegalID", jsonIllegalInfo.getString("IllegalID"));
						map.put("TaskID", jsonIllegalInfo.getString("TaskID"));
						map.put("MetroLineID", jsonIllegalInfo.getString("MetroLineID"));
						map.put("IllegalName", jsonIllegalInfo.getString("IllegalName"));
						map.put("SectionName", jsonIllegalInfo.getString("SectionName"));
						map.put("Area", jsonIllegalInfo.getString("Area"));
						map.put("Distance", jsonIllegalInfo.getString("Distance"));
						map.put("IllegalContent", jsonIllegalInfo.getString("IllegalContent"));
						map.put("IllegalCompany", jsonIllegalInfo.getString("IllegalCompany"));
						map.put("ResponsibleName", jsonIllegalInfo.getString("ResponsibleName"));
						map.put("ResponsibleTel", jsonIllegalInfo.getString("ResponsibleTel"));
						map.put("DeclarantName", jsonIllegalInfo.getString("DeclarantName"));
						map.put("RecordingTime", jsonIllegalInfo.getString("RecordingTime"));
						map.put("IsWaring", jsonIllegalInfo.getString("IsWaring"));
						mContainerMap.put("BaseInfo", map);
						//违规建筑坐标点
						ArrayList<HashMap<String, String>> point = getPoints(jsonIllegalInfo.getString("IllegalPoints"));
						mContainerMap.put("IllegalPoints", point);
						//告知单
						JSONObject noticeObject = jsonIllegalInfo.getJSONObject("IllegalNotice");
						HashMap<String, String> noticeMap = new HashMap<String, String>();
						noticeMap.put("NoticeCompany", noticeObject.getString("NoticeCompany"));
						noticeMap.put("SignCompany", noticeObject.getString("SignCompany"));
						noticeMap.put("NoticeoPerson", noticeObject.getString("NoticeoPerson"));
						noticeMap.put("SignDate", noticeObject.getString("SignDate"));
						mContainerMap.put("IllegalNotice", noticeMap);
						//告知单存根
						JSONObject noticeSubObject = jsonIllegalInfo.getJSONObject("IllegalNoticeStub");
						HashMap<String, String> noticeSubMap = new HashMap<String, String>();
						noticeSubMap.put("NoticeCompany", noticeSubObject.getString("NoticeCompany"));
						noticeSubMap.put("SignCompany", noticeSubObject.getString("SignCompany"));
						noticeSubMap.put("NoticeoPerson", noticeSubObject.getString("NoticeoPerson"));
						noticeSubMap.put("SignDate", noticeSubObject.getString("SignDate"));
						mContainerMap.put("IllegalNoticeStub", noticeSubMap);
						//违规建筑照片
						JSONArray photoArray = jsonIllegalInfo.getJSONArray("PhotoFiles");
						ArrayList<String> photoList = new ArrayList<String>();
						if(photoArray != null && photoArray.length() > 0){
							for(int j = 0; j < photoArray.length(); j++){
								JSONObject photoObject = photoArray.getJSONObject(j);
								photoList.add(photoObject.getString("path"));
							}
						}
						mContainerMap.put("PhotoFiles",photoList );
						//违规建筑视频
						JSONArray vedioArray = jsonIllegalInfo.getJSONArray("VideoFilesPath");
						ArrayList<String> vedioList = new ArrayList<String>();
						if(vedioArray != null && vedioArray.length() > 0){
							for(int k = 0; k < vedioArray.length(); k++){
								JSONObject vedioObject = vedioArray.getJSONObject(k);
								vedioList.add(vedioObject.getString("path"));
							}
						}
						mContainerMap.put("VideoFilesPath", vedioList);
						mBackMapList.add(mContainerMap);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mBackMapList;
	}
	/**
	 * 监测管理信息
	 * @param jsonStr
	 * @return
	 */
	public HashMap<String,Object> getCommonProInfo(String jsonStr){
		HashMap<String,Object> mBackMap = new HashMap<String, Object>();
		try{
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetCommonProListResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject json=new JSONObject(jsonObj.getString("v"));
				mBackMap.put("MonitorName", json.getString("MonitorName"));
				JSONArray jsonArrayBaseInfo=new JSONArray(json.getString("MonitorList"));
				ArrayList<HashMap<String,Object>> mBackMapList = new ArrayList<HashMap<String,Object>>();
				if(jsonArrayBaseInfo != null && jsonArrayBaseInfo.length() > 0){
					for(int i = 0; i < jsonArrayBaseInfo.length(); i++){
						HashMap<String,Object> mMap = new HashMap<String, Object>();
						JSONObject  jsonBaseInfo=jsonArrayBaseInfo.getJSONObject(i);
						mMap.put("MonitorName", jsonBaseInfo.getString("MonitorName"));
						mMap.put("MonitorCode", jsonBaseInfo.getString("MonitorCode"));
						mMap.put("DecimalDigit", jsonBaseInfo.getString("DecimalDigit"));
						mMap.put("Unit", jsonBaseInfo.getString("Unit"));
						JSONArray aJsonArray=new JSONArray(jsonBaseInfo.getString("MonitorList"));
						ArrayList<HashMap<String,String>> mMapList = new ArrayList<HashMap<String,String>>();
						if(aJsonArray != null && aJsonArray.length() > 0){
							for(int j = 0; j < aJsonArray.length(); j++){
								JSONObject  jsonObject=aJsonArray.getJSONObject(j);
								HashMap<String,String> map = new HashMap<String, String>();
								map.put("MonitorName", jsonObject.getString("MonitorName"));
								map.put("MonitorCode", jsonObject.getString("MonitorCode"));
								map.put("DecimalDigit", jsonObject.getString("DecimalDigit"));
								map.put("Unit", jsonObject.getString("Unit"));
								map.put("MonitorList", jsonObject.getString("MonitorList"));
								mMapList.add(map);
							}
						}
						mMap.put("MonitorList", mMapList);
						mBackMapList.add(mMap);
					}
				}
				mBackMap.put("MonitorList", mBackMapList);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mBackMap;
	}
	/**
	 * 获得图表信息
	 * @param jsonStr
	 * @return
	 */
	public HashMap<String,Object> getGraphByCommonProInfo(String jsonStr){
		HashMap<String,Object> mBackMap = new HashMap<String, Object>();
		try{
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GraphByCommonProResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject json = jsonObj.getJSONObject("v");
				//预警
				JSONObject warnObject = json.getJSONObject("WaringVal");
				HashMap<String, String> warnMap = new HashMap<String, String>();
				warnMap.put("high", warnObject.getString("high"));
				warnMap.put("low", warnObject.getString("low"));
				mBackMap.put("WaringVal", warnMap);
				//报警
				JSONObject alrmObject = json.getJSONObject("AlarmVal");
				HashMap<String, String> alrmMap = new HashMap<String, String>();
				alrmMap.put("high", alrmObject.getString("high"));
				alrmMap.put("low", alrmObject.getString("low"));
				mBackMap.put("AlarmVal", alrmMap);
				//上行线
//				HashMap<String, Object> upMap = new HashMap<String, Object>();
				JSONArray upArray = json.getJSONArray("LeftDirX");
				ArrayList<HashMap<String, String>> upList = getLineList(upArray);
				mBackMap.put("LeftDirX", upList);
				mBackMap.put("LeftDirName", json.get("LeftDirName"));
				//下行线
				JSONArray downArray = json.getJSONArray("RightDirX");
				ArrayList<HashMap<String, String>> downList = getLineList(downArray);
				mBackMap.put("RightDirX", downList);
				mBackMap.put("RightDirName", json.get("RightDirName"));
				//落差量
				ArrayList<HashMap<String, Object>> leftAllList = new ArrayList<HashMap<String,Object>>();
				ArrayList<HashMap<String, Object>> rightAllList = new ArrayList<HashMap<String,Object>>();
				ArrayList<String> mTimeCacheList = new ArrayList<String>();
				JSONArray yCoordinateArray = json.getJSONArray("YCoordinate");
				if(yCoordinateArray != null){
					for(int i = 0; i < yCoordinateArray.length(); i++){
						JSONObject yObject = yCoordinateArray.getJSONObject(i);
						HashMap<String, Object> leftMap = new HashMap<String, Object>();
						HashMap<String, Object> rightMap = new HashMap<String, Object>();
						leftMap.put("Time", yObject.get("Time"));
						rightMap.put("Time", yObject.get("Time"));
						ArrayList<String> leftList = getCoordinate(yObject.getJSONArray("LeftDirection"));
						ArrayList<String> rightList = getCoordinate(yObject.getJSONArray("RightDirection"));
						leftMap.put("LeftDirection", leftList);
						rightMap.put("RightDirection", rightList);
						leftAllList.add(leftMap);
						rightAllList.add(rightMap);
						mTimeCacheList.add(String.valueOf(yObject.get("Time")));
					}
				}
				mBackMap.put("LeftDirection", leftAllList);
				mBackMap.put("RightDirection", rightAllList);
				mBackMap.put("CacheTimeList", mTimeCacheList);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mBackMap;
	}
	/**
	 * 获得落差量
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<String> getCoordinate(JSONArray array) throws JSONException{
		ArrayList<String> list = new ArrayList<String>();
		if(array != null){
			for(int i = 0; i < array.length(); i++){
				JSONObject object = array.getJSONObject(i);
				list.add(object.getString("y"));
			}
		}
		return list;
	}
	/**
	 * 获得线路信息
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<HashMap<String, String>> getLineList(JSONArray array) throws JSONException{
		ArrayList<HashMap<String, String>> upList = new ArrayList<HashMap<String,String>>();
		if(array != null){
			for(int i = 0; i < array.length(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject upObject = array.getJSONObject(i);
				map.put("x", upObject.getString("x"));
				map.put("xId", upObject.getString("xId"));
				upList.add(map);
			}
		}
		return upList;
	}
	/**
	 * 获取apk最新信息
	 * @param jsonStr
	 * @param mResult
	 * @return
	 * @throws JSONException
	 */
	public HashMap<String,Object> getVersion(String jsonStr, String mResult) throws JSONException{
		HashMap<String, String> map=new HashMap<String, String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, mResult);
		String succStr = jsonObj.getString("s");
		HashMap<String, Object> mR = new HashMap<String, Object>();
		mR.put("s", succStr);
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONObject jb= new JSONObject(jsonObj.getString("v"));
			String apkContent=jb.getString("apkContent");
			map.put("apkContent",apkContent);
			map.put("apkVersion",jb.getString("apkVersion"));
			map.put("apkPath",jb.getString("apkPath"));
		    mR.put("v", map);
		}
		return mR;
	}
	/**
	 * 获取违规建筑坐标点
	 * @param str
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getPoints(String str){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		if(str != null && !"null".equals(str) && !"".equals(str)){
			String points = str.split("\\(\\(")[1];
			points = points.substring(0,points.length()-2);
			String point[] = points.split(",");
			for(int i = 0; i < point.length; i++){
				HashMap<String, String> map = new HashMap<String, String>();
				if(point[i] != null){
					map.put("x", point[i].split(" ")[0]);
					map.put("y", point[i].split(" ")[1]);
					list.add(map);
				}
			}
		}
		return list; 
	}
	
	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public HashMap<String, Object> getTaskDown(String jsonStr) throws JSONException {
		HashMap<String, Object> mR=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Patrol_TaskDownResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equalsIgnoreCase(succStr)) {
			JSONArray ary = jsonObj.getJSONArray("v");
			if(ary != null && ary.length() > 0) {
				ArrayList<HashMap<String, String>> mTaskList = new ArrayList<HashMap<String, String>>();
				for(int i = 0,len = ary.length(); i < len ; i ++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject json = ary.getJSONObject(i);
					map.put("TaskID", json.getString("TaskID"));
					map.put("MetroLineID", json.getString("MetroLineID"));
					map.put("PlanStartTime", json.getString("PlanStartTime"));
					map.put("PlanEndTime", json.getString("PlanEndTime"));
					map.put("RangeName", json.getString("RangeName"));
					map.put("MembersLeader", json.getString("MembersLeader"));
					map.put("Members", json.getString("Members"));
					map.put("StartStation", json.getString("StartStation"));
					map.put("EndStation", json.getString("EndStation"));
					map.put("Remark", json.getString("Remark"));
					mTaskList.add(map);
				}
				mR.put("v", mTaskList);
			}
		}
		return mR;
	}
	
	/**
	 * 
	 * 获取车站基础信息
	 */
	public HashMap<String, Object> getBaseInfoStation(String jsonStr) throws JSONException {
		HashMap<String, Object> mR=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_StationInfoResult");
//		JSONObject jsonObj = getHandlerJSON(jsonStr, "Patrol_StationInfoResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equalsIgnoreCase(succStr)) {
			JSONArray ary = jsonObj.getJSONArray("v");
			if(ary != null && ary.length() > 0) {
				ArrayList<HashMap<String, String>> mBaseInfoList = new ArrayList<HashMap<String, String>>();
				for(int i = 0,len = ary.length(); i < len ; i ++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject json = ary.getJSONObject(i);
					map.put("MetroLineID", json.getString("MetroLineID"));
					map.put("StationID", json.getString("StationID"));
					map.put("StationName", json.getString("StationName"));
					map.put("SerialCode", json.getString("SerialCode"));
					map.put("Shape", json.getString("Shape"));
					mBaseInfoList.add(map);
				}
				mR.put("v", mBaseInfoList);
			}
		}
		return mR;
	}
	
	/**
	 * 
	 * 获取红线基础信息
	 */
	public HashMap<String, Object> getBaseInfoRedLine(String jsonStr) throws JSONException {
		HashMap<String, Object> mR=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_RedLineResult");
//		JSONObject jsonObj = getHandlerJSON(jsonStr, "Patrol_RedLineResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equalsIgnoreCase(succStr)) {
			JSONArray ary = jsonObj.getJSONArray("v");
			if(ary != null && ary.length() > 0) {
				ArrayList<HashMap<String, String>> mBaseInfoList = new ArrayList<HashMap<String, String>>();
				for(int i = 0,len = ary.length(); i < len ; i ++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject json = ary.getJSONObject(i);
					map.put("RedlineID", json.getString("RedlineID"));
					map.put("MetroLineID", json.getString("MetroLineID"));
					map.put("StartStation", json.getString("StartStation"));
					map.put("EndStation", json.getString("EndStation"));
					map.put("DrivingDirection", json.getString("DrivingDirection"));
					map.put("Shape", json.getString("Shape"));
					map.put("SectionId", json.getString("SectionId"));
					mBaseInfoList.add(map);
				}
				mR.put("v", mBaseInfoList);
			}
		}
		return mR;
	}
	/**
	 * 
	 * 获取隧道基础信息
	 */
	public HashMap<String, Object> getBaseInfoTunnel(String jsonStr) throws JSONException {
		HashMap<String, Object> mR=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_TunnelInfoResult");
//		JSONObject jsonObj = getHandlerJSON(jsonStr, "Patrol_TunnelInfoResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equalsIgnoreCase(succStr)) {
			JSONArray ary = jsonObj.getJSONArray("v");
			if(ary != null && ary.length() > 0) {
				ArrayList<HashMap<String, String>> mBaseInfoList = new ArrayList<HashMap<String, String>>();
				for(int i = 0,len = ary.length(); i < len ; i ++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject json = ary.getJSONObject(i);
					map.put("MetroLineID", json.getString("MetroLineID"));
					map.put("DrivingDirection", json.getString("DrivingDirection"));
					map.put("StartStation", json.getString("StartStation"));
					map.put("EndStation", json.getString("EndStation"));
					map.put("Name", json.getString("Name"));
					map.put("Shape", json.getString("Shape"));
					map.put("SectionId", json.getString("SectionId"));
					mBaseInfoList.add(map);
				}
				mR.put("v", mBaseInfoList);
			}
		}
		return mR;
	}
	
	/**
	 * 获取线路的文件信息
	 */
	public ArrayList<HashMap<String, String>>   getLineFiles(String jsonStr){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetRelatedFilesResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONArray  jsonArr=new JSONArray(jsonObj.getString("v"));
				for(int i=0;i<jsonArr.length();i++){
     				JSONObject json=jsonArr.getJSONObject(i);
     				HashMap<String, String> map=new HashMap<String, String>();
     				map.put("fileName", json.getString("FileName"));
     				map.put("filePath", json.getString("FilePath"));
     				arrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	/**
	 * Client_GetInfoByLineResponse{Client_GetInfoByLineResult={"s":"ok",
	 * "v":{"ProjectName":"上海申通地铁有限公司",
	 * "Carrieroperator":"申通地铁","StartTime":"2011-05-01 00:00:00",
	 * "MetroLineList":[{"LineCode":"04","LineName":"上海地铁4号线",
	 * "LeftStartMile":null,"LeftEndMile":null,"RightStartMile":null,
	 * "RightEndMile":null,"StartStationName":null,
	 * "EndStationName":null,"LineNickName":"4号线"},
	 * 
	 */
	public ArrayList<HashMap<String, String>> getLineBaseInfoLevel1(String jsonStr){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try{
			JSONObject  jsonObj=getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject  json=new JSONObject(jsonObj.getString("v"));
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("projectName", json.getString("ProjectName"));
				map.put("carrieroperator", json.getString("Carrieroperator"));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return arrayList;
	}
	/**
	 * 获取线路信息2级菜单信息
	 */
	public ArrayList<HashMap<String, String>> getLineBaseInfoLevel2(String jsonStr){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try{
			JSONObject   jsonObj= getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONArray jsonArr=new JSONArray(jsonObj.getString("v"));
				for(int i=0;i<jsonArr.length();i++){
				JSONObject json=jsonArr.getJSONObject(i);
				HashMap<String, String>  map=new HashMap<String, String>();
				map.put("branchCode", CommonUtils.textIsNull(json.getString("BranchCode")));
				map.put("branchName",CommonUtils.textIsNull(json.getString("BranchName")));
				map.put("leftStartMile", CommonUtils.textIsNull(json.getString("LeftStartMile")));
				map.put("leftEndMile", CommonUtils.textIsNull(json.getString("LeftEndMile")));
				map.put("rightStartMile", CommonUtils.textIsNull(json.getString("RightStartMile")));
				map.put("rightEndMile", CommonUtils.textIsNull(json.getString("RightEndMile")));
				map.put("startStationName", CommonUtils.textIsNull(json.getString("StartStationName")));
				map.put("endStationName", CommonUtils.textIsNull(json.getString("EndStationName")));
				arrayList.add(map);
				}
 			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return arrayList;
	}
	/**
	 * 获取线路信息3级菜单信息
	 * @param jsonStr
	 * @return
	 */
	 public ArrayList<HashMap<String, String>>  getLineBaseInfoLevel3(String jsonStr){
		 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		 try {
			JSONObject jsonObj=getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
			JSONArray jsonArr=new JSONArray(jsonObj.getString("v"));
			for(int i=0;i<jsonArr.length();i++){
				JSONObject json=jsonArr.getJSONObject(i);
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("sectionName", CommonUtils.textIsNull(json.getString("SectionName")));
				map.put("leftStartMile", CommonUtils.textIsNull(json.getString("LeftStartMile")));
				map.put("leftEndMile", CommonUtils.textIsNull(json.getString("LeftEndMile")));
				map.put("rightStartMile", CommonUtils.textIsNull(json.getString("RightStartMile")));
				map.put("rightEndMile", CommonUtils.textIsNull(json.getString("RightEndMile")));
				map.put("startStationName", CommonUtils.textIsNull(json.getString("StartStationName")));
				map.put("endStationName", CommonUtils.textIsNull(json.getString("EndStationName")));
				arrayList.add(map);
			}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 
		 return arrayList;
	 }
	
	 /**
	  * 线路第四级菜单信息
	  * @param jsonStr
	  * @return
	  */
	 public  HashMap<String, Object> getLineBaseInfoLeve4(String jsonStr){
		 HashMap<String, Object> map=new HashMap<String, Object>();
		 try {
			JSONObject jsonObj=getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject  json=new JSONObject(jsonObj.getString("v"));
				HashMap<String, String> mapBaseInfo=new HashMap<String, String>(); 
				mapBaseInfo.put("sectionName",CommonUtils.textIsNull(json.getString("SectionName")));
				mapBaseInfo.put("leftStartMile", CommonUtils.textIsNull(json.getString("LeftStartMile"))); //上行
				mapBaseInfo.put("leftEndMile", CommonUtils.textIsNull(json.getString("LeftEndMile")));
				mapBaseInfo.put("rightStartMile", CommonUtils.textIsNull(json.getString("RightStartMile")));
				mapBaseInfo.put("rightEndMile",CommonUtils.textIsNull( json.getString("RightEndMile")));
				mapBaseInfo.put("startStationName", CommonUtils.textIsNull(json.getString("StartStationName")));
				mapBaseInfo.put("endStationName", CommonUtils.textIsNull(json.getString("EndStationName")));
				mapBaseInfo.put("openTrafficTime", CommonUtils.textIsNull(json.getString("OpenTrafficTime")));
				JSONArray jsonArr=new JSONArray(json.getString("IntervalList"));
				ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
				for(int i=0;i<jsonArr.length();i++){
					JSONObject  jsonInterval=jsonArr.getJSONObject(i);
					HashMap<String, String> mapInterval=new HashMap<String, String>();
					mapInterval.put("intervalName", jsonInterval.getString("IntervalName"));
					mapInterval.put("intervalCode", jsonInterval.getString("IntervalCode"));
					arrayList.add(mapInterval);
				}
				map.put("baseInfo",mapBaseInfo );
				map.put("intervalList", arrayList);
			}
		 } catch (JSONException e) {
			e.printStackTrace();
		}
		 return map;
	 }
	 
	/**
	 * 获取线路信息5级菜单信息
	 * @param jsonStr
	 * @return
	 */
	public HashMap<String,Object> getLineBaseInfoLevel5(String jsonStr){
		HashMap<String, Object> mapParent=new HashMap<String,Object>();
		try {
			JSONObject jsonObj=getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject json=new JSONObject(jsonObj.getString("v"));
				JSONObject jsonBaseInfo=new JSONObject(json.getString("BaseInfo"));
				HashMap<String, String> mapBaseInfo=new HashMap<String, String>();
				mapBaseInfo.put("intervalName", jsonBaseInfo.getString("IntervalName")); //区间名称
				mapBaseInfo.put("intervalNumber", jsonBaseInfo.getString("IntervalNumber"));//区间编号
				mapBaseInfo.put("intervalCode", jsonBaseInfo.getString("IntervalCode"));//区间编码
				mapBaseInfo.put("shaftNumber", jsonBaseInfo.getString("ShaftNumber"));//风井数量
				mapBaseInfo.put("isImportInterval", jsonBaseInfo.getString("IsImportInterval"));
				mapBaseInfo.put("remark", jsonBaseInfo.getString("Remark"));
				mapParent.put("baseInfo",mapBaseInfo); //添加基础信息/
				//上行线基础信息
				JSONObject jsonLeftDirection=new JSONObject(json.getString("LeftDirection"));
				HashMap<String, Object>  mapLeftDirParent=new HashMap<String, Object>();
				HashMap<String, String> mapLeftDirectionBaseInfo=new HashMap<String, String>();
				mapLeftDirectionBaseInfo.put("startMile", jsonLeftDirection.getString("StartMile"));
				mapLeftDirectionBaseInfo.put("endMile", jsonLeftDirection.getString("EndMile"));
				mapLeftDirectionBaseInfo.put("checkFrequency", jsonLeftDirection.getString("CheckFrequency"));
				mapLeftDirParent.put("baseInfo", mapLeftDirectionBaseInfo);
				//
				ArrayList<HashMap<String, Object>> mapLeftDirection=new ArrayList<HashMap<String,Object>>();		
				JSONArray jsonLeftDirectionArrTunnel=new JSONArray(jsonLeftDirection.getString("TunnelList"));
				for(int i=0;i<jsonLeftDirectionArrTunnel.length();i++){
					HashMap<String, Object> mapLeftDir=new HashMap<String, Object>();
					JSONObject  jsonLeftDir=jsonLeftDirectionArrTunnel.getJSONObject(i);
					HashMap<String, String>  mapLeftDirTunnelName=new HashMap<String, String>();
					mapLeftDirTunnelName.put("tunnelName", jsonLeftDir.getString("TunnelName"));
					//MetroTunnel
					JSONObject   jsonLeftDirMetroTunnel=new JSONObject(jsonLeftDir.getString("MetroTunnel"));
					HashMap<String, String> mapLeftDirMetroTunnel=new HashMap<String, String>();
					mapLeftDirMetroTunnel.put("guidKey", jsonLeftDirMetroTunnel.getString("GuidKey"));
					mapLeftDirMetroTunnel.put("idKey", jsonLeftDirMetroTunnel.getString("IdKey"));
					mapLeftDirMetroTunnel.put("tunnelType", jsonLeftDirMetroTunnel.getString("TunnelType"));
					mapLeftDirMetroTunnel.put("segmentType", jsonLeftDirMetroTunnel.getString("SegmentType"));
					mapLeftDirMetroTunnel.put("circleWidth", jsonLeftDirMetroTunnel.getString("CircleWidth"));
					mapLeftDirMetroTunnel.put("tunnelDiameter", jsonLeftDirMetroTunnel.getString("TunnelDiameter"));
					mapLeftDirMetroTunnel.put("firstStationCode", jsonLeftDirMetroTunnel.getString("FirstStationCode"));
					mapLeftDirMetroTunnel.put("segmentsDirection", jsonLeftDirMetroTunnel.getString("SegmentsDirection"));
					mapLeftDirMetroTunnel.put("startMileage", jsonLeftDirMetroTunnel.getString("StartMileage"));
					mapLeftDirMetroTunnel.put("endMileage", jsonLeftDirMetroTunnel.getString("EndMileage"));
					mapLeftDirMetroTunnel.put("hoopBolt", jsonLeftDirMetroTunnel.getString("HoopBolt"));
					mapLeftDirMetroTunnel.put("endwiseBolt", jsonLeftDirMetroTunnel.getString("EndwiseBolt"));
					mapLeftDirMetroTunnel.put("trackbedType", jsonLeftDirMetroTunnel.getString("TrackbedType"));
					mapLeftDirMetroTunnel.put("sectionId", jsonLeftDirMetroTunnel.getString("SectionId"));
					mapLeftDirMetroTunnel.put("mileageAddDirection", jsonLeftDirMetroTunnel.getString("MileageAddDirection"));
					mapLeftDirMetroTunnel.put("sectionDirection", jsonLeftDirMetroTunnel.getString("SectionDirection"));
					mapLeftDirMetroTunnel.put("updateTime", jsonLeftDirMetroTunnel.getString("UpdateTime"));
					mapLeftDirMetroTunnel.put("gullType", jsonLeftDirMetroTunnel.getString("GullType"));
					mapLeftDirMetroTunnel.put("parentId", jsonLeftDirMetroTunnel.getString("ParentId"));
					mapLeftDirMetroTunnel.put("remark", jsonLeftDirMetroTunnel.getString("Remark"));
					mapLeftDirMetroTunnel.put("createTime", jsonLeftDirMetroTunnel.getString("CreateTime"));
					mapLeftDirMetroTunnel.put("createBy", jsonLeftDirMetroTunnel.getString("CreateBy"));
					mapLeftDirMetroTunnel.put("lastUpdateTime", jsonLeftDirMetroTunnel.getString("LastUpdateTime"));
					mapLeftDirMetroTunnel.put("lastUpdateBy", jsonLeftDirMetroTunnel.getString("LastUpdateBy"));
					mapLeftDirMetroTunnel.put("parentLevel", jsonLeftDirMetroTunnel.getString("ParentLevel"));
					mapLeftDirMetroTunnel.put("arenosolArea", jsonLeftDirMetroTunnel.getString("ArenosolArea"));
					mapLeftDirMetroTunnel.put("overproofArea", jsonLeftDirMetroTunnel.getString("OverproofArea"));
					mapLeftDirMetroTunnel.put("damagedArea", jsonLeftDirMetroTunnel.getString("DamagedArea"));
					mapLeftDirMetroTunnel.put("checkObjectDetailCode", jsonLeftDirMetroTunnel.getString("CheckObjectDetailCode"));
					mapLeftDirMetroTunnel.put("startCircle", jsonLeftDirMetroTunnel.getString("StartCircle"));  
					mapLeftDirMetroTunnel.put("endCircle", jsonLeftDirMetroTunnel.getString("EndCircle"));
					mapLeftDirMetroTunnel.put("monitorScope", jsonLeftDirMetroTunnel.getString("MonitorScope"));
					mapLeftDirMetroTunnel.put("monitorArea", jsonLeftDirMetroTunnel.getString("MonitorArea"));
					mapLeftDirMetroTunnel.put("checkFrequency", jsonLeftDirMetroTunnel.getString("CheckFrequency"));
					//隧道附加信息
					ArrayList<HashMap<String, String>> arrayListLeftDirFacilityInfo=new ArrayList<HashMap<String,String>>();
					JSONArray  jsonArrLeftDirFacilityInfo=new JSONArray(jsonLeftDir.getString("FacilityInfo"));
					for(int j=0;j<jsonArrLeftDirFacilityInfo.length();j++){
						JSONObject jsonLeftDirFacilityInfo=jsonArrLeftDirFacilityInfo.getJSONObject(i);
						HashMap<String, String> mapLeftDirFacilityInfo=new HashMap<String, String>();
						mapLeftDirFacilityInfo.put("facilityName", jsonLeftDirFacilityInfo.getString("FacilityName"));
						mapLeftDirFacilityInfo.put("facilityCode", jsonLeftDirFacilityInfo.getString("FacilityCode"));
						mapLeftDirFacilityInfo.put("startMile", jsonLeftDirFacilityInfo.getString("StartMile"));
						mapLeftDirFacilityInfo.put("endMile", jsonLeftDirFacilityInfo.getString("EndMile"));
						mapLeftDirFacilityInfo.put("startCircle", jsonLeftDirFacilityInfo.getString("StartCircle"));
						mapLeftDirFacilityInfo.put("endCircle", jsonLeftDirFacilityInfo.getString("EndCircle"));
						mapLeftDirFacilityInfo.put("remark", jsonLeftDirFacilityInfo.getString("EndCircle"));
						arrayListLeftDirFacilityInfo.add(mapLeftDirFacilityInfo);
					}
					mapLeftDir.put("tunnelName", mapLeftDirTunnelName);
					mapLeftDir.put("metroTunnel", mapLeftDirMetroTunnel);
					mapLeftDir.put("facilityInfo", arrayListLeftDirFacilityInfo);
					mapLeftDirection.add(mapLeftDir);
				}
		
				//MetroGeomeTryLineType
				ArrayList<HashMap<String, String>> arrayListLeftDirMetroGeome=new  ArrayList<HashMap<String,String>>();
				JSONArray   jsonArLeftDirrMetroGeome=new JSONArray(jsonLeftDirection.getString("MetroGeometryLineType"));
				for(int l=0;l<jsonArLeftDirrMetroGeome.length();l++){
				}
				//"SandSoilLoaction":
				ArrayList<HashMap<String, String>> arrayListLeftDirSandSoilLoaction=new  ArrayList<HashMap<String,String>>();
				JSONArray   jsonArrLeftDirSandSoilLoaction=new JSONArray(jsonLeftDirection.getString("SandSoilLoaction"));
				for(int k=0;k<jsonArrLeftDirSandSoilLoaction.length();k++){
					JSONObject jsonLeftDirSandSoilLoaction=jsonArrLeftDirSandSoilLoaction.getJSONObject(k);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("sandSoilCode", jsonLeftDirSandSoilLoaction.getString("SandSoilCode"));//砂性土壤号
					map.put("startCircle", jsonLeftDirSandSoilLoaction.getString("StartCircle"));
					map.put("endCircle", jsonLeftDirSandSoilLoaction.getString("EndCircle"));
					map.put("startMile", jsonLeftDirSandSoilLoaction.getString("StartMile"));
					map.put("remark", jsonLeftDirSandSoilLoaction.getString("Remark"));
					arrayListLeftDirSandSoilLoaction.add(map);
				}
				mapLeftDirParent.put("tunnelList", mapLeftDirection);
				mapLeftDirParent.put("metroGeome", arrayListLeftDirMetroGeome);
				mapLeftDirParent.put("sandSoilLoaction",arrayListLeftDirSandSoilLoaction);
				mapParent.put("leftDirection", mapLeftDirParent);
				//下行线
				JSONObject jsonRightDirection=new JSONObject(json.getString("RightDirection"));
				HashMap<String, Object>  mapRightDirParent=new HashMap<String, Object>();
				HashMap<String, String> mapRightDirectionBaseInfo=new HashMap<String, String>();
				mapRightDirectionBaseInfo.put("startMile", jsonRightDirection.getString("StartMile"));
				mapRightDirectionBaseInfo.put("endMile", jsonRightDirection.getString("EndMile"));
				mapRightDirectionBaseInfo.put("checkFrequency", jsonRightDirection.getString("CheckFrequency"));
				mapRightDirParent.put("baseInfo", mapRightDirectionBaseInfo);
				//
				ArrayList<HashMap<String, Object>> mapRightDirection=new ArrayList<HashMap<String,Object>>();		
				JSONArray jsonRightDirectionArrTunnel=new JSONArray(jsonRightDirection.getString("TunnelList"));
				for(int i=0;i<jsonRightDirectionArrTunnel.length();i++){
					HashMap<String, Object> mapRightDir=new HashMap<String, Object>();
					JSONObject  jsonLeftDir=jsonRightDirectionArrTunnel.getJSONObject(i);
					HashMap<String, String>  mapRightDirTunnelName=new HashMap<String, String>();
					mapRightDirTunnelName.put("tunnelName", jsonLeftDir.getString("TunnelName"));
					//MetroTunnel
					JSONObject   jsonRightDirMetroTunnel=new JSONObject(jsonLeftDir.getString("MetroTunnel"));
					HashMap<String, String> mapRightDirMetroTunnel=new HashMap<String, String>();
					mapRightDirMetroTunnel.put("guidKey", jsonRightDirMetroTunnel.getString("GuidKey"));
					mapRightDirMetroTunnel.put("idKey", jsonRightDirMetroTunnel.getString("IdKey"));
					mapRightDirMetroTunnel.put("tunnelType", jsonRightDirMetroTunnel.getString("TunnelType"));
					mapRightDirMetroTunnel.put("segmentType", jsonRightDirMetroTunnel.getString("SegmentType"));
					mapRightDirMetroTunnel.put("circleWidth", jsonRightDirMetroTunnel.getString("CircleWidth"));
					mapRightDirMetroTunnel.put("tunnelDiameter", jsonRightDirMetroTunnel.getString("TunnelDiameter"));
					mapRightDirMetroTunnel.put("firstStationCode", jsonRightDirMetroTunnel.getString("FirstStationCode"));
					mapRightDirMetroTunnel.put("segmentsDirection", jsonRightDirMetroTunnel.getString("SegmentsDirection"));
					mapRightDirMetroTunnel.put("startMileage", jsonRightDirMetroTunnel.getString("StartMileage")); //起始里程
					mapRightDirMetroTunnel.put("endMileage", jsonRightDirMetroTunnel.getString("EndMileage"));  //终止里程
					mapRightDirMetroTunnel.put("hoopBolt", jsonRightDirMetroTunnel.getString("HoopBolt"));
					mapRightDirMetroTunnel.put("endwiseBolt", jsonRightDirMetroTunnel.getString("EndwiseBolt"));
					mapRightDirMetroTunnel.put("trackbedType", jsonRightDirMetroTunnel.getString("TrackbedType")); //道床类型
					mapRightDirMetroTunnel.put("sectionId", jsonRightDirMetroTunnel.getString("SectionId"));
					mapRightDirMetroTunnel.put("mileageAddDirection", jsonRightDirMetroTunnel.getString("MileageAddDirection"));  //里程增加方向
					mapRightDirMetroTunnel.put("sectionDirection", jsonRightDirMetroTunnel.getString("SectionDirection"));
					mapRightDirMetroTunnel.put("updateTime", jsonRightDirMetroTunnel.getString("UpdateTime"));
					mapRightDirMetroTunnel.put("gullType", jsonRightDirMetroTunnel.getString("GullType"));
					mapRightDirMetroTunnel.put("parentId", jsonRightDirMetroTunnel.getString("ParentId"));
					mapRightDirMetroTunnel.put("remark", jsonRightDirMetroTunnel.getString("Remark")); //备注
					mapRightDirMetroTunnel.put("createTime", jsonRightDirMetroTunnel.getString("CreateTime"));
					mapRightDirMetroTunnel.put("createBy", jsonRightDirMetroTunnel.getString("CreateBy"));
					mapRightDirMetroTunnel.put("lastUpdateTime", jsonRightDirMetroTunnel.getString("LastUpdateTime"));
					mapRightDirMetroTunnel.put("lastUpdateBy", jsonRightDirMetroTunnel.getString("LastUpdateBy"));
					mapRightDirMetroTunnel.put("parentLevel", jsonRightDirMetroTunnel.getString("ParentLevel"));
					mapRightDirMetroTunnel.put("arenosolArea", jsonRightDirMetroTunnel.getString("ArenosolArea"));
					mapRightDirMetroTunnel.put("overproofArea", jsonRightDirMetroTunnel.getString("OverproofArea"));
					mapRightDirMetroTunnel.put("damagedArea", jsonRightDirMetroTunnel.getString("DamagedArea"));
					mapRightDirMetroTunnel.put("checkObjectDetailCode", jsonRightDirMetroTunnel.getString("CheckObjectDetailCode"));
					mapRightDirMetroTunnel.put("startCircle", jsonRightDirMetroTunnel.getString("StartCircle"));
					mapRightDirMetroTunnel.put("endCircle", jsonRightDirMetroTunnel.getString("EndCircle"));
					mapRightDirMetroTunnel.put("monitorScope", jsonRightDirMetroTunnel.getString("MonitorScope"));
					mapRightDirMetroTunnel.put("monitorArea", jsonRightDirMetroTunnel.getString("MonitorArea"));
					mapRightDirMetroTunnel.put("checkFrequency", jsonRightDirMetroTunnel.getString("CheckFrequency"));
					//隧道附加信息
					ArrayList<HashMap<String, String>> arrayListRightDirFacilityInfo=new ArrayList<HashMap<String,String>>();
					JSONArray  jsonArrRightDirFacilityInfo=new JSONArray(jsonLeftDir.getString("FacilityInfo"));
					for(int j=0;j<jsonArrRightDirFacilityInfo.length();j++){
						JSONObject jsonRightDirFacilityInfo=jsonArrRightDirFacilityInfo.getJSONObject(i);
						HashMap<String, String> mapRightDirFacilityInfo=new HashMap<String, String>();
						mapRightDirFacilityInfo.put("facilityName", jsonRightDirFacilityInfo.getString("FacilityName"));
						mapRightDirFacilityInfo.put("facilityCode", jsonRightDirFacilityInfo.getString("FacilityCode"));
						mapRightDirFacilityInfo.put("startMile", jsonRightDirFacilityInfo.getString("StartMile"));
						mapRightDirFacilityInfo.put("endMile", jsonRightDirFacilityInfo.getString("EndMile"));
						mapRightDirFacilityInfo.put("startCircle", jsonRightDirFacilityInfo.getString("StartCircle"));
						mapRightDirFacilityInfo.put("endCircle", jsonRightDirFacilityInfo.getString("EndCircle"));
						mapRightDirFacilityInfo.put("remark", jsonRightDirFacilityInfo.getString("EndCircle"));
						arrayListRightDirFacilityInfo.add(mapRightDirFacilityInfo);
					}
					mapRightDir.put("tunnelName", mapRightDirTunnelName);
					mapRightDir.put("metroTunnel", mapRightDirMetroTunnel);
					mapRightDir.put("facilityInfo", arrayListRightDirFacilityInfo);
					mapRightDirection.add(mapRightDir);
				}
				//MetroGeomeTryLineType
				ArrayList<HashMap<String, String>> arrayListRightDirMetroGeome=new  ArrayList<HashMap<String,String>>();
				JSONArray   jsonArrRightDirMetroGeome=new JSONArray(jsonLeftDirection.getString("MetroGeometryLineType"));
				for(int l=0;l<jsonArrRightDirMetroGeome.length();l++){
				}
				//"SandSoilLoaction":
				ArrayList<HashMap<String, String>> arrayListRightDirSandSoilLoaction=new  ArrayList<HashMap<String,String>>();
				JSONArray   jsonArrRightDirSandSoilLoaction=new JSONArray(jsonLeftDirection.getString("SandSoilLoaction"));
				for(int k=0;k<jsonArrRightDirSandSoilLoaction.length();k++){
					JSONObject jsonRightDirSandSoilLoaction=jsonArrLeftDirSandSoilLoaction.getJSONObject(k);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("sandSoilCode", jsonRightDirSandSoilLoaction.getString("SandSoilCode"));//砂性土壤号
					map.put("startCircle", jsonRightDirSandSoilLoaction.getString("StartCircle"));
					map.put("endCircle", jsonRightDirSandSoilLoaction.getString("EndCircle"));
					map.put("startMile", jsonRightDirSandSoilLoaction.getString("StartMile"));
					map.put("remark", jsonRightDirSandSoilLoaction.getString("Remark"));
					arrayListRightDirSandSoilLoaction.add(map);
				}
				mapRightDirParent.put("metroGeome", arrayListRightDirMetroGeome);
				mapRightDirParent.put("sandSoilLoaction",arrayListRightDirSandSoilLoaction);
				mapParent.put("rightDirection", mapRightDirParent);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapParent;
	}
	
	
	/**
	 * 病害类型信息解析
	 * 表DamageType，DamgeLevel，DamageInfoFieldRel，CheckTypeDamageRel，DamageInputOpreation，z_ConstructionCheckDic,FacilityConstraint
	 * @throws JSONException 
	 */
	public HashMap<String, Object> parseDamageTypeInfo(String jsonStr ) throws JSONException{
		HashMap<String, Object> baseinfo=new HashMap<String, Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "DamageTypeInfoResult");
		ArrayList<HashMap<String, String>> damageTypeList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> damageLevelList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> damageInfoFieldRelList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> checkTypeDamageRelList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> damageInputOpreationList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> z_ConstructionCheckDicList=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> facilityConstraintList=new ArrayList<HashMap<String,String>>();
		
		String succStr = jsonObj.getString("s");
		String updateStr = jsonObj.getString("m");
		if ("ok".equalsIgnoreCase(succStr) && "update".equalsIgnoreCase(updateStr)) {
			JSONObject json = jsonObj.getJSONObject("v");
			String upDateTime = json.getString("UpdateTime");
			JSONArray damageTypeListJSON = json.getJSONArray("DamageTypeList");
			if(damageTypeListJSON!= null) {
				for(int i=0; i<damageTypeListJSON.length(); i++) {
					HashMap<String,String> damageTypeMap = new HashMap<String, String>();
					JSONObject damageTypeJSON= damageTypeListJSON.getJSONObject(i);
					damageTypeMap.put("DamageTypeCode", damageTypeJSON.getString("DamageTypeCode"));
					damageTypeMap.put("DamageTypeName", damageTypeJSON.getString("DamageTypeName"));
					damageTypeMap.put("DamageTypeShowWay", damageTypeJSON.getString("RecordWay"));
					damageTypeMap.put("DamageStatisticsType", damageTypeJSON.getString("DamageStatisticsType"));
					damageTypeMap.put("FacilityType", damageTypeJSON.getString("FacilityTypeCode"));
					damageTypeMap.put("Remark", damageTypeJSON.getString("Remark"));
					damageTypeMap.put("CheckObjectDetailType", damageTypeJSON.getString("CheckObjectDetailType"));
					damageTypeList.add(damageTypeMap);
					JSONArray damageLevelListJSON = damageTypeJSON.getJSONArray("DamageLevelList");
					for(int j=0; j<damageLevelListJSON.length(); j++) {
						HashMap<String,String> damageLevelMap = new HashMap<String, String>();
						JSONObject damageLevelJSON = damageLevelListJSON.getJSONObject(j);
						damageLevelMap.put("DamageTypID", damageTypeJSON.getString("DamageTypeCode"));	//数据库中的键值是DamageTypeID，请注意！
						damageLevelMap.put("DamgeLevelId", damageLevelJSON.getString("DamageLevelId"));
						damageLevelMap.put("DamgeLevel", damageLevelJSON.getString("DamageLevel"));
						damageLevelMap.put("DamgeDescribe", damageLevelJSON.getString("DamageDescribe"));
						damageLevelList.add(damageLevelMap);
					}
					JSONArray damageInfoFieldRelListJSON = damageTypeJSON.getJSONArray("DamageInfoFieldRelList");
					for(int k=0; k<damageInfoFieldRelListJSON.length(); k++) {
						HashMap<String,String> damageInfoFieldRelMap = new HashMap<String, String>();
						JSONObject damageInfoFieldRelJSON = damageInfoFieldRelListJSON.getJSONObject(k);
						damageInfoFieldRelMap.put("DamageTypeCode", damageTypeJSON.getString("DamageTypeCode"));	
						damageInfoFieldRelMap.put("Demo", damageInfoFieldRelJSON.getString("Demo"));
						damageInfoFieldRelMap.put("FieldDesc", damageInfoFieldRelJSON.getString("FieldDesc"));
						damageInfoFieldRelMap.put("FieldName", damageInfoFieldRelJSON.getString("FieldName"));
						damageInfoFieldRelMap.put("DamageInputOpreationCode", damageInfoFieldRelJSON.getString("DamageInputOpreationCode"));
						damageInfoFieldRelList.add(damageInfoFieldRelMap);
					}
					JSONArray checkTypeDamageRelListJSON = damageTypeJSON.getJSONArray("CheckTypeDamageRelList");
					for(int m=0; m<checkTypeDamageRelListJSON.length(); m++) {
						HashMap<String,String> checkTypeDamageRelMap = new HashMap<String, String>();
						JSONObject checkTypeDamageRelJSON = checkTypeDamageRelListJSON.getJSONObject(m);
						checkTypeDamageRelMap.put("DamageTypeCode", damageTypeJSON.getString("DamageTypeCode"));
						checkTypeDamageRelMap.put("CheckTaskType", checkTypeDamageRelJSON.getString("CheckTaskType"));
						checkTypeDamageRelList.add(checkTypeDamageRelMap);
					}
				}
			}
			JSONArray damageInputOpreationListJSON = json.getJSONArray("DamageInputOpreationList");
			if(damageInputOpreationListJSON!= null) {
				for(int i=0; i<damageInputOpreationListJSON.length(); i++) {
					HashMap<String,String> damageInputOpreationMap = new HashMap<String, String>();
					JSONObject damageInputOpreationJSON= damageInputOpreationListJSON.getJSONObject(i);
					damageInputOpreationMap.put("DamageInputOpreationCode", damageInputOpreationJSON.getString("DamageInputOpreationCode"));
					damageInputOpreationMap.put("InputType", damageInputOpreationJSON.getString("InputType"));
					damageInputOpreationMap.put("Items", damageInputOpreationJSON.getString("Items"));
					damageInputOpreationMap.put("SubItems", damageInputOpreationJSON.getString("SubItems"));
					damageInputOpreationMap.put("ContentType", damageInputOpreationJSON.getString("ContentType"));
					damageInputOpreationMap.put("DescribeLeft", damageInputOpreationJSON.getString("DescribeLeft"));
					damageInputOpreationMap.put("DescribeRight", damageInputOpreationJSON.getString("DescribeRight"));
					damageInputOpreationMap.put("ParamType", damageInputOpreationJSON.getString("ParamType"));
					damageInputOpreationList.add(damageInputOpreationMap);
				}
			}
			JSONArray z_ConstructionCheckDicListJSON = json.getJSONArray("z_ConstructionCheckDicList");
			if(z_ConstructionCheckDicListJSON!= null) {
				for(int i=0; i<z_ConstructionCheckDicListJSON.length(); i++) {
					HashMap<String,String> z_ConstructionCheckDicMap = new HashMap<String, String>();
					JSONObject z_ConstructionCheckDicJSON= z_ConstructionCheckDicListJSON.getJSONObject(i);
					z_ConstructionCheckDicMap.put("ParamId", z_ConstructionCheckDicJSON.getString("ParamId"));
					z_ConstructionCheckDicMap.put("ParamType", z_ConstructionCheckDicJSON.getString("ParamType"));
					z_ConstructionCheckDicMap.put("PDicId", z_ConstructionCheckDicJSON.getString("PDicId"));
					z_ConstructionCheckDicMap.put("ParamName", z_ConstructionCheckDicJSON.getString("ParamName"));
					z_ConstructionCheckDicMap.put("ParamValue", z_ConstructionCheckDicJSON.getString("ParamValue"));
					z_ConstructionCheckDicMap.put("Remark", z_ConstructionCheckDicJSON.getString("Remark"));
					z_ConstructionCheckDicList.add(z_ConstructionCheckDicMap);
				}
			}
			JSONArray facilityConstraintListJSON = json.getJSONArray("FacilityConstraintList");
			if(facilityConstraintListJSON!= null) {
				for(int i=0; i<facilityConstraintListJSON.length(); i++) {
					HashMap<String,String> facilityConstraintMap = new HashMap<String, String>();
					JSONObject facilityConstraintJSON= facilityConstraintListJSON.getJSONObject(i);
					facilityConstraintMap.put("DamageTypeCode", facilityConstraintJSON.getString("FacilityTypeCode"));
					facilityConstraintMap.put("canDelete", facilityConstraintJSON.getString("canDelete"));
					facilityConstraintMap.put("Remark", facilityConstraintJSON.getString("Remark"));
					facilityConstraintList.add(facilityConstraintMap);
				}
			}
			baseinfo.put("UpdateTime", upDateTime);
			baseinfo.put("DamageTypeList", damageTypeList);
			baseinfo.put("DamageLevelList", damageLevelList);
			baseinfo.put("DamageInfoFieldRelList", damageInfoFieldRelList);
			baseinfo.put("CheckTypeDamageRelList", checkTypeDamageRelList);
			baseinfo.put("DamageInputOpreationList", damageInputOpreationList);
			baseinfo.put("z_ConstructionCheckDicList", z_ConstructionCheckDicList);
			baseinfo.put("FacilityConstraintList", facilityConstraintList);
			return baseinfo; 
		}else{
			return null;
		}
	}
	
	public HashMap<String, Object>  getLineBaseInfoLevel6(String jsonStr){
		HashMap<String, Object> map=new HashMap<String, Object>();
		try {
			JSONObject jsonObj=getHandlerJSON(jsonStr, "Client_GetInfoByLineResult");
			if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
				JSONObject  json=new JSONObject(jsonObj.getString("v"));
				HashMap<String, String> mapBaseInfo=new HashMap<String, String>();
				mapBaseInfo.put("stationName", CommonUtils.textIsNull(json.getString("StationName"))); //站名
				mapBaseInfo.put("stationType",  CommonUtils.textIsNull(json.getString("StationType")));//站名类型
				mapBaseInfo.put("isChangeOthers", CommonUtils.textIsNull(json.getString("IsChangeOthers"))); //可否换乘
				mapBaseInfo.put("stationNumber", CommonUtils.textIsNull(json.getString("StationNumber"))); //站点序号
				mapBaseInfo.put("stationCode", CommonUtils.textIsNull(json.getString("StationCode")));
				map.put("baseInfo", mapBaseInfo);
				//上行线
				/*{"TunnelInfoList":[{"EndMile":null,"StartMile":null,"FacilityCode":"01-01cz-s-ztdc-01","FacilityName":"车站整体道床"},{
				 * "EndMile":null,"StartMile":null,"FacilityCode":"01-01cz-s-qjcz-01","FacilityName":"区间车站"}],
				 * "EndMile":null,"CheckFrequency":2,"StartMile":null}
				 * 
				 */
				JSONObject jsonLeftDirection=new JSONObject(json.getString("LeftDirection"));
				HashMap<String, Object> mapLeftDirection=new HashMap<String, Object>();
				HashMap<String, String> mapLeftDirectionBaseInfo=new HashMap<String, String>();
				mapLeftDirectionBaseInfo.put("startMile", CommonUtils.textIsNull(jsonLeftDirection.getString("StartMile")));
				mapLeftDirectionBaseInfo.put("endMile", CommonUtils.textIsNull(jsonLeftDirection.getString("EndMile")));
				mapLeftDirectionBaseInfo.put("checkFrequency",CommonUtils.textIsNull(jsonLeftDirection.getString("CheckFrequency")));
				mapLeftDirection.put("baseInfo", mapLeftDirectionBaseInfo);
				JSONArray  jsonArrLeftDirection=new JSONArray(jsonLeftDirection.getString("TunnelInfoList"));
				ArrayList<HashMap<String, String>> arrayListLeftRirectionTunnel=new ArrayList<HashMap<String,String>>();
				for(int i=0;i<jsonArrLeftDirection.length();i++){
					JSONObject jsonLeftDrectionTunnelInfo=jsonArrLeftDirection.getJSONObject(i);
					HashMap<String, String> mapLeftDirectionTunnelInfo=new HashMap<String, String>();
					mapLeftDirectionTunnelInfo.put("facilityName", jsonLeftDrectionTunnelInfo.getString("FacilityName")); //设施名称
					mapLeftDirectionTunnelInfo.put("facilityCode", jsonLeftDrectionTunnelInfo.getString("FacilityCode"));//设施编号
					mapLeftDirectionTunnelInfo.put("startMile",  CommonUtils.textIsNull(jsonLeftDrectionTunnelInfo.getString("StartMile")));//起始里程
					mapLeftDirectionTunnelInfo.put("endMile",  CommonUtils.textIsNull(jsonLeftDrectionTunnelInfo.getString("EndMile")));//终止里程
					arrayListLeftRirectionTunnel.add(mapLeftDirectionTunnelInfo);
				}
				mapLeftDirection.put("tunnelList", arrayListLeftRirectionTunnel);
				map.put("leftDirection", mapLeftDirection);
				//下行线
				JSONObject jsonRightDirection=new JSONObject(json.getString("RightDirection"));
				HashMap<String, Object> mapRightDirection=new HashMap<String, Object>();
				HashMap<String, String> mapRightDirectionBaseInfo=new HashMap<String, String>();
				mapLeftDirectionBaseInfo.put("startMile", CommonUtils.textIsNull(jsonRightDirection.getString("StartMile")));
				mapLeftDirectionBaseInfo.put("endMile", CommonUtils.textIsNull(jsonRightDirection.getString("EndMile")));
				mapLeftDirectionBaseInfo.put("checkFrequency",CommonUtils.textIsNull(jsonLeftDirection.getString("CheckFrequency")));
				mapRightDirection.put("baseInfo", mapRightDirectionBaseInfo);
				JSONArray  jsonArrRightDirection=new JSONArray(jsonRightDirection.getString("TunnelInfoList"));
				ArrayList<HashMap<String, String>> arrayListRightRirectionTunnel=new ArrayList<HashMap<String,String>>();
				for(int i=0;i<jsonArrRightDirection.length();i++){
					JSONObject jsonRightDrectionTunnelInfo=jsonArrRightDirection.getJSONObject(i);
					HashMap<String, String> mapRightDirectionTunnelInfo=new HashMap<String, String>();
					jsonRightDrectionTunnelInfo.put("facilityName", jsonRightDrectionTunnelInfo.getString("FacilityName")); //设施名称
					jsonRightDrectionTunnelInfo.put("facilityCode", jsonRightDrectionTunnelInfo.getString("FacilityCode"));//设施编号
					jsonRightDrectionTunnelInfo.put("startMile", CommonUtils.textIsNull(jsonRightDrectionTunnelInfo.getString("StartMile")));//起始里程
					jsonRightDrectionTunnelInfo.put("endMile",  CommonUtils.textIsNull(jsonRightDrectionTunnelInfo.getString("EndMile")));//终止里程
					arrayListRightRirectionTunnel.add(mapRightDirectionTunnelInfo);
				}
				mapRightDirection.put("tunnelList", arrayListRightRirectionTunnel);
				map.put("rightDirection", mapLeftDirection);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 病害病害分析
	 * @throws JSONException 
	 */
	public ArrayList<HashMap<String, Object>> parseDamageAnalysis(String jsonStr ) throws JSONException{
		
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetDamageAnalysisListResult");
		String succStr = jsonObj.getString("s");
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONArray  detaillist=new JSONArray(jsonObj.getString("v"));
			if(detaillist!= null) {
				ArrayList<HashMap<String, Object>> damageAnalysisList=new ArrayList<HashMap<String,Object>>();
				for(int i = 0; i < detaillist.length(); i ++) {
					HashMap<String, Object> damageAnalysisMap=new HashMap<String, Object>();
					JSONObject jsonDamageAnalysis= (JSONObject)detaillist.get(i);
					damageAnalysisMap.put("CheckObjectCode", jsonDamageAnalysis.get("CheckObjectCode").toString());
					damageAnalysisMap.put("CheckObjectName", jsonDamageAnalysis.get("CheckObjectName").toString());
					JSONArray jsonCompareDateList=jsonDamageAnalysis.getJSONArray("CompareDateList");
					ArrayList<HashMap<String, Object>> compareDateList=new ArrayList<HashMap<String,Object>>();
					for (int j = 0; j < jsonCompareDateList.length(); j++) {
						HashMap<String, Object> compareDateMap=new HashMap<String, Object>();
						JSONObject jsonCheckObjectInfo= (JSONObject)jsonCompareDateList.get(j);
						//对比时间
						String jsonCompareDate = jsonCheckObjectInfo.getString("CompareDate");	
						//上行
						JSONObject jsonUpDirection = jsonCheckObjectInfo.getJSONObject("UpDirection");	
						HashMap<String, Object> UpDirectionMap = new HashMap<String, Object>();
						UpDirectionMap.put("CheckObjectDetailCode", jsonUpDirection.getString("CheckObjectDetailCode"));
						UpDirectionMap.put("DamageNumber", jsonUpDirection.getInt("DamageNumber"));
						//下行
						JSONObject jsonDownDirection = jsonCheckObjectInfo.getJSONObject("DownDirection");	
						HashMap<String, String> DownDirectionMap = new HashMap<String, String>();
						DownDirectionMap.put("CheckObjectDetailCode", jsonDownDirection.getString("CheckObjectDetailCode"));
						DownDirectionMap.put("DamageNumber", jsonDownDirection.getString("DamageNumber"));
						
						compareDateMap.put("CompareDate", jsonCompareDate);
						compareDateMap.put("UpDirection", UpDirectionMap);
						compareDateMap.put("DownDirection", DownDirectionMap);
						
						compareDateList.add(compareDateMap);
					}
					damageAnalysisMap.put("CompareDateList", compareDateList);
					damageAnalysisList.add(damageAnalysisMap);
				}
				return damageAnalysisList;
			}
		}
		return null; 
	}
	
	
	/**
	 * 解析获取获取病害(个数)分析数据在隧道中的位置数据
	 * @throws JSONException 
	 */
	public ArrayList<HashMap<String, String>> parseDamageLocation(String jsonStr ) throws JSONException{
		
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_GetDamageByLocationResult");
		String succStr = jsonObj.getString("s");
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONArray  detaillist=new JSONArray(jsonObj.getString("v"));
			if(detaillist!= null) {
				ArrayList<HashMap<String, String>> damageInfoList=new ArrayList<HashMap<String,String>>();
				for(int i = 0; i < detaillist.length(); i ++) {
					HashMap<String, String> damageInfoMap=new HashMap<String, String>();
					JSONObject jsonDamageAnalysis= (JSONObject)detaillist.get(i);
					damageInfoMap.put("MiliRange", jsonDamageAnalysis.get("MiliRange").toString());
					damageInfoMap.put("DamageNumber", jsonDamageAnalysis.get("DamageNumber").toString());
					damageInfoList.add(damageInfoMap);
				}
				return damageInfoList;
			}
		}
		return null; 
	}

	/**
	 * 获取监测日历列表
	 * @throws JSONException
	 * @author Rubert
	 * @date 2014-04-09
	 */
	public Map<String,Map<String,Boolean>> getMonitorCalendarList(String jsonStr) throws JSONException {
		Map<String,Map<String, Boolean>> mCacheMap = new HashMap<String, Map<String, Boolean>>();
		JSONObject jsonObj = getHandlerJSON(jsonStr, "Client_MonitorCalendarListResult");
		if("ok".equalsIgnoreCase(jsonObj.getString("s"))){
			JSONArray array = jsonObj.getJSONArray("v");
			if(array != null && jsonObj.length() > 0) {
				for(int i = 0,len = array.length(); i < len; i++) {
					JSONObject obj = array.getJSONObject(i);
					JSONArray arrayTime = obj.getJSONArray("time");
					Map<String, Boolean> mTimeCacheMap = new HashMap<String, Boolean>();
					if(arrayTime != null && arrayTime.length() > 0) {
						for(int j = 0, lenJ = arrayTime.length(); j < lenJ; j ++ ) {
							mTimeCacheMap.put(arrayTime.getJSONObject(j).getString("detail"), Boolean.valueOf(true));
						}
					}
					mCacheMap.put(obj.getString("day"), mTimeCacheMap);
				}
			}
		}
		return mCacheMap;
	}
	
	/**
	 * 合成Json
	 * @param mObjectList
	 * @throws JSONException
	 * @return  例如：[{"username":"wanglihong","height":12.5,"age":24}]  
	 */
	public static String getJSONString(ArrayList<HashMap<String, String>> mObjectList){
		try {
			JSONArray jsonArray = new JSONArray(); 
			for(HashMap<String, String> mObject: mObjectList){
				Iterator iterator = mObject.entrySet().iterator();
				JSONObject jsonObject = new JSONObject();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					
						jsonObject.put(key, value);
					
				}
				jsonArray.put(jsonObject);
			}
			return jsonArray.toString();
		} catch (JSONException e) {
			return "";
		}
	}
	
	
}
