package com.tongyan.yanan.act.oa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDatePickerDialog;
import com.tongyan.yanan.common.widgets.view.MDateTimePickerDialog;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: OaScheduleAddAct.java 
 * @author Rubert
 * @date 2014-8-4 PM 02:10:20 
 * @version V1.0 
 * @Description: 日程添加/修改
 *  1表示系统提示，2表示邮件提示，3表示短信提示
 */
public class OaScheduleAddAct extends FinalActivity {
	@ViewInject(id=R.id.title_common_content) TextView mTitleName;
	@ViewInject(id=R.id.schedule_add_title) EditText mScheduleTitle;
	@ViewInject(id=R.id.checkbox_is_allday) CheckBox mIsAllDayBox;
	@ViewInject(id=R.id.schedule_add_time_from, click="startTimeListener") TextView mStartTime;
	@ViewInject(id=R.id.schedule_add_time_end, click="endTimeListener") TextView mEndTime;
	@ViewInject(id=R.id.schedule_notice_time, click="noticeTimeListener") TextView mNoticeTime;
	@ViewInject(id=R.id.schedule_add_content) EditText mScheduleContent;
	
	@ViewInject(id=R.id.checkbox_notice_system) CheckBox mNSystemBox;
	@ViewInject(id=R.id.checkbox_notice_email) CheckBox mNEmialBox;
	@ViewInject(id=R.id.checkbox_notice_message) CheckBox mNMessageBox;
	
	@ViewInject(id=R.id.schedule_select_contacts_btn, click="selectMemberListener") Button mSelectMembersBtn;
	
	@ViewInject(id=R.id.schedule_notice_members_container) LinearLayout mNMembers;
	
	@ViewInject(id=R.id.ok, click="mCommitListener") Button mSureBtn;
	@ViewInject(id=R.id.cancle, click="mCancelListener") Button mCancelBtn;
	
	private Context mContext = this;
	private int mNoticeType = 0; 
	private SharedPreferences mPreferences;
	private String mUserId;
	
	private String mTitle = null;
	private String mStartTimeText = null;
	private String mEndTimeText = null;
	private String mNoticeText = null;
	private String mScheduleContentText = null;
	private StringBuilder mNoticeTypeBuf = new StringBuilder();
	private Dialog mDialog = null;
	
	private ArrayList<HashMap<String, String>> mSelectedList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> mCacheUserIdList = new ArrayList<String>();
	
	private String mNewId = null;
	private int IntentType = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_calendar_schedule_add_update);
		mTitleName.setText(getResources().getString(R.string.oa_add_schedule));//setting title
		mNSystemBox.setOnCheckedChangeListener(mSysCheckedListener);
		mNEmialBox.setOnCheckedChangeListener(mSysCheckedListener);
		mNMessageBox.setOnCheckedChangeListener(mSysCheckedListener);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mUserId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		Bundle mBundle = getIntent().getExtras();
		if(mBundle != null && "OaScheduleListAct".equals(mBundle.getString("IntentType"))) {
			mTitleName.setText(getResources().getString(R.string.oa_modify_schedule));
			mSureBtn.setText("保存");
			HashMap<String, Object> mIntentMap = (HashMap<String, Object>)mBundle.get("IntentMap");
			if(mIntentMap != null) {
				IntentType = 2;
				mNewId = (String)mIntentMap.get("NewId");//
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
	
	/**
	 * 
	 */
	public void commit() {
		if(verification()) {
			mDialog=new Dialog(mContext,R.style.dialog);
			mDialog.setContentView(R.layout.common_normal_progressbar);
			mDialog.show();
			new Thread(new Runnable(){
				@Override
				public void run() {
					HashMap<String, String>  mParameters=new HashMap<String, String>();
					if(IntentType == 2) {
						mParameters.put("method", Constants.METHOD_OF_UPDATEWORKSCHEDULE);
					} else {
						mParameters.put("method", Constants.METHOD_OF_ADDWORKSCHEDULE);
					}
					//mParameters.put("method", Constants.METHOD_OF_ADDWORKSCHEDULE);
					mParameters.put("key",Constants.PUBLIC_KEY);
					mParameters.put("userId", mUserId);
					StringBuilder builder = new StringBuilder();
					builder.append("{\"Title\":\"");
					builder.append(JsonTools.getURLEncoderString(mTitle));
					if(IntentType == 2) {
						builder.append("\",\"NewId\":\"");
						builder.append(mNewId);
					}
					builder.append("\",\"Content\":\"");
					builder.append(JsonTools.getURLEncoderString(mScheduleContentText));
					builder.append("\",\"WorkTime\":\"");
					builder.append(mStartTimeText);
					builder.append("\",\"WarnTime\":\"");
					builder.append(mEndTimeText);
					builder.append("\",\"IsDay\":\"");
					builder.append(String.valueOf(mIsAllDayBox.isChecked()));
					builder.append("\",\"NoticeTime\":\"");
					builder.append(mNoticeText);
					builder.append("\",\"Type\":\"");
					builder.append(mNoticeTypeBuf.toString());
					builder.append("\",\"RelatedStaffAll\":");
					builder.append("[");
					builder.append(appendUserId());
					builder.append("]}");
					String data = builder.toString().replaceAll(" ", "%20");
					//data = data.replaceAll("?", "%3F");
					/*data = data.replaceAll("+", "%2B");
					data = data.replaceAll("/", "%2F");
					data = data.replaceAll("#", "%23");
					data = data.replaceAll("=", "%3D");*/
					
					mParameters.put("data", data);
					
					String mResponseBody="";
					try{				
						mResponseBody = HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParameters, mContext));
						if(JsonTools.getCommonResult(mResponseBody)) {
							sendFMessage(Constants.SUCCESS);
						} else {
							sendFMessage(Constants.ERROR);
						}
					}catch(Exception e){
						e.printStackTrace();
						sendFMessage(Constants.CONNECTION_TIMEOUT);
					}
				}
			}).start();
		}
	}
	
	
	public String appendUserId() {
		StringBuffer buffer = new StringBuffer();
		if(mSelectedList != null && mSelectedList.size() > 0) {
			for(int i = 0, len = mSelectedList.size(); i < len; i ++ ) {
				HashMap<String, String> map = mSelectedList.get(i);
				if(map != null) {
					String name = map.get("UserId");
					if(i == len - 1) {
						buffer.append("{\"UserId\":\"");
						buffer.append(name);
						buffer.append("\"}");
					} else {
						buffer.append("{\"UserId\":\"");
						buffer.append(name);
						buffer.append("\"},");
					}
				}
			}
		}
		return buffer.toString();
	}
	
	
	
	public boolean verification() {
		mNoticeTypeBuf.delete(0, mNoticeTypeBuf.length());
		mTitle = mScheduleTitle.getText().toString();
		if("".equals(mTitle.trim())) {
			Toast.makeText(mContext, "请填写日程标题", Toast.LENGTH_SHORT).show();
			return false;
		}
		mStartTimeText = mStartTime.getText().toString();
		if("".equals(mStartTimeText.trim())) {
			Toast.makeText(mContext, "请选择日程开始时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		mEndTimeText = mEndTime.getText().toString();
		if("".equals(mEndTimeText.trim())) {
			Toast.makeText(mContext, "请选择日程结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		mNoticeText = mNoticeTime.getText().toString();
		if("".equals(mNoticeText.trim())) {
			Toast.makeText(mContext, "请选择日程通知时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		Date mStartDate = null;
		Date mNoticeDate = null;
		try {
			mNoticeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mNoticeText);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(mIsAllDayBox.isChecked()) {
			try {
				mStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mStartTimeText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(mStartTimeText);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(mStartDate != null && mNoticeDate != null) {
			if(mNoticeDate.after(mStartDate)) {
				Toast.makeText(mContext, "通知时间必须在开始时间之前", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if("".equals(mNoticeText.trim())) {//TODO
			Toast.makeText(mContext, "请选择日程通知时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		mScheduleContentText = mScheduleContent.getText().toString();
		if("".equals(mScheduleContentText.trim())) {
			Toast.makeText(mContext, "请填写日程内容", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mNoticeType <= 0) {
			Toast.makeText(mContext, "请勾选通知类型", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			if(mNSystemBox.isChecked()) {
				mNoticeTypeBuf.append("1");
			}
			if(mNEmialBox.isChecked()) {
				mNoticeTypeBuf.append("2");
			}
			if(mNMessageBox.isChecked()) {
				mNoticeTypeBuf.append("3");
			}
		}
		//人员验证
		if(mSelectedList == null || mSelectedList.size() == 0) {
			Toast.makeText(mContext, "请选择通知对象", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 添加日程
	 * @param v
	 */
	public void mCommitListener(View v) {
		commit();
	}
	/**
	 * 取消添加日程
	 * @param v
	 */
	public void mCancelListener(View v) {
		finish();
	}
	
	/**
	 * 开始时间监听
	 * @param v
	 */
	public void startTimeListener(View v) {
		if(mIsAllDayBox.isChecked()) {
			new MDateTimePickerDialog(mContext, new MDateTimePickerDialog.OnDateTimeSetListener() {
				@Override
				public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int second) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
					String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
					String mSecond = second < 10 ? "0" + second : String.valueOf(second);
					mStartTime.setText(year + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":" + mSecond);
					return true;
				}
			}).show();
		} else {
			new MDatePickerDialog(mContext, new MDatePickerDialog.OnDateTimeSetListener(){
				@Override
				public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mDateInfo = year + "-" + mMonth + "-" + mDay;
					mStartTime.setText(mDateInfo);
					return true;
				}
			}).show();
		}
	}
	
	/**
	 * 结束时间监听
	 * @param v
	 */
	public void endTimeListener(View v) {
		if(mIsAllDayBox.isChecked()) {
			new MDateTimePickerDialog(mContext, new MDateTimePickerDialog.OnDateTimeSetListener() {
				@Override
				public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int second) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
					String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
					String mSecond = second < 10 ? "0" + second : String.valueOf(second);
					mEndTime.setText(year + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":" + mSecond);
					return true;
				}
			}).show();
		} else {
			new MDatePickerDialog(mContext, new MDatePickerDialog.OnDateTimeSetListener(){
				@Override
				public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					String mDateInfo = year + "-" + mMonth + "-" + mDay;
					mEndTime.setText(mDateInfo);
					return true;
				}
			}).show();
		}
	}
	
	/**
	 * 通知时间监听
	 * @param v
	 */
	public void noticeTimeListener(View v) {
		new MDateTimePickerDialog(mContext, new MDateTimePickerDialog.OnDateTimeSetListener() {
			@Override
			public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int second) {
				String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
				String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
				String mHour = hour < 10 ? "0" + hour : String.valueOf(hour);
				String mMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
				String mSecond = second < 10 ? "0" + second : String.valueOf(second);
				mNoticeTime.setText(year + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":" + mSecond);
				return true;
			}
		}).show();
	}
	
	/**
	 * 选择人员监听
	 * @param v
	 */
	public void selectMemberListener(View v) {
		Intent intent = new Intent(mContext, OaContactsMenuAct.class);
		intent.putExtra("IntentType", "OaScheduleAddAct");
		startActivityForResult(intent, 2345);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 2345) {
			if(resultCode == 9897) {
				ArrayList<HashMap<String, String>> list = new DBService(mContext).getContactsSelectedList();
				/*if(list != null) {
					mSelectedList.addAll(list);
				}*/
				//writeUserName();
				if(list != null && list.size() > 0) {
					for(HashMap<String, String> m : list) {
						if(m != null) {
							if(!mCacheUserIdList.contains(m.get("UserId"))) {
								mCacheUserIdList.add(m.get("UserId"));
								mSelectedList.add(m);
							}
						}
					}
					//mCacheUserList.addAll(list);
					refreshLayout();
				}
				
			}
		}
		new DBService(mContext).updateContactsAllSelect("0");
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*public void writeUserName() {
		if(mSelectedList != null && mSelectedList.size() > 0) {
			mNMembers.setText("");
			StringBuffer buffer = new StringBuffer();
			for(int i = 0, len = mSelectedList.size(); i < len; i ++ ) {
				HashMap<String, String> map = mSelectedList.get(i);
				if(map != null) {
					String name = map.get("UserName");
					if(i == len - 1) {
						buffer.append(name);
					} else {
						buffer.append(name);
						buffer.append(",");
					}
				}
			}
			mNMembers.setText(buffer.toString());
		}
	}*/
	
	
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
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog=new Dialog(mContext, R.style.dialog);
				dialog.setContentView(R.layout.common_tips_dialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				
				TextView mTitleView = (TextView)dialog.findViewById(R.id.tipsDialogTitle);
				TextView mTipsView = (TextView)dialog.findViewById(R.id.tipsDialogContent);
				Button mOkBtn = (Button)dialog.findViewById(R.id.ok);
				Button mCancleBtn = (Button)dialog.findViewById(R.id.cancle);
				mTitleView.setText("提示");
				mTipsView.setText("确定要删除-"  + map.get("UserName") + "?");
				mOkBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						mSelectedList.remove(map);
						refreshLayout();
					}
				});
				
				mCancleBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				
			}
		});
		return mButton;
	}
	
	
	public void refreshLayout() {
		mNMembers.removeAllViews();
		initUserLayout();
	}
	
	OnCheckedChangeListener mSysCheckedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked) {
				mNoticeType++;
			} else {
				mNoticeType--;
			}
		}
	};
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constants.SUCCESS:
			closeDialog();
			if(IntentType == 2) {
				Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "新增成功", Toast.LENGTH_SHORT).show();
			}
			setResult(8888);
			finish();
			break;
		case Constants.ERROR:
			closeDialog();
	 		Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
	 		break;
		case Constants.CONNECTION_TIMEOUT:
			closeDialog();
	 		Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
	 		break;
		}
	};
	
	
}
