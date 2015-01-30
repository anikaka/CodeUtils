package com.tongyan.common.entities;//

import java.util.List;

/**
 * 
 * @ClassName _Document 
 * @author wanghb
 * @date 2013-8-5 am 09;//35;//55
 * @desc TODO
 */
public class _Document {
	
	private String row_id;// 主键
	private String d_title;// 标题
	private String d_num;// 
	private String d_code;// 文号
	private String booker;// 拟稿人(发文),登记人（收文）
	private String d_content;
	private String d_sendtime;//发文时间
	private String d_department;//来文/发文单位

	private String flow_name;//当前流程
	private String step_type;//流程类型
	private String flow_id;//流程id
	private String d_class;
	private String nPath;//附件地址(“”表示没有附件)
	private String nFileName;//附件名称
	
	private String step_id;//当前流程步骤id 
	
	private List<_DocFlows> docFlows;

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public String getD_title() {
		return d_title;
	}

	public void setD_title(String d_title) {
		this.d_title = d_title;
	}

	public String getD_num() {
		return d_num;
	}

	public void setD_num(String d_num) {
		this.d_num = d_num;
	}

	public String getD_code() {
		return d_code;
	}

	public void setD_code(String d_code) {
		this.d_code = d_code;
	}

	public String getBooker() {
		return booker;
	}

	public void setBooker(String booker) {
		this.booker = booker;
	}

	public String getD_content() {
		return d_content;
	}

	public void setD_content(String d_content) {
		this.d_content = d_content;
	}

	public String getD_sendtime() {
		return d_sendtime;
	}

	public void setD_sendtime(String d_sendtime) {
		this.d_sendtime = d_sendtime;
	}

	public String getD_department() {
		return d_department;
	}

	public void setD_department(String d_department) {
		this.d_department = d_department;
	}

	public String getFlow_name() {
		return flow_name;
	}

	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}

	public String getStep_type() {
		return step_type;
	}

	public void setStep_type(String step_type) {
		this.step_type = step_type;
	}

	public String getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	public String getD_class() {
		return d_class;
	}

	public void setD_class(String d_class) {
		this.d_class = d_class;
	}

	public String getnPath() {
		return nPath;
	}

	public void setnPath(String nPath) {
		this.nPath = nPath;
	}

	public String getnFileName() {
		return nFileName;
	}

	public void setnFileName(String nFileName) {
		this.nFileName = nFileName;
	}

	public List<_DocFlows> getDocFlows() {
		return docFlows;
	}

	public void setDocFlows(List<_DocFlows> docFlows) {
		this.docFlows = docFlows;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public String getStep_id() {
		return step_id;
	}
	
}
