package com.tongyan.common.entities;
/**
 * 
 * @ClassName _RiskNotice.java
 * @Author wanghb
 * @Date 2013-9-11 am  09:40:29
 * @Desc TODO
 */
public class _RiskNotice {
	//row_id:主键, list_id:风险检查id，a_date:检查日期, a_position_t:风险标题, a_risk_event:风险事件, a_check_item：检查项 a_alarm_class:风险等级
	private String row_id;
	private String aDate;
	private String sectionName;
	private String unitName;
	private String alarmClass;
	private String checker;
	
	public String getRow_id() {
		return row_id;
	}
	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}
	public String getaDate() {
		return aDate;
	}
	public void setaDate(String aDate) {
		this.aDate = aDate;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getAlarmClass() {
		return alarmClass;
	}
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	
}
