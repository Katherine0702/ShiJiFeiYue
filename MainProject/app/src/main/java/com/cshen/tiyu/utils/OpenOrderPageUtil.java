package com.cshen.tiyu.utils;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.JCLQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JCZQOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.cai115.EL11TO5OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.sfc.SFCOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQOrderDetailActivity;
import com.cshen.tiyu.activity.mian4.personcenter.orders.OrdersActivity;
import com.cshen.tiyu.base.ConstantsBase;



/**
 * 处理订单详情的跳转
 * @author Administrator
 *
 */
public class OpenOrderPageUtil {
    
	/**
     * 
     * @param context
     * @param lotteryType
     * @param schemeId
     * @param isSingleTask 是否是singletop模式，如推送
     * @param playtype 是否可以区分胜负彩和任选九 ， 值为null区别不了如推送 “0”：SFZC14 “1”：SFZC9
     */
	public void toOpenOrderPage(Context context,String lotteryType,String schemeId,boolean isSingleTask,String playType){
		Intent intent_order = null;
		if ((ConstantsBase.DLT+"").equals(lotteryType)  ) {
			intent_order  = new Intent(context,DLTOrderDetailActivity.class);
		}else if ((ConstantsBase.SD115+"").equals(lotteryType)||(ConstantsBase.GD115+"").equals(lotteryType)) {
			intent_order  = new Intent(context,EL11TO5OrderDetailActivity.class);
		}else if((ConstantsBase.Fast3+"").equals(lotteryType)) {
			intent_order  = new Intent(context,Fast3OrderDetailActivity.class);
		}else if ((ConstantsBase.JCZQ+"").equals(lotteryType)) {
			intent_order  = new Intent(context,JCZQOrderDetailActivity.class);
		}else if ((ConstantsBase.JCLQ+"").equals(lotteryType)) {
			intent_order  = new Intent(context,JCLQOrderDetailActivity.class);
		}else if ((ConstantsBase.SFC+"").equals(lotteryType)) {//胜负彩直接跳到所有订单页面
			if (!TextUtils.isEmpty(playType)) {
				intent_order  = new Intent(context,SFCOrderDetailActivity.class);
				intent_order.putExtra("playType", playType);
			}else {
				intent_order  = new Intent(context,OrdersActivity.class);
				intent_order.putExtra("arg0", 1);
			}
			
		}else if ((ConstantsBase.SSQ+"").equals(lotteryType)) {
			intent_order  = new Intent(context,SSQOrderDetailActivity.class);
		}else if ((ConstantsBase.PL35+"").equals(lotteryType)) {
			ToastUtils.showShort(context, "敬请期待...");
			intent_order  = new Intent(context,OrdersActivity.class);
			intent_order.putExtra("arg0", 1);
			return;
		}else{
			intent_order= new Intent(context,LotteryTypeActivity.class);
			String url = ConstantsBase.IPQT + "/#0/account/lotteryOrderDetail/"
					+ lotteryType + "_" + schemeId;
			intent_order.putExtra("url", url);
			context.startActivity(intent_order);
			return;
		}
		if (null != intent_order) {
			intent_order.putExtra("schemeId", Integer.parseInt(schemeId));
			intent_order.putExtra("onlyClose", true);
			intent_order.putExtra("lotteryId", lotteryType);
			if (isSingleTask) {//singletask模式
				intent_order.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent_order);
			return;
		}
	}
}
