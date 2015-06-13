package com.education.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ShaiXuanConditionItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int schoolImg;
	protected String conditionName;
	protected String detailCondition;
	// 每个院校省份下会含有很多子类型 如：北京市，上海市等等
	protected ArrayList<ConditionItem> mSubDetailConditionItemList;

	public void setmSubDetailConditionItemList(
			ArrayList<ConditionItem> detailConditionItemList) {
		this.mSubDetailConditionItemList = detailConditionItemList;
	}

	public ArrayList<ConditionItem> getmSubDetailConditionItemList() {
		return mSubDetailConditionItemList;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setDetailCondition(String detailCondition) {
		this.detailCondition = detailCondition;
	}

	public String getDetailCondition() {
		return detailCondition;
	}

	public void setSchoolImg(int schoolImg) {
		this.schoolImg = schoolImg;
	}

	public int getSchoolImg() {
		return schoolImg;
	}

//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(this.schoolImg);
//		dest.writeString(this.conditionName);
//		dest.writeString(this.detailCondition);
//		dest.writeTypedList(mSubDetailConditionItemList);
//	}

	public ShaiXuanConditionItem() {
	}

	protected ShaiXuanConditionItem(Parcel in) {
		this.schoolImg = in.readInt();
		this.conditionName = in.readString();
		this.detailCondition = in.readString();
		this.mSubDetailConditionItemList = in.readArrayList(ConditionItem.class.getClassLoader());
	}

//	public static final Creator<ShaiXuanConditionItem> CREATOR = new Creator<ShaiXuanConditionItem>() {
//		public ShaiXuanConditionItem createFromParcel(Parcel source) {
//			return new ShaiXuanConditionItem(source);
//		}
//
//		public ShaiXuanConditionItem[] newArray(int size) {
//			return new ShaiXuanConditionItem[size];
//		}
//	};
}
