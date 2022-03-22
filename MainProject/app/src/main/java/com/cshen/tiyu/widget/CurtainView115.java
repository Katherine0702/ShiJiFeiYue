package com.cshen.tiyu.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.Prize;
import com.cshen.tiyu.tool.FontDisplayUtil;

public class CurtainView115 extends LinearLayout implements OnTouchListener{
	private static String TAG = "CurtainView";
	private Context mContext;
	/** Scroller 拖动类 */
	private Scroller mScroller;
	/** 点击时候Y的坐标*/
	private int downY = 0;
	/** 拖动时候Y的坐标*/
	private int moveY = 0;
	/** 拖动时候Y的方向距离*/
	private int scrollY = 0;
	/** 松开时候Y的坐标*/
	private int upY = 0;
	/** 广告幕布的高度*/
	private int curtainHeigh = 0;
	private int allInt = 0,linearlayout1Int = 0,linearlayout2Int = 0;
	/** 是否 打开*/
	private boolean isOpen = false;
	/** 是否在动画 */
	private boolean isMove = false;
	/** 类似绳子的图片*/
	private View img_curtain_rope;
	/** 类似卷帘的图片*/
	private View qiShu_view;
	private ScrollView scrollView;
	private View linearlayout1,linearlayout2;
	private TextView qishunow,qishunext;
	public TextView timeend;
	private TextView realtimeend;
	private TextView wanwei;
	private TextView qianwei;
	private TextView baiwei;
	private TextView line1,line2,line3;
	ArrayList<SingBallView> singBallViews = new ArrayList<SingBallView>();
	private TextView downTx;//下拉文字
	private ImageView downIm;//下拉图标
	private boolean Zhankai = false;
	/** 上升动画时间 */
	private int upDuration = 250;
	/** 下落动画时间 */
	private int downDuration = 250;
	View view,viewZhanwei;
	int wanfa;
	boolean firstTime = true;
	MyCountTime2 mMyCountTime,mMyCountTimeReal;
	private int[] qishus = new int[]{
			R.id.qishu0,R.id.qishu1,R.id.qishu2,R.id.qishu3,R.id.qishu4,
			R.id.qishu5,R.id.qishu6,R.id.qishu7,R.id.qishu8,R.id.qishu9};

	private int[] qishutexts = new int[]{
			R.id.num01,R.id.num02,R.id.num03,R.id.num04,R.id.num05,
			R.id.num06,R.id.num07,R.id.num08,R.id.num09,R.id.num10,R.id.num11};
	boolean show = false;
	WeiClickItemListener weiClickItemListener;
	public WeiClickItemListener getTopClickItemListener() {
		return weiClickItemListener;
	}
	public void setWeiClickItemListener(WeiClickItemListener weiClickItemListener) {
		this.weiClickItemListener = weiClickItemListener;
	}

	public CurtainView115(Context context) {
		super(context);
		init(context);
	}

	public CurtainView115(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CurtainView115(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/** 初始化 */
	private void init(Context context) {
		this.mContext = context;
		mScroller = new Scroller(context);
		// 背景设置成透明
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view = LayoutInflater.from(mContext).inflate(R.layout.curtain115, null);
		scrollView = (ScrollView) view.findViewById(R.id.dlt_qishu_scroll);
		qishunext = (TextView) view.findViewById(R.id.qishunext);
		qishunow  = (TextView) view.findViewById(R.id.qishu);
		timeend = (TextView) view.findViewById(R.id.timeend);
		realtimeend  = (TextView) view.findViewById(R.id.realtimeend);
		linearlayout1 = view.findViewById(R.id.linearLayout1);
		linearlayout2 = view.findViewById(R.id.linearLayout2);
		wanwei = (TextView) view.findViewById(R.id.tv_tab_wan);
		qianwei = (TextView) view.findViewById(R.id.tv_tab_qian);
		baiwei = (TextView) view.findViewById(R.id.tv_tab_bai);
		wanwei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				line1.setVisibility(View.VISIBLE);
				line2.setVisibility(View.INVISIBLE);
				if(wanfa == ConstantsBase.ZHIXUAN2){
					line3.setVisibility(View.GONE);
				}else{
					line3.setVisibility(View.INVISIBLE);
				}
				wanwei.setTextColor(Color.parseColor("#FF3232"));
				qianwei.setTextColor(Color.parseColor("#666666"));
				baiwei.setTextColor(Color.parseColor("#666666"));
				weiClickItemListener.clickWan(v);
			}
		});
		qianwei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				line1.setVisibility(View.INVISIBLE);
				line2.setVisibility(View.VISIBLE);
				if(wanfa == ConstantsBase.ZHIXUAN2){
					line3.setVisibility(View.GONE);
				}else{
					line3.setVisibility(View.INVISIBLE);
				}
				wanwei.setTextColor(Color.parseColor("#666666"));
				qianwei.setTextColor(Color.parseColor("#FF3232"));
				baiwei.setTextColor(Color.parseColor("#666666"));
				weiClickItemListener.clickQian(v);
			}
		});
		baiwei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				line1.setVisibility(View.INVISIBLE);
				line2.setVisibility(View.INVISIBLE);
				line3.setVisibility(View.VISIBLE);
				wanwei.setTextColor(Color.parseColor("#666666"));
				qianwei.setTextColor(Color.parseColor("#666666"));
				baiwei.setTextColor(Color.parseColor("#FF3232"));
				weiClickItemListener.clickBai(v);
			}
		});
		line1 = (TextView) view.findViewById(R.id.iv_bottom_line1);
		line2 = (TextView) view.findViewById(R.id.iv_bottom_line2);
		line3 = (TextView) view.findViewById(R.id.iv_bottom_line3);
		qiShu_view = view.findViewById(R.id.dlt_qishu_view);
		img_curtain_rope = view.findViewById(R.id.dlt_qishu_down_view);
		downTx = (TextView) view.findViewById(R.id.dlt_down_tx);
		downIm = (ImageView) view.findViewById(R.id.dlt_down_im);
		LinearLayout.LayoutParams relLayoutParams = 
				new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		this.addView(view,relLayoutParams);
		qiShu_view.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				allInt  = qiShu_view.getHeight();
				linearlayout1Int  = linearlayout1.getHeight();
				linearlayout2Int  = linearlayout2.getHeight();
				if(wanfa == ConstantsBase.ZHIXUAN2||wanfa == ConstantsBase.ZHIXUAN3){//前直选
					curtainHeigh = allInt;
				}else{
					curtainHeigh = allInt-linearlayout1Int-linearlayout2Int;
				}
				CurtainView115.this.scrollTo(0, curtainHeigh);
				setTitle(wanfa);
			}
		});	
		img_curtain_rope.setOnTouchListener(this);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_add_ball);

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				FontDisplayUtil.dip2px(getContext(), 28),
				FontDisplayUtil.dip2px(getContext(), 28));
		param.gravity = Gravity.CENTER;
		param.setMargins(FontDisplayUtil.dip2px(getContext(),3), 0, 0, 0);// 5dp转像素
		for (int i = 0; i < 5; i++) {
			SingBallView singBallView = new SingBallView(getContext());
			singBallView.setGravity(Gravity.CENTER);
			String textString = null;
			textString = "?" ;
			singBallView.setText(textString);
			singBallView.setTextColor(this.getResources().getColor(R.color.white));
			singBallView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
			singBallView.setBackgroundResource(R.drawable.chooseballred);
			singBallViews.add(singBallView);// 添加到集合
			linearLayout.addView(singBallView, param);
		}

		timeend.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if("已截止".equals(s.toString())){
					show = false;	
				}else{
					show = true;
				}
			}  

			@Override  
			public void onTextChanged(CharSequence s, int start, int before, int count) {}  

			@Override  
			public void afterTextChanged(Editable s) {

				if("已截止".equals(s.toString())
						&&show){				
					weiClickItemListener.clickTimeOver(timeend);
					show = false;
				}				

			}  
		}); 
		realtimeend.addTextChangedListener(new TextWatcher(){  
			@Override  
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}  

				@Override  
				public void onTextChanged(CharSequence s, int start, int before, int count) {}  

				@Override  
				public void afterTextChanged(Editable s) {  
					if("已截止".equals(s.toString())){
						weiClickItemListener.clickRealTimeOver(timeend);
					}
				}  
			});  
		}

		public void setWanfa(int type){
			wanfa = type;
			if(!firstTime){
				setTitle(wanfa);
			}else{
				firstTime = false;
			}
		}
		public void setTitle(int type){
			if(type == ConstantsBase.ZHIXUAN2||type == ConstantsBase.ZHIXUAN3){//前直选
				linearlayout1.setVisibility(View.VISIBLE);
				linearlayout2.setVisibility(View.VISIBLE);
				viewZhanwei.findViewById(R.id.linearLayout1).setVisibility(View.INVISIBLE);	
				viewZhanwei.findViewById(R.id.linearLayout2).setVisibility(View.INVISIBLE);	
				if(type == ConstantsBase.ZHIXUAN3){
					line1.setVisibility(View.VISIBLE);
					line2.setVisibility(View.INVISIBLE);
					line3.setVisibility(View.INVISIBLE);
					baiwei.setVisibility(View.VISIBLE);
					wanwei.setTextColor(Color.parseColor("#FF3232"));
					qianwei.setTextColor(Color.parseColor("#666666"));
					baiwei.setTextColor(Color.parseColor("#666666"));
					weiClickItemListener.clickWan(null);
				}
				if(type == ConstantsBase.ZHIXUAN2){
					line1.setVisibility(View.VISIBLE);
					line2.setVisibility(View.INVISIBLE);
					line3.setVisibility(View.GONE);
					baiwei.setVisibility(View.GONE);
					wanwei.setTextColor(Color.parseColor("#FF3232"));
					qianwei.setTextColor(Color.parseColor("#666666"));
					baiwei.setTextColor(Color.parseColor("#666666"));
					weiClickItemListener.clickWan(null);
				}
				curtainHeigh = allInt;
				if(!Zhankai){
					CurtainView115.this.scrollTo(0, curtainHeigh);
				}
			}else{
				linearlayout1.setVisibility(View.GONE);
				linearlayout2.setVisibility(View.GONE);	
				viewZhanwei.findViewById(R.id.linearLayout1).setVisibility(View.GONE);	
				viewZhanwei.findViewById(R.id.linearLayout2).setVisibility(View.GONE);	
				curtainHeigh = allInt-linearlayout1Int-linearlayout2Int;
				if(!Zhankai){
					CurtainView115.this.scrollTo(0, curtainHeigh);
				}
			}
		}
		public void clearTime(){
			if(mMyCountTime!=null){
				mMyCountTime.cancel();
				mMyCountTime = null;
			}if(mMyCountTimeReal!=null){
				mMyCountTimeReal.cancel();
				mMyCountTimeReal = null;
			}
			timeend.setText("");
			realtimeend.setText("");
		}
		public void openTime(String qishu,Long time,Long realtime) {
			qishunext.setText("距"+qishu+"期截止还有");
			if(time<=0l){
				time = 0l;
			}
			mMyCountTime = new MyCountTime2(mContext, time*1000,
					1000, timeend, "已截止", "", null, false);
			mMyCountTime.start();

			mMyCountTimeReal = new MyCountTime2(mContext, realtime*1000,
					1000, realtimeend, "已截止", "", null, false);
			mMyCountTimeReal.start();
		}
		public void showNone(Prize prizeNumber){
			String number = prizeNumber.getPeriodNumber();
			number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length());
			if("00".equals(number)){
				number = "01";
			}
			qishunow.setText("等待"+number+"期开奖");
			for (int i = 0; i < singBallViews.size(); i++) {
				singBallViews.get(i).setText("?");
				singBallViews.get(i).setTextColor(this.getResources().getColor(R.color.white));
				singBallViews.get(i).setTextAfterAnim("?");
			}
		}
		public void showIng(Prize prizeNumber){
			String number = prizeNumber.getPeriodNumber();
			number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length());
			qishunow.setText(number+"期开奖中...");
			for (int i = 0; i < singBallViews.size(); i++) {
				singBallViews.get(i).setText("?");
				singBallViews.get(i).setTextColor(this.getResources().getColor(R.color.white));
				singBallViews.get(i).setTextAfterAnim("?");
				singBallViews.get(i).setTime(250);// 第一个球也有动画
				singBallViews.get(i).setIsStop(false);
				singBallViews.get(i).startRotateAnimation(false);
			}
		}
		public void showNumber(Prize prizeNumber){
			String number = prizeNumber.getPeriodNumber();
			number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length());
			qishunow.setText(number+"期开奖中...");
			String[] numbers = prizeNumber.getResult().split(",");
			for (int i = 0; i < singBallViews.size(); i++) {
				String textString = null;
				if (numbers[i].length() == 1) {
					textString = "0" + numbers[i];
				} else {
					textString = "" + numbers[i];
				}
				// 获取第i个view
				singBallViews.get(i).setTextAfterAnim(textString);
				singBallViews.get(i).setTextColor(
						this.getResources().getColor(R.color.white));
				singBallViews.get(i).setTime(i * 250 + 1);// 第一个球也有动画
				singBallViews.get(i).startRotateAnimation();
			}
			qishunow.setText(number+"期开奖号码");

		}
		public void changeNumber(Prize prizeNumber) {
			String number = prizeNumber.getPeriodNumber();
			number = number.substring(prizeNumber.getPeriodNumber().length()-2,prizeNumber.getPeriodNumber().length())+"期";
			if(prizeNumber == null||prizeNumber.getResult() == null
					||prizeNumber.getResult().split(",") == null
					||prizeNumber.getResult().split(",").length == 0){
				qishunow.setText(number+"开奖中...");
			}else{
				qishunow.setText(number+"开奖号码");
				String[] numbers = prizeNumber.getResult().split(",");
				for (int i = 0; i < singBallViews.size(); i++) {
					String textString = null;
					if (numbers[i].length() == 1) {
						textString = "0" + numbers[i];
					} else {
						textString = "" + numbers[i];
					}
					// 获取第i个view
					singBallViews.get(i).setTextAfterAnim(textString);
					singBallViews.get(i).setTextColor(
							this.getResources().getColor(R.color.white));
					singBallViews.get(i).setTime(i * 250 + 1);// 第一个球也有动画
					singBallViews.get(i).startRotateAnimation();
				}

			}
		}
		public void addItenView(ArrayList<Prize> resultList,String wei){
			clear();
			for(int i = 0;i<resultList.size();i++){
				setPrizeItem(i,resultList.get(i),wei);
			}	
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
		}
		public void setViewZhanwei(View view){
			viewZhanwei = view;
		}
		public void clear(){
			for(int i = 0;i<10;i++){
				View qishu = view.findViewById(qishus[i]);
				for(int j=0;j<11;j++){
					TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
					qishuget.setBackgroundColor(Color.TRANSPARENT);  
					qishuget.setTextColor(Color.parseColor("#666666"));
				}
			}	
		}
		public void setPrizeItem(int i,Prize prize115,String wei){
			View qishu = view.findViewById(qishus[9-i]);
			if(i%2==0){
				qishu.setBackgroundColor(Color.parseColor("#F8F8F8"));
			}else{
				qishu.setBackgroundColor(Color.parseColor("#F0F0F0"));
			}
			TextView qishunumber = (TextView) qishu.findViewById(R.id.qici);
			if(prize115.getPeriodNumber().length()>3){
				String number = prize115.getPeriodNumber();
				qishunumber.setText(number.substring(number.length()-2,number.length())+"期");
			}else{
				qishunumber.setText(prize115.getPeriodNumber()+"期");
			}
			String prizeNumber = prize115.getResult();
			String[] prizesMain = prizeNumber.split("\\|");
			if(prizesMain.length == 2){
				String[] prizes1 = prizesMain[0].split(",");
				String[] prizes2 = prizesMain[1].split(",");
				int k = 0;
				for(int j=0;j<11;j++){
					TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
					if("0".equals(prizes2[j])){
						if((j+1)<10){
							qishuget.setText("0"+(j+1));
						}else{
							qishuget.setText((j+1)+"");
						}
						qishuget.setBackgroundResource(R.drawable.chooseballred);
						qishuget.setTextColor(Color.WHITE);
						k++;
					}else{
						qishuget.setText(prizes2[j]);
						qishuget.setBackgroundColor(Color.TRANSPARENT);  
						qishuget.setTextColor(Color.parseColor("#666666"));
					}
				}
			}
			if(prizesMain.length == 4){
				String[] prizes1 = prizesMain[0].split(",");
				String[] prizes2 = prizesMain[1].split(",");
				String[] prizes3 = prizesMain[2].split(",");
				String[] prizes4 = prizesMain[3].split(",");
				if("wan".equals(wei)){
					for(int j=0;j<11;j++){
						TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
						if("0".equals(prizes2[j])){
							if((j+1)<10){
								qishuget.setText("0"+(j+1));
							}else{
								qishuget.setText((j+1)+"");
							}
							qishuget.setBackgroundResource(R.drawable.chooseballred);
							qishuget.setTextColor(Color.WHITE);
						}else{
							qishuget.setText(prizes2[j]);
							qishuget.setBackgroundColor(Color.TRANSPARENT);  
							qishuget.setTextColor(Color.parseColor("#666666"));
						}
					}
				}else if("qian".equals(wei)){
					for(int j=0;j<11;j++){
						TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
						if("0".equals(prizes3[j])){
							if((j+1)<10){
								qishuget.setText("0"+(j+1));
							}else{
								qishuget.setText((j+1)+"");
							}
							qishuget.setBackgroundResource(R.drawable.chooseballred);
							qishuget.setTextColor(Color.WHITE);
						}else{
							qishuget.setText(prizes3[j]);
							qishuget.setBackgroundColor(Color.TRANSPARENT);  
							qishuget.setTextColor(Color.parseColor("#666666"));
						}
					}
				}else if("bai".equals(wei)){
					for(int j=0;j<11;j++){
						TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
						if("0".equals(prizes4[j])){
							if((j+1)<10){
								qishuget.setText("0"+(j+1));
							}else{
								qishuget.setText((j+1)+"");
							}
							qishuget.setBackgroundResource(R.drawable.chooseballred);
							qishuget.setTextColor(Color.WHITE);
						}else{
							qishuget.setText(prizes4[j]);
							qishuget.setBackgroundColor(Color.TRANSPARENT);  
							qishuget.setTextColor(Color.parseColor("#666666"));
						}
					}
				}else if("0".equals(wei)){
					for(int j=0;j<11;j++){
						TextView qishuget = (TextView) qishu.findViewById(qishutexts[j]);
						if("0".equals(prizes2[j])){
							if((j+1)<10){
								qishuget.setText("0"+(j+1));
							}else{
								qishuget.setText((j+1)+"");
							}
							qishuget.setBackgroundResource(R.drawable.chooseballred);
							qishuget.setTextColor(Color.WHITE);
						}else{
							qishuget.setText(prizes2[j]);
							qishuget.setBackgroundColor(Color.TRANSPARENT);  
							qishuget.setTextColor(Color.parseColor("#666666"));
						}
					}
				}
			}	
		}
		/**
		 * 拖动动画
		 * @param startY  
		 * @param dy  垂直距离, 滚动的y距离
		 * @param duration 时间
		 */
		public void startMoveAnim(int startY, int dy, int duration) {
			isMove = true;
			mScroller.startScroll(0, startY, 0, dy, duration);
			invalidate();//通知UI线程的更新
		}

		public RotateAnimation upDown(float from,float to)
		{
			RotateAnimation animation =new RotateAnimation(from,to,Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
			animation.setDuration(500);//设置动画持续时间 
			animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
			return animation;
		}
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub
			super.onLayout(changed, l, t, r, b);
		}

		@Override
		public void computeScroll() {
			//判断是否还在滚动，还在滚动为true
			if (mScroller.computeScrollOffset()) {
				scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
				//更新界面
				postInvalidate();
				isMove = true;
			} else {
				isMove = false;
			}
			super.computeScroll();
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(viewZhanwei == null){
				return false;
			}
			if (!isMove) {
				int offViewY = 0;//屏幕顶部和该布局顶部的距离
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downY = (int) event.getRawY();
					offViewY = downY - (int)event.getX();
					return true;
				case MotionEvent.ACTION_MOVE:
					moveY = (int) event.getRawY();
					scrollY = moveY - downY;
					if (scrollY < 0) {
						if(isOpen){
							if(Math.abs(scrollY) <= qiShu_view.getBottom() - offViewY){
								scrollTo(0, -scrollY);
							}
						}
					} else if (scrollY > 0){
						if (scrollY <= curtainHeigh) {
							if(!isOpen){
								setDownIm("上拉");
								viewZhanwei.setVisibility(View.INVISIBLE);
								scrollTo(0, curtainHeigh - scrollY);
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					upY = (int) event.getRawY();
					if(Math.abs(upY - downY) < 10){
						onRopeClick();
						break;
					}
					if (downY > upY) {
						// 向上滑动
						setDownIm("下拉");
						viewZhanwei.setVisibility(View.GONE);
						startMoveAnim(this.getScrollY(),
								(curtainHeigh - this.getScrollY()), upDuration);
						isOpen = false;
					} else {
						// 向下滑动
						setDownIm("上拉");
						viewZhanwei.setVisibility(View.INVISIBLE);
						CurtainView115.this.startMoveAnim(this.getScrollY(), -this.getScrollY(), downDuration);
						isOpen = true;
					}
					break;
				default:
					break;
				}
			}
			return false;
		}
		/**
		 * 点击绳索开关，会展开关闭
		 * 在onToch中使用这个中的方法来当点击事件，避免了点击时候响应onTouch的衔接不完美的影响
		 */
		public void onRopeClick(){

			if(isOpen){
				if(viewZhanwei!=null){
					setDownIm("下拉");
					viewZhanwei.setVisibility(View.GONE);
				}
				CurtainView115.this.startMoveAnim(0, curtainHeigh, upDuration);
				isOpen = false;
			}else{
				if(viewZhanwei!=null){
					setDownIm("上拉");
					viewZhanwei.setVisibility(View.INVISIBLE);
				}
				CurtainView115.this.startMoveAnim(curtainHeigh,-curtainHeigh, downDuration);
				isOpen = true;
			}
		}
		public void setDownIm(String text){
			if("上拉".equals(text)&&!Zhankai){
				downIm.setAnimation(upDown(0f,180f));
				Zhankai = true;
			}
			if("下拉".equals(text)&&Zhankai){
				downIm.setAnimation(upDown(180f,360f));
				Zhankai = false;
			}
			downTx.setText(text);
		}
		public interface WeiClickItemListener{
			//有高级调用者去实现点击后该干什么
			public void clickWan(View view);
			public void clickQian(View view);
			public void clickBai(View view);

			public void clickTimeOver(View view);
			public void clickRealTimeOver(View view);
		}
	}
