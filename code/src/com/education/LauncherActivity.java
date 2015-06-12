package com.education;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LauncherActivity extends FragmentBaseActivity {

    private static final String TAG = "LauncherActivity";
    private static final int FRAGMENT_COUNT = 3;
    private ViewPager mPager;
    private Resources mResources;
    private FragmentManager mFragmentManager;
    private MainPagerAdapter mMainPagerAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFragmentManager = getFragmentManager();
        mResources = getResources();

        mMainPagerAdapter = new MainPagerAdapter(mFragmentManager);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mMainPagerAdapter);
        mPager.setOffscreenPageLimit(FRAGMENT_COUNT);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LauncherFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }
    }

    public static class LauncherFragment extends CommonFragment {
        private static final String TAG = "LauncherFragment";
        private Activity mActivity;
        private LayoutInflater mInflater;
        private Resources mResources;
        private FragmentManager mFragmentManager;
        private int mPosition;

        public static LauncherFragment newInstance(int position) {
            LauncherFragment fragment = new LauncherFragment();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mActivity = getActivity();
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResources = getResources();
            Bundle b = getArguments();
            mPosition = b.getInt("position", -1);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mFragmentManager = getFragmentManager();
            View mainLayout = inflater.inflate(R.layout.fragment_launcher, container, false);
            ImageView iv = (ImageView) mainLayout.findViewById(R.id.img);

            int id = mResources.getIdentifier("launcher_" + mPosition, "drawable", getActivity().getPackageName());
            iv.setImageBitmap(BitmapFactory.decodeResource(mResources, id));
            if (mPosition == 2) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });
            }
            return mainLayout;
        }

        @Override
        protected String getLogTag() {
            return "LauncherFragment";
        }
    }
}
