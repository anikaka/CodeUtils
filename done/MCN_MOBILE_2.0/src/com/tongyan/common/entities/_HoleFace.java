package com.tongyan.common.entities;
/**
 * 
 * @ClassName _HoloFace.java
 * @Author wanghb
 * @Date 2013-9-3 pm 04:39:13
 * @Desc TODO
 */
public class _HoleFace {
	
	private String _id;
	private String riskId;
	private String isFinish;//1:isFinish,0:noFinish
	private String hole;
	private String currMile;
	private String riskDegree;
	private String proposeDegree;
	private String riskHSuggest;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	public String getHole() {
		return hole;
	}
	public void setHole(String hole) {
		this.hole = hole;
	}
	public String getCurrMile() {
		return currMile;
	}
	public void setCurrMile(String currMile) {
		this.currMile = currMile;
	}
	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}
	public String getRiskId() {
		return riskId;
	}
	public String getRiskDegree() {
		return riskDegree;
	}
	public void setRiskDegree(String riskDegree) {
		this.riskDegree = riskDegree;
	}
	public String getProposeDegree() {
		return proposeDegree;
	}
	public void setProposeDegree(String proposeDegree) {
		this.proposeDegree = proposeDegree;
	}
	public String getRiskHSuggest() {
		return riskHSuggest;
	}
	public void setRiskHSuggest(String riskHSuggest) {
		this.riskHSuggest = riskHSuggest;
	}
	
}
