package com.tongyan.common.entities;

import java.io.Serializable;

/**
 * 
 * @ClassName _Agendas 
 * @author wanghb
 * @date 2013-7-23 pm 04:50:12
 * @desc TODO
 */
public class _Agendas implements Serializable{
	private static final long serialVersionUID = 8754570809479807758L;
	private String rowId;//主键
	private String sTitle;
	private String sContent;//提醒内容
	private String sTime;
	private String eTime;
	private String isAllDay;//全天事件
	private String createEmp;//创建人
	private String empId;//创建人id
	
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getsTitle() {
		return sTitle;
	}
	public void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}
	public String getsContent() {
		return sContent;
	}
	public void setsContent(String sContent) {
		this.sContent = sContent;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getIsAllDay() {
		return isAllDay;
	}
	public void setIsAllDay(String isAllDay) {
		this.isAllDay = isAllDay;
	}
	public String getCreateEmp() {
		return createEmp;
	}
	public void setCreateEmp(String createEmp) {
		this.createEmp = createEmp;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
}
