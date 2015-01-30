package com.tongyan.activity.risk;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.RiskSettingAdapter;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._HoleFace;
import com.tongyan.common.entities._HolefaceSettingInfo;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.common.entities._LocRisk;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.utils.ImageUtil;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.ScreenUtil;
import com.tongyan.widget.view.AsyncImageView;
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
import android.text.InputType;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @ClassName P36_HoleCheckAct.java
 * @Author wanghb
 * @Date 2013-9-3 pm 08:33:17
 * @Desc 掌子面(Hole face)
 */
public class P36_HoleCheckAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private EditText mHoleMileEditText,mEditContent;
	
	private TextView mSuggest,mMsuggest,mTextContent,mHoleMileText;
	private String mSuggestNum,mRiskNum;
	
	private Button mHomeBtn,mHoleInfoSaveBtn,mMDegreeBtn;//,mHoleMudOutBtn,mHoleDropBtn
	
	private ListView mListView;
	
	private _HoleFace mHoleFace = null;
	//part of Camera 
	private Button mOpenCameraBtn;
	private Dialog mDialog;
	private List<DefineButton> mImageBtnList = new ArrayList<DefineButton>();
	private int mScreenWidth;
	private int mScreenHeight;
	private int mNumsPerRow;
	private LayoutParams mLayoutParams = new LayoutParams(123, 88);
	private LayoutParams mMidLayoutParams = null;
	public int mDeviceDistance;
	private int mImageLen = 123;
	private int mMidTextLen;
	
	private LinearLayout mPhotoContainer1;
	private LinearLayout mPhotoContainer2;
	
	private List<SoftReference<Bitmap>> mCacheList = new ArrayList<SoftReference<Bitmap>>();	
	private RiskSettingAdapter mAdapter;
	private List<String>  mRiskTypes;
	
	private String $holefaceId;
	private String $riskId;
	//private String $riskType;
	
	_LocRisk mLocalRisk;
	private MDialog mDegreeDialog = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			$holefaceId = (String) getIntent().getExtras().get("$id");       //HolefaceId
			$riskId = (String) getIntent().getExtras().get("$rowId"); //RiskId
			mRiskTypes = new DBService(this).findHolefaceSettingGroupByType();// Maybe,It has some errors //TODO
		}
		
		mLocalRisk = new DBService(this).selectRiskOne($riskId);
		
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		
		if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
			setContentView(R.layout.risk_camera);
			mHoleInfoSaveBtn = (Button)findViewById(R.id.p36_risk_tunnel_save_btn);
			mEditContent  = (EditText)findViewById(R.id.p36_risk_propose_edittext);
			mHoleMileEditText = (EditText)findViewById(R.id.p36_risk_tunnel_mile_edittext);
			mHoleMileEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
			mHoleMileEditText.setCursorVisible(false);//去光标
		} else {
			setContentView(R.layout.risk_camera_b);
			mTextContent  = (TextView)findViewById(R.id.p36_risk_propose_edittext);
			mHoleMileText = (TextView)findViewById(R.id.p36_risk_tunnel_mile_edittext);
		}
		((MyApplication)getApplication()).addActivity(this);
		
		/*mHoleMudOutBtn = (Button)findViewById(R.id.p36_risk_out_mud_setting_btn);
		mHoleDropBtn = (Button)findViewById(R.id.p36_risk_bad_building_setting_btn);*/
		mHomeBtn = (Button)findViewById(R.id.p36_risk_tunnel_home_btn);
		mListView = (ListView)findViewById(R.id.p36_risk_settings_listview);
		
		mPhotoContainer1 = (LinearLayout)findViewById(R.id.p36_risk_container1);
		mPhotoContainer2 = (LinearLayout)findViewById(R.id.p36_risk_container2);
		
		
		mMDegreeBtn = (Button)findViewById(R.id.p36_risk_degree_btn);
		mSuggest = (TextView)findViewById(R.id.p36_risk_propose_degree_text);
		mMsuggest = (TextView)findViewById(R.id.p36_risk_degree_text);
		
		
		initParams();
	}
	
	private void initParams() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		int moreLen = ScreenUtil.dp2px(this, 20);
		int centerLen = mScreenWidth - moreLen;
		
		mNumsPerRow = centerLen / mImageLen;
		
		int midLen = centerLen % mImageLen;
		
		mMidTextLen = midLen / (mNumsPerRow - 1);//计算图片间距
		
		
		mMidLayoutParams = new LayoutParams(mMidTextLen ,//图片间距离Params,
				88);
		//处理相片列表
		mOpenCameraBtn = new Button(this);
		mOpenCameraBtn.setBackgroundResource(R.drawable.p31_select_photo_type_selector);
	}
	/**
	 * 测量当前分辨率手机的初始化Button控件的高度
	 * @return
	 */
	public int measureButtonHeight(){
		Button mButton = new Button(this);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mButton.measure(w, h);	
		return mButton.getMeasuredHeight();
	}
	
	
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(mHomeBtnListener);
		//mHoleMudOutBtn.setOnClickListener(mHoleMudOutBtnListener);
		mOpenCameraBtn.setOnClickListener(mOpenCameraBtnListener);
		
		if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
			mHoleInfoSaveBtn.setOnClickListener(mHoleInfoSaveBtnListener);
			mHoleMileEditText.setOnTouchListener(mHoleMileTouchListener);
			mMDegreeBtn.setOnClickListener(mDegreeBtnListener);
		}
		mListView.setOnItemClickListener(mListViewItemListener);
	} 
	
	private void businessM(){
			initPhotosList($holefaceId);
			
			mAdapter = new RiskSettingAdapter(this, mRiskTypes, $holefaceId, $riskId);
			
			mListView.setAdapter(mAdapter);
			
			if($holefaceId != null) {
				mHoleFace = new DBService(this).getOneHoloFace($holefaceId);
				if(mHoleFace != null) {
					if(mHoleMileEditText != null) {
						mHoleMileEditText.setText(mHoleFace.getCurrMile());
					    mHoleMileEditText.setSelection(mHoleMileEditText.getText().length());
					}
					if(mHoleMileText != null) {
						mHoleMileText.setText(mHoleFace.getCurrMile());	
					}
					mSuggest.setText(mHoleFace.getProposeDegree() + "级");
					mMsuggest.setText(mHoleFace.getRiskDegree()+ "级");
					mSuggestNum = mHoleFace.getProposeDegree();
					mRiskNum = mHoleFace.getRiskDegree();
					if(mEditContent != null)
					mEditContent.setText(mHoleFace.getRiskHSuggest());
					if(mTextContent != null)
					mTextContent.setText(mHoleFace.getRiskHSuggest());
				}
			} else {
				Toast.makeText(mContext, "数据获取失败，请重试", Toast.LENGTH_SHORT).show();
			}
	}
	
	/**
	 * 初始化照片列表
	 * @param id
	 */
	public void initPhotosList(final String id){
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<_LocalPhotos> photsList = new DBService(mContext).selectHolefacePhotos(id);//TODO
				if(photsList != null && photsList.size() > 0) {
					if(mImageBtnList != null) {
						mImageBtnList.clear();
					}
					for(int i = 0; i < photsList.size(); i ++) {
						_LocalPhotos p = photsList.get(i);
		                ImageUtil utils = new ImageUtil();
		                
						SoftReference<Bitmap> bmp = new SoftReference<Bitmap>(utils.getZoomBmpByDecodePath(p.getLocal_img_path(),123,88));
						if(bmp.get() != null) {
							mCacheList.add(bmp);
							Drawable drawable = new BitmapDrawable(bmp.get());
							//Drawable drawable = new BitmapDrawable(bmp);
							DefineButton btn = new DefineButton(mContext);
							btn.setBackgroundDrawable(drawable);
							btn.setImagePath(p.getLocal_img_path());
							btn.set$id(p.getId());
							mImageBtnList.add(btn);
						}
					}
				} 
				sendMessage(Constansts.SUCCESS);
			}
		}).start();
	}
	
	
	//====================================================================
	// Part of OnClickListener
	//====================================================================
	OnTouchListener mHoleMileTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			mHoleMileEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			mHoleMileEditText.setCursorVisible(true);
			return false;
		}
	};
	
	OnClickListener mDegreeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			double val = (double)(4 * measureButtonHeight() + ScreenUtil.dp2px(mContext, 60))/(double)mScreenHeight;
			if(val == 0) {
				val = 0.3;
			}
			mDegreeDialog = new MDialog(mContext, R.style.dialog);
			mDegreeDialog.createDialog(R.layout.risk_holo_face_pop, 0.9, val, getWindowManager());
			mDegreeDialog.setCanceledOnTouchOutside(false);
			LinearLayout container = (LinearLayout)mDegreeDialog.findViewById(R.id.p35_risk_holo_face_container);
			DefineButton mFButton = new DefineButton(mContext);
			mFButton.setText("1级");
			mFButton.setTextColor(getResources().getColor(R.color.white));
			mFButton.setmClassTypeNum("1");
			mFButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
			mFButton.setOnClickListener(mDegreeClickBtnListener);
			container.addView(mFButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
			
			TextView textView1 = new TextView(mContext);
			container.addView(textView1, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,5));
			
			DefineButton mGButton = new DefineButton(mContext);
			mGButton.setText("2级");
			mGButton.setTextColor(getResources().getColor(R.color.white));
			mGButton.setmClassTypeNum("2");
			mGButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
			mGButton.setOnClickListener(mDegreeClickBtnListener);
			container.addView(mGButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
			
			TextView textView2 = new TextView(mContext);
			container.addView(textView2, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,5));
			
			DefineButton mHButton = new DefineButton(mContext);
			mHButton.setText("3级");
			mHButton.setTextColor(getResources().getColor(R.color.white));
			mHButton.setmClassTypeNum("3");
			mHButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
			mHButton.setOnClickListener(mDegreeClickBtnListener);
			container.addView(mHButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
			
			TextView textView3 = new TextView(mContext);
			container.addView(textView3, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,5));
			
			DefineButton mIButton = new DefineButton(mContext);
			mIButton.setText("4级");
			mIButton.setTextColor(getResources().getColor(R.color.white));
			mIButton.setmClassTypeNum("4");
			mIButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
			mIButton.setOnClickListener(mDegreeClickBtnListener);
			container.addView(mIButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
		}
	};
	/**
	 * return main  page
	 * @target click
	 */
	OnClickListener mHomeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext, MainAct.class);
			startActivity(intent);
		}
	};
	/**
	 * my degree select button click listener
	 * @target click
	 */
	OnClickListener mDegreeClickBtnListener = new OnClickListener() {
		public void onClick(View v) {
			DefineButton mDefineBtn = (DefineButton)v;
			mMsuggest.setText(mDefineBtn.getmClassTypeNum() + "级");
			mRiskNum = mDefineBtn.getmClassTypeNum();
			if("1".equals(mDefineBtn.getmClassTypeNum())) {
				mEditContent.setText(getResources().getString(R.string.p36_risk_1));
			} else if("2".equals(mDefineBtn.getmClassTypeNum())) {
				mEditContent.setText(getResources().getString(R.string.p36_risk_2));
			} else if("3".equals(mDefineBtn.getmClassTypeNum())) {
				mEditContent.setText(getResources().getString(R.string.p36_risk_3));
			} else {
				mEditContent.setText(getResources().getString(R.string.p36_risk_4));
			}
			if(mDegreeDialog != null) {
				mDegreeDialog.dismiss();
			}
		}
	};
	
	/**
	 * save the page data
	 * @target click
	 */
	OnClickListener mHoleInfoSaveBtnListener = new OnClickListener() {
		public void onClick(View v) {
			String mMSuggestContent = mEditContent.getText().toString();
			//I//Mile
			String mMileText = mHoleMileEditText.getText().toString();
			if(mMileText == null || "".equals(mMileText)) {
				Toast.makeText(mContext, "请填写里程", Toast.LENGTH_SHORT).show();
				return;
			}
			//II//to select the table of LOCAL_HOLO_SETTING_INFO and loop isCheck by riskId
			//if (isCheck == 1)  return true; else return false; 
			List<_HolefaceSettingInfo> mSettingInfoList = new DBService(mContext).getHolefaceSettingInfoByRiskId($riskId,$holefaceId);
			if(mSettingInfoList != null && mSettingInfoList.size() > 0) {
				boolean isCheck = true;
				for(int i = 0; i < mSettingInfoList.size(); i ++) {
					if(null != mSettingInfoList.get(i) && "0".equals(mSettingInfoList.get(i).getIsCheck())) {
						isCheck = false;
						break;
					}
				}
				if(!isCheck) {
					Toast.makeText(mContext, "请检查风险", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			//III//handler the photos
			if(mImageBtnList == null || mImageBtnList.size() <= 0) {
				Toast.makeText(mContext, "请选择照片", Toast.LENGTH_SHORT).show();
				return;
			} else {
				boolean isInsertSuc = true;//数据插入成功标志
				new DBService(mContext).deleteHolefacePhotos($holefaceId);
				_LocRisk risk = new DBService(mContext).selectRiskOne($riskId);
				for(int i = 0; i < mImageBtnList.size(); i ++) {
					DefineButton btn = mImageBtnList.get(i);
					String path = btn.getImagePath();
					_LocalPhotos photos = new _LocalPhotos();
					photos.setLocal_img_path(path);
					photos.setCheck_tab_id($holefaceId);
					if(risk != null) {
						photos.setCheckId(risk.getRowId());
					}
					photos.set$riskId($riskId);
					boolean isSuc = new DBService(mContext).insertHolefacePhotos(photos);
					if(!isSuc) {
						isInsertSuc = false;
					}
				}
				
				if(!isInsertSuc){
					Toast.makeText(mContext, "数据没录入成功，请重新操作", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			//IV//suggest content
			if(null == mMSuggestContent || "".equals(mMSuggestContent.trim())) {
				Toast.makeText(mContext, "请填写风险建议", Toast.LENGTH_SHORT).show();
				return;
			}
			
			new DBService(mContext).updateHoleface($holefaceId, "1", mMileText, mSuggestNum, mRiskNum, mMSuggestContent);
			new DBService(mContext).updateRisk($riskId, "2");//正检查
			Intent intent = new Intent(mContext, P35_RiskTaskListAct.class);
			intent.putExtra("$riskId", $riskId);
			setResult(402, intent);
			finish();
			Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
		}
	};
	
	OnClickListener seeImageListener = new OnClickListener() {
		public void onClick(View v) {
			MDialog dialogCategory = new MDialog(mContext, R.style.dialog);
			dialogCategory.createDialog(R.layout.supervice_see_pop, 0.95,0.95, getWindowManager());
			dialogCategory.setCanceledOnTouchOutside(false);
			AsyncImageView view = (AsyncImageView)dialogCategory.findViewById(R.id.p33_see_supervice_p);
			DefineButton btn = (DefineButton)v;
			ImageUtil utils = new ImageUtil();
			
			SoftReference<Bitmap> bmpi = new SoftReference<Bitmap>(utils.getBmpByPath(btn.getImagePath()));
			mCacheList.add(bmpi);
			
			Drawable drawable = new BitmapDrawable(bmpi.get());
			view.setBackgroundDrawable(drawable);
		}
    };
	
    OnLongClickListener deleteImageListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			dialog(v);
			return false;
		}
    };
	
	//====================================================================
	// Part of OnItemClickListener
	//====================================================================
	OnItemClickListener mListViewItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RiskSettingAdapter.ViewHolder viewHolder = (RiskSettingAdapter.ViewHolder)arg1.getTag();
			if(viewHolder != null) {
				Intent intent = null;
				
				if(mLocalRisk != null && "1".equals(mLocalRisk.getIsFinish())){
					intent = new Intent(mContext,P37B_RiskCollapseAct.class);
				} else {
					intent = new Intent(mContext,P37A_RiskCollapseAct.class);
				}
				
				intent.putExtra("$holefaceId", viewHolder.$holefaceId); //HolefaceId
				intent.putExtra("$riskId", viewHolder.$riskId);         //RiskId
				intent.putExtra("$riskType", viewHolder.mSettingName);
				//$riskType = viewHolder.mSettingName;
				startActivityForResult(intent, 123);
			} else {
				Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * LOCAL_RISK_SETTINGS_RECORD ===>> LOCAL_HOLO_SETTING_INFO ===>> LOCAL_HOLO_FACE /// LOCAL_RISK
	 */
	
	boolean isFile = false;//sometime someone open camera firstly and check risk last,so we must be sign the file of picture
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 123 || resultCode == 0) {
			//compute degree 
			int mOneD = 0; 
			int mTwoD = 0;
			int mThreeD = 0;
			int mFourD = 0;
			int mMDegree = 0;
			List<_HolefaceSettingRecord> mHolefaceRecordsList = new DBService(this).getHolefaceRecordsByAllParams($riskId, $holefaceId, null, null);
			if(mHolefaceRecordsList != null) {
				for(int i = 0; i < mHolefaceRecordsList.size(); i ++) {
					_HolefaceSettingRecord mRecord = mHolefaceRecordsList.get(i);
					if(mRecord != null) {
						if("2".equals(mRecord.getTypeNum())) {
							mTwoD ++;
						} else if("3".equals(mRecord.getTypeNum())) {
							mThreeD ++;
						} else if("4".equals(mRecord.getTypeNum())) {
							mFourD ++;
						} else {
							mOneD ++;
						}
					}
				}
				int[] mDegree = new int[]{mOneD,mTwoD,mThreeD,mFourD};
				int temp = 0;
				for(int i = 0; i < mDegree.length - 1; i ++) {
					for(int j = i + 1; j < mDegree.length; j ++) {
						if(mDegree[i] > mDegree[j]) {
							temp = mDegree[i];
							mDegree[i] = mDegree[j];
							mDegree[j] = temp;
						}
					}
				}
				if(mOneD == mTwoD){
					mMDegree = 2;
				} else  {
					if(mDegree[3] == mFourD) {
						mMDegree = 4;
					} else if(mDegree[3] == mThreeD) {
						mMDegree = 3;
					} else if(mDegree[3] == mTwoD) {
						mMDegree = 2;
					} else if(mDegree[3] == mOneD) {
						mMDegree = 1;
					}
				}
				mSuggest.setText(mMDegree + "级");
				mSuggestNum = String.valueOf(mMDegree);
				new DBService(this).updateHolefaceByDegree($holefaceId,String.valueOf(mMDegree));
			}
			//
			//new DBService(mContext).updateHolefaceSettingInfo($holefaceId, "1");//
			mRiskTypes = new DBService(this).findHolefaceSettingGroupByType();// Maybe,It has some errors 
			mAdapter.notifyDataSetChanged();
		}
		if (resultCode != RESULT_OK) {// -1
			if (files != null && files.exists() && !isFile) {
				files.delete();
			}
			 return;
		}
           
		switch (requestCode) {
		case 3021://相机返回
			if (files != null && files.exists()) {
				isFile = true;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
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
			 } catch (Exception e) {}
			break;
		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String getPath(Uri uri) {  
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
		cursor.moveToFirst();
		return cursor.getString(column_index); 
    } 
	
	
	public void computeView(Drawable drawable,String imagePath) {
		if(mImageBtnList != null && mImageBtnList.size() < mNumsPerRow) {//如果第一行未排满
			mPhotoContainer1.removeView(mOpenCameraBtn);
			
			DefineButton btn = new DefineButton(this);
			btn.setBackgroundDrawable(drawable);
			btn.setImagePath(imagePath);
			
			if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
				btn.setOnLongClickListener(deleteImageListener);
			}
			btn.setOnClickListener(seeImageListener);
			
			mImageBtnList.add(btn);
			mPhotoContainer1.addView(btn, mLayoutParams);
			
			TextView textView = new TextView(this);
			mPhotoContainer1.addView(textView, mMidLayoutParams);
			if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
			if(mImageBtnList.size() < 5) {
				if(mImageBtnList.size() == mNumsPerRow) {
					mPhotoContainer2.addView(mOpenCameraBtn, mLayoutParams);
				} else {
					mPhotoContainer1.addView(mOpenCameraBtn, mLayoutParams);
				}
			}
			}
		} else {//如果第一行排满了,适合小屏幕
				mPhotoContainer2.removeView(mOpenCameraBtn);
				
				DefineButton btn = new DefineButton(this);
				btn.setBackgroundDrawable(drawable);
				btn.setImagePath(imagePath);
				
				if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
					btn.setOnLongClickListener(deleteImageListener);
				}
				btn.setOnClickListener(seeImageListener);
				
				mImageBtnList.add(btn);
				
				mPhotoContainer2.addView(btn, mLayoutParams);
				TextView textView = new TextView(this);
				mPhotoContainer1.addView(textView, mMidLayoutParams);
				if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
				if(mImageBtnList.size() < 5) {
					mPhotoContainer2.addView(mOpenCameraBtn, mLayoutParams);
				}
				}
		}
	}
	
	public void reComputeView() {
		mPhotoContainer1.removeAllViews();
		mPhotoContainer2.removeAllViews();
		if(mImageBtnList != null && mImageBtnList.size() > 0) {
			for(int i = 0; i < mImageBtnList.size(); i ++) {
				if(i < mNumsPerRow) {
					if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
						mImageBtnList.get(i).setOnLongClickListener(deleteImageListener);
					}
					mImageBtnList.get(i).setOnClickListener(seeImageListener);
					
					mPhotoContainer1.addView(mImageBtnList.get(i), mLayoutParams);
					TextView textView = new TextView(this);
					mPhotoContainer1.addView(textView, mMidLayoutParams);
				} else {
					if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
						mImageBtnList.get(i).setOnLongClickListener(deleteImageListener);
					}
					mImageBtnList.get(i).setOnClickListener(seeImageListener);
					
					mPhotoContainer2.addView(mImageBtnList.get(i), mLayoutParams);
					TextView textView = new TextView(this);
					mPhotoContainer2.addView(textView, mMidLayoutParams);
				}
			}
			if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
				if(mImageBtnList.size() < 5) {
					if(mImageBtnList.size() == mNumsPerRow) {
						mPhotoContainer2.addView(mOpenCameraBtn, mLayoutParams);
					} else {
						mPhotoContainer1.addView(mOpenCameraBtn, mLayoutParams);
					}
				}
			}
		} else {
			if(mLocalRisk != null && !"1".equals(mLocalRisk.getIsFinish())){
				mPhotoContainer1.addView(mOpenCameraBtn, mLayoutParams);
			}
		}
	}
	
	File files;
 	String originCameraImgPath;
	OnClickListener mOpenCameraBtnListener = new OnClickListener() {
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
					 if (!FileUtils.isExistSDCard()) { // 检测sd是否可用
						 Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
			             return;
			         }
					String fileDir = FileUtils.getSDCardPath() + Constansts.CN_CAMERA_RISK_PATH;
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
	
	/**
	 * 清除照片列表+List缓存
	 */
	public void clearPhotos() {
		mPhotoContainer1.removeAllViews();
		mPhotoContainer2.removeAllViews();
		mPhotoContainer1.addView(mOpenCameraBtn, mLayoutParams);
		if(mImageBtnList != null)
		mImageBtnList.clear();
	}
    
    protected void dialog(final View v) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				DefineButton btn = (DefineButton)v;
				if(btn != null){
					if(btn.get$id() != null && !"".equals(btn.get$id())) {
						new DBService(mContext).delSingle("LOCAL_FACE_PHOTOS", btn.get$id());
					}
					
					File file = new File(btn.getImagePath());
					if(file.exists()) {
						file.delete();
					}
				}
				
				mImageBtnList.remove(btn);
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			reComputeView();
			if(mDialog != null)
				mDialog.dismiss();
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
		default:
			break;
		}
	}
}
