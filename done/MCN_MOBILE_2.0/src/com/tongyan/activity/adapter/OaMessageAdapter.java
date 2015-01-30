package com.tongyan.activity.adapter;

import java.util.LinkedList;

import com.tongyan.activity.R;
import com.tongyan.common.entities._Message;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P06_MsgAdapter 
 * @author wanghb
 * @date 2013-7-16 am 11:20:37
 * @desc TODO
 */
public class OaMessageAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private LinkedList<_Message> msgFlowList;
	private LayoutInflater inflater;
	
	public OaMessageAdapter(Context context, LinkedList<_Message> msgFlowList, int resource) {
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
		_Message msg = msgFlowList.get(position);
		if(msg != null) {
			String mTitle = msg.getnTitle();
			if(mTitle != null) {
				mTitle = mTitle.replaceAll("<b>", "");
				mTitle = mTitle.replaceAll("</b>", "");
			}
			holder.title.setText(mTitle);
			holder.date.setText(msg.getnDate());
			holder.type.setText("[" + msg.getnType() + "]");
			holder.msg = msg;
		}
		
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView type;
		public TextView title;
		public TextView date;
		
		public _Message msg;
	}
	
}
