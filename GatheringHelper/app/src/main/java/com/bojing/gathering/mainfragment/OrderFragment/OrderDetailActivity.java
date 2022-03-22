package com.bojing.gathering.mainfragment.OrderFragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.OrderDetailBack;
import com.bojing.gathering.domain.OrderInfoBack;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceUser;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;

/**
 * Created by admin on 2018/10/16.
 */

public class OrderDetailActivity extends Activity {
    OrderDetailActivity _this;
    View back;
    TextView num, state, createtime, time, payer, paynumber;
    private boolean isTasking = false;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail);
        _this = this;
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _this.finish();
            }
        });
        num = (TextView) findViewById(R.id.order_num_tv);
        state = (TextView) findViewById(R.id.order_state_tv);
        createtime = (TextView) findViewById(R.id.create_time_tv);
        time = (TextView) findViewById(R.id.order_time_tv);
        payer = (TextView) findViewById(R.id.order_payer_tv);
        paynumber = (TextView) findViewById(R.id.order_paynumber_tv);
        initData();
    }

    private void initData() {
        if (isTasking) {// 任务在运行中不再登入
            return;
        }
        isTasking = true;
        ServiceUser.getInstance().GetOrderDetail(_this, PreferenceUtil.getString(_this, "username"), orderId,
                new ServiceABase.CallBack<OrderDetailBack>() {
                    @Override
                    public void onSuccess(OrderDetailBack userResultData) {
                        if (userResultData != null &&
                                userResultData.getData() != null) {
                            if (TextUtils.isEmpty(userResultData.getData().getOrderNumber())) {
                                num.setText("暂无");
                            } else {
                                num.setText(userResultData.getData().getOrderNumber());
                            }
                            if (TextUtils.isEmpty(userResultData.getData().getOrderNumber())) {
                                state.setText("暂无");
                            } else {
                                switch (userResultData.getData().getOrderStatus()) {
                                    case "1":
                                        state.setText("待支付");
                                        break;
                                    case "2":
                                        state.setText("已支付");
                                        break;
                                    default:
                                        state.setText("暂无");
                                        break;
                                }
                            }
                            if (TextUtils.isEmpty(userResultData.getData().getPayDate())) {
                                time.setText("暂无");
                            } else {
                                time.setText(userResultData.getData().getPayDate());
                            }
                            if (TextUtils.isEmpty(userResultData.getData().getCreateDate())) {
                                createtime.setText("暂无");
                            } else {
                                createtime.setText(userResultData.getData().getCreateDate());
                            }
                            if (TextUtils.isEmpty(userResultData.getData().getPayer())) {
                                payer.setText("暂无");
                            } else {
                                payer.setText(userResultData.getData().getPayer());
                            }
                            isTasking = false;
                        }
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        isTasking = false;
                    }
                });
    }
}
