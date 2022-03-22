package com.cshen.tiyu.activity.mian4.find.shareorder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cshen.tiyu.utils.ScreenShotsUtils;
import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.BitmapUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.ContainsEmojiEditText;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class AddShareOrderActivity extends BaseActivity {
	private AddShareOrderActivity _this;
	private TopViewLeft tv_head;// 头
	private TextView tv_textSize;
	private ContainsEmojiEditText edt_content;
	private ImageView imageView;
	private Button btn_send;

	private int i = 0;  
	private int failCount=0;//上传图片失败的次数
	private boolean isShowing =false;
	private String screen_filePath;//截屏文件路径

	File file ;//上传图片内容
	String imgUrl;//晒单图片内容
	String content;//晒单文字内容

	private AlertDialog alertDialog;// 退出提示弹窗
	private Timer timer = new Timer();
	private View load;  
	private ImageView loading;
	private Handler handler = new Handler()  
	{  
		@Override  
		public void handleMessage(Message msg)  
		{  

			super.handleMessage(msg); 
			loading.setBackgroundResource(images[i % 19]);
		}  
	}; 
	public int images[] = new int[] {
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong1,R.mipmap.gundong2,
			R.mipmap.gundong3,R.mipmap.gundong4,R.mipmap.gundong5,
			R.mipmap.gundong6,R.mipmap.gundong7,R.mipmap.gundong8,
			R.mipmap.gundong9,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0};

	boolean isTasking =false;
	private boolean isNotFailue=false;//上传并未失败，只是显示只可晒近两周数据、同订单只可晒一单等信息的情况
	private String schemeId,schemeCost,won,orderTime;//订单id，方案金额，奖金,投注时间

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_shareorder_add);
		_this = this;
		screen_filePath=getIntent().getStringExtra("filePath");
		if (screen_filePath!=null) {
			file =new File(screen_filePath);
		}
		
		schemeId=getIntent().getStringExtra("schemeId");
		schemeCost=getIntent().getStringExtra("schemeCost");
		won=getIntent().getStringExtra("won");
		orderTime=getIntent().getStringExtra("orderTime");
		
		initViews();
	}



	private void initViews() {
		tv_head = (TopViewLeft) findViewById(R.id.title);
		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {
			@Override
			public void clickLoginView(View view) {
			}

			@Override
			public void clickContactView(View view) {
			}

			@Override
			public void clickBackImage(View view) {
				showExitDialog();
			}

		});


		tv_textSize = (TextView) findViewById(R.id.tv_textSize);
		edt_content = (ContainsEmojiEditText) findViewById(R.id.edt_content);
		btn_send = (Button) findViewById(R.id.btn_send);

		edt_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				tv_textSize.setText("还能输入" + (200 - s.length()) + "字");
			}
		});

		imageView = (ImageView) findViewById(R.id.iv_pic);
//		imageView.setImageDrawable(new BitmapDrawable(screen_filePath));
		xUtilsImageUtils.display(imageView, R.mipmap.ic_error,"file://"+screen_filePath);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 查看大图？
				imageBrower("file://"+screen_filePath);					
			}
		});
		btn_send.setOnClickListener(new OnClickListener() {// 发送

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				if(!isTasking){
					content = edt_content.getText().toString();
					if (content.replace("\n", "\b").length() > 0 && content.replace("\n", "\b").length() < 5) {// 有文字但不足5个字
						ToastUtils.showShort(_this, "晒单内容不少于5个字");
						return;
					}
					if (!isNotFailue) {
						failCount++;
					}
			
					if (failCount==3) {//提交次数超过3次
						showResultDialog(1);
					}
					Loading();
					isTasking = true;
					postImageView(file,content.replace("\n", "\b"));	
				}

			}
		});

		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		loading = (ImageView) findViewById(R.id.loading);
	}

	/**
	 * 上传图片
	 */
	public  synchronized void postImageView(File file,final String content){
		ServiceShareOrder.getInstance().postImageView(_this,file, new CallBack<String>() {

			@Override
			public void onSuccess(String t) {
				if (t!=null) {
					imgUrl = t;
				}else{
					imgUrl = "";
				}  
				submitContent(content);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				LoadingStop();
				imgUrl = "";
				isTasking = false;
				isNotFailue=false;
				ToastUtils.showShort(_this, "图片上传失败~"+errorMessage.msg);
			}
		});
	} 

	public void submitContent(String content ){
		Map<String, Object> ParamMap = new LinkedHashMap<String, Object>();
		ParamMap.put("content", content);//晒单内容	
		ParamMap.put("imageUrl", imgUrl);//晒单图片
		ParamMap.put("orderId", schemeId);//订单id
		ParamMap.put("orderMoney", schemeCost);//方案金额
		ParamMap.put("orderMoneyAt", won);//奖金
		ParamMap.put("orderTime", orderTime);//投注时间
		
		ServiceShareOrder.getInstance().PostAddShareOrder(_this, ParamMap , new CallBack<String>() {

			@Override
			public void onSuccess(String processID) {
				LoadingStop();
				if ("0".equals(processID)) {//晒单成功
					showResultDialog(0);		
				}else {//其他信息，如只可晒近两周数据
					isNotFailue=true;
				}
				
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				LoadingStop();
				isTasking = false;
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(String url) {
		Intent intent = new Intent(_this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URL, url);
		_this.startActivity(intent);
	}


	/**
	 * 返回时是否退出的提示
	 */
	private void showExitDialog() {
		if((_this).isFinishing()) {  
			return;
		} 

		if(alertDialog == null){
			alertDialog = new AlertDialog.Builder(this).create();
		}
		alertDialog.setCancelable(true);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView title = (TextView) window.findViewById(R.id.title);
		title.setText("温馨提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("退出此次编辑？");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("取消");
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("退出");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				_this.finish();
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
			/**
			 * @param v
			 */
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

	}

	/**
	 * 来源订单详情页  晒单成功弹窗 
	 * 或上传失败三次的提示
	 * @param  type 0:成功 1：失败
	 */
	private void showResultDialog(int type) {
		if((_this).isFinishing()) {  
			return;
		} 
		if(alertDialog==null){
			alertDialog = new AlertDialog.Builder(this).create();
		}
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView title = (TextView) window.findViewById(R.id.title);
		title.setText("温馨提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		if (type==0) {
			updatesize.setText("晒单成功后在“发现”-“晒单”-“我的”查看，审核通过后出现在晒单列表。可以在“我的”进行删除操作。");
		}else {
			updatesize.setText("网络不稳定，请稍后重试！");
		}

		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setVisibility(View.GONE);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("确定");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				_this.finish();
				isTasking = false;
			}
		});


	}	
	/**
	 * 获取InputMethodManager，隐藏软键盘
	 *
	 * @param token
	 */
	private void hideKeyboard(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	//=========输入框键盘 当点击空白处，键盘隐藏==========
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideKeyboard(v, ev)) {
				hideKeyboard(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideKeyboard(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = {0, 0};
			v.getLocationInWindow(l);
			int left = l[0],
					top = l[1],
					bottom = top + v.getHeight(),
					right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。

				return false;
			} else {
				v.clearFocus();
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			showExitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LoadingStop();
		if(alertDialog!=null){
			alertDialog.dismiss();
			alertDialog.cancel();
			alertDialog = null;
		}
		timer.cancel(); 
	}
	public void Loading(){
		if(!isShowing){
			isShowing = true;	
			load.setVisibility(View.VISIBLE);
			loading.setVisibility(View.VISIBLE);
			timer.scheduleAtFixedRate(new TimerTask()  
			{  
				@Override  
				public void run()  
				{  
					// TODO Auto-generated method stub  
					i++;  
					Message mesasge = new Message();  
					mesasge.what = i;  
					handler.sendMessage(mesasge);  
				}  
			}, 0, 100);  
		}
	}
	public void LoadingStop(){
		isShowing = false;		
		loading.setVisibility(View.GONE);
		load.setVisibility(View.GONE);
	}


}
