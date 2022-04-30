package com.quanlykho.model;

public class ExDetail {
	private int exDetailSuppliesId;
	private int exDetailExportId;
	private int exDetailAmount;

	public ExDetail() {
	}

	public ExDetail(int exDetailSuppliesId, int exDetailExportId, int exDetailAmount) {
		this.exDetailSuppliesId = exDetailSuppliesId;
		this.exDetailExportId = exDetailExportId;
		this.exDetailAmount = exDetailAmount;
	}

	public int getExDetailSuppliesId() {
		return exDetailSuppliesId;
	}

	public void setExDetailSuppliesId(int exDetailSuppliesId) {
		this.exDetailSuppliesId = exDetailSuppliesId;
	}

	public int getExDetailExportId() {
		return exDetailExportId;
	}

	public void setExDetailExportId(int exDetailExportId) {
		this.exDetailExportId = exDetailExportId;
	}

	public int getExDetailAmount() {
		return exDetailAmount;
	}

	public void setExDetailAmount(int exDetailAmount) {
		this.exDetailAmount = exDetailAmount;
	}
}
