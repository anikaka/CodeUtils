package com.tongyan.activity.supervice;



import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.SuperviceResultAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.widget.view.MyDatePickerDialog;
import com.tongyan.widget.view.MyDatePickerDialog.OnDateTimeSetListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @ClassName P30_IsSuperviceAct 
 * @author wanghb
 * @date 2013-8-13 pm 02:42:49
 * @desc 检查结果列表
 */
public class SearchSuperviceAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private TextView noList;
	private EditText mEditText;
	
	private SuperviceResultAdapter adaper;
	
	private LinkedList<Map<String,String>> mResultList = new LinkedList<Map<String,String>>();
	
	private MyApplication mApplication;
	private _User mLocalUser;
	private Dialog mDialog;
	private String mIsSucc;
	private int page = 0;
	
	private String unitId = null;
	private String date = "";
	
	private GetDataTask getDataTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.supervice_list);
		mEditText = (EditText)findViewById(R.id.p30_supervise_search_edittext);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p30_supervice_listview);
		mListView = mPullRefreshListView.getRefreshableView();
		noList = (TextView)findViewById(R.id.p30_supervice_no_listview);
	}
	private void setClickListener() {
		mListView.setOnItemClickListener(onItemListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
		mEditText.setOnClickListener(mEditTextClick);
		mEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mEditText.setCursorVisible(false);//去光标
	}
	
	OnClickListener  mEditTextClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			new MyDatePickerDialog(mContext, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					date = year+"-"+mMonth +"-"+mDay;
					mEditText.setText(date);
					getFirstPage();
				}
			}).show();
		}
	};
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		adaper = new SuperviceResultAdapter(this,mResultList);
		mListView.setAdapter(adaper);
		if(getIntent() != null && getIntent().getExtras() != null) {
			unitId = getIntent().getExtras().getString("unitId");
		}
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
	
private class GetDataTask extends AsyncTask<Void, Void, List<Map<String,String>>> {
		
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
		
        @Override
        protected List<Map<String,String>> doInBackground(Void... params) {
            if(pullType == 0) {
            	if(mResultList != null) {
            		mResultList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 1) {
            	if(mResultList != null) {
            		mResultList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 2) {//向上
            	page = page + 1;
            } 
			try {
				String parms = "{unitId:'"+ unitId +"',date:'"+ date +"'}";  
				String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), "GetSupervise", parms, Constansts.METHOD_OF_GETLIST, mContext);
				Map<String,Object> mR = new Str2Json().getResultList(str);
				if(mR != null) {
					mIsSucc = (String)mR.get("s");
					if("ok".equals(mIsSucc)) {
						List<Map<String,String>> mFlows = (List<Map<String,String>>)mR.get("v");
						if(mFlows == null || mFlows.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return mFlows;
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
        protected void onPostExecute(List<Map<String,String>> result) {
        	if(result != null && result.size() > 0) {
        			for(int i = 0; i < result.size(); i ++) {
        				mResultList.addLast(result.get(i));
            		}
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( pullType == 0) {
        		adaper.notifyDataSetChanged();
        	} else {
        		mPullRefreshListView.onRefreshComplete();
        	}
        }
    }
	
	OnItemClickListener onItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			SuperviceResultAdapter.ViewHolder viewHolder = (SuperviceResultAdapter.ViewHolder)arg1.getTag();
			Intent intent = new Intent(mContext,ShowCotentSuperAct.class);
			Map<String,String> checkObj = viewHolder.checkObj;
			if(checkObj != null && checkObj.get("rowId") != null) {
				intent.putExtra("rowId", checkObj.get("rowId"));
				startActivity(intent);
			} else {
				Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			}
			
		}
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
			Toast.makeText(mContext, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
