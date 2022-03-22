package com.cshen.tiyu.activity.lottery.ball.football;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ball.FootBallGuansList;
import com.cshen.tiyu.domain.ball.FootBallMatch;
import com.cshen.tiyu.domain.ball.FootBallScroeList;
import com.cshen.tiyu.domain.ball.ScroeBQC;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeIn;

public class FootballJQSDetailActivity extends BaseActivity {

	public static final int JQS = 2;//进球数
	public static final int BQC = 3;//半全场
	FootballJQSDetailActivity _this;
	View cancle,sure;
	TextView titlezhu,titleke,titlename;

	View bqc1,bqc2,bqc3,bqc4,bqc5,bqc6,bqc7,bqc8,bqc9;
	TextView bqcview1,bqcview2,bqcview3,bqcview4,bqcview5,bqcview6,bqcview7
	,bqcview8,bqcview9;
	TextView bqctext1,bqctext2,bqctext3,bqctext4,bqctext5,bqctext6,bqctext7
	,bqctext8,bqctext9;

	FootBallMatch match;
	int motherkay,childkey,wanfa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footballjqsdetail);
		_this = this;
		initView();
		initData();
	}
	private void initView(){
		cancle =  findViewById(R.id.cancle);
		sure =  findViewById(R.id.sure);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});

		titlezhu = (TextView) findViewById(R.id.titlezhu);
		titleke = (TextView) findViewById(R.id.titleke);
		titlename = (TextView) findViewById(R.id.titlename); 

		bqc1 =findViewById(R.id.ss);
		bqctext1 = (TextView)findViewById(R.id.sstext);
		bqcview1 = (TextView)findViewById(R.id.ssview);

		bqc2 =findViewById(R.id.sp);
		bqctext2 = (TextView)findViewById(R.id.sptext);
		bqcview2 = (TextView)findViewById(R.id.spview);

		bqc3 =findViewById(R.id.sf);
		bqctext3 = (TextView)findViewById(R.id.sftext);
		bqcview3 = (TextView)findViewById(R.id.sfview);

		bqc4 =findViewById(R.id.ps);
		bqctext4 = (TextView)findViewById(R.id.pstext);
		bqcview4 = (TextView)findViewById(R.id.psview);

		bqc5 =findViewById(R.id.pp);
		bqctext5 = (TextView)findViewById(R.id.pptext);
		bqcview5 = (TextView)findViewById(R.id.ppview);

		bqc6 =findViewById(R.id.pf);
		bqctext6 = (TextView)findViewById(R.id.pftext);
		bqcview6 = (TextView)findViewById(R.id.pfview);

		bqc7 =findViewById(R.id.fs);
		bqctext7 = (TextView)findViewById(R.id.fstext);
		bqcview7 = (TextView)findViewById(R.id.fsview);

		bqc8 =findViewById(R.id.fp);
		bqctext8 = (TextView)findViewById(R.id.fptext);
		bqcview8 = (TextView)findViewById(R.id.fpview);

		bqc9 =findViewById(R.id.ff);
		bqctext9 = (TextView)findViewById(R.id.fftext);
		bqcview9 = (TextView)findViewById(R.id.ffview);
	}
	private void initData() {
		Bundle b = getIntent().getExtras();
		match = (FootBallMatch)b.getSerializable("match");
		motherkay = b.getInt("motherkay");
		childkey = b.getInt("childkey");
		wanfa  = b.getInt("wanfa");
		if(wanfa == JQS){
			bqc5.setVisibility(View.GONE);
			titlename.setText("总进球");
			bqcview1.setText("0球");
			bqcview2.setText("1球");
			bqcview3.setText("2球");
			bqcview4.setText("3球");
			bqcview6.setText("4球");
			bqcview7.setText("5球");
			bqcview8.setText("6球");
			bqcview9.setText("7+球");
		}
		if(wanfa == BQC){
			bqc5.setVisibility(View.VISIBLE);
			titlename.setText("半全场");
			bqcview1.setText("胜胜");
			bqcview2.setText("胜平");
			bqcview3.setText("胜负");
			bqcview4.setText("平胜");
			bqcview5.setText("平平");
			bqcview6.setText("平负");
			bqcview7.setText("负胜");
			bqcview8.setText("负平");
			bqcview9.setText("负负");
		}
		if(match!=null){
			setValue();
		}
	}

	private void setValue() {
		titlezhu.setText(match.getHomeTeamName());
		titleke.setText(match.getGuestTeamName());
		FootBallGuansList guan = match.getMixOpen();
		FootBallScroeList sp = match.getSp();
		if(guan!=null){
			if(wanfa == JQS){
				setInValue(sp.getJQS());
			}
			if(wanfa == BQC){
				setBqcValue(sp.getBQQ());
			}

		}

	}
	public void setInValue(ScroeIn in){
		bqctext1.setText(in.getS0().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc1,bqcview1,bqctext1,in.getS0(),match.getCheckNumber());
		bqctext2.setText(in.getS1().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc2,bqcview2,bqctext2,in.getS1(),match.getCheckNumber());
		bqctext3.setText(in.getS2().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc3,bqcview3,bqctext3,in.getS2(),match.getCheckNumber());
		bqctext4.setText(in.getS3().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc4,bqcview4,bqctext4,in.getS3(),match.getCheckNumber());
		bqctext6.setText(in.getS4().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc6,bqcview6,bqctext6,in.getS4(),match.getCheckNumber());
		bqctext7.setText(in.getS5().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc7,bqcview7,bqctext7,in.getS5(),match.getCheckNumber());
		bqctext8.setText(in.getS6().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc8,bqcview8,bqctext8,in.getS6(),match.getCheckNumber());
		bqctext9.setText(in.getS7().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc9,bqcview9,bqctext9,in.getS7(),match.getCheckNumber());
	}
	public void setBqcValue(ScroeBQC bqc){
		bqctext1.setText(bqc.getWIN_WIN().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc1,bqcview1,bqctext1,bqc.getWIN_WIN(),match.getCheckNumber());
		bqctext2.setText(bqc.getWIN_DRAW().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc2,bqcview2,bqctext2,bqc.getWIN_DRAW(),match.getCheckNumber());
		bqctext3.setText(bqc.getWIN_LOSE().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc3,bqcview3,bqctext3,bqc.getWIN_LOSE(),match.getCheckNumber());
		bqctext4.setText(bqc.getDRAW_WIN().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc4,bqcview4,bqctext4,bqc.getDRAW_WIN(),match.getCheckNumber());
		bqctext5.setText(bqc.getDRAW_DRAW().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc5,bqcview5,bqctext5,bqc.getDRAW_DRAW(),match.getCheckNumber());
		bqctext6.setText(bqc.getDRAW_LOSE().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc6,bqcview6,bqctext6,bqc.getDRAW_LOSE(),match.getCheckNumber());
		bqctext7.setText(bqc.getLOSE_WIN().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc7,bqcview7,bqctext7,bqc.getLOSE_WIN(),match.getCheckNumber());
		bqctext8.setText(bqc.getLOSE_DRAW().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc8,bqcview8,bqctext8,bqc.getLOSE_DRAW(),match.getCheckNumber());
		bqctext9.setText(bqc.getLOSE_LOSE().getValue());
		JCZQUtil.getJCZQUtil().setColor(bqc9,bqcview9,bqctext9,bqc.getLOSE_LOSE(),match.getCheckNumber());
	}
	public void onClick(View v) {
		if(wanfa == JQS){
	
			switch(v.getId()){
			case R.id.ss:
				JCZQUtil.getJCZQUtil().setColorClick(bqc1,bqcview1,
						bqctext1,match.getSp().getJQS().getS0(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS0());
				break;
			case R.id.sp:
				JCZQUtil.getJCZQUtil().setColorClick(bqc2,bqcview2,
						bqctext2,match.getSp().getJQS().getS1(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS1());
				break;
			case R.id.sf:
				JCZQUtil.getJCZQUtil().setColorClick(bqc3,bqcview3,
						bqctext3,match.getSp().getJQS().getS2(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS2());
				break;
			case R.id.ps:
				JCZQUtil.getJCZQUtil().setColorClick(bqc4,bqcview4,
						bqctext4,match.getSp().getJQS().getS3(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS3());
				break;
			case R.id.pf:
				JCZQUtil.getJCZQUtil().setColorClick(bqc6,bqcview6,
						bqctext6,match.getSp().getJQS().getS4(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS4());
				break;
			case R.id.fs:
				JCZQUtil.getJCZQUtil().setColorClick(bqc7,bqcview7,
						bqctext7,match.getSp().getJQS().getS5(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS5());
				break;
			case R.id.fp:
				JCZQUtil.getJCZQUtil().setColorClick(bqc8,bqcview8,
						bqctext8,match.getSp().getJQS().getS6(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS6());
				break;
			case R.id.ff:
				JCZQUtil.getJCZQUtil().setColorClick(bqc9,bqcview9,
						bqctext9,match.getSp().getJQS().getS7(),match.getCheckNumber());
				setNumber(match.getSp().getJQS().getS7());
				break;
			}
		}
		if(wanfa == BQC){
			switch(v.getId()){
			case R.id.ss:
				JCZQUtil.getJCZQUtil().setColorClick(bqc1,bqcview1,
						bqctext1,match.getSp().getBQQ().getWIN_WIN(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getWIN_WIN());
				break; 
			case R.id.sp:
				JCZQUtil.getJCZQUtil().setColorClick(bqc2,bqcview2,
						bqctext2,match.getSp().getBQQ().getWIN_DRAW(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getWIN_DRAW());
				break; 
			case R.id.sf:
				JCZQUtil.getJCZQUtil().setColorClick(bqc3,bqcview3,
						bqctext3,match.getSp().getBQQ().getWIN_LOSE(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getWIN_LOSE());
				break; 
			case R.id.ps:
				JCZQUtil.getJCZQUtil().setColorClick(bqc4,bqcview4,
						bqctext4,match.getSp().getBQQ().getDRAW_WIN(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getDRAW_WIN());
				break; 
			case R.id.pp:
				JCZQUtil.getJCZQUtil().setColorClick(bqc5,bqcview5,
						bqctext5,match.getSp().getBQQ().getDRAW_DRAW(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getDRAW_DRAW());
				break; 
			case R.id.pf:
				JCZQUtil.getJCZQUtil().setColorClick(bqc6,bqcview6,
						bqctext6,match.getSp().getBQQ().getDRAW_LOSE(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getDRAW_LOSE());
				break; 
			case R.id.fs:
				JCZQUtil.getJCZQUtil().setColorClick(bqc7,bqcview7,
						bqctext7,match.getSp().getBQQ().getLOSE_WIN(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getLOSE_WIN());
				break; 
			case R.id.fp:
				JCZQUtil.getJCZQUtil().setColorClick(bqc8,bqcview8,
						bqctext8,match.getSp().getBQQ().getLOSE_DRAW(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getLOSE_DRAW());
				break; 
			case R.id.ff:
				JCZQUtil.getJCZQUtil().setColorClick(bqc9,bqcview9,
						bqctext9,match.getSp().getBQQ().getLOSE_LOSE(),match.getCheckNumber());
				setNumber(match.getSp().getBQQ().getLOSE_LOSE());
				break; 
	
			}	
		}		
	}
	public void setNumber(ScroeBean sb){
		boolean isHas = false;
		for(int i=0;i<match.getCheckNumber().size();i++){
			if(match.getCheckNumber().get(i).getKey().equals(sb.getKey())){
				match.getCheckNumber().remove(i);
				isHas = true;
				break;
			}
		}
		String key = sb.getKey();
		if(isHas){
			if(key.contains("SPF")||key.contains("RQSPF")){
				//不是胜平负也不是让球胜平负
			}else if(key.contains("S")&&!key.contains("LOSE")){
				match.getSp().setChooseJQS(match.getSp().getChooseJQS()-1);
			}else if(key.contains("_")&&!key.contains("OTHER")){
				match.getSp().setChooseBQQ(match.getSp().getChooseBQQ()-1);
			}else{
				match.getSp().setChooseBF(match.getSp().getChooseBF()-1);
			}
		}else{
			match.getCheckNumber().add(sb);
			if(key.contains("SPF")||key.contains("RQSPF")){
				//不是胜平负也不是让球胜平负
			}else if(key.contains("S")&&!key.contains("LOSE")){
				match.getSp().setChooseJQS(match.getSp().getChooseJQS()+1);
			}else if(key.contains("_")&&!key.contains("OTHER")){
				match.getSp().setChooseBQQ(match.getSp().getChooseBQQ()+1);
			}else{
				match.getSp().setChooseBF(match.getSp().getChooseBF()+1);
			}
		}		
	}
	public void back(){

		Intent intent = new Intent();  
		Bundle bundle = new Bundle();   
		bundle.putSerializable("match", match);
		bundle.putInt("motherkay", motherkay);
		bundle.putInt("childkey", childkey);
		bundle.putInt("wanfa", wanfa);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		_this.finish(); 
	}
}