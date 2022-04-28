package com.quanlykho.model;

public class SuppliesDetail {
	private String name;
	private int amount;
	private String unit;

	public SuppliesDetail() {
	}

	public SuppliesDetail(String name, int amount, String unit) {
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
