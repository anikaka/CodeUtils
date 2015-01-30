package com.TY.TYBHMapForAndroid;

public class CItem {

	private String ID = "";
	private String Value = "";

	public CItem() {
		ID = "";
		Value = "";
	}

	public CItem(String _ID, String _Value) {
		ID = _ID;
		Value = _Value;
	}

	@Override
	public String toString() {
		return Value;
	}

	public String GetID() {
		return ID;
	}

	public String GetValue() {
		return Value;
	}
}
