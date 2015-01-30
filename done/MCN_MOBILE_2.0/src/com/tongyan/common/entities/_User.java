package com.tongyan.common.entities;


/**
 * 
 * @ClassName User 
 * @author wanghb
 * @date 2013-7-16 am 10:03:59
 * @desc TODO
 */
public class _User {
	private Integer id;
	private String userid;//人员id
	private String username;
	private String password;
	private String dptRowId;////部门id
	private String empLevel;//人员职务名称
	private String empName;//人员名称
	private String department;
	private String lasttime;
	private Integer savepassword;
	private Integer autologin;
	private String phone;
	private String nickname;
	private String birthday;
	private Integer sex;
	private String address;
	private String email;
	
	private Integer isUpdate;
	private Integer isGps;
	
	
	private String contactsTime;
	private String itemSecTime;
	private String projectTime;
	private String riskTypeTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDptRowId() {
		return dptRowId;
	}
	public void setDptRowId(String dptRowId) {
		this.dptRowId = dptRowId;
	}
	public String getEmpLevel() {
		return empLevel;
	}
	public void setEmpLevel(String empLevel) {
		this.empLevel = empLevel;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLasttime() {
		return lasttime;
	}
	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	public Integer getSavepassword() {
		return savepassword;
	}
	public void setSavepassword(Integer savepassword) {
		this.savepassword = savepassword;
	}
	public Integer getAutologin() {
		return autologin;
	}
	public void setAutologin(Integer autologin) {
		this.autologin = autologin;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}
	public Integer getIsUpdate() {
		return isUpdate;
	}
	public void setIsGps(Integer isGps) {
		this.isGps = isGps;
	}
	public Integer getIsGps() {
		return isGps;
	}
	public void setContactsTime(String contactsTime) {
		this.contactsTime = contactsTime;
	}
	public String getContactsTime() {
		return contactsTime;
	}
	public void setItemSecTime(String itemSecTime) {
		this.itemSecTime = itemSecTime;
	}
	public String getItemSecTime() {
		return itemSecTime;
	}
	public void setProjectTime(String projectTime) {
		this.projectTime = projectTime;
	}
	public String getProjectTime() {
		return projectTime;
	}
	public void setRiskTypeTime(String riskTypeTime) {
		this.riskTypeTime = riskTypeTime;
	}
	public String getRiskTypeTime() {
		return riskTypeTime;
	}
}
