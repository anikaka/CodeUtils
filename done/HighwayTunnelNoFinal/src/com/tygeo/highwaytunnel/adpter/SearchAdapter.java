package com.tygeo.highwaytunnel.adpter;

import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.R.color;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private List<LineSearch> list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;
	
	public SearchAdapter(List<LineSearch> list, Context ctx) {
		this.list = list;
		this.ctx = ctx;
		this.inflater = LayoutInflater.from(ctx);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public void setSelectPosition(int position) {
		selectItem = position;
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
			convertView = inflater.inflate(R.layout.project_list, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView.findViewById(R.id.Pro_list_text);
			comp.imageView = (ImageView)convertView.findViewById(R.id.Pro_list_img);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		if (position == selectItem) {
			//convertView.setBackgroundResource(R.color.p03_list_click_background_color);
			comp.text1.setTextColor(Color.parseColor("#68c4f8"));
			comp.imageView.setBackgroundResource(R.drawable.radio_select);
		} else {
			//convertView.setBackgroundResource(R.color.p03_list_background_color);
			comp.text1.setTextColor(Color.WHITE);
			comp.imageView.setBackgroundResource(R.drawable.radio);
		}
		if("暂无相关信息".equals(list.get(position).getSection_name()))
			comp.imageView.setVisibility(View.INVISIBLE);
		comp.text1.setText(list.get(position).getSection_name());
		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		public ImageView imageView;
	}
	
}
