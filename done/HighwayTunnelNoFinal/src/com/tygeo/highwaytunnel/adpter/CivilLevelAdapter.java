package com.tygeo.highwaytunnel.adpter;

import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilCheckInfo;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.app.Activity;
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

public class CivilLevelAdapter extends BaseAdapter {
	private List<String> list;

	private int selectItem = -1;
	RadioButton tempButton;
	private LayoutInflater inflater;
	private Context ctx;
	String[] level = { "S", "B", "A" };
	Activity activity;
	private int clickPosition = -1;

	public CivilLevelAdapter(List<String> list, Context ctx, Activity activity) {
		this.list = list;
		this.ctx = ctx;
		this.activity = activity;
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
		int p = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.civil_config_level_list,
					null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView
					.findViewById(R.id.Civil_Config_Level_List_Text);
			comp.btn = (Button) convertView
					.findViewById(R.id.Civil_Config_Level_List_Btn);
			comp.rb = (RadioButton) convertView
					.findViewById(R.id.Civil_Config_Level_List_Rbtn);

			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}

		comp.text1.setText(list.get(position));
		comp.rb.setId(position);
		comp.btn.setText(level[position]);

		comp.rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					if (clickPosition != -1) {
						tempButton = (RadioButton) activity
								.findViewById(clickPosition);

						if (tempButton != null) {
							tempButton.setChecked(false);
							System.out.println(clickPosition);

						}
					}
					clickPosition = buttonView.getId();
				}

				// notifyDataSetChanged();

			}
		});
		if (clickPosition == p) {
			comp.rb.setChecked(true);
		} else {
			comp.rb.setChecked(false);
		}

		System.out.println(clickPosition);
		StaticContent.CivilListView3Position = clickPosition;
		return convertView;

	}

	public final class ResultComp {
		public TextView text1;
		public Button btn;
		public RadioButton rb;

	}

}
