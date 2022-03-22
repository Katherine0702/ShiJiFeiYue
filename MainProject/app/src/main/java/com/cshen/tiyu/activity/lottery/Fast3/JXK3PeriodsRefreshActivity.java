package com.cshen.tiyu.activity.lottery.Fast3;

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
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.DisplayUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


import de.greenrobot.event.EventBus;

public class JXK3PeriodsRefreshActivity extends BaseActivity {
	private RefreshListView refreshList_sdel11to5;
	private JXK3PeriodsRefreshActivity _this;
	private HelpAdapter helpAdapter = null;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ArrayList<Prize> dataList;
	private String lottery;
	int start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periods_sdel11to5);
		_this = this;
		Intent intent = getIntent();
		lottery = intent.getStringExtra("lottery");
		initHead();

		initView();
		initAllDataForList();
		refreshList_sdel11to5.getItemAtPosition(0);

	}

	private void initView() {
		refreshList_sdel11to5 = (RefreshListView) findViewById(R.id.refreshList_sdel11to5);
		helpAdapter = new HelpAdapter();
		refreshList_sdel11to5.setAdapter(helpAdapter);
		refreshList_sdel11to5.setOnRefreshListener(new OnRefreshListener() {

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

					ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,lottery,
							start + "", step + "","",
							new CallBack<PrizeList>() {
						@Override
						public void onFailure(ErrorMsg errorMessage) {
							more--;
							ToastUtils.showShort(_this,
									errorMessage.msg + "");
							if (helpAdapter != null) {
								helpAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshList_sdel11to5.onRefreshComplete(false);
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
							if (helpAdapter != null) {
								helpAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshList_sdel11to5.onRefreshComplete(false);
						}
					});

				} else {
					initAllDataForList();
				}

			}
		});
		refreshList_sdel11to5.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Prize data=dataList.get(position);
				if (data!=null) {
					Intent intent=new Intent(_this,JXK3PeriodDetailActivity.class);
					intent.putExtra("periodNumber", data.getPeriodNumber());
					intent.putExtra("prizeTime", data.getPrizeTime());
					intent.putExtra("result", data.getResult());
					intent.putExtra("lottery", lottery);
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
		if (helpAdapter != null) {
			helpAdapter.notifyDataSetChanged();
		}
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,lottery, more + "", step + "",
				"",new CallBack<PrizeList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				ToastUtils.showShort(_this, errorMessage.msg + "");
				if (helpAdapter != null) {
					helpAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshList_sdel11to5.onRefreshComplete(false);
			}

			@Override
			public void onSuccess(PrizeList t) {
				if (dataList == null) {
					dataList = new ArrayList<Prize>();
				}
				dataList.addAll(t.getResultList());
				helpAdapter.setDataList(dataList);
				helpAdapter.notifyDataSetChanged();
				refreshList_sdel11to5.onRefreshComplete(false);
			}
		});

	}

	private void initHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_do);
		TextView tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		tv_head_title.setText("江西快3");



		tvTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(_this,
						Fast3MainActivity.class);

				if (null != intent) {
					startActivity(intent);
					finish();
				}

			}
		});

		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(_this,
						MainActivity.class);
				startActivity(intent);
				EventBus.getDefault().post("updatePeriodsInfo");
				finish();
			}
		});

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Intent intent = new Intent(_this,
				MainActivity.class);
		startActivity(intent);
		EventBus.getDefault().post("updatePeriodsInfo");
		finish();
		return super.onKeyUp(keyCode, event);
	}
	class HelpAdapter extends BaseAdapter {
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
				convertView = View.inflate(_this, R.layout.sdel11to5_period_item,
						null);
				holder = new ViewHolder();
				holder.textview_periodNumber_sdel11to5 = (TextView) convertView
						.findViewById(R.id.textview_periodNumber_sdel11to5);
				holder.textview_time_sdel11to5 = (TextView) convertView
						.findViewById(R.id.textview_time_sdel11to5);
				holder.textview_result_sdel11to501 = (TextView) convertView
						.findViewById(R.id.textview_result_sdel11to501);
				holder.textview_result_sdel11to502 = (TextView) convertView
						.findViewById(R.id.textview_result_sdel11to502);
				holder.textview_result_sdel11to503 = (TextView) convertView
						.findViewById(R.id.textview_result_sdel11to503);
				convertView.findViewById(R.id.textview_result_sdel11to504)
				.setVisibility(View.GONE);
				convertView.findViewById(R.id.textview_result_sdel11to505)
				.setVisibility(View.GONE);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data != null) {
				if (!TextUtils.isEmpty(data.getPeriodNumber())) {
					holder.textview_periodNumber_sdel11to5.setText("第"
							+ data.getPeriodNumber() + "期");
				}
				String prizeTime = data.getPrizeTime();
				if (!TextUtils.isEmpty(prizeTime)) {
					prizeTime = prizeTime.trim();
					try {
						holder.textview_time_sdel11to5.setText(prizeTime.subSequence(
								5, 10)
								+ " ("
								+ getDayForWeek(prizeTime.substring(0, 10))
								+ ")");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (data.getResult() != null) {
					String[] resultNumber = data.getResult().split(",");
					holder.textview_result_sdel11to501.setText(Util
							.periodNumberFromat(resultNumber[0]));
					holder.textview_result_sdel11to502.setText(Util
							.periodNumberFromat(resultNumber[1]));
					holder.textview_result_sdel11to503.setText(Util
							.periodNumberFromat(resultNumber[2]));

					if (position==0) {
						int left_right_Pix = DisplayUtil.dip2px(_this, 6);
						int top_bottom_Pix = DisplayUtil.dip2px(_this, 4);
						holder.textview_result_sdel11to501.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
						holder.textview_result_sdel11to501.setTextColor(Color.WHITE);
						holder.textview_result_sdel11to501.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
						holder.textview_result_sdel11to502.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
						holder.textview_result_sdel11to502.setTextColor(Color.WHITE);
						holder.textview_result_sdel11to502.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
						holder.textview_result_sdel11to503.setBackgroundDrawable(getResources().getDrawable(R.drawable.chooseballred));
						holder.textview_result_sdel11to503.setTextColor(Color.WHITE);
						holder.textview_result_sdel11to503.setPadding(left_right_Pix,top_bottom_Pix,left_right_Pix,top_bottom_Pix);
					}else{
						holder.textview_result_sdel11to501.setBackgroundDrawable(null);
						holder.textview_result_sdel11to501.setTextColor(getResources().getColor(R.color.mainred));
						holder.textview_result_sdel11to502.setBackgroundDrawable(null);
						holder.textview_result_sdel11to502.setTextColor(getResources().getColor(R.color.mainred));
						holder.textview_result_sdel11to503.setBackgroundDrawable(null);
						holder.textview_result_sdel11to503.setTextColor(getResources().getColor(R.color.mainred));
					}
				}
			}
			return convertView;
		}

		private String getDayForWeek(String date) throws Exception {
			String day = null;
			switch (DateUtils.dayForWeek(date)) {
			case 1:
				day = "周一";
				break;
			case 2:
				day = "周二";
				break;
			case 3:
				day = "周三";
				break;
			case 4:
				day = "周四";
				break;
			case 5:
				day = "周五";
				break;
			case 6:
				day = "周六";
				break;
			case 7:
				day = "周日";
				break;

			default:
				break;
			}
			return day;
		}

		class ViewHolder {
			public TextView textview_periodNumber_sdel11to5;
			public TextView textview_time_sdel11to5;
			public TextView textview_result_sdel11to501;
			public TextView textview_result_sdel11to502;
			public TextView textview_result_sdel11to503;

		}

	}

}
