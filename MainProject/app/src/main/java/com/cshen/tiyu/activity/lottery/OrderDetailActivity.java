package com.cshen.tiyu.activity.lottery;

import java.io.File;

import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.activity.mian4.find.shareorder.AddShareOrderActivity;
import com.cshen.tiyu.activity.mian4.personcenter.redpacket.RedPacketActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.HttpOrderDetailInfo;
import com.cshen.tiyu.domain.Scheme;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.DialogHelper;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.ScreenShotsUtils;
import com.cshen.tiyu.utils.Util;


public class OrderDetailActivity extends BaseActivity {
	public OrderDetailActivity _this;
	private boolean isTasking = false;
	//public PopupWindow pop;
	public View load;

	public int schemeId;
	public String lotteryId ="";
	public String lotteryName ="";
	public String playTypeStr ="";
	public boolean onlyClose = false;

	private TextView tvTextView;
	private ImageView backImageView;
	private ImageView lottery_icon;
	private TextView lottery_name;
	private TextView schemeCost,multiple,schemePrintState,won;

	private ImageView share_order;
	private TextView schemeNumber,createTimeStr;
	private TextView btn_go;
	private View money;

	public Scheme scheme;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		onCreateView();
		getData();
		initViewBase();		
		setDataBase();	
	}
	public void onCreateView() {}
	private void getData() {
		Intent intent = getIntent();
		schemeId = intent.getIntExtra("schemeId", -1);
		lotteryId = intent.getStringExtra("lotteryId");
		playTypeStr = intent.getStringExtra("playType");
		onlyClose = intent.getBooleanExtra("onlyClose",false);
	}
	private void initViewBase() {
		// TODO 自动生成的方法存根
		backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(!onlyClose){
					Intent intent = new Intent(_this,MainActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
		tvTextView = (TextView) findViewById(R.id.tv_head_title);

		lottery_icon = (ImageView) findViewById(R.id.lottery_icon_11to5);
		lottery_name = (TextView) findViewById(R.id.textview_lotteryName);

		schemeCost = (TextView) findViewById(R.id.textView_schemeCost);
		multiple = (TextView) findViewById(R.id.textView_multiple);
		schemePrintState = (TextView) findViewById(R.id.tectview_schemePrintState);
		won = (TextView) findViewById(R.id.textview_won);

		schemeNumber = (TextView) findViewById(R.id.textview_schemeNumber);
		createTimeStr = (TextView) findViewById(R.id.textview_createTimeStr);

		share_order=(ImageView) findViewById(R.id.iv_share_order);
		share_order.setOnClickListener(new OnClickListener() {//晒单

			@Override
			public void onClick(View v) {
				if(!isScreening){
					new MyAsyTask("").execute();
				}
				/*if (pop == null) {
					popwindows();
				}
				if (pop.isShowing()) {
					pop.dismiss();
					load.setVisibility(View.GONE);
				} else {
					pop.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
					load.setVisibility(View.VISIBLE);
				}	*/
			}
		});
		btn_go = (TextView) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch (lotteryId) {
				case ConstantsBase.JCZQ+"":
					intent = new Intent(_this,FootballMainActivity.class);
				break;
				case ConstantsBase.JCLQ+"":
					intent = new Intent(_this,BasketBallMainActivity.class);
				break;
				case ConstantsBase.SD115+"":
					intent = new Intent(_this,Main115Activity.class);
				break;
				case ConstantsBase.GD115+"":
					intent = new Intent(_this,MainGd115Activity.class);
				break;
				case ConstantsBase.DLT+"":
					intent = new Intent(_this,DLTMainActivity.class);
				break;
				case ConstantsBase.Fast3+"":
					intent = new Intent(_this,Fast3MainActivity.class);
				break;
				case ConstantsBase.SFC+"":
					intent = new Intent(_this,SFCMainActivity.class);
				    intent.putExtra("playType", playTypeStr);
				break;
				case ConstantsBase.SSQ+"":
					intent = new Intent(_this,SSQMainActivity.class);
				break;
				default:
					break;
				}
				startActivity(intent);
				Intent intent2 = new Intent();
				intent2.putExtra("isOnlyClose",false);
				intent2.putExtra("closeMainActivity",true);
				setResult(1, intent2);
				finish();
			}
		});
		money  = findViewById(R.id.money);
		money.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(hasRedPacket){
					Intent intent = new Intent(_this,RedPacketActivity.class);
					startActivity(intent);
				}
			}
		});
		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
	}
	public void setDataBase(){
		if(!TextUtils.isEmpty(lotteryId)){
			lotteryName = Util.getLotteryName(Util.getLotteryTypeToString(Integer.parseInt(lotteryId)));
			int  baseImage = 0 ;
			switch (lotteryId) {
			case ConstantsBase.JCZQ+"":
				baseImage = R.mipmap.jczqicon;
			break;
			case ConstantsBase.JCLQ+"":
				baseImage = R.mipmap.jclqicon;
			break;
			case ConstantsBase.SD115+"":
				baseImage = R.mipmap.sd115icon;
			break;
			case ConstantsBase.GD115+"":
				baseImage = R.mipmap.gd115icon;
			break;
			case ConstantsBase.DLT+"":
				baseImage = R.mipmap.dlticon;
			break;
			case ConstantsBase.Fast3+"":
				baseImage = R.mipmap.fast3icon;
			break;
			case ConstantsBase.SFC+"":
				if("0".equals(playTypeStr)){
					lotteryName = "胜负彩";
					baseImage = R.mipmap.sfcicon;
				}if("1".equals(playTypeStr)){
					lotteryName = "任选九";
					baseImage = R.mipmap.rxjicon;
				}
				break;
			case ConstantsBase.SSQ+"":
				baseImage = R.mipmap.ssqicon;
			break;
			default:
				break;
			}
			tvTextView.setText(lotteryName);
			lottery_name.setText(lotteryName);	
			lottery_icon.setBackgroundResource(baseImage);
			LotteryTypeData currentLotteryTypeData = MyDbUtils.getCurrentLotteryTypeData();
			if (currentLotteryTypeData != null&& currentLotteryTypeData.getLotteryList() != null&& currentLotteryTypeData.getLotteryList().size() > 0) {
				for (int i = 0; i < currentLotteryTypeData.getLotteryList().size(); i++) {
					if (lotteryName != null&& lotteryName.equals(currentLotteryTypeData.getLotteryList().get(i).getTitle())) {
						xUtilsImageUtils.display(lottery_icon,baseImage,currentLotteryTypeData.getLotteryList().get(i).getIcon());
						break;
					}
				}
			}
		}
		if (isTasking) {// 任务在运行中不再登入
			return;
		}
		http();
	}
	private void http() {
		isTasking = true;
		ServiceUser.getInstance().PostFindOrderDetail(_this,schemeId, lotteryId,
				new CallBack<HttpOrderDetailInfo>() {
			@Override
			public void onSuccess(HttpOrderDetailInfo t) {
				scheme = t.getScheme();
				if(TextUtils.isEmpty(scheme.getHongbaoId())) {
					hasRedPacket = false;
					findViewById(R.id.payhong).setVisibility(View.GONE);
				}else {
					hasRedPacket = true;
					findViewById(R.id.payhong).setVisibility(View.VISIBLE);
				}
				if (scheme != null) {
					schemeCost.setText(scheme.getSchemeCost()+ "元");
					multiple.setText(scheme.getMultiple()+ "倍");
					String schemePrintStateStr = scheme.getSchemePrintState();
					String schemePrintStateStrResult = "";
					switch (schemePrintStateStr) {
					case "PRINT":
						schemePrintStateStrResult = "出票委托中";
						break;
					case "SUCCESS":
						schemePrintStateStrResult = "出票成功";
						break;
					case "FAILED":
						schemePrintStateStrResult = "出票失败";
						break;
					case "UNPRINT":
						schemePrintStateStrResult = "未出票";
						break;
					default:
						break;
					}
					schemePrintState.setText(schemePrintStateStrResult);
					if ("SUCCESS".equals(schemePrintStateStr)) {
						if ("WINNING_UPDATED".equals(scheme.getWinningUpdateStatus()) ||"PRICE_UPDATED".equals(scheme.getWinningUpdateStatus())) {
							if(scheme.getWon()){
								java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.##");
								won.setText(df.format(scheme.getPrizeAfterTax())+ "元");
								won.setTextColor(getResources().getColor(R.color.mainred));
								share_order.setVisibility(View.VISIBLE);
							}else{ 
								won.setText("未中奖");
							}
						}else{
							won.setText("待开奖");
						}
					}
					if ("PRINT".equals(schemePrintStateStr)) {
						won.setText("待开奖");
					}
					if ("FAILED".equals(schemePrintStateStr) || "UNPRINT".equals(schemePrintStateStr)) {
						if ("REFUNDMENT".equals(scheme.getState())) {
							won.setText("退款");
						}else if ("CANCEL".equals(scheme.getState())) {
							won.setText("撤单");
						}else{
							won.setText("待开奖");
						}
					}

					setData(t.getResult(),schemePrintStateStr);
					isTasking = false;
					schemeNumber.setText(Html.fromHtml("<html><font color='#000000'>方案编号：</font> "+ scheme.getSchemeNumber()+"</html>"));
					createTimeStr.setText(Html.fromHtml("<html><font color='#000000'>投注时间：</font> "+ scheme.getCreateTimeStr()+"</html>"));
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage,"访问失败");
				isTasking = false;
			}
		});
	}
	public void setData(String result,String state) {

	}
	public boolean isScreening=false;
	public boolean hasRedPacket=false;
	private String strPath="";
	private ProgressDialog mDialog;
	public class MyAsyTask extends AsyncTask<Void, Void, String>{
		String platform;
		public MyAsyTask(String platformFrom){
			platform = platformFrom;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//pop.dismiss();
			isScreening=true;
			mDialog=DialogHelper.getProgressDialog(_this,"截图中...");
			mDialog.show();
			ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView1);
			int permission = ActivityCompat.checkSelfPermission(_this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if (permission != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(_this,  ConstantsBase.PERMISSIONS_STORAGE, ConstantsBase.REQUEST_EXTERNAL_STORAGE);
			}
//			if(TextUtils.isEmpty(platform)){
				strPath=ScreenShotsUtils.captureView(scrollView.getChildAt(0));
//			}else{
//				strPath=ScreenShotsUtils.captureView(scrollView.getChildAt(0),BitmapFactory.decodeResource(_this.getResources(), R.drawable.sharehead));
//			}
		}
		@Override
		protected String doInBackground(Void... params) {
			strPath = ScreenShotsUtils.toZoom(strPath);
			return strPath;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			isScreening=false;
			mDialog.dismiss();
			if(!TextUtils.isEmpty(result)){
				switch (platform) {
				case "":
					toshare();	
					break;
				default:
					break;
				}
			}
		}
	}
	/*public void popwindows() {
		View viewPop = LayoutInflater.from(_this).inflate(R.layout.shareview, null);
		View weixin = viewPop.findViewById(R.id.weixin);
		weixin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScreening){
					new MyAsyTask("weixin").execute();
				}
			}
		});
		View weixinpyq = viewPop.findViewById(R.id.pyq);
		weixinpyq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScreening){
					new MyAsyTask("pyq").execute();
				}
			}
		});
		View qq = viewPop.findViewById(R.id.qq);
		qq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScreening){
					new MyAsyTask("qq").execute();
				}
			}
		});
		View qqzone = viewPop.findViewById(R.id.qqzone);
		qqzone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScreening){
					new MyAsyTask("qqzone").execute();
				}
			}
		});
		View sdq = viewPop.findViewById(R.id.shaidan);
		sdq.setVisibility(View.VISIBLE);
		sdq.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScreening){
					new MyAsyTask("").execute();
				}
			}
		});
		View cancel = viewPop.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		// 创建PopupWindow对象
		pop = new PopupWindow(viewPop, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		pop.setFocusable(true);
		pop.setOnDismissListener(new poponDismissListener());
	}	*/
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			load.setVisibility(View.GONE);
		}
	}
	public void toshare() {
		Intent intent_share=new Intent(_this,AddShareOrderActivity.class);
		intent_share.putExtra("filePath", strPath);
		intent_share.putExtra("schemeId", schemeId+"");//订单id	
		intent_share.putExtra("orderTime", scheme.getCreateTimeStr());//投注单子时间
		intent_share.putExtra("schemeCost", scheme.getSchemeCost()+"");//方案金额
		intent_share.putExtra("won", scheme.getPrizeAfterTax()+"");//奖金		
		startActivity(intent_share);
	}

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mDialog!=null){
			mDialog.dismiss();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mDialog!=null){
			mDialog.dismiss();
			mDialog = null;
		}
		if(!TextUtils.isEmpty(strPath)){
			ScreenShotsUtils.deleteFile(strPath);
		}
	}
}
