package com.cshen.tiyu.activity.lottery.cai115;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.NowPrize;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.CurtainView115;
import com.cshen.tiyu.widget.CurtainView115.WeiClickItemListener;


public class MainGd115Activity  extends BaseActivity  implements OnClickListener,SensorEventListener  {
	public static  int WANFAINT = ConstantsBase.RENXUAN2;
	public static  int DANTUO = 1;
	public static  int NUMLEAST = 2;
	public static  int NUMMOST = 2;
	private MainGd115Activity _this;
	private TextView tv_head_item;//???
	private ImageView shuoming,back;
	private View load;//?????????
	private PopupWindow pop;//???????????????
	private View viewPop;
	private int[] wanfas_tv = new int[]{
			R.id.rx1_tv,R.id.rx2_tv,R.id.q2zux_tv,R.id.q2zx_tv,
			R.id.rx3_tv,R.id.q3zux_tv,R.id.q3zx_tv,R.id.rx4_tv,
			R.id.rx5_tv,R.id.rx6_tv,R.id.rx7_tv,R.id.rx8_tv,
			R.id.rx2dt_tv,R.id.rx3dt_tv,R.id.rx4dt_tv,R.id.rx5dt_tv,
			R.id.rx6dt_tv,R.id.rx7dt_tv,R.id.q2zuxdt_tv,R.id.q3zuxdt_tv};


	private com.cshen.tiyu.widget.CurtainView115 curtain;
	private View zhanwei;


	private SensorManager mSensorManager;//???????????????
	private Vibrator vibrator ;//????????????
	private View shake;//???????????????
	private boolean isMoving = false;//???????????????
	private View[] chooseesView ;//???????????????view?????????????????????view?????????,?????????view???????????????????????????textview?????????
	private View chooseView1,chooseView2,chooseView3,chooseView4,
	chooseView5,chooseView6,chooseView7,chooseView8;
	private TextView[] choosees ;//????????????
	private TextView choose1,choose2,choose3,choose4,
	choose5,choose6,choose7,choose8;
	int scaleAnimationsI=0;
	private ArrayList<TranslateAnimation> translateAnimations = new ArrayList<TranslateAnimation>();//???????????????????????????


	private View wudantuoView;
	private TextView wanwei,least,maymoney,prizeMoney;
	private View qianweiview,baiweiview;
	private int[] wanweis = new int[]{
			R.id.wanweinum1,R.id.wanweinum2,R.id.wanweinum3,R.id.wanweinum4,
			R.id.wanweinum5,R.id.wanweinum6,R.id.wanweinum7,R.id.wanweinum8,
			R.id.wanweinum9,R.id.wanweinum10,R.id.wanweinum11};
	private int[] qianweis = new int[]{
			R.id.qianweinum1,R.id.qianweinum2,R.id.qianweinum3,R.id.qianweinum4,
			R.id.qianweinum5,R.id.qianweinum6,R.id.qianweinum7,R.id.qianweinum8,
			R.id.qianweinum9,R.id.qianweinum10,R.id.qianweinum11};
	private int[] baiweis = new int[]{
			R.id.baiweinum1,R.id.baiweinum2,R.id.baiweinum3,R.id.baiweinum4,
			R.id.baiweinum5,R.id.baiweinum6,R.id.baiweinum7,R.id.baiweinum8,
			R.id.baiweinum9,R.id.baiweinum10,R.id.baiweinum11};

	private View dantuoView;
	private TextView danmatitle,tuomatitle;
	private int[] dans = new int[]{
			R.id.danmanum1,R.id.danmanum2,R.id.danmanum3,R.id.danmanum4,
			R.id.danmanum5,R.id.danmanum6,R.id.danmanum7,R.id.danmanum8,
			R.id.danmanum9,R.id.danmanum10,R.id.danmanum11};
	private int[] tuos = new int[]{
			R.id.tuomanum1,R.id.tuomanum2,R.id.tuomanum3,R.id.tuomanum4,
			R.id.tuomanum5,R.id.tuomanum6,R.id.tuomanum7,R.id.tuomanum8,
			R.id.tuomanum9,R.id.tuomanum10,R.id.tuomanum11};

	private TextView money;//??????
	private ImageView clear;//??????
	private View sure;//??????

	private AlertDialog  alertDialog;
	private Number115  number115;
	private String state ; 
	private int OLDWANFA,OLDDANTUO ; 
	private int position =-1;//??????-1????????????????????????
	private Timer timer; 
	private boolean idStop = false;
	private String wei = "";
	String	periodTime = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main115);
		_this = this;
		initView();
		initdata(); 
	}

	public void initView(){
		/********************??????***********************/
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(this);
		shuoming = (ImageView) findViewById(R.id.iv_setting);
		shuoming.setOnClickListener(this);
		findViewById(R.id.tv_head).setOnClickListener(this);
		tv_head_item = (TextView) findViewById(R.id.tv_head_title_item);

		((TextView) findViewById(R.id.tv_head_title)).setText("??????11???5???");
		/********************??????????????????***********************/
		curtain = (CurtainView115) findViewById(R.id.dlt_show_downview);
		curtain.setWeiClickItemListener(new WeiClickItemListener(){
			@Override
			public void clickWan(View view) {
				wei = "wan";
				initHistroyDataForCathectic();
			}
			@Override
			public void clickQian(View view) {
				wei = "qian";
				initHistroyDataForCathectic();
			}
			@Override
			public void clickBai(View view) {
				wei = "bai";
				initHistroyDataForCathectic();
			}
			@Override
			public void clickTimeOver(View view) {
				// TODO Auto-generated method stub
				showStopDialog(periodTime);
			}
			@Override
			public void clickRealTimeOver(View view) {
				// TODO Auto-generated method stub
				if(timer!=null){
					timer.cancel();
					timer=null;
				}
				initDataForCathectic();
			}
		});
		zhanwei = findViewById(R.id.dlt_zhanwei_wuyong2);
		curtain.setViewZhanwei(zhanwei);
		/********************???????????????***********************/
		wudantuoView = findViewById(R.id.wudantuo_show_view);
		least = (TextView) findViewById(R.id.least);
		maymoney  = (TextView) findViewById(R.id.maymoney);
		shake = findViewById(R.id.dlt_shake);
		shake.setOnClickListener(this);			
		wanwei = (TextView) findViewById(R.id.wanwei);
		qianweiview = findViewById(R.id.qianweiview);
		baiweiview = findViewById(R.id.baiweiview);
		for(int i=0;i<wanweis.length;i++){
			findViewById(wanweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<qianweis.length;i++){
			findViewById(qianweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<baiweis.length;i++){
			findViewById(baiweis[i]).setOnClickListener(this);
		}
		/********************??????***********************/
		dantuoView = findViewById(R.id.dantuo_show_view);
		danmatitle = (TextView)findViewById(R.id.danmatitle);
		tuomatitle = (TextView)findViewById(R.id.tuomatitle);
		for(int i=0;i<dans.length;i++){
			findViewById(dans[i]).setOnClickListener(this);
		}
		for(int i=0;i<tuos.length;i++){  
			findViewById(tuos[i]).setOnClickListener(this);
		}
		/********************??????***********************/
		clear =  (ImageView) findViewById(R.id.dlt_clear);
		clear.setOnClickListener(this);
		sure =  findViewById(R.id.dlt_sure);
		sure.setOnClickListener(this);
		money = (TextView) findViewById(R.id.dlt_money);
		prizeMoney = (TextView) findViewById(R.id.prizemoney);
		setNumMoney(0);
		/********************???????????????***********************/
		choose1 = (TextView) findViewById(R.id.dltnumchoose1);
		choose2 = (TextView) findViewById(R.id.dltnumchoose2);
		choose3 = (TextView) findViewById(R.id.dltnumchoose3);
		choose4 = (TextView) findViewById(R.id.dltnumchoose4);
		choose5 = (TextView) findViewById(R.id.dltnumchoose5);
		choose6 = (TextView) findViewById(R.id.dltnumchoose6);
		choose7 = (TextView) findViewById(R.id.dltnumchoose7);
		choose8 = (TextView) findViewById(R.id.dltnumchoose8);
		chooseView1 = findViewById(R.id.dltnumchoose1view);
		chooseView2 = findViewById(R.id.dltnumchoose2view);
		chooseView3 = findViewById(R.id.dltnumchoose3view);
		chooseView4 = findViewById(R.id.dltnumchoose4view);
		chooseView5 = findViewById(R.id.dltnumchoose5view);
		chooseView6 = findViewById(R.id.dltnumchoose6view);
		chooseView7 = findViewById(R.id.dltnumchoose7view);
		chooseView8 = findViewById(R.id.dltnumchoose8view);
		choosees = new TextView[]{choose1,choose2,choose3,choose4,choose5,choose6,choose7,choose8};
		chooseesView  = new View[]{chooseView1,chooseView2,chooseView3,chooseView4,
				chooseView5,chooseView6,chooseView7,chooseView8};
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		load.setOnClickListener(this);
	}
	private void initdata() {
		Bundle b = getIntent().getExtras();
		try{
			position = b.getInt("position");
		}catch(Exception e){
			position = -1;
			e.printStackTrace();
		}
		try{
			WANFAINT = b.getInt("wanfa");
		}catch(Exception e){
			WANFAINT = PreferenceUtil.getInt(this, PreferenceUtil.PERIOD115NUMBERWANFAN,ConstantsBase.RENXUAN2); 
			e.printStackTrace();
		}
		NUMLEAST = ChooseUtil.chooseNUMLEAST(WANFAINT);
		NUMMOST = ChooseUtil.chooseNUMMOST(WANFAINT);
		OLDWANFA = WANFAINT;
		try{
			DANTUO = b.getInt("dantuo");
		}catch(Exception e){
			DANTUO = 1;
			DANTUO = PreferenceUtil.getInt(this, PreferenceUtil.PERIOD115NUMBERDANTUO,1); 
			e.printStackTrace();
		}
		OLDDANTUO = DANTUO;
		try{
			number115 = (Number115)b.getSerializable("number115");
			if(number115!=null){
				setNumbersChoosed(number115);			
				setNumMoney(number115.getNum());
			}
			tv_head_item.setText(ChooseUtil.choose(WANFAINT,DANTUO));
			setView();
		}catch(Exception e){
			number115 = null;
			tv_head_item.setText(ChooseUtil.choose(WANFAINT,DANTUO));
			clear();
			setNumMoney(0);
			setView();
			e.printStackTrace();
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
		//???????????????????????????   
		vibrator = (Vibrator) this
				.getSystemService(Service.VIBRATOR_SERVICE);
		mSensorManager = (SensorManager) this  
				.getSystemService(Service.SENSOR_SERVICE);  
		//??????????????????    
		mSensorManager.registerListener(this,  
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
				//??????SENSOR_DELAY_UI???SENSOR_DELAY_FASTEST???SENSOR_DELAY_GAME??????    
				//?????????????????????????????????????????????????????????????????????????????????    
				SensorManager.SENSOR_DELAY_NORMAL);  
		getNetDate();
	}  
	@Override  
	public void onPause()  
	{  
		curtain.clearTime();
		PreferenceUtil.putInt(this, PreferenceUtil.PERIOD115NUMBERWANFAN,WANFAINT);
		PreferenceUtil.putInt(this, PreferenceUtil.PERIOD115NUMBERDANTUO,DANTUO);
		mSensorManager.unregisterListener(this); 
		idStop = true;
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		super.onPause();  
	}  	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.load:
			if (pop.isShowing()) {
				pop.dismiss();
				load.setVisibility(View.GONE);
			}
			break;
		case R.id.iv_back:
			_this.finish();
			break;
		case R.id.tv_head:
			if(!isMoving){
				if("change".equals(state)){
					ToastUtils.showShort(_this, "??????????????????????????????");
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
					tv_head_item.setText(ChooseUtil.choose(WANFAINT,DANTUO));
				}
	
			}
			break;
		case R.id.iv_setting:
			Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
			intentHelp.putExtra("url","http://an.caiwa188.com/help/syydj.html");
			startActivity(intentHelp);
			break;
		case R.id.dlt_shake:
			if(!isMoving){
				vibrator.vibrate(150);
				jiXuanChoose();
			}
			break;
		case R.id.dlt_clear:
			if(!isMoving){
				clear();
				setNumMoney(0);
			}
			break;
		case R.id.dlt_sure:
			if(!isMoving){
				if(curtain.timeend!=null&&"?????????".equals(curtain.timeend.getText().toString())){
					ToastUtils.showShort(_this, "???????????????");
				}else{
					sure();
				}
			}
			break;
		default:
			if(!isMoving){
				chooseBall(v);
			}
			break;
		}
	}

	/******************************?????????????????????*****************************************/
	private void getNetDate() {
		initHistroyDataForCathectic();
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
	private void initHistroyDataForCathectic(){//final String wei) {
		String missType = "";
		if(WANFAINT == ConstantsBase.ZUXUAN2){
			missType = "2";
		}else if(WANFAINT == ConstantsBase.ZUXUAN3){
			missType = "3";
		}else if(WANFAINT == ConstantsBase.ZHIXUAN1||
				WANFAINT == ConstantsBase.ZHIXUAN2||
				WANFAINT == ConstantsBase.ZHIXUAN3){
			missType = "4";
		}else{
			missType = "1";
		}
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,ConstantsBase.GD115+"","0",10+"",missType,new CallBack<PrizeList>() {
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
						curtain.addItenView(resultList,wei);	
					}
				}
			}
		});
	}
	private void initDataForCathectic() {
		// TODO ???????????????????????????
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.GD115+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO ???????????????????????????
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// ?????????????????????
					if(period!=null){
						periodTime = period.getPeriodNumber();
						PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115GD,periodTime);
						if(periodTime.length() >2){
							periodTime = periodTime.substring(periodTime .length()-2,periodTime.length());     					
						}
						Long time = period.getInterval();
						Long realtime = period.getRealInterval();
						curtain.openTime(periodTime,time,realtime);
					}
				}
				initKaijiangDataForCathectic();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO ???????????????????????????
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}

	private void initKaijiangDataForCathectic() {
		ServiceCaiZhongInformation.getInstance().pastNowPrize(_this,ConstantsBase.GD115+"",new CallBack<NowPrize>() {
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
						curtain.showNumber(t);
						initHistroyDataForCathectic();
					}else{
						curtain.showNone(t);
						if(timer == null){
							timer = new Timer();
							setTimer(3);
						}
					}		
				}
			}
		});
	}
	/******************************????????????*****************************************/	
	public void setView(){
		isMoving = false;
		if(DANTUO == 1){
			wudantuoView.setVisibility(View.VISIBLE);
			dantuoView.setVisibility(View.INVISIBLE);			
			String leastStr = "<html><font color=\"#666666\">????????????"
					+"</font><font color=\"#FF3232\">"+NUMLEAST+"???"
					+ "</font><font color=\"#666666\">??????</font></html>";
			least.setText(Html.fromHtml(leastStr));
			String maymoneyStr = "";
			if(WANFAINT == ConstantsBase.ZHIXUAN2||WANFAINT == ConstantsBase.ZHIXUAN3){
				wanwei.setText("??????");
				qianweiview.setVisibility(View.VISIBLE);
				if(WANFAINT == ConstantsBase.ZHIXUAN3){
					baiweiview.setVisibility(View.VISIBLE);
					maymoneyStr = "<html><font color=\"#666666\">???????????????3??????????????????"
							+"</font><font color=\"#FF3232\">1170???"
							+ "</font></html>";
				}else{
					baiweiview.setVisibility(View.GONE);
					maymoneyStr = "<html><font color=\"#666666\">???????????????2??????????????????"
							+"</font><font color=\"#FF3232\">130???"
							+ "</font></html>";
				}
			}else{
				wanwei.setText("??????");
				qianweiview.setVisibility(View.GONE);
				baiweiview.setVisibility(View.GONE);
				if(WANFAINT == ConstantsBase.ZHIXUAN1){
					maymoneyStr = "<html><font color=\"#666666\">?????????1??????????????????"
							+"</font><font color=\"#FF3232\">13???"
							+ "</font></html>";
				}else if(WANFAINT == ConstantsBase.ZUXUAN2){
					maymoneyStr = "<html><font color=\"#666666\">?????????2??????????????????"
							+"</font><font color=\"#FF3232\">65???"
							+ "</font></html>";
				}else if(WANFAINT == ConstantsBase.ZUXUAN3){
					maymoneyStr = "<html><font color=\"#666666\">?????????3??????????????????"
							+"</font><font color=\"#FF3232\">195???"
							+ "</font></html>";
				}else{
					int mayMoney = 0;
					switch (WANFAINT) {
					case ConstantsBase.RENXUAN2:
						mayMoney = 6;
						break;
					case ConstantsBase.RENXUAN3:
						mayMoney = 19;
						break;
					case ConstantsBase.RENXUAN4:
						mayMoney = 78;
						break;
					case ConstantsBase.RENXUAN5:
						mayMoney = 540;
						break;
					case ConstantsBase.RENXUAN6:
						mayMoney = 90;
						break;
					case ConstantsBase.RENXUAN7:
						mayMoney = 26;
						break;
					case ConstantsBase.RENXUAN8:
						mayMoney = 9;
						break;
					}
					if(NUMLEAST>=5){
						maymoneyStr = "<html><font color=\"#666666\">????????????5???????????????"
								+"</font><font color=\"#FF3232\">"+mayMoney+"???"
								+ "</font></html>";
					}else{
						maymoneyStr = "<html><font color=\"#666666\">????????????"+NUMLEAST+"???????????????"
								+"</font><font color=\"#FF3232\">"+mayMoney+"???"
								+ "</font></html>";
					}
				}
			}
			maymoney.setText(Html.fromHtml(maymoneyStr));
		}
		if(DANTUO == 0){//??????
			wudantuoView.setVisibility(View.INVISIBLE);
			dantuoView.setVisibility(View.VISIBLE);
			String danmatitleStr;
			if(NUMMOST == 1){
				danmatitleStr =  "<html><font color=\"#666666\">?????????"
						+"</font><font color=\"#FF3232\">1???"
						+"</font><font color=\"#666666\">??????"
						+ "</font></html>";
			}else{
				danmatitleStr=  "<html><font color=\"#666666\">?????????"
						+"</font><font color=\"#FF3232\">1???"
						+"</font><font color=\"#666666\">?????????????????????"
						+"</font><font color=\"#FF3232\">"+NUMMOST+"???"
						+"</font><font color=\"#666666\">??????"
						+ "</font></html>";
			}

			String tuomatitleStr = "<html><font color=\"#666666\">????????????"
					+"</font><font color=\"#FF3232\">1???"
					+"</font><font color=\"#666666\">?????????????????????"
					+"</font><font color=\"#FF3232\">10???"
					+"</font><font color=\"#666666\">??????"
					+ "</font></html>";
			tuomatitle.setText(Html.fromHtml(tuomatitleStr));
			danmatitle.setText(Html.fromHtml(danmatitleStr));
		}
		curtain.setWanfa(WANFAINT);
		if(WANFAINT == ConstantsBase.ZHIXUAN2
				||WANFAINT == ConstantsBase.ZHIXUAN3){
			wei = "wan";
			initHistroyDataForCathectic();
		}else if(WANFAINT == ConstantsBase.ZHIXUAN1){
			wei = "wan";
			initHistroyDataForCathectic();
		}else{
			wei = "";
			initHistroyDataForCathectic();
		}
	}
	public void clear(){
		number115 = null;
		for(int i=0;i<wanweis.length;i++){
			seColorBack(wanweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<qianweis.length;i++){
			seColorBack(qianweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<baiweis.length;i++){
			seColorBack(baiweis[i],"#E73C42",R.drawable.dlt_number_back);
		}	
		for(int i=0;i<dans.length;i++){
			seColorBack(dans[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<tuos.length;i++){
			seColorBack(tuos[i],"#E73C42",R.drawable.dlt_number_back);
		}
	}
	public void setNumbersChoosed(Number115 numbers){
		if(numbers.getPlayType() == ConstantsBase.ZHIXUAN2 ||
				numbers.getPlayType() == ConstantsBase.ZHIXUAN3){
			for(int i=0;i<numbers.getWan().size();i++){
				seColorBack(wanweis[numbers.getWan().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
			for(int i=0;i<numbers.getQian().size();i++){
				seColorBack(qianweis[numbers.getQian().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
			if(numbers.getPlayType() == ConstantsBase.ZHIXUAN3){
				for(int i=0;i<numbers.getBai().size();i++){
					seColorBack(baiweis[numbers.getBai().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
				}
			}
		}else{
			if(DANTUO == 0){
				for(int i=0;i<numbers.getDan().size();i++){
					seColorBack(dans[numbers.getDan().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
				}
				for(int i=0;i<numbers.getTuo().size();i++){
					seColorBack(tuos[numbers.getTuo().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
				}
			}else{
				for(int i=0;i<numbers.getNumbers().size();i++){
					seColorBack(wanweis[numbers.getNumbers().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
				}
			}
		}
	}
	public void seColorBack(int id,String color,int back){
		((TextView) findViewById(id)).setTextColor(Color.parseColor(color));
		findViewById(id).setBackgroundResource(back);
	}
	public void sure(){
		if(number115 == null
				||number115.getNumbers() == null
				||number115.getNumbers().size() == 0){//????????????

			if(DANTUO == 0){
				ToastUtils.showShort(_this, "????????????");
			}if(DANTUO == 1){
				jiXuanChoose();
			}
			return;
		}else{
			if(DANTUO == 0){
				if(number115.getDan()== null
						||number115.getDan().size() == 0){
					ToastUtils.showShort(_this, "???????????????");
					return;
				}
				if(number115.getTuo()== null
						||number115.getTuo().size() == 0){
					ToastUtils.showShort(_this, "???????????????");
					return;
				}
				if(number115.getNumbers().size()<NUMLEAST){
					ToastUtils.showShort(_this, "????????????"+NUMLEAST+"?????????");
					return;
				}
			}if(DANTUO == 1){
				if(number115.getNumbers().size()<NUMLEAST){
					ToastUtils.showShort(_this, "????????????"+NUMLEAST+"?????????");
					return;
				}
				if(WANFAINT == ConstantsBase.ZHIXUAN2//????????????
						||WANFAINT == ConstantsBase.ZHIXUAN3){//????????????
					if(number115.getWan()== null
							||number115.getWan().size() == 0){
						ToastUtils.showShort(_this, "?????????????????????");
						return;
					}
					if(number115.getQian()== null
							||number115.getQian().size() == 0){
						ToastUtils.showShort(_this, "?????????????????????");
						return;
					}
					if(WANFAINT == ConstantsBase.ZHIXUAN3
							&&(number115.getBai()== null
							||number115.getBai().size()  == 0)){
						ToastUtils.showShort(_this, "?????????????????????");
						return;
					}
				}

			}
			number115.setPlayType(WANFAINT);
			number115.setMode(DANTUO);
			number115.setNum(ChooseUtil.getChooseUtil().get115Time(number115));
			number115 = ChooseUtil.getChooseUtil().getSortList115(number115);
			if(position == -1){
				Intent intent = new Intent(_this, Accountgd115ListActivity.class);
				Bundle bundle = new Bundle();   
				bundle.putSerializable("number115", number115);
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				if("add".equals(state)
						&&(OLDWANFA!=WANFAINT||
						OLDDANTUO!=DANTUO)){
					dialog() ;
					return;
				}
				Intent intent = new Intent();  
				Bundle bundle = new Bundle();   
				bundle.putInt("position", position);
				bundle.putSerializable("number115", number115);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent); 
			}
			this.finish();
		}
	}
	public void chooseBall(View v){
		if(DANTUO == 1){
			Arrays.sort(wanweis);  
			Arrays.sort(qianweis); 
			Arrays.sort(baiweis); 
			int indexWan = Arrays.binarySearch(wanweis, v.getId()); 
			int indexQian = Arrays.binarySearch(qianweis, v.getId());  
			int indexBai = Arrays.binarySearch(baiweis, v.getId()); 
			if(indexWan>=0){//?????????0?????????????????????1??????
				if(!isWanHave(indexWan)){
					number115.getNumbers().add(indexWan+1);
					number115.getWan().add(indexWan+1);
					seColorBack(wanweis[indexWan],"#FFFFFF",R.drawable.dlt_number_back_red);
					isQianHave(indexWan);
					isBaiHave(indexWan);
				}
			}else if(indexQian>=0){
				if(!isQianHave(indexQian)){//???????????????????????????????????????
					number115.getNumbers().add(indexQian+1);
					number115.getQian().add(indexQian+1);
					seColorBack(qianweis[indexQian],"#FFFFFF",R.drawable.dlt_number_back_red);
					isWanHave(indexQian);
					isBaiHave(indexQian);
				}
			}else if(indexBai>=0){
				if(!isBaiHave(indexBai)){//???????????????????????????????????????
					number115.getNumbers().add(indexBai+1);
					number115.getBai().add(indexBai+1);
					seColorBack(baiweis[indexBai],"#FFFFFF",R.drawable.dlt_number_back_red);
					isWanHave(indexBai);
					isQianHave(indexBai);
				}
			}
			if(number115 == null||number115.getNumbers().size()<NUMLEAST){
				if(number115!=null){
					number115.setNum(0);
				}
				setNumMoney(0);
			}else{
				number115.setPlayType(WANFAINT);
				number115.setMode(DANTUO);
				if(WANFAINT == ConstantsBase.ZHIXUAN2||WANFAINT == ConstantsBase.ZHIXUAN3){//?????????
					if(number115.getWan()== null||number115.getWan().size() == 0
							||number115.getQian()== null||number115.getQian().size() == 0){
						number115.setNum(0);
						setNumMoney(0);
					}else if(WANFAINT == ConstantsBase.ZHIXUAN3
							&&(number115.getBai()== null||number115.getBai().size()  == 0)){
						number115.setNum(0);
						setNumMoney(0);
					}else{
						int time = ChooseUtil.getChooseUtil().get115Time(number115);
						number115.setNum(time);
						setNumMoney(time);
					}
				}else{
					int time = ChooseUtil.getChooseUtil().get115Time(number115);
					number115.setNum(time);
					setNumMoney(time);
				}

			}
		}
		if(DANTUO == 0){
			Arrays.sort(dans);  
			Arrays.sort(tuos); 
			int indexDan = Arrays.binarySearch(dans, v.getId()); 
			int indexTuo = Arrays.binarySearch(tuos, v.getId());
			if(indexDan>=0){//?????????0?????????????????????1??????
				if(!isDanHave(indexDan,true)){
					number115.getNumbers().add(indexDan+1);
					number115.getDan().add(indexDan+1);
					seColorBack(dans[indexDan],"#FFFFFF",R.drawable.dlt_number_back_red);
					isTuoHave(indexDan,false);
				}
			}else if(indexTuo>=0){
				if(!isTuoHave(indexTuo,true)){
					number115.getNumbers().add(indexTuo+1);
					number115.getTuo().add(indexTuo+1);
					seColorBack(tuos[indexTuo],"#FFFFFF",R.drawable.dlt_number_back_red);
					isDanHave(indexTuo,false);
				}
			}
			if(number115 == null||number115.getNumbers().size()<NUMLEAST){
				if(number115!=null){
					number115.setNum(0);
				}
				setNumMoney(0);
			}else{
				number115.setPlayType(WANFAINT);
				number115.setMode(DANTUO);
				if(number115.getDan()== null||number115.getDan().size() == 0
						||number115.getTuo()== null||number115.getTuo().size() == 0){
					number115.setNum(0);
					setNumMoney(0);
				}else {
					int time = ChooseUtil.getChooseUtil().get115Time(number115);
					number115.setNum(time);
					setNumMoney(time);
				}
			}
		}
	}
	public void setNumMoney(int time){
		String moneyStr1 = "<html><font color=\"#000000\">"+time+"???"
				+"</font><font color=\"#e73c42\">"+time*2+""
				+ "</font><font color=\"#000000\">???</font></html>";
		money.setText(Html.fromHtml(moneyStr1));
		if(time == 0){
			prizeMoney.setVisibility(View.GONE);
			findViewById(R.id.line2).setVisibility(View.GONE);
		}else{
			prizeMoney.setVisibility(View.VISIBLE);
			findViewById(R.id.line2).setVisibility(View.VISIBLE);
			Spanned prizeMoneyStr = ChooseUtil.getChooseUtil().maymoney(number115);
			prizeMoney.setText(prizeMoneyStr);
		}
	}
	/*******************************popwindow??????************************************/
	public void clearView(){
		for(int i = 0;i<wanfas_tv.length;i++){
			((TextView)viewPop.findViewById(wanfas_tv[i])).setTextColor(Color.parseColor("#666666"));
			((TextView)viewPop.findViewById(wanfas_tv[i])).setBackgroundColor(Color.TRANSPARENT);  
		}
	}
	public void choose(View v){
		if(isMoving){
			return ;
		}
		clearView();
		switch (v.getId()) {
		case R.id.rx1:
			WANFAINT = ConstantsBase.ZHIXUAN1;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx1_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx1_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx2:
			WANFAINT = ConstantsBase.RENXUAN2;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx2_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx2_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q2zux:
			WANFAINT = ConstantsBase.ZUXUAN2;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.q2zux_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q2zux_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q2zx:
			WANFAINT = ConstantsBase.ZHIXUAN2;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.q2zx_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q2zx_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx3:
			WANFAINT = ConstantsBase.RENXUAN3;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx3_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx3_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q3zux:
			WANFAINT = ConstantsBase.ZUXUAN3;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.q3zux_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q3zux_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q3zx:
			WANFAINT = ConstantsBase.ZHIXUAN3;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.q3zx_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q3zx_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx4:
			WANFAINT = ConstantsBase.RENXUAN4;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx4_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx4_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx5:
			WANFAINT = ConstantsBase.RENXUAN5;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx5_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx5_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx6:
			WANFAINT = ConstantsBase.RENXUAN6;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx6_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx6_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx7:
			WANFAINT = ConstantsBase.RENXUAN7;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx7_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx7_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx8:
			WANFAINT = ConstantsBase.RENXUAN8;
			DANTUO = 1;
			((TextView)viewPop.findViewById(R.id.rx8_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx8_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx2dt:
			WANFAINT = ConstantsBase.RENXUAN2;
			DANTUO = 0;
			NUMMOST = 1; 
			((TextView)viewPop.findViewById(R.id.rx2dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx2dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx3dt:
			WANFAINT = ConstantsBase.RENXUAN3;
			DANTUO = 0;
			NUMMOST = 2; 
			((TextView)viewPop.findViewById(R.id.rx3dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx3dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx4dt:
			WANFAINT = ConstantsBase.RENXUAN4;
			DANTUO = 0;
			NUMMOST = 3; 
			((TextView)viewPop.findViewById(R.id.rx4dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx4dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx5dt:
			WANFAINT = ConstantsBase.RENXUAN5;
			DANTUO = 0;
			NUMMOST = 4; 
			((TextView)viewPop.findViewById(R.id.rx5dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx5dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx6dt:
			WANFAINT = ConstantsBase.RENXUAN6;
			DANTUO = 0;
			NUMMOST = 5; 
			((TextView)viewPop.findViewById(R.id.rx6dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx6dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.rx7dt:
			WANFAINT = ConstantsBase.RENXUAN7;
			DANTUO = 0;
			NUMMOST = 6;
			((TextView)viewPop.findViewById(R.id.rx7dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.rx7dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q2zuxdt:
			WANFAINT = ConstantsBase.ZUXUAN2;
			DANTUO = 0;
			NUMMOST = 1;
			((TextView)viewPop.findViewById(R.id.q2zuxdt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q2zuxdt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		case R.id.q3zuxdt:
			WANFAINT = ConstantsBase.ZUXUAN3;
			DANTUO = 0;
			NUMMOST = 2;
			((TextView)viewPop.findViewById(R.id.q3zuxdt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
			((TextView)viewPop.findViewById(R.id.q3zuxdt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			break;
		}
		NUMLEAST =ChooseUtil.chooseNUMLEAST(WANFAINT); 
		tv_head_item.setText(ChooseUtil.choose(WANFAINT,DANTUO));
		clear();
		setNumMoney(0);
		setView();
		if (pop.isShowing()) {
			pop.dismiss();
			load.setVisibility(View.GONE);
		}
	}
	public void  choose(int WANFAINT,int DANTUO){
		clearView();
		switch (WANFAINT) {
		case ConstantsBase.ZHIXUAN1:
			if(DANTUO == 1){
				((TextView)viewPop.findViewById(R.id.rx1_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx1_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.RENXUAN2:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx2dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx2dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx2_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx2_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}break;
		case ConstantsBase.ZUXUAN2:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.q2zuxdt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q2zuxdt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.q2zux_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q2zux_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.ZHIXUAN2:
			if(DANTUO == 1){
				((TextView)viewPop.findViewById(R.id.q2zx_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q2zx_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}
			break;
		case ConstantsBase.RENXUAN3:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx3dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx3dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx3_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx3_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.ZUXUAN3:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.q3zuxdt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q3zuxdt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.q3zux_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q3zux_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.ZHIXUAN3:
			if(DANTUO == 1){
				((TextView)viewPop.findViewById(R.id.q3zx_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.q3zx_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}
			break;
		case ConstantsBase.RENXUAN4:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx4dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx4dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx4_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx4_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.RENXUAN5:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx5dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx5dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx5_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx5_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.RENXUAN6:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx6dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx6dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx6_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx6_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.RENXUAN7:
			if(DANTUO == 0){
				((TextView)viewPop.findViewById(R.id.rx7dt_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx7dt_tv)).setBackgroundResource(R.drawable.cornerfullred);
			}else{
				((TextView)viewPop.findViewById(R.id.rx7_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx7_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		case ConstantsBase.RENXUAN8:
			if(DANTUO == 1){
				((TextView)viewPop.findViewById(R.id.rx8_tv)).setTextColor(Color.parseColor("#FFFFFF"));
				((TextView)viewPop.findViewById(R.id.rx8_tv)).setBackgroundResource(R.drawable.cornerfullred);			
			}
			break;
		};
	}
	public void popwindows() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// ????????????????????????
		viewPop = inflater.inflate(R.layout.main115chooseview, null);
		// ??????PopupWindow??????
		pop = new PopupWindow(viewPop, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		// ???????????????????????????????????????????????????
		pop.setBackgroundDrawable(new BitmapDrawable());
		// ????????????????????????????????????
		pop.setOutsideTouchable(true);
		// ????????????????????????????????????????????????
		pop.setFocusable(true);

		pop.setOnDismissListener(new poponDismissListener());
	}	
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			load.setVisibility(View.GONE);
		}
	}
	/*****************************???????????????******************************/
	public boolean isWanHave(int num){
		if(number115 == null){
			number115 = new Number115();
		}
		if(number115.getWan()!=null&&number115.getWan().contains(num+1)){
			number115.getWan().remove((Object)(num+1));
			number115.getNumbers().remove((Object)(num+1));
			seColorBack(wanweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(number115.getNumbers().size() == 0){
				number115 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isQianHave(int num){
		if(number115 == null){
			number115 = new Number115();
		}
		if(number115.getQian()!=null&&number115.getQian().contains(num+1)){
			number115.getQian().remove((Object)(num+1));
			number115.getNumbers().remove((Object)(num+1));
			seColorBack(qianweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(number115.getNumbers().size() == 0){
				number115 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isBaiHave(int num){
		if(number115 == null){
			number115 = new Number115();
		}
		if(number115.getBai()!=null&&number115.getBai().contains(num+1)){
			number115.getBai().remove((Object)(num+1));
			number115.getNumbers().remove((Object)(num+1));
			seColorBack(baiweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(number115.getNumbers().size() == 0){
				number115 = null;
			}
			return true;
		}
		return false;
	}
	public void jiXuanChoose(){
		clear();
		number115 =  ChooseUtil.getChooseUtil().getRandom115(NUMLEAST,WANFAINT);
		setNumbersChoosed(number115);
		setNumMoney(number115.getNum());
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(DANTUO == 0){
			return ;
		}
		int sensorType = event.sensor.getType();  
		//values[0]:X??????values[1]???Y??????values[2]???Z???    
		float[] values = event.values;  

		if (sensorType == Sensor.TYPE_ACCELEROMETER)  
		{  
			/*?????????????????????????????????????????????9.8~10????????????????????????????????????  
	                                     ?????????????????????????????????????????????????????????   ?????????????????????????????????17?????? 
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
				//????????????????????????OpenDoor??????  
				number115 = 
						ChooseUtil.getChooseUtil().getRandom115(NUMLEAST,WANFAINT);
				setNumMoney(number115.getNum());
				scaleAnimationsI = 0;
				translateAnimations.clear();
				setBigToSmallView();
			}  
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}	
	public void setBigToSmallView(){
		for(int i=0;i<chooseesView.length;i++){
			chooseesView[i].setVisibility(View.GONE);
		}
		for(int i=0;i<number115.getNumbers().size();i++){
			chooseesView[i].setVisibility(View.INVISIBLE);			
			if(number115.getNumbers().get(i)<10){
				choosees[i].setText("0"+number115.getNumbers().get(i)+"");
			}else{
				choosees[i].setText(number115.getNumbers().get(i)+"");
			}
		}
		choosees[number115.getNumbers().size()-1].post(new Runnable() {
			@Override
			public void run() {
				setAnimation();
			}
		});	
	}
	public ScaleAnimation bigToSmall(){
		//?????????
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.1f,0.1f,1.1f,  
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//??????????????????
		scaleAnimation.setDuration(200);		
		return (ScaleAnimation) scaleAnimation;
	}
	public void setAnimation(){
		if(scaleAnimationsI<number115.getNumbers().size()){
			ScaleAnimation scaleAnimation = bigToSmall();
			chooseesView[scaleAnimationsI].startAnimation(scaleAnimation);
			chooseesView[scaleAnimationsI].setVisibility(View.VISIBLE);
			scaleAnimationsI++;	
			if(scaleAnimationsI<number115.getNumbers().size()){
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						setAnimation();
					}
				}, 100);
			}else{
				setAnimation();
			}
		}else{		
			setToPlace();	
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					for(int i = 0;i<translateAnimations.size();i++){
						chooseesView[i].startAnimation(translateAnimations.get(i));
					}

				}
			}, 300);

		}			
	}
	public void setToPlace(){
		for(int i=0;i<number115.getNumbers().size();i++){
			TextView viewTextView = null;
			if(WANFAINT == ConstantsBase.ZHIXUAN2
					||WANFAINT == ConstantsBase.ZHIXUAN3){
				if(i == 0){
					viewTextView = (TextView) findViewById(wanweis[number115.getNumbers().get(i)-1]);
				}
				if(i == 1){
					viewTextView = (TextView) findViewById(qianweis[number115.getNumbers().get(i)-1]);
				}
				if(WANFAINT == ConstantsBase.ZHIXUAN3){
					if(i == 2){
						viewTextView = (TextView) findViewById(baiweis[number115.getNumbers().get(i)-1]);
					}

				}
			}else{
				viewTextView = (TextView) findViewById(wanweis[number115.getNumbers().get(i)-1]);
			}
			// TODO Auto-generated method stub

			int[] locationFrom  = new int[2];  
			chooseesView[i].getLocationOnScreen(locationFrom);  

			int leftFrom = locationFrom[0];  
			int topFrom = locationFrom[1]; 
			int[] locationTo  = new int[2];  
			viewTextView.getLocationOnScreen(locationTo);  
			int leftTo = locationTo[0];  
			int topTo = locationTo[1]; 
			translateAnimations.add(
					toPlace(i,number115.getNumbers().size(),viewTextView,leftFrom,topFrom,leftTo,topTo));

		}
	}	
	public TranslateAnimation toPlace(final int index,final int length,final TextView viewToTextView,
			float leftFrom,float topFrom,float leftTo,float topTo){   
		TranslateAnimation animation = new TranslateAnimation(0,leftTo-leftFrom,0,topTo-topFrom);
		animation.setDuration(500);
		animation.setFillAfter(true);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {

				for(int i=0;i<chooseesView.length;i++){
					chooseesView[index].clearAnimation();
					chooseesView[index].setVisibility(View.INVISIBLE);
				}
				viewToTextView.setTextColor(Color.parseColor("#FFFFFF"));
				viewToTextView.setBackgroundResource(R.drawable.dlt_number_back_red);

				if(index == length-1){
					isMoving = false;
				}
			}
		});
		return animation;
	}
	/*****************************????????????******************************/
	public boolean isDanHave(int num,boolean isToast){
		if(number115 == null){
			number115 = new Number115();
		}
		if(number115.getDan()!=null&&number115.getDan().contains(num+1)){
			number115.getDan().remove((Object)(num+1));
			number115.getNumbers().remove((Object)(num+1));
			seColorBack(dans[num],"#E73C42",R.drawable.dlt_number_back);
			if(number115.getNumbers().size() == 0){
				number115 = null;
			}
			return true;
		}else{
			if(number115.getDan().size() == NUMMOST&&isToast){
				ToastUtils.showShort(_this, "???????????????????????????"+NUMMOST+"???");
				return true;
			}
		}
		return false;
	}
	public boolean isTuoHave(int num,boolean isToast){
		if(number115 == null){
			number115 = new Number115();
		}
		if(number115.getTuo()!=null&&number115.getTuo().contains(num+1)){
			number115.getTuo().remove((Object)(num+1));
			number115.getNumbers().remove((Object)(num+1));
			seColorBack(tuos[num],"#E73C42",R.drawable.dlt_number_back);
			if(number115.getNumbers().size() == 0){
				number115 = null;
			}
			return true;
		}else{
			if(number115.getTuo().size() == 10&&isToast){
				ToastUtils.showShort(_this, "???????????????????????????10???");
				return true;
			}
		}
		return false;
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
		title.setText("??????");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("????????????????????????????????? \n???????????????????????????????????????");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("??????");
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
		ok.setText("??????");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				number115.setPlayType(WANFAINT);
				number115.setPlayType(WANFAINT);
				Intent intent = new Intent();  
				Bundle bundle = new Bundle();   
				bundle.putInt("numleast",NUMLEAST);
				bundle.putInt("position", position);
				bundle.putBoolean("clear", true);
				bundle.putSerializable("number115", number115);
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
		title.setText("??????");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText(qici+"???????????????????????????????????????");
		updatesize.setTextSize(18);
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setVisibility(View.GONE);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("??????");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		if(alertDialog!=null){
			alertDialog.cancel();
			alertDialog = null;
		}
		curtain.clearTime();
		idStop = true;
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}
}
