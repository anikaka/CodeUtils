package com.tongyan.yanan.act.quality;

import java.io.IOException;
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
import com.tongyan.yanan.common.adapter.QualityWaveInjectListAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: QualityWaveVelocityAct.java 
 * @author Rubert
 * @date 2014-8-19 PM 03:54:44 
 * @version V1.0 
 * @Description: 波速测试
 *  typeId = 3表示波速
 * 
 */
public class QualityWaveVelocityListAct extends FinalActivity implements OnItemClickListener{
	
	@ViewInject(id=R.id.listView_data) ListView mListView;
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	@ViewInject(id=R.id.common_button_container, click="addWaveListener") RelativeLayout mRightBtn;
	@ViewInject(id=R.id.title_common_add_view) ImageView mAddView;
	
	
	private Context mContext = this;
	private ArrayList<HashMap<String, String>> mWaveVelocityList = new ArrayList<HashMap<String, String>>();
	
	private QualityWaveInjectListAdapter mAdapter;
	
	
	private HashMap<String, String> mPactMap = null;
	private SharedPreferences mPreferences;
	private String mUserid = null;
	private String mIntentType = null;
	private String mUpdateParams = null;
	
	private Dialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		mRightBtn.setVisibility(View.VISIBLE);
		mAddView.setVisibility(View.VISIBLE);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		
		mAdapter = new QualityWaveInjectListAdapter(mContext, mWaveVelocityList, R.layout.quality_staticload_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			String s = getIntent().getExtras().getString("IntentType");
			if("wave_testing".equals(s)) {
				mIntentType = String.valueOf(1);
				mTitleContent.setText("波速测试列表");
				mUpdateParams = String.valueOf(3);
			} else if("inject_testing".equals(s)) {
				mIntentType = String.valueOf(2);
				mTitleContent.setText("标准贯入测试列表");
				mUpdateParams = String.valueOf(1);
			}
			refreshListView();
		}
	}
	
	/**
	 * 刷新列表
	 */
	public void refreshListView() {
		if(mPactMap != null) {
			ArrayList<HashMap<String, String>> list = new DBService(mContext).getWaveInjectInfo(mUserid, mPactMap.get("NewId"), mIntentType);
			if(mWaveVelocityList != null) {
				mWaveVelocityList.clear();
				if(list != null) {
					mWaveVelocityList.addAll(list);
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * @param v
	 */
	public void addWaveListener(View v) {
		Intent intent = new Intent(mContext, QualityWaveVelocityAddAct.class);
		intent.putExtra("PactMap", mPactMap);
		intent.putExtra("IntentType", mIntentType);
		intent.putExtra("IntentMethod", "add");
		startActivityForResult(intent, 3333);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		QualityWaveInjectListAdapter.ViewHolderQualityStaticLoad mViewHolder = (QualityWaveInjectListAdapter.ViewHolderQualityStaticLoad)arg1.getTag();
		HashMap<String, String> map = mViewHolder.mMapQualityStaticLoad;
		showDialog(map);
	}
	
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
					Intent intent = new Intent(mContext, QualityWaveVelocityDetailAct.class);
					intent.putExtra("PactMap", mPactMap);
					intent.putExtra("ItemMap", map);
					startActivity(intent);
				} else {//修改
					Intent intent = new Intent(mContext, QualityWaveVelocityAddAct.class);
					intent.putExtra("PactMap", mPactMap);
					intent.putExtra("IntentMethod", "modify");
					intent.putExtra("IntentType", mIntentType);
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
				mDialog.dismiss();
				upload(map);//上传
				
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
						new DBService(mContext).delWaveInjectById(map.get("ID"));
						new DBService(mContext).delWaveInjectPointByWaveInjectionId(map.get("ID"));
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
	
	/**上传*/
	public void upload(final HashMap<String, String> map){
		mDialog=new Dialog(mContext,R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_ADDASSAYDATA);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mUserid);
				StringBuffer mData=new StringBuffer();
				mData.append("{");
				//合同段Id
				mData.append("\"LotId\"");
				mData.append(":");
				mData.append("\""+mPactMap.get("NewId")+"\"");
				mData.append(",");
				//监测人
				mData.append("\"DetectPeople\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(mPreferences.getString(Constants.PREFERENCES_INFO_USERNAME, ""))+"\"");
				mData.append(",");
				//监测单位
				mData.append("\"DetectUnit\"");
				mData.append(":");
				mData.append("\""+ map.get("DetectUnitId") +"\"");
				mData.append(",");
				//监测时间
				mData.append("\"DetectDate\"");
				mData.append(":");
				mData.append("\""+map.get("DetectDate")+"\"");
				mData.append(",");
				//监测编号
				mData.append("\"DetectNo\"");
				mData.append(":");
				mData.append("\""+map.get("DetectNumber")+"\"");
				mData.append(",");
				//试验内容
				mData.append("\"TestContent\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(map.get("TestContent"))+"\"");
				mData.append(",");
				//试验结论
				mData.append("\"TestConClusion\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(map.get("TestConClusion"))+"\"");
				mData.append(",");
				//类型 1表示标贯，2表示静载，3表示波速
				mData.append("\"TypeId\"");
				mData.append(":");
				mData.append("\""+ mUpdateParams +"\"");
				mData.append(",");
				//工程Id
				mData.append("\"ProjectId\"");
				mData.append(":");
				mData.append("\""+mPactMap.get("periodId")+"\"");
				mData.append(",");
				//点
				mData.append("\"AssayPoint\"");
				mData.append(":");
				mData.append("[");
				HashMap<HashMap<String, String>, ArrayList<HashMap<String, String>>> mCacheMap = new DBService(mContext).getWaveInjectPointsMap(map.get("ID"));
				int i = 1;
				int size = mCacheMap.size();
				
				if(mCacheMap != null && size > 0) {
					for(Map.Entry<HashMap<String, String>, ArrayList<HashMap<String, String>>> m : mCacheMap.entrySet()) {
						HashMap<String, String> mKeyMap = m.getKey();
						ArrayList<HashMap<String, String>> mValueList = m.getValue();
						
						mData.append("{");
						//点号
						mData.append("\"AssayNum\""); 
						mData.append(":");
						mData.append("\""+mKeyMap.get("PointNums")+"\"");
						mData.append(",");
						//x
						mData.append("\"PointX\"");
						mData.append(":");
						mData.append("\""+mKeyMap.get("PointX")+"\"");
						mData.append(",");
						//y
						mData.append("\"PointY\"");
						mData.append(":");
						mData.append("\""+mKeyMap.get("PointX")+"\"");
						mData.append(",");
						//Z
						mData.append("\"PointZ\"");
						mData.append(":");
						mData.append("\""+mKeyMap.get("PointX")+"\"");
						mData.append(",");
						mData.append("\"AssayData\"");
						mData.append(":");
						mData.append("[");
						if(mValueList != null && mValueList.size() > 0) {
							for(int w = 0,len = mValueList.size(); w < len; w ++) {
								HashMap<String, String> mValue = mValueList.get(w);
								mData.append("{");
								//
								mData.append("\"StartValue\""); 
								mData.append(":");
								mData.append("\"" + mValue.get("DeepStartValue") + "\"");
								mData.append(",");
								//
								mData.append("\"EndValue\""); 
								mData.append(":");
								mData.append("\"" + mValue.get("DeepEndValue") + "\"");
								mData.append(",");
								//
								mData.append("\"Results\""); 
								mData.append(":");
								mData.append("\"" + mValue.get("WaveVelocityValue") + "\"");
								
								if(w == len - 1) {
									mData.append("}");
								} else {
									mData.append("},");
								}
							}
						}
						mData.append("]");
						mData.append("}");
						if(i != size){
							mData.append(",");
						} 
						i ++;
					}
				}
				mData.append("]");
				mData.append("}");
				String data=mData.toString();
				data=data.replaceAll(" ", "%20");
				params.put("data", data);
				String mStream="";
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_QUALITY, params, mContext));
					if(!"".equals(mStream)){
						if(JsonTools.getCommonResult(mStream)){
							//上传成功
							new DBService(mContext).updateWaveInjectInfoState(map.get("ID"));
							sendFMessage(Constants.SUCCESS);
						}else{
							//上传失败
							sendFMessage(Constants.ERROR);
						}
					}else{
						sendFMessage(Constants.ERROR);
					}
				} catch (IOException e) {
					sendFMessage(Constants.CONNECTION_TIMEOUT);
					e.printStackTrace();
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
