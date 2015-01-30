package com.tongyan.common.entities;
/**
 * 
 * @ClassName _ItemSec 
 * @author wanghb
 * @date 2013-8-9 am 09:46:30
 * @desc TODO
 */
public class _ItemSec {
	
	private String iid;//项目主键
	private String itext;//项目内容
	private String iattributes;//{"t":"1"}
	
	private String sid;//标段主键
	private String stext;//标段内容
	private String sattributes;//{"t":"1"}
	
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public String getIattributes() {
		return iattributes;
	}
	public void setIattributes(String iattributes) {
		this.iattributes = iattributes;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getStext() {
		return stext;
	}
	public void setStext(String stext) {
		this.stext = stext;
	}
	public String getSattributes() {
		return sattributes;
	}
	public void setSattributes(String sattributes) {
		this.sattributes = sattributes;
	}
	
}
