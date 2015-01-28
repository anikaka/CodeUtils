package com.tongyan.yanan.act;

import java.io.IOException;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DownloadFileService;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 程序入口,编码格式UTF-8
 * 
 * @author ChenLang 
 * 1.每次进来访问服务器的版本,与本地的是否一致 如果一致,则不弹出更新版本对话框
 *  否,弹出对话框提示更新,
 *  点击确认,从服务器下载到本地并且运行
 *   ,否,正常跳转
 */

public class LoadingAct extends FinalActivity {

	private Context mContext=this;
	private HashMap<String, String> mMapVersion=new HashMap<String, String>();
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		getRequestVeision(Constants.VERSION_TYPE);
	
	}
	
	/**
	 * 获取服务器版本号
	 */
	public void getRequestVeision(final String type){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_VERSION);
				params.put("key",Constants.PUBLIC_KEY);
				params.put("versionId", String.valueOf(Constants.VERSION_CODE));//版本号
				params.put("type", type);//类型
				String mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_VERSION, params, mContext));
				if(mStream!=null){
					if(JsonTools.getCommonResult(mStream)){
						//返回为真
						mMapVersion=JsonTools.getVersion(mStream);
						sendFMessage(Constants.SUCCESS);
					}else{
						sendFMessage(Constants.ERROR);
					}
				}else{
					sendFMessage(Constants.ERROR);
				}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 比较当前版本和服务器版本是否一致
	 * <dl>
	 * 如果 当前版本小于服务器版本 更新
	 * <dl>
	 * 否则 不错任何操作
	 */
	public boolean getVersionUpdate() {

		String version = mMapVersion.get("version");
		double newVersionCode = 0;
		try {
			newVersionCode = Double.parseDouble(version);
		} catch(Exception e) {
			
		}
		if (newVersionCode > Constants.VERSION_CODE) {
			return true;
		}
		return false;
	}
	
		
	/** 跳转到登陆界面 */
	public void intoLoginPage() {
		Intent intent = new Intent(mContext, LoginAct.class);
		startActivity(intent);
		this.finish();
	}
		
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
			case Constants.SUCCESS:
				if(getVersionUpdate()){
					//如果有版本更新提示对框
					mDialog=new Dialog(mContext, R.style.dialog);
					mDialog.setCanceledOnTouchOutside(false);
					mDialog.setContentView(R.layout.common_tips_dialog);
					mDialog.show();
					TextView mTitle =(TextView)mDialog.findViewById(R.id.tipsDialogTitle);
					mTitle.setText("提示");
					TextView mTipsContent =(TextView)mDialog.findViewById(R.id.tipsDialogContent);
					mTipsContent.setText("确定更新版本?");
					Button mButtonConfirm=(Button)mDialog.findViewById(R.id.ok);
					Button mButtonCancle=(Button)mDialog.findViewById(R.id.cancle);
					//确定更新
					mButtonConfirm.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(mContext, DownloadFileService.class);
							intent.putExtra("fileInfo", mMapVersion);
							startService(intent);
							mDialog.dismiss();
						}
					});
					//取消更新
					mButtonCancle.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mDialog.dismiss();
							intoLoginPage();
						}
					});
				} else {
					sendFMessage(Constants.ERROR);
				}
				break;
			case  Constants.ERROR:
				intoLoginPage();
				break;
		}	
	}

}
