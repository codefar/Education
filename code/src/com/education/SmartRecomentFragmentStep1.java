package com.education;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.User;
import com.education.entity.UserInfo;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;
import info.hoang8f.android.segmented.SegmentedGroup;

import com.education.utils.MenuHelper;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SmartRecomentFragmentStep1 extends CommonFragment implements
		OnClickListener, RadioGroup.OnCheckedChangeListener {

	private static final String TAG = SmartRecomentFragmentStep1.class
			.getSimpleName();
    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

	private Button mNextBtn;
	private EditText mNameEditText;
	private EditText mIdEditText;
	private EditText mScoreEditText;
	private EditText mPostionEditText;
	private TextView mZoneTextView;
	private SegmentedGroup mSegmentedGroup;

	private boolean goSmartRecomnetStep2 = false;
	public static final String GO_SMART_RECOMNET_STEP2 = "GO_SMART_RECOMNET_STEP2";

	String[] mKaoQu;
	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupTitleBar();
		if (getArguments() != null) {
			goSmartRecomnetStep2 = getArguments().getBoolean(
					GO_SMART_RECOMNET_STEP2, true);
		}
		mKaoQu = getResources().getStringArray(R.array.kaoqu_name);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_recomment_step1,
                container, false);
        mNextBtn = (Button) v.findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(this);
        mScoreEditText = (EditText) v.findViewById(R.id.editText1);
        mPostionEditText = (EditText) v.findViewById(R.id.editText2);
        mNameEditText = (EditText) v.findViewById(R.id.name);
        mIdEditText = (EditText) v.findViewById(R.id.id);
        mZoneTextView = (TextView) v.findViewById(R.id.editText3);
        mZoneTextView.setOnClickListener(this);
        mSegmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmented);
        User user = User.getInstance();
        if (user.getXm() != null) {
            mNameEditText.setText(user.getXm());
            mIdEditText.setText(user.getSfzh());
            mScoreEditText.setText(String.valueOf(user.getKscj()));
            mPostionEditText.setText(String.valueOf(user.getKspw()));
            mZoneTextView.setText(user.getKskqName());
            mSegmentedGroup.check(user.getKskl() == 1 ? R.id.btn_li_ke : R.id.btn_wen_ke);
            v.findViewById(R.id.btn_li_ke).setEnabled(false);
            v.findViewById(R.id.btn_wen_ke).setEnabled(false);

            mNameEditText.setEnabled(false);
            mIdEditText.setEnabled(false);
            mScoreEditText.setEnabled(false);
            mPostionEditText.setEnabled(false);
            mZoneTextView.setEnabled(false);
        }

        mSegmentedGroup.setOnCheckedChangeListener(this);
        return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		setupTitleBar();
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

	protected void setupTitleBar() {
		ActionBar bar = getActivity().getActionBar();
		bar.setTitle(R.string.smart_recomment_smart1);
		setHasOptionsMenu(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_next) {
			if (checkInput()) {
                updateKsxx(makeUserInfo());
			}
		} else if (v.getId() == R.id.editText3) {
			simpleDialog();
		}
	}

    private boolean checkInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.confirm, null);
        if (TextUtils.isEmpty(mNameEditText.getText().toString())) {
            builder.setMessage("请填写姓名");
            builder.show();
            return false;
        } else if (mIdEditText.getText().toString().trim().length() != 15
                && mIdEditText.getText().toString().trim().length() != 18) {
            builder.setMessage("请填写正确身份证号");
            builder.show();
            return false;
        } else if (TextUtils.isEmpty(mScoreEditText.getText().toString())) {
            builder.setMessage("请填写总分数");
            builder.show();
            return false;
        } else if (TextUtils.isEmpty(mPostionEditText.getText().toString())) {
            builder.setMessage("请填写排名");
            builder.show();
            return false;
        } else if (TextUtils.isEmpty(mZoneTextView.getText().toString())) {
            builder.setMessage("请填写考区");
            builder.show();
            return false;
        }

        return true;
    }

    private UserInfo makeUserInfo() {
        User user = User.getInstance();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setXm(user.getXm()); //姓名
        userInfo.setSfzh(user.getSfzh()); //身份证
        userInfo.setKscj(user.getKscj()); //分数
        userInfo.setKspw(user.getKspw()); //排名
        userInfo.setKskl(user.getKskl()); //理工
        userInfo.setKqdh(user.getKqdh()); //天津
        return userInfo;
    }

    private void updateKsxx(final UserInfo userInfo) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mBlockedDialogFragment.updateMessage("");
        mBlockedDialogFragment.show(ft, "block_dialog");

        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.KAO_SHENG_XIN_XI_XIU_GAI
                , null, new VolleyResponseListener(getActivity()) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    String ksxx = response.getString("ksxx");
                    UserInfo ui = JSON.parseObject(ksxx, UserInfo.class);
                    User user = User.getInstance();
                    user.setAccountId(ui.getAccountId());
                    user.setXm(ui.getXm());
                    user.setSfzh(ui.getSfzh());
                    user.setKscj(ui.getKscj());
                    user.setKspw(ui.getKspw());
                    user.setKskl(ui.getKskl());
                    user.setKsklName(ui.getKsklName());//科类名称
                    user.setKqdh(ui.getKskq());//考区代号
                    user.setKskqName(ui.getKskqName());
                    User.saveUser(user);

                    if (goSmartRecomnetStep2) {
                        next();
                    }
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(getActivity(), errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userInfo.getUserId());
                params.put("xm", userInfo.getXm());
                params.put("sfzh", userInfo.getSfzh());
                params.put("kscj", String.valueOf(userInfo.getKscj()));
                params.put("kspw", String.valueOf(userInfo.getKspw()));
                params.put("kskl", String.valueOf(userInfo.getKskl()));
                params.put("kskq", String.valueOf(userInfo.getKqdh()));
                return AppHelper.makeSimpleData("ksxxUpdate", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    private void next() {
        FragmentTransaction ft = getActivity().getFragmentManager()
                .beginTransaction();
        ft.replace(R.id.container, new SmartRecomentFragmentStep2(),
                "step2");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MenuHelper.menuItemSelected(getActivity(), 0, item);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.i(TAG, "理科" + checkedId);
		switch (checkedId) {
		case R.id.btn_li_ke:
			Toast.makeText(getActivity(), "理科", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_wen_ke:
			Toast.makeText(getActivity(), "文科", Toast.LENGTH_SHORT).show();
			break;
		default:
			// Nothing to do
			break;
		}
	}

	/**
	 * 单选Dialogs
	 */
	private void simpleDialog() {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择考区");
		builder.setSingleChoiceItems(mKaoQu, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						mZoneTextView.setText(mKaoQu[which]);
					}
				});
		dialog = builder.create();
		dialog.show();
	}
}