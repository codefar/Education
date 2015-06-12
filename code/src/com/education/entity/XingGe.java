package com.education.entity;

/**
 * Created by su on 15-6-12.
 */
public class XingGe {
//    "xm":"语言智能","score":80,"pj":"完全符合"
    private String xm; //项目
    private int score; //测试得分
    private String pj; //评价

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
    }
}
