package com.tongyan.activity.oa;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaStandardFileAdapter;
import com.tongyan.activity.adapter.OaStandardFileAdapter.ViewHolder;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.service.DownloadService;
import com.tongyan.service.DownloadService.ICallbackResult;
import com.tongyan.service.DownloadService.MBinder;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.OpenFileUtils;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @ClassName P22_StandardFileAct 
 * @author wanghb
 * @date 2013-7-25 pm 05:30:30
 * @date2 2014-07-14 13:23
 * @desc 移动OA-规范文件
 */
public class OaStandardFileAct extends AbstructCommonActivity {
	
	private Button homeBtn,searchBtn;
	
	private EditText editText;
	private ListView listView;
	private OaStandardFileAdapter p22Adapter;
	private ArrayList<File> mFileList = new ArrayList<File>();
	private ArrayList<File> mSearchList = new ArrayList<File>();
	
	private LinearLayout mLinearLayout = null;
	private LinearLayout mScrollLayout = null;
	
	private HorizontalScrollView mScrollContainer;
	
	private RelativeLayout mSearchLayout = null;
	
	//private SharedPreferences mPreferences;
	
	boolean mSearchShow = false;
	boolean mIsLastMenu = false;
	
	private Context mContext = this;
	private File  mAssignFile = null;
	
	
	private MBinder binder;
	private boolean isStartService = false;
	
	private String isSucc; 
	private _User localUser;
	
	private Dialog mDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_standard_file_list);
		homeBtn = (Button)findViewById(R.id.p22_standard_file_home_btn);
		listView = (ListView)findViewById(R.id.p22_standard_file_listview);
		searchBtn = (Button)findViewById(R.id.p22_standard_file_search_btn_id);
		editText = (EditText)findViewById(R.id.p22_standard_file_search_edittext);
		
		mLinearLayout = (LinearLayout)findViewById(R.id.file_dir_container);
		
		mScrollLayout = (LinearLayout)findViewById(R.id.file_dir_menu_scroll);
		
		mScrollContainer = (HorizontalScrollView)findViewById(R.id.file_scroll_container);
		mSearchLayout = (RelativeLayout)findViewById(R.id.p22_standard_file_search_container);
		
	}
	
	
	public List<File> getFileList(File file) {
		if(file != null && file.exists()) {
			File[] mFileAry = file.listFiles();
			if(mFileAry != null) {
				return  Arrays.asList(mFileAry);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	public void addFileMenu(File text, boolean state) {
		View view = getLayoutInflater().inflate(R.layout.oa_standard_file_menu_container, null);
		TextView textview = (TextView)view.findViewById(R.id.menu_text);
		textview.setText(text.getName());
		if(state) {
			textview.setTextColor(getResources().getColor(R.color.common_color));
		} else {
			textview.setTextColor(getResources().getColor(R.color.gray));
		}
		view.setTag(text);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = (File)v.getTag();
				refreshListView(file);
				removeAllFileMenu();
				mFileMenuList.add(file);
				getMenuFileList(file);
				initMenuView();
				scrollTo();
			}
		});
		mScrollLayout.addView(view,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
	}
	
	public void removeAllFileMenu() {
		if(mScrollLayout != null) {
			mScrollLayout.removeAllViews();
		}
		if(mFileMenuList != null) {
			mFileMenuList.clear();
		}
	}
	
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		listView.setOnItemClickListener(listViewListener);
		
		editText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		editText.setCursorVisible(false);//去光标
		
		editText.setOnTouchListener(editTouchListener);
		searchBtn.setOnClickListener(searchBtnListener);
	}
	
	/**
	 * 搜索框-点击触摸监听事件
	 */
	OnTouchListener editTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			editText.setCursorVisible(true);
			return false;
		}
	};
	private String mAssignPath = null;
	
	/**
	 * 业务主入口
	 */
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		
		//mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable(){
			@Override
			public void run() {
				searchFilePath("/storage/");//查找固化文件
				if(mAssignPath == null) {
					searchFilePath("/mnt/");
				}
				sendMessage(Constansts.MES_TYPE_2);
			}
		}).start();
		
		
	}
	/**
	 * 刷新列表
	 */
	public void refreshList() {
		if(mAssignPath != null) {
			mAssignFile = new File(mAssignPath);
			
			List<File> fileAry = getFileList(mAssignFile);
			if(fileAry != null) {
				mFileList.addAll(fileAry);
				mFileMenuList.add(mAssignFile);
				getMenuFileList(mAssignFile);
				initMenuView();
			}
			p22Adapter = new OaStandardFileAdapter(this, mFileList);
			listView.setAdapter(p22Adapter);
		}
	}
	
	
	/**
	 * 
	 */
	public void searchFilePath(String mRootPath) {
		File file = new File(mRootPath);
		getAssignPath(file);
	}
	
	public void getAssignPath(File file) {
		if(file != null) {
			if("固化文件".equals(file.getName())) {
				mAssignPath = file.getPath();
			} else {
				if(file.isDirectory()) {
					File[] fileAry = file.listFiles();
					if(fileAry != null && fileAry.length > 0) {
						for(int i = 0,len = fileAry.length; i < len; i ++) {
							File mFile = fileAry[i];
							getAssignPath(mFile);
						}
					}
				} 
			}
		}
	}
	
	
	public void initMenuView() {
		if(mFileMenuList != null && mFileMenuList.size() > 0) {
			for(int i = mFileMenuList.size() - 2; i >= 0; i -- ) {
				File file = mFileMenuList.get(i);
				if("固化文件".equals(file.getName())) {
					mIsLastMenu = true;
					if(i == 0) {
						addFileMenu(file, true);
					} else {
						addFileMenu(file, false);
					}
				} else if(mIsLastMenu) {
					if(i == 0) {
						addFileMenu(file, true);
					} else {
						addFileMenu(file, false);
					}
				}
			}
			mIsLastMenu = false;
		}
	}
	
	
	public ArrayList<File> mFileMenuList = new ArrayList<File>();
	public boolean getMenuFileList(File file) {
		if(file != null) {
			File mF = file.getParentFile();
			if(mF != null) {
				mFileMenuList.add(mF);
				if(getMenuFileList(mF)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void scrollTo() {
		mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
	}
	
	OnItemClickListener listViewListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			ViewHolder holder = (ViewHolder)arg1.getTag();
			File file = holder.mFile;
			String fileName = file.getName();
			if(file.isDirectory()) {
				refreshListView(file);
				removeAllFileMenu();
				mFileMenuList.add(file);
				getMenuFileList(file);
				initMenuView();
				scrollTo();
			} else {
				if(fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".xlsx")) {
					try {
						PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo("cn.wps.moffice_eng", 0);
						if(packageInfo == null){
							dialog();
					     } else {
					    	Intent intent = new Intent();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
							Uri uri = Uri.fromFile(file);
							intent.setData(uri);
							startActivity(intent);
					     }
					} catch (NameNotFoundException e) {
						e.printStackTrace();
						dialog();
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
						dialog();
						return;
					}
				} else {
					OpenFileUtils.openFileByFile(file);
				}
			}
		}
	};
	
	public void refreshListView(File file) {
		if(mFileList != null) {
			mFileList.clear();
			List<File> fileAry = getFileList(file);
			if(fileAry != null) {
				mFileList.addAll(fileAry);
			}
		}
		if(p22Adapter != null) {
			p22Adapter.notifyDataSetChanged();
		}
	}
 	
	public void refreshSearchListView(ArrayList<File> mSearchList) {
		if(mFileList != null) {
			mFileList.clear();
			if(mSearchList != null) {
				mFileList.addAll(mSearchList);
			}
		}
		if(p22Adapter != null) {
			p22Adapter.notifyDataSetChanged();
		}
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			/*Intent intent = new Intent(OaStandardFileAct.this,MainAct.class);
			startActivity(intent);*/
			if(mSearchShow) {
				mSearchLayout.setVisibility(View.INVISIBLE);
				mLinearLayout.setVisibility(View.VISIBLE);
				mSearchShow = false;
			} else {
				mSearchLayout.setVisibility(View.VISIBLE);
				mLinearLayout.setVisibility(View.INVISIBLE);
				mSearchShow = true;
			}
		}
	};
	
	OnClickListener searchBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(mSearchList != null) {
				mSearchList.clear();
			}
			String mSearchText = editText.getText().toString(); 
			if(mSearchText == null || "".equals(mSearchText.trim())) {
				Toast.makeText(mContext, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			getSearchList(mSearchText,mAssignFile);
			if(mSearchList != null) {
				refreshSearchListView(mSearchList);
			} else {
				Toast.makeText(mContext, "无搜索内容", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	
	
	public void getSearchList(String name, File file){
		if(file != null) {
			if(file.isDirectory()) {
				if(file.getName().contains(name) || file.getName().contains(name.toLowerCase()) || file.getName().contains(name.toUpperCase())) {//需要忽略大小写
					mSearchList.add(file);
				} else {
					File[] fileAry = file.listFiles();
					if(fileAry != null && fileAry.length > 0) {
						for(int i = 0,len = fileAry.length; i < len; i ++) {
							File mFile = fileAry[i];
							getSearchList(name, mFile);
						}
					}
				}
			} else {
				if(file.getName().contains(name)) {
					mSearchList.add(file);
				}
			}
		}
	}
	
	public void dialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("您还没有文件查看工具，请先下载");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				getWPSapk();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MBinder) service;
			// 开始下载
			binder.addCallback(callback);
			binder.start();
		}
	};
	
	
	
	protected void onDestroy() {
		super.onDestroy();
		if(mFileList != null) {
			mFileList.clear();
		}
		if(mSearchList != null) {
			mSearchList.clear();
		}
	};
	
	private ICallbackResult callback = new ICallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(conn != null && isStartService) {
					unbindService(conn);
					isStartService = false;
				}
			} else if("error".equals(result)) {
				if(conn != null && isStartService) {
					unbindService(conn);
					isStartService = false;
				}
			}
		}
	};
	
	public void getWPSapk() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					String jsonStr = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETOFFICEAPK, mContext);
					isSucc = new Str2Json().getWpsApk(jsonStr);
					if(isSucc != null && !"".equals(isSucc.trim())) {
						sendMessage(Constansts.MES_TYPE_1);
					} else {
						sendMessage(Constansts.ERRER);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.MES_TYPE_1 :
			Intent it = new Intent(mContext, DownloadService.class);
			it.putExtra("DownloadContent", String.valueOf("WPS Office 正在下载..."));
			it.putExtra("DownloadPath", isSucc);
			it.putExtra("DownloadIcon", R.drawable.wps_icon);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
			isStartService = true;
			break;
		case Constansts.ERRER:
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_2:
			if(mDialog != null) {
				mDialog.dismiss();
				mDialog = null;
			}
			refreshList();
			break;
		default:
			break;
		}
	}
}
