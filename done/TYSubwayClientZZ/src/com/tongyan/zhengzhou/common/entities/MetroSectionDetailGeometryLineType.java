package com.tongyan.zhengzhou.common.entities;

public class MetroSectionDetailGeometryLineType {
 
	private int id;
	private int sectionDetailId;//所属区间上下行ID
	private  int geoLineType; //线型类型,1.曲线,2.缓曲线,3.变曲点,4.坡度,5.变坡点
	private  int subType; //变曲点：1.ZH,2.HY,3.YH,4.HZ,5.ZY,6,YZ  坡度：1.上坡,2.下坡 变坡点:1.ZY,2.YZ
	private int typeLevel;
	private int parentId; 
	private int startCircle;
	private int endCircle;
	private String  startMileage;
	private String  endMileage;
	private String  superelevation;//只有1.曲线才有值
	private String curveRadius;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSectionDetailId() {
		return sectionDetailId;
	}
	public void setSectionDetailId(int sectionDetailId) {
		this.sectionDetailId = sectionDetailId;
	}
	public int getGeoLineType() {
		return geoLineType;
	}
	public void setGeoLineType(int geoLineType) {
		this.geoLineType = geoLineType;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	public int getTypeLevel() {
		return typeLevel;
	}
	public void setTypeLevel(int typeLevel) {
		this.typeLevel = typeLevel;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getStartCircle() {
		return startCircle;
	}
	public void setStartCircle(int startCircle) {
		this.startCircle = startCircle;
	}
	public int getEndCircle() {
		return endCircle;
	}
	public void setEndCircle(int endCircle) {
		this.endCircle = endCircle;
	}
	public String getStartMileage() {
		return startMileage;
	}
	public void setStartMileage(String startMileage) {
		this.startMileage = startMileage;
	}
	public String getEndMileage() {
		return endMileage;
	}
	public void setEndMileage(String endMileage) {
		this.endMileage = endMileage;
	}
	public String getSuperelevation() {
		return superelevation;
	}
	public void setSuperelevation(String superelevation) {
		this.superelevation = superelevation;
	}
	public String getCurveRadius() {
		return curveRadius;
	}
	public void setCurveRadius(String curveRadius) {
		this.curveRadius = curveRadius;
	}
	
	@Override
	public String toString() {
		return "MetroSectionDetailGeometryLineType [id=" + id
				+ ", sectionDetailId=" + sectionDetailId + ", geoLineType="
				+ geoLineType + ", subType=" + subType + ", typeLevel="
				+ typeLevel + ", parentId=" + parentId + ", startCircle="
				+ startCircle + ", endCircle=" + endCircle + ", startMileage="
				+ startMileage + ", endMileage=" + endMileage
				+ ", superelevation=" + superelevation + ", curveRadius="
				+ curveRadius + "]";
	}
	
}
