package com.education.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.education.Constants;
import com.education.LoginActivity;
import com.education.R;
import com.education.RegisterActivity;
import com.education.entity.User;

/**
 * Created by su on 15-6-6.
 */
public class MenuHelper {

    public static void menuItemSelected(Context context, int featureId, MenuItem item) {
        User user = User.getInstance();
        switch (item.getItemId()) {
            case R.id.action_login:
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
            case R.id.action_register:
                context.startActivity(new Intent(context, RegisterActivity.class));
                break;
            default:
                break;
        }
    }
}
