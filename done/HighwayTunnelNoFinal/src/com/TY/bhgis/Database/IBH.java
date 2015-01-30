package com.TY.bhgis.Database;

import com.TY.bhgis.Geometry.IGeometry;



public interface IBH {

	public String getBHID();

	public IGeometry getShape();

	public void setShape(IGeometry paramIGeometry);

	public String[] getFields();

	public String[] getValues();

	public boolean setValues(String[] values);

	public void setValue(int index, String value);

	public String getValue(int index);

	public int getBHType();

	public void store();

	public boolean getVisible();

	public void setVisible(boolean flag);

}
