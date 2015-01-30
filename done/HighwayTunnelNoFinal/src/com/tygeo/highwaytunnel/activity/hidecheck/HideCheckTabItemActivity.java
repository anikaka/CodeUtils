package com.tygeo.highwaytunnel.activity.hidecheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.adpter.HideCheckContentAdapter;
import com.tygeo.highwaytunnel.adpter.HideCheckRecordAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * TabHost每一个选项
 * @author ChenLnag
 *
 */

public class HideCheckTabItemActivity  extends Activity implements OnClickListener{

    private  Context mContext=this;
	private ListView mCheckContent,  //检查内容
							  mCheckRecord ;	  //检查记录单
	private TextView mTxtMileage;
	private RadioButton mRadioBtnYes,mRadioBtnNo;
	private ImageButton mImgBtnSubmit; //提交
	private EditText mTxtContentDescribe;
	private EditText mTxtStartMileage;
	private EditText mTxtEndMileage;
	private static Bundle mCheckBundle;
	private HideCheckContentAdapter mCheckContentAdapter;
	private static HideCheckRecordAdapter   mCheckRecordAdapter; 
	private ArrayList<HashMap<String, String>> mArrayListCheckContent=new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> mArrayListCheckRecord=new ArrayList<HashMap<String,String>>();
	private String mContentCode;  //检查内容编码
	private String mContent;  //检查内容
	
	/**
	 * 初始化组件*/
	private void initWidget(){
		mCheckContent=(ListView)findViewById(R.id.listViewCheckContent);
		mCheckRecord=(ListView)findViewById(R.id.listViewCheckRecord);
		mTxtMileage=(TextView)findViewById(R.id.txtMileage);
		mTxtStartMileage=(EditText)findViewById(R.id.txtStartMileage);
		mTxtEndMileage=(EditText)findViewById(R.id.txtEndMileage);
		
		mRadioBtnYes=(RadioButton)findViewById(R.id.radioBtnYes);
		mRadioBtnNo=(RadioButton)findViewById(R.id.radioBtnNo);
		mTxtContentDescribe=(EditText)findViewById(R.id.txtContentDescribe);
		mImgBtnSubmit=(ImageButton)findViewById(R.id.imgBtnSubmitRecord);
		mTxtMileage.setText("格式为("+mCheckBundle.getString("startMileage")+"/"+mCheckBundle.getString("endMileage")+")");
		mTxtStartMileage.setText(mCheckBundle.getString("startMileage").split("\\+")[0]);
		
		
//		mTxtStartMileage.setFocusable(false);
		mCheckContent.setOnItemClickListener(checkContentListener);
		mRadioBtnNo.setChecked(true);
		mTxtContentDescribe.setFocusable(false);
//		mTxtContentDescribe.setBackgroundResource(R.drawable.maybehs);
		mRadioBtnYes.setOnClickListener(this);
		mRadioBtnNo.setOnClickListener(this);
		mImgBtnSubmit.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hidecheck_tab_item);
		if(getIntent().getExtras()!=null){
			mCheckBundle=getIntent().getExtras().getBundle("checkBundle");
		}
		initWidget();
		initApdater();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
//		Log.i("test", "onRestart()");
//		refresh();
	}
	
@Override
protected void onResume() {
	if(getIntent().getExtras()!=null){
		mCheckBundle=getIntent().getExtras().getBundle("checkBundle");
	}
	
//	refresh();
	initApdater();
	super.onResume();
}
	/**
	 * 初始化适配器*/
	private  void initApdater(){
		if(mArrayListCheckContent.size()>0){
			mArrayListCheckContent.clear();
		}
		if(mArrayListCheckRecord.size()>0){
			mArrayListCheckRecord.clear();
		}
		mArrayListCheckContent.addAll(DB_Provider.getCheckContent(mCheckBundle.getString("itemCode")));
		mArrayListCheckRecord.addAll(DB_Provider.queryCheckRecord(mCheckBundle.getString("checkFormId"),mCheckBundle.getString("itemCode")));
		mCheckContentAdapter=new HideCheckContentAdapter(mContext, mArrayListCheckContent, R.layout.hidecheck_content_listview_item);
		mCheckRecordAdapter=new HideCheckRecordAdapter(mContext, mArrayListCheckRecord, R.layout.hidecheck_record_listview_item);
		mCheckContent.setAdapter(mCheckContentAdapter);
		mCheckRecord.setAdapter(mCheckRecordAdapter);
//		refresh();
	}
	
	/**
	 * 刷新
	 */
	public static void refresh(){
//		ArrayList<HashMap<String, String>> list = DB_Provider.queryCheckRecord(mCheckBundle.getString("checkFormId"), mCheckBundle.getString("itemCode"));
//		if(mArrayListCheckRecord != null){
//			if(list != null){
//				mArrayListCheckRecord.clear();
//				mArrayListCheckRecord.addAll(list);
//			}
//		}
		if(mArrayListCheckRecord.size()>0){
			mArrayListCheckRecord.clear();
		}
		mArrayListCheckRecord.addAll(DB_Provider.queryCheckRecord(mCheckBundle.getString("checkFormId"), mCheckBundle.getString("itemCode")));
		mCheckRecordAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 检查内容监听事件*/
	OnItemClickListener checkContentListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			setClickState(position);
			HideCheckContentAdapter.ViewHolder viewHolder=(HideCheckContentAdapter.ViewHolder)view.getTag();
			HashMap<String, String> map=viewHolder.map;
			mContentCode=map.get("code");
			mContent=map.get("content");
		}
	};
	
	
	
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
			mCheckContentAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 *设置默认点击状态*/
	public void  setDefalutClickState(){
		if(mArrayListCheckContent!=null && mArrayListCheckContent.size()>0){
			for(int i=0;i<mArrayListCheckContent.size();i++){
				HashMap<String, String> map=mArrayListCheckContent.get(i);
					map.put("isCheck", "false");
			}
		}
	}
	
	/**
	 * 判断检查内容是否选择
	 * @return true 已选择,otherwise false*/
	public boolean  checkContentIsCheck(){
		if(mArrayListCheckContent!=null && mArrayListCheckContent.size()>0){
			for(int i=0;i<mArrayListCheckContent.size();i++){
				HashMap<String, String> map=mArrayListCheckContent.get(i);
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
		case R.id.radioBtnYes:
			mTxtContentDescribe.setFocusable(true);
			mTxtContentDescribe.setFocusableInTouchMode(true);
			mTxtContentDescribe.setBackgroundResource(R.drawable.boder_white);
			break;
		case R.id.radioBtnNo:
			mTxtContentDescribe.setFocusable(false);
			mTxtContentDescribe.setBackgroundResource(R.drawable.boder_black);
			break;
		case R.id.imgBtnSubmitRecord: //提交
					if(!checkContentIsCheck()){
						Toast.makeText(mContext, "没有选择检查内容", Toast.LENGTH_SHORT).show();
						return;
					}
					if("".equals(mTxtEndMileage.getText().toString()) || "null".equals(mTxtEndMileage.getText().toString())){
						Toast.makeText(mContext, "里程必须填写", Toast.LENGTH_SHORT).show();
						return;
					}
					if(!mileageisAppropriate(mTxtEndMileage.getText().toString()) || !isAppropriateThousandMileage(mTxtStartMileage.getText().toString())){
						Toast.makeText(mContext, "里程输入不正确", Toast.LENGTH_SHORT).show();
					}else{
						//提交
//						Log.i("test", "insertItemCode="+mCheckBundle.getString("itemCode"));
					AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext).setTitle("确认提交数据?");
					alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DB_Provider.insertCheckRecord(mCheckBundle.getString("checkFormId"), mCheckBundle.getString("itemCode"),
									mContentCode, mTxtStartMileage.getText().toString()+"+"+mTxtEndMileage.getText().toString(), mContent, "", mTxtContentDescribe.getText().toString());
							refresh();
						}
					});
					alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					alertDialog.show();}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 判断里程是输入是否合法*/
	public  boolean  mileageisAppropriate(String mileage){
		int mileageMin=Integer.parseInt(mCheckBundle.getString("startMileage").split("\\+")[1]); //最小里程
		int mileagMax=Integer.parseInt(mCheckBundle.getString("endMileage").split("\\+")[1]); //最大里程
		int tempMileage=0;
		if(mileageMin>mileagMax){
			tempMileage=mileageMin;
			mileageMin=mileagMax;
			mileagMax=tempMileage;
		}
		Pattern pattern=Pattern.compile("\\d{1,3}");
		Matcher matcher=pattern.matcher(mileage);
		if(Integer.parseInt(mileage)<mileageMin || Integer.parseInt(mileage)>mileagMax ||  !matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证千位以上,包括千位里程格式 
	 * @retrue true  正确*/
	private boolean  isAppropriateThousandMileage(String mileage){
		Pattern  pattern=Pattern.compile("K{0,1}\\d+");
		Matcher m=pattern.matcher(mileage);
		if(m.matches()){
			return true;
		}
		return false;
	}
}
