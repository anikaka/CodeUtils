package com.tongyan.zhengzhou.common.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CheckManagementDBService {

	private final static String TAG = "CheckManagementDBService";
	private DBHelp dbHelper;

	public CheckManagementDBService() {
		//this.dbHelper = new DBHelper(context);
		dbHelper = new DBHelp();
	}
	
	public void closeDB() {
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

	public DBHelp getDbHelper() {
		return dbHelper;
	}
	
	/**
	 * 获取线路信息
	 * @return ArrayList<HashMap<String, Object>>
	 * MetroLineId（int）:地铁线ID；PMetroLineId（int）：地铁线父ID;ObjectName（String）:地铁线名称;
	 * ObjectLevel(int):对象级别；IsCheck（boolean）：是否选择了该对象
	 * */
	public ArrayList<HashMap<String, Object>> getAllLines(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<HashMap<String, Object>> arrayList=new ArrayList<HashMap<String,Object>>();
		String sql="SELECT MetroLineId ,PMetroLineId ,MetroLineName FROM MetroLine where PMetroLineId=-1 OR PMetroLineId=0";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				
				HashMap<String, Object> map=new HashMap<String, Object>();
				
				int metroLineId = cursor.getInt(cursor.getColumnIndex("MetroLineId"));
				map.put("MetroLineId", metroLineId);
				
				int PMetroLineId = cursor.getInt(cursor.getColumnIndex("PMetroLineId"));
				map.put("PMetroLineId", PMetroLineId);
				
				String metroLineName = " "+cursor.getString(cursor.getColumnIndex("MetroLineName"));
				map.put("ObjectName", metroLineName);
				
				map.put("ObjectLevel", 1);	//1：线路级别	例如：上海地铁7号线
				
				map.put("IsSelect", false);
				
				map.put("IsClick", false);
				
				arrayList.add(map);
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	/**
	 * 从对象列表中移除对象的子对象
	 * @param baseObjectList 对象列表
	 * @param baseObject 基本对象
	 * */
	public void removeObject(ArrayList<HashMap<String, Object>> baseObjectList, HashMap<String, Object> baseObject){
		
		int objectLevel = (Integer) baseObject.get("ObjectLevel")+1;
		
		int index = baseObjectList.indexOf(baseObject);
		if(index==-1){	//该对象不存在
			return;
		}
		for(int i=index+1; i<baseObjectList.size(); ){
			int level = (Integer) baseObjectList.get(i).get("ObjectLevel");
			if(level >= objectLevel){
				baseObjectList.remove(i);
			}else{
				return;
			}
		}
	}
	
	/**
	 * 获取线路的子信息
	 * @param baseObject 基本对象
	 * @return ArrayList<HashMap<String, Object>>
	 * MetroLineId（int）:地铁线ID；PMetroLineId（int）：地铁线父ID;ObjectName（String）:地铁线名称;
	 * ObjectLevel(int):对象级别；IsCheck（boolean）：是否选择了该对象;IsClick（boolean）：是否点击了该对象
	 * */
	public ArrayList<HashMap<String, Object>> getLineInfos(HashMap<String, Object> baseObject){
		
		int pMetroLineId = (Integer) baseObject.get("MetroLineId");
		int objectLevel = (Integer) baseObject.get("ObjectLevel")+1;
		boolean isSelect = (Boolean) baseObject.get("IsSelect");
		
		String startStationCode = "";
		String endStationCode = "";
		if(objectLevel==5){
			startStationCode = (String) baseObject.get("StartStationCode");
			endStationCode = (String) baseObject.get("EndStationCode");
		}
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<HashMap<String, Object>> arrayList=new ArrayList<HashMap<String,Object>>();
		String sql = null;
		if(objectLevel<=3){		//2-3级数据	 2：主线、支线级 	例如：7号线主线、北延伸      3：期级工程  例如：1期工程
			sql="SELECT MetroLineId ,PMetroLineId ,MetroLineName FROM MetroLine where PMetroLineId="+pMetroLineId+"";
		}else if(objectLevel==4){	//4:区间级别
			sql="SELECT MetroLineId , CheckObjectCode, CheckObjectName, StartStationCode, EndStationCode FROM " +
					"MetroCheckObject where TermPartId="+pMetroLineId+" AND CheckObjectType=1";
		}else if(objectLevel==5){	//5：车站级别
			sql="SELECT MetroLineId , CheckObjectCode, CheckObjectName FROM MetroCheckObject where " + 
					"CheckObjectCode='"+startStationCode+"' OR CheckObjectCode='"+endStationCode+"'";
			return null;	//不显示车站级别
		}
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				
				HashMap<String, Object> map=new HashMap<String, Object>();
				
				int metroLineId = cursor.getInt(cursor.getColumnIndex("MetroLineId"));
				map.put("MetroLineId", metroLineId);
				
				if(objectLevel<=3){
					int PMetroLineId = cursor.getInt(cursor.getColumnIndex("PMetroLineId"));
					map.put("PMetroLineId", PMetroLineId);
					
					String metroLineName = ""+cursor.getString(cursor.getColumnIndex("MetroLineName"));
					map.put("ObjectName", metroLineName);
				}else if(objectLevel==4){
					String metroLineName = ""+cursor.getString(cursor.getColumnIndex("CheckObjectName"));
					map.put("ObjectName", metroLineName);
					map.put("StartStationCode", cursor.getString(cursor.getColumnIndex("StartStationCode")));
					map.put("EndStationCode", cursor.getString(cursor.getColumnIndex("EndStationCode")));
					map.put("CheckObjectCode", cursor.getString(cursor.getColumnIndex("CheckObjectCode")));
				}else if(objectLevel==5){
					String metroLineName = ""+cursor.getString(cursor.getColumnIndex("CheckObjectName"));
					map.put("ObjectName", metroLineName);
				}
				
				map.put("ObjectLevel", objectLevel);
				
				map.put("IsSelect", isSelect);
				
				map.put("IsClick", false);
				
				arrayList.add(map);
			}
			
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	
	/**
	 * 合并ArrayList;把list放到baseObjectList中指定的位置
	 * @param baseObjectList 
	 * @param position 要插入的位置
	 * @param list
	 * MetroLineId（int）:地铁线ID；PMetroLineId（int）：地铁线父ID;ObjectName（String）:地铁线名称;
	 * ObjectLevel(int):对象级别；IsCheck（boolean）：是否选择了该对象
	 * */
	public void combineList(ArrayList<HashMap<String, Object>> baseObjectList, int position, 
								ArrayList<HashMap<String, Object>> list){
		
		if(baseObjectList==null || list==null){
			return;
		}
		
		ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String,Object>>();
		for(int i=0; i<=position; i++){
			HashMap<String, Object> map = baseObjectList.get(i);
			list1.add(map);
		}
		
		ArrayList<HashMap<String, Object>> list2 = new ArrayList<HashMap<String,Object>>();
		for(int i=position+1; i<baseObjectList.size(); i++){
			HashMap<String, Object> map = baseObjectList.get(i);
			list2.add(map);
		}
		
		baseObjectList.clear();
		baseObjectList.addAll(list1);
		baseObjectList.addAll(list);
		baseObjectList.addAll(list2);
	}
	
	/**
	 * 获取地铁线路所有数据
	 * @param baseObjectList 
	 * */
	public void getCheckObjectAllData(ArrayList<HashMap<String, Object>> baseObjectList){
		
		if(baseObjectList==null){
			baseObjectList = new ArrayList<HashMap<String,Object>>();
		}
		
		baseObjectList = new CheckManagementDBService().getAllLines();
		int index=0;
		for(HashMap<String, Object> baseObject:baseObjectList){
			ArrayList<HashMap<String, Object>> list = new CheckManagementDBService().getLineInfos(baseObject);
			if(list != null){
				new CheckManagementDBService().combineList(baseObjectList, index, list);
			}
			index++;
		}
	}
	
	/**
	 * 获取已经选择的基本对象（区间对象）
	 * @param baseObjectList 
	 * @return ArrayList<HashMap<String, String>>
	 * 区间对象
	 * */
	public ArrayList<HashMap<String, String>> getSelectedBaseObject(ArrayList<HashMap<String, Object>> baseObjectList){
		
		if(baseObjectList==null){
			return null;
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, Object> baseObject: baseObjectList){
			boolean select = (Boolean) baseObject.get("IsSelect");
			int objectLevel = (Integer) baseObject.get("ObjectLevel");
			if(select && objectLevel==4){
				HashMap<String, String> map = new HashMap<String, String>();
				String checkObjectCode = (String) baseObject.get("CheckObjectCode");
				map.put("CheckObjectCode", checkObjectCode);
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 获取已经选择的设施设备对象）
	 * @param facilityObjectList 
	 * @return ArrayList<String>
	 * */
	public ArrayList<HashMap<String, String>> getSelectedfacilityObject(ArrayList<HashMap<String, Object>> facilityObjectList){
		
		if(facilityObjectList==null){
			return null;
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, Object> baseObject: facilityObjectList){
			boolean select = (Boolean) baseObject.get("IsSelect");
			int objectLevel = (Integer) baseObject.get("ObjectLevel");
			if(select && objectLevel==2){
				String checkObjectCode = (String) baseObject.get("ParamValue");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("FacilityCode", checkObjectCode);
				list.add(map);
			}
		}
		
		return list;
	}
	
	
	/**
	 * 获取所有设施
	 * @return ArrayList<HashMap<String, Object>>
	 * PDicId（int）:父ID；ParamName（int）：设施名称;ParamValue（String）:设施的值;ObjectLevel：设施级别；IsSelect（boolean）：是否选择了该对象
	 * */
	public ArrayList<HashMap<String, Object>> getAllFacilitys(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<HashMap<String, Object>> arrayList=new ArrayList<HashMap<String,Object>>();
		
		String sql="SELECT PDicId ,ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='检查对象类型' Order by ParamValue";
		Cursor cursor=null;
		Cursor cursor2=null;
		Cursor cursor3=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("PDicId", cursor.getInt(cursor.getColumnIndex("PDicId")));
				map.put("ParamName", cursor.getString(cursor.getColumnIndex("ParamName")));
				map.put("ParamValue", cursor.getString(cursor.getColumnIndex("ParamValue")));
				map.put("ObjectLevel", 1);
				map.put("IsSelect", false);
				arrayList.add(map);
				
				String sql2="SELECT ParamValue FROM z_ConstructionCheckDic where ParamType='检查对象段类型'" +
							" AND PDicId='"+cursor.getString(cursor.getColumnIndex("ParamValue"))+"'";
				cursor2=db.rawQuery(sql2, null);
				while(cursor2.moveToNext()){
					String paramValue = cursor2.getString(cursor2.getColumnIndex("ParamValue"));
					String sql3="SELECT PDicId ,ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='设施类型'" +
								" AND PDicId='"+paramValue+"'";
					cursor3=db.rawQuery(sql3, null);
					while (cursor3.moveToNext()) {
						HashMap<String, Object> map0=new HashMap<String, Object>();
						map0.put("PDicId", cursor3.getInt(cursor3.getColumnIndex("PDicId")));
						map0.put("ParamName", cursor3.getString(cursor3.getColumnIndex("ParamName")));
						map0.put("ParamValue", cursor3.getString(cursor3.getColumnIndex("ParamValue")));
						map0.put("ObjectLevel", 2);
						map0.put("IsSelect", false);
						arrayList.add(map0);
					}
				}
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			if(cursor2!=null){
				cursor2.close();
			}
			if(cursor3!=null){
				cursor3.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	
	/**
	 * 获取检查方式
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getAllCheckWay(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<String> arrayList=new ArrayList<String>();
		arrayList.add("默认");
		String sql="SELECT ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='检查方式'";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				arrayList.add(cursor.getString(cursor.getColumnIndex("ParamName")));
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	/**
	 * 获取检查类型
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getAllCheckType(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<String> arrayList=new ArrayList<String>();
		arrayList.add("全选");
		String sql="SELECT ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='检查任务类型'";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				arrayList.add(cursor.getString(cursor.getColumnIndex("ParamName")));
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	/**
	 * 获取病害类型
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getAllDamageType(){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<String> arrayList=new ArrayList<String>();
		arrayList.add("全选");
		String sql="SELECT ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='病害统计类型'";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				arrayList.add(cursor.getString(cursor.getColumnIndex("ParamName")));
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	/**
	 * 获取已选择的病害类型编号
	 * @return ArrayList<HashMap<String, String>>
	 * */
	public ArrayList<HashMap<String, String>> getSelectedDamageType(String damageTypeName){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String, String>>();
		if(!"全选".equals(damageTypeName)){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("DamageTypeCode", String.valueOf(getParamValue(db, "病害统计类型", damageTypeName)));
			arrayList.add(map);
			return arrayList;
		}
		String sql="SELECT ParamName ,ParamValue FROM z_ConstructionCheckDic where ParamType='病害统计类型'";
		Cursor cursor=null;
		try{
			cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("DamageTypeCode", cursor.getString(cursor.getColumnIndex("ParamValue")));
				arrayList.add(map);
			}
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			closeDB();
		 }
		 return arrayList;
	}
	
	/**
	 * 获取已选择的检查方式
	 * @return String
	 * 默认值为表单的值
	 * */
	public String getSelectedCheckWay(String checkWayName){
		
		String checkWay;
		if("默认".equals(checkWayName) || "表单".equals(checkWayName)){
			checkWay = String.valueOf(getParamValue("检查方式", "表单"));
		}else{
			checkWay = String.valueOf(getParamValue("检查方式", checkWayName));
		}
		return checkWay;
	}
	
	/**
	 * 获取已选择的检查类型
	 * @return String
	 * 全选：-1
	 * */
	public String getSelectedCheckType(String checkTypeName){
		
		String checkWay;
		if("全选".equals(checkTypeName)){
			checkWay = "-1";
		}else{
			checkWay = String.valueOf(getParamValue("检查任务类型", checkTypeName));
		}
		return checkWay;
	}
	
	
	/**
	 * 通过字典表查询数据
	 * @param paramType 参数类型（如：检查方向）
	 * @param paramName 参数名字（如：里程增加方向）
	 * @return int 如果查询失败，返回-1；否则，返回查询的值
	 * */
	public int getParamValue(SQLiteDatabase db, String paramType, String paramName) {
		
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "SELECT * FROM  z_ConstructionCheckDic WHERE ParamType = '"+paramType+"' AND ParamName = '"+paramName+"'";
		Cursor cursor = null;
		int paramValue = -1;
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				paramValue = cursor.getInt(cursor.getColumnIndex("ParamValue"));
			}
			return paramValue;
		}catch(Exception e){
			e.getStackTrace();
			return -1;
		}finally{
			if(cursor != null)
				cursor.close();
			//db.close();
		}
	}
	
	/**
	 * 通过字典表查询数据
	 * @param paramType 参数类型（如：检查方向）
	 * @param paramName 参数名字（如：里程增加方向）
	 * @return int 如果查询失败，返回-1；否则，返回查询的值
	 * */
	public int getParamValue(String paramType, String paramName) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "SELECT * FROM  z_ConstructionCheckDic WHERE ParamType = '"+paramType+"' AND ParamName = '"+paramName+"'";
		Cursor cursor = null;
		int paramValue = -1;
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				paramValue = cursor.getInt(cursor.getColumnIndex("ParamValue"));
			}
			return paramValue;
		}catch(Exception e){
			e.getStackTrace();
			return -1;
		}finally{
			if(cursor != null)
				cursor.close();
			db.close();
		}
	}
}
