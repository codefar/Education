package com.education;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.education.entity.User;

/**
 * Created by su on 15-6-6.
 */
public class FindPasswordActivity extends CommonBaseActivity implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        setupTitleBar();
        if (savedInstanceState == null) {
            Fragment newFragment = FindPasswordStep1Fragment.create();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.layout, newFragment, "step1").commit();
        }
    }


    @Override
    public void onClick(View v) {

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
        bar.setTitle("找回密码");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return null;
    }
}
