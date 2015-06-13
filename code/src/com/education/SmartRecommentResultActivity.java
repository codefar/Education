package com.education;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.TestActivity.Item5;
import com.education.TestActivity.Item6;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

public class SmartRecommentResultActivity extends FragmentBaseActivity
		implements View.OnClickListener {

	protected static final String TAG = SmartRecommentResultActivity.class
			.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	private TextView mNameTextView, mPiciTextView;

	private Item5 mXueXiaoItem;
	private MajorItem mMajorItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_recomment_result);
		setupTitleBar();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mNameTextView = (TextView) findViewById(R.id.name);
		mPiciTextView = (TextView) findViewById(R.id.pici);

		mTabsAdapter = new TabsAdapter(getFragmentManager());
		Intent intent = getIntent();
		if (intent != null) {
			mXueXiaoItem = (Item5) intent.getSerializableExtra("xuexiao");
			mMajorItem = (MajorItem) intent.getSerializableExtra("zhuanye");
			zhuanYeFenXiBaoGao();
		}
	}

	Item6 item;

	public Item6 getItem() {
		return item;
	}

	// 单专业分析报告
	private void zhuanYeFenXiBaoGao() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.ZHUAN_YE_FEN_XI_BAO_GAO, null,
				new VolleyResponseListener(this) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						Log.i(TAG, response.toJSONString());
						if (success) {
							String zyfxdata = response.getString("zyfxdata");
							item = JSON.parseObject(zyfxdata, Item6.class);
							Toast.makeText(SmartRecommentResultActivity.this,
									"size: " + item.getXgfx().size(),
									Toast.LENGTH_SHORT).show();
							mViewPager.setAdapter(mTabsAdapter);
							mNameTextView.setText(item.getZymc());
							mPiciTextView.setText(item.getYxpc());

						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(SmartRecommentResultActivity.this,
									errorData.getText(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								SmartRecommentResultActivity.this,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("yxdh", mXueXiaoItem.getYxdh());
				map.put("zydh", mMajorItem.getZydh());
				map.put("yxpc", String.valueOf(mXueXiaoItem.getYxpc()));
				map.put("kskl", String.valueOf(User.getInstance().getKskl()));
				map.put("kqdh", String.valueOf(User.getInstance().getKqdh()));
				return AppHelper.makeSimpleData("zyfx", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	@Override
	public void onClick(View v) {
	}

	public static class TabsAdapter extends FragmentPagerAdapter {

		public TabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new SmartRecomentResult1Fragment();
			} else if (position == 1) {
				return new SmartRecomentResult2Fragment();
			} else if (position == 2) {
				return new SmartRecomentResult3Fragment();
			} else {
				return new SmartRecomentResult4Fragment();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		default:
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	protected void setupTitleBar() {
		ActionBar bar = getActionBar();
		bar.setTitle(R.string.single_major_report);
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE);
		bar.setHomeButtonEnabled(true);
	}
}
