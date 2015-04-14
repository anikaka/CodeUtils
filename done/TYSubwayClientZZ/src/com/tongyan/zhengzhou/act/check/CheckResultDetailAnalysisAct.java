package com.tongyan.zhengzhou.act.check;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.R.id;
import com.tongyan.zhengzhou.act.R.layout;
import com.tongyan.zhengzhou.common.utils.Constants;
import com.tongyan.zhengzhou.common.utils.JSONParseUtils;
import com.tongyan.zhengzhou.common.utils.WebServiceUtils;
import com.tongyan.zhengzhou.common.widgets.charting.charts.BarChart;
import com.tongyan.zhengzhou.common.widgets.charting.data.BarData;
import com.tongyan.zhengzhou.common.widgets.charting.data.BarDataSet;
import com.tongyan.zhengzhou.common.widgets.charting.data.BarEntry;
import com.tongyan.zhengzhou.common.widgets.charting.data.Entry;
import com.tongyan.zhengzhou.common.widgets.charting.interfaces.OnChartValueSelectedListener;
import com.tongyan.zhengzhou.common.widgets.charting.utils.Legend;
import com.tongyan.zhengzhou.common.widgets.charting.utils.Legend.LegendPosition;
import com.tongyan.zhengzhou.common.widgets.charting.utils.LimitLine;
import com.tongyan.zhengzhou.common.widgets.charting.utils.XLabels;
import com.tongyan.zhengzhou.common.widgets.charting.utils.XLabels.XLabelPosition;
import com.tongyan.zhengzhou.common.widgets.charting.utils.YLabels;
import com.tongyan.zhengzhou.common.afinal.MFinalActivity;

public class CheckResultDetailAnalysisAct extends MFinalActivity implements OnChartValueSelectedListener, OnClickListener{

	private BarChart mChart;
	private LinearLayout backBtn;
    private TextView titleContent;
    private RelativeLayout backCheckManageBtn;
    private TextView chartName;

    private String mCheckObjectName;	//区间名
    
    private ArrayList<HashMap<String, String>> damageAnalysisList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_result_analysis_act);
        MApplication.getInstance().addActivity(this); 

        getNetData(); 	//从后台获取数据
        
        View titleLayout = findViewById(R.id.check_result_analysis_title);
        backBtn = (LinearLayout) titleLayout.findViewById(R.id.title_back_btn);
        titleContent = (TextView) titleLayout.findViewById(R.id.title_content);
        backCheckManageBtn = (RelativeLayout) titleLayout.findViewById(R.id.back_check_manage);
        backCheckManageBtn.setVisibility(View.VISIBLE);
        titleContent.setText("检查成果详细分析");
        
        chartName = (TextView) findViewById(R.id.chart_name);
        chartName.setText(mCheckObjectName);
        
        mChart = (BarChart) findViewById(R.id.bar_chart);
        mChart.setOnChartValueSelectedListener(this);
        backBtn.setOnClickListener(this);
        backCheckManageBtn.setOnClickListener(this);
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        // 
        mChart.setDrawYValues(true);

        // 仅能在x或者y方向缩放
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        
        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(true);
        mChart.setDrawVerticalGrid(false);
        
        //定义字形
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        //说明
        
        initLoadData();
        
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setTypeface(tf);
        
        //定义x标签和设置字形
        XLabels xl  = mChart.getXLabels();
        xl.setPosition(XLabelPosition.BOTTOM);
        xl.setCenterXLabelText(true);
        xl.setTypeface(tf);
        //定义y标签和设置字形
        YLabels yl = mChart.getYLabels();
        yl.setTypeface(tf);
        
        mChart.setValueTypeface(tf);
        mChart.animateXY(1000, 1000);
       
    }

    //初始化数据
    private void initLoadData(){

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(" ");
       

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        BarDataSet set1 = new BarDataSet(yVals1, " ");
        yVals1.add(new BarEntry(0, 0));
     
        set1.setColor(Color.rgb(255, 255, 255));
        
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setGroupSpace(110f);

        mChart.setData(data);
        mChart.invalidate();
    }
    
    //重新加载数据
    private void loadData(){

    	//将区间名称添加到xVals中（x轴的项目）（里程范围）
        ArrayList<String> xVals = new ArrayList<String>();
        //与x轴项目所对应的项目的y轴上的值（病害数量）
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
    	
        int index=0;
    	for(HashMap<String, String> damageAnalysis:damageAnalysisList){
    		String mMiliRange = damageAnalysis.get("MiliRange");	//里程范围
    		int mDamageNumber = Integer.parseInt(damageAnalysis.get("DamageNumber"));	//病害数量
    		xVals.add(mMiliRange);
    		yVals.add(new BarEntry(mDamageNumber, index));
    		index++;
    	}
    	
    	BarDataSet set = new BarDataSet(yVals, "病害数量");
        set.setColor(Color.BLUE);
    	
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(xVals, dataSets);
        
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateXY(1000, 1000);
    	
    	
       /* ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("市光路~嫩江路");
        xVals.add("嫩江路~翔殷路");
        xVals.add("翔殷路~黄兴公园");
       

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(5, 0));
        yVals1.add(new BarEntry(3, 1));
        yVals1.add(new BarEntry(6, 2));
        
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        yVals2.add(new BarEntry(2, 0));
        yVals2.add(new BarEntry(5, 1));
        yVals2.add(new BarEntry(7, 2));
        
       

        // create 3 datasets with different types
        BarDataSet set1 = new BarDataSet(yVals1, "2013年3月11日  上行");
//	        set1.setColors(ColorTemplate.createColors(getApplicationContext(), ColorTemplate.FRESH_COLORS));
        set1.setColor(Color.rgb(104, 241, 175));
        BarDataSet set2 = new BarDataSet(yVals2, "2013年3月11日  下行");
        set2.setColor(Color.rgb(164, 228, 251));
        
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(xVals, dataSets);
        
        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(110f);

        LimitLine ll = new LimitLine(2.5f);
        ll.setLineWidth(1f);
        ll.setLineColor(Color.RED);
        data.addLimitLine(ll);
        
        
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateXY(1000, 1000);
        */
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		// TODO Auto-generated method stub
		Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub back_check_manage
		switch (v.getId()) {
		case R.id.title_back_btn:
			setResult(Constants.MSG_0x3001);
			finish();
			break;
		case R.id.back_check_manage:
			setResult(Constants.MSG_0x3002);
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 获取网络数据
	 * */
	private void getNetData(){
		
		Intent intent = getIntent();
		if(intent==null){
			return;
		}
		final Bundle bundle = intent.getExtras();
		mCheckObjectName = bundle.get("CheckObjectName").toString();
		sendMessage(Constants.MSG_0x4002);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//选择的设施设备对象
				ArrayList<HashMap<String, String>> mCheckFacilityCodeList = (ArrayList<HashMap<String, String>>) bundle.get("CheckFacilityCodeList");
				//选择的病害
				ArrayList<HashMap<String, String>> mDamageTypeList = (ArrayList<HashMap<String, String>>) bundle.get("DamageTypeList");
		
				HashMap<String, String> mDamageAnalysisMap=new HashMap<String, String>();
				mDamageAnalysisMap.put("CheckObjectDetailCode", bundle.get("CheckObjectDetailCode").toString());
				mDamageAnalysisMap.put("CheckFacilityCodeList", JSONParseUtils.getJSONString(mCheckFacilityCodeList));	
				mDamageAnalysisMap.put("CheckingMethod", bundle.get("CheckingMethod").toString());	
				mDamageAnalysisMap.put("CheckingType", bundle.get("CheckingType").toString());	
				mDamageAnalysisMap.put("DamageTypeList", JSONParseUtils.getJSONString(mDamageTypeList));	
				mDamageAnalysisMap.put("CompareDate", bundle.get("CompareDate").toString());	
				String jsonStr="";
				try {
					jsonStr = WebServiceUtils.requestM(CheckResultDetailAnalysisAct.this, mDamageAnalysisMap, Constants.METHOD_OF_CLIENT_GetDAMAGELOCATION);
					damageAnalysisList = new JSONParseUtils().parseDamageLocation(jsonStr);
					if(damageAnalysisList == null){
						return;
					}
					sendMessage(Constants.MSG_0x3004);
				} catch (Exception e) {
					sendMessage(Constants.GET_DATA_ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Dialog mDialog = null;	//网络请求动画
	@Override
	protected void handleOtherMessage(int flag) {
		// TODO Auto-generated method stub
		
		super.handleOtherMessage(flag);
		switch (flag) {
		case Constants.MSG_0x3004:
			loadData();
			mDialog.dismiss();
			break;
		case Constants.MSG_0x4002:
			mDialog = new AlertDialog.Builder(this).create();
			mDialog.show();
			//注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.common_loading_process_dialog);
			mDialog.setCanceledOnTouchOutside(false);
			break;
		case Constants.GET_DATA_ERROR:
			Toast.makeText(this, "网络请求失败！", Toast.LENGTH_SHORT).show();
			mDialog.dismiss();
			break;
			
		default:
			break;
		}
	}
}

