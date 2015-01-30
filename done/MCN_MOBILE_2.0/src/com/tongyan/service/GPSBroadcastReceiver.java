package com.tongyan.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
/**
 * 
 * @ClassName GPSBroadcastReceiver.java
 * @Author Administrator
 * @Date 2013-8-27 pm 07:50:53
 * @Desc "android.location.PROVIDERS_CHANGED" 
 */
public class GPSBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LocationManager alm = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
		Intent servicIntentStart = new Intent(context,MGPSService.class);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			context.startService(servicIntentStart);
		} else {
			context.stopService(servicIntentStart);
		}
	}
	
	
	@Override
	public IBinder peekService(Context myContext, Intent service) {
		return super.peekService(myContext, service);
	}
}
