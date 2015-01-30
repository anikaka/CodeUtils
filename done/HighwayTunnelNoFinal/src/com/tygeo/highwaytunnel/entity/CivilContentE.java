package com.tygeo.highwaytunnel.entity;

import android.R.integer;

public class CivilContentE {
	Integer _id;
	String check_data;
	String check_position;
	String mileage;
	String judge_level;
	String level_content;
	String bh_pic;
	String pic_id;
	String belong_pro;
	String BZ;
	String BHID;

	public String getBHID() {
		return BHID;
	}

	public void setBHID(String bHID) {
		BHID = bHID;
	}

	public String getBZ() {
		return BZ;
	}

	public void setBZ(String bZ) {
		BZ = bZ;
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getCheck_data() {
		return check_data;
	}

	public void setCheck_data(String check_data) {
		this.check_data = check_data;
	}

	public String getCheck_position() {
		return check_position;
	}

	public void setCheck_position(String check_position) {
		this.check_position = check_position;
	}

	public String getMileage () {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getJudge_level() {
		return judge_level;
	}

	public void setJudge_level(String judge_level) {
		this.judge_level = judge_level;
	}

	public String getLevel_content() {
		return level_content;
	}

	public void setLevel_content(String level_content) {
		this.level_content = level_content;
	}

	public String getBh_pic() {
		return bh_pic;
	}

	public void setBh_pic(String bh_pic) {
		this.bh_pic = bh_pic;
	
	}

	public String getPic_id() {
		return pic_id;
	}

	public void setPic_id(String pic_id) {

		this.pic_id = pic_id;
	}

	public String getBelong_pro() {
		return belong_pro;
	}

	public void setBelong_pro(String belong_pro) {
		this.belong_pro = belong_pro;
	}

}
