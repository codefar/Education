package com.education;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.TestActivity.Answer;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.Questions;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.utils.MenuHelper;
import com.education.widget.SimpleBlockedDialogFragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

public class SmartRecomentFragmentStep2 extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentFragmentStep2.class
			.getSimpleName();

	private Button mAnsABtn, mAnsBBtn, mAnsCBtn, mAnsDBtn;
	private TextView mTotalNumTextView, mCurrentNumTextView, mQuestionTextView;
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	private ArrayList<Answer> answerList = new ArrayList<Answer>();
    private Activity mActivity;

    /**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mActivity = getActivity();
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
		mAnsCBtn = (Button) v.findViewById(R.id.btn_c);
		mAnsCBtn.setOnClickListener(this);
		mAnsDBtn = (Button) v.findViewById(R.id.btn_d);
		mAnsDBtn.setOnClickListener(this);

		mTotalNumTextView = (TextView) v.findViewById(R.id.total);
		mCurrentNumTextView = (TextView) v.findViewById(R.id.current);
		mQuestionTextView = (TextView) v.findViewById(R.id.question);

        huiDaZhuangTai();
		return v;
	}

    //回答状态
    private void huiDaZhuangTai() {
        mBlockedDialogFragment.updateMessage(getString(R.string.get_questions));
        mBlockedDialogFragment.show(getFragmentManager(), "block_dialog");

        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.XING_GE_CE_SHI_HUI_DA_ZHUANG_TAI
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    int status = response.getInteger("status");
                    if (status == 1) {
                        mBlockedDialogFragment.dismissAllowingStateLoss();
                        FragmentTransaction ft = mActivity.getFragmentManager()
                                .beginTransaction();
//                        ft.remove(SmartRecomentFragmentStep2.this);
                        SmartRecomentFragmentStep3 fragmentStep3 = new SmartRecomentFragmentStep3();
                        fragmentStep3.setArguments(new Bundle());
                        ft.replace(R.id.container, fragmentStep3, "step3");
                        ft.commit();
                    } else {
                        getQuestions();
                    }
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", User.getInstance().getId());
                return AppHelper.makeSimpleData("start", map);
            }
        };
        EduApp.sRequestQueue.add(request);
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
		ActionBar bar = mActivity.getActionBar();
		bar.setTitle(R.string.smart_recomment_smart2);
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "mCurrentQueston = " + mCurrentQueston + " total "
				+ mQuestions.getCount());
		switch (v.getId()) {
		case R.id.btn_a:
			Answer answera = new Answer();
			answera.setDm(mQuestions.getTmDatas().get(mCurrentQueston - 1).getDm());
			answera.setAns("A");
			answerList.add(answera);
			gotoNextQuestion();
			mCurrentQueston++;
			break;
		case R.id.btn_b:
			Answer answerb = new Answer();
			answerb.setDm(mQuestions.getTmDatas().get(mCurrentQueston - 1).getDm());
			answerb.setAns("B");
			answerList.add(answerb);
			gotoNextQuestion();
			mCurrentQueston++;
			break;
		case R.id.btn_c:
			Answer answerc = new Answer();
			answerc.setDm(mQuestions.getTmDatas().get(mCurrentQueston - 1).getDm());
			answerc.setAns("C");
			answerList.add(answerc);
			gotoNextQuestion();
			mCurrentQueston++;
			break;
		case R.id.btn_d:
			Answer answerd = new Answer();
			answerd.setDm(mQuestions.getTmDatas().get(mCurrentQueston - 1).getDm());
			answerd.setAns("D");
			answerList.add(answerd);
			gotoNextQuestion();
			mCurrentQueston++;
			break;
		default:
			break;
		}
	}

	private void gotoStep3() {
		FragmentTransaction ft = mActivity.getFragmentManager()
				.beginTransaction();
		SmartRecomentFragmentStep3 fragmentStep3 = new SmartRecomentFragmentStep3();
		fragmentStep3.setArguments(new Bundle());
		ft.replace(R.id.container, fragmentStep3, "step3");
		ft.commit();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MenuHelper.menuItemSelected(mActivity, 0, item);
		return super.onOptionsItemSelected(item);
	}

	private int mCurrentQueston = 0;

	private void gotoNextQuestion() {
		if (mCurrentQueston < mQuestions.getCount()) {
            int next = mCurrentQueston + 1;
            List<Questions.Question> questionList = mQuestions.getTmDatas();
			mCurrentNumTextView.setText(String.valueOf(next));
			mQuestionTextView.setText(questionList.get(next - 1).getXm());
		} else if (mCurrentQueston == mQuestions.getCount()) {
            answerQuestions(answerList);
        }
	}

	private Questions mQuestions = new Questions();

	private void getQuestions() {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.XING_GE_CE_SHI_TI_MU, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						Log.i(TAG, response.toJSONString());
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							String tmList = response.getString("tmList");
							Questions questions = JSON.parseObject(tmList,
									Questions.class);
							mQuestions = questions;
							mTotalNumTextView.setText(getString(
									R.string.index_questions,
									String.valueOf(mQuestions.getCount())));
							mCurrentQueston = 0;
							gotoNextQuestion();
                            mCurrentQueston++;
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
                            Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						LogUtil.logNetworkResponse(volleyError, TAG);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return AppHelper.makeSimpleData("getTms", null);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private void answerQuestions(final List<Answer> answerList) {
		mBlockedDialogFragment.updateMessage(getString(R.string.post_answers));
		mBlockedDialogFragment.show(getFragmentManager(), "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.XING_GE_CE_SHI_HUI_DA, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							gotoStep3();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(mActivity, errorData.getText(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								mActivity,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("request", "tmhd");
				int size = answerList.size();
				List<JSONObject> array = new ArrayList<JSONObject>();
				for (int i = 0; i < size; i++) {
					Answer a = answerList.get(i);
					JSONObject item = new JSONObject();
					item.put("dm", a.getDm());
					item.put("ans", a.getAns());
					array.add(item);
				}

				JSONObject params = new JSONObject();
				params.put("userId", user.getId());
				params.put("count", String.valueOf(10));
				params.put("hdDatas", array);
				jsonObject.put("params", params);
				map.put("userData", jsonObject.toJSONString());
				if (EduApp.DEBUG) {
					Log.d(TAG, "map: " + map);
				}
				return map;
			}
		};
		EduApp.sRequestQueue.add(request);
	}

}