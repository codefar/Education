package com.education.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-11.
 */
public class ShaiXuanJieGuo {

    private int totalPage;
    private int curPage;
    private List<CollegeItem> yxzydata = new ArrayList<CollegeItem>();

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<CollegeItem> getYxzydata() {
        return yxzydata;
    }

    public void setYxzydata(List<CollegeItem> yxzydata) {
        this.yxzydata = yxzydata;
    }

	@Override
	public String toString() {
		return "ShaiXuanJieGuo [totalPage=" + totalPage + ", curPage="
				+ curPage + ", yxzydata=" + yxzydata + "]";
	}
    
    
}
