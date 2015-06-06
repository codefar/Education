package com.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.education.common.AppHelper;
import com.education.entity.User;
import com.education.utils.MenuHelper;

import java.util.Map;

/**
 * Created by su on 2014/6/13.
 */
public abstract class BaseActivity extends Activity {

    protected Map<String, String> mUrlParamMap;

    protected abstract void unLoginForward(User user);
    protected abstract void forceUpdateForward();
    protected abstract void setupTitleBar();
    protected abstract String getTag();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        boolean showDialog = intent.getBooleanExtra("show_dialog", false);
        if (showDialog) {
            String title = intent.getStringExtra("title");
            String msg = intent.getStringExtra("msg");
            new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton(R.string.known, null).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean force = sp.getBoolean(Constants.SP_COLUMN_FORCE_TO_UPDATE, false);
        if (force) {
            forceUpdateForward();
            return;
        }

        User user = User.getInstance();
        if (TextUtils.isEmpty(user.getId())) {
            unLoginForward(user);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        MenuHelper.menuItemSelected(this, featureId, item);
        return super.onMenuItemSelected(featureId, item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        User user = User.getInstance();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public Map<String, String> getUrlParamMap() {
        return mUrlParamMap;
    }

    protected void logout() {
        User.clearUser();
        startActivity(AppHelper.makeLogoutIntent(this));
    }

//    protected void showForceUpdateDialog() {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        Intent intent = new Intent(this, DownloadDialogActivity.class);
//        intent.putExtra("description", sp.getString(Constants.SP_COLUMN_NEW_VERSION_DESCRIPTION, ""));
//        intent.putExtra("url", sp.getString(Constants.SP_COLUMN_NEW_VERSION_URL, ""));
//        intent.putExtra("force", true);
//        intent.putExtra("version", sp.getString(Constants.SP_COLUMN_NEW_VERSION, ""));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
}
