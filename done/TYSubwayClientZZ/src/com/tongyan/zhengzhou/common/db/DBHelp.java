package com.tongyan.zhengzhou.common.db;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


public class DBHelp {

	public SQLiteDatabase db;
	private String dbPath = Environment.getExternalStorageDirectory().getPath()+ "/";
	private String dbFile = dbPath + "TYSubwayClientZZ.db";
	private File path = new File(dbPath);
	private File file = new File(dbFile);

	public DBHelp() {
		if (!path.exists()) {
			path.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
				db = SQLiteDatabase.openOrCreateDatabase(file, null);
				onCreate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
	}

	public void onCreate() {
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
		String create_MetroCheckObject = "CREATE TABLE MetroCheckObject (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,MetroLineId integer,CheckObjectCode TEXT,TermPartId integer,CheckObjectName TEXT,CheckObjectType integer,SerialCode TEXT,StartStationCode TEXT,EndStationCode TEXT);";
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
		String create_WebService = "CREATE TABLE WebService (ID integer NOT NULL PRIMARY KEY, WebServiceName TEXT, WebServiceUpdateTime TEXT );";
		db.execSQL(drop_WebService);
		db.execSQL(create_WebService);
		
		/** 违规视频信息  */
		String drop_IllegalVedioFiles = "DROP TABLE IF EXISTS IllegalVedioFiles;";
		String create_IllegalVedioFiles = "CREATE TABLE IllegalVedioFiles (ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,IllegalID TEXT,FilePath TEXT,FileRemotePath TEXT);";
		db.execSQL(drop_IllegalVedioFiles);
		db.execSQL(create_IllegalVedioFiles);
		
		/** 字典表 */
		String drop_z_ConstructionCheckDic = "DROP TABLE IF EXISTS z_ConstructionCheckDic;";
		String create_z_ConstructionCheckDic = "CREATE TABLE [z_ConstructionCheckDic] ([ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL, [ParamId] integer, [ParamType] varchar (50), [PDicId] integer, [ParamName] VARCHAR (50), [ParamValue] varchar (20), [Remark] varchar (50));";
		db.execSQL(drop_z_ConstructionCheckDic);
		db.execSQL(create_z_ConstructionCheckDic);
		
	}


	public boolean insert(ContentValues values, String table_name) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.beginTransaction();
		Long rowId = db.insert(table_name, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		if (rowId == -1) {
			return false;
		}
		return true;
	}
	

	public Long getIdInsert(ContentValues values, String table_name) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.beginTransaction();
		Long rowId = db.insert(table_name, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		if (rowId == -1) {
			return 0L;
		}
		return rowId;
	}

	public Cursor query(String tableName) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db
				.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryByParam(String table_name, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.query(table_name, null, null, null, null, null, null,
				null);
		return c;
	}

	public Cursor queryBySql(String sql, String[] params) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.rawQuery(sql, params);
		return c;
	}

	public void del(String table_name, String id) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.delete(table_name, "_id=?", new String[] { id });
	}

	public Cursor count(String tableName) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public void del(String tableName) {
		if(db == null){
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.delete(tableName, null, null);
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}

	public SQLiteDatabase getReadableDatabase() {
		return db;
	}

	public SQLiteDatabase getWritableDatabase() {
		return db;
	}

}
