package com.tongyan.yanan.common.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.tongyan.yanan.common.utils.PingyinUtils;
import com.tongyan.yanan.fragment.progress.OthersFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	// private DBHelper dbHelper;
	private final static String TAG = "DBService";

	private Context context;
	private DBHelp dbHelpe;
	public DBService(Context context) {
		// this.dbHelper = new DBHelper(context);
		dbHelpe = new DBHelp();
	}
	
	/**
	 * 
	 */
	public void closeDBHelper() {
		if (dbHelpe != null) {
			dbHelpe.close();
			dbHelpe = null;
		}
			
	}
	
	/**  删除某表，所有数据 */
	public boolean delAll(String tabName) {
		try {
			dbHelpe.del(tabName);
		} catch (Exception e) {
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	
	/** 判断表是否为空 */
	public boolean isEmpty(String tabname) {
		Cursor cursor = null;
		try {
			cursor = dbHelpe.getReadableDatabase().rawQuery("SELECT COUNT(1) FROM " + tabname,null);
			if(cursor.moveToFirst()) {
				if (cursor != null && cursor.getInt(0) > 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
			
		}catch (Exception e) {
			return false;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHelper();
		}
	}
	
	/** 根据UserId查询用户名*/
	public String queryTableUser(String userId){
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_USER",new String[]{"UserName"} ,"UserId=?", new String[]{userId}, null, null, null);
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("UserName"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return "";
	}
	
	/** 
	 * 向用户表中插入数据
	 * @author wanghb
	 * @date 2014/06/25
	 * @return boolean
     */
	public boolean insertUser(HashMap<String, String> mUserMap) {
		ContentValues values = new ContentValues();
		values.put("UserId", mUserMap.get("UserId"));
		values.put("LoginAccount", mUserMap.get("LoginAccount"));
		values.put("Password", mUserMap.get("Password"));
		values.put("UserName", mUserMap.get("UserName"));
		values.put("DeptId", mUserMap.get("DeptId"));
		values.put("JobId", mUserMap.get("JobId"));
		values.put("UserPhone", mUserMap.get("UserPhone"));
		values.put("UserEmail", mUserMap.get("UserEmail"));
		values.put("UserQQ", mUserMap.get("UserQQ"));
		values.put("SysId", mUserMap.get("SysId"));
		values.put("UserRoleId", mUserMap.get("UserRoleId"));
		values.put("UserSex", mUserMap.get("UserSex"));
		values.put("UserBirthday", mUserMap.get("UserBirthday"));
		values.put("UserLastLoginDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		values.put("SavePassword", mUserMap.get("SavePassword"));
		try {
			dbHelpe.insert(values, "LOCAL_USER");
		} catch(Exception e) {
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}

	/** 查询用户名的记住密码的状态信息 */
	public HashMap<String, String> queryUserByAccount(String mLoginAccount) {
		HashMap<String, String> map = null;
		Cursor c = null;
		try {
			c = dbHelpe.getWritableDatabase().query("LOCAL_USER", null, "LoginAccount=?",new String[] { mLoginAccount }, null, null, null, null);
			if (c.moveToFirst()) {
				map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("LoginAccount", c.getString(c.getColumnIndex("LoginAccount")));
				map.put("Password", c.getString(c.getColumnIndex("Password")));
				map.put("UserName", c.getString(c.getColumnIndex("UserName")));
				map.put("UserId", c.getString(c.getColumnIndex("UserId")));
				map.put("SavePassword", c.getString(c.getColumnIndex("SavePassword")));
				return map;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHelper();
		}
		return null;
	}
	
	/** 获取最后一次登陆的用户名和密码,以及状态值*/
	public HashMap<String, String> queryUser() {
		HashMap<String, String> map = new HashMap<String, String>();
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_USER ORDER BY UserLastLoginDate DESC", null);
			if (c.moveToFirst()) {
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("LoginAccount", c.getString(c.getColumnIndex("LoginAccount")));
				map.put("Password", c.getString(c.getColumnIndex("Password")));
				map.put("UserName", c.getString(c.getColumnIndex("UserName")));
				map.put("UserId", c.getString(c.getColumnIndex("UserId")));
				map.put("SavePassword", c.getString(c.getColumnIndex("SavePassword")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c != null) {
				c.close();
			}
			closeDBHelper();
		}
		return map;
	}

	/** 更新用户表的状态值 */
	public boolean updateUserState(String mPassword, int mSavePassword,String mLoginAccount) {
		String sql = String.format("UPDATE LOCAL_USER SET Password='%s',SavePassword=%d,UserLastLoginDate='%s' WHERE LoginAccount='%s'",
				mPassword,mSavePassword,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),mLoginAccount);
		try {
			dbHelpe.getWritableDatabase().execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 *  标段-合同表数据插入 
	 * @author wanghb
	 * @date 2014/06/26
	 * @return boolean
	 * */
	public boolean insertTermPartPact(ArrayList<HashMap<String, String>> list) {
		try {
				for (HashMap<String, String> map : list) {
					if (map != null) {
						ContentValues values = new ContentValues();
						values.put("periodId", map.get("periodId"));
						values.put("periodName", map.get("periodName"));
						values.put("NewId", map.get("NewId"));
						values.put("ProjectId", map.get("ProjectId"));
						values.put("LotName", map.get("LotName"));
						values.put("LotCode", map.get("LotCode"));
						values.put("CompactionUnit", map.get("CompactionUnit"));
						values.put("SupervisorUnit", map.get("SupervisorUnit"));
						values.put("ProjectName", map.get("ProjectName"));
						values.put("ProjectCount", map.get("ProjectCount"));
						values.put("ProjectArea", map.get("ProjectArea"));
						values.put("StationArea", map.get("StationArea"));
						try {
							dbHelpe.insert(values, "TermPartPact");
						} catch (Exception e) {
							return false;
						}
					}
				}
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 进度基础数据表-插入
	 * @author wanghb
	 * @date 2014/07/08
	 * @param list
	 * @return boolean
	 */
	public boolean insertProgressInfo(ArrayList<HashMap<String, String>> list) {
		try {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					ContentValues values = new ContentValues();
					values.put("PNewId", map.get("PNewId"));
					values.put("PConstructionName", map.get("PConstructionName"));
					values.put("NewId", map.get("NewId"));
					values.put("ConstructionName", map.get("ConstructionName"));
					values.put("MeasureUnit", map.get("MeasureUnit"));
					values.put("Statistics", map.get("Statistics"));
					values.put("Sort", map.get("Sort"));
					values.put("DName", map.get("DName"));
					values.put("Pid", map.get("Pid"));
					values.put("ConstructioType", map.get("ConstructioType"));
					values.put("IsEnable", "false");
					values.put("SortType", map.get("sortType"));
					try {
						dbHelpe.insert(values, "ProgressInfo");
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 进度计划检查项基础数据表 - 查询
	 * @param type    检查项类型1：施工项 2：机械 3：人员
	 * @author wanghb
	 * @date 2014/07/08
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getProgressInfoList(String type) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		//TODO
		String sql0 = String.format("SELECT * FROM ProgressInfo WHERE ConstructioType = '%s' group by SortType  order by ID", type);


		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while (c0.moveToNext()) {
				HashMap<String, String> map0 = new HashMap<String, String>();
				map0.put("ID", c0.getString(c0.getColumnIndex("ID")));
				String PNewId = c0.getString(c0.getColumnIndex("PNewId"));
				map0.put("PNewId", PNewId);
				map0.put("PConstructionName", c0.getString(c0.getColumnIndex("PConstructionName")));
				map0.put("NewId", c0.getString(c0.getColumnIndex("NewId")));
				map0.put("ConstructionName", c0.getString(c0.getColumnIndex("ConstructionName")));
				//map0.put("MeasureUnit", c0.getString(c0.getColumnIndex("MeasureUnit")));
				//map0.put("Statistics", c0.getString(c0.getColumnIndex("Statistics")));
				//map0.put("Sort", c0.getString(c0.getColumnIndex("Sort")));
				map0.put("DName", c0.getString(c0.getColumnIndex("DName")));
				//String pid = c0.getString(c0.getColumnIndex("Pid"));
				//map0.put("Pid", pid);
				
				//map0.put("ConstructioType", c0.getString(c0.getColumnIndex("ConstructioType")));
				//map0.put("IsEnable", c0.getString(c0.getColumnIndex("IsEnable")));
				
				map0.put("ItemType","title");
				list.add(map0);				

				String sql = String.format("SELECT * FROM ProgressInfo WHERE ConstructioType = '%s' AND PNewId='%s' ORDER BY Sort", type, PNewId);
				Cursor c = null;
				try {
					c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
					while (c.moveToNext()) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("ID", c.getString(c.getColumnIndex("ID")));
						map.put("PNewId", c.getString(c.getColumnIndex("PNewId")));
						map.put("PConstructionName", c.getString(c.getColumnIndex("PConstructionName")));
						map.put("NewId", c.getString(c.getColumnIndex("NewId")));
						map.put("ConstructionName", c.getString(c.getColumnIndex("ConstructionName")));
						//map.put("MeasureUnit", c.getString(c.getColumnIndex("MeasureUnit")));
						//map.put("Statistics", c.getString(c.getColumnIndex("Statistics")));
						//map.put("Sort", c.getString(c.getColumnIndex("Sort")));
						map.put("DName", c.getString(c.getColumnIndex("DName")));
						map.put("Pid", c.getString(c.getColumnIndex("Pid")));
						
						map.put("ConstructioType", c.getString(c.getColumnIndex("ConstructioType")));
						map.put("IsEnable", c.getString(c.getColumnIndex("IsEnable")));
						map.put("ItemType","content");
						map.put("InputText",c.getString(c.getColumnIndex("InputText")));
						
						list.add(map);
					}
				} finally {
					if(c != null) {
						c.close();
						c = null;
					}
				}
				/*if(!"00000000-0000-0000-0000-000000000000".equals(pid)) {
					map0.put("ItemType","title");
					list.add(map0);
					
				} else {
					map0.put("ItemType","content");
					list.add(map0);
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	/**
	 * 进度计划检查项基础数据表 - 查询填写值
	 * @author wanghb
	 * @date 2014/07/10
	 * @return  ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getProgressInfoValueList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT * FROM ProgressInfo";
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("NewId", c.getString(c.getColumnIndex("NewId")));
				map.put("IsEnable", c.getString(c.getColumnIndex("IsEnable")));
				map.put("ConstructioType", c.getString(c.getColumnIndex("ConstructioType")));
				map.put("InputText",c.getString(c.getColumnIndex("InputText")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	
	/**
	 * 进度计划检查项基础数据表 - 更新填写值
	 * @param mInputText    填写值
	 * @param id            数据id
	 * @author wanghb
	 * @date 2014/07/09
	 * @return boolean
	 */
	public boolean updateProgressInputText(String mInputText, String id) {
		String sql = String.format("UPDATE ProgressInfo SET InputText='%s' WHERE ID=%s", mInputText, id);
		try {
			dbHelpe.getWritableDatabase().execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
		
		
		
	}
	/**
	 * 进度计划检查项基础数据表 - 更新所填项
	 * @param mIsEnable    勾选值(true/false)
	 * @param id           数据id
	 * @author wanghb
	 * @date 2014/07/09
	 * @return boolean
	 */
	public boolean updateProgressChecked(String mIsEnable, String id) {
		String sql = String.format("UPDATE ProgressInfo SET IsEnable='%s' WHERE ID=%s", mIsEnable, id);
		try {
			dbHelpe.getWritableDatabase().execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	
	/**
	 * 进度计划检查项基础数据表 - 初始化清空缓存数据
	 * @author wanghb
	 * @date 2014/07/10
	 * @return boolean
	 */
	public boolean updateClearProgress() {
		try {
			dbHelpe.getWritableDatabase().execSQL("UPDATE ProgressInfo SET IsEnable='false', InputText=''");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	public boolean deleteTableProgressUpdateInfo(String userId,String weekPlanId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("ProgressUpdateInfo", "UserId=? and WeekPlanId=?", new String[]{userId,weekPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * 进度计划检查项基础数据表 - 初始化已完成数据
	 * @author wanghb
	 * @date 2014/07/10
	 * @return boolean
	 */
	public boolean updateCacheProgress(ArrayList<HashMap<String, String>> list) {
		try {
			for(HashMap<String, String> map : list) {
				if(map != null) {
					String sql = String.format("UPDATE ProgressInfo SET IsEnable='true', InputText='%s' WHERE NewId='%s'", map.get("ValueContent"),map.get("ItemsId"));
					dbHelpe.getWritableDatabase().execSQL(sql);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 进度计划上传表 - 插入
	 * @param list  
	 * @author wanghb
	 * @date 2014/07/10
	 * @return boolean
	 */
	public boolean insertProgressUpdateInfo(ArrayList<HashMap<String, String>> list, String mUserId) {
		try {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					String mLotId = map.get("lotId");
					String mWeekPlanId = map.get("WeekPlanId");
					String mDayId = map.get("DayId");
					String mItemsId = map.get("ItemsId");
					String mValueContent = map.get("ValueContent");
					
					String sql = String.format("SELECT * FROM ProgressUpdateInfo WHERE LotId='%s' AND WeekPlanId='%s' AND DayId=%s AND ItemsId='%s' AND UserId='%s'", mLotId,mWeekPlanId,mDayId,mItemsId,mUserId);
					Cursor c = null;
					try {
						c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
						if(c.moveToFirst()) {//如果存在本条记录就更新
							dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE ProgressUpdateInfo SET ValueContent='%s' WHERE LotId='%s' AND WeekPlanId='%s' AND DayId=%s AND ItemsId='%s' AND UserId='%s'", mValueContent, mLotId,mWeekPlanId,mDayId,mItemsId,mUserId));
						} else {//否则
							ContentValues values = new ContentValues();
							values.put("lotId", mLotId);
							values.put("WeekPlanId", mWeekPlanId);
							values.put("DayId", mDayId);
							values.put("ItemsId", mItemsId);
							values.put("ItemsType", map.get("ItemsType"));
							values.put("RealDay", map.get("RealDay"));
							values.put("ValueContent", mValueContent);
							values.put("UserId", mUserId);
							dbHelpe.insert(values, "ProgressUpdateInfo");
						}
					} finally {
						if(c != null) {
							c.close();
							c = null;
						}
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 进度计划检查项检查数据表 - 查询
	 * @param list  
	 * @author wanghb
	 * @date 2014/07/10
	 * @return boolean
	 */
	public ArrayList<HashMap<String, String>> getProgressUpdateInfoList(String mLotId, String mWeekPlanId, String mDayId, String mUserId) {
		String sql = String.format("SELECT ID,ItemsId,ValueContent FROM ProgressUpdateInfo WHERE LotId='%s' AND WeekPlanId='%s' AND DayId=%s AND UserId='%s'", mLotId, mWeekPlanId, mDayId, mUserId);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("ItemsId", c.getString(c.getColumnIndex("ItemsId")));
				map.put("ValueContent",c.getString(c.getColumnIndex("ValueContent")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
 	
	/**
	 * 获取合同段列表 - 查询
	 * @author wanghb
	 * @date 2014/06/29
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getTermPartPact() {
		String sql0 = "SELECT periodId,periodName FROM TermPartPact GROUP BY periodId";
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while(c0.moveToNext()) {
				HashMap<String, String> map0 = new HashMap<String, String>();
				String  periodId = c0.getString(c0.getColumnIndex("periodId"));
				String periodName = c0.getString(c0.getColumnIndex("periodName"));
				map0.put("periodId", periodId);
				map0.put("periodName", periodName);
				map0.put("attribute", "title");
				list.add(map0);
				Cursor c = null;
				String sql = String.format("SELECT * FROM TermPartPact WHERE periodId = '%s'", periodId);
				try {
					c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
					while(c.moveToNext()) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("periodId", periodId);
						map.put("periodName", periodName);
						map.put("NewId", c.getString(c.getColumnIndex("NewId")));
						map.put("ProjectId", c.getString(c.getColumnIndex("ProjectId")));
						map.put("LotName", c.getString(c.getColumnIndex("LotName")));
						map.put("LotCode", c.getString(c.getColumnIndex("LotCode")));
						map.put("CompactionUnit", c.getString(c.getColumnIndex("CompactionUnit")));
						map.put("SupervisorUnit", c.getString(c.getColumnIndex("SupervisorUnit")));
						map.put("ProjectName", c.getString(c.getColumnIndex("ProjectName")));
						map.put("ProjectCount", c.getString(c.getColumnIndex("ProjectCount")));
						map.put("ProjectArea", c.getString(c.getColumnIndex("ProjectArea")));
						map.put("StationArea", c.getString(c.getColumnIndex("StationArea")));
						map.put("attribute", "text");
						list.add(map);
					}
				} catch(Exception e) {} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c0 != null) {
				c0.close();
			}
			closeDBHelper();
		}
		return list;
	}
	
	/**
	 * @category 检测点上传数据表查询
	 * @return List
	 */
	public ArrayList<HashMap<String, String>>  queryTableMonitorPointUpload(String userId,String monitorPointId){
		ArrayList<HashMap<String, String>>  mListMonitorPointUpload=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase   db=dbHelpe.getWritableDatabase(); 
		Cursor c=  db.query("MonitorPointUpload", null, "UserId=? and MonitorPointId=?", new String[]{userId,monitorPointId}, null, null, null);
		try{
			while(c.moveToNext()){
				HashMap<String, String>  map=new HashMap<String, String>();
				map.put("monitorPointId", c.getString(c.getColumnIndex("MonitorPointId"))); //监测点Id
				map.put("monitorPointName", c.getString(c.getColumnIndex("MonitorPointName")));
				map.put("monitorProjectId", c.getString(c.getColumnIndex("MonitorProjectTypeId")));
				map.put("monitorProjectTypeName", c.getString(c.getColumnIndex("MonitorProjectTypeName")));
				map.put("monitorTypeId", c.getString(c.getColumnIndex("MonitorTypeId")));
				map.put("monitorTypeName", c.getString(c.getColumnIndex("MonitorTypeName")));
				map.put("pactId", c.getString(c.getColumnIndex("PactId")));
				map.put("pactName", c.getString(c.getColumnIndex("PactName")));
				map.put("monitorValue", c.getString(c.getColumnIndex("MonitorValue"))); //本次监测值
				map.put("superviseDate", c.getString(c.getColumnIndex("SuperviseDate")));
				map.put("uploadDate", c.getString(c.getColumnIndex("UploadDate")));
				map.put("uploadUser", c.getString(c.getColumnIndex("UploadUser")));
				map.put("uploadState", c.getString(c.getColumnIndex("UploadState")));
				map.put("uploadMark", c.getString(c.getColumnIndex("UploadMark")));
				map.put("monitorDeep", c.getString(c.getColumnIndex("Monitordeep")));//埋深
				mListMonitorPointUpload.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return mListMonitorPointUpload;
	
	}
	/**
	 * @category 检测点上传数据表查询
	 * @return List
	 */
	public ArrayList<HashMap<String, String>>  queryTableMonitorPointUpload(String monitorPointId,String monitorTypeId,String uploadMark){
		ArrayList<HashMap<String, String>>  mListMonitorPointUpload=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase   db=dbHelpe.getWritableDatabase();
		Cursor c=  db.query("MonitorPointUpload", null, "MonitorPointId=? and MonitorTypeId=? and  UploadMark=?", new String[]{monitorPointId,monitorTypeId,uploadMark}, null, null, null);
		try{
			while(c.moveToNext()){
				HashMap<String, String>  map=new HashMap<String, String>();
				map.put("monitorPointId", c.getString(c.getColumnIndex("MonitorPointId"))); //监测点ID
				map.put("monitorPointName",c.getString(c.getColumnIndex("MonitorPointName")));  //监测点名称
				map.put("monitorProjectId", c.getString(c.getColumnIndex("MonitorProjectTypeId")));
				map.put("monitorProjectTypeName", c.getString(c.getColumnIndex("MonitorProjectTypeName")));
				map.put("monitorTypeId", c.getString(c.getColumnIndex("MonitorTypeId")));
				map.put("monitorTypeName", c.getString(c.getColumnIndex("MonitorTypeName")));
				map.put("pactId", c.getString(c.getColumnIndex("PactId")));
				map.put("pactName", c.getString(c.getColumnIndex("PactName")));
				map.put("monitorValue", c.getString(c.getColumnIndex("MonitorValue"))); //本次监测值
				map.put("superviseDate", c.getString(c.getColumnIndex("SuperviseDate")));
				map.put("uploadDate", c.getString(c.getColumnIndex("UploadDate")));
				map.put("uploadUser", c.getString(c.getColumnIndex("UploadUser")));
				map.put("uploadState", c.getString(c.getColumnIndex("UploadState")));
				map.put("uploadMark", c.getString(c.getColumnIndex("UploadMark")));
				map.put("monitorDeep", c.getString(c.getColumnIndex("Monitordeep")));
				mListMonitorPointUpload.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return mListMonitorPointUpload;
		
	}

	/**
	 * @category插入监测点数据表
	 * @TableName  MonitorPointUpload	 上传表
	 * @param monitorProjectTypeId         检测类型标题Id  
	 * @param monitorProjectTypeName	   检测类型标题名称
	 * @param MonitorTypeId  						检测类型Id
	 * @param MonitorTypeName 			   检测类型名称
	 * @param pactId									合同段ID
	 * @param pactName								合同段名称
	 * @param changNumber						本次变化量
	 * @param   monitorValue
	 * @param superviseDate						检测时间
	 * @param uploadDate							 上传时间
	 * @param UploadingUser						  上传人
	 * @param uploadState
	 * @param 	uploadMark 			
	 * @param  monitorDeep 						埋深		
	 * @return
	 */
	public boolean insertTableMonitorPointUpload(String userId,String monitorPointId,String monitorPointName,String monitorProjectTypeId,
			String monitorProjectTypeName, String MonitorTypeId,
			String monitorTypeName, String PactId, String pactName,
			String monitorValue,String superviseDate, String uploadDate,
			String UploadingUser,String uploadMark,String monitorDeep) {
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("UserId", userId);
		values.put("MonitorPointId", monitorPointId);
		values.put("MonitorPointName", monitorPointName); //监测点名称
		values.put("MonitorProjectTypeId", monitorProjectTypeId);
		values.put("MonitorProjectTypeName", monitorProjectTypeName);
		values.put("MonitorTypeId", MonitorTypeId);
		values.put("MonitorTypeName", monitorTypeName);
		values.put("PactId", pactName);
		values.put("PactName", PactId);
		values.put("MonitorValue", monitorValue);
		values.put("SuperviseDate", superviseDate); //监测时间
		values.put("uploadDate", uploadDate);		  //上传时间
		values.put("UploadUser", UploadingUser);
		values.put("UploadMark", uploadMark);
		values.put("MonitorDeep", monitorDeep);//埋深
		long result = db.insert("MonitorPointUpload", "", values);
		if (result == -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * @category查询上传的状态值 MonitorValue
	 * @param monitorTypeId
	 * @param uploadMark
	 * @return String
	 */
	public String queryTableMonitorPointUploadState(String monitorTypeId,String uploadMark){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonitorPointUpload", new String[]{"UploadState"}, "MonitorTypeId=? and UploadMark=?", new String[]{monitorTypeId,uploadMark}, null, null, null);
		try{
			if(c.moveToNext()){
				 return	c.getString(c.getColumnIndex("UploadState"));
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * @category查询监测值
	 * @param monitorTypeId
	 * @param MonitorValue
	 * @return String
	 */
	public String queryTableMonitorPointUploadMonitorValue(String monitorTypeId,String monitorValue){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonitorPointUpload", new String[]{"MonitorValue"}, "MonitorTypeId=? and UploadMark=?", new String[]{monitorTypeId,monitorValue}, null, null, null);
		try{
			if(c.moveToNext()){
				return	c.getString(c.getColumnIndex("MonitorValue"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * @category删除记录
	 * @param monitorTypeId
	 * @param uploadMark
	 * @return
	 */
	public boolean delTableMonitorPointUploadData(String monitorTypeId,String uploadMark){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{			
			db.delete("MonitorPointUpload", "MonitorTypeId=? and UploadMark=?", new String[]{monitorTypeId,uploadMark});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
/**
 * @category 更新上传状态
 * @param monitorTypeId
 * @param uploadMark
 * @param superviseDate 监测时间
 * @return
 */
	public boolean updateUploadState(String monitorTypeId,String uploadMark,String uploadDate ){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("UploadState", "1");
			values.put("UploadDate", uploadDate);
			db.update("MonitorPointUpload", values, "MonitorTypeId=? and UploadMark=?", new String[]{monitorTypeId,uploadMark});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null){
				db.close();
			}
		}

		return true;
	}

	/**
	 * @category更新监测值和监测时间
	 * @param monitorTypeId 
	 * @param uploadMark
	 * @param monitorValue 监测值
	 * @param monitorDate 监测时间
	 * @return
	 */
	public boolean  updateUploadInfo(String monitorTypeId,String uploadMark,String monitorValue,String monitorDate){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("MonitorValue", monitorValue);
			values.put("SuperviseDate", monitorDate);
			db.update("MonitorPointUpload", values, "MonitorTypeId=? and uploadMark=?",new String[]{monitorTypeId,uploadMark});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/** --------------------------------------------WeekPlan-周计划------------------------*/
	
	/**
	 * @category查询状态值
	 * @param weekPlanId	周期Id
	 * @return
	 */
	public  String queryTableWeekPlanRemark(String weekPlanId){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("WeekPlan",new String[]{"Remark"},"WeekPlanId=?",new String[]{weekPlanId},null,null,null);
		try{
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("Remark"));
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return "";
	}
	/**
	 * @categor更新备注信息
	 * @param weekPlanId 周期计划Id
	 * @param remark 备注
	 * @param remainDay剩余工期天数
	 * @return
	 */
	public boolean updateTableWeekPlanRemark(String weekPlanId,String remark,String remainDay){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("Remark", remark);
			values.put("RemainDay", remainDay);
			db.update("weekPlan", values, "WeekPlanId=?", new String[]{weekPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category查询周计划表中的状态
	 * @param id周计划表中的id
	 * @return
	 */
	public  String queryTableWeekPlanState(String id){
		SQLiteDatabase db=new DBHelp().getWritableDatabase();
		Cursor c=db.query("WeekPlan", new String[]{"State"}, "WeekPlanId=?", new String[]{id}, null, null, null);
		try{
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("State"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return null;
	}
	
	/**
	 * @category周计划上报数据表
	 * <p>插入没有完成的周
	 * @param list
	 * @return
	 */
	public boolean insertTableWeekPlan(ArrayList<HashMap<String, String>> list,String periodId,String lotId, String mUserId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		Cursor c1=null;
		try{
				for(HashMap<String, String> map : list){
			    	c =db.query("WeekPlan", null, "UserId=? and  lotId=? and PeriodId=?  and WeekPlanId=? and State=?",
			    	new String[]{mUserId,lotId,periodId,map.get("newId"),"0"}, null, null, null);
			    	c1=db.query("WeekPlan", null, "UserId=? and  lotId=? and PeriodId=?  and WeekPlanId=? and State<>?",
					    	new String[]{mUserId,lotId,periodId,map.get("newId"),"0"}, null, null, null);
				 	if(c.moveToNext()){
				 		db.delete("WeekPlan", "UserId=? and  lotId=? and PeriodId=?  and WeekPlanId=? and State=?",
						    	new String[]{mUserId,lotId,periodId,map.get("newId"),"0"});
				 	}
				 	if(c1.moveToNext()){
				 		
				 	}else{				 		
				 		ContentValues  values=new ContentValues();
				 		values.put("PeriodId", periodId);
				 		values.put("LotId", lotId);
				 		values.put("WeekPlanId", map.get("newId"));
				 		values.put("CycleName", map.get("cycleName"));
				 		values.put("StartDate", map.get("startDate"));
				 		values.put("EndDate", map.get("endDate"));
				 		values.put("CycleType", map.get("cycleType"));
				 		values.put("UserId", mUserId);
				 		db.insert("WeekPlan", "", values);
				 	}
				}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			if(c!=null){
				c.close();
			}
			if(c1!=null){
				c1.close();
			}
			closeDBHelper();
		}
		return true;
	}
	
	
	/**
	 * @category查询周计划数据表是否有指定行数据存在,
	 * @param LotId
	 * @return  boolean
	 */
	public boolean  queryTableWeekPlanEmptyData(String lotId, String mUserId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("WeekPlan", null, "LotId=? AND UserId=?", new String[]{lotId, mUserId}, null, null, null);
			try{
				if(c.moveToNext()){
					return true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(c!=null){
					c.close();
				}
				if(db!=null){
					db.close();
				}
			}
		
		return false;
	}
	/**
	 * 删除未完成的信息
	 * 
	 */
	public boolean delTableWeekPlan(String lotId,String userId,String weekPlanId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("WeekPlan", "LotId=? and UserId=? and State=?",new String[]{lotId,userId,"0"});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category清空周期计划表中的数据
	 * @return
	 */
	public  boolean clearTableWeekPlanData(){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("WeekPlan", null, null);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category指定周计划Id删除数据
	 * @param 周计划Id
	 * @return boolean
	 */
	public boolean delTableWeekPlanData(String weekPlanId, String UserId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("WeekPlan", "WeekPlanId=? AND UserId=?", new String[]{weekPlanId,UserId});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category获取周期计划表的数据
	 * @param lotId
	 * @return list
	 */
	public  ArrayList<HashMap<String,String>> queryTableWeekPlan(String lotId,String  mUserId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("WeekPlan", null, "LotId=? AND UserId=?", new String[]{lotId,mUserId}, null, null, null);
		try{
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				   map.put("_Id",c.getString(c.getColumnIndex("_Id")));
				   map.put("weekPlanId",c.getString(c.getColumnIndex("WeekPlanId")));
				   map.put("UserId",c.getString(c.getColumnIndex("UserId")));
				   map.put("CommonInfo",c.getString(c.getColumnIndex("CycleName")));
				   map.put("startDate", c.getString(c.getColumnIndex("StartDate")));
				   map.put("endDate",c.getString(c.getColumnIndex("EndDate")));
				   map.put("cycleType",c.getString(c.getColumnIndex("CycleType")));
				   if("".equals(c.getString(c.getColumnIndex("Remark"))) || c.getString(c.getColumnIndex("Remark"))==null){
					   map.put("remark","");
				   }else{					   
					   map.put("remark",c.getString(c.getColumnIndex("Remark")));
				   }
				   if("".equals(c.getString(c.getColumnIndex("RemainDay"))) || c.getString(c.getColumnIndex("RemainDay"))==null){
					   map.put("remainDay", "");
				   }else{					   
					   map.put("remainDay", c.getString(c.getColumnIndex("RemainDay")));
				   }
				   map.put("State",c.getString(c.getColumnIndex("State")));
				   arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}
	/**
	 * WeekPlan 状态- 更新
	 * @param list  
	 * @author wanghb
	 * @date 2014/07/10
	 * @return boolean
	 */
	public boolean updateWeekPlanState(String id, String type, String mUserId) {
		try {
			String sql = String.format("UPDATE WeekPlan SET State=%s WHERE WeekPlanId='%s' AND UserId='%s'",type, id, mUserId);
			dbHelpe.getWritableDatabase().execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	
	
	/**
	 * @category插入周期日期表
	 * @param lotId 	合同段Id
	 * @param weekPlanId 周期计划Id
	 * @param dayDate	 	 日期
	 * @param state			 完成状态
	 * @return				
	 */
	public boolean  insertTableWeekPlanDay(String lotId,String weekPlanId,String dayDate, String dataType, String mUserId){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		try{			
			ContentValues values=new ContentValues();
			values.put("LotId", lotId);
			values.put("WeekPlanId", weekPlanId);
			values.put("DayDate", dayDate);
			values.put("DataType", dataType);
			values.put("UserId", mUserId);
			db.insert("WeekPlanDay", "", values);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	public boolean delTableWeekPlanDay(String userId,String weekPlanId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("WeekPlanDay", "UserId=? and WeekPlanId=?", new String[]{userId,weekPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category查询周期计划日期表
	 * @param periodId
	 * @param weekPanId
	 * @return  boolean
	 */
	public  boolean queryTableWeekPanDay(String lotId,String weekPlanId, String mUserId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor 	c = db.query("WeekPlanDay", null, "LotId=? and weekPlanId=? AND UserId=?", new String[]{lotId,weekPlanId,mUserId}, null, null, null);
		try{
			if(c.moveToNext()){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
	   return false;
	}
	
	
	/**
	 * 查询WeekPanDay当前周的各项的是否已全部完成
	 * @param periodId
	 * @param weekPlanId
	 * @return
	 */
	public  boolean selectWeekPanDayState(String lotId,String weekPlanId, String mUserId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor 	c = db.query("WeekPlanDay", null, "LotId=? and weekPlanId=? AND UserId=?", new String[]{lotId, weekPlanId, mUserId}, null, null, null);
		try{
			while(c.moveToNext()){
				String mState = c.getString(c.getColumnIndex("State"));
				if(mState != null && "0".equals(mState)) {
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
				c = null;
			}
			closeDBHelper();
		}
	   return true;
	}
	
	
	
	
	/**
	 * @category周期计划周期表所有数据查询
	 * @param periodId 期数Id
	 * @param weekPanId 周期计划Id
	 * @return	 ArrayList
	 */
	public  ArrayList<HashMap<String, String>> queryTableWeekPanDayAllInfo(String lotId,String weekPlanId, String mUserId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("WeekPlanDay", null, "LotId=? and weekPlanId=?", new String[]{lotId,weekPlanId}, null, null, null);
		try{
				while(c.moveToNext()){
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("_Id",c.getString(c.getColumnIndex("_Id")));
					map.put("lotId",c.getString(c.getColumnIndex("LotId")));
					map.put("weekPlanId",c.getString(c.getColumnIndex("WeekPlanId")));
					map.put("dayDate",c.getString(c.getColumnIndex("DayDate")));
					map.put("state", c.getString(c.getColumnIndex("State")));
					map.put("DataType", c.getString(c.getColumnIndex("DataType")));
					arrayList.add(map);
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
		}
	
/**
 * @category修改周期计划日期表中状态
 * @param id 
 * @param state 状态值
 * @lastMt 2014/07/10
 * @return boolean
 */
public boolean updateTableWeekPanDayState(String id,String state){
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("State", state);
			db.update("WeekPlanDay", values, "_Id=?", new String[] { id });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return true;
	}

public boolean updateTableWeekPlanDayState(String userId,String lotId,String state){
	SQLiteDatabase  db=dbHelpe.getWritableDatabase();
	try{
		ContentValues values=new ContentValues();
		values.put("State", state);
		db.update("WeekPlanDay", values, "UserId=? and LotId=?", new String[]{userId,lotId});
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(db!=null){
			db.close();
		}
	}
	return true;
}
	/**
	 * 查询进度计划上传信息
	 * @param map
	 * @author wanghb
	 * @date 未知
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> selectProgressUpdateInfo(String weekPlanId, String type, String mUserId) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		String sql = String.format("SELECT * FROM ProgressUpdateInfo WHERE WeekPlanId='%s' and ItemsType=%s AND UserId='%s'", weekPlanId, type, mUserId);
		Cursor c =  null;
		try {
			 c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while (c.moveToNext()) {
				String info = c.getString(c.getColumnIndex("RealDay"));
				if (!"".equals(info) && !"".equals(c.getString(c.getColumnIndex("ValueContent")))) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("type", info);
					map.put("itemsId", c.getString(c.getColumnIndex("ItemsId")));
					map.put("value",c.getString(c.getColumnIndex("ValueContent")));
					arrayList.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return arrayList;
	}
	/**
	 * 查询进度计划施工项上传信息
	 * @param map
	 * @author wanghb
	 * @date 未知
	 * @return ArrayList<HashMap<String, String>>
	 */
	public HashMap<String, ArrayList<HashMap<String,String>>> selectProgressUpdateInfoItems(String weekPlanId, String mUserId) {
		HashMap<String, ArrayList<HashMap<String,String>>> allMap = new HashMap<String, ArrayList<HashMap<String,String>>>();
		String sql0 = String.format("SELECT ItemsId FROM ProgressUpdateInfo WHERE WeekPlanId='%s' and ItemsType=1 AND UserId='%s' GROUP BY ItemsId", weekPlanId, mUserId);
		Cursor c0 =  null;
		try {
			 c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while (c0.moveToNext()) {
				String mItemsId = c0.getString(0);
				Cursor c =  null;
				String sql = String.format("SELECT * FROM ProgressUpdateInfo WHERE WeekPlanId='%s' AND ItemsType=1 AND ItemsId='%s' AND UserId='%s'", weekPlanId, mItemsId, mUserId);
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
				try {
					c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
					while (c.moveToNext()) {
						if(!"".equals(c.getString(c.getColumnIndex("ValueContent")))){
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("RealDay", c.getString(c.getColumnIndex("RealDay")));
							map.put("ItemsId", c.getString(c.getColumnIndex("ItemsId")));
							map.put("ValueContent",c.getString(c.getColumnIndex("ValueContent")));			
							list.add(map);
						}
					}
				} finally {
					if(c != null) {
						c.close();
						c = null;
					}
				}
				allMap.put(mItemsId, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return allMap;
	}


/**
 * @category查询周期计划日期表中的人员id和值
 * @param weekPlanId
 * @return HashMap<String,String>
 */
//竟然没用到该方法
/*public  ArrayList<HashMap<String, String>> qureyTableWeekPlanDayPersonnel(String  weekPlanId, String mUserId){
	SQLiteDatabase db=dbHelpe.getWritableDatabase();
	ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
	Cursor c=db.query("ProgressUpdateInfo", null, "WeekPlanId=? and ItemsType=? AND UserId=?",new String[]{weekPlanId,"3", mUserId}, null, null, null);
	try{
		while(c.moveToNext()){
			//realDay 这个地方要注意  在表中 WeekPlanDay的字段 RealDay 还有机械和人员的值
			String  info= c.getString(c.getColumnIndex("RealDay"));
			if(!"".equals(info)){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("type", info);
				map.put("itemsId",c.getString(c.getColumnIndex("ItemsId")));
				map.put("value",c.getString(c.getColumnIndex("ValueContent")));
				arrayList.add(map);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(db!=null){
			db.close();
		}	
	}
	return arrayList;
}*/

/** ------------------对月计划MonthPlan表操作----------------*/
	/**
	 * @categorz向月计划表插入数据
	 * @param periodId
	 *            期段Id
	 * @param lotId
	 *            合同段Id
	 * @param monthDate
	 *            每月的时间
	 * @return boolean
	 * @author chenl
	 * @LastMT 2014-07-10 by wangbh
	 * 
	 */
	public boolean insertTableMonthPlan(String userId,String periodId,String lotId,String monthDate) {
		ContentValues values = new ContentValues();
		values.put("PeriodId", periodId);
		values.put("UserId", userId);
		values.put("LotId", lotId);
		values.put("MonthDate", monthDate);
		try {
			if (dbHelpe.getWritableDatabase().insert("MonthPlan", "", values) != -1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDBHelper();
		}
		return false;
	}
/**
 * @category查询指定列的日期数据
 * @param lotId 合同段ID
 * @param monthPlanId
 * @return
 */
	public HashMap<String, String> queryTableMonthPlanDate(String userId,String lotId,String monthPlanId){
		HashMap<String, String>  map=new HashMap<String, String>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonthPlan", null, "UserId=? and LotId=? and  ID=?", new String[]{userId,lotId,monthPlanId}, null, null, null);
		try{
			if(c.moveToNext()){
				String  date=c.getString(c.getColumnIndex("MonthDate"));
				map.put("year", date.substring(0, 4));
				map.put("month", date.substring(date.indexOf("年")+1,date.indexOf("月")));
				map.put("createdTime", c.getString(c.getColumnIndex("CreatedTime")));
			}
		}catch(Exception e){
			e.printStackTrace();
			}finally{
			 if(c!=null){
				 c.close();
			 }
			if(db!=null){
				db.close();
			}
		
		}
		return map;
	}
	/**
	 * @category查询月计划表中的状态值
	 * @param monthId 月计划Id
	 * @return
	 */
	public String queryTableMonthPlanDate(String userId,String monthId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonthPlan", new String[]{"State"}, "UserId=? and ID=?",new String[]{userId,monthId}, null, null, null);
		try{
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("State"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return null;
	}
/**
 *@category修改月计划表中的状态值
 *@param id 月计划表中的主键Id
 */
public   boolean updateTableMonthPlanState(String id,String state){
	SQLiteDatabase  db=dbHelpe.getWritableDatabase();
	 ContentValues  values=new ContentValues();
	  values.put("State", state);
	try{
		db.update("MonthPlan", values, "ID=?", new String[]{id});			
	}catch(Exception  e){
		e.printStackTrace();
		return false;
	}finally{
		if(db!=null){
			db.close();
		}
	}
	return true;
}
/**
 * @category指定列删除表中的数据
 * @param 主键ID
 * @return  boolean
 */
public boolean delTableMonthPlanRow(String id){
	SQLiteDatabase db=dbHelpe.getWritableDatabase();
	 try{
		 db.delete("MonthPlan", "ID=?", new  String[]{id});		 
	 }catch(Exception e){
		 e.printStackTrace();
		 return false;
	 }finally{
		 if(db!=null){
			 db.close();
		 }
	 }
	 return true;
}
/**
 * @category指定列是有存在数据,
 * @param lotId 合同段Id
 * @param monthDate 传入的日期
 * @return如果存在返回true，不存在返回false
 */
public boolean queryTableMonthPlan(String userId,String lotId,String monthDate){
	SQLiteDatabase db=dbHelpe.getReadableDatabase();
    Cursor  c=db.query("MonthPlan", null, "UserId=? and LotId=? and MonthDate=?", new String[]{userId,lotId,monthDate}, null, null, null);
	try{
		if(c.moveToNext()){
			return true;
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(c!=null){
			c.close();
		}
		if(db!=null){
			db.close();
		}
	}
	return false;
}

/**
 * @category查询月计划中MonthPlan表数据
 * @param 传入的Id
 */
	public ArrayList<HashMap<String, String>> queryMonthPlan(String userId,String mPeriodId, String mLotId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = String.format("SELECT * FROM MonthPlan WHERE UserId='%s' AND PeriodId='%s' AND LotId='%s'", userId,mPeriodId, mLotId);
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", c.getString(c.getColumnIndex("ID")));
				map.put("CommonInfo", c.getString(c.getColumnIndex("MonthDate")));
				map.put("State", c.getString(c.getColumnIndex("State")));
				map.put("createdTime", c.getString(c.getColumnIndex("CreatedTime")));
				map.put("remark", c.getString(c.getColumnIndex("Remark")));
				// if u wanna get more..please add
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}

	/**
	 * @category查询备注信息
	 * @param 月计划Id
	 */
	public String  queryTableMontPlanRemark(String monthId){
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=db.query("MonthPlan", new String[]{"Remark"}, "ID=?", new String[]{monthId}, null, null, null);
		try{
			if(c.moveToNext()){
				return  c.getString(c.getColumnIndex("Remark"));
			}else{
				return "";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
			
		}
		return "";
	}
	/**
	 * @category修改备注信息
	 */
	public  boolean  updateMonthPlanRemark(String monthPlanId,String remark){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			  values.put("Remark", remark);
			  db.update("MonthPlan", values, "ID=?", new String[]{monthPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category查询备注信息
	 */
	public boolean insertMonthPlanRemark(String monthPlanId,String remark){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("Remark",remark);
			db.insert("MonthPlan", "", values);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/** 统计月计划表中行数*/
	public  ArrayList<HashMap<String, String >> getTableMonthPlanNumber(String userId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		try{
			String sql=String.format("select count(*),LotId from MonthPlan where UserId='%s' group by LotId", userId);
			c=db.rawQuery(sql, null);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("dataCount", c.getString(0));
				map.put("lotId", c.getString(1));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}
/** --------------------对月计划周期数表MonthPlanWeekDate操作----------------------------------------*/
	
	

	/**
	 * @category查询月计划周期表所有信息
	 * @param monthPlanId  月计划Id
	 * @return ArrayList
	 */
 public ArrayList<HashMap<String, String>>  queryTableMonthPlanWeekDateAllData(String monthPlanId){
	 ArrayList<HashMap<String, String>>  arrayList=new ArrayList<HashMap<String,String>>();
	 SQLiteDatabase  db=dbHelpe.getReadableDatabase();
	 Cursor c=db.query("MonthPlanWeekDate", null, "MonthPlanId=?", new String[]{monthPlanId}, null, null, null);
	 try{
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			   map.put("weekId", c.getString(c.getColumnIndex("ID")));
			   map.put("dataType", c.getString(c.getColumnIndex("DataType")));
			   map.put("weekDate", c.getString(c.getColumnIndex("WeekDate")));
			   map.put("dataType", c.getString(c.getColumnIndex("DataType")));
			   map.put("state", c.getString(c.getColumnIndex("State")));
			   arrayList.add(map);
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 if(db!=null){
			 db.close();
		 }
	 }
	 return arrayList;
 }
/**
 * @category插入MonthPlanWeekDate表中的数据
 * @param monthPlanId   月计划ID
 * @param weekDate周期数
 * @param dataType数据类型1 日期 2 机械,3 人员
 * @param boolean
 */
	public boolean insertTableMonthPlanWeekDate(String monthPlanId,String weekDate,String dataType){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("MonthPlanId", monthPlanId);
		values.put("WeekDate", weekDate);
		values.put("DataType", dataType);
		try{
			if(db.insert("MonthPlanWeekDate", "", values)!=-1){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	
	public boolean queryTableMonthPlanWeekDateDataEmpty(String id){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonthPlanWeekDate", null, "MonthPlanId=?", new String[]{id}, null, null, null);
		try{
			if(c.moveToNext()){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	
	/**
	 * 修改状态值
	 * @param id 月计划周期Id
	 * @param state 状态值
	 */
	public boolean updateTableMonthPlanWeekDateState(String id,String state){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("State", state);
		try{
			db.update("MonthPlanWeekDate", values, "ID=?",new String[]{id});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	public boolean updateTableMonthPlanWeekDateAllState(String monthPlanId,String state){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("State", state);
		try{
			db.update("MonthPlanWeekDate", values, "MonthPlanId=?", new String[]{monthPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/**
	 * @category删除周期计划表中的数据 
	 * @param monthPlanId
	 * @return
	 */
	public boolean deleteTableMonthPlanWeekDateInfo(String   monthPlanId ){
		SQLiteDatabase	db=dbHelpe.getWritableDatabase();
		try{
			db.delete("MonthPlanWeekDate", "MonthPlanId=?", new String[]{monthPlanId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	/** -----------------------对表ProgressInfo 操作-------------------------*/
	public  boolean queryTableProgressInfoCheckBox(String newId){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("ProgressInfo", null, "NewId=?", new String[]{newId}, null, null, null);
		 try{
			 if(c.moveToNext()){
				 if("true".equals(c.getString(c.getColumnIndex("IsEnable")))){
					 return true;
				 }
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 if(c!=null){
				 c.close();
			 }
			 if(db!=null){
				 db.close();
			 }
		 }
		return false;
	}

	
	/**  --------------对月计划上传表操作MonthPlanUpload----------------------*/
	
	public  boolean deleteTableMonthPlanUpload(String monthPlanId,String lotId){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		try{
			db.delete("MonthPlanUpload", "MonthPlanId=? and LotId=?", new String[]{monthPlanId,lotId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	
	public ArrayList<HashMap<String, String>> queryTableMonthPlanUpload(String lotId,String weekId){
		ArrayList< HashMap<String, String>> arrayList=new  ArrayList<HashMap<String, String>>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonthPlanUpload", null, "LotId=? and WeekId=? ", new String[]{lotId,weekId}, null, null, null);
		try{
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("ItemsId",c.getString(c.getColumnIndex("ItemsId")));
				map.put("ValueContent",c.getString(c.getColumnIndex("Value")));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return  arrayList;
	}
	public boolean insertTableMonthPlanUpload(ArrayList<HashMap<String, String>> list){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		ArrayList<HashMap<String, String>> arrayList=list;
		Cursor c=null;
		try{
			if(arrayList!=null){
				for(HashMap<String, String> map:arrayList){
					c=db.query("MonthPlanUpload", null, "WeekId =? and ItemsId=? and ItemsType=?", new String[]{map.get("weekId"),map.get("itemsId"),map.get("itemsType")}, null, null, null);
				  if(c.moveToFirst()){
					  ContentValues values=new ContentValues();
					  values.put("value", map.get("value"));
					  db.update("MonthPlanUpload", values, "WeekId =? and ItemsId=? and ItemsType=?", new String[]{map.get("weekId"),map.get("itemsId"),map.get("itemsType")});
				  }else{
					  ContentValues values=new ContentValues();
					  values.put("LotId", map.get("lotId"));
					  values.put("MonthPlanId", map.get("monthPlanId"));
					  values.put("WeekId", map.get("weekId"));  		//每一周的Id
					  values.put("ItemsId", map.get("itemsId"));
					  values.put("ItemsType", map.get("itemsType"));
					  values.put("WeekNum", map.get("weekNum")); //周期数,有可能为机械和人员
					  values.put("Value", map.get("value"));
					  db.insert("MonthPlanUpload", "", values);
				  }
				  }
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
			c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return true;
	}
	
	/**
	 * @category 根据月计划Id得到所有要上传的信息
	 * @param lotId		 合同段Id
	 * @param monthId  月计划Id
	 * @param itemsType 类型Id
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryTableMonthPlanUploadItemsId(String lotId,String monthPlanId,String itemsType){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		Cursor  c= db.query(true, "MonthPlanUpload", null, "LotId=? and MonthPlanId=? and ItemsType=?", new String[]{lotId,monthPlanId,itemsType}, "ItemsId", null, null, null);
			try{
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("itemsId", c.getString(c.getColumnIndex("ItemsId")));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
			return arrayList;
	}
	
	public ArrayList<HashMap<String, String>> queryMonthPlanUploadItemsId(String lotId,String monthPlanId,String itemsType){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		Cursor  c= db.query(true, "MonthPlanUpload", null, "LotId=? and MonthPlanId=? and ItemsType=?", new String[]{lotId,monthPlanId,itemsType}, null, null, null, null);
		try {
			while (c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("itemsId", c.getString(c.getColumnIndex("ItemsId")));
				map.put("value", c.getString(c.getColumnIndex("Value")));
				arrayList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return arrayList;
	}
	
	/**
	 * @categoryg
	 * @param lotId
	 * @param monthPlanId
	 * @param itemsId
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryTableMonthPlanUploadInfo(String lotId,String monthPlanId,String itemsId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("MonthPlanUpload", null, "LotId=? and MonthPlanId=? and ItemsId=?", new String[]{lotId,monthPlanId,itemsId},null, null, null);
		try{
			while(c.moveToNext()){
				HashMap<String, String>   map = new HashMap<String, String>();
				//map.put("weekNum",weekNum.substring(weekNum.indexOf("第")+1, weekNum.indexOf("周")));
				map.put("weekNum",c.getString(c.getColumnIndex("WeekNum")));
				map.put("value", c.getString(c.getColumnIndex("Value")));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}

	/**
	 * 完成量日期列表查询
	 * @param userId
	 * @param dataType 数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	 * @author wanghb
	 * @date 2014/07/16
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getCompletionDateList(String userId, String dataType, String mLotId) {
		ArrayList<HashMap<String, String>>   arrayList=new ArrayList<HashMap<String,String>>();
		String sql = String.format("SELECT * FROM CompletionDate WHERE UserId='%s' AND DateType=%s AND LotId='%s'", userId,dataType,mLotId);
		Cursor c = null;
		try{
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("UserId", userId);
				map.put("LotId", c.getString(c.getColumnIndex("LotId")));
				map.put("CommonInfo", c.getString(c.getColumnIndex("DateInfo")));
				
				map.put("WeekId", c.getString(c.getColumnIndex("WeekId")));
				map.put("startDate", c.getString(c.getColumnIndex("WeekStartDate")));
				map.put("endDate", c.getString(c.getColumnIndex("WeekEndDate")));
				
				map.put("Reason", c.getString(c.getColumnIndex("Reason")));
				map.put("Question", c.getString(c.getColumnIndex("Question")));
				map.put("Remark", c.getString(c.getColumnIndex("Remark")));
				map.put("DateType", c.getString(c.getColumnIndex("DateType")));
				map.put("State", c.getString(c.getColumnIndex("State")));
				arrayList.add(map);
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(c!=null){
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return arrayList;
	}
	
	/** 
	 * 统计完成量数据
	 * @param dataType 数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	 * */
	public  ArrayList<HashMap<String, String >> getTableCompletion(String userId,String dataType){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		try{
			String sql=String.format("select count(*),LotId from CompletionDate where UserId='%s' AND DateType=%s group by LotId", userId,dataType);
			c=db.rawQuery(sql, null);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("dataCount", c.getString(0));
				map.put("lotId", c.getString(1));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}
	/**
	 * 完成量日完成量，月完成量列表新增
	 * @param map
	 * @author wanghb
	 * @date 2014/07/16
	 * @return ArrayList<HashMap<String, String>>
	 * @DateType 数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	 */
	public boolean insertCompletionDate(HashMap<String, String> map) {
		ContentValues values = new ContentValues();
		values.put("UserId", map.get("UserId"));
		values.put("LotId", map.get("LotId"));
		values.put("DateInfo", map.get("DateInfo"));
		values.put("DateType", map.get("DateType"));
		try{
			dbHelpe.insert(values, "CompletionDate");
		} catch (Exception e){
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 完成量日期列表查询
	 * @param userId
	 * @param mLotId 合同段ID
	 * @param dataType  数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	 * @param mSameValue 值
	 * @author wanghb
	 * @date 2014/07/17
	 * @return boolean
	 */
	public boolean isExistSameCompletion(String userId,String mLotId, String dataType,String mSameValue) {
		String sql = String.format("SELECT COUNT(1) FROM CompletionDate WHERE UserId='%s' AND DateType=%s AND LotId='%s' AND DateInfo='%s'", userId, dataType, mLotId, mSameValue);
		Cursor c = null;
		try{
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				if(c.getInt(0) != 0) {
					return true;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(c!=null){
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return false;
	}
	/**
	 * 完成量日期列表修改问题，备注，原因.以及状态值的修改
	 * @param id  主键id
	 * @param content 修改内容
	 * @param editType  修改类型
	 * @param mSameValue 值
	 * @author wanghb
	 * @date 2014/07/18 
	 * @return boolean
	 */
	public boolean updateCompletionDate(String id, String content, int editType) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE CompletionDate SET ");
		switch (editType) {
		case OthersFragment.TYPE_QUESTION_EDIT:
			builder.append("Question='");
			builder.append(content);
			builder.append("'");
			break;
		case OthersFragment.TYPE_REASON_EDIT:
			builder.append("Reason='");
			builder.append(content);
			builder.append("'");
			break;
		case OthersFragment.TYPE_REMARK_EDIT:
			builder.append("Remark='");
			builder.append(content);
			builder.append("'");
			break;
		case OthersFragment.TYPE_STATE:
			builder.append("State=");
			builder.append(content);
			break;
		case OthersFragment.TYPE_ALL:
			builder.append(content);
			break;
		
		default:
			break;
		}
		builder.append(" WHERE ID=");
		builder.append(id);
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 完成量日期列表查询按id
	 * @param id  主键id
	 * @param content 修改内容
	 * @param editType  修改类型
	 * @param mSameValue 值
	 * @author wanghb
	 * @date 2014/07/18 11:17
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getCompletionDateById(String id) {
		String sql = String.format("SELECT * FROM CompletionDate WHERE ID=%s", id);	
		Cursor c = null;
		try{
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", id);
				map.put("UserId", c.getString(c.getColumnIndex("UserId")));
				map.put("LotId", c.getString(c.getColumnIndex("LotId")));
				map.put("CommonInfo", c.getString(c.getColumnIndex("DateInfo")));
				map.put("Reason", c.getString(c.getColumnIndex("Reason")));
				map.put("Question", c.getString(c.getColumnIndex("Question")));
				map.put("Remark", c.getString(c.getColumnIndex("Remark")));
				map.put("DateType", c.getString(c.getColumnIndex("DateType")));
				map.put("State", c.getString(c.getColumnIndex("State")));
				return map;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(c!=null){
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return null;
	}
	
	/**
	 * 插入CompletionUpdateInfo
	 * @param map
	 * @author wanghb
	 * @date 2014/07/17
	 * @return boolean
	 */
	public boolean insertCompletionUpdateInfo(ArrayList<HashMap<String, String>> list) {
		try {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					ContentValues values = new ContentValues();
					values.put("UserId", map.get("UserId"));
					values.put("WeekPlanId", map.get("WeekPlanId"));
					values.put("CompletionDateId", map.get("CompletionDateId"));
					
					values.put("DataType", map.get("DataType"));
					values.put("ItemsId", map.get("ItemsId"));
					values.put("ItemsType", map.get("ItemsType"));
					values.put("ValueContent", map.get("ValueContent"));
					try {
						dbHelpe.insert(values, "CompletionUpdateInfo");
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 查询进度上报上传信息
	 * @param map
	 * @author wanghb
	 * @date 2014-07-18 14:51
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> selectCompletionUpdateInfo(String mCompletionDateId, String mItemsType, String mDataType, String mUserId) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		String sql = String.format("SELECT * FROM CompletionUpdateInfo WHERE CompletionDateId='%s' AND ItemsType=%s AND DataType=%s AND UserId='%s'", mCompletionDateId, mItemsType, mDataType, mUserId);
		Cursor c =  null;
		try {
			 c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while (c.moveToNext()) {
					HashMap<String, String> map = new HashMap<String, String>();
					//map.put("type", c.getString(c.getColumnIndex("RealDay")));
					map.put("ItemsId", c.getString(c.getColumnIndex("ItemsId")));
					map.put("ValueContent",c.getString(c.getColumnIndex("ValueContent")));
					arrayList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return arrayList;
	}
	/**
	 * 删除进度上报上传信息
	 * @param id
	 * @author wanghb
	 * @date 2014-07-18 16:37
	 * @return boolean
	 */
	public boolean deleteCompletionById(String id) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM CompletionDate WHERE ID=%s", id));
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM CompletionUpdateInfo WHERE CompletionDateId=%s", id));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 进度上报(完成量)检查项检查数据表 - 查询
	 * @param list  
	 * @author wanghb
	 * @date 2014/07/19 11:40
	 * @return boolean
	 */
	public ArrayList<HashMap<String, String>> getCompletionUpdateInfoList(String mCompletionDateId, String mUserId) {
		String sql = String.format("SELECT ID,ItemsId,ValueContent FROM CompletionUpdateInfo WHERE CompletionDateId='%s' AND UserId='%s'", mCompletionDateId, mUserId);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("ItemsId", c.getString(c.getColumnIndex("ItemsId")));
				map.put("ValueContent",c.getString(c.getColumnIndex("ValueContent")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	/**
	 * 完成量日期列表新增周完成量
	 * @param list 每次更获取的周完成量
	 * @param mLotId
	 * @param mUserId
	 * @author wanghb
	 * @date 2014/07/19 14:21
	 * @return ArrayList<HashMap<String, String>>
	 * @DateType 数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	   {newId=b66c0f50-87be-40d2-8e33-bbe06f4b2fa2, startDate=2014-04-26, cycleType=1, endDate=2014-05-03, cycleName=4}
	 */
	public boolean insertWeekCompletionDate(ArrayList<HashMap<String, String>> list, String mLotId, String mUserId) {
		try {

		for(HashMap<String, String> map : list) {
			if(map != null) {
				String sql = String.format("SELECT * FROM CompletionDate WHERE LotId='%s' AND UserId='%s' AND WeekId='%s'  and DateType='%s' and State<>'%s'", mLotId, mUserId, map.get("newId"),"2","0");
				Cursor c = null;
				try {
					c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
					if(c.moveToNext()) {
					}else{						
						ContentValues values = new ContentValues();
						values.put("UserId", mUserId);
						values.put("LotId", mLotId);
						values.put("DateInfo", map.get("cycleName"));
						values.put("DateType", "2");
						values.put("WeekId", map.get("newId"));
						values.put("WeekStartDate", map.get("startDate"));
						values.put("WeekEndDate", map.get("endDate"));
						dbHelpe.insert(values, "CompletionDate");
					}
						
				} catch (Exception e){
					e.printStackTrace();
					return false;
				} finally {
					if(c != null) {
						c.close();
						c = null;
					}
				}
			}
		}
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/** 删除表周完成量未完成的值
	 *
	 * */
	public boolean delTableCompletionDate(String userId,String lotId,String state){
		SQLiteDatabase  db=dbHelpe.getWritableDatabase();
		try{
			db.delete("CompletionDate", "UserId=? and LotId=? and DateType=?  and State=?", new String[]{userId,lotId,"2","0"});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
				if(db!=null){
					db.close();
			}
		}
		return true;
	}
	/**
	 * 插入单位性质
	 * @param list
	 * @author wanghb
	 * @date 2014/08/05 13:21
	 * @return
	 */
	public boolean insertDepartmentNature(ArrayList<HashMap<String, String>> list) {
		try {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					ContentValues values = new ContentValues();
					values.put("NewId", map.get("NewId"));
					values.put("SysId", map.get("SysId"));
					values.put("DType", map.get("DType"));
					values.put("DName", map.get("DName"));
					values.put("DValue", map.get("DValue"));
					values.put("DSort", map.get("DSort"));
					try {
						dbHelpe.insert(values, "DepartmentNature");
					} catch (Exception e) {
						return false;
					}
				}
			}
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 查询单位性质
	 * @param userId
	 * @author wanghb
	 * @date 2014/08/05 13:32
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getDepartmentNatureList(String userId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT * FROM DepartmentNature";
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				//userId
				map.put("NewId", c.getString(c.getColumnIndex("NewId")));
				//map.put("SysId", c.getString(c.getColumnIndex("SysId")));
				//map.put("DType",c.getString(c.getColumnIndex("DType")));
				map.put("DName",c.getString(c.getColumnIndex("DName")));
				//map.put("DValue",c.getString(c.getColumnIndex("DValue")));
				//map.put("DSort",c.getString(c.getColumnIndex("DSort")));
				list.add(map);
			}
		} catch (Exception e){
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	
	
	
	
	public String getDepartmentNatureByName() {
		String sql = "SELECT * FROM DepartmentNature WHERE DName='检测单位'";
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToNext()) {
				//userId
				return c.getString(c.getColumnIndex("NewId"));
				//map.put("SysId", c.getString(c.getColumnIndex("SysId")));
				//map.put("DType",c.getString(c.getColumnIndex("DType")));
				//map.put("DName",c.getString(c.getColumnIndex("DName")));
				//map.put("DValue",c.getString(c.getColumnIndex("DValue")));
				//map.put("DSort",c.getString(c.getColumnIndex("DSort")));
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return null;
	}
	/**
	 * 获取检测单位
	 * @author Rubert
	 * @param userId
	 * @date 2014/08/15
	 * @return boolean
	 */
	public ArrayList<HashMap<String, String>> getTestCompanyList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT c.DeptName,c.DptId FROM DepartmentNature d, Contacts c WHERE d.NewId=c.CompanyId AND d.DName='检测单位' GROUP BY c.DeptName";
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("DeptName", c.getString(c.getColumnIndex("DeptName")));
				map.put("DptId",c.getString(c.getColumnIndex("DptId")));
				list.add(map);
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	
	
	/**
	 * 删除单位性质
	 * @param userId
	 * @date 2014/08/05 13:46
	 * @return boolean
	 */
	public boolean delDepartmentNature(String mUserid){
		try{
			dbHelpe.getWritableDatabase().execSQL("DELETE FROM DepartmentNature");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 插入通讯录
	 * @param list
	 * @author wanghb
	 * @date 2014/08/05 13:23
	 * @return
	 */
	public boolean insertContacts(ArrayList<HashMap<String, String>> list, String mDptId) {
		try {
			PingyinUtils mPingyinUtils = new PingyinUtils();
			for (HashMap<String, String> map : list) {
				if (map != null) {
					ContentValues values = new ContentValues();
					values.put("UserId", map.get("UserId"));
					values.put("UserName", map.get("UserName"));
					values.put("DeptName", map.get("DeptName"));
					values.put("UserPhone", map.get("UserPhone"));
					values.put("UserEmail", map.get("UserEmail"));
					values.put("UserQQ", map.get("UserQQ"));
					values.put("UserSex", map.get("UserSex"));//true为男 false为女
					values.put("UserNamePinyin", mPingyinUtils.converterToPinYin(map.get("UserName")));
					values.put("DeptNamePinyin", mPingyinUtils.converterToPinYin(map.get("DeptName")));
					values.put("DptId", map.get("DeptId"));
					values.put("CompanyId", mDptId);
					try {
						dbHelpe.insert(values, "Contacts");
					} catch (Exception e) {
						return false;
					}
				}
			}
			mPingyinUtils = null;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	public ArrayList<HashMap<String, String>> getContactsDept(String mCompanyId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = String.format("SELECT DeptName,DptId FROM Contacts WHERE CompanyId='%s' GROUP BY DeptName", mCompanyId);
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("CompanyId", mCompanyId);
				map.put("DptId", c.getString(c.getColumnIndex("DptId")));
				map.put("DeptName", c.getString(c.getColumnIndex("DeptName")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	
	/**
	 * 查询通讯录
	 * @param list
	 * @author wanghb
	 * @date 2014/08/05 13:54
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getContactsList(String mCompanyId, String mOrderBy) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		StringBuilder builder = new StringBuilder("SELECT * FROM Contacts WHERE CompanyId='");
		builder.append(mCompanyId);
		builder.append("'");
		if(mOrderBy != null) {
			builder.append(" ORDER BY ");
			builder.append(mOrderBy);
		}
		//String sql = String.format("SELECT * FROM Contacts WHERE DptId='%s' ", mDptId);
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(builder.toString(), null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("UserId", c.getString(c.getColumnIndex("UserId")));
				map.put("CompanyId", mCompanyId);
				map.put("DptId", c.getString(c.getColumnIndex("DptId")));
				map.put("UserName", c.getString(c.getColumnIndex("UserName")));
				map.put("DeptName", c.getString(c.getColumnIndex("DeptName")));
				map.put("UserPhone",c.getString(c.getColumnIndex("UserPhone")));
				map.put("UserEmail",c.getString(c.getColumnIndex("UserEmail")));
				map.put("UserQQ",c.getString(c.getColumnIndex("UserQQ")));
				map.put("UserSex",c.getString(c.getColumnIndex("UserSex")));
				map.put("UserNamePinyin",c.getString(c.getColumnIndex("UserNamePinyin")));
				map.put("DeptNamePinyin",c.getString(c.getColumnIndex("DeptNamePinyin")));
				map.put("DSelect",c.getString(c.getColumnIndex("DSelect")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	/**
	 * 删除通讯录
	 * @param userId
	 * @date 2014/08/05 13:54
	 * @return boolean
	 */
	public boolean delContacts(String mDptId){
		try {
			 dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM Contacts WHERE CompanyId='%s'", mDptId));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 修改单个通讯录勾选状态
	 * @param Id
	 * @date 2014/08/07
	 * @return boolean
	 */
	public boolean updateContactsSelect(String Id, String state) {
		try {
			 dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE Contacts SET DSelect=%s WHERE ID=%s", state, Id));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 修改某个单位通讯录勾选状态
	 * @param mDptId
	 * @date 2014/08/07
	 * @return
	 */
	public boolean updateContactsAllSelect(String state) {
		try {
			 dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE Contacts SET DSelect=%s", state));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 查询勾选对象
	 * @date 2014/08/07
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getContactsSelectedList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		StringBuilder builder = new StringBuilder("SELECT * FROM Contacts WHERE DSelect=1");
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(builder.toString(), null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("UserId", c.getString(c.getColumnIndex("UserId")));
				map.put("DptId", c.getString(c.getColumnIndex("DptId")));
				map.put("CompanyId", c.getString(c.getColumnIndex("CompanyId")));
				map.put("UserName", c.getString(c.getColumnIndex("UserName")));
				map.put("DeptName", c.getString(c.getColumnIndex("DeptName")));
				map.put("UserPhone",c.getString(c.getColumnIndex("UserPhone")));
				map.put("UserEmail",c.getString(c.getColumnIndex("UserEmail")));
				map.put("UserQQ",c.getString(c.getColumnIndex("UserQQ")));
				map.put("UserSex",c.getString(c.getColumnIndex("UserSex")));
				map.put("UserNamePinyin",c.getString(c.getColumnIndex("UserNamePinyin")));
				map.put("DeptNamePinyin",c.getString(c.getColumnIndex("DeptNamePinyin")));
				map.put("DSelect",c.getString(c.getColumnIndex("DSelect")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	/**---------------------------------照片上传Pic表------------------------------------------*/

	/**
	 * 查询是否有这个文件存在如果有返回true,否则false
	 * @param userId 用户Id
	 * @param newId 上传Id
	 * @param picName //照片名称
	 * @return
	 */
	public boolean queryTablePic(String userId,String newId,String picName){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=db.query("Pic", null, "UserId=? AND NewId=? AND PicName=?", new String[]{userId,newId,picName}, null, null, null);
		try{
			if(c.moveToFirst()){
				return true;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return false;
	}

	public ArrayList<HashMap<String, String>> queryTablePic(String userId,String newId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		try{
		 c=db.query("Pic", null, "UserId=? AND NewId=?", new String[]{userId,newId}, null, null, null);
		 while(c.moveToNext()){
			 HashMap<String, String> map=new HashMap<String, String>();
			 map.put("newId", c.getString(c.getColumnIndex("NewId")));
			 map.put("picName", c.getString(c.getColumnIndex("PicName")));
			 map.put("picPath", c.getString(c.getColumnIndex("PicPath")));
			 map.put("picDate", c.getString(c.getColumnIndex("PicDate")));
			 arrayList.add(map);
		 }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}
	/**
	 * 插入数据到照片表Pic
	 * @param userId 用户Id
	 * @param newId 上传Id
	 * @param PicName 照片名称
	 *@param PicSize 照片大小
	 *@param PicPath 照片路径
	 *@param PicDate  拍照日期
	 *@param PicReName照片重名命,上传字段
	 */
	public void insertTablePic(String userId,String newId,String picName,String picSize,String picPath,String picDate,String picRename) {
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("UserId", userId);
			values.put("NewId", newId);
			values.put("PicName", picName);
			values.put("PicSize",picSize);
			values.put("PicPath", picPath);
			values.put("PicDate", picDate);
			values.put("PicRename", picRename);
			db.insert("Pic", "", values);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null)
				db.close();
		}
	}
	/**
	 * 查询拍照日期和照片的数量
	 * @param userId 用户Id
	 * @param newId 上传Id
	 * <p>
	 * 1.查询Pic表中日期并且去掉重复值
	 * 2.根据日期条件获取照片的数量
	 * </p>
	 * @return 返回的是日期列表
	 */
	public ArrayList<HashMap<String, String>> queryPic(String userId,String newId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		ArrayList<String> arrayListDate=new ArrayList<String>();
		boolean state=true;
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		Cursor c1=null;
	    c=db.query(true, "pic", new String[]{"PicDate"}, "UserId=? AND NewId=?", new String[]{userId,newId}, null, null, null, null); 
		while(c.moveToNext()){
			arrayListDate.add(c.getString(c.getColumnIndex("PicDate")));
		}
		for(String s:arrayListDate){	
			HashMap<String, String> map=new HashMap<String, String>();
			String sql=String.format("select  PicDate,PicState,Count(*) from pic where UserId='%s'  AND NewId='%s' AND picDate='%s'" ,userId,newId,s);
			c1=db.rawQuery(sql, null);
			//遍历所有的状态值,如果还有状态值为0就未上传
			while(c1.moveToNext()){
				if("0".equals(c1.getString(1)))
					state=false;
			}
			if(c1.moveToFirst()){
				map.put("picDate", s);
				map.put("picNumber", String.valueOf(c1.getLong(2)));
				if(state)
					map.put("picState", "1");
				else
					map.put("picState", "0");
			}
			arrayList.add(map);
		}
		 return arrayList;
	}
	/**
	 * 查询未上传的文件列表
	 */
	public  ArrayList<HashMap<String, String>> getPicUpload(String userId,String newId,String picDate){
		ArrayList<HashMap<String, String >> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
	   Cursor c=db.query("Pic", null, "UserId=? AND NewId=? AND PicState=? AND PicDate=?", new String[]{userId,newId,"0",picDate}, null, null,null);
	   try{
		    while(c.moveToNext()){
		    	HashMap<String, String> map=new HashMap<String, String>();
		    	map.put("picName",c.getString(c.getColumnIndex("PicName")));
		    	map.put("picPath", c.getString(c.getColumnIndex("PicPath")));
		    	map.put("picDate", c.getString(c.getColumnIndex("PicDate")));
		    	map.put("picRename", c.getString(c.getColumnIndex("PicRename")));
		    	arrayList.add(map);
		    }
	   }catch(Exception e){
		   e.printStackTrace();
	   }finally{
		   if(c!=null){
			   c.close();
		   }
		   if(db!=null){
			   db.close();
		   }
	   }
	   return arrayList;
	}

	/**
	 * 改变照片上传的状态
	 */
	public void  updatePicState(String userId,String newId,ArrayList<HashMap<String, String>> list){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		ArrayList<HashMap<String, String>> arrayList=list;
		ContentValues values=new ContentValues();
		values.put("PicState", "1");
		try{			
			Iterator<HashMap<String, String>> iterator=arrayList.iterator();
			while(iterator.hasNext()){
				HashMap<String, String> map =iterator.next();
				db.update("Pic", values, "userId=? AND NewID=? AND PicName=?", new String[]{userId,newId,map.get("picName")});
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
	}
	/**
	 * 删除单张照片
	 */
	public void delPic(String userId,String newId,String picName){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("Pic", "UserId=? AND NewId=? AND PicName=?",new String[]{userId,newId,picName});			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null)
				db.close();
		}
	}
	/**
	 * 查询照片名称是否存在
	 */
	public boolean queryPic(String userId,String newId,String picName){
		SQLiteDatabase db=dbHelpe.getWritableDatabase(); 
		Cursor c=null;
		try{
			c=db.query("Pic", new String[]{"PicName"}, "UserId=? AND NewId=? AND PicName=?", new String[]{userId,newId,picName},null ,null, null);
			if(c.moveToNext()){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	/**
	 * 文件命名
	 */
	public void updatePicRename(String userId,String newId,String picName,String picRename){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("PicName", picRename);
			db.update("Pic", values, "UserId=? AND NewId=? AND PicName=?", new String[]{userId,newId,picName});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
	}
	
	/***************************begin 质量-压实度*****************************************/
	/**
	 * 新增
	 */
	public Long insertHarding(HashMap<String, String> map) {
		try{
			ContentValues values=new ContentValues();
			values.put("UserId", map.get("UserId"));
			values.put("LocId", map.get("LocId"));
			values.put("DetectionAreaId", map.get("DetectionAreaId"));
			values.put("ChangeName", map.get("ChangeName"));
			values.put("AreaName", map.get("AreaName"));
			/*values.put("DetectionCompany", map.get("DetectionCompany"));
			values.put("DetectionCompanyId", map.get("DetectionCompanyId"));
			values.put("DetectionTime",map.get("DetectionTime"));
			values.put("Area",map.get("Area"));
			values.put("Machine",map.get("Machine"));
			values.put("Variate",map.get("Variate"));
			values.put("Thickness",map.get("Thickness"));
			values.put("PressThickness",map.get("PressThickness"));
			values.put("ExpContent",map.get("ExpContent"));
			values.put("ExpResult",map.get("ExpResult"));*/
			return dbHelpe.getIdInsert(values, "QualityHarding");
		}catch(Exception e){
			e.printStackTrace();
			return -1L;
		}finally{
			closeDBHelper();
		}
	}
	/**
	 * 查询
	 * @param userId
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getHardingByUserid(String userId, String locId, String DetectionAreaId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(String.format("SELECT * FROM QualityHarding WHERE UserId='%s' AND LocId='%s' AND DetectionAreaId='%s'", userId, locId, DetectionAreaId), null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("UserId", userId);
				map.put("LocId", locId);
				map.put("DetectionAreaId", c.getString(c.getColumnIndex("DetectionAreaId")));
				map.put("CommonInfo", c.getString(c.getColumnIndex("ChangeName")));
				map.put("AreaName", c.getString(c.getColumnIndex("AreaName")));
				map.put("DetectionCompany", c.getString(c.getColumnIndex("DetectionCompany")));
				map.put("DetectionCompanyId",c.getString(c.getColumnIndex("DetectionCompanyId")));
				map.put("DetectionTime",c.getString(c.getColumnIndex("DetectionTime")));
				map.put("Area",c.getString(c.getColumnIndex("Area")));
				map.put("Machine",c.getString(c.getColumnIndex("Machine")));
				map.put("Variate",c.getString(c.getColumnIndex("Variate")));
				map.put("Thickness",c.getString(c.getColumnIndex("Thickness")));
				map.put("PressThickness",c.getString(c.getColumnIndex("PressThickness")));
				map.put("ExpContent",c.getString(c.getColumnIndex("ExpContent")));
				map.put("ExpResult",c.getString(c.getColumnIndex("ExpResult")));
				map.put("State",c.getString(c.getColumnIndex("State")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	/**
	 * 删除
	 */
	public boolean delQualityHarding(String id){
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM QualityHarding WHERE ID=%s", id));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 修改内容
	 */
	public boolean updateQualityHarding(HashMap<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE QualityHarding SET ");
		builder.append("DetectionCompany='");
		builder.append(map.get("DetectionCompany"));
		builder.append("',DetectionCompanyId='");
		builder.append(map.get("DetectionCompanyId"));
		builder.append("',Area='");
		builder.append(map.get("Area"));
		builder.append("',Machine='");
		builder.append(map.get("Machine"));
		builder.append("',DetectionTime='");
		builder.append(map.get("DetectionTime"));
		builder.append("',Variate='");
		builder.append(map.get("Variate"));
		builder.append("',Thickness='");
		builder.append(map.get("Thickness"));
		builder.append("',PressThickness='");
		builder.append(map.get("PressThickness"));
		builder.append("',ExpContent='");
		builder.append(map.get("ExpContent"));
		builder.append("',ExpResult='");
		builder.append(map.get("ExpResult"));
		builder.append("',State=1");
		builder.append(" WHERE ID=");
		builder.append(map.get("ID"));
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 修改状态
	 */
	public boolean updateQualityHardingState(String id, String state) {
		try {
			dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE QualityHarding SET State=%s WHERE ID=%s", state, id));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/***************************end 质量-压实度*****************************************/
	/***************************start 质量-压实度点*************************************/
	//
	/**
	 * 新增
	 */
	public boolean insertHardingPoint(HashMap<String, String> map) {
		try{
			ContentValues values=new ContentValues();
			values.put("HardingId", map.get("HardingId"));
			values.put("LayerNum", map.get("LayerNum"));
			values.put("PointX", map.get("PointX"));
			values.put("PointY", map.get("PointY"));
			values.put("PointZ", map.get("PointZ"));
			values.put("Compaction", map.get("Compaction"));
			values.put("Density",map.get("Density"));
			values.put("MaxDensity",map.get("MaxDensity"));
			values.put("EarthNature",map.get("EarthNature"));
			dbHelpe.insert(values, "QualityHardingPoint");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 查询
	 */
	public ArrayList<HashMap<String, String>> getHardingPoint(String mHardingId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql0 = String.format("SELECT LayerNum FROM QualityHardingPoint WHERE HardingId=%s GROUP BY LayerNum", mHardingId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while(c0.moveToNext()) {
				HashMap<String, String> map0 = new HashMap<String, String>();
				map0.put("LayerNum", c0.getString(0));//  LayerNum
				map0.put("attribute", "title");
				list.add(map0);
				String sql1 = String.format("SELECT * FROM QualityHardingPoint WHERE LayerNum='%s' AND HardingId=%s", c0.getString(0), mHardingId);
				Cursor c1 = null;
				try {
					c1 = dbHelpe.getReadableDatabase().rawQuery(sql1, null);
					while(c1.moveToNext()) {
						HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("ID", c1.getString(c1.getColumnIndex("ID")));
						map1.put("HardingId", mHardingId);
						map1.put("LayerNum", c0.getString(0));
						map1.put("attribute", "content");
						String x = c1.getString(c1.getColumnIndex("PointX"));
						String y = c1.getString(c1.getColumnIndex("PointY"));
						String z = c1.getString(c1.getColumnIndex("PointZ"));
						String compaction = c1.getString(c1.getColumnIndex("Compaction"));
						map1.put("PointX", x);
						map1.put("PointY", y);
						map1.put("PointZ", z);
						map1.put("Compaction", compaction);
						map1.put("Density", c1.getString(c1.getColumnIndex("Density")));
						map1.put("MaxDensity", c1.getString(c1.getColumnIndex("MaxDensity")));
						map1.put("EarthNature", c1.getString(c1.getColumnIndex("EarthNature")));
						map1.put("Content", "压实度:" + compaction + ", x:" + x + ", y:" + y + ", z:" + z);//Content
						list.add(map1);
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(c1 != null) {
						c1.close();
						c1 = null;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return list;
	}
	
	
	/**
	 * 查询2
	 */
	public HashMap<String,ArrayList<HashMap<String, String>>> getHardingPointsMap(String mHardingId) {
		HashMap<String,ArrayList<HashMap<String, String>>> map = new HashMap<String,ArrayList<HashMap<String, String>>>();
		
		String sql0 = String.format("SELECT LayerNum FROM QualityHardingPoint WHERE HardingId=%s GROUP BY LayerNum", mHardingId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while(c0.moveToNext()) {
				String mLayerNum = c0.getString(0);
				map.put(mLayerNum, null);//  LayerNum
				String sql1 = String.format("SELECT * FROM QualityHardingPoint WHERE LayerNum='%s' AND HardingId=%s", mLayerNum, mHardingId);
				Cursor c1 = null;
				try {
					c1 = dbHelpe.getReadableDatabase().rawQuery(sql1, null);
					while(c1.moveToNext()) {
						HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("ID", c1.getString(c1.getColumnIndex("ID")));
						map1.put("HardingId", mHardingId);
						map1.put("LayerNum", c0.getString(0));
						map1.put("PointX", c1.getString(c1.getColumnIndex("PointX")));
						map1.put("PointY", c1.getString(c1.getColumnIndex("PointY")));
						map1.put("PointZ", c1.getString(c1.getColumnIndex("PointZ")));
						map1.put("Compaction", c1.getString(c1.getColumnIndex("Compaction")));
						map1.put("Density", c1.getString(c1.getColumnIndex("Density")));
						map1.put("MaxDensity", c1.getString(c1.getColumnIndex("MaxDensity")));
						map1.put("EarthNature", c1.getString(c1.getColumnIndex("EarthNature")));
						
						ArrayList<HashMap<String, String>> list = map.get(mLayerNum);
						if(list == null) {
							list = new ArrayList<HashMap<String, String>>();
						} 
						list.add(map1);
						map.put(mLayerNum, list);
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(c1 != null) {
						c1.close();
						c1 = null;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return map;
	}
	
	
	
	
	
	public int getHardingPointsNum(String mHardingId) {
		String sql0 = String.format("SELECT COUNT(1) FROM QualityHardingPoint WHERE HardingId=%s", mHardingId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			if(c0.moveToNext()) {
				return c0.getInt(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return 0;
	}
	/**
	 * 删除1
	 * @param HardingId
	 * @return
	 */
	public boolean delHardingPoints(String HardingId) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM QualityHardingPoint WHERE HardingId=%s", HardingId));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 删除2
	 * @param id
	 * @return
	 */
	public boolean delHardingPointsById(String id) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM QualityHardingPoint WHERE ID=%s", id));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 删除3
	 * @param HardingId
	 * @param layerNums
	 * @return
	 */
	public boolean delHardingPointsByLayerNums(String HardingId, String layerNums) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM QualityHardingPoint WHERE HardingId=%s AND LayerNum='%s'", HardingId, layerNums));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 修改1
	 */
	public boolean updateHardingPointLayer(String mQualityHardingId, String layerNums, String newLayerNums) {
		try {
			dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE QualityHardingPoint SET LayerNum='%s' WHERE HardingId=%s AND LayerNum='%s'", newLayerNums, mQualityHardingId, layerNums));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 修改2
	 */
	public boolean updateHardingPoint(HashMap<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE QualityHardingPoint SET ");
		builder.append("PointX='");
		builder.append(map.get("PointX"));
		builder.append("',PointY='");
		builder.append(map.get("PointY"));
		builder.append("',PointZ='");
		builder.append(map.get("PointZ"));
		builder.append("',Compaction='");
		builder.append(map.get("Compaction"));
		builder.append("',Density='");
		builder.append(map.get("Density"));
		builder.append("',MaxDensity='");
		builder.append(map.get("MaxDensity"));
		builder.append("',EarthNature='");
		builder.append(map.get("EarthNature"));
		builder.append("' WHERE ID=");
		builder.append(map.get("ID"));
		/*builder.append(" AND LayerNum='");
		builder.append(map.get("LayerNum"));
		builder.append("'");*/
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}

	/***************************end 质量-压实度点*************************************/
	
	/**---------------------StaticLoad----------------------------------------------*/
	/**
	 * 数据插入
	 * 
	 * @param userId
	 *            用户Id
	 * @param ProjectId
	 *            期段Id
	 * @param  LotId
	 *            合同段
	 * @param person
	 *            检测人
	 * @param no
	 *            检测编号
	 * @param unit
	 *            检测单位
	 * @param date
	 *            检测日期
	 * @param content
	 *            试验内容
	 * @param Conclusion
	 *            试验结论
	 *            @return ID
	 */
	public String insertTableStaticLoad(String userId, String objectId,
			String lotId, String person, String no, String unit, String date,
			String content, String Conclusion) {
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("UserId", userId);
		values.put("ProjectId", objectId);
		values.put("LotId",lotId);
		values.put("Person", person);
		values.put("No", no);
		values.put("Unit", unit);
		values.put("Date", date);
		values.put("Content", content);
		values.put("Conclusion", Conclusion);
		try {
			return String.valueOf(db.insert("StaticLoad", "", values));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return "";
	}
	
	/** 查询StaticLoad表中的状态*/
	public String getTableStaticLoadState(String id){
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("StaticLoad", new String[]{"State"}, "ID=?", new String[]{id}, null, null, null);
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("State"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return "";
	}
	
	/**
	 * 更新表中数据
	 */
	public boolean updateTableStaticLoad(String noId,String userId,String person, String no, String unit, String date,
			String content, String Conclusion){
			SQLiteDatabase db=dbHelpe.getWritableDatabase();
			ContentValues values = new ContentValues();
			//values.put("UserId", userId);
//			values.put("Object", Object);
//			values.put("Pact", pact);
			values.put("Person", person);
			values.put("No", no);
			values.put("Unit", unit);
			values.put("Date", date);
			values.put("Content", content);
			values.put("State", "1");
			values.put("Conclusion", Conclusion);
		try{
			db.update("StaticLoad", values, "ID=? AND UserId=?", new String[]{noId,userId});
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} finally {
			if(db != null) {
				db.close();
			}
		}
		return true;
	}
	/**
	 * 查询平板静电荷试验表中的数据
	 * 
	 * @param userId
	 *            用户Id
	 * @return ArrayList
	 */
	public ArrayList<HashMap<String, String>> queryTableStaticLoad(String projecttId,String lotId,String userId) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = dbHelpe.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.query("StaticLoad", null, "ProjectId=? AND LotId=? AND UserId=?", new String[] {projecttId,lotId,userId }, null, null, null);
			while (c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", c.getString(c.getColumnIndex("ID")));
				map.put("projectId", c.getString(c.getColumnIndex("ProjectId")));
				map.put("lotId", c.getString(c.getColumnIndex("LotId")));
				map.put("person", c.getString(c.getColumnIndex("Person")));
				map.put("no", c.getString(c.getColumnIndex("No")));
				map.put("unit", c.getString(c.getColumnIndex("Unit")));
				map.put("date", c.getString(c.getColumnIndex("Date")));
				map.put("content", c.getString(c.getColumnIndex("Content")));
				map.put("conclusion", c.getString(c.getColumnIndex("Conclusion")));
				map.put("state", c.getString(c.getColumnIndex("State")));
				arrayList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return arrayList;
	}

	/** 查询表示是否已经存在检测编号,如果有就返回true,否则返回false */
	public boolean queryTableStatiLoadNo(String userId, String no) {
		SQLiteDatabase db = dbHelpe.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.query("StaticLoad", null, "UserId=? AND No=?", new String[] {
					userId, no }, null, null, null);
			if (c.moveToNext())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return false;
	}

	/**指定列查询单条数据 */
	public HashMap<String, String> queryTableStaticLoadRow(String id){
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		HashMap<String, String > map=new HashMap<String, String>();
		try{
		c=db.query("StaticLoad", null, "ID=?", new String[]{id}, null, null, null);
		if(c.moveToNext()){
			map.put("projectId", c.getString(c.getColumnIndex("ProjectId")));
			map.put("lotId",c.getString(c.getColumnIndex("LotId")));
			map.put("person", c.getString(c.getColumnIndex("Person")));
			map.put("no", c.getString(c.getColumnIndex("No")));
			map.put("unit", c.getString(c.getColumnIndex("Unit")));
			map.put("date", c.getString(c.getColumnIndex("Date")));
			if("".equals(c.getString(c.getColumnIndex("Content")))){
				map.put("content", "");
			}else{
				map.put("content", c.getString(c.getColumnIndex("Content")));
			}
			if("".equals(c.getString(c.getColumnIndex("Conclusion")))){
				map.put("conclusion", "");
			}else{
				map.put("conclusion", c.getString(c.getColumnIndex("Conclusion")));
			}
			map.put("state", c.getString(c.getColumnIndex("State")));
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return map;
	}
	/** 修改表StaticLoad状态值 */
	public boolean updateTableStaticLoadState(String id) {

		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("State", "2");
		try {
			db.update("StaticLoad", values, "ID=?", new String[] { id });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return true;
	}

	/** 删除表中指定列的数据 */
	public boolean delTableStatiLoadData(String id) {
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		try {
			db.delete("StaticLoad", "ID=?", new String[] { id });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return true;
	}
	/** 清空编号为空的数据*/
	public boolean delTableStaticLoad(){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("StaticLoad", "No=?", new String[]{""});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	
	public  ArrayList<HashMap<String, String>> queryTableStaticLod(String userId){
		ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		String sql=String.format("select count(*),LotId from StaticLoad where UserId='%s' group by LotId",userId);
		Cursor c=null;
		try{
			c=db.rawQuery(sql, null);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("dataCount", c.getString(0));
				map.put("lotId", c.getString(1));
				mArrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return mArrayList;
	}
	/**-----------------------ExaminePoint 表------------------------------------------- */
	
	/**
	 * 
	 */
	public boolean insertTableExaminePointData(HashMap<String, String> map) {
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("NoId", map.get("NoId"));
		values.put("PointName", map.get("PointName"));
		values.put("PointX", map.get("PointX"));
		values.put("PointY", map.get("PointY"));
		values.put("PointZ", map.get("PointZ"));
		try {
			db.insert("ExaminePoint", "", values);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return true;
	}
	
	/** 根据ID修改表中数据 */
	public boolean updateTableExaminePointData(HashMap<String, String> map) {
		SQLiteDatabase db = dbHelpe.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("NoId", map.get("NoId"));
		values.put("PointName", map.get("PointName"));
		values.put("PointX", map.get("PointX"));
		values.put("PointY", map.get("PointY"));
		values.put("PointZ", map.get("PointZ"));
		try {
			db.update("ExaminePoint", values, "ID=?", new String[] { map.get("ID") });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return true;
	}
	
	/** 删除指定列数据*/
	public boolean deleteTableExaminePointColumn(String id){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("ExaminePoint", "ID=?", new String[]{id});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	
	/** 删除指定列数据*/
	public boolean deleteTableExaminePoint(String noId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		try{
			db.delete("ExaminePoint", "NoId=?", new String[]{noId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		return false;
	}
	
	/*public boolean queryTableExaminePoint(String userId,String noId,String pointName,String pointX,String pointY,String pointZ){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		try{
			c=db.query("ExaminePoint", null, "userId=? AND NoId=? AND PointName=? AND PointX=?" +
					"AND PointY=? AND PointZ=?", new String[]{userId,noId,pointName,pointX,pointY,pointZ},null, null,null);
		if(c.moveToNext()){
			return true;
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return false;
	}*/
	
	/** 查询表ExaminePoint 所有数据*/
	public ArrayList<HashMap<String, String>> queryTableExaminePointAllData(String noId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor  c=null;
		try{
			c=db.query("ExaminePoint", null, "NoId=?", new String[]{noId}, null, null, null);
			while(c.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", c.getString(c.getColumnIndex("ID")));
				map.put("pointName", c.getString(c.getColumnIndex("PointName")));
				map.put("pointX", c.getString(c.getColumnIndex("PointX")));
				map.put("pointY", c.getString(c.getColumnIndex("PointY")));
				map.put("pointZ", c.getString(c.getColumnIndex("PointZ")));
				arrayList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return arrayList;
	}
	
	/** 统计表ExaminePoint 的数目*/
	public int queryTableExaminePointNumber(String noId){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		String sql=String.format("select Count(1) from ExaminePoint where NoId='%s'",noId);
		try{
			c=db.rawQuery(sql, null);
			if(c.moveToFirst()){				
				return c.getInt(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();	
			}
		}
		return 0;
	}
	
	public HashMap<String, String>  queryTableExaminePoint(String id){
		SQLiteDatabase db=dbHelpe.getWritableDatabase();
		Cursor c=null;
		HashMap<String, String> map=new HashMap<String, String>();
		try{
		c=db.query("ExaminePoint", null, "ID=?", new String[]{id}, null, null, null);
		if(c.moveToNext()){
			map.put("pointName", c.getString(c.getColumnIndex("PointName")));
			map.put("pointX", c.getString(c.getColumnIndex("PointX")));
			map.put("pointY", c.getString(c.getColumnIndex("PointY")));
			map.put("pointZ", c.getString(c.getColumnIndex("PointZ")));
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			if(db!=null){
				db.close();	
			}
		}
		return map;
	}
	/****************************************波速、贯入 start***********************************************/
	/**
	 * 插入
	 */
	public Long insertWaveInjectInfo(HashMap<String, String> map) {
		try{
			ContentValues values=new ContentValues();
			values.put("UserId", map.get("UserId"));
			values.put("LocId", map.get("LocId"));
			values.put("PeriodId", map.get("PeriodId"));
			values.put("DetectType", map.get("DetectType"));//检测类型 1:波速测试,2:标准灌入试验
			return dbHelpe.getIdInsert(values, "WaveInjectionInfo");
		}catch(Exception e){
			e.printStackTrace();
			return -1L;
		}finally{
			closeDBHelper();
		}
	}
	
	/**
	 * 修改内容
	 */
	public boolean updateWaveInjectInfo(HashMap<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE WaveInjectionInfo SET ");
		builder.append("DetectNumber='");
		builder.append(map.get("DetectNumber"));
		builder.append("',DetectUnitName='");
		builder.append(map.get("DetectUnitName"));
		builder.append("',DetectUnitId='");
		builder.append(map.get("DetectUnitId"));
		builder.append("',DetectDate='");
		builder.append(map.get("DetectDate"));
		builder.append("',TestContent='");
		builder.append(map.get("TestContent"));
		builder.append("',TestConClusion='");
		builder.append(map.get("TestConClusion"));
		builder.append("',State=1");
		builder.append(" WHERE ID=");
		builder.append(map.get("ID"));
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 查询
	 * @param userId
	 * @param locId
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getWaveInjectInfo(String userId, String locId, String mDetectType) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		try {
			c = dbHelpe.getReadableDatabase().rawQuery(String.format("SELECT * FROM WaveInjectionInfo WHERE UserId='%s' AND LocId='%s' AND DetectType=%s", userId, locId, mDetectType), null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("ID")));
				map.put("UserId", userId);
				map.put("LocId", locId);
				map.put("DetectNumber", c.getString(c.getColumnIndex("DetectNumber")));
				map.put("PeriodId", c.getString(c.getColumnIndex("PeriodId")));
				map.put("DetectUnitName", c.getString(c.getColumnIndex("DetectUnitName")));
				map.put("DetectUnitId", c.getString(c.getColumnIndex("DetectUnitId")));
				map.put("DetectDate", c.getString(c.getColumnIndex("DetectDate")));
				map.put("TestContent",c.getString(c.getColumnIndex("TestContent")));
				map.put("TestConClusion",c.getString(c.getColumnIndex("TestConClusion")));
				map.put("DetectType",c.getString(c.getColumnIndex("DetectType")));
				map.put("State",c.getString(c.getColumnIndex("State")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHelper();
		}
		return list;
	}
	/**
	 * 删除1
	 * @param id
	 * @return
	 */
	public boolean delWaveInjectById(String id) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM WaveInjectionInfo WHERE ID=%s", id));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 修改内容2
	 */
	public boolean updateWaveInjectInfoState(String id) {
		try {
			dbHelpe.getWritableDatabase().execSQL(String.format("UPDATE WaveInjectionInfo SET State=2 WHERE ID=%s", id));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	/****************************************波速、贯入 信息end***********************************************/
	
	/****************************************波速、贯入 点start***********************************************/
	//
	/** 查询单位Id*/
//	public String queryContacts(String userId,String DeptName){
//		SQLiteDatabase db=dbHelpe.getReadableDatabase();
//		Cursor c=null;
//		try{
//			c=db.query(true, "Contacts", new String[]{"DptId"}, "UserId=? AND DeptName=?", new String[]{userId,DeptName}, "DptId", null, null, null);
//			if(c.moveToNext()){
//				return c.getString(c.getColumnIndex("DptId"));
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(c!=null){
//				c.close();
//			}
//			if(db!=null){
//				db.close();
//			}
//		}
//		return "";
//	}
	
	public String queryContactName(String DeptId) {
		SQLiteDatabase db=dbHelpe.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.rawQuery(String.format("SELECT DeptName FROM Contacts WHERE DptId='%s'", DeptId), null);
			if(c.moveToFirst()){
				return c.getString(c.getColumnIndex("DeptName"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null){
				c.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return "";
	}
	
	
	/**
	 * 插入
	 */
	public boolean insertWaveInjectPoint(HashMap<String, String> map) {
		try{
			ContentValues values=new ContentValues();
			values.put("WaveInjectionId", map.get("WaveInjectionId"));
			values.put("PointNums", map.get("PointNums"));
			values.put("PointX", map.get("PointX"));
			values.put("PointY", map.get("PointY"));
			values.put("PointZ", map.get("PointZ"));
			values.put("DeepStartValue", map.get("DeepStartValue"));
			values.put("DeepEndValue", map.get("DeepEndValue"));
			values.put("WaveVelocityValue", map.get("WaveVelocityValue"));
			dbHelpe.insert(values, "WaveInjectionPoint");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}

	public int getWaveInjectPointsNum(String mWaveInjectId) {
		String sql0 = String.format("SELECT COUNT(1) FROM WaveInjectionPoint WHERE WaveInjectionId=%s", mWaveInjectId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			if(c0.moveToNext()) {
				return c0.getInt(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return 0;
	}
	/**
	 * 修改内容1
	 */
	public boolean updateWaveInjectPoint(HashMap<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE WaveInjectionPoint SET ");
		builder.append("DeepStartValue='");
		builder.append(map.get("DeepStartValue"));
		builder.append("',DeepEndValue='");
		builder.append(map.get("DeepEndValue"));
		builder.append("',WaveVelocityValue='");
		builder.append(map.get("WaveVelocityValue"));
		builder.append("' WHERE ID=");
		builder.append(map.get("ID"));
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	/**
	 * 修改标题内容2
	 */
	public boolean updateWaveInjectPointValues(HashMap<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE WaveInjectionPoint SET ");
		builder.append("PointNums='");
		builder.append(map.get("PointNums"));
		builder.append("',PointX='");
		builder.append(map.get("PointX"));
		builder.append("',PointY='");
		builder.append(map.get("PointY"));
		builder.append("',PointZ='");
		builder.append(map.get("PointZ"));
		builder.append("' WHERE PointNums='");
		builder.append(map.get("PointNums"));
		builder.append("' AND WaveInjectionId=");
		builder.append(map.get("WaveInjectionId"));
		try {
			dbHelpe.getWritableDatabase().execSQL(builder.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHelper();
		}
		return true;
	}
	
	
	
	/**
	 * 查询1
	 */
	public ArrayList<HashMap<String, String>> getWaveInjectPointsList(String mWaveInjectionId) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		String sql0 = String.format("SELECT PointNums,PointX,PointY,PointZ FROM WaveInjectionPoint WHERE WaveInjectionId=%s GROUP BY PointNums", mWaveInjectionId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while(c0.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				String mPointNums = c0.getString(c0.getColumnIndex("PointNums"));
				map.put("PointNums", mPointNums);
				map.put("PointX", c0.getString(c0.getColumnIndex("PointX")));
				map.put("PointY", c0.getString(c0.getColumnIndex("PointY")));
				map.put("PointZ", c0.getString(c0.getColumnIndex("PointZ")));
				map.put("attribute", "title");
				list.add(map);
				String sql1 = String.format("SELECT * FROM WaveInjectionPoint WHERE PointNums='%s' AND WaveInjectionId=%s", mPointNums, mWaveInjectionId);
				Cursor c1 = null;
				try {
					c1 = dbHelpe.getReadableDatabase().rawQuery(sql1, null);
					while(c1.moveToNext()) {
						HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("ID", c1.getString(c1.getColumnIndex("ID")));
						map1.put("WaveInjectionId", mWaveInjectionId);
						map1.put("PointNums", mPointNums);
						map1.put("PointX", c1.getString(c1.getColumnIndex("PointX")));
						map1.put("PointY", c1.getString(c1.getColumnIndex("PointY")));
						map1.put("PointZ", c1.getString(c1.getColumnIndex("PointZ")));
						map1.put("DeepStartValue", c1.getString(c1.getColumnIndex("DeepStartValue")));
						map1.put("DeepEndValue", c1.getString(c1.getColumnIndex("DeepEndValue")));
						map1.put("WaveVelocityValue", c1.getString(c1.getColumnIndex("WaveVelocityValue")));
						map1.put("attribute", "content");
						list.add(map1);
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(c1 != null) {
						c1.close();
						c1 = null;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return list;
	} 
	
	/**
	 * 查询2
	 */
	public HashMap<HashMap<String, String>, ArrayList<HashMap<String, String>>> getWaveInjectPointsMap(String mWaveInjectionId) {
		HashMap<HashMap<String, String>, ArrayList<HashMap<String, String>>> mCacheMap = new HashMap<HashMap<String, String>, ArrayList<HashMap<String, String>>>();
		String sql0 = String.format("SELECT PointNums,PointX,PointY,PointZ FROM WaveInjectionPoint WHERE WaveInjectionId=%s GROUP BY PointNums", mWaveInjectionId);
		Cursor c0 = null;
		try {
			c0 = dbHelpe.getReadableDatabase().rawQuery(sql0, null);
			while(c0.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				String mPointNums = c0.getString(c0.getColumnIndex("PointNums"));
				map.put("PointNums", mPointNums);
				map.put("PointX", c0.getString(c0.getColumnIndex("PointX")));
				map.put("PointY", c0.getString(c0.getColumnIndex("PointY")));
				map.put("PointZ", c0.getString(c0.getColumnIndex("PointZ")));
				mCacheMap.put(map, null);
				String sql1 = String.format("SELECT * FROM WaveInjectionPoint WHERE PointNums='%s' AND WaveInjectionId=%s", mPointNums, mWaveInjectionId);
				Cursor c1 = null;
				try {
					c1 = dbHelpe.getReadableDatabase().rawQuery(sql1, null);
					while(c1.moveToNext()) {
						HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("ID", c1.getString(c1.getColumnIndex("ID")));
						map1.put("WaveInjectionId", mWaveInjectionId);
						map1.put("PointNums", mPointNums);
						map1.put("PointX", c1.getString(c1.getColumnIndex("PointX")));
						map1.put("PointY", c1.getString(c1.getColumnIndex("PointY")));
						map1.put("PointZ", c1.getString(c1.getColumnIndex("PointZ")));
						map1.put("DeepStartValue", c1.getString(c1.getColumnIndex("DeepStartValue")));
						map1.put("DeepEndValue", c1.getString(c1.getColumnIndex("DeepEndValue")));
						map1.put("WaveVelocityValue", c1.getString(c1.getColumnIndex("WaveVelocityValue")));
						ArrayList<HashMap<String, String>> list = mCacheMap.get(map);
						if(list == null) {
							list = new ArrayList<HashMap<String, String>>();
						}
						list.add(map1);
						mCacheMap.put(map, list);
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(c1 != null) {
						c1.close();
						c1 = null;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(c0 != null) {
				c0.close();
				c0 = null;
			}
			closeDBHelper();
		}
		return mCacheMap;
	} 
	
	
	/**
	 * 删除1
	 * @param id
	 * @return
	 */
	public boolean delWaveInjectPointById(String id) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM WaveInjectionPoint WHERE ID=%s", id));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 删除2
	 * @param id
	 * @return
	 */
	public boolean delWaveInjectPoints(String mPointNums, String mWaveInjectionId) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM WaveInjectionPoint WHERE mPointNums='%s' AND WaveInjectionId=%s", mPointNums, mWaveInjectionId));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
	
	/**
	 * 删除3
	 * @param id
	 * @return
	 */
	public boolean delWaveInjectPointByWaveInjectionId(String mWaveInjectionId) {
		try{
			dbHelpe.getWritableDatabase().execSQL(String.format("DELETE FROM WaveInjectionPoint WHERE WaveInjectionId=%s", mWaveInjectionId));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			closeDBHelper();
		}
		return true;
	}
}