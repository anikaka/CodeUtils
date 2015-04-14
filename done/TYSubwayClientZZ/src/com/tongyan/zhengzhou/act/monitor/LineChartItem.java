
package com.tongyan.zhengzhou.act.monitor;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.tongyan.zhengzhou.common.widgets.charting.charts.LineChart;
import com.tongyan.zhengzhou.common.widgets.charting.data.ChartData;
import com.tongyan.zhengzhou.common.widgets.charting.data.LineData;
import com.tongyan.zhengzhou.common.widgets.charting.utils.Legend;
import com.tongyan.zhengzhou.common.widgets.charting.utils.Legend.LegendForm;
import com.tongyan.zhengzhou.common.widgets.charting.utils.Legend.LegendPosition;
import com.tongyan.zhengzhou.common.widgets.charting.utils.XLabels;
import com.tongyan.zhengzhou.common.widgets.charting.utils.XLabels.XLabelPosition;
import com.tongyan.zhengzhou.common.widgets.charting.utils.YLabels;
import com.tongyan.zhengzhou.act.R;

public class LineChartItem extends ChartItem {

	private String dirName;
	private ArrayList<String> mTime = new ArrayList<String>();
	
    public LineChartItem(ChartData cd, String name,ArrayList<String> time) {
        super(cd);
        dirName = name;
        mTime.addAll(time);
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.setDrawYValues(false);
        holder.chart.setHighlightEnabled(true);
        holder.chart.setTouchEnabled(true);
        holder.chart.setStartAtZero(false);
        holder.chart.setDescription(dirName);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDragScaleEnabled(true);
        holder.chart.setPinchZoom(false);
        //holder.chart.setBackgroundColor(Color.parseColor("#eeeeee"));
        XLabels xl = holder.chart.getXLabels();
        xl.setPosition(XLabelPosition.BOTTOM);
        
        YLabels y = holder.chart.getYLabels();
        
        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        String[] labels = new String[mTime.size()];
        for(int i = 0; i < mTime.size();i++){
        	labels[i] = mTime.get(i);
        }
        Legend l = holder.chart.getLegend();
        if(l != null){
        	 l.setPosition(LegendPosition.BELOW_CHART_RIGHT);
             l.setLegendLabels(labels);
             l.setOffsetLeft(TYPE_LINECHART);
             l.setOffsetRight(TYPE_LINECHART);
             l.setOffsetBottom(TYPE_LINECHART);
             l.setOffsetTop(TYPE_LINECHART);
        }
      
      holder.chart.animateX(1000);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
