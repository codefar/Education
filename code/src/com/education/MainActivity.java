package com.education;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.education.ManualTimFragment;
import com.education.PersonCenterFragment;
import com.education.R;
import com.education.SmartRecomentFragment;
import com.education.VolunteerCollectionFragment;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabs();
	}

	private void initTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(),
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

}
