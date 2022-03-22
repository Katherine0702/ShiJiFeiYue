package com.cshen.tiyu.activity.pay;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;
import com.ipaynow.plugin.view.IpaynowLoading;*/
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.pay.HttpPayUrlData;
import com.cshen.tiyu.domain.pay.HttpPayWayConfig;
import com.cshen.tiyu.domain.pay.Ipsorder;
import com.cshen.tiyu.domain.pay.IpsorderReslut;
import com.cshen.tiyu.domain.pay.PayWayConfig;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServicePay;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.AlertDialog;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.MyLoading;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
/*import com.sj.pay.sdk.api.JFPaySdk;
import com.sj.pay.sdk.app.common.contants.PayVariety;
import com.sj.pay.sdk.app.entity.PayResult;
import com.sj.pay.sdk.app.listener.Callback;
import com.sj.pay.sdk.app.listener.PaymentCallback;*/


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
public class PayMoneyActivity extends BaseActivity {//implements ReceivePayResult {
	public static final int TOPAY = 0;
	public static final int TOAuthorName = 1;//去实名认证
	public static final int PAY_SUCESS = 10;
	private boolean isShowing = false;
	private Timer timer = new Timer();
	private int i = 0;
	private boolean task = false,hasToPay = false;
	public int payWayInt;
	public String payWayStr;
	// 用于云朵的动画效果
	private ImageView loading;
	public int images[] = new int[] { R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong0, R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong1, R.mipmap.gundong2, R.mipmap.gundong3,
			R.mipmap.gundong4, R.mipmap.gundong5, R.mipmap.gundong6,
			R.mipmap.gundong7, R.mipmap.gundong8, R.mipmap.gundong9,
			R.mipmap.gundong0, R.mipmap.gundong0, R.mipmap.gundong0,
			R.mipmap.gundong0, R.mipmap.gundong0 };
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			loading.setBackgroundResource(images[i % 19]);
		}
	};

	private EditText et_pay_value;
	private TextView tv_value01, tv_value02, tv_value03, tv_value04;
	private LinearLayout pay_way1,pay_way2,show_pay_way2;
	List<PayWayConfig> listData;
	private PayMoneyActivity mContext;
	private boolean needback = false;

	private View load;//遮挡层
	private PopupWindow pop;//玩法弹出框
	private View viewPop;

	private double moneyValue = 0.01;//首次充值弹送红包的金额界限
	private TextView name,money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_money);
		EventBus.getDefault().register(this);
		initHead();
		mContext = this;
		needback = getIntent().getBooleanExtra("needback",false);
		PreferenceUtil.putString(mContext, "rechargeOrderId", "");
		initView();
		initDate();
		httpInit();
	}

	private void httpInit() {
		ServicePay.getInstance().GetPayWay(mContext,ConstantsBase.PayForChongZhi,
				new CallBack<HttpPayWayConfig>() {

			@Override
			public void onSuccess(HttpPayWayConfig t) {
				listData = t.getPayWayList();
				if (listData!=null && listData.size()>0) {
					Collections.sort(listData);
					setPayView();
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(mContext, errorMessage,"访问出错");
			}

		});

	}
	private void setPayView() {
		pay_way1.removeAllViews();
		pay_way2.removeAllViews();
		if(listData.size()>3) {
			show_pay_way2.setVisibility(View.VISIBLE);
		}else {
			show_pay_way2.setVisibility(View.GONE);
		}
		for(int i=0;i<listData.size();i++) {
			final PayWayConfig data = listData.get(i);
			View convertView = View.inflate(mContext, R.layout.pay_way_item,null);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_context = (TextView) convertView.findViewById(R.id.tv_context);
			if (data != null) {
				xUtilsImageUtils.displayIN(img,R.mipmap.ic_error,data.getIcon());
				tv_title.setText(TextUtils.isEmpty(data.getTitle()) ? "":data.getTitle().trim());
				tv_context.setText(TextUtils.isEmpty(data.getContext()) ? "": data.getContext().trim());
				if (!NetWorkUtil.isNetworkAvailable(mContext)) {
					ToastUtils.showShort(mContext, "当前网络信号较差，请检查网络设置");
					return;
				}
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						data.setTemp(data.getTemp() + 1);
						payWayInt = data.getPayWayInt();
						payWayStr = data.getPayWayStr();

						String value = et_pay_value.getText().toString();
						if (value == null || "".equals(value.trim())
								||"0.".equals(value.trim())|| "0".equals(value.trim()) | "0.0".equals(value.trim()) || "0.00".equals(value.trim())) {
							ToastUtils.showShort(mContext, "充值金额不能为空");
							return  ;
						}
						pay(payWayStr, value);
					}
				});
			}
			if(i<3) {
				pay_way1.addView(convertView);
			}else{
				pay_way2.addView(convertView);
			}
		}
	}
	private void initView() {
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		loading = (ImageView) findViewById(R.id.loading);

		tv_value01 = (TextView) findViewById(R.id.tv_value01);
		//tv_value01.setText(randDouble(10,21)+"元");
		tv_value01.setText("10元");
		tv_value01.setOnClickListener(new Tv_value_Onclicklistener());
		tv_value02 = (TextView) findViewById(R.id.tv_value02);
		//tv_value02.setText(randDouble(50,61)+"元");
		tv_value02.setText("50元");
		tv_value02.setOnClickListener(new Tv_value_Onclicklistener());
		tv_value03 = (TextView) findViewById(R.id.tv_value03);
		//tv_value03.setText(randDouble(100,111)+"元");
		tv_value03.setText("100元");
		tv_value03.setOnClickListener(new Tv_value_Onclicklistener());
		tv_value04 = (TextView) findViewById(R.id.tv_value04);
		//tv_value04.setText(randDouble(500,511)+"元");
		tv_value04.setText("500元");
		tv_value04.setOnClickListener(new Tv_value_Onclicklistener());

		et_pay_value = (EditText) findViewById(R.id.et_pay_value);
		String value = tv_value03.getText().toString();
		et_pay_value.setText(value.contains("元")?value.replace("元", ""):"");
		
		et_pay_value.setSelection(et_pay_value.getText().length());
		et_pay_value.addTextChangedListener(new TextChangedListener());
		pay_way1 = (LinearLayout) findViewById(R.id.pay_way1);
		pay_way2 = (LinearLayout) findViewById(R.id.pay_way2);
		show_pay_way2 = (LinearLayout) findViewById(R.id.show_pay_way2);
		show_pay_way2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pay_way2.setVisibility(View.VISIBLE);
				show_pay_way2.setVisibility(View.GONE);
			}
		});
		//mLoadingDialog = new  MyLoading(mIpaynowplugin.getDefaultLoading());

	
		name = (TextView) findViewById(R.id.name);
		money = (TextView) findViewById(R.id.money);

	}
	private void initDate() {
		User user = MyDbUtils.getCurrentUser();
		UserInfo userInfo = MyDbUtils.getCurrentUserInfo();
		name.setText(user.getUserName());
		try {
		double moneyInt= Double.parseDouble(userInfo.getRemainMoney());
		DecimalFormat   df   =new   DecimalFormat("0.00");//保留2位小数
		money.setText(df.format(moneyInt)+"");
		}catch(Exception e) {
			e.printStackTrace();
			money.setText(userInfo.getRemainMoney()+"");
		}
	}
	protected void pay(String payWayStr, String value) {
		try {
			if (task) {
				return;
			}
			task =true;
			final double money = Double.parseDouble(value);
			if(payWayInt == 25&&money<1){
				ToastUtils.showShort(mContext, "至少充值1元");
				task=false;
				return;
			}
			if (payWayInt == 25&&money>3000) {
				ToastUtils.showShort(mContext, "单笔限额3000元");
				task=false;
				return;
			}
			if (payWayInt == 22&&money>50000) {
				ToastUtils.showShort(mContext, "单笔限额50000元");
				task=false;
				return;
			}
			Loading();
			ServicePay.getInstance().setCharge(mContext,money, payWayStr,
					new CallBack<IpsorderReslut>() {
				@Override
				public void onSuccess(IpsorderReslut t) {
					Ipsorder ipsorder = t.getIpsorder();
					if (ipsorder != null) {				
						payUrl(ipsorder,money);
					}
				}

				@Override
				public void onFailure(ErrorMsg errorMessage) {
					task=false;
					LoadingStop();
					// TODO 自动生成的方法存根
					// LoadingStop();
					String error = errorMessage.msg;
					if (error.contains("请先实名认证")) {
						showNameDialog();
					}else {
						if (!TextUtils.isEmpty(error)&&error.contains("-")) {
							if (error.split("-").length > 1) {
								ToastUtils.showShort(mContext,
										error.split("-")[1]);
							} else {
								ToastUtils.showShort(mContext, error);
							}
						} else {
							ToastUtils.showShort(mContext, error);
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void payUrl(Ipsorder ipsorder,final double money) {
		try {
			final String orderId = ipsorder.getPayOrderID();
			ServicePay.getInstance().getPayUrl(mContext,orderId,
					money, payWayInt, new CallBack<HttpPayUrlData>() {
				@Override
				public void onSuccess(HttpPayUrlData t) {
					LoadingStop();
					if (money>=moneyValue) {//充值大于20
						PreferenceUtil.putString(mContext, "rechargeOrderId", orderId);//保存充值订单id，用于我的彩票页面查询是否首次充值大于20
					}
					if(payWayInt == 29){//支付宝聚合
						setZFBJH(t.getPayUrl());
					}else  if(payWayInt == 30){//微信聚合
						setWXJH(t.getPayUrl());
					}else  if(payWayInt == 38||payWayInt == 13
							||payWayInt == 14
							||payWayInt == 17
							||payWayInt == 18){
						Intent intent = new Intent(mContext,QRCodeActivity.class);
						intent.putExtra("paywayInt", payWayInt);
						intent.putExtra("orderID", orderId);
						intent.putExtra("payUrl",t.getPayUrl());
						startActivityForResult(intent, TOPAY);
						task = false;
					}else{//北京现在H5，兴业QQ，兴业微信，盛付通
						Intent intent = new Intent(mContext,WebForPayActivity.class);
						intent.putExtra("url", t.getPayUrl());
						if (!TextUtils.isEmpty(t.getFlag())) {//盛付通专用
							intent.putExtra("flag",t.getFlag());
							intent.putExtra("httpbody",t.getHttpbody());
						}
						startActivityForResult(intent, TOPAY);
					}                
				}
				@Override
				public void onFailure(ErrorMsg errorMessage) {
					task = false;
					// TODO 自动生成的方法存根
					LoadingStop();
					PostHttpInfoUtils.doPostFail(mContext, errorMessage, "访问失败");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showNameDialog() {
		new AlertDialog(PayMoneyActivity.this).builder()
		.setTitle("温馨提示").setMsg("为保证资金安全，请先实名认证！")
		.setPositiveButton("去认证", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, NameAuthActivity.class);
				intent.putExtra("needback", true);
				startActivityForResult(intent, TOAuthorName);
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		}).show();
	}
	public void setWXJH(String  url){//聚合微信
		/*JFPaySdk.getInstance().jfPayment(mContext, url,PayVariety.WeiXin,new PaymentCallback() {

			@Override
			public void onPaySuccess(String payCode) {
				task = false; 
				Log.d("JFPAY", "支付成功");
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
				if (!TextUtils.isEmpty(PreferenceUtil.getString(mContext, "rechargeOrderId"))) {
					EventBus.getDefault().post("checkMoney"+PreferenceUtil.getString(mContext, "rechargeOrderId"));
				}
				finish();
			}

			@Override
			public void onPayFail(PayResult result) {
				task = false; 
				if("未安装微信".equals(result.errorMessage)){
					ToastUtils.showShortCenter(mContext,"请先安装微信！");
				}else{
					Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
				}
				Log.e("JFPAY", "ErrodCode:" + result.errorCode + "   ,ErrorMessage:" + result.errorMessage);
			}
		});*/
	}
	public void setZFBJH(String  url){//聚合支付宝
	/*	JFPaySdk.getInstance().jfPayment(mContext, url,PayVariety.Alipay,new PaymentCallback() {

			@Override
			public void onPaySuccess(String payCode) {
				Log.d("JFPAY", "支付成功");
				task = false; 
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
				if (!TextUtils.isEmpty(PreferenceUtil.getString(mContext, "rechargeOrderId"))) {
					EventBus.getDefault().post("checkMoney"+PreferenceUtil.getString(mContext, "rechargeOrderId"));
				}
				finish();
			}

			@Override
			public void onPayFail(PayResult result) {
				task = false; 
				Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
				Log.e("JFPAY", "ErrodCode:" + result.errorCode + "   ,ErrorMessage:" + result.errorMessage);
			}

		});*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TOPAY:
				task = false; 
				if(needback){//来自网页的支付通知
					ServiceUser.getInstance().GetUserInfo(mContext);// 余额跟新  
					Intent intent = new Intent();  
					intent.putExtra("czchenggong", true);
					setResult(RESULT_OK, intent);
				}
				finish();
				break;
			case TOAuthorName://实名认证成功
				break;
			default:
				break;
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			if(needback){
				Intent intent = new Intent();  
				intent.putExtra("czchenggong", false);
				setResult(RESULT_OK, intent);
			} 
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void initHead() {
		TopViewLeft tv_head = (TopViewLeft) findViewById(R.id.tv_head);
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
				if(needback){
					Intent intent = new Intent();  
					intent.putExtra("czchenggong", false);
					setResult(RESULT_OK, intent);
				}
				finish();
			}
		});

	}

	class Tv_value_Onclicklistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			tv_value01.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value01.setTextColor(getResources().getColor(R.color.black));
			tv_value02.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value02.setTextColor(getResources().getColor(R.color.black));
			tv_value03.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value03.setTextColor(getResources().getColor(R.color.black));
			tv_value04.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value04.setTextColor(getResources().getColor(R.color.black));
			
			
			if (v == tv_value01) {
				String value1 = tv_value01.getText().toString();
				et_pay_value.setText(value1.contains("元")?value1.replace("元", ""):"");
				et_pay_value.setSelection(et_pay_value.getText().length());
			}
			if (v == tv_value02) {
				String value2 = tv_value02.getText().toString();
				et_pay_value.setText(value2.contains("元")?value2.replace("元", ""):"");
				et_pay_value.setSelection(et_pay_value.getText().length());
			}
			if (v == tv_value03) {
				String value3 = tv_value03.getText().toString();
				et_pay_value.setText(value3.contains("元")?value3.replace("元", ""):"");
				et_pay_value.setSelection(et_pay_value.getText().length());
			}
			if (v == tv_value04) {
				String value4 = tv_value04.getText().toString();
				et_pay_value.setText(value4.contains("元")?value4.replace("元", ""):"");
				et_pay_value.setSelection(et_pay_value.getText().length());
			}
			v.setBackgroundResource(R.drawable.red_stroke_shape);
			((TextView)v).setTextColor(getResources().getColor(R.color.mainred));
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
				convertView = View.inflate(mContext, R.layout.pay_way_item_pay,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.pic);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.name);
				holder.tv_context = (TextView) convertView
						.findViewById(R.id.nameinfo);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data != null) {
				xUtilsImageUtils.display(holder.img,R.mipmap.ic_error,data.getIcon());
				holder.tv_title
				.setText(TextUtils.isEmpty(data.getTitle()) ? ""
						: data.getTitle().trim());
				holder.tv_context
				.setText(TextUtils.isEmpty(data.getContext()) ? ""
						: data.getContext().trim());

				if (position == selectIndex) {
					holder.choose.setImageResource(R.mipmap.useaccount);
					PayWayConfig pwc = listData.get(selectIndex);
					payWayInt = pwc.getPayWayInt();
					payWayStr = pwc.getPayWayStr();
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
			public ImageView img;
			public TextView tv_title;
			public TextView tv_context;
			public ImageView choose;
		}

	}

	public void Loading() {
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

	public void LoadingStop() {
		load.setVisibility(View.GONE);
	}

	class TextChangedListener implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!s.toString().trim().contains(".")) {  
				if (s.length()> 7) {  
					s = s.toString().subSequence(0,  7);  
					et_pay_value.setText(s);  
					et_pay_value.setSelection(s.length());  
				}  
			} 
			if (s.toString().trim().contains(".")) { 
				if (s.toString().trim().startsWith(".")) {
					et_pay_value.setText("");  
				}else{
					String[] ss=s.toString().trim().split("\\.");
					if (ss[0].length()> 7) {  
						if (ss.length == 2) {
							s= ss[0].subSequence(0,7)+"."+ss[1]; 	
						}else {
							s= ss[0].subSequence(0,7)+"."; 	
						}

						et_pay_value.setText(s);  
						et_pay_value.setSelection(s.length());  
					}  
				}
			} 
			if (s.toString().contains(".")) {  
				if (s.length() - 1 - s.toString().indexOf(".") > 2) {  
					s = s.toString().subSequence(0,  
							s.toString().indexOf(".") + 3);  
					et_pay_value.setText(s);  
					et_pay_value.setSelection(s.length());  
				}  
			}  
			if (s.toString().trim().substring(0).equals(".")) {  
				s = "0" + s;  
				et_pay_value.setText(s);  
				et_pay_value.setSelection(2);  
			}  

			if (s.toString().startsWith("0")  
					&& s.toString().trim().length() > 1) {  
				if (!s.toString().substring(1, 2).equals(".")) {  
					et_pay_value.setText(s.subSequence(0, 1));  
					et_pay_value.setSelection(1);  
					return;  
				}  
			}  






			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String value = et_pay_value.getText().toString();

			tv_value01.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value01.setTextColor(getResources().getColor(R.color.black));
			tv_value02.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value02.setTextColor(getResources().getColor(R.color.black));
			tv_value03.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value03.setTextColor(getResources().getColor(R.color.black));
			tv_value04.setBackgroundResource(R.drawable.white_stroke_shape);
			tv_value04.setTextColor(getResources().getColor(R.color.black));
			if (value != null) {
				value = value.trim();
				if (value.equals(tv_value01.getText().toString())) {
					tv_value01.setBackgroundResource(R.drawable.red_stroke_shape);
					tv_value01.setTextColor(getResources().getColor(
							R.color.mainred));
				}
				if (value.equals(tv_value02.getText().toString())) {
					tv_value02
					.setBackgroundResource(R.drawable.red_stroke_shape);
					tv_value02.setTextColor(getResources().getColor(
							R.color.mainred));
				}
				if (value.equals(tv_value03.getText().toString())) {
					tv_value03
					.setBackgroundResource(R.drawable.red_stroke_shape);
					tv_value03.setTextColor(getResources().getColor(
							R.color.mainred));
				}
				if (value.equals(tv_value04.getText().toString())) {
					tv_value04
					.setBackgroundResource(R.drawable.red_stroke_shape);
					tv_value04.setTextColor(getResources().getColor(
							R.color.mainred));
				}
			}

		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(hasToPay){
			hasToPay = false;
		}
	}
	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)) {
			if ("backPay".equals(event)) {
				hasToPay = true;
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!TextUtils.isEmpty(PreferenceUtil.getString(mContext, "rechargeOrderId"))) {
			EventBus.getDefault().post("checkMoney"+PreferenceUtil.getString(mContext, "rechargeOrderId"));
		}
		//JFPaySdk.getInstance().jfPayDestroy(this);
		EventBus.getDefault().unregister(this);
	}
	public String randDouble(int max,int min) {
		Random r=new Random();
		double y=r.nextDouble()*(max-min)+min;
		DecimalFormat   df   =new   DecimalFormat("#.0");//保留2位小数
		return df.format(y);
	}
}
