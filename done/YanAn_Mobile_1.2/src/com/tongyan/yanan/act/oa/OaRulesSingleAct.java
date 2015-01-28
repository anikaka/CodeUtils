package com.tongyan.yanan.act.oa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.OaFileAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.FileOpen;
import com.tongyan.yanan.common.utils.FileTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 单条规章制度查看
 * 
 * @author ChenLang
 * @date 2014/07/30
 * @version YanAn 1.0
 */
public class OaRulesSingleAct  extends FinalActivity implements OnItemClickListener{

	//标题
	@ViewInject(id=R.id.title_oaRules)
	TextView mTitle__oaRules;
	//类型
	@ViewInject(id=R.id.dName_oaRules)
	TextView mDName_oaRules;
	//发件人
	@ViewInject(id=R.id.user_oaRules)
	TextView  mUser_oaRules;
	//日期
	@ViewInject(id=R.id.time_oaRules)
	TextView mTime_oaRules;
	//附件
	@ViewInject(id=R.id.listView_file_oaRules)
	ListView mListView_file_oaRules;
	//内容
	@ViewInject(id=R.id.content_oaRules)
	EditText mContent_oaRules;
	//文件下载进度条
	@ViewInject(id=R.id.progressRules_upload)
	ProgressBar mProgressBar;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private String mNewId; //规章制度Id
	private HashMap<String,String> mMap=new HashMap<String, String>();
	private HashMap<String,String> mMapJson=new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> mArrayListFile=new ArrayList<HashMap<String,String>>();//附件集合
	private ArrayList<HashMap<String, String>> mArrayListFileJson=new ArrayList<HashMap<String,String>>();
	private OaFileAdapter mFileAdapter;//文件适配器
	private  int mDownUploadSize; //下载文件大小
	private int  mFileSize;//文件总大小
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_rules_single);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mNewId=getIntent().getExtras().getString("newId");
		}
		requestReceiveTextSingle();
		//构造文件设配器
		mFileAdapter=new OaFileAdapter(mContext, mArrayListFile, R.layout.oa_receivetext_file_listview_item);
		mListView_file_oaRules.setAdapter(mFileAdapter);
		mListView_file_oaRules.setOnItemClickListener(this);
	}
	
	/**请求单条规章制度信息*/
	public void requestReceiveTextSingle(){
		
			new Thread(new Runnable() {
				@Override
				public void run() {
					HashMap<String, String> params=new HashMap<String, String>();
					params.put("method", Constants.METHOD_OF_OARULES_SINGLE);
					params.put("key", Constants.PUBLIC_KEY);
					params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID,""));
					params.put("newId", mNewId); //单条规章制度Id
					params.put("fieldList", "");
					String mBoby=null;
					try {
						mBoby=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params ,mContext));
						Log.i("test", HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params ,mContext));
						if(mBoby!=null){
								if(JsonTools.getCommonResult(mBoby)){
									//解析Json信息
									mMapJson=JsonTools.getRulesSingle(mBoby);
									//解析文件
									mArrayListFileJson=JsonTools.getReceiveTextFile(mBoby);
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
	
	/** 文件下载
	 * 	主要采用文件的IO*/
	public void requestReceiveFile(final HashMap<String, String> map,final File mFile){
		new Thread(){
			public void run(){
				String mUrl=map.get("fileUrl");
				try {
						URL url=new URL(mUrl);
//						Log.i("test","url"+ mUrl);
						//打开连接
						URLConnection con=url.openConnection();
						con.connect();
						mFileSize=con.getContentLength();
						InputStream is=con.getInputStream();
						byte []bs=new byte[1024];
						int len=0;
						OutputStream os= new FileOutputStream(mFile);
						while((len=is.read(bs))!=-1){
							os.write(bs,0,len);
							mDownUploadSize+=len;
							sendFMessage(Constants.COMMON_MESSAGE_1);
						}
						//关闭流
						os.close();
						is.close();
				
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
					sendFMessage(Constants.COMMON_MESSAGE_2);
			  }
			
		}.start();
	}
	
	/** 设置文本的信息*/
	public void  setText(){
		if(mMap!=null && mMap.size()>0){
			mTitle__oaRules.setText(mMap.get("title"));
			mTime_oaRules.setText(mMap.get("createTime"));
			mUser_oaRules.setText(mMap.get("userName"));
			mContent_oaRules.setText(mMap.get("content"));
			mDName_oaRules.setText(mMap.get("dName"));
		}
	}
	/**查看文件*/
	public void openFile(String mFileSuffix,File mFile){
		if(mFileSuffix.equals("txt") || mFileSuffix.equals("doc") || mFileSuffix.equals("docx")){						
			startActivity(FileOpen.getWordFileIntent(mFile));
		}else if(mFileSuffix.equals("pdf")){
			startActivity(FileOpen.getPdfFileIntent(mFile));
		}else if(mFileSuffix.equals("xls")){
			startActivity(FileOpen.getExcelFileIntent(mFile));
		}else if(mFileSuffix.equals("png") || mFileSuffix.equals("jpg") || mFileSuffix.equals("jpeg") || mFileSuffix.equals("gif")){
			startActivity(FileOpen.getImageFileIntent(mFile));
		}
	}
	 /** 动态改变ListView的高度 */
	 public static void setListViewHeightBasedOnChildren(ListView listView) { 
		    if(listView == null) return;
		    ListAdapter listAdapter = listView.getAdapter(); 
		    if (listAdapter == null) { 
		        return; 
		    } 

		    int totalHeight = 0; 
		    for (int i = 0; i < listAdapter.getCount(); i++) { 
		        View listItem = listAdapter.getView(i, null, listView); 
		        listItem.measure(0, 0); 
		        totalHeight += listItem.getMeasuredHeight(); 
		    } 

		    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
		    listView.setLayoutParams(params); 
		}
	 
		@Override
		protected void handleOtherMessage(int flag) {
			super.handleOtherMessage(flag);
			switch (flag) {
			case Constants.SUCCESS:
						refersh();
						setText();
				break;
			case Constants.ERROR:
					Toast.makeText(mContext, "网络繁忙,请稍后再试.",Toast.LENGTH_SHORT).show();
					mProgressBar.setVisibility(View.INVISIBLE);
				break;
			case Constants.COMMON_MESSAGE_1:
					if(mProgressBar!=null){
						int result=(mDownUploadSize*100)/mFileSize;
						if(100==result){
						}else{						
							mProgressBar.setProgress(result);
						}
					}
				break;
			case Constants.COMMON_MESSAGE_2:
//				Toast.makeText(mContext,"下载完成" , Toast.LENGTH_SHORT).show();
				if(mProgressBar!=null){
					mProgressBar.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				break;
			}
		}
		
	/** 刷新数据*/
	public void refersh(){
		if(mMapJson!=null && mMapJson.size()>0){
			mMap.putAll(mMapJson);
			mMapJson.clear();
		}
		if(mArrayListFileJson!=null && mArrayListFileJson.size()>0){
			mArrayListFile.addAll(mArrayListFileJson);
			mArrayListFileJson.clear();
			mFileAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(mListView_file_oaRules);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OaFileAdapter.ViewHolderOaReceiveTextFire mHolderView =(OaFileAdapter.ViewHolderOaReceiveTextFire)view.getTag();
		final HashMap<String, String> mMap=mHolderView.mMapReciveFile;
		if(mMap!=null && mMap.size()>0){
			String mFileName=mMap.get("fileName");
			String mUpdateData=mMap.get("updateTime");
			//查看目录是否存在,如果不存在就创建目录
			if(!FileTools.isFileExist("OA/规章制度/"+mNewId.replaceAll("-", "")+"_"+mUpdateData)){
				FileTools.createDir("OA/规章制度/"+mNewId.replaceAll("-", "")+"_"+mUpdateData);
			}
			/*
			 *1.首先判断这个文件是否存在不存在就下载 
			 *  
			 *2.如果存在,看是否url相等(及不能修改),如果相等就查看,如果不相等重新在下载
			 */
			File mFile=new File(Environment.getExternalStorageDirectory().getPath()+"/OA/规章制度/"+mNewId.replaceAll("-", "")+"_"+mUpdateData+"/"+mFileName);
			//如果文件存在就查看
			String mFileSuffix=mFileName.substring(mFileName.lastIndexOf(".")+1); //文件后缀
			if(!mFile.exists()){
				//下载完成查看
				mProgressBar.setVisibility(View.VISIBLE);
				requestReceiveFile(mMap,mFile);
				openFile(mFileSuffix, mFile);
			}else{
				openFile(mFileSuffix, mFile);
		}
		}
		
	}
}
