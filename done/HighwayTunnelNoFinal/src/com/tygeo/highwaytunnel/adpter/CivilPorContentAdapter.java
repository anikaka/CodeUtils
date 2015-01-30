package com.tygeo.highwaytunnel.adpter;

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

public class CivilPorContentAdapter extends BaseAdapter {
	private List<String> list;
	private LayoutInflater inflater;
	private int selectItem = -1;
	private Context ctx;

	public CivilPorContentAdapter(List<String> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
		this.inflater = LayoutInflater.from(ctx);

	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	public void setSelectPosition(int position) {
		selectItem = position;
	}
	
	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	
	public long getItemId(int position) {
		return position;
	}

	public int getposition(String name) {
		int i = list.indexOf(name);
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ResultComp comp = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.civil_porcontent_list, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView.findViewById(R.id.civil_porcontent_list_text);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		// if (position == 0) {
		// convertView.setSelected(true);
		// convertView.setBackgroundResource(R.drawable.blue);
		// }
		if (position ==selectItem) {
			convertView.setBackgroundResource(R.drawable.blue);
		} else {
			convertView.setBackgroundResource(R.drawable.maybehs);
		}
		
		comp.text1.setText(list.get(position));
		// if (position == 0) {
		// // convertView.setBackgroundResource(R.drawable.blue);
		//
		// }

		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		
	}

}
