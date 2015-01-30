package com.tongyan.activity;

import java.util.HashMap;
import java.util.Map;

import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.service.DownloadBService;
import com.tongyan.service.MGPSService;
import com.tongyan.service.DownloadBService.IBCallbackResult;
import com.tongyan.service.DownloadBService.MbBinder;
import com.tongyan.service.DownloadService;
import com.tongyan.service.DownloadService.ICallbackResult;
import com.tongyan.service.DownloadService.MBinder;
import com.tongyan.utils.ConnectivityUtils;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
/***
 * 
 * @ClassName P01_LoadingAct 
 * @author wanghb
 * @date 2013-7-12 14:35  
 * @desc loading page
 */
public class LoadingAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private String path = null;
	private String mSqiltePath = null;
	private String mSqliteVersion = null;
	
	private SharedPreferences mPreferences;
	
	
	private MyApplication mApplication;
	private _User localUser;
	
	private final static int MESSAGE_INTENT_LOGIN = 0x45422;
	private final static int MESSAGE_LOADING_1 = 0x45433;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		checkConnection();
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		localUser = mApplication.localUser;
		
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		float screenRate = screenWidth/1280.0f;
		//drawLine.setScreenRate(screenRate);
		
		//Toast.makeText(this, screenWidth+"*"+screenHeight, Toast.LENGTH_LONG).show();
		
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();  
		float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
		int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
		Toast.makeText(this, "分辨率："+screenWidth+"*"+screenHeight+"  每英寸:"+densityDPI, Toast.LENGTH_LONG).show();
		Log.d("11111", "分辨率："+screenWidth+"*"+screenHeight+"  每英寸:"+densityDPI);
		
		
		getPreferences();
	}
	
	public void getPreferences() {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	
	/**
	 * 检测网络
	 */
	public void checkConnection() {
		boolean isConn = new ConnectivityUtils(this).isConnection();
		if(!isConn) {
			Toast.makeText(this, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			new Thread(){
				public void run() {
					try {
						Thread.currentThread().sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					sendMessage(Constansts.SUCCESS);
				};
			}.start();
		} else {
			new Thread(){
				public void run() {
					try {
						Map<String, String> properties = new HashMap<String, String>();
						properties.put("publicKey", Constansts.PUBLIC_KEY);
						String str = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_GETVERSION, LoadingAct.this);
						Map<String,String> mR = new Str2Json().getNewVersion(str, "GetVersionResult");
						if(mR != null) {
							double mOldV = 0.0;
							double mNewV = 0.0;
							boolean isParseTrue = true;
							try {
								mOldV = Double.parseDouble(Constansts.VERSION);
								mNewV = Double.parseDouble(mR.get("version").trim());
							} catch(Exception e) {
								isParseTrue = false;
							}
							if(isParseTrue) {
								if(mOldV < mNewV) {
									path = (String)mR.get("path");
									sendMessage(Constansts.MES_TYPE_1);
								} else {
									sendMessage(Constansts.ERRER);
								}
							} else {
								if(!Constansts.VERSION.equals(mR.get("version"))) {
									path = (String)mR.get("path");
									sendMessage(Constansts.MES_TYPE_1);
								} else {
									sendMessage(Constansts.ERRER);
								}
							}
						} else {
							sendMessage(Constansts.ERRER);
						}
					} catch (Exception e) {
						sendMessage(Constansts.ERRER);
						e.printStackTrace();
					}
				};
			}.start();
		}
	}
	
	
	public void getLocalSqlite() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Map<String, String> propert = new HashMap<String, String>();
					propert.put("publicKey", Constansts.PUBLIC_KEY);
					String strJson = WebServiceUtils.requestM(propert, Constansts.METHOD_OF_GETSECTIONITEM, LoadingAct.this);
					Map<String,String> mR2 = new Str2Json().getNewVersion(strJson,"GetSectionItemResult");
					if(mR2 != null) {
						mSqliteVersion = mR2.get("version") == null ? "" : mR2.get("version");
						String mSqlitePath = mR2.get("path");
						if(mPreferences == null) {
							getPreferences();
						}
						String mLocVersion = mPreferences.getString(Constansts.PREFERENCES_SQLITE_VERSION, "0");
						double mOldV = 0.0;
						double mNewV = 0.0;
						boolean isParseTrue = true;
						try {
							mOldV = Double.parseDouble(mLocVersion);
							mNewV = Double.parseDouble(mSqliteVersion.trim());
						} catch(Exception e) {
							isParseTrue = false;
						}
						if(isParseTrue) {
							if(mOldV < mNewV) {
								mSqiltePath = mSqlitePath;
								sendMessage(Constansts.MES_TYPE_2);
							} else {
								sendMessage(MESSAGE_LOADING_1);
							}
						} else {
							if(!mLocVersion.equals(mSqliteVersion)) {
								mSqiltePath = mSqlitePath;
								sendMessage(Constansts.MES_TYPE_2);
							} else {
								sendMessage(MESSAGE_LOADING_1);
							}
						}
					} else {
						sendMessage(MESSAGE_LOADING_1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(MESSAGE_LOADING_1);
				}
			}
		}).start();
		
		
	}
	
	/**
	 * intent
	 */
	public void toIntent() {
		//判断自动登录
		if(localUser != null && localUser.getSavepassword()!= null && localUser.getSavepassword()== 1 && localUser.getAutologin() != null && localUser.getAutologin() == 1) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_LOGIN,mContext);
						if(str != null) {
							Map<String, String> loginBackMap = new Str2Json().getLoginInfo(str);
							//sectionId
							if(loginBackMap != null && "ok".equals(loginBackMap.get("succ"))) {//登录成功
								new DBService(mContext).updateLoginType(localUser);
								sendMessage(Constansts.MES_TYPE_3);
							} else {
								sendMessage(MESSAGE_INTENT_LOGIN);
							}
						} else {
							sendMessage(MESSAGE_INTENT_LOGIN);
						}
						} catch (Exception e) {
							e.printStackTrace();
							sendMessage(MESSAGE_INTENT_LOGIN);
						}
				}
			}).start();
			
			
		} else {
			sendMessage(MESSAGE_INTENT_LOGIN);
		}
	}
	
	
	public void intentLogin() {
		Intent intent = new Intent(LoadingAct.this,LoginAct.class);
		startActivity(intent);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 404 || resultCode ==0 ) {
			new Thread(){
				public void run() {
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					sendMessage(Constansts.DEFAULT);
					
				};
			}.start();
			
 		}
	}
	private MBinder binder;
	private boolean mIsBinder;
	private boolean mIsBinderB;
	private MbBinder mBBinder;
	
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBinder = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MBinder) service;
			// 开始下载
			binder.addCallback(callback);
			binder.start();
		}
	};
	
	ServiceConnection connB = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBinderB = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBBinder = (MbBinder) service;
			// 开始下载
			mBBinder.addCallback(callbackB);
			mBBinder.start();
		}
	};
	
	private IBCallbackResult callbackB = new IBCallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(connB != null && mIsBinderB) {
					//unbindService(connB);
					//connB = null;
				}
				finish();
				return;
			} else if("error".equals(result)) {
				if(connB != null && mIsBinderB) {
					//unbindService(connB);
					//connB = null;
				}
			}
		}
	};
	
	
	private ICallbackResult callback = new ICallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
				return;
			} else if("error".equals(result)) {
				if(conn != null && mIsBinder) {
					//unbindService(conn);
					//conn = null;
				}
			}
		}
	};
	
	
	
	
	
//------------------------------------------------------
Dialog dialog1 = null; 
public void check() {
	dialog1 = new AlertDialog.Builder(this)
		.setTitle("系统更新")
		.setMessage("发现新版本，请更新！")
		// 设置内容
		.setPositiveButton("确定",// 设置确定按钮
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent it = new Intent(LoadingAct.this, DownloadService.class);
					it.putExtra("DownloadContent", "昌宁高速APP 正在下载...");
					it.putExtra("DownloadPath", path);
					it.putExtra("DownloadIcon", R.drawable.logo);
					mIsBinder = bindService(it, conn, Context.BIND_AUTO_CREATE);
					dialog1 = null;
					/*if(mSqliteShow) {
						checkSqliteVersion();
					}*/
					sendMessage(Constansts.ERRER);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		})
		.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				dialog1 = null;
				/*if(mSqliteShow) {
					checkSqliteVersion();
				}*/
				sendMessage(Constansts.ERRER);
			}
		}).create();// 创建并显示
		dialog1.show();
		dialog1.setCanceledOnTouchOutside(false);
	}


	Dialog dialog2 = null;
	public void checkSqliteVersion() {
		dialog2 = new AlertDialog.Builder(this)
		.setTitle("标段工程目录更新")
		.setMessage("发现有新数据，请更新！")
		// 设置内容
		.setPositiveButton("确定",// 设置确定按钮
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent it = new Intent(LoadingAct.this, DownloadBService.class);
					it.putExtra("DownloadContent", "标段工程目录数据 正在下载...");
					it.putExtra("DownloadPath", mSqiltePath);
					it.putExtra("DownloadIcon", R.drawable.download_soft);
					it.putExtra("DownloadParams", mSqliteVersion);		
					mIsBinderB = bindService(it, connB, Context.BIND_AUTO_CREATE);
					sendMessage(MESSAGE_LOADING_1);
					dialog2 = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		})
		.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				dialog2 = null;
				sendMessage(MESSAGE_LOADING_1);
			}
		}).create();// 创建并显示
		dialog2.show();
		dialog2.setCanceledOnTouchOutside(false);
	}
	
	
	@Override
	protected void onDestroy() {
		if(null != conn && mIsBinder) {
			unbindService(conn);
		}
		
		if(null != connB && mIsBinderB) {
			unbindService(connB);
		}
		
		if(null != dialog1) {
			dialog1.dismiss();
			dialog1 = null;
		}
		
		if(null != dialog2) {
			dialog2.dismiss();
			dialog2 = null;
		}
		
		super.onDestroy();
	}
	
	public void savePreferences() {
		Editor mEditor = mPreferences.edit();
		mEditor.putString(Constansts.INFO_USER_ACCOUNT, localUser.getUsername());
		mEditor.putString(Constansts.INFO_USER_ID, localUser.getUserid());
		mEditor.putString(Constansts.INFO_USER_NAME, localUser.getEmpName());
		mEditor.putString(Constansts.INFO_USER_PASSWORD, localUser.getPassword());
		mEditor.commit();
	}
	
	public void notificationGPS(){
		LocationManager alm = (LocationManager)this.getSystemService( Context.LOCATION_SERVICE );
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "请打开GPS", Toast.LENGTH_SHORT).show();
		} else {
			//Intent servicIntentStart = new Intent(this,GPSService.class);
			Intent servicIntentStart = new Intent(this,MGPSService.class);
			stopService(servicIntentStart);
			startService(servicIntentStart);
		}
	}
	//--------------------------------------------------------------
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 404);//intent to setting page of net
			break;
		case Constansts.ERRER:
			//toIntent();
			getLocalSqlite();
			break;
		case Constansts.DEFAULT:
			ConnectivityUtils isConn = new ConnectivityUtils(LoadingAct.this);
			if(isConn.isConnection()) {
				toIntent();
			} else {
				finish();
			}
			break;
		case Constansts.NET_ERROR :
			Toast.makeText(this, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case Constansts.MES_TYPE_1 :
			check();
			break;
		case Constansts.MES_TYPE_2:
			checkSqliteVersion();
			break;
		case Constansts.MES_TYPE_3 :
			savePreferences();
			if (localUser.getIsGps() != null && 1 == localUser.getIsGps()) {
				notificationGPS();
			}
			Intent intent = new Intent(this, MainAct.class);
			startActivity(intent);
			finish();
			break;
		case MESSAGE_INTENT_LOGIN :
			finish();
			intentLogin();
			break;
		case MESSAGE_LOADING_1 :
			toIntent();
			break;
		default:
			finish();
			break;
		}
	}
	
	
}
