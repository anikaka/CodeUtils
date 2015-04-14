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
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
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
import com.tongyan.zhengzhou.common.db.WebServiceDBService;

public class CheckResultAnalysisAct extends MFinalActivity implements OnChartValueSelectedListener, OnClickListener{

	private Bundle bundle;
	
    private BarChart mChart;
    private LinearLayout backBtn;
    private TextView titleContent;
    private View titleLayout;
    private TextView chartName;

    ArrayList<HashMap<String, Object>> damageAnalysisList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_result_analysis_act);
        MApplication.getInstance().addActivity(this); 
        
        getNetData();	//从后台获取数据
        
        View titleLayout = findViewById(R.id.check_result_analysis_title);
        backBtn = (LinearLayout) titleLayout.findViewById(R.id.title_back_btn);
        titleContent = (TextView) titleLayout.findViewById(R.id.title_content);
        titleContent.setText("检查成果分析");
        
        chartName = (TextView) findViewById(R.id.chart_name);
        chartName.setText("地铁病害分析");
        
        mChart = (BarChart) findViewById(R.id.bar_chart);
        mChart.setOnChartValueSelectedListener(this);
        backBtn.setOnClickListener(this);
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        //chartName.setBackgroundColor(mChart.getb);
        //mChart.setp
        
        // 
        mChart.setDrawYValues(true);

        // 仅能在x或者y方向缩放
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        
        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(true);
        
        // 创建一个自定义的 MarkerView (extend MarkerView) 和指定一个布局去使用它
       // MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // 设置偏量改变marker的最初位置
        //mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // 设置marker到chart中
        //mChart.setMarkerView(mv);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	switch (resultCode) {
		case Constants.MSG_0x3001:	//返回
			
			break;
		case Constants.MSG_0x3002:	//返回并销毁
			finish();
			break;
			
		default:
			break;
		}
    }
    
    
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
    
    private void loadData(){
    	
    	ArrayList<Object> mColorList = new ArrayList<Object>();	//柱状图颜色列表
    	mColorList.add(Color.BLUE);
    	mColorList.add(Color.GREEN);
    	mColorList.add(Color.YELLOW);
    	mColorList.add(Color.GRAY);
    	mColorList.add(Color.CYAN);
    	mColorList.add(Color.DKGRAY);
    	
    	ArrayList<String> mDateDirectionList = new ArrayList<String>();	//时间上下行项
    	
    	//将区间名称添加到xVals中
        ArrayList<String> xVals = new ArrayList<String>();
        //
        ArrayList<ArrayList<BarEntry>> yValsList = new ArrayList<ArrayList<BarEntry>>();
    	
        float mObjectNum=0;
        float mDamageNum=0;
        
        int i=0;
    	for(HashMap<String, Object> damageAnalysis:damageAnalysisList){
    		String mObjectName = (String) damageAnalysis.get("CheckObjectName");	//获取区间名称
    		xVals.add(mObjectName);
    		//获取该区间不同时间段的病害情况
    		ArrayList<HashMap<String, Object>> mCompareDateList = (ArrayList<HashMap<String, Object>>) damageAnalysis.get("CompareDateList");
    		int index = 0;
    		for(HashMap<String, Object> mCompareDateMap:mCompareDateList){
    			//获取该区间某一时间段的日期
    			String mCompareDate = (String) mCompareDateMap.get("CompareDate");
    			HashMap<String, Object> mDownDirection = (HashMap<String, Object>) mCompareDateMap.get("DownDirection");
    			HashMap<String, Object> mUpDirection = (HashMap<String, Object>) mCompareDateMap.get("UpDirection");
    			int mUpDamageNum = Integer.parseInt(mUpDirection.get("DamageNumber").toString());
    			int mDownDamageNum = Integer.parseInt(mDownDirection.get("DamageNumber").toString()) ;
    			
    			if(yValsList.size() == index){
    				ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        			yValsList.add(index, yVals);
        			mDateDirectionList.add(mCompareDate+" 上行");
    			}
    			BarEntry barEntry1 = new BarEntry(mUpDamageNum, i);
    			yValsList.get(index).add(barEntry1);
    			index++;
    			
    			if(yValsList.size() == index){
    				ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        			yValsList.add(index, yVals);
        			mDateDirectionList.add(mCompareDate+" 下行");
    			}
    			BarEntry barEntry2 = new BarEntry(mDownDamageNum, i);
    			yValsList.get(index).add(barEntry2);
    			index++;
    			
    			mDamageNum = mDamageNum+mUpDamageNum+mDownDamageNum;
    			mObjectNum=mObjectNum+2;
    		}
    		i++;
    	}
    	
    	ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
    	for(int index=0; index<mDateDirectionList.size(); index++){
    		BarDataSet set = new BarDataSet(yValsList.get(index), mDateDirectionList.get(index));
    		set.setColor((Integer)mColorList.get(index));
    		dataSets.add(set);
    	}
    	
        BarData data = new BarData(xVals, dataSets);
        data.setGroupSpace(110f);

        LimitLine ll = new LimitLine(mDamageNum/mObjectNum);
        ll.setLineWidth(1f);
        ll.setLineColor(Color.RED);
        data.addLimitLine(ll);
        
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateXY(1000, 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		// TODO Auto-generated method stub
		Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
		//e.getXIndex();
		String mCheckObjectDetailCode="";	//检查对象详细编号
		String mCheckObjectName = damageAnalysisList.get(e.getXIndex()).get("CheckObjectName").toString(); //检查对象名
		
		
		ArrayList<HashMap<String, Object>> mCompareDateList = (ArrayList<HashMap<String, Object>>) damageAnalysisList.get(e.getXIndex()).get("CompareDateList");
		String mCompareDate = mCompareDateList.get(dataSetIndex/2).get("CompareDate").toString();
		if(dataSetIndex%2==0){	//上行
			HashMap<String, Object> mUpDirection = (HashMap<String, Object>) mCompareDateList.get(dataSetIndex/2).get("UpDirection");
			mCheckObjectDetailCode = mUpDirection.get("CheckObjectDetailCode").toString();
			mCheckObjectName = mCheckObjectName+" (上行)";
		}else{		//下行
			HashMap<String, Object> mDownDirection = (HashMap<String, Object>) mCompareDateList.get(dataSetIndex/2).get("DownDirection");
			mCheckObjectDetailCode = mDownDirection.get("CheckObjectDetailCode").toString();
			mCheckObjectName = mCheckObjectName+" (下行)";
		}
		
		Intent intent = new Intent(CheckResultAnalysisAct.this, CheckResultDetailAnalysisAct.class);
		bundle.putString("CheckObjectDetailCode", mCheckObjectDetailCode);
		bundle.putString("CheckObjectName", mCheckObjectName);
		bundle.putString("CompareDate", mCompareDate);
		intent.putExtras(bundle);
		startActivityForResult(intent, Constants.MSG_0x3003);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_btn:
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
		bundle = intent.getExtras();
		sendMessage(Constants.MSG_0x4001);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//选择的区间对象
				ArrayList<HashMap<String, String>> mCheckObjectCodeList = (ArrayList<HashMap<String, String>>) bundle.get("CheckObjectCodeList");
				//选择的设施设备对象
				ArrayList<HashMap<String, String>> mCheckFacilityCodeList = (ArrayList<HashMap<String, String>>) bundle.get("CheckFacilityCodeList");
				//选择的对比时间
				ArrayList<HashMap<String, String>> mCompareDateList = (ArrayList<HashMap<String, String>>) bundle.get("CompareDateList");
				//选择的病害
				ArrayList<HashMap<String, String>> mDamageTypeList = (ArrayList<HashMap<String, String>>) bundle.get("DamageTypeList");
		
				HashMap<String, String> mDamageAnalysisMap=new HashMap<String, String>();
				mDamageAnalysisMap.put("CheckObjectCodeList", JSONParseUtils.getJSONString(mCheckObjectCodeList));
				mDamageAnalysisMap.put("CheckFacilityCodeList", JSONParseUtils.getJSONString(mCheckFacilityCodeList));	
				mDamageAnalysisMap.put("CheckingMethod", bundle.get("CheckingMethod").toString());	
				mDamageAnalysisMap.put("CheckingType", bundle.get("CheckingType").toString());	
				mDamageAnalysisMap.put("DamageTypeList", JSONParseUtils.getJSONString(mDamageTypeList));	
				mDamageAnalysisMap.put("CompareDateList", JSONParseUtils.getJSONString(mCompareDateList));	
				String jsonStr="";
				try {
					jsonStr = WebServiceUtils.requestM(CheckResultAnalysisAct.this, mDamageAnalysisMap, Constants.METHOD_OF_CLIENT_GetDAMAGEANALYSIS);
					damageAnalysisList = new JSONParseUtils().parseDamageAnalysis(jsonStr);
					if(damageAnalysisList == null){
						return;
					}
					sendMessage(Constants.MSG_0x2009);
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
		case Constants.MSG_0x2009:
			loadData();
			mDialog.dismiss();
			break;
		case Constants.MSG_0x4001:
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


