package com.cshen.tiyu.activity.lottery.ball.basketball;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.ball.AccountListActivity;
import com.cshen.tiyu.activity.lottery.ball.football.JJYHMainActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ball.BasketBallMatch;
import com.cshen.tiyu.domain.ball.JczqChoosedScroeBean;
import com.cshen.tiyu.domain.ball.Match;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeDXF;
import com.cshen.tiyu.domain.ball.ScroeRFSF;
import com.cshen.tiyu.domain.ball.ScroeSF;
import com.cshen.tiyu.domain.ball.ScroeSFC;
import com.cshen.tiyu.utils.ToastUtils;

public class BasketBallAccountListActivity extends AccountListActivity {
	public static final int SF = 1;//胜负 0
	public static final int RFSF = 2;//让分胜负 让分值
	public static final int SFC = 3;//胜分差 0
	public static final int DXF = 4;//大小分  大的分
	public static final int HH = 5;//混合 
	@Override
	public void onCreateBase() {
		mapChoosed = new HashMap<String, BasketBallMatch>();
		tv_head.setTitle("竞彩篮球投注");
		myListAdapter = new MyListAdapter();
		initView();
		jiangjinyouhua.setOnClickListener(this);
		findViewById(R.id.faqituijianview).setVisibility(View.GONE);
		recommend.setVisibility(View.INVISIBLE);
		initdata(); // 获取数据
	}
	@Override
	public void getSingle(){
		isSingle = JCLQUtil.getJCLQUtil().isSingleBasket(matchs);
	}
	@Override
	public boolean getChuan(){
		int matchCount = matchs.size();		
		boolean hasSFC = false;
		for(Match match:matchs){
			if(!TextUtils.isEmpty(JCLQUtil.getJCLQUtil().isHaveSFC(match.getCheckNumber()))){
				hasSFC = true;
				break;
			}
		}
		if(wanfa == SFC||hasSFC){
			if(matchCount>4){
				matchCount = 4;
			}
		}
		return getChuanBase(matchCount);
	}
	@Override
	public void setTextViewNum(){//重新计算注数	
		JCLQUtil.getJCLQUtil().setPassTypeEach(setTextViewNumBase());
		allNumInt = JCLQUtil.getJCLQUtil().getUnit(isSingle, matchs);
	}
	@Override
	public void setTextViewMoneyFanWeiMother(boolean needChange){
		if((isSingle&&matchs.size()>0)||matchs.size()>1){
			new setTextViewMoneyFanWei(needChange).execute();
		}
	}
	class setTextViewMoneyFanWei extends AsyncTask<String, Integer, List<BasketBallMatch>> {
		boolean needChange;
		public setTextViewMoneyFanWei(boolean needChangeR){
			needChange = needChangeR ;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			JCLQUtil.getJCLQUtil().setHuancun();
			String numberStr2 = "<html><font color=\"#333333\">预测奖金:"
					+"</font><font color=\"#FF3232\">正在计算中....."
					+ "</font></html>";
			allMoney.setText(Html.fromHtml(numberStr2));
		}

		@Override
		protected List<BasketBallMatch> doInBackground(String... arg0) {
			if(needChange){
				try{  
					minMoney = 0f;
					maxMoney = 0f;
					minMoney = 2*JCLQUtil.getJCLQUtil().getMinMoney(matchs);
					maxMoney = 2*JCLQUtil.getJCLQUtil().getMaxMoney(matchs);
				}catch(Exception e){
					e.printStackTrace();
					ToastUtils.showShort(_this, e.getMessage());
					minMoney = 0f;
					maxMoney = 0f;
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<BasketBallMatch> match) {
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
			Intent intentLogin = new Intent(_this, JJYHJCLQMainActivity.class); 
			Bundle bundle = new Bundle();  
			bundle.putLong("timeInt", timeInt);
			bundle.putLong("allMoneyInt", allMoneyInt);
			bundle.putInt("wanfa", wanfa);
			bundle.putSerializable("chuanfa", chuan1.get(chuan1.size()-1));  
			bundle.putSerializable("matchs", matchs);
			intentLogin.putExtras(bundle);
			startActivity(intentLogin);
		}else if(ziyouPT.size() == 1&&!"单关".equals(ziyouPT.get(0).getText())){
			Intent intentLogin = new Intent(_this, JJYHJCLQMainActivity.class); 
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
	//计算总额和注数等	
	@Override
	public void toAdd(){
		Intent intent = new Intent(_this, BasketBallMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putSerializable("matchs", mapChoosed);
		bundle.putBoolean("add", true);
		bundle.putInt("wanfa", wanfa);
		intent.putExtras(bundle);
		startActivityForResult(intent, TOADD);
	}
	@Override
	public void toHavePay(){
		Intent intent= new Intent(_this, BasketBallMainActivity.class);
		Bundle bundle = new Bundle();   
		bundle.putBoolean("add", false);		
		intent.putExtras(bundle);
		startActivity(intent);
	};
	@Override
	public ArrayList<JczqChoosedScroeBean> choosedScroeBean(){
		ArrayList<JczqChoosedScroeBean> choosedScroeBean = new ArrayList<JczqChoosedScroeBean>();
		if(wanfa == HH){
			for(int i=0;i<matchs.size();i++){
				BasketBallMatch m  = (BasketBallMatch) matchs.get(i);
				for(int j=0;j<m.getCheckNumber().size();j++){
					JczqChoosedScroeBean jcs = new JczqChoosedScroeBean();
					jcs.setDan(m.isDan());
					jcs.setMatchKey(m.getMatchKey());
					jcs.setValue(JCLQUtil.getJCLQUtil().chooseScroeValue(m.getCheckNumber().get(j).getKey()));
					jcs.setPlayTypeItem(JCLQUtil.getJCLQUtil().getPlayTypeItem(m.getCheckNumber().get(j).getKey()));
					switch(JCLQUtil.getJCLQUtil().getPlayTypeItem(m.getCheckNumber().get(j).getKey())+1){
					case SF:
					case SFC:
						jcs.setReferenceValue("0");
						break;
					case RFSF:
						jcs.setReferenceValue(m.getHandicap());
						break;
					case DXF:
						jcs.setReferenceValue(m.getTotalScore()+"");
						break;
					}
					choosedScroeBean.add(jcs);
				}	
			}			
		}else{
			for(int i=0;i<matchs.size();i++){
				BasketBallMatch m  = (BasketBallMatch) matchs.get(i);
				JczqChoosedScroeBean jcs = new JczqChoosedScroeBean();
				jcs.setDan(m.isDan());
				jcs.setMatchKey(m.getMatchKey());
				StringBuffer value = new StringBuffer();
				for(int j=0;j<m.getCheckNumber().size();j++){
					value.append(JCLQUtil.getJCLQUtil().chooseScroeValue(m.getCheckNumber().get(j).getKey())+"^");
				}
				String valueStr = value.toString();
				jcs.setValue(valueStr.substring(0,valueStr.length()-1));
				jcs.setPlayTypeItem(wanfa-1);
				switch(wanfa){
				case SF:
				case SFC:
					jcs.setReferenceValue("0");
					break;
				case RFSF:
					jcs.setReferenceValue(m.getHandicap());
					break;
				case DXF:
					jcs.setReferenceValue(m.getTotalScore()+"");
					break;
				}
				choosedScroeBean.add(jcs);				
			}
		}
		return choosedScroeBean;
	}
	@Override
	public void toPayMoney(){
		if(wanfa == SF){
			toPayMoneyBase(0+"",ConstantsBase.JCLQ);
		}if(wanfa == RFSF){
			toPayMoneyBase(1+"",ConstantsBase.JCLQ);
		}if(wanfa == SFC){
			toPayMoneyBase(2+"",ConstantsBase.JCLQ);
		}if(wanfa == DXF){
			toPayMoneyBase(3+"",ConstantsBase.JCLQ);
		}if(wanfa == HH){
			toPayMoneyBase(4+"",ConstantsBase.JCLQ);//4混合
		}
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
		public BasketBallMatch getItem(int position) {
			if(matchs == null){
				return new BasketBallMatch();
			}else{
				return (BasketBallMatch) matchs.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
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
			if(wanfa == SF){
				return SF;
			}if(wanfa == RFSF){
				return RFSF;
			}if(wanfa == SFC){
				return SFC;
			}if(wanfa == DXF){
				return DXF;
			}if(wanfa == HH){
				return HH;
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
			final BasketBallMatch match = (BasketBallMatch) matchs.get(position);
			String key = keys.get(position);
			String[] keysStr = key.split("\\+");
			final int motherkey = Integer.parseInt(keysStr[0]);
			final int childkey = Integer.parseInt(keysStr[1]);
			final ViewHolder holder;
			int type = getItemViewType(position);
			if (convertView == null) {
				holder = new ViewHolder();
				switch (type) {
				case SF:
					convertView = View.inflate(_this, R.layout.basketballmain_accountitemsf,null);
					holder.nodatesf = (TextView) convertView.findViewById(R.id.nodate);
					holder.havedatesf =  convertView.findViewById(R.id.havedate);
					holder.zs =  convertView.findViewById(R.id.view2);
					holder.zsview = (TextView) convertView.findViewById(R.id.txtview2);
					holder.zstext = (TextView) convertView.findViewById(R.id.txt2);
					holder.ks = convertView.findViewById(R.id.view1);
					holder.ksview = (TextView) convertView.findViewById(R.id.txtview1);
					holder.kstext = (TextView) convertView.findViewById(R.id.txt1);
					holder.dansf = convertView.findViewById(R.id.danview);
					break;
				case RFSF:
					convertView = View.inflate(_this, R.layout.basketballmain_accountitemsf,null);
					holder.nodaterfsf = (TextView) convertView.findViewById(R.id.nodate);
					holder.havedaterfsf =  convertView.findViewById(R.id.havedate);
					holder.zsrf = convertView.findViewById(R.id.view2);
					holder.zsrfview = (TextView) convertView.findViewById(R.id.txtview2);
					holder.zsrftext = (TextView) convertView.findViewById(R.id.txt2);
					holder.ksrf = convertView.findViewById(R.id.view1);
					holder.ksrfview = (TextView) convertView.findViewById(R.id.txtview1);
					holder.ksrftext = (TextView) convertView.findViewById(R.id.txt1);
					holder.danrfsf = convertView.findViewById(R.id.danview);
					break;
				case DXF:
					convertView = View.inflate(_this, R.layout.basketballmain_accountitemsf,null);
					holder.nodatedxf = (TextView) convertView.findViewById(R.id.nodate);
					holder.havedatedxf =  convertView.findViewById(R.id.havedate);
					holder.dy = convertView.findViewById(R.id.view1);
					holder.dyview = (TextView) convertView.findViewById(R.id.txtview1);
					holder.dytext = (TextView) convertView.findViewById(R.id.txt1);
					holder.xy = convertView.findViewById(R.id.view2);
					holder.xyview = (TextView) convertView.findViewById(R.id.txtview2);
					holder.xytext = (TextView) convertView.findViewById(R.id.txt2);
					holder.dandxf = convertView.findViewById(R.id.danview);
					break;
				case SFC:
					convertView = View.inflate(_this, R.layout.basketballmain_accountitemsfc,null);
					holder.nodatesfc = (TextView) convertView.findViewById(R.id.nodatesfc);
					holder.sfc = (TextView) convertView.findViewById(R.id.sfc);
					holder.dansfc = convertView.findViewById(R.id.danview);
					break;
				case HH:
					convertView = View.inflate(_this, R.layout.basketballmain_accountitemhh,null);
					holder.dansf = convertView.findViewById(R.id.danviewsf);
					holder.danrfsf = convertView.findViewById(R.id.danviewrfsf);
					holder.dandxf = convertView.findViewById(R.id.danviewdxf);
					holder.dansfc = convertView.findViewById(R.id.danviewsfc);

					holder.nodatesf = (TextView) convertView.findViewById(R.id.nodatesf);
					holder.havedatesf =  convertView.findViewById(R.id.havedatesf);
					holder.zs =  convertView.findViewById(R.id.zs);
					holder.zsview = (TextView) convertView.findViewById(R.id.zstxtview);
					holder.zstext = (TextView) convertView.findViewById(R.id.zstxt);
					holder.ks = convertView.findViewById(R.id.ks);
					holder.ksview = (TextView) convertView.findViewById(R.id.kstxtview);
					holder.kstext = (TextView) convertView.findViewById(R.id.kstxt);
					holder.sfline = (TextView) convertView.findViewById(R.id.sfline);

					holder.nodaterfsf = (TextView) convertView.findViewById(R.id.nodaterfsf);
					holder.havedaterfsf =  convertView.findViewById(R.id.havedaterfsf);
					holder.zsrf = convertView.findViewById(R.id.zsrf);
					holder.zsrfview = (TextView) convertView.findViewById(R.id.zsrftxtview);
					holder.zsrftext = (TextView) convertView.findViewById(R.id.zsrftxt);
					holder.ksrf = convertView.findViewById(R.id.ksrf);
					holder.ksrfview = (TextView) convertView.findViewById(R.id.ksrftxtview);
					holder.ksrftext = (TextView) convertView.findViewById(R.id.ksrftxt);
					holder.rfsfline = (TextView) convertView.findViewById(R.id.rfsfline);

					holder.nodatedxf = (TextView) convertView.findViewById(R.id.nodatedxf);
					holder.havedatedxf  =  convertView.findViewById(R.id.havedatedxf );
					holder.dy = convertView.findViewById(R.id.dy);
					holder.dyview = (TextView) convertView.findViewById(R.id.dytxtview);
					holder.dytext = (TextView) convertView.findViewById(R.id.dytxt);
					holder.xy = convertView.findViewById(R.id.xy);
					holder.xyview = (TextView) convertView.findViewById(R.id.xytxtview);
					holder.xytext = (TextView) convertView.findViewById(R.id.xytxt);

					holder.nodatesfc = (TextView) convertView.findViewById(R.id.nodatesfc);
					holder.sfc = (TextView) convertView.findViewById(R.id.sfc);
					break;
				}
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.delete = convertView.findViewById(R.id.delete);
			holder.dan = (TextView) convertView.findViewById(R.id.dan);
			holder.titlezhu = (TextView) convertView.findViewById(R.id.titlezhu);
			holder.titleke = (TextView) convertView.findViewById(R.id.titleke);
			holder.titlezhu.setText(getItem(position).getHomeTeamName());
			holder.titleke.setText(getItem(position).getGuestTeamName());
			if(getItem(position).isDan()){
				holder.dan.setTextColor(Color.parseColor("#ffffff"));
				if(wanfa == HH){
					holder.dan.setBackgroundResource(R.mipmap.danbackchoosel);
				}else{
					holder.dan.setBackgroundResource(R.mipmap.danbackchoose);
				}
			}else{
				if((danMaxCount == 0)||(danCount>=danMaxCount&&danMaxCount!=0)){
					holder.dan.setTextColor(Color.parseColor("#DCDCDCDC"));
				}else{
					holder.dan.setTextColor(Color.parseColor("#888888"));
				}
				if(wanfa == HH){
					holder.dan.setBackgroundResource(R.mipmap.danbackchooselno);
				}else{
					holder.dan.setBackgroundResource(R.mipmap.danbackchooseno);
				}					
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
					//notifyDataSetChanged();
				}
			});
			if(wanfa == HH||wanfa == SF){
				if(wanfa == HH&&getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isSingle_0()){
					//是否单关
					holder.dansf.setVisibility(View.VISIBLE);
				}else{
					holder.dansf.setVisibility(View.GONE);
				}
				if(getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isPass_0()){
					//是否过关
					if(getItem(position).getMixSp()!= null){
						final ScroeSF sf = getItem(position).getMixSp().getSF();
						if(sf != null){
							holder.nodatesf.setVisibility(View.INVISIBLE);
							holder.havedatesf.setVisibility(View.VISIBLE);
							if(sf.getWIN()!=null){		
								holder.zstext.setText(sf.getWIN().getValue());
								setString(holder.zs,holder.zsview,holder.zstext,
										sf.getWIN(),getItem(position).getCheckNumber());
							}
							if(sf.getLOSE()!=null){		
								holder.kstext.setText(sf.getLOSE().getValue());											
								setString(holder.ks,holder.ksview,holder.kstext,
										sf.getLOSE(),getItem(position).getCheckNumber());
							}
							if(wanfa == HH){
								holder.sfline.setBackgroundColor(Color.parseColor("#DFDFDF"));
							}

							holder.zs.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClick(holder.zs,holder.zsview,holder.zstext,
											sf.getWIN(),match.getCheckNumber());
									setChoose(match,sf.getWIN(),motherkey,childkey);
								}
							});
							holder.ks.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClick(holder.ks,holder.ksview,holder.kstext
											,sf.getLOSE(),match.getCheckNumber());
									setChoose(match,sf.getLOSE(),motherkey,childkey);
								}
							});
						}else{
							if(wanfa == HH){
								holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
							}
							holder.nodatesf.setText("暂未开售");
							holder.nodatesf.setVisibility(View.VISIBLE);
							holder.havedatesf.setVisibility(View.INVISIBLE);
						}
					}else{
						if(wanfa == HH){
							holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
						}
						holder.nodatesf.setText("暂未开售");
						holder.nodatesf.setVisibility(View.VISIBLE);
						holder.havedatesf.setVisibility(View.INVISIBLE);
					}
				}else{
					if(wanfa == HH){
						holder.sfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
					}
					holder.nodatesf.setText("暂未开售");
					holder.nodatesf.setVisibility(View.VISIBLE);
					holder.havedatesf.setVisibility(View.INVISIBLE);
				}
			}
			if(wanfa == HH||wanfa == RFSF){
				if(wanfa == HH&&getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isSingle_1()){
					//是否单关
					holder.danrfsf.setVisibility(View.VISIBLE);
				}else{
					holder.danrfsf.setVisibility(View.GONE);
				}
				if(getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isPass_1()){
					//是否过关
					if(getItem(position).getMixSp()!= null){		
						final ScroeRFSF rfsf = getItem(position).getMixSp().getRFSF();
						if(rfsf != null){
							holder.nodaterfsf.setVisibility(View.INVISIBLE);
							holder.havedaterfsf.setVisibility(View.VISIBLE);

							if(rfsf.getSF_WIN()!=null){		
								holder.zsrftext.setText(rfsf.getSF_WIN().getValue());	
								setStringColor(holder.zsrf,holder.zsrfview,holder.zsrftext,
										rfsf.getSF_WIN(),getItem(position).getHandicap(),getItem(position).getCheckNumber());						
							}
							if(rfsf.getSF_LOSE()!=null){	

								holder.ksrftext.setText(rfsf.getSF_LOSE().getValue());	
								setString(holder.ksrf,holder.ksrfview,holder.ksrftext,
										rfsf.getSF_LOSE(),getItem(position).getCheckNumber());
							}
							if(wanfa == HH){
								holder.rfsfline.setBackgroundColor(Color.parseColor("#DFDFDF"));
							}
							holder.zsrf.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClickColor(holder.zsrf,holder.zsrfview,holder.zsrftext,
											rfsf.getSF_WIN(),match.getHandicap(),match.getCheckNumber());
									setChoose(match,rfsf.getSF_WIN(),motherkey,childkey);
								}
							});

							holder.ksrf.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClick(holder.ksrf,holder.ksrfview,holder.ksrftext,
											rfsf.getSF_LOSE(),match.getCheckNumber());
									setChoose(match,rfsf.getSF_LOSE(),motherkey,childkey);
								}
							});
						}else{
							if(wanfa == HH){
								holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
							}
							holder.nodaterfsf.setText("暂未开售");
							holder.nodaterfsf.setVisibility(View.VISIBLE);
							holder.havedaterfsf.setVisibility(View.INVISIBLE);
						}
					}else{
						if(wanfa == HH){
							holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
						}
						holder.nodaterfsf.setVisibility(View.VISIBLE);
						holder.nodaterfsf.setText("暂未开售");
						holder.havedaterfsf.setVisibility(View.INVISIBLE);
					}
				}else{
					if(wanfa == HH){
						holder.rfsfline.setBackgroundColor(Color.parseColor("#FFFFFF"));
					}
					holder.nodaterfsf.setText("暂未开售");
					holder.nodaterfsf.setVisibility(View.VISIBLE);
					holder.havedaterfsf.setVisibility(View.INVISIBLE);
				}
			}
			if(wanfa == HH||wanfa == SFC){
				if(wanfa == HH&&getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isSingle_2()){
					//是否单关
					holder.dansfc.setVisibility(View.VISIBLE);
				}else{
					holder.dansfc.setVisibility(View.GONE);
				}

				if(getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isPass_2()){
					//是否过关
					if(getItem(position).getMixSp()!= null){			
						final	ScroeSFC sfc = getItem(position).getMixSp().getSFC();
						if(sfc != null){
							holder.nodatesfc.setVisibility(View.INVISIBLE);
							holder.sfc.setVisibility(View.VISIBLE);
							String sfcStr = JCLQUtil.getJCLQUtil().isHaveSFC(getItem(position).getCheckNumber());
							if(sfcStr!=null&&sfcStr.length()>1){
								sfcStr = sfcStr.substring(0,sfcStr.length()-1);
								holder.sfc.setText(sfcStr);
							}else{
								if(wanfa == SFC){
									holder.sfc.setText("点击选择胜负差");
								}else{
									holder.sfc.setText("展开胜分差玩法");
								}
							}
							holder.sfc.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									isToDetil = true;
									Intent intent = new Intent(_this,BasketBallDetailActivity.class);
									Bundle bundle = new Bundle();   
									bundle.putSerializable("match", match);
									bundle.putInt("motherkay", motherkey);
									bundle.putInt("childkey", childkey);
									intent.putExtras(bundle);
									startActivityForResult(intent,TODETAIL);
								}
							});
						}else{
							holder.nodatesfc.setText("暂未开售");
							holder.nodatesfc.setVisibility(View.VISIBLE);
							holder.sfc.setVisibility(View.INVISIBLE);
						}
					}else{
						holder.nodatesfc.setVisibility(View.VISIBLE);
						holder.nodatesfc.setText("暂未开售");
						holder.sfc.setVisibility(View.INVISIBLE);
					}
				}else{
					holder.nodatesfc.setText("暂未开售");
					holder.nodatesfc.setVisibility(View.VISIBLE);
					holder.sfc.setVisibility(View.INVISIBLE);
				}


			}
			if(wanfa == HH||wanfa == DXF){
				if(wanfa == HH&&getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isSingle_3()){
					//是否单关
					holder.dandxf.setVisibility(View.VISIBLE);
				}else{
					holder.dandxf.setVisibility(View.GONE);
				}
				if(getItem(position).getMixOpen()!=null&&getItem(position).getMixOpen().isPass_3()){
					//是否过关
					if(getItem(position).getMixSp()!= null){						
						final ScroeDXF dxf = getItem(position).getMixSp().getDXF();
						if(dxf != null){

							holder.dyview.setText("大于"+getItem(position).getTotalScore());
							holder.xyview.setText("小于"+getItem(position).getTotalScore());
							holder.nodatedxf.setVisibility(View.INVISIBLE);
							holder.havedatedxf.setVisibility(View.VISIBLE);
							if(dxf.getLARGE()!=null){		
								holder.dytext.setText(dxf.getLARGE().getValue());	
								setString(holder.dy,holder.dyview,holder.dytext,
										dxf.getLARGE(),getItem(position).getCheckNumber());						
							}
							if(dxf.getLITTLE()!=null){	
								holder.xytext.setText(dxf.getLITTLE().getValue());
								setString(holder.xy,holder.xyview,holder.xytext,
										dxf.getLITTLE(),getItem(position).getCheckNumber());
							}

							holder.dy.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClick(holder.dy,holder.dyview,holder.dytext,
											dxf.getLARGE(),match.getCheckNumber());
									setChoose(match,dxf.getLARGE(),motherkey,childkey);
								}
							});

							holder.xy.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(match.getCheckNumber().size()==0&&mapChoosed!=null&&mapChoosed.size()==8){
										ToastUtils.showShort(_this, "最多选择8场比赛");
										return;
									}
									setStringClick(holder.xy,holder.xyview,holder.xytext,
											dxf.getLITTLE(),match.getCheckNumber());
									setChoose(match,dxf.getLITTLE(),motherkey,childkey);
								}
							});
						}else{
							holder.nodatedxf.setText("暂未开售");
							holder.nodatedxf.setVisibility(View.VISIBLE);
							holder.havedatedxf.setVisibility(View.INVISIBLE);
						}
					}else{
						holder.nodatedxf.setVisibility(View.VISIBLE);
						holder.nodatedxf.setText("暂未开售");
						holder.havedatedxf.setVisibility(View.INVISIBLE);
					}
				}else{
					holder.nodatedxf.setText("暂未开售");
					holder.nodatedxf.setVisibility(View.VISIBLE);
					holder.havedatedxf.setVisibility(View.INVISIBLE);
				}
			}
			convertView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
			return convertView;    
		}
		class ViewHolder {
			public View delete,dansf,danrfsf,dandxf,dansfc;
			public TextView titlezhu,titleke,dan;
			public TextView nodatesf,nodaterfsf,nodatedxf,nodatesfc;
			public View havedatesf,havedaterfsf,havedatedxf;

			public View zs,ks;
			public TextView zsview,zstext,ksview,kstext,sfline;
			public View zsrf,ksrf;
			public TextView zsrfview,zsrftext,ksrfview,ksrftext,rfsfline;
			public View dy,xy;
			public TextView dyview,dytext,xyview,xytext;

			public TextView sfc;
		}


		public void setString(View view,TextView tv0,TextView tv,
				ScroeBean key,ArrayList<ScroeBean> has){
			boolean isCheck = false;
			for(ScroeBean sb:has){
				if(sb.getKey().equals(key.getKey())){
					isCheck = true;
					break;
				}
			}
			setTextColor(view,tv0,tv,isCheck);
		}
		public void setStringColor(View view,TextView tv0,TextView tv,
				ScroeBean key,String hand,ArrayList<ScroeBean> has){
			boolean isCheck = false;
			for(ScroeBean sb:has){
				if(sb.getKey().equals(key.getKey())){
					isCheck = true;
					break;
				}
			}
			setTextColorText(view,tv0,tv,hand,isCheck);
		}
		public void setStringClick(View view,TextView tv0,TextView tv,
				ScroeBean key,ArrayList<ScroeBean> has){
			boolean isCheck = true;
			for(ScroeBean sb:has){
				if(sb.getKey().equals(key.getKey())){
					isCheck = false;
					break;
				}
			}
			setTextColor(view,tv0,tv,isCheck);
		}
		public void setStringClickColor(View view,TextView tv0,TextView tv,
				ScroeBean key,String hand,ArrayList<ScroeBean> has){
			boolean isCheck = true;
			for(ScroeBean sb:has){
				if(sb.getKey().equals(key.getKey())){
					isCheck = false;
					break;
				}
			}
			setTextColorText(view,tv0,tv,hand,isCheck);
		}
		public void setTextColor(View view,TextView tv0,TextView tv,boolean isCheck){
			if(isCheck){
				tv0.setTextColor(Color.parseColor("#ffffff"));
				tv.setTextColor(Color.parseColor("#ffffff"));
				if(wanfa!=HH){
					view.setBackgroundResource(R.drawable.cornerfullred);
				}else{
					view.setBackgroundColor(getResources().getColor(R.color.mainred));
				}
			}else{
				tv0.setTextColor(Color.parseColor("#333333"));
				tv.setTextColor(Color.parseColor("#888888"));
				if(wanfa!=HH){
					view.setBackgroundResource(R.drawable.dlt_tzback);
				}else{
					view.setBackgroundColor(Color.parseColor("#ffffff"));
				}
			}
		};
		public void setTextColorText(View view,TextView tv0,TextView tv,String hand,boolean isCheck){
			String Str = "";		
			float handicap = Float.parseFloat(hand);
			if(isCheck){
				if(handicap<=0){
					Str = "<html><font color=\"#ffffff\">主胜"
							+"</font><font color=\"#ffffff\">("+hand
							+ ")</font></html>";
				}else{
					Str = "<html><font color=\"#ffffff\">主胜"
							+"</font><font color=\"#ffffff\">(+"+hand
							+ ")</font></html>";
				}		
				tv0.setText(Html.fromHtml(Str));
				tv.setTextColor(Color.parseColor("#ffffff"));
				if(wanfa!=HH){
					view.setBackgroundResource(R.drawable.cornerfullred);
				}else{
					view.setBackgroundColor(getResources().getColor(R.color.mainred));
				}
			}else{
				if(handicap<=0){
					Str = "<html><font color=\"#333333\">主胜"
							+"</font><font color=\"#1f8b16\">("+hand
							+ ")</font></html>";
				}else{
					Str = "<html><font color=\"#333333\">主胜"
							+"</font><font color=\"#FF3232\">(+"+hand
							+ ")</font></html>";
				}		
				tv0.setText(Html.fromHtml(Str));
				tv.setTextColor(Color.parseColor("#888888"));
				if(wanfa!=HH){
					view.setBackgroundResource(R.drawable.dlt_tzback);
				}else{
					view.setBackgroundColor(Color.parseColor("#ffffff"));
				}
			}
		};
		public void setChoose(BasketBallMatch match,ScroeBean key,int montherkey,int  childkey){
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

			//myListAdapter.notifyDataSetChanged();
			doDate();
		}
	}









}


