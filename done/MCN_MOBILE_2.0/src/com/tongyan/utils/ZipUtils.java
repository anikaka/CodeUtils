package com.tongyan.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 
 * @className ZipUtils
 * @author wanghb
 * @date 2014-7-28 PM 04:17:36
 * @Desc 
 */
public class ZipUtils {
	/**
	 * ZIP包解压
	 * @param zipFile
	 * @param folderPath
	 * @throws ZipException
	 * @throws IOException
	 */
	public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			InputStream in = null;
			OutputStream out = null;
			try {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				in = zf.getInputStream(entry);
				String str = folderPath;
				// str = new String(str.getBytes("8859_1"), "GB2312");
				File desFile = new File(str, java.net.URLEncoder.encode(entry.getName(), "UTF-8"));
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
				}
				out = new FileOutputStream(desFile);
				byte buffer[] = new byte[1024 * 1024];
				int realLength = in.read(buffer);
				while (realLength != -1) {
					out.write(buffer, 0, realLength);
					realLength = in.read(buffer);
				}
				} finally {
					if(out != null) {
						out.close();
					}
					if(in != null) {
						in.close();
					}
				}
		}
	}

}
