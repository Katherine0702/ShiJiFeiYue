package com.cshen.tiyu.activity.lottery.ball.basketball.item;





import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;




/**
 * 竞彩篮球-大小分玩法选项
 * 
 */
public enum ItemDXF implements Item {
	/** 大 */
	LARGE("大", "1"),

	/** 小 */
	LITTLE("小", "0");

	private final String text;
	private final String value;

	private ItemDXF(String text, String value) {
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
	public static ItemDXF valueOfValue(String value) {
		  if (!TextUtils.isEmpty(value)) {
			for (ItemDXF type : ItemDXF.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
}
