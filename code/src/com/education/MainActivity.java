package com.education;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentBaseActivity {

    private static final String TAG = "MainActivity";
	private FragmentTabHost mTabHost;
    public static final int TAB_SMART = 0;
    public static final int TAB_MANUAL = 1;
    public static final int TAB_VOLUNTEER = 2;
    public static final int TAB_CENTER = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTabs();
        fetchUserInfo();
	}

    private void fetchUserInfo() {
        User user = User.getInstance();
        if (user.getXm() == null) { // 如果用户没有填写真实姓名,那么从后台读取
            fetch(user.getId());
        }
    }

    private void fetch(final String userId) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.KAO_SHENG_XIN_XI
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String ksxx = response.getString("ksxx");
                    UserInfo userInfo = JSON.parseObject(ksxx, UserInfo.class);
                    User user = User.getInstance();
                    user.setAccountId(userInfo.getAccountId());
                    user.setXm(userInfo.getXm());
                    user.setSfzh(userInfo.getSfzh());
                    user.setKscj(userInfo.getKscj());
                    user.setKspw(userInfo.getKspw());
                    user.setKskl(userInfo.getKskl());
                    user.setKsklName(userInfo.getKsklName());//科类名称
                    user.setKqdh(userInfo.getKqdh());//考区代号
                    user.setKskqName(userInfo.getKskqName());
                    User.saveUser(user);
                    if (TextUtils.isEmpty(user.getXm())) {
                        //需要用户录入信息
                    } else {
                        //打开首页
//                        startActivity(new Intent());
                    }
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(MainActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return AppHelper.makeSimpleData("ksxxQuery", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

	private void initTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getFragmentManager(),
				R.id.realtabcontent);

		View tab_smart = getLayoutInflater().inflate(
				R.layout.view_main_tab_smart, null);
		View tab_manual = getLayoutInflater().inflate(
				R.layout.view_main_tab_manual, null);
		View tab_volunteer = getLayoutInflater().inflate(
				R.layout.view_main_tab_volunteer, null);
		View tab_center = getLayoutInflater().inflate(
				R.layout.view_main_tab_center, null);

		mTabHost.addTab(mTabHost.newTabSpec("tab_smart").setIndicator(tab_smart),
				SmartRecomentFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab_manual").setIndicator(tab_manual),
				ManualTimFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab_volunteer").setIndicator(tab_volunteer),
				VolunteerCollectionFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("tab_center1").setIndicator(tab_center),
				PersonCenterFragment.class, null);
		//设置tabs之间的分隔线不显示
		mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onBackPressed() {
        int tabId = mTabHost.getCurrentTab();
        if (tabId == TAB_VOLUNTEER) {
            VolunteerCollectionFragment f= (VolunteerCollectionFragment) getFragmentManager().findFragmentByTag("tab_volunteer");
            if (f.back()) {
                super.onBackPressed();
            } else {
                return;
            }
        }
        super.onBackPressed();
    }
}
