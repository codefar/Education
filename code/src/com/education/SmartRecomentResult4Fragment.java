package com.education;

import com.education.utils.MenuHelper;

import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SmartRecomentResult4Fragment extends CommonFragment implements OnClickListener{

    private static final String TAG = SmartRecomentResult4Fragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smart_recomment_result4, container, false);
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