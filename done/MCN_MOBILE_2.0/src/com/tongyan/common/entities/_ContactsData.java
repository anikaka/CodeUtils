package com.tongyan.common.entities;

import java.io.Serializable;

/**
 * 
 * @ClassName _ContactsData 
 * @author wanghb
 * @date 2013-7-18 pm 08:07:21
 * @desc TODO
 */
public class _ContactsData implements Serializable{
	
	private static final long serialVersionUID = -6494031110374745642L;
	
	private String $id;
	private String dptId;
	private String dptName;
	private String dptTel;
	private String dptFax;
	private String empName;
	private String empUser;
	private String empContact;
	private String empPinyin;
	private String dptPinyin;
	private String rowId;
	private String roleName;
	
	public String getDptName() {
		return dptName;
	}
	public void setDptName(String dptName) {
		this.dptName = dptName;
	}
	public String getDptTel() {
		return dptTel;
	}
	public void setDptTel(String dptTel) {
		this.dptTel = dptTel;
	}
	public String getDptFax() {
		return dptFax;
	}
	public void setDptFax(String dptFax) {
		this.dptFax = dptFax;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpContact() {
		return empContact;
	}
	public void setEmpContact(String empContact) {
		this.empContact = empContact;
	}
	public String getEmpPinyin() {
		return empPinyin;
	}
	public void setEmpPinyin(String empPinyin) {
		this.empPinyin = empPinyin;
	}
	public String getDptPinyin() {
		return dptPinyin;
	}
	public void setDptPinyin(String dptPinyin) {
		this.dptPinyin = dptPinyin;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getRowId() {
		return rowId;
	}
	public void set$id(String $id) {
		this.$id = $id;
	}
	public String get$id() {
		return $id;
	}
	public void setDptId(String dptId) {
		this.dptId = dptId;
	}
	public String getDptId() {
		return dptId;
	}
	public void setEmpUser(String empUser) {
		this.empUser = empUser;
	}
	public String getEmpUser() {
		return empUser;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleName() {
		return roleName;
	}
	
}
