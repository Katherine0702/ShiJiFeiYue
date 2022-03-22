package com.cshen.tiyu.activity.mian4.gendan;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.TOLotteryUtil;
import com.cshen.tiyu.activity.mian4.gendan.bangdan.BangDanMainFragment;
import com.cshen.tiyu.activity.mian4.gendan.niuren.NiuRenSearchactivity;
import com.cshen.tiyu.activity.mian4.gendan.niuren.RenZhengMainFragment;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.HuanDengBack;
import com.cshen.tiyu.domain.gendan.HuanDengsInfo;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.ImageCycleView;
import com.cshen.tiyu.widget.ImageCycleView.ImageCycleViewListener;


import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class GenDanMainFragment extends Fragment
        implements OnClickListener {

    private Activity _this;
    private View search;
    private TextView search_txt;
    private String gendanniuren = "bangdan";
    private View shouming, jifen;
    private TextView bangdan, niuren;

    private ImageCycleView mAdView;
    private ArrayList<HuanDengsInfo> adsList = null;
    private boolean showAds = false;
    private boolean isGetting = false;
    public int stype = 2;
    /*******倍数相关
     **************/
    private View loadtime;//倍数时使用
    public TextView realTime;//倍数
    public int timeIntMAX = 99999, timeInt = 0;//总倍数
    public View timekeybroad;//画的键盘和付款栏
    private TextView number10, number25, number50, number100, number500;


    private FragmentManager fm;
    private BangDanMainFragment mTab01;
    private RenZhengMainFragment mTab02;
    private Bundle savedInstanceState;
    String SchemeBackupsId;
    long cost;
    int type;
    View mFakeStatusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        fm = getFragmentManager();
        View view = inflater.inflate(R.layout.gendan_main_fragment, container, false);
        _this = this.getActivity();
        EventBus.getDefault().register(this);

        initView(view);
        initdata();
        setSelect(0);
        return view;

    }

    public void initView(View view) {
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            mFakeStatusBar.setVisibility(View.GONE);
        } else {
            mFakeStatusBar.setVisibility(View.VISIBLE);
        }

        shouming = view.findViewById(R.id.iv_setting);
        shouming.setOnClickListener(this);
        jifen = view.findViewById(R.id.jifen);
        jifen.setOnClickListener(this);
        bangdan = (TextView) view.findViewById(R.id.bangdan);
        niuren = (TextView) view.findViewById(R.id.niuren);
        bangdan.setOnClickListener(this);
        niuren.setOnClickListener(this);
        search_txt = (TextView) view.findViewById(R.id.search_txt);
        search = view.findViewById(R.id.search);
        search.setOnClickListener(this);

        mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
        /******************************************/
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;//屏幕宽度
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mAdView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;
        // 控件的高强制设成20
        linearParams.width = screenW;// 控件的宽强制设成30
        linearParams.height = (int) screenW / 3;// 控件的宽强制设成30
        mAdView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        /******************************************/
        /**************软键盘************/

        realTime = (TextView) view.findViewById(R.id.realtime);
        timekeybroad = view.findViewById(R.id.dlt_pay_numbertime);
        view.findViewById(R.id.suretime).setOnClickListener(this);
        view.findViewById(R.id.cleartime).setOnClickListener(this);
        view.findViewById(R.id.number0).setOnClickListener(this);
        view.findViewById(R.id.number1).setOnClickListener(this);
        view.findViewById(R.id.number2).setOnClickListener(this);
        view.findViewById(R.id.number3).setOnClickListener(this);
        view.findViewById(R.id.number4).setOnClickListener(this);
        view.findViewById(R.id.number5).setOnClickListener(this);
        view.findViewById(R.id.number6).setOnClickListener(this);
        view.findViewById(R.id.number7).setOnClickListener(this);
        view.findViewById(R.id.number8).setOnClickListener(this);
        view.findViewById(R.id.number9).setOnClickListener(this);
        number10 = (TextView) view.findViewById(R.id.time10);
        number10.setOnClickListener(this);
        number25 = (TextView) view.findViewById(R.id.time25);
        number25.setOnClickListener(this);
        number50 = (TextView) view.findViewById(R.id.time50);
        number50.setOnClickListener(this);
        number100 = (TextView) view.findViewById(R.id.time100);
        number100.setOnClickListener(this);
        number500 = (TextView) view.findViewById(R.id.time500);
        number500.setOnClickListener(this);

        loadtime = view.findViewById(R.id.load);
        loadtime.setAlpha(0.7f);
        loadtime.setOnClickListener(this);
    }

    public void initdata() {
        if (isGetting) {
            return;
        } else {
            isGetting = true;
        }
        ServiceGenDan.getInstance().PostGetHuangDengData(getActivity(), new CallBack<HuanDengBack>() {

            @Override
            public void onSuccess(HuanDengBack t) {
                if (t != null && t.getAdsList() != null && t.getAdsList().size() > 0) {
                    adsList = t.getAdsList();
                    showAds = true;
                    setAds();
                } else {
                    mAdView.setVisibility(View.GONE);
                    isGetting = false;
                }
            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                // TODO 自动生成的方法存根
                ToastUtils.showShort(getActivity(), errorMessage.msg);
                mAdView.setVisibility(View.GONE);
                isGetting = false;
            }
        });
    }

    public void setAds() {
        ArrayList<String> mImageUrl = new ArrayList<String>();
        ArrayList<String> mImageTitle = new ArrayList<String>();
        for (int i = 0; i < adsList.size(); i++) {
            mImageUrl.add(adsList.get(i).getIcon());
            mImageTitle.add(adsList.get(i).getTitle());
        }
        mAdView.setImageResources(mImageUrl, mImageTitle, mAdCycleViewListener, stype, false);
        mAdView.setVisibility(View.VISIBLE);
        isGetting = false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.jifen:
                Intent intent = new Intent(getActivity(), LotteryTypeActivity.class);// 跳转html活动界面
                intent.putExtra("url", ConstantsBase.IP + "/integrationCenter.html?userId=" + MyDbUtils.getCurrentUser().getUserId());
                startActivity(intent);
                break;
            case R.id.iv_setting:
                Intent intentHelp = new Intent(_this, LotteryTypeActivity.class);
                intentHelp.putExtra("url", "http://an.caiwa188.com/help/gendan.html");
                startActivity(intentHelp);
                break;
            case R.id.search:
                Intent intentSearch = new Intent(_this, NiuRenSearchactivity.class);
                intentSearch.putExtra("from", gendanniuren);
                startActivity(intentSearch);
                break;
            case R.id.bangdan:
                setSelect(0);
                //			if(showAds){
                //				mAdView.setVisibility(View.VISIBLE);
                //			}else{
                //				mAdView.setVisibility(View.GONE);
                //			}
                initdata();//重新获取轮播图
                int rightBD = bangdan.getPaddingRight();
                int topBD = bangdan.getPaddingTop();
                int bottomBD = bangdan.getPaddingBottom();
                int leftBD = bangdan.getPaddingLeft();
                int rightNR = niuren.getPaddingRight();
                int topNR = niuren.getPaddingTop();
                int bottomNR = niuren.getPaddingBottom();
                int leftNR = niuren.getPaddingLeft();
                bangdan.setTextColor(getResources().getColor(R.color.mainred));
                bangdan.setBackgroundResource(R.drawable.corner13linenone_whitefull);
                niuren.setTextColor(Color.parseColor("#ffffff"));
                niuren.setBackground(null);
                bangdan.setPadding(leftBD, topBD, rightBD, bottomBD);
                niuren.setPadding(leftNR, topNR, rightNR, bottomNR);
                break;
            case R.id.niuren:
                setSelect(1);
                mAdView.setVisibility(View.GONE);
                int rightBD2 = bangdan.getPaddingRight();
                int topBD2 = bangdan.getPaddingTop();
                int bottomBD2 = bangdan.getPaddingBottom();
                int leftBD2 = bangdan.getPaddingLeft();
                int rightNR2 = niuren.getPaddingRight();
                int topNR2 = niuren.getPaddingTop();
                int bottomNR2 = niuren.getPaddingBottom();
                int leftNR2 = niuren.getPaddingLeft();
                bangdan.setTextColor(Color.parseColor("#ffffff"));
                bangdan.setBackground(null);
                niuren.setTextColor(getResources().getColor(R.color.mainred));
                niuren.setBackgroundResource(R.drawable.corner24linenone_whitefull);
                bangdan.setPadding(leftBD2, topBD2, rightBD2, bottomBD2);
                niuren.setPadding(leftNR2, topNR2, rightNR2, bottomNR2);
                break;
            case R.id.realtime:
                ToastUtils.showShort(_this, "点击");
                break;
            case R.id.number0:
                setTime("0");
                break;
            case R.id.number1:
                setTime("1");
                break;
            case R.id.number2:
                setTime("2");
                break;
            case R.id.number3:
                setTime("3");
                break;
            case R.id.number4:
                setTime("4");
                break;
            case R.id.number5:
                setTime("5");
                break;
            case R.id.number6:
                setTime("6");
                break;
            case R.id.number7:
                setTime("7");
                break;
            case R.id.number8:
                setTime("8");
                break;
            case R.id.number9:
                setTime("9");
                break;
            case R.id.time10:
                clearTime();
                number10.setTextColor(Color.parseColor("#FF3232"));
                number10.setBackgroundResource(R.drawable.dlt_tzback_click);
                realTime.setText("10");
                break;
            case R.id.time25:
                clearTime();
                number25.setTextColor(Color.parseColor("#FF3232"));
                number25.setBackgroundResource(R.drawable.dlt_tzback_click);
                realTime.setText("25");
                break;
            case R.id.time50:
                clearTime();
                number50.setTextColor(Color.parseColor("#FF3232"));
                number50.setBackgroundResource(R.drawable.dlt_tzback_click);
                realTime.setText("50");
                break;
            case R.id.time100:
                clearTime();
                number100.setTextColor(Color.parseColor("#FF3232"));
                number100.setBackgroundResource(R.drawable.dlt_tzback_click);
                realTime.setText("100");
                break;
            case R.id.time500:
                clearTime();
                number500.setTextColor(Color.parseColor("#FF3232"));
                number500.setBackgroundResource(R.drawable.dlt_tzback_click);
                realTime.setText("500");
                break;
            case R.id.cleartime:
                if (realTime.getText().toString().length() <= 1) {
                    realTime.setText("");
                } else {
                    String realtimeStr = realTime.getText().toString();
                    realTime.setText(realtimeStr.substring(0, realtimeStr.length() - 1));
                }
                break;
            case R.id.suretime:
                timekeybroad.setVisibility(View.GONE);
                loadtime.setVisibility(View.GONE);
                String timeStr = realTime.getText().toString();
                try {
                    timeInt = Integer.parseInt(timeStr);
                    if (timeInt == 0) {
                        timeInt = 1;
                    }
                } catch (Exception e) {
                    timeInt = 1;
                    e.printStackTrace();
                }
                Intent intentPay = new Intent(_this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("lotteryid", ConstantsBase.JCZQ);
                bundle.putLong("totalaccount", cost * timeInt);
                bundle.putBoolean("useRedPacket", false);
                bundle.putString("SchemeBackupsId", SchemeBackupsId);
                bundle.putInt("type", type);
                bundle.putInt("timeInt", timeInt);
                intentPay.putExtras(bundle);
                startActivity(intentPay);
                break;
        }
    }

    protected void setSelect(int i) {

        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                gendanniuren = "bangdan";
                search_txt.setText("搜索方案发起人");
                if (mTab01 == null) {
                    if (savedInstanceState != null) {
                        mTab01 = (BangDanMainFragment) fm.findFragmentByTag("BangDanMainFragment");
                        if (mTab01 == null) {
                            mTab01 = new BangDanMainFragment();
                            transaction.add(R.id.id_content, mTab01, "BangDanMainFragment");
                        }
                    } else {
                        mTab01 = new BangDanMainFragment();
                        transaction.add(R.id.frgament, mTab01, "BangDanMainFragment");
                    }
                } else {
                    transaction.show(mTab01);
                }

                break;
            case 1:
                gendanniuren = "niuren";
                search_txt.setText("搜索牛人");
                if (mTab02 == null) {
                    if (savedInstanceState != null) {
                        mTab02 = (RenZhengMainFragment) fm.findFragmentByTag("RenZhengMainFragment");
                        if (mTab02 == null) {
                            mTab02 = new RenZhengMainFragment();
                            transaction.add(R.id.id_content, mTab02, "RenZhengMainFragment");
                        }
                    } else {
                        mTab02 = new RenZhengMainFragment();
                        transaction.add(R.id.frgament, mTab02, "RenZhengMainFragment");
                    }
                } else {
                    transaction.show(mTab02);
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
                mTab01 = (BangDanMainFragment) fm.findFragmentByTag("BangDanMainFragment");
                if (mTab01 != null) {
                    transaction.hide(mTab01);
                }
            }
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        } else {
            if (savedInstanceState != null) {
                mTab02 = (RenZhengMainFragment) fm.findFragmentByTag("RenZhengMainFragment");
                if (mTab02 != null) {
                    transaction.hide(mTab02);
                }
            }
        }
    }

    public void setTime(String i) {
        clearTime();
        int number = 0;
        try {
            number = Integer.parseInt(realTime.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            number = 0;
        }
        if (number > timeIntMAX || realTime.getText().toString().length() == 5) {
            ToastUtils.showShort(_this, "倍数不能超过" + timeIntMAX);
            realTime.setText(timeIntMAX + "");
        } else {
            if (number == 0) {
                realTime.setText(i);
            } else {
                realTime.setText(number + i);
            }
        }
    }

    public void clearTime() {
        number10.setTextColor(Color.parseColor("#333333"));
        number10.setBackgroundResource(R.drawable.dlt_tzback);
        number25.setTextColor(Color.parseColor("#333333"));
        number25.setBackgroundResource(R.drawable.dlt_tzback);
        number50.setTextColor(Color.parseColor("#333333"));
        number50.setBackgroundResource(R.drawable.dlt_tzback);
        number100.setTextColor(Color.parseColor("#333333"));
        number100.setBackgroundResource(R.drawable.dlt_tzback);
        number500.setTextColor(Color.parseColor("#333333"));
        number500.setBackgroundResource(R.drawable.dlt_tzback);
    }

    public void onEventMainThread(String event) {

        if (!TextUtils.isEmpty(event)) {
            if (!TextUtils.isEmpty(event)
                    && event.startsWith("toPayGenDan")) {
                timekeybroad.setVisibility(View.VISIBLE);
                try {
                    String startStr = event.split(",")[0];
                    SchemeBackupsId = startStr.substring(startStr.indexOf(":") + 1, startStr.length());
                    cost = Long.parseLong(event.split(",")[1]);
                    type = Integer.parseInt(event.split(",")[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(event)
                    && event.startsWith("toGetAds")) {
                initdata();
            }
        }
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void onImageClick(final int position, View imageView) {
            HuanDengsInfo ha = adsList.get(position);
            Intent intent;
            if ("1".equals(ha.getUseLocal())) {
                TOLotteryUtil.getTOLotteryUtil().toUrl(getActivity(), "1", ha.getLotteryId(), "", ha.getPlayType());
            } else if ("0".equals(ha.getUseLocal())) {
                if ("taocan".equals(ha.getUrl().trim())) {
                    Intent intentHelp = new Intent(getActivity(),
                            TaoCanMainActivity.class);
                    startActivity(intentHelp);
                } else {
                    String oldUrl = ha.getUrl();
                    String newUrl = oldUrl.replaceFirst("shouji", "scp");
                    intent = new Intent(getActivity(), LotteryTypeActivity.class);// 跳转html活动界面
                    if (ConstantsBase.IP.equals("http://cp.citycai.com")) {
                        intent.putExtra("url", newUrl);
                    } else {
                        intent.putExtra("url", oldUrl);
                    }
                    if (!adsList.get(position).getUrl().equals("")) {
                        startActivity(intent);
                    }
                }
            }
        }
    };
}
