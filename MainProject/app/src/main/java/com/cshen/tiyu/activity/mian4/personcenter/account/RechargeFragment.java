package com.cshen.tiyu.activity.mian4.personcenter.account;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cshen.tiyu.R;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.account.CathecticItem;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.CathecticItemView;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class RechargeFragment extends Fragment {
	ArrayList<CathecticItem> fundLists;
	RefreshListView refreshListView;
	MyListAdapter myListAdapter = null;
	int more = 0;// 第几次更多加载

	int step = 10;// 每次步数

	View nodata;
	TextView touzhu;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.total_fragment, container, false);
		initView(view);// 初使化控件
		initdata(); // 获取数据

		return view;
	}
	private void compositeData() {
		// TODO 自动生成的方法存根

	}

	private void initdata() {

		more =0;
		// 加载网络数据
		User user = ConstantsBase.getUser(); // 从数据库中获取现在是用的内存中的不长久
		// 从1开始不然不行
		String userId = MyDbUtils.getCurrentUser().getUserId();
		String userPwd = MyDbUtils.getCurrentUser().getUserPwd();

		ServiceUser.getInstance().PostDetailAccount(getActivity(),"10", "0", userId, userPwd, "4",
				ConstantsBase.SEARCH_TIME, new CallBack<ArrayList<CathecticItem>>() {

			@Override
			public void onSuccess(ArrayList<CathecticItem> t) {
				// TODO 自动生成的方法存根

				// 此刻才拿到数据 将数据给adapter 给listview
				//ToastUtils.showShort(getActivity(), "成功获取数据");

				fundLists = t;// 加载数据结束

				// 放数据给listview
				if(fundLists.size()>0){
					setDateForList();
					nodata.setVisibility(View.GONE);
					refreshListView.setVisibility(View.VISIBLE);
				}else{
					nodata.setVisibility(View.VISIBLE);
					refreshListView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				if(errorMessage!=null
						&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(RechargeFragment.this.getActivity(),errorMessage.msg);						
				}
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}

		});

	}

	// 为listview adapter设置数据
	protected void setDateForList() {

		myListAdapter = new MyListAdapter(getActivity());// 最好传个参数过去 以后重构

		refreshListView.setAdapter(myListAdapter);

		// 添加监听器

		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根
				requestDataFromServer(false); // 冲服务器中获取刷新的数据

			}

			@Override
			public void onLoadMore() {
				// TODO 自动生成的方法存根
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根

				if (isLoadingMore) {
					// 加载更多
					more++;
					int start = more * step;

					String userId = MyDbUtils.getCurrentUser().getUserId();
					String userPwd = MyDbUtils.getCurrentUser().getUserPwd();

					// 加载网络数据
					User user = ConstantsBase.getUser(); // 从数据库中获取现在是用的内存中的不长久
					// 从1开始不然不行
					ServiceUser.getInstance().PostDetailAccount(getActivity(),
							"10",
							String.valueOf(start),
							userId,
							userPwd,
							"4",
							ConstantsBase.SEARCH_TIME,
							new CallBack<ArrayList<CathecticItem>>() {

								@Override
								public void onSuccess(ArrayList<CathecticItem> t) {
									// TODO 自动生成的方法存根

									// 此刻才拿到数据 将数据给adapter 给listview

									ArrayList<CathecticItem> morefundList = t;// 加载数据结束
									if (morefundList == null||morefundList.size() == 0) {
										ToastUtils.showLong(getActivity(), "没有更多数据了");
										more--;
									}

									fundLists.addAll(morefundList);
									myListAdapter.notifyDataSetChanged();// 重新刷新数据

									refreshListView.onRefreshComplete(false);

								}

								@Override
								public void onFailure(ErrorMsg errorMessage) {
									// TODO 自动生成的方法存根
									if(errorMessage!=null
											&&!TextUtils.isEmpty(errorMessage.msg)){
										ToastUtils.showShort(RechargeFragment.this.getActivity(),errorMessage.msg);						
									}									more--;
									refreshListView.onRefreshComplete(false);
								}

							});

				} else {
					// 展示变加载更多数据太少了
					// 跟新下还是那段加载初始化数据的代码 跟新不过是重新访问了下代码而已 默认访问的就是最新的
					// 重新获取数据

					more =0;
					String userId = MyDbUtils.getCurrentUser().getUserId();
					String userPwd = MyDbUtils.getCurrentUser().getUserPwd();
					ServiceUser.getInstance().PostDetailAccount(getActivity(),
									"10",
									"0",
									userId,
									userPwd,
									"4",
									ConstantsBase.SEARCH_TIME,
									new CallBack<ArrayList<CathecticItem>>() {

										@Override
										public void onSuccess(
												ArrayList<CathecticItem> t) {
											// TODO 自动生成的方法存根

											// 此刻才拿到数据 将数据给adapter 给listview
											//											ToastUtils.showShort(getActivity(),
											//													"成功获取数据");

											fundLists = t;// 加载数据结束

											// 放数据给listview

											myListAdapter
											.notifyDataSetChanged();// 重新刷新数据

											refreshListView
											.onRefreshComplete(false);

										}

										@Override
										public void onFailure(
												ErrorMsg errorMessage) {
											// TODO 自动生成的方法存根
											if(errorMessage!=null
													&&!TextUtils.isEmpty(errorMessage.msg)){
												ToastUtils.showShort(RechargeFragment.this.getActivity(),errorMessage.msg);						
											}											myListAdapter.notifyDataSetChanged();// 重新刷新数据
											refreshListView.onRefreshComplete(false);												
										}

									});

				}

			}
		});

	}

	private void initView(View view) {
		// TODO 自动生成的方法存根
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		nodata  = view.findViewById(R.id.nodata);
		view.findViewById(R.id.nodataview).setVisibility(View.GONE);
	}

	class MyListAdapter extends BaseAdapter {

		public MyListAdapter(Context context) {

		}

		@Override
		public int getCount() {

			return fundLists.size();
		}

		@Override
		public CathecticItem getItem(int position) {
			return fundLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CathecticItemView itemView;
			if (convertView == null) {

				itemView = new CathecticItemView(getActivity());

				convertView = itemView; // 首先初始化组合控件 塞给tag 下次直接拿可以不？

				convertView.setTag(itemView);// 把这个类给concertview里面都view都初始化好了下次就不用了
				// 但是里面的view还没有设置数据也就是convertview中的view没数据
			} else {
				itemView = (CathecticItemView) convertView.getTag();

			}

			CathecticItem cathecticItem = fundLists.get(position);

			String cathectic_ymd = cathecticItem.getTime1();// 年月日

			String cathectic_hms = cathecticItem.getTime2();// 时分秒

			String typeString = cathecticItem.getType();

			typeString=Util.getTypeString(typeString);


			String moneyType = cathecticItem.getMoneyType();

			String moneyString = String.valueOf(cathecticItem.getMoney()) + "元";

			itemView.setTextColor(moneyType);
			
			itemView.setNormalTextValue(cathectic_ymd, cathectic_hms,
					typeString, moneyString,null);

			return convertView;

		}

	}



	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//fragment可见时执行加载数据或者进度条等
		} else {
			//不可见时不执行操作
		}
	}

}
