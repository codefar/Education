package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import com.education.entity.User;

/**
 * Created by su on 15-6-13.
 */
public class UserInfoActivity extends CommonBaseActivity {

    private static final String TAG = "UserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setupTitleBar();
        User user = User.getInstance();
        TextView xm = (TextView) findViewById(R.id.xm);
        TextView sfzh = (TextView) findViewById(R.id.sfzh);
        TextView kscj = (TextView) findViewById(R.id.kscj);
        TextView kspw = (TextView) findViewById(R.id.kspw);
        TextView kskl = (TextView) findViewById(R.id.kskl);
        TextView kskq = (TextView) findViewById(R.id.kskq);

        xm.setText(user.getXm());
        sfzh.setText(user.getSfzh());
        kscj.setText(String.valueOf(user.getKscj()));
        kspw.setText(String.valueOf(user.getKspw()));
        kskl.setText(user.getKskl() == 1 ? "理工" : "文史");
        kskq.setText(user.getKskqName());
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
