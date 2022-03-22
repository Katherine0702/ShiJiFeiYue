package com.bojing.gathering.mainfragment.OrderFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bojing.gathering.MainActivity;
import com.bojing.gathering.R;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.OrderInfo;
import com.bojing.gathering.domain.OrderInfoBack;
import com.bojing.gathering.domain.UserInfo;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceUser;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by admin on 2018/10/15.
 */

public class   OrderFragment extends Fragment implements View.OnClickListener {
    Activity _this;
    View view;
    private boolean isTasking = false;
    OrderListAdapter adapter;
    ListView listview;
    ArrayList<OrderInfo> data;

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _this = this.getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.orderfragment, container, false);
            initView(view);
        }
        initData();
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
        adapter = new OrderListAdapter(_this);
        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(data!=null&&data.size()>0){
                    Intent intent = new Intent(_this, OrderDetailActivity.class);
                    intent.putExtra("orderId", data.get(arg2).getOrderNumber());// 标记
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        if (isTasking) {// 任务在运行中不再登入
            return;
        }
        isTasking = true;
        ServiceUser.getInstance().GetOrder(_this, PreferenceUtil.getString(_this, "username"),
                new ServiceABase.CallBack<OrderInfoBack>() {
                    @Override
                    public void onSuccess(OrderInfoBack userResultData) {
                        if(userResultData!=null&&
                                userResultData.getData()!=null&&
                                userResultData.getData().size()>0){
                            data  = userResultData.getData();
                            adapter.setDate(data);
                            adapter.notifyDataSetInvalidated();
                        }
                        isTasking = false;
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        isTasking = false;
                    }
                });
    }
    @Override
    public void onClick(View view) {

    }
}