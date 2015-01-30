package com.tygeo.highwaytunnel.entity;

import android.graphics.Bitmap;

public class newphotoentity {
	private String photourl	;
	private String photo_type;
	int flag;
	int index;
	public int getIndex() {
		return index;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPhoto_type() {
		return photo_type;
	}
	public void setPhoto_type(String photo_type) {
		this.photo_type = photo_type;
	}
	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}
	public String getPhotoname() {
		return photoname;
	}
	public void setPhotoname(String photoname) {
		this.photoname = photoname;
	}
	private String photoname;
}
