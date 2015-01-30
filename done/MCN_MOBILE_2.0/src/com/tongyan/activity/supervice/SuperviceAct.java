package com.tongyan.activity.supervice;


import java.io.File;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.SuperviceAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Check;
import com.tongyan.common.entities._ListObj;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.common.entities._Project;
import com.tongyan.common.entities._User;
import com.tongyan.service.MGPSService;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ImageUtil;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.MSpinner;
import com.tongyan.widget.view.MyDatePickerDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @ClassName P28_SuperviceAct 
 * @author wanghb
 * @date 2013-8-13 pm 02:43:09
 * @desc 现场检查
 */
public class SuperviceAct extends AbstructCommonActivity {
	//private static final String TAG = "P28_SuperviceAct";
	private Context mContext = this;
	private Button mHomeBtn,mBottomBtn;
	private ListView mListView;
	private TextView mNoListTextView;
	private _User mLocUser;
	private Dialog mDialog;
	private String isSucc;
	
	private SuperviceAdapter adaper;
	
	private List<_Check> mCheckList = new ArrayList<_Check>();
	List<SoftReference<Bitmap>> mCacheList = new ArrayList<SoftReference<Bitmap>>();
	private MDialog mDialogCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.supervise_check_list_layout);
		mHomeBtn = (Button)findViewById(R.id.p31_supervise_camera_home_btn);
		mBottomBtn = (Button)findViewById(R.id.p28_supervise_add_btn_id);
		mListView = (ListView)findViewById(R.id.p28_supervice_listview);
		mNoListTextView = (TextView)findViewById(R.id.p28_supervice_no_listview);
	}
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(homeBtnListener);
		mBottomBtn.setOnClickListener(bottomBtnListener);
		mListView.setOnItemClickListener(mOnItemListener);
	}
	
	public List<_Check> getLastChecks() {
		List<_Check> list = new DBService(mContext).selectCheckList();
		if(list == null || list.size() == 0) {
			mNoListTextView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.INVISIBLE);
		} else {
			mNoListTextView.setVisibility(View.INVISIBLE);
			mListView.setVisibility(View.VISIBLE);
		}
		return list;
	}
	
	private void businessM(){
		MyApplication mApplication = ((MyApplication)getApplication());
		mLocUser = mApplication.localUser;
		mApplication.addActivity(this);
		if(mCheckList != null) {
			mCheckList.clear();
		}
		mCheckList.addAll(getLastChecks());
		adaper = new SuperviceAdapter(mContext, mCheckList);
		mListView.setAdapter(adaper);
	}
	
	public void loadingData() {
		if(mCheckList != null) {
			mCheckList.clear();
		}
		mCheckList.addAll(getLastChecks());
		adaper.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 111) {
			loadingData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	// ===========================================================
	// part of listener
	// ===========================================================
	
	public void dismissDialog() {
		if(null != mMDialogCategory)
		mMDialogCategory.dismiss();
	}
	
	MDialog mMDialogCategory;
	OnItemClickListener mOnItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			SuperviceAdapter.ViewHolder mViewHolder = (SuperviceAdapter.ViewHolder)arg1.getTag();
			final _Check mCheck = mViewHolder.checkObj;
			if(mCheck != null) {
				mMDialogCategory = new MDialog(mContext, R.style.dialog);
				mMDialogCategory.createDialog(R.layout.supervice_see_pop_a, 0.9, 0.5, getWindowManager());
				Button mReadBtn = (Button)mMDialogCategory.findViewById(R.id.p28_supervice_pop_read);
				Button mCheckBtn = (Button)mMDialogCategory.findViewById(R.id.p28_supervice_pop_check);
				Button mModifyBtn = (Button)mMDialogCategory.findViewById(R.id.p28_supervice_pop_modify);
				Button mCommitBtn = (Button)mMDialogCategory.findViewById(R.id.p28_supervice_pop_commit);
				Button mDeleteBtn = (Button)mMDialogCategory.findViewById(R.id.p28_supervice_pop_delete);
				if("0".equals(mCheck.getIsUpdate())) {//未检查
					mReadBtn.setVisibility(View.INVISIBLE);
					mModifyBtn.setVisibility(View.INVISIBLE);
				} else if("1".equals(mCheck.getIsUpdate())) {//已检查
					mCheckBtn.setVisibility(View.INVISIBLE);
					mReadBtn.setVisibility(View.INVISIBLE);
				} else if("2".equals(mCheck.getIsUpdate())) {//已上传
					mCheckBtn.setVisibility(View.INVISIBLE);
					mModifyBtn.setVisibility(View.INVISIBLE);
				}
				//checking
				mCheckBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, AddSuperviseAct.class);
						intent.putExtra("check", mCheck);
						startActivityForResult(intent, 111);
						dismissDialog();
					}
				});
				//reading
				mReadBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,ShowCotentSuperAct.class);
						intent.putExtra("check", mCheck);
						startActivity(intent);
						dismissDialog();
					}
				});
				//modify
				mModifyBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,AddSuperviseAct.class);
						intent.putExtra("check", mCheck);
						startActivityForResult(intent, 111);
						dismissDialog();
					}
				});
				
				//commit
				mCommitBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if("0".equals(mCheck.getIsUpdate())) {//未检查
							Toast.makeText(mContext, "该记录还未检查", Toast.LENGTH_SHORT).show();
						}else if("2".equals(mCheck.getIsUpdate())) {//已上传
							Toast.makeText(mContext, "该记录已上传", Toast.LENGTH_SHORT).show();
						}else if("1".equals(mCheck.getIsUpdate())) {
							dismissDialog();
							commitCheck(mCheck);
						}
					}
				});
				//delete
				mDeleteBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissDialog();
						List<_LocalPhotos> locPhotos = new DBService(mContext).selectPhotos(mCheck.getId().toString());
						if(locPhotos != null && locPhotos.size() > 0) {
							for(int i = 0 ; i < locPhotos.size(); i ++) {
								_LocalPhotos p = locPhotos.get(i);
								if(p != null) {
									//删除本地文件
									File file = new File(p.getLocal_img_path());
									if(file.exists() && p.getLocal_img_path().contains(Constansts.CN_CAMERA_CHECK_PATH)) {
										file.delete();
									}
									//删除图片表数据
									boolean isDel = new DBService(mContext).delSingle("LOCAL_PHOTOS", p.getId());
									if(!isDel) {
										Toast.makeText(mContext, "删除失败，请重试", Toast.LENGTH_SHORT).show();
									}
								}
							}
						}
						//删除主表数据
						boolean isDel = new DBService(mContext).delSingle("LOCAL_CHECK", mCheck.getId().toString());
						if(!isDel) {
							Toast.makeText(mContext, "删除失败，请重试", Toast.LENGTH_SHORT).show();
						}
						if(mCheckList != null) {
							mCheckList.clear();
						}
						mCheckList.addAll(getLastChecks());
						adaper.notifyDataSetChanged();
					}
				});
			}
		}
	};
	
	public void commitCheck(final _Check check)  {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		new Thread(new Runnable() {
			@Override
			public void run() {
				String params = "{unitId:'"+check.getaProjectId()+"',sEmpid:'"+ mLocUser.getUserid() +"',sEmp:'"+mLocUser.getEmpName() + "." + mLocUser.getDepartment()+"',sContent:'" + check.getCheckContent()+  "',sStartmile:'"+check.getaStartMile() + "',sEndmile:'" + check.getaEndMile()+"',sChecktime:'" + date +"'}";
				try {
					String str = WebServiceUtils.getRequestStr(mLocUser.getUsername(), mLocUser.getPassword(), null, null, "Supervise", params, Constansts.METHOD_OF_ADD, mContext);
					Map<String,Object> mR = new Str2Json().addResult(str);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							String checkId = (String)mR.get("v");
							check.setCheckId(checkId);
							new DBService(mContext).updateCheckId(check);
							upPic(check);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				}  catch (Exception e) {
					sendMessage(Constansts.NET_ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void upPic(_Check check) {
		List<_LocalPhotos> photsList = new DBService(mContext).getPhotosList(check.getId().toString());
		 boolean isAllUp = true;
		 if (photsList != null && photsList.size() > 0) {
			 for (int i = 0; i < photsList.size(); i++) {
				_LocalPhotos p = photsList.get(i);
				ImageUtil utils = new ImageUtil();
				SoftReference<Bitmap> bmp2 = new SoftReference<Bitmap>(utils.getZoomBmpByDecodePath(p.getLocal_img_path(), 300,400));
				mCacheList.add(bmp2);
				String changeStr = Base64.encodeToString(utils.Bitmap2Bytes(bmp2.get()), 0);
				try {
					Map<String, String> properties = new HashMap<String, String>();
					properties.put("publicKey", Constansts.PUBLIC_KEY);
					properties.put("userName", mLocUser.getUsername());
					properties.put("Password", mLocUser.getPassword());
					properties.put("img", changeStr);
					properties.put("filetype", "jpg");
					properties.put("objid", check.getCheckId());
					String strJ = WebServiceUtils.requestM(properties, "UpPic", mContext);
					Map<String,Object> mRR =  new Str2Json().upPic(strJ);
					if(mRR != null) {
						isSucc = (String)mRR.get("s");
						if("ok".equals(isSucc)) {
							p.setRemote_img_id((String)mRR.get("rowId"));
							p.setRemote_img_path((String)mRR.get("path"));
							boolean isSuc = new DBService(mContext).updatePhotos(p);
							if(!isSuc) {
								isAllUp = false;
							}
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			if(isAllUp) {
				check.setIsUpdate("2");
				new DBService(mContext).updateCheck(check);
				sendMessage(Constansts.SUCCESS);
			} else {
				Toast.makeText(mContext, "数据没录入成功，请重新操作", Toast.LENGTH_SHORT).show();
			}
		 }
	}
	
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	EditText mDateEditText;
	EditText mBMileEditText;
	EditText mEMileEditText;
	MSpinner mSectionSpinner;
	MSpinner mProjectSpinner;
	_ListObj mItem;
	_ListObj mSction;
	_Project mProject;
	
	
	OnClickListener bottomBtnListener = new OnClickListener() {
		public void onClick(View v) {
			mDialogCategory = new MDialog(mContext, R.style.dialog);
			mDialogCategory.createDialog(R.layout.supervice_add_pop, 0.9, 0.9, getWindowManager());
			mDateEditText = (EditText)mDialogCategory.findViewById(R.id.p28_supervice_time_edit);
			MSpinner mItemSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p28_supervice_item_spinner);
			mSectionSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p328_supervice_section_spinner);
			mProjectSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p28_supervice_project_spinner);
			mBMileEditText = (EditText)mDialogCategory.findViewById(R.id.p28_supervice_begin_mile);
			mEMileEditText = (EditText)mDialogCategory.findViewById(R.id.p28_supervice_end_mile);
			Button mCommitButton = (Button)mDialogCategory.findViewById(R.id.p28_supervice_sure_btn);
			
			mDateEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
			mDateEditText.setCursorVisible(false);//去光标
			
			final List<_ListObj> mItemList = new DBService(mContext).getItemListByObj();
			if(mItemList != null && mItemList.size() > 0) {
				ArrayAdapter<_ListObj> mHighWayAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mItemList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							String mTextContent= mItemList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = getResources().getString(R.string.p04_gps_select_project_hint);
							} 
							textView.setText(mTextContent);
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mItemList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.p04_gps_select_project_hint);
							textView.setTextColor(Color.GRAY);
						} 
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mItemSpinner.setAdapter(mHighWayAdapter);
				 mItemSpinner.setOnItemSelectedListener(mItemSelectedListener);
			} else {
				mItemSpinner.setmBackgroundView(R.layout.common_spinner3);
				mItemSpinner.setmInitText("选择项目");
				mItemSpinner.setAdapter(mItemSpinner.getAdapter());
				mItemSpinner.setmTextColor(Color.GRAY);
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			    mSectionSpinner.setmTextColor(Color.GRAY);
			    mProjectSpinner.setmBackgroundView(R.layout.common_spinner3);
				mProjectSpinner.setmInitText("选择隧道");
				mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
				mProjectSpinner.setmTextColor(Color.GRAY);
			}
			mDateEditText.setOnClickListener(mDateEditTextListener);
			mCommitButton.setOnClickListener(mCommitButtonListener);
		}
	};
	OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			final List<_ListObj> mSectionList = new DBService(mContext).getSectionListByObj(item.getId());
			if(mSectionList != null && mSectionList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mSectionList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							String mTextContent= mSectionList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
							} 
							textView.setText(mTextContent);
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mSectionList.get(position).getText();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.p04_gps_select_section_hint);
							textView.setTextColor(Color.GRAY);
						} 
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mSectionSpinner.setAdapter(mSectionAdapter);
				 mSectionSpinner.setOnItemSelectedListener(mSectionListener);
			} else {
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			    mSectionSpinner.setmTextColor(Color.GRAY);
			    
			    mProjectSpinner.setmBackgroundView(R.layout.common_spinner3);
				mProjectSpinner.setmInitText("选择隧道");
				mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
				mProjectSpinner.setmTextColor(Color.GRAY);
			}
			 mItem = item;
			 mSction = null;
			 mProject = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	OnItemSelectedListener mSectionListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			final List<_Project> mProList = new DBService(mContext).selectProList(item.getId());
			mProjectSpinner.setAdapter(mProjectSpinner.getAdapter());
			if(mProList != null && mProList.size() > 0) {
				ArrayAdapter<_Project> mSectionAdapter = new ArrayAdapter<_Project>(mContext,R.layout.common_spinner3, mProList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							String mTextContent= mProList.get(position).getaName();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = getResources().getString(R.string.select_tunnel_hint);
							}
							textView.setText(mTextContent);
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						String mTextContent= mProList.get(position).getaName();
						if(mTextContent == null || "".equals(mTextContent) ) {
							mTextContent = getResources().getString(R.string.select_tunnel_hint);
							textView.setTextColor(Color.GRAY);
						} 
						textView.setText(mTextContent);
						return convertView;
					}
				 };
				 mProjectSpinner.setAdapter(mSectionAdapter);
				 mProjectSpinner.setOnItemSelectedListener(mProjectListener);
			} else {
				mProjectSpinner.setmBackgroundView(R.layout.common_spinner3);
				mProjectSpinner.setmInitText("选择隧道");
				mProjectSpinner.setAdapter(mProjectSpinner.getmAdapter());
				mProjectSpinner.setmTextColor(Color.GRAY);
			}
			mSction = item;
			mProject = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	OnItemSelectedListener mProjectListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_Project item = (_Project) parent.getAdapter().getItem(position);
			mProject = item;
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	OnClickListener mDateEditTextListener = new OnClickListener() {
		public void onClick(View v) {
			new MyDatePickerDialog(mContext, new MyDatePickerDialog.OnDateTimeSetListener() {
				@Override
				public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
						int hour, int minute) {
					String mMonth = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
					String mDay =  dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
					mDateEditText.setText(year+"-"+mMonth +"-"+mDay);
				}
			}).show();
		}
	};
	OnClickListener mCommitButtonListener = new OnClickListener() {
		public void onClick(View v) {
			if(null == mDateEditText.getText().toString() || "".equals(mDateEditText.getText().toString())) {
				Toast.makeText(mContext, "请选择日期", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == mItem) {
				Toast.makeText(mContext, "请选择项目", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == mSction) {
				Toast.makeText(mContext, "请选择标段", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == mProject) {
				Toast.makeText(mContext, "请选择隧道", Toast.LENGTH_SHORT).show();
				return;
			}
			if(null == mBMileEditText.getText().toString() || "".equals(mBMileEditText.getText().toString())) {
				Toast.makeText(mContext, "请填写开始里程", Toast.LENGTH_SHORT).show();
				return;
			}
			if(null == mEMileEditText.getText().toString() || "".equals(mEMileEditText.getText().toString())) {
				Toast.makeText(mContext, "请填写结束里程", Toast.LENGTH_SHORT).show();
				return;
			}
			//insert
			_Check mCheck = new _Check();
			mCheck.setUpTime(mDateEditText.getText().toString());
			mCheck.setaItemId(mItem.getId());
			mCheck.setaItemName(mItem.getText());
			mCheck.setaSecId(mSction.getId());
			mCheck.setaSecName(mSction.getText());
			mCheck.setaProjectId(mProject.getRowId());
			mCheck.setaProName(mProject.getaName());
			mCheck.setaStartMile(mBMileEditText.getText().toString());
			mCheck.setaEndMile(mEMileEditText.getText().toString());
			Long mSuc = new DBService(mContext).insertCheck(mCheck);
			if(mDialogCategory != null) {
				mDialogCategory.dismiss();
			}
			if(mSuc != 0) {
				Toast.makeText(mContext, "新增成功", Toast.LENGTH_SHORT).show();
				loadingData();
			} else {
				Toast.makeText(mContext, "新增失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			loadingData();
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(mContext, isSucc, Toast.LENGTH_SHORT).show();
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
			if(mDialog != null)
				mDialog.dismiss();
			break;
		default :
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
