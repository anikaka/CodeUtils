package com.tongyan.yanan.act.oa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.WebServiceUtils;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @className OaMessageAct
 * @author wanghb
 * @date 2014-7-14 AM 08:19:16
 * @Desc 短信通知
 */
public class OaMessageAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	@ViewInject(id=R.id.message_content) EditText mMessageContent;
	@ViewInject(id=R.id.schedule_select_contacts_btn, click="selectMemberListener") Button mSelectMembersBtn;
	//@ViewInject(id=R.id.schedule_notice_members) EditText mNMembers;
	@ViewInject(id=R.id.schedule_notice_members_container) LinearLayout mNMembers;
	@ViewInject(id=R.id.send, click="onSendListener") Button mSendBtn;
	
	private Context mContext = this;
	private Dialog mDialog = null;
	private String mMessageContentText = "";
	private ArrayList<HashMap<String, String>> mCacheUserList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> mCacheUserIdList = new ArrayList<String>();
	private String mBackRespose = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_message);
		mTitleName.setText(getResources().getString(R.string.message_notice));//设置标题
	}
	
	public void selectMemberListener(View v) {
		Intent intent = new Intent(mContext, OaContactsMenuAct.class);
		intent.putExtra("IntentType", "OaScheduleAddAct");
		startActivityForResult(intent, 2345);
	}
	
	
	public void onSendListener(View v) {
		if(verification()) {
			commit();
		}
		
	}
	public boolean verification(){
		mMessageContentText = mMessageContent.getText().toString();
		if("".equals(mMessageContentText.trim())) {
			Toast.makeText(mContext, "请填写短信内容", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mMessageContentText.length() > 70) {
			Toast.makeText(mContext, "短信内容不能超过70个字", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mCacheUserList == null || mCacheUserList.size() == 0) {
			Toast.makeText(mContext, "请选择收件人", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	public void commit() {
		mDialog=new Dialog(mContext,R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		mDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				HashMap<String, String> properties = new HashMap<String, String>();
				properties.put("key", Constants.PUBLIC_KEY);
				StringBuilder builder = new StringBuilder();
				if(mCacheUserList != null) {
					for(int i = 0; i < mCacheUserList.size(); i ++) {
						HashMap<String, String> m = mCacheUserList.get(i);
						if(m != null) {
							String mail = m.get("UserPhone");
							if("null".equals(mail) || "".equals(mail)) {
								continue;//TODO 没有手机号码的就没有提示
							}
							if(i == mCacheUserList.size() - 1) {
								builder.append(mail);
							} else {
								builder.append(mail);
								builder.append(",");
							}
						}
					}
				}
				properties.put("receiveList", builder.toString());
				properties.put("mailBody", mMessageContentText);
				try {
					String json = WebServiceUtils.requestM(properties, Constants.WEBSERVICE_OF_SENDSMS, mContext);
					if(json != null) {
						//SendMailResponse{SendMailResult=ok; }
						String result = json.substring(json.indexOf("=") + 1, json.indexOf(";"));
						if("ok".equalsIgnoreCase(result)) {
							sendFMessage(Constants.SUCCESS);
						} else {
							mBackRespose = result;
							sendFMessage(Constants.ERROR);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 2345) {
			if(resultCode == 9897) {
				ArrayList<HashMap<String, String>> list = new DBService(mContext).getContactsSelectedList();
				if(list != null && list.size() > 0) {
					for(HashMap<String, String> m : list) {
						if(m != null) {
							if(!mCacheUserIdList.contains(m.get("UserId"))) {
								mCacheUserIdList.add(m.get("UserId"));
								mCacheUserList.add(m);
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
	
	public void initUserLayout() {
		if(mCacheUserList != null) {
			int size = mCacheUserList.size();
			if(size > 0) {
				int rows = size / 3;//每行3列
				if(size % 3 > 0) {
					rows ++;
				}
				for(int i = 1; i <= rows; i ++) {
					LinearLayout mLinearLayout = drawLinearLayout();
					mLinearLayout.addView(addButton(mCacheUserList.get(i * 3 - 3)));
					if(i * 3 - 2 < size) {
						mLinearLayout.addView(addButton(mCacheUserList.get(i * 3 - 2)));
					}
					if((i * 3 - 1) < size) {
						mLinearLayout.addView(addButton(mCacheUserList.get(i * 3 - 1)));
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
						mCacheUserList.remove(map);
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
	
	
	private void clear() {
		mMessageContentText = "";
		mTitleName.setText("");
		mMessageContent.setText("");
		mCacheUserList.clear();
		mNMembers.removeAllViews();
	}
	
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	protected void handleOtherMessage(int index) {
		switch (index) {
		case Constants.SUCCESS:
			closeDialog();
			Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
			clear();
			break;
		case Constants.ERROR:
			closeDialog();
	 		Toast.makeText(mContext, mBackRespose, Toast.LENGTH_SHORT).show();
	 		break;
		case Constants.CONNECTION_TIMEOUT:
			closeDialog();
	 		Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
	 		break;
		}
	};
	
}
