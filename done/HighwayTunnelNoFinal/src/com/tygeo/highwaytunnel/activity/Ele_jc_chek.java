package com.tygeo.highwaytunnel.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DBManager;
import com.tygeo.highwaytunnel.adpter.CivilPorContentAdapter;
import com.tygeo.highwaytunnel.adpter.EleCheckResultAdapter;
import com.tygeo.highwaytunnel.adpter.EleCheckResultAdapter_jc;
import com.tygeo.highwaytunnel.adpter.EleShowAdapter;
import com.tygeo.highwaytunnel.adpter.SimpleStringAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.EleContentE;

import android.R.array;
import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 *经常检修界面
 **/
public class Ele_jc_chek extends Activity {
	ImageButton check_btn1;
	Button commit_btn,check_formBtn;
	ListView listview1, listview2;
	Builder builder;
	ArrayList<EleContentE> contentListData;
	ImageButton check_Btn3;
	ImageButton newbtn;
	String state;
	ImageButton check_Btn2;
	EleCheckResultAdapter_jc adapter;
	CivilPorContentAdapter adapter0, adapter1, adapter2, adapter3;
	EditText ed1, ed2, ed3, ed4, ed5, ed6, newEd;
	int checktypeele;
	String sql1, check_pro, select_content, iteminfo;
	ArrayList<String> s1, s, s2, s3, codelist, codelist2, codelist3, SiteList,
			EleNameList, EleCodeList, BaseEleList, baseEleCheckList,
			baseEleCodeList;
	String ename, de_name, cde_name, de_pro, cde_content, strdate;
	DBHelper db;
	int task_id, ud_type, EleCode;
	String check_type = "经常性检查", ele_kindcode, sc2, sc3, EleName, Site,
			ed2Code, ed3Code;
	RadioButton rb1, rb2;
	//
	// _id INTEGER PRIMARY KEY AUTOINCREMENT
	// NOT NULL,
	// tacilitiy_type VARCHAR( 20 ),
	// de_date VARCHAR( 20 ),
	// device_name VARCHAR( 20 ),
	// device_id VARCHAR( 20 ),
	// device_position VARCHAR( 30 ),
	// handle VARCHAR( 200 ),
	// device_state VARCHAR( 20 ),
	// fault VARCHAR( 200 ),
	// check_pro VARCHAR( 20 ),
	// check_content VARCHAR( 200 ),
	// check_type VARCHAR( 20 ),
	// task_id INTEGER( 20 )
	String ci[] = { "de_date", "device_name", "device_position", "check_pro",
			"check_content", "handle", "check_type", "task_id" };
	ListView listview;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ele_jc_check);
		InfoApplication.getinstance().addActivity(this);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		db = new DBHelper(StaticContent.DataBasePath);
		inint();
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		ename = budle.getString("ename");
		System.out.println(ename);
		if (StaticContent.S_X.equals("S")) {
			ud_type = 0;
		}
		if (StaticContent.S_X.equals("X")) {
			ud_type = 1;
		}

		String sq = "";
		// 查询内容
		String sqlf = "select  Name from BaseEquipments where TunnelCode='"
				+ StaticContent.Tturnnel_id + "'";

		BaseEleList = new ArrayList<String>();
		Cursor c1 = db.query(sqlf);
		if (!(c1.getCount() == 0)) {
			if (c1.moveToFirst()) {

				do {
					BaseEleList.add(c1.getString(0));

				} while (c1.moveToNext());

			}

		}

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
			sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='5' ";

		}
		if (ename.equals("XF")) {
			check_pro = "4";
			sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='4' ";

		}
		if (ename.equals("JK")) {
			check_pro = "5";
			sql1 = "select EquipmentKindName,EquipmentKindCode  from EquipmentKinds  where EquipmentTypeCode='8' ";

		}

		if (((InfoApplication) getApplication()).getTask().getCivil_check()
				.equals("定期检查")) {
			// this.setContentView(R.layout.civil_por_dq);

			// bt1.setVisibility(View.GONE);
			check_type = "定期检查";

		}

		listview = (ListView) findViewById(R.id.ele_jc_check_list);
		query();
		Cursor c = db.query(sql1);
		if (c.moveToFirst()) {
			baseEleCheckList = new ArrayList<String>();
			baseEleCodeList = new ArrayList<String>();
			s = new ArrayList<String>();

			codelist = new ArrayList<String>();
			do {
				select_content = c.getString(0);
				baseEleCheckList.add(select_content);
				baseEleCodeList.add(c.getString(1));
			} while (c.moveToNext());

		}
		for (int i = 0; i < baseEleCodeList.size(); i++) {
			if (!(BaseEleList.indexOf(baseEleCodeList.get(i)) == -1)) {
				s.add(baseEleCheckList.get(i));
				codelist.add(baseEleCodeList.get(i));
			}

		}
		// ed5.setClickable(false);
		// ed5.setInputType(InputType.TYPE_NULL);
		// ed6.setClickable(false);
		// ed6.setInputType(InputType.TYPE_NULL);
		ed5.setInputType(InputType.TYPE_NULL);
		ed6.setInputType(InputType.TYPE_NULL);
		rb1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed5.setEnabled(false);
				ed6.setEnabled(false);
				ed5.setInputType(InputType.TYPE_NULL);
				ed6.setInputType(InputType.TYPE_NULL);
			}
		});
		rb2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed5.setEnabled(true);
				ed6.setEnabled(true);
				ed5.setInputType(InputType.TYPE_CLASS_TEXT);
				ed6.setInputType(InputType.TYPE_CLASS_TEXT);

			}
		});
		ed5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rb1.isChecked()) {
					Toast t = new Toast(Ele_jc_chek.this);
					t.makeText(Ele_jc_chek.this, "请选择设备异常后添加",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		ed6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rb1.isChecked()) {
					Toast t = new Toast(Ele_jc_chek.this);
					t.makeText(Ele_jc_chek.this, "请选择设备异常后添加",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		check_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StaticContent.select_id = 0;

				if (s.size() == 0) {
					Toast toast = new Toast(Ele_jc_chek.this);
					toast.makeText(Ele_jc_chek.this, "此隧道暂无设备检查信息",
							Toast.LENGTH_SHORT).show();
				} else {
					builder = new Builder(Ele_jc_chek.this);
					builder.setTitle("请选择设备");
					LinearLayout ly = new LinearLayout(Ele_jc_chek.this);
					ly.setOrientation(LinearLayout.VERTICAL);
					// ListView listview = new ListView(Ele_jc_chek.this);
					LayoutInflater inflater = LayoutInflater
							.from(Ele_jc_chek.this);

					final View view = inflater.inflate(
							R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view
							.findViewById(R.id.dialoglistview1);
					ly.addView(view);

					EleShowAdapter adapter1 = new EleShowAdapter(s,
							Ele_jc_chek.this);

					listview.setAdapter(adapter1);
					listview.setItemChecked(0, true);
					builder.setView(ly);

					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							// TODO Auto-generated method stub

							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);

							}

							((ListView) parent).setTag(view);

							view.setBackgroundResource(R.drawable.blue);
							iteminfo = s.get(position);
							ele_kindcode = codelist.get(position);

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
									ed3.setText("");
									newEd.setText("");
									ed2.setText("");
									ed4.setText("");
									de_name = ed1.getText().toString();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.show();
				}
				
			}
		}
		
		);
		
		// listview1.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position,
		// long id) {
		// // TODO Auto-generated method stub
		//
		//
		//
		// if (((ListView)parent).getTag() != null){
		//
		// ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
		//
		// }
		//
		// ((ListView)parent).setTag(view);
		//
		// view.setBackgroundResource(R.drawable.blue);
		//
		// ed1.setText(s.get(position));
		// System.out.println(select_id);
		// de_name=ed1.getText().toString();
		//
		// // adapter0.notifyDataSetChanged();
		//
		// }
		// });

		newbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sql = "select  id,Code,Site from BaseEquipments where TunnelCode='"
						+ StaticContent.Tturnnel_id
						+ "'and Name='"
						+ ele_kindcode + "'";

				Cursor c = db.query(sql);
				EleCodeList = new ArrayList<String>();
				EleNameList = new ArrayList<String>();
				SiteList = new ArrayList<String>();
				if (!(c.getCount() == 0)) {
					if (c.moveToFirst()) {

						do {
							EleCode = Integer.parseInt(c.getString(0));
							EleName = c.getString(1);
							Site = c.getString(2);
							EleCodeList.add(EleCode + "");
							EleNameList.add(EleName);
							SiteList.add(Site);

						} while (c.moveToNext());
					}
				} else {
					EleNameList.add("暂无设备");
					EleCodeList.add("0");
					SiteList.add("");
				}

				builder = new Builder(Ele_jc_chek.this);
				builder.setTitle("请选择设备");
				LinearLayout ly = new LinearLayout(Ele_jc_chek.this);
				ly.setOrientation(LinearLayout.VERTICAL);
				// ListView listview = new ListView(Ele_jc_chek.this);
				LayoutInflater inflater = LayoutInflater.from(Ele_jc_chek.this);

				final View view = inflater.inflate(R.layout.dialoglistviewtype,
						null);
				ListView listview = (ListView) view
						.findViewById(R.id.dialoglistview1);
				ly.addView(view);

				EleShowAdapter adapter1 = new EleShowAdapter(EleNameList,
						Ele_jc_chek.this);

				listview.setAdapter(adapter1);
				listview.setItemChecked(0, true);
				builder.setView(ly);
				EleName = "";
				EleCode = 0;
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
						EleName = EleNameList.get(position);
						EleCode = Integer.parseInt(EleCodeList.get(position));
						Site = SiteList.get(position);
					}

				});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								newEd.setText(EleName);

								ed3.setText("");
								ed4.setText("");
								ed2.setText(Site);

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();

			}
		});

		check_Btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StaticContent.select_id = 1;
				if (ed1.getText().toString().trim().equals("")) {
					Toast toast = new Toast(Ele_jc_chek.this);
					toast.makeText(Ele_jc_chek.this, "请选择设备",
							Toast.LENGTH_SHORT).show();

				} else {
					String sql2 = "select EquipmentCheckItemName,EquipmentCheckItemCode from EquipmentCheckItems where EquipmentKindCode='"
							+ ele_kindcode + "'";
					System.out.println(sql2);
					Cursor c2 = db.query(sql2);
					s2 = new ArrayList<String>();
					codelist2 = new ArrayList<String>();
					if (!(c2.getCount() == 0)) {

						if (c2.moveToFirst()) {

							do {
								cde_name = c2.getString(0);
								System.out.println(cde_name);
								s2.add(cde_name);
								codelist2.add(c2.getString(1));
							} while (c2.moveToNext());
						}
					} else {

						s2.add("暂无检查项目");
					}
					
					builder = new Builder(Ele_jc_chek.this);
					builder.setTitle("请选择检查项");
					LinearLayout ly = new LinearLayout(Ele_jc_chek.this);
					ly.setOrientation(LinearLayout.VERTICAL);
					// ListView listview = new ListView(Ele_jc_chek.this);
					LayoutInflater inflater = LayoutInflater
							.from(Ele_jc_chek.this);

					final View view = inflater.inflate(
							R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view
							.findViewById(R.id.dialoglistview1);
					ly.addView(view);

					EleShowAdapter adapter1 = new EleShowAdapter(s2,
							Ele_jc_chek.this);

					listview.setAdapter(adapter1);
					listview.setItemChecked(0, true);
					builder.setView(ly);

					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							// TODO Auto-generated method stub

							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);

							}

							((ListView) parent).setTag(view);

							view.setBackgroundResource(R.drawable.blue);
							cde_name = s2.get(position);
							ed2Code = codelist2.get(position);

						}

					});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									ed3.setText(cde_name);

									ed4.setText("");

								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.show();

				}
			}

		});
		// StaticContent.listpositonindex = 0;
		check_Btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StaticContent.select_id = 2;
				if (ed3.getText().toString().trim().equals("")) {
					Toast toast = new Toast(Ele_jc_chek.this);
					toast.makeText(Ele_jc_chek.this, "请选择检查项目",
							Toast.LENGTH_SHORT).show();

				} else {
					if (StaticContent.ele_check_type == 2) {
						checktypeele = 2;
					} else {
						checktypeele = 3;
					}

					String sql3 = "select EquipmentCheckRangeName,EquipmentCheckRangeCode  from EquipmentCheckRanges where EquipmentItemCode='"
							+ ed2Code
							+ "' and EquipmentCheckTypeCode='"
							+ checktypeele + "'";

					System.out.println(sql3);
					Cursor c3 = db.query(sql3);
					s3 = new ArrayList<String>();
					codelist3 = new ArrayList<String>();
					if (!(c3.getCount() == 0)) {
						if (c3.moveToFirst()) {

							do {
								cde_content = c3.getString(0);
								System.out.println(cde_content);
								s3.add(cde_content);
								codelist3.add(c3.getString(1));
							} while (c3.moveToNext());

						}
					} else {
						s3.add("暂无设备内容");
						codelist3.add("");

					}
					EleShowAdapter adapter3 = new EleShowAdapter(s3,
							Ele_jc_chek.this);
					builder = new Builder(Ele_jc_chek.this);
					builder.setTitle("请选择设备内容");
					LinearLayout ly = new LinearLayout(Ele_jc_chek.this);
					ly.setOrientation(LinearLayout.VERTICAL);
					// ListView listview = new ListView(Ele_jc_chek.this);
					LayoutInflater inflater = LayoutInflater
							.from(Ele_jc_chek.this);

					final View view = inflater.inflate(
							R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view
							.findViewById(R.id.dialoglistview1);
					ly.addView(view);

					listview.setAdapter(adapter3);
					listview.setItemChecked(0, true);
					builder.setView(ly);

					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							// TODO Auto-generated method stub

							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);

							}

							((ListView) parent).setTag(view);

							view.setBackgroundResource(R.drawable.blue);
							cde_content = s3.get(position);
							ed3Code = codelist3.get(position);

						}

					});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									ed4.setText(cde_content);

								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.show();

				}
			}
		});

		check_formBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Ele_jc_chek.this,
						Ele_check_from_jc.class);
				intent.putExtra("check_type", "机电设施");
				intent.putExtra("task_id", task_id);
				startActivity(intent);
			}
		});

		commit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (ed1.getText().toString().equals("")
						|| ed3.getText().toString().equals("")
						|| ed4.getText().toString().equals("")
						|| newEd.getText().toString().equals("")
						|| newEd.getText().toString().equals("暂无设备")
						|| ed4.getText().toString().equals("暂无设备内容")) {
					Toast t = new Toast(Ele_jc_chek.this);
					t.makeText(Ele_jc_chek.this, "请选择含有完整信息的选择项",
							Toast.LENGTH_LONG).show();
				} else {
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
					listData.add(ed4.getText());
					listData.add(ed5.getText());
					if (StaticContent.ele_check_type == 2) {
						listData.add("经常性检修");
					} else {
						listData.add("定期检修");
					}
					listData.add(StaticContent.update_id);
					System.out.println("存入 task_id= " + task_id);

					for (int i = 0; i < listData.size(); i++) {
						cv.put(ci[i], listData.get(i).toString());
						System.out.println(ci[i] + ":"
								+ listData.get(i).toString());
					}
					if (StaticContent.S_X.equals("S")) {
						cv.put("UP_DOWN", 0);
					}
					if (StaticContent.S_X.equals("X")) {
						cv.put("UP_DOWN", 1);

					}
					if (rb1.isChecked()) {
						state = "1";
					}
					if (rb2.isChecked()) {
						state = "2";
					}
					cv.put("tacilitiy_type", ename);
					cv.put("device_state", state);
					cv.put("fault", ed6.getText().toString());
					cv.put("EleName", EleName);
					cv.put("EleCode", EleCode);
					cv.put("EquipmentKindCode", ele_kindcode);
					cv.put("EquipmentCheckItemCode", ed2Code);
					cv.put("EquipmentCheckRangeCode", ed3Code);
					db.insert("ELECTRICAL_FAC", cv);

					query();
					Toast t = new Toast(Ele_jc_chek.this);
					t.makeText(Ele_jc_chek.this, "添加成功", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	public void inint() {
		check_formBtn = (Button) findViewById(R.id.ele_check_form);
		check_btn1 = (ImageButton) findViewById(R.id.ele_jc_check_btn1);
		commit_btn = (Button) findViewById(R.id.ele_jc_check_commintBtn);
		check_Btn2 = (ImageButton) findViewById(R.id.ele_jc_check_btn2);
		check_Btn3 = (ImageButton) findViewById(R.id.ele_jc_check_btn3);
		newbtn = (ImageButton) findViewById(R.id.ele_jc_check_newbtn);
		listview1 = (ListView) findViewById(R.id.ele_jc_check_list);
		listview2 = (ListView) findViewById(R.id.ele_jc_checkresult_list);
		ed1 = (EditText) findViewById(R.id.ele_rc_Edit1);
		ed2 = (EditText) findViewById(R.id.ele_rc_Edit2);
		ed3 = (EditText) findViewById(R.id.ele_rc_Edit3);
		ed4 = (EditText) findViewById(R.id.ele_rc_Edit4);
		ed5 = (EditText) findViewById(R.id.ele_rc_Edit5);
		ed6 = (EditText) findViewById(R.id.ele_rc_Edit6);
		newEd = (EditText) findViewById(R.id.ele_rc_Editnew);
		rb1 = (RadioButton) findViewById(R.id.ele_rc_check_rb1);
		rb2 = (RadioButton) findViewById(R.id.ele_rc_check_rb2);
	}

	public void query() {
		int task_id = ((InfoApplication) getApplication()).getTask().get_id();
		String check_type = "";
		if (StaticContent.ele_check_type == 2) {
			check_type = "经常性检修";
		} else {
			check_type = "定期检修";
		}

		String sql3 = "select* from ELECTRICAL_FAC where task_id='"
				+ StaticContent.update_id + "'  and check_type='" + check_type
				+ "'and tacilitiy_type='" + ename + "'  and UP_DOWN='"
				+ ud_type + "'";
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
					ce.setCheck_pro(c3.getString(9));
					ce.setCheck_content(c3.getString(10));
					ce.setTask_id(c3.getInt(12));
					System.out.println(c3.getInt(12));
					contentListData.add(ce);

				} while (c3.moveToNext());

				c3.close();
			}
			adapter = new EleCheckResultAdapter_jc(contentListData, this, 1);
			listview2.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Ele_jc_chek.this,Task_info.class));
			finish();
			Intent intent = new Intent(Ele_jc_chek.this, Task_info.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			startActivity(intent);
			break;

		default:
			break;
		}

		return false;
	}

}
