package com.education;

import info.hoang8f.android.segmented.SegmentedGroup;

import com.education.utils.MenuHelper;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SmartRecomentFragmentStep1 extends CommonFragment implements
		OnClickListener, RadioGroup.OnCheckedChangeListener {

	private static final String TAG = SmartRecomentFragmentStep1.class
			.getSimpleName();

	private Button mNextBtn;
	private EditText mNameEditText;
	private EditText mIdEditText;
	private EditText mScoreEditText;
	private EditText mPostionEditText;
	private TextView mZoneTextView;
	private SegmentedGroup mSegmentedGroup;

	private boolean goSmartRecomnetStep2 = false;
	public static final String GO_SMART_RECOMNET_STEP2 = "GO_SMART_RECOMNET_STEP2";

	String[] mKaoQu;
	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupTitleBar();
		if (getArguments() != null) {
			goSmartRecomnetStep2 = getArguments().getBoolean(
					GO_SMART_RECOMNET_STEP2, true);
		}
		mKaoQu = getResources().getStringArray(R.array.kaoqu_name);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_step1,
				container, false);
		mNextBtn = (Button) v.findViewById(R.id.btn_next);
		mNextBtn.setOnClickListener(this);
		mScoreEditText = (EditText) v.findViewById(R.id.editText1);
		mPostionEditText = (EditText) v.findViewById(R.id.editText2);
		mNameEditText = (EditText) v.findViewById(R.id.name);
		mIdEditText = (EditText) v.findViewById(R.id.id);
		mZoneTextView = (TextView) v.findViewById(R.id.editText3);
		mZoneTextView.setOnClickListener(this);
		mSegmentedGroup = (SegmentedGroup) v.findViewById(R.id.segmented);
		mSegmentedGroup.setOnCheckedChangeListener(this);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		setupTitleBar();
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

	protected void setupTitleBar() {
		ActionBar bar = getActivity().getActionBar();
		bar.setTitle(R.string.smart_recomment_smart1);
		setHasOptionsMenu(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_next) {
			if (goSmartRecomnetStep2) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.container, new SmartRecomentFragmentStep2(),
						"step2");
				ft.addToBackStack(null);
				ft.commit();
			} else {
				// whet to do
			}
		} else if (v.getId() == R.id.editText3) {
			simpleDialog();
		}
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.i(TAG, "理科" + checkedId);
		switch (checkedId) {
		case R.id.btn_li_ke:
			Toast.makeText(getActivity(), "理科", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_wen_ke:
			Toast.makeText(getActivity(), "文科", Toast.LENGTH_SHORT).show();
			break;
		default:
			// Nothing to do
			break;
		}
	}

	/**
	 * 单选Dialogs
	 */
	private void simpleDialog() {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择考区");
		builder.setSingleChoiceItems(mKaoQu, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						mZoneTextView.setText(mKaoQu[which]);
					}
				});
		dialog = builder.create();
		dialog.show();
	}
}