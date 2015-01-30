package com.tygeo.highwaytunnel.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.TY.bhgis.Util.Image;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.NewphotoImageViewAdapter;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.newphotoentity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
	/**
	 *拍照管理
	 **/
public class NewphotoShow extends Activity {
	private Button backbtn, photobtn;
	ImageButton delbtn;
	ArrayList<newphotoentity> photoinfo;
	String strImgPath, fileName;// 文件名
	NewphotoImageViewAdapter imageadapter;
	GridView gridview;
	ProgressDialog pd;
	String pid, P_type, delimageURlString;
	String pstrImgPath;// 文件绝对路径
	Thread mThread;
	CheckBox checkbox;
	ImageView imv;
	ArrayList<newphotoentity> getcheckedbox;
	public static  ArrayList<String> checked;
	ArrayList<CheckBox> cb;
	Handler mHandler;
	String s = DB_Provider.GetTaskDate(StaticContent.update_id);
	String ss = DB_Provider.GetTaskUp_Down(StaticContent.update_id);
	public String photo_position = Environment.getExternalStorageDirectory()
			.toString()
			+ "/TYPIC"
			+ "/"
			+ StaticContent.task_name
			+ "/"
			+ s
			+ "/"
			+ ss
			+ "/"
			+ StaticContent.BH_index_name
			+ "/"
			+ StaticContent.BH_p_name + "/";
	Map<Integer, Boolean> map;
	private static final int CAMERA_WITH_DATA = 3023;
	ArrayList<Bitmap> bitlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.newphoto);
		bitlist = new ArrayList<Bitmap>();
		inint();
		
		
		photoinfo = getImagePath();
		checked=new ArrayList<String>();
		for (int i = 0; i < photoinfo.size(); i++) {
			checked.add(0+"");
		}
//		if (!(photoinfo.size() == 0)) {
//			pd = new ProgressDialog(NewphotoShow.this);
//			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			pd.setTitle("正在加载图片");
//			pd.show();
//		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {

				case 0:
					gridview.setAdapter(imageadapter);
//					pd.dismiss();
					System.out.println("OK");
					break;

				}

			};
		};

		Runnable runnable = new Runnable() {
			public void run() {

//				if (!(photoinfo.size() == 0)) {
					imageadapter = new NewphotoImageViewAdapter(photoinfo,
							NewphotoShow.this);
					mHandler.sendEmptyMessage(0);
//				}

			};

		};
		mThread = new Thread(runnable);
		mThread.start();

		initOnClickListener();
//		map = NewphotoImageViewAdapter.isSelected;
	}

	public void setview() {

	}

	public void inint() {
		backbtn = (Button) findViewById(R.id.photo_BackBtn);
		photobtn = (Button) findViewById(R.id.newphoto_photoBtn);
		delbtn = (ImageButton) findViewById(R.id.newphoto_del);
		checkbox = (CheckBox) findViewById(R.id.newphotocheckbox);
		gridview = (GridView) findViewById(R.id.photo_GridView);
		
	}

	private void initOnClickListener() {
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < bitlist.size(); i++) {
					if (bitlist.get(i) != null) {

						bitlist.get(i).recycle();
						System.gc();
					}
				}

				finish();
			}
		});
		delbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog dialog = new AlertDialog.Builder(NewphotoShow.this)
						.setTitle("确定删除选择项")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										// getcheckedbox=imageadapter.GetCheckedBOx();
										 int pos = -1;
										 int length=checked.size();
										 ArrayList<String> sc=new ArrayList<String>();
										 ArrayList<newphotoentity> sp=new ArrayList<newphotoentity>();
										 for (int i = 0; i < length;
										 i++) {
											 System.out.println(photoinfo.size());
											 
										 if (checked.get(i).equals(1+"")) {
										  sp.add(photoinfo.get(i));
										  
										 deletePhoto(photoinfo.get(i));
										 sc.add(checked.get(i));
										
										 System.out.println(photoinfo.get(i)+"isdel");
										 }
										 }
										 checked.remove(sc);
										 for (int j = 0; j < photoinfo.size(); j++) {
											 if (photoinfo.get(j).getFlag()==1) {
												 photoinfo.remove(photoinfo.get(j));
												 j--;
											 }
											 
										}
//									     photoinfo.remove(sp);
										
										 
									     System.out.println("大小   "+photoinfo.size());
										imageadapter.notifyDataSetChanged();
										
										checkbox.setChecked(false);
										 
										 
//										for (Iterator<Integer> keys = map
//												.keySet().iterator(); keys
//												.hasNext();) {
//											int key =keys.next();
//											boolean select =map.get(key);
//											System.out.println("键" + key + "="
//													+ "值" + select);
//											if (select) {
//												deletePhoto(photoinfo.get(key));
//												photoinfo.remove(photoinfo.get(key));
//												map.remove(key);
//											}
//										}
										
										System.out
												.println("photoinfo.size():: "
														+ photoinfo.size());
										// System.out.println("所 删除的索引"+
										// getcheckedbox.get(i).getIndex());
									

										// ArrayList<CheckBox>
										// cc=imageadapter.DoBox();
										// System.out.println(cc.size());
										// if (!(cc.size()==0)) {
										// for (int i = 0; i < cc.size(); i++) {
										// cb.get(i).setChecked(false);
										// }
										//
										// }
										 

									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).show();

			}
		});

		photobtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final AlertDialog.Builder messageDialog = new AlertDialog.Builder(
						NewphotoShow.this);
				messageDialog
						.setMessage("选择拍照方式")
						.setPositiveButton("调用内部相机",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										P_type = "内";
										// photo_type.add("内");
										doTakePhoto();// 用户点击了从照相机获取

									}
								})
						.setNeutralButton("外部相机拍摄",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										P_type = "外";

										String pname = newPhoto("外");
										String fileName = pname;
										// imagePathList=getImagePath();
										// photo_type.add("外");
										// phtot_name.add(pname);
										// imageAdapter.lis.add(fileName);

										// imageAdapter.notifyDataSetChanged();
										// mGallery.setSelection(imageAdapter.lis
										// .size() - 1);
										delimageURlString = fileName;
										newphotoentity c = new newphotoentity();
										
										c.setPhoto_type("外");
										c.setPhotoname(fileName);
										c.setPhotourl(photo_position + fileName);
										c.setFlag(0);
										checked.add(0+"");
										photoinfo.add(c);
//										map.put(photoinfo.size()-1, false);
//										if (photoinfo.size() == 1) {
//											imageadapter = new NewphotoImageViewAdapter(
//													photoinfo,
//													NewphotoShow.this);
//											gridview.setAdapter(imageadapter);
//											imageadapter.notifyDataSetChanged();
//										} else {
											imageadapter.notifyDataSetChanged();
//										}
										// mmyImageView
										// .setImageResource(R.drawable.outphoto);
										//
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
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					for (int i = 0; i < photoinfo.size(); i++) {
						photoinfo.get(i).setFlag(1);
//						map.put(i, true);
//						checked.set(i, 1+"");
						imageadapter.notifyDataSetChanged();
					}
				} else {
					for (int i = 0; i < photoinfo.size(); i++) {
						photoinfo.get(i).setFlag(0);
//						map.put(i, false);
//						checked.set(i, 1+"");
						imageadapter.notifyDataSetChanged();
					}
				}
			}
		});

	}

	ArrayList<newphotoentity> getImagePath() {

		String sql = "select pic_pistion,pic_name,type from BH_PIC where bh_id="
				+ StaticContent.bh_id + "";

		ArrayList<newphotoentity> it = new ArrayList<newphotoentity>();
		Cursor c = DB_Provider.dbHelper.query(sql);
		int count = 1;
		String bh_position = "";
		String bh_name = "";
		String bh_type = "";
		if (c.moveToFirst()) {
			ArrayList<newphotoentity> list = new ArrayList<newphotoentity>();
			for (int i = 0; i < c.getCount(); i++) {
				newphotoentity entity = new newphotoentity();
				bh_position = c.getString(0);
				entity.setPhotourl(bh_position);
				bh_name = c.getString(1);
				entity.setPhotoname(bh_name);
				bh_type = c.getString(2);
				entity.setPhoto_type(bh_type);
				System.out.println("查询出的路径" + c.getString(0));
				System.out.println("查询出的照片名称" + c.getString(1));
				System.out.println("查询出的类型" + c.getString(2));
				it.add(entity);
				// phtot_name.add(bh_name);
				// photo_type.add(bh_type);

				c.moveToNext();
			}
		}

		c.close();

		return it;
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
				// super.onActivityResult(requestCode, resultCode, data);

				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize = 2;
				// // 这样就不会内存溢出了
				// Bitmap bitmap = BitmapFactory.decodeFile(pstrImgPath,
				// options);
				//
				// bitlist.add(bitmap);
				// System.out.println(pstrImgPath);

				DB_Provider.PhotoStore(fileName, "内", pstrImgPath);
				// imagePathList=getImagePath();
				// imageAdapter.lis.add(pstrImgPath);
				// photo_type.add("内");
				// phtot_name.add(fileName);
				// imageAdapter.notifyDataSetChanged();
				// mGallery.setSelection(imageAdapter.lis.size()-1);
				// delimageURlString = pstrImgPath;

				StaticContent.photocount += 1;
				StaticContent.photo_last_id = "p" + pid;
				newphotoentity c = new newphotoentity();

				c.setPhoto_type("内");
				c.setPhotoname(fileName);
				c.setPhotourl(pstrImgPath);
				c.setFlag(0);
				checked.add(0+"");
				photoinfo.add(c);
//				map.put(photoinfo.size()-1, false);
//				if (photoinfo.size() == 1) {
//					imageadapter = new NewphotoImageViewAdapter(photoinfo,
//							NewphotoShow.this);
//					gridview.setAdapter(imageadapter);
					imageadapter.notifyDataSetChanged();
//				} else {
//					imageadapter.notifyDataSetChanged();
//				}
				// phtot_name.add(fileName);
				// mmyImageView.setImageBitmap(bitmap);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		}
	}

	protected void doTakePhoto() {
		try {
			strImgPath = photo_position;
			System.out.println(strImgPath);
			pid = DB_Provider.GetMaxBHImageID();

			Intent imageCaptureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 存放照片的文件夹

			fileName = pid;
			// phtot_name.add(fileName);
			File out = new File(strImgPath);

			if (!out.exists()) {
				
				out.mkdirs();

			}
			out = new File(strImgPath, fileName + ".jpg");

			pstrImgPath = strImgPath + fileName;// 该照片的绝对路径

			Uri uri = Uri.fromFile(out);

			//
			// File out = new File(tempImagePath, tempImageName);
			// if (!out.exists()) {
			// out.mkdirs();
			// }
			// System.out.println(tempImageName);
			// Uri uri = Uri.fromFile(out);
			imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			imageCaptureIntent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, 1);
			startActivityForResult(imageCaptureIntent, CAMERA_WITH_DATA);

		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "doTakePhoto：e=" + e, Toast.LENGTH_LONG)
					.show();
		}
	}

	public String newPhoto(String type) {
		String imageID = DB_Provider.GetMaxBHImageID();

		DB_Provider.PhotoStore("w" + imageID, type, photo_position + "w"
				+ imageID);
		// phtot_name.add(imageID);
		// photo_type.add(type);
		return "w" + imageID;
	}

	public void deletePhoto(newphotoentity dp) {

		String type = dp.getPhoto_type();
		System.out.println(type);
		DB_Provider.deletepic(dp.getPhotoname());
		// DataProvider_table.PhotoDelete(photoname);

		// if (type.equals("内")){
		// DateJCUtil.deleteFile(mapUtil.getSDPath()+"/"+DataProvider_table.getPhotoPath()+"/"+photoname+".jpg");
		deletePic(dp.getPhotourl() + ".jpg");
		System.out.println("删除：：" + photo_position + dp.getPhotoname());
		// }
	}

	public void deletePic(String filepath) {
		try {
			File file = new File(filepath);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			System.out.println("删除文件异常:e=" + e.getMessage());
		}

	}
}
