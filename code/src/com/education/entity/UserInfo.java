package com.education.entity;

/**
 * Created by su on 15-6-7.
 */
public class UserInfo {
    /**
     * 登录时获取
     * */
    private String userId;
    private String userSession;


    /**
     * 获取考生信息时获取
     * */
    private String accountId;
    private String xm;
    private String sfzh;
    private int kscj;
    private int kspw;
    private int kskl;
    private String ksklName;
    private int kqdh; //考区代号
    private String kskqName; //考区名称

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public int getKscj() {
        return kscj;
    }

    public void setKscj(int kscj) {
        this.kscj = kscj;
    }

    public int getKspw() {
        return kspw;
    }

    public void setKspw(int kspw) {
        this.kspw = kspw;
    }

    public int getKskl() {
        return kskl;
    }

    public void setKskl(int kskl) {
        this.kskl = kskl;
    }

    public String getKsklName() {
        return ksklName;
    }

    public void setKsklName(String ksklName) {
        this.ksklName = ksklName;
    }

    public int getKqdh() {
        return kqdh;
    }

    public void setKqdh(int kqdh) {
        this.kqdh = kqdh;
    }

    public String getKskqName() {
        return kskqName;
    }

    public void setKskqName(String kskqName) {
        this.kskqName = kskqName;
    }
}
