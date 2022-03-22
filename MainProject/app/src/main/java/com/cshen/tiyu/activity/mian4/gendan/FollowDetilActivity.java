 package com.cshen.tiyu.activity.mian4.gendan;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Back;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.domain.gendan.RenZhengPerson;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;


public class FollowDetilActivity  extends BaseActivity implements OnClickListener{
	public static final int HAVEPAY = 0;
	public static final int LOGIN = 3;
	FollowDetilActivity _this;
	View head_view,match1;
	TextView  guanzhu;
	ImageView head,v;
	TextView name,mingzhonglv,yinglilv,money,endtime;
	TextView matchkey1,hostteamname1,gustteamname1,touzhu1;
	LinearLayout matchs;
	TextView chuanfa;
	RenZhengPerson rzp;
	TextView tuijiantitle,tuijiancontent;
	View tuijian;
	boolean hasFouce = false;
	String userId,userPwd,eventType;
	/***********
	 ******倍数相关
	 **************/
	public TextView realTime,moneyfinal,tobuy;//倍数
	public int timeIntMAX = 99999, timeInt = 0;//总倍数
	long cost = 0l;
	private View timekeybroad;//画的键盘和付款栏
	private TextView number10,number25,number50,number100,number500;
	private boolean isFoucing= false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.gendan_detil);
		initView();
		initDate();

	}
	public void initView(){
		findViewById(R.id.iv_back).setOnClickListener(this);
		head_view = findViewById(R.id.head_view);
		guanzhu = (TextView) findViewById(R.id.guanzhu);
		guanzhu.setOnClickListener(this);
		head = (ImageView) findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		v = (ImageView) findViewById(R.id.v);
		mingzhonglv = (TextView) findViewById(R.id.mingzhonglv); 
		yinglilv = (TextView) findViewById(R.id.yinglilv); 
		money = (TextView) findViewById(R.id.money);
		endtime = (TextView) findViewById(R.id.endtime);

		matchs =  (LinearLayout) findViewById(R.id.matchs);
		matchkey1 = (TextView) findViewById(R.id.matchkey1);
		hostteamname1 = (TextView) findViewById(R.id.hostteamname1);
		gustteamname1 = (TextView) findViewById(R.id.gustteamname1);
		touzhu1 = (TextView) findViewById(R.id.touzhu1);
		chuanfa = (TextView) findViewById(R.id.chuanfa);

		tuijian = findViewById(R.id.tuijian);
		tuijiantitle = (TextView) findViewById(R.id.tuijiantitle);
		tuijiancontent = (TextView) findViewById(R.id.tuijiancontent);

		realTime =  (TextView) findViewById(R.id.realtime);
		realTime.setOnClickListener(this);
		moneyfinal  =  (TextView) findViewById(R.id.moneyfinal);
		tobuy  =  (TextView) findViewById(R.id.tobuy);
		tobuy.setOnClickListener(this);

		timekeybroad =  findViewById(R.id.dlt_pay_numbertime);
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
	}
	public void initDate(){
		rzp = (RenZhengPerson) getIntent().getExtras().getSerializable("match");
		if(rzp!=null){
			setDate();
		}
		getFouce();
	}
	public void getFouce(){
		if (PreferenceUtil.getBoolean(_this,"hasLogin")) {          
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		}else{
			userId = "";
			userPwd = "";
		}
		ServiceGenDan.getInstance().getFollowDetil(_this,rzp.getSponsorId(),userId,userPwd,
				new CallBack<NiuRen>() {
			@Override
			public void onSuccess(NiuRen niuren) {
				// TODO 自动生成的方法存根
				if(niuren.isConcern()){
					hasFouce = true;
				}else{
					hasFouce = false;
				}
				if(TextUtils.isEmpty(niuren.getHitRateWeek())
						||"0".equals(niuren.getHitRateWeek())){
					mingzhonglv.setText("100%");
				}else{
					mingzhonglv.setText(niuren.getHitRateWeek());
				}
				if(TextUtils.isEmpty(niuren.getEarningsRateWeek())
						||"0".equals(niuren.getEarningsRateWeek())){
					yinglilv.setText("100%");
				}else{
					yinglilv.setText(niuren.getEarningsRateWeek());
				}
				setFouceData();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public void setFouce(){
		if(hasFouce){
			eventType = "CANCEL_CONCERN";
		}else{
			eventType = "CONCERN";
		}
		userId = MyDbUtils.getCurrentUser().getUserId();
		userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		ServiceGenDan.getInstance().setFouce(_this,rzp.getSponsorId(),userId,userPwd,eventType,
				new CallBack<Back>() {
			@Override
			public void onSuccess(Back t) {
				// TODO 自动生成的方法存根
				if(hasFouce){
					hasFouce = false;
					ToastUtils.showShort(_this, "取消关注");
				}else{
					hasFouce = true;
					ToastUtils.showShort(_this, "关注成功");
				}
				setFouceData();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				isFoucing = false;
			}
		});

	}
	public void setDate(){
		xUtilsImageUtils.display(head,R.mipmap.defaultniu,
				rzp.getSponsorUserPic());
		name.setText(rzp.getSponsorName());
		String level = rzp.getSponsorUserLevelNew();
		if(TextUtils.isEmpty(level)){
			v.setVisibility(View.INVISIBLE);
		}else{
			if(RenZhengUtils.getRenZhengUtils().getV(level)!=0){
				v.setImageResource(RenZhengUtils.getRenZhengUtils().getV(level));
				v.setVisibility(View.VISIBLE);
			}else{
				v.setVisibility(View.INVISIBLE);
			}
		}
		money.setText(rzp.getSchemeCost());
		endtime.setText(rzp.getFirstMatchTime());
		if(TextUtils.isEmpty(rzp.getRecommendTitle())
				&&TextUtils.isEmpty(rzp.getRecommendContent())){
			findViewById(R.id.title4).setVisibility(View.GONE);
			tuijian.setVisibility(View.GONE);
		}else{
			tuijian.setVisibility(View.VISIBLE);
			findViewById(R.id.title4).setVisibility(View.VISIBLE);
			if(!TextUtils.isEmpty(rzp.getRecommendTitle())){
				tuijiantitle.setText(rzp.getRecommendTitle());
			}else{
				tuijiantitle.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(rzp.getRecommendContent())){
				tuijiancontent.setText(rzp.getRecommendContent());
			}else{
				tuijiancontent.setVisibility(View.GONE);
			}
		}
		if(rzp.getResultList().size()>0){
			RenZhengMatch rzm1 = rzp.getResultList().get(0);
			matchkey1.setText(rzm1.getMatchKey());
			hostteamname1.setText(rzm1.getHomeTeamName());
			gustteamname1.setText(rzm1.getGuestTeamName());
			setTouZhu(touzhu1,rzm1);


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
						chuanfa.setText(chuans[0]+","+chuans[1]+"...");
					}else{
						if(passtype.split(",").length == 1){
							chuanfa.setText(chuans[0]);
						}else{
							chuanfa.setText(chuans[0]+","+chuans[1]);
						}
					}
				}else{
					chuanfa.setText(passtype);
				}
			}
			chuanfa.setVisibility(View.VISIBLE);
			matchs.removeAllViews();
			if(rzp.getResultList().size()>1){
				for(int i=1;i<rzp.getResultList().size();i++){
					RenZhengMatch rzm2 = rzp.getResultList().get(i);
					View otherMatch = View.inflate(_this, R.layout.match2_item,null);
					TextView matchkey = (TextView) otherMatch.findViewById(R.id.matchkey);
					matchkey.setText(rzm2.getMatchKey());
					TextView hostteamname = (TextView) otherMatch.findViewById(R.id.hostteamname);
					hostteamname.setText(rzm2.getHomeTeamName());
					TextView gustteamname = (TextView) otherMatch.findViewById(R.id.gustteamname);
					gustteamname.setText(rzm2.getGuestTeamName());
					TextView touzhu = (TextView) otherMatch.findViewById(R.id.touzhu);
					setTouZhu(touzhu,rzm2);
					matchs.addView(otherMatch);
				}
			}
		}else{
			match1.setVisibility(View.GONE);
			chuanfa.setVisibility(View.GONE);
		}

		String moneyStr = money.getText().toString();
		try{
			cost = Long.parseLong(moneyStr);				
		}catch(Exception e){
			cost = 0l;
			e.printStackTrace();
		}
		moneyfinal.setText(10*(cost/rzp.getMultiple())+"");
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
	public void setFouceData(){
		int right = guanzhu.getPaddingRight();  
		int top = guanzhu.getPaddingTop();  
		int bottom = guanzhu.getPaddingBottom();  
		int left = guanzhu.getPaddingLeft();
		if(!hasFouce){ 
			guanzhu.setText("+ 关注");
			guanzhu.setTextColor(Color.parseColor("#FF3232"));
			guanzhu.setBackgroundResource(R.drawable.cornerfulllinefull_redline_nofull);
		}else{
			guanzhu.setText("已关注");
			guanzhu.setTextColor(Color.parseColor("#ffffff"));
			guanzhu.setBackgroundResource(R.drawable.cornerfullred);
		}
		guanzhu.setPadding(left,top,right,bottom);  
		if(userId.equals(rzp.getSponsorId())){
			guanzhu.setVisibility(View.GONE);
		}else{
			guanzhu.setVisibility(View.VISIBLE);
		}
		isFoucing = false;
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
		String timeStr = realTime.getText().toString();
		try{
			timeInt = Integer.parseInt(timeStr);		
			if(timeInt == 0){
				timeInt = 1;
			}
		}catch(Exception e){
			timeInt = 1;
			e.printStackTrace();
		}
		moneyfinal.setText(timeInt*(cost/rzp.getMultiple())+"");
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN:	
				getFouce();
				break;	
			case HAVEPAY:	
				_this.finish();
				break;	
			default:
				break;
			}
		}
	}
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			_this.finish();
			break;
		case R.id.guanzhu:
			if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
				Intent intentLogin = new Intent(_this, LoginActivity.class); 
				intentLogin.putExtra("requestName", "intentLogin");
				startActivityForResult(intentLogin,LOGIN);
			}else{
				if(!isFoucing){
					isFoucing = true;
					setFouce();
				}
			}
			break;
		case R.id.realtime:
			timekeybroad.setVisibility(View.VISIBLE);
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
			moneyfinal.setText(10*(cost/rzp.getMultiple())+"");
			break;
		case R.id.time25:
			clearTime();
			number25.setTextColor(Color.parseColor("#FF3232"));
			number25.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("25");
			moneyfinal.setText(25*(cost/rzp.getMultiple())+"");
			break;
		case R.id.time50:
			clearTime();
			number50.setTextColor(Color.parseColor("#FF3232"));
			number50.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("50");
			moneyfinal.setText(50*(cost/rzp.getMultiple())+"");
			break;
		case R.id.time100:
			clearTime();
			number100.setTextColor(Color.parseColor("#FF3232"));
			number100.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("100");
			moneyfinal.setText(100*(cost/rzp.getMultiple())+"");
			break;
		case R.id.time500:
			clearTime();
			number500.setTextColor(Color.parseColor("#FF3232"));
			number500.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("500");
			
			moneyfinal.setText(500*(cost/rzp.getMultiple())+"");
			break;
		case R.id.cleartime:
			if(realTime.getText().toString().length()<=1){
				realTime.setText("");
			}else{
				String realtimeStr = realTime.getText().toString();
				realTime.setText(realtimeStr.substring(0, realtimeStr.length()-1));
			}

			String timeStr2 = realTime.getText().toString();
			try{
				timeInt = Integer.parseInt(timeStr2);
				if(timeInt == 0){
					timeInt = 1;
				}
			}catch(Exception e){
				timeInt = 1;
				e.printStackTrace();
			}
			moneyfinal.setText(timeInt*(cost/rzp.getMultiple())+"");

			break;
		case R.id.suretime:
			timekeybroad.setVisibility(View.GONE);
			break;
		case R.id.tobuy:
			if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
				Intent intentLogin = new Intent(_this, LoginActivity.class); 
				intentLogin.putExtra("requestName", "intentLogin");
				startActivity(intentLogin);
			}else{
				String userId = MyDbUtils.getCurrentUser().getUserId();
				if( rzp.getSponsorId().equals(userId)){
					ToastUtils.showShort(_this, "不能跟自己的单子哦");
					return;
				}
				String timeStr = realTime.getText().toString();
				int timeInt = 0;
				try{
					timeInt = Integer.parseInt(timeStr);	
					if(timeInt == 0){
						timeInt = 1;
					}
				}catch(Exception e){
					timeInt = 1;
					e.printStackTrace();
				}
				String moneyStr = moneyfinal.getText().toString();
				long cost = 0l;
				try{
					cost = Long.parseLong(moneyStr);				
				}catch(Exception e){
					cost = 0l;
					e.printStackTrace();
				}

				Intent intentPay = new Intent(_this,PayActivity.class);
				Bundle bundle = new Bundle();   
				bundle.putInt("lotteryid",ConstantsBase.JCZQ);
				bundle.putLong("totalaccount",cost);
				bundle.putBoolean("useRedPacket",false);
				bundle.putString("SchemeBackupsId",rzp.getSchemeBackupsId());
				bundle.putInt("timeInt",timeInt);
				intentPay.putExtras(bundle);
				startActivityForResult(intentPay, HAVEPAY);
			}
			break;
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if(timekeybroad.getVisibility() == View.VISIBLE){
				timekeybroad.setVisibility(View.GONE);
				return true;
			}else{
				_this.finish();
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
