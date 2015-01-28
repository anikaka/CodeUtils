package com.tongyan.yanan.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

/**
 * @category Fragment基类
 * @author ChenLang
 * @date  2014/06/18
 *@version YanAn 1.0
 */
public class BaseFragement  extends Fragment{
	
	/**
	 * add by wanghb 
	 * 2014-06-26
	 */
	private MHandler mHandler = new MHandler();

	protected void handleOtherMessage(int flag) {
		
	}

	public void sendFragmentMessage(int flag) {
		mHandler.sendEmptyMessage(flag);
	}

	public void sendMessageDely(int flag, long delayMillis) {
		mHandler.sendEmptyMessageDelayed(flag, delayMillis);
	}
	
	private class MHandler extends Handler {
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
