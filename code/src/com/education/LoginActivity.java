package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends CommonBaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final boolean DEBUG = EduApp.DEBUG;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mRegisterTextView;
    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private Resources mResources;
    private String mUserName;
    private String mCheckingPhoneNumber;


    @Override
    protected void unLoginForward(User user) { 
        //do nothing here
    }

    @Override
    protected void forceUpdateForward() {

    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupTitleBar();

        mUserNameEditText = (EditText) findViewById(R.id.user_name);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login);
        mLoginButton.setOnClickListener(this);
        mRegisterTextView = (TextView) findViewById(R.id.register);
        mRegisterTextView.setOnClickListener(this);

        findViewById(R.id.forget_password).setOnClickListener(this);
    }

    private boolean checkUserName() {
        String userName = mUserNameEditText.getText().toString();
        if (userName.trim().length() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.input_username)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPassword() {
        String password = mPasswordEditText.getText().toString();
        if (password.trim().length() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.input_password)
                    .setPositiveButton(R.string.confirm, null)
                    .show();
            return false;
        } else {
            return true;
        }
    }

    protected void setupTitleBar() {
        ActionBar bar = getActionBar();
        bar.setTitle(R.string.login);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUserName = mUserNameEditText.getText().toString();
        outState.putString("user_name", mUserName);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUserName = savedInstanceState.getString("user_name");
        mUserNameEditText.setText(mUserName);
    }

    public void onResume() {
        super.onResume();
    }

    private boolean checkInput() {
        if (!checkUserName()) {
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
            case R.id.login:
                if (checkInput()) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mBlockedDialogFragment.updateMessage(getText(R.string.login_ing));
                    mBlockedDialogFragment.show(ft, "block_dialog");
                    //new LoginAsyncTask(ACTION_LOGIN).execute();
                    final String userName = mUserNameEditText.getText().toString();
                    final String password = mPasswordEditText.getText().toString();

                    appLogin(userName, password);
                }
                break;
            case R.id.register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.forget_password:
                getPasswordBack();
                break;
            default:
                break;
        }
    }

    private void getPasswordBack() {
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    private void appLogin(final String userName, final String password) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.LOGIN
                , null, new VolleyResponseListener(LoginActivity.this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String data = response.getString("userInfo");
                    UserInfo userInfo = JSON.parseObject(data, UserInfo.class);
                    User user = User.getInstance();
                    user.setId(userInfo.getUserId());
                    user.setUserSession(userInfo.getUserSession());
                    User.saveUser(user);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    mBlockedDialogFragment.dismissAllowingStateLoss();
                    Toast.makeText(LoginActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mBlockedDialogFragment.dismissAllowingStateLoss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
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
