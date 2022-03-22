package com.cshen.tiyu.activity.lottery.ball.football;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cshen.tiyu.GuideActivity;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.SplashActivity;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.FootBallMatchList;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBQC;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeBiFen;
import com.cshen.tiyu.domain.ball.ScroeIn;
import com.cshen.tiyu.domain.ball.ScroePeilv;
import com.cshen.tiyu.domain.ball.URLBack;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;
import com.cshen.tiyu.zx.ZXMainActivity;


public class FootballMainActivity extends BaseActivity
        implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int TODETAIL = 1;//详情选择

    public static final int SPF = 1;//胜负
    public static final int JQS = 2;//进球数
    public static final int BQC = 3;//半全场
    public static final int BF = 4;//比分
    public static final int HH = 5;//混合
    private FootballMainActivity _this;
    private ExpandableListView exListView;
    private ExpandableListAdapter adpater;
    private TextView tv_head_item;//头
    private ImageView tv_head_title_im;
    private ImageView shuoming, back;
    private PopupWindow pop;//玩法弹出框
    private View viewPop;

    private int wanfa = HH;//胜平负状态
    private ImageView clear;//清除
    private TextView number;//场次
    private View sure;//确定

    private ArrayList<String> parent = new ArrayList<String>();
    private HashMap mapChild = new HashMap<String, List<FootBallMatch>>();
    private HashMap mapChoosed = new HashMap<String, FootBallMatch>();

    private ImageView loading;
    private boolean isToDetil = false;
    private View load;
    private Timer timer = new Timer();
    private int i = 0;
    private boolean isShowing = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            loading.setBackgroundResource(images[i % 19]);
        }
    };
    public int images[] = new int[]{
            R.mipmap.gundong0, R.mipmap.gundong0,
            R.mipmap.gundong0, R.mipmap.gundong0,
            R.mipmap.gundong0, R.mipmap.gundong1, R.mipmap.gundong2,
            R.mipmap.gundong3, R.mipmap.gundong4, R.mipmap.gundong5,
            R.mipmap.gundong6, R.mipmap.gundong7, R.mipmap.gundong8,
            R.mipmap.gundong9, R.mipmap.gundong0,
            R.mipmap.gundong0, R.mipmap.gundong0,
            R.mipmap.gundong0, R.mipmap.gundong0};
    boolean add = false;
    boolean change = false;
    private VerticalSwipeRefreshLayout mSwipeLayout;


    View yindao, yindao1, yindao2, yindao3, yindao4;
    TextView text2,text31,text41;
    TextView tioaguo1, tioaguo2, tioaguo3, tioaguo4, next1, next2, next3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footballmain);
        _this = this;
        initView();
        Loading();
        initdata();
    }

    public void initView() {
        mSwipeLayout = (VerticalSwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        /********************顶部***********************/
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        shuoming = (ImageView) findViewById(R.id.iv_setting);
        shuoming.setOnClickListener(this);
        findViewById(R.id.tv_head).setOnClickListener(this);
        tv_head_item = (TextView) findViewById(R.id.tv_head_title_item);
        tv_head_title_im = (ImageView) findViewById(R.id.tv_head_title_im);
        exListView = (ExpandableListView) findViewById(R.id.list);
        adpater = new ExpandableListAdapter();
        exListView.setAdapter(adpater);
        exListView.setGroupIndicator(null);
        exListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (exListView != null && exListView.getChildCount() > 0) {
                    boolean firstItemVisible = exListView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = exListView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeLayout.setEnabled(enable);
            }
        });
        /********************底部***********************/
        clear = (ImageView) findViewById(R.id.dlt_clear);
        clear.setOnClickListener(this);
        sure = findViewById(R.id.dlt_sure);
        sure.setOnClickListener(this);
        number = (TextView) findViewById(R.id.jczq_number);
        load = findViewById(R.id.load);
        load.setAlpha(0.7f);
        load.setOnClickListener(this);
        loading = (ImageView) findViewById(R.id.loading);
        setNum();
        yindao = findViewById(R.id.yindaofootball);
        yindao1 = findViewById(R.id.yindao1);
        yindao2 = findViewById(R.id.yindao2);
        yindao3 = findViewById(R.id.yindao3);
        yindao4 = findViewById(R.id.yindao4);
        tioaguo1 = (TextView) findViewById(R.id.tiaoguo1);
        tioaguo1.setOnClickListener(this);
        tioaguo2 = (TextView) findViewById(R.id.tiaoguo2);
        tioaguo2.setOnClickListener(this);
        text2 = (TextView) findViewById(R.id.text2);
        tioaguo3 = (TextView) findViewById(R.id.tiaoguo3);
        tioaguo3.setOnClickListener(this);
        text31 = (TextView) findViewById(R.id.text31);
        tioaguo4 = (TextView) findViewById(R.id.tiaoguo4);
        tioaguo4.setOnClickListener(this);
        text41 = (TextView) findViewById(R.id.text41);
        next1 = (TextView) findViewById(R.id.next1);
        next1.setOnClickListener(this);
        next2 = (TextView) findViewById(R.id.next2);
        next2.setOnClickListener(this);
        next3 = (TextView) findViewById(R.id.next3);
        next3.setOnClickListener(this);

    }

    private void initdata() {
        Bundle b = getIntent().getExtras();
        try {
            add = b.getBoolean("add");
            wanfa = b.getInt("wanfa");
            if (wanfa == 0) {
                wanfa = HH;
            }
            switch (wanfa) {
                case SPF:
                    tv_head_item.setText("胜平负（让）");
                    break;
                case JQS:
                    tv_head_item.setText("进球数");
                    break;
                case BQC:
                    tv_head_item.setText("半全场");
                    break;
                case BF:
                    tv_head_item.setText("比分");
                    break;
                case HH:
                    tv_head_item.setText("混合过关");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            wanfa = HH;
            tv_head_title_im.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_head).setClickable(true);
        }

        if (JCZQUtil.getJCZQUtil().isFiveMin(_this, ConstantsBase.CHOOSEDNUMBERSJCZQTIME) && !add) {
            setFootBall();
        } else {
            new GetMatches().execute();
        }
    }

    private void initdataDetail() {
        Bundle b = getIntent().getExtras();
        try {
            HashMap mapChoosedB = (HashMap) b.getSerializable("matchs");
            if (mapChoosedB != null) {
                mapChoosed = mapChoosedB;
                if (mapChoosed.size() > 0) {
                    Iterator iter = mapChoosed.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String keys = (String) entry.getKey();
                        String[] positions = keys.split("\\+");
                        int motherkay = Integer.parseInt(positions[0]);
                        int childkey = Integer.parseInt(positions[1]);
                        FootBallMatch match = (FootBallMatch) entry.getValue();
                        String key = parent.get(motherkay);
                        ((ArrayList<FootBallMatch>) mapChild.get(key)).set(childkey, match);
                        adpater.notifyDataSetChanged();
                    }
                }
                setNum();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (isToDetil) {
            loading.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isToDetil) {
            isToDetil = false;
            loading.setVisibility(View.GONE);
            load.setVisibility(View.GONE);
        }
    }

    public void setFootBall() {
        ServiceCaiZhongInformation.getInstance().pastFootBallMatches(_this, "", "4", "2",
                ConstantsBase.JCZQ + "", "", new CallBack<FootBallMatchList>() {
                    @Override
                    public void onSuccess(FootBallMatchList t) {
                        // TODO 自动生成的方法存根
                        if (t.getResultList() != null && t.getResultList().size() > 0) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());
                            ChooseUtil.setDate(_this, ConstantsBase.CHOOSEDNUMBERSJCZQTIME, curDate);
                            ChooseUtil.setData(_this, ConstantsBase.CHOOSEDNUMBERSJCZQ, t.getResultList());
                            new GetMatches().execute();
                        } else {
                            LoadingStop();
                        }
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存根
                        ToastUtils.showShort(_this, errorMessage.msg);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
    }

    class GetMatches extends AsyncTask<String, Integer, List<FootBallMatch>> {
        List<FootBallMatch> _departmentList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading();
        }

        @Override
        protected List<FootBallMatch> doInBackground(String... arg0) {
            ArrayList<FootBallMatch> match = (ArrayList<FootBallMatch>)
                    ChooseUtil.getData(_this, ConstantsBase.CHOOSEDNUMBERSJCZQ);
            return match;
        }

        @Override
        protected void onPostExecute(List<FootBallMatch> match) {
            super.onPostExecute(match);
            if (match != null) {
                parent.clear();
                mapChild.clear();
                adpater.notifyDataSetChanged();
                for (int i = 0; i < match.size(); i++) {
                    String key = "";
                    if (match.get(i).isTop()) {
                        key = "【热门】";
                    } else {
                        key = match.get(i).getMatchKey().substring(0, 8);
                    }
                    if (parent.contains(key)) {
                        ((ArrayList<FootBallMatch>) mapChild.get(key)).add(match.get(i));
                    } else {
                        parent.add(key);
                        List<FootBallMatch> list = new ArrayList<FootBallMatch>();
                        list.add(match.get(i));
                        mapChild.put(key, list);
                    }
                }
                if (parent.indexOf("【热门】") > 0) {
                    parent.remove("【热门】");
                    parent.add(0, "【热门】");
                }
                adpater.setDateMother(parent);
                adpater.setDateChild(mapChild);
                for (int i = 0; i < parent.size(); i++) {
                    exListView.expandGroup(i);
                }
                adpater.notifyDataSetChanged();
            }
            if (!PreferenceUtil.getBoolean(_this, "is_user_yindao1_showed")) {
                yindao.setVisibility(View.VISIBLE);
                if (mapChild != null && mapChild.size() > 0 &&
                        ((ArrayList<FootBallMatch>) mapChild.get(parent.get(0))).get(0).getMixOpen().isSingle_0()) {
                    yindao1.setVisibility(View.VISIBLE);
                    text2.setText("2.选择你预测的比赛结果");
                    text31.setText("3.数字表示让球");
                    text41.setText("4.点击展开赛事分析");
                } else {
                    yindao1.setVisibility(View.GONE);
                    yindao2.setVisibility(View.VISIBLE);
                    text2.setText("1.选择你预测的比赛结果");
                    text31.setText("2.数字表示让球");
                    text41.setText("3.点击展开赛事分析");
                }
            }else{
                yindao.setVisibility(View.GONE);
            }
            if (!change) {
                initdataDetail();
            }
            LoadingStop();
        }

    }

    private void getUrl(String matchkey) {
        Loading();
        // TODO 自动生成的方法存�?
        ServiceCaiZhongInformation.getInstance().pastUrl(_this,matchkey, new CallBack<URLBack>() {

            @Override
            public void onSuccess(URLBack t) {
                // TODO 自动生成的方法存�?
                LoadingStop();
                Intent intent = new Intent(_this,LotteryTypeActivity.class);
                intent.putExtra("url", t.getUrl());
                intent.putExtra("flag", "matchkey");
                startActivity(intent);
            }

            @Override
            public void onFailure(ErrorMsg errorMessage) {
                // TODO 自动生成的方法存
                LoadingStop();
                ToastUtils.showShort(_this, errorMessage.msg);
            }
        });

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.load:
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                    loading.setVisibility(View.GONE);
                    load.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_back:
                if (add) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("matchs", mapChoosed);
                    if (change) {
                        bundle.putBoolean("change", change);
                    }
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                }
                _this.finish();
                break;
            case R.id.tv_head:
                if (!isShowing) {
                    if (pop == null) {
                        popwindows();
                    }
                    if (pop.isShowing()) {
                        pop.dismiss();
                        loading.setVisibility(View.GONE);
                        load.setVisibility(View.GONE);
                    } else {
                        pop.showAsDropDown(v);
                        loading.setVisibility(View.GONE);
                        load.setVisibility(View.VISIBLE);
                        switch (wanfa) {
                            case SPF:
                                setHeadBack(SPF);
                                break;
                            case JQS:
                                setHeadBack(JQS);
                                break;
                            case BQC:
                                setHeadBack(BQC);
                                break;
                            case BF:
                                setHeadBack(BF);
                                break;
                            case HH:
                                setHeadBack(HH);
                                break;
                        }
                    }
                }
                break;
            case R.id.iv_setting:
                Intent intentHelp = new Intent(_this, LotteryTypeActivity.class);
                intentHelp.putExtra("url", "http://an.caiwa188.com/help/jczq.html");
                startActivity(intentHelp);
                break;
            case R.id.dlt_sure:
                if (!isShowing) {
                    sure();
                }
                break;
            case R.id.dlt_clear:
                if (!isShowing) {
                    clear();
                }
                break;
            case R.id.tiaoguo1:
            case R.id.tiaoguo2:
            case R.id.tiaoguo3:
            case R.id.tiaoguo4:
                PreferenceUtil.putBoolean(FootballMainActivity.this, "is_user_yindao1_showed", true);
                yindao.setVisibility(View.GONE);
                yindao.setClickable(false);
                break;
            case R.id.next1:
                yindao1.setVisibility(View.GONE);
                yindao2.setVisibility(View.VISIBLE);
                break;
            case R.id.next2:
                yindao2.setVisibility(View.GONE);
                yindao3.setVisibility(View.VISIBLE);
                break;
            case R.id.next3:
                yindao3.setVisibility(View.GONE);
                yindao4.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setNum() {
        String numberStr = "<html><font color=\"#666666\">已选"
                + "</font><font color=\"#FF3232\">" + mapChoosed.size()
                + "</font><font color=\"#666666\">场比赛</font></html>";
        number.setText(Html.fromHtml(numberStr));
    }

    public void clear() {
        Iterator iter = mapChoosed.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String keys = (String) entry.getKey();
            String[] positions = keys.split("\\+");
            int motherkay = Integer.parseInt(positions[0]);
            int childkey = Integer.parseInt(positions[1]);
            String key = parent.get(motherkay);
            FootBallMatch match = (FootBallMatch) entry.getValue();
            match.getCheckNumber().clear();
            match.getSp().setChooseBF(0);
            match.getSp().setChooseJQS(0);
            match.getSp().setChooseBQQ(0);
        }
        adpater.notifyDataSetChanged();
        mapChoosed.clear();
        setNum();
    }

    public void sure() {
        if (mapChoosed.size() == 0) {
            ToastUtils.showShort(_this, "至少选两场比赛");
            return;
        }
        if (mapChoosed.size() == 1) {
            Iterator iter = mapChoosed.entrySet().iterator();
            ArrayList<Match> matchs = new ArrayList<>();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                FootBallMatch match = (FootBallMatch) entry.getValue();
                matchs.add(match);
            }
            boolean isSingle = JCZQUtil.getJCZQUtil().isSingle(matchs);
            if (!isSingle) {
                ToastUtils.showShort(_this, "至少选两场比赛");
                return;
            }
        }
        if (!add) {
            Intent intent = new Intent(_this, FootballAccountListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("wanfa", wanfa);
            bundle.putSerializable("matchs", mapChoosed);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("matchs", mapChoosed);
            if (change) {
                bundle.putBoolean("change", change);
            }
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            if (change) {
                Intent intent2 = new Intent(_this, FootballAccountListActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("wanfa", wanfa);
                bundle2.putSerializable("matchs", mapChoosed);
                intent2.putExtras(bundle2);
                startActivity(intent2);
            }
        }
        this.finish();
    }

    public void popwindows() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        viewPop = inflater.inflate(R.layout.footballmainchooseview, null);
        // 创建PopupWindow对象
        pop = new PopupWindow(viewPop, LayoutParams.FILL_PARENT,
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
            loading.setVisibility(View.GONE);
        }
    }

    public void choose(View v) {

        switch (v.getId()) {
            case R.id.hunhe_item:
                if (wanfa != HH) {
                    change = true;
                }
                wanfa = HH;
                break;
            case R.id.spf_item:
                if (wanfa != SPF) {
                    change = true;
                }
                wanfa = SPF;
                break;
            case R.id.jqs_item:
                if (wanfa != JQS) {
                    change = true;
                }
                wanfa = JQS;
                break;
            case R.id.bqc_item:
                if (wanfa != BQC) {
                    change = true;
                }
                wanfa = BQC;
                break;
            case R.id.bf_item:
                if (wanfa != BF) {
                    change = true;
                }
                wanfa = BF;
                break;
        }

        if (pop.isShowing()) {
            pop.dismiss();
            load.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        }
        if (change) {
            clear();
            setHeadBack(wanfa);
            if (JCZQUtil.getJCZQUtil().isFiveMin(_this, ConstantsBase.CHOOSEDNUMBERSJCZQTIME)) {
                setFootBall();
            }
        }
    }

    public void setHeadBack(int wanfa) {
        ((TextView) viewPop.findViewById(R.id.spf_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.spf_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.jqs_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.jqs_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.bqc_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.bqc_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.bf_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.bf_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.hunhe_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.hunhe_item).setBackgroundResource(R.drawable.dlt_tzback);


        switch (wanfa) {
            case SPF:
                tv_head_item.setText("胜平负（让）");
                ((TextView) viewPop.findViewById(R.id.spf_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.spf_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case JQS:
                tv_head_item.setText("进球数");
                ((TextView) viewPop.findViewById(R.id.jqs_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.jqs_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case BQC:
                tv_head_item.setText("半全场");
                ((TextView) viewPop.findViewById(R.id.bqc_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.bqc_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case BF:
                tv_head_item.setText("比分");
                ((TextView) viewPop.findViewById(R.id.bf_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.bf_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case HH:
                tv_head_item.setText("混合过关");
                ((TextView) viewPop.findViewById(R.id.hunhe_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.hunhe_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
        }

    }

    class ExpandableListAdapter extends BaseExpandableListAdapter {
        ArrayList<String> montherDay;
        HashMap childExam = new HashMap<String, ArrayList<FootBallMatch>>();

        public void setDateMother(ArrayList<String> montherDay) {
            this.montherDay = montherDay;
        }

        public void setDateChild(HashMap childExam) {
            this.childExam = childExam;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getGroupCount() {
            if (montherDay == null) {
                return 0;
            }
            return montherDay.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            String key = montherDay.get(groupPosition);
            if (montherDay == null
                    || montherDay.size() < groupPosition) {
                return 0;
            }
            try {
                if (childExam.get(key) == null) {
                    return 0;
                } else {
                    return ((ArrayList<FootBallMatch>) childExam.get(key)).size();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            if (montherDay == null) {
                return new String("暂无数据");
            }
            return montherDay.get(groupPosition);
        }

        //获取一个视图显示给定组，存放armTypes
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            ViewMotherHolder holder;
            String motherTitle = montherDay.get(groupPosition);
            int size = getChildrenCount(groupPosition);
            if (convertView == null) {
                holder = new ViewMotherHolder();
                convertView = View.inflate(_this, R.layout.footballmain_itemmother, null);
                holder.hot = (ImageView) convertView.findViewById(R.id.hot);
                holder.mothericon = (ImageView) convertView.findViewById(R.id.mothericon);
                holder.mothertext = (TextView) convertView.findViewById(R.id.mothertext);
                convertView.setTag(holder);
            } else {
                holder = (ViewMotherHolder) convertView.getTag();
            }
            if (!isExpanded) {
                holder.mothericon.setImageResource(R.mipmap.downfootball);
            } else {
                holder.mothericon.setImageResource(R.mipmap.upfootball);
            }
            if(!TextUtils.isEmpty(motherTitle)&&motherTitle.contains("热门")){
                holder.hot.setVisibility(View.VISIBLE);
                holder.mothertext.setText(size + "场可投");
            }else{
                holder.hot.setVisibility(View.GONE);
                holder.mothertext.setText(DateUtils.SubToDayChinese3(motherTitle) + " " + size + "场可投");
            }


            return convertView;
        }

        /**
         * ----------利用上面getChildId得到ID，从而根据ID得到arms中的数据，并填到TextView中---------
         */

        //获取与孩子在给定的组相关的数据,得到数组arms中元素的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = montherDay.get(groupPosition);
            if (montherDay == null
                    || montherDay.size() < groupPosition) {
                return new String("暂无数据");
            }
            try {
                if (childExam.get(key) == null) {
                    return new String("暂无数据");
                } else {
                    return ((ArrayList<FootBallMatch>) childExam.get(key)).get(childPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new String("暂无数据");
            }
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            if (wanfa == SPF) {
                return SPF;
            }
            if (wanfa == HH) {
                return HH;
            }
            if (wanfa == BQC) {
                return BQC;
            }
            if (wanfa == JQS) {
                return JQS;
            }
            if (wanfa == BF) {
                return BF;
            } else {
                return super.getChildType(groupPosition, childPosition);
            }
        }

        @Override
        public int getChildTypeCount() {
            // TODO Auto-generated method stub
            return 6;
        }

        //获取一个视图显示在给定的组 的儿童的数据，就是存放arms
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            final ViewChildHolder holder;
            final int motherkey = groupPosition;
            final int childkey = childPosition;
            final FootBallMatch match = (FootBallMatch) getChild(groupPosition, childPosition);
            int type = getChildType(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ViewChildHolder();
                switch (type) {
                    case SPF:
                    case HH:
                        convertView = View.inflate(_this, R.layout.footballmain_itemchild, null);
                        holder.openalljiaview = convertView.findViewById(R.id.openalljiaview);
                        holder.openall = (TextView) convertView.findViewById(R.id.openalltext);
                        holder.openallline = convertView.findViewById(R.id.openallline);
                        holder.dan = convertView.findViewById(R.id.dan);
                        holder.danspf = convertView.findViewById(R.id.danviewspf);
                        holder.nodate0 = (TextView) convertView.findViewById(R.id.nodate0);
                        holder.havedate0 = convertView.findViewById(R.id.havedate0);
                        holder.zhusheng0 = (TextView) convertView.findViewById(R.id.zhusheng0);
                        holder.ping0 = (TextView) convertView.findViewById(R.id.ping0);
                        holder.kesheng0 = (TextView) convertView.findViewById(R.id.kesheng0);
                        holder.danview = convertView.findViewById(R.id.danview);
                        holder.danrqspf = convertView.findViewById(R.id.danviewrqspf);
                        holder.nodate1 = (TextView) convertView.findViewById(R.id.nodate1);
                        holder.havedate1 = convertView.findViewById(R.id.havedate1);
                        holder.danrqspf = convertView.findViewById(R.id.danviewrqspf);
                        holder.rangqiu = (TextView) convertView.findViewById(R.id.rangqiu);
                        holder.zhusheng1 = (TextView) convertView.findViewById(R.id.zhusheng1);
                        holder.ping1 = (TextView) convertView.findViewById(R.id.ping1);
                        holder.kesheng1 = (TextView) convertView.findViewById(R.id.kesheng1);
                        break;
                    case BQC:
                        convertView = View.inflate(_this, R.layout.footballmain_itemchildbqc, null);
                        holder.ss = convertView.findViewById(R.id.ss);
                        holder.ssview = (TextView) convertView.findViewById(R.id.ssview);
                        holder.sstext = (TextView) convertView.findViewById(R.id.sstext);
                        holder.sp = convertView.findViewById(R.id.sp);
                        holder.spview = (TextView) convertView.findViewById(R.id.spview);
                        holder.sptext = (TextView) convertView.findViewById(R.id.sptext);
                        holder.sf = convertView.findViewById(R.id.sf);
                        holder.sfview = (TextView) convertView.findViewById(R.id.sfview);
                        holder.sftext = (TextView) convertView.findViewById(R.id.sftext);
                        holder.ps = convertView.findViewById(R.id.ps);
                        holder.psview = (TextView) convertView.findViewById(R.id.psview);
                        holder.pstext = (TextView) convertView.findViewById(R.id.pstext);
                        holder.pp = convertView.findViewById(R.id.pp);
                        holder.ppview = (TextView) convertView.findViewById(R.id.ppview);
                        holder.pptext = (TextView) convertView.findViewById(R.id.pptext);
                        holder.pf = convertView.findViewById(R.id.pf);
                        holder.pfview = (TextView) convertView.findViewById(R.id.pfview);
                        holder.pftext = (TextView) convertView.findViewById(R.id.pftext);
                        holder.fs = convertView.findViewById(R.id.fs);
                        holder.fsview = (TextView) convertView.findViewById(R.id.fsview);
                        holder.fstext = (TextView) convertView.findViewById(R.id.fstext);
                        holder.fp = convertView.findViewById(R.id.fp);
                        holder.fpview = (TextView) convertView.findViewById(R.id.fpview);
                        holder.fptext = (TextView) convertView.findViewById(R.id.fptext);
                        holder.ff = convertView.findViewById(R.id.ff);
                        holder.ffview = (TextView) convertView.findViewById(R.id.ffview);
                        holder.fftext = (TextView) convertView.findViewById(R.id.fftext);

                        holder.havedate1 = convertView.findViewById(R.id.havedate1);
                        holder.nodatebqc = (TextView) convertView.findViewById(R.id.nodate);
                        holder.danbqc = convertView.findViewById(R.id.danview);
                        break;
                    case JQS:
                        convertView = View.inflate(_this, R.layout.footballmain_itemchildbqc, null);
                        holder.jqs0 = convertView.findViewById(R.id.ss);
                        holder.jqs0view = (TextView) convertView.findViewById(R.id.ssview);
                        holder.jqs0view.setText("0球");
                        holder.jqs0text = (TextView) convertView.findViewById(R.id.sstext);
                        holder.jqs1 = convertView.findViewById(R.id.sp);
                        holder.jqs1view = (TextView) convertView.findViewById(R.id.spview);
                        holder.jqs1view.setText("1球");
                        holder.jqs1text = (TextView) convertView.findViewById(R.id.sptext);
                        holder.jqs2 = convertView.findViewById(R.id.sf);
                        holder.jqs2view = (TextView) convertView.findViewById(R.id.sfview);
                        holder.jqs2view.setText("2球");
                        holder.jqs2text = (TextView) convertView.findViewById(R.id.sftext);
                        holder.jqs3 = convertView.findViewById(R.id.ps);
                        holder.jqs3view = (TextView) convertView.findViewById(R.id.psview);
                        holder.jqs3view.setText("3球");
                        holder.jqs3text = (TextView) convertView.findViewById(R.id.pstext);

                        holder.jqs4 = convertView.findViewById(R.id.pf);
                        holder.jqs4view = (TextView) convertView.findViewById(R.id.pfview);
                        holder.jqs4view.setText("4球");
                        holder.jqs4text = (TextView) convertView.findViewById(R.id.pftext);
                        holder.jqs5 = convertView.findViewById(R.id.fs);
                        holder.jqs5view = (TextView) convertView.findViewById(R.id.fsview);
                        holder.jqs5view.setText("5球");
                        holder.jqs5text = (TextView) convertView.findViewById(R.id.fstext);
                        holder.jqs6 = convertView.findViewById(R.id.fp);
                        holder.jqs6view = (TextView) convertView.findViewById(R.id.fpview);
                        holder.jqs6view.setText("6球");
                        holder.jqs6text = (TextView) convertView.findViewById(R.id.fptext);
                        holder.jqs7 = convertView.findViewById(R.id.ff);
                        holder.jqs7view = (TextView) convertView.findViewById(R.id.ffview);
                        holder.jqs7view.setText("7+球");
                        holder.jqs7text = (TextView) convertView.findViewById(R.id.fftext);

                        holder.danjqs = convertView.findViewById(R.id.danview);
                        holder.havedate1 = convertView.findViewById(R.id.havedate1);
                        holder.nodatejqs = (TextView) convertView.findViewById(R.id.nodate);
                        convertView.findViewById(R.id.pp).setVisibility(View.GONE);
                        break;

                    case BF:
                        convertView = View.inflate(_this, R.layout.footballmain_itemchildbf, null);
                        holder.bfview = convertView.findViewById(R.id.bfview);
                        holder.nodatebf = (TextView) convertView.findViewById(R.id.nodatebf);
                        holder.bf = (TextView) convertView.findViewById(R.id.bf);
                        holder.danbf = convertView.findViewById(R.id.danview);
                        break;
                }
                holder.titlename = (TextView) convertView.findViewById(R.id.titlename);
                holder.titletime = (TextView) convertView.findViewById(R.id.titletime);
                holder.titleend = (TextView) convertView.findViewById(R.id.titleend);
                holder.childicon = (ImageView) convertView.findViewById(R.id.childicon);

                holder.titlezhu = (TextView) convertView.findViewById(R.id.titlezhu);
                holder.titleke = (TextView) convertView.findViewById(R.id.titleke);

                holder.child = convertView.findViewById(R.id.child);
                holder.history = (TextView) convertView.findViewById(R.id.history);
                holder.zhushuju = (TextView) convertView.findViewById(R.id.zhushuju);
                holder.keshuju = (TextView) convertView.findViewById(R.id.keshuju);
                holder.shengpei = (TextView) convertView.findViewById(R.id.shengpei);
                holder.pingpei = (TextView) convertView.findViewById(R.id.pingpei);
                holder.fupei = (TextView) convertView.findViewById(R.id.fupei);
                holder.shengpeiim = (ImageView) convertView.findViewById(R.id.shengpeiim);
                holder.pingpeiim = (ImageView) convertView.findViewById(R.id.pingpeiim);
                holder.fupeiim = (ImageView) convertView.findViewById(R.id.fupeiim);
                holder.saishifenxi = convertView.findViewById(R.id.saishifenxi);
                holder.yuce = convertView.findViewById(R.id.yuce);


                convertView.setTag(holder);
            } else {
                holder = (ViewChildHolder) convertView.getTag();
            }
            holder.titlename.setText(match.getGameName());
            String matchKeyStr = match.getMatchKey();
            holder.titletime.setText(DateUtils.SubToDayChinese4(matchKeyStr.substring(0, 8), matchKeyStr.substring(9, matchKeyStr.length())));
            holder.titleend.setText(match.getEndSaleTime().substring(11, 16) + "截止");
            holder.titlezhu.setText(match.getHomeTeamName());
            holder.titleke.setText(match.getGuestTeamName());
            if (match.isOpen()) {
                holder.child.setVisibility(View.VISIBLE);
                holder.childicon.setImageResource(R.mipmap.main115choose);
            } else {
                holder.child.setVisibility(View.GONE);
                holder.childicon.setImageResource(R.mipmap.main115chooseno);
            }

            holder.history.setText("暂无交战记录");
            holder.zhushuju.setText("暂无交战记录");
            holder.keshuju.setText("暂无交战记录");

            holder.shengpei.setText("0.00");
            holder.pingpei.setText("0.00");
            holder.fupei.setText("0.00");

            String[] Str = null;
            if (!TextUtils.isEmpty(match.getHistoryHelp())
                    && match.getHistoryHelp().contains("|")) {
                Str = match.getHistoryHelp().split("\\|");
                if (Str.length > 0) {
                    if (!TextUtils.isEmpty(Str[0])
                            && Str[0].contains(",")) {
                        String[] history = Str[0].split(",");
                        if (history != null && history.length == 3) {
                            int sheng = Integer.parseInt(history[0]);
                            int ping = Integer.parseInt(history[1]);
                            int fu = Integer.parseInt(history[2]);
                            if ((sheng + ping + fu) > 0) {
                                String historystr = "<html><font color=\"#ffffff\">近" + (sheng + ping + fu)
                                        + "次交战，" + match.getHomeTeamName() + "</font><font color=\"#ff0023\">" + sheng
                                        + "胜</font><font color=\"#a3a3a3\">" + ping
                                        + "平</font><font color=\"#7676ff\">" + fu
                                        + "负</font></html>";
                                holder.history.setText(Html.fromHtml(historystr));
                            }
                        }
                    }
                }
                if (Str.length > 1) {
                    if (!TextUtils.isEmpty(Str[1])
                            && Str[1].contains(",")) {
                        String[] zhu = Str[1].split(",");
                        if (zhu != null && zhu.length == 3) {
                            int sheng = Integer.parseInt(zhu[0]);
                            int ping = Integer.parseInt(zhu[1]);
                            int fu = Integer.parseInt(zhu[2]);
                            if ((sheng + ping + fu) > 0) {
                                String historystr = "<html><font color=\"#ffffff\">主队"
                                        + "</font><font color=\"#ff0023\">" + sheng
                                        + "胜</font><font color=\"#a3a3a3\">" + ping
                                        + "平</font><font color=\"#7676ff\">" + fu
                                        + "负</font></html>";
                                holder.zhushuju.setText(Html.fromHtml(historystr));
                            }
                        }
                    }
                }
                if (Str.length > 2) {
                    if (!TextUtils.isEmpty(Str[2])
                            && Str[2].contains(",")) {
                        String[] ke = Str[2].split(",");
                        if (ke != null && ke.length == 3) {
                            int sheng = Integer.parseInt(ke[0]);
                            int ping = Integer.parseInt(ke[1]);
                            int fu = Integer.parseInt(ke[2]);
                            if ((sheng + ping + fu) > 0) {
                                String historystr = "<html><font color=\"#ffffff\">客队"
                                        + "</font><font color=\"#ff0023\">" + sheng
                                        + "胜</font><font color=\"#a3a3a3\">" + ping
                                        + "平</font><font color=\"#7676ff\">" + fu
                                        + "负</font></html>";
                                holder.keshuju.setText(Html.fromHtml(historystr));
                            }
                        }
                    }
                }
                if (Str.length > 3) {
                    if (!TextUtils.isEmpty(Str[3])
                            && Str[3].contains(",")) {
                        String[] pei = Str[3].split(",");
                        if (pei != null && pei.length == 3) {
                            holder.shengpei.setText(pei[0]);
                            holder.pingpei.setText(pei[1]);
                            holder.fupei.setText(pei[2]);
                        }
                    }
                }
            }
            holder.child.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (match != null && !TextUtils.isEmpty(match.getMatchKey())) {
                        String matchkey = "";
                        try {
                            matchkey = Integer.parseInt(match.getMatchKey()) + "";
                        } catch (Exception e) {
                            matchkey = match.getMatchKey().substring(0, 8) + match.getMatchKey().substring(9, match.getMatchKey().length());
                        }
                        getUrl(matchkey);
                    }
                }
            });
            //public ImageView shengpeiim,pingpeiim,fupeiim
            holder.childicon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (match.isOpen()) {
                        match.setOpen(false);
                        holder.child.setVisibility(View.GONE);
                        holder.childicon.setImageResource(R.mipmap.main115chooseno);
                    } else {
                        match.setOpen(true);
                        holder.child.setVisibility(View.VISIBLE);
                        holder.childicon.setImageResource(R.mipmap.main115choose);
                    }
                    mapChild.put(motherkey + "+" + childkey, match);
                }
            });
            if (wanfa == HH || wanfa == SPF) {
                holder.rangqiu.setText(match.getHandicap() + "");
                int handicap = Integer.parseInt(match.getHandicap());
                if (handicap <= 0) {
                    holder.rangqiu.setBackgroundColor(Color.parseColor("#529AF2"));
                } else {
                    holder.rangqiu.setBackgroundColor(Color.parseColor("#f2826a"));
                }

                holder.dan.setVisibility(View.GONE);
                if (match.getMixOpen() != null && match.getMixOpen().isPass_0()) {
                    //是否过关
                    if (match.getSp() != null) {
                        final ScroePeilv spf = match.getSp().getSPF();
                        if (spf != null) {
                            holder.nodate0.setVisibility(View.INVISIBLE);
                            holder.havedate0.setVisibility(View.VISIBLE);
                            if (spf.getWIN() != null) {
                                String key = spf.getWIN().getKey();
                                if (!key.endsWith("SPF")) {
                                    spf.getWIN().setKey(key + "SPF");
                                }

                                JCZQUtil.getJCZQUtil().setString(holder.zhusheng0, "主胜", spf.getWIN(), match.getCheckNumber());
                            }
                            if (spf.getDRAW() != null) {
                                String key = spf.getDRAW().getKey();
                                if (!key.endsWith("SPF")) {
                                    spf.getDRAW().setKey(key + "SPF");
                                }
                                JCZQUtil.getJCZQUtil().setString(holder.ping0, "平", spf.getDRAW(), match.getCheckNumber());
                            }
                            if (spf.getLOSE() != null) {
                                String key = spf.getLOSE().getKey();
                                if (!key.endsWith("SPF")) {
                                    spf.getLOSE().setKey(key + "SPF");
                                }
                                JCZQUtil.getJCZQUtil().setString(holder.kesheng0, "客胜", spf.getLOSE(), match.getCheckNumber());
                            }
                            holder.zhusheng0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = spf.getWIN().getKey();
                                    if (!key.endsWith("SPF")) {
                                        spf.getWIN().setKey(key + "SPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "主胜", spf.getWIN(), match.getCheckNumber());
                                    setChoose(match, spf.getWIN(), motherkey, childkey);
                                }
                            });
                            holder.ping0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = spf.getDRAW().getKey();
                                    if (!key.endsWith("SPF")) {
                                        spf.getDRAW().setKey(key + "SPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "平", spf.getDRAW(), match.getCheckNumber());
                                    setChoose(match, spf.getDRAW(), motherkey, childkey);
                                }
                            });
                            holder.kesheng0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = spf.getLOSE().getKey();
                                    if (!key.endsWith("SPF")) {
                                        spf.getLOSE().setKey(key + "SPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "客胜", spf.getLOSE(), match.getCheckNumber());
                                    setChoose(match, spf.getLOSE(), motherkey, childkey);
                                }
                            });
                        } else {
                            holder.nodate0.setText("暂未开售");
                            holder.nodate0.setVisibility(View.VISIBLE);
                            holder.havedate0.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodate0.setText("暂未开售");
                        holder.nodate0.setVisibility(View.VISIBLE);
                        holder.havedate0.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodate0.setText("暂未开售");
                    holder.nodate0.setVisibility(View.VISIBLE);
                    holder.havedate0.setVisibility(View.INVISIBLE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_0()) {
                    //是否单关
                    holder.danspf.setVisibility(View.VISIBLE);
                    holder.dan.setVisibility(View.VISIBLE);
                } else {
                    holder.danspf.setVisibility(View.INVISIBLE);
                }

                if (match.getMixOpen() != null && match.getMixOpen().isPass_5()) {
                    //是否过关
                    if (match.getSp() != null) {
                        final ScroePeilv rqspf = match.getSp().getRQSPF();
                        if (rqspf != null) {
                            holder.nodate1.setVisibility(View.INVISIBLE);
                            holder.havedate1.setVisibility(View.VISIBLE);
                            if (rqspf.getWIN() != null) {
                                String key = rqspf.getWIN().getKey();
                                if (!key.endsWith("RQSPF")) {
                                    rqspf.getWIN().setKey(key + "RQSPF");
                                }
                                JCZQUtil.getJCZQUtil().setString(holder.zhusheng1, "主胜", rqspf.getWIN(), match.getCheckNumber());
                            }
                            if (rqspf.getDRAW() != null) {
                                String key = rqspf.getDRAW().getKey();
                                if (!key.endsWith("RQSPF")) {
                                    rqspf.getDRAW().setKey(key + "RQSPF");
                                }
                                JCZQUtil.getJCZQUtil().setString(holder.ping1, "平", rqspf.getDRAW(), match.getCheckNumber());
                            }
                            if (rqspf.getLOSE() != null) {
                                String key = rqspf.getLOSE().getKey();
                                if (!key.endsWith("RQSPF")) {
                                    rqspf.getLOSE().setKey(key + "RQSPF");
                                }
                                JCZQUtil.getJCZQUtil().setString(holder.kesheng1, "客胜", rqspf.getLOSE(), match.getCheckNumber());
                            }
                            holder.zhusheng1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = rqspf.getWIN().getKey();
                                    if (!key.endsWith("RQSPF")) {
                                        rqspf.getWIN().setKey(key + "RQSPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "主胜", rqspf.getWIN(), match.getCheckNumber());
                                    setChoose(match, rqspf.getWIN(), motherkey, childkey);
                                }
                            });
                            holder.ping1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = rqspf.getDRAW().getKey();
                                    if (!key.endsWith("RQSPF")) {
                                        rqspf.getDRAW().setKey(key + "RQSPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "平", rqspf.getDRAW(), match.getCheckNumber());
                                    setChoose(match, rqspf.getDRAW(), motherkey, childkey);
                                }
                            });
                            holder.kesheng1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    String key = rqspf.getLOSE().getKey();
                                    if (!key.endsWith("RQSPF")) {
                                        rqspf.getLOSE().setKey(key + "RQSPF");
                                    }
                                    JCZQUtil.getJCZQUtil().setStringClick((TextView) v, "客胜", rqspf.getLOSE(), match.getCheckNumber());
                                    setChoose(match, rqspf.getLOSE(), motherkey, childkey);
                                }
                            });
                        } else {
                            holder.nodate1.setText("暂未开售");
                            holder.nodate1.setVisibility(View.VISIBLE);
                            holder.havedate1.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodate1.setVisibility(View.VISIBLE);
                        holder.nodate1.setText("暂未开售");
                        holder.havedate1.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodate1.setText("暂未开售");
                    holder.nodate1.setVisibility(View.VISIBLE);
                    holder.havedate1.setVisibility(View.INVISIBLE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_5()) {
                    //是否单关
                    holder.danrqspf.setVisibility(View.VISIBLE);
                    holder.dan.setVisibility(View.VISIBLE);
                } else {
                    holder.danrqspf.setVisibility(View.INVISIBLE);
                }

                if (wanfa == HH) {
                    holder.openall.setVisibility(View.VISIBLE);
                    holder.openallline.setVisibility(View.VISIBLE);
                    holder.openalljiaview.setVisibility(View.INVISIBLE);
                    if (match.getCheckNumber().size() == 0) {
                        holder.openall.setText("展开全部");
                        holder.openall.setTextColor(Color.parseColor("#333333"));
                        holder.openall.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        holder.openall.setText("已选" + match.getCheckNumber().size() + "项");
                        holder.openall.setTextColor(Color.parseColor("#9e7321"));
                        holder.openall.setBackgroundColor(Color.parseColor("#FFCA01"));
                    }


                    holder.openall.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                ToastUtils.showShort(_this, "最多选择8场比赛");
                                return;
                            }
                            isToDetil = true;
                            Intent intent = new Intent(_this, FootballDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("match", match);
                            bundle.putInt("motherkay", motherkey);
                            bundle.putInt("childkey", childkey);
                            bundle.putInt("wanfa", wanfa);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, TODETAIL);
                        }
                    });
                } else {
                    holder.openall.setVisibility(View.GONE);
                    holder.openallline.setVisibility(View.GONE);
                    holder.openalljiaview.setVisibility(View.GONE);
                }
            }
            if (wanfa == JQS) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_1()) {
                    //是否单关
                    holder.danjqs.setVisibility(View.VISIBLE);

                } else {
                    holder.danjqs.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_1()) {
                    //是否过关
                    if (match.getSp() != null) {
                        final ScroeIn jqs = match.getSp().getJQS();
                        if (jqs != null) {
                            holder.nodatejqs.setVisibility(View.INVISIBLE);
                            holder.havedate1.setVisibility(View.VISIBLE);
                            if (jqs.getS0() != null) {
                                holder.jqs0text.setText(jqs.getS0().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs0, holder.jqs0view, holder.jqs0text,
                                        jqs.getS0(), match.getCheckNumber());
                            }
                            if (jqs.getS1() != null) {
                                holder.jqs1text.setText(jqs.getS1().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs1, holder.jqs1view, holder.jqs1text,
                                        jqs.getS1(), match.getCheckNumber());
                            }
                            if (jqs.getS2() != null) {
                                holder.jqs2text.setText(jqs.getS2().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs2, holder.jqs2view, holder.jqs2text,
                                        jqs.getS2(), match.getCheckNumber());
                            }
                            if (jqs.getS3() != null) {
                                holder.jqs3text.setText(jqs.getS3().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs3, holder.jqs3view, holder.jqs3text,
                                        jqs.getS3(), match.getCheckNumber());
                            }
                            if (jqs.getS4() != null) {
                                holder.jqs4text.setText(jqs.getS4().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs4, holder.jqs4view, holder.jqs4text,
                                        jqs.getS4(), match.getCheckNumber());
                            }
                            if (jqs.getS5() != null) {
                                holder.jqs5text.setText(jqs.getS5().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs5, holder.jqs5view, holder.jqs5text,
                                        jqs.getS5(), match.getCheckNumber());
                            }
                            if (jqs.getS6() != null) {
                                holder.jqs6text.setText(jqs.getS6().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs6, holder.jqs6view, holder.jqs6text,
                                        jqs.getS6(), match.getCheckNumber());
                            }
                            if (jqs.getS7() != null) {
                                holder.jqs7text.setText(jqs.getS7().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.jqs7, holder.jqs7view, holder.jqs7text,
                                        jqs.getS7(), match.getCheckNumber());
                            }
                            holder.jqs0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs0,holder.jqs0view,holder.jqs0text,match);
                                    setNumber(match, jqs.getS0(), motherkey, childkey);
                                }
                            });
                            holder.jqs1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs1,holder.jqs1view,holder.jqs1text,match);
                                    setNumber(match, jqs.getS1(), motherkey, childkey);
                                }
                            });
                            holder.jqs2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs2,holder.jqs2view,holder.jqs2text,match);
                                    setNumber(match, jqs.getS2(), motherkey, childkey);
                                }
                            });
                            holder.jqs3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs3,holder.jqs3view,holder.jqs3text,match);
                                    setNumber(match, jqs.getS3(), motherkey, childkey);
                                }
                            });
                            holder.jqs4.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs4,holder.jqs4view,holder.jqs4text,match);
                                    setNumber(match, jqs.getS4(), motherkey, childkey);
                                }
                            });
                            holder.jqs5.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs5,holder.jqs5view,holder.jqs5text,match);
                                    setNumber(match, jqs.getS5(), motherkey, childkey);
                                }
                            });
                            holder.jqs6.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs6,holder.jqs6view,holder.jqs6text,match);
                                    setNumber(match, jqs.getS6(), motherkey, childkey);
                                }
                            });
                            holder.jqs7.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.jqs7,holder.jqs7view,holder.jqs7text,match);
                                    setNumber(match, jqs.getS7(), motherkey, childkey);
                                }
                            });
                        } else {
                            holder.nodatejqs.setText("暂未开售");
                            holder.nodatejqs.setVisibility(View.VISIBLE);
                            holder.havedate1.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodatejqs.setText("暂未开售");
                        holder.nodatejqs.setVisibility(View.VISIBLE);
                        holder.havedate1.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodatejqs.setText("暂未开售");
                    holder.nodatejqs.setVisibility(View.VISIBLE);
                    holder.havedate1.setVisibility(View.INVISIBLE);
                }
            }
            if (wanfa == BQC) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_3()) {
                    //是否单关
                    holder.danbqc.setVisibility(View.VISIBLE);
                } else {
                    holder.danbqc.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_3()) {
                    //是否过关
                    if (match.getSp() != null) {
                        final ScroeBQC bqc = match.getSp().getBQQ();
                        if (bqc != null) {
                            holder.havedate1.setVisibility(View.VISIBLE);
                            holder.nodatebqc.setVisibility(View.INVISIBLE);
                            if (bqc.getWIN_WIN() != null) {
                                holder.sstext.setText(bqc.getWIN_WIN().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.ss, holder.ssview, holder.sstext,
                                        bqc.getWIN_WIN(), match.getCheckNumber());
                            }
                            if (bqc.getWIN_DRAW() != null) {
                                holder.sptext.setText(bqc.getWIN_DRAW().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.sp, holder.spview, holder.sptext,
                                        bqc.getWIN_DRAW(), match.getCheckNumber());
                            }
                            if (bqc.getWIN_LOSE() != null) {
                                holder.sftext.setText(bqc.getWIN_LOSE().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.sf, holder.sfview, holder.sftext,
                                        bqc.getWIN_LOSE(), match.getCheckNumber());
                            }
                            if (bqc.getDRAW_WIN() != null) {
                                holder.pstext.setText(bqc.getDRAW_WIN().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.ps, holder.psview, holder.pstext,
                                        bqc.getDRAW_WIN(), match.getCheckNumber());
                            }
                            if (bqc.getDRAW_DRAW() != null) {
                                holder.pptext.setText(bqc.getDRAW_DRAW().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.pp, holder.ppview, holder.pptext,
                                        bqc.getDRAW_DRAW(), match.getCheckNumber());
                            }
                            if (bqc.getDRAW_LOSE() != null) {
                                holder.pftext.setText(bqc.getDRAW_LOSE().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.pf, holder.pfview, holder.pftext,
                                        bqc.getDRAW_LOSE(), match.getCheckNumber());
                            }
                            if (bqc.getLOSE_WIN() != null) {
                                holder.fstext.setText(bqc.getLOSE_WIN().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.fs, holder.fsview, holder.fstext,
                                        bqc.getLOSE_WIN(), match.getCheckNumber());
                            }
                            if (bqc.getLOSE_DRAW() != null) {
                                holder.fptext.setText(bqc.getLOSE_DRAW().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.fp, holder.fpview, holder.fptext,
                                        bqc.getLOSE_DRAW(), match.getCheckNumber());
                            }
                            if (bqc.getLOSE_LOSE() != null) {
                                holder.fftext.setText(bqc.getLOSE_LOSE().getValue());
                                JCZQUtil.getJCZQUtil().setColor(holder.ff, holder.ffview, holder.fftext,
                                        bqc.getLOSE_LOSE(), match.getCheckNumber());
                            }
                            holder.ss.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.ss,holder.ssview,holder.sstext,match);
                                    setNumber(match, bqc.getWIN_WIN(), motherkey, childkey);
                                }
                            });
                            holder.sp.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.sp,holder.spview,holder.sptext,match);
                                    setNumber(match, bqc.getWIN_DRAW(), motherkey, childkey);
                                }
                            });
                            holder.sf.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.sf,holder.sfview,holder.sftext,match);
                                    setNumber(match, bqc.getWIN_LOSE(), motherkey, childkey);
                                }
                            });
                            holder.ps.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.ps,holder.psview,holder.pstext,match);
                                    setNumber(match, bqc.getDRAW_WIN(), motherkey, childkey);
                                }
                            });
                            holder.pp.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.pp,holder.ppview,holder.pptext,match);
                                    setNumber(match, bqc.getDRAW_DRAW(), motherkey, childkey);
                                }
                            });
                            holder.pf.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.pf,holder.pfview,holder.pftext,match);
                                    setNumber(match, bqc.getDRAW_LOSE(), motherkey, childkey);
                                }
                            });
                            holder.fs.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.fs,holder.fsview,holder.fstext,match);
                                    setNumber(match, bqc.getLOSE_WIN(), motherkey, childkey);
                                }
                            });
                            holder.fp.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.fp,holder.fpview,holder.fptext,match);
                                    setNumber(match, bqc.getLOSE_DRAW(), motherkey, childkey);
                                }
                            });

                            holder.ff.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    //choose(holder.ff,holder.ffview,holder.fftext,match);
                                    setNumber(match, bqc.getLOSE_LOSE(), motherkey, childkey);
                                }
                            });
                        } else {
                            holder.nodatebqc.setText("暂未开售");
                            holder.nodatebqc.setVisibility(View.VISIBLE);
                            holder.havedate1.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodatebqc.setText("暂未开售");
                        holder.nodatebqc.setVisibility(View.VISIBLE);
                        holder.havedate1.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodatebqc.setText("暂未开售");
                    holder.nodatebqc.setVisibility(View.VISIBLE);
                    holder.havedate1.setVisibility(View.INVISIBLE);
                }
            }
            if (wanfa == BF) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_2()) {
                    //是否单关
                    holder.danbf.setVisibility(View.VISIBLE);
                } else {
                    holder.danbf.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_2()) {
                    //是否过关
                    if (match.getMixSp() != null) {
                        final ScroeBiFen bf = match.getMixSp().getBF();
                        if (bf != null) {
                            holder.nodatebf.setVisibility(View.INVISIBLE);
                            holder.bf.setVisibility(View.VISIBLE);
                            String bfStr = JCZQUtil.getJCZQUtil().isHaveBF(match.getCheckNumber());
                            if (bfStr != null && bfStr.length() > 1) {
                                bfStr = bfStr.substring(0, bfStr.length() - 1);
                                holder.bf.setText(bfStr);
                                holder.bf.setTextColor(Color.parseColor("#ec3633"));
                                holder.bfview.setBackgroundResource(R.drawable.bf_tzback);
                            } else {
                                holder.bf.setText("点击选择比分");
                                holder.bf.setTextColor(Color.parseColor("#ff812a"));
                                holder.bfview.setBackgroundResource(R.drawable.dlt_tzback);
                            }
                            holder.bf.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    isToDetil = true;
                                    Intent intent = new Intent(_this, FootballBFDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("match", match);
                                    bundle.putInt("motherkay", motherkey);
                                    bundle.putInt("childkey", childkey);
                                    bundle.putInt("wanfa", wanfa);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, TODETAIL);
                                }
                            });
                        } else {
                            holder.nodatebf.setText("暂未开售");
                            holder.nodatebf.setVisibility(View.VISIBLE);
                            holder.bf.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodatebf.setVisibility(View.VISIBLE);
                        holder.nodatebf.setText("暂未开售");
                        holder.bf.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodatebf.setText("暂未开售");
                    holder.nodatebf.setVisibility(View.VISIBLE);
                    holder.bf.setVisibility(View.INVISIBLE);
                }
            }

            convertView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            return convertView;
        }

        /*public void choose(View view,TextView tv0,TextView tv,FootBallMatch match){
            JCZQUtil.getJCZQUtil().setColorClick(view,tv0,
					tv,match.getSp().getBQQ().getLOSE_WIN(),match.getCheckNumber());

		}*/
        public void setChoose(FootBallMatch match, ScroeBean key, int montherkey, int childkey) {
            boolean ishas = false;
            for (int i = 0; i < match.getCheckNumber().size(); i++) {
                if (match.getCheckNumber().get(i).getKey().equals(key.getKey())) {
                    match.getCheckNumber().remove(i);
                    ishas = true;
                    break;
                }
            }
            if (!ishas) {
                match.getCheckNumber().add(key);
            }
            int checkNumber = match.getCheckNumber().size();
            if (checkNumber > 0 && !
                    mapChoosed.containsKey(montherkey + "+" + childkey)) {//有check并且没存在已选单中
                mapChoosed.put(montherkey + "+" + childkey, match);
            }
            if (checkNumber <= 0 && mapChoosed.containsKey(montherkey + "+" + childkey)) {//没check并且存在已选单中
                mapChoosed.remove(montherkey + "+" + childkey);
            }

            adpater.notifyDataSetChanged();
            setNum();
        }

        public void setNumber(FootBallMatch match, ScroeBean sb, int montherkey, int childkey) {
            boolean isHas = false;
            for (int i = 0; i < match.getCheckNumber().size(); i++) {
                if (match.getCheckNumber().get(i).getKey().equals(sb.getKey())) {
                    match.getCheckNumber().remove(i);
                    isHas = true;
                    break;
                }
            }
            String key = sb.getKey();
            if (isHas) {
                if (key.contains("SPF") || key.contains("RQSPF")) {
                    //不是胜平负也不是让球胜平负
                } else if (key.contains("S") && !key.contains("LOSE")) {
                    match.getSp().setChooseJQS(match.getSp().getChooseJQS() - 1);
                } else if (key.contains("_") && !key.contains("OTHER")) {
                    match.getSp().setChooseBQQ(match.getSp().getChooseBQQ() - 1);
                } else {
                    match.getSp().setChooseBF(match.getSp().getChooseBF() - 1);
                }
            } else {
                match.getCheckNumber().add(sb);
                if (key.contains("SPF") || key.contains("RQSPF")) {
                    //不是胜平负也不是让球胜平负
                } else if (key.contains("S") && !key.contains("LOSE")) {
                    match.getSp().setChooseJQS(match.getSp().getChooseJQS() + 1);
                } else if (key.contains("_") && !key.contains("OTHER")) {
                    match.getSp().setChooseBQQ(match.getSp().getChooseBQQ() + 1);
                } else {
                    match.getSp().setChooseBF(match.getSp().getChooseBF() + 1);
                }
            }
            int checkNumber = match.getCheckNumber().size();
            if (checkNumber > 0 && !
                    mapChoosed.containsKey(montherkey + "+" + childkey)) {//有check并且没存在已选单中
                mapChoosed.put(montherkey + "+" + childkey, match);
            }
            if (checkNumber <= 0 && mapChoosed.containsKey(montherkey + "+" + childkey)) {//没check并且存在已选单中
                mapChoosed.remove(montherkey + "+" + childkey);
            }
            adpater.notifyDataSetChanged();

            setNum();
        }

        class ViewChildHolder {
            public ImageView childicon;
            public TextView titlename, titletime, titleend;
            public TextView titlezhu, titleke;
            public View child;
            public TextView history;
            public TextView zhushuju, keshuju;
            public TextView shengpei, pingpei, fupei;
            public ImageView shengpeiim, pingpeiim, fupeiim;
            public View saishifenxi, yuce;


            public View dan;
            public View danview;
            public View danspf;
            public TextView nodate0;
            public View havedate0;
            public TextView zhusheng0, ping0, kesheng0;

            public View danrqspf;
            public TextView nodate1;
            public View havedate1;
            public TextView rangqiu;
            public TextView zhusheng1, ping1, kesheng1;

            public TextView openall;
            public View openallline;
            public View openalljiaview;

            public View danbqc;
            public TextView nodatebqc;
            public View ss, sp, sf, pp, ps, pf, ff, fs, fp;
            public TextView ssview, spview, sfview, ppview, psview, pfview, ffview, fsview, fpview;
            public TextView sstext, sptext, sftext, pptext, pstext, pftext, fftext, fstext, fptext;

            public View danjqs;
            public TextView nodatejqs;
            public View jqs0, jqs1, jqs2, jqs3, jqs4, jqs5, jqs6, jqs7;
            public TextView jqs0text, jqs1text, jqs2text, jqs3text, jqs4text, jqs5text, jqs6text, jqs7text;
            public TextView jqs0view, jqs1view, jqs2view, jqs3view, jqs4view, jqs5view, jqs6view, jqs7view;

            public View danbf;
            public TextView nodatebf;
            public View bfview;
            public TextView bf;

        }

        class ViewMotherHolder {
            public ImageView hot;
            public ImageView mothericon;
            public TextView mothertext;
        }

        /**
         * -------------------其他设置-------------------------------------------------------------------
         */

        //孩子在指定的位置是可选的，即：arms中的元素是可点击的
        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {
            return true;
        }

        //表示孩子是否和组ID是跨基础数据的更改稳定
        public boolean hasStableIds() {
            return true;
        }
    }

    ;

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TODETAIL:
                    Bundle b = data.getExtras();
                    FootBallMatch match = (FootBallMatch) b.getSerializable("match");
                    int motherkay = b.getInt("motherkay");
                    int childkey = b.getInt("childkey");

                    if (match.getCheckNumber().size() > 0) {//选了这个比赛
                        mapChoosed.put(motherkay + "+" + childkey, match);
                    }
                    //没选这个比赛，但是在已选名单中
                    if (match.getCheckNumber().size() <= 0 && mapChoosed.containsKey(motherkay + "+" + childkey)) {
                        mapChoosed.remove(motherkay + "+" + childkey);
                    }

                    String key = parent.get(motherkay);
                    ((ArrayList<FootBallMatch>) mapChild.get(key)).set(childkey, match);
                    adpater.notifyDataSetChanged();
                    setNum();
                    break;
            }
        }
    }

    public void Loading() {
        if (!isShowing) {
            isShowing = true;
            load.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    i++;
                    Message mesasge = new Message();
                    mesasge.what = i;
                    handler.sendMessage(mesasge);
                }
            }, 0, 100);
        }
    }

    public void LoadingStop() {
        isShowing = false;
        loading.setVisibility(View.GONE);
        load.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoadingStop();
        timer.cancel();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        Loading();
        clear();
        change = true;
        setFootBall();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (add) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("matchs", mapChoosed);
                    if (change) {
                        bundle.putBoolean("change", change);
                    }
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                }
                _this.finish();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
