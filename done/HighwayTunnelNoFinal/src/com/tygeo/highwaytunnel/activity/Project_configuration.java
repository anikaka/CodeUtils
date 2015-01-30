package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;
import java.util.List;
import com.tygeo.highwaytunnel.R;

import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DBManager;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.DBhelper.Web_date_provider;
import com.tygeo.highwaytunnel.adpter.SearchAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.BaseEquiment;
import com.tygeo.highwaytunnel.entity.CheckinfoE;
import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import com.tygeo.highwaytunnel.entity.line_json;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wsd 项目信息配置
 */
public class Project_configuration extends Activity {
	private Thread mThread;
	TextView Check_PsyAdd;
	int isok=0;
	ImageButton Check_SureBtn;
	private Handler mHandler;
	Button Check_NextBtn;
	List<line_json> lj;
	ProgressDialog pd;
	List<CheckinfoE> ce,me;
	List<TunnelInfoE> te;
	List<BaseEquiment> be;
	EditText Check_Edit;
	ProgressBar progressBar;
	ListView list1, list2, list3;
	private SQLiteDatabase database;
	String father_name;
	Button ConfigBtn2, login_page;
	DBHelper db;
	int sition = -1;
	SearchAdapter adapter, adapter2, adapter3, adapter2f, adapter3f,adapter3n;
	List<LineSearch> listData1, listData2, listData3;
	ContentValues cv;
	DBManager dbm;
	String info;
	boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.project_config);
		
		InfoApplication.getinstance().addActivity(this);
		login_page = (Button) findViewById(R.id.ConfigBtn1);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//			System.out.println(StaticContent.UnitCode);
		// 打开数据库
		
		// dbm = new DBManager(getApplicationContext());
		// dbm.openDatabase();
		// database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH +
		// "/"
		// + DBManager.DB_NAME, null);
		// WebServiceUtil.GetBaseRoadInfo();
		
		pd = new ProgressDialog(Project_configuration.this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setTitle("正在更新数据");
		pd.show();
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					 pd.dismiss();
					 isok=1;
//					 Toast t=new Toast(Project_configuration.this);
//					 t.makeText(Project_configuration.this, "更新数据完毕",
//					 Toast.LENGTH_LONG).show();
					 Toast.makeText(Project_configuration.this, "数据更新完毕", Toast.LENGTH_SHORT).show();
					 break;
					
				}
				
			};
			
		};
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {// run()在新的线程中运行
				mHandler.sendEmptyMessage(1);
			}
		};
		mThread = new Thread(runnable);
		if (StaticContent.Isdown == 0) {
			mThread.start();
			StaticContent.Isdown = 1;
		}else{
			pd.dismiss();
		}
//		String sql1 = "select _id,section_name,section_id from PRO_INFO where father_id=0";
		db = new DBHelper(StaticContent.DataBasePath);
//		
//		
//		Cursor c = db.query(sql1);
//		// 将游标的数据加入通过自定义adapter加入listview中
//		if (c.moveToFirst()) {
//			listData1 = new ArrayList<LineSearch>();
//			do {
//				LineSearch li = new LineSearch();
//				li.set_id(c.getInt(0));
//				li.setSection_name(c.getString(1));
//				li.setSection_id(c.getString(2));
//				father_name = c.getString(1);
//				
//				listData1.add(li);
//				
//			} while (c.moveToNext());
//			adapter = new SearchAdapter(listData1, Project_configuration.this);
//			c.close();
//			
//			// list.add(ls);
//			
//		}
	ArrayList<String> s1=DB_Provider.GetUnitTunnle();
	ArrayList<String> s=DB_Provider.GetSectionName(s1);
		
		listData1 = new ArrayList<LineSearch>();
		
		for (int i = 0; i < s.size(); i++) {
			LineSearch li = new LineSearch();
			li.setSection_name(s.get(i));
			System.out.println("隧道名称::"+s.get(i));
			li.setSection_id(s1.get(i));	
			listData1.add(li);
		}
		
		adapter = new SearchAdapter(listData1, Project_configuration.this);
		
		list1 = (ListView) findViewById(R.id.Config_ListView1);

		list2 = (ListView) findViewById(R.id.Config_ListView2);
		
		list3 = (ListView) findViewById(R.id.Config_ListView3);
		
		list1.setAdapter(adapter);
		
		
		// 为点击事件加入监听 
		list1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelectPosition(position);
				
				adapter.notifyDataSetChanged();
				
				sition=-1;
				/*if (((ListView) parent).getTag() != null) {
					
					((View) ((ListView) parent).getTag())
							.setBackgroundDrawable(null);
					
				}*/
				
				//((ListView) parent).setTag(view);
				
				//view.setBackgroundResource(R.drawable.blue);
				StaticContent.Tsection_name = listData1.get(position).getSection_name();
				StaticContent.Tsection_id = listData1.get(position)
						.getSection_id();
				String pid = listData1.get(position).getSection_id();
				String sql2 = "select _id,section_name,section_id from PRO_INFO where father_id='"
						+ pid + "'";
				
				Web_date_provider wdata=new Web_date_provider();
				boolean f=wdata.JustTunnle(pid);
				if (f==false) {
					
				
				if (DB_Provider.exitson(StaticContent.Tsection_id) == true) {
					Cursor c2 = db.query(sql2);
					if (c2.moveToFirst()) {
						listData2 = new ArrayList<LineSearch>();
						ArrayList<LineSearch> tlistData2 = new ArrayList<LineSearch>();
						
						ArrayList<String> ss=DB_Provider.GetLineName();	
						do {
							
							LineSearch liS = new LineSearch();
							liS.set_id(c2.getInt(0));
							liS.setSection_name(c2.getString(1));
							
							liS.setSection_id(c2.getString(2));
							System.out.println("隧道名称: "+c2.getString(2));
							if (!(ss.indexOf(c2.getString(2))==-1)) {
								
								listData2.add(liS);
							}
							
							
							
						} while (c2.moveToNext());
						
						adapter2 = new SearchAdapter(listData2,
								Project_configuration.this);
						list2.setAdapter(adapter2);
//						LineSearch lj = new LineSearch();
//						listData3 = new ArrayList<LineSearch>();
//						adapter3n=new SearchAdapter(listData3, Project_configuration.this);
						list3.setAdapter(null);
						c2.close();
						
					} else {
						System.out.println(00);
					}
				} else {
					LineSearch lj = new LineSearch();
					lj.setSection_name("暂无相关信息");
					listData2 = new ArrayList<LineSearch>();
					listData2.add(lj);
					adapter2f = new SearchAdapter(listData2,
							Project_configuration.this);
					System.out.println("cao");
					list2.setAdapter(adapter2f);
					list3.setAdapter(adapter2f);
				}
					
				}else{
					String sql="select section_id,section_name from TURNNEL where line_id='"+pid+"'";
					Cursor c=db.query(sql);
					list2.setAdapter(null);
					StaticContent.Tline_id=StaticContent.Tsection_id;
					StaticContent.Tline_name=StaticContent.Tsection_name;
					if (!(c.getCount()==0)) {
						if (c.moveToFirst()) {
							listData3 = new ArrayList<LineSearch>();
							do {
								
								LineSearch liS2 = new LineSearch();
								liS2.setSection_id(c.getString(0));
								liS2.setSection_name(c.getString(1));
								listData3.add(liS2);
							} while (c.moveToNext());
						}
						
						adapter3 = new SearchAdapter(listData3,Project_configuration.this);
						list3.setAdapter(adapter3);
						c.close();
					}
					
				}
				
				
			}
		});
		
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(adapter2 != null) {
					adapter2.setSelectPosition(position);
					adapter2.notifyDataSetChanged();
				} else {
					Toast.makeText(Project_configuration.this, "adapter2f is null", 1).show();
				}
				
				sition=-1;
					/*if (((ListView) parent).getTag() != null) {
					
					((View) ((ListView) parent).getTag()).setBackgroundDrawable(null);
					
				}*/
				((ListView) parent).setTag(view);
				//view.setBackgroundResource(R.drawable.blue);
				
				if(!list2.getAdapter().getItem(0).toString().equals("暂无相关信息")){
				String pid = listData2.get(position).getSection_id();
				StaticContent.Tline_id = listData2.get(position)
						.getSection_id();
				StaticContent.Tline_name = listData2.get(position)
						.getSection_name();
				String sql2 = "select _id,section_id,section_name from TURNNEL where line_id='" + pid
						+ "'";
				if (DB_Provider.exitsonTunnel(StaticContent.Tline_id) == true) {
					Cursor c3 = db.query(sql2);
					if (c3.moveToFirst()) {
						listData3 = new ArrayList<LineSearch>();
						do {
							LineSearch liS2 = new LineSearch();
							liS2.set_id(c3.getInt(0));
							liS2.setSection_id(c3.getString(1));
							liS2.setSection_name(c3.getString(2));

							// 将隧道信息存入application
							((InfoApplication) getApplication())
									.setLinesearch(liS2);
							listData3.add(liS2);
							
						} while (c3.moveToNext());
						adapter3 = new SearchAdapter(listData3,
								Project_configuration.this);
						list3.setAdapter(adapter3);

						c3.close();
					} else {
						System.out.println(00);
					}
				} else {
					System.out.println("cao");

					LineSearch lj = new LineSearch();
					listData3 = new ArrayList<LineSearch>();
					lj.setSection_name("暂无相关信息");
					listData3.add(lj);
					
					adapter3f = new SearchAdapter(listData3,
							Project_configuration.this);
					list3.setAdapter(adapter3f);
				}
				}
			}
		});
				
		list3.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				
				if(adapter3 != null) {
					adapter3.setSelectPosition(position);
					adapter3.notifyDataSetChanged();
				}
				/*if (((ListView) parent).getTag() != null) {
					((View) ((ListView) parent).getTag())
							.setBackgroundDrawable(null);
					
				}*/
				((ListView) parent).setTag(view);
				sition=position;
				//view.setBackgroundResource(R.drawable.blue);
				
				int pid = listData3.get(position).get_id();
				try {
					((InfoApplication) getApplication())
					.setLinesearch( listData3.get(position));
				} catch (Exception e) {
				}
				
			}
		});
		
		 ConfigBtn2 = (Button) findViewById(R.id.ConfigBtn2);
		 ConfigBtn2.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 
			if(sition==-1){
//				Toast t = new Toast(Project_configuration.this);
//				t.makeText(Project_configuration.this, "请选择有信息的隧道",
//						Toast.LENGTH_LONG).show();
				Toast.makeText(Project_configuration.this, "请选择有信息的隧道", Toast.LENGTH_SHORT).show();
			}else{
				
				if (listData3.get(sition).getSection_name().equals("暂无相关信息")) {
//					Toast t = new Toast(Project_configuration.this);
//					t.makeText(Project_configuration.this, "请选择有信息的隧道",
//							Toast.LENGTH_LONG).show();
					Toast.makeText(Project_configuration.this, "请选择有信息的隧道", Toast.LENGTH_SHORT).show();
					
				}
			 else {
					StaticContent.Tturnnel_name = listData3.get(sition).getSection_name();

					StaticContent.Tturnnel_id = listData3.get(sition).getSection_id();
					info = listData3.get(sition).getSection_name(); 
			 
			    Intent intent = new Intent(Project_configuration.this,Tunnel_info.class);
				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
				((InfoApplication) getApplication()).setTask_name(info);
				StaticContent.task_name = info;
				System.out.println("任务名称"+ ((InfoApplication) getApplication()).getTask_name());
				startActivity(intent);
		 }}} 
		 });
		login_page.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				Intent intent = new Intent(Project_configuration.this,
//						Login.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
				finish();
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// Intent intent=new Intent(Project_configuration.this,Login.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			finish();
			
			break;
			
		default: 
			break;
		}

		return false;
	}

}
