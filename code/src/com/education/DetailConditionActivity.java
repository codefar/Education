package com.education;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.education.entity.ConditionItem;

public class DetailConditionActivity extends Activity implements
		View.OnClickListener {

	private int mCurrentCondition = 0;

	private List<ConditionItem> mItemList = new ArrayList<ConditionItem>();
	private List<ConditionItem> mSelectedItmeList = new ArrayList<ConditionItem>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ListView mConditionListView;
	private Button mConfirmBt;
	private ItemAdapter mItemAdapter;
	private Intent mSelectedItemIntent;
	private int mCurrentSelectShaixuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_condition);

		 setTheme(android.R.style.Theme_Dialog);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		mCurrentSelectShaixuan = getIntent().getIntExtra(
				ShaiXuanActivity.SHAIXUAN_CLICK_POSITION, 0);
		mCurrentCondition = getIntent().getIntExtra(
				ShaiXuanActivity.SHAIXUAN_CLICK_POSITION, 0);
		mConditionListView = (ListView) findViewById(R.id.detail_condition_lists);
		mConfirmBt = (Button) findViewById(R.id.detail_condition_confirm_bt);
		mItemAdapter = new ItemAdapter();
		mSelectedItemIntent = new Intent();
		mConditionListView.setAdapter(mItemAdapter);
		mConfirmBt.setOnClickListener(this);
		displayCollege();
	}

	private void displayCollege() {
		fetchCollege();
		mConditionListView.setOnItemClickListener(mItemAdapter);
		mItemAdapter.notifyDataSetChanged();
	}

	private void fetchCollege() {
		switch (mCurrentCondition) {
		// 省
		case DetailConditionConstants.PROVINCE:
			mItemList = generateItem(
					getResources().getStringArray(R.array.province_name),
					getResources().getStringArray(R.array.province_id));
			break;
		// 类型
		case DetailConditionConstants.SCHOOL_TYPE:
			mItemList = generateItem(
					getResources().getStringArray(R.array.college_type_name),
					getResources().getStringArray(R.array.college_type_id));
			break;
		// 院校性质
		case DetailConditionConstants.MAJOR:
			mItemList = generateItem(
					getResources().getStringArray(R.array.college_property_name),
					getResources().getStringArray(R.array.college_property_id));
			break;
		// 录取批次
		case DetailConditionConstants.LUQU_PICI:
			mItemList = generateItem(
					getResources().getStringArray(R.array.luqu_pici_name),
					getResources().getStringArray(R.array.luqu_pici_id));
			break;
		// 历年分数
		case DetailConditionConstants.LUQU_SCORE:
		//	mItemList = Arrays.asList(DetailConditionConstants.provinces);
			break;
		}
	}

	private List<ConditionItem> generateItem(String provicename[],String provinceId[]){
		List<ConditionItem> itemList=new ArrayList<ConditionItem>();
		for(int i=0;i<provicename.length;i++){
			itemList.add(new ConditionItem(provicename[i],provinceId[i]));
		}
		return itemList;
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
				convertView = mInflater.inflate(R.layout.detail_condition_item,
						null, false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.detialConditionItemName = (TextView) convertView
						.findViewById(R.id.detail_condition_item_title);
				holder.conditionItemSelectedImg = (ImageView) convertView
						.findViewById(R.id.detail_condition_item_selected_img);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			ConditionItem ConditionItem = mItemList.get(position);

			holder.detialConditionItemName.setText(ConditionItem
					.getDetailConditionName());

			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			View selectedImgView = view
					.findViewById(R.id.detail_condition_item_selected_img);
			if (selectedImgView.getVisibility() == View.VISIBLE) {
				selectedImgView.setVisibility(View.GONE);
				if (mSelectedItmeList.contains(mItemList.get(position)))
					mSelectedItmeList.remove(mItemList.get(position));
			} else {
				selectedImgView.setVisibility(View.VISIBLE);
				if (!mSelectedItmeList.contains(mItemList.get(position)))
					mSelectedItmeList.add(mItemList.get(position));
			}
		}
	}

	private static class ViewHolder {
		TextView detialConditionItemName;
		ImageView conditionItemSelectedImg;
		View dividerView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.detail_condition_confirm_bt) {
			mSelectedItemIntent.putExtra(
					DetailConditionConstants.SELECTED_ITEM_TAG,
					getSelectItemText(mSelectedItmeList));
			mSelectedItemIntent.putExtra(
					ShaiXuanActivity.SHAIXUAN_CLICK_POSITION,
					mCurrentSelectShaixuan);
			setResult(
					DetailConditionConstants.CONDITION_ITEM_SELECTE_CONFIRM_RESULE_CODE,
					mSelectedItemIntent);
			finish();
		}
	}

	private String[] getSelectItemText(List<ConditionItem> list) {
		if (list == null)
			return null;
		String result[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			result[i] = list.get(i).getDetailConditionName();
		return result;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
