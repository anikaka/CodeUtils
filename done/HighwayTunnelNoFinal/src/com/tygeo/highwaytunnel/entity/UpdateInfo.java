package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.database.Cursor;

import com.TY.bhgis.Database.DataProvider;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.common.StaticContent;




public class UpdateInfo implements Serializable {
	static String s;
	int id;
	int CheckStaffCode;
	public int getCheckStaffCode() {
		return CheckStaffCode;
	}
	public void setCheckStaffCode(int checkStaffCode) {
		CheckStaffCode = checkStaffCode;
	}
	String TunnelCode ;//隧道编码 
	String RoadCode;
	 String UnitCode; //养护机构  
	 String CheckDate;//检查日期  
	 String Weather;//天气 
	int CheckUnitCode;//检查单位  
	 String DailyCheckType;//检测类型  
	 String TunnelStake;//里程桩号  
	 String BaseCheckTunnelType;//隧道行向  
	 String AttchmentFileGuid;
	 boolean IsSubmit;
	 boolean IsSubmitRating;
	 List<DailyCheckTunnelInfoDto> dailyCheckTunnelInfoDto; 
	 static JSONObject sb,dailyCheckJson;
	 
	 public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoadCode() {
		return RoadCode;
	}
	public void setRoadCode(String roadCode) {
		RoadCode = roadCode;
	}
	public String getAttchmentFileGuid() {
		return AttchmentFileGuid;
	}
	public void setAttchmentFileGuid(String attchmentFileGuid) {
		AttchmentFileGuid = attchmentFileGuid;
	}
	public boolean isIsSubmit() {
		return IsSubmit;
	}
	public void setIsSubmit(boolean isSubmit) {
		IsSubmit = isSubmit;
	}
	public boolean isIsSubmitRating() {
		return IsSubmitRating;
	}
	public void setIsSubmitRating(boolean isSubmitRating) {
		IsSubmitRating = isSubmitRating;
	}
	List<CheckDetailInfo> CheckDetailList;//检查明细 
	public String getTunnelCode() {
		return TunnelCode;
	}
	public void setTunnelCode(String tunnelCode) {
		TunnelCode = tunnelCode;
	}
	public String getUnitCode() {
		return UnitCode;
	}
	public void setUnitCode(String unitCode) {
		UnitCode = unitCode;
	}
	public String getCheckDate() {
		return CheckDate;
	}
	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}
	public String getWeather() {
		return Weather;
	}
	public void setWeather(String weather) {
		Weather = weather;
	}
	public int getCheckUnitCode() {
		return CheckUnitCode;
	}
	public void setCheckUnitCode(int checkUnitCode) {
		CheckUnitCode = checkUnitCode;
	}
	public String getDailyCheckType() {
		return DailyCheckType;
	}
	public void setDailyCheckType(String dailyCheckType) {
		DailyCheckType = dailyCheckType;
	}
	public String getTunnelStake() {
		return TunnelStake;
	}
	public void setTunnelStake(String tunnelStake) {
		TunnelStake = tunnelStake;
	}
	public String getBaseCheckTunnelType() {
		return BaseCheckTunnelType;
	}
	public void setBaseCheckTunnelType(String baseCheckTunnelType) {
		BaseCheckTunnelType = baseCheckTunnelType;
	}
	public List<CheckDetailInfo> getCheckDetailList() {
		return CheckDetailList;
	}
	public void setCheckDetailList(List<CheckDetailInfo> checkDetailList) {
		CheckDetailList = checkDetailList;
	}
	
	/**
	 *获得土建日常上传的任务数据
	 *
	 * */
	public static String getde(String up_down) throws Exception{ 
			JSONObject param=new JSONObject();
			//天气状况,检查日期
			UpdateInfo up=GetBaseUpdateInfo();
			//对应上传的实体类
			List<DailyCheckTunnelInfoDto> cl=new ArrayList<DailyCheckTunnelInfoDto>();
			//
			cl.add(DailyCheckTunnelInfoDto.getUpDemo(up_down));
//			cl.add(DailyCheckTunnelInfoDto.getDownDemo());
			
			
		try {
			param.put("TunnelCode", StaticContent.Tturnnel_id);
//			param.put("CheckStaffCode", 7);
			param.put("RoadCode", StaticContent.Tline_id);
			String sname=DB_Provider.GetManagerName(StaticContent.UnitCode);
			param.put("UnitCode", sname);
			String s1=up.getCheckDate().replace("年", "-").replace("月", "-").replace("日", "").trim();
			param.put("CheckDate", s1);
			param.put("Weather", up.getWeather());
			int ucode=Integer.parseInt(StaticContent.UnitCode.trim());
			System.out.println("监听的ucode   ."+ucode+"..");
			param.put("CheckUnitCode",ucode);
			param.put("DailyCheckType", "TJ001");
			param.put("AttchmentFileGuid", null);
			param.put("IsSubmit", false);
			param.put("IsSubmitRating", false);
			Gson gson = new Gson();
			param.put("dailyCheckTunnelInfoDto",cl);
		    s = gson.toJson(param);
		    s=s.substring(18, s.length()-1);
			System.out.println(s);
//			sb = new JSONObject(s);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	return s;
	}
	
	
	public static String getdq(String up_down) throws Exception{ 
			    JSONObject param=new JSONObject();
				UpdateInfo up=GetBaseUpdateInfo();
				List<DailyCheckTunnelInfoDto> cl=new ArrayList<DailyCheckTunnelInfoDto>();
				cl.add(DailyCheckTunnelInfoDto.getUpDemoDQ(up_down));
			try {
				param.put("TunnelCode", StaticContent.Tturnnel_id);
//				param.put("CheckStaffCode", 7);
				param.put("RoadCode", StaticContent.Tline_id);
				int ucode=Integer.parseInt(StaticContent.UnitCode.trim());
				param.put("UnitCode", StaticContent.UnitCode);
				param.put("CheckDate", up.getCheckDate());
				param.put("Weather", up.getWeather());
				//param.put("CheckUnitCode", 25);
				param.put("CheckUnitCode", StaticContent.UnitCode);
				param.put("DailyCheckType", "TJ002");
				param.put("AttchmentFileGuid", null);
				param.put("IsSubmit", false);
				param.put("IsSubmitRating", false);
				Gson gson = new Gson();
				param.put("dailyCheckTunnelInfoDto",cl);
				 s = gson.toJson(param);
				 s=s.substring(18, s.length()-1);
				 System.out.println(s);
//				sb = new JSONObject(s);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		return s;
		}
	
	
	
	public static UpdateInfo GetBaseUpdateInfo(){
		UpdateInfo up=new UpdateInfo();
		String sql="select check_date,weather from TASK where update_id='"+StaticContent.update_id+"'";
		Cursor c=DB_Provider.dbHelper.query(sql);
		
		if (c.moveToFirst()) {
			do {
			up.setCheckDate(c.getString(0));
			up.setWeather(DataProvider.getBaseCSName("天气状况", c.getInt(1)));
			
			} while (c.moveToNext());
		}
		
		
		return up;
	}
	
}
