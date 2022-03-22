package com.cshen.tiyu.activity.mian4.find;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQPeriodsRefreshActivity;
import com.cshen.tiyu.activity.lottery.cai115.SDEL11to5PeriodsActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTPeriodsActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQPeriodsActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.domain.PrizeList;
import com.cshen.tiyu.domain.ball.HttpZcMatchResult;
import com.cshen.tiyu.domain.ball.ZcMatchResultDTO;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.Util;


import de.greenrobot.event.EventBus;

public class LatestLotteryInfoActivity extends BaseActivity{


	private LatestLotteryInfoActivity _this;
	private ImageView lottery_img_dlt;
	/*
	 * 双色球
	 */
	private TextView textview_lotteryName_ssq, textview_periodNumber_ssq,
			textview_time_ssq;
	private TextView textview_result_ssq01, textview_result_ssq02,
			textview_result_ssq03, textview_result_ssq04,
			textview_result_ssq05, textview_result_ssq06,
			textview_result_ssq07;
	/*
	 * 大乐透
	 */
	private TextView textview_lotteryName_dlt, textview_periodNumber_dlt,
			textview_time_dlt;
	private TextView textview_result_dlt01, textview_result_dlt02,
			textview_result_dlt03, textview_result_dlt04,
			textview_result_dlt05, textview_result_dlt06,
			textview_result_dlt07;
	/*
	 * 山东11选5
	 */
	private TextView textview_lotteryName_sdel11to5,
			textview_periodNumber_sdel11to5, textview_time_sdel11to5;
	private TextView textview_result_sdel11to501, textview_result_sdel11to502,
			textview_result_sdel11to503, textview_result_sdel11to504,
			textview_result_sdel11to505;
	/*
	 * 广东11选5
	 */
	private TextView textview_lotteryName_gdel11to5,
			textview_periodNumber_gdel11to5, textview_time_gdel11to5;
	private TextView textview_result_gdel11to501, textview_result_gdel11to502,
			textview_result_gdel11to503, textview_result_gdel11to504,
			textview_result_gdel11to505;
	/*
	 * 竞彩足球
	 */
	private TextView textview_time_jczq, textview_result_homeName,
			textview_result_bifen, textview_result_guestName;
	private LinearLayout linearyLayout_ssq, linearyLayout_dlt,
			linearyLayout_sdel11to5, linearyLayout_gdel11to5,linearyLayout_jczq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.latest_lotteryinfo_fragment);
		EventBus.getDefault().register(this);
		changHead();

		
		initViewSSQ();
		http_SSQ();
		
		initViewDLT();
		setImage();
		http_dlt();
		
		initViewSDEL11to5();
		http_sdel11to5();
		
		initViewGDEL11to5();
		http_gdel11to5();
		
		initViewJCZQ();
		http_jczq();
	}

	public void onEventMainThread(String event) {

		if (!TextUtils.isEmpty(event)) {
			if ("updatePeriodsInfo".equals(event)) {
				http_SSQ();
				http_dlt();
				http_sdel11to5();

			}
		}
	}

	private void http_SSQ() {
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"0", "0", "1","",
				new CallBack<PrizeList>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(_this, errorMessage,
								"访问失败");
					}

					@Override
					public void onSuccess(PrizeList t) {
						// TODO Auto-generated method stub
						if (t != null) {
							ArrayList<Prize> resultList = t.getResultList();
							if (resultList != null && resultList.size() > 0) {
								textview_periodNumber_ssq.setText("第"
										+ resultList.get(0).getPeriodNumber()
										+ "期");
								textview_time_ssq
										.setText(resultList.get(0)
												.getPrizeTime().trim()
												.substring(0, 10));
								;
								if (resultList.get(0).getResult() != null) {
									String[] resultNumber = resultList.get(0)
											.getResult().split(",");
									textview_result_ssq01.setText(Util
											.periodNumberFromat(resultNumber[0]));
									textview_result_ssq02.setText(Util
											.periodNumberFromat(resultNumber[1]));
									textview_result_ssq03.setText(Util
											.periodNumberFromat(resultNumber[2]));
									textview_result_ssq04.setText(Util
											.periodNumberFromat(resultNumber[3]));
									textview_result_ssq05.setText(Util
											.periodNumberFromat(resultNumber[4]));
									textview_result_ssq06.setText(Util
											.periodNumberFromat(resultNumber[5]));
									textview_result_ssq07.setText(Util
											.periodNumberFromat(resultNumber[6]));
								}
							}
						}

					}
				});
	}

	private void http_dlt() {
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"13", "0", "1","",
				new CallBack<PrizeList>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(_this, errorMessage,
								"访问失败");
					}

					@Override
					public void onSuccess(PrizeList t) {
						// TODO Auto-generated method stub
						if (t != null) {
							ArrayList<Prize> resultList = t.getResultList();
							if (resultList != null && resultList.size() > 0) {
								textview_periodNumber_dlt.setText("第"
										+ resultList.get(0).getPeriodNumber()
										+ "期");
								textview_time_dlt
										.setText(resultList.get(0)
												.getPrizeTime().trim()
												.substring(0, 10));
								;
								if (resultList.get(0).getResult() != null) {
									String[] resultNumber = resultList.get(0)
											.getResult().split(",");
									textview_result_dlt01.setText(Util
											.periodNumberFromat(resultNumber[0]));
									textview_result_dlt02.setText(Util
											.periodNumberFromat(resultNumber[1]));
									textview_result_dlt03.setText(Util
											.periodNumberFromat(resultNumber[2]));
									textview_result_dlt04.setText(Util
											.periodNumberFromat(resultNumber[3]));
									textview_result_dlt05.setText(Util
											.periodNumberFromat(resultNumber[4]));
									textview_result_dlt06.setText(Util
											.periodNumberFromat(resultNumber[5]));
									textview_result_dlt07.setText(Util
											.periodNumberFromat(resultNumber[6]));
								}
							}
						}

					}
				});
	}

	private void http_sdel11to5() {
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"14", "0", "1","",
				new CallBack<PrizeList>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(_this, errorMessage,
								"访问失败");
					}

					@Override
					public void onSuccess(PrizeList t) {
						// TODO Auto-generated method stub
						if (t != null) {
							ArrayList<Prize> resultList = t.getResultList();
							if (resultList != null && resultList.size() > 0) {
								String periodNumber = resultList.get(0)
										.getPeriodNumber();
								if (!TextUtils.isEmpty(periodNumber)
										&& periodNumber.trim().length() > 2) {
									periodNumber = periodNumber.trim();
									textview_periodNumber_sdel11to5.setText("第"
											+ periodNumber.substring(
													periodNumber.length() - 2,
													periodNumber.length())
											+ "期");
								}
								textview_time_sdel11to5.setText(resultList
										.get(0).getPrizeTime().trim()
										.substring(0, 10));
								if (resultList.get(0).getResult() != null) {
									String[] resultNumber = resultList.get(0)
											.getResult().split(",");
									textview_result_sdel11to501.setText(Util
											.periodNumberFromat(resultNumber[0]));
									textview_result_sdel11to502.setText(Util
											.periodNumberFromat(resultNumber[1]));
									textview_result_sdel11to503.setText(Util
											.periodNumberFromat(resultNumber[2]));
									textview_result_sdel11to504.setText(Util
											.periodNumberFromat(resultNumber[3]));
									textview_result_sdel11to505.setText(Util
											.periodNumberFromat(resultNumber[4]));
								}
							}
						}

					}
				});
	}
	private void http_gdel11to5() {
		ServiceCaiZhongInformation.getInstance().pastPrizeList(_this,"19", "0", "1","",
				new CallBack<PrizeList>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage,
						"访问失败");
			}
			
			@Override
			public void onSuccess(PrizeList t) {
				// TODO Auto-generated method stub
				if (t != null) {
					ArrayList<Prize> resultList = t.getResultList();
					if (resultList != null && resultList.size() > 0) {
						String periodNumber = resultList.get(0)
								.getPeriodNumber();
						if (!TextUtils.isEmpty(periodNumber)
								&& periodNumber.trim().length() > 2) {
							periodNumber = periodNumber.trim();
							textview_periodNumber_gdel11to5.setText("第"
									+ periodNumber.substring(
											periodNumber.length() - 2,
											periodNumber.length())
											+ "期");
						}
						// textview_periodNumber_sdel11to5.setText("第"+periodNumber+"期");
						textview_time_gdel11to5.setText(resultList
								.get(0).getPrizeTime().trim()
								.substring(0, 10));
						;
						if (resultList.get(0).getResult() != null) {
							String[] resultNumber = resultList.get(0)
									.getResult().split(",");
							textview_result_gdel11to501.setText(Util
									.periodNumberFromat(resultNumber[0]));
							textview_result_gdel11to502.setText(Util
									.periodNumberFromat(resultNumber[1]));
							textview_result_gdel11to503.setText(Util
									.periodNumberFromat(resultNumber[2]));
							textview_result_gdel11to504.setText(Util
									.periodNumberFromat(resultNumber[3]));
							textview_result_gdel11to505.setText(Util
									.periodNumberFromat(resultNumber[4]));
						}
					}
				}
				
			}
		});
	}

	private void http_jczq() {
		Date date = new Date();
		String matchDate = DateUtils.getDateforint(date);

		ServiceCaiZhongInformation.getInstance().getZc_first_Macth_Result(_this,"",
				new CallBack<HttpZcMatchResult>() {
					@Override
					public void onFailure(ErrorMsg errorMessage) {
						PostHttpInfoUtils.doPostFail(_this, errorMessage,
								"访问失败");
					}

					@Override
					public void onSuccess(HttpZcMatchResult t) {
						// TODO Auto-generated method stub
						if (t != null && t.getData() != null) {
							ZcMatchResultDTO data = t.getData();
							textview_time_jczq.setText(data.getMatchTime()
									.subSequence(0, 10));
							StringBuilder homename = new StringBuilder("");
							homename.append(data.getHomeTeamName());
							if (data.getHandicap() != null) {
								homename.append("(")
										.append(data.getHandicap().intValue())
										.append(")");
							}
							textview_result_homeName.setText(homename);
							textview_result_bifen.setText(data.getHomeScore()
									+ ":" + data.getGuestScore());
							textview_result_guestName.setText(data
									.getGuestTeamName());
							;

						}

					}
				});
		// CaiZhongService.getInstance().getZc_Macth_Result(_this,matchDate,"17",new
		// CallBack<HttpZcMatchResult>() {
		// @Override
		// public void onFailure(ErrorMsg errorMessage) {
		// PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
		// }
		// @Override
		// public void onSuccess(HttpZcMatchResult t) {
		// // TODO Auto-generated method stub
		// if(t!=null && t.getResult()!=null){
		// List<ZcMatchResultDTO> match=t.getResult().get("match");
		// if (match!=null && match.size()>0) {
		// ZcMatchResultDTO data=match.get(0);
		// textview_time_jczq.setText(data.getMatchTime().subSequence(0, 10));
		// StringBuilder homename=new StringBuilder("");
		// homename.append(data.getHomeTeamName());
		// if (data.getHandicap() != null) {
		// homename.append("(").append(data.getHandicap().intValue()).append(")");
		// }
		// textview_result_homeName.setText(homename);
		// textview_result_bifen.setText(data.getHomeScore() +":"
		// +data.getGuestScore());
		// textview_result_guestName.setText(data.getGuestTeamName());;
		// }
		//
		// }
		//
		// }
		// });
	}

	private void setImage() {
		String lotteryName_dlt = textview_lotteryName_dlt.getText().toString()
				.trim();
		LotteryTypeData currentLotteryTypeData = MyDbUtils
				.getCurrentLotteryTypeData();
		if (currentLotteryTypeData != null
				&& currentLotteryTypeData.getLotteryList() != null
				&& currentLotteryTypeData.getLotteryList().size() > 0) {
			for (int i = 0; i < currentLotteryTypeData.getLotteryList().size(); i++) {
				setImageHelp(lottery_img_dlt, lotteryName_dlt,
						currentLotteryTypeData.getLotteryList().get(i));
			}
		}
	}

	private void setImageHelp(ImageView lottery_img, String lotteryName,
			LotteryType lotteryType) {
		if (lotteryName != null && lotteryType != null
				&& lotteryName.equals(lotteryType.getTitle())) {
			xUtilsImageUtils.display(lottery_img,R.mipmap.ic_error,lotteryType.getIcon());
		}

	}

	private void changHead() {
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
	}

	private void initViewSSQ() {
		// 双色球相关 view
		linearyLayout_ssq = (LinearLayout) findViewById(R.id.linearyLayout_ssq);
		textview_lotteryName_ssq = (TextView) findViewById(R.id.textview_lotteryName_ssq);
		textview_periodNumber_ssq = (TextView) findViewById(R.id.textview_periodNumber_ssq);
		textview_time_ssq = (TextView) findViewById(R.id.textview_time_ssq);
		textview_result_ssq01 = (TextView) findViewById(R.id.textview_result_ssq01);
		textview_result_ssq02 = (TextView) findViewById(R.id.textview_result_ssq02);
		textview_result_ssq03 = (TextView) findViewById(R.id.textview_result_ssq03);
		textview_result_ssq04 = (TextView) findViewById(R.id.textview_result_ssq04);
		textview_result_ssq05 = (TextView) findViewById(R.id.textview_result_ssq05);
		textview_result_ssq06 = (TextView) findViewById(R.id.textview_result_ssq06);
		textview_result_ssq07 = (TextView) findViewById(R.id.textview_result_ssq07);
		linearyLayout_ssq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this, SSQPeriodsActivity.class);
				_this.startActivity(intent);
			}
		});
	}

	private void initViewDLT() {
		// 大乐透相关 view
		linearyLayout_dlt = (LinearLayout) findViewById(R.id.linearyLayout_dlt);
		lottery_img_dlt = (ImageView) findViewById(R.id.lottery_img_dlt);
		textview_lotteryName_dlt = (TextView) findViewById(R.id.textview_lotteryName_dlt);
		textview_periodNumber_dlt = (TextView) findViewById(R.id.textview_periodNumber_dlt);
		textview_time_dlt = (TextView) findViewById(R.id.textview_time_dlt);
		textview_result_dlt01 = (TextView) findViewById(R.id.textview_result_dlt01);
		textview_result_dlt02 = (TextView) findViewById(R.id.textview_result_dlt02);
		textview_result_dlt03 = (TextView) findViewById(R.id.textview_result_dlt03);
		textview_result_dlt04 = (TextView) findViewById(R.id.textview_result_dlt04);
		textview_result_dlt05 = (TextView) findViewById(R.id.textview_result_dlt05);
		textview_result_dlt06 = (TextView) findViewById(R.id.textview_result_dlt06);
		textview_result_dlt07 = (TextView) findViewById(R.id.textview_result_dlt07);
		linearyLayout_dlt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this, DLTPeriodsActivity.class);
				_this.startActivity(intent);
			}
		});
	}

	private void initViewSDEL11to5() {
		// 山东11选5 view
		linearyLayout_sdel11to5 = (LinearLayout)findViewById(R.id.linearyLayout_sdel11to5);
		textview_lotteryName_sdel11to5 = (TextView) findViewById(R.id.textview_lotteryName_sdel11to5);
		textview_periodNumber_sdel11to5 = (TextView) findViewById(R.id.textview_periodNumber_sdel11to5);
		textview_time_sdel11to5 = (TextView) findViewById(R.id.textview_time_sdel11to5);
		textview_result_sdel11to501 = (TextView) findViewById(R.id.textview_result_sdel11to501);
		textview_result_sdel11to502 = (TextView) findViewById(R.id.textview_result_sdel11to502);
		textview_result_sdel11to503 = (TextView) findViewById(R.id.textview_result_sdel11to503);
		textview_result_sdel11to504 = (TextView) findViewById(R.id.textview_result_sdel11to504);
		textview_result_sdel11to505 = (TextView) findViewById(R.id.textview_result_sdel11to505);
		linearyLayout_sdel11to5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this,
						SDEL11to5PeriodsActivity.class);
				intent.putExtra("lottery", "14");
				_this.startActivity(intent);
			}
		});

	}
	private void initViewGDEL11to5() {
		// 山东11选5 view
		linearyLayout_gdel11to5 = (LinearLayout) findViewById(R.id.linearyLayout_gdel11to5);
		textview_lotteryName_gdel11to5 = (TextView) findViewById(R.id.textview_lotteryName_gdel11to5);
		textview_periodNumber_gdel11to5 = (TextView) findViewById(R.id.textview_periodNumber_gdel11to5);
		textview_time_gdel11to5 = (TextView) findViewById(R.id.textview_time_gdel11to5);
		textview_result_gdel11to501 = (TextView) findViewById(R.id.textview_result_gdel11to501);
		textview_result_gdel11to502 = (TextView)findViewById(R.id.textview_result_gdel11to502);
		textview_result_gdel11to503 = (TextView) findViewById(R.id.textview_result_gdel11to503);
		textview_result_gdel11to504 = (TextView) findViewById(R.id.textview_result_gdel11to504);
		textview_result_gdel11to505 = (TextView) findViewById(R.id.textview_result_gdel11to505);
		linearyLayout_gdel11to5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this,
						SDEL11to5PeriodsActivity.class);
				intent.putExtra("lottery", "19");
				_this.startActivity(intent);
			}
		});
		
	}

	private void initViewJCZQ() {
		// 竞彩足球 view
		linearyLayout_jczq = (LinearLayout) findViewById(R.id.linearLayout_jczq);
		textview_time_jczq = (TextView) findViewById(R.id.textview_time_jczq);
		textview_result_homeName = (TextView) findViewById(R.id.textview_result_homeName);
		textview_result_bifen = (TextView) findViewById(R.id.textview_result_bifen);
		textview_result_guestName = (TextView) findViewById(R.id.textview_result_guestName);
		linearyLayout_jczq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this, JCZQPeriodsRefreshActivity.class);
				_this.startActivity(intent);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}


}
