package com.cshen.tiyu.activity;

import java.net.URL;

import com.cshen.tiyu.R;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.information.InformationDetail;
import com.cshen.tiyu.domain.information.InformationDetailBack;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class InformationDetailActivity extends Activity {
	InformationDetailActivity _this;
	TopViewLeft tv_head;
	TextView title,content,name,time;
	String id;
	@SuppressLint("NewApi") 
	ImageGetter imgGetter = new Html.ImageGetter() {  
		public Drawable getDrawable(String source) { 
			Drawable drawable = null;  
			URL url;  
			try {  
				url = new URL(ConstantsBase.HOMEIP+source);  
				drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
			} catch (Exception e) {  
				e.printStackTrace();  
				return null;  
			}  
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
					drawable.getIntrinsicHeight());  
			return drawable;  
		}  
	};  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informationdetail);
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();  
        StrictMode.setThreadPolicy(policy);  
		_this = this;
		id = getIntent().getStringExtra("ID"); 
		initView();
		if(!TextUtils.isEmpty(id)) {
			initdata();
		}
	}
	public void initView(){
		tv_head = (TopViewLeft) findViewById(R.id.title);
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

		title = (TextView) findViewById(R.id.titlestr);
		content   = (TextView) findViewById(R.id.content);
		name   = (TextView) findViewById(R.id.name);
		time   = (TextView) findViewById(R.id.time);
		content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页  
	}

	private void initdata() {	

		ServiceMain.getInstance().PostGetInformationDetail(_this,id,
				new CallBack<InformationDetailBack>() {

			@Override
			public void onSuccess(InformationDetailBack t) {
				// TODO 自动生成的方法存根	
				if(t!=null){
					InformationDetail id = t.getResult();
					if(id!=null){
						title.setText(id.getTitleDetail());
						content.setText(Html.fromHtml(id.getContent(), imgGetter, null));  
						name.setText(id.getSign());
						time.setText(id.getSignTime());
					}
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根				
				ToastUtils.showShort(_this,errorMessage.msg+"");		
			}
		});

	}
}
