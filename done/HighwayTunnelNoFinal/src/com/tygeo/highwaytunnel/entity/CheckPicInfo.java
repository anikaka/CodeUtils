package com.tygeo.highwaytunnel.entity;

import java.io.Serializable;

public class CheckPicInfo implements Serializable{

	 String PicName;//ͼƬ��ʵ����
	 String PicRealName;//ͼƬ��������
	public String getPicName() {
		return PicName;
	}
	public void setPicName(String picName) {
		PicName = picName;
	}
	public String getPicRealName() {
		return PicRealName;
	}
	public void setPicRealName(String picRealName) {
		PicRealName = picRealName;
	}
	
}
