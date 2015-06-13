package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import com.education.entity.User;

/**
 * Created by su on 15-6-13.
 */
public class AboutUsActivity extends CommonBaseActivity {

    private static final String TAG = "AboutUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setupTitleBar();
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
        bar.setTitle("关于");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
