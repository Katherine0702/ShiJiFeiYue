package com.cshen.tiyu.activity.lottery.ball.football.item;

import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;


public enum ItemEFCR implements Item {
	EFCR("EFCR", "欧洲杯冠亚军");
	private final String value;
	private final String text;
	private ItemEFCR(String value, String text) {
		this.value = value;
		this.text = text;
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
	public static ItemEFCR valueOfValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (ItemEFCR type : ItemEFCR.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
}
