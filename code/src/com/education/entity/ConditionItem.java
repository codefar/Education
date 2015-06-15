package com.education.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.education.R;

public class ConditionItem implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int conditionItemSelected = R.drawable.arrow_right;
	protected String detailConditionName;
	protected int proviceId;
	protected boolean isSelected=false;

	public static final Parcelable.Creator<ConditionItem> CREATOR = new Parcelable.Creator<ConditionItem>() {
		public ConditionItem createFromParcel(Parcel in) {
			return new ConditionItem(in);
		}

		public ConditionItem[] newArray(int size) {
			return new ConditionItem[size];
		}
	};

	private ConditionItem(Parcel in) {
		conditionItemSelected = in.readInt();
		detailConditionName = in.readString();
		proviceId = in.readInt();
	}

	
	public boolean getSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ConditionItem(String detailConditionName, int proviceId) {
		super();
		this.detailConditionName = detailConditionName;
		this.proviceId = proviceId;
	}

	public void setProviceId(int proviceId) {
		this.proviceId = proviceId;
	}

	public int getProviceId() {
		return proviceId;
	}

	public ConditionItem(int conditionItemSelected, String detailConditionName,
			int proviceId) {
		super();
		this.conditionItemSelected = conditionItemSelected;
		this.detailConditionName = detailConditionName;
		this.proviceId = proviceId;
	}

	public void setDetailConditionName(String detailConditionName) {
		this.detailConditionName = detailConditionName;
	}

	public String getDetailConditionName() {
		return detailConditionName;
	}

	public void setConditionItemSelected(int conditionItemSelected) {
		this.conditionItemSelected = conditionItemSelected;
	}

	public int getConditionItemSelected() {
		return conditionItemSelected;
	}


	@Override
	public String toString() {
		return "ConditionItem [conditionItemSelected=" + conditionItemSelected
				+ ", detailConditionName=" + detailConditionName
				+ ", proviceId=" + proviceId + ", isSelected=" + isSelected
				+ "]";
	}

//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(conditionItemSelected);
//		dest.writeString(detailConditionName);
//		dest.writeInt(proviceId);
//	}

}
