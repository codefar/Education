package com.education;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    private FindPasswordActivity mActivity;
    private EditText mNewPasswordEditText;
    private EditText mConfirmEditText;

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
        mActivity = (FindPasswordActivity) getActivity();
        mCellNumber = getArguments().getString(ARG_CELL_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_find_password_step2, null);
        mNewPasswordEditText = (EditText) layout.findViewById(R.id.new_password);
        mConfirmEditText = (EditText) layout.findViewById(R.id.confirm);
        layout.findViewById(R.id.commit).setOnClickListener(this);
        return layout;
    }

    private boolean checkPassword() {
        String password = mNewPasswordEditText.getText().toString();
        if (!Pattern.matches(".{6,20}", password)) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(R.string.password_length_error)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        }

        if (!password.equals(mConfirmEditText.getText().toString())) {
            new AlertDialog.Builder(mActivity)
                    .setMessage("两次密码需输入一致")
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        }
        return true;
    }

    private boolean checkInput() {
        if (!checkPassword()) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (checkInput()) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mSimpleBlockedDialogFragment.updateMessage("提交中...");
                    mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                    changePassword(mCellNumber, mNewPasswordEditText.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void changePassword(final String phoneNum, final String smsCode) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.RESET_PASSWORD
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    JSONObject result = response.getJSONObject("result");
                    int status = result.getInteger("status");
                    if (status == 1) {
                        successful();
                        mActivity.finish();
                    } else {
                        Toast.makeText(mActivity, result.getString("msgText"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
                mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneNum", phoneNum);
                params.put("password", smsCode);
                return AppHelper.makeSimpleData("resetPassword", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }


    private void successful() {
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
                .setMessage("修改密码成功")
                .setPositiveButton("请重新登录", null)
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
