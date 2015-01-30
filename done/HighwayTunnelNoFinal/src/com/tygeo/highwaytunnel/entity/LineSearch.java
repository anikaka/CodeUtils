package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;

public class LineSearch implements Serializable {
	String section_name;
	String section_id;
	String father_id;
	int _id;
	String tempfather_name;

	public String getTempfather_name() {
		return tempfather_name;
	}

	public void setTempfather_name(String tempfather_name) {
		this.tempfather_name = tempfather_name;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public String getFather_id() {
		return father_id;
	}

	public void setFather_id(String father_id) {
		this.father_id = father_id;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

}
