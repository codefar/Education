package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.education.entity.User;

public class LuQuScore extends BaseActivity implements OnClickListener {

	private TextView mLuquYearTextView;
	String[] mKaoQu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luqu_score);
		mLuquYearTextView = (TextView) findViewById(R.id.luqu_score_year_spinner);
		mKaoQu = getResources().getStringArray(R.array.exam_year);
		mLuquYearTextView.setOnClickListener(this);
	}

	/**
	 * 单选Dialogs
	 */
	private void simpleDialog() {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择年份");
		builder.setSingleChoiceItems(mKaoQu, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						mLuquYearTextView.setText(mKaoQu[which]);
					}
				});
		dialog = builder.create();
		dialog.show();
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.luqu_score_year_spinner) {
			simpleDialog();
		}
	}
}
