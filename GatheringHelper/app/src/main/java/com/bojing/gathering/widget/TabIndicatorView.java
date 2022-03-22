package com.bojing.gathering.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.util.xUtilsImageUtils;


public class TabIndicatorView extends RelativeLayout {
	private int rank; 
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	private ImageView ivTabIcon;
	private TextView tvTabHint;
	private TextView tvTabUnRead;
	private String  normalIconUrl;
	private String  focusIconUrl;
	private boolean isLocalDrawable;
    private Context context;//为了创建bitmap对象
	public TabIndicatorView(Context context) {
		this(context, null);
		this.context=context;
		
		
		
		
	}

	public TabIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 将布局文件和 代码进行绑定
		View.inflate(context, R.layout.tab_indicator, this);

		ivTabIcon = (ImageView) findViewById(R.id.tab_indicator_icon);
		tvTabHint = (TextView) findViewById(R.id.tab_indicator_hint);
		tvTabUnRead = (TextView) findViewById(R.id.tab_indicator_unread);
		setTabUnreadCount(0);
	}

	// 设置tab的title
	public void setTabTitle(String title) {
		tvTabHint.setText(title);
	}
	

	// 初始化图标
	public void setTabIcon(String normalIconUrl, String focusIconUrl,boolean isLocalDrawable) {
		this.normalIconUrl = normalIconUrl;
		
		this.focusIconUrl = focusIconUrl;
		this.isLocalDrawable=isLocalDrawable;
       //默认是什么样子的 工具给其附图片
		if (isLocalDrawable) {
			ivTabIcon.setImageResource(Integer.parseInt(normalIconUrl));
		}else {
			xUtilsImageUtils.displayIN(ivTabIcon,R.mipmap.ic_launcher,normalIconUrl);
		}
		
	}
	// 设置未读数
	public void setTabUnreadCount(int unreadCount) {
		if (unreadCount <= 0) {
			tvTabUnRead.setVisibility(View.GONE);
		} else {
			if (unreadCount <= 99) {
				tvTabUnRead.setText(unreadCount + "");
			} else {
				tvTabUnRead.setText("99+");
			}

			tvTabUnRead.setVisibility(View.VISIBLE);
		}
	}

	// 设置选中
	public void setTabSelected(boolean selected) {
		if (selected) {
			tvTabHint.setTextColor(context.getResources().getColor(R.color.mainChoosed));
			if (isLocalDrawable) {
				ivTabIcon.setImageResource(Integer.parseInt(focusIconUrl));
			}else {
				xUtilsImageUtils.display(ivTabIcon,R.mipmap.ic_launcher,focusIconUrl);
			}
		} else {
			tvTabHint.setTextColor(context.getResources().getColor(R.color.mainUnChoosed));
			if (isLocalDrawable) {
				ivTabIcon.setImageResource(Integer.parseInt(normalIconUrl));
			}else {
				xUtilsImageUtils.display(ivTabIcon,R.mipmap.ic_launcher,normalIconUrl);
			}
		}
	}
}
