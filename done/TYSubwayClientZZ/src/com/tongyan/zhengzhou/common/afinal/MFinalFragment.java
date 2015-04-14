package com.tongyan.zhengzhou.common.afinal;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

public class MFinalFragment extends Fragment{

	private MHandler handler = new MHandler();

	protected void handleOtherMessage(int flag) {
		switch (flag) {
		   default:
			   break;
		}
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
