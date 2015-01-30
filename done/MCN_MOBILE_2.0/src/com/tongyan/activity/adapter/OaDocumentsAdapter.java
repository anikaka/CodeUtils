package com.tongyan.activity.adapter;

import java.util.HashMap;
import java.util.LinkedList;

import com.tongyan.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName OaDocumentsAdapter 
 * @author wanghb
 * @date 2013-7-16 am 11:20:37
 * @desc 收文管理、发文管理
 */
public class OaDocumentsAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private LinkedList<HashMap<String, Object>> msgFlowList;
	private LayoutInflater inflater;
	
	public OaDocumentsAdapter(Context context, LinkedList<HashMap<String, Object>> msgFlowList, int resource) {
		this.msgFlowList = msgFlowList;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount() {
		if(msgFlowList == null || msgFlowList.size() == 0) {
			return 0;
		}
		return msgFlowList.size();
	}

	@Override
	public Object getItem(int position) {
		return msgFlowList.get(position);
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
			holder.type = (TextView) convertView.findViewById(R.id.p06_message_type);
			holder.title = (TextView) convertView.findViewById(R.id.p06_message_title);
			holder.date = (TextView) convertView.findViewById(R.id.p06_msg_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object> msg = msgFlowList.get(position);
		if(msg != null) {
			holder.title.setText((String)msg.get("listTitle"));
			holder.date.setText((String)msg.get("listDate"));
			holder.type.setText("[" + msg.get("category") + "]");
			holder.msg = msg;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView type;
		public TextView title;
		public TextView date;
		
		public HashMap<String, Object> msg;
	}
	
}
