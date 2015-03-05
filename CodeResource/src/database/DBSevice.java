package database;

import android.content.Context;

public class DBSevice {

	private DBHelp db;
	private Context mContext;
	
	public DBSevice(Context context){
		this.mContext=context;
		 db=new DBHelp();
	}
	
	
	
}
