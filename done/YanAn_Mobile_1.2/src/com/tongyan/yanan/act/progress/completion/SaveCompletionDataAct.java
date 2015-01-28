package com.tongyan.yanan.act.progress.completion;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.MFragmentPagerAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.fragment.progress.ConstructionFragment;
import com.tongyan.yanan.fragment.progress.MachineFragment;
import com.tongyan.yanan.fragment.progress.MemberFragment;
import com.tongyan.yanan.fragment.progress.OthersFragment;
import com.tongyan.yanan.tfinal.FinalFragmentAct;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @className SaveCompletionDataAct
 * @author wanghb
 * @date 2014-7-17 PM 01:56:28
 * @Desc 进度上报数据编辑
 */
public class SaveCompletionDataAct extends FinalFragmentAct {
	
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	
	@ViewInject(id=R.id.completion_menu_construction_btn) Button mConstructionButton;
	@ViewInject(id=R.id.completion_menu_machine_btn) Button mMachineButton;
	@ViewInject(id=R.id.completion_menu_member_btn) Button mMemberButton;
	@ViewInject(id=R.id.completion_menu_others_btn) Button mOthersButton;
	
	@ViewInject(id=R.id.completion_viewpager) ViewPager mViewPage;
	
	@ViewInject(id=R.id.progress_plan_report_save_btn, click="onSaveListener") Button mSaveBtn;
	@ViewInject(id=R.id.progress_plan_report_clear_btn, click="onClearListener") Button mClearBtn;
	
	public static final String OTHERS_EDIT_REMARK = "others_edit_remark";
	public static final String OTHERS_EDIT_QUESTION = "others_edit_question";
	public static final String OTHERS_EDIT_REASON = "others_edit_reason";
	
	private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
	private Context mContext = this;
	
	private String mProjectInfo;
	//private String mLotId; //合同段Id
	private String mDataType = null;
	private String mCompletionDateId = null;
	private String mWeekPlanId = "";
	
	private SharedPreferences mPreferences;
    private String mUserId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_completion_report_edit);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mUserId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		clearEditPrefereces();
		Bundle mBundle=getIntent().getExtras();
		if(mBundle != null){
			mProjectInfo = mBundle.getString("projectInfo");
			mCompletionDateId = mBundle.getString("ID");
			mDataType = mBundle.getString("DataType");
			//mLotId=mBundle.getString("lotId");
			
			mTitleName.setText(mProjectInfo);//设置标题
		}
		initFinishValues();
		mFragmentList.add(ConstructionFragment.newInstance(mContext));
		mFragmentList.add(MachineFragment.newInstance(mContext));
		mFragmentList.add(MemberFragment.newInstance(mContext));
		mFragmentList.add(OthersFragment.newInstance(mContext, mDataType, mCompletionDateId));
		mViewPage.setAdapter(new MFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
		mViewPage.setCurrentItem(0);
		selectMenu(0);
		mViewPage.setOnPageChangeListener(new MyOnPageChangeListener());
		
		mConstructionButton.setOnClickListener(new MyOnClickListener(0));
		mMachineButton.setOnClickListener(new MyOnClickListener(1));
		mMemberButton.setOnClickListener(new MyOnClickListener(2));
		mOthersButton.setOnClickListener(new MyOnClickListener(3));
	}
	
	/**
	 * 初始化已完成的值
	 */
	public void initFinishValues() {
		clearCompletionValues();
		ArrayList<HashMap<String, String>> mlist = new DBService(mContext).getCompletionUpdateInfoList(mCompletionDateId, mUserId);
		if (mlist != null && mlist.size() > 0) {
			new DBService(mContext).updateCacheProgress(mlist);
		}
	}
	
	/**
	 * 清除缓存中的备注、问题、原因
	 */
	public void clearEditPrefereces() {
		 Editor editor = mPreferences.edit();
		 editor.putString(OTHERS_EDIT_REMARK, "");
		 editor.putString(OTHERS_EDIT_QUESTION, "");
		 editor.putString(OTHERS_EDIT_REASON, "");
		 editor.commit();
	}
	
	public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
        	mViewPage.setCurrentItem(index);
        }
    };
    /**
     * 保存按钮监听事件
     * @param v
     */
    public void onSaveListener(View v) {
    	ArrayList<HashMap<String, String>> list = new DBService(mContext).getProgressInfoValueList();
		final  ArrayList<HashMap<String, String>> mCacheList = new  ArrayList<HashMap<String, String>>();
		if (null != list && list.size() > 0) {
			for (HashMap<String, String> map : list) {
				if (map != null) {
					String mInputText = map.get("InputText");
					String mIsEnable = map.get("IsEnable");
					if ((mInputText != null && !"".equals(mInputText)) || ("true".equals(mIsEnable))) {
						HashMap<String, String> map0 = new HashMap<String, String>();
						map0.put("ValueContent", mInputText);
						map0.put("ItemsId", map.get("NewId"));
						//map0.put("lotId", mLotId);
						map0.put("CompletionDateId", mCompletionDateId);
						map0.put("DataType", mDataType);
						map0.put("WeekPlanId", mWeekPlanId);
						map0.put("ItemsType", map.get("ConstructioType"));
						map0.put("UserId", mUserId);
						mCacheList.add(map0);
					}
				}
			}
			if(mCacheList != null && mCacheList.size() > 0) {
				new DBService(mContext).insertCompletionUpdateInfo(mCacheList);
			}
		}
		save();
    }
    
    
    /**
     * 保存相关操作
     */
    public void save() {
    	clearCompletionValues();
		setResult(2016);
		new DBService(mContext).updateCompletionDate(mCompletionDateId, "1", OthersFragment.TYPE_STATE);
		
		String mRemark = mPreferences.getString(SaveCompletionDataAct.OTHERS_EDIT_REMARK, "");
		String mReason = mPreferences.getString(SaveCompletionDataAct.OTHERS_EDIT_REASON, "");
		String mQuestion = mPreferences.getString(SaveCompletionDataAct.OTHERS_EDIT_QUESTION, "");
		
		String mText = String.format("Question='%s',Reason='%s',Remark='%s'", mQuestion,mReason,mRemark);
		new DBService(mContext).updateCompletionDate(mCompletionDateId, mText, OthersFragment.TYPE_ALL);
		finish();
    }
    
    
    /**
     * 清除按钮监听事件
     * @param v
     */
    public void onClearListener(View v) {
    	clearCompletionValues();
    	//刷新 施工项，机械，人员，备注。且备注要清空数据
    	ConstructionFragment.refreshListView(mContext);
    	MachineFragment.refreshListView(mContext);
    	MemberFragment.refreshListView(mContext);
    	OthersFragment.mReasonEdit.setText("");
    	OthersFragment.mQuestionEdit.setText("");
    	OthersFragment.mReamrkEdit.setText("");
    }
    
//    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			dialog();
//		}
//		return false;
//	}
    
//    public void dialog() {
//    	final Dialog mDialog = new Dialog(mContext);
//    	mDialog.setContentView(R.layout.common_tips_dialog);
//    	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//    	mDialog.show();
//    	Button mOkBtn = (Button) mDialog.findViewById(R.id.ok);
//    	Button mCancleBtn = (Button) mDialog.findViewById(R.id.cancle);
//    	TextView mTitleText = (TextView) mDialog.findViewById(R.id.tipsDialogTitle);
//    	TextView mContentText = (TextView) mDialog.findViewById(R.id.tipsDialogContent);
//    	
//    	mTitleText.setText("提示");
//    	mContentText.setText("确定保存吗？");
//    	mOkBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDialog.dismiss();
//				save();
//			}
//		});
//    	
//    	mCancleBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDialog.dismiss();
//				finish();
//			}
//		});
//    	
//    }
    
    
    /**
     * 重置基础数据表
     */
    public void clearCompletionValues() {
		 new DBService(mContext).updateClearProgress();
	}
    
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int arg0) {
			selectMenu(arg0);
		}
	 };
	
	 
	 public void selectMenu(int index) {
		 switch (index) {
		case 0:
			initButtonState();
			mConstructionButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mConstructionButton.setTextColor(Color.WHITE);
			break;
		case 1:
			initButtonState();
			mMachineButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mMachineButton.setTextColor(Color.WHITE);
			break;
		case 2:
			initButtonState();
			mMemberButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mMemberButton.setTextColor(Color.WHITE);
			break;
		case 3:
			initButtonState();
			mOthersButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mOthersButton.setTextColor(Color.WHITE);
			break;
		default:
			break;
		}
	 }
	
	
	 public void initButtonState() {
		 mConstructionButton.setBackgroundColor(Color.WHITE);
		 mMachineButton.setBackgroundColor(Color.WHITE);
		 mMemberButton.setBackgroundColor(Color.WHITE);
		 mOthersButton.setBackgroundColor(Color.WHITE);
		 
		 mConstructionButton.setTextColor(Color.BLACK);
		 mMachineButton.setTextColor(Color.BLACK);
		 mMemberButton.setTextColor(Color.BLACK);
		 mOthersButton.setTextColor(Color.BLACK);
	 }
	
}
