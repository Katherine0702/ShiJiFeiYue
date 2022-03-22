package com.cshen.tiyu.activity.mian4.personcenter.message;

import java.util.ArrayList;
import java.util.Collections;

import com.cshen.tiyu.R;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.Message;
import com.cshen.tiyu.utils.StatusBarUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import android.app.AlertDialog;

public class MessageListActivity   extends Activity {
	private MessageListActivity _this;
	private View clear,nodata;
	private ListView listView;
	private MessageAdapter adapter;
	private ArrayList<Message>	messages = new ArrayList<Message>();
	private AlertDialog  alertDialog ,alertDialogDelete;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.messagelist);
		StatusBarUtil.setColor(this, getResources().getColor(R.color.mainred));//状态栏
		initView();
	}

	
	public void initView(){
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
				if(messages!=null&&position<messages.size()&&position>=0){
					Intent	intentMessage = new Intent(_this,MessageDetailActivity.class);
					if(messages.get(position)!=null){
						intentMessage.putExtra("title", messages.get(position).getTitle());
						intentMessage.putExtra("time", messages.get(position).getTime());
						intentMessage.putExtra("content", messages.get(position).getContent());
						intentMessage.putExtra("needSave", false);
					}
					_this.startActivity(intentMessage);		
				}
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetMessages().execute();
	}

	class GetMessages extends AsyncTask<String, Integer, ArrayList<Message>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(messages!=null){
				messages.clear();
			}
		}

		@Override
		protected ArrayList<Message> doInBackground(String... arg0) {
			messages = MyDbUtils.getCurrentMessageData();
			return messages;
		}
		@Override
		protected void onPostExecute(ArrayList<Message> messages) {
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
		ArrayList<Message>	messes;
		public void setData(ArrayList<Message> messages) {
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
		public Message getItem(int position) {
			if(messes == null){
				return new Message();
			}else{
				return (Message)messes.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			Message mess = getItem(position);
			if (convertView == null) {  
				holder = new ViewHolder();  
				convertView = View.inflate(_this, R.layout.message_item,null);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.time = (TextView) convertView.findViewById(R.id.time);

				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			} 
			holder.title.setText(mess.getTitle());
			holder.time.setText(mess.getTime());
			return convertView;
		}
	}
	class ViewHolder {
		public TextView title,time;
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
		title.setText("温馨提示");
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
				MyDbUtils.removeMessageSigleData(messages.get(position));
				new GetMessages().execute();
			}
		});
	}
	private void showNormalDialog(){
		if(alertDialogDelete==null){
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setCancelable(false);
		}
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("温馨提示");
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
				MyDbUtils.removeAllCurrentMessageData();
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

