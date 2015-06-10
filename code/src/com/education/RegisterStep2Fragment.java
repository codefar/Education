package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
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
    private EditText mCodeEditText;
    private TextView mCellNumberTextView;

    private RegisterActivity mActivity;
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

        mCodeEditText = (EditText) layout.findViewById(R.id.sms_code);
        mCodeEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(mCodeEditText.getWindowToken(), InputMethodManager.SHOW_FORCED);

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
                .setMessage("请出入短信验证码")
                .setPositiveButton(R.string.confirm, null)
                .show();
        return false;
    }

    private boolean checkInput() {
        if (!checkSmsCode()) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                mRegisterButton.requestFocus();
                checkCode();
                break;
            case R.id.fetch_code:
                mFetchSmsCodeButton.setEnabled(false);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                mSimpleBlockedDialogFragment.updateMessage("获取中...");
                mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                fetchCode(mCellNumber);
                break;
            default:
                break;
        }
    }

    private boolean checkCode() {
        if (checkInput()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            mSimpleBlockedDialogFragment.updateMessage(getText(R.string.register_ing));
            mSimpleBlockedDialogFragment.show(ft, "block_dialog");
            checkCode(mCellNumber, mCodeEditText.getText().toString());
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

    private void checkCode(final String phoneNumber, final String smsCode) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER_CHECK_SMS_CODE
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    Fragment newFragment = RegisterStep3Fragment.create(mCellNumber, 120);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.layout, newFragment, "step3");
                    ft.addToBackStack(null);
                    ft.commit();
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



    private void fetchCode(final String mobile) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER_GET_SMS_CODE
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    mCountDown = mInitCountDownTime;
                    mFetchSmsCodeButton.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(ACTION_COUNT_DOWN, 1000);
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
                params.put("phoneNum", mobile);
                return AppHelper.makeSimpleData("regChkPN", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }
}
