package com.tongyan.yanan.common.utils;
/**
 * 
 * @className Constansts
 * @Desc 常量 + 配置信息类
 */
public class Constants {
	
	public static final double VERSION_CODE = 1.0;//版本号
	
	public static final String VERSION_NAME = "1.0.0";
	
	public static final String VERSION_TYPE = "E5D0002B-6695-4593-BD2A-A035F015494A";
	
	public static final String HTTP = "http://";
	
	public static final String COMMON_URL_IP = "www.tjgeo.cn:8085";
	
	public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
	
	public static final String LOGIN_URL = "/WebService/Common/UserService.ashx";
	//监测类型URL
    public final  static String SERVICE_SUBSIDE_POINT = "/WebService/HarthWork/MonitorService.ashx";
	//合同段URL
	public final static String SERVICE_PACTSELECT = "/WebService/HarthWork/LotService.ashx";
	//进度URL
	public final static String SERVICE_PROGRESS = "/WebService/HarthWork/Schedule.ashx";
	//OA URL
	public final  static String SERVICE_OA = "/WebService/HarthWork/OAService.ashx";
	//Oa Email & Message
	public final static String SERVICE_EM = "/WebService/Common/MessagesService.asmx";
	//原地貌
	public final static String SERVICE_ORIGINAL="/WebService/HarthWork/OriginalService.ashx";
	//强夯处理
	public final static String SERVICE_COMPACTION="/WebService/HarthWork/CompactionService.ashx";
	//盲沟
	public final static String SERVICE_GUTTER="/WebService/HarthWork/GutterService.ashx";
	//定点拍照
	public final static String SERVICE_POINTPHOTO="/WebService/HarthWork/PointPhotoService.ashx";
	//照片上传
	public final  static String SERVICE_PHOTO="/WebService/HarthWork/PhotoManageService.ashx";
	//质量
	public final  static String SERVICE_QUALITY = "/WebService/HarthWork/QualityService.ashx";
	//版本更新
	public  final static String SERVICE_VERSION="/WebService/HarthWork/VersionUpdateService.ashx";
	
	public final static String METHOD_OF_VERSION="GetVersionInfo";
	
	public static final String METHOD_OF_LOGIN = "Login";
	public static final String METHOD_OF_SUBSIDE_TYPE = "GetMonitorTypes";
	public static final String METHOD_OF_SUBSIDE_POINT = "GetMonitorPointByType";
	public static final String METHOD_OF_TERMPART_COMPACT = "GetLotListByUserId";
	public static final String METHOD_OF_MONITOR_VALUES_UPLOAD="AddMonitorData";
	public static final String METHOD_OF_PACTSELECT_All="GetLotAll";
	public static final String METHOD_OF_PACTSELECT_PERIODS="GetProject"; //获取所有期数
	public static final String METHOD_OF_GET_CONSTURCTION = "GetConsturctionAll";
	
	public static  final String METHOD_OF_ADDPTHOTO="AddPhoto";
	public static final String METHO_OF_POINTPHOTO_ALL="GetPointPhotoByLot";//定点拍照
	
	public static final String METHOD_OF_GetGutterType_ALL="GetGutterType"; //盲沟类型获取
	public static final String METHOD_OF_GETGUTTER="GetGutterByType" ;//根据类型获取盲沟
	
	public static final String METHOD_OF_ALL_COMPACTION="GetCompactionType"; //所有强夯类型
	public static final String  METHOD_OF_SINGLE_COMPACTION="GetCompactionByType"; //单条强夯类型
	
	public static final String METHOD_OF_ALLORIGINAL="GetOriginalTypeAll";//获取所有原地貌
	public static final String METHOD_OF_ORIGINAL="GetOriginalPoint";//获取单条原地貌
	
	public static final String METHOD_OF_OARULES_SINGLE="GetRegulationsById";//单条规章制度
	public static final String METHOD_OF_OA_RULES="GetRegulations";//规章制度
	
	public static final String METHOD_OF_OA_NOTICE = "GetAnnouncement";// 公告
	public static final String METHOD_OF_NOTICE_SINGLE = "GetAnnouncementById"; // 单条公告
	public static final String METHOD_OF_OA_RECEIVETEXT = "GetDispatch";// 收发文
	public static final String METHOD_OF_OA_RECEIVEText_SINGLE = "GetDispatchById"; // 获取单条收文
	public static final String METHOD_OF_PROGRESS_PROJECT_UPLOAD_WEEK = "GetCycleByProjectId"; // 获取周期进度
	public static final String METHOD_OF_ADDWEEKPLAN = "AddPlanWeek";// 周进度上传
	public static final String METHOD_OF_ADDMONTHPLAN = "AddPlanMonth";// 月进度上传
	public static final String METHOD_OF_RERPORTDAY = "ReportDay";// 日完成量上报
	public static final String METHOD_OF_RERPORTWEEK = "ReportWeek";// 周完成量上报
	public static final String METHOD_OF_RERPORTMONTH = "ReportMonth";// 月完成量上报
	
	public static final String METHOD_OF_GETDPTNATURE = "GetDptNature"; //OA-单位性质
	public static final String METHOD_OF_GETCONTACTS = "GetContacts"; //OA-通讯录
	public static final String METHOD_OF_GETWORKSCHEDULE = "GetWorkSchedule"; //OA-获取日程
	public static final String METHOD_OF_ADDWORKSCHEDULE = "AddWorkSchedule"; //OA-添加日程
	public static final String METHOD_OF_UPDATEWORKSCHEDULE = "UpdataWorkSchedule"; //OA-修改日程
	
	public static final String METHOD_OF_GETDETECTIONAREA = "GetDetectionArea";//质量-压实度区域
	public static final String METHOD_OF_GETDETECTIONAREABYID = "GetDetectionAreaById";//质量-压实度区域 by id
	public static final String METHOD_OF_ADDDETECTIONDATA = "AddDetectionData";//添加压实度
	
	public static final String METHOD_OF_ADDASSAYDATA="AddAssayData";//添加静载 波速标贯数据
	
	public static final String WEBSERVICE_OF_SENDMAIL = "SendMail";
	public static final String WEBSERVICE_OF_SENDSMS = "SendSMS";
	

	public static final String PREFERENCES_INFO_USERID = "preferences_info_userid";
	public static final String PREFERENCES_INFO_USERNAME = "preferences_info_username";
	public static final String PREFERENCES_INFO_ACCOUNT = "preferences_info_account";
	public static final String PREFERENCES_INFO_ROUTE = "preferences_info_route";//缓存的IP地址
	public static final String PREFERENCES_PROGRESS_SETTING = "preferences_progress_setting";
	public static final String PUBLIC_KEY = "FC240E11-D9D5-443F-BBE5-7CD49381B7A7";
	
	//Message
	public static final int SUCCESS = 0x0001;            //成功
	public static final int ERROR = 0x0002;              //返回后台提示
	public static final int CONNECTION_TIMEOUT = 0x0003; //请求超时
	public static final int OPERATION_ERROR = 0x0004;    //操作失败
	public static final int REFRESH=0x0008;//数据更新
	public static final int DATA_EMPTY=0X0000;
	public static final int INFA_EMPTY=0x00100;
	public static final int COMMON_MESSAGE_1 = 0x0005;   //自定义提示1
	public static final int COMMON_MESSAGE_2 = 0x0006;   //自定义提示2
	public static final int COMMON_MESSAGE_3 = 0x0007;   //自定义提示3
	public static final int PAGE_GO=0x9999;
	public static final int INSERT_TABLE_SUCCESS=0x11;
	
	public static final int WHAT_DID_REFRESH = 0x010;
	public static final int WHAT_DID_MORE = 0x012;
	
	//Fragement 
	public static final int FLAGEMENT_OA=0;
	public static final int FLAGEMENT_V=1;
	public static final int FLAGEMENT_PROGRESS=2;
	public static final int FLAGEMENT_QUALITY=3;	
	public static final int FLAGEMENT_PICTURE=4;
	public static final int FLAGEMENT_COUNT=5;
	
	// 数据回传
	public static final int PAGE_BACK_SUbSIDEPACTSELECPOINTDATALIST=0x101;
	public static final int PAGE_BACK_SUBSIDEPACTSELECTPOINTDATALIST_LOOK=0x104;
	public static final int PAGE_BACK_SUBSIDEPACTSELECTPOINTDATALIST_UPLOAD=0x102;
	public static final int PAGE_BACK_SUBSIDEPACTSELECTPOINTDATALIST_DEL=0x103;
	public static final int PAGE_BACK_PROGRESSPROJECT_UPLOAD_DATE=0x105; 
	public static final int PAGE_BACK_PROGRESS_MONTHPROJECT=0x106;
	public static final int PAGE_BACK_PROGRESS_MONTHPROJECT_WEEK=0x107; //月计划周期数
	//public static  final  String  TERMPART_TABLE="TermPart"; //期段表
	public static final String TABLE_TERMPART_PACT="TermPartPact";  //标段表
	public static final String TABLE_PROGRESS_INFO = "ProgressInfo";
	
	public static final String PATH_OF_OA_FILE = "/YanAn/oa/file";
	//public static final String PATH_OF_OA_IMAGE = "/YanAn/oa/image";
	
	
	public static final String METHOD_OF_LOTNUMBER="GetLotIdAndDataCount";
	}
