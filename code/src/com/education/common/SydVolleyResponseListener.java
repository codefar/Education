package com.souyidai.investment.android.common;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.souyidai.investment.android.R;
import com.souyidai.investment.android.SydApp;

/**
 * Created by su on 15-3-30.
 */
public abstract class SydVolleyResponseListener implements Response.Listener<JSONObject> {

    private Context mContext;
    private String mUrl;

    public SydVolleyResponseListener(Context context) {
        this.mContext = context;
    }

    public SydVolleyResponseListener(String url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    /*
        * 302 重新登录
        * 699 系统维护
        * */
    public void onResponse(JSONObject response) {
        Integer errorCode = response.getInteger("errorCode");
        if (errorCode != null) {
            if (SydApp.DEBUG) {
                Log.d("SydVolleyResponseListener", "url: " + mUrl + "\t errorCode: " + errorCode);
            }
            if (errorCode == 302) {//302 重新登录
                if (mContext != null) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("您的登录状态已经过期，请重新登录")
                            .setPositiveButton(R.string.confirm, null)
                            .show();
                }
            } else if (errorCode == 699) {//699 系统维护
                if (mContext != null) {
                    Toast.makeText(mContext, "平台正在停机维护,请稍侯", Toast.LENGTH_LONG).show();
                }
            }
            onSuccessfulResponse(response);
        }
    }

    public abstract void onSuccessfulResponse(JSONObject response);
}
