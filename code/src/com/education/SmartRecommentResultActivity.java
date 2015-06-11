package com.education;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class SmartRecommentResultActivity extends FragmentBaseActivity implements
		View.OnClickListener {

	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_recomment_result);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(getFragmentManager());
		mViewPager.setAdapter(mTabsAdapter);
	}

	@Override
	public void onClick(View v) {
	}

	public static class TabsAdapter extends FragmentPagerAdapter{

		public TabsAdapter(FragmentManager fm)  {
			super(fm);
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Fragment getItem(int position) {
			if(position == 0){
				return new SmartRecomentResult1Fragment();
			} else if(position == 1){
				return new SmartRecomentResult2Fragment();
			} else if(position == 2){
				return new SmartRecomentResult3Fragment();
			} else {
				return new SmartRecomentResult4Fragment();
			}
		}
	}
}
