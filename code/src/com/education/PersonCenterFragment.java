package com.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
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
import com.education.widget.SimpleBlockedDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonCenterFragment extends CommonFragment {
	
	private static final String TAG = PersonCenterFragment.class.getSimpleName();
    private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment.newInstance();

    private LayoutInflater mInflater;
    private ListView mListView;
    private Resources mResources;
    private List<Item> mItemList = new ArrayList<Item>();
    private ItemAdapter mItemAdapter;
    private Activity mActivity;


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
            }
            else {
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
                if (TextUtils.isEmpty(user.getNickName())) {
                    nicknameDialog(view);
                }
            } else if (position == 1) {
                startActivity(new Intent(mActivity, ChangePasswordActivity.class));
            } else if (position == 2) {

            } else if (position == 3) {

            } else if (position == 4) {
                Share share = new Share();
                share.setTitle("测试");
                share.setUrl("www.baidu.com");
                share.setDescription("这里可以多写一些字啊多写一些字啊多写一些字!");
                AppHelper.showShareDialog(mActivity, share);
            }
        }

        public Object getItem(int position) {
            return mItemList.get(position);
        }
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
                .setTitle("设置昵称")
                .setView(linearLayout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.SET_NICKNAME
                , null, new VolleyResponseListener(mActivity) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                mBlockedDialogFragment.dismissAllowingStateLoss();
                if (success) {
                    User user = User.getInstance();
                    user.setNickName(nickname);
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
        item1.title = "昵称";
        item1.icon = R.drawable.nickname;
        item1.desc = user.getNickName();
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
        item5.icon = R.drawable.about;
        mItemList.add(item5);

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