package com.tongyan.yanan.act.quality;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.common.widgets.view.MDatePickerDialog;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
import com.tongyan.yanan.tfinal.https.HttpUtils;

/**
 * 新增平板静载荷试验
 * @author ChenLang
 * @Desc modify by Rubert 2014/09/03
 */
public class QualityStaticLoadAdd extends FinalActivity{
	// 工程
	@ViewInject(id = R.id.txtObject_staticload) TextView mTxtobject;
	// 合同段
	@ViewInject(id = R.id.txtPact_staticload) TextView mTxtPact;
	// 检测人
	@ViewInject(id = R.id.txtExaminePerson_staticload) TextView mTxtExaminePerson;
	// 检测编号
	@ViewInject(id = R.id.txtExamineNo_staticload) EditText mEditExamineNo;
	// 检测单位
	@ViewInject(id = R.id.spinnerExamineUnit_staticload) Spinner mSpinnerExamineUnit;
	// 检测时间
	@ViewInject(id = R.id.editExamineDate_staticload, click="selectDate") TextView mEditExamineDate;
	// 监测数量
	@ViewInject(id = R.id.testing_layer_numbers_container,click="inputNumber") RelativeLayout mEditPointNumberContainer;
	@ViewInject(id = R.id.experimentPoint) TextView mEditPointNumber;
	// 试验内容
	@ViewInject(id = R.id.editExperimentContent_staticload) EditText mEditContent;
	// 试验结论
	@ViewInject(id = R.id.editExperimentVerdict_staticload) EditText mEditVerdict;
	// 保存按钮
	@ViewInject(id = R.id.butConfirm_staticload,click="saveStaticLoadInfo") Button mButSave;
	// 清空按钮
	@ViewInject(id = R.id.butCancel_staticload,click="clearStaticLoadInfo")
	Button mButClear;
	
	private Context mContext=this;
	private SharedPreferences mSP;
	private String mUserId;
	private Bundle mBundle;
	private String mNoId;//平板静电荷试验编号Id
	//private String mSpinnerItemValue="";
	//private String mLotId;//合同段Id
	//private String mProjectId;//期段Id
	//private String mPreson;//上传人
	private String mUnitId="";//单位Id
	private String mProjectName;//期段名称
	private String mLotName;//合同段名称  
	private  static final int RESULT_LAYOUT=0x4234;
	
	private String mIntentType = null;
	
	ArrayList<HashMap<String, String>>  mConpanyUnitList=new ArrayList<HashMap<String,String>>();
	
	
	private HashMap<String,String> mCacheMap = null;
	
	private ArrayAdapter<HashMap<String, String>> mDistanceAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quality_staticload_add);
		mSP=PreferenceManager.getDefaultSharedPreferences(mContext);
		mUserId =  mSP.getString(Constants.PREFERENCES_INFO_USERID, "");
		((TextView)findViewById(R.id.title_common_content)).setText(getResources().getString(R.string.addStaticUpload));
		if(getIntent().getExtras()!=null){
			mBundle=getIntent().getExtras();
			mNoId = mBundle.getString("noId");
			mProjectName=mBundle.getString("projectName");
			mLotName=mBundle.getString("lotName");
			mIntentType = mBundle.getString("IntentType");
			
			mTxtobject.setText(getResources().getString(R.string.project) +  mProjectName);//项目
			mTxtPact.setText(getResources().getString(R.string.pact) +  mLotName);//合同段
			mTxtExaminePerson.setText(getResources().getString(R.string.examinePerson) +  mSP.getString(Constants.PREFERENCES_INFO_USERNAME, ""));//检测人
			
			if("modify".equals(mIntentType)) {
				mCacheMap = (HashMap<String,String>)mBundle.get("CacheMap");
				initData();
			}
		}
		
		// 监测单位
		mConpanyUnitList = new DBService(mContext).getTestCompanyList();
		if(mConpanyUnitList.size() <= 0) {
			getDptNature();
		}
		
		mDistanceAdapter = new ArrayAdapter<HashMap<String, String>>(mContext, R.layout.common_spinner, mConpanyUnitList){
			 @Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				 if (convertView == null) {
						convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner_down_item, null);
					} 
					TextView textView = (TextView)convertView.findViewById(R.id.spinner_content_item_text);
					textView.setText(mConpanyUnitList.get(position).get("DeptName"));
					return convertView;
			}
			 
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.common_spinner, null);
				} 
				TextView textView = (TextView)convertView.findViewById(R.id.spinner_id);
				textView.setText(mConpanyUnitList.get(position).get("DeptName"));
				return convertView;
			}
		 };
		 mSpinnerExamineUnit.setAdapter(mDistanceAdapter);
		 mSpinnerExamineUnit.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//mDptId = mConpanyList.get(position).get("DptId");
				//mDptName = mConpanyUnitList.get(position).get("DeptName");
				//mSpinnerItemValue = mConpanyUnitList.get(position).get("DeptName");
				mUnitId = mConpanyUnitList.get(position).get("DptId");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
			
		});
		
	}

	
	/** 监测编号输入 */
	public void inputNumber(View v) {
		Intent intent = new Intent(mContext, QualityStaticLoadAddPoint.class);
		intent.putExtras(mBundle);
		intent.putExtra("IntentType", "add");
		startActivityForResult(intent, RESULT_LAYOUT);
	}
	/**选择时间 */
	public void selectDate(View v) {
		mEditExamineDate.setInputType(InputType.TYPE_NULL);
		new MDatePickerDialog(mContext,
				new MDatePickerDialog.OnDateTimeSetListener() {
					@Override
					public boolean onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
						mEditExamineDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
						return true;
					}
				}).show();
	}

	/**组件数据初始化 */
	public void initData(){
		if(mCacheMap !=null){
			mEditExamineNo.setText(mCacheMap.get("no"));
			for(int i=0; i<mConpanyUnitList.size(); i++){
				HashMap<String, String> mMapUnit = mConpanyUnitList.get(i);
				if(mMapUnit != null) {
					if(mCacheMap.get("unit").equals(mMapUnit.get("DptId"))){
						mSpinnerExamineUnit.setSelection(i);
						break ;
					}
				}
			}
		mEditExamineDate.setText(mCacheMap.get("date"));
		mEditContent.setText(mCacheMap.get("content"));//试验内容
		mEditVerdict.setText(mCacheMap.get("conclusion"));//试验结论
		mEditPointNumber.setText(getResources().getString(R.string.examinePoint) + new DBService(mContext).queryTableExaminePointNumber(mNoId));
		}
	}
	/**
	 *  保存数据
	 * <p>插入数据之前判断信息是否填写完全
	*  数据库用相同的编号就不插入数据</p>  
	 * @param v
	 */
	public void saveStaticLoadInfo(View v) {
		if (verify()) {
			if(new DBService(mContext).updateTableStaticLoad(mNoId, mUserId,
					mTxtExaminePerson.getText().toString(), mEditExamineNoText, mUnitId, mEditExamineDateText, mEditContent.getText()
							.toString(), mEditVerdict.getText().toString())) {
				Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	private String mEditExamineNoText = null;
	private String mEditExamineDateText = null;
	public boolean verify() {
		mEditExamineNoText = mEditExamineNo.getText().toString();
		if("".equals(mEditExamineNoText)) {
			Toast.makeText(mContext, "请填写检测编号", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(mUnitId)) {
			Toast.makeText(mContext, "请选择检测单位", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(0 == new DBService(mContext).queryTableExaminePointNumber(mNoId)) {
			Toast.makeText(mContext, "请添加检测点", Toast.LENGTH_SHORT).show();
			return false;
		}
		mEditExamineDateText = mEditExamineDate.getText().toString();
		if("".equals(mEditExamineDateText)) {
			Toast.makeText(mContext, "请选择检测时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/** 清空数据 */
	public void clearStaticLoadInfo(View v){
		finish();
	}
	
	/** 检测单位获取*/
	public void getDptNature() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 HashMap<String, String>  mParams=new HashMap<String, String>();
				 mParams.put("method", Constants.METHOD_OF_GETDPTNATURE);
				 mParams.put("key", Constants.PUBLIC_KEY);
				 mParams.put("userId", mUserId);
				 mParams.put("fieldList", "");
				 try{
					String mStrJson = HttpUtils.httpGetString(HttpUtils.getUrlWithParas(Constants.SERVICE_OA, mParams, mContext));
					if(!"".equals(mStrJson) && mStrJson!=null){		
						ArrayList<HashMap<String, String>> list = JsonTools.getDptNature(mStrJson);
						if(list != null) {
								if(list != null && list.size() > 0) {
									if(new DBService(mContext).delDepartmentNature(mUserId)) {
										new DBService(mContext).insertDepartmentNature(list);
										
										 String mCompanyId = new DBService(mContext).getDepartmentNatureByName();
										
										 HashMap<String, String>  params = new HashMap<String, String>();
										 params.put("method", Constants.METHOD_OF_GETCONTACTS);
										 params.put("key", Constants.PUBLIC_KEY);
										 params.put("userId", mUserId);
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

	@Override
	protected void handleOtherMessage(int flag) {
		super.handleOtherMessage(flag);
		switch (flag) {
		case Constants.SUCCESS:
			break;
		case Constants.ERROR:
			Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constants.COMMON_MESSAGE_1:
			ArrayList<HashMap<String, String>> mUnitList=new DBService(mContext).getTestCompanyList();
			if(mUnitList != null) {
				mConpanyUnitList.clear();
				mConpanyUnitList.addAll(mUnitList);
			}
			mDistanceAdapter.notifyDataSetChanged();
			break;
		case Constants.COMMON_MESSAGE_2:
			Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==RESULT_LAYOUT){
			mEditPointNumber.setText(getResources().getString(R.string.examinePoint) + String.valueOf(new DBService(mContext).queryTableExaminePointNumber(mNoId)));
		}
	}

}
