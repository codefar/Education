package com.education;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by su on 15-6-13.
 */
public class MajorDetailActivity extends FragmentBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_major_detail);

		if (savedInstanceState == null) {
			MajorDetailFragment fragmentStep1 = MajorDetailFragment
					.createInstance(getIntent().getExtras());
			Log.i("TAG", getIntent().getExtras().toString());
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragmentStep1).commit();
		}

	}
}
