package com.souyidai.investment.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.souyidai.investment.android.common.AppHelper;
import com.souyidai.investment.android.common.PhoneService;
import com.souyidai.investment.android.common.SpHelper;
import com.souyidai.investment.android.component.lock.LockPatternUtils;
import com.souyidai.investment.android.service.LongService;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * Created by su on 2014/9/15.
 */
public class EduApp extends Application {

    public static final boolean DEBUG = true;
    public static final String TAG = "SydApp";
    public static final String MOBILE_MD5_SIGN = "j7dAuXMhpE76LRrETe8bTQ";

    public static RequestQueue sRequestQueue;
    public static String sHost;
    public static String sPassportHost;
    public static String sPassportRootHost;
    public static String sVersionName;
    public static int sVersionCode;
    public static String sPackageName;
    public static int sScreenWidth;
    public static int sScreenHeight;
    public static String sApplicationLabel;
    private static boolean sInit;

    public static final String sRootDirPath;
    public static final String sTempDirPath;
    public static final String sPicDirPath;
    public static String sDeviceId;
    public static String sUMengChannel;

    public static final String SP_COLUMN_NEED_TO_UPDATE = "need_to_update";
    public static final String SP_COLUMN_FORCE_TO_UPDATE = "force_to_update";
    public static final String SP_COLUMN_NEW_VERSION_APK_NAME = "new_version_apk_name";
    public static final String SP_COLUMN_NEW_VERSION = "new_version";
    public static final String SP_COLUMN_NEW_VERSION_URL = "new_version_url";
    public static final String SP_COLUMN_NEW_VERSION_DESCRIPTION = "new_version_description";
    public static final String SP_COLUMN_NEW_VERSION_TITLE = "new_version_title";

    public static boolean sIsInBackground = true;

    public static final String WX_APP_ID = "wxece2d80f3747bc52";
    private IWXAPI mWxApi;

    static {
        File externalStorage = Environment.getExternalStorageDirectory();
        String prefix = externalStorage.getAbsolutePath() + File.separator;
        sRootDirPath = prefix + Constants.APP_DIR_ROOT;
        File rootDirFile = new File(sRootDirPath);
        if (!rootDirFile.exists()) {
            rootDirFile.mkdir();
        } else {
            if (!rootDirFile.isDirectory()) {
                if (rootDirFile.delete()) {
                    rootDirFile.mkdir();
                }
            }
        }

        sTempDirPath = prefix + Constants.APP_DIR_TEMP;
        File tempDirFile = new File(sTempDirPath);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdir();
        } else {
            if (!tempDirFile.isDirectory()) {
                if (tempDirFile.delete()) {
                    tempDirFile.mkdir();
                }
            }
        }

        sPicDirPath = prefix + Constants.APP_DIR_PIC;
        File picDirFile = new File(sPicDirPath);
        if (!picDirFile.exists()) {
            picDirFile.mkdir();
        } else {
            if (!picDirFile.isDirectory()) {
                if (picDirFile.delete()) {
                    picDirFile.mkdir();
                }
            }
        }
    }

    private void regToWx() {
        mWxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        mWxApi.registerApp(WX_APP_ID);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SpHelper.initSharedPreferences(this);
        if (DEBUG) {
            AppHelper.logUMengDeviceInfo(this);
        }
        final SharedPreferences defaultSP = PreferenceManager.getDefaultSharedPreferences(this);
        sHost = defaultSP.getString(Constants.SP_COLUMN_ENVIRONMENT, "https://app.souyidai.com/app/");
        sPassportHost = "https://passport.souyidai.com/app/";
        sPassportRootHost = "https://passport.souyidai.com/";
        AppHelper.getNewHttpClient();

        sDeviceId = PhoneService.getUUID(this);
        if (DEBUG) {
            Log.v(TAG, "SydApp is creating...");
            MobclickAgent.setDebugMode(true);
        }
        sRequestQueue = Volley.newRequestQueue(this);
        //禁止自动统计页面跳转
        //使用MobclickAgent.onPageStart、MobclickAgent.onPageEnd统计页面跳转
        MobclickAgent.openActivityDurationTrack(false);

        JPushInterface.setDebugMode(DEBUG);
        JPushInterface.init(this);

        if (sVersionName == null) {
            try {
                PackageManager pm = getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
                sVersionName = pi.versionName;
                sVersionCode = pi.versionCode;
                sPackageName = pi.packageName;
                ApplicationInfo applicationInfo = pm.getApplicationInfo(getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                sApplicationLabel = pm.getApplicationLabel(applicationInfo).toString();
                ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                sUMengChannel = appInfo.metaData.getString("UMENG_CHANNEL");
                if (sUMengChannel == null || "".equals(sUMengChannel)) {
                    sUMengChannel = String.valueOf(appInfo.metaData.getInt("UMENG_CHANNEL"));
                }
            } catch (PackageManager.NameNotFoundException e) {
                if (DEBUG) {
                    Log.d(TAG, "onCreate: " + e.getMessage());
                }
            }
        }

        sInit = defaultSP.getBoolean(Constants.SP_COLUMN_INIT, true);
        if (sInit) {
            defaultSP.edit().putBoolean(Constants.SP_COLUMN_INIT, false).apply();
        }
        
        String userId = defaultSP.getString(Constants.SP_COLUMN_USER_ID, "-1");
        if (Long.parseLong(userId) > 0) {
            AppHelper.bindJpush(this, "T_" + userId, true);
        } else {
            AppHelper.bindJpush(this, "T_" + PhoneService.getUUID(this), true);
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (size.x < size.y) {
            sScreenWidth = size.x;
            sScreenHeight = size.y;
        } else {
            sScreenWidth = size.y;
            sScreenHeight = size.x;
        }

        regToWx();

        AppHelper.initImageLoader(this);
        AppHelper.createGestureFileIfNotExist(this);
        AppHelper.createPasswordFileIfNotExist(this);

        new Thread() {
            public void run() {
                new LockPatternUtils(EduApp.this);

                boolean patternPasswordEnable = defaultSP.getBoolean(Constants.SP_COLUMN_PATTERN_PASSWORD_ENABLE, false);
                if (patternPasswordEnable && !LockPatternUtils.checkPattern(null)) {
                    AppHelper.enableAllReceiverAndService(EduApp.this, new Class<?>[]{ LongService.class });
                    startService(new Intent(EduApp.this, LongService.class));
                }
            }
        }.start();
    }
}
