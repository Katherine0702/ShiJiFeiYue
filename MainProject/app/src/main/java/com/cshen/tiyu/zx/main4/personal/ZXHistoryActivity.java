package com.cshen.tiyu.zx.main4.personal;

import java.util.ArrayList;
import java.util.Collections;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.main.MessageDetailActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ZXHistoryActivity extends BaseActivity {
	private ZXHistoryActivity _this;
	private View clear,nodata;
	private ListView listView;
	private MessageAdapter adapter;
	private ArrayList<NewslistBean>	messages = new ArrayList<NewslistBean>();
	private AlertDialog  alertDialog ,alertDialogDelete;
	private TextView tv_nodata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.messagelist);
		initView();
	}

	public void initView(){
		TextView title=(TextView)findViewById(R.id.tv_head_title);
		title.setText("浏览历史");
		tv_nodata=(TextView) findViewById(R.id.guanzhutxt);
		tv_nodata.setText("暂无数据");
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
		clear = findViewById(R.id. clear);
		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNormalDialog();
			}
		});
		nodata = findViewById(R.id.nodata);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new MessageAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position>=messages.size()) {
					return;
				}
				 Intent intent = new Intent(_this, MessageDetailActivity.class);
				 String newsId=messages.get(position).getId();
				 intent.putExtra("id", newsId);
				 intent.putExtra("newsType", 5);
                 startActivity(intent);
			}			
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(messages!=null&&position<messages.size()&&position>=0){
					showDeleteDialog(position);
				}
				
				return true;
			}
			
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetMessages().execute();
	}

	class GetMessages extends AsyncTask<String, Integer, ArrayList<NewslistBean>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(messages!=null){
				messages.clear();
			}
		}

		@Override
		protected ArrayList<NewslistBean> doInBackground(String... arg0) {
			messages = MyDbUtils.getCurrentHistoryData();
			return messages;
		}
		@Override
		protected void onPostExecute(ArrayList<NewslistBean> messages) {
			super.onPostExecute(messages);
			if(messages!=null&&messages.size()>0){
				Collections.reverse(messages);  
				adapter.setData(messages);
				adapter.notifyDataSetChanged();
				nodata.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}else{
				nodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		}

	}
	class MessageAdapter extends BaseAdapter {
		ArrayList<NewslistBean>	messes;
		public void setData(ArrayList<NewslistBean> messages) {
			messes = messages;
		}
		@Override
		public int getCount() {
			if(messes == null){
				return 0;
			}else{
				return messes.size();
			}
		}

		@Override
		public NewslistBean getItem(int position) {
			if(messes == null){
				return new NewslistBean();
			}else{
				return (NewslistBean)messes.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			NewslistBean mess = getItem(position);
			if (convertView == null) {  
				viewHolder = new ViewHolder();  
				convertView = LayoutInflater.from(_this).inflate(
						R.layout.item_listview_home_news, parent, false);
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

				convertView.setTag(viewHolder);
			} else {  
				viewHolder = (ViewHolder)convertView.getTag();  
			} 
			if (mess.getAuthor()!=null&&!mess.getAuthor().equals("")) {
				viewHolder.tv_company.setText(mess.getAuthor());
			}else {
				viewHolder.tv_company.setText("58彩牛");
			}
			
			viewHolder.tv_title.setText(mess.getTitle()==null?"":mess.getTitle());
			viewHolder.tv_time.setText(mess.getAddTime()==null?"":mess.getAddTime());
//			viewHolder.tv_reading.setText("阅读"+mess.getViewCount()+"");
			viewHolder.tv_reading.setVisibility(View.GONE);
			if (mess.getImageUrl()!=null&&!mess.getImageUrl().equals("")) { 
				xUtilsImageUtils.display(viewHolder.iv_news,R.mipmap.icon,mess.getImageUrl());
				
			}else {
				viewHolder.iv_news.setImageResource(R.mipmap.icon);
			}
			return convertView;
		}
	}
	class ViewHolder {
		TextView tv_title, tv_company, tv_time, tv_reading;
		ImageView iv_news;
	}
	private void showDeleteDialog(final int position){
		if(alertDialogDelete==null){
			alertDialogDelete = new AlertDialog.Builder(this).create();
			alertDialogDelete.setCancelable(false);
		}
		alertDialogDelete.show();
		Window window = alertDialogDelete.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("删除");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("确定删除本条消息吗？");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("确定");
		ImageView delete = (ImageView) window.findViewById(R.id.delete);
		delete.setVisibility(View.GONE);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("取消");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialogDelete.dismiss();
				alertDialogDelete.cancel();
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
			/**
			 * @param v
			 */
			@Override
			public void onClick(View v) {
				alertDialogDelete.dismiss();
				alertDialogDelete.cancel();
				MyDbUtils.removeHistorySigleData(messages.get(position));
				new GetMessages().execute();
			}
		});
	}
	private void showNormalDialog(){
		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setCancelable(false);
		}
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("清空");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("确定将清空所有消息吗？");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("确定");
		ImageView delete = (ImageView) window.findViewById(R.id.delete);
		delete.setVisibility(View.GONE);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("取消");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
			/**
			 * @param v
			 */
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				MyDbUtils.removeAllCurrentHistoryData();
				new GetMessages().execute();
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(alertDialog!=null){
			alertDialog.dismiss();
			alertDialog.cancel();
		}if(alertDialogDelete!=null){
			alertDialogDelete.dismiss();
			alertDialogDelete.cancel();
		}
	}

}
