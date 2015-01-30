package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;

import android.R.integer;

public class Task implements Serializable {
	private Integer _id;
	private String task_name;
	private String check_date;
	private String begin_num;
	private String end_num;
	private String up_num;
	private String down_num;
	private String check_type;
	private String check_member;
	private String up_check_direciton;
	private int is_update;
	public int getIs_update() {
		return is_update;
	}

	public void setIs_update(int is_update) {
		this.is_update = is_update;
	}

	private String down_check_direciton;
	private String mainte_org;
	private String weather;
	private Integer temperature;
	private Integer humidity;
	private Integer picture_beginnum;
	private String patrol_car;
	private String operating_car;
	private String civil_check;
	private String tacilitiy_check;
	private String update_id;

	public String getUpdate_id() {
		return update_id;
	}

	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	private String check_head;
	private String titile;

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getCheck_date() {
		return check_date;
	}

	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}

	public String getBegin_num() {
		return begin_num;
	}

	public void setBegin_num(String begin_num) {
		this.begin_num = begin_num;
	}

	public String getEnd_num() {
		return end_num;
	}

	public void setEnd_num(String end_num) {
		this.end_num = end_num;
	}

	public String getCheck_type() {
		return check_type;
	}

	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}

	public String getCheck_member() {
		return check_member;
	}

	public void setCheck_member(String check_member) {
		this.check_member = check_member;
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

	public String getUp_check_direciton() {
		return up_check_direciton;
	}

	public void setUp_check_direciton(String up_check_direciton) {
		this.up_check_direciton = up_check_direciton;
	}

	public String getDown_check_direciton() {
		return down_check_direciton;
	}

	public void setDown_check_direciton(String down_check_direciton) {
		this.down_check_direciton = down_check_direciton;
	}

	public String getMainte_org() {
		return mainte_org;
	}

	public void setMainte_org(String mainte_org) {
		this.mainte_org = mainte_org;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	public Integer getPicture_beginnum() {
		return picture_beginnum;
	}

	public void setPicture_beginnum(Integer picture_beginnum) {
		this.picture_beginnum = picture_beginnum;
	}

	public String getPatrol_car() {
		return patrol_car;
	}

	public void setPatrol_car(String patrol_car) {
		this.patrol_car = patrol_car;
	}

	public String getOperating_car() {
		return operating_car;
	}

	public void setOperating_car(String operating_car) {
		this.operating_car = operating_car;
	}

	public String getCivil_check() {
		return civil_check;
	}

	public void setCivil_check(String civil_check) {
		this.civil_check = civil_check;
	}

	public String getTacilitiy_check() {
		return tacilitiy_check;
	}

	public void setTacilitiy_check(String tacilitiy_check) {
		this.tacilitiy_check = tacilitiy_check;
	}

	public String getCheck_head() {
		return check_head;
	}

	public void setCheck_head(String check_head) {
		this.check_head = check_head;
	}

}
