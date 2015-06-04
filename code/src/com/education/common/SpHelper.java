package com.souyidai.investment.android.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.souyidai.investment.android.Constants;

/**
 * Created by su on 15-3-30.
 */
public class SpHelper {

    private static SharedPreferences sDefaultSharedPreferences;

    public static void initSharedPreferences(Context context) {
        sDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setXAuthToken(String xAuthToken) {
        sDefaultSharedPreferences.edit().putString(Constants.SP_COLUMN_X_AUTH_TOKEN, xAuthToken).apply();
    }

    public static String getXAuthToken() {
        return sDefaultSharedPreferences.getString(Constants.SP_COLUMN_X_AUTH_TOKEN, "");
    }

}
