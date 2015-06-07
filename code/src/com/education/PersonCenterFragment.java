package com.education;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonCenterFragment extends CommonFragment {
	
	private static final String TAG = PersonCenterFragment.class.getSimpleName();
    private LayoutInflater mInflater;
    private ListView mListView;
    private Resources mResources;
    private List<Item> mItemList = new ArrayList<Item>();
    private ItemAdapter mItemAdapter;


    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_center, container, false);

        mInflater = inflater;
        mListView = (ListView) v.findViewById(R.id.list);
        mItemAdapter = new ItemAdapter();
        mListView.setAdapter(mItemAdapter);
        mListView.setOnItemClickListener(mItemAdapter);
        makePreferenceList();
        return v;
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
                convertView = mInflater.inflate(R.layout.item_center, null, false);
                holder = new ViewHolder();
                holder.dividerView = convertView.findViewById(R.id.divider);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.item_title);
                holder.descTextView = (TextView) convertView.findViewById(R.id.desc);
                holder.iconImageView = (ImageView) convertView.findViewById(R.id.icon);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
                convertView.setLayoutParams(lp);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setTag(holder);

            Item item = mItemList.get(position);
            holder.descTextView.setText(item.desc);
            holder.titleTextView.setText(item.title);
            holder.iconImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, item.icon));

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {

            } else if (position == 1) {

            } else if (position == 2) {

            } else if (position == 3) {

            }
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }
    }

    public class Item {
        int icon;
        String title;
        String desc;
    }

    private void makePreferenceList() {
        mItemList.clear();
        Item item1 = new Item();
        item1.title = "昵称";
        item1.icon = R.drawable.nickname;
        mItemList.add(item1);

        Item item2 = new Item();
        item2.title = "修改密码";
        item2.icon = R.drawable.change_password;
        mItemList.add(item2);

        Item item3 = new Item();
        item3.title = "个人信息";
        item3.icon = R.drawable.profile;
        mItemList.add(item3);

        Item item4 = new Item();
        item4.title = "关于";
        item4.icon = R.drawable.about;
        mItemList.add(item4);

        mItemAdapter.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descTextView;
        ImageView iconImageView;
        View dividerView;
    }

    @Override
	protected String getLogTag() {
		return TAG;
	}
}