package com.education.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;
import com.android.volley.VolleyError;
import com.education.EduApp;
import com.education.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by su on 14-6-5.
 */
public class LogUtil {

    public static final boolean DEBUG = EduApp.DEBUG;
    private static final String TAG = "LogUtil";

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String MD5 = "MD5";

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String md5Hex(final String data) {
        return encodeHexString(md5(data));
    }

    public static byte[] md5(final String data) {
        return md5(getBytesUtf8(data));
    }

    public static byte[] md5(final byte[] data) {
        return getMd5Digest().digest(data);
    }
    public static MessageDigest getMd5Digest() {
        return getDigest(MD5);
    }

    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, UTF_8);
    }

    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    private static void logSeparators(boolean enable, boolean isStart) {
        if (enable) {
            if (isStart) {
                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            } else {
                Log.i(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
    }

    public static Cursor logCursorAllTitles(Cursor c) {
        if (c != null) {
            String[] names = c.getColumnNames();
            int length = names.length;
            for (int i = 0; i < length; i++) {
                Log.d(TAG, names[i]);
            }
        }
        return c;
    }

    public static void log(String s) {
        Log.v(TAG, s);
    }

    public static void logComponentEnabledSetting(PackageManager packageManager, Context context, Class<?>[] classes) {
        if (classes != null) {
            for (Class<?> clazz : classes) {
                int state = packageManager.getComponentEnabledSetting(new ComponentName(context, clazz.getName()));
                if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                    Log.d(TAG, clazz.getName() + " state = COMPONENT_ENABLED_STATE_DISABLED");
                } else if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER) {
                    Log.d(TAG, clazz.getName() + " state = COMPONENT_ENABLED_STATE_DISABLED_USER");
                } else if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    Log.d(TAG, clazz.getName() + " state = COMPONENT_ENABLED_STATE_ENABLED");
                } else {
                    Log.d(TAG, clazz.getName() + " state = COMPONENT_ENABLED_STATE_DEFAULT");
                }

            }
        }
    }

    public static Cursor logCursorWithTitle(Cursor c) {
        logSeparators(true, true);
        String separator = " | ";
        if (c != null) {
            String[] names = c.getColumnNames();
            int length = names.length;
            if (c.moveToFirst()) {
                do {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        sb.append(names[i]);
                        sb.append(": ");
                        sb.append(c.getString(i));
                        sb.append(separator);
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    Log.d(TAG, sb.toString());
                } while (c.moveToNext());
            }
            c.moveToFirst();
        }
        logSeparators(true, false);
        return c;
    }

    public static Cursor logCursorWithHeader(Cursor c) {
        logSeparators(true, true);
        String separator = " | ";
        if(c != null) {
            String[] names = c.getColumnNames();
            int length = names.length;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < length; i++) {
                sb.append(names[i]);
                sb.append(separator);
            }
            sb.deleteCharAt(sb.length() - 1);
            Log.e(TAG, sb.toString());
            if(c.moveToFirst()) {
                do {
                    sb = new StringBuilder();
                    for(int i = 0; i < length; i++) {
                        sb.append(c.getString(i));
                        sb.append(separator);
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    Log.d(TAG, sb.toString());
                } while (c.moveToNext());
            }
            c.moveToFirst();
        }
        logSeparators(true, false);
        return c;
    }

    public static boolean checkMobileString(String phoneNumber) {
        return phoneNumber.matches(Constants.MOBILE_REGEXP);
    }

    public static boolean checkEmailString(String email) {
        return email.matches("^.+@.+\\.\\w+$");
    }

    private static void modifyField(Class<?> clazz, Object o, String fieldName, Object v) {
        Field field = null;
        try {
            field = clazz.getField(fieldName);
            field.setAccessible(true);
            field.set(o, v);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void logTestIntent(Intent intent, Object o) {
        String[] fieldNames = intent.getStringArrayExtra("fieldNames");
        String[] fieldTypes = intent.getStringArrayExtra("fieldTypes");
        int length = fieldNames.length;
        if (length != fieldTypes.length) {
            return;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(o.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < length; i++) {
            String type = fieldTypes[i];
            String name = fieldNames[i];
            Object value = null;
            if (type.equalsIgnoreCase("boolean")) {
                value = intent.getBooleanExtra(fieldNames[i], false);
            } else if (type.equalsIgnoreCase("byte")) {
                value = intent.getByteExtra(fieldNames[i], (byte) 0);
            } else if (type.equalsIgnoreCase("char")) {
                value = intent.getCharExtra(fieldNames[i], (char) 0);
            } else if (type.equalsIgnoreCase("int")) {
                value = intent.getIntExtra(fieldNames[i], 0);
            }
            modifyField(clazz, o, name, value);
        }
    }

    public static void logNetworkResponse(VolleyError volleyError, String tag) {
        if (DEBUG) {
            Log.w(tag, "TAG", volleyError);
            if (volleyError.networkResponse != null) {
                Log.e(tag, "headers: " + volleyError.networkResponse.headers);
                Log.e(tag, "statusCode: " + volleyError.networkResponse.statusCode);
                Log.e(tag, "data: " + new String(volleyError.networkResponse.data));
            }
        }
    }

    public static void writeObject(Serializable s, String filePath) {
        writeObject(s, new File(filePath));
    }

    public static void writeObject(Serializable s, File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            oos.close();
        } catch (IOException e) {
            Log.e(TAG, "writeObject", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
