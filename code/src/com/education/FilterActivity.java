package com.education;

import android.app.ActionBar;
import android.content.Context;
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
import com.education.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-6.
 */
public class FilterActivity  extends CommonBaseActivity {

    private static final String TAG = "FilterActivity";

    protected LayoutInflater mInflater;
    protected Resources mResources;

    private ListView mListView;
    private List<Item> mItemList = new ArrayList<Item>();
    private ItemAdapter mItemAdapter;
    private RelativeLayout mHeaderLayout;
    private View mHeaderTitleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        setupTitleBar();

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = getResources();

        mListView = (ListView) findViewById(R.id.list);
        mItemAdapter = new ItemAdapter();
        mListView.setAdapter(mItemAdapter);

        mHeaderLayout = (RelativeLayout) mInflater.inflate(R.layout.header_collection_list, null);
        mHeaderTitleTextView = mHeaderLayout.findViewById(R.id.header_title);
        displayCollege();
    }

    private class ItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
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
                convertView = mInflater.inflate(R.layout.item_college, null, false);
                holder = new ViewHolder();
                holder.dividerView = convertView.findViewById(R.id.divider);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.item_title);
                holder.descTextView = (TextView) convertView.findViewById(R.id.desc);
                holder.iconImageView = (ImageView) convertView.findViewById(R.id.icon);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
                convertView.setLayoutParams(lp);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setTag(holder);

            Item item = mItemList.get(position);
            holder.iconImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, item.getIcon()));
            holder.descTextView.setText(item.getDesc());
            holder.titleTextView.setText(item.getTitle());

            return convertView;
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {

            } else if (position == 1) {

            } else if (position == 2) {

            } else if (position == 3) {

            } else if (position == 4) {

            }
        }
    }

    private void fetchCollege() {
        Item item1 = new Item();
        item1.setIcon(R.drawable.sx_shengfen);
        item1.setTitle("院校省份");
        item1.setDesc("全部");
        mItemList.add(item1);

        Item item2 = new Item();
        item2.setIcon(R.drawable.xuexiao_4);
        item2.setTitle("院校类型");
        item2.setDesc("全部");
        mItemList.add(item2);

        Item item3 = new Item();
        item3.setIcon(R.drawable.sx_zhuanye);
        item3.setTitle("专业");
        item3.setDesc("全部");
        mItemList.add(item3);

        Item item4 = new Item();
        item4.setIcon(R.drawable.sx_pici);
        item4.setTitle("录取批次");
        item4.setDesc("全部");
        mItemList.add(item4);

        Item item5 = new Item();
        item5.setIcon(R.drawable.sx_fenshu);
        item5.setTitle("历史录取分数");
        item5.setDesc("全部");
        mItemList.add(item5);
    }

    public class Item {
        protected int icon;
        protected String desc;
        protected String title;

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descTextView;
        ImageView iconImageView;
        View dividerView;
    }

    private void displayCollege() {
        fetchCollege();
        mListView.setOnItemClickListener(mItemAdapter);
        mListView.removeHeaderView(mHeaderLayout);
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void unLoginForward(User user) {

    }

    @Override
    protected void forceUpdateForward() {

    }

    @Override
    protected void setupTitleBar() {
        ActionBar bar = getActionBar();
        bar.setTitle("筛选");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}