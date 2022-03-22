package com.cshen.tiyu.activity.pay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.HttpOrderDetailInfo;
import com.cshen.tiyu.domain.Scheme;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.pay.HttpPayUrlData;
import com.cshen.tiyu.domain.pay.HttpPayWayConfig;
import com.cshen.tiyu.domain.pay.Ipsorder;
import com.cshen.tiyu.domain.pay.IpsorderReslut;
import com.cshen.tiyu.domain.pay.PayWayConfig;
import com.cshen.tiyu.domain.pay.Ticket;
import com.cshen.tiyu.domain.pay.TicketBack;
import com.cshen.tiyu.domain.pay.TicketBackResult;
import com.cshen.tiyu.domain.redpacket.RedPacket;
import com.cshen.tiyu.domain.redpacket.RedPacketData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.ServicePay;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.HorizontalListView;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.google.gson.Gson;
/*
import com.sj.pay.sdk.api.JFPaySdk;
import com.sj.pay.sdk.app.common.contants.PayVariety;
import com.sj.pay.sdk.app.entity.PayResult;
import com.sj.pay.sdk.app.listener.PaymentCallback;
*/


import de.greenrobot.event.EventBus;

public class PayActivity extends BaseActivity implements OnClickListener{//},ReceivePayResult {
	public static final int TOPAY = 0;
	public static final int TOREALNAME = 1;
	private PayActivity _this;
	private int type = -1;//投注来源，来自跟单的哪个列表
	private int timeInt = 1;
	private String schemeBackupsId = "";
	private String selectResourceId = "";
	private int lotteryId = 0;// 彩种
	private String playType = "";
	private boolean useRedPacket = true;
	// 账户余额，订单金额，红包金额
	private Double accountInt = 0.0;
	private long totalAccountInt = 0;// 总金额
	private int activityId = -2;
	private BigDecimal totalAccountF, hongbaoAccountF;
	private ArrayList<Ticket> ticket;// 投注内容     
	private long hongbaoId = -1;
	private TopViewLeft tv_head;// 头
	private View hongbao;// 红包条目
	private View hongbaokey;//
	private ImageView hongbaoicon;
	private TextView hongbaotext;
	private boolean isShow = true;
	private HorizontalListView listview;
	private HorizontalListViewAdapter adapter;
	private ArrayList<RedPacket> redPackets;
	private RedPacket redPacket;
	private TextView lottery, totalAccount, account, hongbaoAccount, realPay;// 彩种，订单金额，账户余额，红包，实际支付

	private View otherpay;
	private TextView pay;// 支付按钮
	private boolean paying =false;
	private ImageView loading;
	private View load;
	private Timer timer = new Timer();
	private int i = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			loading.setBackgroundResource(images[i % 19]);
		}
	};
	public int images[] = new int[] { R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong0, R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong1, R.mipmap.gundong2, R.mipmap.gundong3,
			R.mipmap.gundong4, R.mipmap.gundong5, R.mipmap.gundong6,
			R.mipmap.gundong7, R.mipmap.gundong8, R.mipmap.gundong9,
			R.mipmap.gundong0, R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong0, R.mipmap.gundong0 };
	private boolean task = false;
	List<PayWayConfig> listData;
	private MyAdapter myAdapter;
	MyListView  lv_pay_way;

	private boolean canJMJF = true;
	private String orderId = "";//票的订单编号
	private String payWay = "";
	private int payWayInt = 5;
	//private IpaynowPlugin mIpaynowplugin;
	//private IpaynowLoading mLoadingDialog;

	//boolean isTuijian  = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		EventBus.getDefault().register(this);
		_this = this;
		//北京现在
		/*mIpaynowplugin = IpaynowPlugin.getInstance().init(this);// 1.插件初始化
		mIpaynowplugin.unCkeckEnvironment();// 无论微信、qq安装与否，网关页面都显示渠道按钮。
		//聚合支付
		JFPaySdk.getInstance().jfPayInit(this, new Callback() {

			@Override
			public void onCancel() {

			}

			@Override
			public void onSuccess(Bundle bundle) {
			}

			@Override
			public void onFail(int errorCode, String errorMessage) {
				Toast.makeText(_this, errorMessage, Toast.LENGTH_LONG).show();
			}
		});*/


		initView();
		initdata();
	}

	public void initView() {
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
			}

			@Override
			public void clickContactView(View view) {
			}

			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		lottery = (TextView) findViewById(R.id.lotteryname);
		totalAccount = (TextView) findViewById(R.id.totalaccount_tv);
		account = (TextView) findViewById(R.id.acconut_tv);
		hongbao = findViewById(R.id.hongbao);
		hongbao.setVisibility(View.GONE);
		hongbaoAccount = (TextView) findViewById(R.id.hongbao_tv);
		hongbaokey = findViewById(R.id.hongbaokey);
		hongbaokey.setOnClickListener(this);
		hongbaoicon = (ImageView) findViewById(R.id.hongbaoicon);
		hongbaotext = (TextView) findViewById(R.id.hongbaotext);
		adapter = new HorizontalListViewAdapter();
		listview = (HorizontalListView) findViewById(R.id.rl_listview);
		listview.setAdapter(adapter);
		realPay = (TextView) findViewById(R.id.realpay_tv);
		pay = (TextView) findViewById(R.id.btn_pay);
		pay.setOnClickListener(this);
		otherpay = findViewById(R.id.otherpay);

		lv_pay_way = (MyListView) findViewById(R.id.lv_pay_way);

		myAdapter = new MyAdapter();
		lv_pay_way.setAdapter(myAdapter);

		lv_pay_way.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!NetWorkUtil.isNetworkAvailable(_this)) {
					ToastUtils.showShort(_this, "当前网络信号较差，请检查网络设置");
					return;
				}
				myAdapter.setSelectIndex(position);
				myAdapter.notifyDataSetChanged();
			}
		});
		load = findViewById(R.id.load);
		load.getBackground().setAlpha(70);
		loading = (ImageView) findViewById(R.id.loading);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelectIndex(position);
				adapter.notifyDataSetChanged();
			}
		});


	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setAccountBase();
		System.out.println("orderId="+orderId);
		/*if(!TextUtils.isEmpty(orderId)){
			setCaiZhongResult();
		}*/
	}
	private void initdata() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			totalAccountInt = bundle.getLong("totalaccount", 0l);//一共多少钱
			useRedPacket = bundle.getBoolean("useRedPacket");//使用红包
			lotteryId = bundle.getInt("lotteryid");//玩法
			playType = bundle.getString("playType");//区分胜负彩任选九
			//追号活动
			activityId = bundle.getInt("activityId", -2);
			//投注彩种
			ticket = (ArrayList<Ticket>) bundle.getSerializable("ticketarray");
			//投注跟单
			selectResourceId  = bundle.getString("selectResourceId");
			schemeBackupsId = bundle.getString("SchemeBackupsId");
			timeInt = bundle.getInt("timeInt");
			type = bundle.getInt("type",-1);

		}

		switch (lotteryId) {
		case ConstantsBase.DLT:
			lottery.setText("大乐透");
			break;
		case ConstantsBase.SD115:
			lottery.setText("山东11选5");
			break;
		case ConstantsBase.GD115:
			lottery.setText("广东11选5");
			break;
		case ConstantsBase.Fast3:
			lottery.setText("快3");
			break;
		case ConstantsBase.JCZQ:
			lottery.setText("竞彩足球");
			break;
		case ConstantsBase.JCLQ:
			lottery.setText("竞彩篮球");
			break;
		case ConstantsBase.SFC:
			if("0".equals(playType)){
				lottery.setText("胜负彩");
			}
			if("1".equals(playType)){
				lottery.setText("任选九");
			}
			break;
		case ConstantsBase.PL35:
			if("0".equals(playType)){
				lottery.setText("排列三");
			}
			if("1".equals(playType)){
				lottery.setText("排列五");
			}
			break;
		case ConstantsBase.SSQ:
			lottery.setText("双色球");
			break;
		}
		totalAccount.setText(totalAccountInt + "元");
		getRedPacket();
		httpInit();


	}
	private void httpInit() {
		if (task) {
			return;
		}
		task=true;
		ServicePay.getInstance().GetPayWay(_this,ConstantsBase.PayForPay,
				new CallBack<HttpPayWayConfig>() {

			@Override
			public void onSuccess(HttpPayWayConfig t) {
				task=false;
				listData = t.getPayWayList();
				if (listData!=null && listData.size()>0) {
					Collections.sort(listData);
					myAdapter.setDataList(listData);
					myAdapter.notifyDataSetChanged();
				}
				setAccount();

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				task=false;
				PostHttpInfoUtils.doPostFail(_this, errorMessage,"访问出错");
				setAccount();
			}

		});

	}
	public void setAccountBase() {
		ServiceUser.getInstance().GetUserInfo(_this);// 余额跟新
	}
	public void setAccount() {
		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		if (userInfo != null) {
			String accountStr = userInfo.getRemainMoney();
			try {
				accountInt = Double.parseDouble(accountStr);
			} catch (Exception e) {
				e.printStackTrace();
				accountInt = 0d;
			}
		}
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false); 
		account.setText(nf.format(accountInt) + "元");
		double totalAccountD = Double.parseDouble(totalAccountInt+"");
		totalAccountF = new BigDecimal(Double.toString(totalAccountD));
		if (hongbaoAccountF == null) {
			hongbaoAccountF = new BigDecimal(Double.toString(0));
		}
		BigDecimal accountF = new BigDecimal(Double.toString(accountInt));
		if (Long.parseLong(totalAccountInt + "") > (accountF
				.add(hongbaoAccountF)).longValue()) {// 投注金额大于账户余额		
			DecimalFormat df = new DecimalFormat("0.00");
			Double d = new Double(totalAccountF.subtract(accountF).subtract(hongbaoAccountF).doubleValue()+ "");
			String realPayStr = df.format(d);
			if(realPayStr.endsWith(".00")){
				realPay.setText(realPayStr.substring(0, realPayStr.length()-3));
			}else{
				realPay.setText(realPayStr);
			}
			if(lotteryId == ConstantsBase.SD115||
					lotteryId == ConstantsBase.GD115||
					lotteryId == ConstantsBase.Fast3){//11选5，快三，追号不能即买即付
				canJMJF = false;
			}else if(ticket!=null&&ticket.size()==1&&"true".equals(ticket.get(0).getIsChase())){//追期
				canJMJF = false;
			}else if(listData==null||listData.size()==0){//没有支付方式
				canJMJF = false;
			}else if (ticket == null || ticket.size() == 0) {//跟单
				canJMJF = false;
			}else{
				canJMJF = true;
			}
		} else {
			realPay.setText(0 + "");
			canJMJF = false;
		}
		if(canJMJF){
			otherpay.setVisibility(View.VISIBLE);
		}else{
			otherpay.setVisibility(View.GONE);
		}
	}

	public void getRedPacket() {
		ServicePay.getInstance().getRedPacketPayOrder(_this,
				totalAccountInt + "", lotteryId + "",
				new CallBack<RedPacketData>() {

			@Override
			public void onSuccess(RedPacketData t) {
				// TODO 自动生成的方法存根
				ArrayList<RedPacket> newlists = t.getResult();
				if (redPackets == null) {
					redPackets = new ArrayList<RedPacket>();
				}
				redPackets.addAll(newlists);
				if (redPackets.size() == 0) {
					hongbao.setVisibility(View.GONE);
				} else {
					hongbao.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					findViewById(R.id.rl_listview_line).setVisibility(
							View.VISIBLE);
					if (!useRedPacket) {
						hongbao.setVisibility(View.GONE);
					} else {
						adapter.setSelectIndex(0);
					}
				}
				adapter.setDate(redPackets);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				hongbao.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.hongbaokey:
			if (!isShow) {
				isShow = true;
				findViewById(R.id.rl_listview_line).setVisibility(View.VISIBLE);
				listview.setVisibility(View.VISIBLE);
				hongbaoicon.setImageResource(R.mipmap.up);
				hongbaotext.setText("收起红包");
			} else {
				isShow = false;
				findViewById(R.id.rl_listview_line).setVisibility(View.GONE);
				listview.setVisibility(View.GONE);
				hongbaoicon.setImageResource(R.mipmap.down);
				hongbaotext.setText("展开红包");
			}
			break;
		case R.id.btn_pay:
			if(paying){
				ToastUtils.showShort(_this, "订单已生成，正在处理");
				return;
			}
			paying = true;
			Loading();	
			String realMoney = realPay.getText().toString();
			if (ticket != null && ticket.size() > 0) {
				if(!canJMJF&&!"0".equals(realMoney)){//不能即买即付，并且要支付的钱不是0
					LoadingStop();	
					paying = false;
					showNameDialog2();
				}else{
					payDreict();
				}
			}else{
				if ("0".equals(realMoney)) {
					payGenDanDreict(true);
				} else {
					LoadingStop();	
					paying = false;
					showNameDialog2();
				}
			}
			break;
		}
	}
	public void payDreict() {
		paying = true;
		Gson aGson = new Gson();
		String json2 = aGson.toJson(ticket);
		ServicePay.getInstance().PostCathectic(_this,activityId,lotteryId + "", json2,
				new CallBack<TicketBackResult>() {
			@Override
			public void onSuccess(TicketBackResult t) {
				ArrayList<TicketBack> arrayList = t.getTicket();
				if (arrayList != null && arrayList.size() > 0) {
					TicketBack ticketBack = arrayList.get(0);
					if ("0".equals(ticketBack.getProcess())) {
						orderId = ticketBack.getOrderId();
						if(!TextUtils.isEmpty(orderId)){
							if("PAYSAVE".equals(ticketBack.getOrderState())){
								Pay(orderId);
							}else{
								payResultSuccess(ticketBack.getOrderId(),true);
							}
						}else{
							payResultFail(ticketBack.getErrorMsg(),ticketBack.getProcess());
						}
					} else {
						payResultFail(ticketBack.getErrorMsg(),ticketBack.getProcess());
					}
				}
				LoadingStop();			
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				LoadingStop();
				ToastUtils.showShort(_this, errorMessage.msg);
				paying = false;
			}

		});
	}
	public void payGenDanDreict(final boolean isDriect) {
		paying = true;
		ServiceGenDan.getInstance().pay(_this,schemeBackupsId,selectResourceId,timeInt,
				new CallBack<TicketBack>() {
			@Override
			public void onSuccess(TicketBack ticketBack) {

				if (!TextUtils.isEmpty(ticketBack.getOrderId())) {
					if(isDriect){
						payResultSuccess(ticketBack.getOrderId(),true);												
					}else{
						Pay(ticketBack.getOrderId());
					}
				} else {
					if (!TextUtils.isEmpty(ticketBack.getErrorMsg())&&(ticketBack
							.getErrorMsg().contains("已截止销售")||ticketBack           
							.getErrorMsg().contains("已经停止销售"))){
						ToastUtils.showShort(_this, "投注内容中含有截止场次，请更换场次投注");
						paying = false;
					}else{
						Intent intent = new Intent(_this,PayResultActivity.class);
						intent.putExtra("lotteryId", lotteryId);
						intent.putExtra("playType", playType);
						intent.putExtra("result", false);
						Intent base= getIntent();
						String from=base.getStringExtra("fromPage");
						if (!TextUtils.isEmpty(from)) {
							intent.putExtra("fromPage", from);
						}
						startActivityForResult(intent, 0);
						return;			
					}
				}

				LoadingStop();			
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				LoadingStop();
				ToastUtils.showShort(_this, errorMessage.msg);
				paying = false;
			}

		});
	}
	public void Pay(String orderId) {
		try {
			double money = Double.parseDouble(realPay.getText().toString());
			ServicePay.getInstance().setAccount(_this,money, payWay,
					lotteryId,hongbaoId,orderId,
					new CallBack<IpsorderReslut>() {
				@Override
				public void onSuccess(IpsorderReslut t) {
					Ipsorder ipsorder = t.getIpsorder();
					ticket.get(0).setOrderId(ipsorder.getPayOrderID());
					setOrder(ticket);
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					// TODO 自动生成的方法存根
					LoadingStop();
					String error = errorMessage.msg;
					if (error.contains("请先实名认证")) {
						showNameDialog();
					}else 
						if (error.contains("-")) {
							if (error.split("-").length > 1) {
								ToastUtils.showShort(_this,error.split("-")[1]);
							} else {
								ToastUtils.showShort(_this, error);
							}
						} else {
							ToastUtils.showShort(_this, error);
						}
					paying = false;
				}
			});
		} catch (Exception e) {
			paying = false;
			e.printStackTrace();
		}
	}

	public void setOrder(ArrayList<Ticket> arrayList) {		
		payUrl(arrayList.get(0).getOrderId());
	}

	public void payUrl(final String orderPayId) {
		try {
			double money = Double.parseDouble(realPay.getText().toString());
			ServicePay.getInstance().getPayUrl(_this,orderPayId, money, payWayInt,
					new CallBack<HttpPayUrlData>() {
				@Override
				public void onSuccess(HttpPayUrlData t) {					
					if(payWayInt ==20){//现在支付
						if(t.getPayUrl().contains("?")){
							String[] indexStr = t.getPayUrl().split("[?]");
							//setXZ(indexStr[1]);
						}
					}else  if(payWayInt == 29){//金服支付宝
						setZFBJH(t.getPayUrl());
						pay.setClickable(true);
					}else  if(payWayInt == 30){//金服微信
						setWXJH(t.getPayUrl());					
					}else  if(payWayInt == 38||payWayInt == 13
							||payWayInt == 14
							||payWayInt == 17
							||payWayInt == 18){
						Intent intent = new Intent(_this,QRCodeActivity.class);
						intent.putExtra("paywayInt", payWayInt);
						intent.putExtra("orderID", orderId);
						intent.putExtra("payUrl",t.getPayUrl());
						startActivityForResult(intent, TOPAY);
						paying = false;
					}else{//北京现在H5，兴业QQ，兴业微信
						Intent intent = new Intent(_this,WebForPayActivity.class);
						intent.putExtra("url", t.getPayUrl());
						if (!TextUtils.isEmpty(t.getFlag())) {//盛付通专用
							intent.putExtra("flag",t.getFlag());
							intent.putExtra("httpbody",t.getHttpbody());
						}
						startActivityForResult(intent, TOPAY);
					}
					LoadingStop();
				} 

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					// TODO 自动生成的方法存根
					paying = false;
					LoadingStop();
					ToastUtils.showShort(_this, errorMessage.msg);
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setWXJH(String  url){//金服微信
		/*JFPaySdk.getInstance().jfPayment(_this, url,PayVariety.WeiXin,new PaymentCallback() {

			@Override
			public void onPaySuccess(String payCode) {
				payResultSuccess(orderId,false);
				paying = false;
				finish();
			}

			@Override
			public void onPayFail(PayResult result) {
				paying = false;
				payResultFail(result.errorMessage,"-1");
			}
		});*/
	}
	public void setZFBJH(String  url){//金服支付宝
		/*JFPaySdk.getInstance().jfPayment(_this, url, PayVariety.Alipay,new PaymentCallback() {

			@Override
			public void onPaySuccess(String payCode) {
				payResultSuccess(orderId,false);
				paying = false;
				finish();
			}
			@Override
			public void onPayFail(PayResult result) {
				paying = false;
				if("未安装微信".equals(result.errorMessage)){
					ToastUtils.showShortCenter(_this,"请先安装微信！");
				}
				payResultFail(result.errorMessage,"-1");
			}

		});*/
	}
	/*public void setXZ(String  url){//现在支付
		mIpaynowplugin.setCustomLoading(mLoadingDialog).setCallResultReceiver(_this).pay(url);	
	}*/
	public void payResultFail(String reason,String process){
		if (!TextUtils.isEmpty(reason)&&
				(reason.contains("已截止销售")||
						reason.contains("已经停止销售")||
						reason.contains("已经截止销售"))){
			ToastUtils.showShort(_this, "投注内容中含有截止场次，请更换场次投注");
			paying = false;
		}else{
			Intent intent = new Intent(_this,PayResultActivity.class);
			intent.putExtra("lotteryId", lotteryId);
			intent.putExtra("playType", playType);
			intent.putExtra("resultStr",process);
			intent.putExtra("reason",reason);
			intent.putExtra("result", false);
			Intent base= getIntent();
			String from=base.getStringExtra("fromPage");
			if (!TextUtils.isEmpty(from)) {
				intent.putExtra("fromPage", from);
			}
			startActivityForResult(intent, 0);
			return;			
		}
	}
	public void payResultSuccess(String orderId,boolean canSeeDetail){
		GetUserInfo();
		EventBus.getDefault().post("updateFragment");
		Intent intent = new Intent(_this,PayResultActivity.class);
		intent.putExtra("canSeeDetail",canSeeDetail);
		intent.putExtra("orderId",orderId);
		intent.putExtra("lotteryId", lotteryId);
		intent.putExtra("result", true);
		intent.putExtra("playType", playType);
		intent.putExtra("type", type);
		Intent base= getIntent();
		String from=base.getStringExtra("fromPage");
		if (!TextUtils.isEmpty(from)) {
			intent.putExtra("fromPage", from);
		}
		startActivityForResult(intent, 0);			
	}
	/*@Override
	public void onIpaynowTransResult(ResponseParams arg0) {
		// TODO Auto-generated method stub
		String respCode = arg0.respCode;
		String errorCode = arg0.errorCode;
		String errorMsg = arg0.respMsg;
		StringBuilder temp = new StringBuilder();
		if (respCode.equals("00")) {
			temp.append("支付成功");
			payResultSuccess(orderId,false);
			_this.finish();
			return;
		} else if (respCode.equals("02")) {
			temp.append("交易状态:取消");
		} else if (respCode.equals("01")) {
			temp.append("交易状态:失败").append("\n").append("错误码:").append(errorCode).append("原因:" + errorMsg);
		} else if (respCode.equals("03")) {
			temp.append("交易状态:未知").append("\n").append("原因:" + errorMsg);
		} else {
			temp.append("respCode=").append(respCode).append("\n").append("respMsg=").append(errorMsg);
		}
		payResultFail(temp.toString(),"-1");
		paying = false;
		return;			
	}*/

	public void GetUserInfo() {
		ServiceUser.getInstance().PostUserInfo(_this,"0", null, null,
				MyDbUtils.getCurrentUser().getUserPwd(), null, null, null,
				null, null, false, new CallBack<String>() {
			@Override
			public void onSuccess(String s) {
				EventBus.getDefault().post("updateUserInfo");
			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				ToastUtils.showShort(_this, errorMsg.msg);
			}
		});
	}
	public void Loading() {
		if(load.getVisibility() != View.VISIBLE){
			load.setVisibility(View.VISIBLE);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					i++;
					Message mesasge = new Message();
					mesasge.what = i;
					handler.sendMessage(mesasge);
				}
			}, 0, 100);
		}

	}

	public void LoadingStop() {
		load.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LoadingStop();
		timer.cancel();
		EventBus.getDefault().unregister(this);
		//JFPaySdk.getInstance().jfPayDestroy(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TOREALNAME:// 实名验证回来
				if(data.getBooleanExtra("yanzheng", false)){
					if (ticket != null && ticket.size() > 0) {
						Loading();
						payDreict();
					}
				}
				break;
			case TOPAY://支付宝H5，盛付通，京东支付，
				paying = false;
				setCaiZhongResult();
				break;
			default:
				break;
			}
		}
		if (resultCode == 2) {
			boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
			if (!isOnlyClose) {
				Intent intent = new Intent();
				intent.putExtra("isOnlyClose", false);
				boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
				intent.putExtra("closeMainActivity", closeMainActivity);
				setResult(RESULT_OK, intent);
				_this.finish();
			}
		}
	}

	public void setCaiZhongResult() {
		int schemeId = -1;
		try {
			schemeId =  Integer.parseInt(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServiceUser.getInstance().PostFindOrderDetail(_this,schemeId, lotteryId+"",
				new CallBack<HttpOrderDetailInfo>() {
			@Override
			public void onSuccess(HttpOrderDetailInfo t) {
				Scheme scheme = t.getScheme();
				if (scheme != null) {
					if("FULL".equals(scheme.getState())||
							"SUCCESS".equals(scheme.getState())){
						payResultSuccess(orderId,false);
					}else{
						payResultFail("","-1");
					}

				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage,"访问失败");
				payResultFail("","-1");
			}
		});
	}

	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)) {
			if ("updateUserInfo".equals(event)) {
				setAccount();
			}

		}

	}

	public class HorizontalListViewAdapter extends BaseAdapter {
		private ArrayList<RedPacket> orderLists;
		private int selectIndex = -1;

		public HorizontalListViewAdapter() {
		}

		public void setDate(ArrayList<RedPacket> orderListsFrom) {
			this.orderLists = orderListsFrom;
		}

		@Override
		public int getCount() {
			if (orderLists == null) {
				return 0;
			} else {
				return orderLists.size();
			}
		}

		@Override
		public RedPacket getItem(int position) {
			if (orderLists == null) {
				return null;
			} else {
				return orderLists.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RedPacket order = orderLists.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this,
						R.layout.horizontal_list_item, null);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.money = (TextView) convertView.findViewById(R.id.rmb_tv);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (order != null) {
				holder.title.setText(order.getHongbaoName());
				BigDecimal total = order.getTotalMoney();
				BigDecimal used = order.getUsedMoney();
				if (total != null && used != null) {
					String maymoneyStr = "";
					float canUse = total.subtract(used).floatValue();
					if (total != null && used != null) {
						int one = (int) canUse;
						if (Float.parseFloat((one + "")) == canUse) {
							maymoneyStr = "<html><font color=\"#FFFFFF\">￥"
									+ "</font><font color=\"#ffd200\">" + one
									+ "" + "</font></html>";
						} else {
							maymoneyStr = "<html><font color=\"#FFFFFF\">￥"
									+ "</font><font color=\"#ffd200\">"
									+ canUse + "" + "</font></html>";
						}
					}

					holder.money.setText(Html.fromHtml(maymoneyStr));
				}
				holder.time.setText(order.getRealAvailabilityDays() + "天过期");
			}
			if (position == selectIndex) {
				holder.choose.setVisibility(View.VISIBLE);
			} else {
				holder.choose.setVisibility(View.INVISIBLE);
			}
			return convertView;

		}

		class ViewHolder {
			public ImageView choose;
			public TextView title;
			public TextView money;
			public TextView time;

		}

		public void setSelectIndex(int i) {
			if (selectIndex == i) {
				selectIndex = -1;
				hongbaoAccount.setText("0元");
				if (ticket != null && ticket.size() > 0
						&& ticket.get(0) != null){
					ticket.get(0).setHongBaoId("");
				}
				hongbaoId = -1;
				hongbaoAccountF = null;
				setAccount();
			} else {
				selectIndex = i;
				redPacket = redPackets.get(i);
				BigDecimal total = redPacket.getTotalMoney();
				BigDecimal used = redPacket.getUsedMoney();
				if (total != null && used != null) {
					hongbaoAccount.setText(total.subtract(used).floatValue()
							+ "元");
					hongbaoAccountF = total.subtract(used);
					setAccount();
				}
				if (ticket != null && ticket.size() > 0
						&& ticket.get(0) != null){
					ticket.get(0).setHongBaoId(redPacket.getId() + "");
				}
				hongbaoId = redPacket.getId();
			}

		}
	}
	class MyAdapter extends BaseAdapter {
		private List<PayWayConfig> dataList;
		private int selectIndex = 0;
		public void setDataList(List<PayWayConfig> dataList) {
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			// How many items are in the data set represented by this
			// Adapter.(在此适配器中所代表的数据集中的条目数)
			if (dataList == null) {
				return 0;
			} else {
				return dataList.size();
			}
		}

		@Override
		public PayWayConfig getItem(int position) {
			// Get the data item associated with the specified position in the
			// data set.(获取数据集中与指定索引对应的数据项)
			if (dataList != null) {
				return dataList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// Get the row id associated with the specified position in the
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Get a View that displays the data at the specified position in
			// the data set.
			PayWayConfig data = null;
			if (dataList != null) {
				data = dataList.get(position);
			}
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(_this, R.layout.pay_way_item_pay,
						null);
				holder = new ViewHolder();
				holder.pic = (ImageView) convertView.findViewById(R.id.pic);
				holder.name = (TextView) convertView
						.findViewById(R.id.name);
				holder.nameinfo = (TextView) convertView
						.findViewById(R.id.nameinfo);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data != null) {
				xUtilsImageUtils.display(holder.pic, data.getIcon(), R.mipmap.ybpic);
				holder.name.setText(TextUtils.isEmpty(data.getTitle()) ? "": data.getTitle().trim());
				holder.nameinfo.setText(TextUtils.isEmpty(data.getContext()) ? "": data.getContext().trim());
				if (position == selectIndex) {
					holder.choose.setImageResource(R.mipmap.useaccount);
					PayWayConfig pwc = listData.get(selectIndex);
					payWayInt = pwc.getPayWayInt();
					payWay = pwc.getPayWayStr();
				} else {
					holder.choose.setImageResource(R.mipmap.useaccountno);
				}
			}
			return convertView;
		}
		public void setSelectIndex(int i) {
			if (selectIndex != i) {
				selectIndex = i;
			}
		}
		class ViewHolder {
			public ImageView pic;
			public TextView name;
			public TextView nameinfo;
			public ImageView choose;
		}
	}
	public void showNameDialog() {
		new AlertDialog(_this).builder()
		.setTitle("温馨提示").setMsg("为保证资金安全，请先实名认证！")
		.setPositiveButton("去认证", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this, NameAuthActivity.class);
				intent.putExtra("needback", true);
				startActivityForResult(intent, TOREALNAME);
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				paying = false;
			}
		}).show();
	}
	public void showNameDialog2() {
		new AlertDialog(_this).builder()
				.setTitle("温馨提示").setMsg("余额不足！")
				.setPositiveButton("去充值", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(_this, PayMoneyActivity.class);
						intent.putExtra("needback", true);
						startActivityForResult(intent, TOREALNAME);
					}
				}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				paying = false;
			}
		}).show();
	}
}
