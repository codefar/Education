package com.education;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.education.entity.User;

public class SetUserInfoActivity extends CommonBaseActivity implements OnClickListener {

	private static final String TAG = "SetUserInfoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_user_info);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SmartRecomentFragmentStep1()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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
	}

	@Override
	protected String getTag() {
		return TAG;
	}
}
