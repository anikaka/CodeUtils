package com.tongyan.activity.measure.command;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.CommandFileAdapter;
import com.tongyan.activity.adapter.CommandImageAdapter;
import com.tongyan.activity.adapter.MyTaskLookAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.fragment.CommandMenuFragment;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileOpen;
import com.tongyan.utils.ProcessOperation;
import com.tongyan.utils.WebServiceUtils;
	

/** 监理指令审批
 * @author ChenLang
 */

public class CommandApproveAct extends AbstructCommonActivity implements OnClickListener,OnItemClickListener{
	
   private Button   mBtnTaskStart,       //流程启动
							 mBtnTaskApprove,  //流程审批
							 mBtnTaskTracking,  	//流程跟踪
							 mBtnTaskLook;  		 //流程查看
	private EditText  mTxtMonitorContent;  //指令内容
	private ListView mListViewFile;  			  //附件
	private GridView mGridViewImage;       //照片
	private Context mContext=this;
	private  MyApplication mApplication;
	private HashMap<String, String> mMapAttribute=new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> mArrayListFile=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListImage=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, Bitmap>> mArrayListImageBitmap=new ArrayList<HashMap<String,Bitmap>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
	private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMapData;
	private ArrayList<HashMap<String, String>>    mArrayListTask=new ArrayList<HashMap<String,String>>(); //流程信息
	private ArrayList<HashMap<String, String>>    mArrayListTaskData;
	private CommandFileAdapter mAdapterFile;
	private CommandImageAdapter  mAdapterImage;
	private  int mDownUploadSize; //下载文件大小
	private int  mFileSize;//文件总大小
	private File mFile;
	private String mFileName;
	private String[] mUrl; //图片Url
	private _User mUser;
	private  Dialog mDialogCommandPerson,mDialogTaskTracking,mDialogTaskLook,mDialogApprovePerson,mDialogApproveSuggestion;
	private  ExpandableListView mExpandableListViewApprovePerson;
	private  MyExpandableListAdapter mExpandableListApdater;
	private  String mTaskTracking;
	private  MyTaskLookAdapter  mMyTaskLookAdapter;
	
	/**初始化widget*/
	 public void initWeiget(){
		  mBtnTaskStart=(Button)findViewById(R.id.btnCommandStart);
		  mBtnTaskApprove=(Button)findViewById(R.id.btnCommandApprove);
		  mBtnTaskTracking=(Button)findViewById(R.id.btnCommandTaskTracking);
		  mBtnTaskLook=(Button)findViewById(R.id.btnCommandTaskLook);
		  mTxtMonitorContent=(EditText)findViewById(R.id.txtCommandMonitorContent);
		  mListViewFile=(ListView)findViewById(R.id.listViewCommandFile);
		  mGridViewImage=(GridView)findViewById(R.id.gridViewCommandMonitorImg);
		  setListener();
	 }
	 
	 /** 设置监听事件*/
	 public void setListener(){
		 mBtnTaskStart.setOnClickListener(this);
		 mBtnTaskApprove.setOnClickListener(this);
		 mBtnTaskTracking.setOnClickListener(this);
		 mBtnTaskLook.setOnClickListener(this);
		 mListViewFile.setOnItemClickListener(onItemClickListenerFile);
		 mGridViewImage.setOnItemClickListener(this);
	 }
	
	 /** 初始化视图*/
	 public void initShowView(){
		 //	 flowStatus; // 流程状态 值为 0未申报,1待审批2已申报,3已审批,4全部
		 if(mMapAttribute!=null){
			  if("0".equals(mMapAttribute.get("flowStatus"))){
				  mBtnTaskStart.setVisibility(View.VISIBLE);
				  mBtnTaskTracking.setVisibility(View.GONE);
				  mBtnTaskLook.setVisibility(View.GONE);
			  }else if("1".equals(mMapAttribute.get("flowStatus"))){
					  mBtnTaskApprove.setVisibility(View.VISIBLE);
				  }
			 }
	 }
	 
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.measure_commmand_arrove);
		if(getIntent().getExtras()!=null){
			mMapAttribute=(HashMap<String, String>)getIntent().getExtras().getSerializable("mapAttribute");
		}
		mApplication=(MyApplication)getApplication();
		mUser=mApplication.localUser;
		initWeiget();
		initShowView();
		initFileData();
		initDownload();
		mAdapterFile=new CommandFileAdapter(mContext, mArrayListFile, R.layout.gps_command_edit_listview_file_item);
		mAdapterImage=new CommandImageAdapter(mContext, mArrayListImageBitmap, mUrl, R.layout.gps_command_edit_gridview_item);
		mMyTaskLookAdapter=new MyTaskLookAdapter(mContext, mArrayListTask, R.layout.measure_task_dialog_tasklook_listview_item);
		mListViewFile.setAdapter(mAdapterFile);
		mGridViewImage.setAdapter(mAdapterImage);
	}
	 
	 /** 初始化文件数据*/
	 public void initFileData(){
		 if(mMapAttribute!=null){
			HashMap<String, ArrayList<HashMap<String, String>>>baseMap= 
					CommandMenuFragment.baseArrayListCommand.get(Integer.parseInt(mMapAttribute.get("fileIndex")));
		if(mMapAttribute!=null){
			mTxtMonitorContent.setText(mMapAttribute.get("content"));
		}
		if(baseMap.get("commandFile").size()>0){
			mArrayListFile.addAll(baseMap.get("commandFile"));
		}
	
		if(baseMap.get("commandImage").size()>0){
			mArrayListImage.addAll(baseMap.get("commandImage"));
			mUrl=new String[mArrayListImage.size()];
			for(int i=0;i<mArrayListImage.size();i++){
				mUrl[i]=mArrayListImage.get(i).get("filePath");
				}
			}
		 }
	 }
	 
	 public void initDownload(){
		if(mArrayListImage.size()>0){
			for(int i=0;i<mArrayListImage.size();i++){
				ImageAsynTask img=new ImageAsynTask();
				img.execute(mArrayListImage.get(i).get("filePath"));
			}
		}
	 }
		 
		 
	 /**点击文件浏览
	  * 查看数据是否有这条数据,如果没有就重新下载 ,
	  * otherwise  查看文件
	  */
	OnItemClickListener    onItemClickListenerFile=new OnItemClickListener() {
	
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				   CommandFileAdapter.ViewHoloder viewHolder=(CommandFileAdapter.ViewHoloder)view.getTag();
				   HashMap<String, String> map=viewHolder.mapFile;
				   HashMap<String, String> mapData=new DBService(mContext).queryCommandFileExist(map.get("fileName"), "0"); //0表示类型 为附件 1为图片
			   if(mapData.size()>0){ 
				   File file=new File(mapData.get("filePath"));
				   if(file.exists()){
					    //打开文件
					   mFileName=mapData.get("fileName");
					   openFile(file, mFileName);
				   }else{
					   //如果文件不存在就下载,并且打开
					   baseShowDialog();
					   try {
							mFile=new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH+map.get("fileName"));
							if(!new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH).exists()){
								new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH).mkdirs();
							}
							mFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					   mFileName=map.get("fileName");
					   downloadFile(map.get("filePath"));
				   }
			   }else{
				   //下载文件并且打开
				   baseShowDialog();
				   try {
						mFile=new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH+map.get("fileName"));
						if(!new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH).exists()){
							new File(Environment.getExternalStorageDirectory().getPath()+Constansts.CN_CAMERA_COMMAND_PATH).mkdirs();
						}
						mFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				   mFileName=map.get("fileName");
				   downloadFile(map.get("filePath"));
				   }
		}
	};
	
	/** 文件下载*/
	public void  downloadFile(final String path){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
	//				URL  url=new URL("http://192.168.0.162:9000/Download/DownFile?filePath=UploadFiles/2014/12/09/33a521d47e264f47851c53e18bc331ae.xls");
					URL url=new URL(path);
					URLConnection urlConn=url.openConnection();
					urlConn.connect();
					InputStream fis=urlConn.getInputStream();
					mFileSize=urlConn.getContentLength();
					FileOutputStream  fos=new FileOutputStream(mFile);
				    byte[] bs=new byte[1024];
					int len=0;
					while((len=fis.read(bs))!=-1){
						fos.write(bs, 0, len);
						mDownUploadSize+=len;
					}
					fos.flush();
					fos.close();
					fis.close();
					sendMessage(Constansts.MES_TYPE_1);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				} catch (IOException e) {
					e.printStackTrace();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				}
			}
		}).start();
	}
	
	/**下载图片
	 * @throws IOException */
	public Bitmap downLoadImage(String fileUrl) throws IOException{
	 //   ByteArrayOutputStream bs = new ByteArrayOutputStream(1024);    
	    Bitmap bitmap=null;;
		URL  urlImage=new URL(fileUrl);
		URLConnection  conn=urlImage.openConnection();
		conn.connect();
		InputStream  is=conn.getInputStream();  //HttpDownloader.getInputStreamByHttpClient("");
		byte[] data=null;
		try {
			data = readStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	//			bitmap=BitmapFactory.decodeStream(is);
			bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
			is.close();
			return bitmap;
	}
	
	/** 
	 * 得到图片字节流 数组大小 
	 */  
	public byte[] readStream(InputStream inStream) throws Exception{        
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);        
	    byte[] buffer = new byte[1024];        
	    int len = 0;        
	    while( (len=inStream.read(buffer)) != -1){        
	        outStream.write(buffer, 0, len);        
	    }        
	    outStream.flush();
	    outStream.close();        
	    return outStream.toByteArray();            
	} 
	
	public class   ImageAsynTask extends  AsyncTask<String, Void, Bitmap>{
		
		private String mFileUrl;
	//		public ImageAsynTask(String fileUrl){
	//			this.mFileUrl=fileUrl;
	//		}
		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				mFileUrl=params[0];
				return downLoadImage(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result!=null){				
				HashMap<String, Bitmap> map=new HashMap<String, Bitmap>();
				map.put(mFileUrl, result);
				mArrayListImageBitmap.add(map);
				mAdapterImage.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}
	
	/** 流程跟踪对话框*/
	public void showDialogTaskTracking(){
		if(mDialogTaskTracking==null){
			mDialogTaskTracking=new Dialog(mContext);
			mDialogTaskTracking.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogTaskTracking.setContentView(R.layout.measure_task_tracking_dialog);
		}
		mDialogTaskTracking.show();
		TextView txtTaskTracking=(TextView)mDialogTaskTracking.findViewById(R.id.textViewMeasureTaskTrackingContent);
		txtTaskTracking.setText(Html.fromHtml(mTaskTracking));
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		Intent intent=null;
		switch (flag) {
		case Constansts.SUCCESS:  //流程启动成功
			//判断本地数据库是否有这条数据存在,如果有改变该数据的流程状态,否则不做修改
			String  commandId=new DBService(mContext).queryCommandId(mMapAttribute.get("rowId"));
			if(commandId!=null){
				new DBService(mContext).updateCommandProcedureState(commandId);
			}
			baseCloseDialog();
			Toast.makeText(mContext, "流程启动成功", Toast.LENGTH_SHORT).show();	
			if(intent==null){
				intent=new Intent(mContext,CommandMenuFragment.class);
			}
			this.setResult(0x01);
//			startActivity(intent);
			finish();
		break;
	case Constansts.ERRER:
			baseCloseDialog();
			Toast.makeText(mContext, "当前流程已经启动", Toast.LENGTH_SHORT).show();
		break;
	case Constansts.MES_TYPE_1: //打开文件
		if(mFile.exists()){
			openFile(mFile, mFileName);
		}else{
			Toast.makeText(mContext, "文件为空,请重新下载", Toast.LENGTH_SHORT).show();
		}
		baseCloseDialog();
		break;
	case Constansts.MES_TYPE_2: //审批流程
		    update();
			baseCloseDialog();
	    	approve();
		break;
	case Constansts.MES_TYPE_3:  //流程启动
		baseCloseDialog();
		updateCommandPerson();
		showCommandPersonDialog();
		break;
	case Constansts.MES_TYPE_4://
		baseCloseDialog();
		Toast.makeText(mContext, "获取人员信息失败", Toast.LENGTH_SHORT).show();
		break;
	case Constansts.MES_TYPE_5:  //流程跟踪
		baseCloseDialog();
		showDialogTaskTracking();
		break;
	case Constansts.MES_TYPE_6: //流程查看
		baseCloseDialog();
		updateTaskData();
		showDialogTaskLook();
		break;
	case Constansts.MES_TYPE_7:
		baseCloseDialog();
		Toast.makeText(mContext, "信息获取失败", Toast.LENGTH_SHORT).show();
		break;
	case Constansts.MES_TYPE_8://审批成功
		baseCloseDialog();
		closeWindowDialog();
		Toast.makeText(mContext, "审批成功", Toast.LENGTH_SHORT).show();
		if(intent==null){
			intent=new Intent(mContext,CommandMenuFragment.class);
		}
		this.setResult(0x01);
//		startActivity(intent);
		finish();
		break;
	case Constansts.MES_TYPE_9: //审批失败
		baseCloseDialog();
		closeWindowDialog();
		Toast.makeText(mContext, "该数据已经处理过,审批失败", Toast.LENGTH_SHORT).show();
		break;
	case Constansts.CONNECTION_TIMEOUT:
		baseCloseDialog();
		Toast.makeText(mContext, "网络访问超时", Toast.LENGTH_SHORT).show();
		default:
			break;
		}
		super.handleOtherMessage(flag);
	}
	
	
	public void openFile(File file,String fileName){
			// 截取文件后缀名
		String  fileFormatSuffix=fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		if("doc".equalsIgnoreCase(fileFormatSuffix) || "docx".equalsIgnoreCase(fileFormatSuffix)){
			startActivity(FileOpen.getWordFileIntent(file));
		}else if("xls".equalsIgnoreCase(fileFormatSuffix)){
			startActivity(FileOpen.getExcelFileIntent(file));
		}else if("pdf".equalsIgnoreCase(fileFormatSuffix)){
			startActivity(FileOpen.getPdfFileIntent(file));
		}else if("txt".equalsIgnoreCase(fileFormatSuffix)){
			startActivity(FileOpen.getTextFileIntent(file));
		}else if("ppt".equals(fileFormatSuffix)){
			startActivity(FileOpen.getPdfFileIntent(file));
		}else{
			Toast.makeText(mContext, "不支持此类型文件",Toast.LENGTH_SHORT).show();
			}
	}
		
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCommandStart: //流程启动
		baseShowDialog();
		getCommandStartPerson();  
		break;
	case R.id.btnCommandApprove: //流程审批
		baseShowDialog();
		getApproveData();
		break;
	case R.id.btnCommandTaskTracking://流程跟踪
		baseShowDialog();
		getTaskTrackingData();
		break;
	case R.id.btnCommandTaskLook:// 流程查看
			baseShowDialog();
			getTaskLookData();
			break;
		default:
			break;
		}
	}
		
	/** 审批数据跟新*/
	public void update(){
			if(mBaseMapData!=null && mBaseMapData.size()>0){				
				mBaseMap.clear();
				mBaseMap.putAll(mBaseMapData);
				mBaseMapData.clear();
			}
	}
		 
		/** 审批数据获取*/
	public void getApproveData(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			StringBuffer   str=new StringBuffer();
			str.append("{");
			str.append("rowId");
			str.append(":");
			str.append("\'"+mMapAttribute.get("rowId")+"\'");
			str.append(",");
			str.append("flowId");
			str.append(":");
			str.append("\'"+mMapAttribute.get("flowId")+"\'");
			str.append("}");
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("publicKey", Constansts.PUBLIC_KEY);
			parameters.put("userName", mUser.getUsername());
			parameters.put("Password", mUser.getPassword());
			parameters.put("type", "监理指令审批流程");
			parameters.put("parms",str.toString().trim());
			String stream=null;
			try {
				stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_READYFORAPPROVE, mContext);
				if(stream!=null){
					mBaseMapData=new Str2Json().getPaymentCertificate(stream);
					if(mBaseMapData!=null && mBaseMapData.size()>0){
						sendMessage(Constansts.MES_TYPE_2);
					}else{
					sendMessage(Constansts.MES_TYPE_7);
					}
				}else{
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendMessage(Constansts.CONNECTION_TIMEOUT);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			}
		}).start();
	}
	
	/** 关闭当前对话框*/
	public void closeWindowDialog(){
		baseCloseDialog();
		if(mDialogApproveSuggestion!=null){
			mDialogApproveSuggestion.dismiss();
		}
		if(mDialogApprovePerson!=null){
			mDialogApprovePerson.dismiss();
		}
	}
	
	
	/** 审批数据上传*/
	public void  uploadApproveData(final String str,final ProcessOperation  op){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StringBuffer  data=new StringBuffer();
				data.append("{");//开始
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword()); 
				parameters.put("type", "监理指令审批流程");
				data.append("\'rowId\'");
				data.append(":");
			    data.append("\'"+mMapAttribute.get("rowId")+"\'");
				data.append(",");
				data.append("\'flowId\'");
				data.append(":");
				data.append("\'"+mMapAttribute.get("flowId")+"\'");
				data.append(",");
				//command:first打回到第一步\previous打回到上一步\next正常审批
	//					if(op==null){
			   //正常审批
				data.append("\'command\'");
				data.append(":");
				data.append("\'next\'");
				data.append(",");
				//stepId:当command为next时提供的步骤Id
				data.append("\'stepId\':");
				data.append("\'"+mBaseMap.get("approveAttribute").get(0).get("stepId")+"\'");
				data.append(",");
				if(mBaseMap.get("arrayListApprovePersonInfo").size()>0){
					//	selectEmpId:当command为next时提供的用户选择的下步执行人id(以，号分隔的字符串)
					data.append("\'selectEmpId\'");
					data.append(":");
					data.append("\'");
					for(int j=0;j<mBaseMap.get("arrayListApprovePersonInfo").size();j++){
						if("1".equals(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("checkBoxState"))){							
							data.append(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("personId"));
						}
						if(j!=mBaseMap.get("arrayListApprovePersonInfo").size()-1){
							data.append(",");
						}
					}
					data.append("\'");
					data.append(",");
				}
	//					}else if(op==ProcessOperation.STEP_FIRST){
	//						// 打回到第一步
	//						data.append("\'command\'");
	//						data.append(":");
	//						data.append("\'first\'");
	//						data.append(",");
	//					}else if(op==ProcessOperation.STEP_BACk){
	//						//打回到上一步
	//						data.append("\'command\'");
	//						data.append(":");
	//						data.append("\'previous\'");
	//						data.append(",");
	//					}
				//suggest:用户输入的处理意见或打回原因
				data.append("\'suggest\'");
				data.append(":");
				data.append("\'");
				data.append(str==null?"":str);
				data.append("\'");
				data.append("}");//结束
				parameters.put("parms", data.toString().trim());
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_APPROVE, mContext);
					if(stream!=null){
						if(new Str2Json().getApproveResult(stream)){
							sendMessage(Constansts.MES_TYPE_8);
						}else{
							sendMessage(Constansts.MES_TYPE_9);
						}
					}else{
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();;
	}
	
	/** 审批流程*/
	public  void   approve(){
		if(mDialogApproveSuggestion==null){			
			mDialogApproveSuggestion=new Dialog(mContext);
			mDialogApproveSuggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogApproveSuggestion.setCanceledOnTouchOutside(false);
			mDialogApproveSuggestion.setContentView(R.layout.measure_mid_record_approve_dialog);
		}
		mDialogApproveSuggestion.show();
		EditText txtApproveStepName=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveStepName);
		EditText txtApproveOperatePerson=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveOperatePerson);
	//			final EditText txtApproveOperateTime=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveOperateTime);
		final EditText txtApproveSuggestion=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveSuggestion);
		Button   btnApproveNext=(Button)mDialogApproveSuggestion.findViewById(R.id.btnApproveNext);
		Button   btnApproveCancel=(Button)mDialogApproveSuggestion.findViewById(R.id.btnApproveCancel);
		txtApproveSuggestion.setText("");
		if(mBaseMap!=null && mBaseMap.size()>0){
			ArrayList<HashMap<String, String>> arrayListApproveAttribute=mBaseMap.get("approveAttribute");
			if(arrayListApproveAttribute.size()>0){
				HashMap<String, String> map=arrayListApproveAttribute.get(0);
				txtApproveStepName.setText(map.get("stepName"));
				txtApproveOperatePerson.setText(mUser.getEmpName());
	//					txtApproveOperateTime.setText(text);
			}
		}
		//时间对话框
	//			txtApproveOperateTime.setOnClickListener(new OnClickListener() {
	//				
	//				@Override
	//				public void onClick(View v) {
	//					new MyDatePickerDialog(mContext, new MyDatePickerDialog.OnDateTimeSetListener() {
	//						
	//						@Override
	//						public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
	//								int hour, int minute) {
	//							txtApproveOperateTime.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
	//						}
	//					}).show();
	//				}
	//			});
		
		//退出对话框
		btnApproveCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialogApproveSuggestion.dismiss();
			}
		});
		
		//转交给下一步
		btnApproveNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Button  btnApprovePersonConfirm=null;
				if("".equals(txtApproveSuggestion.getText().toString())){
					Toast.makeText(mContext,"审批意见不能为空" ,Toast.LENGTH_SHORT).show();
					return;
				}
				if(mDialogApprovePerson==null){						
					mDialogApprovePerson=new Dialog(mContext,R.style.dialog);
					mDialogApprovePerson.requestWindowFeature(Window.FEATURE_NO_TITLE);
					mDialogApprovePerson.setContentView(R.layout.measure_mid_record_approve_person_dialog);
					mExpandableListViewApprovePerson=(ExpandableListView)mDialogApprovePerson.findViewById(R.id.expandableListViewApprovePerson);
				}
				
				if(mBaseMap!=null && mBaseMap.size()>0){
					ArrayList<HashMap<String, String>> arrayList=mBaseMap.get("approveAttribute");
					if(arrayList!=null && arrayList.size()>0){
							if("true".equals(arrayList.get(0).get("hasNext"))){									
								mExpandableListApdater=new MyExpandableListAdapter();
								mExpandableListViewApprovePerson.setAdapter(mExpandableListApdater);
								mExpandableListViewApprovePerson.expandGroup(0);
								mDialogApprovePerson.show();
								btnApprovePersonConfirm=(Button)mDialogApprovePerson.findViewById(R.id.btnApprovePersonConfirm);									
							}else{
								baseShowDialog();
								uploadApproveData(txtApproveSuggestion.getText().toString(),null);
							}
					}
				}
				//完成按钮
				if(btnApprovePersonConfirm!=null){
					btnApprovePersonConfirm.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(isCheckPerson()==false){
								Toast.makeText(mContext,"必须选择审批人" ,Toast.LENGTH_SHORT).show();
								return;
							}else{								
								baseShowDialog();
								uploadApproveData(txtApproveSuggestion.getText().toString(),null);
							}
						}
					});
				}
			}
		});
	}
	
	/** 更新流程数据*/
	public void updateTaskData(){
		if(mArrayListTask.size()>0){
			mArrayListTask.clear();
		}
		if(mArrayListTaskData.size()>0){
			mArrayListTask.addAll(mArrayListTaskData);
			mMyTaskLookAdapter.notifyDataSetChanged();
			mArrayListTaskData.clear();
		}
	}
	
	/** 流程查看对话框*/
	public void showDialogTaskLook(){
		if(mDialogTaskLook==null){
			mDialogTaskLook=new Dialog(mContext);
			mDialogTaskLook.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogTaskLook.setContentView(R.layout.measure_task_look_dialog);
		}
		mDialogTaskLook.show();
		ListView mListView=(ListView)mDialogTaskLook.findViewById(R.id.listViewTaskLook);
		mListView.setAdapter(mMyTaskLookAdapter);
	}
	
	/** 流程查看信息获取*/
	public void  getTaskLookData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());			
				parameters.put("type", "View");
				parameters.put("flowId", mMapAttribute.get("flowId"));
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_FLOW, mContext);
					mArrayListTaskData=new Str2Json().getTaskLookData(stream);
					if(mArrayListTaskData!=null ){
						sendMessage(Constansts.MES_TYPE_6);
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
	
	/** 流程跟踪信息获取*/
	public void  getTaskTrackingData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());			
				parameters.put("type", "Trace");
				parameters.put("flowId", mMapAttribute.get("flowId"));
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_FLOW, mContext);
						if(new Str2Json().getTaskTrackingData(stream)!=null){
							mTaskTracking=new Str2Json().getTaskTrackingData(stream);
							sendMessage(Constansts.MES_TYPE_5);
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
		
	/** 更新流程启动人员信息 */
	public void updateCommandPerson() {
		if (mBaseMap.size() > 0) {
			mBaseMap.clear();
		}
		if (mBaseMapData != null) {
			if (mBaseMapData.size() > 0) {
				mBaseMap.putAll(mBaseMapData);
			}
		}
		if (mBaseMapData != null) {
			mBaseMapData.clear();
		}
	}
	
	/** 获取流程启动的人员信息 */
	public void getCommandStartPerson() {
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append("{");
				strBuffer.append("\"rowId\"");
				strBuffer.append(":");
				strBuffer.append("\"" + mMapAttribute.get("rowId") + "\"");
				strBuffer.append("}");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("publicKey", Constansts.PUBLIC_KEY);
				map.put("userName", mUser.getUsername());
				map.put("Password", mUser.getPassword());
				map.put("parms", strBuffer.toString().trim());
				map.put("type", "监理指令审批流程");
				String stream = null;
				try {
					stream = WebServiceUtils.requestM(map,
							Constansts.START_OF_COMMAND, mContext);
					if (stream != null) {
						mBaseMapData = new Str2Json()
								.getCommandProcedurePerson(stream);
						if (mBaseMapData.size() > 0) {
							sendMessage(Constansts.MES_TYPE_3);
						} else {
							sendMessage(Constansts.MES_TYPE_4);
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
	
	/** 判断用户是否勾选了人 如果没有勾选人不能审批 return false otherwise true */
	public boolean isCheckPerson() {
		ArrayList<HashMap<String, String>> arrayList = mBaseMap.get("arrayListApprovePersonInfo");
		for (int i = 0; i < arrayList.size(); i++) {
		HashMap<String, String> map = arrayList.get(i);
		if ("1".equals(map.get("checkBoxState"))) {
			return true;
		}
		if (i == arrayList.size() - 1&& "0".equals(map.get("checkBoxState"))) {
				return false;
			}
		}
		return false;
	}
	
	/** 显示流程启动人员dialog */
	public void showCommandPersonDialog() {
		if (mDialogCommandPerson == null) {
			mDialogCommandPerson = new Dialog(mContext, R.style.dialog);
			mDialogCommandPerson.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogCommandPerson.setContentView(R.layout.measure_mid_record_approve_person_dialog);
			mExpandableListViewApprovePerson = (ExpandableListView) mDialogCommandPerson.findViewById(R.id.expandableListViewApprovePerson);
		}
		mExpandableListViewApprovePerson.setAdapter(new MyExpandableListAdapter());
		mExpandableListViewApprovePerson.expandGroup(0);
		mDialogCommandPerson.show();
		final Button btnConfirm = (Button) mDialogCommandPerson.findViewById(R.id.btnApprovePersonConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				if (isCheckPerson()) {
					if (mDialogCommandPerson != null) {
						mDialogCommandPerson.cancel();
					}
					baseShowDialog();
					startCommandProcedure();
				} else {
					Toast.makeText(mContext, "您必须勾选人员", Toast.LENGTH_SHORT)
							.show();
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
			strBuffer.append("\""+mMapAttribute.get("rowId")+"\"");
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
	
	
	/** 单张图片浏览**/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 CommandImageAdapter.ViewHolder  viewHolderImg=(CommandImageAdapter.ViewHolder)view.getTag();
			Bitmap  bitmap=viewHolderImg.mBitmap;
			Dialog  imgDialog=null;
			if(bitmap!=null){
				imgDialog=new Dialog(mContext);
				imgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				imgDialog.setContentView(R.layout.command_img_browse_dialog);
				imgDialog.show();
				 ImageView  imageView=(ImageView)imgDialog.findViewById(R.id.imgBrowse);
				 imageView.setImageBitmap(bitmap);
			}
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
	//						 size=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(gropPosition).get("count"));
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
	     for(int i=0;i<mExpandableListViewApprovePerson.getCount();i++){
	    		if(i!=groupPosition && mExpandableListViewApprovePerson.isGroupExpanded(groupPosition)){
	    			mExpandableListViewApprovePerson.collapseGroup(i);
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
		if(mAdapterImage.getImageCache()!=null && mAdapterImage.getImageCache().size()>0){
		HashMap<String, SoftReference<Bitmap>>  map=mAdapterImage.getImageCache();
		if(map!=null){
			Iterator iterator	=map.entrySet().iterator();
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
		}
		super.onDestroy();
	}
	}
	
