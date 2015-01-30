package com.tongyan.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
/**
 * 
 * @Title: AbstructFragment.java 
 * @author Rubert
 * @date 2014-10-20 PM 02:29:18 
 * @version V1.0 
 * @Description: Fragment 抽象类
 */
public abstract class AbstructFragment extends Fragment {
		
	private MHandler handler = new MHandler();
	
	
	protected void handleOtherMessage(int flag) {

	}

	public void sendMessage(int flag) {
		handler.sendEmptyMessage(flag);
	}
	
	public void sendMessageDely(int flag, long delayMillis) {
		handler.sendEmptyMessageDelayed(flag, delayMillis);
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
