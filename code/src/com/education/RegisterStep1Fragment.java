package com.education;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.regex.Pattern;

/**
 * Created by su on 2014/9/19.
 */
public class RegisterStep1Fragment extends CommonFragment implements View.OnClickListener {

    private static final String TAG = "RegisterStep1";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private EditText mPhoneNumberEditText;
    private Resources mResources;
    private RegisterActivity mActivity;
    private CheckBox mCheckBox;
    private Button mRegister;

    public static RegisterStep1Fragment create() {
        return new RegisterStep1Fragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cell_number", mPhoneNumberEditText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (RegisterActivity) getActivity();
        mResources = getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_register_step1, null);
        TextView protocolTextView = (TextView) layout.findViewById(R.id.protocol);
        protocolTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mRegister = (Button) layout.findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        mCheckBox = (CheckBox) layout.findViewById(R.id.agree);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRegister.setEnabled(isChecked);
            }
        });
        mRegister.setEnabled(mCheckBox.isChecked());
        mPhoneNumberEditText = (EditText) layout.findViewById(R.id.cell_number);
        return layout;
    }

    private boolean checkUserName(boolean showDialog) {
        String phoneNumber = mPhoneNumberEditText.getText().toString();
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

    private static final int SMS_RESULT_SUCCESSFUL = 0;
    private static final int SMS_RESULT_FAIL = 1;
    private static final int SMS_RESULT_INVALIDATE = 2;
    private static final int SMS_RESULT_BLACK_LIST = 3;

    private static final int ACTION_CHECK_MOBILE = 1;
    private static final int ACTION_FETCH_SMS = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (checkUserName(true)) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mSimpleBlockedDialogFragment.updateMessage("正在获取验证码");
                    mSimpleBlockedDialogFragment.show(ft, "block_dialog");
                    String phoneNumber = mPhoneNumberEditText.getText().toString();
                    appCheckMobile(phoneNumber);
                }
                break;
            case R.id.protocol:
                break;
            default:
                break;
        }
    }

    private void appCheckMobile(final String mobile) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.REGISTER_GET_SMS_CODE
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    Fragment newFragment = RegisterStep2Fragment.create(mPhoneNumberEditText.getText().toString(), 120);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.layout, newFragment, "step2");
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
                params.put("phoneNum", mobile);
                return AppHelper.makeSimpleData("regChkPN", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }
}
