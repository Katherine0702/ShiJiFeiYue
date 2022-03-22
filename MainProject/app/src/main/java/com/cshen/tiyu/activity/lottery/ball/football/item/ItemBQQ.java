package com.cshen.tiyu.activity.lottery.ball.football.item;

import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;



/**
 * 竞彩足球-半全场玩法选项
 * 
 */
public enum ItemBQQ implements Item {
	/** 胜胜 */
	WIN_WIN("33", "胜胜"),

	/** 胜平 */
	WIN_DRAW("31", "胜平"),

	/** 胜负 */
	WIN_LOSE("30", "胜负"),

	/** 平胜 */
	DRAW_WIN("13", "平胜"),

	/** 平平 */
	DRAW_DRAW("11", "平平"),

	/** 平负 */
	DRAW_LOSE("10", "平负"),

	/** 负胜 */
	LOSE_WIN("03", "负胜"),

	/** 负平 */
	LOSE_DRAW("01", "负平"),

	/** 负负 */
	LOSE_LOSE("00", "负负");

	private final String value;
	private final String text;

	private ItemBQQ(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}/**
	 * 根据值获取对应的类型,找不到对应的类型返回null.
	 */
	public static ItemBQQ valueOfValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (ItemBQQ type : ItemBQQ.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
	
	public static final ItemBQQ[] WINS = { WIN_WIN, DRAW_WIN, LOSE_WIN};
	public static final ItemBQQ[] DRAWS = { WIN_DRAW, DRAW_DRAW, LOSE_DRAW };
	public static final ItemBQQ[] LOSES = { WIN_LOSE, DRAW_LOSE, LOSE_LOSE};
}
