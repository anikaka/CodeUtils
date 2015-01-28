package com.tongyan.yanan.act.oa;

import java.util.HashMap;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.tongyan.yanan.act.R;
import com.tongyan.yanan.common.utils.JsonTools;
import com.tongyan.yanan.tfinal.FinalActivity;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;
/**
 * 
 * @Title: ContactsDetailsAct.java 
 * @author Rubert
 * @date 2014-8-21 AM 08:29:57 
 * @version V1.0 
 * @Description: 通讯录详情
 */
public class ContactsDetailsAct extends FinalActivity {
	
	@ViewInject(id=R.id.title_common_content)  TextView  mTitleName;
	
	@ViewInject(id=R.id.oa_contacts_name)  TextView  mName;
	@ViewInject(id=R.id.oa_contacts_department)  TextView  mDepartment;
	@ViewInject(id=R.id.oa_contacts_phone)  TextView  mPhone;
	@ViewInject(id=R.id.oa_contacts_email)  TextView  mEmail;
	@ViewInject(id=R.id.oa_contacts_qq)  TextView  mQQ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oa_contacts_details);
		mTitleName.setText("详情");
		if(getIntent() != null && getIntent().getExtras() != null) {
			HashMap<String, String> map = (HashMap<String, String>)getIntent().getExtras().get("IntentMap");
			if(map != null) {
				Resources mResources = getResources();
				mName.setText(mResources.getString(R.string.name) + JsonTools.getHandlerString(map.get("UserName")));
				mDepartment.setText(mResources.getString(R.string.department_colon) + JsonTools.getHandlerString(map.get("DeptName")));
				mPhone.setText(mResources.getString(R.string.phone) + JsonTools.getHandlerString(map.get("UserPhone")));
				mEmail.setText(mResources.getString(R.string.emial) + JsonTools.getHandlerString(map.get("UserEmail")));
				mQQ.setText(mResources.getString(R.string.qq) + JsonTools.getHandlerString(map.get("UserQQ")));
			}
		}
		
	}
}
