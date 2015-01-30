package com.tygeo.highwaytunnel.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.common.Common_Name;
import com.tygeo.highwaytunnel.common.ImageUtil;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.common.mapUtil;
import com.tygeo.highwaytunnel.widget.myImageView;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class PhotoShow extends Activity {
	Button bt1, bt2, bt3;

	public  String  photo_position=Environment.getExternalStorageDirectory().toString() + "/TYPIC"+"/"+StaticContent.task_name+"/"+StaticContent.BH_index_name+"/"+StaticContent.BH_p_name+"/";
	Map<String, SoftReference<Bitmap>> imageCache = new  HashMap<String, SoftReference<Bitmap>>();
	
	private myImageView mmyImageView;
	Gallery mGallery;
//	Context ct=(InfoApplication)getApplication();
	Bitmap bitmap;
	byte[] mContent;
	ArrayList<String> photo_type;
	String delimageURlString,pstrImgPath;
	String pid;
	String fileName,bh_id;
	String strImgPath ;
	ArrayList<String> phtot_name;
	String P_type="内";
	ImageAdapter imageAdapter;
	ArrayList<String> imagePathList;
	ArrayList<Bitmap> bitlist;
	private String tempImagePath = mapUtil.getSDPath() + "/TYPIC/";
	private String tempImageName ="P"+ new SimpleDateFormat("yyyyMMddHHmmss")
			.format(new Date()) + ".jpg";
	
	private static final int CAMERA_WITH_DATA = 3023;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.photoshow);
		InfoApplication.getinstance().addActivity(this);
		
		bitlist=new ArrayList<Bitmap>();
		phtot_name=new ArrayList<String>();
		photo_type=new ArrayList<String>();
		
//		Bitmap bitmap = BitmapFactory.decodeStream(InputStream);
		
		
		inint();
		
		imagePathList=new ArrayList<String>();
		imagePathList=getImagePath();
		imageAdapter = new ImageAdapter(PhotoShow.this, imagePathList);
	 	mGallery=(Gallery)findViewById(R.id.mygallery);
	 	
//	 	mGallery.set
		 initPhotoView(); 
		initOnClickListener();
		mGallery.setAdapter(imageAdapter);
		
		
		
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String photoURL = imagePathList.get(position);
				delimageURlString = photoURL;
				System.out.println(photoURL);
				String photoType =photo_type.get(position);
				
				setPhotoView(photoURL,photoType);
				mGallery.setPadding(5, 5, 5, 5);
				
			}
		});
	
		
		
	}
	
	public void inint() {
		bt1 = (Button) findViewById(R.id.backBtn);
		bt2 = (Button) findViewById(R.id.takepicBtn);
		bt3 = (Button) findViewById(R.id.delBtn);
		
	}

	private void initPhotoView() {
		mmyImageView = (myImageView) findViewById(R.id.photoimage);
		mmyImageView.setBorderWidth(10);
		mmyImageView.setColour(Color.GRAY);
		mmyImageView.setScaleType(ScaleType.FIT_XY);
		 if 
		 (imagePathList.size() > 0) {
		 delimageURlString=imagePathList.get(0);
		 String phototype=photo_type.get(0);
		 setPhotoView(delimageURlString, phototype);
		 }
	}

	private void initOnClickListener() {
		Button backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < bitlist.size(); i++) {
					if (bitlist.get(i)!=null) {

						bitlist.get(i).recycle();	
						System.gc();
					}
				}
				
				finish();
			}	
		});
		
		Button takepicBtn = (Button) findViewById(R.id.takepicBtn);
		takepicBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final AlertDialog.Builder messageDialog = new AlertDialog.Builder(
						PhotoShow.this);
				messageDialog
						.setMessage("选择拍照方式")
						.setPositiveButton("调用内部相机",
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										P_type="内";
//										photo_type.add("内");
										doTakePhoto();// 用户点击了从照相机获取
										
										}
								})
						.setNeutralButton("外部相机拍摄",
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										P_type="外";
										
										String pname = newPhoto("外");
								String fileName = strImgPath+"/"
										+ pname + ".jpg";
//								imagePathList=getImagePath();
								photo_type.add("外");
								phtot_name.add(pname);
								imageAdapter.lis.add(fileName);
								
								imageAdapter.notifyDataSetChanged();
								mGallery.setSelection(imageAdapter.lis
										.size() - 1);
								delimageURlString = fileName;
								mmyImageView
										.setImageResource(R.drawable.outphoto);
										
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
				messageDialog.create();
				messageDialog.show();
				
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				AlertDialog dialog=new AlertDialog.Builder(PhotoShow.this).setTitle("请选择").setPositiveButton("删除", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						
						int pos = -1;
						for (int i = 0; i < imageAdapter.lis
								.size(); i++) {
							if (imageAdapter.lis.get(i).equals(
									delimageURlString)){
								System.out.println(delimageURlString);
								pos = i;
								imageAdapter.lis.remove(i);
								System.out.println(i);
								deletePhoto(phtot_name.get(pos));
								break;
							}
						}
						// 更新视图
						imageAdapter.notifyDataSetChanged();
						if (imageAdapter.lis.size() < 1) {
							delimageURlString ="";
							mmyImageView
									.setImageResource(R.drawable.nophoto);
						} else {
							int newpos = pos;
							if (pos == imageAdapter.lis.size())// 删的是最后一个
							{
								newpos = pos - 1;
								delimageURlString = imageAdapter.lis
										.get(newpos);
							} else {
								newpos = pos;
								delimageURlString = imageAdapter.lis
										.get(newpos);
							}
							String phototype = photo_type.get(newpos);
							
							setPhotoView(delimageURlString, phototype);
						
						}
//						mmyImageView.setImageResource(R.drawable.nophoto);
					}
				}).setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
					}
				}).show();
				
			}
		});
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		 ContentResolver resolver = getContentResolver();
		switch (requestCode) {
		case CAMERA_WITH_DATA: {
			try {
				
				System.out.println("ok");
//				super.onActivityResult(requestCode, resultCode, data);
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				// 这样就不会内存溢出了
				Bitmap bitmap = BitmapFactory.decodeFile(pstrImgPath, options);
				
				bitlist.add(bitmap);
				System.out.println(pstrImgPath);
				
				
				DB_Provider.PhotoStore(fileName, "内", pstrImgPath);
//				imagePathList=getImagePath();
				imageAdapter.lis.add(pstrImgPath);
				photo_type.add("内");
				phtot_name.add(fileName);
				imageAdapter.notifyDataSetChanged();
				mGallery.setSelection(imageAdapter.lis.size()-1);
				delimageURlString = pstrImgPath;
				
				StaticContent.photocount+=1;
				StaticContent.photo_last_id="p"+pid;
//				phtot_name.add(fileName);
				mmyImageView.setImageBitmap(bitmap);
				
			    
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		}

		// if (requestCode == 1)
		// {
		// try
		// {
		// super.onActivityResult(requestCode, resultCode, data);
		// Bundle extras = data.getExtras();
		// bitmap = (Bitmap) extras.get("data");
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// mContent = baos.toByteArray();
		// } catch ( Exception e )
		// {
		// 
		// e.printStackTrace();
		// }
		// // 把得到的图片绑定在控件上显示
		// mmyImageView.setImageBitmap(bitmap);
		// }
		//
		//
		//
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		for (int i = 0; i < bitlist.size(); i++) {
			if (bitlist.get(i)!=null) {

				bitlist.get(i).recycle();	
			
			}
		}
	
	}
	protected void doTakePhoto() {
		try {
			strImgPath=photo_position;
			System.out.println(strImgPath); 
			pid=DB_Provider.GetMaxBHImageID();
			
			Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//存放照片的文件夹
							
			                fileName =pid+".jpg";
//			                phtot_name.add(fileName);
			                File out = new File(strImgPath);
			                
			                if (!out.exists()) {
			                	
			                	out.mkdirs();
			                	
			                }
			                out = new File(strImgPath, fileName);
			                
			                pstrImgPath = strImgPath + fileName;//该照片的绝对路径
				            
			                Uri uri = Uri.fromFile(out);
			                
			                
//				
//			File out = new File(tempImagePath, tempImageName);
//			if (!out.exists()) {
//				out.mkdirs();
//			}
//			System.out.println(tempImageName);
//			Uri uri = Uri.fromFile(out);
			imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			imageCaptureIntent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, 1);
			startActivityForResult(imageCaptureIntent, CAMERA_WITH_DATA);
			
			
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "doTakePhoto：e=" + e, Toast.LENGTH_LONG)
					.show();
		}
	}
	
	private void setPhotoView(String photoURL,String photo_type)
	{
		
		if (photo_type.equals(Common_Name.inphoto)) {
			if (!(new File(photoURL).exists())) {
				mmyImageView.setImageResource(R.drawable.delphoto);
			} else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize=2;
				ImageUtil util=new ImageUtil();
//				addBitmapToCache(photoURL);
				Bitmap	bm = BitmapFactory.decodeFile(photoURL, options);
				byte[] b =util.Bitmap2Bytes(bm);
				System.out.println("压缩前 :"+b.length);
//				bitlist.add(bm);
				Bitmap	b2=ThumbnailUtils.extractThumbnail(bm, 150, 200);
				byte[]s =util.Bitmap2Bytes(b2);
			 	 System.out.println("压缩后"+ s.length); 
				
//				Bitmap bt=getBitmapByPath(photoURL);
//								mmyImageView.setImageURI(Uri.parse(photoURL));
				mmyImageView.setImageBitmap(b2);
								
				bitlist.add(b2);
//				bt.recycle();
//				b2.recycle();
				
			}
			
		} else {
			mmyImageView.setImageResource(R.drawable.outphoto);
		} 
	}
	
	
	
	ArrayList<String> getImagePath() {
		
	    String sql="select pic_pistion,pic_name,type from BH_PIC where bh_id="+StaticContent.bh_id+"";
		
	    ArrayList<String> it = new ArrayList<String>();	
	    Cursor c=DB_Provider.dbHelper.query(sql);
	    int count = 1;
	    String bh_position="";
	    String bh_name="";
	    String bh_type="";
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				
				bh_position= c.getString(0);
				bh_name=c.getString(1);
				bh_type=c.getString(2);
				System.out.println("查询出的路径"+c.getString(0));
				System.out.println("查询出的照片名称"+c.getString(1));
				System.out.println("查询出的类型"+c.getString(2));
				it.add(bh_position); 
				phtot_name.add(bh_name);
				photo_type.add(bh_type);
				
				c.moveToNext();
			}
		}

		c.close();	
		DB_Provider.dbHelper.close();
		
		
	
		
		
		return it;
	}
	
	class ImageAdapter extends BaseAdapter {
		/* 声明变量 */
		Bitmap bm;
		int mGalleryItemBackground;
		private Context mContext;
		private List<String> lis;

		/* ImageAdapter的构造符 */
		public ImageAdapter(Context c, List<String> li) {
			mContext = c;
			lis = li;
		}
		
		/* 重写的方法getCount,传回图片数目 */
		public int getCount() {
			return lis.size();
		}

		/* 重写的方法getItem,传回position */
		public Object getItem(int position) {
			return position;
		}
			
		/* 重写的方法getItemId,传并position */
		public long getItemId(int position) {

			return position;
		}

		/* 重写方法getView,传并几View对象 */
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				// View的findViewById()方法也是比较耗时的，因此需要考虑中调用一次，之后用
				// View的getTag()来获取这个ViewHolder对象
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.photo_gallery,
						null);
				holder.myimageView = (myImageView) convertView
						.findViewById(R.id.gallery_image);
				holder.textView = (TextView) convertView
						.findViewById(R.id.gallery_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String photoType =photo_type 
					.get(position);
			String photoname =phtot_name
					.get(position);
			if (photoType.equals("内")) {
				
				String photoURL = lis.get(position);
				if ((new File(photoURL)).exists()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap newbitmap;
					addBitmapToCache(photoURL, options);
					Bitmap	bm =getBitmapByPath(photoURL);
					bitlist.add(bm);
					newbitmap=ThumbnailUtils.extractThumbnail(bm, 80, 80);
					bitlist.add(newbitmap);
//					 ByteArrayInputStream bas = new ByteArrayInputStream(mContent);
//					 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bais);
						// mContent = baos.toByteArray();
					
					
					holder.myimageView.setImageBitmap(newbitmap);
					holder.myimageView.setScaleType(ScaleType.FIT_XY);
					holder.myimageView.setBorderWidth(10);
					holder.myimageView.setColour(Color.LTGRAY);
					
					bm.recycle();
//					newbitmap.recycle();
				
					
				}
				holder.textView.setText(photoname);
			}
			
			else {
				holder.textView.setText(photoname + "(外)");
			}

			return convertView;
		}

	}
	
	// /**
	// * 用当前时间给取得的图片命名
	// *
	// */
	// private String getPhotoFileName() {
	// Date date = new Date(System.currentTimeMillis());
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
	// return dateFormat.format(date) + "." + Type.photoFormat;
	// }

	// 增加这样一个静态类，缓存一下，这样不用每次都重新加载
	final class ViewHolder {
		public myImageView myimageView;
		public TextView textView;
	}
	public String newPhoto(String type)
	{	
		String imageID= DB_Provider.GetMaxBHImageID();
		
		DB_Provider.PhotoStore("w"+imageID, type,"");
//		phtot_name.add(imageID);
//		photo_type.add(type);
		return imageID;
	}
	
	
	public void deletePhoto(String photoname)
	{
		int index=getIndex(photoname);
		String type=photo_type.get(index);
		System.out.println(type);
		DB_Provider.deletepic(photoname);
//		DataProvider_table.PhotoDelete(photoname);
		phtot_name.remove(index);
		photo_type.remove(index);
		
		
		if (type.equals("内")){
//			DateJCUtil.deleteFile(mapUtil.getSDPath()+"/"+DataProvider_table.getPhotoPath()+"/"+photoname+".jpg");
			deletePic(photo_position+photoname);
			System.out.println("删除：："+photo_position+photoname);
		}
	}
	private int getIndex(String photoname) {
		int i=-1;
		for (int j = 0; j < phtot_name.size(); j++) {
			if (phtot_name.get(j).equals(photoname)) {
				return j;
			}
		}
		return i;
	}
	public  void deletePic(String filepath) {
		try {
			File file=new File(filepath);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			System.out.println("删除文件异常:e=" + e.getMessage());
		}
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new
			// Intent(Tunnel_info.this,Project_configuration.class));
			for (int i = 0; i < bitlist.size(); i++) {
				if (!(bitlist.get(i)==null)) {
					bitlist.get(i).recycle();
					System.gc();
				}
				
			}
			
			finish();
			break;
			
		default:
			break;
		}

		return false;
	}

	 public void addBitmapToCache(String path, BitmapFactory.Options options) {
	        // 强引用的Bitmap对象
	        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
	        // 软引用的Bitmap对象
	        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
	        // 添加该对象到Map中使其缓存
	        imageCache.put(path, softBitmap);
	    }
	 
	 
//	    获取的时候，可以通过SoftReference的get()方法得到Bitmap对象。
	    public Bitmap getBitmapByPath(String path) {
	        // 从缓存中取软引用的Bitmap对象
	        SoftReference<Bitmap> softBitmap = imageCache.get(path);
	        // 判断是否存在软引用
	        if (softBitmap == null) {
	            return null;
	        }
	        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
	        Bitmap bitmap = softBitmap.get();
	        return bitmap;
	    }
	    
	
}
