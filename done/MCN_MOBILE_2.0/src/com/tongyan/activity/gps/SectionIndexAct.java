
package com.tongyan.activity.gps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mapapi.model.LatLng;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBSectionService;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._User;
import com.tongyan.utils.CommonUtils;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MapUtils;
import com.tongyan.utils.WebServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @className SectionIndexAct
 * @author wanghb
 * @date 2014-07-24 AM 10:16:37
 * @desc 工程标段列表
 *   功能1：从标段到工程编号，层级展示
 *   功能2：在工程编号的第四级 可以拍照并上传
 *   功能3：在工程编号的第三级 可以采点并上传 
 * 
 */

public class SectionIndexAct extends Activity {
	
	private TextView mTitleContent;
	private Button mBackBtn, mCameraBtn;
	private ListView mListView;
	private static ArrayList<HashMap<String, String>> mReferenceList = new ArrayList<HashMap<String, String>>();
	
	private static GpsSectionMenuListAdapter mAdapter;
	
	private Context mContext = this;
	
	private LinkedList<HashMap<String, String>> mLinkedList = new LinkedList<HashMap<String, String>>();
	//根据不同的类型，来使用不同的参数
	private static final int GET_ALL_CONTENT_TYPE = 1;
	private static final int GET_CODE_CONTENT_TYPE = 2;
	private static final int GET_NAME_CONTENT_TYPE = 3; 
	
	private int mIntentType = 0;// 0:周边工程进入,1:根目录进去
	
	private _User mLocalUser = null;
	
	private  Location mLocation;
	private  double mLongitude;//经度
	private  double mLatitude;//纬度
	private  LocationManager mGpsManager  = null;
	private  Dialog mDialog = null;
	private  Timer mTimer = null;
	private  boolean  mDialogFlag=false;
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constansts.SUCCESS:
				updatePoints((PointData)msg.obj);
				break;
			case Constansts.MES_TYPE_1:
				Toast.makeText(mContext, "请不要在室内或封闭的地方进行采点", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.MES_TYPE_2:
				if(mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				if(mTimer != null) {
					mTimer = null;
				}
				Toast.makeText(mContext, "数据更新成功", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.MES_TYPE_3 :
				if(mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				if(mTimer != null) {
					mTimer = null;
				}
				Toast.makeText(mContext, "数据已缓存", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.DEFAULT :
				if(mDialog != null) {
					mDialog.dismiss();
					mDialog = null;
				}
				if(mTimer != null) {
					mTimer = null;
				}
				Toast.makeText(mContext, "上传失败,请重试", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		mAdapter = new GpsSectionMenuListAdapter(mContext, mReferenceList, R.layout.gps_section_menu_list_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		Bundle mBundle = getIntent().getExtras();
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mLocalUser = mApplication.localUser;
		
		if(mBundle != null) {
			HashMap<String, String> map = (HashMap<String, String>)mBundle.get("MoreDetail");
			//secId,subId,subCode,subName
			String mParentId = map.get("subId");
			String mSectionId = map.get("secId");//标段id
			if(mParentId != null && !"".equals(mParentId)) {
				HashMap<String, String> mNewMap = new HashMap<String, String>();
				mNewMap.put("sid", mSectionId);//工程编号id
				mNewMap.put("RowId", mParentId);//父级工程编号id
				mNewMap.put("Allcode", map.get("subCode"));//编号
				mNewMap.put("AllName", map.get("subName"));//
				mNewMap.put("sContent", map.get("subName") + "("+ map.get("subCode") +")");
				mLinkedList.add(mNewMap);
				refreshSubListView(mSectionId, mParentId);
				
				mTitleContent.setText(map.get("subName"));
				mIntentType = 1;//
			} else {
				refreshListView(mContext);
				mIntentType = 0;
			}
		} else {
			refreshListView(mContext);
			mIntentType = 0;
		}
	}
	
	/**
	 * 第一次刷新列表
	 * @param mContext
	 */
	public static void refreshListView(Context mContext) {
		ArrayList<HashMap<String, String>> list = new DBService(mContext).getSectionList();
		if(mReferenceList != null) {
			mReferenceList.clear();
			if(list != null) {
				mReferenceList.addAll(list);
				list = null;
			}
			mAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 计算当前 进入的层级
	 * @param mSectionId
	 * @param mParentId
	 * @return
	 */
	public HashMap<String, String> couputeCurrentCent(String mSectionId, String mParentId) {
		HashMap<String, String> mParamMap = new HashMap<String, String>();
		mParamMap.put("RowId", mParentId);
		mParamMap.put("SectionId", mSectionId);
		DBSectionService.mCacheNum = 0;
		new DBSectionService().computeSection(mParamMap);
		return mParamMap;
	}
	
	/**
	 * 进入下一级后，刷新列表
	 * @param mSectionId
	 * @param mParentId
	 */
	public void refreshSubListView(String mSectionId, String mParentId) {
		HashMap<String, String> mParamMap =  couputeCurrentCent(mSectionId, mParentId);
		if (DBSectionService.mCacheNum >= 4) {//计算工程能否拍照
			mCameraBtn.setVisibility(View.VISIBLE);
			mCameraBtn.setTag(mParamMap);
		} else {
			mCameraBtn.setVisibility(View.INVISIBLE);
		}
		
		boolean isShowCollectBtn = false;
		if (DBSectionService.mCacheNum >= 2) {
			isShowCollectBtn = true;
		}
		
		ArrayList<HashMap<String, String>> list = new DBSectionService().getSectionList(mSectionId, mParentId, isShowCollectBtn);
		if(mReferenceList != null) {
			mReferenceList.clear();
			if(list != null) {
				mReferenceList.addAll(list);
				list = null;
			}
			mAdapter.notifyDataSetChanged();
		}
	}
	
	
   OnItemClickListener mItemClickListener = new OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		GpsSectionMenuListAdapter.ViewHolder mViewHolder = (GpsSectionMenuListAdapter.ViewHolder)arg1.getTag();
		HashMap<String, String> map = mViewHolder.mData;
		if(map != null) {
			String sid = map.get("sid");
			String rowId = map.get("RowId");
			boolean isExist = new DBSectionService().countSectionExist(sid, rowId);	
			/*if(sid != null && !sid.equals(rowId)) {
				Toast.makeText(mContext, "请下载工程数据", Toast.LENGTH_SHORT).show();
				return;
			} else {*/
				if(isExist) {//下一级
					refreshSubListView(sid, rowId);
					mLinkedList.add(map);
					mTitleContent.setText(getTitleBuffer(GET_ALL_CONTENT_TYPE));
					/*HashMap<String, String> mParamMap = new HashMap<String, String>();
					mParamMap.put("RowId", rowId);
					mParamMap.put("SectionId", sid);
					DBSectionService.mCacheNum = 0;
					new DBSectionService().computeSection(mParamMap);
					if (DBSectionService.mCacheNum >= 4) {
						mCameraBtn.setVisibility(View.VISIBLE);
						mCameraBtn.setTag(mParamMap);
					} else {
						mCameraBtn.setVisibility(View.INVISIBLE);
					}*/
				} else {//最后一级
					couputeCurrentCent(sid, rowId);
					if (DBSectionService.mCacheNum == 0) {
						Toast.makeText(mContext, "请下载工程数据", Toast.LENGTH_SHORT).show();
						return;
					} else {
						showDialog(map);
					}
				}
			//}
		}
	}
   };
   
//   private Dialog mBottomDialog = null;
   public void showDialog(final HashMap<String, String> map) {
	   Dialog   mBottomDialog = null;
	   if(mBottomDialog==null){
		   mBottomDialog=new AlertDialog.Builder(this).create();
	   }
	    Window window = mBottomDialog.getWindow(); 
	    window.setWindowAnimations(R.style.dialog_anim_bottom_top);  //添加动画  
	    if(mDialogFlag==false){	    	
	    	mBottomDialog.show();
	    }
		//注意此处要放在show之后 否则会报异常
	    mBottomDialog.setContentView(R.layout.gps_section_detail_dialog);
	    mBottomDialog.setCanceledOnTouchOutside(false);
		TextView NameText = (TextView)mBottomDialog.findViewById(R.id.p26_gps_project_name);
		TextView CodeText = (TextView)mBottomDialog.findViewById(R.id.p26_gps_project_code);
		NameText.setText(getTitleBuffer(GET_NAME_CONTENT_TYPE));
		CodeText.setText("编号:" + getTitleBuffer(GET_CODE_CONTENT_TYPE));
		
		Button ProgressBtn = (Button)mBottomDialog.findViewById(R.id.p26_gps_project_info_progress);
		Button MeasureBtn = (Button)mBottomDialog.findViewById(R.id.p26_gps_project_info_measure);
		Button PhotographBtn = (Button)mBottomDialog.findViewById(R.id.p26_gps_project_info_photograph);
		
		//监理指令//新增
		Button SupervisorBtn = (Button)mBottomDialog.findViewById(R.id.p26_gps_supervisor_command_btn);
		
		
		SupervisorBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog d = new AlertDialog.Builder(mContext).create();
				if(d!=null){
					d.show();
					//注意此处要放在show之后 否则会报异常
					d.setContentView(R.layout.common_supervisor_command_select_dialog);
					d.setCanceledOnTouchOutside(false);
				}
				final CheckBox   monitorOrder =(CheckBox)d.findViewById(R.id.gps_checkbox); //监控指令
		        final CheckBox   projectOfficeOrder=(CheckBox)d.findViewById(R.id.gprs_checkbox); //项目办指令
		        final Button btnConfirm=(Button)d.findViewById(R.id.checking_gps_selectd_sure_btn);  //确认按钮
		        final Button btnCancel=(Button)d.findViewById(R.id.checking_gps_selectd_cancel_btn);  //取消按钮
		        		        
		        monitorOrder.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						projectOfficeOrder.setChecked(false);
					}
				});
		        
		        
		        projectOfficeOrder.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						monitorOrder.setChecked(false);
						
					}
				});
		        
		        btnConfirm.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Bundle bundle=new Bundle();
						bundle.putString("newId", map.get("RowId"));//工程编号Id
						bundle.putString("sectionId", map.get("sid")); //标段Id
						if(monitorOrder.isChecked()){
							bundle.putString("commandType","0");//指令类型 //监控指令0  项目办指令1
						}
						if(projectOfficeOrder.isChecked()){
							bundle.putString("commandType","1");
						}
						if(monitorOrder.isChecked() || projectOfficeOrder.isChecked()){		
							if(d!=null){
								d.cancel();
							}
							Intent intent=new Intent(mContext, CommandAct.class);
							intent.putExtra("commandBundle",bundle); //工程编号Id
							startActivity(intent);
						}
					}
				});
		        
		        btnCancel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						d.dismiss();
					}
				});
			}
		});
		
		//进度信息按钮监听
		ProgressBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SectionIndexDetailAct.class);
				intent.putExtra("subId", map.get("RowId"));
				intent.putExtra("type", "progress");
				startActivity(intent);
			}
		});
		
		//计量信息按钮监听
		MeasureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SectionIndexDetailAct.class);
				intent.putExtra("subId", map.get("RowId"));
				intent.putExtra("type", "measure");
				startActivity(intent);
			}
		});
		
		//拍照信息按钮监听
		PhotographBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SectionCameraAct.class);
				HashMap<String, String> CacheMap = new HashMap<String, String>();
				CacheMap.put("subId", map.get("RowId"));
				CacheMap.put("secId", map.get("SectionId"));
				intent.putExtra("CacheMap", CacheMap);
				startActivity(intent);
			}
		});
   }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mDialogFlag=true;
		return super.onKeyDown(keyCode, event);
	}
	
	public void initView() {
		setContentView(R.layout.gps_sections_menu_list);
		mTitleContent = (TextView)findViewById(R.id.titleContent);
		mBackBtn = (Button)findViewById(R.id.gps_section_menu_back);
		mListView = (ListView)findViewById(R.id.gps_section_menu_list);
		mCameraBtn = (Button)findViewById(R.id.gps_section_carema);
		mBackBtn.setOnClickListener(mBackListener);
		mCameraBtn.setOnClickListener(mCameraBtnListener);
		mTitleContent.setText("标段目录");
	}
	
	
	OnClickListener mCameraBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			HashMap<String, String> map = (HashMap<String, String>)v.getTag();
			if(map != null) {
				Intent intent = new Intent(mContext, SectionCameraAct.class);
				map.put("subId", map.get("RowId"));
				map.put("secId", map.get("RowId"));
				intent.putExtra("CacheMap", map);
				startActivity(intent);
			} else {
				Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	
	
	/**
	 * 拼装标题
	 */
	private String getTitleBuffer(int type) {
		StringBuffer b = new StringBuffer();
		if(mLinkedList != null && mLinkedList.size() > 0) {
			for(int i = 0,len = mLinkedList.size(); i < len; i ++) {
				HashMap<String, String> m = mLinkedList.get(i);
				if(m != null) {
					if(type == GET_ALL_CONTENT_TYPE) {
						b.append(m.get("sContent"));
					}
					if(type == GET_CODE_CONTENT_TYPE) {
						b.append(m.get("Allcode"));
					}
					if(type == GET_NAME_CONTENT_TYPE) {
						b.append(m.get("AllName"));
					}
					if(i != len - 1) {
						if(type == GET_CODE_CONTENT_TYPE) {
							b.append(".");
						} else {
							b.append("\\");
						}
					}
				}
			}
		}
		return b.toString();
	}
	
	
	/**
	 * 标题栏回退操作
	 */
	OnClickListener mBackListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mLinkedList.size() == 0) {
				finish();
			} else {
				mLinkedList.removeLast();
				if(mLinkedList.size() == 0) {
					if(mIntentType == 1) {
						finish();
					} else {
						refreshListView(mContext);
						mTitleContent.setText("标段目录");
					}
				} else {
					HashMap<String, String> map = mLinkedList.getLast();
					if(map != null)
					refreshSubListView(map.get("sid"), map.get("RowId"));
					String buffer = getTitleBuffer(GET_ALL_CONTENT_TYPE);
					if(buffer != null && buffer.length()> 20) {
						mTitleContent.setTextSize(13);
					}
					mTitleContent.setText(buffer);
				}
			}
		}
	};
	
	
	/**
	 * GPS定位功能
	 */
	public void collectPoint(final String mRowid) {
		if (!CommonUtils.getLocationServiceState(mContext)) {
			//CommonUtils.toggleGPS(mContext);
			//Settings.Secure.setLocationProviderEnabled( mContext.getContentResolver(), LocationManager.GPS_PROVIDER, true);
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mContext.startActivity(intent);
		} else {
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.show();
	    	//注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.common_loading_process_dialog);
			mDialog.setCanceledOnTouchOutside(false);
			
			
			mDialog.setOnKeyListener(new OnKeyListener(){
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(mTimer != null) {
						mTimer.cancel();
						mTimer = null;
						mLongitude = 0;
						mLatitude = 0;
					}
					return false;
				}
				
			});
			
			if(mGpsManager == null) {
				mGpsManager  = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE); 
			}
			//从可用的位置提供器中，匹配以上标准的最佳提供器
			final String provider = mGpsManager.getBestProvider(MapUtils.getCriteria(), true);
			if(mGpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				mGpsManager.requestLocationUpdates(provider, 1000, 1, locationListener);
				mLocation = mGpsManager.getLastKnownLocation(provider);
				mTimer  = new Timer();
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						mLocation = mGpsManager.getLastKnownLocation(provider);
						Message mMessage = new Message();
						if(mLocation != null) {
							mLatitude = mLocation.getLatitude();
							mLongitude = mLocation.getLongitude();
							if(mLatitude != 0 & mLongitude != 0) {
								LatLng mLatLng = MapUtils.getGeoPointByGps(mLatitude, mLongitude);
								LatLng mLatLngOffset = new LatLng(mLatLng.latitude + 0.0005, mLatLng.longitude + 0.0005);
								PointData mPointData = new PointData();
								mPointData.mRowid = mRowid;
								mPointData.mContent = mLatLng.longitude + " " + mLatLng.latitude + "," + mLatLngOffset.longitude + " " + mLatLngOffset.latitude;
								mMessage.obj = mPointData;
								mMessage.what = Constansts.SUCCESS;
								mHandler.sendMessage(mMessage);
								this.cancel();
							}
						} else {
							mHandler.sendEmptyMessage(Constansts.MES_TYPE_1);
						}
					}
				},1000, 3000);
			}
		}
	}
	
	/**
	 * 上传数据
	 * @param msg
	 */
	public void updatePoints(final PointData mPointData) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(mPointData == null) {
					mHandler.sendEmptyMessage(Constansts.DEFAULT);
					return;
				}
				String param = "{ProjectId:'"+ mPointData.mRowid +"', GeographyString:'LINESTRING("+ mPointData.mContent +")'}";
				try {
					String str = WebServiceUtils.getRequestStr(mLocalUser.getUsername(), mLocalUser.getPassword(), null, null, "ProjectGeometryInfo", param, Constansts.METHOD_OF_UPDATE, mContext);
					if(new Str2Json().commonJsonParse(str)) {
						mHandler.sendEmptyMessage(Constansts.MES_TYPE_2);
					} else {
						//在插入数据前，先判断是否有相同的工程编号的记录，如果有，则修改数据
						if(new DBService(mContext).isExistProjectPoints(mPointData.mRowid)) {
							if(new DBService(mContext).updateProjectPoints(mPointData.mRowid, mPointData.mContent)) {
								mHandler.sendEmptyMessage(Constansts.MES_TYPE_3);
							} else {
								mHandler.sendEmptyMessage(Constansts.DEFAULT);
							}
						} else {
							if(new DBService(mContext).insertProjectPoints(mPointData.mRowid, mPointData.mContent)) {
								mHandler.sendEmptyMessage(Constansts.MES_TYPE_3);
							} else {
								mHandler.sendEmptyMessage(Constansts.DEFAULT);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(Constansts.DEFAULT);
				} 
			}
			
		}).start();
	}
	
	/**
	 * 通过GPS定位
	 */
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {//位置信息变化时触发
			if(location != null)
				mLocation = location;
			else 
				mLocation = null;
		}
		@Override
		public void onProviderDisabled(String provider) {}//GPS禁用时触发
		@Override
		public void onProviderEnabled(String provider) {}// GPS开启时触发
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}// GPS状态变化时触发
	};
	
	
	public class PointData{
		public String mRowid;
		public String mContent;
	}
	
	/**
	 * 使用内部类，来降低模块的耦合度
	 * @Title: SectionIndexAct.java 
	 * @author Rubert
	 * @date 2014-10-12 下午08:14:43 
	 * @version V1.0 
	 * @Description: TODO
	 */
	public class GpsSectionMenuListAdapter extends BaseAdapter {
		
		private int resource;//绑定条目界面
		private ArrayList<HashMap<String, String>> mProjectList;
		private LayoutInflater inflater;
		
		public GpsSectionMenuListAdapter(Context context, ArrayList<HashMap<String, String>> mProjectList, int resource) {
			this.mProjectList = mProjectList;
			this.resource = resource;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return mProjectList.size();
		}

		@Override
		public Object getItem(int position) {
			return mProjectList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(resource, null);
				holder = new ViewHolder();
				holder.mContent = (TextView) convertView.findViewById(R.id.section_item_name);
				holder.mItemTarget = (ImageView) convertView.findViewById(R.id.section_item_menu_next);
				holder.mCollectBtn = (Button) convertView.findViewById(R.id.section_item_menu_loc);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final HashMap<String, String> mItem = mProjectList.get(position);
			if(mItem != null) {
				holder.mContent.setText(mItem.get("sContent"));
				holder.mData = mItem;
				//判断是否可以采点
				if("true".equals(mItem.get("isShowCollectBtn"))) {
					holder.mCollectBtn.setVisibility(View.VISIBLE);
				} else {
					holder.mCollectBtn.setVisibility(View.INVISIBLE);
				}
			}
			
			/**
			 * 采点监听
			 */
			holder.mCollectBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					collectPoint(mItem.get("RowId"));
				}
			});
			
			return convertView;
		}
		
		
		
		public final class ViewHolder {
			public TextView mContent;
			public ImageView mItemTarget;
			public Button mCollectBtn;
			public HashMap<String,String> mData;
		}
		
	}
}
