package com.tongyan.yanan.common.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * 
 * @Title: WebServiceUtils.java 
 * @author Rubert
 * @date 2014-8-8 AM 09:28:41 
 * @version V1.0 
 * @Description: TODO
 */
public class WebServiceUtils {
	
	
	public static String requestM(Map<String,String> properties,String subM, Context mContext) throws IOException,XmlPullParserException{
		SoapObject request = new SoapObject(Constants.SERVICE_NAMESPACE, subM);
		//request.addProperty("publicKey","AA8F96C7-8294-4E33-B695-AD3BB67B1B41");
		if(properties != null && !properties.isEmpty()) {
			Iterator iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry)iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				request.addProperty(String.valueOf(key),String.valueOf(val));
			}
		}
		//获得序列化的Envelope        
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);        
		envelope.bodyOut=request;
		envelope.dotNet = true;
		//envelope.setOutputSoapObject(request);
		envelope.encodingStyle="UTF-8";  
		new MarshalBase64().register(envelope);
		//Android传输对象      
		SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(mContext);
		String mPath = mShared.getString(Constants.PREFERENCES_INFO_ROUTE, "");
		if(mPath == null || "".equals(mPath)) {
			mPath = Constants.COMMON_URL_IP;
		}
		String mRoute = Constants.HTTP + mPath + Constants.SERVICE_EM ;
		if(mRoute == null || "".equals(mRoute)) {
			return null;
		}
		HttpTransportSE transport = new HttpTransportSE(mRoute, 10000);        
		transport.debug = true;
		transport.call(Constants.SERVICE_NAMESPACE + subM, envelope);
		if(envelope.getResponse()!=null){                
			return envelope.bodyIn.toString();  
		}
		return null;
	}
	
	
	
	
}
