package com.cshen.tiyu.activity.mian4.gendan.bangdan;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.gendan.FollowDetilActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.RenZhengPerson;
import com.cshen.tiyu.domain.gendan.RenZhengPersonList;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class YingLiFragment  extends Fragment {
	private Activity _this;
	private RefreshListView refreshListView;

	private BangDanListAdapter myListAdapter = null;
	private ArrayList<RenZhengPerson> renzhenglist;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private boolean isGetting = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mingzhong_fragment, container,false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	private void initView(View view) {		
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		myListAdapter = new BangDanListAdapter(_this,1);
		refreshListView.setAdapter(myListAdapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if(renzhenglist!=null&&position<renzhenglist.size()&&position>=0){
					Intent intent = new Intent(_this,FollowDetilActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("match", renzhenglist.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
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

			private   void  requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if(isGetting){
					return;
				}				
				if (isLoadingMore) {
					if(!isGetting){
						isGetting = true;
					}
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					ServiceGenDan.getInstance().pastRenZhengList(_this,start,step,1,1,BangDanMainFragment.YINGLI,
							new CallBack<RenZhengPersonList>() {

						@Override
						public void onSuccess(RenZhengPersonList t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(myListAdapter!=null){
								myListAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
							isGetting = false;
						}
					});
				} else {
					initdata();
				}

			}
		});
	}

	void initdata() {
		EventBus.getDefault().post("toGetAds");
		if(!isGetting){
			isGetting = true;
			getRenZhengNiuRen();
		}
	}

	private void getRenZhengNiuRen() {
		more = 0;
		ServiceGenDan.getInstance().pastRenZhengList(_this,more,step,1,1,BangDanMainFragment.YINGLI,
				new CallBack<RenZhengPersonList>() {
			@Override
			public void onSuccess(RenZhengPersonList t) {
				// TODO 自动生成的方法存根
				dodata(t,true);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(RenZhengPersonList t,boolean isRefresh){
		if(isRefresh){
			if(renzhenglist== null){
				renzhenglist = new ArrayList<RenZhengPerson>();
			}else{
				renzhenglist.clear();
			}
			if(t.getResultList()!=null&& t.getResultList().size()>0){
				renzhenglist.addAll(t.getResultList());  
				myListAdapter.setDate(renzhenglist);
			}
		}else{
			ArrayList<RenZhengPerson> newlists = t.getResultList();
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(renzhenglist== null){
				renzhenglist = new ArrayList<RenZhengPerson>();
			}
			renzhenglist.addAll(newlists);
		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
		isGetting = false;
	}

}
