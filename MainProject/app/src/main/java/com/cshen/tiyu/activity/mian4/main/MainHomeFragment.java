package com.cshen.tiyu.activity.mian4.main;

import java.util.ArrayList;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.InformationDetailActivity;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.login.RegisterByMoibleActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3AccountListActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQUtil;
import com.cshen.tiyu.activity.lottery.ball.util.PassType2;
import com.cshen.tiyu.activity.lottery.cai115.Account115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Accountgd115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.lottery.dlt.DLTAccountListActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.pl35.Pl5MainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQAccountListActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.activity.mian4.find.LatestLotteryInfoActivity;
import com.cshen.tiyu.activity.mian4.find.LiveScoresActivity;
import com.cshen.tiyu.activity.mian4.personcenter.redpacket.RedPacketActivity;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.pay.PayMoneyActivity;
import com.cshen.tiyu.activity.redpacket.RedPacketRainActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.activity.ActivityEach;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.FootBallMatchList;
import com.cshen.tiyu.domain.ball.JczqChoosedScroeBean;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.domain.information.Information;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.main.HomeAdsData;
import com.cshen.tiyu.domain.main.HomeAdsInfo;
import com.cshen.tiyu.domain.main.HomePpwBean;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServiceMessage;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.OpenOrderPageUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.CommonPPW;
import com.cshen.tiyu.widget.FootballLinearLayout;
import com.cshen.tiyu.widget.FootballLinearLayout.onBetClickListener;
import com.cshen.tiyu.widget.ImageCycleView;
import com.cshen.tiyu.widget.ImageCycleView.ImageCycleViewListener;
import com.cshen.tiyu.widget.LooperTextView;
import com.cshen.tiyu.widget.LotteryTypeView;
import com.cshen.tiyu.widget.LotteryTypeView.LotteryTypeListener;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.PlayingMethodView;
import com.cshen.tiyu.widget.PlayingMethodView.CathecticListener;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class MainHomeFragment extends Fragment implements OnItemClickListener,
SwipeRefreshLayout.OnRefreshListener, OnClickListener {
	public static final int LOGIN = 1;
	public static final int TOPERSONAL= 0;
	private final String mPageName = "MainHomeFragment";
	private VerticalSwipeRefreshLayout mSwipeLayout;
	private ScrollView scrollview;
	private View view;
	// 轮播�?
	private  View  mAdViewview;
	private ImageCycleView mAdView,mAdView2;
	private ArrayList<String> mImageUrl = new ArrayList<>();
	private ArrayList<String> mImageTitle = new ArrayList<>();
	// 消息通知
	private RelativeLayout rl_news;
	private LooperTextView looperview;
	private TextView looperview1;
	// 足球赛事
	private FootballLinearLayout ll_football;
	// 大乐透专属幸运号
	private PlayingMethodView playingMethodView;
	//	private View signicon;
	// 活动
	private ImageView activity1, activity2;
	// 热门彩种
	private LotteryTypeView mLotteryTypeView;
	private HomeAdsData homeInfos = null;
	private HomeAdsData homeInfos2 = null;
	private ArrayList<HomeAdsInfo> adsList = null;
	private ArrayList<HomeAdsInfo> adsList2 = null;
	private FootBallMatch fm = new FootBallMatch();
	private ArrayList<Information> informations = null;
	private ActivityEach ae0, ae1;
	private ArrayList<LotteryType> lotteryTypes = null;
	boolean temp = false;
	private Period period = null;// 为投注做铺垫必须先拿�?	
	public int stype = 2;
	private String news_id;// 消息id
	private String otherPPWType = "";
	//中奖通知订单信息
	private boolean isShowOtherPPW=false;//是否显示其他弹窗
	private HomePpwBean mhomePpwBean ;
	private CommonPPW otherPPW;//其他弹窗


	// 资讯
	private View view_message;
	private View iv_message_jczx;// 竞彩资讯
	private View iv_message_latestLottery;// 最新开奖
	private View iv_message_componyNews;// 公司公告
	private MyListView listview_message;// 展示最新三条资讯的列表
	private MessageAdapter mMessageAdapter;
	private ArrayList<NewslistBean> newsDatas = new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);

		if (view == null) {
			view = inflater.inflate(R.layout.home_fragment, container, false);
			initView(view);
		}
		initData(false);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!PreferenceUtil.getBoolean(getActivity(),
				"hasLogin")){
			isShowOtherPPW = false;
		}
		if (isShowOtherPPW&&!hidden) {
			showSomePPW(otherPPWType);
		}
	}

	private void initView(View view) {
		mSwipeLayout = (VerticalSwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		// 轮播
		mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
		/******************************************/
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;	
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams)mAdView.getLayoutParams();
		linearParams.width = screenW;//   
		linearParams.height = (int) ((int)screenW/2.16);  
		mAdView.setLayoutParams(linearParams);
		/******************************************/
		/*mImageUrl.clear();
		mImageTitle.clear();
		//添加头部
		mImageUrl.add(R.mipmap.zhuce+"");
		mImageUrl.add(R.mipmap.chongzhi+"");
		mImageTitle.add("http://an.caiwa188.com/3caijin.html");
		mImageTitle.add("http://an.caiwa188.com/100hongbao.html ");
		mAdView.setImageResources(mImageUrl, mImageTitle,mAdCycleViewListener2, 2,true);*/
		/******************************************/
		// 轮播
		mAdViewview = view.findViewById(R.id.ad_viewview);
		mAdView2 = (ImageCycleView) view.findViewById(R.id.ad_view2);
		/******************************************/
		LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) mAdView2.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;
		// 控件的高强制设成20
		screenW = screenW-50;
		linearParams2.width = screenW ;// 控件的宽强制设成30
		linearParams2.height = (int) ((int) screenW / 5.4);// 控件的宽强制设成30
		mAdView2.setLayoutParams(linearParams2); //使设置好的布局参数应用到控件
		/******************************************/
		rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);
		looperview = (LooperTextView) view.findViewById(R.id.looperview);
		looperview1 = (TextView) view.findViewById(R.id.looperview1);
		looperview1.setOnClickListener(this);
		looperview1.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
		looperview1.setMarqueeRepeatLimit(-1);	// "marquee_forever"
		looperview1.setFocusable(true);
		looperview1.setFocusableInTouchMode(true);
		looperview1.setSelected(true);

		// 足球赛事
		ll_football = (FootballLinearLayout)view.findViewById(R.id.football);
		ll_football.setonBetClickListener(new onBetClickListener() {

			@Override
			public void onBetClick(int moneyTouzhu) {// 投注
				if (0 != moneyTouzhu) {
					toPayFootBall(moneyTouzhu);
				} else {
					ToastUtils.showShort(getActivity(), "至少选择一项");
				}
			}

			@Override
			public void onMoreMatchClick() {// 更多赛事
				Intent intent = new Intent(getActivity(),FootballMainActivity.class);
				startActivity(intent);
			}
		});

		// 大乐透专�?		
		mLotteryTypeView = (LotteryTypeView) view.findViewById(R.id.lt_lotterytype);
		scrollview = (ScrollView) view.findViewById(R.id.scrollview);
		scrollview.getViewTreeObserver().addOnScrollChangedListener(
				new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						mSwipeLayout.setEnabled(scrollview.getScrollY() == 0);
					}
				});
		scrollview.smoothScrollTo(0, 20);
		// 活动
		activity1 = (ImageView) view.findViewById(R.id.lt_activity1);
		activity2 = (ImageView) view.findViewById(R.id.lt_activity2);
		// 热门彩种
		playingMethodView = (PlayingMethodView) view
				.findViewById(R.id.pm_playingmethod);
		playingMethodView.setCathecticListener(cathecticListener);

		// 资讯
		view_message = view.findViewById(R.id.view_news);
		iv_message_jczx =  view_message.findViewById(R.id.iv_news_jczx);
		iv_message_latestLottery = view_message.findViewById(R.id.iv_news_latestLottery);
		iv_message_componyNews = view_message.findViewById(R.id.iv_news_componyNews);
		listview_message = (MyListView) view_message.findViewById(R.id.lv_home_news);

		iv_message_jczx.setOnClickListener(this);
		iv_message_latestLottery.setOnClickListener(this);
		iv_message_componyNews.setOnClickListener(this);

		mMessageAdapter = new MessageAdapter(getActivity(), newsDatas);
		listview_message.setAdapter(mMessageAdapter);
		listview_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),InformationDetailActivity.class);
				String id=newsDatas.get(arg2).getId();
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
	}

	private void initData(boolean isRefresh) {
		// 一些不涉及到传参数都请求都在预加载界面缓存到本地了
		if (!NetWorkUtil.isNetworkAvailable(this.getActivity())) {
			ToastUtils.showShort(this.getActivity(), "当前网络信号较差，请检查网络设置");
		}
		if (!isRefresh) {
			initFiveLeague();//判断五大联赛
			initShowPPW(false);
		}
		initDataForCathectic();
		initHomeAds(isRefresh);// 广告
		initHomeAds2(isRefresh);// 广告
		initNewsData();// 公告
		InitIsShowingDLTLayout();// 判断是否显示足球赛事和大乐�?

		initFootBall();//获取足球赛事数据
		initLotteryType(isRefresh);// 彩种

		// /资讯
		initMessageData();
	} 

	private void initShowPPW(final boolean isFromLogin) {

		if (!PreferenceUtil.getBoolean(getActivity(), "isNotNewUser")) {//新用户
			showSomePPW("register");
			PreferenceUtil.putBoolean(getActivity(), "isNotNewUser", true);
		}else {
			if (!PreferenceUtil.getBoolean(getActivity(), "hasLogin")) {
				return;
			}

			ServiceMain.getInstance().getHomePPWInfo(getActivity(), new CallBack<HomePpwBean>() {

				@Override
				public void onSuccess(HomePpwBean homePpwBean) {
					mhomePpwBean = homePpwBean;
					otherPPWType= "";
					if ("1".equals(homePpwBean.getPushWin())) {//弹中奖提示 ，优先级：中奖>充值>回归
						otherPPWType = "win";
					}else if ("1".equals(homePpwBean.getPushPay())) {//充值
						otherPPWType = "recharge";
					}else if ("1".equals(homePpwBean.getPushBack())) {//回归
						otherPPWType = "returngift";
					}

					if (otherPPWType.equals("win")||otherPPWType.equals("recharge")||otherPPWType.equals("returngift")) {
						if (!isFromLogin&&isVisible()) {//当前页面可见
							showSomePPW(otherPPWType);
						}else {//当前页面不可见, 等待页面可见时弹出
							isShowOtherPPW=true;
						}
					}

				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					System.out.println("==>>msg"+errorMessage.msg);
					//					ToastUtils.showShort(getActivity(), errorMessage.msg);
				}
			});


		}

	}

	private void initFiveLeague() {
		boolean hasLongin = PreferenceUtil.getBoolean(getActivity(),
				"hasLogin");
		if (hasLongin) {//已经登录
			getFiveLeagueData(false);

		}

	}

	public void getFiveLeagueData(final boolean isFromRegister){
		ServiceMain.getInstance().PostFiveLeagueAcivity(getActivity(), new CallBack<String>() {

			@Override
			public void onSuccess(String result) {
				if (result!=null&&result.equals("3")) {
					otherPPWType="fiveleague";
					if (isFromRegister) {//from注册页面
						isShowOtherPPW = true;
					}else {
						if (isVisible()) {//当前页面可见
							showSomePPW(otherPPWType);
						}else {//当前页面不可见, 等待页面可见时弹出
							isShowOtherPPW = true;
						}
					}
				}else {
					isShowOtherPPW = false ;
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				//				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});
	}
	private void initHomeAds(boolean isRefresh) {
		if (!isRefresh) {
			HomeAdsData homeAdsData = MyDbUtils.getHomeAdsData();
			if (homeAdsData != null) {
				homeInfos = homeAdsData;
				setAds();
			}
			return;
		}
		ServiceMain.getInstance().PostGetHomeAdsData(getActivity(),
				new CallBack<HomeAdsData>() {

			@Override
			public void onSuccess(HomeAdsData t) {
				homeInfos = t;
				setAds();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存�?						
				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});
	}
	private void initHomeAds2(boolean isRefresh) {
		ServiceMain.getInstance().PostGetHomeAdsData2(getActivity(),
				new CallBack<HomeAdsData>() {

					@Override
					public void onSuccess(HomeAdsData t) {
						homeInfos2 = t;
						setAds2();
					}

					@Override
					public void onFailure(ErrorMsg errorMessage) {
						// TODO 自动生成的方法存根
						ToastUtils.showShort(getActivity(), errorMessage.msg);
					}
				});
	}
	public void setAds() {
		if (homeInfos != null) {
			adsList = homeInfos.getAdsList();
			ArrayList<String> mImageUrl = null;
			ArrayList<String> mImageTitle = null;
			mImageUrl = new ArrayList<String>();
			mImageTitle = new ArrayList<String>();
			if(homeInfos.getAdsList()!=null&&homeInfos.getAdsList().size()!=0){//修改，处理后台无轮播图闪退的问�?				
				mAdView.setVisibility(View.VISIBLE);
				for (int i = 0; i < homeInfos.getAdsList().size(); i++) {
					mImageUrl.add(homeInfos.getAdsList().get(i).getIcon());
					mImageTitle.add(homeInfos.getAdsList().get(i).getTitle());
				}

				mAdView.setImageResources(mImageUrl, mImageTitle,
						mAdCycleViewListener, stype,false);
			}else {
				mAdView.setVisibility(View.GONE);
			}
		}
	}
	public void setAds2() {
		if (homeInfos2 != null) {
			adsList2 = homeInfos2.getAdsList();
			ArrayList<String> mImageUrl = null;
			ArrayList<String> mImageTitle = null;
			mImageUrl = new ArrayList<String>();
			mImageTitle = new ArrayList<String>();
			if (adsList2 != null && adsList2.size() != 0) {//修改，处理后台无轮播图闪退的问题
				mAdViewview.setVisibility(View.VISIBLE);
				for (int i = 0; i < homeInfos2.getAdsList().size(); i++) {
					mImageUrl.add(homeInfos2.getAdsList().get(i).getIcon());
					mImageTitle.add(homeInfos2.getAdsList().get(i).getTitle());
				}
				mAdView2.setImageResources(mImageUrl, mImageTitle, mAdCycleViewListener2, stype, false);
			} else {
				mAdViewview.setVisibility(View.GONE);
			}
		} else {
			mAdViewview.setVisibility(View.GONE);
		}
	}


	public void InitIsShowingDLTLayout(){

		ServiceMain.getInstance().GetIsShowingDLTLayout(getActivity(), new CallBack<String>() {

			@Override
			public void onSuccess(String showPage) {//1为显�? 2为隐�?                 
				if ("1".equals(showPage)) {
					playingMethodView.setVisibility(View.VISIBLE);

				}else if ("2".equals(showPage)) {
					playingMethodView.setVisibility(View.GONE);
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});
	}

	private void initFootBall() {
		ServiceCaiZhongInformation.getInstance().pastFootBallMatches(
				this.getActivity(), "", "4", "2", ConstantsBase.JCZQ + "","index", new CallBack<FootBallMatchList>() {
					@Override
					public void onSuccess(FootBallMatchList t) {
						// TODO 自动生成的方法存�?					
						if (t.getResultList() != null&& t.getResultList().size() > 0) {
							fm = t.getResultList().get(0);
							ll_football.setVisibility(View.VISIBLE);
							ll_football.setDatas(fm);
							// ll_football.setFirstChecked();// 默认显示第一�?
						} else {
							ll_football.setVisibility(View.GONE);

						}
					}

					@Override
					public void onFailure(ErrorMsg errorMessage) {
						// TODO 自动生成的方法存�?						
						ToastUtils.showShort(getActivity(), errorMessage.msg);
						ll_football.setVisibility(View.GONE);
					}
				});
	}

	public void toUrl(String local, String lotteryId, String url,String playType,String infoMsg) {
		if ("0".equals(local)) {
			if ("taocan".equals(url.trim())) {
				Intent intentHelp = new Intent(getActivity(),TaoCanMainActivity.class);
				startActivity(intentHelp);
			} else {
				Intent intentHelp = new Intent(getActivity(),LotteryTypeActivity.class);
				intentHelp.putExtra("url", url);
				startActivity(intentHelp);
			}
		}
		if ("1".equals(local)) {
			Intent intent;
			if ((ConstantsBase.DLT + "").equals(lotteryId)) {
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(getActivity(), ConstantsBase.CHOOSEDNUMBERS);
				if (list == null) {
					intent = new Intent(getActivity(), DLTMainActivity.class);
				} else {
					intent = new Intent(getActivity(),DLTAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("dltNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			} else if ((ConstantsBase.SD115 + "").equals(lotteryId)) {
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(getActivity(), ConstantsBase.CHOOSEDNUMBERS115);
				if (list == null) {
					intent = new Intent(getActivity(), Main115Activity.class);
				} else {
					intent = new Intent(getActivity(),Account115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("number115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			} else if ((ConstantsBase.GD115 + "").equals(lotteryId)) {
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(getActivity(),ConstantsBase.CHOOSEDNUMBERSGD115);
				if (list == null) {
					intent = new Intent(getActivity(), MainGd115Activity.class);
				} else {
					intent = new Intent(getActivity(),Accountgd115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("numbergd115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			} else if ((ConstantsBase.Fast3 + "").equals(lotteryId)) {
				ArrayList<NumberFast> list = (ArrayList<NumberFast>) ChooseUtil.getData(getActivity(),ConstantsBase.CHOOSEDNUMBERSFAST3);
				if (list == null) {
					intent = new Intent(getActivity(), Fast3MainActivity.class);
				} else {
					intent = new Intent(getActivity(),Fast3AccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean("frommain", true);
					bundle.putSerializable("numberfastList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			} else if ((ConstantsBase.JCZQ + "").equals(lotteryId)) {
				intent = new Intent(getActivity(), FootballMainActivity.class);
				startActivity(intent);
			} else if ((ConstantsBase.JCLQ + "").equals(lotteryId)) {
				intent = new Intent(getActivity(), BasketBallMainActivity.class);
				startActivity(intent);
			} else if ((ConstantsBase.SFC + "").equals(lotteryId)) {
				intent = new Intent(getActivity(), SFCMainActivity.class);
				intent.putExtra("playType", playType);
				startActivity(intent);
			} else if ((ConstantsBase.PL35 + "").equals(lotteryId)) {
				intent = new Intent(getActivity(), Pl5MainActivity.class);
				intent.putExtra("playType", playType);
				startActivity(intent);
			} else if ((ConstantsBase.SSQ + "").equals(lotteryId)) {
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(getActivity(), ConstantsBase.CHOOSEDNUMBERSSSQ);
				if (list == null) {
					intent = new Intent(getActivity(), SSQMainActivity.class);
				} else {
					intent = new Intent(getActivity(),SSQAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ssqNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			} else if ("4".equals(lotteryId)) {
				//MobclickAgent.onEvent(getActivity(), "buttonredpacket");// 统计
				Intent intent4 = new Intent(getActivity(),RedPacketRainActivity.class);
				startActivity(intent4);
			} else {
				ToastUtils.showShort(getActivity(), infoMsg==null?"敬请期待...":infoMsg);
			}
		}
	}

	private void initLotteryType(boolean isRefresh) {
		if (!isRefresh) {
			LotteryTypeData currentLotteryTypeData = MyDbUtils.getCurrentLotteryTypeData();

			if (currentLotteryTypeData != null) {
				lotteryTypes = currentLotteryTypeData.getLotteryList();// 鑾峰彇瀵硅薄闆嗗悎灞炴�鐨勬暟鎹粰view鏄剧�?
				for(LotteryType lt:lotteryTypes){
					lt.setUseLocal("true2");
				}
				mLotteryTypeView.setResources(lotteryTypes,lotteryTypeListener, 2);
			}
			return;
		}
		ServiceMain.getInstance().PostGetLotteryTypeDatas(getActivity(),
				new CallBack<LotteryTypeData>() {

			@Override
			public void onSuccess(LotteryTypeData currentLotteryTypeData) {
				if (currentLotteryTypeData != null) {
					lotteryTypes = currentLotteryTypeData.getLotteryList();// 获取对象集合属性的数据给view显示
					for(LotteryType lt:lotteryTypes){
						lt.setUseLocal("true2");
					}
					mLotteryTypeView.setResources(lotteryTypes,lotteryTypeListener, 2);
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存�?						
				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});
	}
	/* 资讯
	 */
	private void initMessageData() {
		ServiceMain.getInstance().PostGetHomeMessageData(getActivity(),3,
				new CallBack<NewsBean>() {

			@Override
			public void onSuccess(NewsBean t) {
				newsDatas.clear();
				newsDatas.addAll(t.getNewslist());
				mMessageAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});
	}
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(final int position, View imageView) {
			HomeAdsInfo ha = adsList.get(position);
			Intent intent;
			if ("1".equals(ha.getUseLocal())) {
				toUrl("1", ha.getLotteryId(), "", ha.getPlayType(),"敬请期待...");
			} else if ("0".equals(ha.getUseLocal())) {
				if ("taocan".equals(ha.getUrl().trim())) {
					Intent intentHelp = new Intent(getActivity(),TaoCanMainActivity.class);
					startActivity(intentHelp);
				} else {
					String oldUrl = ha.getUrl();
					String newUrl = oldUrl.replaceFirst("shouji", "scp");
					intent = new Intent(getActivity(),LotteryTypeActivity.class);// 跳转html活动界面
					if (ConstantsBase.IP.equals("http://cp.citycai.com")) {
						intent.putExtra("url", newUrl);
					} else {
						intent.putExtra("url", oldUrl);
					}
					if (!ha.getUrl().equals("")) {
						startActivity(intent);
					}
				}
			}
		}

	};
	private ImageCycleViewListener mAdCycleViewListener2 = new ImageCycleViewListener() {
		@Override
		public void onImageClick(final int position, View imageView) {
			HomeAdsInfo ha = adsList2.get(position);
			Intent intent;
			if ("1".equals(ha.getUseLocal())) {
				toUrl("1", ha.getLotteryId(), "", ha.getPlayType(),"敬请期待...");
			} else if ("0".equals(ha.getUseLocal())) {
				if ("taocan".equals(ha.getUrl().trim())) {
					Intent intentHelp = new Intent(getActivity(),TaoCanMainActivity.class);
					startActivity(intentHelp);
				} else {
					String oldUrl = ha.getUrl();
					String newUrl = oldUrl.replaceFirst("shouji", "scp");
					intent = new Intent(getActivity(),LotteryTypeActivity.class);// 跳转html活动界面
					if (ConstantsBase.IP.equals("http://cp.citycai.com")) {
						intent.putExtra("url", newUrl);
					} else {
						intent.putExtra("url", oldUrl);
					}
					if (!ha.getUrl().equals("")) {
						startActivity(intent);
					}
				}
			}
		}

	};
	private LotteryTypeListener lotteryTypeListener = new LotteryTypeListener() {

		@Override
		public void onItemClick(final int position, View view) {
			// TODO 自动生成的方法存�?			
			LotteryType lt = lotteryTypes.get(position);
			toUrl("1", lt.getLotteryId(), "", lt.getPlayType(),lt.getInfoMsg());
		}
	};

	private void initDataForCathectic() {
		// TODO 自动生成的方法存�?
		ServiceUser.getInstance().PostPeriod(getActivity(),ConstantsBase.DLT + "", new CallBack<PeriodResultData>() {

			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存�?						
				if (t != null && t.getPeriodList() != null&& t.getPeriodList().size() > 0) {
					period = t.getPeriodList().get(0);// 拿到第一个数�?						
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存�?						
				ToastUtils.showShort(getActivity(), errorMessage.msg);
			}
		});

	}

	private CathecticListener cathecticListener = new CathecticListener() {
		@Override
		public void clickCathectic() {
			Intent intentHelp = new Intent(getActivity(),TaoCanMainActivity.class);
			startActivity(intentHelp);
		}

		@Override
		public void clickCathectic(int[] a) {
			boolean hasLongin = PreferenceUtil.getBoolean(getActivity(),"hasLogin");
			if (hasLongin) {
				String number = formatNumber(a);
				String Cathecticnumber = number;
				String periodNumber = "";
				if (period != null) {
					periodNumber = period.getPeriodNumber();
				}

				DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
				User user;
				String userId = null;
				String userPwd = null;
				try {
					user = manager.findFirst(User.class);
					userId = user.getUserId();
					userPwd = user.getUserPwd();
				} catch (DbException e) {
					e.printStackTrace();
				}
				Ticket t = new Ticket();
				t.setUserId(userId);
				t.setUserPwd(userPwd);
				t.setPeriodNumber(periodNumber);
				t.setWonStopOfChase("false");
				t.setIsSpecialFlag("false");
				t.setPlayTypeID("0");
				t.setTotalAmount(2 + "");
				t.setMultiple(1 + "");
				t.setSumNum(1 + "");
				t.setHongBaoId("");
				t.setIsChase("false");
				t.setPeriodSizeOfChase(null);
				t.setTotalCostOfChase(null);
				t.setMode("1");
				t.setNumber(number);

				ArrayList<Ticket> arrayList = new ArrayList<Ticket>();
				arrayList.add(t);
				Intent intent = new Intent(getActivity(), PayActivity.class);
				Bundle bundle = new Bundle();
				intent.putExtra("fromPage", "MainHomeFragment");
				bundle.putSerializable("ticketarray", arrayList);
				bundle.putInt("lotteryid", ConstantsBase.DLT);
				bundle.putLong("totalaccount", 2l);
				bundle.putBoolean("useRedPacket", true);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));

			}
		}

		private String formatNumber(int[] a) {
			// TODO 自动生成的方法存�?			
			StringBuilder totalNumber = new StringBuilder();
			for (int i = 0; i < a.length; i++) {
				String singerNumber = null;
				if (a[i] < 10) {
					singerNumber = "0" + a[i];
				} else {
					singerNumber = String.valueOf(a[i]);
				}
				if (i == 4) {
					singerNumber += "|";
				} else if (i == 6) {
					singerNumber = singerNumber;
				} else {
					singerNumber += ",";
				}
				totalNumber.append(singerNumber);
			}
			return totalNumber.toString();
		}

		private void initDataForathectic() {
			// TODO 自动生成的方法存�?		
		}

	};

	public void toPayFootBall(int money) {
		if (fm != null) {
			ll_football.setCheckNumber();
		}
		boolean hasLongin = PreferenceUtil.getBoolean(getActivity(), "hasLogin");
		if (hasLongin) {
			User user = MyDbUtils.getCurrentUser();
			ArrayList<Ticket> tickets = new ArrayList<>();
			Ticket t = new Ticket();
			t.setUserId(user.getUserId());
			t.setUserPwd(user.getUserPwd());
			t.setwLotteryId(ConstantsBase.JCZQ + "");
			t.setMode("0");
			t.setPeriodNumber("");
			t.setPlayType("4");// SPF
			t.setType("2");
			t.setPassType(new PassType2().PassTypeArr[0].getPassTypeValue()
					+ "");// 单关

			t.setMultiple(10 + "");
			t.setSumNum(money / 20 + "");// 总倍数
			t.setTotalAmount(money + "");
			t.setHongBaoId("");

			Gson aGson = new Gson();
			String json2 = aGson.toJson(choosedScroeBean());
			Log.d("tag","===json2" + json2);
			t.setNumber("{\"items\":" + json2 + "}");
			tickets.add(t);
			Intent intent = new Intent(getActivity(), PayActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ticketarray", tickets);
			bundle.putInt("lotteryid", ConstantsBase.JCZQ);
			bundle.putLong("totalaccount", money);
			bundle.putLong("timeInt", 1);
			bundle.putBoolean("useRedPacket", true);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
			intentLogin.putExtra("requestName", "intentLogin");
			startActivityForResult(intentLogin, LOGIN);
		}
	}

	public ArrayList<JczqChoosedScroeBean> choosedScroeBean() {
		ArrayList<JczqChoosedScroeBean> choosedScroeBean = new ArrayList<JczqChoosedScroeBean>();
		for (int j = 0; j < fm.getCheckNumber().size(); j++) {
			JczqChoosedScroeBean jcs = new JczqChoosedScroeBean();
			jcs.setDan(false);
			jcs.setMatchKey(fm.getMatchKey());
			jcs.setValue(JCZQUtil.getJCZQUtil().chooseScroeValue(fm.getCheckNumber().get(j).getKey() + "SPF"));
			jcs.setPlayTypeItem(JCZQUtil.getJCZQUtil().getPlayTypeItem(fm.getCheckNumber().get(j).getKey() + "SPF"));
			choosedScroeBean.add(jcs);
		}
		return choosedScroeBean;
	}

	public void showMyToast() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = View.inflate(getActivity(), R.layout.customer_toast, null);
		view.findViewById(R.id.tv_toast_title);
		Toast toast = new Toast(getActivity());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}

	private void initEvent() {
		showMyToast();
	}

	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)) {
			if ("updateLotteryType".equals(event)) {
				initLotteryType(false);
			}
			if ("updateAds".equals(event)) {
				initHomeAds(false);
			}
			if ("updateInformation".equals(event)) {
				// initDateForInfomationView(false);
			}
			if ("updateCathecticInfo".equals(event)) {
				//				initDataForActivity();
				// initDateForInfomationView(false);
			}
			if ("clearSign".equals(event)&&!PreferenceUtil.getBoolean(getActivity(), "hasLogin")) {
				isShowOtherPPW = false;
				otherPPWType = "";
			}

			//五大联赛  from登录
			if ("initFiveLeague".equals(event)&&PreferenceUtil.getBoolean(getActivity(), "hasLogin")) {
				getFiveLeagueData(false);
				initShowPPW(true);//精准弹窗
			}
			//五大联赛 from 注册
			if ("initFiveLeagueFromRegt".equals(event)) {
				getFiveLeagueData(true);
				initShowPPW(true);//精准弹窗
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) view.getParent()).removeAllViews();
		view = null;
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TOPERSONAL:
			((MainActivity)this.getActivity()).setView(4);		
			break;
		}
	}

	@Override
	public void onRefresh() {
		initData(true);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 停止刷新
				mSwipeLayout.setRefreshing(false);
			}
		}, 3000); // 5秒后发送消息，停止刷新
	}

	public TranslateAnimation toPlace(float leftFrom, float leftTo,
			float topFrom, float topTo) {
		TranslateAnimation animation = new TranslateAnimation(0, leftTo- leftFrom, 0, topTo - topFrom);
		animation.setDuration(500);
		animation.setFillAfter(true);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		return animation;
	}
	/**
	 * 公告
	 */
	private void initNewsData() {
		ServiceMessage.getInstance().PostGetHomeMessageData(getActivity(), 1,0, 1, true, new CallBack<NewsBean>() {

			@Override
			public void onSuccess(NewsBean t) {
				if (t != null && t.getNewslist() != null) {
					ArrayList<NewsBean.NewslistBean> newsBeanList = t.getNewslist();
					if (newsBeanList != null && newsBeanList.size() > 0) {
						setInformation(newsBeanList);
						rl_news.setVisibility(View.VISIBLE);
					} else {
						rl_news.setVisibility(View.GONE);
					}
				} else {
					rl_news.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				rl_news.setVisibility(View.GONE);
			}
		});

	} public void setInformation(final ArrayList<NewsBean.NewslistBean> informations) {
		if (informations != null) {
			ArrayList<String> tipList = new ArrayList<String>();
			for (int i = 0; i < informations.size(); i++) {
				tipList.add(informations.get(i).getTitle());
			}
			if (tipList.size() == 1) {
				looperview1.setText(tipList.get(0));
				looperview1.setVisibility(View.VISIBLE);
				looperview.setVisibility(View.GONE);
				looperview1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent_newDetail = new Intent(getActivity(), MessageDetailActivity.class);
						intent_newDetail.putExtra("id", informations.get(0).getId());
						startActivity(intent_newDetail);
					}
				});
			} else {
				looperview.setType(0);
				looperview.setTipList(tipList);
				looperview.setVisibility(View.VISIBLE);
				looperview1.setVisibility(View.GONE);
				looperview.tv_tip_out.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						for (int i = 0; i < informations.size(); i++) {
							if (looperview.tv_tip_out.getText().toString()
									.equals(informations.get(i).getTitle())) {
								Intent intentHelp = new Intent(getActivity(), MessageDetailActivity.class);
								intentHelp.putExtra("id", informations.get(i).getId());
								startActivity(intentHelp);
								break;
							}
						}
					}
				});
				looperview.tv_tip_in.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						for (int i = 0; i < informations.size(); i++) {
							if (looperview.tv_tip_in.getText().toString()
									.equals(informations.get(i).getTitle())) {
								Intent intentHelp = new Intent(getActivity(), InformationDetailActivity.class);
								intentHelp.putExtra("id", informations.get(i).getId());
								startActivity(intentHelp);
								break;
							}
						}
					}
				});
			}
		}

	}
	//精准推送首页弹窗 type 1：新下载app注册送礼包 2：登录但未充值  3：》=7日未登录 回归礼包  4：用户购彩中奖且金额大于50元且中奖是本金3倍
	public void showSomePPW(String type){
		isShowOtherPPW=false;//只显示一次
		if (otherPPW != null && otherPPW.isShowing()) {
			otherPPW.dismiss();
		} else {
			View view_layout = null;
			switch (type) {
			case "fiveleague"://五大联赛
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_five_league,null);
				Button btnUse=(Button) view_layout.findViewById(R.id.btn_use);
				btnUse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), FootballMainActivity.class);
						startActivity(intent);
						otherPPW.dismiss();
					}
				});
				break;
			case "register":
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_register_188,null);
				TextView toRegisterBtn = (TextView) view_layout.findViewById(R.id.btn_ppw_register);
				toRegisterBtn.setOnClickListener(this);
				break;
			case "recharge":
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_torecharge,null);
				TextView toRechargeBtn = (TextView) view_layout.findViewById(R.id.btn_ppw_recharge);
				toRechargeBtn.setOnClickListener(this);
				break;
			case "returngift":
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_return_gift,null);
				Button toUseBtn = (Button) view_layout.findViewById(R.id.btn_ppw_use); 
				Button toViewBtn = (Button) view_layout.findViewById(R.id.btn_ppw_view); 
				toUseBtn.setOnClickListener(this);
				toViewBtn.setOnClickListener(this);
				break;

			case "win":
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_win_than50,null);
				Button toShareBtn = (Button) view_layout.findViewById(R.id.btn_ppw_share); 
				TextView tv_scheme = (TextView) view_layout.findViewById(R.id.tv_scheme);
				TextView tv_bonus = (TextView) view_layout.findViewById(R.id.tv_bonus);
				tv_bonus.setText(Html.fromHtml("<html>中奖<big>"+mhomePpwBean.getPrize()+"</big>元</html>"));			
				tv_scheme.setText(mhomePpwBean.getMsg());
				toShareBtn.setOnClickListener(this);
				break;

			default:
				view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_register_188,null);
				Button todeButton = (Button) view_layout.findViewById(R.id.btn_ppw_register); 
				todeButton.setOnClickListener(this);
				break;
			}

			otherPPWType ="";
			Button btnClose = (Button) view_layout.findViewById(R.id.btn_close);
			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					otherPPW.dissmissPopupWindow();
				}
			});
			otherPPW= new CommonPPW( getActivity(),view_layout);
			otherPPW.showPopupWindow(view.findViewById(R.id.scrollview));	


		}
	}


	public void setWindowAlpha(float alpha){
		if (getActivity()==null) {
			return;
		}
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = alpha;
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getActivity().getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_news_jczx:// 竞彩资讯
			Intent intent = new Intent(getActivity(), MessageListActivity.class);
			intent.putExtra("newsType", 2);
			startActivity(intent);
			break;
		case R.id.iv_news_latestLottery:// 最新开奖
			Intent intent_latestLottery = new Intent(getActivity(),LatestLotteryInfoActivity.class);
			startActivity(intent_latestLottery);
			break;
		case R.id.iv_news_componyNews:// 公司公告
			Intent intent_componyNews = new Intent(getActivity(),MessageListActivity.class);
			intent_componyNews.putExtra("newsType", 1);
			startActivity(intent_componyNews);
			break;
		case R.id.looperview1:// 公告
			Intent intent_newDetail = new Intent(getActivity(),MessageDetailActivity.class);
			intent_newDetail.putExtra("id", news_id);
			startActivity(intent_newDetail);
			break;
		case R.id.btn_ppw_register://注册送188 弹窗 立即注册
			otherPPW.dissmissPopupWindow();
			Intent intent_register = new Intent(getActivity(),RegisterByMoibleActivity.class);
			intent_register.putExtra("from", "login");
			startActivity(intent_register);
			break;
		case R.id.btn_ppw_recharge://冲20送20 弹窗 立即充值
			otherPPW.dissmissPopupWindow();
			Intent intent_recharge = new Intent(getActivity(), PayMoneyActivity.class);
			intent_recharge.putExtra("needback", true);												
			startActivityForResult(intent_recharge, TOPERSONAL);

			break;
		case R.id.btn_ppw_use://回归礼包 弹窗 立即使用
			otherPPW.dissmissPopupWindow();
			//			Intent intent_use = new Intent(getActivity(),FootballMainActivity.class);
			//			startActivity(intent_use);
			break;
		case R.id.btn_ppw_view://回归礼包 弹窗 立即查看
			otherPPW.dissmissPopupWindow();
			Intent redpacket = new Intent(getActivity(), RedPacketActivity.class);
			startActivity(redpacket);
			break;

		case R.id.btn_ppw_share://中奖》=50 弹窗 立即分享
			otherPPW.dissmissPopupWindow();
			new OpenOrderPageUtil().toOpenOrderPage(getActivity(), mhomePpwBean.getLotteryType(),
					mhomePpwBean.getSourceSchemeId(),false,mhomePpwBean.getPlayType());	
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				looperview1.setFocusable(true);
				looperview1.requestFocus();
				looperview1.setSelected(true);
			}
		}, 3000); // 5秒

	}
}
