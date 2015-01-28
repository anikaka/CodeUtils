package com.tongyan.yanan.fragment.oa;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.oa.ContactsDetailsAct;
import com.tongyan.yanan.common.adapter.OaContactsAllAdapter;
import com.tongyan.yanan.common.db.DBService;
import com.tongyan.yanan.common.utils.PingyinUtils;
import com.tongyan.yanan.common.widgets.view.ResizeLayout;
import com.tongyan.yanan.common.widgets.view.RightCharacterListView;
import com.tongyan.yanan.fragment.BaseFragement;
/**
 * 
 * @className OaContactsAllFragment
 * @author wanghb
 * @date 2014-7-14 AM 10:16:42
 * @Desc 通讯录-全部
 */
public class OaContactsAllFragment extends BaseFragement implements OnClickListener, OnItemClickListener{
	
	public Context mMContext;
	private ArrayList<HashMap<String, String>> mMList = new ArrayList<HashMap<String, String>>();
	private OaContactsAllAdapter mAdapter;
	private ListView mListView;
	private RightCharacterListView  mRightCharacterView;
	
	public static OaContactsAllFragment newInstance(Context mContext, String mDptId) {
		OaContactsAllFragment fragment = new OaContactsAllFragment();
		fragment.mMContext = mContext;
		ArrayList<HashMap<String, String>> list = new DBService(mContext).getContactsList(mDptId,"UserNamePinyin");
		if(list != null && list.size() > 0) {
			if(fragment.mMList != null ) {
				fragment.mMList.clear();
				fragment.mMList.addAll(list);
			}
		}
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.oa_contacts_fragment_layout, null, false);
		ResizeLayout mResizeLayout = (ResizeLayout)view.findViewById(R.id.p15_root_id);
		mListView = (ListView)view.findViewById(R.id.contact_depart_listview);
		mRightCharacterView = (RightCharacterListView )view.findViewById(R.id.contacts_rightCharacterListView);
		mAdapter = new OaContactsAllAdapter(mMContext, mMList, this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mRightCharacterView.setOnTouchingLetterChangedListener(new LetterListViewListener());
		
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
		return view;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE: {
				if (msg.arg1 == BIGGER) {
					mRightCharacterView.setVisibility(View.VISIBLE);
				} else {
					mRightCharacterView.setVisibility(View.GONE);
				}
			}
			break;
			
			default:
				break;
		};
		
		}
	}; 
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;
	/*public static ArrayList<HashMap<String, String>> getAllContacts(ArrayList<HashMap<String, String>> list) {
		if(list != null && list.size() > 0) {
			ArrayList<HashMap<String, String>> mBackList = new ArrayList<HashMap<String, String>>();
			ArrayList<String> mSortList = new ArrayList<String>();
			HashMap<String, HashMap<String, String>> mCacheMap = new HashMap<String, HashMap<String, String>>();
			for(int i = 0,len = list.size(); i < len; i ++) {
				HashMap<String, String> map = list.get(i);
				String mNameStr = map.get("UserNamePinyin");
				mCacheMap.put(mNameStr + i, map);
				mSortList.add(mNameStr + i);
			}
			Collections.sort(mSortList);
			for(String s : mSortList) {
				mBackList.add(mCacheMap.get(s));
			}
			return mBackList;
		}
		return null;
	}*/
	
	public class LetterListViewListener implements RightCharacterListView.OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			int num = 0;
			for (int i = 0; i < mMList.size(); i++) {
				if ("a".equalsIgnoreCase(s)) {
					num = 0;
				} else if ( new PingyinUtils().character2ASCII(mMList.get(i).get("UserNamePinyin").substring(0, 1)) < ( new PingyinUtils().character2ASCII(s) + 32)) {
					num += 1;
				}
			}
			mListView.setSelectionFromTop(num, 0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		OaContactsAllAdapter.ViewHolder mViewHolder = (OaContactsAllAdapter.ViewHolder)arg1.getTag();
		Intent intent = new Intent(mMContext,ContactsDetailsAct.class);
		intent.putExtra("IntentMap", mViewHolder.contactsData);
		startActivity(intent);
	}


	@Override
	public void onClick(View v) {
	}
	
}
