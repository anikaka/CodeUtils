package com.tygeo.highwaytunnel.adpter;

import java.util.List;

import com.TY.bhgis.Database.DataProvider;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.adpter.SearchAdapter.ResultComp;
import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.Task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskInfoAdapter extends BaseAdapter {
	private List<Task> list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private int check_type;// 1土建2机电3土建/机电
	private Context ctx;

	public TaskInfoAdapter(List<Task> list, Context ctx, int check_type) {
		this.list = list;
		this.ctx = ctx;
		this.check_type = check_type;
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
			convertView = inflater.inflate(R.layout.task_info_listview, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView
					.findViewById(R.id.Tast_info_list_text1);
			comp.text2 = (TextView) convertView
					.findViewById(R.id.Tast_info_list_text2);
			comp.text3 = (TextView) convertView
					.findViewById(R.id.Tast_info_list_text3);
			comp.text4 = (TextView) convertView
					.findViewById(R.id.Tast_info_list_text4);
			comp.text5 = (TextView) convertView
					.findViewById(R.id.Tast_info_list_text5);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		comp.text1.setText(list.get(position).getTask_name() + "-");

		comp.text2.setText(list.get(position).getTitile());
		
		comp.text3.setText("检查日期：" + list.get(position).getCheck_date());
		comp.text4.setText("检查里程：" + "起始:"+list.get(position).getUp_num()+"/"+"结束:"+list.get(position).getDown_num());
		comp.text5.setText("检查方向：" + DataProvider.getBaseCSName("检查方向",Integer.parseInt(list.get(position).getUp_check_direciton())));
//		comp.text5.setText("检查方向：" + list.get(position).getUp_check_direciton());

		return convertView;
	}
	
	public final class ResultComp {
		public TextView text1;
		public TextView text2;
		public TextView text3;
		public TextView text4;
		public TextView text5;
		
	}
}
