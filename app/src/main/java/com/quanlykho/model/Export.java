package com.quanlykho.model;

public class Export {
	private int exportId;
	private int exportWarehouseId;
	private String exportDate;
	private String exportAddress;

	public Export() {
	}

	public Export(int exportId, int exportWarehouseId, String exportDate, String exportAddress) {
		this.exportId = exportId;
		this.exportWarehouseId = exportWarehouseId;
		this.exportDate = exportDate;
		this.exportAddress = exportAddress;
	}


	public Export(int exportWarehouseId, String exportDate,String exportAddress) {
		this.exportWarehouseId = exportWarehouseId;
		this.exportDate = exportDate;
		this.exportAddress = exportAddress;
	}

	public String getExportAddress() {
		return exportAddress;
	}

	public void setExportAddress(String exportAddress) {
		this.exportAddress = exportAddress;
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
