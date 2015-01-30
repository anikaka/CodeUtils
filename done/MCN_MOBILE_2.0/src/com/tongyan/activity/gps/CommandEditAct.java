package com.tongyan.activity.gps;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsCommandFileAdapter;
import com.tongyan.activity.adapter.GpsCommandImageAdapter;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.DateTools;
import com.tongyan.utils.FileOpen;

/**
 * 指令编辑
 * @author ChenLang
 *
 */
public class CommandEditAct  extends Activity implements OnClickListener,OnItemClickListener,OnItemLongClickListener{

	private Context mContext=this;
	private EditText mTxtMonitorContent; //监理指令
	private ListView  mListViewFile;					    //附件
	private ImageButton mImageButtonAddFile;    //添加附件
	private Button mBtnSaveCommandInfo;      	 //保存指令信息
	private  GridView mGridViewImg; 				     //网格照片视图
	private  GpsCommandImageAdapter  mImageAdapter;
	private  GpsCommandFileAdapter     mFileApdater;
	public  static LinkedList<HashMap<String, String>> mLinkedListImg=new LinkedList<HashMap<String,String>>(); //图片储存容器
	private static LinkedList<HashMap<String, String>>   mLinkedListFile=new LinkedList<HashMap<String,String>>(); //附件存储容器
	public static final int  IMG_CAPTURE=0x101;
	public static final int   FILE_FLAG=0x102;
	private  String mFileName;
	private  String mDir=Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH;
	private  Bundle mBundle;
	private  MyApplication mMyApplication;
	private _User mUser;
	private  String mCommandId;
	private String mIsItem;//判断是否是ListView点击的子选择 true 是 otherwise return  false
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_command_edit);
		initWidget();
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getBundleExtra("commandBundle");
			mCommandId=mBundle.getString("commandId");
			mIsItem=mBundle.getString("isItem");
			setSaveBtnVisibility();
		}
		mMyApplication=(MyApplication)getApplication();
		mMyApplication.addActivity(this);
		mUser=mMyApplication.localUser;
		//添加适配器
		initCommandData();
		mImageAdapter=new GpsCommandImageAdapter(mContext, mLinkedListImg, R.layout.gps_command_edit_gridview_item);
		mFileApdater=new GpsCommandFileAdapter(mContext, mLinkedListFile, R.layout.gps_command_edit_listview_file_item);
		mGridViewImg.setAdapter(mImageAdapter);
		mListViewFile.setAdapter(mFileApdater);
	}
	
	/** 组件初始化*/
	private void initWidget(){
		mTxtMonitorContent=(EditText)findViewById(R.id.txtMonitorContent);
		mListViewFile=(ListView)findViewById(R.id.listViewFile);
		mImageButtonAddFile=(ImageButton)findViewById(R.id.imageButtonAddFile);
		mGridViewImg=(GridView)findViewById(R.id.gridViewMonitorCommandImg);
		mBtnSaveCommandInfo=(Button)findViewById(R.id.btnSaveCommandInfo);
		mBtnSaveCommandInfo.setText("保  存");
		initListener();
	}

	/** 监听事件初始化*/
	public void initListener(){
		mImageButtonAddFile.setOnClickListener(this);
		mBtnSaveCommandInfo.setOnClickListener(this);
		mGridViewImg.setOnItemClickListener(this);
		mGridViewImg.setOnItemLongClickListener(this);
		mListViewFile.setOnItemClickListener(onItemClickFile);
		mListViewFile.setOnItemLongClickListener(onItemLongClickFile);
	}
	
	/** 指令数据初始化*/
	public void initCommandData(){
	//	查询指令内容
		if("true".equals(mIsItem)){
			ArrayList<HashMap<String, String>> arrayList=new DBService(mContext).queryCommandInfo(mCommandId);
			if(arrayList!=null && arrayList.size()>0){
				mTxtMonitorContent.setText(arrayList.get(0).get("content"));
				mLinkedListFile=new DBService(mContext).queryCommandFile(mCommandId, CommandAct.FILE_OF_TYPE);
				mLinkedListImg=new DBService(mContext).queryCommandFile(mCommandId, CommandAct.IMAGE_OF_TYPE);
			}
		}else{
			mTxtMonitorContent.setText("");
			if(mLinkedListFile.size()>0){
				mLinkedListFile.clear();
			}
			if(mLinkedListImg.size()>0){
				mLinkedListImg.clear();
			}
		}
		addDefaultImg();
	}

	/** 设置保存按钮的可视 ,如果该指令已经启动 隐藏按钮,otherwise*/
	public void setSaveBtnVisibility(){
		if("1".equals(mBundle.getString("isStart"))){
			mBtnSaveCommandInfo.setVisibility(View.INVISIBLE);
		}else{
			mBtnSaveCommandInfo.setVisibility(View.VISIBLE);
		}
	}

	/**默认新增一张拍照照片 */
	public void addDefaultImg(){
		//如果流程已经启动,就跳出方法
		if("1".equals(mBundle.getString("isStart"))){
			return ;
		}
		if(mLinkedListImg.size()==0){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("fileName", "");
			map.put("type", String.valueOf(true));
			map.put("filePath", "");
			map.put("fileType", "1");
			mLinkedListImg.addLast(map);
			return;
		}
		for(int i=0;i<mLinkedListImg.size();i++){
			HashMap<String, String> map=mLinkedListImg.get(i);
			if("true".equals(map.get("type"))){
				return;
			}
			if("false".equals(map.get("type")) && i==mLinkedListImg.size()-1){
				HashMap<String, String> mapImg=new HashMap<String, String>();
				mapImg.put("fileName", "");
				mapImg.put("type", String.valueOf(true));
				mapImg.put("filePath", "");
				mapImg.put("fileType", "1");
				mLinkedListImg.addLast(mapImg);
			}
		}
	}

	
	/** 更新照片*/
	public void  updateImg(){
		if(mLinkedListImg.size()>0){
			mLinkedListImg.clear();
		}
		if(mCommandId!=null){			
			mLinkedListImg=new DBService(mContext).queryCommandFile(mCommandId,"1");
		}
		addDefaultImg();
	}
	
	/** 更新文件*/
	public void updateFile(){
		if(mLinkedListFile.size()>0){
			mLinkedListFile.clear();
		}
		if(mCommandId!=null){
			mLinkedListFile=new DBService(mContext).queryCommandFile(mCommandId, "0");
		}
	}
	

	@Override
	public void onClick(View v) {
		 
	switch (v.getId()) {
	
	case R.id.btnSaveCommandInfo: //保存指令信息
			 if("".equals(mTxtMonitorContent.getText().toString())){
				 Toast.makeText(mContext,"指令信息必须填写", Toast.LENGTH_SHORT).show();
			 }else{
				 addCommandInfo(mBundle.getString("commandType"));
			 }
		break;
	case R.id.imageButtonAddFile:  //添加除照片以外的文件,调用系统文件浏览器返回路径
		    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
		    intent.setType("*/*");  
		    intent.addCategory(Intent.CATEGORY_OPENABLE);  
		    startActivityForResult(intent,FILE_FLAG);
		break;
	default:
		break;
		}
	}
	
	/** 附件点击事件 打开附件 支持.
	 * jpg 
	 * .doc
	 * .PDF
	 * .xls*/
	OnItemClickListener onItemClickFile=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			GpsCommandFileAdapter.ViewHoloder  viewHolder=(GpsCommandFileAdapter.ViewHoloder)view.getTag();
			HashMap<String, String> map=viewHolder.mapFile;
			if(map!=null){
				if(new File(map.get("filePath")).exists()){
					 //查看文件
					// 截取文件后缀名
					String fileName=map.get("fileName");
					String  fileFormatSuffix=fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
					if("doc".equalsIgnoreCase(fileFormatSuffix)){
						startActivity(FileOpen.getWordFileIntent(new File(map.get("filePath"))));
					}else if("xls".equalsIgnoreCase(fileFormatSuffix)){
						startActivity(FileOpen.getExcelFileIntent(new File(map.get("filePath"))));
					}else if("pdf".equalsIgnoreCase(fileFormatSuffix)){
						startActivity(FileOpen.getPdfFileIntent(new File(map.get("filePath"))));
					}else if("txt".equalsIgnoreCase(fileFormatSuffix)){
						startActivity(FileOpen.getTextFileIntent(map.get("filePath"),Boolean.FALSE));
					}else if("ppt".equals(fileFormatSuffix)){
						startActivity(FileOpen.getPdfFileIntent(new File(map.get("filePath"))));
					}else{
						Toast.makeText(mContext, "不支持此类型文件",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	/** 附件长按事件*/
	OnItemLongClickListener onItemLongClickFile=new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			 GpsCommandFileAdapter.ViewHoloder  viewHolder=(GpsCommandFileAdapter.ViewHoloder)view.getTag();
			  HashMap<String, String>  map=viewHolder.mapFile;
			  if(map!=null){
//				boolean result=new DBService(mContext).deleteCommandFile(map.get("fileId"),"0");
//				if(result){
//					updateFile();
//					mFileApdater.notifyDataSetChanged();
//				}else{
					mLinkedListFile.remove(position);
					mFileApdater.notifyDataSetChanged();
//				}
			  }
			return false;
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GpsCommandImageAdapter.ViewHolder    viewHolder=(GpsCommandImageAdapter.ViewHolder)view.getTag();
		 HashMap<String, String> map=viewHolder.mapImg;
		 	if("true".equals(map.get("type"))){
		 		mFileName=String.valueOf(System.currentTimeMillis())+".jpg";
		 		if(!new File(mDir).exists()){
		 			new File(mDir).mkdir();
		 		}
		 		File  fileImg=new File(mDir,mFileName);
		 		if(!fileImg.exists()){
		 			try {
		 				fileImg.createNewFile();
					} catch (IOException e) {
						e.printStackTrace(); 
					}
		 		}		
		 		Uri  uri=Uri.fromFile(fileImg);
		 		Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		 		startActivityForResult(intent, IMG_CAPTURE);
		 	}else{
		 		//查看单张图片
		 		if(new File(map.get("filePath")).exists()){
		 			startActivity(FileOpen.getImageFileIntent(new File(map.get("filePath"))));
		 		}else{
		 			Toast.makeText(mContext, "照片不存在", Toast.LENGTH_SHORT).show();
		 		}
		 	}
	}
	
	/** 长按照片删除*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
				GpsCommandImageAdapter.ViewHolder  viewHolder =(GpsCommandImageAdapter.ViewHolder)view.getTag();
				HashMap<String, String> map=	viewHolder.mapImg;
				if(map!=null){
					if("false".equals(map.get("type"))){			
//						boolean  result=new DBService(mContext).deleteCommandFile(map.get("fileId"),"1");
//						if(result){							
//							updateImg();
//							mImageAdapter.notifyDataSetChanged();
//						}else{
							mLinkedListImg.remove(position);
							mImageAdapter.notifyDataSetChanged();
//						}
					}
				}
				
		return false;
	}
	
	/**添加指令信息到数据库 表LOCAL_COMMAND中*/
	public void addCommandInfo(String type){
		//如果是点击的子选项就修改信息,否则重新插入
		if("true".equals(mIsItem)){
			new DBService(mContext).updateCommandInfo(mCommandId,mBundle.getString("sectionId"), mBundle.getString("newId"),
					mUser.getUserid(), mBundle.getString("commandType"), mTxtMonitorContent.getText().toString(),DateTools.getDate());
			new DBService(mContext).deleteCommandFile(mCommandId);
			addFile();
		}else{			
			//添加指令信息
			boolean result=new DBService(mContext).insertCommandInfo(getUUID(),mBundle.getString("sectionId"), mBundle.getString("newId"),
					mUser.getUserid(), mBundle.getString("commandType"), mTxtMonitorContent.getText().toString(),DateTools.getDate());
			//添加文件
			addFile();
		}
		Intent intent=new Intent(mContext, CommandAct.class);
		setResult(0x101, intent);
		this.finish();
	}
	
	/** 生成UUID*/
	@SuppressLint("DefaultLocale")
	public  String getUUID(){
		return  UUID.randomUUID().toString().toUpperCase();
		
	}
	
	/** 添加文件到数据库 */
	public void  addFile(){
		if("false".equals(mIsItem)){			
			mCommandId=new DBService(mContext).queryLastCommandId();
		}
		for(HashMap<String, String> map: mLinkedListFile){
			new DBService(mContext).insertCommandFile(mCommandId, "0",map.get("fileName"),map.get("filePath"), "0");
		}
		for(HashMap<String, String> map:mLinkedListImg){
			if(!"".equals(map.get("fileName")) && "false".equals(map.get("type")) ){				
				new DBService(mContext).insertCommandFile(mCommandId, "1", map.get("fileName"),map.get("filePath"), "0");
			}
		}
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_CANCELED){
			//如果取消保存照片就把本地照片删除
			if(new File(mDir+mFileName).exists()){
				new File(mDir+mFileName).delete();
			}
			return;
		}
		if(requestCode==FILE_FLAG){	
			//添加文件
			Uri uri =data.getData();
			HashMap<String, String> map=new HashMap<String, String>();
			if(uri.getScheme().toString().compareTo("content")==0){				
				map.put("filePath", getUriFilePath(uri));
				map.put("fileName",getUriFilePath(uri).substring(getUriFilePath(uri).lastIndexOf("/")+1,getUriFilePath(uri).length()));
			}else{				
				map.put("filePath", uri.getPath());
				map.put("fileName", uri.getPath().substring(uri.getPath().lastIndexOf("/")+1,uri.getPath().length()));
			}
			map.put("fileType", "0");  //fileType 文件类型 0  是附件 1表示图片
			mLinkedListFile.add(map);
			mFileApdater.notifyDataSetChanged();
		}
		if(requestCode==IMG_CAPTURE){
			//添加照片
			HashMap<String, String> map=new HashMap<String	, String>();
			map.put("fileName", mFileName);
			map.put("type", String.valueOf(false));
			map.put("fileType", "1");
			map.put("filePath", Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH+mFileName);
			mLinkedListImg.addFirst(map);
			mImageAdapter.notifyDataSetChanged();
		}
	}
	

	/** 获取媒体文件的路径*/
	public String getUriFilePath(Uri uri){
		Cursor c=mContext.getContentResolver().query(uri, null, null, null, null);
		if(c.moveToFirst()){
			 int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);  
            return c.getString(columnIndex); // 取出文件路径  
		}
		return "";
	}

 
	
	/** 照片回收*/
	@Override
	protected void onDestroy() {
		if(mImageAdapter.getCache()!=null && mImageAdapter.getCache().size()>0){
			HashMap<String, SoftReference<Bitmap>> map=mImageAdapter.getCache();
			Iterator  iterator=map.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry entry=(Map.Entry)iterator.next();
				SoftReference<Bitmap> sr=(SoftReference<Bitmap>)entry.getValue();
				if(sr.get()!=null){
					sr.get().recycle();
					sr.clear();
					System.gc();
				}
			}
		}
		super.onDestroy();
	}


}
