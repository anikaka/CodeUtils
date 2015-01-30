package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.CheckFormAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Chek_form extends Activity {
	// ��������¼��
	CheckFormAdapter adapter;
	TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10;
	ListView listview;
	DBHelper db;
  	Context mcont=this;
  	Dialog d;
  	View view;
	Button bt1, bt2, bt3, backBtn,baseinfo;
	ArrayList<CivilContentE> contentListData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.check_form);
		InfoApplication.getinstance().addActivity(this);
		db = new DBHelper(StaticContent.DataBasePath);
		init();
		query();
		backBtn.setOnClickListener(new OnClickListener() {
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(StaticContent.tabhost_id);
				// Intent intent = new Intent(Chek_form.this,
				// Civil_Check.class);
				// startActivity(intent);
				finish();
				
			}
		});

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentListData = DB_Provider.Sorting("mileage");
				adapter = new CheckFormAdapter(contentListData, Chek_form.this,
						1);

				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);
				
			}
		});
		
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentListData = DB_Provider.Sorting("belong_pro");
				adapter = new CheckFormAdapter(contentListData, Chek_form.this,
						1);
				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);

			}
		});
		bt3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentListData = DB_Provider.Sorting("judge_level");
				adapter = new CheckFormAdapter(contentListData, Chek_form.this,
						1);
				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);
				
			}
		});
		baseinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout ly = new LinearLayout(Chek_form.this);
				LayoutInflater inflater = LayoutInflater
						.from(Chek_form.this);
				
				  view = inflater.inflate(
						R.layout.chekcformdialog, null);
				ListView listview = (ListView) view
						.findViewById(R.id.dialoglistview1);
				ly.addView(view);
				initdialog();
				 d=new Dialog(mcont); 
				
				d.setContentView(ly);
				d.setTitle("������Ϣ");
				d.show();
				d.setCanceledOnTouchOutside(true);
			}
		});
		
		
	
	
		
	}
	

	// ʵ����
	public void init() {
		backBtn = (Button) findViewById(R.id.check_form_BackBtn);
		
		listview = (ListView) findViewById(R.id.check__listview);
		bt1 = (Button) findViewById(R.id.check_form_btn1);
		bt2 = (Button) findViewById(R.id.check_form_btn2);
		bt3 = (Button) findViewById(R.id.check_form_btn3);
		baseinfo=(Button) findViewById(R.id.checkfrombaseinfo);
	
		
	}
public void initdialog(){
	Task task = ((InfoApplication) getApplication()).getTask();
	LineSearch lins = ((InfoApplication) getApplication()).getLinesearch();
	TunnelInfoE tn = ((InfoApplication) getApplication()).getTe();
	TextView tv1 = (TextView)view 
	.findViewById(R.id.melete_input_tv1);
	tv1 = (TextView)view. findViewById(R.id.check_form_text1);
	tv2 = (TextView)view. findViewById(R.id.check_form_text2);
	tv3 = (TextView)view. findViewById(R.id.check_form_text3);
	tv4 = (TextView)view. findViewById(R.id.check_form_text4);
	tv5 = (TextView)view. findViewById(R.id.check_form_text5);
	tv6 = (TextView)view. findViewById(R.id.check_form_text6);
	tv7 = (TextView)view. findViewById(R.id.check_form_text7);
	tv8 = (TextView)view. findViewById(R.id.check_form_text8);
	tv9 = (TextView)view. findViewById(R.id.check_form_text9);
	tv10 = (TextView)view. findViewById(R.id.check_form_text10);
	tv1.setText("������ƣ�" + task.getTask_name());
	tv2.setText("�����ţ�" + lins.getSection_id());
	tv3.setText("����������" + task.getMainte_org());

	Intent intent = getIntent();
	Bundle bundle = intent.getExtras();
	String check_type = bundle.getString("check_type");
	tv4.setText("������" + check_type);
	tv5.setText("����鳤��" + task.getCheck_head());
	tv6.setText("��·���ƣ�" + StaticContent.Tline_name);
	tv7.setText("·�߱��룺" + tn.getLine_id());
	tv8.setText("������ڣ�" + task.getCheck_date());
	tv9.setText("�������: " + task.getCivil_check());
	tv10.setText("�����Ա��" + task.getCheck_member());
	
	
}
	// ��ѯ
	public void query() {
		// String sql = "select *from CILIV_CHECKCONTENT ";
		// Cursor c3 = db.query(sql);
		// // if (!(c3.getCount()== 0)) {
		// if (c3.moveToFirst()) {
		// contentListData = new ArrayList<CivilContentE>();
		// do {
		// CivilContentE ce = new CivilContentE();
		// ce.setCheck_data(c3.getString(1));
		// ce.setCheck_position(c3.getString(2));
		// ce.setMileage(c3.getString(3));
		// ce.setJudge_level(c3.getString(4));
		// ce.setLevel_content(c3.getString(5));
		// ce.setPic_id(c3.getInt(7));
		// ce.setBelong_pro(c3.getString(8));
		// contentListData.add(ce);
		//
		// } while (c3.moveToNext());
		//
		// c3.close();
		// adapter = new CheckFormAdapter(contentListData, Chek_form.this, 1);
		// listview.setAdapter(adapter);
		// }
		
		contentListData = DB_Provider.Sorting("belong_pro");
		adapter = new CheckFormAdapter(contentListData, Chek_form.this, 1);
		listview.setAdapter(adapter);
	}

	// }

}
