package com.cshen.tiyu;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.find.FindFragment;
import com.cshen.tiyu.activity.mian4.gendan.GenDanMainFragment;
import com.cshen.tiyu.activity.mian4.main.MainHomeFragment;
import com.cshen.tiyu.activity.mian4.personcenter.MainPersonalCenterFragment;
import com.cshen.tiyu.activity.mian4.personcenter.message.MessageDetailActivity;
import com.cshen.tiyu.activity.redpacket.RedPacketRainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.base.NetReceivers;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Back;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.information.InformationData;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.main.HomeAdsData;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.main.TabIndicator;
import com.cshen.tiyu.domain.main.TabIndicatorData;
import com.cshen.tiyu.net.https.BaseHttps;
import com.cshen.tiyu.net.https.BaseHttpsCallback;
import com.cshen.tiyu.net.https.ServiceABase;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceVersion;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.OpenOrderPageUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.StatusBarUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TabIndicatorView;
import com.google.gson.Gson;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.RequestParams;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {

    private final String mPageName = "MainActivity";

    private Fragment mTab01;
    private GenDanMainFragment mTab02;
    private Fragment mTab03;
    private Fragment mTab04;
    ArrayList<TabIndicator> tabIndicators = new ArrayList<TabIndicator>();
    ArrayList<TabIndicatorView> tabIndicatorViews = new ArrayList<TabIndicatorView>();

    LinearLayout bottom;
    TextView activity, share, message;
    EditText messageEd;
    private long firstTime = 0;
    FragmentManager fm;
    Bundle savedInstanceState;

    // 广播接受 判断你是否连接网络
    private NetReceivers mReceiver = new NetReceivers();
    private IntentFilter mFilter = new IntentFilter();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_main);


        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        initDate();

        if ("toopenredpacket".equals(getIntent().getStringExtra("todo"))) {
            Intent intent = new Intent(this, RedPacketRainActivity.class);
            startActivity(intent);
        }
        if ("togetmessage".equals(getIntent().getStringExtra("todo"))) {
            Intent intentMessage = new Intent(this, MessageDetailActivity.class);
            intentMessage.putExtra("title", getIntent().getStringExtra("title"));
            intentMessage.putExtra("content", getIntent().getStringExtra("content"));
            intentMessage.putExtra("needSave", true);
            startActivity(intentMessage);
        }

        if ("toOrderDetail".equals(getIntent().getStringExtra("todo"))) {//鍘昏鍗曡鎯�
            String lotteryType = getIntent().getStringExtra("lotteryId");
            String schemeId = getIntent().getStringExtra("schemeId");
            new OpenOrderPageUtil().toOpenOrderPage(MainActivity.this, lotteryType, schemeId, true, "");
        }

        if ("rechargeSuccess".equals(getIntent().getStringExtra("todo"))) {//鍏呭�煎埌璐�,璺宠浆鍒版垜鐨勫僵绁╢ragment
            if (tabIndicatorViews != null && tabIndicatorViews.size() > 0) {
                setView(4);
            }
        }

        if ("toLink".equals(getIntent().getStringExtra("todo"))) {//璺抽摼鎺�
            Intent intentLink = new Intent(this, LotteryTypeActivity.class);
            intentLink.putExtra("url", getIntent().getStringExtra("url"));
            startActivity(intentLink);
        }
        user = MyDbUtils.getCurrentUser();
        if (user != null) {
            addPushAlias(user.getUserId());
        }
        findViewById(R.id.activity).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
               // FeedbackAPI.openFeedbackActivity();
            }
        });
    }

    @Override
    public void setStatusBar() {

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, null);//鐘舵�佹爮
        }


    }

    private void initDate() {
        String[] title = new String[]{"首页", "跟单", "发现", "我的"};
        int[] ic_common = new int[]{R.mipmap.ic_home_common, R.mipmap.ic_gendan_common, R.mipmap.ic_find_common, R.mipmap.ic_my_common};
        int[] ic_light = new int[]{R.mipmap.ic_home_light, R.mipmap.ic_gendan_light, R.mipmap.ic_find_light, R.mipmap.ic_my_light};
        //鍥剧墖鍏堝啓姝�
        for (int i = 0; i < title.length; i++) {
            TabIndicator tabIndicator = new TabIndicator();
            tabIndicator.setTitle(title[i]);
            tabIndicator.setIcon_common(ic_common[i] + "");
            tabIndicator.setIcon_light(ic_light[i] + "");
            tabIndicator.setRank(i + 1);
            tabIndicators.add(tabIndicator);
        }
        initView();
// 先加载本地的数据
        /*TabIndicatorData currentTabIndicatorData = MyDbUtils.getCurrentTabIndicatorData();
        // 本地有数据的话就直接先用本地
        if (currentTabIndicatorData != null) {
            tabIndicators = currentTabIndicatorData.getIndicators();
            if(tabIndicators!=null){
                initView();
            }else{
                setSelect(1);// 显示第一个fragment
            }
        }else{
            setSelect(1);// 显示第一个fragment
        }*/
    }

    private void initView() {
        // TODO 鏁版嵁闆嗗悎缁檝iew闆嗗悎 鏍规嵁鏁版嵁娣诲姞view
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabIndicator tabIndicator = tabIndicators.get(i);
            TabIndicatorView tabIndicatorView = new TabIndicatorView(this);
            tabIndicatorView.setRank(tabIndicator.getRank());// 设置这个view的位置
            if(i==0) {
                tabIndicatorView.setTabTitle("首页");
            }if(i==1) {
                tabIndicatorView.setTabTitle("跟单");
            }if(i==2) {
                tabIndicatorView.setTabTitle("发现");
            }if(i==3) {
                tabIndicatorView.setTabTitle("我的");
            }
            tabIndicatorView.setTabIcon(tabIndicator.getIcon_common(),tabIndicator.getIcon_light(),true);
            tabIndicatorViews.add(tabIndicatorView);
        }
        // 搴曢儴娣诲姞view
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        params.weight = 1;

        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < tabIndicatorViews.size(); i++) {
            final TabIndicatorView contactIndicator = tabIndicatorViews.get(i);

            bottom.addView(contactIndicator, params);

            if (contactIndicator.getRank() == 1) {
                contactIndicator.setTabSelected(true);// 绗竴涓閫変腑
                setSelect(contactIndicator.getRank());// 鏄剧ず绗竴涓猣ragment
            } else {
                contactIndicator.setTabSelected(false);
            }

            contactIndicator.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO 鐐瑰嚮浜嬩欢
                    // 濡傛灉鏄鍥涗釜琚偣鍑诲苟涓旇繕娌℃湁鐧诲叆
                    if (!PreferenceUtil.getBoolean(MainActivity.this,
                            "hasLogin") && contactIndicator.getRank() == 4) {
                        // 璺宠浆鍒扮櫥鍏ョ晫闈�
                        startActivity(new Intent(MainActivity.this,
                                LoginActivity.class));
                    } else {
                        // 閲嶇疆鍥剧墖
                        resetImgs();
                        contactIndicator.setTabSelected(true);// 鍙樹负楂樹寒
                        setSelect(contactIndicator.getRank());
                    }
                }
            });

        }
    }

    protected void resetImgs() {
        for (int i = 0; i < tabIndicatorViews.size(); i++) {
            tabIndicatorViews.get(i).setTabSelected(false);
        }
    }

    public void update() {
        final Map<String, String> oldVersion = MyDbUtils.getVersion();
        ServiceVersion.getInstance()
                .PostGetVersionDatas(this, new CallBack<Map<String, String>>() {

                    @Override
                    public void onSuccess(Map<String, String> t) {
                        Map<String, String> newVersion = MyDbUtils
                                .getVersion();

                        if (!oldVersion.get("LotteryTypeVersion")
                                .equals(newVersion.get("LotteryTypeVersion"))) {
                            getLotteryTypeDatas();
                        }
                        if (!oldVersion.get("LunboVersion")
                                .equals(newVersion.get("LunboVersion"))) {
                            getAdsDatas();
                        }

                        if (!oldVersion.get("InformationVersion")
                                .equals(newVersion.get("InformationVersion"))) {
                        }
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO
                        ToastUtils.showShort(MainActivity.this, errorMessage.msg);
                    }
                });
    }

    protected void getInformationDatas() {
        // TODO
        ServiceMain.getInstance().PostGetInformationDatas(this,
                new CallBack<InformationData>() {

                    @Override
                    public void onSuccess(InformationData t) {
                        // TODO
                        EventBus.getDefault().post("updateInformation");
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO
                        ToastUtils.showShort(MainActivity.this, errorMessage.msg);
                    }

                });

    }

    protected void setSelect(int i) {

        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 1:
                if (mTab01 == null) {
                    if (savedInstanceState != null) {
                        mTab01 = fm.findFragmentByTag("MainHomeFragment");
                        if (mTab01 == null) {
                            mTab01 = new MainHomeFragment();
                            transaction.add(R.id.id_content, mTab01, "MainHomeFragment");
                        }
                    } else {
                        mTab01 = new MainHomeFragment();
                        transaction.add(R.id.id_content, mTab01, "MainHomeFragment");
                    }
                } else {
                    transaction.show(mTab01);
                    // 绗竴涓富椤佃鐗规畩澶勭悊璨屼技鍙樼儲浜�
                    update();
                    EventBus.getDefault().post("updateCathecticInfo");// gengxingqichi
                }

                break;
            case 2:
                if (mTab02 == null) {
                    if (savedInstanceState != null) {
                        mTab02 = (GenDanMainFragment) fm.findFragmentByTag("GenDanMainFragment");
                        if (mTab02 == null) {
                            mTab02 = new GenDanMainFragment();
                            transaction.add(R.id.id_content, mTab02, "GenDanMainFragment");
                        }
                    } else {
                        mTab02 = new GenDanMainFragment();
                        transaction.add(R.id.id_content, mTab02, "GenDanMainFragment");
                    }
                } else {
                    transaction.show(mTab02);
                    EventBus.getDefault().post("updateLiveScores");
                }
                break;
            case 3:
                if (mTab03 == null) {
                    if (savedInstanceState != null) {
                        mTab03 = fm.findFragmentByTag("FindFragment");
                        if (mTab03 == null) {
                            mTab03 = new FindFragment();
                            transaction.add(R.id.id_content, mTab03, "FindFragment");
                        }
                    } else {
                        mTab03 = new FindFragment();
                        transaction.add(R.id.id_content, mTab03, "FindFragment");
                    }
                } else {
                    transaction.show(mTab03);
                    EventBus.getDefault().post("updatePeriodsInfo");
                    EventBus.getDefault().post("updateLatestLottery");
                    EventBus.getDefault().post("updateFindData");

                }
                break;
            case 4:
                // 鍏堝垽鏂湁鏈ㄦ湁鐧诲叆杩�
                if (PreferenceUtil.getBoolean(MainActivity.this,
                        "hasLogin")) {
                    ServiceUser.getInstance().GetUserInfo(this);// 浣欓璺熸柊
                    if (mTab04 == null) {
                        if (savedInstanceState != null) {
                            mTab04 = fm.findFragmentByTag("MainPersonalCenterFragment2");
                            if (mTab04 == null) {
                                mTab04 = new MainPersonalCenterFragment();
                                transaction.add(R.id.id_content, mTab04, "MainPersonalCenterFragment2");
                            }
                        } else {
                            mTab04 = new MainPersonalCenterFragment();
                            transaction.add(R.id.id_content, mTab04, "MainPersonalCenterFragment2");
                        }
                    } else {
                        transaction.show(mTab04);
                        EventBus.getDefault().post("updateCathectic"); //閫氱煡鏇存柊
                    }
                } else {
                    // 濡傛灉娌℃湁鐧诲叆 璋冨埌鐧诲叆鐣岄潰
                    startActivity(new Intent(this, LoginActivity.class));
                }

                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        // TODO Auto-generated method stub

        if (mTab01 != null) {
            transaction.hide(mTab01);
        } else {
            if (savedInstanceState != null) {
                mTab01 = fm.findFragmentByTag("MainHomeFragment");
                if (mTab01 != null) {
                    transaction.hide(mTab01);
                }
            }
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        } else {
            if (savedInstanceState != null) {
                mTab02 = (GenDanMainFragment) fm.findFragmentByTag("GenDanMainFragments");
                if (mTab02 != null) {
                    transaction.hide(mTab02);
                }
            }
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        } else {
            if (savedInstanceState != null) {
                mTab03 = fm.findFragmentByTag("FindFragment");
                if (mTab03 != null) {
                    transaction.hide(mTab03);
                }
            }
        }
        if (mTab04 != null) {
            transaction.hide(mTab04);
        } else {
            if (savedInstanceState != null) {
                mTab04 = fm.findFragmentByTag("MainPersonalCenterFragment2");
                if (mTab04 != null) {
                    transaction.hide(mTab04);
                }
            }
        }
    }

    private void getAdsDatas() {
        ServiceMain.getInstance()
                .PostGetHomeAdsData(this, new CallBack<HomeAdsData>() {
                    @Override
                    public void onSuccess(HomeAdsData t) {
                        EventBus.getDefault().post("updateAds");
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        ToastUtils.showShort(MainActivity.this, errorMessage.msg);
                    }
                });
    }

    private void getLotteryTypeDatas() {
        ServiceMain.getInstance()
                .PostGetLotteryTypeDatas(this, new CallBack<LotteryTypeData>() {

                    @Override
                    public void onSuccess(LotteryTypeData t) {
                        EventBus.getDefault().post("updateLotteryType");
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        ToastUtils.showShort(MainActivity.this, errorMessage.msg);
                    }
                });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO 鑷姩鐢熸垚鐨勬柟娉曞瓨鏍�
        super.onNewIntent(intent);
        String hasLoginString = intent.getStringExtra("hasLogin");
        String nowPage = intent.getStringExtra("nowPage");

        if (hasLoginString != null) {
            if (hasLoginString.equals("yes")) {
                // 閫変腑绗洓涓猣ragment
                if (nowPage != null) {
                    if (nowPage.equals("1")) {
                        setView(1);
                    } else if (nowPage.equals("2")) {
                        setView(2);
                    } else {
                        setView(4);
                        EventBus.getDefault().post("updateFragment");
                    }
                } else {
                    setView(4);
                    EventBus.getDefault().post("updateFragment");
                }
            }
            if (hasLoginString.equals("cancel")) {
                // 閫変腑绗竴涓猣ragment
                setView(1);
            }
        }

    }

    // 杩斿洖閿��鍑�
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mTab02 != null && mTab02.timekeybroad != null && mTab02.timekeybroad.getVisibility() == View.VISIBLE) {
                    mTab02.timekeybroad.setVisibility(View.GONE);
                    return true;
                } else {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) { // 濡傛灉涓ゆ鎸夐敭鏃堕棿闂撮殧澶т簬2绉掞紝鍒欎笉閫�鍑�
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;// 鏇存柊firstTime
                        return true;
                    } else { // 涓ゆ鎸夐敭灏忎簬2绉掓椂锛岄��鍑哄簲鐢�
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        System.exit(0);
                        unregisterReceiver(mReceiver);

                    }
                    break;
                }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 鍙嶆敞鍐孍ventBus
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        Log.d("result", "onActivityResult");
    }

    public void setView(int i) {
        resetImgs();
        tabIndicatorViews.get(i - 1).setTabSelected(true);
        setSelect(i);
    }

    public void onEventMainThread(String event) {

        if (!TextUtils.isEmpty(event)) {
            if ("deviceToken".equals(event)) {
                String deviceToken = PreferenceUtil.getString(this, "deviceToken");
                if (!TextUtils.isEmpty(deviceToken)) {
                    setDeviceToken(deviceToken);
                }
            }
        }
    }

    //淇濆瓨鍙嬬洘璁惧鍙�
    public void setDeviceToken(String deviceToken) {
        ServiceMain.getInstance().getSaveDeviceToken(MainActivity.this, deviceToken, new CallBack<String>() {

            @Override
            public void onSuccess(String t) {

            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
            }
        });
    }

    private void clearPushAlias(String userId) {
        PushAgent.getInstance(this).deleteAlias(userId, ConstantsBase.AliasType, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean arg0, String arg1) {
                System.out.println("deleteAlias" + arg1);
            }
        });
    }

    private void addPushAlias(String userId) {
        PushAgent.getInstance(this).deleteAlias(userId, ConstantsBase.AliasType, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean arg0, String arg1) {
                System.out.println("deleteAlias" + arg1);
            }
        });
        PushAgent.getInstance(this).addAlias(userId, ConstantsBase.AliasType, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean arg0, String arg1) {
                System.out.println("addAlias" + arg1);
            }
        });
    }
}
