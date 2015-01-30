package com.tygeo.highwaytunnel.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class PaseXMl {
	String ss;
	public String parseXMLDom(String filename) {
		String result = null;
		String temp;
		try {
			File file = new File("mnt/sdcard/TYcache/local.xml");
			if (!file.exists()) {
				String st = String.format("Couldn't find file...");
				return st;
			} 
			// Parse the document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			// Walk the document
			Element root = document.getDocumentElement();
			temp = ("root=" + root.getTagName() + "\n");
			result += temp;
			// List the children of <books>; a set of <book> elements
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					// Found a <book> element
					temp = ("Handling node: " + node.getNodeName() + "\n");
					result += temp;
					Element element = (Element) node;
					temp = ("tCategory Attribute: "+ element.getAttribute("loacl") + "\n");
					result += temp;
					ss="";
					// Get its children: <author>, <title>, <price>
					NodeList childList = element.getChildNodes();
					for (int j = 0; j < childList.getLength(); j++) {
						// Once one of these nodes is attained, the next
						// step is locating its text element
						// It is important. It is NOT <author>'s value, but it
						// is its text element's value
						Node childNode = childList.item(j);
						if (childNode.getNodeType() == childNode.ELEMENT_NODE) {
							NodeList childNodeList = childNode.getChildNodes();
							for (int k = 0; k < childNodeList.getLength(); k++) {
								Node innerChildNode = childNodeList.item(k);		
								temp = ("ttNode (" + childNode.getNodeName()+ ") = "+ innerChildNode.getNodeValue() + "\n");
								result += temp;
								StaticContent.localinfo.put(childNode.getNodeName(), innerChildNode.getNodeValue());
								StaticContent.serviceURL1="http://"+StaticContent.localinfo.get("WebServiceUrl").trim()+"/WebService/BaseInfoService.asmx";
//								StaticContent.serviceURL1=StaticContent.serviceURL1.replaceAll("\n", "").trim();
								StaticContent.webURLxml=StaticContent.localinfo.get("WebServiceUrl").trim();
								ss=StaticContent.localinfo.get("UnitCode");
								System.out.println(StaticContent.UnitCode+"unticode!!!!");
								// innerChildNode's name is "text", and
								// childNode's value is null
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		StaticContent.UnitCode=ss.replaceAll("\n", "").trim();
		System.out.println(ss.replaceAll("\n", "").trim()+"unticode!!!!");
		return result;
	}
	
	public static void save(Map<String, String>  st)
	throws Exception, IllegalStateException, IOException {
	XmlSerializer serializer = Xml.newSerializer();//获取XML写入信息的序列化对象 
	File file = new File("mnt/sdcard/TYcache/local.xml");
	FileOutputStream os = new FileOutputStream(file);	
	serializer.setOutput(os, "UTF-8");//设置要写入的OutputStream
	serializer.startDocument("UTF-8", true);//设置文档标签
	serializer.startTag(null, "fileinfo");//设置开始标签，第一个参数为namespace
	serializer.startTag(null, "loacl");
	serializer.startTag(null, "WebServiceUrl");
	serializer.text(st.get("WebServiceUrl"));
	serializer.endTag(null, "WebServiceUrl");
	serializer.startTag(null, "UnitCode");
	serializer.text(st.get("UnitCode"));
	serializer.endTag(null, "UnitCode");
	serializer.endTag(null, "loacl");
	serializer.endTag(null, "fileinfo");
	serializer.endDocument();
	os.flush();
	os.close();
	}
	
	

	
	
}
