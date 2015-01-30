package com.tygeo.highwaytunnel.common;

/**
 * 常量类
 */

public class Constant {

	public static final double VERSION = 1.0;
	
	public static final  int   MSG_SUCCESS=0x001;
	public static final  int   MSG_ERROR=0X002;
	public static final  int   MSG_CONN_TIMEOUT=0x003;
	public static final int   MSG_TEMP1=	0x004;
	public static final int   MSG_TEMP2=	0x005;
	public static final int   MSG_TEMP3=	0x006;
	public static final int   MSG_TEMP4=	0x007;
	
	//http://192.168.0.3:8088/WebService/BaseInfoService.asmx?
	public static final int Down = 0x3001;
	public static final int MSG_0x2005 = 0x2005;
	public static final int ERRER = 0x1002;
	
	
	public static final String METHOD_OF_CheckVersion="CheckVersion";
	public static final String PUBLIC_KEY = "AB8F46C7-8A94-4E33-B795-AC3DB67B3B52";
	//WebService
	public static final String SERVICE_NAMESPACE = "www.GZHTMIS.com";
	public final static String SERVER_SUB_URL = "/WebService/BaseInfoService.asmx";
	public static final String HTTP = "http://";
	public final static String SERVER_URL_IP_PORT = "192.168.0.3:8088";
	//用于SharedPreferences存放IP用的
	public static final String PREFERENCES_URL_ROUTE = "url_route";
	public static final String PREFERENCES_UNIT_CODE = "unit_code";
	
	
	
	//http://192.168.0.3:8088/WebService/BaseInfoService.asmx?
	
	public static  String serviceURL = "http://tjgeo.cn:8070/WebService/PadWebService.asmx";
	
	public static final String METHOD_OF_GETBASEROADINFO = "GetBaseRoadInfo"; 
	public static final String METHOD_OF_GETBASETUNNELINFO = "GetBaseTunnelInfo";
	public static final String METHOD_OF_GETBASEUSERINFO = "GetBaseUserInfo";
	public static final String METHOD_OF_GETBASECHECKINFO = "GetBaseCheckInfo";
	public static final String METHOD_OF_UPLOADBASECHECKFILE = "UploadBaseCheckFile";
	public static final String METHOD_OF_GETUNITINFO = "GetUnitInfo";
	public static final String METHOD_OF_SAVEHDDATAINFO = "SaveHDDataInfo";
	public static final String METHOD_OF_GETBASEEQUIPMENTINFO = "GetBaseEquipmentInfo";
	public static final String METHOD_OF_SAVEEQUIPMENTREGULARLTCHECK = "SaveEquipmentRegularlyCheck";
	public static final String METHOD_OF_SAVEEQUIPMENTPERIODICCHECK = "SaveEquipmentPeriodicCheck";
	public static final String METHOD_OF_SAVEREGULARCHECK = "SaveRegularCheck";
	
	
}
