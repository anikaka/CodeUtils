package com.tongyan.activity.secamera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.GpsSectionImageAdapter;
import com.tongyan.common.db.DBService;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.widget.view.AsyncImageView;
/**
 * 
 * @Title: SectionCameraAct.java 
 * @author Rubert
 * @date 2014-9-10 上午10:14:24 
 * @version V1.0 
 * @Description: 工程订阅拍照-无网络
 */
public class SectionCameraNoNetAct extends AbstructCommonActivity{
	
	
	private Context mContext = this;
	//private _User mLocalUser;
	
	private Button mCameraBtn;
	
	private File files;
	private String originCameraImgPath;
 	boolean isFile = false;
 	private String mSectionId = null;
 	private String subId = null;
 	private Dialog mDialog = null;
 	
 	private ArrayList<HashMap<String, String>> mLocalImageList = null;
 	
 	private ArrayList<HashMap<String, String>> mImageList = new ArrayList<HashMap<String, String>>();
 	
 	private GpsSectionImageAdapter mAdapter;
 	
 	private GridView mGridView = null;
 	
 	//private SharedPreferences mPreferences = null;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_section_camera_list);
		mCameraBtn = (Button)findViewById(R.id.main_title_camera_btn);
		mGridView = (GridView)findViewById(R.id.gps_section_camera_gridview);
		
		mCameraBtn.setOnClickListener(mOpenCameraListener);
		
		//mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		//mLocalUser = mApplication.localUser;
		
		mAdapter = new GpsSectionImageAdapter(mContext, mImageList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(mListener);
		mGridView.setOnItemLongClickListener(mLongClickLinstener);
		
		Bundle mBundle = getIntent().getExtras();
		if(mBundle  != null) {
			HashMap<String, String> mCacheMap = (HashMap<String, String>)mBundle.get("CacheMap");
			subId = mCacheMap.get("subId");
			mSectionId = mCacheMap.get("secId");
			reloadListView();
			//mLocalImageList = new DBService(mContext).getSectionImageList(subId);
			//getPictureList(subId);
		}
		//reloadListView();
	}
	
	
	OnClickListener mOpenCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!FileUtils.isExistSDCard()) { // 检测sd是否可用
				Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
				return;
			}
			String mLocalPath = FileUtils.getSDCardPath() + Constansts.CN_CAMERA_NO_NET_GPS_PATH + "/" + subId + "/";
			File mFileDir = new File(mLocalPath);
			if (!mFileDir.exists()) {
				mFileDir.mkdirs();
			}
			String fileName = "图片_" +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
			originCameraImgPath = mLocalPath + fileName;
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
		}
	};
	
	
	public void showInitDialog() {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	OnItemClickListener mListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			GpsSectionImageAdapter.ViewHolder mViewHolder = (GpsSectionImageAdapter.ViewHolder)arg1.getTag();
			if(mViewHolder != null) {
				HashMap<String, String> map  = mViewHolder.mItemData;
				if(map != null) {
					mViewHolder.mContainer.setPath(map.get("local_img_path"), null, map.get("local_img_path"));
					
					if(mViewHolder.mContainer.isLoaded()) {
						imageDialog(map, mViewHolder.mContainer);
					}
					/*
					String path = map.get("path");
					if(null != path && !"".equals(path)) {
						path = Constansts.HTTP + mPreferences.getString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT) + "/" + path;
						//先从本地找
						HashMap<String, String> mRetomeIdMap = new DBService(mContext).getImageByRemoteId(map.get("fileId"));
						if(mRetomeIdMap != null && !"".equals(mRetomeIdMap.get("local_img_path"))) {
							File file = new File(mRetomeIdMap.get("local_img_path"));
							if(file.exists()) {
								mViewHolder.mContainer.setPath(file.getPath(), null, file.getPath());
							} else {
								//mViewHolder.mContainer.setPath(path, null);//remote
								getLoader(map);
								mCurrentImageView = mViewHolder.mContainer;
							}
						} else {
							//mViewHolder.mContainer.setPath(path, null);//remote
							getLoader(map);
							mCurrentImageView = mViewHolder.mContainer;
						}
					} else {
						imageDialog(map, mViewHolder.mContainer);
					}
				*/}
			}
		}
	};
	
	//private AsyncImageView mCurrentImageView = null;
	//private String mCurrentLocalImagePath = null;
	
/*	public void getLoader(final HashMap<String, String> map) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					String path = Constansts.HTTP + mPreferences.getString("ROUTE_OF_SERVICE", Constansts.SERVER_URL_IP_PORT) + "/" + map.get("path");
					InputStream mInputStream = HttpDownloader.getInputStreamByHttpClient(path);
					//用工程编号作为唯一文件目录
					String mProjectId = map.get("fileId");
					String mFileName = map.get("photo_name") + ".jpg";
					HashMap<String, String> mRetomeIdMap = new DBService(mContext).getImageByRemoteId(mProjectId);
					if (!FileComUtils.isExistSDCard()) { // 检测sd是否可用
						Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
						return;
					}
					String mLocalPath = Constansts.CN_CAMERA_GPS_PATH + "/" + subId + "/";
					File file = new File(mLocalPath + mFileName);
					if(file.exists()) {
						mFileName = map.get("photo_name") + "_" +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
					}
					File mNewFile = HttpDownloader.write2SDFromInput(mLocalPath, mFileName, mInputStream);
					if(mRetomeIdMap != null) {
						if(new DBService(mContext).updateImageLoaclInfo(mNewFile.getName(), mNewFile.getPath(), mRetomeIdMap.get("ID"))) {
							mCurrentLocalImagePath = mNewFile.getPath();
							sendMessage(MES_TYPE_4);
						}
					} else {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("section_id", mSectionId);
						map.put("new_id", subId);
						map.put("local_img_path", mNewFile.getPath());
						map.put("photo_name", mNewFile.getName());
						map.put("remote_img_id", mProjectId);
						if(new DBService(mContext).insertGpsRemotePhotos(map)) {
							mCurrentLocalImagePath = mNewFile.getPath();
							sendMessage(MES_TYPE_4);
						} 
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}*/
	
	
	/**
	 * 长按跳转系统图片浏览器 
	 */
	OnItemLongClickListener mLongClickLinstener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			GpsSectionImageAdapter.ViewHolder mViewHolder = (GpsSectionImageAdapter.ViewHolder)arg1.getTag();
			if(mViewHolder != null) {
				HashMap<String, String> map  = mViewHolder.mItemData;
				if(map != null) {
					/*String path = map.get("path");
					if(null != path && !"".equals(path)) {
						HashMap<String, String> mRetomeIdMap = new DBService(mContext).getImageByRemoteId(map.get("fileId"));
						if(mRetomeIdMap != null && !"".equals(mRetomeIdMap.get("local_img_path"))) {
							intentToImageBrowser(mRetomeIdMap.get("local_img_path"));
						} else {
							Toast.makeText(mContext, "请先加载图片", Toast.LENGTH_SHORT).show();
						}
					} else {*/
						intentToImageBrowser(map.get("local_img_path"));
					//}
				}
			}
			return false;
		}
	};
	
	public void intentToImageBrowser(String loaclPath) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		Uri mUri = Uri.parse("file://" + loaclPath);
		it.setDataAndType(mUri, "image/*");
		startActivity(it);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (files != null && files.exists() && !isFile) {
				files.delete();
			}
			 return;
		}
		if(requestCode == 3021) {
			if (files != null && files.exists()) {
				isFile = true;
				//先保存照片(/CnClient/gps/image/工程编号/图片名)
				//入库
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("section_id", mSectionId);
				map.put("new_id", subId);
				map.put("local_img_path", files.getPath());
				map.put("photo_name", files.getName());
				if(new DBService(mContext).insertGpsPhotos(map)) {
					reloadListView();
				} else {
					files.delete();
					Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	/**
	 * 本地未上传图片-单击操作提示框
	 * @param map
	 */
	private void imageDialog (final HashMap<String, String> map, final AsyncImageView view) {
		final Dialog mImageDialog = new Dialog(mContext, R.style.dialog);
		mImageDialog.show();
		mImageDialog.setContentView(R.layout.gps_section_image_pop);
		mImageDialog.setCanceledOnTouchOutside(false);
		
		Button loadingBtn = (Button)mImageDialog.findViewById(R.id.gps_section_iamge_loading);
		Button commitBtn = (Button)mImageDialog.findViewById(R.id.gps_section_iamge_commit);
		Button deleteBtn = (Button)mImageDialog.findViewById(R.id.gps_section_iamge_delete);
		Button renameBtn = (Button)mImageDialog.findViewById(R.id.gps_section_iamge_rename);
		commitBtn.setVisibility(View.GONE);
		
		loadingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mImageDialog.dismiss();
				String mLocalPath = map.get("local_img_path");
				if(mLocalPath != null) {
					view.setPath(mLocalPath, null, mLocalPath);
				} else {
					sendMessage(Constansts.MES_TYPE_3);
				}
			}
		});
		/*commitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mImageDialog.dismiss();
				upload(map);
			}
		});*/
		deleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mImageDialog.dismiss();
				delete(map);
			}
		});
		
		renameBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mImageDialog.dismiss();
				rename(map);
			}
		});
	}
	
	/**
	 * 重命名
	 */
	public void rename(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(mContext, R.style.dialog);
		dialog.show();
		dialog.setContentView(R.layout.gps_section_image_rename_pop);
		dialog.setCanceledOnTouchOutside(false);
		
		final EditText mEditText = (EditText)dialog.findViewById(R.id.rename_edittext);
		Button mSureBtn = (Button)dialog.findViewById(R.id.rename_sure);
		Button mCancelBtn = (Button)dialog.findViewById(R.id.rename_cancel);
		
		mSureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String s = mEditText.getText().toString();
				if(!"".equals(s)) {
					if(new DBService(mContext).updateSectionImageName(s, map.get("ID"))) {
						reloadListView();
					} else {
						Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, "请填写文件名", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
	}
	
	/**
	 * 重新加载页面
	 */
	public void reloadListView() {
		if(mImageList != null) {
			mImageList.clear();
			mLocalImageList = new DBService(mContext).getSectionImageList(subId);
			if(mLocalImageList != null) {
				mImageList.addAll(mLocalImageList);
			}
			mAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 删除
	 * @param map
	 */
	public void delete(final HashMap<String, String> map) {
		if(map != null) {
			if(new DBService(mContext).deleteImage(map.get("ID"))) {
				File file = new File(map.get("local_img_path"));
				if(file.exists()) {
					file.delete();
				}
				reloadListView();
				Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
			} else {
				sendMessage(Constansts.MES_TYPE_3);
			}
		} else {
			sendMessage(Constansts.MES_TYPE_3);
		}
	}
	
	
	/**
	 * 上传代码
	 * @param map
	 */
	/*public void upload(final HashMap<String, String> map) {
		showInitDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mLocalPath = map.get("local_img_path");
				if(mLocalPath != null) {
					File file = new File(mLocalPath);
					if(file.exists()) {
						ImageUtil utils = new ImageUtil();
						SoftReference<Bitmap> bmp2 = new SoftReference<Bitmap>(utils.getBmpByPath(file.getPath()));
						String changeStr = Base64.encodeToString(utils.Bitmap2Bytes(bmp2.get()), 0);
						Map<String, String> properties = new HashMap<String, String>();
						properties.put("publicKey", Constansts.PUBLIC_KEY);
						properties.put("userName", mLocalUser.getUsername());
						properties.put("Password", mLocalUser.getPassword());
						properties.put("img", changeStr);
						properties.put("fileName", map.get("photo_name"));
						properties.put("objid", subId);
						try {
							String strJ = WebServiceUtils.requestM(properties, Constansts.METHOD_OF_ATTACHMENTUPLOAD, mContext);
							 Map<String, String> mBackMap = new Str2Json().getSectionUpdateBack(strJ);
							if(mBackMap != null && "ok".equalsIgnoreCase(mBackMap.get("s"))) {
								if(new DBService(mContext).updateImageState("1", mBackMap.get("v"), map.get("ID"))) {
									sendMessage(Constansts.MES_TYPE_2);
								} else {
									sendMessage(Constansts.MES_TYPE_1);
								}
							} else {
								sendMessage(Constansts.MES_TYPE_1);
							}
						} catch (Exception e) {
							e.printStackTrace();
							sendMessage(Constansts.MES_TYPE_1);
						} 
					} else {
						sendMessage(Constansts.MES_TYPE_1);
					}
				} else {
					sendMessage(Constansts.MES_TYPE_1);
				}
			}
		}).start();
	}*/
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
	//private final static int MES_TYPE_4 = 0xAAAA1;
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			closeDialog();
			reloadListView();
			break;
		case Constansts.ERRER:
			closeDialog();
			reloadListView();
			break;
		case Constansts.NET_ERROR:
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			closeDialog();
			reloadListView();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			closeDialog();
			reloadListView();
			break;
		/*case Constansts.MES_TYPE_1 :
			closeDialog();
			Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_2 :
			closeDialog();
			reloadListView();
			Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
			break;*/
		/*case Constansts.MES_TYPE_3 :
			Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
			break;
		case MES_TYPE_4:
			if(mCurrentImageView != null) {
				mCurrentImageView.setPath(mCurrentLocalImagePath, null, mCurrentLocalImagePath);
			}
			break;*/
		default:
			break;
		}
	}
	
}
