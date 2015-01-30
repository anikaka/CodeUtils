package com.tygeo.highwaytunnel.activity.hidecheck;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.common.ImageUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HideCheckDevicePictureActivity extends Activity implements OnClickListener{

	private Context context = this;
	private Button backbtn, photobtn;
	private ImageButton delbtn;
	private String fileName;// 文件名
	private HideCheckPictureAdapter imageadapter;
	private GridView gridview;
	private CheckBox checkbox;
	public static  ArrayList<String> checked;
	private String id;
	private File files;			//存放照片的文件
	private String originCameraImgPath;  	  //存放照片的路径
	private static final int CAMERA_WITH_DATA = 3023;
	private static final int FILE_SELECT_CODE = 3024;
	private ArrayList<SoftReference<Bitmap>> mBitmapCacheList = new ArrayList<SoftReference<Bitmap>>();
	//用于格式化日期,作为日志文件名的一部分  
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String time;
    private ArrayList<String> imageList = new ArrayList<String>();
    
    private int i = 0;
	
    private HashMap<String, String> mCachePicMap = new HashMap<String, String>();
    private boolean isAllSelected = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hidecheck_record_pic);
		if(getIntent() != null && getIntent().getExtras() != null) {
			id = getIntent().getExtras().getString("id");
		}
		inint();
		backbtn.setOnClickListener(this);
		photobtn.setOnClickListener(this);
		delbtn.setOnClickListener(this);
		checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isAllSelected) {
					checkbox.setChecked(true);
					isAllSelected = true;
				} else {
					checkbox.setChecked(false);
					isAllSelected = false;
					mCachePicMap.clear();
				}
				refreshImageViewList();	
				}
		});
		imageadapter = new HideCheckPictureAdapter(context, imageList, R.layout.pohotoimagetype);
		gridview.setAdapter(imageadapter);
		refreshImageViewList();
	}
	/**
	 * 刷新
	 */
	public void refreshImageViewList(){
		ArrayList<String> list = DB_Provider.getHideCheckDevicePicPath(id);
		if(imageList != null){
			imageList.clear();
			if(list !=null){
				imageList.addAll(list);
			}
		}
		imageadapter.notifyDataSetChanged();
	}
	/**
	 * 相关控件关联
	 */
	public void inint() {
		backbtn = (Button) findViewById(R.id.photo_BackBtn);
		photobtn = (Button) findViewById(R.id.newphoto_photoBtn);
		delbtn = (ImageButton) findViewById(R.id.newphoto_del);
		checkbox = (CheckBox) findViewById(R.id.newphotocheckbox);
		gridview = (GridView) findViewById(R.id.photo_GridView);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.photo_BackBtn:
			finish();
			break;
		case R.id.newphoto_photoBtn:
			showPhotoDialog();
			break;
		case R.id.newphoto_del:
			AlertDialog dialog = new AlertDialog.Builder(HideCheckDevicePictureActivity.this)
			.setTitle("提示")
			.setMessage("是否删除？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							for(Map.Entry<String, String> m : mCachePicMap.entrySet()){
								DB_Provider.delHideCheckDevicePic(m.getKey());
//								DB_Provider.updateCheckRecordPicNum(id, -1);
							}
							if(mCachePicMap.size() == imageList.size()){
								checkbox.setChecked(false);
							}
							mCachePicMap.clear();
							refreshImageViewList();
						}

					})
			.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					}).show();
			
			break;
		default:
			break;
		}
		
	}
	/**
	 * 拍照
	 */
	private void showPhotoDialog(){
		final Dialog photoDialog=new Dialog(context);
		photoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		photoDialog.setContentView(R.layout.photo_form_dialog);
		photoDialog.show();
		
		Button btnCamera=(Button)photoDialog.findViewById(R.id.btnCamera);
		Button btnAlbum = (Button) photoDialog.findViewById(R.id.btnAlbum);
		
		btnCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCamera();
				if(photoDialog != null){
					photoDialog.cancel();
				}
			}
		});
		
		btnAlbum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFileChooser();
				if(photoDialog != null){
					photoDialog.cancel();
				}
			}
		});
		
	}
	/**
	 * 文件夹中选择照片
	 */
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(getApplicationContext(), "请安装文件管理器", Toast.LENGTH_SHORT)
					.show();
		}
	}
	/**
	 * 打开系统照相机并拍照
	 */
	private void openCamera() {
		String sdStatus = Environment.getExternalStorageState();
		 if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			 Toast.makeText(context, "SD卡不可用", Toast.LENGTH_SHORT).show();
          return;
		 }
		 String fileDir = Environment.getExternalStorageDirectory().getPath() + "/TYPIC"+ "/hideCheckDevicePic/"+ id+"/";
		 File file = new File(fileDir);
		 if(!file.exists()){
			 file.mkdirs();
		 }
		 Date date = new Date();
		 time = formatter.format(date);
		 fileName = date.getTime()+".jpg";
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
			// 存放照片的文件夹
		intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(files));
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (files != null && files.exists()) {
				files.delete();
			}
			return;
		}
		if(CAMERA_WITH_DATA == requestCode) {
			try {
				if (files.exists()) {
					UUID uuid = UUID.randomUUID();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("deviceRecordId", id);
					map.put("picName", fileName);
					map.put("picPath", originCameraImgPath);
					map.put("picDate", time);
					map.put("uploadState", "0");
					map.put("guid", String.valueOf(uuid));
					DB_Provider.insertHideCheckDevicePic(map);
//					DB_Provider.updateCheckRecordPicNum(id, 1);
					refreshImageViewList();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(FILE_SELECT_CODE == requestCode){
			Uri uri = data.getData();
			String url;
			url = getPath(uri);
			if(url != null || !"".equals(url)){
				UUID uuid = UUID.randomUUID();
				HashMap<String, String> map = new HashMap<String, String>();
				String fileName = null;
				if (fileName == null || fileName == ""){
					fileName = url.substring(url.lastIndexOf("/") + 1);
				}
				
				String type = getFileType(fileName);
				if(isImage(type)){
					map.put("checkRecordId", id);
					map.put("picName", fileName);
					map.put("picPath", url);
					map.put("picDate", formatter.format(new Date()));
					map.put("uploadState", "0");
					map.put("guid", String.valueOf(uuid));
					DB_Provider.insertHidecheckpic(map);
					DB_Provider.updateCheckRecordPicNum(id, 1);
					refreshImageViewList();
				}else{
					Toast.makeText(getApplicationContext(), "请选择图片文件", Toast.LENGTH_LONG).show();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 获得图片地址
	 * @param uri
	 * @return
	 */
	private String getPath(Uri uri){
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if(cursor != null){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}
	
	/**
     * 获取文件后缀名
     *
     * @param fileName
     * @return 文件后缀名
     */
    public static String getFileType(String fileName) {
            if (fileName != null) {
                    int typeIndex = fileName.lastIndexOf(".");
                    if (typeIndex != -1) {
                            String fileType = fileName.substring(typeIndex + 1)
                                            .toLowerCase();
                            return fileType;
                    }
            }
            return "";
    }
    /**
     * 根据后缀名判断是否是图片文件
     *
     * @param type
     * @return 是否是图片结果true or false
     */
    public static boolean isImage(String type) {
            if (type != null
                            && (type.equals("jpg") || type.equals("gif")
                                            || type.equals("png") || type.equals("jpeg")
                                            || type.equals("bmp") || type.equals("wbmp")
                                            || type.equals("ico") || type.equals("jpe"))) {
                    return true;
            }
            return false;
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		clearBitmap();
	}
	/**
	 * 回收图片
	 */
	public void clearBitmap() {
		if(mBitmapCacheList != null && mBitmapCacheList.size() > 0) {
			for(int i = 0; i < mBitmapCacheList.size(); i ++) {
				SoftReference<Bitmap> bmp = mBitmapCacheList.get(i);
				if(bmp != null) {
					if(bmp.get() != null)
					bmp.get().recycle();
					bmp = null;
				}
			}
			mBitmapCacheList.clear();
		}
	}
	
	public class HideCheckPictureAdapter extends BaseAdapter {
		
		private LayoutInflater mLayoutInflater;
		private ArrayList<String> list;
		private Context mContext;
		private int mResource;
		private int j = 0;
		
		public HideCheckPictureAdapter( Context context,ArrayList<String> mList,int resource ) {
			this.mContext = context;
			this.list = mList;
			this.mResource = resource;
			this.mLayoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder mViewHolder = null;
			if(arg1 == null) {
				arg1 = mLayoutInflater.inflate(mResource, null);
				mViewHolder = new ViewHolder();
				mViewHolder.text=(TextView)arg1.findViewById(R.id.photoimagetextview);
				mViewHolder.checkbox = (CheckBox) arg1.findViewById(R.id.photoimagecheckbox);
				mViewHolder.imageview = (ImageView) arg1.findViewById(R.id.phototypeimageview);
				arg1.setTag(mViewHolder);
			}else{
				mViewHolder = (ViewHolder)arg1.getTag();
			}
			final String mLocalPath = list.get(arg0);
			SoftReference<Bitmap> bmp = new SoftReference<Bitmap>(new ImageUtil().getZoomBmpByDecodePath(mLocalPath,dp2px(mContext, 180),dp2px(mContext, 120)));
			mBitmapCacheList.add(bmp);
			Drawable drawable = new BitmapDrawable(bmp.get());
			mViewHolder.imageview.setBackgroundDrawable(drawable);
			mViewHolder.imageview.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//用系统图片浏览器
					Intent it = new Intent(Intent.ACTION_VIEW);
					Uri mUri = Uri.parse("file://" + mLocalPath);
					it.setDataAndType(mUri, "image/*");
					mContext.startActivity(it); 
				}
			});
			
			if(isAllSelected) {
				mViewHolder.checkbox.setChecked(true);
			} else {
				mViewHolder.checkbox.setChecked(false);
			}
			
			mViewHolder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							mCachePicMap.put(mLocalPath, mLocalPath);						
						}else{
							mCachePicMap.remove(mLocalPath);
					    }
						if(mCachePicMap.size() == list.size()) {
							checkbox.setChecked(true);
						} else {
							checkbox.setChecked(false);
						}
				}
			});
			
			
			return arg1;
		}

		public final class ViewHolder {
			public TextView text;
			public CheckBox checkbox;
			public ImageView imageview;
		}
		public int px2dp(Context context,float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int)(pxValue/scale+0.5f);
		}
		
		public int dp2px(Context context,float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int)(dpValue*scale+0.5f);
		}
	}


}
