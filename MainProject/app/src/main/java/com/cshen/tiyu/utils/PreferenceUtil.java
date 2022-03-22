package com.cshen.tiyu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户资料\数据工具类
 * <p>
 * 利用ShardPreferences存取用户的资料
 * </p>
 */
public class PreferenceUtil {

	public static String prefName = "caipiao";
	public static final String PRIZE = "PRIZE";//中奖通知
	public static final String FOLLOWORDER = "FOLLOWORDER";//跟单通知
	public static final String IFPRIZE = "IFPRIZE";//开奖通知
	public static final String FOOTBALL = "FOOTBALL";//竞彩足球
	public static final String DOUBLECOLOR = "DOUBLECOLOR";//双色球
	public static final String HAPPY = "HAPPY";//大乐透
	public static final String SSQPRIZEPERIOD = "SSQPRIZEPERIOD";//双色球开奖期号
	public static final String DLTPRIZEPERIOD = "DLTPRIZEPERIOD";//大乐透开奖期号
	public static final String PL5PRIZEPERIOD = "PL5PRIZEPERIOD";//排列5开奖期号
	public static final String PRIZEPERIOD115 = "PRIZEPERIOD115";//115开奖期号
	public static final String PRIZEPERIOD115GD = "PRIZEPERIOD115GD";//GD115开奖期号
	public static final String PERIODSFC = "PERIODSFC";//胜负彩期次
	public static final String PRIZEPERIODFAST3 = "PRIZEPERIODFAST3";//115开奖期号

	public static final String PERIOD115NUMBERWANFAN = "PERIOD115NUMBERWANFAN";//115玩法
	public static final String PERIOD115NUMBERDANTUO = "PERIOD115NUMBERDANTUO";//115胆拖
	
	public static final String Fast3NUMBERWANFAN = "Fast3NUMBERWANFAN";//115玩法
	public static final String Fast3NUMBERDANTUO = "Fast3NUMBERDANTUO";//115胆拖
	
    /**
     * 由数据的key得到单个用户字符串数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个字符串数据
     */
    public static String getString(Context context, String prefName, String key) {
        return key == null ? "" : getPreference(context, prefName).getString(key, "");
    }

    public static String getString(Context context, String key) {
        return key == null ? "" : getPreference(context, prefName).getString(key, "");
    }

    /**
     * 将用户字符串数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putString(Context context, String prefName, String key, String value) {
        if (key != null) {
            getPreference(context, prefName).edit().putString(key, value).commit();
        }
    }

    public static void putString(Context context, String key, String value) {
        if (key != null) {
            getPreference(context, prefName).edit().putString(key, value).commit();
        }
    }

    /**
     * 由数据的key得到单个用户整型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个字符串数据
     */
    public static int getInt(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }

    public static int getInt(Context context, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }
    public static int getInt(Context context, String key,int defaultInt) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, defaultInt);
    }
    /**
     * 将用户整型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putInt(Context context, String prefName, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }
    }

    public static void putInt(Context context, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }
    }

    /**
     * 由数据的key得到单个用户浮点型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个浮点型数据
     */
    public static float getFloat(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getFloat(key, 0F);
    }

    public static float getFloat(Context context, String key) {
        return key == null ? 0 : getPreference(context, prefName).getFloat(key, 0F);
    }

    /**
     * 将用户浮点型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putFloat(Context context, String prefName, String key, float value) {
        if (key != null) {
            getPreference(context, prefName).edit().putFloat(key, value).commit();
        }
    }

    public static void putFloat(Context context, String key, float value) {
        if (key != null) {
            getPreference(context, prefName).edit().putFloat(key, value).commit();
        }
    }

    public static void putLong(Context context, String prefName, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }
    }

    public static void putLong(Context context, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }
    }

    public static long getLong(Context context, String prefName, String key) {
        return key == null ? 0l : getPreference(context, prefName).getLong(key, 0l);
    }

    public static long getLong(Context context, String key) {
        return key == null ? 0l : getPreference(context, prefName).getLong(key, 0l);
    }

    /**
     * 由数据的key得到单个用户布尔型数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @return 一个布尔型数据
     */
    public static boolean getBoolean(Context context, String prefName, String key) {
        return key == null ? false : getPreference(context, prefName).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key) {
        return key == null ? false : getPreference(context, prefName).getBoolean(key, false);
    }

    /**
     * 将用户布尔型数据写入到sharePreferences中
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     * @param value    数据的值
     */
    public static void putBoolean(Context context, String prefName, String key, boolean value) {
        if (key != null) {
            getPreference(context, prefName).edit().putBoolean(key, value).commit();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (key != null) {
            getPreference(context, prefName).edit().putBoolean(key, value).commit();
        }
    }

    /**
     * 删除数据
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     * @param key      数据的键
     */
    public static void remove(Context context, String prefName, String key) {
        if (key != null) {
            getPreference(context, prefName).edit().remove(key).commit();
        }
    }

    public static void remove(Context context, String key) {
        if (key != null) {
            getPreference(context, prefName).edit().remove(key).commit();
        }
    }

    /**
     * 清除文件中的所有内容
     *
     * @param context  上下文对象,用以取得shardPreferences
     * @param prefName shardPreferences文件名
     */
    public static void clearAll(Context context, String prefName) {
        getPreference(context, prefName).edit().clear().commit();
    }

    /**
     * 通过上下文对象得到sharePreferences对象
     *
     * @param context  上下文对象
     * @param prefName sharePreferences文件名
     * @return sharePreferences对象
     */
    private static SharedPreferences getPreference(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
    public static void clearLoginAll(Context context){
		PreferenceUtil.putString(context, "bindMobile", "");
		PreferenceUtil.putBoolean(context, "hasPayPassword",false);
		PreferenceUtil.putBoolean(context, "hasLogin", false);
		PreferenceUtil.putBoolean(context, "isExitWay", false);
		PreferenceUtil.putBoolean(context, "hasBindBankCard",false);
		PreferenceUtil.putBoolean(context, "hasBindMobile", false);
		PreferenceUtil.putBoolean(context, "hasRealName", false);
		PreferenceUtil.putBoolean(context, "hasHtmlTitleChanged", false);
		PreferenceUtil.putBoolean(context, "hasSafeCertification", false);
		PreferenceUtil.putInt(context, "provinceId",-1);
		PreferenceUtil.putInt(context, "cityId",-1);
		PreferenceUtil.putBoolean(context, PreferenceUtil.PRIZE,false);
		PreferenceUtil.putBoolean(context, PreferenceUtil.FOLLOWORDER,false);
		PreferenceUtil.putBoolean(context, PreferenceUtil.IFPRIZE,false);
    }
    public static void clearAll(Context context){
    	clearLoginAll(context);
		PreferenceUtil.putBoolean(context, PreferenceUtil.FOOTBALL,false);
		PreferenceUtil.putBoolean(context, PreferenceUtil.DOUBLECOLOR,false);
		PreferenceUtil.putBoolean(context, PreferenceUtil.HAPPY,false);
		PreferenceUtil.putString(context, "html_version","0");
		PreferenceUtil.putBoolean(context,"is_user_guide_showed", false);
		PreferenceUtil.putString(context, "TabVersion","");
		PreferenceUtil.putString(context, "LotteryTypeVersion","");
		PreferenceUtil.putString(context, "LunboVersion", "");
		PreferenceUtil.putString(context, "InformationVersion","");
		PreferenceUtil.putString(context, "PreLoadPageVersion","");
    }
}
