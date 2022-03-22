package com.cshen.tiyu.domain.login;

import java.io.Serializable;

public class UserTime implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;   
	String lastLoginTime;
	String midName;
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	User user;
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
