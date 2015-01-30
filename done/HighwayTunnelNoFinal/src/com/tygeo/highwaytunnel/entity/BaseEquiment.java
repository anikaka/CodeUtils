package com.tygeo.highwaytunnel.entity;

public class BaseEquiment {

		int  id;
		String TunnelCode;
		 String   Name;
		 String Code;
		 String  Site;
		 String  Status;
		 String ParamId;  
		 String EqType; 
		 String Manufacturer; 
		 String  Effect;
		 
		public String getParamId() {
			return ParamId;
		}
		public void setParamId(String paramId) {
			ParamId = paramId;
		}
		public String getEqType() {
			return EqType;
		}
		public void setEqType(String eqType) {
			EqType = eqType;
		}
		public String getManufacturer() {
			return Manufacturer;
		}
		public void setManufacturer(String manufacturer) {
			Manufacturer = manufacturer;
		}
		public String getEffect() {
			return Effect;
		}
		public void setEffect(String effect) {
			Effect = effect;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTunnelCode() {
			return TunnelCode;
		}
		public void setTunnelCode(String tunnelCode) {
			TunnelCode = tunnelCode;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public String getCode() {
			return Code;
		}
		public void setCode(String code) {
			Code = code;
		}
		public String getSite() {
			return Site;
		}
		public void setSite(String site) {
			Site = site;
		}
		public String getStatus() {
			return Status;
		}
		public void setStatus(String status) {
			Status = status;
		}
		
		
		
}

