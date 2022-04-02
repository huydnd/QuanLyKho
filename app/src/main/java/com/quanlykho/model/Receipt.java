package com.quanlykho.model;

public class Receipt {
	private int receiptId;
	private int receiptWarehouseId;
	private String receiptDate;

	public Receipt() {
	}

	public Receipt(int receiptId, int receiptWarehouseId, String receiptDate) {
		this.receiptId = receiptId;
		this.receiptWarehouseId = receiptWarehouseId;
		this.receiptDate = receiptDate;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public int getReceiptWarehouseId() {
		return receiptWarehouseId;
	}

	public void setReceiptWarehouseId(int receiptWarehouseId) {
		this.receiptWarehouseId = receiptWarehouseId;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
}
