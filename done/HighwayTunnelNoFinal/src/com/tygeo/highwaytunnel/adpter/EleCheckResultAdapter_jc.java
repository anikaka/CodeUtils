package com.tygeo.highwaytunnel.adpter;

import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.adpter.CivilContentAdapter.listviewButtonListener;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilCheckInfo;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.EleContentE;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class EleCheckResultAdapter_jc extends BaseAdapter {
	// 查询内容适配器
	private List<EleContentE> list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	DBHelper db = new DBHelper(StaticContent.DataBasePath);
	int gid;
	private Context ctx;
	ResultComp comp;
	private int pors;

	// 1当前结果 2检查表

	public EleCheckResultAdapter_jc(List<EleContentE> list, Context ctx,
			int pors) {
		this.list = list;
		this.ctx = ctx;
		this.pors = pors;
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
		comp = null;
		final int p = position;
		if (convertView == null) {
			if (pors == 1) {
				convertView = inflater.inflate(
						R.layout.ele_jc_checkresult_list, null);
				comp = new ResultComp();
				comp.text1 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV1);
				comp.text2 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV2);
				comp.text3 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV3);
				comp.text4 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV4);
				comp.text5 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV5);
				comp.text6 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV6);
				comp.text7 = (TextView) convertView
						.findViewById(R.id.ele_rc_checkresultTV7);
				comp.button1 = (Button) convertView
						.findViewById(R.id.Civil_Por_CheckResultBtn);
				comp.button1.setTag(position);
				
			}
			if (pors == 2) {

				convertView = inflater.inflate(R.layout.check_form_ele_list,
						null);
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
				comp.text7 = (TextView) convertView
						.findViewById(R.id.check_form_CheckResultT7);
				// comp.button1=(Button)convertView.findViewById(R.id.Civil_Por_CheckResultBtn);

			}

			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();

		}

		comp.text1.setText(position + "");
		comp.text2.setText(list.get(position).getDe_date());
		comp.text3.setText(list.get(position).getDevice_name());
		comp.text4.setText(list.get(position).getDevice_position());
		comp.text5.setText(list.get(position).getCheck_pro());
		comp.text6.setText(list.get(position).getCheck_content());
		comp.text7.setText(list.get(position).getHandle());
		if (pors == 1) {
			comp.button1.setOnClickListener(new listviewButtonListener(position));
			
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
		public TextView text7;
		public Button button1;

	}


	class listviewButtonListener implements View.OnClickListener {
		private int position;

		public listviewButtonListener(int i) {
			position = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == comp.button1.getId()) {

				AlertDialog dialog = new AlertDialog.Builder(ctx)
						.setTitle("确定删除？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
							
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										gid = list.get(position).get_id();
										String sql = "delete from ELECTRICAL_FAC where _id ='"
												+ gid + "'";
										System.out.println(gid);
										System.out.println(sql);
										db.execSql(sql);
										list.remove(position);
										notifyDataSetChanged();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).show();

			}
			;

		}

	}
}
