package com.cshen.tiyu.activity.lottery.ball.basketball;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.find.LiveScoresActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.ball.BFZBDate;
import com.cshen.tiyu.domain.ball.BFZBDateList;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class BasketBallBFZBListActivity extends BaseActivity {
	private BasketBallBFZBListActivity _this;
	private TopViewLeft tv_head;
	private ListView listview;
	private MyListAdapter myListAdapter = new MyListAdapter();;
	private ArrayList<BFZBDate> bfzbdate = new ArrayList<>();//从缓存中来的
	private int schemeId;

	private ImageView loading;
	private View load;
	private Timer timer = new Timer();  
	private int i = 0;  
	private Handler handler = new Handler()  
	{  
		@Override  
		public void handleMessage(Message msg)  
		{  

			super.handleMessage(msg); 
			loading.setBackgroundResource(images[i % 19]);
		}  
	}; 
	public int images[] = new int[] {
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong1,R.mipmap.gundong2,
			R.mipmap.gundong3,R.mipmap.gundong4,R.mipmap.gundong5,
			R.mipmap.gundong6,R.mipmap.gundong7,R.mipmap.gundong8,
			R.mipmap.gundong9,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_bfzblist);
		_this = this;
		tv_head=(TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true,false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override   
			public void clickLoginView(View view) {		
			}
			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根
			}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		initView();
		initdata();
	}
	public void initView(){
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(_this,LiveScoresActivity.class);
				intent.putExtra("url", bfzbdate.get(position).getMatchDetailsURL());
				startActivity(intent);
			}
		});
		listview.setAdapter(myListAdapter);
		
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);	
		loading = (ImageView) findViewById(R.id.loading);
		
		schemeId = getIntent().getExtras().getInt("schemeId");
	}
	public void initdata(){
		// TODO 自动生成的方法存根
		Loading();
		ServiceCaiZhongInformation.getInstance().pastFootBallBFZB(this,schemeId+"",ConstantsBase.JCLQ+"",new CallBack<BFZBDateList>() {

			@Override
			public void onSuccess(BFZBDateList t) {
				// TODO 自动生成的方法存根 
				// 更新过跳转就要显示界面
				if(t!=null&&t.getResultList()!=null){
					bfzbdate = t.getResultList();
					myListAdapter.notifyDataSetChanged();
				}
				LoadingStop();	
			}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				LoadingStop();	
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(_this, errorMessage.msg);
				}
			}
		});
	}
	class MyListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return bfzbdate.size();
		}

		@Override
		public BFZBDate getItem(int position) {
			return bfzbdate.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final BFZBDate bfzb = bfzbdate.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.bfzb_basket_item,null);
				holder.dateview = convertView.findViewById(R.id.dateview);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.tip = (TextView) convertView.findViewById(R.id.tip);
				holder.matchkey = (TextView) convertView.findViewById(R.id.matchkey);
				holder.matchname = (TextView) convertView.findViewById(R.id.matchname);
				holder.time = (TextView) convertView.findViewById(R.id.time);

				holder.host_name1 = (TextView) convertView.findViewById(R.id.host_name1);
				holder.host_name = (TextView) convertView.findViewById(R.id.host_name);
				holder.guest_name1 = (TextView) convertView.findViewById(R.id.guest_name1);
				holder.guest_name = (TextView) convertView.findViewById(R.id.guest_name);
				holder.host_scroes = convertView.findViewById(R.id.host_scroes);
				holder.host_scroe = (TextView) convertView.findViewById(R.id.host_scroe);
				holder.host_scroe1 = (TextView) convertView.findViewById(R.id.host_scroe1);
				holder.host_scroe2 = (TextView) convertView.findViewById(R.id.host_scroe2);
				holder.host_scroe3 = (TextView) convertView.findViewById(R.id.host_scroe3);
				holder.host_scroe4 = (TextView) convertView.findViewById(R.id.host_scroe4); 
				holder.host_scroe5 = (TextView) convertView.findViewById(R.id.host_scroe5);

				holder.guest_scroes = convertView.findViewById(R.id.guest_scroes);
				holder.guest_scroe = (TextView) convertView.findViewById(R.id.guest_scroe);
				holder.guest_scroe1 = (TextView) convertView.findViewById(R.id.guest_scroe1);
				holder.guest_scroe2 = (TextView) convertView.findViewById(R.id.guest_scroe2);
				holder.guest_scroe3 = (TextView) convertView.findViewById(R.id.guest_scroe3);
				holder.guest_scroe4 = (TextView) convertView.findViewById(R.id.guest_scroe4);
				holder.guest_scroe5 = (TextView) convertView.findViewById(R.id.guest_scroe5);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(position!=0&&bfzb.getMatchYear().equals(bfzbdate.get(position-1).getMatchYear())){
				holder.dateview.setVisibility(View.GONE);
			}else{
				holder.dateview.setVisibility(View.VISIBLE);
				holder.date.setText(bfzb.getMatchYear());
			}
			if(!TextUtils.isEmpty(bfzb.getMatchCl())){
				try{
					holder.tip.setBackgroundColor(Color.parseColor(bfzb.getMatchCl()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			holder.matchkey.setText(bfzb.getMatchKeyScores());
			holder.matchname.setText(bfzb.getMatchLn());
			holder.time.setText(bfzb.getMatchMtime());
			if(TextUtils.isEmpty(bfzb.getMatchHpm())){
				holder.host_name1.setVisibility(View.INVISIBLE); 
			}else{
				holder.host_name1.setText(bfzb.getMatchHpm()); 
			}
			holder.host_name.setText(bfzb.getMatchHome()); 
			if(TextUtils.isEmpty(bfzb.getMatchApm())){
				holder.guest_name1.setVisibility(View.INVISIBLE); 
			}else{
				holder.guest_name1.setText(bfzb.getMatchApm()); 
			}
			holder.guest_name.setText(bfzb.getMatchAway()); 
			if("Y".equals(bfzb.getMatchFlg())){
				holder.host_scroe.setText(bfzb.getMatchHscore()); 
				holder.guest_scroe.setText(bfzb.getMatchAscore()); 
				holder.host_scroes.setVisibility(View.VISIBLE);
				holder.guest_scroes.setVisibility(View.VISIBLE);
				holder.host_scroe.setVisibility(View.VISIBLE);
				holder.guest_scroe.setVisibility(View.VISIBLE);
			}if("N".equals(bfzb.getMatchFlg())){
				holder.host_scroes.setVisibility(View.GONE);
				holder.guest_scroes.setVisibility(View.GONE);
				holder.host_scroe.setVisibility(View.GONE);
				holder.guest_scroe.setVisibility(View.GONE);
			}
			String asBf = bfzb.getMatchAs();
			String hsBf = bfzb.getMatchHs();
			if(!TextUtils.isEmpty(asBf)){
				if(asBf.contains(",")){
					String[] bfs = asBf.split(",");
					if(bfs.length>0){
						holder.guest_scroe1.setText(bfs[0]); 
						holder.guest_scroe1.setVisibility(View.VISIBLE);
					}
					if(bfs.length>1){
						holder.guest_scroe2.setText(bfs[1]); 
						holder.guest_scroe2.setVisibility(View.VISIBLE);
					}
					if(bfs.length>2){
						holder.guest_scroe3.setText(bfs[2]); 
						holder.guest_scroe3.setVisibility(View.VISIBLE);
					}
					if(bfs.length>3){
						holder.guest_scroe4.setText(bfs[3]); 
						holder.guest_scroe4.setVisibility(View.VISIBLE);
					}
					if(bfs.length>4){
						holder.guest_scroe5.setText(bfs[4]); 
						holder.guest_scroe5.setVisibility(View.VISIBLE);
					}
				}
			}
			if(!TextUtils.isEmpty(hsBf)){
				if(hsBf.contains(",")){
					String[] bfs = hsBf.split(",");
					if(bfs.length>0){
						holder.host_scroe1.setText(bfs[0]); 
						holder.host_scroe1.setVisibility(View.VISIBLE);
					}
					if(bfs.length>1){
						holder.host_scroe2.setText(bfs[1]); 
						holder.host_scroe2.setVisibility(View.VISIBLE);
					}
					if(bfs.length>2){
						holder.host_scroe3.setText(bfs[2]); 
						holder.host_scroe3.setVisibility(View.VISIBLE);
					}
					if(bfs.length>3){
						holder.host_scroe4.setText(bfs[3]); 
						holder.host_scroe4.setVisibility(View.VISIBLE);
					}
					if(bfs.length>4){
						holder.host_scroe5.setText(bfs[4]); 
						holder.host_scroe5.setVisibility(View.VISIBLE);
					}
				}
			}
			return convertView;
		}


		class ViewHolder {
			public View dateview;
			public TextView date;//日期
			public TextView tip;//标记
			public TextView matchkey,matchname;//比赛标记和名字
			public TextView time;//时间
			public TextView host_name1,host_name,guest_name1,guest_name;//球队名字
			public View guest_scroes,host_scroes;
			public TextView host_scroe,guest_scroe;
			public TextView host_scroe1,host_scroe2,host_scroe3,host_scroe4,host_scroe5,
			guest_scroe1,guest_scroe2,guest_scroe3,guest_scroe4,guest_scroe5;
		}
	}
	public void Loading(){
		load.setVisibility(View.VISIBLE);
		loading.setVisibility(View.VISIBLE);
		if(timer == null){
			timer = new Timer();
		}
		timer.scheduleAtFixedRate(new TimerTask()  
		{  
			@Override  
			public void run()  
			{  
				// TODO Auto-generated method stub  
				i++;  
				Message mesasge = new Message();  
				mesasge.what = i;  
				handler.sendMessage(mesasge);  
			}  
		}, 0, 100);  

	}
	public void LoadingStop(){
		load.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
		if(timer!=null){
			timer.cancel(); 
			timer = null;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LoadingStop();
		if(timer!=null){
			timer.cancel(); 
			timer = null;
		}
	}
}
