package com.tongyan.fragment;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.R;
import com.tongyan.activity.R.color;
import com.tongyan.activity.measure.measure.MidMeasureRecordAct;
import com.tongyan.activity.measure.measure.MidTermPayCertificateCountTabAct;
import com.tongyan.activity.measure.measure.PayNoticeRecordAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @Title: MeasureMenuFragment.java 
 * @author Rubert
 * @date 2014-10-16
 * @version V1.0 
 */
public class MeasureMenuFragment extends AbstructFragment {
	
	private Context mContext;
	private String mSectionId = "";
	private String mType;
	private ListView mListView;
	private GetDataTask getDataTask;
	//private Dialog mDialog;
	private int mCurrentPageCount = 0;
	private _User mLocalUser;
	private String mSuccBack;
	private MeasureListAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private LinkedList<Map<String,String>> mDataList = new LinkedList<Map<String,String>>();
	private Dialog mDialog;
	
	public static MeasureMenuFragment newInstance(Context context, String sectionId, String type, _User mUser) {
		MeasureMenuFragment mFragment = new MeasureMenuFragment();
		mFragment.mContext = context;
		mFragment.mSectionId = sectionId;
		mFragment.mType = type;
		mFragment.mLocalUser = mUser;
		return mFragment;
	}
	
	public void getFirstPage() {
		//mDialog = new AlertDialog.Builder(mContext).create();
		//mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		//mDialog.setContentView(R.layout.common_loading_process_dialog);
		//mDialog.setCanceledOnTouchOutside(false);
		getDataTask = new GetDataTask();
		getDataTask.execute();
		showDialog();
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.measure_menu_content, container, false);
		mContext=getActivity();
		mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.measure_content_lsitview);
		mListView = mPullToRefreshListView.getRefreshableView();
		mPullToRefreshListView.setOnRefreshListener(mFreshListener);
		mAdapter = new MeasureListAdapter(mContext, mDataList);
		mListView.setAdapter(mAdapter);
		getFirstPage();
		return view;
	}
	
	OnRefreshListener mFreshListener = new OnRefreshListener(){
		@Override
		public void onRefresh() {
			if(getDataTask == null) {
				getDataTask = new GetDataTask(mPullToRefreshListView.getRefreshType());
			} else {
				if(!getDataTask.isCancelled())
					getDataTask.cancel(true);
				getDataTask = new GetDataTask(mPullToRefreshListView.getRefreshType());
			}
			getDataTask.execute();
		}
	};
	
	/** 显示对话框*/
	protected void showDialog(){
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
	
	/**关闭对话框 */
	protected void closeDialog(){
		if(mDialog!=null){
			mDialog.dismiss();
		}
	}
	private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
		
		int mPullType;
		
		public GetDataTask(){}
		public GetDataTask(int pullType) {
			this.mPullType = pullType;
		}
		
		@Override
		protected List<Map<String, String>> doInBackground(Void... params) {
			if(mPullType == 0) {
            	if(mDataList != null) {
            		mDataList.clear();
            	}
            	mCurrentPageCount = 1;
            } 
            if(mPullType == 1) {
            	if(mDataList != null) {
            		mDataList.clear();
            	}
            	mCurrentPageCount = 1;
            } 
            if(mPullType == 2) {//向上
            	mCurrentPageCount ++;
            } 
            try {
            	String param = "{type:'" + mType + "',sectionId:'" + mSectionId + "'}";
				String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(mCurrentPageCount), Constansts.TYPE_OF_MEASURE, param, Constansts.METHOD_OF_GETLIST, mContext);
				Map<String,Object> mR = new Str2Json().getMeasureList(str);
				if(mR != null) {
					mSuccBack = (String)mR.get("s");
					Log.i("test", mSuccBack);
					if("ok".equals(mSuccBack)) {
						List<Map<String, String>> newFlows = (List<Map<String, String>>)mR.get("v");
						if(newFlows == null || newFlows.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return newFlows;
					} else {
						
						//sendMessage(Constansts.ERRER);
					}
				} else {
					//sendMessage(Constansts.NET_ERROR);
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<Map<String, String>> result) {
			if(result != null && result.size() > 0) {
				mDataList.addAll(result);
				if( mPullType == 0) {
	        		mAdapter.notifyDataSetChanged();
	        	} else {
	        		mPullToRefreshListView.onRefreshComplete();
	        	}
			}
			closeDialog();
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			
			break;
		case Constansts.NO_DATA:
			closeDialog();
			Toast.makeText(mContext, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		if(getDataTask != null && !getDataTask.isCancelled())
			getDataTask.cancel(true);
		super.onPause();
	}
	
	
	
	public class MeasureListAdapter extends BaseAdapter {
		
		private LayoutInflater layoutInflater;
		private LinkedList<Map<String,String>> mDataList;
		
		public MeasureListAdapter(Context context, LinkedList<Map<String,String>> mDataList) {
			layoutInflater = LayoutInflater.from(context);
			this.mDataList = mDataList;
		}
		
		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.measure_roadbed_list_item, null);
				holder = new ViewHolder();
				holder.mSectionCode = (TextView)convertView.findViewById(R.id.section_code);
				holder.mMeasureCodeOfMonth = (TextView)convertView.findViewById(R.id.measure_code_of_month);
				holder.mMeasureDate = (TextView)convertView.findViewById(R.id.measure_date);
				holder.mMeasureStartDate = (TextView)convertView.findViewById(R.id.start_date);
				holder.mMeasureFinishDate = (TextView)convertView.findViewById(R.id.finish_date);
				holder.mCreater = (TextView)convertView.findViewById(R.id.creater);
				holder.mCreateDate = (TextView)convertView.findViewById(R.id.create_date);
				
				holder.mContractMidMeasureRecord = (TextView)convertView.findViewById(R.id.contract_mid_measure_record);
				
				holder.mChangeMidMeasureRecord = (TextView)convertView.findViewById(R.id.change_mid_measure_record);
				holder.mMidTermPayMeasureRecord = (TextView)convertView.findViewById(R.id.midterm_pay_certificate_measure_record);
				holder.mPayNoticeMeasureRecord = (TextView)convertView.findViewById(R.id.pay_notice_measure_record);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			final Map<String,String> mDataMap = mDataList.get(position);
			holder.mSectionCode.setText("所属标段 :" + mDataMap.get("secName"));
			holder.mMeasureCodeOfMonth.setText("月计量号 :" + mDataMap.get("miCode"));
			holder.mMeasureDate.setText("计量日期 :" + mDataMap.get("miMDate"));
			holder.mMeasureStartDate.setText("开始日期 :" + mDataMap.get("miStartDate"));
			holder.mMeasureFinishDate.setText("截止日期 :" + mDataMap.get("miEndDate"));
			holder.mCreater.setText("制表人 :" + mDataMap.get("miTabulator"));
			holder.mCreateDate.setText("制表日期 :" + mDataMap.get("miTabDate"));
			holder.mContractMidMeasureRecord.setText(Html.fromHtml("<u>" +getResources().getString(R.string.contract_mid_compute_measure_record)+" :"+mDataMap.get("contractStatus")+"</u>"));
			
			holder.mChangeMidMeasureRecord.setText(Html.fromHtml("<u>"+ getResources().getString(R.string.change_mid_compute_measure_record) +" :"+mDataMap.get("changeStatus")+"</u>"));
			
			holder.mMidTermPayMeasureRecord.setText(Html.fromHtml("<u>"+getResources().getString(R.string.mid_term_pay_certificate_count_table)+" :"+mDataMap.get("paycertStatus")+"</u>"));
			
			holder.mPayNoticeMeasureRecord.setText(Html.fromHtml("<u>"+ getResources().getString(R.string.pay_notice_note) +" :"+mDataMap.get("noticeStatus")+"</u>"));
			

			
			holder.mContractMidMeasureRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((TextView)v).setTextColor(color.blue);
					Intent intent = new Intent(mContext, MidMeasureRecordAct.class);
					intent.putExtra("mid_measure_type", getResources().getString(R.string.contract_mid_compute_measure_record));
					intent.putExtra("rowid", mDataMap.get("rowId"));
					intent.putExtra("tabstatus", mDataMap.get("tabstatus"));
					intent.putExtra("imlType", "1"); //合同中间计量单
					intent.putExtra("approveFormState", mDataMap.get("contractStatus"));//合同计量单状态
					startActivity(intent);
				}
			});
			
			holder.mChangeMidMeasureRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((TextView)v).setTextColor(color.blue);
					Intent intent = new Intent(mContext, MidMeasureRecordAct.class);
					intent.putExtra("mid_measure_type", getResources().getString(R.string.change_mid_compute_measure_record));
					intent.putExtra("rowid", mDataMap.get("rowId"));
					intent.putExtra("tabstatus", mDataMap.get("tabstatus"));
					intent.putExtra("imlType", "2"); //上传的变更计量单
					intent.putExtra("approveFormState", mDataMap.get("changeStatus"));//变更计量单状态
					startActivity(intent);
				}
			});
			holder.mMidTermPayMeasureRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((TextView)v).setTextColor(color.blue);
					Intent intent = new Intent(mContext, MidTermPayCertificateCountTabAct.class);
					intent.putExtra("rowid", mDataMap.get("rowId"));
					intent.putExtra("flowId", mDataMap.get("paycertFlowid"));//paycertFlowid
					intent.putExtra("approveFormState", mDataMap.get("paycertStatus"));//支付证书状态
					startActivity(intent);
				}
			});
			
			holder.mPayNoticeMeasureRecord.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((TextView)v).setTextColor(color.blue);
					Intent intent = new Intent(mContext, PayNoticeRecordAct.class);
					intent.putExtra("rowId", mDataMap.get("rowId"));
					intent.putExtra("flowId", mDataMap.get("noticeFlowid"));
					intent.putExtra("approveFormState", mDataMap.get("noticeStatus"));
					startActivity(intent);
				}
			});
			
			return convertView;
			
		}
		
		
		public final class ViewHolder {
			public TextView mSectionCode;
			public TextView mMeasureCodeOfMonth;
			public TextView mMeasureDate;
			public TextView mMeasureStartDate;
			public TextView mMeasureFinishDate;
			public TextView mCreater;
			public TextView mCreateDate;
			public TextView mContractMidMeasureRecord;
			public TextView mChangeMidMeasureRecord;
			public TextView mMidTermPayMeasureRecord;
			public TextView mPayNoticeMeasureRecord;
			public Map<String,String> mDataMap;
		}
		
	}
	
}
