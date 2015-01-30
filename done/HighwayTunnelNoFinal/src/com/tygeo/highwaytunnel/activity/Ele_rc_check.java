package com.tygeo.highwaytunnel.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.adpter.EleCheckResultAdapter;
import com.tygeo.highwaytunnel.adpter.EleShowAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.EleContentE;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
/**
 *定期检修界面 
 **/
public class Ele_rc_check extends Activity {
	Button check_btn1, commit_btn, check_formBtn, newBtn;
	ListView listview;
	String strdate, state;
	Builder builder;
	int task_id, ud_type;

	EditText ed1, ed2, ed3, ed4, ed5, ed6, newEd;
	String sql1, check_pro, select_content, iteminfo;
	ArrayList<String> s, EleCodeList, EleNameList, KindList,Sitelist;
	String ename, kindcode,site,elecode,elename;
	EleCheckResultAdapter adapter;
	DBHelper db;
	ArrayList<EleContentE> contentListData;
	String zh = StaticContent.Up_Down;
	RadioButton rb1, rb2;
	
	String ci[] = { "de_date", "device_name", "device_position", "device_id",
			"device_state", "handle", "fault", "check_type", "task_id" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ele_rc_check);
		InfoApplication.getinstance().addActivity(this);
		if (StaticContent.S_X.equals("S")) {
			ud_type = 0;
		}
		if (StaticContent.S_X.equals("X")) {
			ud_type = 1;
		}
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		db = new DBHelper(StaticContent.DataBasePath);
		inint();
		query();
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		ename = budle.getString("ename");
		System.out.println(ename);
		// 查询内容
		
		check_btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (ename.equals("PD")) {
					check_pro = "1";
					sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='1' ";
				}
				if (ename.equals("ZM")) {
					check_pro = "2";
					sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='2' ";
				}
				if (ename.equals("TF")) {
					check_pro = "3";
					sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='3' ";
				}
				if (ename.equals("XF")) {
					check_pro = "4";
					sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='4' ";
				}
				if (ename.equals("JK")) {
					check_pro = "5";
					sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='5' ";
				}
				Cursor c1 = db.query(sql1);
				
				if (c1.moveToFirst()) {
					s = new ArrayList<String>();
					KindList=new ArrayList<String>();
					do {
						select_content = c1.getString(0);
						s.add(select_content);
						KindList.add(c1.getString(1));
					} while (c1.moveToNext());
					
				}
				iteminfo="";
				builder = new Builder(Ele_rc_check.this);
				builder.setTitle("请选择设备");
				LinearLayout ly = new LinearLayout(Ele_rc_check.this);
//				ListView listview = new ListView(Ele_rc_check.this);
				
				LayoutInflater inflater = LayoutInflater
				.from(Ele_rc_check.this);
				
				final View view = inflater
				.inflate(R.layout.dialoglistviewtype, null);
				ListView listview=(ListView)view.findViewById(R.id.dialoglistview1);
				
				
				ly.addView(view);
				
				EleShowAdapter adapter1 = new EleShowAdapter(s,
						Ele_rc_check.this);
				listview.setAdapter(adapter1);
				builder.setView(ly);
				
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long arg3) {
						// TODO Auto-generated method stub

						if (((ListView) parent).getTag() != null) {
							
							((View) ((ListView) parent).getTag())
									.setBackgroundDrawable(null);
							
						}

						((ListView) parent).setTag(view);

						view.setBackgroundResource(R.drawable.blue);
						iteminfo = s.get(position);
						kindcode= KindList.get(position);
						
					}
					
				});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								ed1.setText(iteminfo);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								iteminfo="";
								kindcode="";
							}
						});
				builder.show();
				
			}

		});

		// 提交数据
		commit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ContentValues cv = new ContentValues();
				task_id = ((InfoApplication) getApplication()).getTask()
						.get_id();
				ArrayList listData = new ArrayList();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日      ");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				strdate = formatter.format(curDate);
				listData.add(strdate);
				listData.add(ed1.getText());
				listData.add(ed2.getText());
				listData.add(ed3.getText());
				if (rb1.isChecked()) {
					state = "正常";
				}
				if (rb2.isChecked()) {
					state = "异常";
				}
				listData.add(state);
				listData.add(ed5.getText());
				listData.add(ed6.getText());
				listData.add("定期检修");
				listData.add(StaticContent.update_id);

				for (int i = 0; i < listData.size(); i++) {
					cv.put(ci[i], listData.get(i).toString());
					System.out
							.println(ci[i] + ":" + listData.get(i).toString());
				}
				if (StaticContent.S_X.equals("S")) {
					cv.put("UP_DOWN", 0);
				}
				if (StaticContent.S_X.equals("X")) {
					cv.put("UP_DOWN", 1);

				}
				cv.put("EleName", elename);
				cv.put("EleCode", elecode);
				db.insert("ELECTRICAL_FAC", cv);
				query();
				Toast t = new Toast(Ele_rc_check.this);
				t.makeText(Ele_rc_check.this, "添加成功",
						Toast.LENGTH_LONG).show();
				
			}
		});
		newBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				
			if(ed1.getText().toString().equals("")){
				Toast t=new Toast(Ele_rc_check.this);
				t.makeText(Ele_rc_check.this, "请选择设备类型", 0).show();
			}
			else{	
				String sql = "select  id,Code,Site from BaseEquipments where TunnelCode='"
					+ StaticContent.Tturnnel_id
					+ "'and Name='"
					+ kindcode + "'";
			
			Cursor c = db.query(sql);
			EleCodeList=new ArrayList<String>();
			EleNameList=new ArrayList<String>();
			Sitelist=new ArrayList<String>();
			if (!(c.getCount()==0)) {
				if (c.moveToFirst()) {
					
					do {
						
						EleCodeList.add(c.getString(0));
						EleNameList.add(c.getString(1));
						Sitelist.add(c.getString(2));
						
					} while (c.moveToNext());
				}
			}else {
				EleNameList.add("暂无设备");
				EleCodeList.add("0");
				Sitelist.add("");
			}
				
				elecode="";
				elename="";
				site="";
				builder = new Builder(Ele_rc_check.this);
				builder.setTitle("请选择设备");
				LinearLayout ly = new LinearLayout(Ele_rc_check.this);
//				ListView listview = new ListView(Ele_rc_check.this);
					
				LayoutInflater inflater = LayoutInflater
				.from(Ele_rc_check.this);
					
				final View view = inflater
				.inflate(R.layout.dialoglistviewtype, null);
				ListView listview=(ListView)view.findViewById(R.id.dialoglistview1);
					
					
				ly.addView(view);
					
				EleShowAdapter adapter1 = new EleShowAdapter(EleNameList,
						Ele_rc_check.this);
				listview.setAdapter(adapter1);
				builder.setView(ly);
				listview.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long arg3) {
						// TODO Auto-generated method stub
						
						if (((ListView) parent).getTag() != null) {
							
							((View) ((ListView) parent).getTag())
									.setBackgroundDrawable(null);
							
						}
						
						((ListView) parent).setTag(view);
						
						view.setBackgroundResource(R.drawable.blue);
						elename =EleNameList.get(position);
						elecode=EleCodeList.get(position);
						site=Sitelist.get(position);
					}
					
				});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								newEd.setText(elename);
								ed2.setText(site);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								elecode="";
								elename="";
								site="";
							}
						});
				builder.show();
				
					
			}}
		});
		
			
		check_formBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Ele_rc_check.this,
						Check_form_Ele.class);
				intent.putExtra("check_type", "机电设施");
				intent.putExtra("task_id", task_id);
				startActivity(intent);
			}
		});
		
	}
		
	public void inint() {
		check_btn1 = (Button) findViewById(R.id.ele_rc_check_checkformBtn);
		commit_btn = (Button) findViewById(R.id.ele_rc_check_commitBtn);
		check_formBtn = (Button) findViewById(R.id.ele_check_form);
		newBtn = (Button) findViewById(R.id.ele_rc_check_newBtn);
		listview = (ListView) findViewById(R.id.ele_rc_check_list);
		ed1 = (EditText) findViewById(R.id.ele_rc_Edit1);
		ed2 = (EditText) findViewById(R.id.ele_rc_Edit2);
		ed3 = (EditText) findViewById(R.id.ele_rc_Edit3);
		ed4 = (EditText) findViewById(R.id.ele_rc_Edit4);
		ed5 = (EditText) findViewById(R.id.ele_rc_Edit5);
		ed6 = (EditText) findViewById(R.id.ele_rc_Edit6);
		newEd = (EditText) findViewById(R.id.ele_rc_newEd);
		rb1 = (RadioButton) findViewById(R.id.ele_rc_check_rb1);
		rb2 = (RadioButton) findViewById(R.id.ele_rc_check_rb2);
		
	}
	
	public void query() {
		int task_id = ((InfoApplication) getApplication()).getTask().get_id();
		
		String sql3 = "select*from ELECTRICAL_FAC where task_id='"
				+ StaticContent.update_id + "'and check_type='" + "定期检修"
				+ "'and UP_DOWN='" + ud_type + "'";
		System.out.println(sql3);
		
		Cursor c3 = db.query(sql3);
		if (!(c3.getCount() == 0)) {
			if (c3.moveToFirst()) {
				contentListData = new ArrayList<EleContentE>();
				do {
					EleContentE ce = new EleContentE();
					ce.set_id(c3.getInt(0));
					ce.setDe_date(c3.getString(2));
					ce.setDevice_name(c3.getString(17));
					ce.setDevice_position(c3.getString(5));
					ce.setDevice_id(c3.getString(4));
					ce.setDevice_state(c3.getString(7));
					ce.setHandle(c3.getString(6));
					ce.setFault(c3.getString(8));
					contentListData.add(ce);

				} while (c3.moveToNext());

				c3.close();
			}
			adapter = new EleCheckResultAdapter(contentListData, this, 1);
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Ele_rc_check.this,Task_info.class));
			// finish();
			Intent intent = new Intent(Ele_rc_check.this, Task_info.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent);
			break;

		default:
			break;
		}
		
		return false;
	}

}
