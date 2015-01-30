package com.tongyan.common.entities;

import java.util.List;

public class _RiskInfo {
	
	private String oneType;
	private String twoType;
	private String riskContent;
	private List<_RiskInfo> mList;
	
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
	public String getRiskContent() {
		return riskContent;
	}
	public void setRiskContent(String riskContent) {
		this.riskContent = riskContent;
	}
	public List<_RiskInfo> getmList() {
		return mList;
	}
	public void setmList(List<_RiskInfo> mList) {
		this.mList = mList;
	}
	
}
