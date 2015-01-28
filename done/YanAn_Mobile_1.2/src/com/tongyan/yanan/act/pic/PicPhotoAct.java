package com.tongyan.yanan.act.pic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicPhotoAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 拍照时间列表
 * @author ChenLang
 */

public class PicPhotoAct  extends FinalActivity implements OnClickListener,OnItemClickListener{

	@ViewInject(id=R.id.rlAddPic)
	RelativeLayout rlAddPic;
	//照片时间类列表
	@ViewInject(id=R.id.listView_picPhoto)
	ListView mListView;
	private Context mContext=this;
	private SharedPreferences mSP;
	private String mNewId;
	private Bundle mBundle;
	private PicPhotoAdapter mAdapter;
	private ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListPicUpload=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListSQL=new ArrayList<HashMap<String,String>>();
	private  static final int PHOHT=0x141;
	private String mUrl="192.168.0.222";//
	private String mUser="ftpuser";
	private String mPwd="ftp";
	private Dialog mDialog;
	private Dialog mProgressBar;
	private int mType;
	private int  mPicLoadFlag=0;
	private ExecutorService executorService = Executors.newFixedThreadPool(2);//2N
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.pic_photo);
	    mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
	    if(getIntent().getExtras()!=null){
	    	mBundle=getIntent().getExtras();
	    	mNewId=mBundle.getString("newId");
	    	mType=Integer.parseInt(mBundle.getString("type"));
	    }
	    //构造适配器
	    mArrayList= new DBService(mContext).queryPic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId);
	    mAdapter=new PicPhotoAdapter(mContext, mArrayList, R.layout.pic_photo_listview_item);
	    mListView.setAdapter(mAdapter);
	    //设置监听
	    rlAddPic.setOnClickListener(this);
	    mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicPhotoAdapter.ViewHolderPicPhoto mViewHolder=(PicPhotoAdapter.ViewHolderPicPhoto)view.getTag();
			final HashMap<String, String> mMap=mViewHolder.mMapPicPhoto;
			mDialog=new Dialog(mContext);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setContentView(R.layout.pic_upload_dilog);
			mDialog.show();
			Button mButLook=(Button)mDialog.findViewById(R.id.butPicLook);
			Button mButUpload=(Button)mDialog.findViewById(R.id.butPicUpload);
			
			mButLook.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mContext,PicPhotoLevelAct.class);
					intent.putExtras(mBundle);
					startActivityForResult(intent, PHOHT);
					mDialog.cancel();
				}
			});
			
			//上传照片
			mButUpload.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mDialog!=null){
						mDialog.cancel();
					}
					//提示上传进度框
					mProgressBar=new Dialog(mContext,R.style.dialog);
					mProgressBar.setCanceledOnTouchOutside(false);
					mProgressBar.setContentView(R.layout.common_normal_progressbar);
					mProgressBar.show();
					mArrayListPicUpload.clear();
					mArrayListPicUpload=new DBService(mContext).getPicUpload(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), 
						mNewId,mMap.get("picDate"));
					 if(mArrayListPicUpload.size()>0){
						for(int i=0;i<mArrayListPicUpload.size();i++){
							HashMap<String, String> mMap=mArrayListPicUpload.get(i);
							mPicLoadFlag++;
							Log.i("test", "i="+mPicLoadFlag);
							ftpUpload(mMap);
						}
					}else{
						mProgressBar.cancel();
						Toast.makeText(mContext,"不能重复上传该照片", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
	}
	
	/**数据更新*/
	public void refersh(){
		if(mArrayList!=null){
			mArrayList.clear();
			mArrayListSQL.clear();
			mArrayListSQL=new DBService(mContext).queryPic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId);
			mArrayList.addAll(mArrayListSQL);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/** 
	 * 通过ftp上传文件 
	 * @param url ftp服务器地址  
	 * @param port 端口如 ：
	 * @param username  登录名 
	 * @param password   密码 
	 * @param remotePath  上到ftp服务器的磁盘路径 
	 * @param fileNamePath  要上传的文件路径 
	 * @param fileName      要上传的文件名 
	 * @return 
	 */  
	public void ftpUpload(final HashMap<String, String> map) {  
		
		executorService.execute(new Runnable() {	
		 FTPClient ftpClient = new FTPClient();  
		 FileInputStream fis = null;  
		 //文件信息
		 StringBuffer mData=new StringBuffer();
		@Override
		public void run() {
			 try {  
				 mData.append("{");
				 //拍照人
				 mData.append("\"PhotographPeople\"");
				 mData.append(":");
				 mData.append("\"\"");
				 mData.append(",");
				 //拍照时间
				 mData.append("\"PhotographTime\"");
				 mData.append(":");
				 mData.append("\""+map.get("picDate")+"\"");
				 mData.append(",");
				 //newId
				 mData.append("\"ObjectId\"");
				 mData.append(":");
				 mData.append("\""+mNewId+"\"");
				 mData.append(",");
				 //上传时间
				 mData.append("\"UploadTime\"");
				 mData.append(":");
				 mData.append("\""+DateTools.getDateTime()+"\"");
				 mData.append(",");
				 //上传人
				 mData.append("\"UploadPeople\"");
				 mData.append(":");
				 mData.append("\"\"");
				 mData.append(",");
				 // 备注
				 mData.append("\"Remark\"");
				 mData.append(":");
				 mData.append("\"\"");
				 mData.append(",");
				 //照片信息
				 mData.append("\"ImageInfo\"");
				 mData.append(":");
				 mData.append("[");
				 mData.append("{");
				 mData.append("\"Sort\"");
				 mData.append(":");
				 mData.append("0");
				 mData.append(",");
				 // 照片路径
				 mData.append("\"PhysicalUrl\"");
				 mData.append(":"); 
				 mData.append("\""+(map.get("picRename")+".jpg")+"\"");
				 mData.append(",");
				 /*
				  * "PhotoType": "图片类型",
                   "PhotoName": "图片名称",
            		"PhotoFormat": "图片格式"
				  */
				 mData.append("\"PhotoType\"");
				 mData.append(":");
				 mData.append("\""+mType+"\"");
//				 if(mType==0){
//					 mData.append("\"原地貌\"");
//				 }else if(mType==1){
//					 mData.append("\"强夯处理\"");
//				 }else if(mType==2){
//					 mData.append("\"盲沟修筑\"");
//				 }else{
//					 mData.append("\"定点拍照\"");
//				 }
				 mData.append(",");
				 //图片名称
				 mData.append("\"PhotoName\"");
				 mData.append(":");
				 mData.append("\""+JsonTools.getURLEncoderString(map.get("picName"))+"\"");
				 mData.append(",");
				 mData.append("\"PhotoFormat\"");
				 mData.append(":");
				 mData.append("\".jpg\"");
				 mData.append(",");
				 //照片大小
				 mData.append("\"PhotoSize\"");
				 mData.append(":");
				 mData.append("\""+new File(map.get("picPath")).length()+"\"");
				 mData.append("}");
				 mData.append("]");
				 mData.append("}");
				
				 ftpClient.connect(getUrl());
			     boolean loginResult = ftpClient.login(mUser, mPwd);  
			     int returnCode = ftpClient.getReplyCode();  
			     if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功  
			         ftpClient.makeDirectory("/");  
			         // 设置上传目录  
			         ftpClient.changeWorkingDirectory("");  
			         ftpClient.setBufferSize(1024);  
			         ftpClient.enterLocalPassiveMode();  
			         ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
			         ftpClient.setControlEncoding("UTF-8");  
			         fis = new FileInputStream(new File(map.get("picPath")));  
			        boolean  mUploadResult=ftpClient.storeFile((map.get("picRename")+".jpg"), fis);    
			        if(mUploadResult){			        	
			        	HashMap<String, String> map=new HashMap<String, String>();
			        	map.put("method", Constants.METHOD_OF_ADDPTHOTO);
			        	map.put("key", Constants.PUBLIC_KEY);
			        	map.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
			        	String data=mData.toString();
			        	data = data.replaceAll(" ", "%20");
			        	map.put("data", data);
			        	String mStream=null;
			        	mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PHOTO, map, mContext));
			        	if(mStream!=null){			        		
			        		if(JsonTools.getCommonResult(mStream)){
			        			//上传成功
			        			sendFMessage(Constants.SUCCESS);  
			        		}else{
			        			//上传失败
			        			sendFMessage(Constants.COMMON_MESSAGE_2);
			        		}
			        	}else{
			        		sendFMessage(Constants.COMMON_MESSAGE_2);
			        	}
			        }else{
			        	sendFMessage(Constants.COMMON_MESSAGE_2);
			        }

			         
			     } else {
			    	 // 如果登录失败  
			         }  
			  
			 } catch (IOException e) {  
				 sendFMessage(Constants.ERROR);
			     e.printStackTrace();  
			 }
		}
	});
	}  

	@Override
	public void onClick(View v) {
		Intent intent=new Intent(mContext,PicPhotoLevelAct.class);
		intent.putExtras(mBundle);
		startActivityForResult(intent, PHOHT);
	}	
	
	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch(flag){
		case Constants.SUCCESS:
			cancelDialog();
			if(mPicLoadFlag==mArrayListPicUpload.size()){
				new DBService(mContext).updatePicState(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId, mArrayListPicUpload);
				refersh();
				mPicLoadFlag=0;
				mArrayListPicUpload.clear();
			}
			break;
		case Constants.ERROR:
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			mPicLoadFlag=0;
			cancelDialog();
			break;
		case  Constants.COMMON_MESSAGE_2:
			//照片上传失败
			Toast.makeText(mContext, "照片上传失败", Toast.LENGTH_SHORT).show();
			mPicLoadFlag=0;
			cancelDialog();
			break;
		}
	}
	/**
	 * 关闭对话框
	 */
	public void  cancelDialog(){
		if(mProgressBar!=null){			
			mProgressBar.cancel();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==PHOHT){
			refersh();
		}
	}
	/** 获取URL*/
	public String  getUrl(){
		  mUrl=mSP.getString(Constants.PREFERENCES_INFO_ROUTE, mUrl);
		  return mUrl.substring(0, mUrl.indexOf(":"));
	}
	@Override
	protected void onStop() {
		super.onStop();

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//关闭线程池
		executorService.shutdown();
	}
}
