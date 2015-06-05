package com.education.common;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by su on 15-6-5.
 */
public abstract class VolleyErrorListener implements Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        onVolleyErrorResponse(volleyError);
    }

    public abstract void onVolleyErrorResponse(VolleyError volleyError);
}
