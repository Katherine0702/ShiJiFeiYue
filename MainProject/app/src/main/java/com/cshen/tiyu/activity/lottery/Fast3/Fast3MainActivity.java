package com.cshen.tiyu.activity.lottery.Fast3;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.NowPrize;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.domain.fast3.MissDate;
import com.cshen.tiyu.domain.fast3.MissDateList;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.StatusBarUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.MyCountTime2;


public class Fast3MainActivity  extends BaseActivity  implements OnClickListener,SensorEventListener{
	public static  int WANFAINT = ConstantsBase.ERBUTONGHAO;
	public static  int DANTUO = 0;//0是胆拖，1不是胆拖
	int NUMLEAST = 0;
	private Fast3MainActivity _this;
	private TextView tv_head_item,shuoming;//头
	private ImageView back;
	private View load,load2;//遮挡层
	private PopupWindow pop;//玩法弹出框
	private PopupWindow popJixuan;//机选玩法弹出框
	private View viewPop,viewPopJixuan;
	private int[] wanfas_tv = new int[]{
			R.id.hezhi,R.id.santonghao,R.id.santonghaotong,
			R.id.sanbutonghao,R.id.sanlianhao,R.id.ertonghao,R.id.ertonghaofu,
			R.id.erbutonghao,R.id.sanbutonghao_dt,R.id.erbutonghao_dt};
	private LinearLayout mainview;
	private ScrollView scrollview;
	private View viewmain;
	private TextView da,xiao,dan,shuang;
	private TextView statename,hezhiname,daxiaoname,danshuangname;
	//二不同号
	private int[] numbers;//和值 /三同号、三同号通选/三不同号、三不同号连选/二同号中的同号、二同号中的复选/二不同号//胆
	private int[] numbersText;//和值/三同号、三同号通选/三不同号、三不同号连选/二同号中的同号、二同号中的复选/二不同号//胆
	private int[] numbers_txText;//和值/三同号、三同号通选/三不同号、三不同号连选/二同号中的同号、二同号中的复选/二不同号//胆
	private int[] number1;//二同号中的不同号//脱
	private int[] number1Text;//二同号中的不同号//脱

	private View prizeMoney;
	private TextView prizeMoney_tx;
	private View sure;//确定
	private TextView money;
	private View clear;

	private View shake,jixuan;//点击摇一摇
	private SensorManager mSensorManager;//震动传感器
	private Vibrator vibrator ;//震动空间
	private boolean isMoving = false;//是否在移动
	private View shakeview;
	private ImageView numchoose1,numchoose2,numchoose3;
	private ImageView[] choosees;
	private int[] shakenumbers = new int[]{R.mipmap.fast3_1,R.mipmap.fast3_2,R.mipmap.fast3_3,
			R.mipmap.fast3_4,R.mipmap.fast3_5,R.mipmap.fast3_6};
	private ArrayList<AnimatorSet> animatorSetFirst = new ArrayList<AnimatorSet>();//摇中的动画分散开来
	private ArrayList<AnimatorSet> animatorSet = new ArrayList<AnimatorSet>();//摇中的动画分散开来
	private ArrayList<Integer> nums = new ArrayList<Integer>();//摇中的动画分散开来
	Random random=new Random();




	private AlertDialog  alertDialog,endAlertDialog;
	private NumberFast  numberfast;
	private String state ; 
	private int OLDWANFA,OLDDANTUO ; 
	private int position =-1;//大于-1就是从详情传来的
	private Timer timer,timer2; 
	private boolean idStop = false;
	private MyCountTime2 mMyCountTime,mMyCountTimeReal;
	private View prizeView,noprizeView;
	private View qishuview;
	private TextView qishunow,qishunext,time,realtime;
	private ImageView fast3updown;
	private ImageView prizenumber1,prizenumber2,prizenumber3;
	private int[] prizenumbers = new int[]{R.mipmap.small1,
			R.mipmap.small2,R.mipmap.small3,R.mipmap.small4,
			R.mipmap.small5,R.mipmap.small6};
	String	periodTime = "";
	private boolean qishuitemshow = false;
	private View qishuviewall;
	private ListView qishulistview;
	private MyListAdapter adapter ;
	MissDate missdate;//遗漏

	private ArrayList<NumberFast> choosedNumbers = new ArrayList<>();//从缓存中来的
	private boolean end = false;
	private Handler handler = new Handler() {  

		// 处理子线程给我们发送的消息。  
		@Override  
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				for(int i=0;i<choosees.length;i++){
					if(!end){
						choosees[i].setImageResource(shakenumbers[random.nextInt(6)]);
					}
				}

			}
		};  
	};  
	/** 点击时候Y的坐标*/
	private int downY = 0;
	/** 拖动时候Y的坐标*/
	private int moveY = 0;
	/** 拖动时候Y的方向距离*/
	private int scrollY = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fast3main);
		_this = this;
		Bundle b = getIntent().getExtras();
		try{
			WANFAINT = b.getInt("wanfa");
		}catch(Exception e){
			WANFAINT = PreferenceUtil.getInt(this, PreferenceUtil.Fast3NUMBERWANFAN,ConstantsBase.ERBUTONGHAO); 
			e.printStackTrace();
		}
		OLDWANFA = WANFAINT;
		try{
			DANTUO = b.getInt("dantuo");
		}catch(Exception e){
			DANTUO = PreferenceUtil.getInt(this, PreferenceUtil.Fast3NUMBERDANTUO,1); 
			e.printStackTrace();
		}
		OLDDANTUO = DANTUO;
		initView();
		initdata(b); 
		getHistroy();
		getYiLou();
	}

	@Override
	public void setStatusBar() {
		super.setStatusBar();
		if (Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtil.setColor(this, getResources().getColor(R.color.fast3Status));//状态栏
		}

	}

	public void initView(){
		/********************顶部***********************/
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(this);
		shuoming = (TextView) findViewById(R.id.iv_setting);
		shuoming.setOnClickListener(this);
		findViewById(R.id.tv_head).setOnClickListener(this);
		tv_head_item = (TextView) findViewById(R.id.tv_head_title_item);

		qishuview = findViewById(R.id.qishuview);
		qishuview.setOnClickListener(this);
		qishunow = (TextView) findViewById(R.id.qishunow);
		prizeView = findViewById(R.id.prizenumber);
		noprizeView = findViewById(R.id.noprizenumber);
		fast3updown = (ImageView) findViewById(R.id.fast3updown);
		prizenumber1 = (ImageView) findViewById(R.id.prizenumber1);
		prizenumber2 = (ImageView) findViewById(R.id.prizenumber2);
		prizenumber3 = (ImageView) findViewById(R.id.prizenumber3);
		qishunext = (TextView) findViewById(R.id.qishunext);
		time = (TextView) findViewById(R.id.timeend);
		time.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {  
				if("已截止".equals(s.toString())){
					clickTimeOver();
				}
			}  
		});  
		realtime  = (TextView) findViewById(R.id.realtimeend);
		realtime.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {  
				if("已截止".equals(s.toString())){
					clickRealTimeOver();
				}
			}  
		});  
		qishuviewall = findViewById(R.id.qishuviewall);
		qishulistview = (ListView) findViewById(R.id.qishulistview);
		adapter = new MyListAdapter();
		/********************历史期次***********************/
		statename = (TextView) findViewById(R.id.statename);
		hezhiname = (TextView) findViewById(R.id.hezhiname);
		daxiaoname = (TextView) findViewById(R.id.daxiaoname);
		danshuangname = (TextView) findViewById(R.id.danshuangname);
		/********************球框***********************/

		scrollview = (ScrollView) findViewById(R.id.scrollview);
		scrollview.setOnTouchListener(new OnTouchListener(){    

			@Override    
			public boolean onTouch(View v, MotionEvent event) {    
				// TODO Auto-generated method stub  

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downY = (int) event.getRawY();
					return true;
				case MotionEvent.ACTION_MOVE:
					moveY = (int) event.getRawY();
					scrollY = moveY - downY;//滑动距离
					if (scrollY < 0) {//从下往上滑
						if(qishuitemshow){//正开着，则关闭
							qishuitemshow = false;
							setToShowPeriod(false);
							fast3updown.setImageResource(R.mipmap.fast3down);

						}
					} else if (scrollY > 100){//从上往下滑
						if(!qishuitemshow){//没开着
							if(scrollview.getScrollY() == 0){//处于顶部，则展开
								qishuitemshow = true;
								setToShowPeriod(true);
								fast3updown.setImageResource(R.mipmap.fast3up);
								adapter.notifyDataSetChanged();
								if(WANFAINT == ConstantsBase.HEZHI){
									hezhiname.setVisibility(View.VISIBLE);
									daxiaoname.setVisibility(View.VISIBLE);
									danshuangname.setVisibility(View.VISIBLE);
									statename.setVisibility(View.GONE);
								}else{
									hezhiname.setVisibility(View.GONE);
									daxiaoname.setVisibility(View.GONE);
									danshuangname.setVisibility(View.GONE);
									statename.setVisibility(View.VISIBLE);
								}
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}		
				return false;    
			}    

		});
		mainview = (LinearLayout) findViewById(R.id.mainview);
		setView();
		/********************底部*******************/
		jixuan = findViewById(R.id.jixuan);
		jixuan.setOnClickListener(this);
		clear = findViewById(R.id.clear);
		clear.setOnClickListener(this);	
		money = (TextView) findViewById(R.id.money);	
		sure =  findViewById(R.id.sure);
		sure.setOnClickListener(this);
		prizeMoney = findViewById(R.id.prizemoney);
		prizeMoney_tx = (TextView) findViewById(R.id.prizemoney_tx);
		/********************选中球浮页***********************/
		shakeview = findViewById(R.id.shake);
		numchoose1 = (ImageView) findViewById(R.id.numchoose1);
		numchoose2 = (ImageView) findViewById(R.id.numchoose2);
		numchoose3 = (ImageView) findViewById(R.id.numchoose3);

		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		load.setOnClickListener(this);
		load2 = findViewById(R.id.load2);
		load2.setAlpha(0.7f);
		load2.setOnClickListener(this);
	}
	private void initdata(Bundle b) {
		try{
			position = b.getInt("position");
		}catch(Exception e){
			position = -1;
			e.printStackTrace();
		}		
		try{
			numberfast = (NumberFast)b.getSerializable("numberfast");
			if(numberfast!=null){
				setNumbersChoosed(numberfast);			
				setNumMoney(numberfast.getNum());
			}
			tv_head_item.setText(ChooseUtilFast3.choose(WANFAINT,DANTUO));
		}catch(Exception e){
			numberfast = null;
			e.printStackTrace();
		}
		if(numberfast == null){
			tv_head_item.setText(ChooseUtilFast3.choose(WANFAINT,DANTUO));
			setNumMoney(0);
		}
		try{ 
			state = (String)b.getString("state");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override  
	public void onResume()  
	{  
		super.onResume();  
		//获取传感器管理服务   
		vibrator = (Vibrator) this
				.getSystemService(Service.VIBRATOR_SERVICE);
		mSensorManager = (SensorManager) this  
				.getSystemService(Service.SENSOR_SERVICE);  
		//加速度传感器    
		mSensorManager.registerListener(this,  
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
				//还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，    
				//根据不同应用，需要的反应速率不同，具体根据实际情况设定    
				SensorManager.SENSOR_DELAY_NORMAL);  
		getNetDate();
	}  
	@Override  
	public void onPause()  
	{  
		clearTime();
		PreferenceUtil.putInt(this, PreferenceUtil.Fast3NUMBERWANFAN,WANFAINT);
		PreferenceUtil.putInt(this, PreferenceUtil.Fast3NUMBERDANTUO,DANTUO);
		mSensorManager.unregisterListener(this); 
		idStop = true;
		if(timer!=null){
			timer.cancel();
			timer=null;
		}if(timer2!=null){
			timer2.cancel();
			timer2=null;
		}
		super.onPause();  
	}  	  
	public void clickTimeOver() {
		// TODO Auto-generated method stub
		showStopDialog(periodTime);
	}
	public void clickRealTimeOver() {
		// TODO Auto-generated method stub
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		initDataForCathectic();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.load:
			if (pop!=null&&pop.isShowing()) {
				pop.dismiss();
				load.setVisibility(View.GONE);
			}
			break;
		case R.id.load2:
			if (popJixuan!=null&&popJixuan.isShowing()) {
				popJixuan.dismiss();
				load2.setVisibility(View.GONE);
			}
			break;
		case R.id.iv_back:
			_this.finish();
			break;
		case R.id.tv_head:
			if(!isMoving){
				if("change".equals(state)){
					ToastUtils.showShort(_this, "修改方案不能切换玩法");
					return;
				}
				if (pop == null) {
					popwindows();
				}
				if (pop.isShowing()) {
					pop.dismiss();
					load.setVisibility(View.GONE);
				} else {
					choose(WANFAINT,DANTUO);
					pop.showAsDropDown(v);
					load.setVisibility(View.VISIBLE);
					tv_head_item.setText(ChooseUtilFast3.choose(WANFAINT,DANTUO));
				}

			}
			break;
		case R.id.iv_setting:
			Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
			intentHelp.putExtra("url","http://an.caiwa188.com/help/k3.html");
			startActivity(intentHelp);
			break;
		case R.id.clear:
			if(!isMoving){
				clear();
				setNumMoney(0);
			}
			break;
		case R.id.dlt_shake:
			if(DANTUO == 0){
				ToastUtils.showShort(_this, "胆拖玩法暂不支持机选");
				return;	
			}
			if(WANFAINT  == ConstantsBase.SANTONGHAOTONG||
					WANFAINT  == ConstantsBase.SANLIANHAO){
				ToastUtils.showShort(_this, "通选玩法暂不支持机选");
				return;	
			}
			if(!isMoving){
				vibrator.vibrate(150);
				jiXuanChoose();
			}
			break;
		case R.id.jixuan:
			if(DANTUO == 0){
				ToastUtils.showShort(_this, "胆拖玩法暂不支持机选");
				return;}
			if(WANFAINT  == ConstantsBase.SANTONGHAOTONG||
					WANFAINT  == ConstantsBase.SANLIANHAO){
				ToastUtils.showShort(_this, "通选玩法暂不支持机选");
				return;	
			}
			if(!isMoving){
				if (popJixuan == null) {
					popJixuan();
				}
				if (popJixuan.isShowing()) {
					popJixuan.dismiss();
					load2.setVisibility(View.GONE);
				} else {
					int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
					int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
					viewPopJixuan.measure(intw, inth); 
					int height = viewPopJixuan.getMeasuredHeight();

					int[] location = new int[2];
					v.getLocationOnScreen(location);
					popJixuan.showAtLocation(v,
							Gravity.NO_GRAVITY, 
							location[0], 
							location[1]-height);
					load2.setVisibility(View.VISIBLE);  
				} 
			}
			break;
		case R.id.sure:
			if(!isMoving){
				if("已截止".equals(time.getText().toString())){
					ToastUtils.showShort(_this, "本期已截止");
				}else{
					sure();
				}
			}
			break;
		case R.id.qishuview:
			if(qishuitemshow){
				qishuitemshow = false;
				setToShowPeriod(false);
				fast3updown.setImageResource(R.mipmap.fast3down);
			}else{
				qishuitemshow = true;
				setToShowPeriod(true);
				fast3updown.setImageResource(R.mipmap.fast3up);
				adapter.notifyDataSetChanged();
				if(WANFAINT == ConstantsBase.HEZHI){
					hezhiname.setVisibility(View.VISIBLE);
					daxiaoname.setVisibility(View.VISIBLE);
					danshuangname.setVisibility(View.VISIBLE);
					statename.setVisibility(View.GONE);
				}else{
					hezhiname.setVisibility(View.GONE);
					daxiaoname.setVisibility(View.GONE);
					danshuangname.setVisibility(View.GONE);
					statename.setVisibility(View.VISIBLE);
				}
			}

			break;
		case R.id.da:
			hezhiOtherBack(0);
			break;
		case R.id.xiao:
			hezhiOtherBack(1);
			break;
		case R.id.dan:
			hezhiOtherBack(2);
			break;
		case R.id.shuang:
			hezhiOtherBack(3);
			break;
		default:
			if(!isMoving){
				chooseBall(v);
			}
			break;
		}
	}

	/******************************历史合开奖逻辑*****************************************/
	private void getNetDate() {
		initDataForCathectic();
	}
	private void setTimer(final int time){
		if(timer!=null){
			timer.scheduleAtFixedRate(new TimerTask()  
			{  
				@Override  
				public void run()  
				{  
					if(!idStop){
						initKaijiangDataForCathectic(); 
					}else{
						this.cancel();
					}
				}
			}, time*60*1000, 60*1000);  
		}
	}
	private void initDataForCathectic() {
		// TODO 自动生成的方法存根
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.Fast3+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						periodTime = period.getPeriodNumber();
						PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIODFAST3,periodTime);
						if(periodTime.length() >2){
							periodTime = periodTime.substring(periodTime .length()-2,periodTime.length());     					
						}
						Long time = period.getInterval();
						Long realtime = period.getRealInterval();
						openTime(periodTime,time,realtime);
					}
				}
				initKaijiangDataForCathectic();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public void openTime(String qishuStr,Long timeL,Long realtimeL) {
		qishunext.setText("距第"+qishuStr+"期截止:");
		if(timeL<=0){
			timeL = (long) (60*10);
			realtimeL = (long) (60*10)+10;
		}
		mMyCountTime = new MyCountTime2(_this, timeL*1000,
				1000, time, "已截止", "", null, false);
		mMyCountTime.start();

		mMyCountTimeReal = new MyCountTime2(_this, realtimeL*1000,
				1000, realtime, "已截止", "", null, false);
		mMyCountTimeReal.start();

	}
	public void clearTime(){
		if(mMyCountTime!=null){
			mMyCountTime.cancel();
			mMyCountTime = null;
		}if(mMyCountTimeReal!=null){
			mMyCountTimeReal.cancel(); 
			mMyCountTimeReal = null;
		}
		time.setText("");
		realtime.setText("");
	}
	private void initKaijiangDataForCathectic() {
		ServiceCaiZhongInformation.getInstance().pastNowPrize(_this,ConstantsBase.Fast3+"",new CallBack<NowPrize>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub	
				if(errorMessage!=null
						&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(_this,errorMessage.msg);					
				}
			}
			@Override
			public void onSuccess(NowPrize nowP) {
				// TODO Auto-generated method stub

				Prize t = nowP.getPreviousPeriod();
				if(t!=null){
					if(!t.getResult().contains("?")){
						if(timer!=null){
							timer.cancel();
							timer=null;
						}
						showNumber(t);
					}else{
						showNone(t);
						if(timer == null){
							timer = new Timer();
							setTimer(3);
						}
					}		
				}
			}
		});
	}
	public void showNumber(Prize prizeNumber){
		String number = prizeNumber.getPeriodNumber();
		number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length());
		qishunow.setText(number+"期开奖中...");
		prizenumber1.startAnimation(getAnimation());
		prizenumber2.startAnimation(getAnimation());
		prizenumber3.startAnimation(getAnimation());
		qishunow.setText(number+"期开奖号码:"+prizeNumber.getResult().replace(",", " "));
		String[] numbers = prizeNumber.getResult().split(",");
		if(numbers!=null&&numbers.length == 3){
			prizenumber1.setImageResource(prizenumbers[Integer.parseInt(numbers[0])-1]);
			prizenumber2.setImageResource(prizenumbers[Integer.parseInt(numbers[1])-1]);
			prizenumber3.setImageResource(prizenumbers[Integer.parseInt(numbers[2])-1]);
		} 

		noprizeView.setVisibility(View.INVISIBLE);
		prizeView.setVisibility(View.VISIBLE);
	}
	public void showNone(Prize prizeNumber){
		String number = prizeNumber.getPeriodNumber();
		number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length());
		if("00".equals(number)){
			number = "01";
		}
		qishunow.setText(number+"期开奖中...");
		prizeView.setVisibility(View.INVISIBLE);
		noprizeView.setVisibility(View.VISIBLE);
	}
	/******************************公用逻辑*****************************************/	
	public void clear(){
		numberfast = null;
		if(numbers_txText!=null){
			for(int i=0;i<numbers_txText.length;i++){
				((TextView) findViewById(numbers_txText[i])).setTextColor(Color.parseColor("#FFFFFF"));
			}
		}
		for(int i=0;i<numbers.length;i++){
			seColorBack(numbersText[i],numbers[i],"#FFFFFF",R.drawable.corner_lightgreen);
		}
		for(int i=0;i<number1.length;i++){
			seColorBack(number1Text[i],number1[i],"#FFFFFF",R.drawable.corner_lightgreen);
		}
		if(da!=null){
			da.setBackgroundResource(0);
			da.setTextColor(Color.parseColor("#ffffff"));
		}if(xiao!=null){
			xiao.setBackgroundResource(0);
			xiao.setTextColor(Color.parseColor("#ffffff"));
		}if(dan!=null){
			dan.setBackgroundResource(0);
			dan.setTextColor(Color.parseColor("#ffffff"));
		}if(shuang!=null){
			shuang.setBackgroundResource(0);
			shuang.setTextColor(Color.parseColor("#ffffff"));
		}
	}
	public void setView(){
		isMoving = false;
		numbers_txText = null;
		switch (WANFAINT) {
		case ConstantsBase.HEZHI:
			viewmain = View.inflate(_this, R.layout.fast3mainhezhi, null);
			da = (TextView) viewmain.findViewById(R.id.da);
			da.setOnClickListener(this);		
			xiao = (TextView) viewmain.findViewById(R.id.xiao);
			xiao.setOnClickListener(this);		
			dan = (TextView) viewmain.findViewById(R.id.dan);
			dan.setOnClickListener(this);		
			shuang = (TextView) viewmain.findViewById(R.id.shuang);
			shuang.setOnClickListener(this);		

			numbers =  new int[]{
					R.id.view4,R.id.view5,R.id.view6,
					R.id.view7,R.id.view8,R.id.view9,R.id.view10,
					R.id.view11,R.id.view12,R.id.view13,R.id.view14,
					R.id.view15,R.id.view16,R.id.view17};
			numbersText =  new int[]{
					R.id.num4,R.id.num5,R.id.num6,
					R.id.num7,R.id.num8,R.id.num9,R.id.num10,
					R.id.num11,R.id.num12,R.id.num13,R.id.num14,
					R.id.num15,R.id.num16,R.id.num17};
			numbers_txText =  new int[]{
					R.id.num4_tx,R.id.num5_tx,R.id.num6_tx,
					R.id.num7_tx,R.id.num8_tx,R.id.num9_tx,R.id.num10_tx,
					R.id.num11_tx,R.id.num12_tx,R.id.num13_tx,R.id.num14_tx,
					R.id.num15_tx,R.id.num16_tx,R.id.num17_tx};
			number1 = new int[]{};
			number1Text = new int[]{};
			shake = viewmain.findViewById(R.id.dlt_shake);
			shake.setOnClickListener(this);		

			break;
		case ConstantsBase.SANTONGHAO:
			viewmain = View.inflate(_this, R.layout.fast3main3t, null);
			numbers =  new int[]{
					R.id.view1,R.id.view2,R.id.view3,R.id.view4,
					R.id.view5,R.id.view6};
			numbersText =  new int[]{
					R.id.num1,R.id.num2,R.id.num3,R.id.num4,
					R.id.num5,R.id.num6};
			numbers_txText =  new int[]{
					R.id.num1_tx,R.id.num2_tx,R.id.num3_tx,R.id.num4_tx,
					R.id.num5_tx,R.id.num6_tx};
			number1 = new int[]{};
			number1Text = new int[]{};
			shake = viewmain.findViewById(R.id.dlt_shake);
			shake.setOnClickListener(this);		

			break;
		case ConstantsBase.SANTONGHAOTONG:
			viewmain = View.inflate(_this, R.layout.fast3main3tt, null);
			numbers =  new int[]{R.id.view0};
			numbersText =  new int[]{R.id.num0};
			numbers_txText =  new int[]{R.id.num0_tx};
			number1 = new int[]{};
			number1Text = new int[]{};
			break;
		case ConstantsBase.SANBUTONGHAO:
			if(DANTUO == 1){
				viewmain = View.inflate(_this, R.layout.fast3main3bu, null);
				number1 = new int[]{};
				number1Text = new int[]{};
				shake = viewmain.findViewById(R.id.dlt_shake);
				shake.setOnClickListener(this);		

			}else{
				viewmain = View.inflate(_this, R.layout.fast3maindan, null);
				((TextView)viewmain.findViewById(R.id.title)).setText("选3个不同号码，与开奖相同即中40元");
				number1 = new int[]{
						R.id.tuoview1,R.id.tuoview2,R.id.tuoview3,R.id.tuoview4,
						R.id.tuoview5,R.id.tuoview6};
				number1Text = new int[]{
						R.id.tuonum1,R.id.tuonum2,R.id.tuonum3,R.id.tuonum4,
						R.id.tuonum5,R.id.tuonum6};
			}
			numbers =  new int[]{
					R.id.view1,R.id.view2,R.id.view3,R.id.view4,
					R.id.view5,R.id.view6};
			numbersText =  new int[]{
					R.id.num1,R.id.num2,R.id.num3,R.id.num4,
					R.id.num5,R.id.num6,};
			break;
		case ConstantsBase.SANLIANHAO:
			viewmain = View.inflate(_this, R.layout.fast3main3lian, null);
			numbers =  new int[]{R.id.view0};
			numbersText =  new int[]{R.id.num0};
			number1 = new int[]{};
			number1Text = new int[]{};
			break;

		case ConstantsBase.ERTONGHAO:
			viewmain = View.inflate(_this, R.layout.fast3main2t, null);	
			numbers =  new int[]{
					R.id.view1,R.id.view2,R.id.view3,R.id.view4,
					R.id.view5,R.id.view6};
			numbersText =  new int[]{
					R.id.num1,R.id.num2,R.id.num3,R.id.num4,
					R.id.num5,R.id.num6};
			number1 = new int[]{
					R.id.tuoview1,R.id.tuoview2,R.id.tuoview3,R.id.tuoview4,
					R.id.tuoview5,R.id.tuoview6};
			number1Text = new int[]{
					R.id.tuonum1,R.id.tuonum2,R.id.tuonum3,R.id.tuonum4,
					R.id.tuonum5,R.id.tuonum6};
			shake = viewmain.findViewById(R.id.dlt_shake);
			shake.setOnClickListener(this);	
			break;
		case ConstantsBase.ERTONGHAOFU:
			viewmain = View.inflate(_this, R.layout.fast3main2tf, null);	
			numbers =  new int[]{
					R.id.view1,R.id.view2,R.id.view3,R.id.view4,
					R.id.view5,R.id.view6};
			numbersText =  new int[]{
					R.id.num1,R.id.num2,R.id.num3,R.id.num4,
					R.id.num5,R.id.num6};
			number1 = new int[]{};
			number1Text = new int[]{};
			shake = viewmain.findViewById(R.id.dlt_shake);
			shake.setOnClickListener(this);	
			break;
		case ConstantsBase.ERBUTONGHAO:
			if(DANTUO == 1){
				viewmain = View.inflate(_this, R.layout.fast3main2bu, null);
				number1 = new int[]{};
				number1Text = new int[]{};
				shake = viewmain.findViewById(R.id.dlt_shake);
				shake.setOnClickListener(this);	
			}else{
				viewmain = View.inflate(_this, R.layout.fast3maindan, null);
				((TextView)viewmain.findViewById(R.id.title)).setText("选2个不同号码，猜中开奖的任意2位即中8元");
				number1 = new int[]{
						R.id.tuoview1,R.id.tuoview2,R.id.tuoview3,R.id.tuoview4,
						R.id.tuoview5,R.id.tuoview6};
				number1Text = new int[]{
						R.id.tuonum1,R.id.tuonum2,R.id.tuonum3,R.id.tuonum4,
						R.id.tuonum5,R.id.tuonum6};
			}
			numbers =  new int[]{
					R.id.view1,R.id.view2,R.id.view3,R.id.view4,
					R.id.view5,R.id.view6};
			numbersText =  new int[]{
					R.id.num1,R.id.num2,R.id.num3,R.id.num4,
					R.id.num5,R.id.num6};
			break;
		}
		mainview.removeAllViews();
		mainview.addView(viewmain);
		for(int i=0;i<numbers.length;i++){
			viewmain.findViewById(numbers[i]).setOnClickListener(this);
		}
		for(int i=0;i<number1.length;i++){
			viewmain.findViewById(number1[i]).setOnClickListener(this);
		}
		if(missdate!=null){
			setYiLouValue(missdate);
		}
	}
	public void getYiLou(){
		ServiceCaiZhongInformation.getInstance().getYiLou(_this,ConstantsBase.Fast3+"",new CallBack<MissDateList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub	
				if(errorMessage!=null
						&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(_this,errorMessage.msg);					
				}
			}
			@Override
			public void onSuccess(MissDateList nowP) {
				// TODO Auto-generated method stub
				if(nowP!=null&&
						nowP.getMissData()!=null){
					missdate = nowP.getMissData();
					setYiLouValue(missdate);
				}
			}
		});
	}
	public void setYiLouValue(MissDate missdate){
		int[] numberst,number1t;
		switch (WANFAINT) {  
		case ConstantsBase.HEZHI:
			numberst =  new int[]{R.id.num4_notice,R.id.num5_notice,R.id.num6_notice,
					R.id.num7_notice,R.id.num8_notice,R.id.num9_notice,R.id.num10_notice,
					R.id.num11_notice,R.id.num12_notice,R.id.num13_notice,R.id.num14_notice,
					R.id.num15_notice,R.id.num16_notice,R.id.num17_notice};
			for(int weizhi=0;weizhi<numberst.length;weizhi++){
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"+missdate.getHz().get(weizhi+1));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getHz().get(weizhi+1));				
				}
			}
			break;
		case ConstantsBase.SANTONGHAO:
			numberst =  new int[]{
					R.id.num1_notice,R.id.num2_notice,R.id.num3_notice,
					R.id.num4_notice,R.id.num5_notice,R.id.num6_notice};
			for(int weizhi=0;weizhi<missdate.getSTHDx().size();weizhi++){
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"+missdate.getSTHDx().get(weizhi));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getSTHDx().get(weizhi));				
				}
			}
			break;
		case ConstantsBase.SANTONGHAOTONG:
			((TextView)findViewById(R.id.num0_notice)).setText("遗漏:"+missdate.getSTHTx().get(0));		
			break;
		case ConstantsBase.SANBUTONGHAO:
			numberst =  new int[]{
					R.id.num1_notice,R.id.num2_notice,R.id.num3_notice,
					R.id.num4_notice,R.id.num5_notice,R.id.num6_notice};
			for(int weizhi=0;weizhi<missdate.getSBEB().size();weizhi++){				
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"
							+missdate.getSBEB().get(weizhi));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getSBEB().get(weizhi));				
				}			
			}
			if(DANTUO == 0){
				number1t =  new int[]{
						R.id.tuonum1_notice,R.id.tuonum2_notice,R.id.tuonum3_notice,
						R.id.tuonum4_notice,R.id.tuonum5_notice,R.id.tuonum6_notice};			
				for(int weizhi=0;weizhi<missdate.getSBEB().size();weizhi++){				
					if(weizhi == 0){
						((TextView)findViewById(number1t[weizhi])).setText("遗漏:"
								+missdate.getSBEB().get(weizhi));
					}else{	
						((TextView)findViewById(number1t[weizhi])).setText(missdate.getSBEB().get(weizhi));				
					}			
				}
			}
			break;

		case ConstantsBase.SANLIANHAO:
			((TextView)findViewById(R.id.num0_notice)).setText("遗漏:"+missdate.getSTHL().get(0));		

			break;

		case ConstantsBase.ERTONGHAO:
			numberst =  new int[]{
					R.id.num1_notice,R.id.num2_notice,R.id.num3_notice,
					R.id.num4_notice,R.id.num5_notice,R.id.num6_notice};
			for(int weizhi=0;weizhi<missdate.getETHDFx().size();weizhi++){				
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"+missdate.getETHDFx().get(weizhi));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getETHDFx().get(weizhi));				
				}			
			}
			number1t =  new int[]{
					R.id.tuonum1_notice,R.id.tuonum2_notice,R.id.tuonum3_notice,
					R.id.tuonum4_notice,R.id.tuonum5_notice,R.id.tuonum6_notice};			
			for(int weizhi=0;weizhi<missdate.getETHBTH().size();weizhi++){			
				if(weizhi == 0){
					((TextView)findViewById(number1t[weizhi])).setText("遗漏:"+missdate.getETHBTH().get(weizhi));
				}else{	
					((TextView)findViewById(number1t[weizhi])).setText(missdate.getETHBTH().get(weizhi));				
				}			
			}
			break;
		case ConstantsBase.ERTONGHAOFU:
			numberst =  new int[]{
					R.id.num1_notice,R.id.num2_notice,R.id.num3_notice,
					R.id.num4_notice,R.id.num5_notice,R.id.num6_notice};
			for(int weizhi=0;weizhi<missdate.getETHDFx().size();weizhi++){		
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"+missdate.getETHDFx().get(weizhi));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getETHDFx().get(weizhi));				
				}			
			}
			break;
		case ConstantsBase.ERBUTONGHAO:
			numberst =  new int[]{
					R.id.num1_notice,R.id.num2_notice,R.id.num3_notice,
					R.id.num4_notice,R.id.num5_notice,R.id.num6_notice};
			for(int weizhi=0;weizhi<missdate.getSBEB().size();weizhi++){		
				if(weizhi == 0){
					((TextView)findViewById(numberst[weizhi])).setText("遗漏:"+missdate.getSBEB().get(weizhi));
				}else{	
					((TextView)findViewById(numberst[weizhi])).setText(missdate.getSBEB().get(weizhi));				
				}			
			}
			if(DANTUO == 0){
				number1t =  new int[]{
						R.id.tuonum1_notice,R.id.tuonum2_notice,R.id.tuonum3_notice,
						R.id.tuonum4_notice,R.id.tuonum5_notice,R.id.tuonum6_notice};			
				for(int weizhi=0;weizhi<missdate.getSBEB().size();weizhi++){		
					if(weizhi == 0){
						((TextView)findViewById(number1t[weizhi])).setText("遗漏:"+missdate.getSBEB().get(weizhi));
					}else{	
						((TextView)findViewById(number1t[weizhi])).setText(missdate.getSBEB().get(weizhi));				
					}			
				}
			}			
			break;
		}
		mainview.removeAllViews();
		mainview.addView(viewmain);
		for(int i=0;i<numbers.length;i++){
			viewmain.findViewById(numbers[i]).setOnClickListener(this);
		}
		for(int i=0;i<number1.length;i++){
			viewmain.findViewById(number1[i]).setOnClickListener(this);
		}
	}
	public void getHistroy(){
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,ConstantsBase.Fast3+"","0","10","",new CallBack<PrizeList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub		
				if(errorMessage!=null
						&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(_this,errorMessage.msg);					
				}
			}
			@Override
			public void onSuccess(PrizeList t) {
				// TODO Auto-generated method stub
				if(t!=null){
					ArrayList<Prize>	resultList = t.getResultList();
					if(resultList!=null&&resultList.size()>0){
						setDate(resultList);						
					}
				}

			}
		});
	}
	public void setDate(ArrayList<Prize>	resultList){
		qishulistview.setAdapter(adapter);
		adapter.setDate(resultList);
	}
	public void setNumbersChoosed(NumberFast number){
		if(WANFAINT == ConstantsBase.HEZHI){
			for(int i=0;i<number.getNumbers().size();i++){
				seColorBack(numbersText[number.getNumbers().get(i)-4],numbers_txText[number.getNumbers().get(i)-4],
						numbers[number.getNumbers().get(i)-4],"#FFBA00",R.drawable.corner_lightgreenchoosed);
			}
			otherHezhi();
		}else{
			if(WANFAINT == ConstantsBase.SANTONGHAO||
					WANFAINT == ConstantsBase.SANTONGHAOTONG){
				for(int i=0;i<number.getNumbers().size();i++){
					seColorBack(numbersText[number.getNumbers().get(i)-1],numbers_txText[number.getNumbers().get(i)-1],
							numbers[number.getNumbers().get(i)-1],"#FFBA00",R.drawable.corner_lightgreenchoosed);
				}
				for(int i=0;i<number.getNumber1().size();i++){
					seColorBack(number1Text[number.getNumber1().get(i)-1],
							number1[number.getNumber1().get(i)-1],"#FFBA00",R.drawable.corner_lightgreenchoosed);
				}	
			}else{
				for(int i=0;i<number.getNumbers().size();i++){
					seColorBack(numbersText[number.getNumbers().get(i)-1],numbers[number.getNumbers().get(i)-1],"#FFBA00",R.drawable.corner_lightgreenchoosed);
				}
				for(int i=0;i<number.getNumber1().size();i++){
					seColorBack(number1Text[number.getNumber1().get(i)-1],number1[number.getNumber1().get(i)-1],"#FFBA00",R.drawable.corner_lightgreenchoosed);
				}	
			}
		}

	}
	public void seColorBack(int id,int idView,String color,int back){
		((TextView) findViewById(id)).setTextColor(Color.parseColor(color));
		findViewById(idView).setBackgroundResource(back);
	}
	public void seColorBack(int id,int id2,int idView,String color,int back){
		((TextView) findViewById(id)).setTextColor(Color.parseColor(color));
		((TextView) findViewById(id2)).setTextColor(Color.parseColor(color));
		findViewById(idView).setBackgroundResource(back);
	}
	public void sure(){
		switch (WANFAINT) {
		case ConstantsBase.HEZHI:
		case ConstantsBase.SANTONGHAO:
		case ConstantsBase.ERTONGHAOFU:
			if(numberfast == null||
			numberfast.getNumbers()==null||
			numberfast.getNumbers().size() == 0){
				jiXuanChoose();
				return;
			}
			break;
		case ConstantsBase.SANBUTONGHAO:
			if(DANTUO == 1){
				if(numberfast == null||
						numberfast.getNumbers()==null||
						numberfast.getNumbers().size() == 0){
					jiXuanChoose();
					return;
				}else if(numberfast.getNumbers().size()>0
						&&numberfast.getNumbers().size()<3){
					ToastUtils.showShort(_this, "三不同号请至少选择三个号码");
					return;
				}
			}else{
				if(numberfast == null){
					ToastUtils.showShort(_this, "请选择号码");
					return;
				}else if(numberfast.getNumbers()==null||
						numberfast.getNumbers().size() == 0){
					ToastUtils.showShort(_this, "请至少选择1个胆码");
					return;
				}else if(numberfast.getNumber1()==null||
						numberfast.getNumber1().size() == 0){
					ToastUtils.showShort(_this, "请至少选择1个拖码");
					return;
				}else if((numberfast.getNumbers().size()+
						numberfast.getNumber1().size()) < 3){
					ToastUtils.showShort(_this, "请至少选择3个号码");
					return;
				}
			}

			break;
		case ConstantsBase.ERTONGHAO:
			if(numberfast == null||
			numberfast.getNumbers()==null||
			(numberfast.getNumbers().size()+
					numberfast.getNumber1().size() == 0)){
				jiXuanChoose();
				return;
			}else if(numberfast.getNumber1().size() > 0
					&& numberfast.getNumbers().size() == 0){
				ToastUtils.showShort(_this, "二同号请至少选择1个同号");
				return;
			}else if(numberfast.getNumbers().size() > 0
					&& numberfast.getNumber1().size() == 0){
				ToastUtils.showShort(_this, "二同号请至少选择1个不同号");
				return;
			}
			break;			
		case ConstantsBase.ERBUTONGHAO:
			if(DANTUO == 1){
				if(numberfast == null||
						numberfast.getNumbers()==null||
						numberfast.getNumbers().size() == 0){
					jiXuanChoose();
					return;
				}else if(numberfast.getNumbers().size()>0
						&&numberfast.getNumbers().size()<2){
					ToastUtils.showShort(_this, "二不同号请至少选择2个号码");
					return;
				}
			}else{
				if(numberfast == null){
					ToastUtils.showShort(_this, "请选择号码");
					return;
				}else if(numberfast.getNumbers()==null||
						numberfast.getNumbers().size() == 0){
					ToastUtils.showShort(_this, "请至少选择1个胆码");
					return;
				}else if(numberfast.getNumber1()==null||
						numberfast.getNumber1().size() == 0){
					ToastUtils.showShort(_this, "请至少选择1个拖码");
					return;
				}
			}
			break;
		case ConstantsBase.SANTONGHAOTONG:
		case ConstantsBase.SANLIANHAO:
			if(numberfast == null){
				numberfast = new NumberFast();
			}

			break;
		default:
			break;
		}

		numberfast.setPlayType(WANFAINT);
		numberfast.setMode(DANTUO);
		numberfast.setNum(ChooseUtilFast3.getChooseUtil().getTime(numberfast));
		numberfast = ChooseUtilFast3.getChooseUtil().getSortList(numberfast);

		if(TextUtils.isEmpty(state)){
			Intent intent = new Intent(_this, Fast3AccountListActivity.class);
			Bundle bundle = new Bundle();   
			bundle.putSerializable("numberfast", numberfast);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			if("add".equals(state)
					&&(OLDWANFA!=WANFAINT||//新增号码，修改方案要给提示
					OLDDANTUO!=DANTUO)){
				dialog() ;
				return;
			}
			Intent intent = new Intent();  
			Bundle bundle = new Bundle();   
			bundle.putInt("position", position);
			bundle.putSerializable("numberfast", numberfast);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent); 
		}
		this.finish();
	}
	public void chooseBall(View v){
		boolean isWanZheng = false;
		if(DANTUO == 1){ 
			int indexNumbers = -1; 
			int indexNumber1 = -1;
			for(int i = 0;i<numbers.length;i++){
				if(numbers[i]==v.getId()){
					indexNumbers = i;
					break;
				}
			}
			for(int j = 0;j<number1.length;j++){
				if(number1[j]==v.getId()){
					indexNumber1 = j;
					break;
				}
			}
			if(indexNumbers>=0){//排位从0开始，初始值从1开始
				if(!isNumbersHave(indexNumbers)){
					if(WANFAINT == ConstantsBase.HEZHI){
						numberfast.getNumbers().add(indexNumbers+4);
						otherHezhi();			
					}else{
						numberfast.getNumbers().add(indexNumbers+1);
					}
					if(WANFAINT == ConstantsBase.HEZHI||
							WANFAINT == ConstantsBase.SANTONGHAO||
							WANFAINT == ConstantsBase.SANTONGHAOTONG){
						seColorBack(numbersText[indexNumbers],numbers_txText[indexNumbers],numbers[indexNumbers],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					}else{
						seColorBack(numbersText[indexNumbers],numbers[indexNumbers],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					}
					if(WANFAINT == ConstantsBase.ERTONGHAO){
						isNumber1Have(indexNumbers);
					}
					if(WANFAINT == ConstantsBase.SANTONGHAOTONG){
						isWanZheng = true;
					}if(WANFAINT == ConstantsBase.SANLIANHAO){
						isWanZheng = true;
					}
				}
			}else if(indexNumber1>=0){
				if(!isNumber1Have(indexNumber1)){
					numberfast.getNumber1().add(indexNumber1+1);
					if(WANFAINT == ConstantsBase.HEZHI||
							WANFAINT == ConstantsBase.SANTONGHAO||
							WANFAINT == ConstantsBase.SANTONGHAOTONG){
						seColorBack(numbersText[indexNumbers],numbers_txText[indexNumbers],numbers[indexNumbers],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					}else{
						seColorBack(number1Text[indexNumber1],number1[indexNumber1],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					}
					if(WANFAINT == ConstantsBase.ERTONGHAO){
						isNumbersHave(indexNumber1);
					}
				}
			}
			switch (WANFAINT) {
			case ConstantsBase.HEZHI:
			case ConstantsBase.SANTONGHAO:
			case ConstantsBase.ERTONGHAOFU:
				if(numberfast.getNumbers().size()>0){
					isWanZheng = true;
				}
				break;
			case ConstantsBase.SANBUTONGHAO:
				if(numberfast.getNumbers().size()>2){
					isWanZheng = true;
				}
				break;
			case ConstantsBase.ERTONGHAO:
				if(numberfast.getNumbers().size()>0
						&&numberfast.getNumber1().size()>0){
					isWanZheng = true;
				}
				break;
			case ConstantsBase.ERBUTONGHAO:
				if(numberfast.getNumbers().size()>1){
					isWanZheng = true;
				}
				break;
			}
		}
		if(DANTUO == 0){ 

			int indexDan = -1; 
			int indexTuo = -1;
			for(int i = 0;i<numbers.length;i++){
				if(numbers[i]==v.getId()){
					indexDan = i;
					break;
				}
			}
			for(int j = 0;j<number1.length;j++){
				if(number1[j]==v.getId()){
					indexTuo = j;
					break;
				}
			}
			if(indexDan>=0){
				if(!isNumbersHave(indexDan)){
					switch (WANFAINT) {			
					case ConstantsBase.SANBUTONGHAO:
						if(numberfast.getNumbers().size() == 2 ){
							ToastUtils.showShort(_this, "三不同号最多只能选2个胆码");
							return ;
						}
						break;
					case ConstantsBase.ERBUTONGHAO:
						if(numberfast.getNumbers().size() == 1 ){
							ToastUtils.showShort(_this, "二不同号最多只能选1个胆码");
							return ;
						}
						break;
					}

					numberfast.getNumbers().add(indexDan+1);
					seColorBack(numbersText[indexDan],numbers[indexDan],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					isNumber1Have(indexDan);

				}
			}else if(indexTuo>=0){
				if(!isNumber1Have(indexTuo)){
					switch (WANFAINT) {			
					case ConstantsBase.SANBUTONGHAO:
						if(numberfast.getNumber1().size() == 5 ){
							ToastUtils.showShort(_this, "三不同号最多只能选5个拖码");
							return ;
						}
						break;
					case ConstantsBase.ERBUTONGHAO:
						if(numberfast.getNumber1().size() == 5 ){
							ToastUtils.showShort(_this, "二不同号最多只能选5个拖码");
							return ;
						}
						break;
					}

					numberfast.getNumber1().add(indexTuo+1);
					seColorBack(number1Text[indexTuo],number1[indexTuo],"#FFBA00",R.drawable.corner_lightgreenchoosed);
					isNumbersHave(indexTuo);
				}
			}
			switch (WANFAINT) {			
			case ConstantsBase.SANBUTONGHAO:
				if(numberfast.getNumbers().size()>0&&
						numberfast.getNumber1().size()>0&&
						(numberfast.getNumbers().size()+
								numberfast.getNumber1().size())>2){
					isWanZheng = true;
				}
				break;
			case ConstantsBase.ERBUTONGHAO:
				if(numberfast.getNumbers().size()>0&&
						numberfast.getNumber1().size()>0&&
						(numberfast.getNumbers().size()+
								numberfast.getNumber1().size())>1){
					isWanZheng = true;
				}
				break;
			}
		}

		if(!isWanZheng){
			if(numberfast!=null){
				numberfast.setNum(0);
			}
			setNumMoney(0);
		}else{
			numberfast.setPlayType(WANFAINT);
			numberfast.setMode(DANTUO);
			int time = ChooseUtilFast3.getChooseUtil().getTime(numberfast);
			numberfast.setNum(time);
			setNumMoney(time);
		}
	}
	public void setNumMoney(int time){
		if(time == 0){
			prizeMoney.setVisibility(View.GONE);
			money.setVisibility(View.GONE);
			if(numberfast!=null&&(numberfast.getNumber1().size()+numberfast.getNumbers().size())>0){
				jixuan.setVisibility(View.GONE);
				clear.setVisibility(View.VISIBLE);
			}else{
				jixuan.setVisibility(View.VISIBLE);
				clear.setVisibility(View.GONE);
			}
		}else{
			jixuan.setVisibility(View.GONE);
			clear.setVisibility(View.VISIBLE);

			money.setVisibility(View.VISIBLE);
			prizeMoney.setVisibility(View.VISIBLE);
			Spanned prizeMoneyStr = ChooseUtilFast3.getChooseUtil().maymoney(numberfast);
			prizeMoney_tx.setText(prizeMoneyStr);
			String moneyStr1 = "<html><font color=\"#ffffff\">"+time+"注"
					+"</font><font color=\"#fab12f\">"+time*2+""
					+ "</font><font color=\"#ffffff\">元</font></html>";
			money.setText(Html.fromHtml(moneyStr1));
		}
	}
	/*******************************popwindow逻辑************************************/
	public void clearView(){
		for(int i = 0;i<wanfas_tv.length;i++){
			viewPop.findViewById(wanfas_tv[i]).setBackgroundResource(R.drawable.corner_lightgreen);
		}
	}
	public void choose(View v){
		if(isMoving){
			return ;
		}

		clearView();
		switch (v.getId()) {
		case R.id.hezhi:
			WANFAINT = ConstantsBase.HEZHI;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.santonghao:
			WANFAINT = ConstantsBase.SANTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.santonghaotong:
			WANFAINT = ConstantsBase.SANTONGHAOTONG;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.sanbutonghao:
			WANFAINT = ConstantsBase.SANBUTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.sanlianhao:
			WANFAINT = ConstantsBase.SANLIANHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.ertonghao:
			WANFAINT = ConstantsBase.ERTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.ertonghaofu:
			WANFAINT = ConstantsBase.ERTONGHAOFU;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.erbutonghao:
			WANFAINT = ConstantsBase.ERBUTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			DANTUO = 1;
			break;
		case R.id.sanbutonghao_dt:
			WANFAINT = ConstantsBase.SANBUTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.drawable.corner_lightgreenchoosed);
			DANTUO = 0;
			break;
		case R.id.erbutonghao_dt:
			WANFAINT = ConstantsBase.ERBUTONGHAO;
			viewPop.findViewById(v.getId()).setBackgroundResource(R.drawable.corner_lightgreenchoosed);
			DANTUO = 0;
			break;
		}
		tv_head_item.setText(ChooseUtilFast3.choose(WANFAINT,DANTUO));
		setView();
		clear();
		setNumMoney(0);
		if (pop!=null&&pop.isShowing()) {
			pop.dismiss();
			load.setVisibility(View.GONE);
		}
		if (popJixuan!=null&&popJixuan.isShowing()) {
			popJixuan.dismiss();
			load2.setVisibility(View.GONE);
		}
		if(qishuitemshow){
			adapter.notifyDataSetChanged();
			if(WANFAINT == ConstantsBase.HEZHI){
				hezhiname.setVisibility(View.VISIBLE);
				daxiaoname.setVisibility(View.VISIBLE);
				danshuangname.setVisibility(View.VISIBLE);
				statename.setVisibility(View.GONE);
			}else{
				hezhiname.setVisibility(View.GONE);
				daxiaoname.setVisibility(View.GONE);
				danshuangname.setVisibility(View.GONE);
				statename.setVisibility(View.VISIBLE);
			}
		}

	}
	public void  choose(int WANFAINT,int DANTUO){
		clearView();
		switch (WANFAINT) {
		case ConstantsBase.HEZHI:
			viewPop.findViewById(R.id.hezhi).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.SANTONGHAO:
			viewPop.findViewById(R.id.santonghao).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.SANTONGHAOTONG:
			viewPop.findViewById(R.id.santonghaotong).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.SANBUTONGHAO:
			if(DANTUO == 1){
				viewPop.findViewById(R.id.sanbutonghao).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			}else{
				viewPop.findViewById(R.id.sanbutonghao_dt).setBackgroundResource(R.drawable.corner_lightgreenchoosed);

			}
			break;
		case ConstantsBase.SANLIANHAO:
			viewPop.findViewById(R.id.sanlianhao).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.ERTONGHAO:
			viewPop.findViewById(R.id.ertonghao).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.ERTONGHAOFU:
			viewPop.findViewById(R.id.ertonghaofu).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			break;
		case ConstantsBase.ERBUTONGHAO:
			if(DANTUO == 1){
				viewPop.findViewById(R.id.erbutonghao).setBackgroundResource(R.mipmap.corner_lightgreenchoosed2);
			}else{
				viewPop.findViewById(R.id.erbutonghao_dt).setBackgroundResource(R.drawable.corner_lightgreenchoosed);
			}
			break;
		};
	}
	public void popwindows() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		viewPop = inflater.inflate(R.layout.mainfast3chooseview, null);
		// 创建PopupWindow对象
		pop = new PopupWindow(viewPop, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);

		pop.setOnDismissListener(new poponDismissListener());
	}	
	public void popJixuan() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		viewPopJixuan = inflater.inflate(R.layout.mainfast3jixuanview, null);
		// 创建PopupWindow对象
		popJixuan = new PopupWindow(viewPopJixuan, 300,
				LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		popJixuan.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		popJixuan.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		popJixuan.setFocusable(true);
		popJixuan.setOnDismissListener(new poponDismissListener());
	}	
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			load.setVisibility(View.GONE);
			load2.setVisibility(View.GONE);
		}
	}
	public void jixuan(View v){
		if(isMoving){
			return ;
		}
		if("已截止".equals(time.getText().toString())){
			ToastUtils.showShort(_this, "本期已截止");
			return ;
		}
		clear();
		switch (v.getId()) {
		case R.id.jixuan1:
			for(int i=0;i<1;i++){
				NumberFast NumberFast =  
						ChooseUtilFast3.getChooseUtil().getRandom(WANFAINT);
				NumberFast.setMode(DANTUO);
				choosedNumbers.add(0,NumberFast);
			}
			break;
		case R.id.jixuan5:
			for(int i=0;i<5;i++){
				NumberFast NumberFast =  
						ChooseUtilFast3.getChooseUtil().getRandom(WANFAINT);
				NumberFast.setMode(DANTUO);
				choosedNumbers.add(0,NumberFast);
			}
			break;
		case R.id.jixuan10:
			for(int i=0;i<10;i++){
				NumberFast NumberFast =  
						ChooseUtilFast3.getChooseUtil().getRandom(WANFAINT);
				NumberFast.setMode(DANTUO);
				choosedNumbers.add(0,NumberFast);
			}
			break;
		}
		if(TextUtils.isEmpty(state)){
			Intent intent = new Intent(_this, Fast3AccountListActivity.class);
			Bundle bundle = new Bundle();   
			bundle.putSerializable("numberfastList", choosedNumbers);
			intent.putExtras(bundle);
			startActivity(intent);
			_this.finish();
		}else{
			if("add".equals(state)//新增号码，修改方案要给提示
					&&(OLDWANFA!=WANFAINT||
					OLDDANTUO!=DANTUO)){
				dialog() ;
				return;
			}
			Intent intent = new Intent();  
			Bundle bundle = new Bundle();   
			bundle.putInt("position", position);
			bundle.putSerializable("numberfastList", choosedNumbers);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent); 
			_this.finish();
		}
	}

	/*****************************万千百逻辑******************************/
	public boolean isNumbersHave(int num){
		if(numberfast == null){
			numberfast = new NumberFast();
		}
		int bican = 1;
		if(WANFAINT == ConstantsBase.HEZHI){
			bican = 4;
		}
		if(numberfast.getNumbers()!=null&&numberfast.getNumbers().contains(num+bican)){

			numberfast.getNumbers().remove((Object)(num+bican));

			if(WANFAINT == ConstantsBase.HEZHI||
					WANFAINT == ConstantsBase.SANTONGHAO||
					WANFAINT == ConstantsBase.SANTONGHAOTONG){
				seColorBack(numbersText[num],numbers_txText[num],numbers[num],"#ffffff",R.drawable.corner_lightgreen);
				if(WANFAINT == ConstantsBase.HEZHI){
					otherHezhi();					
				}
			}else{
				seColorBack(numbersText[num],numbers[num],"#ffffff",R.drawable.corner_lightgreen);
			}
			return true;
		}
		return false;
	}
	public boolean isNumber1Have(int num){
		if(numberfast == null){
			numberfast = new NumberFast();
		}
		if(numberfast.getNumber1()!=null&&numberfast.getNumber1().contains(num+1)){
			numberfast.getNumber1().remove((Object)(num+1));
			seColorBack(number1Text[num],number1[num],"#ffffff",R.drawable.corner_lightgreen);
			return true;
		}
		return false;
	}
	public void jiXuanChoose(){
		clear();
		numberfast =  ChooseUtilFast3.getChooseUtil().getRandom(WANFAINT);
		//setNumbersChoosed(numberfast);
		setNumMoney(numberfast.getNum());
		setToAreaView();
	}
	public void hezhiOtherBack(int choose){//choose 0大 1小   0单 1双
		if(numbers_txText!=null){
			for(int i=0;i<numbers_txText.length;i++){
				((TextView) findViewById(numbers_txText[i])).setTextColor(Color.parseColor("#FFFFFF"));
			}
		}
		for(int i=0;i<numbers.length;i++){
			seColorBack(numbersText[i],numbers[i],"#FFFFFF",R.drawable.corner_lightgreen);
		}
		for(int i=0;i<number1.length;i++){
			seColorBack(number1Text[i],number1[i],"#FFFFFF",R.drawable.corner_lightgreen);
		}
		if(numberfast == null){
			numberfast = new NumberFast();
		}else{
			numberfast.getNumbers().clear();
		}
		if(choose == 0){
			if(numberfast.getDa() == 1){
				numberfast.setDa(0);
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));
			}else{
				numberfast.setDa(1);
				da.setBackgroundColor(Color.parseColor("#fab12f"));
				da.setTextColor(Color.parseColor("#000000"));
				if(numberfast.getXiao() == 1){
					numberfast.setXiao(0);
					xiao.setBackgroundResource(0);
					xiao.setTextColor(Color.parseColor("#ffffff"));
				}
			}
		}if(choose == 1){
			if(numberfast.getXiao() == 1){
				numberfast.setXiao(0);
				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));
			}else{
				numberfast.setXiao(1);
				xiao.setBackgroundColor(Color.parseColor("#fab12f"));
				xiao.setTextColor(Color.parseColor("#000000"));
				if(numberfast.getDa() == 1){
					numberfast.setDa(0);
					da.setBackgroundResource(0);
					da.setTextColor(Color.parseColor("#ffffff"));
				}
			}
		}if(choose == 2){
			if(numberfast.getDan() == 1){
				numberfast.setDan(0);
				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));
			}else{
				numberfast.setDan(1);
				dan.setBackgroundColor(Color.parseColor("#fab12f"));
				dan.setTextColor(Color.parseColor("#000000"));
				if(numberfast.getShuang() == 1){
					numberfast.setShuang(0);
					shuang.setBackgroundResource(0);
					shuang.setTextColor(Color.parseColor("#ffffff"));
				}
			}
		}if(choose == 3){
			if(numberfast.getShuang() == 1){
				numberfast.setShuang(0);
				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}else{
				numberfast.setShuang(1);
				shuang.setBackgroundColor(Color.parseColor("#fab12f"));
				shuang.setTextColor(Color.parseColor("#000000"));
				if(numberfast.getDan() == 1){
					numberfast.setDan(0);
					dan.setBackgroundResource(0);
					dan.setTextColor(Color.parseColor("#ffffff"));
				}
			}
		}

		hezhiOther();
	}
	public void hezhiOther(){//choose 0大 1小   0单 1双
		if(numberfast.getDa() == 1 //只选大
				&& numberfast.getDan() == 0 && numberfast.getShuang() == 0){
			if(!numberfast.getNumbers().contains(11)){
				numberfast.getNumbers().add(11);
			}if(!numberfast.getNumbers().contains(13)){
				numberfast.getNumbers().add(13);
			}if(!numberfast.getNumbers().contains(15)){
				numberfast.getNumbers().add(15);
			}if(!numberfast.getNumbers().contains(17)){
				numberfast.getNumbers().add(17);
			}if(!numberfast.getNumbers().contains(12)){
				numberfast.getNumbers().add(12);
			}if(!numberfast.getNumbers().contains(14)){
				numberfast.getNumbers().add(14);
			}if(!numberfast.getNumbers().contains(16)){
				numberfast.getNumbers().add(16);
			}
		}
		if(numberfast.getDa() == 1 //只选大，单
				&& numberfast.getDan() == 1 && numberfast.getShuang() == 0){
			if(!numberfast.getNumbers().contains(11)){
				numberfast.getNumbers().add(11);
			}if(!numberfast.getNumbers().contains(13)){
				numberfast.getNumbers().add(13);
			}if(!numberfast.getNumbers().contains(15)){
				numberfast.getNumbers().add(15);
			}if(!numberfast.getNumbers().contains(17)){
				numberfast.getNumbers().add(17);
			}
		}
		if(numberfast.getDa() == 1 //只选大，双
				&& numberfast.getDan() == 0 && numberfast.getShuang() == 1){
			if(!numberfast.getNumbers().contains(12)){
				numberfast.getNumbers().add(12);
			}if(!numberfast.getNumbers().contains(14)){
				numberfast.getNumbers().add(14);
			}if(!numberfast.getNumbers().contains(16)){
				numberfast.getNumbers().add(16);
			}
		}
		if(numberfast.getXiao() == 1 //只选小 
				&& numberfast.getDan() == 0 && numberfast.getShuang() == 0){
			if(!numberfast.getNumbers().contains(5)){
				numberfast.getNumbers().add(5);
			}if(!numberfast.getNumbers().contains(7)){
				numberfast.getNumbers().add(7);
			}if(!numberfast.getNumbers().contains(9)){
				numberfast.getNumbers().add(9);
			}if(!numberfast.getNumbers().contains(4)){
				numberfast.getNumbers().add(4);
			}if(!numberfast.getNumbers().contains(6)){
				numberfast.getNumbers().add(6);
			}if(!numberfast.getNumbers().contains(8)){
				numberfast.getNumbers().add(8);
			}if(!numberfast.getNumbers().contains(10)){
				numberfast.getNumbers().add(10);
			}
		}
		if(numberfast.getXiao() == 1 //只选小，单
				&& numberfast.getDan() == 1 && numberfast.getShuang() == 0){
			if(!numberfast.getNumbers().contains(5)){
				numberfast.getNumbers().add(5);
			}if(!numberfast.getNumbers().contains(7)){
				numberfast.getNumbers().add(7);
			}if(!numberfast.getNumbers().contains(9)){
				numberfast.getNumbers().add(9);
			}
		}
		if(numberfast.getXiao() == 1 //只选小，双
				&& numberfast.getDan() == 0 && numberfast.getShuang() == 1){
			if(!numberfast.getNumbers().contains(4)){
				numberfast.getNumbers().add(4);
			}if(!numberfast.getNumbers().contains(6)){
				numberfast.getNumbers().add(6);
			}if(!numberfast.getNumbers().contains(8)){
				numberfast.getNumbers().add(8);
			}if(!numberfast.getNumbers().contains(10)){
				numberfast.getNumbers().add(10);
			}
		}
		if(numberfast.getDa() == 0 && numberfast.getXiao() == 0 //只选双
				&& numberfast.getShuang() == 1){
			if(!numberfast.getNumbers().contains(4)){
				numberfast.getNumbers().add(4);
			}if(!numberfast.getNumbers().contains(6)){
				numberfast.getNumbers().add(6);
			}if(!numberfast.getNumbers().contains(8)){
				numberfast.getNumbers().add(8);
			}if(!numberfast.getNumbers().contains(10)){
				numberfast.getNumbers().add(10);
			}if(!numberfast.getNumbers().contains(12)){
				numberfast.getNumbers().add(12);
			}if(!numberfast.getNumbers().contains(14)){
				numberfast.getNumbers().add(14);
			}if(!numberfast.getNumbers().contains(16)){
				numberfast.getNumbers().add(16);
			}
		}
		if(numberfast.getDa() == 0 && numberfast.getXiao() == 0 //只选单
				&& numberfast.getDan() == 1){
			if(!numberfast.getNumbers().contains(5)){
				numberfast.getNumbers().add(5);
			}if(!numberfast.getNumbers().contains(7)){
				numberfast.getNumbers().add(7);
			}if(!numberfast.getNumbers().contains(9)){
				numberfast.getNumbers().add(9);
			}if(!numberfast.getNumbers().contains(11)){
				numberfast.getNumbers().add(11);
			}if(!numberfast.getNumbers().contains(13)){
				numberfast.getNumbers().add(13);
			}if(!numberfast.getNumbers().contains(15)){
				numberfast.getNumbers().add(15);
			}if(!numberfast.getNumbers().contains(17)){
				numberfast.getNumbers().add(17);
			}
		}
		numberfast.setPlayType(WANFAINT);
		numberfast.setMode(DANTUO);
		numberfast.setNum(ChooseUtilFast3.getChooseUtil().getTime(numberfast));
		setNumbersChoosed(numberfast);
		setNumMoney(numberfast.getNum());
	}
	public void otherHezhi(){
		int daInt  = 0;
		int xiaoInt  = 0;
		int danInt  = 0;
		int shuangInt  = 0;
		if(numberfast.getNumbers().size()<3){
			numberfast.setDa(0);
			numberfast.setXiao(0);
			numberfast.setDan(0);
			numberfast.setShuang(0);
		}else{
			for(int i:numberfast.getNumbers()){
				if(i<11){
					numberfast.setDa(0);
					da.setBackgroundResource(0);
					da.setTextColor(Color.parseColor("#ffffff"));
				}else{
					daInt++;
				}
				if(i>10){
					numberfast.setXiao(0);
					xiao.setBackgroundResource(0);
					xiao.setTextColor(Color.parseColor("#ffffff"));
				}else{
					xiaoInt++;
				}
				if(i%2 == 0){
					numberfast.setDan(0);
					dan.setBackgroundResource(0);
					dan.setTextColor(Color.parseColor("#ffffff"));
				}else{
					danInt++;
				}
				if(i%2 == 1){
					numberfast.setShuang(0);
					shuang.setBackgroundResource(0);
					shuang.setTextColor(Color.parseColor("#ffffff"));
				}else{
					shuangInt++;
				}
			}
		}
		if(danInt == 7&&shuangInt == 0){
			numberfast.setDan(1);
			numberfast.setShuang(0);
			numberfast.setDa(0);
			numberfast.setXiao(0);

			da.setBackgroundResource(0);
			da.setTextColor(Color.parseColor("#ffffff"));

			xiao.setBackgroundResource(0);
			xiao.setTextColor(Color.parseColor("#ffffff"));

			dan.setBackgroundColor(Color.parseColor("#fab12f"));
			dan.setTextColor(Color.parseColor("#000000"));

			shuang.setBackgroundResource(0);
			shuang.setTextColor(Color.parseColor("#ffffff"));
		}else
			if(shuangInt == 7&&danInt == 0){
				numberfast.setDan(0);
				numberfast.setShuang(1);
				numberfast.setDa(0);
				numberfast.setXiao(0);
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));

				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundColor(Color.parseColor("#fab12f"));
				shuang.setTextColor(Color.parseColor("#000000"));
			}else if(daInt == 7&&xiaoInt == 0){
				numberfast.setDa(1);
				numberfast.setXiao(0);
				numberfast.setDan(0);
				numberfast.setShuang(0);
				da.setBackgroundColor(Color.parseColor("#fab12f"));
				da.setTextColor(Color.parseColor("#000000"));

				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}else if(xiaoInt == 7&&daInt == 0){
				numberfast.setDa(0);
				numberfast.setXiao(1);
				numberfast.setDan(0);
				numberfast.setShuang(0);
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));

				xiao.setBackgroundColor(Color.parseColor("#fab12f"));
				xiao.setTextColor(Color.parseColor("#000000"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}else if(danInt == 4 &&daInt == 4){
				numberfast.setDan(1);
				numberfast.setDa(1);
				numberfast.setShuang(0);
				numberfast.setXiao(0);
				da.setBackgroundColor(Color.parseColor("#fab12f"));
				da.setTextColor(Color.parseColor("#000000"));

				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));

				dan.setBackgroundColor(Color.parseColor("#fab12f"));
				dan.setTextColor(Color.parseColor("#000000"));

				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}else if(danInt == 3 &&xiaoInt == 3){
				numberfast.setDan(1);
				numberfast.setXiao(1);
				numberfast.setDa(0);
				numberfast.setShuang(0);
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));

				xiao.setBackgroundColor(Color.parseColor("#fab12f"));
				xiao.setTextColor(Color.parseColor("#000000"));

				dan.setBackgroundColor(Color.parseColor("#fab12f"));
				dan.setTextColor(Color.parseColor("#000000"));

				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}else if(shuangInt == 3 &&daInt == 3){
				numberfast.setDan(0);
				numberfast.setDa(1);
				numberfast.setShuang(1);
				numberfast.setXiao(0);
				da.setBackgroundColor(Color.parseColor("#fab12f"));
				da.setTextColor(Color.parseColor("#000000"));

				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundColor(Color.parseColor("#fab12f"));
				shuang.setTextColor(Color.parseColor("#000000"));
			}else if(shuangInt == 4 &&xiaoInt == 4){
				numberfast.setDan(0);
				numberfast.setDa(0);
				numberfast.setShuang(1);
				numberfast.setXiao(1);
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));

				xiao.setBackgroundColor(Color.parseColor("#fab12f"));
				xiao.setTextColor(Color.parseColor("#000000"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundColor(Color.parseColor("#fab12f"));
				shuang.setTextColor(Color.parseColor("#000000"));
			}else{
				da.setBackgroundResource(0);
				da.setTextColor(Color.parseColor("#ffffff"));

				xiao.setBackgroundResource(0);
				xiao.setTextColor(Color.parseColor("#ffffff"));

				dan.setBackgroundResource(0);
				dan.setTextColor(Color.parseColor("#ffffff"));

				shuang.setBackgroundResource(0);
				shuang.setTextColor(Color.parseColor("#ffffff"));
			}
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(DANTUO == 0){
			return ;
		}
		if(WANFAINT  == ConstantsBase.SANTONGHAOTONG||
				WANFAINT  == ConstantsBase.SANLIANHAO){
			return;	
		}
		int sensorType = event.sensor.getType();  
		//values[0]:X轴，values[1]：Y轴，values[2]：Z轴    
		float[] values = event.values;  

		if (sensorType == Sensor.TYPE_ACCELEROMETER)  
		{  
			/*正常情况下，任意轴数值最大就在9.8~10之间，只有在突然摇动手机  
	                                     的时候，瞬时加速度才会突然增大或减少。   监听任一轴的加速度大于17即可 
			 */  

			if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14  || Math  
					.abs(values[2]) > 14 ))  
			{  
				if(isMoving){
					return;
				}
				isMoving = true;
				vibrator.vibrate(150);

				clear();
				//检测到晃动后启动OpenDoor效果  
				numberfast = 
						ChooseUtilFast3.getChooseUtil().getRandom(WANFAINT);
				setNumMoney(numberfast.getNum());
				setToAreaView();
			}  
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}	
	public void setToAreaView(){
		isMoving = true;
		shakeview.setVisibility(View.VISIBLE);
		numchoose1.setVisibility(View.VISIBLE);
		numchoose2.setVisibility(View.VISIBLE);
		numchoose3.setVisibility(View.VISIBLE);
		choosees = new ImageView[]{numchoose1,numchoose2,numchoose3};
		nums.clear();
		switch (WANFAINT) {
		case ConstantsBase.HEZHI:	
			for(int i=0;i<choosees.length;i++){			
				nums.add(numberfast.getNumberHezhi().get(i)-1);
			}
			break;
		case ConstantsBase.SANTONGHAO:	
			for(int i=0;i<choosees.length;i++){		
				nums.add(numberfast.getNumbers().get(0)-1);	
			}
			break;
		case ConstantsBase.SANBUTONGHAO:	
			for(int i=0;i<choosees.length;i++){			
				nums.add(numberfast.getNumbers().get(i)-1);	
			}
			break;
		case ConstantsBase.ERTONGHAO:	
			for(int i=0;i<choosees.length;i++){	
				if(i == 0){
					nums.add(numberfast.getNumbers().get(0)-1);	
				}
				if(i == 1){
					nums.add(numberfast.getNumbers().get(0)-1);	
				}
				if(i == 2){
					nums.add(numberfast.getNumber1().get(0)-1);	
				}
			}
			break;
		case ConstantsBase.ERTONGHAOFU:	
			numchoose3.setVisibility(View.GONE);
			choosees = new ImageView[]{numchoose1,numchoose2};
			for(int i=0;i<choosees.length;i++){	
				nums.add(numberfast.getNumbers().get(0)-1);					
			}
			break;
		case ConstantsBase.ERBUTONGHAO:	
			numchoose3.setVisibility(View.GONE);
			choosees = new ImageView[]{numchoose1,numchoose2};
			for(int i=0;i<choosees.length;i++){		
				nums.add(numberfast.getNumbers().get(i)-1);	
			}

			break;
		}
		choosees[choosees.length-1].post(new Runnable() {
			@Override
			public void run() {
				setAnimationArea(nums);
			}
		});	
	}
	public void setAnimationArea(ArrayList<Integer> nums){		
		animatorSetFirst.clear();
		for(int i=0;i<choosees.length;i++){
			getAnimationSetFirst(choosees[i],nums.get(i));
		}
		for(int i = 0;i<animatorSetFirst.size();i++){
			animatorSetFirst.get(i).start();
		}
		animatorSet.clear();
		setToPlace();	
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				for(int i = 0;i<animatorSet.size();i++){
					animatorSet.get(i).start();
				}
			}
		}, 2000);	
	}
	public void setToPlace(){
		View viewView = null;
		TextView viewTextView = null;
		TextView viewTextViewtext = null;
		for(int i=0;i<choosees.length;i++){
			switch (WANFAINT) {
			case ConstantsBase.HEZHI:	
				viewView  =  findViewById(numbers[numberfast.getNumbers().get(0)-4]);
				viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(0)-4]);
				viewTextViewtext= (TextView) findViewById(numbers_txText[numberfast.getNumbers().get(0)-4]);
				break;
			case ConstantsBase.SANTONGHAO:	
				viewView  =  findViewById(numbers[numberfast.getNumbers().get(0)-1]);
				viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(0)-1]);
				viewTextViewtext= (TextView) findViewById(numbers_txText[numberfast.getNumbers().get(0)-1]);
				break;
			case ConstantsBase.SANBUTONGHAO:	
				viewView  =  findViewById(numbers[numberfast.getNumbers().get(i)-1]);
				viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(i)-1]);
				break;
			case ConstantsBase.ERTONGHAO:	
				if(i == 0){
					viewView  =  findViewById(numbers[numberfast.getNumbers().get(0)-1]);
					viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(0)-1]);
				}
				if(i == 1){
					viewView  =  findViewById(numbers[numberfast.getNumbers().get(0)-1]);
					viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(0)-1]);
				}
				if(i == 2){
					viewView  =  findViewById(number1[numberfast.getNumber1().get(0)-1]);
					viewTextView = (TextView) findViewById(number1Text[numberfast.getNumber1().get(0)-1]);
				}

				break;
			case ConstantsBase.ERTONGHAOFU:	
				viewView  =  findViewById(numbers[numberfast.getNumbers().get(0)-1]);
				viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(0)-1]);
				break;
			case ConstantsBase.ERBUTONGHAO:	
				viewView  =  findViewById(numbers[numberfast.getNumbers().get(i)-1]);
				viewTextView = (TextView) findViewById(numbersText[numberfast.getNumbers().get(i)-1]);
				break;
			}
			int[] locationFrom  = new int[2];  
			choosees[i].getLocationOnScreen(locationFrom);  

			int leftFrom = locationFrom[0];  
			int topFrom = locationFrom[1]; 
			int[] locationTo  = new int[2];  
			viewView.getLocationOnScreen(locationTo);  
			int leftTo = locationTo[0];  
			int topTo = locationTo[1]; 
			getAnimationSet(choosees[i],viewView,viewTextView,viewTextViewtext,leftFrom,topFrom,leftTo,topTo);
		}
	}	
	public void getAnimationSetFirst(final ImageView view,final int num){
		int[] locationFrom  = new int[2];  
		view.getLocationOnScreen(locationFrom);  
		ObjectAnimator rotateX1 = ObjectAnimator.ofFloat(view, "translationX",0f,
				0-random.nextInt(locationFrom[0]),
				0f); 
		ObjectAnimator rotateY1 = ObjectAnimator.ofFloat(view, "translationY",0f,
				0-random.nextInt(locationFrom[1])/2,
				0f);  
		ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "rotation", 0f,360000f);
		AnimatorSet animSet = new AnimatorSet();  
		animSet.play(moveIn).with(rotateX1).with(rotateY1);  
		animSet.setDuration(1000); 
		animSet.setInterpolator(new OvershootInterpolator());  
		animatorSetFirst.add(animSet);
		rotateY1.addListener(new AnimatorListener() {  
			@Override  
			public void onAnimationStart(Animator animation) { 
				if(timer2 == null){
					end = false; 
					timer2 = new Timer();
					setShaiZiView()	;
				}		
			}  

			@Override  
			public void onAnimationRepeat(Animator animation) {  
			}  

			@Override  
			public void onAnimationEnd(Animator animation) {
				if(timer2 != null){
					end = true;
					timer2.cancel();
					timer2 = null;
				}
				view.setImageResource(shakenumbers[num]);
			}  

			@Override  
			public void onAnimationCancel(Animator animation) {  
			}  
		}); 
	}
	public void setShaiZiView(){
		if(timer2!=null){
			timer2.scheduleAtFixedRate(new TimerTask()  
			{  
				@Override  
				public void run()  
				{  
					Message msg = Message.obtain();  
					msg.what = 1; 
					handler.sendMessage(msg);  
				}
			},0, 100);  
		}
	}
	public void getAnimationSetAfter(View view){
		ObjectAnimator rotateX = ObjectAnimator.ofFloat(view, "translationX", 0); 
		ObjectAnimator rotateY = ObjectAnimator.ofFloat(view, "translationY", 0);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f); 
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f); 
		rotateX.start();
		rotateY.start();
		scaleY.start();
		scaleX.start();
	}
	public void getAnimationSet(View view,final View viewToView,final TextView viewToTextView,final TextView viewToTextViewtext,
			int leftFrom,int topFrom,int leftTo,int topTo){
		ObjectAnimator rotateX = ObjectAnimator.ofFloat(view, "translationX", 0f,leftTo-leftFrom); 
		ObjectAnimator rotateY = ObjectAnimator.ofFloat(view, "translationY", 0f,topTo-topFrom);  
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f); 
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f); 
		AnimatorSet animSet = new AnimatorSet();  
		animSet.play(rotateX).with(rotateY).with(scaleY).with(scaleX);  
		animSet.setDuration(1000); 
		animatorSet.add(animSet);
		scaleX.addListener(new AnimatorListener() {  
			@Override  
			public void onAnimationStart(Animator animation) {  
			}  

			@Override  
			public void onAnimationRepeat(Animator animation) {  
			}  

			@Override  
			public void onAnimationEnd(Animator animation) {  
				for(int i=0;i<choosees.length;i++){
					getAnimationSetAfter(choosees[i]);
				}
				shakeview.setVisibility(View.GONE);

				viewToTextView.setTextColor(Color.parseColor("#FFBA00"));
				if(viewToTextViewtext!=null){
					viewToTextViewtext.setTextColor(Color.parseColor("#FFBA00"));
				}
				viewToView.setBackgroundResource(R.drawable.corner_lightgreenchoosed);
				isMoving = false;
			}  

			@Override  
			public void onAnimationCancel(Animator animation) {  
			}  
		}); 
	}

	public void dialog() {
		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setCancelable(false);
		}
		if(!alertDialog.isShowing()){
			alertDialog.show();
		}
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("不支持多种玩法同时选择 \n您需要删除已选号码才可切换");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("取消");
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
		ok.setText("确定");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				Intent intent = new Intent();  
				Bundle bundle = new Bundle();  
				if(numberfast!=null){
					numberfast.setPlayType(WANFAINT);
					numberfast.setMode(DANTUO); 
					bundle.putInt("position", position);
					bundle.putBoolean("clear", true);
					bundle.putSerializable("numberfast", numberfast);
				}else{  
					bundle.putInt("position", position);
					bundle.putBoolean("clear", true);
					bundle.putSerializable("numberfastList", choosedNumbers); 		
				}	
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent); 
				_this.finish();	
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
	public void showStopDialog(String qici) {
		if(endAlertDialog == null){
			endAlertDialog = new AlertDialog.Builder(this).create();
			endAlertDialog.setCancelable(false);
		}
		if(!endAlertDialog.isShowing()){
			endAlertDialog.show();
		}
		Window window = endAlertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText(qici+"期已截止，请确认期次投注！");
		updatesize.setTextSize(18);
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setVisibility(View.GONE);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("确定");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endAlertDialog.dismiss();
				endAlertDialog.cancel();
			}
		});
	}
	public RotateAnimation  getAnimation(){
		RotateAnimation rotate = new RotateAnimation(0,2880,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(250);// 动画时间
		rotate.setFillAfter(true);// 保持动画状态
		return rotate;
	}
	class MyListAdapter extends BaseAdapter {
		ArrayList<Prize>	resultList = new ArrayList<Prize>();
		public void setDate(ArrayList<Prize>	resultListM) {
			resultList = resultListM;
		}
		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public Prize getItem(int position) {
			return resultList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final Prize order = resultList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.qishufast3_item,null);
				holder.qishu = (TextView) convertView.findViewById(R.id.qishu);
				holder.im1 = (ImageView) convertView.findViewById(R.id.im1);
				holder.im2 = (ImageView) convertView.findViewById(R.id.im2);
				holder.im3 = (ImageView) convertView.findViewById(R.id.im3);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.state = (TextView) convertView.findViewById(R.id.state);
				holder.hezhi = (TextView) convertView.findViewById(R.id.hezhi);
				holder.daxiao = (TextView) convertView.findViewById(R.id.daxiao);
				holder.danshuang = (TextView) convertView.findViewById(R.id.danshuang);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String[] prizes = order.getResult().split(",");
			if(prizes!=null&&prizes.length == 3){
				holder.im1.setImageResource(prizenumbers[Integer.parseInt(prizes[0])-1]);
				holder.im2.setImageResource(prizenumbers[Integer.parseInt(prizes[1])-1]);
				holder.im3.setImageResource(prizenumbers[Integer.parseInt(prizes[2])-1]);
			}
			holder.number.setText(prizes[0]+" "+prizes[1]+" "+prizes[2]);
			String  per = order.getPeriodNumber();
			if(!TextUtils.isEmpty(per)
					&&per.length()>=2){
				holder.qishu.setText(per.substring(per.length()-2, per.length())+"期");

			}
			int hezhi = Integer.parseInt(prizes[0])+Integer.parseInt(prizes[1])+Integer.parseInt(prizes[2]);
			if(hezhi<10){
				holder.hezhi.setText("0"+hezhi);
			}else{
				holder.hezhi.setText(""+hezhi);
			}
			if(hezhi%2==0){
				holder.danshuang.setText("双");
			}else{
				holder.danshuang.setText("单");
			}
			if(hezhi <=17&&hezhi>=11){
				holder.daxiao.setText("大");
			}else{
				holder.daxiao.setText("小");
			}
			holder.state.setText(order.getResultType());
			if(WANFAINT == ConstantsBase.HEZHI){
				holder.hezhi.setVisibility(View.VISIBLE);
				holder.daxiao.setVisibility(View.VISIBLE);
				holder.danshuang.setVisibility(View.VISIBLE);
				holder.state.setVisibility(View.GONE);
			}else{
				holder.hezhi.setVisibility(View.GONE);
				holder.daxiao.setVisibility(View.GONE);
				holder.danshuang.setVisibility(View.GONE);
				holder.state.setVisibility(View.VISIBLE);
			}
			return convertView;
		}


		class ViewHolder {
			TextView qishu;
			ImageView im1;
			ImageView im2;
			ImageView im3;
			TextView number;
			TextView state;
			TextView hezhi;
			TextView daxiao;
			TextView danshuang;

		}
	}
	public void setToShowPeriod(boolean isShow){
		int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		qishuviewall.measure(intw, inth);
		qishulistview.measure(intw, inth);  
		int height = qishulistview.getMeasuredHeight()*9+qishuviewall.getMeasuredHeight()+10;

		ObjectAnimator rotateY;
		if(isShow){
			rotateY = ObjectAnimator.ofFloat(scrollview, "translationY", 0f,height); 
		}else{
			rotateY = ObjectAnimator.ofFloat(scrollview, "translationY", height,0f); 
		}
		AnimatorSet animSet = new AnimatorSet();  
		animSet.play(rotateY);  
		animSet.setDuration(500); 
		animSet.start();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		if(alertDialog!=null){
			alertDialog.cancel();
			alertDialog = null;
		}
		if(endAlertDialog!=null){
			endAlertDialog.cancel();
			endAlertDialog = null;
		}
		clearTime();
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(timer2!=null){
			timer2.cancel();
			timer2=null;
		}
		idStop = true;
	}
}
