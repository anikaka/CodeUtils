package com.tygeo.highwaytunnel.entity;

import android.R.integer;

public class CivilCheckInfo {
	private integer _id;
	/*
	 * _id INTEGER, check_pro VARCHAR(50), check_content VARCHAR(50), s_level
	 * VARCHAR(200), a_level VARCHAR(200), b_level VARCHAR(200)
	 */
	private String check_pro;
	private String check_content;
	private String s_level;
	private String a_level;
	private String b_level;

	public integer get_id() {
		return _id;
	}

	public void set_id(integer _id) {
		this._id = _id;
	}

	public String getCheck_pro() {
		return check_pro;
	}

	public void setCheck_pro(String check_pro) {
		this.check_pro = check_pro;
	}

	public String getCheck_content() {
		return check_content;
	}

	public void setCheck_content(String check_content) {
		this.check_content = check_content;
	}

	public String getS_level() {
		return s_level;
	}

	public void setS_level(String s_level) {
		this.s_level = s_level;
	}

	public String getA_level() {
		return a_level;
	}

	public void setA_level(String a_level) {
		this.a_level = a_level;
	}

	public String getB_level() {
		return b_level;
	}

	public void setB_level(String b_level) {
		this.b_level = b_level;
	}

}
