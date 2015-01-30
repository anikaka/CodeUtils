package com.tongyan.common.entities;
/**
 * 
 * @ClassName EmpsFlow 
 * @author wanghb
 * @date 2013-8-5 pm 06:57:24
 * @desc TODO
 */
public class _EmpsFlow {
	
	private String emp_name;//人员姓名
	private String dpt_name;//部门
	private String deal_time;//批复时间
	private String suggestion;//批复意见
	
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getDpt_name() {
		return dpt_name;
	}
	public void setDpt_name(String dpt_name) {
		this.dpt_name = dpt_name;
	}
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	
}
