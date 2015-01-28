package com.tongyan.yanan.act.pic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.PicPactAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * @category 照片上传合同段
 * @author Administrator
 * @date 2014/08/05
 * @version YanAn 1.0
 */
public class PicPactAct extends FinalActivity implements OnItemClickListener{
	
	@ViewInject(id=R.id.listView_picPact) 
	ListView mListView;
	private Context mContext=this;
	private PicPactAdapter mAdapter;
	private Bundle mBundle;
	private String mType;
	private String mNewId;
	private SharedPreferences mSP;
    private ArrayList<HashMap<String, String>> mArrayListLotNumber=new ArrayList<HashMap<String,String>>();
    private ArrayList<HashMap<String, String>> mArrayListlotNumberJson=new ArrayList<HashMap<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_pact);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mType=mBundle.getString("type");
			mNewId=mBundle.getString("newId");
//			Log.i("test", mBundle.getString("newId"));
		}
		if("0".equals(mType)){			
			getLotNumber("ydm");
		}else if("1".equals(mType)){
			getLotNumber("qh");
		}else if("2".equals(mType)){
			getLotNumber("mg");
		}else if("3".equals(mType)){
			getLotNumber("ddpz");
		}
		ArrayList<HashMap<String, String>> mAllList = new DBService(this).getTermPartPact();
		mAdapter =new PicPactAdapter(this, mAllList, R.layout.main_subside_paceselect_listview_item,mArrayListLotNumber);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PicPactAdapter.ViewHolderPicPact mHolderView = (PicPactAdapter.ViewHolderPicPact)view.getTag();
		HashMap<String, String> mMap=mHolderView.mMapPicPact;
		if(mMap.size()>0){
			if("0".equals(mType)){			
				//原地貌
				Intent intent=new Intent(mContext,PicOriginalPointAct.class);
				mBundle.putString("lotId", mMap.get("NewId"));
				intent.putExtras(mBundle);
				startActivity(intent);
			}else if("1".equals(mType)){
				//强夯
				Intent intent=new Intent(mContext,PicCompactionPointAct.class);
				mBundle.putString("lotId", mMap.get("NewId"));
				intent.putExtras(mBundle);
				startActivity(intent);
		}else if("2".equals(mType)){
			    //盲沟
				Intent  intent=new Intent(mContext, PicGutterPointAct.class);
				mBundle.putString("lotId", mMap.get("NewId"));
				intent.putExtras(mBundle);
				startActivity(intent);
		}else if("3".equals(mType)){
			   //定点拍照
			Intent intent=new Intent(mContext,PicPointPhotoAct.class);
			mBundle.putString("lotId", mMap.get("NewId"));
			intent.putExtras(mBundle);
			startActivity(intent);
		}
	}
		}
	/** 获取每个合同段信息的数目*/
	public void getLotNumber(final String typeName){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("method", Constants.METHOD_OF_LOTNUMBER);
				params.put("key", Constants.PUBLIC_KEY);
				params.put("userId", mSP.getString(Constants.PREFERENCES_INFO_USERID, ""));
				params.put("typeName", typeName);
				params.put("typeId", mNewId);
				String mStream="";
				try {
					mStream=HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_PACTSELECT, params, mContext));
					if(!"".equals(mStream)){
						mArrayListlotNumberJson=JsonTools.getLotNumber(mStream);
						sendFMessage(Constants.SUCCESS);
					}else{

					}
				} catch (IOException e) {
					sendFMessage(Constants.ERROR);
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void refresh(){
		if(mArrayListlotNumberJson.size()>0){
			mArrayListLotNumber.clear();
			mArrayListLotNumber.addAll(mArrayListlotNumberJson);
			mArrayListlotNumberJson.clear();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constants.ERROR:
			Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.SUCCESS:
				refresh();
			break;
		default:
			break;
		}
	};
}	
