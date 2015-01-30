package com.tongyan.activity.oa;

import java.util.HashMap;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Agendas;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.MyDatePickerDialog;
import com.tongyan.widget.view.MyDateTimePickerDialog;
import com.tongyan.widget.view.MyDateTimePickerDialog.OnDateTimeSetListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName P09_AddAgendaAct 
 * @author wanghb
 * @date 2013-7-16 pm 02:17:57
 * @desc 移动OA-添加日程
 */
public class OaAgendaModifyAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private Button homeBtn,sureBtn,cancelBtn;
	
	private EditText titleEdit,contentEdit;
	
	private TextView beginTimeEdit,endTimeEdit;//,clockTimeEdit;
	
	private _User localUser;
	
	private Dialog mDialog;
	
	private String isSucc;
	
	private CheckBox box;
	
	private  _Agendas agendas = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_schedule_modify);
		homeBtn = (Button) findViewById(R.id.p09_schedule_title_home_btn);

		titleEdit = (EditText) findViewById(R.id.p09_schedule_add_title);
		contentEdit = (EditText) findViewById(R.id.p09_schedule_add_content);
		beginTimeEdit = (TextView) findViewById(R.id.p09_schedule_add_time_from);
		endTimeEdit = (TextView) findViewById(R.id.p09_schedule_add_time_end);
		//clockTimeEdit = (TextView) findViewById(R.id.p09_schedule_add_clock_time_edittext);
		box = (CheckBox)findViewById(R.id.p09_checkbox_is_allday);
		
		sureBtn = (Button) findViewById(R.id.p07_schedule_modify_sure_btn);
		cancelBtn = (Button) findViewById(R.id.p07_schedule_modify_cancel_btn);
	}
	
	
	OnClickListener cancelBtnListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext, MainAct.class);
			startActivity(intent);
		}
	};
	
	String titleText;
	String contentText;
	String beginTimeText;
	String endTimeText;
	//String clockTimeText;
	boolean isAllDay = false;
	
	OnClickListener submitBtnListener = new OnClickListener() {
		public void onClick(View v) {
			//校验
			titleText = titleEdit.getText().toString();
			contentText = contentEdit.getText().toString();
			if(isAllDay) {
				beginTimeText = beginTimeEdit.getText().toString();// + " 00:00:00";
				endTimeText = endTimeEdit.getText().toString() ;//+ " 23:59:59";
			} else {
				beginTimeText = beginTimeEdit.getText().toString();
				endTimeText = endTimeEdit.getText().toString();
			}
			
			//clockTimeText = clockTimeEdit.getText().toString();
			
			if(titleText == null || "".equals(titleText)) {
				Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(contentText == null || "".equals(contentText)) {
				Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(beginTimeText == null || "".equals(beginTimeText)) {
				Toast.makeText(mContext, "开始时间不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(endTimeText == null || "".equals(endTimeText)) {
				Toast.makeText(mContext, "结束时间不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			/*if(clockTimeText == null || "".equals(clockTimeText)) {
				BaseToast.show(P09_AddAgendaAct.this, "提醒时间不能为空");
				return;
			}*/
			
			newThread();
		}
	};
	
	
	private void setClickListener() {
		titleEdit.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		titleEdit.setCursorVisible(false);//去光标
	
		homeBtn.setOnClickListener(homeBtnListener);
		sureBtn.setOnClickListener(submitBtnListener);
		titleEdit.setOnTouchListener(editTouchListener);
		beginTimeEdit.setOnClickListener(beginTimeListener);
		endTimeEdit.setOnClickListener(endTimeListener);
		//clockTimeEdit.setOnClickListener(clockTimeListener);
		box.setOnCheckedChangeListener(checkChangeListener);
		cancelBtn.setOnClickListener(cancelBtnListener);
	}
	
	OnCheckedChangeListener checkChangeListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked) {
				isAllDay = true;
				beginTimeEdit.setText("");
				endTimeEdit.setText("");
			} else {
				isAllDay = false;
				beginTimeEdit.setText("");
				endTimeEdit.setText("");
			}
		}
	};
	
	OnClickListener beginTimeListener = new OnClickListener() {
		public void onClick(View v) {
			if(isAllDay) {
				new MyDatePickerDialog(mContext, new MyDatePickerDialog.OnDateTimeSetListener() {
					@Override
					public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
							int hour, int minute) {
						String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
						String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
						
						beginTimeEdit.setText(year+"-"+mMonth +"-"+mDay);
					}
				}).show();
			} else {
				new MyDateTimePickerDialog(mContext, new OnDateTimeSetListener() {
					@Override
					public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
							int hour, int minute) {
					  //text.setText(year+"年"+monthOfYear+"月"+dayOfMonth+"日"+hour+"时"+minute+"分");
						String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
						String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
						String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
						String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
						
						beginTimeEdit.setText(year+"-"+mMonth +"-"+mDay+" "+mHour+":"+mMinute+":" +"00");
					}
				}).show();
			}
		}
	};
	
	OnClickListener endTimeListener = new OnClickListener() {
		public void onClick(View v) {
			
			if(isAllDay) {
				new MyDatePickerDialog(mContext, new MyDatePickerDialog.OnDateTimeSetListener() {
					@Override
					public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
							int hour, int minute) {
						String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
						String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
						
						endTimeEdit.setText(year+"-"+mMonth +"-"+mDay);
						
					}
				}).show();
			} else {
			new MyDateTimePickerDialog(mContext, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
				  //text.setText(year+"年"+monthOfYear+"月"+dayOfMonth+"日"+hour+"时"+minute+"分");	
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
					String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
					endTimeEdit.setText(year+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute+":" +"00");
				}
			}).show();
			}
		}
	};
	/*OnClickListener clockTimeListener = new OnClickListener() {
		public void onClick(View v) {
			new MyDateTimePickerDialog(P09_AddAgendaAct.this, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
				  //text.setText(year+"年"+monthOfYear+"月"+dayOfMonth+"日"+hour+"时"+minute+"分");
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
					String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
					clockTimeEdit.setText(year+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute+":" +"00");
				}
			}).show();
		}
	};*/
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		agendas = (_Agendas)getIntent().getExtras().get("agendas");
		if(agendas != null) {
			if("false".equals(agendas.getIsAllDay())) {
				box.setChecked(false);
			} else {
				box.setChecked(true);
			}
			titleEdit.setText(agendas.getsTitle());
			contentEdit.setText(agendas.getsContent());
			beginTimeEdit.setText(agendas.getsTime());
			endTimeEdit.setText(agendas.geteTime());
			
			//departmentText.setText(agendas.getCreateEmp());
		} else {
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		
	}
	
	
	
	
	public void newThread() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String,String> properties = new HashMap<String,String>();
				properties.put("publicKey", Constansts.PUBLIC_KEY);
				properties.put("userName", localUser.getUsername());
				properties.put("Password", localUser.getPassword());
				properties.put("type", "Scheduling");
				String params = "{s_content:'"+contentText+"',s_scheduling_id:'" +agendas.getRowId() + "',s_stime:'"+ beginTimeText +"',s_etime:'" +endTimeText + "',s_isallday:'"+ isAllDay +"',s_title:'" + titleText + "'}";//s_isallday:"false"
				properties.put("parms", params);
				//Log.i("P07_ModifyAgendaAct-params", params);
				try {
					String jsonStr = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_UPDATE, mContext);
					//Log.i("P07_ModifyAgendaAct", jsonStr);
					Map<String,Object> mR = new Str2Json().updateSchedule(jsonStr);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				}  catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
			setResult(222);
			finish();
			break;
		case Constansts.ERRER:
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			if (mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
	/**
	 * 输入框-点击触摸监听事件
	 */
	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			titleEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			titleEdit.setCursorVisible(true);
			return false;
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(302);
			finish();
		}
		return false;
	}
}
