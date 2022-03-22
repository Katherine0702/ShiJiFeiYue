package com.cshen.tiyu.activity.lottery.sfc;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.sfc.SFCMatch;
import com.cshen.tiyu.domain.sfc.SFCMatchList;
import com.cshen.tiyu.domain.sfc.SFCScroeList;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;


public class SFCMainActivity extends BaseActivity implements OnClickListener{
	SFCMainActivity _this;	
	private TextView tv_head;//头
	private TextView qishutext,qishutime;
	private ImageView shuoming,back;
	private ListView listView;
	private MyListAdapter adapter;
	private ImageView loading;
	private View load,nodataview;
	private ImageView clear;//清除
	private TextView number;//场次
	private View sure;//确定
	private Timer timer = new Timer();  
	private int i = 0;  
	private boolean isShowing =false;
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
	String playType;
	boolean add = false;
	int minNum = 0;

	public ArrayList<SFCMatch> matchs;
	public int matchschoosed = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sfcmain);
		_this = this;
		tv_head=(TextView) findViewById(R.id.tv_head_title);
		initView();
		initdata();
		Loading();
	}
	public void initView(){
		/********************顶部***********************/
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(this);
		shuoming = (ImageView) findViewById(R.id.iv_setting);
		shuoming.setOnClickListener(this);
		qishutext = (TextView) findViewById(R.id.qishutext);
		qishutime = (TextView) findViewById(R.id.qishutime);
		listView = (ListView) findViewById(R.id.list);
		adapter = new MyListAdapter();
		listView.setAdapter(adapter);
		/********************底部***********************/
		clear =  (ImageView) findViewById(R.id.dlt_clear);
		clear.setOnClickListener(this);
		sure =  findViewById(R.id.dlt_sure);
		sure.setOnClickListener(this);
		number = (TextView) findViewById(R.id.jczq_number);
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		load.setOnClickListener(this);		
		loading = (ImageView) findViewById(R.id.loading);

		nodataview =  findViewById(R.id.nodataview);
		setNum();	
	}
	public void setNum(){
		String numberStr = "<html><font color=\"#666666\">已选"
				+"</font><font color=\"#FF3232\">"+matchschoosed
				+ "</font><font color=\"#666666\">场比赛</font></html>";
		number.setText(Html.fromHtml(numberStr));
	}
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:			
			_this.finish();
			break;
		case R.id.iv_setting:
			Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
			if("1".equals(playType)){
				intentHelp.putExtra("url","http://an.caiwa188.com/help/rx9c.html");
			}if("0".equals(playType)){
				intentHelp.putExtra("url","http://an.caiwa188.com/help/sfc.html");
			}
			startActivity(intentHelp);
			break;
		case R.id.dlt_sure:
			if(!isShowing){
				sure();
			}
			break;
		case R.id.dlt_clear:
			if(!isShowing){
				clear();
			}
			break;
		}
	}
	public void clear(){
		matchschoosed = 0;
		for(SFCMatch match:matchs){
			match.getSp().setCheck1(false);
			match.getSp().setCheck2(false);
			match.getSp().setCheck3(false);
		}
		adapter.notifyDataSetChanged();
		setNum();
	}
	public void sure(){
		if(matchschoosed == 0){
			ToastUtils.showShort(_this, "您还未选择比赛");
			return;
		}
		if(matchschoosed<minNum){
			ToastUtils.showShort(_this, "请选择"+minNum+"场");
			return;
		}
		Intent intent = new Intent(_this,SFCAccountListActivity.class);
		Bundle bundle = new Bundle();  
		bundle.putString("playType", playType);
		bundle.putSerializable("matchs", matchs);
		bundle.putInt("matchschoosed", matchschoosed);
		intent.putExtras(bundle);
		startActivity(intent);
		_this.finish();
	}
	public void initdata() {
		Bundle b = getIntent().getExtras();			
		add  = b.getBoolean("add");
		if(add){
			playType =  b.getString("playType");
			matchs  =  (ArrayList<SFCMatch>) b.getSerializable("matchs");
			matchschoosed  =  b.getInt("matchschoosed");
			GetMatches();
			setNum();
		}else{
			playType =  getIntent().getStringExtra("playType");		
			peridDate();
		}
		if( "0".equals(playType)){
			tv_head.setText("胜负彩");
			minNum = 14;
		}
		if("1".equals(playType)){
			tv_head.setText("任选九");
			minNum = 9;
		}
	}
	public void peridDate(){
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.SFC+"", new   CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				String periodTimeNew = "",periodTimeOld = "",periodTimeEnd = "";
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						periodTimeNew = period.getPeriodNumber();
						periodTimeEnd = period.getEndJoinTime_single();
					}
					periodTimeOld = PreferenceUtil.getString(_this, PreferenceUtil.PERIODSFC,"");
				}
				qishutext.setText("第"+periodTimeNew+"期");
				if(!TextUtils.isEmpty(periodTimeEnd) &&
						(periodTimeEnd.length() >= 8)){
					qishutime.setText(periodTimeEnd.substring(5,periodTimeEnd.length()-3)+"截止");
				}
				if(!periodTimeOld.equals(periodTimeNew)){
					PreferenceUtil.putString(_this,PreferenceUtil.PERIODSFC,periodTimeNew);
					getSFCDate(periodTimeNew);
				}else{
					matchs = (ArrayList<SFCMatch>) 
							ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSFC);
					GetMatches();
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public void getSFCDate(String number){
		ServiceCaiZhongInformation.getInstance().pastSFCMatches(_this,number,"0","1",
				ConstantsBase.SFC+"", new CallBack<SFCMatchList>() {
			@Override
			public void onSuccess(SFCMatchList	 t) {
				// TODO 自动生成的方法存根
				if(t.getResultList()!=null&& t.getResultList().size()>0){
					ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERSFC,t.getResultList());
					matchs = t.getResultList();
					GetMatches();
				}else{
					LoadingStop();
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				LoadingStop();
			}
		});
	}
	public void GetMatches(){
		if(matchs!=null){
			adapter.setDate(matchs);
			adapter.notifyDataSetChanged();
			showNoDate(false);
		}else{
			showNoDate(true);
		}
		LoadingStop();
	}

	public void showNoDate(boolean nodate){
		if(nodate){
			nodataview.setVisibility(View.VISIBLE);
		}else{
			nodataview.setVisibility(View.GONE);
		}
	}




	class MyListAdapter extends BaseAdapter {
		public ArrayList<SFCMatch> matchs;
		public void setDate(ArrayList<SFCMatch> match){
			this.matchs = match;
		}
		@Override
		public int getCount() {
			if(matchs == null){
				return 0;
			}else{
				return matchs.size();
			}
		}
		@Override
		public SFCMatch getItem(int position) {
			if(matchs == null){
				return new SFCMatch();
			}else{
				return (SFCMatch)matchs.get(position);
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SFCMatch match = (SFCMatch) matchs.get(position);
			final ViewHolder holder;
			if (convertView == null) {  
				holder = new ViewHolder();  
				convertView = View.inflate(_this, R.layout.sfc_item,null);
				holder.day = (TextView) convertView.findViewById(R.id.day);
				holder.dan = (TextView) convertView.findViewById(R.id.dan);
				holder.titlename = (TextView) convertView.findViewById(R.id.titlename);
				holder.titletime = (TextView) convertView.findViewById(R.id.titletime);
				holder.titleend = (TextView) convertView.findViewById(R.id.titleend);
				holder.titlezhu = (TextView) convertView.findViewById(R.id.titlezhu);
				holder.titleke = (TextView) convertView.findViewById(R.id.titleke);
				holder.zhusheng = (TextView) convertView.findViewById(R.id.zhusheng);
				holder.ping = (TextView) convertView.findViewById(R.id.ping);
				holder.kesheng = (TextView) convertView.findViewById(R.id.kesheng);
				holder.dan.setVisibility(View.GONE);
				convertView.setTag(holder);  
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			}  
			if(match!=null){
				holder.titlename.setText(match.getGameName());
				holder.titletime.setText(match.getMatchKey());
				holder.titleend.setText(match.getMatchTime().substring(5,match.getMatchTime().length()-3));
				holder.titlezhu.setText(match.getHomeTeamName());
				holder.titleke.setText(match.getGuestTeamName());
				final SFCScroeList sp = match.getSp();
				if(match.isOpen()&&sp!=null){
					if(sp.getOdds1()!=null){	
						setTextColor(holder.zhusheng,"胜",sp.getOdds1().getValue(),sp.isCheck1());
					}
					if(sp.getOdds2()!=null){
						setTextColor(holder.ping,"平",sp.getOdds2().getValue(),sp.isCheck2());
					}
					if(sp.getOdds3()!=null){	
						setTextColor(holder.kesheng,"负",sp.getOdds3().getValue(),sp.isCheck3());					
					}
					holder.zhusheng.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							sp.setCheck1(!sp.isCheck1());
							setMatchschoosed();
						}
					});
					holder.ping.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							sp.setCheck2(!sp.isCheck2());
							setMatchschoosed();
						}
					});
					holder.kesheng.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							sp.setCheck3(!sp.isCheck3());
							setMatchschoosed();
						}
					});
				}
			}
			convertView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
			return convertView;
		}
		class ViewHolder {
			public TextView titlename,titletime,titleend;
			public TextView day;

			public TextView titlezhu,titleke;
			public TextView zhusheng,ping,kesheng;
			public TextView dan;
		}
		public void setTextColor(TextView view,String name,String detail,boolean isCheck){
			String Str = "";
			if(isCheck){
				Str = "<html><font color=\"#ffffff\">"+name
						+"</font><font color=\"#ffffff\">&#160;&#160;&#160;"+detail
						+ "</font></html>";
				view.setBackgroundResource(R.drawable.cornerfullred);
			}else{
				Str = "<html><font color=\"#333333\">"+name
						+"</font><font color=\"#888888\">&#160;&#160;&#160;"+detail
						+ "</font></html>";
				view.setBackgroundResource(R.drawable.dlt_tzback);
			}
			view.setText(Html.fromHtml(Str));
		};	
	}
	public void setMatchschoosed(){
		matchschoosed = 0;
		for(SFCMatch sfc:matchs){
			if(sfc.getSp().isCheck1()
					||sfc.getSp().isCheck2()
					||sfc.getSp().isCheck3()){
				matchschoosed++;
			}
		}
		adapter.notifyDataSetChanged();
		setNum();
	}
	public void Loading(){
		if(!isShowing){
			isShowing = true;	
			load.setVisibility(View.VISIBLE);
			loading.setVisibility(View.VISIBLE);
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
	}
	public void LoadingStop(){
		isShowing = false;		
		loading.setVisibility(View.GONE);
		load.setVisibility(View.GONE);
	}
}
