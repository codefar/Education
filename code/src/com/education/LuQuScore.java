package com.education;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.education.entity.User;

public class LuQuScore extends CommonBaseActivity implements OnClickListener,
		RadioGroup.OnCheckedChangeListener {

	private TextView mLuquYearTextView;
	private String[] mYearArray;
	private List<Integer> mYearList = new ArrayList<Integer>();
	private EditText mLowEdit, mHighEdit;
    private Button mResetBt;
    private TextView mHintTextView;
    private Button mConfirmBt;
    private SegmentedGroup mTypeGroup;
    private int mCurrentRadioType = 1;;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luqu_score);
		setupTitleBar();
		mLuquYearTextView = (TextView) findViewById(R.id.luqu_score_year_spinner);
		mLowEdit = (EditText) findViewById(R.id.lowScoreEdit);
		mHighEdit = (EditText) findViewById(R.id.highScoreEdit);
        mResetBt = (Button) findViewById(R.id.reset);
        mResetBt.setOnClickListener(this);
        mHintTextView = (TextView) findViewById(R.id.hint);
        mConfirmBt = (Button) findViewById(R.id.luqu_confirm_bt);
        mConfirmBt.setOnClickListener(this);
        mTypeGroup = (SegmentedGroup) findViewById(R.id.score_type_radio_grop);
        mTypeGroup.setOnCheckedChangeListener(this);
        initYear();
        mLuquYearTextView.setOnClickListener(this);
	}

	/**
	 * 单选Dialogs
	 */
	private void simpleDialog() {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择年份");
		builder.setSingleChoiceItems(mYearArray, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						mLuquYearTextView.setText(mYearArray[which]);
					}
				});
		dialog = builder.create();
		dialog.show();
	}

	private void initYear() {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		int year = c.get(Calendar.YEAR);
		for (int i = year - 1; i >= year - 4; i--) {
			mYearList.add(i);
		}
		mYearArray = new String[mYearList.size()];
		for (int i = 0; i < mYearList.size(); i++) {
			mYearArray[i] = String.valueOf(mYearList.get(i));// + "年";
		}

		mLuquYearTextView.setText(mYearArray[0]);
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
		bar.setTitle("历年录取情况");
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
		} else if (v.getId() == R.id.luqu_confirm_bt) {
			Intent intent = new Intent();
			intent.putExtra("low_score", mLowEdit.getText().toString());
			intent.putExtra("high_score", mHighEdit.getText().toString());
			intent.putExtra("year_score", mLuquYearTextView.getText()
					.toString());
			intent.putExtra("score_type",mCurrentRadioType);

			setResult(3, intent);
			finish();
		} else if (v.getId() == R.id.reset) {
            if (mCurrentRadioType == 1) {
                fenShuReset();
            } else {
                paiWeiReset();
            }
        }

	}

    private void fenShuReset() {
        User user = User.getInstance();
        yearReset();
        int low = user.getKscj() - 20;
        mLowEdit.setText(String.valueOf(low < 1 ? 1 : low));
        mHighEdit.setText(String.valueOf(user.getKscj() + 20));
    }

    private void paiWeiReset() {
        User user = User.getInstance();
        yearReset();
        int low = user.getKspw() - 200;
        mLowEdit.setText(String.valueOf(low < 1 ? 1 : low));
        mHighEdit.setText(String.valueOf(user.getKspw() + 200));
    }

    private void yearReset() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        int year = c.get(Calendar.YEAR) - 1;
        mLuquYearTextView.setText(String.valueOf(year));
    }



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.fenshu_radiobutton) {
            mCurrentRadioType = 1;
            mHintTextView.setText("注:默认值是你的高考成绩上下浮动20分");
        }
		else {
            mCurrentRadioType = 2;
            mHintTextView.setText("注:默认值是你的高考成绩上下浮动200名");
        }
	}
//    注:默认值是你的高考成绩上下浮动20分
}
