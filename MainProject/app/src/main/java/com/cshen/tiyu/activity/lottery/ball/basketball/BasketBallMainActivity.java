package com.cshen.tiyu.activity.lottery.ball.basketball;

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

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.ball.BasketBallMatch;
import com.cshen.tiyu.domain.ball.BasketBallMatchList;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeDXF;
import com.cshen.tiyu.domain.ball.ScroeRFSF;
import com.cshen.tiyu.domain.ball.ScroeSF;
import com.cshen.tiyu.domain.ball.ScroeSFC;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.VerticalSwipeRefreshLayout;

public class BasketBallMainActivity extends BaseActivity
        implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int SF = 1;//胜负
    public static final int RFSF = 2;//让分胜负
    public static final int SFC = 3;//胜分差
    public static final int DXF = 4;//大小分
    public static final int HH = 5;//混合
    public static final int TODETAIL = 10;//详情选择
    private BasketBallMainActivity _this;
    private ExpandableListView exListView;
    private ExpandableListAdapter adpater;
    private TextView tv_head, tv_head_item;//头
    private ImageView tv_head_title_im;
    private ImageView shuoming, back;
    private PopupWindow pop;//玩法弹出框
    private View viewPop;

    private int wanfa = HH;//胜平负状态
    private ImageView clear;//清除
    private TextView number;//场次
    private View sure;//确定

    private ArrayList<String> parent = new ArrayList<String>();
    private HashMap mapChild = new HashMap<String, List<BasketBallMatch>>();
    private HashMap mapChoosed = new HashMap<String, BasketBallMatch>();

    private ImageView loading;
    private View load, nodataview;
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
    private boolean isToDetil = false;
    private VerticalSwipeRefreshLayout mSwipeLayout;

    View yindao, yindao1, yindao2, yindao3;
    TextView tioaguo1, tioaguo2, tioaguo3, next1, next2;

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
        tv_head = (TextView) findViewById(R.id.tv_head_title);
        tv_head.setText("竞篮—");
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
        findViewById(R.id.notice).setVisibility(View.GONE);
        findViewById(R.id.line).setVisibility(View.GONE);
        nodataview = findViewById(R.id.nodataview);
        setNum();
        yindao = findViewById(R.id.yindaobasketball);
        yindao1 = findViewById(R.id.yindao5);
        yindao2 = findViewById(R.id.yindao6);
        yindao3 = findViewById(R.id.yindao7);
        tioaguo1 = (TextView) findViewById(R.id.tiaoguo5);
        tioaguo1.setOnClickListener(this);
        tioaguo2 = (TextView) findViewById(R.id.tiaoguo6);
        tioaguo2.setOnClickListener(this);
        tioaguo3 = (TextView) findViewById(R.id.tiaoguo7);
        tioaguo3.setOnClickListener(this);
        next1 = (TextView) findViewById(R.id.next5);
        next1.setOnClickListener(this);
        next2 = (TextView) findViewById(R.id.next6);
        next2.setOnClickListener(this);
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
                case SF:
                    tv_head_item.setText("胜负");
                    break;
                case RFSF:
                    tv_head_item.setText("让分胜负");
                    break;
                case SFC:
                    tv_head_item.setText("胜分差");
                    break;
                case DXF:
                    tv_head_item.setText("大小分");
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

        if (JCLQUtil.getJCLQUtil().isFiveMin(_this, ConstantsBase.CHOOSEDNUMBERSJCLQTIME) && !add) {
            setBasketBall();
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
                        BasketBallMatch match = (BasketBallMatch) entry.getValue();
                        String key = parent.get(motherkay);
                        ((ArrayList<BasketBallMatch>) mapChild.get(key)).set(childkey, match);
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

    public void setBasketBall() {
        ServiceCaiZhongInformation instance = ServiceCaiZhongInformation.getInstance();
        instance.pastBasketBallMatches(_this, "", "4", "2",
                ConstantsBase.JCLQ + "", new CallBack<BasketBallMatchList>() {
                    @Override
                    public void onSuccess(BasketBallMatchList t) {
                        // TODO 自动生成的方法存根
                        if (t.getResultList() != null && t.getResultList().size() > 0) {
                            Date curDate = new Date(System.currentTimeMillis());
                            ChooseUtil.setDate(_this, ConstantsBase.CHOOSEDNUMBERSJCLQTIME, curDate);
                            ChooseUtil.setData(_this, ConstantsBase.CHOOSEDNUMBERSJCLQ, t.getResultList());
                            new GetMatches().execute();
                        } else {
                            LoadingStop();
                            showNoDate(true);
                        }
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存根
                        LoadingStop();
                        ToastUtils.showShort(_this, errorMessage.msg);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
    }

    class GetMatches extends AsyncTask<String, Integer, List<BasketBallMatch>> {
        List<BasketBallMatch> _departmentList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading();
        }

        @Override
        protected List<BasketBallMatch> doInBackground(String... arg0) {
            ArrayList<BasketBallMatch> match = (ArrayList<BasketBallMatch>)
                    ChooseUtil.getData(_this, ConstantsBase.CHOOSEDNUMBERSJCLQ);
            return match;
        }

        @Override
        protected void onPostExecute(List<BasketBallMatch> match) {
            super.onPostExecute(match);
            if (match != null) {
                parent.clear();
                mapChild.clear();
                adpater.notifyDataSetChanged();
                for (int i = 0; i < match.size(); i++) {
                    String key = match.get(i).getMatchKey().substring(0, 8);
                    if (parent.contains(key)) {
                        ((ArrayList<BasketBallMatch>) mapChild.get(key)).add(match.get(i));
                    } else {
                        parent.add(key);
                        List<BasketBallMatch> list = new ArrayList<BasketBallMatch>();
                        list.add(match.get(i));
                        mapChild.put(key, list);
                    }
                }
                adpater.setDateMother(parent);
                adpater.setDateChild(mapChild);
                exListView.expandGroup(0);
                adpater.notifyDataSetChanged();
                showNoDate(false);
            } else {
                showNoDate(true);
            }
            if (!change) {
                initdataDetail();
            }
            if (!PreferenceUtil.getBoolean(BasketBallMainActivity.this, "is_user_yindao2_showed")) {
                yindao.setVisibility(View.VISIBLE);
                yindao1.setVisibility(View.VISIBLE);
            } else {
                yindao.setVisibility(View.GONE);
            }
            LoadingStop();
        }

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
                            case SF:
                                setHeadBack(SF);
                                break;
                            case RFSF:
                                setHeadBack(RFSF);
                                break;
                            case SFC:
                                setHeadBack(SFC);
                                break;
                            case DXF:
                                setHeadBack(DXF);
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
                intentHelp.putExtra("url", "http://an.caiwa188.com/help/jclq.html");
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
            case R.id.tiaoguo5:
            case R.id.tiaoguo6:
            case R.id.tiaoguo7:
                PreferenceUtil.putBoolean(_this, "is_user_yindao2_showed", true);
                yindao.setVisibility(View.GONE);
                yindao.setClickable(false);
                break;
            case R.id.next5:
                yindao1.setVisibility(View.GONE);
                yindao2.setVisibility(View.VISIBLE);
                break;
            case R.id.next6:
                yindao2.setVisibility(View.GONE);
                yindao3.setVisibility(View.VISIBLE);
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
            BasketBallMatch match = (BasketBallMatch) entry.getValue();
            match.getCheckNumber().clear();
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
                BasketBallMatch match = (BasketBallMatch) entry.getValue();
                matchs.add(match);
            }

            boolean isSingle = JCLQUtil.getJCLQUtil().isSingleBasket(matchs);
            if (!isSingle) {
                ToastUtils.showShort(_this, "至少选两场比赛");
                return;
            }
        }
        if (!add) {
            Intent intent = new Intent(_this, BasketBallAccountListActivity.class);
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
                Intent intent2 = new Intent(_this, BasketBallAccountListActivity.class);
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
        viewPop = inflater.inflate(R.layout.basketballmainchooseview, null);
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
            case R.id.sf_item:
                if (wanfa != SF) {
                    change = true;
                }
                wanfa = SF;
                break;
            case R.id.rfsf_item:
                if (wanfa != RFSF) {
                    change = true;
                }
                wanfa = RFSF;
                break;
            case R.id.sfc_item:
                if (wanfa != SFC) {
                    change = true;
                }
                wanfa = SFC;
                break;
            case R.id.dxf_item:
                if (wanfa != DXF) {
                    change = true;
                }
                wanfa = DXF;
                break;
            case R.id.hunhe_item:
                if (wanfa != HH) {
                    change = true;
                }
                wanfa = HH;
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
            if (JCLQUtil.getJCLQUtil().isFiveMin(_this, ConstantsBase.CHOOSEDNUMBERSJCLQTIME)) {
                setBasketBall();
            }
        }
    }

    public void setHeadBack(int wanfa) {
        ((TextView) viewPop.findViewById(R.id.sf_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.sf_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.rfsf_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.rfsf_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.sfc_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.sfc_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.dxf_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.dxf_item).setBackgroundResource(R.drawable.dlt_tzback);

        ((TextView) viewPop.findViewById(R.id.hunhe_itemtv)).setTextColor(Color.parseColor("#666666"));
        viewPop.findViewById(R.id.hunhe_item).setBackgroundResource(R.drawable.dlt_tzback);


        switch (wanfa) {
            case SF:
                tv_head_item.setText("胜负");
                ((TextView) viewPop.findViewById(R.id.sf_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.sf_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case RFSF:
                tv_head_item.setText("让分胜负");
                ((TextView) viewPop.findViewById(R.id.rfsf_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.rfsf_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;
            case SFC:
                tv_head_item.setText("胜分差");
                ((TextView) viewPop.findViewById(R.id.sfc_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.sfc_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
                break;

            case DXF:
                tv_head_item.setText("大小分");
                ((TextView) viewPop.findViewById(R.id.dxf_itemtv)).setTextColor(Color.parseColor("#ff812a"));
                viewPop.findViewById(R.id.dxf_item).setBackgroundResource(R.drawable.footballchooseview_itemback);
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
        HashMap childExam = new HashMap<String, ArrayList<BasketBallMatch>>();

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
                    return ((ArrayList<BasketBallMatch>) childExam.get(key)).size();
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
            holder.mothertext.setText(DateUtils.SubToDayChinese3(motherTitle) + " " + size + "场可投");
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
                    return ((ArrayList<BasketBallMatch>) childExam.get(key)).get(childPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new String("暂无数据");
            }
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            if (wanfa == SF) {
                return SF;
            }
            if (wanfa == RFSF) {
                return RFSF;
            }
            if (wanfa == HH) {
                return HH;
            }
            if (wanfa == SFC) {
                return SFC;
            }
            if (wanfa == DXF) {
                return DXF;
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
            final BasketBallMatch match = (BasketBallMatch) getChild(groupPosition, childPosition);
            int type = getChildType(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ViewChildHolder();
                switch (type) {
                    case SF:
                        convertView = View.inflate(_this, R.layout.basketballmain_itemchildsf, null);
                        holder.nodatesf = (TextView) convertView.findViewById(R.id.nodate);
                        holder.havedatesf = convertView.findViewById(R.id.havedate);
                        holder.zs = convertView.findViewById(R.id.view2);
                        holder.zsview = (TextView) convertView.findViewById(R.id.txtview2);
                        holder.zstext = (TextView) convertView.findViewById(R.id.txt2);
                        holder.ks = convertView.findViewById(R.id.view1);
                        holder.ksview = (TextView) convertView.findViewById(R.id.txtview1);
                        holder.kstext = (TextView) convertView.findViewById(R.id.txt1);
                        holder.dansf = convertView.findViewById(R.id.danview);
                        break;
                    case RFSF:
                        convertView = View.inflate(_this, R.layout.basketballmain_itemchildsf, null);
                        holder.nodaterfsf = (TextView) convertView.findViewById(R.id.nodate);
                        holder.havedaterfsf = convertView.findViewById(R.id.havedate);
                        holder.zsrf = convertView.findViewById(R.id.view2);
                        holder.zsrfview = (TextView) convertView.findViewById(R.id.txtview2);
                        holder.zsrftext = (TextView) convertView.findViewById(R.id.txt2);
                        holder.ksrf = convertView.findViewById(R.id.view1);
                        holder.ksrfview = (TextView) convertView.findViewById(R.id.txtview1);
                        holder.ksrftext = (TextView) convertView.findViewById(R.id.txt1);
                        holder.danrfsf = convertView.findViewById(R.id.danview);
                        break;
                    case DXF:
                        convertView = View.inflate(_this, R.layout.basketballmain_itemchildsf, null);
                        holder.nodatedxf = (TextView) convertView.findViewById(R.id.nodate);
                        holder.havedatedxf = convertView.findViewById(R.id.havedate);
                        holder.dy = convertView.findViewById(R.id.view1);
                        holder.dyview = (TextView) convertView.findViewById(R.id.txtview1);
                        holder.dytext = (TextView) convertView.findViewById(R.id.txt1);
                        holder.xy = convertView.findViewById(R.id.view2);
                        holder.xyview = (TextView) convertView.findViewById(R.id.txtview2);
                        holder.xytext = (TextView) convertView.findViewById(R.id.txt2);
                        holder.dandxf = convertView.findViewById(R.id.danview);
                        break;
                    case SFC:
                        convertView = View.inflate(_this, R.layout.basketballmain_itemchildsfc, null);
                        holder.nodatesfc = (TextView) convertView.findViewById(R.id.nodatesfc);
                        holder.sfc = (TextView) convertView.findViewById(R.id.sfc);
                        holder.sfcview = convertView.findViewById(R.id.sfcview);
                        holder.dansfc = convertView.findViewById(R.id.danview);
                        break;
                    case HH:
                        convertView = View.inflate(_this, R.layout.basketballmain_itemchildhh, null);
                        holder.dansf = convertView.findViewById(R.id.danviewsf);
                        holder.danrfsf = convertView.findViewById(R.id.danviewrfsf);
                        holder.dandxf = convertView.findViewById(R.id.danviewdxf);
                        holder.dansfc = convertView.findViewById(R.id.danviewsfc);

                        holder.nodatesf = (TextView) convertView.findViewById(R.id.nodatesf);
                        holder.havedatesf = convertView.findViewById(R.id.havedatesf);
                        holder.zs = convertView.findViewById(R.id.zs);
                        holder.zsview = (TextView) convertView.findViewById(R.id.zstxtview);
                        holder.zstext = (TextView) convertView.findViewById(R.id.zstxt);
                        holder.ks = convertView.findViewById(R.id.ks);
                        holder.ksview = (TextView) convertView.findViewById(R.id.kstxtview);
                        holder.kstext = (TextView) convertView.findViewById(R.id.kstxt);
                        holder.sfline = (TextView) convertView.findViewById(R.id.sfline);

                        holder.nodaterfsf = (TextView) convertView.findViewById(R.id.nodaterfsf);
                        holder.havedaterfsf = convertView.findViewById(R.id.havedaterfsf);
                        holder.zsrf = convertView.findViewById(R.id.zsrf);
                        holder.zsrfview = (TextView) convertView.findViewById(R.id.zsrftxtview);
                        holder.zsrftext = (TextView) convertView.findViewById(R.id.zsrftxt);
                        holder.ksrf = convertView.findViewById(R.id.ksrf);
                        holder.ksrfview = (TextView) convertView.findViewById(R.id.ksrftxtview);
                        holder.ksrftext = (TextView) convertView.findViewById(R.id.ksrftxt);
                        holder.rfsfline = (TextView) convertView.findViewById(R.id.rfsfline);

                        holder.nodatedxf = (TextView) convertView.findViewById(R.id.nodatedxf);
                        holder.havedatedxf = convertView.findViewById(R.id.havedatedxf);
                        holder.dy = convertView.findViewById(R.id.dy);
                        holder.dyview = (TextView) convertView.findViewById(R.id.dytxtview);
                        holder.dytext = (TextView) convertView.findViewById(R.id.dytxt);
                        holder.xy = convertView.findViewById(R.id.xy);
                        holder.xyview = (TextView) convertView.findViewById(R.id.xytxtview);
                        holder.xytext = (TextView) convertView.findViewById(R.id.xytxt);

                        holder.nodatesfc = (TextView) convertView.findViewById(R.id.nodatesfc);
                        holder.sfc = (TextView) convertView.findViewById(R.id.sfc);
                        break;
                }
                holder.childicon = (ImageView) convertView.findViewById(R.id.childicon);
                holder.titlename = (TextView) convertView.findViewById(R.id.titlename);
                holder.titletime = (TextView) convertView.findViewById(R.id.titletime);
                holder.titleend = (TextView) convertView.findViewById(R.id.titleend);

                holder.titlezhu = (TextView) convertView.findViewById(R.id.titlezhu);
                holder.titleke = (TextView) convertView.findViewById(R.id.titleke);
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
            if (wanfa == HH || wanfa == SF) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_0()) {
                    //是否单关
                    holder.dansf.setVisibility(View.VISIBLE);
                } else {
                    holder.dansf.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_0()) {
                    //是否过关
                    if (match.getMixSp() != null) {
                        final ScroeSF sf = match.getMixSp().getSF();
                        if (sf != null) {
                            holder.nodatesf.setVisibility(View.INVISIBLE);
                            holder.havedatesf.setVisibility(View.VISIBLE);
                            if (sf.getWIN() != null) {
                                holder.zstext.setText(sf.getWIN().getValue());
                                setString(holder.zs, holder.zsview, holder.zstext,
                                        sf.getWIN(), match.getCheckNumber());
                            }
                            if (sf.getLOSE() != null) {
                                holder.kstext.setText(sf.getLOSE().getValue());
                                setString(holder.ks, holder.ksview, holder.kstext,
                                        sf.getLOSE(), match.getCheckNumber());
                            }
                            if (wanfa == HH) {
                                holder.sfline.setBackgroundColor(Color.parseColor("#DFDFDF"));
                            }

                            holder.zs.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClick(holder.zs, holder.zsview, holder.zstext,
                                            sf.getWIN(), match.getCheckNumber());
                                    setChoose(match, sf.getWIN(), motherkey, childkey);
                                }
                            });
                            holder.ks.setOnClickListener(new View.OnClickListener() {


                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClick(holder.ks, holder.ksview, holder.kstext
                                            , sf.getLOSE(), match.getCheckNumber());
                                    setChoose(match, sf.getLOSE(), motherkey, childkey);
                                }
                            });
                        } else {
                            if (wanfa == HH) {
                                holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                            holder.nodatesf.setText("暂未开售");
                            holder.nodatesf.setVisibility(View.VISIBLE);
                            holder.havedatesf.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (wanfa == HH) {
                            holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        holder.nodatesf.setText("暂未开售");
                        holder.nodatesf.setVisibility(View.VISIBLE);
                        holder.havedatesf.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (wanfa == HH) {
                        holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    holder.nodatesf.setText("暂未开售");
                    holder.nodatesf.setVisibility(View.VISIBLE);
                    holder.havedatesf.setVisibility(View.INVISIBLE);
                }
            }
            if (wanfa == HH || wanfa == RFSF) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_1()) {
                    //是否单关
                    holder.danrfsf.setVisibility(View.VISIBLE);
                } else {
                    holder.danrfsf.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_1()) {
                    //是否过关
                    if (match.getMixSp() != null) {
                        final ScroeRFSF rfsf = match.getMixSp().getRFSF();
                        if (rfsf != null) {
                            holder.nodaterfsf.setVisibility(View.INVISIBLE);
                            holder.havedaterfsf.setVisibility(View.VISIBLE);

                            if (rfsf.getSF_WIN() != null) {
                                holder.zsrftext.setText(rfsf.getSF_WIN().getValue());
                                setStringColor(holder.zsrf, holder.zsrfview, holder.zsrftext,
                                        rfsf.getSF_WIN(), match.getHandicap(), match.getCheckNumber());
                            }
                            if (rfsf.getSF_LOSE() != null) {

                                holder.ksrftext.setText(rfsf.getSF_LOSE().getValue());
                                setString(holder.ksrf, holder.ksrfview, holder.ksrftext,
                                        rfsf.getSF_LOSE(), match.getCheckNumber());
                            }
                            if (wanfa == HH) {
                                holder.rfsfline.setBackgroundColor(Color.parseColor("#DFDFDF"));
                            }
                            holder.zsrf.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClickColor(holder.zsrf, holder.zsrfview, holder.zsrftext,
                                            rfsf.getSF_WIN(), match.getHandicap(), match.getCheckNumber());
                                    setChoose(match, rfsf.getSF_WIN(), motherkey, childkey);
                                }
                            });

                            holder.ksrf.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClick(holder.ksrf, holder.ksrfview, holder.ksrftext,
                                            rfsf.getSF_LOSE(), match.getCheckNumber());
                                    setChoose(match, rfsf.getSF_LOSE(), motherkey, childkey);
                                }
                            });
                        } else {
                            if (wanfa == HH) {
                                holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                            holder.nodaterfsf.setText("暂未开售");
                            holder.nodaterfsf.setVisibility(View.VISIBLE);
                            holder.havedaterfsf.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (wanfa == HH) {
                            holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        holder.nodaterfsf.setVisibility(View.VISIBLE);
                        holder.nodaterfsf.setText("暂未开售");
                        holder.havedaterfsf.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (wanfa == HH) {
                        holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    holder.nodaterfsf.setText("暂未开售");
                    holder.nodaterfsf.setVisibility(View.VISIBLE);
                    holder.havedaterfsf.setVisibility(View.INVISIBLE);
                }
            }
            if (wanfa == HH || wanfa == SFC) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_2()) {
                    //是否单关
                    holder.dansfc.setVisibility(View.VISIBLE);
                } else {
                    holder.dansfc.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_2()) {
                    //是否过关
                    if (match.getMixSp() != null) {
                        final ScroeSFC sfc = match.getMixSp().getSFC();
                        if (sfc != null) {
                            holder.nodatesfc.setVisibility(View.INVISIBLE);
                            holder.sfc.setVisibility(View.VISIBLE);
                            String sfcStr = JCLQUtil.getJCLQUtil().isHaveSFC(match.getCheckNumber());
                            if (sfcStr != null && sfcStr.length() > 1) {
                                sfcStr = sfcStr.substring(0, sfcStr.length() - 1);
                                holder.sfc.setText(sfcStr);
                                holder.sfc.setTextColor(Color.parseColor("#ec3633"));
                                if (wanfa == SFC) {
                                    holder.sfcview.setBackgroundResource(R.drawable.bf_tzback);
                                }
                            } else {
                                if (wanfa == SFC) {
                                    holder.sfc.setText("点击选择胜负差");
                                    holder.sfc.setTextColor(Color.parseColor("#ff812a"));
                                    holder.sfcview.setBackgroundResource(R.drawable.dlt_tzback);
                                } else {
                                    holder.sfc.setText("展开胜分差玩法");
                                    holder.sfc.setTextColor(Color.parseColor("#888888"));
                                }

                            }
                            holder.sfc.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    isToDetil = true;
                                    Intent intent = new Intent(_this, BasketBallDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("match", match);
                                    bundle.putInt("motherkay", motherkey);
                                    bundle.putInt("childkey", childkey);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, TODETAIL);
                                }
                            });
                        } else {
                            holder.nodatesfc.setText("暂未开售");
                            holder.nodatesfc.setVisibility(View.VISIBLE);
                            holder.sfc.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodatesfc.setVisibility(View.VISIBLE);
                        holder.nodatesfc.setText("暂未开售");
                        holder.sfc.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodatesfc.setText("暂未开售");
                    holder.nodatesfc.setVisibility(View.VISIBLE);
                    holder.sfc.setVisibility(View.INVISIBLE);
                }


            }
            if (wanfa == HH || wanfa == DXF) {
                if (match.getMixOpen() != null && match.getMixOpen().isSingle_3()) {
                    //是否单关
                    holder.dandxf.setVisibility(View.VISIBLE);
                } else {
                    holder.dandxf.setVisibility(View.GONE);
                }
                if (match.getMixOpen() != null && match.getMixOpen().isPass_3()) {
                    //是否过关
                    if (match.getMixSp() != null) {
                        final ScroeDXF dxf = match.getMixSp().getDXF();
                        if (dxf != null) {

                            holder.dyview.setText("大于" + match.getTotalScore());
                            holder.xyview.setText("小于" + match.getTotalScore());
                            holder.nodatedxf.setVisibility(View.INVISIBLE);
                            holder.havedatedxf.setVisibility(View.VISIBLE);
                            if (dxf.getLARGE() != null) {
                                holder.dytext.setText(dxf.getLARGE().getValue());
                                setString(holder.dy, holder.dyview, holder.dytext,
                                        dxf.getLARGE(), match.getCheckNumber());
                            }
                            if (dxf.getLITTLE() != null) {
                                holder.xytext.setText(dxf.getLITTLE().getValue());
                                setString(holder.xy, holder.xyview, holder.xytext,
                                        dxf.getLITTLE(), match.getCheckNumber());
                            }

                            holder.dy.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClick(holder.dy, holder.dyview, holder.dytext,
                                            dxf.getLARGE(), match.getCheckNumber());
                                    setChoose(match, dxf.getLARGE(), motherkey, childkey);
                                }
                            });

                            holder.xy.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (match.getCheckNumber().size() == 0 && mapChoosed != null && mapChoosed.size() == 8) {
                                        ToastUtils.showShort(_this, "最多选择8场比赛");
                                        return;
                                    }
                                    setStringClick(holder.xy, holder.xyview, holder.xytext,
                                            dxf.getLITTLE(), match.getCheckNumber());
                                    setChoose(match, dxf.getLITTLE(), motherkey, childkey);
                                }
                            });
                        } else {
                            holder.nodatedxf.setText("暂未开售");
                            holder.nodatedxf.setVisibility(View.VISIBLE);
                            holder.havedatedxf.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.nodatedxf.setVisibility(View.VISIBLE);
                        holder.nodatedxf.setText("暂未开售");
                        holder.havedatedxf.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.nodatedxf.setText("暂未开售");
                    holder.nodatedxf.setVisibility(View.VISIBLE);
                    holder.havedatedxf.setVisibility(View.INVISIBLE);
                }
            }

            return convertView;
        }

        class ViewChildHolder {
            public ImageView childicon;
            public View dansf, danrfsf, dandxf, dansfc;
            public TextView titlename, titletime, titleend;
            public TextView titlezhu, titleke;
            public TextView rangqiu;
            public TextView nodatesf, nodaterfsf, nodatedxf, nodatesfc;
            public View havedatesf, havedaterfsf, havedatedxf;
            public View zs, ks;
            public TextView zsview, zstext, ksview, kstext, sfline;
            public View zsrf, ksrf;
            public TextView zsrfview, zsrftext, ksrfview, ksrftext, rfsfline;
            public View dy, xy;
            public TextView dyview, dytext, xyview, xytext;
            public View sfcview;
            public TextView sfc;

        }

        class ViewMotherHolder {
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

    public void setString(View view, TextView tv0, TextView tv,
                          ScroeBean key, ArrayList<ScroeBean> has) {
        boolean isCheck = false;
        for (ScroeBean sb : has) {
            if (sb.getKey().equals(key.getKey())) {
                isCheck = true;
                break;
            }
        }
        setTextColor(view, tv0, tv, isCheck);
    }

    public void setStringColor(View view, TextView tv0, TextView tv,
                               ScroeBean key, String hand, ArrayList<ScroeBean> has) {
        boolean isCheck = false;
        for (ScroeBean sb : has) {
            if (sb.getKey().equals(key.getKey())) {
                isCheck = true;
                break;
            }
        }
        setTextColorText(view, tv0, tv, hand, isCheck);
    }

    public void setStringClick(View view, TextView tv0, TextView tv,
                               ScroeBean key, ArrayList<ScroeBean> has) {
        boolean isCheck = true;
        for (ScroeBean sb : has) {
            if (sb.getKey().equals(key.getKey())) {
                isCheck = false;
                break;
            }
        }
        setTextColor(view, tv0, tv, isCheck);
    }

    public void setStringClickColor(View view, TextView tv0, TextView tv,
                                    ScroeBean key, String hand, ArrayList<ScroeBean> has) {
        boolean isCheck = true;
        for (ScroeBean sb : has) {
            if (sb.getKey().equals(key.getKey())) {
                isCheck = false;
                break;
            }
        }
        setTextColorText(view, tv0, tv, hand, isCheck);
    }

    public void setTextColor(View view, TextView tv0, TextView tv, boolean isCheck) {
        if (isCheck) {
            tv0.setTextColor(Color.parseColor("#ffffff"));
            tv.setTextColor(Color.parseColor("#ffffff"));
            if (wanfa != HH) {
                view.setBackgroundResource(R.drawable.cornerfullred);
            } else {
                view.setBackgroundColor(Color.parseColor("#FF3232"));
            }
        } else {
            tv0.setTextColor(Color.parseColor("#333333"));
            tv.setTextColor(Color.parseColor("#888888"));
            if (wanfa != HH) {
                view.setBackgroundResource(R.drawable.dlt_tzback);
            } else {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }

    ;

    public void setTextColorText(View view, TextView tv0, TextView tv, String hand, boolean isCheck) {
        String Str = "";
        float handicap = Float.parseFloat(hand);
        if (isCheck) {
            if (handicap <= 0) {
                Str = "<html><font color=\"#ffffff\">主胜"
                        + "</font><font color=\"#ffffff\">(" + hand
                        + ")</font></html>";
            } else {
                Str = "<html><font color=\"#ffffff\">主胜"
                        + "</font><font color=\"#ffffff\">(+" + hand
                        + ")</font></html>";
            }
            tv0.setText(Html.fromHtml(Str));
            tv.setTextColor(Color.parseColor("#ffffff"));
            if (wanfa != HH) {
                view.setBackgroundResource(R.drawable.cornerfullred);
            } else {
                view.setBackgroundColor(Color.parseColor("#FF3232"));
            }
        } else {
            if (handicap <= 0) {
                Str = "<html><font color=\"#333333\">主胜"
                        + "</font><font color=\"#1f8b16\">(" + hand
                        + ")</font></html>";
            } else {
                Str = "<html><font color=\"#333333\">主胜"
                        + "</font><font color=\"#FF3232\">(+" + hand
                        + ")</font></html>";
            }
            tv0.setText(Html.fromHtml(Str));
            tv.setTextColor(Color.parseColor("#888888"));
            if (wanfa != HH) {
                view.setBackgroundResource(R.drawable.dlt_tzback);
            } else {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }

    ;

    public void setChoose(BasketBallMatch match, ScroeBean key, int montherkey, int childkey) {
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TODETAIL:
                    Bundle b = data.getExtras();
                    BasketBallMatch match = (BasketBallMatch) b.getSerializable("match");
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
                    ((ArrayList<BasketBallMatch>) mapChild.get(key)).set(childkey, match);
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

    public void showNoDate(boolean nodate) {
        if (nodate) {
            nodataview.setVisibility(View.VISIBLE);
        } else {
            nodataview.setVisibility(View.GONE);
        }
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
        setBasketBall();
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