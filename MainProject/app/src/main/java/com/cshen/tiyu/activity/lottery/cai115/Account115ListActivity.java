package com.cshen.tiyu.activity.lottery.cai115;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
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
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.activity.pay.TZXYActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Period;
import com.cshen.tiyu.domain.PeriodResultData;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.JixuanListView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.JixuanListView.OnJixuanListener;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;


public class Account115ListActivity  extends BaseActivity implements OnClickListener{
	public static final int HAVEPAY = 0;
	public static final int ZIXUANNUMBER = 1;//自选
	public static final int CHANGENUMBER = 2;//号码详情
	public static final int LOGIN = 3;//登录
	public static final int ZNZH = 4;//登录
	public int state = 0;//0付钱。1智能追号
	private Account115ListActivity  _this;
	private TopViewLeft tv_head;
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

	private AlertDialog  alertDialog;
	private int longInt = 0,timeInt = 0,allNumInt = 0;//连买期数，投注倍数，总注数
	private long  allMoneyInt = 0;//总金额
	private boolean isTongyi = true;//条款同意与否
	private ArrayList<Number115> choosedNumbers = new ArrayList<>();//从缓存中来的
	private boolean isMore = true;//中奖停止追号
	private int numleast,wanfa,dantuo = 1;
	private View long10,long30,long60,long154;
	private TextView title10,title30,title60,title154;
	private TextView content10,content30,content60,content154;
	private ArrayList<Ticket> tickets = new ArrayList<Ticket>();// 投注内容     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account115_list);
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
				if(choosedNumbers!=null&&
						choosedNumbers.size()>0){
					dialog();
				}else{
					finish();
				}
			}
		});
		initView();
		initdata(); // 获取数据
	}
	private void initView() {
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
				Number115 number115 =  ChooseUtil.getChooseUtil().getRandom115(numleast,wanfa);
				choosedNumbers.add(0,number115);
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
					Intent intent = new Intent(_this, Main115Activity.class);
					Bundle bundle = new Bundle();   
					bundle.putInt("position",position);
					bundle.putString("state", "change");
					bundle.putSerializable("number115", choosedNumbers.get(position));
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
		title10 = (TextView) findViewById(R.id.title10);
		content10 = (TextView) findViewById(R.id.content10);
		long10.setOnClickListener(this);
		long30 = findViewById(R.id.view30);
		title30 = (TextView) findViewById(R.id.title30);
		content30 = (TextView) findViewById(R.id.content30);
		long30.setOnClickListener(this);
		long60 = findViewById(R.id.view60);
		title60 = (TextView) findViewById(R.id.title60);
		content60 = (TextView) findViewById(R.id.content60);
		long60.setOnClickListener(this);
		long154 = findViewById(R.id.view154);
		title154 = (TextView) findViewById(R.id.title154);
		content154 = (TextView) findViewById(R.id.content154);
		long154.setOnClickListener(this);


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
		ChooseUtil.getChooseUtil().maymoney(choosedNumbers.get(0));
		int	jiangjin = ChooseUtil.getChooseUtil().getJiangjin();			

		Intent intent = new Intent(_this, ZNZHMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putInt("jiangjin", jiangjin);
		bundle.putInt("wanfa", ConstantsBase.SD115);
		bundle.putSerializable("ticketarray", getTicket());
		intent.putExtras(bundle);
		startActivityForResult(intent,ZNZH);
	}
	private void initdata() {
		Bundle b = getIntent().getExtras();
		if(b.getBoolean("clear")){
			choosedNumbers = new ArrayList<Number115>();
		}
		Number115 number115 = (Number115)b.getSerializable("number115");
		choosedNumbers  = (ArrayList<Number115>)b.getSerializable("number115List");
		if(choosedNumbers == null){
			choosedNumbers = new ArrayList<Number115>();
		}else{
			ToastUtils.showShort(_this, "这是您上次保存的号码");
		}
		if(number115!=null){
			choosedNumbers.add(number115);
		}
		if(choosedNumbers!=null&&choosedNumbers.size()>0){
			dantuo = choosedNumbers.get(0).getMode();
			wanfa  = choosedNumbers.get(0).getPlayType();
		}else{
			wanfa = 1;
			dantuo = 1;
		}
		listview.setWanFa(dantuo);
		numleast  = ChooseUtil.chooseNUMLEAST(wanfa);
		myListAdapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dlt_zixuan:
			Intent intent = new Intent(_this, Main115Activity.class);
			Bundle bundle = new Bundle();   
			bundle.putInt("position", 0);
			bundle.putString("state", "add");
			bundle.putSerializable("wanfa", wanfa);
			bundle.putSerializable("dantuo", dantuo);
			intent.putExtras(bundle);
			startActivityForResult(intent, ZIXUANNUMBER);
			break;
		case R.id.dlt_jixuan1:
			if(dantuo == 0){
				ToastUtils.showShort(_this, "胆拖暂不支持机选");
				return;	
			}
			Number115 Number115 =  //ChooseUtil.getChooseUtil().getSortList115(
					ChooseUtil.getChooseUtil().getRandom115(numleast,wanfa);
	
			choosedNumbers.add(0,Number115);
			myListAdapter.notifyDataSetChanged();
			updateView1();
			break;
		case R.id.dlt_jixuan5:
			if(dantuo == 0){
				ToastUtils.showShort(_this, "胆拖暂不支持机选");
				return;	
			}
			for(int i=0;i<5;i++){
				Number115 Number1150 =  //ChooseUtil.getChooseUtil().getSortList115(
						ChooseUtil.getChooseUtil().getRandom115(numleast,wanfa);
				choosedNumbers.add(0,Number1150);
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
						checkPeriodNumber();
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
		title10.setTextColor(Color.parseColor("#000000"));
		content10.setTextColor(Color.parseColor("#000000"));
		title30.setTextColor(Color.parseColor("#000000"));
		content30.setTextColor(Color.parseColor("#000000"));
		title60.setTextColor(Color.parseColor("#000000"));
		content60.setTextColor(Color.parseColor("#000000"));
		title154.setTextColor(Color.parseColor("#000000"));
		content154.setTextColor(Color.parseColor("#000000"));
		long10.setBackgroundColor(Color.parseColor("#FFFFFF"));
		long30.setBackgroundColor(Color.parseColor("#FFFFFF"));
		long60.setBackgroundColor(Color.parseColor("#FFFFFF"));
		long154.setBackgroundColor(Color.parseColor("#FFFFFF"));
		if(longValue == 10){
			title10.setTextColor(Color.parseColor("#FFFFFF"));
			content10.setTextColor(Color.parseColor("#FFFFFF"));
			long10.setBackgroundColor(getResources().getColor(R.color.mainred));
		}
		if(longValue == 30){
			title30.setTextColor(Color.parseColor("#FFFFFF"));
			content30.setTextColor(Color.parseColor("#FFFFFF"));
			long30.setBackgroundColor(getResources().getColor(R.color.mainred));
		}
		if(longValue == 60){
			title60.setTextColor(Color.parseColor("#FFFFFF"));
			content60.setTextColor(Color.parseColor("#FFFFFF"));
			long60.setBackgroundColor(getResources().getColor(R.color.mainred));
		}
		if(longValue == 154){
			title154.setTextColor(Color.parseColor("#FFFFFF"));
			content154.setTextColor(Color.parseColor("#FFFFFF"));
			long154.setBackgroundColor(getResources().getColor(R.color.mainred));
		}
	}
	public boolean  setEvent(View view, MotionEvent ev){
		if(dantuo == 0){
			ToastUtils.showShort(_this, "胆拖暂不支持下拉机选");
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
		String moneyStr = "<html"
				+ "<font color=\"#FF3232\">"+allMoneyInt
				+ "</font><font color=\"#000000\">元"
				+ "</html>";
		allMoney.setText(Html.fromHtml(moneyStr));
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
		public Number115 getItem(int position) {
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
			final Number115 order = choosedNumbers.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.tz_item,null);
				holder.line = (TextView) convertView.findViewById(R.id.line);
				holder.number = (TextView) convertView.findViewById(R.id.number);
				holder.sigledouble = (TextView) convertView.findViewById(R.id.sigledouble);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(order!=null){
				StringBuffer numbers = new StringBuffer();
				numbers.append("<html><font color=\"#FF3232\">");
				ArrayList<Integer> wan = null,qian = null,bai = null;
				if(order.getPlayType() == ConstantsBase.ZHIXUAN2){
					wan = order.getWan();
					qian = order.getQian();
					numbers.append("前二直选")
					.append("&#160;&#160;");
					for(int i=0;i<wan.size();i++){
						if(wan.get(i)<10){
							numbers.append("0"+wan.get(i)+"&#160;");
						}else{
							numbers.append(wan.get(i)+"&#160;");
						}
					}
					numbers.append(",");
					for(int i=0;i<qian.size();i++){
						if(qian.get(i)<10){
							numbers.append("0"+qian.get(i)+"&#160;");
						}else{
							numbers.append(qian.get(i)+"&#160;");
						}
					}
				}else
					if(order.getPlayType() == ConstantsBase.ZHIXUAN3){
						wan = order.getWan();
						qian = order.getQian();
						bai = order.getBai();
						numbers.append("前三直选")
						.append("&#160;&#160;");
						for(int i=0;i<wan.size();i++){
							if(wan.get(i)<10){
								numbers.append("0"+wan.get(i)+"&#160;");
							}else{
								numbers.append(wan.get(i)+"&#160;");
							}
						}
						numbers.append(",");
						for(int i=0;i<qian.size();i++){
							if(qian.get(i)<10){
								numbers.append("0"+qian.get(i)+"&#160;");
							}else{
								numbers.append(qian.get(i)+"&#160;");
							}
						}
						numbers.append(",");
						for(int i=0;i<bai.size();i++){
							if(bai.get(i)<10){
								numbers.append("0"+bai.get(i)+"&#160;");
							}else{
								numbers.append(bai.get(i)+"&#160;");
							}
						}
					}else
						if(order.getMode() == 0){
							wan = order.getDan();
							qian = order.getTuo();
							numbers.append(ChooseUtil.choose(order.getPlayType(),dantuo))
							.append("&#160;&#160;");
							for(int i=0;i<wan.size();i++){
								if(wan.get(i)<10){
									numbers.append("0"+wan.get(i)+"&#160;");
								}else{
									numbers.append(wan.get(i)+"&#160;");
								}
							}
							numbers.append("|&#160;");
							for(int i=0;i<qian.size();i++){
								if(qian.get(i)<10){
									numbers.append("0"+qian.get(i)+"&#160;");
								}else{
									numbers.append(qian.get(i)+"&#160;");
								}
							}
						}else{
							wan = order.getNumbers();
							numbers.append(ChooseUtil.choose(order.getPlayType(),dantuo))
							.append("&#160;&#160;");
							for(int i=0;i<wan.size();i++){
								if(wan.get(i)<10){
									numbers.append("0"+wan.get(i)+"&#160;");
								}else{
									numbers.append(wan.get(i)+"&#160;");
								}
							}
						}
				numbers.append("</font></html>");
				holder.number.setText(Html.fromHtml(numbers.toString()));
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
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERS115,null);
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
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERS115,choosedNumbers);
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
				ChooseUtil.setData(_this,ConstantsBase.CHOOSEDNUMBERS115,null);
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
				PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115,newTime);			
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
					Number115	number115 = (Number115)ziXuan.getSerializable("number115");
					if(number115!=null){
						if(number115!=null){
							dantuo = number115.getMode();
							wanfa  = number115.getPlayType();
						}else{
							dantuo = 1;
							wanfa = 1;
						}
						listview.setWanFa(dantuo);
						numleast  = ChooseUtil.chooseNUMLEAST(wanfa);
						choosedNumbers.add(0,number115);
						myListAdapter.notifyDataSetChanged();	
					}	
				}				
				break;
			case CHANGENUMBER:
				Bundle change = data.getExtras();
				if(change!=null){
					int position = change.getInt("position");
					Number115	number115 = (Number115)change.getSerializable("number115");
					if(number115!=null){
						choosedNumbers.get(position).setNumbers(number115.getNumbers());
						choosedNumbers.get(position).setDan(number115.getDan());
						choosedNumbers.get(position).setTuo(number115.getTuo());
						choosedNumbers.get(position).setWan(number115.getWan());
						choosedNumbers.get(position).setQian(number115.getQian());
						choosedNumbers.get(position).setBai(number115.getBai());
						choosedNumbers.get(position).setNum(number115.getNum());
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
						Intent intent = new Intent(_this, Main115Activity.class);
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
						Intent intent = new Intent(_this, Main115Activity.class);
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
		ServiceUser.getInstance().PostPeriod(_this,ConstantsBase.SD115+"", 
				new CallBack<PeriodResultData>() {
			@Override
			public void onSuccess(PeriodResultData t) {
				// TODO 自动生成的方法存根
				if(t.getPeriodList()!=null&& t.getPeriodList().size()>0){
					Period period = t.getPeriodList().get(0);// 拿到第一个数据
					if(period!=null){
						String periodTimeOld = PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIOD115);
						String periodTimeNew = period.getPeriodNumber();
						if(TextUtils.isEmpty(periodTimeOld)){
							periodTimeOld = periodTimeNew;
							PreferenceUtil.putString(_this,PreferenceUtil.PRIZEPERIOD115,periodTimeNew);
						}
						if(periodTimeOld.equals(periodTimeNew)){
							toPayMoney();
						}else{
							showEndTimeDialog(periodTimeOld,periodTimeNew);
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
	private void toPayMoney(){
		tickets.clear();
		tickets.add(getTicket());
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",ConstantsBase.SD115);
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
		String periodNumber = PreferenceUtil.getString(_this,PreferenceUtil.PRIZEPERIOD115);	    
		User user = MyDbUtils.getCurrentUser();
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
		StringBuffer numberString = new StringBuffer();
		for(int j=0;j<choosedNumbers.size();j++){
			StringBuffer number = new StringBuffer();
			String numbers = "";
			Number115 eachNumber = choosedNumbers.get(j);
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
		}
		t.setNumber(numberString.toString().substring(1,numberString.toString().length()));
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
			Number115 number115 =  ChooseUtil.getChooseUtil().getRandom115(numleast,wanfa);
			choosedNumbers.add(0,number115);
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
