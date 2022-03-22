package com.cshen.tiyu.activity.mian4.find.shareorder;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.redpacket.RedPacketListAdapter;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.find.MyShareOrderBean;
import com.cshen.tiyu.domain.find.MyShareOrderBean.ThesunlistBean;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.net.https.ServiceActivity;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class MyShareOrderActivity  extends BaseActivity {
	private MyShareOrderActivity _this;
	private TextView nodata;
	private RefreshListView refreshListView;
	private MyShareOrderAdapter shareOrderAdapter = null;
	private ArrayList<ThesunlistBean> shareOrderDatas=new ArrayList<>();
	private TopViewLeft tv_head;//头
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.fragment_shareorder_hostest);
		_this=this;
		initView();// 初使化控件
		initdata(); // 获取数据
		
	}
	
	private void initView() {
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setVisibility(View.VISIBLE);
		tv_head.setResourceVisiable(true, false ,false);
		tv_head.setTitle("我的晒单圈");
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
			}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		nodata = (TextView)findViewById(R.id.tv_nodata);
		shareOrderAdapter = new MyShareOrderAdapter(this,0);
		refreshListView = (RefreshListView)findViewById(R.id.rl_listview);
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
				if (isLoadingMore) {
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
		});
	}

	private void initdata() {		
		initForList(0);
	}
	private void initForList(int start) {
	
		ServiceShareOrder.getInstance().PostGetMyShareOrder(_this,start,0,"", new CallBack<MyShareOrderBean>() {
			
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
			}
		});
	}


}
