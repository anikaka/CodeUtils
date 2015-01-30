package com.tongyan.activity.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.activity.R;
import com.tongyan.utils.CommonUtils;
import com.tongyan.utils.ConnectivityUtils;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @className P12_DocumentBaseInfoAdapter
 * @author wanghb
 * @date 2014-4-16 am 11:09:46
 * @Desc TODO
 */
public class OaDocumentAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private ArrayList<HashMap<String, String>> mBaseInfoList;
	private int mItemLayout;
	private Context mContext;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case Constansts.MES_TYPE_1:
				Toast.makeText(mContext, "文件已存在", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.MES_TYPE_2:
				Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.MES_TYPE_3:
				//Toast.makeText(mContext, "已保存 "+new FileComUtils().getSDPATH() + Constansts.CN_NOTICE_PATH + (String)msg.obj, Toast.LENGTH_SHORT).show();
				Toast.makeText(mContext, "已保存 单击可打开", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			
		};
	};
	
	public OaDocumentAdapter(Context context, ArrayList<HashMap<String, String>> mBaseInfoList, int mItemLayout) {
		layoutInflater = LayoutInflater.from(context);
		this.mBaseInfoList = mBaseInfoList;
		this.mItemLayout = mItemLayout;
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return mBaseInfoList == null ? 0 : mBaseInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mBaseInfoList != null) {
			return mBaseInfoList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(mItemLayout, null);
			holder = new ViewHolder();
			holder.mInfoTitle = (TextView) convertView.findViewById(R.id.p12_document_info_title);
			holder.mInfoContent = (TextView) convertView.findViewById(R.id.p12_document_info_content);
			holder.mDownloadBtn = (Button) convertView.findViewById(R.id.p12_document_info_download_btn);
			holder.mWebView = (WebView) convertView.findViewById(R.id.p12_document_info_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> map = mBaseInfoList.get(position);
		if(map != null) {
			String state = map.get("state");
			holder.mInfoTitle.setText(map.get("value"));
			String name = CommonUtils.toHandlerString(map.get("name"));
			if("1".equals(state)) {//内容 html格式
				holder.mInfoContent.setVisibility(View.GONE);
				holder.mWebView.setVisibility(View.VISIBLE);
				holder.mWebView.loadDataWithBaseURL("", name, "text/html", "UTF-8", "");//以WebView的形式，加载内容
			} else if("2".equals(state)) {//附件
				holder.mDownloadBtn.setVisibility(View.VISIBLE);
				holder.mInfoContent.setVisibility(View.VISIBLE);
				holder.mWebView.setVisibility(View.GONE);
				holder.mInfoContent.setText(name);
			} else {
				holder.mDownloadBtn.setVisibility(View.GONE);
				holder.mInfoContent.setVisibility(View.VISIBLE);
				holder.mWebView.setVisibility(View.GONE);
				holder.mInfoContent.setText(name);
			}
			holder.mItemData = map;
		}
		
		holder.mDownloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mFilePath = map.get("path");
				String fileName = null;
				if(mFilePath != null && !"".equals(mFilePath.trim())) {
					try {
						fileName = mFilePath.substring(mFilePath.lastIndexOf("/") + 1);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					mFilePath =  ConnectivityUtils.getRoute(mContext) + "/" +mFilePath;
					File file = new File(FileUtils.getSDCardPath() + Constansts.CN_NOTICE_PATH + fileName);
					if(file.exists()) {
						dialog(file, fileName, mFilePath);
					} else {
						download(fileName,mFilePath);
					}
				}
			}
		});
		return convertView;
	}
	
	
	protected void dialog(final File file, final String fileName, final String filePath) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("该附件已存在，需要重新下载吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(file != null) {
					file.delete();
				}
				download(fileName,filePath);
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void download(final String fileName, final String filePath) {
		new Thread(new Runnable() {
			@Override
			public void run() { 
				int backParams = new com.tongyan.utils.HttpDownloader().downfile(filePath, Constansts.CN_NOTICE_PATH, fileName);
				Message msg = new Message();
				if(backParams == 1) {
					msg.arg1 = Constansts.MES_TYPE_1;
					mHandler.sendMessage(msg);
				} else if(backParams == -1) {
					msg.arg1 = Constansts.MES_TYPE_2;
					mHandler.sendMessage(msg);
				} else if(backParams == 0) {
					msg.arg1 = Constansts.MES_TYPE_3;
					msg.obj = fileName;
					mHandler.sendMessage(msg);
				} 
			}
		}).start();
	}
	
	public final class ViewHolder {
		public TextView mInfoTitle;
		public TextView mInfoContent;
		public Button mDownloadBtn;
		public WebView mWebView;
		public HashMap<String, String> mItemData;
	}
	
}
