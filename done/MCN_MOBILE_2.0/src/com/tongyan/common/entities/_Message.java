package com.tongyan.common.entities;

import java.io.Serializable;

/**
 * @ClassName Message 
 * @author wanghb
 * @date 2013-7-16 pm 12:04:29
 * @desc TODO
 */
public class _Message implements Serializable{
	private static final long serialVersionUID = 3275507679210239005L;
	private String rowId;//主键
	private String nType;//1:日程安排,2:代办公文,3:公告通知,4:
	private String nTitle;
	private String nDate;//发文时间
	private String nClass;
	
	private String nNo;
	private String nPublisher;//发布人
	private String nContent;//发布内容(HTML格式)
	private String nPath;//公告附件(为””时表示没有附件)
	private String nFileName;//附件名称
	private String mLink;
	
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getnType() {
		return nType;
	}
	public void setnType(String nType) {
		this.nType = nType;
	}
	public String getnTitle() {
		return nTitle;
	}
	public void setnTitle(String nTitle) {
		this.nTitle = nTitle;
	}
	public String getnDate() {
		return nDate;
	}
	public void setnDate(String nDate) {
		this.nDate = nDate;
	}
	public String getnNo() {
		return nNo;
	}
	public void setnNo(String nNo) {
		this.nNo = nNo;
	}
	public String getnPublisher() {
		return nPublisher;
	}
	public void setnPublisher(String nPublisher) {
		this.nPublisher = nPublisher;
	}
	public String getnContent() {
		return nContent;
	}
	public void setnContent(String nContent) {
		this.nContent = nContent;
	}
	public String getnPath() {
		return nPath;
	}
	public void setnPath(String nPath) {
		this.nPath = nPath;
	}
	public String getnFileName() {
		return nFileName;
	}
	public void setnFileName(String nFileName) {
		this.nFileName = nFileName;
	}
	public void setnClass(String nClass) {
		this.nClass = nClass;
	}
	public String getnClass() {
		return nClass;
	}
	public void setmLink(String mLink) {
		this.mLink = mLink;
	}
	public String getmLink() {
		return mLink;
	}
}
