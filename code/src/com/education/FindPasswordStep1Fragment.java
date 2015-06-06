package com.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by su on 2014/9/19.
 */
public class FindPasswordStep1Fragment extends CommonFragment implements View.OnClickListener {

    private static final String TAG = "RegisterStep1";

    private SimpleBlockedDialogFragment mSimpleBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private EditText mPhoneNumberEditText;
    private Resources mResources;
    private Activity mActivity;
    private LinearLayout mCellNumberLayout;

    public static FindPasswordStep1Fragment create() {
        return new FindPasswordStep1Fragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cell_number", mPhoneNumberEditText.getText().toString());
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
        mCellNumberLayout = (LinearLayout) layout.findViewById(R.id.cell_number_layout);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step:
                if (checkUserName(true)) {

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }
}
