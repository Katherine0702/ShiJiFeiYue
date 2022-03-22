package com.cshen.tiyu.activity.mian4.main;

import java.util.ArrayList;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<NewslistBean> mDatas;

	public MessageAdapter(Context mContext, ArrayList<NewslistBean> mDatas) {
		super();
		this.mContext = mContext;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_listview_home_news, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.tv_company = (TextView) convertView
					.findViewById(R.id.tv_item_news_company);
			viewHolder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_item_news_title);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_item_news_time);
			viewHolder.tv_reading = (TextView) convertView
					.findViewById(R.id.tv_item_news_reading);
			viewHolder.iv_news=(ImageView) convertView.findViewById(R.id.iv_item_news);
			convertView.setTag(viewHolder);
			

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NewslistBean data=mDatas.get(position);
		if (data!=null) {
			
			if (data.getAuthor()!=null&&!data.getAuthor().equals("")) {
				viewHolder.tv_company.setText(data.getAuthor());
			}else {
				viewHolder.tv_company.setText("58彩牛");
			}
			
			viewHolder.tv_title.setText(data.getTitle()==null?"":data.getTitle());
			viewHolder.tv_time.setText(data.getAddTime()==null?"":data.getAddTime());
			viewHolder.tv_reading.setText("阅读"+data.getViewCount()+"");
			int error_drawable;//加载失败默认图片
			if (PreferenceUtil.getBoolean(mContext, "isMaJia")) {//马甲包
				error_drawable=R.mipmap.majiabaodefult;
			}else {
				error_drawable=R.mipmap.icon;
			}
			if (data.getImageUrl()!=null&&!data.getImageUrl().equals("")) { 
				xUtilsImageUtils.display(viewHolder.iv_news,error_drawable,data.getImageUrl());
				
			}else {
				viewHolder.iv_news.setImageResource(error_drawable);
			}
			
			
		}
  
		return convertView;
	}

	class ViewHolder {
		TextView tv_title, tv_company, tv_time, tv_reading;
		ImageView iv_news;

	}

}
