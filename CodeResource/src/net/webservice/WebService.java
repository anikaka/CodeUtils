package net.webservice;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;


/**
 * 网络请求类
 * @author Administrator
 *
 */

public class WebService {
	

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
//		String mRoute = ConnectivityUtils.getRoute(mContext);
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
//	}
	

	/**
	 * 隐患排查数据上传
	 * @param json
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	/*
	public static boolean  uploadHiedCheck(String json) throws IOException, XmlPullParserException{
		String methodName="SaveHDDataInfo";
		  String url="http://192.168.0.3:8086/WebService/BaseInfoService.asmx";
		//www.GZHTMIS.com/SaveHDDataInfo
//		SoapObject soapObject=new SoapObject("www.GZHTMIS.com", methodName);
		SoapObject soapObject=new SoapObject("www.GZHTMIS.com", methodName);
		soapObject.addProperty("makeInfo", json);
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=soapObject;
		envelope.dotNet=true;
		envelope.encodingStyle="UTF-8";
		(new MarshalBase64()).register(envelope);
		//http://192.168.0.166:8086/WebService/BaseInfoService.asmx?
		//StaticContent.serviceURL1
		AndroidHttpTransport http = new AndroidHttpTransport(StaticContent.serviceURL1,10000);
		http.debug = true;
		http.call("www.GZHTMIS.com/SaveHDDataInfo", envelope);
		Log.i("test", envelope.getResponse().toString());
			if (envelope.getResponse().toString().equals("成功")) {
				return true;
			}
	 */

}
