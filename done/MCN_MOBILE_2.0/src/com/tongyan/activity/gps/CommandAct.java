package com.tongyan.activity.gps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kobjects.base64.Base64;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsCommandAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.utils.WebServiceUtils;

/**
 * 监理指令
 * @Title: CommandAct.java
 * @author Rubert
 * @date 2014-11-24 下午01:44:41
 * @version V1.0
 * @Description:
 */

public class CommandAct extends AbstructCommonActivity implements
		OnClickListener, OnItemClickListener {

	private Context mContext = this;
	private Button mBtnAddCommand; // 新增监理指令
	private ListView mListViewCommand; // 指令列表
	private Bundle mBundle;
	private ArrayList<HashMap<String, String>> mArrayListCommand = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> mArrayListCommandData;
	private LinkedList<HashMap<String, String>> mLinkeListFile=new LinkedList<HashMap<String,String>>();
	private LinkedList<HashMap<String, String>> mLinkeListImage=new LinkedList<HashMap<String,String>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMapData;
	private GpsCommandAdapter mAdapter;
	private _User mUser;
	private MyApplication mApplication;
	private ExecutorService  mExecutorService = Executors.newFixedThreadPool(2);// 2N
	private ExecutorService mExecutorServiceImg = Executors.newFixedThreadPool(2);// 2N
	private String mCommandId;
	private Dialog mDialog;
	private int mUploadFilePosition; // 上传附件position
	private int mUploadImgPosition; // 上传图片position
	public static final String  FILE_OF_TYPE="0"; //附件
	public static final String  IMAGE_OF_TYPE="1"; //图片
	private  ExpandableListView expandableListViewApprovePerson;
	private  Dialog mDialogCommandPerson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_command);
		mApplication = (MyApplication) getApplication();
		mApplication.addActivity(this);
		mUser = mApplication.localUser;
		if (getIntent().getExtras() != null) {
			mBundle = getIntent().getExtras().getBundle("commandBundle");
		}
		initWidget();
		initCommandData();
		mAdapter = new GpsCommandAdapter(mContext, mArrayListCommand,R.layout.gps_command_item);
		mListViewCommand.setAdapter(mAdapter);
	}

	/** 控件初始化 */
	public void initWidget() {
		mBtnAddCommand = (Button) findViewById(R.id.btnAddCommand);
		mListViewCommand = (ListView) findViewById(R.id.listViewCommand);
		initListener();
	}

	/** 监听事件初始化 */
	public void initListener() {
		mBtnAddCommand.setOnClickListener(this);
		mListViewCommand.setOnItemClickListener(this);
	}

	public void initCommandData() {
		if (mArrayListCommand.size() > 0) {
			mArrayListCommand.clear();
		}
		mArrayListCommand = new DBService(mContext).queryCommandInfo(mBundle.getString("newId"), mBundle.getString("commandType"));
	}
	

	/** 重新从数据库查询指令数据,并且更新适配器 */
	public void updateCommandData() {
		if (mArrayListCommand.size() > 0) {
			mArrayListCommand.clear();
		}
		if (mArrayListCommandData != null) {
			mArrayListCommandData.clear();
		}
		mArrayListCommandData = new DBService(mContext).queryCommandInfo(mBundle.getString("newId"), mBundle.getString("commandType"));
		mArrayListCommand.addAll(mArrayListCommandData);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GpsCommandAdapter.ViewHolder viewHolder = (GpsCommandAdapter.ViewHolder) view
				.getTag();
		final HashMap<String, String> map = viewHolder.mapCommand;
		mCommandId = map.get("commandId");
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.gps_command_dialog);
		mDialog.show();
		final Button btnCommandUpload = (Button) mDialog.findViewById(R.id.btnCommandUpload); // 指令上传
		final Button btnCommandProcedureStart = (Button) mDialog.findViewById(R.id.btnCommandProcedureStart); // 流程启动
		final Button btnCommandLook = (Button) mDialog.findViewById(R.id.btnCommandLook);// 指令内容查看
		final Button btnCommandDelete = (Button) mDialog.findViewById(R.id.btnCommandDelete); // 指令删除
		// 首先从数据库查看这条数据是否上传,如果上传修改相应按钮的值
		if (new DBService(mContext).queryCommandIsStart(map
				.get("commandId"))) {
			btnCommandLook.setText("查看");
	   	    mBundle.putString("isStart", "1"); //流程已经启动
		} else {
			btnCommandLook.setText("编辑");
			mBundle.putString("isStart", "0");
		}
		//上传
		btnCommandUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getFileLinkedList();
				getImageLinkedList();
				if(mDialog!=null){
					mDialog.cancel();
				}
				if(new DBService(mContext).queryCommandProcedure(map.get("commandId"))){
					Toast.makeText(mContext, "流程已经启动,不能再上传", Toast.LENGTH_SHORT).show();
					return;
				}else{
					baseShowDialog();
					uploadCommand(map.get("commandId"));
				}
			}
		});
		
		/**
		 * <ul> 1.判断指令流程是否启动,如果启动则不能再启动
		 * 			2. 启动流程,修改指令表中的sStart状态 0 未启动 1已启动
		 */ 
		btnCommandProcedureStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(new DBService(mContext).queryCommandIsStart(mCommandId)){
					Toast.makeText(mContext, "流程已经启动", Toast.LENGTH_SHORT).show();
				}else{
					//判断该指令是否已经上传
					if(new DBService(mContext).queryCommandUploadState(mCommandId)){
						if(mDialog!=null){
							mDialog.cancel();
						}
						baseShowDialog();
						getCommandStartPerson(mCommandId);  
					}else{
						Toast.makeText(mContext, "该指令未上传,应该先上传", Toast.LENGTH_SHORT).show();
					}
			 
				}
			}
		});

		btnCommandLook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mDialog!=null){
					mDialog.dismiss();
				}
				Intent intent = new Intent(mContext, CommandEditAct.class);
				mBundle.putString("commandId", mCommandId);
				mBundle.putString("isItem", "true");
				intent.putExtra("commandBundle", mBundle);
				startActivity(intent);
			}
		});

		// 指令删除
		btnCommandDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteCommand(mCommandId);
			}
		});
	}

	/** 获取流程启动的人员信息*/
	public void getCommandStartPerson(final String commandId){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
		   StringBuffer  strBuffer=new StringBuffer();
		   strBuffer.append("{");
		   strBuffer.append("\"rowId\"");
		   strBuffer.append(":");
		   strBuffer.append("\""+new DBService(mContext).queryCommandIdUUID(mCommandId)+"\"");
		   strBuffer.append("}");
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("publicKey", Constansts.PUBLIC_KEY);
			map.put("userName", mUser.getUsername());
		    map.put("Password", mUser.getPassword());
		    map.put("parms", strBuffer.toString().trim());
		    map.put("type", "监理指令审批流程");
		    String stream=null;
		    try {
		    	stream=WebServiceUtils.requestM(map, Constansts.START_OF_COMMAND, mContext);
		    	if(stream!=null){		    		
		    		mBaseMapData=new Str2Json().getCommandProcedurePerson(stream);
		    		if(mBaseMapData.size()>0){
		    			sendMessage(Constansts.MES_TYPE_8);
		    		}else{
		    		    sendMessage(Constansts.ERRER);
		    		}
		    	}else{
		    		sendMessage(Constansts.CONNECTION_TIMEOUT);
		    	}
			} catch (IOException e) {
				sendMessage(Constansts.CONNECTION_TIMEOUT);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			}
		}).start();
	}
	
	/**
	 * 删除监理指令
	 * <ul>
	 * 判断指令流程是否启动.如果已经启动就不能删除
	 * <ul>
	 * 判断指令是否已经上传,如果已经上传,则服务器和本地的数据都要删除,如果没有上传就删除本地数据
	 * */
	public void deleteCommand(final String commandId) {
//		if (new DBService(mContext).queryCommandProcedure(commandId)) {
//			Toast.makeText(mContext, "该流程已经启动,不能删除", Toast.LENGTH_SHORT).show();
//		} else {
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setMessage("确定删除吗?");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (new DBService(mContext).queryCommandUploadState(commandId)) {
								// 删除服务器数据和本地数据表
								if(mDialog!=null){
									mDialog.cancel();
								}
								baseShowDialog();
								deleteUploadCommand(commandId);
							} else {
								// 只删除本地数据
								new DBService(mContext).deleteCommand(commandId);
								new DBService(mContext).deleteCommandFile(commandId);
								Toast.makeText(mContext, "您已将该数据成功删除",Toast.LENGTH_SHORT).show();
								if (mDialog != null) {
									mDialog.cancel();
								}
								updateCommandData();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
		}
//	}

	/** 上传监理指令 */
	public void uploadCommand(final String commandId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				/*
				 * * parms{ guid:主键,
				 * Details:指令内容,,ProjectID:对应的工程编号Type:1项目指令2监理指令}
				 */
				StringBuffer parameters = new StringBuffer();
				ArrayList<HashMap<String, String>> arrayList = new DBService(mContext).queryCommandInfo(commandId);
				parameters.append("{");
				for (int i = 0; i < arrayList.size(); i++) {
					HashMap<String, String> map = arrayList.get(i);
					// 指令ID
					parameters.append("\"rowId\"");
					parameters.append(":");
					parameters.append("\"" + map.get("uuid") + "\"");
					parameters.append(",");
					// 指令内容
					parameters.append("\"Details\"");
					parameters.append(":");
					parameters.append("\"" + map.get("content") + "\"");
					parameters.append(",");
					// 工程编号
					parameters.append("\"ProjectID\"");
					parameters.append(":");
					parameters.append("\"" + map.get("newId") + "\"");
					parameters.append(",");
					// 项目指令
					parameters.append("\"Type\"");
					parameters.append(":");
					if ("0".equals(map.get("type"))) {
						parameters.append("\"2\"");
					} else {
						parameters.append("\"1\"");
					}
					if (i != arrayList.size() - 1) {
						parameters.append(",");
					}
				}
				parameters.append("}");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("publicKey", Constansts.PUBLIC_KEY);
				map.put("userName", mUser.getUsername());
				map.put("Password", mUser.getPassword());
				map.put("type", "SuperviorOrder");
				map.put("parms", parameters.toString().trim());
				String stream = null;
				try {
					stream = WebServiceUtils.requestM(map,Constansts.ADD_OF_COMMAND, mContext);
					if (stream != null) {
						if (new Str2Json().getCommandUploadResult(stream)) {
							sendMessage(Constansts.MES_TYPE_1);
						} else {
							sendMessage(Constansts.CONNECTION_TIMEOUT);
						}
					} else {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (IOException e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/** 从服务器删除上传的指令 */
	public void deleteUploadCommand(String commandId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append("{");
				strBuffer.append("\"rowId\"");
				strBuffer.append(":");
				strBuffer.append("\""+new DBService(mContext).queryCommandIdUUID(mCommandId) + "\"");
				strBuffer.append("}");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("publicKey", Constansts.PUBLIC_KEY);
				map.put("userName", mUser.getUsername());
				map.put("Password", mUser.getPassword());
				map.put("type", "SuperviorOrder");
				map.put("parms", strBuffer.toString().trim());
				String stream = null;
				try {
					stream = WebServiceUtils.requestM(map,Constansts.DELETE_OF_COMMAND, mContext);
					if (stream != null) {
						 if(new Str2Json().getCommandDeleteResult(stream)){
							 sendMessage(Constansts.MES_TYPE_5);
						 }else{
							 sendMessage(Constansts.MES_TYPE_6);
						 }
					} else {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (IOException e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
	/***
	 * 上传文件
	 * @param commandId  指令Id
	 * @param type="SuperviorOrderImage " 上传图片,
	 * type="SuperviorOrderAttackment"上传附件
	 */
	public void uploadCommandFile(final String commandId,final HashMap<String, String> map) {
		mExecutorService.execute(new Runnable() {
	//	new Thread(new Runnable(){	
		@Override
			public void run() {

				HashMap<String, String> parametersMap = new HashMap<String, String>();
				parametersMap.put("publicKey", Constansts.PUBLIC_KEY);
				parametersMap.put("userName", mUser.getUsername());
				parametersMap.put("password", mUser.getPassword());
				parametersMap.put("fileName", map.get("fileName"));
				parametersMap.put("objid",new DBService(mContext).queryCommandIdUUID(commandId));
				parametersMap.put("type", "SuperviorOrderAttackment");
				String stream = null;
//				FileInputStream fis = null;
				String uploadBuffer = null;
//				ByteArrayOutputStream ba = null;
				try {
					/*
					 * fis = new FileInputStream(map.get("filePath")); ba = new
					 * ByteArrayOutputStream(); byte[] b = new byte[1024]; int
					 * count = 0; while ((count = fis.read()) >= 0) {
					 * ba.write(b, 0, count); }
					 */
					uploadBuffer = new String(Base64.encode(new FileUtils().getBytes(map.get("filePath")))); // 进行Base64编码
					parametersMap.put("fileBytes", uploadBuffer);
					//DocumentFileUploadResponse{DocumentFileUploadResult={"s":"ok","v":"a8b1794f-bab7-4a89-9db3-224307206584"}; }
					stream = WebServiceUtils.requestM(parametersMap,Constansts.UPLOAD_OF_COMMAND, mContext);
					if(stream!=null){
							if(new Str2Json().getCommandUploadFileResult(stream)){							
									sendMessage(Constansts.MES_TYPE_2);;
							}else{
								 sendMessage(Constansts.MES_TYPE_3);
							}
					}else{
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} finally {
//					try {
//						if (fis != null)
//							fis.close();
//						if (ba != null)
//							ba.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
				}
			}
		});
	}

	
	/** 图片上传*/
	public void uploadCommandImage(final String commandId,final HashMap<String, String> map) {
		mExecutorServiceImg.execute(new Runnable() {
//		new Thread(new Runnable(){
			@Override
			public void run() {
				HashMap<String, String> parametersMap = new HashMap<String, String>();
				parametersMap.put("publicKey", Constansts.PUBLIC_KEY);
				parametersMap.put("userName", mUser.getUsername());
				parametersMap.put("password", mUser.getPassword());
				parametersMap.put("fileName", map.get("fileName"));
				parametersMap.put("objid",new DBService(mContext).queryCommandIdUUID(commandId));
				parametersMap.put("type", "SuperviorOrderImage");
				String stream = null;
//				FileInputStream fis = null;
				String uploadBuffer = null;
//				ByteArrayOutputStream ba = null;
				try {
					/*
					 * fis = new FileInputStream(m
					 * 
					 * ap.get("filePath")); ba = new
					 * ByteArrayOutputStream(); byte[] b = new byte[1024]; int
					 * count = 0; while ((count = fis.read()) >= 0) {
					 * ba.write(b, 0, count); }
					 */
//					uploadBuffer = new String(Base64.encode(new FileUtils().getBytes(map.get("filePath")))); // 进行Base64编码
					uploadBuffer = new String(bitmapToBase64(map.get("filePath"))); // 进行Base64编码					
					parametersMap.put("fileBytes", uploadBuffer);
					//DocumentFileUploadResponse{DocumentFileUploadResult={"s":"ok","v":"a8b1794f-bab7-4a89-9db3-224307206584"}; }
					stream = WebServiceUtils.requestM(parametersMap,Constansts.UPLOAD_OF_COMMAND, mContext);
					if (stream != null) {
						if (new Str2Json().getCommandUploadFileResult(stream)) {
							sendMessage(Constansts.MES_TYPE_4);
						} else {
							sendMessage(Constansts.MES_TYPE_7);
						}
					}else{
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} finally {
//					try {
//						if (fis != null)
//							fis.close();
//						if (ba != null)
//							ba.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
				}
			}
		});
	}
	/** 计算图片的缩放值*/
	public int calculateInSapleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		if(height>reqHeight || width>reqWidth){
			final int heightRatio=Math.round((float)height/(float)reqHeight);
			final int widthRatio=Math.round((float)width/(float)reqWidth);
			inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
		}
		return inSampleSize;
	}
	
	/**返回bitmap对象 */
	public Bitmap getBitmp(String filePath){
		BitmapFactory.Options  options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(filePath,options);
		options.inSampleSize=calculateInSapleSize(options, 400, 800);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/** */
	public String  bitmapToBase64(String filePath){
		Bitmap bitmap=getBitmp(filePath);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b=baos.toByteArray();
		return Base64.encode(b);
	}
	/** 获取未上传的附件集合*/
	public void getFileLinkedList(){
		if(mLinkeListFile.size()>0){
			mLinkeListFile.clear();
		}
		mLinkeListFile=new DBService(mContext).queryCommandFile(mCommandId, FILE_OF_TYPE);
	}
	
	/** 获取未上传的图片集合*/
	public void getImageLinkedList(){
		if(mLinkeListImage.size()>0){
			mLinkeListImage.clear();
		}
		mLinkeListImage=new DBService(mContext).queryCommandFile(mCommandId, IMAGE_OF_TYPE);
	}
	
	
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS: //监理指令流程启动成功,修改启动流程状态
			baseCloseDialog();
			new DBService(mContext).updateCommandProcedureState(mCommandId);
			updateCommandData();
			Toast.makeText(mContext, "流程启动成功", Toast.LENGTH_SHORT).show();
			
			break;
		case Constansts.MES_TYPE_1: // 指令上传成功,
			// 修改指令状态,并且上传附件
			new DBService(mContext).updateCommandUploadState(mCommandId, 1);
			if(mLinkeListFile.size()>0){			
					for (int i = 0; i < mLinkeListFile.size(); i++) {
						mUploadFilePosition = i; // 记录上传文件的position
						uploadCommandFile(mCommandId,mLinkeListFile.get(i));
					}
			}else{
		 		if(mLinkeListImage.size()>0){	
					for (int i = 0; i < mLinkeListImage.size(); i++) {
						mUploadImgPosition = i; // 记录上传图片的position
						uploadCommandImage(mCommandId,mLinkeListImage.get(i));
					}
		 		}else{
		 			baseCloseDialog();
		 			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
		 		}
			}
			break;
		case Constansts.MES_TYPE_2://上传附件成功修改文件的上传状态,然后上传图片
			 	if(mLinkeListFile.size()>0){
					HashMap<String, String> map=mLinkeListFile.get(mUploadFilePosition);
					new DBService(mContext).updateCommandFileUpload(map.get("commandId"), FILE_OF_TYPE, "1");
				}
			 	if(mLinkeListFile.size()-1==mUploadFilePosition){//上传图片
//			 		mExecutorService.shutdown();
			 		if(mLinkeListImage.size()>0){	
						for (int i = 0; i < mLinkeListImage.size(); i++) {
							mUploadImgPosition = i; // 记录上传图片的position
							uploadCommandImage(mCommandId,mLinkeListImage.get(i));
						}
			 		}else{
			 			baseCloseDialog();
			 			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			 		}
				}
			break;
		case Constansts.MES_TYPE_3: // 上传附件失败.//上传图片
//			mExecutorService.shutdown();
			if(mLinkeListFile.size()-1==mUploadFilePosition){ 
				if(mLinkeListImage.size()>0){					
					for (int i = 0; i < mLinkeListImage.size(); i++) {
						mUploadImgPosition = i; // 记录上传图片的position
						uploadCommandImage(mCommandId,mLinkeListImage.get(i));
					}
				}
			}else{
				baseCloseDialog();
				Toast.makeText(mContext, "上传附件失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case Constansts.MES_TYPE_4: // 上传图片成功,修改文件的上传状态
			if(mLinkeListImage.size()>0){
				HashMap<String, String> map=mLinkeListImage.get(mUploadImgPosition);
				new DBService(mContext).updateCommandFileUpload(map.get("commandId"), IMAGE_OF_TYPE, "1");
			}
			if(mLinkeListImage.size()-1==mUploadImgPosition){
//				mExecutorServiceImg.shutdown();
				baseCloseDialog();
				Toast.makeText(mContext, "文件上传成功", Toast.LENGTH_SHORT).show();
			}
			break;
		case Constansts.MES_TYPE_7:   //上传图片失败
//			mExecutorServiceImg.shutdown();
			baseCloseDialog();
			Toast.makeText(mContext, "文件上传失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_5:// 删除指令成功
			new DBService(mContext).deleteCommand(mCommandId);
			new DBService(mContext).deleteCommandFile(mCommandId);
			updateCommandData();
			baseCloseDialog();
			Toast.makeText(mContext, "您已将该数据成功删除", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_6: // 删除指令失败
			baseCloseDialog();
			Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_8: //流程启动人员获取
				baseCloseDialog();
				updateCommandPerson();
				showCommandPersonDialog();
			break;
		case Constansts.ERRER:
			baseCloseDialog();
			Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			baseCloseDialog();
			Toast.makeText(mContext, "访问超时,请检查网咯", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		super.handleOtherMessage(flag);
	}

	/**更新流程启动人员信息 */
	public void  updateCommandPerson(){
		if(mBaseMap.size()>0){
			mBaseMap.clear();
		}
		if(mBaseMapData!=null){
			if(mBaseMapData.size()>0){				
				mBaseMap.putAll(mBaseMapData);
			}
		}
		if(mBaseMapData!=null){
			mBaseMapData.clear();
		}
	}
	
	/** 判断用户是否勾选了人 如果没有勾选人不能审批 return false   otherwise true*/
	
	public boolean  isCheckPerson(){
	ArrayList<HashMap<String, String>>  arrayList=mBaseMap.get("arrayListApprovePersonInfo");
	for(int i=0; i<arrayList.size();i++){
		HashMap<String, String> map=arrayList.get(i);
		if("1".equals(map.get("checkBoxState"))){
			return true;
		}
		if(i==arrayList.size()-1 && "0".equals(map.get("checkBoxState"))){
			return false;
		}
	}
	return false;
	}
	
	/** 显示流程启动人员dialog*/
	public void  showCommandPersonDialog(){
	
		if(mDialogCommandPerson==null){						
			mDialogCommandPerson=new Dialog(mContext,R.style.dialog);
			mDialogCommandPerson.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogCommandPerson.setContentView(R.layout.measure_mid_record_approve_person_dialog);
			expandableListViewApprovePerson=(ExpandableListView)mDialogCommandPerson.findViewById(R.id.expandableListViewApprovePerson);
		}
		 expandableListViewApprovePerson.setAdapter(new MyExpandableListAdapter());
	 	 expandableListViewApprovePerson.expandGroup(0);
		 mDialogCommandPerson.show();
		 final  Button btnConfirm=(Button)mDialogCommandPerson.findViewById(R.id.btnApprovePersonConfirm);
		 btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCheckPerson()){
					if(mDialogCommandPerson!=null){
						mDialogCommandPerson.cancel();
					}
					baseShowDialog();
				    startCommandProcedure();

				}else{
					Toast.makeText(mContext, "您必须勾选人员", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	

	/** 启动流程*/
	public void startCommandProcedure(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StringBuffer strBuffer=new StringBuffer();
				strBuffer.append("{");
				//指令Id
				strBuffer.append("\"rowId\"");
				strBuffer.append(":");
				strBuffer.append("\""+new DBService(mContext).queryCommandIdUUID(mCommandId)+"\"");
				strBuffer.append(",");
				//
				strBuffer.append("\"command\"");
				strBuffer.append(":");
				strBuffer.append("\"start\"");
				strBuffer.append(",");
				//人员Id
				if(mBaseMap.get("arrayListApprovePersonInfo").size()>0){
					//	selectEmpId:当command为next时提供的用户选择的下步执行人id(以，号分隔的字符串)
					strBuffer.append("\"selectEmpId\"");
					strBuffer.append(":");
					strBuffer.append("\"");
					for(int j=0;j<mBaseMap.get("arrayListApprovePersonInfo").size();j++){
						if("1".equals(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("checkBoxState"))){							
							strBuffer.append(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("personId"));
							Log.i("test", "personId="+mBaseMap.get("arrayListApprovePersonInfo").get(j).get("personId"));
						}
						if(j!=mBaseMap.get("arrayListApprovePersonInfo").size()-1){
							strBuffer.append(",");
						}
					}
					strBuffer.append("\"");
					strBuffer.append("}");
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("publicKey", Constansts.PUBLIC_KEY);
					map.put("userName", mUser.getUsername());
					map.put("Password", mUser.getPassword());
					map.put("type", "监理指令审批流程");
					map.put("parms", strBuffer.toString().trim());
					String stream=null;
					try {
						stream=WebServiceUtils.requestM(map, Constansts.APPROVE_OF_COMMAND, mContext);
						if(stream!=null){
							if(new Str2Json().getApproveCommandResult(stream)){
								sendMessage(Constansts.SUCCESS);
							}else{
								sendMessage(Constansts.ERRER);
							}
						}else{
							sendMessage(Constansts.CONNECTION_TIMEOUT);
						}
					
					} catch (IOException e) {
						sendMessage(Constansts.CONNECTION_TIMEOUT);
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					}
			}
			}
		}).start();
	
}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddCommand:
			if(mDialog!=null){
				mDialog.dismiss();
			}
			Intent intent = new Intent(mContext, CommandEditAct.class);
			mBundle.putString("uploadState", "0");
			mBundle.putString("isItem", "false");
			mBundle.putString("isStart", "0");
			intent.putExtra("commandBundle", mBundle);
			startActivityForResult(intent, 0x0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0x101) {
			updateCommandData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	/** 部门信息和人员列表适配器*/
	private class  MyExpandableListAdapter extends  BaseExpandableListAdapter{

		public  ArrayList<HashMap<String, String>> mArrayListDept,mArrayListPerson;		
		private ChildViewHolder mChildViewHolder;
		private ParentViewHolder mParentViewHolder;
		private LayoutInflater mInflater;
		private int mChildIndex; //子选项开始的下标
		private MyExpandableListAdapter(){
			mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		private MyExpandableListAdapter(ArrayList<HashMap<String, String>> arrayListDept,ArrayList<HashMap<String, String>> arrayListPerson){
			this.mArrayListDept=arrayListDept;
			this.mArrayListPerson=arrayListPerson;
		}
		
		@Override
		public int getGroupCount() {
			return mBaseMap.get("approveDeptInfo").size();
		}

		/** 得到每个部门的人数*/
		public int getPersonCount(int groupPosition){
			int size=0;
			if(mBaseMap!=null){				
				size=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(groupPosition).get("count"));
			}
			return size;
		}
		
		/** 记录子选项的开始下标*/
		public int  getChildIndex(int gropPosition){
			int size=0;
			if(mBaseMap!=null){
				if(gropPosition==0){
//					 size=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(gropPosition).get("count"));
					 return 0;
				}else{					
					for(int i=0;i<gropPosition;i++){
						size+=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(i).get("count"));
					}
				}
			}
			return size;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			
			//BaseMap.get("arrayListApprovePersonInfo").size()
			return getPersonCount(groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			
			return mBaseMap.get("approveDeptInfo").get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			return childPosition;
		}

		@Override
		public long getGroupId(int groupPosition) {
			
			return groupPosition ;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			   if(convertView==null){
				   mParentViewHolder=new ParentViewHolder();
				  convertView= mInflater.inflate(R.layout.measure_mid_record_approve_person_listview_parent_item, null);
				  mParentViewHolder.mTextViewDeptName=(TextView)convertView.findViewById(R.id.textViewParentDeptName);
				  convertView.setTag(mParentViewHolder);
			   }else{
				   mParentViewHolder=(ParentViewHolder)convertView.getTag();
			   }
			     HashMap<String, String> map=mBaseMap.get("approveDeptInfo").get(groupPosition);
			     if(map!=null && map.size()>0){
			    	 mParentViewHolder.mTextViewDeptName.setText(map.get("deptName"));
			     }
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
				if(convertView==null){					
					mChildViewHolder=new ChildViewHolder();
					convertView=mInflater.inflate(R.layout.measure_mid_record_approve_person_listview_child_item, null);
					mChildViewHolder.mTextViewDeptPersonName=(TextView)convertView.findViewById(R.id.textViewChildDeptPersonName);
					mChildViewHolder.mCheckBoxDeptPersonName=(CheckBox)convertView.findViewById(R.id.checkBoxChildPersonName);
					convertView.setTag(mChildViewHolder);
				}else{
					mChildViewHolder=(ChildViewHolder)convertView.getTag();
				}
					HashMap<String, String> map=mBaseMap.get("arrayListApprovePersonInfo").get(mChildIndex++);
					if("1".equals(map.get("checkBoxState"))){
						mChildViewHolder.mCheckBoxDeptPersonName.setChecked(true);
					}else{
						mChildViewHolder.mCheckBoxDeptPersonName.setChecked(false);
					}
					if(map!=null && map.size()>0){
						mChildViewHolder.mTextViewDeptPersonName.setText(map.get("personName"));
				}
				mChildViewHolder.mCheckBoxDeptPersonName.setOnClickListener(new  CheckBoxListener(mChildIndex-1,mChildViewHolder.mCheckBoxDeptPersonName));
		
				return convertView;
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			super.onGroupCollapsed(groupPosition);
		}
		
		@Override
		public void onGroupExpanded(int groupPosition) {
		     for(int i=0;i<expandableListViewApprovePerson.getCount();i++){
		    		if(i!=groupPosition && expandableListViewApprovePerson.isGroupExpanded(groupPosition)){
		    			expandableListViewApprovePerson.collapseGroup(i);
		    		}
		     }
		     //每展开一下都会重新获取子选项的下标,来获取map的下标
		     mChildIndex=getChildIndex(groupPosition);
			super.onGroupExpanded(groupPosition);
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return false;
		}	
		
		private  class CheckBoxListener implements View.OnClickListener{
			
			private int mPosition;
			private CheckBox mCheckBox;
			public CheckBoxListener(int position,CheckBox checkBox){
				this.mPosition=position;
				this.mCheckBox=checkBox;
			}
			@Override
			public void onClick(View v) {
					for(int i=0;i<mBaseMap.get("arrayListApprovePersonInfo").size();i++){
						if(i==mPosition){
							HashMap<String, String> map=mBaseMap.get("arrayListApprovePersonInfo").get(i);
							if(mCheckBox.isChecked()){
								map.put("checkBoxState", "1");
							}else{
								map.put("checkBoxState", "0");
							}
			}
		}
			}	
		}

	public  class  ChildViewHolder{
		private TextView mTextViewDeptPersonName;
		private CheckBox mCheckBoxDeptPersonName;
	}
	public  class  ParentViewHolder{
		private TextView mTextViewDeptName;
	}
 }
	@Override
	protected void onDestroy() {
		if(!mExecutorService.isShutdown()){
			mExecutorService.shutdown();
		}
		if(!mExecutorServiceImg.isShutdown()){
			mExecutorServiceImg.shutdownNow();
		}
		super.onDestroy();
	}
}
