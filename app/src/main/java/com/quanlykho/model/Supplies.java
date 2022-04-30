package com.quanlykho.model;

public class Supplies {
	private int suppliesId;
	private String suppliesName;
	private String suppliesUnit;

	public Supplies() {
	}

	public Supplies(int suppliesId, String suppliesName, String suppliesUnit) {
		this.suppliesId = suppliesId;
		this.suppliesName = suppliesName;
		this.suppliesUnit = suppliesUnit;
	}

	public Supplies(String suppliesName, String suppliesUnit) {
		this.suppliesName = suppliesName;
		this.suppliesUnit = suppliesUnit;
	}

	public int getSuppliesId() {
		return suppliesId;
	}

	public void setSuppliesId(int suppliesId) {
		this.suppliesId = suppliesId;
	}

	public String getSuppliesName() {
		return suppliesName;
	}

	public void setSuppliesName(String suppliesName) {
		this.suppliesName = suppliesName;
	}

	public String getSuppliesUnit() {
		return suppliesUnit;
	}

	public void setSuppliesUnit(String suppliesUnit) {
		this.suppliesUnit = suppliesUnit;
	}
}
