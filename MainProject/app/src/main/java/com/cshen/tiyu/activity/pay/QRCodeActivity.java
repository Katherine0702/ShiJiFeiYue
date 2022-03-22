package com.cshen.tiyu.activity.pay;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.xutils.x;

import com.cshen.tiyu.R;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.tool.DistinguishQRCode;
import com.cshen.tiyu.tool.RGBLuminanceSource;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class QRCodeActivity extends Activity{
	public static final int ZFBBACK = 1;
	QRCodeActivity _this;
	ImageView iv_back,yanzhengma;
	TextView  username;
	TextView  tv_head_title,username1;
	View text2viewzfb,text2viewwx;
	TextView text1,text3,text4,qr;
	int payway = 13;//17 支付宝图片定，18微信图片定。13微信图片变，14支付宝图片变
	String payUrl ;//17 支付宝图片定，18微信图片定。13微信图片变，14支付宝图片变
	String orderID ;//17 支付宝图片定，18微信图片定。13微信图片变，14支付宝图片变
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode);
		_this = this;
		payway = getIntent().getIntExtra("paywayInt", 17);
		payUrl = getIntent().getStringExtra("payUrl")+"?"+System.currentTimeMillis();
		orderID = getIntent().getStringExtra("orderID");
		initView();
	}
	private void initView() {
		iv_back =  (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
		tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		username  = (TextView) findViewById(R.id.username);
		username1 = (TextView) findViewById(R.id.username1);
		yanzhengma = (ImageView)findViewById(R.id.yanzhengma);
		text1 = (TextView) findViewById(R.id.text1);
		text2viewzfb = findViewById(R.id.text2viewzfb);
		text2viewwx = findViewById(R.id.text2viewwx);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);
		qr = (TextView)findViewById(R.id.qr);
		qr.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(payway == 13||payway == 18){
					toWX();
				}else if(payway == 14||payway == 17){
					toZFB();
				}
			}
		});
		if(payway == 14||payway == 17){
			if(payway == 14){
				username.setVisibility(View.INVISIBLE);
				username1.setText("订单号："+orderID);
				xUtilsImageUtils.display(yanzhengma,R.mipmap.zhifubao,payUrl);
			}if(payway == 17){
				username.setVisibility(View.VISIBLE);
				username1.setText("用户名："+PreferenceUtil.getString(_this, "username")) ;
				xUtilsImageUtils.display(yanzhengma,R.mipmap.zhifubao,payUrl);
			}
			tv_head_title.setText("支付宝支付");
			qr.setText("保存图片并打开支付宝");
			text1.setText(Html.fromHtml(
					"<html><font color=\"#000000\">1、点击下方" +"</font><font color=\"#FF3232\">\"保存图片，并打开支付宝\""+ "</font><font color=\"#000000\">，并同时打开支付宝</font></html>"));
			text2viewwx.setVisibility(View.GONE);
			text2viewzfb.setVisibility(View.VISIBLE);
			text3.setVisibility(View.VISIBLE);
			text4.setVisibility(View.VISIBLE);
		}else if(payway == 13||payway == 18){
			if(payway == 13){
				username.setVisibility(View.INVISIBLE);
				username1.setText("订单号："+orderID);
				xUtilsImageUtils.display(yanzhengma,R.mipmap.weixin,payUrl);
			}
			if(payway == 18){
				username.setVisibility(View.VISIBLE);
				username1.setText("用户名："+PreferenceUtil.getString(_this, "username")) ;
				xUtilsImageUtils.display(yanzhengma,R.mipmap.weixin,payUrl);
			}
			tv_head_title.setText("微信扫码支付");
			qr.setText("保存图片并打开微信");
			text1.setText(Html.fromHtml(
					"<html><font color=\"#000000\">1、点击下方" +"</font><font color=\"#FF3232\">\"保存图片，并打开微信\""+ "</font><font color=\"#000000\">，并同时打开微信</font></html>"));
			text2viewwx.setVisibility(View.VISIBLE);
			text2viewzfb.setVisibility(View.GONE);
			text3.setVisibility(View.VISIBLE);
			text4.setVisibility(View.VISIBLE);
		}else  if(payway == 38){			
			username.setVisibility(View.INVISIBLE);
			tv_head_title.setText("QQ支付");
			username1.setText("订单号："+orderID);
			xUtilsImageUtils.display(yanzhengma,R.mipmap.zhifubao,payUrl);
			new MyAsyTask().execute();
		}
	}
	public void toZFB(){
		try {
			EventBus.getDefault().post("backPay");
			Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			_this.finish();
		} catch (Exception e) {
			//若无法正常跳转，在此进行错误处理
			Toast.makeText(_this, "无法跳转到支付宝，请检查您是否安装了支付宝！", Toast.LENGTH_SHORT).show();
		}finally{
			DistinguishQRCode.getDistinguishQRCode().saveCurrentImage(_this);
		}
	}
	public void toWX(){
		try {
			EventBus.getDefault().post("backPay");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("weixin://"));    
			startActivity(intent);
			_this.finish();
		} catch (Exception e) {
			//若无法正常跳转，在此进行错误处理
			Toast.makeText(_this, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
		}finally{
			DistinguishQRCode.getDistinguishQRCode().saveCurrentImage(_this);
		}
	}
	public class MyAsyTask extends AsyncTask<Void, Void, Result>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Result doInBackground(Void... params) {
			Bitmap obmp = getBitMBitmap(payUrl);
			Result result = null;
			try {
				RGBLuminanceSource source = new RGBLuminanceSource(obmp);  
				//将图片转换成二进制图片
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
				//初始化解析对象
				QRCodeReader reader = new QRCodeReader();
				//开始解析
				result = reader.decode(binaryBitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			super.onPostExecute(result);
			if(result!=null){
				Intent intent = new Intent(_this,WebForPayActivity.class);
				intent.putExtra("url", result.toString());
				startActivityForResult(intent, ZFBBACK);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ZFBBACK) {
			ServiceUser.getInstance().GetUserInfo(_this);// 余额跟新	
			Intent intentClose = new Intent();
			setResult(RESULT_OK, intentClose);
			_this.finish(); 
		}
	}
	public static Bitmap getBitMBitmap(String urlpath) {
		Bitmap map = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			map = BitmapFactory.decodeStream(in);
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	private Bitmap generateBitmap(String content,int width, int height) {  
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();  
	    Map<EncodeHintType, String> hints = new HashMap<>();  
	    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
	    try {  
	        BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);  
	        int[] pixels = new int[width * height];  
	        for (int i = 0; i < height; i++) {  
	            for (int j = 0; j < width; j++) {  
	                if (encode.get(j, i)) {  
	                    pixels[i * width + j] = 0x00000000;  
	                } else {  
	                    pixels[i * width + j] = 0xffffffff;  
	                }  
	            }  
	        }  
	        return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);  
	    } catch (WriterException e) {  
	        e.printStackTrace();  
	    }  
	    return null;  
	}  
}
