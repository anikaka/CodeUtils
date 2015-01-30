package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;

import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Util.mapUtil;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.CivilLevelAdapter;
import com.tygeo.highwaytunnel.adpter.CivilPorContentAdapter;
import com.tygeo.highwaytunnel.adpter.RoadbedResultAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Roadbed extends Activity {
	// 路面显示
	EditText tv1, tv2, civil_bz;
	ListView RoadbedListview1, RoadbedListview3, RoadbedListview2,
			PorCheckResultListview;
	ArrayList<String> s, s3;
	RoadbedResultAdapter adapter4;
	int  PDok;//判读描述存在
	int cs;
	int involve;
	ArrayList<Integer> CheckcontentId;
	int check_id, check_itemId, positionid, checktypeid, ud_type,begin_melete,end_melete,meter;
	String c_content, BH_content, ZKmeter;
	/*
	 * check_data VARCHAR( 20 ), check_position VARCHAR( 20 ), mileage VARCHAR(
	 * 20 ), judge_level VARCHAR( 20 ), level_content VARCHAR( 20 ), bh_pic
	 * VARCHAR( 20 ), pic_id INTEGER, belong_pro VARCHAR( 50 ) /
	 */
	String ci[] = { "mileage", "level_content", "judge_level", "check_data",
			"pic_id", "belong_pro" };
	String Zk, t1, t2;
	DBHelper db;
	String up_down;
	TextView text;
	ArrayList<CivilContentE> contentListData;
	// String []args={"出口","入口"};
	String index;
	CivilPorContentAdapter adapter1;
	CivilLevelAdapter adapter3;
	// CivilContentAdapter adapter4 ;
	// ArrayAdapter<String> adapter2;
//	private int clickPosition = -1;
	RadioGroup Civil_RadioGp_2,Civil_RadioGp;
	String check_pro, select_content, level_content;
	Button bt1, bt2, bt3, commitBtn, photoBtn, photoDelBtn, check_formBtn;
	RadioButton rb1, rb2, rb3,hava1,hava2;
	String check_type = "日常检查";
	String sql1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.civil_roadbed);
		InfoApplication.getinstance().addActivity(this);
		
		// 打开数据库
		db = new DBHelper(StaticContent.DataBasePath);
		text = (TextView) findViewById(R.id.roadbedMETER);
		// 实体化listview
		tv1 = (EditText) findViewById(R.id.Civil_RoadBed_Edit1);
		PDok=0;
		tv2 = (EditText) findViewById(R.id.Civil_RoadBed_Edit2);
		tv1.setInputType(InputType.TYPE_CLASS_NUMBER);
		tv2.setInputType(InputType.TYPE_CLASS_NUMBER);
		commitBtn = (Button) findViewById(R.id.Civil_RoadBed_CommitBtn);
		rb1 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn1);
		// bt1 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn1);
		rb2 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn2);
		// bt2 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn2);
		rb3 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn3);
		// bt3 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn3);
		hava1= (RadioButton) findViewById(R.id.Civil_Config_HAVA_1);
		hava2= (RadioButton) findViewById(R.id.Civil_Config_HAVA_2);
		Civil_RadioGp=(RadioGroup)findViewById(R.id.Civil_RadioGp);
		Civil_RadioGp_2=(RadioGroup)findViewById(R.id.Civil_RadioGp_2);
		civil_bz = (EditText) findViewById(R.id.civil_BZ_Edit);
		civil_bz.setInputType(InputType.TYPE_NULL);
		civil_bz.setHint("请输入");
		rb1.setClickable(false);
		rb2.setClickable(false);
		rb3.setClickable(false);
		if (((InfoApplication) getApplication()).getTask().getCivil_check()
				.equalsIgnoreCase("定期检查")) {
			// this.setContentView(R.layout.roadbed_dq);
			check_type = "定期检查";
//			rb1.setVisibility(View.GONE);
			// bt1.setVisibility(View.GONE);
		}
			
		if (StaticContent.S_X.equals("S")) {
			up_down = "上行";
			ud_type = 0;
			begin_melete=StaticContent.BeginMile;
			end_melete=StaticContent.EndMile;
			text.setText("("+ StaticContent.TaskStartMile + "/"+ StaticContent.TaskEndMile+ ")");
		} else {
			up_down = "下行";
			ud_type = 1;
			begin_melete=StaticContent.BeginMile;
			end_melete=StaticContent.EndMile;
			text.setText("K" + "("
					+ StaticContent.TaskStartMile + "/"
					+ StaticContent.TaskEndMile + ")");
		}
		
	hava2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PDok=0;
				rb1.setChecked(false);
				rb2.setChecked(false);
				rb3.setChecked(false);
				rb1.setClickable(false);
				rb2.setClickable(false);
				rb3.setClickable(false);
				rb1.setTextColor(Color.GRAY);
				rb2.setTextColor(Color.GRAY);
				rb3.setTextColor(Color.GRAY);
				
			}
		});
	hava1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			PDok=1;
			rb1.setClickable(true);
			rb2.setClickable(true);
			rb3.setClickable(true);
			rb1.setTextColor(Color.BLACK);
			rb2.setTextColor(Color.BLACK);
			rb3.setTextColor(Color.BLACK);
			 if (check_type.equals("定期检查")) {
				 
				Civil_RadioGp.check(1); 
				 rb2.setChecked(true);
				 } else {
				 Civil_RadioGp.check(0);
				 rb1.setChecked(true);
				 
				 }	
		}
	});
		RoadbedListview1 = (ListView) findViewById(R.id.Civil_RoadBed_listview1);

		PorCheckResultListview = (ListView) findViewById(R.id.Civil_Roadbed_CheckResult_Listview);

		// 适配器
		// 查询检查内容

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		index = bundle.getString("name");
		// 判断所选页面
		if (index.equals("LM")) {
			check_pro = "路面";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'  ";
			StaticContent.tabhost_id = 3;
			c_content = "落物";
		}
		if (index.equals("JXD")) {
			check_pro = "检修道";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 4;
			c_content = "结构破损";
		}
		if (index.equals("PASS")) {
			check_pro = "排水";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 5;
			c_content = "破损";
		}
		if (index.equals("DD")) {
			check_pro = "吊顶";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 6;
			c_content = "变形";
		}
		if (index.equals("NZ")) {
			check_pro = "内装";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 7;
			c_content = "脏污";
		}
		StaticContent.BH_index_name = check_pro;
//		String checkt = ((InfoApplication) getApplication()).getTask().getCivil_check();
		query();
		Cursor c = db.query(sql1);
		System.out.println(c.getColumnCount());
		if (c.moveToFirst()) {
			s = new ArrayList<String>();
			CheckcontentId = new ArrayList<Integer>();
			do {
				select_content = c.getString(0);
				s.add(select_content);
				CheckcontentId.add(c.getInt(1));
			} while (c.moveToNext());
			adapter1 = new CivilPorContentAdapter(s, this);
			c.close();
		}
		check_id = CheckcontentId.get(0);
		ArrayList<String> p_level = DB_Provider.GetFirstP_level(check_pro,check_id);
		rb1.setText(p_level.get(0));
		rb2.setText(p_level.get(1));
		if(rb3!=null){			
			rb3.setText(p_level.get(2));
		}
		RoadbedListview1.setItemChecked(0, true);
		RoadbedListview1.setAdapter(adapter1);
		// listview1的点击监听
		 cs = RoadbedListview1.getCheckedItemPosition();
		RoadbedListview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				
				if (((ListView) parent).getTag() != null) {

					((View) ((ListView) parent).getTag())
							.setBackgroundDrawable(null);

				}
			
				if (!(position == cs)) {
					civil_bz.setTag(null);
					civil_bz.setText("");
				}

				((ListView) parent).setTag(view);
				StaticContent.listselectindex = position;
				view.setBackgroundResource(R.drawable.blue);
				c_content = s.get(position);
				check_id = CheckcontentId.get(position);
				StaticContent.BH_p_name = c_content;

//				String sql2 = "select* from CIVIL_CHECK_INFO where check_content='"
//						+ c_content
//						+ "'and check_pro='"
//						+ check_pro
//						+ "'and check_type='" + check_type + "'";
//				Cursor c1 = db.query(sql2);
//				if (c1.moveToFirst()) {
//					s3 = new ArrayList<String>();
//					do {
//
//						rb1.setText(c1.getString(3));
//						rb2.setText(c1.getString(4));
//						rb3.setText(c1.getString(5));
//					} while (c1.moveToNext());
//
//				}
				ArrayList<String> p_level = DB_Provider.GetFirstP_level(check_pro,check_id);
				rb1.setText(p_level.get(0));
				rb2.setText(p_level.get(1));
				if(p_level.size()>2 && rb3!=null){					
					rb3.setText(p_level.get(2));
				}
			}
		});
		
		if (check_type.equals("日常检查")) {
			civil_bz.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (hava2.isChecked()) {
//						Toast to = new Toast(Roadbed.this);
//						to.makeText(Roadbed.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT)
//								.show();
						Toast.makeText(Roadbed.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT).show();
					}else{
					final EditText editText = new EditText(Roadbed.this);
					editText.setText(civil_bz.getText().toString());
					editText.setGravity(Gravity.TOP);
					editText.setWidth(300);
					editText.setHeight(200);
					AlertDialog dialog = new AlertDialog.Builder(Roadbed.this)
							.setTitle("请输入备注")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											
											dialog.dismiss();
											civil_bz.setText(editText.getText()
													.toString());
											
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											
											dialog.dismiss();
										}
									}).setView(editText).show();
					}
				}
			});
		} else {
			civil_bz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if (hava2.isChecked()) {
//						Toast to = new Toast(Roadbed.this);
//						to.makeText(Roadbed.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT)
//								.show();
						Toast.makeText(Roadbed.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT).show();
					}else{
					mapUtil.DialogBHInfoInput(Roadbed.this, check_id);
					}
					//
					// ArrayList<String>
					// list1=StaticContent.DQinfoN.get(StaticContent.update_id+check_id);
					// ArrayList<String>
					// list2=StaticContent.DQinfoN.get(StaticContent.update_id+check_id);
					// for (int i = 0; i <list1.size(); i++) {
					//
					// DQBZ+=list1.get(i)+":"+list2.get(i)+"\n";
					//
					// }

				}
			});

		}

		
		commitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZKmeter = tv1.getText().toString() + "+"+ tv2.getText().toString();
				System.out.println(ZKmeter);
				t1 = tv1.getText().toString().trim();
				t2 = tv2.getText().toString().trim();
//				if(!"".equals(t1) && !"".equals(t2)){					
//					int meter = Integer.parseInt(t1)*1000+ Integer.parseInt(t2);
//				}

				if (t1.equals("") ||t2.equals("")) {
//					Toast to = new Toast(Roadbed.this);
//					to.makeText(Roadbed.this, "请输入完整桩号", Toast.LENGTH_LONG)
//							.show();
					Toast.makeText(Roadbed.this, "请输入完整桩号",Toast.LENGTH_SHORT).show();
				} else if (c_content == null) {
//					Toast to = new Toast(Roadbed.this);
//					to.makeText(Roadbed.this, "请选择检查内容", Toast.LENGTH_SHORT)
//							.show();
					Toast.makeText(Roadbed.this, "请选择检查内容",Toast.LENGTH_SHORT).show();
				} else {
					meter = Integer.parseInt(tv1.getText().toString()) * 1000
					+ Integer.parseInt(tv2.getText().toString());

			if (meter < begin_melete|| meter > end_melete) {
				
//				Toast to = new Toast(Roadbed.this);
//				to.makeText(Roadbed.this, "请输入正确的桩号", Toast.LENGTH_SHORT)
//						.show();
				Toast.makeText(Roadbed.this, "请输入正确的桩号",Toast.LENGTH_SHORT).show();
			}
					
				else{
					int z1 = Integer.parseInt(tv1.getText().toString()) * 1000;
					int z2 = Integer.parseInt(tv2.getText().toString());
					StaticContent.now_zh = z1 + z2;
					DB_Provider.InsertCurrentZh(StaticContent.now_zh,
							StaticContent.update_id, ud_type);
					level_content = "";
					BH_content = "无";
					if (rb1.isChecked()) {
						level_content = "S";
						BH_content = rb1.getText().toString();
						System.out.println(BH_content);
					}
					if (rb2.isChecked()) {
						BH_content = rb2.getText().toString();
						level_content = "B";
					}
					if (rb3.isChecked()) {
						BH_content = rb3.getText().toString();
						level_content = "A";
					}

					ContentValues cv = new ContentValues();
					ArrayList<Object> listData = new ArrayList<Object>();
//					if (StaticContent.Up_Down.equals("ZK")) {
//						listData.add("ZK" + ZKmeter);
//						listData.add("K" + ZKmeter); //ChenLang modify 
//					}
//					if (StaticContent.Up_Down.equals("YK")) {
//						listData.add("YK" + ZKmeter);
					//	listData.add("K" + ZKmeter); //ChenLang modify
//					}
					listData.add(meter);
					listData.add(BH_content);
					listData.add(level_content);
					listData.add(c_content);
					listData.add(StaticContent.photo_frist_id);
					listData.add(check_pro);// 照片编号
					for (int i = 0; i < listData.size(); i++) {
						cv.put(ci[i], listData.get(i).toString());
						System.out.println(ci[i] + ":"
								+ listData.get(i).toString());
					}
					cv.put("task_id", StaticContent.update_id);
					cv.put("BHID",StaticContent.update_id + "_"+ DataProvider.GetMaxBHID());
					cv.put("BZ", civil_bz.getText().toString());
					cv.put("CHECKID", check_id);
					cv.put("CheckType", check_type);
					if (StaticContent.S_X.equals("S")) {
						cv.put("UP_DOWN", 0);
					}
					if (StaticContent.S_X.equals("X")) {
						cv.put("UP_DOWN", 1);
					}if (hava1.isChecked()) {
						involve=0;
					}if (hava2.isChecked()) {
					 involve=1;
					}
					cv.put( "involve", involve);
					boolean flag = DataProvider.ValidationCheckContentForCILIV(
							StaticContent.update_id, ud_type, check_id, -1);
					if (flag == true) {
						String er = mapUtil.getTJAddErro(check_id, -1);
						mapUtil.DialogErro(Roadbed.this, er);
					} else {
						db.insert("CILIV_CHECKCONTENT", cv);
//
//						Toast t = new Toast(Roadbed.this);
//						t.makeText(Roadbed.this, "添加成功", Toast.LENGTH_LONG)
//								.show();
						Toast.makeText(Roadbed.this, "添加成功",Toast.LENGTH_SHORT).show();
					}
					query();

					if (!(civil_bz.getTag() == null)) {
						int c = contentListData.size();
						String DQbhid = contentListData.get(c - 1).get_id().toString();
						String te = civil_bz.getTag().toString();
						String as[] = te.split(";");
						int si = as.length;
//						ArrayList<Integer> ci = new ArrayList<Integer>();
//						ArrayList<String> val = new ArrayList<String>();
						for (int i = 0; i < si; i++) {
							ArrayList<String> va = new ArrayList<String>();
							va.add(as[i]);
							String s1 = as[i].split(":")[0];
							int i1 = Integer.parseInt(s1);
							String s2 = as[i].split(":")[1];
							DataProvider.storeDQInfo(DQbhid, check_id, i1, s2);
							System.out.println(i1 + s2);
						}
					}}
				}
			}
		});
		check_formBtn = (Button) findViewById(R.id.Civil_Roadbed_FormBtn);
		check_formBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Roadbed.this, Chek_form.class);
				intent.putExtra("check_type", "土建结构");
				startActivity(intent);

			}
		});

	}

	public void query() {
		String sql3 = "select*from CILIV_CHECKCONTENT where belong_pro='"
				+ check_pro + "'  and task_id='" + StaticContent.update_id + "'and UP_DOWN='"+ud_type+"' order by _id desc";

		Cursor c3 = db.query(sql3);
		if (!(c3.getCount() == 0)) {
			if (c3.moveToFirst()) {
				contentListData = new ArrayList<CivilContentE>();
				do {
					CivilContentE ce = new CivilContentE();
					ce.set_id(c3.getInt(0));
					StaticContent.bh_id = c3.getInt(0) + "";
					ce.setCheck_data(c3.getString(1));
					ce.setCheck_position(c3.getString(2));
					ce.setMileage(c3.getString(3));
					ce.setJudge_level(c3.getString(4));
					ce.setLevel_content(c3.getString(5));
					ce.setBZ(c3.getString(13));
					ce.setPic_id(DB_Provider.getpic_id(StaticContent.bh_id));
					contentListData.add(ce);
				} while (c3.moveToNext());
				c3.close();
			}
			adapter4 = new RoadbedResultAdapter(contentListData, this);
			PorCheckResultListview.setAdapter(adapter4);
			adapter4.notifyDataSetChanged();
		}
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		query();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Roadbed.this,Task_info.class));
			StaticContent.listselectindex = 0;
			StaticContent.listpositonindex = 0;
			finish();
			break;
			
		default:
			break;
		}
		
		return false;
	}
	
}
