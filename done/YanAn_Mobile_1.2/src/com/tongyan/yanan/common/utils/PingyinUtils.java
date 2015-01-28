package com.tongyan.yanan.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingyinUtils {
	/**
	 * 把单个英文字母或者字符串转换成数字ASCII码
	 * 
	 * @param input
	 * @return
	 */
	public int character2ASCII(String input) {
		char[] temp = input.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (char each : temp) {
			builder.append((int) each);
		}
		String result = builder.toString();
		return Integer.parseInt(result);
	}
	
	
	
	public String converterToPinYin(String chinese) {
		if(chinese == null || "".equals(chinese)) {
			return "z";
		}
		String pinyinString = "";
		char[] charArray = chinese.toCharArray();
		// 根据需要定制输出格式，我用默认的即可
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		try {
			// 遍历数组，ASC码大于128进行转换
			for (int i = 0; i < charArray.length; i++) {
				if (charArray[i] > 128) {
					// charAt(0)取出首字母
					if(PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat) != null && PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat)[0] != null)  {
						pinyinString += PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat)[0].charAt(0);
					}
				} else {
					pinyinString += charArray[i];
				}
			}
			return pinyinString;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public static ArrayList<HashMap<String, String>> getAllContacts(ArrayList<HashMap<String, String>> list) {
		if(list != null && list.size() > 0) {
			ArrayList<HashMap<String, String>> mBackList = new ArrayList<HashMap<String, String>>();
			ArrayList<String> mSortList = new ArrayList<String>();
			HashMap<String, HashMap<String, String>> mCacheMap = new HashMap<String, HashMap<String, String>>();
			for(int i = 0,len = list.size(); i < len; i ++) {
				HashMap<String, String> map = list.get(i);
				String mNameStr = map.get("UserNamePinyin");
				mCacheMap.put(mNameStr + i, map);
				mSortList.add(mNameStr + i);
			}
			Collections.sort(mSortList);
			for(String s : mSortList) {
				mBackList.add(mCacheMap.get(s));
			}
			return mBackList;
		}
		return null;
	}*/
	
	
	
}
