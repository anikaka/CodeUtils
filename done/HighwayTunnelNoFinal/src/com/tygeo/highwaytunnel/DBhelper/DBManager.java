package com.tygeo.highwaytunnel.DBhelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tygeo.highwaytunnel.R;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;



	public class DBManager {
		private final int BUFFER_SIZE = 400000;
		public static final String DB_NAME = "test1.db"; // 保存的数据库文件名
		
		
	
		public static final String DB_PATH =android.os.Environment.getExternalStorageDirectory().getAbsolutePath() ;
			//	+ PACKAGE_NAME; // 在手机里存放数据库的位置
		private SQLiteDatabase database;
		private Context context;
		
		public DBManager(Context context) {
			this.context = context;
		}
			
		public void openDatabase() {
//			this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
		}
		
//		private  SQLiteDatabase openDatabase(String dbfile) {
//			try {
////				
//				String databaseFilename=DB_PATH+"/"+DB_NAME;
//				File dir=new File(DB_PATH);
//				if(!dir.exists()){
//					dir.mkdir();
//				}
//				if(!(new File(databaseFilename)).exists()){
//					byte[] buffer=new byte[8192];
//					int count = 0;
//					InputStream is = this.context.getResources().openRawResource(
//							R.raw.test1);
//					FileOutputStream fos=new FileOutputStream(databaseFilename);
//					while ((count=is.read(buffer))>0) {
//						fos.write(buffer, 0, count);
//					}
//					fos.close();
//					is.close();
//				}
//				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
//						null);
//				return db;
//			} catch (FileNotFoundException e) {
//				Log.e("Database", "File not found");
//				e.printStackTrace();
//			} catch (IOException e) {
//				Log.e("Database", "IO exception");
//				e.printStackTrace();
//			}
//			return null;
//		}
		public  void sd(){
	     String dbDirPath = "/data/data/com.tygeo.highwaytunnel/databases";
         File dbDir = new File(dbDirPath);
         
         try {if (!dbDir.exists() || !dbDir.isDirectory()) {
             dbDir.mkdir();
         
           InputStream is = this.context.getResources().openRawResource(R.raw.test1);//这就是你所说的外部数据库
           FileOutputStream os = new FileOutputStream(dbDirPath+"/test1.db");
           byte[] buffer = new byte[1024];
           int count = 0;
           // 将静态数据库文件拷贝到目的地
           while ((count = is.read(buffer)) > 0) {
             os.write(buffer, 0, count);
           }
           is.close();
           os.close();
         }
		} catch (Exception e) {
		}
}
		public boolean  local(){
			boolean flag= false;
		     String dbDirPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/TYcache";
	         File dbDir = new File(dbDirPath);
	         
	         try {if (!dbDir.exists() || !dbDir.isDirectory()) {
	           dbDir.mkdir();
	           flag=true; 
	           InputStream is = this.context.getResources().openRawResource(R.raw.localinfo);//这就是你所说的外部数据库
	           FileOutputStream os = new FileOutputStream(dbDirPath+"/local.xml");
	           byte[] buffer = new byte[1024];
	           int count = 0;
	           // 将文件拷贝到目的地
	           while ((count = is.read(buffer)) > 0) {
	             os.write(buffer, 0, count);
	           }
	           is.close();
	           os.close();
	         }
			} catch (Exception e) {
			e.printStackTrace();
			}
			return flag;
	}
		
			public  void close(){
					this.database.close();
			}
	}



