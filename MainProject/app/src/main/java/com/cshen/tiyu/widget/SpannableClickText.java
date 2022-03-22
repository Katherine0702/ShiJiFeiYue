package com.cshen.tiyu.widget;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pc on 2017/8/17.
 * 部分字体颜色改变，并可点击的textView ，如xxx联系客服
 */

public class SpannableClickText {

    private boolean isUnderline = false;
    private onTextClickListener mListener;
    private TextView mTextView;
    private String text;
    private int color;

    /**
     * 构造函数
     * @param mTextView textView
     * @param text      文字
     * @param color     部分字体颜色
     */
    public SpannableClickText(TextView mTextView, String text, int color) {
        this.mTextView = mTextView;
        this.text = text;
        this.color = color;
    }

    /**
     * 设置字体
     * @param start  设置字体在字符串中的开始位置
     * @param end    设置字体在字符串中的结束位置
     * @param listener  点击监听
     */
    public void setSpannableText(int start, int end, onTextClickListener listener) {
        SpannableString spanStr = new SpannableString(text);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableText(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        mTextView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明

        mTextView.setText(spanStr);
    }

    /**
     * 设置是否显示下划线
     * @param hasUnderline
     */
    public void setHasUnderline(boolean hasUnderline) {
        isUnderline = hasUnderline;
    }

    /**
     * 可点击
     */
    class ClickableText extends ClickableSpan {
        public ClickableText(onTextClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onclick(view);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(color);
            ds.setUnderlineText(isUnderline);
        }
    }

    public interface onTextClickListener {
        void onclick(View view);
    }
}
