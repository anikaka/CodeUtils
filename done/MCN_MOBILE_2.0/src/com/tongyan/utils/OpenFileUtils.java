package com.tongyan.utils;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

/**
 * @className OpenFileUtils
 * @author wanghb
 * @date 2014-07-15 PM 04:44:51
 * @Desc TODO
 */
public class OpenFileUtils {
	
	
	
	public static Intent openFileByPath(String filePath) {
		File file = new File(filePath);
		return openFileByFile(file);
	}
	
	public static Intent openFileByFile(File file) {
		if (!file.exists())
			return null;
		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			return getAudioFileIntent(file);
		} else if (end.equals("3gp") || end.equals("mp4")) {
			return getAudioFileIntent(file);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			return getImageFileIntent(file);
		} else if (end.equals("apk")) {
			return getApkFileIntent(file);
		} else if (end.equals("ppt")) {
			return getPptFileIntent(file);
		} else if (end.equals("xls")) {
			return getExcelFileIntent(file);
		} else if (end.equals("doc")) {
			return getWordFileIntent(file);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(file);
		} else if (end.equals("chm")) {
			return getChmFileIntent(file);
		} else if (end.equals("txt")) {
			return getTextFileIntent(file, false);
		} else {
			return getAllIntent(file);
		}
	}
	

	// 获取一个用于打开APK文件的intent
	public static Intent getAllIntent(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// 获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// 获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// 获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// 获取一个用于打开Html文件的intent
	public static Intent getHtmlFileIntent(File file) {
		Uri uri = Uri.parse(file.getPath()).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(file.getPath()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// 获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// 获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// 获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// 获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// 获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// 获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(File file, boolean paramBoolean) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(file.getPath());
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(file);
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// 获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

}
