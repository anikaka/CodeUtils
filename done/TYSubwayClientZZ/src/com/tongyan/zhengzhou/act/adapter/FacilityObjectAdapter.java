package com.tongyan.zhengzhou.act.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FacilityObjectAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, Object>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder  mViewHolder;
	
	public FacilityObjectAdapter(Context context,ArrayList<HashMap<String, Object>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final int index = position;
		
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null );
			mViewHolder.levelView=(TextView)convertView.findViewById(R.id.base_object_item_level);
			mViewHolder.objectCheckBox=(CheckBox)convertView.findViewById(R.id.base_object_item_checkBox);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		HashMap<String, Object> map=mArrayList.get(position);
		// PDicId（int）:父ID；ParamName（int）：设施名称;ParamValue（String）:设施的值;IsSelect（boolean）：是否选择了该对象
		if(map!=null){
			String objectLevelFormat = "";
			boolean objectIsSelect = (Boolean) map.get("IsSelect");
			String objectName = (String) map.get("ParamName");
			mViewHolder.objectCheckBox.setText(objectName);
			mViewHolder.objectCheckBox.setChecked(objectIsSelect);
			mViewHolder.objectCheckBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					boolean isChecked = (Boolean) mArrayList.get(index).get("IsSelect");
					if(isChecked){
						isChecked = false;
					}else{
						isChecked = true;
					}
					mArrayList.get(index).put("IsSelect", isChecked);
					int objectLevel = (Integer) mArrayList.get(index).get("ObjectLevel");
					for(int i=index+1; i<mArrayList.size(); i++){
						if(objectLevel>=(Integer) mArrayList.get(i).get("ObjectLevel")){
							break;
						}
						mArrayList.get(i).put("IsSelect", isChecked);
					}
					notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}

	public class ViewHolder{
		private TextView levelView; //等级
		private CheckBox objectCheckBox; //对象CheckBox
	}
}
