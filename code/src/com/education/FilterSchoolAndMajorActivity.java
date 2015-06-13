package com.education;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.education.entity.ShaiXuanConditionItem;
import com.education.entity.ShaiXuanInfo;
import com.education.entity.ShaiXuanJieGuo;
import com.education.entity.User;
import com.education.utils.LogUtil;

public class FilterSchoolAndMajorActivity extends CommonBaseActivity implements
		View.OnClickListener {

	private ListView mSearchResulListView;
	private List<SchoolItem> mItemList = new ArrayList<SchoolItem>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mItemAdapter;
	private TextView mFilterTextView, mScoreRankText, mSchoolRankText,
			mUserScoreText, mUserRankText, mUserTypeText, mUserLocation,
			mSchoolNumbersText, mMajorNumbersText;
	private Intent mShaixuanIntent;
	private ImageView mScoreRankImg, mSchoolRankImg;
	private int clickPinyinNumbers, clickSchoolNumbers;
	private User mUser;
	private String mSchoolNumbers, mLuquQingkuang;
	private int mMajorNumbers;
	private ArrayList<ShaiXuanConditionItem> conditionItemList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_school_and_major);
		setupTitleBar();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		initView();

		mItemAdapter = new ItemAdapter();
		mShaixuanIntent = new Intent(this, ShaiXuanActivity.class);
		mSearchResulListView.setAdapter(mItemAdapter);
		displayCollege();
		mFilterTextView.setOnClickListener(this);
		mScoreRankText.setOnClickListener(this);
		mSchoolRankText.setOnClickListener(this);
		shaiXuan();
	}

	private void initView() {
		mSearchResulListView = (ListView) findViewById(R.id.filter_school_result_list);
		mFilterTextView = (TextView) findViewById(R.id.filter_textview);
		mScoreRankText = (TextView) findViewById(R.id.paixu_pinyin);
		mSchoolRankText = (TextView) findViewById(R.id.paiming_school);
		mScoreRankImg = (ImageView) findViewById(R.id.paixu_pinyin_img);
		mSchoolRankImg = (ImageView) findViewById(R.id.paiming_school_img);
		mUserScoreText = (TextView) findViewById(R.id.user_score);
		mUserRankText = (TextView) findViewById(R.id.user_rank);
		mUserTypeText = (TextView) findViewById(R.id.user_type);
		mUserLocation = (TextView) findViewById(R.id.user_location);
		mSchoolNumbersText = (TextView) findViewById(R.id.school_numbers);
		mMajorNumbersText = (TextView) findViewById(R.id.major_numbers);

		mUser = User.getInstance();
		if (mUser != null) {
			Log.w("wutl", mUser.toString());
			mUserScoreText.setText(mUser.getKscj() + "");
			mUserRankText.setText(mUser.getKspw() + "");
			mUserTypeText.setText(mUser.getKskl() + "");
			mUserLocation.setText(mUser.getKskqName() + "");
		}
	}

	private void shaiXuan() {
		final FastJsonRequest request = new FastJsonRequest(Request.Method.GET,
				Url.SHAI_XUAN, null, new VolleyResponseListener(this) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						if (success) {
							String datas = response.getString("datas");
							ShaiXuanJieGuo result = JSON.parseObject(datas,
									ShaiXuanJieGuo.class);
							if (result != null)
								setDataSource(result);
							Log.d("wutl", "resutl=" + result.toString());
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(FilterSchoolAndMajorActivity.this,
									errorData.getText(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, "wutl");
						Toast.makeText(
								FilterSchoolAndMajorActivity.this,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("pageno", String.valueOf(1));
				map.put("skey", "清华");
				map.put("yxss", "1|2|3");
				map.put("yxlx", "");
				map.put("yxxz", "1|2");
				map.put("lqpc", "3");
				map.put("lqqk", "2013|1|688|720");
				map.put("kskl", String.valueOf(1/* 文史 */));
				map.put("kqdh", String.valueOf(2/* 上海 */));

				return AppHelper.makeSimpleData("search", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private void setDataSource(ShaiXuanJieGuo result) {
		mSchoolNumbers = String.valueOf(result.getYxzydata().size());
		List<CollegeItem> schoolList = result.getYxzydata();
		mItemList.clear();
		for (int i = 0; i < schoolList.size(); i++) {
			SchoolItem localItem = new SchoolItem(schoolList.get(i).getZysl(),
					schoolList.get(i).getYxmc());
			mMajorNumbers += Integer.valueOf(schoolList.get(i).getZysl())
					.intValue();
			setSchoolImg(localItem, i % 3);
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

	@Override
	protected void unLoginForward(User user) {

	}

	@Override
	protected void forceUpdateForward() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filter_major, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void setupTitleBar() {
		ActionBar bar = getActionBar();
		bar.setTitle("返回");
		bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE);
		bar.setHomeButtonEnabled(true);
	}

	@Override
	protected String getTag() {
		return null;
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
			if (position == 0) {

			} else if (position == 1) {

			} else if (position == 2) {

			} else if (position == 3) {

			}
		}
	}

	private class SchoolItem {
		protected int schoolImg;
		protected String marjorNumber;
		protected String schoolName;

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取筛选条件
		if (requestCode == 1 && resultCode == 1) {

			Bundle bundle = data
					.getBundleExtra(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			// ArrayList<ShaiXuanConditionItem> conditionItemList =
			// bundle.getParcelableArrayList(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			conditionItemList = (ArrayList<ShaiXuanConditionItem>) bundle
					.getSerializable(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
			postData2Server(conditionItemList);
			mLuquQingkuang = getLuquQingkuang(data);
			Log.w("wutl", "录取情况＝" + mLuquQingkuang);
			Toast.makeText(this, conditionItemList.toString(),
					Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getLuquQingkuang(Intent data) {
		return data.getStringExtra("year_score") + "|"
				+ data.getIntExtra("score_type", 1) + "|"
				+ data.getStringExtra("low_score") + "|"
				+ data.getStringExtra("high_score");
	}

	private void postData2Server(
			final ArrayList<ShaiXuanConditionItem> conditionlist) {
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHAI_XUAN, null,
				new VolleyResponseListener(this) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						if (success) {
							String datas = response.getString("datas");
							ShaiXuanJieGuo result = JSON.parseObject(datas,
									ShaiXuanJieGuo.class);
							if (result != null)
								setDataSource(result);
							Log.d("wutl", "resutl=" + result.toString());
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(FilterSchoolAndMajorActivity.this,
									errorData.getText(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						LogUtil.logNetworkResponse(volleyError, "wutl");
						Toast.makeText(
								FilterSchoolAndMajorActivity.this,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();

				map.put("pageno", String.valueOf(1));
				// map.put("skey", "清华");
				map.put("yxss", getYxss(conditionlist));
				map.put("yxlx", getYxlx(conditionlist));
				map.put("yxxz", getYxxz(conditionlist));
				map.put("lqpc", getLuqupici(conditionlist));
				map.put("lqqk", mLuquQingkuang);
				map.put("kskl", String.valueOf(mUser.getKskl()));
				map.put("kqdh", String.valueOf(mUser.getKqdh()));

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
		return buffer.toString();
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
}
