package com.TY.bhgis.Database;



import java.util.Vector;

public interface IBHClass {
	  public  int getBHCount();

	  public  IBH getBH(String BHID);

	  public  int getBHType();

	  public  IBH createBH(boolean isPhoto);

	  public void delete(IBH bh);
	  
	  public  String[] getFields();

	  public  int findField(String fieldName);

	  public  Vector<IBH> getBHs();

	  public  void setBHs(Vector paramVector);

	  public  void clearBHs();
}
