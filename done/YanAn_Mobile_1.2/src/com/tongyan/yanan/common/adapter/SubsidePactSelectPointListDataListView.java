package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @category 数据列表适配器
 * @author ChenLang
 *@version YanAn 1.0
 */
public class SubsidePactSelectPointListDataListView extends BaseAdapter {

	private Context mContext;
	public ArrayList<HashMap<String, String>> mListPointData;
	private  LayoutInflater mInflater;
	private int mResoureId;
	private HolderViewSubsidePactSelectPointListDataListView mHolderViewPointListDataList;
	public SubsidePactSelectPointListDataListView(Context context,ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mListPointData=list;
		mInflater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListPointData.size();
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
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=mInflater.inflate(mResoureId, null);
			mHolderViewPointListDataList=new HolderViewSubsidePactSelectPointListDataListView();
		    mHolderViewPointListDataList.txtPoint_subside_point_datalist= (TextView)convertView.findViewById(R.id.txtPoint_subside_point_datalist);
		    mHolderViewPointListDataList.txtUploading_subside_point_datalist=(TextView)convertView.findViewById(R.id.txtUploading_subside_point_datalist);
		    mHolderViewPointListDataList.txtUploadingDate_subside_point_datalist=(TextView)convertView.findViewById(R.id.txtUploadingDate_subside_point_datalist);
		    convertView.setTag(mHolderViewPointListDataList);
		}else{
			mHolderViewPointListDataList=(HolderViewSubsidePactSelectPointListDataListView)convertView.getTag();
		}
			if(mListPointData.size()>0){
				HashMap<String, String> map=mListPointData.get(position);
			 String  monitorTypeName=	map.get("monitorTypeName"); //获取监测类型名称
			 String monitorName =map.get("monitorPointName"); //获取监测点名称
			 String  superviseDate=map.get("superviseDate"); //获取监测时间
			 String uploadState=map.get("uploadState");
			 if(!"".equals(monitorTypeName) && ! "".equals(monitorName)){
				 mHolderViewPointListDataList.txtPoint_subside_point_datalist.setText(monitorTypeName+" "+monitorName);
			 }
			 if(!"".equals(uploadState)){
				  if("1".equals(uploadState)){
					  mHolderViewPointListDataList.txtUploading_subside_point_datalist.setText("已上传");
					  mHolderViewPointListDataList.txtUploading_subside_point_datalist.setVisibility(View.VISIBLE);
				  }else{
					  mHolderViewPointListDataList.txtUploading_subside_point_datalist.setVisibility(View.INVISIBLE);
				  }
			 }
			 if(!"".equals(superviseDate)){
				 mHolderViewPointListDataList.txtUploadingDate_subside_point_datalist.setText(superviseDate);
			 }
			 mHolderViewPointListDataList.mapHolderViewSubsidePactSelectPointListDataListView=map;
			}

		return convertView;
	}
  
	public class HolderViewSubsidePactSelectPointListDataListView{
		TextView txtPoint_subside_point_datalist;
		TextView txtUploading_subside_point_datalist;
		TextView txtUploadingDate_subside_point_datalist;
		public HashMap<String, String>  mapHolderViewSubsidePactSelectPointListDataListView;
	}

}
