package com.cshen.tiyu.activity.mian4.personcenter.zhuihao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.Fast3.ChooseUtilFast3;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3AccountListActivity;
import com.cshen.tiyu.activity.lottery.Fast3.Fast3OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.cai115.Account115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.Accountgd115ListActivity;
import com.cshen.tiyu.activity.lottery.cai115.EL11TO5OrderDetailActivity;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.activity.lottery.dlt.DLTAccountListActivity;
import com.cshen.tiyu.activity.lottery.dlt.DLTOrderDetailActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQAccountListActivity;
import com.cshen.tiyu.activity.lottery.ssq.SSQOrderDetailActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.cai115.El11to5CompoundContent;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.domain.dltssq.DltCompoundContent;
import com.cshen.tiyu.domain.dltssq.NumberEach;
import com.cshen.tiyu.domain.fast3.Fast3CompoundContent;
import com.cshen.tiyu.domain.fast3.NumberFast;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.order.OrderDetail;
import com.cshen.tiyu.domain.order.OrderDetailEach;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AddOrderDetailActivity extends BaseActivity{
	AddOrderDetailActivity _this;
	private TopViewLeft tv_head;//头
	LinearLayout listview;
	View first;
	OrderDetail orderdetail;
	private ArrayList<OrderDetailEach> orderLists;
	ImageView icon;
	TextView name,state,xianzhi;
	TextView touzhumoney,prizemoney;
	TextView addperiod;
	TextView fisrtstate,periodnumber,touzhumoneyeach,touzhutime,touzhunumber;
	TextView addcontinue;
	String chaseId;
	int lotteryId;
	String playType;
	String totalAccount;
	private int defaultBit;
	ArrayList<DLTNumber> dltnumbers = new ArrayList<DLTNumber>();
	ArrayList<Number115> number115s = new ArrayList<Number115>();
	ArrayList<NumberFast> numberfastList = new ArrayList<NumberFast>();

	DecimalFormat df;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail);
		_this = this;
		df = new DecimalFormat("0.00");
		initView();
		initdata();
	}
	private void initView() {
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false ,false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {}
			@Override
			public void clickContactView(View view) {}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		icon = (ImageView) findViewById(R.id.icon);
		name = (TextView) findViewById(R.id.name);
		state = (TextView) findViewById(R.id.state);
		xianzhi = (TextView) findViewById(R.id.xianzhi);

		touzhumoney = (TextView) findViewById(R.id.touzhumoney);
		prizemoney = (TextView) findViewById(R.id.prizemoney);
		addperiod = (TextView) findViewById(R.id.addperiod);
		fisrtstate = (TextView) findViewById(R.id.fisrtstate);
		periodnumber = (TextView) findViewById(R.id.periodnumber);
		touzhumoneyeach = (TextView) findViewById(R.id.touzhumoneyeach);
		touzhutime = (TextView) findViewById(R.id.touzhutime);
		touzhunumber = (TextView) findViewById(R.id.touzhunumber);

		first = findViewById(R.id.first);
		first.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				if (lotteryId == ConstantsBase.SD115) {
					intent = new Intent(_this, EL11TO5OrderDetailActivity.class);
				}
				if (lotteryId == ConstantsBase.GD115) {
					intent = new Intent(_this, EL11TO5OrderDetailActivity.class);
				}
				if (lotteryId == ConstantsBase.DLT) {
					intent = new Intent(_this, DLTOrderDetailActivity.class);
				}
				if (lotteryId == ConstantsBase.SSQ) {
					intent = new Intent(_this, SSQOrderDetailActivity.class);
				}

				if (lotteryId == ConstantsBase.Fast3) {
					intent = new Intent(_this, Fast3OrderDetailActivity.class);
				}
				if (null != intent) {
					try {
						intent.putExtra("onlyClose", true);
						intent.putExtra("schemeId",Integer.parseInt(orderdetail.getFirstOrderId()));
					} catch (Exception e) {
						e.printStackTrace();
						intent.putExtra("schemeId", -1);
					}
					intent.putExtra("lotteryId", lotteryId + "");
					startActivity(intent);
				}
			}
		});
		listview = (LinearLayout)findViewById(R.id.listview);
		addcontinue = (TextView) findViewById(R.id.addcontinue);
		addcontinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				if (ConstantsBase.DLT == lotteryId) {
					intent  = new Intent(_this,DLTAccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("dltNumberList", dltnumbers);
					intent.putExtras(bundle);
				}else if (ConstantsBase.SSQ == lotteryId) {					
					intent = new Intent(_this, SSQAccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("ssqNumberList", dltnumbers);
					intent.putExtras(bundle);
				}else if (ConstantsBase.Fast3 == lotteryId) {					
					intent = new Intent(_this, Fast3AccountListActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("numberfastList", numberfastList);
					intent.putExtras(bundle);
				}else if (ConstantsBase.SD115 == lotteryId) {
					if(!"选三前直".equals(orderdetail.getPlayType())&&
							!"直三前选".equals(orderdetail.getPlayType())){
						intent = new Intent(_this, Account115ListActivity.class);
						Bundle bundle = new Bundle();   
						bundle.putSerializable("number115List", number115s);
						intent.putExtras(bundle);
					}
				}else if(ConstantsBase.GD115 == lotteryId){
					if(!"选三前直".equals(orderdetail.getPlayType())&&
							!"直三前选".equals(orderdetail.getPlayType())){
						intent = new Intent(_this, Accountgd115ListActivity.class);
						Bundle bundle = new Bundle();   
						bundle.putSerializable("numbergd115List", number115s);
						intent.putExtras(bundle);
					}
				}
				if (null != intent) {
					startActivity(intent);
					_this.finish();
				}else{
					ToastUtils.showShort(_this, "敬请期待...");
				}
			}
		});
	}
	public void initdata(){
		Intent intent = getIntent();
		lotteryId = intent.getIntExtra("lotteryId", -1);
		playType = intent.getStringExtra("playType");
		chaseId = intent.getStringExtra("chaseId");
		totalAccount  = intent.getStringExtra("totalAccount");
		initForList();
	}
	private void initForList(){
		ServiceCaiZhongInformation.getInstance().getAddOrderDetail(_this,chaseId,lotteryId+"",
				new CallBack<OrderDetail>() {

			@Override
			public void onSuccess(OrderDetail t) {
				// TODO 自动生成的方法存根
				orderdetail = t;
				setValue();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg+"");

			}
		});
	}
	public void setValue(){
		String lotteryName = Util.getLotteryName(Util
				.getLotteryTypeToString(lotteryId));
		if(lotteryId == ConstantsBase.PL35){
			if(!TextUtils.isEmpty(playType)
					&&playType.contains("排五")){
				lotteryName = "排列5";
			}else{
				lotteryName = "排列3";
			}
		}
		name.setText(lotteryName);
		switch(lotteryId){
		case ConstantsBase.DLT:
			defaultBit = R.mipmap.dlticon;
			break;
		case ConstantsBase.SSQ:
			defaultBit = R.mipmap.ssq;
			break;
		case ConstantsBase.SD115:
			defaultBit = R.mipmap.sd115icon;
			break;
		case ConstantsBase.GD115:
			defaultBit = R.mipmap.gd115icon;
			break;
		case ConstantsBase.Fast3:
			defaultBit = R.mipmap.fast3icon;
			break;
		case ConstantsBase.PL35:
			if(!TextUtils.isEmpty(playType)
					&&playType.contains("排五")){
				defaultBit = R.mipmap.pl5icon;
			}else{
				defaultBit = R.mipmap.pl3icon;
			}
			break;
		default:
			defaultBit = R.mipmap.ic_error;
			break;
		}
		icon.setImageResource(defaultBit);
		LotteryTypeData currentLotteryTypeData = MyDbUtils
				.getCurrentLotteryTypeData();
		if (currentLotteryTypeData!=null && currentLotteryTypeData.getLotteryList()!=null && currentLotteryTypeData.getLotteryList().size()>0) {
			for (int i = 0; i < currentLotteryTypeData.getLotteryList().size(); i++) {
				if (lotteryName!=null && lotteryName.equals(currentLotteryTypeData.getLotteryList().get(i).getTitle()) ) {
					xUtilsImageUtils.display(icon,defaultBit,
							currentLotteryTypeData.getLotteryList().get(i).getIcon());
				}
			}
		}

		String prizeForWonStop = "";// 中奖金额大于该金额才停止追号
		float prizeForWonStopF = Float.parseFloat(
				TextUtils.isEmpty(orderdetail.getPrizeForWonStop())?"0.00":orderdetail.getPrizeForWonStop());
		int prizeForWonStopI = (int)prizeForWonStopF;
		if(Float.parseFloat((prizeForWonStopI+"")) == prizeForWonStopF){
			if(prizeForWonStopI == 0){
				prizeForWonStopI = 1;
			}
			prizeForWonStop = prizeForWonStopI+"元";
		}else{
			prizeForWonStop = prizeForWonStopF+"元";
		}
		if(orderdetail.isWonStop()){
			xianzhi.setText("(中>="+prizeForWonStop+"停止)");
		}else{
			xianzhi.setText("(完成方案停止)");
		}
		state.setText("STOPED".equals(orderdetail.getChaseState())?"完成追号":"追号中");
		touzhumoney.setText(totalAccount+"元");
		String wonPrize = df.format(orderdetail.getWonPrice());
		if("0.00".equals(wonPrize)){
			prizemoney.setText("0.00元");
		}else{
			prizemoney.setText(wonPrize+"元");
			prizemoney.setTextColor(getResources()
					.getColor(R.color.mainred));
			prizemoney.setAlpha(1);
		}
		String addperiodStr = "<html><font color=\"#000000\">已追"
				+"</font><font color=\"#ff0000\">"+orderdetail.getSuccessCount()
				+ "</font><font color=\"#000000\">期/共</font></font><font color=\"#ff0000\">"+orderdetail.getTotalChaseCount()
				+ "</font><font color=\"#000000\">期</font></html>";
		addperiod.setText(Html.fromHtml(addperiodStr));

		periodnumber.setText("第"+orderdetail.getFirstPeriodNumber()+"期");
		touzhumoneyeach.setText("投注金额："+df.format(orderdetail.getFirstSchemeCost())+"元");
		touzhutime.setText("投注时间："+orderdetail.getFirstCreateTime());
		if(lotteryId == ConstantsBase.DLT){
			touzhunumber.setText(Html
					.fromHtml(""+getStringNumber(5)));
		}if(lotteryId == ConstantsBase.SSQ){
			touzhunumber.setText(Html
					.fromHtml(""+getStringNumber(6)));
		}if(lotteryId == ConstantsBase.SD115||lotteryId == ConstantsBase.GD115){
			touzhunumber.setText(Html
					.fromHtml(""+getStringNumber115()));
		}if(lotteryId == ConstantsBase.Fast3){
			touzhunumber.setText(Html
					.fromHtml(""+getStringNumberFast3()));
		}
		String prizeType = orderdetail.getFirstIsWon();
		if(!TextUtils.isEmpty(prizeType)){
			if(prizeType.contains("元")){
				fisrtstate.setTextColor(getResources()
						.getColor(R.color.mainred));
			}
			fisrtstate.setText(orderdetail.getFirstIsWon());
		}
		orderLists = orderdetail.getChaseOrderList();
		setDate();
	}
	public String getStringNumber(int count){
		String number = "";

		String content = orderdetail.getFirstContent();
		StringBuilder showContent = new StringBuilder("");
		if (content != null && content.contains("[")) {
			Gson g = new Gson();
			List<DltCompoundContent> contents = g.fromJson(content,
					new TypeToken<List<DltCompoundContent>>() {
			}.getType());
			getDltNumbers(contents,count);
			if (contents != null && contents.size() > 0) {
				for (int i = 0; i < contents.size(); i++) {
					if(i==5){
						showContent.append("...");
						break;
					}

					if (checkContents(contents.get(i).getRedDanList())) {
						showContent.append("<font color='#FF3232'>  (胆) </font>");
						for (int j = 0; j < contents.get(i).getRedDanList().size(); j++) {
							showContent.append(contents.get(i).getRedDanList().get(j)+" ");
						}
						showContent.append("|");
					}
					if (checkContents(contents.get(i).getRedList())) {
						for (int j = 0; j < contents.get(i).getRedList().size(); j++) {
							showContent.append(contents.get(i).getRedList().get(j)+" ");
						}
					}
					showContent.append(" + ");
					if (checkContents(contents.get(i).getBlueDanList())) {
						for (int j = 0; j < contents.get(i).getBlueDanList().size(); j++) {
							showContent.append(contents.get(i).getBlueDanList().get(j)+" ");
						}
						showContent.append("|");
					}
					if (checkContents(contents.get(i).getBlueList())) {
						for (int j = 0; j < contents.get(i).getBlueList().size(); j++) {
							showContent.append(contents.get(i).getBlueList().get(j)+" ");
						}
					}
					if (i!=contents.size()-1) {
						showContent.append("<br/> ");
					}
				}
			}

		} else {
			String[] numberContent = content.split(" ");

			DLTNumber dltNumber = new DLTNumber();
			NumberEach qianqu = new NumberEach();
			qianqu.setType(0);
			NumberEach houqu = new NumberEach();
			houqu.setType(1);

			if (numberContent!=null && numberContent.length>=7) {
				for (int i = 0; i < numberContent.length; i++) {
					if (i<count) {
						showContent.append(numberContent[i]+" ");
						qianqu.getNumbers().add(Integer.parseInt(numberContent[i]));
					}
					if (i == count) {
						showContent.append(" + ");
					}
					if (i >= count) {
						showContent.append(numberContent[i]+" ");
						houqu.getNumbers().add(Integer.parseInt(numberContent[i]));
					}
				}
			}
			dltNumber.setQianqu(qianqu);
			dltNumber.setHouqu(houqu);		
			if(count == 5){
				dltNumber.setNum(ChooseUtil.getChooseUtil().getTime(count,numberContent.length-count));
			}else{
				dltNumber.setNum(ChooseUtil.getChooseUtil().getTimeSSQ(count,numberContent.length-count));
			}
			dltnumbers.add(dltNumber);
		}
		number = showContent.toString();

		return number;
	}
	private boolean checkContents(List<String> contents) {
		if (contents != null && contents.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	private String getContentNumberString(String number, String[] resultNumbers) {
		number = Util.periodNumberFromat(number);
		if (resultNumbers != null && resultNumbers.length > 0) {
			for (int i = 0; i < resultNumbers.length; i++) {
				if (number.equals( Util.periodNumberFromat(resultNumbers[i]))) {
					number = "<font color='#FF3232'>" + number + "</font>";
				}
			}
		}
		return number;
	}

	public void getDltNumbers(List<DltCompoundContent> contents,int count){
		for(DltCompoundContent content:contents){
			DLTNumber dltnumber = new DLTNumber();
			NumberEach qianqu = new NumberEach();
			qianqu.setType(0);
			NumberEach houqu = new NumberEach();
			houqu.setType(1);

			for(String i : content.getRedList()){
				qianqu.getNumbers().add(Integer.parseInt(i));
			}
			for(String j : content.getBlueList()){
				houqu.getNumbers().add(Integer.parseInt(j));
			}
			dltnumber.setQianqu(qianqu);
			dltnumber.setHouqu(houqu);
			if(count == 5){
				dltnumber.setNum(ChooseUtil.getChooseUtil().
						getTime(content.getRedList().size(),content.getBlueList().size()));
			}else{
				dltnumber.setNum(ChooseUtil.getChooseUtil().
						getTimeSSQ(content.getRedList().size(),content.getBlueList().size()));
			}

			dltnumbers.add(dltnumber);
		}
	}
	public String getStringNumber115(){
		String wanfa = orderdetail.getPlayType();
		String content= orderdetail.getFirstContent();
		StringBuilder showContent=new StringBuilder("");
		if (content!=null && content.contains("[")) {
			Gson g=new Gson();
			List<El11to5CompoundContent> contents=g.fromJson(content, new TypeToken<List<El11to5CompoundContent>>(){}.getType());
			get115Numbers(contents,wanfa);
			if (contents!=null && contents.size()>0) {
				for (int i = 0; i < contents.size(); i++) {
					if(i==5){
						showContent.append("...");
						break;
					}
					showContent.append(wanfa).append(" | ");
					if ("前二直选".equals(wanfa) || "前三直选".equals(wanfa)|| "选三前直".equals(wanfa) ) {
						if (checkContents(contents.get(i).getBet1List())) {
							for (int j = 0; j < contents.get(i).getBet1List().size(); j++) {
								showContent.append(contents.get(i).getBet1List().get(j)+" ");
							}
						}
						if (checkContents(contents.get(i).getBet2List())) {
							showContent.append("| ");
							for (int j = 0; j < contents.get(i).getBet2List().size(); j++) {
								showContent.append(contents.get(i).getBet2List().get(j)+" ");
							}
						}
						if (checkContents(contents.get(i).getBet3List())) {
							showContent.append("| ");
							for (int j = 0; j < contents.get(i).getBet3List().size(); j++) {
								showContent.append(contents.get(i).getBet3List().get(j)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("任选一".equals(wanfa)){
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#FF3232'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(contents.get(i).getBetDanList().get(j)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(contents.get(i).getBetList().get(j)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("前二组选".equals(wanfa)){
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#FF3232'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(contents.get(i).getBetDanList().get(j)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(contents.get(i).getBetList().get(j)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else if("前三组选".equals(wanfa)){
						if (checkContents(contents.get(i).getBetDanList())) {

							showContent.append("<font color='#FF3232'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(contents.get(i).getBetDanList().get(j)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(contents.get(i).getBetList().get(j)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else{
						if (checkContents(contents.get(i).getBetDanList())) {
							showContent.append("<font color='#FF3232'>  (胆) </font>");
							for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
								showContent.append(contents.get(i).getBetDanList().get(j)+" ");
							}
							showContent.append("| ");
						}
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(contents.get(i).getBetList().get(j)+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}
				}
			}

		}else{
			String[] numberContent=content.split(" ");
			showContent.append(wanfa).append(" | ");
			if (numberContent!=null && numberContent.length>0) {
				for (int i = 0; i < numberContent.length; i++) {
					showContent.append(numberContent[i]+" ");
				}
			}
			Number115 number115 = new Number115();
			number115.setPlayType(ChooseUtil.getChooseUtil().getWANFA(wanfa));

		}
		return showContent.toString();

	}
	public String getStringNumberFast3(){
		String wanfa = orderdetail.getPlayType();
		String content= orderdetail.getFirstContent();

		StringBuilder showContent=new StringBuilder("");
		if (content!=null && content.contains("[")) {
			Gson g=new Gson();
			List<Fast3CompoundContent> contents=g.fromJson(content, new TypeToken<List<Fast3CompoundContent>>(){}.getType());
			getFast3Numbers(contents,wanfa);
			if (contents!=null && contents.size()>0) {
				for (int i = 0; i < contents.size(); i++) {
					if(i==5){
						showContent.append("...");
						break;
					}
					if (checkContents(contents.get(i).getBetDanList())) {
						showContent.append(wanfa);
						showContent.append("胆拖");
						showContent.append(" | ");
						showContent.append("<font color='#FF3232'>  (胆) </font>");
						for (int j = 0; j < contents.get(i).getBetDanList().size(); j++) {
							showContent.append(Util.periodNumberFromat(contents.get(i).getBetDanList().get(j))+" ");
						}
						showContent.append("|");
						if (checkContents(contents.get(i).getBetList())) {
							for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
								showContent.append(Util.periodNumberFromat(contents.get(i).getBetList().get(j))+" ");
							}
						}
						if (i!=contents.size()-1) {
							showContent.append("<br/> ");
						}
					}else{
						if ("二同号单选".equals(wanfa) ) {
							showContent.append(wanfa);
							showContent.append(" | ");
							if (checkContents(contents.get(i).getBetList())) {
								for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
									showContent.append(Util.periodNumberFromat(contents.get(i).getBetList().get(j))+" ");
								}
							}
							showContent.append("#");
							if (checkContents(contents.get(i).getDisList())) {
								for (int j = 0; j < contents.get(i).getDisList().size(); j++) {
									showContent.append(Util.periodNumberFromat(contents.get(i).getDisList().get(j))+" ");
								}
							}
							if (i!=contents.size()-1) {
								showContent.append("<br/> ");
							}
						}else if("三连号通选".equals(wanfa)){
							showContent.append(wanfa);
							if (i!=contents.size()-1) {
								showContent.append("<br/> ");
							}
						}else if("三同号通选".equals(wanfa)){
							showContent.append(wanfa);
							if (i!=contents.size()-1) {
								showContent.append("<br/> ");
							}
						}else {
							showContent.append(wanfa);
							showContent.append(" | ");
							if (checkContents(contents.get(i).getBetList())) {
								for (int j = 0; j < contents.get(i).getBetList().size(); j++) {
									showContent.append(Util.periodNumberFromat(contents.get(i).getBetList().get(j))+" ");
								}
							}
							if (i!=contents.size()-1) {
								showContent.append("<br/> ");
							}
						}
					}
				}
			}

		}
		return showContent.toString();

	}
	public void get115Numbers(List<El11to5CompoundContent> contents,String wanfa){
		for(El11to5CompoundContent content:contents){
			Number115 number115 = new Number115();
			number115.setNum(content.getUnits());
			number115.setMode(1);
			if ("前二直选".equals(wanfa) || "前三直选".equals(wanfa) || "选三前直".equals(wanfa)) {
				if (checkContents(content.getBet1List())) {
					for (int j = 0; j < content.getBet1List().size(); j++) {
						number115.getNumbers().add(
								Integer.parseInt(content.getBet1List().get(j)));	
						number115.getWan().add(Integer.parseInt(content.getBet1List().get(j)));
					}
				}
				if (checkContents(content.getBet2List())) {
					for (int j = 0; j < content.getBet2List().size(); j++) {
						number115.getNumbers().add(
								Integer.parseInt(content.getBet2List().get(j)));	
						number115.getQian().add(
								Integer.parseInt(content.getBet2List().get(j)));

					}
				}
				if (checkContents(content.getBet3List())) {
					for (int j = 0; j < content.getBet3List().size(); j++) {
						number115.getNumbers().add(
								Integer.parseInt(content.getBet3List().get(j)));
						number115.getBai().add(
								Integer.parseInt(content.getBet3List().get(j)));	

					}
				}
			}else{
				if (checkContents(content.getBetDanList())) {
					number115.setMode(0);
					for (int j = 0; j < content.getBetDanList().size(); j++) {
						number115.getDan().add(
								Integer.parseInt(content.getBetDanList().get(j)));
					}
				}
				if (checkContents(content.getBetList())) {
					for (int j = 0; j < content.getBetList().size(); j++) {
						number115.getNumbers().add(
								Integer.parseInt(content.getBetList().get(j)));
						number115.getTuo().add(
								Integer.parseInt(content.getBetList().get(j)));

					}
				}
			}
			number115.setPlayType(ChooseUtil.getChooseUtil().getWANFA(wanfa));
			number115s.add(number115);
		}
	}
	public void getFast3Numbers(List<Fast3CompoundContent> contents,String wanfa){
		for(Fast3CompoundContent content:contents){
			NumberFast numberfast = new NumberFast();
			numberfast.setNum(content.getUnits());
			if (checkContents(content.getBetDanList())) {
				numberfast.setMode(0);
				for (int j = 0; j < content.getBetDanList().size(); j++) {
					numberfast.getNumbers().add(
							Integer.parseInt(content.getBetDanList().get(j)));	
				}
				for (int j = 0; j < content.getBetList().size(); j++) {
					numberfast.getNumber1().add(
							Integer.parseInt(content.getBetList().get(j)));	
				}
			}else{
				numberfast.setMode(1);
				if ("二同号单选".equals(wanfa)) {
					if (checkContents(content.getBetList())) {
						for (int j = 0; j < content.getBetList().size(); j++) {
							numberfast.getNumbers().add(
									Integer.parseInt(content.getBetList().get(j))/11);	
						}
					}
					if (checkContents(content.getDisList())) {
						for (int j = 0; j < content.getDisList().size(); j++) {
							numberfast.getNumber1().add(
									Integer.parseInt(content.getDisList().get(j)));	

						}
					}
				}if ("三同号单选".equals(wanfa)) {
					if (checkContents(content.getBetList())) {
						for (int j = 0; j < content.getBetList().size(); j++) {
							numberfast.getNumbers().add(
									Integer.parseInt(content.getBetList().get(j))/111);	
						}
					}
				}else{
					if (checkContents(content.getBetList())) {
						for (int j = 0; j < content.getBetList().size(); j++) {
							numberfast.getNumbers().add(
									Integer.parseInt(content.getBetList().get(j)));
						}
					}
				}
			}
			numberfast.setPlayType(ChooseUtilFast3.getChooseUtil().getWANFA(wanfa));
			numberfastList.add(numberfast);
		}
	}
	public void setDate(){
		if(orderLists.size() <=0){
			findViewById(R.id.greyline2).setVisibility(View.GONE);
		}else{
			//zCollections.reverse(orderLists);
			findViewById(R.id.greyline2).setVisibility(View.VISIBLE);
			for(int i = 0;i<orderLists.size();i++){
				final OrderDetailEach order = orderLists.get(i);
				View convertView = View.inflate(_this, R.layout.orderdetail_item,null);
				TextView number = (TextView) convertView.findViewById(R.id.number);
				TextView cost = (TextView) convertView.findViewById(R.id.cost);
				TextView state = (TextView) convertView.findViewById(R.id.state);
				if(i == orderLists.size()-1){
					convertView.findViewById(R.id.line2).setVisibility(View.GONE);
				}else{
					convertView.findViewById(R.id.line2).setVisibility(View.VISIBLE);
				}
				number.setText("第"+order.getPeriodNumber()+"期");
				cost.setText(df.format(order.getSchemeCost())+"元");
				String prizeType = order.getWon();
				if(!TextUtils.isEmpty(prizeType)){
					if(prizeType.contains("元")){
						state.setTextColor(getResources()
								.getColor(R.color.mainred));
						state.setAlpha(1);
					}
					state.setText(order.getWon());
				}
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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
						if (lotteryId == ConstantsBase.SSQ) {
							intent = new Intent(_this, SSQOrderDetailActivity.class);
						}

						if (null != intent) {
							try {
								intent.putExtra("onlyClose", true);
								intent.putExtra("schemeId", Integer.parseInt(order.getId()));
							} catch (Exception e) {
								e.printStackTrace();
								intent.putExtra("schemeId", -1);
							}
							intent.putExtra("lotteryId", lotteryId + "");
							startActivity(intent);
						}
					}
				});
				listview.addView(convertView);
			}	
		}
	}
}
