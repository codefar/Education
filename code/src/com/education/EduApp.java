package com.education;

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
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.education.common.AppHelper;
import com.education.common.PhoneService;
import com.education.common.SpHelper;

import java.io.File;

/**
 * Created by su on 2014/9/15.
 */
public class EduApp extends Application {

    public static final boolean DEBUG = true;
    public static final String TAG = "SydApp";

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
    public static String sDeviceId;
    private static boolean sInit;

    public static final String sRootDirPath;
    public static final String sTempDirPath;
    public static final String sPicDirPath;

    public static final String SP_COLUMN_NEED_TO_UPDATE = "need_to_update";
    public static final String SP_COLUMN_FORCE_TO_UPDATE = "force_to_update";
    public static final String SP_COLUMN_NEW_VERSION_APK_NAME = "new_version_apk_name";
    public static final String SP_COLUMN_NEW_VERSION = "new_version";
    public static final String SP_COLUMN_NEW_VERSION_URL = "new_version_url";
    public static final String SP_COLUMN_NEW_VERSION_DESCRIPTION = "new_version_description";
    public static final String SP_COLUMN_NEW_VERSION_TITLE = "new_version_title";

    public static boolean sIsInBackground = true;

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

    @Override
    public void onCreate() {
        super.onCreate();
        SpHelper.initSharedPreferences(this);
        if (DEBUG) {
            AppHelper.logUMengDeviceInfo(this);
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        sDeviceId = tm.getDeviceId();
        final SharedPreferences defaultSP = PreferenceManager.getDefaultSharedPreferences(this);
        sHost = defaultSP.getString(Constants.SP_COLUMN_ENVIRONMENT, "https://app.souyidai.com/app/");
        sPassportHost = "https://passport.souyidai.com/app/";
        sPassportRootHost = "https://passport.souyidai.com/";
        AppHelper.getNewHttpClient();

        sDeviceId = PhoneService.getUUID(this);
        if (DEBUG) {
            Log.v(TAG, "SydApp is creating...");
        }
        sRequestQueue = Volley.newRequestQueue(this);
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

        AppHelper.initImageLoader(this);
        AppHelper.createGestureFileIfNotExist(this);
        AppHelper.createPasswordFileIfNotExist(this);
    }
}
