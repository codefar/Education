package com.education;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.education.entity.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-6.
 */
public class CollectionListActivity extends CommonBaseActivity {

    protected LayoutInflater mInflater;
    protected Resources mResources;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions mDisplayImageOptions;

    private static final int TYPE_COLLEGE = 0;
    private static final int TYPE_MAJOR = 1;
    private int mType = TYPE_COLLEGE;
    private ListView mListView;
    private List<Item> mCollegeItemList;
    private List<Item> mMajorItemList = new ArrayList<Item>();
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
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成

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
            holder.descTextView.setText(item.getDesc());
            imageLoader.displayImage(item.getIcon(), holder.iconImageView, mDisplayImageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
            if (mType == TYPE_COLLEGE) {
                CollegeItem collegeItem = (CollegeItem) item;
                holder.titleTextView.setText(collegeItem.getYxmc());
            } else {
                MajorItem majorItem = (MajorItem) item;
                holder.titleTextView.setText(majorItem.getZymc());
            }

            return convertView;
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mType = TYPE_MAJOR;
            displayMajor();
        }
    }

    private void fetchCollege() {
        if (mCollegeItemList == null) {
            mCollegeItemList = new ArrayList<Item>();
            CollegeItem item1 = new CollegeItem();
            item1.setYxdh("123");
            item1.setYxmc("北京大学");
            item1.setIcon("http://www.icosky.com/icon/png/Emoticon/Emoticons/Glad.png");

            CollegeItem item2 = new CollegeItem();
            item2.setYxdh("124");
            item2.setYxmc("天津大学");
            item2.setIcon("http://www.aa25.cn/uploadfile/png/Iconbase/Waterworld/ok.png");

            CollegeItem item3 = new CollegeItem();
            item3.setYxdh("1235");
            item3.setYxmc("复旦大学");
            item3.setIcon("http://img.sc115.com/uploads/png/110125/201101251607119003.png");

            mCollegeItemList.add(item1);
            mCollegeItemList.add(item2);
            mCollegeItemList.add(item3);
        }
    }

    private void fetchMajor() {
        mMajorItemList.clear();
        MajorItem item1 = new MajorItem();
        item1.setZydh("123");
        item1.setZymc("数学");
        item1.setIcon("http://www.icosky.com/icon/png/Emoticon/Emoticons/Glad.png");

        MajorItem item2 = new MajorItem();
        item2.setZydh("124");
        item2.setZymc("物理");
        item2.setIcon("http://img2.imgtn.bdimg.com/it/u=2285030123,2126699377&fm=21&gp=0.jpg");

        MajorItem item3 = new MajorItem();
        item3.setZydh("1235");
        item3.setZymc("化学");
        item3.setIcon("http://img.sc115.com/uploads/png/110125/201101251607119003.png");

        mMajorItemList.add(item1);
        mMajorItemList.add(item2);
        mMajorItemList.add(item3);
    }

    public class Item {
        protected String icon;
        protected String desc;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public class CollegeItem extends Item {
        protected String yxdh; //1001
        protected String yxmc; //北京大学

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
    }

    public class MajorItem extends Item {
        private String zydh;
        private String zymc;

        public String getZydh() {
            return zydh;
        }

        public void setZydh(String zydh) {
            this.zydh = zydh;
        }

        public String getZymc() {
            return zymc;
        }

        public void setZymc(String zymc) {
            this.zymc = zymc;
        }
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descTextView;
        ImageView iconImageView;
        View dividerView;
    }

    @Override
    public void onBackPressed() {
        if (mType == TYPE_COLLEGE) {
            super.onBackPressed();
        } else {
            mType = TYPE_COLLEGE;
            displayCollege();
        }
    }

    private void displayCollege() {
        fetchCollege();
        mItemList = mCollegeItemList;
        mListView.setOnItemClickListener(mItemAdapter);
        mListView.removeHeaderView(mHeaderLayout);
        mItemAdapter.notifyDataSetChanged();
    }

    private void displayMajor() {
        fetchMajor();
        mItemList = mMajorItemList;
        mListView.setOnItemClickListener(null);
        mListView.addHeaderView(mHeaderLayout);
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
        bar.setTitle("志愿收藏");
        bar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected String getTag() {
        return null;
    }
}
