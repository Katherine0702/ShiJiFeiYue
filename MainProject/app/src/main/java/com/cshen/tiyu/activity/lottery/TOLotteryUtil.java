package com.cshen.tiyu.activity.lottery;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import com.cshen.tiyu.activity.lottery.pl35.Pl5MainActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCMainActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQAccountListActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQMainActivity;
import com.cshen.tiyu.activity.redpacket.RedPacketRainActivity;
import com.cshen.tiyu.activity.taocan.TaoCanMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.utils.ToastUtils;

public class TOLotteryUtil {
	private static TOLotteryUtil lottery;

	public static TOLotteryUtil getTOLotteryUtil() {
		if (lottery == null) {
			lottery = new TOLotteryUtil();
		}
		return lottery;
	}
	public void toUrl(Context context,String local, String lotteryId, String url,String playType) {
		if ("0".equals(local)) {
			if ("taocan".equals(url.trim())) {
				Intent intentHelp = new Intent(context,TaoCanMainActivity.class);
				context.startActivity(intentHelp);
			} else {
				Intent intentHelp = new Intent(context,LotteryTypeActivity.class);
				intentHelp.putExtra("url", url);
				context.startActivity(intentHelp);
			}
		}
		if ("1".equals(local)) {
			Intent intent;
			if ((ConstantsBase.DLT + "").equals(lotteryId)) {
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(context, ConstantsBase.CHOOSEDNUMBERS);
				if (list == null) {
					intent = new Intent(context, DLTMainActivity.class);
				} else {
					intent = new Intent(context,DLTAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("dltNumberList", list);
					intent.putExtras(bundle);
				}
				context.startActivity(intent);
			} else if ((ConstantsBase.SD115 + "").equals(lotteryId)) {
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(context, ConstantsBase.CHOOSEDNUMBERS115);
				if (list == null) {
					intent = new Intent(context, Main115Activity.class);
				} else {
					intent = new Intent(context,Account115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("number115List", list);
					intent.putExtras(bundle);
				}
				context.startActivity(intent);
			} else if ((ConstantsBase.GD115 + "").equals(lotteryId)) {
				ArrayList<Number115> list = (ArrayList<Number115>) ChooseUtil.getData(context,ConstantsBase.CHOOSEDNUMBERSGD115);
				if (list == null) {
					intent = new Intent(context, MainGd115Activity.class);
				} else {
					intent = new Intent(context,Accountgd115ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("numbergd115List", list);
					intent.putExtras(bundle);
				}
				context.startActivity(intent);
			} else if ((ConstantsBase.Fast3 + "").equals(lotteryId)) {
				ArrayList<NumberFast> list = (ArrayList<NumberFast>) ChooseUtil.getData(context,ConstantsBase.CHOOSEDNUMBERSFAST3);
				if (list == null) {
					intent = new Intent(context, Fast3MainActivity.class);
				} else {
					intent = new Intent(context,Fast3AccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean("frommain", true);
					bundle.putSerializable("numberfastList", list);
					intent.putExtras(bundle);
				}
				context.startActivity(intent);
			} else if ((ConstantsBase.JCZQ + "").equals(lotteryId)) {
				intent = new Intent(context, FootballMainActivity.class);
				context.startActivity(intent);
			} else if ((ConstantsBase.JCLQ + "").equals(lotteryId)) {
				intent = new Intent(context, BasketBallMainActivity.class);
				context.startActivity(intent);
			} else if ((ConstantsBase.SFC + "").equals(lotteryId)) {
				intent = new Intent(context, SFCMainActivity.class);
				intent.putExtra("playType", playType);
				context.startActivity(intent);
			} else if ((ConstantsBase.PL35 + "").equals(lotteryId)) {
				intent = new Intent(context, Pl5MainActivity.class);
				intent.putExtra("playType", playType);
				context.startActivity(intent);
			} else if ((ConstantsBase.SSQ + "").equals(lotteryId)) {
				ArrayList<DLTNumber> list = (ArrayList<DLTNumber>) ChooseUtil.getData(context, ConstantsBase.CHOOSEDNUMBERSSSQ);
				if (list == null) {
					intent = new Intent(context, SSQMainActivity.class);
				} else {
					intent = new Intent(context,SSQAccountListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ssqNumberList", list);
					intent.putExtras(bundle);
				}
				context.startActivity(intent);
			} else if ("4".equals(lotteryId)) {
			//	MobclickAgent.onEvent(context, "buttonredpacket");// 统计
				Intent intent4 = new Intent(context,RedPacketRainActivity.class);
				context.startActivity(intent4);
			} else {
				ToastUtils.showShort(context, "敬请期待......");
			}
		}
	}
}
