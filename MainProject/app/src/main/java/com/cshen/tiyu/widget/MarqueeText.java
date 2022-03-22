package com.cshen.tiyu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 2018/10/8.
 */

public class MarqueeText extends android.support.v7.widget.AppCompatTextView {
    public MarqueeText(Context con) {
        super(con);
    }
    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
    @Override
    public boolean isFocused() {
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction,Rect previouslyFocusedRect)
    {}
}
