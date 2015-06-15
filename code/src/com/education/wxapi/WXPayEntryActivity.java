package com.education.wxapi;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.EduApp;
import com.education.R;
import com.education.TestActivity;
import com.education.Url;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();
	
    private IWXAPI api;
    private String mPartnerId;
    private String mPrepayId;
    private String mNonceStr;
    private String mTimeStamp;
    private String mPackageString;
    private String mSign;
    private String mAppId;
	protected PayReq req;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, EduApp.WX_APP_ID);
        api.handleIntent(getIntent(), this);
        
//        getorder();
    }
    
    private void getorder() {
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.WEIXIN_ORDER
                , null, new VolleyResponseListener(this) {
			@Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
				mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    JSONObject result = response.getJSONObject("result");
                    int status = result.getInteger("status");
                    if (status == 1)  {
                    	mPartnerId = result.getString("partnerid");
                    	mPrepayId = result.getString("prepayid");
                    	mNonceStr = result.getString("noncestr");
                    	mTimeStamp = result.getString("timestamp");
                    	mPackageString = result.getString("package");
                    	mSign = result.getString("sign");
                    	mAppId = result.getString("appid");
                    	Log.v("WX", "partnerId: " + mPartnerId);
                    	Log.v("WX", "prepayId: " + mPrepayId);
                    	Log.v("WX", "nonceStr: " + mNonceStr);
                    	Log.v("WX", "timeStamp: " + mTimeStamp);
                    	Log.v("WX", "packageString: " + mPackageString);
                    	Log.v("WX", "sign: " + mSign);
                    	Log.v("WX", "appId: " + mAppId);
                    	
                    	req = new PayReq();
                    	req.appId = mAppId;
                		req.partnerId =mPartnerId;
                		req.prepayId = mPrepayId;
                		req.packageValue = mPackageString;
                		req.nonceStr = mNonceStr;
                		req.timeStamp = mTimeStamp;
                		req.sign = mSign;
                		
                		sendPayReq();
                    } else {
                        Toast.makeText(WXPayEntryActivity.this, result.getString("msgText"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                	mBlockedDialogFragment.dismissAllowingStateLoss();
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(WXPayEntryActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(WXPayEntryActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	private void sendPayReq() {
		api.registerApp(mAppId);
		api.sendReq(req);
	}
	
	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("微信支付结果:" + (resp.errStr +";code=" + String.valueOf(resp.errCode)));
			builder.show();
		}
	}
}