package com.education;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.education.entity.User;

public class SmartRecommentResultActivity extends CommonBaseActivity implements
		View.OnClickListener {

	private TextView mNameTextView,mPiciTextView;
	private ListView mResultListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_recomment_result);
		
		mNameTextView = (TextView) findViewById(R.id.name);
		mPiciTextView = (TextView) findViewById(R.id.pici);
		mResultListView = (ListView) findViewById(R.id.listView);
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
		return null;
	}
}
