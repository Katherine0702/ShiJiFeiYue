package com.cshen.tiyu.activity.lottery.dlt;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.DisplayUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


import de.greenrobot.event.EventBus;

public class DLTPeriodsActivity extends BaseActivity {
	private RefreshListView refreshList_dlt;
	private DLTPeriodsActivity _this;
	private DltAdapter dltAdapter = null;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ArrayList<Prize> dataList;
	int start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periods_dlt);
		_this = this;
		initHead();

		initView();
		initAllDataForList();
		refreshList_dlt.getItemAtPosition(0);

	}

	private void initView() {
		refreshList_dlt = (RefreshListView) findViewById(R.id.refreshList_dlt);
		dltAdapter = new DltAdapter();
		refreshList_dlt.setAdapter(dltAdapter);
		refreshList_dlt.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				requestDataFromServer(false);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				if (isLoadingMore) {
					more++;
					start = more * step;
					ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"13",
							start + "", step + "","",
							new CallBack<PrizeList>() {
								@Override
								public void onFailure(ErrorMsg errorMessage) {
									more--;
									ToastUtils.showShort(_this,
											errorMessage.msg + "");
									if (dltAdapter != null) {
										dltAdapter.notifyDataSetChanged();// 重新刷新数据
									}
									refreshList_dlt.onRefreshComplete(false);
								}

								@Override
								public void onSuccess(PrizeList t) {
									if (t.getResultList() == null
											|| t.getResultList().size() == 0) {
										ToastUtils.showShort(_this, "已无更多数据");
										more--;
									}
									if (dataList == null) {
										dataList = new ArrayList<Prize>();
									}
									dataList.addAll(t.getResultList());
									if (dltAdapter != null) {
										dltAdapter.notifyDataSetChanged();// 重新刷新数据
									}
									refreshList_dlt.onRefreshComplete(false);
								}
							});

				} else {
					initAllDataForList();
				}

			}
		});
		refreshList_dlt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Prize data=dataList.get(position);
				if (data!=null) {
					Intent intent=new Intent(DLTPeriodsActivity.this,DLTPeriodDetailActivity.class);
					intent.putExtra("periodNumber", data.getPeriodNumber());
					intent.putExtra("prizeTime", data.getPrizeTime());
					intent.putExtra("periodId", data.getPeriodId());
					intent.putExtra("result", data.getResult());
					startActivity(intent);
				}
				
			}
		});

	}

	protected void initAllDataForList() {
		more = 0;
		if (dataList != null) {
			dataList.clear();
		}
		if (dltAdapter != null) {
			dltAdapter.notifyDataSetChanged();
		}
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"13", more + "", step + "","",
				new CallBack<PrizeList>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						ToastUtils.showShort(_this, errorMessage.msg + "");
						if (dltAdapter != null) {
							dltAdapter.notifyDataSetChanged();// 重新刷新数据
						}
						refreshList_dlt.onRefreshComplete(false);
					}

					@Override
					public void onSuccess(PrizeList t) {
						if (dataList == null) {
							dataList = new ArrayList<Prize>();
						}
						dataList.addAll(t.getResultList());
						dltAdapter.setDataList(dataList);
						dltAdapter.notifyDataSetChanged();
						refreshList_dlt.onRefreshComplete(false);
					}
				});

	}

	private void initHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_do);

		tvTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(DLTPeriodsActivity.this,
						DLTMainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
//				Intent intent = new Intent(DLTPeriodsActivity.this,
//						MainActivity.class);
//				startActivity(intent);
				EventBus.getDefault().post("updatePeriodsInfo");
				finish();
			}
		});

	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// TODO 自动生成的方法存根
//		Intent intent = new Intent(DLTPeriodsActivity.this,
//				MainActivity.class);
//		startActivity(intent);
		EventBus.getDefault().post("updatePeriodsInfo");
		finish();
		return super.onKeyUp(keyCode, event);
	}

	class DltAdapter extends BaseAdapter {
		private ArrayList<Prize> dataList;

		public void setDataList(ArrayList<Prize> dataList) {
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			// How many items are in the data set represented by this
			// Adapter.(在此适配器中所代表的数据集中的条目数)
			if (dataList == null) {
				return 0;
			} else {
				return dataList.size();
			}
		}

		@Override
		public Prize getItem(int position) {
			// Get the data item associated with the specified position in the
			// data set.(获取数据集中与指定索引对应的数据项)
			if (dataList != null) {
				return dataList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// Get the row id associated with the specified position in the
			// list.(取在列表中与指定索引对应的行id)
			if (dataList != null) {
				return Long.parseLong(dataList.get(position).getPeriodId());
			} else {
				return position;
			}
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Get a View that displays the data at the specified position in
			// the data set.
			Prize data = null;
			if (dataList != null) {
				data = dataList.get(position);
			}
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(_this, R.layout.dlt_period_item,
						null);
				holder = new ViewHolder();
				holder.textview_periodNumber_dlt = (TextView) convertView
						.findViewById(R.id.textview_periodNumber_dlt);
				holder.textview_time_dlt = (TextView) convertView
						.findViewById(R.id.textview_time_dlt);
				holder.textview_result_dlt01 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt01);
				holder.textview_result_dlt02 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt02);
				holder.textview_result_dlt03 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt03);
				holder.textview_result_dlt04 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt04);
				holder.textview_result_dlt05 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt05);
				holder.textview_result_dlt06 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt06);
				holder.textview_result_dlt07 = (TextView) convertView
						.findViewById(R.id.textview_result_dlt07);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data != null) {
				if (!TextUtils.isEmpty(data.getPeriodNumber())) {
					holder.textview_periodNumber_dlt.setText("第"
							+ data.getPeriodNumber() + "期");
				}
				String prizeTime = data.getPrizeTime();
				if (!TextUtils.isEmpty(prizeTime)) {
					prizeTime = prizeTime.trim();
					try {
						holder.textview_time_dlt.setText(prizeTime.subSequence(
								5, 10)
								+ " ("
								+ Util.getDayForWeek(prizeTime.substring(0, 10))
								+ ")");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (data.getResult() != null) {
					String[] resultNumber = data.getResult().split(",");
					holder.textview_result_dlt01.setText(Util
							.periodNumberFromat(resultNumber[0]));
					holder.textview_result_dlt02.setText(Util
							.periodNumberFromat(resultNumber[1]));
					holder.textview_result_dlt03.setText(Util
							.periodNumberFromat(resultNumber[2]));
					holder.textview_result_dlt04.setText(Util
							.periodNumberFromat(resultNumber[3]));
					holder.textview_result_dlt05.setText(Util
							.periodNumberFromat(resultNumber[4]));
					holder.textview_result_dlt06.setText(Util
							.periodNumberFromat(resultNumber[5]));
					holder.textview_result_dlt07.setText(Util
							.periodNumberFromat(resultNumber[6]));

					 if (position==0 ) {
					 int left_right_Pix = DisplayUtil.dip2px(_this, 6);
					 int top_bottom_Pix = DisplayUtil.dip2px(_this, 4);
					 holder.textview_result_dlt01.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
					 holder.textview_result_dlt01.setTextColor(Color.WHITE);
					 holder.textview_result_dlt01.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt02.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
					 holder.textview_result_dlt02.setTextColor(Color.WHITE);
					 holder.textview_result_dlt02.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt03.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
					 holder.textview_result_dlt03.setTextColor(Color.WHITE);
					 holder.textview_result_dlt03.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt04.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
					 holder.textview_result_dlt04.setTextColor(Color.WHITE);
					 holder.textview_result_dlt04.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt05.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
					 holder.textview_result_dlt05.setTextColor(Color.WHITE);
					 holder.textview_result_dlt05.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt06.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballblue));
					 holder.textview_result_dlt06.setTextColor(Color.WHITE);
					 holder.textview_result_dlt06.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					 holder.textview_result_dlt07.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballblue));
					 holder.textview_result_dlt07.setTextColor(Color.WHITE);
					 holder.textview_result_dlt07.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					
					 }else{
						 holder.textview_result_dlt01.setBackgroundDrawable(null);
						 holder.textview_result_dlt01.setTextColor(getResources().getColor(R.color.mainred));
						 holder.textview_result_dlt02.setBackgroundDrawable(null);
						 holder.textview_result_dlt02.setTextColor(getResources().getColor(R.color.mainred));
						 holder.textview_result_dlt03.setBackgroundDrawable(null);
						 holder.textview_result_dlt03.setTextColor(getResources().getColor(R.color.mainred));
						 holder.textview_result_dlt04.setBackgroundDrawable(null);
						 holder.textview_result_dlt04.setTextColor(getResources().getColor(R.color.mainred));
						 holder.textview_result_dlt05.setBackgroundDrawable(null);
						 holder.textview_result_dlt05.setTextColor(getResources().getColor(R.color.mainred));
						 holder.textview_result_dlt06.setBackgroundDrawable(null);
						 holder.textview_result_dlt06.setTextColor(getResources().getColor(R.color.commonBlue));
						 holder.textview_result_dlt07.setBackgroundDrawable(null);
						 holder.textview_result_dlt07.setTextColor(getResources().getColor(R.color.commonBlue));
						 
					 }
				}
			}
			return convertView;
		}


		class ViewHolder {
			public TextView textview_periodNumber_dlt;
			public TextView textview_time_dlt;
			public TextView textview_result_dlt01;
			public TextView textview_result_dlt02;
			public TextView textview_result_dlt03;
			public TextView textview_result_dlt04;
			public TextView textview_result_dlt05;
			public TextView textview_result_dlt06;
			public TextView textview_result_dlt07;
			public TextView textview_result_dlt08;

		}

	}

}
