package com.cshen.tiyu;



import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.xutils.x;

import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.information.InformationData;
import com.cshen.tiyu.domain.main.HomeAdsData;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.domain.main.TabIndicatorData;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceMain;
import com.cshen.tiyu.net.https.ServiceVersion;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.SilentDownHelp;
import com.cshen.tiyu.utils.StatusBarUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.zx.ZXMainActivity;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {
	public final String TABVERSION = "TabVersion";
	public final String LOTTERYTYPEVERSION = "LotteryTypeVersion";
	public final String LUNBOVERSION = "LunboVersion";
	public final String INFORMATIONVERSION = "InformationVersion";
	public final String PRELOADPAGEVERSION  = "PreLoadPageVersion";
	PreLoadPage exercisePage = null;//???????????????
	ImageView iv_exercise;
	ServiceVersion versionService;//????????????
	ServiceMain mainService;// ?????????????????????,??????????????????????????????
	private Handler mHandler = new Handler();// ???????????????????????????????????????chenggon
	boolean guideHas = false;//??????????????????
	private boolean isMaJia=false;//?????????????????????
	Map<String, String> mapMain;
	Handler mHandlerUpdate = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case ConstantsBase.FILEDOWNSTART:
				updatesize.setText("????????????...");
				pgsBar.setVisibility(View.VISIBLE);
				alertDialog.show();
				break;			
			case ConstantsBase.FILEDOWNNOW:
				int length = (Integer) ((Object[]) msg.obj)[0];
				int now = (Integer) ((Object[]) msg.obj)[1];
				update = "?????????" + (now * 100) / length + "%";
				updatesize.setText(update);
				pgsBar.setProgress((now * 100) / length );
				pgsBar.setVisibility(View.VISIBLE);
				alertDialog.show();
				break;
			case ConstantsBase.FILEDOWNOK:
				if(mapMain!=null){
					MyDbUtils.saveVersion(mapMain);
				}		
				pgsBar.setVisibility(View.GONE);
				alertDialog.dismiss();
				alertDialog.cancel();
				MyDbUtils.removeAll();
				PreferenceUtil.clearAll(SplashActivity.this);
				String filePath = String.valueOf(((Object[]) msg.obj)[0]);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				File file = new File(filePath);
				Uri uri = Uri.fromFile(file);
				intent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				startActivity(intent);
				break;
			case ConstantsBase.FILEDOWNERROR:
				alertDialog.dismiss();
				alertDialog.cancel();
				pgsBar.setVisibility(View.GONE);
				String errorMessage = String.valueOf(((Object[]) msg.obj)[0]);
				ToastUtils.showShort(SplashActivity.this, errorMessage);
				break;
			default:
				break;
			}

		};
	};
	AlertDialog  alertDialog;
	TextView  updatesize ;
	ProgressBar pgsBar;
	String update ;
	String VersionCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_splash);
		x.view().inject(this);
		initView();
		initSomeData();
		startAnim();
	}

	@Override
	public void setStatusBar() {
		super.setStatusBar();
		if (Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT !=  Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtil.setColor(this, getResources().getColor(R.color.fast3Status));//?????????
		}
	}

	private void initView() {
		// TODO ???????????????????????????
		if (!PreferenceUtil.getBoolean(this, "isNotNewUser")) {//????????????????????????????????????????????????guideactivity?????????????????????????????????????????????
			PreferenceUtil.putBoolean(this, "isNotNewUser", false);
		}

		iv_exercise = (ImageView) findViewById(R.id.iv_exercise);
		iv_exercise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ???????????????????????????
				// ??????html5???????????? ???????????????????????????
				if (exercisePage != null) {
					Intent intent = new Intent(SplashActivity.this,LotteryTypeActivity.class);
					intent.putExtra("url", exercisePage.getUrl());
					intent.putExtra("intentWay", "exercise");
					startActivity(intent);
					// ??????
					mHandler.removeCallbacks(mExerciseTimerTask);
					finish();
				}
			}
		});
	}

	// ?????????????????????
	private void initSomeData() {
		// TODO ???????????????????????????
		versionService = ServiceVersion.getInstance();
		mainService = ServiceMain.getInstance();
		//???????????????????????????????????????
		//getMarketFlag();
		isMaJia = false;
		PreferenceUtil.putBoolean(SplashActivity.this,"isMaJia",false);
		if (!PreferenceUtil.getBoolean(SplashActivity.this, "isNotNewUser")) {//????????????????????????????????????????????????guideactivity?????????????????????????????????????????????
			PreferenceUtil.putBoolean(SplashActivity.this, "isNotNewUser", false);
		}
		getVersionDatas(MyDbUtils.getVersion());
		/*isMaJia = true;
		guideHas=true;
		PreferenceUtil.putBoolean(SplashActivity.this,"isMaJia",true);
		initPage();*/
	}
	//???????????????????????????????????????
	private void getMarketFlag() {
		versionService.PostGetMarketFlag(this, new CallBack<String>() {

			@Override
			public void onSuccess(String struts) {
				if ("1".equals(struts)) {//???
					isMaJia = true;
					guideHas=true;
					PreferenceUtil.putBoolean(SplashActivity.this,"isMaJia",true);
					initPage();
				}else if ("0".equals(struts)) {//??????
					isMaJia = false;
					PreferenceUtil.putBoolean(SplashActivity.this,"isMaJia",false);
					if (!PreferenceUtil.getBoolean(SplashActivity.this, "isNotNewUser")) {//????????????????????????????????????????????????guideactivity?????????????????????????????????????????????
						PreferenceUtil.putBoolean(SplashActivity.this, "isNotNewUser", false);
					}
					getVersionDatas(MyDbUtils.getVersion());
				}

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				isMaJia = true;//?????????????????????????????????
				guideHas=true;
				PreferenceUtil.putBoolean(SplashActivity.this,"isMaJia",true);
				initPage();			
			}


		});
	}

	private void getVersionDatas(final Map<String, String> oldVersion ) {
		// ????????????????????????
		versionService.PostGetVersionDatas(this,new CallBack<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> map) {
				if(map!=null){
					// ???????????????????????????share?????????????????????????????????
					String appversion = map.get("appversion");
					try{
						int getVersion = Integer.valueOf(appversion);
						if(isNeedDownLoadVersion(getVersion)){
							dialog("??????????????????",oldVersion,map, 
									ConstantsBase.IP+"/download/CaiShen.apk");
						}else{
							toShow(oldVersion,map);
						}
					}catch(Exception e){
						e.printStackTrace();
						initPage();
					}
				}else{
					initPage();
				}
			}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub
				initPage();
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this,errorMessage.msg);

				}
			}
		});
	}
	public void toShow(Map<String, String> oldVersion,Map<String, String> map) {
		if (!isMaJia) {//????????????????????????????????????????????????
			getDate(oldVersion,map,PRELOADPAGEVERSION);	
			getDate(oldVersion,map,TABVERSION);
			getDate(oldVersion,map,LUNBOVERSION);	
			getDate(oldVersion,map,LOTTERYTYPEVERSION);	
		}
		MyDbUtils.saveVersion(map);	
	}
	private boolean isNeedDownLoadVersion(int getVersion){
		try {
			PackageManager manager = SplashActivity.this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(SplashActivity.this.getPackageName(), 0);
			int nowVersion = info.versionCode;
			if(getVersion>nowVersion){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void getDate(Map<String,String> oldVersion,Map<String,String> newVersion,String key){
		boolean isUpdate= false;
		if(oldVersion ==null||newVersion== null){
			isUpdate = true;
		}else{
			String oldValue = oldVersion.get(key);
			String newTValue = newVersion.get(key);
			if(TextUtils.isEmpty(oldValue)){
				isUpdate = true;
			}else if(TextUtils.isEmpty(newTValue)){
				isUpdate = true;
			}else if (!oldValue.equals(newTValue)) {
				isUpdate = true;
			} 
		}
		switch (key) {

		case PRELOADPAGEVERSION:
			if(isUpdate){
				getPreLoadPageDatas();//??????
			}else{
				initPage();
			}
			break;
		case TABVERSION:// ??????tab
			if(isUpdate){
				getTabIndicatorDatas();
			}
			break;
		case LOTTERYTYPEVERSION:// ???????????????????????????
			if(isUpdate){
				getLotteryTypeDatas();
			}
			break;
		case LUNBOVERSION:// ??????????????????
			if(isUpdate){
				getAdsDatas();
			}
			break;
		case INFORMATIONVERSION:// ???????????????????????????
			if(isUpdate){
				getInformationDatas();
			}
			break;
		}
	}
	private void getPreLoadPageDatas() {
		// TODO ???????????????????????????
		mainService.PostGetAdsData(this,new CallBack<PreLoadPageData>() {

			@Override
			public void onSuccess(PreLoadPageData t) {
				// TODO ??????????????????????????? 
				// ?????????????????????????????????
				if(t!=null){
					for(PreLoadPage pp:t.getPreLoadPages()){
						if(pp.getType() == 2){//?????????????????????
							guideHas = true;
							break;
						}
					}
				}
				initPage();
			}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO ???????????????????????????
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this, errorMessage.msg);
				}
				initPage();
			}
		});

	}
	private void getTabIndicatorDatas() {
		mainService.PostGetTabIndicatorDatas(this,new CallBack<TabIndicatorData>() {

			@Override
			public void onSuccess(TabIndicatorData t) {}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO ???????????????????????????
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this, errorMessage.msg);
				}
			}
		});
	}

	private void getAdsDatas() {
		mainService.PostGetHomeAdsData(this,new CallBack<HomeAdsData>() {
			@Override
			public void onSuccess(HomeAdsData t) {}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this, errorMessage.msg);
				}
			}
		});
	}
	private void getLotteryTypeDatas() {
		mainService.PostGetLotteryTypeDatas(this,new CallBack<LotteryTypeData>() {
			@Override
			public void onSuccess(LotteryTypeData t) {}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this, errorMessage.msg);
				}
			}
		});
	}
	protected void getInformationDatas() {
		// TODO ???????????????????????????
		mainService.PostGetInformationDatas(this,new CallBack<InformationData>() {
			@Override
			public void onSuccess(InformationData t) {}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO ???????????????????????????
				if(errorMessage!=null&&!TextUtils.isEmpty(errorMessage.msg)){
					ToastUtils.showShort(SplashActivity.this, errorMessage.msg);
				}
			}
		});
	}
	// ??????????????????
	//getType 0 ??????????????????1 ??????????????????2 ?????????
	public void initPage() {
		if (isMaJia) {//??????????????????
			initMaJiaPage();
			return;
		}
		iv_exercise.setImageResource(R.mipmap.guide);
		iv_exercise.startAnimation(startAnim());
		mHandler.postDelayed(mImageTimerTask, 2000);//2s??????????????????
		/*PreLoadPageData preLoadPageData = MyDbUtils.getPreLoadPageData();
		if (preLoadPageData != null) {
			PreLoadPage huanchunPage = null;
			ArrayList<PreLoadPage> preLoadPages = preLoadPageData.getPreLoadPages(); 
			if(preLoadPages != null){
				for (int i = 0; i < preLoadPages.size(); i++) {
					if (preLoadPages.get(i).getType() == 0) {
						huanchunPage = preLoadPages.get(i);
					}
					if (preLoadPages.get(i).getType() == 1) {
						exercisePage = preLoadPages.get(i);
					}
				}
			}
			if(huanchunPage != null&&!TextUtils.isEmpty(huanchunPage.getIcon())){
				xUtilsImageUtils.display(iv_exercise,R.mipmap.ic_error,huanchunPage.getIcon());
				iv_exercise.startAnimation(startAnim());
				mHandler.postDelayed(mImageTimerTask, 2000);//2s???????????????
			}else if (exercisePage != null&&!TextUtils.isEmpty(exercisePage.getIcon())) {
				xUtilsImageUtils.display(iv_exercise,R.mipmap.ic_error,exercisePage.getIcon());
				iv_exercise.startAnimation(startAnim());
				mHandler.postDelayed(mExerciseTimerTask, 2000);//2s????????????
			}else{
				xUtilsImageUtils.display(iv_exercise,R.mipmap.guide,"");
				iv_exercise.startAnimation(startAnim());
				mHandler.postDelayed(mExerciseTimerTask, 2000);//2s????????????
			}

		}*/
	}
	//?????????splash??????
	public void initMaJiaPage(){
		iv_exercise.setImageResource(R.mipmap.zx_splash);
		iv_exercise.startAnimation(startAnim());
		mHandler.postDelayed(mImageTimerTask, 2000);//2s??????????????????
	}
	/**
	 * ?????????????????????
	 */

	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			jumpNextPage();//???????????????
		}
	};
	protected void jumpNextPage() {
		if (exercisePage != null) {
			xUtilsImageUtils.display(iv_exercise,R.mipmap.ic_error,exercisePage.getIcon());
			mHandler.postDelayed(mExerciseTimerTask, 2000);
		}else{
			jumpNextActivity();
		}
	}
	private Runnable mExerciseTimerTask = new Runnable() {
		@Override
		public void run() {
			jumpNextActivity();
		}
	};
	protected void jumpNextActivity() {
		if(!PreferenceUtil.getBoolean(SplashActivity.this,"is_user_guide_showed")&&guideHas){
			PreferenceUtil.putBoolean(SplashActivity.this,"is_user_guide_showed",true);
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		}else{
			if (isMaJia) {//?????????
				startActivity(new Intent(SplashActivity.this, ZXMainActivity.class));
			}else {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
			}

		}
		finish();
	}

	public void dialog(String info,final Map<String, String> oldVersion, final Map<String, String> map,final String urlString) {

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCancelable(false);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.updatedialog);
		pgsBar = (ProgressBar) window.findViewById(R.id.pgsBar);
		updatesize = (TextView) window.findViewById(R.id.updatesize);
		if(map.get("moduleName")!=null&&map.get("moduleName").contains("#")){
			updatesize.setText(map.get("moduleName").replace("#", "\n"));
		}
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				SilentDownHelp.getInstance(true,DirsUtil.getSD_DOWNLOADS())
				.addDownFile(0l, urlString,mHandlerUpdate, false,SplashActivity.this);
			}
		});
		
		TextView cancel = (TextView) window.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				toShow(oldVersion,map);
			}
		});
	}


	private AnimationSet startAnim() {
		AnimationSet set = new AnimationSet(false);
		// ????????????
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(1000);// ????????????
		scale.setFillAfter(true);// ??????????????????
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		return set;
	}
	/*public static boolean checkPermission(Context context, String permission) {
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Class<?> clazz = Class.forName("android.content.Context");
				Method method = clazz.getMethod("checkSelfPermission", String.class);
				int rest = (Integer) method.invoke(context, permission);
				if (rest == PackageManager.PERMISSION_GRANTED) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
		} else {
			PackageManager pm = context.getPackageManager();
			if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				result = true;
			}
		}
		return result;
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = null;
			if (checkPermission(context, permission.READ_PHONE_STATE)) {
				device_id = tm.getDeviceId();
			}
			String mac = null;
			FileReader fstream = null;
			try {
				fstream = new FileReader("/sys/class/net/wlan0/address");
			} catch (FileNotFoundException e) {
				fstream = new FileReader("/sys/class/net/eth0/address");
			}
			BufferedReader in = null;
			if (fstream != null) {
				try {
					in = new BufferedReader(fstream, 1024);
					mac = in.readLine();
				} catch (IOException e) {
				} finally {
					if (fstream != null) {
						try {
							fstream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
}
