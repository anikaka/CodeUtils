package com.TY.bhgis.Util;

public final class Type {

	// 病害类型
	public static final byte JGLF = 1; // 结构裂缝
	public static final byte JGCT = 2;// 结构错台
	public static final byte JGQC = 3;// 结构起层
	public static final byte JGBL = 4;// 结构剥落
	public static final byte SFDSL = 5;// 三缝点渗漏
	public static final byte SFXSL = 6;// 三缝线渗漏
	public static final byte LFDSL = 7;// 裂缝点渗漏
	public static final byte LFXSL = 8;// 裂缝线渗漏
	public static final byte MZGL = 9;// 面状渗漏
	public static final byte GB = 10;// 挂冰

	
	// 内外侧
	public static final String inhole = "进口";
	public static final String outhole = "出口";

	// 上下行
	public static final int sx = 0;//上行
	public static final int xx = 1;//下行
	public static final String z = "Z";
	public static final String y = "Y";

	
	// 日常检查衬砌病害位置编号
	public static final String leftbqid = "4";
	public static final String rightbqid = "5";
	public static final String leftgyid = "2";
	public static final String rightgyid = "3";
	public static final String gdid = "1";
	
	// 定期检查衬砌病害位置编号
	public static final String dleftbqid = "13";
	public static final String drightbqid = "14";
	public static final String dleftgyid = "11";
	public static final String drightgyid = "12";
	public static final String dgdid = "10";
	
}
