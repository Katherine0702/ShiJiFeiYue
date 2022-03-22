package com.cshen.tiyu.activity.lottery.ball;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.JCLQUtil;
import com.cshen.tiyu.activity.lottery.ball.football.JJYHMainActivity;
import com.cshen.tiyu.activity.lottery.ball.util.PassType2;
import com.cshen.tiyu.activity.lottery.ball.util.SortByChuanName;
import com.cshen.tiyu.activity.lottery.ball.util.SortByKey;
import com.cshen.tiyu.activity.lottery.ball.util.SortByMatchsKey;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.pay.TZXYActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ball.JczqChoosedScroeBean;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.PassTypeEach;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.google.gson.Gson;

public class AccountListActivity extends BaseActivity implements OnClickListener{
	public static final int HAVEPAY = 0;
	public static final int TODETAIL = 1;//详情选择
	public static final int TOADD = 2;
	public static final int LOGIN = 3;
	public AccountListActivity _this;
	public boolean isToPay = false;

	public TopViewLeft tv_head;//标题
	private View add;//增加赛事
	public ListView listview;//赛事列表
	public BaseAdapter myListAdapter;

	private CheckBox provisions;//条款
	private boolean isTongyi = true;//条款同意与否
	private View clear;//清除
	public View nodate;//没有赛事
	public TextView numbers;//已选几场
	/***********
	 ******玩法相关
	 **************/
	private PopupWindow pop;//玩法弹出框
	private View loadchuan;//串关时使用
	public View hunheguoguan;
	public View choosechuanguan;//串关玩法
	public TextView choosechuanguantext;//串关玩法
	public ImageView rightarrow1;//串关玩法
	public TextView[] chuan1s;
	public TextView[] chuanNs;
	public PassType2 pyMain= new PassType2();
	public ArrayList<PassTypeEach> chuan1,chuanm;
	public ArrayList<PassTypeEach> ziyouPT;//选中的
	public ArrayList<PassTypeEach> hunhePT;//选中的
	/***********
	 ******胆相关
	 **************/
	public int danMaxCount = 0;
	public int danCount = 0;
	/***********
	 ******倍数相关
	 **************/
	private View loadtime;//倍数时使用
	private View realtimeview;
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
	public View jiangjinyouhua;
	public float minMoney=0f,maxMoney=0f;
	/***********
	 ******计算相关
	 **************/
	public HashMap	mapChoosed;
	public int wanfa = 0;
	public boolean isSingle ;
	public ArrayList<Match> matchs;
	public ArrayList<String> keys ;
	/***********
	 ******跳转
	 **************/
	public boolean isToDetil ;
	public boolean canClick ;
	private View loadmain;//detail时候使用
	private AlertDialog  alertDialog ;
	/***********
	 ******发起推荐
	 **************/
	public View recommend;
	private PopupWindow popRecom;//玩法弹出框
	private TextView faqituijiantxt;
	private boolean isTuijianProvisional = false;
	private boolean cannull = false;
	private boolean isTuijian = false;
	private String tuijianTiltle = "";
	private String tuijianContent = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footballaccount_list);
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
				if(mapChoosed!=null&&
						mapChoosed.size()>0){
					dialog();
				}else{
					finish();
				}
			}
		});
		onCreateBase();
	}
	public void initView() {
		jiangjinyouhua = findViewById(R.id.jiangjinyouhua);
		recommend = findViewById(R.id.faqituijian);
		faqituijiantxt  = (TextView) findViewById(R.id.faqituijiantxt);
		add = findViewById(R.id.add);
		add.setOnClickListener(this);
		numbers = (TextView) findViewById(R.id.numbers);
		nodate  = findViewById(R.id.nodate);
		nodate.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.dlt_listview);
		listview.setAdapter(myListAdapter);
		allMoney =  (TextView) findViewById(R.id.allmoney);
		allTime =  (TextView) findViewById(R.id.alltime);
		realtimeview =  findViewById(R.id.realtimeview);
		realtimeview.setOnClickListener(this);
		realTime =  (TextView) findViewById(R.id.realtime);
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

		choosechuanguan = findViewById(R.id.choosechuanguan);
		choosechuanguan.setOnClickListener(this);
		choosechuanguantext = (TextView)findViewById(R.id.wanfatext);
		rightarrow1 = (ImageView)findViewById(R.id.right_arrow1);
		View footer =  View.inflate(_this, R.layout.dltaccount_list_footer,null);
		listview.addFooterView(footer);
		clear = footer.findViewById(R.id.clear);
		clear.setOnClickListener(this);
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
		loadmain = findViewById(R.id.loadmain);
		loadmain.setAlpha(0.7f);
		loadchuan = findViewById(R.id.load);
		loadchuan.setAlpha(0.7f);
		loadchuan.setOnClickListener(this);	
		loadtime = findViewById(R.id.load2);
		loadtime.setAlpha(0.7f);
		loadtime.setOnClickListener(this);	
	}
	public void onCreateBase() {}
	public void initdata() {
		Bundle b = getIntent().getExtras();
		mapChoosed  = (HashMap) b.getSerializable("matchs");
		wanfa  = b.getInt("wanfa");
		doDate();
	}
	public void doDate(){
		if(mapChoosed!=null&&
				mapChoosed.size()>0){
			keys = new ArrayList<String>();
			matchs = new ArrayList<Match>();
			Iterator iter = mapChoosed.entrySet().iterator();  
			while (iter.hasNext()) {  
				Map.Entry entry = (Map.Entry) iter.next(); 
				keys.add((String) entry.getKey()); 
				matchs.add((Match)entry.getValue());
			}
		}else{
			if(keys!=null){
				keys.clear();
			}
			if(matchs!=null){
				matchs.clear();
			}
		}
		if(keys!=null&&matchs!=null){
			Collections.sort(keys, new SortByKey());
			Collections.sort(matchs, new SortByMatchsKey());
			numbers.setText(matchs.size()+"场）");
			getChange();
		}else{
			numbers.setText("0场）");
		}
	}
	public void doDateBefore(){}
	public void getChange(){//改变了比赛内容
		getSingle();//判断单关
		if(getChuan()){//更新串关模式
			setTextViewNum();//更新注数
			setTextViewMoney();//更新投注金额
			setTextViewMoneyFanWeiMother(true);
		}else{
			realTime.setText("1");
			allTime.setText("1倍0注0元");
			String moneyStr = "<html><font color=\"#333333\">预测奖金:"
					+"</font><font color=\"#FF3232\">0"
					+"</font><font color=\"#333333\">元"
					+ "</font></html>";
			allMoney.setText(Html.fromHtml(moneyStr));
		}
		setTextViewDan(chuan1.size()>0?chuan1.get(chuan1.size()-1):null);//更新胆数
		myListAdapter.notifyDataSetChanged();
	}
	public void getSingle(){}
	public boolean getChuan(){return false;}
	public boolean getChuanBase(int matchCount){
		if(matchs.size()>=2||(isSingle&&matchs.size()>0)){
			chuan1 = JCLQUtil.getJCLQUtil().getMultiplePassType(matchCount);
			if(isSingle){
				chuan1.add(0,new PassType2().PassTypeArr[0]);
			}
			chuanm = JCLQUtil.getJCLQUtil().getNormalPassType(matchCount);
			if(chuan1.size()==1){
				choosechuanguan.setClickable(false);
				rightarrow1.setVisibility(View.INVISIBLE);
			}else{
				choosechuanguan.setClickable(true);
				rightarrow1.setVisibility(View.VISIBLE);
			}
			ziyouPT = new ArrayList<>();
			hunhePT = new ArrayList<>();
			if(chuan1.size()>0){
				choosechuanguantext.setText(chuan1.get(chuan1.size()-1).getText());
			}
			return true;
		}else{
			ziyouPT.clear();
			hunhePT.clear();
			chuan1.clear();
			chuanm.clear();
			allNumInt = 0;
			allMoneyInt = 0;
			choosechuanguantext.setText("过关方式");
			return false;
		}
	}
	public void setTextViewNum(){}
	public ArrayList<PassTypeEach> setTextViewNumBase(){//重新计算注数	
		allNumInt = 0;
		ArrayList<PassTypeEach> chuan = new ArrayList<PassTypeEach>();
		if(ziyouPT!=null&&ziyouPT.size()>0){
			chuan = ziyouPT;
		}else if(hunhePT!=null&&hunhePT.size()>0){
			chuan = hunhePT;
		}else{
			chuan.add(chuan1.get(chuan1.size()-1));	
		}	
		return chuan;
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
		String numberStr3 = (TextUtils.isEmpty(timeStr)?"1":timeStr)
				+"倍"
				+allNumInt+"注"
				+allMoneyInt+"元";
		allTime.setText(numberStr3);
	}
	public void setTextViewMoneyFanWeiMother(boolean needChange){}
	public void setTextViewDan(PassTypeEach chuan){//重新计算胆
		if((hunhePT!=null&&hunhePT.size()>0)||matchs.size()<=2){//单关或者混合投注
			danMaxCount = 0;
		}else{	
			danMaxCount = JCLQUtil.getJCLQUtil().getCanShedanNum(chuan, matchs.size());					
		}
		if((danCount>danMaxCount||danMaxCount==0||danCount==0)){
			danCount = 0;
			for(int i=0;i<matchs.size();i++){
				matchs.get(i).setDan(false);
			}
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		canClick = true;
		isToPay = false;
		if(isToDetil){
			isToDetil = false;
			loadchuan.setVisibility(View.GONE);
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		canClick = false;
		if(isToDetil){
			loadchuan.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nodate:
		case R.id.add:
			toAdd();
			break;
		case R.id.clear:
			dialog("清空");
			break;
		case R.id.realtimeview:
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
			number10.setTextColor(getResources().getColor(R.color.mainred));
			number10.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("10");
			break;
		case R.id.time25:
			clearTime();
			number25.setTextColor(getResources().getColor(R.color.mainred));
			number25.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("25");
			break;
		case R.id.time50:
			clearTime();
			number50.setTextColor(getResources().getColor(R.color.mainred));
			number50.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("50");
			break;
		case R.id.time100:
			clearTime();
			number100.setTextColor(getResources().getColor(R.color.mainred));
			number100.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("100");
			break;
		case R.id.time500:
			clearTime();
			number500.setTextColor(getResources().getColor(R.color.mainred));
			number500.setBackgroundResource(R.drawable.dlt_tzback_click);
			realTime.setText("500");
			break;
		case R.id.cleartime:
			if(realTime.getText().toString().length()<=1){
				realTime.setText("");
			}else{
				String realtimeStr = realTime.getText().toString();
				realTime.setText(realtimeStr.substring(0, realtimeStr.length()-1)+"");
			}
			break;
		case R.id.suretime:
			setTextViewMoney();
			setTextViewMoneyFanWeiMother(false);
			timekeybroad.setVisibility(View.GONE);
			loadtime.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			break;
		case R.id.dlt_pay:
			if((minMoney*timeInt<allMoneyInt||allMoneyInt== 0)&&isTuijian){
				ToastUtils.showShort(_this, "当前方案最低奖金小于本金，无法发起推荐");
				return;
			}
			if(matchs==null||matchs.size()==0){
				ToastUtils.showShort(_this, "您还未选择比赛");
				return;
			}
			if(matchs!=null&&matchs.size()==1&&!isSingle){
				ToastUtils.showShort(_this, "请至少选择2场");
				return;
			}
			if(matchs!=null&&matchs.size()>=1){
				if(isTongyi){
					realTime.setText(timeInt+"");
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
			}
			break;
		case R.id.load:
			if (pop!=null&&pop.isShowing()) {
				pop.dismiss();
				loadchuan.setVisibility(View.GONE);
			}
			setTextViewMoney();
			setTextViewMoneyFanWeiMother(false);
			timekeybroad.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			break;
		case R.id.load2:
			setTextViewMoney();
			setTextViewMoneyFanWeiMother(false);
			loadtime.setVisibility(View.GONE);
			timekeybroad.setVisibility(View.GONE);
			payview.setVisibility(View.VISIBLE);
			break;
		case R.id.choosechuanguan:
			if (pop == null) {
				popwindows();
			}
			if (pop.isShowing()) {
				pop.dismiss();
				loadchuan.setVisibility(View.GONE);
			} else {
				int[] location = new int[2];
				findViewById(R.id.line0).getLocationOnScreen(location);
				pop.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-pop.getHeight());
				loadchuan.setVisibility(View.VISIBLE);
				if(ziyouPT.size() == 0&&hunhePT.size() == 0){
					setChuanZiYouBack(chuan1.size());
					if("单关".equals(chuan1.get(0).getText())){//有单关存在
						setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()-1]);
					}else{
						setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()]);
					}

				}
			}
			break;
		case R.id.jiangjinyouhua:
			if(chuan1.size() == 0||hunhePT.size() > 0){
				ToastUtils.showShort(_this, "奖金优化暂不支持该过关方式");
				return ;
			}
			if(danCount>0){
				ToastUtils.showShort(_this, "奖金优化暂不支持选胆方式");
				return ;
			}
			toJJYH();
			
			break;
		case R.id.faqituijian:			
			if (popRecom == null) {
				popwindowsRecom();
			}
			if (popRecom.isShowing()) {
				popRecom.dismiss();
				loadmain.setVisibility(View.GONE);
			} else {
				popRecom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				loadmain.setVisibility(View.VISIBLE);

			}
			break;
		}
	}
	public void toJJYH(){}
	public void toAdd(){}
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
		setTextViewMoneyFanWeiMother(false);
	}
	public void popwindows() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View viewPop = inflater.inflate(R.layout.jczqaccountchooseview, null);

		TextView chuan1 = (TextView) viewPop.findViewById(R.id.chuan11);
		TextView chuan2 = (TextView) viewPop.findViewById(R.id.chuan21);
		TextView chuan3 = (TextView) viewPop.findViewById(R.id.chuan31);
		TextView chuan4 = (TextView) viewPop.findViewById(R.id.chuan41);
		TextView chuan5 = (TextView) viewPop.findViewById(R.id.chuan51);
		TextView chuan6 = (TextView) viewPop.findViewById(R.id.chuan61);
		TextView chuan7 = (TextView) viewPop.findViewById(R.id.chuan71);
		TextView chuan8 = (TextView) viewPop.findViewById(R.id.chuan81);
		TextView chuan82 = (TextView) viewPop.findViewById(R.id.chuan82);
		chuan1s = new TextView[]{chuan1,chuan2,chuan3,chuan4,chuan5,chuan6,chuan7,chuan8,chuan82};
		hunheguoguan =  viewPop.findViewById(R.id.hunheguoguantx);
		TextView chuan33 = (TextView) viewPop.findViewById(R.id.chuan33);
		TextView chuan34 = (TextView) viewPop.findViewById(R.id.chuan34);
		TextView chuan44 = (TextView) viewPop.findViewById(R.id.chuan44);
		TextView chuan45 = (TextView) viewPop.findViewById(R.id.chuan45);
		TextView chuan46 = (TextView) viewPop.findViewById(R.id.chuan46);
		TextView chuan411 = (TextView) viewPop.findViewById(R.id.chuan411);
		TextView chuan55 = (TextView) viewPop.findViewById(R.id.chuan55);
		TextView chuan56 = (TextView) viewPop.findViewById(R.id.chuan56);

		TextView chuan510 = (TextView) viewPop.findViewById(R.id.chuan510);
		TextView chuan516 = (TextView) viewPop.findViewById(R.id.chuan516);
		TextView chuan520 = (TextView) viewPop.findViewById(R.id.chuan520);
		TextView chuan526 = (TextView) viewPop.findViewById(R.id.chuan526);
		TextView chuan66 = (TextView) viewPop.findViewById(R.id.chuan66);
		TextView chuan67 = (TextView) viewPop.findViewById(R.id.chuan67);
		TextView chuan615 = (TextView) viewPop.findViewById(R.id.chuan615);
		TextView chuan620 = (TextView) viewPop.findViewById(R.id.chuan620);

		TextView chuan622 = (TextView) viewPop.findViewById(R.id.chuan622);
		TextView chuan635 = (TextView) viewPop.findViewById(R.id.chuan635);
		TextView chuan642 = (TextView) viewPop.findViewById(R.id.chuan642);
		TextView chuan650 = (TextView) viewPop.findViewById(R.id.chuan650);
		TextView chuan657 = (TextView) viewPop.findViewById(R.id.chuan657);
		TextView chuan77 = (TextView) viewPop.findViewById(R.id.chuan77);
		TextView chuan78 = (TextView) viewPop.findViewById(R.id.chuan78);
		TextView chuan721 = (TextView) viewPop.findViewById(R.id.chuan721);

		TextView chuan735 = (TextView) viewPop.findViewById(R.id.chuan735);
		TextView chuan7120 = (TextView) viewPop.findViewById(R.id.chuan7120);
		TextView chuan88 = (TextView) viewPop.findViewById(R.id.chuan88);
		TextView chuan89 = (TextView) viewPop.findViewById(R.id.chuan89);
		TextView chuan828 = (TextView) viewPop.findViewById(R.id.chuan828);
		TextView chuan856 = (TextView) viewPop.findViewById(R.id.chuan856);
		TextView chuan870 = (TextView) viewPop.findViewById(R.id.chuan870);
		TextView chuan8247 = (TextView) viewPop.findViewById(R.id.chuan8247);
		chuanNs = new TextView[]{
				chuan33,chuan34,chuan44,chuan45,chuan46,chuan411,chuan55,chuan56,
				chuan510,chuan516,chuan520,chuan526,chuan66,chuan67,chuan615,chuan620,
				chuan622,chuan635,chuan642,chuan650,chuan657,chuan77,chuan78,chuan721,
				chuan735,chuan7120,chuan88,chuan89,chuan828,chuan856,chuan870,chuan8247};

		// 创建PopupWindow对象
		pop = new PopupWindow(viewPop, LayoutParams.MATCH_PARENT,
				600, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);

		pop.setOnDismissListener(new poponDismissListener());
	}	
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			loadchuan.setVisibility(View.GONE);
		}
	}
	public void popwindowsRecom() {

		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View viewPop = inflater.inflate(R.layout.tuijian, null);
		popRecom = new PopupWindow(this);  
		popRecom.setContentView(viewPop);  
		popRecom.setHeight(LayoutParams.WRAP_CONTENT);  
		popRecom.setWidth(LayoutParams.MATCH_PARENT);
		popRecom.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final EditText title = (EditText) viewPop.findViewById(R.id.tuijiantitle);
		final EditText content = (EditText) viewPop.findViewById(R.id.tuijiancontent);
		final TextView notuijian = (TextView) viewPop.findViewById(R.id.notuijian);
		final View tuijianview = viewPop.findViewById(R.id.tuijianview);
		final TextView tuijian = (TextView) viewPop.findViewById(R.id.tuijian);
		notuijian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isTuijianProvisional = false;
				tuijianview.setVisibility(View.GONE);	
				
				int right = notuijian.getPaddingRight();  
				int top = notuijian.getPaddingTop();  
				int bottom = notuijian.getPaddingBottom();  
				int left = notuijian.getPaddingLeft();  
				notuijian.setTextColor(Color.WHITE);
				notuijian.setBackgroundResource(R.drawable.cornernonelinefull_redline_redfull);
				notuijian.setPadding(left,top,right,bottom);  

				int right2 = tuijian.getPaddingRight();  
				int top2= tuijian.getPaddingTop();  
				int bottom2 = tuijian.getPaddingBottom();  
				int left2 = tuijian.getPaddingLeft();  
				tuijian.setTextColor(Color.BLACK);
				tuijian.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
				tuijian.setPadding(left2,top2,right2,bottom2);  
			}
		});
		tuijian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isTuijianProvisional = true;
				tuijianview.setVisibility(View.VISIBLE);
				int right2 = tuijian.getPaddingRight();  
				int top2= tuijian.getPaddingTop();  
				int bottom2 = tuijian.getPaddingBottom();  
				int left2 = tuijian.getPaddingLeft(); 
				tuijian.setTextColor(Color.WHITE);
				tuijian.setBackgroundResource(R.drawable.cornernonelinefull_redline_redfull);
				tuijian.setPadding(left2,top2,right2,bottom2);  

				int right = notuijian.getPaddingRight();  
				int top = notuijian.getPaddingTop();  
				int bottom = notuijian.getPaddingBottom();  
				int left = notuijian.getPaddingLeft();  
				notuijian.setTextColor(Color.BLACK);
				notuijian.setBackgroundResource(R.drawable.cornernonelinefull_greyline_nofull);
				notuijian.setPadding(left,top,right,bottom);  
			}
		});
		View sure = viewPop.findViewById(R.id.sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTuijianProvisional){
					if(cannull){
						tuijianTiltle = "";
						tuijianContent = "";
						isTuijian = true;
						faqituijiantxt.setText("发起推荐");
					}else{
						tuijianTiltle = title.getText().toString().trim();
						tuijianContent = content.getText().toString().trim();
						if(TextUtils.isEmpty(tuijianTiltle)){
							ToastUtils.showShort(_this, "请填写推荐标题");
							return;
						}
						if(!Util.textNameTemp3(tuijianTiltle)){
							ToastUtils.showShort(_this, "不要输入特殊字符");
							return;
						}
						if(tuijianTiltle.length()<5||tuijianTiltle.length()>15){
							ToastUtils.showShort(_this, "推荐标题字数范围5-15内");
							return;
						}
						if(TextUtils.isEmpty(tuijianContent)){
							ToastUtils.showShort(_this, "请填写推荐内容");
							return;
						}
						if(tuijianContent.length()<5||tuijianContent.length()>100){
							ToastUtils.showShort(_this, "推荐内字数范围5-100内");
							return;
						}
						if(!Util.textNameTemp3(tuijianContent)){
							ToastUtils.showShort(_this, "不要输入特殊字符");
							return;
						}
						isTuijian = true;
						faqituijiantxt.setText("发起推荐");
					}
				}else{
					isTuijian = false;
					faqituijiantxt.setText("不发起推荐");
				}

				popRecom.dismiss();
				loadmain.setVisibility(View.GONE);
			}
		});
		CheckBox noedit = (CheckBox) viewPop.findViewById(R.id.noedit);
		noedit.setOnCheckedChangeListener(new OnCheckedChangeListener(){  
			@Override  
			public void onCheckedChanged(CompoundButton button, boolean isChecked){  
				if(isChecked){  
					cannull = true;
				} else{  
					cannull = false;
				}
			}  
		}); 
		// 需要设置一下此参数，点击外边可消失
		popRecom.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		popRecom.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		popRecom.setFocusable(true);

		popRecom.setOnDismissListener(new popoRecomDismissListener());
	}
	class popoRecomDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			loadmain.setVisibility(View.GONE);
		}
	}
	public void xuanguan(View v){
		switch(v.getId()){
		case R.id.chuan11:
			setZiYou(pyMain.PassTypeArr[0],v);
			break;
		case R.id.chuan21:
			setZiYou(pyMain.PassTypeArr[1],v);
			break;
		case R.id.chuan31:
			setZiYou(pyMain.PassTypeArr[2],v);
			break;
		case R.id.chuan41:
			setZiYou(pyMain.PassTypeArr[5],v);
			break;
		case R.id.chuan51:
			setZiYou(pyMain.PassTypeArr[10],v);
			break;
		case R.id.chuan61:
			setZiYou(pyMain.PassTypeArr[17],v);
			break;
		case R.id.chuan71:
			setZiYou(pyMain.PassTypeArr[27],v);
			break;
		case R.id.chuan81:
			setZiYou(pyMain.PassTypeArr[33],v);
			break;
		case R.id.chuan33:
			setHunhe(pyMain.PassTypeArr[3],v);
			break;
		case R.id.chuan34:
			setHunhe(pyMain.PassTypeArr[4],v);
			break;
		case R.id.chuan44:
			setHunhe(pyMain.PassTypeArr[6],v);
			break;
		case R.id.chuan45:
			setHunhe(pyMain.PassTypeArr[7],v);
			break;
		case R.id.chuan46:
			setHunhe(pyMain.PassTypeArr[8],v);
			break;
		case R.id.chuan411:
			setHunhe(pyMain.PassTypeArr[9],v);
			break;
		case R.id.chuan55:
			setHunhe(pyMain.PassTypeArr[11],v);
			break;
		case R.id.chuan56:
			setHunhe(pyMain.PassTypeArr[12],v);
			break;
		case R.id.chuan510:
			setHunhe(pyMain.PassTypeArr[13],v);
			break;
		case R.id.chuan516:
			setHunhe(pyMain.PassTypeArr[14],v);
			break;
		case R.id.chuan520:
			setHunhe(pyMain.PassTypeArr[15],v);
			break;
		case R.id.chuan526:
			setHunhe(pyMain.PassTypeArr[16],v);
			break;
		case R.id.chuan66:
			setHunhe(pyMain.PassTypeArr[18],v);
			break;
		case R.id.chuan67:
			setHunhe(pyMain.PassTypeArr[19],v);
			break;
		case R.id.chuan615:
			setHunhe(pyMain.PassTypeArr[20],v);
			break;
		case R.id.chuan620:
			setHunhe(pyMain.PassTypeArr[21],v);
			break;
		case R.id.chuan622:
			setHunhe(pyMain.PassTypeArr[22],v);
			break;
		case R.id.chuan635:
			setHunhe(pyMain.PassTypeArr[23],v);
			break;
		case R.id.chuan642:
			setHunhe(pyMain.PassTypeArr[24],v);
			break;
		case R.id.chuan650:
			setHunhe(pyMain.PassTypeArr[25],v);
			break;
		case R.id.chuan657:
			setHunhe(pyMain.PassTypeArr[26],v);
			break;
		case R.id.chuan77:
			setHunhe(pyMain.PassTypeArr[28],v);
			break;
		case R.id.chuan78:
			setHunhe(pyMain.PassTypeArr[29],v);
			break;
		case R.id.chuan721:
			setHunhe(pyMain.PassTypeArr[30],v);
			break;
		case R.id.chuan735:
			setHunhe(pyMain.PassTypeArr[31],v);
			break;
		case R.id.chuan7120:
			setHunhe(pyMain.PassTypeArr[32],v);
			break;
		case R.id.chuan88:
			setHunhe(pyMain.PassTypeArr[34],v);
			break;
		case R.id.chuan89:
			setHunhe(pyMain.PassTypeArr[35],v);
			break;
		case R.id.chuan828:
			setHunhe(pyMain.PassTypeArr[36],v);
			break;
		case R.id.chuan856:
			setHunhe(pyMain.PassTypeArr[37],v);
			break;
		case R.id.chuan870:
			setHunhe(pyMain.PassTypeArr[38],v);
			break;
		case R.id.chuan8247:
			setHunhe(pyMain.PassTypeArr[39],v);
			break;
		}
		setTextViewDan(ziyouPT.size()<=0?null:ziyouPT.get(0));//更新胆数
		myListAdapter.notifyDataSetChanged();
		setTextViewNum();//更新注数
		setTextViewMoney();//更新投注金额
		setTextViewMoneyFanWeiMother(true);
	}
	public void setZiYou(PassTypeEach contain,View v){
		boolean has = false;
		for(int i = 0;i<ziyouPT.size();i++){
			if(ziyouPT.get(i).getText().equals(contain.getText())){
				ziyouPT.remove(i);
				has = true;
				break;
			}
		}
		if(has){
			setColorBack(v,false);
			if(ziyouPT.size() == 0
					&&hunhePT.size() == 0){
				if("单关".equals(chuan1.get(0).getText())){//有单关存在
					setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()-1]);
				}else{
					setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()]);
				}
			}
		}else{
			if(hunhePT.size()>=0){//选中一个自由，则混合全部清空
				setChuanHunHeBack(chuanm.size());
				hunhePT.clear();
			}
			ziyouPT.add(contain);
			setColorBack(v,true);
		}
		setTextChuan();
	}
	public void setHunhe(PassTypeEach contain,View v){
		boolean has = false;
		for(int i = 0;i<hunhePT.size();i++){
			if(hunhePT.get(i).getText().equals(contain.getText())){
				hunhePT.remove(i);
				has = true;
				break;
			}
		}
		if(has){
			setColorBack(v,false);
			if(ziyouPT.size() == 0
					&&hunhePT.size() == 0){
				if("单关".equals(chuan1.get(0).getText())){//有单关存在
					setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()-1]);
				}else{
					setZiYou(chuan1.get(chuan1.size()-1),chuan1s[chuan1.size()]);
				}
			}
		}else{
			if(ziyouPT.size()>0){
				setChuanZiYouBack(chuan1.size());
				ziyouPT.clear();
			}
			if(hunhePT.size()>0){//选中一个混合，先将其他混合
				setChuanHunHeBack(chuanm.size());
				hunhePT.clear();
			}
			hunhePT.add(contain);
			setColorBack(v,true);//混合种没有则添加
		}
		setTextChuan();
	}
	public void setChuanZiYouBack(int size){
		if("单关".equals(chuan1.get(0).getText())){//有单关存在
			chuan1s[0].setVisibility(View.VISIBLE);
			setColorBack(chuan1s[0],false);
		}else{
			chuan1s[0].setVisibility(View.GONE);
			size = size+1;
		}
		for(int i = 1;i<size;i++){
			chuan1s[i].setVisibility(View.VISIBLE);
			setColorBack(chuan1s[i],false);
		}
		for(int j = size;j<9;j++){
			if(size<=5){
				if(j<5){
					chuan1s[j].setVisibility(View.INVISIBLE);
				}else{
					chuan1s[j].setVisibility(View.GONE);
				}
			}
			if(size>5){
				if(j<9){
					chuan1s[j].setVisibility(View.INVISIBLE);
				}
			}
		}
	}
	public void setChuanHunHeBack(int size){
		for(int i = 0;i<size;i++){
			chuanNs[i].setVisibility(View.VISIBLE);
			setColorBack(chuanNs[i],false);
		}
		if(size<=0){
			hunheguoguan.setVisibility(View.INVISIBLE);
		}else{
			hunheguoguan.setVisibility(View.VISIBLE);
		}
		for(int j = size;j<32;j++){
			if(size<=4){
				if(j<4){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>4&&size<=8){
				if(j<8){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>8&&size<=12){
				if(j<12){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>12&&size<=16){
				if(j<16){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>16&&size<=20){
				if(j<20){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>20&&size<=24){
				if(j<24){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>24&&size<=28){
				if(j<28){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
			if(size>28&&size<=32){
				if(j<32){
					chuanNs[j].setVisibility(View.INVISIBLE);
				}else{
					chuanNs[j].setVisibility(View.GONE);
				}
			}
		}
	}
	public void setColorBack(View v,boolean isChoosed){
		if(isChoosed){
			((TextView)v).setTextColor(getResources().getColor(R.color.mainred));
			v.setBackgroundResource(R.mipmap.chuaguanchoose);
		}else{
			((TextView)v).setTextColor(Color.parseColor("#888888"));
			v.setBackgroundResource(R.mipmap.chuanguan);
		}
	}
	public void setTextChuan(){
		if(ziyouPT.size()>0){
			StringBuffer TextChuanBuffer = new StringBuffer();
			Collections.sort(ziyouPT, new SortByChuanName()); 
			for(PassTypeEach pte:ziyouPT){
				TextChuanBuffer.append(pte.getText()).append(",");
			}
			String ziyouStr = TextChuanBuffer.toString();
			choosechuanguantext.setText(ziyouStr.substring(0, ziyouStr.length()-1));	
		}else{
			choosechuanguantext.setText(hunhePT.get(0).getText());	
		}
	}
	public void toPayMoney(){}
	public void toPayMoneyBase(String wanfa,int lotteryId){
		if(isToPay){
			return;
		}else{
			isToPay = true;
		}
		if(allNumInt == 0){
			ToastUtils.showShort(_this, "投注金额不能为0");
			return;
		}
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		User user = MyDbUtils.getCurrentUser();
		ArrayList<Ticket> tickets = new ArrayList<>();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());	
		t.setwLotteryId(lotteryId+"");
		t.setMode("0");
		t.setPeriodNumber("");
		t.setPlayType(wanfa);
		if(hunhePT!=null&&hunhePT.size()>0){
			t.setPassType(hunhePT.get(0).getPassTypeValue()+"");
			t.setType("1");
		}else if(ziyouPT!=null&&ziyouPT.size()>0){
			t.setType("2");
			StringBuffer passType = new StringBuffer();
			for(PassTypeEach pt:ziyouPT){
				passType.append(pt.getPassTypeValue()+",");
			}
			t.setPassType(passType.toString().substring(0,passType.toString().length()-1));
		}else{
			t.setType("2");
			t.setPassType(chuan1.get(chuan1.size()-1).getPassTypeValue()+"");	
		}

		t.setMultiple(timeInt+"");
		t.setSumNum(allNumInt+"");
		t.setTotalAmount(allNumInt*2*timeInt+"");
		t.setHongBaoId("");
		t.setBestMaxPrize(df.format(maxMoney*timeInt));
		t.setBestMinPrize(df.format(minMoney*timeInt));
		t.setAreeCopy(isTuijian?"true":"false");
		t.setRecommendTitle(tuijianTiltle);
		t.setRecommendContent(tuijianContent);

		Gson aGson = new Gson();
		String json2 = aGson.toJson(choosedScroeBean());
		t.setNumber("{\"items\":"+json2+"}");

		tickets.add(t);
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",lotteryId);
		bundle.putLong("totalaccount",allMoneyInt);
		bundle.putLong("timeInt",timeInt);
		bundle.putBoolean("useRedPacket",true);

		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);
	}
	public void dialog(String info) {
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText(info);
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("您确定要清空当前的投注内容？");
		TextView ok = (TextView) window.findViewById(R.id.cancle);
		TextView cancle = (TextView) window.findViewById(R.id.ok);
		ok.setText("确定");
		cancle.setText("取消");
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				clearTouZhu();
			}
		});
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

	public void clearTouZhu(){
		mapChoosed.clear();
		keys.clear();
		matchs.clear();
		myListAdapter.notifyDataSetChanged();
		//setTextViewValue();
		numbers.setText("0场）");
		realTime.setText("1");
		allTime.setText("1倍0注0元");

		String numberStr2 = "<html><font color=\"#333333\">预测奖金:"
				+"</font><font color=\"#FF3232\">0"
				+"</font><font color=\"#333333\">元"
				+ "</font></html>";
		allMoney.setText(Html.fromHtml(numberStr2));
		choosechuanguantext.setText("过关方式");	
		numbers.setText("0场）");
		choosechuanguan.setClickable(false);
		rightarrow1.setVisibility(View.INVISIBLE);
	}
	public ArrayList<JczqChoosedScroeBean> choosedScroeBean(){return null;}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(mapChoosed!=null&&
			mapChoosed.size()>0){
				dialog();
				return true;
			}else{
				finish();
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TOADD:
				Bundle add = data.getExtras();
				mapChoosed  = (HashMap) add.getSerializable("matchs");
				boolean change  = add.getBoolean("change");
				if(change){
					_this.finish();
				}else{
					keys.clear();
					matchs.clear();
					doDate();
				}
				break;	
			case TODETAIL:
				Bundle b = data.getExtras();
				Match match = (Match)b.getSerializable("match");
				int motherkay = b.getInt("motherkay");
				int childkey = b.getInt("childkey");
				if(match.getCheckNumber().size()>0){//选了这个比赛
					mapChoosed.put(motherkay+"+"+childkey, match);	
				}
				//没选这个比赛，但是在已选名单中
				if(match.getCheckNumber().size()<=0&&mapChoosed.containsKey(motherkay+"+"+childkey)){
					mapChoosed.remove(motherkay+"+"+childkey);
				}
				keys.clear();
				matchs.clear();
				doDate();
				break;
			case LOGIN:	
				toPayMoney();
				break;
			case HAVEPAY:	
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				if(!isOnlyClose){
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					if (!closeMainActivity) {
						toHavePay();
					}
				}
				_this.finish();
				break;		
			default:
				break;
			}
		}
	}
	public void toHavePay(){}
}
