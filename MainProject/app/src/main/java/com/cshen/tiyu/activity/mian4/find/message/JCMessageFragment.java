package com.cshen.tiyu.activity.mian4.find.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.main.MessageAdapter;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.net.https.ServiceActivity;
import com.cshen.tiyu.net.https.ServiceMessage;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class JCMessageFragment extends Fragment implements OnRefreshListener{
	private View view;
	private TextView tv_nodata;
	private MessageAdapter mAdapter;
	private ArrayList<NewslistBean> mDataList=new ArrayList<>();
	private RefreshListView mRefreshListView;
	//下拉加载
	private int pageIndex = 0;// 第几次更多加载
	private int pageSize = 10;// 每次步数
	private boolean isNoMoreData = false;
	private int newsType=2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_message_list, container,
				false);
		initViews();
		getMessageData(pageIndex);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO 自动生成的方法存根
		if (!hidden) {
			getMessageData(pageIndex);
		}
		super.onHiddenChanged(hidden);
	}

	private void initViews() {
		tv_nodata = (TextView)view. findViewById(R.id.tv_nodata);
		mRefreshListView = (RefreshListView)view. findViewById(R.id.listview_message);
		mRefreshListView.setOnRefreshListener(this);
		mAdapter = new MessageAdapter(getActivity(), mDataList);
		mRefreshListView.setAdapter(mAdapter);

		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 >= mDataList.size()) {
					return;
				}
				Intent intent = new Intent(getActivity(),
						MessageDetailActivity.class);
				String id = mDataList.get(arg2).getId();
				intent.putExtra("id", id);
				intent.putExtra("newsType", newsType);

				startActivity(intent);
			}
		});
	}
	
private void getMessageData(int start) {
		
		ServiceMessage.getInstance().PostGetHomeMessageData(getActivity(), newsType, start, pageSize,false, new CallBack<NewsBean>() {
			
			@Override
			public void onSuccess(NewsBean t) {
			ArrayList<NewslistBean> newlists = t.getNewslist();
			if(pageIndex > 0){
				if (newlists.size()==0) {
					ToastUtils.showShort(getActivity(), "已无更多数据");
					pageIndex--;
					mAdapter.notifyDataSetChanged();
					mRefreshListView.onRefreshComplete(false);
					return;
				}
				
			}
			if (newlists.size()<pageSize) {
				isNoMoreData=true;
			}
			mDataList.addAll(newlists);	
			if(pageIndex == 0){
				if (mDataList.size()==0) {
					tv_nodata.setVisibility(View.VISIBLE);
					mRefreshListView.setVisibility(View.GONE);
				}else{
					tv_nodata.setVisibility(View.GONE);
					mRefreshListView.setVisibility(View.VISIBLE);
				}
//				mAdapter.setDate(mDataList);
			}
			mAdapter.notifyDataSetChanged();
			mRefreshListView.onRefreshComplete(false);
				
			}
			
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				
				if(pageIndex > 0){
					pageIndex -- ;
				}else {
					if (mDataList.size()==0) {
						tv_nodata.setVisibility(View.VISIBLE);
						mRefreshListView.setVisibility(View.GONE);
					}
				}
				
				ToastUtils.showShort(getActivity(),errorMessage.msg+"");
				if(mAdapter!=null){
					mAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				mRefreshListView.onRefreshComplete(false);
				
			}
		});
	}
	
	
	
	private void requestDataFromServer(boolean isLoadingMore) {
		if (isLoadingMore) {
			// 加载更多
			pageIndex++;
			if(mAdapter!=null){
				mAdapter.notifyDataSetChanged();// 重新刷新数据
			}
			getMessageData(pageIndex * pageSize);
		} else {
			pageIndex= 0;
			isNoMoreData=false;
			if(mDataList!=null){
				mDataList.clear();
			}
			if(mAdapter!=null){
				mAdapter.notifyDataSetChanged();// 重新刷新数据
			}
			getMessageData(pageIndex);
		}

	}
	@Override
	public void onRefresh() {
		requestDataFromServer(false); 
	}

	@Override
	public void onLoadMore() {
		if (!isNoMoreData) {
			requestDataFromServer(true);
		}else {
			ToastUtils.showShort(getActivity(), "已无更多数据");
			mRefreshListView.onRefreshComplete(false);
		}
		
	}
}
