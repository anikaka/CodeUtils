package com.tongyan.activity.risk;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.RisksListAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._HoleFace;
import com.tongyan.common.entities._HolefaceSetting;
import com.tongyan.common.entities._HolefaceSettingInfo;
import com.tongyan.common.entities._HolefaceSettingRecord;
import com.tongyan.common.entities._ListObj;
import com.tongyan.common.entities._LocRisk;
import com.tongyan.common.entities._LocalPhotos;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ImageUtil;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.ScreenUtil;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.DefineButton;
import com.tongyan.widget.view.MSpinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author wanghb
 * @desc so complex, code and logic
 */
public class P35_RiskTaskListAct extends AbstructCommonActivity {
	
	private Context mContext = this;
	
	private Button mAddBtn,mHomeBtn;
	private ListView mListView;
	private TextView mNoListView;
	
	
	private List<_LocRisk> mList = new ArrayList<_LocRisk>();
	
	private List<_ListObj> mItemList,mSectionList,mTunnelList;
	
	private _ListObj mSelectItem,mSelectSection,mSelectTunnel;
	
	private RisksListAdapter mAdapter;
	
	private MyApplication mApplication;
	private Dialog mDialog;
	private _User mLocalUser;
	private String isSucc;
	
	MDialog mHighWayDialogCategory = null;
	MDialog mSectionDialogCategory = null;
	MDialog mTunnelDialogCategory = null;
	MDialog mDialogCategory = null;
	MDialog mHolofaceDialog = null;
	
	private MSpinner mHighWaySpinner = null;
	private MSpinner mSectionSpinner = null;
	private MSpinner mTunnelSpinner = null;
	
	private List<SoftReference<Bitmap>> mCacheList = new ArrayList<SoftReference<Bitmap>>();
	
	private int mScreenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.risk_task_list);
		mListView = (ListView)findViewById(R.id.p35_risk_task_listview);
		mAddBtn = (Button)findViewById(R.id.p35_risk_add_btn_id);
		mNoListView = (TextView)findViewById(R.id.p35_risk_task_no_listview);
		mHomeBtn = (Button)findViewById(R.id.p35_risk_task_list_home_btn);
	}
	
	private void setClickListener() {
		mHomeBtn.setOnClickListener(mHomeBtnListener);
		mAddBtn.setOnClickListener(mAddBtnListener);
		mListView.setOnItemClickListener(mListViewItemListener);
	}
	
	private void businessM(){
		mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		mScreenHeight = display.getHeight();
		
		mAdapter = new RisksListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		
		refeshList();
		mItemList = new DBService(this).getItemListByObj();
	}
	
	//====================================================================
	// Part of Common Method
	//====================================================================
	public void refeshList() {
		List<_LocRisk> mLiskList = new DBService(this).selectRiskList(mLocalUser.getUserid());
		if(mLiskList != null) {
			if(mList != null) {
				mList.clear();
			}
			mList.addAll(mLiskList);
		}
		mAdapter.notifyDataSetChanged();
		if(mList == null || mList.size() == 0) {
			mNoListView.setVisibility(View.VISIBLE);
		} else {
			mNoListView.setVisibility(View.INVISIBLE);
		}
	}
	
	protected void delDialog(final String $id) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				new DBService(mContext).deleteHoloFace($id);
				boolean isDel = new DBService(mContext).delSingle("LOCAL_RISK", $id);
				new DBService(mContext).deleteHoloFace($id);//1//
				new DBService(mContext).deleteHolefaceSettingInfo($id);//3
				new DBService(mContext).deleteHolefaceSettingRecord($id);//4
				
				if(isDel) {
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
				}
				refeshList();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	//====================================================================
	// Part of OnClickListener
	//====================================================================
	
	OnClickListener mHomeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(mContext,MainAct.class);
			startActivity(intent);
		}
	};
	
	OnClickListener mHolofaceListener = new OnClickListener() {
		public void onClick(View v) {
			DefineButton mDelButton = (DefineButton)v;
			if(mDelButton != null) {
				if(mHolofaceDialog != null) {
					mHolofaceDialog.dismiss();
				}
				Intent intent = new Intent(mContext,P36_HoleCheckAct.class);
				intent.putExtra("$id", mDelButton.get$id());//HolefaceId
				intent.putExtra("$rowId", mDelButton.get$rowId());//RiskId
				startActivityForResult(intent, 402);
			} else {
				Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	OnClickListener mDelListener = new OnClickListener() {
		public void onClick(View v) {
			DefineButton mDelButton = (DefineButton)v;
			if(mDelButton != null) {
				if(mHolofaceDialog != null) {
					mHolofaceDialog.dismiss();
				}
				delDialog(mDelButton.get$id());
			} else {
				Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	
	/**
	 * update opera
	 */
	OnClickListener mUpListener = new OnClickListener() {
		public void onClick(View v) {
			DefineButton mDelButton = (DefineButton)v;
			if(mDelButton != null) {
				_LocRisk mRisk = mDelButton.getmRisk();
				if(mRisk != null) {
					if(mRisk.getIsFinish() != null) {
						if("3".equals(mRisk.getIsFinish())) {
							updateRemote(mRisk);
						}
						if("2".equals(mRisk.getIsFinish())) {
							Toast.makeText(mContext, "您还有未检查的风险,请继续检查风险", Toast.LENGTH_SHORT).show();
						}
						if("4".equals(mRisk.getIsFinish()) || "1".equals(mRisk.getIsFinish())) {
							Toast.makeText(mContext, "不能再上传了", Toast.LENGTH_SHORT).show();
						}
						if("0".equals(mRisk.getIsFinish())) {
							Toast.makeText(mContext, "您还未检查风险,请检查风险", Toast.LENGTH_SHORT).show();
						}
						
					} else {
						Toast.makeText(mContext, "请检查风险", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				if(mHolofaceDialog != null) {
					mHolofaceDialog.dismiss();
				}
			} else {
				Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/**
	 * *****
	 * @param mRisk
	 */
	public void updateRemote(final _LocRisk mRisk) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				/**
				 * the detail of interface, please read the interface documents
				 */
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("{risk:{r_unit:'");
				sBuilder.append(mRisk.getProwid());
				sBuilder.append("',r_date:'");
				sBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				sBuilder.append("',r_person:'");
				sBuilder.append(mLocalUser.getEmpName() + "." + mLocalUser.getDepartment());
				sBuilder.append("',r_emp_id:'");
				sBuilder.append(mLocalUser.getUserid());
				sBuilder.append("'}, list:[");
				
				List<_HolefaceSettingInfo> mSettingInfoList = new DBService(mContext).getHolefaceSettingInfoByRiskId(mRisk.getId(),null);
				if(mSettingInfoList != null && mSettingInfoList.size() > 0) {
					
					for(int i = 0; i < mSettingInfoList.size(); i ++) {
						_HolefaceSettingInfo mSettingInfo = mSettingInfoList.get(i);
						
						if(mSettingInfo != null) {
							//sBuilder.append("{row_id:'");
							//sBuilder.append(mRisk.getRowId());
							sBuilder.append("{r_position:'");
							sBuilder.append(mSettingInfo.getRiskHoleName());
							sBuilder.append("',r_type:'");
							sBuilder.append(mSettingInfo.getRiskTypeName());
							sBuilder.append("',items:[");
							List<_HolefaceSettingRecord> mRecordsTypeList = new DBService(mContext).getHolefaceRecordsByAllParams(mRisk.getId(), mSettingInfo.get$holefaceId(), mSettingInfo.getRiskTypeName(), null);//
							if(mRecordsTypeList != null && mRecordsTypeList.size() > 0) {
								for(int j = 0; j < mRecordsTypeList.size(); j ++) {
									_HolefaceSettingRecord mRecord = mRecordsTypeList.get(j);
									if(mRecord != null) {
										_HolefaceSetting mSetting = new DBService(mContext).findRiskSettingsById(mRecord.get$rowId());
										if(mSetting != null) {
											sBuilder.append("{item1:'");
											sBuilder.append(mRecord.getOneType());
											sBuilder.append("',item2:'");
											sBuilder.append(mSetting.getTwoType());
											sBuilder.append("',riskcontent:'");
											sBuilder.append(mRecord.getSelectedType());
											sBuilder.append("',childs:[");
											//sBuilder.append(mRecord.getClassTypeNum());
											if(mRecord.getmHolefaceRecordList() != null && mRecord.getmHolefaceRecordList().size() > 0) {
												for(int k = 0; k < mRecord.getmHolefaceRecordList().size(); k++) {
													_HolefaceSettingRecord mMRecord = mRecord.getmHolefaceRecordList().get(k);
													if(null != mMRecord) {
														_HolefaceSetting mMSetting = new DBService(mContext).findRiskSettingsById(mMRecord.get$rowId());
														if(null != mMSetting) {
															sBuilder.append("{item1:'");
															sBuilder.append(mMRecord.getOneType());
															sBuilder.append("',item2:'");
															sBuilder.append(mMSetting.getTwoType());
															sBuilder.append("',riskcontent:'");
															sBuilder.append(mMRecord.getSelectedType());
															if(k ==  mRecord.getmHolefaceRecordList().size() -1) {
																sBuilder.append("'}");
															} else {
																sBuilder.append("'},");
															}
														}
													}
												}
											}
											if(j == mRecordsTypeList.size() -1) {
												sBuilder.append("]}");
											} else {
												sBuilder.append("]},");
											}
										}
									}
								}
							}
							sBuilder.append("],r_startmile:'");
							_HoleFace mHoleFace = new DBService(mContext).getOneHoloFace(mSettingInfo.get$holefaceId());
							String currentMile = "";
							String mProposeDegree = "";
							String mRiskDegree = "";
							String mRiskHSuggest = "";
							if(mHoleFace != null) {
								currentMile = mHoleFace.getCurrMile();
								mProposeDegree = mHoleFace.getProposeDegree();
								mRiskDegree = mHoleFace.getRiskDegree();
								mRiskHSuggest = mHoleFace.getRiskHSuggest();
							}
							sBuilder.append(currentMile);
							sBuilder.append("',r_endmile:'");
							sBuilder.append(currentMile);
							sBuilder.append("',r_suggest:'");
							sBuilder.append(mProposeDegree);
							sBuilder.append("',r_class:'");
							sBuilder.append(mRiskDegree);
							sBuilder.append("',r_memo:'");
							sBuilder.append(mRiskHSuggest);
							if(i == mSettingInfoList.size() - 1) {
								sBuilder.append("'}");
							} else {
								sBuilder.append("'},");
							}
						}
					}
				}
				sBuilder.append("]}");
				//Log.i(TAG, sBuilder.toString());
				try {
					String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "RiskCheck", sBuilder.toString(), Constansts.METHOD_OF_ADD,mContext);
					//Log.i(TAG, "RiskCheck:" + str);
					Map<String,Object> mR = new Str2Json().addResult(str);
					if(mR != null) {
						isSucc = (String)mR.get("s");
						if("ok".equals(isSucc)) {
							String mImageId = (String)mR.get("v");
							submitPic(mRisk.getId(),mImageId);
						} else {
							sendMessage(Constansts.ERRER);
						}
					} else {
						sendMessage(Constansts.NET_ERROR);
					}
				}  catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * commit picture
	 * @param $riskId
	 * @param mImageId
	 */
	public void submitPic(String $riskId, String mImageId) {
		List<_LocalPhotos> mLocHolefaceList = new DBService(mContext).selectHolefacePhotosByRiskId($riskId);
		if(mLocHolefaceList != null && mLocHolefaceList.size() > 0) {
			boolean mIsSucB = true;
			for(int i = 0; i < mLocHolefaceList.size(); i++) {
				_LocalPhotos mPhotos = mLocHolefaceList.get(i);
				if(mPhotos != null) {
					String path = mPhotos.getLocal_img_path();
					ImageUtil utils = new ImageUtil();
					SoftReference<Bitmap> bmp2 = new SoftReference<Bitmap>(utils.getZoomBmpByDecodePath(path, 300, 400));
					String changeStr = Base64.encodeToString(utils.Bitmap2Bytes(bmp2.get()), 0);
					mCacheList.add(bmp2);
					_HoleFace mFace = new DBService(mContext).getOneHoloFace(mPhotos.getCheck_tab_id());
					try {
						Map<String, String> properties = new HashMap<String, String>();
						properties.put("publicKey", Constansts.PUBLIC_KEY);
						properties.put("userName", mLocalUser.getUsername());
						properties.put("Password", mLocalUser.getPassword());
						properties.put("img", changeStr);
						properties.put("filetype", "jpg");
						properties.put("objid", mImageId);
						if(mFace != null)
						properties.put("position", mFace.getHole());
						
						String strJ = WebServiceUtils.requestM(properties, "UpPic", mContext);
						//Log.i(TAG, "UpPic : "+ strJ);
						
						Map<String,Object> mR =  new Str2Json().upPic(strJ);
						if(mR != null) {
							isSucc = (String)mR.get("s");
							if("ok".equals(isSucc)) {
								boolean isSuc = new DBService(mContext).updateHolefacePhotos(mPhotos.getId(),(String)mR.get("path"),(String)mR.get("rowId"));
								if(!isSuc) {
									mIsSucB = false;
								}
								clearCache();
							} else {
								//sendMessage(Constansts.ERRER);
							}
						} else {
							sendMessage(Constansts.NET_ERROR);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
			
			if(mIsSucB) {
				new DBService(mContext).updateRisk($riskId, "1");//
				sendMessage(Constansts.SUCCESS);
			} else {
				new DBService(mContext).updateRisk($riskId, "2");//
				sendMessage(Constansts.MES_TYPE_1);
			}
		}
	}
	/**
	 * new task - dialog of define spinner
	 */
	OnClickListener mAddBtnListener = new OnClickListener() {
		public void onClick(View v) {
			mDialogCategory = new MDialog(mContext, R.style.dialog);
			mDialogCategory.createDialog(R.layout.risk_task_pop_a, 0.85, 0.9, getWindowManager());
			
			mHighWaySpinner = (MSpinner)mDialogCategory.findViewById(R.id.p35_risk_item_spinner);
			mSectionSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p35_risk_section_spinner);
			mTunnelSpinner = (MSpinner)mDialogCategory.findViewById(R.id.p35_risk_tunnel_spinner);
			if(mItemList != null && mItemList.size() > 0) {
				ArrayAdapter<_ListObj> mHighWayAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mItemList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							if(mItemList.get(position) != null) {
								String mTextContent = mItemList.get(position).getText();
								if(mTextContent == null || "".equals(mTextContent) ) {
									mTextContent = "选择项目";
								}
								textView.setText(mTextContent);
							}
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						if(mItemList.get(position) != null) {
							String mTextContent = mItemList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = "选择项目";
								textView.setTextColor(Color.GRAY);
							}
							textView.setText(mTextContent);
						} 
						return convertView;
					}
				 };
				 mHighWaySpinner.setAdapter(mHighWayAdapter);
				 mHighWaySpinner.setOnItemSelectedListener(mHighWayEditListener);
			} else {
				mHighWaySpinner.setmBackgroundView(R.layout.common_spinner3);
				mHighWaySpinner.setmInitText("选择项目");
				mHighWaySpinner.setAdapter(mHighWaySpinner.getmAdapter());
				mHighWaySpinner.setmTextColor(Color.GRAY);
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			    mSectionSpinner.setmTextColor(Color.GRAY);
			    mTunnelSpinner.setmBackgroundView(R.layout.common_spinner3);
				mTunnelSpinner.setmInitText("选择隧道");
			    mTunnelSpinner.setAdapter(mTunnelSpinner.getmAdapter());
			    mTunnelSpinner.setmTextColor(Color.GRAY);
			}
			
			Button mClickSureBtn = (Button)mDialogCategory.findViewById(R.id.p35_risk_sure_btn);
			mClickSureBtn.setOnClickListener(mClickSureBtnListener);
		}
	};
	/**
	 * 此操作的逻辑会有一点复杂，因为接触到5张表.集整个风险检查的本质所在.
	 * 1.生成风险记录数据
	 * 2.生成掌子面数据
	 * 3.生成章子面风险设置信息 (有几种风险，1个掌子面就有几条记录数据)
	 * 4.某种风险的记录表
	 * 5.图片上传表
	 * 
	 * I.如果有一条数据操作失败，将会删除此操作所有插入数据库的记录
	 * II.code No handler
	 */
	OnClickListener mClickSureBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mSelectItem == null) {
				Toast.makeText(mContext, "请选择高速公路", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mSelectSection == null) {
				Toast.makeText(mContext, "请选择标段", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mSelectTunnel == null) {
				Toast.makeText(mContext, "请选择隧道", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mDialogCategory != null) {
				mDialogCategory.dismiss();
			}
			addRiskByThread();
		}
	};
	//
	public void addRiskByThread() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			public void run() {
				_LocRisk risk = new _LocRisk();
				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				risk.setRowId(UUID.randomUUID().toString());
				risk.setUserId(mLocalUser.getUserid());
				risk.setIid(mSelectItem.getId());
				risk.setiContent(mSelectItem.getText());
				risk.setSid(mSelectSection.getId());
				risk.setsContent(mSelectSection.getText());
				risk.setProwid(mSelectTunnel.getId());
				risk.setpContent(mSelectTunnel.getText());
				//risk.setIsFinish(isFinish);
				risk.setCurrDate(date);
				Long riskId = new DBService(mContext).insertLocRisk(risk);
				if(riskId != 0) {
					//insert the sub table
					String aPosition = mSelectTunnel.getaPosition();//左洞,右洞,
					if(aPosition != null && !"".equals(aPosition)) {
						String[] mPositionArray = aPosition.split(",");//获取洞口个数
						if(mPositionArray != null && mPositionArray.length > 0) {
							boolean isInsert = true;
							List<String>  mRiskTypes = new DBService(mContext).findHolefaceSettingGroupByType();
							Map<String,String> mMileMap = new HashMap<String,String>();
							String mPositionMile = mSelectTunnel.getaPositionMile();//左洞:ZK01+10:Zk01+90;右洞:LK01+10:Lk01+90;
							if(mPositionMile != null && !"".equals(mPositionMile)) {
								String[] mMileAry = mPositionMile.split(";");
								if(mMileAry != null && mMileAry.length > 0) {
									for(int w = 0; w < mMileAry.length; w ++) {
										String mOneMile = mMileAry[w];
										if(mOneMile != null) {
											String[] mMMileAry = mOneMile.split(":");
											if(mMMileAry != null && mMMileAry.length > 0) {
												try {
													mMileMap.put(mMMileAry[0], mMMileAry[1]);
												}catch(Exception e) {/*IndexOutOfBoundsException*/}
											}
										}
									}
								}
							}
							
							if(mRiskTypes != null && mRiskTypes.size() > 0 ) {
								for(int i = 0; i < mPositionArray.length; i ++) {
									String mPos = mPositionArray[i];
									if("左洞".equals(mPos)) {
										_HoleFace face = new _HoleFace();
										face.setRiskId(riskId.toString());
										face.setHole("左洞入口");
										face.setCurrMile(mMileMap.get("左洞"));
										Long mLeftFaceEnter = new DBService(mContext).insertLocHoloFace(face);
										if(mLeftFaceEnter == 0 ) {
											isInsert = false;
											break;
										} else {//maybe u see here,u think it's so difficult
											    //first,how many hole faces...
											    //second,how many types of risk(but the risk has father type and son type,so...)
											for(int j = 0; j < mRiskTypes.size(); j ++) {
												_HolefaceSettingInfo mHolefaceSetting = new _HolefaceSettingInfo();
												mHolefaceSetting.set$riskId(riskId.toString());
												mHolefaceSetting.set$holefaceId(mLeftFaceEnter.toString());
												mHolefaceSetting.setIsCheck("0");
												mHolefaceSetting.setRiskHoleName("左洞入口");
												mHolefaceSetting.setRiskTypeName(mRiskTypes.get(j));
												Long mSettingInfoId = new DBService(mContext).insertHolefaceSettingInfo(mHolefaceSetting);
												if(mSettingInfoId == 0) {
													isInsert = false;
													break;
												} else {
													List<_HolefaceSetting> mHolefaceSettingType = new DBService(mContext).findHolefaceSettingGroupByItype(mRiskTypes.get(j));
													if(mHolefaceSettingType != null && mHolefaceSettingType.size() > 0) {
														for(int k = 0; k < mHolefaceSettingType.size(); k ++) {
															_HolefaceSetting mSetting = mHolefaceSettingType.get(k);
															if(mSetting != null) {
																_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
																mRecord.set$riskId(riskId.toString());
																mRecord.set$rowId(mSetting.get$id());
																mRecord.set$holefaceId(mLeftFaceEnter.toString());
																mRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																mRecord.set$subId("0");
																mRecord.setTypeNum("1");
																mRecord.setRiskType(mSetting.getRiskType());
																mRecord.setOneType(mSetting.getOneType());
																mRecord.setSelectedType(mSetting.getClass1());
																mRecord.setCurrentState(mSetting.getClass1());
																Long mRecordSuc = new DBService(mContext).insertHolefaceSettingRecord(mRecord);
																if(mSetting.getmClildList() != null && mSetting.getmClildList().size() > 0) {
																	for(int l = 0; l < mSetting.getmClildList().size(); l ++ ) {
																		_HolefaceSetting mSSetting = mSetting.getmClildList().get(l);
																		if(mSSetting != null) {
																			_HolefaceSettingRecord mSRecord = new _HolefaceSettingRecord();
																			mSRecord.set$rowId(mSSetting.get$id());
																			mSRecord.set$riskId(riskId.toString());
																			mSRecord.set$holefaceId(mLeftFaceEnter.toString());
																			mSRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																			mSRecord.setTypeNum("1");
																			mSRecord.setRiskType(mSSetting.getRiskType());
																			mSRecord.setOneType(mSSetting.getOneType());
																			mSRecord.setSelectedType(mSSetting.getClass1());
																			mSRecord.setCurrentState(mSSetting.getClass1());
																			mSRecord.set$subId(mRecordSuc.toString());
																			Long mRecordSub = new DBService(mContext).insertHolefaceSettingRecord(mSRecord);//
													
																			if(mRecordSub == 0) {
																				isInsert = false;
																				break;
																			}
																		}
																	}
																}
																if(isInsert == false || mRecordSuc == 0) {
																	isInsert = false;
																	break;
																}
															}
														}
													} else {
														isInsert = false;
														break;
													}
												}
											}
										}
										face.setHole("左洞出口");
										Long mLeftFaceExit = new DBService(mContext).insertLocHoloFace(face);
										if(mLeftFaceExit == 0) {
											isInsert = false;
											break;
										} else {
											for(int j = 0; j < mRiskTypes.size(); j ++) {
											_HolefaceSettingInfo mHolefaceSetting = new _HolefaceSettingInfo();
											mHolefaceSetting.set$riskId(riskId.toString());
											mHolefaceSetting.set$holefaceId(mLeftFaceExit.toString());
											mHolefaceSetting.setIsCheck("0");
											mHolefaceSetting.setRiskHoleName("左洞出口");
											mHolefaceSetting.setRiskTypeName(mRiskTypes.get(j));
											Long mSettingInfoId = new DBService(mContext).insertHolefaceSettingInfo(mHolefaceSetting);
											if(mSettingInfoId == 0) {
												isInsert = false;
												break;
											} else {
												List<_HolefaceSetting> mHolefaceSettingType = new DBService(mContext).findHolefaceSettingGroupByItype(mRiskTypes.get(j));
												if(mHolefaceSettingType != null && mHolefaceSettingType.size() > 0) {
													for(int k = 0; k < mHolefaceSettingType.size(); k ++) {
														_HolefaceSetting mSetting = mHolefaceSettingType.get(k);
														if(mSetting != null) {
															_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
															mRecord.set$riskId(riskId.toString());
															mRecord.set$rowId(mSetting.get$id());
															mRecord.set$holefaceId(mLeftFaceExit.toString());
															mRecord.set$subId("0");
															mRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
															mRecord.setTypeNum("1");
															mRecord.setRiskType(mSetting.getRiskType());
															mRecord.setOneType(mSetting.getOneType());
															mRecord.setSelectedType(mSetting.getClass1());
															mRecord.setCurrentState(mSetting.getClass1());
															Long mRecordSuc = new DBService(mContext).insertHolefaceSettingRecord(mRecord);
															if(mSetting.getmClildList() != null && mSetting.getmClildList().size() > 0) {
																for(int l = 0; l < mSetting.getmClildList().size(); l ++ ) {
																	_HolefaceSetting mSSetting = mSetting.getmClildList().get(l);
																	if(mSSetting != null) {
																		_HolefaceSettingRecord mSRecord = new _HolefaceSettingRecord();
																		mSRecord.set$rowId(mSSetting.get$id());
																		mSRecord.set$riskId(riskId.toString());
																		mSRecord.set$holefaceId(mLeftFaceExit.toString());
																		mSRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																		mSRecord.setTypeNum("1");
																		mSRecord.setRiskType(mSSetting.getRiskType());
																		mSRecord.setOneType(mSSetting.getOneType());
																		mSRecord.setSelectedType(mSSetting.getClass1());
																		mSRecord.set$subId(mRecordSuc.toString());
																		mSRecord.setCurrentState(mSSetting.getClass1());
																		Long mRecordSub = new DBService(mContext).insertHolefaceSettingRecord(mSRecord);
												
																		if(mRecordSub == 0) {
																			isInsert = false;
																			break;
																		}
																	}
																}
															}
															if(isInsert == false || mRecordSuc == 0) {
																isInsert = false;
																break;
															}
														}
													}
												} else {
													isInsert = false;
													break;
												}
											}
											}
										}
									} else if("右洞".equals(mPos)) {
										_HoleFace face = new _HoleFace();
										face.setRiskId(riskId.toString());
										face.setHole("右洞入口");
										face.setCurrMile(mMileMap.get("右洞"));
										Long mRightFaceEnter = new DBService(mContext).insertLocHoloFace(face);
										if(mRightFaceEnter == 0) {
											isInsert = false;
											break;
										} else {
											for(int j = 0; j < mRiskTypes.size(); j ++) {
											_HolefaceSettingInfo mHolefaceSetting = new _HolefaceSettingInfo();
											mHolefaceSetting.set$riskId(riskId.toString());
											mHolefaceSetting.set$holefaceId(mRightFaceEnter.toString());
											mHolefaceSetting.setIsCheck("0");
											mHolefaceSetting.setRiskHoleName("右洞入口");
											mHolefaceSetting.setRiskTypeName(mRiskTypes.get(j));
											Long mSettingInfoId = new DBService(mContext).insertHolefaceSettingInfo(mHolefaceSetting);
											if(mSettingInfoId == 0) {
												isInsert = false;
												break;
											} else {
												List<_HolefaceSetting> mHolefaceSettingType = new DBService(mContext).findHolefaceSettingGroupByItype(mRiskTypes.get(j));
												if(mHolefaceSettingType != null && mHolefaceSettingType.size() > 0) {
													for(int k = 0; k < mHolefaceSettingType.size(); k ++) {
														_HolefaceSetting mSetting = mHolefaceSettingType.get(k);
														if(mSetting != null) {
															_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
															mRecord.set$riskId(riskId.toString());
															mRecord.set$rowId(mSetting.get$id());
															mRecord.set$holefaceId(mRightFaceEnter.toString());
															mRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
															mRecord.set$subId("0");
															mRecord.setTypeNum("1");
															mRecord.setRiskType(mSetting.getRiskType());
															mRecord.setOneType(mSetting.getOneType());
															mRecord.setSelectedType(mSetting.getClass1());
															mRecord.setCurrentState(mSetting.getClass1());
															Long mRecordSuc = new DBService(mContext).insertHolefaceSettingRecord(mRecord);
															if(mSetting.getmClildList() != null && mSetting.getmClildList().size() > 0) {
																for(int l = 0; l < mSetting.getmClildList().size(); l ++ ) {
																	_HolefaceSetting mSSetting = mSetting.getmClildList().get(l);
																	if(mSSetting != null) {
																		_HolefaceSettingRecord mSRecord = new _HolefaceSettingRecord();
																		mSRecord.set$rowId(mSSetting.get$id());
																		mSRecord.set$riskId(riskId.toString());
																		mSRecord.set$holefaceId(mRightFaceEnter.toString());
																		mSRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																		mSRecord.setTypeNum("1");
																		mSRecord.setRiskType(mSSetting.getRiskType());
																		mSRecord.setOneType(mSSetting.getOneType());
																		mSRecord.setSelectedType(mSSetting.getClass1());
																		mSRecord.set$subId(mRecordSuc.toString());
																		mSRecord.setCurrentState(mSSetting.getClass1());
																		Long mRecordSub = new DBService(mContext).insertHolefaceSettingRecord(mSRecord);
																		
																		if(mRecordSub == 0) {
																			isInsert = false;
																			break;
																		}
																	}
																}
															}
															if(isInsert == false || mRecordSuc == 0) {
																isInsert = false;
																break;
															}
														}
													}
												} else {
													isInsert = false;
													break;
												}
											}
											}
										}
										face.setHole("右洞出口");
										Long mRightFaceExit = new DBService(mContext).insertLocHoloFace(face);
										if(mRightFaceExit == 0) {
											isInsert = false;
											break;
										} else {
											for(int j = 0; j < mRiskTypes.size(); j ++) {
											_HolefaceSettingInfo mHolefaceSetting = new _HolefaceSettingInfo();
											mHolefaceSetting.set$riskId(riskId.toString());
											mHolefaceSetting.set$holefaceId(mRightFaceExit.toString());
											mHolefaceSetting.setIsCheck("0");
											mHolefaceSetting.setRiskHoleName("右洞出口");
											mHolefaceSetting.setRiskTypeName(mRiskTypes.get(j));
											Long mSettingInfoId = new DBService(mContext).insertHolefaceSettingInfo(mHolefaceSetting);
											if(mSettingInfoId == 0) {
												isInsert = false;
												break;
											} else {
												List<_HolefaceSetting> mHolefaceSettingType = new DBService(mContext).findHolefaceSettingGroupByItype(mRiskTypes.get(j));
												if(mHolefaceSettingType != null && mHolefaceSettingType.size() > 0) {
													for(int k = 0; k < mHolefaceSettingType.size(); k ++) {
														_HolefaceSetting mSetting = mHolefaceSettingType.get(k);
														if(mSetting != null) {
															_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
															mRecord.set$riskId(riskId.toString());
															mRecord.set$rowId(mSetting.get$id());
															mRecord.set$holefaceId(mRightFaceExit.toString());
															mRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
															mRecord.set$subId("0");
															mRecord.setTypeNum("1");
															mRecord.setRiskType(mSetting.getRiskType());
															mRecord.setOneType(mSetting.getOneType());
															mRecord.setSelectedType(mSetting.getClass1());
															mRecord.setCurrentState(mSetting.getClass1());
															Long mRecordSuc = new DBService(mContext).insertHolefaceSettingRecord(mRecord);
															if(mSetting.getmClildList() != null && mSetting.getmClildList().size() > 0) {
																for(int l = 0; l < mSetting.getmClildList().size(); l ++ ) {
																	_HolefaceSetting mSSetting = mSetting.getmClildList().get(l);
																	if(mSSetting != null) {
																		_HolefaceSettingRecord mSRecord = new _HolefaceSettingRecord();
																		mSRecord.set$rowId(mSSetting.get$id());
																		mSRecord.set$riskId(riskId.toString());
																		mSRecord.set$holefaceId(mRightFaceExit.toString());
																		mSRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																		mSRecord.setTypeNum("1");
																		mSRecord.setRiskType(mSSetting.getRiskType());
																		mSRecord.setOneType(mSSetting.getOneType());
																		mSRecord.setSelectedType(mSSetting.getClass1());
																		mSRecord.set$subId(mRecordSuc.toString());
																		mSRecord.setCurrentState(mSSetting.getClass1());
																		Long mRecordSub = new DBService(mContext).insertHolefaceSettingRecord(mSRecord);
																		if(mRecordSub == 0) {
																			isInsert = false;
																			break;
																		}
																	}
																}
															}
															if(isInsert == false || mRecordSuc == 0) {
																isInsert = false;
																break;
															}
														}
													}
												} else {
													isInsert = false;
													break;											}
											}
											}
										}
									} else {
										_HoleFace face = new _HoleFace();
										face.setRiskId(riskId.toString());
										face.setHole(mPos);
										face.setCurrMile(mMileMap.get(mPos));
										Long mFace = new DBService(mContext).insertLocHoloFace(face);
										if(mFace == 0) {
											isInsert = false;
											break;
										} else {
											for(int j = 0; j < mRiskTypes.size(); j ++) {
											_HolefaceSettingInfo mHolefaceSetting = new _HolefaceSettingInfo();
											mHolefaceSetting.set$riskId(riskId.toString());
											mHolefaceSetting.set$holefaceId(mFace.toString());
											mHolefaceSetting.setIsCheck("0");
											mHolefaceSetting.setRiskHoleName(mPos);
											mHolefaceSetting.setRiskTypeName(mRiskTypes.get(j));
											
											Long mSettingInfoId = new DBService(mContext).insertHolefaceSettingInfo(mHolefaceSetting);
											
											if(mSettingInfoId == 0) {
												isInsert = false;
												break;
											} else {
												List<_HolefaceSetting> mHolefaceSettingType = new DBService(mContext).findHolefaceSettingGroupByItype(mRiskTypes.get(j));
												if(mHolefaceSettingType != null && mHolefaceSettingType.size() > 0) {
													for(int k = 0; k < mHolefaceSettingType.size(); k ++) {
														_HolefaceSetting mSetting = mHolefaceSettingType.get(k);
														if(mSetting != null) {
															_HolefaceSettingRecord mRecord = new _HolefaceSettingRecord();
															mRecord.set$riskId(riskId.toString());
															mRecord.set$rowId(mSetting.get$id());
															mRecord.set$holefaceId(mFace.toString());
															mRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
															mRecord.set$subId("0");
															mRecord.setTypeNum("1");
															mRecord.setRiskType(mSetting.getRiskType());
															mRecord.setOneType(mSetting.getOneType());
															mRecord.setSelectedType(mSetting.getClass1());
															mRecord.setCurrentState(mSetting.getClass1());
															Long mRecordSuc = new DBService(mContext).insertHolefaceSettingRecord(mRecord);
															if(mSetting.getmClildList() != null && mSetting.getmClildList().size() > 0) {
																for(int l = 0; l < mSetting.getmClildList().size(); l ++ ) {
																	_HolefaceSetting mSSetting = mSetting.getmClildList().get(l);
																	if(mSSetting != null) {
																		_HolefaceSettingRecord mSRecord = new _HolefaceSettingRecord();
																		mSRecord.set$rowId(mSSetting.get$id());
																		mSRecord.set$riskId(riskId.toString());
																		mSRecord.set$holefaceId(mFace.toString());
																		mSRecord.set$holefaceSettingInfoId(mSettingInfoId.toString());
																		mSRecord.setTypeNum("1");
																		mSRecord.setRiskType(mSSetting.getRiskType());
																		mSRecord.setOneType(mSSetting.getOneType());
																		mSRecord.setSelectedType(mSSetting.getClass1());
																		mSRecord.setCurrentState(mSSetting.getClass1());
																		mSRecord.set$subId(mRecordSuc.toString());
																		Long mRecordSub = new DBService(mContext).insertHolefaceSettingRecord(mSRecord);
																		if(mRecordSub == 0) {
																			isInsert = false;
																			break;
																		}
																	}
																}
															}
															if(isInsert == false || mRecordSuc == 0) {
																isInsert = false;
																break;
															}
														}
													}
												} else {
													isInsert = false;
													break;
												}
											}
											}
										}
									}
								}
							}  else {// mark at 20:23 2013/9/4; mark at 18:47 2013/9/28
								new DBService(mContext).delSingle("LOCAL_RISK", riskId.toString());
								isInsert = false;
								//Toast.makeText(mContext, "添加失败，请重新添加", Toast.LENGTH_SHORT).show();
								sendMessage(Constansts.MES_TYPE_2);
							}
							if(isInsert) {
								//Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
								sendMessage(Constansts.MES_TYPE_3);
							} else {
								new DBService(mContext).deleteHoloFace(riskId.toString());//1
								new DBService(mContext).delSingle("LOCAL_RISK", riskId.toString());//2
								new DBService(mContext).deleteHolefaceSettingInfo(riskId.toString());//3
								new DBService(mContext).deleteHolefaceSettingRecord(riskId.toString());//4
								new DBService(mContext).deleteHolefacePhotosByRikId(riskId.toString());//5
								//Toast.makeText(mContext, "添加失败，请重新添加", Toast.LENGTH_SHORT).show();
								sendMessage(Constansts.MES_TYPE_2);
							}
						}
					}
					mSelectItem = null;
					mSelectSection = null;
					mSelectTunnel = null;
				} else {
					sendMessage(Constansts.MES_TYPE_2);
				}
				
			}
		}).start();	
	}
	//####from EditView 2 Spinner#### maybe more easy 
	OnItemSelectedListener mHighWayEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			mSectionList = new DBService(mContext).getSectionListByObj(item.getId());
			if(mSectionList != null && mSectionList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mSectionList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							if(mSectionList.get(position) != null) {
								String mTextContent = mSectionList.get(position).getText();
								if(mTextContent == null || "".equals(mTextContent) ) {
									mTextContent = "选择标段";
								} 
								textView.setText(mTextContent);
							}
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						if(mSectionList.get(position) != null) {
							String mTextContent = mSectionList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = "选择标段";
								textView.setTextColor(Color.GRAY);
							} 
							textView.setText(mTextContent);
						} 
						return convertView;
					}
				 };
				 mSectionSpinner.setAdapter(mSectionAdapter);
				 mSectionSpinner.setOnItemSelectedListener(mSectionEditListener);
			} else {
				mSectionSpinner.setmBackgroundView(R.layout.common_spinner3);
			    mSectionSpinner.setmInitText("选择标段");
			    mSectionSpinner.setAdapter(mSectionSpinner.getmAdapter());
			    mSectionSpinner.setmTextColor(Color.GRAY);
			    mTunnelSpinner.setmBackgroundView(R.layout.common_spinner3);
				mTunnelSpinner.setmInitText("选择隧道");
			    mTunnelSpinner.setAdapter(mTunnelSpinner.getmAdapter());
			    mTunnelSpinner.setmTextColor(Color.GRAY);
			}
			mSelectItem = item;
			mSelectSection = null;
			mSelectTunnel = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	OnItemSelectedListener mSectionEditListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			mTunnelList = null;
			mTunnelList = new DBService(mContext).getTunnelListByObj(item.getId());
			mTunnelSpinner.setAdapter(mTunnelSpinner.getAdapter());
			if(mTunnelList != null && mTunnelList.size() > 0) {
				ArrayAdapter<_ListObj> mSectionAdapter = new ArrayAdapter<_ListObj>(mContext,R.layout.common_spinner3, mTunnelList){
					 @Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						 if (convertView == null) {
								convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
							} 
							TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
							if(mTunnelList.get(position) != null) {
								String mTextContent = mTunnelList.get(position).getText();
								if(mTextContent == null || "".equals(mTextContent) ) {
									mTextContent = "选择隧道";
								} 
								textView.setText(mTextContent);
							} 
							return convertView;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner3, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
						if(mTunnelList.get(position) != null) {
							String mTextContent = mTunnelList.get(position).getText();
							if(mTextContent == null || "".equals(mTextContent) ) {
								mTextContent = "选择隧道";
								textView.setTextColor(Color.GRAY);
							} 
							textView.setText(mTextContent);
						}
						return convertView;
					}
				 };
				 mTunnelSpinner.setAdapter(mSectionAdapter);
				 mTunnelSpinner.setOnItemSelectedListener(mTunnelEditListener);
			} else {
				mTunnelSpinner.setmBackgroundView(R.layout.common_spinner3);
				mTunnelSpinner.setmInitText("选择隧道");
			    mTunnelSpinner.setAdapter(mTunnelSpinner.getmAdapter());
			    mTunnelSpinner.setmTextColor(Color.GRAY);
			}
			mSelectSection = item;
			mSelectTunnel = null;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	OnItemSelectedListener mTunnelEditListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_ListObj item = (_ListObj) parent.getAdapter().getItem(position);
			mSelectTunnel = item;
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	};
	
	//====================================================================
	// Part of OnItemClickListener
	//====================================================================
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
	
	
	
	OnItemClickListener mListViewItemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RisksListAdapter.ViewHolder viewHolder = (RisksListAdapter.ViewHolder)arg1.getTag();
			if(viewHolder != null) {
				_LocRisk risk = viewHolder.mRisk;
				if(risk != null) {
				List<_HoleFace> mHoloFaceList = new DBService(mContext).getHoloFaceList(risk.getId());
				if(mHoloFaceList != null && mHoloFaceList.size() > 0) {
					double mPercentScreen = (double)((mHoloFaceList.size() + 2) * measureButtonHeight() + ScreenUtil.dp2px(mContext, 69))/(double)mScreenHeight;
					if(mPercentScreen == 0) {
						mPercentScreen = 0.4;
					}
					mHolofaceDialog = new MDialog(mContext, R.style.dialog);
					mHolofaceDialog.createDialog(R.layout.risk_holo_face_pop, 0.9, mPercentScreen, getWindowManager());
					mHolofaceDialog.setCanceledOnTouchOutside(false);
					LinearLayout container = (LinearLayout)mHolofaceDialog.findViewById(R.id.p35_risk_holo_face_container);
					
					for(int i = 0; i < mHoloFaceList.size(); i ++) {
						_HoleFace mHoloface = mHoloFaceList.get(i);
						if(mHoloface != null) {
							String mBtnText = mHoloface.getHole();
							if(!"1".equals(mHoloface.getIsFinish())) {
								mBtnText = mBtnText + "(未完成)";
							} else {
								mBtnText = mBtnText + "(完成)";
							}
							DefineButton mButton = new DefineButton(mContext);
							mButton.setText(mBtnText);
							mButton.setTextColor(getResources().getColor(R.color.white));
							//mButton.setBackgroundResource(R.drawable.p00_blank_btn_selector);
							mButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
							mButton.set$id(mHoloface.get_id());// Watch out I
							mButton.set$rowId(risk.getId());   // Watch out II
							container.addView(mButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
							mButton.setOnClickListener(mHolofaceListener);
							
							TextView textView1 = new TextView(mContext);
							container.addView(textView1, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,7));
						}
					}
					DefineButton  mUpButton = new DefineButton(mContext);
					mUpButton.setText("上传");
					mUpButton.setTextColor(getResources().getColor(R.color.white));
					mUpButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
					mUpButton.set$id(risk.getId());
					mUpButton.setmRisk(risk);
					container.addView(mUpButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
					mUpButton.setOnClickListener(mUpListener);
					
					TextView textView2 = new TextView(mContext);
					container.addView(textView2, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,7));
					
					DefineButton  mDelButton = new DefineButton(mContext);
					mDelButton.setText("删除");
					mDelButton.setTextColor(getResources().getColor(R.color.white));
					mDelButton.setBackgroundResource(R.drawable.common_blue_btn_selector);
					mDelButton.set$id(risk.getId());
					container.addView(mDelButton, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(mContext, 46)));
					mDelButton.setOnClickListener(mDelListener);
				}
				}
			}
			
		}
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 402 || resultCode == 0) {
			String $riskId = "";
			if(data != null && data.getExtras() != null) {
				$riskId = data.getExtras().getString("$riskId");
			}
			List<_HolefaceSettingInfo> mSettingInfoList = new DBService(mContext).getHolefaceSettingInfoByRiskId($riskId,null);
			if(mSettingInfoList != null && mSettingInfoList.size() > 0) {
				boolean mSettingInfoCheck = true;
				for(int i = 0; i < mSettingInfoList.size(); i ++) {
					_HolefaceSettingInfo mSettingInfo = mSettingInfoList.get(i);
					if(mSettingInfo != null && !"1".equals(mSettingInfo.getIsCheck())) {
						mSettingInfoCheck = false;
					} 
				}
				if(mSettingInfoCheck) {
					new DBService(mContext).updateRisk($riskId, "3");//"已检查"
				} else {
					new DBService(mContext).updateRisk($riskId, "2");//"正检查"
				}
			}
			refeshList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void clearCache() {
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
	protected void onDestroy() {
		clearCache();
		super.onDestroy();
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if (mDialog != null)
				mDialog.dismiss();
			refeshList();
			Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.ERRER:
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1 :
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "您有图片未上传成功", Toast.LENGTH_SHORT).show();
			refeshList();
			break;
		case Constansts.MES_TYPE_2 :
			if (mDialog != null)
				mDialog.dismiss();
			refeshList();
			Toast.makeText(mContext, "添加失败，请重新添加", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_3 :
			if (mDialog != null)
				mDialog.dismiss();
			refeshList();
			Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
			break;
		default:
			if (mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
