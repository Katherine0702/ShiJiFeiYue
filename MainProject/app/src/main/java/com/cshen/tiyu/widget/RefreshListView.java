package com.cshen.tiyu.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cshen.tiyu.R;

public class RefreshListView extends ListView implements OnScrollListener,
android.widget.AdapterView.OnItemClickListener {

	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新

	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新

	private static final int STATE_REFRESHING = 2;// 正在刷新

	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态

	private int startY = -1;// 滑动起点的y坐标
	private View mHeaderView;
	private TextView tvTitle;
	private ImageView ivArrow;
	private ProgressBar pbProgress;

	private int mHeaderViewHeight;
	private TextView tvTime;
	private RotateAnimation animUp;
	private View mFooterView;

	private RotateAnimation animDown;



	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
		// TODO 自动生成的构造函数存根
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		// TODO 自动生成的构造函数存根
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
		// TODO 自动生成的构造函数存根
	}
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);

		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
		mHeaderView.measure(0, 0);

		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

		initArrowAnim();// init animation
		tvTime.setText("最后刷新时间"+getCurrentTime());

	}
	//初始化动画
	private void initArrowAnim() {

		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animUp.setDuration(200);
		animUp.setFillAfter(true);
		animDown = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}


	public interface OnRefreshListener{
		public void onRefresh();//刷新
		public void onLoadMore();//加载跟多
	}
	OnRefreshListener mListener;

	private int mFooterViewHeight;
	public void setOnRefreshListener(OnRefreshListener listener) {// listener 为具体的 累对象 面向借口编程
		mListener = listener;
	}

	/*
	 * 初始化脚布局
	 */
	private void initFooterView() {
		// TODO 自动生成的方法存根

		mFooterView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏 相对脚于布局来说的
		this.setOnScrollListener(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// TODO 自动生成的方法存根
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
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int padding = dy - mHeaderViewHeight;
			
				if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {
					mCurrrentState = STATE_RELEASE_REFRESH;// 松开刷新
					refreshState();
				} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {
					mCurrrentState = STATE_PULL_REFRESH;// 下拉刷新
					refreshState();
					return true;
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrrentState == STATE_RELEASE_REFRESH) {

				mCurrrentState = STATE_REFRESHING;// zaishuangxing
				mHeaderView.setPadding(0, 0, 0, 0);// 显示

				refreshState();
			} else if (mCurrrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏

			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}
	// refresh date
	private void refreshState() {
		// TODO 自动生成的方法存根
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:

			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animDown);


			break;

		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animUp);
			break;
		case STATE_REFRESHING:
			
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
			if(mListener!=null){

				mListener.onRefresh();//调用实现接口的类的方法

			}
			break;
		default:
			break;
		}



	}

	/**
	 * 获取当前时间
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/*
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete(boolean success){

		//对外提供
		if (isLoadingMore) {// 正在加载更多...
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
			isLoadingMore = false;
		} else {
			mCurrrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
			if (success) {
				tvTime.setText("最后刷新时间:" + getCurrentTime());
			}
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根

	}



	private boolean isLoadingMore;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore && mCurrrentState == STATE_PULL_REFRESH) {// 滑动到最后
				mFooterView.setPadding(0, 0, 0, 0);// 显示
				setSelection(getCount() - 1);// 改变listview显示位置

				isLoadingMore = true;

				if (mListener != null) {
					mListener.onLoadMore(); //接口对象调用方法
				}
			}
		}
	}
	OnItemClickListener mItemClickListener;
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
	
	
}
