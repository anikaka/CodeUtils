package com.TY.bhgis.Database;

import com.TY.bhgis.Geometry.IGeometry;



public class BH implements IBH {

	private IGeometry shape;
	private String BHID;
	private String[] fields;
	private String[] values;
	private int bhtype;
	private boolean flag=false;

	public BH(IGeometry shape, String BHID, String[] fields, String[] values,int bhtype) {
		this.shape = shape;
		this.BHID = BHID;
		this.fields = fields;
		this.values = values;
		this.bhtype = bhtype;
	}

	@Override
	public String getBHID() {
	
		return this.BHID;
	}

	@Override
	public IGeometry getShape() {
	
		return this.shape;
	}

	@Override
	public void setShape(IGeometry paramIGeometry) {
	
		if (this.shape != null)
			this.shape = null;
		this.shape = paramIGeometry;
	}

	@Override
	public String[] getValues() {
	
		return this.values;
	}

	@Override
	public boolean setValues(String[] values) {
	
		this.values = values;
		return true;
	}

	@Override
	public void setValue(int index, String value) {
	
		this.values[index] = value;
	}

	@Override
	public String getValue(int index) {
	
		return this.values[index];
	}

	@Override
	public int getBHType() {
	
		return this.bhtype;
	}

	@Override
	public void store() {
	
		DataProvider.BHStore(this.BHID, this.fields, this.values, this.shape);
	}

	@Override
	public String[] getFields() {
	
		return this.fields;
	}

	@Override
	public boolean getVisible() {
	
		return this.flag;
	}

	@Override
	public void setVisible(boolean flag) {
	
		this.flag=flag;
	}

}
