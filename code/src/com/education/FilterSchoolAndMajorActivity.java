package com.education;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.education.entity.User;

public class FilterSchoolAndMajorActivity extends CommonBaseActivity {

	private ListView mSearchResulListView;
	private List<SchoolItem> mItemList = new ArrayList<SchoolItem>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_school_and_major);
		setupTitleBar();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		mSearchResulListView = (ListView) findViewById(R.id.filter_school_result_list);
		mItemAdapter = new ItemAdapter();
		mSearchResulListView.setAdapter(mItemAdapter);
		displayCollege();

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
		fetchCollege();
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

	private void fetchCollege() {
		SchoolItem item1 = new SchoolItem();
		item1.setSchoolImg(R.drawable.xuexiao_1);
		item1.setSchoolName("北京大学");
		item1.setMarjorNumber("5");
		mItemList.add(item1);

		SchoolItem item2 = new SchoolItem();
		item2.setSchoolImg(R.drawable.xuexiao_2);
		item2.setSchoolName("清华大学");
		item2.setMarjorNumber("4");
		mItemList.add(item2);

		SchoolItem item3 = new SchoolItem();
		item3.setSchoolImg(R.drawable.xuexiao_3);
		item3.setSchoolName("中国人民大学");
		item3.setMarjorNumber("6");
		mItemList.add(item3);
	}

	private class SchoolItem {
		protected int schoolImg;
		protected String marjorNumber;
		protected String schoolName;

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
}
