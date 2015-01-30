package com.TY.bhgis.Display;

public abstract class MarkSymbol extends Symbol implements IMarkSymbol {


	public static enum MarkStyle {
		SolidCircle,HollowCircle,SolidTriangle,HollowTriangle,Cross;
	}
	public static enum TextMarkStyle {
		DiLou,GuanPianCuoTai,GuanPianJieFeng,DaoChuangTuoKai,qita;
	}

}
