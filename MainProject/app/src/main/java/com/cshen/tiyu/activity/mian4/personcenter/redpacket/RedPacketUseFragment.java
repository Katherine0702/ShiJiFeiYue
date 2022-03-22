package com.cshen.tiyu.activity.mian4.personcenter.redpacket;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.net.https.ServiceActivity;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class RedPacketUseFragment  extends Fragment {
	private Activity _this;
	private View nodata;
	private RefreshListView refreshListView;
	private RedPacketListAdapter redPacketAdapter = null;
	private ArrayList<RedPacket> redPacketLists;
	private TextView noRedPacket_tv,toGetRedPacket;
	
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private ServiceActivity caizhongService ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_red_packet_item, container,false);
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
		noRedPacket_tv = (TextView) view.findViewById(R.id.noredpacket_tv);
		toGetRedPacket = (TextView) view.findViewById(R.id.togetredpacket);
		toGetRedPacket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		redPacketAdapter = new RedPacketListAdapter(_this,0);
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		refreshListView.setAdapter(redPacketAdapter);
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
					if(redPacketAdapter!=null){
						redPacketAdapter.notifyDataSetChanged();// 重新刷新数据
					}
					initForList(more * step);
				} else {
					more= 0;
					if(redPacketLists!=null){
						redPacketLists.clear();
					}
					if(redPacketAdapter!=null){
						redPacketAdapter.notifyDataSetChanged();// 重新刷新数据
					}
					initForList(more);
				}

			}
		});
	}

	private void initdata() {		
		noRedPacket_tv.setText("您还没有红包哦~");
		caizhongService = ServiceActivity.getInstance();
		initForList(0);
	}

	private void initForList(int start) {
		caizhongService.getRedPacketOrder(_this,step+"",start+"",
				"2",new CallBack<RedPacketData>() {

			@Override
			public void onSuccess(RedPacketData t) {
				// TODO 自动生成的方法存根
				ArrayList<RedPacket> newlists = t.getResult();
				if(more > 0){
					if (newlists.size()==0) {
						ToastUtils.showShort(_this, "已无更多数据");
						more--;
						redPacketAdapter.notifyDataSetChanged();
						refreshListView.onRefreshComplete(false);
						return;
					}
				}
				if(redPacketLists== null){
					redPacketLists = new ArrayList<RedPacket>();
				}
				redPacketLists.addAll(newlists);	
				if(more == 0){
					if (redPacketLists.size()==0) {
						nodata.setVisibility(View.VISIBLE);
						refreshListView.setVisibility(View.GONE);
					}else{
						nodata.setVisibility(View.GONE);
						refreshListView.setVisibility(View.VISIBLE);
					}
					redPacketAdapter.setDate(redPacketLists);
				}
				redPacketAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete(false);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				if(more > 0){
					more -- ;
				}
				
				
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(redPacketAdapter!=null){
					redPacketAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
			}
		});
	}
}
