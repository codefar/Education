package com.education;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.CollegeItem;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolunteerCollectionFragment extends CommonFragment {

	private static final String TAG = SmartRecomentFragment.class
			.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();

	protected LayoutInflater mInflater;
	protected Resources mResources;

	private static final int TYPE_COLLEGE = 0;
	private static final int TYPE_MAJOR = 1;
	private int mType = TYPE_COLLEGE;
	private ListView mListView, mMajorListView;
	private ArrayList<MajorItem> mMajorItemList = new ArrayList<MajorItem>();
	private ArrayList<CollegeItem> mCollegeItemList = new ArrayList<CollegeItem>();
	private ItemAdapter mItemAdapter;
	private MajorItemAdapter mMajorItemAdapter;
	private RelativeLayout mHeaderLayout;
	private TextView mHeaderTitleTextView;
	private Activity mActivity;

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResources = getResources();
		mActivity = getActivity();
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_volunteer, container,
				false);

		mInflater = inflater;
		mListView = (ListView) v.findViewById(R.id.list);
		mMajorListView = (ListView) v.findViewById(R.id.majorlist);
		mItemAdapter = new ItemAdapter();
		mMajorItemAdapter = new MajorItemAdapter();
		mListView.setAdapter(mItemAdapter);
		mMajorListView.setAdapter(mMajorItemAdapter);
		mListView.setOnItemClickListener(mItemAdapter);
		mMajorListView.setOnItemClickListener(mMajorItemAdapter);

		mHeaderLayout = (RelativeLayout) mInflater.inflate(
				R.layout.header_collection_list, null);
		mHeaderTitleTextView = (TextView) mHeaderLayout
				.findViewById(R.id.header_title);
		mMajorListView.addHeaderView(mHeaderLayout);
		shouCangYuanXiaoLieBiao();
		return v;
	}

	public boolean back() {
		if (mMajorListView.getVisibility() == View.VISIBLE) {
			mMajorListView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mMajorItemList.clear();
			mCurrentCollegeItem = null;
			return false;
		} else {
			return true;
		}
	}

	private class ItemAdapter extends BaseAdapter implements
			AdapterView.OnItemClickListener {
		public int getCount() {
			return mCollegeItemList.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_volunteer_collection, null, false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.descTextView = (TextView) convertView
						.findViewById(R.id.desc);
				holder.quxiaoTextView = (TextView) convertView
						.findViewById(R.id.quxiao);
				holder.iconImageView = (ImageView) convertView
						.findViewById(R.id.icon);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			CollegeItem collegeItem = mCollegeItemList.get(position);
			holder.descTextView.setText(collegeItem.getZysl());
			holder.quxiaoTextView.setVisibility(View.GONE);
			holder.titleTextView.setText(collegeItem.getYxmc());
			holder.iconImageView.setImageBitmap(BitmapFactory.decodeResource(
					mResources, getImgId(position)));
			return convertView;
		}

		public Object getItem(int position) {
			return mCollegeItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CollegeItem collegeItem = (CollegeItem) mCollegeItemList
					.get(position);
			shouCangZhuanYeLieBiao(collegeItem);
		}
	}

	private class MajorItemAdapter extends BaseAdapter implements
			AdapterView.OnItemClickListener {
		public int getCount() {
			return mMajorItemList.size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_volunteer_collection, null, false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.descTextView = (TextView) convertView
						.findViewById(R.id.desc);
				holder.quxiaoTextView = (TextView) convertView
						.findViewById(R.id.quxiao);
				holder.iconImageView = (ImageView) convertView
						.findViewById(R.id.icon);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			MajorItem majorItem = mMajorItemList.get(position);
			int source = majorItem.getSource();
			if (source == 0) {
				// 未收藏
			} else if (source == 1) {
				holder.descTextView.setText("手工筛选");
			} else if (source == 2) {
				holder.descTextView.setText("智能推荐");
			}
			holder.quxiaoTextView.setVisibility(View.GONE); // 以后可能会添加取消收藏
			holder.titleTextView.setText(majorItem.getZymc());
			holder.iconImageView.setImageBitmap(BitmapFactory.decodeResource(
					mResources, getImgId(position - 1)));

			return convertView;
		}

		public Object getItem(int position) {
			return mMajorItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent(getActivity(), MajorDetailActivity.class);
			intent.putExtra("yxdh", mCurrentCollegeItem.getYxdh());
			MajorItem item = ((MajorItem) parent.getAdapter().getItem(position));
			intent.putExtra("zydh", item.getZydh());
			intent.putExtra("yxpc", item.getLqpc());
			intent.putExtra(MajorDetailActivity.SOURSE_TAG, item.getSource());
			startActivity(intent);
		}
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

	public static class Item {
		private String yxdh;
		private String yxmc;
		private List<MajorItem> sczyDatas = new ArrayList<MajorItem>();

		public String getYxdh() {
			return yxdh;
		}

		public void setYxdh(String yxdh) {
			this.yxdh = yxdh;
		}

		public String getYxmc() {
			return yxmc;
		}

		public void setYxmc(String yxmc) {
			this.yxmc = yxmc;
		}

		public List<MajorItem> getSczyDatas() {
			return sczyDatas;
		}

		public void setSczyDatas(List<MajorItem> sczyDatas) {
			this.sczyDatas = sczyDatas;
		}
	}

	private static class ViewHolder {
		TextView titleTextView;
		TextView descTextView;
		TextView quxiaoTextView;
		ImageView iconImageView;
		View dividerView;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shouCangYuanXiaoLieBiao() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");

		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHOU_CANG_YUAN_XIAO_LIE_BIAO, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							mCollegeItemList.clear();
							JSONArray array = response.getJSONArray("datas");
							int size = array.size();
							for (int i = 0; i < size; i++) {
								String item = array.getString(i);
								CollegeItem collegeItem = JSON.parseObject(
										item, CollegeItem.class);
								mCollegeItemList.add(collegeItem);
							}
							mItemAdapter.notifyDataSetChanged();
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
				map.put("userId", user.getId()); // user.getId()
				return AppHelper.makeSimpleData("getcollectschool", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private CollegeItem mCurrentCollegeItem;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shouCangZhuanYeLieBiao(final CollegeItem collegeItem) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHOU_CANG_ZHUAN_YE_LIE_BIAO, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							String datas = response.getString("datas");
							Item item = JSON.parseObject(datas, Item.class);
							mMajorItemList.clear();
							mMajorItemList.addAll(item.getSczyDatas());
							mHeaderTitleTextView.setText(mResources.getString(
									R.string.major_collected_in_college,
									mMajorItemList.size()));
							mMajorItemAdapter.notifyDataSetChanged();
							mCurrentCollegeItem = collegeItem;
							mMajorListView.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(mActivity, errorData.getText(),
									Toast.LENGTH_SHORT).show();
							mCurrentCollegeItem = null;
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mCurrentCollegeItem = null;
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
				map.put("userId", user.getId()); // user.getId()
				map.put("yxdh", collegeItem.getYxdh());
				return AppHelper.makeSimpleData("getcollectmajor", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}
}