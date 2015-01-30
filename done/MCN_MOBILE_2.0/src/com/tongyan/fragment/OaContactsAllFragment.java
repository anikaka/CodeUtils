package com.tongyan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaContactsAllAdapter;
import com.tongyan.activity.gps.GpsRouteAct;
import com.tongyan.activity.oa.OaContactsDetailAct;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._ContactsData;
import com.tongyan.utils.PingyinUtils;
import com.tongyan.widget.view.ResizeLayout;
import com.tongyan.widget.view.RightCharacterListView;
import com.tongyan.widget.view.RightCharacterListView.OnTouchingLetterChangedListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @className OaContactsAllFragment
 * @author wanghb
 * @date 2014-6-19 AM 09:37:47
 * @Desc OA-通讯录-全部
 */
public class OaContactsAllFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private Context  mMContext;
	private String mMIntentType;
	private ArrayList<String> mMRouteList;
	private boolean mMRouteIsVisiable;
	
	private Button mSearchBtn;
	private EditText mSearchEditText;
	private ListView mListView;
	private RightCharacterListView mLetterListView;
	private OaContactsAllAdapter mOaContactsAllAdapter;
	
	//private String isSucc;
	
	private List<_ContactsData> mContactsList = new ArrayList<_ContactsData>();
	private List<_ContactsData> newdatalist = new ArrayList<_ContactsData>();
	
	private ResizeLayout mResizeLayout;
	
	//private MyApplication myApp;
	
	//private String type;
	
	//private TextView noResult;
	
	public static OaContactsAllFragment newInstance(Context mContext, String mIntentType, ArrayList<String> mRouteList, boolean mRouteIsVisiable) {
		OaContactsAllFragment mFragment = new OaContactsAllFragment();
		mFragment.mMContext = mContext;
		mFragment.mMIntentType = mIntentType;
		mFragment.mMRouteList = mRouteList;
		mFragment.mMRouteIsVisiable = mRouteIsVisiable;
		return mFragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.cloneInContext(mMContext).inflate(R.layout.oa_contacts_fragment_layout, null);
		mSearchBtn = (Button)view.findViewById(R.id.p15_contact_search_btn_id);
		mSearchEditText = (EditText)view.findViewById(R.id.p15_contact_search_edittext);
		mListView = (ListView)view.findViewById(R.id.p15_contact_depart_listview);
		mResizeLayout = (ResizeLayout)view.findViewById(R.id.p15_root_id); 
		//noResult = (TextView)view.findViewById(R.id.p15_contacts_no_result);
		mLetterListView = (RightCharacterListView)view.findViewById(R.id.p15_contacts_rightCharacterListView);
		
		mSearchEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mSearchEditText.setCursorVisible(false);//去光标
		
		mLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
		mSearchBtn.setOnClickListener(searchBtnListener);
		mSearchEditText.setOnTouchListener(mEditTouchListener);
		businessM();
		return view;
	}
	/**
	 * 搜索框-点击触摸监听事件
	 */
	OnTouchListener mEditTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			mSearchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			mSearchEditText.setCursorVisible(true);
			return false;
		}
	};
	
	
	private void businessM(){
		// 初始化ListAdapter
		mOaContactsAllAdapter = new OaContactsAllAdapter(mMContext, mContactsList, this);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mOaContactsAllAdapter);
		
		//List<_Contacts> conList = new DBService(mMContext).selectContacts2(mMRouteList,mMRouteIsVisiable);
		List<_ContactsData> orderConList = new DBService(mMContext).selectContacts(mMRouteList, mMRouteIsVisiable, 1);//new ContactsUtils().getAllContacts(conList);
		if(orderConList != null && orderConList.size() > 0) {
			if(mContactsList != null) {
				mContactsList.clear();
			}
			if(newdatalist != null) {
				newdatalist.clear();
			}
			newdatalist.addAll(orderConList);
			mContactsList.addAll(orderConList);
			mOaContactsAllAdapter.notifyDataSetChanged();
		} else {
			//refreshListView();
		}
		
		mResizeLayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				int change = BIGGER; 
				if (h < oldh) { 
					 change = SMALLER; 
				}
				Message msg = new Message(); 
				msg.what = 1; 
				msg.arg1 = change; 
				mHandler.sendMessage(msg); 
			}
		 });
	}
	
	
	
	OnClickListener searchBtnListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			final String searchText = mSearchEditText.getText().toString();
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<_ContactsData> changeList = new ArrayList<_ContactsData>();
					if(newdatalist != null && newdatalist.size() > 0) {
						for(int i = 0; i < newdatalist.size(); i ++) {
							_ContactsData data = newdatalist.get(i);
							String empName = data.getEmpName();
							String dptName = data.getDptName();
							if(empName.contains(searchText) || dptName.contains(searchText)) {//判读用户名和部门名是否在搜索框里出现
								changeList.add(data);
							}
						}
						if(mContactsList != null) {
							mContactsList.clear();
							mContactsList.addAll(changeList);
						} else {
							mContactsList = new ArrayList<_ContactsData>();
							mContactsList.addAll(changeList);
						}
						mHandler.sendEmptyMessage(0x90993);
					}
				}
			}).start();
		}
	};
	
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE: {
				if (msg.arg1 == BIGGER) {
					mLetterListView.setVisibility(View.VISIBLE);
				} else {
					mLetterListView.setVisibility(View.GONE);
				}
			}
			break;
			case 0x90993:
				mOaContactsAllAdapter.notifyDataSetChanged();
			break;
			default:
				break;
		};
		
		}
	}; 
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
OaContactsAllAdapter.ViewHolder viewHolder = (OaContactsAllAdapter.ViewHolder)arg1.getTag();
		
		_ContactsData values = viewHolder.contactsData;
		/*if("gps".equals(type)) {
			Intent intent = new Intent(this,P04_GpsAct.class);
			intent.putExtra("_ContactsData", values);
			startActivity(intent);
			//finish();
		} else */if("route".equals(mMIntentType)) {
			Intent intent = new Intent(mMContext,GpsRouteAct.class);
			intent.putExtra("_ContactsData", values);
			startActivity(intent);
			//finish();
		} else {
			Intent intent = new Intent(mMContext,OaContactsDetailAct.class);
			intent.putExtra("_ContactsData", values);
			startActivity(intent);
		}
	}
	
	
	public class LetterListViewListener implements OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			int num = 0;
			for (int i = 0,len = mContactsList.size(); i < len; i++) {
				if ("a".equalsIgnoreCase(s)) {
					num = 0;
				} else if ( new PingyinUtils().character2ASCII(mContactsList.get(i).getEmpPinyin().substring(0, 1).toLowerCase()) < ( new PingyinUtils().character2ASCII(s) + 32)) {
					num += 1;
				}
			}
			mListView.setSelectionFromTop(num, 0);
		}
	}


	@Override
	public void onClick(View v) {
		
	}
	
}
