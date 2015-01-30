package com.tongyan.common.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Paint.Join;
import android.text.Html;
import android.util.Log;

import com.baidu.mapapi.search.core.PoiInfo.POITYPE;
import com.tongyan.common.entities._Agendas;
import com.tongyan.common.entities._Contacts;
import com.tongyan.common.entities._ContactsData;
import com.tongyan.common.entities._ContactsEmp;
import com.tongyan.common.entities._GpsEmp;
import com.tongyan.common.entities._HoleFace;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._Item;
import com.tongyan.common.entities._ItemSec;
import com.tongyan.common.entities._LocInfo;
import com.tongyan.common.entities._LocationInfo;
import com.tongyan.common.entities._Message;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._ProjectLoc;
import com.tongyan.common.entities._RiskInfo;
import com.tongyan.common.entities._RiskNotice;
import com.tongyan.common.entities._Section;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.PingyinUtils;
import com.tongyan.utils.ProcessOperation;


/**
 * @ClassName Str2Json 
 * @author wanghb
 * @date 2013-7-17 pm 02:00:58
 * @desc TODO
 */
public class Str2Json {
	/**
	 * common method
	 * @param jsonStr
	 * @param mType
	 * @return
	 * @throws JSONException
	 */
	//AddResponse{AddResult={"s":"ok","v":null}; }
	public JSONObject getHandlerJSON(String jsonStr,String mType) throws JSONException {
			jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}")+1);
			jsonStr = jsonStr.replaceAll(mType + "=", mType + ":");
			jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}")-2) + "}";
		return new JSONObject(jsonStr).getJSONObject(mType);//进行数据处理
	}
	
	/**
	 * Login
	 */
	public Map<String,String> getLoginInfo(String jsonStr) throws Exception {
		jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}")+1);
		jsonStr = jsonStr.replaceAll("=", ":");
		jsonStr = jsonStr.replaceAll(";", "");
		JSONObject jsonObj = new JSONObject(jsonStr).getJSONObject("LoginResult");// 进行数据处理
	    Map<String,String> map = new HashMap<String,String>();
		String loginType = jsonObj.getString("s");
		map.put("succ", loginType);
		
		if("ok".equals(loginType)) {
			JSONObject jsonObj2 = new JSONObject(jsonObj.getString("v"));
			String dptRowId = jsonObj2.getString("dptRowId");
			String empId = jsonObj2.getString("empId");
			String dptName = jsonObj2.getString("dptName");
			String empName = jsonObj2.getString("empName");
			String empLevel = jsonObj2.getString("empLevel");
			
			map.put("dptRowId", dptRowId);
			map.put("empId", empId);
			map.put("dptName", dptName);
			map.put("empName", empName);
			map.put("empLevel", empLevel);
		}
		return map;
	}
	/**
	 * 用户权限
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUserRoute(String jsonStr) throws Exception {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonRoute = (JSONObject)jsonAry.get(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("menu", jsonRoute.getString("menu"));
					map.put("role", jsonRoute.getString("role"));
					list.add(map);
				}
				mR.put("v", list);
			}
		}
		return mR;
	}
	
	
	/**
	 * 工程定位
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 * GetListNoPageResponse{GetListNoPageResult={"s":"ok","v":[
	 * {"subId":"152fbae9-bc15-43c5-b9dc-bb3c1b2566db",
	 *  "subCode":"B6.0.101.402.03.002",
	 *  "subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板涵\\K103+735盖板涵",
	 *  "distance":650109.21,
	 *  "latlngs":[{"Lat":27.610175,"Lng":115.752932},
	 * {"Lat":27.610275,"Lng":115.753032}]},
	 * {"subId":"5d838d5c-1985-4947-9f7e-48fb70da0afa","subCode":"B6.0.101.404.01.003","subName":"B6\\主线\\路基工程\\涵洞、通道（线外涵洞、通道）\\圆管涵\\K99+080~K100+790整修原有道路圆管涵",
	 * "distance":650227.92,"latlngs":[{"Lat":27.606167,"Lng":115.754011},{"Lat":27.606267,"Lng":115.754111}]},
	 * {"subId":"c87c0ac2-a662-4acf-841c-ab9e4c9d11cf","subCode":"B6.0.101.101","subName":"B6\\主线\\路基工程\\K99+080～K102+000路基土石方工程","distance":650241.06,"latlngs":[{"Lat":27.606167,"Lng":115.754011},{"Lat":27.580229,"Lng":115.755927}]},{"subId":"cfcc9f6f-9610-4661-93ce-f413ca796afd","subCode":"B6.0.101.201","subName":"B6\\主线\\路基工程\\K99+080～K102+000排水工程","distance":650241.06,"latlngs":[{"Lat":27.606167,"Lng":115.754011},{"Lat":27.580229,"Lng":115.755927}]},{"subId":"d17e347d-5d32-4862-a198-7d4dab5c566f","subCode":"B6.0.101.401.03.001","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板涵\\K99+220盖板涵","distance":650320.26,"latlngs":[{"Lat":27.604945,"Lng":115.75364},{"Lat":27.605045,"Lng":115.75374}]},{"subId":"71d46697-8ed1-486d-9f9a-a5d71310951d","subCode":"B6.0.101.401.01.001","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\圆管涵\\K99+320圆管涵","distance":650383.65,"latlngs":[{"Lat":27.604063,"Lng":115.753413},{"Lat":27.604163,"Lng":115.753513}]},{"subId":"583698b4-f67b-4974-9ddc-682c50704ff0","subCode":"B6.0.101.401.05.001","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板通道\\K99+960盖板通道","distance":650730.73,"latlngs":[{"Lat":27.598323,"Lng":115.752754},{"Lat":27.598423,"Lng":115.752854}]},{"subId":"72f8db7d-c726-49ac-89bd-503f5c218389","subCode":"B6.0.101.404.01.001","subName":"B6\\主线\\路基工程\\涵洞、通道（线外涵洞、通道）\\圆管涵\\K0+200主线K99+960改路圆管涵","distance":650730.73,"latlngs":[{"Lat":27.598323,"Lng":115.752754},{"Lat":27.598423,"Lng":115.752854}]},{"subId":"eff967e7-54e0-4b5a-937c-b47df1b288dc","subCode":"B6.0.101.401.05.002","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板通道\\K100+100盖板通道","distance":650792.40,"latlngs":[{"Lat":27.59706,"Lng":115.752793},{"Lat":27.59716,"Lng":115.752893}]},{"subId":"a8d6fe84-b58c-48df-bae7-2057397b1c4a","subCode":"B6.0.101.401.03.002","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板涵\\K100+188盖板涵","distance":650828.31,"latlngs":[{"Lat":27.596268,"Lng":115.752852},{"Lat":27.596368,"Lng":115.752952}]},{"subId":"7d42518e-3c07-4e34-9a5f-eb4b7283dd88","subCode":"B6.0.101.404.01.004","subName":"B6\\主线\\路基工程\\涵洞、通道（线外涵洞、通道）\\圆管涵\\K100+790~K101+240整修原有道路圆管涵","distance":651014.67,"latlngs":[{"Lat":27.59094,"Lng":115.753941},{"Lat":27.59104,"Lng":115.754041}]},{"subId":"9ae308ed-8e89-4ec4-a5bb-b593a2d661a9","subCode":"B6.0.101.401.01.002","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\圆管涵\\K101+000圆管涵","distance":651060.96,"latlngs":[{"Lat":27.589123,"Lng":115.754529},{"Lat":27.589223,"Lng":115.754629}]},{"subId":"b16dacc6-a39a-4fe0-b06c-e77bd4d3d83b","subCode":"B6.0.304.101","subName":"B6\\主线\\耿元大桥（K101+307）\\0#台基础及下部构造","distance":651124.46,"latlngs":[{"Lat":27.586882,"Lng":115.755175},{"Lat":27.586982,"Lng":115.755275}]},{"subId":"624f3135-8668-4e03-a4f1-cac8f0f9cd10","subCode":"B6.0.304.102","subName":"B6\\主线\\耿元大桥（K101+307）\\1#墩基础及下部构造","distance":651130.15,"latlngs":[{"Lat":27.586706,"Lng":115.755217},{"Lat":27.586806,"Lng":115.755317}]},{"subId":"a7861d14-edc0-4db8-9cb1-fdc625d68241","subCode":"B6.0.304.103","subName":"B6\\主线\\耿元大桥（K101+307）\\2#墩基础及下部构造","distance":651136.05,"latlngs":[{"Lat":27.586529,"Lng":115.755257},{"Lat":27.586629,"Lng":115.755357}]},{"subId":"eb2d232e-bd1d-48fe-a6e5-2ca7061e73a8","subCode":"B6.0.304.104","subName":"B6\\主线\\耿元大桥（K101+307）\\3#墩基础及下部构造","distance":651141.90,"latlngs":[{"Lat":27.586353,"Lng":115.755297},{"Lat":27.586453,"Lng":115.755397}]},{"subId":"7b19d730-0e93-412f-b129-482183f4f6a3","subCode":"B6.0.304.105","subName":"B6\\主线\\耿元大桥（K101+307）\\4#墩基础及下部构造","distance":651148.04,"latlngs":[{"Lat":27.586176,"Lng":115.755334},{"Lat":27.586276,"Lng":115.755434}]},{"subId":"086cb49b-fd6d-4381-8579-6bbada74dc01","subCode":"B6.0.304.106","subName":"B6\\主线\\耿元大桥（K101+307）\\5#台基础及下部构造","distance":651154.24,"latlngs":[{"Lat":27.585998,"Lng":115.755371},{"Lat":27.586098,"Lng":115.755471}]},{"subId":"51ce2c53-86ca-4e7b-a413-36e7a2d9541a","subCode":"B6.0.101.401.01.003","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\圆管涵\\K101+460圆管涵","distance":651187.78,"latlngs":[{"Lat":27.585083,"Lng":115.75554},{"Lat":27.585183,"Lng":115.75564}]},{"subId":"ad58573d-ae23-435d-97d9-bfb16c30fdaf","subCode":"B6.0.101.401.05.003","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板通道\\K101+580盖板通道","distance":651230.82,"latlngs":[{"Lat":27.58401,"Lng":115.755692},{"Lat":27.58411,"Lng":115.755792}]},{"subId":"e1ca3e02-6747-4e17-9b4a-43bde544b27b","subCode":"B6.0.101.401.05.004","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板通道\\K101+705盖板通道","distance":651279.57,"latlngs":[{"Lat":27.582887,"Lng":115.755805},{"Lat":27.582987,"Lng":115.755905}]},{"subId":"5c1bc421-44f3-40ec-a63d-e1d93881c3aa","subCode":"B6.0.101.401.03.003","subName":"B6\\主线\\路基工程\\K99+080～K102+000涵洞、通道\\盖板涵\\K101+960盖板涵","distance":651388.45,"latlngs":[{"Lat":27.58059,"Lng":115.755922},{"Lat":27.58069,"Lng":115.756022}]},{"subId":"95f70c2b-2416-4588-8b68-5be4231b9c82","subCode":"B6.0.101.202","subName":"B6\\主线\\路基工程\\K102+000～K105+000排水工程","distance":651419.79,"latlngs":[{"Lat":27.580229,"Lng":115.755927},{"Lat":27.553681,"Lng":115.750416}]},{"subId":"c027dbdb-897a-46d6-b205-91a5905f1f75","subCode":"B6.0.101.102","subName":"B6\\主线\\路基工程\\K102+000～K105+000路基土石方工程","distance":651419.79,"latlngs":[{"Lat":27.580229,"Lng":115.755927},{"Lat":27.553681,"Lng":115.750416}]},{"subId":"e7f77378-9cb1-4732-9940-a617c07c1c68","subCode":"B6.0.305.101","subName":"B6\\主线\\大坑中桥（K102+111）\\0#台基础及下部构造","distance":651439.73,"latlngs":[{"Lat":27.579588,"Lng":115.755926},{"Lat":27.579688,"Lng":115.756026}]},{"subId":"d98f2b68-a5c3-4321-9ba9-b8ee278a16c9","subCode":"B6.0.305.102","subName":"B6\\主线\\大坑中桥（K102+111）\\1#墩基础及下部构造","distance":651449.13,"latlngs":[{"Lat":27.579407,"Lng":115.755925},{"Lat":27.579507,"Lng":115.756025}]},{"subId":"b83eb341-46cd-40f7-b572-1a1c5b49d47f","subCode":"B6.0.305.103","subName":"B6\\主线\\大坑中桥（K102+111）\\2#墩基础及下部构造","distance":651458.64,"latlngs":[{"Lat":27.579227,"Lng":115.755922},{"Lat":27.579327,"Lng":115.756022}]},{"subId":"3243b341-8c31-4bba-bc30-bdf5fb385a7e","subCode":"B6.0.305.104","subName":"B6\\主线\\大坑中桥（K102+111）\\3#墩基础及下部构造","distance":651468.23,"latlngs":[{"Lat":27.579047,"Lng":115.755918},{"Lat":27.579147,"Lng":115.756018}]},{"subId":"22b98f55-5ae5-4bd7-828f-84ed4e9a5ea2","subCode":"B6.0.305.105","subName":"B6\\主线\\大坑中桥（K102+111）\\4#台基础及下部构造","distance":651477.75,"latlngs":[{"Lat":27.57887,"Lng":115.755913},{"Lat":27.57897,"Lng":115.756013}]},{"subId":"1d6814a4-5dae-4d28-bea6-43d39060e659","subCode":"B6.0.101.402.03.001","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板涵\\K102+620盖板涵","distance":651724.71,"latlngs":[{"Lat":27.574645,"Lng":115.755547},{"Lat":27.574745,"Lng":115.755647}]},{"subId":"498fb7a5-76f8-4752-b803-da5515389688","subCode":"B6.0.101.402.05.001","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K102+780盖板通道","distance":651817.38,"latlngs":[{"Lat":27.573215,"Lng":115.75531},{"Lat":27.573315,"Lng":115.75541}]},{"subId":"88ad18c2-062f-49c0-910e-a6dca4afb5f0","subCode":"B6.0.101.404.01.005","subName":"B6\\主线\\路基工程\\涵洞、通道（线外涵洞、通道）\\圆管涵\\K102+820~K103+560整修原有道路圆管涵","distance":651841.17,"latlngs":[{"Lat":27.572859,"Lng":115.755242},{"Lat":27.572959,"Lng":115.755342}]},{"subId":"35a92393-6456-48f4-a14f-2ec695dfa702","subCode":"B6.0.101.402.05.002","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K102+970盖板通道","distance":651932.80,"latlngs":[{"Lat":27.571527,"Lng":115.754955},{"Lat":27.571627,"Lng":115.755055}]},{"subId":"7c6a66c1-5b0a-4062-8a05-d31bbf430dfe","subCode":"B6.0.101.402.05.003","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K103+432盖板通道","distance":652236.42,"latlngs":[{"Lat":27.56749,"Lng":115.753762},{"Lat":27.56759,"Lng":115.753862}]},{"subId":"eab9ab70-a94c-4af8-bfff-467eeb7847ca","subCode":"B6.0.101.402.05.004","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K103+675盖板通道","distance":652405.39,"latlngs":[{"Lat":27.565402,"Lng":115.752996},{"Lat":27.565502,"Lng":115.753096}]},{"subId":"54e810d5-7cc4-411c-bbd2-6b6f8c5f1ca7","subCode":"B6.0.101.402.05.005","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K103+900盖板通道","distance":652558.92,"latlngs":[{"Lat":27.563456,"Lng":115.752332},{"Lat":27.563556,"Lng":115.752432}]},{"subId":"353cf5ff-9267-48d7-973e-34f534f6dee4","subCode":"B6.0.101.402.03.003","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板涵\\K104+500盖板涵","distance":652931.30,"latlngs":[{"Lat":27.558154,"Lng":115.751099},{"Lat":27.558254,"Lng":115.751199}]},{"subId":"6d4abdc8-d5d9-49a0-b2e3-561a7c572083","subCode":"B6.0.101.402.05.006","subName":"B6\\主线\\路基工程\\K102+000～K105+000涵洞、通道\\盖板通道\\K104+940盖板通道","distance":653181.91,"latlngs":[{"Lat":27.554216,"Lng":115.75051},{"Lat":27.554316,"Lng":115.75061}]},{"subId":"0b1a31f4-02af-4594-9c4d-4aea70e02c89","subCode":"B6.0.101.103","subName":"B6\\主线\\路基工程\\K105+000～K107+620路基土石方工程","distance":653230.24,"latlngs":[{"Lat":27.553681,"Lng":115.750416},{"Lat":27.532402,"Lng":115.739263}]},{"subId":"f35500b8-b1d7-4371-bf11-7cd32de9b95e","subCode":"B6.0.101.203","subName":"B6\\主线\\路基工程\\K105+000～K107+620排水工程","distance":653230.24,"latlngs":[{"Lat":27.553681,"Lng":115.750416},{"Lat":27.532402,"Lng":115.739263}]},{"subId":"3063d6cb-4a32-4b63-a66d-4db5ad9363f9","subCode":"B6.0.101.403.01.001","subName":"B6\\主线\\路基工程\\K105+000～K107+620涵洞、通道\\圆管涵\\K105+107圆管涵","distance":653282.06,"latlngs":[{"Lat":27.552732,"Lng":115.750217},{"Lat":27.552832,"Lng":115.750317}]},{"subId":"f75518d4-ea7c-4324-9912-746149b6a36d","subCode":"B6.0.101.403.05.001","subName":...
	 */
	public Map<String,Object> getProjectLocList(String jsonStr) throws Exception {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonObj2 = (JSONObject)jsonAry.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					if(jsonObj2 != null) {
						map.put("secId", jsonObj2.getString("secId"));
						map.put("subId", jsonObj2.getString("subId"));
						map.put("subCode", jsonObj2.getString("subCode"));
						map.put("subName", jsonObj2.getString("subName"));
						map.put("distance", jsonObj2.getString("distance"));
						map.put("index", String.valueOf(i));
					}
					JSONArray jsonSubAry = new JSONArray(jsonObj2.getString("latlngs"));
					if(jsonSubAry != null && jsonSubAry.length() > 0) {
						ArrayList<HashMap<String,String>> locList = new ArrayList<HashMap<String, String>>();
						for(int j = 0; j < jsonSubAry.length(); j++) {
							HashMap<String, String> locMap = new HashMap<String, String>();
							JSONObject jsonSubObj = (JSONObject)jsonSubAry.get(j);
							if(jsonSubObj != null) {
								locMap.put("Lat", jsonSubObj.getString("Lat"));
								locMap.put("Lng", jsonSubObj.getString("Lng"));
								locList.add(locMap);
							}
						}
						map.put("latlngs", locList);
					}
					list.add(map);
				}
				mR.put("v", list);
			}
		}
		return mR;
	}
	
	
	/**
	 * Contacts
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getLinkManInfo(String jsonStr) throws Exception {
		//处理json字符串
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Contacts> mContactsList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetLinkManResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				
				List<String> pingyinList = new ArrayList<String>();
				mContactsList = new ArrayList<_Contacts>();
				for(int i = 0; i < jsonAry.length(); i ++) {
					_Contacts _contacts = new _Contacts();
					JSONObject jsonDpt = (JSONObject)jsonAry.get(i);
					String dptName = jsonDpt.getString("dptName");
					String dptTel = jsonDpt.getString("dptTel");
					String dptFax = jsonDpt.getString("dptFax");
					
					String dptNamePinyin = new PingyinUtils().converterToPinYin(dptName);
					
					if(pingyinList.contains(dptNamePinyin)) {
						pingyinList.add(dptNamePinyin + i);
						_contacts.setDptNamePinyin(dptNamePinyin + i);
					} else {
						pingyinList.add(dptNamePinyin);
						_contacts.setDptNamePinyin(dptNamePinyin);
					}
					
					_contacts.setDptFax(dptFax);
					_contacts.setDptName(dptName);
					_contacts.setDptTel(dptTel);
					
					JSONArray subJsonAry = new JSONArray(jsonDpt.getString("empList"));
					if(subJsonAry != null && subJsonAry.length() > 0) {
						for(int j = 0; j < subJsonAry.length(); j ++) {
							_ContactsEmp emp = new _ContactsEmp();
							JSONObject jsonEmp = (JSONObject)subJsonAry.get(j);
							String empName = jsonEmp.getString("empName");
							String empContact = jsonEmp.getString("empContact");
							String empPinyin = jsonEmp.getString("empPinyin");
							String rowId = jsonEmp.getString("rowId");
							
							emp.setEmpName(empName);
							emp.setEmpContact(empContact);
							emp.setEmpPinyin(empPinyin);
							emp.setRowId(rowId);
							
							_contacts.getEmpList().add(emp);
						}
					}
					
					mContactsList.add(_contacts);
					
				}
			}
		}
		mR.put("v", mContactsList);
		return mR;
	}
	/**
	 * 公告列表
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getNoticeList(String jsonStr) throws Exception {
		
		Map<String,Object> mR = new HashMap<String,Object>(); 
		List<_Message> msgList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				int rows = jsonO.getInt("rows");
				mR.put("rows", rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					msgList = new ArrayList<_Message>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Message msg = new _Message();
						JSONObject jsonmsg = (JSONObject)jsonAry.get(i);
						String rowId = jsonmsg.getString("rowId");
						String nType = jsonmsg.getString("nType");
						String nTitle = jsonmsg.getString("nTitle");
						String nDate = jsonmsg.getString("nDate");
						
						msg.setRowId(rowId);
						msg.setnDate(nDate);
						msg.setnTitle(nTitle);
						msg.setnType(nType);
						msgList.add(msg);
					}
					mR.put("v", msgList);
				}
			}
		}
		return mR;
	}
	
	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
 public List<HashMap<String, String>> getSectionList(String jsonStr) throws Exception {
		List<HashMap<String, String>> msgList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		if("ok".equals(jsonObj.getString("s"))) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					msgList = new ArrayList<HashMap<String, String>>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jsonmsg = (JSONObject)jsonAry.get(i);
						map.put("rowId", jsonmsg.getString("rowId"));
						map.put("allcode", jsonmsg.getString("allcode"));
						map.put("allName", jsonmsg.getString("allName"));
						map.put("parentId", jsonmsg.getString("parentId"));
						msgList.add(map);
					}
				}
			}
		} 
		return msgList;
	}
	
	
	
	/**
	 * 单条公告
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getNoticeContent(String jsonStr) throws Exception {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			String nNo = jsonO.getString("nNo");
			String rowId = jsonO.getString("rowId");
			String nPublisher = jsonO.getString("nPublisher");
			String nContent = jsonO.getString("nContent");
			String nPath = jsonO.getString("nPath");
			String nFileName = jsonO.getString("nFileName");
			String nType = jsonO.getString("nType");
			String nTitle = jsonO.getString("nTitle");
			String nDate = jsonO.getString("nDate");
			
			_Message message = new _Message();
			message.setnContent(nContent);
			message.setnDate(nDate);
			message.setnFileName(nFileName);
			message.setnType(nType);
			message.setnPath(nPath);
			message.setnPublisher(nPublisher);
			message.setRowId(rowId);
			message.setnNo(nNo);
			message.setnTitle(nTitle);
			
			mR.put("v", message);
		}
		return mR;
	}
	/**
	 * 获取日程列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 * @throws ParseException 
	 */
	public Map<String,Object> getScheduleList(String jsonStr, int currMonth, int maxDayOfMonth) throws JSONException, ParseException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Agendas> agendaList = null;
		Map<String,List<_Agendas>> agendasMap = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				int rows = jsonO.getInt("rows");
				mR.put("rows", rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					agendaList = new ArrayList<_Agendas>();
					agendasMap = new HashMap<String,List<_Agendas>>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Agendas _agendas = new _Agendas();
						JSONObject jsonobj = (JSONObject) jsonAry.get(i);
						String rowId = jsonobj.getString("rowId");
						String sTitle = jsonobj.getString("sTitle");
						String sContent = jsonobj.getString("sContent");
						String sTime = jsonobj.getString("sTime");
						String eTime = jsonobj.getString("eTime");
						String isAllDay = jsonobj.getString("isAllDay");
						String createEmp = jsonobj.getString("createEmp");
						String empId = jsonobj.getString("empId");//创建人id
						
						_agendas.setRowId(rowId);
						_agendas.setsTitle(sTitle);
						_agendas.setsContent(sContent);
						_agendas.setsTime(sTime);
						_agendas.seteTime(eTime);
						_agendas.setIsAllDay(isAllDay);
						_agendas.setCreateEmp(createEmp);
						_agendas.setEmpId(empId);
						
						agendaList.add(_agendas);
						
						Date mStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sTime);
						Date mEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eTime);
						Calendar mStartCalendar = Calendar.getInstance();
						mStartCalendar.setTime(mStartDate);	
						Calendar mEndCalendar = Calendar.getInstance();
						mEndCalendar.setTime(mEndDate);
						
						int mStartMonth = mStartCalendar.get(Calendar.MONTH) + 1;
						int mEndMonth = mEndCalendar.get(Calendar.MONTH) + 1;
						
						int mStratDay = mStartCalendar.get(Calendar.DAY_OF_MONTH);
						int mEndDay = mEndCalendar.get(Calendar.DAY_OF_MONTH);
						
						if(mStartMonth == mEndMonth ) {
							for(int j = mStratDay; j <= mEndDay; j ++) {
								String day = j < 10 ? "0" + j : String.valueOf(j);
								List<_Agendas> currDayAgendasList = agendasMap.get(day);
								if(currDayAgendasList == null) {
									currDayAgendasList = new ArrayList<_Agendas>();
									currDayAgendasList.add(_agendas);
								} else {
									currDayAgendasList.add(_agendas);
								}
								agendasMap.put(day, currDayAgendasList);
							}
						} else {
							if(currMonth > mStartMonth && mEndMonth == currMonth) {
								for(int j = 1; j <= mEndDay; j ++) {
									String day = j < 10 ? "0" + j : String.valueOf(j);
									List<_Agendas> currDayAgendasList = agendasMap.get(day);
									if(currDayAgendasList == null) {
										currDayAgendasList = new ArrayList<_Agendas>();
										currDayAgendasList.add(_agendas);
									} else {
										currDayAgendasList.add(_agendas);
									}
									agendasMap.put(day, currDayAgendasList);
								}
							} else if(currMonth > mStartMonth && mEndMonth > currMonth) {
								for(int j = 1; j <= maxDayOfMonth; j ++) {
									String day = j < 10 ? "0" + j : String.valueOf(j);
									List<_Agendas> currDayAgendasList = agendasMap.get(day);
									if(currDayAgendasList == null) {
										currDayAgendasList = new ArrayList<_Agendas>();
										currDayAgendasList.add(_agendas);
									} else {
										currDayAgendasList.add(_agendas);
									}
									agendasMap.put(day, currDayAgendasList);
								}
							} else if(currMonth == mStartMonth && currMonth < mEndMonth) {
								for(int j = mStratDay; j <= maxDayOfMonth; j ++) {
									String day = j < 10 ? "0" + j : String.valueOf(j);
									List<_Agendas> currDayAgendasList = agendasMap.get(day);
									if(currDayAgendasList == null) {
										currDayAgendasList = new ArrayList<_Agendas>();
										currDayAgendasList.add(_agendas);
									} else {
										currDayAgendasList.add(_agendas);
									}
									agendasMap.put(day, currDayAgendasList);
								}
							}
						}
					}
					//data.setAgendasList(agendaList);
					mR.put("agendaList", agendaList);
					//data.setAgendasMap(agendasMap);
					mR.put("agendasMap", agendasMap);
				}
			}
		}
		return mR;
	}
	
	public Map<String,Object> getScheduleList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Agendas> agendaList = null;
		Map<String,List<_Agendas>> agendasMap = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				int rows = jsonO.getInt("rows");
				mR.put("rows", rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					agendaList = new ArrayList<_Agendas>();
					agendasMap = new HashMap<String,List<_Agendas>>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Agendas _agendas = new _Agendas();
						JSONObject jsonobj = (JSONObject) jsonAry.get(i);
						String rowId = jsonobj.getString("rowId");
						String sTitle = jsonobj.getString("sTitle");
						String sContent = jsonobj.getString("sContent");
						String sTime = jsonobj.getString("sTime");
						String eTime = jsonobj.getString("eTime");
						String isAllDay = jsonobj.getString("isAllDay");
						String createEmp = jsonobj.getString("createEmp");
						String empId = jsonobj.getString("empId");//创建人id
						
						_agendas.setRowId(rowId);
						_agendas.setsTitle(sTitle);
						_agendas.setsContent(sContent);
						_agendas.setsTime(sTime);
						_agendas.seteTime(eTime);
						_agendas.setIsAllDay(isAllDay);
						_agendas.setCreateEmp(createEmp);
						_agendas.setEmpId(empId);
						
						agendaList.add(_agendas);
						
						if(sTime != null && !"".equals(sTime)) {
							String day = sTime.substring(8, 10);
							List<_Agendas> currDayAgendasList = agendasMap.get(day);
							if(currDayAgendasList == null) {
								currDayAgendasList = new ArrayList<_Agendas>();
								currDayAgendasList.add(_agendas);
							} else {
								currDayAgendasList.add(_agendas);
							}
							agendasMap.put(day, currDayAgendasList);
						}
					}
					//data.setAgendasList(agendaList);
					mR.put("agendaList", agendaList);
					//data.setAgendasMap(agendasMap);
					mR.put("agendasMap", agendasMap);
				}
			}
		}
		return mR;
	}
	
	/**
	 * 添加日程
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 *//*
	public _BackData addSchedule(String jsonStr) throws JSONException {
		_BackData data = new _BackData();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"AddResult");
		String succStr = jsonObj.getString("s");
		data.setSuccStr(succStr);
		String rowId = jsonObj.getString("v");
		data.setRowId(rowId);
		return data;
	}*/
	
	/**
	 * 添加日程 & 风险检查-提交
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> addResult(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"AddResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			String rowid = jsonObj.getString("v");
			mR.put("v", rowid);
		}
		return mR;
	}
	
	/**
	 * 获取Wps 文件路径
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public String getWpsApk(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetOfficeAPKResult");
		return jsonObj.getString("path");
	}
	
	/**
	 * 删除日程
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> deleteSchedule(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"DeleteResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		String rowId = jsonObj.getString("v");
		mR.put("v", rowId);
		return mR;
	}
	
	/**
	 * 更新日程
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> updateSchedule(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"UpdateResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		String rowId = jsonObj.getString("v");
		mR.put("v", rowId);
		return mR;
	}
	
	
	
	/**
	 * 更新日程
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,String> getSectionsCount(String jsonStr) throws JSONException {
		Map<String,String> mR = new HashMap<String,String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetSectionSubItemCountResult");
		mR.put("s", jsonObj.getString("s"));
		mR.put("v", jsonObj.getString("v"));
		return mR;
	}
	/**
	 * 获取工程列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getProjectList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Project> proList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
				JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
				if(jsonAry != null && jsonAry.length() > 0) {
					proList = new ArrayList<_Project>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Project pro = new _Project();
						JSONObject jsonPro = (JSONObject)jsonAry.get(i);
						String rowId = jsonPro.getString("rowId");//主键
						String aName = jsonPro.getString("aName");//工程名称
						String aPid = jsonPro.getString("aPid");//项目id
						String aSecid = jsonPro.getString("aSecid");//标段id
						String aType = jsonPro.getString("aType");//类型
						
						String aStartMile = jsonPro.getString("a_start_mile");//起始里程
						String  aEndMile = jsonPro.getString("a_end_mile");//结束里程
						
						String aPosition = jsonPro.getString("aPosition");
						String aPositionMile = jsonPro.getString("aPositionMile");
						
						
						pro.setRowId(rowId);
						pro.setaName(aName);
						pro.setaPid(aPid);
						pro.setaSecid(aSecid);
						pro.setaType(aType);
						
						pro.setaStartMile(aStartMile);
						pro.setaEndMile(aEndMile);
						
						pro.setaPosition(aPosition);
						pro.setaPositionMile(aPositionMile);
						proList.add(pro);
					}
			}
		}
		mR.put("v", proList);
		return mR;
	}
	
	/**
	 * 获取工程详细
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	@Deprecated
	public Map<String,Object> getProjectContent(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		_Project pro = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
						pro = new _Project();
						String rowId = jsonO.getString("rowId");//主键
						String aName = jsonO.getString("aName");//工程名称
						String aPid = jsonO.getString("aPid");//项目id
						String aSecid = jsonO.getString("aSecid");//标段id
						String aType = jsonO.getString("aType");//类型
						
						String aPmName = jsonO.getString("aPmName");//项目经理
						String aPContract = jsonO.getString("aPContract");//联系方式
						String aConstruct = jsonO.getString("aConstruct");//施工单位
						String aStartMile = jsonO.getString("a_start_mile");//起始里程
						String  aEndMile = jsonO.getString("a_end_mile");//结束里程
						
						pro.setRowId(rowId);
						pro.setaName(aName);
						pro.setaPid(aPid);
						pro.setaSecid(aSecid);
						pro.setaType(aType);
						
						pro.setaPmName(aPmName);
						pro.setaPContract(aPContract);
						pro.setaConstruct(aConstruct);
						pro.setaStartMile(aStartMile);
						pro.setaEndMile(aEndMile);
				}
		}
		mR.put("v", pro);
		return mR;
	}
	
	
	/**
	 * 获取项目标段信息
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	@Deprecated
	public Map<String,Object> getProSecInfo(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Item> itemList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetProjectResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				itemList = new ArrayList<_Item>();
				for(int i = 0; i < jsonAry.length(); i ++) {
					_Item item = new _Item();
					JSONObject jsonI = (JSONObject)jsonAry.get(i);
					String id = jsonI.getString("id");//项目主键
					String text = jsonI.getString("text");//项目内容
					String attributes = jsonI.getString("attributes");
					item.setId(id);
					item.setText(text);
					item.setAttributes(attributes);
					JSONArray jsonSubAry = new JSONArray(jsonI.getString("children"));
					if(jsonSubAry != null && jsonSubAry.length() > 0) {
						for(int j = 0; j < jsonSubAry.length(); j++) {
							_Section sec = new _Section();
							
							JSONObject jsonsI = (JSONObject)jsonSubAry.get(j);
							String cid = jsonsI.getString("id");//项目主键
							String ctext = jsonsI.getString("text");//项目内容
							String cattributes = jsonsI.getString("attributes");
							sec.setId(cid);
							sec.setText(ctext);
							sec.setAttributes(cattributes);
							
							item.getSecList().add(sec);
						}
					}
					itemList.add(item);
				}
			}
		}
		mR.put("v", itemList);
		return mR;
	}
	
	
	/**
	 * 获取项目标段信息
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getItemSecInfo(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_ItemSec> itemList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetProjectResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				itemList = new ArrayList<_ItemSec>();
				for(int i = 0; i < jsonAry.length(); i ++) {
					_ItemSec itemsec = null;
					JSONObject jsonI = (JSONObject)jsonAry.get(i);
					String id = jsonI.getString("id");//项目主键
					String text = jsonI.getString("text");//项目内容
					String attributes = jsonI.getString("attributes");
					
					JSONArray jsonSubAry = new JSONArray(jsonI.getString("children"));
					if(jsonSubAry != null && jsonSubAry.length() > 0) {
						for(int j = 0; j < jsonSubAry.length(); j++) {
							itemsec = new _ItemSec();
							JSONObject jsonsS = (JSONObject)jsonSubAry.get(j);
							String cid = jsonsS.getString("id");//项目主键
							String ctext = jsonsS.getString("text");//项目内容
							String cattributes = jsonsS.getString("attributes");
							itemsec.setIid(id);
							itemsec.setItext(text);
							itemsec.setIattributes(attributes);
							itemsec.setSid(cid);
							itemsec.setStext(ctext);
							itemsec.setSattributes(cattributes);
							
							itemList.add(itemsec);
						}
					} else {
						itemsec = new _ItemSec();
						itemsec.setIid(id);
						itemsec.setItext(text);
						itemsec.setIattributes(attributes);
						itemList.add(itemsec);
					}
				}
			}
		}
		mR.put("v", itemList);
		return mR;
	}
	
	
	/**
	 * 获取指定人员当天内最近一笔的GPS坐标
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	@Deprecated
	public Map<String,Object> getEmpGps(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetEmpGpsResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonObj2 = new JSONObject(jsonObj.getString("v"));
			if(jsonObj2 != null) {
				String lat = jsonObj2.getString("lat");
				String lng = jsonObj2.getString("lng");
				String date = jsonObj2.getString("date");
				_GpsEmp gpsEmp = new _GpsEmp();
				
				gpsEmp.setLng(lng);
				gpsEmp.setLat(lat);
				gpsEmp.setGpsDate(date);
				
				mR.put("v", gpsEmp);
			}
		} else {
			mR.put("s", "没有找到定位信息");
		}
		return mR;
	}
	/**
	 * 根据地图的范围，返回当前范围内的人员信息
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	@Deprecated
	public Map<String,Object> getEmpAreaGps(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_GpsEmp> gpsEmpList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetMapAreaGpsResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
			  gpsEmpList = new ArrayList<_GpsEmp>();
			  for(int i = 0; i < jsonAry.length(); i ++) {
				  _GpsEmp emp = new _GpsEmp();
				  
				  JSONObject jsonI = (JSONObject)jsonAry.get(i);
				  
				  String empName = jsonI.getString("empName");
				  String dptName = jsonI.getString("dptName");
				  String empLevel = jsonI.getString("empLevel");
				  String empContact = jsonI.getString("empContact");
				  String lng = jsonI.getString("lng");
				  String lat = jsonI.getString("lat");
				  String gpsDate = jsonI.getString("gpsDate");
				  String rowId = jsonI.getString("empId");
				  
				  emp.setEmpName(empName);//人员姓名
				  emp.setDptName(dptName);//单位名称
				  emp.setEmpLevel(empLevel);//职务
				  emp.setEmpContact(empContact);//联系方式
				  emp.setLng(lng);//经度
				  emp.setLat(lat);
				  emp.setGpsDate(gpsDate);//gps时间
				  emp.setEmpId(rowId);
				  
				  gpsEmpList.add(emp);
			  }
			}
		}
		mR.put("v", gpsEmpList);
		return mR;
	}
	/**
	 * 获取人员的运行轨迹
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getListNoPage(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_GpsEmp> gpsEmpList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
			  gpsEmpList = new ArrayList<_GpsEmp>();
			  for(int i = 0; i < jsonAry.length(); i ++) {
				  _GpsEmp emp = new _GpsEmp();
				  JSONObject jsonI = (JSONObject)jsonAry.get(i);
				  String lng = jsonI.getString("lng");
				  String lat = jsonI.getString("lat");
				  String gpsDate = jsonI.getString("gpsDate");
				  
				  emp.setLng(lng);//经度
				  emp.setLat(lat);
				  emp.setGpsDate(gpsDate);//gps时间
				  
				  gpsEmpList.add(emp);
			  }
			}
		}
		mR.put("v", gpsEmpList);
		return mR;
	}
	
	/**
	 * 获取工程清单信息列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getGpsRecordListNoPage(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		ArrayList<HashMap<String, String>> list = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonSObj = jsonObj.getJSONObject("v");
			JSONArray jsonAry = jsonSObj.getJSONArray("list");
			if(jsonAry != null && jsonAry.length() > 0) {
				list = new ArrayList<HashMap<String, String>>();
			  for(int i = 0; i < jsonAry.length(); i ++) {
				  HashMap<String, String> map = new HashMap<String, String>();
				  JSONObject jsonI = (JSONObject)jsonAry.get(i);
				  map.put("code", jsonI.getString("code"));//清单编号
				  map.put("name", jsonI.getString("name"));//清单编号
				  map.put("lastapprove", jsonI.getString("lastapprove"));//上一次计量
				  map.put("sumapprove", jsonI.getString("sumapprove"));//累计计量 
				  map.put("remain", jsonI.getString("remain"));//剩余量
				  map.put("total", jsonI.getString("total"));//合同量
				  map.put("price", jsonI.getString("price"));//单价
				  list.add(map);
			  }
			}
		}
		mR.put("v", list);
		return mR;
	}
	/**
	 * 获取进度清单信息列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getGpsProgressListNoPage(String jsonStr) throws JSONException {
		//{"code":"420-1-a-7","SsiName":"钢筋混凝土盖板涵，3m×3.5m",
		//"afterchgnum":51.50,"munit":"m","todayfinishrate":0.00,"todayNum":0.00,"todayfinishrateJE":0.00}
		Map<String,Object> mR = new HashMap<String,Object>();
		ArrayList<HashMap<String, String>> list = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonSObj = jsonObj.getJSONObject("v");
			JSONArray jsonAry = jsonSObj.getJSONArray("list");
			if(jsonAry != null && jsonAry.length() > 0) {
				list = new ArrayList<HashMap<String, String>>();
			  for(int i = 0; i < jsonAry.length(); i ++) {
				  HashMap<String, String> map = new HashMap<String, String>();
				  JSONObject jsonI = (JSONObject)jsonAry.get(i);
				  map.put("code", jsonI.getString("code"));//清单细目编号
				  map.put("SsiName", jsonI.getString("SsiName"));//名称
				  map.put("afterchgnum", jsonI.getString("afterchgnum"));//分解量
				  map.put("munit", jsonI.getString("munit"));//单位
				  map.put("todayfinishrate", jsonI.getString("todayfinishrate"));//当日完成%
				  map.put("todayNum", jsonI.getString("todayNum"));//数量
				  map.put("todayfinishrateJE", jsonI.getString("todayfinishrateJE"));//金额
				  list.add(map);
			  }
			}
		}
		mR.put("v", list);
		return mR;
	}
	
	
	
	/**
	 * 获取代办文件列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	/*public Map<String,Object> getDocumentList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Message> docList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				int rows = jsonO.getInt("rows");
				mR.put("rows", rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					docList = new ArrayList<_Message>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Message doc = new _Message();
						JSONObject jsonPro = (JSONObject)jsonAry.get(i);
						String rowId = jsonPro.getString("rowId");//主键
						String dTitle = jsonPro.getString("dTitle");//标题
						String dType = jsonPro.getString("dClass");//公文类型dType //TODO
						String dSendTime = jsonPro.getString("dSendTime");//收发文时间
						String dClass = jsonPro.getString("dClass");//类别(收文/发文)
						
						doc.setRowId(rowId);
						doc.setnTitle(dTitle);
						doc.setnType(dType);
						doc.setnDate(dSendTime);
						doc.setnClass(dClass);
						doc.setmLink(jsonPro.getString("mLink"));
						docList.add(doc);
					}
				}
			}
		}
		mR.put("v", docList);
		return mR;
	}*/
	
	public List<HashMap<String, Object>> getAcceptDocumentList(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		List<HashMap<String, Object>> mAllList = null;
		if("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				JSONArray jsonAry = jsonO.getJSONArray("list");
				if(jsonAry != null && jsonAry.length() > 0) {
					mAllList = new ArrayList<HashMap<String, Object>>();
					for(int i = 0, len = jsonAry.length(); i < len; i ++) {
						HashMap<String, Object> map0 = new HashMap<String, Object>();
						JSONObject jsonPro = jsonAry.getJSONObject(i);
						map0.put("rowId", jsonPro.getString("rowId"));//主键
						map0.put("comeDepartment", jsonPro.getString("comeDepartment"));//来文单位/发文机关
						map0.put("comeNum", jsonPro.getString("comeNum"));//来文字号
						map0.put("inCode", jsonPro.getString("inCode"));//收文编号/收文顺序号
						map0.put("comeTitle", jsonPro.getString("comeTitle"));//来文标题
						map0.put("todoSug", jsonPro.getString("todoSug"));//拟办意见
						map0.put("hostSug", jsonPro.getString("hostSug"));//承办意见
						map0.put("originalDate", jsonPro.getString("originalDate"));//原件日期
						map0.put("inDate", jsonPro.getString("inDate"));//收文日期
						map0.put("docName", jsonPro.getString("docName"));//文件名称
						map0.put("memo", jsonPro.getString("memo"));//备注
						map0.put("detail", jsonPro.getString("detail"));//收文内容
						map0.put("create_emp_id", jsonPro.getString("create_emp_id"));//创建人id
						map0.put("create_person", jsonPro.getString("create_person"));//创建人
						map0.put("type", jsonPro.getString("type"));//类型
						
						map0.put("category", jsonPro.getString("category"));//[待办][已办][办结]
						map0.put("listDate", jsonPro.getString("inDate"));
						map0.put("listTitle", jsonPro.getString("inCode"));
						
						map0.put("flowId", jsonPro.getString("flowId"));
						
						JSONArray jsonFileAry = jsonPro.getJSONArray("files");
						//文件列表
						ArrayList<HashMap<String, String>> mFileList = null;
						if(jsonFileAry != null && jsonFileAry.length() > 0) {
							mFileList = new ArrayList<HashMap<String, String>>();
							for(int l = 0,lenL = jsonFileAry.length(); l < lenL; l ++) {
								JSONObject jsonFileItem = jsonFileAry.getJSONObject(l);
								HashMap<String, String> map3 = new HashMap<String,String>();
								map3.put("file_name", jsonFileItem.getString("file_name"));
								map3.put("file_path", jsonFileItem.getString("file_path"));
								mFileList.add(map3);
							}
						}
						map0.put("FileList", mFileList);
						//流程列表
						JSONArray jsonSubAry = jsonPro.getJSONArray("flows");
						ArrayList<HashMap<String, Object>> mFlowList = null;
						if(jsonSubAry != null && jsonSubAry.length() > 0) {
							mFlowList = new ArrayList<HashMap<String, Object>>();
							for(int j = 0,lenJ = jsonSubAry.length(); j < lenJ; j ++) {
								JSONObject jsonSubItem = jsonSubAry.getJSONObject(j);
								HashMap<String, Object> map1 = new HashMap<String, Object>();
								map1.put("flow_name", jsonSubItem.getString("flow_name"));//流程名称
								map1.put("flow_type", jsonSubItem.getString("flow_type"));//流程类型(串签，并签，会签)
								map1.put("flow_stime", jsonSubItem.getString("flow_stime"));//流程处理时间
								map1.put("flow_status", jsonSubItem.getString("flow_status"));//状态(未开始 进行中  已结束)
								JSONArray josnEmpAry = jsonSubItem.getJSONArray("flow_emps");
								//人员列表
								ArrayList<HashMap<String, String>> mEmpList = null;
								if(josnEmpAry != null && josnEmpAry.length() > 0) {
									mEmpList = new ArrayList<HashMap<String, String>>();
									for(int k =0,lenK = josnEmpAry.length(); k < lenK; k ++) {
										HashMap<String, String> map2 = new HashMap<String, String>();
										JSONObject jsonEmpItem = josnEmpAry.getJSONObject(k);
										map2.put("emp_name", jsonEmpItem.getString("emp_name"));//姓名
										map2.put("dpt_name", jsonEmpItem.getString("dpt_name"));//部门
										map2.put("deal_time", jsonEmpItem.getString("deal_time"));//处理时间
										map2.put("suggestion", jsonEmpItem.getString("suggestion"));//处理意见
										mEmpList.add(map2);
									}
								}
								map1.put("EmpList", mEmpList);
								mFlowList.add(map1);
							}
						}
						map0.put("FlowList", mFlowList);
						mAllList.add(map0);
					}
				}
			}
			return mAllList;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> getSendDocumentList(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		List<HashMap<String, Object>> mAllList = null;
		if("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				JSONArray jsonAry = jsonO.getJSONArray("list");
				if(jsonAry != null && jsonAry.length() > 0) {
					mAllList = new ArrayList<HashMap<String, Object>>();
					for(int i = 0, len = jsonAry.length(); i < len; i ++) {
						HashMap<String, Object> map0 = new HashMap<String, Object>();
						JSONObject jsonPro = jsonAry.getJSONObject(i);
						map0.put("rowId", jsonPro.getString("rowId"));//主键
						map0.put("title", jsonPro.getString("title"));//标题
						map0.put("classification", jsonPro.getString("classification"));//密级
						map0.put("urgency", jsonPro.getString("urgency"));//缓急
						map0.put("typer", jsonPro.getString("typer"));//打字
						map0.put("revision", jsonPro.getString("revision"));//校对
						map0.put("copiesnum", jsonPro.getString("copiesnum"));//份数
						map0.put("wordcode", jsonPro.getString("wordcode"));//字号
						map0.put("code", jsonPro.getString("code"));//编号
						map0.put("sendtime", jsonPro.getString("sendtime"));//印发日期
						map0.put("booker_id", jsonPro.getString("booker_id"));//拟稿人id
						map0.put("booker", jsonPro.getString("booker"));//拟稿人
						map0.put("date", jsonPro.getString("date"));//拟稿日期
						map0.put("organizers", jsonPro.getString("organizers"));//主办部门
						map0.put("content", jsonPro.getString("content"));//事由
						map0.put("department", jsonPro.getString("department"));//发文单位
						map0.put("status", jsonPro.getString("status"));//流程状态
						map0.put("mainsend", jsonPro.getString("mainsend"));//主送
						map0.put("copyreport", jsonPro.getString("copyreport"));//抄报
						map0.put("copysend", jsonPro.getString("copysend"));//抄送
						map0.put("detail", jsonPro.getString("detail"));//内容
						map0.put("year", jsonPro.getString("year"));//年份
						map0.put("allwordcode", jsonPro.getString("allwordcode"));//字号编号
						map0.put("type", jsonPro.getString("type"));//类型
						
						map0.put("category", jsonPro.getString("category"));//[待办][已办][办结]
						map0.put("listDate", jsonPro.getString("date"));
						map0.put("listTitle", jsonPro.getString("allwordcode"));
						map0.put("flowId", jsonPro.getString("flowId"));
						
						JSONArray jsonFileAry = jsonPro.getJSONArray("files");
						//文件列表
						ArrayList<HashMap<String, String>> mFileList = null;
						if(jsonFileAry != null && jsonFileAry.length() > 0) {
							mFileList = new ArrayList<HashMap<String, String>>();
							for(int l = 0,lenL = jsonFileAry.length(); l < lenL; l ++) {
								JSONObject jsonFileItem = jsonFileAry.getJSONObject(l);
								HashMap<String, String> map3 = new HashMap<String,String>();
								map3.put("file_name", jsonFileItem.getString("file_name"));
								map3.put("file_path", jsonFileItem.getString("file_path"));
								mFileList.add(map3);
							}
						}
						map0.put("FileList", mFileList);
						//流程列表
						JSONArray jsonSubAry = jsonPro.getJSONArray("flows");
						ArrayList<HashMap<String, Object>> mFlowList = null;
						if(jsonSubAry != null && jsonSubAry.length() > 0) {
							mFlowList = new ArrayList<HashMap<String, Object>>();
							for(int j = 0,lenJ = jsonSubAry.length(); j < lenJ; j ++) {
								JSONObject jsonSubItem = jsonSubAry.getJSONObject(j);
								HashMap<String, Object> map1 = new HashMap<String, Object>();
								map1.put("flow_name", jsonSubItem.getString("flow_name"));//流程名称
								map1.put("flow_type", jsonSubItem.getString("flow_type"));//流程类型(串签，并签，会签)
								map1.put("flow_stime", jsonSubItem.getString("flow_stime"));//流程处理时间
								map1.put("flow_status", jsonSubItem.getString("flow_status"));//状态(未开始 进行中  已结束)
								JSONArray josnEmpAry = jsonSubItem.getJSONArray("flow_emps");
								//人员列表
								ArrayList<HashMap<String, String>> mEmpList = null;
								if(josnEmpAry != null && josnEmpAry.length() > 0) {
									mEmpList = new ArrayList<HashMap<String, String>>();
									for(int k =0,lenK = josnEmpAry.length(); k < lenK; k ++) {
										HashMap<String, String> map2 = new HashMap<String, String>();
										JSONObject jsonEmpItem = josnEmpAry.getJSONObject(k);
										map2.put("emp_name", jsonEmpItem.getString("emp_name"));//姓名
										map2.put("dpt_name", jsonEmpItem.getString("dpt_name"));//部门
										map2.put("deal_time", jsonEmpItem.getString("deal_time"));//处理时间
										map2.put("suggestion", jsonEmpItem.getString("suggestion"));//处理意见
										mEmpList.add(map2);
									}
								}
								map1.put("EmpList", mEmpList);
								mFlowList.add(map1);
							}
						}
						map0.put("FlowList", mFlowList);
						mAllList.add(map0);
					}
				}
			}
			return mAllList;
		}
		return null;
	}
	
	
	
	/**
	 * 公告列表
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getMessageCount(String jsonStr) throws Exception {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetMessageCountResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
				int rows = jsonObj.getInt("v");
				mR.put("v", rows);
		}
		return mR;
	}
	
	
	
	/**
	 * 获取消息列表
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getMessageList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_Message> msgList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				//int rows = jsonO.getInt("rows");
				//data.setRows(rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					msgList = new ArrayList<_Message>();
					for(int i = 0; i < jsonAry.length(); i ++) {
						_Message msg = new _Message();
						JSONObject jsonMsg = (JSONObject)jsonAry.get(i);
						String rowId = jsonMsg.getString("rowId");//主键
						String mTitle = jsonMsg.getString("mTitle");//标题
						String mClass = jsonMsg.getString("mClass");//类型
						String mTime = jsonMsg.getString("mTime");//时间
						String mLink = jsonMsg.getString("mLink");//链接
						//
						msg.setRowId(rowId);
						msg.setnTitle(mTitle);
						msg.setnType(mClass);
						msg.setnDate(mTime);
						msg.setmLink(mLink);
						
						msgList.add(msg);
					}
				}
			}
		}
		mR.put("v", msgList);
		return mR;
	}
	
	/**
	 * 获取消息代办详情
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	/*public Map<String,Object> getDocDetails(String jsonStr) throws JSONException {//TODO
		Map<String,Object> mR = new HashMap<String,Object>();
		_Document document = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetDocumentContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				//int rows = jsonO.getInt("rows");
				//data.setRows(rows);
				document = new _Document();
				
				String row_id = jsonO.getString("row_id");
				String d_title = jsonO.getString("d_title");
				String d_num = jsonO.getString("d_num");
				String d_code = jsonO.getString("d_code");
				String booker = jsonO.getString("booker");
				String d_content = jsonO.getString("d_content");
				String d_sendtime = jsonO.getString("d_sendtime");
				String d_department = jsonO.getString("d_department");
				String flow_name = jsonO.getString("flow_name");
				String step_type = jsonO.getString("step_type");
				String flow_id = jsonO.getString("flow_id");
				String nPath = jsonO.getString("nPath");//附件地址(“”表示没有附件)
				String nFileName = jsonO.getString("nFileName");//附件名称
				String d_class = jsonO.getString("d_class");
				
				//new adddd
				//String step_type = jsonO.getString("step_type");//当前流程步骤 
				String step_id = jsonO.getString("step_id");//当前流程步骤id 
				
				document.setRow_id(row_id);
				document.setD_title(d_title);
				document.setD_num(d_num);
				document.setD_code(d_code);
				document.setBooker(booker);
				document.setD_content(d_content);
				document.setD_sendtime(d_sendtime);
				document.setD_department(d_department);
				document.setFlow_id(flow_id);
				document.setFlow_name(flow_name);
				document.setStep_type(step_type);
				document.setnPath(nPath);
				document.setnFileName(nFileName);
				document.setD_class(d_class);
				document.setStep_id(step_id);
				
				List<_DocFlows> docFlowsList = new ArrayList<_DocFlows>();
				
				JSONArray jsonAry = new JSONArray(jsonO.getString("flows"));
				if(jsonAry != null && jsonAry.length() > 0) {
					for(int i = 0; i < jsonAry.length(); i ++) {
						_DocFlows docFlows = new _DocFlows();
						
						JSONObject jsonD = (JSONObject)jsonAry.get(i);
						String flow_name2 = jsonD.getString("flow_name");//
						String flow_type = jsonD.getString("flow_type");//类型
						String flow_stime = jsonD.getString("flow_stime");//
						String flow_status = jsonD.getString("flow_status");//
						
						List<_EmpsFlow> empsFlowsList = new ArrayList<_EmpsFlow>();
						//flow_emps
						JSONArray jsonEmps = new JSONArray(jsonD.getString("flow_emps"));
						if(jsonEmps != null && jsonEmps.length() > 0) {
							for(int j = 0; j < jsonEmps.length(); j ++) {
								_EmpsFlow empsFlow = new _EmpsFlow();
								JSONObject jsonE = (JSONObject)jsonEmps.get(j);
								String emp_name = jsonE.getString("emp_name");//
								String dpt_name = jsonE.getString("dpt_name");//标题
								String deal_time = jsonE.getString("deal_time");//类型
								String suggestion = jsonE.getString("suggestion");//时间
								
								empsFlow.setEmp_name(emp_name);
								empsFlow.setDpt_name(dpt_name);
								empsFlow.setDeal_time(deal_time);
								empsFlow.setSuggestion(suggestion);
								
								empsFlowsList.add(empsFlow);
							}
						}
						docFlows.setFlow_name(flow_name2);
						docFlows.setFlow_type(flow_type);
						docFlows.setFlow_status(flow_status);
						docFlows.setFlow_stime(flow_stime);
						docFlows.setEmpsFlowsList(empsFlowsList);
						docFlowsList.add(docFlows);
					}
				}
				document.setDocFlows(docFlowsList);
			}
		}
		mR.put("v", document);
		return mR;
	}*/
	
	@Deprecated
	public Map<String, Object> getDocDetail(String jsonStr, String mResult) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		HashMap<String, Object> mReceiveDoc = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr, mResult);
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				mReceiveDoc = new HashMap<String, Object>();
				ArrayList<HashMap<String, String>> mBaseInfoList = new ArrayList<HashMap<String, String>>();
				String row_id = jsonO.getString("row_id");//6430ac00-9d48-411b-8d0b-d8b65526bba8
				String d_title = jsonO.getString("d_title");//收文测试
				if(d_title != null && !"".equals(d_title.trim()) && !"null".equals(d_title.trim())) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("title", "标题");
					map.put("content", d_title);
					mBaseInfoList.add(map);
				}
				if ("GetReceiveDocumentContentResult".equals(mResult)) {// mark1
					String d_num = jsonO.getString("d_num");// 收文编号/收文顺序号-昌201541
					if (d_num != null && !"".equals(d_num.trim()) && !"null".equals(d_num.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "收文编号/收文顺序号");
						map.put("content", d_num);
						mBaseInfoList.add(map);
					}
					String d_code = jsonO.getString("d_code");// 来文字号-2014（2）号
					if (d_code != null && !"".equals(d_code.trim()) && !"null".equals(d_code.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "来文字号");
						map.put("content", d_code);
						mBaseInfoList.add(map);
					}
					String d_department = jsonO.getString("d_department");// 来文单位-江西高速公路投资集团
					if (d_department != null && !"".equals(d_department.trim()) && !"null".equals(d_department.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "来文单位");
						map.put("content", d_department);
						mBaseInfoList.add(map);
					}
					String d_todosug = jsonO.getString("d_todosug");// 拟办意见-尽快查阅
					if (d_todosug != null && !"".equals(d_todosug.trim()) && !"null".equals(d_todosug.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "拟办意见");
						map.put("content", d_todosug);
						mBaseInfoList.add(map);
					}
					String d_hostsug = jsonO.getString("d_hostsug");// 承办意见
					if (d_hostsug != null && !"".equals(d_hostsug.trim()) && !"null".equals(d_hostsug.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "承办意见");
						map.put("content", d_hostsug);
						mBaseInfoList.add(map);
					}
					String d_originaldate = jsonO.getString("d_originaldate");// 原件日期
					if (d_originaldate != null && !"".equals(d_originaldate.trim()) && !"null".equals(d_originaldate.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "原件日期");
						map.put("content", d_originaldate);
						mBaseInfoList.add(map);
					}
					String d_indate = jsonO.getString("d_indate");// 收文日期
					if (d_indate != null && !"".equals(d_indate.trim())
							&& !"null".equals(d_indate.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "收文日期");
						map.put("content", d_indate);
						mBaseInfoList.add(map);
					}
					String d_docName = jsonO.getString("d_docName");// 文件名称
					if (d_docName != null && !"".equals(d_docName.trim())
							&& !"null".equals(d_docName.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "文件名称");
						map.put("content", d_docName);
						mBaseInfoList.add(map);
					}
					String flow_name = jsonO.getString("flow_name");//当前步骤名称
					if(flow_name != null && !"".equals(flow_name.trim()) && !"null".equals(flow_name.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "当前步骤名称");
						map.put("content", flow_name);
						mBaseInfoList.add(map);
					}
					/*String step_type = jsonO.getString("step_type");//步骤类型
					if(step_type != null && !"".equals(step_type.trim()) && !"null".equals(step_type.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "步骤类型");
						map.put("content", step_type);
						mBaseInfoList.add(map);
					}*/
					String d_memo = jsonO.getString("d_memo");//备注
					if(d_memo != null && !"".equals(d_memo.trim()) && !"null".equals(d_memo.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "备注");
						map.put("content", d_memo);
						mBaseInfoList.add(map);
					}
				} else {//send document
					String d_classification = jsonO.getString("d_classification");//密级
					if(d_classification != null && !"".equals(d_classification.trim()) && !"null".equals(d_classification.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "密级");
						map.put("content", d_classification);
						mBaseInfoList.add(map);
					}
					String d_urgency = jsonO.getString("d_urgency");//缓急
					if(d_urgency != null && !"".equals(d_urgency.trim()) && !"null".equals(d_urgency.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "缓急");
						map.put("content", d_urgency);
						mBaseInfoList.add(map);
					}
					String d_typer = jsonO.getString("d_typer");//打字
					if(d_typer != null && !"".equals(d_typer.trim()) && !"null".equals(d_typer.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "打字");
						map.put("content", d_typer);
						mBaseInfoList.add(map);
					}
					String d_revision = jsonO.getString("d_revision");//校对
					if(d_revision != null && !"".equals(d_revision.trim()) && !"null".equals(d_revision.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "校对");
						map.put("content", d_revision);
						mBaseInfoList.add(map);
					}
					String d_copiesnum = jsonO.getString("d_copiesnum");//份数
					if(d_copiesnum != null && !"".equals(d_copiesnum.trim()) && !"null".equals(d_copiesnum.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "份数");
						map.put("content", d_copiesnum);
						mBaseInfoList.add(map);
					}
					/*String d_wordcode = jsonO.getString("d_wordcode");//字号
					if(d_wordcode != null && !"".equals(d_wordcode.trim()) && !"null".equals(d_wordcode.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "字号");
						map.put("content", d_wordcode);
						mBaseInfoList.add(map);
					}
					String d_code = jsonO.getString("d_code");//编号
					if (d_code != null && !"".equals(d_code.trim()) && !"null".equals(d_code.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "编号");
						map.put("content", d_code);
						mBaseInfoList.add(map);
					}*/
					String d_allwordcode = jsonO.getString("d_allwordcode");//编号
					if (d_allwordcode != null && !"".equals(d_allwordcode.trim()) && !"null".equals(d_allwordcode.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "编号");
						map.put("content", d_allwordcode);
						mBaseInfoList.add(map);
					}
					String d_sendtime = jsonO.getString("d_sendtime");///印发日期
					if(d_sendtime != null && !"".equals(d_sendtime.trim()) && !"null".equals(d_sendtime.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "印发日期");
						map.put("content", d_sendtime);
						mBaseInfoList.add(map);
					}
					String d_booker = jsonO.getString("d_booker");//拟稿人
					if(d_booker != null && !"".equals(d_booker.trim()) && !"null".equals(d_booker.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "拟稿人");
						map.put("content", d_booker);
						mBaseInfoList.add(map);
					}
					String d_date = jsonO.getString("d_date");//拟稿日期
					if(d_date != null && !"".equals(d_date.trim()) && !"null".equals(d_date.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "拟稿日期");
						map.put("content", d_date);
						mBaseInfoList.add(map);
					}
					String d_organizers = jsonO.getString("d_organizers");//主办部门
					if(d_organizers != null && !"".equals(d_organizers.trim()) && !"null".equals(d_organizers.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "主办部门");
						map.put("content", d_organizers);
						mBaseInfoList.add(map);
					}
					String d_content = jsonO.getString("d_content");//事由
					if(d_content != null && !"".equals(d_content.trim()) && !"null".equals(d_content.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "事由");
						map.put("content", d_content);
						mBaseInfoList.add(map);
					}
					String d_department = jsonO.getString("d_department");// 来文单位-江西高速公路投资集团
					if (d_department != null && !"".equals(d_department.trim()) && !"null".equals(d_department.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "发文单位");
						map.put("content", d_department);
						mBaseInfoList.add(map);
					}
					String d_mainsend = jsonO.getString("d_mainsend");//主送
					if(d_mainsend != null && !"".equals(d_mainsend.trim()) && !"null".equals(d_mainsend.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "主送");
						map.put("content", d_mainsend);
						mBaseInfoList.add(map);
					}
					String d_copyreport = jsonO.getString("d_copyreport");//抄报
					if(d_copyreport != null && !"".equals(d_copyreport.trim()) && !"null".equals(d_copyreport.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "抄报");
						map.put("content", d_copyreport);
						mBaseInfoList.add(map);
					}
					String d_copysend = jsonO.getString("d_copysend");//抄送
					if(d_copysend != null && !"".equals(d_copysend.trim()) && !"null".equals(d_copysend.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "抄送");
						map.put("content", d_copysend);
						mBaseInfoList.add(map);
					}
					/*String d_detail = jsonO.getString("d_detail");//内容
					if(d_detail != null && !"".equals(d_detail.trim()) && !"null".equals(d_detail.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "内容");
						map.put("content", d_detail);
						mBaseInfoList.add(map);
					}*/
					mReceiveDoc.put("BaseInfoContent", jsonO.getString("d_detail"));
					String flow_name = jsonO.getString("flow_name");//当前步骤名称
					if(flow_name != null && !"".equals(flow_name.trim()) && !"null".equals(flow_name.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "当前步骤名称");
						map.put("content", flow_name);
						mBaseInfoList.add(map);
					}
					
					/*String step_type = jsonO.getString("step_type");//步骤类型
					if(step_type != null && !"".equals(step_type.trim()) && !"null".equals(step_type.trim())) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", "步骤类型");
						map.put("content", step_type);
						mBaseInfoList.add(map);
					}*/
				}
				String step_type = jsonO.getString("step_type");//步骤类型
				String step_id = jsonO.getString("step_id");//当前步骤id 
				String flow_id = jsonO.getString("flow_id");//流程id
				mReceiveDoc.put("row_id", row_id);
				mReceiveDoc.put("step_id", step_id);
				mReceiveDoc.put("flow_id", flow_id);
				mReceiveDoc.put("step_type", step_type);
				ArrayList<HashMap<String,Object>> docFlowsList = null;
				JSONArray jsonAry = new JSONArray(jsonO.getString("flows"));
				if(jsonAry != null && jsonAry.length() > 0) {
					docFlowsList = new ArrayList<HashMap<String,Object>>();
					int step_target = -1;
					for(int i = 0; i < jsonAry.length(); i ++) {
						JSONObject jsonD = (JSONObject)jsonAry.get(i);
						if(jsonD != null) {
							HashMap<String, Object> mFlowsMap = new HashMap<String, Object>();
							
							String flow_row_id = jsonD.getString("flow_row_id");//
							String flow_name2 = jsonD.getString("flow_name");//
							String flow_type = jsonD.getString("flow_type");//类型
							String flow_stime = jsonD.getString("flow_stime");//
							String flow_status = jsonD.getString("flow_status");//
							
							if(step_id != null && step_id.equals(flow_row_id)) {
								step_target = i + 1;
							}
							if(step_target == i) {
								mReceiveDoc.put("step_next_id", flow_row_id);
								mReceiveDoc.put("step_next_type", flow_type);
								mReceiveDoc.put("flow_name", flow_name2);
							}
							
							ArrayList<HashMap<String, String>> empsFlowsList = new ArrayList<HashMap<String, String>>();
							JSONArray jsonEmps = new JSONArray(jsonD.getString("flow_emps"));
							if(jsonEmps != null && jsonEmps.length() > 0) {
								for(int j = 0; j < jsonEmps.length(); j ++) {
									JSONObject jsonE = (JSONObject)jsonEmps.get(j);
									if(jsonE != null) {
										HashMap<String, String> mEmpsFlow = new HashMap<String, String>();
										String emp_name = jsonE.getString("emp_name");//标题
										String dpt_name = jsonE.getString("dpt_name");//类型
										String deal_time = jsonE.getString("deal_time");//时间
										String suggestion = jsonE.getString("suggestion");//
										
										mEmpsFlow.put("emp_name", emp_name);
										mEmpsFlow.put("dpt_name", dpt_name);
										mEmpsFlow.put("deal_time", deal_time);
										mEmpsFlow.put("suggestion", suggestion);
										empsFlowsList.add(mEmpsFlow);
									}
								}
							}
							mFlowsMap.put("flow_name2", flow_name2);
							mFlowsMap.put("flow_type", flow_type);
							mFlowsMap.put("flow_stime", flow_stime);
							mFlowsMap.put("flow_status", flow_status);
							mFlowsMap.put("empsFlowsList", empsFlowsList);
							docFlowsList.add(mFlowsMap);
						}
					}
				}
				JSONArray jsonFileAry = new JSONArray(jsonO.getString("files"));
				if(jsonFileAry != null) {
					for(int i = 0; i < jsonFileAry.length(); i ++) {
						JSONObject json = (JSONObject)jsonFileAry.get(i);
						if(json != null) {
							HashMap<String, String> mFileMap = new HashMap<String, String>();
							String fileName = json.getString("file_name");
							String filePath = json.getString("file_path");
							
							mFileMap.put("title", "附件" + (i + 1));
							mFileMap.put("content", fileName);
							mFileMap.put("path", filePath);
							String mFileName = null;
							if(filePath != null && !"".equals(filePath.trim())) {
								try {
									mFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
								} catch (Exception e) {}
							}
							mFileMap.put("fileName", mFileName);
							mBaseInfoList.add(mFileMap);
						}
					}
				}
				mReceiveDoc.put("docFlowsList", docFlowsList);
				mReceiveDoc.put("BaseInfo", mBaseInfoList);
			}
		}
		mR.put("v", mReceiveDoc);
		return mR;
	}
	
	
	/**
	 * 获取单笔日程的内容
	 */
	public Map<String,Object> getScheduleContent(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		_Agendas agendas = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if (jsonO != null) {
				agendas = new _Agendas();
				String rowId = jsonO.getString("rowId");
				String sTitle = jsonO.getString("sTitle");
				String sContent = jsonO.getString("sContent");
				String sTime = jsonO.getString("sTime");
				String eTime = jsonO.getString("eTime");
				String isAllDay = jsonO.getString("isAllDay");
				String createEmp = jsonO.getString("createEmp");
				String empId = jsonO.getString("empId");// 创建人id

				agendas.setRowId(rowId);
				agendas.setsTitle(sTitle);
				agendas.setsContent(sContent);
				agendas.setCreateEmp(createEmp);
				agendas.setsTime(sTime);
				agendas.seteTime(eTime);
				agendas.setEmpId(empId);
				agendas.setIsAllDay(isAllDay);
			}
		}
		mR.put("v", agendas);
		return mR;
	}
	
	/**
	 * 待办公文-检查
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> checkDocApprove(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_ContactsData> dataList = new ArrayList<_ContactsData>();
		
		JSONObject jsonObj = getHandlerJSON(jsonStr,"CheckNextFlowEmpResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					
					if(jsonD != null) {
						String dptId = jsonD.getString("DptId");
						String dptName = jsonD.getString("DptName");
						
						JSONArray jsonBAry = new JSONArray(jsonD.getString("Employee"));
						if(jsonBAry != null && jsonBAry.length() > 0) {
							for(int j = 0; j < jsonBAry.length(); j ++) {
								JSONObject jsonE = (JSONObject)jsonBAry.get(j);
								if(jsonE != null) {
									String rowId = jsonE.getString("rowId");
									String empUser = jsonE.getString("empUser");
									String empName = jsonE.getString("empName");
									String roleName = jsonE.getString("roleName");
									_ContactsData contacts = new _ContactsData();
									
									contacts.setDptId(dptId);
									contacts.setDptName(dptName);
									contacts.setRowId(rowId);
									contacts.setEmpName(empName);
									contacts.setEmpUser(empUser);
									contacts.setRoleName(roleName);
									
									dataList.add(contacts);
								}
							}
						}
					}
				}
			}
			mR.put("v", dataList);
		}
		return mR;
	}
	
	/**
	 * 待办公文-审批
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,String> updateDocApprove(String jsonStr) throws JSONException {
		Map<String,String> mR = new HashMap<String,String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"ApproveResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		return mR;
	}
	/**
	 * 风险检查-提交
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	/*public _BackData addChecking(String jsonStr) throws JSONException {
		_BackData data = new _BackData();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"AddResult");
		String succStr = jsonObj.getString("s");
		data.setSuccStr(succStr);
		if ("ok".equals(succStr)) {
			String rowid = jsonObj.getString("v");
			data.setRowId(rowid);
		}
		return data;
	}*/
	
	/**
	 * 风险检查-提交-上传图片返回
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> upPic(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"UpPicResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONObject jsonV = jsonObj.getJSONObject("v");
			String picId = jsonV.getString("picid");
			String picPath = jsonV.getString("path");
			mR.put("rowId", picId);
			mR.put("path", picPath);
		}
		return mR;
	}
	/**
	 * 版本更新-检查
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,String> getNewVersion(String jsonStr,String result) throws JSONException {
		Map<String,String> mR = new HashMap<String,String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,result);//1.GetVersionResult,2.GetSectionItemResult
		String version = jsonObj.getString("version");
		String path = jsonObj.getString("path");
		mR.put("version", version);
		mR.put("path", path);
		return mR;
	}
	/**
	 * 风险检查-获取
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getRiskSettings(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_HolefaceSetting> mHolefaceSettingList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				mHolefaceSettingList = new ArrayList<_HolefaceSetting>();
			  for(int i = 0; i < jsonAry.length(); i ++) {
				  _HolefaceSetting mSetting = new _HolefaceSetting();
				  
				  JSONObject jsonI = (JSONObject)jsonAry.get(i);
				  
				  String oneType = jsonI.getString("oneType");
				  String twoType = jsonI.getString("twoType");
				  String class1 = jsonI.getString("class1");
				  String class2 = jsonI.getString("class2");
				  String isChild = jsonI.getString("isChild");
				  String riskType = jsonI.getString("riskType");
				  String class2Tip = jsonI.getString("class2Tip");
				  
				  mSetting.setOneType(oneType);
				  mSetting.setTwoType(twoType);
				  mSetting.setClass1(class1);
				  mSetting.setClass2(class2);
				  mSetting.setIsChild(isChild);
				  mSetting.setRiskType(riskType);
				  mSetting.setClass2Tip(class2Tip);
				  mSetting.set$cId("0");
				  List<_HolefaceSetting> mSubHolefaceSettingList = null;
				  if(null != isChild && "2".equals(isChild)) {
					  JSONArray jsonSubAry = new JSONArray(jsonI.getString("childs"));
					  mSubHolefaceSettingList = new ArrayList<_HolefaceSetting>();
					  for(int j = 0; j < jsonSubAry.length(); j ++) {
						  _HolefaceSetting mJSetting = new _HolefaceSetting();
						  JSONObject jsonJ = (JSONObject)jsonSubAry.get(j);
						  String oneType2 = jsonJ.getString("oneType");
						  String twoType2 = jsonJ.getString("twoType");
						  String class12 = jsonJ.getString("class1");
						  String class22 = jsonJ.getString("class2");
						  String isChild2 = jsonJ.getString("isChild");
						  String riskType2 = jsonJ.getString("riskType");
						  String class2Tip2 = jsonJ.getString("class2Tip");
						  
						  mJSetting.setOneType(oneType2);
						  mJSetting.setTwoType(twoType2);
						  mJSetting.setClass1(class12);
						  mJSetting.setClass2(class22);
						  mJSetting.setIsChild(isChild2);
						  mJSetting.setRiskType(riskType2);
						  mJSetting.setClass2Tip(class2Tip2);
						  
						  mSubHolefaceSettingList.add(mJSetting);
					  }
					  mSetting.setmClildList(mSubHolefaceSettingList);
				  }
				  mHolefaceSettingList.add(mSetting);
			  }
			}
		}
		mR.put("v", mHolefaceSettingList);
		return mR;
	}
	/**
	 * 风险检查-获取提示
	 * @param jsonStr
	 * @return //row_id:主键, list_id:风险检查id，a_date:检查日期, a_position_t:风险标题, a_risk_event:风险事件, a_check_item：检查项 a_alarm_class:风险等级
	 * @throws JSONException
	 */
	public Map<String,Object> getRiskShowings(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_RiskNotice> mHRiskNoticeList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if(jsonAry != null && jsonAry.length() > 0) {
					mHRiskNoticeList = new ArrayList<_RiskNotice>();
				  for(int i = 0; i < jsonAry.length(); i ++) {
					  _RiskNotice mShowing = new _RiskNotice();
					  JSONObject jsonI = (JSONObject)jsonAry.get(i);
					  String row_id = jsonI.getString("rowId");
					  String aDate = jsonI.getString("aDate");
					  String sectionName = jsonI.getString("sectionName");
					  String unitName = jsonI.getString("unitName");
					  String alarmClass = jsonI.getString("alarmClass");
					  String checker = jsonI.getString("chkEmp");
					  mShowing.setRow_id(row_id);
					  mShowing.setaDate(aDate);
					  mShowing.setAlarmClass(alarmClass);
					  mShowing.setChecker(checker);
					  mShowing.setUnitName(unitName);
					  mShowing.setSectionName(sectionName);
					  mHRiskNoticeList.add(mShowing);
				  }
				}
				 String rows = jsonO.getString("rows");
				 mR.put("rows", rows);
			}
		}
		mR.put("v", mHRiskNoticeList);
		return mR;
	}
	/**
	 * GPS定位-在岗查询
	 * @param jsonStr
	 * @throws JSONException 
	 */
	public Map<String,Object> getEmpStateList(String jsonStr) throws JSONException {     
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_LocationInfo> mLocationInfoList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				String rows = jsonO.getString("rows");
				mR.put("rows", rows);
				JSONArray jsonAry = new JSONArray(jsonO.getString("list"));
				if (jsonAry != null && jsonAry.length() > 0) {
					mLocationInfoList = new ArrayList<_LocationInfo>();
					for (int i = 0; i < jsonAry.length(); i++) {
						_LocationInfo mLoc = new _LocationInfo();
						JSONObject jsonI = (JSONObject) jsonAry.get(i);
						String emp_id = jsonI.getString("emp_id");
						String emp_name = jsonI.getString("emp_name");
						String dpt_name = jsonI.getString("dpt_name");
						String r_name = jsonI.getString("r_name");
						String num = jsonI.getString("num");

						mLoc.setEmp_id(emp_id);
						mLoc.setEmp_name(emp_name);
						mLoc.setDpt_name(dpt_name);
						mLoc.setNum(num);
						mLoc.setR_name(r_name);
						mLocationInfoList.add(mLoc);
					}
				}
			}
		}
		mR.put("v", mLocationInfoList);
		return mR;
	}
	@Deprecated
	public Map<String,Object> getProjectOfRound(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_ProjectLoc> mProjectLocList = null;
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetNearByUnitResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
			JSONObject jsonA = new JSONObject(jsonObj.getString("v"));
			JSONArray jsonB = new JSONArray(jsonA.getString("projectList"));
			if (jsonB != null && jsonB.length() > 0) {
				mProjectLocList = new ArrayList<_ProjectLoc>();
				for (int i = 0; i < jsonB.length(); i++) {
					_ProjectLoc mLoc = new _ProjectLoc();
					JSONObject jsonI = (JSONObject) jsonB.get(i);
					String projectName = jsonI.getString("projectName");
					String projectId = jsonI.getString("projectId");
					
					JSONArray jsonT = new JSONArray(jsonI.getString("gpsList"));
					
					List<_LocInfo> mLocInfoList = null;
					if(jsonT != null && jsonT.length() > 0) {
						mLocInfoList = new ArrayList<_LocInfo>();
						for(int j = 0; j < jsonT.length(); j ++) {
							JSONObject jsonX = (JSONObject) jsonT.get(j);
							_LocInfo mLocInfo = new _LocInfo();
							
							String lat = jsonX.getString("lat");
							String lng = jsonX.getString("lng");
							
							if( j == jsonT.length() / 2) {
								mLoc.setmCenterLat(lat);
								mLoc.setmCenterLng(lng);
							}
							mLocInfo.setLat(lat);
							mLocInfo.setLng(lng);
							mLocInfoList.add(mLocInfo);
						}
					}
					mLoc.setmLocInfoList(mLocInfoList);
					mLoc.setProjectId(projectId);
					mLoc.setProjectName(projectName);
					mProjectLocList.add(mLoc);
				}
				mR.put("v", mProjectLocList);
			}
		}
		return mR;
	}
	@Deprecated
	public Map<String,Object> getRiskCount(String jsonStr) throws Exception {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetRiskCountResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if("ok".equals(succStr)) {
				int rows = jsonObj.getInt("v");
				mR.put("v", rows);
		}
		return mR;
	}
	
	/**
	 * 风险检查-获取子提示
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> checkHoleList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<_HoleFace> dataList = new ArrayList<_HoleFace>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					if(jsonD != null) {
						_HoleFace mHole = new _HoleFace();
						String rowId = jsonD.getString("rowId");
						String aPosition = jsonD.getString("aPosition");
						String aClass = jsonD.getString("aClass");
						String aMemo = jsonD.getString("aMemo");
						String aMile = jsonD.getString("aMile");
						
						mHole.set_id(rowId);
						mHole.setCurrMile(aMile);
						mHole.setHole(aPosition);
						mHole.setRiskDegree(aClass);
						mHole.setRiskHSuggest(aMemo);
						dataList.add(mHole);
					}
				}
			}
			mR.put("v", dataList);
		}
		return mR;
	}
	/**
	 * 风险检查-获取子提示
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> checkHoleDetail(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<Map<String,Map<String,List<_RiskInfo>>>> mAllListMap = new ArrayList<Map<String,Map<String,List<_RiskInfo>>>>();
		//List<Map<key1,Map<key2,List<_RiskInfo>>>>----key1:type of risk,key2:sub type of risk
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONArray jsonA = new JSONArray(jsonObj.getString("v"));
			if(jsonA != null && jsonA.length() > 0) {
				for(int k = 0; k < jsonA.length(); k ++) {
					Map<String, Map<String,List<_RiskInfo>>> mMapFather = new HashMap<String, Map<String,List<_RiskInfo>>>();
					JSONObject jsonB = (JSONObject)jsonA.get(k);
					if(jsonB != null) {
						Iterator<?> iter = jsonB.keys();
						while (iter.hasNext()) {
							String entry = (String)iter.next();
							JSONArray jsonC = new JSONArray(jsonB.getString(entry));
							Map<String,List<_RiskInfo>> mMapSon = null;
							if(jsonC != null && jsonC.length() > 0) {
								mMapSon = new HashMap<String, List<_RiskInfo>>();
								for(int c = 0; c < jsonC.length(); c ++) {
									JSONObject jsonD = (JSONObject)jsonC.get(c);
									if(jsonD != null) {//maybe it has a little difficult
										_RiskInfo mRiskInfo = new _RiskInfo();
										mRiskInfo.setTwoType(jsonD.getString("item2"));
										mRiskInfo.setRiskContent(jsonD.getString("riskcontent"));
										JSONArray jsonSubAry = new JSONArray(jsonD.getString("childs"));
										List<_RiskInfo> mSubList = null;
										if(jsonSubAry != null && jsonSubAry.length() > 0) {
											mSubList = new ArrayList<_RiskInfo>();
											for(int j = 0; j < jsonSubAry.length(); j ++) {
												JSONObject jsonE = (JSONObject)jsonSubAry.get(j);
												_RiskInfo mSRiskInfo = new _RiskInfo();
												mSRiskInfo.setTwoType(jsonE.getString("item2"));
												mSRiskInfo.setRiskContent(jsonE.getString("riskcontent"));
												mSubList.add(mSRiskInfo);
											}
										}
										mRiskInfo.setmList(mSubList); 
										if(mMapSon.get(jsonD.getString("item1")) == null) {
											List<_RiskInfo> mList = new ArrayList<_RiskInfo>();
											mList.add(mRiskInfo);
											mMapSon.put(jsonD.getString("item1"), mList);
										} else {
											List<_RiskInfo> mList = mMapSon.get(jsonD.getString("item1"));
											mList.add(mRiskInfo);
										}
								}
							}
						}
						mMapFather.put(entry, mMapSon);
					}
					}
					mAllListMap.add(mMapFather);
				}
			}
		}
		mR.put("v", mAllListMap);
		return mR;
	}
	
	/**
	 * 风险检查-获取图片地址
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> checkPictureList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<String> dataList = new ArrayList<String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					if(jsonD != null) {
						String rowId = jsonD.getString("imgPath");
						dataList.add(rowId);
					}
				}
			}
			mR.put("v", dataList);
		}
		return mR;
	}
	
	/**
	 * 监理监督-查询结果
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getResultList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONObject jsonB = new JSONObject(jsonObj.getString("v"));
			String rows = jsonB.getString("rows");
			mR.put("rows", rows);
			JSONArray jsonAry = new JSONArray(jsonB.getString("list"));
			if(jsonAry != null && jsonAry.length() > 0) {
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					if(jsonD != null) {
						String rowId = jsonD.getString("rowId");
						String aCheckDate = jsonD.getString("aCheckDate");
						String sectionName = jsonD.getString("sectionName");
						String unitName = jsonD.getString("unitName");
						String aStartMile = jsonD.getString("aStartMile");
						String aEndMile = jsonD.getString("aEndMile");
						String checkEmp = jsonD.getString("checkEmp");
						String projName = jsonD.getString("projName");
						
						Map<String,String> mMap = new HashMap<String, String>();
						mMap.put("rowId", rowId);
						mMap.put("aCheckDate", aCheckDate);
						mMap.put("sectionName", sectionName);
						mMap.put("unitName", unitName);
						mMap.put("aStartMile", aStartMile);
						mMap.put("aEndMile", aEndMile);
						mMap.put("checkEmp", checkEmp);
						mMap.put("projName", projName);
						dataList.add(mMap);
					}
				}
			}
			mR.put("v", dataList);
		}
		return mR;
	}
	/**
	 * 监理监督-查询结果内容
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getResultPicture(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetContentResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONObject jsonO = new JSONObject(jsonObj.getString("v"));
			if(jsonO != null) {
				String content = jsonO.getString("content");
				mR.put("content", content);
				JSONArray jsonAry = new JSONArray(jsonO.getString("imglist"));
				if(jsonAry != null && jsonAry.length() > 0) {
					ArrayList<String> dataList = new ArrayList<String>();
					for(int i = 0; i < jsonAry.length(); i++) {
						JSONObject jsonD = (JSONObject)jsonAry.get(i);
						if(jsonD != null) {
							String imgPath = jsonD.getString("imgPath");
							dataList.add(imgPath);
						}
					}
					mR.put("v", dataList);
				}
			}
		}
		return mR;
	}
	/**
	 * 监理监督-查询已检查工程个数
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> checkProjectNumsList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		if ("ok".equals(succStr)) {
			JSONArray jsonAry = new JSONArray(jsonObj.getString("v"));
			if(jsonAry != null && jsonAry.length() > 0) {
				for(int i = 0; i < jsonAry.length(); i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					if(jsonD != null) {
						Map<String,String> dataMap = new HashMap<String,String>();
						String rowId = jsonD.getString("rowId");
						String cCount = jsonD.getString("cCount");
						String aPname = jsonD.getString("aPname");
						dataMap.put("rowId", rowId);
						dataMap.put("cCount", cCount);
						dataMap.put("aPname", aPname);
						dataList.add(dataMap);
					}
				}
			}
			mR.put("v", dataList);
		}
		return mR;
	}
	/**
	 * 工程查阅-图片列表数据
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	GetListResponse{GetListResult={"s":"ok","v":{"rows":2,"list":[
	
	{"fileId":"5d7a0af8-f223-4df3-ad80-3b56410f67fc","rowId":"aa6761fa-6a32-47db-9e99-24fc73d9203a","allcode":null,
	"allName":null,"title":"1406883332941","dtname":"图片","tname":"图片","createperson":"管理员","createtime":"2014-08-01","filetype":".jpg","filesize":2642.61328125,
	"path":"UploadFiles/2014/08/01/f2e84af2e2774033ac0b5911b811663e.jpg"},
	
	{"fileId":"db9eb936-04cf-4be0-98e4-b6c5f191da35","rowId":"0ea42017-8ed3-49a6-8587-4a175cf5c69f",
	"allcode":null,"allName":null,"title":"1406883332941","dtname":"图片","tname":"图片","createperson":"管理员","createtime":"2014-08-01","filetype":".jpg","filesize":2642.61328125,
	"path":"UploadFiles/2014/08/01/fb62442488124253a06cde20dd20c774.jpg"}]}}; }
	 */
	public ArrayList<HashMap<String, String>> getSectionImageList(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		if ("ok".equals(succStr)) {
			JSONObject jsonBbj = jsonObj.getJSONObject("v");
			JSONArray jsonAry = jsonBbj.getJSONArray("list");
			//Value {"list":[],"rows":0} at v of type org.json.JSONObject cannot be converted to JSONArray
			if(jsonAry != null && jsonAry.length() > 0) {
				ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
				for(int i = 0,len = jsonAry.length(); i < len; i++) {
					JSONObject jsonD = (JSONObject)jsonAry.get(i);
					if(jsonD != null) {
						HashMap<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("fileId", jsonD.getString("fileId"));
						dataMap.put("rowId", jsonD.getString("rowId"));
						//dataMap.put("allcode", jsonD.getString("allcode"));
						//dataMap.put("allName", jsonD.getString("allName"));
						dataMap.put("photo_name", jsonD.getString("title"));
						dataMap.put("dtname", jsonD.getString("dtname"));
						//dataMap.put("createperson", jsonD.getString("createperson"));
						//dataMap.put("createtime", jsonD.getString("createtime"));
						//dataMap.put("filetype", jsonD.getString("filetype"));
						
						dataMap.put("path", jsonD.getString("path"));
						
						dataList.add(dataMap);
					}
				}
				return dataList;
			}
		}
		return null;
	}
	
	/**
	 * 工程查阅-图片上传
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,String> getSectionUpdateBack(String jsonStr) throws JSONException {
		Map<String,String> mR = new HashMap<String,String>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"AttachmentUploadResult");
		String version = jsonObj.getString("s");
		String path = jsonObj.getString("v");
		mR.put("s", version);
		mR.put("v", path);
		return mR;
	}
	/**
	 * gps-轨迹查询
	 * @param jsonStr
	 * @throws JSONException
	 */
	public ArrayList<String> getGpsRoute(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetEmployeeLocusVisibleIdsResult");
		String s = jsonObj.getString("s");
		if("ok".equalsIgnoreCase(s)) {
			JSONArray v = jsonObj.getJSONArray("v");
			if(v != null && v.length() > 0) {
				ArrayList<String> list = new ArrayList<String>();
				for(int i = 0,len = v.length();i < len; i ++) {
					list.add((String) v.get(i));
				}
				return list;
			}
		}
		return null;
	}
	/**
	 * 发文/收文 JSON 数据解析
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public HashMap<String, Object> getSendDocumentReady(String jsonStr, String mDocType) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr, "ReadyForApproveResult");
		String s = jsonObj.getString("s");
		HashMap<String, Object> map = null;
		if("ok".equalsIgnoreCase(s)) {
			//action
			map = new HashMap<String, Object>();
			JSONObject dataV = jsonObj.getJSONObject("v");
			if(dataV != null) {
				if(Constansts.TYPE_OF_DOCUMENTSEND.equals(mDocType)) {//发文
					String action = dataV.getString("action");
					map.put("action", action);
					if("distribute".equals(action)) {//需要分发
						JSONArray dataAry = dataV.getJSONArray("data");
						ArrayList<HashMap<String, Object>> mCacheList = recursionJSONAry(dataAry);
						map.put("data", mCacheList);
						/*if(dataAry != null && dataAry.length() > 0) {
							mCacheList = new ArrayList<HashMap<String, String>>();
							for(int i = 0,len = dataAry.length(); i < len; i ++) {
								HashMap<String, Object> mParentMap = new HashMap<String, Object>();
								JSONObject mParentObj = dataAry.getJSONObject(i);
								if(mParentObj != null) {
									mParentMap.put("Name", mParentObj.getString("Name"));
									mParentMap.put("Id", mParentObj.getString("Id"));
									JSONArray mParentAry = mParentObj.getJSONArray("EmployeeList");
									ArrayList<HashMap<String, String>> mEmployeeList = null;
									if(mParentAry != null && mParentAry.length() > 0) {
										mEmployeeList = new ArrayList<HashMap<String, String>>();
										for(int j = 0,lenJ = mParentAry.length(); j < lenJ; j ++) {
											JSONObject mEmpObj = mParentAry.getJSONObject(j);
											if(mEmpObj != null) {
												HashMap<String, String> mEmpMap = new HashMap<String, String>();
												mEmpMap.put("Id", mEmpObj.getString("Id"));
												mEmpMap.put("Name", mEmpObj.getString("Name"));
												mEmployeeList.add(mEmpMap);
											}
										}
									}
									JSONArray mParentChildAry = mParentObj.getJSONArray("Children");
									ArrayList<HashMap<String, Object>> mChildrenList = null;
									if(mParentChildAry != null && mParentChildAry.length() > 0) {
										mChildrenList = new ArrayList<HashMap<String, Object>>();
										
									}
								}
							}
						}*/
					} else if("suggestion".equals(action)) {//审批意见
						JSONObject dataObj = dataV.getJSONObject("data");
						if(dataObj != null) {
							/*
							 "emplist":[
										{"id":"caadc35d-b0fb-4aa8-b56a-5b977dc6db24","name":"财审处",
		 								"emps":[
		 		 					   			{"id":"4700b334-d0ee-47f7-8a7c-e172b45a66bc","name":"余剑锋"}
		 									   ]
		 								},...
							 */
							map.put("stepName", dataObj.getString("stepName"));//当前步骤的名称
							map.put("template", dataObj.getString("template"));//意见模板
							JSONArray mEmpList = dataObj.getJSONArray("emplist");
							ArrayList<HashMap<String, Object>> mEmpCacheList = null;
							if(mEmpList != null && mEmpList.length() > 0) {
								mEmpCacheList = new ArrayList<HashMap<String,Object>>();
								for(int i = 0,len = mEmpList.length(); i < len; i ++) {
									JSONObject mDptObj = mEmpList.getJSONObject(i);
									HashMap<String,Object> mDptMap = new HashMap<String, Object>();
									if(mDptObj != null) {
										mDptMap.put("id", mDptObj.getString("id"));
										mDptMap.put("name", mDptObj.getString("name"));
										JSONArray mEmpAry = mDptObj.getJSONArray("emps");
										ArrayList<HashMap<String, String>> mEmpsList = null;
										
										if(mEmpAry != null && mEmpAry.length() > 0) {
											mEmpsList = new ArrayList<HashMap<String, String>>();
											for(int j = 0, lenJ = mEmpAry.length(); j < lenJ; j ++) {
												JSONObject mEmpObj = mEmpAry.getJSONObject(j);
												if(mEmpObj != null) {
													HashMap<String, String> mEmpMap = new HashMap<String, String>();
													mEmpMap.put("id", mEmpObj.getString("id"));
													mEmpMap.put("name", mEmpObj.getString("name"));
													mEmpsList.add(mEmpMap);
												}
											}
										}
										mDptMap.put("EmpsList", mEmpsList);
									}
									mEmpCacheList.add(mDptMap);
								}
							}
							map.put("emplist", mEmpCacheList);//下一步骤可选择执行人
							map.put("laststepdate", dataObj.getString("laststepdate"));//上一步骤处理时间
						}
					} else { //要红头文件(noAttachment)
						
					}
				} else {//收文
					//ReadyForApproveResponse{ReadyForApproveResult={"s":"ok","v":{"needDeal":true,"canEnd":false,"emplist":[]}}; }
					if(dataV != null) {
						map.put("needDeal", dataV.getString("needDeal"));
						map.put("canEnd", dataV.getString("canEnd"));
						JSONArray dataAry = dataV.getJSONArray("emplist");
						ArrayList<HashMap<String, Object>> mDptList = null;
						if(dataAry != null && dataAry.length() > 0) {
							mDptList = new ArrayList<HashMap<String, Object>>();
							for(int i = 0,len = dataAry.length(); i < len; i ++) {//级别
								/*
								 {"DptId":"00000000-0000-0000-0000-000000000000","DptName":"副主任",
	        					"Employee":[{"rowId":"0012f9e2-ba66-438f-8edb-1fa9ec639ccf","empUser":"huym","empName":"胡玉明","roleName":"副主任","roleId":"48e7e4a6-4b43-4531-8c7f-a4f6761cde62"},
	                    		{"rowId":"c8349801-8a49-4bd2-bc77-492c085a135b","empUser":"xuyb","empName":"徐义标","roleName":"常务副主任","roleId":"3d94e94d-e123-4d58-93b1-20f34c6b875a"},
	                    		{"rowId":"51fc007f-782c-4e23-998a-4b305b0b232a","empUser":"guosm","empName":"郭索敏","roleName":"副主任","roleId":"48e7e4a6-4b43-4531-8c7f-a4f6761cde62"},
	                    		{"rowId":"03174a39-0324-45a1-a12e-9890a89cd794","empUser":"zhangls","empName":"张龙生","roleName":"副主任","roleId":"48e7e4a6-4b43-4531-8c7f-a4f6761cde62"},
	                    		{"rowId":"f77b9983-90a4-40df-a896-bc6969b1b643","empUser":"sunh","empName":"孙宏","roleName":"副主任","roleId":"48e7e4a6-4b43-4531-8c7f-a4f6761cde62"}]},
								 */
								HashMap<String, Object> emp = null;
								JSONObject dptObj = dataAry.getJSONObject(i);
								if(dptObj != null) {
									emp = new HashMap<String, Object>();
									emp.put("DptId", dptObj.getString("DptId"));
									emp.put("DptName", dptObj.getString("DptName"));
									JSONArray dptAry = dptObj.getJSONArray("Employee");
									ArrayList<HashMap<String, String>> mEmpList = null;
									if(dptAry != null && dptAry.length() > 0) {
										mEmpList = new ArrayList<HashMap<String, String>>();
										for(int j = 0,lenJ = dptAry.length(); j < lenJ; j ++) {//人员
											JSONObject empObj = dptAry.getJSONObject(j);
											HashMap<String, String> empMap = new HashMap<String, String>();
											empMap.put("rowId", empObj.getString("rowId"));
											empMap.put("empUser", empObj.getString("empUser"));
											empMap.put("empName", empObj.getString("empName"));
											empMap.put("roleName", empObj.getString("roleName"));
											empMap.put("roleId", empObj.getString("roleId"));
											mEmpList.add(empMap);
										}
									}
									emp.put("Employee", mEmpList);
									mDptList.add(emp);
								}
							}
							map.put("emplist", mDptList);
						}
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 递归发文的JSON数据
	 * @return
	 * @throws JSONException 
	 *  "Name":"昌宁项目办","Id":"6036d6b0-0db2-45df-ab89-3d1270647432","EmployeeList":[
				 {"Id":"1601c467-3a06-421a-84c9-f04ab35783e7","Name":"超级管理员"},
                 {"Id":"df442f37-a64d-4a64-9035-e1116a0aa8e7","Name":"管理员"}],
                 "Children":[{"Name":"项目办领导","Id":"187182ab-e96a-42de-b9d3-0bab976cd33a","EmployeeList":[
                 {"Id":"51fc007f-782c-4e23-998a-4b305b0b232a","Name":"郭索敏"},
                 {"Id":"0012f9e2-ba66-438f-8edb-1fa9ec639ccf","Name":"胡玉明"},
                 {"Id":"564dd3fc-ca9a-4c19-8c83-a699083171fd","Name":"刘云川"},
                 {"Id":"f77b9983-90a4-40df-a896-bc6969b1b643","Name":"孙宏"},
                 {"Id":"c8349801-8a49-4bd2-bc77-492c085a135b","Name":"徐义标"},
                 {"Id":"03174a39-0324-45a1-a12e-9890a89cd794","Name":"张龙生"},
                 {"Id":"1013cd98-3c8d-4ea6-99a2-962a81658724","Name":"曾云谋"}],"Children":[]},
	 */
	public ArrayList<HashMap<String, Object>> recursionJSONAry(JSONArray dataAry) throws JSONException {
		ArrayList<HashMap<String, Object>> mCacheList = null;
		if(dataAry != null && dataAry.length() > 0) {
			mCacheList = new ArrayList<HashMap<String, Object>>();
			for(int i = 0,len = dataAry.length(); i < len; i ++) {
				JSONObject mParentObj = dataAry.getJSONObject(i);
				if(mParentObj != null) {
					HashMap<String, Object> mParentMap = new HashMap<String, Object>();
					mParentMap.put("Name", mParentObj.getString("Name"));
					mParentMap.put("Id", mParentObj.getString("Id"));
					mParentMap.put("attribute", "parent");//区分 父级/子级
					JSONArray mParentAry = mParentObj.getJSONArray("EmployeeList");
					ArrayList<HashMap<String, String>> mEmployeeList = null;
					if(mParentAry != null && mParentAry.length() > 0) {
						mEmployeeList = new ArrayList<HashMap<String, String>>();
						for(int j = 0,lenJ = mParentAry.length(); j < lenJ; j ++) {
							JSONObject mEmpObj = mParentAry.getJSONObject(j);
							if(mEmpObj != null) {
								HashMap<String, String> mEmpMap = new HashMap<String, String>();
								mEmpMap.put("Id", mEmpObj.getString("Id"));
								mEmpMap.put("Name", mEmpObj.getString("Name"));
								mEmpMap.put("attribute", "child");//区分 父级/子级
								mEmployeeList.add(mEmpMap);
							}
						}
					}
					mParentMap.put("EmployeeList", mEmployeeList);
					JSONArray mParentChildAry = mParentObj.getJSONArray("Children");
					if(mParentChildAry != null && mParentChildAry.length() > 0) {
						mParentMap.put("Children", recursionJSONAry(mParentChildAry));
					}
					mCacheList.add(mParentMap);
				}
			}
		}
		return mCacheList;
	}
	
	/**
	 * 更新成功
	 */
	public boolean commonJsonParse(String jsonStr) throws JSONException {
		JSONObject jsonObj = getHandlerJSON(jsonStr,"UpdateResult");
		String succStr = jsonObj.getString("s");
		if("ok".equalsIgnoreCase(succStr)) {
			return true;
		}
		return false;
	}
	/**
	 * GetListResponse{GetListResult={"s":"ok","v":{"rows":38,"list":[
	 * {"rowId":"90c8235e-dc9d-4fa1-a941-230f57269a3c",
	 * 	"miSecId":"aad94180-a620-4af9-963b-819262f89d3a",
	 *  "secType":"施工标",
	 *  "secName":"C5",
	 *  "miCode":"CNJDJL-C05-201407A",
	 *  "miIssue":1,
	 *  "miMDate":"2014-07-12",
	 *  "miStartDate":"2014-07-12",
	 *  "miEndDate":"2014-07-26",
	 *  "miTabDate":"2014-07-12",
	 *  "miTabulator":"张静翔",
	 *  "paybillFlowid":null,
	 *  "paybillFlowstatus":0,
	 *  "paycertFlowid":"a103df9c-6744-44c4-99dd-4e736833602e",
	 *  "paycertFlowstatus":2,
	 *  "contractIntermStatus":2,
	 *  "changeIntermStatus":"",
	 *  "noticeFlowstate":2,
	 *  "tabstatus":3,
	 *  "hType":"[column unavailable]",
	 *  "bType":"[column unavailable]",
	 *  "zType":"[column unavailable]",
	 *  "fType":"[column unavailable]",
	 *  "dContNum":"[column unavailable]",
	 *  "dChgNum":"[column unavailable]",
	 *  "chgNum":"[column unavailable]",
	 *  "MeasureCanHandle":"[column unavailable]",
	 *  "MeasureCanRollBack":"[column unavailable]",
	 *  "CertCanHandle":false,"CertEarlyFinish":false},
	 * @param jsonStr
	 * @desc  计量列表数据
	 * @throws JSONException
	 */
	public Map<String,Object> getMeasureList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		List<Map<String, String>> mMeasureList = null;
		if("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonBbj = jsonObj.getJSONObject("v");
			JSONArray jsonAry = jsonBbj.getJSONArray("list");
			if(jsonAry != null && jsonAry.length() > 0) {
				mMeasureList = new ArrayList<Map<String,String>>();
				for(int i = 0,len = jsonAry.length(); i < len; i ++) {
					JSONObject obj = jsonAry.getJSONObject(i);
					if(obj != null) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("rowId", obj.getString("rowId"));
						map.put("miSecId", obj.getString("miSecId"));
						map.put("secType", obj.getString("secType"));//标段类型
						map.put("secName", obj.getString("secName"));//所属标段
						map.put("miCode", obj.getString("miCode"));//月计量号
						map.put("miIssue", obj.getString("miIssue"));//期数
						map.put("miMDate", obj.getString("miMDate"));//计量日期
						map.put("miStartDate", obj.getString("miStartDate"));
						map.put("miEndDate", obj.getString("miEndDate"));//截止日期
						map.put("miTabDate", obj.getString("miTabDate"));//制表日期
						map.put("miTabulator", obj.getString("miTabulator"));//制表人
						map.put("paybillFlowid", obj.getString("paybillFlowid"));//清单流程id
						map.put("paybillFlowstatus", obj.getString("paybillFlowstatus"));
						map.put("paycertFlowid", obj.getString("paycertFlowid"));//paycertFlowid 
						map.put("paycertFlowstatus", obj.getString("paycertFlowstatus"));
						map.put("contractIntermStatus", obj.getString("contractIntermStatus"));
						map.put("noticeFlowstate", obj.getString("noticeFlowstate"));
						map.put("noticeFlowid", obj.getString("noticeFlowid")); //付款通知单Id
						map.put("tabstatus", obj.getString("tabstatus"));
						map.put("hType", obj.getString("hType"));
						map.put("bType", obj.getString("bType"));
						map.put("zType", obj.getString("zType"));
						map.put("fType", obj.getString("fType"));
						map.put("dContNum", obj.getString("dContNum"));
						map.put("dChgNum", obj.getString("dChgNum"));
						map.put("chgNum", obj.getString("chgNum"));
						map.put("MeasureCanHandle", obj.getString("MeasureCanHandle"));
						map.put("MeasureCanRollBack", obj.getString("MeasureCanRollBack"));
						map.put("CertCanHandle", obj.getString("CertCanHandle"));
						map.put("CertEarlyFinish", obj.getString("CertEarlyFinish"));
						map.put("contractStatus", obj.getString("contractStatus"));//合同中间计量单状态
						map.put("changeStatus", obj.getString("changeStatus"));//变更中间计量单状态
						map.put("paycertStatus", obj.getString("paycertStatus"));//中期支付证书汇总表状态
						map.put("noticeStatus", obj.getString("noticeStatus"));//付款通知单状态
						
						mMeasureList.add(map);
					}
				}
				mR.put("v", mMeasureList);
			}
		}
		return mR;
	}
	
	/**
	 * 中间计量单列表数据
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public Map<String,Object> getMidMeasureList(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		List<Map<String, String>> mMeasureList = null;
		if("ok".equalsIgnoreCase(succStr)) {
			JSONObject jsonBbj = jsonObj.getJSONObject("v");
			JSONArray jsonAry = jsonBbj.getJSONArray("list");
			if(jsonAry != null && jsonAry.length() > 0) {
				mMeasureList = new ArrayList<Map<String,String>>();
				for(int i = 0,len = jsonAry.length(); i < len; i ++) {
					JSONObject obj = jsonAry.getJSONObject(i);
					if(obj != null) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("rowId", obj.getString("rowId"));
						map.put("imMiId", obj.getString("imMiId"));//期数
						map.put("flowId", obj.getString("flowId"));//流程id
						map.put("imType", obj.getString("imType"));//中间计量单类型
						map.put("imCode", obj.getString("imCode"));//中间计量单号
						map.put("imTabulator", obj.getString("imTabulator"));//制表人
						map.put("imTabDate", obj.getString("imTabDate"));//截止日期
						map.put("imCertificates", obj.getString("imCertificates"));//交工证书号
						map.put("imClientNum", obj.getString("imClientNum"));//计量委托单号
						map.put("imName", obj.getString("imName"));//计量名称
						map.put("imPart", obj.getString("imPart"));//部位
						map.put("imMapNum", obj.getString("imMapNum"));//图号
						map.put("imMile", obj.getString("imMile"));//桩号
						map.put("flowStatus", obj.getString("flowStatus"));//申报状态
						map.put("checkBoxState", "0");//默认为0代表未选择 1代码已经勾选
						mMeasureList.add(map);
					}
				}
				mR.put("v", mMeasureList);
			}
		}
		return mR;
	}
	/**
	 * 中期支付证书汇总表json解析
	 * @param jsonStr
	 * @return
	 * @throws JSONException 
	 */
	public Map<String, Object> getPayCertificate(String jsonStr) throws JSONException {
		Map<String,Object> mR = new HashMap<String,Object>();
		JSONObject jsonObj = getHandlerJSON(jsonStr,"GetListNoPageResult");
		String succStr = jsonObj.getString("s");
		mR.put("s", succStr);
		List<Map<String, String>> mMeasureList = null;
		if("ok".equalsIgnoreCase(succStr)) {
			JSONArray jsonAry = jsonObj.getJSONArray("v");
			if(jsonAry != null && jsonAry.length() > 0) {
				mMeasureList = new ArrayList<Map<String,String>>();
				for(int i = 0,len = jsonAry.length(); i < len; i ++) {
					JSONObject obj = jsonAry.getJSONObject(i);
					if(obj != null) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("rowId", obj.getString("rowId"));
						map.put("imMiId", obj.getString("miCode"));//期数编号
						map.put("verse", obj.getString("verse"));//章节号
						map.put("pitem", obj.getString("pitem"));//项目内容
						map.put("contactAmount", obj.getString("contactAmount"));//原合同金额(元)
						map.put("afterRevamount", obj.getString("afterRevamount"));//修编后金额(元) (清单核查后金额)
						map.put("afterChangeAmount", obj.getString("afterChangeAmount"));//变更后金额(元)
						map.put("nowAmount", obj.getString("nowAmount"));//至本期末完成金额(元) (本期末累计支付)
						map.put("lastAmount", obj.getString("lastAmount"));//上期末完成金额(元) (上期末累计支付)
						map.put("thisAmount", obj.getString("thisAmount"));//本期完成金额(元) (本期支付 )
						map.put("canEdit", obj.getString("canEdit"));//是否可修改
						mMeasureList.add(map);
					}
				}
				mR.put("v", mMeasureList);
			}
		}
		return mR;
	}
	
	
	/**
	 * 付款通知单数据解析
	 * @author ChenLang
	 */
	public  HashMap<String, ArrayList<HashMap<String, String>>> getPayFormData(String jsonData){
		HashMap<String, ArrayList<HashMap<String, String>>>  baseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
		JSONObject json=null;
		JSONObject projectAttributeJson=null;
		JSONObject currentPeriodsJson=null;
		JSONObject countPayJson=null;
		JSONObject payProjectJson=null;
		JSONObject  deductProjectJson=null;
		String  str=null;
		try {
			json = getHandlerJSON(jsonData,"GetContentResult");
			if(!"ok".equalsIgnoreCase(json.getString("s"))){
				return null;
			}else{
				str=json.getString("v");
				json=new JSONObject(str);
				ArrayList<HashMap<String, String>> arrayProjectAttribute=new ArrayList<HashMap<String,String>>();//项目信息
				ArrayList<HashMap<String, String>> arrayPactAttribute=new ArrayList<HashMap<String,String>>();//合同信息
				ArrayList<HashMap<String, String>> arrayCurrentPeriodPay=new ArrayList<HashMap<String,String>>();//本期付款
				ArrayList<HashMap<String, String>> arrayCountPay=new ArrayList<HashMap<String,String>>(); //累计付款
				ArrayList<HashMap<String, String>> arrayPayProject=new ArrayList<HashMap<String,String>>();//付款项目
				ArrayList<HashMap<String,String>>  arrayDeductProject=new ArrayList<HashMap<String,String>>();//扣款项目
				//项目信息
				projectAttributeJson=new JSONObject(json.getString("ContractInfo"));
				HashMap<String, String>  mapProjectAttribute=new HashMap<String, String>();
				mapProjectAttribute.put("projectName", projectAttributeJson.getString("pro_name"));
				mapProjectAttribute.put("periods", projectAttributeJson.getString("mi_issue"));
				arrayProjectAttribute.add(mapProjectAttribute);
				//合同信息
				HashMap<String, String>  mapPactAttribute=new HashMap<String, String>();
				mapPactAttribute.put("pactName", projectAttributeJson.getString("cont_contname"));
				mapPactAttribute.put("pactNo", projectAttributeJson.getString("cont_contcode"));
				mapPactAttribute.put("pactTotal", projectAttributeJson.getString("contamount"));
				arrayPactAttribute.add(mapPactAttribute);
				//本期付款
				currentPeriodsJson=new JSONObject(json.getString("ThisTime"));
				HashMap<String, String> mapCurrentPeriodPay=new HashMap<String, String>();
				mapCurrentPeriodPay.put("currentPeriodPrice", currentPeriodsJson.getString("thisamount"));
				mapCurrentPeriodPay.put("currentPeriodScale", currentPeriodsJson.getString("thisamountbfb"));
				mapCurrentPeriodPay.put("currentPeriodWrite", currentPeriodsJson.getString("thisamounts"));
				arrayCurrentPeriodPay.add(mapCurrentPeriodPay);
				//累计付款
				countPayJson=new JSONObject(json.getString("Tatol"));
			    HashMap<String,String>  mapCountPay=new HashMap<String, String>();
			    mapCountPay.put("countPayPrice", countPayJson.getString("nowamount"));
			    mapCountPay.put("countPayScale", countPayJson.getString("nowamountsbfb"));
			    mapCountPay.put("countPayWrite", countPayJson.getString("nowamounts"));
			    arrayCountPay.add(mapCountPay);
			    //付款项目
			    payProjectJson=new JSONObject(json.getString("Debit"));
			    if(payProjectJson!=null){			    	
			    	JSONArray  jsonArray=new JSONArray(payProjectJson.getString("Items"));
			    	//添加标题
			    	HashMap<String, String> mapTitle=new HashMap<String, String>();
			    	mapTitle.put("projectName", "项目名称");
			    	mapTitle.put("upCountMoney", "上期累计金额");
			    	mapTitle.put("currentPeriodMoney", "本期金额");
			    	mapTitle.put("countMoney", "累计金额");
			    	arrayPayProject.add(mapTitle);
			    	//添加内容
			    	for(int i=0;i<jsonArray.length();i++){
			    		HashMap<String, String> map=new HashMap<String, String>();
			    		JSONObject  jsonObj=jsonArray.getJSONObject(i);
			    		map.put("projectName", jsonObj.getString("name"));
			    		map.put("upCountMoney", jsonObj.getString("lastamount"));
			    		map.put("currentPeriodMoney", jsonObj.getString("thisamount"));
			    		map.put("countMoney",jsonObj.getString("nowamount"));
			    		arrayPayProject.add(map);
			    	}
			    }
			    //扣款项目
			    deductProjectJson=new JSONObject(json.getString("Credit"));
			    if(deductProjectJson!=null){
			    	JSONArray jsonArray=new JSONArray(deductProjectJson.getString("Items"));
			    	//添加标题
			    	HashMap<String, String> mapTitle=new HashMap<String, String>();
			    	mapTitle.put("projectName", "项目名称");
			    	mapTitle.put("upCountMoney", "上期累计金额");
			    	mapTitle.put("currentPeriodMoney", "本期金额");
			    	mapTitle.put("countMoney", "累计金额");
			    	arrayDeductProject.add(mapTitle);
			    	//添加内容
			    	for(int i=0;i<jsonArray.length();i++){
			    		HashMap<String, String>  map=new HashMap<String, String>();
			    		JSONObject jsonObj=jsonArray.getJSONObject(i);
			    		map.put("projectName", jsonObj.getString("name"));
			    		map.put("upCountMoney", jsonObj.getString("lastamount"));
			    		map.put("currentPeriodMoney", jsonObj.getString("thisamount"));
			    		map.put("countMoney",jsonObj.getString("nowamount"));
			    		arrayDeductProject.add(map);
			    	}
			    }
			    //添加所有子选项
			    baseMap.put("arrayProjectAttribute", arrayProjectAttribute);
			    baseMap.put("arrayPactAttribute", arrayPactAttribute);
			    baseMap.put("arrayCurrentPeriodPay", arrayCurrentPeriodPay);
			    baseMap.put("arrayCountPay", arrayCountPay);
			    baseMap.put("arrayPayProject", arrayPayProject);
			    baseMap.put("arrayDeductProject", arrayDeductProject);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return baseMap;
	}
	
	/**
	 * 解析合同计量单清单数据
	 * @author ChenLang
	 */
	public ArrayList<HashMap<String, String>> getBillData(String jsonData){
		ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
		JSONObject jsonObject;
		String strJson=null;
		try{
			jsonObject=getHandlerJSON(jsonData, "GetListNoPageResult");
			if(!"ok".equals(jsonObject.get("s"))){
				return null;
			}else{
				strJson=jsonObject.getString("v");
				JSONArray 	jsonArray=new JSONArray(strJson);
				for(int i=0;i<jsonArray.length();i++){
					jsonObject=jsonArray.getJSONObject(i);
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("rowId",jsonObject.getString("rowId"));
					map.put("imid", jsonObject.getString("imid"));
					map.put("projectName", jsonObject.getString("allCode"));
					map.put("billNo", jsonObject.getString("bCode"));
					map.put("billName", jsonObject.getString("bName"));
					map.put("count", jsonObject.getString("mount"));
					map.put("declareCount", jsonObject.getString("declarenum"));
					map.put("declarerateScale", jsonObject.getString("declarerate"));
					map.put("verifyCount", jsonObject.getString("finalnum"));
					map.put("verifyScale", jsonObject.getString("finalrate"));
					map.put("approveCount", jsonObject.getString("imdApprovenum"));
					map.put("currentSurplusCount", jsonObject.getString("thisSurplusNum"));
					map.put("currentSurplusScale", jsonObject.getString("thisSurplusRatio"));
					map.put("priorPeriodCount", jsonObject.getString("imdLastrestnum"));
					map.put("priorPeriodScale", jsonObject.getString("imdLastrestrate"));
					map.put("allSurplusCount", jsonObject.getString("imdAllrestnum"));
					map.put("allSurplusScale", jsonObject.getString("imdAllrestrate"));
				   arrayList.add(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return arrayList;
	}
	
/**
 * 获取计量流程审批信息
	 * type
	 * 
	 *  ReadyForApproveResult  中期支付证书汇总表审批信息获取
	 *  
 * @author ChenLang
 * 
 */
	public  HashMap<String, ArrayList<HashMap<String, String>>> getApprovedata(String jsonData){
		HashMap<String, ArrayList<HashMap<String, String>>> baseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
		ArrayList<HashMap<String, String>>  arrayListApproveAttribute=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>>  arrayListApproveDeptInfo=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>>  arrayListApprovePersonInfo=new ArrayList<HashMap<String,String>>();
		JSONObject jsonObject=null;
		try {
			jsonObject = getHandlerJSON(jsonData,"ReadyForApproveResult");
			if("ok".equalsIgnoreCase(jsonObject.getString("s"))){
			JSONObject json= new JSONObject(jsonObject.getString("v"));
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("flowId", json.getString("flowid"));//流程Id
			map.put("stepId", json.getString("stepId")); //步骤Id
			map.put("stepName",json.getString("stepName"));//步骤名称
			map.put("prvFlowDate", json.getString("prvFlowDate"));//上一步处理的时间
			map.put("hasNext", json.getString("hasNext"));//是否有下一步
			arrayListApproveAttribute.add(map);
			if("true".equalsIgnoreCase(json.getString("hasNext"))){
				JSONArray jsonArray=new JSONArray(json.getString("emplist"));
				for(int i=0;i<jsonArray.length();i++){
					//部门信息
					HashMap<String, String> mapDept=new  HashMap<String, String>();
					JSONObject jsonObj=jsonArray.getJSONObject(i);
					mapDept.put("deptId", jsonObj.getString("id"));
					mapDept.put("deptName", jsonObj.getString("name"));
					int count=0;
					//员工信息
					JSONArray jsonObjectDept=new JSONArray(jsonObj.getString("emps"));
					for(int j=0;j<jsonObjectDept.length();j++){
						JSONObject jsonObjPerson=jsonObjectDept.getJSONObject(j);
						count++;
						HashMap<String, String> mapPerson=new HashMap<String, String>();
						mapPerson.put("personId", jsonObjPerson.getString("id"));
						mapPerson.put("personName", jsonObjPerson.getString("name"));
						mapPerson.put("checkBoxState", "0");//默认为0 未勾选 1勾选
						arrayListApprovePersonInfo.add(mapPerson);
					}
					mapDept.put("count", String.valueOf(count));
					arrayListApproveDeptInfo.add(mapDept);
				}
			}
			//添加所有信息
			baseMap.put("approveAttribute", arrayListApproveAttribute);
			baseMap.put("approveDeptInfo", arrayListApproveDeptInfo);
			baseMap.put("arrayListApprovePersonInfo", arrayListApprovePersonInfo);
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return baseMap;
	}
	
	/** 解析审批结果
	 * @author ChenLang
	 * @return true 通过  false 未通过
	 */
	
	public boolean getApproveResult(String  jsonData){
		JSONObject json=null;
		 try {
			json=getHandlerJSON(jsonData, "ApproveResult");
			if(json!=null){				
				if("ok".equalsIgnoreCase(json.getString("s"))){
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * 中期支付证书汇总表审批信息获取
	 * @author ChenLang
	 * 
	 */
	
	public HashMap<String, ArrayList<HashMap<String, String>>>  getPaymentCertificate(String jsonData){
		HashMap<String, ArrayList<HashMap<String, String>>> baseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
		ArrayList<HashMap<String, String>>   arrayListPaymentCertificate=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>>    arrayListDeptInfo=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>>    arrayListPersonInfo=new ArrayList<HashMap<String,String>>();
		JSONObject json=null;
		try {
			json=getHandlerJSON(jsonData, "ReadyForApproveResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONObject  jsonObject=new JSONObject(json.getString("v"));
				HashMap<String, String>  map=new HashMap<String, String>();
				map.put("flowId", jsonObject.getString("flowid"));
				map.put("stepId", jsonObject.getString("stepId"));
				map.put("stepName", jsonObject.getString("stepName"));
				map.put("prvFlowDate",jsonObject.getString("prvFlowDate"));//上一步处理的时间
				map.put("hasNext", jsonObject.getString("hasNext"));
				arrayListPaymentCertificate.add(map);
				if("true".equalsIgnoreCase(jsonObject.getString("hasNext"))){
					JSONArray jsonArray=new JSONArray(jsonObject.getString("emplist"));
					for(int i=0;i<jsonArray.length();i++){
						//部门信息
						HashMap<String, String> mapDept=new  HashMap<String, String>();
						JSONObject jsonObj=jsonArray.getJSONObject(i);
						mapDept.put("deptId", jsonObj.getString("id"));
						mapDept.put("deptName", jsonObj.getString("name"));
						int count=0;//记录子选项有多少条
						//员工信息
						JSONArray jsonObjectDept=new JSONArray(jsonObj.getString("emps"));
						for(int j=0;j<jsonObjectDept.length();j++){
							JSONObject jsonObjPerson=jsonObjectDept.getJSONObject(j);
							count++;
							HashMap<String, String> mapPerson=new HashMap<String, String>();
							mapPerson.put("personId", jsonObjPerson.getString("id"));
							mapPerson.put("personName", jsonObjPerson.getString("name"));
							mapPerson.put("checkBoxState", "0");//默认为0 未勾选 1勾选
							arrayListPersonInfo.add(mapPerson);
						}
						mapDept.put("count", String.valueOf(count));
						arrayListDeptInfo.add(mapDept);
					}
					//添加所有信息
				}
				baseMap.put("approveAttribute", arrayListPaymentCertificate);
				baseMap.put("approveDeptInfo", arrayListDeptInfo);
				baseMap.put("arrayListApprovePersonInfo", arrayListPersonInfo);
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return baseMap;
	}
	
	/** 
	 * 计量模块流程跟踪解析
	 * @author ChenLang
	 */
	public  String getTaskTrackingData(String jsonData){
		JSONObject json=null;
		try {
			json=getHandlerJSON(jsonData, "FlowInfosResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){			
					json=new JSONObject(json.getString("v"));
					return json.getString("flowContent");

			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * 计量模块
	 * 流程查看JSON解析
	 * @author ChenLang
	 */
	public ArrayList<HashMap<String, String>> getTaskLookData(String jsonData){
		JSONObject json=null;
		ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
		try {
			json=getHandlerJSON(jsonData, "FlowInfosResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				  JSONArray  jsonArray=new JSONArray(json.getString("v"));
				  for(int i=0;i<jsonArray.length();i++){
					  JSONObject jsonObj=jsonArray.getJSONObject(i);
					  HashMap<String, String>  map=new HashMap<String, String>();
					  map.put("status", jsonObj.getString("status"));
					  map.put("flowName", jsonObj.getString("flowName")); //步骤名称
					  map.put("approveTime", jsonObj.getString("dealdate")); //审批时间
					  map.put("approvePerson", jsonObj.getString("emp_name")); //审批人
					  map.put("approveSuggestion", jsonObj.getString("suggestion"));//审批意见
					  arrayList.add(map);
				  }
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  arrayList;
	}
	
	/** 返回指令上传
	 * */
	public boolean  getCommandUploadResult(String jsonData){
		JSONObject json=null;
		try {
			json=getHandlerJSON(jsonData, "AddResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				return true;
			}else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/** 返回指令删除结构*/
	public boolean  getCommandDeleteResult(String jsonData){
		JSONObject json=null;
		try {
			json=getHandlerJSON(jsonData, "DeleteResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				return true;
			}else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 返回附件上传的结果
	 */
	public boolean  getCommandUploadFileResult(String jsonData){
		 JSONObject json=null;
		 try {
			json=getHandlerJSON(jsonData, "DocumentFileUploadResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				return true;
			}else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/** 获取流程启动的人员信息
	 * FlowStartResponse{
         FlowStartResult={
        "s": "ok",
        "v": [
            {
                "id": "d5416f1f-4f7d-40ea-b762-edb41236ac55",
                "name": "江西省嘉和工程咨询监理有限公司",
                "emps": [
                    {
                        "id": "8aa034fe-f874-47a1-b8d6-60441a754b5d",
                        "name": "邓桢颖 （驻地合同工程师）"
                    }
                ]
            }
        ]
    };
}* */
	public HashMap<String, ArrayList<HashMap<String, String>>> getCommandProcedurePerson(String jsonData){
		HashMap<String, ArrayList<HashMap<String, String>>> baseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
		ArrayList<HashMap<String, String>>    arrayListDeptInfo=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>>    arrayListPersonInfo=new ArrayList<HashMap<String,String>>();
		JSONObject json=null;
		  try {
			json=getHandlerJSON(jsonData, "FlowStartResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONArray  jsonArray=new JSONArray(json.getString("v"));
				for(int i=0;i<jsonArray.length();i++){
					JSONObject  jsonObj=jsonArray.getJSONObject(i);
					//部门信息
						HashMap<String, String> mapDept=new  HashMap<String, String>();
						mapDept.put("deptId", jsonObj.getString("id"));
						mapDept.put("deptName", jsonObj.getString("name"));
						int count=0;//记录子选项有多少条
						//员工信息
						JSONArray jsonObjectDept=new JSONArray(jsonObj.getString("emps"));
						for(int j=0;j<jsonObjectDept.length();j++){
							JSONObject jsonObjPerson=jsonObjectDept.getJSONObject(j);
							count++;
							HashMap<String, String> mapPerson=new HashMap<String, String>();
							mapPerson.put("personId", jsonObjPerson.getString("id"));
							mapPerson.put("personName", jsonObjPerson.getString("name"));
							mapPerson.put("checkBoxState", "0");//默认为0 未勾选 1勾选
							arrayListPersonInfo.add(mapPerson);
						}
						mapDept.put("count", String.valueOf(count));
						arrayListDeptInfo.add(mapDept);
					}
					//添加所有信息
				baseMap.put("approveDeptInfo", arrayListDeptInfo);
				baseMap.put("arrayListApprovePersonInfo", arrayListPersonInfo);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		  return baseMap;
	}
	
	/** 获取监控指令审批机构*/
	public boolean  getApproveCommandResult(String jsonData){
		JSONObject  json=null;
		try {
			json=getHandlerJSON(jsonData, "ApproveResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				return true;
			}else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 计息监理指令所有信息
	 * @param jsonData  JSON*/
	public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>>  getCommandData(String jsonData){
		ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> baseArrayList=new ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>();
 		JSONObject json=null;
		try {
			 json=getHandlerJSON(jsonData, "GetListResult");
			if("ok".equalsIgnoreCase(json.getString("s"))){
				JSONObject  baseJsonObj=new JSONObject(json.getString("v"));
				JSONArray baseJsonArray=new JSONArray(baseJsonObj.getString("list"));
				for(int i=0;i<baseJsonArray.length();i++){
					HashMap<String, ArrayList<HashMap<String, String>>>  map=new HashMap<String, ArrayList<HashMap<String,String>>>();
					ArrayList<HashMap<String, String>>   arrayListCommandAttribute=new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String, String>>   arrayListCommandFile=new ArrayList<HashMap<String,String>>(); // 文件集合
					ArrayList<HashMap<String, String>>   arrayListCommandImage=new ArrayList<HashMap<String, String>>(); //图片集合
					JSONObject  jsonAttrube=baseJsonArray.getJSONObject(i);
					JSONArray    jsonArrayFile=new JSONArray(jsonAttrube.getString("attachments"));
					JSONArray    jsonArrayImage=new JSONArray(jsonAttrube.getString("images"));
					HashMap<String, String>  mapAttribute=new HashMap<String, String>();
					mapAttribute.put("rowId", jsonAttrube.getString("rowId"));
					mapAttribute.put("flowId", jsonAttrube.getString("flowId"));//流程Id
					mapAttribute.put("sectionName", jsonAttrube.getString("secName"));//标段名称
					mapAttribute.put("projectName", jsonAttrube.getString("project")); //工程名称
					mapAttribute.put("createTime", jsonAttrube.getString("createTime"));
					mapAttribute.put("empName", jsonAttrube.getString("empName"));
					mapAttribute.put("type", jsonAttrube.getString("type"));
					mapAttribute.put("flowStatus", jsonAttrube.getString("flowStatus")); // 流程状态 值为 0未申报,1待审批2已申报,3已审批,4全部
					mapAttribute.put("content", jsonAttrube.getString("details"));  //
					mapAttribute.put("fileIndex", String.valueOf(i)); //文件索引
					arrayListCommandAttribute.add(mapAttribute);			
					//遍历附件
					for(int j=0;j<jsonArrayFile.length();j++){
						JSONObject jsonFile=jsonArrayFile.getJSONObject(j);
						HashMap<String, String> mapFile=new HashMap<String, String>();
						mapFile.put("fileName", jsonFile.getString("fileName"));
						mapFile.put("filePath", jsonFile.getString("filePath"));
						arrayListCommandFile.add(mapFile);
					}
					//遍历图片
					for(int k=0;k<jsonArrayImage.length();k++){
						JSONObject jsonImage=jsonArrayImage.getJSONObject(k);
						HashMap<String, String> mapImage=new HashMap<String, String>();
						mapImage.put("fileName", jsonImage.getString("fileName"));
						mapImage.put("filePath",jsonImage.getString("filePath"));
						arrayListCommandImage.add(mapImage);
					}
					//添加所有信息
					map.put("commandAttribute", arrayListCommandAttribute);
					map.put("commandFile", arrayListCommandFile);
					map.put("commandImage", arrayListCommandImage);
					baseArrayList.add(map);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baseArrayList;
	}
}

