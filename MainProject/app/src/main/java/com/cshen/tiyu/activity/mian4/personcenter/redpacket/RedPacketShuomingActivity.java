package com.cshen.tiyu.activity.mian4.personcenter.redpacket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class RedPacketShuomingActivity extends BaseActivity{

	WebView mWebView = null;
	private TopViewLeft tv_head;
	private TextView textcall;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shuoming);
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);	
		tv_head.setVisibility(View.VISIBLE);
		tv_head.setTitle("红包说明");			
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {}

			@Override
			public void clickContactView(View view) {}

			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		mWebView = (WebView) findViewById(R.id.wv_latest_lottery);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("file:///android_asset/hongBaoInfo.html");
		textcall = (TextView)findViewById(R.id.textcall);
		String call = "<html><font color=\"#000000\">若还有疑问，可咨询客服"
				+"</font><font color=\"#2B2BF1\"><u>4009282770"
				+ "</u></font></html>";
		textcall.setText(Html.fromHtml(call));
		textcall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					String phoneno= "4009282770";
					if(phoneno==null||"".equals(phoneno.trim()))
					{
						Toast.makeText(RedPacketShuomingActivity.this, "没有号码",Toast.LENGTH_SHORT).show();
					}
					else
					{
						Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneno));
						startActivity(intent);
					}
			}
		});
	}
}
