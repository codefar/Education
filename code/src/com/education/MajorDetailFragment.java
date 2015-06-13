package com.education;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.TestActivity.Item6;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.HistoryMajor;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.utils.MenuHelper;
import com.education.widget.SimpleBlockedDialogFragment;

public class MajorDetailFragment extends CommonFragment implements
		OnClickListener {

	private static final String TAG = MajorDetailFragment.class.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	private ListView mResultListView;
	protected LayoutInflater mInflater;
	List<HistoryMajor> mHistoryMajorItems = new ArrayList<HistoryMajor>();
	ItemAdapter mAdpter;
	Item6 mItem;

	private String yxdh;
	private String zydh;
	private String yxpc;
	
	public static MajorDetailFragment createInstance (Bundle param){
		MajorDetailFragment instance = new MajorDetailFragment();
		instance.setArguments(param);
		return instance;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle param =  getArguments();
		yxdh = param.getString("yxdh", "");
		zydh = param.getString("zydh", "");
		yxpc = param.getString("yxpc", "");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_result2,
				container, false);
		mInflater = inflater;
		mResultListView = (ListView) v.findViewById(R.id.listView);
		mAdpter = new ItemAdapter();
		mResultListView.setAdapter(mAdpter);
		zhuanYeFenXiBaoGao();
		return v;
	}

	@Override
	public void onClick(View v) {

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

	private class ItemAdapter extends BaseAdapter {
		public int getCount() {
			return mHistoryMajorItems.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_smart_recomment_result2, null, false);
				holder = new ViewHolder();
				holder.yearTextView = (TextView) convertView
						.findViewById(R.id.year);
				holder.scoreTextView = (TextView) convertView
						.findViewById(R.id.score);
				holder.personNumTextView = (TextView) convertView
						.findViewById(R.id.person_num);

				holder.t1TextView = (TextView) convertView
						.findViewById(R.id.text1);
				holder.t3TextView = (TextView) convertView
						.findViewById(R.id.text3);
				holder.t5TextView = (TextView) convertView
						.findViewById(R.id.text5);

				holder.t11TextView = (TextView) convertView
						.findViewById(R.id.text11);
				holder.t13TextView = (TextView) convertView
						.findViewById(R.id.text13);
				holder.t15TextView = (TextView) convertView
						.findViewById(R.id.text15);

				holder.t21TextView = (TextView) convertView
						.findViewById(R.id.text21);
				holder.t23TextView = (TextView) convertView
						.findViewById(R.id.text23);
				holder.t25TextView = (TextView) convertView
						.findViewById(R.id.text25);

				holder.mScrollView = (LinearLayout) convertView
						.findViewById(R.id.all_score);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HistoryMajor item = mHistoryMajorItems.get(position);
			holder.yearTextView.setText(String.valueOf(item.getYear()));
			String type = User.getInstance().getKskl() == 1 ? "理工" : "文史";
			holder.scoreTextView.setText(type + mItem.getYxpc() + "分数线:"
					+ String.valueOf(item.getLqline()));
			holder.personNumTextView.setText("共录取:"
					+ String.valueOf(item.getLqrs()) + "人");

			holder.t1TextView.setText(String.valueOf(item.getMaxpw()));
			holder.t3TextView.setText(String.valueOf(item.getMinpw()));
			holder.t5TextView.setText(String.valueOf(item.getPjpw()));

			holder.t11TextView.setText(String.valueOf(item.getMaxcj()));
			holder.t13TextView.setText(String.valueOf(item.getMincj()));
			holder.t15TextView.setText(String.valueOf(item.getPjcj()));

			holder.t21TextView.setText(String.valueOf(item.getMaxxc()));
			holder.t23TextView.setText(String.valueOf(item.getMinxc()));
			holder.t25TextView.setText(String.valueOf(item.getPjxc()));

			holder.mScrollView.removeAllViews();
			ArrayList<Integer> cjList = (ArrayList<Integer>) item.getCjlist();

			for (int i = 0; i < cjList.size(); i++) {
				View scrollItem = mInflater.inflate(
						R.layout.item_smart_recomment_result2_sub_item, null,
						false);
				int width14 = (int) ((EduApp.sScreenWidth - getResources()
						.getDimensionPixelOffset(R.dimen.dimen_68_dip)) / 4F);
				scrollItem.setMinimumWidth(width14);
				TextView sub_score_rank = (TextView) scrollItem
						.findViewById(R.id.sub_score_rank);
				TextView sub_score = (TextView) scrollItem
						.findViewById(R.id.sub_score);
				sub_score_rank.setText(getString(R.string.rank_index, i + 1));
				sub_score.setText(String.valueOf(cjList.get(i)));
				holder.mScrollView.addView(scrollItem);
			}
			return convertView;
		}

		public Object getItem(int position) {
			return mHistoryMajorItems.get(position);
		}
	}

	private static class ViewHolder {
		TextView yearTextView;
		TextView scoreTextView;
		TextView personNumTextView;

		TextView t1TextView;
		TextView t3TextView;
		TextView t5TextView;

		TextView t11TextView;
		TextView t13TextView;
		TextView t15TextView;

		TextView t21TextView;
		TextView t23TextView;
		TextView t25TextView;

		LinearLayout mScrollView;
	}

	// 单专业分析报告
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void zhuanYeFenXiBaoGao() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.ZHUAN_YE_FEN_XI_BAO_GAO, null,
				new VolleyResponseListener(getActivity()) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						Log.i(TAG, response.toJSONString());
						if (success) {
							String zyfxdata = response.getString("zyfxdata");
							mItem = JSON.parseObject(zyfxdata, Item6.class);
							mHistoryMajorItems.clear();
							mHistoryMajorItems.addAll(mItem.getLssj());
							mAdpter.notifyDataSetChanged();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(getActivity(),
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
								getActivity(),
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("yxdh", yxdh);
				map.put("zydh", zydh);
				map.put("yxpc", String.valueOf(yxpc));
				map.put("kskl", String.valueOf(User.getInstance().getKskl()));
				map.put("kqdh", String.valueOf(User.getInstance().getKqdh()));
				return AppHelper.makeSimpleData("zyfx", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}
}