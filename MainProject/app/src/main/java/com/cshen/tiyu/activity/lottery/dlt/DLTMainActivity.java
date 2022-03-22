package com.cshen.tiyu.activity.lottery.dlt;


import java.util.ArrayList;
import java.util.Arrays;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.dltssq.NumberEach;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.CurtainView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class DLTMainActivity  extends BaseActivity  implements OnClickListener,SensorEventListener  {
	public static final int HAVENUMBER = 2;
	private DLTMainActivity _this;
	private TopViewLeft tv_head;//头
	private com.cshen.tiyu.widget.CurtainView curtain;
	private View zhanwei;
	private TextView money;//帮助，注钱
	private ImageView clear;//清除
	private View sure;//确定
	private int[] reds35View = new int[]{//红球View、方便移动
			R.id.dlt_numview1 ,R.id.dlt_numview2 ,R.id.dlt_numview3 ,R.id.dlt_numview4 ,R.id.dlt_numview5,
			R.id.dlt_numview6 ,R.id.dlt_numview7 ,R.id.dlt_numview8 ,R.id.dlt_numview9 ,R.id.dlt_numview10,
			R.id.dlt_numview11,R.id.dlt_numview12,R.id.dlt_numview13,R.id.dlt_numview14,R.id.dlt_numview15,
			R.id.dlt_numview16,R.id.dlt_numview17,R.id.dlt_numview18,R.id.dlt_numview19,R.id.dlt_numview20,
			R.id.dlt_numview21,R.id.dlt_numview22,R.id.dlt_numview23,R.id.dlt_numview24,R.id.dlt_numview25,
			R.id.dlt_numview26,R.id.dlt_numview27,R.id.dlt_numview28,R.id.dlt_numview29,R.id.dlt_numview30,
			R.id.dlt_numview31,R.id.dlt_numview32,R.id.dlt_numview33,R.id.dlt_numview34,R.id.dlt_numview35};
	private int[] blues12View = new int[]{//蓝球View、方便移动
			R.id.dltnumview1,R.id.dltnumview2,R.id.dltnumview3,R.id.dltnumview4,R.id.dltnumview5,
			R.id.dltnumview6,R.id.dltnumview7,R.id.dltnumview8,R.id.dltnumview9,R.id.dltnumview10,
			R.id.dltnumview11,R.id.dltnumview12};
	private int[] reds35 = new int[]{//红球
			R.id.dlt_num1,R.id.dlt_num2,R.id.dlt_num3,R.id.dlt_num4,R.id.dlt_num5,
			R.id.dlt_num6,R.id.dlt_num7,R.id.dlt_num8,R.id.dlt_num9,R.id.dlt_num10,
			R.id.dlt_num11,R.id.dlt_num12,R.id.dlt_num13,R.id.dlt_num14,R.id.dlt_num15,
			R.id.dlt_num16,R.id.dlt_num17,R.id.dlt_num18,R.id.dlt_num19,R.id.dlt_num20,
			R.id.dlt_num21,R.id.dlt_num22,R.id.dlt_num23,R.id.dlt_num24,R.id.dlt_num25,
			R.id.dlt_num26,R.id.dlt_num27,R.id.dlt_num28,R.id.dlt_num29,R.id.dlt_num30,
			R.id.dlt_num31,R.id.dlt_num32,R.id.dlt_num33,R.id.dlt_num34,R.id.dlt_num35};
	private int[] blues12 = new int[]{//蓝球
			R.id.dltnum1,R.id.dltnum2,R.id.dltnum3,R.id.dltnum4,R.id.dltnum5,
			R.id.dltnum6,R.id.dltnum7,R.id.dltnum8,R.id.dltnum9,R.id.dltnum10,
			R.id.dltnum11,R.id.dltnum12};

	private boolean isMoving = false;//是否在移动
	private SensorManager mSensorManager;//震动传感器
	private Vibrator vibrator ;//震动空间
	private View shake;//点击摇一摇
	private TextView[] choosees ;//摇中的字
	private View[] chooseesView ;//摇中的字的view，因为动画在父view内移动
	private View chooseView1,chooseView2,chooseView3,chooseView4,chooseView5,chooseView6,chooseView7;
	private TextView choose1,choose2,choose3,choose4,choose5,choose6,choose7;

	int scaleAnimationsI=0;
	private ArrayList<TranslateAnimation> translateAnimations = new ArrayList<TranslateAnimation>();//摇中的动画分散开来
	private NumberEach qianqu,houqu;
	private DLTNumber  dltNumber;
	private int position =-1;//大于-1就是从详情传来的

	Period period = null;// 为投注做铺垫必须先拿到	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dltmain);
		_this = this;
		initView();
		initdata(); 
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
		mSensorManager.unregisterListener(this);  
		super.onPause();  
	}  	
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true, true ,false);
		tv_head.setImageRight(R.mipmap.shuoming);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
				Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
				intentHelp.putExtra("url","http://an.caiwa188.com/help/taocan.html");
				startActivity(intentHelp);
			}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});		
		curtain = (CurtainView) findViewById(R.id.dlt_show_downview);
		zhanwei = findViewById(R.id.dlt_zhanwei_wuyong2);

		clear =  (ImageView) findViewById(R.id.dlt_clear);
		clear.setOnClickListener(this);
		sure =  findViewById(R.id.dlt_sure);
		sure.setOnClickListener(this);
		money = (TextView) findViewById(R.id.dlt_money);
		String moneyStr = "<html><font color=\"#000000\">0注"
				+"</font><font color=\"#FF3232\">0"
				+ "</font><font color=\"#000000\">元</font></html>";
		money.setText(Html.fromHtml(moneyStr));
		shake = findViewById(R.id.dlt_shake);
		shake.setOnClickListener(this);

		for(int i=0;i<reds35.length;i++){
			findViewById(reds35[i]).setOnClickListener(this);
		}
		for(int i=0;i<blues12.length;i++){
			findViewById(blues12[i]).setOnClickListener(this);
		}
		choose1 = (TextView) findViewById(R.id.dltnumchoose1);
		choose2 = (TextView) findViewById(R.id.dltnumchoose2);
		choose3 = (TextView) findViewById(R.id.dltnumchoose3);
		choose4 = (TextView) findViewById(R.id.dltnumchoose4);
		choose5 = (TextView) findViewById(R.id.dltnumchoose5);
		choose6 = (TextView) findViewById(R.id.dltnumchoose6);
		choose7 = (TextView) findViewById(R.id.dltnumchoose7);
		chooseView1 = findViewById(R.id.dltnumchoose1view);
		chooseView2 = findViewById(R.id.dltnumchoose2view);
		chooseView3 = findViewById(R.id.dltnumchoose3view);
		chooseView4 = findViewById(R.id.dltnumchoose4view);
		chooseView5 = findViewById(R.id.dltnumchoose5view);
		chooseView6 = findViewById(R.id.dltnumchoose6view);
		chooseView7 = findViewById(R.id.dltnumchoose7view);
		choosees = new TextView[]{choose1,choose2,choose3,choose4,choose5,choose6,choose7};
		chooseesView  = new View[]{chooseView1,chooseView2,chooseView3,chooseView4,
				chooseView5,chooseView6,chooseView7};
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
			dltNumber = (DLTNumber)b.getSerializable("dltNumber");
			if(dltNumber!=null){
				qianqu = dltNumber.getQianqu();
				houqu = dltNumber.getHouqu();
				setNumbersChoosed(qianqu);
				setNumbersChoosed(houqu);
				String moneyStr = "<html><font color=\"#000000\">"+dltNumber.getNum()+"注"
						+"</font><font color=\"#FF3232\">"+dltNumber.getNum()*2+""
						+ "</font><font color=\"#000000\">元</font></html>";
				money.setText(Html.fromHtml(moneyStr));
			}	
		}catch(Exception e){
			qianqu = null;
			houqu = null;
			dltNumber = null;
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dlt_clear:
			clear();
			String moneyStr = "<html><font color=\"#000000\">0注"
					+"</font><font color=\"#FF3232\">0"
					+ "</font><font color=\"#000000\">元</font></html>";
			money.setText(Html.fromHtml(moneyStr));
			break;
		case R.id.dlt_shake:
			if(!isMoving){
				vibrator.vibrate(150);
				jiXuanChoose();
			}
			break;
		case R.id.dlt_sure:
			if(!isMoving){
				if(qianqu == null&&houqu == null){//没选择过
					jiXuanChoose();
					return;
				}else{
					if(qianqu != null&&houqu != null){//都选择了
						if(qianqu.getNumbers().size()<5){
							ToastUtils.showShort(_this, "前区至少选择5个");					
							return;
						}
						if(qianqu.getNumbers().size()>18){
							ToastUtils.showShort(_this, "前区至多选择18个");					
							return;
						}
						if(houqu.getNumbers().size()<2){
							ToastUtils.showShort(_this, "后区至少选择2个");					
							return;
						}
						if(dltNumber == null){
							dltNumber = new DLTNumber();
						}
						dltNumber.setQianqu(ChooseUtil.getChooseUtil().getSortList(qianqu));
						dltNumber.setHouqu(ChooseUtil.getChooseUtil().getSortList(houqu));
					}else{//选择了一个
						if(qianqu == null){
							ToastUtils.showShort(_this, "前区至少选择5个");	
						}
						if(houqu == null){
							ToastUtils.showShort(_this, "后区至少选择2个");	
						}
						return;
					}
				}
				dltNumber.setNum(ChooseUtil.getChooseUtil().getTime(
						dltNumber.getQianqu().getNumbers().size(),
						dltNumber.getHouqu().getNumbers().size()));
				if(position == -1){
					Intent intent = new Intent(_this, DLTAccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("dltNumber", dltNumber);
					intent.putExtras(bundle);
					startActivity(intent);
				}else{
					Intent intent = new Intent();  
					Bundle bundle = new Bundle();   
					bundle.putInt("position", position);
					bundle.putSerializable("dltNumber", dltNumber);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent); 
				}
				this.finish();
			}
			break;
		default:
			Arrays.sort(reds35);  
			Arrays.sort(blues12);  
			int indexRed = Arrays.binarySearch(reds35, v.getId()); 
			int indexBlue = Arrays.binarySearch(blues12, v.getId());  
			if(indexRed>=0){//排位从0开始，初始值从1开始
				if(qianqu == null){
					qianqu = new NumberEach();
				}
				qianqu.setType(0);
				if(qianqu.getNumbers().contains(indexRed+1)){
					qianqu.getNumbers().remove((Object)(indexRed+1));
					seColorBack(reds35[indexRed],"#E73C42",R.drawable.dlt_number_back);
					if(qianqu.getNumbers().size() == 0){
						qianqu   = null;
					}
				}else{
					if(qianqu.getNumbers().size() == 18){
						ToastUtils.showShort(_this, "前区至多选择18个");					
						return;
					}
					qianqu.getNumbers().add(indexRed+1);	
					seColorBack(reds35[indexRed],"#FFFFFF",R.drawable.dlt_number_back_red);
				}
			}else if(indexBlue>=0){
				if(houqu == null){
					houqu = new NumberEach();
				}
				houqu.setType(1);
				if(houqu.getNumbers().contains(indexBlue+1)){
					houqu.getNumbers().remove((Object)(indexBlue+1));
					seColorBack(blues12[indexBlue],"#3389F1",R.drawable.dlt_number_back);
					if(houqu.getNumbers().size() == 0){
						houqu = null;
					}
				}else{
					houqu.getNumbers().add(indexBlue+1);
					seColorBack(blues12[indexBlue],"#FFFFFF",R.drawable.dlt_number_back_blue);
				}
			}			
			int qianquSize =0,houquSize=0;
			if(qianqu!=null){
				qianquSize =  qianqu.getNumbers().size();
			}if(houqu!=null){
				houquSize =  houqu.getNumbers().size();
			}
			if(qianquSize>=5&&houquSize>=2){
				int time = ChooseUtil.getChooseUtil().getTime(
						qianquSize,
						houquSize);
				String moneyStr1 = "<html><font color=\"#000000\">"+time+"注"
						+"</font><font color=\"#FF3232\">"+time*2+""
						+ "</font><font color=\"#000000\">元</font></html>";
				money.setText(Html.fromHtml(moneyStr1));
			}else{
				String moneyStr2 = "<html><font color=\"#000000\">0注"
						+"</font><font color=\"#FF3232\">0"
						+ "</font><font color=\"#000000\">元</font></html>";
				money.setText(Html.fromHtml(moneyStr2));
			}
			break;
		}
	}
	public void clear(){
		qianqu = null;
		houqu = null;
		dltNumber = null;
		for(int i=0;i<reds35.length;i++){
			seColorBack(reds35[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<blues12.length;i++){
			seColorBack(blues12[i],"#3389F1",R.drawable.dlt_number_back);
		}
	}
	public void jiXuanChoose(){
		clear();
		dltNumber =  ChooseUtil.getChooseUtil().getRandom();
		qianqu = dltNumber.getQianqu();
		houqu = dltNumber.getHouqu();
		setNumbersChoosed(qianqu);
		setNumbersChoosed(houqu);
		String moneyStr = "<html><font color=\"#000000\">"+dltNumber.getNum()+"注"
				+"</font><font color=\"#FF3232\">"+dltNumber.getNum()*2
				+ "</font><font color=\"#000000\">元</font></html>";
		money.setText(Html.fromHtml(moneyStr));
	}
	public void setNumbersChoosed(NumberEach numbers){
		if(numbers.getType() == 0){//前区
			for(int i=0;i<numbers.getNumbers().size();i++){
				seColorBack(reds35[numbers.getNumbers().get(i)-1],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}
		if(numbers.getType() == 1){//后区
			for(int j=0;j<numbers.getNumbers().size();j++){
				seColorBack(blues12[numbers.getNumbers().get(j)-1],"#FFFFFF",R.drawable.dlt_number_back_blue);
			}
		}
	}
	public void seColorBack(int id,String color,int back){
		((TextView) findViewById(id)).setTextColor(Color.parseColor(color));
		findViewById(id).setBackgroundResource(back);
	}
	private void getNetDate() {
		initDataForCathectic();
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,ConstantsBase.DLT+"","0","10","",new CallBack<PrizeList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub		
				ArrayList<Prize>	resultList = new ArrayList();
				Prize dlt = new Prize();
				dlt.setPeriodNumber("2001602015");
				dlt.setResult("12,34,23,22,09,10,12");
				for(int i = 0;i<10;i++){
					resultList.add(dlt);
				}
				curtain.addItenView(resultList);
				curtain.setViewZhanwei(zhanwei);
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
						curtain.addItenView(resultList);	
						curtain.setViewZhanwei(zhanwei);					
					}
				}

			}
		});


	}
	private void initDataForCathectic() {
		// TODO 自动生成的方法存根

		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.DLT+"", new CallBack<PeriodResultData>() {

			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t!=null&&t.getPeriodList()!=null
						&&t.getPeriodList().size()>0){
					period = t.getPeriodList().get(0);// 拿到第一个数据

					if(period!=null){
						String periodTime = period.getPeriodNumber();
						String newPeriodTime = "第" + periodTime + "期      ";
						String money = period.getPrizePool();
						StringBuffer s1=new StringBuffer(money);
						if(money.length()>3){
							for(int i = money.length()-3;i>0;i-=3){
								s1.insert(i,',');
							}
						}
						String time = "";
						if(period.getEndJoinTime_single().length()>16){
							String endTime = period.getEndJoinTime_single()
									.substring(5, 16);
							String newEndTime = endTime + "截止，20:30开奖";
							PreferenceUtil.putString(_this,PreferenceUtil.DLTPRIZEPERIOD,periodTime);
							time = newPeriodTime+newEndTime;
						}
						curtain.setTextView(s1.toString(),time);
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

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
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
				dltNumber =  ChooseUtil.getChooseUtil().getRandom();
				qianqu = dltNumber.getQianqu();
				houqu = dltNumber.getHouqu();
				String moneyStr = "<html><font color=\"#000000\">"+dltNumber.getNum()+"注"
						+"</font><font color=\"#FF3232\">"+dltNumber.getNum()*2
						+ "</font><font color=\"#000000\">元</font></html>";
				money.setText(Html.fromHtml(moneyStr));
				scaleAnimationsI = 0;
				translateAnimations.clear();
				setBigToSmallView();
				setBigToSmall();
			}  
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	public void setBigToSmallView(){
		for(int i=0;i<qianqu.getNumbers().size();i++){
			if(qianqu.getNumbers().get(i)<10){
				choosees[i].setText("0"+qianqu.getNumbers().get(i)+"");
			}else{
				choosees[i].setText(qianqu.getNumbers().get(i)+"");
			}
		}
		for(int i=0;i<houqu.getNumbers().size();i++){
			if(houqu.getNumbers().get(i)<10){
				choosees[5+i].setText("0"+houqu.getNumbers().get(i)+"");
			}else{
				choosees[5+i].setText(houqu.getNumbers().get(i)+"");
			}
		}
	}
	public void setBigToSmall(){
		if(scaleAnimationsI<7){
			ScaleAnimation scaleAnimation = bigToSmall();
			chooseesView[scaleAnimationsI].startAnimation(scaleAnimation);
			chooseesView[scaleAnimationsI].setVisibility(View.VISIBLE);
			scaleAnimationsI++;	
			if(scaleAnimationsI<7){
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						setBigToSmall();
					}
				}, 100);
			}else{
				setBigToSmall();
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
		for(int i=0;i<choosees.length;i++){
			View view ;TextView viewTextView ;
			if(i<5){
				view = findViewById(reds35View[qianqu.getNumbers().get(i)-1]);
			}else{
				view = findViewById(blues12View[houqu.getNumbers().get(i-5)-1]);
			}
			if(i<5){
				viewTextView = (TextView) findViewById(reds35[qianqu.getNumbers().get(i)-1]);
			}else{
				viewTextView = (TextView) findViewById(blues12[houqu.getNumbers().get(i-5)-1]);
			}	
			// TODO Auto-generated method stub
			int[] locationFrom  = new int[2];  
			chooseesView[i].getLocationOnScreen(locationFrom);  
			int leftFrom = locationFrom[0];  
			int topFrom = locationFrom[1]; 

			int[] locationTo  = new int[2];  
			view.getLocationOnScreen(locationTo);  
			int leftTo = locationTo[0];  
			int topTo = locationTo[1]; 
			translateAnimations.add(
					toPlace(i,chooseesView[i],viewTextView,leftFrom,topFrom,leftTo,topTo));

		}
	}
	public ScaleAnimation bigToSmall(){
		//初始化
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.1f,0.1f,1.1f,  
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//设置动画时间
		scaleAnimation.setDuration(200);		
		return (ScaleAnimation) scaleAnimation;
	}

	public TranslateAnimation toPlace(final int index,final View viewFrom,final TextView viewToTextView,
			float leftFrom,float topFrom,float leftTo,float topTo)
	{
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
				viewFrom.clearAnimation();
				viewFrom.setVisibility(View.INVISIBLE);
				if(index<5){
					viewToTextView.setTextColor(Color.parseColor("#FFFFFF"));
					viewToTextView.setBackgroundResource(R.drawable.dlt_number_back_red);
				}else{
					viewToTextView.setTextColor(Color.parseColor("#FFFFFF"));
					viewToTextView.setBackgroundResource(R.drawable.dlt_number_back_blue);
				}
				if(index == 6){
					isMoving = false;
				}


			}
		});
		return animation;
	}
}
