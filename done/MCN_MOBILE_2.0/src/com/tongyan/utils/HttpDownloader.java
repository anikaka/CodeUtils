package com.tongyan.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Environment;

/**
 * 
 * @ClassName HttpDownloader 
 * @author wanghb
 * @date 2013-7-12 pm 04:25:21
 * @desc TODO
 */
public class HttpDownloader {
	private URL url = null;
	FileUtils fileUtils = new FileUtils();

	public int downfile(String urlStr, String path, String fileName) {
		if (fileUtils.isFileExist(path + fileName)) {
			return 1;//已有文件
		} else {

			try {
				InputStream input = null;
				input = getInputStream(urlStr);
				File resultFile = write2SDFromInput(path, fileName, input);
				if (resultFile == null) {
					return -1;//下载失败
				}
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}

		}
		return 0;//"下载成功！", 
	}
	
	public File downfile2(String urlStr, String path, String fileName) {
		try {
			InputStream input = null;
			input = getInputStream(urlStr);
			File resultFile = write2SDFromInput(path, fileName, input);
			return resultFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
	public InputStream getInputStream(String urlStr) throws IOException {
		InputStream is = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			is = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	
	 
	 public static File write2SDFromInput(String dirPath,String fileName,InputStream input) throws IOException {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return null;
		}
		String cDirPath = Environment.getExternalStorageDirectory().getPath() + dirPath;
		
		File dirFile = new File(cDirPath);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		FileOutputStream output = null;
		File file = null;
		try {
			file = new File(cDirPath + fileName);
			if(!file.exists()) {
				file.createNewFile();
			}
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer,0,len);
			}
			output.flush();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			if(file != null && file.exists()) {
				file.delete();
				file = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			if(file != null && file.exists()) {
				file.delete();
				file = null;
			}
		} finally {
			if(output != null)
				 output.close();
			if(input != null) {
				input.close();
			}
		}
		return file;
	 }
	
	 public static InputStream  getInputStreamByHttpClient(String mUrl) throws ClientProtocolException, IOException {
		 HttpGet httpRequest = new HttpGet(mUrl);
         HttpClient httpclient = new DefaultHttpClient();
         HttpParams httpParams = new BasicHttpParams();
         HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
         HttpConnectionParams.setSoTimeout(httpParams, 5000);
         httpRequest.setParams(httpParams);
         HttpResponse response = (HttpResponse)httpclient.execute(httpRequest);
         HttpEntity entity = response.getEntity();
         BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
         InputStream instream = bufHttpEntity.getContent();
         BufferedInputStream bi = new BufferedInputStream(instream);
		 return bi;
	 }
	 
}
