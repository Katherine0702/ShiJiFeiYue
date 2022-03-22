package com.cshen.tiyu.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.tool.FontDisplayUtil;
import com.cshen.tiyu.utils.RandomNumberDLTUtils;

public class PlayingMethodView extends LinearLayout {
	ArrayList<SingBallView> singBallViews = new ArrayList<SingBallView>();
	int number[] = new int[7];// number集合会传递给主界面
	View zhuihao;
	ImageView update;
	TextView btn_cathectic;
	CathecticListener cathecticListener;// 主界面设置的监听器为了拿数据
	TextView tv_change_number = null;


	public CathecticListener getCathecticListener() {
		return cathecticListener;
	}

	public void setCathecticListener(CathecticListener cathecticListener) {
		this.cathecticListener = cathecticListener;
	}

	public void setNumber(int[] number) {
		this.number = number;
	}

	// 玩法view
	public PlayingMethodView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	@SuppressLint("ResourceAsColor")
	public PlayingMethodView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		LayoutInflater.from(context)
		.inflate(R.layout.playing_method_view, this);


		getNumber();

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_add_ball);

		update = (ImageView) findViewById(R.id.iv_update_play);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				changeNumber(context);
			}
		});
		btn_cathectic = (TextView) findViewById(R.id.btn_cathectic);// 点击投注事件后

		btn_cathectic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (cathecticListener != null) {

					// 点击出发传递值给调用者处理
					cathecticListener.clickCathectic(number);

				}
			}
		});
		zhuihao = findViewById(R.id.zhuihao);// 点击投注事件后

		zhuihao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				cathecticListener.clickCathectic();
			}
		});
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				FontDisplayUtil.dip2px(getContext(), 34),
				FontDisplayUtil.dip2px(getContext(), 34));
		param.gravity = Gravity.CENTER;
		param.setMargins(FontDisplayUtil.dip2px(getContext(),6), 0, 0, 0);// 5dp转像素
		for (int i = 0; i < 7; i++) {
			// 添加7个textview

			SingBallView singBallView = new SingBallView(getContext());
			singBallView.setGravity(Gravity.CENTER);
			// int random=Math.random()
			String textString = null;
			if (number[i] < 10) {
				textString = "0" + number[i];

			} else {
				textString = "" + number[i];

			}

			singBallView.setText(textString);
			// singBallView.setSingBallColor(R.color.white);

			singBallView.setTextSize(16);
			singBallView.setBackgroundResource(R.drawable.ball_linered_pinkfull);
			singBallView.setTextColor(Color.parseColor("#f04d46"));
			if (i == 5 || i == 6) {
				singBallView.setBackgroundResource(R.drawable.ball_linebule_lightbulefull);
				singBallView.setTextColor(Color.parseColor("#309bf3"));
			}
			singBallViews.add(singBallView);// 添加到集合
			linearLayout.addView(singBallView, param);

		}

	}

	public void getNumber() {
		number=  RandomNumberDLTUtils.getRandomNumberDLTInt();
	}

	@SuppressLint("ResourceAsColor")
	public void changeNumber(Context context) {
		getNumber();

		for (int i = 0; i < singBallViews.size(); i++) {

			String textString = null;
			if (number[i] < 10) {
				textString = "0" + number[i];

			} else {
				textString = "" + number[i];

			}
			// 获取第i个view
			singBallViews.get(i).setTextAfterAnim(textString);
			singBallViews.get(i).setTextColor(Color.parseColor("#f04d46"));
			if (i == 5 || i == 6) {
				singBallViews.get(i).setTextColor(Color.parseColor("#309bf3"));
			}
			singBallViews.get(i).setTime(i * 250 + 1);// 第一个球也有动画
			singBallViews.get(i).startRotateAnimation();

		}

	}

	/**
	 * 投注监听器监听器 冲这边传递投注号码给主界面
	 * 
	 * @author minking
	 */
	public static interface CathecticListener {

		/**
		 * 单击投注事件传递数组
		 * 
		 * @param position
		 * @param imageView
		 */
		public void clickCathectic(int[] a);
		public void clickCathectic();

	}

}
