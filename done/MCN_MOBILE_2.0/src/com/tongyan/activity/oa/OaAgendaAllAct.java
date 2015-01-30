package com.tongyan.activity.oa;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaAgendaAllAdapter;
import com.tongyan.activity.adapter.OaAgendaAllAdapter.ViewHolder;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Agendas;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
/**
 * 
 * @ClassName P10_AllAgendaAct 
 * @author wanghb
 * @date 2013-7-16 下午02:21:24
 * @desc 移动OA-全部日程
 */
public class OaAgendaAllAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private Button homeBtn;
	private PullToRefreshListView mPullRefreshListView;
	
	private ListView listView;
	
	private OaAgendaAllAdapter p10AllAdapter;
	
	private _User localUser;
	private String isSucc;
	
	private LinkedList<_Agendas> agendaFlowList = new LinkedList<_Agendas>();
	
	
	private int page = 0;
	
	private Dialog mDialog;
	
	private GetDataTask getDataTask;
	
	private String yearAndMonth;
	private String actualMaximum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_schedule_all);
		homeBtn = (Button)findViewById(R.id.p10_schedule_title_home_btn);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p10_schedule_listview);
		listView = mPullRefreshListView.getRefreshableView();
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		listView.setOnItemClickListener(listViewListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
	}
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		if(getIntent() != null && getIntent().getExtras() != null) {
			yearAndMonth = getIntent().getExtras().getString("yearAndMonth");
			actualMaximum = getIntent().getExtras().getString("ActualMaximum");
		}
		p10AllAdapter = new OaAgendaAllAdapter(this,agendaFlowList,R.layout.oa_schedule_all_list_item);
		listView.setAdapter(p10AllAdapter);
		loadingData();
	}
	
	//下拉刷新 2013-7-22
	OnRefreshListener frshListener = new OnRefreshListener(){
		@Override
		public void onRefresh() {
			if(getDataTask == null) {
				getDataTask = new GetDataTask(mPullRefreshListView.getRefreshType());
			} else {
				if(!getDataTask.isCancelled())
				getDataTask.cancel(true);
				getDataTask = new GetDataTask(mPullRefreshListView.getRefreshType());
			}
			getDataTask.execute();
		}
	};
	
	
	public void loadingData() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	
private class GetDataTask extends AsyncTask<Void, Void, List<_Agendas>> {
		int pullType;
		public GetDataTask() {

		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
        @Override
        protected List<_Agendas> doInBackground(Void... params) {
        	if(pullType == 0) {
            	if(agendaFlowList != null) {
            		agendaFlowList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 1) {
            	if(agendaFlowList != null) {
            		agendaFlowList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 2) {//向上
            	page = page + 1;
            } 
            
            String mStartDate = yearAndMonth + "-1";
			String mEndDate = yearAndMonth + "-" + actualMaximum;
			//String paramsStr = "{emp_id:'"+localUser.getUserid()+"',stime:'"+ yearAndMonth +"'}";
			String paramsStr = "{emp_id:'"+localUser.getUserid()+"',stime:'"+ mStartDate +"',etime:'"+mEndDate+"'}";
			try {
				String jsonStr = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), "Scheduling", paramsStr, Constansts.METHOD_OF_GETLIST,mContext);
				Map<String,Object> mR = new Str2Json().getScheduleList(jsonStr);
				if(mR != null) {
					isSucc = (String)mR.get("s");
					if("ok".equals(isSucc)) {
						List<_Agendas> newAgendaFlows = (List<_Agendas>)mR.get("agendaList");
						if(newAgendaFlows ==null || newAgendaFlows.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return newAgendaFlows;
						
					} else {
						sendMessage(Constansts.ERRER);
					}
				} else {
					sendMessage(Constansts.NET_ERROR);
				}
			}  catch (Exception e) {
				sendMessage(Constansts.CONNECTION_TIMEOUT);
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(List<_Agendas> result) {
        	if(result != null && result.size() > 0) {
        		agendaFlowList.addAll(result);
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( pullType == 0) {
        		p10AllAdapter.notifyDataSetChanged();
        	} else {
        		mPullRefreshListView.onRefreshComplete();
        	}
        }
        
    }
	//下拉刷新 2013-7-22
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(getDataTask != null  && !getDataTask.isCancelled())
				getDataTask.cancel(true);
			Intent intent = new Intent(OaAgendaAllAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	OnItemClickListener listViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ViewHolder holder = (ViewHolder) arg1.getTag();
			//BaseToast.show(P10_AllAgendaAct.this, holder.empId + "," + holder.sContent);
			_Agendas agendas = holder.agendas;
			Intent intent = new Intent(OaAgendaAllAct.this,OaAgendaAllDetailsAct.class);
			intent.putExtra("agendas", agendas);
			startActivityForResult(intent, 105);
		}
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 105 || resultCode == 106 || resultCode ==0 ) {
			if(agendaFlowList != null) {
				agendaFlowList.clear();
			}
			page = 0;
			loadingData();
		}
 		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void onPause() {
		if(getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		super.onPause();
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(getDataTask != null  && !getDataTask.isCancelled())
			getDataTask.cancel(true);
			setResult(302);
		}
		return super.onKeyDown(keyCode, event);
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
}
