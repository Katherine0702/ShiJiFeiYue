package com.cshen.tiyu.zx;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.main.TabIndicator;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.widget.TabIndicatorView;
import com.cshen.tiyu.zx.main4.find.ZXFindFragment;
import com.cshen.tiyu.zx.main4.main.ZXNewHomeMainFragment2;
import com.cshen.tiyu.zx.main4.personal.ZXMainPersonalCenterFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class ZXMainActivity extends BaseActivity {


    private ZXNewHomeMainFragment2 mTab01;
    private Fragment mTab02;
    private Fragment mTab04;
    private ArrayList<TabIndicator> tabIndicators = new ArrayList<TabIndicator>();// 数据集合
    private ArrayList<TabIndicatorView> tabIndicatorViews = new ArrayList<TabIndicatorView>();// 数据集合
    private LinearLayout bottom;
    private long firstTime = 0;
    private FragmentManager fm;
    private Bundle savedInstanceState;
    private TextView activity;
    private EditText messageEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        activity = (TextView) findViewById(R.id.activity);
        activity.setVisibility(View.GONE);


        initDate();// 初始化view组件的数据集合

    }

    private void initDate() {
        String[] title = new String[]{"首页", "头条", "我的"};
        int[] ic_common = new int[]{R.mipmap.zx_home_common, R.mipmap.zx_find_common, R.mipmap.zx_personal_comon};
        int[] ic_light = new int[]{R.mipmap.zx_home_light, R.mipmap.zx_find_light, R.mipmap.zx_personal_light};
        // 图片先写死
        for (int i = 0; i < title.length; i++) {
            TabIndicator tabIndicator = new TabIndicator();
            tabIndicator.setTitle(title[i]);
            tabIndicator.setIcon_common(ic_common[i] + "");
            tabIndicator.setIcon_light(ic_light[i] + "");
            tabIndicator.setRank(i + 1);
            tabIndicators.add(tabIndicator);
        }
        initView();

    }

    private void initView() {
        // TODO 数据集合给view集合 根据数据添加view
        if (tabIndicators != null) {
            for (int i = 0; i < tabIndicators.size(); i++) {
                TabIndicator tabIndicator = tabIndicators.get(i);
                TabIndicatorView tabIndicatorView = new TabIndicatorView(this);
                tabIndicatorView.setRank(tabIndicator.getRank());// 设置这个view的位置
                tabIndicatorView.setTabTitle(tabIndicator.getTitle());// 设置view的底部标题
                tabIndicatorView.setTabIcon(tabIndicator.getIcon_common(), tabIndicator.getIcon_light(), true);
                tabIndicatorViews.add(tabIndicatorView);
            }
        }

        // 底部添加view
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        params.weight = 1;

        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i = 0; i < tabIndicatorViews.size(); i++) {
            final TabIndicatorView contactIndicator = tabIndicatorViews.get(i);

            bottom.addView(contactIndicator, params);

            if (contactIndicator.getRank() == 1) {
                contactIndicator.setTabSelected(true);// 第一个被选中
                setSelect(contactIndicator.getRank());// 显示第一个fragment
            } else {
                contactIndicator.setTabSelected(false);
            }

            contactIndicator.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO 点击事件
                    // 如果是第四个被点击并且还没有登入
                    if (!PreferenceUtil.getBoolean(ZXMainActivity.this, "hasLogin") && contactIndicator.getRank() == 3) {
                        // 跳转到登入界面
                        Intent intent = new Intent(ZXMainActivity.this, LoginActivity.class);
                        intent.putExtra("isMaJia", true);//资讯包
                        startActivity(intent);
                    } else {
                        // 重置图片
                        resetImgs();
                        contactIndicator.setTabSelected(true);// 变为高亮
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
                        mTab01 = (ZXNewHomeMainFragment2) fm.findFragmentByTag("ZXNewHomeMainFragment2");
                        if (mTab01 == null) {
                            mTab01 = new ZXNewHomeMainFragment2();
                            transaction.add(R.id.id_content, mTab01, "ZXNewHomeMainFragment2");
                        }
                    } else {
                        mTab01 = new ZXNewHomeMainFragment2();
                        transaction.add(R.id.id_content, mTab01, "ZXNewHomeMainFragment2");
                    }
                } else {
                    transaction.show(mTab01);
                    // 第一个主页要特殊处理貌似变烦了
                }

                break;
            case 2:
                if (mTab02 == null) {
                    if (savedInstanceState != null) {
                        mTab02 = fm.findFragmentByTag("ZXFindFragment");
                        if (mTab02 == null) {
                            mTab02 = new ZXFindFragment();
                            transaction.add(R.id.id_content, mTab02, "ZXFindFragment");
                        }
                    } else {
                        mTab02 = new ZXFindFragment();
                        transaction.add(R.id.id_content, mTab02, "ZXFindFragment");
                    }
                } else {
                    transaction.show(mTab02);
                }
                break;

            case 3:
                if (mTab04 == null) {
                    if (savedInstanceState != null) {
                        mTab04 = fm.findFragmentByTag("ZXMainPersonalCenterFragment");
                        if (mTab04 == null) {
                            mTab04 = new ZXMainPersonalCenterFragment();
                            transaction.add(R.id.id_content, mTab04, "ZXMainPersonalCenterFragment");
                        }
                    } else {
                        mTab04 = new ZXMainPersonalCenterFragment();
                        transaction.add(R.id.id_content, mTab04, "ZXMainPersonalCenterFragment");
                    }
                } else {
                    transaction.show(mTab04);
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
                mTab01 = (ZXNewHomeMainFragment2) fm.findFragmentByTag("ZXNewHomeMainFragment2");
                if (mTab01 != null) {
                    transaction.hide(mTab01);
                }
            }
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        } else {
            if (savedInstanceState != null) {
                mTab02 = (ZXFindFragment) fm.findFragmentByTag("ZXFindFragment");
                if (mTab02 != null) {
                    transaction.hide(mTab02);
                }
            }
        }

        if (mTab04 != null) {
            transaction.hide(mTab04);
        } else {
            if (savedInstanceState != null) {
                mTab04 = fm.findFragmentByTag("ZXMainPersonalCenterFragment");
                if (mTab04 != null) {
                    transaction.hide(mTab04);
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO 自动生成的方法存根
        super.onNewIntent(intent);
        String hasLoginString = intent.getStringExtra("hasLogin");
        String nowPage = intent.getStringExtra("nowPage");

        if (hasLoginString != null) {
            if (hasLoginString.equals("yes")) {
                // 选中第四个fragment
                if (nowPage != null) {
                    if (nowPage.equals("1")) {
                        resetImgs();
                        tabIndicatorViews.get(0).setTabSelected(true);
                        setSelect(1);
                    } else if (nowPage.equals("2")) {
                        resetImgs();
                        tabIndicatorViews.get(1).setTabSelected(true);
                        setSelect(2);
                    }
                } else {
                    resetImgs();
                    tabIndicatorViews.get(2).setTabSelected(true);
                    setSelect(3);
                }
            }
            if (hasLoginString.equals("cancel")) {
                // 选中第一个fragment
                if (tabIndicatorViews != null && tabIndicatorViews.size() > 0) {
                    resetImgs();
                    tabIndicatorViews.get(0).setTabSelected(true);
                }
                setSelect(1);


            }
        }

    }

    // 返回键退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
