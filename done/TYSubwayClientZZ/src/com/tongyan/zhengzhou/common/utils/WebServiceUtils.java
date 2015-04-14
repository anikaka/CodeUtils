package com.tongyan.zhengzhou.common.utils;

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

/**
 * @ClassName WebServiceUtils 
 * @author wanhb
 * @date 2013-12-1 pm 20:24:56
 */
public class WebServiceUtils {
	
	/**
	 * @param properties
	 * @param subM
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @author wanghb 
	 */
	public static String requestM(Context mContext, HashMap<String,String> properties, String subM) throws IOException,XmlPullParserException{
		SoapObject request = new SoapObject(Constants.SERVICE_NAMESPACE, subM);
		request.addProperty("publicKey",Constants.PUBLIC_KEY);
		if(properties != null && !properties.isEmpty()) {
			Iterator iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry)iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				request.addProperty(String.valueOf(key), String.valueOf(val));
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
		String mRoute = CommonUtils.getRouteAndSubUrl(mContext);
		if(mRoute == null || "".equals(mRoute)) {
			return null;
		}
		HttpTransportSE transport = new HttpTransportSE(mRoute);        
		transport.debug = true;
		transport.call(Constants.SERVICE_NAMESPACE + subM, envelope);
		if(envelope.getResponse() != null){                
			return envelope.bodyIn.toString();  
		}
		return null;
	}
	
	/**
	 * @param properties
	 * @param subM
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @author wanghb 
	 */
	/*public static String requestOthersM(Context mContext, HashMap<String,String> properties, String subM) throws IOException,XmlPullParserException{
		SoapObject request = new SoapObject(Constants.SERVICE_NAMESPACE, subM);
		request.addProperty("publicKey",Constants.SERVER_COMMON_PUBLIC_KEY);
		if(properties != null && !properties.isEmpty()) {
			Iterator iter = properties.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry)iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				request.addProperty(String.valueOf(key), String.valueOf(val));
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
		HttpTransportSE transport = new HttpTransportSE(Constants.SERVER_COMMON_WEBSERVICE);        
		transport.debug = true;
		transport.call(Constants.SERVICE_NAMESPACE + subM, envelope);
		
		if(envelope.getResponse() != null){                
			return envelope.bodyIn.toString();  
		}
		return null;
	}*/
	
	
	
}
