package com.cshen.tiyu.activity.lottery.ball.basketball.item;
import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;


/**
 * 竞彩篮球-让分胜负玩法选项
 * 
 */
public enum ItemRFSF implements Item {
	/** 主负 */
	SF_LOSE("主负", "0"),

	/** 主胜 */
	SF_WIN("主胜", "3");

	private final String text;
	private final String value;

	private ItemRFSF(String text, String value) {
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
	public static ItemRFSF valueOfValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (ItemRFSF type : ItemRFSF.values()) {
				if (type.getValue().equals(value))
					return type;
			}
		}
		return null;
	}
}
