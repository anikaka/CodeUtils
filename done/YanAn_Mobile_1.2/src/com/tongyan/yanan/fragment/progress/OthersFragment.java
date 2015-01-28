package com.tongyan.yanan.fragment.progress;

import java.util.HashMap;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.progress.completion.SaveCompletionDataAct;
import com.tongyan.yanan.common.db.DBService;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @className ConstructionFragment
 * @author wanghb
 * @date 2014-7-17 PM 01:51:48
 * @Desc 施工项
 */
public class OthersFragment extends Fragment implements OnClickListener{
	
	private Context mMContext;
	private String mMDataType;//1：日完成量，2：周完成量，3：月完成量，4：年完成量
	private String mMCompletionDateId;//主表id
	
	public static final int TYPE_REASON_EDIT = 1;
	public static final int TYPE_QUESTION_EDIT = 2;
	public static final int TYPE_REMARK_EDIT = 3;
	public static final int TYPE_STATE = 0;
	public static final int TYPE_ALL = 4;
	
	public static TextView mReasonEdit = null,mQuestionEdit = null, mReamrkEdit = null;
	
	private SharedPreferences mPreferences;
	
	
	public static OthersFragment newInstance(Context mContext, String mDataType, String mCompletionDateId) {
		OthersFragment fragment = new OthersFragment();
		fragment.mMContext = mContext;
		fragment.mMDataType = mDataType;
		fragment.mMCompletionDateId = mCompletionDateId;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.progerss_compltion_others, null);
		TextView mReasonHint =  (TextView)view.findViewById(R.id.editReason_hint);
		mReasonEdit =  (TextView)view.findViewById(R.id.editReason);
		TextView mQuestionHint =  (TextView)view.findViewById(R.id.editQuestion_hint);
		mQuestionEdit =  (TextView)view.findViewById(R.id.editQuestion);
		mReamrkEdit =  (TextView)view.findViewById(R.id.editRemark);
		if("1".equals(mMDataType)) {//日完成量只需要 备注
			mReasonHint.setVisibility(View.GONE);
			mReasonEdit.setVisibility(View.GONE);
			mQuestionHint.setVisibility(View.GONE);
			mQuestionEdit.setVisibility(View.GONE);
		} 
		mReasonEdit.setOnClickListener(this);
		mQuestionEdit.setOnClickListener(this);
		mReamrkEdit.setOnClickListener(this);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mMContext);
		
		//初始化
		HashMap<String, String> map = new DBService(mMContext).getCompletionDateById(mMCompletionDateId);
		if(null != map) {
			mReasonEdit.setText(map.get("Reason"));
			mQuestionEdit.setText(map.get("Question"));
			mReamrkEdit.setText(map.get("Remark"));
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(mReasonEdit)) {
			showDialog(TYPE_REASON_EDIT);
			return;
		}
		if(v.equals(mQuestionEdit)) {
			showDialog(TYPE_QUESTION_EDIT);
			return;
		}
		if(v.equals(mReamrkEdit)) {
			showDialog(TYPE_REMARK_EDIT);
			return;
		}
		
	}
	
	public void setPreferences(String text, int type) {
		Editor editor = mPreferences.edit();
		switch (type) {
		case TYPE_REASON_EDIT:
			editor.putString(SaveCompletionDataAct.OTHERS_EDIT_REASON, text);
			break;
		case TYPE_QUESTION_EDIT:
			editor.putString(SaveCompletionDataAct.OTHERS_EDIT_QUESTION, text);
			break;
		case TYPE_REMARK_EDIT:
			editor.putString(SaveCompletionDataAct.OTHERS_EDIT_REMARK, text);
			break;
		default:
			break;
		}
		editor.commit();
	}
	
	
	public void showDialog(final int mEditType) {
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
				if (null != mDialog) {
					mDialog.dismiss();
				}
				//new DBService(mMContext).updateCompletionDate(mMCompletionDateId, mText, mEditType);
				
				switch (mEditType) {
				case TYPE_REASON_EDIT:
					if(mReasonEdit != null) {
						setPreferences(mText, TYPE_REASON_EDIT);
						mReasonEdit.setText(mText);
					}
					break;
				case TYPE_QUESTION_EDIT:
					if(mQuestionEdit != null) {
						setPreferences(mText, TYPE_QUESTION_EDIT);
						mQuestionEdit.setText(mText);
					}
					break;
				case TYPE_REMARK_EDIT:
					if(mReamrkEdit != null) {
						setPreferences(mText, TYPE_REMARK_EDIT);
						mReamrkEdit.setText(mText);
					}
					break;
				default:
					break;
				}
			}
		});
		mCancleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mDialog) {
					mDialog.dismiss();
				}
			}
		});
	}
	
}
