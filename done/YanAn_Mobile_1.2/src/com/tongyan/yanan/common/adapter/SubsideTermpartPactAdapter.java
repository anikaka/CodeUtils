package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @category 标段适配器
 * @author ChenLang
 * @version YanAn 1.0
 */
public class SubsideTermpartPactAdapter  extends BaseAdapter {
	private ArrayList<HashMap<String, String>> mListPaceSelect;
	private ArrayList<HashMap<String, String>> mLotNumber=new ArrayList<HashMap<String,String>>();
	private Context context;
	private LayoutInflater inflater;
	private int resoureId;
	private HolderViewPactSelect holderView;

	public SubsideTermpartPactAdapter(Context context,ArrayList<HashMap<String, String>> list,int reoureId,ArrayList<HashMap<String, String>> lotNumber){
		this.context=context;
		this.mListPaceSelect=list;
		inflater=LayoutInflater.from(context);
		this.resoureId=reoureId;
		this.mLotNumber=lotNumber;
	}
	@Override
	public int getCount() {
		return mListPaceSelect.size();
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
		if(convertView==null){
			// 解析文件
			 convertView=inflater.inflate(resoureId, null);
			 holderView=new HolderViewPactSelect();
			 //查找资源ID
			 holderView.rlContent=(RelativeLayout)convertView.findViewById(R.id.rlContent_pactSelect_item);
			 holderView.rlTitlte=(RelativeLayout)convertView.findViewById(R.id.rlTitle_pactSelect_item);
			 holderView.txtTitle=(TextView)convertView.findViewById(R.id.txtTitle_pactSelect_item);
			 holderView.txtContent=(TextView)convertView.findViewById(R.id.txtContent_pactSelect_item);
			 holderView.mTxtLotNumber=(TextView)convertView.findViewById(R.id.txtLotNumber);
			 convertView.setTag(holderView);
		}else{
			holderView = (HolderViewPactSelect) convertView.getTag();
		}
		if (mListPaceSelect != null) {
			HashMap<String, String> map = mListPaceSelect.get(position);
			if (map != null) {
				String title = map.get("attribute");
				if("title".equals(title)) {
					holderView.txtTitle.setText(map.get("periodName"));
					holderView.rlTitlte.setVisibility(View.VISIBLE);
					holderView.rlContent.setVisibility(View.GONE);
				} else {
					holderView.txtContent.setText(map.get("LotName"));
					holderView.rlContent.setVisibility(View.VISIBLE);
					holderView.rlTitlte.setVisibility(View.GONE);
					holderView.mTxtLotNumber.setText("数量: "+"0");
					for(int i=0;i<mLotNumber.size();i++){
						HashMap<String, String> mLotMap=mLotNumber.get(i);
						String mNewId=map.get("NewId");
						String mLotId=mLotMap.get("lotId");
						if(mNewId.equals(mLotId)){
							holderView.mTxtLotNumber.setText("数量: "+mLotMap.get("dataCount"));
						}
						
					}
				}
//				if(){
//					
//				}
			} 
			holderView.mItemMap = map;
		}
		
		return convertView;
	}

	public class HolderViewPactSelect {
		private RelativeLayout rlContent;
		private RelativeLayout rlTitlte;
		private TextView txtTitle;
		private TextView txtContent;
		private TextView mTxtLotNumber;//信息数量
		public HashMap<String, String> mItemMap;
	}
}
