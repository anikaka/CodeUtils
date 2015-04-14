package com.tongyan.zhengzhou.act.patro;

import android.os.Bundle;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.common.afinal.MFinalActivity;
/**
 * 
 * @Title: PatroInfoDetailAct.java 
 * @author Rubert
 * @date 2015-3-24 PM 04:13:47 
 * @version V1.0 
 * @Description: 巡查详细 act
 */
public class PatroProtectProjectDetailAct extends MFinalActivity {
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_detail_info_act);
		MApplication.getInstance().addActivity(this); 
		
	}
	
}
