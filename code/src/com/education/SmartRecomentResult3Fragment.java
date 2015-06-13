package com.education;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.renderscript.Type.CubemapFace;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.education.TestActivity.Item6;
import com.education.entity.HistoryMajor;
import com.education.entity.UserInfo;
import com.education.utils.MenuHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class SmartRecomentResult3Fragment extends CommonFragment implements
		OnClickListener {

	private static final String TAG = SmartRecomentResult3Fragment.class
			.getSimpleName();
	private List<HistoryMajor> mHistoryMajorItems = new ArrayList<HistoryMajor>();

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private LineChart mChart;
	private Item6 mItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smart_recomment_result3,
				container, false);
		mChart = (LineChart) v.findViewById(R.id.chart1);
		// if enabled, the chart will always start at zero on the y-axis

		// no description text
		// mChart.setOnChartValueSelectedListener(this);

		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.getXAxis().setEnabled(true); // x轴
		mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		mChart.getXAxis().setDrawGridLines(false);
		mChart.getAxisRight().setEnabled(false); // 右纵轴
		mChart.getAxisLeft().setDrawGridLines(false);
		// mChart.setStartAtZero(true);

		// enable value highlighting
		mChart.setHighlightEnabled(true);

		// disable touch gestures
		mChart.setTouchEnabled(false);
		mChart.setDragEnabled(false);
		mChart.setScaleEnabled(false);
		mChart.setPinchZoom(false);

		Legend l = mChart.getLegend();
		l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

		mItem = ((SmartRecommentResultActivity) getActivity()).getItem();
		if (mItem != null) {
			mHistoryMajorItems.clear();
			mHistoryMajorItems.addAll(mItem.getLssj());
			setData();
		}
		return v;
	}

	private void setData() {
		int dataSize = mHistoryMajorItems.size();

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<Entry> cjMaxValues = new ArrayList<Entry>();
		ArrayList<Entry> cjMinValues = new ArrayList<Entry>();
		ArrayList<Entry> cjPjValues = new ArrayList<Entry>();
		int sum = 0;
		for (int i = 0; i < dataSize; i++) {
			xVals.add(mHistoryMajorItems.get(i).getYear() + "");
			cjMaxValues.add(new Entry((float) mHistoryMajorItems.get(i)
					.getMaxcj(), i));
			cjMinValues.add(new Entry((float) mHistoryMajorItems.get(i)
					.getMincj(), i));
			cjPjValues.add(new Entry((float) mHistoryMajorItems.get(i)
					.getPjcj(), i));
			sum += mHistoryMajorItems.get(i).getPjcj();
		}

		LineDataSet dMax = new LineDataSet(cjMaxValues, "最大成绩");
		dMax.setLineWidth(2.5f);
		dMax.setColor(mColors[0]);
		dataSets.add(dMax);
		
		LineDataSet dMin = new LineDataSet(cjMinValues, "最小成绩");
		dMin.setLineWidth(2.5f);
		dMin.setColor(mColors[1]);
		dataSets.add(dMin);
		
		LineDataSet dPj = new LineDataSet(cjPjValues, "平均成绩");
		dPj.setLineWidth(2.5f);
		dPj.setColor(mColors[2]);
		dataSets.add(dPj);

		for (LineDataSet set : dataSets) {
			set.setDrawCubic(true);
		}
		
		ArrayList<Entry> cjPjPjValues = new ArrayList<Entry>(dataSize);
		for (int i = 0; i < dataSize; i++) {
			cjPjPjValues.add(new Entry(sum /dataSize, i));
		}
		LineDataSet dPjAll = new LineDataSet(cjPjPjValues, "");
		dPjAll.setLineWidth(2.5f);
		dPjAll.setColor(mColors[3]);
		dPjAll.setDrawCubic(false);
		dataSets.add(dPjAll);

		LineData data = new LineData(xVals, dataSets);
		mChart.getAxisLeft().setStartAtZero(false);
		mChart.setData(data);
		mChart.invalidate();
	}

	private int[] mColors = new int[] { ColorTemplate.VORDIPLOM_COLORS[0],
			ColorTemplate.VORDIPLOM_COLORS[1],
			ColorTemplate.VORDIPLOM_COLORS[2],
			ColorTemplate.VORDIPLOM_COLORS[3]};

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