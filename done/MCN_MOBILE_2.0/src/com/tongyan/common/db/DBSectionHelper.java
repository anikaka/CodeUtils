package com.tongyan.common.db;

import java.io.File;
import java.io.IOException;

import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 
 * @className DBSectionHelp
 * @author wanghb
 * @date 2014-7-21 PM 08:15:24
 * @Desc TODO
 */
public class DBSectionHelper {
	
	public final static String dbPath = FileUtils.getSDCardPath()+ Constansts.CN_DB_PATH;
	public final static String dbFile = dbPath + Constansts.CN_SECTION_DB_NAME;
	private File path = new File(dbPath);
	private File file = new File(dbFile);
	
	
	private boolean mIsInitializing = false;
	public SQLiteDatabase db = null;
	
	
	public DBSectionHelper() {
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
		/*int version = sqlite.getVersion();
		if (version != Constants.NEW_DB_VERSION) {
			try {
				sqlite.beginTransaction();
				onUpgrade(sqlite, version, Constants.NEW_DB_VERSION);
				sqlite.setVersion(Constants.NEW_DB_VERSION);
				sqlite.setTransactionSuccessful();
			} finally {
				sqlite.endTransaction();
			}
		}*/
	}
	private static DBSectionHelper mDBSectionHelper;
	
	public synchronized static DBSectionHelper getInstance() {
		if(mDBSectionHelper == null) {
			mDBSectionHelper = new DBSectionHelper();
		} else {
			if(mDBSectionHelper.db == null || !mDBSectionHelper.db.isOpen()) {
				mDBSectionHelper = new DBSectionHelper();
			}
		}
		return mDBSectionHelper;
	}
	
	
	public void onCreate() {
		String dropSectionSql = "DROP TABLE IF EXISTS SectionBaseInfo;";
		String createSectionSql = "CREATE TABLE SectionBaseInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			"SectionId VARCHAR(100)," + 
			"RowId VARCHAR(100)," + 
			"Allcode TEXT," + 
			"AllName TEXT," + 
			"ParentId VARCHAR(100))";
		db.execSQL(dropSectionSql);
		db.execSQL(createSectionSql);
	}
	public boolean insert(ContentValues values, String table_name) {
		//SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
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
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
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
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryByParam(String table_name, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.query(table_name, null, null, null, null, null, null,
				null);
		return c;
	}

	public Cursor queryBySql(String sql, String[] params) {
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.rawQuery(sql, params);
		return c;
	}

	public void del(String table_name, String id) {
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.delete(table_name, "_id=?", new String[] { id });
	}

	public Cursor count(String tableName) {
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		Cursor c = db.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public void del(String tableName) {
		if(!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
		db.delete(tableName, null, null);
		if (db != null) {
			db.close();
		}
	}

	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	/***
	 * Create and/or open a database that will be used for reading and writing.
	 * Once opened successfully, the database is cached, so you can call this
	 * method every time you need to write to the database. Make sure to call
	 * {@link #close} when you no longer need it.
	 * 
	 * <p>
	 * Errors such as bad permissions or a full disk may cause this operation to
	 * fail, but future attempts may succeed if the problem is fixed.
	 * </p>
	 * 
	 * @throws SQLiteException
	 *             if the database cannot be opened for writing
	 * @return a read/write database object valid until {@link #close} is called
	 */
	public synchronized SQLiteDatabase getReadableDatabase() {
		if (db != null && db.isOpen() && !db.isReadOnly()) {
			return db; // The database is already open for business
		}
		
		if (mIsInitializing) {
			throw new IllegalStateException(
					"getWritableDatabase called recursively");
		}
		
		return db;
	}

	public SQLiteDatabase getWritableDatabase() {
		
		return db;
	}
	
}
