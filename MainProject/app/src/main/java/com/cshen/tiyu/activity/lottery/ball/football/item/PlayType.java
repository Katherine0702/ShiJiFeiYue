package com.cshen.tiyu.activity.lottery.ball.football.item;

import android.text.TextUtils;

import java.sql.Types;

import com.cshen.tiyu.activity.lottery.ball.domain.Item;



/**
 * 竞彩足球玩法类型
 * 
 */
public enum PlayType {
	/** 胜平负 */
	SPF("胜平负", 8, ItemSPF.values()),

	/** 进球数 */
	JQS("进球数", 6, ItemJQS.values()),

	/** 比分 */
	BF("比分", 4, ItemBF.values()),

	/** 半全场 */
	BQQ("半全场", 4, ItemBQQ.values()),
	
	/** 混合串 */
	MIX("混合串", 4, null),
	
	RQSPF("让球胜平负", 8, ItemSPF.values()),
	
	/** 欧冠杯冠军 */
	UCC("欧冠杯冠军", 32, ItemUCC.values()),
	
	/** 世界杯冠军 */
	WC("世界杯冠军", 32, ItemWC.values()),
	
	/** 欧洲杯冠军 */
	EFC("欧洲杯冠军", 24, ItemEFC.values()),
	
	/** 欧洲杯冠亚军 */
	EFCR("欧洲杯冠亚军", 50, ItemEFCR.values());

	/** 玩法名称 */
	private final String text;

	/** 此玩法所能选择的最大场次数目 */
	private final int maxMatchSize;

	private final Item[] allItems;

	private PlayType(String text, int maxMatchSize, Item[] allItems) {
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

	public static final String SQL_TYPE = "" + Types.TINYINT;

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
