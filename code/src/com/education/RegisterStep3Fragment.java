package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.User;
import com.education.entity.UserInfo;
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
    private EditText mConfirmEditText;

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
        mConfirmEditText = (EditText) layout.findViewById(R.id.confirm);
        return layout;
    }

    private boolean checkPassword() {
        String password = mPasswordEditText.getText().toString();
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
            appRegister(mCellNumber, password);
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

    private void appRegister(final String userName, final String password) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    appLogin(userName, password);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("phoneNum", userName);
                map.put("password", password);
                return AppHelper.makeSimpleData("register", map);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    private void appLogin(final String userName, final String password) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.LOGIN
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String data = response.getString("userInfo");
                    UserInfo userInfo = JSON.parseObject(data, UserInfo.class);
                    User user = User.getInstance();
                    user.setId(userInfo.getUserId());
                    user.setUserSession(userInfo.getUserSession());
                    User.saveUser(user);
                    startActivity(AppHelper.mainActivityIntent(mActivity));
                    mActivity.finish();
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    mSimpleBlockedDialogFragment.dismissAllowingStateLoss();
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("loginName", userName);
                map.put("password", password);
                return AppHelper.makeSimpleData("login", map);
            }
        };
        EduApp.sRequestQueue.add(request);
    }
}
