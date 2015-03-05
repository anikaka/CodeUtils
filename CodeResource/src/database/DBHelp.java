package database;

import java.io.File;

import util.Constants;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelp {

	private File mDBFile=new File(Constants.DB_PATH);
	private SQLiteDatabase  db=null;
	
	public DBHelp() {
		if(!mDBFile.exists()){
			try{				
				mDBFile.createNewFile();
				onCreate();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			db=SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		}
	}
	
	public void onCreate(){
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
	}
	
	
	public boolean insert(ContentValues values, String table_name) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		db.beginTransaction();
		Long rowId = db.insert(table_name, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return false;
		}
		return true;
	}
	

	public Long getIdInsert(ContentValues values, String table_name) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		db.beginTransaction();
		Long rowId = db.insert(table_name, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return 0L;
		}
		return rowId;
	}

	public Cursor query(String tableName) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		Cursor c = db
				.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryByParam(String table_name, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		Cursor c = db.query(table_name, null, null, null, null, null, null,
				null);
		return c;
	}

	public Cursor queryBySql(String sql, String[] params) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		Cursor c = db.rawQuery(sql, params);
		return c;
	}

	public void del(String table_name, String id) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		db.delete(table_name, "_id=?", new String[] { id });
	}

	public Cursor count(String tableName) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public void del(String tableName) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDBFile, null);
		db.delete(tableName, null, null);
		if (db != null) {
			db.close();
		}
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}
	
	public  SQLiteDatabase getSQLiteDatabase(){
		return db;
	}
}
