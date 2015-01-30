package com.tygeo.highwaytunnel.adpter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.adpter.TunnelInfoAdapter.ResultComp;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskShowAdapter extends BaseAdapter {

	private List list;
	private int selectItem = -1;
	private LayoutInflater inflater;
	private Context ctx;
//	private String[] name = { "������ڣ�", "��ʼ׮��: ", "����׮�� : ", "������� :   �����ṹ: ",
//			"                     ������ʩ: ", "����鳤 : ", "�����Ա : ", "��鷽�� : ",
//			"����������", "������", "�¶�(��)��", "ʪ��(%)��", "��Ƭ��ʼ���: ", "�������: ", "Ѳ�ӳ���",
//			"��ҵ����" };
	private String[] name = { "�������: ", "������ڣ�", "��ʼ׮��: ", "����׮�� : ", "������� :   �����ṹ: ",
			"                     ������ʩ: ", "����鳤 : ", "�����Ա : ", "��鷽�� : ",
			 "������", "�¶�(��)��", "ʪ��(%)��", "��Ƭ��ʼ���: ", "Ѳ�ӳ���" };

	public TaskShowAdapter(List list, Context ctx) {
		this.ctx = ctx;
		this.list = list;
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
			convertView = inflater.inflate(R.layout.task_show_listview, null);
			comp = new ResultComp();
			comp.text1 = (TextView) convertView.findViewById(R.id.Task_show_listview_text1);
			comp.text2 = (TextView) convertView.findViewById(R.id.Task_show_listview_text2);
			comp.text2.setId(position);
			convertView.setTag(comp);
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		comp.text1.setText(name[position]);
		comp.text2.setText(list.get(position).toString());
		return convertView;
	}

	public final class ResultComp {
		public TextView text1;
		public TextView text2;

	}

}
