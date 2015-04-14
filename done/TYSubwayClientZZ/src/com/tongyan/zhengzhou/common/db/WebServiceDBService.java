package com.tongyan.zhengzhou.common.db;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WebServiceDBService {

	/**
	 * @Title: WebServiceDBService.java 
	 * @author 王贺龙
	 * @date 2015-03-09 
	 * @version V2.0z
	 * @Description:
	 */
	private final static String TAG = "WebServiceDBService";
	private DBHelp dbHelper;

	public WebServiceDBService(Context context) {
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
	  *	获取本地接口更新的时间
	  * @param WebServiceName 接口名称
	  * @return String 接口更新时间
	  **/
	public String getUpdateTime(String webServiceName) {
		 
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "SELECT * FROM WebService WHERE WebServiceName = '"+webServiceName+"'";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			String upDateStr = "";
			if(cursor.moveToNext()) {
				upDateStr = cursor.getString(cursor.getColumnIndex("WebServiceUpdateTime"));
			}
			return upDateStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDB();
		}
	}
	/**
	 * 获取本地更新时间
	 * @return
	 */
	public String getLastUpdateTime(){
		String time = "";
		String sql = "SELECT * FROM WebService WHERE WebServiceName = '"+"LastLocalTime"+"'";
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToNext()){
				time = c.getString(c.getColumnIndex("WebServiceUpdateTime"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c != null){
				c.close();
				c = null;
			}
			closeDB();
		}
		return time;
	}
	
	
	/**
	  *	更新接口的更新时间
	  * @param WebServiceName 接口名称
	  * @param updateTime 接口更新时间
	  **/
	public void setUpdateTime(String webServiceName, String updateTime) {
		String selectSql = "SELECT * FROM WebService WHERE WebServiceName = '"+webServiceName+"'";
		Cursor c = null;
		try {
			try{
				c = dbHelper.getReadableDatabase().rawQuery(selectSql, null);
				if(c.moveToFirst()){
					String sql = "DELETE from WebService WHERE WebServiceName = '"+webServiceName+"'";
					dbHelper.getWritableDatabase().execSQL(sql);
				}
			}finally{
				if(c != null){
					c.close();
					c = null;
				}
			}
			ContentValues values = new ContentValues();
			values.put("WebServiceName", webServiceName);
			values.put("WebServiceUpdateTime", updateTime);
			dbHelper.insert(values, "WebService");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}
	/**
	 * 修改本地更新时间
	 * @param updateTime
	 */
	public void setLocalUpdateTime( String updateTime){
		String sql = "SELECT * FROM WebService WHERE WebServiceName = '"+"LastLocalTime"+"'";
		Cursor c = null;
		try{
			try{
				c = dbHelper.getReadableDatabase().rawQuery(sql, null);
				if(c.moveToFirst()){
					String delSql = "DELETE FROM WebService  WHERE WebServiceName = '"+"LastLocalTime"+"'";
					dbHelper.getReadableDatabase().execSQL(delSql);
				}
			}finally{
				if(c != null){
					c.close();
					c = null;
				}
			}
			ContentValues values = new ContentValues();
			values.put("WebServiceName", "LastLocalTime");
			values.put("WebServiceUpdateTime", updateTime);
			dbHelper.insert(values, "WebService");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeDB();
		}
	}
	
	
	/**
	 *	保存病害类型基础数据库
	 * @param tableName 需要保存的表名
	 * @param tablelist 需要保存的数据
	 **/
	public boolean saveDBInfo(String tableName, ArrayList<HashMap<String, String>> tablelist) {
		 
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor c = null;
		int errorNum = 0;
		try {
			if(tablelist != null && tablelist.size() > 0) {
				for(int i=0; i<tablelist.size(); i++) {
					HashMap<String, String> tableMap = tablelist.get(i);
					ContentValues values = new ContentValues();
					Iterator iterator = tableMap.entrySet().iterator();
					while (iterator.hasNext()) {
						HashMap.Entry entry = (HashMap.Entry) iterator.next();
						String key = entry.getKey().toString();
						String value = entry.getValue().toString();
						if("CheckObjectType".equals(key)) {
							if("区间".equals(value)) {
								value = "1";
							}
							if("车站".equals(value)) {
								value = "3";
							}
							if("风井".equals(value)) {
								value = "2";
							}
						}
						values.put(key, value);
					}
					if(db.insert(tableName ,null,values) == -1){
						errorNum++;
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
			closeDB();
		}
		return false;
	}
	
	/**
	 *	清空表中的数据
	 * @param tableName 需要保存的表名
	 **/
	public boolean clearDBTable(String tableName) {
		 
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.execSQL("delete from "+tableName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDB();
		}
	}
	 
}
