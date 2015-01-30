package com.tygeo.highwaytunnel.entity;

public class CheckinfoE {
	private int _id;
	private String name;
	private String check_id;
	private String type;
	private String Leader_UnitCode;
	public String getLeader_UnitCode() {
		return Leader_UnitCode;
	}
	
	public void setLeader_UnitCode(String leader_UnitCode) {
		Leader_UnitCode = leader_UnitCode;
	}

	private String ManangerUnitName;
	private int  UnitCode;
	private String RoadCodeList;
	
	
	
	
	public String getManangerUnitName() {
		return ManangerUnitName;
	}

	public void setManangerUnitName(String manangerUnitName) {
		ManangerUnitName = manangerUnitName;
	}

	public int getUnitCode() {
		return UnitCode;
	}

	public void setUnitCode(int unitCode) {
		UnitCode = unitCode;
	}

	public String getRoadCodeList() {
		return RoadCodeList;
	}

	public void setRoadCodeList(String roadCodeList) {
		RoadCodeList = roadCodeList;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheck_id() {
		return check_id;
	}

	public void setCheck_id(String check_id) {
		this.check_id = check_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
