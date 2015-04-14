package com.tongyan.zhengzhou.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @Title: DBHelper.java 
 * @author Rubert
 * @date 2015-3-4 AM 11:20:49 
 * @version V1.0 
 * @Description: 数据库创建表 + 数据库更新
 */
public class DBHelper extends SQLiteOpenHelper {


	public DBHelper(Context context) {
		super(context, "TYSubwayClientZZ.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/** 例子 */
		/*String drop_holo_face = "DROP TABLE IF EXISTS LOCAL_HOLO_FACE;";
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
		db.execSQL(create_holo_face);*/
		/** 用户表  */
		String drop_User = "DROP TABLE IF EXISTS User;";
		String create_User = "CREATE TABLE User (ID integer NOT NULL PRIMARY KEY,LoginAccount TEXT,UserName TEXT,UserPassword TEXT,UserLastLoginDate DEFAULT CURRENT_TIMESTAMP,UserCode integer,SavePassword integer);";
		db.execSQL(drop_User);
		db.execSQL(create_User);
		/** 线路表  */
		String drop_MetroLine = "DROP TABLE IF EXISTS MetroLine;";
		String create_MetroLine = "CREATE TABLE MetroLine (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,MetroLineId integer,PMetroLineId integer,MetroLineName TEXT,MetroLineCode TEXT);";
		db.execSQL(drop_MetroLine);
		db.execSQL(create_MetroLine);
		/** 检查对象表  */
		String drop_MetroCheckObject = "DROP TABLE IF EXISTS MetroCheckObject;";
		String create_MetroCheckObject = "CREATE TABLE MetroCheckObject (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,MetroLineId integer,CheckObjectCode TEXT,TermPartId integer,CheckObjectName TEXT,CheckObjectType integer,SerialCode TEXT);";
		db.execSQL(drop_MetroCheckObject);
		db.execSQL(create_MetroCheckObject);
		/** 检查对象明细表  */
		String drop_MetroCheckObjectDetail = "DROP TABLE IF EXISTS MetroCheckObjectDetail;";
		String create_MetroCheckObjectDetail  = "CREATE TABLE [MetroCheckObjectDetail] ([ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL, [CheckObjectCode] TEXT, [CheckObjectDetailCode] TEXT, [LineDirection] integer, [StartMileage] Float, [EndMileage] Float, [MileageAddDirection] integer, [ArenosolArea] TEXT, [OverproofArea] TEXT, [DamagedArea] TEXT, [UpdateTime] TEXT, [CheckObjectDetailType] integer);";
		db.execSQL(drop_MetroCheckObjectDetail);
		db.execSQL(create_MetroCheckObjectDetail);
		/** 设施表  */
		String drop_MetroFacility = "DROP TABLE IF EXISTS MetroFacility;";
		String create_MetroFacility = "CREATE TABLE MetroFacility (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,CheckObjectDetailCode TEXT,FacilityCode TEXT,FacilityName TEXT,FacilityTypeCode integer,PFacilityCode TEXT,StartMileage Float,EndMileage Float,StartCircle integer,EndCircle integer,Remark TEXT,SerialNumber TEXT, CheckRecordCode TEXT, Status integer);";
		db.execSQL(drop_MetroFacility);
		db.execSQL(create_MetroFacility);
		/** 设施附属表  */
		String drop_FacilityShieldInfo = "DROP TABLE IF EXISTS FacilityShieldInfo;";
		String create_FacilityShieldInfo = "CREATE TABLE FacilityShieldInfo (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,FacilityCode TEXT,SegmentType integer,CircleWidth integer,TunnelDiameter float,HoopBolt integer,EndwiseBolt integer,FirstStationCode TEXT,SegmentsDirection TEXT,Remark TEXT);";
		db.execSQL(drop_FacilityShieldInfo);
		db.execSQL(create_FacilityShieldInfo);
		
		/** 巡查红线表  */
		String drop_RedLine = "DROP TABLE IF EXISTS RedLine;";
		String create_RedLine = "CREATE TABLE [RedLine] ([ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL, [RedlineID] TEXT, [MetroLineID] integer, [DrivingDirection] integer, [StartStation] integer, [EndStation] integer, [Shape] Text, [SectionId] INTEGER);";
		db.execSQL(drop_RedLine);
		db.execSQL(create_RedLine); 
		/** 巡查隧道表  */
		String drop_MetroTunnel = "DROP TABLE IF EXISTS MetroTunnel;";
		String create_MetroTunnel = "CREATE TABLE [MetroTunnel] ([ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL, [MetroLineID] integer, [DrivingDirection] integer, [StartStation] integer, [EndStation] integer, [Name] varchar (100), [Shape] Text, [SectionId] INTEGER);";
		db.execSQL(drop_MetroTunnel);
		db.execSQL(create_MetroTunnel); 
		/** 巡查车站表  */
		String drop_MetroStation = "DROP TABLE IF EXISTS MetroStation;";
		String create_MetroStation = "CREATE TABLE [MetroStation] ([ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL, [MetroLineID] integer, [StationID] integer, [StationName] varchar (50), [Shape] text, [SerialCode] TEXT);";
		db.execSQL(drop_MetroStation);
		db.execSQL(create_MetroStation); 
		
		/** 接口更新时间记录表  */
		String drop_WebService = "DROP TABLE IF EXISTS WebService;";
		String create_WebService = "CREATE TABLE WebService (ID integer NOT NULL PRIMARY KEY, WebServiceName TEXT, WebServiceUpdateTime TEXT);";
		db.execSQL(drop_WebService);
		db.execSQL(create_WebService);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public boolean insert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		Long rowId = db.insert(tableName, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
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
	
	public SQLiteDatabase getSQLiteDatabaseInstance(){
	return getWritableDatabase();
}
	
	
}
