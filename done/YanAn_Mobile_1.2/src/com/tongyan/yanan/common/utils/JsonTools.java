package com.tongyan.yanan.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author ChenLang
 * @category Json数据解析
 * @version 1.0
 */
public class JsonTools {

	public static JSONObject getJson(String jsonString) {
		JSONObject json = null;
		try {
			if (json == null) {
				json = new JSONObject(jsonString);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static String getURLEncoderString(String str) {
		try {
			return URLEncoder.encode(str, "utf8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return str;
	}
	
	
	public static String getHandlerString(String str) {
		if(str == null || "null".equalsIgnoreCase(str)) {
			return "";
		}
		return str;
	}
	
	
	/**
	 * 
	 * 登陆
	 * 
	 * @LastMT 2014/06/25 10:00 wanghb
	 * */
	public static HashMap<String, Object> getLoginMap(String jsonStr) throws JSONException {
		HashMap<String, Object> mR = new HashMap<String, Object>();
		JSONObject jsonObj = new JSONObject(jsonStr);
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonUser = new JSONObject(jsonObj.getString("v"));
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("UserId", jsonUser.getString("UserId"));
			map.put("LoginAccount", jsonUser.getString("LoginAccount"));
			map.put("UserName", jsonUser.getString("UserName"));
			map.put("DeptId", jsonUser.getString("DeptId"));
			map.put("JobId", jsonUser.getString("JobId"));
			map.put("UserPhone", jsonUser.getString("UserPhone"));
			map.put("UserEmail", jsonUser.getString("UserEmail"));
			map.put("UserQQ", jsonUser.getString("UserQQ"));
			map.put("SysId", jsonUser.getString("SysId"));
			map.put("UserRoleId", jsonUser.getString("UserRoleId"));
			map.put("UserSex", jsonUser.getString("UserSex"));
			map.put("UserBirthday", jsonUser.getString("UserBirthday"));
			mR.put("v", map);
		}
		return mR;
	}


	/**
	 * 
	 * 合同段数据解析
	 * @author wanghb
	 * @LastMT 2014/06/26
	 * */
	public static ArrayList<HashMap<String, String>> getTypeInfo(String typeInfo) throws Exception {
		String mParentId = "00000000-0000-0000-0000-000000000000";
		ArrayList<HashMap<String, String>> mAllList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> mParentList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> mBackList = new ArrayList<HashMap<String, String>>();
		if (!"".equals(typeInfo)) {
			JSONObject object = new JSONObject(typeInfo);
			String typeStr = object.getString("v");
			JSONArray arr = new JSONArray(typeStr);
			// 首先添加所有父类监测
			for (int i = 0; i < arr.length(); i++) {
				JSONObject json = arr.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("NewId", json.getString("NewId"));
				map.put("MonitorTypeName", json.getString("MonitorTypeName"));
				String mPid = json.getString("Pid");
				map.put("Pid", mPid);
				if(mParentId.equals(mPid)) {
					mParentList.add(map);
				}
				mAllList.add(map);
			}
		}
		if(null != mParentList && mParentList.size() > 0) {
			for(int i = 0,len = mParentList.size(); i < len; i ++) {
				HashMap<String, String> map = mParentList.get(i);
				if(map != null) {
					map.put("attribute", "title");
					mBackList.add(map);
					String mNewId = map.get("NewId");
					for(int j = 0, lenJ = mAllList.size(); j < lenJ; j ++) {
						HashMap<String, String> mapJ = mAllList.get(j);
						if(mNewId != null && mNewId.equals(mapJ.get("Pid"))) {
							mapJ.put("attribute", "content");
							mapJ.put("monitorProjectTypeName", map.get("MonitorTypeName"));
							mBackList.add(mapJ);
						}
					}
				}
			}
		}
		return mBackList;
	}
	
	
	
	/**
	 * @category 解析期段数据
	 * @param jsonStr
	 *  传入从服务器得到的字符串
	 * @return list
	 */
	public static ArrayList<HashMap<String, String>> getPeriodsList(String jsonStr) {
		ArrayList<HashMap<String, String>> mPeriodsList = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject object = new JSONObject(jsonStr);
			JSONArray arr = new JSONArray(object.getString("v"));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("newId", jsonObj.getString("NewId"));
				map.put("projectName", jsonObj.getString("ProjectName"));
				map.put("projectCode", jsonObj.getString("ProjectCode"));
//				map.put("stutas", jsonObj.getString("Stutas"));
//				map.put("startDate", jsonObj.getString("StartDate"));
//				map.put("endDate", jsonObj.getString("EndDate"));
//				map.put("sysId", jsonObj.getString("SysId"));
				mPeriodsList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mPeriodsList;
	}

	
	/**
	 * @category 解析合同段数据
	 * @param jsonStr  传入从服务器得到的字符串
	 * @return list
	 */
	public static ArrayList<HashMap<String, String>> getPactList(String jsonStr) {
		ArrayList<HashMap<String, String>> mPactList = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject object = new JSONObject(jsonStr);
			JSONArray arr = new JSONArray(object.getString("v"));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("newId", jsonObj.getString("NewId"));
				map.put("projectId", jsonObj.getString("ProjectId"));
				map.put("lotName", jsonObj.getString("LotName"));
				map.put("attribute", "content");
				mPactList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mPactList;
	}
	
	/**
	 * @category监测点获取
	 * @param   传入从服务器得到的字符串
	 * @return list
	 */
	public static ArrayList<HashMap<String, String>> getMonitorPointList(String jsonStr){
		ArrayList<HashMap<String, String>>  mMonnitorPointList=new ArrayList<HashMap<String,String>>();
		try{
			JSONObject object=new JSONObject(jsonStr);
			String arrV=object.getString("v");
			if(!"".equals(arrV)){
				JSONArray  arrObj=new JSONArray(arrV);
				 for(int i=0;i<arrObj.length();i++){
					 JSONObject  jsonObj=arrObj.getJSONObject(i);
					 HashMap<String, String> map=new HashMap<String, String>();
					 map.put("newId", jsonObj.getString("NewId"));
					 map.put("monitorName", jsonObj.getString("MonitorName"));
					 map.put("monitorTypeId", jsonObj.getString("MonitotTypeId"));
					 map.put("pointX", jsonObj.getString("PointX"));
					 map.put("pointY", jsonObj.getString("PointY"));
					 map.put("pointZ", jsonObj.getString("PointZ"));
					 map.put("lotId", jsonObj.getString("LotId"));
					 mMonnitorPointList.add(map);
				 }
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mMonnitorPointList;
	}
	
	public static String getLoadingData(String jsonData) {
		String str = null;
		if (jsonData != null) {
			String mStrR = jsonData.substring(jsonData.indexOf("=") + 1,
					jsonData.indexOf(";"));
			str = mStrR.replaceAll("=", ":");
			if (str != null) {
				try {
					JSONObject json = new JSONObject(str);
					return json.get("path").toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * @category进度数据解析
	 * @param jsonData 
	 * @return  list
	 */
	public static  ArrayList<HashMap<String, String>> getProgress(String jsonData){
		ArrayList<HashMap<String, String>>list=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject   json=new JSONObject(jsonData);
			if(json!=null){
				String arrV=json.getString("v");
				if(!"".equals(arrV)){
					JSONArray    jsonArray=new JSONArray(arrV);
					 for(int i=0;i<jsonArray.length();i++){
						 HashMap<String, String> map=new HashMap<String, String>();
						 JSONObject  jsonObj=jsonArray.getJSONObject(i);
						   map.put("newId", jsonObj.getString("NewId"));
						   map.put("cycleName", jsonObj.getString("CycleName"));
						   map.put("startDate", jsonObj.getString("StartDate"));
						   map.put("endDate", jsonObj.getString("EndDate"));
						   map.put("cycleType",jsonObj.getString("CycleType"));
//						   map.put("lotId", jsonObj.getString("LotId"));
//						   map.put("remark",jsonObj.getString("Remark"));
						   list.add(map);
					 }
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 
	 * 合同段数据解析
	 * @author wanghb
	 * @LastMT 2014/06/30
	 * */
	public static HashMap<String, Object> getBaseData(String jsonData) throws JSONException {
		HashMap<String, Object> mR = new HashMap<String, Object>();
		JSONObject object = new JSONObject(jsonData);
		if(object != null) {
			String s = object.getString("s");
			mR.put("s", s);
			if("ok".equalsIgnoreCase(s)) {
				 JSONArray arry = object.getJSONArray("v");
				if(arry != null && arry.length() > 0) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					for(int i = 0, len = arry.length(); i < len; i ++) {
						JSONObject a = arry.getJSONObject(i);
						JSONArray lotsArray = a.getJSONArray("lots");
						if(lotsArray != null && lotsArray.length() > 0) {
							for(int j = 0,lenJ = lotsArray.length(); j < lenJ; j++) {
								JSONObject b = lotsArray.getJSONObject(j);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("NewId", b.getString("NewId"));
								map.put("ProjectId", b.getString("ProjectId"));
								map.put("LotName", b.getString("LotName"));
								map.put("LotCode", b.getString("LotCode"));
								map.put("CompactionUnit", b.getString("CompactionUnit"));
								map.put("SupervisorUnit", b.getString("SupervisorUnit"));
								map.put("ProjectName", b.getString("ProjectName"));
								map.put("ProjectCount", b.getString("ProjectCount"));
								map.put("ProjectArea", b.getString("ProjectArea"));
								map.put("StationArea", b.getString("StationArea"));								
								map.put("periodId", a.getString("periodId"));
								map.put("periodName", a.getString("periodName"));
								list.add(map);
							}
						}
					}
					mR.put("v", list);
				}
			}
		}
		return mR;
		
	}
	
	/**
	 * @category 监测数据上传返回结果
	 * @param jsonStr  从服务器得到的字符串
	 * @return  boolean
	 */
	public static  boolean  getCommonResult(String jsonStr){
		JSONObject json;
		try {
			json = new JSONObject(jsonStr);
			if(json!=null){
				String  mUploadResult=json.getString("s");
				if("ok".equals(mUploadResult)){
					 return true;
				}else
					 return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 进度计划
	 * @return
	 * @throws JSONException 
	 * @author wanghb
	 * @LastMT 2014/07/08
	 */
	public static HashMap<String, Object> getProgerssInfo(String jsonData) throws JSONException {
		HashMap<String, Object> mR = new HashMap<String, Object>();
		JSONObject object = new JSONObject(jsonData);
		if(object != null) {
			String s = object.getString("s");
			mR.put("s", s);
			if("ok".equalsIgnoreCase(s)) {
				 JSONArray arry = object.getJSONArray("v");
				if(arry != null && arry.length() > 0) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					for(int i = 0, len = arry.length(); i < len; i ++) {
						JSONObject a = arry.getJSONObject(i);
						JSONArray Items = a.getJSONArray("Items");
						if(Items != null && Items.length() > 0) {
							for(int j = 0,lenJ = Items.length(); j < lenJ; j ++ ) {
								HashMap<String, String> map = new HashMap<String, String>();
								JSONObject sub = Items.getJSONObject(j);
								map.put("PNewId", a.getString("Pid"));
								map.put("PConstructionName", a.getString("ConstructionName"));	
								map.put("NewId", sub.getString("NewId"));
								map.put("ConstructionName", sub.getString("ConstructionName"));
								//map.put("MeasureUnit", sub.getString("MeasureUnit"));
								map.put("Statistics", sub.getString("Statistics"));
								map.put("Sort", sub.getString("Sort"));
								map.put("DName", sub.getString("DName"));
								map.put("Pid", sub.getString("PId"));
								map.put("ConstructioType", sub.getString("ConstructioType"));
								map.put("IsEnable", sub.getString("IsEnable"));
								map.put("sortType", sub.getString("type"));//排序类型
								list.add(map);
							}
						}
					}
					mR.put("v", list);
				}
			}
		}
		return mR;
	}
	
	/**解析收文Json*/
	public static ArrayList<HashMap<String, String>> getReceiveText(String strJson){
		ArrayList<HashMap<String, String>>  mArrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject   json=new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null && !"".equals(v)){
				JSONArray arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){
					HashMap<String, String> map=new HashMap<String, String>();
					JSONObject  jsonObj=arrJson.getJSONObject(i);
					map.put("newId", jsonObj.getString("NewId"));
					map.put("theme", jsonObj.getString("Theme"));
					map.put("createTime", jsonObj.getString("CreateTime"));
					mArrayList.add(map);
				}
				v=null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return  mArrayList;
	}
	
/**解析单条收文*/
	public static HashMap<String, String> getReceiveTextSingle(String strJson){
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject json=new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null &&  !"".equals(v)){
				JSONArray arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){					
					JSONObject  jsonObj=arrJson.getJSONObject(i);
					map.put("newId", jsonObj.getString("NewId"));
					map.put("title", jsonObj.getString("Theme"));
					map.put("createTime", jsonObj.getString("CreateTime"));
					map.put("userName", jsonObj.getString("UserName"));
					if("null".equals(jsonObj.getString("Remark")) || "".equals(jsonObj.getString("Remark"))){
						map.put("remark", "");
					}else{
						map.put("remark", jsonObj.getString("Remark"));
					}
				}
				 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	/** 解析附件
	 * {"s":"ok","v":[{"NewId":"a14e7388-2ef5-4db3-b326-5e6e2b42fc84","Theme":"123",
	 * "Remark":"123","UserId":"b5a5d6cd-8bd0-441a-bf33-70fced8dadc2","CreateTime":"2014-08-26 12:00:00",
	 * "ReceiveUser":"b5a5d6cd-8bd0-441a-bf33-70fced8dadc2,101b5e6e-9622-441f-ada2-85981d184c08,11d1ef06-0bf0-4bd7-8d11-8b93f2ec672e,1dcb203c-6fee-4b29-bb0d-b17f66a7849e",
	 * "UserName":"上海同岩","AllUserName":"上海同岩,李鑫,郭建军,同岩测试",
	 * "FileList":[{"FileUrl":"http://192.168.0.222:8086/UploadFile/Docs/2014/8/26/a443bb86-6c89-49f6-b85a-c111352d297c.txt",
	 * "FileName":"photo.txt","UpdataTime":"201408291512"}]}]}
	 * */
	public static ArrayList<HashMap<String, String>> getReceiveTextFile(String strJson){
		ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json =new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null && !"".equals(v)){
				JSONArray  arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){					
					JSONObject j=arrJson.getJSONObject(i);
					if(!"[]".equals(j.getString("FileList")) && !"".equals(j.getString("FileList"))){					
						JSONArray arrJ=new JSONArray(j.getString("FileList"));
						for(int k=0;k<arrJ.length();k++){
							HashMap<String, String> map=new HashMap<String, String>();
							JSONObject jsonObj=arrJ.getJSONObject(k);
							map.put("updateTime", jsonObj.getString("UpdataTime"));
							map.put("fileName", jsonObj.getString("FileName"));
							map.put("fileUrl", jsonObj.getString("FileUrl"));
							arrayList.add(map);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	/**解析单条公告*/
	public static HashMap<String, String> getNoticeSingle(String strJson){
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject json=new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null &&  !"".equals(v)){
				JSONArray arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){					
					JSONObject  jsonObj=arrJson.getJSONObject(i);
					map.put("newId", jsonObj.getString("NewId"));
					map.put("title", jsonObj.getString("Title"));
					map.put("createTime", jsonObj.getString("Time"));
					map.put("userName", jsonObj.getString("UserName"));
					map.put("fileNumber", jsonObj.getString("FileNumber")); //文号
					map.put("dName", jsonObj.getString("DName"));//类型名称
					if("null".equals(jsonObj.getString("Content")) || "".equals(jsonObj.getString("Content"))){
						map.put("content", "");
					}else{
						map.put("content", jsonObj.getString("Content"));//Content
					}
				}
				 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	/** 解析公告信息*/
	public static ArrayList<HashMap<String, String>> getNotice(String strJson){
		ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json = new JSONObject(strJson);
			String  v=json.getString("v");
			if(v!=null && !"".equals(v)){
				JSONArray  arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){
					HashMap<String, String> map=new HashMap<String, String>();
					JSONObject jsonObj=arrJson.getJSONObject(i);
					map.put("newId",jsonObj.getString("NewId"));
					map.put("title", jsonObj.getString("Title"));
					map.put("content", jsonObj.getString("Content"));
					map.put("time", jsonObj.getString("Time"));
					list.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	/** 解析规章制度信息*/
	public static ArrayList<HashMap<String, String>> getRules(String strJson){
		ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null && !"".equals(v)){
				JSONArray arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){
					HashMap<String, String> map=new HashMap<String, String>();
					JSONObject jsonObj=arrJson.getJSONObject(i);
					map.put("newId",jsonObj.getString("NewId"));
					map.put("title", jsonObj.getString("Title"));
					map.put("content", jsonObj.getString("Content"));
					map.put("time", jsonObj.getString("Time"));
					list.add(map);
					}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**解析单条规章制度信息*/
	public static HashMap<String, String> getRulesSingle(String strJson){
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject json=new JSONObject(strJson);
			String v=json.getString("v");
			if(v!=null &&  !"".equals(v)){
				JSONArray arrJson=new JSONArray(v);
				for(int i=0;i<arrJson.length();i++){					
					JSONObject  jsonObj=arrJson.getJSONObject(i);
					map.put("title", jsonObj.getString("Title"));
					map.put("createTime", jsonObj.getString("Time"));
					map.put("userName", jsonObj.getString("UserName"));
//					map.put("type", jsonObj.getString("Type")); //类别
					map.put("dName", jsonObj.getString("DName"));//类型名称
					if("null".equals(jsonObj.getString("Content")) || "".equals(jsonObj.getString("Content"))){
						map.put("content", "");
					}else{
						map.put("content", jsonObj.getString("Content"));//Content
					}
				}
				 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取单位性质
	 * @param strJson
	 * @return
	 * @throws JSONException
	 * @author wanghb
	 * @date 2014/07/29
	 */
	public static ArrayList<HashMap<String, String>> getDptNature(String strJson) throws JSONException {
		JSONObject json = new JSONObject(strJson);
		if(json != null) {
			String s = json.getString("s");
			if("ok".equalsIgnoreCase(s)) {
				 JSONArray arry = json.getJSONArray("v");
				if(arry != null && arry.length() > 0) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					for(int i = 0, len = arry.length(); i < len; i ++) {
						JSONObject jsonObj = arry.getJSONObject(i);
						HashMap<String, String> map=new HashMap<String, String>();
						map.put("NewId",jsonObj.getString("NewId"));
						map.put("DName", jsonObj.getString("DName"));
						map.put("DValue", jsonObj.getString("DValue"));
						
						map.put("SysId", jsonObj.getString("SysId"));
						map.put("DType", jsonObj.getString("DType"));
						map.put("DSort", jsonObj.getString("DSort"));
						list.add(map);
					}
					return list;
				}
			}
		}
		return null;
	}
	/**
	 * 
	 * @param strJson
	 * @return
	 * @throws JSONException
	 * @author wanghb
	 * @date 2014/07/30
	 */
	public static ArrayList<HashMap<String, String>> getContacts(String strJson) throws JSONException {
		JSONObject json = new JSONObject(strJson);
		if(json != null) {
			String s = json.getString("s");
			if("ok".equalsIgnoreCase(s)) {
				 JSONArray arry = json.getJSONArray("v");
				if(arry != null && arry.length() > 0) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					for(int i = 0, len = arry.length(); i < len; i ++) {
						JSONObject jsonObj = arry.getJSONObject(i);
						HashMap<String, String> map=new HashMap<String, String>();
						String mUserName = jsonObj.getString("UserName");
						String mDeptName = jsonObj.getString("DeptName");
						
						map.put("UserName", mUserName);
						map.put("DeptName", mDeptName);
						map.put("UserSex", jsonObj.getString("UserSex"));
						
						map.put("UserNamePinyin", new PingyinUtils().converterToPinYin(mUserName));
						map.put("DeptNamePinyin", new PingyinUtils().converterToPinYin(mDeptName));
						
						map.put("UserPhone",jsonObj.getString("UserPhone"));
						map.put("UserEmail", jsonObj.getString("UserEmail"));
						map.put("UserQQ", jsonObj.getString("UserQQ"));
						map.put("UserId", jsonObj.getString("UserId"));
						map.put("DeptId", jsonObj.getString("DeptId"));
						
						list.add(map);
					}
					return list;
				}
			}
		}
		return null;
	}
	/**
	 * 获取日程列表
	 * @param strJson
	 * @throws JSONException
	 * @author wanghb
	 * @date 2014/08/07
	 * @return ArrayList<HashMap<String, String>>
	 * {"s":"ok","v":[{"NewId":"aedf4dfd-7723-4f6f-9825-1d2ca6636e0b","UserId":"1dcb203c-6fee-4b29-bb0d-b17f66a7849e",
	 * "WarnTime":"2014-08-07 12:00:00","WorkTime":"2014-08-07 12:00:00","Title":"33434","Content":"3334444",
	 * "IsDay":false,"Type":2,"NoticeTime":"2014-08-06 12:00:00","UserName":null,"UserPhone":null,"UserEmail":null,"IsRepeat":false,
	 * "NoticeStaff":[{"UserId":"dcad180a-8fb1-4458-aed2-2040a9c35658","UserName":"zkg"},
	 * {"UserId":"1d7c16ea-ef3b-4a9e-8dcb-9a1f8e4f27f4","UserName":"鼎正人员1"},
	 * {"UserId":"c737b75b-56f8-47c3-bbf7-1c3ef80cd2ba","UserName":"翟哲 "}]},
	 * {"NewId":"4307c0e1-6bf7-4dcc-b554-1eae97b85b51","UserId":"1dcb203c-6fee-4b29-bb0d-b17f66a7849e","WarnTime":"2014-08-07 05:31:04",
	 * "WorkTime":"2014-08-07 05:31:02","Title":"fyyft","Content":"dggu","IsDay":true,"Type":2,"NoticeTime":"2014-08-07 05:31:07",
	 * "UserName":null,"UserPhone":null,"UserEmail":null,"IsRepeat":false,
	 * "NoticeStaff":[{"UserId":"1dcb203c-6fee-4b29-bb0d-b17f66a7849e","UserName":"同岩测试"}]}]}
	 */
	public static HashMap<String, Object> getScheduleList(String strJson) throws JSONException {
		HashMap<String, Object> mBackMap = new HashMap<String, Object>();
		JSONObject json = new JSONObject(strJson);
		if(json != null) {
			String s = json.getString("s");
			if("ok".equalsIgnoreCase(s)) {
				 JSONArray arry = json.getJSONArray("v");
				 if(arry != null && arry.length() > 0) {
						ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
						HashMap<String, ArrayList<HashMap<String, Object>>> mCacheMap = new HashMap<String, ArrayList<HashMap<String, Object>>>();
						for(int i = 0, len = arry.length(); i < len; i ++) {
							JSONObject jsonObj = arry.getJSONObject(i);
							String mStartTime = jsonObj.getString("WorkTime");
							HashMap<String, Object> map=new HashMap<String, Object>();
							map.put("NewId", jsonObj.getString("NewId"));
							map.put("UserId", jsonObj.getString("UserId"));
							map.put("WarnTime", jsonObj.getString("WarnTime"));
							map.put("WorkTime", mStartTime);//日程开始时间
							map.put("Title", jsonObj.getString("Title"));
							map.put("Content", jsonObj.getString("Content"));
							map.put("IsDay", jsonObj.getString("IsDay"));
							map.put("Type", jsonObj.getString("Type"));
							map.put("NoticeTime", jsonObj.getString("NoticeTime"));
							map.put("UserName", jsonObj.getString("UserName"));
							map.put("UserPhone", jsonObj.getString("UserPhone"));
							map.put("UserEmail", jsonObj.getString("UserEmail"));
							map.put("IsRepeat", jsonObj.getString("IsRepeat"));
							//NoticeStaff
							ArrayList<HashMap<String, String>> staffList = new ArrayList<HashMap<String, String>>();
							JSONArray arryStaff = jsonObj.getJSONArray("NoticeStaff");
							if(arryStaff != null && arryStaff.length() > 0) {
								for(int j = 0 ; j < arryStaff.length(); j ++) {
									JSONObject jsonStaff = arryStaff.getJSONObject(j);
									HashMap<String, String> staffMap = new HashMap<String, String>();
									staffMap.put("UserId", jsonStaff.getString("UserId"));
									staffMap.put("UserName", jsonStaff.getString("UserName"));
									staffList.add(staffMap);
								}
							}
							map.put("NoticeStaff", staffList);
							list.add(map);
							try {
							String mMapKey = "";
							if(mStartTime != null) {
								Date mStartDate = null;
								if(mStartTime.length() > 10) {
									mStartDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(mStartTime);
								} else {
									mStartDate = new SimpleDateFormat("yyyy/MM/dd").parse(mStartTime);
								}
								Calendar mStartCalendar = Calendar.getInstance();
								mStartCalendar.setTime(mStartDate);	
								int day = mStartCalendar.get(Calendar.DAY_OF_MONTH);
								mMapKey = day < 10 ? "0" + day :String.valueOf(day);
							}
							ArrayList<HashMap<String, Object>> mCacheList = mCacheMap.get(mMapKey);
							if(mCacheMap.get(mMapKey) == null) {
								mCacheList = new ArrayList<HashMap<String, Object>>();
							}
							mCacheList.add(map);
							mCacheMap.put(mMapKey, mCacheList);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						mBackMap.put("AllList", list);
						mBackMap.put("AllMap", mCacheMap);
				 }
			}
		}
		return mBackMap;
	}
	
	/**所有原地貌类型解析*/
	public static ArrayList<HashMap<String, String>> getAllOriginal(String strJson){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject  json=new JSONObject(strJson);
			String str=json.getString("s");
			if("ok".equalsIgnoreCase(str)){
				JSONArray  arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					JSONObject  jsonObj=arrJson.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("newId", jsonObj.getString("NewId"));
					map.put("displayName", jsonObj.getString("DisplayName"));
					arrayList.add(map);
 				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	/** 解析单条原地貌数据*/
	public static ArrayList<HashMap<String, String>> getOriginalSingle(String strJson){
		ArrayList<HashMap<String, String >>  mArrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			String  str=json.getString("s");
			if("ok".equalsIgnoreCase(str)){
				JSONArray  arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					JSONObject jsonObj=arrJson.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("newId", jsonObj.getString("NewId"));
					if(jsonObj.getString("OriginalName")==null || "null".equals(jsonObj.getString("OriginalName"))){
						map.put("text", "");
					}else{						
						map.put("text",jsonObj.getString("OriginalName"));
					}
					map.put("pointX", jsonObj.getString("PointX"));
					map.put("pointY", jsonObj.getString("PointY"));
					if("null".equals(jsonObj.getString("PointZ")) || jsonObj.getString("PointZ")==null){
						map.put("pointZ", "");
					}else{
						map.put("pointZ", jsonObj.getString("PointZ"));
					}
					mArrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mArrayList;
	}
	/**
	 * 强夯类型获取
	 */
	public static  ArrayList<HashMap<String, String>>  getCompaction(String strJson){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray  arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					JSONObject jsonObj=arrJson.getJSONObject(i);;
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("newId", jsonObj.getString("NewId"));
					map.put("text", jsonObj.getString("CType"));
					arrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
 	}
	
	/** 强夯点获取*/
	public static ArrayList<HashMap<String, String>> getCompactionPoint(String strJson){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
					JSONArray arrJson=new JSONArray(json.getString("v"));
					for(int i=0;i<arrJson.length();i++){
					    JSONObject	 jsonObj=arrJson.getJSONObject(i);
					    HashMap<String, String> map=new HashMap<String,String>();
					    map.put("newId", jsonObj.getString("NewId"));
					    map.put("text", jsonObj.getString("CompactionName"));
					    map.put("type", jsonObj.getString("CType"));
					    map.put("area", jsonObj.getString("Area"));
					    arrayList.add(map);
					}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	/** 盲沟类型解析*/
	public static ArrayList<HashMap<String, String>> getGutterType(String strJson){
		ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					JSONObject jsonObj=arrJson.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("newId", jsonObj.getString("NewId"));
					map.put("text", jsonObj.getString("TypeName"));
					arrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	/**根据盲沟类型获取盲沟 */
	public static ArrayList<HashMap<String, String>> getGutter(String strJson){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject  json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray  arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					HashMap<String, String> map=new HashMap<String, String>();
					JSONObject jsonObj=arrJson.getJSONObject(i);
					map.put("newId", jsonObj.getString("NewId"));
					map.put("typeName", jsonObj.getString("GutterName"));
					map.put("startPointX", jsonObj.getString("StartPointX"));
					map.put("startPointY", jsonObj.getString("StartPointY")); //StartPointZ
					if("".equals(jsonObj.getString("StartPointZ")) || "null".equals(jsonObj.getString("StartPointZ"))){
						map.put("startPointZ", "");
					}else{						
						map.put("startPointZ", jsonObj.getString("StartPointZ"));
					}
					map.put("endPointX", jsonObj.getString("EndPointX"));
					map.put("endPointY", jsonObj.getString("EndPointY"));
					if("".equals(jsonObj.getString("StartPointZ")) || "null".equals(jsonObj.getString("StartPointZ"))){
						map.put("endPointZ","");
					}else{						
						map.put("endPointZ", jsonObj.getString("EndPointZ"));
					}
					arrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	/** 定点拍照获取*/
	public static ArrayList<HashMap<String, String>> getPointPhoto(String strJson){
		ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray arrayJson=new  JSONArray(json.getString("v"));
				for(int i=0;i<arrayJson.length();i++){
					HashMap<String, String> map=new HashMap<String, String>();
					JSONObject jsonObj=arrayJson.getJSONObject(i);
					map.put("newId", jsonObj.getString("NewId"));
					map.put("pointName", jsonObj.getString("PointName"));
					map.put("pointX", jsonObj.getString("PointX"));
					map.put("pointY", jsonObj.getString("PointY"));
					if("".equals(jsonObj.getString("PointZ")) || "null".equals(jsonObj.getString("PointZ"))){ 
						map.put("pointZ", "");
					}else{
					map.put("pointZ", jsonObj.getString("PointZ"));
				}
					mArrayList.add(map);
			}
	 	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return mArrayList;
	}
	/**
	 * @author Rubert
	 * @param strJson
	 * @date 2014/08/14
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> getDetectionArea(String strJson){
		ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject json = new JSONObject(strJson);
			if ("ok".equalsIgnoreCase(json.getString("s"))) {
				JSONArray arrayJson = new JSONArray(json.getString("v"));
				for (int i = 0; i < arrayJson.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonObj = arrayJson.getJSONObject(i);
					map.put("NewId", jsonObj.getString("NewId"));
					map.put("ChangeName", jsonObj.getString("ChangeName"));
					map.put("ChangeTime", jsonObj.getString("ChangeTime"));
					map.put("attribute", "title");
					mArrayList.add(map);
					JSONArray ary = jsonObj.getJSONArray("AreaPoint");
					if (ary != null && ary.length() > 0) {
						for (int j = 0; j < ary.length(); j++) {
							HashMap<String, String> m = new HashMap<String, String>();
							JSONObject aryJ = ary.getJSONObject(j);
							m.put("NewId", aryJ.getString("NewId"));
							m.put("AreaName", aryJ.getString("AreaName"));
							m.put("ChangeName", jsonObj.getString("ChangeName"));
							m.put("ChangeTime", jsonObj.getString("ChangeTime"));
							m.put("attribute", "content");
							mArrayList.add(m);
						}
					}
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mArrayList;
	}
	
	/**
	 * @author Rubert
	 * @param strJson
	 * @date 2014/08/14
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> getDetectionSubArea(String strJson){
		ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject json = new JSONObject(strJson);
			if ("ok".equalsIgnoreCase(json.getString("s"))) {
				JSONArray arrayJson = new JSONArray(json.getString("v"));
				for (int i = 0; i < arrayJson.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonObj = arrayJson.getJSONObject(i);
					map.put("NewId", jsonObj.getString("NewId"));
					map.put("AreaName", jsonObj.getString("AreaName"));
					map.put("attribute", "content");
					mArrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mArrayList;
	}
	
	
	/** 解析合同段信息的数量*/
	public static  ArrayList<HashMap<String, String>> getLotNumber(String strJson){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try {
			JSONObject json=new JSONObject(strJson);
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray arrJson=new JSONArray(json.getString("v"));
				for(int i=0;i<arrJson.length();i++){
					JSONObject jsonObj=arrJson.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("dataCount", jsonObj.getString("DataCount"));
					map.put("lotId", jsonObj.getString("LotId"));
					arrayList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	/** 解析版本更新*/
	public static HashMap<String, String> getVersion(String strJson){
		HashMap<String, String> map=new HashMap<String, String>();
		JSONObject json=null;
		try {
			json = new JSONObject(strJson);
			JSONArray arrJson=new JSONArray(json.getString("v"));
			for(int i=0;i<arrJson.length();i++){
				JSONObject jsonObj=arrJson.getJSONObject(i);
				map.put("version", jsonObj.getString("VersionId"));
				map.put("fileName", jsonObj.getString("VersionName"));
				map.put("url", jsonObj.getString("VersionUrl"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
