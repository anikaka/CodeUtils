package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P16_ContactAllAdapter 
 * @author wanghb
 * @date 2013-7-18 pm 04:21:30
 * @desc TODO
 */
public class OaContactsAllAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private OnClickListener onClickListener;
	private ArrayList<HashMap<String, String>> stringArr;
	
	public OaContactsAllAdapter(Context context, ArrayList<HashMap<String, String>> arr, OnClickListener listener) {
		layoutInflater = LayoutInflater.from(context);
		this.setOnClickListener(listener);
		stringArr = arr;
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
			convertView = layoutInflater.inflate(R.layout.oa_contacts_all_list_item, null);
			holder = new ViewHolder();
			holder.firstCharHintTextView = (TextView) convertView.findViewById(R.id.contacts_text_first_char_hint);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.contacts_text_website_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = stringArr.get(position);
		
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
		holder.contactsData = map;
		return convertView;
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public final class ViewHolder {
		public TextView firstCharHintTextView;
		public TextView nameTextView;
		public HashMap<String, String> contactsData;
	}
	
}
