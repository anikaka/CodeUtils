package com.tongyan.common.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 标段具体信息数据操作
 * @className DBSectionService
 * @author wanghb
 * @date 2014-7-22 AM 10:09:48
 */
public class DBSectionService {
	
	private DBSectionHelper mDBHelper;
	
	public DBSectionService() {
		mDBHelper = DBSectionHelper.getInstance();
	}
	
	public boolean insertSectionBaseInfo(List<HashMap<String, String>> list, String mSectionId) {
		synchronized(mDBHelper){
				for (HashMap<String, String> map : list) {
					if(map != null) {
						ContentValues values = new ContentValues();
						values.put("SectionId", mSectionId);
						values.put("RowId", map.get("rowId"));
						values.put("Allcode", map.get("allcode"));
						values.put("AllName", map.get("allName"));
						values.put("ParentId", map.get("parentId"));
						try {
							mDBHelper.insert(values, "SectionBaseInfo");
							values = null;
							map = null;
						} catch (Exception e) {
							e.printStackTrace();
							mDBHelper.insert(values, "SectionBaseInfo");
						}
					}
				}
			return true;
		}
	}
	/**
	 * 
	 * @param mSection
	 * @return
	 */
	public boolean deleteSectionBaseInfo(String mSectionId) {
		synchronized(mDBHelper){
			try {
				mDBHelper.getWritableDatabase().execSQL("DELETE FROM SectionBaseInfo WHERE SectionId = '" + mSectionId + "' COLLATE NOCASE");
			} catch (Exception e) {
				e.printStackTrace();
			return false;
			}
			return true;
		}
	}
	
	public boolean delAll(String tabName) {
		synchronized(mDBHelper){
			try {
				mDBHelper.del(tabName);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 工程编号
	 * @param SectionId
	 * @param parentId
	 * @return 2014-10-11 
	 */
	public boolean countSectionExist(String SectionId, String parentId) {
		String sql = String.format("SELECT COUNT(1) FROM SectionBaseInfo WHERE SectionId='%s' COLLATE NOCASE AND ParentId='%s' COLLATE NOCASE ORDER BY Allcode", SectionId, parentId);
		Cursor c = null;
		try {
			c = mDBHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				if(c.getInt(0) <= 0) { 
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param SectionId
	 * @param parentId
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getSectionList(String SectionId, String parentId, boolean isShowCollectBtn) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = String.format("SELECT * FROM SectionBaseInfo WHERE SectionId='%s' COLLATE NOCASE AND ParentId='%s' COLLATE NOCASE ORDER BY Allcode", SectionId, parentId);
		Cursor c = null;
		try {
			c = mDBHelper.getReadableDatabase().rawQuery(sql, null);
			while(c.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("sid", SectionId);
				map.put("RowId", c.getString(c.getColumnIndex("RowId")));
				String mAllCode = c.getString(c.getColumnIndex("Allcode"));
				String mAllName = c.getString(c.getColumnIndex("AllName"));
				map.put("Allcode", mAllCode);
				map.put("AllName", mAllName);
				map.put("sContent", mAllCode + " " + mAllName);
				map.put("ParentId", c.getString(c.getColumnIndex("ParentId")));
				map.put("isShowCollectBtn", String.valueOf(isShowCollectBtn));//判断是否要显示采点的按钮
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
			return null;
		}
	}
	
	
	public static int mCacheNum = 0;;
	
	/**
	 * 
	 * @return
	 */
	public void computeSection(HashMap<String, String> map) {
		String sql = String.format("SELECT * FROM SectionBaseInfo WHERE SectionId='%s' COLLATE NOCASE AND RowId='%s' COLLATE NOCASE", map.get("SectionId"), map.get("RowId"));
		Cursor c = null;
		try {
			c = mDBHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				String sectionId = c.getString(c.getColumnIndex("SectionId"));
				//String rowId = c.getString(c.getColumnIndex("RowId"));
				String ParentId = c.getString(c.getColumnIndex("ParentId"));
				map.put("SectionId", sectionId);
				map.put("RowId", ParentId);
				if(sectionId != null && sectionId.equals(ParentId)) {
					mCacheNum ++;
				} else {
					mCacheNum ++;
					computeSection(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public int getSectionIsParent(String parentId,String sectionId) {
		String sql = String.format("SELECT COUNT(1) FROM SectionBaseInfo WHERE ParentId='%s' COLLATE NOCASE AND SectionId='%s' COLLATE NOCASE", parentId, sectionId);
		Cursor c = null;
		try {
			c = mDBHelper.getReadableDatabase().rawQuery(sql, null);
			if(c.moveToFirst()) {
				if(c.getInt(0) == 0) {
					return 0;
				} else {
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
	
	
}
