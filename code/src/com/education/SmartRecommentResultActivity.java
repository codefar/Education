package com.education;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.TestActivity.Item6;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.utils.LogUtil;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SmartRecommentResultActivity extends FragmentBaseActivity implements
		View.OnClickListener {

	protected static final String TAG = SmartRecommentResultActivity.class.getSimpleName();
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_recomment_result);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(getFragmentManager());
		zhuanYeFenXiBaoGao();
	}

	Item6 item;
    public Item6 getItem() {
		return item;
	}

	//单专业分析报告
    private void zhuanYeFenXiBaoGao() {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.ZHUAN_YE_FEN_XI_BAO_GAO
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
            	Log.i(TAG, response.toJSONString());
                if (success) {
                    String zyfxdata = response.getString("zyfxdata");
                    item = JSON.parseObject(zyfxdata, Item6.class);
                    Toast.makeText(SmartRecommentResultActivity.this, "size: " + item.getXgfx().size(), Toast.LENGTH_SHORT).show();
                    mViewPager.setAdapter(mTabsAdapter);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(SmartRecommentResultActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(SmartRecommentResultActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("yxdh", "哈尔滨理工大学");
                map.put("zydh", "微电子科学与工程");
                map.put("yxpc", String.valueOf(5));
                map.put("kskl", String.valueOf(1));
                map.put("kqdh", String.valueOf(6));
                return AppHelper.makeSimpleData("zyfx", map);
            }
        };
        EduApp.sRequestQueue.add(request);
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
