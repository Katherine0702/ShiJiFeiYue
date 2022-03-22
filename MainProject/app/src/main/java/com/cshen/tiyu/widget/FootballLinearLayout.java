package com.cshen.tiyu.widget;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.net.https.xUtilsImageUtils;


/**
 * Created by Administrator on 2017/8/25.
 */

public class FootballLinearLayout extends LinearLayout {

    private LinearLayout ll_host, ll_flat, ll_guest;
    private ImageView iv_football_host, iv_football_guest;// 球队图标
    private TextView tv_host_name, tv_host_odds;// 主队胜
    private TextView tv_flat_odds; // 平
    private TextView tv_guest_name, tv_guest_odds; // 客队胜
    private float odd = 0, odd1 = 0, odd2 = 0,odd3 =0;
    private TextView tv_value01, tv_value02, tv_value03, tv_value04;
    private int beishu = 50;
    private TextView tv_betMoney;// 投注金额
    private View tv_football_more;// 更多比赛
    private boolean hostChoose = false, flatChoose = false, guestChoose = false;
    private FootBallMatch mFootBean;
    private onBetClickListener mListener;
    private DecimalFormat df;
    public FootballLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(
                R.layout.item_football_linearlayout, this);

        df = new DecimalFormat("0.00");
        initViews();
    }

    public void setFirstChecked() {
        //默认显示第一条数据,刷新时重新设置一些变量
        hostChoose = false;
        guestChoose = false;
        flatChoose = false;
        resetResourceColor();
        setbetMoney();
        setCheckNumber();
    }

    private void initViews() {
        tv_football_more = findViewById(R.id.tv_football_moreMatch);
        ll_host = (LinearLayout) findViewById(R.id.ll_host);
        ll_flat = (LinearLayout) findViewById(R.id.ll_flat);
        ll_guest = (LinearLayout) findViewById(R.id.ll_guest);

        iv_football_host = (ImageView) findViewById(R.id.iv_football_host);
        tv_host_name = (TextView) findViewById(R.id.tv_host_name);
        tv_host_odds = (TextView) findViewById(R.id.tv_host_odds);

        tv_flat_odds = (TextView) findViewById(R.id.tv_flat_odds);

        iv_football_guest = (ImageView) findViewById(R.id.iv_football_guest);
        tv_guest_name = (TextView) findViewById(R.id.tv_guest_name);
        tv_guest_odds = (TextView) findViewById(R.id.tv_guest_odds);

        tv_value01 = (TextView) findViewById(R.id.tv_value01);
        tv_value02 = (TextView) findViewById(R.id.tv_value02);
        tv_value03 = (TextView) findViewById(R.id.tv_value03);
        tv_value04 = (TextView) findViewById(R.id.tv_value04);

        tv_betMoney = (TextView) findViewById(R.id.tv_betMoney);


        ll_host.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hostChoose) {
                    hostChoose = false;
                    odd = 0;
                    ll_host.setBackgroundResource(R.mipmap.left);
                } else {
                    hostChoose = true;
                    odd = odd1;
                    ll_host.setBackgroundResource(R.mipmap.left1);


                    guestChoose = false;
                    flatChoose = false;
                    ll_flat.setBackgroundResource(R.mipmap.middle);
                    ll_guest.setBackgroundResource(R.mipmap.right);

                }
                setbetMoney();
                setCheckNumber();
            }
        });

        ll_flat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flatChoose) {
                    flatChoose = false;
                    odd = 0;
                    ll_flat.setBackgroundResource(R.mipmap.middle);
                } else {
                    flatChoose = true;
                    odd = odd3;
                    ll_flat.setBackgroundResource(R.mipmap.middle1);


                    hostChoose = false;
                    guestChoose = false;
                    ll_host.setBackgroundResource(R.mipmap.left);
                    ll_guest.setBackgroundResource(R.mipmap.right);
                }
                setbetMoney();
                setCheckNumber();
            }
        });

        ll_guest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (guestChoose) {
                    guestChoose = false;
                    odd = 0;
                    ll_guest.setBackgroundResource(R.mipmap.right);
                } else {
                    guestChoose = true;
                    odd = odd2;
                    ll_guest.setBackgroundResource(R.mipmap.right1);


                    hostChoose = false;
                    flatChoose = false;
                    ll_host.setBackgroundResource(R.mipmap.left);
                    ll_flat.setBackgroundResource(R.mipmap.middle);
                }
                setbetMoney();
                setCheckNumber();

            }
        });
        tv_value01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                beishuView(tv_value01);
            }
        });
        tv_value02.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                beishuView(tv_value02);
            }
        });
        tv_value03.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                beishuView(tv_value03);
            }
        });
        tv_value04.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                beishuView(tv_value04);
            }
        });
        tv_betMoney.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if( hostChoose||guestChoose||flatChoose ) {
                        mListener.onBetClick(2 * beishu);
                    }else{
                        mListener.onBetClick(0);
                    }
                }

            }
        });

        tv_football_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMoreMatchClick();
                }
            }
        });

    }

    public void setonBetClickListener(onBetClickListener mListener) {
        this.mListener = mListener;
    }

    public interface onBetClickListener {
        void onBetClick(int checkedResult);

        void onMoreMatchClick();
    }

    public void setDatas(FootBallMatch match) {
        this.mFootBean = match;
        if (mFootBean == null || mFootBean.getSp() == null)
            return;
        String host_odds = mFootBean.getSp().getSPF().getWIN().getValue();
        String flat_odds = mFootBean.getSp().getSPF().getDRAW().getValue();
        String guest_odds = mFootBean.getSp().getSPF().getLOSE().getValue();

        tv_host_name.setText(mFootBean.getHomeTeamName() == null ? ""
                : mFootBean.getHomeTeamName());
        tv_host_odds.setText(host_odds == null ? "" : "胜  " + host_odds);

        tv_flat_odds.setText(flat_odds == null ? "" : "平  " + flat_odds);

        tv_guest_name.setText(mFootBean.getGuestTeamName() == null ? ""
                : mFootBean.getGuestTeamName());
        tv_guest_odds.setText(guest_odds == null ? "" : "胜  " + guest_odds);

        xUtilsImageUtils.displayIN(iv_football_host, R.mipmap.default_football,
                mFootBean.getHomeTeamImage());
        xUtilsImageUtils.displayIN(iv_football_guest, R.mipmap.default_football,
                mFootBean.getGuestTeamImage());
        try {
            odd3 = Float.parseFloat(flat_odds);
            odd1 = Float.parseFloat(host_odds);
            odd2 = Float.parseFloat(guest_odds);
            if (odd1 <= odd2) {
                hostChoose = true;
                odd = odd1;
                ll_host.setBackgroundResource(R.mipmap.left1);
            } else {
                guestChoose = true;
                odd = odd2;
                ll_guest.setBackgroundResource(R.mipmap.right1);
            }
            setbetMoney();
            setCheckNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FootBallMatch getDatas() {
        return mFootBean;
    }


    public void setCheckNumber() {
        if(mFootBean == null){
            return;
        }
        boolean isHas0 = false;
        boolean isHas1 = false;
        boolean isHas2 = false;
        if (mFootBean.getCheckNumber() == null) {
            mFootBean.setCheckNumber(new ArrayList<ScroeBean>());
        }
        for (int i = 0; i < mFootBean.getCheckNumber().size(); i++) {
            if (mFootBean.getCheckNumber().get(i).getKey()
                    .equals(mFootBean.getSp().getSPF().getWIN().getKey())) {
                if (!hostChoose) {// 存在且不该存在
                    mFootBean.getCheckNumber().remove(i);
                }
                isHas0 = true;
                break;
            }
        }
        if (!isHas0 && (hostChoose)) {// 不存在，且该存在
            mFootBean.getCheckNumber().add(mFootBean.getSp().getSPF().getWIN());
        }
        for (int i = 0; i < mFootBean.getCheckNumber().size(); i++) {
            if (mFootBean.getCheckNumber().get(i).getKey()
                    .equals(mFootBean.getSp().getSPF().getDRAW().getKey())) {
                if (!flatChoose) {// 存在且不该存在
                    mFootBean.getCheckNumber().remove(i);
                }
                isHas1 = true;
                break;
            }
        }
        if (!isHas1 && flatChoose) {// 不存在，且该存在
            mFootBean.getCheckNumber().add(mFootBean.getSp().getSPF().getDRAW());
        }
        for (int i = 0; i < mFootBean.getCheckNumber().size(); i++) {
            if (mFootBean.getCheckNumber().get(i).getKey()
                    .equals(mFootBean.getSp().getSPF().getLOSE().getKey())) {
                if (!guestChoose) {// 存在且不该存在
                    mFootBean.getCheckNumber().remove(i);
                }
                isHas2 = true;
                break;
            }
        }
        if (!isHas2 && guestChoose) {// 不存在，且该存在
            mFootBean.getCheckNumber().add(mFootBean.getSp().getSPF().getLOSE());
        }
    }

    public void setbetMoney() {
        if( hostChoose||guestChoose||flatChoose ) {

            String jiangjin  = df.format(2 * beishu * odd);
            tv_betMoney.setText("投注" + 2 * beishu + "元狂中" + jiangjin  + "元");
        }else{
            tv_betMoney.setText("投注0元狂中0元");
        }
    }

    public void resetResourceColor() {
        ll_host.setBackgroundResource(R.mipmap.left);
        ll_flat.setBackgroundResource(R.mipmap.middle);
        ll_guest.setBackgroundResource(R.mipmap.right);
    }

    public void beishuView(View v) {
        int left1 = tv_value01.getPaddingLeft();
        int right1 = tv_value01.getPaddingRight();
        int top1 = tv_value01.getPaddingTop();
        int bottom1 = tv_value01.getPaddingBottom();
        int left2 = tv_value02.getPaddingLeft();
        int right2 = tv_value02.getPaddingRight();
        int top2 = tv_value02.getPaddingTop();
        int bottom2 = tv_value02.getPaddingBottom();
        int left3 = tv_value03.getPaddingLeft();
        int right3 = tv_value03.getPaddingRight();
        int top3 = tv_value03.getPaddingTop();
        int bottom3 = tv_value03.getPaddingBottom();
        int left4 = tv_value04.getPaddingLeft();
        int right4 = tv_value04.getPaddingRight();
        int top4 = tv_value04.getPaddingTop();
        int bottom4 = tv_value04.getPaddingBottom();

        tv_value01.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
        tv_value01.setTextColor(getResources().getColor(R.color.black));
        tv_value01.setPadding(left1, top1, right1, bottom1);
        tv_value02.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
        tv_value02.setTextColor(getResources().getColor(R.color.black));
        tv_value02.setPadding(left2, top2, right2, bottom2);
        tv_value03.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
        tv_value03.setTextColor(getResources().getColor(R.color.black));
        tv_value03.setPadding(left3, top3, right3, bottom3);
        tv_value04.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
        tv_value04.setTextColor(getResources().getColor(R.color.black));
        tv_value04.setPadding(left4, top4, right4, bottom4);


        v.setBackgroundResource(R.drawable.cornernonelinefull_redline_nofull);
        ((TextView) v).setTextColor(getResources().getColor(R.color.mainred));
        if (v == tv_value01) {
            beishu = 5;
            tv_value01.setPadding(left1, top1, right1, bottom1);
        }
        if (v == tv_value02) {
            beishu = 25;
            tv_value02.setPadding(left2, top2, right2, bottom2);
        }
        if (v == tv_value03) {
            beishu = 50;
            tv_value03.setPadding(left3, top3, right3, bottom3);
        }
        if (v == tv_value04) {
            beishu = 250;
            tv_value04.setPadding(left4, top4, right4, bottom4);
        }
        setbetMoney();
    }

}
