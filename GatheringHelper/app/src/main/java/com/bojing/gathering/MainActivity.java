package com.bojing.gathering;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bojing.gathering.base.MyNotificationListenService;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.InfoDetail;
import com.bojing.gathering.mainfragment.MaKuFragment.MaKuFragment;
import com.bojing.gathering.mainfragment.MyFragment;
import com.bojing.gathering.mainfragment.OrderFragment.OrderFragment;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceMain;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.widget.TabIndicatorView;

import java.util.ArrayList;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {
    MainActivity _this;
    private MyFragment mTab01;
    private MaKuFragment mTab02;
    private OrderFragment mTab03;
    LinearLayout bottom;
    ArrayList<TabIndicatorView> tabIndicatorViews = new ArrayList<TabIndicatorView>();
    FragmentManager fm;
    Bundle savedInstanceState;

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        _this = this;
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        message = (TextView) findViewById(R.id.message);
        EventBus.getDefault().register(this);
        startNotificationListenService();
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDetail infoDetail = new InfoDetail();
                infoDetail.setCreateTime("2018年10月25日 17:26");
                infoDetail.setPaymentId(1);
                infoDetail.setTotalFee("1");
                infoDetail.setTxNoStatus("success");
                infoDetail.setProviderAccount(PreferenceUtil.getString(_this,"zhifubaoAccountNo"));
                infoDetail.setProviderId(PreferenceUtil.getString(_this,"zhifubaoProviderId"));
                putMessage(infoDetail);
            }
        });
        initView();
    }
    private void initView() {
        bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        TabIndicatorView tabIndicatorView1 = new TabIndicatorView(this);
        TabIndicatorView tabIndicatorView2 = new TabIndicatorView(this);
        TabIndicatorView tabIndicatorView3 = new TabIndicatorView(this);
        tabIndicatorView1.setTabTitle("我的");
        tabIndicatorView1.setRank(1);
        tabIndicatorView1.setTabIcon(R.mipmap.wode+"", R.mipmap.wode1+"", true);
        tabIndicatorViews.add(tabIndicatorView1);
        tabIndicatorView2.setTabTitle("码库");
        tabIndicatorView2.setRank(2);
        tabIndicatorView2.setTabIcon(R.mipmap.erweima+"", R.mipmap.erweima1+"", true);
        tabIndicatorViews.add(tabIndicatorView2);
        tabIndicatorView3.setTabTitle("订单");
        tabIndicatorView3.setRank(3);
        tabIndicatorView3.setTabIcon(R.mipmap.dindan+"", R.mipmap.dindan1+"", true);
        tabIndicatorViews.add(tabIndicatorView3);

        // 搴曢儴娣诲姞view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight =1;
        params.gravity =Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < tabIndicatorViews.size(); i++){
            final TabIndicatorView contactIndicator = tabIndicatorViews.get(i);
            bottom.addView(contactIndicator, params);
            if (contactIndicator.getRank() == 1) {
                contactIndicator.setTabSelected(true);
                setSelect(contactIndicator.getRank());
            } else {
                contactIndicator.setTabSelected(false);
            }
            contactIndicator.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!PreferenceUtil.getBoolean(MainActivity.this,
                            "hasLogin") && contactIndicator.getRank() == 3) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
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
    protected void setSelect(int i) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 1:
                if (mTab01 == null) {
                    if (savedInstanceState != null) {
                        mTab01 = (MyFragment) fm.findFragmentByTag("MyFragment");
                        if (mTab01 == null) {
                            mTab01 = new MyFragment();
                            transaction.add(R.id.id_content, mTab01, "MyFragment");
                        }
                    } else {
                        mTab01 = new MyFragment();
                        transaction.add(R.id.id_content, mTab01, "MyFragment");
                    }
                } else {
                    transaction.show(mTab01);
                }
                break;
            case 2:
                if (mTab02 == null) {
                    if (savedInstanceState != null) {
                        mTab02 = (MaKuFragment) fm.findFragmentByTag("MaKuFragment");
                        if (mTab02 == null) {
                            mTab02 = new MaKuFragment();
                            transaction.add(R.id.id_content, mTab02, "MaKuFragment");
                        }
                    } else {
                        mTab02 = new MaKuFragment();
                        transaction.add(R.id.id_content, mTab02, "MaKuFragment");
                    }
                } else {
                    transaction.show(mTab02);
                }
                break;
            case 3:
                if (mTab03 == null) {
                    if (savedInstanceState != null) {
                        mTab03 = (OrderFragment) fm.findFragmentByTag("OrderFragment");
                        if (mTab03 == null) {
                            mTab03 = new OrderFragment();
                            transaction.add(R.id.id_content, mTab03, "OrderFragment");
                        }
                    } else {
                        mTab03 = new OrderFragment();
                        transaction.add(R.id.id_content, mTab03, "OrderFragment");
                    }
                } else {
                    transaction.show(mTab03);

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
                mTab01 = (MyFragment) fm.findFragmentByTag("MyFragment");
                if (mTab01 != null) {
                    transaction.hide(mTab01);
                }
            }
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        } else {
            if (savedInstanceState != null) {
                mTab02 = (MaKuFragment) fm.findFragmentByTag("MaKuFragment");
                if (mTab02 != null) {
                    transaction.hide(mTab02);
                }
            }
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        } else {
            if (savedInstanceState != null) {
                mTab03 = (OrderFragment) fm.findFragmentByTag("OrderFragment");
                if (mTab03 != null) {
                    transaction.hide(mTab03);
                }
            }
        }
    }
    private void startNotificationListenService() {
        if (!isNotificationListenerEnabled(this)) {
            openNotificationAccess();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Intent intent = new Intent(this, MyNotificationListenService.class);
            startService(intent);
        } else {
            Toast.makeText(this, "手机的系统不支持此功能", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    private void openNotificationAccess() {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    public void onEventMainThread(InfoDetail infoDetail) {
        message.setText(infoDetail.getBuyerName() + ":" + infoDetail.getTotalFee() + "," + infoDetail.getCreateTime());
        putMessage(infoDetail);
    }

    private void putMessage(InfoDetail infoDetail) {
        ServiceMain.getInstance().PostInfo(MainActivity.this, infoDetail, new ServiceABase.CallBack<Back>() {
            @Override
            public void onSuccess(Back t) {
                // TODO 自动生成的方法存�?

            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                // TODO 自动生成的方法存�?
                Toast.makeText(MainActivity.this, errorMessage.msg,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
