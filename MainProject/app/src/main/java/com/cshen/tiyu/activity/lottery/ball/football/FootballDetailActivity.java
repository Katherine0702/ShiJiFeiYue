package com.cshen.tiyu.activity.lottery.ball.football;

import android.content.Intent;
import android.graphics.Color;
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
import com.cshen.tiyu.domain.ball.ScroeBiFen;
import com.cshen.tiyu.domain.ball.ScroeIn;
import com.cshen.tiyu.domain.ball.ScroePeilv;

public class FootballDetailActivity extends BaseActivity {
	FootballDetailActivity _this;
	View cancle,sure;
	TextView titlezhu,titleke,rangqiu;
	View danSPF,danRQSPF,danBIFEN,danJQS,danBQC;
	View viewSPF,viewRQSPF,viewBIFEN,viewJQS,viewBQC;
	View nodataSPF,nodataRQSPF,nodataBIFEN,nodataJQS,nodataBQC;
	TextView zhusheng0,zhuping0,kesheng0,zhusheng1,zhuping1,kesheng1;

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

	View in1,in2,in3,in4,in5,in6,in7,in8,in9;
	TextView inview1,inview2,inview3,inview4,inview5,inview6,inview7,inview8;
	TextView intext1,intext2,intext3,intext4,intext5,intext6,intext7,intext8;

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
		setContentView(R.layout.footballdetail);
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
		rangqiu = (TextView) findViewById(R.id.rangqiu);


		danSPF = findViewById(R.id.danviewspf);
		danRQSPF = findViewById(R.id.danviewrqspf);
		danBIFEN = findViewById(R.id.danviewbifen);
		danJQS = findViewById(R.id.danviewin);
		danBQC = findViewById(R.id.danviewbqc);

		viewSPF = findViewById(R.id.viewspf);
		viewRQSPF = findViewById(R.id.viewrqspf);
		viewBIFEN = findViewById(R.id.viewbifen);
		viewJQS = findViewById(R.id.viewin);
		viewBQC = findViewById(R.id.viewbqc);

		nodataSPF = findViewById(R.id.nodatespf);
		nodataRQSPF = findViewById(R.id.nodaterqspf);
		nodataBIFEN = findViewById(R.id.nodatebifen);
		nodataJQS = findViewById(R.id.nodatein);
		nodataBQC = findViewById(R.id.nodatebqc);

		zhusheng0 = (TextView) findViewById(R.id.zhusheng0);
		zhuping0 = (TextView)findViewById(R.id.zhuping0);
		kesheng0 = (TextView)findViewById(R.id.kesheng0);
		zhusheng1 = (TextView)findViewById(R.id.zhusheng1);
		zhuping1 = (TextView)findViewById(R.id.zhuping1);
		kesheng1 = (TextView)findViewById(R.id.kesheng1);

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

		in1 =findViewById(R.id.in1);
		intext1  = (TextView)findViewById(R.id.intext1);
		inview1  = (TextView)findViewById(R.id.inview1);

		in2 =findViewById(R.id.in2);
		intext2  = (TextView)findViewById(R.id.intext2);
		inview2  = (TextView)findViewById(R.id.inview2);

		in3 =findViewById(R.id.in3);
		intext3  = (TextView)findViewById(R.id.intext3);
		inview3  = (TextView)findViewById(R.id.inview3);

		in4 =findViewById(R.id.in4);
		intext4  = (TextView)findViewById(R.id.intext4);
		inview4  = (TextView)findViewById(R.id.inview4);

		in5 =findViewById(R.id.in5);
		intext5  = (TextView)findViewById(R.id.intext5);
		inview5  = (TextView)findViewById(R.id.inview5);

		in6 =findViewById(R.id.in6);
		intext6  = (TextView)findViewById(R.id.intext6);
		inview6  = (TextView)findViewById(R.id.inview6);

		in7 =findViewById(R.id.in7);
		intext7  = (TextView)findViewById(R.id.intext7);
		inview7  = (TextView)findViewById(R.id.inview7);

		in8 =findViewById(R.id.in8);
		intext8  = (TextView)findViewById(R.id.intext8);
		inview8  = (TextView)findViewById(R.id.inview8);


		bqc1 =findViewById(R.id.bqc1);
		bqctext1 = (TextView)findViewById(R.id.bqctext1);
		bqcview1 = (TextView)findViewById(R.id.bqcview1);

		bqc2 =findViewById(R.id.bqc2);
		bqctext2 = (TextView)findViewById(R.id.bqctext2);
		bqcview2 = (TextView)findViewById(R.id.bqcview2);

		bqc3 =findViewById(R.id.bqc3);
		bqctext3 = (TextView)findViewById(R.id.bqctext3);
		bqcview3 = (TextView)findViewById(R.id.bqcview3);

		bqc4 =findViewById(R.id.bqc4);
		bqctext4 = (TextView)findViewById(R.id.bqctext4);
		bqcview4 = (TextView)findViewById(R.id.bqcview4);

		bqc5 =findViewById(R.id.bqc5);
		bqctext5 = (TextView)findViewById(R.id.bqctext5);
		bqcview5 = (TextView)findViewById(R.id.bqcview5);

		bqc6 =findViewById(R.id.bqc6);
		bqctext6 = (TextView)findViewById(R.id.bqctext6);
		bqcview6 = (TextView)findViewById(R.id.bqcview6);

		bqc7 =findViewById(R.id.bqc7);
		bqctext7 = (TextView)findViewById(R.id.bqctext7);
		bqcview7 = (TextView)findViewById(R.id.bqcview7);

		bqc8 =findViewById(R.id.bqc8);
		bqctext8 = (TextView)findViewById(R.id.bqctext8);
		bqcview8 = (TextView)findViewById(R.id.bqcview8);

		bqc9 =findViewById(R.id.bqc9);
		bqctext9 = (TextView)findViewById(R.id.bqctext9);
		bqcview9 = (TextView)findViewById(R.id.bqcview9);
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
		rangqiu.setText(match.getHandicap());
		int handicap = Integer.parseInt(match.getHandicap());
		if(handicap<=0){
			rangqiu.setBackgroundColor(Color.parseColor("#529AF2"));
		}else{
			rangqiu.setBackgroundColor(Color.parseColor("#f2826a"));
		}
		FootBallGuansList guan = match.getMixOpen();
		FootBallScroeList sp = match.getSp();
		if(guan!=null){
			if(guan.isSingle_0()){
				danSPF.setVisibility(View.VISIBLE);
			}if(guan.isSingle_1()){
				danJQS.setVisibility(View.VISIBLE);
			}if(guan.isSingle_2()){
				danBIFEN.setVisibility(View.VISIBLE);
			}if(guan.isSingle_3()){
				danBQC.setVisibility(View.VISIBLE);
			}if(guan.isSingle_5()){
				danRQSPF.setVisibility(View.VISIBLE);
			}
			if(!guan.isPass_0()||sp == null||sp.getSPF() == null){
				viewSPF.setVisibility(View.INVISIBLE);
				nodataSPF.setVisibility(View.VISIBLE);
			}else{
				viewSPF.setVisibility(View.VISIBLE);
				nodataSPF.setVisibility(View.INVISIBLE);
				setSpfValue(sp.getSPF());
			}
			if(!guan.isPass_1()||sp == null||sp.getJQS() == null){
				viewJQS.setVisibility(View.INVISIBLE);
				nodataJQS.setVisibility(View.VISIBLE);
			}else{
				viewJQS.setVisibility(View.VISIBLE);
				nodataJQS.setVisibility(View.INVISIBLE);
				setInValue(sp.getJQS());
			}
			if(!guan.isPass_2()||sp == null||sp.getBF() == null){
				viewBIFEN.setVisibility(View.INVISIBLE);
				nodataBIFEN.setVisibility(View.VISIBLE);
			}else{
				viewBIFEN.setVisibility(View.VISIBLE);
				nodataBIFEN.setVisibility(View.INVISIBLE);
				setBifenValue(sp.getBF());
			}
			if(!guan.isPass_3()||sp == null||sp.getBQQ() == null){
				viewBQC.setVisibility(View.INVISIBLE);
				nodataBQC.setVisibility(View.VISIBLE);
			}else{
				viewBQC.setVisibility(View.VISIBLE);
				nodataBQC.setVisibility(View.INVISIBLE);
				setBqcValue(sp.getBQQ());
			}
			if(!guan.isPass_5()||sp == null||sp.getRQSPF() == null){
				viewRQSPF.setVisibility(View.INVISIBLE);
				nodataRQSPF.setVisibility(View.VISIBLE);
			}else{
				viewRQSPF.setVisibility(View.VISIBLE);
				nodataRQSPF.setVisibility(View.INVISIBLE);
				setRqSpfValue(sp.getRQSPF());
			}
		}
	}
	public void setSpfValue(ScroePeilv spf){
		String keyWin = spf.getWIN().getKey();
		if(!keyWin.endsWith("SPF")){
			spf.getWIN().setKey(keyWin+"SPF");
		}	
		JCZQUtil.getJCZQUtil().setString(zhusheng0,"主胜",spf.getWIN(),match.getCheckNumber());

		String keyDraw = spf.getDRAW().getKey();
		if(!keyDraw.endsWith("SPF")){
			spf.getDRAW().setKey(keyDraw+"SPF");
		}	
		JCZQUtil.getJCZQUtil().setString(zhuping0,"平",spf.getDRAW(),match.getCheckNumber());

		String keyLose = spf.getLOSE().getKey();
		if(!keyLose.endsWith("SPF")){
			spf.getLOSE().setKey(keyLose+"SPF");
		}
		JCZQUtil.getJCZQUtil().setString(kesheng0,"客胜",spf.getLOSE(),match.getCheckNumber());
	}
	public void setRqSpfValue(ScroePeilv rqspf){
		String keyWin= rqspf.getWIN().getKey();
		if(!keyWin.endsWith("RQSPF")){
			rqspf.getWIN().setKey(keyWin+"RQSPF");
		}
		JCZQUtil.getJCZQUtil().setString(zhusheng1,"主胜",rqspf.getWIN(),match.getCheckNumber());
		
		String keyDraw = rqspf.getDRAW().getKey();
		if(!keyDraw.endsWith("RQSPF")){
			rqspf.getDRAW().setKey(keyDraw+"RQSPF");
		}
		JCZQUtil.getJCZQUtil().setString(zhuping1,"平",rqspf.getDRAW(),match.getCheckNumber());
		
		String keyLose = rqspf.getLOSE().getKey();
		if(!keyLose.endsWith("RQSPF")){
			rqspf.getLOSE().setKey(keyLose+"RQSPF");
		}
		JCZQUtil.getJCZQUtil().setString(kesheng1,"客胜",rqspf.getLOSE(),match.getCheckNumber());
			
	}
	public void setInValue(ScroeIn in){
		intext1.setText(in.getS0().getValue());
		JCZQUtil.getJCZQUtil().setColor(in1,inview1,intext1,in.getS0(),match.getCheckNumber());
		intext2.setText(in.getS1().getValue());
		JCZQUtil.getJCZQUtil().setColor(in2,inview2,intext2,in.getS1(),match.getCheckNumber());
		intext3.setText(in.getS2().getValue());
		JCZQUtil.getJCZQUtil().setColor(in3,inview3,intext3,in.getS2(),match.getCheckNumber());
		intext4.setText(in.getS3().getValue());
		JCZQUtil.getJCZQUtil().setColor(in4,inview4,intext4,in.getS3(),match.getCheckNumber());
		intext5.setText(in.getS4().getValue());
		JCZQUtil.getJCZQUtil().setColor(in5,inview5,intext5,in.getS4(),match.getCheckNumber());
		intext6.setText(in.getS5().getValue());
		JCZQUtil.getJCZQUtil().setColor(in6,inview6,intext6,in.getS5(),match.getCheckNumber());
		intext7.setText(in.getS6().getValue());
		JCZQUtil.getJCZQUtil().setColor(in7,inview7,intext7,in.getS6(),match.getCheckNumber());
		intext8.setText(in.getS7().getValue());
		JCZQUtil.getJCZQUtil().setColor(in8,inview8,intext8,in.getS7(),match.getCheckNumber());
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
		switch(v.getId()){
		case R.id.zhusheng0:
			String keyWin = match.getSp().getSPF().getWIN().getKey();
			if(!keyWin.endsWith("SPF")){
				match.getSp().getSPF().getWIN().setKey(keyWin+"SPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(zhusheng0,"主胜",match.getSp().getSPF().getWIN(),match.getCheckNumber());
	
			setNumber(match.getSp().getSPF().getWIN());
			break;
		case R.id.zhuping0:
			String keyDraw = match.getSp().getSPF().getDRAW().getKey();
			if(!keyDraw.endsWith("SPF")){
				match.getSp().getSPF().getDRAW().setKey(keyDraw+"SPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(zhuping0,"平",match.getSp().getSPF().getDRAW(),match.getCheckNumber());	
	
			setNumber(match.getSp().getSPF().getDRAW());
			break;
		case R.id.kesheng0:
			String keyLose = match.getSp().getSPF().getLOSE().getKey();
			if(!keyLose.endsWith("SPF")){
				match.getSp().getSPF().getLOSE().setKey(keyLose+"SPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(kesheng0,"客胜",match.getSp().getSPF().getLOSE(),match.getCheckNumber());	
			setNumber(match.getSp().getSPF().getLOSE());
			break;
		case R.id.zhusheng1:
			String keyWinRQ = match.getSp().getRQSPF().getWIN().getKey();
			if(!keyWinRQ.endsWith("RQSPF")){
				match.getSp().getRQSPF().getWIN().setKey(keyWinRQ+"RQSPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(zhusheng1,"主胜",match.getSp().getRQSPF().getWIN(),match.getCheckNumber());
			setNumber(match.getSp().getRQSPF().getWIN());
			break;
		case R.id.zhuping1:
			String keyDrawRQ = match.getSp().getRQSPF().getDRAW().getKey();
			if(!keyDrawRQ.endsWith("RQSPF")){
				match.getSp().getRQSPF().getDRAW().setKey(keyDrawRQ+"RQSPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(zhuping1,"平",match.getSp().getRQSPF().getDRAW(),match.getCheckNumber());	
			setNumber(match.getSp().getRQSPF().getDRAW());
			break;
		case R.id.kesheng1:
			String keyLoseRQ = match.getSp().getRQSPF().getLOSE().getKey();
			if(!keyLoseRQ.endsWith("RQSPF")){
				match.getSp().getRQSPF().getLOSE().setKey(keyLoseRQ+"RQSPF");
			}
			JCZQUtil.getJCZQUtil().setStringClick(kesheng1,"客胜",match.getSp().getRQSPF().getLOSE(),match.getCheckNumber());
			setNumber(match.getSp().getRQSPF().getLOSE());
			break;
		
	
	
	
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
	
	
		case R.id.in1:
			JCZQUtil.getJCZQUtil().setColorClick(in1,inview1,
					intext1,match.getSp().getJQS().getS0(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS0());
			break;
		case R.id.in2:
			JCZQUtil.getJCZQUtil().setColorClick(in2,inview2,
					intext2,match.getSp().getJQS().getS1(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS1());
			break;
		case R.id.in3:
			JCZQUtil.getJCZQUtil().setColorClick(in3,inview3,
					intext3,match.getSp().getJQS().getS2(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS2());
			break;
		case R.id.in4:
			JCZQUtil.getJCZQUtil().setColorClick(in4,inview4,
					intext4,match.getSp().getJQS().getS3(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS3());
			break;
		case R.id.in5:
			JCZQUtil.getJCZQUtil().setColorClick(in5,inview5,
					intext5,match.getSp().getJQS().getS4(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS4());
			break;
		case R.id.in6:
			JCZQUtil.getJCZQUtil().setColorClick(in6,inview6,
					intext6,match.getSp().getJQS().getS5(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS5());
			break;
		case R.id.in7:
			JCZQUtil.getJCZQUtil().setColorClick(in7,inview7,
					intext7,match.getSp().getJQS().getS6(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS6());
			break;
		case R.id.in8:
			JCZQUtil.getJCZQUtil().setColorClick(in8,inview8,
					intext8,match.getSp().getJQS().getS7(),match.getCheckNumber());
			setNumber(match.getSp().getJQS().getS7());
			break;
	
		case R.id.bqc1:
			JCZQUtil.getJCZQUtil().setColorClick(bqc1,bqcview1,
					bqctext1,match.getSp().getBQQ().getWIN_WIN(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getWIN_WIN());
			break; 
		case R.id.bqc2:
			JCZQUtil.getJCZQUtil().setColorClick(bqc2,bqcview2,
					bqctext2,match.getSp().getBQQ().getWIN_DRAW(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getWIN_DRAW());
			break; 
		case R.id.bqc3:
			JCZQUtil.getJCZQUtil().setColorClick(bqc3,bqcview3,
					bqctext3,match.getSp().getBQQ().getWIN_LOSE(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getWIN_LOSE());
			break; 
		case R.id.bqc4:
			JCZQUtil.getJCZQUtil().setColorClick(bqc4,bqcview4,
					bqctext4,match.getSp().getBQQ().getDRAW_WIN(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getDRAW_WIN());
			break; 
		case R.id.bqc5:
			JCZQUtil.getJCZQUtil().setColorClick(bqc5,bqcview5,
					bqctext5,match.getSp().getBQQ().getDRAW_DRAW(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getDRAW_DRAW());
			break; 
		case R.id.bqc6:
			JCZQUtil.getJCZQUtil().setColorClick(bqc6,bqcview6,
					bqctext6,match.getSp().getBQQ().getDRAW_LOSE(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getDRAW_LOSE());
			break; 
		case R.id.bqc7:
			JCZQUtil.getJCZQUtil().setColorClick(bqc7,bqcview7,
					bqctext7,match.getSp().getBQQ().getLOSE_WIN(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getLOSE_WIN());
			break; 
		case R.id.bqc8:
			JCZQUtil.getJCZQUtil().setColorClick(bqc8,bqcview8,
					bqctext8,match.getSp().getBQQ().getLOSE_DRAW(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getLOSE_DRAW());
			break; 
		case R.id.bqc9:
			JCZQUtil.getJCZQUtil().setColorClick(bqc9,bqcview9,
					bqctext9,match.getSp().getBQQ().getLOSE_LOSE(),match.getCheckNumber());
			setNumber(match.getSp().getBQQ().getLOSE_LOSE());
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
