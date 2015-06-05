package com.education;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	private void initTabs(){  
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);    
  
        View tab_smart = getLayoutInflater().inflate(R.layout.view_main_tab_smart, null);
        View tab_manual = getLayoutInflater().inflate(R.layout.view_main_tab_manual, null);
        View tab_volunteer = getLayoutInflater().inflate(R.layout.view_main_tab_volunteer, null);  
        View tab_center = getLayoutInflater().inflate(R.layout.view_main_tab_center, null);  
  
        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator(tab_smart), PlaceholderFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator(tab_manual), PlaceholderFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator(tab_volunteer), PlaceholderFragment.class, null);  
        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator(tab_center), PlaceholderFragment.class, null);  
        //设置tabs之间的分隔线不显示  
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }  
	
}
