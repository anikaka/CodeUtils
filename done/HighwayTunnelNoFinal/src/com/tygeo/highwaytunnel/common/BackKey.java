package com.tygeo.highwaytunnel.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public class BackKey extends Activity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("ע��")
					.setMessage("ȷ��Ҫ�˳���")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
									
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
									                                
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).show();
			break;

		default:
			break;
		}
		return false;
		// return super.onKeyDown(keyCode, event);

	}

}
