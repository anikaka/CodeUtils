package com.tongyan.zhengzhou.act.line;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.utils.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 隧道信息
 * @author ChenLang
 *
 */
public class LineInfoDetailTunnel  extends Activity{
	
	
	private Context mContext=this;
	private TextView mTxtLineLevel5StartMileage,  //开始里程
								mTxtLineLevel5EndMileage,    //结束里程
								mTxtLineLevel5TrackbedType,  //道床类型
								mTxtLineLevel5MileageAddDirection, //里程增加方向
								mTxtLineLevel5Remark;  //备注
	private TextView mTxtLineLevel5TunnelInfo;
	private HashMap<String, Object> mMap;
	/**
	 *初始化*/
	private void initView(){
		mTxtLineLevel5StartMileage=(TextView)findViewById(R.id.txtLineLevel5StartMileage);
		mTxtLineLevel5EndMileage=(TextView)findViewById(R.id.txtLineLevel5EndMileage);
		mTxtLineLevel5TrackbedType=(TextView)findViewById(R.id.txtLineLevel5TrackbedType);
		mTxtLineLevel5MileageAddDirection=(TextView)findViewById(R.id.txtLineLevel5MileageAddDirection);
		mTxtLineLevel5Remark=(TextView)findViewById(R.id.txtLineLevel5BRemark);
		mTxtLineLevel5TunnelInfo=(TextView)findViewById(R.id.txtLineLevel5TunnelInfo);
		if(mMap!=null){
			HashMap<String, String> map=(HashMap<String, String>)mMap.get("metroTunnel");
			mTxtLineLevel5StartMileage.setText("起始里程:"+map.get("startMileage"));
			mTxtLineLevel5EndMileage.setText("终止里程:"+map.get("endMileage"));
			mTxtLineLevel5TrackbedType.setText("道床类型:"+map.get("trackbedType"));
			mTxtLineLevel5MileageAddDirection.setText("里程增加方向:+"+map.get("mileageAddDirection"));
			mTxtLineLevel5Remark.setText("备注:"+CommonUtils.textIsNull(map.get("remark")));
			mTxtLineLevel5TunnelInfo.setText(Html.fromHtml("<u>"+"隧道附加信息"+"</u>"));
			mTxtLineLevel5TunnelInfo.setOnClickListener(onClickListener);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_baseinfo_tunnel);
		if(getIntent().getExtras()!=null){
			mMap=(HashMap<String, Object>)getIntent().getExtras().get("baseInfo");
		}
		initView();
	}

	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(mContext,LineInfoDetailTunnelList.class);
			intent.putExtra("baseInfo", (ArrayList<HashMap<String,String>>)mMap.get("facilityInfo"));
			startActivity(intent);
		}
	};

}
