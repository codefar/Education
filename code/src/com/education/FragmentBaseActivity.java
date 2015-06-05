package com.education;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.education.utils.MenuHelper;

/**
 * Created by su on 2014/8/22.
 */
public abstract class FragmentBaseActivity extends FragmentActivity {

    protected int mTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTabId = intent.getIntExtra("tabId", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        MenuHelper.menuItemSelected(this, featureId, item);
        return super.onMenuItemSelected(featureId, item);
    }

    protected void setCurrentItem(ViewPager viewPager, int fragmentCount, int fragmentItemId) {
        if (fragmentCount == 0) {
            return;
        }
        if (fragmentItemId >= fragmentCount) {
            return;
        }
        if (fragmentItemId < 0) {
            fragmentItemId = 0;
        }
        viewPager.setCurrentItem(fragmentItemId);
    }
}
