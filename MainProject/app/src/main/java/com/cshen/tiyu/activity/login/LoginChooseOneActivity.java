package com.cshen.tiyu.activity.login;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserResultData;
import com.cshen.tiyu.domain.login.UserTime;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;

import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;

public class LoginChooseOneActivity  extends BaseActivity {
	private LoginChooseOneActivity _this;
	private View mProgressView;
	private View mLoginFormView;
	private TextView warnview;
	private TopViewLeft tv_head=null;
	ListView refreshListView;
	ArrayList<UserTime> names ;
	MyListAdapter myListAdapter;
	String userPhone;
	AlertDialog  alertDialog ;
	private boolean isTasking = false;
	String requestName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_choose_one);
		_this = this;

		requestName=getIntent().getStringExtra("requestName");
		tv_head=(TopViewLeft) findViewById(R.id.tv_head);
		tv_head.setResourceVisiable(true,false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {		
			}
			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根
			}
			@Override
			public void clickBackImage(View view) {
				finish();
			}
		});
		initView();
		initdata(); // 获取数据
	}
	private void initView() {

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		warnview = (TextView) findViewById(R.id.warnview);
		// TODO 自动生成的方法存根
		refreshListView = (ListView) findViewById(R.id.rl_listview);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if(names!=null&&names.size()>position
						&&names.get(position)!=null){
					User chooseUser = names.get(position).getUser();
					dialog("登录确认",chooseUser.getUserName(),chooseUser.getUserPwd());
				}
			}
		});
	}
	@SuppressWarnings("unchecked")
	private void initdata() {
		Bundle b = getIntent().getExtras();
		names = (ArrayList<UserTime>)b.getSerializable("names");
		if(names!=null){
			myListAdapter = new MyListAdapter();// 最好传个参数过去 以后重构
			refreshListView.setAdapter(myListAdapter);
		}
		userPhone = b.getString("userPhone");
		String notice = getResources().getString(R.string.chooseone).replace("***********", userPhone);
		warnview.setText(Html.fromHtml(notice));
	}

	public void dialog(String info,final String userName,final String password) {
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText(info);
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("是否确认使用短信验证码登录账号"+userName+"?");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("确定");
		cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				alertDialog.cancel();
				attemptLogin(userName,password);
			}
		});
	}
	public void attemptLogin(final String username,final String password) {
		if (isTasking) {// 任务在运行中不再登入
			return;
		}
		showProgress(true);
		isTasking = true;
		ServiceUser.getInstance().PostLogin(_this,username, password,
				new CallBack<UserResultData>() {
			@Override
			public void onSuccess(UserResultData userResultData) {
				isTasking = false;// 任务标记访问结束后
				ToastUtils.showShort(_this, "登录成功！");
				showProgress(false);
				PreferenceUtil.putString(_this, "username",username);
				PreferenceUtil.putBoolean(_this, "hasLogin", true);
				PreferenceUtil.putBoolean(_this, "isExitWay", false);
				ConstantsBase.setUser(userResultData.user);				
				// 存放你已经登入的信息
				if (!TextUtils.isEmpty(requestName)) {
					Intent intent=new Intent();
					if("intentLogin".equals(requestName)){
						setResult(RESULT_OK, intent);
					}
				}else{					
					Intent intent = new Intent(_this,MainActivity.class);
					intent.putExtra("hasLogin", "yes");// 标记
					startActivity(intent);
				}				
				isTasking = false;
				finish();

			}

			@Override
			public void onFailure(ErrorMsg errorMsg) {
				PostHttpInfoUtils.doPostFail(_this, errorMsg, "登陆失败");
				showProgress(false);
				isTasking = false;
			}
		});
	}

	class MyListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return names.size();
		}

		@Override
		public UserTime getItem(int position) {
			return names.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			UserTime order = names.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.name_item,
						null);
				holder.head = (ImageView) convertView.findViewById(R.id.head);
				holder.name = (TextView) convertView.findViewById(R.id.nikename);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.setdefault = (TextView) convertView.findViewById(R.id.setdefault);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(order!=null){
				String time = order.getLastLoginTime();
				if(!TextUtils.isEmpty(time)){
					holder.time.setText(time);
				}else{
					holder.time.setText("暂无最后登录时间");
				}
				User use = order.getUser();
				String midName= order.getMidName();
				if(!TextUtils.isEmpty(midName)){
					midName = midName+"  ";
				}
				if(use != null){
					xUtilsImageUtils.display(holder.head, use.getUserPic(), R.mipmap.defaultniu);
					String userName = use.getUserName();
					if(!TextUtils.isEmpty(userName)){
						holder.name.setText(midName+userName);
						
					}else{
						holder.name.setText(midName+"暂无账户名");
					}
				}else{
					holder.name.setText(midName);
				}
				holder.setdefault.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}

				});
			}

			return convertView;

		}

		class ViewHolder {
			public TextView name;
			public TextView time;
			public ImageView head;
			public TextView setdefault;

		}
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mProgressView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}