package com.tongyan.zhengzhou.common.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LineInfoDBService {

	
	private final static String TAG = "LineInfoDBService";
	private DBHelp dbHelper;
	private Context mContext;

	public LineInfoDBService(Context context) {
		mContext = context;
		//this.dbHelper = new DBHelper(context);
		dbHelper = new DBHelp();
	}
	
	public void closeDB() {
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

	public DBHelp getDbHelper() {
		return dbHelper;
	}
	
	
	/**
	  * 获取车站的基础信息
	  * @param mCheckObjectCode 检查对象编号（车站编号）
	  * @return 返回车站的相关信息
	  */
	public ArrayList<HashMap<String, String>> getStationInfo(String mCheckObjectCode){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		String sql="SELECT mf.FacilityName, mf.FacilityCode, mf.StartMileage, mf.EndMileage FROM " +
					"MetroCheckObjectDetail mcod, MetroFacility mf  where mf.CheckObjectDetailCode=" +
					"mcod.CheckObjectDetailCode AND mcod.CheckObjectCode = '"+mCheckObjectCode+"'";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			int i = 0;
			while(cursor.moveToNext()){
				
				if(i!=0){
					HashMap<String, String> map0=new HashMap<String, String>();
					map0.put(" ", cursor.getString(cursor.getColumnIndex(" ")));
					arrayList.add(map0);
				}
				
				HashMap<String, String> map1=new HashMap<String, String>();
				String mFacilityName = cursor.getString(cursor.getColumnIndex("FacilityName"));
				if(mFacilityName == null || "null".equals(mFacilityName)){
					mFacilityName = " ";
				}
				map1.put("信息类型", mFacilityName);
				arrayList.add(map1);
				
				HashMap<String, String> map2=new HashMap<String, String>();
				String mFacilityCode = cursor.getString(cursor.getColumnIndex("FacilityCode"));
				if(mFacilityCode == null || "null".equals(mFacilityCode)){
					mFacilityCode = " ";
				}
				map2.put("编号", mFacilityCode);
				arrayList.add(map2);
				
				HashMap<String, String> map3=new HashMap<String, String>();
				String mStartMileage = cursor.getString(cursor.getColumnIndex("StartMileage"));
				if(mStartMileage == null || "null".equals(mStartMileage)){
					mStartMileage = " ";
				}
				map3.put("起始里程", mStartMileage);
				arrayList.add(map3);
				
				HashMap<String, String> map4=new HashMap<String, String>();
				String mEndMileage = cursor.getString(cursor.getColumnIndex("EndMileage"));
				if(mEndMileage == null || "null".equals(mEndMileage)){
					mEndMileage = " ";
				}
				map4.put("终止里程", mEndMileage);
				arrayList.add(map4);
				i++;
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	 }
}
