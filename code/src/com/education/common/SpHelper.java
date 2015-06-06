package com.education.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.education.Constants;

/**
 * Created by su on 15-3-30.
 */
public class SpHelper {

    public static SharedPreferences sDefaultSharedPreferences;

    public static void initSharedPreferences(Context context) {
        sDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

}
