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
import android.widget.LinearLayout.LayoutParams;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.Prize;

public class CurtainViewGuiZe extends LinearLayout implements OnTouchListener{
	private static String TAG = "CurtainViewGuiZe";
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
	private ImageView downIm;
	private boolean hasUp = false;
	/** 上升动画时间 */
	private int upDuration = 250;
	/** 下落动画时间 */
	private int downDuration = 250;
	View view;
	public CurtainViewGuiZe(Context context) {
		super(context);
		init(context);
	}

	public CurtainViewGuiZe(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CurtainViewGuiZe(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/** 初始化 */
	private void init(Context context) {
		this.mContext = context;
		mScroller = new Scroller(context);
		// 背景设置成透明
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view = LayoutInflater.from(mContext).inflate(R.layout.guize, null);

		img_curtain_ad = view.findViewById(R.id.ad);
		img_curtain_rope = view.findViewById(R.id.rope);
		downIm = (ImageView) view.findViewById(R.id.down_im);
		LinearLayout.LayoutParams relLayoutParams = 
				new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		this.addView(view,relLayoutParams);
		img_curtain_ad.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				curtainHeigh  = img_curtain_ad.getHeight();
				CurtainViewGuiZe.this.scrollTo(0, curtainHeigh);
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
					startMoveAnim(this.getScrollY(),
							(curtainHeigh - this.getScrollY()), upDuration);
					isOpen = false;
				} else {
					// 向下滑动
					setDownIm("上拉");
					CurtainViewGuiZe.this.startMoveAnim(this.getScrollY(), -this.getScrollY(), downDuration);
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
			setDownIm("下拉");
			CurtainViewGuiZe.this.startMoveAnim(0, curtainHeigh, upDuration);
			isOpen = false;
		}else{
			setDownIm("上拉");
			CurtainViewGuiZe.this.startMoveAnim(curtainHeigh,-curtainHeigh, downDuration);
			isOpen = true;
		}
	}
	public void setDownIm(String text){
		if("上拉".equals(text)&&!hasUp){
			downIm.startAnimation(upDown(0f,180f));
			hasUp = true;
		}
		if("下拉".equals(text)&&hasUp){
			downIm.startAnimation(upDown(180f,360f));
			hasUp = false;
		}
	}
}

