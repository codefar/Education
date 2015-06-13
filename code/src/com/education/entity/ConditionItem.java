package com.education.entity;

import com.education.R;

public class ConditionItem {

	protected int conditionItemSelected=R.drawable.arrow_right;
	protected String detailConditionName;
	protected String proviceId;

	
	public ConditionItem(String detailConditionName,
			String proviceId) {
		super();
		this.detailConditionName = detailConditionName;
		this.proviceId = proviceId;
	}
	public void setProviceId(String proviceId) {
		this.proviceId = proviceId;
	}
	public String getProviceId() {
		return proviceId;
	}

	public ConditionItem(int conditionItemSelected, String detailConditionName,
			String proviceId) {
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

}
