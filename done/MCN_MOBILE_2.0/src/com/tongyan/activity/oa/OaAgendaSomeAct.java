package com.tongyan.activity.oa;


import java.util.LinkedList;
import java.util.List;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaAgendaAllAdapter;
import com.tongyan.activity.adapter.OaAgendaAllAdapter.ViewHolder;
import com.tongyan.common.entities._Agendas;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
/**
 * 
 * @ClassName P21_SomeAgendaAct 
 * @author wanghb
 * @date 2013-7-24 pm 02:21:24
 * @desc 移动OA-部分日程
 */
public class OaAgendaSomeAct extends AbstructCommonActivity {
	
	private Button homeBtn;
	private ListView listView;
	private OaAgendaAllAdapter p10AllAdapter;
	private LinkedList<_Agendas> agendaFlowList = new LinkedList<_Agendas>();
	private _Agendas agendas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_schedule_some);
		homeBtn = (Button)findViewById(R.id.p21_schedule_title_home_btn);
		listView = (ListView)findViewById(R.id.p21_schedule_listview);
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		listView.setOnItemClickListener(listViewListener);
	}
	
	private void businessM(){
		((MyApplication)getApplication()).addActivity(this);
		List<_Agendas> data = (List<_Agendas>)getIntent().getExtras().get("agendasList");
		if(data != null) {
			agendaFlowList.addAll(data);
		}
		p10AllAdapter = new OaAgendaAllAdapter(this,agendaFlowList,R.layout.oa_schedule_all_list_item);
		listView.setAdapter(p10AllAdapter);
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaAgendaSomeAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	OnItemClickListener listViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ViewHolder holder = (ViewHolder) arg1.getTag();
			agendas = holder.agendas;
			Intent intent = new Intent(OaAgendaSomeAct.this,OaAgendaAllDetailsAct.class);
			intent.putExtra("agendas", agendas);
			startActivityForResult(intent, 105);
		}
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 105) {
			finish();
		}
 		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(302);
		}
		return super.onKeyDown(keyCode, event);
	};
}
