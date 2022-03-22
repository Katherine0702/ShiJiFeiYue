package com.cshen.tiyu.activity.taocan;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3AccountListActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3MainActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballMainActivity;
import com.cshen.tiyu.activity.lottery.cai115.Account115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Accountgd115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.activity.lottery.cai115.MainGd115Activity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.lottery.dlt.DLTAccountListActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTMainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQAccountListActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.domain.taocan.TaoCan;
import com.cshen.tiyu.domain.taocan.TaoCanImage;
import com.cshen.tiyu.domain.taocan.TaoCanResult;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.ToastUtils;

public class TaoCanMainActivity extends BaseActivity{
	TaoCanMainActivity _this;
	ImageView image;
	View chaozhititle,jingxuantitle;
	LinearLayout chaozhis,jingxuans;
	int chaozhiInt,jingxuanInt;
	ArrayList<TaoCan> list;
	TaoCanImage tanCanImage;
	TextView guizeDetail;
	View iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taocanmain);
		_this = this;
		initView();
	}
	private void initView(){
		iv_back  = findViewById(R.id.iv_back);
		image = (ImageView) findViewById(R.id.image);
		guizeDetail = (TextView) findViewById(R.id.detail);
		guizeDetail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
				intentHelp.putExtra("url","http://an.caiwa188.com/help/taocan.html");
				startActivity(intentHelp);
			}
		});
		chaozhititle = findViewById(R.id.chaozhititle);
		chaozhis = (LinearLayout) findViewById(R.id.chaozhis);
		jingxuantitle = findViewById(R.id.jingxuantitle);
		jingxuans = (LinearLayout) findViewById(R.id.jingxuans);

		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
	}
	@Override
	public void onResume() {
	  super.onResume();
	  initDate();
	}
	private void initDate() {
		// TODO 自动生成的方法存根
		ServiceCaiZhongInformation.getInstance().getTaoCan(_this, new CallBack<TaoCanResult>() {

			@Override
			public void onSuccess(final TaoCanResult t) {
				// TODO 自动生成的方法存根
				if(t == null||t.getList()==null){
					return;
				}
				tanCanImage = t.getImgURL();
				list = t.getList();
				chaozhis.removeAllViews();
				jingxuans.removeAllViews();
				chaozhiInt = 0;
				jingxuanInt = 0;
				xUtilsImageUtils.displayIN(image,R.mipmap.taocanicon, tanCanImage.getImageURL());
				image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						toUrl(tanCanImage.getUseLocal(),tanCanImage.getLotteryId(),tanCanImage.getUrl());
					}
				});
				for(final TaoCan tc:list){
					if("1".equals(tc.getType())){
						int shengyu = tc.getNumber() - tc.getUser_get_number();
						if(shengyu>=0){

							View chaozhiView =
									View.inflate(_this, R.layout.taocanitem,null);
							ImageView icon = (ImageView) chaozhiView.findViewById(R.id.icon);
							TextView title = (TextView) chaozhiView.findViewById(R.id.title);
							TextView money = (TextView) chaozhiView.findViewById(R.id.money);
							TextView money2 = (TextView) chaozhiView.findViewById(R.id.money2);
							title.setText(tc.getTitle());
							money.setText(tc.getPeriodNumber()*2+"元");
							money2.setText(tc.getBackMoney()+"元");
							TextView jinsheng = (TextView) chaozhiView.findViewById(R.id.jinsheng);
							TextView qishu = (TextView) chaozhiView.findViewById(R.id.qishu);
							TextView zhongjianglv = (TextView) chaozhiView.findViewById(R.id.zhongjianglv);
							jinsheng.setText(shengyu+"份");
							qishu.setText(tc.getPeriodNumber()+"期");
							zhongjianglv.setText(tc.getWin_rate());
							xUtilsImageUtils.display(icon,tc.getImageURL(), R.mipmap.ssq);
							ImageView deal = (ImageView) chaozhiView.findViewById(R.id.deal);
							switch(tc.getBuyStatus()){
							case 2:
								deal.setImageResource(R.mipmap.unstart);
								break;
							case 1:
							case 4:
								deal.setImageResource(R.mipmap.ended);
								break;
							case 3:
								deal.setImageResource(R.mipmap.goumai);
								break;
							}deal.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(tc.getBuyStatus() == 3){
										Intent intent = new Intent(TaoCanMainActivity.this, TaoCanResultActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("taocan", tc);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}
							});
							chaozhis.addView(chaozhiView);
							chaozhiInt++;
						}

					}
					if(chaozhiInt == 0){
						chaozhititle.setVisibility(View.GONE);
						chaozhis.setVisibility(View.GONE);
					}else{
						chaozhititle.setVisibility(View.VISIBLE);
						chaozhis.setVisibility(View.VISIBLE);
					}
					if("0".equals(tc.getType())){
						int shengyu = tc.getNumber() - tc.getUser_get_number();
						if(shengyu>=0){

							View jingxuanView =
									View.inflate(_this, R.layout.taocanitem,null);
							ImageView icon = (ImageView) jingxuanView.findViewById(R.id.icon);
							TextView title = (TextView) jingxuanView.findViewById(R.id.title);
							TextView money = (TextView) jingxuanView.findViewById(R.id.money);
							TextView money2 = (TextView) jingxuanView.findViewById(R.id.money2);
							title.setText(tc.getTitle());
							money.setText(tc.getPeriodNumber()*2+"元");
							money2.setText(tc.getBackMoney()+"元");

							xUtilsImageUtils.display(icon,tc.getImageURL(), R.mipmap.ssq);
							TextView jinsheng = (TextView) jingxuanView.findViewById(R.id.jinsheng);
							TextView qishu = (TextView) jingxuanView.findViewById(R.id.qishu);
							TextView zhongjianglv = (TextView) jingxuanView.findViewById(R.id.zhongjianglv);
							jinsheng.setText(shengyu+"份");
							qishu.setText(tc.getPeriodNumber()+"期");
							zhongjianglv.setText(tc.getWin_rate());
							ImageView dealjingxuan = (ImageView) jingxuanView.findViewById(R.id.deal);
							switch(tc.getBuyStatus()){
							case 2:
								dealjingxuan.setImageResource(R.mipmap.unstart);
								break;
							case 1:
							case 4:
								dealjingxuan.setImageResource(R.mipmap.ended);
								break;
							case 3:
								dealjingxuan.setImageResource(R.mipmap.goumai);
								break;
							}
							dealjingxuan.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(tc.getBuyStatus() == 3){
										Intent intent = new Intent(TaoCanMainActivity.this, TaoCanResultActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("taocan", tc);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}
							});
							jingxuans.addView(jingxuanView);
							jingxuanInt++;
						}
					}
					if(jingxuanInt == 0){
						jingxuantitle.setVisibility(View.GONE);
						jingxuans.setVisibility(View.GONE);
					}else{
						jingxuantitle.setVisibility(View.VISIBLE);
						jingxuans.setVisibility(View.VISIBLE);
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
	public void toUrl(String local,String lotteryId,String url){
		if("0".equals(local)){
			if("taocan".equals(url.trim())){
				Intent intentHelp = new Intent(_this,TaoCanMainActivity.class);
				startActivity(intentHelp);
			}else{
				if(!TextUtils.isEmpty(url)){
					Intent intentHelp = new Intent(_this,LotteryTypeActivity.class);
					intentHelp.putExtra("url",url);
					startActivity(intentHelp);
				}
			}
		}
		if("1".equals(local)){
			Intent intent;
			if((ConstantsBase.DLT+"").equals(lotteryId)){
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERS);
				if(list==null){
					intent = new Intent(_this, DLTMainActivity.class);
				}else{
					intent = new Intent(_this, DLTAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("dltNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}if((ConstantsBase.SD115+"").equals(lotteryId)){
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERS115);
				if(list==null){
					intent = new Intent(_this, Main115Activity.class);
				}else{
					intent = new Intent(_this, Account115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("number115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.GD115+"").equals(lotteryId)){
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSGD115);
				if(list==null){
					intent = new Intent(_this, MainGd115Activity.class);
				}else{
					intent = new Intent(_this, Accountgd115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("numbergd115List", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.Fast3+"").equals(lotteryId)){
				ArrayList<NumberFast> list = (ArrayList<NumberFast>) ChooseUtil.getData(_this,ConstantsBase.CHOOSEDNUMBERSFAST3);
				if(list==null){
					intent = new Intent(_this, Fast3MainActivity.class);
				}else{
					intent = new Intent(_this, Fast3AccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("numberfastList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else if((ConstantsBase.JCZQ+"").equals(lotteryId)){
				intent = new Intent(_this, FootballMainActivity.class);
				startActivity(intent);
			}else if((ConstantsBase.JCLQ+"").equals(lotteryId)){
				intent = new Intent(_this, BasketBallMainActivity.class);
				startActivity(intent);
			}else if((ConstantsBase.SFC+"").equals(lotteryId)){
				intent = new Intent(_this, SFCMainActivity.class);
				startActivity(intent);
			}else if((ConstantsBase.SSQ+"").equals(lotteryId)){
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(_this,
						ConstantsBase.CHOOSEDNUMBERSSSQ);
				if(list==null){
					intent = new Intent(_this, SSQMainActivity.class);
				}else{
					intent = new Intent(_this, SSQAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ssqtNumberList", list);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}else {
				ToastUtils.showShort(_this, "敬请期待......");
			}
		}
	}
}
