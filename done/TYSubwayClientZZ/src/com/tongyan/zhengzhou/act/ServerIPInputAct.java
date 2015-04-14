package com.tongyan.zhengzhou.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongyan.zhengzhou.common.afinal.MFinalActivity;
import com.tongyan.zhengzhou.common.afinal.annotation.view.ViewInject;
import com.tongyan.zhengzhou.common.utils.Constants;

public class ServerIPInputAct extends MFinalActivity implements OnClickListener {

	@ViewInject(id = R.id.ip_Input_Edit)
	EditText ipText;
	@ViewInject(id = R.id.title_content)
	TextView mTitle;
	@ViewInject(id = R.id.title_back_btn)
	LinearLayout mTitleBackBtn;
	@ViewInject(id = R.id.save)
	Button mSaveBtn;

	private String ipStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_input_main);
		MApplication.getInstance().addActivity(this);

		mTitle.setText("服务器IP配置");
		mSaveBtn.setVisibility(View.VISIBLE);
		mSaveBtn.setText("保存");

		if (getIntent().getExtras() != null) {
			ipStr = getIntent().getExtras().getString("setttingValue");
		}

		if (ipStr != null && !"".equals(ipStr)) {
			ipText.setText(ipStr);
			ipText.setSelection(ipText.getText().toString().length());
		}

		mTitleBackBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);

	}

	public void setValue() {
		SharedPreferences setting = getSharedPreferences(
				Constants.PRFERENCES_IP_ROUTE_KEY, 0);
		SharedPreferences.Editor editor = setting.edit();
		editor.putString(Constants.PREFERENCES_IP_ROUTE, ipText.getText()
				.toString());
		editor.commit();
		closeKeyBoard();

	}
	/**
	 * 关闭软键盘
	 */
	public void closeKeyBoard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_btn:
			finish();
			closeKeyBoard();
			break;
		case R.id.save:
			setValue();
			finish();
			break;
		default:
			break;
		}

	}
}
