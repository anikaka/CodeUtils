package com.tongyan.widget.view;

import com.tongyan.common.entities._ContactsData;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
/**
 * 
 * @ClassName MyCheckBox 
 * @author wanghb
 * @date 2013-8-20 pm 05:01:03
 * @desc TODO
 */
public class MyCheckBox extends CheckBox {
	
	private _ContactsData contactsData;
	
	
	public MyCheckBox(Context context) {
		super(context);
	}
	
	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setContactsData(_ContactsData contactsData) {
		this.contactsData = contactsData;
	}

	public _ContactsData getContactsData() {
		return contactsData;
	}
	
}
