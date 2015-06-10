package com.education;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.entity.ShaiXuanInfo;
import com.education.entity.User;

public class ShaiXuanActivity extends CommonBaseActivity implements
		View.OnClickListener {

	public static final String SHAIXUAN_CLICK_POSITION = "shuaixuan_click_position";
	public static final String SHAIXUAN_RESULT_TAG = "shuaixuan_result_tag";

	private ListView mConditionListView;
	private List<ConditionItem> mItemList = new ArrayList<ConditionItem>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mItemAdapter;
	private Intent mDetailConditionItemIntent, mShaixuanIntent;
	private Button mConfirmBt;
	private ShaiXuanInfo mShaiXuanInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shaixuan);
		setupTitleBar();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		mDetailConditionItemIntent = new Intent(this,
				DetailConditionActivity.class);
		mShaixuanIntent = new Intent();
		mConditionListView = (ListView) findViewById(R.id.shaixuan_condition_list);
		mConfirmBt = (Button) findViewById(R.id.shaixuan_confirm_bt);
		mItemAdapter = new ItemAdapter();
		mShaiXuanInfo = new ShaiXuanInfo();
		mConditionListView.setAdapter(mItemAdapter);
		mConfirmBt.setOnClickListener(this);
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
		mConditionListView.setOnItemClickListener(mItemAdapter);
		mItemAdapter.notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView conditionNameTextView;
		TextView detailConditionTextView;
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
				holder.conditionNameTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.detailConditionTextView = (TextView) convertView
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

			ConditionItem ConditionItem = mItemList.get(position);
			holder.schoolImg.setImageBitmap(BitmapFactory.decodeResource(
					mResources, ConditionItem.getSchoolImg()));
			holder.schoolImg.setVisibility(View.GONE);
			holder.conditionNameTextView.setText(ConditionItem
					.getConditionName());
			holder.detailConditionTextView.setText(ConditionItem
					.getDetailCondition());
			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mDetailConditionItemIntent.putExtra(SHAIXUAN_CLICK_POSITION,
					position);
			startActivityForResult(mDetailConditionItemIntent, 0);
		}
	}

	private void fetchCollege() {
		ConditionItem item1 = new ConditionItem();
		item1.setSchoolImg(R.drawable.xuexiao_1);
		item1.setConditionName("院校省份");
		item1.setDetailCondition("全部");
		mItemList.add(item1);

		ConditionItem item2 = new ConditionItem();
		item2.setSchoolImg(R.drawable.xuexiao_2);
		item2.setConditionName("院校类型");
		item2.setDetailCondition("全部");
		mItemList.add(item2);

		ConditionItem item3 = new ConditionItem();
		item3.setSchoolImg(R.drawable.xuexiao_3);
		item3.setConditionName("专业");
		item3.setDetailCondition("全部");
		mItemList.add(item3);

		ConditionItem item4 = new ConditionItem();
		item4.setSchoolImg(R.drawable.xuexiao_3);
		item4.setConditionName("录取批次");
		item4.setDetailCondition("全部");
		mItemList.add(item4);

		ConditionItem item5 = new ConditionItem();
		item5.setSchoolImg(R.drawable.xuexiao_3);
		item5.setConditionName("历年录取分数");
		item5.setDetailCondition("全部");
		mItemList.add(item5);
	}

	private class ConditionItem {
		protected int schoolImg;
		protected String conditionName;
		protected String detailCondition;

		public void setConditionName(String conditionName) {
			this.conditionName = conditionName;
		}

		public String getConditionName() {
			return conditionName;
		}

		public void setDetailCondition(String detailCondition) {
			this.detailCondition = detailCondition;
		}

		public String getDetailCondition() {
			return detailCondition;
		}

		public void setSchoolImg(int schoolImg) {
			this.schoolImg = schoolImg;
		}

		public int getSchoolImg() {
			return schoolImg;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0
				&& resultCode == DetailConditionConstants.CONDITION_ITEM_SELECTE_CONFIRM_RESULE_CODE) {

			Toast.makeText(
					this,
					"选中个数"
							+ data.getStringArrayExtra(DetailConditionConstants.SELECTED_ITEM_TAG).length,
					Toast.LENGTH_SHORT).show();
			int position = data.getIntExtra(SHAIXUAN_CLICK_POSITION, 0);

			// 更新条件视图
			View view = mConditionListView.getChildAt(position);

			mItemList
					.get(position)
					.setDetailCondition(
							getSelectedItemString(data
									.getStringArrayExtra(DetailConditionConstants.SELECTED_ITEM_TAG)));
			mItemAdapter.notifyDataSetChanged();

			// 设置需要回传给FilterSchoolAndMajorActivity的数据封装
			wrapperConditionData(
					position,
					data.getStringArrayExtra(DetailConditionConstants.SELECTED_ITEM_TAG));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void wrapperConditionData(int position, String conditionArray[]) {
		switch (position) {
		case DetailConditionConstants.PROVINCE:
			mShaiXuanInfo.setProvinceList(Arrays.asList(conditionArray));
			break;
		case DetailConditionConstants.SCHOOL_TYPE:
			mShaiXuanInfo.setProvinceList(Arrays.asList(conditionArray));
			break;
		case DetailConditionConstants.MAJOR:
			mShaiXuanInfo.setMajorList(Arrays.asList(conditionArray));
			break;
		case DetailConditionConstants.LUQU_PICI:
			mShaiXuanInfo.setLuquPiciList(Arrays.asList(conditionArray));
			break;
		}
	}

	private String getSelectedItemString(String[] selectedStr) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < selectedStr.length; i++) {
			buffer.append(selectedStr[i]);
			if (i != selectedStr.length - 1)
				buffer.append("/");
		}
		return buffer.toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shaixuan_confirm_bt:
			mShaixuanIntent.putExtra(SHAIXUAN_RESULT_TAG, mShaiXuanInfo);
			setResult(1, mShaixuanIntent);
			finish();
			break;
		}
	}
}
