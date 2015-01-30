package com.tygeo.highwaytunnel.common;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.R.array;
import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.TY.bhgis.Util.Image;
import com.google.gson.Gson;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.DBhelper.Web_date_provider;
import com.tygeo.highwaytunnel.entity.BaseEquiment;
import com.tygeo.highwaytunnel.entity.CheckinfoE;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;
import com.tygeo.highwaytunnel.entity.UpdateInfo;
import com.tygeo.highwaytunnel.entity.line_json;

public class WebServiceUtil {
	static JSONObject j, sb;
	static String s;
	// static String serviceNameSpace = "http://192.168.0.96:8088";
	// static String serviceNameSpace =
	// StaticContent.localinfo.get("WebServiceUrl");
	// static String serviceNameSpace =;
	static String serviceURL = "http://192.168.0.3:8088/WebService/BaseInfoService.asmx";
	
	// static String serviceURL1
	// =StaticContent.WebUrl+"/WebService/BaseInfoService.asmx";
	
	public static List<line_json> GetBaseRoadInfo() {
		List<line_json> list = new ArrayList<line_json>();
		String methodName = "GetBaseRoadInfo";
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
//		request.addProperty("UnitCode", 25);
		(new MarshalBase64()).register(envelope);
		
		AndroidHttpTransport ht = new AndroidHttpTransport(
				StaticContent.serviceURL1);
		ht.debug = true;
		try {
			
			ht.call("www.GZHTMIS.com/GetBaseRoadInfo", envelope);
			if (envelope.getResponse() != null) {
				return parse_line(envelope.bodyIn.toString());
															
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return list;
	}

	// 获得线路信息
	private static List<line_json> parse_line(String str) {
		str = str.replace("GetBaseRoadInfoResponse{GetBaseRoadInfoResult=", "");
		str = str.replace("; }", "");
		List<line_json> list = new ArrayList<line_json>();
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject ob = result.getJSONObject(i);
				
				line_json lj = new line_json();
				lj.setSection_name(ob.getString("Section_Name"));
				lj.setSection_id(ob.getString("Section_Id"));
				lj.setFather_id(ob.getString("Father_Id"));
				list.add(lj);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		return list;
	}

	// 获取隧道信息
	public static List<TunnelInfoE> GetBaseTunnelInfo() {
		List<TunnelInfoE> list = new ArrayList<TunnelInfoE>();
		String methodName = "GetBaseTunnelInfo";
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		System.out.println("re" + request.toString());
		(new MarshalBase64()).register(envelope);
		
		AndroidHttpTransport ht = new AndroidHttpTransport(
				StaticContent.serviceURL1);
		ht.debug = true;
		try {
			ht.call("www.GZHTMIS.com/GetBaseTunnelInfo", envelope);
			if (envelope.getResponse() != null) {
				return parse_tunnel(envelope.bodyIn.toString());
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return list;
	}

	private static List<TunnelInfoE> parse_tunnel(String str) {
		str = str.replace("GetBaseTunnelInfoResponse{GetBaseTunnelInfoResult=",
				"");
		str = str.replace("; }", "");
		List<TunnelInfoE> list = new ArrayList<TunnelInfoE>();
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject ob = result.getJSONObject(i);

				/*
				 * line_name String 隧道名称 section_name String 隧道上行长度 up_length
				 * int 隧道上行长度 down_length int 上行桩号 up_num String 下行桩号 down_num
				 * String 竣工时间 completion_time String
				 */
				TunnelInfoE lj = new TunnelInfoE();
				lj.setLine_id(ob.getString("Line_Id"));
				lj.setLine_name(ob.getString("Line_Name"));
				lj.setSection_id(ob.getString("Section_Code"));
				lj.setSection_name(ob.getString("Section_Name"));
				lj.setUp_length(ob.getDouble("Up_Length"));
				lj.setDown_length(ob.getDouble("Down_Length"));
				lj.setUp_num(ob.getString("Up_Num"));
				lj.setDown_num(ob.getString("Down_Num"));
				lj.setCompletion_time(ob.getString("Completion_Time"));
				list.add(lj);
				System.out.println(list.get(0).getSection_id());
				System.out.println(list.get(0).getLine_id());
				System.out.println(list.get(0).getUp_length());
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		return list;
	}

	public static List<CheckinfoE> GetBaseUserInfo() {
		List<CheckinfoE> list = new ArrayList<CheckinfoE>();
		String methodName = "GetBaseUserInfo";
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);

		AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1);
		ht.debug = true;
		try {
			ht.call("www.GZHTMIS.com/GetBaseUserInfo", envelope);
			if (envelope.getResponse() != null) {
				return parse_User(envelope.bodyIn.toString());
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return list;
	}
	
	// 获得线路信息
	private static List<CheckinfoE> parse_User(String str) {
		str = str.replace("GetBaseUserInfoResponse{GetBaseUserInfoResult=", "");
		str = str.replace("; }", "");
		List<CheckinfoE> list = new ArrayList<CheckinfoE>();
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject ob = result.getJSONObject(i);

				CheckinfoE lj = new CheckinfoE();
				lj.setName(ob.getString("Leader_Name"));
				lj.setLeader_UnitCode(ob.getString("Leader_UnitCode"));
				lj.setCheck_id(ob.getString("Leader_Id"));
				lj.setType("P");
				list.add(lj);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		return list;
	}

	public static boolean RequestBaseCheckInfo(JSONObject jb) {
		boolean flag = false;
		List<TunnelInfoE> list = new ArrayList<TunnelInfoE>();
		String methodName = "GetBaseCheckInfo";
		String tmp = jb.toString();
		String mp = tmp.replace("\\\\", "");

		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		request.addAttribute("GetBaseCheckInfo", jb);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;

		// try {
		// URLEncoder.encode(jb.toString(),"UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// 
		// e1.printStackTrace();
		// }
		envelope.setOutputSoapObject(request);
		System.out.println("re" + request.toString());
		(new MarshalBase64()).register(envelope);
		envelope.dotNet = true;

		AndroidHttpTransport ht = new AndroidHttpTransport(
				StaticContent.serviceURL1);
		ht.debug = true;
		try {

			ht.call("GetBaseCheckInfo", envelope);
			if (envelope.getResponse() != null) {
				
				System.out.println("1");
				System.out.println(envelope.bodyIn.toString());

			} else {
				System.out.println("无返回值");
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return flag;
	}

	public static void RequestCheck(String ji, JSONArray jo) {
		// HttpPost request = new
		// HttpPost("http://192.168.0.96:8088/WebService/BaseInfoService/GetBaseCheckInfo");

		HttpPost request = new HttpPost(
				"http://192.168.0.50:8001/GetBaseCheckInfo");
		try {
			// URLEncoder.encode(jo.toString(),"UTF-8");
			StringEntity se = new StringEntity(ji + jo.toString());
			request.setEntity(se);

		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}

		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			String retSrc = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("r:+" + retSrc);
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	public static JSONObject GetCheckInfo(UpdateInfo upfo) {
		JSONObject param = new JSONObject();

		try {
			param.put("TunnelCode", upfo.getTunnelCode());
			param.put("UnitCode", upfo.getUnitCode());
			param.put("CheckDate", upfo.getCheckDate());
			param.put("Weather", upfo.getWeather());
			param.put("CheckUnitCode", upfo.getCheckUnitCode());
			param.put("DailyCheckType", upfo.getDailyCheckType());
			param.put("TunnelStake", upfo.getTunnelStake());
			param.put("BaseCheckTunnelType", upfo.getBaseCheckTunnelType());
			Gson gson = new Gson();
			param.put("CheckDetailList", upfo.getCheckDetailList());
			String s = gson.toJson(param);
			sb = new JSONObject(s);

		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}

		return sb;
	}

	public static boolean  RequestTest(String jb, JSONArray js) throws IOException,
			XmlPullParserException, JSONException {
		
		boolean f=false;
//		List<TunnelInfoE> list = new ArrayList<TunnelInfoE>();
		String methodName = "GetBaseCheckInfo";
//		String tmp = jb.toString();
//		JSONObject job = new JSONObject();
		
//		String mp = tmp.replace("nameValuePairs", "");
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		request.addProperty("dailyCheckJson", jb);
		request.addProperty("diseaseInfoJson", js.toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		// try {
		// URLEncoder.encode(jb.toString(),"UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// 
		// e1.printStackTrace();
		// }
		// envelope.setOutputSoapObject(request);
		System.out.println("re" + request.toString());
		(new MarshalBase64()).register(envelope);
		envelope.dotNet = true;
//		AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1);
		HttpTransportSE ht = new HttpTransportSE(StaticContent.serviceURL1, 10000);   
		ht.debug = true;
		ht.call("www.GZHTMIS.com/GetBaseCheckInfo", envelope);
		System.out.println(envelope.getResponse().toString().equals("true"));
		if (envelope.getResponse().toString().equals("true")) {
			System.out.println("1");
			System.out.println(envelope.bodyIn.toString());
			f=true;

		} else {
			System.out.println("无返回值");
		}
		return f;
	}

	public static boolean UpdatePic() throws IOException, XmlPullParserException {
		boolean f=false;
		Map<String, ArrayList<String>> map = DB_Provider.GetTaskPicPath(StaticContent.update_id);
		// try {
		ArrayList<String> path = map.get("PicPath");
		ArrayList<String> name = map.get("PicName");
		String methodName = "UploadBaseCheckFile";
		for (int i = 0; i < path.size(); i++) {
			SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			// String s = Environment.getExternalStorageDirectory().getPath() +
			// "/index.png"
			// String s=dopic("/mnt/sdcard/index.png");
			request.addProperty("fileName", name.get(i));
			request.addProperty("fileBytes", dopic(path.get(i)));
			// System.out.println("re" + request.toString());
			(new MarshalBase64()).register(envelope);
			envelope.dotNet = true;
			AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1, 4000);
			ht.debug = true;
			ht.call("www.GZHTMIS.com/UploadBaseCheckFile", envelope);
			System.out.println(envelope.bodyIn.toString());
			if (envelope.getResponse().toString().equals("文件已经上传成功")) {
				f=true;
			}
		}
		// } catch (Exception e) {
		
		// e.printStackTrace();
		// }
		return f;
	}

	public static String GetByteArray(String path) {
		String s = "";
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			fis = new FileInputStream(path);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);

			}
			byte[] bt = baos.toByteArray();
			s = new String(Base64.encode(bt, Base64.DEFAULT));
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}

		}

		return s;
	}

	public static byte[] dopic(String path) {
		String s = "";
		ImageUtil utils = new ImageUtil();
		Bitmap bmp1 = utils.returnBmpByPath(path+".jpg");
		Bitmap bmp2 = utils.zoomBitmap(bmp1, 300, 400);
		// s=Base64.encodeToString(utils.Bitmap2Bytes(bmp2), 0);
		byte[] by = utils.Bitmap2Bytes(bmp2);
		if (bmp1 != null && !bmp1.isRecycled()) {
			bmp1.recycle();
			bmp1 = null;

		}
		if (bmp2 != null && !bmp2.isRecycled()) {
			bmp2.recycle();
			bmp2 = null;
		}
		return by;

	}

	// 获取管理站的信息
	public static List<CheckinfoE> GetBaseManagerUnitInfo() {
		List<CheckinfoE> list = new ArrayList<CheckinfoE>();
		String methodName = "GetUnitInfo";
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		System.out.println("re" + request.toString());
		(new MarshalBase64()).register(envelope);
		AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1);
		System.out.println(StaticContent.serviceURL1);
		ht.debug = true;
		try {
			ht.call("www.GZHTMIS.com/GetUnitInfo", envelope);
			if (envelope.getResponse() != null) {
				System.out.println(envelope.bodyIn.toString());
				return parse_Manager(envelope.bodyIn.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<CheckinfoE> parse_Manager(String str) {
		str = str.replace("GetUnitInfoResponse{GetUnitInfoResult=", "");
		str = str.replace("; }", "");
		System.out.println(str);
		List<CheckinfoE> list = new ArrayList<CheckinfoE>();
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject ob = result.getJSONObject(i);

				CheckinfoE lj = new CheckinfoE();
				lj.setManangerUnitName(ob.getString("ManagerUnitName"));
				 lj.setRoadCodeList(ob.getString("RoadCodeList"));
				 lj.setUnitCode(ob.getInt("UnitCode"));
				list.add(lj);
				
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			
		}

		return list;
	}

	// 定期检查上传
	public static boolean RequestTestDQ(String jb, String js) throws JSONException, IOException, XmlPullParserException {
		boolean flag = false;
			List<TunnelInfoE> list = new ArrayList<TunnelInfoE>();
			String methodName = "SaveRegularCheck";
			String tmp = jb.toString();
			JSONObject job = new JSONObject();
			job.put("dailyCheckJson", js);
			String mp = tmp.replace("nameValuePairs", "");
			SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
			request.addProperty("dailyCheckDtoJson", jb);
			request.addProperty("diseaseInfoDtoJson", js);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			// try {
			// URLEncoder.encode(jb.toString(),"UTF-8");
			// } catch (UnsupportedEncodingException e1) {
			// 
			// e1.printStackTrace();
			// }
			// envelope.setOutputSoapObject(request);
			System.out.println("re" + request.toString());
			(new MarshalBase64()).register(envelope);
			envelope.dotNet = true;
			AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1);
			ht.debug = true;
			ht.call("www.GZHTMIS.com/SaveRegularCheck", envelope);
			Log.i("sdsdsds", envelope.bodyIn.toString());
			if (envelope.getResponse().toString().equals("true")) {
				System.out.println("1");
				System.out.println(envelope.bodyIn.toString());
				flag = true;
			} else {
				System.out.println("无返回值");
			}
		return flag;
	}
	
	
	public static boolean SavaEquipmentPeriodicCheck(String jb, String js) throws IOException, XmlPullParserException {
		boolean f=false;
		String methodName = "SaveEquipmentPeriodicCheck";
		JSONObject job = new JSONObject();
			SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
			request.addProperty("equipmentCheckJson", jb);
			request.addProperty("dailyCheckJson", js);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			// try {
			// URLEncoder.encode(jb.toString(),"UTF-8");
			// } catch (UnsupportedEncodingException e1) {
			// 
			// e1.printStackTrace();
			// }
			// envelope.setOutputSoapObject(request);
			System.out.println("re" + request.toString());
			(new MarshalBase64()).register(envelope);
			envelope.dotNet = true;
			AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1);
			ht.debug = true;
			ht.call("www.GZHTMIS.com/SaveEquipmentPeriodicCheck", envelope);
			Log.i("sdsdsds", envelope.bodyIn.toString());
			if (envelope.getResponse().toString().equals("true")) {
				System.out.println("1");
				System.out.println(envelope.bodyIn.toString());
				f=true;
			} else {
				System.out.println("无返回值");
			}

			return f;
	}

	public static boolean SavaEquipmentRegularlyCheck(String jb, String js) throws IOException, XmlPullParserException {
		boolean f=false;
			String methodName = "SaveEquipmentRegularlyCheck";
			JSONObject job = new JSONObject();
			SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
			request.addProperty("equipmentCheckJson", jb);
			request.addProperty("dailyCheckJson", js);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			
			// try {
			// URLEncoder.encode(jb.toString(),"UTF-8");
			// } catch (UnsupportedEncodingException e1) {
			// 
			// e1.printStackTrace();
			// }
			// envelope.setOutputSoapObject(request);
			System.out.println("re" + request.toString());
			(new MarshalBase64()).register(envelope);
			envelope.dotNet = true;
			AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1, 4000);
			ht.debug = true;
			ht.call("www.GZHTMIS.com/SaveEquipmentRegularlyCheck", envelope);
//			Log.i("sdsdsds", envelope.bodyIn.toString());
			if (envelope.getResponse().toString().equals("true")) {
				System.out.println("1");
				System.out.println(envelope.bodyIn.toString());
				f=true;
			} else {
				System.out.println("无返回值");
			}
			return f;
	

	}
	
	
	//获得基础设备信息
	public static List<BaseEquiment> GetBaseEquipmentInfo() {
		List<BaseEquiment> list = new ArrayList<BaseEquiment>();
		String methodName = "GetBaseEquipmentInfo";
		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
//		request.addProperty("UnitCode", 25);
		(new MarshalBase64()).register(envelope);

		AndroidHttpTransport ht = new AndroidHttpTransport(
				StaticContent.serviceURL1);
		ht.debug = true;
		try {
			
			ht.call("www.GZHTMIS.com/GetBaseEquipmentInfo", envelope);
			if (envelope.getResponse() != null) {
				return BaseEquipment(envelope.bodyIn.toString());
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return list;
	}
	private static List<BaseEquiment> BaseEquipment(String str) {
		str = str.replace("GetBaseEquipmentInfoResponse{GetBaseEquipmentInfoResult=", "");
		str = str.replace("; }", "");
		str=str.replaceAll("\\,\"Id\":0", "");
		List<BaseEquiment> list = new ArrayList<BaseEquiment>();
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject ob = result.getJSONObject(i);
				
				BaseEquiment lj = new BaseEquiment();
				lj.setId(Integer.parseInt(ob.getString("Id")));
				Gson g=new Gson();
				JSONObject jarr=(ob.getJSONObject("BaseEquipmentDetail"));
				for (int j = 0; j < jarr.length(); j++) {
				lj.setName(jarr.getString("Name"));
				lj.setParamId(jarr.getString("ParamId"));
				lj.setEffect(jarr.getString("Effect"));
				lj.setEqType(jarr.getString("EqType"));
				lj.setCode(ob.getString("Code"));
				lj.setTunnelCode(ob.getString("TunnelCode"));
				lj.setSite(jarr.getString("Site"));
				list.add(lj);
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
			
		return list;
	}
	
	
	/**
	 * 隐患排查数据上传
	 * @param json
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static boolean  uploadHiedCheck(String json) throws IOException, XmlPullParserException{
		String methodName="SaveHDDataInfo";
		  String url="http://192.168.0.166:8086/WebService/BaseInfoService.asmx";
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
		AndroidHttpTransport http = new AndroidHttpTransport(StaticContent.serviceURL1,5000);
		http.debug = true;
		http.call("www.GZHTMIS.com/SaveHDDataInfo", envelope);
		Log.i("test", envelope.getResponse().toString());
			if (envelope.getResponse().toString().equals("成功")) {
				return true;
			}

//		SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
//		request.addProperty("makeInfo", json);
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		envelope.bodyOut = request;
//		(new MarshalBase64()).register(envelope);
//		envelope.dotNet = true;
//		HttpTransportSE ht = new HttpTransportSE(StaticContent.serviceURL1, 10000);   
//		ht.debug = true;
//		ht.call("www.GZHTMIS.com/SaveHDDataInfo", envelope);
//		System.out.println(envelope.getResponse().toString().equals("true"));
//		if (envelope.getResponse().toString().equals("成功")) {
//			return true;
//
//		} else {
//			System.out.println("无返回值");
//		}
			
			return false;
	}
	
	/**
	 * 隐患照片排查
	 * @return  true 上传成功,otherWise false
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static boolean uplaodHideCheckPic(String picName,byte[] picByte) throws IOException, XmlPullParserException {
			String methodName = "UploadBaseCheckFile";
			SoapObject request = new SoapObject("www.GZHTMIS.com", methodName);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			request.addProperty("fileName", picName);
			request.addProperty("fileBytes", picByte);
			envelope.bodyOut = request;
			(new MarshalBase64()).register(envelope);
			envelope.dotNet = true;
			AndroidHttpTransport ht = new AndroidHttpTransport(StaticContent.serviceURL1, 8000);
			ht.debug = true;
			ht.call("www.GZHTMIS.com/UploadBaseCheckFile", envelope);
			System.out.println(envelope.bodyIn.toString());
			if (envelope.getResponse().toString(). equals("文件已经上传成功")) {
				return true;
			}
			return false;
		}
	
	// 判断是否联网
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

}
