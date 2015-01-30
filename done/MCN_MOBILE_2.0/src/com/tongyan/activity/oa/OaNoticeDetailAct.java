package com.tongyan.activity.oa;

import java.util.HashMap;
import java.util.Map;


import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._Message;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName P19_NoticeDetailAct 
 * @author wanghb
 * @date 2013-7-22 pm 02:15:02
 * @desc 移动OA-公告通知详情
 */
public class OaNoticeDetailAct extends AbstructCommonActivity {
	
	private Button homeBtn, downLoadBtn;
	private TextView titleText,publisherText,dateText,fileNameText;
	
	String downloadPath;
	
	private String rowId;
	
	private Dialog mDialog;
	
	private _User localUser;
	private String isSucc;
	
	private _Message msg;
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_notice_detail);
		homeBtn = (Button)findViewById(R.id.p19_notice_home_btn);
		downLoadBtn = (Button)findViewById(R.id.p19_notice_detail_download_btn);
		titleText = (TextView)findViewById(R.id.p19_notice_detail_title_text); 
		publisherText = (TextView)findViewById(R.id.p19_notice_detail_publisher_text); 
		webView = (WebView)findViewById(R.id.p19_notice_detail_contnt_text); 
		dateText = (TextView)findViewById(R.id.p19_notice_detail_time_text); 
		fileNameText = (TextView)findViewById(R.id.p19_notice_detail_file_text); 
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		downLoadBtn.setOnClickListener(downLoadBtnListener);
	}
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		Bundle bundle = getIntent().getExtras();
		//msg = (_Message) bundle.getSerializable("_message");
		rowId = bundle.getString("_message");
		anotherThread();
	}
	
	private void anotherThread() {
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
				properties.put("userName", localUser.getUsername());
				properties.put("Password", localUser.getPassword());
				properties.put("type", "Notice");//
				properties.put("id", rowId);
				try {
					String str = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETCONTENT, OaNoticeDetailAct.this);
					Map<String,Object> mR = new Str2Json().getNoticeContent(str);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							_Message message = (_Message)mR.get("v");
							if(message != null ) {
								msg = message;
							} 
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} 
			}
		}).start();
		
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaNoticeDetailAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	int backParams;
	String filename;
	OnClickListener downLoadBtnListener = new OnClickListener() {
		public void onClick(View v) {
			filename = fileNameText.getText().toString();
			final String path  = downloadPath;
			if(path != null) {
				new Thread(new Runnable() {
					@Override
					public void run() { 
						backParams = new com.tongyan.utils.HttpDownloader().downfile(path, Constansts.CN_NOTICE_PATH, filename);
						
						if(backParams == 1) {//文件 下载成功
							sendMessage(Constansts.MES_TYPE_1);
						} else if(backParams == -1) {
							sendMessage(Constansts.MES_TYPE_2);
						} else if(backParams == 0) {
							sendMessage(Constansts.MES_TYPE_3);
						} 
					}
				}).start();
			}
			
		}
	};
	
	private void setPageText() {
		
		if(msg != null) {
		titleText.setText(msg.getnTitle());
		publisherText.setText(msg.getnPublisher());
		//webView.getSettings().setDefaultTextEncodingName("UTF-8");//UTF-8
		/*
		 StringBuilder buf = new StringBuilder(infocontent.length());
			   for (char c : infocontent.toCharArray()) {//loadData()中的html data中不能包含'#', '%', '\', '?'四中特殊字符
			       switch (c) {
			         case '#':  buf.append("%23"); break;
			         case '%':  buf.append("%25"); break;
			         case '\'': buf.append("%27"); break;
			         case '?':  buf.append("%3f"); break;               
			         default:
			           buf.append(c);
			           break;
			       }
			   }
		 */
		webView.loadDataWithBaseURL("", msg.getnContent(), "text/html", "UTF-8", "");// 以WebView的形式，加载内容
		
		dateText.setText(msg.getnDate());
		fileNameText.setText(msg.getnFileName() == null || "null".equals(msg.getnFileName()) ? "无" : msg.getnFileName());
		downloadPath = msg.getnPath();
		if(msg.getnFileName() == null || "null".equals(msg.getnFileName())) {
			downLoadBtn.setVisibility(View.INVISIBLE);
		}
		
		}
	}
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			setPageText();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1:
			Toast.makeText(this, "文件已存在", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_2:
			Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_3:
			Toast.makeText(this, "已保存 "+ FileUtils.getSDCardPath() + Constansts.CN_NOTICE_PATH + filename, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
