package com.cshen.tiyu.activity.mian4.personcenter;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.InformationDetailActivity;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BankCardActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.main.NewsBean;
import com.cshen.tiyu.net.https.ServiceMessage;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SecurityUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.LooperTextView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;

public class DrawingsActivity extends BaseActivity implements OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private boolean isTasking = false;

    private VerticalSwipeRefreshLayout mSwipeLayout;
    private ScrollView scrollview;
    private ImageView back;
    // 消息通知
    private RelativeLayout rl_news;
    private LooperTextView looperview;
    private TextView looperview1;
    private TextView tv_update_bankCard, shuoming;
    private TextView tv_remain_money, tv_drawable_money, tv_bank_name,
            tv_bank_card, tv_real_name;
    private EditText et_amount, et_payPassword;
    private User user;
    private UserInfo userInfo;
    private TextView btn_sure;
    private boolean hasBindBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawings);


        mContext = this;
        hasBindBankCard = PreferenceUtil
                .getBoolean(mContext, "hasBindBankCard");
        user = MyDbUtils.getCurrentUser();
        userInfo = MyDbUtils.getCurrentUserInfo();
        initView();
        initNewsData();// 公告
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        shuoming = (TextView) findViewById(R.id.iv_setting);
        shuoming.setOnClickListener(this);

        mSwipeLayout = (VerticalSwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        scrollview = (ScrollView) findViewById(R.id.myScrollView);
        scrollview.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        mSwipeLayout.setEnabled(scrollview.getScrollY() == 0);
                    }
                });
        scrollview.smoothScrollTo(0, 20);
        /******************************************/
        rl_news = (RelativeLayout) findViewById(R.id.rl_news);
        looperview = (LooperTextView) findViewById(R.id.looperview);
        looperview1 = (TextView) findViewById(R.id.looperview1);
        looperview1.setFocusable(true);
        looperview1.setSelected(true);
        looperview1.setOnClickListener(this);

        tv_remain_money = (TextView) findViewById(R.id.tv_remain_money);
        tv_drawable_money = (TextView) findViewById(R.id.tv_drawable_money);
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        tv_bank_card = (TextView) findViewById(R.id.tv_bank_card);
        tv_real_name = (TextView) findViewById(R.id.tv_real_name);
        et_amount = (EditText) findViewById(R.id.et_amount);
        et_payPassword = (EditText) findViewById(R.id.et_payPassword);
        btn_sure = (TextView) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
        if (userInfo != null) {
            String remainAccount = userInfo.getRemainMoney();
            String frozenAccount = userInfo.getFrozenMoney();
            if (TextUtils.isEmpty(remainAccount)) {
                tv_remain_money.setText("0元");
                tv_drawable_money.setText("0元");
            } else {
                if (TextUtils.isEmpty(frozenAccount)) {
                    tv_remain_money.setText(remainAccount + "元");
                    tv_drawable_money.setText(remainAccount + "元");
                } else {
                    BigDecimal remainAccountF = new BigDecimal(remainAccount);
                    BigDecimal frozenAccountF = new BigDecimal(frozenAccount);
                    float canUse = remainAccountF.subtract(frozenAccountF).floatValue();
                    tv_remain_money.setText(remainAccount + "元");
                    if (canUse == 0.0) {
                        tv_drawable_money.setText("0元");
                    } else {
                        int one = (int) canUse;
                        if (Float.parseFloat((one + "")) == canUse) {
                            tv_drawable_money.setText(one + "元");
                        } else {
                            tv_drawable_money.setText(canUse + "元");
                        }
                    }
                }
            }

            if (hasBindBankCard) {
                tv_bank_name.setText(userInfo.getBankName());
                if ((!TextUtils.isEmpty(userInfo.getBankCard()))
                        && userInfo.getBankCard().length() > 15) {
                    tv_bank_card.setText(new StringBuilder(userInfo
                            .getBankCard()).replace(3, 15, "************"));
                }
            }

            String realName = userInfo.getRealName();
            if (!TextUtils.isEmpty(realName)) {
                StringBuilder realNameBuilder = new StringBuilder(
                        realName.subSequence(0, 1));
                for (int i = 0; i < realName.length() - 1; i++) {
                    realNameBuilder.append("*");
                    tv_real_name.setText(realNameBuilder);
                }
            }
        }

        tv_update_bankCard = (TextView) findViewById(R.id.drawings_tv_update_bankCard);
        tv_update_bankCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (isTasking) {// 任务在运行中不再登入
            return;
        }
        if (v == back) {
            DrawingsActivity.this.finish();
        }
        if (v == shuoming) {
            Intent intentHelp = new Intent(DrawingsActivity.this, LotteryTypeActivity.class);
            intentHelp.putExtra("url", "http://www.wocai188.com/drawing.html");
            startActivity(intentHelp);
        }
        if (v == tv_update_bankCard) {
            Intent intent = new Intent(DrawingsActivity.this,
                    BankCardActivity.class);
            startActivityForResult(intent, 1);
        }
        if (v == btn_sure) {
            String amount = et_amount.getText().toString().trim();
            String payPassword = et_payPassword.getText().toString();
            String payPasswordCode = PreferenceUtil.getString(
                    CaiPiaoApplication.getmContext(), "payPassword");
            if (!PreferenceUtil.getBoolean(mContext, "hasBindBankCard")) {
                ToastUtils.showShort(mContext, "亲，请您先绑定银行卡后再进行提款!");
                et_amount.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(amount)) {
                ToastUtils.showShort(mContext, "亲，提款金额不能为空哟!");
                et_amount.requestFocus();
                return;
            }
            if (!Util.isDollar(amount)) {
                ToastUtils.showShort(mContext, "输入金额格式不正确");
                et_amount.requestFocus();
                return;
            }
            /*if (new BigDecimal(amount).compareTo(new BigDecimal(20)) == -1) {
                ToastUtils.showShort(mContext, "亲，提款金额不能小于20元哦！");
                et_amount.requestFocus();
                return;
            }*/
            try {
                String amountFS = tv_drawable_money.getText().toString();
                if (amountFS.contains("元")) {
                    amountFS = amountFS.replace("元", "");
                }
                float amountF = Float.parseFloat(amount);
                float amountFF = Float.parseFloat(amountFS);
                if (amountF > amountFF) {
                    ToastUtils.showShort(mContext, "亲，提款金额不能大于可提金额哦！");
                    et_amount.requestFocus();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(mContext, "亲，可提金额有问题哦！");
                return;
            }

            if (TextUtils.isEmpty(payPasswordCode)) {
                ToastUtils.showShort(mContext, "亲，请您先设置支付密码再进行提款");
                return;
            }
            if (TextUtils.isEmpty(payPassword)) {
                ToastUtils.showShort(mContext, "亲，支付密码不能为空哟！");
                et_payPassword.requestFocus();
                return;
            }
            if (!payPasswordCode.equals(SecurityUtil.md5(payPassword)
                    .toUpperCase())) {
                ToastUtils.showShort(mContext, "支付密码错误");
                et_payPassword.requestFocus();
                return;
            }

            attemptPostDistll(amount);

        }
    }

    private void attemptPostDistll(final String amount) {
        isTasking = true;
        ServiceUser.getInstance().PostDistll(mContext, amount, "1",
                new CallBack<String>() {
                    @Override
                    public void onSuccess(String remainMoney) {

                        ToastUtils.showShort(mContext, "提款成功");
                        isTasking = false;
                        Intent data = new Intent();
                        data.putExtra("remainMoney", amount);
                        setResult(1, data);
                        finish();
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        PostHttpInfoUtils.doPostFail(mContext, errorMessage, "提款失败");
                        isTasking = false;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String bankName = data.getStringExtra("bankName");
                String bankCard = data.getStringExtra("bankCard");
                if (bankCard != null && bankCard.length() > 15) {
                    tv_bank_card.setText(new StringBuilder(bankCard).replace(3, 15,
                            "************"));
                }
                if (!TextUtils.isEmpty(bankName)) {
                    tv_bank_name.setText(bankName);
                }

                String realName = data.getStringExtra("realName");
                if (realName != null && realName.length() > 0) {
                    StringBuilder realNameBuilder = new StringBuilder(
                            realName.subSequence(0, 1));
                    for (int i = 0; i < realName.length() - 1; i++) {
                        realNameBuilder.append("*");
                    }
                    tv_real_name.setText(realNameBuilder);
                }

            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something...
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 公告
     */
    private void initNewsData() {
        ServiceMessage.getInstance().PostGetDrawing(mContext, 6, 0, 1, true, new CallBack<NewsBean>() {

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

    }
    public void setInformation(final ArrayList<NewsBean.NewslistBean> informations) {
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

                        Intent intent_newDetail = new Intent(mContext, MessageDetailActivity.class);
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
                                Intent intentHelp = new Intent(mContext, MessageDetailActivity.class);
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
                                Intent intentHelp = new Intent(mContext, MessageDetailActivity.class);
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

    @Override
    public void onRefresh() {
        initNewsData();// 公告
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            // 停止刷新
            mSwipeLayout.setRefreshing(false);
        }
    }, 3000); // 5秒后发送消息，停止刷新
    }
}
