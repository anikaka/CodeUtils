package com.tongyan.activity.risk;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.RiskNoticeAAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._ListObj;
import com.tongyan.common.entities._RiskNotice;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.widget.view.MSpinner;
import com.tongyan.widget.view.MyDatePickerDialog;
import com.tongyan.widget.view.MyDatePickerDialog.OnDateTimeSetListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * 
 * @ClassName P38_RiskShowingAct.java
 * @Author wanghb
 * @Date 2013-9-9 am 11:02:43
 * @Desc TODO
 */
public class P38_RiskShowingAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private TextView mNoListTextView;
	private Button mHomeButton;
	private TextView mImageView;
	
	private ListView mListView;
	private PullToRefreshListView mPullRefreshListView;
	
	private LinkedList<_RiskNotice> mRiskNoticeList = new LinkedList<_RiskNotice>();
	private List<_RiskNotice> mRemoteNoticeList = null;
	
	private RiskNoticeAAdapter mAdapter;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String mIsSucc;
	
	private GetDataTask getDataTask;
	
	private int page = 0;
	
	//search
	MDialog mDialogCategory;
	MSpinner mItemSpinner,mSectionSpinner;
	EditText mTimeEdit;
	_ListObj mSelectItem,mSelectSection;
	String mItemId = "", mSectionId = "", mDateTiem = "";
	String mIntentType = "1";//notice
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_showing_list);
		mImageView = (TextView)findViewById(R.id.p38_title_words_imageview);
		mHomeButton = (Button)findViewById(R.id.p35_risk_task_list_home_btn);
		if(getIntent() != null && getIntent().getExtras() != null) {
			mHomeButton.setVisibility(View.INVISIBLE);
			//mImageView.setBackgroundResource(R.drawable.p42_title_image_words);//风险展示
			mImageView.setText(getResources().getString(R.string.main_risk_showing_text));
			mItemId = getIntent().getExtras().getString("mItemId");
			mSectionId = getIntent().getExtras().getString("mSectionId");
			mDateTiem = getIntent().getExtras().getString("mDateTiem");
			mIntentType = "0";//showing
		} 
			
		mNoListTextView = (TextView)findViewById(R.id.p38_risk_showing_no_listview);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.p38_risk_showing_listview);
		mListView = mPullRefreshListView.getRefreshableView();
	}
	
	private void setClickListener() {
		mHomeButton.setOnClickListener(mHomeBtnListener);
		mListView.setOnItemClickListener(mItemClickListener);
		mPullRefreshListView.setOnRefreshListener(frshListener);
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		mAdapter = new RiskNoticeAAdapter(this, mRiskNoticeList);
		mListView.setAdapter(mAdapter);
		getRemoteNotice();
	}
	
	//====================================================================
	// Part of Common Method
	//====================================================================
	public void refeshList() {
		if(mRemoteNoticeList != null) {
			if(mRiskNoticeList != null) {
				mRiskNoticeList.clear();
			}
			mRiskNoticeList.addAll(mRemoteNoticeList);
		}
		mAdapter.notifyDataSetChanged();
		if(mRiskNoticeList == null || mRiskNoticeList.size() == 0) {
			mNoListTextView.setVisibility(View.VISIBLE);
		} else {
			mNoListTextView.setVisibility(View.INVISIBLE);
		}
	}
	
	//下拉刷新
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
	
	private class GetDataTask extends AsyncTask<Void, Void, List<_RiskNotice>> {
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
        @Override
        protected List<_RiskNotice> doInBackground(Void... params) {
            // Simulates a background job.
        	if(pullType == 0) {
            	if(mRiskNoticeList != null) {
            		mRiskNoticeList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 1) {
            	if(mRiskNoticeList != null) {
            		mRiskNoticeList.clear();
            	}
            	page = 1;
            } 
            if(pullType == 2) {//向上
            	page = page + 1;
            } 
			try {
				mItemId = mItemId == null ? "" : mItemId;
				mSectionId = mSectionId == null ? "" : mSectionId;
				String param = "{emp_id:'" +mLocalUser.getUserid() +"',isararm:'"+mIntentType+"',pjid:'"+ mItemId +"',secid:'"+ mSectionId +"',sdate:'"+ mDateTiem +"'}";//1风险提示
				String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(page), "GetRisk", param, Constansts.METHOD_OF_GETLIST,mContext);
				Map<String,Object> mR = new Str2Json().getRiskShowings(str);
				if(mR != null) {
					mIsSucc = (String)mR.get("s");
					if("ok".equals(mIsSucc)) {
						mRemoteNoticeList = (List<_RiskNotice>)mR.get("v");
						if(mRemoteNoticeList == null || mRemoteNoticeList.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return mRemoteNoticeList;
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
        protected void onPostExecute(List<_RiskNotice> result) {
        	if(result != null && result.size() > 0) {
        		for(int i = 0; i < result.size(); i ++) {
        			mRiskNoticeList.addLast(result.get(i));
        		}
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( pullType == 0) {
        		//mAdapter.notifyDataSetChanged();
        		refeshList();
        	} else {
        		mPullRefreshListView.onRefreshComplete();
        	}
        }
    }
	
	public void getRemoteNotice() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	OnClickListener mHomeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			mItemId = ""; mSectionId = ""; mDateTiem = "";
			mDialogCategory = new MDialog(mContext, R.style.dialog);
			mDialogCategory.createDialog(R.layout.risk_task_pop, 0.85, 0.9, getWindowManager());
			
			mItemSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p35_risk_item_spinner);
			mSectionSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p35_risk_section_spinner);
			mTimeEdit = (EditText)mDialogCategory.findViewById(R.id.p38_risk_datea_editext);
			mTimeEdit.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
			mTimeEdit.setCursorVisible(false);//去光标
			
			final List<_ListObj> mItemList = new DBService(mContext).getItemListByObj();
			if(mItemList != null) {
				_ListObj mObj = new _ListObj();
				mObj.setText("选择项目");
				mItemList.add(mObj);
			}
			if(mItemList != null && mItemList.size() > 0) {
				ArrayAdapter<_ListObj> mHighWayAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mItemList){
					 @Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
						 	if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							textView.setText(mItemList.get(position).getText());
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mItemList.get(position).getText();
						if("选择项目".equals(mTextContent)) {
							textView.setTextColor(Color.parseColor("#808080"));
						}
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mItemSpinner.setAdapter(mHighWayAdapter);
				 mItemSpinner.setOnItemSelectedListener(mHighWayEditListener);
			} else {
				mItemSpinner.setmBackgroundView(R.layout.common_spinner3);
				mItemSpinner.setmInitText("选择项目");
				mItemSpinner.setAdapter(mItemSpinner.getAdapter());
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			}
			
			mTimeEdit.setOnClickListener(mDateClickListener);
			
			Button mClickSureBtn = (Button)mDialogCategory.findViewById(R.id.p35_risk_sure_btn);
			mClickSureBtn.setOnClickListener(mClickSureBtnListener);
			
			
		}
	};
	
	OnClickListener mDateClickListener = new OnClickListener() {
		public void onClick(View v) {
			new MyDatePickerDialog(mContext, new OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					
					mDateTiem = year+"-" + mMonth + "-" + mDay;
					mTimeEdit.setText(mDateTiem);
				}
			}).show();
		}
	};
	
	
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			RiskNoticeAAdapter.ViewHolder mViewHolder = (RiskNoticeAAdapter.ViewHolder)arg1.getTag();
			Intent intent = new Intent(mContext, P39_RiskHolefaceAct.class);
			intent.putExtra("checkid", mViewHolder._mRiskNotice.getRow_id());
			intent.putExtra("mIntentType", mIntentType);
			startActivity(intent);
		}
	};
	
	OnItemSelectedListener mHighWayEditListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			final List<_ListObj> mSectionList = new DBService(mContext).getSectionListByObj(item.getId());
			if(mSectionList != null) {
				_ListObj mObj = new _ListObj();
				mObj.setText("选择标段");
				mSectionList.add(mObj);
			}
			if(mSectionList != null && mSectionList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mSectionList){
					 @Override
					public View getDropDownView(int position, View convertView, ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							textView.setText(mSectionList.get(position).getText());
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mSectionList.get(position).getText();
						if("选择标段".equals(mTextContent)) {
							textView.setTextColor(Color.parseColor("#808080"));
						}
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mSectionSpinner.setAdapter(mSectionAdapter);
				 mSectionSpinner.setOnItemSelectedListener(mSectionEditListener);
			} else {
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			}
			mSelectItem = item;
			mSelectSection = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	OnItemSelectedListener mSectionEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			mSelectSection = item;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	OnClickListener mClickSureBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(null != mDialogCategory) {
				mDialogCategory.dismiss();
			}
			if (null != mSelectItem) {
				mItemId = mSelectItem.getId() == null ? "" : mSelectItem.getId();
			}
			if (null != mSelectSection) {
				mSectionId = mSelectSection.getId() == null ? "" : mSelectSection.getId();
			}
			
			getRemoteNotice();
			
		}
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			refeshList();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
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
