package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.progress.plan.PlanReportEditAct;
import com.tongyan.yanan.common.db.DBService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @category 进度计划适配器
 * @author wanghb
 * @date 	2014/07/08
 *@version  
 */

public class ProgressPlanEditListAdpater  extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInfalter;
	private ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	
	public ProgressPlanEditListAdpater(Context context, ArrayList<HashMap<String, String>> list, int resoureId){
		this.mContext=context;
		this.mArrayList=list;
		this.mResoureId=resoureId;
		mInfalter=LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
	 final ViewHolder viewHolder;
		if(convertView == null){
			 viewHolder = new ViewHolder();
			 convertView = mInfalter.inflate(mResoureId, null);
			 viewHolder.llTitle=(TextView)convertView.findViewById(R.id.progress_plan_report_list_item_title);
			 viewHolder.llContent=(LinearLayout)convertView.findViewById(R.id.progress_plan_report_list_item_content_container);
			 viewHolder.llCheckBox=(CheckBox)convertView.findViewById(R.id.progress_plan_report_list_item_box);
			 viewHolder.txtContentHint=(TextView)convertView.findViewById(R.id.progress_plan_report_list_item_content_hint);
			 viewHolder.mContentEdit=(TextView)convertView.findViewById(R.id.progress_plan_report_list_item_content_edit);
			 //设置标签
			 convertView.setTag(viewHolder); 
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
			final HashMap<String, String> map = mArrayList.get(position);
			if (map != null) {
				String title = map.get("ItemType");
				if ("title".equals(title)) {
					viewHolder.llTitle.setText(map.get("PConstructionName") + "(" + map.get("DName") + ")");
					viewHolder.llTitle.setVisibility(View.VISIBLE);
					viewHolder.llContent.setVisibility(View.GONE);
				} else {
					viewHolder.txtContentHint.setText(map.get("ConstructionName") + "(" + map.get("DName") + "):");
					viewHolder.llContent.setVisibility(View.VISIBLE);
					viewHolder.llTitle.setVisibility(View.GONE);
				}
				
				final String mIsChecked = map.get("IsEnable");
				
				if("true".equals(mIsChecked)) {
					viewHolder.llCheckBox.setChecked(true);
				} else {
					viewHolder.llCheckBox.setChecked(false);
				}
				
				String value = map.get("InputText");
				if(null != value && !"".equals(value)) {
					viewHolder.mContentEdit.setText(value);
				} else {
					viewHolder.mContentEdit.setText("");
				}
				
				viewHolder.llCheckBox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if("true".equals(mIsChecked)) {
							new DBService(mContext).updateProgressChecked("false", map.get("ID"));
						} else {
							new DBService(mContext).updateProgressChecked("true", map.get("ID"));
						}
					}
				});
		}
		viewHolder.mItemData = map;
		return convertView;
	}

	public class  ViewHolder{
	    private TextView llTitle;
	    private LinearLayout llContent;
	    private CheckBox llCheckBox;
		private TextView txtContentHint;
		private TextView mContentEdit;
		public HashMap<String,String> mItemData;
	}
}
