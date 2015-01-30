package com.tygeo.highwaytunnel.DBhelper;

import java.io.File;
import java.sql.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.StaticLayout;
import android.webkit.WebChromeClient.CustomViewCallback;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.activity.Chek_form;
import com.tygeo.highwaytunnel.adpter.CheckFormAdapter;
import com.tygeo.highwaytunnel.adpter.CivilPorContentAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.DiseaseInfoDto;
import com.tygeo.highwaytunnel.entity.EleContentE;

public class DB_Provider {
	// ��ѯ������¼������
	static String sql;
	public static int task_id = StaticContent.task_id;
	public static DBHelper dbHelper = new DBHelper(StaticContent.DataBasePath);
	static String ci[] = { "bh_id", "pic_name", "pic_id", "pic_pistion", "type" };

	public static ArrayList<CivilContentE> Sorting(String fors) {

		
		  int	i=StaticContent.checkfors;
			System.out.println(i);
			StaticContent.checkfors=i+1;
			
			if (i % 2 == 0) {
				 sql = "select*from CILIV_CHECKCONTENT where  task_id='"
					+ StaticContent.update_id + "'   order by " + fors + " asc";
				
				
				
			} else {
				 sql = "select*from CILIV_CHECKCONTENT where task_id='"
					+ StaticContent.update_id + "'   order by " + fors + " desc";
			}
		System.out.println(sql);
		DBHelper dbhelp = new DBHelper(StaticContent.DataBasePath);
		ArrayList<CivilContentE> list = new ArrayList<CivilContentE>();
		Cursor c = dbhelp.query(sql);
		if (c.moveToFirst()) {

			do {
				CivilContentE ce = new CivilContentE();
				ce.setCheck_data(c.getString(1));
				ce.setCheck_position(c.getString(2));
				int doc=0;
				if (c.getString(7).equals("����")) {
					if (c.getString(3).contains("K")) {
						String s3=c.getString(3).replace("K", "");
							doc=Integer.parseInt(s3.replace("+", ""));
						}else{
							 doc = Integer.parseInt(c.getString(3));
						}
					int num_ = doc / 1000;
					int _num = doc % 1000;
//					ce.setMileage(StaticContent.Up_Down + num_ + "+" + _num);
					ce.setMileage(c.getString(3)); //ChenLang modify TODO
				} else {

					ce.setMileage(c.getString(3));
				}
				ce.setJudge_level(c.getString(4));
				ce.setLevel_content(c.getString(5));
				ce.setPic_id(c.getString(6));
				ce.setBelong_pro(c.getString(7));
				ce.setBZ(c.getString(13));
				list.add(ce);

			} while (c.moveToNext());

			c.close();

		}
		c.close();
		dbhelp.close();
		return list;
	}

	public static ArrayList<CivilContentE> SortingLining(String fors) {
		
	
		System.out.println(sql);
		
		  int	i=StaticContent.checkfors;
			System.out.println(i);
			StaticContent.checkfors=i+1;
			
			if (i % 2 == 0) {
				 sql = "select*from CILIV_CHECKCONTENT where  belong_pro='"
					+ "����" + "' and task_id='" + StaticContent.update_id
					+ "'order by " + fors + " asc";
				
				
			} else {
				 sql = "select*from CILIV_CHECKCONTENT where  belong_pro='"
					+ "����" + "' and task_id='" + StaticContent.update_id
					+ "'   order by " + fors + " desc";
			}
		DBHelper dbhelp = new DBHelper(StaticContent.DataBasePath);
		ArrayList<CivilContentE> list = new ArrayList<CivilContentE>();
		Cursor c = dbhelp.query(sql);
		if (c.moveToFirst()) {

			do {
				CivilContentE ce = new CivilContentE();
				ce.setCheck_data(c.getString(1));
				ce.setCheck_position(c.getString(2));
				if (c.getString(7).equals("����")) {
//					int doc = Integer.parseInt(c.getString(3));
//					int num_ = doc / 1000;
//					int _num = doc % 1000;
//					ce.setMileage(StaticContent.Up_Down + num_ + "+" + _num);
					ce.setMileage(c.getString(3)); //ChenLang modify
				} else {
					ce.setMileage(c.getString(3));
				}
				ce.setJudge_level(c.getString(4));
				ce.setLevel_content(c.getString(5));
				ce.setPic_id(c.getString(6));
				ce.setBelong_pro(c.getString(7));
				ce.setBZ(c.getString(13));
				list.add(ce);

			} while (c.moveToNext());
			c.close();
		}
		c.close();
		dbhelp.close();
		return list;
	}

	// �����ճ�����¼��ѯ
	public static ArrayList<EleContentE> Sorting_ele(String fors, int task_id) {
		System.out.println(sql);
		 int	i=StaticContent.checkfors;
			System.out.println(i);
			StaticContent.checkfors=i+1;
			
			if (i % 2 == 0) {
				 sql = "select* from ELECTRICAL_FAC where check_type='" + "���ڼ���"
				+ "'" + "and  task_id='" + StaticContent.update_id + "' order by " + fors + " asc";
				
			} else {
				 sql = "select* from ELECTRICAL_FAC where check_type='" + "���ڼ���"
				+ "'" + "and  task_id='" + StaticContent.update_id + "' order by " + fors + " desc";
			}
		DBHelper dbhelp = new DBHelper(StaticContent.DataBasePath);
		ArrayList<EleContentE> list = new ArrayList<EleContentE>();
		Cursor c = dbhelp.query(sql);
		if (c.moveToFirst()) {
			
			do {
				EleContentE ce = new EleContentE();
				ce.setTacilitiy_type(c.getString(1));
				ce.setDe_date(c.getString(2));
				ce.setDevice_name(c.getString(17));
				ce.setDevice_id(c.getString(4));
				ce.setDevice_position(c.getString(5));
				ce.setHandle(c.getString(6));
				ce.setDevice_state(c.getString(7));
				ce.setFault(c.getString(8));
				list.add(ce);

			} while (c.moveToNext());

			c.close();

		}
		c.close();
		dbhelp.close();
		return list;
	}

	// ���羭���Լ���¼��ѯ
	public static ArrayList<EleContentE> Sorting_ele_jc(String fors, int task_id) {
	    int	i=StaticContent.checkfors;
		System.out.println(i);
		StaticContent.checkfors=i+1;
		
		if (i % 2 == 0) {
			sql = "select*from ELECTRICAL_FAC  where  task_id='" + StaticContent.update_id +"' order by " + fors
					+ " asc";
			
			
			
		} else {
			sql = "select*from ELECTRICAL_FAC  where  task_id='" + StaticContent.update_id + "'  order by " + fors
					+ " desc";
		}
		
		System.out.println(sql);
		DBHelper dbhelp = new DBHelper(StaticContent.DataBasePath);
		ArrayList<EleContentE> list = new ArrayList<EleContentE>();
		Cursor c = dbhelp.query(sql);
		if (c.moveToFirst()) {

			do {
				
				EleContentE ce = new EleContentE();
				ce.setTacilitiy_type(c.getString(1));
				ce.setDe_date(c.getString(2));
				ce.setDevice_name(c.getString(17));
				ce.setDevice_id(c.getString(4));
				ce.setDevice_position(c.getString(5));
				ce.setHandle(c.getString(6));
				ce.setDevice_state(c.getString(7));
				ce.setFault(c.getString(8));
				ce.setCheck_pro(c.getString(9));
				ce.setCheck_content(c.getString(10));
				ce.setCheck_type(c.getString(11));
				ce.setTask_id(c.getInt(12));
				list.add(ce);

			} while (c.moveToNext());

			c.close();

		}
		c.close();
		dbhelp.close();
		return list;
	}

	//
	// public static String getPhotoPath(){
	//
	//
	//
	//
	//
	//
	//
	//
	// return
	// }
	//

	public static void PhotoStore(String photoName, String type, String position) {
		String bhid = StaticContent.bh_id;
		String pid = photoName.substring(1).replace(".jpg", "");
		ContentValues cv = new ContentValues();
		cv.put(ci[0], bhid);
		cv.put(ci[1], photoName.replace(".jpg", ""));
		cv.put(ci[2], pid);
		cv.put(ci[3], position);
		cv.put(ci[4], type);
		UUID uuid = UUID.randomUUID();    
		cv.put("guid", uuid.toString());
		cv.put("update_id", StaticContent.update_id);
		dbHelper.insert("BH_PIC", cv);
		// String sql_ =
		// "insert into DATE_PHOTO(BHID,PROJECTID,PID,PNAME,PTYPE) values({0},{1},{2},{3},{4})";
		// Object[] array = new Object[] { "'" + bhid + "'",
		// "'" + DataProvider.PROJECTID + "'", "'" + pid + "'",
		// "'" + photoName + "'", "'" + type + "'" };
		// String sql = MessageFormat.format(sql_, array);
		// dbHelper.execSql(sql);
		
	}

	public static String GetMaxBHImageID() {
		return "p" + getMaxImageID();
	}
	
	private static String getMaxImageID() {
		
		int mid = getImageID();
		addMaxImageID(mid);
		return String.valueOf(mid);
	}

	// ���id++
	public static void addMaxImageID(int maxid) {
		String sql = "UPDATE TASK SET picture_beginnum='"
				+ String.valueOf(maxid + 1) + "' WHERE _id='"
				+ StaticContent.task_id + "'";
		dbHelper.execSql(sql);
		dbHelper.close();
	}

	/*
	 * �����Ƭ����ʼ���
	 */
	public static int getImageID() {
		String sql = "select picture_beginnum  from TASK WHERE _id='"
				+ StaticContent.task_id + "'";

		Cursor cursor = dbHelper.query(sql);
		int mid = 1;
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {

				int id = cursor.getInt(0);

				mid = id;
				System.out.println("��Ƭ��ʼ��ţ�" + id);
				cursor.moveToNext();
			}
		}
		cursor.close();
		dbHelper.close();
		return mid;
	}

	/***
	 * ɾ�����ݿ��е���Ƭ
	 */

	public static void deletepic(String photoname) {
		String bhid = StaticContent.bh_id;
		String sql = "delete from BH_PIC where bh_id='" + bhid + "' "
				+ "and pic_name='" + photoname + "'";
		dbHelper.execSql(sql);
		System.out.println("ɾ����Ƭ����" + sql);
	}
									
	public static String getpic_id(String hd_id) {
		String S = "";
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select *from BH_PIC where bh_id='" + hd_id
				+ "' order by pic_id ";
		Cursor c = dbHelper.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				do {
					list.add(c.getString(6));

				} while (c.moveToNext());
				
				if (list.size() == 1) {
					S = list.get(0);
				} else if (list.size() == 3) {
					S = list.get(0) + "," + list.get(1) + "," + list.get(2);
				} else if (list.size() == 2) {
					S = list.get(0) + "," + list.get(1);
				} else {
					S = list.get(0) + "," + list.get(1) + "," + list.get(2)
					+ "����";			
				}
				
			}
		}

		return S;

	}

	public static boolean addtask(String up_id) {
		boolean f = true;
		String sql = "select count() from TASK where update_id='" + up_id + "'";
		Cursor c = dbHelper.query(sql);
		int i = -1;
		if (c.moveToFirst()) {
			i = c.getInt(0);
			System.out.println(i);
		}
		if (i == 1) {
			f = false;
		} else {
			f = true;
		}

		return f;

	}

	public static boolean exitson(String section_id) {
		boolean f = true;
		String sql = "select count() from pro_info where father_id='"
				+ section_id + "'";
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
	public static boolean exitsonTunnel(String section_id) {
		boolean f = true;
		String sql = "select count() from TURNNEL where Line_id='"
				+ section_id + "'";
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
	
	// ����ǰ׮����Ϣ�������ݿ�
	public static void InsertCurrentZh(int current_zh, String update_id,int up_down) {
		String current="";
	    current="up_current_lczh";
		String sql = "update TASK  set "+current+"='" + current_zh
				+ "' where update_id='" + update_id + "'";
		System.out.println(sql);
		dbHelper.execSql(sql);

	}

	public static int GetCurrentZh(String id,int ud_type) {
		String up_down="";
	    up_down="up_current_lczh";
		
		String sql = "select "+up_down+" from Task where update_id='" + id
				+ "'";
		int now = 0;
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {

				now = c.getInt(0);
				
			} while (c.moveToNext());
		}
		return now;
	}

	public static void deleteBH(String update_id) {

		String sql = "delete from CILIV_CHECKCONTENT where task_id='"
				+ update_id + "'";
		System.out.println("sql:::" + sql);
		
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		list = getbh(update_id);
		for (int i = 0; i < list.size(); i++) {
			int c = Integer.parseInt(list.get(i));
			System.out.println("list������id=" + c);
			deleteBHpic(c);
			
			path = GetPicPath(c);
			DeleteBasePic(path);
		}
		dbHelper.execSql(sql);
	}

	//
	public static ArrayList<String> getbh(String update_id) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select _id from CILIV_CHECKCONTENT where task_id='"
				+ update_id + "'";
		System.out.println("��������id");
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				int id = c.getInt(0);
				list.add(id + "");
			} while (c.moveToNext());
			c.close();
		}

		return list;
	}

	public static void deleteBHpic(int id) {

		String sql = "delete from BH_PIC where bh_id='" + id + "'";
		System.out.println("ɾ�� " + id);
		dbHelper.execSql(sql);
	}

	public static ArrayList<String> GetPicPath(int bhid) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select pic_pistion from BH_PIC where bh_id='" + bhid
				+ "'";
		System.out.println("�����Ƭ��·��" + sql);
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				String id = c.getString(0)+".jpg";
				list.add(id);
				System.out.println("��ȡ��Ƭ·��-" + id);
			} while (c.moveToNext());
			c.close();
		}

		return list;
	}

	public static void DeleteBasePic(ArrayList<String> list) { 
		for (int i = 0; i < list.size(); i++) {
			try {
				File file = new File(list.get(i));
				if (file.exists()) {
					file.delete();
					
				}
			} catch (Exception e) {
				System.out.println("ɾ���ļ��쳣:e=" + e.getMessage());
			}

		}
		
	}
	/**
	 * ��������Ϣ����hidecheckpic����
	 * @param map
	 */
	public static void insertHidecheckpic(HashMap<String, String> map){
		if(map == null){
			return;
		}
		sql = "SELECT * FROM hidecheckpic WHERE picName = '"+map.get("picName")+"'";
		Cursor c = null;
		try{
			c = dbHelper.query(sql);
			if(!c.moveToNext()){
				ContentValues values = new ContentValues();
				values.put("checkRecordId", map.get("checkRecordId"));
				values.put("picName", map.get("picName"));
				values.put("picPath", map.get("picPath"));
				values.put("picDate", map.get("picDate"));
				values.put("uploadState", map.get("uploadState"));
				values.put("guid", map.get("guid"));
				dbHelper.insert("hidecheckpic", values);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	}
	/**
	 * ��������Ϣ����hideCheckDevicePic����
	 * @param map
	 */
	public static void insertHideCheckDevicePic(HashMap<String, String> map){
		if(map == null){
			return;
		}
		sql = "SELECT * FROM hideCheckDevicePic WHERE picName = '"+map.get("picName")+"'";
		Cursor c = null;
		try{
			c = dbHelper.query(sql);
			if(!c.moveToNext()){
				ContentValues values = new ContentValues();
				values.put("deviceRecordId", map.get("deviceRecordId"));
				values.put("picName", map.get("picName"));
				values.put("picPath", map.get("picPath"));
				values.put("picDate", map.get("picDate"));
				values.put("uploadState", map.get("uploadState"));
				values.put("guid", map.get("guid"));
				dbHelper.insert("hideCheckDevicePic", values);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	}
	
	/**
	 * �޸���Ƭ����
	 * @param id
	 * @param num
	 */
	public static void updateCheckRecordPicNum(String id,int num){
		sql = "SELECT * FROM checkRecord WHERE id ='"+id+"'";
		Cursor c = null;
		String picNum = "";
		try{
			c = dbHelper.query(sql);
			if(c.moveToNext()){
				String picNumber = c.getString(c.getColumnIndex("picNumber"));
					try {
						if((num+Integer.parseInt(picNumber)) < 0){
							picNum = "0";
						}else{
							picNum = String.valueOf(num+Integer.parseInt(picNumber));
						}
					} catch(Exception e) {
						picNum = String.valueOf(num);
					}
					
				String updateSql = "UPDATE checkRecord SET picNumber = '"+picNum+"' WHERE id = '"+id+"'";
				dbHelper.execSql(updateSql);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	}
	/**
	 * ɾ�������Ϣ
	 * @param id
	 */
	public static void delCheckRecordHidecheckpic(String id){
		Cursor c = null;
		try{
			dbHelper.execSql("DELETE FROM checkRecord WHERE id = '"+id+"'");
			sql = "SELECT * FROM hidecheckpic WHERE checkRecordId = '"+id+"'";
			c = dbHelper.query(sql);
			while(c.moveToNext()){
				dbHelper.execSql("DELETE FROM hidecheckpic WHERE checkRecordId = '"+id+"'");
				File file = new File(c.getString(c.getColumnIndex("picPath")));
				 if(file.exists()){
					 file.delete();
				 }
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	}
	/**
	 * ɾ�������Ϣ
	 * @param id
	 */
	public static void delDeviceRecordHideCheckDevicePic(String id){
		Cursor c = null;
		try{
			dbHelper.execSql("DELETE FROM deviceRecord WHERE id = '"+id+"'");
			sql = "SELECT * FROM hideCheckDevicePic WHERE deviceRecordId = '"+id+"'";
			c = dbHelper.query(sql);
			while(c.moveToNext()){
				dbHelper.execSql("DELETE FROM hideCheckDevicePic WHERE deviceRecordId = '"+id+"'");
				File file = new File(c.getString(c.getColumnIndex("picPath")));
				if(file.exists()){
					file.delete();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	}
	/**
	 * �����Ƭ��ַ
	 * @param checkRecordId
	 * @return
	 */
	public static ArrayList<String> getPicPath(String checkRecordId){
		ArrayList<String> list = new ArrayList<String>();
		sql = "SELECT * FROM hidecheckpic WHERE checkRecordId = '"+checkRecordId+"'";
		Cursor c = null;
		try{
			c = dbHelper.query(sql);
			while(c.moveToNext()){
				list.add(c.getString(c.getColumnIndex(("picPath"))));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
		return list;
	}
	/**
	 * �����Ƭ��ַ
	 * @param checkRecordId
	 * @return
	 */
	public static ArrayList<String> getHideCheckDevicePicPath(String deviceRecordId){
		ArrayList<String> list = new ArrayList<String>();
		sql = "SELECT * FROM hideCheckDevicePic WHERE deviceRecordId = '"+deviceRecordId+"'";
		Cursor c = null;
		try{
			c = dbHelper.query(sql);
			while(c.moveToNext()){
				list.add(c.getString(c.getColumnIndex(("picPath"))));
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(c != null){
				c.close();
			}
			if(dbHelper != null){
				dbHelper.close();
			}
		}
		return list;
	}
	/**
	 * ɾ����Ƭ
	 * @param picPath
	 */
	 public static void delHidecheckpic(String picPath){
		 try{
			 sql = "DELETE FROM hidecheckpic WHERE picPath = '"+picPath+"'";
			 dbHelper.execSql(sql);
			 File file = new File(picPath);
			 if(file.exists()){
				 file.delete();
			 }
		 }catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dbHelper != null){
				dbHelper.close();
			}
		}
	 }
	 /**
	  * ɾ����Ƭ
	  * @param picPath
	  */
	 public static void delHideCheckDevicePic(String picPath){
		 try{
			 sql = "DELETE FROM hideCheckDevicePic WHERE picPath = '"+picPath+"'";
			 dbHelper.execSql(sql);
			 File file = new File(picPath);
			 if(file.exists()){
				 file.delete();
			 }
		 }catch (Exception e) {
			 e.printStackTrace();
		 }finally{
			 if(dbHelper != null){
				 dbHelper.close();
			 }
		 }
	 }
	
	public static int getCheckContentId(int checktypeid, String CheckContent) {
		int i = -1;
		sql = "select POSITIONID from BHPOSITION_DIC where CHECKITEMID='" + checktypeid
				+ "' and POSITIONNAME='" + CheckContent + "'";
		dbHelper.query(sql);
		
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {

				i = c.getInt(0);
				
			} while (c.moveToNext());
		}
		
		return i;

	}
	
	public static ArrayList<String> GetFirstP_level(String check_pro, int check_id) {
		ArrayList<String> list = new ArrayList<String>();
		sql="select CheckItemDesc from CheckItemDescInfos where CheckId='"+check_id+"'";
		Cursor c1 =dbHelper.query(sql);
		if (c1.moveToFirst()) {
			
			list = new ArrayList<String>();
			do {
				list.add(c1.getString(0));
				
			} while (c1.moveToNext());

			// adapter3 = new CivilLevelAdapter(s3,
			// Portal.this,Portal.this);
			// PortalListview3.setAdapter(adapter3);
		}
		return list;
	}
	
	public static ArrayList<String> GetNewP_level(int checkid) {
		ArrayList<String> list = new ArrayList<String>();
		sql = "select CheckItemDesc from CheckItemDescInfos where CheckId='"+checkid+"'";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {

			do {
				
				list.add(c.getString(1));
//				list.add(c.getString(4));
//				list.add(c.getString(5));
			} while (c.moveToNext());
			
		}

		return list;
		
	}
	
	public static  Map<String,ArrayList<String>> GetBaseNameInfo(String BaseNameType){
		
		Map<String, ArrayList<String>> map=new HashMap<String, ArrayList<String>>();
		ArrayList<String> name=new ArrayList<String>();
		ArrayList<String> value=new ArrayList<String>();
		sql ="select PARAMNAME,PARAMVALUE from BaseDictionaries where PARAMTYPE='"+BaseNameType+"'";
				
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {

			do {
				
				name.add(c.getString(0));
				value.add(c.getString(1));
			} while (c.moveToNext());
			
		}
		map.put("name", name);
		map.put("value", value);
		
		return map;
		
	}
	
	public static Map<String, ArrayList<String>> GetTaskPicPath(String update_id){
		Map<String, ArrayList<String>> map=new HashMap<String, ArrayList<String>>();
		ArrayList<String> name=new ArrayList<String>();	
		ArrayList<String> path=new ArrayList<String>();
		String sql="select pic_pistion,guid from BH_PIC where update_id='"+update_id+"'and type='"+"��"+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				path.add(c.getString(0));
				name.add(c.getString(1)+".jpg");
				
			} while (c.moveToNext());			
			map.put("PicPath", path);
			map.put("PicName", name);
		}
		
		return map;
	}
	

	public static DiseaseInfoDto GetCheckId(int checkid) {
		DiseaseInfoDto  di=new DiseaseInfoDto();
		sql = "select check_content,CHECKITEMID from CIVIL_CHECK_INFO where CHECKID='"+checkid+"' ";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			
			do {
				di.setDiseaseName(c.getString(0));
				di.setCheckItem(c.getString(1));
				
			} while (c.moveToNext());
			
		}
		
		return di;
		
	}
	public static ArrayList<String> GetManagerUnit(){
		ArrayList<String> list=new ArrayList<String>();
		sql="select ManagerName from ManagerUnit ";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
			list.add(c.getString(0));
				
				
			} while (c.moveToNext());
		}
		
		
		
		return list;
	}
	public static String GetManagerCode(String name){
//		ArrayList<String> list=new ArrayList<String>();
		String s="";
		sql="select UnitCode from  ManagerUnit where ManagerName='"+name+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				s=c.getString(0);
				
			} while (c.moveToNext());
		}
		
		
		
		return s;
	}
	public static String GetManagerName(String unitcode){
//		ArrayList<String> list=new ArrayList<String>();
		String s="";
		sql="select ManagerName from  ManagerUnit where UnitCode='"+unitcode+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				s=c.getString(0);
				
			} while (c.moveToNext());
		}
		
		
		
		return s;
	}
	
	
	
	
	public static String jsondemo(){
		String s="";
		
		JSONArray jarr=new JSONArray();
		JSONObject jo=new JSONObject();
		JSONObject jo2=new JSONObject();
		JSONObject jo3=new JSONObject();
		JSONObject jo4=new JSONObject();
		JSONObject jo5=new JSONObject();
		try {
			jo.put("ManagerUnitName", "��Ԫ�Ź���վ");
			jo.put("UnitCode", 25);
			jo.put("RoadCodeList", "S14,S15,16,S20");
			jo2.put("ManagerUnitName", "���������վ");
			jo3.put("ManagerUnitName", "�۰ӹ���վ");
			jo4.put("ManagerUnitName", "�ڽ�����վ");
			jo5.put("ManagerUnitName", "ę́����վ");
			jarr.put(jo);
			jarr.put(jo2);
			jarr.put(jo3);
			jarr.put(jo4);
			jarr.put(jo5);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		s=jarr.toString();
		return s;
	}
	
	
	//��ü����Ա���
	public static  int getcheckhead(String name){
		int i=0;
		String sql="select  check_id from CHECKINFO where name='"+name+"'";
		Cursor c=dbHelper.query(sql);
		if (!(c.getCount()==0)) {
			if (c.moveToFirst()) {
				i=c.getInt(0);
			}	
		}
		
		
		return i;
	}
	
	//��õ�ǰ����վ�����Ա
	public static     Map<String, ArrayList<String>>  getcheckheadunit(){
		Map<String, ArrayList<String>> map=new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> s=new ArrayList<String>();
		ArrayList<String> s2=new ArrayList<String>();
		
		
		String sql="select  name,check_id from CHECKINFO where  type='P' and BelongUnit='"+StaticContent.UnitCode+"'";
		Cursor c=dbHelper.query(sql);
		if (!(c.getCount()==0)) {
			if (c.moveToFirst()) {
				s.add(c.getString(0));
				s2.add(c.getString(1));
				map.put("name", s);
				map.put("code", s2);
				
			}	
		}
		
		
		return map;
	}
	//���뵱ǰ����վ���������������Ϣ
	public static  void savalineinfo(String s,String code){
		String ss[]=s.split(",");
		int i =ss.length;
		for (int j = 0; j < ss.length; j++) {
			String tname=ss[j];
			System.out.println("roadlist:  "+tname);
 			ContentValues cv=new ContentValues();
 			cv.put("name", tname);
 			cv.put("BelongUnit", code);
			cv.put("type", "R");
			dbHelper.insert("CHECKINFO", cv);
		}
		
	};
	
	/**
	 *��õ�ǰ����վ������
	 **/
	public static  ArrayList<String> GetUnitTunnle(){
		ArrayList<String> s=new ArrayList<String>();
		String sql="select name from CHECKINFO where type='R' and BelongUnit ='"+StaticContent.UnitCode+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			do {
				String sname=c.getString(0);
				DB_Provider cc=new DB_Provider();
				String setionname=cc.GetUnitSectionName(sname);
				System.out.println("��ǰ����վ������"+sname);
				if (s.indexOf(setionname)==-1) {
					s.add(setionname);	
				}
			} while (c.moveToNext());
			
		}
		return s;
		
	}
	//��·����
	public static  ArrayList<String> GetLineName(){
		ArrayList<String> s=new ArrayList<String>();
		String sql="select name from CHECKINFO where type='R' and BelongUnit ='"+StaticContent.UnitCode+"'";
		Cursor c=dbHelper.query(sql);
		
		if (c.moveToFirst()) {
			
			do {
				String sname=c.getString(0);
//				String setionname=GetUnitSectionName(sname);
				System.out.println("��ǰ����վ����·"+sname);
				s.add(sname);
			} while (c.moveToNext());
			
			
		}
		return s;
		
	}
	
	
	public  String GetUnitSectionName(String linename){
		
		String sql1="select father_id from PRO_INFO where section_id='"+linename+"'";
		String sectionname="";
		Cursor c=dbHelper.query(sql1);
		if (c.moveToFirst()) {
			String s=c.getString(0);
			System.out.println(s);
			if (s.equals("0")) {
				sectionname=linename;
			}else{
				String sql2="select section_name from PRO_INFO where father_id='"+s+"'";
				Cursor c1=dbHelper.query(sql2);
				if (c1.moveToFirst()) {
					sectionname=c.getString(0);
				}
				
			}
		}
		System.out.println("sectionname::+"+sectionname);
		
		return sectionname;
	}
	
	/**
	 *�����·����
	 */
	public static ArrayList<String> GetSectionName(ArrayList<String > name){
		ArrayList<String> s=new ArrayList<String>();
		
		for (int i = 0; i < name.size(); i++) {
			String sql="select section_name from PRO_INFO where section_id='"+name.get(i)+"'";
			Cursor c=dbHelper.query(sql);
			if (c.moveToFirst()) {
				s.add(c.getString(0));	
			}
		}
		
		
		return s;
	}
	//������������
	public static String GetTaskDate(String updateid){
		String s="";
		String sql="select check_date from TASK where update_id='"+updateid+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			s=c.getString(0);
		}
		
		return s;
	}
	
	public static String GetTaskUp_Down(String updateid){
		String s="";
		String sql="select up_num,down_num from TASK where update_id='"+updateid+"'";
		Cursor c=dbHelper.query(sql);
		if (c.moveToFirst()) {
			 String s1=c.getString(0);
			 String s2=c.getString(1);
			 s=s1+"-"+s2;
			                 
		}
		
		return s;
	}
	
	/***
	 * ��ȡ�����Ŀ����Ϣ
	 */
	public static ArrayList<HashMap<String, String>> getCheckProjectInfo(){
		String sql="select * from checkProject";
		Cursor c=null;
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		try{			
			c=dbHelper.query(sql);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", c.getString(c.getColumnIndex("id")));
				map.put("name",c.getString(c.getColumnIndex("name")));
				map.put("code", c.getString(c.getColumnIndex("code")));
				map.put("isCheck", "false");
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(dbHelper!=null){
				dbHelper.close();
			}
		}
		return arrayList;
	}
	
	public static String getTunnelName(String projectId){
		Cursor c=null;
		String sql=String.format("select name from checkProject where id= %s ", projectId);
		try{
			c=dbHelper.query(sql);
			if(c.moveToFirst()){
				return c.getString(c.getInt(0));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null)
				c.close();
		}
		return "";
	}
	
	/**
	 * �жϱ����Ƿ������ͬ��׮��
	 * @param projectId ��ĿId
	 * @param mileage  ��ʼ���׮��
	 * @return true ����,otherwise
	 */
	public static boolean queryTunnelIsExist(String projectId,String mileage){
		Cursor c=null;
		String sql=String.format("select * from checkForm where projectId=%s AND  mileage=%s", projectId,mileage);
		try{
			c=dbHelper.query(sql);
			if(c.moveToFirst()){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null)
				c.close();
		}
		return false;
	}
	/**
	 * ��ȫ�����Ų��¼����checkForm��Ϣ¼��
	 * @param projectId ����Id
	 * @param projectName ��Ŀ����
	 * @param tunnelName  �������
	 * @param startUnit        ��Ӫ����λ
	 * @param startMileage  ��ʼ���
	 * @param endMileage   �������
	 * @param checkPerson  �����
	 * @param date				�������
	 */
	public static void insertCheckForm(HashMap<String, String> map){
		ContentValues  values=new ContentValues();
		values.put("projectId", map.get("projectId"));
		values.put("lineCode", map.get("lineCode")); //��·����
		values.put("tunnelCode", map.get("tunnelCode")); //�������
		values.put("projectName", map.get("projectName"));
		values.put("tunnelName", map.get("tunnelName"));
		values.put("section", map.get("section"));//ʩ�����
		values.put("startUnit", map.get("startUnit"));
		values.put("startMileage", map.get("startMileage"));
		values.put("endMileage", map.get("endMileage"));
		values.put("checkPerson", map.get("checkPerson"));
		values.put("date", map.get("date"));
		try{
			dbHelper.insert("checkForm", values);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	/**
	 * ��ȡ����¼����������Ϣ
	 * @param projectId  ��Ӧ��checkProject ����id
	 * @return  ArrayList<HashMap<String,String>
	 */
public static ArrayList<HashMap<String, String>> getCheckFormInfo(String projectId){
		ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
		Cursor c=null;
		String sql=String.format("select * from checkForm where projectId =%s", projectId);
		try{
			c=dbHelper.query(sql);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", String.valueOf(c.getInt(c.getColumnIndex("id"))));
				map.put("projectId", c.getString(c.getColumnIndex("projectId")));
				map.put("lineCode", c.getString(c.getColumnIndex("lineCode"))); //��·����
				map.put("tunnelCode", c.getString(c.getColumnIndex("tunnelCode"))); //�������
				map.put("projectName", c.getString(c.getColumnIndex("projectName")));
				map.put("tunnelName", c.getString(c.getColumnIndex("tunnelName")));
				map.put("section", c.getString(c.getColumnIndex("section")));//ʩ�����
				map.put("startUnit", c.getString(c.getColumnIndex("startUnit")));
				map.put("startMileage", c.getString(c.getColumnIndex("startMileage"))); //��ʼ���
				map.put("endMileage", c.getString(c.getColumnIndex("endMileage"))); //�������
				map.put("checkPerson", c.getString(c.getColumnIndex("checkPerson")));
				map.put("date",c.getString(c.getColumnIndex("date")));
				map.put("uploadState", c.getString(c.getColumnIndex("uploadState")));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null)
				c.close();
			if(dbHelper!=null){
				dbHelper.close();
			}
		}
		return arrayList;
}

/**
 *��������ID �޸ļ�¼���еļ�¼
  */
 public static  void updateCheckForm(HashMap<String, String> map){
	  ContentValues  values=new ContentValues();
		values.put("lineCode", map.get("lineCode")); //��·����
		values.put("tunnelCode", map.get("tunnelCode")); //�������
		values.put("projectName", map.get("projectName"));
		values.put("tunnelName", map.get("tunnelName"));
		values.put("section", map.get("section"));//ʩ�����
		values.put("startUnit", map.get("startUnit"));
		values.put("startMileage", map.get("startMileage"));
		values.put("endMileage", map.get("endMileage"));
		values.put("checkPerson", map.get("checkPerson"));
		values.put("date", map.get("date"));
	 try{
//		 dbHelper.update("checkForm", values, "projectId=?", new String[]{map.get("projectId")});		 
		 dbHelper.update("checkForm", values, "id=?", new String[]{map.get("id")});		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }
 }
 
/**
 * ɾ��������Ϣ
 * @param id
 */
 public static void delCheckForm(String id){
	 Cursor c=null;
	 try{
		 dbHelper.execSql("DELETE FROM checkForm WHERE id ='"+id+"'"); 
		 sql = "SELECT * FROM checkRecord WHERE checkFormId='"+id+"'";
		 c = dbHelper.query(sql);
		 while(c.moveToNext()){
			 String picSql = "SELECT * FROM hidecheckpic WHERE checkRecordId = '"+c.getString(c.getColumnIndex("id"))+"'";
			 Cursor cursor = null;
			 try{
				 cursor = dbHelper.query(picSql);
				 while(cursor.moveToNext()){
					 dbHelper.execSql("DELETE FROM hidecheckpic WHERE picPath = '"+cursor.getString(cursor.getColumnIndex("picPath"))+"'");
					 File file = new File(cursor.getString(cursor.getColumnIndex("picPath")));
					 if(file.exists()){
						 file.delete();
					 }
				 }
			 }finally{
				 if(cursor != null){
					 cursor.close();
				 }
			 }
			 dbHelper.execSql("DELETE FROM checkRecord WHERE id ='"+c.getString(c.getColumnIndex("id"))+"'");
		 }	 
	 }catch (Exception e) {
		 e.printStackTrace();
	 }finally{
		 if(c != null){
			 c.close();
		 }
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	
 }
 
 
 
 /**
  * ��ѯ��checkForm����¼���ϴ�״̬����Ϣ
  * @param id   
  * @return true ���ϴ� ,otherwise false
  */
 public  static boolean  queryCheckFormUploadState(String id){
	 Cursor c=null;
	 String sql=String.format("select uploadState from checkForm where id='%s'", id);
	 try{
		 c=dbHelper.query(sql);
		 if(c.moveToFirst()){
			 if("1".equals(c.getString(c.getColumnIndex("uploadState")))){
				 return true;
			 }
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null){
			 c.close();
		 }
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	 return false;
 }
 
 /**
  * �޸ı�checkForm���ϴ�״̬
  * @param id ����ID
  */
 public static void uploadCheckFromUploadState(String id){
	 ContentValues values=new ContentValues();
	 values.put("uploadState", "1");
	 try{
		 dbHelper.update("checkForm", values, "id=?", new String[]{id});
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	 
 }
 /**
  * ɾ��������Ϣ
  * @param id
  */
 public static void deleCheckDevice(String id){
	 Cursor c=null;
	 try{
		 dbHelper.execSql("DELETE FROM checkDevice WHERE id ='"+id+"'"); 
		 sql = "SELECT * FROM deviceRecord WHERE deviceId='"+id+"'";
		 c = dbHelper.query(sql);
		 while(c.moveToNext()){
			 String picSql = "SELECT * FROM hideCheckDevicePic WHERE deviceRecordId = '"+c.getString(c.getColumnIndex("id"))+"'";
			 Cursor cursor = null;
			 try{
				 cursor = dbHelper.query(picSql);
				 while(cursor.moveToNext()){
					 dbHelper.execSql("DELETE FROM hideCheckDevicePic WHERE picPath = '"+cursor.getString(cursor.getColumnIndex("picPath"))+"'");
					 File file = new File(cursor.getString(cursor.getColumnIndex("picPath")));
					 if(file.exists()){
						 file.delete();
					 }
				 }
			 }finally{
				 if(cursor != null){
					 cursor.close();
				 }
			 }
			 dbHelper.execSql("DELETE FROM deviceRecord WHERE id ='"+c.getString(c.getColumnIndex("id"))+"'");
		 }	 
	 }catch (Exception e) {
		 e.printStackTrace();
	 }finally{
		 if(c != null){
			 c.close();
		 }
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	 
 }
 
 
 /**
  * ��ѯ�ϴ�״̬
  * @return true ���ϴ�,otherwise false
  */
 public static boolean  queryUploadState(String id){
	 Cursor c=null;
	 String  sql=String.format("select * from checkForm where id=%d AND uploadState=%d", id,1);
	 try{
		c= dbHelper.query(sql);
		if(c.moveToFirst()){
			return true;
		}
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null)
			 c.close();
	 }
	 return false;
 }
 
 
 public static ArrayList<HashMap<String, String>> getCheckPorjectItem(String projectCode){
	 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	 Cursor c=null;
	 String sql=String.format("select * from checkProjectItem where  projectCode= '%s'", projectCode);
	 try{
		 c=dbHelper.query(sql);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("id",c.getString(c.getColumnIndex("_id")));
			 map.put("projectCode", c.getString(c.getColumnIndex("projectCode")));
			 map.put("code", c.getString(c.getColumnIndex("code")));
			 map.put("name", c.getString(c.getColumnIndex("name")));
			 arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null){
			 c.close();
		 }
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	 return arrayList;
 }
 
 
 public static ArrayList<HashMap<String, String>> getCheckContent(String itemCode){
	 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	 Cursor c=null;
	 String sql=String.format("select * from  checkContent where itemCode= '%s'",itemCode);
	 try{
		 c= dbHelper.query(sql);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("id", c.getString(c.getColumnIndex("id")));
			 map.put("content", c.getString(c.getColumnIndex("content")));
			 map.put("itemCode", c.getString(c.getColumnIndex("itemCode")));
			 map.put("code", c.getString(c.getColumnIndex("code")));
			 map.put("isCheck", "false");
			 arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null){
			 c.close();
		 }
	 }
	 return arrayList;
 }
 
 
 
 /**
  * ��ѯ��checkContent�����豸��Ϣ��content
  * id>128 �������豸��Ϣ
  * @return ArrayList<HashMap<String,String>>
  */
 public static ArrayList<HashMap<String, String>> queryCheckContentDeviceInfo(){
	 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	 String sql=String.format("select * from checkContent where id>%d", 128);
	 Cursor c=null;
	 try{
		 c=dbHelper.query(sql);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("id", c.getString(c.getColumnIndex("id")));
			 map.put("content", c.getString(c.getColumnIndex("content")));
			 map.put("itemCode", c.getString(c.getColumnIndex("itemCode")));
			 map.put("code", c.getString(c.getColumnIndex("code")));
			 map.put("isCheck", "false");
			 arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null)
			 c.close();
		  if(dbHelper!=null)
			  dbHelper.close();
	 }
	 return arrayList;
 }
 
 /*********************����¼��checkRecord****************************/
 
 /**
  * ��ѯ���м�¼����Ϣ
  * @param checkFormId ��ID
  * @param itemCode ѡ���� ��Ӧ��checkProjectItem code��
  */
 public static ArrayList<HashMap<String, String>>  queryCheckRecord(String checkFormId,String itemCode){
	 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	 Cursor c=null;
	 String sql=String.format("select * from checkRecord where checkFormId='%s' AND ItemCode='%s'", checkFormId,itemCode);
	 try{
		 c=dbHelper.query(sql);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("id", c.getString(c.getColumnIndex("id")));
			 map.put("checkFormId", c.getString(c.getColumnIndex("itemCode")));
			 map.put("contentCode", c.getString(c.getColumnIndex("contentCode")));
			 map.put("mileage", c.getString(c.getColumnIndex("mileage")));
			 map.put("checkContent", c.getString(c.getColumnIndex("checkContent"))) ; //�������
			 map.put("picNumber", c.getString(c.getColumnIndex("picNumber"))); //��Ƭ���
			 map.put("describe", c.getString(c.getColumnIndex("describe"))); //��Ƭ����
			 arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null){
			 c.close();
		 }
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
	 return arrayList;
 }
 
 
 /**
  * ��ѯ��checkRecord ��Ϣ
  *@return ArrayList<Hash<String,String>>
  */
 //TODO
 public static ArrayList<HashMap<String, String>>  queryTableCheckRecord(String checkFromId){
	 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	 Cursor c=null;
	 String sql=String.format("select * from checkRecord where checkFormId='%s'",checkFromId);
	 try{
		 c=dbHelper.query(sql);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("id", c.getString(c.getColumnIndex("id")));
			 map.put("checkFormId", c.getString(c.getColumnIndex("checkFormId")));
			 map.put("itemCode", c.getString(c.getColumnIndex("itemCode")));
			 map.put("contentCode", c.getString(c.getColumnIndex("contentCode")));
			 map.put("mileage", c.getString(c.getColumnIndex("mileage")));
			 map.put("describe", c.getString(c.getColumnIndex("describe")));
			 arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(c!=null){
			 c.close();
		 }
		 if(dbHelper!=null){
			dbHelper.close(); 
		 }
	 }
	 return arrayList;
 }
 /**
  * �����checkRecord�����Ų��¼����
  * @param checkFormId    ��Id ��Ӧ��checkForm id��
  * @param itemCode         ѡ����  ��Ӧ��checkPorjectItem  code��
  * @param contentCode    ������ݱ�� ��Ӧ��checkContent   code��
  * @param mileage  	        ���  ��ʽΪ0+0
  * @param checkContent   �������
  * @param picNumber        ��Ƭ���,��Ƭ����
  * @param decribe              ��������
  */
 public static void insertCheckRecord(String checkFormId,String itemCode,String contentCode,String mileage,String checkContent,
		 String picNumber,String decribe){
	 ContentValues values=new ContentValues();
	 values.put("checkFormId", checkFormId);
	 values.put("itemCode", itemCode);
	 values.put("contentCode", contentCode);
	 values.put("mileage", mileage);
	 values.put("checkContent", checkContent);
	 values.put("picNumber", picNumber);
	 values.put("describe", decribe);
	 try{
		 dbHelper.insert("checkRecord", values);
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
 }
 
 /**
  * ���ݱ�checkRecord id ɾ��ָ������
  * @param id ����id
  */
 public static  void   delCheckRecord(String id){
	 try{
		 dbHelper.delete("checkRecord", "id=?",new String[]{id});
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
 }
 
 /***********��checkDevice  DML**********************************************/
 
 
 /**
  * ������Ϣ����checkDevice��
  * @param projectName  ��Ŀ����
  * @param section			  ʩ�����
  * @param unit				  ��λ����
  * @param checkPerson   �����Ա
  * @param checkDate      �������
  */
 
 public static void  insertCheckDevice(String projectName,String section,String unit,String checkPerson,String checkDate){
	 ContentValues values=new ContentValues();
	 values.put("projectName", projectName);
	 values.put("section", section);
	 values.put("unit", unit);
	 values.put("checkPerson", checkPerson);
	 values.put("checkDate",checkDate);
	 try{
	  	 dbHelper.insert("checkDevice", values);
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
 }
 
 
 /**
  * ��������Idɾ����checkDevice����Ϣ
  * @param id ����Id
  */
 public static void  delCheckDevice(String id){
	 try{
		 dbHelper.delete("checkDevice", "id=?", new String[]{id});
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(dbHelper!=null){
			 dbHelper.close();
		 }
	 }
 }
 
 
 /**
  * ��ѯ��checkDevice ������Ϣ
  * @return ArrayList<HashMap<String,String>>
  */
 
public static ArrayList<HashMap<String, String>> queryCheckDevice(){
	ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=null;
	String sql="select * from checkDevice";
	try{
		c=dbHelper.query(sql);
		while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("id", c.getString(c.getColumnIndex("id")));
			map.put("projectName", c.getString(c.getColumnIndex("projectName")));
			map.put("section", c.getString(c.getColumnIndex("section")));
			map.put("unit", c.getString(c.getColumnIndex("unit")));
			map.put("checkPerson", c.getString(c.getColumnIndex("checkPerson")));
			map.put("checkDate", c.getString(c.getColumnIndex("checkDate")));
			arrayList.add(map);
 		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return arrayList;
 }



/*********************************�豸��¼�� deviceRecord DML*************************************/

/**
 *�����豸Id ��ѯ�豸��¼��
 * @param deviceId  ��Ӧ��checkDevice ������id
 * @return  ArrayList<Hash<String,String>>
 */
public static ArrayList<HashMap<String, String>> queryTableDeviceRecord(String deviceId){
	ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=null;
	String sql=String.format("select * from deviceRecord where deviceId='%s'", deviceId);
	try{
		c=dbHelper.query(sql);
		while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("id", c.getString(c.getColumnIndex("id")));
			map.put("deviceId", c.getString(c.getColumnIndex("deviceId")));
			map.put("itemCode", c.getString(c.getColumnIndex("itemCode")));
			map.put("contentCode", c.getString(c.getColumnIndex("contentCode")));
			map.put("name", c.getString(c.getColumnIndex("name")));
			map.put("number", c.getString(c.getColumnIndex("number")));
			map.put("checkContent", c.getString(c.getColumnIndex("checkContent")));
			map.put("approachRecord", c.getString(c.getColumnIndex("approachRecord")));
			map.put("loaction", c.getString(c.getColumnIndex("loaction")));
			map.put("acceptanceRecord", c.getString(c.getColumnIndex("acceptanceRecord")));//���ռ�¼
			map.put("operator", c.getString(c.getColumnIndex("operator"))); //������
			map.put("writtenRecord", c.getString(c.getColumnIndex("writtenRecord")));//�����¼
			map.put("certificate", c.getString(c.getColumnIndex("certificate")));//֤��
			map.put("archives", c.getString(c.getColumnIndex("archives")));//��¼����
			map.put("picNumber", c.getString(c.getColumnIndex("picNumber")));
			map.put("remark", c.getString(c.getColumnIndex("remark")));
			arrayList.add(map);
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return arrayList;
}


/**
 *�������ݵ�deviceRecord�� 
 * @param map
 */
public  static void   insertTableDeviceRecord(HashMap<String, String> map){
	ContentValues  values=new ContentValues();
	try{
		for(Entry<String, String> entry:map.entrySet()){
			values.put(entry.getKey(), entry.getValue());
		}
		dbHelper.insert("deviceRecord", values);
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
}

/**
 * ��������idɾ����DeviceRecord����
 */
public static void delTableDeviceRecord(String id){
	try{
		dbHelper.delete("deviceRecord", "id=?", new String[]{id});
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
}

/*** ��hideCheckPic DML ************************/

/**
 * ���ݱ�checRecord ����id��ѯ��hideCheckPic ����Ƭ��Ϣ
 * @param checkRecordId  ��Ӧ�� hideCheckPic������id
 * @return ArrayList<Hash<String,String>>
 */
public static ArrayList<HashMap<String, String>>  queryTableHideCheckPic(String checkRecordId){
	ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=null;
	String sql=String.format("select * from hideCheckPic where checkRecordId= '%s'", checkRecordId);
	try{
		c=dbHelper.query(sql);
		while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("id", c.getString(c.getColumnIndex("id")));
			map.put("checkRecordId", c.getString(c.getColumnIndex("checkRecordId")));
			map.put("picName", c.getString(c.getColumnIndex("picName")));
			map.put("picPath", c.getString(c.getColumnIndex("picPath")));
			map.put("picDate", c.getString(c.getColumnIndex("picDate")));
			map.put("uploadState", c.getString(c.getColumnIndex("uploadState")));
			map.put("guid", c.getString(c.getColumnIndex("guid")));
			arrayList.add(map);
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return arrayList;
}

/**
 * ��ѯ��PRO_INFO��������·����Ϣ
 *@return ArrayList<HashMap<String,String>>
 */

public static ArrayList<HashMap<String, String>> queryLine(){
	ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=null;
	String sql="select * from PRO_INFO";
	try{
		c=dbHelper.query(sql);
		while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("lineName", c.getString(c.getColumnIndex("section_name"))); //�����·
			map.put("lineCode",c.getString(c.getColumnIndex("section_id")));
			arrayList.add(map);
		}	
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return arrayList;
}


/**
 * ����lineCode��ѯ��·����
 * @param lineCode ��·���� ��Ӧ��PRO_INFO section_id�ֶ�
 * @return ��·����
 */
public static String queryLineName(String lineCode){
	Cursor c=null;
	String sql=String.format("select section_name from PRO_INFO where section_id='%s'", lineCode);
	try{
		c=dbHelper.query(sql);
		if(c.moveToNext()){
			return c.getString(0);
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return "";
}

/**
 * ������·�����ѯ���
 * @param lineCode ��Ӧ��Pro_INFO �е�section_id�ֶ�
 * @return ArrayList<HashMap<String,String>>
 */

public static ArrayList<HashMap<String, String>>  queryTunnelInfo(String lineCode){
	ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=null;
	String sql=String.format("select * from TURNNEL where line_id='%s'", lineCode);
	try{
		c=dbHelper.query(sql);
		while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("id", c.getString(c.getColumnIndex("_id")));
			map.put("lineCode", c.getString(c.getColumnIndex("line_id")));				//��·����
			map.put("tunnelCode", c.getString(c.getColumnIndex("section_id")));     //�������
			map.put("tunnelName", c.getString(c.getColumnIndex("section_name")));//�������
			map.put("mileageLength",c.getString(c.getColumnIndex("up_length")));   //��̳���
			map.put("startMileage",c.getString(c.getColumnIndex("up_num")));          //��ʼ���
			map.put("endMileage", c.getString(c.getColumnIndex("down_num")));   //�������
			arrayList.add(map);
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	return arrayList;
}

/**
 * ����tunnelCode��ѯ �������
 */
public static String  queryTunnelName(String tunnelCode){
	Cursor c=null;
	String sql=String.format("select section_name from  TURNNEL where section_id='%s'", tunnelCode);
	try{
		c=dbHelper.query(sql);
		if(c.moveToNext()){
			return c.getString(0);
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		
	}
	return "";
}
}
