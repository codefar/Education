package com.education.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.education.Constants;
import com.education.common.AppHelper;
import com.education.common.PhoneService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by su on 2014/6/12.
 */
public class User {

    public static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "User";

    private static User sUser = null;

    private User() {}

    public static synchronized User getInstance(Context context) {
        if (sUser == null) {
            sUser = new User();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String wayToLogin = sp.getString(Constants.SP_COLUMN_WAY_TO_LOGIN, "");
            Log.v(TAG, "wayToLogin: " + wayToLogin);
            //未登录
            if (wayToLogin.equals("")) {
                return sUser;
            } else {
                if (wayToLogin.equals(LoginWay.SYD.name())) {
                    sUser.mWayToLogin = LoginWay.SYD;
                } else if (wayToLogin.equals(LoginWay.QQ.name())) {
                    sUser.mWayToLogin = LoginWay.QQ;
                } else if (wayToLogin.equals(LoginWay.WEIBO.name())) {
                    sUser.mWayToLogin = LoginWay.WEIBO;
                }
                sUser.mId = Integer.parseInt(sp.getString(Constants.SP_COLUMN_USER_ID, "-1"));
                sUser.mName = sp.getString(Constants.SP_COLUMN_USER_NAME, "");
                sUser.mToken = sp.getString(Constants.SP_COLUMN_USER_TOKEN, "");
                sUser.mLoginTime = sp.getLong(Constants.SP_COLUMN_USER_LOGIN_TIME, -1);
                sUser.mExpireTime = sp.getLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, -1);
                sUser.mPhoneNumber = sp.getLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, -1);
                sUser.mEmail = sp.getString(Constants.SP_COLUMN_USER_PHONE_EMAIL, "");
                sUser.mId5 = sp.getString(Constants.SP_COLUMN_USER_PHONE_ID5, "");
//                sUser.mDisplayName = sp.getString(Constants.SP_COLUMN_USER_DISPLAY_NAME, "");
                sUser.setNickName(sp.getString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, ""));
            }
        } else {
            if (sUser.mWayToLogin != null && !isValid()) {
                clearUser(context);
            }
        }

        return sUser;
    }

    public static enum LoginWay {
        SYD,
        QQ,
        WEIBO
    }

    private LoginWay mWayToLogin = null;
    private long mId;
    private String mId5;
    private String mName;
    private String mRealName;
    private String mNickName;
    private String mDisplayName;
    private String mToken = "";
    private long mLoginTime;
    private long mPhoneNumber;
    private String mEmail;
    private long mExpireTime;

    private long mCurrentBalance; //可用余额
    private long mBlockedBalance; //冻结余额
    private long mGainAmount; //已收利息
    private long mWillAmount; //待收本息
    private long mCouponAmount; //可用红包金额
    private long mCouponLockedAmount; //冻结红包金额
    private long mWillRepayAmount; //待还本息
    private int mWeight; //总加权收益

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (mWayToLogin != null) {
            sb.append("WayToLogin: " + mWayToLogin.name() + "\n");
        }
        sb.append("Id: " + mId + "\n");
        sb.append("Name: " + mName + "\n");
        sb.append("Token: " + mToken + "\n");
        sb.append("PhoneNumber: " + mPhoneNumber + "\n");
        sb.append("LoginTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mLoginTime)));
        sb.append("ExpireTime: " + Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date(mExpireTime)));
        return sb.toString();
    }

    public LoginWay getWayToLogin() {
        return mWayToLogin;
    }

    public void setWayToLogin(LoginWay wayToLogin) {
        this.mWayToLogin = wayToLogin;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public String getEncodedToken() {
        String encodedToken = "";
        try {
            encodedToken = URLEncoder.encode(mToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedToken;
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

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            try {
                this.mPhoneNumber = Long.parseLong(phoneNumber.replaceAll("^\\+0?86", ""));
                if (mDisplayName == null || "".equals(mDisplayName)) {
                    mDisplayName = hideName(String.valueOf(mPhoneNumber), 3, 4);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "wrong cell number! NUMBER = " + phoneNumber);
            }
        }
    }

    public String getId5() {
        return mId5;
    }

    public void setId5(String id5) {
        this.mId5 = id5;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;
        mDisplayName = hideName(nickName, 1, 1);
        if ("".equals(mDisplayName)) {
            mDisplayName = hideName(String.valueOf(mPhoneNumber), 3, 3);
        }
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public static String hideName(String name, int first, int last) {
        String hideName = "";
        if (name != null) {
            int length = name.length();
            for (int i = 0; i < length; i++) {
                if (i < first || i > length - last - 1) {
                    hideName += String.valueOf(name.charAt(i));
                } else {
                    hideName += "*";
                }
            }
        }
        return hideName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public long getCurrentBalance() {
        return mCurrentBalance;
    }

    public String getCurrentBalanceText() {
        return Constants.FORMAT_AMOUNT.format(mCurrentBalance / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setCurrentBalance(long currentBalance) {
        this.mCurrentBalance = currentBalance;
    }

    public long getBlockedBalance() {
        return mBlockedBalance;
    }

    public String getBlockedBalanceText() {
        return Constants.FORMAT_AMOUNT.format(mBlockedBalance / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setBlockedBalance(long blockedBalance) {
        this.mBlockedBalance = blockedBalance;
    }

    public long getGainAmount() {
        return mGainAmount;
    }

    public String getGainAmountText() {
        return Constants.FORMAT_AMOUNT.format(mGainAmount / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setGainAmount(long gainAmount) {
        this.mGainAmount = gainAmount;
    }

    public long getWillAmount() {
        return mWillAmount;
    }

    public String getWillAmountText() {
        return Constants.FORMAT_AMOUNT.format(mWillAmount / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setWillAmount(long willAmount) {
        this.mWillAmount = willAmount;
    }

    public long getCouponAmount() {
        return mCouponAmount;
    }

    public String getCouponAmountText() {
        return Constants.FORMAT_AMOUNT.format(mCouponAmount / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setCouponAmount(long couponAmount) {
        this.mCouponAmount = couponAmount;
    }

    public long getCouponLockedAmount() {
        return mCouponLockedAmount;
    }

    public void setCouponLockedAmount(long couponLockedAmount) {
        this.mCouponLockedAmount = couponLockedAmount;
    }

    public String getCouponLockedAmountText() {
        return Constants.FORMAT_AMOUNT.format(mCouponLockedAmount / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public long getWillRepayAmount() {
        return mWillRepayAmount;
    }

    public String getWillRepayAmountText() {
        return Constants.FORMAT_AMOUNT.format(mWillRepayAmount / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public void setWillRepayAmount(long willRepayAmount) {
        this.mWillRepayAmount = willRepayAmount;
    }

    public long getAllBalance() {
        return mCurrentBalance + mBlockedBalance;
    }

    public String getAllBalanceText() {
        return Constants.FORMAT_AMOUNT.format((mCurrentBalance + mBlockedBalance) / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public String getAllAssetText() {
        //账户余额（可用余额 + 冻结余额） + 待收本息 + 可用红包 + 冻结红包 + 待还本息
        return Constants.FORMAT_AMOUNT.format((mCurrentBalance + mBlockedBalance + mWillAmount + mCouponAmount - mWillRepayAmount + mCouponLockedAmount) / (double) 100).replaceAll("(\\d)(?=(?:\\d{3})+(?:\\.\\d+)?$)", "$1,");
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        this.mWeight = weight;
    }

    public String getWeightText() {
        return Constants.FORMAT_AMOUNT.format(mWeight / (double) 100);
    }

    public static synchronized void saveUser(Context context, User user) {
        //不要保存SP_COLUMN_USER_BIND_JPUSH字段
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(Constants.SP_COLUMN_WAY_TO_LOGIN, user.mWayToLogin.name())
                .putString(Constants.SP_COLUMN_USER_ID, String.valueOf(user.mId))
                .putString(Constants.SP_COLUMN_USER_NAME, user.mName)
                .putString(Constants.SP_COLUMN_USER_TOKEN, user.mToken)
                .putLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, user.mPhoneNumber)
                .putString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, user.mNickName)
                .putString(Constants.SP_COLUMN_USER_PHONE_EMAIL, user.mEmail)
                .putString(Constants.SP_COLUMN_USER_PHONE_ID5, user.mId5)
                .putLong(Constants.SP_COLUMN_USER_LOGIN_TIME, user.mLoginTime)
                .putLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, user.mExpireTime)
        .apply();
        onSaveUser(context);
    }

    private static void onSaveUser(Context context) {

    }

    public static synchronized void clearUser(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(Constants.SP_COLUMN_WAY_TO_LOGIN, "")
                .putString(Constants.SP_COLUMN_USER_ID, "-1") //不删除temp_user_id，用于检测手势密码
                .putString(Constants.SP_COLUMN_USER_NAME, "")
                .putString(Constants.SP_COLUMN_USER_REAL_NAME, "")
                .putString(Constants.SP_COLUMN_USER_TOKEN, "")
                .putBoolean(Constants.SP_COLUMN_NICKNAME_STATUS, false)
                .putString(Constants.SP_COLUMN_USER_PHONE_NICKNAME, "")
                .putString(Constants.SP_COLUMN_USER_PHONE_EMAIL, "")
                .putString(Constants.SP_COLUMN_USER_PHONE_ID5, "")
                .putLong(Constants.SP_COLUMN_USER_PHONE_NUMBER, -1)
                .putLong(Constants.SP_COLUMN_USER_LOGIN_TIME, -1)
//                .putLong(Constants.SP_COLUMN_DEFAULT_TRANSACTION_AMOUNT, context.getResources().getInteger(R.integer.default_transaction_amount))
                .putLong(Constants.SP_COLUMN_USER_EXPIRE_TIME, -1)
                .putBoolean(Constants.SP_COLUMN_USER_BIND_JPUSH, false)
//                .putBoolean(Constants.SP_COLUMN_PATTERN_PASSWORD_ENABLE, false)
                .putInt(Constants.SP_COLUMN_REAL_NAME_AUTHENTICATION_STATUS, 0)
                .putString(Constants.SP_COLUMN_REAL_NAME, "")
                .putInt(Constants.SP_COLUMN_WRONG_TIMES, 0)
                .putLong(Constants.SP_COLUMN_WRONG_DATE, System.currentTimeMillis())
                .putString(Constants.SP_COLUMN_QUESTION_ID, "")
                .putString(Constants.SP_COLUMN_X_AUTH_TOKEN, "")
                .apply();
        clear();
        Log.w(TAG, "clearUser!!", new Throwable());
        new Thread(){
            public void run() {
                removeAllUserFiles(context);
            }
        }.start();
    }

    private static void removeAllUserFiles(Context context) {
        if (DEBUG) {
            Log.w(TAG, "removeAllUserFiles!!");
        }
//        new File(context.getFilesDir().getAbsolutePath() + "/" + Constants.LOCK_PATTERN_FILE).delete(); //不删除手势密码，以便下次同一用户登录使用
        new File(context.getFilesDir().getAbsolutePath() + "/" + Constants.FILE_FORMAL_UMENG_FEEDBACK).delete(); //友盟反馈
        new File(context.getFilesDir().getAbsolutePath() + "/" + Constants.FILE_FORMAL_UMENG_CACHE).delete(); //友盟缓存
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

        return sUser.mExpireTime > System.currentTimeMillis();
    }
}
