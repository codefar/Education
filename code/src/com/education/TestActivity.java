package com.education;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by su on 15-6-10.
 */
public class TestActivity extends CommonBaseActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInfo userInfo = makeUserInfo();
        updateKsxx(userInfo);
    }


    private UserInfo makeUserInfo() {
        User user = User.getInstance();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setXm("苏"); //姓名
        userInfo.setSfzh("123456789012345678"); //身份证
        userInfo.setKscj(601); //分数
        userInfo.setKspw(10001); //排名
        userInfo.setKskl(2); //理工
        userInfo.setKqdh(3); //天津
        return userInfo;
    }

    private void updateKsxx(final UserInfo userInfo) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.KAO_SHENG_XIN_XI_XIU_GAI
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
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
                    user.setKqdh(ui.getKqdh());//考区代号
                    user.setKskqName(ui.getKskqName());
                    User.saveUser(user);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userInfo.getUserId());
                params.put("xm", userInfo.getXm());
                params.put("sfzh", userInfo.getSfzh());
                params.put("kscj", String.valueOf(userInfo.getKscj()));
                params.put("kspw", String.valueOf(userInfo.getKspw()));
                params.put("kskl", String.valueOf(userInfo.getKskl()));
                params.put("kskq", String.valueOf(userInfo.getKqdh()));
                return AppHelper.makeSimpleData("ksxxUpdate", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    @Override
    protected void unLoginForward(User user) {

    }

    @Override
    protected void forceUpdateForward() {

    }

    @Override
    protected void setupTitleBar() {

    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
