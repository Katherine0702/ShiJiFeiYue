package com.cshen.tiyu.activity.lottery.sfc;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.football.FootballBFZBListActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ball.SchemeInfoDTO;
import com.cshen.tiyu.domain.ball.SchemeMatchDTO;
import com.cshen.tiyu.utils.DisplayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class SFCOrderDetailActivity extends OrderDetailActivity {
	private TableLayout table;
	private TextView textview_periodNumber,passType,bfzb,textview_notice;
	@Override
	public void onCreateView() {
		setContentView(R.layout.activity_detail_jczq);
		initView();
	}
	private void initView() {
		table = (TableLayout) findViewById(R.id.tablelayout);
		textview_periodNumber = (TextView) findViewById(R.id.textview_periodNumber);
		passType= (TextView) findViewById(R.id.textView_passTypeText);
		bfzb = (TextView) findViewById(R.id.bfzb);
		bfzb.setVisibility(View.GONE);
		bfzb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				Intent intentHelp = new Intent(_this,FootballBFZBListActivity.class);
				intentHelp.putExtra("schemeId",schemeId);
				intentHelp.putExtra("caizhong",ConstantsBase.SFC);
				startActivity(intentHelp);
			}
		});
		textview_notice = (TextView) findViewById(R.id.textview_notice);
		textview_notice.setText("实际赔率以出票赔率数据为准");
	}
	@Override
	public void setData(String result,String state){
		textview_periodNumber.setText("第"+scheme.getPeriodNumber()+"期");
		String content = scheme.getContent();
		if (!TextUtils.isEmpty(content)) {
			Gson g = new Gson();
			List<SchemeInfoDTO> contents = g.fromJson("["+ content + "]",new TypeToken<List<SchemeInfoDTO>>() {}.getType());
			if (contents != null && contents.size() > 0) {
				initTable(contents,scheme.getPlayTypeOrdinal(),state);
			}
		}
	}
	protected void initTable(List<SchemeInfoDTO> contents,Integer playType,String state){
		passType.setText("过关方式："+ contents.get(0).getPassTypeText());
		table.setStretchAllColumns(true);
		List<SchemeMatchDTO> items = contents.get(0).getItems();
		StringBuilder bets = new StringBuilder("");
		if (items != null && items.size() > 0) {
			for (int i = 0; i < items.size() + 1; i++) {
				if ((i >= 1 && i < items.size())
						&& items.get(i).getMatchKey()
						.equals(items.get(i - 1).getMatchKey())) {
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
					testview.setTextSize(12);
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
							testview.setText("主队");
						}
						if (j == 2) {
							testview.setText("比分");
						}
						if (j == 3) {
							testview.setText("客队");
						}
						if (j == 4) {
							testview.setText("投注内容");
							/*String result = "SUCCESS".equals(state)?"出票SP":"预计SP";
							StringBuilder stateStr = new StringBuilder("");
							stateStr.append("<html>");
							stateStr.append("投注内容");
							stateStr.append("<br/>");
							stateStr.append("<font color='#f04d46'> ");
							stateStr.append(result);
							stateStr.append("</font></html>");*/
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
							ttview.setTextSize(12);
							ttview.setPadding(DisplayUtil.dip2px(_this, 10),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 10),
									DisplayUtil.dip2px(_this, 5));
							String macheKey = temp.getMatchKey().trim();

							if (!TextUtils.isEmpty(macheKey)) {
								ttview.setText(macheKey);
							}
							layout.addView(ttview);// 添加组件
							tablerow.addView(layout, tableLayout);
						}
						if (j == 1) {
							testview.setText(temp.getHomeTeamName());
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
									score.append(temp.getHomeScore()
											+ ":" + temp.getGuestScore());
									score.append("</font>");
								}
							}
							testview.setText(Html.fromHtml(score.toString()));
						}
						if (j == 3) {
							testview.setText(temp.getGuestTeamName());
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
							ttview.setTextSize(10);
							ttview.setBackgroundResource(R.drawable.jczq_table_shape);
							ttview.setGravity(Gravity.CENTER);
							ttview.setPadding(DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5),
									DisplayUtil.dip2px(_this, 5));
							bets.append(getPlayBet(temp,playType));
							ttview.setText(Html.fromHtml(bets.toString()));
							bets = new StringBuilder("");

							if (temp.getDan()) {
								TextView txtview = new TextView(this);// 定义组件
								txtview.setText("胆");
								txtview.setPadding(DisplayUtil.dip2px(_this, 2),0,0,0);
								txtview.setBackgroundResource(R.mipmap.dan);
								txtview.setTextSize(9);
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

	}
	private String getPlayBet(SchemeMatchDTO temp,Integer playTypeInt) {
		String tempStr="";
		StringBuilder playType = new  StringBuilder("");
		try {
			if ((playTypeInt!=null && playTypeInt == 4) || (playTypeInt!=null && playTypeInt == 0) ) {
				playType = new StringBuilder("<string name='str'>") ;
				playType.append(tempStr).append("<string name='str'>");
			}
			if (temp.getResult() != null && !"".equals(temp.getResult())) {

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
										playType.append("<font color='#f04d46'>" + betTemps[i]
												+ "</font>");
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
								playType.append("<font color='#f04d46'>" + temp.getBet()
										+ "</font>");
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
