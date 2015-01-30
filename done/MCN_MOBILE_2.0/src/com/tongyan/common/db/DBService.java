package com.tongyan.common.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.entities._Check;
import com.tongyan.common.entities._Contacts;
import com.tongyan.common.entities._ContactsData;
import com.tongyan.common.entities._ContactsEmp;
import com.tongyan.common.entities._HoleFace;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._HolefaceSettingInfo;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.common.entities._Item;
import com.tongyan.common.entities._ItemSec;
import com.tongyan.common.entities._ListObj;
import com.tongyan.common.entities._LocRisk;
import com.tongyan.common.entities._LocalMsg;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._Section;
import com.tongyan.common.entities._User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @ClassName DBService 
 * @author wanghb
 * @date 2013-7-16 am 09:37:56
 * @desc TODO
 */
public class DBService {
	
	private final static String TAG = "DBService";
	//private DBHelper dbHelper;
	
	private DBHelp dbHelper; 
	
	private Context context;
	public DBService(Context context) {
		this.setContext(context);
		//this.dbHelper = new DBHelper(context);
		dbHelper = new DBHelp();
	}
	
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
	
	/** 删除某表中一条数据，所有数据*/
	public boolean delSingle(String tabName, String id) {
		try {
			dbHelper.del(tabName,id);
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
		return true;
	}
	
	/** 1.用户表-插入数据 */
	public boolean insertUser(_User user) {
		ContentValues values = new ContentValues();
		values.put("USERID", user.getUserid());
		values.put("USERNAME", user.getUsername());
		values.put("PASSWORD", user.getPassword());
		values.put("DPTROWID", user.getDptRowId());
		values.put("DEPARTMENT", user.getDepartment());
		values.put("EMPLEVEL", user.getEmpLevel());
		values.put("EMPNAME", user.getEmpName());
		values.put("SAVEPASSWORD", user.getSavepassword());
		//values.put("IsUpdate", user.getIsUpdate());
		values.put("AUTOLOGIN", user.getAutologin());
		values.put("IsGps", "1");
		try {
			if(dbHelper.insert(values,"LOCAL_USER")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			closeDBHepler();
		}
	}
	
	/** 4.通讯录表-插入数据 */
	public boolean insertContacts(List<_Contacts> mContactsList) {
		try {
		for(_Contacts d : mContactsList) {
			if(d != null) {
				ArrayList<_ContactsEmp> list = d.getEmpList();
				if(list != null && list.size() > 0) {
					for(_ContactsEmp e : list) {
						if(e != null) {
							ContentValues values = new ContentValues();
							values.put("dptName", d.getDptName());
							values.put("dptTel", d.getDptTel());
							values.put("dptFax", d.getDptFax());
							values.put("empName", e.getEmpName());
							values.put("empContact", e.getEmpContact());
							values.put("empPinyin",e.getEmpPinyin());
							values.put("dptPinyin", d.getDptNamePinyin());
							values.put("rowId", e.getRowId());
							dbHelper.insert(values,"LOCAL_CONTACTS");
						}
					}
				}
			}
		}
		} catch (Exception ex) {
			return false;
		}finally{
			closeDBHepler();
		}
		return true;
	}
	
	
	/** 2.工程表-插入数据 */
	public boolean insertProject(_Project project) {
		ContentValues values = new ContentValues();
		values.put("rowId", project.getRowId());
		values.put("aName", project.getaName());
		values.put("aPid", project.getaPid());
		values.put("aSecid", project.getaSecid());
		values.put("aType", project.getaType());
//		values.put("aSlng", project.getaSlng());
//		values.put("aSlat", project.getaSlat());
//		values.put("aElng", project.getaElng());
//		values.put("aElat", project.getaElat());
		values.put("aPmName", project.getaPmName());
		values.put("aPContract", project.getaPContract());
		values.put("aConstruct", project.getaConstruct());
		values.put("aStartMile", project.getaStartMile());
		values.put("aEndMile", project.getaEndMile());
		values.put("aPosition", project.getaPosition());
		values.put("aPositionMile", project.getaPositionMile());
		try {
			if(dbHelper.insert(values,"LOCAL_PROJECT")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
	}
	/** 2.工程表-查询数据 */
	public List<_Project> selectProjectListBySecId(String aSecid) {
		List<_Project> proList = new ArrayList<_Project>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM LOCAL_PROJECT WHERE aSecid = '" + aSecid + "'", null);
			while(cursor.moveToNext()) {
				_Project project = new _Project();
				project.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				project.setRowId(cursor.getString(cursor.getColumnIndex("rowId")));
				project.setaName(cursor.getString(cursor.getColumnIndex("aName")));
				project.setaPid(cursor.getString(cursor.getColumnIndex("aPid")));
				project.setaSecid(aSecid);
				project.setaType(cursor.getString(cursor.getColumnIndex("aType")));
				project.setaPmName(cursor.getString(cursor.getColumnIndex("aPmName")));
				project.setaPContract(cursor.getString(cursor.getColumnIndex("aPContract")));
				project.setaConstruct(cursor.getString(cursor.getColumnIndex("aConstruct")));
				project.setaStartMile(cursor.getString(cursor.getColumnIndex("aStartMile")));
				project.setaEndMile(cursor.getString(cursor.getColumnIndex("aEndMile")));
				project.setaPositionMile(cursor.getString(cursor.getColumnIndex("aPositionMile")));
				project.setaPosition(cursor.getString(cursor.getColumnIndex("aPosition")));
				proList.add(project);
			}
		} catch(Exception e) {
			return null;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return proList;
	}
	
	
	
	
	/** 3.项目标段表-插入数据 */
	public boolean insertItemSec(_ItemSec itemSec) {
		ContentValues values = new ContentValues();
		values.put("iid", itemSec.getIid());
		values.put("iContent", itemSec.getItext());
		values.put("iAttributes", itemSec.getIattributes());
		values.put("sid", itemSec.getSid());
		values.put("sContent", itemSec.getStext());
		values.put("sAttributes", itemSec.getSattributes());
		try {
			if(dbHelper.insert(values,"LOCAL_ITEM_SECTION")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
	}
	/** 5.风险检查表-插入数据 */
	public Long insertCheck(_Check check) {
		ContentValues values = new ContentValues();
		values.put("upTime", check.getUpTime());
		values.put("aStartMile", check.getaStartMile());
		values.put("aEndMile", check.getaEndMile());
		values.put("aProjectId", check.getaProjectId());
		values.put("aProName", check.getaProName());
		values.put("aSecName", check.getaSecName());
		values.put("aItemName", check.getaItemName());
		values.put("aSecId", check.getaSecId());
		values.put("aItemId", check.getaItemId());
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_CHECK");
			if(id != null){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	
	/** 5.风险检查表-更新数据 */
	public boolean updateCheck(_Check check) {
		try {
		    dbHelper.getWritableDatabase().execSQL("update LOCAL_CHECK set isUpdate=?,checkContent=? where _ID=?",
				new Object[] { check.getIsUpdate(), check.getCheckContent(), check.getId()});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/** 5.风险检查表-更新数据 */
	public boolean updateCheckId(_Check check) {
		try {
		    dbHelper.getWritableDatabase().execSQL("update LOCAL_CHECK set checkId=? where _ID=?",
				new Object[] { check.getCheckId(), check.getId()});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/**6. 图片地址表-插入数据 */
	public boolean insertPhotos(_LocalPhotos photos) {
		ContentValues values = new ContentValues();
		values.put("check_tab_id", photos.getCheck_tab_id());
		values.put("checkId", photos.getCheckId());
		values.put("local_img_path", photos.getLocal_img_path());
		values.put("remote_img_path", photos.getRemote_img_path());
		values.put("remote_img_id", photos.getRemote_img_id());
		try {
			if(dbHelper.insert(values,"LOCAL_PHOTOS")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
	}
	/** 6.图片地址表-删除数据   */
	public boolean deletePhotos(String tid) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_PHOTOS WHERE check_tab_id = '" + tid + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/** 6.图片地址表-更新数据   */
	public boolean updatePhotos(_LocalPhotos photos) {
		try {
			dbHelper.getWritableDatabase().execSQL("update LOCAL_PHOTOS set remote_img_path = '" + photos.getRemote_img_path() + "',remote_img_id='" + photos.getRemote_img_id()+ "',checkId='" + photos.getCheckId() +"' WHERE _ID='" + photos.getId() + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	/** 6.图片地址表-查询数据   */
	public List<_LocalPhotos> selectPhotos(String check_tab_id) {
		List<_LocalPhotos> locPhoto = new ArrayList<_LocalPhotos>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM LOCAL_PHOTOS WHERE check_tab_id = '" + check_tab_id + "'", null);
			while(cursor.moveToNext()) {
				_LocalPhotos photo = new _LocalPhotos();
				photo.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				photo.setCheck_tab_id(check_tab_id);
				photo.setCheckId(cursor.getString(cursor.getColumnIndex("checkId")));
				photo.setLocal_img_path(cursor.getString(cursor.getColumnIndex("local_img_path")));
				photo.setRemote_img_path(cursor.getString(cursor.getColumnIndex("remote_img_path")));
				photo.setRemote_img_id(cursor.getString(cursor.getColumnIndex("remote_img_id")));
				
				locPhoto.add(photo);
			}
		} catch(Exception e) {
			return locPhoto;
		} finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return locPhoto;
	}
	
	
	
	
	/** 7.定位信息缓存表-插入数据 */
	public boolean inserLocs(_LocalMsg msg) {
		ContentValues values = new ContentValues();
		//values.put("username", msg.getUsername());
		//values.put("password", msg.getPasswrod());
		values.put("userid", msg.getUsrid());
		values.put("params", msg.getParams());
		try {
			if(dbHelper.insert(values,"LOCAL_LOC")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
	}
	/** 7.定位信息缓存表-数据查询 */ 
	public List<_LocalMsg> selectLocalMsg() {
		List<_LocalMsg> locMsg = new ArrayList<_LocalMsg>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.query("LOCAL_LOC");
			while(cursor.moveToNext()) {
				_LocalMsg msg = new _LocalMsg();
				msg.set_id(cursor.getInt(cursor.getColumnIndex("_ID")));
				msg.setUsrid(cursor.getString(cursor.getColumnIndex("userid")));
				msg.setParams(cursor.getString(cursor.getColumnIndex("params")));
				locMsg.add(msg);
			}
		} catch(Exception e) {
			return locMsg;
		} finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return locMsg;
	}
	
	
	/** 7.定位信息缓存表-数据删除  */ 
	public boolean deleteLocalMsg(String id) {
		try {
			dbHelper.del("LOCAL_LOC", id);
		} catch(Exception e) {
			return false;
		} finally{
			closeDBHepler();
		}
		return true;
	}
	/** 8.风险检查表-插入数据  */
	public Long insertLocRisk(_LocRisk risk) {
		ContentValues values = new ContentValues();
		values.put("rowId", risk.getRowId());
		values.put("userId", risk.getUserId());
		values.put("iid", risk.getIid());
		values.put("iContent", risk.getiContent());
		values.put("sid", risk.getSid());
		values.put("sContent", risk.getsContent());
		values.put("prowid", risk.getProwid());
		values.put("pContent", risk.getpContent());
		values.put("currDate", risk.getCurrDate());
		//values.put("isFinish", risk.getIsFinish());
		//values.put("riskName", risk.getRiskName());
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_RISK");
			if(id != null){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	/** 8.风险检查表-查询数据  */
	public List<_LocRisk> selectRiskList(String userId) { 
	List<_LocRisk> riskList = new ArrayList<_LocRisk>();
	Cursor cursor = null;
	try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_RISK WHERE userId = '" + userId + "' ORDER BY currDate DESC",null);
			while(cursor.moveToNext()) {
				_LocRisk risk = new _LocRisk();
				risk.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				risk.setRowId(cursor.getString(cursor.getColumnIndex("rowId")));
				risk.setUserId(userId);
				risk.setIid(cursor.getString(cursor.getColumnIndex("iid")));
				risk.setiContent(cursor.getString(cursor.getColumnIndex("iContent")));
				risk.setSid(cursor.getString(cursor.getColumnIndex("sid")));
				risk.setsContent(cursor.getString(cursor.getColumnIndex("sContent")));
				risk.setProwid(cursor.getString(cursor.getColumnIndex("prowid")));
				risk.setpContent(cursor.getString(cursor.getColumnIndex("pContent")));
				risk.setIsFinish(cursor.getString(cursor.getColumnIndex("isFinish")));
				risk.setCurrDate(cursor.getString(cursor.getColumnIndex("currDate")));
				riskList.add(risk);
			}
		} catch (Exception e) {
			return riskList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return riskList;
	}
	
	/** 8.风险检查表-查询数据  */
	public _LocRisk selectRiskOne(String $id) { 
	_LocRisk risk = null;
	Cursor cursor = null;
	try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_RISK WHERE _ID = '" + $id + "'",null);
			if(cursor.moveToFirst()) {
				risk = new _LocRisk();
				risk.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				risk.setRowId(cursor.getString(cursor.getColumnIndex("rowId")));
				risk.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
				risk.setIid(cursor.getString(cursor.getColumnIndex("iid")));
				risk.setiContent(cursor.getString(cursor.getColumnIndex("iContent")));
				risk.setSid(cursor.getString(cursor.getColumnIndex("sid")));
				risk.setsContent(cursor.getString(cursor.getColumnIndex("sContent")));
				risk.setProwid(cursor.getString(cursor.getColumnIndex("prowid")));
				risk.setpContent(cursor.getString(cursor.getColumnIndex("pContent")));
				risk.setIsFinish(cursor.getString(cursor.getColumnIndex("isFinish")));
				risk.setCurrDate(cursor.getString(cursor.getColumnIndex("currDate")));
			}
		} catch (Exception e) {
			return risk;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return risk;
	}
	/** 8.风险检查表-修改数据  */
	public boolean updateRisk(String $riskId, String isFinish) {
		try {
		    dbHelper.getWritableDatabase().execSQL("update LOCAL_RISK set isFinish=? where _ID=?",
				new Object[] {isFinish,$riskId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	
	/** 9.掌子面表-插入数据 */
	public Long insertLocHoloFace(_HoleFace face) {
		ContentValues values = new ContentValues();
		values.put("risk_id", face.getRiskId());
		values.put("proposeDegree", "1");
		values.put("hole", face.getHole());
		values.put("currMile", face.getCurrMile());
		values.put("riskDegree", "1");
		values.put("riskHSuggest", context.getResources().getString(R.string.p36_risk_1));
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_HOLO_FACE");
			if(id != 0){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	/** 9.掌子面表-删除数据 */
	public boolean deleteHoloFace(String riskId) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_HOLO_FACE WHERE risk_id = '" + riskId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/** 9.掌子面表-查询数据 */
	public List<_HoleFace> getHoloFaceList(String risk_id) {
		List<_HoleFace> holoList = new ArrayList<_HoleFace>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_HOLO_FACE where risk_id = '"+ risk_id +"'", null);
			while(cursor.moveToNext()) {
				_HoleFace holo = new _HoleFace();
				String _id = cursor.getString(cursor.getColumnIndex("_ID"));
				//String risk_id = cursor.getString(cursor.getColumnIndex("risk_id"));
				String isFinish = cursor.getString(cursor.getColumnIndex("isFinish"));
				String hole = cursor.getString(cursor.getColumnIndex("hole"));
				String currMile = cursor.getString(cursor.getColumnIndex("currMile"));
				
				holo.set_id(_id);
				holo.setIsFinish(isFinish);
				holo.setCurrMile(currMile);
				holo.setRiskId(risk_id);
				holo.setHole(hole);
				
				holoList.add(holo);
			}
			return holoList;
		} catch (Exception e) {
			return holoList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/** 9.掌子面表-查询one数据 */
	public _HoleFace getOneHoloFace(String $id) {
		_HoleFace holo = null;
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_HOLO_FACE where _ID = "+ $id, null);
			if(cursor.moveToFirst()) {
				holo = new _HoleFace();
				
				holo.set_id(cursor.getString(cursor.getColumnIndex("_ID")));
				holo.setIsFinish(cursor.getString(cursor.getColumnIndex("isFinish")));
				holo.setCurrMile(cursor.getString(cursor.getColumnIndex("currMile")));
				holo.setRiskId(cursor.getString(cursor.getColumnIndex("risk_id")));
				holo.setHole(cursor.getString(cursor.getColumnIndex("hole")));
				holo.setProposeDegree(cursor.getString(cursor.getColumnIndex("proposeDegree")));
				holo.setRiskDegree(cursor.getString(cursor.getColumnIndex("riskDegree")));
				holo.setRiskHSuggest(cursor.getString(cursor.getColumnIndex("riskHSuggest")));
			}
			return holo;
		} catch (Exception e) {
			return holo;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/** 9.掌子面表-修改数据   */
	public boolean updateHoleface(String $holefaceId,String isFinish,String mMile, String mDegree, String mMDegree, String mSuggest) {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("update LOCAL_HOLO_FACE set isFinish='" + isFinish + "'");
			if(mDegree != null) {
				buffer.append(" , proposeDegree='" + mDegree + "'");
			}
			if(mMDegree != null) {
				buffer.append(" , riskDegree='" + mMDegree + "'");
			}
			if(mMile != null) {
				buffer.append(" , riskHSuggest='" + mSuggest + "'");
			}
			if(mSuggest != null) {
				buffer.append(" , currMile='" + mMile + "'");
			}
			buffer.append(" where _ID='" + $holefaceId +"'");
			
		    dbHelper.getWritableDatabase().execSQL(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/** 9.掌子面表-修改数据   */
	public boolean updateHolefaceByDegree(String $holefaceId,String mDegree) {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("update LOCAL_HOLO_FACE set proposeDegree='" + mDegree + "'");
			buffer.append(" where _ID='" + $holefaceId +"'");
		    dbHelper.getWritableDatabase().execSQL(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	
	
	/** 10.掌子面图片表-插入数据 */
	public boolean insertHolefacePhotos(_LocalPhotos photos) {
		ContentValues values = new ContentValues();
		values.put("riskId",photos.get$riskId());
		values.put("holeface_tab_id", photos.getCheck_tab_id());
		values.put("riskUUID", photos.getCheckId());
		values.put("local_img_path", photos.getLocal_img_path());
		values.put("remote_img_path", photos.getRemote_img_path());
		values.put("remote_img_id", photos.getRemote_img_id());
		try {
			if(dbHelper.insert(values,"LOCAL_FACE_PHOTOS")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			closeDBHepler();
		}
	}
	/** 10.掌子面图片表-查询数据 */
	public List<_LocalPhotos> selectHolefacePhotosByRiskId(String $riskId) {
		List<_LocalPhotos> locPhoto = new ArrayList<_LocalPhotos>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM LOCAL_FACE_PHOTOS WHERE riskId = '" + $riskId + "'", null);
			while(cursor.moveToNext()) {
				_LocalPhotos photo = new _LocalPhotos();
				
				photo.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				photo.set$riskId(cursor.getString(cursor.getColumnIndex("riskId")));
				photo.setCheck_tab_id(cursor.getString(cursor.getColumnIndex("holeface_tab_id")));
				photo.setCheckId(cursor.getString(cursor.getColumnIndex("riskUUID")));
				photo.setLocal_img_path(cursor.getString(cursor.getColumnIndex("local_img_path")));
				photo.setRemote_img_path(cursor.getString(cursor.getColumnIndex("remote_img_path")));
				photo.setRemote_img_id(cursor.getString(cursor.getColumnIndex("remote_img_id")));
				
				locPhoto.add(photo);
			}
		} catch(Exception e) {
			return locPhoto;
		} finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return locPhoto;
	}
	
	
	
	/** 10.掌子面图片表-删除数据   */
	public boolean deleteHolefacePhotos(String tid) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_FACE_PHOTOS WHERE holeface_tab_id = '" + tid + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 10.掌子面图片表-删除数据   */
	public boolean deleteHolefacePhotosByRikId(String $riskId) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_FACE_PHOTOS WHERE riskId = '" + $riskId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 10.掌子面图片表-修改数据   */
	public boolean updateHolefacePhotos(String $photoId,String remote_path, String remote_id) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_FACE_PHOTOS set remote_img_path=?,remote_img_id=? where _ID=?",
				new Object[] {remote_path,remote_id, $photoId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 10.掌子面图片表-查询数据   */
	public List<_LocalPhotos> selectHolefacePhotos(String holeface_tab_id) {
		List<_LocalPhotos> locPhoto = new ArrayList<_LocalPhotos>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getWritableDatabase().rawQuery("SELECT * FROM LOCAL_FACE_PHOTOS WHERE holeface_tab_id = '" + holeface_tab_id + "'", null);
			while(cursor.moveToNext()) {
				_LocalPhotos photo = new _LocalPhotos();
				
				photo.setId(cursor.getString(cursor.getColumnIndex("_ID")));
				photo.set$riskId(cursor.getString(cursor.getColumnIndex("riskId")));
				photo.setCheck_tab_id(holeface_tab_id);
				photo.setCheckId(cursor.getString(cursor.getColumnIndex("riskUUID")));
				photo.setLocal_img_path(cursor.getString(cursor.getColumnIndex("local_img_path")));
				photo.setRemote_img_path(cursor.getString(cursor.getColumnIndex("remote_img_path")));
				photo.setRemote_img_id(cursor.getString(cursor.getColumnIndex("remote_img_id")));
				
				locPhoto.add(photo);
			}
		} catch(Exception e) {
			return locPhoto;
		} finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return locPhoto;
	}
	
	/** 11.掌子面表-风险配置数据表-插入数据 */
	public Long insertHolefaceSetting(_HolefaceSetting mHolefaceSetting) {
		ContentValues values = new ContentValues();
		values.put("oneType", mHolefaceSetting.getOneType());
		values.put("twoType", mHolefaceSetting.getTwoType());
		values.put("cId", mHolefaceSetting.get$cId());
		values.put("class1", mHolefaceSetting.getClass1());
		values.put("class2", mHolefaceSetting.getClass2());
		values.put("isChild", mHolefaceSetting.getIsChild());
		values.put("class2Tip", mHolefaceSetting.getClass2Tip());
		values.put("riskType", mHolefaceSetting.getRiskType());
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_RISK_SETTINGS");
			if(id != 0){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	/** 11.掌子面表-风险配置数据表-按类型查询 */
	public List<String> findHolefaceSettingGroupByType() {
		List<String> mHolefaceSettingList = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT riskType FROM LOCAL_RISK_SETTINGS GROUP BY riskType", null);
			while(cursor.moveToNext()) {
				String i_type = cursor.getString(cursor.getColumnIndex("riskType"));
				mHolefaceSettingList.add(i_type);
			}
			return mHolefaceSettingList;
		} catch (Exception e) {
			return mHolefaceSettingList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/** 11.掌子面表-风险配置数据表-按类型查询 2 */
	public List<_HolefaceSetting> findHolefaceSettingGroupByItype(String riskType) {
		List<_HolefaceSetting> mHolefaceSettingList = new ArrayList<_HolefaceSetting>();
		Cursor cursor = null;
		Cursor cursor2 = null;
		
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_RISK_SETTINGS WHERE riskType='" + riskType + "' AND cId = '0'", null);
			while(cursor.moveToNext()) {
				_HolefaceSetting mSetting = new _HolefaceSetting();
				String $id = cursor.getString(cursor.getColumnIndex("_ID"));
				mSetting.set$id($id);
				mSetting.setOneType(cursor.getString(cursor.getColumnIndex("oneType")));
				mSetting.setTwoType(cursor.getString(cursor.getColumnIndex("twoType")));
				mSetting.set$cId(cursor.getString(cursor.getColumnIndex("cId")));
				mSetting.setIsChild(cursor.getString(cursor.getColumnIndex("isChild")));
				mSetting.setClass2Tip(cursor.getString(cursor.getColumnIndex("class2Tip")));
				mSetting.setRiskType(riskType);
				mSetting.setClass1(cursor.getString(cursor.getColumnIndex("class1")));
				mSetting.setClass2(cursor.getString(cursor.getColumnIndex("class2")));
				
				List<_HolefaceSetting> mSubHolefaceSettingList = new ArrayList<_HolefaceSetting>();
				cursor2 = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_RISK_SETTINGS WHERE riskType='" + riskType + "' AND cId = '"+ $id +"'", null);
				while(cursor2.moveToNext()) {
					_HolefaceSetting mSSetting = new _HolefaceSetting();
					mSSetting.set$id(cursor2.getString(cursor2.getColumnIndex("_ID")));
					mSSetting.setOneType(cursor2.getString(cursor2.getColumnIndex("oneType")));
					mSSetting.setTwoType(cursor2.getString(cursor2.getColumnIndex("twoType")));
					mSSetting.set$cId(cursor2.getString(cursor2.getColumnIndex("cId")));
					mSSetting.setIsChild(cursor2.getString(cursor2.getColumnIndex("isChild")));
					mSSetting.setClass2Tip(cursor2.getString(cursor2.getColumnIndex("class2Tip")));
					mSSetting.setRiskType(riskType);
					mSSetting.setClass1(cursor2.getString(cursor2.getColumnIndex("class1")));
					mSSetting.setClass2(cursor2.getString(cursor2.getColumnIndex("class2")));
					
					mSubHolefaceSettingList.add(mSSetting);
				}
				mSetting.setmClildList(mSubHolefaceSettingList);
				mHolefaceSettingList.add(mSetting);
			}
			return mHolefaceSettingList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			if(cursor2 != null) {
				cursor2.close();
				cursor2 = null;
			}
			closeDBHepler();
		}
	}
	/** 11.掌子面表-风险配置数据表-按roid */
	public _HolefaceSetting findRiskSettingsById(String rowid) {
		_HolefaceSetting mSetting = null;
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_RISK_SETTINGS WHERE _ID='" + rowid + "'", null);
			if(cursor.moveToFirst()) {
				mSetting = new _HolefaceSetting();
				mSetting.set$id(cursor.getString(cursor.getColumnIndex("_ID")));
				mSetting.set$cId(cursor.getString(cursor.getColumnIndex("cId")));
				mSetting.setClass1(cursor.getString(cursor.getColumnIndex("class1")));
				mSetting.setClass2(cursor.getString(cursor.getColumnIndex("class2")));
				mSetting.setOneType(cursor.getString(cursor.getColumnIndex("oneType")));
				mSetting.setTwoType(cursor.getString(cursor.getColumnIndex("twoType")));
				mSetting.setIsChild(cursor.getString(cursor.getColumnIndex("isChild")));
				mSetting.setClass2Tip(cursor.getString(cursor.getColumnIndex("class2Tip")));
				mSetting.setRiskType(cursor.getString(cursor.getColumnIndex("riskType")));
			}
			return mSetting;
		} catch (Exception e) {
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	/** 12.掌子面风险配置信息表-插入数据 */
	public Long insertHolefaceSettingInfo(_HolefaceSettingInfo mInfo) {
		ContentValues values = new ContentValues();
		values.put("risk_id", mInfo.get$riskId());
		values.put("holeface_id", mInfo.get$holefaceId());
		values.put("risk_hole", mInfo.getRiskHoleName());
		values.put("isCheck", mInfo.getIsCheck());
		values.put("risk_type_name", mInfo.getRiskTypeName());
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_HOLO_SETTING_INFO");
			if(id != 0){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	/** 12.掌子面风险配置信息表-查询是否已检查 */
	public List<_HolefaceSettingInfo> getHolefaceSettingInfoByRiskType(String mHolefaceId, String mRiskType) {
		List<_HolefaceSettingInfo> mHolefaceSettingList = new ArrayList<_HolefaceSettingInfo>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_HOLO_SETTING_INFO WHERE holeface_id='" + mHolefaceId + "' AND risk_type_name='" + mRiskType + "'", null);
			while(cursor.moveToNext()) {
				_HolefaceSettingInfo mSetting = new _HolefaceSettingInfo();
				//String $holefaceId = cursor.getString(cursor.getColumnIndex("holeface_id"));
				//String mRiskTypeName = cursor.getString(cursor.getColumnIndex("risk_type_name"));
				mSetting.set$id(cursor.getString(cursor.getColumnIndex("_ID")));
				mSetting.set$holefaceId(mHolefaceId);
				mSetting.set$riskId(cursor.getString(cursor.getColumnIndex("risk_id")));
				mSetting.setIsCheck(cursor.getString(cursor.getColumnIndex("isCheck")));
				mSetting.setRiskHoleName(cursor.getString(cursor.getColumnIndex("risk_hole")));
				mSetting.setRiskTypeName(mRiskType);
				
				mHolefaceSettingList.add(mSetting);
			}
			return mHolefaceSettingList;
		} catch (Exception e) {
			return mHolefaceSettingList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/** 12.掌子面风险配置信息表-查询 */
	public List<_HolefaceSettingInfo> getHolefaceSettingInfoByRiskId(String $riskId,String $holefaicId) {
		List<_HolefaceSettingInfo> mHolefaceSettingList = new ArrayList<_HolefaceSettingInfo>();
		Cursor cursor = null;
		StringBuilder sql = new StringBuilder("SELECT * FROM LOCAL_HOLO_SETTING_INFO WHERE risk_id='");
		sql.append($riskId).append("'");
		if($holefaicId != null) {
			sql.append("AND holeface_id='");
			sql.append($holefaicId).append("'");
		}
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery(sql.toString(), null);
			while(cursor.moveToNext()) {
				_HolefaceSettingInfo mSetting = new _HolefaceSettingInfo();
				//String $riskId = cursor.getString(cursor.getColumnIndex("risk_id"));
				
				mSetting.set$id(cursor.getString(cursor.getColumnIndex("_ID")));
				mSetting.set$holefaceId(cursor.getString(cursor.getColumnIndex("holeface_id")));
				mSetting.set$riskId($riskId);
				mSetting.setIsCheck(cursor.getString(cursor.getColumnIndex("isCheck")));
				mSetting.setRiskHoleName(cursor.getString(cursor.getColumnIndex("risk_hole")));
				mSetting.setRiskTypeName(cursor.getString(cursor.getColumnIndex("risk_type_name")));
				
				mHolefaceSettingList.add(mSetting);
			}
			return mHolefaceSettingList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	/** 12.掌子面风险配置信息表-删除数据 */
	public boolean deleteHolefaceSettingInfo(String $riskId) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_HOLO_SETTING_INFO WHERE risk_id = '" + $riskId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 12.掌子面风险配置信息表-修改数据   */
	public boolean updateHolefaceSettingInfo(String $holefaceInfoId,String isCheck, String $riskId, String mClassType) {
		StringBuilder sql = new StringBuilder("update LOCAL_HOLO_SETTING_INFO set isCheck=" + isCheck + " where 1=1");
		if($riskId != null) {
			sql.append(" AND risk_id='").append($riskId).append("'");
		}
		if($holefaceInfoId != null) {
			sql.append(" AND holeface_id='").append($holefaceInfoId).append("'");
		}
		if(mClassType != null) {
			sql.append(" AND risk_type_name='").append(mClassType).append("'");
		}
		try {
		    dbHelper.getWritableDatabase().execSQL(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	//LOCAL_RISK_SETTINGS_RECORD   _HolefaceSettingRecord
	/** 13.掌子面风险配置具体信息表-插入数据  */
	public Long insertHolefaceSettingRecord(_HolefaceSettingRecord mRecord) {
		ContentValues values = new ContentValues();
		values.put("riskId", mRecord.get$riskId());
		values.put("holefaceId", mRecord.get$holefaceId());
		values.put("holefaceSettingInfoId", mRecord.get$holefaceSettingInfoId());
		values.put("rowId", mRecord.get$rowId());
		values.put("subId", mRecord.get$subId());
		values.put("oneType", mRecord.getOneType());
		values.put("selectedType", mRecord.getSelectedType());
		values.put("typeNum", mRecord.getTypeNum());
		values.put("riskType", mRecord.getRiskType());
		values.put("currentState", mRecord.getCurrentState());
		try {
			Long id = dbHelper.getIdInsert(values,"LOCAL_RISK_SETTINGS_RECORD");
			if(id != 0){
				return id;
			}else{
				return 0L;
			}
		} catch (Exception e) {
			return 0L;
		}finally{
			closeDBHepler();
		}
	}
	
	/** 13.掌子面风险配置具体信息表-查询数据  */
	public List<String> getHolefaceRecordsByOneQuota(String $riskId, String $holefaceId, String riskType) {
		List<String> mOneQuotaList = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT oneType FROM LOCAL_RISK_SETTINGS_RECORD WHERE holefaceId='" + $holefaceId + "' AND riskId='" + $riskId + "' AND  riskType='" + riskType + "' GROUP BY oneType", null);
			while(cursor.moveToNext()) {
				String oneType = cursor.getString(cursor.getColumnIndex("oneType"));
				mOneQuotaList.add(oneType);
			}
			return mOneQuotaList;
		} catch (Exception e) {
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	//
	public void updateFlushRecords(String $riskId, String $holefaceId, String riskType) {
		List<_HolefaceSettingRecord> mRecordList = getHolefaceRecordsBy( $riskId, $holefaceId, riskType, null);
		if(mRecordList != null && mRecordList.size() > 0) {
			for(int i = 0; i < mRecordList.size(); i ++) {
				_HolefaceSettingRecord mRecord = mRecordList.get(i);
				if(null != mRecord) {
					_HolefaceSetting mSetting = new DBService(context).findRiskSettingsById(mRecord.get$rowId());
					if(null != mSetting) {
						new DBService(context).updateHolefaceSettingOneRecord(mRecord.get$id(),mSetting.getClass1(),"1",mSetting.getClass1());
					}
				}
			}
		}
	}
	
	/** 13.掌子面风险配置具体信息表-查询数据  */
	public List<_HolefaceSettingRecord> getHolefaceRecordsBy(String $riskId, String $holefaceId, String riskType, String oneType) {
		List<_HolefaceSettingRecord> mOneQuotaList = new ArrayList<_HolefaceSettingRecord>();
		Cursor cursor = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT * FROM LOCAL_RISK_SETTINGS_RECORD WHERE riskId='" + $riskId + "' AND holefaceId='" + $holefaceId + "'");
			if(riskType != null) {
				buffer.append(" AND riskType='" + riskType + "' ");
			}
			if(oneType != null) {
				buffer.append(" AND oneType='" + oneType + "'");
			}
			cursor = dbHelper.getReadableDatabase().rawQuery(buffer.toString(), null);
			
			while(cursor.moveToNext()) {
				_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
				
				mRecord.set$id(cursor.getString(cursor.getColumnIndex("_ID")));
				mRecord.set$holefaceId($holefaceId);
				mRecord.set$holefaceSettingInfoId(cursor.getString(cursor.getColumnIndex("holefaceSettingInfoId")));
				mRecord.set$riskId($riskId);
				mRecord.set$rowId(cursor.getString(cursor.getColumnIndex("rowId")));
				mRecord.set$subId(cursor.getString(cursor.getColumnIndex("subId")));
				mRecord.setSelectedType(cursor.getString(cursor.getColumnIndex("selectedType")));
				mRecord.setTypeNum(cursor.getString(cursor.getColumnIndex("typeNum")));
				mRecord.setRiskType(riskType);
				mRecord.setOneType(oneType);
				
				mOneQuotaList.add(mRecord);
			}
			return mOneQuotaList;
		} catch (Exception e) {
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	/** 13.掌子面风险配置具体信息表-查询数据  */
	public List<_HolefaceSettingRecord> getHolefaceRecordsByAllParams(String $riskId, String $holefaceId, String riskType, String oneType) {
		List<_HolefaceSettingRecord> mOneQuotaList = new ArrayList<_HolefaceSettingRecord>();
		Cursor cursor = null;
		Cursor cursor2 = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT * FROM LOCAL_RISK_SETTINGS_RECORD WHERE riskId='" + $riskId + "' AND holefaceId='" + $holefaceId + "' AND subId='0'" );
			if(riskType != null) {
				buffer.append(" AND riskType='" + riskType + "' ");
			}
			if(oneType != null) {
				buffer.append(" AND oneType='" + oneType + "'");
			}
			cursor = dbHelper.getReadableDatabase().rawQuery(buffer.toString(), null);
			while(cursor.moveToNext()) {
				_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
				String $id = cursor.getString(cursor.getColumnIndex("_ID"));
				String $holefaceSettingInfoId = cursor.getString(cursor.getColumnIndex("holefaceSettingInfoId"));
				String $rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				String $subId = cursor.getString(cursor.getColumnIndex("subId"));
				String selectedType = cursor.getString(cursor.getColumnIndex("selectedType"));
				String typeNum = cursor.getString(cursor.getColumnIndex("typeNum"));
				String currentState = cursor.getString(cursor.getColumnIndex("currentState"));
				String moneType = cursor.getString(cursor.getColumnIndex("oneType"));
				String mriskType = cursor.getString(cursor.getColumnIndex("riskType"));
				
				mRecord.set$id($id);
				mRecord.set$holefaceId($holefaceId);
				mRecord.set$holefaceSettingInfoId($holefaceSettingInfoId);
				mRecord.set$riskId($riskId);
				mRecord.set$rowId($rowId);
				mRecord.set$subId($subId);
				mRecord.setSelectedType(selectedType);
				mRecord.setTypeNum(typeNum);
				mRecord.setRiskType(mriskType);
				mRecord.setOneType(moneType);
				mRecord.setCurrentState(currentState);
				
				String sql = "SELECT * FROM LOCAL_RISK_SETTINGS_RECORD WHERE riskId='" + $riskId + "' AND holefaceId='" + $holefaceId + "' AND subId='"+ $id +"' AND riskType='" + riskType + "'";
				cursor2 = dbHelper.getReadableDatabase().rawQuery(sql, null);
				
				List<_HolefaceSettingRecord> mRiskRecordList = new ArrayList<_HolefaceSettingRecord>();
				while(cursor2.moveToNext()) {
					_HolefaceSettingRecord mMRecord = new _HolefaceSettingRecord();
					String $id2 = cursor2.getString(cursor2.getColumnIndex("_ID"));
					String $holefaceSettingInfoId2 = cursor2.getString(cursor2.getColumnIndex("holefaceSettingInfoId"));
					String $rowId2 = cursor2.getString(cursor2.getColumnIndex("rowId"));
					String $subId2 = cursor2.getString(cursor2.getColumnIndex("subId"));
					String selectedType2 = cursor2.getString(cursor2.getColumnIndex("selectedType"));
					String typeNum2 = cursor2.getString(cursor2.getColumnIndex("typeNum"));
					String currentState2 = cursor2.getString(cursor2.getColumnIndex("currentState"));
					String mOneType = cursor2.getString(cursor2.getColumnIndex("oneType"));
					String mRiskType = cursor2.getString(cursor2.getColumnIndex("riskType"));
					
					mMRecord.set$id($id2);
					mMRecord.set$holefaceId($holefaceId);
					mMRecord.set$holefaceSettingInfoId($holefaceSettingInfoId2);
					mMRecord.set$riskId($riskId);
					mMRecord.set$rowId($rowId2);
					mMRecord.set$subId($subId2);
					mMRecord.setSelectedType(selectedType2);
					mMRecord.setTypeNum(typeNum2);
					mMRecord.setRiskType(mRiskType);
					mMRecord.setOneType(mOneType);
					mMRecord.setCurrentState(currentState2);
					mRiskRecordList.add(mMRecord);
				}
				mRecord.setmHolefaceRecordList(mRiskRecordList);
				mOneQuotaList.add(mRecord);
			}
			return mOneQuotaList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			if(cursor2 != null) {
				cursor2.close();
				cursor2 = null;
			}
			closeDBHepler();
		}
	}
	
	
	
	
	
	/** 13.掌子面风险配置具体信息表-删除数据  */
	public boolean deleteHolefaceSettingRecord(String mRiskId) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_RISK_SETTINGS_RECORD WHERE riskId = '" + mRiskId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/** 13.掌子面风险配置具体信息表-更新数据  */
	public boolean updateHolefaceSettingRecord(String $id,String mClassType,String mClassTypeNum) {
		try {
			dbHelper.getWritableDatabase().execSQL("UPDATE LOCAL_RISK_SETTINGS_RECORD set classType='" + mClassType +"', classTypeNum='"+ mClassTypeNum+ "' WHERE _ID = '" + $id + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 13.掌子面风险配置具体信息表-更新数据  */
	public boolean updateHolefaceSettingOneRecord(String $id,String currntState,String mTypeNum,String mSelectType) {
		try {
			dbHelper.getWritableDatabase().execSQL("UPDATE LOCAL_RISK_SETTINGS_RECORD set currentState='" + currntState +"', typeNum='"+ mTypeNum +  "', selectedType='" + mSelectType + "' WHERE _ID = '" + $id + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	/** 13.掌子面风险配置具体信息表-更新数据  */
	public boolean updateHolefaceSettingRecords(String $riskId, String $holefaceId, String iType) {
		try {
			dbHelper.getWritableDatabase().execSQL("UPDATE LOCAL_RISK_SETTINGS_RECORD SET classType='' WHERE i_type='" + iType + "' AND riskId='" + $riskId + "' AND holefaceId='" + $holefaceId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/** 查询 工程标段*/ 
	public List<_Item> selectItemSec() {
		List<_Item> itemList = new ArrayList<_Item>();
		Cursor cursor = null;
		Cursor cursor2 = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT iid,iContent,iAttributes FROM LOCAL_ITEM_SECTION GROUP BY iid",null);
			while(cursor.moveToNext()) {
				_Item item = new _Item();
				String iid = cursor.getString(cursor.getColumnIndex("iid"));
				String iContent = cursor.getString(cursor.getColumnIndex("iContent"));
				String iAttributes = cursor.getString(cursor.getColumnIndex("iAttributes"));
				
				item.setId(iid);
				item.setAttributes(iAttributes);
				item.setText(iContent);
				
				cursor2 = dbHelper.getReadableDatabase().rawQuery("SELECT sid,sContent,sAttributes FROM LOCAL_ITEM_SECTION where iid = '"+ iid +"'", null);
				while(cursor2.moveToNext()) {
					_Section section = new _Section();
					String sid = cursor2.getString(cursor2.getColumnIndex("sid"));
					String sContent = cursor2.getString(cursor2.getColumnIndex("sContent"));
					String sAttributes = cursor2.getString(cursor2.getColumnIndex("sAttributes"));
					if(sid != null && !"".equals(sid)) {
						section.setId(sid);
						section.setIid(iid);
						section.setAttributes(sAttributes);
						section.setText(sContent);
						
						item.getSecList().add(section);
					}
				}
				itemList.add(item);
			}
		} catch (Exception e) {
			return itemList;
		}finally{
			if(cursor2 != null) {
				cursor2.close();
				cursor2 = null;
			}
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return itemList;
	}
	@Deprecated
	public List<_ItemSec> getItemList() {
		List<_ItemSec> itemList = new ArrayList<_ItemSec>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT iid,iContent,iAttributes FROM LOCAL_ITEM_SECTION GROUP BY iid",null);
			while(cursor.moveToNext()) {
				_ItemSec item = new _ItemSec();
				String iid = cursor.getString(cursor.getColumnIndex("iid"));
				String iContent = cursor.getString(cursor.getColumnIndex("iContent"));
				String iAttributes = cursor.getString(cursor.getColumnIndex("iAttributes"));
				
				item.setIid(iid);
				item.setIattributes(iAttributes);
				item.setItext(iContent);
				
				itemList.add(item);
			}
			return itemList;
		} catch (Exception e) {
			return itemList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	public List<_ListObj> getItemListByObj() {
		List<_ListObj> itemList = new ArrayList<_ListObj>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT iid,iContent,iAttributes FROM LOCAL_ITEM_SECTION GROUP BY iid",null);
			while(cursor.moveToNext()) {
				_ListObj item = new _ListObj();
				item.setId(cursor.getString(cursor.getColumnIndex("iid")));
				item.setText(cursor.getString(cursor.getColumnIndex("iContent")));
				itemList.add(item);
			}
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
			itemList = null;
			return null;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/**
	 * @Date 2014/07/21 15:47
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getSectionList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT sid,sContent,UpdateTime,IsUpdate FROM LOCAL_ITEM_SECTION ORDER BY sContent";
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				String sid = c.getString(c.getColumnIndex("sid"));
				map.put("sid", sid);
				String mSContent = c.getString(c.getColumnIndex("sContent"));
				map.put("sContent", mSContent);
				map.put("Allcode", mSContent);
				map.put("AllName", mSContent);
				map.put("UpdateTime", c.getString(c.getColumnIndex("UpdateTime")));
				map.put("IsUpdate", c.getString(c.getColumnIndex("IsUpdate")));
				map.put("RowId", sid);
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		}finally{
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
	}
	
	
	public ArrayList<HashMap<String, String>> getSectionListByMeasure() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT sid,sContent,UpdateTime,IsUpdate FROM LOCAL_ITEM_SECTION WHERE sid != 'd9ddff58-adc9-4f93-b451-e0b814e5acad' ORDER BY sContent";
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				String sid = c.getString(c.getColumnIndex("sid"));
				map.put("sid", sid);
				String mSContent = c.getString(c.getColumnIndex("sContent"));
				map.put("sContent", mSContent);
				map.put("Allcode", mSContent);
				map.put("AllName", mSContent);
				map.put("UpdateTime", c.getString(c.getColumnIndex("UpdateTime")));
				map.put("IsUpdate", c.getString(c.getColumnIndex("IsUpdate")));
				map.put("RowId", sid);
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		}finally{
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean updateALLSectionUpdate(String state) {
		String sql = "UPDATE LOCAL_ITEM_SECTION SET IsUpdate=" + state;
		try {
		    dbHelper.getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	public boolean updateALLSectionUpdateTime(String mSectionId) {
		StringBuilder sql = new StringBuilder("UPDATE LOCAL_ITEM_SECTION SET UpdateTime=''");
		if(mSectionId != null) {
			sql.append(" WHERE sid='");
			sql.append(mSectionId);
			sql.append("'");
		}
		try {
		    dbHelper.getWritableDatabase().execSQL(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeDBHepler();
		}
		return true;
	}
	
	/**
	 * 
	 * @param id
	 * @param state
	 * @return
	 */
	public boolean updateSectionUpdateById(String id, String state) {
		String sql = String.format("UPDATE LOCAL_ITEM_SECTION SET IsUpdate=%s WHERE sid='%s'", state, id);
		try {
		    dbHelper.getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	public boolean getSectionById(String mSectionId) {
		String sql = String.format("SELECT UpdateTime FROM LOCAL_ITEM_SECTION WHERE sid='%s'", mSectionId);
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				String s = c.getString(0);
				if(s != null && !"".equals(s)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return false;
	}
	
	
	
	
	public boolean updateSectionUpdateTimeById(String id) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String sql = String.format("UPDATE LOCAL_ITEM_SECTION SET UpdateTime='%s' WHERE sid='%s'", date, id);
		try {
		    dbHelper.getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/*public List<_Section> getSectionList(String iid) {
		List<_Section> itemList = new ArrayList<_Section>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT sid,sContent,sAttributes FROM LOCAL_ITEM_SECTION where iid = '"+ iid +"'", null);
			while(cursor.moveToNext()) {
				_Section section = new _Section();
				String sid = cursor.getString(cursor.getColumnIndex("sid"));
				String sContent = cursor.getString(cursor.getColumnIndex("sContent"));
				String sAttributes = cursor.getString(cursor.getColumnIndex("sAttributes"));
				
				section.setId(sid);
				section.setIid(iid);
				section.setAttributes(sAttributes);
				section.setText(sContent);
				
				itemList.add(section);
			}
			return itemList;
		} catch (Exception e) {
			return itemList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}*/
	public List<_ListObj> getSectionListByObj(String iid) {
		List<_ListObj> itemList = new ArrayList<_ListObj>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT sid,sContent,sAttributes FROM LOCAL_ITEM_SECTION where iid = '"+ iid +"'", null);
			while(cursor.moveToNext()) {
				_ListObj section = new _ListObj();
				String sid = cursor.getString(cursor.getColumnIndex("sid"));
				String sContent = cursor.getString(cursor.getColumnIndex("sContent"));
				
				section.setId(sid);
				section.setText(sContent);
				itemList.add(section);
			}
			return itemList;
		} catch (Exception e) {
			return itemList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	/**
	 * @param sid
	 * @return
	 */
	public List<_ListObj> getTunnelListByObj(String sid) {
		List<_ListObj> proList = new ArrayList<_ListObj>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT rowId,aName,aPosition,aPositionMile FROM LOCAL_PROJECT where aType = '隧道' AND aSecid = '"+ sid +"'", null);
			while(cursor.moveToNext()) {
				_ListObj obj = new _ListObj();
				String id = cursor.getString(cursor.getColumnIndex("rowId"));
				String sContent = cursor.getString(cursor.getColumnIndex("aName"));
				String aPosition = cursor.getString(cursor.getColumnIndex("aPosition"));
				String aPositionMile = cursor.getString(cursor.getColumnIndex("aPositionMile"));
				obj.setId(id);
				obj.setText(sContent);
				obj.setaPosition(aPosition);
				obj.setaPositionMile(aPositionMile);
				proList.add(obj);
			}
			return proList;
		} catch (Exception e) {
			return proList;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	/**
	 * 通讯录表-查询数据
	 * @param mMRouteList
	 * @param mMRouteIsVisiable
	 * @param type 0：部门、1：全部
	 * @return
	 */
	public List<_ContactsData> selectContacts(ArrayList<String> mMRouteList, boolean mMRouteIsVisiable, int type) {
		List<_ContactsData> conList = new ArrayList<_ContactsData>();
		Cursor cursor = null;
		String sql = "";
		if(type == 0) {
			sql = "SELECT * FROM LOCAL_CONTACTS ORDER BY dptPinyin COLLATE NOCASE";
		} else {
			sql = "SELECT * FROM LOCAL_CONTACTS ORDER BY empPinyin COLLATE NOCASE";
		}
		
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
			while(cursor.moveToNext()) {
				_ContactsData contacts = new _ContactsData();
				String id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
				String dptName = cursor.getString(cursor.getColumnIndex("dptName"));
				String dptTel = cursor.getString(cursor.getColumnIndex("dptTel"));
				String dptFax = cursor.getString(cursor.getColumnIndex("dptFax"));
				String empName = cursor.getString(cursor.getColumnIndex("empName"));
				String empContact = cursor.getString(cursor.getColumnIndex("empContact"));
				String empPinyin = cursor.getString(cursor.getColumnIndex("empPinyin"));
				String dptPinyin = cursor.getString(cursor.getColumnIndex("dptPinyin"));
				String rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				
				contacts.set$id(id);
				contacts.setRowId(rowId);
				contacts.setDptName(dptName);
				contacts.setDptTel(dptTel);
				contacts.setDptFax(dptFax);
				contacts.setEmpContact(empContact);
				contacts.setEmpPinyin(empPinyin);
				contacts.setDptPinyin(dptPinyin);
				contacts.setEmpName(empName);
				
				
				if(mMRouteIsVisiable) {
					if(mMRouteList != null && mMRouteList.size() > 0) {
						for(String s : mMRouteList) {
							if(rowId.equals(s)) {
								conList.add(contacts);
							}
						}
					}
				} else {
					conList.add(contacts);
				}
			}
		}catch (Exception e) {
			return conList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return conList;
	}
	
	/** 查询数据条数 */
	@Deprecated
	public int getTabRows(String tabName) {
		int rows = 0;
		Cursor cursor = null;
		try {
			cursor = dbHelper.count(tabName);
			rows = cursor.getCount();
		} catch(Exception e) {
			return rows;
		}finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return rows;
	}
	
	
	/** 通讯录表-查询数据 */
	/*public List<_Contacts> selectContacts2(ArrayList<String> mMRouteList, boolean mMRouteIsVisiable) {
		List<_Contacts> conList = new ArrayList<_Contacts>();
		Cursor cursor = null;
		Cursor cursor2 = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT dptName,dptTel,dptFax,dptPinyin FROM LOCAL_CONTACTS group by dptName", null);
			while(cursor.moveToNext()) {
				_Contacts contacts = new _Contacts();
				String dptName = cursor.getString(cursor.getColumnIndex("dptName"));
				String dptTel = cursor.getString(cursor.getColumnIndex("dptTel"));
				String dptFax = cursor.getString(cursor.getColumnIndex("dptFax"));
				String dptPinyin = cursor.getString(cursor.getColumnIndex("dptPinyin"));
				
				contacts.setDptName(dptName);
				contacts.setDptTel(dptTel);
				contacts.setDptFax(dptFax);
				contacts.setDptNamePinyin(dptPinyin);
				
				
				cursor2 = dbHelper.getReadableDatabase().rawQuery("SELECT empName,empContact,empPinyin,rowId FROM LOCAL_CONTACTS where dptName='"+dptName+"'", null);
				while(cursor2.moveToNext()) {
					_ContactsEmp emp = new _ContactsEmp();
					String empName = cursor2.getString(cursor2.getColumnIndex("empName"));
					String empContact = cursor2.getString(cursor2.getColumnIndex("empContact"));
					String empPinyin = cursor2.getString(cursor2.getColumnIndex("empPinyin"));
					String rowId = cursor2.getString(cursor2.getColumnIndex("rowId"));
					
					emp.setEmpName(empName);
					emp.setEmpContact(empContact);
					emp.setEmpPinyin(empPinyin);
					emp.setRowId(rowId);
					
					if(mMRouteIsVisiable && mMRouteList != null && mMRouteList.size() > 0) {
						for(String s : mMRouteList) {
							if(rowId.equals(s)) {
								contacts.getEmpList().add(emp);
							}
						}
					} else {
						contacts.getEmpList().add(emp);
					//}
				}
				
				conList.add(contacts);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return conList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			if(cursor2 != null) {
				cursor2.close();
				cursor2 = null;
			}
			closeDBHepler();
		}
		return conList;
	}*/
	
	
	/** 通讯录表-查询1数据 */
	@Deprecated
	public _ContactsData selectContactsByRowid(String rowid) {
		_ContactsData contacts = new _ContactsData();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("select * from LOCAL_CONTACTS where rowId = '" + rowid + "'", null);
			while(cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
				String dptName = cursor.getString(cursor.getColumnIndex("dptName"));
				String dptTel = cursor.getString(cursor.getColumnIndex("dptTel"));
				String dptFax = cursor.getString(cursor.getColumnIndex("dptFax"));
				String empName = cursor.getString(cursor.getColumnIndex("empName"));
				String empContact = cursor.getString(cursor.getColumnIndex("empContact"));
				String empPinyin = cursor.getString(cursor.getColumnIndex("empPinyin"));
				String dptPinyin = cursor.getString(cursor.getColumnIndex("dptPinyin"));
				String rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				
				contacts.set$id(id);
				contacts.setRowId(rowId);
				contacts.setDptName(dptName);
				contacts.setDptTel(dptTel);
				contacts.setDptFax(dptFax);
				contacts.setEmpContact(empContact);
				contacts.setEmpPinyin(empPinyin);
				contacts.setDptPinyin(dptPinyin);
				contacts.setEmpName(empName);
			}
		}catch (Exception e) {
			return contacts;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return contacts;
	}
	
	
	
	/** 风险查询表-插入数据 */
	public List<_Check> selectCheckList() {
		List<_Check> checkList = new ArrayList<_Check>();
		Cursor cursor = null;
		try {
				cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_CHECK ORDER BY inTime DESC",null);
				while(cursor.moveToNext()) {
					_Check check = new _Check();
					int id = cursor.getInt(cursor.getColumnIndex("_ID"));//区分大小写
					String aProjectId = cursor.getString(cursor.getColumnIndex("aProjectId"));
					String isUpdate = cursor.getString(cursor.getColumnIndex("isUpdate"));
					String checkContent = cursor.getString(cursor.getColumnIndex("checkContent"));
					String upTime = cursor.getString(cursor.getColumnIndex("upTime"));
					String aStartMile = cursor.getString(cursor.getColumnIndex("aStartMile"));
					String aEndMile = cursor.getString(cursor.getColumnIndex("aEndMile"));
					String aProName = cursor.getString(cursor.getColumnIndex("aProName"));
					String checkId = cursor.getString(cursor.getColumnIndex("checkId"));
					
					String inTime = cursor.getString(cursor.getColumnIndex("inTime"));
					String aSecName = cursor.getString(cursor.getColumnIndex("aSecName"));
					String aItemName = cursor.getString(cursor.getColumnIndex("aItemName"));
					String aItemId = cursor.getString(cursor.getColumnIndex("aItemId"));
					String aSecId = cursor.getString(cursor.getColumnIndex("aSecId"));
					
					
					check.setId(id);
					check.setaProjectId(aProjectId);
					check.setIsUpdate(isUpdate);
					check.setCheckContent(checkContent);
					check.setUpTime(upTime);
					check.setaStartMile(aStartMile);
					check.setaEndMile(aEndMile);
					check.setaProName(aProName);
					check.setCheckId(checkId);
					check.setInTime(inTime);
					check.setaSecId(aSecId);
					check.setaSecName(aSecName);
					check.setaItemId(aItemId);
					check.setaItemName(aItemName);
					
					checkList.add(check);
				}
			} catch (Exception e) {
				return checkList;
			}finally{
				if(cursor != null) {
					cursor.close();
					cursor = null;
				}
				closeDBHepler();
			}
			return checkList;
	}
	/** 工程信息-获取列表 */
	@Deprecated
	public List<_Project> selectProject() {
		List<_Project> proList = new ArrayList<_Project>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.query("LOCAL_PROJECT");
			while(cursor.moveToNext()) {
				_Project project = new _Project();
				
				String id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
				String rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				String aName = cursor.getString(cursor.getColumnIndex("aName"));
				String aPid = cursor.getString(cursor.getColumnIndex("aPid"));
				String aSecid = cursor.getString(cursor.getColumnIndex("aSecid"));
				
				String aType = cursor.getString(cursor.getColumnIndex("aType"));
				String aPmName = cursor.getString(cursor.getColumnIndex("aPmName"));
				String aPContract = cursor.getString(cursor.getColumnIndex("aPContract"));
				String aConstruct = cursor.getString(cursor.getColumnIndex("aConstruct"));
				String aStartMile = cursor.getString(cursor.getColumnIndex("aStartMile"));
				String aEndMile = cursor.getString(cursor.getColumnIndex("aEndMile"));
				String aPositionMile = cursor.getString(cursor.getColumnIndex("aPositionMile"));
				String aPosition = cursor.getString(cursor.getColumnIndex("aPosition"));
				
				project.setId(id);
				project.setRowId(rowId);
				project.setaName(aName);
				project.setaPid(aPid);
				project.setaSecid(aSecid);
				
				project.setaType(aType);
				project.setaPmName(aPmName);
				project.setaPContract(aPContract);
				project.setaConstruct(aConstruct);
				project.setaStartMile(aStartMile);
				project.setaEndMile(aEndMile);
				project.setaPositionMile(aPositionMile);
				project.setaPosition(aPosition);
				
				proList.add(project);
			}
		}catch (Exception e) {
			return proList;
		}finally{
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return proList;
	}
	/** 获取工程信息 by rowid */
	@Deprecated
	public _Project selectProByRowId(String rowId) {
		_Project project = new _Project();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_PROJECT WHERE rowId ='" + rowId + "'",null);
			if(cursor.moveToFirst()) {
				
				String id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
				//String rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				String aName = cursor.getString(cursor.getColumnIndex("aName"));
				String aPid = cursor.getString(cursor.getColumnIndex("aPid"));
				String aSecid = cursor.getString(cursor.getColumnIndex("aSecid"));
				
				String aType = cursor.getString(cursor.getColumnIndex("aType"));
				String aPmName = cursor.getString(cursor.getColumnIndex("aPmName"));
				String aPContract = cursor.getString(cursor.getColumnIndex("aPContract"));
				String aConstruct = cursor.getString(cursor.getColumnIndex("aConstruct"));
				String aStartMile = cursor.getString(cursor.getColumnIndex("aStartMile"));
				String aEndMile = cursor.getString(cursor.getColumnIndex("aEndMile"));
				String aPositionMile = cursor.getString(cursor.getColumnIndex("aPositionMile"));
				String aPosition = cursor.getString(cursor.getColumnIndex("aPosition"));
				
				project.setId(id);
				project.setRowId(rowId);
				project.setaName(aName);
				project.setaPid(aPid);
				project.setaSecid(aSecid);
				
				project.setaType(aType);
				project.setaPmName(aPmName);
				project.setaPContract(aPContract);
				project.setaConstruct(aConstruct);
				project.setaStartMile(aStartMile);
				project.setaEndMile(aEndMile);
				project.setaPositionMile(aPositionMile);
				project.setaPosition(aPosition);
				
			}
		}catch (Exception e) {
			return project;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return project;
	}
	public List<_Project> selectProList(String aSecid) {
		List<_Project> mProList = new ArrayList<_Project>();
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_PROJECT WHERE aSecid ='" + aSecid + "'",null);
			if(cursor.moveToFirst()) {
				_Project project = new _Project();
				String id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
				String rowId = cursor.getString(cursor.getColumnIndex("rowId"));
				String aName = cursor.getString(cursor.getColumnIndex("aName"));
				String aPid = cursor.getString(cursor.getColumnIndex("aPid"));
				//String aSecid = cursor.getString(cursor.getColumnIndex("aSecid"));
				
				String aType = cursor.getString(cursor.getColumnIndex("aType"));
				String aPmName = cursor.getString(cursor.getColumnIndex("aPmName"));
				String aPContract = cursor.getString(cursor.getColumnIndex("aPContract"));
				String aConstruct = cursor.getString(cursor.getColumnIndex("aConstruct"));
				String aStartMile = cursor.getString(cursor.getColumnIndex("aStartMile"));
				String aEndMile = cursor.getString(cursor.getColumnIndex("aEndMile"));
				String aPositionMile = cursor.getString(cursor.getColumnIndex("aPositionMile"));
				String aPosition = cursor.getString(cursor.getColumnIndex("aPosition"));
				
				project.setId(id);
				project.setRowId(rowId);
				project.setaName(aName);
				project.setaPid(aPid);
				project.setaSecid(aSecid);
				
				project.setaType(aType);
				project.setaPmName(aPmName);
				project.setaPContract(aPContract);
				project.setaConstruct(aConstruct);
				project.setaStartMile(aStartMile);
				project.setaEndMile(aEndMile);
				project.setaPositionMile(aPositionMile);
				project.setaPosition(aPosition);
				
				mProList.add(project);
			}
		}catch (Exception e) {
			return null;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return mProList;
	}
	/** 获取检查信息-图片列表by id */
	public List<_LocalPhotos> getPhotosList(String id) {
		
		if(id == null) {
			return null;
		}
		List<_LocalPhotos> photosList = new ArrayList<_LocalPhotos>();
		Cursor cursor = null;
		try {
				cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_PHOTOS WHERE check_tab_id='" + id +"' ORDER BY _ID",null);
				while(cursor.moveToNext()) {
					
					_LocalPhotos photos = new _LocalPhotos();
					String $id = cursor.getString(cursor.getColumnIndex("_ID"));//区分大小写
					String check_tab_id = cursor.getString(cursor.getColumnIndex("check_tab_id"));
					String checkId = cursor.getString(cursor.getColumnIndex("checkId"));
					String local_img_path = cursor.getString(cursor.getColumnIndex("local_img_path"));
					String remote_img_path = cursor.getString(cursor.getColumnIndex("remote_img_path"));
					String remote_img_id = cursor.getString(cursor.getColumnIndex("remote_img_id"));
					
					photos.setId($id);
					photos.setCheck_tab_id(check_tab_id);
					photos.setCheckId(checkId);
					photos.setLocal_img_path(local_img_path);
					photos.setRemote_img_id(remote_img_id);
					photos.setRemote_img_path(remote_img_path);
					
					photosList.add(photos);
				}
			} catch (Exception e) {
				return photosList;
			}finally{
				if(cursor != null) {
					cursor.close();
					cursor = null;
				}
				closeDBHepler();
			}
			return photosList;
	}
	
	/** 判断表是否为空 */
	public boolean isEmpty(String tabname) {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + tabname,null);
			if (cursor != null && cursor.getCount() != 0) {
				return false;
			} else {
				return true;
			}
		}catch (Exception e) {
			return true;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
	}
	
	/** 获取请判断是否为空 */
	public _User getCurrentUser() {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_USER ORDER BY LASTTIME DESC",null);
			if (cursor.moveToFirst()) {
				_User localUser = new _User();
				String userid = cursor.getString(cursor.getColumnIndex("USERID"));//区分大小写
				String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
				String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
				String department = cursor.getString(cursor.getColumnIndex("DEPARTMENT"));
				String lastLoginTime = cursor.getString(cursor.getColumnIndex("LASTTIME"));
				
				String dptRowId = cursor.getString(cursor.getColumnIndex("DPTROWID"));
				String empLevel = cursor.getString(cursor.getColumnIndex("EMPLEVEL"));
				String empName = cursor.getString(cursor.getColumnIndex("EMPNAME"));
				
				int savePassword = cursor.getInt(cursor.getColumnIndex("SAVEPASSWORD"));
				int autoLogin = cursor.getInt(cursor.getColumnIndex("AUTOLOGIN"));
				String phone = cursor.getString(cursor.getColumnIndex("PHONE"));
				String nickname = cursor.getString(cursor.getColumnIndex("NICKNAME"));
				String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
				String address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
				int sex = cursor.getInt(cursor.getColumnIndex("SEX"));
				String birthday = cursor.getString(cursor.getColumnIndex("BIRTHDAY"));
				
				int isUpdate = cursor.getInt(cursor.getColumnIndex("IsUpdate"));
				int isGps = cursor.getInt(cursor.getColumnIndex("IsGps"));
				
				String contactsTime = cursor.getString(cursor.getColumnIndex("ContactsTime"));
				String itemSecTime = cursor.getString(cursor.getColumnIndex("ItemSecTime"));
				String projectTime = cursor.getString(cursor.getColumnIndex("ProjectTime"));
				String riskTypeTime = cursor.getString(cursor.getColumnIndex("RiskTypeTime"));
				
				localUser.setUserid(userid);
				localUser.setDepartment(department);
				localUser.setLasttime(lastLoginTime);
				localUser.setUsername(username);
				localUser.setPassword(password);
				
				localUser.setSavepassword(savePassword);
				localUser.setAutologin(autoLogin);
				localUser.setPhone(phone);
				localUser.setNickname(nickname);
				localUser.setEmail(email);
				
				localUser.setAddress(address);
				localUser.setBirthday(birthday);
				localUser.setSex(sex);
				localUser.setDptRowId(dptRowId);
				localUser.setEmpLevel(empLevel);
				localUser.setEmpName(empName);
				
				localUser.setIsUpdate(isUpdate);
				localUser.setIsGps(isGps);
				
				localUser.setContactsTime(contactsTime);
				localUser.setItemSecTime(itemSecTime);
				localUser.setProjectTime(projectTime);
				localUser.setRiskTypeTime(riskTypeTime);
				
				
				return localUser;
			}
			
		}catch (Exception e) {
			return null;
		} finally {
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return null;
	}
	
	/** 对登录时，查询数据库localUser表   */
	public _User findUser(String userid) {
		Cursor cursor = null;
		try {
			cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from LOCAL_USER where USERID=?",
				new String[] { String.valueOf(userid) });
		if (cursor.moveToFirst()) {
			_User localUser = new _User();
			int id = cursor.getInt(cursor.getColumnIndex("_ID"));
			//int userid = cursor.getInt(cursor.getColumnIndex("USERID"));//区分大小写
			String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
			String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
			String department = cursor.getString(cursor.getColumnIndex("DEPARTMENT"));
			String lastLoginTime = cursor.getString(cursor.getColumnIndex("LASTTIME"));
			
			int savePassword = cursor.getInt(cursor.getColumnIndex("SAVEPASSWORD"));
			int autoLogin = cursor.getInt(cursor.getColumnIndex("AUTOLOGIN"));
			String phone = cursor.getString(cursor.getColumnIndex("PHONE"));
			String nickname = cursor.getString(cursor.getColumnIndex("NICKNAME"));
			String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
			
			String address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
			int sex = cursor.getInt(cursor.getColumnIndex("SEX"));
			String birthday = cursor.getString(cursor.getColumnIndex("BIRTHDAY"));
			
			String dptRowId = cursor.getString(cursor.getColumnIndex("DPTROWID"));
			String empLevel = cursor.getString(cursor.getColumnIndex("EMPLEVEL"));
			String empName = cursor.getString(cursor.getColumnIndex("EMPNAME"));
			
			int isUpdate = cursor.getInt(cursor.getColumnIndex("IsUpdate"));
			int isGps = cursor.getInt(cursor.getColumnIndex("IsGps"));
			
			String contactsTime = cursor.getString(cursor.getColumnIndex("ContactsTime"));
			String itemSecTime = cursor.getString(cursor.getColumnIndex("ItemSecTime"));
			String projectTime = cursor.getString(cursor.getColumnIndex("ProjectTime"));
			String riskTypeTime = cursor.getString(cursor.getColumnIndex("RiskTypeTime"));
			
			localUser.setId(id);
			localUser.setUserid(userid);
			localUser.setDepartment(department);
			localUser.setLasttime(lastLoginTime);
			localUser.setUsername(username);
			localUser.setPassword(password);
			
			localUser.setSavepassword(savePassword);
			localUser.setAutologin(autoLogin);
			localUser.setPhone(phone);
			localUser.setNickname(nickname);
			localUser.setEmail(email);
			
			localUser.setAddress(address);
			localUser.setBirthday(birthday);
			localUser.setSex(sex);
			localUser.setDptRowId(dptRowId);
			localUser.setEmpLevel(empLevel);
			localUser.setEmpName(empName);
			localUser.setIsUpdate(isUpdate);
			localUser.setIsGps(isGps);
			
			localUser.setContactsTime(contactsTime);
			localUser.setItemSecTime(itemSecTime);
			localUser.setProjectTime(projectTime);
			localUser.setRiskTypeTime(riskTypeTime);
			
			return localUser;
		}
		} catch (Exception e) {
			return null;
		}finally {  
			if(cursor != null) {
				cursor.close();
				cursor = null;
			}
			closeDBHepler();
		}
		return null;
	}
	
	/** 登录时更新登录状态 */
	public boolean updateLoginType(_User _u) {
		try {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set USERNAME=?,PASSWORD=?,SAVEPASSWORD=?,AUTOLOGIN=?,LASTTIME=? where USERID=?",
				new Object[] { _u.getUsername(),_u.getPassword(),_u.getSavepassword(),_u.getAutologin(),date,_u.getUserid()});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateDataType(String userId,String isUpdate) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set IsUpdate=? where USERID=?",
				new Object[] { isUpdate,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateGPSType(String userId, String isGps) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set IsGps=? where USERID=?",
				new Object[] { isGps,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateContactTime(String userId, String contactsTime) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set ContactsTime=? where USERID=?",
				new Object[] { contactsTime,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateItemSecTime(String userId, String itemSecTime) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set ItemSecTime=? where USERID=?",
				new Object[] { itemSecTime,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateProjectTime(String userId, String projectTime) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set ProjectTime=? where USERID=?",
				new Object[] { projectTime,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	public boolean updateRiskTime(String userId, String riskTime) {
		try {
		    dbHelper.getWritableDatabase().execSQL(
				"update LOCAL_USER set RiskTypeTime=? where USERID=?",
				new Object[] { riskTime,userId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	//**************************************LOCAL_GPS_PHOTOS******************************//
	/*CREATE TABLE LOCAL_GPS_PHOTOS(_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "section_id TEXT,"
			+ "new_id TEXT,"
			+ "photo_name TEXT,"
			+ "local_img_path TEXT,"
			+ "remote_img_path TEXT,"
			+ "remote_img_id TEXT," 
			+ "state INTEGER DEFAULT 0" +//0:未上传，1：已上传，2：上传失败
			")"*/
	
	public boolean insertGpsPhotos(HashMap<String, String> map) {
		ContentValues values = new ContentValues();
		values.put("section_id", map.get("section_id"));
		values.put("new_id", map.get("new_id"));
		values.put("photo_name", map.get("photo_name"));
		values.put("local_img_path", map.get("local_img_path"));
		//values.put("remote_img_path", map.get("remote_img_path"));
		//values.put("remote_img_id", map.get("remote_img_id"));
		//values.put("state", map.get("state"));
		try {
			if(dbHelper.insert(values,"LOCAL_GPS_PHOTOS")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			Log.e(TAG, "Method:insertGpsPhotos" + e.getMessage());
			return false;
		}finally{
			closeDBHepler();
		}
	}
	
	public boolean insertGpsRemotePhotos(HashMap<String, String> map) {
		ContentValues values = new ContentValues();
		values.put("section_id", map.get("section_id"));
		values.put("new_id", map.get("new_id"));
		values.put("photo_name", map.get("photo_name"));
		values.put("local_img_path", map.get("local_img_path"));
		//values.put("remote_img_path", map.get("remote_img_path"));
		values.put("remote_img_id", map.get("remote_img_id"));
		values.put("state", "1");
		
		try {
			if(dbHelper.insert(values,"LOCAL_GPS_PHOTOS")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			Log.e(TAG, "Method:insertGpsPhotos" + e.getMessage());
			return false;
		}finally{
			closeDBHepler();
		}
	}
	
	
	
	public boolean updateSectionImageName(String mImageName, String id) {
		try {
		    dbHelper.getWritableDatabase().execSQL(String.format("UPDATE LOCAL_GPS_PHOTOS SET photo_name='%s' WHERE _ID=%s", mImageName, id));
		} catch (Exception e) {
			Log.e(TAG, "Method:updateImageName" + e.getMessage());
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	public boolean updateImageState(String mState, String mRemoteId, String id) {
		if(mRemoteId == null || "".equals(mRemoteId) || null == id || "".equals(id)) {
			return false;
		}
		try {
		    dbHelper.getWritableDatabase().execSQL(String.format("UPDATE LOCAL_GPS_PHOTOS SET state=%s,remote_img_id='%s' WHERE _ID=%s", mState, mRemoteId, id));
		} catch (Exception e) {
			Log.e(TAG, "Method:updateImageState" + e.getMessage());
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	public boolean updateImageLoaclInfo(String mPhotoName, String mPhotoLocalPath, String id) {
		try {
		    dbHelper.getWritableDatabase().execSQL(String.format("UPDATE LOCAL_GPS_PHOTOS SET photo_name='%s',local_img_path='%s' WHERE _ID=%s", mPhotoName, mPhotoLocalPath, id));
		} catch (Exception e) {
			Log.e(TAG, "Method:updateImageLoaclInfo" + e.getMessage());
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	
	public HashMap<String, String> getImageByRemoteId(String id) {
		String sql = String.format("SELECT * FROM LOCAL_GPS_PHOTOS WHERE remote_img_id ='%s'", id);
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("_ID")));
				map.put("section_id", c.getString(c.getColumnIndex("section_id")));
				map.put("new_id", c.getString(c.getColumnIndex("section_id")));
				map.put("photo_name", c.getString(c.getColumnIndex("photo_name")));
				map.put("local_img_path", c.getString(c.getColumnIndex("local_img_path")));
				map.put("remote_img_path", c.getString(c.getColumnIndex("remote_img_path")));
				map.put("remote_img_id", c.getString(c.getColumnIndex("remote_img_id")));
				map.put("state", c.getString(c.getColumnIndex("state")));
				return map;
			}
		}catch (Exception e) {
			Log.e(TAG, "Method:getImageByRemoteId, error" + e.getMessage());
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return null;
	}
	
	
	
	/**
	 * 
	 * @param newId 工程编号
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getSectionImageList(String newId) {
		ArrayList<HashMap<String, String>> mImgList = new ArrayList<HashMap<String, String>>();
		Cursor c = null;
		StringBuilder builder = new StringBuilder("SELECT * FROM LOCAL_GPS_PHOTOS WHERE state!=1");
		if(newId != null) {
			builder.append(" AND new_id = '");
			builder.append(newId);
			builder.append("'");
		}
		try {
			c = dbHelper.getReadableDatabase().rawQuery(builder.toString() , null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", c.getString(c.getColumnIndex("_ID")));
				map.put("section_id", c.getString(c.getColumnIndex("section_id")));
				map.put("new_id", c.getString(c.getColumnIndex("new_id")));
				map.put("photo_name", c.getString(c.getColumnIndex("photo_name")));
				map.put("local_img_path", c.getString(c.getColumnIndex("local_img_path")));
				map.put("remote_img_path", c.getString(c.getColumnIndex("remote_img_path")));
				map.put("remote_img_id", c.getString(c.getColumnIndex("remote_img_id")));
				map.put("state", c.getString(c.getColumnIndex("state")));
				mImgList.add(map);
			}
		} catch (Exception e) {
			Log.e(TAG, "Method:getSectionImageList,error" + e.getMessage());
			mImgList = null;
			return mImgList;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return mImgList;
	}
	
	public boolean deleteImage(String id) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_GPS_PHOTOS WHERE _ID = " + id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	/**
	 * 15 insert
	 */
	public boolean insertProjectPoints(String mCodeId, String mContent) {
		ContentValues values = new ContentValues();
		values.put("project_code_id", mCodeId);
		values.put("content", mContent);
		try {
			if(dbHelper.insert(values,"LOCAL_PROJECT_POINTS")){
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
	 * 15 delete
	 */
	public boolean deleteProjectPoints(String mCodeId) {
		try {
			dbHelper.getWritableDatabase().execSQL("DELETE FROM LOCAL_PROJECT_POINTS WHERE project_code_id = '" + mCodeId + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/**
	 * 15 update
	 */
	public boolean updateProjectPoints(String mCodeId, String content) {
		try {
		    dbHelper.getWritableDatabase().execSQL("update LOCAL_PROJECT_POINTS set content=? where project_code_id = ?", new Object[] { content, mCodeId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closeDBHepler();
		}
		return true;
	}
	
	/**
	 * 15 select
	 * 如果有此编号：返回true
	 * 如果没有此编号：返回false
	 */
	public boolean isExistProjectPoints(String project_code_id) {
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery("SELECT COUNT(1) FROM LOCAL_PROJECT_POINTS WHERE project_code_id ='" + project_code_id + "'" , null);
			if(c.moveToFirst()) {
				if(c.getInt(0) <= 0) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return true;
	}
	/**
	 * 15 获取所有需要上传的点
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getUpdatePointsList() {
		ArrayList<HashMap<String, String>> mPointList = new ArrayList<HashMap<String,String>>();
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM LOCAL_PROJECT_POINTS" , null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("project_code_id", c.getString(c.getColumnIndex("project_code_id")));
				map.put("content", c.getString(c.getColumnIndex("content")));
				mPointList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mPointList = null;
			return null;
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
			closeDBHepler();
		}
		return mPointList;
	}
	
	
	//**************************************LOCAL_GPS_PHOTOS ******************************//
	
	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
	
	
	/** LOCAL_COMMAND 指令表操作
	 * <dl>
	 * 		 +"_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "section_id TEXT,"//标段id
		+ "new_id TEXT,"//工程编号id
		+ "user_id TEXT,"  //
		+ "type INTEGER,"//指令类型(监理0，项目办指令1) 
		+ "content TEXT,"//内容
	    +" saveDate	 Date," //保存的日期
		 +" isStart" //流程是否启动   0 未启动  1表示已经启动
		+ "state INTEGER)";//0:未上传，1：已上传，2：上传失败
	 * </dl>
	 *   */
	
	//TODO
	/** 查询指令信息
	 * @param newId 工程编号Id
	 * @param commandType 指令类型 
	 * */
	public ArrayList<HashMap<String, String>>  queryCommandInfo(String newId,String commandType){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		 Cursor c=null;
		 try{
		     c=db.query("LOCAL_COMMAND", null, "new_id=? and type=? ", new String[]{newId,commandType}, null, null, null);
		     while(c.moveToNext()){
		    	 HashMap<String,String> map=new HashMap<String, String>();
		    	 map.put("commandId", c.getString(c.getColumnIndex("_ID"))); //指令编号
		    	 map.put("sectionId", c.getString(c.getColumnIndex("section_id"))); //标段ID
		    	 map.put("uuid", c.getString(c.getColumnIndex("UUID")));
		    	 map.put("newId", c.getString(c.getColumnIndex("new_id")));//工程编号Id
		    	 map.put("userId", c.getString(c.getColumnIndex("user_id")));
		    	 map.put("content", c.getString(c.getColumnIndex("content")));//指令内容
		    	 map.put("saveDate", c.getString(c.getColumnIndex("saveDate"))); //保存时间
		    	 map.put("isStart", c.getString(c.getColumnIndex("isStart"))); //流程是否启动
		    	 map.put("state", c.getString(c.getColumnIndex("state")));//上传状态
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
	 * 查询监理指令表中的UUID 
	 * 
	 */
	public String   queryCommandIdUUID(String commandId){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND", new String[]{"UUID"}, "_ID = ?",new String[]{commandId}, null, null, null);
			if(c.moveToNext()){
				return c.getString(c.getColumnIndex("UUID"));
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
	 * 根据指令Id查询指令信息
	 * @param commandId 指令ID
	 * @return  ArrayList<HashMap<String,String>>
	 */
	public ArrayList<HashMap<String, String>>  queryCommandInfo(String commandId){
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String,String>>();
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND", null, "_ID=? ", new String[]{commandId}, null, null, null);
			while(c.moveToNext()){
				HashMap<String,String> map=new HashMap<String, String>();
				map.put("commandId", c.getString(c.getColumnIndex("_ID"))); //指令编号
				map.put("uuid", c.getString(c.getColumnIndex("UUID")));
				map.put("sectionId", c.getString(c.getColumnIndex("section_id"))); //标段ID
				map.put("newId", c.getString(c.getColumnIndex("new_id")));//工程编号Id
				map.put("userId", c.getString(c.getColumnIndex("user_id")));
				map.put("type", c.getString(c.getColumnIndex("type")));
				map.put("content", c.getString(c.getColumnIndex("content")));//指令内容
				map.put("saveDate", c.getString(c.getColumnIndex("saveDate"))); //保存时间
				map.put("isStart", c.getString(c.getColumnIndex("isStart"))); //流程是否启动
				map.put("state", c.getString(c.getColumnIndex("state")));//上传状态
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
	
	/** 查询LOCAL_COMMAND 最后一条数据的id*/
	public String  queryLastCommandId(){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		c=db.query("LOCAL_COMMAND", null, null, null, null, null,null);
		try{
			if(c.moveToLast()){
				return  c.getString(c.getColumnIndex("_ID"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null)
				c.close();
			if(db!=null)
				db.close();
		}
		return null;
	}
	
	/** 插入指令内容
	 * @param uuid   
	 * @param sectionId 标段Id
	 * @param newId     工程编号Id
	 * @param type         指令类型指令类型(监理，项目办)
	 * @param content    指令内容
	 * @param saveDate  保存时间
	 * @param  isStart      流程是否启动 
	 * @param state         上传状态 0未上传  1 已上传 2 上传失败
	 *  */
	public boolean  insertCommandInfo(String uuid,String sectionId,String newId,String userId,String type,String content,String saveDate){
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		ContentValues  values=new ContentValues();
		values.put("uuid", uuid);
		values.put("section_id", sectionId);
		values.put("new_id", newId);
		values.put("user_id", userId);
		values.put("type", type);
		values.put("content", content);
		values.put("saveDate", saveDate);
		try{
			long  result=db.insert("LOCAL_COMMAND", "", values);			
			if(result!=-1){
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
	/** 修改指令内容
	 * @param uuid   
	 * @param sectionId 标段Id
	 * @param newId     工程编号Id
	 * @param type         指令类型指令类型(监理，项目办)
	 * @param content    指令内容
	 * @param saveDate  保存时间
	 * @param  isStart      流程是否启动 
	 * @param state         上传状态 0未上传  1 已上传 2 上传失败
	 *  */
	public boolean  updateCommandInfo(String commanId,String sectionId,String newId,String userId,String type,String content,String saveDate){
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		ContentValues  values=new ContentValues();
		values.put("section_id", sectionId);
		values.put("new_id", newId);
		values.put("user_id", userId);
		values.put("type", type);
		values.put("content", content);
		values.put("saveDate", saveDate);
		try{
			long  result=db.update("LOCAL_COMMAND", values, "_ID=?", new String[]{commanId});
			if(result!=-1){
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
	
	/**删除指令信息 */
	public boolean deleteCommand(String commandId){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		try{			
			int result =db.delete("LOCAL_COMMAND", "_ID = ?", new String[]{commandId});
			if(result==0){
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
	
	/** 查询指令流程是否上传状态
	 * @param return ture  已经上传  false 未为上传
	 */
	public boolean  queryCommandUploadState(String commandId){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND", new String[]{"isStart"}, "_ID=? AND state=?", new String[]{commandId,"1"}, null, null, null);
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
		return  false;
	}
	
	/** 查询指令流程是否启动状态
	 * @param return ture  已经启动  false 未启动
	 */
	public boolean  queryCommandIsStart(String commandId){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
		c=db.query("LOCAL_COMMAND", new String[]{"isStart"}, "_ID=? AND isStart=?", new String[]{commandId,"1"}, null, null, null);
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
		return  false;
	}
  
	/**查询指令流程状态 是否启动
	 * @param commandId  指令Id
	 * <ul>是 return true
	 * <ul> otherwise  return false
	 */
	public boolean  queryCommandProcedure(String commandId){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND", new String[]{"isStart"}, "_ID= ? AND isStart=?", new String[]{commandId,"1"}, null, null, null);
			if(c.moveToFirst())
					return true;
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
	
	/** 根据UUID查询指令Id
	 * @param UUID
	 * @return 如果有数据存在返回指令ID otherwise  null;
	 */
	public String queryCommandId(String uuid){
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor  c=null;
		try{
			c=db.query("LOCAL_COMMAND", new String[]{"_ID"}, "UUID=?", new String[]{uuid}, null, null, null);
			if(c.moveToFirst()){
				return c.getString(c.getColumnIndex("_ID"));
			}else{
				return null;
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
	 * 修改监理指令的流程启动状态isStart 0未启动  1 已启动
	 * @param commandId
	 */
	public  void  updateCommandProcedureState(String commandId){
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		 try{
			 ContentValues values=new ContentValues();
			 values.put("isStart", "1");
			 db.update("LOCAL_COMMAND", values, "_ID=?", new String[]{commandId});
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 if(db!=null){
				 db.close();
			 }
		 }
	}
	
	/**
	 * 修改指令的上传状态
	 * @param commandId 指令Id
	 * @param uploadState 0 未上传 1 已经上传
	 */
	public void  updateCommandUploadState(String commandId,int uploadState){
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("state", String.valueOf(Integer.toString(uploadState)));
			db.update("LOCAL_COMMAND", values, "_ID=?",new String[]{commandId});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
	}
	
	/**    监理指令文件表  LOCAL_COMMAND_FILES  
	 * <dl>
	 * 	   +	"_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "command_id TEXT,"
			+ "file_type INTEGER,"//0:附件，1：图片
			+ "file_name TEXT,"
			+ "local_file_path TEXT,"
			+ "state INTEGER DEFAULT 0" +//0:未上传，1：已上传，2：上传失败
	 *  </dl>
	 *  */
	
	
	/**
	 * 插入指令文件表
	 * @param commandId  指令Id
	 * @param fileType    	   文件类型 0:附件，1：图片
	 * @param fileName      文件名称
	 * @param filePath  	  文件路径
	 * @param state            上传状态 0:未上传，1：已上传，2：上传失败
	 */
	public void  insertCommandFile(String commandId,String fileType,String fileName,String filePath,String state){
		SQLiteDatabase  db=dbHelper.getWritableDatabase();
		try{
			ContentValues values=new ContentValues();
			values.put("command_id", commandId);  
			values.put("file_type", fileType); 
			values.put("file_name", fileName);  
			values.put("local_file_path", filePath);
			values.put("state", state);             
			db.insert("LOCAL_COMMAND_FILES", "", values);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
	}
	
	
	/**
	 * 查询文件表中的信息
	 * @param commandId 指令Id
	 * @param fileType        文件类型  0 附件 1图片
	 */
	public  LinkedList<HashMap<String, String>> queryCommandFile(String commandId,String fileType){
		LinkedList<HashMap<String, String>> linkedList=new LinkedList<HashMap<String,String>>();
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND_FILES", null, "command_id=? AND file_type=?", new String[]{commandId,fileType}, null, null, null);
			while(c.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("fileId",c.getString(c.getColumnIndex("_ID"))); //指令ID
			map.put("commandId", commandId);
			map.put("type", "false");
			map.put("fileName", c.getString(c.getColumnIndex("file_name")));//文件名称
			map.put("filePath", c.getString(c.getColumnIndex("local_file_path")));//文件路径
			linkedList.addFirst(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(c!=null)
				c.close();
			if(db!=null){
				db.close();
			}
		}
		return linkedList;
	}
	/**
	 * 根据文件名称 查询文件表文件是否存在
	 * @param fileName   	  文件名称
	 * @param fileType        文件类型  0 附件 1图片
	 * @return  true 存在  false不存在
	 */
	public HashMap<String, String> queryCommandFileExist(String fileName,String fileType){
		HashMap<String, String> map=new HashMap<String, String>();
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor c=null;
		try{
			c=db.query("LOCAL_COMMAND_FILES", new String[]{"local_file_path"}, "file_name=? AND local_file_path=?", new String[]{fileName,fileType}, null, null, null);
			if(c.moveToFirst()){
				map.put("fileName", fileName);
				map.put("filePath",c.getString(c.getColumnIndex("local_file_path")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 监理指令文件删除
	 * @param fileId 文件ID
	 * @param fileType
	 */
	public  boolean deleteCommandFile(String fileId,String fileType){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		try{
			long  result=db.delete("LOCAL_COMMAND_FILES", "_ID=? AND file_type=? ", new String[]{fileId,fileType});
			if(result!=0){
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
	/**
	 * 监理指令文件删除
	 * @param fileId 文件ID
	 * @param fileType
	 */
	public  boolean deleteCommandFile(String commandId){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		try{
			long  result=db.delete("LOCAL_COMMAND_FILES", "command_id=?", new String[]{commandId});
			if(result!=0){
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
	
	/** 修改文件的上传状态 
	 * @param commandId 文件类型
	 * @param fileType 文件类型 0 附件 ,1图片
	 * @param state 0未上传, 1已经上传
	 */
	public void updateCommandFileUpload(String commandId,String fileType,String state){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		try{	
			ContentValues values=new ContentValues();
			values.put("state", state);
			db.update("LOCAL_COMMAND_FILES", values, "command_Id=? AND  file_type=?", new String[]{commandId,fileType});
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.close();
			}
		}
		}

}

