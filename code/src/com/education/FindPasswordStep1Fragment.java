package com.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
public class FindPasswordStep1Fragment extends CommonFragment implements View.OnClickListener {

    private static final String TAG = "FindPasswordStep1";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private EditText mUserNameEditText;
    private EditText mSmsCodeEditText;
    private Resources mResources;
    private Activity mActivity;
    private Button mFetchSmsCodeButton;
    private int mInitCountDownTime = 120;

    public static FindPasswordStep1Fragment create() {
        return new FindPasswordStep1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mResources = getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_find_password_step1, null);
        mFetchSmsCodeButton = (Button) layout.findViewById(R.id.fetch_code);
        mFetchSmsCodeButton.setOnClickListener(this);
        mUserNameEditText = (EditText) layout.findViewById(R.id.user_name);
        mSmsCodeEditText = (EditText) layout.findViewById(R.id.sms_code);
        layout.findViewById(R.id.next_step).setOnClickListener(this);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
        return layout;
    }

    private boolean checkUserName(boolean showDialog) {
        String phoneNumber = mUserNameEditText.getText().toString();
        if (!Pattern.matches("\\d{11}", phoneNumber)) {
            if (showDialog) {
                new AlertDialog.Builder(mActivity)
                        .setMessage(R.string.input_right_phone_number)
                        .setPositiveButton(R.string.confirm, null)
                        .show();
            }
            return false;
        } else if (Pattern.matches("^1\\d{10}$", phoneNumber)) {
            return true;
        } else {
            if (showDialog) {
                new AlertDialog.Builder(mActivity)
                        .setMessage(R.string.input_right_phone_number)
                        .setPositiveButton(R.string.confirm, null)
                        .show();
            }
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fetch_code:
                if (checkUserName(true)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mSimpleBlockedDialogFragment.updateMessage("获取验证码...");
                    mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                    sendSms(mUserNameEditText.getText().toString());
                }
                break;
            case R.id.next_step:
                if (checkUserName(true)
                        && !TextUtils.isEmpty(mSmsCodeEditText.getText().toString())) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mSimpleBlockedDialogFragment.updateMessage("验证中...");
                    mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                    checkCode(mUserNameEditText.getText().toString(), mSmsCodeEditText.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    private void checkCode(final String phoneNumber, final String smsCode) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER_CHECK_SMS_CODE
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    JSONObject result = response.getJSONObject("result");
                    int status = result.getInteger("status");
                    if (status == 1) {
                        Fragment newFragment = FindPasswordStep2Fragment.create(phoneNumber, 120);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.layout, newFragment, "step2");
                        ft.addToBackStack(null);
                        ft.commit();
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
                params.put("phoneNum", phoneNumber);
                params.put("SMCode", smsCode);
                return AppHelper.makeSimpleData("chkSMCode", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }


    private void sendSms(final String userName) {
        mCountDown = mInitCountDownTime;
        mFetchSmsCodeButton.setEnabled(false);
        mHandler.sendEmptyMessageDelayed(ACTION_COUNT_DOWN, 1000);

        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER_GET_SMS_CODE
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    Toast.makeText(mActivity, "验证码已发送", Toast.LENGTH_SHORT).show();
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
                params.put("phoneNum", userName);
                return AppHelper.makeSimpleData("regChkPN", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    protected void restoreInstanceState(Bundle savedInstanceState) {
        int countDown = savedInstanceState.getInt("count_down");
        long leaveTime = savedInstanceState.getLong("leave_time");
        int diff = ((int) (System.currentTimeMillis() - leaveTime)) / 1000;
        int ms = (int) (System.currentTimeMillis() - leaveTime) % 1000;
        mHandler.removeMessages(ACTION_COUNT_DOWN);
        Log.w(TAG, "diff: " + diff);
        mCountDown = countDown - diff;
        if (mCountDown > 0) {
            mFetchSmsCodeButton.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(ACTION_COUNT_DOWN, 1000 - ms);
        } else {
            mCountDown = 0;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count_down", mCountDown);
        outState.putLong("leave_time", System.currentTimeMillis());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(ACTION_COUNT_DOWN);
    }

    private static final int ACTION_COUNT_DOWN = 1;
    private int mCountDown = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_COUNT_DOWN:
                    if (mCountDown > 0) {
                        mFetchSmsCodeButton.setText(getString(R.string.fetch_again_in_seconds, mCountDown));
                        mCountDown -= 1;
                        sendEmptyMessageDelayed(ACTION_COUNT_DOWN, 1000);
                        mFetchSmsCodeButton.setEnabled(false);
                    } else {
                        mFetchSmsCodeButton.setText(getResources().getText(R.string.fetch_again).toString());
                        mFetchSmsCodeButton.setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected String getLogTag() {
        return TAG;
    }
}
