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

import com.education.utils.MenuHelper;

public class SmartRecomentResult3Fragment extends CommonFragment implements OnClickListener{

    private static final String TAG = SmartRecomentResult3Fragment.class.getSimpleName();

	/**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_recomment_result3, container, false);
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