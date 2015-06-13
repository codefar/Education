package com.education.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.education.EduApp;
import com.education.LoginActivity;
import com.education.MainActivity;
import com.education.R;
import com.education.entity.ErrorData;
import com.education.entity.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * Created by su on 2014/8/20.
 */
public class AppHelper {

    private static final String TAG = "AppHelper";
    private static final boolean DEBUG = EduApp.DEBUG;

    public static Intent mainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static void startLauncher(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    /**
     * it's important to set up a notification, or the service would be killed by system.
     * */
    public static void keepServiceAlive(Service service) {
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        service.startForeground(99999, notification);
    }

    public static void stopServiceAlive(Service service) {
        service.stopForeground(true);
    }

    public static void enableAllReceiverAndService(Context context, Class<?>[] classes) {
        PackageManager pm = context.getPackageManager();
        setComponentEnabled(pm, context, classes, true);
    }

    public static void disableReceiverOrService(Context context, Class<?> clazz) {
        PackageManager pm = context.getPackageManager();
        final ComponentName c = new ComponentName(context, clazz.getName());
        pm.setComponentEnabledSetting(c, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static void setComponentEnabled(PackageManager packageManager, Context context, Class<?>[] classes, boolean enabled) {
        int length = classes.length;
        for (int i = 0; i < length; i++) {
            Class<?> clazz = classes[i];
            final ComponentName c = new ComponentName(context, clazz.getName());
            packageManager.setComponentEnabledSetting(c,
                    enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String fillNullNumberString(String s) {
        return  (s == null || s.equals("")) ? "0.00" : s;
    }

    public static boolean saveBitmap(Bitmap bitmap, String filename) {
        boolean status = true;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return status;
        }
    }

    public static void showLogoutDialog(final Context context) {
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(R.string.logout_dialog_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.clearUser();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("back_to_main", true);
                        context.startActivity(intent);
                    }
                })
                .show();
    }

    public static AlertDialog showTip(Context context, String tip) {
        if (TextUtils.isEmpty(tip)) {
            return null;
        }
        return new AlertDialog.Builder(context)
                .setMessage(tip)
                .setPositiveButton(R.string.known, null)
                .show();
    }

    public static Intent makeLogoutIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static ShapeDrawable getShapeDrawable(int color, int radius) {
        RoundRectShape roundRectShape = new RoundRectShape(new float[] { radius, radius, radius, radius, radius, radius, radius, radius }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public static LayerDrawable getBorderLayerDrawable(int color, int backgroundColor, int padding, int radius) {
        Drawable[] layers = new Drawable[2];
        layers[0] = getShapeDrawable(color, radius);
        layers[0].setState(new int[] {android.R.attr.state_enabled});
        layers[1] = getShapeDrawable(backgroundColor, radius);
        layers[1].setState(new int[] {android.R.attr.state_enabled});
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, padding, padding, padding, padding);
        return layerDrawable;
    }

    public static boolean isPhoneNumber(String s) {
        return Pattern.matches("1[3-9]\\d{9}", s);
    }

    public static void showLoadErrorLayout(Context context, FrameLayout fl, boolean show, View.OnClickListener onClickListener) {
        View v = fl.findViewById(R.id.load_error_layout);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.load_error, null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            fl.addView(v, lp);
        }
        if (show) {
            v.setVisibility(View.VISIBLE);
            v.setOnClickListener(onClickListener);
            Toast.makeText(context, R.string.loading_error, Toast.LENGTH_SHORT).show();
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static void showLoadErrorLayout(Activity activity, boolean show, View.OnClickListener onClickListener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        View v = activity.findViewById(R.id.load_error_layout);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.load_error, null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            activity.addContentView(v, lp);
        }
        if (show) {
            v.setVisibility(View.VISIBLE);
            v.setOnClickListener(onClickListener);
            Toast.makeText(activity, R.string.loading_error, Toast.LENGTH_SHORT).show();
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static void showLoadingLayout(Context context, FrameLayout fl, boolean show) {
        View v = fl.findViewById(R.id.progress_bar_layout);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.loading, null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            fl.addView(v, lp);
        }
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static void showLoadingLayout(Activity activity, boolean show) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        View bgView = activity.findViewById(R.id.main_layout);
        View v = activity.findViewById(R.id.progress_bar_layout);
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.loading, null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            activity.addContentView(v, lp);
        }

        if (show) {
            v.setVisibility(View.VISIBLE);
            if (bgView != null) {
                bgView.setVisibility(View.GONE);
            }
        } else {
            if (bgView != null) {
                bgView.setVisibility(View.VISIBLE);
            }
            v.setVisibility(View.GONE);
        }
    }

    public static String formatTimeToHMS(long time) {
        int h = (int) (time / (60 * 60 * 1000));
        time = time - 60 * 60 * 1000 * h;
        int m = (int) (time / (60 * 1000));
        time = time - 60 * 1000 * m;
        int s = (int) (time / 1000);
        return h + "时" + ((m < 10) ? ("0" + m) : m) + "分" + ((s < 10) ? ("0" + s) : s) + "秒";
    }

    public static boolean hasNewVersion(String oldVersion, String newVersion) {
        newVersion = "".equals(newVersion) ? "1" : newVersion;
        String[] olds = oldVersion.split("\\.");
        String[] news = newVersion.split("\\.");
        int length = olds.length > news.length ? news.length : olds.length;
        for(int i = 0; i < length; i++) {
            if (Integer.parseInt(olds[i]) > Integer.parseInt(news[i])) {
                return false;
            } else if (Integer.parseInt(olds[i]) < Integer.parseInt(news[i])) {
                return true;
            }
        }

        if (olds.length == news.length) {
            return false;
        } else if (olds.length < news.length) {
            for (int i = olds.length; i < news.length; i++) {
                if (Integer.parseInt(news[i]) > 0) {
                    return true;
                }
            }
        } else {
            for (int i = news.length; i < olds.length; i++) {
                if (Integer.parseInt(olds[i]) > 0) {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isSupportAccelerometerSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
    }

    public static String formatBankCardNumbers(String number) {
        if (number.length() == 16) {
            return number.substring(0, 4) + " **** **** " + number.substring(13, 16);
        } else {
            return number;
        }
    }

    public static String encodeString(String str) {
        if (str == null) {
            return null;
        }
        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * provide by umeng
     * http://www.umeng.com/test_devices
     * */
    public static void logUMengDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if(TextUtils.isEmpty(deviceId)){
                deviceId = mac;
            }

            if(TextUtils.isEmpty(deviceId)){
                deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", deviceId);
            Log.d("UMENG", "Umeng_id: " + json);
        }catch(Exception e){
            //ignore
        }
    }

    public static String setParamForUrl(String url, Map<String, String> map) {
        if (map == null) {
            return url;
        }

        Uri uri = Uri.parse(url);
        String query = uri.getEncodedQuery();
        String urlHead = url.replace(query == null ? "" : "?" + query, "");
        Set<String> set = uri.getQueryParameterNames();
        Set<String> keySet = map.keySet();
        int i = 0;
        for (String paramKey : keySet) {
            i++;
            if (i == 1) {
                urlHead += "?" + paramKey + "=" + map.get(paramKey);
            } else {
                urlHead += "&" + paramKey + "=" + map.get(paramKey);
            }
        }

        try {
            for (String originParamKey : set) {
                if (map.get(originParamKey) != null) {
                    continue;
                }
                i++;

                String queryParameter = URLEncoder.encode(uri.getQueryParameter(originParamKey), "UTF-8");
                if (i == 1) {
                    urlHead += "?" + originParamKey + "=" + queryParameter;
                } else {
                    urlHead += "&" + originParamKey + "=" + queryParameter;
                }
            }
        } catch (UnsupportedEncodingException e) {
            if (EduApp.DEBUG) {
                Log.e("SydApp", "setParamForUrl error!", e);
            }
            return url;
        }
        Log.d("NEW_URL", "url: " + urlHead);
        map.clear();
        return urlHead;
    }

    public static int[] getTimeArray(long time) {
        int[] array = new int[4];
        int h = (int) (time / (60 * 60 * 1000));
        time = time - 60 * 60 * 1000 * h;
        int m = (int) (time / (60 * 1000));
        time = time - 60 * 1000 * m;
        int s = (int) (time / 1000);
        int ms = (int) (time - s * 1000);

        array[0] = h;
        array[1] = m;
        array[2] = s;
        array[3] = ms;
        return array;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        if (EduApp.DEBUG) {
            builder.writeDebugLogs();
        }
        ImageLoaderConfiguration config = builder.build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static Map<String, String> makeSimpleData(String request, Map<String, String> data) {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        if (data != null) {
            Set<String> set = data.keySet();
            JSONObject params = new JSONObject();
            for (String key : set) {
                params.put(key, data.get(key));
            }
            jsonObject.put("params", params);
        }
        jsonObject.put("request", request);
        map.put("userData", jsonObject.toJSONString());
        if (EduApp.DEBUG) {
            Log.d(TAG, "map: " + map);
        }
        return map;
    }

    public static ErrorData getErrorData(JSONObject response) {
        String data = response.getString("error");
        return JSON.parseObject(data, ErrorData.class);
    }
}
