package com.tongyan.common.entities;

import java.util.List;
/**
 * 
 * @ClassName _ProjectLoc.java
 * @Author wanghb
 * @Date 2013-9-25 pm 08:25:19
 * @Desc TODO
 */
public class _ProjectLoc {
	private String projectName;
	private String projectId;
	private List<_LocInfo> mLocInfoList;
	
	private String mCenterLat;
	private String mCenterLng;
	
	public String getmCenterLat() {
		return mCenterLat;
	}
	public void setmCenterLat(String mCenterLat) {
		this.mCenterLat = mCenterLat;
	}
	public String getmCenterLng() {
		return mCenterLng;
	}
	public void setmCenterLng(String mCenterLng) {
		this.mCenterLng = mCenterLng;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<_LocInfo> getmLocInfoList() {
		return mLocInfoList;
	}
	public void setmLocInfoList(List<_LocInfo> mLocInfoList) {
		this.mLocInfoList = mLocInfoList;
	}
}

