package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 照片上传 合同段适配器
 * @author ChenLang
 * @version YanAn 1.0
 */
public class PicPactAdapter  extends BaseAdapter{

	private Context  mContext;
	private ArrayList<HashMap<String, String>> mArrayList;
	private LayoutInflater mInflater;
	private int  mResoureId;
	//每个合同段显示的信息数目
	private ArrayList<HashMap<String, String>> mLotNumber=new ArrayList<HashMap<String,String>>();
	private ViewHolderPicPact mViewHodler;
	
	public PicPactAdapter(Context context,ArrayList<HashMap<String, String>> list,	int resoureId,ArrayList<HashMap<String, String>> lotNumber){
		this.mContext=context;
		this.mArrayList=list;
		this.mResoureId=resoureId;
		this.mInflater=LayoutInflater.from(context);
		this.mLotNumber=lotNumber;
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
		if(convertView==null){
			mViewHodler=new ViewHolderPicPact();
			convertView=mInflater.inflate(mResoureId, null);
			 //查找资源ID
			mViewHodler.rlContent=(RelativeLayout)convertView.findViewById(R.id.rlContent_pactSelect_item);
			mViewHodler.rlTitlte=(RelativeLayout)convertView.findViewById(R.id.rlTitle_pactSelect_item);
			mViewHodler.txtTitle=(TextView)convertView.findViewById(R.id.txtTitle_pactSelect_item);
			mViewHodler.txtContent=(TextView)convertView.findViewById(R.id.txtContent_pactSelect_item);
			mViewHodler.mTxtNumber=(TextView)convertView.findViewById(R.id.txtLotNumber);
		  convertView.setTag(mViewHodler);
		}else{
			mViewHodler =(ViewHolderPicPact)convertView.getTag();
		}
		if (mArrayList != null) {
			HashMap<String, String> map = mArrayList.get(position);
			if (map != null) {
				String title = map.get("attribute");
				if("title".equals(title)) {
					mViewHodler.txtTitle.setText(map.get("periodName"));
					mViewHodler.rlTitlte.setVisibility(View.VISIBLE);
					mViewHodler.rlContent.setVisibility(View.GONE);
				} else {
					mViewHodler.txtContent.setText(map.get("LotName"));
					mViewHodler.rlContent.setVisibility(View.VISIBLE);
					mViewHodler.rlTitlte.setVisibility(View.GONE);
					mViewHodler.mTxtNumber.setText("数量: "+"0");
					for(int i=0;i<mLotNumber.size();i++){
						HashMap<String, String> mLotMap=mLotNumber.get(i);
						String mNewId=map.get("NewId");
						String mLotId=mLotMap.get("lotId");
						if(mNewId.equals(mLotId)){
							mViewHodler.mTxtNumber.setText("数量: "+mLotMap.get("dataCount"));
						}
						
					}
				}
			} 
			mViewHodler.mMapPicPact = map;
		}
		return convertView;
	}

	public class ViewHolderPicPact{
		private RelativeLayout rlContent;
		private RelativeLayout rlTitlte;
		private TextView txtTitle;
		private TextView txtContent;
		private TextView mTxtNumber;
		public  HashMap<String,String> mMapPicPact;
	}
	
}
