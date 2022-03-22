package com.cshen.tiyu.zx.main4.main;


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
import com.cshen.tiyu.activity.mian4.main.MessageAdapter;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
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

public class ZXNewHomeMainFragment2 extends Fragment {
    private View view;
    // 轮播
    private ImageCycleView mAdView;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ArrayList<String> mImageTitle = new ArrayList<>();
    // 大乐透专属幸运号
    private PlayingMethodView playingMethodView;
    // 热门彩种
    private LotteryTypeView mLotteryTypeView;
    private ArrayList<LotteryType> lotteryTypes = new ArrayList<LotteryType>();
    private String[] lotteryids =new String[]{ConstantsBase.JCZQ+"",ConstantsBase.JCLQ+"",
            ConstantsBase.SFC+"",ConstantsBase.SFC+"",ConstantsBase.SSQ+"",ConstantsBase.DLT+"",
            ConstantsBase.Fast3+"",ConstantsBase.SD115+""};
    private String[] titles =new String[]{"竞彩足球","竞彩篮球","胜负彩","任选九","双色球","大乐透","快3","山东11选5"};

    // 资讯
    private MyListView listview_message;// 展示最新三条资讯的列表
    private MessageAdapter mMessageAdapter;
    private ArrayList<NewslistBean> newsDatas = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_new_mainhome2, container, false);
            initView(view);
        }
        initData();
        return view;
    }
    private void initData() {
        // 一些不涉及到传参数都请求都在预加载界面缓存到本地了
        if (!NetWorkUtil.isNetworkAvailable(this.getActivity())) {
            ToastUtils.showShort(this.getActivity(), "当前网络信号较差，请检查网络设置");
        }
        // /资讯
        initMessageData();
    }

    private void initView(View view) {
        // 轮播
        mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
        setRecycleAds();
        // 大乐透
        playingMethodView = (PlayingMethodView) view.findViewById(R.id.pm_playingmethod);
        setRecyclPlay();
        // 热门彩种
        mLotteryTypeView = (LotteryTypeView) view.findViewById(R.id.lt_lotterytype);
        setLottery();
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
    public void setRecyclPlay(){
        playingMethodView.setCathecticListener(cathecticListener);
    }
    private CathecticListener cathecticListener = new CathecticListener() {
        @Override
        public void clickCathectic() {
            ToastUtils.showShort(getActivity(), "暂停销售");
        }
        @Override
        public void clickCathectic(int[] a) {
            ToastUtils.showShort(getActivity(), "暂停销售");
        }
    };
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
        mImageUrl.clear();
        mImageTitle.clear();
        //添加头部
        mImageUrl.add(R.mipmap.zhuce+"");
       // mImageUrl.add(R.mipmap.chongzhi+"");
        mImageTitle.add("");
       // mImageTitle.add("");
        mAdView.setImageResources(mImageUrl, mImageTitle,mAdCycleViewListener, 2,true);

    }
    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void onImageClick(final int position, View imageView) {
           // ToastUtils.showShort(getActivity(), "暂停销售");
        }
    };
    public void setLottery(){
        lotteryTypes.clear();
        for(int i =0;i<8;i++){
            LotteryType lt = new LotteryType();
            lt.setLotteryId(lotteryids[i]);
            lt.setTitle(titles[i]);
            lt.setInfo("暂停销售");
            if( (ConstantsBase.SFC+"").equals(lotteryids[i])){
                lt.setPlayType(i%2+"");
            }
            lt.setUseLocal("true2");
            lotteryTypes.add(lt);
        }
        mLotteryTypeView.setResources(lotteryTypes, lotteryTypeListener, 2);
    }
    private LotteryTypeListener lotteryTypeListener = new LotteryTypeListener() {

        @Override
        public void onItemClick(final int position, View view) {
            ToastUtils.showShort(getActivity(), "暂停销售");
        }
    };
    /**
     * 热点资讯
     */
    private void initMessageData() {

        ServiceMain.getInstance().PostGetZXHomeMessageData(getActivity(), new CallBack<NewsBean>() {

            @Override
            public void onSuccess(NewsBean t) {
                newsDatas.clear();
                if(t!=null&&t.getNewslist()!=null
                        &&t.getNewslist().size()>5){
                    newsDatas.addAll(t.getNewslist().subList(0,5));
                }else{
                    newsDatas.addAll(t.getNewslist());
                }
                mMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {

            }
        });

    }
}
