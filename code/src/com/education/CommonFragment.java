package com.education;

import android.app.Fragment;

/**
 * Created by su on 2014/8/22.
 */
public abstract class CommonFragment extends Fragment {

    protected abstract String getLogTag();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
