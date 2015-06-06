package com.education;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.education.common.FastJsonRequest;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by su on 2014/9/19.
 */
public class FindPasswordStep2Fragment extends CommonFragment implements View.OnClickListener {

    public static final String TAG = "RegisterStep2Fragment";
    private static final boolean DEBUG = EduApp.DEBUG;
    private static final String ARG_CELL_NUMBER = "cell_number";
    private static final String ARG_COUNT_DOWN_TIME = "count_down_time";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();
    private String mCellNumber;

    private RegisterActivity mActivity;

    public static FindPasswordStep2Fragment create(String cellNumber, int countDownTime) {
        FindPasswordStep2Fragment fragment = new FindPasswordStep2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_CELL_NUMBER, cellNumber);
        args.putInt(ARG_COUNT_DOWN_TIME, countDownTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (RegisterActivity) getActivity();
        mCellNumber = getArguments().getString(ARG_CELL_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_find_password_step2, null);
        return layout;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void successful() {
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
                .setMessage("修改密码成功")
                .setPositiveButton("进入应用", null)
                .show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mActivity.finish();
            }
        });
    }

    @Override
    protected String getLogTag() {
        return null;
    }

    private void appLogin(final String userName, final String passWord, final String kaptcha, final String smsCode, final String sign, final String version){
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.LOGIN
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Integer errorCode = response.getInteger("errorCode");
                if (EduApp.DEBUG) {
                    Log.i(TAG, response.toJSONString());
                }
                mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                if (errorCode != null && errorCode == 0) {

                } else {
                    Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userName", userName);
                params.put("passWord", passWord);
                params.put("kaptcha", kaptcha);
                params.put("smsCode", smsCode);
                params.put("sign", sign);
                params.put("version", version);
                return params;
            }
        };
        EduApp.sRequestQueue.add(request);
    }
}
