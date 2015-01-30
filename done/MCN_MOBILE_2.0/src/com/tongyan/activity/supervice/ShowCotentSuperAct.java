package com.tongyan.activity.supervice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.risk.P43_ShowPictureAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Check;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

/**
 * 
 * @ClassName P32_UpdateSuperAct 
 * @author wanghb
 * @date 2013-8-13 pm 02:44:45
 * @desc 
 */
public class ShowCotentSuperAct extends AbstructCommonActivity{
	private Context mContext = this;
	private Button mHomeBtn,mShowPictureBtn;
	private EditText mEditText;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String mIsSucc;
	private String mRemoteContent;
	private ArrayList<String> mRemoteList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supervise_content);
		initPage();
		setClickListener();
		businessM();
	}
	private void initPage() {
		mHomeBtn = (Button)findViewById(R.id.p31_supervise_camera_home_btn);
		mShowPictureBtn = (Button)findViewById(R.id.p40_risk_see_pic_btn);
		mEditText = (EditText)findViewById(R.id.p31_supervise_edittext);
	}
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(homeBtnListener);
		mShowPictureBtn.setOnClickListener(pictureBtnListener);
		mEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mEditText.setCursorVisible(false);//去光标
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		if(getIntent() != null && getIntent().getExtras() != null) {
			if(getIntent().getExtras().getString("rowId") != null && !"".equals(getIntent().getExtras().getString("rowId"))) {
				getRemote(getIntent().getExtras().getString("rowId"));
			}
			_Check mCheck = ((_Check)getIntent().getExtras().get("check"));
			if(mCheck != null) {
				mEditText.setText(mCheck.getCheckContent());
				List<_LocalPhotos> photsList = new DBService(mContext).getPhotosList(mCheck.getId().toString());
				if(photsList != null && photsList.size() > 0){
					for(int i = 0; i < photsList.size(); i ++) {
						_LocalPhotos mPhoto = photsList.get(i);
						if(mPhoto != null)
						mRemoteList.add(mPhoto.getRemote_img_path());
						}
					}
				}
			}
	}
	
	public void getRemote(final String rowId) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		 new Thread(new Runnable() {
				@Override
				public void run() {
					Map<String,String> properties = new HashMap<String,String>();
					properties.put("publicKey", Constansts.PUBLIC_KEY);
					properties.put("userName", mLocalUser.getUsername());
					properties.put("Password", mLocalUser.getPassword());
					properties.put("type", "GetSupervise");//
					properties.put("id", rowId);
					try {
						String jsonStr = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, mContext);
						Map<String,Object> mR = new Str2Json().getResultPicture(jsonStr);
							if(mR != null) {
								mIsSucc = (String)mR.get("s");
								if("ok".equals(mIsSucc)) {
									mRemoteContent = (String)mR.get("content");
									mRemoteList = (ArrayList<String>)mR.get("v");
									sendMessage(Constansts.SUCCESS);
								} else {
									sendMessage(Constansts.ERRER);
								}
							} else {
								sendMessage(Constansts.CONNECTION_TIMEOUT);
							}
					} catch (Exception e) {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
						e.printStackTrace();
					} 
				};
			}).start(); 
	}
	
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnClickListener pictureBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(mRemoteList != null && mRemoteList.size() > 0) {
				Intent intent = new Intent(mContext,P43_ShowPictureAct.class);
				intent.putExtra("mIntentType", "2");
				intent.putStringArrayListExtra("mRemoteList", mRemoteList);
				startActivity(intent);
			} else {
				Toast.makeText(mContext, "无可浏览图片", Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			mEditText.setText(mRemoteContent);
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
