package com.tygeo.highwaytunnel.activity.hidecheck;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.HideCheckContentAdapter;
import com.tygeo.highwaytunnel.adpter.HideCheckDeviceRecordAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 隐患排查设备
 * @author ChenLang
 *
 */

public class HideCheckDeviceActivity extends Activity implements View.OnClickListener{


	private Context mContext=this;
	private ListView mListViewCheckContent; //检查内容
	private ListView mListViewCheckDeviceRecord; //检查记录单
	private RadioButton   mRadioBtnYes; 
	private RadioButton   mRadioBtnNo;
	private EditText          mTxtRemark; //备注
	private EditText   	   mTxtDeviceName; //设备名称
	private EditText  		   mTxtDeviceNumber;// 设备数量
	private EditText 		   mTxtApproachRecord;//进场记录
	private EditText  		   mTxtLocation;			//工程部位
	private EditText    	   mTxtAcceptanceRecord;//监理验收记录
	private EditText          mTxtOperator; 				//操作人员
	private EditText          mTxtWrittenRecord;       //是否有书面路径
	private EditText          mTxtCertificate;				//证书
	private EditText		   mTxtArchives;  				//档案
	private ImageButton  mImgBtnSubmit; //提交
	private HideCheckContentAdapter mCheckContentAdpater; //检查内容适配器
	private static HideCheckDeviceRecordAdapter mDeviceRecordAdapter;
	private ArrayList<HashMap<String, String>> mArrayListCheckContent=new ArrayList<HashMap<String,String>>();
	private static ArrayList<HashMap<String, String >> mArrayListCheckDeviceRecord=new ArrayList<HashMap<String, String>>();
	private static String  mDeviceId; 			//设备id
	private String  mItemCode;       //检查内容
	private String  mContentCode; //检车内容编号
	private String  mContent; //检查内容
	
	/**
	 * 初始化组件*/
	private  void initWeiget(){
		mListViewCheckContent=(ListView)findViewById(R.id.listViewCheckDeviceContent);
		mListViewCheckDeviceRecord=(ListView)findViewById(R.id.listViewCheckDeviceRecord);
		mTxtDeviceName=(EditText)findViewById(R.id.txtDeviceName);
		mTxtDeviceNumber=(EditText)findViewById(R.id.txtDeviceNumber);
		mTxtApproachRecord=(EditText)findViewById(R.id.txtApproachRecord);
		mTxtLocation=(EditText)findViewById(R.id.txtLoaction);
		mTxtAcceptanceRecord=(EditText)findViewById(R.id.txtAcceptanceRecord);
		mTxtOperator=(EditText)findViewById(R.id.txtOperator);
		mTxtWrittenRecord=(EditText)findViewById(R.id.txtWrittenRecord);
		mTxtCertificate=(EditText)findViewById(R.id.txtCertificate);
		mTxtArchives=(EditText)findViewById(R.id.txtArchives);
		mRadioBtnYes=(RadioButton)findViewById(R.id.radioDeviceBtnYes);
		mRadioBtnNo=(RadioButton)findViewById(R.id.radioDeviceBtnNo);
		mTxtRemark=(EditText)findViewById(R.id.txtDeviceContentDescribe);
		mImgBtnSubmit=(ImageButton)findViewById(R.id.imgBtnSubmitDeviceRecord);
		mImgBtnSubmit.setOnClickListener(this);
		mListViewCheckContent.setOnItemClickListener(checkContentListener);
		mListViewCheckDeviceRecord.setOnItemClickListener(checkDeviceRecordListener);
		mRadioBtnNo.setChecked(true);
		mTxtRemark.setFocusable(false);
		mTxtRemark.setBackgroundColor(getResources().getColor(R.drawable.maybehs));
		mRadioBtnYes.setOnClickListener(this);
		mRadioBtnNo.setOnClickListener(this);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hidecheck_device);
		if(getIntent().getExtras()!=null){
			mDeviceId=getIntent().getExtras().getString("deviceId");
		}
		initWeiget();
		initAdapter();
	}
	
	
	/**
	 * 初始化适配器 */
	private void initAdapter(){
		if(mArrayListCheckContent.size()>0){
			mArrayListCheckContent.clear();
		}
		mArrayListCheckContent.addAll(DB_Provider.queryCheckContentDeviceInfo());
		mCheckContentAdpater=new HideCheckContentAdapter(mContext, mArrayListCheckContent, R.layout.hidecheck_content_listview_item);
		mListViewCheckContent.setAdapter(mCheckContentAdpater);
		if(mArrayListCheckDeviceRecord.size()>0){
			mArrayListCheckDeviceRecord.clear();
		}
		mDeviceRecordAdapter=new HideCheckDeviceRecordAdapter(mContext, mArrayListCheckDeviceRecord, R.layout.hidecheck_devicerecord_listview_item);
		mListViewCheckDeviceRecord.setAdapter(mDeviceRecordAdapter);
		refresh();
	}
	
	/**
	 * 刷新
	 */
	public static void refresh(){
		ArrayList<HashMap<String, String>> list = DB_Provider.queryTableDeviceRecord(mDeviceId);
		if(mArrayListCheckDeviceRecord != null){
			if(list != null){
				mArrayListCheckDeviceRecord.clear();
				mArrayListCheckDeviceRecord.addAll(list);
			}
		}
		mDeviceRecordAdapter.notifyDataSetChanged();
	}
	
	/**
	 *更新设备记录单*/
	public void updateDeviceRecord(){
		if(mArrayListCheckDeviceRecord.size()>0){
			mArrayListCheckDeviceRecord.clear();
		}
		mArrayListCheckDeviceRecord.addAll(DB_Provider.queryTableDeviceRecord(mDeviceId));
		mDeviceRecordAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 修改点击状态*/
	public void setClickState(int position){
		if(mArrayListCheckContent!=null && mArrayListCheckContent.size()>0){
			for(int i=0;i<mArrayListCheckContent.size();i++){
				HashMap<String, String> map=mArrayListCheckContent.get(i);
				if(i==position){
					map.put("isCheck", "true");
				}else{
					map.put("isCheck", "false");
				}
			}
			mCheckContentAdpater.notifyDataSetChanged();
		}
	}
	
	/**
	 * 检查内容*/
	OnItemClickListener  checkContentListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			setClickState(position);
			HideCheckContentAdapter.ViewHolder viewHolder=(HideCheckContentAdapter.ViewHolder)view.getTag();
			HashMap<String, String> map=viewHolder.map;
			mItemCode=map.get("itemCode");
			mContentCode=map.get("code");
			mContent=map.get("content");
		}
	};
	
	/**
	 *设备记录单*/
	OnItemClickListener checkDeviceRecordListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
		}
	};



	/**
	 * 判断信息是否填写完整  @return true 已经填写完整,otherwise false*/ 
	private boolean  isComplete(){
		if("".equals(mTxtDeviceName.getText().toString()) || "null".equals(mTxtDeviceName.getText().toString())){
			return false;
		}
		if("".equals(mTxtDeviceNumber.getText().toString()) || "null".equals(mTxtDeviceNumber.getText().toString())){
			return false;
		}
		if("".equals(mTxtOperator.getText().toString()) || "null".equals(mTxtOperator.getText().toString())){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判断检查内容是否选择,
	 * @return 已选,otherwise false*/
	private  boolean  contentIsCheck(){
		if(mArrayListCheckContent!=null && mArrayListCheckContent.size()>0){
			for(HashMap<String, String> map:mArrayListCheckContent){
				if("true".equals(map.get("isCheck"))){
					return true;
				}
			}
		}
		return false;
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtnSubmitDeviceRecord:
						if(contentIsCheck()){
							if(isComplete()){
								HashMap<String, String> map=new HashMap<String, String>();
								map.put("deviceId", mDeviceId);
								map.put("itemCode", mItemCode);
								map.put("name", mTxtDeviceName.getText().toString());  //设备名称
								map.put("number", mTxtDeviceNumber.getText().toString()); //设备数量
								map.put("checkContent", mContent);//检查
								map.put("approachRecord", mTxtApproachRecord.getText().toString());//进场记录
								map.put("loaction", mTxtLocation.getText().toString());
								map.put("acceptanceRecord", mTxtAcceptanceRecord.getText().toString());
								map.put("operator",mTxtOperator.getText().toString());
								map.put("writtenRecord", mTxtWrittenRecord.getText().toString()); //书面
								map.put("certificate", mTxtCertificate.getText().toString()); 
								map.put("archives", mTxtArchives.getText().toString());
								map.put("remark", mTxtRemark.getText().toString());
								DB_Provider.insertTableDeviceRecord(map);
								updateDeviceRecord();
							}else{
								Toast.makeText(mContext, "信息未填写完整", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(mContext, "没有选择检查内容", Toast.LENGTH_SHORT).show();
						}
			break;
		case R.id.radioDeviceBtnYes:
					mTxtRemark.setFocusable(true);
					mTxtRemark.setFocusableInTouchMode(true);
					mTxtRemark.setBackgroundColor(getResources().getColor(R.color.white_color));
			break;
		case R.id.radioDeviceBtnNo:
					mTxtRemark.setFocusable(false);
					mTxtRemark.setBackgroundColor(getResources().getColor(R.drawable.maybehs));
				break;
		default:
			break;
		}
	}
	
}
