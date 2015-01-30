package com.tongyan.common.entities;
/**
 * 
 * @ClassName _LocalMsg 
 * @author wanghb
 * @date 2013-8-21 pm 04:12:20
 * @desc TODO
 */
public class _LocalMsg {
	
	private int _id;
	private String usrid;
	private String username;
	private String passwrod;
	private String params;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswrod() {
		return passwrod;
	}
	public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public void setUsrid(String usrid) {
		this.usrid = usrid;
	}
	public String getUsrid() {
		return usrid;
	}
}
