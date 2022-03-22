package com.cshen.tiyu.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.lottery.dlt.ChooseUtil;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.cai115.Number115;
import com.cshen.tiyu.domain.dltssq.DLTNumber;
import com.cshen.tiyu.tool.FontDisplayUtil;

public class PlayingTaoCanView extends LinearLayout {
	ArrayList<SingBallView> singBallViews = new ArrayList<SingBallView>();
	int number = 0;
	String wanfa = "";
	int playType =-1;
	ArrayList<Integer> numbers = new ArrayList<>();
	Number115 number115;
	DLTNumber numberdlt;
	LinearLayout linearLayout;
	LinearLayout.LayoutParams param;
	ImageView update;
	// 玩法view
	public PlayingTaoCanView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	@SuppressLint("ResourceAsColor")
	public PlayingTaoCanView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根;
		LayoutInflater.from(context).inflate(R.layout.playing_taocan_view, this);
		linearLayout = (LinearLayout) findViewById(R.id.ll_add_ball);
		update = (ImageView) findViewById(R.id.iv_update_play);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				changeNumber(context);
			}
		});
		param = new LinearLayout.LayoutParams(
				FontDisplayUtil.dip2px(getContext(), 25),
				FontDisplayUtil.dip2px(getContext(), 25));
		param.gravity = Gravity.CENTER;
		param.setMargins(FontDisplayUtil.dip2px(getContext(),5), 0, 0, 0);// 5dp转像素
	}
	public void setView(int num,int playtype,String wanFa) {
		number = num;
		playType = playtype;
		wanfa = wanFa;
		if((ConstantsBase.SD115+"").equals(wanfa)||
				(ConstantsBase.GD115+"").equals(wanfa)){
			get115Number();
		}else if((ConstantsBase.DLT+"").equals(wanfa)){
			getNumber();
		}else if((ConstantsBase.SSQ+"").equals(wanfa)){
			getNumberSSQ();
		}
		singBallViews.clear();
		linearLayout.removeAllViews();
		for (int i = 0; i < number; i++) {
			SingBallView singBallView = new SingBallView(getContext());
			singBallView.setGravity(Gravity.CENTER);
			String textString = null;
			if (numbers.get(i) < 10) {
				textString = "0" + numbers.get(i);
			} else {
				textString = "" + numbers.get(i);
			}
			singBallView.setText(textString);
			singBallView.setTextColor(this.getResources().getColor(
					R.color.white));
			singBallView.setTextSize(12);
			singBallView.setBackgroundResource(R.drawable.chooseballred);
			
			if((ConstantsBase.DLT+"").equals(wanfa)&&(i == 5 || i == 6)){
				singBallView.setBackgroundResource(R.mipmap.ballback_blue);
			}else if((ConstantsBase.SSQ+"").equals(wanfa)&&(i == 6)){
				singBallView.setBackgroundResource(R.mipmap.ballback_blue);
			}
			singBallViews.add(singBallView);// 添加到集合
			linearLayout.addView(singBallView, param);
		}
	}
	public DLTNumber getChoosedNumberDLT() {
		return numberdlt;
	}
	public DLTNumber getChoosedNumberSSQ() {
		return numberdlt;
	}
	public Number115 getChoosedNumber115() {
		return number115;
	}
	@SuppressLint("ResourceAsColor")
	public void changeNumber(Context context) {
		if((ConstantsBase.SD115+"").equals(wanfa)||
				(ConstantsBase.GD115+"").equals(wanfa)){
			get115Number();
		}else if((ConstantsBase.DLT+"").equals(wanfa)){
			getNumber();
		}else if((ConstantsBase.SSQ+"").equals(wanfa)){
			getNumberSSQ();
		}
		for (int i = 0; i < singBallViews.size(); i++) {
			String textString = null;
			if (numbers.get(i) < 10) {
				textString = "0" + numbers.get(i);
			} else {
				textString = "" + numbers.get(i);
			}
			// 获取第i个view
			singBallViews.get(i).setTextAfterAnim(textString);
			singBallViews.get(i).setTextColor(
					this.getResources().getColor(R.color.white));
			singBallViews.get(i).setTime(i * 250 + 1);// 第一个球也有动画
			singBallViews.get(i).startRotateAnimation();
		}
	}
	public void getNumber() {
		numberdlt = ChooseUtil.getChooseUtil().getRandom();
		numbers.clear();
		while(numbers.size()<number){
			for(int aa:numberdlt.getQianqu().getNumbers()){
				numbers.add(aa);
			}
			for(int aa:numberdlt.getHouqu().getNumbers()){
				numbers.add(aa);
			}
		}	
	}
	public void getNumberSSQ() {
		numberdlt = ChooseUtil.getChooseUtil().getRandomSSQ();
		numbers.clear();
		while(numbers.size()<number){
			for(int aa:numberdlt.getQianqu().getNumbers()){
				numbers.add(aa);
			}
			for(int aa:numberdlt.getHouqu().getNumbers()){
				numbers.add(aa);
			}
		}	
	}
	public void get115Number(){
		number115 = ChooseUtil.getChooseUtil().getRandom115(ChooseUtil.chooseNUMLEAST(playType),playType);
		numbers.clear();
		while(numbers.size()<number){
			for(int aa:number115.getNumbers()){
				numbers.add(aa);
			}
		}	
	}
}
