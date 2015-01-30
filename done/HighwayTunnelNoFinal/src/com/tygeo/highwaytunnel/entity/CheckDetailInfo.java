package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.R.color;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.common.StaticContent;

public class CheckDetailInfo implements Serializable {
	String CheckContent;// 检查内容
	String StateDescription;// 状态描述
	String CheckItemState;// 检查评定等级
	String CheckItemMeasure;// 养护措施
	int CheckItemLocationCode;// 检查空间位置
	String CheckPointArray;// 检查手势轨迹
	List<CheckPicInfo> CheckPicInfoList;// 检查图片
	String CheckItemCode;// 洞口,洞门等id
	int involve;	
	int CheckTunnelInfoId;
	int CheckType;
	
	public int getInvolve() {
		return involve;
	}

	public void setInvolve(int involve) {
		this.involve = involve;
	}

	int getcheckitem;
	int value;
	int _id;
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getGetcheckitem() {
		return getcheckitem;
	}

	public void setGetcheckitem(int getcheckitem) {
		this.getcheckitem = getcheckitem;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	String bhid;
	
	
	public String getCheckItemCode() {
		return CheckItemCode;
	}

	public void setCheckItemCode(String checkItemCode) {
		CheckItemCode = checkItemCode;
	}

	public int getCheckTunnelInfoId() {
		return CheckTunnelInfoId;
	}

	public void setCheckTunnelInfoId(int checkTunnelInfoId) {
		CheckTunnelInfoId = checkTunnelInfoId;
	}

	public int getCheckType() {
		return CheckType;
	}

	public void setCheckType(int checkType) {
		CheckType = checkType;
	}

	public String getBhid() {
		return bhid;
	}

	public void setBhid(String bhid) {
		this.bhid = bhid;
	}

	public String getCheckContent() {
		return CheckContent;
	}

	public void setCheckContent(String checkContent) {
		CheckContent = checkContent;
	}

	public String getStateDescription() {
		return StateDescription;
	}

	public void setStateDescription(String stateDescription) {
		StateDescription = stateDescription;
	}

	public String getCheckItemState() {
		return CheckItemState;
	}

	public void setCheckItemState(String checkItemState) {
		CheckItemState = checkItemState;
	}

	public String getCheckItemMeasure() {
		return CheckItemMeasure;
	}

	public void setCheckItemMeasure(String checkItemMeasure) {
		CheckItemMeasure = checkItemMeasure;
	}

	public int getCheckItemLocationCode() {
		return CheckItemLocationCode;
	}

	public void setCheckItemLocationCode(int checkItemLocationCode) {
		CheckItemLocationCode = checkItemLocationCode;
	}

	public String getCheckPointArray() {
		return CheckPointArray;
	}

	public void setCheckPointArray(String checkPointArray) {
		CheckPointArray = checkPointArray;
	}

	public List<CheckPicInfo> getCheckPicInfoList() {
		return CheckPicInfoList;
	}

	public void setCheckPicInfoList(List<CheckPicInfo> list) {
		CheckPicInfoList = list;
	}
	
	//获得日常检查参数，对应的位置上是否存在病害
	public static ArrayList<CheckDetailInfo> GetRCdemoU(String up_down) {
		ArrayList<CheckDetailInfo> arrck = new ArrayList<CheckDetailInfo>();
		String[] a = { "1", "1", "1", "1", "1", "2", "3", "4", "5", "5", "6",
				"6", "7", "8" };
		int[] b = { 1, 2, 3, 4, 5, 0, 0, 0, 6, 7, 8, 9, 0, 0 };
		if (up_down!=null&&!up_down.equals("")) {
	    ArrayList<CheckDetailInfo> Bhlist =getBHdes(StaticContent.update_id,Integer.parseInt(up_down));	
		}else{
		ArrayList<CheckDetailInfo> Bhlist =getBHdes(StaticContent.update_id,0);
		}
		for (int i = 0; i < 14; i++) {
			
			CheckDetailInfo ac1 = new CheckDetailInfo();
			//A表示存在，B表示不存在，C未检查.
			ac1.setCheckContent(replaceCheck(getRClistU("'", i), b[i],0));
			ac1.setCheckItemCode(a[i]);
			ac1.setCheckItemLocationCode(b[i]);
			ac1.setStateDescription(null);
			ac1.setCheckItemMeasure(null);
			// ac1.setCheckTunnelInfoId(0);
			// ac1.setCheckType(0);
			// ac1.setCheckItemState(null);
			// ac1.setCheckItemState(null);
			arrck.add(ac1);
		}

		return arrck;
	}
	
	public static ArrayList<CheckDetailInfo> GetRCdemoD() {
		ArrayList<CheckDetailInfo> arrck = new ArrayList<CheckDetailInfo>();
		String[] a = { "1", "1", "1", "1", "1", "2", "3", "4", "5", "5", "6",
				"6", "7", "8" };
		int[] b = { 1, 2, 3, 4, 5, 0, 0, 0, 6, 7, 8, 9, 0, 0 };
		for (int i = 0; i < 14; i++) {
			
			CheckDetailInfo ac1 = new CheckDetailInfo();
			ac1.setCheckContent(replaceCheck(getRClistU("'", i), b[i],1));
			ac1.setCheckItemCode(a[i]);
			ac1.setCheckItemLocationCode(b[i]);
			ac1.setStateDescription(null);
			ac1.setCheckItemMeasure(null);
			// ac1.setCheckTunnelInfoId(0);
			// ac1.setCheckType(0);
			// ac1.setCheckItemState(null);
			// ac1.setCheckItemState(null);
			arrck.add(ac1);
		}

		return arrck;
	}
	
	public static String getRClistU(String update_id, int num) {

		String s = "";
		ArrayList<String> list = new ArrayList<String>();
		/** B 不需要改  C  什么都没做*/
//		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
//		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
//		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
//		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
//		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
//		list.add("11C,12C,13C,14C,15C,16C,17C,18C");
//		list.add("19C,20C,21C,22C");
//		list.add("23C,24C,25C,26C,27C");
//		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
//		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
//		list.add("36C,37C,38C,39C,40C,41C,42C,43C,44C,45C");
//		list.add("36C,37C,38C,39C,40C,41C,42C,43C,44C,45C");
//		list.add("46C,47C,48C,49C,50C,51C");
//		list.add("52C,53C,54C");
		list.add("1C,2C,3C,5C,10C");
		list.add("1C,2C,3C,5C,10C");
		list.add("1C,2C,3C,5C,10C");
		list.add("1C,2C,3C,5C,10C");
		list.add("1C,2C,3C,5C,10C");
		list.add("11C,12C,13C,14C,15C,16C,17C,18C");
		list.add("19C,20C,21C,22C");
		list.add("23C,24C");
		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
		list.add("37C,38C,39C,40C,42C,45C");
		list.add("37C,38C,39C,40C,42C,45C");
		list.add("46C,47C,48C,51C");
		list.add("52C,53C,54C");
//		for (int i = 0; i < Bhlist.size(); i++) {
//			for (int j = 0; j <14; j++) {
//				
//				String s1=list.get(j).replace(Bhlist.get(i).toString()+"B", Bhlist.get(i).toString()+"A");
//				
//				list.set(j, s1);
//				
//				list.get(j).replace("1", "2");
//				System.out.println(Bhlist.get(i).toString()+"B");
//			}
			
//		}
		
		
		return list.get(num);

	}

	public static String getRClistD(int num) {
		String s = "";
		ArrayList<String> list = new ArrayList<String>();
//		ArrayList<CheckDetailInfo> Bhlist =getBHdes(StaticContent.update_id,1);
		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
		list.add("1C,2C,3C,4C,5C,6C,7C,8C,9C,10C");
		list.add("11C,12C,13C,14C,15C,16C,17C,18C");
		list.add("19C,20C,21C,22C");
		list.add("23C,24C,25C,26C,27C");
		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
		list.add("28C,29C,30C,31C,32C,33C,34C,35C");
		list.add("36C,37C,38C,39C,40C,41C,42C,43C,44C,45C");
		list.add("36C,37C,38C,39C,40C,41C,42C,43C,44C,45C");
		list.add("46C,47C,48C,49C,50C,51C");
		list.add("52C,53C,54C");
//		for (int i = 0; i < Bhlist.size(); i++) {
//			for (int j = 0; j<14; j++) {
//				 String s1=list.get(j).replace(Bhlist.get(i).toString()+"B", Bhlist.get(i).toString()+"A");
//				list.set(j, s1);
//				
//				list.get(j).replace("1", "2");
//				System.out.println(list.get(j));	
//			}
//			
//		}
		return list.get(num);

	}
	
	public static ArrayList<CheckDetailInfo> GetDQdemoU() {
		ArrayList<CheckDetailInfo> arrck = new ArrayList<CheckDetailInfo>();
		String[] a = { "9", "9", "9", "9", "9", "10", "11", "12", "12", "13",
				"13", "14", "15", "16" };
		int[] b = { 10, 11, 12, 13, 14, 0, 0, 0, 15, 16, 17, 18, 0, 0 };
//		ArrayList<CheckDetailInfo> Bhlist =getBHdes(StaticContent.update_id,0);
		for (int i = 0; i < 14; i++) {
			
			CheckDetailInfo ac1 = new CheckDetailInfo();
			ac1.setCheckContent(replaceCheck(getRClistD( i), b[i],0));
			ac1.setCheckItemCode(a[i]);
			ac1.setCheckItemLocationCode(b[i]);
			ac1.setStateDescription(null);
			ac1.setCheckItemMeasure(null);
			// ac1.setCheckTunnelInfoId(0);
			// ac1.setCheckType(0);
			// ac1.setCheckItemState(null);
			// ac1.setCheckItemState(null);
			arrck.add(ac1);
		}

		return arrck;
	}
	
	public static ArrayList<CheckDetailInfo> GetDQdemoD(String up_down)throws Exception {
		ArrayList<CheckDetailInfo> arrck = new ArrayList<CheckDetailInfo>();
//		String[] a = { "9", "9", "9", "9", "9", "10", "11", "12", "12", "13",
//				"13", "14", "15", "16" };
		String[] a = { "9", "9", "9", "9", "9", "10", "11", "12", "13", "13",
				"14", "14", "15", "16" };
		int[] b = { 10, 11, 12, 13, 14, 0, 0, 0, 15, 16, 17, 18, 0, 0 };
		ArrayList<CheckDetailInfo> Bhlist =getBHdes(StaticContent.update_id,Integer.parseInt(up_down));
		for (int i = 0; i < 14; i++) {
			
			CheckDetailInfo ac1 = new CheckDetailInfo();
			ac1.setCheckContent(replaceCheck(getDQlistU(i), b[i],Integer.parseInt(up_down))); //I==8 
			ac1.setCheckItemCode(a[i]);
			ac1.setCheckItemLocationCode(b[i]);
			ac1.setStateDescription(null);
			ac1.setCheckItemMeasure(null);
			// ac1.setCheckTunnelInfoId(0);
			// ac1.setCheckType(0);
			// ac1.setCheckItemState(null);
			// ac1.setCheckItemState(null);
			arrck.add(ac1);
		}
		return arrck;
	}
	
	
	
	
	public static String getDQlistU(int i) {
		String s = "";
		ArrayList<String> list = new ArrayList<String>();
//		ArrayList<String> Bhlist =getBHdes(StaticContent.update_id);
//		list.add("55C,56C,57C,58C,59C,60C,61C,62C,63C,64C,65C,66C,67C");
//		list.add("55C,56C,57C,58C,59C,60C,61C,62C,63C,64C,65C,66C,67C");
//		list.add("55C,56C,57C,58C,59C,60C,61C,62C,63C,64C,65C,66C,67C");
//		list.add("55C,56C,57C,58C,59C,60C,61C,62C,63C,64C,65C,66C,67C");
//		list.add("55C,56C,57C,58C,59C,60C,61C,62C,63C,64C,65C,66C,67C");
//		list.add("68C,69C,70C,71C,72C,73C,74C,75C,76C");
//		list.add("77C,78C,79C,80C,81C,82C,83C,84C,85C,86C,87C,88C");
//		list.add("89C,90C,91C,92C,93C,94C");
//		list.add("95C,96C,97C,98C,99C,100C,101C,102C,103C,104C,105C,106C,107C,108C,109C,110C,111C,112C,113C,114C,115C,116C,117C,118C,119C,120C,121C,122C,123C,124C,125C,126C,127C,128C,129C");
//		list.add("95C,96C,97C,98C,99C,100C,101C,102C,103C,104C,105C,106C,107C,108C,109C,110C,111C,112C,113C,114C,115C,116C,117C,118C,119C,120C,121C,122C,123C,124C,125C,126C,127C,128C,129C");
//		list.add("130C,131C,132C,133C,134C,135C");
//		list.add("130C,131C,132C,133C,134C,135C");
//		list.add("136C,137C,138C,139C,140C,141C,142C");
//		list.add("143C,144C,145C");
		//因为定期检查洞门新增了挂冰,所以需要调整,一下是调整内容//TODO
		list.add("55C,56C,57C,62C,67C");
		list.add("55C,56C,57C,62C,67C");
		list.add("55C,56C,57C,62C,67C");
		list.add("55C,56C,57C,62C,67C");
		list.add("55C,56C,57C,62C,67C");
		list.add("68C,69C,70C,71C,72C,73C,74C,75C,76C");
		list.add("77C,78C,79C,80C,81C,82C,83C,84C,85C");
		list.add("89C,90C");
		list.add("95C,97C,98C,99C,100C,101C,102C,103C,104C,105C");
		list.add("95C,96C,97C,98C,99C,100C,101C,102C,103C,104C,105C");
		list.add("130C,131C,132C,133C,134C,135C,136C");
		list.add("130C,131C,132C,133C,134C,135C,136C");
		list.add("137C,138C,139C,140C");
		list.add("143C,144C,145C");
		return list.get(i);
		
	}
	
	//

	public static ArrayList<CheckDetailInfo> getBHdes(String update_id,int up_down) {
		ArrayList<CheckDetailInfo>  list= new ArrayList<CheckDetailInfo>();
//		ArrayList<String> list2 = new ArrayList<String>();
		String sql = " select distinct CHECKID,POSITIONID,involve  from CILIV_CHECKCONTENT where task_id='"
				+ update_id + "'and UP_DOWN='"+up_down+"'";
		Cursor c = DB_Provider.dbHelper.query(sql);
		try {
		if (c.moveToFirst()) {
			do {
				CheckDetailInfo ck=new CheckDetailInfo();
				ck.setValue(c.getInt(0));
				ck.setInvolve(c.getInt(2));
				if(c.getString(1)==null){
				ck.setGetcheckitem(0);
//				System.out.println("空");
				}else {
					
				ck.setGetcheckitem(c.getInt(1));	
//				System.out.println("设置空间位置为:"+c.getInt(1));
				}
				System.out.println(c.getString(0));
				list.add(ck);
			} while (c.moveToNext());
		}
		} catch (Exception e) {
		}
		
		return list;
	}
	
	//替换是否存在病害的方法
	public static String replaceCheck(String s,int num,int up_down){
		ArrayList<CheckDetailInfo>  list=getBHdes(StaticContent.update_id, up_down);
//		ArrayList<CheckDetailInfo>  list2=new ArrayList<CheckDetailInfo>();
		System.out.println(s);
		for (int i = 0; i <list.size(); i++) {
			if (list.get(i).getGetcheckitem()==num) {
				if (list.get(i).getInvolve()==0) {
					String ss=list.get(i).getValue()+"";
					s=s.replace(ss+"C", ss+"A");	
				}else{
					String ss=list.get(i).getValue()+"";
					s=s.replace(ss+"C", ss+"B");	
				}
				
				
//				System.out.println("替换后的s:"+s);
			}
		}
	return s;	
	}
	
}
