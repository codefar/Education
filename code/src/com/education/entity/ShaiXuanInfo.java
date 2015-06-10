package com.education.entity;

import java.io.Serializable;
import java.util.List;

public class ShaiXuanInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<String> provinceList;
	private List<String> schoolTypeList;
	private List<String> majorList;
	private List<String> luquPiciList;

	public void setLuquPiciList(List<String> luquPiciList) {
		this.luquPiciList = luquPiciList;
	}

	public void setMajorList(List<String> majorList) {
		this.majorList = majorList;
	}

	public void setProvinceList(List<String> provinceList) {
		this.provinceList = provinceList;
	}

	public void setSchoolTypeList(List<String> schoolTypeList) {
		this.schoolTypeList = schoolTypeList;
	}

	public List<String> getLuquPiciList() {
		return luquPiciList;
	}

	public List<String> getMajorList() {
		return majorList;
	}

	public List<String> getProvinceList() {
		return provinceList;
	}

	public List<String> getSchoolTypeList() {
		return schoolTypeList;
	}

	@Override
	public String toString() {
		return "ShaiXuanInfo [provinceList=" + provinceList
				+ ", schoolTypeList=" + schoolTypeList + ", majorList="
				+ majorList + ", luquPiciList=" + luquPiciList + "]";
	}

}
