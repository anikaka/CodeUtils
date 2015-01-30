package com.tongyan.common.entities;

import java.util.List;

/**
 * 
 * @ClassName _HolefaceSetting.java
 * @Author wanghb
 * @Date 2013-9-4 pm 03:40:36
 * @Desc TODO
 */
public class _HolefaceSetting {
	
	private String $id;
	private String $cId;
	private String oneType;
	private String twoType;
	private String class1;
	private String class2;
	private String isChild;//0,1,2
	private String riskType;
	private String class2Tip;
	private List<_HolefaceSetting> mClildList;
	
	public String get$id() {
		return $id;
	}
	public void set$id(String $id) {
		this.$id = $id;
	}
	public String getOneType() {
		return oneType;
	}
	public void setOneType(String oneType) {
		this.oneType = oneType;
	}
	public String getTwoType() {
		return twoType;
	}
	public void setTwoType(String twoType) {
		this.twoType = twoType;
	}
	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	public String getClass2() {
		return class2;
	}
	public void setClass2(String class2) {
		this.class2 = class2;
	}
	public String get$cId() {
		return $cId;
	}
	public void set$cId(String $cId) {
		this.$cId = $cId;
	}
	public String getIsChild() {
		return isChild;
	}
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getClass2Tip() {
		return class2Tip;
	}
	public void setClass2Tip(String class2Tip) {
		this.class2Tip = class2Tip;
	}
	public List<_HolefaceSetting> getmClildList() {
		return mClildList;
	}
	public void setmClildList(List<_HolefaceSetting> mClildList) {
		this.mClildList = mClildList;
	}
}
