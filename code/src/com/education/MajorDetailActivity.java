package com.education;

import android.os.Bundle;

/**
 * Created by su on 15-6-13.
 */
public class MajorDetailActivity extends FragmentBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_detail);

        if (savedInstanceState == null) {
            SmartRecomentResult2Fragment fragmentStep1 = new SmartRecomentResult2Fragment();
            Bundle args = new Bundle();
            args.putBoolean(SmartRecomentFragmentStep1.GO_SMART_RECOMNET_STEP2, true);
            fragmentStep1.setArguments(args);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragmentStep1).commit();
        }
    }
}
