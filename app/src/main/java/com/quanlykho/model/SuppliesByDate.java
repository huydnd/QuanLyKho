package com.quanlykho.model;

import java.util.Date;

public class SuppliesByDate {
	String suppliesName;
	int suppliesAmount;
	String date;

	public SuppliesByDate() {
	}

	public SuppliesByDate(String suppliesName, int suppliesAmount, String date) {
		this.suppliesName = suppliesName;
		this.suppliesAmount = suppliesAmount;
		this.date = date;
	}

	public String getSuppliesName() {
		return suppliesName;
	}

	public void setSuppliesName(String suppliesName) {
		this.suppliesName = suppliesName;
	}

	public int getSuppliesAmount() {
		return suppliesAmount;
	}

	public void setSuppliesAmount(int suppliesAmount) {
		this.suppliesAmount = suppliesAmount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
