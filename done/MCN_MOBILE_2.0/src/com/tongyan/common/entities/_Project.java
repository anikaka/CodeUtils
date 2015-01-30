package com.tongyan.common.entities;

import java.io.Serializable;

/**
 * 
 * @ClassName _Project 
 * @author wanghb
 * @date 2013-7-30 am 09:16:44
 * @desc 工程
 */
public class _Project implements Serializable{
	private static final long serialVersionUID = -5306404389765087115L;
	private String id;
	private String rowId;//主键
	private String aName;//工程名称
	private String aPid;//项目id
	private String aSecid;//标段id
	private String aType;//类型
	/*private String aSlng;//开始经度
	private String aSlat;//开始纬度
	private String aElng;//结束经度
	private String aElat;//结束纬度
*/	
	private String aPmName;//项目经理
	private String aPContract;//联系方式
	private String aConstruct;//施工单位
	private String aStartMile;//起始里程
	private String aEndMile;//结束里程
	
	private String aPosition;//位置(隧道位置) 类型是隧道时才用到
	private String aPositionMile;
	
	public String getaPmName() {
		return aPmName;
	}
	public void setaPmName(String aPmName) {
		this.aPmName = aPmName;
	}
	public String getaPContract() {
		return aPContract;
	}
	public void setaPContract(String aPContract) {
		this.aPContract = aPContract;
	}
	public String getaConstruct() {
		return aConstruct;
	}
	public void setaConstruct(String aConstruct) {
		this.aConstruct = aConstruct;
	}
	public String getaStartMile() {
		return aStartMile;
	}
	public void setaStartMile(String aStartMile) {
		this.aStartMile = aStartMile;
	}
	public String getaEndMile() {
		return aEndMile;
	}
	public void setaEndMile(String aEndMile) {
		this.aEndMile = aEndMile;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getaName() {
		return aName;
	}
	public void setaName(String aName) {
		this.aName = aName;
	}
	public String getaPid() {
		return aPid;
	}
	public void setaPid(String aPid) {
		this.aPid = aPid;
	}
	public String getaSecid() {
		return aSecid;
	}
	public void setaSecid(String aSecid) {
		this.aSecid = aSecid;
	}
	public String getaType() {
		return aType;
	}
	public void setaType(String aType) {
		this.aType = aType;
	}
	/*public String getaSlng() {
		return aSlng;
	}
	public void setaSlng(String aSlng) {
		this.aSlng = aSlng;
	}
	public String getaSlat() {
		return aSlat;
	}
	public void setaSlat(String aSlat) {
		this.aSlat = aSlat;
	}
	public String getaElng() {
		return aElng;
	}
	public void setaElng(String aElng) {
		this.aElng = aElng;
	}
	public String getaElat() {
		return aElat;
	}
	public void setaElat(String aElat) {
		this.aElat = aElat;
	}*/
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setaPosition(String aPosition) {
		this.aPosition = aPosition;
	}
	public String getaPosition() {
		return aPosition;
	}
	public void setaPositionMile(String aPositionMile) {
		this.aPositionMile = aPositionMile;
	}
	public String getaPositionMile() {
		return aPositionMile;
	}
}
