package com.cshen.tiyu.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import com.cshen.tiyu.base.CaiPiaoApplication;
import com.cshen.tiyu.domain.information.Information;
import com.cshen.tiyu.domain.information.InformationData;
import com.cshen.tiyu.domain.login.User;
import com.cshen.tiyu.domain.login.UserInfo;
import com.cshen.tiyu.domain.login.Version;
import com.cshen.tiyu.domain.main.HomeAdsData;
import com.cshen.tiyu.domain.main.HomeAdsInfo;
import com.cshen.tiyu.domain.main.LotteryType;
import com.cshen.tiyu.domain.main.LotteryTypeData;
import com.cshen.tiyu.domain.main.Message;
import com.cshen.tiyu.domain.main.NewsBean.NewslistBean;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.domain.main.TabIndicator;
import com.cshen.tiyu.domain.main.TabIndicatorData;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ScreenShotsUtils;


import android.content.Context;
import android.text.TextUtils;

public class MyDbUtils {
	private MyDbUtils() {}

	public static void removeAll() {
		removeLoginAll();
		removeAllCurrentPreLoadPageData();//引导
		removeAllCurrentHomeAdsData();//轮播
		removeAllCurrentInformationData();//信息  
		removeAllCurrentLotteryTypeData();//彩种
		removeAllCurrentTabIndicatorData();//底部tab
        removeAllCurrentHistoryData();//马甲包浏览记录

	}
	public static void removeLoginAll() {
		removeAllUser();
		removeAllUserInfo();
		ScreenShotsUtils.deleteFilesByDirectory(DirsUtil.getSD_PHOTOS());
		//ScreenShotsUtils.deleteFilesByDirectory(DirsUtil.getSD_DOWNLOADS());
	}
	/*****************PreLoadPage.class*********************/
	// 获取预加载页面的信息
	public static PreLoadPageData getPreLoadPageData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		PreLoadPageData preLoadPageData = new PreLoadPageData();
		ArrayList<PreLoadPage> preLoadPages = null;
		try {
			preLoadPages = (ArrayList<PreLoadPage>) manager.findAll(PreLoadPage.class);
			preLoadPageData.setPreLoadPages(preLoadPages);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return preLoadPageData;
	}
	// 保存预加载页面数据
	public static void savePreLoadPageData(PreLoadPageData preLoadPageData) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<PreLoadPage> preLoadPages = null;
		try {
			preLoadPages = preLoadPageData.getPreLoadPages();
			manager.dropTable(PreLoadPage.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(preLoadPages);// 添加预加载数据集合数据

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeAllCurrentPreLoadPageData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.dropTable(PreLoadPage.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/*****************Information.class*********************/
	// 保存资讯的信息
	public static void saveInformationData(InformationData informationData) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<Information> informations = null;
		try {
			informations = informationData.getInfoList();
			manager.dropTable(Information.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(informations);

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	// 获取资讯的信息
	public static InformationData getInformationData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		InformationData informationData = new InformationData();
		ArrayList<Information> informations = null;
		try {
			informations = (ArrayList<Information>) manager.findAll(Information.class);
			informationData.setInfoList(informations);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return informationData;

	}
	public static void removeAllCurrentInformationData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(Information.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}


	/*****************HomeAdsInfo.class*********************/
	// 保存轮播图的信息
	public static void saveHomeAdsData(HomeAdsData homeAdsData) {

		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<HomeAdsInfo> homeAdsInfos = null;
		try {
			homeAdsInfos = homeAdsData.getAdsList();
			manager.dropTable(HomeAdsInfo.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(homeAdsInfos);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	// 获取轮播图的信息
	public static HomeAdsData getHomeAdsData() {

		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		HomeAdsData homeAdsData = new HomeAdsData();
		ArrayList<HomeAdsInfo> homeAdsInfos = null;
		try {
			homeAdsInfos = (ArrayList<HomeAdsInfo>) manager.findAll(HomeAdsInfo.class);
			homeAdsData.setAdsList(homeAdsInfos);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return homeAdsData;

	}
	public static void removeAllCurrentHomeAdsData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(HomeAdsInfo.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}

	/*****************TabIndicator.class*********************/

	// 保存底部导航栏的信息
	public static void saveTabIndicatorData(TabIndicatorData tabIndicatorData) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<TabIndicator> tabIndicators = null;
		try {
			tabIndicators = tabIndicatorData.getIndicators();
			manager.dropTable(TabIndicator.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(tabIndicators);

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	// 获取导航栏的信息
	public static TabIndicatorData getCurrentTabIndicatorData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		TabIndicatorData tabIndicatorData = new TabIndicatorData();
		ArrayList<TabIndicator> tabIndicators = null;
		try {
			tabIndicators = (ArrayList<TabIndicator>) manager.findAll(TabIndicator.class);
			tabIndicatorData.setIndicators(tabIndicators);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return tabIndicatorData;
	}
	public static void removeAllCurrentTabIndicatorData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(TabIndicator.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}
	/*****************LotteryType.class*********************/
	public static LotteryTypeData getCurrentLotteryTypeData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		LotteryTypeData lotteryTypeData = new LotteryTypeData();
		ArrayList<LotteryType> lotteryTypes = null;
		try {
			lotteryTypes = (ArrayList<LotteryType>) manager.findAll(LotteryType.class);
			lotteryTypeData.setLotteryList(lotteryTypes);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return lotteryTypeData;

	}
	public static void saveLotteryTypeData(LotteryTypeData lotteryTypeData) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<LotteryType> lotteryList = null;
		try {
			lotteryList = lotteryTypeData.getLotteryList();
			manager.dropTable(LotteryType.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(lotteryList);

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeAllCurrentLotteryTypeData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(LotteryType.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}


	/*****************User.class*********************/
	public static User getCurrentUser() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		User user = null;
		try {
			user = manager.findFirst(User.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	public static void saveUser(User user) {
		User currentUser = getCurrentUser();
		if (user != null) {
			try {
				DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
				if (currentUser != null) {
					manager.dropTable(User.class);
				}
				manager.save(user);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void removeAllUser() {
		if (getCurrentUser()!=null) {
			DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
			try {  
				manager.dropTable(User.class);	
			} catch (DbException e) {  
				e.printStackTrace();  
			}
		}
	}


	/*****************UserInfo.class*********************/
	public static UserInfo getCurrentUserInfo() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		UserInfo userInfo = null;
		try {
			userInfo = manager.findFirst(UserInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return userInfo;
	}
	public static void removeAllUserInfo() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		if (getCurrentUserInfo()!=null) {
			try {
				manager.dropTable(UserInfo.class);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void saveUserInfo(UserInfo userInfo) {
		if (userInfo != null) {
			DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
			try {
				manager.dropTable(UserInfo.class);
				manager.save(userInfo);
			} catch (DbException e) {
				e.printStackTrace();
			}
			if (!TextUtils.isEmpty(userInfo.getMobile())) {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasBindMobile", true);
				PreferenceUtil.putString(CaiPiaoApplication.getmContext(),
						"bindMobile", userInfo.getMobile());
			} else {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasBindMobile", false);
				PreferenceUtil.putString(CaiPiaoApplication.getmContext(),
						"bindMobile", "");
			}
			if (!TextUtils.isEmpty(userInfo.getRealName())
					&&!TextUtils.isEmpty(userInfo.getIdCard())) {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasRealName", true);
			} else {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasRealName", false);
			}
			if (!TextUtils.isEmpty(userInfo.getBankCard())) {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasBindBankCard", true);
			} else {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasBindBankCard", false);
			}
			if (userInfo.getHasPayPassword()) {
				PreferenceUtil.putBoolean(
						CaiPiaoApplication.getmContext(),
						"hasPayPassword",true);
			} else {
				PreferenceUtil.putBoolean(CaiPiaoApplication.getmContext(),
						"hasPayPassword", false);
			}
		


		}
	}
	/*****************资讯包历史记录history.class*********************/
	public static ArrayList<NewslistBean> getCurrentHistoryData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<NewslistBean> messages = null;
		try {
			messages = (ArrayList<NewslistBean>) manager.findAll(NewslistBean.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return messages;

	}
	public static void saveHistorySigleData(NewslistBean message) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.save(message);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void saveHistoryData(ArrayList<NewslistBean> messages) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.dropTable(NewslistBean.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(messages);

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeHistorySigleData(NewslistBean message) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.delete(NewslistBean.class,  
					WhereBuilder.b("id", "=", message.getId()));
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeAllCurrentHistoryData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(NewslistBean.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}
	/*******************ScoreBean*******************/
	/*public static void saveScoreBeanEntity(ScoreBean data) {
		if (data == null ) {
			return;
		}
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.save(data);;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ScoreBean  findScoreBeanEntity(String matchKey) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		List<ScoreBean> findChild = null ;
		try {
			findChild = manager.selector(ScoreBean.class).where("matchKey", "=", matchKey).findAll();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (findChild == null || findChild.size()==0) {
			return null;  
		}
		return findChild.get(0);  
	}
	public static void  deleteScoreBeanEntity(ScoreBean entity) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.delete(entity);
		} catch (DbException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
		return ;  
	}
	public static List<ScoreBean>  findScoreBeanAll() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		List<ScoreBean> list = null;
		try {
			list = manager.selector(ScoreBean.class).orderBy("matchKey", true).findAll();;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;  
	}*/


	/*******************Version*******************/
	public static void saveCurrentVersion(Version version) {
		Version currentVersion = getCurrentVersion();	
		if (version != null) {
			try {
				DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
				if (currentVersion != null) {
					manager.dropTable(Version.class);
				}
				manager.save(version);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static Version getCurrentVersion() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		Version version = null;
		try {
			version = manager.findFirst(Version.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return version;
	}
	public static void removeCurrentVersion() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(Version.class);	
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}
	// 获取本地版本信息
	public static Map<String, String> getVersion() {
		Context context = CaiPiaoApplication.getmContext();
		Map<String, String> versionMap = new HashMap<String, String>();
		String TabVersion = TextUtils.isEmpty(PreferenceUtil.getString(context,
				"TabVersion")) ? "0" : PreferenceUtil.getString(context,"TabVersion");

		String LotteryTypeVersion = TextUtils.isEmpty(PreferenceUtil.getString(
				context, "LotteryTypeVersion")) ? "0.0" : PreferenceUtil
						.getString(context, "LotteryTypeVersion");

		String LunboVersion = TextUtils.isEmpty(PreferenceUtil.getString(
				context, "LunboVersion")) ? "0.0" : PreferenceUtil.getString(
						context, "LunboVersion");

		String InformationVersion = TextUtils.isEmpty(PreferenceUtil.getString(
				context, "InformationVersion")) ? "0.0" : PreferenceUtil
						.getString(context, "InformationVersion");

		String PreLoadPageVersion = TextUtils.isEmpty(PreferenceUtil.getString(
				context, "PreLoadPageVersion")) ? "0.0" : PreferenceUtil
						.getString(context, "PreLoadPageVersion");

		versionMap.put("TabVersion", TabVersion);
		versionMap.put("LotteryTypeVersion", LotteryTypeVersion);
		versionMap.put("LunboVersion", LunboVersion);
		versionMap.put("InformationVersion", InformationVersion);
		versionMap.put("PreLoadPageVersion", PreLoadPageVersion);//

		return versionMap;

	}

	// 保存版本信息
	public static void saveVersion(Map<String, String> map) {
		Context context = CaiPiaoApplication.getmContext();
		PreferenceUtil.putString(context, "TabVersion", map.get("appversion"));
		PreferenceUtil.putString(context, "LotteryTypeVersion",
				map.get("LotteryType"));
		PreferenceUtil.putString(context, "LunboVersion", map.get("Lunbo"));
		PreferenceUtil.putString(context, "InformationVersion",
				map.get("Information"));
		PreferenceUtil.putString(context, "PreLoadPageVersion",
				map.get("PreLoadPage"));
		String html_version=PreferenceUtil.getString(context, "html_version");
		if (!TextUtils.isEmpty(html_version)) {
			if (!html_version.equals(map.get("html_version"))) {
				PreferenceUtil.putBoolean(context, "hasHtmlTitleChanged", true);
			}else {
				PreferenceUtil.putBoolean(context, "hasHtmlTitleChanged", false);
			}
		}else{
			PreferenceUtil.putString(context, "html_version","0");
		}
		PreferenceUtil.putString(context, "html_version",	map.get("html_version"));
	}
	
	
	/*****************Message.class*********************/
	public static ArrayList<Message> getCurrentMessageData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		ArrayList<Message> messages = null;
		try {
			messages = (ArrayList<Message>) manager.findAll(Message.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return messages;

	}
	public static void saveMessageSigleData(Message message) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.save(message);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void saveMessageData(ArrayList<Message> messages) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.dropTable(Message.class); // 删除以前的 保存现在的数据
			manager.saveBindingId(messages);

		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeMessageSigleData(Message message) {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {
			manager.delete(Message.class,  
					WhereBuilder.b("title", "=", message.getTitle()).and("content", "=", message.getContent()).and("time", "=", message.getTime()));
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void removeAllCurrentMessageData() {
		DbManager manager = x.getDb(CaiPiaoApplication.getConfig());
		try {  
			manager.dropTable(Message.class);
		} catch (DbException e) {  
			e.printStackTrace();  
		}
	}

}
