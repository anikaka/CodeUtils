package com.tongyan.yanan.act.quality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.QualityStaticloadAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 平板静载荷试验
 * 
 * @author ChenLang
 */
public class QualityStaticLoad  extends FinalActivity implements OnItemClickListener,OnClickListener{

	@ViewInject(id=R.id.rlAddStaticLoad)
	RelativeLayout mRlAddStaticLoad;
	
	@ViewInject(id=R.id.listViewStaticUpload)
	ListView mListView;
	
	private Context mContext=this;
	private QualityStaticloadAdapter mAdapter;
	private SharedPreferences mSP;
	private String mUserId;
	private Bundle mBundle;
	private String mLotId;//合同段Id
	private String mProjectId;//期段Id
	private String mPreson;//上传人
	private String mLotName;//合同段名称
	private String mProjectName; //期段名称
	private Dialog mProgressBar;
	private String mId;
	private HashMap<String, String> mMapPact=new HashMap<String, String>(); //标段
	private ArrayList<HashMap<String, String>> mArrayListStaticLoad=new ArrayList<HashMap<String,String>>();
	private final int ENTER_LAYOUT=0x12341;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_staticload);
		//用户ID获取
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserId = mSP.getString(Constants.PREFERENCES_INFO_USERID, "");
		if(getIntent().getExtras() != null){
		    mMapPact=(HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			mLotId=mMapPact.get("NewId");
			mProjectId=mMapPact.get("ProjectId");
			mProjectName=mMapPact.get("periodName");
			mLotName=mMapPact.get("LotName");
			mPreson= new DBService(mContext).queryTableUser(mUserId);
		}
		mBundle=new Bundle();
		clear();
		//构造适配器I
		mAdapter=new QualityStaticloadAdapter(mContext, mArrayListStaticLoad, R.layout.quality_staticload_listview_item);
		mListView.setAdapter(mAdapter);
		refreshData();
		mListView.setOnItemClickListener(this);
		mRlAddStaticLoad.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		QualityStaticloadAdapter.ViewHolderQualityStaticLoad mViewHolder = (QualityStaticloadAdapter.ViewHolderQualityStaticLoad)view.getTag();
		final HashMap<String, String> mMap = mViewHolder.mMapQualityStaticLoad;
		if(mMap!=null){
			mId=mMap.get("id");
		}
		 //新建对话框
		final Dialog mDialog=new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.layout_subside_pactselect_datalist_dialog);
		mDialog.show();
		//查找对话框组件;
		Button  mButLook=(Button)mDialog.findViewById(R.id.butLook_layout_subside_pactselect_point_datalist);
		Button mButUpload=(Button)mDialog.findViewById(R.id.butUpload_layout_subside_pactselect_point_datalist);
		Button mButDelete=(Button)mDialog.findViewById(R.id.butDelete_layout_subside_pactselect_point_datalist);
		if(!"2".equals(mMap.get("state"))){//状态有0：未开始，1：已完成，2：已上传
			mButLook.setText("修改");
		} else {
			mButLook.setText("查看");
		}
		mButLook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("2".equals(mMap.get("state"))) {
					Intent intent=new Intent(mContext, QualityStaticLoadDetailsAct.class);
					mBundle.putString("noId", mMap.get("id"));
					mBundle.putString("projectName",mProjectName);
					mBundle.putString("lotName", mLotName);
					mBundle.putSerializable("CacheMap", mMap);
					intent.putExtras(mBundle);
					startActivity(intent);
				} else {
					Intent intent=new Intent(mContext, QualityStaticLoadAdd.class);
					mBundle.putString("noId", mMap.get("id"));
					mBundle.putString("IntentType", "modify");
					mBundle.putString("lotId", mMap.get("NewId")); //合同段Id 
					mBundle.putString("projectId", mMap.get("ProjectId")); //项目Id
					mBundle.putString("projectName",mProjectName);
					mBundle.putString("lotName", mLotName);
					mBundle.putSerializable("CacheMap", mMap);
					intent.putExtras(mBundle);
					startActivityForResult(intent, ENTER_LAYOUT);
				}
			}
		});
		//上传
		mButUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("2".equals(mMap.get("state"))) {
					Toast.makeText(mContext, "该记录已上传", Toast.LENGTH_SHORT).show();
				} else if("0".equals(mMap.get("state"))){
					Toast.makeText(mContext, "该记录还未完成", Toast.LENGTH_SHORT).show();
				} else {
					mDialog.cancel();
					mProgressBar=new Dialog(mContext,R.style.dialog);
					mProgressBar.setContentView(R.layout.common_normal_progressbar);
					mProgressBar.setCanceledOnTouchOutside(false);
					mProgressBar.show();
					upload(mMap);
				}
			}
		});
		//删除数据
		mButDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder mDialogBuilder=new AlertDialog.Builder(mContext);
				mDialogBuilder.setMessage("确定删除吗?");
				mDialogBuilder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					new DBService(mContext).delTableStatiLoadData(mMap.get("id"));
					new DBService(mContext).deleteTableExaminePoint(mMap.get("id"));
					refreshData();
					mDialog.cancel();
						
					}
				});
				mDialogBuilder.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.cancel();
					}
				});
				mDialogBuilder.show();
			}
		});

	}
	
	/**上传*/
	public void upload(final HashMap<String, String> map){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_ADDASSAYDATA);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mUserId);
				StringBuffer mData=new StringBuffer();
				mData.append("{");
				//合同段Id
				mData.append("\"LotId\"");
				mData.append(":");
				mData.append("\""+mLotId+"\"");
				mData.append(",");
				//监测人
				mData.append("\"DetectPeople\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(mPreson)+"\"");
				mData.append(",");
				//监测单位
				mData.append("\"DetectUnit\"");
				mData.append(":");
				mData.append("\"" + map.get("unit") + "\"");//new DBService(mContext).queryContacts(mUserId,map.get("unit"))
				mData.append(",");
				//监测时间
				mData.append("\"DetectDate\"");
				mData.append(":");
				mData.append("\""+map.get("date")+"\"");
				mData.append(",");
				//监测编号
				mData.append("\"DetectNo\"");
				mData.append(":");
				mData.append("\""+map.get("no")+"\"");
				mData.append(",");
				//试验内容
				mData.append("\"TestContent\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(map.get("content"))+"\"");
				mData.append(",");
				//试验结论
				mData.append("\"TestConClusion\"");
				mData.append(":");
				mData.append("\""+JsonTools.getURLEncoderString(map.get("conclusion"))+"\"");
				mData.append(",");
				//类型 1表示标贯，2表示静载，3表示波速
				mData.append("\"TypeId\"");
				mData.append(":");
				mData.append("\"2\"");
				mData.append(",");
				//工程Id
				mData.append("\"ProjectId\"");
				mData.append(":");
				mData.append("\""+mProjectId+"\"");
				mData.append(",");
				//点
				mData.append("\"AssayPoint\"");
				mData.append(":");
				mData.append("[");
				ArrayList<HashMap<String, String>> mArrayListPoint=new DBService(mContext).queryTableExaminePointAllData(map.get("id"));
				for(int i=0;i<mArrayListPoint.size();i++){
					HashMap<String, String> map=mArrayListPoint.get(i);
					mData.append("{");
					//点号
					mData.append("\"AssayNum\""); 
					mData.append(":");
					mData.append("\""+map.get("pointName")+"\"");
					mData.append(",");
					//x
					mData.append("\"PointX\"");
					mData.append(":");
					mData.append("\""+map.get("pointX")+"\"");
					mData.append(",");
					//y
					mData.append("\"PointY\"");
					mData.append(":");
					mData.append("\""+map.get("pointY")+"\"");
					mData.append(",");
					//Z
					mData.append("\"PointZ\"");
					mData.append(":");
					mData.append("\""+map.get("pointZ")+"\"");
					mData.append(",");
					mData.append("\"AssayData\"");
					mData.append(":");
					mData.append("[");
					mData.append("]");
					mData.append("}");
					if((i+1)!=mArrayListPoint.size()){
						mData.append(",");
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
							sendFMessage(Constants.SUCCESS);
						}else{
							//上传失败
							sendFMessage(Constants.COMMON_MESSAGE_2);
						}
					}else{
						sendFMessage(Constants.COMMON_MESSAGE_2);
					}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlAddStaticLoad:
				String mNoId=new DBService(mContext).insertTableStaticLoad(mUserId, mProjectId, mLotId, mPreson, "", "", "", "", "");
				Intent intent=new Intent(mContext, QualityStaticLoadAdd.class);
				mBundle.putString("noId", mNoId);
				mBundle.putString("IntentType", "add");
				mBundle.putString("lotId", mLotId); //合同段Id 
				mBundle.putString("projectId", mProjectId); //项目Id
				mBundle.putString("projectName",mProjectName);
				mBundle.putString("lotName", mLotName);
				//mBundle.putString("state", new DBService(mContext).getTableStaticLoadState(mId));
				intent.putExtras(mBundle);
				startActivityForResult(intent, ENTER_LAYOUT);
			break;
		default:
			break;
		}
	}

	/** 刷新数据*/
	public void refreshData() {
		if (mArrayListStaticLoad != null) {
			mArrayListStaticLoad.clear();
			ArrayList<HashMap<String, String>> mArrayListStaticLoadSQL = new DBService(mContext).queryTableStaticLoad(mProjectId, mLotId, mUserId);
			if(mArrayListStaticLoadSQL != null) {
				mArrayListStaticLoad.addAll(mArrayListStaticLoadSQL);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	/** 清空编号为空的数据 */
	public void clear() {
		new DBService(mContext).delTableStaticLoad();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ENTER_LAYOUT) {
			clear();
			refreshData();
		}
	}

	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch (flag) {
		case Constants.SUCCESS:
			mProgressBar.cancel();
			Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
			new DBService(mContext).updateTableStaticLoadState(mId);
			refreshData();
			break;
		case Constants.ERROR:
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.COMMON_MESSAGE_2:
			mProgressBar.cancel();
			Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
