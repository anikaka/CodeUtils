package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;

import android.R.integer;

public class TunnelInfoE implements Serializable {

	private Integer _id;
	String line_id; // 线路编号
	String line_name;
	String section_id; // 隧道编号
	String section_name; // 隧道名称
	Double up_length; // 上行长度

	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}

	Double down_length; // 下行长度
	String up_num; // 上行桩号
	String down_num; // 下行桩号
	String completion_time;// 完成时间

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer integer) {
		this._id = integer;
	}

	public String getLine_id() {
		return line_id;
	}

	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public Double getUp_length() {
		return up_length;
	}

	public void setUp_length(Double integer) {
		this.up_length = integer;
	}

	public Double getDown_length() {
		return down_length;
	}

	public void setDown_length(Double down_length) {
		this.down_length = down_length;
	}

	public String getUp_num() {
		return up_num;
	}

	public void setUp_num(String up_num) {
		this.up_num = up_num;
	}

	public String getDown_num() {
		return down_num;
	}

	public void setDown_num(String down_num) {
		this.down_num = down_num;
	}

	public String getCompletion_time() {
		return completion_time;
	}

	public void setCompletion_time(String completion_time) {
		this.completion_time = completion_time;
	}

}
