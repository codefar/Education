package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.regex.Pattern;

/**
 * Created by su on 2014/9/19.
 */
public class RegisterStep2Fragment extends CommonFragment implements View.OnClickListener {

    public static final String TAG = "RegisterStep2Fragment";
    private static final boolean DEBUG = EduApp.DEBUG;
    private static final String ARG_CELL_NUMBER = "cell_number";
    private static final String ARG_COUNT_DOWN_TIME = "count_down_time";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();
    private String mCellNumber;
    private Button mRegisterButton;
    private TextView mFetchSmsCodeButton;
    private EditText mPasswordEditText;
    private EditText mCodeEditText;
    private TextView mCellNumberTextView;

    private RegisterActivity mActivity;
    private LinearLayout mSmsCodeLayout;
    private int mInitCountDownTime;

    public static RegisterStep2Fragment create(String cellNumber, int countDownTime) {
        RegisterStep2Fragment fragment = new RegisterStep2Fragment();
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
        mInitCountDownTime = getArguments().getInt(ARG_COUNT_DOWN_TIME, 60);
        ActionBar bar = mActivity.getActionBar();
        bar.setTitle(R.string.verify_and_set_password);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_register_step2, null);
        mFetchSmsCodeButton = (TextView) layout.findViewById(R.id.fetch_code);
        mFetchSmsCodeButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            mCountDown = mInitCountDownTime;
            mFetchSmsCodeButton.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(ACTION_COUNT_DOWN, 1000);
        }

        mRegisterButton = (Button) layout.findViewById(R.id.commit);
        mRegisterButton.setOnClickListener(this);

        mSmsCodeLayout = (LinearLayout) layout.findViewById(R.id.sms_code_layout);
        mCodeEditText = (EditText) layout.findViewById(R.id.sms_code);
        mCodeEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(mCodeEditText.getWindowToken(), InputMethodManager.SHOW_FORCED);

        mPasswordEditText = (EditText) layout.findViewById(R.id.password);
        mCellNumberTextView = (TextView) layout.findViewById(R.id.cell_number);
        mCellNumberTextView.setText(getString(R.string.input_phone_sms_code, mCellNumber));
        return layout;
    }

    private boolean checkSmsCode() {
        String code = mCodeEditText.getText().toString();
        if (code.trim().length() > 0) {
            return true;
        }
        new AlertDialog.Builder(mActivity)
                .setMessage(R.string.sms_local_check_error)
                .setPositiveButton(R.string.confirm, null)
                .show();
        return false;
    }

    private boolean checkPassword() {
        String password = mPasswordEditText.getText().toString();
        if (!Pattern.matches(".{5,20}", password)) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(R.string.password_length_error)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkInput() {
        if (!checkSmsCode()) {
            return false;
        }

        if (!checkPassword()) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                mRegisterButton.requestFocus();
                register();
                break;
            case R.id.fetch_code:
                mFetchSmsCodeButton.setEnabled(false);
                String password = mPasswordEditText.getText().toString();
                String code = mCodeEditText.getText().toString();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                mSimpleBlockedDialogFragment.updateMessage(getText(R.string.register_ing));
                mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                break;
            default:
                break;
        }
    }

    private boolean register() {
        if (checkInput()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            mSimpleBlockedDialogFragment.updateMessage(getText(R.string.register_ing));
            mSimpleBlockedDialogFragment.show(ft, "block_dialog");

            String password = mPasswordEditText.getText().toString();
            String code = mCodeEditText.getText().toString();
            appRegister(mCellNumber, password, code);
        }
        return false;
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
        outState.putString("cell_number", mCellNumber);
        outState.putInt("count_down", mCountDown);
        outState.putLong("leave_time", System.currentTimeMillis());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(ACTION_COUNT_DOWN);
    }

    private static final int ACTION_REGISTER = 3;
    private static final int ACTION_LOGIN = 4;
    private static final int ACTION_FETCH_RED_ENVELOPE_AMOUNT = 5;

    private static final int SMS_RESULT_SUCCESSFUL = 0;
    private static final int SMS_RESULT_FAIL = 1;
    private static final int SMS_RESULT_INVALIDATE = 2;
    private static final int SMS_RESULT_BLACK_LIST = 3;
    private static final int SMS_RESULT_VOICE_CODE = 101;
    private static final int SMS_RESULT_CALL_CUSTOMER_SUPPORT = 102;

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
        return null;
    }

    private void appRegister(final String userName, final String passWord, final String smsCode) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Integer errorCode = response.getInteger("errorCode");
                if (EduApp.DEBUG) {
                    Log.i(TAG, response.toJSONString());
                }
                mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                if (errorCode != null && errorCode == 0) {
                    if (errorCode == 0) {

                    } else {
                        mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                    }
                    String message = null;
                    switch (errorCode) {
                        case -1:
                            message = getResources().getString(R.string.internet_exception);
                            break;
                        default:
                            message = response.getString("errorMessage");
                            break;
                    }

                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
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
                params.put("smsCode", smsCode);
                return params;
            }
        };
        EduApp.sRequestQueue.add(request);
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
