package com.education.entity;

/**
 * Created by su on 15-6-11.
 */
public class MajorItem {
    private String zydh; //专业代号
    private String zymc; //专业名称
    private String lqpc; //录取批次
    private int source; //收藏来源

    private int lqrs; //录取人数
    private int min; //录取成绩最低
    private int max; //录取成绩最高
    private int pjz; //录取成绩平均值
    private String lqgl;

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

    public int getLqrs() {
        return lqrs;
    }

    public void setLqrs(int lqrs) {
        this.lqrs = lqrs;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getPjz() {
        return pjz;
    }

    public void setPjz(int pjz) {
        this.pjz = pjz;
    }

    public String getLqgl() {
        return lqgl;
    }

    public void setLqgl(String lqgl) {
        this.lqgl = lqgl;
    }

	@Override
	public String toString() {
		return "MajorItem [zydh=" + zydh + ", zymc=" + zymc + ", lqpc=" + lqpc
				+ ", source=" + source + ", lqrs=" + lqrs + ", min=" + min
				+ ", max=" + max + ", pjz=" + pjz + ", lqgl=" + lqgl + "]";
	}
    
    
}
