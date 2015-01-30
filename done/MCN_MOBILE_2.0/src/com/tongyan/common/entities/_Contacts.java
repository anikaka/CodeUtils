package com.tongyan.common.entities;

import java.util.ArrayList;

/**
 * 
 * @ClassName _Contacts 
 * @author wanghb
 * @date 2013-7-18 am 10:33:05
 * @desc TODO
 */
public class _Contacts {
	
	private String dptName;
	private String dptTel;
	private String dptFax;
	private String dptNamePinyin;
	private ArrayList<_ContactsEmp> empList = new ArrayList<_ContactsEmp>();
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
	public void setEmpList(ArrayList<_ContactsEmp> empList) {
		this.empList = empList;
	}
	public ArrayList<_ContactsEmp> getEmpList() {
		return empList;
	}
	public void setDptNamePinyin(String dptNamePinyin) {
		this.dptNamePinyin = dptNamePinyin;
	}
	public String getDptNamePinyin() {
		return dptNamePinyin;
	}
	
}
