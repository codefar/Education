package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by su on 2014/9/26.
 */
public class ChangePasswordActivity extends CommonBaseActivity implements View.OnClickListener {

    private static final String TAG = "ModifyPasswordActivity";
    private EditText mOldPasswordEditText;
    private EditText mPasswordEditText;
    private Button mSaveButton;
    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();
    private EditText mConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        setupTitleBar();

        mOldPasswordEditText = (EditText) findViewById(R.id.old_password);
        mPasswordEditText = (EditText) findViewById(R.id.new_password);
        mConfirmEditText = (EditText) findViewById(R.id.confirm_password);

        mSaveButton = (Button) findViewById(R.id.change);
        mSaveButton.setOnClickListener(this);
    }

    private boolean checkPassword() {
        String password = mPasswordEditText.getText().toString();
        if (!Pattern.matches(Constants.PASSWORD_STR, password)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.password_length_error)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        }

        if (!password.equals(mConfirmEditText.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setMessage("新密码与确认密码必须一致")
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                mSaveButton.requestFocus();
                modifyPassword();
                break;
        }
    }

    private boolean modifyPassword() {
        if (checkInput()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            mBlockedDialogFragment.updateMessage(getText(R.string.progress_bar_committing_please_wait));
            mBlockedDialogFragment.show(ft, "block_dialog");
            User user = User.getInstance();
            String oldPassword = mOldPasswordEditText.getText().toString();
            String newPassword = mPasswordEditText.getText().toString();
            changePassword(user.getId(), oldPassword, newPassword);
        }
        return false;
    }

    private boolean checkInput() {
        if (!checkPassword()) {
            return false;
        }

        if (mOldPasswordEditText.getText().toString().equals(mPasswordEditText.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.same_password_error)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void unLoginForward(User user) {

    }

    @Override
    protected void forceUpdateForward() {

    }

    @Override
    protected void setupTitleBar() {
        ActionBar bar = getActionBar();
        bar.setTitle(R.string.modify_login_password);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    private void changePassword(final String uid, final String oldpassword, final String newpassword) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.CHANGE_PASSWORD
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    mBlockedDialogFragment.dismissAllowingStateLoss();
                    Toast.makeText(ChangePasswordActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mBlockedDialogFragment.dismiss();
                Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", uid);
                params.put("oldpsw", LogUtil.md5Hex(oldpassword));
                params.put("newpsw", LogUtil.md5Hex(newpassword));
                return AppHelper.makeSimpleData("setpassword", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }
}
