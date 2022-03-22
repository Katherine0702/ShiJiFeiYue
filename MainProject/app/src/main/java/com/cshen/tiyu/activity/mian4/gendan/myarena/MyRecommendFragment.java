package com.cshen.tiyu.activity.mian4.gendan.myarena;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQOrderDetailActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.MyFollowRecommend;
import com.cshen.tiyu.domain.gendan.MyFollowRecommendList;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


public class MyRecommendFragment extends Fragment {
	private Activity _this;
	private View nodata;
	private TextView tuijian;
	private RefreshListView refreshListView;

	private RecommendAdapter myListAdapter = null;
	private ArrayList<MyFollowRecommend> orderLists;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String userId,userPwd;
	private ServiceGenDan gendanService ;
	private boolean isGetting = false;
	Bundle savedInstanceState;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		View view = inflater.inflate(R.layout.myfouce, container,false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	private void initView(View view) {
		nodata = view.findViewById(R.id.nodata);
		tuijian  = (TextView) view.findViewById(R.id.guanzhu);
		((TextView) view.findViewById(R.id.guanzhutxt)).setText("暂无推荐");
		tuijian.setText("立即推荐");
		tuijian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(_this,FootballMainActivity.class);
				startActivity(intent);
				_this.finish();
			}
		});
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		myListAdapter = new RecommendAdapter();
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

			private void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					gendanService.MyFollowRecommend(_this,start,step,userId,userPwd,"RECCOMMEND",new CallBack<MyFollowRecommendList>() {

						@Override
						public void onSuccess(MyFollowRecommendList t) {
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
						}
					});
				} else {
					initAllOrdersForList();
				}

			}
		});
	}

	void initdata() {
		if(!isGetting&&MyDbUtils.getCurrentUser()!=null){
			isGetting = true;
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
			gendanService = ServiceGenDan.getInstance();
			initAllOrdersForList();
		}
	}

	private void initAllOrdersForList() {
		more = 0;
		gendanService.MyFollowRecommend(_this,more,step,userId,userPwd,"RECCOMMEND",
				new CallBack<MyFollowRecommendList>() {

			@Override
			public void onSuccess(MyFollowRecommendList t) {
				// TODO 自动生成的方法存根	
				dodata(t,true);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(MyFollowRecommendList t,boolean isRefresh){
		if(isRefresh){
			if(orderLists== null){
				orderLists = new ArrayList<MyFollowRecommend>();
			}else{
				orderLists.clear();
			}
			orderLists.addAll(t.getResultList());               
			if (orderLists.size()==0) {
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}else{
				nodata.setVisibility(View.GONE);
				refreshListView.setVisibility(View.VISIBLE);
			}
			myListAdapter.setDate(orderLists);
		}else{

			ArrayList<MyFollowRecommend> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(orderLists== null){
				orderLists = new ArrayList<MyFollowRecommend>();
			}
			orderLists.addAll(newlists);
		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
	}
	class RecommendAdapter extends BaseAdapter {
		private ArrayList<MyFollowRecommend> rzlist;
		public void setDate(ArrayList<MyFollowRecommend> orderListsFrom){
			this.rzlist = orderListsFrom;
		}
		@Override
		public int getCount() {
			if(rzlist == null){
				return 0;
			}else{
				return rzlist.size();
			}
		}

		@Override
		public MyFollowRecommend getItem(int position) {
			if(rzlist == null){
				return new MyFollowRecommend();
			}else{
				return (MyFollowRecommend)rzlist.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final MyFollowRecommend rzp = getItem(position);
			if (convertView == null) {  
				holder = new ViewHolder();  
				convertView = View.inflate(_this, R.layout.gendan_item_myrecommend,null);
				holder.detils = (TextView) convertView.findViewById(R.id.detils);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				holder.matchs = (LinearLayout) convertView.findViewById(R.id.matchs);


				holder.match1 = convertView.findViewById(R.id.match1);
				holder.matchkey1 = (TextView) convertView.findViewById(R.id.matchkey1);
				holder.hostteamname1 = (TextView) convertView.findViewById(R.id.hostteamname1);
				holder.gustteamname1 = (TextView) convertView.findViewById(R.id.gustteamname1);
				holder.touzhu1 = (TextView) convertView.findViewById(R.id.touzhu1);

				holder.chuanfa = (TextView) convertView.findViewById(R.id.chuanfa);
				holder.endtime = (TextView) convertView.findViewById(R.id.endtime);
				holder.todetil = convertView.findViewById(R.id.todetil);
				holder.tuijian = convertView.findViewById(R.id.tuijian);
				holder.tuijiantitle = (TextView) convertView.findViewById(R.id.tuijiantitle);

				holder.prize = (ImageView) convertView.findViewById(R.id.prize);
				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			}


			String passtype = rzp.getPassTypeStr();
			if(!TextUtils.isEmpty(passtype)){
				if(passtype.contains(",")||passtype.contains("，")){
					String[] chuans = null;
					if(passtype.contains(",")){
						chuans = passtype.split(",");
					}
					if(passtype.contains("，")){
						chuans = passtype.split("，");
					}
					if(chuans.length>2){
						holder.chuanfa.setText(chuans[0]+","+chuans[1]+"...");
					}else{
						if(passtype.split(",").length == 1){
							holder.chuanfa.setText(chuans[0]);
						}else{
							holder.chuanfa.setText(chuans[0]+","+chuans[1]);
						}
					}
				}else{
					holder.chuanfa.setText(passtype);
				}
			}

			
			holder.endtime.setText(rzp.getCreateTime());
			String moneyStr = "<html><font color=\"#000000\">已跟投"
					+"</font><font color=\"#FF3232\">"+rzp.getCopiedSchemePrize()+"元"
					+ "</font></html>";
			holder.money.setText(Html.fromHtml(moneyStr));

			if(rzp.getItems().size()>0){
				RenZhengMatch rzm1 = rzp.getItems().get(0);
				holder.matchkey1.setText(rzm1.getMatchKey());
				holder.hostteamname1.setText(rzm1.getHomeTeamName());
				holder.gustteamname1.setText(rzm1.getGuestTeamName());

				setTouZhu(holder.touzhu1,rzm1);
				
				holder.match1.setVisibility(View.VISIBLE);
				holder.chuanfa.setVisibility(View.VISIBLE);
				holder.matchs.removeAllViews();
				if(rzp.getItems().size()>1){
					for(int i=1;i<rzp.getItems().size();i++){
						RenZhengMatch rzm2 = rzp.getItems().get(i);
						View otherMatch = View.inflate(_this, R.layout.match2_item,null);
						TextView matchkey2 = (TextView) otherMatch.findViewById(R.id.matchkey);
						matchkey2.setText(rzm2.getMatchKey());
						TextView hostteamname2 = (TextView) otherMatch.findViewById(R.id.hostteamname);
						hostteamname2.setText(rzm2.getHomeTeamName());
						TextView gustteamname2 = (TextView) otherMatch.findViewById(R.id.gustteamname);
						gustteamname2.setText(rzm2.getGuestTeamName());
						TextView touzhu = (TextView) otherMatch.findViewById(R.id.touzhu);
						setTouZhu(touzhu,rzm2);
						holder.matchs.addView(otherMatch);
					}
				}
			}else{
				holder.match1.setVisibility(View.GONE);
				holder.chuanfa.setVisibility(View.GONE);
			}

			if(TextUtils.isEmpty(rzp.getRecommendTitle())
					&&TextUtils.isEmpty(rzp.getRecommendContent())){
				holder.tuijian.setVisibility(View.GONE);
			}else{
				holder.tuijian.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(rzp.getRecommendTitle())){
					holder.tuijiantitle.setText(rzp.getRecommendTitle());
				}else{
					holder.tuijiantitle.setVisibility(View.GONE);
				}
			}
			switch(rzp.getSchemeStateInt()){
			case 0:
				holder.prize.setVisibility(View.INVISIBLE);
				break;
			case 1:
				holder.prize.setImageResource(R.mipmap.prize_yes);
				holder.prize.setVisibility(View.VISIBLE);
				break;
			case 2:
				holder.prize.setImageResource(R.mipmap.prize_no);
				holder.prize.setVisibility(View.VISIBLE);
				break;
			}
			holder.todetil.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(_this,MyRecommendDetilActivity.class);
					intent.putExtra("id", rzp.getSchemeBackupsId());
					_this.startActivity(intent);
				}
			});
			holder.detils.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent(_this,JCZQOrderDetailActivity.class);
					try {
						intent.putExtra("schemeId", Integer.parseInt(rzp.getSourceSchemeId()));
					} catch (Exception e) {
						e.printStackTrace();
						intent.putExtra("schemeId", -1);
					}
					intent.putExtra("onlyClose", true);
					intent.putExtra("lotteryId", ConstantsBase.JCZQ+"");
					startActivity(intent);
				}
			});
			return convertView;
		}
		public void setTouZhu(TextView touzhu,RenZhengMatch rzm) {  
			if(TextUtils.isEmpty(rzm.getBet())){
				touzhu.setText("保密");
			}else{
				if(rzm.getBet().contains("保密")){
					touzhu.setText("保密");
				}else if(rzm.getBet().contains("{")&&
						rzm.getBet().contains("}")){
					int has1 = rzm.getBet().length() - rzm.getBet().replace("{", "").length();
					int has2 = rzm.getBet().length() - rzm.getBet().replace("}", "").length();
					StringBuffer betSB = new StringBuffer();
					if(has1 == has2){
						String betStr = rzm.getBet();
						betSB.append("<html>");
						for(int i=0;i<has1;i++){
							int index1 = betStr.indexOf("{");
							int index2 = betStr.indexOf("}");
							betSB.append("<font color=\"#454545\">"+betStr.substring(0,index1));
							betSB.append("</font><font color=\"#FF3232\">"+betStr.substring(index1+1,index2)+"</font>");
							betStr = betStr.substring(index2+1, betStr.length());
						}
						betSB.append("<font color=\"#454545\">"+betStr+"</font></html>");
						touzhu.setText(Html.fromHtml(betSB.toString()));
					}else{
						touzhu.setText(rzm.getBet());
						touzhu.setTextColor(Color.parseColor("#454545"));
					}
				}else{
					touzhu.setText(rzm.getBet());
					touzhu.setTextColor(Color.parseColor("#454545"));
				}
			}
		}
		class ViewHolder {
			public TextView money;
			public LinearLayout matchs;
			public TextView detils;

			public View match1;
			public TextView matchkey1;
			public TextView hostteamname1,gustteamname1;
			public TextView touzhu1;

			public TextView chuanfa;
			public TextView endtime;
			public View todetil;
			public View  tuijian;
			public TextView  tuijiantitle;

			public ImageView prize;
		}

	}

}
