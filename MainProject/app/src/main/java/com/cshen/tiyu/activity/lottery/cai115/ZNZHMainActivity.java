package com.cshen.tiyu.activity.lottery.cai115;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.cai115.ZNZHItem;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.MyCountTime2;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class ZNZHMainActivity extends BaseActivity{
	public static final int HAVEPAY = 0;
	ZNZHMainActivity _this;
	private TopViewLeft tv_head;
	private TextView qishu,time,realtime;
	private LinearLayout qishuview;
	private CheckBox moreMon;//条款，追加
	Period period;//期数
	String periodTime;
	MyCountTime2 mMyCountTime,mMyCountTimeReal;
	boolean show = false;

	private AlertDialog  alertDialog,alertDialogback,endAlertDialog;
	private EditText realLong,realTime;
	private TextView allmoney,allyll;
	private ImageView ylmoneyIm ,yllIm;
	private EditText ylled,ylmoneyed;
	private TextView znzhPay;
	private boolean chooseyll = true,chooseyllOld = true;
	private int longIntOld = 10,timeIntOld = 1,yllIntOld = 50,ylmoneyIntOld = 100;
	private int longInt = 10,timeInt = 1,yllInt = 50,ylmoneyInt = 100; 
	ArrayList<ZNZHItem> znzhitems = new ArrayList<>();
	ArrayList<View> znzhviews = new ArrayList<>();
	int allmoneyInt = 0;
	int changNumber = -1;
	String beis ;
	private boolean isMore = true;//中奖停止追号

	private int wanfa = 0;
	private int jiangjin = 0;
	Ticket ticket;
	private ArrayList<Ticket> tickets = new ArrayList<Ticket>();// 投注内容     


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.znzh);
		_this = this;
		tv_head=(TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true,false, true);
		tv_head.contactTextView("修改方案");
		tv_head.contactTextViewSize();
		tv_head.contactTextViewBackground();
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override   
			public void clickLoginView(View view) {		
			}
			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根
				dialog();
			}
			@Override
			public void clickBackImage(View view) {		
				dialogBack();
			}
		});
		initView();
		initDate();
	}
	private void initView() {
		qishu = (TextView) findViewById(R.id.qishu);
		time = (TextView) findViewById(R.id.time);
		realtime = (TextView) findViewById(R.id.realtime);
		allmoney = (TextView) findViewById(R.id.allmoney);
		allyll = (TextView) findViewById(R.id.allyll);
		moreMon =  (CheckBox) findViewById(R.id.realmore);
		moreMon.setOnCheckedChangeListener(new OnCheckedChangeListener(){  
			@Override  
			public void onCheckedChanged(CompoundButton button, boolean isChecked){  
				if(isChecked){  
					isMore = true;
				} else{  
					isMore = false;
				}
			}  
		}); 
		time.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if("已截止".equals(s.toString())){
					show = false;	
				}else{
					show = true;
				}
			}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {
				if("已截止".equals(s.toString())
						&&show){			
					show = false;
					showStopDialog(periodTime);
				}				
			}  
		}); 
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
		qishuview = (LinearLayout) findViewById(R.id.qishuview);
		znzhPay =  (TextView) findViewById(R.id.znzh_pay);
		znzhPay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkPeriodNumber();
			}
		});
	}
	private void checkPeriodNumber(){
		// TODO 自动生成的方法存根
		ServiceUser.getInstance().PostPeriod(_this,wanfa+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){						
						Long time = period.getInterval();
						if(time <= 0){
							ToastUtils.showShort(_this,"本期已截止");
						}else{
							toPayMoney();
						}

					}
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	private void  toPayMoney(){

		// TODO Auto-generated method stub
		StringBuffer beiB = new StringBuffer();
		if(znzhitems!=null&&znzhitems.size()>0){
			beiB.append("[");
			for(ZNZHItem item:znzhitems){
				beiB.append(item.getBeishu()+",");
			}
			String bei = beiB.toString();
			if(znzhitems.size()>1){
				ticket.setIsChase("true");
				ticket.setPeriodSizeOfChase(znzhitems.size()+"");
				ticket.setTotalCostOfChase(allmoneyInt+"");
			}else{
				ticket.setIsChase("false");
				ticket.setPeriodSizeOfChase("");
				ticket.setTotalCostOfChase("");
			}
			if(ConstantsBase.SD115 == wanfa){		
				ticket.setPeriodNumber(PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIOD115));
			}
			if(ConstantsBase.GD115 == wanfa){
				ticket.setPeriodNumber(PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIOD115GD));								
			}
			ticket.setMultiplesOfChase((TextUtils.isEmpty(bei)?
					"":bei.substring(0, bei.length()-1))+"]");
		}
		ticket.setWonStopOfChase(isMore+"");
		tickets.add(ticket);
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",wanfa);
		bundle.putLong("totalaccount",allmoneyInt);
		if(znzhitems.size() == 1){
			bundle.putBoolean("useRedPacket",true);
		}else{
			bundle.putBoolean("useRedPacket",false);
		}
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);

	}
	private void  initDate(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			wanfa = bundle.getInt("wanfa",0);
			jiangjin = bundle.getInt("jiangjin",0);
			ticket = (Ticket) bundle.getSerializable("ticketarray");
		}
		setTextViewValue();
	}
	public void setViews(){
		if(chooseyll){
			allyll.setText("计划盈利率"+yllIntOld+"%");
		}else{
			allyll.setText("计划盈利金额"+ylmoneyIntOld+"元");
		}

		ZNZHItem itemF = new ZNZHItem();
		if(chooseyll){
			if(((jiangjin-2)*100)/2<yllInt){
				ToastUtils.showShort(_this, "盈利率设置过大，无法计算方案，请重新设置");	
				if(alertDialog!=null){
					alertDialog.show();
				}
				return ;
			}
			itemF.setBeishu(1*timeInt);
			itemF.setTouru(2*itemF.getBeishu());
			itemF.setYingli((jiangjin-2)*itemF.getBeishu());
			itemF.setYinglilv(((jiangjin-2)*100)/2f);
		}else{
			int  beishuFirst = ZNZHUtils.getZNZHUtils().getBeishuYLMoney(0, jiangjin,ylmoneyInt);
			if(beishuFirst>999){
				ToastUtils.showShort(_this, "按您的方案，投注倍数已超过上限999,请修改方案");	
				if(alertDialog!=null){
					alertDialog.show();
				}
				return ;
			}
			if(beishuFirst<timeInt){
				beishuFirst = timeInt;
			}
			itemF.setBeishu(beishuFirst);
			itemF.setTouru(2*itemF.getBeishu());
			itemF.setYingli((jiangjin-2)*itemF.getBeishu());
			itemF.setYinglilv(((jiangjin-2)*100)/2f);
		}
		qishuview.removeAllViews();
		znzhviews.clear();
		znzhitems.clear();

		znzhitems.add(itemF);
		for(int i=1;i<longInt;i++){
			int beishuInt = 0;
			if(chooseyll){
				beishuInt = ZNZHUtils.getZNZHUtils().getBeishuYLL(
						znzhitems.get(i-1).getTouru(), jiangjin, getYuShu(yllInt,100));
			}else{
				beishuInt = ZNZHUtils.getZNZHUtils().getBeishuYLMoney(
						znzhitems.get(i-1).getTouru(), jiangjin,ylmoneyInt);
			}
			if(beishuInt<timeInt){
				beishuInt = timeInt;
			}
			if(beishuInt>999){
				ToastUtils.showShort(_this, "投注倍数已超过上限999，超过部分将不再计算");
				break;
			}
			int touruInt = (i==0?0:znzhitems.get(i-1).getTouru())+
					2*beishuInt;
			int yingliInt = jiangjin*beishuInt-touruInt;

			ZNZHItem item = new ZNZHItem();
			item.setBeishu(beishuInt);
			item.setTouru(touruInt);
			item.setYingli(yingliInt);
			float yllD = (float)(yingliInt*100)/touruInt;
			item.setYinglilv(yllD);
			znzhitems.add(item);
		}
		for(int i=0;i<znzhitems.size();i++){
			ZNZHItem item = znzhitems.get(i);
			final int number = i;
			View convertView = View.inflate(_this, R.layout.znzh_item,null);
			final EditText beishu = (EditText) convertView.findViewById(R.id.beishu);
			beishu.setText(""+item.getBeishu());
			beishu.setOnFocusChangeListener(new android.view.View.
					OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {  
					if (hasFocus) {		
						beishu.setCursorVisible(true);
						changNumber = number;
					}else {
						if(TextUtils.isEmpty(beishu.getText().toString())){
							beishu.setText("1");
						}else{
							int beishuInt = Integer.parseInt(beishu.getText().toString());
							try{
								if(beishuInt == 0){
									beishu.setText("1");
								}
							}catch(Exception e){
								e.printStackTrace();
								beishu.setText("1");
							}
						}
						beishu.setCursorVisible(false);
						changNumber = -1;
					}
				}
			});
			beishu.addTextChangedListener(new TextWatcher() {  
				@Override  
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

				@Override  
				public void onTextChanged(CharSequence s, int start, int before, int count) {}  

				@SuppressLint("NewApi")
				@Override  
				public void afterTextChanged(Editable s) { 
					if(changNumber == number ){
						minusaddEach(-1,number,beishu);
					}
				}  
			});
			ImageView minustime = (ImageView) convertView.findViewById(R.id.minustime);
			ImageView addtime = (ImageView) convertView.findViewById(R.id.addtime);
			minustime.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					minusaddEach(0,number,beishu);
				}
			});addtime.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					minusaddEach(1,number,beishu);
				}
			});
			TextView touru = (TextView) convertView.findViewById(R.id.touru);
			touru.setText(""+item.getTouru());
			TextView yingli = (TextView) convertView.findViewById(R.id.yingli);
			yingli.setText(""+item.getYingli());
			if(item.getYingli()<0){
				yingli.setTextColor(Color.parseColor("#098409"));
			}else{
				yingli.setTextColor(getResources().getColor(R.color.mainred));
			}
			TextView yinglilv = (TextView) convertView.findViewById(R.id.yinglilv);
			DecimalFormat df = new DecimalFormat("0.00");
			yinglilv.setText(df.format(item.getYinglilv())+"%");
			if(item.getYinglilv()<0){
				yinglilv.setTextColor(Color.parseColor("#098409"));
			}else{
				yinglilv.setTextColor(getResources().getColor(R.color.mainred));
			}
			if(i%2 == 0){
				convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
			}else{
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			znzhviews.add(convertView);
			qishuview.addView(convertView);
		}
		if(znzhitems.size() > 0){
			allmoneyInt = znzhitems.get(znzhitems.size()-1).getTouru();
			if(chooseyll){
				allyll.setText("计划盈利率"+yllInt+"%");
			}else{
				allyll.setText("计划盈利金额"+ylmoneyInt+"元");
			}
		}else{
			ToastUtils.showShort(_this, "按您的方案，投注倍数已超过上限999,请修改方案");	
			if(alertDialog!=null){
				alertDialog.show();
			}
		}
	}
	public void updateViews(int start,int changeBeishu){
		for(int i=start ;i<znzhviews.size();i++){
			int beishuInt = changeBeishu;
			if(i==0){
				znzhitems.get(i).setBeishu(beishuInt);
				znzhitems.get(i).setTouru(2*changeBeishu);
				znzhitems.get(i).setYingli((jiangjin-2)*changeBeishu);
				znzhitems.get(i).setYinglilv(((jiangjin-2)*100)/2f);
			}else{
				if(i != start){
					beishuInt = znzhitems.get(i).getBeishu();
				}
				int touruInt = znzhitems.get(i-1).getTouru()+
						2*beishuInt;
				int yingliInt = jiangjin*beishuInt-touruInt;

				znzhitems.get(i).setBeishu(beishuInt);
				znzhitems.get(i).setTouru(touruInt);
				znzhitems.get(i).setYingli(yingliInt);
				DecimalFormat df = new DecimalFormat("0.00");
				float yllD = (float)(yingliInt*100)/touruInt;
				znzhitems.get(i).setYinglilv(Float.parseFloat(df.format(yllD)));
			}
			View convertView = znzhviews.get(i);//View.inflate(_this, R.layout.znzh_item,null);
			final EditText beishu = (EditText) convertView.findViewById(R.id.beishu);			
			if(i != start){
				beishu.setText(""+znzhitems.get(i).getBeishu());
			}

			TextView touru = (TextView) convertView.findViewById(R.id.touru);
			touru.setText(""+znzhitems.get(i).getTouru());
			TextView yingli = (TextView) convertView.findViewById(R.id.yingli);
			yingli.setText(""+znzhitems.get(i).getYingli());
			if(znzhitems.get(i).getYingli()<0){
				yingli.setTextColor(Color.parseColor("#098409"));
			}else{
				yingli.setTextColor(getResources().getColor(R.color.mainred));
			}
			TextView yinglilv = (TextView) convertView.findViewById(R.id.yinglilv);
			DecimalFormat df = new DecimalFormat("0.00");
			yinglilv.setText(df.format(znzhitems.get(i).getYinglilv())+"%");
			if(znzhitems.get(i).getYinglilv()<0){
				yinglilv.setTextColor(Color.parseColor("#098409"));
			}else{
				yinglilv.setTextColor(getResources().getColor(R.color.mainred));
			}
			if(i%2 == 0){
				convertView.setBackgroundColor(Color.parseColor("#F6F6F6"));
			}else{
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}
		allmoneyInt = znzhitems.get(znzhitems.size()-1).getTouru();
		String moneyStr = "<html>"
				+ "<font color=\"#000000\">"+znzhitems.size()+"期</font><font color=\"#FF3232\">"+allmoneyInt
				+ "</font><font color=\"#000000\">元"
				+ "</font></html>";
		allmoney.setText(Html.fromHtml(moneyStr));
	}
	public void updateQiHaoViews(){
		for(int i=0;i<znzhviews.size();i++){
			View convertView = znzhviews.get(i);
			final TextView qishu = (TextView) convertView.findViewById(R.id.qishu);
			if(i == 0){
				try{
					znzhitems.get(i).setQihao(Integer.parseInt(periodTime));
				}catch(Exception e){
					e.printStackTrace();
					znzhitems.get(i).setQihao(1);
				}
			}else{
				if(znzhitems.get(i-1).getQihao() == 78){
					znzhitems.get(i).setQihao(1);
				}else{
					znzhitems.get(i).setQihao(znzhitems.get(i-1).getQihao()+1);
				}
			}
			qishu.setText(""+znzhitems.get(i).getQihao());
		}
	}
	public double getYuShu(int chushu,int beichushu){
		BigDecimal b1 = new BigDecimal(Integer.toString(chushu));
		BigDecimal b2 = new BigDecimal(Integer.toString(beichushu));
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public void clickRealTimeOver() {
		// TODO Auto-generated method stub
		qiShu();
	}
	public void qiShu(){
		ServiceUser.getInstance().PostPeriod(_this,wanfa+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						periodTime = period.getPeriodNumber();
						if(ConstantsBase.SD115 == wanfa){
							PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115,periodTime);								
						}
						if(ConstantsBase.GD115 == wanfa){
							PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115GD,periodTime);								
						}
						if(periodTime.length() >2){
							periodTime = periodTime.substring(periodTime .length()-2,periodTime.length());     					
						}				
						Long time = period.getInterval();
						Long realtime = period.getRealInterval();
						openTime(periodTime,time,realtime);
						updateQiHaoViews();
					}				
				}	
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);	
				updateQiHaoViews();
			}
		});
	}
	public void openTime(String qishuStr,Long timeL,Long realtimeL) {
		qishu.setText("距第"+qishuStr+"期投注截止:");
		if(timeL<=0l){
			timeL = 0l;
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
	@Override  
	public void onResume()  
	{  
		super.onResume();
		qiShu();
	}
	@Override  
	public void onPause()  
	{  
		super.onPause();
		clearTime();
	}

	public void dialog() {
		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(_this).create();
		}
		alertDialog.setCancelable(false);
		alertDialog.show();
		timeIntOld  = timeInt;
		longIntOld  = longInt;
		yllIntOld = yllInt;
		ylmoneyIntOld = ylmoneyInt;
		chooseyll = chooseyllOld;

		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.znzhdialog);
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		ImageView minuslong = (ImageView) window.findViewById(R.id.minuslong);
		ImageView addlong = (ImageView) window.findViewById(R.id.addlong);
		minuslong.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				minusaddLong(0);
			}
		});addlong.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				minusaddLong(1);
			}
		});
		realLong = (EditText) window.findViewById(R.id.reallong);
		realLong.setText(longIntOld+"");
		realLong.addTextChangedListener(new TextWatcher() {  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) { 
				minusaddLong(-1);
			}  
		});
		ImageView minustime = (ImageView) window.findViewById(R.id.minustime);
		ImageView addtime = (ImageView) window.findViewById(R.id.addtime);
		minustime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				minusaddTime(0);
			}
		});addtime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				minusaddTime(1);
			}
		});
		realTime = (EditText) window.findViewById(R.id.realtime);
		realTime.setText(timeIntOld+"");
		realTime.addTextChangedListener(new TextWatcher() {  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) { 
				minusaddTime(-1);
			}  
		});
		View yllview  = window.findViewById(R.id.yllview);
		yllIm = (ImageView) window.findViewById(R.id.yllim);
		yllview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chooseyll = true;
				yllIm.setImageResource(R.mipmap.znzhcheck);
				ylmoneyIm.setImageResource(R.mipmap.znzhcheckno);
				ylmoneyed.setTextColor(Color.parseColor("#a0a0a0"));
				ylmoneyed.setEnabled(false);
				ylled.setTextColor(Color.parseColor("#000000"));
				ylled.setEnabled(true);
			}
		});
		ylled = (EditText) window.findViewById(R.id.yll);
		ylled.setText(yllIntOld+"");
		View ylmoneyview  = window.findViewById(R.id.ylmoneyview);
		ylmoneyIm = (ImageView) window.findViewById(R.id.ylmoneyim);
		ylmoneyview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chooseyll = false;
				yllIm.setImageResource(R.mipmap.znzhcheckno);
				ylmoneyIm.setImageResource(R.mipmap.znzhcheck);
				ylmoneyed.setTextColor(Color.parseColor("#000000"));
				ylmoneyed.setEnabled(true);
				ylled.setTextColor(Color.parseColor("#a0a0a0"));
				ylled.setEnabled(false);
			}
		});
		ylmoneyed = (EditText) window.findViewById(R.id.ylmoney);
		ylmoneyed.setText(ylmoneyIntOld+"");
		if(chooseyll){
			yllIm.setImageResource(R.mipmap.znzhcheck);
			ylmoneyIm.setImageResource(R.mipmap.znzhcheckno);
			ylmoneyed.setTextColor(Color.parseColor("#a0a0a0"));
			ylmoneyed.setEnabled(false);
			ylled.setTextColor(Color.parseColor("#000000"));
			ylled.setEnabled(true);
		}else{	
			yllIm.setImageResource(R.mipmap.znzhcheckno);
			ylmoneyIm.setImageResource(R.mipmap.znzhcheck);
			ylmoneyed.setTextColor(Color.parseColor("#000000"));
			ylmoneyed.setEnabled(true);
			ylled.setTextColor(Color.parseColor("#a0a0a0"));
			ylled.setEnabled(false);
		}

		TextView ok = (TextView) window.findViewById(R.id.ok);
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				timeInt = timeIntOld ;
				longInt = longIntOld ;
				yllInt = yllIntOld ;
				ylmoneyInt = ylmoneyIntOld;
				chooseyll = chooseyllOld;
				closeKeyboard();
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					yllInt = Integer.parseInt(ylled.getText().toString());	
				}catch(Exception e){
					yllInt = 50;
					e.printStackTrace();
				}
				try{
					ylmoneyInt = Integer.parseInt(ylmoneyed.getText().toString());	
				}catch(Exception e){
					ylmoneyInt = 50;
					e.printStackTrace();
				}
				if(chooseyll){
					if(yllInt == 0){
						ToastUtils.showShort(_this, "盈利率不能为0");
						return;
					}
				}else{
					if(ylmoneyInt == 0){
						ToastUtils.showShort(_this, "盈利金额不能为0");
						return;
					}
				}
				alertDialog.dismiss();
				alertDialog.cancel();

				setTextViewValue();
				updateQiHaoViews();
				closeKeyboard();
				longIntOld  = longInt;
				yllIntOld = yllInt ;
				ylmoneyIntOld = ylmoneyInt;
				timeIntOld  = timeInt;
				chooseyllOld = chooseyll;
			}
		});
	}
	public void closeKeyboard(){
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}
	public void dialogBack() {
		if(alertDialogback == null){
			alertDialogback = new AlertDialog.Builder(_this).create();
		}		
		alertDialogback.setCancelable(false);
		alertDialogback.show();
		Window window = alertDialogback.getWindow();
		window.setContentView(R.layout.dialog);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("温馨提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("退出后当前所有方案信息将不会被保存");
		TextView ok = (TextView) window.findViewById(R.id.ok);
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		ok.setText("确定");
		cancle.setText("取消");
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialogback.dismiss();
				alertDialogback.cancel();
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialogback.dismiss();
				alertDialogback.cancel();
				_this.finish();
			}
		});
	}
	public void showStopDialog(String qici) {
		if(endAlertDialog == null){
			endAlertDialog  = new AlertDialog.Builder(this).create();
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
	public void minusaddLong(int type){
		String longStr = realLong.getText().toString();
		try{
			longInt = Integer.parseInt(longStr);
			if(type == 0){//0minus
				if(longInt == 1){
					ToastUtils.showShort(_this, "追期不能为0");
					realLong.setText("1");
				}else{
					longInt--;
					realLong.setText(longInt+"");
				}
			}if(type == 1){//0add
				if(longInt>154){
					ToastUtils.showShort(_this, "追期不能超过154期");
					realLong.setText("154");
				}else{
					longInt++;
					realLong.setText(longInt+"");
				}
			}if(type == -1){
				if(longInt  == 0){
					longInt = 1;
					ToastUtils.showShort(_this, "追期不能为0");
					realLong.setText("1");
				}
				if(longInt>154){
					ToastUtils.showShort(_this, "追期不能超过154期");
					realLong.setText("154");
				}
			}
		}catch(Exception e){
			longInt = 1;
			e.printStackTrace();
		}
	}
	public void minusaddTime(int type){
		String timeStr = realTime.getText().toString();
		try{
			timeInt = Integer.parseInt(timeStr);
			if(type == 0){//0minus
				if(timeInt == 1){
					ToastUtils.showShort(_this, "投注倍数不能为0");
					realTime.setText("1");
				}else{
					timeInt--;
					realTime.setText(timeInt+"");
				}
			}
			if(type == 1){//0add
				if(timeInt < 999){
					timeInt++;
					realTime.setText(timeInt+"");
				}
			}
			if(type == -1){
				if(timeInt  == 0){
					timeInt = 1;
					ToastUtils.showShort(_this, "投注倍数不能为0");
					realTime.setText("1");
				}
			}
		}catch(Exception e){
			timeInt = 1; 
			e.printStackTrace();
		}
	}
	public void setTextViewValue(){
		setViews();
		String moneyStr = "<html>"
				+ "<font color=\"#000000\">"+znzhitems.size()+"期</font><font color=\"#e73c42\">"+allmoneyInt
				+ "</font><font color=\"#000000\">元"
				+ "</font></html>";
		allmoney.setText(Html.fromHtml(moneyStr));

	}
	public void minusaddEach(int type,int i,EditText beishu){
		String timeStr = beishu.getText().toString();
		if(type == -1){
			if(TextUtils.isEmpty(timeStr)){
				return;
			}
		}

		int timeInt =1;
		try{
			timeInt = Integer.parseInt(timeStr);
			if(type == -1){//0minus
				if(timeInt == 0){
					timeInt = 1;
				}
			}
			if(type == 0){//0minus
				if(timeInt == 1){
					ToastUtils.showShort(_this, "投注倍数不能为0");
					beishu.setText("1");
				}else{
					timeInt--;
					beishu.setText(timeInt+"");
				}
			}if(type == 1){//0add
				if(timeInt < 999){
					timeInt++;
					beishu.setText(timeInt+"");
				}
			}
		}catch(Exception e){
			timeInt = 1;
			beishu.setText(timeInt+"");
			e.printStackTrace();
		}
		updateViews(i,timeInt);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case HAVEPAY:	
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				if (!isOnlyClose) {
					Intent intent = new Intent();
					intent.putExtra("isOnlyClose", false);

					boolean closeMainActivity = data.getBooleanExtra(
							"closeMainActivity", false);
					intent.putExtra("closeMainActivity", closeMainActivity);

					setResult(RESULT_OK, intent);
					_this.finish();
				}
				break;			
			default:
				break;
			}
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			dialogBack();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		if(alertDialog!=null){
			alertDialog.cancel();
			alertDialog = null;
		}if(alertDialogback!=null){
			alertDialogback.cancel();
			alertDialogback = null;
		}if(endAlertDialog!=null){
			endAlertDialog.cancel();
			endAlertDialog = null;
		}
		clearTime();
	}
}

