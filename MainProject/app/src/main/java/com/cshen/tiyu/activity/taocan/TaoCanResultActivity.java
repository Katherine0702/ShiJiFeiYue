package com.cshen.tiyu.activity.taocan;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.domain.taocan.TaoCan;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.MyCountTime2;
import com.cshen.tiyu.widget.PlayingTaoCanView;

public class TaoCanResultActivity extends BaseActivity{
	public static final int HAVEPAY = 0;
	public static final int LOGIN = 3;//登录
	TaoCanResultActivity _this;
	TaoCan taocan;
	View iv_back;
	ImageView icon;
	TextView playType,qishu,time,realtime,title,content,pay;
	com.cshen.tiyu.widget.PlayingTaoCanView play;
	MyCountTime2 mMyCountTime,mMyCountTimeReal;
	String  wanfa = "";
	Period period;//期数
	int PeriodNumber;//追期
	private AlertDialog  alertDialog ;
	boolean show = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taocanresult);
		_this = this;
		initView();
		initDate();
	}
	private void initView(){
		iv_back  = findViewById(R.id.iv_back);
		icon = (ImageView) findViewById(R.id.icon);
		qishu = (TextView) findViewById(R.id.qishu);
		time = (TextView) findViewById(R.id.time);
		realtime = (TextView) findViewById(R.id.realtime);
		title = (TextView) findViewById(R.id.title);
		playType = (TextView) findViewById(R.id.playtype);
		content = (TextView) findViewById(R.id.content);
		play = (PlayingTaoCanView) findViewById(R.id.play);
		pay = (TextView) findViewById(R.id.pay);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
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
					clickRealTimeOver(time);
				}
			}  
		});  
	}
	private void  initDate(){
		Bundle b = getIntent().getExtras();
		taocan = (TaoCan) b.getSerializable("taocan");
		setValue();
	}
	private void setValue(){
		switch(taocan.getLottery()){
		case ConstantsBase.SD115:
			wanfa = ConstantsBase.SD115+"";
			break;
		case ConstantsBase.GD115:
			wanfa = ConstantsBase.GD115+"";
			break;
		case ConstantsBase.DLT:
			wanfa = ConstantsBase.DLT+"";
			break;
		case ConstantsBase.SSQ:
			wanfa = ConstantsBase.SSQ+"";
			break;
		}
		qiShu(); 
		int num = 7;
		if((ConstantsBase.SD115+"").equals(wanfa)||
				(ConstantsBase.GD115+"").equals(wanfa)){

			switch(taocan.getPlayType()){
			case ConstantsBase.ZHIXUAN1:
				num = 1;
				playType.setText("前一直选");
				break;
			case ConstantsBase.RENXUAN2:
				num = 2;
				playType.setText("任选二");
				break;
			case ConstantsBase.ZHIXUAN2:
				num = 2;
				playType.setText("前二直选");
				break;
			case ConstantsBase.ZUXUAN2:
				num = 2;
				playType.setText("前二组选");
				break;
			case ConstantsBase.RENXUAN3:
				num = 3;
				playType.setText("任选三");
				break;

			case ConstantsBase.ZHIXUAN3:
				num = 3;
				playType.setText("前三直选");
				break;
			case ConstantsBase.ZUXUAN3:
				num = 3;
				playType.setText("前三组选");
				break;
			case ConstantsBase.RENXUAN4:
				num = 4;
				playType.setText("任选四");
				break;
			case ConstantsBase.RENXUAN5:
				num = 5;
				playType.setText("任选五");
				break;
			case ConstantsBase.RENXUAN6:
				num = 6;
				playType.setText("任选六");
				break;
			case ConstantsBase.RENXUAN7:
				num = 7;
				playType.setText("任选七 ");
				break;
			case ConstantsBase.RENXUAN8:
				num = 8;
				playType.setText("任选八");
				break;
			}
			playType.setVisibility(View.VISIBLE);
			findViewById(R.id.jiezhiview).setVisibility(View.VISIBLE);
		}else{
			playType.setVisibility(View.GONE);
			findViewById(R.id.jiezhiview).setVisibility(View.GONE);
		}
		play.setView(num,taocan.getPlayType(),wanfa);


		xUtilsImageUtils.display(icon, taocan.getImageURL(), R.mipmap.ssq);

		title.setText(taocan.getTitle());
		PeriodNumber = taocan.getPeriodNumber();
		String numberStr = "<html><font color=\"#84744b\">追"+PeriodNumber+"期，不中即返"
				+"</font><font color=\"#ee423a\">"+taocan.getBackMoney()+"元</font></html>";
		content.setText(Html.fromHtml(numberStr));
		pay.setText("立即付款"+PeriodNumber*2+"元");
		pay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean hasLongin = PreferenceUtil.getBoolean(_this,
						"hasLogin");
				if (hasLongin) {
					switch(wanfa){
					case ConstantsBase.SD115+"":
						pay115(ConstantsBase.SD115); 
					break;
					case ConstantsBase.GD115+"":
						pay115(ConstantsBase.GD115); 
					break;
					/*case ConstantsBase.Fast3+"":
						pay115(ConstantsBase.Fast3); 
					break;*/
					case ConstantsBase.DLT+"":
						payDlt(); 
					break;
					case ConstantsBase.SSQ+"":
						paySsq(); 
					break;
					}
				} else {
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					startActivityForResult(intentLogin,LOGIN);
				}
			}
		});
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
	public void qiShu(){
		ServiceUser.getInstance().PostPeriod(_this,wanfa, new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						String periodTime = period.getPeriodNumber();
						if(periodTime.length() >2){
							periodTime = periodTime.substring(periodTime .length()-2,periodTime.length());     					
						}
						Long time = period.getInterval();
						Long realtime = period.getRealInterval();
						openTime(periodTime,time,realtime);
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
	public void openTime(String qishuStr,Long timeL,Long realtimeL) {
		qishu.setText("距第"+qishuStr+"期");
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
	public void clickRealTimeOver(View view) {
		// TODO Auto-generated method stub
		qiShu();
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
	public void pay115(int wanfaInt){
		User user = MyDbUtils.getCurrentUser();
		ArrayList<Ticket> tickets = new ArrayList<>();

		Number115 eachNumber=play.getChoosedNumber115();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());
		t.setMode("0");
		/*if(eachNumber.getNum()>1
				||eachNumber.getPlayType() == WANFA.ZHIXUAN1){
			t.setMode("0");
		}else{
			t.setMode("1");
		}*/
		t.setPeriodNumber(period.getPeriodNumber());
		t.setWonStopOfChase("false");
		t.setIsSpecialFlag("false");
		t.setPlayTypeID(eachNumber.getPlayType()+"");
		t.setMultiple("1");

		t.setSumNum(eachNumber.getNum()+"");
		t.setTotalAmount(eachNumber.getNum()*2*1+"");
		t.setHongBaoId("");
		t.setIsChase("true");
		t.setPeriodSizeOfChase(PeriodNumber+"");
		t.setTotalCostOfChase(eachNumber.getNum()*2*1*PeriodNumber+""+"");


		StringBuffer numberString = new StringBuffer();
		StringBuffer number = new StringBuffer();
		String numbers = "";
		if(eachNumber.getMode() == 0){//鑳嗘嫋
			for(Integer i:eachNumber.getDan()){
				number.append(","+i);
			}
			number.append(";");
			for(Integer i:eachNumber.getTuo()){
				number.append(i+",");
			}
			numbers = number.toString().substring(1,number.toString().length()-1);
		}else{
			if(eachNumber.getPlayType()!=ConstantsBase.ZHIXUAN2&&
					eachNumber.getPlayType()!=ConstantsBase.ZHIXUAN3){
				for(Integer i:eachNumber.getNumbers()){
					number.append(i+",");
				}
				numbers = number.toString().substring(0,number.toString().length()-1);	
			}else{
				for(Integer i:eachNumber.getWan()){
					number.append(","+i);
				}
				number.append("-");
				for(Integer i:eachNumber.getQian()){
					number.append(i+",");
				}
				if(number.toString().length()>1){
					numbers = number.toString().substring(1,number.toString().length()-1);
				}

				if(eachNumber.getPlayType()==ConstantsBase.ZHIXUAN3){
					numbers = number.toString().substring(1,number.toString().length()-1);
					number.delete(0,number.toString().length());
					number.append(numbers);
					number.append("-");
					for(Integer i:eachNumber.getBai()){
						number.append(i+",");
					}
					numbers = number.toString().substring(0,number.toString().length()-1);
				}
			}
		}	
		numberString.append("+").append(eachNumber.getNum()+":").append(numbers);
		t.setNumber(numberString.toString().substring(1,numberString.toString().length()));
		tickets.add(t);

		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("activityId", taocan.getId());
		bundle.putInt("lotteryid",wanfaInt);
		bundle.putLong("totalaccount",PeriodNumber*2);
		bundle.putBoolean("useRedPacket",false);
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);

	}
	public void payDlt(){
		ArrayList<Integer> num =new ArrayList<Integer>();
		DLTNumber eachNumber=play.getChoosedNumberDLT();
		num.addAll(eachNumber.getQianqu().getNumbers());
		num.addAll(eachNumber.getHouqu().getNumbers());
		User user = MyDbUtils.getCurrentUser();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());
		t.setPeriodNumber(period.getPeriodNumber());
		t.setWonStopOfChase("false");
		t.setIsSpecialFlag("false");
		t.setPlayTypeID("0");
		t.setTotalAmount(2+"");
		t.setMultiple(1+"");
		t.setSumNum(1+"");
		t.setHongBaoId("");
		t.setIsChase("true");
		t.setPeriodSizeOfChase(PeriodNumber+"");
		t.setTotalCostOfChase(PeriodNumber*2+"");
		t.setMode("1");
		t.setNumber(formatNumber(num));

		ArrayList<Ticket> tickets =new ArrayList<>();
		tickets.add(t);
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("activityId", taocan.getId());
		bundle.putInt("lotteryid",ConstantsBase.DLT);
		bundle.putLong("totalaccount",PeriodNumber*2);
		bundle.putBoolean("useRedPacket",false);
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);
	}
	public void paySsq(){
		ArrayList<Integer> num =new ArrayList<Integer>();
		DLTNumber eachNumber=play.getChoosedNumberDLT();
		num.addAll(eachNumber.getQianqu().getNumbers());
		num.addAll(eachNumber.getHouqu().getNumbers());
		User user = MyDbUtils.getCurrentUser();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());
		t.setPeriodNumber(period.getPeriodNumber());
		t.setWonStopOfChase("false");
		t.setIsSpecialFlag("false");
		t.setPlayTypeID("0");
		t.setTotalAmount(2+"");
		t.setMultiple(1+"");
		t.setSumNum(1+"");
		t.setHongBaoId("");
		t.setIsChase("true");
		t.setPeriodSizeOfChase(PeriodNumber+"");
		t.setTotalCostOfChase(PeriodNumber*2+"");
		t.setMode("1");
		t.setNumber(formatNumber(num));

		ArrayList<Ticket> tickets =new ArrayList<>();
		tickets.add(t);
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("activityId", taocan.getId());
		bundle.putInt("lotteryid",ConstantsBase.SSQ);
		bundle.putLong("totalaccount",PeriodNumber*2);
		bundle.putBoolean("useRedPacket",false);
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);
	}
	private String formatNumber(ArrayList<Integer> a) {
		// TODO 自动生成的方法存根
		StringBuilder totalNumber = new StringBuilder();
		for (int i = 0; i < a.size(); i++) {
			String singerNumber = null;
			if (a.get(i) < 10) {
				singerNumber = "0" + a.get(i);
			} else {
				singerNumber = String.valueOf(a.get(i));
			}
			if (i == 4) {
				singerNumber += "|";
			} else if (i == 6) {
				singerNumber = singerNumber;
			} else {
				singerNumber += ",";
			}
			totalNumber.append(singerNumber);
		}
		return totalNumber.toString();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN:	
				switch(wanfa){
				case ConstantsBase.SD115+"":
					checkPeriodNumber(ConstantsBase.SD115);
				break;
				case ConstantsBase.GD115+"":
					checkPeriodNumber(ConstantsBase.SD115);
				break;
				case ConstantsBase.DLT+"":
					payDlt(); 
				break;
				}
				break;
			case HAVEPAY:
				_this.finish();
				break;
			default:
				break;
			}
		}
	}
	private void checkPeriodNumber(final int lotteryId){
		// TODO 自动生成的方法存根
		ServiceUser.getInstance().PostPeriod(_this,lotteryId+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						String periodTimeOld = PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIOD115);
						String periodTimeNew = period.getPeriodNumber();
						if(periodTimeOld.equals(periodTimeNew)){
							pay115(lotteryId);
						}else{
							showEndTimeDialog(periodTimeOld,periodTimeNew,lotteryId);
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
	void showEndTimeDialog(String oldTime,final String newTime,final int lotteryId){

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);

		if(oldTime.length() >2){
			oldTime = oldTime.substring(oldTime .length()-2,oldTime.length());     					
		}
		String newTimeSub = "";
		if(newTime.length() >2){
			newTimeSub = newTime.substring(newTime .length()-2,newTime.length());     					
		}
		updatesize.setText(oldTime+"期已截止，可投注"+newTimeSub+"期");
		updatesize.setGravity(Gravity.CENTER);
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("取消");
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("投注");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115,newTime);			
				pay115(lotteryId);
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
			}
		});
	}
}
