package com.tongyan.yanan.fragment;



import com.tongyan.yanan.act.R;
import com.tongyan.yanan.act.oa.OaContactsMenuAct;
import com.tongyan.yanan.act.oa.OaEmailAct;
import com.tongyan.yanan.act.oa.OaMessageAct;
import com.tongyan.yanan.act.oa.OaNoticeAct;
import com.tongyan.yanan.act.oa.OaReceiveTextAct;
import com.tongyan.yanan.act.oa.OaRulesAct;
import com.tongyan.yanan.act.oa.OaScheduleAct;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 
 * @author ChenLang
 *  OA界面
 * @Date 0214/06/18
 * @version  YanAn 1.0
 * @LastModifyDate 2014/06/14 by wanghb
 */

public class OaFragement extends BaseFragement implements OnClickListener{
	
	RelativeLayout mCalendarSchedule,mOaContacts,mOa_receiveText,mOaNotice,mOaMessage,mEmail,mOa_rules;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_oa, null, false);
		mCalendarSchedule = (RelativeLayout) view.findViewById(R.id.oa_calendar_schedule);
		mOaContacts = (RelativeLayout) view.findViewById(R.id.oa_contacts);
		mOa_receiveText=(RelativeLayout)view.findViewById(R.id.oa_receiveText);
		mOaNotice = (RelativeLayout) view.findViewById(R.id.oa_notice);
		mOaMessage = (RelativeLayout) view.findViewById(R.id.oa_message);
		mEmail = (RelativeLayout) view.findViewById(R.id.oa_email);
		mOa_rules=(RelativeLayout)view.findViewById(R.id.oa_rules);
		mCalendarSchedule.setOnClickListener(this);
		mOaContacts.setOnClickListener(this);
		mOaNotice.setOnClickListener(this);
		mOa_receiveText.setOnClickListener(this);
		mOaMessage.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mOa_rules.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v.equals(mCalendarSchedule)) {
			intentClazz(OaScheduleAct.class);
			return;
		}

		if (v.equals(mOaContacts)) {
			intentClazz(OaContactsMenuAct.class);
			return;
		}
		
		if (v.equals(mOaNotice)) {
			intentClazz(OaNoticeAct.class);
			return;
		}

		if (v.equals(mOaMessage)) {
			intentClazz(OaMessageAct.class);
			return;
		}
		
		if (v.equals(mEmail)) {
			intentClazz(OaEmailAct.class);
			return;
		}
		//收文查看
		if(v.equals(mOa_receiveText)){
			intentClazz(OaReceiveTextAct.class);
			return;
		}
		//规章制度
		if(v.equals(mOa_rules)){
			intentClazz(OaRulesAct.class);
			return;
		}
	}
	
	public void intentClazz(Class<?> clazz) {
		Intent intent = new Intent(getActivity(), clazz);
		if(clazz == OaContactsMenuAct.class) {
			intent.putExtra("IntentType", "OaFragement");
		}
		startActivity(intent);
	}
}
