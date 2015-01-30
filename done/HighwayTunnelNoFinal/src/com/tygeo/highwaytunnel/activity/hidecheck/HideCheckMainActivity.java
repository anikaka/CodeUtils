package com.tygeo.highwaytunnel.activity.hidecheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.CheckProjectAdapter;
import com.tygeo.highwaytunnel.adpter.HideCheckDeviceAdapter;
import com.tygeo.highwaytunnel.adpter.HideCheckFormAdapter;
import com.tygeo.highwaytunnel.common.Constant;
import com.tygeo.highwaytunnel.common.DateTools;
import com.tygeo.highwaytunnel.common.WebServiceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �����Ų�������
 * @author ChenLang
 *
 */
public class HideCheckMainActivity extends Activity implements View.OnClickListener{
	

	private Context mContext=this;
//	private String  	  tag="HideCheckMainActivity";
	private ListView mListViewCheckProject, //��鹤��
							  mListViewCheckContent;  //�������
	private ImageButton mImgAdd;//��Ӱ�ť
	private Dialog mDialogAdd;
	private CheckProjectAdapter mCheckProjectAdapter;
	private HideCheckFormAdapter mCheckFormAdapter; //��¼��������
	private HideCheckDeviceAdapter mCheckDeviceAapter;
	private ArrayList<HashMap<String, String>> mArrayListCheckProject=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListCheckForm=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListCheckDevice=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> mArrayListTunnel=new ArrayList<HashMap<String,String>>();
	private String mTunnelName; //�������
	private String mProjectId;
	private String mProjectCode;
	private String mFormId; //��id
	private ProgressDialog mProgressDialog; 
//	private boolean isBtnForm = false;
	
	/**
	 * �����ʼ�� */
	private  void initWidget(){
		mListViewCheckProject=(ListView)findViewById(R.id.listViewCheckProject);
		mListViewCheckContent=(ListView)findViewById(R.id.listViewCheckConent);
		mImgAdd=(ImageButton)findViewById(R.id.imgAdd);
		mListViewCheckProject.setOnItemClickListener(checkProjectListener);
		mListViewCheckContent.setOnItemClickListener(checkContentListener);
		mImgAdd.setOnClickListener(this);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hidecheck_main);
		initWidget();
		mArrayListCheckProject=DB_Provider.getCheckProjectInfo();
		mArrayListCheckDevice=DB_Provider.queryCheckDevice();
		mCheckProjectAdapter=new CheckProjectAdapter(mContext, mArrayListCheckProject, R.layout.checkproject_listview_item);
		mListViewCheckProject.setAdapter(mCheckProjectAdapter);
		mCheckFormAdapter=new HideCheckFormAdapter(mContext, mArrayListCheckForm, R.layout.hidecheck_form_listview_item);
		mCheckDeviceAapter=new HideCheckDeviceAdapter(mContext, mArrayListCheckDevice, R.layout.hidecheck_device_listview_item);
		
	}
	
	/**
	 * ���ݵ��ÿһ����л�Adapter*/
	private void  changeAdapter(){
		if("GC008".equals(mProjectCode)){
			mListViewCheckContent.setAdapter(mCheckDeviceAapter);
		}else{
			mListViewCheckContent.setAdapter(mCheckFormAdapter);
		}
	}
	
	/**
	 * �����豸��Ϣ*/
	private void  updateCheckDevice(){
		if(mArrayListCheckDevice.size()>0){
			mArrayListCheckDevice.clear();
		}
		mArrayListCheckDevice.addAll(DB_Provider.queryCheckDevice());
		mCheckDeviceAapter.notifyDataSetChanged();
	}
	
	/**
	 * ���õ��״̬ 
	 * */
	private void setCheckState(int position){
		mImgAdd.setVisibility(View.VISIBLE);
		setCheckDefault();
		for(int i=0;i<mArrayListCheckProject.size();i++){
		    HashMap<String, String> map=mArrayListCheckProject.get(position);
				if(i==position){
					map.put("isCheck", "true");
			  }
		}
		mCheckProjectAdapter.notifyDataSetChanged();
	}
	
	/**
	 * ����Ĭ�ϵĵ��״̬
	 */
	private void setCheckDefault(){
		for(HashMap<String, String> map:mArrayListCheckProject){
			if("true".equals(map.get("isCheck"))){
				map.put("isCheck", "false");
			}
		}
	}
	
	
	/**
	 * �����ĿListView����¼�  */
	OnItemClickListener checkProjectListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			setCheckState(position);
			CheckProjectAdapter.ViewHolder viewHolder =(CheckProjectAdapter.ViewHolder)view.getTag();
			HashMap<String, String> map=viewHolder.map;
			if(map!=null){
				mTunnelName=map.get("name");
			}else{
				mTunnelName="";
			}
			mProjectId=map.get("id");
			mProjectCode=map.get("code");
			updateCheckForm(map.get("id"));
			changeAdapter();
		}
	};
	

	/**
	 * �������ListView����¼�  */
	OnItemClickListener  checkContentListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				if("GC008".equals(mProjectCode)){
					HideCheckDeviceAdapter.ViewHolder viewHolder=(HideCheckDeviceAdapter.ViewHolder)view.getTag();
					HashMap<String, String> map=viewHolder.map;
					mFormId=map.get("id");
					showDeviceFormDialog(map);
				}else{					
					HideCheckFormAdapter.ViewHolder  viewHolder=(HideCheckFormAdapter.ViewHolder)view.getTag();
					HashMap<String, String> map=viewHolder.map;
					mFormId=map.get("id");
					showFormDialog(map);
				}
		}
	};
	
	
	/**
	   * ��¼������dialog */
	private void showFormDialog(final HashMap<String, String> map){
		final Dialog mCheckForm=new Dialog(mContext);
		mCheckForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mCheckForm.setContentView(R.layout.hidecheck_form_dialog);
		mCheckForm.show();
		
		Button btnCheck=(Button)mCheckForm.findViewById(R.id.btnHideCheck);
		Button btnForm = (Button) mCheckForm.findViewById(R.id.btnForm);
		Button btnDel = (Button) mCheckForm.findViewById(R.id.btnDel);
		Button btnUpload=(Button)mCheckForm.findViewById(R.id.btnUpload);
		
		btnCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent  intent=new Intent(mContext, HideCheckTabHost.class);
				intent.putExtra("projectCode", mProjectCode);
				intent.putExtra("checkFormId", map.get("id"));
				intent.putExtra("startMileage", map.get("startMileage"));
				intent.putExtra("endMileage", map.get("endMileage"));
				startActivity(intent);
				if(mCheckForm!=null){
					mCheckForm.cancel();
				}
			}
		});
		
		btnForm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddDialog(map); 
				if(mCheckForm != null){
					mCheckForm.cancel();
				}
			}
		});
		
		btnDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			/*	DB_Provider.delCheckForm(map.get("projectId"));
				updateCheckForm(map.get("id"));
				if(mCheckForm != null){
					mCheckForm.cancel();
				}*/
				AlertDialog.Builder  alertDialog=new AlertDialog.Builder(mContext);
				alertDialog.setTitle("ȷ��ɾ��?");
				alertDialog.setPositiveButton("ȷ��", new  OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
						
						DB_Provider.delCheckForm(map.get("id"));
						updateCheckForm(map.get("projectId"));
						if(mCheckForm != null){
							mCheckForm.cancel();
						}
					}
				});				
				alertDialog.setNegativeButton("ȡ��", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				alertDialog.show();
			}
		});
		
		//�ϴ�
		btnUpload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder  alertDialog=new AlertDialog.Builder(mContext);
				alertDialog.setTitle("ȷ���ϴ�?");
				alertDialog.setPositiveButton("ȷ��", new  OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
//						if(DB_Provider.queryCheckFormUploadState(map.get("id"))){
//							Toast.makeText(mContext,"�������ϴ�",Toast.LENGTH_SHORT).show();
//							return;
//						}
						mProgressDialog=new ProgressDialog(mContext);
						mProgressDialog.setTitle("�ϴ���...");
						mProgressDialog.show();
						uploadCheckForm(map);
						if(mCheckForm != null){
							mCheckForm.cancel();
						}
					}
				});				
				alertDialog.setNegativeButton("ȡ��", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				alertDialog.show();
			}
		});
	}

	
	
	/**
	 * �豸��¼������dialog */
	private void showDeviceFormDialog(final HashMap<String, String> map){
		final Dialog mCheckForm=new Dialog(mContext);
		mCheckForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mCheckForm.setContentView(R.layout.hidecheck_form_dialog);
		mCheckForm.show();
		Button btnCheck=(Button)mCheckForm.findViewById(R.id.btnHideCheck);
		Button btnForm = (Button) mCheckForm.findViewById(R.id.btnForm);
		Button btnDel = (Button) mCheckForm.findViewById(R.id.btnDel);
		
		
		btnCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent  intent=new Intent(mContext, HideCheckDeviceActivity.class);
				intent.putExtra("deviceId", map.get("id")); //�豸id
				startActivity(intent);
			}
		});
		
		btnForm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddDialog(map);  
				if(mCheckForm != null){
					mCheckForm.cancel();
				}
			}
		});
		
		btnDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder   alertDialog=new AlertDialog.Builder(mContext);
				alertDialog.setTitle("�Ƿ�ɾ��");
				alertDialog.setPositiveButton("��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DB_Provider.deleCheckDevice(map.get("id"));
						updateCheckDevice();
						if(mCheckForm != null){
							mCheckForm.cancel();
						}
						
					}
				});
			 alertDialog.setNegativeButton("��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(mCheckForm != null){
						mCheckForm.cancel();
					}	
				}
			});
			}
		});
	}

	/** 
	 * ���¼�������*/
	private void  updateCheckForm(String id){
		if(mArrayListCheckForm.size()>0){
			mArrayListCheckForm.clear();
		}
		mArrayListCheckForm.addAll(DB_Provider.getCheckFormInfo(id));
		mCheckFormAdapter.notifyDataSetChanged();	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgAdd:
//			HashMap<String, String> map = new HashMap<String, String>();
//			showAddDialog(map);
			if("GC008".equals(mProjectCode)){ //���code��Ϊ�豸�ľͽ�������豸dialog,otherwise ������Ӧ��dialog
//				showAddDeviceDialog();
				Toast.makeText(mContext, "��δ��ͨ", Toast.LENGTH_SHORT).show();
			}else{
				HashMap<String, String> map = new HashMap<String, String>();
				showAddDialog(map); 
			}
			break;

		default:
			break;
		}
		
	}
	
  /**
   * ��ʾ��Ӽ�¼��dialog */
	@SuppressLint("NewApi")
//	public void showAddDialog(final HashMap<String, String> resource){
//		mDialogAdd=new Dialog(mContext);
//		mDialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mDialogAdd.setContentView(R.layout.hidecheck_add_dialog);
//		mDialogAdd.show();
//		TextView  dialogTitle=(TextView)mDialogAdd.findViewById(R.id.checkContentDialogTitle);
//		final EditText  txtCheckProject=(EditText)mDialogAdd.findViewById(R.id.txtCheckProject);     //�����Ŀ
//		final EditText  txtTunnel=(EditText)mDialogAdd.findViewById(R.id.txtTunnelName); 				  //�������
//		final EditText  txtManagerUnit=(EditText)mDialogAdd.findViewById(R.id.txtManagerUnit);     // ������Ӫ��λ
//		final EditText  txtMileage=(EditText)mDialogAdd.findViewById(R.id.txtMileage); 					  //��ʼ׮��
//		final EditText  startMileageEnd = (EditText) mDialogAdd.findViewById(R.id.start_Mileage_end);
//		final EditText  endMileageHead = (EditText) mDialogAdd.findViewById(R.id.end_mileage_head);
//		final EditText  endMileageEnd = (EditText) mDialogAdd.findViewById(R.id.end_mileage_end);
//		final EditText  txtCheckPerson=(EditText)mDialogAdd.findViewById(R.id.txtCheckPerson);      //�����Ա
//		final EditText  txtCheckDate=(EditText)mDialogAdd.findViewById(R.id.txtCheckDate);			  //�������
//		 Button    btnConfirm=(Button)mDialogAdd.findViewById(R.id.btnConfirm);
//		 Button    btnCancel=(Button)mDialogAdd.findViewById(R.id.btnCancel);
//		 if(resource != null){
//			 if("1".equals(resource.get("uploadState"))){//���ϴ�
//				//��ʼ��
//				 dialogTitle.setText(mTunnelName);
//				 txtCheckProject.setText(resource.get("projectName"));
//				 txtTunnel.setText(resource.get("tunnelName"));
//				 txtManagerUnit.setText(resource.get("startUnit"));
//				 if(resource.get("startMileage") != null){
//					 txtMileage.setText(resource.get("startMileage").split("\\+")[0]);
//				 	 startMileageEnd.setText(resource.get("startMileage").split("\\+")[1]);
//				 }
//				 if(resource.get("endMileage") != null){
//					 endMileageHead.setText(resource.get("endMileage").split("\\+")[0]);
//					 endMileageEnd.setText(resource.get("endMileage").split("\\+")[1]);
//				 }
//				 txtCheckPerson.setText(resource.get("checkPerson"));
//				 txtCheckDate.setText(resource.get("date"));
//				 
//				 btnConfirm.setVisibility(View.INVISIBLE);
//				 btnCancel.setVisibility(View.INVISIBLE);
//				 
//				 txtCheckProject.setFocusable(false);
//				 txtTunnel.setFocusable(false);
//				 txtManagerUnit.setFocusable(false);
//				 txtMileage.setFocusable(false);
//				 startMileageEnd.setFocusable(false);
//				 endMileageHead.setFocusable(false);
//				 endMileageEnd.setFocusable(false);
//				 txtCheckPerson.setFocusable(false);
//				 txtCheckProject.setFocusable(false);
//			 }else{//δ�ϴ�
//				//��ʼ��
//				 dialogTitle.setText(mTunnelName);
//				 if(resource.size() > 0){//�������Ѵ���
//					 txtCheckProject.setText(resource.get("projectName"));
//					 txtTunnel.setText(resource.get("tunnelName"));
//					 txtManagerUnit.setText(resource.get("startUnit"));
//					 if(resource.get("startMileage") != null){
//						 txtMileage.setText(resource.get("startMileage").split("\\+")[0]);
//					 	 startMileageEnd.setText(resource.get("startMileage").split("\\+")[1]);
//					 }
//					 if(resource.get("endMileage") != null){
//						 endMileageHead.setText(resource.get("endMileage").split("\\+")[0]);
//						 endMileageEnd.setText(resource.get("endMileage").split("\\+")[1]);
//					 }
//					 txtCheckPerson.setText(resource.get("checkPerson"));
//					 txtCheckDate.setText(resource.get("date"));
//					 
//					 txtCheckProject.setInputType(InputType.TYPE_NULL);//�״ν��������
//					 txtCheckProject.setCursorVisible(false);//ȥ���
//					 txtTunnel.setInputType(InputType.TYPE_NULL);
//					 txtTunnel.setCursorVisible(false);
//					 txtManagerUnit.setInputType(InputType.TYPE_NULL);
//					 txtManagerUnit.setCursorVisible(false);
//					 txtMileage.setInputType(InputType.TYPE_NULL);
//					 txtMileage.setCursorVisible(false);
//					 startMileageEnd.setInputType(InputType.TYPE_NULL);
//					 startMileageEnd.setCursorVisible(false);
//					 endMileageHead.setInputType(InputType.TYPE_NULL);
//					 endMileageHead.setCursorVisible(false);
//					 endMileageEnd.setInputType(InputType.TYPE_NULL);
//					 endMileageEnd.setCursorVisible(false);
//					 txtCheckPerson.setInputType(InputType.TYPE_NULL);
//					 txtCheckPerson.setCursorVisible(false);
//					 txtCheckDate.setInputType(InputType.TYPE_NULL);
//					 txtCheckDate.setCursorVisible(false);
//				 }else{//���񲻴���
//					 txtCheckDate.setText(DateTools.getDate());
//				 }
//				 
//				 //���������
//				 txtCheckProject.setOnTouchListener(new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						txtCheckProject.setInputType(InputType.TYPE_CLASS_TEXT);
//						txtCheckProject.setCursorVisible(true);
//						return false;
//					}
//				});
//				 
//				 txtTunnel.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 txtTunnel.setInputType(InputType.TYPE_CLASS_TEXT);
//						 txtTunnel.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 txtManagerUnit.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 txtManagerUnit.setInputType(InputType.TYPE_CLASS_TEXT);
//						 txtManagerUnit.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 txtCheckPerson.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 txtCheckPerson.setInputType(InputType.TYPE_CLASS_TEXT);
//						 txtCheckPerson.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 txtCheckDate.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 txtCheckDate.setInputType(InputType.TYPE_CLASS_TEXT);
//						 txtCheckDate.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 //�������ּ���
//				 txtMileage.setOnTouchListener(new OnTouchListener() {
//					
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						txtMileage.setInputType(InputType.TYPE_CLASS_NUMBER);
//						txtMileage.setCursorVisible(true);
//						return false;
//					}
//				});
//				 startMileageEnd.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 startMileageEnd.setInputType(InputType.TYPE_CLASS_NUMBER);
//						 startMileageEnd.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 endMileageHead.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 endMileageHead.setInputType(InputType.TYPE_CLASS_NUMBER);
//						 endMileageHead.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 endMileageEnd.setOnTouchListener(new OnTouchListener() {
//					 
//					 @Override
//					 public boolean onTouch(View v, MotionEvent event) {
//						 endMileageEnd.setInputType(InputType.TYPE_CLASS_NUMBER);
//						 endMileageEnd.setCursorVisible(true);
//						 return false;
//					 }
//				 });
//				 //�ж����ָ�ʽ�����Ƿ���ȷ
//				 txtCheckPerson.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//					
//					@Override
//					public void onFocusChange(View v, boolean hasFocus) {
//						 if(hasFocus){
//								Pattern pattern=Pattern.compile("^[0-9]{1,3}");
//								if(!"".equals(txtMileage.getText().toString()) && !"null".equals(txtMileage.getText().toString())){
//									Matcher m=pattern.matcher(txtMileage.getText().toString());
//									if(m.matches()==false){
//										Toast.makeText(mContext, "��ʼ׮�����벻��ȷ,����������", Toast.LENGTH_SHORT).show();
//										txtMileage.setText("");
//										txtCheckPerson.setCursorVisible(false);
//										txtMileage.requestFocus();
//									}else{
//										txtCheckPerson.setCursorVisible(true);
//									}
//								}
//								if(!"".equals(startMileageEnd.getText().toString()) && !"null".equals(startMileageEnd.getText().toString())){
//									Matcher m=pattern.matcher(startMileageEnd.getText().toString());
//									if(m.matches()==false){
//										Toast.makeText(mContext, "��ʼ׮�����벻��ȷ,����������", Toast.LENGTH_SHORT).show();
//										startMileageEnd.setText("");
//										txtCheckPerson.setCursorVisible(false);
//										startMileageEnd.requestFocus();
//									}else{
//										txtCheckPerson.setCursorVisible(true);
//									}
//								}
//								if(!"".equals(endMileageHead.getText().toString()) && !"null".equals(endMileageHead.getText().toString())){
//									Matcher m=pattern.matcher(endMileageHead.getText().toString());
//									if(m.matches()==false){
//										Toast.makeText(mContext, "����׮�����벻��ȷ,����������", Toast.LENGTH_SHORT).show();
//										endMileageHead.setText("");
//										txtCheckPerson.setCursorVisible(false);
//										endMileageHead.requestFocus();
//									}else{
//										txtCheckPerson.setCursorVisible(true);
//									}
//								}
//								if(!"".equals(endMileageEnd.getText().toString()) && !"null".equals(endMileageEnd.getText().toString())){
//									Matcher m=pattern.matcher(endMileageEnd.getText().toString());
//									if(m.matches()==false){
//										Toast.makeText(mContext, "����׮�����벻��ȷ,����������", Toast.LENGTH_SHORT).show();
//										endMileageEnd.setText("");
//										txtCheckPerson.setCursorVisible(false);
//										endMileageEnd.requestFocus();
//									}else{
//										txtCheckPerson.setCursorVisible(true);
//									}
//								}
//						 }
//					}
//				});
//
//				 
//				 txtCheckDate.setOnClickListener(new View.OnClickListener() {
//					
//					@SuppressLint("NewApi")
//					@Override
//					public void onClick(View v) {
//						WindowManager manager = getWindowManager();
//						Display display = manager.getDefaultDisplay();
//						int width = display.getWidth();
//						int height = display.getHeight();
//						LayoutInflater inflater = LayoutInflater.from(mContext);
//						final View view = inflater.inflate(R.layout.canlender, null);
//						AlertDialog	dia = new AlertDialog.Builder(mContext).setTitle("ѡ������")
//								.setPositiveButton("ȷ��", new OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,int which) {
//										dialog.dismiss();
//									}
//								}).setNegativeButton("ȡ��", new OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.dismiss();
//									}
//								}).setView(view).create();
//						dia.show();
//						final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendarView);
//						calendar.setOnDateChangeListener(new OnDateChangeListener() {
//							
//							@Override
//							public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
//								txtCheckDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
//
//							}
//						});
//						
//					}
//				});
//				 
//				 //��ɰ�ť
//				 btnConfirm.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						//��ʼ׮��
//						if("".equals(txtMileage.getText().toString()) || "null".equals(txtMileage.getText().toString())){
//							if("".equals(startMileageEnd.getText().toString()) || "null".equals(startMileageEnd.getText().toString())){
//								Toast.makeText(mContext, "��ʼ׮�ű�����д", Toast.LENGTH_SHORT).show();
//								return;
//							}else{
//								Toast.makeText(mContext, "��ʼ׮�ű�����д����", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}else{
//							if("".equals(startMileageEnd.getText().toString()) || "null".equals(startMileageEnd.getText().toString())){
//								Toast.makeText(mContext, "��ʼ׮�ű�����д����", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}
//						//����׮��
//						if("".equals(endMileageHead.getText().toString()) || "null".equals(endMileageHead.getText().toString())){
//							if("".equals(endMileageEnd.getText().toString()) || "null".equals(endMileageEnd.getText().toString())){
//								Toast.makeText(mContext, "����׮�ű�����д", Toast.LENGTH_SHORT).show();
//								return;
//							}else{
//								Toast.makeText(mContext, "����׮�ű�����д����", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}else{
//							if("".equals(endMileageEnd.getText().toString()) || "null".equals(endMileageEnd.getText().toString())){
//								Toast.makeText(mContext, "����׮�ű�����д����", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}
//						
//						if("".equals(txtCheckPerson.getText().toString()) || "null".equals(txtCheckPerson.getText().toString())){
//							Toast.makeText(mContext, "�����Ա������д", Toast.LENGTH_SHORT).show();
//							return;
//						}
//						
//						if(DB_Provider.queryTunnelIsExist(mProjectId,txtMileage.getText().toString())){
//							Toast.makeText(mContext, "�Ѿ�������ͬ��׮��", Toast.LENGTH_SHORT).show();
//						}else{
//							HashMap<String, String> map = new HashMap<String, String>();
//							map.put("projectId", mProjectId);
//							map.put("projectName", txtCheckProject.getText().toString());
//							map.put("tunnelName", txtTunnel.getText().toString());
//							map.put("startUnit", txtManagerUnit.getText().toString());
//							map.put("startMileage", txtMileage.getText().toString()+"+"+startMileageEnd.getText().toString());
//							map.put("endMileage", endMileageHead.getText()+"+"+endMileageEnd.getText().toString());
//							map.put("checkPerson", txtCheckPerson.getText().toString());
//							map.put("date", txtCheckDate.getText().toString());
//							if("0".equals(resource.get("uploadState")) && resource.size() > 0){
//								DB_Provider.updateCheckForm(map);
//								updateCheckForm(resource.get("id"));
//							}else{
//								DB_Provider.insertCheckForm(map);
//							}
//							updateCheckForm(mProjectId);
//							mDialogAdd.cancel();
//						}
//					}
//				});
//				 
//				 //ȡ����ť
//				 btnCancel.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						txtCheckProject.setText("");
//						txtTunnel.setText("");
//						txtManagerUnit.setText("");
//						txtMileage.setText("");
//						txtCheckPerson.setText("");
//						txtCheckDate.setText("");
//						mDialogAdd.cancel();
//					}
//				}); 
//			 }
//		 }
//	}
	
/**
 *��ȡ������·��Ϣ*/
	private ArrayList<HashMap<String, String>> getLine(){
		 ArrayList<HashMap<String, String>> arrayListLine=new ArrayList<HashMap<String,String>>();
		 if(arrayListLine.size()<=0){
			 arrayListLine.addAll(DB_Provider.queryLine());
		 }
		 return arrayListLine;
	}
	
	/**
	 * ������·codeLine ��ȡposition*/
	private int  getCodeLinePosition(String lineCode){
		for(int i=0;i<getLine().size();i++){
			HashMap<String, String> map=getLine().get(i);
			if(map.get("lineCode").equals(lineCode)){
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * ����tunnelCode��ȡ��·��position*/
	private int getTunnelLinePosition(String tunnelCode){
		for(int i=0; i<mArrayListTunnel.size();i++){
			HashMap<String, String> map=mArrayListTunnel.get(i);
			if(map.get("tunnelCode").equals(tunnelCode)){
				return i;
			}
		}
		return 0;
	}
	
/**
 * 
 * ������·��ȡ�����Ϣ*/
	private void getTunnelInfo(String lineCode){
		if(mArrayListTunnel.size()>0){
			mArrayListTunnel.clear();
		}
		mArrayListTunnel.addAll(DB_Provider.queryTunnelInfo(lineCode));
	}
	

/**
 * ��֤��λһ��,������λ��̸�ʽ 
 * @retrue true  ��ȷ*/
	private boolean  isAppropriateMileage(String mileage){
		Pattern  pattern=Pattern.compile("\\d{1,3}");
		Matcher m=pattern.matcher(mileage);
		if(m.matches()){
			return true;
		}
		return false;
	}
	
	/**
	 * ��֤ǧλ����,����ǧλ��̸�ʽ 
	 * @retrue true  ��ȷ*/
	private boolean  isAppropriateThousandMileage(String mileage){
		Pattern  pattern=Pattern.compile("K{0,1}\\d+");
		Matcher m=pattern.matcher(mileage);
		if(m.matches()){
			return true;
		}
		return false;
	}
	
/**
 * 
 * @param resource
 */
    private int     mLinePosition=0;  //��·position
    private int     mTunnelPosition=-1; //���posiiton
    private ArrayList<HashMap<String, String>> mArrayListPic=new ArrayList<HashMap<String,String>>();
	public void showAddDialog(final HashMap<String, String> resource){
		mDialogAdd=new Dialog(mContext);
		mDialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogAdd.setContentView(R.layout.hidecheck_add_dialog);
		mDialogAdd.show();
		TextView  dialogTitle=(TextView)mDialogAdd.findViewById(R.id.checkContentDialogTitle);
		final Spinner  spinnerCheckProject=(Spinner)mDialogAdd.findViewById(R.id.spinnerCheckProject);     //�����Ŀ
		final Spinner  spinnertTunnel=(Spinner)mDialogAdd.findViewById(R.id.spinnerTunnelName); 			 //�������
		final EditText  txtSection=(EditText)mDialogAdd.findViewById(R.id.txtSection);									 //ʩ�����
		final EditText  txtManagerUnit=(EditText)mDialogAdd.findViewById(R.id.txtManagerUnit);     // ������Ӫ��λ
		final EditText  txtMileageHead=(EditText)mDialogAdd.findViewById(R.id.txtMileageHead); 					  //��ʼ׮��
		final EditText  startMileageEnd = (EditText) mDialogAdd.findViewById(R.id.start_Mileage_end);
		final EditText  endMileageHead = (EditText) mDialogAdd.findViewById(R.id.end_mileage_head);
		final EditText  endMileageEnd = (EditText) mDialogAdd.findViewById(R.id.end_mileage_end);
		final EditText  txtCheckPerson=(EditText)mDialogAdd.findViewById(R.id.txtCheckPerson);      //�����Ա
		final EditText  txtCheckDate=(EditText)mDialogAdd.findViewById(R.id.txtCheckDate);			  //�������
		Button    btnConfirm=(Button)mDialogAdd.findViewById(R.id.btnConfirm);
	    Button    btnCancel=(Button)mDialogAdd.findViewById(R.id.btnCancel);
	  
		 //��ʼ��
	    dialogTitle.setText(mTunnelName);
	    txtCheckDate.setText(DateTools.getDate());
	    SimpleAdapter  lineSimpleApdater=new SimpleAdapter(mContext, getLine(), R.layout.common_spinner_item, 
				 new String[]{"lineName"}, new int[]{R.id.spinnerContent});
		 spinnerCheckProject.setAdapter(lineSimpleApdater);
		  if("1".equals(resource.get("uploadState")) && resource.size()>0){
		    	spinnerCheckProject.setSelection(getCodeLinePosition(resource.get("lineCode")));
		    	getTunnelInfo(resource.get("lineCode"));
		    	spinnertTunnel.setSelection(getTunnelLinePosition(resource.get("tunnelCode")));
		    	txtSection.setText(resource.get("section"));
		    	txtManagerUnit.setText(resource.get("startUnit"));
		    	txtMileageHead.setText(resource.get("startMileage").split("\\+")[0]);
		    	startMileageEnd.setText(resource.get("startMileage").split("\\+")[1]);
		    	endMileageHead.setText(resource.get("endMileage").split("\\+")[0]);
		    	endMileageEnd.setText(resource.get("endMileage").split("\\+")[1]);
		    	txtCheckPerson.setText(resource.get("checkPerson"));
		    	txtCheckDate.setText(resource.get("date"));
		    	startMileageEnd.setEnabled(false);
		    	endMileageEnd.setEnabled(false);
		    	txtManagerUnit.setEnabled(false);
		    	spinnerCheckProject.setSelected(false);
		    	spinnerCheckProject.setEnabled(false);
		    	spinnertTunnel.setSelected(false);
		    	spinnertTunnel.setEnabled(false);
		    	txtCheckPerson.setFocusable(false);
		    	txtCheckPerson.setEnabled(false);
		    	txtCheckDate.setFocusable(false);
		    	txtCheckDate.setEnabled(false);
		    	txtSection.setFocusable(false);
		    	txtSection.setEnabled(false);
		    	txtCheckPerson.setFocusable(false);
		    	txtMileageHead.setEnabled(false);
		    	endMileageHead.setEnabled(false);
		    	
		    }
		    if(resource.size()>0 && "0".equals(resource.get("uploadState"))){
		    	
		    	spinnerCheckProject.setSelection(getCodeLinePosition(resource.get("lineCode")));
		    	getTunnelInfo(resource.get("lineCode"));
		    	spinnertTunnel.setSelection(getTunnelLinePosition(resource.get("tunnelCode")));
		    	txtSection.setText(resource.get("section"));
		    	txtManagerUnit.setText(resource.get("startUnit"));
		    	txtMileageHead.setText(resource.get("startMileage").split("\\+")[0]);
		    	startMileageEnd.setText(resource.get("startMileage").split("\\+")[1]);
		    	endMileageHead.setText(resource.get("endMileage").split("\\+")[0]);
		    	endMileageEnd.setText(resource.get("endMileage").split("\\+")[1]);
		    	txtCheckPerson.setText(resource.get("checkPerson"));
		    	txtCheckDate.setText(resource.get("date"));
		    }

		 spinnerCheckProject.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mLinePosition=position;
			 getTunnelInfo(getLine().get(position).get("lineCode"));
			 SimpleAdapter  tunnelSimpleApdater=new SimpleAdapter(mContext,mArrayListTunnel, R.layout.common_spinner_item, 
					 new String[]{"tunnelName"}, new int[]{R.id.spinnerContent});
			 spinnertTunnel.setAdapter(tunnelSimpleApdater);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
		
				
			}
		});
		 
		 spinnertTunnel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(mTunnelPosition!=-1){					
					txtMileageHead.setText(mArrayListTunnel.get(position).get("startMileage").split("\\+")[0]);
					startMileageEnd.setText(mArrayListTunnel.get(position).get("startMileage").split("\\+")[1]);
					endMileageHead.setText(mArrayListTunnel.get(position).get("endMileage").split("\\+")[0]);
					endMileageEnd.setText(mArrayListTunnel.get(position).get("endMileage").split("\\+")[1]);
				}
				mTunnelPosition=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		 
		 
		 txtCheckDate.setOnClickListener(new View.OnClickListener() {
			
		
			@Override
			public void onClick(View v) {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				int width = display.getWidth();
				int height = display.getHeight();
				LayoutInflater inflater = LayoutInflater.from(mContext);
				final View view = inflater.inflate(R.layout.canlender, null);
				AlertDialog	dia = new AlertDialog.Builder(mContext).setTitle("ѡ������")
						.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
							}
						}).setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setView(view).create();
				dia.show();
				final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendarView);
				calendar.setOnDateChangeListener(new OnDateChangeListener() {
					
					@Override
					public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
						txtCheckDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
					}
				});
				
			}
		});
		
		 
		 btnConfirm.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if(mArrayListTunnel.size()<=0){
				Toast.makeText(mContext, "û�п��õļ�����", Toast.LENGTH_SHORT).show();
				return;
			}
			if("".equals(spinnertTunnel.getSelectedItem().toString()) || "null".equals(spinnertTunnel.getSelectedItem().toString())){
				Toast.makeText(mContext,"û�пɼ������", Toast.LENGTH_SHORT).show();
				return;
			}
			if("".equals(startMileageEnd.getText().toString()) || "".equals(endMileageEnd.getText().toString()) ||
					"".equals(endMileageHead.getText().toString()) || "".equals(endMileageEnd.getText().toString())){
				Toast.makeText(mContext,"������벻��Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!isAppropriateThousandMileage(txtMileageHead.getText().toString()) && !isAppropriateThousandMileage(endMileageHead.getText().toString())){
				Toast.makeText(mContext,"��̸�ʽ���벻��ȷ", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!isAppropriateMileage(startMileageEnd.getText().toString()) && !isAppropriateMileage(startMileageEnd.getText().toString())){
				Toast.makeText(mContext,"��̸�ʽ���벻��ȷ", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if("".equals(txtManagerUnit.getText().toString()) || "null".equals(txtManagerUnit.getText().toString())){
				Toast.makeText(mContext, "����λ������д", Toast.LENGTH_SHORT).show();
				return;
			}
			if("".equals(txtCheckPerson.getText().toString()) || "null".equals(txtCheckPerson.getText().toString())){
				Toast.makeText(mContext, "�����Ա������д", Toast.LENGTH_SHORT).show();
				return;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id",resource.get("id"));
			map.put("projectId", mProjectId);
			map.put("lineCode", getLine().get(mLinePosition).get("lineCode"));
			map.put("tunnelCode", mArrayListTunnel.get(mTunnelPosition).get("tunnelCode"));
			map.put("projectName",getLine().get(mLinePosition).get("lineName"));
			map.put("tunnelName", mArrayListTunnel.get(mTunnelPosition).get("tunnelName"));
			map.put("section", txtSection.getText().toString());
			map.put("startUnit", txtManagerUnit.getText().toString());
			map.put("startMileage", txtMileageHead.getText().toString()+"+"+startMileageEnd.getText().toString());
			map.put("endMileage", endMileageHead.getText()+"+"+endMileageEnd.getText().toString());
			map.put("checkPerson", txtCheckPerson.getText().toString());
			map.put("date", txtCheckDate.getText().toString());
			if("0".equals(resource.get("uploadState")) && resource.size() > 0){
			DB_Provider.updateCheckForm(map);
			updateCheckForm(resource.get("id"));
			}
			if(!"1".equals(resource.get("upload"))){				
				if(resource.size()<=0){
					DB_Provider.insertCheckForm(map);
				}
				updateCheckForm(mProjectId);
			}
			mDialogAdd.cancel();
			mTunnelPosition=-1;
			}
		});
		 
		 //ȡ����ť
		 btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				txtCheckPerson.setText("");
				txtCheckDate.setText("");
				mDialogAdd.cancel();
				mTunnelPosition=-1;
			}
		}); 
	}
	
	
	/**
	 * ��ʾ����豸dialog*/
	public void showAddDeviceDialog(){
		final Dialog dialogAddDevice=new Dialog(mContext);
		dialogAddDevice.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogAddDevice.setContentView(R.layout.hidecheck_add_device_dialog);
		dialogAddDevice.show();
		TextView  txtAddDeviceTitle=(TextView)dialogAddDevice.findViewById(R.id.addDeviceDialogTitle);
		final EditText txtCheckProject=(EditText)dialogAddDevice.findViewById(R.id.txtAddDeviceCheckProject);
	    final EditText txtSection=(EditText)dialogAddDevice.findViewById(R.id.txtAddDeviceSection);
	    final EditText txtUnit=(EditText)dialogAddDevice.findViewById(R.id.txtAddDeviceUnit);
	    final EditText txtPerson=(EditText)dialogAddDevice.findViewById(R.id.txtAddDeviceCheckPerson);
	    final EditText txtCheckDate=(EditText)dialogAddDevice.findViewById(R.id.txtAddDeviceCheckDate);
	    final Button   btnConfirm=(Button)dialogAddDevice.findViewById(R.id.btnAddDeviceConfirm);
	    final Button  btnCancel=(Button)dialogAddDevice.findViewById(R.id.btnAddDeviceCancel);
	    txtAddDeviceTitle.setText(mTunnelName);
	    txtCheckDate.setText(DateTools.getDate());

	    //�������
	    txtCheckDate.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				int width = display.getWidth();
				int height = display.getHeight();
				LayoutInflater inflater = LayoutInflater.from(mContext);
				final View view = inflater.inflate(R.layout.canlender, null);
				AlertDialog	dia = new AlertDialog.Builder(mContext).setTitle("ѡ������")
						.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
							}
							
							
						}).setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setView(view).create();
				dia.show();
				final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendarView);
				calendar.setOnDateChangeListener(new OnDateChangeListener() {
					
					@Override
					public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
						txtCheckDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);

					}
				});
				
			}
		});
	    
	    //ȷ�ϰ�ť
	    btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("".equals(txtCheckProject.getText().toString()) || "null".equals(txtCheckProject.getText().toString())){
					Toast.makeText(mContext, "��Ϣδ��д����", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(txtSection.getText().toString()) || "null".equals(txtSection.getText().toString())){
					Toast.makeText(mContext, "��Ϣδ��д����", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(txtUnit.getText().toString()) || "null".equals(txtUnit.getText().toString())){
					Toast.makeText(mContext, "��Ϣδ��д����", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(txtPerson.getText().toString()) || "null".equals(txtPerson.getText().toString())){
					Toast.makeText(mContext, "��Ϣδ��д����", Toast.LENGTH_SHORT).show();
					return;
				}
				DB_Provider.insertCheckDevice(txtCheckProject.getText().toString(), txtSection.getText().toString(), txtUnit.getText().toString(),
						txtPerson.getText().toString(), txtCheckDate.getText().toString());
				  updateCheckDevice();
				  dialogAddDevice.cancel();
					mTunnelPosition=-1;
			}
		});
	    
	    
	    //ȡ����ť
	    
	    btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTunnelPosition=-1;
				dialogAddDevice.cancel();
			}
		});
	    
	}
	



	/**
	  �ϴ���������Ų��¼����Ϣ*/
	public void  uploadCheckForm(final HashMap<String, String> map){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(mArrayListPic.size()>0){
					mArrayListPic.clear();
				}
				StringBuffer  str=new StringBuffer();
				str.append("{");
				//��·Code
				str.append("\"LineCode\"");
				str.append(":");
				str.append("\""+map.get("lineCode")+"\"");//
				str.append(",");
				//��·����
				str.append("\"LineName\"");
				str.append(":");
				str.append("\""+DB_Provider.queryLineName(map.get("lineCode"))+"\"");
				str.append(",");
				//���Code
				str.append("\"ObjectCode\"");
				str.append(":");
				str.append("\""+map.get("tunnelCode")+"\"");
				str.append(",");
				//�������
				str.append("\"ObjectName\"");
				str.append(":");
				str.append("\""+DB_Provider.queryTunnelName(map.get("tunnelCode"))+"\"");
				str.append(",");
				//��ʼ׮��
				str.append("\"SEndStake\"");
				str.append(":");
				str.append("\""+map.get("startMileage")+"/"+map.get("endMileage")+"\"");
				str.append(",");
				//��鵥λ
				str.append("\"CheckUnit\"");
				str.append(":");
				str.append("\""+map.get("startUnit")+"\"");
				str.append(",");
				//ʩ�����//
				str.append("\"ConstructionTender\""); 
				str.append(":");
				str.append("\""+map.get("section")+"\""); 
				str.append(",");
				//�����
				str.append("\"CheckPerson\"");
				str.append(":");
				str.append("\""+map.get("checkPerson")+"\"");
//				str.append("}");
				str.append(",");
				//��ʼ����
				str.append("\"BeginDate\"");
				str.append(":");
				str.append("\""+map.get("date")+"\"");
				str.append(",");
				//��������
				str.append("\"EndDate\"");
				str.append(":");
				str.append("\"\"");
				str.append(",");
				//
				str.append("\"BaseHDDataDto\"");
				str.append(":");
				str.append("["); //��ʼ
				ArrayList<HashMap<String, String>>  arrayListCheckRecord=DB_Provider.queryTableCheckRecord(map.get("id"));
				for(int i=0;i<arrayListCheckRecord.size();i++){
					HashMap<String, String> map=arrayListCheckRecord.get(i);
					str.append("{");
					//���̱���
					str.append("\"ProjectCode\"");
					str.append(":");
					str.append("\""+mProjectCode+"\"");
					str.append(",");
					//��Ŀ����
					str.append("\"ItemCode\"");
					str.append(":");
					str.append("\""+map.get("itemCode")+"\"");
					str.append(",");
					//������ݱ���
					str.append("\"ContentCode\"");
					str.append(":");
					str.append("\""+map.get("contentCode")+"\"");
					str.append(",");
					//���
					str.append("\"Stake\"");
					str.append(":");
					str.append("\""+map.get("mileage")+"\"");
					str.append(",");
					//������Ϣ
					str.append("\"Describe\"");
					str.append(":");
					str.append("\""+map.get("describe")+"\"");
					str.append(",");
					//��ƬGUID
//					str.append("\"Describe\"");
					str.append("\"DisPid\"");
					str.append(":");
					ArrayList<HashMap<String, String>> arrayListPic=DB_Provider.queryTableHideCheckPic(map.get("id"));

//					mArrayListPic.addAll(DB_Provider.queryTableHideCheckPic(map.get("id")));
					
					//    [{"PicName":"21efe3d1-cb26-46e7-93ea-84e0e3e276ae.jpg","FileSource":4},
					//	   {"PicName":"85135f78-d17d-4bb2-b543-2f0b890fcfc3.png","FileSource":4"}]
					if(arrayListPic.size()>0){
						str.append("\"");
						str.append("[");
						for(int j=0;j<arrayListPic.size();j++){
							HashMap<String, String> mapPic=arrayListPic.get(j);
							mArrayListPic.add(mapPic);
//							str.append(mapPic.get("guid"));
							//PicName ��Ƭ����
							str.append("{");
							str.append("\'PicName\'");
							str.append(":");
							str.append("\'"+mapPic.get("picName")+"\'");
							str.append(",");
							//FileSoure,�̶�Ϊ4
							str.append("\'FileSource\'");
							str.append(":");
							str.append("\'4\'");
							str.append("}");
							if(j!=arrayListPic.size()-1){
								str.append(",");
							}
						}
						str.append("]");
						str.append("\"");
					}else{
						str.append("\"\"");
					}
					str.append(",");
//					//��ע
//					str.append("\"Describe\"");
//					str.append(":");
//					str.append("\"\"");
//					str.append(",");
					//��ע
					str.append("\"remark\"");
					str.append(":");
					str.append("\"\"");
					str.append("}"); 
					if(i!=arrayListCheckRecord.size()-1){
						str.append(",");
					}
				}
				str.append("]");	//����
				str.append("}");	
				boolean stream=false;
				try {
					stream=WebServiceUtil.uploadHiedCheck(str.toString().trim());
					if(stream){
						handler.sendEmptyMessage(Constant.MSG_SUCCESS);
					}else{
						handler.sendEmptyMessage(Constant.MSG_SUCCESS);
					}
				} catch (IOException e) {
					handler.sendEmptyMessage(Constant.MSG_CONN_TIMEOUT);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * �ϴ�ͼƬ*/
	
	public void uploadPic(final HashMap<String, String> map){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					boolean result=false;
					result=WebServiceUtil.uplaodHideCheckPic(map.get("picName"), bitmapByte(map.get("picPath")));
					if(result){
						handler.sendEmptyMessage(Constant.MSG_TEMP1);
					}else{
						handler.sendEmptyMessage(Constant.MSG_TEMP2);
					}
				}catch(Exception e){
					handler.sendEmptyMessage(Constant.MSG_CONN_TIMEOUT);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/** ����ͼƬ������ֵ*/
	public int calculateInSapleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		if(height>reqHeight || width>reqWidth){
			final int heightRatio=Math.round((float)height/(float)reqHeight);
			final int widthRatio=Math.round((float)width/(float)reqWidth);
			inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
		}
		return inSampleSize;
	}
	
	/**����bitmap���� */
	public Bitmap getBitmp(String filePath){
		BitmapFactory.Options  options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(filePath,options);
		options.inSampleSize=calculateInSapleSize(options, 400, 800);
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/** */
	public byte[]  bitmapByte(String filePath){
		Bitmap bitmap=getBitmp(filePath);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b=baos.toByteArray();
		return  b;
	}
	
	
	private int mPicPosition; //��¼��Ƭ�ϴ���postion
	private Handler handler=new Handler(){
	
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case Constant.MSG_SUCCESS:  // �������ϴ��ɹ�,���ϴ���Ƭ
				    mPicPosition=0;
					DB_Provider.uploadCheckFromUploadState(mFormId);
					updateCheckForm(mProjectId);
					if(mArrayListPic.size()>0){
						for(int i=0;i<mArrayListPic.size();i++){
							uploadPic(mArrayListPic.get(i));
						}
					}else{
						if(mProgressDialog!=null){
							mProgressDialog.cancel();
						}
						Toast.makeText(mContext, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();						
					}
				break;
			case Constant.MSG_ERROR:     //�ϴ�ʧ��

				if(mProgressDialog!=null){
					mProgressDialog.cancel();
				}
				break;
			case Constant.MSG_CONN_TIMEOUT:
					
				if(mProgressDialog!=null){
					mProgressDialog.cancel();
				}
				Toast.makeText(mContext,"�������ӳ�ʱ",Toast.LENGTH_SHORT).show();
				break;
			case Constant.MSG_TEMP1:
					  mPicPosition++;
					  Log.i("test", "picPostion="+mPicPosition+",PicCount="+mArrayListPic.size());
						if(mPicPosition==mArrayListPic.size()-1){
							Toast.makeText(mContext, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
							if(mProgressDialog!=null){
								mProgressDialog.cancel();
							}
						}
				break;
			case Constant.MSG_TEMP2:
				if(mPicPosition==mArrayListPic.size()-1){
					Toast.makeText(mContext, "�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
					if(mProgressDialog!=null){
						mProgressDialog.cancel();
					}
				}
				break;
			}
		};
	};
	
}
