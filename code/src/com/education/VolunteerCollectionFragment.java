package com.education;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.CollegeItem;
import com.education.entity.ErrorData;
import com.education.entity.MajorItem;
import com.education.entity.User;
import com.education.utils.LogUtil;
import com.education.widget.SimpleBlockedDialogFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolunteerCollectionFragment extends CommonFragment {

	private static final String TAG = SmartRecomentFragment.class
			.getSimpleName();
	private SimpleBlockedDialogFragment mBlockedDialogFragment = SimpleBlockedDialogFragment
			.newInstance();

	protected LayoutInflater mInflater;
	protected Resources mResources;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions mDisplayImageOptions;

	private static final int TYPE_COLLEGE = 0;
	private static final int TYPE_MAJOR = 1;
	private int mType = TYPE_COLLEGE;
	private ListView mListView;
	private List mCollegeItemList;
	private List mMajorItemList = new ArrayList();
	private List mItemList = new ArrayList();
	private ItemAdapter mItemAdapter;
	private RelativeLayout mHeaderLayout;
	private TextView mHeaderTitleTextView;
	private Activity mActivity;

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResources = getResources();
		mActivity = getActivity();
		mDisplayImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main_volunteer, container,
				false);

		mInflater = inflater;
		mListView = (ListView) v.findViewById(R.id.list);
		mItemAdapter = new ItemAdapter();
		mListView.setAdapter(mItemAdapter);

		mHeaderLayout = (RelativeLayout) mInflater.inflate(
				R.layout.header_collection_list, null);
		mHeaderTitleTextView = (TextView) mHeaderLayout
				.findViewById(R.id.header_title);
		if (mType == TYPE_COLLEGE) {
			shouCangYuanXiaoLieBiao();
		} else {
			displayMajor();
		}
		return v;
	}

	public boolean back() {
		if (mType == TYPE_COLLEGE) {
			return true;
		} else {
			mType = TYPE_COLLEGE;
			shouCangYuanXiaoLieBiao();
		}
		return false;
	}

	private class ItemAdapter extends BaseAdapter implements
			AdapterView.OnItemClickListener {
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
				convertView = mInflater.inflate(R.layout.item_college, null,
						false);
				holder = new ViewHolder();
				holder.dividerView = convertView.findViewById(R.id.divider);
				holder.titleTextView = (TextView) convertView
						.findViewById(R.id.item_title);
				holder.descTextView = (TextView) convertView
						.findViewById(R.id.desc);
				holder.iconImageView = (ImageView) convertView
						.findViewById(R.id.icon);
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT,
						mResources.getDimensionPixelSize(R.dimen.dimen_34_dip));
				convertView.setLayoutParams(lp);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			convertView.setTag(holder);

			Object item = mItemList.get(position);
			if (mType == TYPE_COLLEGE) {
				CollegeItem collegeItem = (CollegeItem) item;

				holder.descTextView.setText(collegeItem.getZysl());
				// imageLoader.displayImage(item.getIcon(),
				// holder.iconImageView, mDisplayImageOptions, new
				// ImageLoadingListener() {
				// @Override
				// public void onLoadingStarted(String imageUri, View view) {
				// }
				//
				// @Override
				// public void onLoadingFailed(String imageUri, View view,
				// FailReason failReason) {
				// }
				//
				// @Override
				// public void onLoadingComplete(String imageUri, View view,
				// Bitmap loadedImage) {
				// ImageView imageView = (ImageView) view;
				// imageView.setImageBitmap(loadedImage);
				// }
				//
				// @Override
				// public void onLoadingCancelled(String imageUri, View view) {
				// }
				// });

				holder.titleTextView.setText(collegeItem.getYxmc());
			} else {
				MajorItem majorItem = (MajorItem) item;
				int source = majorItem.getSource();
				if (source == 0) {
					// 未收藏
				} else if (source == 1) {
					holder.descTextView.setText("手工筛选");
				} else if (source == 2) {
					holder.descTextView.setText("智能推荐");
				}
				// imageLoader.displayImage(item.getIcon(),
				// holder.iconImageView, mDisplayImageOptions, new
				// ImageLoadingListener() {
				// @Override
				// public void onLoadingStarted(String imageUri, View view) {
				// }
				//
				// @Override
				// public void onLoadingFailed(String imageUri, View view,
				// FailReason failReason) {
				// }
				//
				// @Override
				// public void onLoadingComplete(String imageUri, View view,
				// Bitmap loadedImage) {
				// ImageView imageView = (ImageView) view;
				// imageView.setImageBitmap(loadedImage);
				// }
				//
				// @Override
				// public void onLoadingCancelled(String imageUri, View view) {
				// }
				// });

				holder.titleTextView.setText(majorItem.getZymc());
			}

			return convertView;
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mType == TYPE_MAJOR) {
				Intent intent = new Intent(getActivity(),
						MajorDetailActivity.class);
				intent.putExtra("yxdh", mCurrentCollegeItem.getYxdh());
				intent.putExtra(
						"zydh",
						((MajorItem) parent.getAdapter().getItem(position)).getZydh());
				intent.putExtra(
						"yxpc",
						((MajorItem) parent.getAdapter().getItem(position)).getLqpc());
				startActivity(intent);
			} else {
				mType = TYPE_MAJOR;
				CollegeItem collegeItem = (CollegeItem) mCollegeItemList
						.get(position);
				shouCangZhuanYeLieBiao(collegeItem);
			}
		}
	}

	public static class Item {
		private String yxdh;
		private String yxmc;
		private List<MajorItem> sczyDatas = new ArrayList<MajorItem>();

		public String getYxdh() {
			return yxdh;
		}

		public void setYxdh(String yxdh) {
			this.yxdh = yxdh;
		}

		public String getYxmc() {
			return yxmc;
		}

		public void setYxmc(String yxmc) {
			this.yxmc = yxmc;
		}

		public List<MajorItem> getSczyDatas() {
			return sczyDatas;
		}

		public void setSczyDatas(List<MajorItem> sczyDatas) {
			this.sczyDatas = sczyDatas;
		}
	}

	private static class ViewHolder {
		TextView titleTextView;
		TextView descTextView;
		ImageView iconImageView;
		View dividerView;
	}

	private void displayMajor() {
		mItemList = mMajorItemList;
		// mListView.setOnItemClickListener(null);
		mListView.addHeaderView(mHeaderLayout);
		mHeaderTitleTextView.setText(mResources.getString(
				R.string.major_collected_in_college, mMajorItemList.size()));
		mItemAdapter.notifyDataSetChanged();
	}

	private void shouCangYuanXiaoLieBiao() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");

		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHOU_CANG_YUAN_XIAO_LIE_BIAO, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							mCollegeItemList = new ArrayList<Object>();
							JSONArray array = response.getJSONArray("datas");
							int size = array.size();
							for (int i = 0; i < size; i++) {
								String item = array.getString(i);
								CollegeItem collegeItem = JSON.parseObject(
										item, CollegeItem.class);
								mCollegeItemList.add(collegeItem);
							}

							mItemList = mCollegeItemList;
							mListView.setOnItemClickListener(mItemAdapter);
							mListView.removeHeaderView(mHeaderLayout);
							mItemAdapter.notifyDataSetChanged();
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(mActivity, errorData.getText(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								mActivity,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", "8a8a92f34dce12a0014dce1b97b90000"); // user.getId()
				return AppHelper.makeSimpleData("getcollectschool", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	private CollegeItem mCurrentCollegeItem;

	private void shouCangZhuanYeLieBiao(final CollegeItem collegeItem) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mBlockedDialogFragment.show(ft, "block_dialog");
		final FastJsonRequest request = new FastJsonRequest(
				Request.Method.POST, Url.SHOU_CANG_ZHUAN_YE_LIE_BIAO, null,
				new VolleyResponseListener(mActivity) {
					@Override
					public void onSuccessfulResponse(JSONObject response,
							boolean success) {
						mBlockedDialogFragment.dismissAllowingStateLoss();
						if (success) {
							String datas = response.getString("datas");
							Item item = JSON.parseObject(datas, Item.class);
							mMajorItemList = item.getSczyDatas();
							displayMajor();
							mCurrentCollegeItem = collegeItem;
						} else {
							ErrorData errorData = AppHelper
									.getErrorData(response);
							Toast.makeText(mActivity, errorData.getText(),
									Toast.LENGTH_SHORT).show();
							mCurrentCollegeItem = null;
						}
					}
				}, new VolleyErrorListener() {
					@Override
					public void onVolleyErrorResponse(VolleyError volleyError) {
						mCurrentCollegeItem = null;
						mBlockedDialogFragment.dismissAllowingStateLoss();
						LogUtil.logNetworkResponse(volleyError, TAG);
						Toast.makeText(
								mActivity,
								getResources().getString(
										R.string.internet_exception),
								Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				User user = User.getInstance();
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", "8a8a92f34dce12a0014dce1b97b90000"); // user.getId()
				map.put("yxdh", collegeItem.getYxdh());
				return AppHelper.makeSimpleData("getcollectmajor", map);
			}
		};
		EduApp.sRequestQueue.add(request);
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}
}