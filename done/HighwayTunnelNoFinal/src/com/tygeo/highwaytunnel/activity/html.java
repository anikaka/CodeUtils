package com.tygeo.highwaytunnel.activity;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.InfoApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class html extends Activity {
	//关于同岩的网页界面
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.about_html);
		InfoApplication.getinstance().addActivity(this);
		WebView webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl("file:///android_asset/about.html");
	}
	
}
