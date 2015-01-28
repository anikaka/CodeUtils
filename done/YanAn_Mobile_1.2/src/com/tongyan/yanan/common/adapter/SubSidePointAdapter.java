package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.SubsideTypeAdapter.HolderView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author ChenLang
 * @category 地表检测点界面的ListView
 * @date 2014/06/20
 *  @version YanAn 1.0
 */

public class SubSidePointAdapter  extends BaseAdapter{

	private Context mContext;
	private ArrayList<HashMap<String, String>>  mListPoint;
	private LayoutInflater mInflater;
	private int mResoureId;
	HolderViewSubSidePoint mHolderViewSubSidePoint;
	public SubSidePointAdapter(Context context, ArrayList<HashMap<String, String>> list,int resoureId){
		this.mContext=context;
		this.mListPoint=list;
		this.mInflater=LayoutInflater.from(context);
		this.mResoureId=resoureId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListPoint.size();
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
			convertView=mInflater.inflate(mResoureId, null);
			mHolderViewSubSidePoint=new HolderViewSubSidePoint();
			mHolderViewSubSidePoint.txtMonitorPointName=(TextView)convertView.findViewById(R.id.txtPoint_subside_pointName);
			 mHolderViewSubSidePoint.txtMonitorPointValue=(TextView)convertView.findViewById(R.id.txtPoint_subside_point);
			convertView.setTag(mHolderViewSubSidePoint);
		}else{
			mHolderViewSubSidePoint=(HolderViewSubSidePoint)convertView.getTag();
			
			}
		if(mListPoint.size()>0 && mListPoint!=null){				
//			for(HashMap<String, String> map:mListPoint){
//						String  monitorName=map.get("monitorName");
//						String  pointX=map.get("pointX");
//						String  pointY=map.get("pointY");
//						String  pointZ=map.get("pointZ");
//				mHolderViewSubSidePoint.txtMonitorPoint.setText(monitorName+"( "+"X:"+pointX+", Y="+pointY+",  Z:"+pointZ+" )");
//			}
			HashMap<String, String> map=mListPoint.get(position);
						String  monitorName=map.get("monitorName");
						String  pointX=map.get("pointX");
						String  pointY=map.get("pointY");
						String  pointZ=map.get("pointZ");
				mHolderViewSubSidePoint.txtMonitorPointName.setText(monitorName);
				if(pointZ==null || "".equals(pointZ)||"null".equals(pointZ)){						
					mHolderViewSubSidePoint.txtMonitorPointValue.setText("( "+"X:"+pointX+", Y="+pointY+",  Z:"+""+" )");
				}else{						
					mHolderViewSubSidePoint.txtMonitorPointValue.setText("( "+"X:"+pointX+", Y="+pointY+",  Z:"+pointZ+" )");
				}
				mHolderViewSubSidePoint.mapHolderViewSubSidePoint=map;
			}
		return convertView;
	}

	public class HolderViewSubSidePoint{
		TextView  txtMonitorPointName;
		TextView  txtMonitorPointValue;
		public HashMap<String, String> mapHolderViewSubSidePoint;
	}
}
