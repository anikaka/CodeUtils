package com.tongyan.zhengzhou.act.line;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tongyan.zhengzhou.act.MApplication;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.fragment.line.LineBaseInfoFragment;
import com.tongyan.zhengzhou.act.fragment.line.LineDesignFileFragment;
import com.tongyan.zhengzhou.act.fragment.line.LineImplementFileFragment;
import com.tongyan.zhengzhou.act.fragment.line.LineReconnanceFileFragment;
import com.tongyan.zhengzhou.common.afinal.MFinalFragmentActivity;

/**
 * 
 * @Title: LineInfoDetailAct.java 
 * @author Rubert
 * @date 2015-3-11 AM 11:28:35 
 * @version V1.0 
 * @Description: 线路信息详情(郑州-手持端-03地铁线路信息-详细.png)
 */
public class LineInfoDetailAct extends MFinalFragmentActivity implements OnClickListener{
	
	private Button  mBtnBaseInfo,mBtnDesignFile,mBtnReconnanceFile,mBtnImplementFile;
	private LinearLayout mBackBtn;

	private void initView(){
		mBtnBaseInfo=(Button)findViewById(R.id.menu_base_info_btn);
		mBtnDesignFile=(Button)findViewById(R.id.menu_design_files_btn);
		mBtnReconnanceFile=(Button)findViewById(R.id.menu_reconnance_files_btn);
		mBtnImplementFile=(Button)findViewById(R.id.menu_implement_files_btn);
		mBackBtn = (LinearLayout)findViewById(R.id.title_back_btn);
		mBtnBaseInfo.setOnClickListener(this);
		mBtnDesignFile.setOnClickListener(this);
		mBtnReconnanceFile.setOnClickListener(this);
		mBtnImplementFile.setOnClickListener(this);	
		mBackBtn.setOnClickListener(this);	
	}
	
	private  String mNodeCode;
	private  int mNodeLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_detail_info_act);
		MApplication.getInstance().addActivity(this); 
		initView();
		if(getIntent().getExtras()!=null){
			mNodeCode=getIntent().getExtras().getString("nodeCode");
		    mNodeLevel=getIntent().getExtras().getInt("nodeLevel");
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.flLine, LineBaseInfoFragment.getInstance(mNodeCode, mNodeLevel)).commit();
	}	  
	
	
	/**
	 * 设置按钮的默认颜色*/
	private void setBtnDefaultColor(){
		mBtnBaseInfo.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnDesignFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnReconnanceFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		mBtnImplementFile.setBackgroundResource(R.color.detail_menu_normal_bg);
		
	}
	
	/**
	 * 设置按钮的字体颜色*/
	private void setBtnDefaultTextColor(){
		mBtnBaseInfo.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnDesignFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
		mBtnImplementFile.setTextColor(getResources().getColor(R.color.detail_menu_prees_bg));
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.menu_base_info_btn:	
			setBtnDefaultColor();
			setBtnDefaultTextColor();
			mBtnBaseInfo.setTextColor(getResources().getColor(R.color.white));
			mBtnBaseInfo.setBackgroundResource(getResources().getColor(R.color.transparent));
			 getSupportFragmentManager().beginTransaction().replace(R.id.flLine, LineBaseInfoFragment.getInstance(mNodeCode, mNodeLevel)).commit();
			break;
			
		case R.id.menu_design_files_btn:
			setBtnDefaultColor();
			setBtnDefaultTextColor();
			mBtnDesignFile.setTextColor(getResources().getColor(R.color.white));
			mBtnDesignFile.setBackgroundResource(getResources().getColor(R.color.transparent));
			getSupportFragmentManager().beginTransaction().replace(R.id.flLine, LineDesignFileFragment.getInstance(mNodeCode, mNodeLevel)).commit();
			break;
			
		case R.id.menu_reconnance_files_btn:
			setBtnDefaultColor();
			setBtnDefaultTextColor();
			mBtnReconnanceFile.setBackgroundResource(getResources().getColor(R.color.transparent));
			mBtnReconnanceFile.setTextColor(getResources().getColor(R.color.white));
			getSupportFragmentManager().beginTransaction().replace(R.id.flLine, LineReconnanceFileFragment.getInstance(mNodeCode, mNodeLevel)).commit();
			break;
			
		case R.id.menu_implement_files_btn:
			setBtnDefaultColor();
			setBtnDefaultTextColor();
			mBtnImplementFile.setBackgroundResource(getResources().getColor(R.color.transparent));
			mBtnImplementFile.setTextColor((getResources().getColor(R.color.white)));
			getSupportFragmentManager().beginTransaction().replace(R.id.flLine, LineImplementFileFragment.getInstance(mNodeCode, mNodeLevel)).commit();
			break;
			
		case R.id.title_back_btn:
			finish();
			break;
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
	}
	
}
