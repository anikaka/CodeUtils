package com.tongyan.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 抽象类AbstructCommonActivity  hander接受子线程发送的数据, 并用此数据配合主线程更新UI
 * @author wanghb
 * @date 2014/11/03
 */
public abstract class AbstructCommonActivity extends Activity {

	private MyHandler handler = new MyHandler();
	private Dialog mDialog;
	private Context mContext=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void handleOtherMessage(int flag) {

	}

	public void sendMessage(int flag) {
		handler.sendEmptyMessage(flag);
	}

	public void sendMessageDely(int flag, long delayMillis) {
		handler.sendEmptyMessageDelayed(flag, delayMillis);
	}
	
	/** 显示对话框*/
	protected void baseShowDialog(){
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
	
	/**关闭对话框 */
	protected void baseCloseDialog(){
		if(mDialog!=null){
			mDialog.dismiss();
		//	mDialog = null;
		}
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				default:
					handleOtherMessage(msg.what);
				}
			}
		}
	}
}
