package com.education;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.TestActivity.Item2;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.User;
import com.education.utils.LogUtil;

public class ManulSearchSchollDetailActivity extends CommonBaseActivity
		implements OnClickListener {

	protected static final String TAG = null;
	private List<MajorItem> mZyData = new ArrayList<MajorItem>();
	private MajorItemAdapter mMajorAdapter;
	private ListView mMajorResultListView;
	public LayoutInflater mInflater;

	private Item2 item2;
	protected String mLuqupici;
	protected String mLuquqingkuang;
	private String yxdh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manul_search_scholl_detail);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMajorResultListView = (ListView) findViewById(R.id.filter_major_result_list);
		mMajorAdapter = new MajorItemAdapter();
		mMajorResultListView.setAdapter(mMajorAdapter);
		mMajorResultListView.setOnItemClickListener(mMajorAdapter);

		if (getIntent() != null) {
			mLuqupici = getIntent().getStringExtra("lqpc");
			mLuquqingkuang = getIntent().getStringExtra("lqqk");
			yxdh = getIntent().getStringExtra("yxdh");

			ActionBar bar = getActionBar();
			bar.setTitle(yxdh);
			bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
					| ActionBar.DISPLAY_SHOW_TITLE);
			bar.setHomeButtonEnabled(true);

			shaiXuanByCollege();
		}
		Log.i("TAG", getIntent().getExtras().toString());
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

	private class MajorItemAdapter extends BaseAdapter implements
			AdapterView.OnItemClickListener {
		private String xydh;

		public int getCount() {
			return mZyData.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolderMajor holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_zhuanye_by_colleage, null, false);
				holder = new ViewHolderMajor();
				holder.zymcTextView = (TextView) convertView
						.findViewById(R.id.zymc);
				holder.personNumberTextView = (TextView) convertView
						.findViewById(R.id.person_num);
				holder.t1 = (TextView) convertView.findViewById(R.id.text1);
				holder.t2 = (TextView) convertView.findViewById(R.id.text3);
				holder.t3 = (TextView) convertView.findViewById(R.id.text5);
				holder.tBtn = (TextView) convertView.findViewById(R.id.collect);
			} else {
				holder = (ViewHolderMajor) convertView.getTag();
			}
			convertView.setTag(holder);

			final MajorItem majorItem = mZyData.get(position);

			holder.zymcTextView.setText(majorItem.getZymc());
			holder.personNumberTextView.setText("录取" + majorItem.getLqrs()
					+ "人");
			holder.t1.setText(String.valueOf(majorItem.getMin()));
			holder.t2.setText(String.valueOf(majorItem.getMax()));
			holder.t3.setText(String.valueOf(majorItem.getPjz()));

			holder.tBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// final String yxdh, final String zydh,
					// final String zymc, final int lqpc
					shouCangZhuanYe(item2.getYxdh(), majorItem.getZydh(),
							majorItem.getZymc(), mLuqupici);
				}
			});
			return convertView;
		}

		public Object getItem(int position) {
			return mZyData.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(ManulSearchSchollDetailActivity.this,
					MajorDetailActivity.class);
			intent.putExtra("yxdh", yxdh);
			MajorItem item = ((MajorItem) parent.getAdapter().getItem(position));
			intent.putExtra("zydh", item.getZydh());
			intent.putExtra("yxpc", item.getLqpc());
			intent.putExtra(MajorDetailActivity.SOURSE_TAG, item.getSource());
			startActivity(intent);
		}

		private int getImgId(int position) {
			switch (position) {
			case 0:
				return R.drawable.xuexiao_1;
			case 1:
				return R.drawable.xuexiao_2;
			case 2:
				return R.drawable.xuexiao_3;
			default:
				return R.drawable.xuexiao_2;
			}
		}
	}

	private static class ViewHolderMajor {
		TextView zymcTextView;
		TextView personNumberTextView;
		TextView t1;
		TextView t2;
		TextView t3;
		TextView tBtn;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shaiXuanByCollege() {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST,
				Url.SHAI_XUAN_BY_COLLEGE,
				null,
				new VolleyResponseListener(ManulSearchSchollDetailActivity.this) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						Log.i(TAG, response.toJSONString());
						if (success) {
							String datas = response.getString("datas");
							item2 = JSON.parseObject(datas, Item2.class);
							mZyData.clear();
							mZyData.addAll(item2.getZydata());

							mMajorResultListView.setVisibility(View.VISIBLE);
							mMajorResultListView
									.setOnItemClickListener(mMajorAdapter);
							// mMajorAdapter.setXydh(schoolItem.getYxdh());
							mMajorAdapter.notifyDataSetChanged();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(
									ManulSearchSchollDetailActivity.this,
									errorData.getText(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								ManulSearchSchollDetailActivity.this,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("yxdh", yxdh);
				if (TextUtils.isEmpty(mLuqupici)) {
					mLuqupici = "3";
				}
				map.put("lqpc", mLuqupici);
				if (TextUtils.isEmpty(mLuquqingkuang)) {
					mLuquqingkuang = getLuquQingkuang(new Intent());
				}
				Log.i(TAG, "mLuquqingkuang=" + mLuquqingkuang);
				map.put("lqqk", mLuquqingkuang);
				map.put("kskl", String.valueOf(user.getKskl()));
				map.put("kqdh", String.valueOf(user.getKqdh()));
				Log.i(TAG, AppHelper.makeSimpleData("searchmajor", map)
						.toString());
				return AppHelper.makeSimpleData("searchmajor", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shouCangZhuanYe(final String yxdh, final String zydh,
			final String zymc, final String lqpc) {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST,
				Url.SHOU_CANG_ZHUAN_YE,
				null,
				new VolleyResponseListener(ManulSearchSchollDetailActivity.this) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						Log.i(TAG, response.toJSONString());
						if (success) {
							JSONObject result = response
									.getJSONObject("result");
							int status = result.getInteger("status");
							if (status == 1) {

							}
							Toast.makeText(
									ManulSearchSchollDetailActivity.this,
									result.getString("msgText"),
									Toast.LENGTH_SHORT).show();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(
									ManulSearchSchollDetailActivity.this,
									errorData.getText(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								ManulSearchSchollDetailActivity.this,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", user.getId());
				map.put("yxdh", yxdh); // 院校代号
				map.put("zydh", zydh); // 专业代号
				map.put("zymc", zymc); // 专业名称
				map.put("lqpc", String.valueOf(lqpc)); // 录取批次
				map.put("source", "1");// 收藏来源 1为手工筛选 2为智能推荐
				Log.i(TAG, Arrays.toString(map.entrySet().toArray()));
				return AppHelper.makeSimpleData("search", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private String getLuquQingkuang(Intent data) {
		String year = data.getStringExtra("year_score");
		String low_score = data.getStringExtra("low_score");
		String high_score = data.getStringExtra("high_score");

		if (TextUtils.isEmpty(year)) {
			Calendar c = Calendar.getInstance(Locale.getDefault());
			year = String.valueOf(c.get(Calendar.YEAR) - 1);
		}
		User mUser = User.getInstance();
		if (TextUtils.isEmpty(low_score))
			low_score = String.valueOf(mUser.getKscj() - 20);

		if (TextUtils.isEmpty(high_score))
			high_score = String.valueOf(mUser.getKscj() + 20);

		mLuquqingkuang = year + "|" + data.getIntExtra("score_type", 1) + "|"
				+ low_score + "|" + high_score;
		Log.i(TAG, "getLuquQingkuang mLuquqingkuang=" + mLuquqingkuang);
		return mLuquqingkuang;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
