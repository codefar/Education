package com.education;

import java.util.ArrayList;
import java.util.List;
import com.education.utils.MenuHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SmartRecomentFragmentStep3 extends CommonFragment implements OnClickListener{

    private static final String TAG = SmartRecomentFragmentStep3.class.getSimpleName();

    private ListView mListView;
    private List mItemList = new ArrayList<Object>();
    protected LayoutInflater mInflater;
    protected Resources mResources;
    private ItemAdapter mAdapter;
    private View mLoading,mResult;
    
	/**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTitleBar();
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = getResources();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_recomment_step3, container, false);
        mListView  = (ListView) v.findViewById(R.id.listView);
        mAdapter = new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mLoading = v.findViewById(R.id.loading);
        mResult = v.findViewById(R.id.result);
        mLoading.setVisibility(View.VISIBLE);
        mResult.setVisibility(View.GONE);
        fetchData();
        return v;
    }
    
	@Override
	protected String getLogTag() {
		return TAG;
	}
	
	protected void setupTitleBar() {
        ActionBar bar = getActivity().getActionBar();
        bar.setTitle(R.string.smart_recomment_smart3);
        setHasOptionsMenu(true);
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_btn) {
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setupTitleBar();
	}
	
	private void fetchData(){
		mResult.postDelayed(new Runnable() {
			@Override
			public void run() {
				mLoading.setVisibility(View.GONE);
		        mResult.setVisibility(View.VISIBLE);
		        Intent intent = new Intent(getActivity(), SmartRecommentResultActivity.class);
		        startActivity(intent);
		        getActivity().finish();
			}
		},1000);
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
                convertView = mInflater.inflate(R.layout.item_smart_setep3, null, false);
                holder = new ViewHolder();
                holder.rankTextView = (TextView) convertView.findViewById(R.id.ranking);
                holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
                holder.typeTextView = (TextView) convertView.findViewById(R.id.type);
                holder.buckTextView = (TextView) convertView.findViewById(R.id.buck);
                holder.itemContainer = (ViewGroup) convertView.findViewById(R.id.item_container);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setTag(holder);

            holder.itemContainer.removeAllViews();
            View item = mInflater.inflate(R.layout.item_smart_setep3_item, null);
            TextView mItemTitle1 = (TextView) item.findViewById(R.id.item_title);
            TextView mItemTitle2 = (TextView) item.findViewById(R.id.item_title2);
            TextView mItemDesc = (TextView) item.findViewById(R.id.item_desc);
            mItemDesc.setOnClickListener(null);
            if(true){
            	mItemDesc.setOnClickListener(null);
            	mItemDesc.setText("已收藏");
            	mItemDesc.setTextColor(getResources().getColor(R.color.third_text));
            } else {
            	mItemDesc.setTextColor(getResources().getColor(R.color.first_text));
            	mItemDesc.setText("收藏");
            	mItemDesc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//收藏
					}
				});
            }
            item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 //下级页面
				}
			});
            holder.itemContainer.addView(item);
            
            return convertView;
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }
	
    private static class ViewHolder {
        TextView rankTextView;
        TextView nameTextView;
        TextView typeTextView;
        TextView buckTextView;
        ViewGroup itemContainer;
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
}