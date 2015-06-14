package com.education;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import com.education.entity.CollegeItem;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.ShaiXuanConditionItem;
import com.education.entity.ShaiXuanJieGuo;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;

public class ManualTimFragment extends CommonFragment implements
		View.OnClickListener, TextWatcher {
	private static final String TAG = ManualTimFragment.class.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	private ListView mSearchResulListView, mMajorResultListView;;
	private List<SchoolItem> mItemList = new ArrayList<SchoolItem>();
	private List<MajorItem> mZyData = new ArrayList<MajorItem>();

	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mItemAdapter;
	private MajorItemAdapter mMajorAdapter;
	private TextView mFilterTextView, mScoreRankText, mSchoolRankText,
			mUserScoreText, mUserRankText, mUserTypeText, mUserLocation,
			mSchoolNumbersText, mMajorNumbersText;
	private Intent mShaixuanIntent;
	private ImageView mScoreRankImg, mSchoolRankImg;
	private int clickPinyinNumbers, clickSchoolNumbers;
	private String mSchoolNumbers, mLuquQingkuang;
	private int mMajorNumbers;
	private ArrayList<ShaiXuanConditionItem> conditionItemList;
	private Activity mActivity;
	private int mPageNo = 1;
	private User mUser;
	private EditText mTitleSearchEdit;
	private String mLuqupici, mLuquqingkuang;

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResources = getResources();
		mActivity = getActivity();
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mUser = User.getInstance();
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_manual, container,
				false);
		initView(v);

		mLuquQingkuang = getLuquQingkuang(new Intent());
		mItemAdapter = new ItemAdapter();
		mMajorAdapter = new MajorItemAdapter();
		mShaixuanIntent = new Intent(mActivity, ShaiXuanActivity.class);
		mSearchResulListView.setAdapter(mItemAdapter);
		mMajorResultListView.setAdapter(mMajorAdapter);
		mMajorResultListView.setOnItemClickListener(mMajorAdapter);
		displayCollege();
		mFilterTextView.setOnClickListener(this);
		mScoreRankText.setOnClickListener(this);
		mSchoolRankText.setOnClickListener(this);
		mLuqupici = getLuqupici(null);
		mLuquQingkuang = getLuquQingkuang(new Intent());
		postData2Server(null, "", true);
		return v;
	}

	private void initView(View v) {
		mSearchResulListView = (ListView) v
				.findViewById(R.id.filter_school_result_list);
		mMajorResultListView = (ListView) v
				.findViewById(R.id.filter_major_result_list);
		mFilterTextView = (TextView) v.findViewById(R.id.filter_textview);
		mScoreRankText = (TextView) v.findViewById(R.id.paixu_pinyin);
		mSchoolRankText = (TextView) v.findViewById(R.id.paiming_school);
		mScoreRankImg = (ImageView) v.findViewById(R.id.paixu_pinyin_img);
		mSchoolRankImg = (ImageView) v.findViewById(R.id.paiming_school_img);
		mUserScoreText = (TextView) v.findViewById(R.id.user_score);
		mUserRankText = (TextView) v.findViewById(R.id.user_rank);
		mUserTypeText = (TextView) v.findViewById(R.id.user_type);
		mUserLocation = (TextView) v.findViewById(R.id.user_location);
		mSchoolNumbersText = (TextView) v.findViewById(R.id.school_numbers);
		mMajorNumbersText = (TextView) v.findViewById(R.id.major_numbers);
		mTitleSearchEdit = (EditText) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.search_school_major_edit);

		mTitleSearchEdit
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == KeyEvent.ACTION_DOWN
								|| actionId == EditorInfo.IME_ACTION_DONE) {
							postData2Server(conditionItemList, v.getText()
									.toString().trim(), false);
						}
						return false;
					}
				});
		mTitleSearchEdit.addTextChangedListener(this);

		User user = User.getInstance();
		if (user != null) {
			Log.w("wutl", user.toString());
			mUserScoreText.setText(user.getKscj() + "");
			mUserRankText.setText(user.getKspw() + "");
			int kskl = user.getKskl();
			mUserTypeText.setText(kskl == 1 ? "理工" : "文史");
			mUserLocation.setText(user.getKskqName() + "");
		}
	}

	private void setDataSource(ShaiXuanJieGuo result) {
		mSchoolNumbers = String.valueOf(result.getYxzydata().size());
		mMajorNumbers = 0;
		List<CollegeItem> schoolList = result.getYxzydata();
		mItemList.clear();
		for (int i = 0; i < schoolList.size(); i++) {
			SchoolItem localItem = new SchoolItem(schoolList.get(i).getZysl(),
					schoolList.get(i).getYxmc());
			mMajorNumbers += Integer.valueOf(schoolList.get(i).getZysl())
					.intValue();
			setSchoolImg(localItem, i % 3);
			localItem.setYxdh(schoolList.get(i).getYxdh());
			mItemList.add(localItem);
		}
		mItemAdapter.notifyDataSetChanged();
		mSchoolNumbersText.setText(String.format(
				getResources().getString(R.string.school_numbers),
				mSchoolNumbers));
		mMajorNumbersText.setText(String.format(
				getResources().getString(R.string.major_numbers),
				String.valueOf(mMajorNumbers)));
	}

	private void setSchoolImg(SchoolItem localItem, int position) {
		switch (position) {
		case 0:
			localItem.setSchoolImg(R.drawable.xuexiao_1);
			break;
		case 1:
			localItem.setSchoolImg(R.drawable.xuexiao_2);
			break;
		case 2:
			localItem.setSchoolImg(R.drawable.xuexiao_3);
			break;
		default:
			localItem.setSchoolImg(R.drawable.xuexiao_2);
			break;
		}
	}

	private void displayCollege() {
		mSearchResulListView.setOnItemClickListener(mItemAdapter);
		mItemAdapter.notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView schoolNameTextView;
		TextView majorNumberTextView;
		ImageView schoolImg;
		View dividerView;
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
				convertView = mInflater.inflate(R.layout.item_college, null,
						false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.schoolNameTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.majorNumberTextView = (TextView) convertView
						.findViewById(R.id.desc);
				holder.schoolImg = (ImageView) convertView
						.findViewById(R.id.icon);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			SchoolItem schoolItem = mItemList.get(position);
			holder.schoolImg.setImageBitmap(BitmapFactory.decodeResource(
					mResources, schoolItem.getSchoolImg()));
			holder.schoolNameTextView.setText(schoolItem.getSchoolName());
			holder.majorNumberTextView.setText(schoolItem.getMarjorNumber());
			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String yxdh = mItemList.get(position).getYxdh();
			String luqupic = mLuqupici;
			String lqqk = mLuquQingkuang;

			SchoolItem schoolItem = (SchoolItem) parent.getAdapter().getItem(
					position);
			shaiXuanByCollege(schoolItem);
		}
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
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_college, null,
						false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.schoolNameTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.majorNumberTextView = (TextView) convertView
						.findViewById(R.id.desc);
				holder.schoolImg = (ImageView) convertView
						.findViewById(R.id.icon);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			MajorItem majorItem = mZyData.get(position);

			holder.schoolNameTextView.setText(majorItem.getZymc());
			int source = majorItem.getSource();
			if (source == 0) {
				// 未收藏
				holder.majorNumberTextView.setText("");
			} else if (source == 1) {
				holder.majorNumberTextView.setText("手工筛选");
			} else if (source == 2) {
				holder.majorNumberTextView.setText("智能推荐");
			}
			holder.schoolImg.setImageBitmap(BitmapFactory.decodeResource(
					mResources, getImgId(position - 1)));

			return convertView;
		}

		public Object getItem(int position) {
			return mZyData.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(), MajorDetailActivity.class);
			intent.putExtra("yxdh", getXydh());
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

		public String getXydh() {
			return xydh;
		}

		public void setXydh(String xydh) {
			this.xydh = xydh;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void shaiXuanByCollege(final SchoolItem schoolItem) {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHAI_XUAN_BY_COLLEGE, null,
				new VolleyResponseListener(getActivity()) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						Log.i(TAG, response.toJSONString());
						if (success) {
							String datas = response.getString("datas");
							Item2 item = JSON.parseObject(datas, Item2.class);
							mZyData.clear();
							mZyData.addAll(item.getZydata());

							mSearchResulListView.setVisibility(View.INVISIBLE);
							mMajorResultListView.setVisibility(View.VISIBLE);
							mMajorResultListView
									.setOnItemClickListener(mMajorAdapter);
							mMajorAdapter.setXydh(schoolItem.getYxdh());
							mMajorAdapter.notifyDataSetChanged();
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
				map.put("yxdh", schoolItem.getSchoolName());
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

	private class SchoolItem {
		protected int schoolImg;
		protected String marjorNumber;
		protected String schoolName;
		protected String yxdh;

		public void setYxdh(String yxdh) {
			this.yxdh = yxdh;
		}

		public String getYxdh() {
			return yxdh;
		}

		public SchoolItem(String marjorNumber, String schoolName) {
			super();
			this.marjorNumber = marjorNumber;
			this.schoolName = schoolName;
		}

		public String getMarjorNumber() {
			return marjorNumber;
		}

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}

		public void setMarjorNumber(String marjorNumber) {
			this.marjorNumber = marjorNumber;
		}

		public void setSchoolImg(int schoolImg) {
			this.schoolImg = schoolImg;
		}

		public int getSchoolImg() {
			return schoolImg;
		}

		public String getSchoolName() {
			return schoolName;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		// 获取筛选条件
		if (requestCode == 1 && resultCode == 1) {

			Bundle bundle = data
					.getBundleExtra(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			// ArrayList<ShaiXuanConditionItem> conditionItemList =
			// bundle.getParcelableArrayList(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			conditionItemList = (ArrayList<ShaiXuanConditionItem>) bundle
					.getSerializable(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			mLuquQingkuang = getLuquQingkuang(data);
			postData2Server(conditionItemList, "", false);
			Log.w("wutl", "录取情况＝" + mLuquQingkuang);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getLuquQingkuang(Intent data) {
		String year = data.getStringExtra("year_score");
		String low_score = data.getStringExtra("low_score");
		String high_score = data.getStringExtra("high_score");

		if (TextUtils.isEmpty(year)) {
			Calendar c = Calendar.getInstance(Locale.getDefault());
			year = String.valueOf(c.get(Calendar.YEAR) - 1);
		}
		if (TextUtils.isEmpty(low_score))
			low_score = String.valueOf(mUser.getKscj() - 20);

		if (TextUtils.isEmpty(high_score))
			high_score = String.valueOf(mUser.getKscj() + 20);

		mLuquQingkuang = year + "|" + data.getIntExtra("score_type", 1) + "|"
				+ low_score + "|" + high_score;
		Log.i(TAG, "getLuquQingkuang mLuquqingkuang=" + mLuquqingkuang);
		return mLuquQingkuang;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void postData2Server(
			final ArrayList<ShaiXuanConditionItem> conditionlist,
			final String skey, final boolean isFirstInto) {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHAI_XUAN, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						if (success) {
							String datas = response.getString("datas");
							ShaiXuanJieGuo result = JSON.parseObject(datas,
									ShaiXuanJieGuo.class);
							if (result != null) {
								setDataSource(result);
								mItemAdapter.notifyDataSetChanged();
							}
							Log.d("wutl", "resutl=" + result.toString());
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
						LogUtil.logNetworkResponse(volleyError, "wutl");
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
				if (isFirstInto) {
					map.put("pageno", String.valueOf(1));
					map.put("skey", skey);
					map.put("yxss", "");
					map.put("yxlx", "");
					map.put("yxxz", "");
					map.put("lqpc", mLuqupici);
					map.put("lqqk", getLuquQingkuang(new Intent()));
					map.put("kskl", String.valueOf(user.getKskl()));
					map.put("kqdh", String.valueOf(user.getKqdh()));
				} else {
					map.put("pageno", String.valueOf(1));
					map.put("skey", skey);
					map.put("yxss", getYxss(conditionlist));
					map.put("yxlx", getYxlx(conditionlist));
					map.put("yxxz", getYxxz(conditionlist));
					map.put("lqpc", getLuqupici(conditionlist));
					map.put("lqqk", mLuquQingkuang);
					map.put("kskl", String.valueOf(user.getKskl()));
					map.put("kqdh", String.valueOf(user.getKqdh()));
				}
				Log.w("wutl", "map="
						+ AppHelper.makeSimpleData("search", map).toString());
				return AppHelper.makeSimpleData("search", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private String getYxss(ArrayList<ShaiXuanConditionItem> conditionlist) {
		StringBuffer buffer = new StringBuffer();
		if (conditionlist != null) {
			for (ShaiXuanConditionItem item : conditionlist) {
				if (item.getConditionName().equals("院校省份")) {
					if (item.getmSubDetailConditionItemList() != null) {
						for (int i = 0; i < item
								.getmSubDetailConditionItemList().size(); i++) {
							buffer.append(item.getmSubDetailConditionItemList()
									.get(i).getProviceId());
							if (i != item.getmSubDetailConditionItemList()
									.size() - 1)
								buffer.append("|");
						}
					}

					break;
				}
			}
		}
		Log.w("wutl", "院校省份＝" + buffer.toString());
		if (TextUtils.isEmpty(buffer.toString()))
			return "";
		return buffer.toString();
	}

	private String getYxlx(ArrayList<ShaiXuanConditionItem> conditionlist) {
		StringBuffer buffer = new StringBuffer();
		if (conditionlist != null) {
			for (ShaiXuanConditionItem item : conditionlist) {
				if (item.getConditionName().equals("院校类型")) {
					if (item.getmSubDetailConditionItemList() != null) {
						for (int i = 0; i < item
								.getmSubDetailConditionItemList().size(); i++) {
							buffer.append(item.getmSubDetailConditionItemList()
									.get(i).getProviceId());
							if (i != item.getmSubDetailConditionItemList()
									.size() - 1)
								buffer.append("|");
						}
					}
					break;
				}
			}
		}
		Log.w("wutl", "院校类型＝" + buffer.toString());
		if (TextUtils.isEmpty(buffer.toString()))
			return "";
		return buffer.toString();
	}

	private String getYxxz(ArrayList<ShaiXuanConditionItem> conditionlist) {
		StringBuffer buffer = new StringBuffer();
		if (conditionlist != null) {
			for (ShaiXuanConditionItem item : conditionlist) {
				if (item.getConditionName().equals("院校性质")) {
					if (item.getmSubDetailConditionItemList() != null) {
						for (int i = 0; i < item
								.getmSubDetailConditionItemList().size(); i++) {
							buffer.append(item.getmSubDetailConditionItemList()
									.get(i).getProviceId());
							if (i != item.getmSubDetailConditionItemList()
									.size() - 1)
								buffer.append("|");
						}
					}
					break;
				}
			}
		}
		Log.w("wutl", "院校性质＝" + buffer.toString());
		if (TextUtils.isEmpty(buffer.toString()))
			return "";
		return buffer.toString();
	}

	private String getLuqupici(ArrayList<ShaiXuanConditionItem> conditionlist) {
		StringBuffer buffer = new StringBuffer();
		if (conditionlist != null) {
			for (ShaiXuanConditionItem item : conditionlist) {
				if (item.getConditionName().equals("录取批次")) {
					if (item.getmSubDetailConditionItemList() != null) {
						for (int i = 0; i < item
								.getmSubDetailConditionItemList().size(); i++) {
							buffer.append(item.getmSubDetailConditionItemList()
									.get(i).getProviceId());
							if (i != item.getmSubDetailConditionItemList()
									.size() - 1)
								buffer.append("|");
						}
					}
					break;
				}
			}
		}
		Log.w("wutl", "录取批次＝" + buffer.toString());
		if (TextUtils.isEmpty(buffer.toString())) {
			mLuqupici = "3";
			return mLuqupici;
		}
		mLuqupici = buffer.toString();
		return mLuqupici;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filter_textview:
			startActivityForResult(mShaixuanIntent, 1);
			break;
		case R.id.paixu_pinyin:
			clickPinyinNumbers++;
			if (clickPinyinNumbers > 2)
				clickPinyinNumbers = 0;
			switchPinyinImgState(clickPinyinNumbers);
			break;
		case R.id.paiming_school:
			clickSchoolNumbers++;
			if (clickSchoolNumbers > 2)
				clickSchoolNumbers = 0;
			switchSchoolImgState(clickSchoolNumbers);
			break;
		}
	}

	private void switchPinyinImgState(int number) {
		switch (number) {
		case 0:
			mScoreRankImg.setImageResource(R.drawable.paixu_normal);
			break;
		case 1:
			mScoreRankImg.setImageResource(R.drawable.paixu_up);
			break;
		case 2:
			mScoreRankImg.setImageResource(R.drawable.paixu_down);
			break;
		}
	}

	private void switchSchoolImgState(int number) {
		switch (number) {
		case 0:
			mSchoolRankImg.setImageResource(R.drawable.paixu_normal);
			break;
		case 1:
			mSchoolRankImg.setImageResource(R.drawable.paixu_up);
			break;
		case 2:
			mSchoolRankImg.setImageResource(R.drawable.paixu_down);
			break;
		}
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (TextUtils.isEmpty(s.toString())) {
			postData2Server(conditionItemList, "", false);
		}

	}

	public boolean back() {
		if (mMajorResultListView.getVisibility() == View.VISIBLE) {
			mMajorResultListView.setVisibility(View.GONE);
			mSearchResulListView.setVisibility(View.VISIBLE);
			mZyData.clear();
			return false;
		} else {
			return true;
		}
	}
}