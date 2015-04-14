package com.tongyan.zhengzhou.common.entities;

public class MetroSectionDetail {

	private String guidKey;
	private int idKey; //区段细节Code
	private int  tunnelType;
	private int segmentType;//管片类型
	private double circleWidth;//环宽
	private int    circleNum;//  环数
	private double tunnelDiameter;//隧道内径
	private int  firstStationCode;// 第一环所在站 关联站点表
	private int  segmentsDirection;// 管片朝向
	private String  startMileage;//开始里程
	private String  endMileage;//终止里程
	private int hoopBolt;// 环向螺栓
	private int endwiseBolt;// 纵向螺栓
	private int trackbedType;//道床类型
	private int sectionId;//所属区间
	private int mileageAddDirection;// 里程增加方向
	private int sectionDirection;// 区段方向 1为上行，0为下行
	private String updateTime;
	private int gullType;//
	private int parentId;//
	private String remark;//备注
	private String createTime;
	private String createBy;
	private String lastUpdateTime;
	private String LastUpdateBy;
	private int parentLevel;//父级节点所处等级 
	private String arenosolArea;
	private String overproofArea;
	private String damagedArea;
	private String checkObjectDetailCode;
	private int  startCircle;
	private int endCircle;
	private String arenosolScope;//砂性土涉及范围
	private String sedimentationOrDislocation;// 沉降/变形突变段
	private String structuralDamage;//结构受损区段
	private String  monitorScope; //监护项目影响范围
	private String  monitorArea; //监测项目对应范围
	private int checkFrequency;//区间,车站,风井查频率 
	public int getIdKey() {
		return idKey;
	}
	public void setIdKey(int idKey) {
		this.idKey = idKey;
	}
	public int getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(int tunnelType) {
		this.tunnelType = tunnelType;
	}
	public int getSegmentType() {
		return segmentType;
	}
	public void setSegmentType(int segmentType) {
		this.segmentType = segmentType;
	}
	public double getCircleWidth() {
		return circleWidth;
	}
	public void setCircleWidth(double circleWidth) {
		this.circleWidth = circleWidth;
	}
	public int getCircleNum() {
		return circleNum;
	}
	public void setCircleNum(int circleNum) {
		this.circleNum = circleNum;
	}
	public double getTunnelDiameter() {
		return tunnelDiameter;
	}
	public void setTunnelDiameter(double tunnelDiameter) {
		this.tunnelDiameter = tunnelDiameter;
	}
	public int getFirstStationCode() {
		return firstStationCode;
	}
	public void setFirstStationCode(int firstStationCode) {
		this.firstStationCode = firstStationCode;
	}
	public int getSegmentsDirection() {
		return segmentsDirection;
	}
	public void setSegmentsDirection(int segmentsDirection) {
		this.segmentsDirection = segmentsDirection;
	}
	public String getStartMileage() {
		return startMileage;
	}
	public void setStartMileage(String startMileage) {
		this.startMileage = startMileage;
	}
	public String getEndMileage() {
		return endMileage;
	}
	public void setEndMileage(String endMileage) {
		this.endMileage = endMileage;
	}
	public int getHoopBolt() {
		return hoopBolt;
	}
	public void setHoopBolt(int hoopBolt) {
		this.hoopBolt = hoopBolt;
	}
	public int getEndwiseBolt() {
		return endwiseBolt;
	}
	public void setEndwiseBolt(int endwiseBolt) {
		this.endwiseBolt = endwiseBolt;
	}
	public int getTrackbedType() {
		return trackbedType;
	}
	public void setTrackbedType(int trackbedType) {
		this.trackbedType = trackbedType;
	}
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public int getMileageAddDirection() {
		return mileageAddDirection;
	}
	public void setMileageAddDirection(int mileageAddDirection) {
		this.mileageAddDirection = mileageAddDirection;
	}
	public int getSectionDirection() {
		return sectionDirection;
	}
	public void setSectionDirection(int sectionDirection) {
		this.sectionDirection = sectionDirection;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getGullType() {
		return gullType;
	}
	public void setGullType(int gullType) {
		this.gullType = gullType;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLastUpdateBy() {
		return LastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		LastUpdateBy = lastUpdateBy;
	}
	public int getParentLevel() {
		return parentLevel;
	}
	public void setParentLevel(int parentLevel) {
		this.parentLevel = parentLevel;
	}
	public String getArenosolArea() {
		return arenosolArea;
	}
	public void setArenosolArea(String arenosolArea) {
		this.arenosolArea = arenosolArea;
	}
	public String getOverproofArea() {
		return overproofArea;
	}
	public void setOverproofArea(String overproofArea) {
		this.overproofArea = overproofArea;
	}
	public String getDamagedArea() {
		return damagedArea;
	}
	public void setDamagedArea(String damagedArea) {
		this.damagedArea = damagedArea;
	}
	public String getCheckObjectDetailCode() {
		return checkObjectDetailCode;
	}
	public void setCheckObjectDetailCode(String checkObjectDetailCode) {
		this.checkObjectDetailCode = checkObjectDetailCode;
	}
	public int getStartCircle() {
		return startCircle;
	}
	public void setStartCircle(int startCircle) {
		this.startCircle = startCircle;
	}
	public int getEndCircle() {
		return endCircle;
	}
	public void setEndCircle(int endCircle) {
		this.endCircle = endCircle;
	}
	public String getArenosolScope() {
		return arenosolScope;
	}
	public void setArenosolScope(String arenosolScope) {
		this.arenosolScope = arenosolScope;
	}
	public String getSedimentationOrDislocation() {
		return sedimentationOrDislocation;
	}
	public void setSedimentationOrDislocation(String sedimentationOrDislocation) {
		this.sedimentationOrDislocation = sedimentationOrDislocation;
	}
	public String getStructuralDamage() {
		return structuralDamage;
	}
	public void setStructuralDamage(String structuralDamage) {
		this.structuralDamage = structuralDamage;
	}
	public String getMonitorScope() {
		return monitorScope;
	}
	public void setMonitorScope(String monitorScope) {
		this.monitorScope = monitorScope;
	}
	public String getMonitorArea() {
		return monitorArea;
	}
	public void setMonitorArea(String monitorArea) {
		this.monitorArea = monitorArea;
	}
	public int getCheckFrequency() {
		return checkFrequency;
	}
	public void setCheckFrequency(int checkFrequency) {
		this.checkFrequency = checkFrequency;
	}
	
	
	public String getGuidKey() {
		return guidKey;
	}
	public void setGuidKey(String guidKey) {
		this.guidKey = guidKey;
	}
	@Override
	public String toString() {
		return "MetroSectionDetail [idKey=" + idKey + ", tunnelType="
				+ tunnelType + ", segmentType=" + segmentType
				+ ", circleWidth=" + circleWidth + ", circleNum=" + circleNum
				+ ", tunnelDiameter=" + tunnelDiameter + ", firstStationCode="
				+ firstStationCode + ", segmentsDirection=" + segmentsDirection
				+ ", startMileage=" + startMileage + ", endMileage="
				+ endMileage + ", hoopBolt=" + hoopBolt + ", endwiseBolt="
				+ endwiseBolt + ", trackbedType=" + trackbedType
				+ ", sectionId=" + sectionId + ", mileageAddDirection="
				+ mileageAddDirection + ", sectionDirection="
				+ sectionDirection + ", updateTime=" + updateTime
				+ ", gullType=" + gullType + ", parentId=" + parentId
				+ ", remark=" + remark + ", createTime=" + createTime
				+ ", createBy=" + createBy + ", lastUpdateTime="
				+ lastUpdateTime + ", LastUpdateBy=" + LastUpdateBy
				+ ", parentLevel=" + parentLevel + ", arenosolArea="
				+ arenosolArea + ", overproofArea=" + overproofArea
				+ ", damagedArea=" + damagedArea + ", checkObjectDetailCode="
				+ checkObjectDetailCode + ", startCircle=" + startCircle
				+ ", endCircle=" + endCircle + ", arenosolScope="
				+ arenosolScope + ", sedimentationOrDislocation="
				+ sedimentationOrDislocation + ", structuralDamage="
				+ structuralDamage + ", monitorScope=" + monitorScope
				+ ", monitorArea=" + monitorArea + ", checkFrequency="
				+ checkFrequency + "]";
	}
	
	
}
