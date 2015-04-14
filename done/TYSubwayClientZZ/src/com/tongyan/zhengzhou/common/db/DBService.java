package com.tongyan.zhengzhou.common.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.tongyan.zhengzhou.common.utils.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * 
 * @Title: DBService.java 
 * @author Rubert
 * @date 2015-3-4 PM 03:53:13 
 * @version V1.0 
 * @Description: TODO
 */
public class DBService {
	
	private final static String TAG = "DBService";
	//private DBHelper dbHelper;
	
	private DBHelp dbHelper; 
	private Context mContext;
	
	public DBService(Context context) {
		mContext = context;
		//this.dbHelper = new DBHelper(context);
		dbHelper = new DBHelp();
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDBHepler() {
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}
	
	
	/** 删除某表，所有数据*/
	public boolean delAll(String tabName) {
		try {
			dbHelper.del(tabName);
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
		return true;
	}
	
	
	
	/**
	 * 查找最后一条用户信息
	 * @return {@link HashMap}
	 */
	public HashMap<String, String>  queryLastUser(){
		HashMap<String, String> map=new HashMap<String, String>();
		Cursor c=null;
		try{
			c=dbHelper.query(Constants.TABLE_USER);
			if(c.moveToLast()){
				map.put("loginAccount", c.getString(c.getColumnIndex("LoginAccount")));
				map.put("userName",c.getString(c.getColumnIndex("UserName")));
				map.put("password", c.getString(c.getColumnIndex("UserPassword")));
				map.put("loginDate", c.getString(c.getColumnIndex("UserLastLoginDate")));
				map.put("userCode", c.getString(c.getColumnIndex("UserCode")));
				map.put("savePassword", c.getString(c.getColumnIndex("SavePassword")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			closeDBHepler();
		}
		return map;
	}
	

	
	/**
	 * 插入用户信息
	 * @param map 插入字段的<key,value>
	 */
	public boolean insertUser(HashMap<String, String > map){
		ContentValues values=new ContentValues();
		if(map!=null){
			for(Entry<String, String> entry:map.entrySet()){
				values.put(entry.getKey(), entry.getValue());
			}
		}
		try{
			boolean result=dbHelper.insert(values, Constants.TABLE_USER);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeDBHepler();
		}
		return false;
	}
	
	/**
	 * 获取最近登录信息
	 */
	public HashMap<String, String> getCurrentUser(int type, String name) {
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "SELECT * FROM User ORDER BY UserLastLoginDate DESC";
		if (type == 2) {
			sql = "SELECT * FROM User WHERE LoginAccount='" + name + "'";
		}
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if (c.moveToFirst()) {
				String id = c.getString(c.getColumnIndex("ID"));// 区分大小写
				String LoginAccount = c.getString(c
						.getColumnIndex("LoginAccount"));
				String UserPassword = c.getString(c
						.getColumnIndex("UserPassword"));
				String UserName = c.getString(c.getColumnIndex("UserName"));
				String savePaswrod = c.getString(c
						.getColumnIndex("SavePassword"));
				String UserCode = c.getString(c.getColumnIndex("UserCode"));
				map.put("ID", id);
				map.put("LoginAccount", LoginAccount);
				map.put("password", UserPassword);
				map.put("savePaswrod", savePaswrod);
				map.put("UserName", UserName);
				map.put("UserCode", UserCode);
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHepler();
		}
		return null;
	}
	
	/**
	 * 更新
	 * 
	 * @param mUserMap
	 * @return
	 */
	public boolean updateUser(HashMap<String, String> mUserMap) {
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			dbHelper.getReadableDatabase().execSQL(
							"UPDATE User SET UserName=?, UserPassword=?, SavePassWord=?, UserLastLoginDate=? WHERE UserCode=?",
							new Object[] { mUserMap.get("UserName"),
									mUserMap.get("UserPassword"),
									mUserMap.get("SavePassWord"), 
									date,
									mUserMap.get("UserCode") });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHepler();
		}
		return true;
	}
	
	public boolean insertPicPath(String illegalID,String vedioPath,String remotePath){
		ContentValues values=new ContentValues();
		values.put("IllegalID", illegalID);
		values.put("FilePath", vedioPath);
		values.put("FileRemotePath", remotePath);
		boolean result = false;
		try{
			result=dbHelper.insert(values, "IllegalVedioFiles");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeDBHepler();
		}
		return result;
	}
	/**
	 * 获取MetroLineId
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getMetroLineID(){
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		String sql = "SELECT * FROM MetroLine WHERE MetroLineId = '97'";
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("MetroLineId", c.getString(c.getColumnIndex("MetroLineId")));
				list.add(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(c != null){
				c.close();
			}
			closeDBHepler();
		}
		return list;
	}
	
	/**
	 * 基础车站数据
	 * 
	 * @param map
	 * @return
	 */
	public boolean insertStationBaseInfo(HashMap<String, String> map) {
		Cursor c = null;
		try {
			c = dbHelper
					.getReadableDatabase()
					.rawQuery(
							String.format(
									"SELECT COUNT(1) FROM MetroStation WHERE StationID=%s",
									map.get("StationID")), null);
			if (c.moveToFirst()) {
				dbHelper.getReadableDatabase().execSQL(String.format("DELETE FROM MetroStation WHERE StationID=%s",map.get("StationID")));
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		ContentValues values = new ContentValues();
		values.put("MetroLineID", map.get("MetroLineID"));
		values.put("StationID", map.get("StationID"));
		values.put("StationName", map.get("StationName"));
		values.put("SerialCode", map.get("SerialCode"));
		values.put("Shape", map.get("Shape"));
		try {
			if (dbHelper.insert(values, "MetroStation")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			closeDBHepler();
		}
	}
	/**
	 * 基础红线数据
	 * 
	 * @param map
	 * @return
	 */
	public boolean insertRedLineBaseInfo(HashMap<String, String> map) {
		Cursor c = null;
		try {
			c = dbHelper
					.getReadableDatabase()
					.rawQuery(String.format("SELECT COUNT(1) FROM RedLine WHERE RedlineID='%s'",
									map.get("RedlineID")), null);
			if (c.moveToFirst()) {
				dbHelper.getReadableDatabase().execSQL(String.format("DELETE FROM RedLine WHERE RedlineID='%s'",map.get("RedlineID")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}

		ContentValues values = new ContentValues();
		values.put("RedlineID", map.get("RedlineID"));
		values.put("MetroLineID", map.get("MetroLineID"));
		values.put("DrivingDirection", map.get("DrivingDirection"));
		values.put("StartStation", map.get("StartStation"));
		values.put("EndStation", map.get("EndStation"));
		values.put("Shape", map.get("Shape"));
		values.put("SectionId", map.get("SectionId"));
		try {
			if (dbHelper.insert(values, "RedLine")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHepler();
		}
	}
	
	
	/**
	 * 基础线路数据
	 * 
	 * @param map
	 * @return
	 */
	public boolean insertTunnelBaseInfo(HashMap<String, String> map) {
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					String.format("SELECT COUNT(1) FROM MetroTunnel WHERE StartStation=%s AND EndStation =%s AND Name='%s' AND DrivingDirection='%s'", map.get("StartStation"), map.get("EndStation"), map.get("Name"), map.get("DrivingDirection")), null);
			if (c.moveToFirst()) {
				dbHelper.getReadableDatabase().execSQL(String.format("DELETE FROM MetroTunnel WHERE StartStation=%s AND EndStation =%s AND Name='%s' AND DrivingDirection='%s'", map.get("StartStation"), map.get("EndStation"), map.get("Name"), map.get("DrivingDirection")));
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}

		ContentValues values = new ContentValues();
		values.put("MetroLineID", map.get("MetroLineID"));
		values.put("DrivingDirection", map.get("DrivingDirection"));
		values.put("StartStation", map.get("StartStation"));
		values.put("EndStation", map.get("EndStation"));
		values.put("Name", map.get("Name"));
		values.put("Shape", map.get("Shape"));
		values.put("SectionId", map.get("SectionId"));
		try {
			if (dbHelper.insert(values, "MetroTunnel")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			closeDBHepler();
		}
	}
	
/**
 * 判断用户名是否已经存在数据库
 * @return true 存在,otherwise return false
 */
	public boolean  userIsExist(String loginAccount){
		Cursor c=null;	  
		try{
			c=dbHelper.queryByParam(Constants.TABLE_USER, new String[]{"UserPassword"}, "LoginAccount=?", new String[]{loginAccount}, null, null, null, null);
			 if(c.moveToFirst()){
				 return true;
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 if(c!=null){
				 c.close();
			 }
			 closeDBHepler();
		 }
		return false;
	}
	/**
	 * 
	 * @param metroLineID
	 * @param mStartStation
	 * @param mEndStation
	 * @return
	 */
	public ArrayList<String> getStationList(String metroLineID, int mStartStation, int mEndStation) {

		ArrayList<String> stationList = new ArrayList<String>();
		String sql = "SELECT StationID FROM MetroStation WHERE MetroLineID=" + metroLineID
						+ " AND StationID>='" + mStartStation + "' AND StationID<='" + mEndStation + "' order by SerialCode ";
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			while (c.moveToNext()) {
				stationList.add(c.getString(0));
			}
		} catch (Exception e) {
			Log.e("TAG", "Method:getStationList, Error:" + e.getMessage());
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHepler();
		}
		return stationList;
	}
	
	public HashMap<String, Object> getTunnelsList(String mLienCode,
			String mStartStation, String mEndStation) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Cursor c = null;
		ArrayList<ArrayList<HashMap<String, String>>> Tunnlelist = new ArrayList<ArrayList<HashMap<String, String>>>();
		String sql = "select *from MetroTunnel where MetroLineID='" + mLienCode
				+ "' and StartStation='" + mStartStation + "' and EndStation='"
				+ mEndStation + "'";
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if (c.moveToFirst()) {
				map.put("MetroLineID",
						c.getString(c.getColumnIndex("MetroLineID")));
				map.put("DrivingDirection",
						c.getString(c.getColumnIndex("DrivingDirection")));
				map.put("StartStation",
						c.getString(c.getColumnIndex("StartStation")));
				map.put("EndStation",
						c.getString(c.getColumnIndex("EndStation")));
				map.put("Name", c.getString(c.getColumnIndex("Name")));
				String tunnelInfo = c.getString(c.getColumnIndex("Shape"));
				if (tunnelInfo.contains("LINESTRING") && !tunnelInfo.contains("MULTILINESTRING")) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
					String tunnelLines = tunnelInfo.split("\\(")[1];
					tunnelLines = tunnelLines.substring(0,tunnelLines.length() - 1);
					String tunnels[] = tunnelLines.split(",");
					for (int i = 0; i < tunnels.length; i++) {
						HashMap<String, String> tmap = new HashMap<String, String>();
						String x = "";
						String y = "";
						try {
							x = tunnels[i].split(" ")[0];
							y = tunnels[i].split(" ")[1];
						} catch (Exception e) {
						}
						tmap.put("x", x);
						tmap.put("y", y);
						list.add(tmap);
					}
					Tunnlelist.add(list);
					System.out.println(tunnels);
				} else if (tunnelInfo.contains("MULTILINESTRING")) {
					String tunnelLines = tunnelInfo.split("\\(\\(")[1];
					tunnelLines = tunnelLines.substring(0, tunnelLines.length() - 2);
					try {
						String tunnels[] = tunnelLines.split("\\)\\,\\(");
						for (int i = 0; i < tunnels.length; i++) {
							ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
							String cd[] = tunnels[i].split(",");
							for (int j = 0; j < cd.length; j++) {
								HashMap<String, String> tmap = new HashMap<String, String>();
								String x = "";
								String y = "";
								x = cd[j].split(" ")[0];
								y = cd[j].split(" ")[1];
								tmap.put("x", x);
								tmap.put("y", y);
								list.add(tmap);
							}
							Tunnlelist.add(list);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				map.put("Shape", Tunnlelist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHepler();
		}
		return map;
	}
	/**
	 * 车站数据
	 * 
	 * @return
	 */
	public HashMap<String, Object> getStationPointList(String mLienCode, String mStationCode) {
		String sql = "SELECT * FROM MetroStation WHERE MetroLineID ='" + mLienCode + "' AND StationID='" + mStationCode + "'";
		HashMap<String, Object> omap = new HashMap<String, Object>();
		//ArrayList<HashMap<String, String>> mGroupStationPointList = new ArrayList<HashMap<String, String>>();
		Cursor c0 = dbHelper.getReadableDatabase().rawQuery(sql, null);
		try {
			ArrayList<ArrayList<HashMap<String, String>>> infoMap = new ArrayList<ArrayList<HashMap<String,String>>>();
			if (c0.moveToFirst()) {
				String tunnelInfo = c0.getString(c0.getColumnIndex("Shape"));
				if (tunnelInfo.contains("POLYGON") && !tunnelInfo.contains("MULTIPOLYGON")) {
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
					String tunnelLines = tunnelInfo.split("\\(\\(")[1];
					tunnelLines = tunnelLines.substring(0,tunnelLines.length() - 2);
					String tunnels[] = tunnelLines.split(",");
					for (int i = 0; i < tunnels.length; i++) {
						HashMap<String, String> tmap = new HashMap<String, String>();
						String x = "";
						String y = "";
						try {
							x = tunnels[i].split(" ")[0];
							y = tunnels[i].split(" ")[1];
						} catch (Exception e) {
						}
						tmap.put("x", x);
						tmap.put("y", y);
						list.add(tmap);
					}
					infoMap.add(list);
				} else if (tunnelInfo.contains("MULTIPOLYGON")) {
					String tunnelLines = tunnelInfo.split("\\(\\(\\(")[1];
					tunnelLines = tunnelLines.substring(0, tunnelLines.length() - 3);
					try {
						String tunnels[] = tunnelLines.split("\\)\\)\\,\\(\\(");
						for (int i = 0; i < tunnels.length; i++) {
							ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
							String cd[] = tunnels[i].split(",");
							for (int j = 0; j < cd.length; j++) {
								HashMap<String, String> tmap = new HashMap<String, String>();
								String x = "";
								String y = "";
								x = cd[j].split(" ")[0];
								y = cd[j].split(" ")[1];
								tmap.put("x", x);
								tmap.put("y", y);
								list.add(tmap);
							}
							infoMap.add(list);
						}
					} catch (Exception e) {
					}
				}
				omap.put("shape", infoMap);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (c0 != null) {
				c0.close();
			}
			closeDBHepler();
		}
		return omap;
	}
	// 车站红线
	public HashMap<String, Object> getRedLineStationList(String mLineNum,
			String mLor, String mStartStation) {
		String sql = "SELECT * FROM RedLine WHERE MetroLineID=" + mLineNum
						+ " AND DrivingDirection='" + mLor + "'"
						+ " AND SectionId='" + mStartStation + "'";
		Cursor c = dbHelper.getReadableDatabase().rawQuery(sql, null);
		HashMap<String, Object> mLine = new HashMap<String, Object>();
		try {
			if (c.moveToFirst()) {
				mLine.put("ID", c.getString(c.getColumnIndex("ID")));
				mLine.put("MetroLineID", c.getString(c.getColumnIndex("MetroLineID")));
				mLine.put("DrivingDirection",
						c.getString(c.getColumnIndex("DrivingDirection")));
				mLine.put("StartStation",
						c.getString(c.getColumnIndex("StartStation")));
				mLine.put("EndStation",
						c.getString(c.getColumnIndex("EndStation")));
				ArrayList<HashMap<String, String>> Tunnlelist = new ArrayList<HashMap<String, String>>();
				String tunnelInfo = c.getString(c.getColumnIndex("Shape"));
				if (tunnelInfo.contains("LINESTRING")
						&& !tunnelInfo.contains("MULTILINESTRING")) {
					String tunnelLines = tunnelInfo.split("\\(")[1];
					tunnelLines = tunnelLines.substring(0,tunnelLines.length() - 1);
					String tunnels[] = tunnelLines.split(",");
					for (int i = 0; i < tunnels.length; i++) {
						HashMap<String, String> tmap = new HashMap<String, String>();
						String x = "";
						String y = "";
						try {
							x = tunnels[i].split(" ")[0];
							y = tunnels[i].split(" ")[1];
						} catch (Exception e) {
							e.printStackTrace();
						}
						tmap.put("x", x);
						tmap.put("y", y);
						Tunnlelist.add(tmap);
					}
				} else if (tunnelInfo.contains("MULTILINESTRING")) {
					String tunnelLines = tunnelInfo.split("\\(\\(")[1];
					tunnelLines = tunnelLines.substring(0,
							tunnelLines.length() - 1);
					try {
						String tunnels[] = tunnelLines.split(",\\(");
						for (int i = 0; i < tunnels.length; i++) {
							tunnels[i] = tunnels[i].replaceAll("\\(", "");
							String cd[] = tunnels[i].split(",");
							for (int j = 0; j < cd.length; j++) {
								HashMap<String, String> tmap = new HashMap<String, String>();
								String x = "";
								String y = "";
								x = cd[i].split(" ")[0];
								y = cd[i].split(" ")[1];
								tmap.put("x", x);
								tmap.put("y", y);
								Tunnlelist.add(tmap);
							}
						}
					} catch (Exception e) {
					}
				}
				mLine.put("shape", Tunnlelist);
			}
		} catch (Exception e) {
			Log.e("TAG", "Method:getRedLineList, Error:" + e.getMessage());
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHepler();
		}
		return mLine;
	}
	/**
	 * 获取红线数据
	 * 
	 * @author wanghb
	 * @return
	 */
	public HashMap<String, Object> getRedLineList(String mLineNum, String mLor,
			String mStartStation, String mEndStation) {
		String sql = "SELECT * FROM RedLine WHERE MetroLineID='" + mLineNum
						+ "' AND StartStation='" + mStartStation
						+ "' AND EndStation='" + mEndStation + "' ";
		Cursor c = dbHelper.getReadableDatabase().rawQuery(sql, null);
		HashMap<String, Object> mLine = new HashMap<String, Object>();
		try {

			if (c.moveToFirst()) {
				mLine.put("ID", c.getString(c.getColumnIndex("ID")));
				mLine.put("MetroLineID",
						c.getString(c.getColumnIndex("MetroLineID")));
				mLine.put("DrivingDirection",
						c.getString(c.getColumnIndex("DrivingDirection")));
				mLine.put("StartStation",
						c.getString(c.getColumnIndex("StartStation")));
				mLine.put("EndStation",
						c.getString(c.getColumnIndex("EndStation")));
				ArrayList<HashMap<String, String>> Tunnlelist = new ArrayList<HashMap<String, String>>();
				String tunnelInfo = c.getString(c.getColumnIndex("Shape"));
				if (tunnelInfo.contains("LINESTRING")
						&& !tunnelInfo.contains("MULTILINESTRING")) {
					String tunnelLines = tunnelInfo.split("\\(")[1];
					tunnelLines = tunnelLines.substring(0,
							tunnelLines.length() - 1);
					String tunnels[] = tunnelLines.split(",");
					for (int i = 0; i < tunnels.length; i++) {
						HashMap<String, String> tmap = new HashMap<String, String>();
						String x = "";
						String y = "";
						try {
							x = tunnels[i].split(" ")[0];
							y = tunnels[i].split(" ")[1];
						} catch (Exception e) {
							e.printStackTrace();
						}
						tmap.put("x", x);
						tmap.put("y", y);
						Tunnlelist.add(tmap);
					}
				} else if (tunnelInfo.contains("MULTILINESTRING")) {
					String tunnelLines = tunnelInfo.split("\\(\\(")[1];
					tunnelLines = tunnelLines.substring(0,
							tunnelLines.length() - 1);
					try {
						String tunnels[] = tunnelLines.split(",\\(");
						for (int i = 0; i < tunnels.length; i++) {
							tunnels[i] = tunnels[i].replaceAll("\\(", "");
							String cd[] = tunnels[i].split(",");
							for (int j = 0; j < cd.length; j++) {
								HashMap<String, String> tmap = new HashMap<String, String>();
								String x = "";
								String y = "";
								x = cd[i].split(" ")[0];
								y = cd[i].split(" ")[1];
								tmap.put("x", x);
								tmap.put("y", y);
								Tunnlelist.add(tmap);
							}
						}
					} catch (Exception e) {
					}
				}
				mLine.put("shape", Tunnlelist);
			}
		} catch (Exception e) {
			Log.e("TAG", "Method:getRedLineList, Error:" + e.getMessage());
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHepler();
		}
		return mLine;
	}
	/** 删除用户名 */
	public  boolean  delUser(String loginAccount){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		try{
		  int  result=db.delete(Constants.TABLE_USER, "LoginAccount=?", new String[]{loginAccount});
		   if(result==0){
			   return true;
		   }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
			closeDBHepler();
		}
		return false;
	}
	/**
	 * 获取用户名
	 */
	public String getUserName(){
		String name = "";
		String sql = "SELECT * FROM User ORDER BY UserLastLoginDate DESC";
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToNext()){
				name = c.getString(c.getColumnIndex("UserName"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return name;
	}
	
	/**
	  *插入线路基础数据 
	  **/
	public boolean saveLinInfo(ArrayList<HashMap<String, String>> list) {
		Cursor c = null;
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HashMap<String, String> linlistMap = list.get(i);
					ContentValues values = new ContentValues();
					String sql = String.format("SELECT * FROM MetroLine WHERE MetroLineId='%s'", linlistMap.get("MetroLineId"));
					try {
						c = dbHelper.getReadableDatabase().rawQuery(sql, null);
						if (c.moveToFirst()) {
							dbHelper.getReadableDatabase().execSQL(String.format("DELETE FROM MetroLine WHERE MetroLineId='%s'", linlistMap.get("MetroLineId")));
						} 
					} finally {
						if (c != null) {
							c.close();
							c = null;
						}
					}
					values.put("MetroLineCode", linlistMap.get("MetroLineCode"));
					values.put("MetroLineName", linlistMap.get("MetroLineName"));
					values.put("MetroLineId", linlistMap.get("MetroLineId"));
					values.put("PMetroLineId", linlistMap.get("PMetroLineId"));
					dbHelper.insert(values, "MetroLine");
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDBHepler();
		}
		return false;
	}
	/**
	 * 获得视频文件的本地地址
	 * @param vedioRemotePath
	 * @return
	 */
	public String getVedioFilePath(String vedioRemotePath){
		String filePath = "";
		String sql = "SELECT * FROM IllegalVedioFiles WHERE FileRemotePath = '"+vedioRemotePath+"'";
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()){
				filePath = c.getString(c.getColumnIndex("FilePath"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return filePath;
	}
	
	 /**
	  *插入检查对象基础信息
	  **/
	 public boolean saveCheckObjectInfo(ArrayList<HashMap<String, String>> list){
		 Cursor c=null;
		 try {
			 if(list != null && list.size()>0) {
				 for (int i = 0; i < list.size(); i++) {
					 HashMap<String, String> objectlistMap=list.get(i);
					 ContentValues values=new ContentValues();
					 values.put("MetroLineId", objectlistMap.get("MetroLineId"));
					 values.put("CheckObjectCode", objectlistMap.get("CheckObjectCode"));
					 values.put("CheckObjectName", objectlistMap.get("CheckObjectName"));
					 values.put("TermPartId", objectlistMap.get("TermPartId"));
					 if (objectlistMap.get("CheckObjectType")!=null) {
						if (objectlistMap.get("CheckObjectType").equals("车站")) {
							values.put("CheckObjectType", 3);
						}else if (objectlistMap.get("CheckObjectType").equals("风井")) {
							values.put("CheckObjectType", 2);
						}else if (objectlistMap.get("CheckObjectType").equals("区间")) {
							values.put("CheckObjectType", 1);
						}
					} else {
						values.put("CheckObjectType", objectlistMap.get("CheckObjectType"));
					}
					 values.put("SerialCode", objectlistMap.get("SerialCode"));
					 
					 values.put("StartStationCode", objectlistMap.get("StartStationCode"));//前站编号 add by Rubert 2014-04-07
					 values.put("EndStationCode", objectlistMap.get("EndStationCode"));//后站编号  add by Rubert 2014-04-07
					 
					 String sql="select * from MetroCheckObject where CheckObjectCode='"+objectlistMap.get("CheckObjectCode")+"'";
					 try {
						 c=dbHelper.getReadableDatabase().rawQuery(sql, null);
					     if (c.moveToFirst()) {
					    	 String sql_="delete from MetroCheckObject where CheckObjectCode='"+objectlistMap.get("CheckObjectCode")+"'";
							 dbHelper.getReadableDatabase().execSQL(sql_);
							 dbHelper.insert(values, "MetroCheckObject");	 
					     } else {
					    	 dbHelper.insert(values, "MetroCheckObject");
					     }
					 } finally {
						if (c != null) {
							c.close();
							c = null;
						}
					 }
				 } return true;		
			 	}
			 } catch(Exception e) {
				 e.printStackTrace();
				 return false;
			 } finally {
				 closeDBHepler();
			 }	
			 return false;
	 }
	
	 
	 /**
	  *插入检查对象段基础信息
	  **/
	 public boolean saveCheckObjectDetailInfo(ArrayList<HashMap<String, String>> list){
		 
			Cursor c=null;
		 try {
			 if (list != null && list.size()>0) {
				 
		 for (int i = 0; i < list.size(); i++) {
			 HashMap<String, String> objectdetaillistMap=list.get(i);
			 ContentValues values=new ContentValues();
			 values.put("CheckObjectCode", objectdetaillistMap.get("CheckObjectCode"));
			 values.put("CheckObjectDetailCode", objectdetaillistMap.get("CheckObjectDetailCode"));
			 values.put("LineDirection", objectdetaillistMap.get("LineDirection"));
			 if (objectdetaillistMap.get("StartMileage")!=null&&!objectdetaillistMap.get("StartMileage").equals("")&&!objectdetaillistMap.get("StartMileage").equals("null")) {
				 values.put("StartMileage", objectdetaillistMap.get("StartMileage"));
			}else{
				 values.put("StartMileage", "");
			}
			if (objectdetaillistMap.get("EndMileage")!=null&&!objectdetaillistMap.get("EndMileage").equals("")&&!objectdetaillistMap.get("EndMileage").equals("null")) {
				values.put("EndMileage", objectdetaillistMap.get("EndMileage"));
			}else{
				values.put("EndMileage", "");
			}
			 values.put("MileageAddDirection", objectdetaillistMap.get("MileageAddDirection"));
			 if (objectdetaillistMap.get("ArenosolArea")!=null&&!objectdetaillistMap.get("ArenosolArea").equals("")&&!objectdetaillistMap.get("ArenosolArea").equals("null")) {
				 	values.put("ArenosolArea", objectdetaillistMap.get("ArenosolArea"));
			 }else{
					 values.put("ArenosolArea","");
			 }
			 if (objectdetaillistMap.get("OverproofArea")!=null&&!objectdetaillistMap.get("OverproofArea").equals("")&&!objectdetaillistMap.get("OverproofArea").equals("null")) {
				 	values.put("OverproofArea", objectdetaillistMap.get("OverproofArea"));
				}else{
					 values.put("OverproofArea", "");
				}
			 if (objectdetaillistMap.get("DamagedArea")!=null&&!objectdetaillistMap.get("DamagedArea").equals("")&&!objectdetaillistMap.get("DamagedArea").equals("null")) {
				 values.put("DamagedArea", objectdetaillistMap.get("DamagedArea"));
				}else{
					 values.put("DamagedArea", "");
				}
			 values.put("CheckObjectDetailType", objectdetaillistMap.get("CheckObjectDetailType"));
			 values.put("UpdateTime", objectdetaillistMap.get("UpdateTime"));
				 try {
				 String sql="select  * from MetroCheckObjectDetail where CheckObjectDetailCode='"+objectdetaillistMap.get("CheckObjectDetailCode")+"'";
				   c=dbHelper.getReadableDatabase().rawQuery(sql, null);
					if (c.moveToFirst()) {
						if (c.getString(c.getColumnIndex("UpdateNum")) != null) { 
							values.put("UpdateNum", c.getString(c.getColumnIndex("UpdateNum")));
						}
						String sql_="delete from MetroCheckObjectDetail where CheckObjectDetailCode='"+objectdetaillistMap.get("CheckObjectDetailCode")+"'";
						dbHelper.getWritableDatabase().execSQL(sql_);
						dbHelper.insert(values, "MetroCheckObjectDetail");
					}else{
						 dbHelper.insert(values, "MetroCheckObjectDetail");
					}	
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if (c!=null) {
							c.close();
						}
					}
			 
//			 dbHelper.insert(values, "MetroCheckObjectDetail");
			 
					}
			 }
		 }catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if (dbHelper!=null) {
				dbHelper.close();
			}
			
		} 
		 return false;
	 }
	 
	 /**
	  *插入检查设施基础信息
	  **/
	public boolean saveFacilityInfo(ArrayList<HashMap<String, String>> list) {
		Cursor c = null;
		try {
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HashMap<String, String> objectdetaillistMap = list.get(i);
					ContentValues values = new ContentValues();
					values.put("CheckObjectDetailCode", objectdetaillistMap.get("CheckObjectDetailCode"));
					values.put("FacilityCode", objectdetaillistMap.get("FacilityCode"));
					values.put("FacilityName", objectdetaillistMap.get("FacilityName"));
					values.put("FacilityTypeCode", objectdetaillistMap.get("FacilityTypeCode"));
					values.put("PFacilityCode", objectdetaillistMap.get("PFacilityCode"));
					values.put("EndMileage", objectdetaillistMap.get("EndMileage"));
					values.put("StartMileage", objectdetaillistMap.get("StartMileage"));
					values.put("StartCircle", objectdetaillistMap.get("StartCircle"));
					values.put("EndCircle", objectdetaillistMap.get("EndCircle"));
					String sql = "select * from MetroFacility where FacilityCode='" + objectdetaillistMap.get("FacilityCode") + "'";
					try {
						c = dbHelper.getReadableDatabase().rawQuery(sql, null);
						if (c.moveToFirst()) {
							String sql_ = "delete from MetroFacility where FacilityCode='" + objectdetaillistMap.get("FacilityCode") + "'";
							dbHelper.getReadableDatabase().execSQL(sql_);
							dbHelper.insert(values, "MetroFacility");
						} else {
							dbHelper.insert(values, "MetroFacility");
						}
					} finally {
						if (c != null) {
							c.close();
							c = null;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHepler();
		}
		return true;
	}

	 /**
	  *插入设施盾构基础信息
	  **/
	 public boolean saveShieldInfo(ArrayList<HashMap<String, String>> list){ 
		 try {
		 if(list != null && list.size() > 0) {
				 for(HashMap<String, String> m : list) {
					 if(m != null) {
						 try {
							 ContentValues values=new ContentValues();
							 values.put("FacilityCode", m.get("FacilityCode"));
							 values.put("SegmentType", m.get("SegmentType"));
							 values.put("CircleWidth", m.get("CircleWidth"));
							 values.put("TunnelDiameter", m.get("TunnelDiameter"));
							 values.put("HoopBolt", m.get("HoopBolt"));
							 values.put("EndwiseBolt", m.get("EndwiseBolt"));
							 values.put("FirstStationCode", m.get("FirstStationCode"));
							 values.put("SegmentsDirection", m.get("SegmentsDirection"));
							 values.put("Remark", m.get("Remark"));
						     dbHelper.getReadableDatabase().execSQL("delete from FacilityShieldInfo where FacilityCode='"+ m.get("FacilityCode")+"'");
							 dbHelper.insert(values, "FacilityShieldInfo");
						 } catch (Exception e) {
							 e.printStackTrace();
						 } 
					 }
				 } 
			 
		 }
		 return true;
		 } finally {
			 closeDBHepler();
		 }
	 }
	 
	/**
	 * 线路信息获取
	 * @param mPMetroLineId
	 * @author Rubert
	 * @date 2015-03-20 13:53
	 * @return ArrayList<HashMap<String, String>>
	 */
	 public ArrayList<HashMap<String, String>> getLineInfoByPMetroLineId(String mPMetroLineId) {
		 String sql = String.format("SELECT * FROM MetroLine WHERE PMetroLineId=%s", mPMetroLineId);
		 Cursor c = null;
		 ArrayList<HashMap<String, String>> mLineList = new ArrayList<HashMap<String, String>>();
		 try {
			 c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			 while(c.moveToNext()) {
				 String mMetroLineId = c.getString(c.getColumnIndex("MetroLineId"));
				 String mMetroLineName = c.getString(c.getColumnIndex("MetroLineName"));
				 String mMetroLineCode = c.getString(c.getColumnIndex("MetroLineCode"));
				 
				 HashMap<String, String> map = new HashMap<String, String>();
				 map.put("MetroLineId", mMetroLineId);
				 map.put("MetroLineName", mMetroLineName);
				 map.put("PMetroLineId", mPMetroLineId);
				 map.put("MetroLineCode", mMetroLineCode);
				 mLineList.add(map);
			 }
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 if(c != null) {
				 c.close();
				 c = null;
			 }
			 closeDBHepler();
		 }
		 return mLineList;
	 }
	    /**
		 * 线路信息获取
		 * @param mMetroLineId
		 * @author Rubert
		 * @date 2015-03-20 13:53
		 * @return ArrayList<HashMap<String, String>>
		 */
	 public ArrayList<HashMap<String, String>> getLineInfoByObject(String mMetroLineId,String mCheckObjectType) {
		 String sql = String.format("SELECT * FROM MetroCheckObject WHERE TermPartId=%s AND CheckObjectType=%s", mMetroLineId, mCheckObjectType);
		 Cursor c = null;
		 ArrayList<HashMap<String, String>> mLineList = new ArrayList<HashMap<String, String>>();
		 try {
			 c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			 while(c.moveToNext()) {
				 String mCheckObjectCode = c.getString(c.getColumnIndex("CheckObjectCode"));
				 String mMetroLineName = c.getString(c.getColumnIndex("CheckObjectName"));
				 // String mMetroLineCode = c.getString(c.getColumnIndex("MetroLineCode"));
				 HashMap<String, String> map = new HashMap<String, String>();
				 map.put("ID", c.getString(c.getColumnIndex("ID")));
				 map.put("CheckObjectCode", mCheckObjectCode);
				 map.put("MetroLineName", mMetroLineName);
				 // map.put("MetroLineCode", mMetroLineCode);
				 map.put("TermPartId", mMetroLineId);
				 mLineList.add(map);
			 }
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 if(c != null) {
				 c.close();
				 c = null;
			 }
			 closeDBHepler();
		 }
		 return mLineList;
	 }
	/**
	 * 线路信息-车站信息获取
	 * @param mId
	 * @author Rubert
	 * @date 2015-03-23 11:03
	 * @return ArrayList<HashMap<String, String>>
	 * @desc 此方法仅限于在MetroCheckObject表中的区间数据的上一条数据和下一条数据是该区间的前站和后站的情况下
	 */
	 public ArrayList<HashMap<String, String>> getLineStationById(String mId) {
		 String sql = String.format("SELECT * FROM MetroCheckObject WHERE ID=%s OR ID=%s", Integer.parseInt(mId) - 1 , Integer.parseInt(mId) + 1);
		 Cursor c = null;
		 ArrayList<HashMap<String, String>> mLineList = new ArrayList<HashMap<String, String>>();
		 try {
			 c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			 while(c.moveToNext()) {
				 HashMap<String, String> map = new HashMap<String, String>();
				 map.put("ID", c.getString(c.getColumnIndex("ID")));
				 map.put("CheckObjectCode", c.getString(c.getColumnIndex("CheckObjectCode")));
				 map.put("MetroLineName", c.getString(c.getColumnIndex("CheckObjectName")));
				 map.put("TermPartId", c.getString(c.getColumnIndex("TermPartId")));
				 mLineList.add(map);
			 }
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 if(c != null) {
				 c.close();
				 c = null;
			 }
			 closeDBHepler();
		 }
		 return mLineList;
	 }
	 
	/* *//**
	  * 获得更新时间
	  *//*
	 public String getUpdataTime(String name){
		 String time = "";
		 String sql = "SELECT * FROM WebService WHERE WebServiceName = '"+ name +"'";
		 Cursor c = null;
		 try{
			 c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			 if(c.moveToNext()){
				 time = c.getString(c.getColumnIndex("WebServiceUpdateTime"));
			 }
		 }catch (Exception e) {
			e.printStackTrace();
		 }finally{
			 if(c != null) {
				 c.close();
				 c = null;
			 }
			 closeDBHepler();
		 }
		 
		 return time;
	 }*/
	 /**
	   * 线路信息-车站信息获取
	   * @author Rubert
	   * @date 2015-03-26 11:04
	   * @return ArrayList<HashMap<String, String>>
	   * @desc 
	   */
	 public ArrayList<HashMap<String, String>> getLinesMainList() {
		 String sql = "SELECT * FROM MetroLine WHERE PMetroLineId in (SELECT MetroLineId FROM MetroLine WHERE PMetroLineId = -1)";
		 Cursor c = null;
		 ArrayList<HashMap<String, String>> mLineList = new ArrayList<HashMap<String, String>>();
		 try {
			 c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			 while(c.moveToNext()) {
				 HashMap<String, String> map = new HashMap<String, String>();
				 map.put("ID", c.getString(c.getColumnIndex("ID")));
				 map.put("MetroLineId", c.getString(c.getColumnIndex("MetroLineId")));
				 map.put("PMetroLineId", c.getString(c.getColumnIndex("PMetroLineId")));
				 map.put("MetroLineName", c.getString(c.getColumnIndex("MetroLineName")));
				 map.put("MetroLineCode", c.getString(c.getColumnIndex("MetroLineCode")));
				 mLineList.add(map);
			 }
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 if(c != null) {
				 c.close();
				 c = null;
			 }
			 closeDBHepler();
		 }
		 return mLineList;
	 }
	 
	 /**
	  * 获取一号线的基础信息表
	  * @return
	  */
	 public ArrayList<HashMap<String, String>> getLineOneInfo(String nodeCode){
		 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		 String sql=String.format("SELECT * FROM MetroLine WHERE PMetroLineId = %s", nodeCode);
		 Cursor c=null;
		 Cursor c2=null;
		 Cursor c3=null;
		 try{
			 c=dbHelper.getWritableDatabase().rawQuery(sql, null);
			 while(c.moveToNext()){
				 HashMap<String, String> map=new HashMap<String, String>();
				 map.put("metroLineCode", c.getString(c.getColumnIndex("MetroLineCode")));
				 map.put("metroLineName", c.getString(c.getColumnIndex("MetroLineName")));
				 String sql2=String.format("SELECT * FROM MetroCheckObject WHERE  MetroLineId =%s AND TermPartId!=0", c.getString(c.getColumnIndex("MetroLineId")));
				 c2=dbHelper.getWritableDatabase().rawQuery(sql2, null);
				 if(c2.moveToLast()){
					 map.put("endStation", c2.getString(c2.getColumnIndex("CheckObjectName")));//
				 }
				 if(c2.moveToFirst()){
					 map.put("startStation", c2.getString(c2.getColumnIndex("CheckObjectName")));
					 String sql3=String.format("SELECT * FROM MetroCheckObjectDetail WHERE CheckObjectDetailCode='%s'" +
						 		" GROUP BY CheckObjectDetailCode", c2.getString(c2.getColumnIndex("CheckObjectCode"))+"-x");
					 String sql4=String.format("SELECT * FROM MetroCheckObjectDetail WHERE CheckObjectDetailCode='%s'" +
						 		" GROUP BY CheckObjectDetailCode", c2.getString(c2.getColumnIndex("CheckObjectCode"))+"-s");
					 c3=dbHelper.getReadableDatabase().rawQuery(sql3, null);
					 if(c3.moveToFirst()){
						 map.put("startMileageX", c3.getString(c3.getColumnIndex("StartMileage")));
						 map.put("endMileageX", c3.getString(c3.getColumnIndex("EndMileage")));
					 }
					 c3=dbHelper.queryBySql(sql4, null);
					 if(c3.moveToFirst()){
						 map.put("startMileageS", c3.getString(c3.getColumnIndex("StartMileage")));
						 map.put("endMileageS", c3.getString(c3.getColumnIndex("EndMileage")));
					 }
				 }
				 arrayList.add(map);
			 }
		 }catch(Exception  e){
			 e.printStackTrace();
		 }finally{
			 if(c!=null){
				 c.close();
			 }
			 if(c2!=null){
				 c2.close();
			 }
			 if(c3!=null){
				 c3.close();
			 }
			 closeDBHepler();
		 }
		 return arrayList;
	 }
	 
	 /**
	  * 获取线路信息的第三级菜单
	  */
	 public ArrayList<HashMap<String, String>>  getLineInfoLevel3(String nodeCode){
		 ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		  String sql=String.format("SELECT * FROM MetroLine WHERE PMetroLineId=%s ORDER BY MetroLineId", nodeCode);
		  Cursor c=null;
		  Cursor c2=null;
		  Cursor c3=null;
		  try{
			  c=dbHelper.getReadableDatabase().rawQuery(sql, null);
			  while(c.moveToNext()){
				  String  endStation="";//把最后一个车站用来作为下一次循环的第一个
				  HashMap<String, String> map=new HashMap<String, String>();
				  map.put("metroLineName", c.getString(c.getColumnIndex("MetroLineName")));
				  String sql2=String.format("SELECT * FROM MetroCheckObject WHERE TermPartId= ", c.getString(c.getColumnIndex("MetroLineId")));
				  c2=dbHelper.getReadableDatabase().rawQuery(sql2, null);
				  if("".equals(endStation)){		
					  if(c2.moveToFirst()){
						  map.put("startStation", c2.getString(c2.getColumnIndex("CheckObjectName")));
					  }
//					 String sql3=String.format("", args)
				  }else{
					  map.put("startStation", endStation);
				  }
					  if(c2.moveToLast()){
						  map.put("EndStation", c2.getString(c2.getColumnIndex("CheckObjectName")));
						  endStation=c2.getString(c2.getColumnIndex("CheckObjectName"));
				  }
				  
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  if(c!=null){
					 c.close();
				 }
				 if(c2!=null){
					 c2.close();
				 }
				 if(c3!=null){
					 c3.close();
				 }
				 closeDBHepler();
		  }
		  return arrayList;
	 }
	 
}
