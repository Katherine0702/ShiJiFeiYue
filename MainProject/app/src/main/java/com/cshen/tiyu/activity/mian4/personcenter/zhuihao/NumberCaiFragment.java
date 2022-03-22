package com.cshen.tiyu.activity.mian4.personcenter.zhuihao;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.zhuihao.AddOrder;
import com.cshen.tiyu.domain.zhuihao.AddOrderList;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class NumberCaiFragment extends Fragment {
	private Activity _this;
	private View nodata;
	private RefreshListView refreshListView;
	private AddOrderListAdapter numberAdapter = null;
	private ArrayList<AddOrder> numberLists;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ServiceCaiZhongInformation caizhongService ;
	private TextView toBuy;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.addlist, container,false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO 自动生成的方法存根
		if (!hidden) {
			initdata(); // 获取数据
		}
		super.onHiddenChanged(hidden);
	}
	private void initView(View view) {
		nodata = view.findViewById(R.id.nodataview);
		toBuy = (TextView) view.findViewById(R.id.buynow);
		toBuy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				boolean hasSSQ = false;
				LotteryTypeData currentLotteryTypeData = MyDbUtils.getCurrentLotteryTypeData();
				if (currentLotteryTypeData != null) {
					ArrayList<LotteryType>	lotteryTypes = currentLotteryTypeData.getLotteryList();
					for(LotteryType lt:lotteryTypes){
						if((ConstantsBase.SSQ+"").equals(lt.getLotteryId())){
							hasSSQ = true;
							break;
						}
					}
				}
				if(hasSSQ){
					intent = new Intent(_this, SSQMainActivity.class);		
				}else{
					intent = new Intent(_this,MainActivity.class);
					intent.putExtra("hasLogin","yes");
					intent.putExtra("nowPage","1");
				}
				startActivity(intent);
				_this.finish();
			}
		});
		numberAdapter = new AddOrderListAdapter(_this);
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		refreshListView.setAdapter(numberAdapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(_this,AddOrderDetailActivity.class);
				intent.putExtra("lotteryId", numberLists.get(position).getLotteryId());
				intent.putExtra("playType", numberLists.get(position).getPlayType());
				intent.putExtra("chaseId", numberLists.get(position).getChaseId());
				DecimalFormat df = new DecimalFormat("0.00");
				intent.putExtra("totalAccount", df.format(numberLists.get(position).getTotalCost()));
				startActivity(intent);
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
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					if(numberAdapter!=null){
						numberAdapter.notifyDataSetChanged();// 重新刷新数据
					}
					initForList(more * step);
				} else {
					more= 0;
					if(numberLists!=null){
						numberLists.clear();
					}
					if(numberAdapter!=null){
						numberAdapter.notifyDataSetChanged();// 重新刷新数据
					}
					initForList(more);
				}

			}
		});
	}

	private void initdata() {		
		caizhongService = ServiceCaiZhongInformation.getInstance();
		initForList(0);
	}

	private void initForList(int start) {
		caizhongService.getAddOrder(_this,step+"",start+"",
				"number",new CallBack<AddOrderList>() {

			@Override
			public void onSuccess(AddOrderList t) {
				// TODO 自动生成的方法存根
				ArrayList<AddOrder> newlists = t.getNumberChanse();
				if(more > 0){
					if (newlists.size()==0) {
						ToastUtils.showShort(_this, "已无更多数据");
						more--;
						numberAdapter.notifyDataSetChanged();
						refreshListView.onRefreshComplete(false);
						return;
					}
				}
				if(numberLists== null){
					numberLists = new ArrayList<AddOrder>();
				}
				numberLists.addAll(newlists);	
				if(more == 0){
					if (numberLists.size()==0) {
						nodata.setVisibility(View.VISIBLE);
						refreshListView.setVisibility(View.GONE);
					}else{
						nodata.setVisibility(View.GONE);
						refreshListView.setVisibility(View.VISIBLE);
					}
					numberAdapter.setDate(numberLists);
				}
				numberAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete(false);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				if(more > 0){
					more -- ;
				}
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(numberAdapter!=null){
					numberAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
			}
		});
	}
}
