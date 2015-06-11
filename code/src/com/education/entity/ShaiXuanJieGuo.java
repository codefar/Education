package com.education.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-11.
 */
public class ShaiXuanJieGuo {

    private int totalPage;
    private int curPage;
    private List<Item> yxzydata = new ArrayList<Item>();

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

    public List<Item> getYxzydata() {
        return yxzydata;
    }

    public void setYxzydata(List<Item> yxzydata) {
        this.yxzydata = yxzydata;
    }

    public static class Item {
        private String yxdh; //院校代号
        private String yxmc; //院校名称
        private String zysl; //符合筛选条件的专业数量

        public String getYxdh() {
            return yxdh;
        }

        public void setYxdh(String yxdh) {
            this.yxdh = yxdh;
        }

        public String getYxmc() {
            return yxmc;
        }

        public void setYxmc(String yxmc) {
            this.yxmc = yxmc;
        }

        public String getZysl() {
            return zysl;
        }

        public void setZysl(String zysl) {
            this.zysl = zysl;
        }
    }
}
