package com.tongyan.activity.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.entities._LocRisk;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P35_RisksListAdapter 
 * @author wanghb
 * @date 2013-9-3 am 10:44:13
 * @desc TODO
 */
public class RisksListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private List<_LocRisk> riskList;
	
	public RisksListAdapter(Context context, List<_LocRisk> riskList) {
		layoutInflater = LayoutInflater.from(context);
		this.riskList = riskList;
	}
	
	
	@Override
	public int getCount() {
		return riskList == null ? 0 : riskList.size();
	}

	@Override
	public Object getItem(int position) {
		if (riskList != null) {
			return riskList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.risk_task_list_item, null);
			holder = new ViewHolder();
			holder.timeTextView = (TextView)convertView.findViewById(R.id.p35_list_item_time_text);
			holder.tunnelTextView = (TextView)convertView.findViewById(R.id.p35_list_item_tunnel_text);
			holder.finishTextView = (TextView)convertView.findViewById(R.id.p35_list_item_is_finish_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		_LocRisk risk = riskList.get(position);
		if(risk != null) {
			String currdate = "";
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(risk.getCurrDate());
				currdate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
			} catch (ParseException e) {
				currdate = risk.getCurrDate();
				e.printStackTrace();
			}
			holder.timeTextView.setText(currdate);
			holder.tunnelTextView.setText(risk.getpContent());
			String isFinish = "未检查"; 
			if("1".equals(risk.getIsFinish())) {
				isFinish = "已上传"; 
			} else if("3".equals(risk.getIsFinish())) {
				isFinish = "已检查"; 
			} else if("2".equals(risk.getIsFinish())) {
				isFinish = "正检查"; 
			}  else if("4".equals(risk.getIsFinish())) {
				isFinish = "上传未成功"; 
			}  
			
			holder.finishTextView.setText(isFinish);
			holder.mRisk = risk;
		}
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView timeTextView;
		public TextView tunnelTextView;
		public TextView finishTextView;
		public _LocRisk mRisk;
	}
}
