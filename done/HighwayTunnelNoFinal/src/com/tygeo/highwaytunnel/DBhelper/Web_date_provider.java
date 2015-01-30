package com.tygeo.highwaytunnel.DBhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.common.WebServiceUtil;
import com.tygeo.highwaytunnel.entity.BaseEquiment;
import com.tygeo.highwaytunnel.entity.CheckDetailInfo;
import com.tygeo.highwaytunnel.entity.CheckPicInfo;
import com.tygeo.highwaytunnel.entity.CheckinfoE;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import com.tygeo.highwaytunnel.entity.UpdateInfo;
import com.tygeo.highwaytunnel.entity.line_json;

public class Web_date_provider {
	public static DBHelper dbHelper = new DBHelper(StaticContent.DataBasePath);
	ProgressDialog pd;
	List<CheckinfoE> ce,me;
	List<line_json> lj;
	List<TunnelInfoE> te;
	List<BaseEquiment> be;
	public static void InsertTurInfo(line_json list) {
		String section_id = list.getSection_id();
		String section_name = list.getSection_name();
		String father_id = list.getFather_id();
		String sql = "select count()from pro_info where section_id='"
				+ section_id + "'";
		System.out.println(sql);
		Cursor c = dbHelper.query(sql);
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
			System.out.println(i);
			
		}
		if (c!=null) {
			c.close();
		}
		if (i == 0) {
			ContentValues cv = new ContentValues();
			cv.put("section_id", section_id);
			cv.put("section_name", section_name);
			cv.put("father_id", father_id);
			dbHelper.insert("pro_info", cv);
			
		} else {

			return;
		}

	}

	public static void InsertTunnelInfo(TunnelInfoE list) {
		String section_id = list.getSection_id();
		String section_name = list.getSection_name();
		String line_id = list.getLine_id();
		String up_num = list.getUp_num();
		double up_length = list.getUp_length();
		String down_num = list.getDown_num();
		double down_length = list.getDown_length();
		String completion_time = list.getCompletion_time();

		String sql = "select count()from TURNNEL where section_id='"
				+ section_id + "'";
		Cursor c = dbHelper.query(sql);
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
		}
		if (c!=null) {
			c.close();
		}
		if (i == 0) {

			/*
			 * line_id VARCHAR( 20 ), section_id VARCHAR( 20 ), section_name
			 * VARCHAR( 20 ), up_length INTEGER, down_length INTEGER, up_num
			 * VARCHAR( 20 ), down_num VARCHAR( 20 ), completion_time VARCHAR(
			 * 20 )
			 */
			ContentValues cv = new ContentValues();
			cv.put("section_id", section_id);
			cv.put("section_name", section_name);
			cv.put("line_id", line_id);
			cv.put("up_num", up_num);
			cv.put("up_length", up_length);
			cv.put("down_num", down_num);
			cv.put("down_length", down_length);
			cv.put("completion_time", completion_time);
			dbHelper.insert("TURNNEL", cv);

		} else {

			return;
		}

	}

	public static void InsertUserInfo(CheckinfoE list) {
		String name = list.getName();
		String check_id = list.getCheck_id();
		String type = list.getType();
		String unitcode=list.getLeader_UnitCode();
		String sql = "select count()from CHECKINFO where check_id='" + check_id
				+ "'";
		try {
		Cursor c = dbHelper.query(sql);
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
		}
		if (c!=null) {
			c.close();
		}
		if (i == 0) {
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("check_id", check_id);
			cv.put("BelongUnit", unitcode);
			cv.put("type", type);
			dbHelper.insert("CHECKINFO", cv);
		} else {
			return;
		}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}finally{
			if (dbHelper!=null) {
				dbHelper.close();
			}
		}
		
	}
	public static void InsertManagerInfo(CheckinfoE list) {
		String name = list.getManangerUnitName();
		String rodelist =list.getRoadCodeList();
		int code = list.getUnitCode();
		String sql = "select count()from  ManagerUnit where ManagerName='" + name
				+ "'";
		Cursor c = dbHelper.query(sql); 
		try {
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
			System.out.println(i);
			
		}
		if (i == 0) {
			ContentValues cv = new ContentValues();
			cv.put("ManagerName", name);
			cv.put("RoadCodeList", rodelist);
			System.out.println("roadcode"+rodelist);
			
			cv.put("UnitCode", code);
			if (!rodelist.equals("")) {
				DB_Provider.savalineinfo(rodelist,code+"");
					
			}
			dbHelper.insert("ManagerUnit", cv);
			
		} else {

			return;
		}
		} catch (Exception e) {
		}finally{
			if (c!=null) {
				c.close();
			}
			if (dbHelper!=null) {
				dbHelper.close();
			}
		}
	}
	
//	//获得管理站管理的路线
//	public static ArrayList<String> GetManageLine(int unitcode){
//		ArrayList<String> s=new ArrayList<String>();
//		String sql="select RoadCodeList from ManagerUnit where";
//		
//		
//		return s;
//	}
	
	
	public static UpdateInfo GetCheckInfo(String updateid, Task ta) {
		UpdateInfo upif = new UpdateInfo();

		upif.setTunnelCode(StaticContent.Tturnnel_name);
		upif.setUnitCode(ta.getMainte_org());
		upif.setCheckDate(ta.getCheck_date());
		upif.setWeather(ta.getWeather());
		upif.setCheckUnitCode(25);
		upif.setDailyCheckType("TJ001");
		upif.setTunnelStake(ta.getBegin_num() + "_" + ta.getEnd_num());
		if (StaticContent.S_X.equals("S")) {
			upif.setBaseCheckTunnelType("上行");
		} else {
			upif.setBaseCheckTunnelType("下行");
		}

 		upif.setCheckDetailList(GetAllCheck(updateid));

		return upif;
	}

	public static List<CheckDetailInfo> GetDetailnfo(String updateid) {
		List<CheckDetailInfo> deIf = new ArrayList<CheckDetailInfo>();
		List<JSONObject> gl = new ArrayList<JSONObject>();
		String sql = "select _id,bhid,checkid,judge_level,positionid from CILIV_CHECKCONTENT where task_id='"
				+ updateid + "'";
		Cursor c = dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					try {
						CheckDetailInfo ck = new CheckDetailInfo();
						JSONObject job = new JSONObject();
						ck.setCheckContent(c.getInt(2) + c.getString(3));
						// job.put("CheckContent", c.getString(1));
						// ck.setStateDescription(c.getString(5));
						// job.put("StateDescription", c.getString(5));
						// ck.setCheckItemState(c.getString(4));
						// job.put("CheckItemState", c.getString(4));
						ck.setCheckItemMeasure("暂无");
						// job.put("CheckItemMeasure", "暂无");
//						ck.setCheckItemLocationCode(c.getString(4));
						// job.put("CheckItemLocationCode", c.getString(2));
						String bhid = c.getString(0);
						String ZBid = c.getString(1);
						ck.setCheckPointArray(checkPointArray(ZBid));
						// job.put("CheckPointArray", checkPointArray(ZBid));
						ck.setCheckPicInfoList(GetCheckPic(bhid));
						// job.put("CheckPicInfoList", GetCheckPic(bhid));
						deIf.add(ck);
						gl.add(job);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
		}
		
		// Gson gson=new Gson();
		// String listgson=gson.toJson(deIf);
		// String listgs=listgson.replace("\\/", "");
		// System.out.println("list to json"+listgson);

		return deIf;
	}

	private static String checkPointArray(String bhid) {
		String s = "";
		String sql = "select*from BHZB where BHID='" + bhid + "'";
		Cursor c = dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					String ox = c.getString(4);
					String oy = c.getString(5);
					int lz = c.getInt(6);
					int num_ = lz / 1000;
					int _num = lz % 1000;

					s += StaticContent.Up_Down + num_ + "+" + _num + "@" + ox
							+ "_" + oy + ":";

					System.out.println(s);
				} while (c.moveToNext());
			}
		}

		return s;
	}
	// 获得病害的照片
	private static List<CheckPicInfo> GetCheckPic(String bhid) {
		List<CheckPicInfo> list = new ArrayList<CheckPicInfo>();
		List<JSONObject> gl = new ArrayList<JSONObject>();
		String sql = "select pic_id from BH_PIC where bh_id='" + bhid + "'";
		Cursor c = dbHelper.query(sql);
		JsonArray jar = new JsonArray();
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					CheckPicInfo cin = new CheckPicInfo();
					cin.setPicName(c.getString(0));
					list.add(cin);
				} while (c.moveToNext());
			}

		}

		return list;
	}

	public static List<CheckDetailInfo> GetAllCheck(String updateid) {
		List<CheckDetailInfo> deIf = new ArrayList<CheckDetailInfo>();
//		List<JSONObject> gl = new ArrayList<JSONObject>();
		String sql = "select _id,checkid from CIVIL_CHECK_INFO";
		Cursor c = dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					try 	{	
						CheckDetailInfo ck = new CheckDetailInfo();
						JSONObject job = new JSONObject();
						ck.setCheckContent(c.getInt(1)+"");
						boolean flag=BHONLY(c.getInt(1));
						if (flag==true) {
							CheckDetailInfo hava=GetBHOnlyInfo(c.getInt(1),updateid);
							ck.setCheckContent(hava.getCheckContent());
							ck.setCheckItemMeasure("暂无");
							ck.setCheckItemLocationCode(hava.getCheckItemLocationCode());
							String bhid =hava.getBhid();
//							int _id=hava.getCheckinfoid();
//							ck.setCheckPicInfoList(GetCheckPic(_id+""));
							ck.setCheckPointArray(checkPointArray(bhid));
//							ck.setCheckPicInfoList(GetCheckPic(_id+""));
						}
						
						deIf.add(ck);
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
		}
		// Gson gson=new Gson();
		// String listgson=gson.toJson(deIf);
		// String listgs=listgson.replace("\\/", "");
		// System.out.println("list to json"+listgson);
		System.err.println("条目数L:"+c.getCount());
		return deIf;
	}

	public static boolean BHONLY(int checkid) {
		boolean f = false; 
		String sql = "select  count() from CILIV_CHECKCONTENT where CHECKID ='"
				+ checkid + "'";
		Cursor c = dbHelper.query(sql);
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
			System.out.println(i);

		}
		if (i == 1) {
			f = true;
		} else {
			f = false;
		}
		
		return f;
		
	}

	public static CheckDetailInfo GetBHOnlyInfo(int checkid,String updateid) {
		CheckDetailInfo cif = new CheckDetailInfo();
		cif.setCheckContent("");
		cif.setCheckItemLocationCode(1);
		cif.setBhid("");
//		cif.setCheckinfoid(-1);
		String judge_level = "S";
		int bhget = -1;
		String sql = "select _id,checkid,judge_level,positionid,BHID from CILIV_CHECKCONTENT where CHECKID ='"
				+ checkid + "'and judge_level='" + "B" + "' and task_id='"+updateid+"' limit 1,0";
		String sql2 = "select _id,checkid,judge_level,positionid,BHID from CILIV_CHECKCONTENT where CHECKID ='"
				+ checkid + "'and judge_level='" + "A" + "' and task_id='"+updateid+"' limit 1,0";
		String sql3 = "select _id,checkid,judge_level,positionid,BHID from CILIV_CHECKCONTENT where CHECKID ='"
				+ checkid + "'and judge_level='" + "S" + "' and task_id='"+updateid+"' limit 1,0";

		Cursor c = dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				bhget = 1;
				cif.setCheckContent(c.getInt(1) + c.getString(2));
//				cif.setCheckItemLocationCode(c.getString(3));
				cif.setBhid(c.getString(4));
//				cif.setCheckinfoid(c.getInt(0));
				
				
			}
		} else if (bhget == -1) {

			Cursor c2 = dbHelper.query(sql2);
			if (!(c2.getCount() == 0)) {
				if (c.moveToFirst()) {
					bhget = 2;
					cif.setCheckContent(c.getInt(1) + c.getString(2));
//					cif.setCheckItemLocationCode(c.getString(3));
					cif.setBhid(c.getString(4));
//					cif.setCheckinfoid(c.getInt(0));
				}
			}
		} else {
			Cursor c3 = dbHelper.query(sql3);
				
			if (!(c3.getCount() == 0)) {
				if (c.moveToFirst()) {
					bhget = 1;
					cif.setCheckContent(c.getInt(1) + c.getString(2));
//					cif.setCheckItemLocationCode(c.getString(3));
					cif.setBhid(c.getString(4));
//					cif.setCheckinfoid(c.getInt(0));
					
				}
			}
		}
		return cif;
	}
	
	/**
	 *插入基础设施数据
	 */
	public static void InsertBaseEquipment(List<BaseEquiment> list) throws Exception {
		
		for (int i = 0; i < list.size(); i++) {
			int id=list.get(i).getId();
			String Code=list.get(i).getCode();
			String Name=list.get(i).getName();
			String Site=list.get(i).getSite();
			String TunnelCode=list.get(i).getTunnelCode();
			String  EqType=list.get(i).getEqType();
			String  Effect=list.get(i).getEffect();
			String  ParamId=list.get(i).getParamId();
			String sql = "select count(*) from BaseEquipments where id='"
				+ id + "'";
			Cursor c = dbHelper.query(sql);
			int j = -1;
			if (c.moveToFirst()) {
				j = c.getInt(0);
			}
			if (c!=null) {
				c.close();
			}
			if (j == 0) {
				ContentValues cv = new ContentValues();
				cv.put("id", id);
				cv.put("Name", Name);
				cv.put("Code", Code);
				cv.put("EqType", EqType);
				cv.put("Effect", Effect);
				cv.put("ParamId", ParamId);
				cv.put("Site", Site);
				cv.put("TunnelCode", TunnelCode);
				dbHelper.insert("BaseEquipments", cv);
			} else {

				return;
			}
			
			
			
		}
		
		
		
		

	}
	public static boolean ischeckoneTJU(){
			boolean flag=false;
			int i=0;
			String sql="select  count(*) from CILIV_CHECKCONTENT where task_id='"+StaticContent.update_id+"' and UP_DOWN='0'";
			Cursor c=DB_Provider.dbHelper.query(sql);
			if (c.moveToFirst()) {
				
	  			i=c.getInt(0);
	  			
	  		}
			if (!(i==0)) {
			flag=true;
		}
			
			return flag;
		}
	public static boolean ischeckoneTJD(){
		boolean flag=false;
		int i=0;
		String sql="select  count(*) from CILIV_CHECKCONTENT where task_id='"+StaticContent.update_id+"' and UP_DOWN='1'";
		Cursor c=DB_Provider.dbHelper.query(sql);
		if (c.moveToFirst()) {
  			i=c.getInt(0);
  			
  		}
		if (!(i==0)) {
		flag=true;
	}
		
		return flag;
	}
	
	
	//数据下载
	public void GetDownData(){
		try {
			Message msg = new Message();
			msg.what = 1;
			lj = new ArrayList<line_json>();//线路信息
			te = new ArrayList<TunnelInfoE>();//隧道信息
			ce = new ArrayList<CheckinfoE>();//人员信息
			be=new ArrayList<BaseEquiment>();//设备信息
//			me=new ArrayList<CheckinfoE>();
			lj = WebServiceUtil.GetBaseRoadInfo();
			ce = WebServiceUtil.GetBaseUserInfo();
			be=WebServiceUtil.GetBaseEquipmentInfo();
			te = WebServiceUtil.GetBaseTunnelInfo();
			Web_date_provider.InsertBaseEquipment(be);
			
			for (int t = 0; t < te.size(); t++) {
				Web_date_provider.InsertTunnelInfo(te.get(t));
				line_json lje = new line_json();
				String section_name = te.get(t).getSection_name();
				String section_id = te.get(t).getSection_id();
				String father_id = te.get(t).getLine_id();
			}
			for (int c = 0; c < ce.size(); c++) {
				Web_date_provider.InsertUserInfo(ce.get(c));
			}
			
			for (int i = 0; i < lj.size(); i++) {
				Web_date_provider.InsertTurInfo(lj.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	public boolean JustTunnle(String section_id){
		boolean f=false;
	String sql="select count() from TURNNEL where line_id='"+section_id+"'";
	Cursor c = dbHelper.query(sql);
	int i = -1;
	if (c.moveToFirst()) {
		i = c.getInt(0);
		System.out.println(i);
		
	}
	if (i == 0) {
		f = false;
	} else {
		f = true;
	}
	c.close();

	return f;
		
	}
	
}
