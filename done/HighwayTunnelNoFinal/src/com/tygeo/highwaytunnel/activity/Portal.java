package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;

import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Util.mapUtil;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.CivilContentAdapter;
import com.tygeo.highwaytunnel.adpter.CivilLevelAdapter;
import com.tygeo.highwaytunnel.adpter.CivilPorContentAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.Task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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

public class Portal extends Activity {
	// 洞口 洞门显示
	ListView PortalListview1, PortalListview3, PortalListview2,
			PorCheckResultListview;
	ArrayList<String> s, s3;
	ArrayList<CivilContentE> contentListData;
	String c_content = "", BH_position, BH_content;
	Button commitBtn, photoBtn, photoDel;
	DBHelper db;
	int  PDok;//判读描述存在
	int check_id, check_itemId, positionid, checktypeid, cs;
	ArrayList<String> s2;
	ArrayList<Integer> CheckcontentId, checkItemId;
	String check_type = "日常检查";
	String[] level = { "S", "B", "A" };
	CivilContentAdapter adapter4;
	String index, DQBZ;
	int involve;
	RadioButton rb1, rb2, rb3, hava1, hava2;
	Button bt1, bt2, bt3, check_formBtn;
	CivilPorContentAdapter adapter1,adapter2;
	CivilLevelAdapter adapter3;
	private EditText edt1,edt2;
	private TextView ptext;
//	SimpleStringAdapter ;
	RadioGroup Civil_RadioGp_2,Civil_RadioGp;
	String CCinfo, POSITIONNAME;
	private int clickPosition = -1, selectid = -1, ud_type;
	Task task;
	String check_pro, select_content, level_content;
	EditText civil_bz;
	String sql1;
	String[] ci = {"mileage","check_position", "level_content", "judge_level",
			"check_data", "pic_id", "belong_pro", "task_id " };

	/*
	 * check_data VARCHAR( 20 ), check_position VARCHAR( 20 ), mileage VARCHAR(
	 * 20 ), judge_level VARCHAR( 20 ), level_content VARCHAR( 20 ), bh_pic
	 * VARCHAR( 20 ), pic_id INTEGER, belong_pro VARCHAR( 50 )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.civil_portal);
		InfoApplication.getinstance().addActivity(this);
		StaticContent.photo_frist_id = "p" + DB_Provider.getImageID();
		// 打开数据库
		s2 = new ArrayList<String>();
		s2.add("进口");
		s2.add("出口");
		db = new DBHelper(StaticContent.DataBasePath);
		// 实体化
		rb1 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn1);
		// bt1=(Button)findViewById(R.id.Civil_Config_Level_List_Btn1);
		rb2 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn2);
		// bt2=(Button)findViewById(R.id.Civil_Config_Level_List_Btn2);
		rb3 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn3);
		hava1 = (RadioButton) findViewById(R.id.Civil_Config_HAVA_1);
		hava2 = (RadioButton) findViewById(R.id.Civil_Config_HAVA_2);
		rb1.setClickable(false);
		rb2.setClickable(false);
		rb3.setClickable(false);
		Civil_RadioGp=(RadioGroup) findViewById(R.id.Civil_RadioGp);
		Civil_RadioGp_2 = (RadioGroup) findViewById(R.id.Civil_RadioGp_2);
		// bt3=(Button)findViewById(R.id.Civil_Config_Level_List_Btn3);
		civil_bz = (EditText) findViewById(R.id.civil_BZ_Edit);
		civil_bz.setInputType(InputType.TYPE_NULL);
		civil_bz.setHint("请输入");
		civil_bz.setText("");
		civil_bz.setClickable(false);
		ptext=(TextView)findViewById(R.id.Portal_RoadBed_Edit1);
		edt1 = (EditText) findViewById(R.id.Civil_Portal_Edit1);
		edt2 = (EditText) findViewById(R.id.Civil_Portal_Edit2);
		if (StaticContent.S_X.equals("S")) {
			ud_type = 0;
			ptext.setText("("
					+ StaticContent.TaskStartMile + "/"
					+ StaticContent.TaskEndMile+ ")");
		} else {
			ud_type = 1;
			ptext.setText("K" + "("
					+ StaticContent.TaskStartMile + "/"
					+ StaticContent.TaskEndMile + ")");
		}
		// civil_bz.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {

		//
		// return false;
		// }
		// });

		BH_position = "进口";
		PDok=0;
		if (((InfoApplication) getApplication()).getTask().getCivil_check()
				.equals("定期检查")) {
			// this.setContentView(R.layout.civil_por_dq);
//			rb1.setVisibility(View.GONE);
			// bt1.setVisibility(View.GONE);
			check_type = "定期检查";
			
		}
		
		// 上下行
		if (StaticContent.S_X.equals("S")) {
			ud_type = 0;
		}
		if (StaticContent.S_X.equals("X")) {
			ud_type = 1;
		}

//		Civil_RadioGp_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//		
//				if (checkedId==1) {
//					rb1.setClickable(false);
//					rb2.setClickable(false);
//					rb3.setClickable(false);
//				}
//				
//			}
//		});
		
		hava2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				civil_bz.setClickable(false);
				rb1.setChecked(false);
				rb2.setChecked(false);
				rb3.setChecked(false);
				rb1.setClickable(false);
				rb2.setClickable(false);
				rb3.setClickable(false);
				rb1.setTextColor(Color.GRAY);
				rb2.setTextColor(Color.GRAY);
				rb3.setTextColor(Color.GRAY);
				PDok=0;
			}
		});
		
		hava1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				civil_bz.setClickable(true);
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
					 PDok=1;
					 }	
			}
		});

		// photoBtn=(Button)findViewById(R.id.Civil_Por_PhotoBtn);
		commitBtn = (Button) findViewById(R.id.Civil_Por_CommitBtn);
		PortalListview1 = (ListView) findViewById(R.id.Civil_portal_listview1);
		PortalListview2 = (ListView) findViewById(R.id.Civil_portal_listview2);
		PorCheckResultListview = (ListView) findViewById(R.id.Civil_Por_CheckResult_Listview);
		// 适配器
		// 查询检查内容
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		index = budle.getString("name");
//		String checkt = ((InfoApplication) getApplication()).getTask()
//				.getCivil_check();
		
		// index=StaticContent.indexTag;
		// SharedPreferences sharedata = getSharedPreferences("data", 0);
		// String data = sharedata.getString("tag", null);
		// Log.v("cola","tag="+data);
		// index=data;
		System.out.println(index);
		task = ((InfoApplication) getApplication()).getTask();
		CCinfo = task.getCivil_check();

		// 'and check_type='"+CCinfo+"'
		if (index.equals("DK")) {
			check_pro = "洞口";
			sql1 = "select check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 0;
			c_content = "边坡危石";
		}
		if (index.equals("DM")) {
			check_pro = "洞门";
			sql1 = "select  check_content,CHECKID,CHECKITEMID from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + check_type + "'";
			StaticContent.tabhost_id = 1;
			c_content = "结构开裂";
		}
		StaticContent.BH_index_name = check_pro;
		// 查询出检查内容

		Cursor c = db.query(sql1);
		if (c.moveToFirst()) {
			s = new ArrayList<String>();
			// 存放检查项目id的list
			CheckcontentId = new ArrayList<Integer>();
			checkItemId = new ArrayList<Integer>();
			do {
				select_content = c.getString(0);
				CheckcontentId.add(c.getInt(1));
				checkItemId.add(c.getInt(2));
				s.add(select_content);

			} while (c.moveToNext());
			adapter1 = new CivilPorContentAdapter(s, this);
			c.close();
		}
		check_id = CheckcontentId.get(0);
		ArrayList<String> p_level = DB_Provider.GetFirstP_level(check_pro,check_id);
		rb1.setText(p_level.get(0));
		rb2.setText(p_level.get(1));
		rb3.setText(p_level.get(2));
//		if (check_type.equals("定期检查")) {
//			rb2.setChecked(true);
//		} else {
//			rb1.setChecked(true);
//
//		}

		PortalListview1.setAdapter(adapter1);

//		PortalListview1.setItemChecked(0, true);

		// PortalListview1.get
		// adapter1.notifyDataSetChanged();
		// listview1的点击监听
		cs = PortalListview1.getCheckedItemPosition();
		PortalListview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
		
				adapter1.setSelectPosition(position);
				
				adapter1.notifyDataSetChanged();

				System.out.println(cs);
				System.out.println(position);
				// cs = PortalListview1.getCheckedItemPosition();
				if (!(position == cs)) {
					civil_bz.setTag(null);
					civil_bz.setText("");
				}
				// view.setBackgroundResource(R.drawable.blue);
				StaticContent.listselectindex = position;
				c_content = s.get(position);
				check_id = CheckcontentId.get(position);// 检查内容id
				check_itemId = checkItemId.get(position);// 项目id
				// 所选病害名称
				StaticContent.BH_p_name = c_content;

				sql1 = "select CheckItemDesc from CheckItemDescInfos where CheckId='"
						+ check_id + "'";
				Cursor c1 = db.query(sql1);
				if (c1.moveToFirst()) {

					s3 = new ArrayList<String>();
					do {
						s3.add(c1.getString(0));

					} while (c1.moveToNext());

					// adapter3 = new CivilLevelAdapter(s3,
					// Portal.this,Portal.this);
					// PortalListview3.setAdapter(adapter3);
					rb1.setText(s3.get(0));
					rb2.setText(s3.get(1));
					rb3.setText(s3.get(2));
				}
			}
		});
		
		query();

		// 点击监听第2个listview
		adapter2 = new CivilPorContentAdapter(s2, Portal.this);
		
		PortalListview2.setAdapter(adapter2);
		// adapter2.getView(0, , parent);

		PortalListview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
		
				
				selectid = position;
				
                adapter2.setSelectPosition(position);
                adapter2.notifyDataSetChanged();
				adapter2.notifyDataSetChanged();
				StaticContent.listpositonindex = position;
				view.setBackgroundResource(R.drawable.blue);
				BH_position = s2.get(position);

			}
		});

		// 点击监听第3个listview

		// PortalListview3.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long id) {

		//
		// try{
		// BH_content=s3.get(position);
		// System.out.println(position);
		// level_content=level[position];
		// PortalListview3.setItemChecked(position, true);
		// }catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// 检查结果显示
		
		if (check_type.equals("日常检查")) {
			civil_bz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
			
					if (hava2.isChecked()) {
//						Toast to = new Toast(Portal.this);
//						to.makeText(Portal.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT)
//								.show();
						Toast.makeText(Portal.this,  "请选择判断描述\"有\"后操作",Toast.LENGTH_SHORT).show();
					}else{
					final EditText editText = new EditText(Portal.this);
					editText.setText(civil_bz.getText().toString());
					editText.setGravity(Gravity.TOP);
					editText.setWidth(300);
					editText.setHeight(200);

					AlertDialog dialog = new AlertDialog.Builder(Portal.this)
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
//						Toast to = new Toast(Portal.this);
//						to.makeText(Portal.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT).show();
						Toast.makeText(Portal.this,  "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT).show();
					}else{
					mapUtil.DialogBHInfoInput(Portal.this, check_id);
				
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
		
				String et1 = edt1.getText().toString().trim();
				String et2 = edt2.getText().toString().trim();
				int meter=0;
				if(!"".equals(et1) && !"".equals(et2)){
					meter= Integer.parseInt(et1)*1000+ Integer.parseInt(et2);
				}
				if (c_content == null) {
//					Toast to = new Toast(Portal.this);
//					to.makeText(Portal.this, "请选择有具体病害描述的选项", Toast.LENGTH_SHORT)
//							.show();
					Toast.makeText(Portal.this,  "请选择有具体病害描述的选项", Toast.LENGTH_SHORT).show();
				}
				// else if (selectid == -1) {
				// Toast to = new Toast(Portal.this);
				// to.makeText(Portal.this, "请选择病害位置", Toast.LENGTH_SHORT)
				// .show();
				// }

				else if (et1.equals("") ||et1.equals("")) {
					Toast.makeText(Portal.this, "请输入桩号", Toast.LENGTH_SHORT).show();
				}
				else {
//				String	t1 = edt1.getText().toString().trim();
//				String	t2 = edt2.getText().toString().trim();

					level_content = "";
					BH_content = "无";
					if (rb1.isChecked()) {
						level_content = "S";
						BH_content = rb1.getText().toString();
						
					}
					if (rb2.isChecked()) {
						BH_content = rb2.getText().toString();
						level_content = "B";
					}
					if (rb3.isChecked()) {
						BH_content = rb3.getText().toString();
						level_content = "A";
					}
					if (check_type.equals("日常检查") && check_pro.equals("洞口")) {
						checktypeid = 5;
						positionid = DB_Provider.getCheckContentId(checktypeid,
								BH_position + "洞洞口");
					} else if (check_type.equals("日常检查")
							&& check_pro.equals("洞门")) {
						checktypeid = 6;
						positionid = DB_Provider.getCheckContentId(checktypeid,
								BH_position + "洞洞门");
					} else if (check_type.equals("定期检查")
							&& check_pro.equals("洞口")) {
						checktypeid = 13;
						positionid = DB_Provider.getCheckContentId(checktypeid,
								BH_position + "洞洞口");
					} else if (check_type.equals("定期检查")
							&& check_pro.equals("洞门")) {
						checktypeid = 14;
						positionid = DB_Provider.getCheckContentId(checktypeid,
								BH_position + "洞洞门");
					};
					ContentValues cv = new ContentValues();
					ArrayList<Object> listData = new ArrayList<Object>();
//					if (StaticContent.Up_Down.equals("ZK")) {
//						listData.add("ZK" + t1+"+"+t2);
//						listData.add("K" + t1+"+"+t2); //ChenLang  modify
//					}
//					if (StaticContent.Up_Down.equals("YK")) {
//						listData.add("YK" + t1+"+"+t2);
//						listData.add("K" + t1+"+"+t2); //ChenLang modify
//					}
					listData.add(meter);
					listData.add(BH_position);
					listData.add(BH_content);
					listData.add(level_content);
					listData.add(c_content);
					listData.add(StaticContent.photo_frist_id);
					listData.add(check_pro);// 照片编号
					
					for (int i = 0; i < listData.size(); i++) {
						cv.put(ci[i], listData.get(i).toString());
						// System.out.println(ci[i] + ":" +
						// listData.get(i).toString());
					}
					cv.put("task_id", StaticContent.update_id);
					cv.put("BHID",StaticContent.update_id + "_"+ DataProvider.GetMaxBHID());
					cv.put("BZ", civil_bz.getText().toString());
					cv.put("CHECKID", check_id);
					cv.put("POSITIONID", positionid);
					cv.put("checktype", check_type);
					if (StaticContent.S_X.equals("S")) {
						cv.put("UP_DOWN", 0);
					}
					if (StaticContent.S_X.equals("X")) {
						cv.put("UP_DOWN", 1);

					}
					if (hava1.isChecked()) {
						involve = 0;
					}
					if (hava2.isChecked()) {
						involve = 1;
					}
					cv.put("involve", involve);
					
					boolean flag = DataProvider.ValidationCheckContentForCILIV(
							StaticContent.update_id, ud_type, check_id,
							positionid);
					if (flag == true) {
						String er = mapUtil.getTJAddErro(check_id, positionid);
						mapUtil.DialogErro(Portal.this, er);
					} else {
						db.insert("CILIV_CHECKCONTENT", cv);
//						Toast t = new Toast(Portal.this);
//						t.makeText(Portal.this, "添加成功", Toast.LENGTH_LONG)
//								.show();
						Toast.makeText(Portal.this, "添加成功", Toast.LENGTH_SHORT).show();
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

					}
				}
			}
		});

	}

	public void query() {
		String sql3 = "select*from CILIV_CHECKCONTENT where belong_pro='"
				+ check_pro + "'and task_id='" + StaticContent.update_id
				+ "'  and  UP_DOWN='" + ud_type + "'order by _id desc";
		System.out.println(sql3);
		Cursor c3 = db.query(sql3);
		if (!(c3.getCount() == 0)) {
			if (c3.moveToFirst()) {
				contentListData = new ArrayList<CivilContentE>();
				do {
					CivilContentE ce = new CivilContentE();
					ce.set_id(c3.getInt(0));
					StaticContent.bh_id = c3.getInt(0) + "";
					System.out.println(c3.getInt(0));
					ce.setCheck_data(c3.getString(1));
					ce.setCheck_position(c3.getString(2));
					ce.setJudge_level(c3.getString(4));
					ce.setLevel_content(c3.getString(5));
					ce.setBZ(c3.getString(13));
					ce.setPic_id(DB_Provider.getpic_id(StaticContent.bh_id));
					contentListData.add(ce);
					
				} while (c3.moveToNext());
				c3.close();
			}
			adapter4 = new CivilContentAdapter(contentListData, this);
			PorCheckResultListview.setAdapter(adapter4);
			adapter4.notifyDataSetChanged();
		}
			
		check_formBtn = (Button) findViewById(R.id.to_check_form);
		check_formBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

		
				Intent intent = new Intent(Portal.this, Chek_form.class);
				intent.putExtra("check_type", "土建结构");

				startActivity(intent);

			}
		});
		// 拍照功能
		// photoBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {

		// StaticContent.pic_id=index+"";
		// startActivity(new Intent(Portal.this,PhotoShow.class));
		//
		// }
		// });

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
			// startActivity(new Intent(Portal.this,Task_info.class));  
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
