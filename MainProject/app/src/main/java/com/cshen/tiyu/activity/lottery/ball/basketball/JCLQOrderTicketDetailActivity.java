package com.cshen.tiyu.activity.lottery.ball.basketball;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.ball.JCLQTicketDetail;
import com.cshen.tiyu.domain.ball.JCLQTicketDetailList;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class JCLQOrderTicketDetailActivity extends BaseActivity {
	JCLQOrderTicketDetailActivity _this;
	ImageView backImageView,lottery_icon;
	TextView lotteryNameView,lotteryWanfaView;
	String lotteryName;

	private RefreshListView refreshListView;

	private TicketListAdapter myListAdapter = null;
	private ArrayList<JCLQTicketDetail> jclqticketdetaillist;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private boolean isGetting = false;

	private int schemeId;// 每次步数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_jclq_cpxq);
		_this = this;
		initView();
		initdata();
	}
	private void initView() {
		backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
		lotteryNameView = (TextView) findViewById(R.id.textview_lotteryName);
		lotteryWanfaView = (TextView) findViewById(R.id.textview_periodNumber);
		lottery_icon = (ImageView) findViewById(R.id.lottery_icon);
		lotteryName = getIntent().getStringExtra("caizhong");
		schemeId = getIntent().getIntExtra("schemeId", -1);	
		lotteryNameView.setText(lotteryName);
		LotteryTypeData currentLotteryTypeData = MyDbUtils.getCurrentLotteryTypeData();
		if (currentLotteryTypeData != null
				&& currentLotteryTypeData.getLotteryList() != null
				&& currentLotteryTypeData.getLotteryList().size() > 0) {
			for (int i = 0; i < currentLotteryTypeData.getLotteryList().size(); i++) {
				if (lotteryName != null&& lotteryName.equals(currentLotteryTypeData.getLotteryList().get(i).getTitle())) {
					xUtilsImageUtils.display(lottery_icon,R.mipmap.jclqicon,currentLotteryTypeData.getLotteryList().get(i).getIcon());
				}
			}
		}
		lotteryWanfaView.setText(getIntent().getStringExtra("wanfa"));
		refreshListView = (RefreshListView)findViewById(R.id.rl_listview);
		myListAdapter = new TicketListAdapter();
		refreshListView.setAdapter(myListAdapter);		
		// 添加监听器
		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根
				requestDataFromServer(false); // 冲服务器中获取刷新的数据
			}

			@Override
			public void onLoadMore() {
				// TODO 自动生成的方法存根
				requestDataFromServer(true);
			}

			private   void  requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if(isGetting){
					return;
				}
				if (isLoadingMore) {
					if(!isGetting){
						isGetting = true;
					}
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					ServiceCaiZhongInformation.getInstance().getJCLQTicketDetail(_this,start,step,ConstantsBase.JCLQ,schemeId,
							new CallBack<JCLQTicketDetailList>() {

						@Override
						public void onSuccess(JCLQTicketDetailList t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(myListAdapter!=null){
								myListAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
							isGetting = false;
						}
					});
				} else {
					initdata();
				}

			}
		});
	}

	void initdata() {
		if(!isGetting){
			isGetting = true;
			getTicketDetail();
		}
	}

	private void getTicketDetail() {
		more = 0;
		ServiceCaiZhongInformation.getInstance().getJCLQTicketDetail(_this,more,step,ConstantsBase.JCLQ,schemeId,
				new CallBack<JCLQTicketDetailList>() {
			@Override
			public void onSuccess(JCLQTicketDetailList t) {
				// TODO 自动生成的方法存根
				dodata(t,true);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(JCLQTicketDetailList t,boolean isRefresh){
		if(isRefresh){
			if(jclqticketdetaillist== null){
				jclqticketdetaillist = new ArrayList<JCLQTicketDetail>();
			}else{
				jclqticketdetaillist.clear();
			}
			if(t.getResultList()!=null&& t.getResultList().size()>0){
				jclqticketdetaillist.addAll(t.getResultList());  
				myListAdapter.setDate(jclqticketdetaillist);
			}
		}else{
			ArrayList<JCLQTicketDetail> newlists = t.getResultList();
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(jclqticketdetaillist== null){
				jclqticketdetaillist = new ArrayList<JCLQTicketDetail>();
			}
			jclqticketdetaillist.addAll(newlists);
		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
		isGetting = false;
	}
	class TicketListAdapter extends BaseAdapter{
		private ArrayList<JCLQTicketDetail> detailLists;
		public void setDate(ArrayList<JCLQTicketDetail> orderListsFrom){
			this.detailLists = orderListsFrom;
		}
		@Override
		public int getCount() {
			if(detailLists == null){
				return 0; 
			}else{
				return detailLists.size();
			}
		}
		@Override
		public JCLQTicketDetail getItem(int position) {
			if(detailLists == null){
				return null; 
			}else{
				return detailLists.get(position);
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			JCLQTicketDetail detail = getItem(position);
			if (convertView == null) {  
				holder = new ViewHolder();  			
				convertView = View.inflate(_this, R.layout.jclqticket_item,null);
				holder.detail = (TextView) convertView.findViewById(R.id.detail);
				holder.wanfa = (TextView)convertView.findViewById(R.id.wanfa);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.success = (TextView) convertView.findViewById(R.id.success);
				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			} 
			if(detail!=null){
				holder.wanfa.setText(detail.getWanfa());
				holder.time.setText(detail.getTime());
				if(detail.getResult()!=null&&detail.getResult().size()>0){
					StringBuffer detailSb = new StringBuffer();
					detailSb.append("<html>");
					for(int i =0;i<detail.getResult().size();i++){
						if(i==0){
							detailSb.append("<font color=\"#333333\">"+detail.getResult().get(i).getMatchKey()+"</font>");
						}else{
							detailSb.append("<font color=\"#333333\">*"+detail.getResult().get(i).getMatchKey()+"</font>");
						}
						detailSb.append("<font color=\"#4873c7\">"+detail.getResult().get(i).getContent()+"</font>");
					}
					detailSb.append("</html>");
					holder.detail.setText(Html.fromHtml(detailSb.toString()));
				}
			}

			return convertView;
		}
		class ViewHolder {
			public TextView detail;
			public TextView wanfa;
			public TextView time;
			public TextView success;
		}
	}

}
