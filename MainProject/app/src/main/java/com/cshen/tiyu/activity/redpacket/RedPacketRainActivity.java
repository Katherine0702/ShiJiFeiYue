package com.cshen.tiyu.activity.redpacket;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.mian4.personcenter.redpacket.RedPacketActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.redpacket.RedPacketRainPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketRainTime;
import com.cshen.tiyu.net.https.ServiceRedPacket;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.MyCountTime2;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class RedPacketRainActivity  extends BaseActivity{
	private RedPacketRainActivity _this;
	private TopViewLeft tv_head;//头
	MyCountTime2 mMyCountTime;
	CountDownTimer timer;
	TextView hour,min,second;
	TextView[] times ;
	Random random;
	TextView title,qiang,money;
	ImageView close;
	boolean hourZero = false;
	boolean minZero = false;
	boolean secZero = false;
	boolean isQiang = false;
	boolean hasGet = false;
	boolean hasNone = false;
	View loadmain,load,zhong,have;
	TextView tobuy,todetail;
	private  Toast toast = null;
	private static int textview_id;  
	long timePrize = 600000l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redpacketrain);
		random=new Random();
		_this = this;
		initView();
		initdata(); 
	}
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.head);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		hour = (TextView) findViewById(R.id.hour);
		min = (TextView) findViewById(R.id.min);
		second = (TextView) findViewById(R.id.second);
		times = new TextView[]{hour,min,second};
		title = (TextView) findViewById(R.id.title);
		qiang = (TextView) findViewById(R.id.qiang);
		qiang.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (PreferenceUtil.getBoolean(_this,"hasLogin")) {
					if(isQiang){
						if(hasGet){
							showPrizeView(false,"");
							return;
						}
						if(hasNone){
							getToast("\n很遗憾\n\n本轮红包已抢光\n\n请期待下个整点\n");
							return;
						}
						int randomInt=random.nextInt(10)+1;
						if(randomInt<=3){
							getRedPacket();
						}else{
							getToast("\n亲，还有机会\n\n请继续戳\n");
						}
					}else{
						getToast("\n亲~还没到开抢时间\n\n请耐心等待\n");
					}
				}else{
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					intentLogin.putExtra("from", "redpacket");
					startActivityForResult(intentLogin,0);
				}
			}
		});

		tobuy = (TextView) findViewById(R.id.tobuy);
		tobuy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RedPacketRainActivity.this, FootballMainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		todetail = (TextView) findViewById(R.id.todetail);
		todetail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent redpacket = new Intent(_this, RedPacketActivity.class);
				startActivity(redpacket);
				finish();
			}
		});
		hour.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {
				if("00".equals(s.toString())){			
					hourZero = true;
				}				    
			}  
		});
		min.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {
				if("00".equals(s.toString())){			
					minZero = true;
				}				
			}  
		});
		second.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if("00".equals(s.toString())){			
					secZero = true;
				}if("01".equals(s.toString())){			
					secZero = false;
				}
			}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {
				if("00".equals(s.toString())&&!secZero){			
					secZero = true;
					if(!isQiang&&hourZero&&minZero&&secZero){
						clickRealTimeOver();
					}
				}				
			}  
		});
		close = (ImageView) findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qiang.setClickable(true);
				loadmain.setVisibility(View.GONE);
			}
		});
		loadmain = findViewById(R.id.loadmain);
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		zhong = findViewById(R.id.zhong);
		money = (TextView) findViewById(R.id.money);
		have = findViewById(R.id.have);
	}
	private void initdata() {
		//getTime();
	}
	@Override  
	public void onResume()  
	{  
		super.onResume();
		getTime();
	}
	@Override  
	public void onPause()  
	{  
		super.onPause();
		clearTime();
	}
	public void getTime(){
		ServiceRedPacket.getInstance().getRedPacketTime(_this,new CallBack<RedPacketRainTime>() {
			@Override
			public void onSuccess(RedPacketRainTime t) {
				// TODO 自动生成的方法存根       
				if(t.getIsGet() == 1){
					hasGet = true;
				}
				if(t.getMessage() ==3 ){
					hasNone = true;
				}
				Long time = t.getSubSecond();
				if(time<=0){
					for(int i=0;i<times.length;i++){
						((TextView) times[i]).setText("00");
					}
					title.setText("整点抢红包，正在进行中");
					isQiang = true;
					setDaoJi(time+timePrize);
				}else{
					openTime(time);
					title.setText("距离开抢时间还剩");
					isQiang = false;

					hourZero = false;
					minZero = false;
					secZero = false;
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}

	public void getRedPacket(){
		ServiceRedPacket.getInstance().getRedPacket(_this,new CallBack<RedPacketRainPacket>() {
			@Override
			public void onSuccess(RedPacketRainPacket t) {
				// TODO 自动生成的方法存根       
				if(t.getIsGet() == 1){
					showPrizeView(false,"");
					hasGet = true;
					return;
				}
				if(t.getMessage() ==3 ){
					getToast("\n很遗憾\n\n本轮红包已抢光\n\n请期待下个整点\n");
					hasNone = true;
					return ;
				}
				String money = t.getAmount();
				if(TextUtils.isEmpty(money)){
					getToast("\n亲，还有机会\n\n请继续戳\n");
				}else{
					showPrizeView(true,money);
					hasGet = true;
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public void clickRealTimeOver() {
		// TODO Auto-generated method stub
		title.setText("整点抢红包，正在进行中");
		isQiang = true;
		setDaoJi(timePrize);
	}
	public void showPrizeView(boolean isHave,String moneyStr){
		qiang.setClickable(false);
		loadmain.setVisibility(View.VISIBLE);
		if(isHave){
			zhong.setVisibility(View.VISIBLE);
			have.setVisibility(View.INVISIBLE);
			money.setText(moneyStr+"元");			
		}else{
			zhong.setVisibility(View.INVISIBLE);
			have.setVisibility(View.VISIBLE);
		}
	}
	public void setDaoJi(Long time) {
		timer = new CountDownTimer(time, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				//每隔countDownInterval秒会回调一次onTick()方法    
			}

			@Override
			public void onFinish() {
				hasNone = false;
				hasGet = false;
				getTime();
				timer.cancel();
			}
		};
		timer.start();// 开始计时
	}
	public void openTime(Long time) {
		if(time<=0l){
			time = 0l;
		}
		mMyCountTime = new MyCountTime2(_this, time,
				1000, times, new String[]{"00","00","00"});
		mMyCountTime.start();
	}

	public void clearTime(){
		if(mMyCountTime!=null){
			mMyCountTime.cancel();
			mMyCountTime = null;
		}
		times[0].setText("00");
		times[1].setText("00");
		times[2].setText("00");
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
	}
	public void getToast(String str){
		if(toast!=null){
			toast.cancel();
		}
		toast = Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT);
		if (textview_id == 0)  
			textview_id = _this.getResources().getSystem().getIdentifier("message", "id", "android");  
		((TextView) toast.getView().findViewById(textview_id)).setGravity(Gravity.CENTER);  
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show(); 

	}
}
