package com.cshen.tiyu.activity.lottery.ball.basketball.item;

import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;


/**
 * 竞彩篮球-胜负玩法选项
 * 
 */
public enum ItemSF implements Item {
	/** 主负 */
	LOSE("主负", "0"),

	/** 主胜 */
	WIN("主胜", "3");

	private final String text;
	private final String value;

	private ItemSF(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	/**
	 * 根据值获取对应的类型,找不到对应的类型返回null.
	 */
	public static ItemSF valueOfValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (ItemSF type : ItemSF.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
}
