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
import android.widget.ListView;
import android.widget.TextView;

import com.education.utils.MenuHelper;

public class SmartRecomentResult1Fragment extends CommonFragment implements OnClickListener{

    private static final String TAG = SmartRecomentResult1Fragment.class.getSimpleName();

	/**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView mNameTextView,mPiciTextView;
	private ListView mResultListView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_recomment_result1, container, false);
        mNameTextView = (TextView) v.findViewById(R.id.name);
		mPiciTextView = (TextView) v.findViewById(R.id.pici);
		mResultListView = (ListView) v.findViewById(R.id.listView);
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
}