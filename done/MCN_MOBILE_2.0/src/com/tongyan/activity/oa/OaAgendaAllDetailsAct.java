package com.tongyan.activity.oa;

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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P20_AllAgendaDetails 
 * @author wanghb
 * @date 2013-7-24 pm 03:31:43
 * @desc 移动OA-全部日程
 */
public class OaAgendaAllDetailsAct extends AbstructCommonActivity {
	private Context mContext = this;
	private EditText titleText,contentText;//createrText,
	
	private TextView beginTimeText,endTimeText,isAllDayText,departmentText;
	
	private Button homeBtn,delBtn,updateBtn;
	
	private _User localUser;
	private String isSucc;
	
	private Dialog mDialog;
	private  _Agendas agendas = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_schedule_all_detail);
		homeBtn = (Button)findViewById(R.id.p20_schedule_home_btn);
		titleText = (EditText)findViewById(R.id.p20_schedule_detail_title_text);
		contentText = (EditText)findViewById(R.id.p20_schedule_detail_contnt_text);
		beginTimeText = (TextView)findViewById(R.id.p20_schedule_detail_bgin_time_text);
		endTimeText = (TextView)findViewById(R.id.p20_schedule_detail_end_time_text);
		isAllDayText = (TextView)findViewById(R.id.p20_schedule_detail_is_allday_text);
		departmentText = (TextView)findViewById(R.id.p20_schedule_detail_department_text);
		
		delBtn = (Button)findViewById(R.id.p20_schedule_delete_btn_id);
		updateBtn = (Button)findViewById(R.id.p20_schedule_update_btn_id);
	}
	
	private void setClickListener() {
		
		titleText.setInputType(InputType.TYPE_NULL);
		titleText.setCursorVisible(false);//去光标
		
		contentText.setInputType(InputType.TYPE_NULL);
		contentText.setCursorVisible(false);//去光标
		
		
		homeBtn.setOnClickListener(homeBtnListener);
		delBtn.setOnClickListener(delBtnListener);
		updateBtn.setOnClickListener(updateBtnListener);
		
	}
	
	/**
	 * 搜索框-点击触摸监听事件
	 */
	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			titleText.setInputType(InputType.TYPE_CLASS_TEXT);
			titleText.setCursorVisible(true);
			return false;
		}
	};
	
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		agendas = (_Agendas)getIntent().getExtras().get("agendas");
		if(agendas != null) {
			titleText.setText(agendas.getsTitle());
			
			String content = agendas.getsContent();
			if(content == null || "null".equals(content) || "".equals(content)) {
				content = "";
			}
			contentText.setText(content);
			beginTimeText.setText(agendas.getsTime());
			endTimeText.setText(agendas.geteTime());
			if("false".equals(agendas.getIsAllDay())) {
				isAllDayText.setText("否");
			} else {
				isAllDayText.setText("是");
			}
			departmentText.setText(agendas.getCreateEmp());
		} else {
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaAgendaAllDetailsAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	//删除日程(只能删除本人创建的日程)
	OnClickListener delBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(agendas != null && localUser.getUserid() != null && localUser.getUserid().equals(agendas.getEmpId()) ) {
				delete();
			} else {
				Toast.makeText(OaAgendaAllDetailsAct.this, "该数据无权限删除", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public void delete() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String params = "{row_id:'"+agendas.getRowId()+"'}";
				try {
					String jsonStr = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, "Scheduling", params, Constansts.METHOD_OF_DELETE, mContext);
					Map<String,Object> mR = new Str2Json().deleteSchedule(jsonStr);
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
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	OnClickListener updateBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaAgendaAllDetailsAct.this,OaAgendaModifyAct.class);
			intent.putExtra("agendas", agendas);
			startActivityForResult(intent, 222);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 222) {
			setResult(105);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			setResult(105);
			finish();
			break;
		case Constansts.MES_TYPE_1:
			if(mDialog != null)
				mDialog.dismiss();
			setResult(106);
			finish();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
		    break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
