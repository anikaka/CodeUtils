package com.tongyan.zhengzhou.act.fragment.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tongyan.zhengzhou.common.widgets.charting.data.ChartData;
import com.tongyan.zhengzhou.common.widgets.charting.data.Entry;
import com.tongyan.zhengzhou.common.widgets.charting.data.LineData;
import com.tongyan.zhengzhou.common.widgets.charting.data.LineDataSet;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.monitor.ChartItem;
import com.tongyan.zhengzhou.act.monitor.LineChartItem;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;


public class MonitorDiagramFragment extends MFinalFragment{
	
	public static final String LEFT_DIR_X = "LeftDirX";
	public static final String RIGHT_DIR_X = "RightDirX";
	public static final String LEFT_DIR_NAME ="LeftDirName";
	public static final String RIGHT_DIR_NAME ="RightDirName";
	public static final String LEFT_DIRECTION = "LeftDirection";
	public static final String RIGHT_DIRECTION = "RightDirection";
	public static final String TIME = "Time";
	private Context mContext;
	private int[] mColors = new int[] { R.color.vordiplom_1, R.color.vordiplom_2, R.color.vordiplom_3, 
										R.color.vordiplom_4, R.color.vordiplom_5, R.color.vordiplom_6, 
										R.color.vordiplom_7, R.color.vordiplom_8, R.color.vordiplom_9};
	private ListView mListView;
	private HashMap<String, Object> mBaseMap = new HashMap<String, Object>();
	
	public static MonitorDiagramFragment getInstance(Context context,HashMap<String, Object> map){
		MonitorDiagramFragment fragment = new MonitorDiagramFragment();
		fragment.mContext = context;
		fragment.mBaseMap.putAll(map);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.monitor_diagram_fragment, container, false);
		mListView = (ListView) view.findViewById(R.id.listView);
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		if(mBaseMap != null){
			//上行
			ArrayList<String> leftXList = getXList(LEFT_DIR_X);
			ArrayList<ArrayList<String>> leftYList = (ArrayList<ArrayList<String>>) getYList(LEFT_DIRECTION,TIME).get("y");
			String leftName = (String) mBaseMap.get(LEFT_DIR_NAME);
			ArrayList<String> leftTimeList = (ArrayList<String>) getYList(LEFT_DIRECTION,TIME).get("time");
//			ArrayList<String> leftTimeList = new ArrayList<String>();
//			leftTimeList.add("2014-09-06 03:00:00");
//			leftTimeList.add("2014-09-05 03:00:00");
//			leftTimeList.add("2014-09-04 03:00:00");
//			leftTimeList.add("2014-09-03 03:00:00");
//			leftTimeList.add("2014-09-02 03:00:00");
//			leftTimeList.add("2014-09-01 03:00:00");
			list.add(new LineChartItem(generateDataLine(leftXList,leftYList),leftName,leftTimeList));
			//下行
			ArrayList<String> rightXList = getXList(RIGHT_DIR_X);
			ArrayList<ArrayList<String>> rightYList = (ArrayList<ArrayList<String>>) getYList(RIGHT_DIRECTION,TIME).get("y");
			String rightName = (String) mBaseMap.get(RIGHT_DIR_NAME);
			ArrayList<String> rightTimeList = (ArrayList<String>) getYList(RIGHT_DIRECTION,TIME).get("time");
			list.add(new LineChartItem(generateDataLine(rightXList,rightYList),rightName,rightTimeList));
		}
		//ChartDataAdapter cda = new ChartDataAdapter(mContext, list);
		mListView.setAdapter(new ChartDataAdapter(mContext, list));
		return view;
	}
	/**
	 * 获得控制点
	 * @param dirX
	 * @return
	 */
	public ArrayList<String> getXList(String dirX){
		ArrayList<String> xList = new ArrayList<String>();
		ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) mBaseMap.get(dirX);
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				HashMap<String, String> map = list.get(i);
				if(map != null){
					xList.add(map.get("x"));
				}
			}
		}
		return xList;
	}
	/**
	 * 获取落差量
	 * @param direcion
	 * @return
	 */
	public HashMap<String, Object> getYList(String direcion,String time){
		HashMap<String, Object> objectMap = new HashMap<String, Object>();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<ArrayList<String>> yList = new ArrayList<ArrayList<String>>();
		ArrayList<HashMap<String, Object>> allList = (ArrayList<HashMap<String, Object>>) mBaseMap.get(direcion);
		if(allList != null){
			for(int i = 0; i < allList.size(); i++){
				HashMap<String, Object> map = allList.get(i);
				if(map != null){
					yList.add( (ArrayList<String>) map.get(direcion));
					timeList.add((String) map.get(time));
				}
			}
		}
		objectMap.put("time", timeList);
		objectMap.put("y", yList);
		return objectMap;
	}
	
	/**
	 * LineData
	 * @param xVals
	 * @param yVals
	 * @return
	 */
	public ChartData generateDataLine(ArrayList<String> xVals,ArrayList<ArrayList<String>> yVals){
      

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < yVals.size(); i++) {

            ArrayList<Entry> values = new ArrayList<Entry>();
            ArrayList<String> yList = yVals.get(i);
            for (int j = 0; j < yList.size(); j++) {
                values.add(new Entry(Float.parseFloat(yList.get(j)), j));
            }
            LineDataSet d = new LineDataSet(values, "DataSet " + (i + 1));
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);
            
            int color = getResources().getColor(mColors[i % mColors.length]);
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // make the first DataSet dashed
//        dataSets.get(0).enableDashedLine(10, 10, 0);
        if(dataSets != null && dataSets.size() > 0){
        	LineData data = new LineData(xVals, dataSets);
            return data;
        }
        
        return null;
        
	}
	
	 /*private class ChartDataAdapter extends BaseAdapter {
	        
		private List<ChartItem> mList;
		
		
		public ChartDataAdapter(Context context, List<ChartItem> objects) {
			this.mList = objects;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChartDataAdapter.ViewHolder mViewHolder = null;
			if(convertView == null) {
				mViewHolder = new ChartDataAdapter.ViewHolder();
				//convertView = LayoutInflater.from(getActivity()).inflate(R.layout.common_list_info_item, null);
				//TextView view = (TextView)convertView.findViewById(R.id.common_list_item_text);
				//mViewHolder.mContent = view;
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder)convertView.getTag();
			}
			ChartItem m = mList.get(position);
			//mViewHolder.mContent.setText(m.getItemType());
			mViewHolder.mChartItem = m;
			return convertView;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}
	    	class ViewHolder {
	    		//public TextView mContent;
	    		public ChartItem mChartItem;
	    	}
	    }*/

	 private class ChartDataAdapter extends ArrayAdapter<ChartItem> {
	        
	        public ChartDataAdapter(Context context, List<ChartItem> objects) {
	            super(context, 0, objects);
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return getItem(position).getView(position, convertView, getContext());
	        }
	        
	        @Override
	        public int getItemViewType(int position) {           
	            // return the views type
	            return getItem(position).getItemType();
	        }
	        
	        @Override
	        public int getViewTypeCount() {
	            return 3; // we have 3 different item-types
	        }
	    }
	 
	 
}
