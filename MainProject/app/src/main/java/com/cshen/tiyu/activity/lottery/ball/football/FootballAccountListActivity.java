package com.cshen.tiyu.activity.lottery.ball.football;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.ball.AccountListActivity;
import com.cshen.tiyu.activity.lottery.ball.basketball.BasketBallMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.JczqChoosedScroeBean;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroePeilv;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.ToastUtils;

public class FootballAccountListActivity extends AccountListActivity implements OnClickListener{
	public static final int SPF = 1;//胜负
	public static final int JQS = 2;//进球数
	public static final int BQC = 3;//半全场
	public static final int BF = 4;//比分
	public static final int HH = 5;//混合
	@Override
	public void onCreateBase() {
		mapChoosed = new HashMap<String, FootBallMatch>();
		myListAdapter = new MyListAdapter();
		initView();
		jiangjinyouhua.setOnClickListener(this);
		recommend.setOnClickListener(this);
		initdata(); // 获取数据
	}

	public void getChange(){//改变了比赛内容
		getSingle();//判断单关
		if(getChuan()){//更新串关模式
			setTextViewNum();//更新注数
			setTextViewMoney();//更新投注金额
			new setTextViewMoneyFanWei(true).execute();
		}else{
			realTime.setText("1");
			allTime.setText("1倍0注0元");
			String moneyStr = "<html><font color=\"#333333\">预测奖金:"
					+"</font><font color=\"#FF3232\">0"
					+"</font><font color=\"#333333\">元"
					+ "</font></html>";
			minMoney = 0f;
			allMoney.setText(Html.fromHtml(moneyStr));
		}
		setTextViewDan(chuan1.size()>0?chuan1.get(chuan1.size()-1):null);//更新胆数
		myListAdapter.notifyDataSetChanged();
	}
	@Override
	public void getSingle(){
		isSingle = JCZQUtil.getJCZQUtil().isSingle(matchs);
	}
	@Override
	public boolean getChuan(){
		int matchCount = matchs.size();
		for(Match match:matchs){
			if(((FootBallMatch)match).getSp().getChooseBF()>0||
					((FootBallMatch)match).getSp().getChooseBQQ()>0){
				if(matchCount>4){
					matchCount = 4;
				}
				break;
			}
			if(((FootBallMatch)match).getSp().getChooseJQS()>0){
				if(matchCount>6){
					matchCount = 6;
				}
			}
		}
		return getChuanBase(matchCount);
	}
	@Override
	public void setTextViewNum(){//重新计算注数	
		JCZQUtil.getJCZQUtil().setPassTypeEach(setTextViewNumBase());
		allNumInt = JCZQUtil.getJCZQUtil().getUnit(isSingle, matchs);
	}
	@Override
	public void setTextViewMoneyFanWeiMother(boolean needChange){
		if((isSingle&&matchs.size()>0)||matchs.size()>1){
			new setTextViewMoneyFanWei(needChange).execute();
		}
	}
	class setTextViewMoneyFanWei extends AsyncTask<String, Integer, List<FootBallMatch>> {
		boolean needChange;
		public setTextViewMoneyFanWei(boolean needChangeR){
			needChange = needChangeR ;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			JCZQUtil.getJCZQUtil().setHuancun();
			String numberStr2 = "<html><font color=\"#333333\">预计奖金:"
					+"</font><font color=\"#FF3232\">正在计算中....."
					+ "</font></html>";
			allMoney.setText(Html.fromHtml(numberStr2));
		}

		@Override
		protected List<FootBallMatch> doInBackground(String... arg0) {
			if(needChange){
				try{  
					minMoney = 0f;
					maxMoney = 0f;
					minMoney = JCZQUtil.getJCZQUtil().getMinMoney(matchs);
					maxMoney = JCZQUtil.getJCZQUtil().getMaxMoney(matchs);
				}catch(Exception e){
					e.printStackTrace();
					minMoney = 0f;
					maxMoney = 0f;
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<FootBallMatch> match) {
			super.onPostExecute(match);
			DecimalFormat    df   = new DecimalFormat("######0.00"); 
			if((minMoney+"").equals(maxMoney+"")){
				String numberStr2 = "<html><font color=\"#333333\">预测奖金:"
						+"</font><font color=\"#FF3232\">"+df.format(minMoney*timeInt)
						+"</font><font color=\"#333333\">元"
						+ "</font></html>";
				allMoney.setText(Html.fromHtml(numberStr2));
			}else{
				String jiangjin = df.format(minMoney*timeInt)+"~"+df.format(maxMoney*timeInt);
				String numberStr = "<html><font color=\"#333333\">预测奖金:"
						+"</font><font color=\"#FF3232\">"+jiangjin
						+"</font><font color=\"#333333\">元"
						+ "</font></html>";
				String numberStr2 = "<html><font color=\"#333333\">预测奖金:"
						+"</font><font color=\"#FF3232\">"+df.format(minMoney*timeInt)
						+"<br/>~"+df.format(maxMoney*timeInt)
						+"</font><font color=\"#333333\">元"
						+ "</font></html>";
				if(jiangjin.length()>18){
					allMoney.setText(Html.fromHtml(numberStr2));
				}else{
					allMoney.setText(Html.fromHtml(numberStr));
				}

			}
		}

	}
	@Override
	public void toJJYH(){
		if(ziyouPT.size() == 0&&!"单关".equals(chuan1.get(chuan1.size()-1).getText())){
			Intent intentLogin = new Intent(_this, JJYHMainActivity.class); 
			Bundle bundle = new Bundle();  
			bundle.putLong("timeInt", timeInt);
			bundle.putLong("allMoneyInt", allMoneyInt);
			bundle.putInt("wanfa", wanfa);
			bundle.putSerializable("chuanfa", chuan1.get(chuan1.size()-1));  
			bundle.putSerializable("matchs", matchs);
			intentLogin.putExtras(bundle);
			startActivity(intentLogin);
		}else if(ziyouPT.size() == 1&&!"单关".equals(ziyouPT.get(0).getText())){
			Intent intentLogin = new Intent(_this, JJYHMainActivity.class); 
			Bundle bundle = new Bundle();  
			bundle.putLong("timeInt", timeInt);
			bundle.putLong("allMoneyInt", allMoneyInt);
			bundle.putInt("wanfa", wanfa);
			bundle.putSerializable("chuanfa", ziyouPT.get(0));  
			bundle.putSerializable("matchs", matchs);
			intentLogin.putExtras(bundle);
			startActivity(intentLogin);
		}else if(ziyouPT.size()>1){
			ToastUtils.showShort(_this, "奖金优化暂不支持同时选择多个过关方式");
		}else {
			ToastUtils.showShort(_this, "奖金优化暂不支持该过关方式");
		}
	}
	@Override
	public void toAdd(){
		Intent intent = new Intent(_this, FootballMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("matchs", mapChoosed);
		bundle.putBoolean("add", true);
		bundle.putInt("wanfa", wanfa);
		intent.putExtras(bundle);
		startActivityForResult(intent, TOADD);			
	}

	class MyListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if(matchs == null){
				return 0;
			}else{
				return matchs.size();
			}
		}

		@Override
		public FootBallMatch getItem(int position) {
			if(matchs == null){
				return new FootBallMatch();
			}else{
				return (FootBallMatch)matchs.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			//setTextViewValue();
			if(matchs == null ||
					matchs.size() == 0){
				nodate.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
			}else{ 
				listview.setVisibility(View.VISIBLE);
				nodate.setVisibility(View.GONE);
			}
		}
		@Override
		public int getItemViewType(int Position) {
			// TODO Auto-generated method stub
			if(wanfa == SPF){
				return SPF;
			}if(wanfa == HH){
				return HH;
			}if(wanfa == BQC){
				return BQC;
			}if(wanfa == JQS){
				return JQS;
			}if(wanfa == BF){
				return BF;
			}else{
				return super.getItemViewType(Position);
			}
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 6;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final FootBallMatch match = (FootBallMatch) matchs.get(position);
			String key = keys.get(position);
			String[] keysStr = key.split("\\+");
			final int motherkey = Integer.parseInt(keysStr[0]);
			final int childkey = Integer.parseInt(keysStr[1]);
			final ViewHolder holder;
			int type = getItemViewType(position); 
			if (convertView == null) {  
				holder = new ViewHolder();  
				switch (type) {
				case HH:
					convertView = View.inflate(_this, R.layout.footballtz_item,null);
					holder.touzhu = (TextView) convertView.findViewById(R.id.touzhu);
					break;
				case SPF:
					convertView = View.inflate(_this, R.layout.footballtzspf_item,null);
					holder.rangqiu  = (TextView) convertView.findViewById(R.id.rangqiu);
					holder.danspf = convertView.findViewById(R.id.danviewspf);
					holder.danrqspf = convertView.findViewById(R.id.danviewrqspf);

					holder.nodate0 = (TextView) convertView.findViewById(R.id.nodate0);
					holder.havedate0 =  convertView.findViewById(R.id.havedate0);
					holder.zhusheng0 = (TextView) convertView.findViewById(R.id.zhusheng0);
					holder.ping0 = (TextView) convertView.findViewById(R.id.ping0);
					holder.kesheng0 = (TextView) convertView.findViewById(R.id.kesheng0);

					holder.nodate1 = (TextView) convertView.findViewById(R.id.nodate1);
					holder.havedate1 =  convertView.findViewById(R.id.havedate1);
					holder.zhusheng1 = (TextView) convertView.findViewById(R.id.zhusheng1);
					holder.ping1 = (TextView) convertView.findViewById(R.id.ping1);
					holder.kesheng1 = (TextView) convertView.findViewById(R.id.kesheng1);
					break;
				case JQS:
				case BQC:
				case BF:
					convertView = View.inflate(_this, R.layout.footballmain_accountitembf,null);
					holder.bf = (TextView) convertView.findViewById(R.id.bf);
					break;
				}
				holder.day = (TextView) convertView.findViewById(R.id.day);
				holder.delete = (ImageView) convertView.findViewById(R.id.delete);
				holder.dan = (TextView) convertView.findViewById(R.id.dan);
				holder.titlezhu = (TextView) convertView.findViewById(R.id.titlezhu);
				holder.titleke = (TextView) convertView.findViewById(R.id.titleke);
				convertView.setTag(holder);  
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			}  

			if(match!=null){
				String matchKeyStr = match.getMatchKey();
				holder.day.setText(DateUtils.SubToDayChinese4(matchKeyStr.substring(0, 8),matchKeyStr.substring(9, matchKeyStr.length())));				
				holder.titlezhu.setText(match.getHomeTeamName());
				holder.titleke.setText(match.getGuestTeamName());
				if(wanfa == HH){
					StringBuffer touzhuStr = new StringBuffer();
					for(ScroeBean str:match.getCheckNumber()){
						touzhuStr.append(JCZQUtil.chooseScroeName(str.getKey()));
					}
					holder.touzhu.setText(touzhuStr.toString()+">");
					holder.touzhu.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(canClick){
								canClick = false;

								isToDetil = true;
								Intent intent = new Intent(_this,FootballDetailActivity.class);
								Bundle bundle = new Bundle();   
								bundle.putSerializable("match", match);
								bundle.putInt("motherkay",motherkey);
								bundle.putInt("childkey",childkey);
								intent.putExtras(bundle);
								startActivityForResult(intent,TODETAIL);
							}
						}
					});
				}
				if(wanfa == SPF){
					holder.rangqiu.setText(match.getHandicap()+"");
					int handicap = Integer.parseInt(match.getHandicap());
					if(handicap<=0){
						holder.rangqiu.setBackgroundResource(R.drawable.rangqiuback);
					}else{
						holder.rangqiu.setBackgroundResource(R.drawable.rangqiubackred);
					}

					if(match.getMixOpen()!=null&&match.getMixOpen().isSingle_0()){
						//是否单关
						holder.danspf.setVisibility(View.VISIBLE);
					}else{
						holder.danspf.setVisibility(View.GONE);
					}
					if(match.getMixOpen()!=null&&match.getMixOpen().isSingle_5()){
						//是否单关
						holder.danrqspf.setVisibility(View.VISIBLE);
					}else{
						holder.danrqspf.setVisibility(View.GONE);
					}
					if(match.getMixOpen()!=null&&match.getMixOpen().isPass_0()){
						//是否过关
						if(match.getSp()!= null){
							final ScroePeilv spf = match.getSp().getSPF();
							if(spf != null){
								holder.nodate0.setVisibility(View.INVISIBLE);
								holder.havedate0.setVisibility(View.VISIBLE);
								if(spf.getWIN()!=null){		
									String keyWin = spf.getWIN().getKey();
									if(!keyWin.endsWith("SPF")){
										spf.getWIN().setKey(keyWin+"SPF");
									}							
									setString(holder.zhusheng0,"主胜",spf.getWIN(),match.getCheckNumber());
								}if(spf.getDRAW()!=null){
									String keyDraw = spf.getDRAW().getKey();
									if(!keyDraw.endsWith("SPF")){
										spf.getDRAW().setKey(keyDraw+"SPF");
									}							
									setString(holder.ping0,"平",spf.getDRAW(),match.getCheckNumber());
								}if(spf.getLOSE()!=null){	
									String keyLose = spf.getLOSE().getKey();
									if(!keyLose.endsWith("SPF")){
										spf.getLOSE().setKey(keyLose+"SPF");
									}							
									setString(holder.kesheng0,"客胜",spf.getLOSE(),match.getCheckNumber());
								}
								holder.zhusheng0.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = spf.getWIN().getKey();
										if(!key.endsWith("SPF")){
											spf.getWIN().setKey(key+"SPF");
										}
										setStringClick((TextView)v,"主胜",spf.getWIN(),match.getCheckNumber());
										setChoose(match,spf.getWIN(),motherkey,childkey);
									}
								});
								holder.ping0.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = spf.getDRAW().getKey();
										if(!key.endsWith("SPF")){
											spf.getDRAW().setKey(key+"SPF");
										}
										setStringClick((TextView)v,"平",spf.getDRAW(),match.getCheckNumber());
										setChoose(match,spf.getDRAW(),motherkey,childkey);
									}
								});
								holder.kesheng0.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = spf.getLOSE().getKey();
										if(!key.endsWith("SPF")){
											spf.getLOSE().setKey(key+"SPF");
										}
										setStringClick((TextView)v,"客胜",spf.getLOSE(),match.getCheckNumber());
										setChoose(match,spf.getLOSE(),motherkey,childkey);
									}
								});
							}else{
								holder.nodate0.setText("暂未开售");
								holder.nodate0.setVisibility(View.VISIBLE);
								holder.havedate0.setVisibility(View.INVISIBLE);
							}
						}else{
							holder.nodate0.setText("暂未开售");
							holder.nodate0.setVisibility(View.VISIBLE);
							holder.havedate0.setVisibility(View.INVISIBLE);
						}
					}else{
						holder.nodate0.setText("暂未开售");
						holder.nodate0.setVisibility(View.VISIBLE);
						holder.havedate0.setVisibility(View.INVISIBLE);
					}
					if(match.getMixOpen()!=null&&match.getMixOpen().isPass_5()){
						//是否过关
						if(match.getSp()!= null){					
							final ScroePeilv rqspf = match.getSp().getRQSPF();
							if(rqspf != null){
								holder.nodate1.setVisibility(View.INVISIBLE);
								holder.havedate1.setVisibility(View.VISIBLE);
								if(rqspf.getWIN()!=null){		
									String keyWin = rqspf.getWIN().getKey();
									if(!keyWin.endsWith("RQSPF")){
										rqspf.getWIN().setKey(keyWin+"RQSPF");
									}
									setString(holder.zhusheng1,"主胜",rqspf.getWIN(),match.getCheckNumber());						
								}if(rqspf.getDRAW()!=null){		
									String keyDraw = rqspf.getDRAW().getKey();
									if(!keyDraw.endsWith("RQSPF")){
										rqspf.getDRAW().setKey(keyDraw+"RQSPF");
									}							
									setString(holder.ping1,"平",rqspf.getDRAW(),match.getCheckNumber());
								}if(rqspf.getLOSE()!=null){	
									String keyLose = rqspf.getLOSE().getKey();
									if(!keyLose.endsWith("RQSPF")){
										rqspf.getLOSE().setKey(keyLose+"RQSPF");
									}							
									setString(holder.kesheng1,"客胜",rqspf.getLOSE(),match.getCheckNumber());
								}
								holder.zhusheng1.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = rqspf.getWIN().getKey();
										if(!key.endsWith("RQSPF")){
											rqspf.getWIN().setKey(key+"RQSPF");
										}
										setStringClick((TextView)v,"主胜",rqspf.getWIN(),match.getCheckNumber());
										setChoose(match,rqspf.getWIN(),motherkey,childkey);
									}
								});
								holder.ping1.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = rqspf.getDRAW().getKey();
										if(!key.endsWith("RQSPF")){
											rqspf.getDRAW().setKey(key+"RQSPF");
										}
										setStringClick((TextView)v,"平",rqspf.getDRAW(),match.getCheckNumber());
										setChoose(match,rqspf.getDRAW(),motherkey,childkey);
									}
								});
								holder.kesheng1.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String key = rqspf.getLOSE().getKey();
										if(!key.endsWith("RQSPF")){
											rqspf.getLOSE().setKey(key+"RQSPF");
										}
										setStringClick((TextView)v,"客胜",rqspf.getLOSE(),match.getCheckNumber());
										setChoose(match,rqspf.getLOSE(),motherkey,childkey);
									}
								});
							}else{
								holder.nodate1.setText("暂未开售");
								holder.nodate1.setVisibility(View.VISIBLE);
								holder.havedate1.setVisibility(View.INVISIBLE);
							}
						}else{
							holder.nodate1.setVisibility(View.VISIBLE);
							holder.nodate1.setText("暂未开售");
							holder.havedate1.setVisibility(View.INVISIBLE);
						}
					}else{
						holder.nodate1.setText("暂未开售");
						holder.nodate1.setVisibility(View.VISIBLE);
						holder.havedate1.setVisibility(View.INVISIBLE);
					}
				}
				if(wanfa == JQS||wanfa == BQC||wanfa == BF){
					String sfcStr = sfcStr = JCZQUtil.getJCZQUtil().isHaveBF(match.getCheckNumber());
					if(sfcStr!=null&&sfcStr.length()>1){
						sfcStr = sfcStr.substring(0,sfcStr.length()-1);
						holder.bf.setText(sfcStr);
					}
					holder.bf.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==4){
								ToastUtils.showShort(_this, "最多选择4场比赛");
								return;
							}
							if(canClick){
								canClick = false;
								isToDetil = true;
								Intent intent = null;
								if(wanfa == BF){
									intent = new Intent(_this,FootballBFDetailActivity.class);
								}
								if(wanfa == JQS||wanfa == BQC){
									intent = new Intent(_this,FootballJQSDetailActivity.class);
								}
								Bundle bundle = new Bundle();   
								bundle.putSerializable("match", match);
								bundle.putInt("motherkay", motherkey);
								bundle.putInt("childkey", childkey);
								bundle.putInt("wanfa", wanfa);
								intent.putExtras(bundle);
								startActivityForResult(intent,TODETAIL);
							}
						}
					});

				}
				if(match.isDan()){
					holder.dan.setTextColor(Color.parseColor("#ffffff"));
					holder.dan.setBackgroundResource(R.mipmap.danbackchoose);
				}else{
					if((danMaxCount == 0)||(danCount>=danMaxCount&&danMaxCount!=0)){
						holder.dan.setTextColor(Color.parseColor("#DCDCDCDC"));
					}else{
						holder.dan.setTextColor(Color.parseColor("#888888"));
					}
					holder.dan.setBackgroundResource(R.mipmap.danbackchooseno);
				}
				holder.delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(match.isDan()&&danCount>0){
							danCount --;
						}
						mapChoosed.remove(motherkey+"+"+childkey);
						keys.clear();
						matchs.clear();
						doDate();
					}
				});
				holder.dan.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(hunhePT!=null&&hunhePT.size()>0){
							ToastUtils.showShort(_this, "该玩法不能设胆");
							return;
						}
						if(match.isDan()){
							match.setDan(false);
							holder.dan.setTextColor(Color.parseColor("#888888"));
							holder.dan.setBackgroundResource(R.mipmap.danbackchooseno);
							danCount--;
							setTextViewNum();//更新注数
							setTextViewMoney();//更新投注金额
							new setTextViewMoneyFanWei(true).execute();
						}else{
							if(danCount>=danMaxCount){
								ToastUtils.showShort(_this, "已达到设胆上限");
							}else{
								match.setDan(true);
								holder.dan.setTextColor(Color.parseColor("#ffffff"));
								holder.dan.setBackgroundResource(R.mipmap.danbackchoose);
								danCount++;
								setTextViewNum();//更新注数
								setTextViewMoney();//更新投注金额
								new setTextViewMoneyFanWei(true).execute();
							}
						}
						notifyDataSetChanged();
					}
				});
			}
			convertView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
			return convertView;
		}
		class ViewHolder {
			public ImageView delete;
			public TextView dan;

			public TextView day;
			public TextView titlezhu,titleke;
			public TextView touzhu;

			public TextView rangqiu;
			public View danspf,danrqspf;
			public TextView nodate0,nodate1;
			public View havedate0,havedate1;
			public TextView zhusheng0,ping0,kesheng0;
			public TextView zhusheng1,ping1,kesheng1;
			public TextView bf;
		}		
		public void setChoose(FootBallMatch match,ScroeBean key,int montherkey,int  childkey){
			boolean ishas = false;
			for(int i=0;i<match.getCheckNumber().size();i++){
				if(match.getCheckNumber().get(i).getKey().equals(key.getKey())){
					match.getCheckNumber().remove(i);
					ishas = true;
					break;
				}
			}
			if(!ishas){			
				match.getCheckNumber().add(key);
			}
			int checkNumber = match.getCheckNumber().size();
			if(checkNumber>0&&!  
					mapChoosed.containsKey(montherkey+"+"+childkey)){//有check并且没存在已选单中
				mapChoosed.put(montherkey+"+"+childkey, match);
			}
			if(checkNumber<=0&&mapChoosed.containsKey(montherkey+"+"+childkey)){//没check并且存在已选单中
				mapChoosed.remove(montherkey+"+"+childkey);
			}
			myListAdapter.notifyDataSetChanged();
			doDate();
		}
	}
	@Override
	public void toHavePay(){
		Intent intent= new Intent(_this,FootballMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putBoolean("add", false);		
		intent.putExtras(bundle);
		startActivity(intent);
	}; 
	@Override
	public void toPayMoney(){
		if(wanfa == HH||wanfa == SPF){
			toPayMoneyBase("4",ConstantsBase.JCZQ);
		}else{
			if(wanfa == JQS){
				toPayMoneyBase(1+"",ConstantsBase.JCZQ);
			}
			if(wanfa == BQC){
				toPayMoneyBase(3+"",ConstantsBase.JCZQ);
			}
			if(wanfa == BF){
				toPayMoneyBase(2+"",ConstantsBase.JCZQ);
			}
		}
	}
	public ArrayList<JczqChoosedScroeBean> choosedScroeBean(){
		ArrayList<JczqChoosedScroeBean> choosedScroeBean = new ArrayList<JczqChoosedScroeBean>();
		if(wanfa == HH||wanfa == SPF){
			for(int i=0;i<matchs.size();i++){
				FootBallMatch m  = (FootBallMatch) matchs.get(i);
				for(int j=0;j<m.getCheckNumber().size();j++){
					JczqChoosedScroeBean jcs = new JczqChoosedScroeBean();
					jcs.setDan(m.isDan());
					jcs.setMatchKey(m.getMatchKey()); 
					jcs.setValue(JCZQUtil.getJCZQUtil().chooseScroeValue(m.getCheckNumber().get(j).getKey()));
					jcs.setPlayTypeItem(JCZQUtil.getJCZQUtil().getPlayTypeItem(m.getCheckNumber().get(j).getKey()));
					choosedScroeBean.add(jcs);
				}	
			}
		}else{
			for(int i=0;i<matchs.size();i++){
				FootBallMatch m  = (FootBallMatch) matchs.get(i);
				JczqChoosedScroeBean jcs = new JczqChoosedScroeBean();
				jcs.setDan(m.isDan());
				jcs.setMatchKey(m.getMatchKey());
				StringBuffer value = new StringBuffer();
				for(int j=0;j<m.getCheckNumber().size();j++){
					value.append(JCZQUtil.getJCZQUtil().chooseScroeValue(m.getCheckNumber().get(j).getKey())+"^");
				}
				String valueStr = value.toString();
				jcs.setValue(valueStr.substring(0,valueStr.length()-1));
				if(wanfa == JQS){
					jcs.setPlayTypeItem(1);
				}if(wanfa == BQC){
					jcs.setPlayTypeItem(3);
				}if(wanfa == BF){
					jcs.setPlayTypeItem(2);
				}
				choosedScroeBean.add(jcs);				
			}
		}
		return choosedScroeBean;
	}



	public void setTextColor(TextView view,String name,String detail,boolean isCheck){
		String Str = "";
		if(isCheck){
			Str = "<html><font color=\"#ffffff\">"+name
					+"</font><font color=\"#ffffff\">&#160;&#160;&#160;"+detail
					+ "</font></html>";
			view.setBackgroundColor(Color.parseColor("#FF3232"));
		}else{
			Str = "<html><font color=\"#333333\">"+name
					+"</font><font color=\"#888888\">&#160;&#160;&#160;"+detail
					+ "</font></html>";
			view.setBackgroundResource(R.drawable.dlt_tzback);
		}
		view.setText(Html.fromHtml(Str));
	};
	public void setString(TextView view,String name,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = false;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = true;
				break;
			}
		}
		setTextColor(view,name,key.getValue(),isCheck);
	}
	public void setStringClick(TextView view,String name,ScroeBean key,ArrayList<ScroeBean> has){
		boolean isCheck = true;
		for(ScroeBean sb:has){
			if(sb.getKey().equals(key.getKey())){
				isCheck = false;
				break;
			}
		}
		setTextColor(view,name,key.getValue(),isCheck);
	}
}
