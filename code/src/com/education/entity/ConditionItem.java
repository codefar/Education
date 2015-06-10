package com.education.entity;

public class ConditionItem {

	protected int conditionItemSelected;
	protected String detailConditionName;

	public ConditionItem(int imgSelected, String detailConditionName) {
		this.conditionItemSelected = imgSelected;
		this.detailConditionName = detailConditionName;
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
