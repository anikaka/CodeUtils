package com.tongyan.activity.adapter;


import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.entities._ContactsData;

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
 * @ClassName P15_ContactsDepartAdapter 
 * @author wanghb
 * @date 2013-7-19 am 09:43:47
 * @desc TODO
 */
public class OaContactsDepartAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	OnClickListener onClickListener;
	private List<_ContactsData> contactArr;
	//private Map<String, String> map = new HashMap<String, String>();
	
	public OaContactsDepartAdapter(Context context, List<_ContactsData> arr, OnClickListener listener) {
		layoutInflater = LayoutInflater.from(context);
		this.onClickListener = listener;
		contactArr = arr;
	}
	
	@Override
	public int getCount() {
		return contactArr == null ? 0 : contactArr.size();
	}

	@Override
	public Object getItem(int position) {
		if (contactArr != null) {
			return contactArr.get(position);
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
			holder.firstCharHintTextView = (TextView) convertView.findViewById(R.id.p16_contacts_text_first_char_hint);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.p16_contacts_text_website_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameTextView.setText(contactArr.get(position).getEmpName());
		int idx = position - 1;
		String previewChar = idx >= 0 ? contactArr.get(idx).getDptName() : "";
		
		String currentChar = contactArr.get(position).getDptName();
		
		if (currentChar != null && !currentChar.equalsIgnoreCase(previewChar)) {
			holder.firstCharHintTextView.setVisibility(View.VISIBLE);
			holder.firstCharHintTextView.setTextColor(Color.parseColor("#11354d"));
			holder.firstCharHintTextView.setText(currentChar);
		} else {
			//实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
			holder.firstCharHintTextView.setVisibility(View.GONE);
		}
		holder.contactsData = contactArr.get(position);
		return convertView;
	}
	
	public final class ViewHolder {
		public TextView firstCharHintTextView;
		public TextView nameTextView;
		public _ContactsData contactsData;
	}
}
