package com.tongyan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

/**
 * 抽象类AbstructCommonActivity  hander接受子线程发送的数据, 并用此数据配合主线程更新UI
 * 
 * @author wanghb
 * 
 */
public abstract class AbstructFragmentActivity extends FragmentActivity {

	private MyHandler handler = new MyHandler();

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
