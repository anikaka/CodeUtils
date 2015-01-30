package com.tongyan.activity.oa;

import java.lang.ref.SoftReference;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.entities._ContactsData;
import com.tongyan.utils.MDialog;
import com.tongyan.widget.view.BaseLine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


/**
 * 
 * @ClassName P17_ContactsDetailAct 
 * @author wanghb
 * @date 2013-7-19 pm 05:07:27
 * @desc 移动OA-通讯录-详情
 */
public class OaContactsDetailAct extends Activity {
	
	private LinearLayout container;
	private Button homeBtn;
	
	private SoftReference<PopupWindow> soft = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_contacts_detail);
		((MyApplication)getApplication()).addActivity(this);
		container = (LinearLayout)findViewById(R.id.p17_contacts_details_linearlayout);
		homeBtn = (Button)findViewById(R.id.p17_contacts_home_btn);
		
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
	}
	
	private void businessM(){
		Intent itent = getIntent();
		_ContactsData values = (_ContactsData) itent.getExtras().get("_ContactsData");
		//姓名
		View view1 = getCommView(R.string.name, values.getEmpName());
		container.addView(view1);
		container.addView(getCommBaseLine());
		//手机
		String[] phoneNums = values.getEmpContact().split(";");
		if(phoneNums != null && phoneNums.length > 0) {
			for(int i = 0; i < phoneNums.length; i ++) {
				//setClickLisener
				String[] subAry = phoneNums[i].split(":");
				if(subAry != null && subAry.length > 0) {
					TextView titleView = new TextView(this);
					titleView.setText(subAry[0]);
					titleView.setTextColor(Color.BLACK);
					titleView.setTextSize(18);
					titleView.setPadding(5, 3, 0, 0);
					TextView contentView = new TextView(this);
					contentView.setText(subAry[1]);
					contentView.setPadding(5, 2, 0, 3);
					container.addView(titleView);
					container.addView(contentView);
					container.addView(getCommBaseLine());
					contentView.setTag(subAry[1]);
					if(subAry[0] != null && ("手机号".equals(subAry[0]))) {
						contentView.setOnClickListener(mCallPhoneOnClickListenr);
					} else if(subAry[0] != null && "电话".equals(subAry[0])) {
						contentView.setOnClickListener(mCallMobileOnClickListenr);
					} else {
						contentView.setOnClickListener(mCopyOnClickListenr);
					}
				}
			}
		}
		//部门
		View view3 = getCommView(R.string.department, values.getDptName());
		container.addView(view3);
		container.addView(getCommBaseLine());
		
		//邮箱
		View view4 = getCommView(R.string.email, "无");
		container.addView(view4);
	}
	
		OnClickListener mCallPhoneOnClickListenr = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v != null && v.getTag() != null) {
					phoneType((String)v.getTag());
				}
			}
		};
		
		OnClickListener mCallMobileOnClickListenr = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v != null && v.getTag() != null) {
					mobileType((String)v.getTag());
				}
			}
		};
		
		
		OnClickListener mCopyOnClickListenr = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v != null && v.getTag() != null) {
					ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText((String)v.getTag()); // 复制
					Toast.makeText(OaContactsDetailAct.this, "已复制-" +(String)v.getTag(), Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		MDialog mCallPhoneDialog = null;
		private void phoneType (final String mobile) {
			mCallPhoneDialog = new MDialog(this, R.style.dialog);
			mCallPhoneDialog.createDialog(R.layout.oa_contacts_pop, 0.95, 0.45, getWindowManager());
			mCallPhoneDialog.setCanceledOnTouchOutside(false);
			
			Button callBtn = (Button)mCallPhoneDialog.findViewById(R.id.p18_contacts_pop_callPhone);
			Button msgBtn = (Button)mCallPhoneDialog.findViewById(R.id.p18_contacts_pop_sendMsg);
			Button copyBtn = (Button)mCallPhoneDialog.findViewById(R.id.p18_contacts_pop_copyWords);
			
			callBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + mobile));
					startActivity(phoneIntent);
					closeCallDialog();
				}
			});
			msgBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.putExtra("address", mobile);
					intent.setType("vnd.android-dir/mms-sms"); 
					startActivity(intent);
					closeCallDialog();
				}
			});
			copyBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText(mobile); // 复制
					Toast.makeText(OaContactsDetailAct.this, "已复制-" + mobile, Toast.LENGTH_SHORT).show();
					closeCallDialog();
				}
			});
		}
		
		
		MDialog mCallMobileDialog = null;
		private void mobileType (final String mobile) {
			mCallMobileDialog = new MDialog(this, R.style.dialog);
			mCallMobileDialog.createDialog(R.layout.oa_contacts_pop2, 0.95, 0.45, getWindowManager());
			mCallMobileDialog.setCanceledOnTouchOutside(false);
			
			Button callBtn = (Button)mCallMobileDialog.findViewById(R.id.p18_contacts_pop_callPhone);
			Button copyBtn = (Button)mCallMobileDialog.findViewById(R.id.p18_contacts_pop_copyWords);
			
			callBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + mobile));
					startActivity(phoneIntent);
					closeCallDialog();
				}
			});
			copyBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText(mobile); // 复制
					Toast.makeText(OaContactsDetailAct.this, "已复制-" + mobile, Toast.LENGTH_SHORT).show();
					closeCallDialog();
				}
			});
		}
		
		public void closeCallDialog() {
			if(mCallPhoneDialog != null) {
				mCallPhoneDialog.dismiss();
			}
			if(mCallMobileDialog != null) {
				mCallMobileDialog.dismiss();
			}
		}
		
	public float getDensity() {
		return this.getResources().getDisplayMetrics().density;
	}
	
	public View getCommView(int title,String content) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(LinearLayout.VERTICAL);
		
		TextView titleView = new TextView(this);
		titleView.setText(title);
		titleView.setTextColor(Color.BLACK);
		titleView.setTextSize(18);
		titleView.setPadding(5, 3, 0, 0);
		TextView contentView = new TextView(this);
		contentView.setText(content);
		contentView.setPadding(5, 2, 0, 3);
		lay.addView(titleView);
		lay.addView(contentView);
		return lay;
	}
	
	public View getCommBaseLine() {
		BaseLine line = new BaseLine(this);
		line.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,1));
		return line;
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaContactsDetailAct.this,MainAct.class);
			if(soft != null && soft.get() != null) {
				soft.get().dismiss();
				soft.clear();
				soft = null;
			}
			startActivity(intent);
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(soft != null && soft.get() != null) {
				soft.get().dismiss();
				soft.clear();
				soft = null;
				return true;
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
