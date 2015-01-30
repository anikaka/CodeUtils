package com.tygeo.highwaytunnel.activity;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.Task;

import android.app.Activity;
import android.app.TabActivity;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
/*
 *设施定期检查tabhost 管理
 */
public class Ele_Check extends TabActivity {
	
	private TabHost tabHost; // 声明TabHost
	private Intent intent1, intent2, intent3, intent4, intent5, intent6,
			intent7, intent8; // 声明Intent
	String task_name;
	ImageButton bakc_btn,ele_btn;
	TextView tv1;
	public static int index;
	Editor sharedata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.civil_config);
		InfoApplication.getinstance().addActivity(this);
		inint();
		
		String task_name = ((InfoApplication) getApplication()).getTask_name();
		StaticContent.Check_Titile = task_name;
		System.out.println(StaticContent.Check_Titile);

		Task task = ((InfoApplication) getApplication()).getTask();
		tv1.setText(StaticContent.Check_Titile + "-" + "供配电设施" + "-" + "定期检查");

	}

	public void getTask_checkName() {
		task_name = ((InfoApplication) getApplication()).getTask_check_name();

	}

	public void inint() {
		// 获取tabhost对象并设置
		tabHost = getTabHost();
		tv1 = (TextView) findViewById(R.id.Civil_Config_Text);

		String check_by = ((InfoApplication) getApplication()).getTask()
				.getTacilitiy_check();

		intent1 = new Intent(this, Ele_jc_chek.class);
		intent1.putExtra("ename", "PD");
		tabHost.addTab(tabHost.newTabSpec("PD").setIndicator("供配电设施")
				.setContent(intent1));

		intent2 = new Intent(this, Ele_jc_chek.class);
		intent2.putExtra("ename", "ZM");
		tabHost.addTab(tabHost.newTabSpec("ZM").setIndicator("照明设施")
				.setContent(intent2));

		intent3 = new Intent(this, Ele_jc_chek.class);
		intent3.putExtra("ename", "TF");
		tabHost.addTab(tabHost.newTabSpec("TF").setIndicator("通风设施")
				.setContent(intent3));

		intent4 = new Intent(this, Ele_jc_chek.class);
		intent4.putExtra("ename", "XF");
		tabHost.addTab(tabHost.newTabSpec("XF").setIndicator("消防与救援设施")
				.setContent(intent4));

		intent5 = new Intent(this, Ele_jc_chek.class);
		intent5.putExtra("ename", "JK");
		tabHost.addTab(tabHost.newTabSpec("JK").setIndicator("监控设施")
				.setContent(intent5));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				StaticContent.indexTag = tabId;
				// Editor sharedata = getSharedPreferences("data", 0).edit();
				//
				// sharedata.putString("tag", indexTag);
				// sharedata.commit();
				tabHost.setCurrentTabByTag(tabId);
				updateTab(tabHost);
				if (tabId.equals("PD")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "机电设施" + "-"
							+ "供配电设施" + "-" + "定期检修");
				}
				if (tabId.equals("ZM")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "机电设施" + "-"
							+ "照明设施" + "-" + "定期检修");
				}
				if (tabId.equals("TF")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "机电设施" + "-"
							+ "通风设施" + "-" + "定期检修");
				}
				if (tabId.equals("XF")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "机电设施" + "-"
							+ "消防与救援设施" + "-" + "定期检修");
				}
				if (tabId.equals("JK")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "机电设施" + "-"
							+ "监控设施" + "-" + "定期检修");
				}
				System.out.println(StaticContent.indexTag);

			}
		});

		tabHost.setCurrentTab(0);
		updateTab(tabHost);
		bakc_btn = (ImageButton) findViewById(R.id.Civil_Config_BackBtn);
		ele_btn = (ImageButton) findViewById(R.id.Civil_Config_Electrical_fac);
		String PK = ((InfoApplication) getApplication()).getTask()
				.getCheck_type();
		if (PK.equals("机电设施")) {
			ele_btn.setVisibility(View.GONE);
		}
		ele_btn.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.civil_select));
		ele_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Ele_Check.this, Civil_Check.class);
				startActivity(intent);
			}
		});
		bakc_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(Ele_Check.this, Task_info.class));
				// finish();
				Intent intent = new Intent(Ele_Check.this, Task_info.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent);
			}
		});
	}

	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextSize(16);
			tv.setWidth(250);
			tv.setGravity(Gravity.CENTER);
			tv.setHeight(30);

			tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
			if (tabHost.getCurrentTab() == i) {// 选中
				view.setBackgroundDrawable(getResources().getDrawable(
						R.color.common_title_or_background_color));// 选中后的背景
				tv.setTextColor(this.getResources().getColorStateList(
						R.drawable.white));
			} else {// 不选中
				view.setBackgroundDrawable(getResources().getDrawable(
						R.color.common_sub_title_background_color));// 非选择的背景
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.background_dark));
			}
		}
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  
		switch (keyCode) { 
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Ele_Check.this,Task_info.class));
			// finish();
			Intent intent = new Intent(Ele_Check.this, Task_info.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
			startActivity(intent);
			break;

		default:
			break;
		}

		return false;
	}

}
