package com.education;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.education.TestActivity.Item6;
import com.education.entity.XingGe;
import com.education.utils.MenuHelper;

public class SmartRecomentResult1Fragment extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentResult1Fragment.class
			.getSimpleName();

	private ListView mResultListView;
	protected LayoutInflater mInflater;
	Item6 mItem;
	ItemAdapter mAdpter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mItem = ((SmartRecommentResultActivity) getActivity()).getItem();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_result1,
				container, false);
		mInflater = inflater;
		mResultListView = (ListView) v.findViewById(R.id.listView);
		mAdpter = new ItemAdapter();
		mResultListView.setAdapter(mAdpter);
		
		return v;
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

	protected void setupTitleBar() {
		ActionBar bar = getActivity().getActionBar();
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setTitle(R.string.smart_recomment);
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
			return mItem.getXgfx().size();
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_character_analysis, null, false);
				holder = new ViewHolder();
				holder.title2TextView = (TextView) convertView
						.findViewById(R.id.item_title2);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.descTextView = (TextView) convertView
						.findViewById(R.id.item_desc);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			XingGe item = mItem.getXgfx().get(position);
			holder.titleTextView.setText(item.getXm());
			holder.title2TextView.setText(String.valueOf(item.getScore()));
			holder.descTextView.setText(item.getPj());
			return convertView;
		}

		public Object getItem(int position) {
			return mItem.getXgfx().get(position);
		}
	}

	private static class ViewHolder {
		TextView titleTextView;
		TextView title2TextView;
		TextView descTextView;
	}
}