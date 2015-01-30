package com.tongyan.activity.oa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaDocumentAdapter;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.service.DownloadService;
import com.tongyan.service.DownloadService.ICallbackResult;
import com.tongyan.service.DownloadService.MBinder;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.FileUtils;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName P12_DocmentDetailAct 
 * @author wanghb
 * @date 2013-7-16 pm 02:30:00
 * @desc 移动OA-待办公文详情/审批详情
 */
public class OaDocmentDetailAct extends AbstructCommonActivity {
	private Context mContext = this;
	private ListView mListView;
	private TableLayout tableLayout;
	private Button homeBtn;
	private TextView mTitleContent;
	
	private _User localUser;
	private String isSucc; 
	private Dialog mDialog;
	//private HashMap<String, Object> document;
	
	private ArrayList<HashMap<String, String>> mDocumentList = new ArrayList<HashMap<String, String>>();
	private OaDocumentAdapter mAdapter;
	
	//private String mDocClass;
	private MBinder binder;
	
	private boolean isStartService = false;
	
	private int mScreenWidth;
	
	private String mIntentType = null;
	
	private HashMap<String,Object> mItemData;
	
	private Button mApprovalBtn = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
	}
	
	
	private void initPage() {
		setContentView(R.layout.oa_document_accept_send_detail);
		
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		
		mScreenWidth  = getWindowManager().getDefaultDisplay().getWidth();  
		
		mListView = (ListView)findViewById(R.id.oa_document_accept_send_list);
		tableLayout = (TableLayout)findViewById(R.id.oa_document_accept_send_tablelayout);
		homeBtn = (Button)findViewById(R.id.p12_document_detail_home_btn);
		mApprovalBtn = (Button)findViewById(R.id.oa_document_approve_btn_id);
		mApprovalBtn.setOnClickListener(mApprovalBtnListener);
		homeBtn.setOnClickListener(homeBtnListener);
		mTitleContent = (TextView)findViewById(R.id.oa_document_accept_send_title);
		mTitleContent.setText(getResources().getString(R.string.detail));
		
		if(getIntent() != null && getIntent().getExtras() != null) {
			mIntentType = getIntent().getExtras().getString("IntentType");
			mItemData = (HashMap<String, Object>)getIntent().getExtras().get("ItemData");
			String mDataType = getIntent().getExtras().getString("DateType");
			//已办和办结-审批按钮隐藏
			if("finished".equals(mDataType)) {
				mApprovalBtn.setVisibility(View.GONE);
			}
			if(Constansts.TYPE_OF_DOCUMENTGET.equals(mIntentType)) {//
				mDocumentList.addAll(getAcceptList(mItemData));
			} else {
				mDocumentList.addAll(getSendList(mItemData));
			}
		} 
		//{state=null, path=UploadFiles/2014/09/01/d2252d2651ae4e81b4d02cc4ad4b85d5.pdf, value=赣诚监字（2014）16号关于成立迎部检工作落实小组的通知, file_name=赣诚监字（2014）16号关于成立迎部检工作落实小组的通知, file_path=UploadFiles/2014/09/01/d2252d2651ae4e81b4d02cc4ad4b85d5.pdf, name=附件1}
		mAdapter = new OaDocumentAdapter(mContext, mDocumentList, R.layout.oa_document_info_item);
		mListView.setAdapter(mAdapter);
		initCurrentPage();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				OaDocumentAdapter.ViewHolder mViewHolder = (OaDocumentAdapter.ViewHolder)arg1.getTag();
				if(mViewHolder != null && mViewHolder.mItemData != null) {
					String fileName = mViewHolder.mItemData.get("path");
					
					try {
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					}catch(Exception e) {
						
					}
					if(fileName != null  && !"".equals(fileName)) {
					try {
							PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo("cn.wps.moffice_eng", 0);
							if(packageInfo == null){
								dialog();
						     } else {
						         //System.out.println("已经安装");
						    	Intent intent = new Intent();
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setAction(android.content.Intent.ACTION_VIEW);
								intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
								File file = new File(FileUtils.getSDCardPath() + Constansts.CN_NOTICE_PATH + fileName);
								if (file == null || !file.exists()) {
									Toast.makeText(mContext, "文件不存在，请先下载", Toast.LENGTH_SHORT).show();
									return;
								}
								Uri uri = Uri.fromFile(file);
								intent.setData(uri);
								startActivity(intent);
						     }
						} catch (NameNotFoundException e) {
							dialog();
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
							return;
						}
					}
				}
			}
		});
	}
	
	OnClickListener mApprovalBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(OaDocmentDetailAct.this, OaApproveDocAct.class);
			intent.putExtra("rowId", (String)mItemData.get("rowId"));
			intent.putExtra("flowId", (String)mItemData.get("flowId"));
			intent.putExtra("IntentType", mIntentType);
			startActivityForResult(intent, 666);
		}
	};
	
	public List<HashMap<String, String>> getAcceptList(HashMap<String, Object> mItemData) {
		List<HashMap<String, String>> mBackList = new ArrayList<HashMap<String, String>>();
		if(mItemData != null) {
			HashMap<String, String> map0 = new HashMap<String, String>();
			map0.put("name", (String)mItemData.get("comeDepartment"));
			map0.put("value", "来文单位/发文机关");
			map0.put("state", "0");
			
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("name", (String)mItemData.get("comeNum"));
			map1.put("value", "来文字号");
			map1.put("state", "0");
			
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("name", (String)mItemData.get("inCode"));
			map2.put("value", "收文编号/收文顺序号");
			map2.put("state", "0");
			
			HashMap<String, String> map3 = new HashMap<String, String>();
			map3.put("name", (String)mItemData.get("comeTitle"));
			map3.put("value", "来文标题");
			map3.put("state", "0");
			
			HashMap<String, String> map4 = new HashMap<String, String>();
			map4.put("name", (String)mItemData.get("todoSug"));
			map4.put("value", "拟办意见");
			map4.put("state", "0");
			
			HashMap<String, String> map5 = new HashMap<String, String>();
			map5.put("name", (String)mItemData.get("hostSug"));
			map5.put("value", "承办意见");
			map5.put("state", "0");
			
			HashMap<String, String> map6 = new HashMap<String, String>();
			map6.put("name", (String)mItemData.get("originalDate"));
			map6.put("value", "原件日期");
			map6.put("state", "0");
			
			HashMap<String, String> map7 = new HashMap<String, String>();
			map7.put("name", (String)mItemData.get("inDate"));
			map7.put("value", "收文日期");
			map7.put("state", "0");
			
			HashMap<String, String> map8 = new HashMap<String, String>();
			map8.put("name", (String)mItemData.get("docName"));
			map8.put("value", "文件名称");
			map8.put("state", "0");
			
			HashMap<String, String> map9 = new HashMap<String, String>();
			map9.put("name", (String)mItemData.get("memo"));
			map9.put("value", "备注");
			map9.put("state", "0");
			
			HashMap<String, String> map11 = new HashMap<String, String>();
			map11.put("name", (String)mItemData.get("create_person"));
			map11.put("value", "创建人");
			map11.put("state", "0");
			
			HashMap<String, String> map12 = new HashMap<String, String>();
			map12.put("name", (String)mItemData.get("type"));
			map12.put("value", "类型");
			map12.put("state", "0");
			mBackList.add(map0);
			mBackList.add(map1);
			mBackList.add(map2);
			mBackList.add(map3);
			mBackList.add(map4);
			mBackList.add(map5);
			mBackList.add(map6);
			mBackList.add(map7);
			mBackList.add(map8);
			mBackList.add(map9);
			mBackList.add(map11);
			mBackList.add(map12);
			ArrayList<HashMap<String, String>> mFileList = (ArrayList<HashMap<String, String>>)mItemData.get("FileList");
			if(mFileList != null && mFileList.size() > 0) {
				for(int i= 0; i < mFileList.size(); i ++) {
					HashMap<String, String> m = mFileList.get(i);
					if(m != null) {
						m.put("name", "附件" + (i + 1));
						m.put("value", m.get("file_name"));
						m.put("path", m.get("file_path"));
						m.put("state", "2");
						mBackList.add(m);
					}
				}
			}
			HashMap<String, String> map10 = new HashMap<String, String>();
			map10.put("name", (String)mItemData.get("detail"));
			map10.put("value", "收文内容");
			map10.put("state", "1");
			mBackList.add(map10);
		}
		return mBackList;
	}
	
	
	public List<HashMap<String, String>> getSendList(HashMap<String, Object> mItemData) {
		List<HashMap<String, String>> mBackList = new ArrayList<HashMap<String, String>>();
		if(mItemData != null) {
			HashMap<String, String> map0 = new HashMap<String, String>();
			map0.put("name", (String)mItemData.get("title"));
			map0.put("value", "标题");
			map0.put("state", "0");
			
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("name", (String)mItemData.get("classification"));
			map1.put("value", "密级");
			map1.put("state", "0");
			
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("name", (String)mItemData.get("urgency"));
			map2.put("value", "缓急");
			map2.put("state", "0");
			
			HashMap<String, String> map3 = new HashMap<String, String>();
			map3.put("name", (String)mItemData.get("typer"));
			map3.put("value", "打字");
			map3.put("state", "0");
			
			HashMap<String, String> map4 = new HashMap<String, String>();
			map4.put("name", (String)mItemData.get("revision"));
			map4.put("value", "校对");
			map4.put("state", "0");
			
			HashMap<String, String> map5 = new HashMap<String, String>();
			map5.put("name", (String)mItemData.get("copiesnum"));
			map5.put("value", "份数");
			map5.put("state", "0");
			
			HashMap<String, String> map6 = new HashMap<String, String>();
			map6.put("name", (String)mItemData.get("wordcode"));
			map6.put("value", "字号");
			map6.put("state", "0");
			
			HashMap<String, String> map7 = new HashMap<String, String>();
			map7.put("name", (String)mItemData.get("code"));
			map7.put("value", "编号");
			map7.put("state", "0");
			
			HashMap<String, String> map8 = new HashMap<String, String>();
			map8.put("name", (String)mItemData.get("sendtime"));
			map8.put("value", "印发日期");
			map8.put("state", "0");
			
			HashMap<String, String> map9 = new HashMap<String, String>();
			map9.put("name", (String)mItemData.get("booker"));
			map9.put("value", "拟稿人");
			map9.put("state", "0");
			
			HashMap<String, String> map11 = new HashMap<String, String>();
			map11.put("name", (String)mItemData.get("date"));
			map11.put("value", "拟稿日期");
			map11.put("state", "0");
			
			HashMap<String, String> map12 = new HashMap<String, String>();
			map12.put("name", (String)mItemData.get("organizers"));
			map12.put("value", "主办部门");
			map12.put("state", "0");
			
			
			HashMap<String, String> map13 = new HashMap<String, String>();
			map13.put("name", (String)mItemData.get("content"));
			map13.put("value", "事由");
			map13.put("state", "0");
			HashMap<String, String> map14 = new HashMap<String, String>();
			map14.put("name", (String)mItemData.get("department"));
			map14.put("value", "发文单位");
			map14.put("state", "0");
			HashMap<String, String> map15 = new HashMap<String, String>();
			map15.put("name", (String)mItemData.get("status"));
			map15.put("value", "流程状态");
			map15.put("state", "0");
			HashMap<String, String> map16 = new HashMap<String, String>();
			map16.put("name", (String)mItemData.get("mainsend"));
			map16.put("value", "主送");
			map16.put("state", "0");
			HashMap<String, String> map17 = new HashMap<String, String>();
			map17.put("name", (String)mItemData.get("copyreport"));
			map17.put("value", "抄报");
			map17.put("state", "0");
			
			HashMap<String, String> map18 = new HashMap<String, String>();
			map18.put("name", (String)mItemData.get("copysend"));
			map18.put("value", "抄送");
			map18.put("state", "0");
			
			HashMap<String, String> map19 = new HashMap<String, String>();
			map19.put("name", (String)mItemData.get("year"));
			map19.put("value", "年份");
			map19.put("state", "0");
			
			HashMap<String, String> map20 = new HashMap<String, String>();
			map20.put("name", (String)mItemData.get("allwordcode"));
			map20.put("value", "字号编号");
			map20.put("state", "0");
			
			HashMap<String, String> map21 = new HashMap<String, String>();
			map21.put("name", (String)mItemData.get("type"));
			map21.put("value", "类型");
			map21.put("state", "0");
			
			mBackList.add(map0);
			mBackList.add(map1);
			mBackList.add(map2);
			mBackList.add(map3);
			mBackList.add(map4);
			mBackList.add(map5);
			mBackList.add(map6);
			mBackList.add(map7);
			mBackList.add(map8);
			mBackList.add(map9);
			mBackList.add(map11);
			mBackList.add(map12);
			mBackList.add(map13);
			mBackList.add(map14);
			mBackList.add(map15);
			mBackList.add(map16);
			mBackList.add(map17);
			mBackList.add(map18);
			mBackList.add(map19);
			mBackList.add(map20);
			mBackList.add(map21);
			ArrayList<HashMap<String, String>> mFileList = (ArrayList<HashMap<String, String>>)mItemData.get("FileList");
			if(mFileList != null && mFileList.size() > 0) {
				for(int i= 0; i < mFileList.size(); i ++) {
					HashMap<String, String> m = mFileList.get(i);
					if(m != null) {
						m.put("name", "附件" + (i + 1));
						m.put("value", m.get("file_name"));
						m.put("path", m.get("file_path"));
						m.put("state", "2");
						mBackList.add(m);
					}
				}
			}
			HashMap<String, String> map10 = new HashMap<String, String>();
			map10.put("name", (String)mItemData.get("detail"));
			map10.put("value", "内容");
			map10.put("state", "1");
			mBackList.add(map10);
		}
		return mBackList;
	}
	
	
	
	
	
	public void dialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("您还没有文件查看工具，请先下载");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				getWPSapk();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MBinder) service;
			// 开始下载
			binder.addCallback(callback);
			binder.start();
		}
	};
	
	private ICallbackResult callback = new ICallbackResult() {
		@Override
		public void onBackResult(Object result) {
			if ("finish".equals(result)) {
				if(conn != null && isStartService) {
					unbindService(conn);
					isStartService = false;
				}
			} else if("error".equals(result)) {
				if(conn != null && isStartService) {
					unbindService(conn);
					isStartService = false;
				}
			}
		}
	};
	
	
	
	
	public void getWPSapk() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					String jsonStr = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETOFFICEAPK, mContext);
					isSucc = new Str2Json().getWpsApk(jsonStr);
					if(isSucc != null && !"".equals(isSucc.trim())) {
						sendMessage(Constansts.MES_TYPE_1);
					} else {
						sendMessage(Constansts.ERRER);
					}
				} catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
	 public void setListViewHeightBasedOnChildren(ListView listView) { 
	        ListAdapter listAdapter = listView.getAdapter();  
	        if (listAdapter == null) { 
	            return; 
	        } 
	        int totalHeight = 0; 
	        for (int i = 0; i < listAdapter.getCount(); i++) { 
	            View listItem = listAdapter.getView(i, null, listView); 
	            listItem.measure(0, 0); 
	            totalHeight += listItem.getMeasuredHeight(); 
	        } 
	        ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	        listView.setLayoutParams(params); 
	}
	
	public void initCurrentPage() {
		setListViewHeightBasedOnChildren(mListView);
		mAdapter.notifyDataSetChanged();
			
			ArrayList<HashMap<String,Object>> flowsList = (ArrayList<HashMap<String,Object>>)mItemData.get("FlowList");
			if(flowsList != null && flowsList.size() > 0) {
				int mTextWidth = 0;
				if(mScreenWidth != 0) {
					mTextWidth = mScreenWidth/4;
				} else {
					mTextWidth = LayoutParams.WRAP_CONTENT;
				}
				tableLayout.setStretchAllColumns(true);
				TableRow rowsTitle = new TableRow(this); 
				rowsTitle.setBackgroundDrawable(getResources().getDrawable(R.drawable.oa_contact_letter_bg));
				TextView flowName = new TextView(this);    
				flowName.setText("流程名");
				flowName.setTextColor(Color.BLACK);
				
				flowName.setPadding(5, 0, 0, 0);
				
				TextView flowType = new TextView(this);  
				flowType.setText("流程类型");
				flowType.setTextColor(Color.BLACK);
				
				TextView flowTime = new TextView(this);  
				flowTime.setText("流程开始时间");
				flowTime.setTextColor(Color.BLACK);
				
				TextView flowStatues = new TextView(this);  
				flowStatues.setText("流程状态");
				flowStatues.setTextColor(Color.BLACK);
				
				rowsTitle.addView(flowName);
				rowsTitle.addView(flowType);
				rowsTitle.addView(flowTime);
				rowsTitle.addView(flowStatues);
				tableLayout.addView(rowsTitle,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));  
				//
				for(int i = 0; i < flowsList.size(); i ++) {
					HashMap<String,Object> flow = flowsList.get(i);
					
					if(flow != null) {
						TableRow rows = new TableRow(this); 
						
						TextView sflowName = new TextView(this);  
						sflowName.setText((String)flow.get("flow_name"));
						sflowName.setTextColor(Color.BLACK);
						sflowName.setPadding(5, 0, 0, 0);
						sflowName.setWidth(mTextWidth);
						
						TextView sflowType = new TextView(this);  
						sflowType.setText((String)flow.get("flow_type"));
						sflowType.setTextColor(Color.BLACK);
						sflowType.setWidth(mTextWidth);
						
						TextView sflowTime = new TextView(this);  
						sflowTime.setText((String)flow.get("flow_stime"));
						sflowTime.setTextColor(Color.BLACK);
						sflowTime.setWidth(mTextWidth);
						
						TextView sflowStatues = new TextView(this);  
						sflowStatues.setText((String)flow.get("flow_status"));
						sflowStatues.setTextColor(Color.BLACK);
						sflowStatues.setWidth(mTextWidth);
						
						rows.addView(sflowName);
						rows.addView(sflowType);
						rows.addView(sflowTime);
						rows.addView(sflowStatues);
						tableLayout.addView(rows,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
						
						ArrayList<HashMap<String, String>> empsFlowsList = (ArrayList<HashMap<String, String>>)flow.get("EmpList");
						if(empsFlowsList != null && empsFlowsList.size() > 0) {
							for(HashMap<String, String> emps : empsFlowsList) {
								if(emps != null) {
									TableRow ssubRows = new TableRow(this); 
									TextView sempName = new TextView(this);  
									sempName.setText(emps.get("emp_name"));
									sempName.setPadding(5, 0, 0, 0);
									TextView sempDep = new TextView(this);  
									sempDep.setText(emps.get("dpt_name"));
									TextView sempTime = new TextView(this);  
									sempTime.setText(emps.get("deal_time"));
									TextView sempDic = new TextView(this);  
									sempDic.setText("null".equals(emps.get("suggestion")) ? "" : emps.get("suggestion"));
									ssubRows.addView(sempName);
									ssubRows.addView(sempDep);
									ssubRows.addView(sempTime);
									ssubRows.addView(sempDic);
									tableLayout.addView(ssubRows,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
								}
							}
						}
					}
				}
			}
		//}
	}
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaDocmentDetailAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	/*OnClickListener approveM = new OnClickListener() {
	public void onClick(View v) {
		Intent intent = new Intent(OaDocmentDetailAct.this, OaApproveDocAct.class);
		if (document != null) {
			//String row_id = (String) document.get("row_id");
			//String flow_id = (String) document.get("flow_id");
			//String step_id = (String) document.get("step_id");
			//String step_type = (String) document.get("step_type");
			//String step_next_id = (String)document.get("step_next_id");
			
			intent.putExtra("step_type", (String) document.get("step_type"));
			intent.putExtra("row_id", (String) document.get("flow_id"));
			intent.putExtra("flow_id", (String) document.get("flow_id"));
			intent.putExtra("step_id", (String) document.get("step_id"));
			intent.putExtra("step_next_id", (String)document.get("step_next_id"));
			intent.putExtra("step_next_type", (String)document.get("step_next_type"));
			intent.putExtra("flow_name", (String)document.get("flow_name"));
			intent.putExtra("mDocClass", mDocClass);
		}
		startActivityForResult(intent, 666);
	}};*/
	
	protected void onDestroy() {
		super.onDestroy();
		if(conn != null && isStartService) {
			unbindService(conn);
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(666 == resultCode) {
			setResult(666);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			if(mDialog != null)
				mDialog.dismiss();
			initCurrentPage();
			break;
		case Constansts.ERRER:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, isSucc, Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NET_ERROR:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.NO_DATA:
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "无最新数据", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT :
			if(mDialog != null)
				mDialog.dismiss();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1 :
			Intent it = new Intent(mContext, DownloadService.class);
			it.putExtra("DownloadContent", String.valueOf("WPS Office 正在下载..."));
			it.putExtra("DownloadPath", isSucc);
			it.putExtra("DownloadIcon", R.drawable.wps_icon);
			bindService(it, conn, Context.BIND_AUTO_CREATE);
			isStartService = true;
			break;
		default:
			if(mDialog != null)
				mDialog.dismiss();
			break;
		}
	}
	
}
