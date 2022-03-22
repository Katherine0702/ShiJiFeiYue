package com.cshen.tiyu.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

public class LotteryTypeView extends LinearLayout {
    private Context mContext;
    private GridView mGridView;
    /**
     * 滚动图片视图适配
     */

    ArrayList<LotteryType> mlotteryTypes;// 外面传进来的数据

    /**
     * @param context
     */
    public LotteryTypeView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    @SuppressLint("Recycle")
    public LotteryTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.lottery_grid_view, this);
        mGridView = (GridView) findViewById(R.id.gv_lottery);// 我的grideview

    }

    /**
     * 装填图片数据设置适配
     *
     * @paramimageUrlList
     * @paramimageCycleViewListener
     */
    public void setResources(ArrayList<LotteryType> lotteryTypes,
                             final LotteryTypeListener listener, int numColumns) {
        mlotteryTypes = lotteryTypes;
        mGridView.setAdapter(new LotteryTypeViewAdapter());
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO 自动生成的方法存根
                listener.onItemClick(position, arg0);
            }

        });
    }

    private class LotteryTypeViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO 自动生成的方法存根
            if (mlotteryTypes == null) {
                return 0;
            }
            return mlotteryTypes.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO 自动生成的方法存根
            if (mlotteryTypes == null) {
                return null;
            }
            return mlotteryTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO 自动生成的方法存根
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO 自动生成的方法存根
            //
            LotteryType ltLotteryType = mlotteryTypes.get(position);
            View view = View.inflate(mContext, R.layout.grid_lotterytype_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.iv_lottery_icon);

            switch (ltLotteryType.getLotteryId()) {
                case ConstantsBase.DLT + "":
                    //imageView.setImageResource(R.mipmap.newdaletou);
                    xUtilsImageUtils.display(imageView, R.mipmap.newdaletou, ltLotteryType.getIcon());
                    break;
                case ConstantsBase.SSQ + "":
                    //imageView.setImageResource(R.mipmap.newshuangseqiu);
                    xUtilsImageUtils.display(imageView, R.mipmap.newshuangseqiu, ltLotteryType.getIcon());
                    break;
                case ConstantsBase.SD115 + "":
                    //imageView.setImageResource(R.mipmap.new11xuan5);
                    xUtilsImageUtils.display(imageView, R.mipmap.new11xuan5, ltLotteryType.getIcon());
                    break;

                case ConstantsBase.GD115 + "":
                    //imageView.setImageResource(R.mipmap.new11xuan5);
                    xUtilsImageUtils.display(imageView, R.mipmap.new11xuan5, ltLotteryType.getIcon());
                    break;
                case ConstantsBase.Fast3 + "":
                    //imageView.setImageResource(R.mipmap.newkuaisan);
                    xUtilsImageUtils.display(imageView, R.mipmap.newkuaisan, ltLotteryType.getIcon());
                    break;
                case ConstantsBase.JCZQ + "":
                    //imageView.setImageResource(R.mipmap.newzuqiu);
                    xUtilsImageUtils.display(imageView, R.mipmap.newzuqiu, ltLotteryType.getIcon());
                    break;

                case ConstantsBase.JCLQ + "":
                    //imageView.setImageResource(R.mipmap.newlanqiu);
                    xUtilsImageUtils.display(imageView, R.mipmap.newlanqiu, ltLotteryType.getIcon());
                    break;
                case ConstantsBase.SFC + "":
                    if ("0".equals(ltLotteryType.getPlayType())) {
                        //imageView.setImageResource(R.mipmap.newsfc);
                        xUtilsImageUtils.display(imageView, R.mipmap.newsfc, ltLotteryType.getIcon());
                    }
                    if ("1".equals(ltLotteryType.getPlayType())) {
                        //imageView.setImageResource(R.mipmap.newsxj);
                        xUtilsImageUtils.display(imageView, R.mipmap.newsxj, ltLotteryType.getIcon());
                    }
                    break;
                default:
                    xUtilsImageUtils.display(imageView, R.mipmap.ic_error, ltLotteryType.getIcon());
                    break;
            }


            TextView tv_lottery_title = (TextView) view
                    .findViewById(R.id.tv_lottery_title);
            tv_lottery_title.setText(ltLotteryType.getTitle());
            TextView tv_lottery_info = (TextView) view.findViewById(R.id.tv_lottery_info);
            if (ltLotteryType.getInfo() == null) {
                tv_lottery_info.setVisibility(View.GONE);
            } else {
                tv_lottery_info.setVisibility(View.VISIBLE);
                if ("true2".equals(ltLotteryType.getUseLocal())) {
                    tv_lottery_info.setText(ltLotteryType.getInfo());
                } else {
                    switch (ltLotteryType.getLotteryId()) {
                        case ConstantsBase.JCZQ + "":
                            tv_lottery_info.setText("单关更易中奖");
                            break;
                        case ConstantsBase.JCLQ + "":
                            tv_lottery_info.setText("玩转NBA赛事");
                            break;
                        case ConstantsBase.DLT + "":
                            tv_lottery_info.setText("3元追加可中1600万");
                            break;
                        case ConstantsBase.SSQ + "":
                            tv_lottery_info.setText("2元赢1000万");
                            break;
                        case ConstantsBase.SD115 + "":
                            tv_lottery_info.setText("十分钟一期");
                            break;
                        case ConstantsBase.Fast3 + "":
                            tv_lottery_info.setText("每十分钟开奖");
                            break;
                        case ConstantsBase.SFC + "":
                            if ("0".equals(ltLotteryType.getPlayType())) {
                                tv_lottery_info.setText("猜胜负赢500万");
                            }
                            if ("1".equals(ltLotteryType.getPlayType())) {
                                tv_lottery_info.setText("9场竞彩赚翻天");
                            }
                            break;
                        default:
                            tv_lottery_info.setText(ltLotteryType.getInfo());
                            break;
                    }
                }
            }


            return view;

        }

    }


    /**
     * 轮播控件的每一个监听器
     *
     * @author minking
     */
    public static interface LotteryTypeListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @paramimageView
         */
        public void onItemClick(int position, View view);
    }

}
