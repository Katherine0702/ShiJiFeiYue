package com.cshen.tiyu.activity.mian4.main;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.RenZhengPerson;
import com.cshen.tiyu.domain.gendan.RenZhengPersonList;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.ServiceMessage;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
/**
 * 资讯
 * @author Administrator
 *
 */
public class MessageListActivity extends BaseActivity implements OnRefreshListener{
	private Context mContext;
	private TopViewLeft tv_head;
	private RefreshListView mRefreshListView;
	private TextView tv_nodata;
	private MessageAdapter mAdapter;
	private ArrayList<NewslistBean> mDataList=new ArrayList<>();
	private int  newsType;//页面来源 1:公告 2：资讯
	//下拉加载
	private int pageIndex = 0;// 第几次更多加载
	private int pageSize = 10;// 每次步数
	private boolean isNoMoreData = false;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_message_list);
		newsType=getIntent().getIntExtra("newsType",1);
		mContext=this;
		initHead();
		initViews();
		getMessageData(pageIndex);
	}
	
	private void initViews() {
		
		tv_nodata=(TextView) findViewById(R.id.tv_nodata);
		mRefreshListView=(RefreshListView) findViewById(R.id.listview_message);
		mRefreshListView.setOnRefreshListener(this);
		mAdapter=new MessageAdapter(mContext, mDataList);
		mRefreshListView.setAdapter(mAdapter);
		
		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2>=mDataList.size()) {
					return;
				}
				 Intent intent = new Intent(mContext, MessageDetailActivity.class);
				 String id=mDataList.get(arg2).getId();
				 intent.putExtra("id", id);
				 intent.putExtra("newsType", newsType);
				
                 startActivity(intent);
			}
		});
		
	}
	private void initHead() {
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
         
		tv_head.setResourceVisiable(true, false, false);
		if (newsType==1) {
			tv_head.setTitle("公司公告");
			tv_head.setVisibility(View.VISIBLE);
		}else {
			tv_head.setTitle("竞彩资讯");
			tv_head.setVisibility(View.VISIBLE);
		}
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickBackImage(View view) {
				// TODO 自动生成的方法存根
				finish();
			}
		});

	}
	
	private void getMessageData(int start) {
		
		ServiceMessage.getInstance().PostGetHomeMessageData(mContext, newsType, start, pageSize,false, new CallBack<NewsBean>() {
			
			@Override
			public void onSuccess(NewsBean t) {
			ArrayList<NewslistBean> newlists = t.getNewslist();
			if(pageIndex > 0){
				if (newlists.size()==0) {
					ToastUtils.showShort(mContext, "已无更多数据");
					pageIndex--;
					mAdapter.notifyDataSetChanged();
					mRefreshListView.onRefreshComplete(false);
					return;
				}
				if (newlists.size()<pageSize) {
					isNoMoreData=true;
				}
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
				
				ToastUtils.showShort(mContext,errorMessage.msg+"");
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
			ToastUtils.showShort(mContext, "已无更多数据");
			mRefreshListView.onRefreshComplete(false);
		}
		
	}
}
