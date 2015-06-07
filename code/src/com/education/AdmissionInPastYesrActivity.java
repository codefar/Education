package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.education.entity.User;
import com.education.widget.ClearableEditText;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by su on 15-6-7.
 */
public class AdmissionInPastYesrActivity extends CommonBaseActivity implements View.OnClickListener {
    private static final String TAG = "MinAdmissionScoreActivity";
    private SegmentedGroup mSegmented;
    private ClearableEditText mMinTextView;
    private ClearableEditText mMaxTextView;
    private Button mResetButton;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admission_score);

        mSegmented = (SegmentedGroup) findViewById(R.id.segmented);
        mMinTextView = (ClearableEditText) findViewById(R.id.min);
        mMaxTextView = (ClearableEditText) findViewById(R.id.max);
        mResetButton = (Button) findViewById(R.id.reset);
        mConfirmButton = (Button) findViewById(R.id.confirm);

        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }



    private void reset() {
        User user = User.getInstance();

    }

    private void confirm() {
        int id = mSegmented.getCheckedRadioButtonId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                reset();
                break;
            case R.id.confirm:
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
