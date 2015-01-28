package com.tongyan.yanan.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @className DBHelper
 * @author Rubert
 * @date 2014-3-7 AM 09:49:32
 * @Desc TODO
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "TYYanAn.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlite) {
		//用户表删除
		String drop_localUser = "DROP TABLE IF EXISTS LOCAL_USER;";
		/*//类型表删除
		String dropTableSubSideType="drop table if exists SubsideType ";
		//监测点删除
		String dropTableSubsidePoint="drop table if exists SubsidePoint";*/
		//创建用户表
		String createTableUser="CREATE TABLE LOCAL_USER(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"UserId 			VARCHAR(100)," +//用户ID
				"LoginAccount 			VARCHAR(100)," +
				"UserName 				VARCHAR(50)," + 
				"Password 	  		 	VARCJAR(50)," +
				"DeptId						VARCHAR(50)," +  //部门id
				"JobId 						VARCHAR(50)," + //职位Id
				"UserPhone   		    VARCHAR(20)," + //手机号码
				"UserEmail 				VARCHAR(50)," +
				"UserQQ					VARCHAR(20)," +
				"SysId						VARCHAR(50)," +
				"UserRoleId				VARCHAR(20)," +
				"UserSex					VARCHAR(20)," + 
				"UserBirthday			VARCHAR(20)," +
				"UserLastLoginDate  DATETIME DEFAULT CURRENT_TIMESTAMP," +
				"SavePassword        INTEGER DEFAULT 0" +
				") ";
		sqlite.execSQL(drop_localUser);
		sqlite.execSQL(createTableUser);
		
		String dropCheckPointTable = "DROP TABLE IF EXISTS CheckPoints;";
		String createCheckPointTable = "CREATE TABLE CheckPoints(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"NewId          VARCHAR(50)," +
				"MonitorName    VARCHAR(50)," +
				"MonitotTypeId  INTEGER," +
				"PointX	        VARHCHAR(50)," +
				"PointY	        VARCHAR(50)," +
				"PointZ	        VARCHAR(50)," +
				"CreateTime	    DATETIME DEFAULT CURRENT_TIMESTAMP," +
				"CheckTime	    DATETIME," +
				"UpdateTime	    DATETIME," +
				"CreateById	    VARHCHAR(100)," +
				"CheckTypeId    VARHCHAR(100)," +
				"CheckTypeName  VARHCHAR(100)," +
				"CheckItemId	VARHCHAR(100)," +
				"CheckItemName	VARHCHAR(100)," +
				"CurrentChange	VARHCHAR(100)," +
				"AllChange	    VARHCHAR(100)," +
				"ChangeRate	    VARHCHAR(100)," +
				"DataUnit	    VARHCHAR(100)," +
				"UpdateState	INTEGER" +//0-未上传,1-已上传
				")";
		sqlite.execSQL(dropCheckPointTable);
		sqlite.execSQL(createCheckPointTable);

	//标段表	
    String dropPactTable="DROP TABLE IF EXISTS TermPartPact";
	String createPactTable="create table TermPartPact(" +
			"_Id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"periodId   				VARCHAR(50)," +//期段id
			"periodName   			VARCHAR(50)," +//期段名称
			"NewId   					VARCHAR(50)," +//合同的id
			"ProjectId		 			VARCHAR(50)," +//项目id
			"LotName    	 			VARCHAR(50)," +//合同的名称
			"LotCode		  			VARCHAR(50),"+
			"CompactionUnit	 	 VARCHAR(50)," + //施工单位
			"SupervisorUnit		  	VARCHAR(50)," +//监理单位
			"ProjectName		  	VARCHAR(50)," +//工程名称
			"ProjectCount        	 VARCHAR(50)," +//工程总量 		
			"ProjectArea			  	VARCHAR(50)," + //工程面积
			"StationArea			 	 VARCHAR(50)" + //驻地面积
			")";		
	sqlite.execSQL(dropPactTable);
	sqlite.execSQL(createPactTable);
	
  //检测点上传数据存储表
		String dropMonitorPointUpload="DROP TABLE IF EXISTS MonitorPointUpload";
		String createMonitorPointUpload = "create table MonitorPointUpload("
				+ "_Id  INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "MonitorPointId						Varchar(50),"+ // 监测点Id
				"   MonitorPointName					Varchar(50),"
				+ "MonitorProjectTypeId	     	     Varchar(50)," +
					"UserId									 	 Varchar(50),"
				+ "MonitorProjectTypeName        Varchar(50),"
				+ "MonitorTypeId   					    Varchar(50),"+ // 检测类型Id
					"MonitorTypeName	 			    Varchar(50),"+ // 检测类型名称
					"PactId									    Varchar(50),"
				+ "PactName							   Varchar(50),"
				+ "MonitorValue      				       Varchar(50)," + // 本次检测值
				 	"Monitordeep						   Varchar(50),"+ //埋深
					"SuperviseDate						   Varchar(50)," + // 检测时间
					"UploadDate						       Varchar(50)," + // 上传时间
					"UploadUser							   Varchar(50)," + // 上传人
					"UploadState						   INTEGER DEFAULT 0," + // 默认为0代表为上传
					"UploadMark						 Varchar(50)" + // 上传备注
				")";	
	sqlite.execSQL(dropMonitorPointUpload);
	sqlite.execSQL(createMonitorPointUpload);
	
	// 周计划表
	String dropProgressUploadWeekPLan="DROP TABLE IF EXISTS WeekPlan";
	String createProgressUploadWeekPlan="CREATE TABLE WeekPlan(" +
			"_Id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"UserId              Varchar(50)," +
			"PeriodId			Varchar(50)," +//期段Id
			"LotId				Varchar(50),"+	//合同段Id
			"WeekPlanId    Varchar(50)," +	//周计划Id
			"CycleName	    Varchar(50)," + //周期名称
			"StartDate		    Varchar(50)," +	//开始时间
			"EndDate		    Varchar(50)," + //结束时间
			"CycleType		Varchar(50)," +
			"Remark			Varchar(50)," +
			"RemainDay		Varchar(20),"+  //剩余工期天数
			"State				INTEGER DEFAULT 0"+ //默认为0 未完成 1已完成 2 已提交 3 已送审 4 审核通过 5 退回 
			")";
	sqlite.execSQL(dropProgressUploadWeekPLan);
	sqlite.execSQL(createProgressUploadWeekPlan);
	
	//周计划日期表
	String  dropProgressUploadWeekPlanDay="DROP TABLE IF EXISTS WeekPlanDay";
	String  createProgressUploadWeekPlanDay =" CREATE TABLE WeekPlanDay(" +
			"_Id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"UserId              Varchar(50)," +
			"LotId			     Varchar(50),"+
			"WeekPlanId 		Varchar(50)," + //周计划Id
			"DayDate			Varchar(50)," +
			"State				INTEGER DEFAULT 0," +//0代表未完成,1代表已完成
			"DataType INTEGER DEFAULT 1" +//1：施工项，2：机械，3：人员
			")"; //默认 0未完成 1已完成
	sqlite.execSQL(dropProgressUploadWeekPlanDay);
	sqlite.execSQL(createProgressUploadWeekPlanDay);
	
	String dropProgressInfo = "DROP TABLE IF EXISTS ProgressInfo";
	String createProgressInfo = "CREATE TABLE ProgressInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			"PNewId Varchar(50)," +//父级id
			"PConstructionName Varchar(50)," +//父级名称
			"NewId Varchar(50)," +//子级数据id 唯一
			"ConstructionName Varchar(50)," +//子级名称
			"MeasureUnit Varchar(50)," +
			"Statistics Varchar(50)," +
			"Sort Varchar(50)," +//排序
			"DName Varchar(50)," +//单位
			"Pid varchar(50)," +//父级id(区分标题字段)
			"ConstructioType Varchar(50)," + //检查项类型 1：施工项 2：机械 3：人员
			"IsEnable Varchar(50)," + //操作保存数据字段-是否勾选
			"InputText varchar(200)," +//操作保存数据字段-输入值
			"SortType 	 Varchar(20)" + //排序类型
			")";
	sqlite.execSQL(dropProgressInfo);
	sqlite.execSQL(createProgressInfo);
	
	//周计划、月计划上传数据表
	String dropProgressUpdateInfo = "DROP TABLE IF EXISTS ProgressUpdateInfo";
	String createProgressUpdateInfo = "CREATE TABLE ProgressUpdateInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
									   "UserId 		VARCHAR(100)," +//用户ID
									   "LotId Varchar(50)," + //合同段id
									   "WeekPlanId Varchar(50)," + //周期id
									   "DayId Varchar(50)," + //具体天数id/具体月份id
									   "ItemsId Varchar(50)," +//检查项id
									   "ItemsType Varchar(50)," +//检查项类型 1：施工项 2：机械 3：人员
									   "RealDay Varchar(50)," +//施工项 的具体天/或具体月的具体周
									   "ValueContent Varchar(50))" ;//检查值
	
	sqlite.execSQL(dropProgressUpdateInfo);
	sqlite.execSQL(createProgressUpdateInfo);
	//月计划表
	String dropTableMonthPan="DROP TABLE IF EXISTS MonthPlan";
	String createTableMonthPlan="CREATE TABLE MonthPlan(" +
												"ID INTEGER PRIMARY KEY AUTOINCREMENT," + /**命名全部用大写的ID*/
												"UserId    Varchar(50)," +
												"PeriodId Varchar(50)," + //期段id
												"LotId Varchar(50)," + //合同段id
												"MonthDate Varchar(10)," +  //月计划日期
												"State	DEFAULT 0," + //状态 0：未完成，1：已完成，2：已提交...
												"CreatedTime DATETIME DEFAULT CURRENT_TIMESTAMP," + //创建时间
												"UpdateTime DATETIME," + //提交时间
												"Remark TEXT" + //备注
												")";
	sqlite.execSQL(dropTableMonthPan);
	sqlite.execSQL(createTableMonthPlan);
	
	//月计划周期表
	String dropTableMonthPlanWeekDate="DROP TABLE IF EXISTS MonthPanWeekDate";
	String createTableMonthPlanWeekDate="CREATE TABLE MonthPlanWeekDate(" +
																	"ID INTEGER PRIMARY KEY AUTOINCREMENT," +/**命名全部用大写的ID*/
																	"MonthPlanId," +      //月计划ID
																	"WeekDate," +		 	//每一周的时间
																	"DataType varchar(50)," +//检查项类型 1：施工项 2：机械 3：人员
																	"State	DEFAULT 0" +	//m默认值为 0未完成 1已完成
																	")";
	sqlite.execSQL(dropTableMonthPlanWeekDate);
	sqlite.execSQL(createTableMonthPlanWeekDate);
	
	//月计划上传信息表
	String dropTableMonthPlanUploadInfo="DROP TABLE IF EXISTS MonthPlanUpload"; 
	String createTableMonthPlanUploadInfo="CREATE TABLE MonthPlanUpload(" +
																	 "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
																	 "LotId," +//合同Id
																	 "MonthPlanId," + //月计划Id
																	 "WeekId," + 		 //周期数Id
																	 "ItemsId," +			 //上传字段Id
																	 "ItemsType," +	     //类型
																	 "WeekNum," +	//周期数
																	 "Value" + 			//值
																	 ")";
	sqlite.execSQL(dropTableMonthPlanUploadInfo);
	sqlite.execSQL(createTableMonthPlanUploadInfo);
	
	
	String dropCompletionDate = "DROP TABLE IF EXISTS CompletionDate";
	String createCompletionDate = "CREATE TABLE CompletionDate(" +
								  "ID               INTEGER PRIMARY KEY AUTOINCREMENT," +
								  "UserId 			VARCHAR(100)," +//用户ID
								  "LotId            VARCHAR(50)," +	//合同段ID
								  "DateInfo         VARCHAR(50)," + 
								  "WeekId           VARCHAR(50)," +
								  "WeekStartDate    VARCHAR(50)," +
								  "WeekEndDate      VARCHAR(50)," +
								  "Reason           TEXT," +//原因
								  "Question         TEXT," +//问题
								  "Remark           TEXT," +
								  "State            INTEGER DEFAULT 0," +
								  "DateType         INTEGER DEFAULT 0)";//数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
	sqlite.execSQL(dropCompletionDate);
	sqlite.execSQL(createCompletionDate);
	
	String dropCompletionUpdateInfo = "DROP TABLE IF EXISTS CompletionUpdateInfo";
	String createCompletionUpdateInfo = "CREATE TABLE CompletionUpdateInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
									   "UserId Varchar(50)," + //用户id
									   "WeekPlanId Varchar(50)," + //周期id
									   "CompletionDateId Varchar(50)," + //外键id
									   "DataType INTEGER DEFAULT 0," +  //数据类型1：日完成量，2：周完成量，3：月完成量，4：年完成量
									   "ItemsId Varchar(50)," +//检查项id
									   "ItemsType INTEGER DEFAULT 0," +//检查项类型 1：施工项 2：机械 3：人员
									   "ValueContent Varchar(50)" +//检查值
									   ")" ;
	sqlite.execSQL(dropCompletionUpdateInfo);
	sqlite.execSQL(createCompletionUpdateInfo);
	
	String dropDepartmentNature = "DROP TABLE IF EXISTS DepartmentNature";
	String createDepartmentNature = "CREATE TABLE DepartmentNature(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
									   "NewId Varchar(50)," + //主键Id
									   "SysId Varchar(50)," + //系统Id
									   "DType INTEGER DEFAULT 0," +  //类型
									   "DName Varchar(50)," +//
									   "DValue INTEGER DEFAULT 0," +//值
									   "DSort Varchar(50)" +//排序
									   ")" ;
	sqlite.execSQL(dropDepartmentNature);
	sqlite.execSQL(createDepartmentNature);
	
	String dropContacts = "DROP TABLE IF EXISTS Contacts";
	String createContacts = "CREATE TABLE Contacts(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
									   "UserId Varchar(100)," + //用户id
									   "DptId Varchar(100)," +
									   "CompanyId Varchar(100)," +
									   "UserName Varchar(200)," + //
									   "DeptName TEXT," + //
									   "UserPhone TEXT," +  //
									   "UserEmail TEXT," +//
									   "UserQQ TEXT," +//
									   "UserSex Varchar(50)," +//
									   "UserNamePinyin Varchar(100)," + 
									   "DeptNamePinyin Varchar(100)," + 
									   "DSelect INTEGER DEFAULT 0" +
									   ")" ;
	sqlite.execSQL(dropContacts);
	sqlite.execSQL(createContacts);
	//照片上传
	String dropPic="DROP TABLE IF EXISTS Pic";
	String createPic="CREATE TABLE Pic(" +
			"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			"UserId	Varchar(50)," +				 //用户Id
			"NewId Varchar(50)," +			     //上传Id
			"PicName  Varchar(50)," +	 	 //照片名称
			"PicSize	     VarhChar(50)," +		 //照片大小
			"PicPath	 Varchar(50)," +	      //存储路径
			"PicDate 	 Date DEFAULT CURRENT_TIMESTAMP," +//拍照日期
			"PicState    char(1)  Default 0," +  //上传状态
			"PicRename Varchar(50)" + 			//照片重命名
			")";
	sqlite.execSQL(dropPic);
	sqlite.execSQL(createPic);
	
	/** 质量模块-地基土压实度  */
	String dropQualityHarding = "DROP TABLE IF EXISTS QualityHarding";
	String createQualityHarding = "CREATE TABLE QualityHarding(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
									   "UserId VARCHAR(100)," + //用户id
									   "LocId VARCHAR(100)," +
									   "DetectionAreaId VARCHAR(100)," +//检测区域Id
									   "ChangeName VARCHAR(200)," + //变更名称
									   "AreaName VARCHAR(200)," + //区域名称
									   "DetectionCompany VARCHAR(200)," + //检测单位名称
									   "DetectionCompanyId VARCHAR(200)," + //检测单位id
									   "DetectionTime VARCHAR(50)," +  //检测时间
									   "Area VARCHAR(50)," + //面积
									   "Machine VARCHAR(200)," +    //辗压机械
									   "Variate VARCHAR(200)," +// 冲击变数
									   "Thickness VARCHAR(200)," + //虚铺厚度
									   "PressThickness VARCHAR(200)," + //压实厚度
									   "ExpContent TEXT," +
									   "ExpResult TEXT," +
									   "State INTEGER DEFAULT 0" +//0:未保存，1:已保存，2:已上传
									   ")" ;
	sqlite.execSQL(dropQualityHarding);
	sqlite.execSQL(createQualityHarding);
	
	/** 压实度点 */
	String dropQualityHardingLayer = "DROP TABLE IF EXISTS QualityHardingPoint";
	String createQualityHardingLayer = "CREATE TABLE QualityHardingPoint(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
										"HardingId VARCHAR(100)," + //用户id
										"LayerNum VARCHAR(100)," + //层数
										"PointX  VARCHAR(100)," +
										"PointY  VARCHAR(100)," +
										"PointZ VARCHAR(100)," +
										"Compaction VARCHAR(100)," + //压实度
										"Density VARCHAR(100)," + //干密度
										"MaxDensity VARCHAR(100)," + //最大干密度
										"EarthNature VARCHAR(100))"; //土的性质
	sqlite.execSQL(dropQualityHardingLayer);
	sqlite.execSQL(createQualityHardingLayer);
	
	// 平板静电荷实验表
	String dropStaticLoad = "DROP TABLE IF EXISTS StaticLoad";
	String createStaticLoad = "CREATE TABLE StaticLoad("
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
			  "UserId  Varchar(50)," + // 用户Id
			  "ProjectId   Varchar(50)," + //期段ID
			  "LotId      Varchar(50)," +	//合同段Id
			  "Person 	  Varchar(50)," + //检测人(多余的字段)
			  "No 		     Varchar(50)," + //编号
			  "Unit		 	 Varchar(50)," +  //检测单位
			  "Date	  Date," + 
			  "Content Varchar(200)," + //内容
			  "Conclusion Varchar(200)," + // 结论
			  "State  	   char(1) Default 0" + ")";
	sqlite.execSQL(dropStaticLoad);
	sqlite.execSQL(createStaticLoad);
	
	// 检测点
	String dropExaminePoint = "DROP TABLE IF EXISTS ExaminePoint";
	String createExaminePoint = "Create TABLE ExaminePoint("+
			 "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			 "NoId    INTEGER,"+     //编号Id
			 "PointName Varchar(50),"+
			 "PointX	Varchar(50)," + 
			 "PointY	Varhchar(50),"+
			 "PointZ	Varchar(50)" +
			  ")";//多余字段
	sqlite.execSQL(dropExaminePoint);
	sqlite.execSQL(createExaminePoint);
	
	
	String dropWaveInjection = "DROP TABLE IF EXISTS WaveInjectionInfo";
	String createWaveInjection = "CREATE TABLE WaveInjectionInfo(ID INTEGER PRIMARY KEY AUTOINCREMENT," + 
			"UserId             VARCHAR(50)," + // 用户Id
			"LocId              VARCHAR(50)," +   //合同段Id
			"PeriodId           VARCHAR(50)," +  // 工程(期段id)
			"DetectNumber       VARCHAR(50)," + //检测编号
			"DetectUnitName 	VARCHAR(100)," + // 检测单位
			"DetectUnitId 		VARCHAR(50)," + //检测单位ID
			"DetectDate		 	VARCHAR(50)," +  //检测时间
			"TestContent	    TEXT," +
			"TestConClusion     TEXT," + // 结论
			"DetectType         INTEGER Default 1," + //1:波速测试,2:标准灌入试验
			"State  	        INTEGER Default 0)";//0:未保存，1:已保存，2:已上传
	sqlite.execSQL(dropWaveInjection);
	sqlite.execSQL(createWaveInjection);
	
	String dropWaveInjectionPoint = "DROP TABLE IF EXISTS WaveInjectionPoint";
	String createWaveInjectionPoint = "Create TABLE WaveInjectionPoint(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
			 "WaveInjectionId    INTEGER,"+ //编号Id
			 "PointNums          VARCHAR(50),"+
			 "PointX			 VARCHAR(50)," + 
			 "PointY			 VARCHAR(50),"+
			 "PointZ			 VARCHAR(50)," +
			 "DeepStartValue	 VARCHAR(50)," +
			 "DeepEndValue	     VARCHAR(50)," +
			 "WaveVelocityValue	 VARCHAR(50))";
	sqlite.execSQL(dropWaveInjectionPoint);
	sqlite.execSQL(createWaveInjectionPoint);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean insert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		Long rowId = db.insert(tableName, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return false;
		}
		return true;
	}

	public Long getIdInsert(ContentValues values, String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		Long rowId = db.insert(tableName, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		if (rowId == -1) {
			return 0L;
		}
		return rowId;
	}

	public Cursor query(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db
				.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor count(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db
				.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryByParam(String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db
				.query(tableName, null, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryBySql(String sql, String[] params) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(sql, params);
		return c;
	}

	public void del(String tableName, String id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, "_id=?", new String[] { id });
	}

	public void del(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, null, null);
	}


}
