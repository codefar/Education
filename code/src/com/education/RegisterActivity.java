package com.education;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.education.common.AppHelper;
import com.education.entity.User;

/**
 * Created by su on 2014/9/18.
 */
public class RegisterActivity extends CommonBaseActivity {

    private static final String TAG = "RegisterActivity";
    private Class<?> mClazz;
    private boolean mJustFinish = false;
    private boolean mBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupTitleBar();

        if (savedInstanceState == null) {
            Fragment newFragment = RegisterStep1Fragment.create();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.layout, newFragment, "step1").commit();
        }
        Intent intent = getIntent();
        mClazz = (Class<?>) intent.getSerializableExtra("class");
        mJustFinish = intent.getBooleanExtra("just_finish", false);
        mBackToMain = intent.getBooleanExtra("back_to_main", false);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            super.onBackPressed();
        } else {
            if (!mBackToMain) {
                super.onBackPressed();
            } else {
                startActivity(AppHelper.makeLogoutIntent(this));
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public Class<?> getClazz() {
        return mClazz;
    }

    public boolean isJustFinish() {
        return mJustFinish;
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
        bar.setTitle("手机快速注册");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
