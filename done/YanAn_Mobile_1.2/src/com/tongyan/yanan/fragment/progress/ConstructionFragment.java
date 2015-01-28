package com.tongyan.yanan.fragment.progress;

import java.util.ArrayList;
import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.adapter.ProgressPlanEditListAdpater;
import com.tongyan.yanan.common.db.DBService;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @className ConstructionFragment
 * @author wanghb
 * @date 2014-7-17 PM 01:51:48
 * @Desc 施工项
 */
public class ConstructionFragment extends Fragment {
	
	
	private ListView mListView;
	private Context mMContext;
	
	private static ArrayList<HashMap<String, String>> mCommonList = new ArrayList<HashMap<String, String>>();
	private static ProgressPlanEditListAdpater mAdapter;
	
	public static ConstructionFragment newInstance(Context mContext) {
		ConstructionFragment fragment = new ConstructionFragment();
		fragment.mMContext = mContext;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_listview_contaienr, null);
		mListView = (ListView)view.findViewById(R.id.listView_data);
		
		mAdapter = new ProgressPlanEditListAdpater(mMContext, mCommonList, R.layout.progress_plan_report_listview_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mOnItemClickListener);
		refreshListView(mMContext);
		return view;
	}
	
	public static void refreshListView(Context context) {
		ArrayList<HashMap<String, String>> list = new DBService(context).getProgressInfoList("1");
		if(mCommonList != null) {
			mCommonList.clear();
			mCommonList.addAll(list);
			list = null;
		}
		mAdapter.notifyDataSetChanged();
	}
	
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			final ProgressPlanEditListAdpater.ViewHolder mViewHolder = (ProgressPlanEditListAdpater.ViewHolder)arg1.getTag();
		
			if(mViewHolder != null && mViewHolder.mItemData != null) {
				if(new DBService(mMContext).queryTableProgressInfoCheckBox(mViewHolder.mItemData.get("NewId"))){
				final Dialog mDialog = new Dialog(mMContext, R.style.dialog);
				mDialog.show();
				mDialog.setContentView(R.layout.common_input_dialog);
				mDialog.setCanceledOnTouchOutside(false);
				TextView mTextView = (TextView) mDialog.findViewById(R.id.title_common_content);
				mTextView.setText("输入值");
				final EditText mInputView = (EditText) mDialog.findViewById(R.id.common_content_edit);
				Button mSureBtn = (Button) mDialog.findViewById(R.id.common_save_btn);
				Button mCancleBtn = (Button) mDialog.findViewById(R.id.common_clear_btn);
				
				mSureBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String mText = mInputView.getText().toString();
						if("".equals(mText)) {
							Toast.makeText(mMContext, "请输入值", Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							Float.parseFloat(mText);
						} catch (Exception e) {
							Toast.makeText(mMContext, "请输入正确的数字", Toast.LENGTH_SHORT).show();
							return;
						}
						
						if (mDialog != null) {
							mDialog.dismiss();
						}
						new DBService(mMContext).updateProgressInputText(mText, mViewHolder.mItemData.get("ID"));
						refreshListView(mMContext);
					}
				});
				mCancleBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mDialog != null) {
							mDialog.dismiss();
						}
					}
				});
				
		 }else{
			 Toast.makeText(mMContext, "请先勾选该项", Toast.LENGTH_SHORT).show();
		 }
			}
		}
	};
}
