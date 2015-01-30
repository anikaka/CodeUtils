package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.tygeo.highwaytunnel.common.StaticContent;

	public class DailyCheckTunnelInfoDto implements Serializable{
	
	int BaseCheckCode ;
	String BaseCheckTunnelType;
	String TunnelStake;
	public String getTunnelStake() {
		return TunnelStake;
	}
	public int getBaseCheckCode() {
		return BaseCheckCode;
	}
	public void setTunnelStake(String tunnelStake) {
		TunnelStake = tunnelStake;
	}
	ArrayList<CheckDetailInfo> checkDetailDto;
	public int getBasecheckCode() {
		return BaseCheckCode;
	}
	public void setBaseCheckCode(int baseCheckCode) {
		BaseCheckCode = baseCheckCode;
	}
	public String getBaseCheckTunnelType() {
		return BaseCheckTunnelType;
	}
	public void setBaseCheckTunnelType(String baseCheckTunnelType) {
		BaseCheckTunnelType = baseCheckTunnelType;
	}
	
	public ArrayList<CheckDetailInfo> getCheckDetailDto() {
		return checkDetailDto;
	}
	
	
	public void setCheckDetailDto(ArrayList<CheckDetailInfo> checkDetailDto) {
		this.checkDetailDto = checkDetailDto;
	}
	
	//日常检查dto
	public static  DailyCheckTunnelInfoDto getUpDemo(String up_down) throws Exception{
		DailyCheckTunnelInfoDto dto=new DailyCheckTunnelInfoDto();
		ArrayList<CheckDetailInfo> ck=CheckDetailInfo.GetRCdemoU(up_down);
		dto.setBaseCheckCode(0);
		String text="";
		if (up_down!=null&&!up_down.equals("")) {
			if (up_down.equals("0")) {
				text="上行";
			}else{
				text="下行";
			}
		}
		dto.setBaseCheckTunnelType(text);
		dto.setTunnelStake(StaticContent.TaskStartMile);
		dto.setCheckDetailDto(ck);
		
		return dto;
	}
	
	public static  DailyCheckTunnelInfoDto getDownDemo(){
		DailyCheckTunnelInfoDto dto=new DailyCheckTunnelInfoDto();
		ArrayList<CheckDetailInfo> ck=CheckDetailInfo.GetRCdemoD();
		dto.setBaseCheckCode(0);
		dto.setBaseCheckTunnelType("下行");
		dto.setTunnelStake(StaticContent.TaskStartMile);
		dto.setCheckDetailDto(ck);
		return dto;
	}

	public static  DailyCheckTunnelInfoDto getUpDemoDQ(String up_down) throws Exception{
		DailyCheckTunnelInfoDto dto=new DailyCheckTunnelInfoDto();
		ArrayList<CheckDetailInfo> ck=CheckDetailInfo.GetDQdemoD(up_down);
		dto.setBaseCheckCode(0);
		String text="";
		if (up_down!=null&&!up_down.equals("")) {
			if (up_down.equals("0")) {
				text="上行";
			}else{
				text="下行";
			}
		}
		dto.setBaseCheckTunnelType(text);
		dto.setTunnelStake(StaticContent.TaskStartMile);
		dto.setCheckDetailDto(ck);
		
		return dto;
	}
	public static  DailyCheckTunnelInfoDto getDownDemoDQ(String up_down) throws Exception{
		DailyCheckTunnelInfoDto dto=new DailyCheckTunnelInfoDto();
		ArrayList<CheckDetailInfo> ck=CheckDetailInfo.GetDQdemoD(up_down);
		dto.setBaseCheckCode(0);
		dto.setBaseCheckTunnelType("下行");
		dto.setTunnelStake(StaticContent.TaskStartMile);
		dto.setCheckDetailDto(ck);
		return dto;
	}
	
	
}
