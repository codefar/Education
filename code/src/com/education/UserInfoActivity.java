package com.education;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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

/**
 * Created by su on 15-6-13.
 */
public class UserInfoActivity extends CommonBaseActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoActivity";
    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private EditText mKscjEditText;
    private EditText mKspwEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setupTitleBar();
        User user = User.getInstance();
        TextView xm = (TextView) findViewById(R.id.xm);
        TextView sfzh = (TextView) findViewById(R.id.sfzh);
        mKscjEditText = (EditText) findViewById(R.id.kscj);
        mKspwEditText = (EditText) findViewById(R.id.kspw);
        TextView kskl = (TextView) findViewById(R.id.kskl);
        TextView kskq = (TextView) findViewById(R.id.kskq);

        xm.setText(user.getXm());
        sfzh.setText(user.getSfzh());
        mKscjEditText.setText(String.valueOf(user.getKscj()));
        mKspwEditText.setText(String.valueOf(user.getKspw()));
        kskl.setText(user.getKskl() == 1 ? "理工" : "文史");
        kskq.setText(user.getKskqName());


        findViewById(R.id.modify).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        enableEdit(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify:
                enableEdit(true);
                break;
            case R.id.save:
                updateKsxx();
                break;
            default:
                break;
        }
    }

    private void enableEdit(boolean enable) {
        mKscjEditText.setEnabled(enable);
        mKspwEditText.setEnabled(enable);
        if (enable) {
            findViewById(R.id.modify).setVisibility(View.GONE);
            findViewById(R.id.save).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.modify).setVisibility(View.VISIBLE);
            findViewById(R.id.save).setVisibility(View.GONE);
        }
    }

    private void updateKsxx() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mBlockedDialogFragment.updateMessage("");
        mBlockedDialogFragment.show(ft, "block_dialog");

        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.KAO_SHENG_XIN_XI_XIU_GAI
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    String ksxx = response.getString("ksxx");
                    UserInfo ui = JSON.parseObject(ksxx, UserInfo.class);
                    User user = User.getInstance();
                    user.setAccountId(ui.getAccountId());
                    user.setXm(ui.getXm());
                    user.setSfzh(ui.getSfzh());
                    user.setKscj(ui.getKscj());
                    user.setKspw(ui.getKspw());
                    user.setKskl(ui.getKskl());
                    user.setKsklName(ui.getKsklName());//科类名称
                    user.setKqdh(ui.getKskq());//考区代号
                    user.setKskqName(ui.getKskqName());
                    User.saveUser(user);

                    finish();
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(UserInfoActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(UserInfoActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = User.getInstance();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", user.getId());
                params.put("xm", user.getXm());
                params.put("sfzh", user.getSfzh());
                params.put("kscj", mKscjEditText.getText().toString());
                params.put("kspw", mKspwEditText.getText().toString());
                params.put("kskl", String.valueOf(user.getKskl()));
                params.put("kskq", String.valueOf(user.getKqdh()));
                return AppHelper.makeSimpleData("ksxxUpdate", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        bar.setTitle("个人信息");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
