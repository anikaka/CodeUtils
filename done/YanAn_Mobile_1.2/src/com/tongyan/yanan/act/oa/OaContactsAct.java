package com.tongyan.yanan.act.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.MFragmentPagerAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.fragment.oa.OaContactsAllFragment;
import com.tongyan.yanan.fragment.oa.OaContactsDepartmentFragment;
import com.tongyan.yanan.tfinal.FinalFragmentAct;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/***
 * 
 * @className OaContacts
 * @author wanghb
 * @date 2014-7-14 AM 08:10:23
 * @Desc 通讯录
 */
public class OaContactsAct extends FinalFragmentAct {
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	
	@ViewInject(id=R.id.oa_contacts_menu_department_btn) Button mDepartmentButton;
	@ViewInject(id=R.id.oa_contacts_menu_all_btn) Button mAllButton;
	
	@ViewInject(id=R.id.oa_contacts_viewpager) ViewPager mViewPage;
	
	private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
	private Context mContext = this;
	private Dialog mDialog = null;
	private SharedPreferences mPreferences;
	//private String mUserid = null;
	private String mDptId = null;
	private ArrayList<HashMap<String, String>> mDataList = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_contacts);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		//mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		mTitleName.setText(getResources().getString(R.string.contacts));//设置标题
		Bundle mBundle = getIntent().getExtras();
		if(mBundle != null) {
			mDptId = mBundle.getString("NewId");
			ArrayList<HashMap<String, String>> list = new DBService(mContext).getContactsList(mDptId, null);
			if(list == null || list.size() == 0) {
				getContacts(mDptId);	
			} else {
				mDataList.addAll(list);
				refreshListView();
			}
		}
	}
	
	public void refreshListView() {
		mFragmentList.add(OaContactsDepartmentFragment.newInstance(mContext, mDptId));
		mFragmentList.add(OaContactsAllFragment.newInstance(mContext, mDptId));
		mViewPage.setAdapter(new MFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
		mViewPage.setCurrentItem(0);
		selectMenu(0);
		mViewPage.setOnPageChangeListener(new MyOnPageChangeListener());
		mDepartmentButton.setOnClickListener(new MyOnClickListener(0));
		mAllButton.setOnClickListener(new MyOnClickListener(1));
	}
	
	
	public void getContacts(final String mDptNatureId) {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String>  mParams = new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETCONTACTS);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mPreferences.getString(Constants.PREFERENCES_INFO_USERID, ""));
				 mParams.put("DptNatureId", mDptNatureId);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					//{"s":"ok","v":[
					//{"UserName":"生元管理员","DeptName":"陕西生元监理有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"信达管理员","DeptName":"陕西信达建设工程监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"兴业人员1","DeptName":"西安兴业建设监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"建兴管理员","DeptName":"延安建兴工程监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"鼎正管理员","DeptName":"陕西鼎正项目管理有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"华茂人员1","DeptName":"陕西华茂建设监理咨询有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"信达人员1","DeptName":"陕西信达建设工程监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"兴业管理员","DeptName":"西安兴业建设监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"华茂管理员","DeptName":"陕西华茂建设监理咨询有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"鼎正人员1","DeptName":"陕西鼎正项目管理有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"测试三","DeptName":"延安建兴工程监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":true},
					//{"UserName":"铁城管理员","DeptName":"北京铁城建设监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"建兴人员1","DeptName":"延安建兴工程监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"铁城管理员","DeptName":"北京铁城建设监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"铁城管理员","DeptName":"北京铁城建设监理有限责任公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":false},
					//{"UserName":"张三","DeptName":"陕西鼎正项目管理有限公司","UserPhone":null,"UserEmail":null,"UserQQ":null,"UserSex":true}]}
					
					if(!"".equals(mStrJson) && mStrJson!=null){		
						ArrayList<HashMap<String, String>> list = JsonTools.getContacts(mStrJson);
						if(list != null) {
							if(mDataList != null) {
								mDataList.clear();
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delContacts(mDptId)) {
										new DBService(mContext).insertContacts(list, mDptId);
									}
									mDataList.addAll(list);
								} 
							}
							sendFMessage(Constants.SUCCESS);
						} else {
							sendFMessage(Constants.ERROR);
						}
					}
				 }catch(Exception e){
					 e.printStackTrace();
					 sendFMessage(Constants.CONNECTION_TIMEOUT);
				 }
			}
		}).start();
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
    
    
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			selectMenu(arg0);
		}
	 };
	
	 
	 public void selectMenu(int index) {
		if (0 == index) {
			mDepartmentButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mDepartmentButton.setTextColor(Color.WHITE);
			mAllButton.setBackgroundColor(Color.WHITE);
			mAllButton.setTextColor(Color.BLACK);
			return;
		}
		if (1 == index) {
			mDepartmentButton.setBackgroundColor(Color.WHITE);
			mDepartmentButton.setTextColor(Color.BLACK);
			mAllButton.setBackgroundColor(getResources().getColor(R.color.common_color));
			mAllButton.setTextColor(Color.WHITE);
			return;
		}
	 }
	 
	 
	 @Override
	protected void handleOtherMessage(int flag) {
		 switch(flag){
			case Constants.SUCCESS:
					if(mDialog!=null){				
						mDialog.dismiss();
					}
					refreshListView();
				break;
			case Constants.ERROR:
				if(mDialog!=null){				
					mDialog.dismiss();
				}
				Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
				break;
			case Constants.CONNECTION_TIMEOUT:
				if(mDialog!=null){				
					mDialog.dismiss();
				}
				Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
				break;
			}
	}
	 
	 
	
}
