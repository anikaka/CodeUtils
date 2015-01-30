package com.TY.bhgis.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILineString;
import com.TY.bhgis.Geometry.ILinearRing;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.IPolygon;
import com.TY.bhgis.Geometry.LineString;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Geometry.Polygon;
import com.TY.bhgis.Util.DBHelper;
import com.TY.bhgis.Util.Image;
import com.TY.bhgis.Util.Type;
import com.TY.bhgis.Util.mapUtil;
import com.tygeo.highwaytunnel.common.StaticContent;

public class DataProvider {

	private static String sourcePath = StaticContent.DataBasePath;

	// public static String SUBWAYID = "1";// 线路
	// public static String QIDUAN = "1";// 期段
	// public static String SECTIONID = "1";// 区间
	// public static String SECTIONMID = "1";// 母区间
	// public static String SECTION_ID = "1";// 区间段
    public static int DIRECTION = 0;// 走向
	public static String z_y = "Z";
	public static String PROJECTID = "1";// 任务编号
	public static String jcdirection = "进口";
	// public static String JLZ = "0";// 进入站 0为"前"1为"后"

	public static DBHelper dbHelper = new DBHelper(sourcePath);

	// 按照线路区间走向类别查询病害信息
	public static Vector<IBH> BHSelect(int type) {
		Vector<IBH> bhs = new Vector<IBH>();
		String[] fields = getFields(type);
//		String sql = "select BHID from CILIV_CHECKCONTENT where BHTYPE=" + type
//				+ " and ISCQ=1  and  task_id='" + PROJECTID + "' and UP_DOWN="+DIRECTION;
		String sql = "select BHID,level_content from CILIV_CHECKCONTENT where BHTYPE=" + type
				+ " and ISCQ=1  and  task_id='" + PROJECTID + "' and UP_DOWN="+DIRECTION;
		Cursor cursor = dbHelper.query(sql);
//		if("".equals(cursor.getString(1)) || "无".equals(cursor.getString(1))){
//			return bhs ;
//		}
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				String bhid = cursor.getString(0);
				IGeometry geometry = getGeometry(bhid, type, dbHelper);
				IBH bh = new BH(geometry, bhid, fields, getValues(fields, bhid,dbHelper), type);
				bhs.add(bh);
				cursor.moveToNext();
			}
		}else{
			 sql = "select BHID from CILIV_CHECKCONTENT where BHTYPE=" + getType(type)
					+ " and ISCQ=1  and  task_id='" + PROJECTID + "' and UP_DOWN="+DIRECTION;
			 cursor = dbHelper.query(sql);
			 if (cursor.moveToFirst()) {
					for (int i = 0; i < cursor.getCount(); i++) {
						String bhid = cursor.getString(0);
						IGeometry geometry = getGeometry(bhid, type, dbHelper);
						IBH bh = new BH(geometry, bhid, fields, getValues(fields, bhid,dbHelper), type);
						bhs.add(bh);
						cursor.moveToNext();
					}
			 }
		}
		cursor.close();
		dbHelper.close();
		return bhs;
	}

	/** 因为定期type是那1到10进行查找的,如果是定期的话,BHTYPE 是从55开始 ,
	 * 所以必须转换,
	 * 具体转换值在表CIVIL_CHECK_INFO中
	 * 字段cqbhtype和CHECKID 一一对应
	 * @author ChenLang
	 */
	public static int getType(int type){
		if(type==1){
			return 55;
		}else if(type==2){
			return 56;
		}else if(type==3){
			return 57;
		}else if(type==4){
			return 58;
		}else if(type==5){
			return 62;
		}else if(type==6){
			return 63;
		}else if(type==7){
			return 64;
		}else if(type==8){
			return 65;
		}else if(type==9){
			return 66;
		}else if(type==10){
			return 67;
		}
		return 1;
	}

	// 删除病害信息
	public static void BHDelete(String BHID) {
		String sql = "delete from CILIV_CHECKCONTENT where BHID='" + BHID + "'";
		dbHelper.execSql(sql);
		sql = "delete from BHZB where BHID='" + BHID + "'";
		dbHelper.execSql(sql);
	}

	// 添加和修改病害信息
	public static void BHStore(String BHID, String[] fields, String[] values,
			IGeometry geometry) {
		String sql = "";
		if (isExistBHID(BHID)) {
			sql = "UPDATE CILIV_CHECKCONTENT SET ";
			for (int i = 0; i < fields.length; i++) {
				sql += fields[i] + "='" + values[i] + "'";
				if (i < fields.length - 1) {
					sql += ",";
				}
			} 
			sql += " where BHID='" + BHID + "'";
			dbHelper.execSql(sql);

			sql = "delete from BHZB where bhid='" + BHID + "'";
			dbHelper.execSql(sql);
		} else {
			sql = "insert into CILIV_CHECKCONTENT(BHID,";
			for (int i = 0; i < fields.length; i++) {
				sql += fields[i];
				if (i < fields.length - 1) {
					sql += ",";
				}
			}
			sql += ") ";
			sql += "values('" + BHID + "',";
			for (int i = 0; i < values.length; i++) {
				sql += "'" + values[i] + "'";
				if (i < values.length - 1) {
					sql += ",";
				}
			}
			sql += ")";
			dbHelper.execSql(sql);
			
			sql = "delete from BHZB where bhid='" + BHID + "'";
			dbHelper.execSql(sql);
		}
		if (geometry == null)
			return;
		switch (geometry.getGeometryType()) {
		case 1:// 点

			IPoint point = (IPoint) geometry;
			sql = "insert into BHZB(BHID,X,Y,OX,OY,LCZH) values('"
					+ BHID
					+ "','"
					+ point.getX()
					+ "','"
					+ point.getY()
					+ "','"
					+ mapUtil.changeX(point.getX())
					+ "','"
					+ mapUtil.changeY(point.getY(),
							mapUtil.getLCZHfromY(point.getY())) + "','"
					+ mapUtil.getLCZHfromY(point.getY()) + "')";
			dbHelper.execSql(sql);
			break;
			
		case 3:// 线
			ILineString polyline = (ILineString) geometry;
			for (int i = 0; i < polyline.getNumPoints(); i++) {
				point = polyline.getPoint(i);
				sql = "insert into BHZB(BHID,X,Y,OX,OY,LCZH) values('"
						+ BHID
						+ "','"
						+ point.getX()
						+ "','"
						+ point.getY()
						+ "','"
						+ mapUtil.changeX(point.getX())
						+ "','"
						+ mapUtil.changeY(point.getY(),
								mapUtil.getLCZHfromY(point.getY())) + "','"
						+ mapUtil.getLCZHfromY(point.getY()) + "')";
				dbHelper.execSql(sql);
			}
			break;
			
		case 5:// 面
			ILineString ring = (ILineString) ((IPolygon) geometry).getExteriorRing();
			for (int i = 0; i < ring.getNumPoints(); i++) {
				point = ring.getPoint(i);
				sql = "insert into BHZB(BHID,X,Y,OX,OY,LCZH) values('"
						+ BHID
						+ "','"
						+ point.getX()
						+ "','"
						+ point.getY()
						+ "','"
						+ mapUtil.changeX(point.getX())
						+ "','"
						+ mapUtil.changeY(point.getY(),
								mapUtil.getLCZHfromY(point.getY())) + "','"
						+ mapUtil.getLCZHfromY(point.getY()) + "')";
				dbHelper.execSql(sql);
			}
			break;
		}
	}

	public static String getBHIDforDQ(int Bhid,int checkType){
		String s="";
		if (checkType==0) {
			return String.valueOf(Bhid);
		}else {
		String sql="select CHECKID from CIVIL_CHECK_INFO where cqbhtype='定期检查'";	
		Cursor cursor = dbHelper.query(sql);
		try {
			if (cursor.moveToFirst()) {
				s=cursor.getString(0);
				return s;
			}else{
				return String.valueOf(Bhid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return String.valueOf(Bhid);
		}	
		}
	}
	
		
	
	// 用于病害照片编号
	public static String GetMaxBHImageID() {
		return "p" + getMaxImageID();
	}

	// 用于病害编号
	public static String GetMaxBHID() {
		String sql = "select _id from CILIV_CHECKCONTENT order by _id desc limit 0,1";
		Cursor cursor = dbHelper.query(sql);
		StringBuffer bhid = new StringBuffer("P");
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				int id = cursor.getInt(0);
				bhid.append(id + 1);
				cursor.moveToNext();
			}
		} else {
			bhid.append("1");
		}
		cursor.close();
		dbHelper.close();
		return bhid.toString();
	}

	
	public static String[] getFields(int type) {
		String[] fields = null;
		String sql = "SELECT FIELDVALUE FROM BASICBHTYPEFIELD WHERE BHTYPE="+type;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.getCount() > 0) {
			fields = new String[cursor.getCount()];
			if (cursor.moveToFirst()) {
				for (int i = 0; i < cursor.getCount(); i++) {
					String fieldname = cursor.getString(0);
					fields[i] = fieldname;
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		dbHelper.close();
		return fields;
	}
	
	
	//插入衬砌病害是否项属性
	public static boolean insertCQISNOT(String bhid,String involve){
		String sql="UPDATE CILIV_CHECKCONTENT SET involve='"+involve+"' where BHID='"+bhid+"'";
		try {
			dbHelper.execSql(sql);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	// 获取病害菜单数据
	public static List<java.util.Map<String, Object>> getBHImageMap() {
		List<java.util.Map<String, Object>> list = new ArrayList<java.util.Map<String, Object>>();
		java.util.Map<String, Object> map = null;
		String sql = "select BHTYPE,BHIMAGE FROM BHTYPE order by bhtype";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			for (int j = 0; j < c.getCount(); j++) {
				int bhtype = c.getInt(0);
				byte[] in = c.getBlob(1);
				Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
				map = new HashMap<String, Object>();
				map.put("bhtype", bhtype);
				map.put("image", new Image(bitmap));
				list.add(map);
				c.moveToNext();
			}
		}
		c.close();
		dbHelper.close();
		return list;
	}

	// 根据病害类型获取图形类别
	public static int getGeoType(int bhtype) {
		int geotype = 1;
		String sql = "select GEOTYPE from BHTYPE where BHTYPE=" + bhtype;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			geotype = cursor.getInt(0);
		}
		return geotype;
	}

	// 根据病害类型获取病害名称
	public static String getBHName(int bhtype) {
		String bhname = "";
		String sql = "select NAME from BHTYPE where BHTYPE=" + bhtype;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			bhname = cursor.getString(0);
		}
		return bhname;
	}

	// 根据衬砌病害的类型获取检查ID
	public static int getCheckIDFromBHType(int bhtype) {
		int checkid = -1;
		String sql = "select CHECKID from CIVIL_CHECK_INFO where cqbhtype="+ bhtype;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			checkid = cursor.getInt(0);
		}
		return checkid;
	}
	
	// 根据衬砌病害的类型获取检查ID
	public static int getCheckIDFromBHType(int bhtype,int checkType) {
		int checkid = -1;
		String type="日常检查";
		if (checkType==1) {
			type="定期检查";
		}
		String sql = "select CHECKID from CIVIL_CHECK_INFO where cqbhtype="
				+ bhtype+" and check_type='"+type+"'";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			checkid = cursor.getInt(0);
		}
		return checkid;
	}
	
	
	

	// 获取空间位置描述
	public static String getPositionName(int checkitemid, int positionid) {
		String positionname = "";
		String sql = "select POSITIONNAME from BHPOSITION_DIC where CHECKITEMID="
				+ checkitemid + " and POSITIONID=" + positionid;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			positionname = cursor.getString(0);
		}
		return positionname;
	}
	
	// 获取空间位置描述
		public static String getPositionName2(int checkid, int positionid) {
			String positionname = "";
			String sql = "select a.POSITIONNAME from BHPOSITION_DIC as a,CIVIL_CHECK_INFO as b where a.CHECKITEMID=b.CHECKITEMID and b.CHECKID="
					+ checkid + " and a.POSITIONID=" + positionid;
			Cursor cursor = dbHelper.query(sql);
			if (cursor.moveToFirst()) {
				positionname = cursor.getString(0);
			}
			return positionname;
		}

	public static String getLevelContente(String level, int bhtype) {
		String filedname = "";
		if (level.equals("S")) {
			filedname = "s_level";
		} else if (level.equals("B")) {
			filedname = "b_level";
		} else {
			filedname = "a_level";
		}
		String levelcontent = "";
		String sql = "select " + filedname
				+ " from CIVIL_CHECK_INFO where cqbhtype=" + bhtype;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			levelcontent = cursor.getString(0);
		}
		return levelcontent;
	}
	
	public static String getLevelContent(String level, int bhtype) {
		String filedname = "";
		if (level.equals("S")) {
			filedname = "1";
		} else if (level.equals("B")) {
			filedname = "2";
		} else {
			filedname = "3";
		}
		
		String levelcontent ="";
		String sql = "select CheckItemDesc " +
				"from CheckItemDescInfos where CheckId="+bhtype+" and YhGradeCode="+filedname+"";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			levelcontent = cursor.getString(0);
		}
		return levelcontent;
	}
	
	
	// 定期检查的检查内容对应的属性编号和属性名称
	public static List<java.util.Map<String, Object>> getDQInfos(int checkid) {

		List<java.util.Map<String, Object>> list = new ArrayList<java.util.Map<String, Object>>();
		java.util.Map<String, Object> map = null;
		String sql = "select PARAMVALUE, PARAMNAME from CheckItemDictionaries where CHECKID="
				+ checkid + " order by PARAMVALUE";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			for (int j = 0; j < c.getCount(); j++) {

				int paramid = c.getInt(0);
				String paramname = c.getString(1);

				map = new HashMap<String, Object>();

				map.put("paramid", paramid);
				map.put("paramname", paramname);
				list.add(map);
				c.moveToNext();
			}

		}
		c.close();
		dbHelper.close();
		return list;
	}

	// 从基础参数表中获取参数名称
	public static String getBaseCSName(String mParamType, int mParamValue) {
		String sql = "select PARAMNAME from BaseDictionaries where PARAMTYPE='"
				+ mParamType + "' and PARAMVALUE=" + mParamValue;
		String csname = "";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			csname = cursor.getString(0);
		}
		return csname;
	}

	// 获取单位编号
	public static int getUnitCode(int checkid, int mParamValue) {
		String sql = "select UNITCODE from CheckItemDictionaries where CHECKID="
				+ checkid + " and PARAMVALUE=" + mParamValue;
		int unitcode = -1;
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			unitcode = cursor.getInt(0);
		}
		return unitcode;
		
	}

	// 从土建参数表中获取参数名称
	public static String getTJCSName(int checkid, int mParamValue) {
		String sql = "select PARAMNAME from CheckItemDictionaries where CHECKID="
				+ checkid + " and PARAMVALUE=" + mParamValue;
		String csname = "";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			csname = cursor.getString(0);
		}
		return csname;
		
	}

	// 保存定期检查属性
	public static void storeDQInfo(String bhid, int checkid, int infoid,
			String infovalue) {
		String sql = "insert into CIVIL_REG_CHECK_INFO(BHID,CHECKID,PARAMCODE,PARAMVALUE) values('"
				+ bhid
				+ "',"
				+ checkid
				+ ","
				+ infoid
				+ ",'"
				+ infovalue
				+ "')";
		dbHelper.execSql(sql);
	}

	
	// 查询定期检查属性
	public static List<String> queryDQInfo(String bhid) {
		List<String> infos = new ArrayList<String>();
		String sql = "select a.PARAMVALUE,b.PARAMNAME,c.PARAMNAME   from CIVIL_REG_CHECK_INFO as a,CheckItemDictionaries as b,BaseDictionaries as c "
				+ "where a.PARAMCODE=b.PARAMVALUE and a.CHECKID=b.CHECKID and a.BHID='"
				+ bhid
				+ "' and "
				+ "c.PARAMTYPE='计量单位' and c.PARAMVALUE=a.PARAMCODE order by a.PARAMCODE";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			for (int j = 0; j < c.getCount(); j++) {
				String infovalue = c.getString(0);
				String infoname = c.getString(1);
				String infounitname = c.getString(2);

				infos.add(infoname + ":" + infovalue + infounitname);
				c.moveToNext();
			}
		}
		c.close();
		dbHelper.close();
		return infos;
	}

	/**
	 * 判断在这个任务下、上或下行有没有相同的土建检查内容且相同空间位置的 有 返回true
	 * 
	 * @param taskid
	 * @param up_down
	 * @param checkid
	 * @param positionid
	 *            如果没有空间位置的就传-1
	 * @return 返回值为
	 */
	public static boolean ValidationCheckContentForCILIV(String taskid,	int up_down, int checkid, int positionid) {
		String sql = "select _id from CILIV_CHECKCONTENT where task_id='"
				+ taskid + "' and CHECKID=" + checkid + " and UP_DOWN="
				+ up_down;
		if (positionid != -1) {
			sql += " and POSITIONID=" + positionid;
		}
		Cursor cursor = dbHelper.query(sql);
		if (cursor.getCount() < 1) {
			cursor.close();
			dbHelper.close();
			return false;
		} else {
			cursor.close();
			dbHelper.close();
			return true;
		}
	}

	public static String getCheck_Content(int checkid) {
		String sql = "select check_pro,check_content from CIVIL_CHECK_INFO where CHECKID="
				+ checkid;
		String check_content = "";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.moveToFirst()) {
			check_content = cursor.getString(0) + cursor.getString(1);
		}
		return check_content;

	}
	

	/**********************
	 * 私有方法
	 ***************************/
	// 获取最大照片编号
	private static String getMaxImageID() {

		int mid = getImageID();
		addMaxImageID(mid);
		return String.valueOf(mid);
	}

	// 最大id++
	private static void addMaxImageID(int maxid) {
		String sql = "UPDATE TASK SET picture_beginnum='"
				+ String.valueOf(maxid + 1) + "' WHERE update_id='" + PROJECTID
				+ "'";
		dbHelper.execSql(sql);
		dbHelper.close();
	}

	// 获取病害值
	private static String[] getValues(String[] fields, String bhid,
			DBHelper dbHelper) {
		String[] values = new String[fields.length];

		String sql = "SELECT ";
		for (int i = 0; i < fields.length; i++) {
			sql += fields[i];
			if (i < fields.length - 1) {
				sql += ",";
			}
		}
		sql += " from CILIV_CHECKCONTENT where BHID='" + bhid + "'";
		Cursor cursor = dbHelper.query(sql);

		if (cursor.moveToFirst()) {

			for (int j = 0; j < values.length; j++) {
				values[j] = cursor.getString(j);
			}

		}
		cursor.close();
		dbHelper.close();
		return values;
	}

	private static IGeometry getGeometry(String BHID, int type,
			DBHelper dbHelper) {
		IGeometry geometry = null;
		switch (type) {
		case Type.JGLF:
			geometry = (IGeometry) getLineString(BHID, dbHelper);
			break;
		case Type.JGCT:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		case Type.JGQC:
			geometry = (IGeometry) getPolygon(BHID, dbHelper);
			break;
		case Type.JGBL:
			geometry = (IGeometry) getPolygon(BHID, dbHelper);
			break;

		case Type.SFDSL:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		case Type.SFXSL:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		case Type.LFDSL:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		case Type.LFXSL:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		case Type.MZGL:
			geometry = (IGeometry) getPolygon(BHID, dbHelper);
			break;
		case Type.GB:
			geometry = (IGeometry) getIPoint(BHID, dbHelper);
			break;
		}
		return geometry;
	}

	private static IPolygon getPolygon(String bhid, DBHelper dbHelper) {
		IPolygon polygon = null;

		IPoint[] points = getIPoints(bhid, dbHelper);

		if (points.length > 0) {
			ILinearRing linearRing = new LinearRing(points, 0);
			polygon = new Polygon(linearRing);
		}
		return polygon;
	}

	// 线性病害
	private static ILineString getLineString(String bhid, DBHelper dbHelper) {

		ILineString lineString = null;

		IPoint[] points = getIPoints(bhid, dbHelper);
		if (points.length > 0) {
			lineString = new LineString(points);
		}
		return lineString;
	}

	// 固定病害
	private static IPoint getIPoint(String bhid, DBHelper dbHelper) {
		String sql = "select X,Y from BHZB where BHID='" + bhid + "'";
		IPoint point = null;
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {

			float X = c.getFloat(0);
			float Y = c.getFloat(1);
			point = new Point(X, Y);

		}
		c.close();
		dbHelper.close();
		return point;
	}

	private static IPoint[] getIPoints(String bhid, DBHelper dbHelper) {
		Vector<IPoint> points = new Vector<IPoint>();
		String sql = "select X,Y from BHZB where BHID='" + bhid + "'";

		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			for (int j = 0; j < c.getCount(); j++) {

				float X = c.getFloat(0);
				float Y = c.getFloat(1);
				IPoint point = new Point(X, Y);
				points.add(point);
				c.moveToNext();
			}

		}
		c.close();
		dbHelper.close();
		IPoint[] mPoints = new Point[points.size()];
		points.toArray(mPoints);
		return mPoints;
	}

	// 判断病害编号是否存在
	private static boolean isExistBHID(String BHID) {

		String sql = "select _id from CILIV_CHECKCONTENT where BHID='" + BHID
				+ "'";
		Cursor cursor = dbHelper.query(sql);
		if (cursor.getCount() < 1) {
			cursor.close();
			dbHelper.close();
			return false;
		} else {
			cursor.close();
			dbHelper.close();
			return true;
		}

	}

	// 获取项目的照片起始编号
	private static int getImageID() {
		String sql = "select picture_beginnum  from TASK WHERE update_id='"
				+ PROJECTID + "'";
		Cursor cursor = dbHelper.query(sql);
		int mid = 1;
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {

				int id = cursor.getInt(0);

				mid = id;

				cursor.moveToNext();
			}
		}

		cursor.close();
		dbHelper.close();
		return mid;

	}

}
