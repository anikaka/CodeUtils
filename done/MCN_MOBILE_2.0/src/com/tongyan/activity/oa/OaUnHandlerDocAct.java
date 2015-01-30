package com.tongyan.activity.oa;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaDocumentsAdapter;
import com.tongyan.common.data.Str2Json;
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
 * @ClassName P11_UnHandlerDocAct 
 * @author wanghb
 * @date 2013-7-16 pm 02:21:02
 * @desc 移动OA-收文管理/发文管理 列表
 */
public class OaUnHandlerDocAct extends AbstructCommonActivity {
	private Context mContext = this;
	
	private Button homeBtn;
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	
    private _User localUser;
	
	private String isSucc;
	private LinkedList<HashMap<String, Object>> msgFlows = new LinkedList<HashMap<String, Object>>();
	
	private Dialog mDialog;
	
	private int page = 0;
	
	private OaDocumentsAdapter mAdapter;
	private GetDataTask getDataTask;
	
	//private SharedPreferences mPreferences = null;
	
	private String mIntentType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_unhandler_document);
		
		//mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		homeBtn = (Button)findViewById(R.id.p11_undocument_title_home_btn);
		homeBtn.setOnClickListener(onHomeBtnClick);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p11_undocument_listview);
		TextView mTitleView = (TextView)findViewById(R.id.oa_handler_document_title);
		listView = mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(listViewListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mIntentType = getIntent().getExtras().getString("IntentType");
			if(Constansts.TYPE_OF_DOCUMENTGET.equals(mIntentType)) {
				mTitleView.setText(getResources().getString(R.string.main_oa_document_get_text));
			} else {
				mTitleView.setText(getResources().getString(R.string.main_oa_document_send_text));
			}
			businessM();
		}
	}
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		
		mAdapter = new OaDocumentsAdapter(this, msgFlows, R.layout.oa_message_list_item);
		listView.setAdapter(mAdapter);
		getFirstPage();
	}
	
    public void getFirstPage() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		getDataTask = new GetDataTask();
		getDataTask.execute();
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
	
	
	private class GetDataTask extends AsyncTask<Void, Void, List<HashMap<String, Object>>> {
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
        @Override
        protected List<HashMap<String, Object>> doInBackground(Void... params) {
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
				//String parms = "{emp_id:'"+ localUser.getUserid() +"'}";  
				String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), mIntentType, null, Constansts.METHOD_OF_GETLIST, mContext);
				List<HashMap<String, Object>> mList = null;
				if(Constansts.TYPE_OF_DOCUMENTGET.equals(mIntentType)) {
					mList = new Str2Json().getAcceptDocumentList(str);
				} else {
					mList = new Str2Json().getSendDocumentList(str);
				}
				if(mList != null) {
					return mList;
				} else {
					sendMessage(Constansts.NO_DATA);
				}
			}  catch (Exception e) {
				sendMessage(Constansts.CONNECTION_TIMEOUT);
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, Object>> result) {
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
	OnClickListener onHomeBtnClick = new OnClickListener() {
	public void onClick(View v) {
		if (getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		Intent intent = new Intent(mContext, MainAct.class);
		startActivity(intent);
	}};
	
	OnItemClickListener listViewListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			/*
			 *version 1
			Intent intent = new Intent(mContext,OaDocmentDetailAct.class);
			intent.putExtra("dClass", viewHolder.msg.getnClass());
			intent.putExtra("_message", viewHolder.msg.getRowId());//.getText().toString()
			startActivityForResult(intent,666);*/
			/*ViewHolder viewHolder = (ViewHolder)arg1.getTag();
			StringBuilder builder = new StringBuilder("http://");//
			version 2
			String port = mPreferences.getString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT);
			builder.append(port);
			builder.append("/Web/SignRtx?s=");
			builder.append(viewHolder.msg.getmLink());
			Uri uri = Uri.parse(builder.toString());    
			Intent it = new Intent(Intent.ACTION_VIEW, uri);    
			startActivity(it);*/
			//version 3
			OaDocumentsAdapter.ViewHolder viewHolder = (OaDocumentsAdapter.ViewHolder)arg1.getTag();
			HashMap<String,Object> map = viewHolder.msg;
			if(map != null) {
				String category =  (String)map.get("category");
				Intent intent = new Intent(mContext, OaDocmentDetailAct.class);
				if(Constansts.TYPE_OF_DOCUMENTGET.equals(mIntentType)) {//收文
					intent.putExtra("IntentType", Constansts.TYPE_OF_DOCUMENTGET);
					if( "已办".equals(category) || "办结".equals(category)) {
						intent.putExtra("DateType", "finished");
					} else {
						intent.putExtra("DateType", "approve");
					}
					intent.putExtra("ItemData", map);
					startActivityForResult(intent, 666);
				} else {//发文
					intent.putExtra("IntentType", Constansts.TYPE_OF_DOCUMENTSEND);
					if( "已办".equals(category) || "办结".equals(category)) {
						intent.putExtra("DateType", "finished");
					} else {
						intent.putExtra("DateType", "approve");
					}
					intent.putExtra("ItemData", map);
					startActivityForResult(intent, 666);
				}
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 666) {
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
	
	@Override
	protected void onPause() {
		if(getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		super.onPause();
	}
}
