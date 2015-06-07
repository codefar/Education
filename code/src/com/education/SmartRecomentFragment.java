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

public class SmartRecomentFragment extends CommonFragment implements OnClickListener{

    private static final String TAG = SmartRecomentFragment.class.getSimpleName();

    private Button mStartBtn;
	/**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTitleBar();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_smart, container, false);
        mStartBtn  = (Button) v.findViewById(R.id.start_btn);
        mStartBtn.setOnClickListener(this);
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
        setHasOptionsMenu(true);
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_btn) {
			Intent intent = new Intent(getActivity(),SmartRecommentActivity.class);
			startActivity(intent);
		}
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