package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.education.common.VolleyResponseListener;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by su on 2014/9/19.
 */
public class RegisterStep3Fragment extends CommonFragment implements View.OnClickListener {

    public static final String TAG = "RegisterStep2Fragment";
    private static final boolean DEBUG = EduApp.DEBUG;
    private static final String ARG_CELL_NUMBER = "cell_number";
    private static final String ARG_COUNT_DOWN_TIME = "count_down_time";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();
    private String mCellNumber;
    private Button mRegisterButton;
    private EditText mPasswordEditText;
    private TextView mCellNumberTextView;

    private RegisterActivity mActivity;

    public static RegisterStep3Fragment create(String cellNumber, int countDownTime) {
        RegisterStep3Fragment fragment = new RegisterStep3Fragment();
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
        ActionBar bar = mActivity.getActionBar();
        bar.setTitle(R.string.verify_and_set_password);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_register_step3, null);

        mRegisterButton = (Button) layout.findViewById(R.id.commit);
        mRegisterButton.setOnClickListener(this);

        mPasswordEditText = (EditText) layout.findViewById(R.id.password);
        mCellNumberTextView = (TextView) layout.findViewById(R.id.cell_number);
        mCellNumberTextView.setText(getString(R.string.input_phone_sms_code, mCellNumber));
        return layout;
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
            String code = "";
            appRegister(mCellNumber, password, code);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
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
