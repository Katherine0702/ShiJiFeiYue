package com.cshen.tiyu.activity.mian4.gendan.niuren;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.find.shareorder.ShareOrderAdapter;
import com.cshen.tiyu.activity.mian4.find.shareorder.ShareOrderAdapter.updatePraiseStatus;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.find.MyShareOrderBean;
import com.cshen.tiyu.domain.find.MyShareOrderBean.ThesunlistBean;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import de.greenrobot.event.EventBus;

public class ShaiDanFragment  extends Fragment {
	public static final int LOGIN_PRISE = 5;// 登录
	private Activity _this;
	private TextView nodata;
	private RefreshListView refreshListView;
	private ShareOrderAdapter shareOrderAdapter = null;
	private ArrayList<ThesunlistBean> shareOrderDatas=new ArrayList<>();
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private boolean isGetting = false;
	String id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shareorder_hostest, container,false);
		_this = this.getActivity();
		EventBus.getDefault().register(this);
		Bundle bundle = getArguments();
		id =  bundle.getString("id");//这里的values就是我们要传的值
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	public void initView(View view){
		view.findViewById(R.id.title).setVisibility(View.GONE);
		nodata = (TextView) view.findViewById(R.id.tv_nodata);
		shareOrderAdapter = new ShareOrderAdapter(_this, new updatePraiseStatus() {
			
			@Override
			public void updateDatas() {//最热列表点赞成功，更新最新列表数据点赞状态				
				EventBus.getDefault().post("updataNewest");
			}

			@Override
			public void toLoginActivity() {
				Intent intent = new Intent(_this, LoginActivity.class);
				intent.putExtra("requestName", "intentLogin");
				_this.startActivityForResult(intent, LOGIN_PRISE);
				
			}
		},0);
		refreshListView = (RefreshListView)view.findViewById(R.id.rl_listview);
		refreshListView.setAdapter(shareOrderAdapter);
		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				requestDataFromServer(false); // 冲服务器中获取刷新的数据
			}

			@Override
			public void onLoadMore() {
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				if(!isGetting){
					isGetting = true;
					if (isLoadingMore) {
						// TODO 自动生成的方法存根
						// 加载更多
						more++;
						if(shareOrderAdapter!=null){
							shareOrderAdapter.notifyDataSetChanged();// 重新刷新数据
						}
						initForList(more * step);
					} else {
						more= 0;
						if(shareOrderAdapter!=null){
							shareOrderDatas.clear();
						}
						if(shareOrderAdapter!=null){
							shareOrderAdapter.clearState();
							shareOrderAdapter.notifyDataSetChanged();// 重新刷新数据
						}
						initForList(more);
					}
				}

			}
		});
	}

	private void initdata() {	
		if(!isGetting){
			isGetting = true;
			initForList(0);
		}
	}
	private void initForList(int start) {

		ServiceShareOrder.getInstance().PostGetMyShareOrder(_this,start,1,id,new CallBack<MyShareOrderBean>() {

			@Override
			public void onSuccess(MyShareOrderBean t) {
				ArrayList<ThesunlistBean> newlists = t.getThesunlist();
				if(more > 0){
					if (newlists.size()==0) {
						ToastUtils.showShort(_this, "已无更多数据");
						more--;
						shareOrderAdapter.notifyDataSetChanged();
						refreshListView.onRefreshComplete(false);
						return;
					}
				}
				if(shareOrderDatas== null){
					shareOrderDatas = new ArrayList();
				}
				shareOrderDatas.addAll(newlists);	
				if(more == 0){
					if (shareOrderDatas.size()==0) {
						nodata.setVisibility(View.VISIBLE);
						refreshListView.setVisibility(View.GONE);
					}else{
						nodata.setVisibility(View.GONE);
						refreshListView.setVisibility(View.VISIBLE);
					}
					shareOrderAdapter.setDate(shareOrderDatas);
				}
				shareOrderAdapter.notifyDataSetChanged();
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				if(more > 0){
					more -- ;
				}
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(shareOrderAdapter!=null){
					shareOrderAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public void onEventMainThread(String event) {

		if (!TextUtils.isEmpty(event)) {
			if ("getDataShaiDan".equals(event)) {
				more= 0;
				if(shareOrderAdapter!=null){
					shareOrderDatas.clear();
				}
				if(shareOrderAdapter!=null){
					shareOrderAdapter.clearState();
					shareOrderAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				initForList(more);
			}
		}
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
}
