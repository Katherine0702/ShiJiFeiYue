package com.cshen.tiyu.activity.mian4.find;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.find.message.MessageActivity;
import com.cshen.tiyu.activity.mian4.find.shareorder.ShareOrderActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.find.FindBean;
import com.cshen.tiyu.domain.find.FindBeanDatas;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.MyGrideview;

import de.greenrobot.event.EventBus;

public class FindFragment extends Fragment {
    private Activity _this;
    ListView refreshListView;
    private ArrayList<FindBean> mDataList = new ArrayList<>();
    private FindGridAdapter mgAdapter;
    private TextView tv_nodata;
    private View mFakeStatusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle sszl) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        _this = this.getActivity();
        EventBus.getDefault().register(this);
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            mFakeStatusBar.setVisibility(View.GONE);
        } else {
            mFakeStatusBar.setVisibility(View.VISIBLE);
        }
        initView(view);// 初使化控件
        initDatas();
        return view;
    }

    private void initDatas() {

        ServiceMain.getInstance().PostGetFindDatas(getActivity(), new CallBack<FindBeanDatas>() {

            @Override
            public void onSuccess(FindBeanDatas findBeanDatas) {
                if (findBeanDatas != null && findBeanDatas.getFindBeans() != null) {
                    mDataList.clear();
                    mDataList.addAll(findBeanDatas.getFindBeans());
                    mgAdapter.notifyDataSetChanged();
                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                ToastUtils.showShort(_this, errorMessage.msg);
            }
        });
    }

    private void initView(View view) {
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        refreshListView = (ListView) view.findViewById(R.id.rl_listview);
        mgAdapter = new FindGridAdapter(mDataList, getActivity());
        refreshListView.setAdapter(mgAdapter);


        refreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String tkey = mDataList.get(arg2).getTkey();
                int useLocal = mDataList.get(arg2).getUseLocal();//0:h5 1:本地
                if (useLocal == 0) {//h5
                    String url = mDataList.get(arg2).getUrl();
                    if(!TextUtils.isEmpty(url)) {
                        Intent intent = new Intent(getActivity(), LiveScoresActivity.class);
                        if ("SSZL".equals(tkey)) {
                            intent.putExtra("url", "https://tb.53kf.com/code/client/10182922/1");
                        } else {
                            intent.putExtra("url", url);
                        }
                        startActivity(intent);
                    }
                } else {//本地
                    switch (tkey) {
                        case "SD"://晒单
                            Intent intent_share = new Intent(getActivity(), ShareOrderActivity.class);
                            startActivity(intent_share);
                            break;
                        case "ZX"://资讯
                            Intent intent_mssage = new Intent(getActivity(), MessageActivity.class);
                            startActivity(intent_mssage);
                            break;
                        case "ZXKJ"://最新开奖
                            Intent intent_open = new Intent(_this, LatestLotteryInfoActivity.class);
                            startActivity(intent_open);
                            break;
                        case "HDLB"://活动列表
                            Intent intent_activity = new Intent(_this, ActivityActivity.class);
                            startActivity(intent_activity);
                            break;
                        case "TAOCAN"://活动列表
                            Intent intent_taocan = new Intent(_this, TaoCanMainActivity.class);
                            startActivity(intent_taocan);
                            break;
                        default:
                            break;

                    }
                }


            }
        });


    }

    public void onEventMainThread(String event) {

        if (!TextUtils.isEmpty(event)) {
            if ("updateFindData".equals(event)) {
                initDatas();
            }

        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
