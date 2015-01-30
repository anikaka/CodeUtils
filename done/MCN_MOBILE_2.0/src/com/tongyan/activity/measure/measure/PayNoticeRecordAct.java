package com.tongyan.activity.measure.measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.bbalbs.common.a.c;
import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.R.id;
import com.tongyan.activity.adapter.MyTaskLookAdapter;
import com.tongyan.activity.adapter.PayNoticeRecordListViewApdater;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ProcessOperation;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.view.MyDatePickerDialog;
/**
 * 
 * @Title: PayNoticeRecordAct 
 * @author Rubert
 * @date 2014-10-21 PM 03:13:41 
 * @version V1.0 
 * @Description: 付款通知单
 */
public class PayNoticeRecordAct extends AbstructCommonActivity implements OnClickListener{
	
   private	TextView projectNameContent, 		//项目名称
   							   periodsContent,                //期数
							   pactName,							//合同名称
							   pactNo,								//合同编号
							   pactPrice,							//合同总价
							   currentPeriodPrice,   		    //本期实付
							   currentPeriodScale,  		    //本期实付占合同比例
							   write,									//本期实付小写
							   countCurrentPeriodPrice,  //累计付款
							   countCurrentPeriodScale, //累计付款占合同比例
							   countWrite;					   //累计付款小写
	
   private   ListView  payProjectListView, 	 	   //付款项目
   								deductMoneyProject;      //扣款项目
   
   private RelativeLayout mRlProjectAttribute,mCheckTools;
   private TableLayout  mPayFormTableLayoutl;
   private Button  
   //mBtnAllCheck,
   //mBtnCancelCheck,
   mBtnApprove,mBtnBack,mBtnTaskTracking,mBtnTaskLook;
   private Context mContext=this;
   private _User mUser;
   private MyApplication mApplication;
   private HashMap<String, ArrayList<HashMap<String, String>>> mBaseMapData;
   private HashMap<String, ArrayList<HashMap<String, String>>>  mBaseMap=new HashMap<String, ArrayList<HashMap<String,String>>>();
   private ArrayList<HashMap<String, String>> mArrayListPay=new ArrayList<HashMap<String,String>>();
   private ArrayList<HashMap<String, String>> mArrayListDeduct=new ArrayList<HashMap<String,String>>();
   private ArrayList<HashMap<String, String>> mArrayListTask=new ArrayList<HashMap<String,String>>(); //流程信息
   private ArrayList<HashMap<String, String>> mArrayListTaskData;
   private PayNoticeRecordListViewApdater  mAdapterPay;
   private PayNoticeRecordListViewApdater   mAdapterDeduct;
   private String mRowId; // 计量期数的id
   private String mFlowId; //计量列表的Id
   private Dialog mDialogApproveSuggestion,mDialogApprovePerson,mDialogDeliverSuggestion,mDialogTaskTracking,mDialogTaskLook;
   private MyExpandableListAdapter mExpandableListApdater;
   private ExpandableListView expandableListViewApprovePerson;
   private ProcessOperation mPO=ProcessOperation.APPROVE;
   private MyTaskLookAdapter mMyTaskLookAdapter;//流程查看显示适配器
   private String mTaskTracking; 
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_pay_notice_record);
		mApplication= ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mUser=mApplication.localUser;
		initWidget();
		if(getIntent().getExtras()!=null){
			mRowId=getIntent().getExtras().getString("rowId");
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
		//绑定适配器
		mMyTaskLookAdapter=new MyTaskLookAdapter(mContext, mArrayListTask, R.layout.measure_task_dialog_tasklook_listview_item);
		mAdapterPay=new PayNoticeRecordListViewApdater(mContext, mArrayListPay, R.layout.measure_pay_notice_record_listview_item);
		mAdapterDeduct=new PayNoticeRecordListViewApdater(mContext, mArrayListDeduct, R.layout.measure_pay_notice_record_listview_item);
		payProjectListView.setAdapter(mAdapterPay);
		deductMoneyProject.setAdapter(mAdapterDeduct);
		requestData();
		baseShowDialog();
	}
	
	
	/** 设置视图*/
	public void setView(int flag){
		if(mRlProjectAttribute!=null){
			mRlProjectAttribute.setVisibility(flag);
		}
		if(mPayFormTableLayoutl!=null){
			mPayFormTableLayoutl.setVisibility(flag);
		}
	}
	
	   /**初始化组件 */
		public void initWidget(){
			projectNameContent=(TextView)findViewById(R.id.projectNameContent);
			periodsContent=(TextView)findViewById(R.id.periodsContent);
			pactName=(TextView)findViewById(R.id.pactName);
			pactNo=(TextView)findViewById(R.id.pactNo);
			pactPrice=(TextView)findViewById(R.id.pactPrice);
			currentPeriodPrice=(TextView)findViewById(R.id.currentPeriodPrice);
			currentPeriodScale=(TextView)findViewById(R.id.currentPeriodScale);
			write=(TextView)findViewById(R.id.write);
			countCurrentPeriodPrice=(TextView)findViewById(R.id.countCurrentPeriodPrice);
			countCurrentPeriodScale=(TextView)findViewById(R.id.countCurrentPeriodScale);
			countWrite=(TextView)findViewById(R.id.countWrite);
			payProjectListView=(ListView)findViewById(R.id.payProject);
			deductMoneyProject=(ListView)findViewById(R.id.deductMoneyProject);
			mRlProjectAttribute=(RelativeLayout)findViewById(R.id.rlProjectAttribute);
			mPayFormTableLayoutl=(TableLayout)findViewById(R.id.payFormTableLayout);
			mCheckTools=(RelativeLayout)findViewById(R.id.rlPayNoticeRecordTools);
//			mBtnAllCheck=(Button)findViewById(R.id.btnAllCheck);
//			mBtnCancelCheck=(Button)findViewById(R.id.btnCancelCheck);
			mBtnApprove=(Button)findViewById(R.id.btnApprove);
			mBtnBack=(Button)findViewById(R.id.btnBack);
			mBtnTaskTracking=(Button)findViewById(R.id.btnTaskTracking);
			mBtnTaskLook=(Button)findViewById(R.id.btnTaskLook);
//			mBtnAllCheck.setVisibility(View.GONE);
//			mBtnCancelCheck.setVisibility(View.GONE);
			mBtnBack.setVisibility(View.GONE);
			mBtnApprove.setOnClickListener(this);
			mBtnBack.setOnClickListener(this);
			mBtnTaskTracking.setOnClickListener(this);
			mBtnTaskLook.setOnClickListener(this);
		}
		
	/** 获取行的Id*/
	public String getRowId(){
		if(getIntent().getExtras()!=null){
			return getIntent().getExtras().getString("rowId");
		}
		return "";
	}
	
	/** 数据请求*/
	public void requestData(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());
				parameters.put("type", "PaymentNotice");
				parameters.put("id", getRowId());
				String stream=null;
				try{					
					 stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_GETCONTENT, mContext);
					 mBaseMap=new Str2Json().getPayFormData(stream);
					 if(mBaseMap==null || mBaseMap.size()<0){
						 sendMessage(Constansts.ERRER);
					 }else
						 sendMessage(Constansts.SUCCESS);
				}catch(Exception e){
					e.printStackTrace();
					sendMessage(Constansts.ERRER);
				}
			}
		}).start();
	}
	
	/** 数据更新*/
	public void updateData(){
		if(mBaseMap!=null){
			mArrayListPay.clear();
			mArrayListDeduct.clear();
			ArrayList<HashMap<String, String>> arrayProjectAttribute=mBaseMap.get("arrayProjectAttribute");
			ArrayList<HashMap<String, String>> arrayPactAttribute=mBaseMap.get("arrayPactAttribute");
			ArrayList<HashMap<String, String>> arrayCurrentPeriodPay=mBaseMap.get("arrayCurrentPeriodPay");
			ArrayList<HashMap<String, String>> arrayCountPay=mBaseMap.get("arrayCountPay");
			//项目信息
			if(arrayProjectAttribute!=null){
				HashMap<String, String> map=arrayProjectAttribute.get(0);
				projectNameContent.setText(Html.fromHtml("<u>"+map.get("projectName")+"</u>"));
				periodsContent.setText(Html.fromHtml("<u>"+map.get("periods")+"</u>"));
			}
			//合同信息
			if(arrayPactAttribute!=null){
				HashMap<String, String> map=arrayPactAttribute.get(0);
				pactName.setText(map.get("pactName"));
				pactNo.setText(map.get("pactNo"));
				pactPrice.setText(map.get("pactTotal"));
			}
			//本期支付
			if(arrayCurrentPeriodPay!=null){
				HashMap<String, String> map=arrayCurrentPeriodPay.get(0);
				currentPeriodPrice.setText(map.get("currentPeriodPrice"));
				currentPeriodScale.setText(map.get("currentPeriodScale"));
				write.setText(map.get("currentPeriodWrite"));
			}
			//累计支付
			if(arrayCountPay!=null){
			HashMap<String, String> map=arrayCountPay.get(0);
			countCurrentPeriodPrice.setText(map.get("countPayPrice"));
			countCurrentPeriodScale.setText(map.get("countPayScale"));
			countWrite.setText(map.get("countPayWrite"));
			}
			//付款项目
			if(mArrayListPay!=null){
				mArrayListPay.addAll(mBaseMap.get("arrayPayProject"));
			}
			//扣款项目
			if(mArrayListDeduct!=null){
				mArrayListDeduct.addAll(mBaseMap.get("arrayDeductProject"));
			}
			//更新适配器
			mAdapterPay.notifyDataSetChanged();
			mAdapterDeduct.notifyDataSetChanged(); 
			//清空数据
			arrayProjectAttribute.clear();
			arrayPactAttribute.clear();
			arrayCurrentPeriodPay.clear();
			arrayCountPay.clear();
		}
	}
	
	/**
	 * 审批数据获取
	 */
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
			parameters.put("type", "计量付款通知单");
			parameters.put("parms",str.toString().trim());
			String stream=null;
			try {
				stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_READYFORAPPROVE, mContext);
				if(stream!=null){
					mBaseMapData=new Str2Json().getPaymentCertificate(stream);
					if(mBaseMapData!=null && mBaseMapData.size()>0){
						sendMessage(Constansts.MES_TYPE_1);
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
				parameters.put("type", "计量付款通知单");
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
		if(mDialogApproveSuggestion==null){			
			mDialogApproveSuggestion=new Dialog(mContext);
			mDialogApproveSuggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialogApproveSuggestion.setCanceledOnTouchOutside(false);
			mDialogApproveSuggestion.setContentView(R.layout.measure_mid_record_approve_dialog);
		}
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
	
	/**审批数据跟新*/
	public void updateApproveData(){
		if(mBaseMap.size()>0){
			mBaseMap.clear();
		}
		if(mBaseMapData!=null){			
			mBaseMap.putAll(mBaseMapData);
			mBaseMapData.clear();
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
	
	
	@Override
		protected void handleOtherMessage(int flag) {
			switch(flag){
			case   Constansts.SUCCESS:
						updateData();
						baseCloseDialog();
						setView(View.VISIBLE);
				break;
			case   Constansts.ERRER:
				Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
				setView(View.INVISIBLE);
				baseCloseDialog();
				break;
			case Constansts.MES_TYPE_1: // 操作成功
				closeWindowDialog();
				updateApproveData();
				approve();//审批			
				break;
			case Constansts.MES_TYPE_2:  //操作成功
					Toast.makeText(mContext,"操作成功" ,Toast.LENGTH_SHORT).show();
					closeWindowDialog();
				break;
			case Constansts.MES_TYPE_3:  //操作失败
				closeWindowDialog();
				Toast.makeText(mContext, "操作失败,该条数据已经处理过", Toast.LENGTH_SHORT).show();
				break;
			case Constansts.CONNECTION_TIMEOUT: //网络失败
				closeWindowDialog();
				baseCloseDialog();
				Toast.makeText(mContext, "网络异常",Toast.LENGTH_SHORT).show();
				break;
			case Constansts.MES_TYPE_4:  //流程跟踪对话框显示
				baseCloseDialog();
			    showDialogTaskTracking();
				break;
			case Constansts.MES_TYPE_5:  //流程查看
				baseCloseDialog();
				updateTaskData();
				showDialogTaskLook();
				break;
			case Constansts.MES_TYPE_6: //更新流程信息
				break;
			default:
				baseCloseDialog();
				break;
			}
			super.handleOtherMessage(flag);
		}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnApprove:  //审批
			mPO=ProcessOperation.APPROVE;
			baseShowDialog();
			getApproveData();
			break;
		case R.id.btnBack:       //退回
			mPO=ProcessOperation.DELIVER;
			deliver();
			break;
		case R.id.btnTaskTracking:  //流程跟踪
			baseShowDialog();
			getTaskTrackingData();
			break;	
		case R.id.btnTaskLook:      //流程查看
			baseShowDialog();
			getTaskLookData();
			break;
		default:
			break;
		}
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
