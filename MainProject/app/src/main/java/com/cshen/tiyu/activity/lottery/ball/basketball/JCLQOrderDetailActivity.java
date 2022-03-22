package com.cshen.tiyu.activity.lottery.ball.basketball;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.OrderDetailActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ball.SchemeInfoDTO;
import com.cshen.tiyu.domain.ball.SchemeMatchDTO;
import com.cshen.tiyu.utils.DisplayUtil;
import com.cshen.tiyu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class JCLQOrderDetailActivity extends OrderDetailActivity {
	private TableLayout table;
	private TextView playType,passType, bfzb,btn_go_cpxq,textview_notice;
	private boolean chaidantableShow = true;
	private TableLayout chaidantable;
	private View chaidanview,chaidantxt;
	private ImageView chaidanim;
	@Override
	public void onCreateView() {
		setContentView(R.layout.activity_detail_jczq);
		initView();
	}
	private void initView() {
		table = (TableLayout) findViewById(R.id.tablelayout);
		playType = (TextView) findViewById(R.id.textview_periodNumber);
		passType = (TextView) findViewById(R.id.textView_passTypeText);
		bfzb = (TextView) findViewById(R.id.bfzb);
		bfzb.setVisibility(View.VISIBLE);
		bfzb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				Intent intentHelp = new Intent(_this,BasketBallBFZBListActivity.class);
				intentHelp.putExtra("schemeId",schemeId);
				intentHelp.putExtra("caizhong",ConstantsBase.JCLQ);
				startActivity(intentHelp);
			}
		});
		chaidanview = findViewById(R.id.chaidanview);
		chaidantxt = findViewById(R.id.chaidantxt);
		chaidanim = (ImageView) findViewById(R.id.chaidanim);
		chaidantxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!chaidantableShow){
					chaidantableShow = true;
					chaidanim.setImageResource(R.mipmap.main115choose);
					chaidantable.setVisibility(View.VISIBLE);
				}else{
					chaidantableShow = false;
					chaidanim.setImageResource(R.mipmap.main115chooseno);
					chaidantable.setVisibility(View.GONE);
				}
			}
		});
		chaidantable = (TableLayout) findViewById(R.id.chaidantablelayout);
		btn_go_cpxq = (TextView) findViewById(R.id.btn_go_cpxq);
		btn_go_cpxq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_this, JCLQOrderTicketDetailActivity.class);
				intent.putExtra("wanfa",playType.getText().toString());
				intent.putExtra("caizhong",lotteryName);
				intent.putExtra("schemeId",schemeId);
				startActivity(intent);
			}
		});
		textview_notice = (TextView) findViewById(R.id.textview_notice);
		textview_notice.setText("实际赔率、让分值、预设总分，以出票时数据为准");
	}
	@Override
	public void setData(String result,String state){
		playType.setText(Util.getJCLQPlayTypeString(scheme.getPlayTypeOrdinal()));
		if("SUCCESS".equals(scheme.getSchemePrintState())
				&&!"MANUAlESUCCESS".equals(scheme.getState())){
			btn_go_cpxq.setVisibility(View.VISIBLE);
		}else{
			btn_go_cpxq.setVisibility(View.GONE);
		}
		String content = scheme.getContent();
		if (!TextUtils.isEmpty(content)) {
			Gson g = new Gson();
			List<SchemeInfoDTO> contents = g.fromJson("["+ content + "]",new TypeToken<List<SchemeInfoDTO>>(){}.getType());
			if (contents != null && contents.size() > 0) {
				initTable(contents,scheme.getPlayTypeOrdinal(),scheme.isCopyContentShow(),state);
			}
		}
	}

	protected void initTable(List<SchemeInfoDTO> contents,Integer playType,boolean isShow,String state) {
		passType.setText("过关方式："+ contents.get(0).getPassTypeText());
		table.setStretchAllColumns(true);
		List<SchemeMatchDTO> items = contents.get(0).getItems();
		StringBuilder bets = new StringBuilder("");
		if (items != null && items.size() > 0) {
			for (int i = 0; i < items.size() + 1; i++) {
				if ((i >= 1 && i < items.size())&& items.get(i).getMatchKey().equals(items.get(i - 1).getMatchKey())) {
					bets.append(getPlayBet(items.get(i - 1),playType));
					bets.append("<br/>");
					continue;
				} else {
					if (i == 0) {
						bets = new StringBuilder("");
					}
				}
				TableRow tablerow = new TableRow(_this);
				tablerow.setBackgroundColor(Color.WHITE);
				for (int j = 0; j < 5; j++) {

					TextView testview = new TextView(_this);

					testview.setMaxEms(5);
					testview.setBackgroundResource(R.drawable.jczq_table_shape);
					testview.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.MATCH_PARENT));
					testview.setGravity(Gravity.CENTER);
					testview.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
					testview.setPadding(DisplayUtil.dip2px(_this, 6),
							DisplayUtil.dip2px(_this, 5),
							DisplayUtil.dip2px(_this, 6),
							DisplayUtil.dip2px(_this, 5));
					if (i == 0) {
						tablerow.setBackgroundColor(getResources().getColor(R.color.jczq_table_back));
						testview.setPadding(DisplayUtil.dip2px(_this, 8),
								DisplayUtil.dip2px(_this, 5),
								DisplayUtil.dip2px(_this, 8),
								DisplayUtil.dip2px(_this, 5));
						testview.setTextColor(Color.BLACK);
						if (j == 0) {
							testview.setText("场次");
						}
						if (j == 1) {
							testview.setText("客队");
						}
						if (j == 2) {
							testview.setText("比分");
						}
						if (j == 3) {
							testview.setText("主队");
						}
						if (j == 4) {
							String result = "SUCCESS".equals(state)?"出票SP":"预计SP";	
							StringBuilder stateStr = new StringBuilder("");
							stateStr.append("<html>");
							stateStr.append("投注内容");
							stateStr.append("<br/>");
							stateStr.append("<font color='#f04d46'> ");
							stateStr.append(result);
							stateStr.append("</font></html>");
							testview.setText(Html.fromHtml(stateStr.toString()));
						}
						tablerow.addView(testview);
					} else {
						SchemeMatchDTO temp = items.get(i - 1);
						if (j == 0) {
							TableRow.LayoutParams tableLayout = new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.MATCH_PARENT);

							FrameLayout layout = new FrameLayout(this);// 定义框架布局器
							FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);// 定义框架布局器参数
							FrameLayout.LayoutParams tparams = new FrameLayout.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);// 定义显示组件参数

							TextView ttview = new TextView(_this);

							ttview.setBackgroundResource(R.drawable.jczq_table_shape);
							testview.setLayoutParams(new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.MATCH_PARENT));
							ttview.setGravity(Gravity.CENTER);
							ttview.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
							ttview.setPadding(DisplayUtil.dip2px(_this, 10),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 10),
									DisplayUtil.dip2px(_this, 5));
							String macheKey = temp.getMatchKey().trim();

							if (!TextUtils.isEmpty(macheKey)) {
								try {
									ttview.setText(Html.fromHtml(macheKey.substring(0, macheKey.length() - 3)+"<br>"+ 
													macheKey.substring(macheKey.length() - 3,macheKey.length())));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							layout.addView(ttview);// 添加组件
							tablerow.addView(layout, tableLayout);
						}
						if (j == 1) {
							testview.setText(temp.getGuestTeamName());
						}
						if (j == 2) {
							StringBuilder score = new StringBuilder("");

							if (TextUtils.isEmpty(temp.getResult())) {
								score.append("<font color='#f04d46'> ");
								score.append("未开奖");
								score.append("</font>");
							} else {
								if (temp.getHomeScore() != null
										&& temp.getGuestScore() != null) {
									score.append("<font color='#f04d46'> ");
									score.append(temp.getGuestScore()
											+ ":" + temp.getHomeScore());
									score.append("</font>");
								}
							}
							testview.setText(Html.fromHtml(score.toString()));
						}
						if (j == 3) {
							testview.setText(temp.getHomeTeamName());
						}						
						if (j == 4) {
							TableRow.LayoutParams tableLayout = new TableRow.LayoutParams(
									TableRow.LayoutParams.WRAP_CONTENT,
									TableRow.LayoutParams.MATCH_PARENT);

							FrameLayout layout = new FrameLayout(this);// 定义框架布局器
							FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);// 定义框架布局器参数
							FrameLayout.LayoutParams tparams = new FrameLayout.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT,
									ViewGroup.LayoutParams.WRAP_CONTENT);// 定义显示组件参数

							TextView ttview = new TextView(_this);
							ttview.setMaxEms(5);
							ttview.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
							ttview.setBackgroundResource(R.drawable.jczq_table_shape);
							ttview.setGravity(Gravity.CENTER);
							ttview.setPadding(DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5));
							bets.append(getPlayBet(temp,playType));
							if(isShow){
								ttview.setText(Html.fromHtml(bets.toString()));
							}else{
								ttview.setText("保密");
							}
							bets = new StringBuilder("");
							if (temp.getDan()) {
								TextView txtview = new TextView(this);// 定义组件
								txtview.setText("胆");
								txtview.setPadding(DisplayUtil.dip2px(_this, 2),0,0,0);
								txtview.setBackgroundResource(R.mipmap.dan);
								txtview.setTextSize(TypedValue.COMPLEX_UNIT_DIP,9);
								txtview.setTextColor(getResources().getColor(R.color.white));
								layout.addView(txtview, tparams);// 添加组件
							}
							layout.addView(ttview);// 添加组件
							tablerow.addView(layout, tableLayout);
						}
						if (j != 0 & j != 4) {
							tablerow.addView(testview);
						}
					}
				}
				table.addView(tablerow);
			}
		}
		if(!TextUtils.isEmpty(contents.get(0).getBet())&&(ConstantsBase.JCLQ+"").equals(lotteryId)){
			initChaiDanTable(contents.get(0).getBet());
			chaidanview.setVisibility(View.VISIBLE);
		}else{
			chaidanview.setVisibility(View.GONE);
		}
	}
	protected void initChaiDanTable(String chaidan) {
		chaidantable.setStretchAllColumns(true);
		String[] items = chaidan.split("\\\r\\\n");
		if (items != null && items.length > 3) {
			for (int i = 2; i < items.length; i++) {
				TableRow tablerow = new TableRow(_this);
				tablerow.setBackgroundColor(Color.WHITE);
				for (int j = 0; j < 2; j++) {
					TextView testview = new TextView(_this);
					testview.setBackgroundResource(R.drawable.jczq_table_shape);
					testview.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.MATCH_PARENT));
					testview.setGravity(Gravity.CENTER);
					testview.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
					testview.setPadding(DisplayUtil.dip2px(_this, 6),
							DisplayUtil.dip2px(_this, 5),
							DisplayUtil.dip2px(_this, 6),
							DisplayUtil.dip2px(_this, 5));
					if (i == 2) {
						tablerow.setBackgroundColor(getResources().getColor(R.color.jczq_table_back));
						if (j == 0) {
							testview.setText("拆单内容");
						}
						if (j == 1) {
							testview.setText("倍数");
						}
						tablerow.addView(testview);
					} else {
						String temp = items[i];
						String[] temps = null ;
						if(temp.contains(",")){
							temps = temp.split(",");
						}else{
							temps = new String[]{"",""};
						}
						if (j == 0) {
							StringBuffer content = new StringBuffer();
							for(int k = 0;k<temps.length-1;k++){
								if(k!=temps.length-2){
									content.append(temps[k]+"\n");
								}else{
									content.append(temps[k]);
								}
							}
							testview.setText(content.toString());
						}
						if (j == 1) {
							testview.setText(temps == null?"":temps[temps.length-1]);
						}
						tablerow.addView(testview);
					}
				}
				chaidantable.addView(tablerow);
			}
		}
	}
	private String getPlayBet(SchemeMatchDTO temp,Integer playTypeInt) {
		String tempStr="";
		StringBuilder playType = new  StringBuilder("");
		try {
			if ((playTypeInt!=null && playTypeInt == 4) || (playTypeInt!=null && playTypeInt == 0) ) {
				playType = new StringBuilder("<string name='str'>") ;
				switch (temp.getPlayType()) {
				case 0:
					tempStr = "胜负:";
					break;
				case 1:
					double handicap = 0.0;
					try{
						handicap = Double.parseDouble(temp.getHandicap());
					}catch(Exception e){
						e.printStackTrace();
					}
					if(handicap<=0){
						tempStr =  "<html><font color=\"#666666\">让分"
								+"</font><font color=\"#529AF2\">("+temp.getHandicap()
								+ ")</font><font color=\"#666666\">:</font></html>";
					}else{
						tempStr = "<html><font color=\"#666666\">让分"
								+"</font><font color=\"#f2826a\">(+"+temp.getHandicap()
								+ ")</font><font color=\"#666666\">:</font></html>";
					}
					break;
				case 2:
					tempStr = "胜分差:";
					break;
				case 3:
					tempStr = "大小分("+temp.getHandicap()+"):";
					break;
				case 4:
					tempStr ="混合串:";
					break;
				default:
					tempStr = "";
					break;
				}
				playType.append(tempStr).append("<string name='str'>");
			}
			if (temp.getResult() != null && !"".equals(temp.getResult())) {
				if(playTypeInt == 3){
					playType.append("大小分("+temp.getHandicap()+"):");
					playType.append("<br/>");
				}
				String bet = temp.getBet();
				if (bet.contains(",")) {
					String[] betTemps = bet.split(",");
					if (betTemps!=null) {
						for (int i = 0; i < betTemps.length; i++) {
							playType.append("<string name='str'>");
							if (i!=0) {
								playType.append("<br/>");
								playType.append(tempStr);
							}
							if (temp.getResult().split("\\(").length > 0) {
								if (temp.getResult().split("\\(")[0].equals(betTemps[i].split("\\(")[0])) {
									if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("胜") ) {
										playType.append("<font color='#f04d46'>").append("主胜(").append(betTemps[i].split("\\(")[1]).append("</font>");
									}else if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("负") ) {
										playType.append("<font color='#f04d46'>").append("客胜(").append(betTemps[i].split("\\(")[1]).append("</font>");
									}else{
										playType.append("<font color='#f04d46'>" + betTemps[i]+ "</font>");
									}
								} else {
									if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("胜") ) {
										playType.append("主胜(").append(betTemps[i].split("\\(")[1]);
									}else if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("负") ) {
										playType.append("客胜(").append(betTemps[i].split("\\(")[1]);
									}else{
										playType.append(betTemps[i]);
									}
								}
							}
							playType.append("</string>");
						}
					}
				}else{
					if (temp.getResult().split("\\(").length > 0) {
						if (temp.getResult().split("\\(")[0].equals(temp.getBet().split("\\(")[0])) {
							if (bet.contains("(") && bet.split("\\(")[0].equals("胜") ) {
								playType.append("<font color='#f04d46'>").append("主胜(").append(bet.split("\\(")[1]).append("</font>");
							}else if (bet.contains("(") && bet.split("\\(")[0].equals("负") ) {
								playType.append("<font color='#f04d46'>").append("客胜(").append(bet.split("\\(")[1]).append("</font>");
							}else{
								playType.append("<font color='#f04d46'>" + temp.getBet()+ "</font>");
							}
						} else {
							if (bet.contains("(") && bet.split("\\(")[0].equals("胜") ) {
								playType.append("主胜(").append(bet.split("\\(")[1]);
							}else if (bet.contains("(") && bet.split("\\(")[0].equals("负") ) {
								playType.append("客胜(").append(bet.split("\\(")[1]);
							}else{
								playType.append(bet);
							}
						}
					}
				}
			} else {
				if(playTypeInt == 3){
					playType.append("大小分("+temp.getHandicap()+"):");
					playType.append("<br/>");
				}
				String bet = temp.getBet();
				if (bet.contains(",")) {
					String[] betTemps = bet.split(",");
					if (betTemps!=null) {
						for (int i = 0; i < betTemps.length; i++) {
							playType.append("<string name='str'>");
							if (i!=0) {
								playType.append("<br/>");
								playType.append(tempStr);
							}
							if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("胜") ) {
								playType.append("主胜(").append(betTemps[i].split("\\(")[1]);
							}else if (betTemps[i].contains("(") && betTemps[i].split("\\(")[0].equals("负") ) {
								playType.append("客胜(").append(betTemps[i].split("\\(")[1]);
							}else{
								playType.append(betTemps[i]);
							}
							playType.append("</string>");
						}
					}
				}else{
					if (bet.contains("(") && bet.split("\\(")[0].equals("胜") ) {
						playType.append("主胜(").append(bet.split("\\(")[1]);
					}else if (bet.contains("(") && bet.split("\\(")[0].equals("负") ) {
						playType.append("客胜(").append(bet.split("\\(")[1]);
					}else{
						playType.append(bet);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return playType.toString().trim();
	}
}
