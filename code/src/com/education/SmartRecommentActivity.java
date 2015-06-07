package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.education.entity.User;

public class SmartRecommentActivity extends CommonBaseActivity implements View.OnClickListener {

	private static final String TAG = "SmartRecommentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_recomment);
		setupTitleBar();
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SmartRecomentFragmentStep1()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.smart_recomment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
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
        bar.setTitle(R.string.smart_recomment);
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
	}

	@Override
	protected String getTag() {
		return TAG;
	}
}
