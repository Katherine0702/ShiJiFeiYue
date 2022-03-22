package com.cshen.tiyu.activity.lottery.ball.football;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ball.JCZQJJYHTOUZHU;
import com.cshen.tiyu.domain.ball.JJYHEach;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.PassTypeEach;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.google.gson.Gson;

public class JJYHMainActivity extends BaseActivity{
	public static final int HAVEPAY = 0;
	public static final int LOGIN = 3;
	public static final int SPF = 1;//胜负
	public static final int JQS = 2;//进球数
	public static final int BQC = 3;//半全场
	public static final int BF = 4;//比分
	public static final int HH = 5;//混合

	final static int PINGJUNYOUHUA = 1;
	final static int BOREYOUHUA = 2;
	final static int BOLENGYOUHUA = 3;
	static int YOUHUA = PINGJUNYOUHUA;

	JJYHMainActivity _this;
	private TopViewLeft tv_head;
	private ImageView planmoneyminus,planmoneyadd;
	private EditText planmoney;
	private boolean isPlanMoneyFouces = false;
	private TextView pjyhtxt,bryhtxt,blyhtxt;
	private TextView allmoney,mayprize,pay;

	MyListAdapter adpater;
	ListView listview;
	private ArrayList<JJYHEach> jjyhList;
	private ArrayList<Match> matchs;
	private int wanfa = 0;//玩法
	private PassTypeEach chuanfa;//串法
	private int chuanfaInt = 0;
	private int timeInt = 0;//投注倍数
	private int numMoney = 0;//单倍钱数

	public boolean isToPay = false;
	private int beishu = 0;

	String moneyMin = "";
	String moneyMax= "";
	DecimalFormat df = new DecimalFormat("0.00");

	private ImageView loading;
	private View load;
	private Timer timer = new Timer();  
	private int i = 0;  
	private boolean isShowing =false;
	private Handler handler = new Handler()  
	{  
		@Override  
		public void handleMessage(Message msg)  
		{  

			super.handleMessage(msg); 
			loading.setBackgroundResource(images[i % 19]);
			loading.invalidate();
		}  
	}; 
	public int images[] = new int[] {
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong1,R.mipmap.gundong2,
			R.mipmap.gundong3,R.mipmap.gundong4,R.mipmap.gundong5,
			R.mipmap.gundong6,R.mipmap.gundong7,R.mipmap.gundong8,
			R.mipmap.gundong9,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jjyh);
		_this = this;
		YOUHUA = PINGJUNYOUHUA;
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
				finish();
			}
		});
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);	
		loading = (ImageView) findViewById(R.id.loading);
		initView();
		initDate();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isToPay = false;
	}
	public void initDate(){
		Bundle b = getIntent().getExtras();
		matchs  = (ArrayList<Match>) b.getSerializable("matchs");
		wanfa  = b.getInt("wanfa");
		chuanfa = (PassTypeEach)b.getSerializable("chuanfa");//玩法
		if(chuanfa!=null){
			if(chuanfa.getText().endsWith("串1")&&chuanfa.getText().length()==3){
				chuanfaInt = Integer.parseInt(chuanfa.getText().substring(0, 1));
			}else if(chuanfa.getText().equals("单关")){
				chuanfaInt = 1;
			}
		}
		long allMoneyInt= (long)b.getLong("allMoneyInt");//总钱
		timeInt= (int)b.getLong("timeInt");//倍数

		numMoney= (int)allMoneyInt/(timeInt <=0?1:timeInt);//总钱/倍数				
		beishu = (int) (allMoneyInt/2);//注数
		planmoney.setText(allMoneyInt+"");
		allmoney.setText(allMoneyInt+"");
		getList();
		setList();
	}
	public void initView(){
		planmoneyminus = (ImageView) findViewById(R.id.planmoneyminus);
		planmoney = (EditText) findViewById(R.id.planmoney);
		planmoneyadd = (ImageView) findViewById(R.id.planmoneyadd);
		planmoney.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			@Override  
			public void onFocusChange(View v, boolean hasFocus) {  
				if(hasFocus) {
					isPlanMoneyFouces = true;
				} else {
					isPlanMoneyFouces = false;
				}
			}
		});

		planmoney.addTextChangedListener(new TextWatcher() {  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {
				int planmoneyInt = 0;
				try{
					planmoneyInt = Integer.parseInt(s.toString());  
				}catch(Exception e){
					e.printStackTrace();
				}
				if(isPlanMoneyFouces){
					if(planmoneyInt < numMoney){
						ToastUtils.showShort(_this, "购买金额不能小于最小情况");
						return;
					}
					if(planmoneyInt>=500000){
						planmoneyInt = 500000;
					}
					if(planmoneyInt%2 == 1){
						beishu = planmoneyInt/2+1;
					}else{
						beishu = planmoneyInt/2;
					}
					planmoney.removeTextChangedListener(this);
					planmoney.setText(""+planmoneyInt);
					planmoney.setSelection((""+planmoneyInt).length());
					planmoney.addTextChangedListener(this);
					getList();
					setList();
				}
			}  
		});
		planmoneyminus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				planmoney.clearFocus();
				int planmoneyInt = 0;
				try{
					planmoneyInt = Integer.parseInt(planmoney.getText().toString());
				}catch(Exception  e){
					e.printStackTrace();
				}
				if(planmoneyInt > numMoney){
					beishu = planmoneyInt/2-1;
					getList();
					setList();
				}
			}
		});
		planmoneyadd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				planmoney.clearFocus();
				int planmoneyInt = 0;
				try{
					planmoneyInt = Integer.parseInt(planmoney.getText().toString());					
				}catch(Exception  e){
					e.printStackTrace();
				}
				if(planmoneyInt >= numMoney){
					if(planmoneyInt>=499998){
						planmoneyInt = 499998;
					}
					beishu = planmoneyInt/2+1;
				}else{
					planmoney.setText(numMoney+"");
					beishu = numMoney/2;
				}
				getList();
				setList();
			}
		});
		pjyhtxt = (TextView) findViewById(R.id.pingjun);
		bryhtxt = (TextView) findViewById(R.id.bore);
		blyhtxt = (TextView) findViewById(R.id.boleng);
		pjyhtxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changTitle(PINGJUNYOUHUA);
			}
		});
		bryhtxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changTitle(BOREYOUHUA);
			}
		});
		blyhtxt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				changTitle(BOLENGYOUHUA);
			}
		}); 
		adpater = new MyListAdapter();
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adpater);
		//list = (LinearLayout) findViewById(R.id.listview);
		allmoney = (TextView) findViewById(R.id.allmoney);
		mayprize = (TextView) findViewById(R.id.mayprize);
		pay = (TextView) findViewById(R.id.jjyh_pay);
		pay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				planmoney.clearFocus();
				if (!PreferenceUtil.getBoolean(_this,"hasLogin") ) {
					Intent intentLogin = new Intent(_this, LoginActivity.class); 
					intentLogin.putExtra("requestName", "intentLogin");
					startActivityForResult(intentLogin,LOGIN);
				} else {
					if(wanfa == HH||wanfa == SPF){
						toPayMoney("4",ConstantsBase.JCZQ);
					}else{
						if(wanfa == JQS){
							toPayMoney(1+"",ConstantsBase.JCZQ);
						}
						if(wanfa == BQC){
							toPayMoney(3+"",ConstantsBase.JCZQ);
						}
						if(wanfa == BF){
							toPayMoney(2+"",ConstantsBase.JCZQ);
						}
					}
				}
			}
		});
	}
	public void changTitle(int youhuaNew){
		planmoney.clearFocus();

		if(jjyhList == null){
			jjyhList = JJYHUtil.getJJYHUtil().getJJYHEachList(chuanfaInt,matchs);
		}
		int planmoneyInt = 0;
		try{
			planmoneyInt = Integer.parseInt(planmoney.getText().toString());
		}catch(Exception  e){
			e.printStackTrace();
		}
		if(planmoneyInt < numMoney){
			planmoneyInt = numMoney;
		}
		beishu = planmoneyInt/2;
		boolean noFushu = JJYHUtil.getJJYHUtil().mian(jjyhList,beishu,youhuaNew);

		switch(youhuaNew){
		case BOREYOUHUA:
			if(!noFushu){
				ToastUtils.showShort(_this, "当前计划金额不足以执行搏热优化");
				return;
			}
			break;
		case BOLENGYOUHUA:
			if(!noFushu){
				ToastUtils.showShort(_this, "当前计划金额不足以执行搏冷优化");
				return;
			}
			break;
		}
		YOUHUA = youhuaNew;
		int leftpj = pjyhtxt.getPaddingLeft();   
		int rightpj = pjyhtxt.getPaddingRight();  
		int toppj = pjyhtxt.getPaddingTop();   
		int bottompj = pjyhtxt.getPaddingBottom();  
		int leftbr = bryhtxt.getPaddingLeft();   
		int rightbr = bryhtxt.getPaddingRight();  
		int topbr = bryhtxt.getPaddingTop();   
		int bottombr = bryhtxt.getPaddingBottom();
		int leftbl = blyhtxt.getPaddingLeft();   
		int rightbl = blyhtxt.getPaddingRight();  
		int topbl = blyhtxt.getPaddingTop();   
		int bottombl = blyhtxt.getPaddingBottom(); 
		pjyhtxt.setTextColor(getResources().getColor(R.color.mainred));
		pjyhtxt.setPadding(leftpj, toppj, rightpj, bottompj);
		pjyhtxt.setBackgroundResource(R.drawable.corner13line124_redline_whitefull);
		bryhtxt.setTextColor(getResources().getColor(R.color.mainred));
		bryhtxt.setBackgroundResource(R.drawable.line24_redline_whitefull);
		bryhtxt.setPadding(leftbr, topbr, rightbr, bottombr);
		blyhtxt.setTextColor(getResources().getColor(R.color.mainred));
		blyhtxt.setBackgroundResource(R.drawable.corner24line234_redline_whitefull);
		blyhtxt.setPadding(leftbl, topbl, rightbl, bottombl);
		switch(YOUHUA){
		case PINGJUNYOUHUA:
			pjyhtxt.setTextColor(Color.parseColor("#ffffff"));
			pjyhtxt.setBackgroundResource(R.drawable.corner13line124_redline_redfull);
			pjyhtxt.setPadding(leftpj, toppj, rightpj, bottompj);
			break;
		case BOREYOUHUA:
			bryhtxt.setTextColor(Color.parseColor("#ffffff"));
			bryhtxt.setBackgroundResource(R.drawable.line24_redline_redfull);
			bryhtxt.setPadding(leftbr, topbr, rightbr, bottombr);
			break;
		case BOLENGYOUHUA:
			blyhtxt.setTextColor(Color.parseColor("#ffffff"));
			blyhtxt.setBackgroundResource(R.drawable.corner24line234_redline_redfull);
			blyhtxt.setPadding(leftbl, topbl, rightbl, bottombl);
			break;
		}

		setList();
	}
	public  void getList(){
		if(jjyhList == null){
			jjyhList = JJYHUtil.getJJYHUtil().getJJYHEachList(chuanfaInt,matchs);
		}
		boolean noFushu = JJYHUtil.getJJYHUtil().mian(jjyhList,beishu,YOUHUA);

		switch(YOUHUA){
		case BOREYOUHUA:
			if(!noFushu){
				ToastUtils.showShort(_this, "当前计划金额不足以执行搏热优化");
				return;
			}
			break;
		case BOLENGYOUHUA:
			if(!noFushu){
				ToastUtils.showShort(_this, "当前计划金额不足以执行搏冷优化");
				return;
			}
			break;
		}
	}
	public  void setList(){
		adpater.setDate(jjyhList);
		adpater.notifyDataSetChanged();
		getMINMAX();
	}
	public void getMINMAX(){	
		int money = 0;
		for(int i=0;i<jjyhList.size();i++){
			if(jjyhList.get(i).isCheck()){
				money = money+jjyhList.get(i).getBeishu()*2;
			}
		}
		if(!isPlanMoneyFouces){
			planmoney.setText(money+"");
		}
		allmoney.setText(money+"");
		if(jjyhList!=null&&jjyhList.size()>0){
			ArrayList<JJYHEach> jjyhListCopy = new ArrayList<JJYHEach>();
			for(JJYHEach item : jjyhList){
				if(item.getBeishu() > 0){
					jjyhListCopy.add(item);
				}
			}	
			if(jjyhListCopy!=null&&jjyhListCopy.size()>0){
				Collections.sort(jjyhListCopy, new SortByName());
				moneyMin = df.format(jjyhListCopy.get(0).getJiangjinEach()*jjyhListCopy.get(0).getBeishu());
				moneyMax = df.format(jjyhListCopy.get(jjyhListCopy.size()-1).getJiangjinEach()*jjyhListCopy.get(jjyhListCopy.size()-1).getBeishu());
				mayprize.setText(moneyMin+" ~ "+moneyMax);
			}else{
				mayprize.setText("0.00");
			}
			jjyhListCopy.clear();
			jjyhListCopy = null;
		}else{
			mayprize.setText(0+"");
		}

	}
	public void toPayMoney(String wanfa ,int lotteryId){
		if(isToPay){
			return;
		}else{
			isToPay = true;
		}
		int moneyInt = Integer.parseInt(allmoney.getText().toString());
		if(moneyInt == 0){
			isToPay = false;
			ToastUtils.showShort(_this, "投注金额不能为0");
			return;
		}  
		User user = MyDbUtils.getCurrentUser();
		ArrayList<Ticket> tickets = new ArrayList<>();
		Ticket t = new Ticket();
		t.setUserId(user.getUserId());
		t.setUserPwd(user.getUserPwd());	
		t.setwLotteryId(lotteryId+"");
		t.setMode("1");
		t.setPeriodNumber("");
		t.setPlayType(wanfa);
		
		t.setType("2");
		t.setPassType(chuanfa.getPassTypeValue()+"");
		
		t.setMultiple(1+"");
		t.setSumNum(moneyInt/2+"");
		t.setTotalAmount(moneyInt+"");
		
		t.setHongBaoId("");
		t.setBestMaxPrize(moneyMax);
		t.setBestMinPrize(moneyMin);
		
		t.setAreeCopy("false");
		t.setRecommendTitle("");
		t.setRecommendContent("");

		Gson aGson = new Gson();
		String json2 = aGson.toJson(JJYHUtil.getJJYHUtil().getJJYHTOUZHU(matchs,jjyhList,moneyMin,moneyMax));
		t.setNumber(json2);
		t.setOptimize(true);
		tickets.add(t);
		
		Intent intent = new Intent(_this,PayActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("ticketarray", tickets);
		bundle.putInt("lotteryid",lotteryId);
		bundle.putLong("totalaccount",moneyInt);
		bundle.putBoolean("useRedPacket",true);
		intent.putExtras(bundle);
		startActivityForResult(intent,HAVEPAY);
	}
	class MyListAdapter extends BaseAdapter {
		ArrayList<JJYHEach> resultList;
		public HashMap<Integer, String> contents = new HashMap<>();
		public void setDate(ArrayList<JJYHEach> match){
			resultList = match;
		}
		@Override
		public int getCount() {
			if(resultList == null){
				return 0;
			}else{
				return resultList.size();
			}
		}

		@Override
		public JJYHEach getItem(int position) {
			if(resultList == null){
				return new JJYHEach();
			}else{
				return (JJYHEach)resultList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final JJYHEach jjyheach = resultList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.jjyheach_item,null);
				holder.choose = (CheckBox) convertView.findViewById(R.id.choose);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.minus = (ImageView) convertView.findViewById(R.id.minustime);
				holder.beishu = (EditText) convertView.findViewById(R.id.beishu);      
				holder.beishu.addTextChangedListener(new MyTextChangedListener(holder,contents));  //尽可能减少new 新对象出来
				holder.add = (ImageView) convertView.findViewById(R.id.addtime);
				holder.money = (TextView) convertView.findViewById(R.id.money);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.beishu.setTag(position);//注意设置position

			if(jjyheach!=null){
				holder.choose.setText(chuanfa.getText());				
				holder.name.setText(jjyheach.getNames().replace(",", "\n"));
				holder.beishu.setText(jjyheach.getBeishu()+"");
				holder.money.setText(df.format(jjyheach.getJiangjinEach()*jjyheach.getBeishu()));
			}
			holder.choose.setOnClickListener(new View.OnClickListener() {  

				public void onClick(View v) { 
					planmoney.clearFocus();
					if (jjyheach.isCheck()) {  
						jjyheach.setCheck(false);
						jjyheach.setBeishu(0);  
					} else {  
						jjyheach.setCheck(true);  
						jjyheach.setBeishu(1); 
					}  
					adpater.notifyDataSetChanged();
					getMINMAX();
				}  
			});
			holder.choose.setChecked(jjyheach.isCheck());

			holder.minus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					planmoney.clearFocus();
					int beishuItem = 0;


					try{
						beishuItem = Integer.parseInt(holder.beishu.getText().toString());			
					}catch(Exception e){
						e.printStackTrace();
					}
					if(beishuItem <= 1){
						return;
					}else{
						beishuItem = beishuItem-1;
					}
					jjyheach.setBeishu(beishuItem);
					adpater.notifyDataSetChanged();
					getMINMAX();
				}
			});
			holder.add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					planmoney.clearFocus();
					int beishuItem = 0;
					try{
						beishuItem = Integer.parseInt(holder.beishu.getText().toString());			
					}catch(Exception e){
						e.printStackTrace();
					}
					if(beishuItem >= 999999){
						return;
					}else{
						beishuItem = beishuItem+1;
					}
					jjyheach.setBeishu(beishuItem);
					if(!jjyheach.isCheck()){
						jjyheach.setCheck(true);
					}
					adpater.notifyDataSetChanged();
					getMINMAX();
				}
			});
			return convertView;
		}
	}
	class ViewHolder {
		CheckBox choose;
		TextView name;
		ImageView minus,add;
		EditText beishu;
		TextView money;

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case HAVEPAY:	
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				/*if(!isOnlyClose){
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					if (!closeMainActivity) {
						toHavePay();
					}
				}*/
				_this.finish();
				break;		
			case LOGIN:	
				if(wanfa == HH||wanfa == SPF){
					toPayMoney("4",ConstantsBase.JCZQ);
				}else{
					if(wanfa == JQS){
						toPayMoney(1+"",ConstantsBase.JCZQ);
					}
					if(wanfa == BQC){
						toPayMoney(3+"",ConstantsBase.JCZQ);
					}
					if(wanfa == BF){
						toPayMoney(2+"",ConstantsBase.JCZQ);
					}
				}
				break;
			default:
				break;
			}
		}
	}
	public class MyTextChangedListener implements TextWatcher{

		public ViewHolder holder;
		public HashMap<Integer, String> contents;

		public MyTextChangedListener(ViewHolder holder,HashMap<Integer, String> contents){
			this.holder = holder;
			this.contents = contents;
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void afterTextChanged(Editable editable) {
			if(holder != null && contents != null){
				int position = (int) holder.beishu.getTag();
				contents.put(position,editable.toString());
				int beishuItem = 0;
				try{
					beishuItem = Integer.parseInt(editable.toString());
				}catch(Exception e){
					e.printStackTrace();
					beishuItem = 1;
				}
				if(beishuItem > 999999||beishuItem<=0){
					if(beishuItem == 0){
						if(jjyhList.get(position).isCheck()){
							jjyhList.get(position).setCheck(false);
							holder.choose.setChecked(false);
						}
					}else{
						return;
					}
				}
				if(beishuItem > 0&&!jjyhList.get(position).isCheck()){
					jjyhList.get(position).setCheck(true);
					holder.choose.setChecked(true);
				}
				jjyhList.get(position).setBeishu(beishuItem);
				holder.money.setText(df.format((jjyhList.get(position).getJiangjinEach()*beishuItem))+"");
				getMINMAX();
			}
		}
	}
	class SortByName implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			double s1 = ((JJYHEach)o1).getJiangjinEach()*((JJYHEach)o1).getBeishu();
			double s2 = ((JJYHEach)o2).getJiangjinEach()*((JJYHEach)o2).getBeishu();
			if (s1 > s2){
				return 1;
			}else if(s1 == s2){
				return 0;
			}else{
				return -1;
			}
		}
	}
	public void Loading(){
		if(!isShowing){
			isShowing = true;	
			load.setVisibility(View.VISIBLE);
			loading.setVisibility(View.VISIBLE);
			timer.scheduleAtFixedRate(new TimerTask()  
			{  
				@Override  
				public void run()  
				{  
					// TODO Auto-generated method stub  
					i++;  
					Message mesasge = new Message();  
					mesasge.what = i;  
					handler.sendMessage(mesasge);  
				}  
			}, 0, 100);  
		}
	}
	public void LoadingStop(){
		isShowing = false;		
		loading.setVisibility(View.GONE);
		load.setVisibility(View.GONE);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LoadingStop();
		timer.cancel(); 
	}
}
