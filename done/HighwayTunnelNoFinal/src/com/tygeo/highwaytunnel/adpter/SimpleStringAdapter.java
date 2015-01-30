package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleStringAdapter extends BaseAdapter {
	private ArrayList<String>  list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;

	public SimpleStringAdapter(ArrayList<String>  list, Context ctx) {
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
			convertView = inflater.inflate(R.layout.civil_porcontent_list, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView
					.findViewById(R.id.civil_porcontent_list_text);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		
		if (position == StaticContent.listpositonindex) {
			convertView.setBackgroundResource(R.drawable.blue);
		} else {
			convertView.setBackgroundResource(R.drawable.maybehs);
		}
		comp.text1.setText(list.get(position));
		return convertView;
	}
	
	public final class ResultComp {
		public TextView text1;

	}

}
