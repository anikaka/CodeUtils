package com.tygeo.highwaytunnel.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import ty.security.com.PermissionManager;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBManager;
import com.tygeo.highwaytunnel.DBhelper.Web_date_provider;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckMainActivity;
import com.tygeo.highwaytunnel.common.Constant;
import com.tygeo.highwaytunnel.common.DateTools;
import com.tygeo.highwaytunnel.common.DeviceUuidFactory;
import com.tygeo.highwaytunnel.common.DownloadService;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.PaseXMl;
import com.tygeo.highwaytunnel.common.UpdateManager;
import com.tygeo.highwaytunnel.common.WebServiceUtil;
import com.tygeo.highwaytunnel.common.DownloadService.DownloadBinder;
import com.tygeo.highwaytunnel.entity.CheckinfoE;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import com.tygeo.highwaytunnel.entity.line_json;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 登陆界面
 */
public class Login extends Activity  implements OnClickListener{
	SharedPreferences preferences;
	int screenHeight;
	int screenWidth;
	private Thread mThread;
	TextView Check_PsyAdd;
	ImageButton Check_SureBtn;
	Message mes;
	Thread thred1, thred;
	Process pro;
	String mg = "";
	String s;
	boolean flag;
	TextView reinfo;
	Button Check_NextBtn,
						  mBtnHideCheck;// 隐患排查
	ProgressDialog pd, pt;
	List<line_json> lj;
	List<CheckinfoE> ce, me;
	List<TunnelInfoE> te;
	EditText Check_Edit;
	ProgressBar progressBar;
	ImageView Login_Img, next_Ibtn, about_tyBtn;
	public DBManager dbHelper;
	WebView webView;
	WebViewClient webViewClient;
	Context context;
	private UpdateManager mUpdateManager;
	private InfoApplication app;
	private int currentVersionCode;
	
	private Thread thread;
	private boolean mFlag = true;
	public String UpdateInfo;
	public static String ApkUrl = null;
	private boolean isBinded;
	private DownloadBinder binder;
	private  ProgressDialog	mUpdateDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.login);
		InfoApplication.getinstance().addActivity(this);
		app = (InfoApplication) getApplication();
		reinfo = (TextView) findViewById(R.id.login_renet);
		Login_Img = (ImageView) findViewById(R.id.Img1);
		next_Ibtn = (ImageButton) findViewById(R.id.login_NextIBtn);
		about_tyBtn = (ImageButton) findViewById(R.id.login_aboutIBtn);
		mBtnHideCheck=(Button)findViewById(R.id.btnHideCheck);
		mBtnHideCheck.setOnClickListener(this);
		
		checkUpdate();

		next_Ibtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// int i=((InfoApplication)getApplication()).getC();
				//
				// if(i==1){startActivity(new
				// Intent(Login.this,Project_configuration.class));}
				DeviceUuidFactory uu = new DeviceUuidFactory(InfoApplication.getInstance());
				String uid = uu.getDeviceUuid().toString();
				PermissionManager pm = new PermissionManager(InfoApplication.getinstance(), getString(R.string.ipstr),getString(R.string.producebh), uid);
				mg = pm.VerifyCode();
				if (!mg.equals("Y")) {
					Toast.makeText(Login.this, mg, Toast.LENGTH_LONG).show();
					System.out.println(mg);
				} else {
					pd = new ProgressDialog(Login.this);
					pd.setCanceledOnTouchOutside(false);
					pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pd.setTitle("检查网络………");
					pd.show();
					Handler myHander = new Handler();
					myHander.postDelayed(new Runnable() {
						@Override
						public void run() {
							WebServiceUtil web = new WebServiceUtil();
							handler.sendEmptyMessage(22);
						}
					}, 2000);

					Runnable run2 = new Runnable() {

						@Override
						public void run() {
						

						}
					};
					Thread thred1 = new Thread(run2);
				}
			}
		});

		about_tyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Login.this, html.class));

			}
		});
		
		reinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Login.this, Check.class));
		}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mFlag = false;
			thread = null;
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("注意")
					.setMessage("确定要退出吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									InfoApplication.getinstance().exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			break;

		default:
			break;
		}

		return false;
	}

	
	/*
	 *下载数据 
	 *
	 */
	public void gotosys() {
		if (!mg.equals("Y")) {
			Toast.makeText(Login.this, mg, Toast.LENGTH_LONG).show();
			System.out.println(mg);

		} else {
			// pt.dismiss();
			if (flag == true) {
				startActivity(new Intent(Login.this, Check.class));
			} else {
				
				// pd = new ProgressDialog(Login.this);
				// pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCanceledOnTouchOutside(false);
				pd.setTitle("正在进入系统…………");
				// pd.show();
				       
				Runnable run = new Runnable() {
					
					@Override
					public void run() {me = WebServiceUtil.GetBaseManagerUnitInfo();
					if (!(me.size() == 0)) {

						Web_date_provider db = new Web_date_provider();
						//下载数据
						db.GetDownData();
						handler.sendEmptyMessage(1);

					} else {
						handler.sendEmptyMessage(1);
					}

				}
					// }
				};

				Thread thred = new Thread(run);
				thred.start();

			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHideCheck: // 进入隐患排查
			    Intent  intent=new Intent(Login.this,HideCheckMainActivity.class);
			    startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	/**
	 *检查更新
	 *接口名：CheckVersion
	 *参数：      versionCode(当前版本) AndroidManifest 文件里面的 versionCode/100 如果小于服务器上的版本号则下载最新apk
	 *
	 **/
	public  void checkUpdate(){
		mUpdateDialog = new ProgressDialog(Login.this);
		mUpdateDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mUpdateDialog.setTitle("检查新版本………");
		mUpdateDialog.show();
		thread = new Thread(
				new Runnable() {
					public void run() {
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("versionCode", String.valueOf(Constant.VERSION));
						try {/*
							String jsonresult = WebServiceUtil.requestM(Login.this, map, Constant.METHOD_OF_CheckVersion).get("BodyIn");
							HashMap<String, Object> vMap= WebServiceUtil.analysisVersion(jsonresult,"CheckVersionResult");
							if (vMap != null && vMap.size() > 0) {
								if ("ok".equalsIgnoreCase(vMap.get("s").toString())) {
									if (vMap.get("v") != null) {
										HashMap<String, String> mLoginMap = (HashMap<String, String>)vMap.get("v");
										if (Constant.VERSION >= Float.parseFloat(mLoginMap.get("apkVersion"))) {
											if(!mFlag){
												return;
											}
											handler.sendEmptyMessage(Constant.MSG_0x2005);
										} else {
											UpdateInfo = mLoginMap.get("apkContent");
											ApkUrl = DateTools.getRoute(Login.this) + "/" + mLoginMap.get("apkPath");
											if(!mFlag){
												return;
											}
											handler.sendEmptyMessage(Constant.Down);
										}
									}else {
										if(!mFlag){
											return;
										}
										handler.sendEmptyMessage(Constant.ERRER);
									}
								} else {
									if(!mFlag){
										return;
									}
									handler.sendEmptyMessage(Constant.MSG_0x2005);
								}
							} else {
								if(!mFlag){
									return;
								}
								handler.sendEmptyMessage(Constant.MSG_0x2005);
							}
						*/} catch (Exception e) {
							if(!mFlag){
								return;
							}
							handler.sendEmptyMessage(Constant.MSG_0x2005);
							e.printStackTrace();
						} 
					}
			});
		thread.start();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case Constant.Down:
					if(mUpdateDialog != null) {
						mUpdateDialog.dismiss();
					}
					showUpdateInfo();
					break;
				case Constant.MSG_0x2005:
					if(mUpdateDialog != null) {
						mUpdateDialog.dismiss();
					}
					break;
				case Constant.ERRER:
					if(mUpdateDialog != null) {
						mUpdateDialog.dismiss();
						Toast.makeText(getApplicationContext(), "未检查到新版本", Toast.LENGTH_SHORT).show();
					}
					break;
				case 1:
					Thread t1 = new Thread(new Runnable() {
						
						@Override
						public void run() {

							try {
								Thread.sleep(2000);// 让他显示10秒后，取消ProgressDialog
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							pd.dismiss();
							handler.sendEmptyMessage(11);
						}
					});
					t1.start();
					break;
				case 22:
					System.out.println("Ok22");
					// pt.dismiss();
					System.out.println("mg" + mg);
					//进入系统
					gotosys();
					break;
				case 3:
					System.out.println("no");
					break;
				case 11:
					startActivity(new Intent(Login.this,Project_configuration.class));
					break;
				default:
					break;
				}
			}
		};
	};
	/**
	 * 确定是否下载最新apk
	 */
	public void showUpdateInfo() {
		AlertDialog dialog = new AlertDialog.Builder(Login.this)
		.setTitle("提示")
		.setMessage("发现新版本")
		.setPositiveButton("确定更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				Intent it = new Intent(Login.this, DownloadService.class);
				bindService(it, conn, Context.BIND_AUTO_CREATE);
			}
		})
		.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
			}
		}).show();
	}
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBinded = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (DownloadBinder) service;
			// 开始下载
			isBinded = true;
			binder.addCallback(callback);
			binder.start();
			
		}
	};
	
	public interface ICallbackResult {
		public void OnBackResult(Object result);
	}
	
	private ICallbackResult callback = new ICallbackResult() {
		@Override
		public void OnBackResult(Object result) {
			if ("finish".equals(result)) {
				finish();
				return;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
			unbindService(conn);
		}
	}
}
