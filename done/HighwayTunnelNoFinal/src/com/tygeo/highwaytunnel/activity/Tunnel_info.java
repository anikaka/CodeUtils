package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;
import java.util.List;



import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.adpter.TunnelInfoAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Tunnel_info extends Activity {
	
	private SQLiteDatabase database;
	private ListView listview;
	TunnelInfoAdapter adapter;
	private Button  back_btn, next_btn;//exit_btn,
	private TextView info_text;
	TunnelInfoE e;
	List<TunnelInfoE> listData;
	DBHelper dbhelper;
	String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.tunnel_info);
		InfoApplication.getinstance().addActivity(this);
		// DBManager dbm = new DBManager(getApplicationContext());
		// dbm.openDatabase();
		dbhelper = new DBHelper(StaticContent.DataBasePath);

		new Thread(new Runnable() {

			@Override
			public void run() {
		
//				String jb = UpdateInfo.getde();
//				JSONArray ja = DiseaseInfoDto.GetdemoInfo();
//				WebServiceUtil.RequestTest(jb, ja);
				
//				WebServiceUtil.UpdatePic();
				
			}
		}).start();

		// 初始化组件
		listview = 	  (ListView) findViewById(R.id.Tun_Listview);
		back_btn = (Button) findViewById(R.id.Tun_BackBtn);
		//exit_btn = (Button) findViewById(R.id.Tun_ExitBtn);
		next_btn = (Button) findViewById(R.id.Tun_NextBtn);
		info_text = (TextView) findViewById(R.id.Tun_InfoText);
			
		// 获取适配器
		
		// 获得传递的参数
		
//		str = ((InfoApplication) getApplication()).getTask_name();
		
		info_text.setText(StaticContent.Tturnnel_name);
		
		// 查询数据
		String sql = "select * from TURNNEL where section_name='"+ StaticContent.Tturnnel_name + "'";
		Cursor c = dbhelper.query(sql);
		if (!(c.getCount() == 0) && c.moveToFirst()) {
			listData = new ArrayList<TunnelInfoE>();
			List listdatainfo = new ArrayList();
			do {
				TunnelInfoE tn = new TunnelInfoE();
				tn.set_id((Integer) c.getInt(0));
				tn.setLine_id(c.getString(1));
				tn.setSection_id(c.getString(2));
				tn.setSection_name(c.getString(3));
				tn.setUp_length(c.getDouble(4));
				tn.setUp_num(c.getString(6));
				tn.setDown_num(c.getString(7));
				tn.setCompletion_time(c.getString(8));
				((InfoApplication) getApplication()).setTe(tn);
				listdatainfo.add(tn.getLine_id());
				listdatainfo.add(tn.getSection_id());
				listdatainfo.add(tn.getUp_length());
				listdatainfo.add(tn.getUp_num());
				String temp = tn.getUp_num();
				if (temp.equals("暂无相关信息")) {
//					 Toast t=new Toast(Tunnel_info.this);
//					 t.makeText(Tunnel_info.this,"此隧道信息不完整,请配置好后操作", Toast.LENGTH_SHORT).show();
					 Toast.makeText(Tunnel_info.this, "此隧道信息不完整,请配置好后操作", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					if (tn.getSection_name().contains("上行")||tn.getSection_name().contains("S")) {
						StaticContent.S_X = "S";
						StaticContent.Up_Down = "ZK";
					}else{
						StaticContent.S_X = "X";
						StaticContent.Up_Down = "YK";
					}
					StaticContent.TunnelBeginMile=temp;
					StaticContent.TunnelEndMile=tn.getDown_num();
					try {
						StaticContent.vTunnelBeginMile=Integer.parseInt(temp.split("K")[1].split("\\+")[0])*1000+Integer.parseInt(temp.split("K")[1].split("\\+")[1]);
						StaticContent.vTunnelEndMile=Integer.parseInt(tn.getDown_num().split("K")[1].split("\\+")[0])*1000+Integer.parseInt(tn.getDown_num().split("K")[1].split("\\+")[1]);
					} catch (Exception e) {
						e.printStackTrace();
						StaticContent.vTunnelBeginMile=0;
						StaticContent.vTunnelEndMile=100000;
					}
				listdatainfo.add(tn.getDown_num());
				listdatainfo.add(tn.getCompletion_time());
				}
			} while (c.moveToNext());
			// adapter3 = new SearchAdapter(listData3, getApplication());
			// list3.setAdapter(adapter3);
			adapter = new TunnelInfoAdapter(listdatainfo, this);
			listview.setAdapter(adapter);
			
		}
		c.close();

		back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startActivity(new
				// Intent(Tunnel_info.this,Project_configuration.class));
				finish();
			}
		});
		//exit_btn.setVisibility(View.GONE);
		/*exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
			}
		});*/
		next_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Tunnel_info.this, Task_info.class);
				startActivity(intent);
			}
		});		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new
			// Intent(Tunnel_info.this,Project_configuration.class));
			finish();
			break;
		default:
			break;
		}

		return false;
	}

}
