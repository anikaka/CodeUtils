package com.tygeo.highwaytunnel.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.dom.DOMResult;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import ty.security.com.PermissionManager;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DBManager;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.DBhelper.Web_date_provider;
import com.tygeo.highwaytunnel.common.Common_Name;
import com.tygeo.highwaytunnel.common.DeviceUuidFactory;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.PaseXMl;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.common.WebServiceUtil;
import com.tygeo.highwaytunnel.entity.CheckinfoE;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import com.tygeo.highwaytunnel.entity.line_json;

import android.R.animator;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Check extends Activity {
	
	/*
	 * 服务器设施，数据下载界面。
	 */

	DBHelper db = new DBHelper(StaticContent.DataBasePath);
	private Thread mThread;
	TextView Check_PsyAdd;
	ImageButton Check_SureBtn;
	private Handler mHandler;
	Button Check_NextBtn, CheckNet, checkwl;
	List<line_json> lj;
	List<CheckinfoE> ce, me;
	ArrayAdapter<String> adapter;
	EditText checkwled;
	int unitc;
	String spname;
	String unitcode;
	int canlogin;
	List<TunnelInfoE> te;
	EditText Check_Edit, Check_Edit2;
	ProgressBar progressBar;
	String weburl;
	Message mes;
	String uuuid;
	ProgressDialog pd;
	LinearLayout linlay;
	private Spinner spinner2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.check);
		InfoApplication.getinstance().addActivity(this);
		initView();

		// if(StaticContent.Isdown==0){
		// pd=new ProgressDialog(Check.this);
		// pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// pd.setTitle("正在更新数据");
		// pd.show();
		// }
		// 提交监听
		
		// List<line_json> lj=new ArrayList<line_json>();
		// lj=WebServiceUtil.GetBaseRoadInfo();
		// System.out.println("lj::::"+lj.get(0).getSection_name());

		// me=WebServiceUtil.GetBaseManagerUnitInfo();
		// String str=DB_Provider.jsondemo();
		// Runnable runnable = new Runnable() {
		// public void run() {
		// getunit(0);
		// };
		//
		// };
		// mThread = new Thread(runnable);
		// mThread.start();
		// spinner2.setVisibility(View.INVISIBLE);
		// CheckNet.setVisibility(View.VISIBLE);
		
		pd = new ProgressDialog(Check.this);
		weburl = StaticContent.webURLxml;
		if(weburl==null){
			checkwled.setText("192.168.0.3:8088");
		}
		checkwled.setText(weburl);
		checkwled.setTag(weburl);
		ArrayList<String> list1 = DB_Provider.GetManagerUnit();
		
		if (list1.size() == 0) {
			spname ="";
		}else{
		
		adapter = new ArrayAdapter<String>(Check.this, R.layout.spinner, list1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter);
		spname = spinner2.getSelectedItem().toString();
		unitc = Integer.parseInt(DB_Provider.GetManagerCode(spname).toString());}
		DeviceUuidFactory uu = new DeviceUuidFactory(this);
		
		
		Check_Edit2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// CheckNet.setVisibility(View.VISIBLE);
				Check_Edit2.setCursorVisible(false);
				linlay.setVisibility(View.VISIBLE);
				checkwled.setText(weburl);
				checkwled.setTag(weburl);
				
				spinner2.setVisibility(View.INVISIBLE);
			}
		});
		
		checkwl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				StaticContent.serviceURL1 = "http://"+checkwled.getText().toString().trim()+ "/WebService/BaseInfoService.asmx";
				weburl = checkwled.getText().toString();
				// List<CheckinfoE>
				// list=WebServiceUtil.GetBaseManagerUnitInfo();
				// if (!(list.size()==0)) {
				//
				// }
				// else{
				//
				//
				// }
				pd = new ProgressDialog(Check.this);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setTitle("正在更新数据");
				pd.show();
				
				Runnable runnable = new Runnable() {
					public void run() {
						getunit(1);
					};
				};
				mThread = new Thread(runnable);
				mThread.start();
				
			}
		});

		mHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				
				case 0:
					pd.dismiss();
					spinner2.setAdapter(adapter);

					Toast t = new Toast(Check.this);
					spname = spinner2.getSelectedItem().toString();
					t.makeText(Check.this, "更新数据完毕", Toast.LENGTH_LONG).show();
					unitc = Integer.parseInt(DB_Provider.GetManagerCode(spname)
							.toString());

					canlogin = 1;
					break;
				case 1:
					pd.dismiss();
					spinner2.setAdapter(adapter);
					linlay.setVisibility(View.INVISIBLE);
					spinner2.setVisibility(View.VISIBLE);
					spname = spinner2.getSelectedItem().toString();
					StaticContent.serviceURL1 = "http://" + weburl
							+ "/WebService/BaseInfoService.asmx";
					unitc = Integer.parseInt(DB_Provider.GetManagerCode(spname)
							.toString());
					pd.dismiss();
					canlogin = 1;
					Toast t1 = new Toast(Check.this);
					t1.makeText(Check.this, "更新数据完毕", Toast.LENGTH_LONG).show();
					break;
				case 2:
					pd.dismiss();
					Toast t2 = new Toast(Check.this);
					t2.makeText(Check.this, "网络连接异常,请检查是否联网或网络地址是否正确",
							Toast.LENGTH_LONG).show();
					canlogin = 0;
					break;
				}

			};

		};

		Check_SureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DeviceUuidFactory uu = new DeviceUuidFactory(
						InfoApplication.getInstance());
				String uid = uu.getDeviceUuid().toString();
				PermissionManager pm = new PermissionManager(
						InfoApplication.getinstance(),getString(R.string.ipstr),
						getString(R.string.producebh), uid);
				String mg = pm.VerifyCode();
				WebServiceUtil web = new WebServiceUtil();
				
				if (!mg.equals("Y")) {
					Toast.makeText(Check.this, mg, Toast.LENGTH_LONG).show();
					System.out.println(mg);
				}else{
				if (spname.equals("")) {
					Toast t1 = new Toast(Check.this);
					t1.makeText(Check.this, "请保持网络地址与管理站的正确性",
							Toast.LENGTH_LONG).show();
				} else {
					spname = spinner2.getSelectedItem().toString();
					unitc = Integer.parseInt(DB_Provider.GetManagerCode(spname)
							.toString());
					StaticContent.UnitCode=unitc+"";
					System.out.println("写入untiCode::::"+ unitc);
					PaseXMl px = new PaseXMl();
					try {
						Map<String, String> s = new HashMap<String, String>();
						s.put("WebServiceUrl", weburl);
						s.put("UnitCode", unitc + "");
						px.save(s);
					} catch (IllegalStateException e) {
					
						e.printStackTrace();
					} catch (IOException e) {
					
						e.printStackTrace();
					} catch (Exception e) {
					
						e.printStackTrace();
					}
					//

					// StaticContent.serviceURL1 = "http://" + weburl
					// + "/WebService/BaseInfoService.asmx";

					// StaticContent.localinfo.put("WebServiceUrl", weburl);
					StaticContent.Isdown = 0;
							
					startActivity(new Intent(Check.this,
							Project_configuration.class));
					((InfoApplication) getApplication()).setC(1);
					
				}}
			}
		});
	}

	//
	public void initView() {
		// Check_PsyAdd = (TextView) findViewById(R.id.Check_PsyAdd);
		Check_SureBtn = (ImageButton) findViewById(R.id.Check_SureBtn);
		// Check_Edit = (EditText) findViewById(R.id.Check_Edit);
		Check_Edit2 = (EditText) findViewById(R.id.Check_Edit2);
		linlay = (LinearLayout) findViewById(R.id.check_layout);
		spinner2 = (Spinner) findViewById(R.id.Spinner);
		CheckNet = (Button) findViewById(R.id.CheckNet);
		Check_Edit2.setInputType(InputType.TYPE_NULL);// 首次禁用软键盘
		Check_Edit2.setCursorVisible(false);// 去光标
		Check_Edit2.setSelection(Check_Edit2.getText().length()); // 光标移动最后
	
		Check_Edit2.setOnTouchListener(editTouchListener);
		checkwl = (Button) findViewById(R.id.check_WlBtn);
		checkwled = (EditText) findViewById(R.id.check_wlEdt);
		checkwled.setSelection(checkwled.getText().length());
	}

	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Check_Edit2.setInputType(InputType.TYPE_CLASS_TEXT);
			Check_Edit2.setCursorVisible(true);
			return false;
		}
	};

	public String tolocaljson() {
		String s = "";

		return s;
	}

	public void getunit(int i) {
		mes = new Message();
		me = WebServiceUtil.GetBaseManagerUnitInfo();
		if (me.size() == 0) {
			mHandler.sendEmptyMessage(2);
		} else {
			for (int c = 0; c < me.size(); c++) {
				Web_date_provider.InsertManagerInfo(me.get(c));
			}
			
			ArrayList<String> list1 = DB_Provider.GetManagerUnit();
			if (list1.size() == 0) {
				
			}
			
			adapter = new ArrayAdapter<String>(Check.this, R.layout.spinner,
					list1);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			System.out.println("unitCode: " + unitc);
			
			Web_date_provider db=new Web_date_provider();
			String sql="delete from TURNNEL where 1=1";
			String sql1="delete from PRO_INFO where 1=1" ;
			DB_Provider.dbHelper.execSql(sql);
			DB_Provider.dbHelper.execSql(sql1);
			db.GetDownData();
			mHandler.sendEmptyMessage(i);
		}

	}
	
}
