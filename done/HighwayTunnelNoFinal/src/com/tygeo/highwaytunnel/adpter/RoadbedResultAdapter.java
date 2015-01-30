package com.tygeo.highwaytunnel.adpter;

import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.activity.NewphotoShow;
import com.tygeo.highwaytunnel.activity.PhotoShow;
import com.tygeo.highwaytunnel.adpter.CivilContentAdapter.listviewButtonListener;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.CivilCheckInfo;
import com.tygeo.highwaytunnel.entity.CivilContentE;
import com.tygeo.highwaytunnel.entity.LineSearch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class RoadbedResultAdapter extends BaseAdapter {
	// ��ѯ����������
	private List<CivilContentE> list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;
	int gid;
	String p_name;
	DBHelper db = new DBHelper(StaticContent.DataBasePath);
	ResultComp comp;

	public RoadbedResultAdapter(List<CivilContentE> list, Context ctx) {
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
		comp = null;
		int p = position;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.civil_portal_checkresult_list, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView
					.findViewById(R.id.Civil_Por_CheckResultT1);
			comp.textcheckdate = (TextView) convertView
			.findViewById(R.id.Civil_Por_CheckResultCheckData);
			comp.text2 = (TextView) convertView
					.findViewById(R.id.Civil_Por_CheckResultT2);
			comp.text3 = (TextView) convertView
					.findViewById(R.id.Civil_Por_CheckResultT3);
			comp.text4 = (TextView) convertView
					.findViewById(R.id.Civil_Por_CheckResultT4);
			comp.text5 = (TextView) convertView
					.findViewById(R.id.Civil_Por_CheckResultT5);
			comp.button1 = (Button) convertView
					.findViewById(R.id.Civil_Por_CheckResultBtn);
			comp.button2 = (Button) convertView
					.findViewById(R.id.Civil_Por_PhotoBtn);

			comp.button1.setTag(p);

			comp.button2.setTag(p);

			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}

		comp.text1.setText(list.get(position).getMileage());
		comp.textcheckdate.setText(list.get(position).getCheck_data());
		comp.text2.setText(list.get(position).getLevel_content());
		comp.text3.setText(list.get(position).getJudge_level());
		comp.text4.setText((list.get(position).getPic_id()).toString());
		comp.text5.setText(list.get(position).getBZ());
		comp.button1.setOnClickListener(new listviewButtonListener(position));
		
		comp.button2.setOnClickListener(new PhotoButtonListener(position));
		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		public TextView text2;
		public TextView text3;
		public TextView text4;
		public TextView text5,textcheckdate;
		public Button button1, button2;
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
						.setTitle("ȷ��ɾ����")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										gid = list.get(position).get_id();
										String sql = "delete from CILIV_CHECKCONTENT where _id ='"
												+ gid + "'";
										System.out.println(gid);
										System.out.println(sql);
										db.execSql(sql);
										list.remove(position);
										notifyDataSetChanged();
									}
								})
						.setNegativeButton("ȡ��",
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

	class PhotoButtonListener implements View.OnClickListener {
		private int position;

		public PhotoButtonListener(int i) {
			position = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == comp.button2.getId()) {
				gid = list.get(position).get_id();
				p_name = list.get(position).getCheck_data();
				AlertDialog dialog = new AlertDialog.Builder(ctx)
						.setTitle("�Ƿ�����?")
						.setPositiveButton("ѡ��������Ƭ",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();

										StaticContent.bh_id = gid + "";
										StaticContent.BH_p_name = p_name;
										Intent intent = new Intent(ctx,
												NewphotoShow.class);

										ctx.startActivity(intent);
										// intent.putExtra("",St);

									}
								})
						.setNegativeButton("��",
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();

									}
								}).show();
			}

		}
	}

}
