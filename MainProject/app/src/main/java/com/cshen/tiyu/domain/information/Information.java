package com.cshen.tiyu.domain.information;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "Information") 
public class Information {
	/**  
	 * isId = true 代表该字段是这张表的主键，类型也可以是String (赋值为false就不是主键了)，虽然数据库表可以设置多个主键，但是在实际操作的时候还是设置一个  
	 * autoGen = true 代表主键自增长，如果不是自增长，那就赋值false (这里主键是否自增长很关键，会影响到后面数据库的操作，下面再结合方法来解释)  
	 */  
	@Column(name = "_id",isId = true,autoGen = true)  
	public int _id;  
	@Column(name = "id")
	String id;
	@Column(name = "title")
	String title;
	@Column(name = "clickable")
	boolean clickable;
	public int get_id() {  
	    return _id;  
	}  
	  
	public void set_id(int _id) {  
	    this._id = _id;  
	}  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}


}
