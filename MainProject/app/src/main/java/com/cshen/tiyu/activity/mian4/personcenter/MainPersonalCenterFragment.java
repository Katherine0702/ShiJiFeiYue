package com.cshen.tiyu.activity.mian4.personcenter;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.TOLotteryUtil;
import com.cshen.tiyu.activity.mian4.find.ActivityActivity;
import com.cshen.tiyu.activity.mian4.find.FindGridAdapter;
import com.cshen.tiyu.activity.mian4.find.LatestLotteryInfoActivity;
import com.cshen.tiyu.activity.mian4.find.LiveScoresActivity;
import com.cshen.tiyu.activity.mian4.find.message.MessageActivity;
import com.cshen.tiyu.activity.mian4.find.shareorder.ShareOrderActivity;
import com.cshen.tiyu.activity.mian4.main.SignActivity;
import com.cshen.tiyu.activity.mian4.personcenter.account.AccountDetailActivity;
import com.cshen.tiyu.activity.mian4.personcenter.message.MessageListActivity;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersActivity;
import com.cshen.tiyu.activity.mian4.personcenter.redpacket.RedPacketActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.SafeSettingActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.SettingActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BankCardActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.PayPwdActivity;
import com.cshen.tiyu.activity.mian4.personcenter.zhuihao.ZhuiHaoMainActivity;
import com.cshen.tiyu.activity.pay.PayMoneyActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Attachment;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Level;
import com.cshen.tiyu.domain.find.FindBean;
import com.cshen.tiyu.domain.find.FindBeanDatas;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServicePay;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceVersion;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.BitmapUtil;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.ImageHelper;
import com.cshen.tiyu.utils.ImageZoom;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.CommonPPW;
import com.cshen.tiyu.widget.PersonalItemView;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import de.greenrobot.event.EventBus;

public class MainPersonalCenterFragment extends Fragment
        implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Activity _this;
    private View sign, kefu;
    private View weirenzheng, renzheng;
    private ImageView namePic;
    private ImageView name_iv;
    private TextView tv_userName;
    private View pv_balance1, pv_balance2;
    private TextView balanceView_tv;
    private View rechargeItemView;//充值
    private View drawingsItemView;//提取
    private View redpacketItemView;
    private ScrollView scrollview;
    private VerticalSwipeRefreshLayout mSwipeLayout;

    private PopupWindow pop;
    String showimageFileName, strImgPath;//拍照名字和路径
    private ProgressDialog mDialog;
    private View load;

    com.cshen.tiyu.widget.MyListView refreshListView;
    private ArrayList<FindBean> mDataList = new ArrayList<>();
    private MyAdapter mgAdapter;
    private boolean isShowOtherPPW = false;//是否显示其他弹窗

    View mFakeStatusBar;
    // private PersonalItemView orders,adds,redpacketView,account,safe,set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_center_fragment2, container, false);
        _this = this.getActivity();
        EventBus.getDefault().register(this);
        initView(view);
        initValue();
        return view;
    }

    private void initView(View view) {
        mFakeStatusBar = view.findViewById(R.id.fake_status_bar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            mFakeStatusBar.setVisibility(View.GONE);
        } else {
            mFakeStatusBar.setVisibility(View.VISIBLE);
        }
        mSwipeLayout = (VerticalSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mSwipeLayout.setEnabled(scrollview.getScrollY() == 0);
            }
        });
        scrollview.smoothScrollTo(0, 20);
        sign = view.findViewById(R.id.sign);
        sign.setOnClickListener(this);
        kefu = view.findViewById(R.id.kefu);
        kefu.setOnClickListener(this);
        weirenzheng = view.findViewById(R.id.weirenzheng);
        weirenzheng.setOnClickListener(this);
        renzheng = view.findViewById(R.id.renzheng);
        namePic = (ImageView) view.findViewById(R.id.name_im);
        namePic.setOnClickListener(this);
        name_iv = (ImageView) view.findViewById(R.id.name_iv);
        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        pv_balance1 = view.findViewById(R.id.pv_balance1);
        pv_balance1.setOnClickListener(this);
        pv_balance2 = view.findViewById(R.id.pv_balance2);
        pv_balance2.setOnClickListener(this);
        balanceView_tv = (TextView) view.findViewById(R.id.pv_balance_tv);
        rechargeItemView = view.findViewById(R.id.pv_recharge);
        rechargeItemView.setOnClickListener(this);
        drawingsItemView = view.findViewById(R.id.pv_drawings);
        drawingsItemView.setOnClickListener(this);
        redpacketItemView = view.findViewById(R.id.redpacketItemView);
        redpacketItemView.setOnClickListener(this);


        refreshListView = (com.cshen.tiyu.widget.MyListView) view.findViewById(R.id.rl_listview);
        mgAdapter = new MyAdapter(mDataList, getActivity(), true);
        refreshListView.setAdapter(mgAdapter);


        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String tkey = mDataList.get(arg2).getTkey();
                int useLocal = mDataList.get(arg2).getUseLocal();//0:h5 1:本地
                if (useLocal == 0) {//h5
                    String url = mDataList.get(arg2).getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        Intent intent = new Intent(getActivity(), LiveScoresActivity.class);
                        if ("SSZL".equals(tkey)) {
                            intent.putExtra("url", "https://tb.53kf.com/code/client/10171088/1");
                        } else if ("JFZX".equals(tkey)) {
                            intent.putExtra("url", url + MyDbUtils.getCurrentUser().getUserId());
                        } else {
                            intent.putExtra("url", url);
                        }
                        startActivity(intent);
                    }
                } else {//本地
                    switch (tkey) {
                        case "GCJL":
                            Intent order = new Intent(_this, OrdersActivity.class);
                            startActivity(order);
                            break;
                        case "ZHJL":
                            Intent intentAdds = new Intent(_this, ZhuiHaoMainActivity.class);
                            startActivity(intentAdds);
                            break;
                        case "ZHMX":
                            Intent intentBankCard = new Intent(_this, AccountDetailActivity.class);
                            startActivity(intentBankCard);
                            break;
                        case "WDHB":
                            Intent redpacket = new Intent(_this, RedPacketActivity.class);
                            startActivity(redpacket);
                            break;
                        case "AQZX":
                            startActivity(new Intent(_this, SafeSettingActivity.class));
                            break;
                        case "JFZX":
                            String url = mDataList.get(arg2).getUrl();
                            Intent intent = new Intent(getActivity(), LotteryTypeActivity.class);// 跳转html活动界面
                            intent.putExtra("url", url + MyDbUtils.getCurrentUser().getUserId());
                            startActivity(intent);
                            break;
                        case "SZ":
                            startActivityForResult(new Intent(_this, SettingActivity.class), 1);
                            break;

                    }
                }


            }
        });
       /* orders = (PersonalItemView) view.findViewById(R.id.orders);
        orders.setOnClickListener(this);
        adds = (PersonalItemView) view.findViewById(R.id.adds);
        adds.setOnClickListener(this);
        account = (PersonalItemView) view.findViewById(R.id.account);
        account.setOnClickListener(this);
        redpacketView = (PersonalItemView) view.findViewById(R.id.redpacket);
        redpacketView.setOnClickListener(this);
        safe = (PersonalItemView) view.findViewById(R.id.safe);
        safe.setOnClickListener(this);
        set = (PersonalItemView) view.findViewById(R.id.set);
        set.setOnClickListener(this);*/

        load = view.findViewById(R.id.load);
        load.setAlpha(0.7f);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO 自动生成的方法存根
        if (!hidden) {
            loadData();
            initValue();
        }
        if (isShowOtherPPW && !hidden) {
            showRedPackagePPW();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_im:
                if (!NetWorkUtil.isNetworkAvailable(_this)) {
                    ToastUtils.showShort(_this, "当前网络信号较差，请检查网络设置");
                } else {
                    if (pop == null) {
                        popwindows();
                    }
                    if (pop.isShowing()) {
                        pop.dismiss();
                        load.setVisibility(View.GONE);
                    } else {
                        pop.showAtLocation(mSwipeLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        load.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.weirenzheng:
                Intent intentSafe = new Intent(_this, NameAuthActivity.class);
                startActivity(intentSafe);
                break;
            case R.id.sign:
                Intent intentSign = new Intent(getActivity(), SignActivity.class);
                startActivity(intentSign);
                break;
            case R.id.kefu:
                Intent nIntent = new Intent();
                nIntent.setClass(_this, LiveScoresActivity.class);
                nIntent.putExtra("url", "https://tb.53kf.com/code/client/10182922/1");//_this.getResources().getString(R.string.kefu).replace("#", "&"));
                //nIntent.putExtra("show", true);
                startActivity(nIntent);
                break;
            case R.id.pv_balance1:
            case R.id.pv_balance2:
                Intent intentAccount = new Intent(_this, AccountDetailActivity.class);
                startActivity(intentAccount);
                break;
            case R.id.pv_recharge:
                Intent intent = new Intent(_this, PayMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.pv_drawings:
                if (!PreferenceUtil.getBoolean(_this, "hasRealName")) {
                    ToastUtils.showShort(_this, "亲，请您先进行实名认证");
                    Intent intentRealName = new Intent(_this, NameAuthActivity.class);
                    startActivity(intentRealName);
                    return;
                }
                if (!PreferenceUtil.getBoolean(_this, "hasBindBankCard")) {
                    ToastUtils.showShort(_this, "亲，请您先绑定银行卡后再进行提款");
                    Intent intentBankCard = new Intent(_this, BankCardActivity.class);
                    startActivity(intentBankCard);
                    return;
                }
                if (!PreferenceUtil.getBoolean(_this, "hasPayPassword")) {
                    ToastUtils.showShort(_this, "亲，请您先设置支付密码再进行提款");
                    Intent intentBankCard = new Intent(_this, PayPwdActivity.class);
                    startActivity(intentBankCard);
                    return;
                }
                startActivityForResult(new Intent(_this, DrawingsActivity.class), 2);
                break;
            case R.id.redpacketItemView:
                Intent redpacket = new Intent(_this, RedPacketActivity.class);
                startActivity(redpacket);
                break;
            /*case R.id.adds:
                Intent intentAdds = new Intent(_this, ZhuiHaoMainActivity.class);
                startActivity(intentAdds);
                break;
            case R.id.account:
                Intent intentBankCard = new Intent(_this, AccountDetailActivity.class);
                startActivity(intentBankCard);
                break;

            case R.id.redpacket:
                Intent redpacket = new Intent(_this, RedPacketActivity.class);
                startActivity(redpacket);
                break;
            case R.id.safe:
                startActivity(new Intent(_this, SafeSettingActivity.class));
                break;
            case R.id.set:
                startActivityForResult(new Intent(_this,
                        SettingActivity.class), 1);
                break;*/
            default:
                break;
        }
    }

    private void initValue() {
        initShareInfo();//头
        setUserPic();//头像
        updateUserName();//昵称
        initRemainMoney();//余额
        if (!TextUtils.isEmpty(PreferenceUtil.getString(getActivity(), "rechargeOrderId"))) {//检查是否是首冲20送20
            checkIsRechareThan20(PreferenceUtil.getString(getActivity(), "rechargeOrderId"));
        }
        ServiceMain.getInstance().PostGetMyData(getActivity(), new CallBack<FindBeanDatas>() {

            @Override
            public void onSuccess(FindBeanDatas findBeanDatas) {
                if (findBeanDatas != null && findBeanDatas.getFindBeans() != null) {
                    mDataList.clear();
                    mDataList.addAll(findBeanDatas.getFindBeans());
                    mgAdapter.notifyDataSetChanged();
                    refreshListView.setVisibility(View.VISIBLE);
                } else {
                    refreshListView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                ToastUtils.showShort(_this, errorMessage.msg);
            }
        });
        ServiceMain.getInstance().GetMyLevel(getActivity(), new CallBack<Level>() {

            @Override
            public void onSuccess(Level level) {
                if (level != null) {
                    name_iv.setVisibility(View.VISIBLE);
                    switch (level.getUserIntegrationGrade()) {
                        case "VIP1":
                            name_iv.setImageResource(R.mipmap.vv1);
                            break;
                        case "VIP2":
                            name_iv.setImageResource(R.mipmap.vv2);
                            break;
                        case "VIP3":
                            name_iv.setImageResource(R.mipmap.vv3);
                            break;
                        case "VIP4":
                            name_iv.setImageResource(R.mipmap.vv4);
                            break;
                    }
                } else {
                    name_iv.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                ToastUtils.showShort(_this, errorMessage.msg);
            }
        });
    }

    private void initShareInfo() {
        boolean hasRealName = PreferenceUtil.getBoolean(_this, "hasRealName");
        if (hasRealName) {
            weirenzheng.setVisibility(View.GONE);
            renzheng.setVisibility(View.VISIBLE);
        } else {
            renzheng.setVisibility(View.GONE);
            weirenzheng.setVisibility(View.VISIBLE);
        }
    }

    private void setUserPic() {
        if (MyDbUtils.getCurrentUser() != null) {
            String url = MyDbUtils.getCurrentUser().getUserPic();
            xUtilsImageUtils.display(namePic, url, R.mipmap.defaultniu, true);

        }
    }

    private void updateUserName() {
        tv_userName.setText("");
        User user = MyDbUtils.getCurrentUser();
        if (user != null && (!TextUtils.isEmpty(user.getUserName()))) {
            tv_userName.setText(user.getUserName());
        } else {
            tv_userName.setText("");
        }
    }

    private void initRemainMoney() {
        UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
        if (userInfo != null) {
            String remainAccount = userInfo.getRemainMoney();
            if (TextUtils.isEmpty(remainAccount)) {
                balanceView_tv.setText("0");
            } else {
                balanceView_tv.setText(remainAccount + "");
            }
/*
            int packetAccount = userInfo.getHongbaoNumber();
            if(packetAccount<=0){
                packetUseView_tv.setText("0个");
            }else{
                packetUseView_tv.setText(packetAccount+"个");
            }*/

        }

    }


    private void loadData() {
        ServiceUser.getInstance().GetUserInfo(_this);// 余额跟新
    }

    public void onEventMainThread(String event) {

        if (!TextUtils.isEmpty(event)) {
            if ("updateUserInfo".equals(event)) {
                initRemainMoney();
            }
            if ("updateUserName".equals(event)) {
                updateUserName();
            }
            if ("updateNotice".equals(event)) {
                initShareInfo();
            }
            if ("updateUserPic".equals(event)) {
                setUserPic();
            }
            if ("updateNotice".equals(event)) {
                initShareInfo();
            }
            if (event.startsWith("checkMoney")) {
                checkIsRechareThan20(event.replace("checkMoney", "").trim());
            }
        }
    }

    //检查是否是首次充值》=20
    public boolean isRecharing = false;

    private void checkIsRechareThan20(String orderId) {
        if (PreferenceUtil.getBoolean(_this, "isHasShow20") || isRecharing) {//因为只会显示一次，所以显示过的，就不要再去请求接口了
            return;
        }
        isRecharing = true;
        ServicePay.getInstance().getIsFirstChargeAndThan20(getActivity(), orderId, new CallBack<String>() {

            @Override
            public void onSuccess(String payedPush) {
                isRecharing = false;


                if ("1".equals(payedPush)) {
                    PreferenceUtil.putBoolean(_this, "isHasShow20", true);//因为只会显示一次，所以显示过的，就不要再去请求接口了
                    if (isVisible()) {
                        showRedPackagePPW();
                    } else {
                        isShowOtherPPW = true;
                    }
                } else {
                    isShowOtherPPW = false;
                }
            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                isShowOtherPPW = false;
                isRecharing = false;
            }
        });
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isShowOtherPPW) {
            showRedPackagePPW();
        }
    }

    public void showRedPackagePPW() {
        isShowOtherPPW = false;//只显示一次
        View view_layout = getActivity().getLayoutInflater().inflate(R.layout.ppw_recharge_than20, null);
        Button btnClose = (Button) view_layout.findViewById(R.id.btn_close);
        Button btnUse = (Button) view_layout.findViewById(R.id.btn_use);
        Button btnView = (Button) view_layout.findViewById(R.id.btn_view);
        final CommonPPW commonPPW = new CommonPPW(getActivity(), view_layout);
        commonPPW.showPopupWindow(scrollview);


        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonPPW.dissmissPopupWindow();
            }
        });
        btnUse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                commonPPW.dissmissPopupWindow();
                Intent intent = new Intent(_this, MainActivity.class);
                intent.putExtra("nowPage", "1");
                intent.putExtra("hasLogin", "yes");//
                _this.startActivity(intent);
            }
        });
        btnView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                commonPPW.dissmissPopupWindow();
                Intent redpacket = new Intent(getActivity(), RedPacketActivity.class);
                startActivity(redpacket);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        loadData();
        initValue();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        }, 3000); // 5秒后发送消息，停止刷新
    }

    public void popwindows() {
        LayoutInflater inflater = LayoutInflater.from(_this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.headview, null);
        View photoCamera = view.findViewById(R.id.btn_take_photo);
        View album = view.findViewById(R.id.btn_pick_photo);
        View cancle = view.findViewById(R.id.btn_cancel);
        photoCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openPhotoCamera();
                pop.dismiss();
            }
        });
        album.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                album();
                pop.dismiss();
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        // 创建PopupWindow对象
        pop = new PopupWindow(view, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setOnDismissListener(new poponDismissListener());
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            load.setVisibility(View.GONE);
        }
    }

    /**
     * 拍摄照片
     */
    private void openPhotoCamera() {
        try {
            String nativetime = DateUtils.getNowDate("yyyyMMddHHmmssSSS");
            showimageFileName = nativetime + ".jpg";
            Intent intentPhone = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            strImgPath = DirsUtil.getSD_PHOTOS() + "/";// 存放照片的文件夹
            File file = new File(strImgPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(strImgPath, showimageFileName);
            Uri uri = Uri.fromFile(file);
            intentPhone.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intentPhone.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intentPhone, ConstantsBase.REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(_this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。");
        }
    }

    /**
     * 相册选择照片
     */
    protected void album() {

        int permission = ActivityCompat.checkSelfPermission(_this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(_this, ConstantsBase.PERMISSIONS_STORAGE, ConstantsBase.REQUEST_EXTERNAL_STORAGE);
        }

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, ConstantsBase.REQUEST_CODE_CHOOSE_PHOTO);
    }

    private String toZoom(String fileAllPath) {
        String[] suffixs = fileAllPath.split("\\.");//用.来区分获取最后的后缀名
        if (suffixs.length <= 1) {//没有后缀名，直接上传；
            return fileAllPath;
        }
        String suffix = suffixs[suffixs.length - 1];//有后缀名获取后缀名，如.jpg
        String[] names = fileAllPath.split("\\/");//用/来区分获取名字
        if (names.length <= 0) {//没有名字，直接上传；
            return fileAllPath;
        }
        String namesuffix = names[names.length - 1];//有名字获取名字带后缀名
        String name = namesuffix.substring(0, namesuffix.length() - suffix.length() - 1);//有名字获取名字
        int length = fileAllPath.length() - namesuffix.length();//名字之前路径的长度
        if (length < 0) {
            return fileAllPath;
        }
        StringBuffer small = new StringBuffer();
        small = small.append(fileAllPath.substring(0, length)).append(name.replace(".", "")).append("_Small.").append(suffix);

        boolean isZoom = false;
        if (null != fileAllPath) {
            isZoom = new ImageZoom().imageZoom(fileAllPath, small.toString());// 照片压缩
        }
        if (isZoom) {
            fileAllPath = small.toString();
        }
        return fileAllPath;
    }

    private void uploadPic(File fileImageBenDi, Attachment attachment, final String result) {
        if (!fileImageBenDi.exists()) {
            ToastUtils.showShort(_this, "您上传的图片已被删除");
            return;
        }
        ServiceVersion.getInstance().uploadMethod(_this, fileImageBenDi, attachment,
                new CallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO 自动生成的方法存根
                        User user = MyDbUtils.getCurrentUser();
                        user.setUserPic(t);
                        MyDbUtils.saveUser(user);
                        EventBus.getDefault().post("updateUserPic");
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存根
                        ToastUtils.showShort(_this, errorMessage.msg);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data != null) {
            String remainMoney = data.getStringExtra("remainMoney");
            if (!TextUtils.isEmpty(remainMoney)) {
                initRemainMoney();
                startActivity(new Intent(_this, AccountDetailActivity.class));
            }
        }
        if (resultCode == -1) {
            switch (requestCode) {
                case ConstantsBase.REQUEST_CODE_CHOOSE_PHOTO:// 本地选择
                    if (data.getData() != null) {
                        Uri selectedImageBenDi = data.getData();
                        String lujinBenDi = "";
                        //根据返回的URI获取对应的SQLite信息
                        Cursor cursorBenDi = _this.getContentResolver().query(selectedImageBenDi, null,
                                null, null, null);
                        if (cursorBenDi != null && cursorBenDi.moveToFirst()) {
                            try {
                                lujinBenDi = cursorBenDi.getString(cursorBenDi.getColumnIndex("_data"));// 获取绝对路径
                                cursorBenDi.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                lujinBenDi = selectedImageBenDi.getPath();
                            }
                        } else {
                            lujinBenDi = selectedImageBenDi.getPath();
                        }
                        lujinBenDi = toZoom(lujinBenDi);

                        File fileImageBenDi = new File(lujinBenDi);

                        namePic.setImageDrawable(new BitmapDrawable(ImageHelper.toRoundBitmap(BitmapUtil.setImageOptions(lujinBenDi))));
                        UUID uuid1 = UUID.randomUUID();
                        String[] names = lujinBenDi.split("\\/");
                        String name = lujinBenDi;
                        if (names.length > 1) {
                            name = names[names.length - 1];
                        }
                        Attachment attachment = new Attachment();
                        attachment.setAttachmentId(uuid1.randomUUID().toString() + "");
                        attachment.setAttachmentName(name);
                        attachment.setAttchmentAllLength(fileImageBenDi.length());
                        uploadPic(fileImageBenDi, attachment, lujinBenDi);
                    } else {
                        ToastUtils.showShort(_this, "文件不存在，请重新选择！");
                    }
                    break;
                case ConstantsBase.REQUEST_CODE_TAKE_PHOTO:// 拍照
                    strImgPath = toZoom(strImgPath + showimageFileName);
                    File fileImageBenDi = new File(strImgPath);

                    namePic.setImageDrawable(new BitmapDrawable(ImageHelper.toRoundBitmap(BitmapUtil.setImageOptions(strImgPath))));

                    UUID uuid1 = UUID.randomUUID();
                    Attachment attachment = new Attachment();
                    attachment.setAttachmentId(uuid1.randomUUID().toString() + "");
                    attachment.setAttachmentName(showimageFileName);
                    attachment.setAttchmentAllLength(fileImageBenDi.length());
                    uploadPic(fileImageBenDi, attachment, strImgPath);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
