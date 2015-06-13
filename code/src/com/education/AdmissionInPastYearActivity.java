package com.education;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.education.entity.User;
import com.education.widget.ClearableEditText;
import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by su on 15-6-7.
 */
public class AdmissionInPastYearActivity extends CommonBaseActivity implements View.OnClickListener {
    private static final String TAG = "AdmissionInPastYearActivity";
    private SegmentedGroup mSegmented;
    private ClearableEditText mMinTextView;
    private ClearableEditText mMaxTextView;
    private Button mResetButton;
    private Button mConfirmButton;
    private boolean isSortByScore = true;

    private List<Integer> mYearList = new ArrayList<Integer>();
    private String[] mYearArray;
    private TextView mYearTextView;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admission_score);
        setupTitleBar();

        mSegmented = (SegmentedGroup) findViewById(R.id.segmented);
        mYearTextView = (TextView) findViewById(R.id.year);
        mMinTextView = (ClearableEditText) findViewById(R.id.min);
        mMaxTextView = (ClearableEditText) findViewById(R.id.max);
        mResetButton = (Button) findViewById(R.id.reset);
        mConfirmButton = (Button) findViewById(R.id.confirm);

        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        int year = c.get(Calendar.YEAR);
        for (int i = year - 1; i >= year - 4; i--) {
            mYearList.add(i);
        }
        mYearArray = new String[mYearList.size()];
        for (int i = 0; i < mYearList.size(); i++) {
            mYearArray[i] = String.valueOf(mYearList.get(i));// + "年";
        }

        mYearTextView.setText(mYearArray[0]);
        mYearTextView.setOnClickListener(this);
    }



    private void reset() {
        User user = User.getInstance();
        isSortByScore = true;
        float score = user.getKscj();
        mMinTextView.setText(String.valueOf(score - 20));
        mMaxTextView.setText(String.valueOf(score + 20));
        mSegmented.check(R.id.score);
        mYearTextView.setText(mYearArray[0]);
    }

    private void confirm() {
        int id = mSegmented.getCheckedRadioButtonId();

        Intent intent = new Intent();
        intent.putExtra("", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                reset();
                break;
            case R.id.confirm:
                break;
            case R.id.year:
                new AlertDialog.Builder(this)
                        .setItems(mYearArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mYear = mYearList.get(which);
                                mYearTextView.setText(mYearArray[which]);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
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
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
