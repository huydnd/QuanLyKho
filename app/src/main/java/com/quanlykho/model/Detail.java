package com.quanlykho.model;

public class Detail {
	private int detailSuppliesId;
	private int detailReceiptId;
	private int detailAmount;

	public Detail() {
	}

	public Detail(int detailSuppliesId, int detailReceiptId, int detailAmount) {
		this.detailSuppliesId = detailSuppliesId;
		this.detailReceiptId = detailReceiptId;
		this.detailAmount = detailAmount;
	}

	public int getDetailSuppliesId() {
		return detailSuppliesId;
	}

	public void setDetailSuppliesId(int detailSuppliesId) {
		this.detailSuppliesId = detailSuppliesId;
	}

	public int getDetailReceiptId() {
		return detailReceiptId;
	}

	public void setDetailReceiptId(int detailReceiptId) {
		this.detailReceiptId = detailReceiptId;
	}

	public int getDetailAmount() {
		return detailAmount;
	}

	public void setDetailAmount(int detailAmount) {
		this.detailAmount = detailAmount;
	}

	@Override
	public String toString() {
		return "Detail{" +
				"detailSuppliesId=" + detailSuppliesId +
				", detailReceiptId=" + detailReceiptId +
				", detailAmount=" + detailAmount +
				'}';
	}
}
