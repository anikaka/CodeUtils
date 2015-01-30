package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.List;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.adpter.SearchAdapter.ResultComp;
import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TunnelInfoAdapter extends BaseAdapter {
	
	
	
	
	private List list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;
	private String[] name = { "路线编码 :", "隧道编码 :", "隧道上行长度 :",
			"起始桩号 :", "结束桩号 :", "竣工时间 :" };
	
	public TunnelInfoAdapter(List<TunnelInfoE> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
		this.inflater = LayoutInflater.from(ctx);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ResultComp comp = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tunnel_info_listview, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView
					.findViewById(R.id.Tunnel_info_listview_text1);
			comp.text2 = (TextView) convertView
					.findViewById(R.id.Tunnel_info_listview_text2);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		try {
			comp.text1.setText(name[position]);
			
			comp.text2.setText(list.get(position).toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		public TextView text2;

	}

}
