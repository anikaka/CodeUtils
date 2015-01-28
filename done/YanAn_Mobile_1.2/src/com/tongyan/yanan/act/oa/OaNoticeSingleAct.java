package com.tongyan.yanan.act.oa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * @category单条公告信息
 * @author ChenLang
 * @date 2014/07/28
 * @version YanAn 1.0
 */
public class OaNoticeSingleAct extends FinalActivity  implements OnItemClickListener{
	
	//标题
	@ViewInject(id=R.id.title_oaNotice) 
	TextView mTxtTitle;
	//发件人
	@ViewInject(id=R.id.user_oaNotice)
	TextView mTxtUser;
	//日期
	@ViewInject(id=R.id.time_oaNotice)
	TextView mTxtTime;
	//附件
	@ViewInject(id=R.id.listView_file_oaNotice)
	ListView mListView;
	//
	@ViewInject(id=R.id.txtFileIsEmpty_oaNotice)
	TextView mTxtFileIsEmpty_oaNotice;
	//内容
	@ViewInject(id=R.id.content_oaNotice)
	EditText mContent_oaNotice;
	//文号
	@ViewInject(id=R.id.fileNumber_oaNotice)
	TextView  mFileNumber_oaNotice; 
	//类别名称
	@ViewInject(id=R.id.dName_oaNotice)
	TextView mDNmae_oaNotice;
	//进度条
	@ViewInject(id=R.id.progressNotice_upload)
	ProgressBar  mProgressBar;
	
	private Context mContext = this;
	private SharedPreferences mSP;
	private String mNewId;// 公告主键Id
	private OaFileAdapter mAdapter;
	private HashMap<String, String> mMap = new HashMap<String, String>();
	private HashMap<String, String> mMapJson = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> mJsonList = new ArrayList<HashMap<String, String>>();
	private  int mDownUploadSize; //下载文件大小
	private int mFileSize;//总文件大小
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_notice_single);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mNewId=getIntent().getExtras().getString("newId");
		}

		mAdapter=new OaFileAdapter(mContext, mList, R.layout.oa_receivetext_file_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		requestNoticeSingle();
	}
	
	/** 获取单条公告的信息*/
	public void requestNoticeSingle(){
		new Thread(){
			public void run(){
				HashMap<String, String>  params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_NOTICE_SINGLE);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId",mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("newId", mNewId);
				params.put("fieldList", "");
				String  mStream=null;
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params, mContext));
					if(mStream!=null && !"".equals(mStream)){
							if(JsonTools.getCommonResult(mStream)){
								//解析数据
								mJsonList=JsonTools.getReceiveTextFile(mStream);
								mMapJson=JsonTools.getNoticeSingle(mStream);
								sendFMessage(Constants.SUCCESS);
							}else{
								sendFMessage(Constants.COMMON_MESSAGE_1);
							}
					}else{
						sendFMessage(Constants.COMMON_MESSAGE_1);
					}
				} catch (IOException e) {
						sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/** 附件数据跟新*/
	public void refersh(){
		if(mMapJson!=null && mMapJson.size()>0){
			mMap.putAll(mMapJson);
			mMapJson.clear();
		}
		if(mJsonList!=null && mJsonList.size()>0){
			mList.addAll(mJsonList);
			mAdapter.notifyDataSetChanged();
			mJsonList.clear();
		}else{
			mTxtFileIsEmpty_oaNotice.setVisibility(View.VISIBLE);
			mTxtFileIsEmpty_oaNotice.setText("没有附件");
		}
		setListViewHeightBasedOnChildren(mListView);
	}
	
	/** 设置文本的信息*/
	public void  setText(){
		if(mMap!=null && mMap.size()>0){
			mTxtTitle.setText(mMap.get("title"));
			mTxtTime.setText(mMap.get("createTime"));
			mTxtUser.setText(mMap.get("userName"));
			mContent_oaNotice.setText(mMap.get("content"));
			mFileNumber_oaNotice.setText(mMap.get("fileNumber"));
			mDNmae_oaNotice.setText(mMap.get("dName"));
		}
	}
	

	/** 文件下载
	 * 	主要采用文件的IO*/
	public void requestReceiveFile(final HashMap<String, String> map){
		new Thread(){
			public void run(){
				String mUrl=map.get("fileUrl");
				try {
						URL url=new URL(mUrl);
						//打开连接
						URLConnection con=url.openConnection();
						con.connect();
						mFileSize=con.getContentLength();
						InputStream is=con.getInputStream();
						byte []bs=new byte[1024];
						int len=0;
						OutputStream os= new FileOutputStream(mFile);
						//
						while((len=is.read(bs))!=-1){
							os.write(bs,0,len);
							mDownUploadSize+=len;
							//sendFMessage(Constants.COMMON_MESSAGE_1);
						}
						//关闭流
						os.close();
						is.close();
						sendFMessage(Constants.COMMON_MESSAGE_2);
				}catch(FileNotFoundException e) {
					sendFMessage(Constants.COMMON_MESSAGE_3);
					e.printStackTrace();
				}catch (MalformedURLException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
					
			  }
			
		}.start();
	}

	/** 文件下载*/
	/*public void requestFile(final HashMap<String, String> map,final File mFile){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				String mFileName = map.get("fileName");
				String mUrl = map.get("fileUrl");
				Log.i("test", "url="+mUrl);
				try {
					URL url = new URL(mUrl);
					// 打开连接
					URLConnection conn = url.openConnection();
					conn.connect();
					// 获取总文件的大小
					mFileSize = conn.getContentLength();
					InputStream is = conn.getInputStream();
					byte[] b = new byte[1024];
					OutputStream os = new FileOutputStream(mFile);
					int len = 0;
					while ((len = is.read()) != -1) {
						os.write(b, 0, len);
						mDownUploadSize += len;
						sendFMessage(Constants.COMMON_MESSAGE_2);
					}
					// 关闭流
					if (os != null) {
						os.close();
					}
					if (is != null) {
						is.close();
					}
					sendFMessage(Constants.COMMON_MESSAGE_3);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
//					sendFMessage(Constants.ERROR);
				}

			}
		}).start();
	}*/
	
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
			Toast.makeText(mContext, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
			mProgressBar.setVisibility(View.INVISIBLE);
			break;
		case Constants.COMMON_MESSAGE_1:
			Toast.makeText(mContext, "服务器繁忙,请稍后再试", Toast.LENGTH_SHORT).show();
			mProgressBar.setVisibility(View.INVISIBLE);
			break;
		case Constants.COMMON_MESSAGE_2:
			if(mProgressBar!=null){
				int result=(mDownUploadSize*100)/mFileSize;
					mProgressBar.setProgress(result);
			}
			mProgressBar.setVisibility(View.INVISIBLE);
			openFile(mFileSuffix, mFile);
			break;
		case Constants.COMMON_MESSAGE_3:
			Toast.makeText(mContext, "目标文件不存在", Toast.LENGTH_SHORT).show();
			if(mProgressBar!=null){
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			break;
		default:
			break;
		}
	}
	File mFile = null;
	String mFileSuffix = null;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OaFileAdapter.ViewHolderOaReceiveTextFire mHolderView =(OaFileAdapter.ViewHolderOaReceiveTextFire)view.getTag();
		final HashMap<String, String> mMap=mHolderView.mMapReciveFile;
		if(mMap.size()>0 && mMap!=null){
			String  mFileName=mMap.get("fileName");
			String mUpdateData=mMap.get("updateTime");
			//查看目录是否存在,如果不存在就创建目录
			if(!FileTools.isFileExist("OA/公告通知/"+mNewId.replaceAll("-", "")+"_"+mUpdateData)){
				FileTools.createDir("OA/公告通知/"+mNewId.replaceAll("-", "")+"_"+mUpdateData);
			}
			/*
			 *1.首先判断这个文件是否存在不存在就下载 
			 *  
			 *2.如果存在,看是否url相等(及不能修改),如果相等就查看,如果不相等重新在下载
			 */
			mFile =new File(Environment.getExternalStorageDirectory().getPath()+"/OA/公告通知/"+mNewId.replaceAll("-", "")+"_"+mUpdateData+"/"+mFileName);
			//如果文件存在就查看
			mFileSuffix=mFileName.substring(mFileName.lastIndexOf(".")+1); //文件后缀
			Log.i("test",Environment.getExternalStorageDirectory().getPath()+"OA/公告通知/"+mNewId.replaceAll("-", "")+"_"+mUpdateData+"/"+mFileName);
			if(!mFile.exists()){
				//下载完成查看
				mProgressBar.setVisibility(View.VISIBLE);
//				requestFile(mMap,mFile);
				requestReceiveFile(mMap);
				//openFile(mFileSuffix, mFile);
			}else{
				openFile(mFileSuffix, mFile);
		}
		}
	}
	/**查看文件*/
public void openFile(String mFileSuffix,File mFile){
	if(mFileSuffix.equals("txt") || mFileSuffix.equals("doc") || mFileSuffix.equals("docx")){						
		startActivity(FileOpen.getWordFileIntent(mFile));
	}else if(mFileSuffix.equals("pdf")){
		startActivity(FileOpen.getPdfFileIntent(mFile));
	}else if(mFileSuffix.equals("xlsx")){
		startActivity(FileOpen.getExcelFileIntent(mFile));
	}else if(mFileSuffix.equals("png") || mFileSuffix.equals("jpg") || mFileSuffix.equals("jpeg")){
		startActivity(FileOpen.getImageFileIntent(mFile));
	}
}
}
