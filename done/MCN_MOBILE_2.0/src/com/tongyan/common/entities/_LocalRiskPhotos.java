package com.tongyan.common.entities;
/**
 * 
 * @ClassName _LocalRiskPhotos.java
 * @Author wanghb
 * @Date 2013-9-6下午02:55:46
 * @Desc 由于_LocalPhotos 和 _LocalRiskPhotos 外键不一样
 */
public class _LocalRiskPhotos {
	
	private String id;
	private String holeface_tab_id;
	private String riskUUID;
	private String local_img_path;
	private String remote_img_path;
	private String remote_img_id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHoleface_tab_id() {
		return holeface_tab_id;
	}
	public void setHoleface_tab_id(String holeface_tab_id) {
		this.holeface_tab_id = holeface_tab_id;
	}
	public String getRiskUUID() {
		return riskUUID;
	}
	public void setRiskUUID(String riskUUID) {
		this.riskUUID = riskUUID;
	}
	public String getLocal_img_path() {
		return local_img_path;
	}
	public void setLocal_img_path(String local_img_path) {
		this.local_img_path = local_img_path;
	}
	public String getRemote_img_path() {
		return remote_img_path;
	}
	public void setRemote_img_path(String remote_img_path) {
		this.remote_img_path = remote_img_path;
	}
	public String getRemote_img_id() {
		return remote_img_id;
	}
	public void setRemote_img_id(String remote_img_id) {
		this.remote_img_id = remote_img_id;
	}
	
	
}
