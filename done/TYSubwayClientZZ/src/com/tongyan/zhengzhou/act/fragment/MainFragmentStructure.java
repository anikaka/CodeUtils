package com.tongyan.zhengzhou.act.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.tongyan.zhengzhou.act.MainAct;
import com.tongyan.zhengzhou.act.R;
import com.tongyan.zhengzhou.act.adapter.BaseObjectAdapter;
import com.tongyan.zhengzhou.act.adapter.FacilityObjectAdapter;
import com.tongyan.zhengzhou.act.check.CheckResultAnalysisAct;
import com.tongyan.zhengzhou.common.afinal.MFinalFragment;
import com.tongyan.zhengzhou.common.db.CheckManagementDBService;
import com.tongyan.zhengzhou.common.utils.Constants;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @Title: MainFragmentStructure.java 
 * @author Rubert
 * @date 2015-3-10 上午11:21:21 
 * @version V1.0 
 * @Description: 检查管理
 */
public class MainFragmentStructure extends MFinalFragment implements OnClickListener, 
											OnItemClickListener, OnScrollListener{
	
	private Button baseObjectBtn;
	private Button facilityObjectBtn;
	private Button checkTaskBtn;
	private Button startAnalysisBtn;
	
	private BaseObjectAdapter baseAdapter;
	private FacilityObjectAdapter facilityAdapter;
	private ListView baseObjectLv;
	private ListView facilityObjectLv;
	
	private int blPosition = 0;
	
	private LinearLayout checkTaskObjectLl;
	
	private LinearLayout backBtn;
    private TextView titleContent;
	
	private ArrayList<HashMap<String, Object>> baseObjectList;	//基本对象列表(listView使用)
	private ArrayList<HashMap<String, Object>> allBaseObjectList;	//基本对象列表（获取数据使用）
	private ArrayList<HashMap<String, Object>> facilityObjectList;	//设施对象列表
	
	//检查方式
	private Spinner checkWaySpinner;
	private ArrayAdapter<String> checkWayAdapter;
	private ArrayList<String> checkWayList;
	//检查类型
	private Spinner checkTypeSpinner;
	private ArrayAdapter<String> checkTypeAdapter;
	private ArrayList<String> checkTypeList;
	//病害类型
	private Spinner damageTypeSpinner;
	private ArrayAdapter<String> damageTypeAdapter;
	private ArrayList<String> damageTypeList;
	
	private CheckBox groupCb1;	//第一组CheckBox
	private CheckBox groupCb2;	//第二组CheckBox
	private CheckBox groupCb3;	//第三组CheckBox
	private TextView timeTv1;	//第一组时间
	private TextView timeTv2;	//第二组时间
	private TextView timeTv3;	//第三组时间
	private TextView timeTv;	//时间
	private String dateFormat;	//日期
	
	
	public static MainFragmentStructure newInstance() {
		MainFragmentStructure mMainFragment2 = new MainFragmentStructure();
		return mMainFragment2;
	} 
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragmentstructure, container, false);
		
		View titleLayout = view.findViewById(R.id.check_manage_fragment_titile);
	    backBtn = (LinearLayout) titleLayout.findViewById(R.id.title_back_btn);
	    titleContent = (TextView) titleLayout.findViewById(R.id.title_content);
	    titleContent.setText("检查管理");
		
		baseObjectBtn = (Button) view.findViewById(R.id.base_object);
		facilityObjectBtn = (Button) view.findViewById(R.id.facility_object);
		checkTaskBtn = (Button) view.findViewById(R.id.check_task);
		startAnalysisBtn = (Button) view.findViewById(R.id.startAnalyze);
		
		baseObjectLv = (ListView) view.findViewById(R.id.base_object_list);
		facilityObjectLv = (ListView) view.findViewById(R.id.facility_object_list);
		checkTaskObjectLl = (LinearLayout) view.findViewById(R.id.check_task_object);
		
		//检查方式
		checkWaySpinner = (Spinner) view.findViewById(R.id.check_way_spinner);
		checkWayList = new CheckManagementDBService().getAllCheckWay();
		checkWayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.check_adapter_item, R.id.check_adapter_item_tv, checkWayList);
		checkWayAdapter.setDropDownViewResource(R.layout.check_adapter_item);
		checkWaySpinner.setAdapter(checkWayAdapter);
		//检查类型
		checkTypeSpinner = (Spinner) view.findViewById(R.id.check_type_spinner);
		checkTypeList = new CheckManagementDBService().getAllCheckType();
		checkTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.check_adapter_item, R.id.check_adapter_item_tv, checkTypeList);
		checkTypeAdapter.setDropDownViewResource(R.layout.check_adapter_item);
		checkTypeSpinner.setAdapter(checkTypeAdapter);
		//病害类型
		damageTypeSpinner = (Spinner) view.findViewById(R.id.damage_type_spinner);
		damageTypeList = new CheckManagementDBService().getAllDamageType();
		damageTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.check_adapter_item, R.id.check_adapter_item_tv, damageTypeList);
		damageTypeAdapter.setDropDownViewResource(R.layout.check_adapter_item);
		damageTypeSpinner.setAdapter(damageTypeAdapter);
		
		//数据对比
		groupCb1 = (CheckBox) view.findViewById(R.id.check_task_object_checkBox1);
		groupCb2 = (CheckBox) view.findViewById(R.id.check_task_object_checkBox2);
		groupCb3 = (CheckBox) view.findViewById(R.id.check_task_object_checkBox3);
		timeTv1 = (TextView) view.findViewById(R.id.check_task_object_time1);
		timeTv2 = (TextView) view.findViewById(R.id.check_task_object_time2);
		timeTv3 = (TextView) view.findViewById(R.id.check_task_object_time3);
		
		backBtn.setOnClickListener(this);
		baseObjectBtn.setOnClickListener(this);
		facilityObjectBtn.setOnClickListener(this);
		checkTaskBtn.setOnClickListener(this);
		startAnalysisBtn.setOnClickListener(this);
		timeTv1.setOnClickListener(this);
		timeTv2.setOnClickListener(this);
		timeTv3.setOnClickListener(this);
		
		baseObjectLv.setOnItemClickListener(this);
		facilityObjectLv.setOnItemClickListener(this);
		baseObjectLv.setOnScrollListener(this);
		facilityObjectLv.setOnScrollListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.base_object:
			baseObjectLv.setVisibility(View.VISIBLE);
			facilityObjectLv.setVisibility(View.GONE);
			checkTaskObjectLl.setVisibility(View.GONE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					baseObjectList = new CheckManagementDBService().getAllLines();
					//new CheckManagementDBService().getCheckObjectAllData(allBaseObjectList);
					
					sendMessage(Constants.MSG_0x2010);
				}
			}).start();
			break;
		case R.id.facility_object:
			baseObjectLv.setVisibility(View.GONE);
			facilityObjectLv.setVisibility(View.VISIBLE);
			checkTaskObjectLl.setVisibility(View.GONE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					facilityObjectList = new CheckManagementDBService().getAllFacilitys();
					sendMessage(Constants.MSG_0x2011);
				}
			}).start();
			break;
		case R.id.check_task:
			baseObjectLv.setVisibility(View.GONE);
			facilityObjectLv.setVisibility(View.GONE);
			checkTaskObjectLl.setVisibility(View.VISIBLE);
			break;
		case R.id.startAnalyze:
			String checkWay = checkWaySpinner.getSelectedItem().toString(); 	//检查方式
			String checkType = checkTypeSpinner.getSelectedItem().toString(); 	//检查类型
			String damageTypeName = damageTypeSpinner.getSelectedItem().toString(); 	//病害类型

			//选择的区间对象
			ArrayList<HashMap<String, String>> mCheckObjectCodeList = new CheckManagementDBService().getSelectedBaseObject(baseObjectList);
			if(mCheckObjectCodeList==null || mCheckObjectCodeList.size()<=0){
				sendMessage(Constants.MSG_0x2012);
				return;
			}
			//选择的设施设备对象
			ArrayList<HashMap<String, String>> mCheckFacilityCodeList = new CheckManagementDBService().getSelectedfacilityObject(facilityObjectList);
			if(mCheckFacilityCodeList==null || mCheckFacilityCodeList.size()<=0){
				sendMessage(Constants.MSG_0x2013);
				return;
			}
			//选择的对比时间
			ArrayList<HashMap<String, String>> mCompareDateList = getCompareDateList();
			if(mCompareDateList==null || mCompareDateList.size()<=0){
				sendMessage(Constants.MSG_0x2014);
				return;
			}
			
			Intent intent = new Intent(getActivity(), CheckResultAnalysisAct.class);
			intent.putExtra("CheckObjectCodeList", mCheckObjectCodeList);
			intent.putExtra("CheckFacilityCodeList", mCheckFacilityCodeList);
			intent.putExtra("CheckingMethod", new CheckManagementDBService().getSelectedCheckWay(checkWay));
			intent.putExtra("CheckingType", new CheckManagementDBService().getSelectedCheckType(checkType));
			intent.putExtra("DamageTypeList", new CheckManagementDBService().getSelectedDamageType(damageTypeName));
			intent.putExtra("CompareDateList", mCompareDateList);
			startActivity(intent);
			break;
		case R.id.title_back_btn://返回
			if(MainAct.mSlidingMenu != null) {
				MainAct.mSlidingMenu.showMenu();
			}
			break;
		case R.id.check_task_object_time1://第一组时间
			timeTv = timeTv1;
			showDatePickerDialog();
			break;
		case R.id.check_task_object_time2://第二组时间
			timeTv = timeTv2;
			showDatePickerDialog();
			break;
		case R.id.check_task_object_time3://第三组时间
			timeTv = timeTv3;
			showDatePickerDialog();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 获取对比的时间
	 * */
	private ArrayList<HashMap<String, String>> getCompareDateList(){
		try {
			ArrayList<HashMap<String, String>> compareDateList = new ArrayList<HashMap<String,String>>();
			if(groupCb1.isChecked()){
				HashMap<String, String> map = new HashMap<String, String>();
				Date date = new SimpleDateFormat("yyyy'年'MM'月'dd'日'").parse(timeTv1.getText().toString());
				map.put("CompareDate", new SimpleDateFormat("yyyy-MM-dd").format(date));
				compareDateList.add(map);
			}
			if(groupCb2.isChecked()){
				HashMap<String, String> map = new HashMap<String, String>();
				Date date = new SimpleDateFormat("yyyy'年'MM'月'dd'日'").parse(timeTv2.getText().toString());
				map.put("CompareDate", new SimpleDateFormat("yyyy-MM-dd").format(date));
				compareDateList.add(map);
			}
			if(groupCb3.isChecked()){
				HashMap<String, String> map = new HashMap<String, String>();
				Date date = new SimpleDateFormat("yyyy'年'MM'月'dd'日'").parse(timeTv3.getText().toString());
				map.put("CompareDate", new SimpleDateFormat("yyyy-MM-dd").format(date));
				compareDateList.add(map);
			}
			return compareDateList;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() { 
		   
       public void onDateSet(DatePicker view, int year, int monthOfYear, 
              int dayOfMonth) { 
    	   Date date = new Date(year-1900, monthOfYear, dayOfMonth);
    	   dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
           timeTv.setText(new SimpleDateFormat("yyyy'年'MM'月'dd'日'").format(date));
       } 
    };
    
    private void showDatePickerDialog(){
    	Calendar calendar = Calendar.getInstance();
		new DatePickerDialog(getActivity(), mDateSetListener, calendar.get(Calendar.YEAR), 
								calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final int pos =  position;
		
		switch (parent.getId()) {
		case R.id.base_object_list:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HashMap<String, Object> baseObject = baseObjectList.get(pos);
					boolean isClick = (Boolean) baseObject.get("IsClick");
					if(isClick){
						baseObject.put("IsClick", false);
						new CheckManagementDBService().removeObject(baseObjectList, baseObject);
						sendMessage(0x1001);
					}else{
						baseObject.put("IsClick", true);
						ArrayList<HashMap<String, Object>> list = new CheckManagementDBService().getLineInfos(baseObject);
						if(list != null){
							new CheckManagementDBService().combineList(baseObjectList, pos, list);
							sendMessage(Constants.MSG_0x2010);
						}
					}
				}
			}).start();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.base_object_list:
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {   
				blPosition = view.getFirstVisiblePosition();
	        }   
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		// TODO Auto-generated method stub
		super.handleOtherMessage(flag);
		switch (flag) {
		case Constants.MSG_0x2010:
			baseAdapter = new BaseObjectAdapter(getActivity(), baseObjectList, R.layout.base_object_list_item);
			baseObjectLv.setAdapter(baseAdapter);
			baseObjectLv.setSelection(blPosition);
			break;
		case Constants.MSG_0x2011:
			facilityAdapter = new FacilityObjectAdapter(getActivity(), facilityObjectList, R.layout.base_object_list_item);
			facilityObjectLv.setAdapter(facilityAdapter);
			break;
		case Constants.MSG_0x2012:
			Toast.makeText(getActivity(), "您没有选择需要分析的区间", Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2013:
			Toast.makeText(getActivity(), "您没有选择需要分析设施设备", Toast.LENGTH_SHORT).show();
			break;
		case Constants.MSG_0x2014:
			Toast.makeText(getActivity(), "至少选择一组对比时间", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	
}
