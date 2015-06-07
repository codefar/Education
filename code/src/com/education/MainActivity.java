package com.education;

import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends FragmentBaseActivity {

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
		mTabHost.addTab(mTabHost.newTabSpec("tab_center").setIndicator(tab_center),
				PersonCenterFragment.class, null);
		// 设置tabs之间的分隔线不显示
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
