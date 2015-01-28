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

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
 * @category单个收文查看
 * @author ChenLang
 *	@version Yan An 1.0
 */

public class OaReceiveTextSingleAct extends FinalActivity   {

	//标题
	@ViewInject(id=R.id.title_oaRecevice)
	TextView mTxtTitle;
	//发件人
	@ViewInject(id=R.id.user_oaRecevice)
	TextView mTxtUser;
	//日期
	@ViewInject(id=R.id.time_oaRecevice)
	TextView mTxtTime;
	//附件
	@ViewInject(id=R.id.listView_file_oaRecevice)
	ListView mListViewFile;
	//是否有文件
	@ViewInject(id=R.id.txtFileIsEmpty_oaRecevice)
	TextView mTxtFileIsEmpty_oaRecevice;
	//备注
	@ViewInject(id=R.id.remark_oaRecevice)
	TextView  mTxtRemark;
	
	@ViewInject(id=R.id.progressReceiveText_upload)
    ProgressBar mProgressBar;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private String  mNewId;//单条收文Id
	private HashMap<String,String> mMap=new HashMap<String, String>();
	private HashMap<String,String> mMapJson=new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> mArrayListFile=new ArrayList<HashMap<String,String>>();//附件集合
	private ArrayList<HashMap<String, String>> mArrayListFileJson=new ArrayList<HashMap<String,String>>();
	private OaFileAdapter mFileAdapter;//文件适配器
//	private Dialog mDialog;
//	private ProgressBar  mProgressBar;
	private  int mDownUploadSize; //下载文件大小
	private int  mFileSize;//文件总大小
	private File mFile;
	private String mFileSuffix;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_receivetext_single);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mNewId=getIntent().getExtras().getString("newId");
		}
		requestReceiveTextSingle();
		//构造文件设配器
		mFileAdapter=new OaFileAdapter(mContext, mArrayListFile, R.layout.oa_receivetext_file_listview_item);
		mListViewFile.setAdapter(mFileAdapter);
		mListViewFile.setOnItemClickListener(listenerListViewFile);
	}
	
	//文件列表监听器
	OnItemClickListener  listenerListViewFile=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		OaFileAdapter.ViewHolderOaReceiveTextFire  mViewHolder=(OaFileAdapter.ViewHolderOaReceiveTextFire)view.getTag();
		final HashMap<String, String> mMap=mViewHolder.mMapReciveFile;
		if(mMap!=null && mMap.size()>0){
			String mFileName=mMap.get("fileName");
			String mUpdateData=mMap.get("updateTime");
			//查看目录是否存在,如果不存在就创建目录
			if(!FileTools.isFileExist("OA/收文查看/"+mNewId.replaceAll("-", "")+"_"+mUpdateData)){
				FileTools.createDir("OA/收文查看/"+mNewId.replaceAll("-", "")+"_"+mUpdateData);
			}
			/*
			 *1.首先判断这个文件是否存在不存在就下载 
			 *  
			 *2.如果存在,看是否url相等(及不能修改),如果相等就查看,如果不相等重新在下载
			 */
			 mFile=new File(Environment.getExternalStorageDirectory().getPath()+"/OA/收文查看/"+mNewId.replaceAll("-", "")+"_"+mUpdateData+"/"+mFileName);
			//如果文件存在就查看
			 mFileSuffix=mFileName.substring(mFileName.lastIndexOf(".")+1); //文件后缀
			if(!mFile.exists()){
				//下载完成查看
				mProgressBar.setVisibility(View.VISIBLE);
				requestReceiveFile(mMap,mFile);
			}else{
				openFile(mFileSuffix, mFile);
		}
		}
		
	}
};
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
	/**请求单条收文信息*/
	public void requestReceiveTextSingle(){
			new Thread(){
				public void run(){
					HashMap<String, String> params=new HashMap<String, String>();
					params.put("method", Constants.METHOD_OF_OA_RECEIVEText_SINGLE);
					params.put("key", Constants.PUBLIC_KEY);
					params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID,""));
					params.put("type", "false");
					params.put("newId", mNewId); //单条收文Id
					params.put("fieldList", "");
					String mBoby=null;
					try {
						mBoby=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params, mContext));
						if(mBoby!=null){
								if(JsonTools.getCommonResult(mBoby)){
									//解析Json信息
									mMapJson=JsonTools.getReceiveTextSingle(mBoby);
									//解析文件
									mArrayListFileJson=JsonTools.getReceiveTextFile(mBoby);
									sendFMessage(Constants.SUCCESS);
								}else{
									//TOD 访问失败
								}
						}else{
							//TODO访问失败
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
	}
	
	/** 文件下载
	 * 	主要采用文件的IO*/
	public void requestReceiveFile(final HashMap<String, String> map,final File mFile){
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
							sendFMessage(Constants.COMMON_MESSAGE_1);
						}
						//关闭流
						os.close();
						is.close();
						sendFMessage(Constants.COMMON_MESSAGE_2);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					sendFMessage(Constants.COMMON_MESSAGE_3);
					return;
				} catch (IOException e) {
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				}
					
			  }
			
		}.start();
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
			setListViewHeightBasedOnChildren(mListViewFile);
		}
	}
	
	/**设置文本的信息*/
	public void  setText(){
		if(mMap!=null && mMap.size()>0){
			mTxtTitle.setText(mMap.get("title"));
			mTxtUser.setText(mMap.get("userName"));
			mTxtTime.setText(mMap.get("createTime"));
			if(mArrayListFile!=null && mArrayListFile.size()>0){

				mTxtFileIsEmpty_oaRecevice.setVisibility(View.INVISIBLE);
			}else{
				mTxtFileIsEmpty_oaRecevice.setVisibility(View.VISIBLE);
				mTxtFileIsEmpty_oaRecevice.setText("没有附件");
			}
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
			break;
		case Constants.COMMON_MESSAGE_1:
				if(mProgressBar!=null){
					int result=(mDownUploadSize*100)/mFileSize;
					if(100==result){
					}else{						
						mProgressBar.setProgress(result);
					}
				}
				mProgressBar.setVisibility(View.INVISIBLE);
			break;
		case Constants.COMMON_MESSAGE_2:
			if(mProgressBar!=null){
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			openFile(mFileSuffix, mFile);
			break;
		case Constants.COMMON_MESSAGE_3 :
			if(mProgressBar!=null){
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			Toast.makeText(mContext,"资源文件不存在" , Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}