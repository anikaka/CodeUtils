package com.tongyan.yanan.act.subside;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.R.bool;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBHelp;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.NumberTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * @category 新增或修改界面
 * @author Administrator
 *  @version  YanAn 1.0
 */

public class SubsidePactSelectDataAddOrModify  extends FinalActivity  implements View.OnClickListener{

	@ViewInject (id=R.id.txtTile_layout_subside_pactselect_data_modify_data_look)
	TextView  mTxtTitle;
	
	@ViewInject(id = R.id.txtSupervise_layout_subside_pactselect_data_modifys_type_title_content)
	TextView mtxtSupervise_layout_subside_pactselect_data_modifys_type_title_content; // 检测类型
	
	@ViewInject(id = R.id.txtSupervise_layout_subside_pactselect_data_modify_content)
	TextView mTxtSuperviseContent; // 检测内容
	
	@ViewInject(id = R.id.editMonitorValue_layout_subside_pactselect_data_modify_change_values)
	EditText mEditMonitorValue_layout_subside_pactselect_data_modify_change_values; //  本次测值
	
	@ViewInject(id = R.id.editSuperviseDate_ayout_subside_pactselect_data_modify_date_content)
	EditText mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content; // 检测时间
	
	@ViewInject(id = R.id.txtUploadingUser_layout_subside_pactselect_data_modifyr_content)
	TextView mtxtUploadingUse_layout_subside_pactselect_data_modifyr_content;// 上传人
	
	@ViewInject (id=R.id.butSave_layout_subside_pactselect_data_modify,click="saveMonitorInfo")
	Button mButSave;
	//埋深
	@ViewInject(id=R.id.editMonitorDeep_layout_subside_pactselect_data_modify_change_values)
	EditText mEditMonitorDeep;
	
	private String mMonitorPointId;
	private String  mMonitorProjectTypeId;	
	private String  mMonitorProjectTypeName;
	private String  mMonitorTypeId;
	private String  mMonitorTypeName;
	private String  mPactId;
	private String mPactName;
	private String mMonitorName;
	private Context mContext=this;
	private String mMonitorValue;
	private String mUploadMark;
	private String  mModify;
	private String  mCander;
	private 	 int   mflag=0;
	private SharedPreferences  mSp;
	TimePickerDialog mTimeDialog;
	DatePickerDialog mDateDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_subside_pactselect_data_modify);
		mSp=PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		//去光标
		mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setCursorVisible(false);
		mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setInputType(InputType.TYPE_NULL);
		mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setCursorVisible(false);
		mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setOnTouchListener(editTouchListener);
		mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setOnClickListener(this);
		/*
		 * data.putString("monitorProjectTypeId", mTypeId); //检测类型Id就等于父类检测类型Id
		 * data.putString("monitorProjectTypeName", mMonitorProjectTypeName);
		 * data.putString("monitorTypeId", mTypeId); //监测类型ID
		 * data.putString("monitorTypeName", mMonitorTypeName); //监测类型名称
		 * data.putString("pactId", "mPactId"); //合同段ID
		 * data.putString("pactName", mPactName);//合同段名称
		 */
		Bundle  bundle =getIntent().getExtras();
		mMonitorProjectTypeId=bundle.getString("monitorProjectTypeId");
		mMonitorProjectTypeName=bundle.getString("monitorProjectTypeName");
		mMonitorTypeId=bundle.getString("monitorTypeId");
		mMonitorTypeName=bundle.getString("monitorTypeName");
		mPactId=bundle.getString("pactId");
		mPactName=bundle.getString("pactName");
		mMonitorName=bundle.getString("monitorName");
		mMonitorPointId=bundle.getString("monitorPointId");
		mMonitorValue=bundle.getString("monitorValue");
		mUploadMark=bundle.getString("uploadMark");
		mModify=bundle.getString("modify");
		//设置初始化数据
		mTxtTitle.setText(mMonitorName+"数据编辑");
		mtxtSupervise_layout_subside_pactselect_data_modifys_type_title_content.setText(mMonitorProjectTypeName);
		mTxtSuperviseContent.setText(mMonitorTypeName);
		mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setText(DateTools.getDate());
		  if("".equals(mMonitorValue) || mMonitorValue==null){
			  mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setText("");
		  }else{
			  mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setText(mMonitorValue);
			  ArrayList<HashMap<String, String>>	 mArrayList = new DBService(mContext).queryTableMonitorPointUpload(mMonitorPointId,mMonitorTypeId, mUploadMark);
			  if(mArrayList.size()>0){
		 			for(HashMap<String, String> map:mArrayList){
		 				mtxtSupervise_layout_subside_pactselect_data_modifys_type_title_content.setText(map.get("monitorProjectTypeName"));
		 				mTxtSuperviseContent.setText(map.get("monitorTypeName"));
		 				mTxtTitle.setText(map.get("monitorPointName")+"数据编辑");
		 				mEditMonitorDeep.setText(map.get("monitorDeep"));
		 			}
		 	}
		  }
		 if(getIntent().getExtras()!=null){
	
		 }
		  setLocation();
	}
	
	/** 保存按钮点击*/
	public void saveMonitorInfo(View view) {

		String mChangeNumber = mEditMonitorValue_layout_subside_pactselect_data_modify_change_values
				.getText().toString();
		String mSuperviseDate = mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content
				.getText().toString();
		String mUploadUser = mtxtUploadingUse_layout_subside_pactselect_data_modifyr_content
				.getText().toString();

		if (lookEditText()) {
			if (!"".equals(mModify) && mModify != null) {
				if ("1".equals(mModify)) {
					boolean updateResult = new DBService(mContext).updateUploadInfo(
							mMonitorTypeId, mUploadMark,
							mEditMonitorValue_layout_subside_pactselect_data_modify_change_values
									.getText().toString(),
							mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content
									.getText().toString());
					if (updateResult) {
						Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent();
						setResult(Constants.PAGE_BACK_SUbSIDEPACTSELECPOINTDATALIST);
						this.finish();
					} else {
						Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT)
								.show();
						this.finish();
					}
				}
			} else {
				// 插入数据表中
				boolean result = new DBService(mContext).insertTableMonitorPointUpload(
						mSp.getString(Constants.PREFERENCES_INFO_USERID, ""),
						mMonitorPointId,
						mMonitorName,
						mMonitorProjectTypeId,
						mMonitorProjectTypeName,
						mMonitorTypeId,
						mMonitorTypeName,
						mPactId,
						mPactName,
						mChangeNumber,
						mSuperviseDate,
						"",
						mUploadUser,
						"P" + NumberTools.getNumber() + "D"+ NumberTools.getNumber() + "L"+ NumberTools.getNumber(),
						mEditMonitorDeep.getText().toString());
				if (result) {
					sendFMessage(Constants.INSERT_TABLE_SUCCESS);
					Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
				}
				Intent intent = new Intent();
				setResult(Constants.PAGE_BACK_SUbSIDEPACTSELECPOINTDATALIST);
				this.finish();
			}
		}
	}
	 
	
	/**把输入框的光标移动到最后面 */
	private  void setLocation(){
		CharSequence  mEditMonitorValue=mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.getText();
		CharSequence  mEditeMonitorDate=mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.getText();
		if(mEditMonitorValue instanceof Spannable || mEditeMonitorDate instanceof  Spannable){
			Spannable mSpanTextLogin=(Spannable)mEditMonitorValue;
			Spannable mSpanTextPwd=(Spannable)mEditeMonitorDate;
			Selection.setSelection(mSpanTextLogin, mEditMonitorValue.length());
			Selection.setSelection(mSpanTextPwd, mEditeMonitorDate.length());
		}
	}
	
	/**
	 * 搜索框-点击触摸监听事件
	 */
	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.editMonitorValue_layout_subside_pactselect_data_modify_change_values:
					//本次测值
					mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setInputType(3);
					mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.setCursorVisible(true);
				break;

			default:
				break;
			}

			return false;
		}
	};
	
	/** 日期选择器监听*/
	DatePickerDialog.OnDateSetListener dateListenter=new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setText("");
			mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			mCander=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
		}
	};
	/** 时间选择器监听*/
	TimePickerDialog.OnTimeSetListener  timeListener=new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
//		 String mDate=	mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.getText().toString();
		 mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setText(mCander+" "+hourOfDay+":"+minute);
		}
	};
	/**
	 *
	 */
	public  boolean   lookEditText(){
		if("".equals(mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.getText().toString()) ||
				"".equals(mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.getText().toString()) ||
				mEditMonitorValue_layout_subside_pactselect_data_modify_change_values.getText().length()<=0 ||
				mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.getText().length()<=0 ||
				mEditMonitorDeep.getText().length()<=0){
			Toast.makeText(mContext, "信息未填写完整", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.editSuperviseDate_ayout_subside_pactselect_data_modify_date_content:
			Calendar calendar=Calendar.getInstance();
			//监测时间
			mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setCursorVisible(false);
			mEditSuperviseDate_ayout_subside_pactselect_data_modify_date_content.setInputType(InputType.TYPE_NULL);
			mTimeDialog=new TimePickerDialog(mContext, timeListener, calendar.get(Calendar.HOUR_OF_DAY), Calendar.MINUTE, true);
		    mDateDialog=new DatePickerDialog(mContext, dateListenter, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		    mTimeDialog.show();
		    mDateDialog.show();
			break;
		}

	}


}
