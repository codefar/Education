package com.education;

import com.education.utils.MenuHelper;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
	private Resources mResources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_result4,
				container, false);
        v.findViewById(R.id.website_layout).setOnClickListener(this);
        v.findViewById(R.id.sina_weibo_layout).setOnClickListener(this);
        v.findViewById(R.id.webchat_layout).setOnClickListener(this);
		mResources = getResources();
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
		default:
			break;
		}
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