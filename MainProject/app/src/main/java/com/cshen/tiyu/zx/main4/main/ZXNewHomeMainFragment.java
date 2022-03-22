package com.cshen.tiyu.zx.main4.main;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.main.MessageAdapter;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.ImageCycleView;
import com.cshen.tiyu.widget.ImageCycleView.ImageCycleViewListener;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;
import com.cshen.tiyu.zx.main4.ZXNewsListActivtity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ZXNewHomeMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {

	private VerticalSwipeRefreshLayout mSwipeLayout;
	private ScrollView scrollview;
	private View view;
	// 轮播图
	private ImageCycleView mAdView;
	// 资讯
	private MyListView listview_message;// 展示最新三条资讯的列表
	private MessageAdapter mMessageAdapter;
	private ArrayList<NewslistBean> newsDatas = new ArrayList<>();
	private LinearLayout ll_chinaFootball,ll_internalFootball,ll_cba,ll_nba;
	private String news_id;// 消息id
	//轮播图资源
	private ArrayList<String> mImageUrl=new ArrayList<>();
	private ArrayList<String> mImageTitle = new ArrayList<>(); 
	private boolean isGetting = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_new_mainhome, container, false);
			initView(view);
		}
		initData(false);
		return view;
	}

	private void initView(View view) {
		mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);

		scrollview = (ScrollView) view.findViewById(R.id.scrollview);
		scrollview.getViewTreeObserver().addOnScrollChangedListener(
				new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						mSwipeLayout.setEnabled(scrollview.getScrollY() == 0);
					}
				});
		scrollview.smoothScrollTo(0, 20);
		// 轮播
		mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
		setRecycleAds();
		//////////////////////////
		ll_chinaFootball= (LinearLayout) view.findViewById(R.id.ll_chinaFootball) ;
		ll_internalFootball= (LinearLayout) view.findViewById(R.id.ll_internalFootball) ;
		ll_cba= (LinearLayout) view.findViewById(R.id.ll_cba) ;
		ll_nba= (LinearLayout) view.findViewById(R.id.ll_nba) ;
		ll_chinaFootball.setOnClickListener(this);
		ll_internalFootball.setOnClickListener(this);
		ll_cba.setOnClickListener(this);
		ll_nba.setOnClickListener(this);

		// 消息通知		
		mMessageAdapter = new MessageAdapter(getActivity(), newsDatas);
		listview_message = (MyListView) view.findViewById(R.id.lv_home_news);
		listview_message.setAdapter(mMessageAdapter);
		listview_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						MessageDetailActivity.class);
				String id=newsDatas.get(arg2).getId();
				intent.putExtra("id", id);		
				intent.putExtra("dataBean", newsDatas.get(arg2));
				intent.putExtra("isMaJia", true);
				intent.putExtra("newsType", 2);
				startActivity(intent);
			}
		});

	}
	
	public void setRecycleAds(){
		/******************************************/
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;//屏幕宽度

		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams)mAdView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;
		// 控件的高强制设成20  
		linearParams.width = screenW;// 控件的宽强制设成30   
		linearParams.height = (int) ((int)screenW/2.16);// 控件的宽强制设成30   
		mAdView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
		/******************************************/
		mImageUrl.clear();
		mImageTitle.clear();
		//添加头部	
		mImageUrl.add(R.mipmap.zx_banner3+"");
		mImageUrl.add(R.mipmap.zx_banner2+"");
		mImageUrl.add(R.mipmap.zx_banner1+"");
		mImageTitle.add("");
		mImageTitle.add("");
		mImageTitle.add("");
		mAdView.setImageResources(mImageUrl, mImageTitle,mAdCycleViewListener, 2,true);			
	}
	private void initData(boolean isRefresh) {
		// 一些不涉及到传参数都请求都在预加载界面缓存到本地了
		if (!NetWorkUtil.isNetworkAvailable(this.getActivity())) {
			ToastUtils.showShort(this.getActivity(), "当前网络信号较差，请检查网络设置");
		}
		// /资讯
		initMessageData();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(final int position, View imageView) {
//			String url="";
//			if (position==2) {
//			url="http://m.xinhuanet.com/sports/2017-10/27/c_1121863326.htm";
//			}else if (position==1) {
//				url="http://m.xinhuanet.com/sports/2017-11/02/c_1121893112.htm";
//			}else if (position==0) {
//				url="http://m.xinhuanet.com/sports/2017-11/01/c_1121884603.htm";
//			}
//			 Intent intent = new Intent(getActivity(), InformationDetailActivity.class);
//			 intent.putExtra("isBanner", true);
//			 intent.putExtra("url", url);
//			 intent.putExtra("newsType", 2);
//			
//             startActivity(intent);
		}

	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initData(true);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 停止刷新
				mSwipeLayout.setRefreshing(false);
			}
		}, 3000); // 5秒后发送消息，停止刷新
	}

	/**
	 * 热点资讯
	 */
	private void initMessageData() {
	
		ServiceMain.getInstance().PostGetZXHomeMessageData(getActivity(), new CallBack<NewsBean>() {
			
			@Override
			public void onSuccess(NewsBean t) {
				newsDatas.clear();
				newsDatas.addAll(t.getNewslist());
				mMessageAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				
			}
		});
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_chinaFootball://中国足球
			toNewsList(7);
			break;
		case R.id.ll_internalFootball://
			toNewsList(4);
			break;
		case R.id.ll_nba:
			toNewsList(9);
			break;
		case R.id.ll_cba:
			toNewsList(5);
			break;
		}
	}

	public void toNewsList(int mType){
		Intent intent = new Intent(getActivity(),ZXNewsListActivtity.class);
		intent.putExtra("mType", mType);
		startActivity(intent);
	}

}
