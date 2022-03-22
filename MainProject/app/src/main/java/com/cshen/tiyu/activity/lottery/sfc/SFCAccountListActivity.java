package com.cshen.tiyu.activity.lottery.sfc;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQUtil;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.pay.TZXYActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.domain.sfc.SFCMatch;
import com.cshen.tiyu.domain.sfc.SFCScroeList;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class SFCAccountListActivity  extends BaseActivity implements OnClickListener{
	public static final int HAVEPAY = 0;
	public static final int TODETAIL = 1;//详情选择
	public static final int LOGIN = 3;
	public SFCAccountListActivity _this;

	public TopViewLeft tv_head;//标题
	public ListView listview;//赛事列表
	private ListAdapter adapter;
	private View nodataview;

	private CheckBox provisions;//条款
	private boolean isTongyi = true;//条款同意与否
	/***********
	 ******胆相关
	 **************/
	public int danCount = 0,danMax = 0;
	/***********
	 ******倍数相关
	 **************/
	private View loadtime;//倍数时使用
	public ImageView addTime,minusTime;
	public TextView realTime;//倍数
	public int timeIntMAX = 99999, timeInt = 0;//总倍数
	private View timekeybroad,payview;//画的键盘和付款栏
	private TextView number10,number25,number50,number100,number500;
	/***********
	 ******金额相关
	 **************/
	public TextView allMoney,allTime,pay,tv_tzxy;//理论钱数，总注倍钱，付款，投注规则
	public int allNumInt = 0;//总注数
	public long allMoneyInt = 0;//总金额
	public boolean isToPay = false;
	/***********
	 ******收到的数据
	 **************/
	public ArrayList<SFCMatch> matchs;
	public int matchschoosed = 0;
	String playType;
	int minNum = 0;
	private AlertDialog  alertDialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sfcaccount_list);
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
				if(matchschoosed>0){
					dialog();
				}else{
					finish();
				}
			}
		});
		initView();
		initdata();
	}
	public void initView() {
		listview = (ListView) findViewById(R.id.dlt_listview);
		adapter = new ListAdapter();
		listview.setAdapter(adapter);

		allMoney =  (TextView) findViewById(R.id.allmoney);
		allTime =  (TextView) findViewById(R.id.alltime);
		realTime =  (TextView) findViewById(R.id.realtime);
		realTime.setOnClickListener(this);
		pay =  (TextView) findViewById(R.id.dlt_pay);
		pay.setOnClickListener(this);
		timekeybroad =  findViewById(R.id.dlt_pay_numbertime);
		payview =  findViewById(R.id.dlt_pay_view);
		findViewById(R.id.suretime).setOnClickListener(this);
		findViewById(R.id.cleartime).setOnClickListener(this);
		findViewById(R.id.number0).setOnClickListener(this);
		findViewById(R.id.number1).setOnClickListener(this);
		findViewById(R.id.number2).setOnClickListener(this);
		findViewById(R.id.number3).setOnClickListener(this);
		findViewById(R.id.number4).setOnClickListener(this);
		findViewById(R.id.number5).setOnClickListener(this);
		findViewById(R.id.number6).setOnClickListener(this);
		findViewById(R.id.number7).setOnClickListener(this);
		findViewById(R.id.number8).setOnClickListener(this);
		findViewById(R.id.number9).setOnClickListener(this);
		number10 = (TextView) findViewById(R.id.time10);
		number10.setOnClickListener(this);
		number25 = (TextView) findViewById(R.id.time25);
		number25.setOnClickListener(this);
		number50 = (TextView) findViewById(R.id.time50);
		number50.setOnClickListener(this);
		number100 = (TextView) findViewById(R.id.time100);
		number100.setOnClickListener(this);
		number500 = (TextView) findViewById(R.id.time500);
		number500.setOnClickListener(this);

		minusTime = (ImageView)findViewById(R.id.minustime);
		minusTime.setOnClickListener(this);
		addTime = (ImageView)findViewById(R.id.addtime);
		addTime.setOnClickListener(this);
		View footer =  findViewById(R.id.footer);//View.inflate(_this, R.layout.dltaccount_list_footer,null);
		//listview.addFooterView(footer);
		footer.findViewById(R.id.clear).setVisibility(View.GONE);;
		provisions =  (CheckBox)footer.findViewById(R.id.text_tongyi);
		provisions.setOnCheckedChangeListener(new OnCheckedChangeListener(){  
			@Override  
			public void onCheckedChanged(CompoundButton button, boolean isChecked){  
				if(isChecked){  
					isTongyi = true;
				} else{  
					isTongyi = false;
				}
			}
		}); 
		tv_tzxy = (TextView)footer.findViewById(R.id.tv_tzxy);
		tv_tzxy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(_this, TZXYActivity.class));
			}
		});

		nodataview =  findViewById(R.id.nodataview);

		loadtime = findViewById(R.id.load2);
		loadtime.setAlpha(0.7f);
		loadtime.setOnClickListener(this);	
	}
	public void initdata() {
		Bundle b = getIntent().getExtras();
		playType =  b.getString("playType");
		matchs  =  (ArrayList<SFCMatch>) b.getSerializable("matchs");
		matchschoosed  =  b.getInt("matchschoosed");
		if(TextUtils.isEmpty(playType)){
			playType = "1";
		}
		if( "0".equals(playType)){
			tv_head.setTitle("胜负彩");
			minNum = 14;
		}
		if("1".equals(playType)){
			tv_head.setTitle("任选九");
			minNum = 9;
			if(matchschoosed>minNum){
				danMax = 8;
			}
		}
		GetMatches();
		setTextViewNum();//更新注数
		setTextViewMoney();//更新投注金额
	}
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.realtime:
			timekeybroad.setVisibility(View.VISIBLE);
			loadtime.setVisibility(View.VISIBLE);
			payview.setVisibility(View.GONE);
			break;
		case R.id.number0:
			setTime("0");
			break;
		case R.id.number1:
			setTime("1");
			break;
		case R.id.number2:
			setTime("2");
			break;
		case R.id.number3:
			setTime("3");
			break;
		case R.id.number4:
			setTime("4");
			break;
		case R.id.number5:
			setTime("5");
			break;
		case R.id.number6:
			setTime("6");
			break;
		case R.id.number7:
			setTime("7");
			break;
		case R.id.number8:
			setTime("8");
			break;
		case R.id.number9:
			setTime("9");
			break;
		case R.id.time10:
			clearTime();
			number10.setTextColor(Color.parseColor("#FF3232"));
			number10.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("10");
			break;
		case R.id.time25:
			clearTime();
			number25.setTextColor(Color.parseColor("#FF3232"));
			number25.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("25");
			break;
		case R.id.time50:
			clearTime();
			number50.setTextColor(Color.parseColor("#FF3232"));
			number50.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("50");
			break;
		case R.id.time100:
			clearTime();
			number100.setTextColor(Color.parseColor("#FF3232"));
			number100.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("100");
			break;
		case R.id.time500:
			clearTime();
			number500.setTextColor(Color.parseColor("#FF3232"));
			number500.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("500");
			break;
		case R.id.cleartime:
			if(realTime.getText().toString().length()<=1){
				realTime.setText("");
			}else{
				String realtimeStr = realTime.getText().toString();
				realTime.setText(realtimeStr.substring(0, realtimeStr.length()-1));
			}
			break;
		case R.id.suretime:
			timekeybroad.setVisibility(View.GONE);
			loadtime.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			setTextViewMoney();
			break;
		case R.id.dlt_pay:
			if(matchschoosed==0){
				ToastUtils.showShort(_this, "您还未选择比赛");
				return;
			}
			if(matchschoosed<minNum){
				ToastUtils.showShort(_this, "请选择"+minNum+"场");
				return;
			}
			if(isTongyi){
				if (!PreferenceUtil.getBoolean(_this,"hasLogin") ) {
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					startActivityForResult(intentLogin,LOGIN);
				} else {
					toPayMoney();
				}
			}else{
				ToastUtils.showShort(_this, "必须同意后才能购买");
			}
			break;
		case R.id.minustime:
			timekeybroad.setVisibility(View.VISIBLE);
			loadtime.setVisibility(View.VISIBLE);
			payview.setVisibility(View.GONE);
			minusaddTime(0);
			break;
		case R.id.addtime:
			timekeybroad.setVisibility(View.VISIBLE);
			loadtime.setVisibility(View.VISIBLE);
			payview.setVisibility(View.GONE);
			minusaddTime(1);
			break;
		case R.id.load:
			setTextViewMoney();
			timekeybroad.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			break;
		case R.id.load2:
			setTextViewMoney();
			loadtime.setVisibility(View.GONE);
			timekeybroad.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			break;
		}
	}
	public void GetMatches(){
		if(matchs!=null){
			adapter.setDate(matchs);
			//adapter.notifyDataSetChanged();
			showNoDate(false);
		}else{
			showNoDate(true);
		}
	}

	public void showNoDate(boolean nodate){
		if(nodate){
			nodataview.setVisibility(View.VISIBLE);
		}else{
			nodataview.setVisibility(View.GONE);
		}
	}
	public void setTime(String i){
		clearTime();
		int number = 0 ;
		try{
			number = Integer.parseInt(realTime.getText().toString());
		}catch(Exception e){
			e.printStackTrace();
			number = 0 ;
		}
		if(number>timeIntMAX||realTime.getText().toString().length()==5){
			ToastUtils.showShort(_this, "倍数不能超过"+timeIntMAX);
			realTime.setText(timeIntMAX+"");
		}else{
			if(number==0){
				realTime.setText(i);
			}else{
				realTime.setText(number+i);
			}
		}
	}
	public void clearTime(){
		number10.setTextColor(Color.parseColor("#333333"));
		number10.setBackgroundResource(R.drawable.dlt_tzback);
		number25.setTextColor(Color.parseColor("#333333"));
		number25.setBackgroundResource(R.drawable.dlt_tzback);
		number50.setTextColor(Color.parseColor("#333333"));
		number50.setBackgroundResource(R.drawable.dlt_tzback);
		number100.setTextColor(Color.parseColor("#333333"));
		number100.setBackgroundResource(R.drawable.dlt_tzback);
		number500.setTextColor(Color.parseColor("#333333"));
		number500.setBackgroundResource(R.drawable.dlt_tzback);
	}
	public void minusaddTime(int type){
		String timeStr = realTime.getText().toString();
		try{
			timeInt = Integer.parseInt(timeStr);
			if(type == 0){//0minus
				if(timeInt < 1){
					ToastUtils.showShort(_this, "投注倍数不能为0");
					realTime.setText("1");
				}else{
					timeInt--;
					realTime.setText(timeInt+"");
				}
			}if(type == 1){//0add
				timeInt++;
				if(timeInt>timeIntMAX){
					ToastUtils.showShort(_this, "倍数不能超过"+timeIntMAX);
					realTime.setText(timeIntMAX+"");
				}else{
					realTime.setText(timeInt+"");
				}
			}
		}catch(Exception e){
			timeInt = 1;
			e.printStackTrace();
		}
		setTextViewMoney();
	}
	public void setTextViewNum(){
		if("0".equals(playType)){
			allNumInt = NumSFC();
		}
		if("1".equals(playType)){
			allNumInt = NumRXJ();
		}
	}
	public void setTextViewMoney(){//重新计算钱
		String timeStr = realTime.getText().toString();
		try{
			timeInt = Integer.parseInt(timeStr);
			if(timeInt == 0){
				ToastUtils.showShort(_this, "投注倍数不能为0");
				realTime.setText("1");
				timeInt=1;
				timeStr = "1";
			}
		}catch(Exception e){
			timeInt = 1;
			realTime.setText("1");
			timeStr = "1";
			e.printStackTrace();
		}
		allMoneyInt = (long)timeInt*allNumInt*2;
		String numberStr3 = "<html><font color=\"#909090\">"+
				(TextUtils.isEmpty(timeStr)?"1":timeStr)+"倍"+
				allNumInt+"注"
				+"</font><font color=\"#FF3232\">"+allMoneyInt
				+ "</font><font color=\"#909090\">元"
				+"</font></html>";
		allTime.setText(Html.fromHtml(numberStr3));
	}	

	class ListAdapter extends BaseAdapter {
		public ArrayList<SFCMatch> matchDate;
		public void setDate(ArrayList<SFCMatch> match){
			this.matchDate = match;
		}
		@Override
		public int getCount() {
			if(matchDate == null){
				return 0;
			}else{
				return matchDate.size();
			}
		}
		@Override
		public SFCMatch getItem(int position) {
			if(matchDate == null){
				return new SFCMatch();
			}else{
				return (SFCMatch)matchDate.get(position);
			}
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SFCMatch match = (SFCMatch) matchDate.get(position);
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
				convertView.setTag(holder); 
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			}   
			if( "0".equals(playType)){
				holder.dan.setVisibility(View.GONE);
			}else{
				holder.dan.setVisibility(View.VISIBLE);
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
				final boolean canClick;
				if(match.isDan()){//本来是胆
					if(danMax == 0||danCount>danMax){//但是可设胆数变成了0，就变灰
						canClick = false;
						holder.dan.setTextColor(Color.parseColor("#DCDCDCDC"));
						holder.dan.setBackgroundResource(R.mipmap.danbackchooseno);
						match.setDan(false);
						danCount = 0;
					}else{
						holder.dan.setTextColor(Color.parseColor("#ffffff"));
						holder.dan.setBackgroundResource(R.mipmap.danbackchoose);
						canClick = true;
					}
				}else{//本来就不是胆
					if(danCount>=danMax){//胆数已经设满，则不可点击
						canClick = false;
						holder.dan.setTextColor(Color.parseColor("#DCDCDCDC"));
					}else{
						if(sp.isCheck1()||sp.isCheck2()||sp.isCheck3()){//胆数没有设满，自己可点击成为胆
							canClick = true;
							holder.dan.setTextColor(Color.parseColor("#888888"));
						}else{
							canClick = false;
							holder.dan.setTextColor(Color.parseColor("#DCDCDCDC"));
						}
					}
					holder.dan.setBackgroundResource(R.mipmap.danbackchooseno);
				}
				holder.dan.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if(!canClick){
							if(danMax!=0&&danCount>=danMax){
								ToastUtils.showShort(_this, "已达到设胆上限");
							}
							return;
						}
						if(match.isDan()){
							match.setDan(false);
							holder.dan.setTextColor(Color.parseColor("#888888"));
							holder.dan.setBackgroundResource(R.mipmap.danbackchoosesno);
							danCount--;
							setTextViewNum();//更新注数
							setTextViewMoney();//更新投注金额
							notifyDataSetChanged();
						}else{
							if(danCount>=danMax){
								ToastUtils.showShort(_this, "已达到设胆上限");
							}else{
								match.setDan(true);
								holder.dan.setTextColor(Color.parseColor("#ffffff"));
								holder.dan.setBackgroundResource(R.mipmap.danbackchooses);
								danCount++;
								notifyDataSetChanged();
								setTextViewNum();//更新注数
								setTextViewMoney();//更新投注金额
							}
						}
					}
				});
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
			}else{
				if(sfc.isDan()){
					sfc.setDan(false);
				}	
			}
		}
		if(matchschoosed >= minNum){
			setTextViewNum();//更新注数
			if("1".equals(playType)&&(matchschoosed>minNum)){
				danMax = 8;
			}
		}else{
			allNumInt = 0;
			danMax = 0;
		}
		adapter.notifyDataSetChanged();
		setTextViewMoney();//更新投注金额
	}
	public void toPayMoney(){
		if(isToPay){
			return;
		}else{
			isToPay = true;
		}
		if(allNumInt == 0){
			ToastUtils.showShort(_this, "投注金额不能为0");
			return;
		}
		User user = MyDbUtils.getCurrentUser();
		ArrayList<Ticket> tickets = new ArrayList<>();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());	
		t.setwLotteryId(ConstantsBase.SFC+"");
		t.setMode("0");
		t.setPeriodNumber(PreferenceUtil.getString(_this,PreferenceUtil.PERIODSFC));
		t.setPlayType(playType); 
		t.setType("1");

		t.setMultiple(timeInt+"");
		t.setSumNum(allNumInt+"");
		t.setTotalAmount(allNumInt*2*timeInt+"");
		t.setHongBaoId("");
		t.setNumber(choosedScroeBean());
		tickets.add(t);
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",ConstantsBase.SFC);
		bundle.putString("playType",playType);
		bundle.putLong("totalaccount",allMoneyInt);
		bundle.putBoolean("useRedPacket",true);
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isToPay = false;
	}@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN:	
				toPayMoney();
				break;
			case HAVEPAY:
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				if(!isOnlyClose){
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					if (!closeMainActivity) {
						Intent intent= new Intent(_this,SFCMainActivity.class);
						intent.putExtra("playType", playType);
						startActivity(intent);
					}
				}
				_this.finish();
				break;
			default:
				break;
			}
		}
	}
	public int NumRXJ(){
		int totalCount = 0;
		ArrayList<Integer> danCheckd = new ArrayList<Integer>();
		ArrayList<Integer> allCheckd = new ArrayList<Integer>();
		int xuanzhongwei  = -1;
		for (int i = 0; i<matchs.size(); i++) {
			int number  = 0;
			if(matchs.get(i).getSp().isCheck1()){
				number ++;
			}
			if(matchs.get(i).getSp().isCheck2()){
				number ++;
			}
			if(matchs.get(i).getSp().isCheck3()){
				number ++;
			}
			if(number>0){
				allCheckd.add(number);
				xuanzhongwei++;
			}
			if(matchs.get(i).isDan()){
				if(xuanzhongwei>=0){
					danCheckd.add(xuanzhongwei);
				}
			}
		}
		ArrayList<ArrayList<Integer>> arrComb = 
				JCZQUtil.getJCZQUtil().combinForX(matchschoosed, 9, danCheckd);
		for (int j = 0; j<arrComb.size(); j++) {
			int tempCount = 1;
			for (int k = 0; k<arrComb.get(j).size(); k++) {
				if (arrComb.get(j).get(k) == 1) {
					tempCount = tempCount*allCheckd.get(k);
				}
			}
			totalCount = totalCount + tempCount;
		}
		return totalCount;
	}
	public int NumSFC(){
		int totalCount = 1;
		for (int i = 0; i<matchs.size(); i++) {
			int number  = 0;
			if(matchs.get(i).getSp().isCheck1()){
				number ++;
			}
			if(matchs.get(i).getSp().isCheck2()){
				number ++;
			}
			if(matchs.get(i).getSp().isCheck3()){
				number ++;
			}
			if(number == 0){
				number = 1;
			}
			totalCount = totalCount*number;
		}
		return totalCount;
	}
	public String choosedScroeBean(){
		StringBuffer number = new StringBuffer();
		if(danCount>0){
			number.append(danCount+";");
		}
		for (int i = 0; i<matchs.size(); i++) {
			SFCMatch sfc = matchs.get(i);
			StringBuffer numberEach = new StringBuffer();
			if(sfc.isDan()){
				numberEach.append("4");
			}
			if(matchs.get(i).getSp().isCheck1()){
				numberEach.append("3");
			}
			if(matchs.get(i).getSp().isCheck2()){
				numberEach.append("1");
			}
			if(matchs.get(i).getSp().isCheck3()){
				numberEach.append("0");
			}
			if(TextUtils.isEmpty(numberEach.toString())){
				numberEach.append("*");
			}
			number.append(numberEach);
			if(i < 13){
				number.append(",");
			}
		}
		return number.toString();
	}
	public void dialog() {
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("退出提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("退出将清空已选号码");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("确定");
		ImageView delete = (ImageView) window.findViewById(R.id.delete);
		delete.setVisibility(View.VISIBLE);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();	
			}
		});
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
				_this.finish();	
			}
		});
	}

}
