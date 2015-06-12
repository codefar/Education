package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.education.entity.User;

public class LuQuScore extends BaseActivity {

	private Spinner mLuquYearSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luqu_score);

		// 初始化控件
		mLuquYearSpinner = (Spinner) findViewById(R.id.luqu_score_year_spinner);
		// 建立数据源
		String[] mItems = { "2012年", "2013年", "2014年", "2015年" };
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				R.layout.year_spinner_item, mItems);
		// 绑定 Adapter到控件
		mLuquYearSpinner.setAdapter(_Adapter);
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
		bar.setTitle("返回");
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE);
		bar.setHomeButtonEnabled(true);

	}

	@Override
	protected String getTag() {
		return null;
	}

}
