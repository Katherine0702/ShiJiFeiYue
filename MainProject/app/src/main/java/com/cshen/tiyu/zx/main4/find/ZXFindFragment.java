package com.cshen.tiyu.zx.main4.find;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.find.LiveScoresActivity;
import com.cshen.tiyu.activity.mian4.main.MessageAdapter;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.net.https.ServiceMessage;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.ImageCycleView;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ZXFindFragment extends Fragment implements com.cshen.tiyu.widget.RefreshListView.OnRefreshListener {
	private Activity _this;
	private RefreshListView mRefreshListView;
	private TextView tv_nodata;
	private MessageAdapter mAdapter;
	private ArrayList<NewslistBean> mDataList=new ArrayList<>();
	private int  newsType=5;
	private int  mType;//页面来源  1为要闻  2为社会 3为娱乐 4为科技 5为汽车
	//下拉加载
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private boolean isGetting = false;
	private TopViewLeft tv_head;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle sszl) {
		View view = inflater.inflate(R.layout.activity_message_list, container,false);
		_this = this.getActivity();
		mType = 14;
		initView(view);// 初使化控件
		initHead(view);
		initdata();
		
		return view;
	}
	public  void initdata() {	
		if(!isGetting){
			initForList(0);
			isGetting = true;
		}

	}
	private void initView(View view ) {
		tv_nodata=(TextView)view.findViewById(R.id.tv_nodata);
		mRefreshListView=(RefreshListView)view.findViewById(R.id.listview_message);
		mRefreshListView.setOnRefreshListener(this);
		mAdapter=new MessageAdapter(_this, mDataList);
		mRefreshListView.setAdapter(mAdapter);

		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2>=mDataList.size()) {
					return;
				}
				Intent intent = new Intent(_this, MessageDetailActivity.class);
				String id=mDataList.get(arg2).getId();
				intent.putExtra("id", id);
				intent.putExtra("newsType", newsType);
				intent.putExtra("dataBean", mDataList.get(arg2));
				startActivity(intent);
			}
		});
	}
	private void initHead(View view) {
		tv_head = (TopViewLeft) view.findViewById(R.id.tv_head);
		tv_head.setVisibility(View.GONE);

	}
	private void initForList(int start) {
		if (start==0) {
			mDataList.clear();
			more=0;
		}
		ServiceMessage.getInstance().PostGetHomeMessageData(_this, newsType,mType, start, step,false, new CallBack<NewsBean>() {

			@Override
			public void onSuccess(NewsBean t) {
				ArrayList<NewslistBean> newlists = t.getNewslist();
				if(more > 0){
					if (newlists.size()==0) {
						ToastUtils.showShort(_this, "已无更多数据");
						more--;
						mAdapter.notifyDataSetChanged();
						mRefreshListView.onRefreshComplete(false);
						return;
					}

				}


				mDataList.addAll(newlists);	
				if(more == 0){
					if (mDataList.size()==0) {
						tv_nodata.setVisibility(View.VISIBLE);
						//					mRefreshListView.setVisibility(View.GONE);
					}else{
						tv_nodata.setVisibility(View.GONE);
						mRefreshListView.setVisibility(View.VISIBLE);
					}
					//				mAdapter.setDate(mDataList);
				}
				mAdapter.notifyDataSetChanged();
				mRefreshListView.onRefreshComplete(false);
				isGetting=false;

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {

				if(more > 0){
					more -- ;
				}else {
					if (mDataList.size()==0) {
						tv_nodata.setVisibility(View.VISIBLE);
						//						mRefreshListView.setVisibility(View.GONE);
					}
				}

				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(mAdapter!=null){
					mAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				mRefreshListView.onRefreshComplete(false);
				isGetting=false;

			}
		});
	}
	@Override
	public void onRefresh() {
		requestDataFromServer(false); 
	}

	@Override
	public void onLoadMore() {

		requestDataFromServer(true);

	}

	private void requestDataFromServer(boolean isLoadingMore) {
		if(!isGetting){
			isGetting = true;
		}
		if (isLoadingMore) {
			// TODO 自动生成的方法存根
			// 加载更多
			more++;
			if(mAdapter!=null){
				mAdapter.notifyDataSetChanged();// 重新刷新数据
			}
			initForList(more * step);
		} else {
			more= 0;
			if(mAdapter!=null){
				mDataList.clear();
			}
			if(mAdapter!=null){
				mAdapter.notifyDataSetChanged();// 重新刷新数据
			}
			initForList(more);
		}

	}	
}
