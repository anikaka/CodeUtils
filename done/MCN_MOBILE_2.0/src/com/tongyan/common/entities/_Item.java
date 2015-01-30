package com.tongyan.common.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName _Item 
 * @author wanghb
 * @date 2013-7-30 上午09:26:46
 * @desc 项目
 */
public class _Item {
	private String id;//项目主键
	private String text;//项目内容
	private String attributes;//{"t":"1"}
	
	private List<_Section> secList = new ArrayList<_Section>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public void setSecList(List<_Section> secList) {
		this.secList = secList;
	}
	public List<_Section> getSecList() {
		return secList;
	}
	
}
