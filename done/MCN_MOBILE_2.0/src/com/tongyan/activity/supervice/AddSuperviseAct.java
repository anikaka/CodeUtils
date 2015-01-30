package com.tongyan.activity.supervice;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Check;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ImageUtil;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.ScreenUtil;
import com.tongyan.widget.view.DefineButton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @ClassName P28_SuperviseAct 
 * @author wanghb
 * @date 2013-8-8 pm 04:11:29
 * @desc 
 */
public class AddSuperviseAct extends AbstructCommonActivity {
	
	//private final static String TAG = "P31_AddSuperviseAct";
	private Context mContext = this;
	
	private Button openCameraBtn,commitBtn,mHomeBtn;
	
	private LinearLayout photoContainer1;
	private LinearLayout photoContainer2;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	
	private int Max_X;
	
	private List<DefineButton> imgBtnList = new ArrayList<DefineButton>();
	
	private int numsPerRow;
	
	private LayoutParams layoutParams = new LayoutParams(123, 88);
	private LayoutParams midLayoutParams = null;
	
	public int deviceDistance;
	
	private int imgLen = 123;
	
	private int midTextLen;
	
	private EditText editTextContent;
	
	private Timer timer;
	
	List<SoftReference<Bitmap>> mCacheList = new ArrayList<SoftReference<Bitmap>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.supervise_camera);
		commitBtn = (Button)findViewById(R.id.p31_supervise_commit_btn_id);
		editTextContent = (EditText)findViewById(R.id.p31_supervise_edittext);
		photoContainer1 = (LinearLayout)findViewById(R.id.p31_supervise_container1);
		photoContainer2 = (LinearLayout)findViewById(R.id.p31_supervise_container2);
		
		mHomeBtn = (Button)findViewById(R.id.p31_supervise_camera_home_btn);
		//
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Max_X = display.getWidth();
		
		int moreLen = ScreenUtil.dp2px(this, 20);
		int centerLen = Max_X - moreLen;
		
		numsPerRow = centerLen / imgLen;
		
		int midLen = centerLen % imgLen;
		
		midTextLen = midLen / (numsPerRow - 1);//计算图片间距
		
		midLayoutParams = new LayoutParams(midTextLen ,88);//图片间距离Params,
		//处理相片列表
		openCameraBtn = new Button(this);
		openCameraBtn.setBackgroundResource(R.drawable.p31_select_photo_type_selector);
		//photoContainer1.addView(openCameraBtn, layoutParams);
		
	}
	
	private void setClickListener() {
		commitBtn.setOnClickListener(commitBtnListener);
		openCameraBtn.setOnClickListener(openCameraBtnListener);
		mHomeBtn.setOnClickListener(homeBtnListener);
	}
	
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		initView();
	}
	
	_Check checkObj;
	/**
	 * Skipped 88 frames!  The application may be doing too much work on its main thread.
	 */
	public void initView() {
		if(getIntent() != null && getIntent().getExtras() != null) {
			checkObj = (_Check) getIntent().getExtras().get("check");
			if(checkObj != null) {
				//工程信息
				//String rowid = checkObj.getaProjectId();
				//内容信息
				String mDefineText = checkObj.getCheckContent();
				if(mDefineText == null || "".equals(mDefineText)) {
					mDefineText = "正常";
				}
				editTextContent.setText(mDefineText);
				editTextContent.setSelection(editTextContent.getText().length());//光标移动最后
				//照片列表
				initPhotosList(checkObj.getId().toString());
			}
		} 
	}
	/**
	 * 初始化照片列表
	 * @param id
	 */
	public void initPhotosList(String id){
		final List<_LocalPhotos> photsList = new DBService(mContext).getPhotosList(id);
		if (photsList != null && photsList.size() > 0) {
			mDialog = new AlertDialog.Builder(this).create();
			mDialog.show();
			// 注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.common_loading_process_dialog);
			mDialog.setCanceledOnTouchOutside(false);

			new Thread(new Runnable() {
				@Override
				public void run() {
					if (imgBtnList != null) {
						imgBtnList.clear();
					}
					for (int i = 0; i < photsList.size(); i++) {
						_LocalPhotos p = photsList.get(i);
						ImageUtil utils = new ImageUtil();
						SoftReference<Bitmap> bmp = new SoftReference<Bitmap>(utils.getZoomBmpByDecodePath(p.getLocal_img_path(),123, 88));
						if (bmp.get() != null) {
							mCacheList.add(bmp);
							Drawable drawable = new BitmapDrawable(bmp.get());
							DefineButton btn = new DefineButton(mContext);
							btn.setBackgroundDrawable(drawable);
							btn.setImagePath(p.getLocal_img_path());
							btn.set$id(p.getId());
							imgBtnList.add(btn);
						}
					}
					sendMessage(Constansts.MES_TYPE_1);
				}
			}).start();
		} else {
			photoContainer1.addView(openCameraBtn, layoutParams);
		}
	}
	//提交
	OnClickListener commitBtnListener = new OnClickListener() {
		public void onClick(View v) {
				String content = editTextContent.getText().toString();
				if(content == null || "".equals(content)) {
					Toast.makeText(mContext, "请填写检查情况", Toast.LENGTH_SHORT).show();
					return;
				}
				if(imgBtnList == null || imgBtnList.size() == 0) {
					Toast.makeText(mContext, "请选择照片", Toast.LENGTH_SHORT).show();
					return;
				}
				if(checkObj != null) {
					//修改
					checkObj.setIsUpdate("1");
					checkObj.setCheckContent(content);
					//保存本地图片
					if(new DBService(mContext).updateCheck(checkObj)) {
						if(imgBtnList != null && imgBtnList.size() > 0) {
								boolean isInsertSuc = true;//数据插入成功标志
								new DBService(mContext).deletePhotos(checkObj.getId().toString());
								for(int i = 0; i < imgBtnList.size(); i ++) {
									DefineButton btn = imgBtnList.get(i);
									String path = btn.getImagePath();
									_LocalPhotos photos = new _LocalPhotos();
									photos.setLocal_img_path(path);
									photos.setCheck_tab_id(checkObj.getId().toString());
									boolean isSuc = new DBService(mContext).insertPhotos(photos);
									if(!isSuc) {
										isInsertSuc = false;
									}
								}
								if(!isInsertSuc){
									Toast.makeText(mContext, "数据没录入成功，请重新操作", Toast.LENGTH_SHORT).show();
								} else {
									clearPhotos();//清除照片列表+List缓存
									Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
								}
							} 
						} else {
							Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
						}
				} else {
					Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
				}
				setResult(111);
				finish();
		}
	};
	
	
	
 	File files;
 	String originCameraImgPath;
	OnClickListener openCameraBtnListener = new OnClickListener() {
		public void onClick(View v) {
			final MDialog dialogCategory = new MDialog(
					mContext, R.style.dialog);
			dialogCategory.createDialog(
					R.layout.supervice_open_camera_pop, 0.95,
					0.3, getWindowManager());
			
			Button openCamera = (Button)dialogCategory.findViewById(R.id.p31_open_camera_pop_btn);
			Button openAlbums = (Button)dialogCategory.findViewById(R.id.p31_open_albums_pop_btn);
			
			openCamera.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String sdStatus = Environment.getExternalStorageState();
					 if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
						 Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
			             return;
			         }
					String fileDir = Environment.getExternalStorageDirectory().getPath() + Constansts.CN_CAMERA_CHECK_PATH;
			        File file = new File(fileDir);
			        if(!file.exists()) {
			        	file.mkdirs();
			        }
			        String fileName = new Date().getTime()+".jpg";
			        originCameraImgPath = fileDir + fileName;
			        files = new File(originCameraImgPath);
			        if (!files.exists()) {  
			        	try {
							files.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}  
			        }
			        
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(files));
					startActivityForResult(intent, 3021);
					dialogCategory.dismiss();
				}
			});
			openAlbums.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, 3022);
					dialogCategory.dismiss();
				}
			});
		}
    };
    
    OnClickListener deleteImageListener = new OnClickListener() {
		public void onClick(View v) {
			dialog(v);
		}
    };
    
    protected void dialog(final View v) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DefineButton btn = (DefineButton)v;
						if (btn != null) {
							File file = new File(btn.getImagePath());
							if (file.exists()) {
								file.delete();
							}
						}
						imgBtnList.remove(btn);
						reComputeView();
			}

		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
    
    public String getPath(Uri uri) {  
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
		cursor.moveToFirst();
		return cursor.getString(column_index); 
    } 
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)   {
			if (files != null && files.exists()) {
				files.delete();
			}
			return;
		}
		
		 switch (requestCode) {
			case 3021://相机返回
			if (files != null && files.exists()) {
				SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(new ImageUtil().getZoomBmpByDecodePath(files.getPath(), 123, 88));
				mCacheList.add(bitmap);
				Drawable drawable = new BitmapDrawable(bitmap.get());
				computeView(drawable, originCameraImgPath);
			} else {
				Toast.makeText(mContext, "无法获取图片", Toast.LENGTH_SHORT).show();
			}
				break;
			case 3022://相册返回
				 Uri uri = data.getData();
				 ContentResolver cr = this.getContentResolver();
				 try {
					 final BitmapFactory.Options options = new BitmapFactory.Options();
					 options.inJustDecodeBounds = true;
					 BitmapFactory.decodeStream(cr.openInputStream(uri), null,options);
					 options.inSampleSize = new ImageUtil().calculateInSampleSize(options, 123, 88);
					 options.inJustDecodeBounds = false;
					 SoftReference<Bitmap> bmp2 = new SoftReference<Bitmap>(BitmapFactory.decodeStream(cr.openInputStream(uri), null, options));
					 mCacheList.add(bmp2);
					 Drawable drawable = new BitmapDrawable(bmp2.get());
					 computeView(drawable,getPath(uri));
				 } catch (Exception e) {
					 
				 }
				break;
			default:
				break;
			}
	}
	
	public void computeView(Drawable drawable,String imagePath) {
		if(imgBtnList != null && imgBtnList.size() < numsPerRow) {//如果第一行未排满
			photoContainer1.removeView(openCameraBtn);
			
			DefineButton btn = new DefineButton(mContext);
			btn.setBackgroundDrawable(drawable);
			btn.setImagePath(imagePath);
			
			btn.setOnClickListener(deleteImageListener);
			
			imgBtnList.add(btn);
			photoContainer1.addView(btn, layoutParams);
			
			TextView textView = new TextView(mContext);
			photoContainer1.addView(textView, midLayoutParams);
			
			if(imgBtnList.size() < 5) {
				if(imgBtnList.size() == numsPerRow) {
					photoContainer2.addView(openCameraBtn, layoutParams);
				} else {
					photoContainer1.addView(openCameraBtn, layoutParams);
				}
			}
		} else {//如果第一行排满了,适合小屏幕
				photoContainer2.removeView(openCameraBtn);
				
				DefineButton btn = new DefineButton(mContext);
				btn.setBackgroundDrawable(drawable);
				btn.setImagePath(imagePath);
				
				btn.setOnClickListener(deleteImageListener);
				
				imgBtnList.add(btn);
				
				photoContainer2.addView(btn, layoutParams);
				TextView textView = new TextView(mContext);
				photoContainer1.addView(textView, midLayoutParams);
				
				if(imgBtnList.size() < 5) {
					photoContainer2.addView(openCameraBtn, layoutParams);
				}
		}
	}
	
	public void reComputeView() {
		photoContainer1.removeAllViews();
		photoContainer2.removeAllViews();
		if(imgBtnList != null && imgBtnList.size() > 0) {
			for(int i = 0; i < imgBtnList.size(); i ++) {
				if(i < numsPerRow) {
					photoContainer1.addView(imgBtnList.get(i), layoutParams);
					TextView textView = new TextView(mContext);
					photoContainer1.addView(textView, midLayoutParams);
				} else {
					photoContainer2.addView(imgBtnList.get(i), layoutParams);
					TextView textView = new TextView(mContext);
					photoContainer2.addView(textView, midLayoutParams);
				}
			}
			if(imgBtnList.size() < 5) {
				if(imgBtnList.size() == numsPerRow) {
					photoContainer2.addView(openCameraBtn, layoutParams);
				} else {
					photoContainer1.addView(openCameraBtn, layoutParams);
				}
			}
		} else {
			photoContainer1.addView(openCameraBtn, layoutParams);
		}
	}
	/**
	 * 清除照片列表+List缓存
	 */
	public void clearPhotos() {
		photoContainer1.removeAllViews();
		photoContainer2.removeAllViews();
		photoContainer1.addView(openCameraBtn, layoutParams);
		editTextContent.setText("正常");
		editTextContent.setSelection(editTextContent.getText().length());//光标移动最后
		if(imgBtnList != null)
		imgBtnList.clear();
	}
	
	@Override
	protected void onDestroy() {
		if(timer != null) {
			timer.cancel();
		}
		if(mCacheList != null && mCacheList.size() > 0) {
			for(int i = 0; i < mCacheList.size(); i ++) {
				SoftReference<Bitmap> bmp = mCacheList.get(i);
				if(bmp != null) {
					if(bmp.get() != null)
					bmp.get().recycle();
					bmp.clear();
					System.gc();
				}
			}
		}
		super.onDestroy();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1:
			reComputeView();
			if(mDialog != null)
				mDialog.dismiss();
			break;
		case Constansts.MES_TYPE_2:
			if(mDialog != null)
				mDialog.dismiss();
			clearPhotos();
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//TODO
			finish();
		}
		return false;
	}
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
}
