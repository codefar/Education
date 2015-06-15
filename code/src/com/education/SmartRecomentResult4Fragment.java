package com.education;

//import net.sourceforge.simcpux.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.utils.MenuHelper;
import com.education.widget.SimpleBlockedDialogFragment;
import com.education.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

public class SmartRecomentResult4Fragment extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentResult4Fragment.class
			.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	private Resources mResources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_result4,
				container, false);
        v.findViewById(R.id.website_layout).setOnClickListener(this);
        v.findViewById(R.id.sina_weibo_layout).setOnClickListener(this);
        v.findViewById(R.id.webchat_layout).setOnClickListener(this);
        v.findViewById(R.id.buy).setOnClickListener(this);
		mResources = getResources();
		api = WXAPIFactory.createWXAPI(getActivity(), EduApp.WX_APP_ID);
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
		switch (v.getId()) {
		case R.id.website_layout:
			copyToClipboard(mResources.getString(R.string.website),
					mResources.getString(R.string.website_content));
			Toast.makeText(
					getActivity(),
					mResources.getString(R.string.already_copy_to_clipboard,
							mResources.getString(R.string.website)),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.sina_weibo_layout:
			copyToClipboard(mResources.getString(R.string.sina_weibo),
					mResources.getString(R.string.sina_weibo_content));
			Toast.makeText(
					getActivity(),
					mResources.getString(R.string.already_copy_to_clipboard,
							mResources.getString(R.string.sina_weibo)),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.webchat_layout:
			copyToClipboard(mResources.getString(R.string.webchat),
					mResources.getString(R.string.webchat_content));
			Toast.makeText(
					getActivity(),
					mResources.getString(R.string.already_copy_to_clipboard,
							mResources.getString(R.string.webchat)),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.buy:
			getorder();
//			startActivity(new Intent(getActivity(), WXPayEntryActivity.class));
			break;
		default:
			break;
		}
	}
	
	private IWXAPI api;
    private void getorder() {
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.WEIXIN_ORDER
                , null, new VolleyResponseListener(getActivity()) {
			@Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
				mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    JSONObject result = response.getJSONObject("result");
                    int status = result.getInteger("status");
                    if (status == 1)  {
                    	String mPartnerId = result.getString("partnerid");
                    	String mPrepayId = result.getString("prepayid");
                    	String  mNonceStr = result.getString("noncestr");
                    	String mTimeStamp = result.getString("timestamp");
                    	String mPackageString = result.getString("package");
                    	String mSign = result.getString("sign");
                    	mSign = "513a4f303c2795f555b33fd136e6aa877b289f26";
                    	String mAppId = result.getString("appid");
                    	Log.v("WX", "partnerId: " + mPartnerId);
                    	Log.v("WX", "prepayId: " + mPrepayId);
                    	Log.v("WX", "nonceStr: " + mNonceStr);
                    	Log.v("WX", "timeStamp: " + mTimeStamp);
                    	Log.v("WX", "packageString: " + mPackageString);
                    	Log.v("WX", "sign: " + mSign);
                    	Log.v("WX", "appId: " + mAppId);
                    	
                    	PayReq req = new PayReq();
                    	req.appId = mAppId;
                		req.partnerId =mPartnerId;
                		req.prepayId = mPrepayId;
                		req.packageValue = mPackageString;
                		req.nonceStr = mNonceStr;
                		req.timeStamp = mTimeStamp;
                		req.sign = mSign;
                		
                		api.registerApp(mAppId);
                		api.sendReq(req);
                    } else {
                        Toast.makeText(getActivity(), result.getString("msgText"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                	mBlockedDialogFragment.dismissAllowingStateLoss();
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(getActivity(), errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
            	User user = User.getInstance();
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", user.getId());
                map.put("product_id", Url.WEIXIN_PRODUCT_ID);
                return AppHelper.makeSimpleData("orderquery", map);
            }
        };
        EduApp.sRequestQueue.add(request);
    }
	

	private void copyToClipboard(String label, String text) {
		ClipboardManager cmb = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setPrimaryClip(ClipData.newPlainText(label, text));
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