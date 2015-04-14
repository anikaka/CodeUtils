package com.tongyan.zhengzhou.common.utils;


import android.os.Environment;

/**
 * 
 * @Title: Constants.java 
 * @author Rubert
 * @date 2015-3-2 AM 11:07:14 
 * @version V1.0 
 * @Description: 
 */
public class Constants {
	
	/** COMMON */
	public static final double VERSION = 1.00001;//apk 版本
	public static final String VERSION_KEY = "0E3AAE35-22E1-43CC-8223-1C43AF1F67F6";//用于
	public static final int NEW_DB_VERSION = 2;  //数据库版本
	
	
	/** Key */
	public static final String PUBLIC_KEY = "20FACE3A-C5B5-47B3-BC8C-6150CAE5115A";
	
	public static final String PREFERENCES_INFO_USERID = "preferences_info_userid";
	public static final String PREFERENCES_INFO_USERNAME = "preferences_info_username";
	
	/** */
	public static final int SUCCESS = 0x1001;
	public static final int ERROR = 0x1002;//自定义
	public static final int CONNECTION_TIMEOUT = 0x1003;//网络连接超时
	public static final int DEFAULT = 0x1004;
	public static final int NO_NEW_DATA = 0x10005;//无最新数据
	public static final int GET_DATA_ERROR = 0x10006;//获取数据失败
	
	public static final int MSG_0x2001 = 0x2001;
	public static final int MSG_0x2002 = 0x2002;
	public static final int MSG_0x2003 = 0x2003;
	public static final int MSG_0x2004 = 0x2004;
	public static final int MSG_0x2005 = 0x2005;
	public static final int MSG_0x2006 = 0x2006;
	public static final int MSG_0x2007 = 0x2007;
	public static final int MSG_0x2008 = 0x2008;
	public static final int MSG_0x2009 = 0x2009;
	public static final int MSG_0x2010 = 0x2010;
	public static final int MSG_0x2011 = 0x2011;
	public static final int MSG_0x2012 = 0x2012;
	public static final int MSG_0x2013 = 0x2013;
	public static final int MSG_0x2014 = 0x2014;
	
	public static final int MSG_0x3001 = 0x3001;
	public static final int MSG_0x3002 = 0x3002;
	public static final int MSG_0x3003 = 0x3003;
	public static final int MSG_0x3004 = 0x3004;
	
	public static final int MSG_0x4001 = 0x4001;
	public static final int MSG_0x4002 = 0x4002;
	
	/**table_name*/
	public static final String TABLE_USER="User";
	
	/** URL */
	public static final String HTTP = "http://";
	//public final static String SERVER_URL_IP_PORT = "www.geodigital.cn:8587";//
	public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
//	public static final String SERVER_URL_IP_PORT = "192.168.0.158:7506";
	public static final String SERVER_URL_IP_PORT = "tjzz.top:8075";
    //public static final String SERVER_URL_IP_PORT = "http://192.168.0.64:1111/";
	//public static final String SERVER_URL_IP_PORT = "http://192.168.0.142:7700/";

	public static final String SERVER_SUB_URL = "/WebService/PadWebService.asmx";
	
	public static final String WPS_PATH = "http://www.geodigital.cn/PadCommonService/APP/WPSOffice_105.apk";
	
	public static final String PRFERENCES_IP_ROUTE_KEY = "ipsettings";
	public static final String PREFERENCES_IP_ROUTE = "server_url_ip";
	
	/** SharedPreferences */
	public static final String PREFERENCES_USER_CODE = "UserCode";
	public static final String PREFERENCES_USER_NAME = "UserName";
	public static final String PREFERENCES_USER_ACCOUNT = "UserAccount";
	public static final String PREFERENCES_URL_ROUTE = "url_route";
	//
	public static final String PREFERENCES_RED_LINE = "red_line";
	public static final String PREFERENCES_INTERVAL = "interval";
	public static final String PREFERENCES_STATION = "station";
	public static final String PREFERENCES_LINE_NUMBER = "line_number";
	public static final String PREFERENCES_LINE_NAME = "line_name";
	
	/** METHOD of INTERFACE, About these you must be begin by Client*/
	public static final String METHOD_OF_CLIENT_LOGIN = "Client_Login";//登录
	public static final String METHOD_OF_CLIENT_LINEINFO = "Client_LineInfo";//地铁线路信息-线路信息(更新通知)
	public static final String METHOD_OF_CLIENT_GETBASEINFOBYLINEIDS = "Client_GetBaseInfoByLineIds";//地铁线路信息-基础数据
	public static final String METHOD_OF_CLIENT_GETRELATEDFILES = "Client_GetRelatedFiles";//地铁线路信息-获取文件(设计/勘察/实施文件)
	public static final String METHOD_OF_CLIENT_STATIONINFO = "Client_StationInfo";//巡查管理-获取车站轮廓数据
	public static final String METHOD_OF_CLIENT_TUNNELINFO = "Client_TunnelInfo";//巡查管理-获取隧道轮廓数据
	public static final String METHOD_OF_CLIENT_REDLINE = "Client_RedLine";//巡查管理-获取红线轮廓数据
	public static final String METHOD_OF_CLIENT_GETDAMAGEANALYSISLIST = "Client_GetDamageAnalysisList";//检查管理-获取病害分析列表
	public static final String METHOD_OF_CLIENT_GETDAMAGEBYLOCATION = "Client_GetDamageByLocation";//检查管理-获取获取病害(个数)分析数据在隧道中的位置数据
	public static final String METHOD_OF_CLIENT_PROTECTPROJECTINFOLIST = "Client_ProtectProjectInfoList";//安保区监护工程信息-获取监护工程信息列表
	public static final String METHOD_OF_CLIENT_GETDANGERPROJECTLIST = "Client_GetDangerProjectList";//安保区监护工程信息-获取违规建筑信息列表
	public static final String METHOD_OF_CLIENT_GETMONITORPROJECTLIST = "Client_GetCommonProList";
	public static final String METHOD_OF_CLIENT_GRAPHBYCOMMONPRO = "Client_GraphByCommonPro";
	
	public static final String METHOD_OF_CLIENT_GETINFOBYLINE = "Client_GetInfoByLine";//线路基础数据获取
	public static final String METHOD_OF_DAMAGETYPEINFO = "DamageTypeInfo";	//下载字典表
	public static final String METHOD_OF_CLIENT_GetDAMAGEANALYSIS = "Client_GetDamageAnalysisList";	//获取获取病害(个数)分析数据
	public static final String METHOD_OF_CLIENT_GetDAMAGELOCATION = "Client_GetDamageByLocation";	//获取获取病害(个数)分析数据在隧道中的位置数据
	
	public static final String METHOD_OF_CheckVersionNew ="CheckVersionNew";
	
	public static final String METHOD_OF_MONITORCALENDARLIST = "Client_MonitorCalendarList";//监测日历测点数据获取
	
	/** Local File Path */
	public static final String  PATH_OF_SDCARD = Environment.getExternalStorageDirectory().getPath();	
	public static final String  PATH_OF_APK = "/TYSubwayClientZZ/apk/";
	public static final String  PATH_OF_IMG = PATH_OF_SDCARD + "/zz/img/protectpro/";
	public static final String  PATH_OF_FILE = PATH_OF_SDCARD + "/zz/file/protectpro/";
	public static final String  PATH_OF_ILLEGAL_IMG = "/TYSubwayClientZZ/Illegal/img/";
	public static final String  PATH_OF_ILLEGAL_VEDIO = "/TYSubwayClientZZ/Illegal/vedio/";
	
}
