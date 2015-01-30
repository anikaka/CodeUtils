package com.TY.bhgis.Display;

import com.TY.bhgis.Display.MarkSymbol.TextMarkStyle;




public interface ITextMarkSymbol {
	public String getText();

	public void setText(String content);

	public int getTextSize();

	public void setTextSize(int textSize);


	public TextMarkStyle getTextMarkStyle();

	public void setTextMarkStyle(TextMarkStyle textMarkStyle);
}
