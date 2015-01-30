package com.tongyan.widget.view;

import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.common.entities._LocRisk;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
/**
 * 
 * @ClassName ImageButton 
 * @author wanghb
 * @date 2013-8-12 am 09:25:40
 * @desc TODO
 */
public class DefineButton extends Button {
	
	private String imagePath;
	
	private String $id;
	
	private String $rowId;
	
	private _HolefaceSettingRecord mRecord;
	private _HolefaceSetting mSetting;
	
	private String mClassType;
	private String mClassTypeNum;
	
	private _LocRisk mRisk;
	
	public DefineButton(Context context) {
		super(context);
	}
	public DefineButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DefineButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void set$id(String $id) {
		this.$id = $id;
	}
	public String get$id() {
		return $id;
	}
	public void set$rowId(String $rowId) {
		this.$rowId = $rowId;
	}
	public String get$rowId() {
		return $rowId;
	}
	public void setmRecord(_HolefaceSettingRecord mRecord) {
		this.mRecord = mRecord;
	}
	public _HolefaceSettingRecord getmRecord() {
		return mRecord;
	}
	public void setmClassType(String mClassType) {
		this.mClassType = mClassType;
	}
	public String getmClassType() {
		return mClassType;
	}
	public void setmRisk(_LocRisk mRisk) {
		this.mRisk = mRisk;
	}
	public _LocRisk getmRisk() {
		return mRisk;
	}
	public void setmClassTypeNum(String mClassTypeNum) {
		this.mClassTypeNum = mClassTypeNum;
	}
	public String getmClassTypeNum() {
		return mClassTypeNum;
	}
	public void setmSetting(_HolefaceSetting mSetting) {
		this.mSetting = mSetting;
	}
	public _HolefaceSetting getmSetting() {
		return mSetting;
	}
}
