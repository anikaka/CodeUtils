package com.tongyan.activity.adapter;

import java.util.LinkedList;

import com.tongyan.activity.R;
import com.tongyan.common.entities._Agendas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P10_AllAgendaAdapter 
 * @author wanghb
 * @date 2013-7-24 上午10:54:26
 * @desc TODO
 */
public class OaAgendaAllAdapter extends BaseAdapter {
	
	private int resource;//绑定条目界面
	private LinkedList<_Agendas> agendaFlowList;
	private LayoutInflater inflater;
	
	public OaAgendaAllAdapter(Context context, LinkedList<_Agendas> agendaFlowList, int resource) {
		this.agendaFlowList = agendaFlowList;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if(agendaFlowList == null || agendaFlowList.size() == 0) {
			return 0;
		}
		return agendaFlowList.size();
	}

	@Override
	public Object getItem(int position) {
		return agendaFlowList.get(position);
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
			holder.createEmp = (TextView) convertView.findViewById(R.id.p10_schdule_all_publisher);
			holder.title = (TextView) convertView.findViewById(R.id.p10_schdule_all_title);
			holder.sTime = (TextView) convertView.findViewById(R.id.p10_schdule_all_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		_Agendas agenda = agendaFlowList.get(position);
		if(agenda != null) {
			holder.createEmp.setText(agenda.getCreateEmp());
			holder.title.setText(agenda.getsTitle());
			holder.sTime.setText(agenda.getsTime());
			holder.agendas = agenda;
		}
		return convertView;
	}
	
	
	public final class ViewHolder {
		public TextView title;
		public TextView sTime;
		public TextView createEmp;//创建人
		public _Agendas agendas;
	}
}
