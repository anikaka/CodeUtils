package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @category进度计划适配器
 * @author ChenLang
 * @date 	2014/07/04
 *@version YanAn 1.0
 */

public class ProgressProjectListVeiwAdpater  extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInfalter;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private ArrayList<HashMap<String, String>> mLotNumber=new ArrayList<HashMap<String,String>>();
	private ViewHolderProgressProject viewHolder;
	
	public ProgressProjectListVeiwAdpater(Context context,ArrayList<HashMap<String, String>> list,int resoureId,ArrayList<HashMap<String, String>> lotNumber){
		this.mContext=context;
		this.mArrayList=list;
		this.mResoureId=resoureId;
		mInfalter=LayoutInflater.from(context);
		this.mLotNumber=lotNumber;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			 viewHolder=new ViewHolderProgressProject();
			 convertView=mInfalter.inflate(mResoureId, null);
			
			 viewHolder.llTitle=(RelativeLayout)convertView.findViewById(R.id.ll_title_progress_project);
			 viewHolder.llContent=(RelativeLayout)convertView.findViewById(R.id.ll_content_progress_project);
			 viewHolder.txtTitle=(TextView)convertView.findViewById(R.id.txt_item_title_progress_project);
			 viewHolder.txtContent=(TextView)convertView.findViewById(R.id.txt_item_content_progress_project);
			 viewHolder.mTxtLotNumber=(TextView)convertView.findViewById(R.id.txtNumberProject);
			 //设置标签
			 convertView.setTag(viewHolder); 
		}else{
			viewHolder =(ViewHolderProgressProject)convertView.getTag();
		}
		if (mArrayList.get(position).size() > 0) {
			HashMap<String, String> map = mArrayList.get(position);
			if (map != null && map.size() > 0) {
				String title = map.get("attribute");
				if ("title".equals(title)) {
					viewHolder.txtTitle.setText(map.get("periodName"));
					viewHolder.llTitle.setVisibility(View.VISIBLE);
					viewHolder.llContent.setVisibility(View.GONE);
				} else {
					viewHolder.txtContent.setText(map.get("LotName"));
					viewHolder.llContent.setVisibility(View.VISIBLE);
					viewHolder.llTitle.setVisibility(View.GONE);
					viewHolder.mTxtLotNumber.setText("数量: " + "0");
					for (int i = 0; i < mLotNumber.size(); i++) {
						HashMap<String, String> mLotMap = mLotNumber.get(i);
						String mNewId = map.get("NewId");
						String mLotId = mLotMap.get("lotId");
						if (mNewId.equals(mLotId)) {
							viewHolder.mTxtLotNumber.setText("数量: "+ mLotMap.get("dataCount"));
						}

					}
				}
				viewHolder.mMapProgressProject = map;
			}
		}
				  return convertView;
		
	}

	public class  ViewHolderProgressProject{
	    private RelativeLayout llTitle;
	    private RelativeLayout llContent;
		private TextView txtTitle;
		private TextView txtContent;
		private TextView mTxtLotNumber;
		public  HashMap<String, String>  mMapProgressProject;
	}
}
