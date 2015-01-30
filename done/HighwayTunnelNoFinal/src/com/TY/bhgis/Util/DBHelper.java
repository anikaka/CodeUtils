package com.TY.bhgis.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {
	private String DataSource;
	private SQLiteDatabase mSqLiteDatabase = null;

	public DBHelper(String DataSource) {
		this.DataSource = DataSource;

	}

	private synchronized  SQLiteDatabase getWritableDatabase() {
		if (mSqLiteDatabase == null || !mSqLiteDatabase.isOpen()
				|| mSqLiteDatabase.getPath() != this.DataSource) {
			mSqLiteDatabase = SQLiteDatabase.openDatabase(this.DataSource,
					null, SQLiteDatabase.OPEN_READWRITE);
		}
		return mSqLiteDatabase;

	}
	private synchronized  SQLiteDatabase getReadOnlyDatabase() {
		if (mSqLiteDatabase == null || !mSqLiteDatabase.isOpen()
				|| mSqLiteDatabase.getPath() != this.DataSource) {

				mSqLiteDatabase = SQLiteDatabase.openDatabase(this.DataSource,
						null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
		return mSqLiteDatabase;

	}

	public Cursor query(String sql) {
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try {
			sqLiteDatabase = getReadOnlyDatabase();
			cursor = sqLiteDatabase.rawQuery(sql, null);
		} catch (SQLException e) {
			Log.v("异常", e.getMessage());
			sqLiteDatabase.close();
		}
		return cursor;
	}

	public void execSql(String sql) {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = getWritableDatabase();
			
			sqLiteDatabase.execSQL(sql);
		} catch (SQLException e) {
			Log.v("异常", e.getMessage());

		} finally {
			close();
		}

	}

	public void insert(String tablename, ContentValues initValues) {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = getWritableDatabase();
			sqLiteDatabase.insert(tablename, null, initValues);
		} catch (SQLException e) {
			Log.v("异常", e.getMessage());

		} finally {
			close();
		}
	}

	public void update(String tablename, ContentValues initValues,
			String whereClause, String[] whereArgs) {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = getWritableDatabase();
			sqLiteDatabase
					.update(tablename, initValues, whereClause, whereArgs);
		} catch (SQLException e) {
			Log.v("异常", e.getMessage());

		} finally {
			close();
		}
	}
	public void delete(String tablename,
			String whereClause, String[] whereArgs) {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = getWritableDatabase();
			sqLiteDatabase
					.delete(tablename, whereClause, whereArgs);
		} catch (SQLException e) {
			Log.v("异常", e.getMessage());

		} finally {
			close();
		}
	}
	public void close() {
		if (mSqLiteDatabase != null && mSqLiteDatabase.isOpen()) {
			mSqLiteDatabase.close();
		}
	}
}
