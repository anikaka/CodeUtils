package com.tongyan.yanan.act.pic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicPhotoLevelAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.FileOpen;
import com.tongyan.yanan.common.utils.FileTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

/**
 * 照片列表界面
 * @author Administrator
 *
 */
public class PicPhotoLevelAct  extends FinalActivity implements OnClickListener,OnItemClickListener{

	//拍照
	@ViewInject(id=R.id.rlPhotograph)
	RelativeLayout mPhotograph;
	//照片列表
	@ViewInject(id=R.id.gridViewPic)
	GridView mGridViewImg;
	private Context mContext=this;
	private String mNewId; //上传Id
	private Bundle mBundle;
	private SharedPreferences mSP;
	private int mType;// 类型名称
	private static final int PIC_FLAG=0x91212;
	private   String mSDPath;
	private   String mPicDir="";//目录名称
	private   String mPicPath="";//照片路径
//	private   String  mInputPicName; //输入的文件名
	private   String mPhotoName="";	//照片名称
	private    Dialog mDialogPic;
	private   PicPhotoLevelAdapter mAdapter;
	private  ArrayList<HashMap<String, String>> mArrayList=new ArrayList<HashMap<String,String>>();
	private  ArrayList<HashMap<String, String>> mArrayListSQL=new ArrayList<HashMap<String,String>>();
	private static final int LAYOUT_ORIGINAL=0;//原地貌 
	private static final int LAYOUT_COMPACTION=1;//强夯处理
	private static final int LAYOUT_GUTTER=2;//盲沟
	private static final int LAYOUT_POINT_PHOTOT=3;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_photo_level);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		//获取SD卡根目录
		mSDPath=Environment.getExternalStorageDirectory()+"/";
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mNewId=mBundle.getString("newId");
			//窄化转型
			mType=Integer.parseInt(mBundle.getString("type"));
		}
		mPhotograph.setOnClickListener(this);
		mArrayList=new DBService(mContext).queryTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId);
		mAdapter=new PicPhotoLevelAdapter((mContext),mArrayList,R.layout.pic_photo_level_gridview_item);
		mGridViewImg.setAdapter(mAdapter);
		mGridViewImg.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (mType) {
		case  LAYOUT_ORIGINAL:
			mPicDir=mSDPath+"images/原地貌/";
			break;
		case LAYOUT_COMPACTION:
			mPicDir=mSDPath+"images/强夯处理/";
			break;
		case LAYOUT_GUTTER:
			mPicDir=mSDPath+"images/盲沟/";
			break;
		case LAYOUT_POINT_PHOTOT:
			mPicDir=mSDPath+"images/定点拍照/";
			break;
		default:
			break;
		}
		mPhotoName=String.valueOf(System.currentTimeMillis());//
		mPicPath=mPicDir+mPhotoName+".jpg";
		File file=new File(mPicDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		 File mFile=new File(mPicPath);
		if(!mFile.exists()){
			try {
				mFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri mUri=Uri.fromFile(mFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
		startActivityForResult(intent, PIC_FLAG); 
	}
	

	
	/**刷新列表中数据 */
	public void refresh(){
		if(mArrayList!=null){
			mArrayList.clear();
			mArrayListSQL=new DBService(mContext).queryTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId);
			mArrayList.addAll(mArrayListSQL);
			mArrayListSQL.clear();
		}
		mAdapter.notifyDataSetChanged();

		}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_CANCELED){
			return;
		}
//		if(resultCode==Activity.RESULT_OK || resultCode==Activity.RESULT_FIRST_USER){
			//照明命名对话框
			final Dialog mDialogPicName=new Dialog(mContext);
			mDialogPicName.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogPicName.setContentView(R.layout.pic_name_dialog);
			mDialogPicName.show();
			//确认按钮
			Button mButConfirm=(Button)mDialogPicName.findViewById(R.id.butPicConfirm);
			//取消按钮
			Button mButCancel=(Button)mDialogPicName.findViewById(R.id.buPicCancel);
			//
			final EditText mEditPicName=(EditText)mDialogPicName.findViewById(R.id.editPicName);
			/*
			 *1.获取用户输入的照片名称
			 *2.如果输入的照片名称为空,设置当前时间为照片名称的默认值 
			 */
			mButConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPhotoName=mEditPicName.getText().toString();
					if(!"".equals(mPhotoName)){
						File mFile=new File(mPicPath);
						mFile.renameTo(new File(mPicDir+mPhotoName+".jpg"));
					}
						//查询数据库是否已经存在该文件的信息,如果存在就不插入数据,否则就插入
						//将文件信息放入到数据库
						if(!new DBService(mContext).queryTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId,mPhotoName)){
							new DBService(mContext).insertTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), 
									mNewId,mPhotoName,"",
											(mPicDir+mPhotoName+".jpg"),DateTools.getDate(),DateTools.getTime());
							}
//						//引用重新指向空
//						mInputPicName=null;
						refresh();
					   mDialogPicName.cancel();
				}
			});
			
			//取消关闭对话框
			mButCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//查询数据库是否已经存在该文件的信息,如果存在就不插入数据,否则就插入
					//将文件信息放入到数据库
					if(!new DBService(mContext).queryTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId,mPhotoName)){
						new DBService(mContext).insertTablePic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), 
								mNewId,mPhotoName,"",
										(mPicDir+mPhotoName+".jpg"),DateTools.getDate(),DateTools.getTime());
						}
//					//引用重新指向空
//					mInputPicName=null;
					refresh();
					mDialogPicName.cancel();
				}
			});
//		}
		
	}

	/**
	 * <p>单张照片处理</p>
	 * </tr>对话框<tr>
	 * 1.照片名称修改
	 * 2.照片查看
	 * 3.照片删除
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicPhotoLevelAdapter.ViewHolderPicPhotoLevel mViewHolder=(PicPhotoLevelAdapter.ViewHolderPicPhotoLevel)view.getTag();
	   	final HashMap<String, String> mMap=mViewHolder.mMapPicPhotoLevel;
	    final File mFile=new File(mMap.get("picPath"));
	    mDialogPic=new Dialog(mContext);
	    mDialogPic.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogPic.setContentView(R.layout.pic_photo_level_dialog);
		mDialogPic.show();
		
		Button mButPicReName=(Button)mDialogPic.findViewById(R.id.butPicRename);
		Button mButPicFillView=(Button)mDialogPic.findViewById(R.id.butFillView);
		Button mButPicDel=(Button)mDialogPic.findViewById(R.id.butDeletePic);
		
		//照片命名
		mButPicReName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog mDialog=new Dialog(mContext,R.style.dialog);
				mDialog.setContentView(R.layout.pic_name_dialog);
				mDialog.show();				
			    final EditText mEditName=(EditText)mDialog.findViewById(R.id.editPicName);
				Button mButConfirm=(Button)mDialog.findViewById(R.id.butPicConfirm);
				Button mButCancel=(Button)mDialog.findViewById(R.id.buPicCancel);
				
				mButConfirm.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if("".equals(mEditName.getText().toString())){
							Toast.makeText(mContext, "照片名称不能为空", Toast.LENGTH_SHORT).show();
						}else{
							mDialog.dismiss();
							mDialogPic.cancel();
							if(new DBService(mContext).queryPic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId, mEditName.getText().toString())){
								Toast.makeText(mContext, "名称已存在", Toast.LENGTH_SHORT).show();
							}else{								
								new DBService(mContext).updatePicRename(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""),
										mNewId, mMap.get("picName"), mEditName.getText().toString());							
								refresh();
							}
						}
			
					}
				});
				mButCancel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mDialog.dismiss();
					}
				});
			}
		});
		//单个照片查看
		mButPicFillView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMap.size()>0){
					startActivity(FileOpen.getImageFileIntent(mFile));
					mDialogPic.cancel();
				}
			}
		});

	//照片删除
	mButPicDel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder=new Builder(mContext);
			builder.setMessage("确定删除吗?");
			builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDialogPic.cancel();
					new DBService(mContext).delPic(mSP.getString(Constants.PREFERENCES_INFO_USERID, ""), mNewId,mMap.get("picName"));
					mFile.delete();
					refresh();
				}
			});
			builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDialogPic.cancel();
				}
			});
			builder.show();
		}
	});
 	}
	
	@Override
	protected void onDestroy() {
		if (mAdapter.getCache()!=null&&mAdapter.getCache().size()>0) {
			HashMap<String,SoftReference<Bitmap>> mCacheList=mAdapter.getCache();
			Iterator  iterator=mCacheList.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry entry=(Map.Entry)iterator.next();
				SoftReference<Bitmap> bmp =(SoftReference<Bitmap>)entry.getValue();
				if(bmp.get()!=null){					
					bmp.get().recycle();
					bmp.clear();
					System.gc();
				}
			}
//			for(int i = 0; i < mCacheList.size(); i ++) {
//				SoftReference<Bitmap> bmp = mCacheList.get(i);
//				if(bmp != null) {
//					if(bmp.get() != null)
//					bmp.get().recycle();
//					bmp.clear();
//					System.gc();
//
//					}
//				}
		}
		super.onDestroy();
	}
}
