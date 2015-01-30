package com.tygeo.highwaytunnel.activity;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class Civil_Check extends TabActivity {
	//�������tabhost����;
	private TabHost tabHost; // ����TabHost
	private Intent intent1, intent2, intent3, intent4, intent5, intent6,
			intent7, intent8; // ����Intent
	String task_name;
	ImageButton bakc_btn,ele_btn;
	TextView tv1;
	TabWidget tabWidget;
	public static int index;
	Editor sharedata;
	String cname;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.civil_config);
		InfoApplication.getinstance().addActivity(this);
		inint();
		String task_name = ((InfoApplication) getApplication()).getTask_name();
		cname = ((InfoApplication) getApplication()).getTask().getCivil_check();
		StaticContent.Check_Titile = task_name + "-" + "�����ṹ";
		// ���ñ���
		tv1.setText(StaticContent.Check_Titile + "-" + "����" + "-" + cname);
		
	}

	public void inint() {
		// ��ȡtabhost��������
		tabHost = getTabHost();
		TabWidget tabWidget = tabHost.getTabWidget();
		tv1 = (TextView) findViewById(R.id.Civil_Config_Text);
		bakc_btn = (ImageButton) findViewById(R.id.Civil_Config_BackBtn);
		String PK = ((InfoApplication) getApplication()).getTask().getCheck_type();
		ele_btn = (ImageButton) findViewById(R.id.Civil_Config_Electrical_fac);
		ele_btn.setImageDrawable(getResources().getDrawable(
				R.drawable.ele_select));
		intent1 = new Intent(this, Portal.class);
		intent1.putExtra("name", "DK");
		tabHost.addTab(tabHost.newTabSpec("DK").setIndicator("����")
				.setContent(intent1));

		intent2 = new Intent(this, Portal.class);
		intent2.putExtra("name", "DM");
		tabHost.addTab(tabHost.newTabSpec("DM").setIndicator("����")
				.setContent(intent2));

		intent3 = new Intent(this, Lining.class);
		intent3.putExtra("name", "CQ");
		tabHost.addTab(tabHost.newTabSpec("CQ").setIndicator("����")
				.setContent(intent3));

		intent4 = new Intent(this, Roadbed.class);
		intent4.putExtra("name", "LM");
		tabHost.addTab(tabHost.newTabSpec("LM").setIndicator("·��").setContent(intent4));

		intent5 = new Intent(this, Roadbed.class);
		intent5.putExtra("name", "JXD");
		tabHost.addTab(tabHost.newTabSpec("JXD").setIndicator("���޵�")
				.setContent(intent5));

		intent6 = new Intent(this, Roadbed.class);
		intent6.putExtra("name", "PASS");
		tabHost.addTab(tabHost.newTabSpec("PSSS").setIndicator("��ˮ��ʩ")
				.setContent(intent6));
		
		intent7 = new Intent(this, Roadbed.class);
		intent7.putExtra("name", "DD");
		tabHost.addTab(tabHost.newTabSpec("DD").setIndicator("����")
				.setContent(intent7));

		intent8 = new Intent(this, Roadbed.class);
		intent8.putExtra("name", "NZ");
		tabHost.addTab(tabHost.newTabSpec("NZ").setIndicator("��װ")
				.setContent(intent8));
		tabHost.setCurrentTab(StaticContent.tabhost_id);
		System.out.println(StaticContent.tabhost_id);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				
				StaticContent.indexTag = tabId;
				// Editor sharedata = getSharedPreferences("data", 0).edit();
				//
				// sharedata.putString("tag", indexTag);
				// sharedata.commit();
				tabHost.setCurrentTabByTag(tabId);
				StaticContent.listselectindex=0;
				StaticContent.listpositonindex=0;
				updateTab(tabHost);
				System.out.println(StaticContent.indexTag);
				if (tabId.equals("DK")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "����" + "-"
							+ cname);
				} else if (tabId.equals("DM")) {
					System.out.println(StaticContent.Check_Titile);
					tv1.setText(StaticContent.Check_Titile + "-" + "����" + "-"
							+ cname);
					System.out.println(StaticContent.Check_Titile);
				} else if (tabId.equals("CQ")) {

					tv1.setText(StaticContent.Check_Titile + "-" + "����" + "-"
							+ cname);
					System.out.println(StaticContent.Check_Titile);
				} else if (tabId.equals("LM")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "·��" + "-"
							+ cname);
				} else if (tabId.equals("JXD")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "���޵�" + "-"
							+ cname);
				} else if (tabId.equals("PSSS")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "��ˮ��ʩ" + "-"
							+ cname);
				} else if (tabId.equals("DD")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "����" + "-"
							+ cname);
				} else if (tabId.equals("NZ")) {
					tv1.setText(StaticContent.Check_Titile + "-" + "��װ" + "-"
							+ cname);
				}

			}
		});
		
		tabHost.setCurrentTab(StaticContent.tabhost_id);
		updateTab(tabHost);

		if (PK.equals("�����ṹ")) {
			ele_btn.setVisibility(View.INVISIBLE);
		}
		
		ele_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Civil_Check.this, Ele_Check.class);
				
				startActivity(intent);
				if (((InfoApplication) getApplication()).getTask()
						.getTacilitiy_check().equals("�����Լ���")) {
					startActivity(new Intent(Civil_Check.this,Ele_Check_jc.class));
					
				}
			}
		});
		bakc_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(Civil_Check.this, Task_info.class));
				Intent intent = new Intent(Civil_Check.this, Task_info.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				StaticContent.listselectindex=0;
				StaticContent.listpositonindex=0;
				startActivity(intent);
//				finish();
			}
			
		});
	}

	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(16);
			tv.setWidth(250);
			tv.setGravity(Gravity.CENTER);
			tv.setHeight(30);
			tv.setTypeface(Typeface.SERIF, 2); // ��������ͷ��
			if (tabHost.getCurrentTab() == i) {// ѡ��
				view.setBackgroundDrawable(getResources().getDrawable(R.color.common_title_or_background_color));// ѡ�к�ı���
				tv.setTextColor(this.getResources().getColorStateList(R.drawable.white));
			} else {// ��ѡ��
				view.setBackgroundDrawable(getResources().getDrawable(R.color.common_sub_title_background_color));// ��ѡ��ı���
				tv.setTextColor(this.getResources().getColorStateList(android.R.color.background_dark));
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(Civil_Check.this, Task_info.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			StaticContent.listselectindex=0;
			StaticContent.listpositonindex=0;
			startActivity(intent);
			// finish();
			break;

		default:
			break;
		}

		return false;
	}

}
