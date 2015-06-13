package com.education;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.education.TestActivity.Item;
import com.education.TestActivity.Item4;
import com.education.TestActivity.Item5;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.utils.MenuHelper;

public class SmartRecomentFragmentStep3 extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentFragmentStep3.class
			.getSimpleName();

	private ListView mListView;
	private List<Item5> mItemList = new ArrayList<Item5>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mAdapter;
	private View mResult;
	private Item4 item;
	private String[] mPici;

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupTitleBar();
		mInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		mPici = mResources.getStringArray(R.array.luqu_pici_name);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_step3,
				container, false);
		mListView = (ListView) v.findViewById(R.id.listView);
		mAdapter = new ItemAdapter();
		mListView.setAdapter(mAdapter);
		mResult = v.findViewById(R.id.result);
		tuiJianXinXi();
		return v;
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

	protected void setupTitleBar() {
		ActionBar bar = getActivity().getActionBar();
		bar.setTitle(R.string.smart_recomment_smart3);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_btn) {

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setupTitleBar();
	}

	// 获取推荐信息
	private void tuiJianXinXi() {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.TUI_JIAN_XIN_XI, null,
				new VolleyResponseListener(getActivity()) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						Log.i(TAG, response.toJSONString());
						if (success) {
							String tjList = response.getString("tjList");
							item = JSON.parseObject(tjList, Item4.class);
							mItemList.clear();
							mItemList.addAll(item.getTjData());
							mAdapter.notifyDataSetChanged();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, TAG);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", user.getId());
				return AppHelper.makeSimpleData("recommenreport", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private class ItemAdapter extends BaseAdapter implements
			AdapterView.OnItemClickListener {
		public int getCount() {
			return mItemList.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_smart_setep3,
						null, false);
				holder = new ViewHolder();
				holder.rankTextView = (TextView) convertView
						.findViewById(R.id.ranking);
				holder.nameTextView = (TextView) convertView
						.findViewById(R.id.name);
				holder.typeTextView = (TextView) convertView
						.findViewById(R.id.type);
				holder.buckTextView = (TextView) convertView
						.findViewById(R.id.pici);
				holder.luquTextView = (TextView) convertView
						.findViewById(R.id.luqu_rate);
				holder.itemContainer = (ViewGroup) convertView
						.findViewById(R.id.item_container);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final Item5 ii = (Item5) mItemList.get(position);
			holder.rankTextView.setText(String.valueOf(position + 1));
			holder.nameTextView.setText(ii.getYxmc());
			holder.typeTextView.setText(ii.getYxDesc());
			holder.buckTextView.setText(mPici[ii.getYxpc()]);
			// holder.luquTextView.setText(mPici[ii.getYxpc()]);

			holder.itemContainer.removeAllViews();
			List<MajorItem> items = ii.getTjzy();
			for (int i = 0; i < items.size(); i++) {
				View item = mInflater.inflate(R.layout.item_smart_setep3_item,
						null);
				TextView mItemTitle1 = (TextView) item
						.findViewById(R.id.item_title);
				TextView mItemTitle2 = (TextView) item
						.findViewById(R.id.item_title2);
				TextView rate = (TextView) item.findViewById(R.id.rate);
				TextView mItemDesc = (TextView) item
						.findViewById(R.id.item_desc);

				final MajorItem majorItem = items.get(i);
				rate.setText(majorItem.getLqgl());
				mItemTitle1.setText("专业" + String.valueOf(i + 1));
				mItemTitle2.setText(majorItem.getZymc());

				mItemDesc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 收藏
						shouCangZhuanYe(ii.getYxdh(), majorItem.getZydh(),
								ii.getYxmc(), majorItem.getLqpc());
					}
				});
				item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 下级页面
						Intent intent = new Intent(getActivity(),
								SmartRecommentResultActivity.class);
						intent.putExtra("xuexiao", ii);
						intent.putExtra("zhuanye", majorItem);
						startActivity(intent);
					}
				});
				holder.itemContainer.addView(item);
			}
			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		}
	}

	private static class ViewHolder {
		TextView rankTextView;
		TextView nameTextView;
		TextView typeTextView;
		TextView buckTextView;
		TextView luquTextView;
		ViewGroup itemContainer;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shouCangZhuanYe(final String yxdh, final String zydh,
			final String zymc, final String lqpc) {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHOU_CANG_ZHUAN_YE, null,
				new VolleyResponseListener(getActivity()) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						if (success) {
							JSONObject result = response
									.getJSONObject("result");
							int status = result.getInteger("status");
							if (status == 1) {

							}
							Toast.makeText(getActivity(),
									result.getString("msgText"),
									Toast.LENGTH_SHORT).show();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(getActivity(), errorData.getText(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								getActivity(),
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
				map.put("lqpc", lqpc); // 录取批次
				map.put("source", "2");// 收藏来源 1为手工筛选 2为智能推荐
				return AppHelper.makeSimpleData("search", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

}