package com.tongyan.utils;
/**
 * 
 * @ClassName Constansts 
 * @author wanghb
 * @date 2013-7-15 pm 03:09:27
 * @desc 请求路径、消息参数、配置参数、文件路径等
 * @see password 发布APK签名密码 tongyan
 */
public class Constansts {
	
	public final static String VERSION = "1.99";
	public final static String VERSION_NAME="1.9.9";
	//Message6
	public final static int SUCCESS = 0x1;
	public final static int ERRER = 0x2;
	public final static int CONNECTION_TIMEOUT = 0x3;
	public final static int DEFAULT = 0x4;
	public final static int SYSTEM_ERROR = 0x5;
	public final static int NET_ERROR = 0x6;
	public final static int NO_DATA = 0x7;
	public final static int MES_TYPE_1 = 0x8;
	public final static int MES_TYPE_2 = 0x9;
	public final static int MES_TYPE_3 = 0xa;
	public final static int MES_TYPE_4=0x031;
	public final static int MES_TYPE_5=0x032;
	public final static int MES_TYPE_6=0x036;
	public final static int MES_TYPE_7=0x037;
	public final static int MES_TYPE_8=0x038;
	public final static int MES_TYPE_9=0x039;
	//命名空间       
	public final static String SERVICE_NAMESPACE = "http://www.geodigital.cn/";    //调用方法
	//请求URL
	public final static String SERVER_URL_IP_PORT = "epms.jxcngs.com:88";
	public final static String HTTP = "http://";
	
	public final static String SERVER_SUB_URL = "/WebService/AndroidService.asmx";
	
	//public key
	public final static String PUBLIC_KEY = "AA8F96C7-8294-4E33-B695-AD3BB67B1B41";
	/** 用户信息参数  */
	public final static String INFO_USER_ACCOUNT = "info_user_account";
	public final static String INFO_USER_NAME = "info_user_name";
	public final static String INFO_USER_ID = "info_user_id";
	public final static String INFO_USER_PASSWORD = "info_user_password";
	public final static String PREFERENCES_FIXED_FILE_PATH = "fixed_file_path";
	public final static String PREFERENCES_SQLITE_VERSION = "section_sqlite_version";
	
	/**请求方法 **/
	//登录请求方法
	public final static String METHOD_OF_LOGIN = "Login";
	//通讯录请求方法
	public final static String METHOD_OF_GETLINKMAN = "GetLinkMan";
	//公告通知列表返回
	public final static String METHOD_OF_GETLIST = "GetList";
	//公告单笔内容
	public final static String METHOD_OF_GETCONTENT = "GetContent";
	//日程添加
	public final static String METHOD_OF_ADD = "Add";
	//日志删除
	public final static String METHOD_OF_DELETE = "Delete";
	//日志更新
	public final static String METHOD_OF_UPDATE = "Update";
	//获取项目信息
	public final static String METHOD_OF_GETPROJECT = "GetProject";
	//获取指定人员当天内最近一笔的GPS坐标
	@Deprecated
	public final static String METHOD_OF_GETEMPGPS = "GetEmpGps";
	//回当前范围内的人员信息
	@Deprecated
	public final static String METHOD_OF_GETEMPAREAGPS = "GetMapAreaGps";
	//获取人员的运行轨迹 
	public final static String METHOD_OF_GETLISTNOPAGE = "GetListNoPage";
	//获取消息提醒总数
	public final static String METHOD_OF_GETMESSAGECOUNT = "GetMessageCount";
	//获取待办公文的具体内容
	@Deprecated
	public final static String METHOD_OF_GETDOCUMENTCONTENT = "GetDocumentContent";
	//获取待办公文-收文的具体内容
	@Deprecated
	public final static String METHOD_OF_GETRECEIVEDOCUMENTCONTENT = "GetReceiveDocumentContent";
	//获取待办公文-发文的具体内容
	@Deprecated
	public final static String METHOD_OF_GETSENDDOCUMENTCONTENT = "GetSendDocumentContent";
	//批复公文
	@Deprecated
	public final static String METHOD_OF_UPDATEDOCUMNT = "UpdataDocument";
	//获取版本信息
	public final static String METHOD_OF_GETVERSION = "GetVersion";
	//检查下一流程
	@Deprecated
	public final static String METHOD_OF_CHECKNEXTFLOWEMP = "CheckNextFlowEmp";
	//风险提示个数
	@Deprecated
	public final static String METHOD_OF_GETRISKCOUNT = "GetRiskCount";
	//
	@Deprecated
	public final static String METHOD_OF_GETNEARBYUNIT = "GetNearByUnit";
	//获取WPS Office地址
	public final static String METHOD_OF_GETOFFICEAPK = "GetOfficeAPK";
	//获取对应标段下工程的总数
	public final static String METHOD_OF_GETSECTIONSUBITEMCOUNT = "GetSectionSubItemCount";
	//获取对应标段下工程信息列表
	public final static String METHOD_OF_GETSECTIONSUBITEMLISTBYPAGE = "GetSectionSubItemListByPage";
	//下载工程编号
	public final static String METHOD_OF_GETSECTIONITEM = "GetSectionItem";
	//工程订阅-上传图片
	public final static String METHOD_OF_ATTACHMENTUPLOAD = "AttachmentUpload";
	//工程订阅-获取对应工程图片列表
	public final static String TYPE_OF_GETLIST_GETATTACHMENTSLISTBYPAGE = "GetAttachmentsListByPage";
	//轨迹查询-人员权限控制
	public final static String METHOD_OF_GETEMPLOYEEVISIBLE = "GetEmployeeLocusVisibleIds";
	//工程订阅-发文管理
	public final static String TYPE_OF_DOCUMENTSEND = "DocumentSend";
	//工程订阅-收文关联
	public final static String TYPE_OF_DOCUMENTGET = "DocumentGet";
	//获取待办公文-收发文审批前数据
	public final static String METHOD_OF_READYFORAPPROVE = "ReadyForApprove";
	//待办公文-收发文审批
	public final static String METHOD_OF_APPROVE = "Approve";
	//计量流程信息
	public final static String METHOD_OF_FLOW="FlowInfos";
	//计量管理-计量
	public final static String TYPE_OF_MEASURE = "Meas";
	
	public final static String TYPE_OF_MID_MEASURE = "MeasurementList";
	
	public final static String TYPE_OF_MEASUER_CERTIFICATE = "MeasurementCertificate";
	
	public final static String  METHOD_OF_RECORDFORM_OPTION="GetListNoPage";
	
	
	public final static String ADD_OF_COMMAND="Add"; //新增指令
	public final static String UPDATE_OF_COMMAND="update"; //修改指令
	public final static String  DELETE_OF_COMMAND="Delete"; //删除指令
	public final static String UPLOAD_OF_COMMAND="DocumentFileUpload";//上传附件
	public final static String  START_OF_COMMAND="FlowStart";//流程启动
	public final static String   APPROVE_OF_COMMAND="Approve";//监理指令审批以及人员信息获取
	public final static int PAGE = 1;
	public final static int PAGE_SIZE = 10;
	public final static int SECTION_PAGE_SIZE = 600;
	/**百度地图 **/
	//时间
	public final static int BAIDU_MAP_TIME = 5;//five minutes
	public final static String BAIDU_MAP_KEY = "D3FD1B3854C2BCD46A10187FC0EA3EAA251DF4B7";
	//初始化经纬度
	public final static double mLat = 26.05;
	public final static double mLon = 115.40;
	/** 文件路径  */
	public final static String CN_CAMERA_CHECK_PATH = "/CnClient/check/";
	
	public final static String CN_NOTICE_PATH = "/CnClient/notice/";
	
	public final static String CN_DB_PATH = "/CnClient/db/";
	
	public final static String CN_APK_PATH = "/CnClient/apk/";
	
	public final static String CN_CAMERA_RISK_PATH = "/CnClient/risk/";
	
	public final static String CN_CAMERA_GPS_PATH = "/CnClient/gps/image/";
	
	public final static String CN_CAMERA_NO_NET_GPS_PATH = "/CnClient/gps/nonetimage/";
	
	public final static String CN_CAMERA_COMMAND_PATH = "/CnClient/gps/command/";
	
	
	public final static String CN_SECTION_DB_NAME = "cnSectionOA.db";
}
