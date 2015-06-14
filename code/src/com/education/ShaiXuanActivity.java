package com.education;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.education.entity.ConditionItem;
import com.education.entity.ShaiXuanConditionItem;
import com.education.entity.User;

public class ShaiXuanActivity extends CommonBaseActivity implements
		View.OnClickListener {

    private static final String TAG = "ShaiXuanActivity";

	public static final String SHAIXUAN_CLICK_POSITION = "shuaixuan_click_position";
	public static final String SHAIXUAN_RESULT_TAG = "shuaixuan_result_tag";

	private ListView mConditionListView;
	private List<ShaiXuanConditionItem> mItemList = new ArrayList<ShaiXuanConditionItem>();
	protected LayoutInflater mInflater;
	protected Resources mResources;
	private ItemAdapter mItemAdapter;
	private Intent mDetailConditionItemIntent, mShaixuanIntent;
	private Button mConfirmBt;
	private boolean isReset = false;
	private String[] mLuQuPici;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shaixuan);
		setupTitleBar();

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResources = getResources();
		mLuQuPici=mResources.getStringArray(R.array.luqu_pici_name);
		mDetailConditionItemIntent = new Intent(this,
				DetailConditionActivity.class);




		mShaixuanIntent = new Intent();
        Intent intent = getIntent();
//        List<ShaiXuanConditionItem> tempList = (ArrayList<ShaiXuanConditionItem>) intent.getSerializableExtra(ShaiXuanActivity.SHAIXUAN_RESULT_TAG);
//        if (tempList != null) {
//            mItemList = tempList;
//        }

        String luQuQingKuang = intent.getStringExtra("LU_QU_QING_KUANG");
        if (!TextUtils.isEmpty(luQuQingKuang)) {
            String[] qingKuangs = luQuQingKuang.split("\\|");
            mShaixuanIntent.putExtra("year_score", qingKuangs[0]);
            mShaixuanIntent.putExtra("score_type", Integer.parseInt(qingKuangs[1]));
            mShaixuanIntent.putExtra("low_score", qingKuangs[2]);
            mShaixuanIntent.putExtra("high_score", qingKuangs[3]);

//            intent.putExtra("low_score", mShaixuanIntent.getStringExtra("low_score"));
//            intent.putExtra("high_score", mShaixuanIntent.getStringExtra("high_score"));
//            intent.putExtra("year_score", mShaixuanIntent.getStringExtra("year_score"));
//            intent.putExtra("score_type", mShaixuanIntent.getIntExtra("score_type", 1));
        }


		mConditionListView = (ListView) findViewById(R.id.shaixuan_condition_list);
		mConfirmBt = (Button) findViewById(R.id.shaixuan_confirm_bt);
		mItemAdapter = new ItemAdapter();
		mConditionListView.setAdapter(mItemAdapter);
		mConfirmBt.setOnClickListener(this);
		displayCollege();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		case R.id.reset_filter_condition:
			Toast.makeText(this, "click ", Toast.LENGTH_SHORT).show();
			isReset = true;
			mItemAdapter.notifyDataSetChanged();
			
			int itemCount=mConditionListView.getAdapter().getCount();
			for(int i=0;i<itemCount;i++){
				Object obj=mConditionListView.getAdapter().getItem(i);
				((ShaiXuanConditionItem)obj).setmSubDetailConditionItemList(null);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	protected void unLoginForward(User user) {

	}

	@Override
	protected void forceUpdateForward() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.reset, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void setupTitleBar() {
		ActionBar bar = getActionBar();
		bar.setTitle("筛选");
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

			ShaiXuanConditionItem ConditionItem = mItemList.get(position);
			holder.schoolImg.setImageBitmap(BitmapFactory.decodeResource(
					mResources, ConditionItem.getSchoolImg()));
			holder.conditionNameTextView.setText(ConditionItem
					.getConditionName());
			if (isReset) {
				if (position == getCount() - 1)
					holder.detailConditionTextView.setText("默认值");
				else
					holder.detailConditionTextView.setText("全部");
			} else {
				if (TextUtils.isEmpty(ConditionItem.getDetailCondition()))
					holder.detailConditionTextView.setText("全部");
				else
					holder.detailConditionTextView.setText(ConditionItem
							.getDetailCondition());
			}

			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == mItemAdapter.getCount() - 1) {
//                mShaixuanIntent.putExtra("low_score",
//                        data.getStringExtra("low_score"));
//                mShaixuanIntent.putExtra("high_score",
//                        data.getStringExtra("high_score"));
//                mShaixuanIntent.putExtra("year_score",
//                        data.getStringExtra("year_score"));
//                mShaixuanIntent.putExtra("score_type",
//                        data.getIntExtra("score_type", 1));
                Intent intent = new Intent(ShaiXuanActivity.this, LuQuScore.class);
                intent.putExtra("low_score", mShaixuanIntent.getStringExtra("low_score"));
                intent.putExtra("high_score", mShaixuanIntent.getStringExtra("high_score"));
                intent.putExtra("year_score", mShaixuanIntent.getStringExtra("year_score"));
                intent.putExtra("score_type", mShaixuanIntent.getIntExtra("score_type", 1));
                Log.v(TAG, "low_score: " + mShaixuanIntent.getStringExtra("low_score"));
                Log.v(TAG, "high_score: " + mShaixuanIntent.getStringExtra("high_score"));
                Log.v(TAG, "year_score: " + mShaixuanIntent.getStringExtra("year_score"));
                Log.v(TAG, "score_type: " + mShaixuanIntent.getStringExtra("score_type"));
                startActivityForResult(intent, 2);
            }
			else if(position==3){
				simpleDialog(position);
			}else {
				mDetailConditionItemIntent.putExtra(SHAIXUAN_CLICK_POSITION,
						position);

				startActivityForResult(mDetailConditionItemIntent, 0);
			}
		}
	}

	private void fetchCollege() {
		ShaiXuanConditionItem item1 = new ShaiXuanConditionItem();
		item1.setSchoolImg(R.drawable.sx_shengfen);
		item1.setConditionName("院校省份");
		item1.setDetailCondition("全部");
		mItemList.add(item1);

		ShaiXuanConditionItem item2 = new ShaiXuanConditionItem();
		item2.setSchoolImg(R.drawable.xuexiao_4);
		item2.setConditionName("院校类型");
		item2.setDetailCondition("全部");
		mItemList.add(item2);

		ShaiXuanConditionItem item3 = new ShaiXuanConditionItem();
		item3.setSchoolImg(R.drawable.sx_zhuanye);
		item3.setConditionName("院校性质");
		item3.setDetailCondition("全部");
		mItemList.add(item3);

		ShaiXuanConditionItem item4 = new ShaiXuanConditionItem();
		item4.setSchoolImg(R.drawable.sx_pici);
		item4.setConditionName("录取批次");
		item4.setDetailCondition("全部");
		mItemList.add(item4);

		ShaiXuanConditionItem item5 = new ShaiXuanConditionItem();
		item5.setSchoolImg(R.drawable.sx_fenshu);
		item5.setConditionName("历年录取情况");
		item5.setDetailCondition("默认值");
		mItemList.add(item5);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data==null)
			return;
		if (requestCode == 0
				&& resultCode == DetailConditionConstants.CONDITION_ITEM_SELECTE_CONFIRM_RESULE_CODE) {
			isReset = false;

			int position = data.getIntExtra(SHAIXUAN_CLICK_POSITION, 0);

			// 更新条件视图
			View view = mConditionListView.getChildAt(position);

			ArrayList<ConditionItem> detailConditionItemList = (ArrayList<ConditionItem>) data
					.getSerializableExtra(DetailConditionConstants.SELECTED_ITEM_TAG);
			// .getParcelableArrayListExtra(DetailConditionConstants.SELECTED_ITEM_TAG);

			mItemList.get(position).setDetailCondition(
					getSelectedItemString(detailConditionItemList));
			mItemList.get(position).setmSubDetailConditionItemList(
					detailConditionItemList);
			mItemAdapter.notifyDataSetChanged();
		} else if (requestCode == 2 || resultCode == 3) {
			mShaixuanIntent.putExtra("low_score",
					data.getStringExtra("low_score"));
			mShaixuanIntent.putExtra("high_score",
					data.getStringExtra("high_score"));
			mShaixuanIntent.putExtra("year_score",
					data.getStringExtra("year_score"));
			mShaixuanIntent.putExtra("score_type",
					data.getIntExtra("score_type", 1));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getSelectedItemString(
			ArrayList<ConditionItem> detailConditionItemList) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < detailConditionItemList.size(); i++) {
			buffer.append(((com.education.entity.ConditionItem) detailConditionItemList
					.get(i)).getDetailConditionName());
			if (i != detailConditionItemList.size() - 1)
				buffer.append("/");
		}
		return buffer.toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shaixuan_confirm_bt:
			Bundle bundle = new Bundle();
			bundle.putSerializable(SHAIXUAN_RESULT_TAG,
					(Serializable) mItemList);
			// bundle.putParcelableArrayList(SHAIXUAN_RESULT_TAG,
			// (ArrayList<? extends Parcelable>) mItemList);
			mShaixuanIntent.putExtra(SHAIXUAN_RESULT_TAG, bundle);
			setResult(1, mShaixuanIntent);
			finish();
			break;
		}
	}
	
	/**
	 * 单选Dialogs
	 */
	private void simpleDialog(final int position) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("录取批次");
		builder.setSingleChoiceItems(mLuQuPici, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						View view=mConditionListView.getChildAt(position);
					    TextView text=(TextView) view.findViewById(R.id.desc);
					    text.setText(mLuQuPici[which]);
					    mItemList.get(3).setmSubDetailConditionItemList(getSubPiciItem(which));
					}
				});
		dialog = builder.create();
		dialog.show();
	}
	
	private ArrayList<ConditionItem>  getSubPiciItem(int position){
		ArrayList<ConditionItem> detailConditionItemList=new ArrayList<ConditionItem>();
		ConditionItem item=new ConditionItem("",position+1);
		detailConditionItemList.add(item);
		
		return detailConditionItemList;
	}
}
