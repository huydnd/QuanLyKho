package com.quanlykho.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SuppliesDetail implements Parcelable {
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

	public SuppliesDetail(Parcel in) {
		name = in.readString();
		amount = in.readInt();
		unit = in.readString();
	}

	public static final Creator<SuppliesDetail> CREATOR = new Creator<SuppliesDetail>() {
		@Override
		public SuppliesDetail createFromParcel(Parcel in) {
			return new SuppliesDetail(in);
		}

		@Override
		public SuppliesDetail[] newArray(int size) {
			return new SuppliesDetail[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(amount);
		dest.writeString(unit);
	}
}
