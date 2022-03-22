package com.cshen.tiyu.activity.lottery.ball.football.item;



import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;


public enum ItemEFC implements Item {
	EFC("EFC", "欧洲杯冠军");
	private final String value;
	private final String text;
	private ItemEFC(String value, String text) {
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
	public static ItemEFC valueOfValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (ItemEFC type : ItemEFC.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
}
