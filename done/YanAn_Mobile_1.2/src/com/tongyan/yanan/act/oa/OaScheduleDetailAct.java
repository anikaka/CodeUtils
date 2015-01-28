package com.tongyan.yanan.act.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: OaScheduleAddAct.java 
 * @author Rubert
 * @date 2014-8-4 PM 02:10:20 
 * @version V1.0 
 * @Description: 日程详情
 *  1表示系统提示，2表示邮件提示，3表示短信提示
 */
public class OaScheduleDetailAct extends FinalActivity {
	@ViewInject(id=R.id.title_common_content) TextView mTitleName;
	@ViewInject(id=R.id.schedule_add_title) EditText mScheduleTitle;
	@ViewInject(id=R.id.checkbox_is_allday) CheckBox mIsAllDayBox;
	@ViewInject(id=R.id.schedule_add_time_from) TextView mStartTime;
	@ViewInject(id=R.id.schedule_add_time_end) TextView mEndTime;
	@ViewInject(id=R.id.schedule_notice_time) TextView mNoticeTime;
	@ViewInject(id=R.id.schedule_add_content) EditText mScheduleContent;
	
	@ViewInject(id=R.id.checkbox_notice_system) CheckBox mNSystemBox;
	@ViewInject(id=R.id.checkbox_notice_email) CheckBox mNEmialBox;
	@ViewInject(id=R.id.checkbox_notice_message) CheckBox mNMessageBox;
	
	
	@ViewInject(id=R.id.schedule_notice_members_container) LinearLayout mNMembers;
	
	private Context mContext = this;
	
	private ArrayList<HashMap<String, String>> mSelectedList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> mCacheUserIdList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_calendar_schedule_detail);
		mTitleName.setText(getResources().getString(R.string.oa_add_schedule));//setting title
		Bundle mBundle = getIntent().getExtras();
		if(mBundle != null && "OaScheduleListAct".equals(mBundle.getString("IntentType"))) {
			mTitleName.setText(getResources().getString(R.string.oa_modify_schedule));
			HashMap<String, Object> mIntentMap = (HashMap<String, Object>)mBundle.get("IntentMap");
			if(mIntentMap != null) {
				mScheduleTitle.setText((String)mIntentMap.get("Title"));
				mStartTime.setText((String)mIntentMap.get("WorkTime"));
				mEndTime.setText((String)mIntentMap.get("WarnTime"));
				mNoticeTime.setText((String)mIntentMap.get("NoticeTime"));
				mScheduleContent.setText((String)mIntentMap.get("Content"));
				String mIsDay = (String)mIntentMap.get("IsDay");
				String type = (String)mIntentMap.get("Type");
				if(mIsDay != null && mIsDay.equals("true")) {
					mIsAllDayBox.setChecked(true);
				}
				if(type != null && type.length() > 0) {
					for(int i = 0; i < type.length(); i ++) {
						char s = type.charAt(i);
						if("1".equals(String.valueOf(s))) {
							mNSystemBox.setChecked(true);
						}
						if("2".equals(String.valueOf(s))) {
							mNEmialBox.setChecked(true);
						}
						if("3".equals(String.valueOf(s))) {
							mNMessageBox.setChecked(true);
						}
					}
				}
				ArrayList<HashMap<String, String>> staffList = (ArrayList<HashMap<String, String>>)mIntentMap.get("NoticeStaff");
				if(staffList != null) {
					int size = staffList.size();
					if(size > 0) {
						for(HashMap<String, String> m : staffList) {
							if(m != null) {
								mCacheUserIdList.add(m.get("UserId"));
							}
						}
						mSelectedList.addAll(staffList);
						initUserLayout();
					}
				}
				
			}
		}
	}
	
	public void initUserLayout() {
		if(mSelectedList != null) {
			int size = mSelectedList.size();
			if(size > 0) {
				int rows = size / 3;//每行3列
				if(size % 3 > 0) {
					rows ++;
				}
				for(int i = 1; i <= rows; i ++) {
					LinearLayout mLinearLayout = drawLinearLayout();
					mLinearLayout.addView(addButton(mSelectedList.get(i * 3 - 3)));
					if(i * 3 - 2 < size) {
						mLinearLayout.addView(addButton(mSelectedList.get(i * 3 - 2)));
					}
					if((i * 3 - 1) < size) {
						mLinearLayout.addView(addButton(mSelectedList.get(i * 3 - 1)));
					}
					mNMembers.addView(mLinearLayout);
				}
			}
		}
	}
	
	public LinearLayout drawLinearLayout() {
		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return mLinearLayout;
	}
	
	public Button addButton(final HashMap<String, String> map) {
		Button mButton = new Button(mContext);
		if(map != null) {
			mButton.setText(map.get("UserName"));
			mButton.setTag(map);
		}
		return mButton;
	}
	
	
	public void refreshLayout() {
		mNMembers.removeAllViews();
		initUserLayout();
	}
	
}
