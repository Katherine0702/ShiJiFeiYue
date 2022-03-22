package com.cshen.tiyu.activity.lottery.pl35;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
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
import android.text.Spanned;
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
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.NumberPl35;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.CurtainView;


public class Pl5MainActivity  extends Activity  implements OnClickListener,SensorEventListener  {
	private Pl5MainActivity _this;
	private ImageView shuoming,back;

	private com.cshen.tiyu.widget.CurtainView curtain;
	private View zhanwei;
	private SensorManager mSensorManager;//震动传感器
	private Vibrator vibrator ;//震动空间
	private View shake;//点击摇一摇
	private boolean isMoving = false;//是否在移动
	private View[] chooseesView ;//摇中的字的view，因为动画在父view内移动,这个父view的大小跟正常现实的textview一样大
	private View chooseView0,chooseView1,chooseView2,chooseView3,chooseView4;
	private TextView[] choosees ;//摇中的字
	private TextView choose0,choose1,choose2,choose3,choose4;
	int scaleAnimationsI=0;
	private ArrayList<TranslateAnimation> translateAnimations = new ArrayList<TranslateAnimation>();//摇中的动画分散开来
	private int[] wanweis = new int[]{R.id.wanweinum0,R.id.wanweinum1,
			R.id.wanweinum2,R.id.wanweinum3,R.id.wanweinum4,R.id.wanweinum5,
			R.id.wanweinum6,R.id.wanweinum7,R.id.wanweinum8,R.id.wanweinum9};
	private int[] qianweis = new int[]{R.id.qianweinum0,R.id.qianweinum1,
			R.id.qianweinum2,R.id.qianweinum3,R.id.qianweinum4,R.id.qianweinum5,
			R.id.qianweinum6,R.id.qianweinum7,R.id.qianweinum8,R.id.qianweinum9};
	private int[] baiweis = new int[]{R.id.baiweinum0,R.id.baiweinum1,
			R.id.baiweinum2,R.id.baiweinum3,R.id.baiweinum4,R.id.baiweinum5,
			R.id.baiweinum6,R.id.baiweinum7,R.id.baiweinum8,R.id.baiweinum9};
	private int[] shiweis = new int[]{R.id.shiweinum0,R.id.shiweinum1,
			R.id.shiweinum2,R.id.shiweinum3,R.id.shiweinum4,R.id.shiweinum5,
			R.id.shiweinum6,R.id.shiweinum7,R.id.shiweinum8,R.id.shiweinum9};
	private int[] geweis = new int[]{R.id.geweinum0,R.id.geweinum1,
			R.id.geweinum2,R.id.geweinum3,R.id.geweinum4,R.id.geweinum5,
			R.id.geweinum6,R.id.geweinum7,R.id.geweinum8,R.id.geweinum9};

	private TextView money;//注钱
	private ImageView clear;//清除
	private TextView prizeMoney;	
	private View sure;//确定

	private NumberPl35  numberPl5;
	private int position =-1;//大于-1就是从详情传来的
	private Period period;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpl5);
		_this = this;
		initView();
		initdata(); 
	}

	public void initView(){
		/********************顶部***********************/
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(this);
		shuoming = (ImageView) findViewById(R.id.iv_setting);
		shuoming.setOnClickListener(this);
		findViewById(R.id.tv_head).setOnClickListener(this);
		/********************下拉历史数据***********************/
		curtain = (CurtainView) findViewById(R.id.dlt_show_downview);
		zhanwei = findViewById(R.id.dlt_zhanwei_wuyong2);
		/********************球框万千百***********************/
		shake = findViewById(R.id.dlt_shake);
		shake.setOnClickListener(this);			
		for(int i=0;i<wanweis.length;i++){
			findViewById(wanweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<qianweis.length;i++){
			findViewById(qianweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<baiweis.length;i++){
			findViewById(baiweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<shiweis.length;i++){
			findViewById(shiweis[i]).setOnClickListener(this);
		}
		for(int i=0;i<geweis.length;i++){
			findViewById(geweis[i]).setOnClickListener(this);
		}
		/********************底部***********************/
		clear =  (ImageView) findViewById(R.id.dlt_clear);
		clear.setOnClickListener(this);
		sure =  findViewById(R.id.dlt_sure);
		sure.setOnClickListener(this);
		money = (TextView) findViewById(R.id.dlt_money);
		prizeMoney = (TextView) findViewById(R.id.prizemoney);
		setNumMoney(0);
		/********************选中球浮页***********************/
		choose0 = (TextView) findViewById(R.id.dltnumchoose0);
		choose1 = (TextView) findViewById(R.id.dltnumchoose1);
		choose2 = (TextView) findViewById(R.id.dltnumchoose2);
		choose3 = (TextView) findViewById(R.id.dltnumchoose3);
		choose4 = (TextView) findViewById(R.id.dltnumchoose4);
		chooseView0 = findViewById(R.id.dltnumchoose0view);
		chooseView1 = findViewById(R.id.dltnumchoose1view);
		chooseView2 = findViewById(R.id.dltnumchoose2view);
		chooseView3 = findViewById(R.id.dltnumchoose3view);
		chooseView4 = findViewById(R.id.dltnumchoose4view);
		choosees = new TextView[]{choose0,choose1,choose2,choose3,choose4};
		chooseesView  = new View[]{chooseView0,chooseView1,chooseView2,chooseView3,chooseView4};
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
			numberPl5 = (NumberPl35)b.getSerializable("numberPl5");
			if(numberPl5!=null){
				setNumbersChoosed(numberPl5);			
				setNumMoney(numberPl5.getNum());
			}
		}catch(Exception e){
			numberPl5 = null;
			clear();
			setNumMoney(0);
			e.printStackTrace();
		}
	}
	@Override  
	protected void onResume()  
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
	protected void onPause()  
	{  
		mSensorManager.unregisterListener(this);
		super.onPause();  
	}  	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			_this.finish();
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
				sure();
			}
			break;
		default:
			if(!isMoving){
				chooseBall(v);
			}
			break;
		}
	}

	/******************************历史合开奖逻辑,以及遗漏*****************************************/
	private void getNetDate() {
		initHistroyDataForCathectic();
		initDataForCathectic();
	}
	private void initHistroyDataForCathectic(){		
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,ConstantsBase.PL35+"","0","10","",new CallBack<PrizeList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub		
				ArrayList<Prize>	resultList = new ArrayList();
				Prize dlt = new Prize();
				dlt.setPeriodNumber("2001602015");
				dlt.setResult("12,34,23,22,09");
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
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.PL35+"", new CallBack<PeriodResultData>() {

			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根

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
						String newEndTime = endTime + "截止，21:30开奖";
						PreferenceUtil.putString(_this,PreferenceUtil.PL5PRIZEPERIOD,periodTime);
						time = newPeriodTime+newEndTime;
					}
					curtain.setTextView(s1.toString(),time);
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}

	public void clear(){
		numberPl5 = null;
		for(int i=0;i<wanweis.length;i++){
			seColorBack(wanweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<qianweis.length;i++){
			seColorBack(qianweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<baiweis.length;i++){
			seColorBack(baiweis[i],"#E73C42",R.drawable.dlt_number_back);
		}	
		for(int i=0;i<shiweis.length;i++){
			seColorBack(shiweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
		for(int i=0;i<geweis.length;i++){
			seColorBack(geweis[i],"#E73C42",R.drawable.dlt_number_back);
		}
	}
	public void setNumbersChoosed(NumberPl35 numbers){
		for(int i=0;i<numbers.getWan().size();i++){
			seColorBack(wanweis[numbers.getWan().get(i)],"#FFFFFF",R.drawable.dlt_number_back_red);
		}
		for(int i=0;i<numbers.getQian().size();i++){
			seColorBack(qianweis[numbers.getQian().get(i)],"#FFFFFF",R.drawable.dlt_number_back_red);
		}
		for(int i=0;i<numbers.getBai().size();i++){
			seColorBack(baiweis[numbers.getBai().get(i)],"#FFFFFF",R.drawable.dlt_number_back_red);
		}
		for(int i=0;i<numbers.getShi().size();i++){
			seColorBack(shiweis[numbers.getShi().get(i)],"#FFFFFF",R.drawable.dlt_number_back_red);
		}
		for(int i=0;i<numbers.getGe().size();i++){
			seColorBack(geweis[numbers.getGe().get(i)],"#FFFFFF",R.drawable.dlt_number_back_red);
		}
	}
	public void seColorBack(int id,String color,int back){
		((TextView) findViewById(id)).setTextColor(Color.parseColor(color));
		findViewById(id).setBackgroundResource(back);
	}
	public void sure(){
		if(numberPl5 == null||(numberPl5.getWan().size()+
				numberPl5.getQian().size()+
				numberPl5.getBai().size()+
				numberPl5.getShi().size()+
				numberPl5.getGe().size())==0){//没选择过
			jiXuanChoose();
			return;
		}else{
			if(numberPl5.getWan()== null
					||numberPl5.getWan().size() == 0){
				ToastUtils.showShort(_this, "请选择万位号码");
				return;
			}
			if(numberPl5.getQian()== null
					||numberPl5.getQian().size() == 0){
				ToastUtils.showShort(_this, "请选择千位号码");
				return;
			}
			if(numberPl5.getBai()== null
					||numberPl5.getBai().size()  == 0){
				ToastUtils.showShort(_this, "请选择百位号码");
				return;
			}
			if(numberPl5.getShi()== null
					||numberPl5.getShi().size()  == 0){
				ToastUtils.showShort(_this, "请选择百位号码");
				return;
			}
			if(numberPl5.getGe()== null
					||numberPl5.getGe().size()  == 0){
				ToastUtils.showShort(_this, "请选择百位号码");
				return;
			}

			numberPl5.setNum(ChooseUtilPl35.getChooseUtil().getPl35Time(numberPl5));
			numberPl5 = ChooseUtilPl35.getChooseUtil().getSortListPl5(numberPl5);
			if(position == -1){
				Intent intent = new Intent(_this, Pl5AccountActivity.class);
				Bundle bundle = new Bundle();   
				bundle.putSerializable("numberPl5", numberPl5);
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				Intent intent = new Intent();  
				Bundle bundle = new Bundle();   
				bundle.putInt("position", position);
				bundle.putSerializable("numberPl5", numberPl5);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent); 
			}
			this.finish();
		}
	}
	public void chooseBall(View v){
		Arrays.sort(wanweis);  
		Arrays.sort(qianweis); 
		Arrays.sort(baiweis); 
		int indexWan = Arrays.binarySearch(wanweis, v.getId()); 
		int indexQian = Arrays.binarySearch(qianweis, v.getId());  
		int indexBai = Arrays.binarySearch(baiweis, v.getId()); 
		int indexShi = Arrays.binarySearch(shiweis, v.getId());  
		int indexGe = Arrays.binarySearch(geweis, v.getId()); 
		if(indexWan>=0){//排位从0开始，初始值从1开始
			if(!isWanHave(indexWan)){
				numberPl5.getWan().add(indexWan+1);
				seColorBack(wanweis[indexWan],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}else if(indexQian>=0){
			if(!isQianHave(indexQian)){//千中有则消掉，千中没有进入
				numberPl5.getQian().add(indexQian+1);
				seColorBack(qianweis[indexQian],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}else if(indexBai>=0){
			if(!isBaiHave(indexBai)){//千中有则消掉，千中没有进入
				numberPl5.getBai().add(indexBai+1);
				seColorBack(baiweis[indexBai],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}else if(indexShi>=0){
			if(!isShiHave(indexShi)){//千中有则消掉，千中没有进入
				numberPl5.getShi().add(indexShi+1);
				seColorBack(shiweis[indexShi],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}else if(indexGe>=0){
			if(!isGeHave(indexGe)){//千中有则消掉，千中没有进入
				numberPl5.getGe().add(indexGe+1);
				seColorBack(geweis[indexGe],"#FFFFFF",R.drawable.dlt_number_back_red);
			}
		}
		if(isNumberPl5Null(numberPl5)){
			if(numberPl5!=null){
				numberPl5.setNum(0);
			}
			setNumMoney(0);
		}else if(isNumberPl5NotFull(numberPl5)){
			numberPl5.setNum(0);
			setNumMoney(0);				
		}else{
			int time = ChooseUtilPl35.getChooseUtil().getPl35Time(numberPl5);
			numberPl5.setNum(time);
			setNumMoney(time);
		}
	}
	public void setNumMoney(int time){
		String moneyStr1 = "<html><font color=\"#000000\">"+time+"注"
				+"</font><font color=\"#e73c42\">"+time*2+""
				+ "</font><font color=\"#000000\">元</font></html>";
		money.setText(Html.fromHtml(moneyStr1));
		if(time == 0){
			prizeMoney.setVisibility(View.GONE);
			findViewById(R.id.line2).setVisibility(View.GONE);
		}else{
			prizeMoney.setVisibility(View.VISIBLE);
			findViewById(R.id.line2).setVisibility(View.VISIBLE);
			Spanned prizeMoneyStr = ChooseUtilPl35.getChooseUtil().maymoney(numberPl5);
			prizeMoney.setText(prizeMoneyStr);
		}
	}
	/*****************************万千百逻辑******************************/
	public boolean isWanHave(int num){
		if(numberPl5 == null){
			numberPl5 = new NumberPl35();
		}
		if(numberPl5.getWan()!=null&&numberPl5.getWan().contains(num+1)){
			numberPl5.getWan().remove((Object)(num+1));
			seColorBack(wanweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(isNumberPl5Null(numberPl5)){
				numberPl5 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isQianHave(int num){
		if(numberPl5 == null){
			numberPl5 = new NumberPl35();
		}
		if(numberPl5.getQian()!=null&&numberPl5.getQian().contains(num+1)){
			numberPl5.getQian().remove((Object)(num+1));
			seColorBack(qianweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(isNumberPl5Null(numberPl5)){
				numberPl5 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isBaiHave(int num){
		if(numberPl5 == null){
			numberPl5 = new NumberPl35();
		}
		if(numberPl5.getBai()!=null&&numberPl5.getBai().contains(num+1)){
			numberPl5.getBai().remove((Object)(num+1));
			seColorBack(baiweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(isNumberPl5Null(numberPl5)){
				numberPl5 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isShiHave(int num){
		if(numberPl5 == null){
			numberPl5 = new NumberPl35();
		}
		if(numberPl5.getShi()!=null&&numberPl5.getShi().contains(num+1)){
			numberPl5.getShi().remove((Object)(num+1));
			seColorBack(shiweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(isNumberPl5Null(numberPl5)){
				numberPl5 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isGeHave(int num){
		if(numberPl5 == null){
			numberPl5 = new NumberPl35();
		}
		if(numberPl5.getGe()!=null&&numberPl5.getGe().contains(num+1)){
			numberPl5.getGe().remove((Object)(num+1));
			seColorBack(geweis[num],"#E73C42",R.drawable.dlt_number_back);
			if(isNumberPl5Null(numberPl5)){
				numberPl5 = null;
			}
			return true;
		}
		return false;
	}
	public boolean isNumberPl5Null(NumberPl35 numberPl5){
		if(numberPl5 == null||(numberPl5.getWan().size()+
				numberPl5.getQian().size()+
				numberPl5.getBai().size()+
				numberPl5.getShi().size()+
				numberPl5.getGe().size()==0)){
			return true;
		}
		return false;
	}
	public boolean isNumberPl5NotFull(NumberPl35 numberPl5){
		if(numberPl5.getWan().size() == 0||
				numberPl5.getQian().size() == 0||
				numberPl5.getBai().size() == 0||
				numberPl5.getShi().size() == 0||
				numberPl5.getGe().size()==0){
			return true;
		}
		return false;
	}
	public void jiXuanChoose(){
		clear();
		numberPl5 =  ChooseUtilPl35.getChooseUtil().getRandomPl5();
		setNumbersChoosed(numberPl5);
		setNumMoney(numberPl5.getNum());
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
				numberPl5 =  ChooseUtilPl35.getChooseUtil().getRandomPl5();
				setNumMoney(numberPl5.getNum());
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
			chooseesView[i].setVisibility(View.INVISIBLE);
		}		
		choosees[0].setText(numberPl5.getWan().get(0)+"");
		choosees[1].setText(numberPl5.getQian().get(0)+"");
		choosees[2].setText(numberPl5.getBai().get(0)+"");
		choosees[3].setText(numberPl5.getShi().get(0)+"");
		choosees[4].setText(numberPl5.getGe().get(0)+"");
		choosees[4].post(new Runnable() {
			@Override
			public void run() {
				setAnimation();
			}
		});	
	}
	public ScaleAnimation bigToSmall(){
		//初始化
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.1f,0.1f,1.1f,  
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//设置动画时间
		scaleAnimation.setDuration(200);		
		return (ScaleAnimation) scaleAnimation;
	}
	public void setAnimation(){
		if(scaleAnimationsI<5){
			ScaleAnimation scaleAnimation = bigToSmall();
			chooseesView[scaleAnimationsI].startAnimation(scaleAnimation);
			chooseesView[scaleAnimationsI].setVisibility(View.VISIBLE);
			scaleAnimationsI++;	
			if(scaleAnimationsI<5){
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
		for(int i=0;i<5;i++){
			TextView viewTextView = null;
			if(i == 0){
				viewTextView = (TextView) findViewById(wanweis[numberPl5.getWan().get(0)]);
			}
			if(i == 1){
				viewTextView = (TextView) findViewById(qianweis[numberPl5.getQian().get(0)]);
			}
			if(i == 2){
				viewTextView = (TextView) findViewById(baiweis[numberPl5.getBai().get(0)]);
			}
			if(i == 3){
				viewTextView = (TextView) findViewById(shiweis[numberPl5.getShi().get(0)]);
			}
			if(i == 4){
				viewTextView = (TextView) findViewById(geweis[numberPl5.getGe().get(0)]);
			}
			int[] locationFrom  = new int[2];  
			chooseesView[i].getLocationOnScreen(locationFrom);  

			int leftFrom = locationFrom[0];  
			int topFrom = locationFrom[1]; 
			int[] locationTo  = new int[2];  
			viewTextView.getLocationOnScreen(locationTo);  
			int leftTo = locationTo[0];  
			int topTo = locationTo[1]; 
			translateAnimations.add(
					toPlace(i,5,viewTextView,leftFrom,topFrom,leftTo,topTo));

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
}
