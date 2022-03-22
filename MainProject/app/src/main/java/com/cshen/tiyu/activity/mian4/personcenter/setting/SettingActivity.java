package com.cshen.tiyu.activity.mian4.personcenter.setting;


import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.R;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.Attachment;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceVersion;
import com.cshen.tiyu.net.https.xUtilsImageUtils;

import com.cshen.tiyu.utils.BitmapUtil;
import com.cshen.tiyu.utils.DataCleanManager;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.ImageZoom;
import com.cshen.tiyu.utils.NetWorkUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SilentDownHelp;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.SafeSettingItemView;


import de.greenrobot.event.EventBus;
public class SettingActivity extends BaseActivity  implements OnClickListener{
	private SettingActivity _this;
	private static int UPDATE = 1;
	private View headview;//修改头像
	private ImageView head; 
	private View changename;//修改昵称
	private TextView nowname;
	private SafeSettingItemView sendMessage;//推送设置
	private SafeSettingItemView clear;//清除缓存
	private SafeSettingItemView checkVersion;//检查更新
	private SafeSettingItemView aboutUs;//关于我们
	private SafeSettingItemView feedback;//用户反馈
	private SafeSettingItemView share;//推荐给好友

	private View btn_exit;
	private boolean canChangeName;
	private String nickName,userName;

	private AlertDialog  alertDialog ;
	private TextView  updatesize ;
	private String update ;
	public int images[] = new int[] {
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong1,R.mipmap.gundong2,
			R.mipmap.gundong3,R.mipmap.gundong4,R.mipmap.gundong5,
			R.mipmap.gundong6,R.mipmap.gundong7,R.mipmap.gundong8,
			R.mipmap.gundong9,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0,
			R.mipmap.gundong0,R.mipmap.gundong0};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case ConstantsBase.FILEDOWNSTART:
				updatesize.setText("开始下载...");
				pgsBar.setVisibility(View.VISIBLE);
				alertDialog.show();
				break;			
			case ConstantsBase.FILEDOWNNOW:
				int length = (Integer) ((Object[]) msg.obj)[0];
				int now = (Integer) ((Object[]) msg.obj)[1];
				update = "已下载" + (now * 100) / length + "%";
				updatesize.setText(update);
				pgsBar.setProgress((now * 100) / length );
				pgsBar.setVisibility(View.VISIBLE);
				alertDialog.show();
				break;
			case ConstantsBase.FILEDOWNOK:
				try{
					pgsBar.setVisibility(View.GONE);
					alertDialog.dismiss();
					alertDialog.cancel();
					String filePath = String.valueOf(((Object[]) msg.obj)[0]);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					File file = new File(filePath);
					Uri uri = Uri.fromFile(file);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");
					startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					MyDbUtils.removeAll();
					PreferenceUtil.clearAll(SettingActivity.this);
				}
				break;
			case ConstantsBase.FILEDOWNERROR:
				alertDialog.dismiss();
				alertDialog.cancel();
				pgsBar.setVisibility(View.GONE);
				String errorMessage = String.valueOf(((Object[]) msg.obj)[0]);
				ToastUtils.showShort(SettingActivity.this, errorMessage);
				break;
			default:
				break;
			}

		};
	};

	// 用于云朵的动画效果
	private View load;
	private ImageView loading;
	//private boolean isShowing = false;
	private int i = 0;  
	Timer timer;  
	private Handler handler = new Handler()  
	{  
		@Override  
		public void handleMessage(Message msg)  
		{  

			super.handleMessage(msg); 
			loading.setBackgroundResource(images[i % 19]);
		}  
	};  
	private PopupWindow pop;//照片弹出框
	String showimageFileName,strImgPath;//拍照名字和路径

	private ProgressBar pgsBar;
	private boolean hasPayPassword,hasBindMobile, hasRealName, hasBindBankCard;
	private ImageView iv_win_notice;//中奖通知开关
	private PopupWindow sharePop;
	private ProgressDialog mDialog;
	private boolean isChanging = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		_this = this;
		changHead();
		initView();
		setUserPic();
	}
	private void initView() {

		btn_exit =  findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		headview = findViewById(R.id.headview);
		headview.setOnClickListener(this);
		head = (ImageView)findViewById(R.id.head);
		changename = findViewById(R.id.changename);
		changename.setOnClickListener(this);
		nowname = (TextView)findViewById(R.id.nowname);
		iv_win_notice=(ImageView) findViewById(R.id.iv_win_notice);
		iv_win_notice.setOnClickListener(this);
		if (PreferenceUtil.getBoolean(_this, "isOpenNotice")) {//开关开状态
			iv_win_notice.setImageResource(R.mipmap.toggle_on);
		}else {//开关关的状态
			iv_win_notice.setImageResource(R.mipmap.toggle_0ff);
		}
		/*safeset = (SafeSettingItemView)findViewById(R.id.safeset);
		safeset.setOnClickListener(this);*/

		sendMessage = (SafeSettingItemView) findViewById(R.id.sendmessage);
		sendMessage.setOnClickListener(this);

		clear = (SafeSettingItemView) findViewById(R.id.clear);
		try {
			clear.setResource(false,DataCleanManager.getTotalCacheSize(_this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clear.setOnClickListener(this);

		checkVersion = (SafeSettingItemView) findViewById(R.id.checkversion);
		checkVersion.setOnClickListener(this);

		aboutUs = (SafeSettingItemView) findViewById(R.id.aboutus);
		aboutUs.setOnClickListener(this);

		feedback = (SafeSettingItemView) findViewById(R.id.feedback);
		feedback.setOnClickListener(this);

		share = (SafeSettingItemView) findViewById(R.id.share);
		share.setOnClickListener(this);

		load = findViewById(R.id.load);
		load.setAlpha(0.7f);
		loading = (ImageView) findViewById(R.id.loading);
	}

	private void initShareInfo() {
		nickName  = MyDbUtils.getCurrentUser().getUsernickName();
		userName  = PreferenceUtil.getString(_this, "username");
		if(TextUtils.isEmpty(nickName)//昵称为空或者跟用户名一致时可以修改
				||nickName.equals(userName)){
			canChangeName = true;
		}else{
			canChangeName = false;
		}
		if(!TextUtils.isEmpty(nickName)){
			nowname.setText(nickName);
		}else{
			if(!TextUtils.isEmpty(userName)){
				if (!Util.isMobileValid(userName)||userName.length()!=11) {
					nowname.setText(userName);
				}else{
					nowname.setText(userName.substring(0, 7)+"****");
				}
			}else{
				nowname.setText("暂无昵称");
			}
		}
	}

	private void setUserPic(){
		if(MyDbUtils.getCurrentUser()!=null){
			String url = MyDbUtils.getCurrentUser().getUserPic();
			xUtilsImageUtils.display(head, url, R.mipmap.defaultniu);
		}
		head.invalidate();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {	
		case R.id.headview:
			if(!NetWorkUtil.isNetworkAvailable(_this)){
				ToastUtils.showShort(_this,"当前网络信号较差，请检查网络设置");				
			}else{
				if (pop == null) {
					popwindows();
				}
				if (pop.isShowing()) {
					pop.dismiss();
					load.setVisibility(View.GONE);
				} else {
					pop.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
					load.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.changename:
			if(canChangeName){
				startActivityForResult(new Intent(_this,
						ChangeNameActivity.class), UPDATE);
			}else{
				ToastUtils.showShort(_this, "昵称已修改过，不能再次修改！");
			}
			break;
		case R.id.share:
			if (sharePop == null) {
				sharePopwindows();
			}
			if (sharePop.isShowing()) {
				sharePop.dismiss();
				load.setVisibility(View.GONE);
			} else {
				sharePop.showAtLocation(findViewById(R.id.headview), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
				load.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.sendmessage://推送设置
			startActivity(new Intent(_this, MessageSendSettingActivity.class));
			break;
		case R.id.iv_win_notice://中奖通知
			if (isChanging) {
				return ;
			}
			if (PreferenceUtil.getBoolean(_this, "isOpenNotice")) {//开=》关
				changeNoticeFlag(0,false);
			}else {
				changeNoticeFlag(1,true);
			}
			break;
		case R.id.clear:
			ToastUtils.showShort(_this,"正在清理缓存...");
			DataCleanManager.clearAllCache(_this);
			try {
				clear.setResource(false,DataCleanManager.getTotalCacheSize(_this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.checkversion:
			toCheckVersion();
			break;
		case R.id.aboutus:
			startActivity(new Intent(_this, AboutUsActivity.class));
			break;
		case R.id.feedback:
			//FeedbackAPI.openFeedbackActivity();
			break;	
		case R.id.btn_exit:

			new com.cshen.tiyu.widget.AlertDialog(SettingActivity.this).builder()
					.setTitle("温馨提示").setMsg("确认退出登录？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							DataCleanManager.cleanCustomCache(ConstantsBase.WEBVIEW_CACHE_PATH);
							PreferenceUtil.putBoolean(_this, "hasLogin", false);
							PreferenceUtil.putBoolean(_this, "isExitWay", true);
							Intent intent = new Intent(_this,MainActivity.class);
							PreferenceUtil.putBoolean(_this, "hasPayPassword", false);
							intent.putExtra("hasLogin", "cancel");// 标记
							// 为了让主activity根据此标记选中相应的fragment
							startActivity(intent);
							ToastUtils.showShort(_this, "退出登录！");
							Clear();
							finish();
							EventBus.getDefault().post("clearSign");// gengxingqichi
						}
					}).setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			}).show();


			break;
		}
	}


	private void changHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_head_title);
		tvTextView.setText("设置");
		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(SettingActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}
	public void popwindows() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.headview, null);
		View photoCamera = view.findViewById(R.id.btn_take_photo);
		View album = view.findViewById(R.id.btn_pick_photo);
		View cancle = view.findViewById(R.id.btn_cancel);
		photoCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openPhotoCamera();
				pop.dismiss();
			}
		});
		album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				album();
				pop.dismiss();
			}
		});
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
		// 创建PopupWindow对象
		pop = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);

		pop.setOnDismissListener(new poponDismissListener());
	}
	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			load.setVisibility(View.GONE);
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ConstantsBase.REQUEST_CODE_CHOOSE_PHOTO:// 本地选择
				if (data.getData() != null) {
					Uri selectedImageBenDi = data.getData();
					String lujinBenDi = "";
					//根据返回的URI获取对应的SQLite信息
					Cursor cursorBenDi = this.getContentResolver().query(selectedImageBenDi, null,
							null, null, null);
					if (cursorBenDi != null && cursorBenDi.moveToFirst()) {
						try {
							lujinBenDi = cursorBenDi.getString(cursorBenDi.getColumnIndex("_data"));// 获取绝对路径
							cursorBenDi.close();
						} catch (Exception e) {
							e.printStackTrace();
							lujinBenDi = selectedImageBenDi.getPath();
						}
					} else {
						lujinBenDi = selectedImageBenDi.getPath();
					}
					lujinBenDi = toZoom(lujinBenDi);

					File fileImageBenDi = new File(lujinBenDi);

					head.setImageDrawable(new BitmapDrawable(BitmapUtil.setImageOptions(lujinBenDi)));
					UUID uuid1 = UUID.randomUUID();
					String[] names = lujinBenDi.split("\\/");
					String name = lujinBenDi;
					if(names.length>1){
						name = names[names.length-1];
					}
					Attachment attachment = new Attachment();
					attachment.setAttachmentId(uuid1.randomUUID().toString()+"");
					attachment.setAttachmentName(name);
					attachment.setAttchmentAllLength(fileImageBenDi.length());
					uploadPic(fileImageBenDi,attachment,lujinBenDi);
				} else {
					ToastUtils.showShort(this, "文件不存在，请重新选择！");
				}
				break;
			case ConstantsBase.REQUEST_CODE_TAKE_PHOTO:// 拍照
				strImgPath = toZoom(strImgPath + showimageFileName);
				File fileImageBenDi = new File(strImgPath);

				head.setImageDrawable(new BitmapDrawable(BitmapUtil.setImageOptions(strImgPath)));

				UUID uuid1 = UUID.randomUUID();
				Attachment attachment = new Attachment();
				attachment.setAttachmentId(uuid1.randomUUID().toString()+"");
				attachment.setAttachmentName(showimageFileName);
				attachment.setAttchmentAllLength(fileImageBenDi.length());
				uploadPic(fileImageBenDi,attachment,strImgPath);
				break;
			default:
				break;
			}
		}

	}
	/**
	 * 相册选择照片
	 */
	protected void album() {
		int permission = ActivityCompat.checkSelfPermission(_this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(_this,  ConstantsBase.PERMISSIONS_STORAGE, ConstantsBase.REQUEST_EXTERNAL_STORAGE);
		}
		// 打开本地相册
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ConstantsBase.REQUEST_CODE_CHOOSE_PHOTO);
	}
	/**
	 * 拍摄照片
	 */
	private void openPhotoCamera() {
		try {
			String nativetime = DateUtils.getNowDate("yyyyMMddHHmmssSSS");
			showimageFileName = nativetime + ".jpg";
			Intent intentPhone = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			strImgPath = DirsUtil.getSD_PHOTOS() + "/";// 存放照片的文件夹
			File file = new File(strImgPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(strImgPath, showimageFileName);
			Uri uri = Uri.fromFile(file);
			intentPhone.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intentPhone.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(intentPhone, ConstantsBase.REQUEST_CODE_TAKE_PHOTO);
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.showShort(_this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。");
		}
	}

	private String toZoom(String  fileAllPath) {
		String[] suffixs = fileAllPath.split("\\.");//用.来区分获取最后的后缀名
		if(suffixs.length<=1){//没有后缀名，直接上传；
			return fileAllPath;
		}
		String suffix = suffixs[suffixs.length-1];//有后缀名获取后缀名，如.jpg
		String[] names= fileAllPath.split("\\/");//用/来区分获取名字
		if(names.length<=0){//没有名字，直接上传；
			return fileAllPath;
		}
		String namesuffix = names[names.length-1];//有名字获取名字带后缀名
		String name = namesuffix.substring(0, namesuffix.length()-suffix.length()-1);//有名字获取名字
		int  length = fileAllPath.length()-namesuffix.length();//名字之前路径的长度
		if(length<0){
			return fileAllPath;
		}
		StringBuffer small = new StringBuffer();
		small = small.append(fileAllPath.substring(0,length)).append(name.replace(".", "")).append("_Small.").append(suffix);

		boolean isZoom = false;
		if (null != fileAllPath) {
			isZoom = new ImageZoom().imageZoom(fileAllPath,small.toString());// 照片压缩
		}
		if (isZoom) {
			fileAllPath = small.toString();						
		}
		return fileAllPath;
	}
	private void uploadPic(File fileImageBenDi,Attachment attachment,final String result) {
		if(!fileImageBenDi.exists()){
			ToastUtils.showShort(_this,"您上传的图片已被删除");
			return;
		}
		ServiceVersion.getInstance().uploadMethod(_this,fileImageBenDi,attachment,
				new CallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO 自动生成的方法存根
				User user =  MyDbUtils.getCurrentUser();
				user.setUserPic(t);
				MyDbUtils.saveUser(user);
				EventBus.getDefault().post("updateUserPic");
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg);
			}
		});
	}
	public void Clear(){
		MyDbUtils.removeLoginAll();
		PreferenceUtil.clearLoginAll(this);
	}
	public void toCheckVersion(){
		Loading();
		ServiceVersion.getInstance().PostGetVersionDatas(_this,new CallBack<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> map) {	
				LoadingStop();
				String appversion = map.get("appversion");
				try{
					int getVersion = Integer.valueOf(appversion);
					if(isNeedDownLoadVersion(getVersion)){
						dialog("有新版本更新",
								map.get("moduleName"), 
								ConstantsBase.IP+"/download/CaiPiaoM.apk");
					}
					else{
						ToastUtils.showShort(_this, "您已经更新至最新版本");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				LoadingStop();	
				ToastUtils.showShort(_this, errorMessage.msg);
				Log.e("errorMessage",errorMessage.msg);
			}
		});
	} 
	public void sharePopwindows() {

		LayoutInflater inflater = LayoutInflater.from(_this);
		// 引入窗口配置文件
		View viewPop = inflater.inflate(R.layout.shareview, null);

		View weixin = viewPop.findViewById(R.id.weixin);
		weixin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//shareToThird(SHARE_MEDIA.WEIXIN);
			}
		});
		View weixinpyq = viewPop.findViewById(R.id.pyq);
		weixinpyq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//shareToThird(SHARE_MEDIA.WEIXIN_CIRCLE);
			}
		});
		View qq = viewPop.findViewById(R.id.qq);
		qq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//shareToThird(SHARE_MEDIA.QQ);
			}
		});
		View qqzone = viewPop.findViewById(R.id.qqzone);
		qqzone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//shareToThird(SHARE_MEDIA.QZONE);
			}
		});
		View cancel = viewPop.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sharePop.dismiss();
			}
		});

		// 创建PopupWindow对象
		sharePop = new PopupWindow(viewPop, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		sharePop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		sharePop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		sharePop.setFocusable(true);
		sharePop.setOnDismissListener(new poponDismissListener());
	}	
	public void Loading(){
		load.setVisibility(View.VISIBLE);
		loading.setVisibility(View.VISIBLE);
		if(timer == null){
			timer = new Timer();
		}
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
	public void LoadingStop(){
		load.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
		if(timer!=null){
			timer.cancel(); 
			timer = null;
		}
	}



	public void dialog(String info,String versionContent, final String urlString) {

		alertDialog = new AlertDialog.Builder(_this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.updatedialog);
		/*TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText(info);*/
		pgsBar = (ProgressBar) window.findViewById(R.id.pgsBar);
		updatesize = (TextView) window.findViewById(R.id.updatesize);
		if(versionContent!=null&&versionContent.contains("#")){
			updatesize.setText(versionContent.replace("#", "\n"));
		}
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				SilentDownHelp.getInstance(true,DirsUtil.getSD_DOWNLOADS())
				.addDownFile(0l,urlString,mHandler,false, _this);
			}
		});
	}

	private void changeNoticeFlag(int flag,final boolean toOpen){
		ServiceUser.getInstance().getChangeNoticeFlag(_this, flag, new CallBack<String>() {

			@Override
			public void onSuccess(String processId) {
				
				if (toOpen) {//设置为开状态
					PreferenceUtil.putBoolean(_this, "isOpenNotice", true);
					iv_win_notice.setImageResource(R.mipmap.toggle_on);
				}else {
					PreferenceUtil.putBoolean(_this, "isOpenNotice", false);
					iv_win_notice.setImageResource(R.mipmap.toggle_0ff);
				}
				isChanging = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				ToastUtils.showShort(_this, "修改开关状态失败："+errorMessage.msg);
				isChanging = false;
			}
		});
	}
	private boolean isNeedDownLoadVersion(int getVersion){
		try {
			PackageManager manager = _this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(_this.getPackageName(), 0);
			int nowVersion = info.versionCode;
			if(getVersion>nowVersion){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LoadingStop();
		if(mDialog!=null){
			mDialog.dismiss();
			mDialog = null;
		}
		if(timer!=null){
			timer.cancel(); 
			timer = null;
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if(mDialog!=null){
			mDialog.dismiss();
			mDialog = null;
		}
		super.onPause();
	}
}  