package com.education;

import java.util.HashMap;
import java.util.Map;

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
import com.education.utils.MenuHelper;
import com.education.widget.SimpleBlockedDialogFragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SmartRecomentFragmentStep2 extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentFragmentStep2.class
			.getSimpleName();

	private Button mAnsABtn, mAnsBBtn, mAnsCBtn, mAnsDBtn;
	private TextView mTotalNumTextView, mCurrentNumTextView, mQuestionTextView;
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();
	
	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupTitleBar();
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_step2,
				container, false);
		mAnsABtn = (Button) v.findViewById(R.id.btn_a);
		mAnsABtn.setOnClickListener(this);
		mAnsBBtn = (Button) v.findViewById(R.id.btn_b);
		mAnsBBtn.setOnClickListener(this);
		mAnsCBtn = (Button) v.findViewById(R.id.btn_b);
		mAnsCBtn.setOnClickListener(this);
		mAnsDBtn = (Button) v.findViewById(R.id.btn_d);
		mAnsDBtn.setOnClickListener(this);

		mTotalNumTextView = (TextView) v.findViewById(R.id.total);
		mCurrentNumTextView = (TextView) v.findViewById(R.id.current);
		mQuestionTextView = (TextView) v.findViewById(R.id.question);
		
		fetchQuestions();
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
		bar.setTitle(R.string.smart_recomment_smart2);
		setHasOptionsMenu(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_a:
			gotoNextQuestion();
			break;
		case R.id.btn_b:
			gotoNextQuestion();
			break;
		case R.id.btn_c:
			gotoNextQuestion();
			break;
		case R.id.btn_d:
			gotoNextQuestion();
			gotoStep3();
			break;
		default:
			break;
		}
	}

	private void gotoStep3() {
		FragmentTransaction ft = getActivity().getFragmentManager()
				.beginTransaction();
		SmartRecomentFragmentStep3 fragmentStep3 = new SmartRecomentFragmentStep3();
		fragmentStep3.setArguments(new Bundle());
		ft.replace(R.id.container, fragmentStep3,
				"step3");
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

	private int mCurrentQueston = 1;

	private void gotoNextQuestion() {
		mCurrentQueston++;
		mCurrentNumTextView.setText("" + mCurrentQueston);
		mQuestionTextView.setText("问题" + String.valueOf(mCurrentQueston));
	}
	
	private void fetchQuestions(){
		mBlockedDialogFragment.show(getFragmentManager(), "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.GET_QUESTION
                , null, new VolleyResponseListener(getActivity()) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
            	Log.i(TAG, response.toJSONString());
                if (success) {
                    String data = response.getString("userInfo");
                    
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    mBlockedDialogFragment.dismissAllowingStateLoss();
                    Toast.makeText(getActivity(), errorData.getText(), Toast.LENGTH_SHORT).show();
                }
                mBlockedDialogFragment.dismissAllowingStateLoss();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mBlockedDialogFragment.dismissAllowingStateLoss();
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return AppHelper.makeSimpleData("", map);
            }
        };
        EduApp.sRequestQueue.add(request);
	}
}