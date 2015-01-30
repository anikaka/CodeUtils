package com.TY.bhgis.Database;

import java.util.Vector;

public class BHClass implements IBHClass {

	private Vector<IBH> bhs;
	private String[] fields;
	private int bhCount;
	private int bhtype;

	public BHClass(String[] fields, int bhtype) {
		this.fields = fields;
		this.bhtype = bhtype;
	}

	@Override
	public int getBHCount() {

		return this.bhCount;
	}

	@Override
	public IBH getBH(String BHID) {

		for (int i = 0; i < this.bhs.size(); i++) {
			if (this.bhs.get(i).getBHID() == BHID) {
				return this.bhs.get(i);
			}
		}
		return null;
	}

	@Override
	public int getBHType() {

		return this.bhtype;
	}

	@Override
	public IBH createBH(boolean isPhoto) {

		String bhid = getMaxBHID();
		String[] values = getValues(isPhoto);
		IBH bh = new BH(null, DataProvider.PROJECTID + "-" + bhid, fields,values, bhtype);
		this.bhs.add(bh);
		this.bhCount = this.bhs.size();
		bh.store();
		return bh;
	}

	@Override
	public String[] getFields() {

		return this.fields;
	}

	@Override
	public int findField(String fieldName) {

		for (int i = 0; i < this.fields.length; i++) {
			if (this.fields[i].equals(fieldName)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Vector<IBH> getBHs() {

		return this.bhs;
	}

	@Override
	public void setBHs(Vector paramVector) {

		this.bhCount = paramVector.size();
		this.bhs = new Vector<IBH>();
		for (int i = 0; i < paramVector.size(); i++) {
			this.bhs.add((IBH) paramVector.get(i));
		}

	}

	@Override
	public void clearBHs() {

		for (int i = 0; i < this.bhs.size(); i++) {
			this.bhs.set(i, null);
		}
		this.bhs = null;

	}

	private String getMaxBHID() {

		return DataProvider.GetMaxBHID();
	}

	private String[] getValues(boolean isPhoto) {
		String[] values = new String[this.fields.length];
		values[findField("check_data")] = DataProvider.getBHName(bhtype);
		values[findField("belong_pro")] = "³ÄÆö";
		values[findField("task_id")] = DataProvider.PROJECTID;
		values[findField("UP_DOWN")] = String.valueOf(DataProvider.DIRECTION);
		values[findField("ISCQ")] = Integer.toString(1);
		values[findField("BHTYPE")] = Integer.toString(this.bhtype);
		values[findField("CHECKID")] = String.valueOf(DataProvider.getCheckIDFromBHType(bhtype));
		if (isPhoto) {
			values[findField("pic_id")] = DataProvider.GetMaxBHImageID();
		} else {
			values[findField("pic_id")] = "";
		}

		return values;

	}

	@Override
	public void delete(IBH bh) {

		this.bhs.removeElement(bh);
		this.bhCount = this.bhs.size();
		DataProvider.BHDelete(bh.getBHID());
	}
}
