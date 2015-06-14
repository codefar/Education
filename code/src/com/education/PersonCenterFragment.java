package com.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.Share;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.ShareDialog;
import com.education.widget.SimpleBlockedDialogFragment;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PersonCenterFragment extends CommonFragment {
	
	private static final String TAG = PersonCenterFragment.class.getSimpleName();

    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private LayoutInflater mInflater;
    private ListView mListView;
    private Resources mResources;
    private List<Item> mItemList = new ArrayList<Item>();
    private ItemAdapter mItemAdapter;
    private Activity mActivity;

    private IWXAPI api;

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
        mActivity = getActivity();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_center, container, false);

        api = WXAPIFactory.createWXAPI(mActivity, EduApp.WX_APP_ID);
        api.registerApp(EduApp.WX_APP_ID);
        mInflater = inflater;
        mListView = (ListView) v.findViewById(R.id.list);
        mItemAdapter = new ItemAdapter();
        mListView.setAdapter(mItemAdapter);
        mListView.setOnItemClickListener(mItemAdapter);
        makePreferenceList();
        return v;
    }

    private class ItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        public int getCount() {
            return mItemList.size();
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_center, null, false);
                holder = new ViewHolder();
                holder.dividerView = convertView.findViewById(R.id.divider);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.item_title);
                holder.descTextView = (TextView) convertView.findViewById(R.id.desc);
                holder.iconImageView = (ImageView) convertView.findViewById(R.id.icon);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
                convertView.setLayoutParams(lp);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setTag(holder);

            Item item = mItemList.get(position);
            holder.descTextView.setText(item.desc);
            holder.titleTextView.setText(item.title);
            holder.iconImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, item.icon));

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User user = User.getInstance();
            if (position == 0) {
                if (TextUtils.isEmpty(user.getAccountId())) { //设置昵称/用户名
                    nicknameDialog(view);
                }
            } else if (position == 1) { //修改密码
                startActivity(new Intent(mActivity, ChangePasswordActivity.class));
            } else if (position == 2) { //个人信息
                startActivity(new Intent(mActivity, UserInfoActivity.class));
            } else if (position == 3) { // 关于
                startActivity(new Intent(mActivity, AboutUsActivity.class));
            } else if (position == 4) { //分享
                ShareDialog dialog = new ShareDialog(mActivity);
                dialog.setOnDismissListener(new ShareDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(int type) {
                        fetchShareData(type);
                    }
                });
                dialog.show();
            } else if (position == 5) { //测试用的
                User.clearUser();
                mActivity.finish();
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }
    }

    private void fetchShareData(final int type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mBlockedDialogFragment.updateMessage("");
        mBlockedDialogFragment.show(ft, "block_dialog");

        final User user = User.getInstance();
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.GET_SHARE_URL
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    String data = response.getString("urls");
                    Share share = JSON.parseObject(data, Share.class);

                    if (type == ShareDialog.SHARE_WEBCHAT_FRIEND_CIRCLE) {
                        webchatShare(type, share);
                    } else if (type == ShareDialog.SHARE_WEBCHAT_FRIEND) {
                        webchatShare(type, share);
                    } else if (type == ShareDialog.SHARE_SMS) {
                        smsShare(share);
                    }
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mBlockedDialogFragment.dismiss();
                Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", user.getId());
                params.put("type", String.valueOf(type));
                return AppHelper.makeSimpleData("getShareUrl", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    private void webchatShare(final int type, final Share share) {
        new Thread() {
            public void run() {
                if (type == ShareDialog.SHARE_WEBCHAT_FRIEND_CIRCLE) {
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = makeWXMediaMessage(share);
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    api.sendReq(req);
                } else {
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = makeWXMediaMessage(share);
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    api.sendReq(req);
                }
            }
        }.start();
    }

    private void smsShare(Share share) {
        String content = share.getDesc();
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", content);
        startActivity( mIntent );
    }


    private static final int THUMB_SIZE = 150;
    private WXMediaMessage makeWXMediaMessage(Share share) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share.getShareUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share.getTitle();
        msg.description = share.getDesc();
        if (share.getIconUrl() != null) {
            try {
                URL url = new URL(share.getIconUrl());
                Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp, true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void nicknameDialog(final View view) {
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setPadding(mResources.getDimensionPixelOffset(R.dimen.dimen_17_dip),
                                mResources.getDimensionPixelOffset(R.dimen.dimen_17_dip),
                                mResources.getDimensionPixelOffset(R.dimen.dimen_17_dip),
                                mResources.getDimensionPixelOffset(R.dimen.dimen_17_dip));
        final EditText et = new EditText(mActivity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(et, lp);
        new AlertDialog.Builder(mActivity)
                .setTitle("设置用户名")
                .setView(linearLayout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickname = et.getText().toString();
                        if (!Pattern.matches(Constants.NICKNAME_STR, nickname)) {
                            Toast.makeText(mActivity, "用户名只能由6-20位大小写字母、数字下划线和连词符组成", Toast.LENGTH_LONG).show();
                            nicknameDialog(view);
                            return;
                        }
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        mBlockedDialogFragment.updateMessage(getText(R.string.progress_bar_committing_please_wait));
                        mBlockedDialogFragment.show(ft, "block_dialog");
                        setNickname(User.getInstance().getId(), et.getText().toString(), view);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setNickname(final String uid, final String nickname, final View parent) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.SET_ACCOUNT
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    User user = User.getInstance();
                    user.setAccountId(nickname);
                    User.saveUser(user);
                    TextView tv = (TextView) parent.findViewById(R.id.desc);
                    tv.setText(nickname);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(mActivity, errorData.getText(), Toast.LENGTH_SHORT).show();
                    nicknameDialog(parent);
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                mBlockedDialogFragment.dismiss();
                Toast.makeText(mActivity, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", uid);
                params.put("account", nickname);
                return AppHelper.makeSimpleData("setaccount", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }
    
    public class Item {
        int icon;
        String title;
        String desc;
    }

    private void makePreferenceList() {
        mItemList.clear();

        User user = User.getInstance();
        Item item1 = new Item();
        item1.title = "用户名";
        item1.icon = R.drawable.nickname;
        item1.desc = user.getAccountId();
        mItemList.add(item1);

        Item item2 = new Item();
        item2.title = "修改密码";
        item2.icon = R.drawable.change_password;
        mItemList.add(item2);

        Item item3 = new Item();
        item3.title = "个人信息";
        item3.icon = R.drawable.profile;
        mItemList.add(item3);

        Item item4 = new Item();
        item4.title = "关于";
        item4.icon = R.drawable.about;
        mItemList.add(item4);

        Item item5 = new Item();
        item5.title = "分享";
        item5.icon = R.drawable.share;
        mItemList.add(item5);

        Item item6 = new Item();
        item6.title = "退出";
        item6.icon = R.drawable.exit;
        mItemList.add(item6);

        mItemAdapter.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView descTextView;
        ImageView iconImageView;
        View dividerView;
    }

    @Override
	protected String getLogTag() {
		return TAG;
	}
}