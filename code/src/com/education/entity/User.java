package com.education.entity;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.education.Constants;
import com.education.common.SpHelper;

import java.util.Date;

/**
 * Created by su on 2014/6/12.
 */
public class User {

    public static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "User";

    private static User sUser = null;

    private User() {}

    public static synchronized User getInstance() {
        if (sUser == null) {
            sUser = new User();
            SharedPreferences sp = SpHelper.sDefaultSharedPreferences;
            String userId = sp.getString(Constants.SP_COLUMN_USER_ID, "");
            //未登录
            if (TextUtils.isEmpty(userId)) {
                return sUser;
            } else {
                sUser.mId = sp.getString(Constants.SP_COLUMN_USER_ID, "");
                sUser.accountId = sp.getString(Constants.SP_COLUMN_USER_ACCOUNTID, "");
                sUser.xm = sp.getString(Constants.SP_COLUMN_USER_XM, "");
                sUser.sfzh = sp.getString(Constants.SP_COLUMN_USER_SFZH, "");
                sUser.kscj = sp.getFloat(Constants.SP_COLUMN_USER_KSCJ, -1);
                sUser.kspw = sp.getInt(Constants.SP_COLUMN_USER_KSPW, -1);
                sUser.kskl = sp.getInt(Constants.SP_COLUMN_USER_KSKL, -1);
                sUser.ksklName = sp.getString(Constants.SP_COLUMN_USER_KSKLNAME, "");
                sUser.kqdh = sp.getInt(Constants.SP_COLUMN_USER_KQDH, -1);
                sUser.kskqName = sp.getString(Constants.SP_COLUMN_USER_KSKQNAME, "");

                sUser.mUserSession = sp.getString(Constants.SP_COLUMN_USER_SESSION, "");
                sUser.mLoginTime = sp.getLong(Constants.SP_COLUMN_USER_LOGIN_TIME, -1);
                sUser.mExpireTime = sp.getLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, -1);
                sUser.mPhoneNumber = sp.getLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, -1);
                sUser.mEmail = sp.getString(Constants.SP_COLUMN_USER_PHONE_EMAIL, "");
            }
        } else {
            if (!TextUtils.isEmpty(sUser.mId) && !isValid()) {
                clearUser();
            }
        }

        return sUser;
    }

    private String mId;
    private String accountId; //昵称/账户
    private String xm; //姓名
    private String sfzh; //身份证号
    private float kscj; //成绩
    private int kspw; //排位
    private int kskl; //科类
    private String ksklName; //科类名称
    private int kqdh; //考区代号
    private String kskqName; //考区名称

    private String mUserSession = "";
    private long mLoginTime;
    private long mPhoneNumber;
    private String mEmail;
    private long mExpireTime;


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

    public float getKscj() {
        return kscj;
    }

    public void setKscj(float kscj) {
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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id: " + mId + "\n");
        sb.append("Name: " + xm + "\n");
        sb.append("UserSession: " + mUserSession + "\n");
        sb.append("PhoneNumber: " + mPhoneNumber + "\n");
        sb.append("LoginTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mLoginTime)));
        sb.append("ExpireTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mExpireTime)));
        return sb.toString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }


    public String getUserSession() {
        return mUserSession;
    }

    public void setUserSession(String userSession) {
        this.mUserSession = userSession;
    }

    public long getExpireTime() {
        return mExpireTime;
    }

    public void setExpireTime(long expireTime) {
        this.mExpireTime = expireTime;
    }

    public long getLoginTime() {
        return mLoginTime;
    }

    public void setLoginTime(long loginTime) {
        this.mLoginTime = loginTime;
    }

    public long getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public static synchronized void saveUser(User user) {
        SharedPreferences sp = SpHelper.sDefaultSharedPreferences;
        sp.edit().putString(Constants.SP_COLUMN_USER_ID, user.mId)
                .putString(Constants.SP_COLUMN_USER_ACCOUNTID, user.accountId)
                .putString(Constants.SP_COLUMN_USER_XM, user.xm)
                .putString(Constants.SP_COLUMN_USER_SFZH, user.sfzh)
                .putFloat(Constants.SP_COLUMN_USER_KSCJ, user.kscj)
                .putInt(Constants.SP_COLUMN_USER_KSPW, user.kspw)
                .putInt(Constants.SP_COLUMN_USER_KSKL, user.kskl)
                .putString(Constants.SP_COLUMN_USER_KSKLNAME, user.ksklName)
                .putInt(Constants.SP_COLUMN_USER_KQDH, user.kqdh)
                .putString(Constants.SP_COLUMN_USER_KSKQNAME, user.kskqName)

                .putString(Constants.SP_COLUMN_USER_SESSION, user.mUserSession)
                .putLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, user.mPhoneNumber)
                .putString(Constants.SP_COLUMN_USER_PHONE_EMAIL, user.mEmail)
                .putLong(Constants.SP_COLUMN_USER_LOGIN_TIME, user.mLoginTime)
                .putLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, user.mExpireTime)
                .putString(Constants.SP_COLUMN_USER_PROVINCE, "")
                .putString(Constants.SP_COLUMN_USER_PROVINCE_ID, "")
                .putFloat(Constants.SP_COLUMN_USER_SCORE, 0f)
        .apply();
    }

    public static synchronized void clearUser() {
        SharedPreferences sp = SpHelper.sDefaultSharedPreferences;
        sp.edit().putString(Constants.SP_COLUMN_USER_ID, "") //不删除temp_user_id，用于检测手势密码
                .putString(Constants.SP_COLUMN_USER_ACCOUNTID, "")
                .putString(Constants.SP_COLUMN_USER_XM, "")
                .putString(Constants.SP_COLUMN_USER_SFZH, "")
                .putFloat(Constants.SP_COLUMN_USER_KSCJ, -1)
                .putInt(Constants.SP_COLUMN_USER_KSPW, -1)
                .putInt(Constants.SP_COLUMN_USER_KSKL, -1)
                .putString(Constants.SP_COLUMN_USER_KSKLNAME, "")
                .putInt(Constants.SP_COLUMN_USER_KQDH, -1)
                .putString(Constants.SP_COLUMN_USER_KSKQNAME, "")

                .putString(Constants.SP_COLUMN_USER_SESSION, "")
                .putBoolean(Constants.SP_COLUMN_NICKNAME_STATUS, false)
                .putString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, "")
                .putString(Constants.SP_COLUMN_USER_PHONE_EMAIL, "")
                .putString(Constants.SP_COLUMN_USER_PHONE_ID5, "")
                .putString(Constants.SP_COLUMN_USER_PROVINCE, "")
                .putString(Constants.SP_COLUMN_USER_PROVINCE_ID, "")
                .putFloat(Constants.SP_COLUMN_USER_SCORE, 0f)
                .putLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, -1)
                .putLong(Constants.SP_COLUMN_USER_LOGIN_TIME, -1)
                .putLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, -1)
                .apply();
        clear();
    }

    private static void clear() {
        sUser = new User();
    }

    /**
     * 判断token是否过期，如果用户修改系统时间，此函数则失效。<br/>
     * 时间可以从服务器获取，如果网络可用的话
     * */
    public static boolean isValid() {
        if (sUser == null) {
            return false;
        }

        if (DEBUG) {
            Log.w(TAG, "mExpireTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS_SSS.format(new Date(sUser.mExpireTime)));
            Log.w(TAG, "currentTimeMillis: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS_SSS.format(new Date(System.currentTimeMillis())));
        }

//        return sUser.mExpireTime > System.currentTimeMillis();
        return true;
    }
}
