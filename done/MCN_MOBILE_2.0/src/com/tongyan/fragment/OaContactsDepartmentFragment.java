package com.tongyan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.adapter.OaContactsDepartAdapter;
import com.tongyan.activity.gps.GpsRouteAct;
import com.tongyan.activity.oa.OaContactsDetailAct;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._Contacts;
import com.tongyan.common.entities._ContactsData;
import com.tongyan.utils.PingyinUtils;
import com.tongyan.widget.view.ResizeLayout;
import com.tongyan.widget.view.RightCharacterListView;
import com.tongyan.widget.view.RightCharacterListView.OnTouchingLetterChangedListener;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @className OaContactsDepartmentFragment
 * @author wanghb
 * @date 2014-6-19 AM 09:38:18
 * @Desc OA-通讯录-部门
 */
public class OaContactsDepartmentFragment extends Fragment implements OnItemClickListener,OnClickListener{
	
	private Context  mMContext;
	private String mMIntentType;
	private ArrayList<String> mMRouteList;
	private boolean mMRouteIsVisiable;
	
	private Button mSearchBtn;
	private EditText mSearchEditText;
	private ListView mListView;
	private RightCharacterListView mLetterListView;
	private OaContactsDepartAdapter mOaContactsDepartAdapter;
	
	
	private List<_ContactsData> mContactsList = new ArrayList<_ContactsData>();
	private List<_ContactsData> newdatalist = new ArrayList<_ContactsData>();
	
	private ResizeLayout mResizeLayout;
	
	public static OaContactsDepartmentFragment newInstance(Context mContext, String mIntentType, ArrayList<String> mRouteList, boolean mRouteIsVisiable) {
		OaContactsDepartmentFragment mFragment = new OaContactsDepartmentFragment();
		mFragment.mMContext = mContext;
		mFragment.mMIntentType = mIntentType;
		mFragment.mMRouteList = mRouteList;
		mFragment.mMRouteIsVisiable = mRouteIsVisiable;
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.cloneInContext(mMContext).inflate(R.layout.oa_contacts_fragment_layout, null);
		mSearchBtn = (Button)view.findViewById(R.id.p15_contact_search_btn_id);
		mSearchEditText = (EditText)view.findViewById(R.id.p15_contact_search_edittext);
		mListView = (ListView)view.findViewById(R.id.p15_contact_depart_listview);
		mResizeLayout = (ResizeLayout)view.findViewById(R.id.p15_root_id); 
		mLetterListView = (RightCharacterListView)view.findViewById(R.id.p15_contacts_rightCharacterListView);
		
		mSearchEditText.setInputType(InputType.TYPE_NULL);//首次禁用软键盘
		mSearchEditText.setCursorVisible(false);//去光标
		
		mLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
		mSearchBtn.setOnClickListener(searchBtnListener);
		mSearchEditText.setOnTouchListener(mEditTouchListener);
		
		businessM();
		return view;
	}
	
	
	private void businessM(){
		//初始化ListAdapter
		mOaContactsDepartAdapter = new OaContactsDepartAdapter(mMContext, mContactsList, this);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mOaContactsDepartAdapter);
		
		//List<_Contacts> conList = new DBService(mMContext).selectContacts2(mMRouteList, mMRouteIsVisiable);
		List<_ContactsData> orderConList = new DBService(mMContext).selectContacts(mMRouteList, mMRouteIsVisiable, 0);//new ContactsUtils().getDepartmentContacts(conList);
		
		if(orderConList != null && orderConList.size() > 0) {
			if(mContactsList != null) {
				mContactsList.clear();
			}
			if(newdatalist != null) {
				newdatalist.clear();
			}
			newdatalist.addAll(orderConList);
			mContactsList.addAll(orderConList);
			mOaContactsDepartAdapter.notifyDataSetChanged();
		} else {
			//getRefresh();
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
				mOaContactsDepartAdapter.notifyDataSetChanged();
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
		OaContactsDepartAdapter.ViewHolder viewHolder = (OaContactsDepartAdapter.ViewHolder)arg1.getTag();
		_ContactsData values = viewHolder.contactsData;
		/*if("gps".equals(type)) {
			Intent intent = new Intent(this,P04_GpsAct.class);
			intent.putExtra("_ContactsData", values);
			startActivity(intent);
			//finish();
		} else*/ if("route".equals(mMIntentType)) {
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
			
			for (int i = 0; i < mContactsList.size(); i++) {
				if ("a".equalsIgnoreCase(s)) {
					num = 0;
				} else if ( new PingyinUtils().character2ASCII(mContactsList.get(i).getDptPinyin().substring(0, 1)) < ( new PingyinUtils().character2ASCII(s) + 32)) {
					num += 1;
				}
			}
			mListView.setSelectionFromTop(num, 0);
		}
	}


	@Override
	public void onClick(View v) {
		
	}
		
	/*public void getRefresh() {
		mDialog = new AlertDialog.Builder(mMContext).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.p00_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		new Thread(new Runnable(){
			@Override
			public void run() {
				String str;
				Map<String,Object> mR = null;
				try {
					str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, null, null, Constansts.METHOD_OF_GETLINKMAN, mMContext);
					mR = new Str2Json().getLinkManInfo(str);
				}  catch (Exception e) {
					sendMessage(Constansts.CONNECTION_TIMEOUT);
					return;
				}
				if (mR != null) {
					isSucc = (String)mR.get("s");

					if ("ok".equals(isSucc)) {
						contactsList = (List<_Contacts>)mR.get("v");
						List<_ContactsData> newlist = new ContactsUtils().getDepartmentContacts(contactsList);
						if (newlist != null && newlist.size() > 0) {
							if (list != null) {
								list.clear();
							}
							if (newdatalist != null) {
								newdatalist.clear();
							}
								if (new DBService(mMContext).delAll("LOCAL_CONTACTS")) {
									if (newlist != null && newlist.size() > 0) {
										for (int i = 0; i < newlist.size(); i++) {
											_ContactsData contacts = newlist.get(i);
											if (contacts != null) {
												new DBService(mMContext).insertContacts(contacts);
											}
										}
									}
								}
							newdatalist.addAll(newlist);
							list.addAll(newlist);
							sendMessage(Constansts.SUCCESS);
						} else {
							sendMessage(Constansts.NO_DATA);
						}
					} else {
						sendMessage(Constansts.ERRER);
					}
				} else {
					sendMessage(Constansts.NET_ERROR);
				}
				
			}
		}).start();
	}*/
}
