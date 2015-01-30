package com.tongyan.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * @ClassName P06_MsgAdapter 
 * @author wanghb
 * @date 2013-7-16 AM 11:20:37
 */
public class GpsSectionRecordAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private ArrayList<HashMap<String, String>> mProjectList;
	private LayoutInflater inflater;
	
	public GpsSectionRecordAdapter(Context context, ArrayList<HashMap<String, String>> mProjectList, int resource) {
		this.mProjectList = mProjectList;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mProjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mProjectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			holder = new ViewHolder();
			holder.mCode = (TextView) convertView.findViewById(R.id.record_code);
			holder.mName = (TextView) convertView.findViewById(R.id.record_name);
			holder.mLastApprove = (TextView) convertView.findViewById(R.id.last_approve);
			holder.mSumApprove = (TextView) convertView.findViewById(R.id.sum_approve);
			holder.mReamin = (TextView) convertView.findViewById(R.id.remain);
			holder.mTotal = (TextView) convertView.findViewById(R.id.total);
			holder.mPrice = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> mItem = mProjectList.get(position);
		if(mItem != null) {
			holder.mCode.setText("清单编号：" + mItem.get("code"));
			holder.mName.setText("清单名称：" + mItem.get("name"));
			holder.mLastApprove.setText("上一次计量：" + mItem.get("lastapprove"));
			holder.mSumApprove.setText("累计计量：" + mItem.get("sumapprove"));
			holder.mReamin.setText("剩余量：" + mItem.get("remain"));
			holder.mTotal.setText("合同量：" + mItem.get("total"));
			holder.mPrice.setText("单价：" + mItem.get("price"));
			holder.mData = mItem;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView mCode;
		public TextView mName;
		public TextView mLastApprove;
		public TextView mSumApprove;
		public TextView mReamin;
		public TextView mTotal;
		public TextView mPrice;
		public HashMap<String,String> mData;
	}
	
}
