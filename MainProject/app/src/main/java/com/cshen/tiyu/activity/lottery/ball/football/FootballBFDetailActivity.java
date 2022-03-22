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
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeBiFen;

public class FootballBFDetailActivity extends BaseActivity {
	FootballBFDetailActivity _this;
	View cancle,sure;
	TextView titlezhu,titleke;
	
	View sheng1,sheng2,sheng3,sheng4,sheng5,sheng6,sheng7,sheng8,
	sheng9,sheng10,sheng11,sheng12,sheng13;
	TextView shengview1,shengview2,shengview3,shengview4,shengview5,
	shengview6,shengview7,shengview8,shengview9,shengview10,shengview11,
	shengview12,shengview13;
	TextView shengtext1,shengtext2,shengtext3,shengtext4,shengtext5,
	shengtext6,shengtext7,shengtext8,shengtext9,shengtext10,shengtext11,
	shengtext12,shengtext13;

	View ping1,ping2,ping3,ping4,ping5;
	TextView pingview1,pingview2,pingview3,pingview4,pingview5;
	TextView pingtext1,pingtext2,pingtext3,pingtext4,pingtext5;

	View fu1,fu2,fu3,fu4,fu5,fu6,fu7,fu8,fu9,fu10,fu11,fu12,fu13;
	TextView fuview1,fuview2,fuview3,fuview4,fuview5,fuview6,fuview7,
	fuview8,fuview9,fuview10,fuview11,fuview12,fuview13;
	TextView futext1,futext2,futext3,futext4,futext5,futext6,futext7,
	futext8,futext9,futext10,futext11,futext12,futext13;
	

	FootBallMatch match;
	int motherkay,childkey,wanfa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footballbfdetail);
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
		
		ping1 = findViewById(R.id.ping1);
		pingtext1 = (TextView) findViewById(R.id.pingtext1);
		pingview1 = (TextView) findViewById(R.id.pingview1);
		sheng1 =findViewById(R.id.sheng1);
		shengtext1 = (TextView)findViewById(R.id.shengtext1);
		shengview1 = (TextView)findViewById(R.id.shengview1);
		fu1 =findViewById(R.id.fu1);
		futext1 = (TextView)findViewById(R.id.futext1);
		fuview1 = (TextView)findViewById(R.id.fuview1);

		ping2 =  findViewById(R.id.ping2);
		pingtext2 = (TextView) findViewById(R.id.pingtext2);
		pingview2 = (TextView) findViewById(R.id.pingview2);
		sheng2 =findViewById(R.id.sheng2);
		shengtext2 = (TextView)findViewById(R.id.shengtext2);
		shengview2 = (TextView)findViewById(R.id.shengview2);
		fu2 =findViewById(R.id.fu2);
		futext2 = (TextView)findViewById(R.id.futext2);
		fuview2 = (TextView)findViewById(R.id.fuview2);

		ping3 = findViewById(R.id.ping3);
		pingtext3 = (TextView) findViewById(R.id.pingtext3);
		pingview3 = (TextView) findViewById(R.id.pingview3);
		sheng3 =findViewById(R.id.sheng3);
		shengtext3 = (TextView)findViewById(R.id.shengtext3);
		shengview3 = (TextView)findViewById(R.id.shengview3);
		fu3 =findViewById(R.id.fu3);
		futext3 = (TextView)findViewById(R.id.futext3);
		fuview3 = (TextView)findViewById(R.id.fuview3);

		ping4 = findViewById(R.id.ping4);
		pingtext4 = (TextView) findViewById(R.id.pingtext4);
		pingview4 = (TextView) findViewById(R.id.pingview4);
		sheng4 =findViewById(R.id.sheng4);
		shengtext4 = (TextView)findViewById(R.id.shengtext4);
		shengview4 = (TextView)findViewById(R.id.shengview4);
		fu4 =findViewById(R.id.fu4);
		futext4 = (TextView)findViewById(R.id.futext4);
		fuview4 = (TextView)findViewById(R.id.fuview4);

		ping5 = findViewById(R.id.ping5);
		pingtext5 = (TextView) findViewById(R.id.pingtext5);
		pingview5 = (TextView) findViewById(R.id.pingview5);
		sheng5 =findViewById(R.id.sheng5);
		shengtext5 = (TextView)findViewById(R.id.shengtext5);
		shengview5 = (TextView)findViewById(R.id.shengview5);
		fu5 =findViewById(R.id.fu5);
		futext5 = (TextView)findViewById(R.id.futext5);
		fuview5 = (TextView)findViewById(R.id.fuview5);

		sheng6 =findViewById(R.id.sheng6);
		shengtext6 = (TextView)findViewById(R.id.shengtext6);
		shengview6 = (TextView)findViewById(R.id.shengview6);
		fu6 =findViewById(R.id.fu6);
		futext6 = (TextView)findViewById(R.id.futext6);
		fuview6 = (TextView)findViewById(R.id.fuview6);

		sheng7 =findViewById(R.id.sheng7);
		shengtext7 = (TextView)findViewById(R.id.shengtext7);
		shengview7 = (TextView)findViewById(R.id.shengview7);
		fu7 =findViewById(R.id.fu7);
		futext7 = (TextView)findViewById(R.id.futext7);
		fuview7 = (TextView)findViewById(R.id.fuview7);

		sheng8 =findViewById(R.id.sheng8);
		shengtext8 = (TextView)findViewById(R.id.shengtext8);
		shengview8 = (TextView)findViewById(R.id.shengview8);
		fu8 =findViewById(R.id.fu8);
		futext8 = (TextView)findViewById(R.id.futext8);
		fuview8 = (TextView)findViewById(R.id.fuview8);

		sheng9 =findViewById(R.id.sheng9);
		shengtext9 = (TextView)findViewById(R.id.shengtext9);
		shengview9 = (TextView)findViewById(R.id.shengview9);
		fu9 =findViewById(R.id.fu9);
		futext9 = (TextView)findViewById(R.id.futext9);
		fuview9 = (TextView)findViewById(R.id.fuview9);

		sheng10 =findViewById(R.id.sheng10);
		shengtext10 = (TextView)findViewById(R.id.shengtext10);
		shengview10 = (TextView)findViewById(R.id.shengview10);
		fu10 =findViewById(R.id.fu10);
		futext10 = (TextView)findViewById(R.id.futext10);
		fuview10 = (TextView)findViewById(R.id.fuview10);

		sheng11 =findViewById(R.id.sheng11);
		shengtext11 = (TextView)findViewById(R.id.shengtext11);
		shengview11 = (TextView)findViewById(R.id.shengview11);
		fu11 =findViewById(R.id.fu11);
		futext11 = (TextView)findViewById(R.id.futext11);
		fuview11 = (TextView)findViewById(R.id.fuview11);

		sheng12 =findViewById(R.id.sheng12);
		shengtext12 = (TextView)findViewById(R.id.shengtext12);
		shengview12 = (TextView)findViewById(R.id.shengview12);
		fu12 =findViewById(R.id.fu12);
		futext12 = (TextView)findViewById(R.id.futext12);
		fuview12 = (TextView)findViewById(R.id.fuview12);

		sheng13 =findViewById(R.id.sheng13);
		shengtext13 = (TextView)findViewById(R.id.shengtext13);
		shengview13 = (TextView)findViewById(R.id.shengview13);
		fu13 =findViewById(R.id.fu13);
		futext13 = (TextView)findViewById(R.id.futext13);
		fuview13 = (TextView)findViewById(R.id.fuview13);
	}
	private void initData() {
		Bundle b = getIntent().getExtras();
		match = (FootBallMatch)b.getSerializable("match");
		motherkay = b.getInt("motherkay");
		childkey = b.getInt("childkey");
		wanfa  = b.getInt("wanfa");
		if(match!=null){
			setValue();
		}
	}

	private void setValue() {
		titlezhu.setText(match.getHomeTeamName());
		titleke.setText(match.getGuestTeamName());

		FootBallGuansList guan = match.getMixOpen();
		FootBallScroeList sp = match.getSp();	
		setBifenValue(sp.getBF());
	}
	public void setBifenValue(ScroeBiFen bifen){
		shengtext1.setText(bifen.getWIN10().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng1,shengview1,shengtext1,bifen.getWIN10(),match.getCheckNumber());
		shengtext2.setText(bifen.getWIN20().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng2,shengview2,shengtext2,bifen.getWIN20(),match.getCheckNumber());
		shengtext3.setText(bifen.getWIN21().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng3,shengview3,shengtext3,bifen.getWIN21(),match.getCheckNumber());
		shengtext4.setText(bifen.getWIN30().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng4,shengview4,shengtext4,bifen.getWIN30(),match.getCheckNumber());
		shengtext5.setText(bifen.getWIN31().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng5,shengview5,shengtext5,bifen.getWIN31(),match.getCheckNumber());
		shengtext6.setText(bifen.getWIN32().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng6,shengview6,shengtext6,bifen.getWIN32(),match.getCheckNumber());
		shengtext7.setText(bifen.getWIN40().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng7,shengview7,shengtext7,bifen.getWIN40(),match.getCheckNumber());
		shengtext8.setText(bifen.getWIN41().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng8,shengview8,shengtext8,bifen.getWIN41(),match.getCheckNumber());
		shengtext9.setText(bifen.getWIN42().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng9,shengview9,shengtext9,bifen.getWIN42(),match.getCheckNumber());
		shengtext10.setText(bifen.getWIN50().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng10,shengview10,shengtext10,bifen.getWIN50(),match.getCheckNumber());
		shengtext11.setText(bifen.getWIN51().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng11,shengview11,shengtext11,bifen.getWIN51(),match.getCheckNumber());
		shengtext12.setText(bifen.getWIN52().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng12,shengview12,shengtext12,bifen.getWIN52(),match.getCheckNumber());
		shengtext13.setText(bifen.getWIN_OTHER().getValue());
		JCZQUtil.getJCZQUtil().setColor(sheng13,shengview13,shengtext13,bifen.getWIN_OTHER(),match.getCheckNumber());

		pingtext1.setText(bifen.getDRAW00().getValue());
		JCZQUtil.getJCZQUtil().setColor(ping1,pingview1,pingtext1,bifen.getDRAW00(),match.getCheckNumber());
		pingtext2.setText(bifen.getDRAW11().getValue());
		JCZQUtil.getJCZQUtil().setColor(ping2,pingview2,pingtext2,bifen.getDRAW11(),match.getCheckNumber());
		pingtext3.setText(bifen.getDRAW22().getValue());
		JCZQUtil.getJCZQUtil().setColor(ping3,pingview3,pingtext3,bifen.getDRAW22(),match.getCheckNumber());
		pingtext4.setText(bifen.getDRAW33().getValue());
		JCZQUtil.getJCZQUtil().setColor(ping4,pingview4,pingtext4,bifen.getDRAW33(),match.getCheckNumber());
		pingtext5.setText(bifen.getDRAW_OTHER().getValue());
		JCZQUtil.getJCZQUtil().setColor(ping5,pingview5,pingtext5,bifen.getDRAW_OTHER(),match.getCheckNumber());

		futext1.setText(bifen.getLOSE01().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu1,fuview1,futext1,bifen.getLOSE01(),match.getCheckNumber());
		futext2.setText(bifen.getLOSE02().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu2,fuview2,futext2,bifen.getLOSE02(),match.getCheckNumber());
		futext3.setText(bifen.getLOSE12().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu3,fuview3,futext3,bifen.getLOSE12(),match.getCheckNumber());
		futext4.setText(bifen.getLOSE03().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu4,fuview4,futext4,bifen.getLOSE03(),match.getCheckNumber());
		futext5.setText(bifen.getLOSE13().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu5,fuview5,futext5,bifen.getLOSE13(),match.getCheckNumber());
		futext6.setText(bifen.getLOSE23().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu6,fuview6,futext6,bifen.getLOSE23(),match.getCheckNumber());
		futext7.setText(bifen.getLOSE04().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu7,fuview7,futext7,bifen.getLOSE04(),match.getCheckNumber());
		futext8.setText(bifen.getLOSE14().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu8,fuview8,futext8,bifen.getLOSE14(),match.getCheckNumber());
		futext9.setText(bifen.getLOSE24().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu9,fuview9,futext9,bifen.getLOSE24(),match.getCheckNumber());
		futext10.setText(bifen.getLOSE05().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu10,fuview10,futext10,bifen.getLOSE05(),match.getCheckNumber());
		futext11.setText(bifen.getLOSE15().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu11,fuview11,futext11,bifen.getLOSE15(),match.getCheckNumber());
		futext12.setText(bifen.getLOSE25().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu12,fuview12,futext12,bifen.getLOSE25(),match.getCheckNumber());
		futext13.setText(bifen.getLOSE_OTHER().getValue());
		JCZQUtil.getJCZQUtil().setColor(fu13,fuview13,futext13,bifen.getLOSE_OTHER(),match.getCheckNumber());

	}
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sheng1:
			JCZQUtil.getJCZQUtil().setColorClick(sheng1,shengview1,
					shengtext1,match.getSp().getBF().getWIN10(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN10());
			break;
		case R.id.sheng2:
			JCZQUtil.getJCZQUtil().setColorClick(sheng2,shengview2,
					shengtext2,match.getSp().getBF().getWIN20(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN20());
			break;
		case R.id.sheng3:
			JCZQUtil.getJCZQUtil().setColorClick(sheng3,shengview3,
					shengtext3,match.getSp().getBF().getWIN21(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN21());
			break;
		case R.id.sheng4:
			JCZQUtil.getJCZQUtil().setColorClick(sheng4,shengview4,
					shengtext4,match.getSp().getBF().getWIN30(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN30());
			break;
		case R.id.sheng5:
			JCZQUtil.getJCZQUtil().setColorClick(sheng5,shengview5,
					shengtext5,match.getSp().getBF().getWIN31(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN31());
			break;
		case R.id.sheng6:
			JCZQUtil.getJCZQUtil().setColorClick(sheng6,shengview6,
					shengtext6,match.getSp().getBF().getWIN32(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN32());
			break;
		case R.id.sheng7:
			JCZQUtil.getJCZQUtil().setColorClick(sheng7,shengview7,
					shengtext7,match.getSp().getBF().getWIN40(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN40());
			break;
		case R.id.sheng8:
			JCZQUtil.getJCZQUtil().setColorClick(sheng8,shengview8,
					shengtext8,match.getSp().getBF().getWIN41(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN41());
			break;
		case R.id.sheng9:
			JCZQUtil.getJCZQUtil().setColorClick(sheng9,shengview9,
					shengtext9,match.getSp().getBF().getWIN42(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN42());
			break;
		case R.id.sheng10:
			JCZQUtil.getJCZQUtil().setColorClick(sheng10,shengview10,shengtext10,
					match.getSp().getBF().getWIN50(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN50());
			break;
		case R.id.sheng11:
			JCZQUtil.getJCZQUtil().setColorClick(sheng11,shengview11,
					shengtext11,match.getSp().getBF().getWIN51(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN51());
			break;
		case R.id.sheng12:
			JCZQUtil.getJCZQUtil().setColorClick(sheng12,shengview12,
					shengtext12,match.getSp().getBF().getWIN52(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN52());
			break;
		case R.id.sheng13:
			JCZQUtil.getJCZQUtil().setColorClick(sheng13,shengview13,
					shengtext13,match.getSp().getBF().getWIN_OTHER(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getWIN_OTHER());
			break;
	
		case R.id.ping1:
			JCZQUtil.getJCZQUtil().setColorClick(ping1,pingview1,
					pingtext1,match.getSp().getBF().getDRAW00(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getDRAW00());
			break;
		case R.id.ping2:
			JCZQUtil.getJCZQUtil().setColorClick(ping2,pingview2,
					pingtext2,match.getSp().getBF().getDRAW11(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getDRAW11());
			break;
		case R.id.ping3:
			JCZQUtil.getJCZQUtil().setColorClick(ping3,pingview3,pingtext3,match.getSp().getBF().getDRAW22(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getDRAW22());
			break;
		case R.id.ping4:
			JCZQUtil.getJCZQUtil().setColorClick(ping4,pingview4,pingtext4,match.getSp().getBF().getDRAW33(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getDRAW33());
			break;
		case R.id.ping5:
			JCZQUtil.getJCZQUtil().setColorClick(ping5,pingview5,pingtext5,match.getSp().getBF().getDRAW_OTHER(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getDRAW_OTHER());
			break;
		case R.id.fu1:
			JCZQUtil.getJCZQUtil().setColorClick(fu1,fuview1,futext1,match.getSp().getBF().getLOSE01(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE01());
			break;
		case R.id.fu2:
			JCZQUtil.getJCZQUtil().setColorClick(fu2,fuview2,futext2,match.getSp().getBF().getLOSE02(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE02());
			break;
		case R.id.fu3:
			JCZQUtil.getJCZQUtil().setColorClick(fu3,fuview3,futext3,match.getSp().getBF().getLOSE12(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE12());
			break;
		case R.id.fu4:
			JCZQUtil.getJCZQUtil().setColorClick(fu4,fuview4,futext4,match.getSp().getBF().getLOSE03(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE03());
			break;
		case R.id.fu5:
			JCZQUtil.getJCZQUtil().setColorClick(fu5,fuview5,futext5,match.getSp().getBF().getLOSE13(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE13());
			break;
		case R.id.fu6:
			JCZQUtil.getJCZQUtil().setColorClick(fu6,fuview6,futext6,match.getSp().getBF().getLOSE23(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE23());
			break;
		case R.id.fu7:
			JCZQUtil.getJCZQUtil().setColorClick(fu7,fuview7,futext7,match.getSp().getBF().getLOSE04(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE04());
			break;
		case R.id.fu8:
			JCZQUtil.getJCZQUtil().setColorClick(fu8,fuview8,futext8,match.getSp().getBF().getLOSE14(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE14());
			break;
		case R.id.fu9:
			JCZQUtil.getJCZQUtil().setColorClick(fu9,fuview9,futext9,match.getSp().getBF().getLOSE24(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE24());
			break;
		case R.id.fu10:
			JCZQUtil.getJCZQUtil().setColorClick(fu10,fuview10,futext10,match.getSp().getBF().getLOSE05(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE05());
			break;
		case R.id.fu11:
			JCZQUtil.getJCZQUtil().setColorClick(fu11,fuview11,
					futext11,match.getSp().getBF().getLOSE15(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE15());
			break;
		case R.id.fu12:
			JCZQUtil.getJCZQUtil().setColorClick(fu12,fuview12,
					futext12,match.getSp().getBF().getLOSE25(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE25());
			break;
		case R.id.fu13:
			JCZQUtil.getJCZQUtil().setColorClick(fu13,fuview13,
					futext13,match.getSp().getBF().getLOSE_OTHER(),match.getCheckNumber());
			setNumber(match.getSp().getBF().getLOSE_OTHER());
			break;
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
