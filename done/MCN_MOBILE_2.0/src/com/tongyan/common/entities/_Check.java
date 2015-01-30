package com.tongyan.common.entities;

import java.io.Serializable;

/**
 * 
 * @ClassName _Check 
 * @author wanghb
 * @date 2013-8-12 pm 04:45:17
 * @desc TODO
 */
public class _Check implements Serializable{
	
	private static final long serialVersionUID = 3539100796970114476L;
	private Integer id;
	private String isUpdate;
	private String aSecName;
	private String aItemName;
	private String aSecId;
	private String aItemId;
	private String aProjectId;
	private String checkContent;
	private String upTime;
	private String inTime;
	private String aStartMile;
	private String aEndMile;
	private String aProName;
	private String checkId;
	
	public String getaSecName() {
		return aSecName;
	}
	public void setaSecName(String aSecName) {
		this.aSecName = aSecName;
	}
	public String getaItemName() {
		return aItemName;
	}
	public void setaItemName(String aItemName) {
		this.aItemName = aItemName;
	}
	public String getaSecId() {
		return aSecId;
	}
	public void setaSecId(String aSecId) {
		this.aSecId = aSecId;
	}
	public String getaItemId() {
		return aItemId;
	}
	public void setaItemId(String aItemId) {
		this.aItemId = aItemId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getCheckContent() {
		return checkContent;
	}
	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	public String getaStartMile() {
		return aStartMile;
	}
	public void setaStartMile(String aStartMile) {
		this.aStartMile = aStartMile;
	}
	public String getaEndMile() {
		return aEndMile;
	}
	public void setaEndMile(String aEndMile) {
		this.aEndMile = aEndMile;
	}
	public void setaProName(String aProName) {
		this.aProName = aProName;
	}
	public String getaProName() {
		return aProName;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getCheckId() {
		return checkId;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getInTime() {
		return inTime;
	}
	public void setaProjectId(String aProjectId) {
		this.aProjectId = aProjectId;
	}
	public String getaProjectId() {
		return aProjectId;
	}
	
}
