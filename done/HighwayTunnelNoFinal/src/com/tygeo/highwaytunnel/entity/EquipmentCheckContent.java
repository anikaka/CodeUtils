package com.tygeo.highwaytunnel.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.google.gson.Gson;
import com.tygeo.highwaytunnel.R.drawable;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.common.StaticContent;

public class EquipmentCheckContent {
  int EquipmentCode;//基础设备表
  
  int EquipmentStatusCode;//设备状态
  
  String EquipmentFaultDesc;//故障描述
	
  
 String EquipmentAttentionRemark;//设备注意事项
 
 int  EquipmentCheckRangeCode; //检查范围 
 
  String  TunnelCode; //
  int  Weather;
  
  
  String CheckDate;	
  
  int  CheckUnit;		//
  String CheckStaff;   //检查人

  int  CheckTypeCode;
   
  String CheckContentCodeList;
   
  
  	public static String getEleCheckJsonJC(int _id){
  		String s="";
  		JSONArray js=new JSONArray();
  		ArrayList<String> S=getBHCont();
  		for (int i = 0; i < S.size(); i++) {
  			JSONObject jo=new JSONObject();
  			Map<String, String>map=getEleCheck(S.get(i));
  		try {
			jo.put("EquipmentCode", map.get("EquipmentCode"));
			jo.put("EquipmentStatusCode", map.get("EquipmentStatusCode"));
			jo.put("EquipmentFaultDesc", map.get("EquipmentFaultDesc"));
			jo.put("EquipmentAttentionRemark", map.get("EquipmentAttentionRemark"));
			jo.put("EquipmentCheckRangeCode", map.get("EquipmentCheckRangeCode"));
  		} catch (JSONException e) {
			e.printStackTrace();
			
  		}
  		js.put(jo);
  		
//  		
  		
  		
//  		
  		}
  		return js.toString();
  	}
  	public String getEleJCDailycheckJC(int _id){
  		String s="";
  		JSONObject jo=new JSONObject();
  		Map<String, String> map=getDaliyCheck(_id);
  		try {
			jo.put("TunnelCode", StaticContent.Tturnnel_id);
			jo.put("Weather", map.get("weather"));
			String s1=map.get("check_date").replace("年", "-").replace("月", "-").replace("日", "");
			jo.put("CheckDate", s1);
//			jo.put("CheckUnit", StaticContent.localinfo.get("UnitCode"));
			jo.put("CheckUnit",  StaticContent.UnitCode);
			jo.put("CheckStaff",  DB_Provider.getcheckhead(map.get("check_head")));
			jo.put("CheckTypeCode", StaticContent.ele_check_type);
			jo.put("CheckContentCodeList", getBHContentlist());
			
  		} catch (JSONException e) {
			e.printStackTrace();
		}
  		
  		
  		return jo.toString();
  	}
  	
  	
  	private Map<String, String> getDaliyCheck(int _id){
  		Map<String, String> map=new HashMap<String, String>();
  		String sql="select weather,check_date,check_head  from TASK where update_id='"+StaticContent.update_id+"'";
  		Cursor c=DB_Provider.dbHelper.query(sql);
  		
  		if (c.moveToFirst()) {
  			map.put("weather", c.getString(0));
  			map.put("check_date", c.getString(1));
  			map.put("check_head", c.getString(2));
		}
  		return map;
  	}
  	
  	
  	private static Map<String, String> getEleCheck(String _id){
  		Map<String, String> map=new HashMap<String, String>();
  		String sql="select EleCode,EquipmentKindCode,handle,fault,EquipmentCheckItemCode,EquipmentCheckRangeCode, device_state from ELECTRICAL_FAC where _id='"+_id+"'";
  		Cursor c=DB_Provider.dbHelper.query(sql);
  			
  		if (c.moveToFirst()) {
			
  			map.put("EquipmentCode", c.getString(0));
  			map.put("EquipmentStatusCode", c.getString(6));
  			map.put("EquipmentFaultDesc", c.getString(2));
  			map.put("EquipmentAttentionRemark", c.getString(3));
  			map.put("EquipmentCheckRangeCode", c.getString(5));
  			
		}
  		
  		
  		
  		
  		return map;
  	}
  	
  	private static  ArrayList<String> getBHCont(){
  		ArrayList<String> s=new ArrayList<String>();
  		String sql="select _id from ELECTRICAL_FAC where task_id='"+StaticContent.update_id+"'";
  		Cursor c=DB_Provider.dbHelper.query(sql);
  		if (c.moveToFirst()) {
  			
  			do {
				
  				s.add(c.getString(0));
			} while (c.moveToNext());
		
  		}
  		
  		
  		return s;
  	}
	private static  String getBHContentlist(){
  		String s="";
		String sql="select _id from ELECTRICAL_FAC where task_id='"+StaticContent.update_id+"'";
  		Cursor c=DB_Provider.dbHelper.query(sql);
  		if (c.moveToFirst()) {
			
  			do {
  				s+=c.getString(0)+",";
  				
			} while (c.moveToNext());
  			
  		} 
  		
  		
  		return s.substring(0, s.length()-1);
  	}
	
  		public static boolean ischeckone(){
  			boolean flag=false;
  			int i=0;
  			String sql="select  count(*) from ELECTRICAL_FAC where task_id='"+StaticContent.update_id+"'";
  			Cursor c=DB_Provider.dbHelper.query(sql);
  			if (c.moveToFirst()) {
  				
  	  			i=c.getInt(0);
  	  			
  	  		}
  			if (!(i==0)) {
				flag=true;
			}
  			
  			return flag;
  		}
  		
  		
  		
  		
}
