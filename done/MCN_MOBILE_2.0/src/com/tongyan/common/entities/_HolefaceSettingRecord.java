package com.tongyan.common.entities;

import java.util.List;

/**
 * @ClassName _HolefaceSettingRecord.java
 * @Author wanghb
 * @Date 2013-9-5 am 10:02:39
 * @Desc TODO
 */
public class _HolefaceSettingRecord {
	
	private String $id;
	private String $riskId;
	private String $holefaceId;
	private String $holefaceSettingInfoId;
	private String $rowId;
	private String $subId;
	private String oneType;
	private String selectedType;
	private String typeNum;
	private String riskType;
	private String currentState;
	private List<_HolefaceSettingRecord> mHolefaceRecordList;
	
	public String get$id() {
		return $id;
	}
	public void set$id(String $id) {
		this.$id = $id;
	}
	public String get$riskId() {
		return $riskId;
	}
	public void set$riskId(String $riskId) {
		this.$riskId = $riskId;
	}
	public String get$holefaceId() {
		return $holefaceId;
	}
	public void set$holefaceId(String $holefaceId) {
		this.$holefaceId = $holefaceId;
	}
	public String get$holefaceSettingInfoId() {
		return $holefaceSettingInfoId;
	}
	public void set$holefaceSettingInfoId(String $holefaceSettingInfoId) {
		this.$holefaceSettingInfoId = $holefaceSettingInfoId;
	}
	public String get$rowId() {
		return $rowId;
	}
	public void set$rowId(String $rowId) {
		this.$rowId = $rowId;
	}
	public String get$subId() {
		return $subId;
	}
	public void set$subId(String $subId) {
		this.$subId = $subId;
	}
	public String getOneType() {
		return oneType;
	}
	public void setOneType(String oneType) {
		this.oneType = oneType;
	}
	public String getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}
	public String getTypeNum() {
		return typeNum;
	}
	public void setTypeNum(String typeNum) {
		this.typeNum = typeNum;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public void setmHolefaceRecordList(List<_HolefaceSettingRecord> mHolefaceRecordList) {
		this.mHolefaceRecordList = mHolefaceRecordList;
	}
	public List<_HolefaceSettingRecord> getmHolefaceRecordList() {
		return mHolefaceRecordList;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getCurrentState() {
		return currentState;
	}
}
