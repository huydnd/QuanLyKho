package com.quanlykho.model;

public class Export {
	private int exportId;
	private int exportWarehouseId;
	private String exportDate;

	public Export() {
	}

	public Export(int exportId, int exportWarehouseId, String exportDate) {
		this.exportId = exportId;
		this.exportWarehouseId = exportWarehouseId;
		this.exportDate = exportDate;
	}

	public Export(int exportWarehouseId, String exportDate) {
		this.exportWarehouseId = exportWarehouseId;
		this.exportDate = exportDate;
	}

	public int getExportId() {
		return exportId;
	}

	public void setExportId(int exportId) {
		this.exportId = exportId;
	}

	public int getExportWarehouseId() {
		return exportWarehouseId;
	}

	public void setExportWarehouseId(int exportWarehouseId) {
		this.exportWarehouseId = exportWarehouseId;
	}

	public String getExportDate() {
		return exportDate;
	}

	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;
	}
}
