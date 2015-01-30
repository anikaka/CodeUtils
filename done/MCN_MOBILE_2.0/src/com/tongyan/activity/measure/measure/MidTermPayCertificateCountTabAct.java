package com.tongyan.activity.measure.measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.MeasurePayCertificateListAdapter;
import com.tongyan.activity.adapter.MyTaskLookAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ProcessOperation;
import com.tongyan.utils.WebServiceUtils;
/**
 * 
 * @Title: MidTermPayCertificateCountTabAct.java
 * @author Rubert
 * @date 2014-10-21 PM 03:13:41 
 * @version V1.0 
 * @Description: 中期支付证书汇总表
 */
public class MidTermPayCertificateCountTabAct extends AbstructCommonActivity implements View.OnClickListener{
	
	private _User mUser;
	private String mRowId = null;
	private Context mContext = this;
	private String mFlowId;
	private Dialog mDialog;
	private List<Map<String, String>> mDataList = new ArrayList<Map<String, String>>();
	private HashMap<String, ArrayList<HashMap<String, String>>>  mBaseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
	private HashMap<String, ArrayList<HashMap<String, String>>>  mBaseMapData;
	private ArrayList<HashMap<String, String>>    mArrayListTask=new ArrayList<HashMap<String,String>>(); //流程信息
	private ArrayList<HashMap<String, String>>    mArrayListTaskData;
	private ListView mListView;
	private MeasurePayCertificateListAdapter mAdapter;
	private String mSuccBack = "";
	private RelativeLayout  mCheckTools;
	private Button  
	//mBtnAllCheck,
	//mBtnCancelCheck,
	mBtnApprove,mBtnBack,mBtnTaskTracking,mBtnTaskLook;
	private Dialog mDialogApproveSuggestion,mDialogApprovePerson,mDialogDeliverSuggestion,mDialogTaskTracking,mDialogTaskLook;
	private MyExpandableListAdapter mExpandableListApdater;
	private ExpandableListView expandableListViewApprovePerson;
	private ProcessOperation mPO=ProcessOperation.APPROVE;
	private MyTaskLookAdapter   mMyTaskLookAdapter;
	private String mTaskTracking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_layout);
		initWidget();
		TextView mTitleConent = (TextView)findViewById(R.id.titleContent);
		mListView = (ListView)findViewById(R.id.commont_listview);
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mUser = mApplication.localUser;
		if(getIntent().getExtras() != null) {
			mTitleConent.setText(getResources().getString(R.string.mid_term_pay_certificate_count_table));
			mRowId = getIntent().getExtras().getString("rowid");
			mFlowId=getIntent().getExtras().getString("flowId");
			String  approveFormState=getIntent().getExtras().getString("approveFormState");
			if("待审批".equals(approveFormState)){
				mCheckTools.setVisibility(View.VISIBLE);
			}else if("未申报".equals(approveFormState)){
				mCheckTools.setVisibility(View.GONE);
			}else{
				mCheckTools.setVisibility(View.VISIBLE);
				mBtnApprove.setVisibility(View.GONE);
				mBtnBack.setVisibility(View.GONE);
			}
		}
		mAdapter = new MeasurePayCertificateListAdapter(mContext, mDataList);
		mMyTaskLookAdapter=new MyTaskLookAdapter(mContext, mArrayListTask, R.layout.measure_task_dialog_tasklook_listview_item);
		mListView.setAdapter(mAdapter);
		getRemote();
	}
	
	/** 初始化组件*/
	private  void initWidget(){
		mCheckTools=(RelativeLayout)findViewById(R.id.paymentCertificateTools);
	//	mBtnAllCheck=(Button)findViewById(R.id.btnAllCheck);
//		mBtnCancelCheck=(Button)findViewById(R.id.btnCancelCheck);
		mBtnApprove=(Button)findViewById(R.id.btnApprove);
		mBtnBack=(Button)findViewById(R.id.btnBack);
		mBtnTaskTracking=(Button)findViewById(R.id.btnTaskTracking);
		mBtnTaskLook=(Button)findViewById(R.id.btnTaskLook);
	//	mBtnAllCheck.setVisibility(View.GONE);
//		mBtnCancelCheck.setVisibility(View.GONE);
		mBtnApprove.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnTaskTracking.setOnClickListener(this);
		mBtnTaskLook.setOnClickListener(this);
	}

	/** 获取远程数据*/
	public void getRemote() {
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable(){
			@Override
			public void run() {
				String param = "{rowId:'" + mRowId + "'}";
				try {
					String str1 = WebServiceUtils.getRequestStr(mUser.getUsername(), mUser.getPassword(), String.valueOf(2000), String.valueOf(1), Constansts.TYPE_OF_MEASUER_CERTIFICATE, param, Constansts.METHOD_OF_GETLISTNOPAGE, mContext);
					Map<String,Object> mR = new Str2Json().getPayCertificate(str1);
					if(mR != null) {
						mSuccBack = (String)mR.get("s");
						if("ok".equals(mSuccBack)) {
							List<Map<String, String>> newFlows = (List<Map<String, String>>)mR.get("v");
							if(newFlows == null || newFlows.size() == 0) {
								sendMessage(Constansts.NO_DATA);
							} else {
								mDataList.clear();
								mDataList.addAll(newFlows);
								sendMessage(Constansts.SUCCESS);
							}
						} else {
							//sendMessage(Constansts.ERRER);
						}
					} else {
						//sendMessage(Constansts.NET_ERROR);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/** 刷新列表*/
	public void refreshListView() {
		if(mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	/*** 审批数据跟新*/
	public void updateApproveData(){
		if(mBaseMap.size()>0){
			mBaseMapData.clear();
		}
		if(mBaseMapData!=null){			
			mBaseMap.putAll(mBaseMapData);
			mBaseMapData.clear();
		}
	}
	
	public void closeMDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	/** * 审批数据获取*/
	public void getApproveData(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			StringBuffer   str=new StringBuffer();
			str.append("{");
			str.append("rowId");
			str.append(":");
			str.append("\'"+mRowId+"\'");
			str.append(",");
			str.append("flowId");
			str.append(":");
			str.append("\'"+mFlowId+"\'");
			str.append("}");
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("publicKey", Constansts.PUBLIC_KEY);
			parameters.put("userName", mUser.getUsername());
			parameters.put("Password", mUser.getPassword());
			parameters.put("type", "中期支付证书");
			parameters.put("parms",str.toString().trim());
			String stream=null;
			try {
				stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_READYFORAPPROVE, mContext);
				if(stream!=null){
					mBaseMapData=new Str2Json().getPaymentCertificate(stream);
					if(mBaseMapData!=null && mBaseMapData.size()>0){
						sendMessage(Constansts.MES_TYPE_2);
					}else{
					sendMessage(Constansts.MES_TYPE_3);
					}
				}else{
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendMessage(Constansts.CONNECTION_TIMEOUT);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			}
		}).start();
	}
	
	/** 审批数据上传*/
	public void  uploadApproveData(final String str,final ProcessOperation  op){
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer  data=new StringBuffer();
				data.append("{");//开始
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword()); 
				parameters.put("type", "中期支付证书");
				data.append("\'rowId\'");
				data.append(":");
			    data.append("\'"+mRowId+"\'");
				data.append(",");
				data.append("\'flowId\'");
				data.append(":");
				data.append("\'"+mFlowId+"\'");
				data.append(",");
				//command:first打回到第一步\previous打回到上一步\next正常审批
				if(op==null){
			   //正常审批
				data.append("\'command\'");
				data.append(":");
				data.append("\'next\'");
				data.append(",");
				//stepId:当command为next时提供的步骤Id
				data.append("\'stepId\':");
				data.append("\'"+mBaseMap.get("approveAttribute").get(0).get("stepId")+"\'");
				data.append(",");
				if(mBaseMap.get("arrayListApprovePersonInfo").size()>0){
					//	selectEmpId:当command为next时提供的用户选择的下步执行人id(以，号分隔的字符串)
					data.append("\'selectEmpId\'");
					data.append(":");
					data.append("\'");
					for(int j=0;j<mBaseMap.get("arrayListApprovePersonInfo").size();j++){
						if("1".equals(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("checkBoxState"))){							
							data.append(mBaseMap.get("arrayListApprovePersonInfo").get(j).get("personId"));
							Log.i("test", "personId="+mBaseMap.get("arrayListApprovePersonInfo").get(j).get("personId"));
						}
						if(j!=mBaseMap.get("arrayListApprovePersonInfo").size()-1){
							data.append(",");
						}
					}
					data.append("\'");
					data.append(",");
				}
				}else if(op==ProcessOperation.STEP_FIRST){
					// 打回到第一步
					data.append("\'command\'");
					data.append(":");
					data.append("\'first\'");
					data.append(",");
				}else if(op==ProcessOperation.STEP_BACk){
					//打回到上一步
					data.append("\'command\'");
					data.append(":");
					data.append("\'previous\'");
					data.append(",");
				}
				//suggest:用户输入的处理意见或打回原因
				data.append("\'suggest\'");
				data.append(":");
				data.append("\'");
				data.append(str==null?"":str);
				data.append("\'");
				data.append("}");//结束
				parameters.put("parms", data.toString().trim());
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_APPROVE, mContext);
					if(stream!=null){
						if(new Str2Json().getApproveResult(stream)){
							mPO=ProcessOperation.SUCCESS;
							sendMessage(Constansts.MES_TYPE_2);
						}else{
							sendMessage(Constansts.MES_TYPE_3);
						}
					}else{
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();;
	}
	
	/** 判断用户是否勾选了人 如果没有勾选人不能审批 return false   otherwise true*/
	
	public boolean  isCheckPerson(){
		ArrayList<HashMap<String, String>>  arrayList=mBaseMap.get("arrayListApprovePersonInfo");
		for(int i=0; i<arrayList.size();i++){
			HashMap<String, String> map=arrayList.get(i);
			if("1".equals(map.get("checkBoxState"))){
				return true;
			}
			if(i==arrayList.size()-1 && "0".equals(map.get("checkBoxState"))){
				return false;
			}
		}
		return false;
	}
	
	/** 审批流程*/
	public  void   approve(){
		mDialogApproveSuggestion=new Dialog(mContext);
		mDialogApproveSuggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogApproveSuggestion.setCanceledOnTouchOutside(false);
		mDialogApproveSuggestion.setContentView(R.layout.measure_mid_record_approve_dialog);
		mDialogApproveSuggestion.show();
		EditText txtApproveStepName=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveStepName);
		EditText txtApproveOperatePerson=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveOperatePerson);
//		final EditText txtApproveOperateTime=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveOperateTime);
		final EditText txtApproveSuggestion=(EditText)mDialogApproveSuggestion.findViewById(R.id.txtApproveSuggestion);
		Button   btnApproveNext=(Button)mDialogApproveSuggestion.findViewById(R.id.btnApproveNext);
		Button   btnApproveCancel=(Button)mDialogApproveSuggestion.findViewById(R.id.btnApproveCancel);
		txtApproveSuggestion.setText("");
		if(mBaseMap!=null && mBaseMap.size()>0){
			ArrayList<HashMap<String, String>> arrayListApproveAttribute=mBaseMap.get("approveAttribute");
			if(arrayListApproveAttribute.size()>0){
				HashMap<String, String> map=arrayListApproveAttribute.get(0);
				txtApproveStepName.setText(map.get("stepName"));
				txtApproveOperatePerson.setText(mUser.getEmpName());
//				txtApproveOperateTime.setText(text);
			}
		}
		//时间对话框
//		txtApproveOperateTime.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				new MyDatePickerDialog(mContext, new MyDatePickerDialog.OnDateTimeSetListener() {
//					
//					@Override
//					public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
//							int hour, int minute) {
//						txtApproveOperateTime.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
//					}
//				}).show();
//			}
//		});
		//退出对话框
		btnApproveCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialogApproveSuggestion.dismiss();
			}
		});
		//转交给下一步
		btnApproveNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Button  btnApprovePersonConfirm=null;
				if("".equals(txtApproveSuggestion.getText().toString())){
					Toast.makeText(mContext,"审批意见不能为空" ,Toast.LENGTH_SHORT).show();
					return;
				}
				if(mDialogApprovePerson==null){						
					mDialogApprovePerson=new Dialog(mContext,R.style.dialog);
					mDialogApprovePerson.requestWindowFeature(Window.FEATURE_NO_TITLE);
					mDialogApprovePerson.setContentView(R.layout.measure_mid_record_approve_person_dialog);
					expandableListViewApprovePerson=(ExpandableListView)mDialogApprovePerson.findViewById(R.id.expandableListViewApprovePerson);
				}

				
				if(mBaseMap!=null && mBaseMap.size()>0){
					ArrayList<HashMap<String, String>> arrayList=mBaseMap.get("approveAttribute");
					if(arrayList!=null && arrayList.size()>0){
							if("true".equals(arrayList.get(0).get("hasNext"))){			
								mExpandableListApdater=new MyExpandableListAdapter();
								expandableListViewApprovePerson.setAdapter(mExpandableListApdater);
								expandableListViewApprovePerson.expandGroup(0);
								mDialogApprovePerson.show();
								btnApprovePersonConfirm=(Button)mDialogApprovePerson.findViewById(R.id.btnApprovePersonConfirm);									
							}else{
								baseShowDialog();
								uploadApproveData(txtApproveSuggestion.getText().toString(),null);
							}
					}
				}
				//完成按钮
				if(btnApprovePersonConfirm!=null){
					btnApprovePersonConfirm.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(isCheckPerson()==false){
								Toast.makeText(mContext,"必须选择审批人" ,Toast.LENGTH_SHORT).show();
								return;
							}else{								
								baseShowDialog();
								uploadApproveData(txtApproveSuggestion.getText().toString(),null);
							}
						}
					});
				}
			}
		});
	}

	/** 打回流程*/
	public void deliver(){
		if(mDialogDeliverSuggestion==null){
			mDialogDeliverSuggestion=new Dialog(mContext);
			mDialogDeliverSuggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogDeliverSuggestion.setCanceledOnTouchOutside(false);
			mDialogDeliverSuggestion.setContentView(R.layout.measure_mid_record_deliver_dialog);
		}
		mDialogDeliverSuggestion.show();
		final EditText txtDeliverOperatePerson=(EditText)mDialogDeliverSuggestion.findViewById(R.id.txtDeliverOperatePerson);
		final EditText txtDeliverSuggestion=(EditText)mDialogDeliverSuggestion.findViewById(R.id.txtDeliverSuggestion);
		final Button btnDeliverCancel=(Button)mDialogDeliverSuggestion.findViewById(R.id.btnDeliverCancel);
		final Button btnDeliverBack=(Button)mDialogDeliverSuggestion.findViewById(R.id.btnDeliverBack);
		final Button btnDeliverFirst=(Button)mDialogDeliverSuggestion.findViewById(R.id.btnDeliverFirst);
		txtDeliverOperatePerson.setText(mUser.getUsername());
		txtDeliverSuggestion.setText("");
		//打回给上一步
		btnDeliverBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("".equals(txtDeliverSuggestion.getText().toString())){
					Toast.makeText(mContext, "原因不能为空", Toast.LENGTH_SHORT).show();
				}else{	
					baseShowDialog();
					uploadApproveData(txtDeliverSuggestion.getText().toString(),ProcessOperation.STEP_BACk);	
				}
			}
		});
		//打回第一步
		btnDeliverFirst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("".equals(txtDeliverSuggestion.getText().toString())){
					Toast.makeText(mContext, "原因不能为空", Toast.LENGTH_SHORT).show();
				}else{					
					baseShowDialog();
					uploadApproveData(txtDeliverSuggestion.getText().toString(),ProcessOperation.STEP_FIRST);	
				}
			}
		});
		//取消对话框
		btnDeliverCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialogDeliverSuggestion.dismiss();
			}
		});
	}
	
	/** 流程跟踪信息获取*/
	public void  getTaskTrackingData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());			
				parameters.put("type", "Trace");
				parameters.put("flowId", mFlowId);
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_FLOW, mContext);
						if(new Str2Json().getTaskTrackingData(stream)!=null){
							mTaskTracking=new Str2Json().getTaskTrackingData(stream);
							sendMessage(Constansts.MES_TYPE_4);
						}else{
							sendMessage(Constansts.CONNECTION_TIMEOUT);
						}

				} catch (IOException e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/** 流程查看信息获取*/
	public void  getTaskLookData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());			
				parameters.put("type", "View");
				parameters.put("flowId", mFlowId);
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_FLOW, mContext);
					mArrayListTaskData=new Str2Json().getTaskLookData(stream);
					if(mArrayListTaskData!=null ){
						sendMessage(Constansts.MES_TYPE_5);
					}else{
						sendMessage(Constansts.CONNECTION_TIMEOUT);
					}
				} catch (IOException e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/** 流程跟踪对话框*/
	public void showDialogTaskTracking(){
		if(mDialogTaskTracking==null){
			mDialogTaskTracking=new Dialog(mContext);
			mDialogTaskTracking.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogTaskTracking.setContentView(R.layout.measure_task_tracking_dialog);
		}
		mDialogTaskTracking.show();
		TextView txtTaskTracking=(TextView)mDialogTaskTracking.findViewById(R.id.textViewMeasureTaskTrackingContent);
		txtTaskTracking.setText(Html.fromHtml(mTaskTracking));
	}
	
	/** 流程查看对话框*/
	public void showDialogTaskLook(){
		if(mDialogTaskLook==null){
			mDialogTaskLook=new Dialog(mContext);
			mDialogTaskLook.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogTaskLook.setContentView(R.layout.measure_task_look_dialog);
		}
		mDialogTaskLook.show();
		ListView mListView=(ListView)mDialogTaskLook.findViewById(R.id.listViewTaskLook);
		mListView.setAdapter(mMyTaskLookAdapter);
	}
	
	/** 关闭流程信息对话框*/
	public void closeDialogTask(){
		if(mDialogTaskTracking!=null){
			mDialogTaskTracking.dismiss();
		}
	}
	
	/** 更新流程数据*/
	public void updateTaskData(){
		if(mArrayListTask.size()>0){
			mArrayListTask.clear();
		}
		if(mArrayListTaskData.size()>0){
			mArrayListTask.addAll(mArrayListTaskData);
			mMyTaskLookAdapter.notifyDataSetChanged();
			mArrayListTaskData.clear();
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			closeMDialog();
			refreshListView();
			break;
		case Constansts.ERRER:
			closeMDialog();
			Toast.makeText(this, mSuccBack, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			closeMDialog();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
			closeMDialog();
			Toast.makeText(this, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			closeMDialog();
			closeWindowDialog();
			baseCloseDialog();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1 :
			closeMDialog();
			break;
		case Constansts.MES_TYPE_2:
			updateApproveData();
			closeWindowDialog();
			if(mPO==ProcessOperation.APPROVE){				
				//审批
				approve();
			}else if(mPO==ProcessOperation.DELIVER){
				//退回
			}else if(mPO==null){
				//更新
				
			}else if(mPO==ProcessOperation.SUCCESS){			
				Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
			}
			break;
		case Constansts.MES_TYPE_3:
				Toast.makeText(mContext, "操作失败,该条数据已经处理过", Toast.LENGTH_SHORT).show();
				closeWindowDialog();
			break;
		case Constansts.MES_TYPE_4:  //流程跟踪
				 baseCloseDialog();
				 showDialogTaskTracking();
			break;
		case Constansts.MES_TYPE_5:  //流程查看
				 baseCloseDialog();
				 updateTaskData();
				 showDialogTaskLook();
			break;
		default:
			baseCloseDialog();
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApprove : //审批按钮
					mPO=ProcessOperation.APPROVE;
					baseShowDialog();
					getApproveData();
			break;
		case R.id.btnBack :     //退回 按钮
					mPO=ProcessOperation.DELIVER;
					deliver();
			break;
		case R.id.btnTaskTracking: //流程跟踪
			        baseShowDialog();
			        getTaskTrackingData();
			break;
		case R.id.btnTaskLook:  //流程查看
					baseShowDialog();
					getTaskLookData();
			break;
		default:
			break;
		}
	}
	
	/** 关闭当前对话框*/
	public void closeWindowDialog(){
		baseCloseDialog();
		if(mDialogApproveSuggestion!=null){
			mDialogApproveSuggestion.dismiss();
		}
		if(mDialogApprovePerson!=null){
			mDialogApprovePerson.dismiss();
		}
		if(mDialogDeliverSuggestion!=null){
			mDialogDeliverSuggestion.dismiss();
		}
	}
	/** 部门信息和人员列表适配器*/
	private class  MyExpandableListAdapter extends  BaseExpandableListAdapter{

		public  ArrayList<HashMap<String, String>> mArrayListDept,mArrayListPerson;		
		private ChildViewHolder mChildViewHolder;
		private ParentViewHolder mParentViewHolder;
		private LayoutInflater mInflater;
		private int mChildIndex; //子选项开始的下标
		private MyExpandableListAdapter(){
			mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		private MyExpandableListAdapter(ArrayList<HashMap<String, String>> arrayListDept,ArrayList<HashMap<String, String>> arrayListPerson){
			this.mArrayListDept=arrayListDept;
			this.mArrayListPerson=arrayListPerson;
		}
		
		@Override
		public int getGroupCount() {
			return mBaseMap.get("approveDeptInfo").size();
		}

		/** 得到每个部门的人数*/
		public int getPersonCount(int groupPosition){
			int size=0;
			if(mBaseMap!=null){				
				size=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(groupPosition).get("count"));
			}
			return size;
		}
		
		/** 记录子选项的开始下标*/
		public int  getChildIndex(int gropPosition){
			int size=0;
			if(mBaseMap!=null){
				if(gropPosition==0){
//					 size=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(gropPosition).get("count"));
					 return 0;
				}else{					
					for(int i=0;i<gropPosition;i++){
						size+=Integer.parseInt(mBaseMap.get("approveDeptInfo").get(i).get("count"));
					}
				}
			}
			return size;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			
			//BaseMap.get("arrayListApprovePersonInfo").size()
			return getPersonCount(groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			
			return mBaseMap.get("approveDeptInfo").get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			return childPosition;
		}

		@Override
		public long getGroupId(int groupPosition) {
			
			return groupPosition ;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			   if(convertView==null){
				   mParentViewHolder=new ParentViewHolder();
				  convertView= mInflater.inflate(R.layout.measure_mid_record_approve_person_listview_parent_item, null);
				  mParentViewHolder.mTextViewDeptName=(TextView)convertView.findViewById(R.id.textViewParentDeptName);
				  convertView.setTag(mParentViewHolder);
			   }else{
				   mParentViewHolder=(ParentViewHolder)convertView.getTag();
			   }
			     HashMap<String, String> map=mBaseMap.get("approveDeptInfo").get(groupPosition);
			     if(map!=null && map.size()>0){
			    	 mParentViewHolder.mTextViewDeptName.setText(map.get("deptName"));
			     }
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
				if(convertView==null){					
					mChildViewHolder=new ChildViewHolder();
					convertView=mInflater.inflate(R.layout.measure_mid_record_approve_person_listview_child_item, null);
					mChildViewHolder.mTextViewDeptPersonName=(TextView)convertView.findViewById(R.id.textViewChildDeptPersonName);
					mChildViewHolder.mCheckBoxDeptPersonName=(CheckBox)convertView.findViewById(R.id.checkBoxChildPersonName);
					convertView.setTag(mChildViewHolder);
				}else{
					mChildViewHolder=(ChildViewHolder)convertView.getTag();
				}
					HashMap<String, String> map=mBaseMap.get("arrayListApprovePersonInfo").get(mChildIndex++);
					if("1".equals(map.get("checkBoxState"))){
						mChildViewHolder.mCheckBoxDeptPersonName.setChecked(true);
					}else{
						mChildViewHolder.mCheckBoxDeptPersonName.setChecked(false);
					}
					if(map!=null && map.size()>0){
						mChildViewHolder.mTextViewDeptPersonName.setText(map.get("personName"));
				}
				Log.i("test", "positon="+mChildIndex+",groupPosition="+groupPosition+",childPositon"+childPosition);
			
				mChildViewHolder.mCheckBoxDeptPersonName.setOnClickListener(new  CheckBoxListener(mChildIndex-1,mChildViewHolder.mCheckBoxDeptPersonName));
		
				return convertView;
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			super.onGroupCollapsed(groupPosition);
		}
		
		@Override
		public void onGroupExpanded(int groupPosition) {
		     for(int i=0;i<expandableListViewApprovePerson.getCount();i++){
		    		if(i!=groupPosition && expandableListViewApprovePerson.isGroupExpanded(groupPosition)){
		    			expandableListViewApprovePerson.collapseGroup(i);
		    		}
		     }
		     //每展开一下都会重新获取子选项的下标,来获取map的下标
		     mChildIndex=getChildIndex(groupPosition);
			super.onGroupExpanded(groupPosition);
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return false;
		}	
		
		private  class CheckBoxListener implements View.OnClickListener{
			
			private int mPosition;
			private CheckBox mCheckBox;
			public CheckBoxListener(int position,CheckBox checkBox){
				this.mPosition=position;
				this.mCheckBox=checkBox;
			}
			@Override
			public void onClick(View v) {
					for(int i=0;i<mBaseMap.get("arrayListApprovePersonInfo").size();i++){
						if(i==mPosition){
							HashMap<String, String> map=mBaseMap.get("arrayListApprovePersonInfo").get(i);
							if(mCheckBox.isChecked()){
								map.put("checkBoxState", "1");
							}else{
								map.put("checkBoxState", "0");
							}
			}
		}
			}	
		}

	public  class  ChildViewHolder{
		private TextView mTextViewDeptPersonName;
		private CheckBox mCheckBoxDeptPersonName;
	}
	public  class  ParentViewHolder{
		private TextView mTextViewDeptName;
	}
 }
}
