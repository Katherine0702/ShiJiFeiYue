package com.cshen.tiyu.activity.mian4.gendan.myarena;


import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.MyFollowRecommend;
import com.cshen.tiyu.domain.gendan.MyFollowRecommendList;
import com.cshen.tiyu.domain.gendan.MyRecommendDetil;
import com.cshen.tiyu.domain.gendan.MyRecommendDetilList;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


public class MyRecommendDetilActivity extends BaseActivity{
	private RefreshListView refreshListView;
	private View nodata;
	private MyRecommendDetilActivity _this;
	private MyRecommendDetilAdapter adapter;
	private ArrayList<MyRecommendDetil> myrecommenddetillist;
	private boolean isGetting = false;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.myrecommenddetil);
		initView(); 
		initdata();
	}
	public void initView(){
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});

		nodata = findViewById(R.id.nodata);
		refreshListView = (RefreshListView) findViewById(R.id.listview);
		adapter = new MyRecommendDetilAdapter();
		refreshListView.setAdapter(adapter);
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
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					ServiceGenDan.getInstance().getMyRecommendDetil(_this,start,step,id,new CallBack<MyRecommendDetilList>() {

						@Override
						public void onSuccess(MyRecommendDetilList t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(adapter!=null){
								adapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
						}
					});
				} else {
					initAllOrdersForList();
				}

			}
		});
	}
	void initdata() {
		id = getIntent().getStringExtra("id");
		if(!isGetting&&MyDbUtils.getCurrentUser()!=null){			
			isGetting = true;
			initAllOrdersForList();
		}
	}
	public void initAllOrdersForList(){
		more = 0;
		ServiceGenDan.getInstance().getMyRecommendDetil(_this,more,step,id,
				new CallBack<MyRecommendDetilList>() {
			@Override
			public void onSuccess(MyRecommendDetilList t) {
				// TODO 自动生成的方法存根
				dodata(t,true);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public synchronized void dodata(MyRecommendDetilList t,boolean isRefresh){
		if(isRefresh){
			if(myrecommenddetillist== null){
				myrecommenddetillist = new ArrayList<MyRecommendDetil>();
			}else{
				myrecommenddetillist.clear();
			}
			myrecommenddetillist.addAll(t.getResultList());               
			if (myrecommenddetillist.size()==0) {
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}else{
				nodata.setVisibility(View.GONE);
				refreshListView.setVisibility(View.VISIBLE);
			}
			adapter.setData(myrecommenddetillist);
		}else{

			ArrayList<MyRecommendDetil> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(myrecommenddetillist== null){
				myrecommenddetillist = new ArrayList<MyRecommendDetil>();
			}
			myrecommenddetillist.addAll(newlists);
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
	}
	class MyRecommendDetilAdapter extends BaseAdapter {
		ArrayList<MyRecommendDetil> niurenList;
		public void setData(ArrayList<MyRecommendDetil> niurenlist) {
			niurenList = niurenlist;
		}
		@Override
		public int getCount() {
			if(niurenList == null){
				return 0;
			}else{
				return niurenList.size();
			}
		}

		@Override
		public MyRecommendDetil getItem(int position) {
			if(niurenList == null){
				return new MyRecommendDetil();
			}else{
				return (MyRecommendDetil)niurenList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			MyRecommendDetil mr = getItem(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.myrecommenddetil_item,null);
				holder.head = (ImageView) convertView.findViewById(R.id.head);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				holder.state = (TextView) convertView.findViewById(R.id.state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			xUtilsImageUtils.display(holder.head,R.mipmap.defaultniu,
					mr.getSponsorUserPic());
			holder.name.setText(mr.getSponsorName());
			holder.money.setText(mr.getSchemeCost());
			switch(mr.getSchemeStateInt()){
			case 0:
				holder.state.setText("待开奖");
				break;
			case 1:
				if(!TextUtils.isEmpty(mr.getPrizeAfterTax())){
					holder.state.setText(mr.getPrizeAfterTax());
				}else{
					holder.state.setText("未中奖");
				}
				break;
			case 2:
				holder.state.setText("未中奖");
				break;
			}

			return convertView;
		}
	}
	class ViewHolder {
		public ImageView head;
		public TextView name;
		public TextView money;
		public TextView state;

	}
}
