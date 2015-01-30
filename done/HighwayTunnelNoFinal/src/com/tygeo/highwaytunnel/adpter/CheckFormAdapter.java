package com.tygeo.highwaytunnel.adpter;

import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.entity.CivilCheckInfo;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class CheckFormAdapter extends BaseAdapter {
	// 查询内容适配器
	private List<CivilContentE> list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;
	private int type;// 1土建 2衬砌 3机电

	public CheckFormAdapter(List<CivilContentE> list, Context ctx, int type) {
		this.list = list;
		this.type = type;
		this.ctx = ctx;
		this.inflater = LayoutInflater.from(ctx);

	}

	@Override
	public int getCount() {
	
		return list.size();
	}

	@Override
	public Object getItem(int position) {
	
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		ResultComp comp = null;

		if (convertView == null) {
			if (type == 1) {
				convertView = inflater.inflate(R.layout.check_form_list, null);
			}
			if (type == 2) {
				convertView = inflater.inflate(R.layout.check_from_lining_list,null);
			}

			comp = new ResultComp();

			comp.text1 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT1);
			comp.text2 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT2);
			comp.text3 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT3);
			comp.text4 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT4);
			comp.text5 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT5);
			comp.text6 = (TextView) convertView
					.findViewById(R.id.check_form_CheckResultT6);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();

		}
		if (type == 1) {
			comp.text1.setText(list.get(position).getMileage());
			if (list.get(position).getBelong_pro().equals("洞口")
					|| list.get(position).getBelong_pro().equals("洞门")) {
				comp.text2.setText(list.get(position).getBelong_pro() + "("
						+ list.get(position).getCheck_position() + ")");
			} else {
				comp.text2.setText(list.get(position).getBelong_pro());
			}
			comp.text3.setText(list.get(position).getCheck_data());
			comp.text4.setText(list.get(position).getLevel_content());
			comp.text5.setText(list.get(position).getJudge_level());
			comp.text6.setText(list.get(position).getBZ());
		}
		if (type == 2) {
			comp.text1.setText(list.get(position).getMileage());
			comp.text2.setText(list.get(position).getCheck_data());
			comp.text3.setText(list.get(position).getCheck_position());
			comp.text4.setText(list.get(position).getLevel_content());
			comp.text5.setText(list.get(position).getJudge_level());
			comp.text6.setText(list.get(position).getBZ());
		}

		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		public TextView text2;
		public TextView text3;
		public TextView text4;
		public TextView text5;
		public TextView text6;
	}

}
