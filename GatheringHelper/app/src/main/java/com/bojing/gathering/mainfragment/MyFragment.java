package com.bojing.gathering.mainfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bojing.gathering.LoginActivity;
import com.bojing.gathering.MainActivity;
import com.bojing.gathering.R;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.UserInfo;
import com.bojing.gathering.domain.UserInfoBack;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceUser;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2018/10/15.
 */

public class MyFragment extends Fragment {
    Activity _this;
    View view;
    private boolean isTasking = false;
    TextView logout, name, money;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _this = this.getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.myfragment, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_srl);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                 initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        money = view.findViewById(R.id.money);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PreferenceUtil.clearLoginAll(_this);
                Intent intent = new Intent(_this, LoginActivity.class);
                startActivity(intent);
                _this.finish();
            }
        });
        initData();
    }

    public void initData() {
        if (isTasking) {// 任务在运行中不再登入
            return;
        }
        isTasking = true;
        ServiceUser.getInstance().GetInfo(_this, PreferenceUtil.getString(_this, "username"),
                new ServiceABase.CallBack<UserInfoBack>() {
                    @Override
                    public void onSuccess(UserInfoBack userResultData) {
                        if (userResultData != null &&
                                userResultData.getData() != null &&
                                userResultData.getData().getAccounts() != null &&
                                userResultData.getData().getAccounts().size() > 0) {
                            for (int i = 0; i < userResultData.getData().getAccounts().size(); i++) {
                                if ("1".equals(userResultData.getData().getAccounts().get(i).getAccountType())) {
                                    PreferenceUtil.putString(_this, "zhifubaoAccountNo"
                                            , userResultData.getData().getAccounts().get(i).getAccountNo());
                                    PreferenceUtil.putString(_this, "zhifubaoProviderId"
                                            , userResultData.getData().getAccounts().get(i).getProviderId());
                                }
                                if ("2".equals(userResultData.getData().getAccounts().get(i).getAccountType())) {
                                    PreferenceUtil.putString(_this, "weixinAccountNo"
                                            , userResultData.getData().getAccounts().get(i).getAccountNo());
                                    PreferenceUtil.putString(_this, "weixinProviderId"
                                            , userResultData.getData().getAccounts().get(i).getProviderId());
                                }
                            }
                        }
                        showView(userResultData);
                        isTasking = false;
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        isTasking = false;
                    }
                });
    }

    public void showView(UserInfoBack userResultData) {
        name.setText(userResultData.getData().getDevCode());
        money.setText(userResultData.getData().getBalance());
    }
}
