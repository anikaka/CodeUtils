package com.tongyan.yanan.act.progress.completion;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.CommonListViewAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.DateTools;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDateNoDayPickerDialog;
import com.tongyan.yanan.fragment.progress.OthersFragment;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @className CompletionListAct
 * @author wanghb
 * @date 2014-7-16 PM 03:14:41
 * @Desc 月完成量列表 + 添加
 */
public class MonthCompletionListAct extends FinalActivity {
	
	@ViewInject(id=R.id.common_button_container, click="addClickListener")  RelativeLayout mAddClickBtn; //图片添加点
	@ViewInject(id=R.id.title_common_content) TextView  mTitleContent;
	@ViewInject(id=R.id.title_common_add_view) ImageView  mCommonAddView;
	@ViewInject (id=R.id.listView_data)	ListView mListView;
	
	private String mLotId; //合同段Id
 	private String  mLotName;//合同段名称
    private String mPeriodName;//期段名称
    private String mProjectInfo; //计划类型;
    
    
    private String mDataType = null;
    
    private Context mContext = this;
    private SharedPreferences mPreferences;
    private String mUserId;
    
    private CommonListViewAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mCompletionList = new ArrayList<HashMap<String, String>>();
    
    private Dialog mDialog = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mAddClickBtn.setVisibility(View.VISIBLE);
		mCommonAddView.setVisibility(View.VISIBLE);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mUserId = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		if(getIntent().getExtras()!=null){
			Bundle mBundle=getIntent().getExtras();
			mPeriodName=mBundle.getString("periodName");
			mProjectInfo=mBundle.getString("projectInfo");
			mLotId=mBundle.getString("lotId");
			mLotName=mBundle.getString("lotName");
			mTitleContent.setText(mPeriodName+" "+mLotName);
			
			mDataType = mBundle.getString("DataType");
			
		}
		
		mAdapter = new CommonListViewAdapter(mContext, mCompletionList, R.layout.common_state_listview_item, MonthCompletionListAct.class);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mOnItemClickListener);
		refreshListView();
		
	}
	OnItemClickListener  mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CommonListViewAdapter.HolderViewProgressMonthProject mHolderView = (CommonListViewAdapter.HolderViewProgressMonthProject)arg1.getTag();
			HashMap<String, String> map = mHolderView.mMapProgressMonth;
			if(map != null) {
				showDialog(map);
			}
		}
		
	};
	
	
	public void showDialog(final HashMap<String, String> map) {
	 	final Dialog  mDialog=new Dialog(mContext);
	 	mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_progressproject_upload);
		mDialog.show();
		//填写/查看按钮
		Button mButModify=(Button)mDialog.findViewById(R.id.butModify_dialog_progssproject_upload);
		//上传
		Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_dialog_progssproject_upload);
		//删除
		Button mButDel=(Button)mDialog.findViewById(R.id.butDelete_butModify_dialog_progssproject_upload);
		
		mButModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SaveCompletionDataAct.class);
				intent.putExtra("lotId", mLotId);
				intent.putExtra("DataType", mDataType);
				intent.putExtra("ID", map.get("ID"));
				intent.putExtra("projectInfo", mProjectInfo);//日完成量
				startActivityForResult(intent, 2015);
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
				update(map);
				mDialog.dismiss();
			}
		});
		
		//删除
		mButDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder  mAlertDialog=new AlertDialog.Builder(mContext);
				mAlertDialog.setMessage("确定删除吗?");
				mAlertDialog.setPositiveButton("是", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {						
						delete(map.get("ID"));
						mDialog.dismiss();
					}
				});
				mAlertDialog.setNegativeButton("否", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
					}
				});
			}
		});
}

	public void update(final HashMap<String, String> map) {
		mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		setProgressBarIndeterminateVisibility(true);
		mDialog.show();
		final String mYearMon = map.get("CommonInfo");
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer mData = new StringBuffer();
				mData.append("{"); // 开始标记
				mData.append("\"LotId\"");
				mData.append(":");
				mData.append("\"" + mLotId + "\"");
				String[] mYearMonAry = null;
				if(mYearMon != null) {
					mYearMonAry = mYearMon.split("-");
				}
				if(mYearMonAry == null || mYearMonAry.length < 2) {
					sendFMessage(Constants.ERROR);
					return;
				}
				
				mData.append(",\"Years\":");
				mData.append("\"" + mYearMonAry[0] + "\",");
				mData.append("\"Months\":");
				mData.append("\"" + mYearMonAry[1] + "\",");
				
				mData.append("\"CreateTime\"");
				mData.append(":");
				mData.append("\"" + DateTools.getDate() + "\"");
				mData.append(",");
				mData.append("\"CreateId\""); // 创建人
				mData.append(":");
				mData.append("\"" + mUserId + "\"");
				mData.append(",");
				mData.append("\"Remark\"");
				mData.append(":\"");
				mData.append(map.get("Remark"));
				mData.append("\",");
				mData.append("\"Question\"");
				mData.append(":\"");
				mData.append(map.get("Question"));
				mData.append("\",");
				mData.append("\"Reason\"");
				mData.append(":\"");
				mData.append(map.get("Reason"));
				mData.append("\",");
				ArrayList<HashMap<String, String>> mConstructList = new DBService(mContext).selectCompletionUpdateInfo(map.get("ID"),
						"1", mDataType, mUserId);// 施工项
				ArrayList<HashMap<String, String>> mMachineryList = new DBService(mContext).selectCompletionUpdateInfo(map.get("ID"),
						"2", mDataType, mUserId);
				ArrayList<HashMap<String, String>> mPersonList = new DBService(mContext).selectCompletionUpdateInfo(map.get("ID"),
						"3", mDataType, mUserId);
				mData.append("\"Items\"");
				mData.append(":");
				mData.append("["); // 开始值
				if (mConstructList != null && mConstructList.size() > 0) {
					for (int i = 0, len = mConstructList.size(); i < len; i++) {
						HashMap<String, String> m = mConstructList.get(i);
						if (m != null) {
							mData.append("{");
							mData.append("\"ItemsId\"");
							mData.append(":");
							mData.append("\"" + m.get("ItemsId") + "\"");
							mData.append(",");
							mData.append("\"WeekDone\"");
							mData.append(":");
							mData.append("\"" + m.get("ValueContent") + "\"");
							mData.append("}");
							if (i != len - 1) {
								mData.append(",");
							}
						}
					}
				}
				mData.append("],"); // 开始值
				mData.append("\"Machinery\""); // 添加机械开始标记
				mData.append(":");
				mData.append("[");
				if (mMachineryList != null && mMachineryList.size() > 0) {
					for (int i = 0, len = mMachineryList.size(); i < len; i++) {
						HashMap<String, String> map = mMachineryList.get(i);
						if (map != null) {
							mData.append("{");
							mData.append("\"MachineryId\"");
							mData.append(":");
							mData.append("\"" + map.get("ItemsId") + "\"");
							mData.append(",");
							mData.append("\"MachineryDone\"");
							mData.append(":");
							mData.append("\"" + map.get("ValueContent") + "\"");
							mData.append("}");
							if (i != len - 1) {
								mData.append(",");
							}
						}
					}
				}
				mData.append("]");
				mData.append(",");
				mData.append("\"Personnel\""); // 添加人员开始标记
				mData.append(":");
				mData.append("[");
				if (mPersonList != null && mPersonList.size() > 0) {
					for (int i = 0, len = mPersonList.size(); i < len; i++) {
						HashMap<String, String> map = mPersonList.get(i);
						if (map != null) {
							mData.append("{");
							mData.append("\"PersonnelId\"");
							mData.append(":");
							mData.append("\"" + map.get("ItemsId") + "\"");
							mData.append(",");
							mData.append("\"PersonnelDone\"");
							mData.append(":");
							mData.append("\"" + map.get("ValueContent") + "\"");
							mData.append("}");
							if (i != len - 1) {
								mData.append(",");
							}
						}
					}
				}
				mData.append("]");
				mData.append("}");// 结束标记

				HashMap<String, String> mParameters = new HashMap<String, String>();
				mParameters.put("method", Constants.METHOD_OF_RERPORTMONTH);
				mParameters.put("key", Constants.PUBLIC_KEY);
				mParameters.put("userId", mUserId);
				String data = mData.toString();
				data = data.replaceAll(" ", "%20");
				mParameters.put("data", data);
				String mResponseBody = "";
				try {
					mResponseBody = HttpUtils.httpPostString(HttpUtils.getUrlWithParas(Constants.SERVICE_PROGRESS, mParameters, mContext));
					Log.i("mResponseBody", mResponseBody);
					if (JsonTools.getCommonResult(mResponseBody)) {
						sendFMessage(Constants.SUCCESS);
						new DBService(mContext).updateCompletionDate(
								map.get("ID"), "2", OthersFragment.TYPE_STATE);
					} else {
						sendFMessage(Constants.ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendFMessage(Constants.ERROR);
				}

			}
		}).start();
	}
	
	
	public void delete(String id) {
		new DBService(mContext).deleteCompletionById(id);
		refreshListView();
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2015 || resultCode == 0) {
			if(resultCode == 2016) {
				new DBService(mContext).updateClearProgress();
				refreshListView();
			}	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 重新加载ListView
	 */
	public void refreshListView() {
		ArrayList<HashMap<String, String>> list = new DBService(mContext).getCompletionDateList(mUserId, mDataType, mLotId);
		if(mCompletionList != null) {
			mCompletionList.clear();
			if(list != null) {
				mCompletionList.addAll(list);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 添加月完成量
	 * @param view
	 */
	public void addClickListener(View view) {
		new MDateNoDayPickerDialog(mContext, new MDateNoDayPickerDialog.OnDateTimeSetListener(){
			@Override
			public boolean onDateTimeSet(int year, int monthOfYear) {
				String mDateInfo = year + "-" + monthOfYear;
				if(new DBService(mContext).isExistSameCompletion(mUserId, mLotId, mDataType, mDateInfo)) {
					Toast.makeText(mContext, "该月完成量已存在", Toast.LENGTH_SHORT).show();
					return false;
				} else { 
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("UserId", mUserId);
					map.put("LotId", mLotId);
					map.put("DateInfo", mDateInfo);
					map.put("DateType", "3");
					if(new DBService(mContext).insertCompletionDate(map)) {
						refreshListView();
						return true;
					} else {
						return false;
					}
				}
			}
		}).show();
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
