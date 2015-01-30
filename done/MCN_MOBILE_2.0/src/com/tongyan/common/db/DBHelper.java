package com.tongyan.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ClassName DBHelper 
 * @author wanghb
 * @date 2013-7-16 am 09:28:20
 * @desc 
 */
public class DBHelper extends SQLiteOpenHelper{
	
	public DBHelper(Context context) {
		super(context,"cnOA.db", null, 4);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		/**1. 用户表 */
		String drop_localUser = "DROP TABLE IF EXISTS LOCAL_USER;";
		String create_localUser = "CREATE TABLE LOCAL_USER(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "USERID VARCHAR(100),"//人员ID-empId
				+ "USERNAME VARCHAR(50),"//用户名
				+ "PASSWORD VARCHAR(50),"//
				+ "DPTROWID VARCHAR(100),"//部门id-dptRowId
				+ "DEPARTMENT VARCHAR(50),"//部门名称
				+ "EMPLEVEL VARCHAR(50),"// 人员职务名称-empLevel
				+ "EMPNAME VARCHAR(50),"//人员名称-empName
				+ "LASTTIME DATETIME DEFAULT CURRENT_TIMESTAMP,"
				+ "SAVEPASSWORD INTEGER DEFAULT 0,"
				+ "AUTOLOGIN INTEGER DEFAULT 0,"
				+ "PHONE VARCHAR(20),"
				+ "NICKNAME VARCHAR(50)," + "EMAIL VARCHAR(50)," +
				"BIRTHDAY TEXT," +
				"SEX INTEGER DEFAULT 2," +//未知
				"IsUpdate INTEGER DEFAULT 0," +//每次登录都更新
				"IsGps INTEGER DEFAULT 0," +//开启GPS
				"ContactsTime TEXT," +//
				"ItemSecTime TEXT," +//
				"ProjectTime TEXT," +//
				"RiskTypeTime TEXT," +//
				"ADDRESS TEXT" + ")";
		db.execSQL(drop_localUser);
		db.execSQL(create_localUser);
		
		/**2.工程表*/
		String drop_project = "DROP TABLE IF EXISTS LOCAL_PROJECT;";
		String create_project = "CREATE TABLE LOCAL_PROJECT(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "rowId VARCHAR(100),"//project id
			+ "aName VARCHAR(50),"//工程名称
			+ "aPid VARCHAR(50),"//
			+ "aSecid VARCHAR(50),"//标段id
			+ "aType VARCHAR(50),"//类型
//			+ "aSlng VARCHAR(50),"// 开始经度
//			+ "aSlat VARCHAR(50),"//开始纬度
//			+ "aElng VARCHAR(50),"//结束经度
//			+ "aElat VARCHAR(50),"//结束纬度
			+ "aPmName VARCHAR(50)," //项目经理
			+ "aPContract VARCHAR(50)," //联系方式
			+ "aConstruct VARCHAR(100)," + //施工单位
			"aStartMile VARCHAR(50)," + //起始里程
			"aEndMile VARCHAR(50)," + //结束里程
			"aPositionMile VARCHAR(100)," +
			 "aPosition VARCHAR(100)" +
			 ")";
		db.execSQL(drop_project);
		db.execSQL(create_project);
		
		/** 3.项目表 */
		String drop_item_section = "DROP TABLE IF EXISTS LOCAL_ITEM_SECTION;";
		String create_item_section = "CREATE TABLE LOCAL_ITEM_SECTION(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "iid VARCHAR(100),"//item id
			+ "iContent VARCHAR(50),"//项目内容
			+ "iAttributes VARCHAR(500),"//
			+ "sid VARCHAR(100),"//section id
			+ "sContent VARCHAR(50),"//标段内容
			+ "sAttributes VARCHAR(500),"//
			+ "IsUpdate INTEGER DEFAULT 0,"//
			+ "UpdateTime VARCHAR(50)"//
			+ ")";
		db.execSQL(drop_item_section);
		db.execSQL(create_item_section);
		
		/**4. 通讯录表  */
		String drop_contacts = "DROP TABLE IF EXISTS LOCAL_CONTACTS;";
		String create_contacts = "CREATE TABLE LOCAL_CONTACTS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "dptName VARCHAR(100),"
			+ "dptTel VARCHAR(50),"
			+ "dptFax VARCHAR(50),"
			+ "empName VARCHAR(50),"
			+ "empContact VARCHAR(50),"
			+ "empPinyin VARCHAR(50),"
			+ "dptPinyin VARCHAR(100),"
			+ "rowId VARCHAR(100)" + ")";
		db.execSQL(drop_contacts);
		db.execSQL(create_contacts);
		
		/**5. 现场检查表   */
		String drop_check = "DROP TABLE IF EXISTS LOCAL_CHECK;";
		String create_check = "CREATE TABLE LOCAL_CHECK(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "isUpdate INTEGER DEFAULT 0,"//0,1,2
			+ "aProjectId VARCHAR(100),"//project id
			+ "checkContent VARCHAR(500),"
			+ "upTime DATETIME DEFAULT CURRENT_TIMESTAMP," + 
			"inTime DATETIME DEFAULT CURRENT_TIMESTAMP," + 
			"aStartMile VARCHAR(50)," +//起始里程
			"aEndMile VARCHAR(50)," +//结束里程
			"aProName VARCHAR(50)," +//
			"aSecName TEXT," +
			"aItemName TEXT," +
			"aItemId TEXT," +
			"aSecId TEXT," +
			"checkId VARCHAR(100)" +
			")";
		db.execSQL(drop_check);
		db.execSQL(create_check);
		/** 6.图片地址表   */
		String drop_photos = "DROP TABLE IF EXISTS LOCAL_PHOTOS;";
		String create_photos = "CREATE TABLE LOCAL_PHOTOS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "check_tab_id INTEGER,"
			+ "checkId VARCHAR(100),"//
			+ "local_img_path TEXT,"
			+ "remote_img_path TEXT,"
			+ "remote_img_id TEXT" + 
			")";
		db.execSQL(drop_photos);
		db.execSQL(create_photos);
		
		/** 7.定位数据表 */
		String drop_loc = "DROP TABLE IF EXISTS LOCAL_LOC;";
		String create_loc = "CREATE TABLE LOCAL_LOC(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "userid TEXT,"
			+ "params TEXT" +
			")";
		db.execSQL(drop_loc);
		db.execSQL(create_loc);
		/** 8.风险检查表 */
		String drop_risk = "DROP TABLE IF EXISTS LOCAL_RISK;";
		String create_risk = "CREATE TABLE LOCAL_RISK(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "rowId TEXT,"//uuid
			+ "userId TEXT,"
			+ "iid TEXT,"//item id
			+ "iContent TEXT,"//项目内容
			+ "sid TEXT,"//section id
			+ "sContent TEXT,"//标段内容
			+ "prowid TEXT," 
			+ "pContent TEXT,"
			+ "currDate TEXT,"
			+ "riskDegree TEXT,"
			+ "isFinish INTEGER DEFAULT 0"
			//+ "riskName TEXT"
			+ ")";
		db.execSQL(drop_risk);
		db.execSQL(create_risk);
		
		/** 9.掌子面表 */
		String drop_holo_face = "DROP TABLE IF EXISTS LOCAL_HOLO_FACE;";
		String create_holo_face = "CREATE TABLE LOCAL_HOLO_FACE(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "risk_id TEXT,"//risk id
			+ "isFinish INTEGER DEFAULT 0,"
			+ "hole TEXT,"//洞口
			+ "proposeDegree TEXT,"//建议等级
			+ "riskDegree TEXT,"//风险等级
			+ "riskHSuggest TEXT,"//风险处理意见
			+ "currMile TEXT"
			+ ")";
		db.execSQL(drop_holo_face);
		db.execSQL(create_holo_face);
		
		/** 12.掌子面风险配置 */
		String drop_holo_face_check = "DROP TABLE IF EXISTS LOCAL_HOLO_SETTING_INFO;";
		String create_holo_face_chck = "CREATE TABLE LOCAL_HOLO_SETTING_INFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "holeface_id TEXT,"//hole_face id
			+ "risk_id TEXT,"//risk id
			+ "risk_hole TEXT,"
			+ "isCheck INTEGER DEFAULT 0,"
			+ "risk_type_name TEXT"
			+ ")";
		db.execSQL(drop_holo_face_check);
		db.execSQL(create_holo_face_chck);
		
		/** 10.掌子面图片地址表   */
		String drop_face_photos = "DROP TABLE IF EXISTS LOCAL_FACE_PHOTOS;";
		String create_face_photos = "CREATE TABLE LOCAL_FACE_PHOTOS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "riskId INTEGER,"
			+ "holeface_tab_id INTEGER,"
			+ "riskUUID TEXT,"
			+ "local_img_path TEXT,"
			+ "remote_img_path TEXT,"
			+ "remote_img_id TEXT" + 
			")";
		db.execSQL(drop_face_photos);
		db.execSQL(create_face_photos);
		
		/** 11.掌子面表-风险配置数据表 */
		String drop_risk_settings = "DROP TABLE IF EXISTS LOCAL_RISK_SETTINGS;";
		String create_risk_settings = "CREATE TABLE LOCAL_RISK_SETTINGS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "cId TEXT,"
			+ "oneType TEXT,"
			+ "twoType TEXT,"
			+ "class1 TEXT," 
			+ "class2 TEXT," 
			+ "isChild TEXT," 
			+ "class2Tip TEXT," + 
			  "riskType TEXT" +
			")";
		db.execSQL(drop_risk_settings);
		db.execSQL(create_risk_settings);
		
		/** 13.掌子面风险配置具体信息表  */
		String drop_risk_settings_record = "DROP TABLE IF EXISTS LOCAL_RISK_SETTINGS_RECORD;";
		String create_risk_settings_record = "CREATE TABLE LOCAL_RISK_SETTINGS_RECORD(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "riskId TEXT,"
			+ "holefaceId TEXT,"
			+ "holefaceSettingInfoId TEXT,"
			+ "rowId TEXT,"//LOCAL_RISK_SETTINGS rowId
			+ "subId TEXT,"
			+ "oneType TEXT,"
			+ "selectedType TEXT," 
			+ "typeNum TEXT," 
			+ "currentState TEXT,"
			+ "riskType TEXT" +
			")";
		db.execSQL(drop_risk_settings_record);
		db.execSQL(create_risk_settings_record);
		
		/** 14.GPS定位 拍照图片相关数据存储表 */
		String drop_gps_photos = "DROP TABLE IF EXISTS LOCAL_GPS_PHOTOS;";
		String create_gps_photos = "CREATE TABLE LOCAL_GPS_PHOTOS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "section_id TEXT,"
			+ "new_id TEXT,"
			+ "photo_name TEXT,"
			+ "local_img_path TEXT,"
			+ "remote_img_path TEXT,"
			+ "remote_img_id TEXT," 
			+ "state INTEGER DEFAULT 0" +//0:未上传，1：已上传，2：上传失败
			")";
		
		db.execSQL(drop_gps_photos);
		db.execSQL(create_gps_photos);
		
		/** 15.工程编号采点 表   如果已上传，将删除该笔记录*/
		String drop_collect_points = "DROP TABLE IF EXISTS LOCAL_PROJECT_POINTS;";
		String create_collect_points = "CREATE TABLE LOCAL_PROJECT_POINTS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "project_code_id TEXT,"
			+ "content TEXT)";
		db.execSQL(drop_collect_points);
		db.execSQL(create_collect_points);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			if(!checkColumnIsExist(db, "LOCAL_ITEM_SECTION", "IsUpdate")) {
				db.execSQL("ALTER TABLE LOCAL_ITEM_SECTION ADD IsUpdate INTEGER DEFAULT 0;");
			}
			
			if(!checkColumnIsExist(db, "LOCAL_ITEM_SECTION", "UpdateTime")) {
				db.execSQL("ALTER TABLE LOCAL_ITEM_SECTION ADD UpdateTime VARCHAR(50);");
			}
			
			if(!tabIsExist(db, "LOCAL_GPS_PHOTOS")) {
				String drop_gps_photos = "DROP TABLE IF EXISTS LOCAL_GPS_PHOTOS;";
				String create_gps_photos = "CREATE TABLE LOCAL_GPS_PHOTOS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "section_id TEXT,"
					+ "new_id TEXT,"
					+ "photo_name TEXT,"
					+ "local_img_path TEXT,"
					+ "remote_img_path TEXT,"
					+ "remote_img_id TEXT," 
					+ "state INTEGER DEFAULT 0" +//0:未上传，1：已上传，2：上传失败
					")";
				db.execSQL(drop_gps_photos);
				db.execSQL(create_gps_photos);
			}
			
			if(!tabIsExist(db, "LOCAL_PROJECT_POINTS")) {
				String drop_collect_points = "DROP TABLE IF EXISTS LOCAL_PROJECT_POINTS;";
				String create_collect_points = "CREATE TABLE LOCAL_PROJECT_POINTS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "project_code_id TEXT,"
					+ "content TEXT)";
				db.execSQL(drop_collect_points);
				db.execSQL(create_collect_points);
			}
		} catch(Exception e) {
			
		}
	}
	
	
	
	/**
	* 方法1：检查某表列是否存在
	* @param db
	* @param tableName 表名
	* @param columnName 列名
	* @return 根据 cursor.getColumnIndex(String columnName) 的返回值判断，如果为-1表示表中无此字段
	*/
	public boolean checkColumnIsExist(SQLiteDatabase db,String tableName , String columnName) {
	    boolean result = false ;
	    Cursor cursor = null ;
	    try{
	        //查询一行
	        cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0", null );
	        result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
	    }catch (Exception e){
	    }finally{
	        if(null != cursor && !cursor.isClosed()){
	            cursor.close() ;
	        }
	    }
	    return result ;
	}
	/**
	 * 判断表是否存在
	 * @param db
	 * @param tabName
	 * @return
	 */
	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='"+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	
	
	public boolean insert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		Long rowId = db.insert(tableName, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return false;
		}
		return true;
	}
	
	public Long getIdInsert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		Long rowId = db.insert(tableName, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return 0L;
		}
		return rowId;
	}
	
	public Cursor query(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}
	public Cursor count(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}
	public Cursor queryByParam(String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryBySql(String sql, String[] params) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(sql, params);
		return c;
	}

	public void del(String tableName, String id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, "_id=?", new String[] {id});
	}
	
	/*public int count(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c.getCount();
	}*/
	public void del(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, null, null);
	}
	
}
