package com.cshen.tiyu.activity.lottery.ball.basketball;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ball.BasketBallMatch;
import com.cshen.tiyu.domain.ball.ScroeBean;
import com.cshen.tiyu.domain.ball.ScroeSFC;

public class BasketBallDetailActivity extends BaseActivity {
	BasketBallDetailActivity _this;
	View cancle,sure;
	TextView titlezhu,titleke;
	View view15,view610,view1115,view1620,view2125,view26;
	TextView text15title,text610title,text1115title,text1620title,text2125title,text26title;
	TextView text15,text610,text1115,text1620,text2125,text26;

	View zhuview15,zhuview610,zhuview1115,zhuview1620,zhuview2125,zhuview26;
	TextView zhutext15title,zhutext610title,zhutext1115title,
	zhutext1620title,zhutext2125title,zhutext26title;
	TextView zhutext15,zhutext610,zhutext1115,zhutext1620,zhutext2125,zhutext26;

	BasketBallMatch match;
	ScroeSFC sfc;
	int motherkey,childkey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogsfc);
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

		view15 = findViewById(R.id.view15);
		view610 = findViewById(R.id.view610);
		view1115 = findViewById(R.id.view1115);
		view1620 = findViewById(R.id.view1620);
		view2125 = findViewById(R.id.view2125);
		view26 = findViewById(R.id.view26);

		text15title = (TextView) findViewById(R.id.text15title);
		text610title = (TextView) findViewById(R.id.text610title);
		text1115title = (TextView) findViewById(R.id.text1115title);
		text1620title = (TextView) findViewById(R.id.text1620title);
		text2125title = (TextView) findViewById(R.id.text2125title);
		text26title = (TextView) findViewById(R.id.text26title);
		text15 = (TextView) findViewById(R.id.text15);
		text610 = (TextView) findViewById(R.id.text610);
		text1115 = (TextView) findViewById(R.id.text1115);
		text1620 = (TextView) findViewById(R.id.text1620);
		text2125 = (TextView) findViewById(R.id.text2125);
		text26 = (TextView) findViewById(R.id.text26);


		zhuview15 = findViewById(R.id.zhuview15);
		zhuview610 = findViewById(R.id.zhuview610);
		zhuview1115 = findViewById(R.id.zhuview1115);
		zhuview1620 = findViewById(R.id.zhuview1620);
		zhuview2125 = findViewById(R.id.zhuview2125);
		zhuview26 = findViewById(R.id.zhuview26);

		zhutext15title = (TextView) findViewById(R.id.zhutext15title);
		zhutext610title = (TextView) findViewById(R.id.zhutext610title);
		zhutext1115title = (TextView) findViewById(R.id.zhutext1115title);
		zhutext1620title = (TextView) findViewById(R.id.zhutext1620title);
		zhutext2125title = (TextView) findViewById(R.id.zhutext2125title);
		zhutext26title = (TextView) findViewById(R.id.zhutext26title);
		zhutext15 = (TextView) findViewById(R.id.zhutext15);
		zhutext610 = (TextView) findViewById(R.id.zhutext610);
		zhutext1115 = (TextView) findViewById(R.id.zhutext1115);
		zhutext1620 = (TextView) findViewById(R.id.zhutext1620);
		zhutext2125 = (TextView) findViewById(R.id.zhutext2125);
		zhutext26 = (TextView) findViewById(R.id.zhutext26);
	}
	private void initData() {
		Bundle b = getIntent().getExtras();
		match = (BasketBallMatch)b.getSerializable("match");
		motherkey = b.getInt("motherkay");
		childkey = b.getInt("childkey");
		if(match!=null){
			setValue();
		}
	}

	private void setValue() {
		titlezhu.setText(match.getHomeTeamName());
		titleke.setText(match.getGuestTeamName());
		sfc = match.getMixSp().getSFC();
		setSFCValue(sfc);
	}
	public void setSFCValue(ScroeSFC sfc){
		text15.setText(sfc.getGUEST1_5().getValue());											
		setString(view15,text15title,text15,
				sfc.getGUEST1_5(),match.getCheckNumber());

		text610.setText(sfc.getGUEST6_10().getValue());											
		setString(view610,text610title,text610,
				sfc.getGUEST6_10(),match.getCheckNumber());

		text1115.setText(sfc.getGUEST11_15().getValue());											
		setString(view1115,text1115title,text1115,
				sfc.getGUEST11_15(),match.getCheckNumber());

		text1620.setText(sfc.getGUEST16_20().getValue());											
		setString(view1620,text1620title,text1620,
				sfc.getGUEST16_20(),match.getCheckNumber());

		text2125.setText(sfc.getGUEST21_25().getValue());											
		setString(view2125,text2125title,text2125,
				sfc.getGUEST21_25(),match.getCheckNumber());
		text26.setText(sfc.getGUEST26().getValue());											
		setString(view26,text26title,text26,
				sfc.getGUEST26(),match.getCheckNumber());



		zhutext15.setText(sfc.getHOME1_5().getValue());											
		setString(zhuview15,zhutext15title,zhutext15,
				sfc.getHOME1_5(),match.getCheckNumber());

		zhutext610.setText(sfc.getHOME6_10().getValue());											
		setString(zhuview610,zhutext610title,zhutext610,
				sfc.getHOME6_10(),match.getCheckNumber());

		zhutext1115.setText(sfc.getHOME11_15().getValue());											
		setString(zhuview1115,zhutext1115title,zhutext1115,
				sfc.getHOME11_15(),match.getCheckNumber());

		zhutext1620.setText(sfc.getHOME16_20().getValue());											
		setString(zhuview1620,zhutext1620title,zhutext1620,
				sfc.getHOME16_20(),match.getCheckNumber());

		zhutext2125.setText(sfc.getHOME21_25().getValue());											
		setString(zhuview2125,zhutext2125title,zhutext2125,
				sfc.getHOME21_25(),match.getCheckNumber());

		zhutext26.setText(sfc.getHOME26().getValue());											
		setString(zhuview26,zhutext26title,zhutext26,
				sfc.getHOME26(),match.getCheckNumber());
	}
	public void  onClick(View view){
		switch(view.getId()){
		case R.id.view15:
			setStringClick(view15,text15title,text15,
					sfc.getGUEST1_5());
			break;
		case R.id.view610:
			setStringClick(view610,text610title,text610,
					sfc.getGUEST6_10());
			break;
		case R.id.view1115:
			setStringClick(view1115,text1115title,text1115,
					sfc.getGUEST11_15());
			break;
		case R.id.view1620:
			setStringClick(view1620,text1620title,text1620,
					sfc.getGUEST16_20());
			break;
		case R.id.view2125:
			setStringClick(view2125,text2125title,text2125,
					sfc.getGUEST21_25());
			break;
		case R.id.view26:
			setStringClick(view26,text26title,text26,
					sfc.getGUEST26());
			break;
		case R.id.zhuview15:
			setStringClick(zhuview15,zhutext15title,zhutext15,
					sfc.getHOME1_5());
			break;
		case R.id.zhuview610:
			setStringClick(zhuview610,zhutext610title,zhutext610,
					sfc.getHOME6_10());
			break;
		case R.id.zhuview1115:
			setStringClick(zhuview1115,zhutext1115title,zhutext1115,
					sfc.getHOME11_15());
			break;
		case R.id.zhuview1620:
			setStringClick(zhuview1620,zhutext1620title,zhutext1620,
					sfc.getHOME16_20());
			break;
		case R.id.zhuview2125:
			setStringClick(zhuview2125,zhutext2125title,zhutext2125,
					sfc.getHOME21_25());
			break;
		case R.id.zhuview26:
			setStringClick(zhuview26,zhutext26title,zhutext26,
					sfc.getHOME26());
			break;
		}
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
	public void setStringClick(View view,TextView tv0,TextView tv,
			ScroeBean key){
		boolean isCheck = true;
		for(int i=0;i<match.getCheckNumber().size();i++){
			if(match.getCheckNumber().get(i).getKey().equals(key.getKey())){
				match.getCheckNumber().remove(i);
				isCheck = false;
				break;
			}
		}
		setTextColor(view,tv0,tv,isCheck);
		if(isCheck){
			match.getCheckNumber().add(key);
		}	
	}
	public void setTextColor(View view,TextView tv0,TextView tv,boolean isCheck){
		if(isCheck){
			tv0.setTextColor(Color.parseColor("#ffffff"));
			tv.setTextColor(Color.parseColor("#ffffff"));
			view.setBackgroundColor(Color.parseColor("#FF3232"));
		}else{
			tv0.setTextColor(Color.parseColor("#333333"));
			tv.setTextColor(Color.parseColor("#888888"));
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}
	};

	public void back(){
		Intent intent = new Intent();  
		Bundle bundle = new Bundle();   
		bundle.putSerializable("match", match);
		bundle.putInt("motherkay", motherkey);
		bundle.putInt("childkey", childkey);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		_this.finish(); 
	}
}
