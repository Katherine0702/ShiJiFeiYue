package com.cshen.tiyu.activity.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.JCLQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.cai115.EL11TO5OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQOrderDetailActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.pay.ActivityForPayPage;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServicePay;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class PayResultActivity extends BaseActivity implements OnClickListener {
	public static final int ORDER_DETAIL = 1;
	private boolean result,canSeeDetail;
	private String orderId,playType,resultStr,reason;
	private int lotteryId;
	private ImageView img_flag, img_activity;
	private TextView tv_textView01, tv_textView02;
	private TextView tv_go_pay, tv_go_detail, tv_go_main;
	private TextView tv_lotteryName;
	private Context _this;
	private boolean task = false;

	private ActivityForPayPage _entity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		Intent intent = getIntent();
		_this = this;

		result = intent.getBooleanExtra("result", false);
		orderId = intent.getStringExtra("orderId");
		lotteryId = intent.getIntExtra("lotteryId", 0);
		playType = intent.getStringExtra("playType");

		canSeeDetail = intent.getBooleanExtra("canSeeDetail",false);//即买即付专用
		resultStr = intent.getStringExtra("resultStr");//推荐单发起一次推荐专用
		reason = intent.getStringExtra("reason");//推荐单发起一次推荐专用
		initHead();
		initView();
		httpinfo();
	}

	private void httpinfo() {
		if (!task) {
			task = true;
			ServicePay.getInstance().getActivityForPayPage(_this,
					lotteryId,playType, new CallBack<ActivityForPayPage>() {
				@Override
				public void onFailure(ErrorMsg errorMessage) {
					task = false;
					PostHttpInfoUtils.doPostFail(_this, errorMessage,
							"网络问题");
				}

				@Override
				public void onSuccess(ActivityForPayPage entity) {
					_entity = entity;
					task = false;
					if (_entity != null) {
						try {
							xUtilsImageUtils.display(img_activity,R.mipmap.ic_error,_entity.getImageURL());
							img_activity
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (_entity.getUseLocal() == 0) {
										Intent intent = null;
										String oldUrl = _entity
												.getUrl();
										if (!TextUtils.isEmpty(oldUrl)) {
											String newUrl = oldUrl.replaceFirst("shouji","scp");
											intent = new Intent(_this,LotteryTypeActivity.class);// 跳转html活动界面
											if (ConstantsBase.IP.equals("http://cp.citycai.com")) {
												intent.putExtra("url",newUrl);
												startActivity(intent);
											} else {
												intent.putExtra("url",oldUrl);
												startActivity(intent);
											}
											Intent intent2 = new Intent();
											intent2.putExtra("isOnlyClose",false);
											intent2.putExtra("closeMainActivity",true);
											setResult(2, intent2);
											finish();
										}

									}
									if (_entity.getUseLocal() == 1) {
										Intent intent = null;

										if ((ConstantsBase.DLT + "").equals(_entity.getLotteryId())) {
											intent = new Intent(_this,DLTMainActivity.class);
										}
										if ((ConstantsBase.SSQ+"").equals(_entity.getLotteryId())) {
											intent = new Intent(_this, SSQMainActivity.class);
										}
										if ((ConstantsBase.SD115 + "").equals(_entity.getLotteryId())) {
											intent = new Intent(_this,Main115Activity.class);
										}
										if ((ConstantsBase.GD115 + "").equals(_entity.getLotteryId())) {
											intent = new Intent(_this,MainGd115Activity.class);
										}
										if( (ConstantsBase.JCZQ+"").equals(_entity.getLotteryId())){
											intent = new Intent(_this, FootballMainActivity.class);
										}
										if( (ConstantsBase.JCLQ+"").equals(_entity.getLotteryId())){
											intent = new Intent(_this, BasketBallMainActivity.class);
										}
										if ((ConstantsBase.SFC + "").equals(_entity.getLotteryId())) {
											intent = new Intent(_this, SFCMainActivity.class);
											intent.putExtra("playType", playType);

										}
										if((ConstantsBase.Fast3+"").equals(_entity.getLotteryId())) {
											intent = new Intent(_this,Fast3MainActivity.class);
										}
										startActivity(intent);
										Intent intent2 = new Intent();
										intent2.putExtra("isOnlyClose",false);
										intent2.putExtra("closeMainActivity",true);
										setResult(2, intent2);
										finish();

									}


								}
							});

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			});

		}

	}

	private void initView() {
		img_flag = (ImageView) findViewById(R.id.img_flag);
		img_activity = (ImageView) findViewById(R.id.img_activity);
		tv_textView01 = (TextView) findViewById(R.id.tv_textView01);
		tv_textView02 = (TextView) findViewById(R.id.tv_textView02);
		tv_go_pay = (TextView) findViewById(R.id.tv_go_pay);
		tv_go_pay.setOnClickListener(this);
		tv_go_detail = (TextView) findViewById(R.id.tv_go_detail);
		tv_go_detail.setOnClickListener(this);
		tv_go_main = (TextView) findViewById(R.id.tv_go_main);
		tv_go_main.setOnClickListener(this);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		String lotteryName = Util.getLotteryName(Util
				.getLotteryTypeToString(lotteryId));
		if("0".equals(playType)){	
			tv_lotteryName.setText("胜负彩");
		}else if("1".equals(playType)){	
			tv_lotteryName.setText("任选九");
		}else{
			tv_lotteryName.setText(lotteryName);
		}
		if (result) {
			img_flag.setBackgroundResource(R.mipmap.pay_success);
			tv_textView01.setText("支付成功，方案正在出票中");
			tv_textView02.setText("可以通过投注详情，查看方案状态");
			tv_go_detail.setVisibility(View.VISIBLE);
		} else {
			img_flag.setBackgroundResource(R.mipmap.pay_fail);
			tv_textView01.setText("购买失败");
			if("5".equals(resultStr)){
				tv_textView02.setText("当前方案最低奖金小于本金，无法发起推荐");
			}else if("8".equals(resultStr)){
				if(!TextUtils.isEmpty(reason)&&reason.contains("8-")){
					tv_textView02.setText(reason.replace("8-",""));
				}else{
					tv_textView02.setText(reason);
				}
			}else{
				tv_textView02.setText("可能原因：购买期次已截止或充值金额未到账");
			}
			tv_go_detail.setVisibility(View.GONE);
		}
		if(canSeeDetail){
			tv_go_detail.setVisibility(View.VISIBLE);
		}else{
			tv_go_detail.setVisibility(View.GONE);
		}
	}

	private void initHead() {
		TopViewLeft tv_head = (TopViewLeft) findViewById(R.id.tv_head);
		if (result) {
			tv_head.setTitle("付款成功");
		} else {
			tv_head.setTitle("购买失败");
		}
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO Auto-generated method stub
			}

			@Override
			public void clickContactView(View view) {
				// TODO Auto-generated method stub
			}

			@Override
			public void clickBackImage(View view) {
				Intent intent = new Intent();
				intent.putExtra("isOnlyClose", false);
				setResult(2, intent);
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {

		if (v == tv_go_pay) {
			Intent base = getIntent();
			String from=base.getStringExtra("fromPage");
			if ("MainHomeFragment".equals(from)) {
				Intent intent = null;
				if (lotteryId == ConstantsBase.SD115) {
					intent = new Intent(_this, Main115Activity.class);
				}
				if (lotteryId == ConstantsBase.GD115) {
					intent = new Intent(_this, MainGd115Activity.class);
				}
				if (lotteryId == ConstantsBase.Fast3) {
					intent = new Intent(_this, Fast3MainActivity.class);
				}
				if (lotteryId == ConstantsBase.DLT) {
					intent = new Intent(_this, DLTMainActivity.class);
				}
				if (lotteryId == ConstantsBase.JCZQ) {
					intent = new Intent(_this, FootballMainActivity.class);
				}
				if (lotteryId == ConstantsBase.JCLQ) {
					intent = new Intent(_this, BasketBallMainActivity.class);
				}
				startActivity(intent);
				Intent returnIntent = new Intent();
				intent.putExtra("isOnlyClose", false);
				setResult(2, returnIntent);
				finish();
			}else{
				Intent intent = new Intent();
				intent.putExtra("isOnlyClose", false);
				setResult(2, intent);
				finish();
			}


		}
		if (v == tv_go_detail) {
			Intent intent = null;
			if (lotteryId == ConstantsBase.SD115) {
				intent = new Intent(_this, EL11TO5OrderDetailActivity.class);
			}
			if (lotteryId == ConstantsBase.GD115) {
				intent = new Intent(_this, EL11TO5OrderDetailActivity.class);
			}

			if (lotteryId == ConstantsBase.Fast3) {
				intent = new Intent(_this, Fast3OrderDetailActivity.class);
			}
			if (lotteryId == ConstantsBase.DLT) {
				intent = new Intent(_this, DLTOrderDetailActivity.class);
			}
			if (lotteryId == ConstantsBase.JCZQ) {
				intent = new Intent(_this, JCZQOrderDetailActivity.class);
			}if (lotteryId == ConstantsBase.JCLQ) {
				intent = new Intent(_this, JCLQOrderDetailActivity.class);
			}if (lotteryId == ConstantsBase.SFC) {
				intent = new Intent(_this, SFCOrderDetailActivity.class);
				intent.putExtra("playType", playType);
			}if (lotteryId == ConstantsBase.SSQ) {
				intent = new Intent(_this, SSQOrderDetailActivity.class);
			}
			try {
				intent.putExtra("schemeId", Integer.parseInt(orderId));
			} catch (Exception e) {
				e.printStackTrace();
				intent.putExtra("schemeId", -1);
			}
			intent.putExtra("lotteryId", lotteryId + "");
			startActivityForResult(intent, ORDER_DETAIL);
		}
		if (v == tv_go_main) {
			startActivity(new Intent(_this, MainActivity.class));
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.putExtra("isOnlyClose", false);
		setResult(2, intent);
		finish();
		return super.onKeyUp(keyCode, event);

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ORDER_DETAIL ) {
			if (resultCode == 1) {
				boolean isOnlyClose = data.getBooleanExtra("isOnlyClose", false);
				if (!isOnlyClose) {
					Intent intent = new Intent();
					intent.putExtra("isOnlyClose", false);
					boolean closeMainActivity = data.getBooleanExtra("closeMainActivity", false);
					intent.putExtra("closeMainActivity", closeMainActivity);
					setResult(2, intent);
					finish();
				}
			}
		}
	}
	@Override
	public void onResume() {  
		// TODO Auto-generated method stub
		super.onResume();
		int type = getIntent().getIntExtra("type", -1);
		String typeStr = "";
		if(type>=0){
			switch (type) {//2热门，3人气，0命中，1盈利，
			case 2:
				typeStr = "payfromgendan_remen";
				break;
			case 3:
				typeStr = "payfromgendan_renqi";//统计
				break;
			case 0:
				typeStr = "payfromgendan_mingzhong";//统计
				break;
			case 1:
				typeStr = "payfromgendan_yingli";//统计
				break;
			default:
				break;
			}
			//System.out.println(typeStr);
			//ToastUtils.showShort(_this, typeStr);
		//	MobclickAgent.onEvent(_this, typeStr);//统计
		}else{
			//ToastUtils.showShort(_this, "pay");
		//	MobclickAgent.onEvent(_this, "pay");//统计
		}
	}
}

