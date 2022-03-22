package com.cshen.tiyu.domain.main;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "TabIndicator") 
public class TabIndicator implements Serializable {
	/**  
	 * isId = true 代表该字段是这张表的主键，类型也可以是String (赋值为false就不是主键了)，虽然数据库表可以设置多个主键，但是在实际操作的时候还是设置一个  
	 * autoGen = true 代表主键自增长，如果不是自增长，那就赋值false (这里主键是否自增长很关键，会影响到后面数据库的操作，下面再结合方法来解释)  
	 */  
	@Column(name = "_id",isId = true,autoGen = true)  
	public int _id;  
	@Column(name = "id")
	private long id;//id编号
	@Column(name = "rank")
	private int rank;//排列顺序 1 2 3 4 5 
	@Column(name = "title")
	private String title;//tab标题
	@Column(name = "icon_common")
	private String icon_common;//未选中样式地址
	@Column(name = "icon_light")
	private String icon_light;;//选中图片样式地址


	@Column(name = "useLocal")
	String useLocal;//1调用本地
	@Column(name = "lotteryId")
	String lotteryId;//彩种id
	@Column(name = "playType")
	private String playType;
	public int get_id() {  
	    return _id;  
	}  
	  
	public void set_id(int _id) {  
	    this._id = _id;  
	}  
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getUseLocal() {
		return useLocal;
	}
	public void setUseLocal(String useLocal) {
		this.useLocal = useLocal;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	private String url;


	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon_common() {
		return icon_common;
	}

	public void setIcon_common(String icon_common) {
		this.icon_common = icon_common;
	}

	public String getIcon_light() {
		return icon_light;
	}

	public void setIcon_light(String icon_light) {
		this.icon_light = icon_light;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
	
	
	
}
