package com.tongyan.yanan.act.quality;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDatePickerDialog;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;
/**
 * 
 * @Title: QualityHardingAddAct.java 
 * @author Rubert
 * @date 2014-8-15 AM 09:21:39 
 * @version V1.0 
 * @Description: 地基土压实度添加
 */
public class QualityHardingAddAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content) TextView mTitleContent;
	
	@ViewInject(id=R.id.quality_project) TextView mQualityProject;//工程
	@ViewInject(id=R.id.quality_term_pact) TextView mQualityTermPact;//合同段
	@ViewInject(id=R.id.quality_area_time) TextView mQualityAreaTime;//区域时间
	@ViewInject(id=R.id.quality_test_area) TextView mQualityTestArea;//检测区域
	@ViewInject(id=R.id.quality_tester) TextView mQualityTester;//检测人
	
	@ViewInject(id=R.id.quality_company) Spinner mQualityTestCompany; //检测单位：
	@ViewInject(id=R.id.quality_time, click="onTestTimeListener") TextView mQualityTestTime;//检测时间：
	@ViewInject(id=R.id.quality_acreage) EditText mQualityAcreage;//面积
	@ViewInject(id=R.id.quality_machine) EditText mQualityMachine;//辗压机械
	@ViewInject(id=R.id.quality_variate) EditText mQualityVariate;//冲击变数
	@ViewInject(id=R.id.quality_thickness) EditText mQualityThickness;//虚铺厚度
	@ViewInject(id=R.id.quality_press_thickness) EditText mQualityPressThickness;//压实厚度
	
	
	@ViewInject(id=R.id.testing_layer_numbers_container, click="onAddLayerNumsListener") RelativeLayout mQualityNumberLayout;//本次检测层数量
	@ViewInject(id=R.id.quality_detection_layer_numbers) TextView mQualityDeteLayerNum;//本次检测层数量
	
	@ViewInject(id=R.id.quality_experiment_content) EditText mQualityExpContent;//实验内容
	@ViewInject(id=R.id.quality_experiment_result) EditText mQualityExpResult;//实验结论
	
	
	@ViewInject(id=R.id.common_save_btn, click="onSaveListener") Button mQualitySaveBtn;//确定
	@ViewInject(id=R.id.common_clear_btn, click="onCancelListener") Button mQualityCancelBtn;//取消
	
	
	private Context mContext = this;
	
	private SharedPreferences mPreferences;
	
	private String mUserid = null;
	
	private Dialog mDialog = null;
	
	private HashMap<String, String> mPactMap = null;
	private HashMap<String, String> mQualityMap = null;
	private HashMap<String, String> mItemMap = null;
	
	private String mDptId = null;
	private String mDptName = null;
	
	private String mQualityHardingId = null;
	private String mIntentType = null;
	
	private String mQualityTestTimeText = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_harding_add);
		mTitleContent.setText("添增压实度");
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserid = mPreferences.getString(Constants.PREFERENCES_INFO_USERID, "");
		
		final ArrayList<HashMap<String, String>>  mConpanyList = new DBService(mContext).getTestCompanyList();
		if(mConpanyList != null && mConpanyList.size() > 0) {
			//q
		} else {
			getDptNature(mUserid);
		}
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mPactMap = (HashMap<String, String>)getIntent().getExtras().get("PactMap");
			mQualityMap = (HashMap<String, String>)getIntent().getExtras().get("QualityMap");
			mItemMap = (HashMap<String, String>)getIntent().getExtras().get("ItemMap");
			mIntentType = getIntent().getExtras().getString("IntentType");
			
			mQualityProject.setText("工程        ：" + JsonTools.getHandlerString(mPactMap.get("periodName")));
			mQualityTermPact.setText("合同段    ：" + JsonTools.getHandlerString(mPactMap.get("LotName")));
			mQualityAreaTime.setText("区域时间：" + JsonTools.getHandlerString(mQualityMap.get("ChangeName")));
			mQualityTestArea.setText("区域：" + JsonTools.getHandlerString(mQualityMap.get("AreaName")));
		    mQualityTester.setText("检测人    ：" + mPreferences.getString(Constants.PREFERENCES_INFO_USERNAME, ""));
			
		    if("modify".equals(mIntentType)) {
		    	if(mItemMap != null) {
		    		mQualityHardingId = mItemMap.get("ID");
		    		String mDetectionCompanyId = mItemMap.get("DetectionCompanyId");
		    		if(mDetectionCompanyId != null) {
		    			if(mConpanyList != null && mConpanyList.size() > 0) {//初始化检测单位
		    				ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
		    				HashMap<String, String> firstMap = new HashMap<String, String>();
		    				firstMap.put("DeptName", mItemMap.get("DetectionCompany"));
		    				firstMap.put("DptId", mDetectionCompanyId);
		    				mList.add(firstMap);
		    				for(HashMap<String, String> sm : mConpanyList) {
		    					if(sm != null && !mDetectionCompanyId.equals(sm.get("DptId"))) {
		    						mList.add(sm);
		    					}
		    				}
		    				mConpanyList.clear();
		    				mConpanyList.addAll(mList);
		    			}
		    		}
		    		mQualityTestTime.setText(JsonTools.getHandlerString(mItemMap.get("DetectionTime")));//
		    		mQualityAcreage.setText(JsonTools.getHandlerString(mItemMap.get("Area")));
		    		mQualityMachine.setText(JsonTools.getHandlerString(mItemMap.get("Machine")));
		    		mQualityVariate.setText(JsonTools.getHandlerString(mItemMap.get("Variate")));
		    		mQualityThickness.setText(JsonTools.getHandlerString(mItemMap.get("Thickness")));
		    		mQualityPressThickness.setText(JsonTools.getHandlerString(mItemMap.get("PressThickness")));
		    		mQualityExpContent.setText(JsonTools.getHandlerString(mItemMap.get("ExpContent")));
		    		mQualityExpResult.setText(JsonTools.getHandlerString(mItemMap.get("ExpResult")));
		    		mQualityDeteLayerNum.setText("本次检测层数量：" + new DBService(mContext).getHardingPointsNum(mQualityHardingId.toString()));
		    	}
		    } else {
		    	//新增-先会在QualityHarding表添加一条数据
		    	initQualityHarding();
		    }
		    ArrayAdapter<HashMap<String, String>> mDistanceAdapter = new ArrayAdapter<HashMap<String, String>>(mContext, R.layout.common_spinner, mConpanyList){
				 @Override
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					 if (convertView == null) {
							convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner_down_item, null);
						} 
						TextView textView = (TextView)convertView.findViewById(R.id.spinner_content_item_text);
						textView.setText(mConpanyList.get(position).get("DeptName"));
						return convertView;
				}
				 
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
					textView.setText(mConpanyList.get(position).get("DeptName"));
					return convertView;
				}
			 };
			 mQualityTestCompany.setAdapter(mDistanceAdapter);
			 mQualityTestCompany.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					mDptId = mConpanyList.get(position).get("DptId");
					mDptName = mConpanyList.get(position).get("DeptName");
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
				
			});
		}
	}
	
	
	
	public void initQualityHarding() {
		//insert 
		HashMap<String, String> insertMap = new HashMap<String, String>();
		insertMap.put("UserId", mUserid);
		insertMap.put("LocId", mPactMap.get("NewId"));
		insertMap.put("DetectionAreaId", mQualityMap.get("NewId"));
		insertMap.put("ChangeName", mQualityMap.get("ChangeName"));
		insertMap.put("AreaName", mQualityMap.get("AreaName"));
		
		mQualityHardingId = new DBService(mContext).insertHarding(insertMap).toString();
	}
	
	
	
	/**
	 * 
	 * @param v
	 */
	public void onAddLayerNumsListener(View v) {
		if(mQualityHardingId != null && !"-1".equals(mQualityHardingId)) {
			Intent intent = new Intent(mContext, QualityHardingAddLayerAct.class);
			intent.putExtra("QualityHardingId", mQualityHardingId.toString());
			startActivityForResult(intent, 1234);
		} else {
			Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(1234 == requestCode) {
			mQualityDeteLayerNum.setText("本次检测层数量：" + new DBService(mContext).getHardingPointsNum(mQualityHardingId.toString()));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * 
	 * @param v
	 */
	public void onTestTimeListener(View v) {
		new MDatePickerDialog(mContext, new MDatePickerDialog.OnDateTimeSetListener(){
			@Override
			public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
				String month = monthOfYear < 10 ? "0" + monthOfYear : String.valueOf(monthOfYear);
				String day = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
				String mDateInfo = year + "-" + month + "-" + day;
				mQualityTestTime.setText(mDateInfo);
				return true;
			}
		}).show();
	}
	/**
	 * 保存
	 * @param v
	 */
	public void onSaveListener(View v) {
		if(verification()) {
			if(mItemMap == null){
				mItemMap = new HashMap<String, String>();
			}
			mItemMap.put("ID", mQualityHardingId);
			mItemMap.put("UserId", mUserid);
			mItemMap.put("LocId", mPactMap.get("NewId"));
			mItemMap.put("DetectionAreaId", mQualityMap.get("NewId"));
			mItemMap.put("ChangeName", mQualityMap.get("ChangeName"));
			mItemMap.put("AreaName", mQualityMap.get("AreaName"));
			mItemMap.put("DetectionCompany", mDptName);
			mItemMap.put("DetectionCompanyId", mDptId);
			mItemMap.put("DetectionTime", mQualityTestTime.getText().toString());
			mItemMap.put("Area", mQualityAcreage.getText().toString());
			mItemMap.put("Machine", mQualityMachine.getText().toString());
			mItemMap.put("Variate", mQualityVariate.getText().toString());
			mItemMap.put("Thickness", mQualityThickness.getText().toString());
			mItemMap.put("PressThickness", mQualityPressThickness.getText().toString());
			mItemMap.put("ExpContent", mQualityExpContent.getText().toString());
			mItemMap.put("ExpResult", mQualityExpResult.getText().toString());
			mItemMap.put("State","1");
			
			if(new DBService(mContext).updateQualityHarding(mItemMap)) {
				Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	
	
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if("add".equals(mIntentType)) {
				new DBService(mContext).delQualityHarding(mQualityHardingId);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	/**
	 * 取消
	 * @param v
	 */
	public void onCancelListener(View v) {
		//TODO 如果是新增，取消时要删除当前记录
		if("add".equals(mIntentType)) {
			new DBService(mContext).delQualityHarding(mQualityHardingId);
		}
		finish();
	}
	
	/**
	 * 验证
	 * @return
	 */
	public boolean verification() {
		mQualityTestTimeText = mQualityTestTime.getText().toString();
		if(mQualityTestTimeText == null || "".equals(mQualityTestTimeText)) {
			Toast.makeText(mContext, "请单击选择检测时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mDptId == null || "".equals(mDptId)) {
			Toast.makeText(mContext, "请选择检测单位", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(0 == new DBService(mContext).getHardingPointsNum(mQualityHardingId.toString())){
			Toast.makeText(mContext, "请添加压实度点", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	
	
	
	public void getDptNature(final String mUserid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 HashMap<String, String>  mParams=new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETDPTNATURE);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mUserid);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					if(!"".equals(mStrJson) && mStrJson!=null){		
						ArrayList<HashMap<String, String>> list = JsonTools.getDptNature(mStrJson);
						if(list != null) {
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delDepartmentNature(mUserid)) {
										new DBService(mContext).insertDepartmentNature(list);
										
										 String mCompanyId = new DBService(mContext).getDepartmentNatureByName();
										
										 HashMap<String, String>  params = new HashMap<String, String>();
										 params.put("method", Constants.METHOD_OF_GETCONTACTS);
										 params.put("key", Constants.PUBLIC_KEY);
										 params.put("userId", mUserid);
										 params.put("DptNatureId", mCompanyId);
										 params.put("fieldList", "");
										 String mjson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, params, mContext));
										 
										 if(!"".equals(mStrJson) && mStrJson!=null){
											 ArrayList<HashMap<String, String>> Contactslist = JsonTools.getContacts(mjson);
											 if (list != null) {
												 if (list != null && list.size() > 0) {
													 if (new DBService(mContext).delContacts(mCompanyId)) {
														 new DBService(mContext).insertContacts(Contactslist, mCompanyId);
														 sendFMessage(Constants.COMMON_MESSAGE_1);
													 }
												 }
											 }
										 } 
									}
								}
							}
						} 
				 } catch(Exception e){
					 e.printStackTrace();
				 }
			}
		}).start();
	}
	
	public void openDialog() {
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_normal_progressbar);
	}
	
	public void closeDialog() {
		if(mDialog!=null){				
			mDialog.dismiss();
		}
	}
	
	 @Override
		protected void handleOtherMessage(int flag) {
			 switch(flag){
				case Constants.SUCCESS:
					closeDialog();
					
					break;
				case Constants.ERROR:
					closeDialog();
					Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
					break;
				case Constants.CONNECTION_TIMEOUT:
					closeDialog();
					Toast.makeText(mContext, "访问超时,请检查网络", Toast.LENGTH_SHORT).show();
					break;
					
				case Constants.COMMON_MESSAGE_1:
					//TODO
					break;
				}
		}
		 
}
