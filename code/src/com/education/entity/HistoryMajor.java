package com.education.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-12.
 */
public class HistoryMajor {
    
    private int year; //年份
    private int lqline; //录取分数线
    private int lqrs; //录取人数
    private int mincj; //最低分
    private int maxcj; //最高分
    private int pjcj; //平均分
    private int minpw; //最低排位
    private int maxpw; //最高排位
    private int pjpw; //平均排位
    private int minxc; //最低线差
    private int maxxc; //最高线差
    private int pjxc; //成绩列表
    private List<Integer> cjlist = new ArrayList<Integer>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLqline() {
        return lqline;
    }

    public void setLqline(int lqline) {
        this.lqline = lqline;
    }

    public int getLqrs() {
        return lqrs;
    }

    public void setLqrs(int lqrs) {
        this.lqrs = lqrs;
    }

    public int getMincj() {
        return mincj;
    }

    public void setMincj(int mincj) {
        this.mincj = mincj;
    }

    public int getMaxcj() {
        return maxcj;
    }

    public void setMaxcj(int maxcj) {
        this.maxcj = maxcj;
    }

    public int getPjcj() {
        return pjcj;
    }

    public void setPjcj(int pjcj) {
        this.pjcj = pjcj;
    }

    public int getMinpw() {
        return minpw;
    }

    public void setMinpw(int minpw) {
        this.minpw = minpw;
    }

    public int getMaxpw() {
        return maxpw;
    }

    public void setMaxpw(int maxpw) {
        this.maxpw = maxpw;
    }

    public int getPjpw() {
        return pjpw;
    }

    public void setPjpw(int pjpw) {
        this.pjpw = pjpw;
    }

    public int getMinxc() {
        return minxc;
    }

    public void setMinxc(int minxc) {
        this.minxc = minxc;
    }

    public int getMaxxc() {
        return maxxc;
    }

    public void setMaxxc(int maxxc) {
        this.maxxc = maxxc;
    }

    public int getPjxc() {
        return pjxc;
    }

    public void setPjxc(int pjxc) {
        this.pjxc = pjxc;
    }

    public List<Integer> getCjlist() {
        return cjlist;
    }

    public void setCjlist(List<Integer> cjlist) {
        this.cjlist = cjlist;
    }
}
