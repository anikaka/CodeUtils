package com.tongyan.yanan.act.quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.CommonListViewAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: QualityHardingListAct.java 
 * @author Rubert
 * @date 2014-8-14 PM 04:44:09 
 * @version V1.0 
 * @Description: 地基土压实度列表
 */
public class QualityHardingListAct extends FinalActivity {
	@ViewInject(id=R.id.listView_data) ListView mListView;
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	@ViewInject(id=R.id.common_button_container, click="addHardingListener") RelativeLayout mRightBtn;
	@ViewInject(id=R.id.title_common_add_view) ImageView mAddView;
	
	private Context mContext = this;
	private HashMap<String, String> mPactMap = null;
	private HashMap<String, String> mQualityMap = null;
	
	private CommonListViewAdapter mAdapter;
	
	private ArrayList<HashMap<String, String>> mHardingList = new ArrayList<HashMap<String, String>>();
	
	private SharedPreferences mPreferences;
	private String mUserid = null;
	
	private Dialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mTitleContent.setText("压实度列表");
		mRightBtn.setVisibility(View.VISIBLE);
		mAddView.setVisibility(View.VISIBLE);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("PactMap");
			mQualityMap = (HashMap<String, String>)getIntent().getExtras().get("QualityMap");
			
			mAdapter = new CommonListViewAdapter(mContext, mHardingList, R.layout.common_state_listview_item, QualityHardingListAct.class);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(onItemClickListener);
			refreshListView();
		}
	}
	
	
	public void refreshListView() {
		if(mPactMap != null && mQualityMap != null) {
			ArrayList<HashMap<String, String>> list = new DBService(mContext).getHardingByUserid(mUserid, mPactMap.get("NewId"), mQualityMap.get("NewId"));
			if(mHardingList != null) {
				mHardingList.clear();
				if(list != null) {
					mHardingList.addAll(list);
				}
				if(mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CommonListViewAdapter.HolderViewProgressMonthProject mViewHolder = (CommonListViewAdapter.HolderViewProgressMonthProject)arg1.getTag();
			/*Intent intent = new Intent(mContext, QualityHardingAddAct.class);
			intent.putExtra("PactMap", mPactMap);
			intent.putExtra("QualityMap", mQualityMap);
			intent.putExtra("IntentType", "modify");
			intent.putExtra("ItemMap", mViewHolder.mMapProgressMonth);
			startActivityForResult(intent, 3333);*/
			showDialog(mViewHolder.mMapProgressMonth);
		}
	};
	
	
	
	public void showDialog(final HashMap<String, String> map) {
	 	final Dialog  mDialog=new Dialog(mContext);
	 	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_progressproject_upload);
		mDialog.show();
		//填写/查看按钮
		Button mButModify=(Button)mDialog.findViewById(R.id.butModify_dialog_progssproject_upload);
		mButModify.setText("修改/查看");
		//上传
		Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_dialog_progssproject_upload);
		//删除
		Button mButDel=(Button)mDialog.findViewById(R.id.butDelete_butModify_dialog_progssproject_upload);
		
		mButModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("2".equals(map.get("State"))) {//查看
					Intent intent = new Intent(mContext, QualityHardingDetailAct.class);
					intent.putExtra("PactMap", mPactMap);
					intent.putExtra("QualityMap", mQualityMap);
					intent.putExtra("ItemMap", map);
					startActivity(intent);
				} else {//修改
					Intent intent = new Intent(mContext, QualityHardingAddAct.class);
					intent.putExtra("PactMap", mPactMap);
					intent.putExtra("QualityMap", mQualityMap);
					intent.putExtra("IntentType", "modify");
					intent.putExtra("ItemMap", map);
					startActivityForResult(intent, 3333);
				}
				mDialog.dismiss();
			}
		});
		
		//上传或者送审
		mButUpload.setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(map.get("State"))) {
					Toast.makeText(mContext, "该记录还未完成", Toast.LENGTH_SHORT).show();
					return;
				}
				if("2".equals(map.get("State"))) {
					Toast.makeText(mContext, "该记录已上传", Toast.LENGTH_SHORT).show();
					return;
				}
				update(map);//上传
				mDialog.dismiss();
			}
		});
		//删除
		mButDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder mDia=new AlertDialog.Builder(mContext);
				mDia.setMessage("确定要删除吗");
				mDia.setPositiveButton("是", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
						new DBService(mContext).delQualityHarding(map.get("ID"));
						new DBService(mContext).delHardingPoints(map.get("ID"));
						refreshListView();
					}
				});
				mDia.setNegativeButton("否", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
					}
				});
				mDia.show();
			}
		});
	}
	
	/**
	 * 上传
	 */
	public void update(final HashMap<String, String> map) {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		setProgressBarIndeterminateVisibility(true);
		mDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {//TODO
				StringBuffer mData=new StringBuffer();
				mData.append("{"); //开始标记
				mData.append("\"DetectionAreaId\":\"");
				mData.append(map.get("DetectionAreaId"));
				mData.append("\",\"PersonId\":\"");
				mData.append(mUserid);
				mData.append("\",\"UnitId\":\"");
				mData.append(map.get("DetectionCompanyId"));
				mData.append("\",\"TestCountent\":\"");
				mData.append(JsonTools.getURLEncoderString(map.get("ExpContent")));
				mData.append("\",\"TestConclusion\":\"");
				mData.append(JsonTools.getURLEncoderString(map.get("ExpResult")));
				mData.append("\",\"DetectionDate\":\"");
				mData.append(map.get("DetectionTime"));//检测时间
				mData.append("\",\"VirtualHeight\":\"");
				mData.append(map.get("Thickness")==null ? String.valueOf(0) : map.get("Thickness"));//虚铺厚度
				mData.append("\",\"ImpactCount\":\"");
				mData.append(map.get("Variate"));//冲击变数
				mData.append("\",\"CompactionHeight\":\"");
				mData.append(map.get("PressThickness"));//压实厚度
				mData.append("\",\"DetectionArea\":\"");
				mData.append(map.get("Area"));//面积
				mData.append("\",\"RollingMachinery\":\"");
				mData.append(map.get("Machine"));//碾压机械
				mData.append("\",\"LevelHeight\":\"\",");
				mData.append("\"DetectionLaye\":[");
				
				HashMap<String,ArrayList<HashMap<String, String>>> mHardingMap = new DBService(mContext).getHardingPointsMap(map.get("ID"));
				int k = 1;
				int mapCount = mHardingMap.size();
				if(mHardingMap != null) {
					for(Map.Entry<String, ArrayList<HashMap<String, String>>> s : mHardingMap.entrySet()) {
						mData.append("{\"LayerName\":\"");
						String mLayerNums = s.getKey();
						mData.append(mLayerNums);
						mData.append("\",\"DetectionData\":[");
						ArrayList<HashMap<String, String>> list = s.getValue();
						if(list != null && list.size() > 0) {
							for(int i = 0, len = list.size(); i < len; i ++) {
								HashMap<String, String> m = list.get(i);
								mData.append("{\"PointX\":\"");
								mData.append(m.get("PointX"));
								mData.append("\",\"PointY\":\"");
								mData.append(m.get("PointY"));
								mData.append("\",\"PointZ\":\"");
								mData.append(m.get("PointZ"));
								mData.append("\",\"compaction\":\"");
								mData.append(m.get("Compaction"));
								mData.append("\",\"Density\":\"");
								mData.append(m.get("Density"));
								mData.append("\",\"MaxDensity\":\"");
								mData.append(m.get("MaxDensity"));
								mData.append("\",\"EarthNature\":\"");
								mData.append(JsonTools.getURLEncoderString(m.get("EarthNature")));
								if(i == len - 1) {
									mData.append("\"}");
								} else {
									mData.append("\"},");
								}
							}
						}
						mData.append("]");
						if(k == mapCount) {
							mData.append("}");
						} else {
							mData.append("},");
						}
						k ++;
					}
				}
				mData.append("]}");//结束记号
				
				HashMap<String, String>  mParameters=new HashMap<String, String>();
				mParameters.put("method", Constants.METHOD_OF_ADDDETECTIONDATA);
				mParameters.put("key",Constants.PUBLIC_KEY);
				mParameters.put("userId", mUserid);
				mParameters.put("data", mData.toString());
				String mResponseBody="";
				try{				
					mResponseBody = HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_QUALITY, mParameters, mContext));
					if(JsonTools.getCommonResult(mResponseBody)) {
						new DBService(mContext).updateQualityHardingState(map.get("ID"), "2");
						sendFMessage(Constants.SUCCESS);
					} else {
						sendFMessage(Constants.ERROR);
					}
				}catch(Exception e){
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				}
			}
			
		}).start();
		
	}
	
	
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 3333) {
			refreshListView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public void addHardingListener(View v) {
		Intent intent = new Intent(mContext, QualityHardingAddAct.class);
		intent.putExtra("PactMap", mPactMap);
		intent.putExtra("IntentType", "add");
		intent.putExtra("QualityMap", mQualityMap);
		startActivityForResult(intent, 3333);
	}
	
	
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.SUCCESS:
			closeDialog();
			refreshListView();
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			break;
		case Constants.ERROR:
			closeDialog();
	 		Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
	 		break;
		case Constants.CONNECTION_TIMEOUT:
			closeDialog();
	 		Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
	 		break;
		default:
			break;
		}
	}
	
	
	
}
