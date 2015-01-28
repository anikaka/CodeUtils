package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.oa.OaContactsSelectAct;
import com.tongyan.yanan.common.db.DBService;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 
 * @Title: OaContactsSelectorAdapter.java 
 * @author Rubert
 * @date 2014-8-7 AM 09:25:15 
 * @version V1.0 
 * @Description: TODO
 */
public class OaContactsSelectorAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private ArrayList<HashMap<String, String>> stringArr;
	private Context mContext = null;
	public OaContactsSelectorAdapter(Context context, ArrayList<HashMap<String, String>> arr) {
		layoutInflater = LayoutInflater.from(context);
		stringArr = arr;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return stringArr == null ? 0 : stringArr.size();
	}

	@Override
	public Object getItem(int position) {
		if (stringArr != null) {
			return stringArr.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.oa_contacts_selector_list_item, null);
			holder = new ViewHolder();
			holder.firstCharHintTextView = (TextView) convertView.findViewById(R.id.contacts_text_first_char_hint);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.contacts_text_website_name);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.contacts_text_checked_box);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> map = stringArr.get(position);
		
		holder.nameTextView.setText(map.get("UserName"));
		int idx = position - 1;
		char previewChar = idx >= 0 ? stringArr.get(idx).get("UserNamePinyin").charAt(0) : ' ';
		char currentChar = map.get("UserNamePinyin").charAt(0);
		if (currentChar != previewChar) {
			holder.firstCharHintTextView.setVisibility(View.VISIBLE);
			holder.firstCharHintTextView.setTextColor(Color.parseColor("#11354d"));
			holder.firstCharHintTextView.setText(String.valueOf(currentChar).toUpperCase());
		} else {
			//实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
			holder.firstCharHintTextView.setVisibility(View.GONE);
		}
		final String mDSelect = map.get("DSelect");
		if(mDSelect != null && "1".equals(mDSelect)) {
			holder.mCheckBox.setChecked(true);
		} else {
			holder.mCheckBox.setChecked(false);
		}
		
		holder.contactsData = map;
		
		holder.mCheckBox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mDSelect != null && "1".equals(mDSelect)) {
					new DBService(mContext).updateContactsSelect(map.get("ID"), "0");
				} else {
					new DBService(mContext).updateContactsSelect(map.get("ID"), "1");
				}
				OaContactsSelectAct.refreshListView(mContext);
			}
		});
		
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView firstCharHintTextView;
		public TextView nameTextView;
		public CheckBox mCheckBox;
		public HashMap<String, String> contactsData;
	}

}
