package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import com.education.entity.User;

/**
 * Created by su on 15-6-7.
 */
public class MinAdmissionScoreActivity extends CommonBaseActivity {
    private static final String TAG = "MinAdmissionScoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admission_score);
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
        bar.setTitle("最低录取分数");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
