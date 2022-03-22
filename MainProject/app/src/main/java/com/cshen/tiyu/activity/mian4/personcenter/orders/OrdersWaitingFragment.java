package com.cshen.tiyu.activity.mian4.personcenter.orders;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.JCLQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.cai115.EL11TO5OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQOrderDetailActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.order.Order;
import com.cshen.tiyu.domain.order.OrderData;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


//待开奖
public class OrdersWaitingFragment extends Fragment {
	private Activity _this;
	private View nodata;
	private TextView touzhu;
	private RefreshListView refreshListView;

	private OrderListAdapter myListAdapter = null;
	private ArrayList<Order> orderLists;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String userId,userPwd;
	private ServiceUser httpService ;
	private boolean isGetting = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.total_fragment, container, false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}

	void initdata() {
		if(MyDbUtils.getCurrentUser()!=null){
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
			httpService = ServiceUser.getInstance();	
		}
		if(!isGetting){
			isGetting = true;	
			initAllOrdersForList();
		}
	}
	private void initAllOrdersForList() {
		more =0;
		httpService.PostOrder(_this,step+"", more+"", userId,userPwd,
				null,"NONE",null,ConstantsBase.SEARCH_TIME,new CallBack<OrderData>() {

			@Override
			public void onSuccess(OrderData t) {
				// TODO 自动生成的方法存根
				if(orderLists== null){
					orderLists = new ArrayList<Order>();
				}else{
					orderLists.clear();
				}
				orderLists.addAll(t.getMyScheme());
				if (orderLists.size()==0) {
					nodata.setVisibility(View.VISIBLE);
					refreshListView.setVisibility(View.GONE);
				}else{
					nodata.setVisibility(View.GONE);
					refreshListView.setVisibility(View.VISIBLE);
				}
				myListAdapter.setDate(orderLists);
				myListAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				nodata.setVisibility(View.VISIBLE);
				if(errorMessage!=null
						&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(_this,errorMessage.msg);
				}
				refreshListView.setVisibility(View.GONE);
				isGetting = false;
			}
		});
	}
	private void initView(View view) {
		nodata  =  view.findViewById(R.id.nodataview);
		touzhu = (TextView) view.findViewById(R.id.touzhu);
		touzhu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(_this, DLTMainActivity.class);	
				startActivity(intent);
			}
		});
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview2);
		myListAdapter = new OrderListAdapter(_this,2);
		refreshListView.setAdapter(myListAdapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if(position<0){
					return ;
				}
				Order order = orderLists.get(position);
				String lotteryType = Util.getLotteryType(order.getLotteryType());
				String playTypeStr = order.getZcPlayType();
				String playType = "";
				if("SFZC14".equals(playTypeStr)){
					playType = "0";
				}if("SFZC9".equals(playTypeStr)){
					playType = "1";
				}
				if (order!=null && order.getLotteryType()!=null  ) {
					Intent intent = null;
					if ((ConstantsBase.DLT+"").equals(lotteryType)  ) {
						intent  = new Intent(_this,DLTOrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if ((ConstantsBase.SD115+"").equals(lotteryType)||(ConstantsBase.GD115+"").equals(lotteryType)) {			
						intent  = new Intent(_this,EL11TO5OrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if((ConstantsBase.Fast3+"").equals(lotteryType)) {
						intent  = new Intent(_this,Fast3OrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if ((ConstantsBase.JCZQ+"").equals(lotteryType)) {
						intent  = new Intent(_this,JCZQOrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if ((ConstantsBase.JCLQ+"").equals(lotteryType)) {
						intent  = new Intent(_this,JCLQOrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if ((ConstantsBase.SFC+"").equals(lotteryType)) {
						intent  = new Intent(_this,SFCOrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
						intent.putExtra("playType", playType);
					}else if ((ConstantsBase.SSQ+"").equals(lotteryType)) {
						intent  = new Intent(_this,SSQOrderDetailActivity.class);
						intent.putExtra("schemeId", order.getSchemeId());
						intent.putExtra("onlyClose", true);
						intent.putExtra("lotteryId", lotteryType);
					}else if ((ConstantsBase.PL35+"").equals(lotteryType)) {
						ToastUtils.showShort(_this, "敬请期待...");
					}
					else{
						intent= new Intent(_this,LotteryTypeActivity.class);
						String url = ConstantsBase.IPQT + "/#0/account/lotteryOrderDetail/"
								+ lotteryType + "_" + order.getSchemeId();
						intent.putExtra("url", url);
					}
					if (null != intent) {
						startActivity(intent);
					}
				}
			}
		});
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

				if(!isGetting){
					isGetting = true;	
				}else{
					return;
				}
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					httpService.PostOrder(_this,step+"", start+"", userId,userPwd,
							null,"NONE",null,ConstantsBase.SEARCH_TIME,new CallBack<OrderData>() {

						@Override
						public void onSuccess(OrderData t) {
							// TODO 自动生成的方法存根
							ArrayList<Order> newlists = t.getMyScheme();// 加载数据结束
							if (newlists.size()==0) {
								ToastUtils.showShort(_this, "已无更多数据");
								more --;
							}
							orderLists.addAll(newlists);
							myListAdapter.notifyDataSetChanged();// 重新刷新数据
							refreshListView.onRefreshComplete(false);
							isGetting = false;
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							myListAdapter.notifyDataSetChanged();// 重新刷新数据
							refreshListView.onRefreshComplete(false);
							isGetting = false;
						}
					});

				} else {
					initAllOrdersForList();
				}
			}
		});
	}
}
