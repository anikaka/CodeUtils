package com.tygeo.highwaytunnel.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.util.Log;

import com.TY.bhgis.Database.DataProvider;
import com.google.gson.Gson;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
public class DiseaseInfoDto {
	int CheckContentId;
	String CheckItem;
	String CheckItemAddtionDto;
	int CheckItemLocationCode;
	String HandPoints;
	String DiseaseDesc;
	String mileage;
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	String DiseaseName;
	String DiseasePIcAlias;
	String DiseasePicNmae;
	int DiseaseState;

	public int getDiseaseState() {
		return DiseaseState;
	}

	public void setDiseaseState(int diseaseState) {
		DiseaseState = diseaseState;
	}

	int DiseaseType;
	String PicName;
	String TunnelName;
	String TunnelStake;
	static JSONObject sb;
	String BH_ID;
	int _id;
	String jugel_level;
	
	public int getCheckDictionaryCode() {
		return CheckDictionaryCode;
	}

	public void setCheckDictionaryCode(int checkDictionaryCode) {
		CheckDictionaryCode = checkDictionaryCode;
	}

	public String getItemValue() {
		return ItemValue;
	}

	public void setItemValue(String itemValue) {
		ItemValue = itemValue;
	}

	public int getCheckItemCode() {
		return CheckItemCode;
	}

	public void setCheckItemCode(int checkItemCode) {
		CheckItemCode = checkItemCode;
	}

	int CheckDictionaryCode;
	String ItemValue ;
	int CheckItemCode;
	public String getJugel_level() {
		return jugel_level;
	}

	public void setJugel_level(String jugel_level) {
		this.jugel_level = jugel_level;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getBH_ID() {
		return BH_ID;
	}

	public void setBH_ID(String bH_ID) {
		BH_ID = bH_ID;
	}

	public int getCheckContentId() {
		return CheckContentId;
	}

	public void setCheckContentId(int checkContentId) {
		CheckContentId = checkContentId;
	}

	public String getCheckItem() {
		return CheckItem;
	}

	public void setCheckItem(String checkItem) {
		CheckItem = checkItem;
	}

	public String getCheckItemAddtionDto() {
		return CheckItemAddtionDto;
	}

	public void setCheckItemAddtionDto(String checkItemAddtionDto) {
		CheckItemAddtionDto = checkItemAddtionDto;
	}

	public int getCheckItemLocationCode() {
		return CheckItemLocationCode;
	}

	public void setCheckItemLocationCodee(int checkItemLocationCode) {
		CheckItemLocationCode = checkItemLocationCode;
	}

	public String getHandPoints() {
		return HandPoints;
	}

	public void setHandPoints(String handPoints) {
		HandPoints = handPoints;
	}

	public String getDiseaseDesc() {
		return DiseaseDesc;
	}

	public void setDiseaseDesc(String diseaseDesc) {
		DiseaseDesc = diseaseDesc;
	}

	public String getDiseaseName() {
		return DiseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		DiseaseName = diseaseName;
	}

	public String getDiseasePIcAlias() {
		return DiseasePIcAlias;
	}

	public void setDiseasePIcAlias(String diseasePIcAlias) {
		DiseasePIcAlias = diseasePIcAlias;
	}

	public String getDiseasePicNmae() {
		return DiseasePicNmae;
	}

	public void setDiseasePicNmae(String diseasePicNmae) {
		DiseasePicNmae = diseasePicNmae;
	}

	public int getDiseaseType() {
		return DiseaseType;
	}

	public void setDiseaseType(int diseaseType) {
		DiseaseType = diseaseType;
	}

	public String getPicName() {
		return PicName;
	}

	public void setPicName(String picName) {
		PicName = picName;
	}

	public String getTunnelName() {
		return TunnelName;
	}

	public void setTunnelName(String tunnelName) {
		TunnelName = tunnelName;
	}

	public String getTunnelStake() {
		return TunnelStake;
	}

	public void setTunnelStake(String tunnelStake) {
		TunnelStake = tunnelStake;
	}

	public static JSONArray GetdemoInfo() {
		JSONArray joy = new JSONArray();
		JSONObject param = new JSONObject();
		JSONObject comp = new JSONObject();
		try {
			param.put("CheckContentId", 1);
			param.put("CheckItem", "1");
			param.put("CheckItemAddtionDto", null);
			param.put("CheckItemLocationCode", 1);
			param.put("HandPoints", "ZK20+100@7.76_0.97;ZK20+100@7.78_0.97");
			param.put("DiseaseDesc", "病害描述");
			param.put("DiseaseName", "结构裂缝");
			param.put("DiseasePIcAlias", null);
			param.put("DiseasePicNmae", "p0");
			param.put("DiseaseType", 0);
			param.put("DiseaseState", 1);
			param.put("PicName", null);
			param.put("TunnelName", null);
			param.put("TunnelStake", "上行");
			comp.put("CheckContentId", 11);
			comp.put("CheckItem", "2");
			comp.put("CheckItemAddtionDto", null);
			comp.put("CheckItemLocationCode", 0);
			comp.put("HandPoints", "");
			comp.put("DiseaseDesc", "病害描述-");
			comp.put("DiseaseName", "落物");
			comp.put("DiseasePIcAlias", null);
			comp.put("DiseasePicNmae", "p1");
			comp.put("DiseaseType", 0);
			comp.put("PicName", null);
			comp.put("DiseaseState", 32);
			comp.put("TunnelName", null);
			comp.put("TunnelStake", "下行");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		try {
			joy.put(0, param);
			joy.put(1, comp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return joy;
	}
	//病害相关json
	
	public static JSONArray GetDiseaseInfo(String update_id) {
		JSONArray jarr = new JSONArray();
		
		ArrayList<CheckDetailInfo> list = new ArrayList<CheckDetailInfo>();
		String sql = " select  CHECKID,POSITIONID,_id,involve from CILIV_CHECKCONTENT where task_id='"
				+ update_id + "' ";
		Cursor c = DB_Provider.dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				CheckDetailInfo ck = new CheckDetailInfo();
				ck.setValue(c.getInt(0));
				if (!(c.getInt(3)==1)) {
					if (c.getString(1) == null) {
						ck.setGetcheckitem(0);
						System.out.println("空");
					} else {
						ck.setGetcheckitem(c.getInt(1));
						System.out.println("设置空间位置为:" + c.getInt(1));
					}
					ck.set_id(c.getInt(2));
					System.out.println(c.getString(0));
					list.add(ck);
				}
				
			} while (c.moveToNext());
		}

		for (int i = 0; i < list.size(); i++) {
			try {
				DiseaseInfoDto di = DB_Provider.GetCheckId(list.get(i).getValue());
				DiseaseInfoDto dio = GetBHUpdateInfo(list.get(i).get_id());
				ArrayList<String> pic_name = getpicName(dio.get_id());
				if (pic_name.size() == 0) {
					JSONObject param = new JSONObject();
					param.put("CheckContentId", list.get(i).getValue());
					param.put("CheckItem", di.getCheckItem());
					param.put("CheckItemAddtionDto", null);
					param.put("CheckItemLocationCode", list.get(i).getGetcheckitem());
					param.put("HandPoints",getHandPoint(dio.getBH_ID(), dio.getTunnelStake()));
					param.put("DiseaseDesc", dio.getDiseaseDesc());
					param.put("DiseaseName", di.getDiseaseName());
					param.put("DiseasePIcAlias", null);
					param.put("DiseasePicName", "");
					param.put("DiseaseType", 0);
					param.put("DiseaseStake", dio.getMileage());
					param.put("DiseaseState", getDiseaseState(list.get(i).getValue(), dio.getJugel_level()));
					param.put("PicName", null);
					param.put("TunnelName", null);
					param.put("TunnelStake", dio.getTunnelStake());
					jarr.put(param);
				} else {
					for (int j = 0; j < pic_name.size(); j++) {
						JSONObject param = new JSONObject();
						param.put("CheckContentId", list.get(i).getValue());
						param.put("CheckItem", di.getCheckItem());
						param.put("CheckItemAddtionDto", null);
						param.put("CheckItemLocationCode", list.get(i).getGetcheckitem());
						param.put("HandPoints",getHandPoint(dio.getBH_ID(),dio.getTunnelStake()));
						param.put("DiseaseDesc", dio.getDiseaseDesc());
						param.put("DiseaseName", di.getDiseaseName());
						param.put("DiseasePIcAlias", null);
						param.put("DiseasePicName", pic_name.get(j)+".jpg");
						param.put("DiseaseType", 0);
						param.put("DiseaseStake", dio.getMileage());
						param.put("DiseaseState", getDiseaseState(list.get(i).getValue(), dio.getJugel_level()));
						param.put("PicName", null);
						param.put("TunnelName", null);
						param.put("TunnelStake", dio.getTunnelStake());
						jarr.put(param );
					}

				}

			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		return jarr;
	}
	
	public static String GetDiseaseInfoDQ(String update_id) {
		JSONArray jarr = new JSONArray();
		String s="";
		ArrayList<CheckDetailInfo> list = new ArrayList<CheckDetailInfo>();
		String sql = " select  CHECKID,POSITIONID,_id,Involve from CILIV_CHECKCONTENT where task_id='"+ update_id + "'";
		Cursor c = DB_Provider.dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				if (!(c.getInt(3)==1)) {
					CheckDetailInfo ck = new CheckDetailInfo();
					ck.setValue(c.getInt(0));
					ck.set_id(c.getInt(2));  //ChenLang 病害ID
					if (c.getString(1) == null) {
						ck.setGetcheckitem(0);
						System.out.println("空");
					} else {
						ck.setGetcheckitem(c.getInt(1));
						System.out.println("设置空间位置为:" + c.getInt(1));
					}
					ck.set_id(c.getInt(2));
					System.out.println(c.getString(0));
					list.add(ck);
				}
			} while (c.moveToNext());
		}

		for (int i = 0; i < list.size(); i++) {
			try {
				JSONArray jsonArray=new JSONArray(); //病害描述JSONArray  比如衬砌描述 裂缝走向,深度,高度,宽度
				DiseaseInfoDto di = DB_Provider.GetCheckId(list.get(i).getValue());
				DiseaseInfoDto dio = GetBHUpdateInfo(list.get(i).get_id());
				ArrayList<String> pic_name = getpicName(dio.get_id());
				if (pic_name.size() == 0) {
					JSONObject param = new JSONObject();
					param.put("CheckContentId", list.get(i).getValue());
					param.put("CheckItem", di.getCheckItem());
//					param.put("CheckItemAddtionDto", null);
					param.put("CheckItemLocationCode", list.get(i).getGetcheckitem());
					param.put("HandPoints",getHandPoint(dio.getBH_ID(), dio.getTunnelStake()));
					param.put("DiseaseDesc", "");
					param.put("DiseaseName", di.getDiseaseName());
					param.put("DiseasePIcAlias", null);
					param.put("DiseasePicName", "");
					param.put("DiseaseType", 0);
					param.put("DiseaseStake", dio.getMileage()); //ChenLang modify
					param.put("DiseaseState", getDiseaseState(list.get(i).getValue(), dio.getJugel_level()));
					param.put("PicName", null);
					param.put("TunnelName", null);
					param.put("TunnelStake", dio.getTunnelStake());
//					param.put("CheckItemAddtionDto", GetDiseaseAddInfo(list.get(i).getValue()));
					//ChenLang modify   CheckItemAddtionDto
					ArrayList<DiseaseInfoDto> arrayList=GetDiseaseAddInfo(list.get(i).getValue(),list.get(i).get_id());
//					StringBuffer  strBuffer=new StringBuffer();
//					strBuffer.append("[");
					if(arrayList.size()>0){
						for(int k=0;k<arrayList.size();k++){
							JSONObject jsonObj=new JSONObject();
							DiseaseInfoDto info=arrayList.get(k);
//							strBuffer.append("{");
//							strBuffer.append("CheckItemCode");
//							strBuffer.append(":");
//							strBuffer.append(""+info.getCheckItemCode()+"");
//							strBuffer.append(",");
//							//
//							strBuffer.append("\"ItemValue\"");
//							strBuffer.append(":");
//							strBuffer.append("\""+info.getItemValue()+"\"");
//							strBuffer.append(",");
//							//
//							strBuffer.append("\"CheckDictionnaryCode\"");
//							strBuffer.append(":");
//							strBuffer.append("\""+info.getCheckDictionaryCode()+"\"");
//							strBuffer.append("}");
//							if(k!=arrayList.size()-1){
//								strBuffer.append(",");
//							}
							jsonObj.put("CheckItemCode", info.getCheckItemCode());
							jsonObj.put("ItemValue", info.getItemValue());
							jsonObj.put("CheckDictionnaryCode", info.getCheckDictionaryCode());
							jsonArray.put(jsonObj);
						}
					}
//					strBuffer.append("]");
					//字段主要是拼接的裂缝方向及其对应的值，和其他属性的值，比如说宽+值；高+值；深度+值等
//					param.put("CheckItemAddtionDto",strBuffer.toString().trim());
					param.put("CheckItemAddtionDtoDto",jsonArray);
					jarr.put(param);
				} else {
					for (int j = 0; j < pic_name.size(); j++) {
						JSONObject param = new JSONObject();
						param.put("CheckContentId", list.get(i).getValue());
						param.put("CheckItem", di.getCheckItem());
						param.put("CheckItemAddtionDto", null);
						param.put("CheckItemLocationCode", list.get(i).getGetcheckitem());
						param.put("HandPoints",getHandPoint(dio.getBH_ID(),dio.getTunnelStake()));
						param.put("DiseaseDesc","");
						param.put("DiseaseName", di.getDiseaseName());
						param.put("DiseasePIcAlias", null);
						param.put("DiseasePicName", pic_name.get(j)+".jpg");
						param.put("DiseaseType", 0);
						param.put("DiseaseStake", dio.getMileage());//ChenLang modify
						param.put("DiseaseState", getDiseaseState(list.get(i).getValue(), dio.getJugel_level()));
						param.put("PicName", null);
						param.put("TunnelName", null);
						param.put("TunnelStake", dio.getTunnelStake());
//						param.put("CheckItemAddtionDto", GetDiseaseAddInfo(list.get(i).getValue()));
						//ChenLang  modify
						ArrayList<DiseaseInfoDto> arrayList=GetDiseaseAddInfo(list.get(i).getValue(),list.get(i).get_id());
						if(arrayList.size()>0){
							for(int k=0;k<arrayList.size();k++){
								JSONObject jsonObj=new JSONObject();
								DiseaseInfoDto info=arrayList.get(k);
								jsonObj.put("CheckItemCode", info.getCheckItemCode());
								jsonObj.put("ItemValue", info.getItemValue());
								jsonObj.put("CheckDictionnaryCode", info.getCheckDictionaryCode());
								jsonArray.put(jsonObj);
							}
						}
						param.put("CheckItemAddtionDtoDto",jsonArray);
						jarr.put(param );
					}Gson gson=new Gson();
					 s=gson.toJson(jarr);
					 s=s.substring(18, s.length()-1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jarr.toString();
	}
	
	
	

	public static DiseaseInfoDto GetBHUpdateInfo(int _id) {
		DiseaseInfoDto di = new DiseaseInfoDto();
		String sql = " select BZ,UP_DOWN,BHID,_id,judge_level,mileage from CILIV_CHECKCONTENT where _id='"
				+ _id + "'";
		Cursor c = DB_Provider.dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				di.setDiseaseDesc(c.getString(0));
				di.setTunnelStake(DataProvider.getBaseCSName("行向", c.getInt(1)));
				di.setBH_ID(c.getString(2));
				di.set_id(c.getInt(3));
				//里程桩号应该转换成k0+000这种格式表示
				int  t=Integer.parseInt(c.getString(5))/1000; //千位
				int  h=Integer.parseInt(c.getString(5))%1000;//百位
				di.setJugel_level(c.getString(4));
//				di.setMileage(c.getString(5));
				di.setMileage("k"+t+"+"+h);
			} while (c.moveToNext());
		}

		return di;
	}

	public static String getHandPoint(String Bhid, String up_down) {
		String k = "";
		if (up_down.equals("上行")) {
			k = "Zk";
		} else {
			k = "YK";
		}
		String s = "";
		String lx = "";
		String ly = "";
		int l;
		String sl = "";
		String sql = "select LCZH,OX,OY from BHZB where BHID='" + Bhid + "' order by ID";
		Cursor c = DB_Provider.dbHelper.query(sql);

		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					l = c.getInt(0);
					int num_ = l / 1000;
					int _num = l % 1000;
					sl = k + num_ + "+" + _num;
					lx = c.getString(1);
					ly = c.getString(2);
					s += sl + "@" + lx + "_" + ly + ";";
				} while (c.moveToNext());
			}
		}

		return s;

	}

	public static ArrayList<String> getpicName(int bhid) {
		ArrayList<String> arr = new ArrayList<String>();
		String sql = "select guid from BH_PIC where bh_id='" + bhid + "'";
		Cursor c = DB_Provider.dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					String s = c.getString(0);//+".jpg";
					arr.add(s);
				} while (c.moveToNext());
			}
		}
		
		return arr;
	}
	public static  int getDiseaseState(int checkid,String level){	
		int i=0;
		int z=0;
		if (level.equals("S")) {
			z=1;
		}else if(level.equals("B")){
			z=2;
		}else {
			z=3;
		}
		String sql="select _id from CheckItemDescInfos where YhGradeCode='"+z+"'and CheckId='"+checkid+"'";
		Cursor c=DB_Provider.dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				i=c.getInt(0);
			} while (c.moveToNext());
		}
		return i;
	}
	
	//第二个病害ID ChenLang 添加的 
	public static ArrayList<DiseaseInfoDto> GetDiseaseAddInfo(int CheckId,int diseaseId){
		ArrayList<DiseaseInfoDto> ad=new ArrayList<DiseaseInfoDto>();
		String  sql="select CHECKID,PARAMCODE,PARAMVALUE from CIVIL_REG_CHECK_INFO where CHECKID ='"+CheckId+"' AND BHID='"+diseaseId+"'";
		Cursor c=DB_Provider.dbHelper.query(sql);
		try {
		if (c.moveToFirst()) {
			do {
				DiseaseInfoDto di=new DiseaseInfoDto();
				di.setCheckItemCode(0);
				di.setCheckDictionaryCode(c.getInt(1));
				di.setItemValue(c.getString(2));
				ad.add(di);
			} while (c.moveToNext());
		}} catch (Exception e) {
			e.printStackTrace();
		}
		return ad;
	}
	
	
}
