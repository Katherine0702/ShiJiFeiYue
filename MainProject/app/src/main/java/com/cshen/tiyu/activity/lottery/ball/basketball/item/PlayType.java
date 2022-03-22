package com.cshen.tiyu.activity.lottery.ball.basketball.item;

import android.text.TextUtils;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;





/**
 * 竞彩篮球玩法类型
 * 
 */
public enum PlayType {
	/** 胜负 */
	SF("胜负", ItemSF.values(), 8),

	/** 让分胜负 */
	RFSF("让分胜负", ItemRFSF.values(), 8),

	/** 胜分差 */
	SFC("胜分差", ItemSFC.values(), 4),

	/** 大小分 */
	DXF("大小分", ItemDXF.values(), 8),
	
	/** 混合串 */
	MIX("混合串", null ,4);
	
	/** 玩法名称 */
	private final String text;

	/** 此玩法所能选择的最大场次数目 */
	private final int maxMatchSize;

	private final Item[] allItems;

	private PlayType(String text, Item[] allItems, int maxMatchSize) {
		this.text = text;
		this.maxMatchSize = maxMatchSize;
		this.allItems = allItems;
	}

	/**
	 * @return {@link #text}
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return {@link #maxMatchSize}
	 */
	public int getMaxMatchSize() {
		return maxMatchSize;
	}

	/**
	 * @return {@link #allItems}
	 */
	public Item[] getAllItems() {
		return allItems;
	}

	public static PlayType valueOfName(String name) {
		if (!TextUtils.isEmpty(name)) {
			for (PlayType type : PlayType.values()) {
				if (type.name().equals(name))
					return type;
			}
		}
		return null;
	}
	public Item getItemByItemValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			for (Item item : this.allItems) {
				if(item.getValue().trim().equalsIgnoreCase(value.trim())){
					return item;
				}
			}
		}
		return null;
	}
	
	public static PlayType valueOfOrdinal(int ordinal){
		for (PlayType type : PlayType.values()) {
			if (type.ordinal()==ordinal)
				return type;
		}
		return null;
	}
}
