package com.cshen.tiyu.domain.main;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "LotteryType") 
public class LotteryType { 
	/**  
	 * isId = true 代表该字段是这张表的主键，类型也可以是String (赋值为false就不是主键了)，虽然数据库表可以设置多个主键，但是在实际操作的时候还是设置一个  
	 * autoGen = true 代表主键自增长，如果不是自增长，那就赋值false (这里主键是否自增长很关键，会影响到后面数据库的操作，下面再结合方法来解释)  
	 */  
	@Column(name = "_id",isId = true,autoGen = true)  
	public int _id;  
	@Column(name = "id")
	long id;
	@Column(name = "icon")
	String icon;
	@Column(name = "rank")
	int  rank;
	@Column(name = "title")
	String title;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "info")
	String info;
	String url;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "infoMsg")
	public  String infoMsg;

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}
}
