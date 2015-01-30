package com.tongyan.activity.measure.command;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.tongyan.activity.AbstructFragmentActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.fragment.CommandMenuFragment;
import com.tongyan.widget.slidingmenu.SlidingMenu;
import com.tongyan.widget.view.MSpinner;

/**
 * 监理指令菜单
 * @author ChenLang
 */

public class CommandMenuAct  extends AbstructFragmentActivity implements OnClickListener{

	private Button mBtnCommandMenu;
	private Context mContext=this;
	private MyApplication mApplication;
	private  _User mUser;
	private SlidingMenu mSlidingMenu;
	private RelativeLayout mWaitingApproveBtn, mNoApproveBtn, mReportedBtn, mApprovedBtn, mAllBtn;
	private String mSectionId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_command_menu);
		initWidget();
		mApplication=(MyApplication)getApplication();
		mApplication.addActivity(this);
		mUser=mApplication.localUser;
		initSlidingMenu();
	}
	
	public void initWidget(){
		mBtnCommandMenu=(Button)findViewById(R.id.btnCommandMenu);
	}
	
	// 设置主界面视图
	//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MeasureMenuFragment()).commit();
	private void initSlidingMenu() {
	// 设置滑动菜单的属性值
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//还不知道
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置目录右侧的阴影区间宽度
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//设置内容占屏幕宽度的多少dp
		mSlidingMenu.setFadeDegree(1f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 设置滑动菜单的视图界面
		mSlidingMenu.setMenu(R.layout.measure_menu_list_layout);
		
		MSpinner btnFirst = (MSpinner)mSlidingMenu.findViewById(R.id.measure_section_selector);
		mWaitingApproveBtn = (RelativeLayout)mSlidingMenu.findViewById(R.id.measure_waiting_approve);
		mNoApproveBtn = (RelativeLayout)mSlidingMenu.findViewById(R.id.measure_no_approve);
		mReportedBtn = (RelativeLayout)mSlidingMenu.findViewById(R.id.measure_reported);
		mApprovedBtn = (RelativeLayout)mSlidingMenu.findViewById(R.id.measure_approved);
		mAllBtn = (RelativeLayout)mSlidingMenu.findViewById(R.id.measure_all);
		mWaitingApproveBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
		
		mWaitingApproveBtn.setOnClickListener(this);
		mNoApproveBtn.setOnClickListener(this);
		mReportedBtn.setOnClickListener(this);
		mApprovedBtn.setOnClickListener(this);
		mAllBtn.setOnClickListener(this);
		mBtnCommandMenu.setOnClickListener(this); 
		
	final ArrayList<HashMap<String, String>> mSectionList = new DBService(mContext).getSectionListByMeasure();
	if(mSectionList != null && mSectionList.size() > 0) {
		ArrayAdapter<HashMap<String, String>> mSectionAdapter = new ArrayAdapter<HashMap<String, String>>(mContext,R.layout.common_spinner, mSectionList){
			 @Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				 if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
					String mTextContent= mSectionList.get(position).get("sContent");
					if(mTextContent == null || "".equals(mTextContent) ) {
						mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
					} 
					textView.setText(mTextContent);
					return convertView;
			}
			 
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
				} 
				TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
				String mTextContent= mSectionList.get(position).get("sContent");
				if(mTextContent == null || "".equals(mTextContent) ) {
					mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
					textView.setTextColor(Color.GRAY);
				} 
				textView.setText(mTextContent);
				return convertView;
			}
		 };
		 
		 btnFirst.setAdapter(mSectionAdapter);
		 btnFirst.setOnItemSelectedListener( new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> map = (HashMap<String, String>)parent.getAdapter().getItem(position);
				if(map != null) {
					mSectionId  = map.get("sid");		
						showRightFragment("1");
						setBtnDefaultBackground();
						mWaitingApproveBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		 });
	}
}	


/**设置按钮的默认颜色 */
public void  setBtnDefaultBackground(){
	mWaitingApproveBtn.setBackgroundResource(R.color.transparent);
	mNoApproveBtn.setBackgroundResource(R.color.transparent);
	mReportedBtn.setBackgroundResource(R.color.transparent);
	mApprovedBtn.setBackgroundResource(R.color.transparent);
	mWaitingApproveBtn.setBackgroundResource(R.color.transparent);
	mAllBtn.setBackgroundResource(R.color.transparent);
}

	@Override
	public void onClick(View v) {
		if(v.equals(mBtnCommandMenu)) {
			if(mSlidingMenu.isMenuShowing()) {
				onBackPressed();
			} else {
				mSlidingMenu.showMenu();
			}
			return;
		}
		
		if(v.equals(mWaitingApproveBtn)) {
			setBtnDefaultBackground();
			mWaitingApproveBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
			showRightFragment("1"); //1待审批
			return;
		}
		if(v.equals(mNoApproveBtn)) {
			setBtnDefaultBackground();
			mNoApproveBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
			showRightFragment("0");//0未申报
			return;
		}
		if(v.equals(mReportedBtn)) {
			setBtnDefaultBackground();
			mReportedBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
			showRightFragment("2"); //2 已申报
			return;
		}
		if(v.equals(mApprovedBtn)) {
			setBtnDefaultBackground();
			mApprovedBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
			showRightFragment("3");//已审批
			return;
		}
		if(v.equals(mAllBtn)) {
			setBtnDefaultBackground();
			mAllBtn.setBackgroundResource(R.color.slidingmenu_item_selected_color);
			showRightFragment("4");//4全部
			return;
		}
	}
	
	public void showRightFragment(String mType) {
		getSupportFragmentManager().beginTransaction().replace(R.id.flCommandMenu, CommandMenuFragment.newInstance(mContext, mSectionId, mType, mUser)).commit();
	}
	
}
