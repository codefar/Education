package com.education.entity;

/**
 * Created by su on 15-6-11.
 */
public class MajorItem {
    private String zydh; //专业代号
    private String zymc; //专业名称
    private String lqpc; //录取批次
    private int source; //收藏来源

    public String getZydh() {
        return zydh;
    }

    public void setZydh(String zydh) {
        this.zydh = zydh;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }

    public String getLqpc() {
        return lqpc;
    }

    public void setLqpc(String lqpc) {
        this.lqpc = lqpc;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
