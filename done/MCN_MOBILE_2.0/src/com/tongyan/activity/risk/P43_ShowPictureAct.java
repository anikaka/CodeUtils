package com.tongyan.activity.risk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.RiskPictureAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.AsyncImageView;
import com.tongyan.widget.view.AsyncImageView.OnImageViewLoadListener;
/**
 * 
 * @ClassName P43_ShowPictureAct.java
 * @Author wanghb
 * @Date 2013-10-16 pm 02:08:56
 * @Desc TODO
 */
public class P43_ShowPictureAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String mIsSucc;
	
	private GridView mGridView;
	private TextView mImageView;
	
	private List<String> mPathList = new ArrayList<String>();
	private RiskPictureAdapter mAdapter;
	private String mIntentType;//0:风险展示，1：风险提示，2：监督监理
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_showing_picture);
		mGridView = (GridView)findViewById(R.id.p43_gridview);
		mImageView = (TextView)findViewById(R.id.p04_station_title_words);
		mAdapter = new RiskPictureAdapter(this,mPathList);
		mGridView.setAdapter(mAdapter);
	}
	
	private void setClickListener() {
		mGridView.setOnItemClickListener(mOnItemClickListener);
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mIntentType = getIntent().getExtras().getString("mIntentType");
			 if("0".equals(mIntentType)) {
				 //mImageView.setBackgroundResource(R.drawable.p42_title_image_words);//风险展示
				 mImageView.setText(getResources().getString(R.string.main_risk_showing_text));
				 getRemote(getIntent().getExtras().getString("maincheckid"), getIntent().getExtras().getString("position"));//
			 } else if("1".equals(mIntentType)) {
				 //mImageView.setBackgroundResource(R.drawable.p38_risk_showing_title);//风险提示
				 mImageView.setText(getResources().getString(R.string.main_risk_notice_text));
				 getRemote(getIntent().getExtras().getString("maincheckid"), getIntent().getExtras().getString("position"));//
			 } else if("2".equals(mIntentType)){
				 //mImageView.setBackgroundResource(R.drawable.p33_supervice_read_content_title);
				 mImageView.setText(getResources().getString(R.string.supervice_check_content));//检查内容
				 List<String> mRemoteList = getIntent().getExtras().getStringArrayList("mRemoteList");
				 if(mPathList != null) {
						mPathList.clear();
						if(mRemoteList != null)
						mPathList.addAll(mRemoteList);
				 }
				 mAdapter.notifyDataSetChanged();
			 }
			
		}
	}
	
	
	public void getRemote(final String checkid,final String position) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		 new Thread(new Runnable() {
				@Override
				public void run() {
					String params = "{checkid:'" + checkid + "',position:'" + position + "'}";
					try {
						String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "GetRiskPositionItemPic", params, Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
						Map<String,Object> mR = new Str2Json().checkPictureList(str);
							if(mR != null) {
								mIsSucc = (String)mR.get("s");
								if("ok".equals(mIsSucc)) {
									List<String> mRemoteDetailList = (List<String>)mR.get("v");
									if(mPathList != null) {
										mPathList.clear();
										mPathList.addAll(mRemoteDetailList);
									}
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
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			RiskPictureAdapter.ViewHolder mViewHolder = (RiskPictureAdapter.ViewHolder)arg1.getTag();
			if(null != mViewHolder && mViewHolder.mPath != null) {
				MDialog dialogCategory = new MDialog(P43_ShowPictureAct.this, R.style.dialog);
				dialogCategory.createDialog(R.layout.supervice_see_pop, 0.95,0.95, getWindowManager());
				dialogCategory.setCanceledOnTouchOutside(false);
				AsyncImageView view = (AsyncImageView)dialogCategory.findViewById(R.id.p33_see_supervice_p);
				view.setPath(mViewHolder.mPath);
				
				view.setOnImageViewLoadListener(new OnImageViewLoadListener(){
					@Override
					public void onLoadingStarted(AsyncImageView imageView) {
						imageView.setDefaultImageResource(R.drawable.p00_ing_picture);
					}

					@Override
					public void onLoadingEnded(AsyncImageView imageView, Bitmap image) {
					}

					@Override
					public void onLoadingFailed(AsyncImageView imageView,
							Throwable throwable) {
						imageView.setDefaultImageResource(R.drawable.p00_no_picture);
					}});
			}
		}
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			mAdapter.notifyDataSetChanged();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, mIsSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
}
