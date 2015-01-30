package com.tongyan.utils;

import java.io.IOException;
import java.util.HashMap;
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
import android.net.http.AndroidHttpClient;
import android.preference.PreferenceManager;

/**
 * @ClassName WebServiceUtils 
 * @author wanghb
 * @date 2013-7-17 pm 01:09:56
 * @desc TODO
 */
public class WebServiceUtils {
	
	/**
	 * @param properties
	 * @param subM
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static String requestM(Map<String,String> properties,String subM, Context mContext) throws IOException,XmlPullParserException{
		SoapObject request = new SoapObject(Constansts.SERVICE_NAMESPACE, subM);
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
		//envelope.setOutputSoapObject(request);
		envelope.encodingStyle="UTF-8";  
		envelope.dotNet = true;
		new MarshalBase64().register(envelope);
		//Android传输对象      
			
		String mRoute = ConnectivityUtils.getRoute(mContext);
		if(mRoute == null || "".equals(mRoute)) {
			return null;
		}
		HttpTransportSE transport = new HttpTransportSE(mRoute, 10000);        
		transport.debug = true;
		transport.call(Constansts.SERVICE_NAMESPACE + subM, envelope);
		if(envelope.getResponse()!=null){                
			return envelope.bodyIn.toString();  
		}
		return null;
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param type
	 * @param params
	 * @param method
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static String getRequestStr(String username, String password, String pageCount, 
			String currentPage, String type, String params, String method,Context context) throws IOException, XmlPullParserException {
		Map<String,String> properties = new HashMap<String,String>();
		properties.put("publicKey", Constansts.PUBLIC_KEY);
		properties.put("userName", username);
		properties.put("Password", password);
		if(pageCount != null) {
			properties.put("pageCount", pageCount);
		}
		if(currentPage != null) {
			properties.put("currentPage", currentPage);
		}
		if(type != null) {
			properties.put("type", type);
		}
		if(params != null) {
			properties.put("parms", params);
		} else if("".equals(params)) {
			properties.put("parms", null);
		}
		return requestM(properties, method,context);
	}
	
}
