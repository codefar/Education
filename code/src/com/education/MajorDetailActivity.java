package com.education;

import com.education.entity.User;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

/**
 * Created by su on 15-6-13.
 */
public class MajorDetailActivity extends CommonBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_major_detail);
		setupTitleBar();

		if (savedInstanceState == null) {
			MajorDetailFragment fragmentStep1 = MajorDetailFragment
					.createInstance(getIntent().getExtras());
			Log.i("TAG", getIntent().getExtras().toString());
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragmentStep1).commit();
		}

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
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE);
		bar.setHomeButtonEnabled(true);
	}

	@Override
	protected String getTag() {
		return null;
	}
}
