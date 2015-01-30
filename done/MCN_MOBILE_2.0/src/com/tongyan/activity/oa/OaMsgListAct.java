package com.tongyan.activity.oa;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaMessageAdapter;
import com.tongyan.activity.adapter.OaMessageAdapter.ViewHolder;
import com.tongyan.activity.measure.measure.MeasureMenuAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Agendas;
import com.tongyan.common.entities._Message;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @ClassName P06_MsgListAct 
 * @author wanghb
 * @date 2013-7-16 pm 01:11:09
 * @desc 移动OA-消息列表
 */
public class OaMsgListAct extends AbstructCommonActivity {
	private PullToRefreshListView mPullRefreshListView;
	
	private ListView listView;
	
	private Button homeBtn;
    private _User localUser;
	
	private String isSucc;
	private LinkedList<_Message> msgFlows = new LinkedList<_Message>();
	
	private Dialog mDialog;
	
	private int page = 0;
	
	private OaMessageAdapter adapter;
	
	private GetDataTask getDataTask;
	
	private String rowId;
	
	private _Agendas agendas;
	
	private SharedPreferences mPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	
	private void initPage() {
		setContentView(R.layout.oa_msg_list);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		homeBtn = (Button)findViewById(R.id.p06_msg_home_btn);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p06_msg_listview);
		listView = mPullRefreshListView.getRefreshableView();
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		listView.setOnItemClickListener(itemListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
	}
	
	private void businessM() {
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		
		adapter = new OaMessageAdapter(this, msgFlows, R.layout.oa_message_list_item);
		listView.setAdapter(adapter);
		getFirstPage();
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
	
	
	public void getFirstPage() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, List<_Message>> {
		
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
		
        @Override
        protected List<_Message> doInBackground(Void... params) {
            if(pullType == 0) {
            	if(msgFlows != null) {
            		msgFlows.clear();
            	}
            	page = 1;
            } 
            if(pullType == 1) {
            	if(msgFlows != null) {
            		msgFlows.clear();
            	}
            	page = 1;
            } 
            if(pullType == 2) {//向上
            	page = page + 1;
            } 
			try {
				String parms = "{emp_id:'"+ localUser.getUserid() +"'}";  
				String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), "GetMessage", parms, Constansts.METHOD_OF_GETLIST,OaMsgListAct.this);
				Map<String,Object> mR = new Str2Json().getMessageList(str);
				if(mR != null) {
					isSucc = (String)mR.get("s");
					if("ok".equals(isSucc)) {
						List<_Message> newMsgFlows = (List<_Message>)mR.get("v");
						if(newMsgFlows == null || newMsgFlows.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return newMsgFlows;
					} else {
						sendMessage(Constansts.ERRER);
					}
				} else {
					sendMessage(Constansts.NET_ERROR);
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(List<_Message> result) {
        	if(result != null && result.size() > 0) {
        		msgFlows.addAll(result);
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( pullType == 0) {
        		adapter.notifyDataSetChanged();
        	} else {
        		mPullRefreshListView.onRefreshComplete();
        	}
        }
    }
	//下拉刷新 2013-7-22
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(getDataTask != null && !getDataTask.isCancelled())
				getDataTask.cancel(true);
			Intent intent = new Intent(OaMsgListAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnItemClickListener itemListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ViewHolder viewHolder = (ViewHolder)arg1.getTag();
			if(viewHolder != null && viewHolder.msg != null) {
				/**
				 日程提醒
			         收发文
                                 分项清单
				 合同管理
				 预付款
				 计量管理
				 预变更管理
				 项目办预变更管理
				 预变更查看
				 项目办预变更查看
				 财务管理
				进度管理 
				 */
				rowId = viewHolder.msg.getRowId();//.getText().toString();
				String mMsgType = viewHolder.msg.getnType();
				if("计量管理".equals(mMsgType)){
					Intent  intent=new Intent(OaMsgListAct.this, MeasureMenuAct.class);
					startActivity(intent);
					return;
				}
				if("日程提醒".equals(mMsgType)) {
					go2Agenda();
				}else if("发文".equals(mMsgType) || "收文".equals(mMsgType) || "发文查看".equals(mMsgType)) {
					/*Intent intent = new Intent(OaMsgListAct.this,OaDocmentDetailAct.class);
					intent.putExtra("dClass", viewHolder.msg.getnType());
					intent.putExtra("_message", rowId);
					startActivityForResult(intent, 666);*/
					StringBuilder builder = new StringBuilder("http://");//
					String port = mPreferences.getString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT);
					builder.append(port);
					builder.append("/Web/SignRtx?s=");
					builder.append(viewHolder.msg.getmLink());
					Uri uri = Uri.parse(builder.toString());    
					Intent it = new Intent(Intent.ACTION_VIEW, uri);    
					startActivity(it);
				}else {
					//Toast.makeText(OaMsgListAct.this, "该待办事项需要在PC端进行操作", Toast.LENGTH_SHORT).show();
					StringBuilder builder = new StringBuilder("http://");//
					String port = mPreferences.getString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT);
					builder.append(port);
					builder.append("/Web/SignRtx?s=");
					builder.append(viewHolder.msg.getmLink());
					Uri uri = Uri.parse(builder.toString());    
					Intent it = new Intent(Intent.ACTION_VIEW, uri);    
					startActivity(it);
				}
			}
		}
	};
	
	public void go2Agenda() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String,String> properties = new HashMap<String,String>();
				properties.put("publicKey", Constansts.PUBLIC_KEY);
				properties.put("userName", localUser.getUsername());
				properties.put("Password", localUser.getPassword());
				properties.put("type", "Scheduling");//
				properties.put("id", rowId);
				try {
					String str = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, OaMsgListAct.this);
					Map<String,Object> mR = new Str2Json().getScheduleContent(str);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							agendas = (_Agendas)mR.get("v");
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} 
			}
		}).start();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 666 || resultCode == 0) {
			if(msgFlows != null) {
				msgFlows.clear();
			}
			page = 0;
			getFirstPage();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(getDataTask != null && !getDataTask.isCancelled())
				getDataTask.cancel(true);
		}
		return super.onKeyDown(keyCode, event);
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			if(agendas != null) {
				Intent intent = new Intent(OaMsgListAct.this,OaAgendaAllDetailsAct.class);
				intent.putExtra("agendas", agendas);
				startActivityForResult(intent, 105);
			}
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
		case Constansts.MES_TYPE_1 :
			if(mDialog != null)
				mDialog.dismiss();
			adapter.notifyDataSetChanged();
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
	@Override
	protected void onPause() {
		if(getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		super.onPause();
	}
}
