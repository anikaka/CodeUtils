package com.tongyan.activity.measure.measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.MidMeasureListAdapter;
import com.tongyan.activity.adapter.MyTaskLookAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.ProcessOperation;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.tongyan.widget.view.MyDatePickerDialog;

/**
 * 
 * @Title: ContractMidMeasureRecordAct.java 
 * @author Rubert
 * @date 2014-10-21 PM 03:13:41 
 * @version V1.0 
 * @Description: 中间计量单包括 合同计量单和变更计量单
 */
public class MidMeasureRecordAct extends AbstructCommonActivity implements OnItemClickListener,OnClickListener{
	
	private _User mUser;
	private Context mContext = this;
	private String mRowId = null;
	private String mTabStatus = null;
	private ListView mListView;
	private GetDataTask getDataTask;
	private Dialog mDialog;
	private int mCurrentPageCount = 0;
	private String mSuccBack;
	private MidMeasureListAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	public static  LinkedList<Map<String,String>> mDataList = new LinkedList<Map<String,String>>();
	public static  HashMap<String, ArrayList<HashMap<String, String>>>  mBaseMap=new  HashMap<String, ArrayList<HashMap<String, String>>>();
	public static  HashMap<String, ArrayList<HashMap<String, String>>>  mBaseMapData;
	private ArrayList<HashMap<String, String>>    mArrayListTask=new ArrayList<HashMap<String,String>>(); //流程信息
	private ArrayList<HashMap<String, String>>    mArrayListTaskData;
	private Button 
	//btnAllCheck,
	//btnCancelCheck,
	btnApprove,btnBack,btnTaskTracking,btnTaskLook;
	private static final int CHECK_TURE=1; //已选择
	private static final int CHECK_FLASE=0;  //未选择
	private  Dialog mDialogApproveSuggestion,mDialogApprovePerson,mDialogDeliverSuggestion,mDialogTaskTracking,mDialogTaskLook;
	private  MyExpandableListAdapter mExpandableListApdater;
	private  ExpandableListView expandableListViewApprovePerson;
	private  RelativeLayout mRlApproveTools;
	private String  mApproveFormState="";//审批状态
	private ProcessOperation mPO=ProcessOperation.APPROVE; //默认点击的是审批
	private String mTaskTracking;
	private String mFlowId;
	private  MyTaskLookAdapter  mMyTaskLookAdapter;
	private String  mImlType;
	//计量申报状态
	public final static int    UNDECLARED=0; //未申报
	public final static int    DECLARED=1;//已申报
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_refresh_listview_layout);
		initWidget();
		TextView mTitleConent = (TextView)findViewById(R.id.titleContent);
		MyApplication mApplication = ((MyApplication)getApplication());
		mApplication.addActivity(this);
		mUser = mApplication.localUser;
		if(getIntent().getExtras() != null) {
			mTitleConent.setText(getIntent().getExtras().getString("mid_measure_type"));
			mRowId = getIntent().getExtras().getString("rowid");
			mTabStatus = getIntent().getExtras().getString("tabstatus");
		    mApproveFormState=getIntent().getExtras().getString("approveFormState");
		    mImlType=getIntent().getExtras().getString("imlType");
			if(mApproveFormState!=null){
				if("待审批".equals(mApproveFormState)){
					mRlApproveTools.setVisibility(View.VISIBLE);
				}else if("未申报".equals(mApproveFormState)){
					mRlApproveTools.setVisibility(View.GONE);
				}else{
					mRlApproveTools.setVisibility(View.VISIBLE);
//					btnAllCheck.setVisibility(View.GONE);
//					btnCancelCheck.setVisibility(View.GONE);
					btnApprove.setVisibility(View.GONE);
					btnBack.setVisibility(View.GONE);
					btnTaskTracking.setVisibility(View.VISIBLE);
					btnTaskLook.setVisibility(View.VISIBLE);
				}
			}
		}
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.common_pull_refresh_listview);
		mListView = mPullToRefreshListView.getRefreshableView();
		mAdapter = new MidMeasureListAdapter(mContext, mDataList,("未申报".equals(mApproveFormState)?UNDECLARED:DECLARED));
		mMyTaskLookAdapter=new MyTaskLookAdapter(mContext, mArrayListTask, R.layout.measure_task_dialog_tasklook_listview_item);
		mListView.setAdapter(mAdapter);
		mPullToRefreshListView.setOnRefreshListener(mFreshListener);
		mListView.setOnItemClickListener(this);
		getFirstPage();
	}
	
	/**设置复选框的状态*/
	public void setCheckBoxState(int flag){
		if(mDataList!=null && mDataList.size()>0){
			if(flag==0 || flag==1){				
				for(Map<String, String> map:mDataList){
					map.put("checkBoxState", String.valueOf(String.valueOf(flag)));
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	/** 初始化组件 */
	public void initWidget(){
//		btnAllCheck=(Button)findViewById(R.id.btnAllCheck);
//		btnCancelCheck=(Button)findViewById(R.id.btnCancelCheck);
		btnApprove=(Button)findViewById(R.id.btnApprove);
		btnBack=(Button)findViewById(R.id.btnBack);
		btnTaskTracking=(Button)findViewById(R.id.btnTaskTracking);
		btnTaskLook=(Button)findViewById(R.id.btnTaskLook);
		mRlApproveTools=(RelativeLayout)findViewById(R.id.rlApproveTools);
//		btnAllCheck.setOnClickListener(this);
//		btnCancelCheck.setOnClickListener(this);
		btnApprove.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnTaskTracking.setOnClickListener(this);
		btnTaskLook.setOnClickListener(this);
	}
	
	public void getFirstPage() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog); 
		mDialog.setCanceledOnTouchOutside(false);
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	OnRefreshListener mFreshListener = new OnRefreshListener(){
		@Override
		public void onRefresh() {
			if(getDataTask == null) {
				getDataTask = new GetDataTask(mPullToRefreshListView.getRefreshType());
			} else {
				if(!getDataTask.isCancelled())
					getDataTask.cancel(true);
			    	getDataTask = new GetDataTask(mPullToRefreshListView.getRefreshType());
			}
			getDataTask.execute();
		}
	};
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MidMeasureListAdapter.ViewHolder  viewHolder=(MidMeasureListAdapter.ViewHolder)view.getTag();
		HashMap<String, String> map=(HashMap<String, String>)viewHolder.mDataMap;
		if(map!=null){
			Intent intent=new Intent(mContext,MidMeasureRecordOptionAct.class);
			intent.putExtra("rowId", map.get("rowId"));
			startActivity(intent);
		}
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
		int pullType;
		
		public GetDataTask(){
			
		}
		public GetDataTask(int pullType) {
			this.pullType = pullType;
		}
        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
        	if(pullType == 0) {
            	if(mDataList != null) {
            		mDataList.clear();
            	}
            	mCurrentPageCount = 1;
            } 
            if(pullType == 1) {
            	if(mDataList != null) {
            		mDataList.clear();
            	}
            	mCurrentPageCount = 1;
            } 
            if(pullType == 2) {//向上
            	mCurrentPageCount ++;
            } 
			//mImlType 
//			data.append(",");
//			data.append("\'imlType:\'");
//			data.append("\'"+mImlType+"\'");
            StringBuffer   param=new StringBuffer();
            param.append("{");
            param.append("\'tabStatus\'");
            param.append(":");
			param.append("\'"+mTabStatus+"\'");
			param.append(",");
			
			param.append("\'rowId\'");
			param.append(":");
			param.append("\'"+mRowId+"\'");
			param.append(",");
			
			param.append("\'imlType\'");
			param.append(":");
			param.append("\'"+mImlType+"\'");
			param.append("}");
			try {
//				String param = "{tabStatus:'" + mTabStatus + "',rowId:'" + mRowId + \'"imlType\':"+"\'"+mImlType+"\'""'}";
				String str = WebServiceUtils.getRequestStr(mUser.getUsername(), mUser.getPassword(), String.valueOf(Constansts.PAGE_SIZE), String.valueOf(mCurrentPageCount), Constansts.TYPE_OF_MID_MEASURE, param.toString().trim(), Constansts.METHOD_OF_GETLIST, mContext);
				Map<String,Object> mR = new Str2Json().getMidMeasureList(str);
				if(mR != null) {
					mSuccBack = (String)mR.get("s");
					if("ok".equals(mSuccBack)) {
						List<Map<String, String>> newFlows = (List<Map<String, String>>)mR.get("v");
						if(newFlows == null || newFlows.size() == 0) {
							sendMessage(Constansts.NO_DATA);
						}
						return newFlows;
					} else {
						sendMessage(Constansts.ERRER);
					}
				} else {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
				}
			}  catch (Exception e) {
				sendMessage(Constansts.CONNECTION_TIMEOUT);
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
        	if(result != null && result.size() > 0) {
        		mDataList.addAll(result);
        	}
        	if(mDialog != null)
				mDialog.dismiss();
        	if( pullType == 0) {
        		mAdapter.notifyDataSetChanged();
        	} else {
        		mPullToRefreshListView.onRefreshComplete();
        	}
        }
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
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
//		case  R.id.btnAllCheck:
			 //全选	
//			setCheckBoxState(CHECK_TURE);
//			mPO=ProcessOperation.ALL_APPROVE;
//			requestApproveData(ProcessOperation.ALL_APPROVE);
//			baseShowDialog();
//			break;
//		case R.id.btnCancelCheck:
//			//取消全选
//			setCheckBoxState(CHECK_FLASE);
//			break;
		case R.id.btnApprove:
			//审批
			mPO=ProcessOperation.APPROVE;
			requestApproveData(ProcessOperation.APPROVE);
			baseShowDialog();
			break;
		case R.id.btnBack:
			//打回流程
			mPO=ProcessOperation.DELIVER;
			requestApproveData(ProcessOperation.APPROVE);
			baseShowDialog();
			break;
		case R.id.btnTaskTracking:
			 //流程跟踪
			if(isChecked()){
				baseShowDialog();
				getTaskTrackingData();
			}else{
				Toast.makeText(mContext, "您没有勾选可查的选项", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnTaskLook:
			//流程查看
			if(isChecked()){
				baseShowDialog();
				getTaskLookData();
			}else{
				Toast.makeText(mContext, "您没有勾选可查的选项", Toast.LENGTH_SHORT).show();
			}
			break;	
	
		}
	}

	/** 判断用户是否勾选了复选框*/

	public boolean isChecked(){
		for(int i=0;i<mDataList.size();i++){
			Map<String, String> map=mDataList.get(i);
			if("1".equals(map.get("checkBoxState"))){
				mFlowId=map.get("flowId");
				return true;
			}
			if(i==mDataList.size()-1 && "0".equals(map.get("checkBoxState"))){
				return false;
			}
		}
		return false;
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
	
	/** 审批流程信息获取*/
	public void requestApproveData(final ProcessOperation po){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword());
				parameters.put("type", "合同中间计量单");
//				if(po==ProcessOperation.APPROVE){					
//					for(Map<String, String> map: mDataList){
//						if("1".equals(map.get("checkBoxState"))){						
//							parameters.put("parms", "{billId:\'"+ map.get("rowId")+"\'}");
//							break;
//						}
//					}
//				}else{					
//					parameters.put("parms", "{issuseId:\'"+ mRowId+"\'}");
//				}
				if(isChecked()){
					for(Map<String, String> map: mDataList){
					if("1".equals(map.get("checkBoxState"))){						
						parameters.put("parms", "{billId:\'"+ map.get("rowId")+"\'}");
						break;
					}
				}
				}else{
					parameters.put("parms", "{issuseId:\'"+ mRowId+"\'}");
				}
				String stream=null;
				try {
					stream=WebServiceUtils.requestM(parameters, Constansts.METHOD_OF_READYFORAPPROVE, mContext);
					mBaseMapData=new Str2Json().getApprovedata(stream);
					if(mBaseMapData!=null ){
						sendMessage(Constansts.SUCCESS);
					}else{
						sendMessage(Constansts.ERRER);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendMessage(Constansts.ERRER);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/** 审批数据上传*/
	public void  uploadApproveData(final String str, final ProcessOperation po){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StringBuffer  data=new StringBuffer();
				data.append("{");//开始
				HashMap<String, String> parameters=new HashMap<String, String>();
				parameters.put("publicKey", Constansts.PUBLIC_KEY);
				parameters.put("userName", mUser.getUsername());
				parameters.put("Password", mUser.getPassword()); 
				parameters.put("type", "合同中间计量单");
//				if(mDataList.size()>0 && mPO==ProcessOperation.APPROVE){
//					data.append("bills:");
//					data.append("[");
//					for(int i=0;i<mDataList.size();i++){
//						Map<String, String> map=mDataList.get(i);
//						if("1".equals(map.get("checkBoxState"))){
//							data.append("{");
//							//id
//							data.append("\"id\"");
//							data.append(":");
//							data.append("\""+map.get("rowId")+"\"");
//							data.append(",");
//							//flowId
//							data.append("\"flow\"");
//							data.append(":");
//							data.append("\""+map.get("flowId")+"\"");
//							data.append("}");
//							if(i!=mDataList.size()-1){
//								for(int j=i;j<mDataList.size()-1;j++){
//								if("1".equals(mDataList.get(j+1).get("checkBoxState"))){									
//									data.append(",");
//									break;
//								 }
//								}
//							}
//						}
//					}
//					data.append("]");
//				}else{
//					data.append("\'issuseId\':");
//					data.append("\'"+mRowId+"\'");
//				}
				// 如果用户没有勾选，就审批全部,否则就上传勾选项
			if(isChecked()){
				data.append("bills:");
				data.append("[");
				for(int i=0;i<mDataList.size();i++){
					Map<String, String> map=mDataList.get(i);
					if("1".equals(map.get("checkBoxState"))){
						data.append("{");
						//id
						data.append("\"id\"");
						data.append(":");
						data.append("\""+map.get("rowId")+"\"");
						data.append(",");
						//flowId
						data.append("\"flow\"");
						data.append(":");
						data.append("\""+map.get("flowId")+"\"");
						data.append("}");
						if(i!=mDataList.size()-1){
							for(int j=i;j<mDataList.size()-1;j++){
							if("1".equals(mDataList.get(j+1).get("checkBoxState"))){									
								data.append(",");
								break;
							 }
							}
						}
					}
				}
				data.append("]");
			}else{
				data.append("\'issuseId\':");
				data.append("\'"+mRowId+"\'");
			}
				data.append(",");
				//command:first打回到第一步\previous打回到上一步\next正常审批
				if(po==null){
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
						}
						if(j!=mBaseMap.get("arrayListApprovePersonInfo").size()-1){
							if("1".equals(mBaseMap.get("arrayListApprovePersonInfo").get(j+1).get("checkBoxState"))){								
								data.append(",");
							}
						}
					}
					data.append("\'");
					data.append(",");
				}
				}else if(po==ProcessOperation.STEP_FIRST){
					// 打回到第一步
					data.append("\'command\'");
					data.append(":");
					data.append("\'first\'");
					data.append(",");
				}else if(po==ProcessOperation.STEP_BACk){
					//打回到上一步
					data.append("\'command\'");
					data.append(":");
					data.append("\'previous\'");
					data.append(",");
				}
				//suggest:用户输入的处理意见或打回原因
				//TODO  stream=null;  A2  许荣发
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
					Log.i("test", "result="+stream);
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
	
	
	/** 审批数据跟新*/
	public void update(){
			if(mBaseMapData!=null && mBaseMapData.size()>0){				
				mBaseMap.clear();
				mBaseMap.putAll(mBaseMapData);
				mBaseMapData.clear();
			}
	}
	
	/**审批流程 */
	public void approve(){
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
		txtDeliverOperatePerson.setText(mUser.getEmpName());
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
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch(flag){
		case Constansts.SUCCESS:
			closeWindowDialog();
			update();
			if(mPO==ProcessOperation.APPROVE || mPO==ProcessOperation.ALL_APPROVE){
				//审批流程
				approve();
			}else if(mPO==ProcessOperation.DELIVER){
				//打回流程
				deliver();
			}
			break;
		case Constansts.ERRER:
			closeWindowDialog();
			Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1:
			baseCloseDialog();
			Toast.makeText(mContext, "操作项不能为空", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_2:
			 //审批成功
			closeWindowDialog();
			//审批或者打回成功重新刷新数据
			if(!getDataTask.isCancelled()){
				getDataTask.cancel(true);
			}
			getDataTask=new GetDataTask(0);
			getDataTask.execute();
		    mPO=ProcessOperation.UPDATE;
			Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_3:
			// 审批失败关闭对话框
			closeWindowDialog();
			Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_4:  
			//流程跟踪
			baseCloseDialog();
			showDialogTaskTracking();
			break;
		case Constansts.MES_TYPE_5:
			//流程查看
			baseCloseDialog();
			updateTaskData();
			showDialogTaskLook();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			closeWindowDialog();
			baseCloseDialog();
			Toast.makeText(mContext,"网络连接超时",Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
		      Toast.makeText(mContext, "暂无最新数据", Toast.LENGTH_LONG).show();
			break;
			default:
				baseCloseDialog();
				break;
		}
		super.handleOtherMessage(flag);
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
