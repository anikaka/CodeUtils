package com.tygeo.highwaytunnel.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.TY.bhgis.Database.DataProvider;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.EleShowAdapter;
import com.tygeo.highwaytunnel.adpter.TaskShowAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 任务添加界面
 */
public class Task_add extends Activity {
	int sition;
	String checkman_id,weatherV="4";
	private SQLiteDatabase database;
	private ListView listview;
	TaskShowAdapter adapter;
	private Button exit_btn, back_btn, next_btn, add_btn, edit_btn;
	//private TextView info_text;
	int bg1, bg2;
	int isedit;
	int bn1, bn2, bn3, bn4,zd,yd;
	ArrayList<String> s1, s2, s3, s4, i1, i2, i3, i4,weathername,weathervalue;
	TunnelInfoE e;
	ArrayList value=new ArrayList();
	String iteminfo, z11, z12, q11, q12;
	Builder builder;
	List<TunnelInfoE> listEData;
	DBHelper db;
	AlertDialog dia;
	DBHelper dbhelper;
	String str, check_t, up_down, meter_type, choic_date, mlete_input;
//	ContentValues cv;
	ArrayList listData;
	EditText dateText, text1, text2, text3, text4, text5, text6;
	EleShowAdapter adapter1;
	TextView c1, c2, c3, c4;
	/*
	 * task_name VARCHAR( 20 ), check_date VARCHAR( 20 ), begin_num VARCHAR( 20
	 * ), end_num VARCHAR( 20 ), civil_check VARCHAR( 50 ), tacilitiy_check
	 * VARCHAR( 30 ), check_head VARCHAR( 20 ), check_member VARCHAR( 20 ),
	 * check_direciton VARCHAR( 20 ), mainte_org VARCHAR( 20 ), weather VARCHAR(
	 * 20 ), temperature INTEGER, humidity INTEGER, picture_beginnum INTEGER,
	 * patrol_car VARCHAR( 20 ), operating_car
	 */
	
//	String ci[] = { "check_date", "up_num", "down_num", "civil_check",
//			"tacilitiy_check", "check_head", "check_member", "up_check_direciton",
//			"mainte_org", "weather", "temperature", "humidity",
//			"picture_beginnum", "task_name"};
	String ci[] = { "task_name","check_date", "up_num", "down_num", "civil_check",
			"tacilitiy_check", "check_head", "check_member", "up_check_direciton", "weather", "temperature", "humidity",
			"picture_beginnum"};
	
	RadioGroup rg1, rg2, rg3;
	RadioButton r1, r2, r3, r4, r5, r6, r7, r8, r9, r10;
	String select_c, select_d, select_f;
	String check_type_text = "";
	int id;
	String strdate, type;
	private Context mContext=this;
	private LayoutInflater mInflater;
	private String mStrStart;//起始桩号+里程
	private String  mStrEnd;//终止桩号+里程
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.task_add);
		InfoApplication.getinstance().addActivity(this);
		if ((((InfoApplication) getApplication()).getNew_task() == null)) {
			id = ((InfoApplication) getApplication()).getTask().get_id();
		}
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		str = ((InfoApplication) getApplication()).getTask_name();
		System.out.println("添加页面任务：" + str);
		up_down = StaticContent.Up_Down;
//		if (up_down.equals("ZK")) {
//			meter_type = "ZK";
//		}
//		if (up_down.equals("YK")) {
//			meter_type = "YK";
//		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		strdate = formatter.format(curDate);
		dbhelper = new DBHelper(StaticContent.DataBasePath);
		// 初始化组件
		listview = (ListView) findViewById(R.id.Task_Add_Listview);
		back_btn = (Button) findViewById(R.id.Task_Add_BackBtn);
		//exit_btn = (Button) findViewById(R.id.Task_Add_ExitBtn);
		// next_btn = (Button) findViewById(R.id.Task_Add_NextBtn);
		edit_btn = (Button) findViewById(R.id.Task_Add_EditBtn);
		//info_text = (TextView) findViewById(R.id.Task_AddText);
		add_btn = (Button) findViewById(R.id.Task_Add_AddBtn);
		Task test = new Task();
		listData = new ArrayList();
//		cv = new ContentValues();
		adapter = new TaskShowAdapter(listData, this);
		listview.setAdapter(adapter);
		String sql = "select* from CHECKINFO ";
		Cursor c1 = dbhelper.query(sql);
		Map<String, ArrayList<String>> map=DB_Provider.	getcheckheadunit();
		s1 = map.get("name");
		s2 = new ArrayList<String>();
		s3 = new ArrayList<String>();
		s4 = new ArrayList<String>();
		i1 = map.get("code");
		i2 = new ArrayList<String>();
		i3 = new ArrayList<String>();
		i4 = new ArrayList<String>();
		if (c1.moveToFirst()) {
			do {
				type = c1.getString(3);
				if (type.equals("P")) {
//					s1.add(c1.getString(1));
//					i1.add(c1.getString(2));
				} else if (type.equals("Y")) {
					s2.add(c1.getString(1));
					i2.add(c1.getString(2));
				} else if (type.equals("X")) {
					s3.add(c1.getString(1));
					i4.add(c1.getString(2));
				} else if(type.equals("Z")){
					s4.add(c1.getString(1));
					i4.add(c1.getString(2));
				}
			} while (c1.moveToNext());
		}
		checkman_id = i1.get(0);
		System.out.println(checkman_id);
		// 如果
		if (!(((InfoApplication) getApplication()).getNew_task() == null)) {
			isedit=0;
			listData.add(str);
			listData.add(strdate);
			listData.add(StaticContent.TunnelBeginMile);
			listData.add(StaticContent.TunnelEndMile);
			listData.add("   ");
			listData.add("   ");
			listData.add("" + s1.get(0) + "");
			listData.add("  ");
			listData.add("进口→出口");
//			listData.add("  "); //TODO ChenLang修改
			listData.add("晴天");
			listData.add(25);
			listData.add(70);
			listData.add(0);
//			listData.add("  ");//ChenLang  update
//			listData.add("  ");
//			listData.add("  ");
			value.add(str);
			value.add(strdate);
			value.add(StaticContent.vTunnelBeginMile);
			value.add(StaticContent.vTunnelEndMile);
			value.add("   ");
			value.add("   ");
			value.add("" + s1.get(0) + "");
			value.add("  ");
			value.add(0);
			value.add("  ");
			value.add(4);
			value.add(25);
			value.add(70);
			value.add(0);
//			value.add("  ");
//			value.add("  ");
			edit_btn.setVisibility(View.GONE);
			StaticContent.TaskStartMile=StaticContent.TunnelBeginMile.split("K")[1];
			StaticContent.TaskEndMile=StaticContent.TunnelEndMile.split("K")[1];
			StaticContent.BeginMile=StaticContent.vTunnelBeginMile;
			StaticContent.EndMile=StaticContent.vTunnelEndMile;
		} else {
			isedit=1;
			Task t = ((InfoApplication) getApplication()).getTask();
			System.out.println(t.getCheck_date());
			try {
				listData.add(t.getTask_name());	  //检查任务名称
				listData.add(t.getCheck_date());  //检查日期
				listData.add(t.getUp_num()); 		 //起始桩号+里程
				listData.add(t.getDown_num());  //终止桩号+里程
				listData.add(t.getCivil_check());   //检查类型
				listData.add(t.getTacilitiy_check());
				listData.add(t.getCheck_head()); //检查组长
				listData.add(t.getCheck_member());
				listData.add(DataProvider.getBaseCSName("检查方向",Integer.parseInt(t.getUp_check_direciton())));
//				listData.add(t.getMainte_org()); TODO
				listData.add(DataProvider.getBaseCSName("天气状况",Integer.parseInt(t.getWeather())));
				listData.add(t.getTemperature());
				listData.add(t.getHumidity());
				listData.add(t.getPicture_beginnum());
//				listData.add(t.getPatrol_car());
//				listData.add(t.getOperating_car());
				value.add(t.getTask_name());
				value.add(t.getCheck_date());
				value.add(t.getUp_num());
				value.add(t.getDown_num());
				value.add(t.getCivil_check());
				value.add(t.getTacilitiy_check());
				value.add(t.getCheck_head());
				value.add(t.getCheck_member());
				value.add(t.getUp_check_direciton()); //TODO values=1
//				value.add(t.getMainte_org());
				value.add(t.getWeather());
				value.add(t.getTemperature());
				value.add(t.getHumidity());
				value.add(t.getPicture_beginnum());
//				value.add(t.getPatrol_car());
//				value.add(t.getOperating_car());	
				check_t = t.getCheck_type();
				add_btn.setVisibility(View.GONE);
				System.out.println("进入界面的检查类型：" + check_t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		((InfoApplication) getApplication()).setNew_task(null);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			// listview 的点击事件
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long id) {
				sition = position;
//				if (sition ==0) {
				if(sition==1){
					WindowManager manager = getWindowManager();
					Display display = manager.getDefaultDisplay();
					int width = display.getWidth();
					int height = display.getHeight();
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.canlender, null);
					final View view = mInflater.inflate(R.layout.canlender, null);
					dia = new AlertDialog.Builder(Task_add.this).setTitle("选择日期")
							.setPositiveButton("确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							}).setNegativeButton("取消", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setView(view).create();
					dia.show();
					final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendarView);
					calendar.setOnDateChangeListener(new OnDateChangeListener() {
						
						@Override
						public void onSelectedDayChange(CalendarView view, int year, int month,
								int dayOfMonth) {
							choic_date=year+"-"+(month+1)+"-"+dayOfMonth+"-";
							listData.set(sition, choic_date);
							value.set(sition, choic_date);
							adapter.notifyDataSetChanged();
						}
					});
				}
				//组长输入
//				if (sition == 5) {
				if(sition==6){
					iteminfo = " ";
					builder = new Builder(Task_add.this);
					builder.setTitle("请选择组长");
					LinearLayout ly = new LinearLayout(Task_add.this);
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.dialoglistviewtype, null);
					final View view = mInflater.inflate(R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view.findViewById(R.id.dialoglistview1);
					final EditText txtHeadManName=new EditText(Task_add.this);
//					final EditText txtHeadManName=(EditText)view.findViewById(R.id.txtHeadmanName);//组长名称
//					txtHeadManName.setVisibility(View.VISIBLE);
//					listview.setVisibility(View.GONE);
					ly.addView(view);
					adapter1 = new EleShowAdapter(s1, Task_add.this);
					listview.setAdapter(adapter1);
//					builder.setView(ly);
					builder.setView(txtHeadManName);
					
//					listview.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> parent,View view, int position, long arg3) {
//							if (((ListView) parent).getTag() != null) {
//								((View) ((ListView) parent).getTag()).setBackgroundDrawable(null);
//							}
//							((ListView) parent).setTag(view);
//							view.setBackgroundResource(R.drawable.blue);
//							iteminfo = s1.get(position);
//							checkman_id = i1.get(position);
//						}
//					});		
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if("".equals(txtHeadManName.getText().toString())){
										Toast.makeText(mContext, "组长信息不能为空", Toast.LENGTH_SHORT).show();
										return;
									}
									dialog.dismiss();
//									listData.set(sition, iteminfo);
//									value.set(sition, iteminfo);
//									adapter.notifyDataSetChanged();
									listData.set(sition, txtHeadManName.getText().toString().trim());
									value.set(sition, txtHeadManName.getText().toString().trim());
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							});
					builder.show();

				}
//				if (sition == 8) {
//					iteminfo = " ";
//					builder = new Builder(Task_add.this);
//					builder.setTitle("请选择");
//					LinearLayout ly = new LinearLayout(Task_add.this);
//					LayoutInflater inflater = LayoutInflater
//							.from(Task_add.this);
//
//					final View view = inflater.inflate(R.layout.dialoglistviewtype, null);
//					ListView listview = (ListView) view
//							.findViewById(R.id.dialoglistview1);
//					ly.addView(view);
//					
//					adapter1 = new EleShowAdapter(s2, Task_add.this);
//					listview.setAdapter(adapter1);
//					builder.setView(ly);
//
//					listview.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> parent,
//								View view, int position, long arg3) {
//
//							if (((ListView) parent).getTag() != null) {
//
//								((View) ((ListView) parent).getTag())
//										.setBackgroundDrawable(null);
//
//							}
//
//							((ListView) parent).setTag(view);
//
//							view.setBackgroundResource(R.drawable.blue);
//
//							iteminfo = s2.get(position);
//						}
//
//					});
//					
//					builder.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.dismiss();
//									listData.set(sition, iteminfo);
//									value.set(sition, iteminfo);
//									
//									adapter.notifyDataSetChanged();
//								}
//							});
//					builder.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.dismiss();
//								}
//							});
//					builder.show();
//
//				}
				//if(sition==9)
//				if (sition == 8) {
				if (sition == 9) {
					iteminfo = "";
					builder = new Builder(Task_add.this);
					builder.setTitle("请选择");
					LinearLayout ly = new LinearLayout(Task_add.this);
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.dialoglistviewtype, null);
					final View view = mInflater.inflate(R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view.findViewById(R.id.dialoglistview1);
					ly.addView(view);
					Map<String, ArrayList<String>> map=DB_Provider.GetBaseNameInfo("天气状况");
					weathername=map.get("name");
					weathervalue=map.get("value");	
					EleShowAdapter adapter1 = new EleShowAdapter(weathername,Task_add.this);
					listview.setAdapter(adapter1);
					builder.setView(ly);
					
					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override 
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							
							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);

							}
							((ListView) parent).setTag(view);
							view.setBackgroundResource(R.drawable.blue);
							iteminfo = weathername.get(position);
							weatherV=weathervalue.get(position);
						}
					});

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
		
									dialog.dismiss();
									listData.set(sition, iteminfo);
									value.set(sition, weatherV);
									adapter.notifyDataSetChanged();
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
//				if (sition == 14) {
				if(sition==13){
					iteminfo = "";
					builder = new Builder(Task_add.this);
					builder.setTitle("请选择");
					LinearLayout ly = new LinearLayout(Task_add.this);
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.dialoglistviewtype, null);
					final View view = mInflater.inflate(R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view.findViewById(R.id.dialoglistview1);
					ly.addView(view);
					EleShowAdapter adapter1 = new EleShowAdapter(s3,Task_add.this);
					listview.setAdapter(adapter1);
					builder.setView(ly);
					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long arg3) {
							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);

							}
							((ListView) parent).setTag(view);
							view.setBackgroundResource(R.drawable.blue);
							iteminfo = s3.get(position);
						}
					});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
		
									dialog.dismiss();
									listData.set(sition, iteminfo);
									value.set(sition, iteminfo);
									adapter.notifyDataSetChanged();
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
				if (sition == 15) {
					iteminfo = "";
					builder = new Builder(Task_add.this);
					builder.setTitle("请选择");
					LinearLayout ly = new LinearLayout(Task_add.this);
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.dialoglistviewtype, null);
					final View view = mInflater.inflate(R.layout.dialoglistviewtype, null);
					ListView listview = (ListView) view.findViewById(R.id.dialoglistview1);
					ly.addView(view);
					EleShowAdapter adapter1 = new EleShowAdapter(s4,Task_add.this);
					listview.setAdapter(adapter1);
					builder.setView(ly);
					listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,View view, int position, long arg3) {

							if (((ListView) parent).getTag() != null) {

								((View) ((ListView) parent).getTag())
										.setBackgroundDrawable(null);
							}
							((ListView) parent).setTag(view);
							view.setBackgroundResource(R.drawable.blue);
							iteminfo = s4.get(position);
						}

					});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
							    
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
		
									dialog.dismiss();
									listData.set(sition, iteminfo);
									value.set(sition, iteminfo);
									
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							});
					builder.show();
				}
				// 文本输入
//				if (sition ==6) {
				if(sition==7){
					dateText = new EditText(Task_add.this);
					dateText.setText(listData.get(sition).toString());
					dateText.setSelection(dateText.getText().length());
					AlertDialog.Builder builder = new Builder(Task_add.this);
					builder.setTitle("请输入：");
					builder.setView(dateText);
					builder.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							String datainfo = dateText.getText().toString().trim();
							listData.set(sition, datainfo);
							value.set(sition, datainfo);
							
						}
					});
					builder.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
				// 桩号输入
//				if (sition == 1) {
				if(sition==2){
					if (isedit==0) {
						System.out.println("new_task");
//						LayoutInflater inflater = LayoutInflater.from(Task_add.this);
						final View view = mInflater.inflate(R.layout.melete_input,null);
						AlertDialog dialog = new AlertDialog.Builder(Task_add.this).setTitle(
										"请输入起始桩号(" + StaticContent.TunnelBeginMile+ "~" + StaticContent.TunnelEndMile+ ")")
										.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
											
										@Override
										public void onClick(DialogInterface dialog,int which) {
											dialog.dismiss();
//											TextView tv1 = (TextView) view.findViewById(R.id.melete_input_tv1);
//											TextView tv2 = (TextView) view.findViewById(R.id.melete_input_tv2);
											EditText et1 = (EditText) view.findViewById(R.id.melete_input_et1);
											EditText et2 = (EditText) view.findViewById(R.id.melete_input_et2);
											// tv1.setText(StaticContent.Up_Down);
											// et1.setText(z11);
											// et2.setText(z12);
											String mt1 = "K"+ et1.getText() + "+"+ et2.getText();
											if (et1.getText().toString().trim().equals("")|| et2.getText().toString().trim().equals("")) {
												Toast.makeText(Task_add.this, "桩号不能为空", Toast.LENGTH_SHORT).show();
												listData.set(sition, StaticContent.TunnelBeginMile);
											} else {
												StaticContent.BeginMile = (Integer.parseInt(et1.getText().toString()) * 1000 + Integer.parseInt(et2.getText().toString()));
												StaticContent.TaskStartMile = mt1.split("K")[1];
													StaticContent.EndMile = StaticContent.vTunnelEndMile- StaticContent.vTunnelBeginMile;
													listData.set(sition, mt1);
													value.set(sition, mt1);
													adapter.notifyDataSetChanged();
													DB_Provider.InsertCurrentZh(StaticContent.BeginMile, StaticContent.update_id,0);
											}
//											if (et1.getText().toString().trim().equals("")) {
//												Toast.makeText(Task_add.this, "桩号不能为空", Toast.LENGTH_SHORT).show();
//												return;
//											} 
//											if(et2.getText().toString().trim().equals("")){
//												Toast.makeText(Task_add.this, "里程不能为空", Toast.LENGTH_SHORT).show();
//												return;
//											}
//											mStrEnd= "K"+ et1.getText() + "+"+ et2.getText();
//											StaticContent.BeginMile = (Integer.parseInt(et1.getText().toString()) * 1000 + Integer.parseInt(et2.getText().toString()));
//											StaticContent.TaskStartMile = mStrEnd.split("K")[1];
//											StaticContent.EndMile = StaticContent.vTunnelEndMile- StaticContent.vTunnelBeginMile;
//											listData.set(sition, mStrEnd);
//											value.set(sition, mStrEnd);
//											DB_Provider.InsertCurrentZh(StaticContent.BeginMile, StaticContent.update_id,0);	
										}
									})
							.setNegativeButton("取消", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
		
									dialog.dismiss();
								}
							}).setView(view).show();
					}
				}
//				if (sition == 2) {
				if (sition == 3) {
					if (isedit==0) {
					System.out.println("ok edit");
//					LayoutInflater inflater = LayoutInflater.from(Task_add.this);
//					final View view = inflater.inflate(R.layout.melete_input,null);
					final View view = mInflater.inflate(R.layout.melete_input,null);
					AlertDialog dialog = new AlertDialog.Builder(Task_add.this)
							.setTitle(
//									"请输入结束桩号(" + StaticContent.TunnelEndMile
//											+ StaticContent.TunnelBeginMile + ")")
//											.setPositiveButton("确定",
													"请输入结束桩号(" + StaticContent.TunnelBeginMile + StaticContent.TunnelEndMile+ ")")
													.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
								
										@Override
										public void onClick(DialogInterface dialog,int which) { 
											dialog.dismiss();
											TextView tv1 = (TextView) view.findViewById(R.id.melete_input_tv1);
											TextView tv2 = (TextView) view.findViewById(R.id.melete_input_tv2);
											EditText et1 = (EditText) view.findViewById(R.id.melete_input_et1);
											EditText et2 = (EditText) view.findViewById(R.id.melete_input_et2);
											// tv1.setText(StaticContent.Up_Down)
											et1.setInputType(InputType.TYPE_CLASS_NUMBER);
											et2.setInputType(InputType.TYPE_CLASS_NUMBER);

											if (et1.getText().toString().trim().equals("")
													|| et2.getText().toString().trim().equals("")) {
												Toast.makeText(Task_add.this, "桩号不能为空", Toast.LENGTH_SHORT).show();
												listData.set(sition,StaticContent.TunnelEndMile);
											} else {
												String mt2 = "K"+ et1.getText() + "+"+ et2.getText();
												StaticContent.EndMile= (Integer.parseInt(et1.getText().toString()) * 1000 + Integer.parseInt(et2.getText().toString()));
												StaticContent.TaskEndMile = mt2.split("K")[1];
											    StaticContent.Ymelete_count = StaticContent.EndMile- StaticContent.BeginMile;
												listData.set(sition, mt2);
												value.set(sition, mt2);
												adapter.notifyDataSetChanged();
												DB_Provider.InsertCurrentZh(StaticContent.BeginMile, StaticContent.update_id,0);
											}	
//											if (et1.getText().toString().trim().equals("")) {
//												Toast.makeText(Task_add.this, "桩号不能为空", Toast.LENGTH_SHORT).show();
//												return;
//											} 
//											if(et2.getText().toString().trim().equals("")){
//												Toast.makeText(Task_add.this, "里程不能为空", Toast.LENGTH_SHORT).show();
//												return;
//											}
//											mStrEnd = "K"+ et1.getText() + "+"+ et2.getText();
//											StaticContent.EndMile= (Integer.parseInt(et1.getText().toString()) * 1000 + Integer.parseInt(et2.getText().toString()));
//											StaticContent.TaskEndMile = mStrEnd.split("K")[1];
//										    StaticContent.Ymelete_count = StaticContent.EndMile- StaticContent.BeginMile;
//											listData.set(sition, mStrEnd);
//											value.set(sition, mStrEnd);
//											DB_Provider.InsertCurrentZh(StaticContent.BeginMile, StaticContent.update_id,0);
										}
									})
							.setNegativeButton("取消", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setView(view).show();
					}
				}
				if (sition == 10 || sition == 11 || sition == 12) {
					
					dateText = new EditText(Task_add.this);
					dateText.setText(listData.get(sition).toString());
					if(sition==10){
						dateText.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
					}else{						
						dateText.setInputType(InputType.TYPE_CLASS_NUMBER);
					}
					dateText.setSelection(dateText.getText().length());
					AlertDialog.Builder builder = new Builder(Task_add.this);
					builder.setTitle("请输入数字：");
					builder.setView(dateText);
					builder.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							String datainfo = dateText.getText().toString().trim();
							listData.set(sition, datainfo);
							value.set(sition, datainfo);
							
						}
					});
					builder.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
//				if (sition == 3) {
				if (sition == 4) {
					rg1 = new RadioGroup(Task_add.this);
					r1 = new RadioButton(Task_add.this);
					r1.setSelected(true);
					r1.setText("日常检查");
					r2 = new RadioButton(Task_add.this);
					r2.setText("定期检查");
					
					//TODO
					 r2.setClickable(true);
//					r3 = new RadioButton(Task_add.this);
//					r3.setText("隐患检查");
//					r3.setClickable(true);
					r4 = new RadioButton(Task_add.this);
					r4.setText("无");
					LinearLayout l = new LinearLayout(Task_add.this);
					rg1.addView(r1);
					rg1.addView(r2);
//					rg1.addView(r3);
					rg1.addView(r4);
					r1.setChecked(true);
					l.addView(rg1);
					
					l.setOrientation(LinearLayout.VERTICAL);
					AlertDialog.Builder builder = new Builder(Task_add.this);
					builder.setView(l);
					builder.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							//
							select_c = "";
							if (r1.isChecked()) {
								select_c = r1.getText().toString();
								System.out.println(select_c);
								check_type_text = "土建结构";
							}
							if (r2.isChecked()) {
								select_c = r2.getText().toString();
								System.out.println(select_c);
								check_type_text = "土建结构";
							}
							
//							if (r3.isChecked()) {
//								select_c = r3.getText().toString();
//								System.out.println(select_c);
//								check_type_text = "土建结构";
//							}
							if (r4.isChecked()) {
								select_c = " ";
								System.out.println(select_c);
							}
							listData.set(sition, select_c);
							value.set(sition, select_c);
							adapter.notifyDataSetChanged();
						}
					});
					
					//
					builder.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
//				if (sition == 4) { //
				if(sition==5){
					rg2 = new RadioGroup(Task_add.this);
					r5 = new RadioButton(Task_add.this);
					r5.setText("经常性检修");
					r6 = new RadioButton(Task_add.this);
					r6.setText("定期检修");
//					r7 = new RadioButton(Task_add.this);
//					r7.setText("隐患排查");
//					r7.setClickable(false);
					r8 = new RadioButton(Task_add.this);
					r8.setText("无");
					LinearLayout l2 = new LinearLayout(Task_add.this);
					rg2.addView(r5);
					rg2.addView(r6);
//					rg2.addView(r7);
					rg2.addView(r8);
					r5.setChecked(true);
					l2.addView(rg2);
					l2.setOrientation(LinearLayout.VERTICAL);
					AlertDialog.Builder builder = new Builder(Task_add.this);
					builder.setView(l2);
					builder.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							select_d = " ";
							if (r5.isChecked()) {
								select_d = r5.getText().toString();
								check_type_text = "机电设施";
							}
							if (r6.isChecked()) {
								select_d = r6.getText().toString();
								System.out.println(select_d);
								check_type_text = "机电设施";
							}
//							if (r7.isChecked()) {
//								select_d += r7.getText().toString();
//								System.out.println(select_d);
//								check_type_text = "机电设施";
//							}
							if (r8.isChecked()) {
								select_d = " ";
								System.out.println(select_d);
							}
							listData.set(sition, select_d);
							value.set(sition, select_d);
							adapter.notifyDataSetChanged();
						}
					});
					//
					builder.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
//				if (sition == 7) {
					if(sition==8){
					rg3 = new RadioGroup(Task_add.this);
					r9 = new RadioButton(Task_add.this);
					r9.setText("进口→出口");
					r10 = new RadioButton(Task_add.this);
					r10.setText("出口→进口");
					LinearLayout l3 = new LinearLayout(Task_add.this);
					rg3.addView(r9);
					rg3.addView(r10);
					l3.addView(rg3);
					r9.setChecked(true);
					l3.setOrientation(LinearLayout.VERTICAL);
					AlertDialog.Builder builder = new Builder(Task_add.this);
					builder.setView(l3);
					builder.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							select_f = "";
							if (r9.isChecked()) {
								select_f = r9.getText().toString();
								zd=0;
								System.out.println(select_f);
							}
							if (r10.isChecked()) {
								select_f = r10.getText().toString();
								System.out.println(select_f);
								zd=1;
							}
							listData.set(sition, select_f);
							value.set(sition, zd);
							adapter.notifyDataSetChanged();
						}
					});
					//
					builder.setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				}
//				if (sition == 8) {
//					rg3 = new RadioGroup(Task_add.this);
//					r9 = new RadioButton(Task_add.this);
//					r9.setText("进口→出口");
//					r10 = new RadioButton(Task_add.this);
//					r10.setText("出口→进口");
//					LinearLayout l3 = new LinearLayout(Task_add.this);
//					rg3.addView(r9);
//					rg3.addView(r10);
//					l3.addView(rg3);
//					r9.setChecked(true);
//					l3.setOrientation(LinearLayout.VERTICAL);
//					AlertDialog.Builder builder = new Builder(Task_add.this);
//					builder.setView(l3);
//					builder.setPositiveButton("确定", new OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//
//							dialog.dismiss();
//							//
//							select_f = "";
//
//							if (r9.isChecked()) {
//								select_f = r9.getText().toString();
//								System.out.println(select_f);
//								yd=0;
//							}
//							if (r10.isChecked()) {
//								select_f = r10.getText().toString();
//								System.out.println(select_f);
//								yd=1;
//							}
//
//							listData.set(sition, select_f);
//							value.set(sition, yd);
//							adapter.notifyDataSetChanged();
//						}
//					});
//					//
//					builder.setNegativeButton("取消", new OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					});
//
//					builder.show();
//				}
			}
		});

		add_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (listData.get(3).toString().trim().equals("")
//						&& listData.get(4).toString().trim().equals("")) {
				if (listData.get(4).toString().trim().equals("")&& listData.get(5).toString().trim().equals("")) {
					Toast.makeText(Task_add.this, "请选择检查类型", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(7).toString().trim().equals("")) {
				} else if (listData.get(8).toString().trim().equals("")) {
					Toast.makeText(Task_add.this, "请选择检查方向", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(1).toString().substring(2).trim().equals("")) {
				} else if (listData.get(2).toString().substring(2).trim().equals("")) {
					Toast.makeText(Task_add.this, "请输入起始桩号", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(2).toString().trim().substring(2).equals("")) {
				} else if (listData.get(3).toString().trim().substring(2).equals("")) {
					Toast.makeText(Task_add.this, "请输入结束桩号", Toast.LENGTH_SHORT).show();
				} else {
					ContentValues cv=new ContentValues();
//					for (int i = 0; i < listData.size(); i++) {					
//							cv.put(ci[i], listData.get(i).toString());
//						System.out.println(ci[i] + ":"+ listData.get(i).toString());
//					}
					for (int i = 0; i < listData.size(); i++) {
						if(i<ci.length-1){							
							cv.put(ci[i], listData.get(i).toString());
							System.out.println(ci[i] + ":"+ value.get(i).toString());
						}
					}
//					if (listData.get(3).toString().trim().equals("")) {
//						check_type_text = "机电设施";
//						System.out.println("土建空");
//						cv.put("check_type", check_type_text);
//					} else if (listData.get(4).toString().trim().equals("")) {
//						check_type_text = "土建结构";
//						System.out.println("机电空");
//						cv.put("check_type", check_type_text);
//					} else {
//						check_type_text = "土建结构/机电设施";
//						cv.put("check_type", check_type_text);
//					}
					if ("".equals(listData.get(4).toString().trim())) {
						check_type_text = "机电设施";
						System.out.println("土建空");
						cv.put("check_type", check_type_text);
					} else if ("".equals(listData.get(5).toString().trim())) {
						check_type_text = "土建结构";
						System.out.println("机电空");
						cv.put("check_type", check_type_text);
					} else if("".equals(listData.get(4).toString().trim()) && "".equals(listData.get(5).toString().trim())){
						check_type_text = "土建结构/机电设施";
						cv.put("check_type", check_type_text);
					}else if(!"".equals(listData.get(4).toString().trim()) && !"".equals(listData.get(5).toString().trim())){
						check_type_text = "土建结构/机电设施";
						cv.put("check_type", check_type_text);
					}
					String date = listData.get(0).toString();
					date = date.replace("年", "");
					date = date.replace("月", "");
					date = date.replace("日", "");
					date = date.trim();
//					StaticContent.update_id = StaticContent.Tsection_id + "_"
//							+ StaticContent.Tline_id + "_"
//							+ StaticContent.Tturnnel_id + "_" + listData.get(1)
//							+ "_" + listData.get(2)
//							+ "_" + date + "_" + checkman_id +"_"+StaticContent.Up_Down;
					StaticContent.update_id = StaticContent.Tsection_id + "_"
							+ StaticContent.Tline_id + "_"
							+ StaticContent.Tturnnel_id + "_" + listData.get(2)
							+ "_" + listData.get(3)
							+ "_" + date + "_" + checkman_id +"_"+StaticContent.Up_Down;
					if (DB_Provider.addtask(StaticContent.update_id) == false) {
						Toast.makeText(Task_add.this, "已有相同任务，请确保检查组长、检查起始里程不同",Toast.LENGTH_LONG).show();
						
					} else {
						cv.put("update_id", StaticContent.update_id);
						cv.put("up_current_lczh", "K"+StaticContent.BeginMile); 
						cv.put("down_current_lczh", "K"+StaticContent.EndMile);
						cv.put("up_down", StaticContent.Up_Down);
						cv.put("up_check_direciton", zd);
						cv.put("down_check_direciton", yd);
						cv.put("weather", weatherV);
						dbhelper.insert("TASK", cv);
						Intent intent = new Intent(Task_add.this,Task_info.class);
						intent.putExtra("task_name", str);
						intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						// finish();
					}
				}
			}
		});
		edit_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (listData.get(3).toString().trim().equals("")&& listData.get(4).toString().trim().equals("")) {
//					Toast.makeText(Task_add.this, "请选择检查类型", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(7).toString().trim().equals("")) {
//					Toast.makeText(Task_add.this, "请选择检查方向", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(1).toString().trim().substring(2).equals("")) {
//					Toast.makeText(Task_add.this, "请输入起始桩号", Toast.LENGTH_SHORT).show();
//				} else if (listData.get(2).toString().trim().substring(2).equals("")) {
//					Toast.makeText(Task_add.this, "请输入结束桩号", Toast.LENGTH_SHORT).show();
//				} else {
//					if (listData.get(3).toString().trim().equals("")) {
//					if (listData.get(5).toString().trim().equals("")) { 
//						check_type_text = "机电设施";
//						System.out.println("土建空");
//					} else if (listData.get(4).toString().trim().equals("")) {
//						check_type_text = "土建结构";
//						System.out.println("机电空");
//					} else {
//						check_type_text = "土建结构/机电设施";
//					}
				if (listData.get(4).toString().trim().equals("")&& listData.get(5).toString().trim().equals("")) {
					Toast.makeText(Task_add.this, "请选择检查类型", Toast.LENGTH_SHORT).show();
				} else if (listData.get(8).toString().trim().equals("")) {
					Toast.makeText(Task_add.this, "请选择检查方向", Toast.LENGTH_SHORT).show();
				} else if (listData.get(2).toString().trim().substring(2).equals("")) {
					Toast.makeText(Task_add.this, "请输入起始桩号", Toast.LENGTH_SHORT).show();
				} else if (listData.get(3).toString().trim().substring(2).equals("")) {
					Toast.makeText(Task_add.this, "请输入结束桩号", Toast.LENGTH_SHORT).show();
				} else {
					if ("".equals(listData.get(4).toString().trim())) {
						check_type_text = "机电设施";
						System.out.println("土建空");
					} else if ("".equals(listData.get(5).toString().trim())) {
						check_type_text = "土建结构";
						System.out.println("机电空");
					} else if("".equals(listData.get(4).toString().trim()) && "".equals(listData.get(5).toString().trim())){
						check_type_text = "土建结构/机电设施";
					}else if(!"".equals(listData.get(4).toString().trim()) && !"".equals(listData.get(5).toString().trim())){
						check_type_text = "土建结构/机电设施";
					}
					System.out.println(check_type_text);

					String sql = "UPDATE TASK SET ";

					for (int i = 0; i < ci.length; i++) {
						if (i < ci.length - 1) {
						sql += ci[i] + "='" + value.get(i).toString() + "'";
						sql += ",";
						}
					}
//					String date = listData.get(0).toString();
					String date=listData.get(1).toString();
					date = date.replace("年", "");
					date = date.replace("月", "");
					date = date.replace("日", "");
					date = date.trim();
//					StaticContent.update_id = StaticContent.Tsection_id + "_"
//							+ StaticContent.Tline_id + "_"
//							+ StaticContent.Tturnnel_id + "_" + listData.get(1)
//							+ "_" + listData.get(2) + "_" + StaticContent.S_X
//							+ "_" + date + "_" + checkman_id;
					StaticContent.update_id = StaticContent.Tsection_id + "_"
							+ StaticContent.Tline_id + "_"
							+ StaticContent.Tturnnel_id + "_" + listData.get(2)
							+ "_" + listData.get(3) + "_" + StaticContent.S_X
							+ "_" + date + "_" + checkman_id;
					
					sql += " check_type='" + check_type_text + "',update_id='"
							+ StaticContent.update_id + "'where _id='" + id
							+ "'";
					System.out.println(sql);
					// String sql="update  TASK  set" +
					//
					// "   where _id='"+ id+"'";
					dbhelper.execSql(sql);
					Intent intent = new Intent(Task_add.this, Task_info.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("task_name", str);
					startActivity(intent);
				}
			}
		});
		
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(Task_add.this, Task_info.class));
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Task_add.this, Task_info.class));
			finish();
			break;

		default:
			break;
		}

		return false;
	}
}
