package com.quanlykho.model;

public class Warehouse {
	private int warehouseId;
	private String warehouseName;
	private String warehouseAddress;

	public Warehouse() {
	}

	public Warehouse(int warehouseId, String warehouseName, String warehouseAddress) {
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.warehouseAddress = warehouseAddress;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
}
