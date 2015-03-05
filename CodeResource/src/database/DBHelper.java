package database;

import util.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{


	public DBHelper(Context context){
		super(context, Constants.DB_PATH, null, 4);
		System.out.println(Constants.DB_PATH);
	}
	
//	public DBHelper(Context context, String name, CursorFactory factory,
//			int version) {
//		super(context, name, factory, version);
//	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("数据库正在创建");
		String  sqlUser="create table  user(_id  INTEGER PRIMARY KEY AUTOINCREMENT)";
		db.execSQL(sqlUser);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
