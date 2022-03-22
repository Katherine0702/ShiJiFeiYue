package com.cshen.tiyu.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
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
import com.cshen.tiyu.domain.Prize;

public class CurtainView extends LinearLayout implements OnTouchListener{
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
	/** 是否 打开*/
	private boolean isOpen = false;
	/** 是否在动画 */
	private boolean isMove = false;
	/** 类似绳子的图片*/
	private View img_curtain_rope;
	/** 类似卷帘的图片*/
	private View img_curtain_ad;
	private ScrollView scrollView;
	private View downMainView;//奖池缓存
	private TextView jiangchi;//奖池缓存
	private LinearLayout  qiShu;//动态加载view
	private TextView downTx;//下拉文字
	private ImageView downIm;//下拉图标
	private TextView nextQishu;//下期开奖
	private boolean hasUp = false;
	/** 上升动画时间 */
	private int upDuration = 250;
	/** 下落动画时间 */
	private int downDuration = 250;
	View view,viewZhanwei;
	int isPl35 = -1;//0 排列3 ；1 排列5
	boolean isSSQ = false;
	private int[] qishus = new int[]{
			R.id.qishu0,R.id.qishu1,R.id.qishu2,R.id.qishu3,R.id.qishu4,
			R.id.qishu5,R.id.qishu6,R.id.qishu7,R.id.qishu8,R.id.qishu9};
	public CurtainView(Context context) {
		super(context);
		init(context);
	}

	public CurtainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CurtainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/** 初始化 */
	private void init(Context context) {
		this.mContext = context;
		mScroller = new Scroller(context);
		// 背景设置成透明
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view = LayoutInflater.from(mContext).inflate(R.layout.curtain, null);
		scrollView = (ScrollView) view.findViewById(R.id.dlt_qishu_scroll);
		img_curtain_ad = view.findViewById(R.id.dlt_qishu_view);
		qiShu = (LinearLayout)view.findViewById(R.id.dlt_qishu);
		img_curtain_rope = view.findViewById(R.id.dlt_qishu_down_view);
		downTx = (TextView) view.findViewById(R.id.dlt_down_tx);
		downIm = (ImageView) view.findViewById(R.id.dlt_down_im);
		jiangchi = (TextView) view.findViewById(R.id.dlt_jiangchi);
		nextQishu = (TextView) view.findViewById(R.id.dlt_next_qishu);
		LinearLayout.LayoutParams relLayoutParams = 
				new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		this.addView(view,relLayoutParams);
		img_curtain_ad.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				curtainHeigh  = img_curtain_ad.getHeight();
				CurtainView.this.scrollTo(0, curtainHeigh);
				//注意scrollBy和scrollTo的区别
			}
		});
		img_curtain_rope.setOnTouchListener(this);
	}
	public RotateAnimation upDown(float from,float to)
	{
		RotateAnimation animation =new RotateAnimation(from,to,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		animation.setDuration(500);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		return animation;
	}
	public void setTextView(String allMoney,String time){
		String jiangchiStr = "<html><font color=\"#7D7D7D\">奖池缓存：</font><font color=\"#E73C42\">&#160;"+
				allMoney+"元"+"</font></html>";

		jiangchi.setText(Html.fromHtml(jiangchiStr));
		nextQishu.setText(time);
	}
	public void addItenView(ArrayList<Prize> resultList){
		for(int i = 0;i<resultList.size();i++){
			setPrizeItem(i,resultList.get(i));
		}	
		scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
	}
	public void setViewZhanwei(View view){
		viewZhanwei = view;
	}
	public void setPl35(int pl35){
		isPl35 = pl35;
	}
	public void setSSQ(boolean isssq){
		isSSQ = isssq;
	}
	public void setPrizeItem(int i,Prize dltprize){
		View qishu = view.findViewById(qishus[i]);
		if(i%2==0){
			qishu.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}else{
			qishu.setBackgroundColor(Color.parseColor("#efefef"));
		}
		TextView qishunumber = (TextView) qishu.findViewById(R.id.dlt_qishu_number_title);
		qishunumber.setText(dltprize.getPeriodNumber()+"期");
		String prizeNumber = dltprize.getResult();
		String[] prizes = prizeNumber.split(",");
		TextView qishunumber1 = (TextView) qishu.findViewById(R.id.dlt_qishu_number1);
		qishunumber1.setText(prizes[0].length() == 2?prizes[0]:"0"+prizes[0]);
		TextView qishunumber2 = (TextView) qishu.findViewById(R.id.dlt_qishu_number2);
		qishunumber2.setText(prizes[1].length() == 2?prizes[1]:"0"+prizes[1]);
		TextView qishunumber3 = (TextView) qishu.findViewById(R.id.dlt_qishu_number3);
		qishunumber3.setText(prizes[2].length() == 2?prizes[2]:"0"+prizes[2]);
		if(prizes.length == 5){		
			TextView qishunumber4 = (TextView) qishu.findViewById(R.id.dlt_qishu_number4);
			TextView qishunumber5 = (TextView) qishu.findViewById(R.id.dlt_qishu_number5);
			if(isPl35 == 0){
				qishunumber4.setVisibility(View.GONE);
				qishunumber5.setVisibility(View.GONE);
			}else{
				qishunumber4.setText(prizes[3].length() == 2?prizes[3]:"0"+prizes[3]);
				qishunumber5.setText(prizes[4].length() == 2?prizes[4]:"0"+prizes[4]);
				qishunumber4.setVisibility(View.VISIBLE);
				qishunumber5.setVisibility(View.VISIBLE);
			}
			qishu.findViewById(R.id.dlt_qishu_number6).setVisibility(View.GONE);
			qishu.findViewById(R.id.dlt_qishu_number7).setVisibility(View.GONE);
		}	
		if(prizes.length == 7){			
			TextView qishunumber4 = (TextView) qishu.findViewById(R.id.dlt_qishu_number4);
			qishunumber4.setText(prizes[3].length() == 2?prizes[3]:"0"+prizes[3]);
			TextView qishunumber5 = (TextView) qishu.findViewById(R.id.dlt_qishu_number5);
			qishunumber5.setText(prizes[4].length() == 2?prizes[4]:"0"+prizes[4]);
			TextView qishunumber6 = (TextView) qishu.findViewById(R.id.dlt_qishu_number6);
			qishunumber6.setText(prizes[5].length() == 2?prizes[5]:"0"+prizes[5]);
			TextView qishunumber7 = (TextView) qishu.findViewById(R.id.dlt_qishu_number7);
			qishunumber7.setText(prizes[6].length() == 2?prizes[6]:"0"+prizes[6]);
			if(isSSQ){
				qishunumber6.setTextColor(Color.parseColor("#E73C42"));
			}else{
				qishunumber6.setTextColor(Color.parseColor("#3389F1"));
			}
			qishunumber4.setVisibility(View.VISIBLE);
			qishunumber5.setVisibility(View.VISIBLE);
			qishunumber6.setVisibility(View.VISIBLE);
			qishunumber7.setVisibility(View.VISIBLE);
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
						if(Math.abs(scrollY) <= img_curtain_ad.getBottom() - offViewY){
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
					CurtainView.this.startMoveAnim(this.getScrollY(), -this.getScrollY(), downDuration);
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
			CurtainView.this.startMoveAnim(0, curtainHeigh, upDuration);
			isOpen = false;
		}else{
			if(viewZhanwei!=null){
				setDownIm("上拉");
				viewZhanwei.setVisibility(View.INVISIBLE);
			}
			CurtainView.this.startMoveAnim(curtainHeigh,-curtainHeigh, downDuration);
			isOpen = true;
		}
	}
	public void setDownIm(String text){
		if("上拉".equals(text)&&!hasUp){
			downIm.setAnimation(upDown(0f,180f));
			hasUp = true;
		}
		if("下拉".equals(text)&&hasUp){
			downIm.setAnimation(upDown(180f,360f));
			hasUp = false;
		}
		downTx.setText(text);
	}
}