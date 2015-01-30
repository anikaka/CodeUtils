package com.tongyan.common.entities;
/**
 * @ClassName _HolefaceSettingInfo.java
 * @Author wanghb
 * @Date 2013-9-4 pm 07:05:12
 * @Desc TODO
 */
public class _HolefaceSettingInfo {
	
	private String $id;
	private String $holefaceId;
	private String $riskId;
	private String riskHoleName;
	private String isCheck;
	private String riskTypeName;
	
	public void setRiskHoleName(String riskHoleName) {
		this.riskHoleName = riskHoleName;
	}
	public String getRiskHoleName() {
		return riskHoleName;
	}
	public String get$id() {
		return $id;
	}
	public void set$id(String $id) {
		this.$id = $id;
	}
	public String get$holefaceId() {
		return $holefaceId;
	}
	public void set$holefaceId(String $holefaceId) {
		this.$holefaceId = $holefaceId;
	}
	public String get$riskId() {
		return $riskId;
	}
	public void set$riskId(String $riskId) {
		this.$riskId = $riskId;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public void setRiskTypeName(String riskTypeName) {
		this.riskTypeName = riskTypeName;
	}
	public String getRiskTypeName() {
		return riskTypeName;
	}
}
