package com.cshen.tiyu.activity.lottery.Fast3;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.lottery.cai115.ZNZHMainActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.pay.TZXYActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.JixuanListView;
import com.cshen.tiyu.widget.JixuanListView.OnJixuanListener;


public class Fast3AccountListActivity  extends BaseActivity implements OnClickListener{
	public static final int HAVEPAY = 0;
	public static final int ZIXUANNUMBER = 1;//自选
	public static final int CHANGENUMBER = 2;//号码详情
	public static final int LOGIN = 3;//登录
	public static final int ZNZH = 4;//登录
	public int state = 0;//0付钱。1智能追号
	private Fast3AccountListActivity  _this;
	private ImageView back;
	private View zixuan,jixuan1,jixuan5;//自选，机选1，机选5
	private View clear;//清除
	private View nodate;//下拉机选
	private View mHeaderView;
	private TextView tvTitle;
	private ImageView ivArrow;
	private int mHeaderViewHeight;
	private int startY = -1;// 滑动起点的y坐标
	private RotateAnimation animDown;
	private static final int STATE_PULL_REFRESH = 0;// 下拉机选
	private static final int STATE_REFRESHING = 2;// 正在机选
	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
	private JixuanListView listview;
	private MyListAdapter myListAdapter = new MyListAdapter();;
	private EditText realLong,realTime;//追期，倍数
	private CheckBox provisions,moreMon;//条款，追加
	private ImageView addLong,minusLong,addTime,minusTime;
	private TextView allMoney,allTime,pay,tv_tzxy,znzh;//总钱数，总注倍期数，付款，投注规则

	private AlertDialog  alertDialog ;
	boolean getting = false;
	private int longInt = 0,timeInt = 0,allNumInt = 0;//连买期数，投注倍数，总注数
	private long  allMoneyInt = 0;//总金额
	private boolean isTongyi = true;//条款同意与否
	private ArrayList<NumberFast> choosedNumbers = new ArrayList<>();//从缓存中来的
	private boolean isMore = true;//中奖停止追号
	private int wanfa,dantuo = 1;
	private View long10,long30,long60,long154;
	private TextView title10,title30,title60,title154;
	private TextView content10,content30,content60,content154;
	private TextView choosed10,choosed30,choosed60,choosed154;
	private TextView line1030,line3060,line60154;
	private ArrayList<Ticket> tickets = new ArrayList<Ticket>();// 投注内容     

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fast3account_list);
		_this = this;
		initView();
		initdata(); // 获取数据
	}
	private void initView() {
		back = (ImageView) findViewById(R.id.iv_back);
		back.setOnClickListener(this);


		zixuan = findViewById(R.id.dlt_zixuan);
		zixuan.setOnClickListener(this);
		jixuan1 = findViewById(R.id.dlt_jixuan1);
		jixuan1.setOnClickListener(this);
		jixuan5 = findViewById(R.id.dlt_jixuan5);
		jixuan5.setOnClickListener(this);
		nodate  = findViewById(R.id.nodate);
		nodate.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				setEvent(view,ev) ;
				return true;
			}
		});
		listview = (JixuanListView) findViewById(R.id.dlt_listview);
		listview.setAdapter(myListAdapter);
		listview.setOnJixuanListener(new OnJixuanListener() {

			@Override
			public void onJixuan() {
				// TODO 自动生成的方法存根
				NumberFast NumberFast =  ChooseUtilFast3.getChooseUtil().getRandom(wanfa);
				choosedNumbers.add(0,NumberFast);
				myListAdapter.notifyDataSetChanged();
				updateView1();
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if(position<choosedNumbers.size()){
					Intent intent = new Intent(_this, Fast3MainActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putInt("position",position);
					bundle.putString("state", "change");
					bundle.putSerializable("numberfast", choosedNumbers.get(position));
					bundle.putSerializable("wanfa", choosedNumbers.get(position).getPlayType());
					bundle.putSerializable("dantuo", choosedNumbers.get(position).getMode());
					intent.putExtras(bundle);
					startActivityForResult(intent, CHANGENUMBER);
				}
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				dialogDelete(position);
				return true;
			}


		});
		allMoney =  (TextView) findViewById(R.id.allmoney);
		allTime =  (TextView) findViewById(R.id.alltime);
		EditChangedListener editChange = new EditChangedListener();
		realLong =  (EditText) findViewById(R.id.reallong);
		realLong.addTextChangedListener(editChange);
		realTime =  (EditText) findViewById(R.id.realtime);
		realTime.addTextChangedListener(editChange);

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
		pay =  (TextView) findViewById(R.id.dlt_pay);
		pay.setOnClickListener(this);

		minusLong = (ImageView)findViewById(R.id.minuslong);
		minusLong.setOnClickListener(this);
		addLong = (ImageView)findViewById(R.id.addlong);
		addLong.setOnClickListener(this);
		minusTime = (ImageView)findViewById(R.id.minustime);
		minusTime.setOnClickListener(this);
		addTime = (ImageView)findViewById(R.id.addtime);
		addTime.setOnClickListener(this);


		long10 = findViewById(R.id.view10);
		long10.setOnClickListener(this);
		title10 = (TextView) findViewById(R.id.title10);
		content10 = (TextView) findViewById(R.id.content10);
		choosed10 = (TextView) findViewById(R.id.choosed10);
		line1030 = (TextView) findViewById(R.id.line1030);

		long30 = findViewById(R.id.view30);
		long30.setOnClickListener(this);
		title30 = (TextView) findViewById(R.id.title30);
		content30 = (TextView) findViewById(R.id.content30);
		choosed30 = (TextView) findViewById(R.id.choosed30);
		line3060 = (TextView) findViewById(R.id.line3060);

		long60 = findViewById(R.id.view60);
		long60.setOnClickListener(this);
		title60 = (TextView) findViewById(R.id.title60);
		content60 = (TextView) findViewById(R.id.content60);
		choosed60 = (TextView) findViewById(R.id.choosed60);
		line60154 = (TextView) findViewById(R.id.line60154);

		long154 = findViewById(R.id.view154);
		long154.setOnClickListener(this);
		title154 = (TextView) findViewById(R.id.title154);
		content154 = (TextView) findViewById(R.id.content154);
		choosed154 = (TextView) findViewById(R.id.choosed154);


		initHeaderView();
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
		znzh= (TextView)findViewById(R.id.znzh);
		znzh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!PreferenceUtil.getBoolean(_this,"hasLogin") ) {
					state = 1;
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					startActivityForResult(intentLogin,LOGIN);
				} else {
					toZNZH();					
				}
			}
		});

	}
	public void toZNZH(){
		if(allNumInt!=1){
			ToastUtils.showShort(_this, "智能追号不支持多注");
			return;			
		}
		ChooseUtilFast3.getChooseUtil().maymoney(choosedNumbers.get(0));
		int	jiangjin = ChooseUtilFast3.getChooseUtil().getJiangjin();			

		Intent intent = new Intent(_this, ZNZHMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putInt("jiangjin", jiangjin);
		bundle.putInt("wanfa", ConstantsBase.Fast3);
		bundle.putSerializable("ticketarray", getTicket());
		intent.putExtras(bundle);
		startActivityForResult(intent,ZNZH);
	}
	private void initdata() {
		Bundle b = getIntent().getExtras();
		if(b.getBoolean("clear")){
			choosedNumbers = new ArrayList<NumberFast>();
		}
		NumberFast NumberFast = (NumberFast)b.getSerializable("numberfast");
		choosedNumbers  = (ArrayList<NumberFast>)b.getSerializable("numberfastList");
		if(choosedNumbers == null){
			choosedNumbers = new ArrayList<NumberFast>();
		}else{
			if(b.getBoolean("frommain")){
				ToastUtils.showShort(_this, "这是您上次保存的号码");
			}
		}
		if(NumberFast!=null){
			choosedNumbers.add(NumberFast);
		}
		if(choosedNumbers!=null&&choosedNumbers.size()>0){
			dantuo = choosedNumbers.get(0).getMode();
			wanfa  = choosedNumbers.get(0).getPlayType();
		}else{
			wanfa = 1;
			dantuo = 1;
		}
		listview.setWanFa(dantuo);
		myListAdapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			if(choosedNumbers!=null&&
			choosedNumbers.size()>0){
				dialog();
			}else{
				finish();
			}
			break;
		case R.id.dlt_zixuan:
			Intent intent = new Intent(_this, Fast3MainActivity.class);
			Bundle bundle = new Bundle(); 
			bundle.putString("state", "add");
			bundle.putSerializable("wanfa", wanfa);
			bundle.putSerializable("dantuo", dantuo);
			intent.putExtras(bundle);
			startActivityForResult(intent, ZIXUANNUMBER);			
			break;
		case R.id.dlt_jixuan1:
			if(dantuo == 0){
				ToastUtils.showShort(_this, "胆拖玩法暂不支持机选");
				return;	
			}
			if(wanfa  == ConstantsBase.SANTONGHAOTONG||
					wanfa  == ConstantsBase.SANLIANHAO){
				ToastUtils.showShort(_this, "通选玩法暂不支持机选");
				return;	
			}
			NumberFast NumberFast = ChooseUtilFast3.getChooseUtil().getRandom(wanfa);
	
			choosedNumbers.add(0,NumberFast);
			myListAdapter.notifyDataSetChanged();
			updateView1();
			break;
		case R.id.dlt_jixuan5:
			if(dantuo == 0){
				ToastUtils.showShort(_this, "胆拖玩法暂不支持机选");
				return;	
			}
			if(wanfa  == ConstantsBase.SANTONGHAOTONG||
					wanfa  == ConstantsBase.SANLIANHAO){
				ToastUtils.showShort(_this, "通选玩法暂不支持机选");
				return;	
			}
			for(int i=0;i<5;i++){
				NumberFast NumberFast0 =  
						ChooseUtilFast3.getChooseUtil().getRandom(wanfa);
				choosedNumbers.add(0,NumberFast0);
			}
			myListAdapter.notifyDataSetChanged();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					updateView5();
				}
			},0);
	
			break;
		case R.id.clear:
			dialog("清空");
			break;
		case R.id.dlt_pay:
			if(choosedNumbers!=null&&choosedNumbers.size()>0){
				if(isTongyi){
					realLong.setText(""+longInt);
					realTime.setText(""+timeInt);
					if (!PreferenceUtil.getBoolean(_this,"hasLogin") ) {
						state = 0;
						Intent intentLogin = new Intent(_this, LoginActivity.class); 
						intentLogin.putExtra("requestName", "intentLogin");
						startActivityForResult(intentLogin,LOGIN);
					} else {
						if(!getting){
							getting = true;
							checkPeriodNumber();
						}
					}
				}else{
					ToastUtils.showShort(_this, "必须同意后才能购买");
				}
			}else{
				ToastUtils.showShort(_this, "请至少选择1注");
			}
			break;
		case R.id.minuslong:
			minusaddLong(0);
			break;
		case R.id.addlong:
			minusaddLong(1);
			break;
		case R.id.minustime:
			minusaddTime(0);
			break;
		case R.id.addtime:
			minusaddTime(1);
			break;
	
		case R.id.view10:
			realLong.setText("10");
			longInt = 10;
			setTextViewValue();
			break;
		case R.id.view30:
			realLong.setText("30");
			longInt = 30;
			setTextViewValue();
			break;
		case R.id.view60:
			realLong.setText("60");
			longInt = 60;
			setTextViewValue();
			break;
		case R.id.view154:
			realLong.setText("154");
			longInt = 154;
			setTextViewValue();
			break;
		}
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
			}
		}catch(Exception e){
			longInt = 1;
			e.printStackTrace();
		}
		setTextViewValue();
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
			}if(type == 1){//0add
				timeInt++;
				realTime.setText(timeInt+"");
			}
		}catch(Exception e){
			timeInt = 1;
			e.printStackTrace();
		}
		setTextViewValue();
	}
	public void setDefaultLong(int longValue){
		title10.setTextColor(Color.parseColor("#ffffff"));
		content10.setTextColor(Color.parseColor("#ffffff"));
		choosed10.setVisibility(View.GONE);
		line1030.setVisibility(View.VISIBLE);

		title30.setTextColor(Color.parseColor("#ffffff"));
		content30.setTextColor(Color.parseColor("#ffffff"));
		choosed30.setVisibility(View.GONE);
		line3060.setVisibility(View.VISIBLE);

		title60.setTextColor(Color.parseColor("#ffffff"));
		content60.setTextColor(Color.parseColor("#ffffff"));
		choosed60.setVisibility(View.GONE);
		line60154.setVisibility(View.VISIBLE);

		title154.setTextColor(Color.parseColor("#ffffff"));
		content154.setTextColor(Color.parseColor("#ffffff"));
		choosed154.setVisibility(View.GONE);

		if(longValue == 10){
			title10.setTextColor(Color.parseColor("#ffc600"));
			content10.setTextColor(Color.parseColor("#ffc600"));
			choosed10.setVisibility(View.VISIBLE);
			line1030.setVisibility(View.GONE);
		}
		if(longValue == 30){
			title30.setTextColor(Color.parseColor("#ffc600"));
			content30.setTextColor(Color.parseColor("#ffc600"));
			choosed30.setVisibility(View.VISIBLE);
			line1030.setVisibility(View.GONE);
			line3060.setVisibility(View.GONE);
		}
		if(longValue == 60){
			title60.setTextColor(Color.parseColor("#ffc600"));
			content60.setTextColor(Color.parseColor("#ffc600"));
			choosed60.setVisibility(View.VISIBLE);
			line3060.setVisibility(View.GONE);
			line60154.setVisibility(View.GONE);
		}
		if(longValue == 154){
			title154.setTextColor(Color.parseColor("#ffc600"));
			content154.setTextColor(Color.parseColor("#ffc600"));
			choosed154.setVisibility(View.VISIBLE);
			line60154.setVisibility(View.GONE);
		}
	}
	public boolean  setEvent(View view, MotionEvent ev){
		if(dantuo == 0){
			ToastUtils.showShort(_this, "胆拖玩法暂不支持机选");
			return false;	
		}

		if(wanfa  == ConstantsBase.SANTONGHAOTONG||
				wanfa  == ConstantsBase.SANLIANHAO){
			ToastUtils.showShort(_this, "通选玩法暂不支持机选");
			return false;	
		}
		// TODO 自动生成的方法存根
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}
			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}
			int endY = (int) ev.getRawY();
			int dy = endY - startY;
			if (dy > 0 ) {
				int padding = dy - mHeaderViewHeight;
				if (padding > 0 ) {
					mCurrrentState = STATE_REFRESHING;
					mHeaderView.setPadding(0, 0, 0, 0);//显示
					refreshState();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrrentState == STATE_REFRESHING) {
				mCurrrentState = STATE_PULL_REFRESH;
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
				refreshState();
				return true;
			}
			break;

		default:
			break;
		}
		return false;
	}
	//计算总额和注数等
	public void setTextViewValue(){
		String longStr = realLong.getText().toString();
		String timeStr = realTime.getText().toString();
		try{
			longInt = Integer.parseInt(longStr);
			if(longInt>1){
				findViewById(R.id.realmoreview_line).setVisibility(View.VISIBLE);
				findViewById(R.id.realmoreview).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.realmoreview_line).setVisibility(View.GONE);
				findViewById(R.id.realmoreview).setVisibility(View.GONE);
			}
			if(longInt>154){
				ToastUtils.showShort(_this, "追期不能超过154期");
				realLong.setText("154");
			}if(longInt == 0){
				ToastUtils.showShort(_this, "追期不能为0");
				realLong.setText("1");
			}
		}catch(Exception e){
			longInt = 1;
			e.printStackTrace();
		}
		try{
			timeInt = Integer.parseInt(timeStr);
			if(timeInt == 0){
				ToastUtils.showShort(_this, "投注倍数不能为0");
				realTime.setText("1");
			}
		}catch(Exception e){
			timeInt = 1;
			e.printStackTrace();
		}
		allNumInt = 0;
		for(int i = 0;i<choosedNumbers.size(); i++){
			allNumInt = allNumInt+choosedNumbers.get(i).getNum();
		}
		allMoneyInt = (long)longInt*timeInt*allNumInt*2;
		allMoney.setText(allMoneyInt+"元");
		allTime.setText(allNumInt+"注"+
				(TextUtils.isEmpty(timeStr)?"1":timeStr)+"倍"+
				longInt+"期");

		setDefaultLong(longInt);
	}
	class MyListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return choosedNumbers.size();
		}

		@Override
		public NumberFast getItem(int position) {
			return choosedNumbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			setTextViewValue();
			if(choosedNumbers == null ||
					choosedNumbers.size() == 0){
				nodate.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
			}else{ 
				listview.setVisibility(View.VISIBLE);
				nodate.setVisibility(View.GONE);
			}
		}
		public void notifyDataSetChangedMore() {
			super.notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final NumberFast order = choosedNumbers.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.tz_item,null);
				holder.line = (TextView) convertView.findViewById(R.id.line);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.number.setTextColor(getResources().getColor(R.color.mainred));
				holder.sigledouble = (TextView) convertView.findViewById(R.id.sigledouble);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(order!=null){
				String numberStr = "";
				StringBuffer numbers = new StringBuffer();

				switch (wanfa) {
				case ConstantsBase.HEZHI:
					numbers.append("和值   ");
					for(int number:order.getNumbers()){
						numbers.append(number+",");
					}
					numberStr = numbers.toString().substring(0,numbers.toString().length()-1);		
					break;
				case ConstantsBase.SANTONGHAO:
					numbers.append("三同号   ");
					for(int number:order.getNumbers()){
						numbers.append(number+"");
						numbers.append(number+"");
						numbers.append(number+",");
					}
					numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			
					break;
				case ConstantsBase.SANTONGHAOTONG:
					numberStr = "三同号通选   ";
					break;
				case ConstantsBase.SANBUTONGHAO:
					if(dantuo == 1){
						numbers.append("三不同号   ");
						for(int number:order.getNumbers()){
							numbers.append(number+",");
						}
						numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			

					}else{
						StringBuffer numbers0 = new StringBuffer();
						numbers0.append("三不同号胆拖   ");
						numbers0.append("(");
						for(int number:order.getNumbers()){
							numbers0.append(number+",");
						}
						numberStr = numbers0.toString().substring(0,numbers0.toString().length()-1);			
						numbers.append(numberStr);
						numbers.append(") ");
						for(int number:order.getNumber1()){
							numbers.append(number+",");
						}
						numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			
					}
					break;
				case ConstantsBase.SANLIANHAO:
					numberStr = "三连号通选   ";
					break;

				case ConstantsBase.ERTONGHAO:
					StringBuffer numbers0 = new StringBuffer();
					numbers0.append("二同号   ");
					for(int number:order.getNumbers()){
						numbers0.append(number+"");
						numbers0.append(number+",");
					}
					numberStr = numbers0.toString().substring(0,numbers0.toString().length()-1);			
					numbers.append(numberStr);
					numbers.append("#");
					for(int number:order.getNumber1()){
						numbers.append(number+",");
					}
					numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			
					break;
				case ConstantsBase.ERTONGHAOFU:
					numbers.append("二同号复选   ");
					for(int number:order.getNumbers()){
						numbers.append(number+"");
						numbers.append(number+"*，");
					}
					numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			

					break;
				case ConstantsBase.ERBUTONGHAO:
					if(dantuo == 1){
						numbers.append("二不同号   ");
						for(int number:order.getNumbers()){
							numbers.append(number+",");
						}
						numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			

					}else{
						StringBuffer numbers1 = new StringBuffer();
						numbers1.append("二不同号胆拖   ");

						numbers1.append("(");
						for(int number:order.getNumbers()){
							numbers1.append(number+",");
						}
						numberStr = numbers1.toString().substring(0,numbers1.toString().length()-1);			
						numbers.append(numberStr);
						numbers.append(") ");
						for(int number:order.getNumber1()){
							numbers.append(number+",");
						}
						numberStr = numbers.toString().substring(0,numbers.toString().length()-1);			
					}
					break;
				}
				holder.number.setText(numberStr);
			}
			if(choosedNumbers.size()>1&&position == choosedNumbers.size()-1){
				holder.line.setVisibility(View.GONE);
			}else{
				holder.line.setVisibility(View.VISIBLE);
			}
			convertView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
			return convertView;
		}


		class ViewHolder {
			public TextView number;//数字
			public TextView sigledouble;//单式复式
			public TextView time;//倍数
			public TextView money;//金额
			public TextView line;//最后的线

		}
	}
	public void updateView1() {  
		View  viewMain= listview.getChildAt(0 - listview.getFirstVisiblePosition()+1);
		if(viewMain!=null){
			View  view= viewMain.findViewById(R.id.main);  
			if(view!=null){
				TranslateAnimation animation = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f, 
						Animation.RELATIVE_TO_SELF, 0.0f);
				animation.setDuration(1000);
				animation.setStartOffset(0);
				view.startAnimation(animation);
			}
		}

		listview.setSelection(0);
	} 
	public void updateView5() {  
		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(1000);
		animation.setStartOffset(0);
		View[] views = new View[5];
		for(int i=0;i<5;i++){
			View  viewMain= listview.getChildAt(i - listview.getFirstVisiblePosition()+1);
			if(viewMain!=null){
				View  view= viewMain.findViewById(R.id.main);  
				if(view!=null){
					views[i] = view; 
				}
			}
		}
		for(int i=0;i<5;i++){
			if(views[i]!=null){
				views[i].startAnimation(animation);
			}
		}

		listview.setSelection(0);
	} 
	public void dialog(String info) {
		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setCancelable(false);
		}
		if(!alertDialog.isShowing()){
			alertDialog.show();
		}
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
				choosedNumbers.clear();
				myListAdapter.notifyDataSetChanged();
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERSFAST3,null);
			}
		});
	}
	public void dialogDelete(final int i) {
		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setCancelable(false);
		}
		if(!alertDialog.isShowing()){
			alertDialog.show();
		}
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("确定删除");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("您确定要删除该条投注？");
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
				choosedNumbers.remove(i);
				myListAdapter.notifyDataSetChanged();
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
		title.setText("退出提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("是否清除本次选号?");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("清除");
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
		ok.setText("保存");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERSFAST3,choosedNumbers);
				ToastUtils.showShort(_this, "已保存，下次进入彩种时将自动调出");	
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
				choosedNumbers.clear();
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERSFAST3,null);
				_this.finish();	
			}
		});
	}
	void showEndTimeDialog(String oldTime,final String newTime){
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
				PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIODFAST3,newTime);			
				toPayMoney();
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
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(choosedNumbers!=null&&
			choosedNumbers.size()>0){
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
			case ZIXUANNUMBER:
				Bundle ziXuan = data.getExtras();
				if(ziXuan.getBoolean("clear")){
					choosedNumbers.clear();
				}
				if(ziXuan!=null){
					NumberFast	NumberFast = (NumberFast)ziXuan.getSerializable("numberfast");
					ArrayList<NumberFast> choosedNumber  = (ArrayList<NumberFast>)ziXuan.getSerializable("numberfastList");
					if(NumberFast!=null){
						if(NumberFast!=null){
							dantuo = NumberFast.getMode();
							wanfa  = NumberFast.getPlayType();
						}
						listview.setWanFa(dantuo);
						choosedNumbers.add(0,NumberFast);
						myListAdapter.notifyDataSetChanged();	
					}
					if(choosedNumber!=null&&choosedNumber.size()>0){
						if(choosedNumber.get(0)!=null){
							dantuo = choosedNumber.get(0).getMode();
							wanfa  = choosedNumber.get(0).getPlayType();
						}
						listview.setWanFa(dantuo);
						choosedNumbers.addAll(0, choosedNumber);
						myListAdapter.notifyDataSetChanged();	
					}
				}				
				break;
			case CHANGENUMBER:
				Bundle change = data.getExtras();
				if(change!=null){
					int position = change.getInt("position");
					NumberFast	NumberFast = (NumberFast)change.getSerializable("numberfast");
					ArrayList<NumberFast> choosedNumber  = (ArrayList<NumberFast>)change.getSerializable("numberfastList");
					
					if(NumberFast!=null){
						choosedNumbers.get(position).setNumbers(NumberFast.getNumbers());
						choosedNumbers.get(position).setNumber1(NumberFast.getNumber1());
						choosedNumbers.get(position).setNum(NumberFast.getNum());
						myListAdapter.notifyDataSetChanged();	
					}
					if(choosedNumber!=null&&choosedNumber.size()>0){
						if(choosedNumber.get(0)!=null){
							dantuo = choosedNumber.get(0).getMode();
							wanfa  = choosedNumber.get(0).getPlayType();
						}
						listview.setWanFa(dantuo);
						choosedNumbers.addAll(0, choosedNumber);
						myListAdapter.notifyDataSetChanged();	
					}	
				}				
				break;
			case LOGIN:	
				if(state == 0){
					checkPeriodNumber();
				}else{
					toZNZH();
				}
				break;
			case HAVEPAY:	
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				if(!isOnlyClose){
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					if (!closeMainActivity) {
						Intent intent = new Intent(_this, Fast3MainActivity.class);
						startActivity(intent);
					}
				}
				_this.finish();
				break;	
			case ZNZH:
				boolean isOnlyClose2 = data.getBooleanExtra("isOnlyClose", false);
				if(!isOnlyClose2){
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					if (!closeMainActivity) {
						Intent intent = new Intent(_this, Fast3MainActivity.class);
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
	class EditChangedListener implements TextWatcher {  
		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

		@Override  
		public void onTextChanged(CharSequence s, int start, int before, int count) {}  

		@Override  
		public void afterTextChanged(Editable s) {  
			setTextViewValue();
		}  
	};  
	private void checkPeriodNumber(){
		// TODO 自动生成的方法存根
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.Fast3+"", new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				getting = false;
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						String periodTimeOld = PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIODFAST3);
						String periodTimeNew = period.getPeriodNumber();
						if(TextUtils.isEmpty(periodTimeOld)){
							periodTimeOld = periodTimeNew;
							PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIODFAST3,periodTimeNew);
						}
						Long time = period.getInterval();
						if(periodTimeOld.equals(periodTimeNew)){
							if(time <= 0){
								ToastUtils.showShort(_this,periodTimeOld+"期已截止");
							}else{
								toPayMoney();
							}							
						}else{
							showEndTimeDialog(periodTimeOld,periodTimeNew);
						}
					}
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				getting = false;
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	private void toPayMoney(){
		tickets.clear();
		tickets.add(getTicket());
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",ConstantsBase.Fast3);
		bundle.putLong("totalaccount",allMoneyInt);
		if(longInt == 1){
			bundle.putBoolean("useRedPacket",true);
		}else{
			bundle.putBoolean("useRedPacket",false);
		}
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);

	}
	public Ticket getTicket(){
		String periodNumber = PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIODFAST3);	    
		User user = MyDbUtils.getCurrentUser();

		String numbers = "";
		StringBuffer number = new StringBuffer();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());
		t.setMode("0"); 
		t.setPeriodNumber(periodNumber);
		t.setWonStopOfChase(isMore+"");
		t.setIsSpecialFlag("false");
		t.setPlayTypeID(wanfa+"");
		t.setMultiple(timeInt+"");

		t.setSumNum(allNumInt+"");
		t.setTotalAmount(allNumInt*2*timeInt+"");
		t.setHongBaoId("");
		if(longInt>1){
			t.setIsChase("true");
			t.setPeriodSizeOfChase(longInt+"");
			t.setTotalCostOfChase(allNumInt*2*timeInt*longInt+""+"");
		}else{
			t.setIsChase("false");
			t.setPeriodSizeOfChase("");
			t.setTotalCostOfChase("");
		}
		for(int j=0;j<choosedNumbers.size();j++){
			NumberFast eachNumber = choosedNumbers.get(j);	
			if(eachNumber.getMode() == 0){
				//三不同号 胆码;拖码,拖码,拖码
				number.append("+"+eachNumber.getNum()+":");
				for(int i=0;i<eachNumber.getNumbers().size();i++){
					number.append(eachNumber.getNumbers().get(i)+"");
					if(i != eachNumber.getNumbers().size()-1){
						number.append(",");
					}
				}
				number.append(";");
				for(int k=0;k<eachNumber.getNumber1().size();k++){
					number.append(eachNumber.getNumber1().get(k)+"");
					if(k != eachNumber.getNumber1().size()-1){
						number.append(",");
					}
				}
			}else{
				if(eachNumber.getPlayType() == ConstantsBase.SANTONGHAO){
					number.append("+"+eachNumber.getNum()+":");
					for(int i=0;i<eachNumber.getNumbers().size();i++){
						number.append(eachNumber.getNumbers().get(i)+""+eachNumber.getNumbers().get(i)+""+eachNumber.getNumbers().get(i));
						if(i != eachNumber.getNumbers().size()-1){
							number.append(",");
						}
					}
				}else if(eachNumber.getPlayType() == ConstantsBase.ERTONGHAO){
					//二同号单选 同号,同号,同号#不同号
					number.append("+"+eachNumber.getNum()+":");
					for(int i=0;i<eachNumber.getNumbers().size();i++){
						number.append(eachNumber.getNumbers().get(i)+""+eachNumber.getNumbers().get(i)+"");
						if(i != eachNumber.getNumbers().size()-1){
							number.append(",");
						}
					}
					number.append("#");
					for(int k=0;k<eachNumber.getNumber1().size();k++){
						number.append(eachNumber.getNumber1().get(k)+"");
						if(k != eachNumber.getNumber1().size()-1){
							number.append(",");
						}
					}
				}else if(eachNumber.getPlayType() == ConstantsBase.ERTONGHAOFU){
					//二同号单选 同号,同号,同号#不同号
					number.append("+"+eachNumber.getNum()+":");
					for(int i=0;i<eachNumber.getNumbers().size();i++){
						number.append(eachNumber.getNumbers().get(i)+""+eachNumber.getNumbers().get(i));
						if(i != eachNumber.getNumbers().size()-1){
							number.append(",");
						}
					}
				}else if(eachNumber.getPlayType() == ConstantsBase.SANLIANHAO||
						eachNumber.getPlayType() == ConstantsBase.SANTONGHAOTONG){
					//三连号通选，三同号通选都传1
					number.append("+"+eachNumber.getNum()+":1");
				}else{
					number.append("+  "+eachNumber.getNum()+":");
					for(int i=0;i<eachNumber.getNumbers().size();i++){
						number.append(eachNumber.getNumbers().get(i)+""); 
						if(i != eachNumber.getNumbers().size()-1){
							number.append(",");
						}
					}				
				}

			}
		}
		numbers = number.toString().substring(1,number.toString().length()) ;
		t.setNumber(numbers);
		return t;
	}
	private void initHeaderView() {
		mHeaderView = listview.getHeaderView();
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		mHeaderView.findViewById(R.id.tv_time).setVisibility(View.GONE);

		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		mHeaderView.findViewById(R.id.pb_progress).setVisibility(View.GONE);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		initArrowAnim();
	}
	//初始化动画
	private void initArrowAnim() {
		animDown = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}

	private void refreshState() {
		// TODO 自动生成的方法存根
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉机选");
			ivArrow.setVisibility(View.INVISIBLE);
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在机选...");
			ivArrow.startAnimation(animDown);
			ivArrow.setVisibility(View.VISIBLE);
			NumberFast NumberFast =  ChooseUtilFast3.getChooseUtil().getRandom(wanfa);
			choosedNumbers.add(0,NumberFast);
			myListAdapter.notifyDataSetChanged();
			updateView1();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		if(alertDialog!=null){
			alertDialog.cancel();
			alertDialog = null;
		}
	}
}
