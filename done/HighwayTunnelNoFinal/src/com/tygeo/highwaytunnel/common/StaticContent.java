package com.tygeo.highwaytunnel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

import com.tygeo.highwaytunnel.DBhelper.DBManager;

public class StaticContent {
	public static String indexTag = "DK";
	public static int CivilListView3Position = 0;
	public static String last_task_name;
	public static String Up_Down;
	public static String DB_help = "/data/data/com.tygeo.highwaytunnel/databases/test1.db";
	public static String Check_Titile;
	public static int select_id = 0;
	public static String pic_id;
	public static String bh_id;
	public static String BH_p_name;
	public static String BH_index_name;
	public static String Photo_path;
	public static String task_name;// ����ѡ����������� ���Ǵ���������
	public static int task_id;// ���������id
	public static String photo_frist_id = "0";
	public static String photo_last_id = "0";
	public static int photocount = 0;
	public static String update_id;
	public static String Tsection_name;// ��·����
	public static String Tline_name;//��������
	public static String Tturnnel_name;//�������
	public static String check_for;// ��鷽��
	public static String Tsection_id;//��·id
	public static String Tline_id;//����id
	public static String Tturnnel_id;//���id
	public static int tabhost_id = 0;
	public static int islining = 0;
	
	
	
	public static int Isdown = 0;// �Ƿ�������
	public static int melete_count;
	public static int Ymelete_count;
	
	public static String S_X;
	public static int now_zh;// ��ǰ׮��
	
	
	public static int listselectindex=0;//ѡ��������
	public static int listpositonindex=0;//ѡ��λ�õ�����
	public static Map<String, ArrayList<String>> DQinfoV=new HashMap<String, ArrayList<String>>();
	public static Map<String, ArrayList<String>> DQinfoN=new HashMap<String, ArrayList<String>>();
	public static String webURLxml;
	public static  int proselect=-1;
//	public static  String DataBasePath="/data/data/com.tygeo.highwaytunnel/databases/test1.db";
	public static  String DataBasePath=Environment.getExternalStorageDirectory().getPath()+ "/"+"test1.db"; 
//	public static  String DataBasePath="mnt/sdcard/test1test.db"; 
	public static Map<String, String> localinfo=new HashMap<String, String>();
//	public static String WebUrl;
	
	//  http://192.168.0.3:8088/WebService/BaseInfoService.asmx?op=UploadBaseCheckFile
	public static String serviceURL1="192.168.0.3:8088/WebService/BaseInfoService.asmx?" ;
//	public static String serviceURL1="192.168.0.3:8088"; //ChenLang modify 2014/12/22
	public static String  UnitCode;
	
	public static String  TunnelBeginMile;
	public static String  TunnelEndMile;
	public static int  vTunnelBeginMile;
	public static int  vTunnelEndMile;
	
	
	public static String  TaskStartMile;
	public static String  TaskEndMile;
	
	
	public static int   BeginMile;
	public static int  EndMile;
	
	
	public  static int ele_check_type;//������޵�����
	public static int checkfors=0;
	
	public static int ischeckU=0;
	public static int ischeckD=0;
	
}