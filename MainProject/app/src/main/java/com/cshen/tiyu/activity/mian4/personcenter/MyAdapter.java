package com.cshen.tiyu.activity.mian4.personcenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.find.FindBean;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

	private ArrayList<FindBean> mDataList;
	private Context mContext;
	boolean isShowRight;

	public MyAdapter(ArrayList<FindBean> mDataList, Context mContext, boolean isShowRight) {
		this.mDataList = mDataList;
		this.mContext = mContext;
		this.isShowRight = isShowRight;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return mDataList.get(position);
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
		FindBean dataBean = mDataList.get(position);
		View view = null;
		view = View.inflate(mContext, R.layout.grid_lotterytype_item_my, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.iv_lottery_icon);

		TextView tv_lottery_title = (TextView) view.findViewById(R.id.tv_lottery_title);

		TextView tv_lottery_info = (TextView) view.findViewById(R.id.tv_lottery_info);

		ImageView rightarrow = (ImageView) view.findViewById(R.id.rightarrow);
		if(this.isShowRight){
			rightarrow.setVisibility(View.VISIBLE);
		}else{
			rightarrow.setVisibility(View.GONE);
		}
		if (dataBean != null) {
			String key = dataBean.getTkey();
			int errorResId = R.mipmap.find_saidan;
			switch (key) {
			case "SD":// 晒单
				errorResId = R.mipmap.find_saidan;
				break;
			case "ZX":// 资讯
				errorResId = R.mipmap.find_zixun;
				break;
			case "BFZB":// 比分直播
				errorResId = R.mipmap.find_bfzb;
				break;
			case "ZXKJ":// 最新开奖
				errorResId = R.mipmap.find_zxkj;
				break;
			case "HDLB":// 活动列表
				errorResId = R.mipmap.find_saidan;
				break;
			case "ZSZX":// 指数中心
				errorResId = R.mipmap.find_zszx;
				break;
			case "SSZL":// 赛事资料
				errorResId = R.mipmap.find_sszl;
				break;

			default:
				errorResId = R.mipmap.find_saidan;
				break;
			}
			xUtilsImageUtils.displayIN(imageView, errorResId, dataBean.getIcon());
			tv_lottery_title.setText(dataBean.getTitle() == null ? "" : dataBean.getTitle());

			tv_lottery_info.setText(dataBean.getInfo() == null ? "" : dataBean.getInfo());
		}

		return view;

	}
}
