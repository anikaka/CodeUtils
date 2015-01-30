package com.tongyan.common.entities;
/**
 * 
 * @ClassName _Section 
 * @author wanghb
 * @date 2013-7-30 am 09:20:50
 * @desc 标段
 */
public class _Section {
	
	private String iid;
	private String id;//标段主键
	private String text;//标段内容
	private String attributes;//{"t":"1"}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getIid() {
		return iid;
	}
}
