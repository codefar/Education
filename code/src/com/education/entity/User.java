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
                sUser.mName = sp.getString(Constants.SP_COLUMN_USER_NAME, "");
                sUser.mUserSession = sp.getString(Constants.SP_COLUMN_USER_SESSION, "");
                sUser.mLoginTime = sp.getLong(Constants.SP_COLUMN_USER_LOGIN_TIME, -1);
                sUser.mExpireTime = sp.getLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, -1);
                sUser.mPhoneNumber = sp.getLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, -1);
                sUser.mEmail = sp.getString(Constants.SP_COLUMN_USER_PHONE_EMAIL, "");
                sUser.setNickName(sp.getString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, ""));
            }
        } else {
            if (!TextUtils.isEmpty(sUser.mId) && !isValid()) {
                clearUser();
            }
        }

        return sUser;
    }

    private String mId;
    private String mName;
    private String mId5;
    private int mRank;
    private String mNickName;
    private String mUserSession = "";
    private long mLoginTime;
    private long mPhoneNumber;
    private String mEmail;
    private long mExpireTime;

    private String mProvince;
    private String mProvinceId;
    private float mScore;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id: " + mId + "\n");
        sb.append("Name: " + mName + "\n");
        sb.append("UserSession: " + mUserSession + "\n");
        sb.append("PhoneNumber: " + mPhoneNumber + "\n");
        sb.append("LoginTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mLoginTime)));
        sb.append("ExpireTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mExpireTime)));
        return sb.toString();
    }

    public float getScore() {
        return mScore;
    }

    public void setScore(float mScore) {
        this.mScore = mScore;
    }

    public String getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(String mProvinceId) {
        this.mProvinceId = mProvinceId;
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getId5() {
        return mId5;
    }

    public void setId5(String id5) {
        this.mId5 = id5;
    }

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        this.mRank = rank;
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

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;
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
                .putString(Constants.SP_COLUMN_USER_NAME, user.mName)
                .putString(Constants.SP_COLUMN_USER_SESSION, user.mUserSession)
                .putLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, user.mPhoneNumber)
                .putString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, user.mNickName)
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
                .putString(Constants.SP_COLUMN_USER_NAME, "")
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
