package com.cshen.tiyu.activity.mian4.gendan.niuren;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.gendan.RenZhengUtils;
import com.cshen.tiyu.activity.pay.PayActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.base.MyFragmentPagerAdapter;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Back;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.MyFollowRecommend;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenDetil;
import com.cshen.tiyu.domain.gendan.NiuRenList;
import com.cshen.tiyu.domain.gendan.RenZhengMatch;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;
import com.cshen.tiyu.activity.mian4.gendan.niuren.GenDanFragment.CallBackValue;


import de.greenrobot.event.EventBus;

public class NiuRenActivity extends BaseActivity implements OnClickListener,CallBackValue{
	public static final int HAVEPAY = 0;
	public static final int LOGIN = 3;//登录获取信息
	public static final int LOGIN_PRISE = 5;// 登录//晒单点赞
	NiuRenActivity _this;
	private TextView guanzhu;
	TextView name,fensis,guanzhus;
	private ImageView head,v;

	private ViewPager mPager;//对应的fragment
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	public GenDanFragment gendanFragment;
	public ShaiDanFragment shaidanFragment;
	private TextView gendan,shaidan;
	private TextView ivBottomLine;
	private int currIndex = 0;
	private int position_one;

	String id;
	boolean hasFouce = false;
	private boolean isFoucing= false;
	private String userId ="",userPwd ="";
	String  eventType;
	private NiuRen niuren;
	Bundle savedInstanceState;
	private int arg0;//晒单页点击时，直接跳到晒单栏
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		_this = this;
		setContentView(R.layout.niurendetil);
		arg0=getIntent().getIntExtra("arg0", 0);
		
		initView(); 
		if(arg0!=0){
			//setBase(arg0);
			mPager.setCurrentItem(arg0);
		}
		initData();
	}
	public void initView(){
		findViewById(R.id.iv_back).setOnClickListener(this);

		head = (ImageView)findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		v = (ImageView) findViewById(R.id.v);
		fensis = (TextView) findViewById(R.id.fensis); 
		guanzhus = (TextView) findViewById(R.id.guanzhus); 
		guanzhu = (TextView) findViewById(R.id.guanzhu);
		guanzhu.setOnClickListener(this);

		InitWidth();
		gendan = (TextView)findViewById(R.id.gendan);
		shaidan = (TextView) findViewById(R.id.shaidan);

		gendan.setOnClickListener(new MyOnClickListener(0));
		shaidan.setOnClickListener(new MyOnClickListener(1));
		mPager = (ViewPager)findViewById(R.id.pager);
		setFragment();//设置订单数据
	}
	public void initData(){
		id = getIntent().getStringExtra("id");
		Bundle bundle = new Bundle();
		bundle.putString("id",id);//这里的values就是我们要传的值
		gendanFragment.setArguments(bundle);
		shaidanFragment.setArguments(bundle);
		if (PreferenceUtil.getBoolean(_this,"hasLogin")&&
				 MyDbUtils.getCurrentUser()!=null) {
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		}
	}
	private void InitWidth() {
		ivBottomLine = (TextView) findViewById(R.id.iv_bottom_line1);
		DisplayMetrics dm = new DisplayMetrics();
		_this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;//屏幕宽度
		position_one = (int) (screenW / 2.0);
	}
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	private void setFragment(){
		if(savedInstanceState != null){		
			gendanFragment = (GenDanFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "gendanFragment");
			if(gendanFragment == null){
				gendanFragment = new GenDanFragment();
			}

			shaidanFragment = (ShaiDanFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "shaidanFragment");
			if(shaidanFragment == null){
				shaidanFragment = new ShaiDanFragment();
			}

		}else{
			gendanFragment = new GenDanFragment();
			shaidanFragment = new ShaiDanFragment();
		}

		fragments.add(gendanFragment);
		fragments.add(shaidanFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					shaidan.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}     
				gendan.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					gendan.setTextColor(_this.getResources().getColor(R.color.dingdanuncolor));
				}
				shaidan.setTextColor(_this.getResources().getColor(R.color.mainred));
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (gendanFragment != null
				&&gendanFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "gendanFragment", gendanFragment);
		}
		if (shaidanFragment != null
				&&shaidanFragment.isVisible()) {
			getSupportFragmentManager().putFragment(savedInstanceState, "shaidanFragment", shaidanFragment);
		}
	}
	public void setFouce(){
		if(hasFouce){
			eventType = "CANCEL_CONCERN";
		}else{
			eventType = "CONCERN";
		}
		ServiceGenDan.getInstance().setFouce(_this,niuren!=null?niuren.getId():"",userId,userPwd,eventType,
				new CallBack<Back>() {
			@Override
			public void onSuccess(Back t) {
				// TODO 自动生成的方法存根
				if(hasFouce){
					hasFouce = false;
					ToastUtils.showShort(_this, "取消关注");
				}else{
					hasFouce = true;
					ToastUtils.showShort(_this, "关注成功");
				}
				setFouceData();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				isFoucing = false;
			}
		});

	}
	public void setBaseData(NiuRen niurenFromFG){
		niuren = niurenFromFG;
		if(userId.equals(niuren.getId())){
			guanzhu.setVisibility(View.GONE);
		}else{
			guanzhu.setVisibility(View.VISIBLE);
		}
		if(niuren.isConcern()){
			hasFouce = true;
		}else{
			hasFouce = false;
		}
		setFouceData();
		xUtilsImageUtils.display(head,R.mipmap.defaultniu,
				niuren.getUserPic());
		name.setText(niuren.getUserNameTemp());
		String level = niuren.getUserLevelNew();
		if(TextUtils.isEmpty(level)){
			v.setVisibility(View.INVISIBLE);
		}else{
			if(RenZhengUtils.getRenZhengUtils().getV(level)!=0){
				v.setImageResource(RenZhengUtils.getRenZhengUtils().getV(level));
				v.setVisibility(View.VISIBLE);
			}else{
				v.setVisibility(View.INVISIBLE);
			}
		}
		fensis.setText(niuren.getConcernedNumber()+"");
		guanzhus.setText(niuren.getConcernNumber()+"");		
	}
	public void setFouceData(){
		int right = guanzhu.getPaddingRight();  
		int top = guanzhu.getPaddingTop();  
		int bottom = guanzhu.getPaddingBottom();  
		int left = guanzhu.getPaddingLeft();
		if(!hasFouce){ 
			guanzhu.setText("+ 关注");
			guanzhu.setTextColor(getResources().getColor(R.color.mainred));
			guanzhu.setBackgroundResource(R.drawable.cornerfulllinefull_redline_nofull);
		}else{
			guanzhu.setText("已关注");
			guanzhu.setTextColor(Color.parseColor("#ffffff"));
			guanzhu.setBackgroundResource(R.drawable.cornerfullred);
		}
		guanzhu.setPadding(left,top,right,bottom);  
		isFoucing = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN:	
				EventBus.getDefault().post("getDataGenDan");
				EventBus.getDefault().post("getDataShaiDan");
				break;	
			case HAVEPAY:	
				//setFouce();
				break;
			case LOGIN_PRISE:
				EventBus.getDefault().post("getDataShaiDan");
				break;
			default:
				break;
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			_this.finish();
			break;
		case R.id.guanzhu:
			if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
				Intent intentLogin = new Intent(_this, LoginActivity.class); 
				intentLogin.putExtra("requestName", "intentLogin");
				startActivityForResult(intentLogin,LOGIN);
			}else{
				if(!isFoucing){
					isFoucing = true;
					setFouce();
				}
			}
			break;

		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(gendanFragment!=null&&gendanFragment.timekeybroad.getVisibility() == View.VISIBLE){
				gendanFragment.timekeybroad.setVisibility(View.GONE);
				return true;
			}else{
				_this.finish();
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void SendMessageValue(NiuRen strValue) {
		// TODO Auto-generated method stub
		setBaseData(strValue);
	}
	
}