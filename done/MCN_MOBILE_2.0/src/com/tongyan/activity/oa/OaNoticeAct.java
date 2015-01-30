package com.tongyan.activity.oa;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaMessageAdapter;
import com.tongyan.activity.adapter.OaMessageAdapter.ViewHolder;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Message;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @ClassName P18_NoticeAct 
 * @author wanghb
 * @date 2013-7-21 pm 03:29:23
 * @desc 移动OA-公告通知
 */
public class OaNoticeAct extends AbstructCommonActivity{
	private Context mContext = this;
	private Button homeBtn;
	
	//private P06_MsgAdapter adapter;
	
	private OaMessageAdapter mAdapter;
	
	private _User localUser;
	
	private String isSucc;
	private LinkedList<_Message> msgFlows = new LinkedList<_Message>();
	
	private Dialog mDialog;
	
	private int page = 0;
	
	private PullToRefreshListView mPullRefreshListView;
	
	private ListView listView;
	
	private GetDataTask getDataTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		
		mAdapter = new OaMessageAdapter(this,msgFlows,R.layout.oa_message_list_item);
		listView.setAdapter(mAdapter);
		
		newThread();
	}
	
	public void newThread() {
		
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		getDataTask = new GetDataTask();
		getDataTask.execute();
		
	}
	
	
	private void initPage() {
		setContentView(R.layout.oa_msg_list);
		TextView mTitleWords = (TextView)findViewById(R.id.oa_msg_title_content);
		mTitleWords.setText("公告通知");
		homeBtn = (Button)findViewById(R.id.p06_msg_home_btn);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p06_msg_listview);
		listView = mPullRefreshListView.getRefreshableView();
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		listView.setOnItemClickListener(itemListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
		
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
	
	
	private class GetDataTask extends AsyncTask<Void, Void, List<_Message>> {
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
        @Override
        protected List<_Message> doInBackground(Void... params) {
            // Simulates a background job.
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
				String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), "Notice", null, Constansts.METHOD_OF_GETLIST,mContext);
				Map<String,Object> mR = new Str2Json().getNoticeList(str);
				if(mR != null) {
					isSucc = (String)mR.get("s");
					if("ok".equals(isSucc)) {
						List<_Message> newMsgFlows = (List<_Message>)mR.get("v");
						if(newMsgFlows ==null || newMsgFlows.size() == 0) {
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
				sendMessage(Constansts.CONNECTION_TIMEOUT);
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
        		mAdapter.notifyDataSetChanged();
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
			Intent intent = new Intent(OaNoticeAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnItemClickListener itemListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			/*_Message values = msgFlows.get(position);
			Intent intent = new Intent(P18_NoticeAct.this,P19_NoticeDetailAct.class);
			intent.putExtra("_message", values);
			startActivity(intent);*/
			ViewHolder viewHolder = (ViewHolder)arg1.getTag();
			Intent intent = new Intent(OaNoticeAct.this,OaNoticeDetailAct.class);
			intent.putExtra("_message", viewHolder.msg.getRowId());//.getText().toString()
			startActivity(intent);
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(getDataTask != null && !getDataTask.isCancelled())
				getDataTask.cancel(true);
			 setResult(104);
		}
		return super.onKeyDown(keyCode, event);
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			//adapter.notifyDataSetChanged();
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
	
	@Override
	protected void onPause() {
		if(getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		super.onPause();
	}
}
