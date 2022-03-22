package com.cshen.tiyu.activity.mian4.find.message;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;

public class MyGridAdapter extends BaseAdapter {

   private ArrayList<GridItemsBean>mDataList;
   private Context mContext;
   private boolean isFromPersonCenter;//是否是来自个人中心页面，因为个人中心页面图标较小

	public MyGridAdapter(ArrayList<GridItemsBean> mDataList, Context mContext,boolean isFromPersonCenter) {
	this.mDataList = mDataList;
	this.mContext = mContext;
	this.isFromPersonCenter=isFromPersonCenter;
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
		GridItemsBean dataBean = mDataList.get(position);
		View view=null;
		if (isFromPersonCenter) {
			view = View.inflate(mContext, R.layout.grid_lotterytype_item_small, null);
		}else {
			view = View.inflate(mContext, R.layout.grid_lotterytype_item, null);
		}
		
		ImageView imageView = (ImageView) view
				.findViewById(R.id.iv_lottery_icon);

		TextView tv_lottery_title = (TextView) view
				.findViewById(R.id.tv_lottery_title);
		
		TextView tv_lottery_info=(TextView) view.findViewById(R.id.tv_lottery_info);
		
		imageView.setImageResource(dataBean.getIconRes());
		tv_lottery_title.setText(dataBean.getTitle());
	
		tv_lottery_info.setText(dataBean.getInfo());
		
		

		return view;

	}
}

