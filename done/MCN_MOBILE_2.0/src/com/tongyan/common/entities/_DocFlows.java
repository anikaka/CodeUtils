package com.tongyan.common.entities;

import java.util.List;

/**
 * 
 * @ClassName _DocFlows 
 * @author wanghb
 * @date 2013-8-5 下午06:54:47
 * @desc TODO
 */
public class _DocFlows {
	
	private String flow_name;//流程名称
	
	private String flow_type;//流程类型
	
	private String flow_stime;//流程开始时间
	
	private String flow_status;//流程状态
	
	private List<_EmpsFlow> empsFlowsList;

	public String getFlow_name() {
		return flow_name;
	}

	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}

	public String getFlow_type() {
		return flow_type;
	}

	public void setFlow_type(String flow_type) {
		this.flow_type = flow_type;
	}

	public String getFlow_stime() {
		return flow_stime;
	}

	public void setFlow_stime(String flow_stime) {
		this.flow_stime = flow_stime;
	}

	public String getFlow_status() {
		return flow_status;
	}

	public void setFlow_status(String flow_status) {
		this.flow_status = flow_status;
	}

	public void setEmpsFlowsList(List<_EmpsFlow> empsFlowsList) {
		this.empsFlowsList = empsFlowsList;
	}

	public List<_EmpsFlow> getEmpsFlowsList() {
		return empsFlowsList;
	}

}
