package com.souyidai.investment.android.common;

import android.os.Build;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.souyidai.investment.android.Constants;
import com.souyidai.investment.android.SydApp;
import com.souyidai.investment.android.utils.LogUtil;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by su on 2014/9/25.
 */
public class FastJsonRequest<T> extends Request<T> {
    private static final String TAG = "FastJsonRequest";
    private final Class<T> mClazz;
    private final Listener<T> mListener;
    private Map<String, String> mHeaders;
    private String mUrl;

    public FastJsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, listener, errorListener);
    }

    public FastJsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(method, url, clazz, null, listener, errorListener);
    }

    public FastJsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                           Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mUrl = url;
        this.mClazz = clazz;
        this.mHeaders = headers;
        if (mHeaders == null) {
            mHeaders = new HashMap<String, String>();
        }
        mHeaders.put("user-agent", "Android/" + SydApp.sVersionName
                + " (" + SydApp.sPackageName + ";" + Build.VERSION.RELEASE + ";" + SydApp.sScreenWidth + ";" + SydApp.sScreenHeight + ";" + Build.MANUFACTURER + ";" + Build.MODEL + ")");
        URI uri = URI.create(mUrl);
        mHeaders.put("authToken", LogUtil.md5Hex(Constants.AUTH_TOKEN_SECRET_KEY + uri.getPath()));
        mHeaders.put("deviceId", SydApp.sDeviceId);
        mHeaders.put("umeng_channel", SydApp.sUMengChannel);
        String xAuthToken = SpHelper.getXAuthToken();
        if (xAuthToken != null && !xAuthToken.equals("")) {
            mHeaders.put(Constants.SP_COLUMN_X_AUTH_TOKEN, xAuthToken);
        }
        this.mListener = listener;

        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json = new String(response.data, Charset.forName("UTF-8"));
        Map<String, String> headers = response.headers;
        String xAuthToken = headers.get(Constants.SP_COLUMN_X_AUTH_TOKEN);
        if (xAuthToken != null && !xAuthToken.equals("")) {
            SpHelper.setXAuthToken(xAuthToken);
        }
//        if (SydApp.DEBUG) {
//            Log.d("json_data", "json: \n" + new String(response.data));
//        }
        return Response.success(JSON.parseObject(json, mClazz),
                HttpHeaderParser.parseCacheHeaders(response));
    }
}