package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ºÏ≤ÈœÓƒøListView  ≈‰∆˜
 * @author Administrator
 *
 */
public class CheckProjectAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int  mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public CheckProjectAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
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
		
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mCheckName=(TextView)convertView.findViewById(R.id.textViewCheckProject);
			mViewHolder.img=(ImageView)convertView.findViewById(R.id.imgArrow);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		 HashMap<String, String> map=mArrayList.get(position);
		 if(map!=null){			 
			 mViewHolder.mCheckName.setText(map.get("name"));
			 if("true".equals(map.get("isCheck"))){
				 mViewHolder.img.setVisibility(View.VISIBLE);
			 }else{
				 mViewHolder.img.setVisibility(View.GONE);
			 }
			 mViewHolder.map=map;
		 }
		 
		return convertView;
	}

	public  class ViewHolder{
		private TextView mCheckName;
		private ImageView img;
		public HashMap<String, String> map;
	}
}
