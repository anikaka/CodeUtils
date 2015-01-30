package com.tongyan.common.entities;
/**
 * 
 * @ClassName _GpsEmp 
 * @author wanghb
 * @date 2013-7-31 pm 08:26:41
 * @desc TODO
 */
public class _GpsEmp {
	private String empName;//人员姓名
	private String dptName;//单位名称
	private String empLevel;//职务
	private String empContact;//联系方式
	private String lng;//经度
	private String lat;//lat
	private String gpsDate;//gps时间
	
	private String empId;
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDptName() {
		return dptName;
	}
	public void setDptName(String dptName) {
		this.dptName = dptName;
	}
	public String getEmpLevel() {
		return empLevel;
	}
	public void setEmpLevel(String empLevel) {
		this.empLevel = empLevel;
	}
	public String getEmpContact() {
		return empContact;
	}
	public void setEmpContact(String empContact) {
		this.empContact = empContact;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getGpsDate() {
		return gpsDate;
	}
	public void setGpsDate(String gpsDate) {
		this.gpsDate = gpsDate;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpId() {
		return empId;
	}
}
