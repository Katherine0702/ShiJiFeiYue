package com.cshen.tiyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.utils.ToastUtils;

public class JixuanListView extends ListView implements 
android.widget.AdapterView.OnItemClickListener,OnItemLongClickListener {

	private static final int STATE_PULL_REFRESH = 0;

	private static final int STATE_REFRESHING = 2;

	private int mCurrrentState = STATE_PULL_REFRESH;

	private int startY = -1;// 滑动起点的y坐标
	private View mHeaderView;
	private TextView tvTitle;
	private ImageView ivArrow;

	private int mHeaderViewHeight;
	private RotateAnimation animDown;
	private int dantuo = 1;

	Context context;
	public JixuanListView(Context context) {
		super(context);
		this.context = context;
		initHeaderView();
		// TODO 自动生成的构造函数存根
	}

	public JixuanListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initHeaderView();
		// TODO 自动生成的构造函数存根
	}

	public JixuanListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initHeaderView();
		// TODO 自动生成的构造函数存根
	}

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		mHeaderView.findViewById(R.id.tv_time).setVisibility(View.GONE);

		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		mHeaderView.findViewById(R.id.pb_progress).setVisibility(View.GONE);
		mHeaderView.measure(0, 0);

		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
		initArrowAnim();

	}
	public View getHeaderView(){
		return mHeaderView;
	}
	public void setWanFa(int dantuo){
		this.dantuo = dantuo;
	}
	//初始化动画
	private void initArrowAnim() {
		animDown = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}
	public interface OnJixuanListener{
		public void onJixuan();//机选
	}
	OnJixuanListener mListener;
	public void setOnJixuanListener(OnJixuanListener listener) {// listener 为具体的 累对象 面向借口编程
		mListener = listener;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:			
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}
			int endY = (int) ev.getRawY();
			int dy = endY - startY;
			if (dy > 0 ) {
				int padding = dy - mHeaderViewHeight;
				if (padding > 0 ) {
					if(dantuo == 0){
						ToastUtils.showShort(context, "胆拖暂不支持下拉机选");
					}else{
						mCurrrentState = STATE_REFRESHING;
						mHeaderView.setPadding(0, 0, 0, 0);//显示
						refreshState();
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;

			if (mCurrrentState == STATE_REFRESHING) {
				mCurrrentState = STATE_PULL_REFRESH;
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
				refreshState();
				return true;
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	private void refreshState() {

		// TODO 自动生成的方法存根
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉机选");
			ivArrow.setVisibility(View.INVISIBLE);
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在机选...");
			ivArrow.startAnimation(animDown);
			ivArrow.setVisibility(View.VISIBLE);
			if(mListener!=null){
				mListener.onJixuan();//调用实现接口的类的方法
			}
			break;
		default:
			break;
		}



	}
	OnItemClickListener mItemClickListener;
	OnItemLongClickListener mItemLongClickListener;
	@Override
	public void setOnItemClickListener(//重写气点击事件 应为position变多了必须要解掉一个 保证与数据对应好
			android.widget.AdapterView.OnItemClickListener listener) {
		// TODO 自动生成的方法存根
		super.setOnItemClickListener(this);
		mItemClickListener = listener;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		if(mItemClickListener!=null){
			mItemClickListener.onItemClick(arg0, arg1, arg2
					- getHeaderViewsCount(), arg3);//重写气点击事件 应为position变多了必须要解掉一个 保证与数据对应好


		}
	}
	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		// TODO 自动生成的方法存根
		super.setOnItemLongClickListener(this);
		mItemLongClickListener = listener;
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(mItemLongClickListener!=null){
			mItemLongClickListener.onItemLongClick(arg0, arg1, arg2
					- getHeaderViewsCount(), arg3);//重写气点击事件 应为position变多了必须要解掉一个 保证与数据对应好

		}
		return true;
	}
}
