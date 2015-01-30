package com.tongyan.common.entities;
/**
 * 
 * @ClassName _LocRisk.java
 * @Author wanghb
 * @Date 2013-9-3 pm 02:00:04
 * @Desc TODO
 */
public class _LocRisk {
	
	private String id;
	private String rowId;
	private String userId;
	private String iid;
	private String iContent;
	private String sid;
	private String sContent;
	private String prowid;
	private String pContent;
	private String currDate;
	private String isFinish;//0:未检查,1:已上传,2:已检查,3:正检查
	private String riskName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getiContent() {
		return iContent;
	}
	public void setiContent(String iContent) {
		this.iContent = iContent;
	}
	public String getsContent() {
		return sContent;
	}
	public void setsContent(String sContent) {
		this.sContent = sContent;
	}
	public String getProwid() {
		return prowid;
	}
	public void setProwid(String prowid) {
		this.prowid = prowid;
	}
	public String getpContent() {
		return pContent;
	}
	public void setpContent(String pContent) {
		this.pContent = pContent;
	}
	public String getCurrDate() {
		return currDate;
	}
	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	public String getRiskName() {
		return riskName;
	}
	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSid() {
		return sid;
	}
	
}
