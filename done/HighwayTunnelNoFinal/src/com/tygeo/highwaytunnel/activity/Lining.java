package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.TY.TYBHMapForAndroid.BHDisplay;
import com.TY.bhgis.Carto.IMap;
import com.TY.bhgis.Carto.Map;
import com.TY.bhgis.Controls.BottomView;
import com.TY.bhgis.Controls.DrawPointTool;
import com.TY.bhgis.Controls.DrawPolygonTool;
import com.TY.bhgis.Controls.DrawPolylineTool;
import com.TY.bhgis.Controls.IInfoToolListener;
import com.TY.bhgis.Controls.MapControl;
import com.TY.bhgis.Controls.MapView;
import com.TY.bhgis.Controls.SelectBHTool;
import com.TY.bhgis.Controls.TopView;
import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.Util.Type;
import com.TY.bhgis.Util.mapUtil;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.CivilLevelAdapter;
import com.tygeo.highwaytunnel.adpter.CivilPorContentAdapter;
import com.tygeo.highwaytunnel.adpter.SimpleStringAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilContentE;
//import com.tygeo.highwaytunnel.adpter.liningAdapter.PhotoButtonListener;
//import com.tygeo.highwaytunnel.adpter.liningAdapter.listviewButtonListener;

public class Lining extends Activity {
	
	// 衬砌显示
	String Zkmeter;
	ListView LiningListview1, LiningListview2, LiningListview3,
			PorCheckResultListview;
	ArrayList<String> s, s3;
	ArrayList<CivilContentE> contentListData;
//	ArrayList<CivilContentE> contentListData =new ArrayList<CivilContentE>(); //ChenLang 修改
	String c_content = "裂缝",  BH_position,  BH_content;
	String ci[] = { "mileage", "check_data", "check_position", "level_content",
			"judge_level", "pic_id", "belong_pro" };
	DBHelper db;
	EditText tv1, tv2;
	String up_down;
	ArrayList<Integer> CheckcontentId;
	int check_id, check_itemId, positionid, checktypeid, end_melete, ud_type;// 0,表示上行
	int meter, melete_count;
	EditText civil_bz;
	int selectid = -1, cs;
	boolean isfirt = true;
	ArrayList<String> s2;
	int involve;
	String[] args = { "拱顶", "左拱腰", "右拱腰", "左边墙", "右边墙" };
	// 结构裂缝
	byte[] type = { Type.JGLF, Type.JGCT, Type.JGQC, Type.JGBL, Type.SFDSL,
			Type.SFXSL, Type.LFDSL, Type.LFXSL, Type.MZGL, Type.GB };
	byte check_type;
	String index;
	int ps, begin_melete;
	CivilPorContentAdapter adapter1;
	TextView text;
	CivilLevelAdapter adapter3;
	liningAdapter adapter4;
	SimpleStringAdapter adapter2;
//	private int clickPosition = -1;
	private int  cqtype=0;
	String check_pro, select_content, level_content;
	String sql1;
	Button bt1, bt2, bt3, commitBtn, photoBtn, photoDelBtn, check_formBtn,
			lining_check_formBtn, lining_photoBtn;
	RadioButton rb1, rb2, rb3,hava1,hava2;
	RadioGroup Civil_RadioGp_2,Civil_RadioGp;
	MapView mapView;
	TopView topView;
	ListView bhlistView;
	BottomView bottomView;
	DBHelper dbHelper;
	private ArrayList<Integer> bhTypeList=new ArrayList<Integer>();
//	private ArrayList<Integer> cqTypeList=new ArrayList<Integer>();
	LinearLayout maplayout, mainLayout;
	Context context;
	int melete_num;
	String checktype = "日常检查";
	private int checkType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lining);
		InfoApplication.getinstance().addActivity(this);
		// 打开数据库
		db = new DBHelper(StaticContent.DataBasePath);
		dbHelper = new DBHelper(StaticContent.DataBasePath);
		// 实体化listview
		BH_position = "拱顶";
		LiningListview1 = (ListView) findViewById(R.id.Civil_lining_listview1);
		LiningListview2 = (ListView) findViewById(R.id.Civil_lining_listview2);
		PorCheckResultListview = (ListView) findViewById(R.id.Civil_lining_CheckResult_Listview);
		text = (TextView) findViewById(R.id.lining_SX);
		tv1 = (EditText) findViewById(R.id.Civil_lining_Edit1);
		tv2 = (EditText) findViewById(R.id.Civil_lining_Edit2);
		tv1.setInputType(InputType.TYPE_CLASS_NUMBER);
		tv2.setInputType(InputType.TYPE_CLASS_NUMBER);
		commitBtn = (Button) findViewById(R.id.Civil_lining_CommitBtn);
		rb1 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn01);
		// bt1 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn1);
		rb2 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn02);
		// bt2 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn2);
		rb3 = (RadioButton) findViewById(R.id.Civil_Config_Level_List_Rbtn03);
		// bt3 = (Button) findViewById(R.id.Civil_Config_Level_List_Btn3);
		civil_bz = (EditText) findViewById(R.id.civil_BZ_Edit);
		hava1= (RadioButton) findViewById(R.id.Civil_Config_HAVA_1);
		hava2= (RadioButton) findViewById(R.id.Civil_Config_HAVA_2);
		Civil_RadioGp=(RadioGroup)findViewById(R.id.Civil_RadioGp);
		Civil_RadioGp_2=(RadioGroup)findViewById(R.id.Civil_RadioGp_2);
		civil_bz.setHint("请输入");
		civil_bz.setInputType(InputType.TYPE_NULL);
		civil_bz.setText("");
		lining_check_formBtn = (Button) findViewById(R.id.Civil_lining_liningRecordBtn);
		lining_photoBtn = (Button) findViewById(R.id.Civil_lining_LiningBHphotoBtn);
		if (((InfoApplication) getApplication()).getTask().getCivil_check().equals("定期检查")) {
			// this.setContentView(R.layout.lining_dq);
//			rb1.setVisibility(View.GONE);
			// bt1.setVisibility(View.GONE);
//			lining_photoBtn.setVisibility(View.INVISIBLE);
			checktype = "定期检查";
			checkType=1;
		}
		check_formBtn = (Button) findViewById(R.id.Civil_lining_CheckRecordBtn);
		rb1.setClickable(false);
		rb2.setClickable(false);
		rb3.setClickable(false);
		
		hava2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
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
			
			rb1.setClickable(true);
			rb2.setClickable(true);
			rb3.setClickable(true);
			rb1.setTextColor(Color.BLACK);
			rb2.setTextColor(Color.BLACK);
			rb3.setTextColor(Color.BLACK);
			 if (checktype.equals("定期检查")) {
				 
				Civil_RadioGp.check(1); 
				 rb2.setChecked(true);
				 } else {
				 Civil_RadioGp.check(0);
				 rb1.setChecked(true);
				 
				 }	
		}
	});

		// 适配器
		// 查询检查内容
		Intent intent = getIntent();
		Bundle budle = intent.getExtras();
		index = budle.getString(("name"));
		query();
		StaticContent.tabhost_id = 1;
		// index=StaticContent.indexTag;
		// SharedPreferences sharedata = getSharedPreferences("data", 0);
		// String data = sharedata.getString("tag", null);
		// Log.v("cola","tag="+data);
		// index=data;
		System.out.println(index);
		mainLayout = (LinearLayout) findViewById(R.id.lining_main);
		maplayout = (LinearLayout) findViewById(R.id.BHMain_layout);
		mainLayout.setVisibility(View.VISIBLE);
		maplayout.setVisibility(View.INVISIBLE);
		mapView = (MapView) findViewById(R.id.mapView);
		topView = (TopView) findViewById(R.id.topView);
		bottomView = (BottomView) findViewById(R.id.bottomView);
		context = this;
		DataProvider.PROJECTID = StaticContent.update_id;
		DataProvider.DIRECTION = ud_type;
		if (StaticContent.S_X.equals("S")) {
			up_down = "上行";
			begin_melete =StaticContent.BeginMile;
			melete_count = StaticContent.melete_count;
			end_melete = StaticContent.EndMile;
			ud_type = 0;
			text.setText( "("
					+ StaticContent.TaskStartMile + "/"
					+ StaticContent.TaskEndMile + ")");
		} else {
			up_down = "下行";
			begin_melete = StaticContent.BeginMile;
			melete_count = StaticContent.melete_count;
			end_melete = StaticContent.EndMile;
			ud_type = 1;
			text.setText("("+ StaticContent.TaskStartMile + "/"+ StaticContent.TaskEndMile + ")");
		}
		bhlistView = (ListView) findViewById(R.id.ListBH);
		bhlistView.setOnItemClickListener(new bhlistItemsListener());
		// ImageView imageView = (ImageView) findViewById(R.id.imageView_home);
		lining_photoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StaticContent.islining = 1;
				mainLayout.setVisibility(View.GONE);
				maplayout.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(Lining.this);
				final View view = inflater.inflate(R.layout.civil_config, null);
				ImageButton bt = (ImageButton) view.findViewById(R.id.Civil_Config_BackBtn);
				bt.setVisibility(View.INVISIBLE);
//				System.out.println(bt.getText());
				SimpleAdapter simpleAdapter = new SimpleAdapter(context,getDatabhMap(), R.layout.bhlist, 
						new String[] {"title", "image" }, new int[] {
								R.id.ItemBHName, R.id.ItemBHImage });
				simpleAdapter.setViewBinder(new MyViewBinder());
				bhlistView.setAdapter(simpleAdapter);
//				int current = DB_Provider.GetCurrentZh(StaticContent.update_id,ud_type);
				initMapLayout();
				System.out.println(StaticContent.check_for+ StaticContent.BeginMile+ StaticContent.Ymelete_count);
				try {
					initMap(StaticContent.check_for, begin_melete,melete_count, ud_type, StaticContent.update_id); //TODO 绘制图形学习
					intoMap(DB_Provider.GetCurrentZh(StaticContent.update_id,ud_type));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		if (index.equals("CQ")) {
			check_pro = "衬砌";
			sql1 = "select  check_content,CHECKID,CHECKITEMID,cqbhtype from CIVIL_CHECK_INFO where check_pro='"
					+ check_pro + "'and check_type='" + checktype + "'";
		}

		StaticContent.BH_index_name = check_pro;
//		String checkt = ((InfoApplication) getApplication()).getTask().getCivil_check();
		query();
		Cursor c = db.query(sql1);
		if (c.moveToFirst()) {
			s = new ArrayList<String>();
			CheckcontentId = new ArrayList<Integer>();
			do {
				select_content = c.getString(0);
				CheckcontentId.add(c.getInt(1));
				s.add(select_content);
				bhTypeList.add(c.getInt(3)); //如果checktype="定期检查",那么cqbhtype=0
			} while (c.moveToNext());
			adapter1 = new CivilPorContentAdapter(s, this);
			c.close();
		}
		check_id = CheckcontentId.get(0); 
		ArrayList<String> p_level = DB_Provider.GetFirstP_level(check_pro,check_id);
		rb1.setText(p_level.get(0));
		rb2.setText(p_level.get(1));
		rb3.setText(p_level.get(2));
		// melete_num=(Integer.parseInt(tv1.getText().toString()))*1000+Integer.parseInt(tv1.getText().toString());
		LiningListview1.setAdapter(adapter1);
		// listview1的点击监听
		cs = LiningListview1.getCheckedItemPosition();
	
		LiningListview1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long arg3) {
				adapter1.setSelectPosition(position);
				adapter1.notifyDataSetChanged();
				if (!(position == cs)) {
					civil_bz.setTag(null);
					civil_bz.setText("");
				}
				ps = CheckcontentId.get(position);
				cqtype=bhTypeList.get(position);
				StaticContent.listselectindex = position;
				((ListView) parent).setTag(view);
				view.setBackgroundResource(R.drawable.blue);
				c_content = s.get(position);
				check_id = CheckcontentId.get(position);
				if (position < 9) {
					check_type = type[position];
				} else {
					check_type = 0;
				}
				StaticContent.BH_p_name = c_content;
				ArrayList<String> p_level = DB_Provider.GetFirstP_level(check_pro,check_id);
				rb1.setText(p_level.get(0));
				rb2.setText(p_level.get(1));
				rb3.setText(p_level.get(2));
			}
		});	
		
		s2 = new ArrayList<String>();
		s2.add("拱顶");
		s2.add("左拱腰");
		s2.add("右拱腰");
		s2.add("左边墙");
		s2.add("右边墙");
		
		adapter2 = new SimpleStringAdapter(s2, Lining.this);
		LiningListview2.setAdapter(adapter2);
		LiningListview2.setItemChecked(0, true);
		LiningListview2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {

				if (((ListView) parent).getTag() != null) {
					((View) ((ListView) parent).getTag()).setBackgroundDrawable(null);
				}
				selectid = position;
				((ListView) parent).setTag(view);
				StaticContent.listpositonindex = position;
				view.setBackgroundResource(R.drawable.blue);
				BH_position = args[position];
				adapter2.notifyDataSetChanged();
			}
		});
		
		if (checktype.equals("日常检查")) {
			civil_bz.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (hava2.isChecked()) {
//						Toast to = new Toast(Lining.this);
//						to.makeText(Lining.this, "请选择判断描述\"有\"后操作", Toast.LENGTH_SHORT)
//								.show();
						Toast.makeText(Lining.this, "请选择判断描述\"有\"后操作",Toast.LENGTH_SHORT).show();
					}else{
					final EditText editText = new EditText(Lining.this);
					editText.setText(civil_bz.getText().toString());
					editText.setGravity(Gravity.TOP);
					editText.setWidth(300);
					editText.setHeight(200);
					AlertDialog dialog = new AlertDialog.Builder(Lining.this)
							.setTitle("请输入备注")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,int which) {
											dialog.dismiss();
											civil_bz.setText(editText.getText().toString());				
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,int which) {
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
						Toast.makeText(Lining.this, "请选择判断描述\"有\"后操作",Toast.LENGTH_SHORT).show();
					}else{
					mapUtil.DialogBHInfoInput(Lining.this, check_id);
				 }
				}
			});
		}

		commitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String t1 = tv1.getText().toString().trim();
				String t2 = tv2.getText().toString().trim();
//				String Zkmeter ="K"+tv1.getText().toString() + "+"+ tv2.getText().toString();
				if (t1.equals("") || t2.equals("")) {
					Toast.makeText(Lining.this, "请输入完整桩号",Toast.LENGTH_SHORT).show();
					
				} else if (c_content == null) {
					Toast.makeText(Lining.this, "请选择有具体病害描述的选项",Toast.LENGTH_SHORT).show();
				}
				// else if (selectid == -1) {
				// Toast to = new Toast(Lining.this);
				// to.makeText(Lining.this, "请选择病害位置", Toast.LENGTH_SHORT)
				// .show();
				// }				
				else {
					meter = Integer.parseInt(tv1.getText().toString()) * 1000+ Integer.parseInt(tv2.getText().toString());
//					if (meter < begin_melete || meter > end_melete) {
//
//						Toast to = new Toast(Lining.this);
//						to.makeText(Lining.this, "请输入正确的桩号", Toast.LENGTH_SHORT)
//								.show();
//					} else {
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
						if (checktype.equals("定期检查")) {
							checkType=1;
							checktypeid = 9;
							positionid = DB_Provider.getCheckContentId(checktypeid, BH_position);
						} else if (checktype.equals("日常检查")) {
							checkType=0;
							checktypeid = 1;
							positionid = DB_Provider.getCheckContentId(checktypeid, BH_position);
						}
						
			        	if (c_content.equals("施工缝开裂")||c_content.equals("施工缝错位")||c_content.equals("衬砌表面剥落")) {
							ContentValues cv = new ContentValues();
							ArrayList<Object> listData = new ArrayList<Object>();
//							if (StaticContent.Up_Down.equals("ZK")) {
//								listData.add("K"+tv1.getText().toString()+"+"+tv2.getText().toString());
//							}
//							if (StaticContent.Up_Down.equals("YK")) {
//								listData.add(meter);
//							}
							listData.add(meter);//ChenLang modify
							listData.add(c_content);
							listData.add(BH_position);
							listData.add(BH_content);
							listData.add(level_content);
							listData.add(StaticContent.photo_frist_id);
							listData.add(check_pro);// 照片编号
							for (int i = 0; i < listData.size(); i++) {
								cv.put(ci[i], listData.get(i).toString());
								System.out.println(ci[i] + ":"+ listData.get(i).toString());
							}
							cv.put("task_id", StaticContent.update_id);
							cv.put("BHID", StaticContent.update_id + "_"	+ DataProvider.GetMaxBHID());
							cv.put("BZ", civil_bz.getText().toString());
							cv.put("CHECKID", check_id);
							cv.put("POSITIONID", positionid);
							cv.put("CheckType", checktype);
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
							boolean flag = DataProvider.ValidationCheckContentForCILIV(StaticContent.update_id, ud_type,check_id, positionid);
							if (flag == true) {
								String er = mapUtil.getTJAddErro(check_id,positionid);
								mapUtil.DialogErro(Lining.this, er);
							} else {
							 db.insert("CILIV_CHECKCONTENT", cv);
							 Toast.makeText(Lining.this, "添加成功", Toast.LENGTH_SHORT).show();
							}
//								db.insert("CILIV_CHECKCONTENT", cv);
//								Toast.makeText(Lining.this, "添加成功", Toast.LENGTH_SHORT).show();
							query();
						} else {
							try {
								boolean flag = DataProvider.ValidationCheckContentForCILIV(StaticContent.update_id,ud_type, check_id, positionid);
								if (flag == true) {
									String er = mapUtil.getTJAddErro(check_id,positionid);
									mapUtil.DialogErro(Lining.this, er);
								} else {
									DB_Provider.InsertCurrentZh(meter,StaticContent.update_id, ud_type);
									//StaticContent_check_for 检查方向进口或者出口,begin_melete开始里程,melete里程的数量,
									//up_type 0 表示上行 1 表示下行
									initMap(StaticContent.check_for,begin_melete, melete_count,ud_type, StaticContent.update_id);
									HighWayLining highWayLining = mapView.getMapControl().getMap().getMapLayer().getHighWayLining(meter);
//									mapUtil.addCQBH(ps, positionid, Zkmeter,level_content, BH_content, civil_bz.getText().toString(),highWayLining,cqtype,checkType);
									mapUtil.addCQBH(ps, positionid, meter,level_content, BH_content, civil_bz.getText().toString(),highWayLining,cqtype,checkType,check_id);
									Toast.makeText(Lining.this, "添加成功", Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							query();
						}
						if (!(civil_bz.getTag() == null)) {
							int c = contentListData.size();
							String DQbhid = contentListData.get(c - 1).get_id().toString();
							String te = civil_bz.getTag().toString();
							String as[] = te.split(";");
							int si = as.length;
//							ArrayList<Integer> ci = new ArrayList<Integer>();
//							ArrayList<String> val = new ArrayList<String>();
							for (int i = 0; i < si; i++) {
								ArrayList<String> va = new ArrayList<String>();
								va.add(as[i]);
								String s1 = as[i].split(":")[0];
								int i1 = Integer.parseInt(s1);
								String s2 = as[i].split(":")[1];
								DataProvider.storeDQInfo(DQbhid, check_id,i1, s2);
								System.out.println(i1 + s2);
							}
						}
//					}
				}
			}
		});

		// 检查记录表
		check_formBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Lining.this, Chek_form.class);
				intent.putExtra("check_type", "土建结构检查");
				startActivity(intent);
			}
		});
		
		lining_check_formBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Lining.this, Check_form_lining.class);
				intent.putExtra("check_type", "土建结构");
				startActivity(intent);
			}
		});
	}

	public void query() {
		String sql3 = "select*from CILIV_CHECKCONTENT where belong_pro='"
				+ check_pro + "' and task_id='" + StaticContent.update_id
				+ "'and UP_DOWN='" + ud_type + "'";
		System.out.println("sql3: " + sql3);
		Cursor c3 = db.query(sql3);
		if (!(c3.getCount() == 0)) {
			if (c3.moveToFirst()) {
				contentListData = new ArrayList<CivilContentE>();
				do {
					CivilContentE ce = new CivilContentE();
					ce.set_id(c3.getInt(0));
					StaticContent.bh_id = ce.get_id() + "";
					ce.setCheck_data(c3.getString(1));
					ce.setCheck_position(c3.getString(2));
//					int doc = Integer.parseInt(c3.getString(3));
//					int num_ = doc / 1000;
//					int _num = doc % 1000;
					ce.setMileage(c3.getString(3));
					ce.setJudge_level(c3.getString(4));
					ce.setLevel_content(c3.getString(5));
					ce.setBZ(c3.getString(13));
					ce.setPic_id(DB_Provider.getpic_id(StaticContent.bh_id));
					ce.setBHID(c3.getString(9));
					contentListData.add(ce);

				} while (c3.moveToNext());

				c3.close();
			}
			adapter4 = new liningAdapter(contentListData, this);
			PorCheckResultListview.setAdapter(adapter4);
			adapter4.notifyDataSetChanged();
		}
		// 跳转检查记录表
	}

	@Override
	protected void onResume() {
		super.onResume();
		query();
	}

	@Override
	protected void onDestroy() {
		
		if (this.mapView.getMapControl() != null) {
			this.mapView.getMapControl().closeMap();
			System.out.println("Map close");
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Lining.this, Task_info.class));
//			Intent intent = new Intent(Lining.this, Task_info.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			StaticContent.listselectindex = 0;
			StaticContent.listpositonindex = 0;
//			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return false;
	}

	void initMapLayout() {

		MapToolButtonListener mapToolButtonListener = new MapToolButtonListener();
		Button btnRingto = (Button) findViewById(R.id.ringto_button);
		btnRingto.setOnClickListener(mapToolButtonListener);
		
		// 平移
		Button mapToolpan = (Button) findViewById(R.id.pan);
		mapToolpan.setOnClickListener(mapToolButtonListener);

		// 选择
		Button mapToolSelect = (Button) findViewById(R.id.selectBH);
		mapToolSelect.setOnClickListener(mapToolButtonListener);
		
		// 删除病害
		Button mapToolDelete = (Button) findViewById(R.id.deleteBH);
		mapToolDelete.setOnClickListener(mapToolButtonListener);
		Button back = (Button) findViewById(R.id.bhback_button);
		Button checkform = (Button) findViewById(R.id.bhcheckform_button);
		
		checkform.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Lining.this, Check_form_lining.class);
				intent.putExtra("check_type", "土建结构");
				startActivity(intent);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mainLayout.setVisibility(View.VISIBLE);
				maplayout.setVisibility(View.GONE);
				if (mapView.getMapControl() != null) {
					mapView.getMapControl().closeMap();	
				}
				isfirt = false;
				query();
			}
		});
	}
		
	// private void init(String jcdirection, int startlczh, int mcount,
	// int currentlczh, String sxdirection, String taskid, boolean isFirst) {
	//
	// if (isFirst) {
	//
	// DataProvider.PROJECTID = taskid;
	// System.out.println(DataProvider.PROJECTID);
	// if (jcdirection.equals(Type.inhole)) {
	// Map.flag = true;
	// } else {
	// Map.flag = false;
	// }
	//
	// if (sxdirection.equals(Type.sx)) {
	// DataProvider.z_y = Type.z;
	// } else {
	// DataProvider.z_y = Type.y;
	// }
	//
	// int startringid = Integer.valueOf(startlczh);
	// int count = Integer.valueOf(mcount);
	//
	// int ringcount = count;
	//
	// float ringwidth = 1;
	// float ringlength = 16;
	// mapUtil.minLczh = startringid;
	// mapUtil.maxLczh = startringid + count;
	// if (currentlczh > mapUtil.maxLczh || mapUtil.minLczh > currentlczh) {
	// mapUtil.mLczh = startringid;
	// } else {
	// mapUtil.mLczh = currentlczh;
	// }
	//
	// if (startringid <= startringid + count && startringid >= 0) {
	//
	// mapView.getMapControl().loadMap(ringwidth, ringlength,
	// ringcount);
	// topView.getTopControl().loadTop(jcdirection);
	// bottomView.getBottomControl().loadBottom(jcdirection);
	// mapView.getMapControl().setPanTool();
	// mapView.getMapControl().setCustomDraw(
	// new BHDisplay(mapView.getMapControl()));
	// HighWayLining subwayRing = mapView.getMapControl().getMap()
	// .getMapLayer().getHighWayLining(mapUtil.mLczh);
	// float y = subwayRing.getGeometry().getEnvelope()
	// .getCenterPoint().getY();
	// float x = subwayRing.getGeometry().getEnvelope()
	// .getCenterPoint().getX();
	// if (Map.flag) {
	// if (mapUtil.mLczh < mapUtil.minLczh + 3) {
	// y = mapView.getMapControl().getFullExtent().getYMin()
	// + mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// } else if (mapUtil.mLczh > mapUtil.minLczh + count - 3) {
	// y = mapView.getMapControl().getFullExtent().getYMax()
	// - mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// }
	// } else {
	// if (mapUtil.mLczh < mapUtil.minLczh + 3) {
	// y = mapView.getMapControl().getFullExtent().getYMax()
	// - mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	//
	// } else if (mapUtil.mLczh > mapUtil.minLczh + count - 3) {
	//
	// y = mapView.getMapControl().getFullExtent().getYMin()
	// + mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// }
	// }
	//
	// mapView.getMapControl().getExtent()
	// .centerAt(new Point(x - 0.5f, y));
	// mapView.getMapControl().refresh();
	//
	// }
	// } else {
	// mapView.getMapControl().setCustomDraw(
	// new BHDisplay(mapView.getMapControl()));
	// mapView.getMapControl().setPanTool();
	// HighWayLining subwayRing = mapView.getMapControl().getMap()
	// .getMapLayer().getHighWayLining(currentlczh);
	// float y = subwayRing.getGeometry().getEnvelope()
	// .getCenterPoint().getY();
	// float x = subwayRing.getGeometry().getEnvelope()
	// .getCenterPoint().getX();
	// if (Map.flag) {
	// if (mapUtil.mLczh < mapUtil.minLczh + 3) {
	// y = mapView.getMapControl().getFullExtent().getYMin()
	// + mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// } else if (mapUtil.mLczh > mapUtil.maxLczh - 3) {
	// y = mapView.getMapControl().getFullExtent().getYMax()
	// - mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// }
	// } else {
	// if (mapUtil.mLczh < mapUtil.minLczh + 3) {
	// y = mapView.getMapControl().getFullExtent().getYMax()
	// - mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	//
	// } else if (mapUtil.mLczh > mapUtil.maxLczh - 3) {
	//
	// y = mapView.getMapControl().getFullExtent().getYMin()
	// + mapView.getMapControl().getInitExtent()
	// .getHeight() / 2;
	// }
	// }
	//
	// mapView.getMapControl().getExtent()
	// .centerAt(new Point(x - 0.5f, y));
	// mapView.getMapControl().refresh();
	// }
	// }
	
	
	/**
	 * @param jcdirection  检查方向进口或者出口
	 * @param startlczh		开始里程
	 * @param mcount		里程的数量,
	 * @param sxdirection 0  表示上行 1 表示下行
	 * @param taskid			 任务Id
	 */
	private void initMap(String jcdirection, int startlczh, int mcount,int sxdirection, String taskid) {
		if (this.mapView.getMapControl().getMap() == null) {
			DataProvider.PROJECTID = taskid;
			System.out.println(DataProvider.PROJECTID);
			if (jcdirection.equals(Type.inhole)) {
				Map.flag = true;
			} else {
				Map.flag = false;
			}
			if (sxdirection == Type.sx) {
				DataProvider.z_y = Type.z;
			} else {
				DataProvider.z_y = Type.y;
			}
			DataProvider.DIRECTION = sxdirection;
			int startringid = Integer.valueOf(startlczh);
			int count = Integer.valueOf(mcount);
			int ringcount = count;
			float ringwidth = 1;
			float ringlength = 16;
			mapUtil.minLczh = startringid;
			mapUtil.maxLczh = startringid + count;
			if (startringid <= startringid + count && startringid >= 0) {
				mapView.getMapControl().loadMap(ringwidth, ringlength,ringcount);
				topView.getTopControl().loadTop(jcdirection);
				bottomView.getBottomControl().loadBottom(jcdirection);
			}
		}
	}
	

	private void intoMap(int currentlczh) {
		if (currentlczh > mapUtil.maxLczh || mapUtil.minLczh > currentlczh) {
			mapUtil.mLczh = mapUtil.minLczh;
		} else {
			mapUtil.mLczh = currentlczh;
		}
		mapView.getMapControl().setCustomDraw(new BHDisplay(mapView.getMapControl()));
		mapView.getMapControl().setPanTool();
		HighWayLining subwayRing = mapView.getMapControl().getMap().getMapLayer().getHighWayLining(currentlczh);
		float y = subwayRing.getGeometry().getEnvelope().getCenterPoint().getY();
		float x = subwayRing.getGeometry().getEnvelope().getCenterPoint().getX();
		if (Map.flag) {
			if (mapUtil.mLczh < mapUtil.minLczh + 3) {
				y = mapView.getMapControl().getFullExtent().getYMin()
						+ mapView.getMapControl().getInitExtent().getHeight()
						/ 2;
			} else if (mapUtil.mLczh > mapUtil.maxLczh - 3) {
				y = mapView.getMapControl().getFullExtent().getYMax()
						- mapView.getMapControl().getInitExtent().getHeight()
						/ 2;
			}
		} else {
			if (mapUtil.mLczh < mapUtil.minLczh + 3) {
				y = mapView.getMapControl().getFullExtent().getYMax()
						- mapView.getMapControl().getInitExtent().getHeight()
						/ 2;

			} else if (mapUtil.mLczh > mapUtil.maxLczh - 3) {
				y = mapView.getMapControl().getFullExtent().getYMin()
						+ mapView.getMapControl().getInitExtent().getHeight()
						/ 2;
			}
		}
		
		mapView.getMapControl().getExtent().centerAt(new Point(x - 0.5f, y));
		mapView.getMapControl().refresh();
	}

	// 获取病害菜单数据
	private List<java.util.Map<String, Object>> getDatabhMap() {
		List<java.util.Map<String, Object>> list = new ArrayList<java.util.Map<String, Object>>();
		java.util.Map<String, Object> map = null;
		String sql = "select name,BHTYPE,IMAGE FROM BHTYPE order by bhtype";
		Cursor c = dbHelper.query(sql);
		if (c.moveToFirst()) {
			for (int j = 0; j < c.getCount(); j++) {
//			for (int j = 0; j < c.getCount()-3; j++) {  //Chen Lang modify
				String name = c.getString(0);
				int bhtype = c.getInt(1);
				byte[] in = c.getBlob(2);
				Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
				map = new HashMap<String, Object>();
				map.put("title", name);
				map.put("bhtype", bhtype);
				map.put("image", bitmap);
				list.add(map);
				c.moveToNext();
			}
		}
		c.close();
		dbHelper.close();
		return list;
	}

	private void setBHTool(int bhtype) {
		switch (bhtype) {
		case Type.JGLF: // 结构裂缝
			mapView.getMapControl().setCurrentTool(new DrawPolylineTool(mapView.getMapControl(), Type.JGLF,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.JGCT:// 结构错台
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.JGCT,checkType));
			InfoApplication.getInstance().setUserdata(context);

			break;
		case Type.JGQC:// 结构起层
			mapView.getMapControl().setCurrentTool(new DrawPolygonTool(mapView.getMapControl(), Type.JGQC,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.JGBL:// 结构剥落
			mapView.getMapControl().setCurrentTool(new DrawPolygonTool(mapView.getMapControl(), Type.JGBL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.SFDSL:// 三缝点渗漏
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.SFDSL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.SFXSL:// 三缝线渗漏
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.SFXSL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.LFDSL:// 裂缝点渗漏
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.LFDSL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.LFXSL:// 裂缝线渗漏
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.LFXSL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.MZGL:// 面状渗漏
			mapView.getMapControl().setCurrentTool(new DrawPolygonTool(mapView.getMapControl(), Type.MZGL,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		case Type.GB:// 挂冰
			mapView.getMapControl().setCurrentTool(new DrawPointTool(mapView.getMapControl(), Type.GB,checkType));
			InfoApplication.getInstance().setUserdata(context);
			break;
		default:
			break;
		}
	}

	class MapToolButtonListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.pan:
				mapView.getMapControl().setPanTool();
				break;
				
			case R.id.ringto_button:

				LayoutInflater factory = LayoutInflater.from(InfoApplication.getInstance());
				final View textEntryView = factory.inflate(R.layout.dialogringto, null);
				((TextView) textEntryView.findViewById(R.id.tv_lczh)).setText(DataProvider.z_y + "K");
				HighWayLining[] highWayLinings = mapView.getMapControl().getMap().getMapLayer().getLinings();
				HighWayLining highWayLining = mapUtil.getHighWayLining(
						highWayLinings, mapView.getMapControl().getExtent()
								.getCenterPoint(), mapView.getMapControl()
								.getMap());
				int lczh = highWayLining.getLczh();
				int num_ = lczh / 1000;
				int _num = lczh % 1000;
                
				((EditText) textEntryView.findViewById(R.id.editTextBigLC))
						.setText(String.valueOf(num_));
				((EditText) textEntryView.findViewById(R.id.editTextSmallLC))
						.setText(String.valueOf(_num));

				AlertDialog dlg = new AlertDialog.Builder(context)
						.setTitle("输入里程")
						.setView(textEntryView)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										EditText editTextBigLC = (EditText) textEntryView
												.findViewById(R.id.editTextBigLC);
										String biglc = editTextBigLC.getText()
												.toString();
										EditText editTextSmallLC = (EditText) textEntryView
												.findViewById(R.id.editTextSmallLC);
										String smalllc = editTextSmallLC
												.getText().toString();

										HighWayLining highWayLining = mapView
												.getMapControl()
												.getMap()
												.getMapLayer()
												.getHighWayLining(mapUtil.getLczh(Integer.valueOf(biglc),Integer.valueOf(smalllc)));
										if (highWayLining == null) {
											mapUtil.disToast("没有找到该里程",
													InfoApplication
															.getInstance());
										} else {

											highWayLining.getGeometry().recalcEnvelope();
											IEnvelope envelope = highWayLining.getGeometry().getEnvelope();
											mapView.getMapControl().getExtent().centerAt((IPoint) new Point(
																	(mapView.getMapControl().getExtent().getCenterPoint().getX()),
																	(envelope.getYMax() - envelope.getYMin())/ 2+ envelope.getYMin()));
											mapView.getMapControl().refresh();
										}
									}
								}).setNegativeButton("取消", null).create();
				try {
					dlg.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.selectBH:
				mapView.getMapControl().setCurrentTool(
						new SelectBHTool(mapView.getMapControl(),
								new IInfoToolListener() {

									@Override
									public void notify(
											MapControl paramMapControl,
											Vector<IBH> bhs) {
									}
								}));
				break;
			case R.id.deleteBH:
				factory = LayoutInflater.from(InfoApplication.getInstance());
				View view = factory.inflate(R.layout.dialogdisplayinfo, null);
				TextView tv = (TextView) view.findViewById(R.id.tv_info);
				tv.setText("确定删除选中的病害？");
				dlg = new AlertDialog.Builder(context)
						.setTitle("警告")
						.setView(view)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,int arg1) {
										IBH[] bhs = mapView.getMapControl().getSelectionBHs();
										if (bhs != null) {
											IMap map = mapView.getMapControl().getMap();
											for (int i = 0; i < bhs.length; i++) {
												IBH bh = bhs[i];
												IBHClass bhcClass = map.getBHClass(bh.getBHType());
												bhcClass.delete(bh);
											}
											clearSelect();
											mapView.getMapControl().repaint();
											// deleteOrselect(mapView
											// .getMapControl());
											mapUtil.disStatus("完成病害删除！ ");
										}
									}
								}).setNegativeButton("取消", null).create();
				try {
					if (mapView.getMapControl().getSelectionBHs() != null&& mapView.getMapControl().getSelectionBHs().length > 0) {
						dlg.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	// 清空选中的病害
	private void clearSelect() {
		mapView.getMapControl().setSelectionBHs(null);
		mapView.getMapControl().setSelectDraw(null);
		mapView.getMapControl().repaint();
	}

	class bhlistItemsListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			int bhtype = (Integer) (getDatabhMap().get(arg2)).get("bhtype");
			
			setBHTool(bhtype);

		}
	}

	// 将bitmap类型加到viewlist控件
	class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}
	
	
	
	
	class liningAdapter extends BaseAdapter {
		// 查询内容适配器
		private List<CivilContentE> list;
//		private int selectItem = -1;
		private LayoutInflater inflater;
		private Context ctx;
		String p_name;
		ResultComp comp;
		int gid;
		DBHelper db = new DBHelper(StaticContent.DataBasePath);

		public liningAdapter(List<CivilContentE> list, Context ctx) {
			this.list = list;
			this.ctx = ctx;
			this.inflater = LayoutInflater.from(ctx);
   
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			comp = null;
//			int p = position;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lining_adapter_list, null);
				comp = new ResultComp();
				comp.text1 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT1);
				comp.text2 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT2);
				comp.text3 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT3);
				comp.text4 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT4);
				comp.text5 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT5);
				comp.text6 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT6);
				comp.text7 = (TextView) convertView.findViewById(R.id.Civil_lining_CheckResultT7);
				comp.button1 = (Button) convertView	.findViewById(R.id.Civil_lining_CheckResultBtn);
				comp.button2 = (Button) convertView.findViewById(R.id.Civil_lining_PhotoBtn);
				comp.button1.setTag(position);
				comp.button2.setTag(position);

				convertView.setTag(comp);
			} else {
				comp = (ResultComp) convertView.getTag();
			}
			comp.text1.setText(list.get(position).getMileage());
			comp.text2.setText(list.get(position).getCheck_data());
			comp.text3.setText(list.get(position).getCheck_position());
			comp.text4.setText(list.get(position).getLevel_content());
			comp.text5.setText(list.get(position).getJudge_level());
			comp.text6.setText((list.get(position).getPic_id()));
			comp.text7.setText(list.get(position).getBZ());
			comp.button1.setOnClickListener(new listviewButtonListener(position));
			comp.button2.setOnClickListener(new PhotoButtonListener(position));
			return convertView;
		}
		
		public final class ResultComp {
			public TextView text1;
			public TextView text2;
			public TextView text3;
			public TextView text4;
			public TextView text5;
			public TextView text6;
			public TextView text7;
			public Button button1, button2;
		}

		class listviewButtonListener implements View.OnClickListener {
			private int position;

			public listviewButtonListener(int i) {
				position = i;
			}

			@Override
			public void onClick(View v) {
				
				if (v.getId() == comp.button1.getId()) {
					AlertDialog dialog = new AlertDialog.Builder(ctx)
							.setTitle("确定删除？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
											gid = list.get(position).get_id();
											mapUtil.deleteCQBH(list.get(position).getBHID());
											String sql = "delete from CILIV_CHECKCONTENT where _id ='"+ gid + "'";
											String sql2 ="DELETE FROM CIVIL_REG_CHECK_INFO WHERE BHID='"+gid+"'"; //ChenLang modify 删除病害描述表
											System.out.println(gid); 
											System.out.println(sql);
											try{
												db.execSql(sql);
												db.execSql(sql2);										
											}catch(Exception e){
												e.printStackTrace();
											}finally{
												if(db!=null){
												db.close();	
												}
											}
											list.remove(position);
											notifyDataSetChanged();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				};

			}

		}

		class PhotoButtonListener implements View.OnClickListener {
			private int position;

			public PhotoButtonListener(int i) {
				position = i;
			}

			@Override
			public void onClick(View v) {
				
				if (v.getId() == comp.button2.getId()) {
					gid = list.get(position).get_id();
					p_name = list.get(position).getCheck_data();
					AlertDialog dialog = new AlertDialog.Builder(ctx).setTitle("是否拍照?")
							.setPositiveButton("选择拍摄照片",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
											StaticContent.bh_id = gid + "";
											StaticContent.BH_p_name = p_name;
											if (mapView.getMapControl() != null) {
												mapView.getMapControl().closeMap();
												System.out.println("Map close");
											}	
											isfirt = false;	
											Intent intent = new Intent(ctx,NewphotoShow.class);		
											ctx.startActivity(intent);
										}
									})
							.setNegativeButton("否",
									new DialogInterface.OnClickListener() {
								
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();

										}
									}).show();
				}

			}
		}

	}
}
