package com.tongyan.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
/**
 * 
 * @author wanghb
 *  
    <!-- 在SDCard中创建与删除文件权限 -->  
	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>  
	<!-- 往SDCard写入数据权限 -->  
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>   

 */
public class FileUtils {
	private String SDPATH;

	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	public FileUtils() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory().getPath();
	}
	/**
	 * 获取SD卡是否存在
	 * @return
	 */
	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取SD卡剩余空间
	 * @return
	 */
	public long getSDFreeSize() {
		// 取得SD卡文件路径
		//File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(SDPATH);
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 获取SD卡总容量
	 * 
	 * @return
	 */
	public long getSDAllSize() {
		// 取得SD卡文件路径
		//File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(SDPATH);
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	
	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String path,String fileName) throws IOException {
		creatSDDir(path);
		File file = new File(SDPATH + "/" + path + fileName);
		if(!file.exists())
		file.createNewFile();
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + "/" + dirName);
		if(!dir.exists())
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + "/" + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) { 
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path, fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(output != null)
				 output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024); // 1000只是缓存
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	/**
	 * 返回文件大小
	 * @param size
	 * @return
	 */
	public static String getFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.##");
        
        //double resourcesize = (double)((double)size/1024);
        
        if((double)((double)size/1024)>1000)
        {
            return df.format((double)((double)size/1024/1024))+"MB";
        }
        else
        {
        	return df.format((double)((double)size/1024))+"KB";
        }
		
	}
	
	
}
